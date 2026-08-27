--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V009__documento_upload_sequences_e_categorias.sql
-- Versão  : 1.0
--
-- Objetivo
--   Brownfield FT-DOCUMENTO-UPLOAD (TK-DOC-UPLOAD-001): habilitar a escrita em
--   Gestão Documental, hoje bloqueada por artefatos ausentes no schema:
--
--     1. Sequences inexistentes (nunca precisaram — FT-DOCUMENTO só lia):
--          SQ_ARQUIVO_BINARIO      -> ARQUIVO_BINARIO.COD_ARQUIVO_BINARIO
--          SQ_DOCUMENTO_VERSAO     -> DOCUMENTO_VERSAO.COD_DOCUMENTO_VERSAO
--          SQ_CAT_DOC_COD_CAT_DOC  -> CATEGORIA_DOCUMENTAL.COD_CATEGORIA_DOCUMENTAL
--        (esta última é REFERENCIADA por ddl/008-initial-data.sql mas nunca foi
--         criada em ddl/002-create-sequences.sql — 008 estava quebrado.)
--
--     2. GRANT SELECT nas 3 sequences para UNMPORTCOM_APP_ROLE
--        (DEC-DB-024 — JPA GenerationType.SEQUENCE / NEXTVAL).
--
--     3. Taxonomia de CATEGORIA_DOCUMENTAL por TIPO DE MÍDIA (DEC-CMS-002):
--          Documentos | Imagens | Vídeos | Outros
--        A tabela está VAZIA no ambiente atual (Oracle TST, verificado 2026-08-27).
--        A categoria de um DOCUMENTO passa a ser derivada do TIP_MIME no Backend
--        (specs/features/documento-upload/specification.md § Categorização por tipo de mídia).
--
-- Decisões relacionadas
--   DEC-CMS-002 (Comunicado é publicação do CMS; CATEGORIA_DOCUMENTAL = tipo de mídia)
--   DEC-013     (Object Storage S3 para binários)
--   DEC-DB-018  (sem DEFAULT para PK; PK via SQ_<TABELA>_<CAMPO>.NEXTVAL)
--   DEC-DB-019  (schema administrado pelo DBA; sem Flyway — script executado manualmente)
--   DEC-DB-024  (UNMPORTCOM_APP_ROLE — grants de sequence)
--
-- Banco
--   Oracle Database 11g+
--
-- Executar como
--   UNMPORTCOM
--
-- Idempotência
--   Sequences: criadas só se ausentes (checagem em USER_SEQUENCES).
--   Grants: idempotentes por natureza.
--   Categorias: MERGE por NOM_CATEGORIA (mesmo padrão de 008-initial-data.sql).
--
-- Greenfield (000-install.sql)
--   NÃO aplicar V009 diretamente após 000-install. Em vez disso o baseline
--   precisa ser reconciliado com DEC-CMS-002 (itens marcados "GREENFIELD"
--   abaixo) — ver database/migrations/README.md § V009.
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT V009 — FT-DOCUMENTO-UPLOAD: sequences + categorias de mídia
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- 1. Sequences ausentes (cria só se não existir)
--------------------------------------------------------------------------------

DECLARE
    PROCEDURE criar_sequence(p_nome IN VARCHAR2) IS
        v_existe NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_existe
          FROM user_sequences
         WHERE sequence_name = p_nome;

        IF v_existe = 0 THEN
            EXECUTE IMMEDIATE
                'CREATE SEQUENCE ' || p_nome ||
                ' START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE';
            DBMS_OUTPUT.PUT_LINE('Sequence criada: ' || p_nome);
        ELSE
            DBMS_OUTPUT.PUT_LINE('Sequence já existe (mantida): ' || p_nome);
        END IF;
    END;
BEGIN
    criar_sequence('SQ_ARQUIVO_BINARIO');
    criar_sequence('SQ_DOCUMENTO_VERSAO');
    criar_sequence('SQ_CAT_DOC_COD_CAT_DOC');
END;
/

--------------------------------------------------------------------------------
-- 2. GRANT SELECT nas sequences para a role da aplicação (DEC-DB-024)
--------------------------------------------------------------------------------

GRANT SELECT ON UNMPORTCOM.SQ_ARQUIVO_BINARIO     TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_DOCUMENTO_VERSAO    TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_CAT_DOC_COD_CAT_DOC TO UNMPORTCOM_APP_ROLE;

