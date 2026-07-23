--------------------------------------------------------------------------------
-- VAL-DB-02 — Verificação de colunas COLABORADOR vs baseline DDL
-- Executar como UNMPORTCOM
-- Referência: database/ddl/003-create-tables.sql, DEC-DB-020
--------------------------------------------------------------------------------

SET LINESIZE 200
SET PAGESIZE 100

PROMPT Colunas organizacionais e corporativas esperadas no baseline:

SELECT column_name, data_type, nullable
  FROM user_tab_columns
 WHERE table_name = 'COLABORADOR'
   AND column_name IN (
     'COD_FEDERACAO',
     'COD_SINGULAR',
     'COD_AREA',
     'COD_EQUIPE',
     'COD_GESTOR',
     'DES_BIOGRAFIA',
     'DAT_NASCIMENTO',
     'DAT_CONTRATACAO',
     'ID_ZIMBRA',
     'NOM_COLABORADOR'
   )
 ORDER BY column_name;

PROMPT
PROMPT Esperado: 10 linhas. Menos que isso => aplicar V004/V007 ou baseline completo (DBA).
PROMPT Colunas legadas DES_CARGO e NUM_CPF não devem existir após V007.

SELECT column_name
  FROM user_tab_columns
 WHERE table_name = 'COLABORADOR'
   AND column_name IN ('DES_CARGO', 'NUM_CPF');
