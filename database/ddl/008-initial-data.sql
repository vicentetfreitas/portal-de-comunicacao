--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 008-initial-data.sql
-- Versão  : 2.3
--
-- Objetivo
--   Bootstrap técnico do schema (categorias documentais e papéis).
--   Dados institucionais da Federação: dml/001-federacao.sql (DEC-DB-021).
--   Script idempotente: pode ser executado múltiplas vezes sem duplicar dados.
--
-- Geração de PK
--   Sem DEFAULT nas tabelas (DEC-DB-018). PKs informadas explicitamente via
--   SQ_<TABELA>_<CAMPO>.NEXTVAL neste script.
--
-- Banco
--   Oracle Database 11g+
--
-- Dependências
--   001-create-users.sql
--   002-create-sequences.sql
--   003-create-tables.sql
--   004-create-constraints.sql
--   005-create-indexes.sql
--   006-create-comments.sql
--   007-create-grants.sql
--
-- Executar como
--   UNMPORTCOM
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Bootstrap técnico (categorias e papéis)
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- CATEGORIAS DOCUMENTAIS
--------------------------------------------------------------------------------

MERGE INTO CATEGORIA_DOCUMENTAL c
USING (
    SELECT 'Normativos'    AS NOM_CATEGORIA, 'Normas e regulamentos corporativos.' AS DSC_CATEGORIA, 'S' AS FLG_ATIVO FROM DUAL UNION ALL
    SELECT 'Manuais',       'Manuais operacionais.',                             'S' FROM DUAL UNION ALL
    SELECT 'Políticas',     'Políticas institucionais.',                         'S' FROM DUAL UNION ALL
    SELECT 'Procedimentos', 'Procedimentos operacionais.',                       'S' FROM DUAL UNION ALL
    SELECT 'Comunicados',   'Documentos de comunicação interna.',              'S' FROM DUAL
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
    );

--------------------------------------------------------------------------------
-- PAPÉIS
--------------------------------------------------------------------------------

MERGE INTO PAPEL p
USING (
    SELECT 'ADMINISTRADOR'       AS NOM_PAPEL, 'Administrador do Portal.'                        AS DSC_PAPEL, 'S' AS FLG_ATIVO FROM DUAL UNION ALL
    SELECT 'GESTOR_DOCUMENTAL',   'Responsável pela gestão documental.',             'S' FROM DUAL UNION ALL
    SELECT 'EDITOR',              'Responsável pela publicação de conteúdos.',        'S' FROM DUAL UNION ALL
    SELECT 'COLABORADOR',         'Usuário padrão do Portal.',                       'S' FROM DUAL
) src
ON (p.NOM_PAPEL = src.NOM_PAPEL)
WHEN NOT MATCHED THEN
    INSERT (
        COD_PAPEL,
        NOM_PAPEL,
        DSC_PAPEL,
        FLG_ATIVO
    )
    VALUES (
        SQ_PAPEL_COD_PAPEL.NEXTVAL,
        src.NOM_PAPEL,
        src.DSC_PAPEL,
        src.FLG_ATIVO
    );

COMMIT;

--------------------------------------------------------------------------------
-- Validação
--------------------------------------------------------------------------------

PROMPT.
PROMPT Registros de referência:
PROMPT.

SELECT 'CATEGORIA_DOCUMENTAL' AS ENTIDADE, COUNT(*) AS QTD FROM CATEGORIA_DOCUMENTAL
UNION ALL
SELECT 'PAPEL', COUNT(*) FROM PAPEL;

PROMPT.
PROMPT ==========================================================
PROMPT Dados iniciais carregados com sucesso.
PROMPT Fim do script 008-initial-data.sql
PROMPT ==========================================================
PROMPT.
