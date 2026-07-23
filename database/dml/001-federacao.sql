--------------------------------------------------------------------------------
-- Portal de Comunicação | dml/001-federacao.sql
-- Pré-requisito: ddl/008-initial-data.sql
--------------------------------------------------------------------------------

SET DEFINE OFF

INSERT INTO FEDERACAO (
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
    'Unimed Federação',
    'UNMFED',
     979,
    '32195-8',
    'Federação administradora do Portal de Comunicação.',
    'S'
);

COMMIT;
