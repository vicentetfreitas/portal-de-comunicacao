# Construction Packages — Single Source of Truth (SSOT)

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Sprint | 1A — Platform Foundation |
| Papel | **SSOT oficial de execução** |
| Consumidor primário | Construction Orchestrator (`.cursor/orchestrator/construction-orchestrator.mdc`) |
| Status | Aprovado |
| Versão | 2.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Definir os Construction Packages da Sprint 1A como **única fonte oficial de verdade** para execução da Platform Foundation.

Este documento permite que o comando `Execute PKG-XX` seja suficiente para o Construction Orchestrator identificar objetivo, escopo, dependências, agentes, tarefas, critérios e próximos passos — **sem prompts adicionais**.

---

# Escopo

Oito pacotes incrementais compõem a Sprint 1A:

| Tipo | Quantidade | Packages |
|------|------------|----------|
| Implementação | 7 | PKG-01 a PKG-07 |
| Auditoria | 1 | PKG-08 |

Cada pacote agrupa tarefas de um módulo da Platform Foundation, respeitando dependências e permitindo validação parcial antes de avançar.

**Fora do escopo deste documento:** implementação de Features (FT-AUTH e Business Features), alteração de arquitetura, alteração de specifications.

---

# Como Executar

```text
Execute PKG-01
Execute PKG-02
...
Execute PKG-08
```

O Construction Orchestrator lê **exclusivamente** a seção do Package correspondente neste documento e executa o fluxo operacional definido.

**Documentos complementares (não substituem este SSOT):**

| Documento | Uso |
|-----------|-----|
| `04-construction-rules.md` | Regras R-01 a R-10 |
| `06-development-order.md` | Justificativas de ordem |
| `05-readiness-review.md` | Checklist RC-* (PKG-08) |
| `platform-foundation/<modulo>/tasks.md` | Detalhamento de tarefas PF-* |
| `platform-foundation/<modulo>/review.md` | Checklist de review do módulo |

---

# PKG-01 — Configuration Foundation

## Identificação

| Campo | Valor |
|-------|-------|
| código | PKG-01 |
| nome | Configuration Foundation |
| módulo | Configuration |
| prefixo | PF-CONF |
| versão | 1.0 |
| status | Não iniciado |
| prioridade | Alta |
| estimativa | 2 dias |

---

## Objetivo

Estender a configuração da Sprint 0 com properties tipadas e validadas para Security, Persistence, Integration e Zimbra, habilitando todos os módulos subsequentes da Platform Foundation.

---

## Escopo

### Inclui

- `SecurityProperties`, `PersistenceProperties`, `IntegrationProperties`, `ZimbraProperties`
- Beans `@EnableConfigurationProperties` por módulo
- Validação e binding por perfil (`local`, `dev`, `hml`)
- Testes de carregamento de properties

### Não inclui

- `ApplicationProperties` existente (Sprint 0 — não alterar)
- Jackson, Locale, Async (Sprint 0 — existentes)
- SecurityFilterChain, JPA, RestClient (módulos posteriores)
- Implementação Zimbra ou endpoints FT-AUTH

---

## Entradas

| Tipo | Referência |
|------|------------|
| Módulos existentes | Sprint 0: `configuration/jackson/`, `locale/`, `async/`, `properties/ApplicationProperties` |
| Documentos | `platform-foundation/configuration/README.md`, `04-construction-rules.md` |
| Baseline | `docs/governance/history/phase2-backend-construction-report.md` |
| Padrões | `docs/implementation/02-repository-structure.md`, `docs/construction/backend/01-project-bootstrap.md` |
| Componentes necessários | `shared/`, `GlobalExceptionHandler` (Sprint 0) |

---

## Dependências

| Tipo | Referência |
|------|------------|
| Packages obrigatórios | Nenhum (pré-requisito: **Sprint 0 Approved**) |
| Packages opcionais | — |
| Dependências externas | Engineering Baseline congelada; build Sprint 0 SUCCESS |
| Bloqueios | Nenhum decisão CD-S1A-* bloqueante para PKG-01 |

---

## Agentes Envolvidos

| Papel | Agente |
|-------|--------|
| Responsável (R) | `construction-engineer` |
| Apoiadores (C) | — |
| Reviewer (R) | `reviewer` |
| Auditor (R) | `auditor` |
| Platform Architect (C) | `platform-architect` — somente em bloqueio arquitetural |

**RACI:** Orquestrador **A** | construction-engineer **R** | reviewer **R** | auditor **R** | platform-architect **C**

---

## Tarefas

Referência exclusiva: `platform-foundation/configuration/tasks.md`

**Intervalo:** PF-CONF-001 até PF-CONF-005

---

## Artefatos Produzidos

### Código

```text
backend/.../configuration/properties/
├── SecurityProperties.java
├── SecurityPropertiesConfiguration.java
├── PersistenceProperties.java
├── PersistencePropertiesConfiguration.java
├── IntegrationProperties.java
├── IntegrationPropertiesConfiguration.java
├── ZimbraProperties.java
└── ZimbraPropertiesConfiguration.java
```

### Configuração

- Seções documentadas em `application.yaml` e perfis `local`, `dev`, `hml`

### Testes

- `SecurityPropertiesTest`, `PersistencePropertiesTest`, `IntegrationPropertiesTest`, `ZimbraPropertiesTest`
- Testes `@SpringBootTest` por perfil

### Documentação

- `platform-foundation/configuration/review.md` — checklist validado

---

## Critérios de Aceite

