# Execution Plan — FT-SINGULAR Frontend (Singular)

| Item | Valor |
|------|-------|
| Feature Code | **FT-SINGULAR** |
| Feature Slug | singular |
| Camada | Frontend |
| Sprint | **2** |
| Status | **Em execução** |
| SSOD | `construction/frontend/features/FT-SINGULAR/feature-manifest.yaml` |
| Construction State | `construction/frontend/features/FT-SINGULAR/construction-state.yaml` |
| Backend Construction | `construction/features/FT-SINGULAR/` (`FEATURE_APPROVED`) |
| Versão | 1.1 |
| Última atualização | 2026-07-16 (State Sync) |

---

# Objetivo

Implementar a interface administrativa de **CRUD de Singulares** no Portal de Comunicação, consumindo a API backend validada em `/api/v1/singulares` e alinhada ao mapeamento legado (`docs/discovery/frontend-feature-mapping.md` — seção FT-SINGULAR).

Consultar `feature-manifest.yaml` (SSOD) antes de qualquer outro artefato.

---

# Escopo

## Inclui

- Módulo frontend `organization/singular` (types, service, composables, rotas)
- Hub administrativo de singulares (equivalente funcional a `OrganizationIndexPage`)
- Página de **cadastro** (`POST /api/v1/singulares`)
- Página de **listagem** paginada com filtros (`GET /api/v1/singulares`)
- Página de **detalhe** (`GET /api/v1/singulares/{id}`)
- Página de **edição** (`PUT /api/v1/singulares/{id}`)
- Ação de **ativação/inativação** (`PATCH /api/v1/singulares/{id}/status`)
- Componentes de domínio: formulário, seções de informação básica, listagem
- Tratamento visual de erros HTTP (400, 401, 403, 404, 422) via infraestrutura compartilhada
- Testes Vitest (componentes/serviços) e Playwright (fluxos E2E dos ATs)
- Breadcrumbs e page header via Design System da Frontend Foundation

## Não inclui

- Permissões por singular (`OrganizationPermissions`) — FT-PERMISSAO
- Auditoria (`OrganizationAudit`) — Feature futura
- Painel contextual por slug (`SinglePage` em `/app/:singularSlug`) — Sprint posterior
- Minha Singular (`CollaboratorOrganizationPage`) — Sprint posterior
- Gestão de áreas dentro da singular (`DepartmentManagement`) — FT-AREA frontend
- CRUD de Federação — FT-FEDERACAO (select de federação limitado; ver DS-SINGULAR-FE-01)
- Regras de negócio no frontend (validações de formato apenas; RN no backend)
- Alteração de contratos API ou código backend

---

# Referências de UI (legado)

| Tela legado | Rota legado | Entrega TO-BE |
|-------------|-------------|---------------|
| `OrganizationIndexPage.vue` | `.../singulares` | Hub administrativo |
| `OrganizationCreatePage.vue` | `.../singulares/novo` | Página de cadastro |
| `OrganizationList.vue` | `.../singulares/lista` | Listagem paginada |
| `OrganizationShowPage.vue` | `.../singulares/:id` | Detalhe |
| `OrganizationEditPage.vue` | `.../singulares/:id/editar` | Edição |

Rotas TO-BE propostas (Vue Router, history mode):

```text
/app/administrador/singulares              → Hub
/app/administrador/singulares/lista        → Listagem
/app/administrador/singulares/novo         → Cadastro
/app/administrador/singulares/:id          → Detalhe
/app/administrador/singulares/:id/editar   → Edição
```

Meta de rota: `requiresAuth: true`, `layout: AdminLayout`, breadcrumbs via `route.meta`.

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| Frontend Foundation (Sprint 0) | `construction/frontend/construction-state.yaml` | ✅ `FEATURE_APPROVED` (PKG-FE-S0-01..10) |
| FT-AUTH frontend (Sprint 1) | `construction/features/FT-AUTH/construction-state.yaml` | ✅ `FEATURE_APPROVED` (`frontend.phase: done`) |
| FT-SINGULAR backend | `construction/features/FT-SINGULAR/construction-state.yaml` | ✅ `FEATURE_APPROVED` |
| API contract | `specs/features/singular/api.md` v1.1.1 | ✅ APPROVED |
| BaseApiClient / HTTP | `frontend/src/services/http/` | ✅ Disponível |
| Auth / Guards | `frontend/src/stores/auth-store.ts`, `router/guards/` | ✅ Disponível |
| Layouts / DS / AppShell | `frontend/src/layouts/`, `components/ds/`, `components/app/` | ✅ Disponível |

