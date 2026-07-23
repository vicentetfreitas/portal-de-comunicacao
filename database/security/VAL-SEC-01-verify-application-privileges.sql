--------------------------------------------------------------------------------
-- VAL-SEC-01 — Verificação de privilégios do application user
-- Executar como UNMPORTCOM_APP
-- Referência: DEC-DB-024, database/security/README.md
--------------------------------------------------------------------------------

SET LINESIZE 200
SET PAGESIZE 100

PROMPT ==========================================================
PROMPT VAL-SEC-01 — Privilégios UNMPORTCOM_APP / UNMPORTCOM_APP_ROLE
PROMPT Usuário conectado:
PROMPT ==========================================================

SELECT USER AS connected_user FROM dual;

PROMPT.
PROMPT --- Roles concedidas ao usuário ---
PROMPT.

SELECT granted_role, admin_option, default_role
  FROM user_role_privs
 ORDER BY granted_role;

PROMPT.
PROMPT --- Privilégios em tabelas do owner UNMPORTCOM (via ALL_TAB_PRIVS) ---
PROMPT Esperado: 23 tabelas com SELECT, INSERT, UPDATE, DELETE
PROMPT.

SELECT owner, table_name, privilege
  FROM all_tab_privs
 WHERE owner = 'UNMPORTCOM'
   AND table_name IN (
     'FEDERACAO','SINGULAR','ENDERECO','CONTATO','AREA','EQUIPE','COLABORADOR',
     'ONBOARDING_SOLICITACAO','CATEGORIA_DOCUMENTAL','PASTA','DOCUMENTO',
     'DOCUMENTO_VERSAO','ARQUIVO_BINARIO','COMPARTILHAMENTO','AUTH_SESSAO',
     'PAPEL','PAPEL_ATRIBUICAO','PERMISSAO_PASTA','SOLICITACAO_PERMISSAO',
     'REGISTRO_AUDITORIA','COMUNICADO','NOTIFICACAO','CONFIGURACAO_PORTAL'
   )
   AND grantee IN ('UNMPORTCOM_APP', 'UNMPORTCOM_APP_ROLE')
 ORDER BY table_name, privilege;

PROMPT.
PROMPT --- Contagem de tabelas com pelo menos SELECT ---
PROMPT.

SELECT COUNT(DISTINCT table_name) AS tables_with_select
  FROM all_tab_privs
 WHERE owner = 'UNMPORTCOM'
   AND privilege = 'SELECT'
   AND grantee IN ('UNMPORTCOM_APP', 'UNMPORTCOM_APP_ROLE')
   AND table_name IN (
     'FEDERACAO','SINGULAR','ENDERECO','CONTATO','AREA','EQUIPE','COLABORADOR',
     'ONBOARDING_SOLICITACAO','CATEGORIA_DOCUMENTAL','PASTA','DOCUMENTO',
     'DOCUMENTO_VERSAO','ARQUIVO_BINARIO','COMPARTILHAMENTO','AUTH_SESSAO',
     'PAPEL','PAPEL_ATRIBUICAO','PERMISSAO_PASTA','SOLICITACAO_PERMISSAO',
     'REGISTRO_AUDITORIA','COMUNICADO','NOTIFICACAO','CONFIGURACAO_PORTAL'
   );

PROMPT.
PROMPT --- Sequences (ALL_TAB_PRIVS: object type SEQUENCE) ---
PROMPT.

SELECT owner, table_name AS sequence_name, privilege
  FROM all_tab_privs
 WHERE owner = 'UNMPORTCOM'
   AND table_name LIKE 'SQ_%'
   AND privilege = 'SELECT'
   AND grantee IN ('UNMPORTCOM_APP', 'UNMPORTCOM_APP_ROLE')
 ORDER BY table_name;

PROMPT.
PROMPT --- Teste de leitura (deve retornar sem ORA-00942) ---
PROMPT.

SELECT COUNT(*) AS federacao_rows FROM UNMPORTCOM.FEDERACAO;

PROMPT.
PROMPT Se tables_with_select < 23 ou SELECT em FEDERACAO falhar:
PROMPT   Executar V900 e V902 como UNMPORTCOM.
PROMPT ==========================================================