1. Todas as properties carregam via `@ConfigurationProperties` com prefixo documentado
2. Validação Bean Validation em campos obrigatórios
3. Properties funcionam nos perfis `local`, `dev`, `hml`
4. Segredos via variáveis de ambiente (`${JWT_SECRET:}`, `${ZIMBRA_AUTH_URL:}`)
5. Testes unitários de binding aprovados
6. `mvn clean verify` — SUCCESS
7. 106 testes Sprint 0 sem regressão

---

## Critérios de Aprovação

### Review (`reviewer`)

- Checklist `platform-foundation/configuration/review.md` — 100%
- Parecer: **Aprovado** ou **Aprovado com ressalvas** (sem itens críticos)
- Sprint 0 `ApplicationProperties` não alterada

### Audit (`auditor`)

- Classificação: **Conforme** ou **Parcialmente Conforme** sem bloqueadores
- Escopo PF-CONF respeitado — sem domínio, sem FT-AUTH
- Rastreabilidade PF-CONF-001 a 005 → código

---

## Definition of Done

Package **Approved** quando:

- [ ] PF-CONF-001 a PF-CONF-005 concluídas
- [ ] Review aprovado
- [ ] Audit aprovado
- [ ] `mvn clean verify` — SUCCESS
- [ ] `09-progress.md` atualizado — PKG-01 Approved

---

## Próximo Package

**PKG-02** — Persistence Foundation

---

## Fluxo Operacional

```text
DoR (Sprint 0 Approved)
        ↓
construction-engineer → PF-CONF-001..005
        ↓
Review (reviewer)
        ↓
[Correções se necessário]
        ↓
Audit (auditor)
        ↓
Approved
        ↓
PKG-02
```

---

## Atualizações Obrigatórias

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | Status PKG-01 Approved; tarefas 5/37 |
| `platform-foundation/configuration/review.md` | Checklist preenchido |
| `construction/07-open-decisions.md` | Registrar se decisão surgir |

---

# PKG-02 — Persistence Foundation

## Identificação

| Campo | Valor |
|-------|-------|
| código | PKG-02 |
| nome | Persistence Foundation |
| módulo | Persistence |
| prefixo | PF-PERS |
| versão | 1.0 |
| status | Não iniciado |
| prioridade | Alta |
| estimativa | 3 dias |

---

## Objetivo

Estabelecer a camada de persistência reutilizável sobre Oracle Database, fornecendo JPA, entidades base, repositório base e convenções de persistência para Features futuras. Schema administrado pelo DBA (DEC-DB-019).

---

## Escopo

### Inclui

- `JpaConfiguration` — EntityManager, transações, dialect Oracle
- `BaseEntity`, `AuditableEntity`
- `BaseRepository<T, ID>`
- `PersistenceException` + handler no `GlobalExceptionHandler`
- Testes de integração Oracle (perfil local)

### Não inclui

- Entidades de domínio (AUTH_SESSAO, Colaborador — FT-AUTH)
- Alteração de `V1__baseline.sql` (Sprint 0)
- Migrations de Features
- Bounded contexts

---

## Entradas

| Tipo | Referência |
|------|------------|
| Módulos existentes | PKG-01 Approved — `PersistenceProperties` |
| Sprint 0 | Baseline DDL (`database/ddl/`), Oracle JDBC, `shared/exception/` |
| Documentos | `platform-foundation/persistence/README.md` |
| Padrões | `docs/implementation/06-database-standards.md`, `docs/construction/backend/03-persistence.md` |
| Componentes | `GlobalExceptionHandler`, `ErrorResponse` |

---

## Dependências

| Tipo | Referência |
|------|------------|
| Packages obrigatórios | **PKG-01 Approved** |
| Packages opcionais | — |
| Dependências externas | Oracle UNMPORTCOM; schema administrado externamente |
| Bloqueios | PKG-01 não Approved |

---

## Agentes Envolvidos

| Papel | Agente |
|-------|--------|
| Responsável (R) | `construction-engineer` |
| Apoiadores (C) | — |
| Reviewer (R) | `reviewer` |
| Auditor (R) | `auditor` |
| Platform Architect (C) | `platform-architect` — dialect Oracle 11.2, bloqueios JPA |

---

## Tarefas

Referência exclusiva: `platform-foundation/persistence/tasks.md`

**Intervalo:** PF-PERS-001 até PF-PERS-006

---

## Artefatos Produzidos

### Código

```text
backend/.../infrastructure/persistence/
├── config/JpaConfiguration.java
├── entity/BaseEntity.java
├── entity/AuditableEntity.java
├── repository/BaseRepository.java
└── exception/PersistenceException.java
```

### Testes

- Testes unitários de entidades e repositório
- Teste integração — transação read-only Oracle

### Documentação

- `platform-foundation/persistence/review.md` — checklist validado

---

## Critérios de Aceite

1. Contexto JPA inicializa sem erro com Oracle
2. `BaseEntity` e `AuditableEntity` extensíveis
3. `BaseRepository` funcional com entidade de teste
4. `PersistenceException` mapeada no `GlobalExceptionHandler`
5. Dialect Oracle configurado explicitamente
6. `mvn clean verify` — SUCCESS
7. 106 testes Sprint 0 sem regressão

---

## Critérios de Aprovação

### Review

- Checklist `platform-foundation/persistence/review.md` — 100%
- Ausência de `@Entity` de domínio
- `V1__baseline.sql` intacto

### Audit

- Conformidade com `docs/implementation/06-database-standards.md`
- RC-06 a RC-10 preparados (validação completa em PKG-08)

---

## Definition of Done

Package **Approved** quando:

- [ ] PF-PERS-001 a PF-PERS-006 concluídas
- [ ] Review aprovado
- [ ] Audit aprovado
- [ ] `mvn clean verify` — SUCCESS
- [ ] `09-progress.md` — PKG-02 Approved

