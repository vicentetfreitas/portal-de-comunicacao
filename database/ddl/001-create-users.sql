--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 001-create-users.sql
-- Versão  : 2.0
--
-- Objetivo
--   Criar os usuários do banco de dados utilizados pelo Portal de Comunicação.
--
-- Usuários
--   UNMPORTCOM      -> Owner do Schema
--   UNMPORTCOM_APP  -> Usuário utilizado pela aplicação
--
-- Banco
--   Oracle Database 11g+
--
-- Execução
--   Executar como SYS ou outro usuário DBA.
--------------------------------------------------------------------------------

SET DEFINE ON
SET VERIFY OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Criação dos usuários do banco
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- Configuração
--------------------------------------------------------------------------------

DEFINE OWNER_USER = UNMPORTCOM
DEFINE APP_USER   = UNMPORTCOM_APP

PROMPT.
PROMPT Informe a senha do usuário &&OWNER_USER
ACCEPT OWNER_PASSWORD CHAR PROMPT 'Senha: ' HIDE

PROMPT.
PROMPT Informe a senha do usuário &&APP_USER
ACCEPT APP_PASSWORD CHAR PROMPT 'Senha: ' HIDE

--------------------------------------------------------------------------------
-- Criação do OWNER
--------------------------------------------------------------------------------

CREATE USER &&OWNER_USER
IDENTIFIED BY "&&OWNER_PASSWORD"
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON USERS;

--------------------------------------------------------------------------------
-- Criação do usuário da aplicação
--------------------------------------------------------------------------------

CREATE USER &&APP_USER
IDENTIFIED BY "&&APP_PASSWORD"
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP
QUOTA 0 ON USERS;

--------------------------------------------------------------------------------
-- Privilégios do OWNER
--------------------------------------------------------------------------------

GRANT CREATE SESSION   TO &&OWNER_USER;
GRANT CREATE TABLE     TO &&OWNER_USER;
GRANT CREATE VIEW      TO &&OWNER_USER;
GRANT CREATE SEQUENCE  TO &&OWNER_USER;
GRANT CREATE TRIGGER   TO &&OWNER_USER;
GRANT CREATE PROCEDURE TO &&OWNER_USER;
GRANT CREATE TYPE      TO &&OWNER_USER;
GRANT CREATE SYNONYM   TO &&OWNER_USER;

--------------------------------------------------------------------------------
-- Privilégios da aplicação
--------------------------------------------------------------------------------

GRANT CREATE SESSION TO &&APP_USER;

--------------------------------------------------------------------------------
-- Role da aplicação (criada pelo DBA)
--------------------------------------------------------------------------------

BEGIN
    EXECUTE IMMEDIATE 'CREATE ROLE UNMPORTCOM_APP_ROLE';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1921 THEN NULL; ELSE RAISE; END IF;
END;
/

GRANT UNMPORTCOM_APP_ROLE TO &&OWNER_USER WITH ADMIN OPTION;

--------------------------------------------------------------------------------
-- Liberação das contas
--------------------------------------------------------------------------------

ALTER USER &&OWNER_USER ACCOUNT UNLOCK;
ALTER USER &&APP_USER ACCOUNT UNLOCK;

--------------------------------------------------------------------------------
-- Validação
--------------------------------------------------------------------------------

COLUMN USERNAME FORMAT A25
COLUMN ACCOUNT_STATUS FORMAT A20
COLUMN DEFAULT_TABLESPACE FORMAT A20

PROMPT.
PROMPT Usuários criados:
PROMPT.

SELECT
    USERNAME,
    ACCOUNT_STATUS,
    DEFAULT_TABLESPACE
FROM DBA_USERS
WHERE USERNAME IN ('UNMPORTCOM', 'UNMPORTCOM_APP')
ORDER BY USERNAME;

PROMPT.
PROMPT Script executado com sucesso.
PROMPT Fim do script 001-create-users.sql
PROMPT.
