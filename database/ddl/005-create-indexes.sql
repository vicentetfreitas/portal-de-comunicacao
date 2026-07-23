--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 005-create-indexes.sql
-- Versão  : 2.0
--
-- Objetivo
--   Criar todos os índices do Portal de Comunicação.
--
-- Banco
--   Oracle Database 11g+
--
-- Dependências
--   001-create-users.sql
--   002-create-sequences.sql
--   003-create-tables.sql
--   004-create-constraints.sql
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Criação dos Índices
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- ORGANIZAÇÃO CORPORATIVA
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- FEDERACAO
--------------------------------------------------------------------------------

CREATE INDEX IDX_FEDERACAO_SIGLA
    ON FEDERACAO (SIG_FEDERACAO);

CREATE INDEX IDX_FEDERACAO_COD_UNIMED
    ON FEDERACAO (COD_UNIMED);

--------------------------------------------------------------------------------
-- SINGULAR
--------------------------------------------------------------------------------

CREATE INDEX IDX_SINGULAR_FEDERACAO
    ON SINGULAR (COD_FEDERACAO);

CREATE INDEX IDX_SINGULAR_SIGLA
    ON SINGULAR (SIG_SINGULAR);

CREATE INDEX IDX_SINGULAR_COD_UNIMED
    ON SINGULAR (COD_UNIMED);

--------------------------------------------------------------------------------
-- ENDERECO
--------------------------------------------------------------------------------

CREATE INDEX IDX_ENDERECO_FEDERACAO
    ON ENDERECO (COD_FEDERACAO);

CREATE INDEX IDX_ENDERECO_SINGULAR
    ON ENDERECO (COD_SINGULAR);

--------------------------------------------------------------------------------
-- CONTATO
--------------------------------------------------------------------------------

CREATE INDEX IDX_CONTATO_FEDERACAO
    ON CONTATO (COD_FEDERACAO);

CREATE INDEX IDX_CONTATO_SINGULAR
    ON CONTATO (COD_SINGULAR);

CREATE INDEX IDX_CONTATO_AREA
    ON CONTATO (COD_AREA);

CREATE INDEX IDX_CONTATO_EQUIPE
    ON CONTATO (COD_EQUIPE);

CREATE INDEX IDX_CONTATO_COLABORADOR
    ON CONTATO (COD_COLABORADOR);

--------------------------------------------------------------------------------
-- AREA
--------------------------------------------------------------------------------

CREATE INDEX IDX_AREA_SINGULAR
    ON AREA (COD_SINGULAR);

CREATE INDEX IDX_AREA_GESTOR
    ON AREA (COD_GESTOR);

--------------------------------------------------------------------------------
-- EQUIPE
--------------------------------------------------------------------------------

CREATE INDEX IDX_EQUIPE_AREA
    ON EQUIPE (COD_AREA);

CREATE INDEX IDX_EQUIPE_LIDER
    ON EQUIPE (COD_LIDER);

--------------------------------------------------------------------------------
-- COLABORADOR NÃO INDEXADO Verificar Posteriormente
--------------------------------------------------------------------------------

CREATE INDEX IDX_COLABORADOR_EMAIL
    ON COLABORADOR (DES_EMAIL);

CREATE INDEX IDX_COLABORADOR_FEDERACAO
    ON COLABORADOR (COD_FEDERACAO);

CREATE INDEX IDX_COLABORADOR_SINGULAR
    ON COLABORADOR (COD_SINGULAR);

CREATE INDEX IDX_COLABORADOR_AREA
    ON COLABORADOR (COD_AREA);

CREATE INDEX IDX_COLABORADOR_EQUIPE
    ON COLABORADOR (COD_EQUIPE);

CREATE INDEX IDX_COLABORADOR_GESTOR
    ON COLABORADOR (COD_GESTOR);

CREATE INDEX IDX_COLABORADOR_ZIMBRA
    ON COLABORADOR (ID_ZIMBRA);

--------------------------------------------------------------------------------
-- ONBOARDING_SOLICITACAO
--------------------------------------------------------------------------------

-- Nenhum índice adicional definido no modelo físico.

PROMPT
PROMPT Bloco Organização Corporativa concluído.
PROMPT

--------------------------------------------------------------------------------
-- GESTÃO DOCUMENTAL
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- CATEGORIA_DOCUMENTAL
--------------------------------------------------------------------------------

CREATE INDEX IDX_CATEGORIA_DOCUMENTAL_NOME
    ON CATEGORIA_DOCUMENTAL (NOM_CATEGORIA);

