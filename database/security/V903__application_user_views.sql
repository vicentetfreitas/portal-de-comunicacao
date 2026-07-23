--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V903__application_user_views.sql
-- Versão  : 1.0
--
-- Inventário (database/ — baseline 2026-07-22)
--   VIEW: nenhuma versionada para consumo do backend
--   MATERIALIZED VIEW: nenhuma
--
-- O backend acessa exclusivamente tabelas mapeadas em JPA.
-- Este script não concede privilégios até que uma Feature aprovada
-- introduza views no baseline.
--
-- Executar como
--   UNMPORTCOM (somente documentação)
--------------------------------------------------------------------------------

SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT V903 — Views: nenhuma no escopo atual
PROMPT ==========================================================
PROMPT Quando uma VIEW for criada em UNMPORTCOM:
PROMPT   GRANT SELECT ON UNMPORTCOM.<VIEW> TO UNMPORTCOM_APP_ROLE;
PROMPT Atualizar também V900/V903 e ddl/007-create-grants.sql
PROMPT ==========================================================
