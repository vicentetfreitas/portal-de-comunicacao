--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V902__application_user_sequences.sql
-- Versão  : 1.0
--
-- Objetivo
--   Conceder SELECT em sequences do owner para UNMPORTCOM_APP_ROLE
--   (necessário para JPA GenerationType.SEQUENCE / NEXTVAL).
--
-- Executar como
--   UNMPORTCOM
--
-- Referência
--   database/ddl/002-create-sequences.sql (12 sequences baseline)
--   database/migrations/V003__auth_sessao_and_colaborador_zimbra.sql
--   DEC-DB-024
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT V902 — Application user grants (sequences)
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- Baseline (002-create-sequences.sql)
--------------------------------------------------------------------------------

GRANT SELECT ON UNMPORTCOM.SQ_FEDERACAO_COD_FEDERACAO     TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_SINGULAR_COD_SINGULAR       TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_AREA_COD_AREA               TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_EQUIPE_COD_EQUIPE           TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_COLABORADOR                 TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_ONBOARD_SOLIC               TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_DOCUMENTO_COD_DOCUMENTO     TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_REG_AUDIT_COD_REG_AUDIT     TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_AUTH_SESSAO                 TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_COMUNICADO_COD_COMUNICADO   TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_NOTIFICACAO_COD_NOTIFICACAO TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON UNMPORTCOM.SQ_CONFIG_PORT_COD_CONFIG_PORT TO UNMPORTCOM_APP_ROLE;

--------------------------------------------------------------------------------
-- Brownfield / nomenclatura JPA (conceder se existir no ambiente)
--------------------------------------------------------------------------------

BEGIN
    FOR r IN (
        SELECT sequence_name
          FROM user_sequences
         WHERE sequence_name IN (
             'SQ_AUTH_SESSAO_COD_SESSAO',
             'SQ_COLABORADOR_COD_COLABORADOR'
         )
    ) LOOP
        EXECUTE IMMEDIATE 'GRANT SELECT ON UNMPORTCOM.' || r.sequence_name || ' TO UNMPORTCOM_APP_ROLE';
        DBMS_OUTPUT.PUT_LINE('GRANT SELECT em ' || r.sequence_name || ' concedido.');
    END LOOP;
END;
/

--------------------------------------------------------------------------------
-- Validação (owner)
--------------------------------------------------------------------------------

PROMPT.
PROMPT Sequences com privilégio na role:
PROMPT.

SELECT table_name AS sequence_name, privilege
  FROM user_tab_privs
 WHERE grantee = 'UNMPORTCOM_APP_ROLE'
   AND table_name LIKE 'SQ_%'
 ORDER BY table_name;

PROMPT.
PROMPT V902 concluído.
PROMPT.
