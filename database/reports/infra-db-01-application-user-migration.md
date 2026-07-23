# INFRA-DB-01 — Relatório de migração Application User

| Campo | Valor |
|-------|-------|
| Atividade | INFRA-DB-01 |
| Decisão | DEC-DB-024 |
| Data | 2026-07-23 |
| Status | Concluída (repositório) — validação Oracle depende de `.env` com `UNMPORTCOM_APP` |

---

## 1. Diagnóstico

### Estado anterior

| Área | Achado |
|------|--------|
| Datasource | Já parametrizado via `SPRING_DATASOURCE_*` (sem hardcode de usuário no código) |
| `.env.example` | `SPRING_DATASOURCE_USERNAME` vazio — risco de conexão como owner por convenção local |
| Documentação raiz | Referências legadas `DB_URL` / `DB_USERNAME` |
| `database/security/` | Referenciado em `GOVERNANCE.md` e `database/README.md`, mas **ausente** no repositório |
| Hibernate `default_schema` | Duplicado em perfis `dev`/`hml`/`prod`/`test`; correto como `UNMPORTCOM` (owner lógico) |
| Entidades JPA | `@Table(schema = "UNMPORTCOM")` — **correto** (não é usuário JDBC) |
| SQL nativo | Apenas `SELECT 1 FROM DUAL` e auditoria `SchemaOracleAuditTest` (`ALL_*` com owner `UNMPORTCOM`) |
| Docker Compose | Postgres local — sem usuário Oracle (fora do escopo Oracle corporativo) |

### Interpretação DEC-DB-024

- **Eliminar dependência do owner** = não usar `UNMPORTCOM` em `spring.datasource.username`.
- **Manter `UNMPORTCOM` em JPA** = qualificador do schema físico dos objetos.

---

## 2. Configurações alteradas

| Arquivo | Alteração |
|---------|-----------|
| `backend/src/main/resources/application.yaml` | `hibernate.default_schema` centralizado via env |
| `backend/src/main/resources/application-{dev,hml,prod}.yaml` | Remoção de `default_schema` duplicado |
| `backend/src/test/resources/application-test.yaml` | `default_schema` via placeholder env |
| `.env.example` | `SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP` + comentários DEC-DB-024 |
| `README.md` | Variáveis `SPRING_DATASOURCE_*` |
| `docs/implementation/06-database-standards.md` | Seção owner × application user |
| `docs/implementation/13-integration-test-database-strategy.md` | Usuário e teste de conexão |
| `docs/construction/backend/01-project-bootstrap.md` | Alinhamento datasource |
| `database/ddl/007-create-grants.sql` | Referência ao SSOT `database/security/` |
| `database/security/**` | **Criado** — SSOT grants + validação + checklist |

### Novos testes

| Classe | Função |
|--------|--------|
| `ApplicationUserConnectionIntegrationTest` | Perfil `test`: `USER` = `UNMPORTCOM_APP` |
| `OraclePersistenceIntegrationTest` | Perfil `local`: mesma asserção + `SPRING_DATASOURCE_URL` |

---

## 3. Features validadas (análise estática)

Persistência implementada no backend (entidades + testes de aceite documentados):

| Feature | Entidades / tabelas | Dependência owner na conexão |
|---------|---------------------|------------------------------|
| FT-AUTH | `AUTH_SESSAO`, `COLABORADOR` | Não (grants em `007` / `security`) |
| FT-COLABORADOR | `COLABORADOR` | Não |
| FT-AREA | `AREA` | Não |
| FT-EQUIPE | `EQUIPE` | Não |
| FT-SINGULAR | `SINGULAR`, `FEDERACAO` | Não |
| FT-SESSION | `AUTH_SESSAO` | Não |
| FT-DOCUMENTO / FT-PASTA / FT-NOTIFICACAO | Não implementadas no backend atual | N/A (grants baseline já em `security`) |

Nenhum `nativeQuery` referencia objetos acessíveis somente ao owner além de views `ALL_*` em teste de auditoria (metadado).

---

## 4. Evidências de execução

Executar localmente (com Oracle e grants aplicados):

```bash
# .env na raiz: SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP
cd backend && mvn test -Dtest=ApplicationUserConnectionIntegrationTest
```

Evidência esperada: teste verde com `USER = UNMPORTCOM_APP`.

Validação DBA:

```text
database/security/validate/validate-application-user.sql  (como UNMPORTCOM_APP)
```

---

## 5. Inconsistências / riscos

| ID | Descrição | Ação |
|----|-----------|------|
| INC-01 | Ambientes com `.env` ainda usando `UNMPORTCOM` como username falharão em `ApplicationUserConnectionIntegrationTest` | Atualizar credenciais locais/CI |
| INC-02 | `database/migrations/V003__*.sql` referencia `SQ_AUTH_SESSAO_COD_SESSAO`; baseline DDL usa `SQ_AUTH_SESSAO` | Reconciliar em migration/DBA (pré-existente) |
| INC-03 | `docs/technology/03-environment-strategy.md` ainda lista `DB_URL` / `DB_USERNAME` | Evolução documental futura (fora do escopo mínimo) |
| INC-04 | `SchemaOracleAuditTest` usa `ALL_TAB_COLUMNS` com owner `UNMPORTCOM` — válido para APP user com privilégio de leitura de dicionário | Monitorar `ORA-00942` se grants de metadata faltarem |

---

## 6. Governança consolidada

Regra permanente (GOV-DB-05 + `database/security/CHECKLIST.md`):

```text
Novo objeto Oracle → DDL/migration → database/security/grants → validação DBA → backend com UNMPORTCOM_APP
```

---

## 7. Recomendações

1. Pipeline CI: definir `SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP` nos secrets e executar `ApplicationUserConnectionIntegrationTest` no job Oracle.
2. Adicionar gate em code review: diff em `database/migrations/` sem diff em `database/security/` → rejeitar.
3. Unificar documentação de ambiente em `docs/technology/03-environment-strategy.md` com `SPRING_DATASOURCE_*`.

---

## 8. Critérios de aceite

| Critério | Atendido |
|----------|----------|
| Backend configurado para `UNMPORTCOM_APP` | Sim (`.env.example` + testes) |
| Nenhuma config com owner como usuário JDBC | Sim (código YAML) |
| `database/security/` como SSOT | Sim (criado e referenciado) |
| Documentação alinhada | Sim (README, 06, 13, bootstrap) |
| Governança para novos objetos | Sim (CHECKLIST + GOVERNANCE) |
| Execução Oracle nesta sessão | Pendente — depende de credenciais no ambiente do operador |
