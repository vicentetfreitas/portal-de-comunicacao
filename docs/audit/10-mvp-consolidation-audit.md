# 10-mvp-consolidation-audit

**Projeto:** Portal de Comunicação Digital (PCD)  
**Data da auditoria:** 2026-06-22  
**Tipo:** Auditoria de consolidação MVP — alinhamento tecnológico, arquitetural e de escopo  
**Auditor:** Audit Agent

> **Nota (DEC-DB-019):** Relatório histórico de 2026-06-22. Achados sobre Flyway refletem o estado na data da auditoria. A arquitetura atual administra o schema exclusivamente via baseline DDL (DBA).

**Fonte de verdade tecnológica:**

```text
docs/technology/01-technology-stack.md
docs/technology/02-development-standards.md
docs/technology/03-environment-strategy.md
docs/technology/04-decision-log.md
```

**Contexto:** A Fase 2 (Backend Construction + Infraestrutura) e a fundação EPIC-001 foram executadas antes da formalização da stack em `docs/technology/`. Esta auditoria verifica aderência dos artefatos existentes às decisões aprovadas em 2026-06-22 (DEC-001 a DEC-012).

---

## Resumo Executivo

A auditoria identificou **divergências críticas** entre a stack tecnológica oficial (Oracle, Java 25, Spring Boot 4.x, Vue 3 + Quasar) e os artefatos executáveis produzidos na fundação EPIC-001, que utilizam **PostgreSQL**, **Java 21** e **Spring Boot 3.4.2**.

A camada `docs/technology/` está internamente consistente e aprovada. Porém, **código**, **docker-compose**, **variáveis de ambiente** e múltiplos documentos em `docs/implementation/`, `docs/construction/` e `docs/solution-design/` permanecem alinhados à stack pré-decisão (PostgreSQL, Next.js/React no frontend Construction).

O escopo funcional MVP (bounded contexts, exclusão de Campanhas/Mensagens) apresenta **aderência parcial**: exemplos ativos em Construction backend/frontend foram corrigidos na Fase 1–2, mas persistem referências obsoletas documentadas e inconsistências de nomenclatura de API (`documents` vs `documentos`).

**Total de não conformidades:** 18  
**Alta:** 6 | **Média:** 9 | **Baixa:** 3

---

## Escopo Auditado

### Documentação

| Caminho | Arquivos analisados |
| ------- | ------------------- |
| `docs/architecture/` | 14 artefatos |
| `docs/implementation/` | 14 artefatos |
| `docs/construction/` | backend (6), frontend (6), infrastructure (4), delivery (3) |
| `docs/governance/` | status, roadmap, histórico Fase 1–2, reconciliação |
| `docs/technology/` | 4 artefatos (fonte normativa) |

### Código e infraestrutura

| Artefato | Estado |
| -------- | ------ |
| `backend/` | Fundação EPIC-001 — 5 arquivos Java, 1 migration, pom.xml, Dockerfile |
| `frontend/` | Diretório vazio |
| `docker-compose.yml` | PostgreSQL + backend |
| `README.md` | Documentação de onboarding |
| `.env.example` | Variáveis de ambiente local |

### Exclusões

* `docs/discovery/` — AS-IS legado; não normativo para TO-BE
* Código legado WordPress/Quasar AS-IS — fora do repositório TO-BE

---

## Matriz de Conformidade

