--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V004__colaborador_corporate_columns.sql
-- Versão  : 1.0
--
-- Objetivo
--   Alinhar COLABORADOR (instalações legadas / mínimo FT-AUTH) ao modelo físico
--   corporativo documentado em database/model/03-physical-model.md e
--   database/ddl/003-create-tables.sql — requisito FT-COLABORADOR + login FT-AUTH.
--
-- Sintoma corrigido
--   ORA-00904: "COD_SINGULAR": invalid identifier — JPA persiste colunas organizacionais
--   ausentes no schema provisionado apenas com COLABORADOR mínimo (legado V2).
--
-- Banco
--   Oracle Database 11g+
--
-- Executar como
--   UNMPORTCOM
--
-- Pré-requisito
--   Tabela COLABORADOR existente (baseline ou legado).
--
-- Referência
--   database/ddl/003-create-tables.sql (COLABORADOR)
--   database/ddl/004-create-constraints.sql (FKs opcionais pós-coluna)
--   database/model/05-decisions-and-risks.md (DEC-DB-020)
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Evolução V004 — COLABORADOR colunas corporativas
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- Colunas organizacionais e atributos (idempotente)
--------------------------------------------------------------------------------

DECLARE
    PROCEDURE add_column_if_missing(p_column VARCHAR2, p_ddl VARCHAR2) IS
    BEGIN
        EXECUTE IMMEDIATE p_ddl;
        DBMS_OUTPUT.PUT_LINE('Coluna ' || p_column || ' adicionada em COLABORADOR.');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -1430 THEN
                DBMS_OUTPUT.PUT_LINE('Coluna ' || p_column || ' já existe — ignorado.');
            ELSE
                RAISE;
            END IF;
    END;
BEGIN
    add_column_if_missing('COD_SINGULAR',
        'ALTER TABLE COLABORADOR ADD (COD_SINGULAR NUMBER(19))');
    add_column_if_missing('COD_AREA',
        'ALTER TABLE COLABORADOR ADD (COD_AREA NUMBER(19))');
    add_column_if_missing('COD_EQUIPE',
        'ALTER TABLE COLABORADOR ADD (COD_EQUIPE NUMBER(19))');
    add_column_if_missing('COD_GESTOR',
        'ALTER TABLE COLABORADOR ADD (COD_GESTOR NUMBER(19))');
    add_column_if_missing('DES_BIOGRAFIA',
        'ALTER TABLE COLABORADOR ADD (DES_BIOGRAFIA VARCHAR2(4000))');
    add_column_if_missing('DAT_NASCIMENTO',
        'ALTER TABLE COLABORADOR ADD (DAT_NASCIMENTO TIMESTAMP(6))');
    add_column_if_missing('DAT_CONTRATACAO',
        'ALTER TABLE COLABORADOR ADD (DAT_CONTRATACAO TIMESTAMP(6))');
    add_column_if_missing('DAT_ULTIMO_ACESSO',
        'ALTER TABLE COLABORADOR ADD (DAT_ULTIMO_ACESSO TIMESTAMP(6))');
    add_column_if_missing('DAT_ATUALIZACAO',
        'ALTER TABLE COLABORADOR ADD (DAT_ATUALIZACAO TIMESTAMP(6))');
END;
/

--------------------------------------------------------------------------------
-- Índices (idempotente)
--------------------------------------------------------------------------------

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX IDX_COLABORADOR_SINGULAR ON COLABORADOR (COD_SINGULAR)';
    DBMS_OUTPUT.PUT_LINE('Índice IDX_COLABORADOR_SINGULAR criado.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 OR SQLCODE = -1408 THEN
            DBMS_OUTPUT.PUT_LINE('IDX_COLABORADOR_SINGULAR já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX IDX_COLABORADOR_AREA ON COLABORADOR (COD_AREA)';
    DBMS_OUTPUT.PUT_LINE('Índice IDX_COLABORADOR_AREA criado.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 OR SQLCODE = -1408 THEN
            DBMS_OUTPUT.PUT_LINE('IDX_COLABORADOR_AREA já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX IDX_COLABORADOR_EQUIPE ON COLABORADOR (COD_EQUIPE)';
    DBMS_OUTPUT.PUT_LINE('Índice IDX_COLABORADOR_EQUIPE criado.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 OR SQLCODE = -1408 THEN
            DBMS_OUTPUT.PUT_LINE('IDX_COLABORADOR_EQUIPE já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX IDX_COLABORADOR_GESTOR ON COLABORADOR (COD_GESTOR)';
    DBMS_OUTPUT.PUT_LINE('Índice IDX_COLABORADOR_GESTOR criado.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 OR SQLCODE = -1408 THEN
            DBMS_OUTPUT.PUT_LINE('IDX_COLABORADOR_GESTOR já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

--------------------------------------------------------------------------------
-- FKs (somente se tabelas de domínio existirem)
--------------------------------------------------------------------------------

BEGIN
    EXECUTE IMMEDIATE '
        ALTER TABLE COLABORADOR ADD CONSTRAINT FK_COLABORADOR_SINGULAR
            FOREIGN KEY (COD_SINGULAR) REFERENCES SINGULAR (COD_SINGULAR)';
    DBMS_OUTPUT.PUT_LINE('FK_COLABORADOR_SINGULAR criada.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE IN (-2261, -2264, -2275) THEN
            DBMS_OUTPUT.PUT_LINE('FK_COLABORADOR_SINGULAR já existe ou tabela SINGULAR ausente — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
        ALTER TABLE COLABORADOR ADD CONSTRAINT FK_COLABORADOR_AREA
            FOREIGN KEY (COD_AREA) REFERENCES AREA (COD_AREA)';
    DBMS_OUTPUT.PUT_LINE('FK_COLABORADOR_AREA criada.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE IN (-2261, -2264, -2275) THEN
            DBMS_OUTPUT.PUT_LINE('FK_COLABORADOR_AREA já existe ou tabela AREA ausente — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
        ALTER TABLE COLABORADOR ADD CONSTRAINT FK_COLABORADOR_EQUIPE
            FOREIGN KEY (COD_EQUIPE) REFERENCES EQUIPE (COD_EQUIPE)';
    DBMS_OUTPUT.PUT_LINE('FK_COLABORADOR_EQUIPE criada.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE IN (-2261, -2264, -2275) THEN
            DBMS_OUTPUT.PUT_LINE('FK_COLABORADOR_EQUIPE já existe ou tabela EQUIPE ausente — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
        ALTER TABLE COLABORADOR ADD CONSTRAINT FK_COLABORADOR_GESTOR
            FOREIGN KEY (COD_GESTOR) REFERENCES COLABORADOR (COD_COLABORADOR)';
    DBMS_OUTPUT.PUT_LINE('FK_COLABORADOR_GESTOR criada.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE IN (-2261, -2264, -2275) THEN
            DBMS_OUTPUT.PUT_LINE('FK_COLABORADOR_GESTOR já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

PROMPT.
PROMPT V004 concluído. Validar: SELECT column_name FROM user_tab_columns WHERE table_name=''COLABORADOR'' ORDER BY column_id;
PROMPT.
