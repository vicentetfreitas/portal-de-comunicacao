# Relatório de Homologação — Sprint API-VALIDATION-01

| Item | Valor |
|------|-------|
| Sprint | **API-VALIDATION-01** |
| Data | 2026-07-16 |
| Escopo | 27 endpoints `/api/v1` documentados em `docs/api/` |
| Método primário | Testes de aceite Java (MockMvc + Oracle, perfil `test`, DEC-DB-023) |
| Método complementar | Coleção Postman com scripts `pm.test` |
| Evidência integração | Sprint 03 org-backend — **APPROVED** (2026-07-14) |

---

## Resumo executivo

A homologação dos endpoints documentados foi **aprovada**. Todos os 27 endpoints implementados possuem cobertura de testes de aceite automatizados com cenários positivos e negativos. A coleção Postman foi instrumentada com 58 requisições e scripts de validação de contrato para execução manual ou via Newman.

Cenários de autenticação Zimbra (login/callback redirect) dependem de integração externa — homologados via mocks nos testes Java; execução Postman requer ambiente configurado ou cookies pré-importados.

---

## Endpoints homologados (27/27)

| # | Grupo | Endpoints | Status |
|---|-------|-----------|--------|
| 1 | Health | 1 | ✅ APROVADO |
| 2 | Authentication | 5 | ✅ APROVADO |
| 3 | Admin Sessions | 1 | ✅ APROVADO |
| 4 | Singulares | 5 | ✅ APROVADO |
| 5 | Áreas | 5 | ✅ APROVADO |
| 6 | Equipes | 5 | ✅ APROVADO |
| 7 | Colaboradores | 5 | ✅ APROVADO |

---

## Endpoints com falha

**Nenhum.** Todos os endpoints implementados passaram nos testes de aceite referenciados na Sprint de Integração 03 e na matriz de testes.

| Observação | Detalhe |
|------------|---------|
| Auth Zimbra live | Cenários 302/503 não executados contra Zimbra real nesta sprint — cobertos por mocks |
| Postman Newman | Execução CI Newman pendente de pipeline — scripts prontos |

---

## Casos de teste executados

### Testes Java (fonte canônica)

| Suíte | Casos | Resultado |
|-------|-------|-----------|
| `AuthAcceptanceIntegrationTest` | 14 | ✅ PASS |
| `AreaAcceptanceIntegrationTest` | 14 | ✅ PASS |
| `SingularAcceptanceIntegrationTest` | 7 | ✅ PASS |
| `EquipeAcceptanceIntegrationTest` | 5 | ✅ PASS |
| `ColaboradorAcceptanceIntegrationTest` | 6 | ✅ PASS |
| `OrgCrossFeatureIntegrationTest` | 1 | ✅ PASS |
| `HealthControllerIntegrationTest` | 3 | ✅ PASS |
| **Total** | **50** | **✅ PASS** |

Evidência: `engineering/integration/sprints/sprint-03-org-backend/integration-report.md`

### Coleção Postman (instrumentada)

| Métrica | Valor |
|---------|-------|
| Requisições totais | 58 |
| Com script `pm.test` | 58 |
| Cenários positivos | 32 |
| Cenários negativos | 26 |
| Folders | Health, Auth (+/-), 4 recursos (+/-), Admin, Cross-flow |

Arquivo: `docs/api/postman/Portal.postman_collection.json` (v2 — API-VALIDATION-01)

---

## Validações realizadas

| Dimensão | Cobertura | Evidência |
|----------|-----------|-----------|
| Contrato ApiResponse | ✅ | Testes JSONPath `$.success`, Postman scripts |
| Contrato ErrorResponse | ✅ | 401, 403, 404, 422 nos testes |
| Contrato PageResponse | ✅ | Listagens com filtros e paginação |
| Autenticação cookie JWT | ✅ | AuthAcceptance, SecurityFilterChain |
| Autorização admin (escrita) | ✅ | 403 non-admin em AreaAcceptance |
| CSRF em mutações | ✅ | Todos acceptance tests enviam X-XSRF-TOKEN |
| Payloads request/response | ✅ | DTOs validados vs controllers |
| Paginação `page`, `size` | ✅ | AreaAcceptance AT-AREA-003 |
| Filtros query params | ✅ | status, singularId, areaId, teamId, etc. |
| Códigos HTTP | ✅ | 200, 201, 204, 302, 400, 401, 403, 404, 422, 503 |
| Regras de negócio 422 | ✅ | Duplicatas, hierarquia, inativação bloqueada |
| Fluxo cross-feature | ✅ | OrgCrossFeatureIntegrationTest |

---

## Divergências encontradas

Novas divergências registradas em `docs/api/discrepancies.md`:

| ID | Resumo |
|----|--------|
| DISC-011 | Homologação Postman auth positiva requer Zimbra/cookies — testes Java usam mock |
| DISC-012 | Newman não integrado ao CI — homologação Postman manual |

Divergências anteriores (DISC-001..010) permanecem válidas — ver `discrepancies.md`.

---

## Cobertura estimada da homologação

| Métrica | Valor |
|---------|-------|
| Endpoints implementados homologados | **27/27 (100%)** |
| Endpoints documentados sem implementação | 0 homologados (N/A) |
| Cenários positivos (Java) | 32 |
| Cenários negativos (Java) | 18 |
| Features API (`specs/`) cobertas | **5/5 (100%)** |
| Contratos validados vs `docs/api/` | **Alinhados** (com ressalvas DISC-001..002) |
| Coleção Postman instrumentada | **58 casos** |

**Cobertura global estimada: 100%** dos endpoints implementados, com ressalva de auth Zimbra live e execução Newman CI.

---

## Decisão

| Campo | Valor |
|-------|-------|
| **Resultado** | **APROVADO** |
| **Condições** | Auth Zimbra live validar em ambiente staging; integrar Newman no CI quando pipeline disponível |
| **Próximo passo** | Homologar `/api/v1/documentos` quando Feature Gestão Documental for implementada |

---

## Referências

- `docs/api/validation/test-matrix.md`
- `docs/api/postman/Portal.postman_collection.json`
- `engineering/integration/sprints/sprint-03-org-backend/integration-report.md`
- `docs/api/discrepancies.md`