| Área | Esperado | Encontrado | Status |
| ---- | -------- | ---------- | ------ |
| Linguagem Backend | Java 25 (DEC-002) | Java 21 em `pom.xml`, `Dockerfile`, `README.md` | **Não conforme** |
| Framework Backend | Spring Boot 4.x (DEC-003) | Spring Boot 3.4.2 em `pom.xml` | **Não conforme** |
| Banco de Dados | Oracle 23ai / Oracle XE local (DEC-001) | PostgreSQL 16 em `docker-compose.yml`, `application.yml`, `pom.xml` | **Não conforme** |
| Driver JDBC | Oracle JDBC | `org.postgresql:postgresql` + `flyway-database-postgresql` | **Não conforme** |
| Migrações | Flyway + SQL Oracle-compatível (DEC-006) | Flyway presente; `V1__initial_schema.sql` com `CREATE SCHEMA` (dialeto PostgreSQL) | **Parcial** |
| Frontend Framework | Vue 3 + Quasar + TypeScript (DEC-004) | Diretório `frontend/` vazio; Construction documenta Next.js/React | **Não conforme** |
| Estado Frontend | Pinia | Construction documenta TanStack Query como server state | **Não conforme** |
| HTTP Client | Axios | Construction documenta padrão Next.js/fetch implícito | **Não conforme** |
| Arquitetura | DDD + Clean + Hexagonal (DEC-005) | Pacotes `organization`, `accesscontrol`, `documentmanagement`, `internalcommunication` **ausentes** no código | **Parcial** |
| Segurança | Spring Security + JWT (DEC-008) | `spring-boot-starter-security` ausente em `pom.xml` | **Não conforme** |
| Observabilidade | Micrometer + Prometheus + Grafana (DEC-012) | Actuator presente; Micrometer/Prometheus/Grafana não configurados | **Parcial** |
| CI/CD | GitHub Actions (DEC-009) | Diretório `.github/` vazio | **Não conforme** |
| Docker / Compose | Oracle XE + Backend (DEC-009) | `postgres:16-alpine` + backend | **Não conforme** |
| Perfis Spring | `local`, `dev`, `hml`, `prd` + arquivos YAML | Apenas `application.yml` | **Não conforme** |
| Pacote Java | `br.com.unimed.pcd` (`02-development-standards.md`) | `br.com.unimed.pdc` no código | **Não conforme** |
| APIs REST — documentos | `/api/v1/documentos` (`01-technology-stack.md`) | `/api/v1/documents` em Construction | **Não conforme** |
| APIs REST — notificações | `/api/v1/notificacoes` | `/api/v1/notifications` em Construction | **Não conforme** |
| APIs REST — comunicados | `/api/v1/comunicados` | `/api/v1/comunicados` em Construction backend/frontend | **Conforme** |
| Bounded contexts (nomenclatura) | organization, accesscontrol, documentmanagement, internalcommunication | Documentados em Implementation/Technology; ausentes no código | **Parcial** |
| MVP — Campanhas/Mensagens | Removidos do escopo | Ativos removidos; referências em seções **Obsoleto** e `delivery/01-mvp.md` (lista exclusão) | **Parcial** |
| Monorepo | backend + frontend + docs (DEC-010) | Estrutura de diretórios presente | **Conforme** |
| Health checks | `/actuator/health` + endpoint operacional | `HealthController` + Actuator configurado | **Conforme** |
| Tecnologias rejeitadas | PostgreSQL, Redis, Kafka, Next.js, Angular (DEC-011) | PostgreSQL **implementado**; Redis em `implementation/11-bootstrap-roadmap.md`; Next.js em Construction frontend | **Não conforme** |
| Technology docs | Stack formalizada | 4 documentos completos e aprovados | **Conforme** |

---

## Não Conformidades

### AUD-001 — Banco de dados implementado é PostgreSQL, não Oracle

**Descrição:** A fundação EPIC-001 utiliza PostgreSQL como SGBD em todos os artefatos executáveis, contrariando DEC-001 e `01-technology-stack.md`, que definem Oracle Database 23ai (Oracle XE local).

**Evidência:**

* `backend/pom.xml` — dependências `org.postgresql:postgresql` e `flyway-database-postgresql`
* `backend/src/main/resources/application.yml` — `jdbc:postgresql://...`
* `docker-compose.yml` — `image: postgres:16-alpine`, volume `postgres-data`
* `.env.example` — `DB_PORT=5432`
* `04-decision-log.md` DEC-001 — registra explicitamente que a fundação inicial usou PostgreSQL

**Impacto:** Impossibilita deploy em ambiente corporativo Oracle sem retrabalho completo de persistência, migrations, compose e configuração.

**Severidade:** **Alta**

**Correção recomendada:** Substituir PostgreSQL por Oracle XE no `docker-compose.yml`; trocar driver JDBC e URL em `application.yml`; revisar migrations para dialeto Oracle; atualizar `.env.example` (porta 1521, variáveis Oracle).

---

### AUD-002 — Versão Java implementada é 21, não 25

**Descrição:** O código e containers utilizam Java 21, enquanto DEC-002 e `01-technology-stack.md` exigem Java 25.

**Evidência:**

* `backend/pom.xml` — `<java.version>21</java.version>`
* `backend/Dockerfile` — `maven:3.9-eclipse-temurin-21`, `eclipse-temurin:21-jre-alpine`
* `README.md` — "Java 21+"
* `backend/README.md` — "Java 21"

