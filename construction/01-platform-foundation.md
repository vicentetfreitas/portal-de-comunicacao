# Platform Foundation

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Sprint | 1A |
| Status | Aprovado |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Definir a **Platform Foundation** — conjunto de capacidades técnicas compartilhadas que sustentam a implementação de todas as Features do Portal de Comunicação.

A Platform Foundation não representa funcionalidades de negócio, domínio ou regras de negócio. Representa infraestrutura reutilizável que elimina duplicação e garante consistência arquitetural entre Features.

---

# Escopo

## Inclui

- Módulos de infraestrutura transversal do backend
- Componentes reutilizáveis por bounded contexts futuros
- Preparação técnica para FT-AUTH e Features subsequentes
- Complemento à baseline congelada da Sprint 0

## Não inclui

- Bounded contexts de domínio (organization, accesscontrol, etc.)
- Endpoints de negócio
- Lógica de autenticação Zimbra (responsabilidade de FT-AUTH)
- Entidades de domínio de Features
- Frontend

---

# Contexto — Sprint 0 vs Sprint 1A

| Aspecto | Sprint 0 (Concluída) | Sprint 1A (Platform Foundation) |
|---------|----------------------|--------------------------------|
| Objetivo | Bootstrap e shared modules | Infraestrutura compartilhada completa |
| Configuration | Jackson, Locale, Async, ApplicationProperties | Properties de Security, Persistence, Integration |
| Persistence | Baseline DDL (DBA) | Camada JPA, repositórios base, convenções |
| Security | Classpath apenas | SecurityFilterChain foundation, CSRF base |
| Integration | — | Cliente HTTP, resiliência, contratos base |
| Web | — | Camada REST, OpenAPI, health endpoint |
| Observability | Correlation ID + MDC | Métricas, request logging, health indicators |
| Testing | Testes unitários shared | Infraestrutura de testes de integração |

**Regra:** A Sprint 0 permanece congelada. A Sprint 1A estende sem alterar o que já foi entregue.

---

# Módulos

## 1. Configuration

**Propósito:** Centralizar configuração tipada e validada de todos os módulos da fundação.

**Pacote:** `configuration/`

**Componentes esperados:**

- `ApplicationProperties` (Sprint 0 — existente)
- `SecurityProperties` — JWT, CSRF, CORS
- `PersistenceProperties` — datasource, pool, JPA
- `IntegrationProperties` — timeouts, retry, circuit breaker
- `ZimbraProperties` — URLs e parâmetros de conexão (estrutura; implementação FT-AUTH)
- Beans de configuração por módulo

**Limites:** Sem lógica de negócio. Sem beans de domínio.

---

## 2. Persistence

**Propósito:** Estabelecer camada de persistência reutilizável sobre Oracle Database.

**Pacote:** `infrastructure/persistence/`

**Componentes esperados:**

- `JpaConfiguration` — EntityManager, transações
- `BaseEntity` / `AuditableEntity` — campos comuns (id, createdAt, updatedAt)
- `BaseRepository` — interface base Spring Data JPA
- Convenções de evolução estrutural via scripts DDL (DBA)
- `PersistenceException` e mapeamento para `GlobalExceptionHandler`

**Limites:** Sem entidades de domínio de Features. Sem queries de negócio.

---

## 3. Security

**Propósito:** Infraestrutura de segurança stateless reutilizável por FT-AUTH e demais Features.

**Pacote:** `infrastructure/security/`

**Componentes esperados:**

- `SecurityConfiguration` — SecurityFilterChain stateless
- `CsrfConfiguration` — token repository para cookies
- `JwtAuthenticationFilter` — esqueleto de validação JWT (extensível por FT-AUTH)
- `SecurityConstants` integração (Sprint 0 — existente em `shared/constants/`)
- Whitelist de endpoints públicos (`/actuator/health`, `/api/v1/health`)
- `CorsConfiguration` base

**Limites:** Sem fluxo de login/callback Zimbra. Sem emissão de tokens (FT-AUTH). Sem tabela AUTH_SESSAO.

---

## 4. Integration

**Propósito:** Infraestrutura para consumo de sistemas externos com resiliência e observabilidade.

**Pacote:** `infrastructure/integration/`

**Componentes esperados:**

- `RestClientConfiguration` / `WebClientConfiguration`
- `IntegrationException` hierarchy
- Propagação de Correlation ID em chamadas outbound
- Configuração de timeout, retry e circuit breaker
- Interface `IdentityProviderClient` (contrato abstrato para Zimbra — implementação FT-AUTH)
- `IntegrationGateway` pattern base

**Limites:** Sem implementação concreta Zimbra. Sem regras de autenticação.

---

## 5. Web

**Propósito:** Camada de exposição REST padronizada para todas as Features.

**Pacote:** `interfaces/rest/`

**Componentes esperados:**

- Estrutura `controller/`, `request/`, `response/`, `mapper/`
- `HealthController` — `GET /api/v1/health`
- Integração com `GlobalExceptionHandler` (Sprint 0)
- Configuração OpenAPI 3 / SpringDoc
- Filtro de versionamento `/api/v1`
- Padrão de resposta `ApiResponse<T>` (Sprint 0 — existente)