--------------------------------------------------------------------------------
-- PASTA
--------------------------------------------------------------------------------

CREATE INDEX IDX_PASTA_PAI
    ON PASTA (COD_PASTA_PAI);

CREATE INDEX IDX_PASTA_NOME
    ON PASTA (NOM_PASTA);

--------------------------------------------------------------------------------
-- DOCUMENTO
--------------------------------------------------------------------------------

CREATE INDEX IDX_DOCUMENTO_TITULO
    ON DOCUMENTO (TIT_DOCUMENTO);

CREATE INDEX IDX_DOCUMENTO_STATUS
    ON DOCUMENTO (STA_DOCUMENTO);

CREATE INDEX IDX_DOCUMENTO_DATA_PUBLICACAO
    ON DOCUMENTO (DAT_PUBLICACAO);

CREATE INDEX IDX_DOCUMENTO_PASTA
    ON DOCUMENTO (COD_PASTA);

CREATE INDEX IDX_DOCUMENTO_CATEGORIA
    ON DOCUMENTO (COD_CATEGORIA_DOCUMENTAL);

--------------------------------------------------------------------------------
-- ARQUIVO_BINARIO
--------------------------------------------------------------------------------

CREATE INDEX IDX_ARQUIVO_HASH
    ON ARQUIVO_BINARIO (HASH_ARQUIVO);

--------------------------------------------------------------------------------
-- DOCUMENTO_VERSAO
--------------------------------------------------------------------------------

CREATE INDEX IDX_DOCUMENTO_VERSAO_DOCUMENTO
    ON DOCUMENTO_VERSAO (COD_DOCUMENTO);

CREATE INDEX IDX_DOCUMENTO_VERSAO_ATUAL
    ON DOCUMENTO_VERSAO (FLG_VERSAO_ATUAL);

CREATE INDEX IDX_DOCUMENTO_VERSAO_DATA
    ON DOCUMENTO_VERSAO (DAT_VERSAO);

--------------------------------------------------------------------------------
-- COMPARTILHAMENTO
--------------------------------------------------------------------------------

CREATE INDEX IDX_COMPART_DEST
    ON COMPARTILHAMENTO
    (
        TIP_DESTINATARIO,
        COD_DESTINATARIO
    );

CREATE INDEX IDX_COMPARTILHAMENTO_ORIGEM
    ON COMPARTILHAMENTO
    (
        TIP_ORIGEM,
        COD_ORIGEM
    );

PROMPT
PROMPT Bloco Gestão Documental concluído.
PROMPT

--------------------------------------------------------------------------------
-- CONTROLE DE ACESSO
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- AUTH_SESSAO
--------------------------------------------------------------------------------

CREATE INDEX IDX_AUTH_SESSAO_COLABORADOR
    ON AUTH_SESSAO (COD_COLABORADOR);

--------------------------------------------------------------------------------
-- PAPEL
--------------------------------------------------------------------------------

CREATE INDEX IDX_PAPEL_NOME
    ON PAPEL (NOM_PAPEL);

--------------------------------------------------------------------------------
-- PAPEL_ATRIBUICAO
--------------------------------------------------------------------------------

CREATE INDEX IDX_PAPEL_ATRIB_COLAB
    ON PAPEL_ATRIBUICAO (COD_COLABORADOR);

CREATE INDEX IDX_PAPEL_ATRIBUICAO_PAPEL
    ON PAPEL_ATRIBUICAO (COD_PAPEL);

CREATE INDEX IDX_PAPEL_ATRIBUICAO_ESCOPOS
    ON PAPEL_ATRIBUICAO
    (
        COD_FEDERACAO,
        COD_SINGULAR,
        COD_AREA,
        COD_EQUIPE
    );

--------------------------------------------------------------------------------
-- PERMISSAO_PASTA
--------------------------------------------------------------------------------

CREATE INDEX IDX_PERM_PASTA_DEST
    ON PERMISSAO_PASTA
    (
        TIP_DESTINATARIO,
        COD_DESTINATARIO
    );

CREATE INDEX IDX_PERMISSAO_PASTA_PASTA
    ON PERMISSAO_PASTA (COD_PASTA);

--------------------------------------------------------------------------------
-- SOLICITACAO_PERMISSAO
--------------------------------------------------------------------------------

CREATE INDEX IDX_SOLICITACAO_STATUS
    ON SOLICITACAO_PERMISSAO (STA_SOLICITACAO);

CREATE INDEX IDX_SOLICITACAO_COLABORADOR
    ON SOLICITACAO_PERMISSAO (COD_COLABORADOR);