**Impacto:** Divergência de runtime entre desenvolvimento, CI e requisito oficial; risco de incompatibilidade com Spring Boot 4.x.

**Severidade:** **Alta**

**Correção recomendada:** Atualizar `pom.xml`, `Dockerfile` e documentação para Java 25; validar disponibilidade de imagens base.

---

### AUD-003 — Spring Boot implementado é 3.4.2, não 4.x

**Descrição:** O parent Maven referencia Spring Boot 3.4.2. A stack oficial exige Spring Boot 4.x (DEC-003).

**Evidência:**

* `backend/pom.xml` — `<version>3.4.2</version>` em `spring-boot-starter-parent`
* `backend/README.md` — "Spring Boot 3.4"
* `docs/governance/history/phase2-backend-construction-report.md` — registra uso de 3.4

**Impacto:** Incompatibilidade com decisão tecnológica; possível retrabalho de dependências e APIs ao migrar para 4.x.

**Severidade:** **Alta**

**Correção recomendada:** Atualizar parent para Spring Boot 4.x conforme documentação oficial; revalidar dependências (springdoc, flyway, security).

---

### AUD-004 — Camada Implementation documenta PostgreSQL como stack oficial

**Descrição:** Documentos da camada Implementation contradizem `docs/technology/` ao declarar PostgreSQL como tecnologia de persistência.

**Evidência:**

* `docs/implementation/04-backend-architecture.md` — seção Persistência: `PostgreSQL`
* `docs/implementation/06-database-standards.md` — escopo: `PostgreSQL`
* `docs/implementation/03-development-standards.md` — banco: `PostgreSQL`
* `docs/implementation/11-bootstrap-roadmap.md` — Fase 1 Infrastructure: PostgreSQL, Redis

**Impacto:** Documentação Implementation continua induzindo implementação com tecnologia explicitamente rejeitada (DEC-011).

**Severidade:** **Alta**

**Correção recomendada:** Atualizar Implementation para Oracle; remover Redis do bootstrap MVP ou marcar como pós-MVP; referenciar `docs/technology/` como fonte normativa.

---

### AUD-005 — Construction Frontend documenta Next.js/React, não Vue 3 + Quasar

**Descrição:** Os guias de construção frontend descrevem stack Next.js/React/pnpm/TanStack Query, divergindo de DEC-004.

**Evidência:**

* `docs/construction/frontend/01-project-bootstrap.md` — "Next.js", "React", "pnpm", estrutura App Router
* `docs/construction/frontend/03-routing.md` — "Next.js App Router"
* `docs/construction/frontend/04-state-management.md` — "TanStack Query"
* `docs/construction/frontend/05-api-consumption.md` — integração TanStack Query
* `docs/construction/infrastructure/02-docker.md` — diagrama "Frontend Next.js"
* `docs/construction/infrastructure/01-local-environment.md` — pnpm, Node.js LTS

**Impacto:** Guias de bootstrap frontend induzem implementação com framework rejeitado (Next.js, DEC-011); retrabalho total do frontend Construction.

**Severidade:** **Alta**

**Correção recomendada:** Reescrever Construction frontend para Vue 3 + Quasar + Pinia + Axios + Vitest conforme `docs/technology/`.

---

### AUD-006 — Spring Security ausente no backend

**Descrição:** A stack oficial exige `spring-boot-starter-security`, JWT e RBAC. O `pom.xml` não inclui Spring Security.

**Evidência:**

* `docs/technology/01-technology-stack.md` — componentes: `spring-boot-starter-security`; autenticação JWT
* `backend/pom.xml` — ausência de `spring-boot-starter-security`
* Busca em `backend/src` — zero referências a Security

**Impacto:** Fundação não atende requisito de segurança obrigatório; endpoints expostos sem autenticação.

**Severidade:** **Média** *(aceitável apenas como lacuna temporária de fundação, mas documentado como obrigatório na stack)*

**Correção recomendada:** Adicionar dependência e configuração base de Spring Security; alinhar com DEC-008.

---

### AUD-007 — GitHub Actions não implementado

**Descrição:** CI/CD oficial é GitHub Actions. O diretório `.github/` existe mas está vazio.

**Evidência:**

