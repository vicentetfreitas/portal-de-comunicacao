# Feature Session — Singular (Frontend)

| Item | Valor |
|------|-------|
| Feature Code | FT-SINGULAR |
| Feature Slug | singular |
| Camada | Frontend |
| Sprint | 2 |
| Session | **v2** (State Sync 2026-07-16) |
| Session anterior | v1 — 2026-07-16 (DoR obsoleto — superseded) |
| Data da sessão | 2026-07-16 |
| Agente | construction-orchestrator |
| SSOD | `construction/frontend/features/FT-SINGULAR/feature-manifest.yaml` |
| Estado operacional | `construction/frontend/features/FT-SINGULAR/construction-state.yaml` |
| Sync report | `reports/state-sync-2026-07-16.md` |
| Imutabilidade | **READ ONLY** após criação (SESSION-01) |

---

# Regra SESSION-01 / STATE-04

Esta Session é **imutável** durante toda a execução da Feature frontend.

Progresso operacional: `construction/frontend/features/FT-SINGULAR/construction-state.yaml` (SSOT).

**v2:** recriada por State Sync — correção de DoR e pré-requisitos (CACHE-02 / divergência documental).

---

# Snapshot de Contexto

## Feature

| Campo | Valor |
|-------|-------|
| Code | FT-SINGULAR |
| Slug | singular |
| Tipo | business_feature (CRUD Reference — Frontend MVP) |
| Objetivo | Interface administrativa CRUD de Singulares via `/api/v1/singulares` |

## Objetivos

- Hub administrativo de singulares
- Cadastrar singular (formulário + POST)
- Listar singulares com paginação, filtros e ordenação
- Consultar detalhe por identificador
- Editar dados cadastrais (federação somente leitura)
- Ativar/inativar via dialog de confirmação
- Testes Vitest + Playwright mapeados aos AT-FE-SINGULAR-001..005

## Premissas

- **Frontend Foundation** encerrada — `FEATURE_APPROVED`, PKG-FE-S0-01..10 concluídos, infraestrutura reutilizável
- **FT-AUTH frontend** encerrada — sessão via cookies HttpOnly, guards ativos, CSRF, HTTP client integrado
- **FT-SINGULAR backend** encerrada — `FEATURE_APPROVED`, API `/api/v1/singulares` disponível
- Especificação v1.1.1 APPROVED em `specs/features/singular/`
- Federação no cadastro via seed conhecido até FT-FEDERACAO (DS-SINGULAR-FE-01)

## Restrições

- Sem regras de negócio no frontend (RN permanecem no backend)
- Sem permissões granulares (OrganizationPermissions) — FT-PERMISSAO
- Sem auditoria, Minha Singular, painel por slug ou gestão de áreas nesta entrega
- Service layer obrigatória — `SingularApiService extends BaseApiClient` (DS-SINGULAR-FE-02)
- Não alterar contratos API nem código backend
- Não criar infraestrutura paralela (HTTP, auth, layouts, DS)

## Infraestrutura reutilizável (PKG-FE-01+)

| Componente | Caminho |
|------------|---------|
| BaseApiClient | `frontend/src/services/http/base-api-client.ts` |
| HTTP Client | `frontend/src/services/http/axios-instance.ts` |
| Auth Store | `frontend/src/stores/auth-store.ts` |
| Auth Service | `frontend/src/services/auth/auth.service.ts` |
| Router + Guards | `frontend/src/router/`, `frontend/src/router/guards/` |
| Layouts | `frontend/src/layouts/` (`AdminLayout` para rotas admin) |
| Design System | `frontend/src/components/ds/` |
| Shared / AppShell | `frontend/src/components/shared/`, `frontend/src/components/app/` |
| Composables | `useAuth`, `useStandardErrorHandling`, `useFormValidation`, `useNotify`, `useLoading` |

## Contratos (consumo UI)

| Endpoint | Método | Uso na UI |
|----------|--------|-----------|
| `/api/v1/singulares` | POST | Cadastro |
| `/api/v1/singulares/{id}` | GET | Detalhe |
| `/api/v1/singulares` | GET | Listagem (filtros: status, federacaoId, name, acronym, codigoUnimed) |
| `/api/v1/singulares/{id}` | PUT | Edição |
| `/api/v1/singulares/{id}/status` | PATCH | Ativar/inativar |

**DTOs:** `CreateSingularRequest`, `UpdateSingularRequest`, `UpdateSingularStatusRequest`, `SingularResponse`.

**Envelopes:** `ApiResponse<T>`, `PageResponse<T>`, `ErrorResponse`.

**Rotas TO-BE:**

