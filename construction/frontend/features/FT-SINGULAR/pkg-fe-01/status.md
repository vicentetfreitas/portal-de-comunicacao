# PKG-FE-01 — Singular Module Scaffold & API Client

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR (Frontend) |
| PKG | FE-01 |
| Status | **DONE** |
| Data início | 2026-07-16 |
| Data conclusão | 2026-07-16 |
| DoR | ✅ Satisfeito (State Sync 2026-07-16) |
| Executor | feature-implementer |

---

# Escopo

Infraestrutura do módulo frontend `organization/singular`: tipos TypeScript, **`SingularApiService extends BaseApiClient`**, composable base e registro de rotas (stubs).

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| Config API | `frontend/src/config/organization.ts` | ✅ |
| Types | `frontend/src/types/organization/singular.types.ts` | ✅ |
| Service | `frontend/src/services/organization/singular.service.ts` | ✅ |
| Barrel service | `frontend/src/services/organization/index.ts` | ✅ |
| Composable form | `frontend/src/composables/organization/useSingularForm.ts` | ✅ |
| Rotas | `frontend/src/router/routes/organization/singular.routes.ts` | ✅ |
| Stub pages | `frontend/src/pages/organization/singular/*.vue` (5) | ✅ |
| i18n | `frontend/src/i18n/pt-BR.ts` — chaves `singular.*` | ✅ |
| Route constants | `frontend/src/constants/routes.ts` | ✅ |
| Teste service | `frontend/test/unit/organization/singular.service.spec.ts` | ✅ |
| Routes test | `frontend/test/unit/infrastructure/routes.spec.ts` | ✅ |

## Reuso Foundation

| Componente | Utilizado |
|------------|-----------|
| BaseApiClient | ✅ |
| getHttpClient | ✅ |
| useFormValidation | ✅ |
| AdminLayout + guards meta | ✅ |
| DsPageHeader / DsCard | ✅ (stubs) |

---

# Validação

| Verificação | Resultado |
|-------------|-----------|
| `SingularApiService extends BaseApiClient` | ✅ |
| Rotas registradas em `createModularRoutes()` | ✅ |
| Testes unitários service | ✅ (Vitest) |
| Infraestrutura paralela criada | ❌ Nenhuma (conforme DoR) |

---

# Rastreabilidade

Base para TASK-SINGULAR-FE-001..005 e PKG-FE-02..06.

---

# Próximo PKG

**PKG-FE-02** — Create Singular Page
