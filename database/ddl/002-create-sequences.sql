--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 002-create-sequences.sql
-- Versão  : 4.0
--
-- Objetivo
--   Criar as sequences do schema homologado (baseline 2026-07-22).
--
-- Referência
--   database/baseline/oracle-baseline-2026-07-22.md (12 sequences)
--
-- Padrão
--   Nomes conforme inspeção estrutural homologada; nem toda tabela possui sequence.
--
-- Banco
--   Oracle Database 11g+
--
-- Dependências
--   001-create-users.sql
--
-- Executar como
--   UNMPORTCOM
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Criação das Sequences (baseline homologada)
PROMPT ==========================================================

CREATE SEQUENCE SQ_FEDERACAO_COD_FEDERACAO
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_SINGULAR_COD_SINGULAR
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_AREA_COD_AREA
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_EQUIPE_COD_EQUIPE
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_COLABORADOR
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_ONBOARD_SOLIC
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_DOCUMENTO_COD_DOCUMENTO
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_REG_AUDIT_COD_REG_AUDIT
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_AUTH_SESSAO
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_COMUNICADO_COD_COMUNICADO
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_NOTIFICACAO_COD_NOTIFICACAO
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_CONFIG_PORT_COD_CONFIG_PORT
START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

--------------------------------------------------------------------------------
-- Validação
--------------------------------------------------------------------------------

COLUMN SEQUENCE_NAME FORMAT A60
COLUMN CACHE_SIZE    FORMAT 999

PROMPT.
PROMPT Sequences criadas (esperado: 12):
PROMPT.

SELECT SEQUENCE_NAME, CACHE_SIZE, LAST_NUMBER
FROM USER_SEQUENCES
ORDER BY SEQUENCE_NAME;

PROMPT.
PROMPT Script executado com sucesso.
PROMPT Fim do script 002-create-sequences.sql
PROMPT.
