--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V009__documento_upload_sequences_e_categorias.sql
-- Versão  : 2.0
--
-- Objetivo
--   Brownfield FT-DOCUMENTO-UPLOAD (TK-DOC-UPLOAD-001): habilitar a escrita em
--   Gestão Documental. Hoje o backend só LÊ essas tabelas; para o upload criar
--   DOCUMENTO / DOCUMENTO_VERSAO / ARQUIVO_BINARIO faltam duas sequences, e
--   CATEGORIA_DOCUMENTAL (FK NOT NULL de DOCUMENTO) está vazia.
--
-- Escopo (mínimo — o resto evolui junto com TK-DOC-UPLOAD-002)
--   1. SQ_ARQUIVO_BINARIO   -> ARQUIVO_BINARIO.COD_ARQUIVO_BINARIO
--   2. SQ_DOCUMENTO_VERSAO  -> DOCUMENTO_VERSAO.COD_DOCUMENTO_VERSAO
--      + GRANT SELECT das duas para UNMPORTCOM_APP_ROLE (DEC-DB-024 / JPA SEQUENCE)
--   3. 4 linhas em CATEGORIA_DOCUMENTAL — taxonomia por tipo de mídia (DEC-CMS-002):
--        Documentos | Imagens | Vídeos | Outros
--      A categoria de cada DOCUMENTO passa a ser derivada do TIP_MIME no Backend
--      (specs/features/documento-upload/specification.md § Categorização por tipo de mídia).
--
-- Fora do escopo (não é preciso agora)
--   - SQ_CAT_DOC_COD_CAT_DOC: a aplicação nunca insere categoria (CategoriaDocumentalEntity
--     não tem @GeneratedValue). As 4 linhas abaixo usam ID explícito. O seed
--     ddl/008-initial-data.sql ainda referencia essa sequence — 008 só é usado
--     em greenfield e precisa ser reconciliado com DEC-CMS-002 à parte.
--   - SQ_DOCUMENTO_COD_DOCUMENTO: já existe.
--
-- Banco       : Oracle Database 11g+
-- Executar como: UNMPORTCOM  (SQL Developer / DBeaver — comandos simples)
-- Pré-check   : VAL-DB-03-verify-documento-upload-prereqs.sql
--
-- Decisões: DEC-CMS-002, DEC-013, DEC-DB-018, DEC-DB-019, DEC-DB-024
--------------------------------------------------------------------------------


-- 1. Sequences -----------------------------------------------------------------

CREATE SEQUENCE SQ_ARQUIVO_BINARIO  START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_DOCUMENTO_VERSAO START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;


-- 2. Grants (DEC-DB-024 — role da aplicação usa NEXTVAL via JPA) --------------

GRANT SELECT ON UNMPORTCOM.SQ_ARQUIVO_BINARIO  TO UNMPORTCOM_APP_ROLE;

GRANT SELECT ON UNMPORTCOM.SQ_DOCUMENTO_VERSAO TO UNMPORTCOM_APP_ROLE;


-- 3. Categorias por tipo de mídia (DEC-CMS-002) ------------------------------
--    CATEGORIA_DOCUMENTAL está vazia (confirmado no ambiente). IDs explícitos.

INSERT INTO CATEGORIA_DOCUMENTAL (COD_CATEGORIA_DOCUMENTAL, NOM_CATEGORIA, DSC_CATEGORIA, FLG_ATIVO)
VALUES (1, 'Documentos', 'Arquivos textuais e ofimáticos (PDF, Word, Excel, PowerPoint, texto, CSV, ODF).', 'S');

INSERT INTO CATEGORIA_DOCUMENTAL (COD_CATEGORIA_DOCUMENTAL, NOM_CATEGORIA, DSC_CATEGORIA, FLG_ATIVO)
VALUES (2, 'Imagens', 'Arquivos de imagem (PNG, JPEG, GIF, SVG, WEBP e afins).', 'S');

INSERT INTO CATEGORIA_DOCUMENTAL (COD_CATEGORIA_DOCUMENTAL, NOM_CATEGORIA, DSC_CATEGORIA, FLG_ATIVO)
VALUES (3, 'Vídeos', 'Arquivos de vídeo (MP4, WEBM, MOV, AVI e afins).', 'S');

INSERT INTO CATEGORIA_DOCUMENTAL (COD_CATEGORIA_DOCUMENTAL, NOM_CATEGORIA, DSC_CATEGORIA, FLG_ATIVO)
VALUES (4, 'Outros', 'Demais tipos de arquivo não classificados como Documentos, Imagens ou Vídeos.', 'S');

COMMIT;


-- 4. Conferência (rodar depois de aplicar) -----------------------------------

-- sequences (esperado: 2 linhas)
SELECT sequence_name, cache_size, last_number
  FROM user_sequences
 WHERE sequence_name IN ('SQ_ARQUIVO_BINARIO', 'SQ_DOCUMENTO_VERSAO')
 ORDER BY sequence_name;

-- grants (esperado: 2 linhas, privilege = SELECT)
SELECT table_name AS sequence_name, privilege
  FROM user_tab_privs
 WHERE grantee = 'UNMPORTCOM_APP_ROLE'
   AND table_name IN ('SQ_ARQUIVO_BINARIO', 'SQ_DOCUMENTO_VERSAO')
 ORDER BY table_name;

-- categorias (esperado: 4 linhas ativas)
SELECT cod_categoria_documental, nom_categoria, flg_ativo
  FROM CATEGORIA_DOCUMENTAL
 ORDER BY cod_categoria_documental;
