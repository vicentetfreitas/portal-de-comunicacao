--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : VAL-DB-05-verify-homologacao-admin-prereqs.sql
-- Versão  : 1.0
--
-- Objetivo
--   Conferência read-only ANTES de aplicar V011 (dados de homologação —
--   admin de área + pasta de teste). Não altera nada. Executar como UNMPORTCOM.
--------------------------------------------------------------------------------

-- 1. Colaborador alvo (esperado: 1 linha, FLG_ATIVO='S', COD_AREA=1)
SELECT cod_colaborador, nom_colaborador, des_email, cod_federacao, cod_singular, cod_area, cod_equipe, flg_ativo
  FROM UNMPORTCOM.COLABORADOR
 WHERE cod_colaborador = 1335;

-- 2. Área alvo (esperado: COD_AREA=1, NOM_AREA='TECNOLOGIA DA INFORMAÇÃO', FLG_ATIVO='S')
SELECT cod_area, cod_singular, nom_area, flg_ativo
  FROM UNMPORTCOM.AREA
 WHERE cod_area = 1;

-- 3. Tabelas que V011 popula (esperado ANTES de V011: 0 em todas)
SELECT 'PAPEL'            AS tabela, COUNT(*) AS linhas FROM UNMPORTCOM.PAPEL
UNION ALL SELECT 'PAPEL_ATRIBUICAO', COUNT(*) FROM UNMPORTCOM.PAPEL_ATRIBUICAO
UNION ALL SELECT 'PASTA',            COUNT(*) FROM UNMPORTCOM.PASTA
UNION ALL SELECT 'PERMISSAO_PASTA',  COUNT(*) FROM UNMPORTCOM.PERMISSAO_PASTA
UNION ALL SELECT 'DOCUMENTO',        COUNT(*) FROM UNMPORTCOM.DOCUMENTO;

-- 4. CATEGORIA_DOCUMENTAL (esperado: 4 linhas; V011 usa a categoria 1 = Documentos)
SELECT cod_categoria_documental, nom_categoria, flg_ativo
  FROM UNMPORTCOM.CATEGORIA_DOCUMENTAL
 ORDER BY cod_categoria_documental;

-- 5. Sequences necessárias (esperado: SQ_PASTA, SQ_PERMISSAO_PASTA,
--    SQ_DOCUMENTO_COD_DOCUMENTO, SQ_DOCUMENTO_VERSAO, SQ_ARQUIVO_BINARIO)
SELECT sequence_name
  FROM user_sequences
 WHERE sequence_name IN (
       'SQ_PASTA', 'SQ_PERMISSAO_PASTA', 'SQ_DOCUMENTO_COD_DOCUMENTO',
       'SQ_DOCUMENTO_VERSAO', 'SQ_ARQUIVO_BINARIO')
 ORDER BY sequence_name;
