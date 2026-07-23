--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 009-configuracao-portal.sql
-- Versão  : 1.0
--
-- Objetivo
--   Configuração padrão do portal vinculada à Federação raiz (COD_FEDERACAO).
--
-- Dependências
--   dml/001-federacao.sql
--
-- Executar como
--   UNMPORTCOM — incluído em ddl/000-install.sql após dml/001-federacao.sql
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

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
    WHERE f.NOM_FEDERACAO = 'Unimed Ceará'
      AND f.FLG_ATIVO = 'S'
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
