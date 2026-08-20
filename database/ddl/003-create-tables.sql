--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 003-create-tables.sql
-- Versão  : 2.0
--
-- Objetivo
--   Criar todas as tabelas do modelo físico aprovado.
--
-- Banco
--   Oracle Database 11g+
--
-- Dependências
--   001-create-users.sql
--   002-create-sequences.sql
--
-- Executar como
--   UNMPORTCOM
--
-- Observação
--   Este script contém exclusivamente CREATE TABLE.
--   Constraints, Índices e Comments são criados em scripts próprios.
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Criação das Tabelas
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- ORGANIZAÇÃO CORPORATIVA
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- FEDERACAO
--------------------------------------------------------------------------------

CREATE TABLE FEDERACAO
(
    COD_FEDERACAO      NUMBER(19)    NOT NULL,
    NOM_FEDERACAO      VARCHAR2(200) NOT NULL,
    SIG_FEDERACAO      VARCHAR2(30)  NOT NULL,
    COD_UNIMED         NUMBER(3)     NOT NULL,
    NUM_REGISTRO_ANS   VARCHAR2(20)  NOT NULL,
    URL_SITE           VARCHAR2(300),
    DSC_FEDERACAO      CLOB,
    FLG_ATIVO          CHAR(1)       DEFAULT 'S' NOT NULL,
    DAT_CADASTRO       TIMESTAMP(6)  DEFAULT SYSTIMESTAMP NOT NULL,
    DAT_ATUALIZACAO    TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- SINGULAR
--------------------------------------------------------------------------------

CREATE TABLE SINGULAR
(
    COD_SINGULAR       NUMBER(19)    NOT NULL,
    COD_FEDERACAO      NUMBER(19)    NOT NULL,
    NOM_SINGULAR       VARCHAR2(200) NOT NULL,
    SIG_SINGULAR       VARCHAR2(30)  NOT NULL,
    COD_UNIMED         NUMBER(3)     NOT NULL,
    NUM_REGISTRO_ANS   VARCHAR2(20)  NOT NULL,
    DES_DOMINIO_EMAIL  VARCHAR2(255),
    FLG_ATIVO          CHAR(1)       DEFAULT 'S' NOT NULL,
    DAT_CADASTRO       TIMESTAMP(6)  DEFAULT SYSTIMESTAMP NOT NULL,
    DAT_ATUALIZACAO    TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- ENDERECO
--------------------------------------------------------------------------------

CREATE TABLE ENDERECO
(
    COD_ENDERECO       NUMBER(19)    NOT NULL,
    COD_FEDERACAO      NUMBER(19),
    COD_SINGULAR       NUMBER(19),

    NOM_LOCAL          VARCHAR2(100) NOT NULL,
    TIP_ENDERECO       VARCHAR2(30)  NOT NULL,
    DES_LOGRADOURO     VARCHAR2(200) NOT NULL,
    NUM_ENDERECO       VARCHAR2(20),
    DES_COMPLEMENTO    VARCHAR2(100),
    NOM_BAIRRO         VARCHAR2(100) NOT NULL,
    NOM_CIDADE         VARCHAR2(100) NOT NULL,
    SIG_UF             CHAR(2)       NOT NULL,
    NUM_CEP            VARCHAR2(8)   NOT NULL,

    FLG_PRINCIPAL      CHAR(1)       DEFAULT 'N' NOT NULL,

    DAT_CADASTRO       TIMESTAMP(6)  DEFAULT SYSTIMESTAMP NOT NULL,
    DAT_ATUALIZACAO    TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- CONTATO
--------------------------------------------------------------------------------

CREATE TABLE CONTATO
(
    COD_CONTATO        NUMBER(19)    NOT NULL,
    COD_FEDERACAO      NUMBER(19),
    COD_SINGULAR       NUMBER(19),
    COD_AREA           NUMBER(19),
    COD_EQUIPE         NUMBER(19),
    COD_COLABORADOR    NUMBER(19),

    TIP_CONTATO        VARCHAR2(30)  NOT NULL,
    DSC_CONTATO        VARCHAR2(200),
    DES_VALOR          VARCHAR2(255) NOT NULL,
    DES_HORARIO        VARCHAR2(200),

    FLG_PRINCIPAL      CHAR(1)       DEFAULT 'N' NOT NULL,

    DAT_CADASTRO       TIMESTAMP(6)  DEFAULT SYSTIMESTAMP NOT NULL,
    DAT_ATUALIZACAO    TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- AREA
--------------------------------------------------------------------------------

CREATE TABLE AREA
(
    COD_AREA           NUMBER(19)    NOT NULL,
    COD_SINGULAR       NUMBER(19),
    NOM_AREA           VARCHAR2(200) NOT NULL,
    SIG_AREA           VARCHAR2(30),
    DSC_AREA           CLOB,
    COD_GESTOR         NUMBER(19),
    FLG_ATIVO          CHAR(1)       DEFAULT 'S' NOT NULL,
    DAT_CADASTRO       TIMESTAMP(6)  DEFAULT SYSTIMESTAMP NOT NULL,
    DAT_ATUALIZACAO    TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- EQUIPE
--------------------------------------------------------------------------------

CREATE TABLE EQUIPE
(
    COD_EQUIPE         NUMBER(19)    NOT NULL,
    COD_AREA           NUMBER(19)    NOT NULL,
    NOM_EQUIPE         VARCHAR2(200) NOT NULL,
    DSC_EQUIPE         CLOB,
    COD_LIDER          NUMBER(19),
    FLG_ATIVO          CHAR(1)       DEFAULT 'S' NOT NULL,
    DAT_CADASTRO       TIMESTAMP(6)  DEFAULT SYSTIMESTAMP NOT NULL,
    DAT_ATUALIZACAO    TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- COLABORADOR
--------------------------------------------------------------------------------

CREATE TABLE COLABORADOR
(
    COD_COLABORADOR    NUMBER(19)    NOT NULL,
    COD_FEDERACAO      NUMBER(19)    NOT NULL,
    COD_SINGULAR       NUMBER(19),
    COD_AREA           NUMBER(19),
    COD_EQUIPE         NUMBER(19),
    COD_GESTOR         NUMBER(19),

    NOM_COLABORADOR    VARCHAR2(255) NOT NULL,
    DES_EMAIL          VARCHAR2(255) NOT NULL,
    ID_ZIMBRA          VARCHAR2(255) NOT NULL,
    DES_BIOGRAFIA      VARCHAR2(4000),

    FLG_ATIVO          CHAR(1)       DEFAULT 'S' NOT NULL,

    DAT_NASCIMENTO     TIMESTAMP(6),
    DAT_CONTRATACAO    TIMESTAMP(6),
    DAT_ULTIMO_ACESSO  TIMESTAMP(6),
    DAT_CADASTRO       TIMESTAMP(6)  DEFAULT SYSTIMESTAMP NOT NULL,
    DAT_ATUALIZACAO    TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- ONBOARDING_SOLICITACAO
--------------------------------------------------------------------------------

CREATE TABLE ONBOARDING_SOLICITACAO
(
    COD_ONBOARDING_SOLICITACAO NUMBER(19) NOT NULL,

    COD_COLABORADOR    NUMBER(19)   NOT NULL,

    STA_SOLICITACAO    VARCHAR2(30)
        DEFAULT 'PENDENTE' NOT NULL,

    DSC_OBSERVACAO     CLOB,

    DAT_SOLICITACAO    TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_PROCESSAMENTO  TIMESTAMP(6)
);

PROMPT
PROMPT Bloco Organização Corporativa criado com sucesso.
PROMPT

--------------------------------------------------------------------------------
-- GESTÃO DOCUMENTAL
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- CATEGORIA_DOCUMENTAL
--------------------------------------------------------------------------------

CREATE TABLE CATEGORIA_DOCUMENTAL
(
    COD_CATEGORIA_DOCUMENTAL NUMBER(19) NOT NULL,

    NOM_CATEGORIA        VARCHAR2(150) NOT NULL,
    DSC_CATEGORIA        CLOB,

    FLG_ATIVO            CHAR(1)
        DEFAULT 'S' NOT NULL,

    DAT_CADASTRO         TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_ATUALIZACAO      TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- PASTA
--------------------------------------------------------------------------------

CREATE TABLE PASTA
(
    COD_PASTA            NUMBER(19) NOT NULL,

    COD_PASTA_PAI        NUMBER(19),

    NOM_PASTA            VARCHAR2(200) NOT NULL,
    DSC_PASTA            CLOB,

    FLG_HERDA_PERMISSAO  CHAR(1)
        DEFAULT 'S' NOT NULL,

    FLG_ATIVO            CHAR(1)
        DEFAULT 'S' NOT NULL,

    DAT_CADASTRO         TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_ATUALIZACAO      TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- DOCUMENTO
--------------------------------------------------------------------------------

CREATE TABLE DOCUMENTO
(
    COD_DOCUMENTO NUMBER(19) NOT NULL,

    COD_CATEGORIA_DOCUMENTAL NUMBER(19) NOT NULL,
    COD_PASTA               NUMBER(19) NOT NULL,
    COD_COLABORADOR         NUMBER(19) NOT NULL,

    TIT_DOCUMENTO           VARCHAR2(300) NOT NULL,
    DSC_DOCUMENTO           CLOB,

    STA_DOCUMENTO           VARCHAR2(30)
        DEFAULT 'ATIVO' NOT NULL,

    DAT_PUBLICACAO          TIMESTAMP(6),
    DAT_EXPIRACAO           TIMESTAMP(6),

    DAT_CADASTRO            TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_ATUALIZACAO         TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- ARQUIVO_BINARIO
--------------------------------------------------------------------------------

CREATE TABLE ARQUIVO_BINARIO
(
    COD_ARQUIVO_BINARIO NUMBER(19) NOT NULL,

    NOM_ARQUIVO           VARCHAR2(500) NOT NULL,
    URL_ARQUIVO           VARCHAR2(2000) NOT NULL,
    TIP_MIME              VARCHAR2(200) NOT NULL,
    QTD_TAMANHO_BYTES     NUMBER(19) NOT NULL,
    HASH_ARQUIVO          VARCHAR2(128) NOT NULL,

    DAT_CADASTRO          TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL
);

--------------------------------------------------------------------------------
-- DOCUMENTO_VERSAO
--------------------------------------------------------------------------------

CREATE TABLE DOCUMENTO_VERSAO
(
    COD_DOCUMENTO_VERSAO NUMBER(19) NOT NULL,

    COD_DOCUMENTO        NUMBER(19) NOT NULL,
    COD_ARQUIVO_BINARIO  NUMBER(19) NOT NULL,
    COD_COLABORADOR      NUMBER(19) NOT NULL,

    NUM_VERSAO           NUMBER(10) NOT NULL,

    DSC_ALTERACAO        CLOB,

    FLG_VERSAO_ATUAL     CHAR(1)
        DEFAULT 'N' NOT NULL,

    DAT_VERSAO           TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL
);

--------------------------------------------------------------------------------
-- COMPARTILHAMENTO
--------------------------------------------------------------------------------

CREATE TABLE COMPARTILHAMENTO
(
    COD_COMPARTILHAMENTO NUMBER(19)  NOT NULL,

    TIP_ORIGEM           VARCHAR2(30) NOT NULL,
    COD_ORIGEM           NUMBER(19) NOT NULL,

    TIP_DESTINATARIO     VARCHAR2(30) NOT NULL,
    COD_DESTINATARIO     NUMBER(19) NOT NULL,

    TIP_ACESSO           VARCHAR2(30) NOT NULL,

    DAT_CADASTRO         TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL
);

PROMPT
PROMPT Bloco Gestão Documental criado com sucesso.


--------------------------------------------------------------------------------
-- CONTROLE DE ACESSO
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- AUTH_SESSAO
--------------------------------------------------------------------------------

CREATE TABLE AUTH_SESSAO
(
    COD_SESSAO            NUMBER(19) NOT NULL,

    ID_SESSAO             VARCHAR2(36)  NOT NULL,
    COD_COLABORADOR       NUMBER(19)    NOT NULL,
    HASH_REFRESH_TOKEN    VARCHAR2(255) NOT NULL,
    DES_DISPOSITIVO       VARCHAR2(255),

    FLG_REMEMBER_ME       CHAR(1)
        DEFAULT 'N' NOT NULL,

    DAT_CRIACAO           TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_EXPIRACAO         TIMESTAMP(6)  NOT NULL,

    FLG_REVOGADA          CHAR(1)
        DEFAULT 'N' NOT NULL,

    DAT_REVOGACAO         TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- PAPEL
--------------------------------------------------------------------------------

CREATE TABLE PAPEL
(
    COD_PAPEL           NUMBER(19) NOT NULL,

    NOM_PAPEL           VARCHAR2(100) NOT NULL,
    DSC_PAPEL           CLOB,

    FLG_ATIVO           CHAR(1)
        DEFAULT 'S' NOT NULL,

    DAT_CADASTRO        TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_ATUALIZACAO     TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- PAPEL_ATRIBUICAO
--------------------------------------------------------------------------------

CREATE TABLE PAPEL_ATRIBUICAO
(
    COD_PAPEL_ATRIBUICAO NUMBER(19)  NOT NULL,

    COD_COLABORADOR      NUMBER(19) NOT NULL,
    COD_PAPEL            NUMBER(19) NOT NULL,

    COD_FEDERACAO        NUMBER(19),
    COD_SINGULAR         NUMBER(19),
    COD_AREA             NUMBER(19),
    COD_EQUIPE           NUMBER(19),

    DAT_INICIO_VIGENCIA  TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_FIM_VIGENCIA     TIMESTAMP(6),

    FLG_ATIVO            CHAR(1)
        DEFAULT 'S' NOT NULL
);

--------------------------------------------------------------------------------
-- PERMISSAO_PASTA
--------------------------------------------------------------------------------

CREATE TABLE PERMISSAO_PASTA
(
    COD_PERMISSAO_PASTA NUMBER(19)  NOT NULL,

    COD_PASTA           NUMBER(19) NOT NULL,

    TIP_DESTINATARIO    VARCHAR2(30) NOT NULL,
    COD_DESTINATARIO    NUMBER(19) NOT NULL,

    TIP_ACESSO          VARCHAR2(30) NOT NULL,

    DAT_CADASTRO        TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL
);

--------------------------------------------------------------------------------
-- SOLICITACAO_PERMISSAO
--------------------------------------------------------------------------------

CREATE TABLE SOLICITACAO_PERMISSAO
(
    COD_SOLICITACAO_PERMISSAO NUMBER(19)  NOT NULL,

    COD_COLABORADOR      NUMBER(19) NOT NULL,
    COD_PASTA            NUMBER(19),
    COD_DOCUMENTO        NUMBER(19),

    STA_SOLICITACAO      VARCHAR2(30)
        DEFAULT 'PENDENTE' NOT NULL,

    DSC_JUSTIFICATIVA    CLOB,

    DAT_SOLICITACAO      TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_ANALISE          TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- REGISTRO_AUDITORIA
--------------------------------------------------------------------------------

CREATE TABLE REGISTRO_AUDITORIA
(
    COD_REGISTRO_AUDITORIA NUMBER(19)  NOT NULL,

    COD_COLABORADOR      NUMBER(19),

    TIP_EVENTO           VARCHAR2(100) NOT NULL,
    TIP_ENTIDADE         VARCHAR2(100) NOT NULL,

    COD_ENTIDADE         NUMBER(19) NOT NULL,

    DADOS_ANTES          CLOB,
    DADOS_DEPOIS         CLOB,

    DAT_EVENTO           TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL
);

PROMPT
PROMPT Bloco Controle de Acesso criado com sucesso.
PROMPT

--------------------------------------------------------------------------------
-- COMUNICAÇÃO
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- COMUNICADO
--------------------------------------------------------------------------------

CREATE TABLE COMUNICADO
(
    COD_COMUNICADO      NUMBER(19)  NOT NULL,

    COD_COLABORADOR     NUMBER(19) NOT NULL,

    TIT_COMUNICADO      VARCHAR2(300) NOT NULL,
    DSC_COMUNICADO      CLOB NOT NULL,

    DAT_PUBLICACAO      TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_EXPIRACAO       TIMESTAMP(6),

    FLG_PUBLICADO       CHAR(1)
        DEFAULT 'N' NOT NULL,

    FLG_DESTAQUE        CHAR(1)
        DEFAULT 'N' NOT NULL,

    DAT_CADASTRO        TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_ATUALIZACAO     TIMESTAMP(6)
);

--------------------------------------------------------------------------------
-- NOTIFICACAO
--------------------------------------------------------------------------------

CREATE TABLE NOTIFICACAO
(
    COD_NOTIFICACAO     NUMBER(19)  NOT NULL,

    COD_COLABORADOR     NUMBER(19) NOT NULL,

    TIT_NOTIFICACAO     VARCHAR2(300) NOT NULL,
    DSC_NOTIFICACAO     VARCHAR2(4000) NOT NULL,

    TIP_NOTIFICACAO     VARCHAR2(50) NOT NULL,

    FLG_LIDA            CHAR(1)
        DEFAULT 'N' NOT NULL,

    DAT_LEITURA         TIMESTAMP(6),

    DAT_ENVIO           TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL
);

--------------------------------------------------------------------------------
-- CONFIGURAÇÃO
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- CONFIGURACAO_PORTAL
--------------------------------------------------------------------------------

CREATE TABLE CONFIGURACAO_PORTAL
(
    COD_CONFIGURACAO_PORTAL NUMBER(19) NOT NULL,

    COD_FEDERACAO           NUMBER(19) NOT NULL,

    NOM_PORTAL              VARCHAR2(200) NOT NULL,

    URL_PORTAL              VARCHAR2(500),

    URL_LOGO                VARCHAR2(1000),

    URL_FAVICON             VARCHAR2(1000),

    DSC_RODAPE              VARCHAR2(4000),

    FLG_ONBOARDING_ATIVO    CHAR(1)
        DEFAULT 'S' NOT NULL,

    FLG_NOTIFICACAO_EMAIL   CHAR(1)
        DEFAULT 'S' NOT NULL,

    FLG_COMUNICADO_DESTAQUE CHAR(1)
        DEFAULT 'S' NOT NULL,

    QTD_DIAS_EXPIRACAO_DOCUMENTO NUMBER(5)
        DEFAULT 365,

    DAT_CADASTRO            TIMESTAMP(6)
        DEFAULT SYSTIMESTAMP NOT NULL,

    DAT_ATUALIZACAO         TIMESTAMP(6)
);

PROMPT
PROMPT ==========================================================
PROMPT Todas as tabelas foram criadas com sucesso.
PROMPT Fim do script 003-create-tables.sql
PROMPT ==========================================================
PROMPT