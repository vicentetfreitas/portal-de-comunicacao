--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V006__drop_auth_sessao_organizational_context.sql
-- Versão  : 1.0
--
-- Objetivo
--   Remover colunas de contexto organizacional persistido em AUTH_SESSAO (REF-DB-CTX-01).
--   Vínculos organizacionais permanecem exclusivamente em COLABORADOR.
--
-- Referência
--   database/ddl/003-create-tables.sql (AUTH_SESSAO)
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Evolução V006 — remoção COD_*_CTX em AUTH_SESSAO
PROMPT ==========================================================

DECLARE
    PROCEDURE drop_column_if_exists(p_table VARCHAR2, p_column VARCHAR2) IS
        v_count NUMBER;
    BEGIN
        SELECT COUNT(*)
          INTO v_count
          FROM user_tab_cols
         WHERE table_name = p_table
           AND column_name = p_column;

        IF v_count > 0 THEN
            EXECUTE IMMEDIATE 'ALTER TABLE ' || p_table || ' DROP COLUMN ' || p_column;
            DBMS_OUTPUT.PUT_LINE(p_column || ' removida de ' || p_table || '.');
        ELSE
            DBMS_OUTPUT.PUT_LINE(p_column || ' não existe em ' || p_table || ' — ignorado.');
        END IF;
    END;
BEGIN
    drop_column_if_exists('AUTH_SESSAO', 'COD_FEDERACAO_CTX');
    drop_column_if_exists('AUTH_SESSAO', 'COD_SINGULAR_CTX');
    drop_column_if_exists('AUTH_SESSAO', 'COD_AREA_CTX');
    drop_column_if_exists('AUTH_SESSAO', 'COD_EQUIPE_CTX');
END;
/

PROMPT V006 concluída.
