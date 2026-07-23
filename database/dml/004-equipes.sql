--------------------------------------------------------------------------------
-- Portal de Comunicação | dml/004-equipes.sql
-- Pré-requisito: dml/003-areas.sql
--------------------------------------------------------------------------------

SET DEFINE OFF

INSERT INTO EQUIPE (COD_EQUIPE, COD_AREA, NOM_EQUIPE, DSC_EQUIPE, FLG_ATIVO, DAT_CADASTRO)
SELECT SQ_EQUIPE_COD_EQUIPE.NEXTVAL,
       a.COD_AREA,
       'Desenvolvimento',
       'Equipe responsável por melhorias, suporte e manutenção do portal de comunicação.',
       'S',
       SYSTIMESTAMP
  FROM area a
 WHERE a.COD_AREA         = 1
       AND a.cod_singular = 2
       AND a.SIG_AREA     = 'TI'
       AND a.flg_ativo    = 'S'
       ;

INSERT INTO EQUIPE (COD_EQUIPE, COD_AREA, NOM_EQUIPE, DSC_EQUIPE, FLG_ATIVO, DAT_CADASTRO)
SELECT SQ_EQUIPE_COD_EQUIPE.NEXTVAL,
       a.COD_AREA,
       'Sustentação de Sistemas',
       'Equipe de sustentação do Portal e integrações corporativas.',
       'S',
       SYSTIMESTAMP
  FROM AREA a
 WHERE a.SIG_AREA = 'TI'
   AND a.COD_SINGULAR IS NULL;

INSERT INTO EQUIPE (COD_EQUIPE, COD_AREA, NOM_EQUIPE, DSC_EQUIPE, FLG_ATIVO, DAT_CADASTRO)
SELECT SQ_EQUIPE_COD_EQUIPE.NEXTVAL,
       a.COD_AREA,
       'Gestão Documental Regional',
       'Equipe regional de apoio à gestão documental (piloto Cariri).',
       'S',
       SYSTIMESTAMP
  FROM AREA a
  JOIN SINGULAR s ON s.COD_SINGULAR = a.COD_SINGULAR
 WHERE s.SIG_SINGULAR = 'CARIRI'
   AND a.SIG_AREA = 'GOV_DOC';

COMMIT;
