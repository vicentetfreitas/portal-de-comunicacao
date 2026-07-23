--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V007__colaborador_ssot_alignment.sql
-- Versão  : 1.0
--
-- Objetivo
--   Alinhar COLABORADOR ao modelo físico SSOT (FT-COLABORADOR): remover atributos
--   legados (DES_CARGO, NUM_CPF) e ajustar tipos/obrigatoriedade.
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
PROMPT Evolução V007 — COLABORADOR alinhamento SSOT
PROMPT ==========================================================

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE COLABORADOR DROP CONSTRAINT UK_COLABORADOR_CPF';
    DBMS_OUTPUT.PUT_LINE('UK_COLABORADOR_CPF removida.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE IN (-2443, -942) THEN
            DBMS_OUTPUT.PUT_LINE('UK_COLABORADOR_CPF ausente — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE COLABORADOR DROP COLUMN DES_CARGO';
    DBMS_OUTPUT.PUT_LINE('Coluna DES_CARGO removida.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -904 THEN
            DBMS_OUTPUT.PUT_LINE('DES_CARGO ausente — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE COLABORADOR DROP COLUMN NUM_CPF';
    DBMS_OUTPUT.PUT_LINE('Coluna NUM_CPF removida.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -904 THEN
            DBMS_OUTPUT.PUT_LINE('NUM_CPF ausente — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

UPDATE COLABORADOR
   SET ID_ZIMBRA = 'legacy-' || COD_COLABORADOR
 WHERE ID_ZIMBRA IS NULL;

COMMIT;

BEGIN
    EXECUTE IMMEDIATE '
        ALTER TABLE COLABORADOR MODIFY (
            NOM_COLABORADOR VARCHAR2(255) NOT NULL,
            ID_ZIMBRA VARCHAR2(255) NOT NULL
        )';
    DBMS_OUTPUT.PUT_LINE('NOM_COLABORADOR e ID_ZIMBRA ajustados.');
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
        ALTER TABLE COLABORADOR ADD (
            DES_BIOGRAFIA_TMP VARCHAR2(4000)
        )';
    EXECUTE IMMEDIATE '
        UPDATE COLABORADOR
           SET DES_BIOGRAFIA_TMP = DBMS_LOB.SUBSTR(DES_BIOGRAFIA, 4000, 1)
         WHERE DES_BIOGRAFIA IS NOT NULL';
    EXECUTE IMMEDIATE 'ALTER TABLE COLABORADOR DROP COLUMN DES_BIOGRAFIA';
    EXECUTE IMMEDIATE 'ALTER TABLE COLABORADOR RENAME COLUMN DES_BIOGRAFIA_TMP TO DES_BIOGRAFIA';
    DBMS_OUTPUT.PUT_LINE('DES_BIOGRAFIA convertida para VARCHAR2(4000).');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -904 OR SQLCODE = -1430 THEN
            DBMS_OUTPUT.PUT_LINE('DES_BIOGRAFIA já alinhada ou ausente — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

PROMPT.
PROMPT V007 concluído.
PROMPT.