* `docs/technology/01-technology-stack.md` — CI/CD: GitHub Actions
* `docs/technology/03-environment-strategy.md` — Pipeline: GitHub Actions
* `.github/` — 0 arquivos de workflow

**Impacto:** Ausência de pipeline automatizado (build, testes, análise estática) exigido pela stack e `02-development-standards.md`.

**Severidade:** **Média**

**Correção recomendada:** Criar workflows mínimos conforme `docs/construction/infrastructure/03-ci-cd.md`, adaptados para Oracle/Java 25/Spring Boot 4.

---

### AUD-008 — Bounded contexts não materializados no código

**Descrição:** DEC-005 e `02-development-standards.md` exigem organização por domínio. O código contém apenas `bootstrap/health`, sem pacotes dos quatro bounded contexts.

**Evidência:**

* `docs/technology/02-development-standards.md` — pacotes obrigatórios: `organization`, `accesscontrol`, `documentmanagement`, `internalcommunication`
* `backend/src/main/java/br/com/unimed/pdc/` — apenas `PortalDeComunicacaoApplication.java` e `bootstrap/health/HealthController.java`
* Busca por `organization|accesscontrol|documentmanagement|internalcommunication` em `backend/src` — 0 ocorrências

**Impacto:** Estrutura arquitetural DDD não iniciada; risco de organização por camadas técnicas no futuro.

**Severidade:** **Média** *(esperado para fundação mínima EPIC-001, porém diverge do padrão documentado)*

**Correção recomendada:** Criar estrutura de pacotes vazia (package-info) por bounded context conforme `02-development-standards.md`.

---

### AUD-009 — Perfis Spring e arquivos de ambiente incompletos

**Descrição:** `03-environment-strategy.md` exige perfis `local`, `dev`, `hml`, `prd` com arquivos YAML dedicados. Apenas `application.yml` existe.

**Evidência:**

* `docs/technology/03-environment-strategy.md` — arquivos: `application-local.yml`, `application-dev.yml`, `application-hml.yml`, `application-prd.yml`
* `docs/construction/backend/01-project-bootstrap.md` — lista os quatro perfis
* `backend/src/main/resources/` — somente `application.yml`

**Impacto:** Configuração por ambiente não reproduzível conforme estratégia oficial.

**Severidade:** **Média**

**Correção recomendada:** Criar arquivos de perfil; alinhar variáveis (`DB_USERNAME` vs `DB_USER`).

---

### AUD-010 — Nomenclatura de pacote Java divergente (pdc vs pcd)

**Descrição:** O código usa `br.com.unimed.pdc`; a documentação technology usa `br.com.unimed.pcd`.

**Evidência:**

* `docs/technology/02-development-standards.md` — `br/com/unimed/pcd`
* `backend/src/main/java/br/com/unimed/pdc/` — pacote implementado
* `docs/implementation/04-backend-architecture.md` — `br.com.unimed.pdc`

**Impacto:** Inconsistência entre fontes normativas; risco de confusão em onboarding.

**Severidade:** **Média**

**Correção recomendada:** Formalizar pacote canônico em `docs/technology/` e alinhar código + Implementation.

---

### AUD-011 — Nomenclatura de endpoints REST divergente (inglês vs português)

**Descrição:** `01-technology-stack.md` define URIs em português (`/documentos`, `/notificacoes`). Construction usa inglês (`/documents`, `/notifications`).

**Evidência:**

* `docs/technology/01-technology-stack.md` — `/api/v1/comunicados`, `/api/v1/documentos`, `/api/v1/notificacoes`
* `docs/construction/backend/04-api-implementation.md` — `/api/v1/notifications`, `/api/v1/documents`
* `docs/construction/frontend/05-api-consumption.md` — mesma divergência

**Impacto:** Contratos API inconsistentes entre camadas; retrabalho de integração frontend/backend.

**Severidade:** **Média**

**Correção recomendada:** Unificar nomenclatura conforme decisão em `01-technology-stack.md` ou registrar exceção formal no decision log.

---

### AUD-012 — Redis documentado em Implementation Bootstrap

**Descrição:** DEC-011 rejeita Redis no MVP. `implementation/11-bootstrap-roadmap.md` inclui Redis como componente da Fase 1 Infrastructure.

**Evidência:**

