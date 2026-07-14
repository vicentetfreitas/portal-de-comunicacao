--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V003__auth_sessao_and_colaborador_zimbra.sql
-- Versão  : 1.0
--
-- Objetivo
--   Evolução pós-baseline para ambientes com DDL corporativa já aplicada.
--   Incorpora estruturas da FT-AUTH: ID_ZIMBRA em COLABORADOR e tabela AUTH_SESSAO.
--
-- Banco
--   Oracle Database 11g+
--
-- Executar como
--   UNMPORTCOM
--
-- Pré-requisito
--   Baseline DDL (002–007) já executado.
--
-- Referência
--   docs/database/ddl/ — baseline consolidado (instalações greenfield)
--   backend/src/main/resources/db/migration/V2__access_control.sql — legado (obsoleto; DEC-DB-019)
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Evolução V003 — AUTH_SESSAO + ID_ZIMBRA
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- COLABORADOR — ID_ZIMBRA (FT-AUTH)
--------------------------------------------------------------------------------

BEGIN
    EXECUTE IMMEDIATE '
        ALTER TABLE COLABORADOR ADD (
            ID_ZIMBRA VARCHAR2(255)
        )';
    DBMS_OUTPUT.PUT_LINE('Coluna ID_ZIMBRA adicionada em COLABORADOR.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1430 THEN
            DBMS_OUTPUT.PUT_LINE('Coluna ID_ZIMBRA já existe em COLABORADOR — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
        ALTER TABLE COLABORADOR
            ADD CONSTRAINT UK_COLABORADOR_ZIMBRA UNIQUE (ID_ZIMBRA)';
    DBMS_OUTPUT.PUT_LINE('Constraint UK_COLABORADOR_ZIMBRA criada.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -2261 OR SQLCODE = -2264 THEN
            DBMS_OUTPUT.PUT_LINE('UK_COLABORADOR_ZIMBRA já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
        CREATE INDEX IDX_COLABORADOR_ZIMBRA ON COLABORADOR (ID_ZIMBRA)';
    DBMS_OUTPUT.PUT_LINE('Índice IDX_COLABORADOR_ZIMBRA criado.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN
            DBMS_OUTPUT.PUT_LINE('Índice IDX_COLABORADOR_ZIMBRA já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

--------------------------------------------------------------------------------
-- AUTH_SESSAO
--------------------------------------------------------------------------------

BEGIN
    EXECUTE IMMEDIATE '
        CREATE SEQUENCE SQ_AUTH_SESSAO_COD_SESSAO
        START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE';
    DBMS_OUTPUT.PUT_LINE('Sequence SQ_AUTH_SESSAO_COD_SESSAO criada.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN
            DBMS_OUTPUT.PUT_LINE('Sequence SQ_AUTH_SESSAO_COD_SESSAO já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
        CREATE TABLE AUTH_SESSAO (
            COD_SESSAO            NUMBER(19)    NOT NULL,
            ID_SESSAO             VARCHAR2(36)  NOT NULL,
            COD_COLABORADOR       NUMBER(19)    NOT NULL,
            HASH_REFRESH_TOKEN    VARCHAR2(255) NOT NULL,
            DES_DISPOSITIVO       VARCHAR2(255),
            FLG_REMEMBER_ME       CHAR(1) DEFAULT ''N'' NOT NULL,
            DAT_CRIACAO           TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
            DAT_EXPIRACAO         TIMESTAMP(6)  NOT NULL,
            FLG_REVOGADA          CHAR(1) DEFAULT ''N'' NOT NULL,
            DAT_REVOGACAO         TIMESTAMP(6),
            CONSTRAINT PK_AUTH_SESSAO PRIMARY KEY (COD_SESSAO),
            CONSTRAINT UK_AUTH_SESSAO_ID UNIQUE (ID_SESSAO),
            CONSTRAINT UK_AUTH_SESSAO_HASH UNIQUE (HASH_REFRESH_TOKEN),
            CONSTRAINT FK_AUTH_SESSAO_COLABORADOR FOREIGN KEY (COD_COLABORADOR)
                REFERENCES COLABORADOR (COD_COLABORADOR),
            CONSTRAINT CK_AUTH_SESSAO_FLG_REMEMBER CHECK (FLG_REMEMBER_ME IN (''S'', ''N'')),
            CONSTRAINT CK_AUTH_SESSAO_FLG_REVOGADA CHECK (FLG_REVOGADA IN (''S'', ''N''))
        )';
    DBMS_OUTPUT.PUT_LINE('Tabela AUTH_SESSAO criada.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN
            DBMS_OUTPUT.PUT_LINE('Tabela AUTH_SESSAO já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
        CREATE INDEX IDX_AUTH_SESSAO_COLABORADOR ON AUTH_SESSAO (COD_COLABORADOR)';
    DBMS_OUTPUT.PUT_LINE('Índice IDX_AUTH_SESSAO_COLABORADOR criado.');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN
            DBMS_OUTPUT.PUT_LINE('Índice IDX_AUTH_SESSAO_COLABORADOR já existe — ignorado.');
        ELSE
            RAISE;
        END IF;
END;
/

COMMENT ON TABLE AUTH_SESSAO IS
'Sessões de autenticação do Portal (Refresh Token, revogação e session_id).';

COMMENT ON COLUMN AUTH_SESSAO.ID_SESSAO IS 'Identificador público da sessão (session_id).';

GRANT SELECT, INSERT, UPDATE, DELETE ON AUTH_SESSAO TO UNMPORTCOM_APP_ROLE;
GRANT SELECT ON SQ_AUTH_SESSAO_COD_SESSAO TO UNMPORTCOM_APP_ROLE;

PROMPT.
PROMPT V003 concluído.
PROMPT.