---

## Próximo Package

**PKG-03** — Security Foundation

---

## Fluxo Operacional

```text
DoR (PKG-01 Approved)
        ↓
construction-engineer → PF-PERS-001..006
        ↓
Review → Correções → Audit → Approved → PKG-03
```

---

## Atualizações Obrigatórias

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | PKG-02 Approved; tarefas 11/37 |
| `platform-foundation/persistence/review.md` | Checklist preenchido |

---

# PKG-03 — Security Foundation

## Identificação

| Campo | Valor |
|-------|-------|
| código | PKG-03 |
| nome | Security Foundation |
| módulo | Security |
| prefixo | PF-SEC |
| versão | 1.0 |
| status | Não iniciado |
| prioridade | Alta |
| estimativa | 3 dias |

---

## Objetivo

Estabelecer infraestrutura de segurança stateless reutilizável: SecurityFilterChain, CSRF, filtro JWT esqueleto e CORS — preparação para FT-AUTH sem implementar fluxos de autenticação.

---

## Escopo

### Inclui

- `SecurityConfiguration` — STATELESS, sem HTTP Session
- `CsrfConfiguration` — `CookieCsrfTokenRepository`
- `JwtAuthenticationFilter` — esqueleto (sem emissão de tokens)
- `RestAuthenticationEntryPoint` — 401 via `ErrorResponse`
- `CorsConfiguration`
- Whitelist: `/actuator/health` (health PKG-05 adicionado depois)
- Testes segurança base

### Não inclui

- Login/callback Zimbra, emissão JWT/Refresh Token
- Tabela AUTH_SESSAO
- Endpoints `/api/v1/auth/*`
- Autorização por permissões

---

## Entradas

| Tipo | Referência |
|------|------------|
| Módulos existentes | PKG-01 Approved — `SecurityProperties`; PKG-02 Approved — JPA para testes |
| Sprint 0 | `shared/constants/SecurityConstants.java` |
| Documentos | `platform-foundation/security/README.md`, `specs/architecture/authentication-architecture.md` |
| Decisões | DA-AUTH-005 (stateless) — `specs/features/authentication/decisions.md` |
| Riscos | CR-S1A-001 (escopo FT-AUTH), CR-S1A-002 (circular dependency) |

---

## Dependências

| Tipo | Referência |
|------|------------|
| Packages obrigatórios | **PKG-01 Approved**, **PKG-02 Approved** |
| Packages opcionais | — |
| Dependências externas | `spring-boot-starter-security` (Sprint 0 classpath) |
| Bloqueios | PKG-01 ou PKG-02 não Approved |

---

## Agentes Envolvidos

| Papel | Agente |
|-------|--------|
| Responsável (R) | `construction-engineer` |
| Apoiadores (C) | — |
| Reviewer (R) | `reviewer` |
| Auditor (R) | `auditor` |
| Platform Architect (C) | `platform-architect` — stateless, CSRF, extensibilidade FT-AUTH |

---

## Tarefas

Referência exclusiva: `platform-foundation/security/tasks.md`

**Intervalo:** PF-SEC-001 até PF-SEC-006

---

## Artefatos Produzidos

### Código

```text
backend/.../infrastructure/security/
├── config/SecurityConfiguration.java
├── config/CsrfConfiguration.java
├── config/CorsConfiguration.java
├── filter/JwtAuthenticationFilter.java
└── entrypoint/RestAuthenticationEntryPoint.java
```

### Testes

- Mínimo 4 cenários: público vs protegido, CSRF, CORS, 401

### Documentação

- `platform-foundation/security/review.md`

---

## Critérios de Aceite

1. `SessionCreationPolicy.STATELESS` configurado
2. CSRF ativo para cookies
3. `JwtAuthenticationFilter` na chain (esqueleto)
4. Rotas protegidas → 401 sem token
5. Endpoints públicos whitelist acessíveis
6. CORS funcional
7. `mvn clean verify` — SUCCESS

---

## Critérios de Aprovação

### Review

- Checklist `platform-foundation/security/review.md` — 100%
- Regra R-10: ausência de fluxos FT-AUTH
- RB-01, RB-02 do `05-readiness-review.md` preparados

### Audit

- Conformidade DA-AUTH-005
- Sem endpoints `/api/v1/auth/*`

---

## Definition of Done

Package **Approved** quando:

- [ ] PF-SEC-001 a PF-SEC-006 concluídas
- [ ] Review aprovado
- [ ] Audit aprovado
- [ ] `09-progress.md` — PKG-03 Approved

---

## Próximo Package

**PKG-04** — Integration Foundation (pode iniciar em paralelo com PKG-05 após PKG-03 Approved)

---

## Fluxo Operacional

```text
DoR (PKG-01 + PKG-02 Approved)
        ↓
construction-engineer → PF-SEC-001..006
        ↓
Review → Correções → Audit → Approved
        ↓
PKG-04  e/ou  PKG-05
```

---

## Atualizações Obrigatórias

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | PKG-03 Approved; tarefas 17/37 |
| `platform-foundation/security/review.md` | Checklist preenchido |

---

# PKG-04 — Integration Foundation

## Identificação

| Campo | Valor |
|-------|-------|
| código | PKG-04 |
| nome | Integration Foundation |
| módulo | Integration |
| prefixo | PF-INT |
| versão | 1.0 |
| status | Não iniciado |
| prioridade | Alta |
| estimativa | 2 dias |

---

## Objetivo

Estabelecer infraestrutura para consumo de sistemas externos: RestClient, resiliência, propagação de Correlation ID e contrato `IdentityProviderClient` para Zimbra (implementação em FT-AUTH).

