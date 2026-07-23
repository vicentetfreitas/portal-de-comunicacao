--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V901__application_user_synonyms.sql
-- Versão  : 1.0
--
-- Decisão DEC-DB-024: SYNONYMS NÃO são utilizados para acesso da aplicação.
--
-- Motivo
--   O backend JPA mapeia explicitamente schema e objeto:
--     @Table(name = "FEDERACAO", schema = "UNMPORTCOM")
--   Com privilégios GRANT do owner para UNMPORTCOM_APP_ROLE, o Hibernate valida
--   e acessa UNMPORTCOM.<TABELA> sem necessidade de synonym público/privado.
--
-- Synonyms adicionariam
--   - superfície de manutenção (um objeto por tabela);
--   - risco de divergência entre synonym e nome físico;
--   - complexidade em auditoria de privilégios.
--
-- Quando synonyms seriam aceitáveis
--   Apenas se um consumidor legado exigisse nomes sem qualificação de schema
--   e não pudesse usar default_schema — não é o caso do Portal de Comunicação.
--
-- Ação deste script
--   Nenhum CREATE SYNONYM. Documentação e validação opcional.
--
-- Executar como
--   Qualquer usuário (somente PROMPT)
--------------------------------------------------------------------------------

SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT V901 — Synonyms: NÃO APLICÁVEL (decisão arquitetural)
PROMPT ==========================================================
PROMPT Backend usa @Table(schema="UNMPORTCOM") + GRANT via role.
PROMPT Nenhum synonym será criado para UNMPORTCOM_APP.
PROMPT Ver DEC-DB-024 e database/security/OPERATIONS.md
PROMPT ==========================================================