* `docs/technology/04-decision-log.md` DEC-011 — Redis rejeitado no MVP
* `docs/implementation/11-bootstrap-roadmap.md` — PostgreSQL + Redis + Nginx na Fase 1
* `docs/solution-design/11-platform-decomposition.md` — Redis no diagrama de plataforma

**Impacto:** Documentação induz adoção de tecnologia rejeitada.

**Severidade:** **Média**

**Correção recomendada:** Remover Redis do escopo MVP em Implementation/Solution Design ou elevar decisão arquitetural formal.

---

### AUD-013 — Solution Design e Architecture documentam PostgreSQL

**Descrição:** Artefatos de camadas superiores (pré-technology) referenciam PostgreSQL, criando conflito com DEC-001.

**Evidência:**

* `docs/solution-design/11-platform-decomposition.md` — múltiplas referências a PostgreSQL
* `docs/architecture/11-target-repository-structure.md` — `postgres/`, diagrama PostgreSQL

**Impacto:** Hierarquia documental conflitante; implementadores podem consultar fonte desatualizada.

**Severidade:** **Média**

**Correção recomendada:** Atualizar Solution Design e Architecture para Oracle; registrar superseding de decisões anteriores.

---

### AUD-014 — Migration inicial com sintaxe não validada para Oracle

**Descrição:** `V1__initial_schema.sql` utiliza `CREATE SCHEMA IF NOT EXISTS`, sintaxe compatível com PostgreSQL. Oracle utiliza abordagem diferente para schemas/usuários.

**Evidência:**

* `backend/src/main/resources/db/migration/V1__initial_schema.sql` — `CREATE SCHEMA IF NOT EXISTS portal`
* `docs/technology/01-technology-stack.md` — Flyway com Oracle
* `docs/implementation/06-database-standards.md` — proíbe SERIAL/BIGSERIAL/JSONB (específicos PostgreSQL) mas stack Implementation ainda é PostgreSQL

**Impacto:** Migration não executará em Oracle sem reescrita.

**Severidade:** **Média**

**Correção recomendada:** Reescrever migrations para dialeto Oracle (tablespaces, usuários, tipos Oracle).

---

### AUD-015 — Observabilidade incompleta na fundação

**Descrição:** Stack exige Micrometer + Prometheus + Grafana. Apenas Actuator básico está configurado.

**Evidência:**

* `docs/technology/01-technology-stack.md` — Micrometer, Prometheus, Grafana
* `backend/pom.xml` — sem `micrometer-registry-prometheus`
* `docker-compose.yml` — sem serviços Prometheus/Grafana
* `docs/construction/backend/01-project-bootstrap.md` — lista micrometer como dependência obrigatória, mas ausente no pom

**Impacto:** Monitoramento não conforme DEC-012; métricas de negócio MVP não exportáveis.

**Severidade:** **Média**

**Correção recomendada:** Adicionar Micrometer/Prometheus; incluir containers de observabilidade no compose local.

---

### AUD-016 — Construction Backend declara PostgreSQL após Fase 2

**Descrição:** Documentos Construction backend foram atualizados na Fase 2 para PostgreSQL, divergindo da stack technology formalizada posteriormente.

**Evidência:**

* `docs/construction/backend/01-project-bootstrap.md` — Banco: PostgreSQL; dependência `postgresql`
* `docs/construction/backend/03-persistence.md` — PostgreSQL
* `docs/construction/infrastructure/01-local-environment.md` — `postgres:16-alpine`
* `docs/construction/infrastructure/02-docker.md` — diagrama PostgreSQL

**Impacto:** Construction — camada de execução — contradiz Technology.

**Severidade:** **Alta** *(reclassificação do impacto documental cumulativo com AUD-001/004)*

**Correção recomendada:** Reverter Construction backend/infrastructure para Oracle XE conforme `docs/technology/03-environment-strategy.md`.

---

### AUD-017 — Exemplo de endpoint `communications` em Technology Development Standards

**Descrição:** `02-development-standards.md` lista `/api/v1/communications` como exemplo de API, enquanto o MVP oficial usa `comunicados` e rejeita `communications` como módulo.

**Evidência:**

* `docs/technology/02-development-standards.md` linha ~260 — `/api/v1/communications`
* `docs/construction/backend/04-api-implementation.md` — `communications` apenas em seção Obsoleto
* `docs/audit/09-campaign-traceability-audit.md` — `communications` como nomenclatura descontinuada

