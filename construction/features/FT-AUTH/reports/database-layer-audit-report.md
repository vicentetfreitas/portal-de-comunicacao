# Auditoria da Camada de Banco — FT-AUTH

> **Nota (DEC-DB-019):** Relatório histórico de 2026-07-09. Referências a Flyway descrevem o estado na data da auditoria. A arquitetura atual administra o schema exclusivamente via baseline DDL (DBA).

> **Nota (DEC-DB-020 / VAL-DB-02):** O `COLABORADOR` mínimo de `V2__access_control.sql` **não** é fonte de verdade. O baseline em `database/ddl/003-create-tables.sql` e a entidade JPA corporativa prevalecem. `ORA-00904` em `COD_SINGULAR` indica Oracle desalinhado do baseline — ver `database/migrations/V004__colaborador_corporate_columns.sql`.

| Item | Valor |
|------|-------|
| Feature | **FT-AUTH** |
| Data | 2026-07-09 |
| Escopo | Modelo físico · entidades JPA · specification · legado backend |
| Executor | auditor |
| Veredito | **Parcialmente consistente** |

---

## Escopo auditado

| Fonte | Artefato |
|-------|----------|
| Migrations Flyway | `backend/src/main/resources/db/migration/V1__baseline.sql`, `V2__access_control.sql` |
| Entidades JPA | `AuthSessaoEntity`, `ColaboradorEntity` |
| Modelo físico | `database/model/03-physical-model.md`, `database/ddl/` |
| Arquitetura auth | `specs/architecture/authentication-architecture.md` § Persistência |
| Specification | `specs/features/authentication/specification.md` § Dependências / Impactos |

---

## 1. Todas as tabelas da FT-AUTH estão implementadas?

**Sim — para o escopo da specification Sprint 1 Backend.**

A specification exige persistência em:

- `AUTH_SESSAO` — Refresh Token, revogação, `session_id`
- Colaborador — localização/criação automática no login

| Tabela exigida | Migration | Entidade JPA | Repository |
|----------------|-----------|--------------|------------|
| `AUTH_SESSAO` | `V2__access_control.sql` | `AuthSessaoEntity` | `AuthSessaoRepository` |
| `COLABORADOR` (mínimo FT-AUTH) | `V2__access_control.sql` | `ColaboradorEntity` | `ColaboradorRepository` |

Não há outras tabelas obrigatórias na specification para os endpoints FT-AUTH (permissões, `FEDERACAO`, papéis permanecem fora do escopo desta Feature).

**Ressalva:** `AUTH_SESSAO` **não consta** no modelo físico oficial (`database/model/`, `database/ddl/`). A tabela existe apenas na migration Flyway `V2`.

---

## 2. Existe alguma entidade JPA sem migration correspondente?

**Não.**

| Entidade | Tabela | Sequence |
|----------|--------|----------|
| `AuthSessaoEntity` | `UNMPORTCOM.AUTH_SESSAO` | `SQ_AUTH_SESSAO` |
| `ColaboradorEntity` | `UNMPORTCOM.COLABORADOR` | `SQ_COLABORADOR` |

Todas as colunas mapeadas nas entidades possuem coluna correspondente em `V2__access_control.sql`.

---

## 3. Existe alguma migration sem entidade correspondente?

**Não — para objetos de tabela.**

`V2__access_control.sql` cria:

| Objeto | Entidade / uso |
|--------|----------------|
| `COLABORADOR` | `ColaboradorEntity` |
| `AUTH_SESSAO` | `AuthSessaoEntity` |
| `SQ_COLABORADOR` | `@SequenceGenerator` em `ColaboradorEntity` |
| `SQ_AUTH_SESSAO` | `@SequenceGenerator` em `AuthSessaoEntity` |
| `IDX_AUTH_SESSAO_COLABORADOR` | suporte a consultas por colaborador — sem entidade (índice) |

`V1__baseline.sql` é placeholder (schema pré-existente); não cria tabelas FT-AUTH.

Não há tabelas órfãs na migration em relação ao código JPA da FT-AUTH.

---

## 4. O modelo físico está sincronizado com as migrations?

**Não.**

### AUTH_SESSAO

| Camada | Status |
|--------|--------|
| Flyway `V2` | ✅ Tabela completa com 10 colunas + constraints |
| Entidades JPA | ✅ Alinhada à migration |
| `database/model/03-physical-model.md` | ❌ **Ausente** |
| `database/ddl/003-create-tables.sql` | ❌ **Ausente** |
| `specs/architecture/authentication-architecture.md` | ⚠️ Modelo conceitual **sem** `ID_SESSAO` (spec e migration incluem) |

### COLABORADOR

A migration `V2` define um **COLABORADOR mínimo** para FT-AUTH, divergente do modelo físico corporativo:

| Aspecto | Flyway `V2` (FT-AUTH) | Modelo físico / DDL baseline |
|---------|----------------------|------------------------------|
| Sequence | `SQ_COLABORADOR` | `SQ_COLABORADOR_COD_COLABORADOR` |
| `ID_ZIMBRA` | ✅ Presente (UK) | ✅ Presente (UK) |
| `ID_ZIMBRA` | Presente | Obrigatório (UK) |
| FKs organizacionais | Apenas `COD_FEDERACAO` (sem FK) | FK → `FEDERACAO`, `SINGULAR`, `AREA`, `EQUIPE` |
| `NOM_COLABORADOR` | `VARCHAR2(255)` | `VARCHAR2(200)` |

**Conclusão:** migrations e JPA estão **entre si consistentes**, mas **não sincronizados** com o modelo físico oficial em `database/`.

---

## 5. Existe script de carga mínima (seed) para desenvolvimento?

**Não — específico para FT-AUTH / Flyway.**

| Artefato | Situação |
|----------|----------|
| Flyway `R__*` / `data.sql` em `backend/` | ❌ Inexistente |
| `database/dml/` | ❌ Vazio |
| `database/ddl/008-initial-data.sql` | Existe para **modelo completo** (`FEDERACAO`, papéis, etc.) — **não** referencia `AUTH_SESSAO` nem o `COLABORADOR` mínimo FT-AUTH |
| Criação de dados em runtime | `ColaboradorService.locateOrCreate` — colaborador criado no primeiro login |
| Testes | `AuthAcceptanceIntegrationTest` faz seed programático (`seedInactiveColaborador`) — fora de migrations |

Para desenvolvimento local com Flyway, **não há seed dedicado**; o fluxo depende de login Zimbra (ou mock) para materializar colaborador e sessão.

---

## 6. É possível executar e testar todos os endpoints da FT-AUTH utilizando apenas as migrations atuais?

**Condicionalmente sim.**

### Cenário A — Banco vazio (somente `V1` + `V2`)

| Endpoint / fluxo | Viável |
|------------------|--------|
| `GET /api/v1/auth/login` → callback | ✅ — cria `COLABORADOR` via `locateOrCreate` |
| `POST /api/v1/auth/refresh` | ✅ — persiste em `AUTH_SESSAO` |
| `GET /api/v1/auth/me` | ✅ |
| `POST /api/v1/auth/logout` | ✅ |
| `DELETE /api/v1/admin/sessions/{sessionId}` | ✅ — authz via config, não tabela extra |

`COD_FEDERACAO` não possui FK na `V2` — valor default (`application.auth.default-federation-id`) funciona sem seed de `FEDERACAO`.

**Dependências externas:** Zimbra (ou cliente mock/configurado) e variáveis de ambiente JWT/DB.

### Cenário B — DDL baseline corporativa já aplicada

`V2` **falhará** ao executar `CREATE TABLE COLABORADOR` — tabela já existe com estrutura diferente (colunas, sequences, FKs).

Neste cenário as migrations atuais **não** são aplicáveis isoladamente; seria necessária migration incremental sobre o baseline, não auditada aqui.

### Testes automatizados (`mvn clean verify`)

| Aspecto | Comportamento |
|---------|---------------|
| Perfil `test` | `spring.flyway.enabled: false` |
| Schema | `hibernate.ddl-auto: create-drop` — **não usa** migrations Flyway |
| Evidência | 188 testes passando — valida lógica, não aplicação Flyway em H2 |

**Conclusão:** endpoints FT-AUTH são exercitáveis com schema equivalente ao `V2` em banco limpo; o suite de testes **não valida** a execução Flyway das migrations atuais.

---

## Matriz de consistência

| Dimensão | Migration ↔ JPA | Migration ↔ Specification | Migration ↔ Modelo físico |
|----------|-----------------|---------------------------|---------------------------|
| `AUTH_SESSAO` | ✅ Consistente | ✅ Consistente | ❌ Modelo físico ausente |
| `COLABORADOR` (mínimo) | ✅ Consistente | ✅ Consistente (auto-create) | ❌ Divergente do baseline |
| Seed dev | — | N/A | ❌ Inexistente (Flyway) |

---

## Veredito

| Pergunta | Resposta |
|----------|----------|
| 1. Tabelas implementadas? | **Sim** (escopo spec) |
| 2. JPA sem migration? | **Não** |
| 3. Migration sem entidade? | **Não** |
| 4. Modelo físico sincronizado? | **Não** |
| 5. Seed mínimo dev? | **Não** (Flyway); auto-create no login |
| 6. Endpoints só com migrations? | **Sim** em banco limpo; **não** se baseline DDL já existir |

**Classificação:** **Parcialmente consistente** — camada implementada e funcional para FT-AUTH Sprint 1, com gaps de sincronização documental (`database/`) e ausência de seed Flyway para desenvolvimento.

---

## Recomendações (informativas — fora do escopo desta auditoria)

1. Registrar `AUTH_SESSAO` em `database/model/` e DDL baseline (ou migration oficial em `database/migrations/`).
2. Documentar estratégia de coexistência: `COLABORADOR` mínimo (V2) vs modelo corporativo.
3. Avaliar seed Flyway opcional (`R__dev_seed.sql`) ou documentar fluxo “login como seed” para dev local.
4. Alinhar modelo conceitual em `authentication-architecture.md` com coluna `ID_SESSAO`.