---

## Escopo

### Inclui

- `RestClientConfiguration`
- `CorrelationIdInterceptor`
- `IntegrationException`, `IntegrationUnavailableException`
- Resiliência: timeout, retry, circuit breaker
- Interface `IdentityProviderClient` + DTOs + teste mock

### Não inclui

- `ZimbraIdentityProviderClient` (FT-AUTH)
- Regras de autenticação
- Webhooks, mensageria

---

## Entradas

| Tipo | Referência |
|------|------------|
| Módulos existentes | PKG-01 — `IntegrationProperties`, `ZimbraProperties`; PKG-03 — Security |
| Sprint 0 | `infrastructure/logging/` — CorrelationIdFilter, MDC |
| Documentos | `platform-foundation/integration/README.md`, `docs/construction/backend/05-integrations.md` |
| Decisões | **CD-S1A-004** — resiliência HTTP (resolver antes de PF-INT-004) |
| Riscos | CR-S1A-006 — ausência Zimbra para testes (WireMock) |

---

## Dependências

| Tipo | Referência |
|------|------------|
| Packages obrigatórios | **PKG-01 Approved**, **PKG-03 Approved** |
| Packages opcionais | PKG-02 (indireto via properties) |
| Paralelismo | Pode executar em paralelo com **PKG-05** após PKG-03 Approved |
| Bloqueios | PKG-03 não Approved; CD-S1A-004 aberta bloqueia PF-INT-004 |

---

## Agentes Envolvidos

| Papel | Agente |
|-------|--------|
| Responsável (R) | `construction-engineer` |
| Apoiadores (C) | — |
| Reviewer (R) | `reviewer` |
| Auditor (R) | `auditor` |
| Platform Architect (C) | `platform-architect` — CD-S1A-004, contrato Zimbra |

---

## Tarefas

Referência exclusiva: `platform-foundation/integration/tasks.md`

**Intervalo:** PF-INT-001 até PF-INT-005

---

## Artefatos Produzidos

### Código

```text
backend/.../infrastructure/integration/
├── config/RestClientConfiguration.java
├── client/IdentityProviderClient.java
├── exception/IntegrationException.java
├── exception/IntegrationUnavailableException.java
└── interceptor/CorrelationIdInterceptor.java
```

### Testes

- Mock server / WireMock — Correlation ID propagado
- Teste resiliência (PF-INT-004)

### Documentação

- `platform-foundation/integration/review.md`

---

## Critérios de Aceite

1. RestClient com timeout de `IntegrationProperties`
2. Correlation ID em chamadas outbound
3. `IntegrationUnavailableException` → 503 no handler
4. Resiliência conforme CD-S1A-004
5. `IdentityProviderClient` alinhado a `authentication-architecture.md`
6. `mvn clean verify` — SUCCESS

---

## Critérios de Aprovação

### Review

- Checklist `platform-foundation/integration/review.md` — 100%
- Interface sem implementação Zimbra concreta

### Audit

- RB-04 preparado
- Gateway pattern respeitado

---

## Definition of Done

Package **Approved** quando:

- [ ] PF-INT-001 a PF-INT-005 concluídas
- [ ] Review aprovado
- [ ] Audit aprovado
- [ ] `09-progress.md` — PKG-04 Approved

---

## Próximo Package

**PKG-06** — Observability Foundation (requer PKG-04 **e** PKG-05 Approved)

---

## Fluxo Operacional

```text
DoR (PKG-01 + PKG-03 Approved; CD-S1A-004 resolvida para PF-INT-004)
        ↓
construction-engineer → PF-INT-001..005
        ↓
Review → Correções → Audit → Approved
        ↓
Aguardar PKG-05 Approved → PKG-06
```

---

## Atualizações Obrigatórias

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | PKG-04 Approved; tarefas 22/37 |
| `platform-foundation/integration/review.md` | Checklist preenchido |
| `construction/07-open-decisions.md` | Encerrar CD-S1A-004 se resolvida |

---

# PKG-05 — Web Foundation

## Identificação

| Campo | Valor |
|-------|-------|
| código | PKG-05 |
| nome | Web Foundation |
| módulo | Web |
| prefixo | PF-WEB |
| versão | 1.0 |
| status | Não iniciado |
| prioridade | Alta |
| estimativa | 2 dias |

---

## Objetivo

Estabelecer camada REST padronizada com health endpoint, OpenAPI 3 e integração com Security e exception handling da Sprint 0.

---

## Escopo

### Inclui

- Estrutura `interfaces/rest/` (controller, response, config)
- `HealthController` — `GET /api/v1/health`
- `HealthResponse` com `ApiResponse<T>`
- `OpenApiConfiguration` / SpringDoc
- Whitelist `/api/v1/health` no SecurityFilterChain
- Testes `@WebMvcTest` e integração

### Não inclui

- Controllers de negócio ou FT-AUTH
- MapStruct (CD-S1A-003 — se adiado, não entra neste PKG)
- Endpoints além de health na Sprint 1A

---

## Entradas

| Tipo | Referência |
|------|------------|
| Módulos existentes | PKG-01 Approved; PKG-03 Approved — SecurityFilterChain |
| Sprint 0 | `shared/dto/ApiResponse`, `GlobalExceptionHandler` |
| Documentos | `platform-foundation/web/README.md`, `docs/implementation/07-api-standards.md` |
| Decisões | **CD-S1A-002** — SpringDoc SB 4 (antes de PF-WEB-004); **CD-S1A-003** — MapStruct (informativo) |
| Riscos | CR-S1A-002 — dependência circular Security/Web |

---

## Dependências