```text
/app/administrador/singulares              → Hub
/app/administrador/singulares/lista        → Listagem
/app/administrador/singulares/novo         → Cadastro
/app/administrador/singulares/:id          → Detalhe
/app/administrador/singulares/:id/editar   → Edição
```

Meta: `requiresAuth: true`, `layout: AdminLayout`, breadcrumbs via `route.meta`.

## Dependências

| Dependência | Status |
|-------------|--------|
| Frontend Foundation | ✅ `FEATURE_APPROVED` |
| FT-AUTH frontend | ✅ `FEATURE_APPROVED` |
| FT-SINGULAR backend | ✅ `FEATURE_APPROVED` |
| API contract v1.1.1 | ✅ APPROVED |

## Decisões

| ID | Decisão |
|----|---------|
| DS-SINGULAR-FE-01 | Select de federação via seed (`federacaoId` conhecido) até FT-FEDERACAO |
| DS-SINGULAR-FE-02 | `SingularApiService extends BaseApiClient` — único ponto de chamada HTTP |
| DS-SINGULAR-FE-03 | Listagem via composable `useSingularList` (Pinia opcional) |
| DS-SINGULAR-FE-04 | Badge de status + confirmação antes de inativar |
| DS-SINGULAR-FE-05 | Erros 422 via `useStandardErrorHandling` / `useNotify` |
| DS-SINGULAR-FE-06 | Rotas registradas em `router/routes/organization/singular.routes.ts` via `createModularRoutes()` |

## PKGs

| PKG | Nome | Tasks | Dependências |
|-----|------|-------|--------------|
| PKG-FE-01 | Singular Module Scaffold & API Client | Base | Foundation ✅, FT-AUTH ✅ |
| PKG-FE-02 | Create Singular Page | TASK-SINGULAR-FE-001 | PKG-FE-01 |
| PKG-FE-03 | List & Detail Pages | FE-002, FE-003 | PKG-FE-01 |
| PKG-FE-04 | Edit Singular Page | TASK-SINGULAR-FE-004 | PKG-FE-02, FE-03 |
| PKG-FE-05 | Status Change UI | TASK-SINGULAR-FE-005 | PKG-FE-03 |
| PKG-FE-06 | Admin Hub, Tests & Closure | AT-FE-001..005 | PKG-FE-02..05 |

## Artefatos

| Camada | Artefato | Pontos-chave |
|--------|----------|--------------|
| Especificação | `specification.md` | 5 RF backend — UI materializa RF-001..005 |
| API | `api.md` | 5 endpoints, 4 DTOs |
| Frontend tasks | `frontend-tasks.md` | TASK-SINGULAR-FE-001..005 |
| Discovery | `frontend-feature-mapping.md` | Telas legado → rotas TO-BE |
| Foundation SSOT | `construction/frontend/construction-state.yaml` | FEATURE_APPROVED |
| FT-AUTH SSOT | `construction/features/FT-AUTH/construction-state.yaml` | frontend done |

## Riscos

| Risco | Mitigação |
|-------|-----------|
| Sem API de federações | DS-SINGULAR-FE-01 — seed até FT-FEDERACAO |
| Matriz RBAC incompleta (OQ-020) | Guards FT-AUTH; 403 via `useStandardErrorHandling` |
| Rotas admin vs legado slug | Rotas TO-BE `/app/administrador/singulares/*` |

## Pendências

- Nenhuma bloqueante de infraestrutura para PKG-FE-01
- Código de domínio singular ainda não existe em `frontend/src/` (escopo PKG-FE-01..06)

---

# Definition of Ready

| Critério | Atendido |
|----------|----------|
| Manifesto frontend presente | ✅ |
| Frontend Foundation FEATURE_APPROVED | ✅ |
| FT-AUTH Frontend FEATURE_APPROVED | ✅ |
| FT-SINGULAR Backend FEATURE_APPROVED | ✅ |
| API contract v1.1.1 APPROVED | ✅ |
| frontend-tasks com backlog | ✅ |
| acceptance-tests backend definidos | ✅ |
| Infraestrutura reutilizável confirmada | ✅ |
| **DoR global** | **✅ Satisfeito** |

---

# Validação de Consistência

| Verificação | Resultado |
|-------------|-----------|
| Manifesto válido e completo | ✅ |
| Sem conflito specs vs discovery | ✅ |
| Ordem de PKGs válida | ✅ |
| DoR atendida | ✅ |
| Sem necessidade de mocks/infra paralela | ✅ |

---

# Próximo Passo

Executar **PKG-FE-01** — `SingularApiService extends BaseApiClient`, types, rotas organization/singular.
