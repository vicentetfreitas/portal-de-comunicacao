--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V010__pasta_permissao_pasta_sequences.sql
-- Versão  : 1.0
--
-- Objetivo
--   Brownfield FT-DOCUMENTO-GESTAO (TK-DOC-GESTAO-001): habilitar a ESCRITA de
--   pastas em Gestão Documental. Hoje o backend só LÊ PASTA / PERMISSAO_PASTA
--   (FT-DOCUMENTO); para criar subpasta (RF-DOC-GESTAO-001) e copiar os grants
--   da pasta-pai (decisions.md D-04) faltam duas sequences.
--
-- Escopo (mínimo)
--   1. SQ_PASTA           -> PASTA.COD_PASTA
--   2. SQ_PERMISSAO_PASTA -> PERMISSAO_PASTA.COD_PERMISSAO_PASTA
--      + GRANT SELECT das duas para UNMPORTCOM_APP_ROLE (DEC-DB-024 / JPA SEQUENCE)
--
-- Fora do escopo (não é preciso agora)
--   - Nenhuma alteração de tabela: PASTA e PERMISSAO_PASTA já existem e o
--     UNMPORTCOM_APP_ROLE já tem SELECT/INSERT/UPDATE/DELETE nas duas
--     (verificado via JDBC no Oracle TST em 2026-08-27).
--   - CRUD de documento já enviado (nova versão, mover, arquivar) reusa
--     SQ_DOCUMENTO_VERSAO / SQ_ARQUIVO_BINARIO (criadas no V009).
--
-- Contexto verificado (Oracle TST, UNMPORTCOM_APP, 2026-08-27)
--   - SQ_PASTA e SQ_PERMISSAO_PASTA: NÃO existem.
--   - PASTA e PERMISSAO_PASTA: existem, 0 linhas -> START WITH 1.
--   - Colunas COD_PASTA / COD_PERMISSAO_PASTA: NUMBER(19) NOT NULL, sem IDENTITY.
--
-- Banco       : Oracle Database 11g+
-- Executar como: UNMPORTCOM  (SQL Developer / DBeaver — comandos simples)
-- Pré-check   : VAL-DB-04-verify-pasta-write-prereqs.sql
--
-- Decisões: DEC-DB-019 (sem Flyway), DEC-DB-024 (role da aplicação usa NEXTVAL)
--------------------------------------------------------------------------------


-- 1. Sequences ---------------------------------------------------------------

CREATE SEQUENCE SQ_PASTA           START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;

CREATE SEQUENCE SQ_PERMISSAO_PASTA START WITH 1 INCREMENT BY 1 CACHE 20 NOCYCLE;


-- 2. Grants (DEC-DB-024 — role da aplicação usa NEXTVAL via JPA) -------------

GRANT SELECT ON UNMPORTCOM.SQ_PASTA           TO UNMPORTCOM_APP_ROLE;

GRANT SELECT ON UNMPORTCOM.SQ_PERMISSAO_PASTA TO UNMPORTCOM_APP_ROLE;


-- 3. Conferência (rodar depois de aplicar) ---------------------------------

-- sequences (esperado: 2 linhas)
SELECT sequence_name, cache_size, last_number
  FROM user_sequences
 WHERE sequence_name IN ('SQ_PASTA', 'SQ_PERMISSAO_PASTA')
 ORDER BY sequence_name;

-- grants (esperado: 2 linhas, privilege = SELECT)
SELECT table_name AS sequence_name, privilege
  FROM user_tab_privs
 WHERE grantee = 'UNMPORTCOM_APP_ROLE'
   AND table_name IN ('SQ_PASTA', 'SQ_PERMISSAO_PASTA')
 ORDER BY table_name;
