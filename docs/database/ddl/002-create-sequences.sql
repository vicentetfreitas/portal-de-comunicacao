--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 002-create-sequences.sql
-- Versão  : 3.0
--
-- Objetivo
--   Criar todas as sequences utilizadas pelo modelo físico.
--
-- Padrão
--   SQ_<TABELA>_<CAMPO_PK> (Padrão Unimed Ceará)
--
-- Performance
--   CACHE 20 — valor padrão Oracle para OLTP corporativo.
--   Reduz contenção de latch em NEXTVAL frente a NOCACHE.
--   Gap de até 20 IDs aceitável: PKs geradas pelo DEFAULT no banco.
--   NOCACHE reservado para ambientes RAC críticos ou auditoria gap-free.
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
PROMPT Criação das Sequences
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- ORGANIZAÇÃO CORPORATIVA
--------------------------------------------------------------------------------

CREATE SEQUENCE SQ_FEDERACAO_COD_FEDERACAO
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_SINGULAR_COD_SINGULAR
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_ENDERECO_COD_ENDERECO
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_CONTATO_COD_CONTATO
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_AREA_COD_AREA
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_EQUIPE_COD_EQUIPE
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_COLABORADOR_COD_COLABORADOR
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_AUTH_SESSAO_COD_SESSAO
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_ONBOARD_SOLIC
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

--------------------------------------------------------------------------------
-- GESTÃO DOCUMENTAL
--------------------------------------------------------------------------------

CREATE SEQUENCE SQ_CAT_DOC_COD_CAT_DOC
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_PASTA_COD_PASTA
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_DOCUMENTO_COD_DOCUMENTO
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_ARQ_BIN_COD_ARQ_BIN
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_DOC_VERS_COD_DOC_VERS
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_COMPART_COD_COMPART
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

--------------------------------------------------------------------------------
-- CONTROLE DE ACESSO
--------------------------------------------------------------------------------

CREATE SEQUENCE SQ_PAPEL_COD_PAPEL
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_PAPEL_ATRIB_COD_PAPEL_ATRIB
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_PERM_PASTA_COD_PERM_PASTA
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_SOLIC_PERM_COD_SOLIC_PERM
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_REG_AUDIT_COD_REG_AUDIT
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

--------------------------------------------------------------------------------
-- COMUNICAÇÃO
--------------------------------------------------------------------------------

CREATE SEQUENCE SQ_COMUNICADO_COD_COMUNICADO
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

CREATE SEQUENCE SQ_NOTIFICACAO_COD_NOTIFICACAO
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

--------------------------------------------------------------------------------
-- CONFIGURAÇÃO
--------------------------------------------------------------------------------

CREATE SEQUENCE SQ_CONFIG_PORT_COD_CONFIG_PORT
START WITH 1
INCREMENT BY 1
CACHE 20
NOCYCLE;

--------------------------------------------------------------------------------
-- Validação
--------------------------------------------------------------------------------

COLUMN SEQUENCE_NAME FORMAT A60
COLUMN CACHE_SIZE    FORMAT 999

PROMPT.
PROMPT Sequences criadas:
PROMPT.

SELECT
    SEQUENCE_NAME,
    CACHE_SIZE,
    LAST_NUMBER
FROM USER_SEQUENCES
ORDER BY SEQUENCE_NAME;

PROMPT.
PROMPT Script executado com sucesso.
PROMPT Fim do script 002-create-sequences.sql
PROMPT.