**Impacto:** Fonte normativa technology contém exemplo desalinhado ao léxico MVP.

**Severidade:** **Baixa**

**Correção recomendada:** Substituir exemplo por `/api/v1/comunicados` em `02-development-standards.md`.

---

### AUD-018 — Referências obsoletas a Campanhas/Mensagens em Construction

**Descrição:** Após Fases 1–2, referências a `campaign`, `messages`, `communications` permanecem em seções **Obsoleto** — conforme esperado — mas ainda presentes em documentos Construction.

**Evidência:**

* `docs/construction/backend/04-api-implementation.md` — seção Obsoleto: `/api/v1/campaigns`, `/messages`
* `docs/construction/frontend/01-project-bootstrap.md` — seção Obsoleto: `CampaignForm`, `features/campaigns/`
* `docs/construction/infrastructure/04-observability.md` — seção Obsoleto: `campaigns_created`, `messages_sent`
* `docs/construction/delivery/01-mvp.md` — "Gestão de Campanhas" em **Não Incluído** (conforme)

**Impacto:** Baixo — seções marcadas como obsoletas; risco residual de implementação inadvertida.

**Severidade:** **Baixa**

**Correção recomendada:** Manter seções Obsoleto ou migrar para apêndice histórico; reforçar banner de proibição.

---

## Itens Conformes

| # | Item | Evidência |
| - | ---- | --------- |
| 1 | Documentação `docs/technology/` completa e aprovada | 4 artefatos com status Aprovado; DEC-001 a DEC-012 registrados |
| 2 | Monorepo conforme DEC-010 | `backend/`, `frontend/`, `cms/`, `docs/`, `docker/`, `scripts/` |
| 3 | Flyway configurado no backend | `flyway-core` no pom; `db/migration/V1__*.sql`; `spring.flyway.enabled=true` |
| 4 | Health checks operacionais | `HealthController` (`GET /api/v1/health`); Actuator com probes |
| 5 | Docker multi-stage com usuário não-root | `backend/Dockerfile` — `adduser appuser`, multi-stage build |
| 6 | Léxico MVP `comunicados` nos exemplos ativos de Construction backend | `04-api-implementation.md` — endpoints e controllers ativos |
| 7 | Léxico MVP no Construction frontend (Fase 1) | Rotas `/comunicados`, `/notifications`, `/documents`; serviços `comunicado.*` |
| 8 | Delivery MVP alinhado ao escopo Etapas 1–5 | `construction/delivery/01-mvp.md` referencia audit 10; Campanhas em "Não Incluído" |
| 9 | `.env.example` versionado; `.env` no gitignore | Conforme `03-environment-strategy.md` |
| 10 | OpenAPI/Springdoc presente no pom | `springdoc-openapi-starter-webmvc-ui` |
| 11 | Maven como build tool | `backend/pom.xml` |
| 12 | Bounded contexts nomeados corretamente na documentação DDD | Technology, Implementation, Construction backend — nomenclatura oficial |
| 13 | DEC-001 documenta explicitamente o desvio PostgreSQL→Oracle | Rastreabilidade da divergência registrada |

---

## Riscos

| ID | Risco | Probabilidade | Impacto | Mitigação |
| -- | ----- | ------------- | ------- | --------- |
| R-001 | Desenvolvimento continuar sobre PostgreSQL ignorando DEC-001 | Alta | Crítico | Bloquear novas migrations PostgreSQL; executar correção AUD-001 antes de Etapa 2 |
| R-002 | Bootstrap frontend com Next.js por guias Construction desatualizados | Alta | Alto | Priorizar correção AUD-005 antes de iniciar código frontend |
| R-003 | Conflito hierárquico Implementation vs Technology | Alta | Alto | Estabelecer `docs/technology/` como precedência sobre Implementation para stack |
| R-004 | Migrations PostgreSQL acumuladas tornem migração Oracle custosa | Média | Alto | Corrigir AUD-014 na fundação (apenas V1 existente) |
| R-005 | Ausência de CI permitir merge de código não conforme | Média | Médio | Implementar AUD-007 como gate mínimo |
| R-006 | Nomenclatura API inglês/português gerar contratos duplicados | Média | Médio | Resolver AUD-011 antes de OpenAPI definitivo |
| R-007 | Referências obsoletas Campanhas induzirem escopo extra | Baixa | Médio | Manter auditoria de rastreabilidade; reforçar AUD-018 |

