--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 900-drop-all.sql
-- Versão  : 1.0
--
-- Objetivo
--   Remover todos os objetos do schema UNMPORTCOM.
--
-- ATENÇÃO
--   Script destrutivo. Utilizar apenas em ambientes de desenvolvimento.
--
-- Banco
--   Oracle Database 11g+
--
-- Executar como
--   UNMPORTCOM (Owner)
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Remoção de Objetos do Schema
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- Views
--------------------------------------------------------------------------------

BEGIN
    FOR r IN (
        SELECT VIEW_NAME
          FROM USER_VIEWS
         ORDER BY VIEW_NAME
    ) LOOP
        EXECUTE IMMEDIATE 'DROP VIEW ' || r.VIEW_NAME;
        DBMS_OUTPUT.PUT_LINE('View removida: ' || r.VIEW_NAME);
    END LOOP;
END;
/

--------------------------------------------------------------------------------
-- Tabelas (ordem inversa de dependência)
--------------------------------------------------------------------------------

BEGIN
    FOR t IN (
        SELECT TABLE_NAME
          FROM (
                SELECT 'NOTIFICACAO' AS TABLE_NAME FROM DUAL UNION ALL
                SELECT 'COMUNICADO' FROM DUAL UNION ALL
                SELECT 'CONFIGURACAO_PORTAL' FROM DUAL UNION ALL
                SELECT 'REGISTRO_AUDITORIA' FROM DUAL UNION ALL
                SELECT 'AUTH_SESSAO' FROM DUAL UNION ALL
                SELECT 'SOLICITACAO_PERMISSAO' FROM DUAL UNION ALL
                SELECT 'PERMISSAO_PASTA' FROM DUAL UNION ALL
                SELECT 'PAPEL_ATRIBUICAO' FROM DUAL UNION ALL
                SELECT 'PAPEL' FROM DUAL UNION ALL
                SELECT 'COMPARTILHAMENTO' FROM DUAL UNION ALL
                SELECT 'DOCUMENTO_VERSAO' FROM DUAL UNION ALL
                SELECT 'ARQUIVO_BINARIO' FROM DUAL UNION ALL
                SELECT 'DOCUMENTO' FROM DUAL UNION ALL
                SELECT 'PASTA' FROM DUAL UNION ALL
                SELECT 'CATEGORIA_DOCUMENTAL' FROM DUAL UNION ALL
                SELECT 'ONBOARDING_SOLICITACAO' FROM DUAL UNION ALL
                SELECT 'COLABORADOR' FROM DUAL UNION ALL
                SELECT 'EQUIPE' FROM DUAL UNION ALL
                SELECT 'AREA' FROM DUAL UNION ALL
                SELECT 'CONTATO' FROM DUAL UNION ALL
                SELECT 'ENDERECO' FROM DUAL UNION ALL
                SELECT 'SINGULAR' FROM DUAL UNION ALL
                SELECT 'FEDERACAO' FROM DUAL
               )
         WHERE TABLE_NAME IN (SELECT TABLE_NAME FROM USER_TABLES)
    ) LOOP
        EXECUTE IMMEDIATE 'DROP TABLE ' || t.TABLE_NAME || ' CASCADE CONSTRAINTS PURGE';
        DBMS_OUTPUT.PUT_LINE('Tabela removida: ' || t.TABLE_NAME);
    END LOOP;
END;
/

--------------------------------------------------------------------------------
-- Sequences
--------------------------------------------------------------------------------

BEGIN
    FOR s IN (
        SELECT SEQUENCE_NAME
          FROM USER_SEQUENCES
         ORDER BY SEQUENCE_NAME
    ) LOOP
        EXECUTE IMMEDIATE 'DROP SEQUENCE ' || s.SEQUENCE_NAME;
        DBMS_OUTPUT.PUT_LINE('Sequence removida: ' || s.SEQUENCE_NAME);
    END LOOP;
END;
/

--------------------------------------------------------------------------------
-- Synonyms
--------------------------------------------------------------------------------

BEGIN
    FOR s IN (
        SELECT SYNONYM_NAME
          FROM USER_SYNONYMS
         ORDER BY SYNONYM_NAME
    ) LOOP
        EXECUTE IMMEDIATE 'DROP SYNONYM ' || s.SYNONYM_NAME;
        DBMS_OUTPUT.PUT_LINE('Synonym removido: ' || s.SYNONYM_NAME);
    END LOOP;
END;
/

--------------------------------------------------------------------------------
-- Role da aplicação (executar como DBA se necessário)
--------------------------------------------------------------------------------

PROMPT.
PROMPT Para remover a role UNMPORTCOM_APP_ROLE, execute como DBA:
PROMPT   REVOKE UNMPORTCOM_APP_ROLE FROM UNMPORTCOM_APP;
PROMPT   DROP ROLE UNMPORTCOM_APP_ROLE;
PROMPT.
PROMPT Para remover usuários, execute como DBA:
PROMPT   DROP USER UNMPORTCOM_APP CASCADE;
PROMPT   DROP USER UNMPORTCOM CASCADE;
PROMPT.

PROMPT ==========================================================
PROMPT Remoção de objetos concluída.
PROMPT Fim do script 900-drop-all.sql
PROMPT ==========================================================
PROMPT.
