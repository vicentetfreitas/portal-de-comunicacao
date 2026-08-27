--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : VAL-DB-03-verify-documento-upload-prereqs.sql
-- Versão  : 2.0
--
-- Objetivo
--   Conferência read-only ANTES de aplicar V009 (FT-DOCUMENTO-UPLOAD).
--   Não altera nada. Executar como UNMPORTCOM.
--------------------------------------------------------------------------------

-- 1. Tabelas alvo (esperado: ARQUIVO_BINARIO, DOCUMENTO_VERSAO, DOCUMENTO, CATEGORIA_DOCUMENTAL)
SELECT table_name
  FROM user_tables
 WHERE table_name IN ('ARQUIVO_BINARIO', 'DOCUMENTO_VERSAO', 'DOCUMENTO', 'CATEGORIA_DOCUMENTAL')
 ORDER BY table_name;

-- 2. Sequences que V009 vai criar (esperado ANTES de V009: 0 linhas)
SELECT sequence_name
  FROM user_sequences
 WHERE sequence_name IN ('SQ_ARQUIVO_BINARIO', 'SQ_DOCUMENTO_VERSAO');

-- 3. Conteúdo atual de CATEGORIA_DOCUMENTAL (esperado: vazio)
SELECT cod_categoria_documental, nom_categoria, flg_ativo
  FROM CATEGORIA_DOCUMENTAL
 ORDER BY cod_categoria_documental;

-- 4. Volume das tabelas de documento (esperado: 0 em todas)
SELECT 'DOCUMENTO'        AS tabela, COUNT(*) AS linhas FROM DOCUMENTO
UNION ALL SELECT 'DOCUMENTO_VERSAO', COUNT(*) FROM DOCUMENTO_VERSAO
UNION ALL SELECT 'ARQUIVO_BINARIO',  COUNT(*) FROM ARQUIVO_BINARIO;

-- 5. Coluna FK obrigatória (esperado: COD_CATEGORIA_DOCUMENTAL nullable = N)
SELECT column_name, nullable
  FROM user_tab_columns
 WHERE table_name = 'DOCUMENTO'
   AND column_name = 'COD_CATEGORIA_DOCUMENTAL';

-- 6. Role da aplicação existe? (esperado: 1 linha)
SELECT role FROM dba_roles WHERE role = 'UNMPORTCOM_APP_ROLE';
