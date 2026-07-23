--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V900__application_user_grants.sql
-- Versão  : 1.0
--
-- Objetivo
--   Conceder privilégios DML do schema owner UNMPORTCOM à role da aplicação.
--
-- Modelo
--   UNMPORTCOM (owner) → UNMPORTCOM_APP_ROLE → UNMPORTCOM_APP (Spring Boot)
--
-- Executar como
--   UNMPORTCOM
--
-- Pré-requisitos
--   ddl/001-create-users.sql (role UNMPORTCOM_APP_ROLE e usuário UNMPORTCOM_APP)
--   Objetos de tabela existentes (003-create-tables.sql e migrations aplicadas)
--
-- Idempotência
--   GRANT repetido é seguro no Oracle.
--
-- Referência
--   DEC-DB-024 — docs/architecture/decisions/DEC-DB-024-application-user-strategy.md
--   Inventário: database/ddl/003-create-tables.sql (23 tabelas)
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT V900 — Application user grants (tabelas)
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- Tabelas — baseline homologado (23)
--------------------------------------------------------------------------------

GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.FEDERACAO               TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.SINGULAR                TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.ENDERECO                TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.CONTATO                 TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.AREA                    TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.EQUIPE                  TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.COLABORADOR             TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.ONBOARDING_SOLICITACAO  TO UNMPORTCOM_APP_ROLE;

GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.CATEGORIA_DOCUMENTAL    TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.PASTA                   TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.DOCUMENTO               TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.DOCUMENTO_VERSAO        TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.ARQUIVO_BINARIO         TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.COMPARTILHAMENTO        TO UNMPORTCOM_APP_ROLE;

GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.PAPEL                   TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.AUTH_SESSAO             TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.PAPEL_ATRIBUICAO        TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.PERMISSAO_PASTA         TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.SOLICITACAO_PERMISSAO   TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.REGISTRO_AUDITORIA      TO UNMPORTCOM_APP_ROLE;

GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.COMUNICADO              TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.NOTIFICACAO             TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.CONFIGURACAO_PORTAL     TO UNMPORTCOM_APP_ROLE;

--------------------------------------------------------------------------------
-- Role → usuário da aplicação
--------------------------------------------------------------------------------

GRANT UNMPORTCOM_APP_ROLE TO UNMPORTCOM_APP;

--------------------------------------------------------------------------------
-- Validação (owner)
--------------------------------------------------------------------------------

PROMPT.
PROMPT Privilégios na role (amostra USER_TAB_PRIVS do owner):
PROMPT.

SELECT table_name, privilege
  FROM user_tab_privs
 WHERE grantee = 'UNMPORTCOM_APP_ROLE'
 ORDER BY table_name, privilege;

PROMPT.
PROMPT V900 concluído.
PROMPT.
