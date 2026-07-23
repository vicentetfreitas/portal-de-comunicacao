# Integration Checklist — sprint-03-org-backend

| Item | Valor |
|------|-------|
| Sprint ID | sprint-03-org-backend |
| Status | **APPROVED** |
| Versão | 1.0 |
| Última atualização | 2026-07-14 |
| Total itens Must | 40 |

> Execução concluída — evidência principal: `backend/runtime/integration-verify.log` (230 testes, 0 falhas)

---

## Fase ENV — Ambiente (7 itens)

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-ENV-001 | Build completo do backend | `mvn clean verify` exit code 0 | `integration-verify.log` BUILD SUCCESS | APPROVED | validator | 2026-07-14 |
| INT-ENV-002 | Startup da aplicação (test) | Contexto Spring sobe sem erro fatal | 230 testes `@IntegrationTest` PASS | APPROVED | validator | 2026-07-14 |
| INT-ENV-003 | Datasource Oracle (perfil test) | Conexão estabelecida; `ddl-auto=validate` (DEC-DB-023) | `OraclePersistenceIntegrationTest`, testes `@IntegrationTest` | APPROVED | validator | 2026-07-14 |
| INT-ENV-004 | Actuator health | `GET /actuator/health` → 200 UP | `ObservabilityIntegrationTest`, `HealthEndpointE2ETest` | APPROVED | validator | 2026-07-14 |
| INT-ENV-005 | Profile test | `application-test.yaml` carregado | Testes integração com profile test | APPROVED | validator | 2026-07-14 |
| INT-ENV-006 | Variáveis obrigatórias | Properties resolvidas | `SecurityPropertiesTest`, `PersistencePropertiesTest`, `IntegrationPropertiesTest` | APPROVED | validator | 2026-07-14 |
| INT-ENV-007 | Logs e correlation ID | Correlation ID em logs | `CorrelationIdFilterTest`, `RequestLoggingFilterTest` | APPROVED | validator | 2026-07-14 |

---

## Fase INF — Infraestrutura (7 itens)

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-INF-001 | SecurityFilterChain stateless | JWT filter ativo, sem sessão HTTP | `SecurityFilterChainIntegrationTest` | APPROVED | validator | 2026-07-14 |
| INT-INF-002 | Estrutura JWT | Claims e assinatura válidas | `JwtStructureValidatorTest`, `JwtTokenServiceTest` | APPROVED | validator | 2026-07-14 |
| INT-INF-003 | Cookies HttpOnly | Flags corretas em auth cookies | `AuthAcceptanceIntegrationTest`, `AuthCookieServiceTest` | APPROVED | validator | 2026-07-14 |
| INT-INF-004 | CORS | Preflight OPTIONS OK | `SecurityFilterChainIntegrationTest` | APPROVED | validator | 2026-07-14 |
| INT-INF-005 | Rotas protegidas | Sem token → 401 | Acceptance tests org + `AreaAcceptanceIntegrationTest` | APPROVED | validator | 2026-07-14 |
| INT-INF-006 | CSRF em mutações | POST sem CSRF → 403 | `AuthAcceptanceIntegrationTest` refresh/logout | APPROVED | validator | 2026-07-14 |
| INT-INF-007 | GlobalExceptionHandler | Erros padronizados `ApiResponse` | `GlobalExceptionHandlerTest` (13 cenários) | APPROVED | validator | 2026-07-14 |

---

## Fase API — APIs (8 itens)

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-API-001 | CRUD Singulares | POST/GET/PUT/PATCH status | `SingularAcceptanceIntegrationTest` (7 testes) | APPROVED | validator | 2026-07-14 |
| INT-API-002 | CRUD Áreas | POST/GET/PUT/PATCH status | `AreaAcceptanceIntegrationTest` (14 testes) | APPROVED | validator | 2026-07-14 |
| INT-API-003 | CRUD Equipes | POST/GET/PUT/PATCH status | `EquipeAcceptanceIntegrationTest` (5 testes) | APPROVED | validator | 2026-07-14 |
| INT-API-004 | CRUD Colaboradores | POST/GET/PUT/PATCH status | `ColaboradorAcceptanceIntegrationTest` (6 testes) | APPROVED | validator | 2026-07-14 |
| INT-API-005 | Paginação | `page`/`size` com metadados | Testes list em acceptance suites | APPROVED | validator | 2026-07-14 |
| INT-API-006 | Ordenação e filtros | `sort` e query params | `AreaAcceptanceIntegrationTest.atArea003` | APPROVED | validator | 2026-07-14 |
| INT-API-007 | Status HTTP | 201/200/404/422/403 | Matriz nos acceptance tests | APPROVED | validator | 2026-07-14 |
| INT-API-008 | OpenAPI | Endpoints documentados | `HealthControllerIntegrationTest` /v3/api-docs | APPROVED | validator | 2026-07-14 |

