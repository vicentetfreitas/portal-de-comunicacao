--------------------------------------------------------------------------------
-- Projeto : Portal de Comunicação
-- Arquivo : V008__singular_email_domain.sql
-- Versão  : 1.0
--
-- Objetivo
--   Brownfield GAP-028-04: persistir domínio de e-mail em SINGULAR
--   (DEC-ORG-003, DH-PA-02, BR-043, BR-044).
--
-- Banco
--   Oracle Database 11g+
--
-- Executar como
--   UNMPORTCOM
--
-- Pré-requisito
--   Tabela SINGULAR existente com UNMCEA, UNMCAR e UNMSOB.
--   Não inserir Singulares neste script.
--
-- Greenfield
--   Coluna e UK já estão em ddl/003 e ddl/004; não aplicar V008 após 000-install.
--------------------------------------------------------------------------------

SET DEFINE OFF
SET SERVEROUTPUT ON

PROMPT ==========================================================
PROMPT Evolução V008 — SINGULAR.DES_DOMINIO_EMAIL (GAP-028-04)
PROMPT ==========================================================

ALTER TABLE SINGULAR
    ADD DES_DOMINIO_EMAIL VARCHAR2(255);

ALTER TABLE SINGULAR
    ADD CONSTRAINT UK_SINGULAR_DOMINIO_EMAIL
    UNIQUE (DES_DOMINIO_EMAIL);

UPDATE SINGULAR
   SET DES_DOMINIO_EMAIL = 'unimedceara.com.br'
 WHERE SIG_SINGULAR = 'UNMCEA'
   AND DES_DOMINIO_EMAIL IS NULL;

UPDATE SINGULAR
   SET DES_DOMINIO_EMAIL = 'unimedcariri.com.br'
 WHERE SIG_SINGULAR = 'UNMCAR'
   AND DES_DOMINIO_EMAIL IS NULL;

COMMIT;

PROMPT V008 concluída. UNMSOB permanece sem UPDATE (DES_DOMINIO_EMAIL NULL).
PROMPT ==========================================================
