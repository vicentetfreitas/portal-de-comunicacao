--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : 000-install.sql
-- Versão  : 1.0
--
-- Objetivo
--   Orquestrar a instalação completa do schema UNMPORTCOM.
--
-- Banco
--   Oracle Database 11g+
--
-- Execução
--   Etapa 1: conectar como SYS (DBA) e executar 001-create-users.sql
--   Etapa 2: conectar como UNMPORTCOM e executar os demais scripts
--
-- Uso (SQL*Plus / SQLcl)
--   @000-install.sql
--------------------------------------------------------------------------------

SET ECHO ON
SET FEEDBACK ON
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Portal de Comunicação
PROMPT Instalação Completa do Schema
PROMPT ==========================================================
PROMPT.
PROMPT ATENÇÃO:
PROMPT   1. Execute 001-create-users.sql como SYS/DBA (senhas interativas).
PROMPT   2. Conecte-se como UNMPORTCOM antes de continuar.
PROMPT.
PAUSE Pressione ENTER para continuar a instalação como UNMPORTCOM...

@@002-create-sequences.sql
@@003-create-tables.sql
@@004-create-constraints.sql
@@005-create-indexes.sql
@@006-create-comments.sql
@@007-create-grants.sql
@@008-initial-data.sql

PROMPT.
PROMPT ==========================================================
PROMPT Instalação concluída.
PROMPT Execute @@901-validation.sql para validar o ambiente.
PROMPT ==========================================================
PROMPT.