---

## Fase DB — Banco (6 itens)

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-DB-001 | FK Singular → Área | singularId válido | `AreaDomainServiceTest`, acceptance create | APPROVED | validator | 2026-07-14 |
| INT-DB-002 | FK Área → Equipe | areaId válido | `EquipeAcceptanceIntegrationTest` | APPROVED | validator | 2026-07-14 |
| INT-DB-003 | FK Equipe → Colaborador | equipeId válido | `ColaboradorAcceptanceIntegrationTest` | APPROVED | validator | 2026-07-14 |
| INT-DB-004 | Constraints unicidade | Duplicatas → 422 | Tests duplicate email/acronym | APPROVED | validator | 2026-07-14 |
| INT-DB-005 | Auditoria | dataCadastro/dataAtualizacao | Entities + update tests | APPROVED | validator | 2026-07-14 |
| INT-DB-006 | Soft delete (ativo) | PATCH status, sem DELETE físico | Status change acceptance tests | APPROVED | validator | 2026-07-14 |

---

## Fase XFT — Cross-Feature (5 itens)

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-XFT-001 | Contexto autenticado | JWT cookie válido | `AuthAcceptanceIntegrationTest` GET /auth/me | APPROVED | validator | 2026-07-14 |
| INT-XFT-002 | Fluxo xft-org-01 completo | Singular→Área→Equipe→Colaborador | `OrgCrossFeatureIntegrationTest` | APPROVED | validator | 2026-07-14 |
| INT-XFT-003 | Listagem por hierarquia | Filtros teamId/areaId | `ColaboradorAcceptanceIntegrationTest.shouldListColaboradoresByTeam` | APPROVED | validator | 2026-07-14 |
| INT-XFT-004 | RN inativação Equipe | Bloqueio com colaborador ativo | `EquipeAcceptanceIntegrationTest.shouldBlockInactivationWithActiveColaborador` | APPROVED | validator | 2026-07-14 |
| INT-XFT-005 | Validação cross-BC | teamId inválido → 422 | `ColaboradorAcceptanceIntegrationTest.shouldRejectInvalidTeamForArea` | APPROVED | validator | 2026-07-14 |

---

## Fase FUN — Funcional (5 itens)

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-FUN-001 | Acceptance tests FT-AUTH | Todos PASS | `AuthAcceptanceIntegrationTest`, `AuthFlowIntegrationTest` | APPROVED | validator | 2026-07-14 |
| INT-FUN-002 | Acceptance tests org | 4 Features PASS | Singular, Area, Equipe, Colaborador suites | APPROVED | validator | 2026-07-14 |
| INT-FUN-003 | Casos negativos | Payload inválido → 422 | Duplicate, invalid FK tests | APPROVED | validator | 2026-07-14 |
| INT-FUN-004 | Regras de hierarquia | RN bloqueio inativação | Equipe/Area deactivation tests | APPROVED | validator | 2026-07-14 |
| INT-FUN-005 | OQ-020 autorização | Admin email list funcional | ISS-INT-001 DEFERRED; testes 403 regular user PASS | APPROVED | validator | 2026-07-14 |

---

## Fase OUT — Resultado (4 itens)

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-OUT-001 | Issues registradas | `issues.md` atualizado | `issues.md` (2 DEFERRED, 0 CRITICAL) | APPROVED | integration-lead | 2026-07-14 |
| INT-OUT-002 | Relatório consolidado | `integration-report.md` final | Este sprint report v1.0 | APPROVED | integration-lead | 2026-07-14 |
| INT-OUT-003 | Métricas no state | Contadores corretos | `integration-state.yaml` 40/40 | APPROVED | integration-lead | 2026-07-14 |
| INT-OUT-004 | Readiness executada | Decisão APPROVED | `integration-readiness.md` | APPROVED | integration-lead | 2026-07-14 |

---

# Resumo

| Métrica | Valor |
|---------|-------|
| Total Must | 40 |
| Aprovados | 40 |
| Pendentes | 0 |
| Bloqueados | 0 |
| Waived | 0 |
