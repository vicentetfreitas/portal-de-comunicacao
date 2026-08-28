--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : VAL-DB-04-verify-pasta-write-prereqs.sql
-- Versão  : 1.0
--
-- Objetivo
--   Conferência read-only ANTES de aplicar V010 (FT-DOCUMENTO-GESTAO).
--   Não altera nada. Executar como UNMPORTCOM.
--------------------------------------------------------------------------------

-- 1. Tabelas alvo (esperado: PASTA, PERMISSAO_PASTA)
SELECT table_name
  FROM user_tables
 WHERE table_name IN ('PASTA', 'PERMISSAO_PASTA')
 ORDER BY table_name;

-- 2. Sequences que V010 vai criar (esperado ANTES de V010: 0 linhas)
SELECT sequence_name
  FROM user_sequences
 WHERE sequence_name IN ('SQ_PASTA', 'SQ_PERMISSAO_PASTA');

-- 3. Volume das tabelas (esperado: 0 -> V010 pode usar START WITH 1;
--    se > 0, ajustar START WITH para MAX(COD_*)+1 antes de aplicar)
SELECT 'PASTA'           AS tabela, COUNT(*) AS linhas, NVL(MAX(cod_pasta), 0)            AS max_id FROM PASTA
UNION ALL
SELECT 'PERMISSAO_PASTA' AS tabela, COUNT(*) AS linhas, NVL(MAX(cod_permissao_pasta), 0) AS max_id FROM PERMISSAO_PASTA;

-- 4. DML da aplicação já concedido? (esperado: INSERT presente nas duas tabelas)
SELECT table_name, privilege
  FROM role_tab_privs
 WHERE role = 'UNMPORTCOM_APP_ROLE'
   AND table_name IN ('PASTA', 'PERMISSAO_PASTA')
 ORDER BY table_name, privilege;

-- 5. Coluna PK (esperado: NUMBER, NOT NULL, sem IDENTITY -> PK via sequence)
SELECT table_name, column_name, data_type, nullable
  FROM user_tab_columns
 WHERE table_name IN ('PASTA', 'PERMISSAO_PASTA')
   AND column_name IN ('COD_PASTA', 'COD_PERMISSAO_PASTA')
 ORDER BY table_name;

-- 6. Role da aplicação existe? (esperado: 1 linha)
SELECT role FROM dba_roles WHERE role = 'UNMPORTCOM_APP_ROLE';
