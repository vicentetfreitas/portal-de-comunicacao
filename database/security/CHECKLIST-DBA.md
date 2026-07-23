# Checklist DBA — Application User (`UNMPORTCOM_APP`)

Use este checklist em **cada** novo ambiente Oracle (DEV/HML/PRD) ou após migrations estruturais.

**Decisão:** DEC-DB-024 · **Scripts:** `database/security/`

---

## 1. Pré-requisitos

- [ ] Oracle 11g+ acessível
- [ ] Tablespace `USERS` (ou corporativo definido) disponível
- [ ] Usuário SYS/DBA para criação de contas
- [ ] Repositório na tag/commit com SSOT `database/` desejada

---

## 2. Criação de usuários

- [ ] Executar `database/ddl/001-create-users.sql` como **SYS/DBA**
- [ ] Confirmar usuários `UNMPORTCOM` e `UNMPORTCOM_APP` em `DBA_USERS`
- [ ] Confirmar role `UNMPORTCOM_APP_ROLE` criada
- [ ] `UNMPORTCOM_APP` com `QUOTA 0` (não cria objetos)

---

## 3. Schema (owner)

- [ ] **Greenfield:** `database/ddl/000-install.sql` como **UNMPORTCOM**
- [ ] **Brownfield:** migrations aplicadas na ordem de `database/migrations/README.md`
- [ ] `database/ddl/901-validation.sql` sem erros críticos (owner)

---

## 4. Grants — tabelas

- [ ] Executar `database/security/V900__application_user_grants.sql` como **UNMPORTCOM**
  - [ ] Ou confirmar que `007-create-grants.sql` foi executado no install e está atualizado
- [ ] 23 tabelas com `SELECT, INSERT, UPDATE, DELETE` para `UNMPORTCOM_APP_ROLE`
- [ ] `GRANT UNMPORTCOM_APP_ROLE TO UNMPORTCOM_APP`

---

## 5. Grants — sequences

- [ ] Executar `database/security/V902__application_user_sequences.sql` como **UNMPORTCOM**
- [ ] 12 sequences baseline com `GRANT SELECT`
- [ ] Se existirem: `SQ_AUTH_SESSAO_COD_SESSAO`, `SQ_COLABORADOR_COD_COLABORADOR`

---

## 6. Views e synonyms

- [ ] Ler `V901` — **nenhum** synonym necessário (confirmado)
- [ ] Ler `V903` — **nenhuma** view no escopo (n/a)

---

## 7. Validação técnica

- [ ] Conectar como **UNMPORTCOM_APP**
- [ ] Executar `database/security/VAL-SEC-01-verify-application-privileges.sql`
- [ ] `ALL_TAB_PRIVS` / role: privilégios sobre tabelas `UNMPORTCOM.*`
- [ ] Teste: `SELECT COUNT(*) FROM UNMPORTCOM.FEDERACAO` (ou 1 tabela representativa)

---

## 8. Testes de aplicação

- [ ] Datasource configurado com `UNMPORTCOM_APP` (nunca owner)
- [ ] `hibernate.default_schema=UNMPORTCOM`
- [ ] `ddl-auto=validate` — aplicação sobe sem `missing table`
- [ ] Smoke test: operação que insere registro com sequence (ex.: auth/sessão ou cadastro org)

---

## 9. Rollback (se necessário)

- [ ] Documentar revogações (`REVOKE` por tabela/sequence)
- [ ] `REVOKE UNMPORTCOM_APP_ROLE FROM UNMPORTCOM_APP` apenas se descomissionar app user
- [ ] Drop completo de schema: `database/ddl/900-drop-all.sql` (planejamento DBA)

---

## 10. Pós-migration (evolução contínua)

- [ ] Nova tabela/sequence na migration inclui `GRANT` **ou** reaplicar V900/V902
- [ ] Atualizar `V900`/`V902` e `ddl/007` no repositório (PR de engenharia)
- [ ] Reexecutar VAL-SEC-01 no ambiente afetado

---

## Registro

| Campo | Valor |
|-------|--------|
| Ambiente | |
| Data | |
| DBA | |
| Commit/tag `database/` | |
| VAL-SEC-01 OK | Sim / Não |
| Backend validate OK | Sim / Não |
