--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 007-create-grants.sql
-- Versão  : 2.0
--
-- Objetivo
--   Conceder privilégios à aplicação via ROLE corporativa.
--
-- Estratégia
--   UNMPORTCOM (owner) -> UNMPORTCOM_APP_ROLE -> UNMPORTCOM_APP
--   Facilita gestão de privilégios e auditoria de segurança.
--
-- Banco
--   Oracle Database 11g+
--
-- Executar como
--   UNMPORTCOM (Owner)
--
-- Dependências
--   001-create-users.sql
--   002-create-sequences.sql
--   003-create-tables.sql
--   004-create-constraints.sql
--   005-create-indexes.sql
--   006-create-comments.sql
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Concessão de Privilégios
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- ROLE da aplicação (criada em 001-create-users.sql)
--------------------------------------------------------------------------------

PROMPT Concedendo privilégios à role UNMPORTCOM_APP_ROLE...

GRANT SELECT, INSERT, UPDATE, DELETE ON FEDERACAO               TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON SINGULAR                TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON ENDERECO                TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON CONTATO                 TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON AREA                    TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON EQUIPE                  TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON COLABORADOR             TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON ONBOARDING_SOLICITACAO  TO UNMPORTCOM_APP_ROLE;

GRANT SELECT, INSERT, UPDATE, DELETE ON CATEGORIA_DOCUMENTAL    TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON PASTA                   TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON DOCUMENTO               TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON DOCUMENTO_VERSAO        TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON ARQUIVO_BINARIO         TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON COMPARTILHAMENTO        TO UNMPORTCOM_APP_ROLE;

GRANT SELECT, INSERT, UPDATE, DELETE ON PAPEL                   TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON AUTH_SESSAO             TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON PAPEL_ATRIBUICAO        TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON PERMISSAO_PASTA         TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON SOLICITACAO_PERMISSAO   TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON REGISTRO_AUDITORIA      TO UNMPORTCOM_APP_ROLE;

GRANT SELECT, INSERT, UPDATE, DELETE ON COMUNICADO              TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON NOTIFICACAO             TO UNMPORTCOM_APP_ROLE;
GRANT SELECT, INSERT, UPDATE, DELETE ON CONFIGURACAO_PORTAL     TO UNMPORTCOM_APP_ROLE;

--------------------------------------------------------------------------------
-- SEQUENCES -> ROLE
-- Nota: PKs geradas via DEFAULT no banco; SELECT necessário para JPA/validação.
--------------------------------------------------------------------------------

GRANT SELECT ON SQ_FEDERACAO_COD_FEDERACAO                                    TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_SINGULAR_COD_SINGULAR                                      TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_ENDERECO_COD_ENDERECO                                      TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_CONTATO_COD_CONTATO                                        TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_AREA_COD_AREA                                              TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_EQUIPE_COD_EQUIPE                                          TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_COLABORADOR_COD_COLABORADOR                                TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_AUTH_SESSAO_COD_SESSAO                                    TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_ONBOARD_SOLIC          TO UNMPORTCOM_APP_ROLE;

GRANT SELECT ON SQ_CAT_DOC_COD_CAT_DOC              TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_PASTA_COD_PASTA                                            TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_DOCUMENTO_COD_DOCUMENTO                                    TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_DOC_VERS_COD_DOC_VERS                      TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_ARQ_BIN_COD_ARQ_BIN                        TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_COMPART_COD_COMPART                      TO UNMPORTCOM_APP_ROLE;

GRANT SELECT ON SQ_PAPEL_COD_PAPEL                                            TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_PAPEL_ATRIB_COD_PAPEL_ATRIB                      TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_PERM_PASTA_COD_PERM_PASTA                        TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_SOLIC_PERM_COD_SOLIC_PERM            TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_REG_AUDIT_COD_REG_AUDIT                TO UNMPORTCOM_APP_ROLE;

GRANT SELECT ON SQ_COMUNICADO_COD_COMUNICADO                                  TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_NOTIFICACAO_COD_NOTIFICACAO                                TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_CONFIG_PORT_COD_CONFIG_PORT                TO UNMPORTCOM_APP_ROLE;

--------------------------------------------------------------------------------
-- ROLE -> USUÁRIO DA APLICAÇÃO
--------------------------------------------------------------------------------

GRANT UNMPORTCOM_APP_ROLE TO UNMPORTCOM_APP;

--------------------------------------------------------------------------------
-- Validação
--------------------------------------------------------------------------------

COLUMN GRANTEE FORMAT A25
COLUMN GRANTED_ROLE FORMAT A25

PROMPT.
PROMPT Privilégios concedidos à role UNMPORTCOM_APP_ROLE:
PROMPT.

SELECT GRANTEE, TABLE_NAME, PRIVILEGE
  FROM USER_TAB_PRIVS
 WHERE GRANTEE = 'UNMPORTCOM_APP_ROLE'
 ORDER BY TABLE_NAME, PRIVILEGE;

PROMPT.
PROMPT Role UNMPORTCOM_APP_ROLE concedida ao usuário UNMPORTCOM_APP.
PROMPT.
PROMPT ==========================================================
PROMPT Grants criados com sucesso.
PROMPT Fim do script 007-create-grants.sql
PROMPT ==========================================================
PROMPT.