--------------------------------------------------------------------------------
-- REGISTRO_AUDITORIA
--------------------------------------------------------------------------------

CREATE INDEX IDX_REGISTRO_AUDITORIA_DATA
    ON REGISTRO_AUDITORIA (DAT_EVENTO);

CREATE INDEX IDX_REG_AUDIT_ENT
    ON REGISTRO_AUDITORIA
    (
        TIP_ENTIDADE,
        COD_ENTIDADE
    );

CREATE INDEX IDX_REG_AUDIT_EVT
    ON REGISTRO_AUDITORIA (TIP_EVENTO);

--------------------------------------------------------------------------------
-- COMUNICAÇÃO
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- COMUNICADO
--------------------------------------------------------------------------------

CREATE INDEX IDX_COMUNICADO_PUBLICACAO
    ON COMUNICADO (DAT_PUBLICACAO);

CREATE INDEX IDX_COMUNICADO_EXPIRACAO
    ON COMUNICADO (DAT_EXPIRACAO);

CREATE INDEX IDX_COMUNICADO_PUBLICADO
    ON COMUNICADO (FLG_PUBLICADO);

CREATE INDEX IDX_COMUNICADO_AUTOR
    ON COMUNICADO (COD_COLABORADOR);

--------------------------------------------------------------------------------
-- NOTIFICACAO
--------------------------------------------------------------------------------

CREATE INDEX IDX_NOTIFICACAO_COLABORADOR
    ON NOTIFICACAO (COD_COLABORADOR);

CREATE INDEX IDX_NOTIFICACAO_LIDA
    ON NOTIFICACAO (FLG_LIDA);

CREATE INDEX IDX_NOTIFICACAO_ENVIO
    ON NOTIFICACAO (DAT_ENVIO);

--------------------------------------------------------------------------------
-- CONFIGURAÇÃO
--------------------------------------------------------------------------------

CREATE INDEX IDX_CONFIG_PORT_FED
    ON CONFIGURACAO_PORTAL (COD_FEDERACAO);

--------------------------------------------------------------------------------
-- ÍNDICES COMPOSTOS (DESEMPENHO)
-- Complementares ao modelo físico; justificados por padrões de consulta OLTP.
--------------------------------------------------------------------------------

-- Listagem de documentos por pasta e status (navegação documental)
CREATE INDEX IDX_DOCUMENTO_PASTA_STATUS
    ON DOCUMENTO (COD_PASTA, STA_DOCUMENTO);

-- Filtro por status com ordenação por data de publicação
CREATE INDEX IDX_DOC_STA_PUBL
    ON DOCUMENTO (STA_DOCUMENTO, DAT_PUBLICACAO);

-- Feed de comunicados publicados ordenados por data
CREATE INDEX IDX_COMUN_PUBL_01
    ON COMUNICADO (FLG_PUBLICADO, DAT_PUBLICACAO);

-- Notificações não lidas por colaborador (caixa de entrada)
CREATE INDEX IDX_NOTIF_COLAB_LIDA
    ON NOTIFICACAO (COD_COLABORADOR, FLG_LIDA);

--------------------------------------------------------------------------------
-- NOTA DE AUDITORIA (ÍNDICES REDUNDANTES COM UK — MANTIDOS POR ADERÊNCIA AO MODELO)
-- IDX_FEDERACAO_SIGLA / UK_FEDERACAO_SIGLA
-- IDX_FEDERACAO_COD_UNIMED / UK_FEDERACAO_COD_UNIMED
-- IDX_SINGULAR_SIGLA / UK_SINGULAR_SIGLA
-- IDX_SINGULAR_COD_UNIMED / UK_SINGULAR_COD_UNIMED
-- IDX_COLABORADOR_EMAIL / UK_COLABORADOR_EMAIL
-- IDX_CATEGORIA_DOCUMENTAL_NOME / UK_CATEGORIA_DOCUMENTAL_NOME
-- IDX_ARQUIVO_HASH / UK_ARQUIVO_HASH
-- IDX_PAPEL_NOME / UK_PAPEL_NOME
-- IDX_CONFIG_PORT_FED / UK_CONFIG_PORT_FED
-- Remoção sugerida em ciclo futuro após validação de planos de execução.
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- FIM DO SCRIPT
--------------------------------------------------------------------------------

PROMPT
PROMPT ==========================================================
PROMPT Todos os índices foram criados com sucesso.
PROMPT Fim do script 005-create-indexes.sql
PROMPT ==========================================================
PROMPT