| Tipo | Referência |
|------|------------|
| Packages obrigatórios | **PKG-01 Approved**, **PKG-03 Approved** |
| Packages opcionais | — |
| Paralelismo | Pode executar em paralelo com **PKG-04** após PKG-03 Approved |
| Bloqueios | PKG-03 não Approved; CD-S1A-002 aberta bloqueia PF-WEB-004 |

---

## Agentes Envolvidos

| Papel | Agente |
|-------|--------|
| Responsável (R) | `construction-engineer` |
| Apoiadores (C) | — |
| Reviewer (R) | `reviewer` |
| Auditor (R) | `auditor` |
| Platform Architect (C) | `platform-architect` — CD-S1A-002 |

---

## Tarefas

Referência exclusiva: `platform-foundation/web/tasks.md`

**Intervalo:** PF-WEB-001 até PF-WEB-005

---

## Artefatos Produzidos

### Código

```text
backend/.../interfaces/rest/
├── controller/HealthController.java
├── response/HealthResponse.java
└── config/OpenApiConfiguration.java
```

### Testes

- `@WebMvcTest(HealthController)` — mínimo 3 cenários
- Teste integração health endpoint

### Documentação

- `platform-foundation/web/review.md`
- OpenAPI documentando `GET /api/v1/health`

---

## Critérios de Aceite

1. `GET /api/v1/health` → 200 com `ApiResponse`
2. Health na whitelist de segurança
3. Swagger UI acessível (CD-S1A-002)
4. Erros via `GlobalExceptionHandler`
5. Prefixo `/api/v1` respeitado
6. `mvn clean verify` — SUCCESS

---

## Critérios de Aprovação

### Review

- Checklist `platform-foundation/web/review.md` — 100%
- Apenas health endpoint na S1A

### Audit

- RB-05 preparado
- RC-22 a RC-26 preparados

---

## Definition of Done

Package **Approved** quando:

- [ ] PF-WEB-001 a PF-WEB-005 concluídas
- [ ] Review aprovado
- [ ] Audit aprovado
- [ ] `09-progress.md` — PKG-05 Approved

---

## Próximo Package

**PKG-06** — Observability Foundation (requer PKG-04 **e** PKG-05 Approved)

---

## Fluxo Operacional

```text
DoR (PKG-01 + PKG-03 Approved; CD-S1A-002 resolvida para PF-WEB-004)
        ↓
construction-engineer → PF-WEB-001..005
        ↓
Review → Correções → Audit → Approved
        ↓
Aguardar PKG-04 Approved → PKG-06
```

---

## Atualizações Obrigatórias

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | PKG-05 Approved; tarefas 27/37 |
| `platform-foundation/web/review.md` | Checklist preenchido |
| `construction/07-open-decisions.md` | Encerrar CD-S1A-002 se resolvida |

---

# PKG-06 — Observability Foundation

## Identificação

| Campo | Valor |
|-------|-------|
| código | PKG-06 |
| nome | Observability Foundation |
| módulo | Observability |
| prefixo | PF-OBS |
| versão | 1.0 |
| status | Não iniciado |
| prioridade | Alta |
| estimativa | 2 dias |

---

## Objetivo

Estender observabilidade da Sprint 0 com métricas Micrometer, request logging estruturado, DatabaseHealthIndicator e configuração Actuator.

---

## Escopo

### Inclui

- `MetricsConfiguration`
- `RequestLoggingFilter`
- `DatabaseHealthIndicator`
- Configuração Actuator (health, metrics, info)
- Integração Correlation ID → logs de requisição

### Não inclui

- OpenTelemetry, Grafana
- Métricas de negócio
- Dashboards

---

## Entradas

| Tipo | Referência |
|------|------------|
| Módulos existentes | PKG-04 Approved; PKG-05 Approved; PKG-02 — JPA/Oracle |
| Sprint 0 | `infrastructure/logging/` — CorrelationIdFilter, MDC |
| Documentos | `platform-foundation/observability/README.md`, `docs/implementation/09-observability-standards.md` |
| Decisões | **CD-S1A-005** — naming métricas (antes de PF-OBS-001) |
| Riscos | Dados sensíveis em logs — mitigação obrigatória |

---

## Dependências

| Tipo | Referência |
|------|------------|
| Packages obrigatórios | **PKG-04 Approved**, **PKG-05 Approved** |
| Packages opcionais | PKG-02 (DatabaseHealthIndicator) |
| Bloqueios | PKG-04 ou PKG-05 não Approved; CD-S1A-005 aberta bloqueia PF-OBS-001 |

---

## Agentes Envolvidos

| Papel | Agente |
|-------|--------|
| Responsável (R) | `construction-engineer` |
| Apoiadores (C) | — |
| Reviewer (R) | `reviewer` |
| Auditor (R) | `auditor` |
| Platform Architect (C) | `platform-architect` — CD-S1A-005 |

---

## Tarefas

Referência exclusiva: `platform-foundation/observability/tasks.md`

**Intervalo:** PF-OBS-001 até PF-OBS-005

---

## Artefatos Produzidos

### Código

```text
backend/.../infrastructure/observability/
├── config/MetricsConfiguration.java
├── filter/RequestLoggingFilter.java
└── health/DatabaseHealthIndicator.java
```

### Testes

- Mínimo 3 testes: métricas, log estruturado, health indicator

### Documentação

- `platform-foundation/observability/review.md`
- Convenção naming métricas documentada

---

## Critérios de Aceite

1. Métricas em `/actuator/metrics`
2. Request log com method, uri, status, durationMs, correlationId
3. Nenhum dado sensível em logs
4. `/actuator/health` inclui componente database
5. Actuator endpoints com segurança adequada
6. `mvn clean verify` — SUCCESS

