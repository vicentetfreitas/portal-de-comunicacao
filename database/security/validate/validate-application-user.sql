--------------------------------------------------------------------------------
-- Validação — conexão e privilégios UNMPORTCOM_APP (DEC-DB-024 / INFRA-DB-01)
-- Executar conectado como UNMPORTCOM_APP
--------------------------------------------------------------------------------

SET SERVEROUTPUT ON

PROMPT === Usuário de sessão (deve ser UNMPORTCOM_APP) ===
SELECT USER AS session_user FROM DUAL;

PROMPT === Role corporativa ativa ===
SELECT GRANTED_ROLE
  FROM USER_ROLE_PRIVS
 WHERE GRANTED_ROLE = 'UNMPORTCOM_APP_ROLE';

PROMPT === Amostra: acesso DML a tabelas do owner UNMPORTCOM ===
SELECT COUNT(*) AS federacao_readable
  FROM UNMPORTCOM.FEDERACAO
 WHERE ROWNUM = 1;

PROMPT === Amostra: sequence visível ao application user ===
SELECT sequence_name
  FROM all_sequences
 WHERE sequence_owner = 'UNMPORTCOM'
   AND sequence_name = 'SQ_FEDERACAO_COD_FEDERACAO';

PROMPT === Fim da validação — revisar erros ORA-00942 / ORA-01031 ===