--------------------------------------------------------------------------------
-- 3. Categorias documentais por tipo de mídia (DEC-CMS-002)
--    Idempotente (MERGE por NOM_CATEGORIA). PK via SQ_CAT_DOC_COD_CAT_DOC.NEXTVAL.
--------------------------------------------------------------------------------

MERGE INTO CATEGORIA_DOCUMENTAL c
USING (
    SELECT 'Documentos' AS NOM_CATEGORIA,
           'Arquivos textuais e ofimáticos (PDF, Word, Excel, PowerPoint, texto, CSV, ODF).' AS DSC_CATEGORIA,
           'S' AS FLG_ATIVO FROM DUAL
    UNION ALL
    SELECT 'Imagens',
           'Arquivos de imagem (PNG, JPEG, GIF, SVG, WEBP e afins).',
           'S' FROM DUAL
    UNION ALL
    SELECT 'Vídeos',
           'Arquivos de vídeo (MP4, WEBM, MOV, AVI e afins).',
           'S' FROM DUAL
    UNION ALL
    SELECT 'Outros',
           'Demais tipos de arquivo não classificados como Documentos, Imagens ou Vídeos.',
           'S' FROM DUAL
) src
ON (c.NOM_CATEGORIA = src.NOM_CATEGORIA)
WHEN NOT MATCHED THEN
    INSERT (
        COD_CATEGORIA_DOCUMENTAL,
        NOM_CATEGORIA,
        DSC_CATEGORIA,
        FLG_ATIVO
    )
    VALUES (
        SQ_CAT_DOC_COD_CAT_DOC.NEXTVAL,
        src.NOM_CATEGORIA,
        src.DSC_CATEGORIA,
        src.FLG_ATIVO
    )
WHEN MATCHED THEN
    UPDATE SET
        c.DSC_CATEGORIA    = src.DSC_CATEGORIA,
        c.FLG_ATIVO        = src.FLG_ATIVO,
        c.DAT_ATUALIZACAO  = SYSTIMESTAMP;

--------------------------------------------------------------------------------
-- 3b. Taxonomia histórica abandonada (DEC-CMS-002)
--     'Comunicado' passa a ser publicação do CMS (WordPress), não categoria.
--     As categorias do seed histórico (ddl/008) são DESATIVADAS onde existirem
--     (nenhum DOCUMENTO as referencia ainda). No ambiente atual a tabela está
--     vazia — este UPDATE é no-op.
--------------------------------------------------------------------------------

UPDATE CATEGORIA_DOCUMENTAL
   SET FLG_ATIVO = 'N',
       DAT_ATUALIZACAO = SYSTIMESTAMP
 WHERE NOM_CATEGORIA IN ('Normativos', 'Manuais', 'Políticas', 'Procedimentos', 'Comunicados')
   AND FLG_ATIVO = 'S';

COMMIT;

--------------------------------------------------------------------------------
-- Validação
--------------------------------------------------------------------------------

COLUMN SEQUENCE_NAME FORMAT A30
COLUMN NOM_CATEGORIA FORMAT A20

PROMPT.
PROMPT Sequences de Gestão Documental (esperado: 3):
PROMPT.
SELECT sequence_name, cache_size, last_number
  FROM user_sequences
 WHERE sequence_name IN ('SQ_ARQUIVO_BINARIO', 'SQ_DOCUMENTO_VERSAO', 'SQ_CAT_DOC_COD_CAT_DOC')
 ORDER BY sequence_name;

PROMPT.
PROMPT GRANT SELECT nas sequences para UNMPORTCOM_APP_ROLE (esperado: 3):
PROMPT.
SELECT table_name AS sequence_name, privilege
  FROM user_tab_privs
 WHERE grantee = 'UNMPORTCOM_APP_ROLE'
   AND table_name IN ('SQ_ARQUIVO_BINARIO', 'SQ_DOCUMENTO_VERSAO', 'SQ_CAT_DOC_COD_CAT_DOC')
 ORDER BY table_name;

PROMPT.
PROMPT Categorias ativas (esperado: 4 — Documentos, Imagens, Vídeos, Outros):
PROMPT.
SELECT nom_categoria, flg_ativo
  FROM CATEGORIA_DOCUMENTAL
 ORDER BY flg_ativo DESC, nom_categoria;

PROMPT.
PROMPT V009 concluída.
PROMPT ==========================================================