---

## Critérios de Aprovação

### Review

- Checklist `platform-foundation/observability/review.md` — 100%

### Audit

- RB-06 preparado
- RC-27 a RC-31 preparados

---

## Definition of Done

Package **Approved** quando:

- [ ] PF-OBS-001 a PF-OBS-005 concluídas
- [ ] Review aprovado
- [ ] Audit aprovado
- [ ] `09-progress.md` — PKG-06 Approved

---

## Próximo Package

**PKG-07** — Testing Foundation

---

## Fluxo Operacional

```text
DoR (PKG-04 + PKG-05 Approved; CD-S1A-005 resolvida)
        ↓
construction-engineer → PF-OBS-001..005
        ↓
Review → Correções → Audit → Approved → PKG-07
```

---

## Atualizações Obrigatórias

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | PKG-06 Approved; tarefas 32/37 |
| `platform-foundation/observability/review.md` | Checklist preenchido |
| `construction/07-open-decisions.md` | Encerrar CD-S1A-005 se resolvida |

---

# PKG-07 — Testing Foundation

## Identificação

| Campo | Valor |
|-------|-------|
| código | PKG-07 |
| nome | Testing Foundation |
| módulo | Testing |
| prefixo | PF-TEST |
| versão | 1.0 |
| status | Não iniciado |
| prioridade | Alta |
| estimativa | 2 dias |

---

## Objetivo

Estabelecer infraestrutura de testes reutilizável: perfil test, `@IntegrationTest`, `AbstractIntegrationTest`, utilitários de segurança e teste E2E do health endpoint.

---

## Escopo

### Inclui

- `application-test.yaml`
- `@IntegrationTest`, `AbstractIntegrationTest`
- `TestSecurityContextFactory`
- Teste integração E2E `GET /api/v1/health`
- Convenções documentadas em README do módulo

### Não inclui

- Testes FT-AUTH (Sprint 1)
- Testes E2E frontend
- Cobertura CI automatizada (futuro)

---

## Entradas

| Tipo | Referência |
|------|------------|
| Módulos existentes | PKG-01 a PKG-06 **Approved** |
| Sprint 0 | 106 testes unitários baseline |
| Documentos | `platform-foundation/testing/README.md`, `specs/foundation/definition-of-done.md` |
| Decisões | **CD-S1A-001** — banco de testes (resolver antes de PF-TEST-001) |
| Componentes | PKG-05 — HealthController; PKG-03 — JwtAuthenticationFilter |

---

## Dependências

| Tipo | Referência |
|------|------------|
| Packages obrigatórios | **PKG-01 a PKG-06 Approved** |
| Packages opcionais | — |
| Bloqueios | Qualquer PKG-01..06 não Approved; CD-S1A-001 aberta bloqueia PF-TEST-001 |

---

## Agentes Envolvidos

| Papel | Agente |
|-------|--------|
| Responsável (R) | `construction-engineer` |
| Apoiadores (C) | — |
| Reviewer (R) | `reviewer` |
| Auditor (R) | `auditor` |
| Platform Architect (C) | `platform-architect` — CD-S1A-001 |

---

## Tarefas

Referência exclusiva: `platform-foundation/testing/tasks.md`

**Intervalo:** PF-TEST-001 até PF-TEST-005

---

## Artefatos Produzidos

### Código

```text
backend/src/test/java/.../support/
├── annotation/IntegrationTest.java
├── base/AbstractIntegrationTest.java
└── security/TestSecurityContextFactory.java

backend/src/test/resources/application-test.yaml
```

### Testes

- Health endpoint E2E via `AbstractIntegrationTest`

### Documentação

- `platform-foundation/testing/review.md`
- Convenções em `platform-foundation/testing/README.md`

---

## Critérios de Aceite

1. Perfil `test` funcional
2. `@IntegrationTest` e `AbstractIntegrationTest` operacionais
3. `TestSecurityContextFactory` funcional
4. E2E health → 200 com `ApiResponse`
5. 106 testes Sprint 0 sem regressão
6. `mvn clean verify` — SUCCESS

---

## Critérios de Aprovação

### Review

- Checklist `platform-foundation/testing/review.md` — 100%

### Audit

- RB-07 preparado
- RC-32 a RC-35 preparados
- RT-04 (≥ 1 teste integração E2E) atendido

---

## Definition of Done

Package **Approved** quando:

- [ ] PF-TEST-001 a PF-TEST-005 concluídas
- [ ] Review aprovado
- [ ] Audit aprovado
- [ ] `09-progress.md` — PKG-07 Approved; tarefas 37/37

---

## Próximo Package

**PKG-08** — Construction Audit

---

## Fluxo Operacional

```text
DoR (PKG-01..06 Approved; CD-S1A-001 resolvida)
        ↓
construction-engineer → PF-TEST-001..005
        ↓
Review → Correções → Audit → Approved → PKG-08
```

---

## Atualizações Obrigatórias

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | PKG-07 Approved; 37/37 tarefas |
| `platform-foundation/testing/review.md` | Checklist preenchido |
| `construction/07-open-decisions.md` | Encerrar CD-S1A-001 se resolvida |

---

# PKG-08 — Construction Audit

## Identificação

| Campo | Valor |
|-------|-------|
| código | PKG-08 |
| nome | Construction Audit |
| módulo | Audit (transversal) |
| prefixo | — |
| versão | 1.0 |
| status | Não iniciado |
| prioridade | Crítica |
| estimativa | 1 dia |

---

## Objetivo

