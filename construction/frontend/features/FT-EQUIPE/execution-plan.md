# Execution Plan — FT-EQUIPE Frontend (Equipe)

| Item | Valor |
|------|-------|
| Feature Code | **FT-EQUIPE** |
| Feature Slug | equipe |
| Camada | Frontend |
| Sprint | **3** |
| Status | **Em execução** |
| SSOD | `construction/frontend/features/FT-EQUIPE/feature-manifest.yaml` |
| Construction State | `construction/frontend/features/FT-EQUIPE/construction-state.yaml` |
| Backend Construction | `construction/features/FT-EQUIPE/` (`FEATURE_APPROVED`) |
| Golden Template | FT-SINGULAR (frontend) |
| Versão | 1.0 |
| Última atualização | 2026-07-17 |

---

# Objetivo

Implementar a interface administrativa de **CRUD de Equipes** no Portal de Comunicação, consumindo `/api/v1/equipes` e `GET /api/v1/areas` (select/filtro), alinhada a `specification-frontend.md` e ao padrão FT-SINGULAR.

Consultar `feature-manifest.yaml` (SSOD) antes de qualquer outro artefato.

---

# Escopo

## Inclui

- Módulo `organization/equipe` (types, service, composables, rotas)
- Hub `/app/administrador/equipes`
- Cadastro (`POST /api/v1/equipes`)
- Listagem paginada com filtros (`GET /api/v1/equipes`)
- Detalhe (`GET /api/v1/equipes/{id}`)
- Edição (`PUT /api/v1/equipes/{id}`) — `areaId` somente leitura
- Status (`PATCH /api/v1/equipes/{id}/status`)
- Cliente mínimo de áreas (`GET /api/v1/areas`)
- i18n `equipe.*`, testes Vitest + Playwright (AT-FE-EQUIPE-001..005)

## Não inclui

- Membros, permissões por equipe, documentos por equipe (legado / Features futuras)
- Alteração de contratos API ou backend
- RBAC completo (OQ-020) além do gate administrativo FT-AUTH

---

# Rotas TO-BE

```text
/app/administrador/equipes              → Hub
/app/administrador/equipes/lista        → Listagem
/app/administrador/equipes/novo         → Cadastro
/app/administrador/equipes/:id          → Detalhe
/app/administrador/equipes/:id/editar   → Edição
```

Meta: `requiresAuth: true`, `layout: AdminLayout`, breadcrumbs via `route.meta`.

---

# Dependências

| Dependência | Status |
|-------------|--------|
| Frontend Foundation | ✅ FEATURE_APPROVED |
| FT-AUTH frontend | ✅ FEATURE_APPROVED |
| FT-EQUIPE backend | ✅ FEATURE_APPROVED |
| FT-AREA backend (`/api/v1/areas`) | ✅ FEATURE_APPROVED |
| `specs/features/equipe/` APPROVED | ✅ |
| FT-SINGULAR frontend (referência) | ✅ FEATURE_APPROVED |

---

# Sequência de PKGs

| PKG | Nome | Tasks |
|-----|------|-------|
| PKG-FE-01 | Equipe Module Scaffold & API Client | Base + area client mínimo |
| PKG-FE-02 | Create Equipe Page | TASK-EQUIPE-FE-001 |
| PKG-FE-03 | List & Detail Pages | TASK-EQUIPE-FE-002, FE-003 |
| PKG-FE-04 | Edit Equipe Page | TASK-EQUIPE-FE-004 |
| PKG-FE-05 | Status Change UI | TASK-EQUIPE-FE-005 |
| PKG-FE-06 | Admin Hub, Tests & Closure | AT-FE-EQUIPE-001..005 |

Ordem: PKG-FE-01 → … → PKG-FE-06.

---

# Decisões de construção

| ID | Decisão |
|----|---------|
| DS-EQUIPE-FE-01 | Select/filtro de área via `GET /api/v1/areas` (FT-AREA); cliente em `area.service.ts` ou compartilhado em PKG-FE-01 |
| DS-EQUIPE-FE-02 | `equipe.service.ts` via `BaseApiClient` — sem Axios em componentes |
| DS-EQUIPE-FE-03 | Listagem via `useEquipeList` |
| DS-EQUIPE-FE-04 | Badge de status + confirmação antes de inativar |
| DS-EQUIPE-FE-05 | Erros 422 via envelope corporativo / `useNotify` |
| DS-EQUIPE-FE-06 | Rotas em `router/routes/organization/equipe.routes.ts` |
| DS-EQUIPE-FE-07 | `leaderId` opcional — select de colaboradores pode ser omitido no MVP (API valida RN-EQUIPE-004) |

---

# Definition of Ready

Satisfeito — ver `review/readiness-checklist.md` e `construction-state.yaml` → `definition_of_ready`.

---

# Definition of Done (encerramento)

1. PKG-FE-01..06 com `status.md` **DONE**
2. `construction-state.yaml` → `phase: closed`, `FEATURE_APPROVED`
3. TASK-EQUIPE-FE-001..005 concluídas
4. AT-FE-EQUIPE-001..005 (Playwright)
5. Review, Audit, Readiness frontend
6. `yarn lint` + `yarn test` + build — SUCCESS

---

# Próximo passo

**PKG-FE-01** — scaffold módulo equipe + service + rotas stub + i18n.