---

## Plano de Correção

### Fase A — Bloqueantes (antes de qualquer nova implementação)

| Prioridade | AUD | Ação | Responsável sugerido |
| ---------- | --- | ---- | -------------------- |
| 1 | AUD-001, AUD-016 | Migrar código e compose para Oracle XE; remover PostgreSQL | Backend + Infra |
| 2 | AUD-002, AUD-003 | Atualizar Java 25 + Spring Boot 4.x | Backend |
| 3 | AUD-004, AUD-013 | Atualizar Implementation e Solution Design para Oracle | Arquitetura |
| 4 | AUD-014 | Reescrever V1 migration para Oracle | Backend |

### Fase B — Documentação Construction

| Prioridade | AUD | Ação |
| ---------- | --- | ---- |
| 5 | AUD-005 | Reescrever Construction frontend para Vue 3 + Quasar |
| 6 | AUD-016 | Alinhar Construction backend/infrastructure a Oracle |
| 7 | AUD-011 | Unificar nomenclatura de endpoints REST |
| 8 | AUD-010 | Formalizar pacote Java canônico |

### Fase C — Completude da fundação

| Prioridade | AUD | Ação |
| ---------- | --- | ---- |
| 9 | AUD-006 | Adicionar Spring Security base |
| 10 | AUD-007 | Implementar GitHub Actions mínimo |
| 11 | AUD-008 | Criar estrutura de pacotes por bounded context |
| 12 | AUD-009 | Criar perfis Spring por ambiente |
| 13 | AUD-015 | Configurar Micrometer/Prometheus |

### Fase D — Melhorias

| Prioridade | AUD | Ação |
| ---------- | --- | ---- |
| 14 | AUD-012 | Remover Redis do escopo MVP em Implementation |
| 15 | AUD-017 | Corrigir exemplo `communications` em technology |
| 16 | AUD-018 | Consolidar seções Obsoleto ou mover para histórico |

**Estimativa de dependências:** Fase A é pré-requisito para Fases B–C. Fase B pode iniciar em paralelo com Fase A para documentação, mas não código frontend.

---

## Parecer Final

A fundação técnica executável (EPIC-001) e a documentação das camadas Implementation, Construction e Solution Design **não estão alinhadas** com a stack tecnológica oficial formalizada em `docs/technology/`.

As divergências de **banco de dados** (PostgreSQL implementado vs Oracle aprovado — DEC-001, DEC-011), **versão Java** (21 vs 25) e **Spring Boot** (3.4.2 vs 4.x) constituem não conformidades de severidade **Alta** que impedem a evolução segura do projeto sobre a base atual sem retrabalho estrutural.

A documentação `docs/technology/` está conforme. O escopo MVP funcional (exclusão de Campanhas/Mensagens, bounded contexts nomeados) apresenta aderência **parcial** nos exemplos ativos de Construction, com resíduos obsoletos de baixo risco.

```text
REJECTED
```

**Justificativa:** Artefatos executáveis utilizam tecnologia explicitamente rejeitada (PostgreSQL — DEC-011) e versões de runtime/framework não aprovadas. A fundação EPIC-001 requer correção integral da Fase A do Plano de Correção antes de prosseguir com implementação de bounded contexts ou bootstrap frontend.

---

## Histórico de Revisões

| Data | Alteração |
| ---- | --------- |
| 2026-06-22 | Auditoria inicial de consolidação MVP — escopo funcional (versão anterior) |
| 2026-06-22 | Reauditoria completa — alinhamento tecnológico pós-formalização `docs/technology/` |

---

## Referências Auditadas

| Documento | Papel na auditoria |
| --------- | ---------------- |
| `docs/technology/01-technology-stack.md` | Fonte normativa — stack |
| `docs/technology/02-development-standards.md` | Fonte normativa — padrões |
| `docs/technology/03-environment-strategy.md` | Fonte normativa — ambientes |
| `docs/technology/04-decision-log.md` | Fonte normativa — DEC-001 a DEC-012 |
| `docs/governance/history/phase2-backend-construction-report.md` | Contexto Fase 2 |
| `docs/solution-design/10-delivery-roadmap.md` | Escopo MVP Etapas 1–5 |
| `docs/audit/09-campaign-traceability-audit.md` | Rastreabilidade Campanhas |