Executar auditoria formal da Platform Foundation, validar readiness completa, emitir relatórios de encerramento e liberar handoff para Sprint 1 (FT-AUTH).

---

## Escopo

### Inclui

- Execução de `construction/review/construction-audit.md` (dimensões A a G)
- Preenchimento de `construction/review/readiness-checklist.md` (RC-*, RR-*, RA-*, RT-*, RB-*)
- Emissão de `construction/review/completion-report.md`
- Atualização de `construction/review/reconciliation-report.md`
- Handoff formal para `feature-implementer`

### Não inclui

- Implementação de código (exceto correções delegadas a PKG-01..07 se audit reprovar)
- Implementação FT-AUTH
- Alteração de specifications

---

## Entradas

| Tipo | Referência |
|------|------------|
| Packages | **PKG-01 a PKG-07 Approved** |
| Documentos | `construction/review/construction-audit.md`, `05-readiness-review.md` |
| Evidências | `backend/`, `backend/runtime/` (logs, reports), `09-progress.md` |
| Baselines | Engineering Baseline, Platform Foundation, Feature Baseline, FT-AUTH (referência) |
| Agentes | `construction-engineer` fornece evidências técnicas |

---

## Dependências

| Tipo | Referência |
|------|------------|
| Packages obrigatórios | **PKG-01 a PKG-07 Approved** |
| Packages opcionais | — |
| Bloqueios | Qualquer PKG-01..07 não Approved |

---

## Agentes Envolvidos

| Papel | Agente |
|-------|--------|
| Responsável (R) | `auditor` |
| Apoiadores (C) | `construction-engineer` (evidências), `reviewer` (histórico reviews) |
| Reviewer (R) | — (reviews já executados por pacote) |
| Auditor (R) | `auditor` |
| Platform Architect (C) | `platform-architect` — bloqueios arquiteturais na auditoria |

**Nota:** `construction-engineer` **não implementa** neste PKG. Apenas fornece artefatos.

---

## Tarefas

Sem tarefas PF-*. Referências de execução:

| Artefato | Conteúdo |
|----------|----------|
| `construction/review/construction-audit.md` | Checklists A-01 a G-04 |
| `construction/review/readiness-checklist.md` | 54 itens RC/RR/RA/RT/RB |
| `construction/05-readiness-review.md` | Definição dos critérios |

---

## Artefatos Produzidos

### Documentação

- `construction/review/construction-audit.md` — preenchido, classificação final
- `construction/review/readiness-checklist.md` — 100% itens
- `construction/review/completion-report.md` — emitido
- `construction/review/reconciliation-report.md` — atualizado pós-implementação

### Saída operacional

- Parecer: **APROVADA** / **REPROVADA** / **APROVADA COM RESSALVAS**
- Handoff documentado para `feature-implementer` (FT-AUTH)

---

## Critérios de Aceite

1. Dimensões A a G do construction-audit — avaliadas
2. 100% itens RC-*, RR-*, RA-*, RT-*, RB-* aprovados
3. `mvn clean verify` — SUCCESS final
4. 106 testes Sprint 0 + novos testes fundação — aprovados
5. Nenhum bloqueador crítico aberto
6. Platform Foundation pronta para FT-AUTH

---

## Critérios de Aprovação

### Review

- Não aplicável — reviews por pacote já concluídos (PKG-01 a PKG-07)

### Audit

- Classificação final: **APROVADA**
- Todas as dimensões A-G sem Não Conforme crítico
- `completion-report.md` emitido
- Handoff RB-01 a RB-08 confirmados

---

## Definition of Done

Package **Approved** quando:

- [ ] construction-audit — **APROVADA**
- [ ] readiness-checklist — 100%
- [ ] completion-report emitido
- [ ] reconciliation-report atualizado
- [ ] `09-progress.md` — Sprint 1A **Concluída**
- [ ] Handoff `feature-implementer` executado

---

## Próximo Package

**Nenhum** — Sprint 1A encerrada.

**Próxima fase:** Sprint 1 — FT-AUTH via `feature-implementer`

---

## Fluxo Operacional

```text
DoR (PKG-01..07 Approved)
        ↓
auditor → construction-audit + readiness-checklist
        ↓
construction-engineer fornece evidências (se solicitado)
        ↓
[Não Conforme] → correções em PKG afetado → re-audit
        ↓
completion-report + reconciliation-report
        ↓
Approved → Handoff feature-implementer
        ↓
Sprint 1A ENCERRADA
```

---

## Atualizações Obrigatórias

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | PKG-08 Approved; Sprint 1A Concluída |
| `construction/review/construction-audit.md` | Preenchido |
| `construction/review/readiness-checklist.md` | Preenchido |
| `construction/review/completion-report.md` | Emitido |
| `construction/review/reconciliation-report.md` | Atualizado |
| `construction/history/README.md` | Registrar encerramento S1A |

---

# Mapa Geral

## Ordem Oficial

```text
PKG-01  Configuration Foundation
   ↓ Approved
PKG-02  Persistence Foundation
   ↓ Approved
PKG-03  Security Foundation
   ↓ Approved
   ├────────────────────┐
   ▼                    ▼
PKG-04 Integration   PKG-05 Web        ← paralelismo permitido
   ↓ Approved            ↓ Approved
   └──────────┬─────────┘
              ▼
PKG-06  Observability Foundation
   ↓ Approved
PKG-07  Testing Foundation
   ↓ Approved
PKG-08  Construction Audit
   ↓ Approved
HANDOFF → feature-implementer (FT-AUTH)
```

**Regra:** sem paralelismo, executar PKG-04 antes de PKG-05. PKG-06 exige ambos Approved.

---

## Matriz de Dependências

