--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 008-initial-data.sql
-- Versão  : 2.1
--
-- Objetivo
--   Inserir os dados mínimos necessários para inicialização do sistema.
--   Script idempotente: pode ser executado múltiplas vezes sem duplicar dados.
--
-- Geração de PK
--   Sem DEFAULT nas tabelas (DEC-DB-018). PKs informadas explicitamente via
--   SQ_<TABELA>_<CAMPO>.NEXTVAL neste script.
--
-- Banco
--   Oracle Database 11g+
--
-- Dependências
--   001-create-users.sql
--   002-create-sequences.sql
--   003-create-tables.sql
--   004-create-constraints.sql
--   005-create-indexes.sql
--   006-create-comments.sql
--   007-create-grants.sql
--
-- Executar como
--   UNMPORTCOM
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Carga Inicial (Idempotente)
PROMPT ==========================================================

--------------------------------------------------------------------------------
-- FEDERAÇÃO
--------------------------------------------------------------------------------

MERGE INTO FEDERACAO f
USING (
    SELECT
        'Federação Unimed Ceará'                          AS NOM_FEDERACAO,
        'FEC'                                             AS SIG_FEDERACAO,
        '0000'                                            AS COD_UNIMED,
        '000000'                                          AS NUM_REGISTRO_ANS,
        'Federação administradora do Portal de Comunicação.' AS DSC_FEDERACAO,
        'S'                                               AS FLG_ATIVO
    FROM DUAL
) src
ON (f.COD_UNIMED = src.COD_UNIMED)
WHEN NOT MATCHED THEN
    INSERT (
        COD_FEDERACAO,
        NOM_FEDERACAO,
        SIG_FEDERACAO,
        COD_UNIMED,
        NUM_REGISTRO_ANS,
        DSC_FEDERACAO,
        FLG_ATIVO
    )
    VALUES (
        SQ_FEDERACAO_COD_FEDERACAO.NEXTVAL,
        src.NOM_FEDERACAO,
        src.SIG_FEDERACAO,
        src.COD_UNIMED,
        src.NUM_REGISTRO_ANS,
        src.DSC_FEDERACAO,
        src.FLG_ATIVO
    );

--------------------------------------------------------------------------------
-- CATEGORIAS DOCUMENTAIS
--------------------------------------------------------------------------------

MERGE INTO CATEGORIA_DOCUMENTAL c
USING (
    SELECT 'Normativos'    AS NOM_CATEGORIA, 'Normas e regulamentos corporativos.' AS DSC_CATEGORIA, 'S' AS FLG_ATIVO FROM DUAL UNION ALL
    SELECT 'Manuais',       'Manuais operacionais.',                             'S' FROM DUAL UNION ALL
    SELECT 'Políticas',     'Políticas institucionais.',                         'S' FROM DUAL UNION ALL
    SELECT 'Procedimentos', 'Procedimentos operacionais.',                       'S' FROM DUAL UNION ALL
    SELECT 'Comunicados',   'Documentos de comunicação interna.',              'S' FROM DUAL
) src
ON (c.NOM_CATEGORIA = src.NOM_CATEGORIA)
WHEN NOT MATCHED THEN
    INSERT (
        COD_CATEGORIA_DOCUMENTAL,
        NOM_CATEGORIA,
        DSC_CATEGORIA,
        FLG_ATIVO
    )
    VALUES (
        SQ_CAT_DOC_COD_CAT_DOC.NEXTVAL,
        src.NOM_CATEGORIA,
        src.DSC_CATEGORIA,
        src.FLG_ATIVO
    );

--------------------------------------------------------------------------------
-- PAPÉIS
--------------------------------------------------------------------------------

MERGE INTO PAPEL p
USING (
    SELECT 'ADMINISTRADOR'       AS NOM_PAPEL, 'Administrador do Portal.'                        AS DSC_PAPEL, 'S' AS FLG_ATIVO FROM DUAL UNION ALL
    SELECT 'GESTOR_DOCUMENTAL',   'Responsável pela gestão documental.',             'S' FROM DUAL UNION ALL
    SELECT 'EDITOR',              'Responsável pela publicação de conteúdos.',        'S' FROM DUAL UNION ALL
    SELECT 'COLABORADOR',         'Usuário padrão do Portal.',                       'S' FROM DUAL
) src
ON (p.NOM_PAPEL = src.NOM_PAPEL)
WHEN NOT MATCHED THEN
    INSERT (
        COD_PAPEL,
        NOM_PAPEL,
        DSC_PAPEL,
        FLG_ATIVO
    )
    VALUES (
        SQ_PAPEL_COD_PAPEL.NEXTVAL,
        src.NOM_PAPEL,
        src.DSC_PAPEL,
        src.FLG_ATIVO
    );

--------------------------------------------------------------------------------
-- CONFIGURAÇÃO PADRÃO
--------------------------------------------------------------------------------

MERGE INTO CONFIGURACAO_PORTAL cp
USING (
    SELECT
        f.COD_FEDERACAO,
        'Portal de Comunicação'              AS NOM_PORTAL,
        'https://portal.unimedceara.com.br'  AS URL_PORTAL,
        'S'                                  AS FLG_ONBOARDING_ATIVO,
        'S'                                  AS FLG_NOTIFICACAO_EMAIL,
        'S'                                  AS FLG_COMUNICADO_DESTAQUE,
        365                                  AS QTD_DIAS_EXPIRACAO_DOCUMENTO
    FROM FEDERACAO f
    WHERE f.COD_UNIMED = '0000'
) src
ON (cp.COD_FEDERACAO = src.COD_FEDERACAO)
WHEN NOT MATCHED THEN
    INSERT (
        COD_CONFIGURACAO_PORTAL,
        COD_FEDERACAO,
        NOM_PORTAL,
        URL_PORTAL,
        FLG_ONBOARDING_ATIVO,
        FLG_NOTIFICACAO_EMAIL,
        FLG_COMUNICADO_DESTAQUE,
        QTD_DIAS_EXPIRACAO_DOCUMENTO
    )
    VALUES (
        SQ_CONFIG_PORT_COD_CONFIG_PORT.NEXTVAL,
        src.COD_FEDERACAO,
        src.NOM_PORTAL,
        src.URL_PORTAL,
        src.FLG_ONBOARDING_ATIVO,
        src.FLG_NOTIFICACAO_EMAIL,
        src.FLG_COMUNICADO_DESTAQUE,
        src.QTD_DIAS_EXPIRACAO_DOCUMENTO
    );

COMMIT;

--------------------------------------------------------------------------------
-- Validação
--------------------------------------------------------------------------------

PROMPT.
PROMPT Registros de referência:
PROMPT.

SELECT 'FEDERACAO' AS ENTIDADE, COUNT(*) AS QTD FROM FEDERACAO
UNION ALL
SELECT 'CATEGORIA_DOCUMENTAL', COUNT(*) FROM CATEGORIA_DOCUMENTAL
UNION ALL
SELECT 'PAPEL', COUNT(*) FROM PAPEL
UNION ALL
SELECT 'CONFIGURACAO_PORTAL', COUNT(*) FROM CONFIGURACAO_PORTAL;

PROMPT.
PROMPT ==========================================================
PROMPT Dados iniciais carregados com sucesso.
PROMPT Fim do script 008-initial-data.sql
PROMPT ==========================================================
PROMPT.
