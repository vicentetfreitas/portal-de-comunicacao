--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : VAL-DB-03-verify-documento-upload-prereqs.sql
-- Versão  : 1.0
--
-- Objetivo
--   Verificação read-only ANTES de aplicar V009 (FT-DOCUMENTO-UPLOAD /
--   TK-DOC-UPLOAD-001). Não altera nada.
--
-- Executar como
--   UNMPORTCOM
--------------------------------------------------------------------------------

SET SERVEROUTPUT ON
COLUMN item          FORMAT A45
COLUMN situacao      FORMAT A50

PROMPT ==========================================================
PROMPT VAL-DB-03 — Pré-condições de V009
PROMPT ==========================================================

PROMPT.
PROMPT 1. Tabelas alvo devem existir (esperado: 4):
PROMPT.
SELECT table_name
  FROM user_tables
 WHERE table_name IN ('ARQUIVO_BINARIO', 'DOCUMENTO_VERSAO', 'DOCUMENTO', 'CATEGORIA_DOCUMENTAL')
 ORDER BY table_name;

PROMPT.
PROMPT 2. Sequences que V009 vai criar (esperado antes de V009: 0 linhas):
PROMPT.
SELECT sequence_name
  FROM user_sequences
 WHERE sequence_name IN ('SQ_ARQUIVO_BINARIO', 'SQ_DOCUMENTO_VERSAO', 'SQ_CAT_DOC_COD_CAT_DOC')
 ORDER BY sequence_name;

PROMPT.
PROMPT 3. Conteúdo atual de CATEGORIA_DOCUMENTAL:
PROMPT.
SELECT nom_categoria, flg_ativo
  FROM CATEGORIA_DOCUMENTAL
 ORDER BY flg_ativo DESC, nom_categoria;

PROMPT.
PROMPT 4. Role da aplicação (DEC-DB-024) deve existir:
PROMPT.
SELECT role
  FROM dba_roles
 WHERE role = 'UNMPORTCOM_APP_ROLE'
 UNION ALL
 SELECT granted_role
  FROM user_role_privs
 WHERE granted_role = 'UNMPORTCOM_APP_ROLE';

PROMPT.
PROMPT 5. FK DOCUMENTO -> CATEGORIA_DOCUMENTAL e coluna NOT NULL (contexto):
PROMPT.
SELECT column_name, nullable
  FROM user_tab_columns
 WHERE table_name = 'DOCUMENTO'
   AND column_name = 'COD_CATEGORIA_DOCUMENTAL';

PROMPT.
PROMPT Interpretacao:
PROMPT  - (1) 4 tabelas: OK para prosseguir.
PROMPT  - (2) 0 linhas: V009 criara as 3 sequences. Se ja existirem, V009 as mantem.
PROMPT  - (3) vazia (ou so historicas): V009 insere Documentos/Imagens/Videos/Outros
PROMPT        e desativa Normativos/Manuais/Politicas/Procedimentos/Comunicados.
PROMPT  - (5) COD_CATEGORIA_DOCUMENTAL = N (NOT NULL): confirma a necessidade do DML.
PROMPT ==========================================================