**Limites:** Sem controllers de negócio. Sem endpoints FT-AUTH.

---

## 6. Observability

**Propósito:** Visibilidade operacional além do logging básico da Sprint 0.

**Pacote:** `infrastructure/observability/`

**Componentes esperados:**

- Métricas Micrometer (registry, convenções de naming)
- `RequestLoggingFilter` — log estruturado de requisições
- Health indicators customizados (database, integrações)
- Configuração Actuator endpoints
- Integração Correlation ID → logs (emissão completa)

**Limites:** Sem dashboards Grafana. Sem OpenTelemetry (decisão futura).

---

## 7. Testing

**Propósito:** Infraestrutura de testes reutilizável para unitários e integração.

**Pacote:** `src/test/java/.../support/`

**Componentes esperados:**

- `@IntegrationTest` meta-anotação
- `TestSecurityContext` — utilitários para testes com JWT mock
- `AbstractIntegrationTest` — base com perfil de teste
- Configuração de banco de testes (H2 ou Testcontainers — decisão pendente)
- Builders e fixtures compartilhados
- Convenções de nomenclatura e organização

**Limites:** Sem testes de cenários FT-AUTH. Sem testes de negócio.

---

# Dependências entre Módulos

```text
Configuration ─────────────────────────────────────────┐
        │                                              │
        ▼                                              │
Persistence ◄────────────────────────────────────────┤
        │                                              │
        ▼                                              │
Security ◄── Configuration                           │
        │                                              │
        ▼                                              │
Integration ◄── Configuration, Security (propagação)   │
        │                                              │
        ▼                                              │
Web ◄── Security, Configuration                        │
        │                                              │
        ▼                                              │
Observability ◄── Web, Integration, Persistence        │
        │                                              │
        ▼                                              │
Testing ◄── Todos os módulos anteriores ───────────────┘
```

---

# Ordem de Construção

| # | Módulo | Justificativa |
|---|--------|---------------|
| 1 | Configuration | Properties e beans base para todos os módulos |
| 2 | Persistence | Security e Features dependem de acesso a dados |
| 3 | Security | Integration e Web exigem contexto de segurança |
| 4 | Integration | Web e FT-AUTH consomem clientes externos |
| 5 | Web | Expõe contratos REST padronizados |
| 6 | Observability | Instrumenta camadas já construídas |
| 7 | Testing | Valida infraestrutura completa |

---

# Critérios de Aceite da Platform Foundation

A Platform Foundation estará pronta quando:

1. Todos os sete módulos atingirem Definition of Done
2. `mvn clean verify` executa com sucesso
3. `GET /api/v1/health` responde com status operacional
4. SecurityFilterChain stateless configurado e testado
5. Camada de persistência JPA operacional com Oracle
6. Cliente HTTP configurado com timeout e propagação de Correlation ID
7. OpenAPI documenta endpoints de infraestrutura
8. Métricas básicas expostas via Actuator
9. Infraestrutura de testes de integração funcional
10. Nenhuma dependência circular entre módulos
11. FT-AUTH pode iniciar sem reimplementar componentes da fundação

---

# Definition of Done — Platform Foundation

| Critério | Descrição |
|----------|-----------|
| Implementação | Todas as tarefas PF-* dos módulos concluídas |
| Testes | Testes unitários e de integração dos módulos aprovados |
| Build | `mvn clean verify` — SUCCESS |
| Documentação | `review.md` de cada módulo aprovado |
| Auditoria | `review/construction-audit.md` — APROVADO |
| Rastreabilidade | Tarefas PF-* rastreáveis até código |
| Reutilização | Componentes documentados como API interna da fundação |
| FT-AUTH | Pré-requisitos técnicos atendidos (checklist em `05-readiness-review.md`) |

---

# Relação com FT-AUTH

A Platform Foundation fornece a infraestrutura que FT-AUTH consome:

| Componente Foundation | Uso em FT-AUTH |
|-----------------------|----------------|
| SecurityFilterChain stateless | TASK-AUTH-BE-001 |
| CSRF base | TASK-AUTH-BE-002 |
| Persistence + DDL (DBA) | TASK-AUTH-DB-001, DB-002 |
| Integration client base | TASK-AUTH-INT-001 |
| Web layer REST | Endpoints `/api/v1/auth/*` |
| Observability | TASK-AUTH-BE-012 |
| Testing infrastructure | TASK-AUTH-QA-* |

FT-AUTH implementa regras de negócio e fluxos sobre a fundação — não duplica infraestrutura.

---

# Referências

- `docs/implementation/02-repository-structure.md`
- `docs/implementation/11-bootstrap-roadmap.md` — Backlog pós-Sprint 0
- `docs/governance/history/phase2-backend-construction-report.md`
- `specs/architecture/authentication-architecture.md`
- `platform-foundation/*/README.md` — Detalhamento por módulo
