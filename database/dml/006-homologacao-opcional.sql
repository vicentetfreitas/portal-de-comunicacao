--------------------------------------------------------------------------------
-- Portal de Comunicação | dml/006-homologacao-opcional.sql
-- Pré-requisito: dml/004-equipes.sql
--------------------------------------------------------------------------------

SET DEFINE OFF

INSERT INTO AREA (COD_AREA, COD_SINGULAR, NOM_AREA, SIG_AREA, DSC_AREA, FLG_ATIVO, DAT_CADASTRO)
SELECT SQ_AREA_COD_AREA.NEXTVAL,
       s.COD_SINGULAR,
       'Relacionamento com Cliente',
       'REL_CLIENTE',
       'Área regional de relacionamento e comunicação com beneficiários.',
       'S',
       SYSTIMESTAMP
  FROM SINGULAR s
 WHERE s.SIG_SINGULAR IN ('CARIRI', 'SOBRAL', 'ARACATI');

INSERT INTO EQUIPE (COD_EQUIPE, COD_AREA, NOM_EQUIPE, DSC_EQUIPE, FLG_ATIVO, DAT_CADASTRO)
SELECT SQ_EQUIPE_COD_EQUIPE.NEXTVAL,
       a.COD_AREA,
       'Canal de Atendimento',
       'Equipe de atendimento e suporte ao beneficiário na singular.',
       'S',
       SYSTIMESTAMP
  FROM AREA a
  JOIN SINGULAR s ON s.COD_SINGULAR = a.COD_SINGULAR
 WHERE a.SIG_AREA = 'REL_CLIENTE';

COMMIT;