| Package | Dependências obrigatórias | Paralelismo | Agente Responsável | Reviewer | Auditor | Próximo Package |
|---------|---------------------------|-------------|-------------------|----------|---------|-----------------|
| PKG-01 | Sprint 0 Approved | — | construction-engineer | reviewer | auditor | PKG-02 |
| PKG-02 | PKG-01 Approved | — | construction-engineer | reviewer | auditor | PKG-03 |
| PKG-03 | PKG-01, PKG-02 Approved | — | construction-engineer | reviewer | auditor | PKG-04, PKG-05 |
| PKG-04 | PKG-01, PKG-03 Approved | Com PKG-05 | construction-engineer | reviewer | auditor | PKG-06 |
| PKG-05 | PKG-01, PKG-03 Approved | Com PKG-04 | construction-engineer | reviewer | auditor | PKG-06 |
| PKG-06 | PKG-04, PKG-05 Approved | — | construction-engineer | reviewer | auditor | PKG-07 |
| PKG-07 | PKG-01..06 Approved | — | construction-engineer | reviewer | auditor | PKG-08 |
| PKG-08 | PKG-01..07 Approved | — | auditor | — | auditor | FT-AUTH |

---

## Matriz de Rastreabilidade

| Package | Tasks | Artefatos código | Review | Audit | Progress |
|---------|-------|------------------|--------|-------|----------|
| PKG-01 | PF-CONF-001..005 | `configuration/properties/` | `configuration/review.md` | auditor | `09-progress.md` |
| PKG-02 | PF-PERS-001..006 | `infrastructure/persistence/` | `persistence/review.md` | auditor | `09-progress.md` |
| PKG-03 | PF-SEC-001..006 | `infrastructure/security/` | `security/review.md` | auditor | `09-progress.md` |
| PKG-04 | PF-INT-001..005 | `infrastructure/integration/` | `integration/review.md` | auditor | `09-progress.md` |
| PKG-05 | PF-WEB-001..005 | `interfaces/rest/` | `web/review.md` | auditor | `09-progress.md` |
| PKG-06 | PF-OBS-001..005 | `infrastructure/observability/` | `observability/review.md` | auditor | `09-progress.md` |
| PKG-07 | PF-TEST-001..005 | `src/test/.../support/` | `testing/review.md` | auditor | `09-progress.md` |
| PKG-08 | — (audit checklists) | — | — (já feito) | `construction-audit.md` | `09-progress.md` + `completion-report.md` |

**Caminho base tasks/review:** `construction/platform-foundation/<modulo>/`

---

## Regras de Execução

1. **Nenhum Package inicia sem DoR** — dependências Approved e decisões bloqueantes resolvidas.
2. **Nenhum Package avança sem Review** — parecer `reviewer` obrigatório (PKG-01 a PKG-07).
3. **Nenhum Package avança sem Auditoria** — classificação `auditor` obrigatória (todos os PKG).
4. **Nenhum Package altera Features** — escopo exclusivo Platform Foundation (regra R-10).
5. **Nenhum Package altera Specifications** — `specs/` é somente leitura.
6. **Nenhum Package gera dependências circulares** — ordem conforme mapa acima.
7. **Execução incremental** — um pacote principal por vez; paralelismo apenas PKG-04/PKG-05.
8. **Sprint 1A termina somente após PKG-08 Approved** — handoff para FT-AUTH.
9. **Build obrigatório** — `mvn clean verify` SUCCESS antes de cada Approved.
10. **Regressão proibida** — 106 testes Sprint 0 devem permanecer aprovados.

---

## Decisões e Riscos por Package

| Package | Decisões bloqueantes | Riscos relevantes |
|---------|---------------------|-------------------|
| PKG-01 | — | — |
| PKG-02 | — | CR-S1A-004 (Oracle dialect) |
| PKG-03 | — | CR-S1A-001, CR-S1A-002 |
| PKG-04 | CD-S1A-004 | CR-S1A-006 |
| PKG-05 | CD-S1A-002 | CR-S1A-002 |
| PKG-06 | CD-S1A-005 | — |
| PKG-07 | CD-S1A-001 | CR-S1A-003, CR-S1A-005 |
| PKG-08 | — | Todos encerrados ou aceitos |

Referência completa: `construction/07-open-decisions.md`, `construction/08-open-risks.md`

---

## Compatibilidade com Agentes

| Agente | Como consome este documento |
|--------|----------------------------|
| Construction Orchestrator | Lê seção PKG-XX; executa fluxo; delega agentes |
| construction-engineer | Implementa intervalo PF-* referenciado |
| reviewer | Valida critérios Review + `review.md` do módulo |
| auditor | Valida critérios Audit; executa PKG-08 |
| platform-architect | Consultado em bloqueios listados em Dependências |
| feature-implementer | Receptor handoff pós PKG-08 |
| backend-engineer | **Não acionado** na Sprint 1A |

---

## Referências

| Documento | Papel |
|-----------|-------|
| `02-construction-roadmap.md` | Cronograma e marcos |
| `04-construction-rules.md` | Regras R-01 a R-10 |
| `06-development-order.md` | Justificativas de ordem |
| `05-readiness-review.md` | Checklist RC-* (PKG-08) |
| `09-progress.md` | Acompanhamento (atualizar por PKG) |
| `.cursor/orchestrator/construction-orchestrator.mdc` | Orquestração |

---

# Histórico de Versões

| Versão | Data | Alteração |
|--------|------|-----------|
| 1.0 | 2026-07-08 | Versão inicial dos Construction Packages |
| 2.0 | 2026-07-08 | Refatoração SSOT — estrutura operacional por PKG; compatível com Construction Orchestrator |
