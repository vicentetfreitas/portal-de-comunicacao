# Package Index — Frontend Foundation (Sprint 0)

| Item | Valor |
|------|-------|
| Sprint | Sprint 0 — Frontend Foundation |
| Versão | 1.0 |
| Data | 2026-07-15 |
| Fonte | `docs/construction/frontend/00-frontend-foundation.md` §8 |

---

## Índice de Packages

| # | Identifier | Nome | Pasta | Dependências | Status |
|---|------------|------|-------|--------------|--------|
| 1 | PKG-FE-S0-01 | Project Bootstrap | `pkg-fe-s0-01/` | Backend Sprint 0; Docker Compose | **Done** |
| 2 | PKG-FE-S0-02 | Theme | `pkg-fe-s0-02/` | PKG-FE-S0-01 | **Done** |
| 3 | PKG-FE-S0-03 | Design System | `pkg-fe-s0-03/` | PKG-FE-S0-02 | **Done** |
| 4 | PKG-FE-S0-04 | Layouts | `pkg-fe-s0-04/` | PKG-FE-S0-02, PKG-FE-S0-03 | Pending |
| 5 | PKG-FE-S0-05 | Routing | `pkg-fe-s0-05/` | PKG-FE-S0-04 | Pending |
| 6 | PKG-FE-S0-06 | HTTP Client | `pkg-fe-s0-06/` | PKG-FE-S0-01 | Pending |
| 7 | PKG-FE-S0-07 | Authentication Integration | `pkg-fe-s0-07/` | PKG-FE-S0-05, PKG-FE-S0-06 | Pending |
| 8 | PKG-FE-S0-08 | Shared Components | `pkg-fe-s0-08/` | PKG-FE-S0-03, PKG-FE-S0-06 | Pending |
| 9 | PKG-FE-S0-09 | Testing Infrastructure | `pkg-fe-s0-09/` | PKG-FE-S0-01, PKG-FE-S0-05 | Pending |

---

## Deliverables por Package

### PKG-FE-S0-01 — Project Bootstrap

- Projeto Quasar/Vue 3/TypeScript compilável em `frontend/`
- Diretórios oficiais: `pages`, `layouts`, `components`, `services`, `stores`, `router`
- Configuração multi-ambiente externa
- Pinia, i18n base (`pt-BR`), lint/format

### PKG-FE-S0-02 — Theme

- Design tokens Unimed
- `quasar.variables.scss`
- Fontes corporativas, light/dark, MDI (`mdi-v7`)

### PKG-FE-S0-03 — Design System

- `components/ds/` (atoms, molecules, organisms)
- Página showcase

### PKG-FE-S0-04 — Layouts

- `AuthLayout`, `MainLayout`, `AdminLayout`, `PublicLayout`
- `AppHeader`, `AppSidebar`, `AppFooter`

### PKG-FE-S0-05 — Routing

- Vue Router history mode
- 404, redirects `/` e `/app`
- Guards scaffolding, meta conventions

### PKG-FE-S0-06 — HTTP Client

- Axios `/api/v1`, `withCredentials`
- CSRF, envelopes `ApiResponse`/`ErrorResponse`
- Interceptors request/response

### PKG-FE-S0-07 — Authentication Integration

- Auth store Pinia (estrutural)
- Política anti-localStorage (RN-AUTH-007)
- Hooks de refresh e guard scaffolding

### PKG-FE-S0-08 — Shared Components

- Toast/notify, loading, error handling
- Composables: `useLoading`, `useStandardErrorHandling`, `useTheme`

### PKG-FE-S0-09 — Testing Infrastructure

- Vitest + Playwright
- Testes de fumaça
- Integração CI

---

## Critérios de Aceite

Ver `00-frontend-foundation.md` §9 — AC-FE-S0-001 a AC-FE-S0-020.

## Definition of Done

Ver `00-frontend-foundation.md` §10.