---

# Sequência de PKGs

| PKG | Nome | Escopo resumido | Tasks |
|-----|------|-----------------|-------|
| PKG-FE-01 | Singular Module Scaffold & API Client | Types, `singular.service.ts`, composable base, registro de rotas | Base |
| PKG-FE-02 | Create Singular Page | Formulário de cadastro, integração POST | TASK-SINGULAR-FE-001 |
| PKG-FE-03 | List & Detail Pages | Listagem com filtros/paginação, página de detalhe | TASK-SINGULAR-FE-002, FE-003 |
| PKG-FE-04 | Edit Singular Page | Formulário de edição, integração PUT | TASK-SINGULAR-FE-004 |
| PKG-FE-05 | Status Change UI | Toggle/dialog de status, integração PATCH | TASK-SINGULAR-FE-005 |
| PKG-FE-06 | Admin Hub, Tests & Closure | Hub de ações, testes E2E/Vitest, encerramento | Todos AT-FE-* |

Ordem obrigatória: PKG-FE-01 → PKG-FE-02 → … → PKG-FE-06.

---

# Decisões de construção frontend

| ID | Decisão |
|----|---------|
| DS-SINGULAR-FE-01 | Select de federação no cadastro usa `federacaoId` do seed conhecido (DDL `008-initial-data.sql`) até FT-FEDERACAO expor listagem — sem inventar endpoint |
| DS-SINGULAR-FE-02 | Service layer obrigatória (`singular.service.ts`); componentes não chamam Axios diretamente |
| DS-SINGULAR-FE-03 | Estado de listagem via composable + service; Pinia store opcional (preferir composable para listagem paginada) |
| DS-SINGULAR-FE-04 | Status exibido como badge (`ACTIVE`/`INACTIVE`); ação de status com confirmação antes de inativar |
| DS-SINGULAR-FE-05 | Mensagens 422 exibidas via toast/notify usando `ErrorResponse` do envelope corporativo |

---

# Critérios de entrada (Definition of Ready)

| # | Critério | Status |
|---|----------|--------|
| 1 | Frontend Foundation `FEATURE_APPROVED` | ✅ |
| 2 | FT-AUTH frontend `FEATURE_APPROVED` | ✅ |
| 3 | FT-SINGULAR backend `FEATURE_APPROVED` | ✅ |
| 4 | API contract v1.1.1 APPROVED | ✅ |
| 5 | Session congelada (v2) | ✅ |
| 6 | Infraestrutura reutilizável confirmada | ✅ |

**DoR:** satisfeito — ver `reports/state-sync-2026-07-16.md`.

---

# Critérios de saída (Definition of Done)

1. Todos os PKGs (FE-01–FE-06) com `status.md` em **DONE**
2. `construction-state.yaml` com `phase: closed`
3. TASK-SINGULAR-FE-001 a FE-005 concluídas
4. Cenários Must de AT-FE-SINGULAR-001 a 005 validados (Vitest + Playwright)
5. Review, Audit e Readiness aprovados
6. `pnpm lint` + `pnpm test` + build frontend — SUCCESS

---

# Riscos

| Risco | Mitigação |
|-------|-----------|
| Sem API de listagem de federações | DS-SINGULAR-FE-01 — seed até FT-FEDERACAO |
| Rotas admin vs slug legado | Rotas TO-BE `/app/administrador/singulares/*` |
| Matriz RBAC incompleta (OQ-020) | Guards FT-AUTH; 403 via `useStandardErrorHandling` |

**Removidos (pós-sync):** Foundation incompleta, FT-AUTH pendente, mocks/infra paralela em PKG-FE-01.

---

# Status

| Métrica | Valor |
|---------|-------|
| Fase | `closed` |
| Closure | **FEATURE_APPROVED** |
| Session | v2 — State Sync 2026-07-16 |
| DoR | ✅ Satisfeito |
| PKGs concluídos | 6 / 6 |
