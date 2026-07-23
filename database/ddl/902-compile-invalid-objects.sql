--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 902-compile-invalid-objects.sql
-- Versão  : 1.0
--
-- Objetivo
--   Recompilar objetos inválidos do schema UNMPORTCOM.
--
-- Banco
--   Oracle Database 11g+
--
-- Executar como
--   UNMPORTCOM
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Recompilação de Objetos Inválidos
PROMPT ==========================================================

BEGIN
    DBMS_UTILITY.COMPILE_SCHEMA(
        SCHEMA => USER,
        COMPILE_ALL => FALSE
    );
END;
/

PROMPT.
PROMPT Objetos ainda inválidos após recompilação:
PROMPT.

SELECT OBJECT_TYPE, OBJECT_NAME, STATUS
  FROM USER_OBJECTS
 WHERE STATUS = 'INVALID'
 ORDER BY OBJECT_TYPE, OBJECT_NAME;

PROMPT.
PROMPT ==========================================================
PROMPT Recompilação concluída.
PROMPT Fim do script 902-compile-invalid-objects.sql
PROMPT ==========================================================
PROMPT.
