# Readiness Checklist — FT-EQUIPE (Frontend)

| Item | Valor |
|------|-------|
| Feature Code | FT-EQUIPE |
| Specification | `specification.md` v1.1 + `specification-frontend.md` v1.0 |
| Escopo avaliado | **Workstream Frontend — CRUD administrativo MVP** |
| Tipo | Pré-Construction (`Readiness` / CMD-07) |
| Status | **PRONTA** |
| Data | 2026-07-17 |
| Executor | auditor |

---

# Gate de entrada

| Critério | Evidência | Status |
|----------|-----------|--------|
| `feature.yaml` → `status.specification: APPROVED` | `specs/features/equipe/feature.yaml` | ✅ |
| Gate 1 concluído | `specs/features/equipe/review-report.md` | ✅ APPROVED WITH MINOR ISSUES |

---

# Definition of Ready (specs)

| ID | Critério DoR | Evidência | Status |
|----|--------------|-----------|--------|
| DOR-01 | Objetivo definido | `specification-frontend.md` | ✅ |
| DOR-02 | Escopo delimitado | Incluído / fora do escopo (MVP vs legado) | ✅ |
| DOR-03 | Regras de negócio documentadas | `specification.md` RN-EQUIPE-001..007 | ✅ |
| DOR-04 | Casos de uso definidos | `use-cases.md` UC-EQUIPE-001..005 | ✅* |
| DOR-05 | Contrato API | `api.md` + consumo frontend | ✅ |
| DOR-06 | Rastreabilidade | `traceability.md` (backend + RF-FE) | ✅ |
| DOR-07 | Critérios de aceite | `acceptance-tests.md` AT + AT-FE | ✅ |
| DOR-08 | Dependências identificadas | `specification-frontend.md` | ✅ |
| DOR-09 | Impactos conhecidos | Frontend only; sem mudança de API/DB | ✅ |
| DOR-10 | Tarefas identificadas | `tasks.md` TK-EQUIPE-FE-001..005 | ✅ |

\* Ressalva **NC-01** (review): `use-cases.md` compacto — aceito; não bloqueia (mesmo critério do encerramento backend).

---

# Dependências de Construction

| ID | Dependência | Estado esperado | Evidência | Status |
|----|-------------|-----------------|-----------|--------|
| DEP-01 | Platform Foundation | FEATURE_APPROVED | `construction/registry.yaml` → platform-foundation | ✅ |
| DEP-02 | Frontend Foundation | FEATURE_APPROVED | `construction/registry.yaml` → frontend-foundation | ✅ |
| DEP-03 | FT-AUTH | FEATURE_APPROVED | `construction/features/FT-AUTH/construction-state.yaml` | ✅ |
| DEP-04 | FT-EQUIPE backend | FEATURE_APPROVED | `construction/features/FT-EQUIPE/construction-state.yaml` | ✅ |
| DEP-05 | FT-AREA backend (`GET /api/v1/areas`) | FEATURE_APPROVED | `construction/features/FT-AREA/construction-state.yaml`, `AreaController` | ✅ |
| DEP-06 | FT-COLABORADOR backend (líder opcional) | FEATURE_APPROVED | `construction/registry.yaml` → FT-COLABORADOR closed | ✅ |
| DEP-07 | API integrada (Sprint org) | APPROVED | `engineering/integration/sprints/sprint-03-org-backend/integration-report.md` | ✅ |
| DEP-08 | Golden template Full Stack | Referência | `construction/golden-template/FT-SINGULAR.md`, FE closed | ✅ |

---

# Prontidão técnica (frontend)

| ID | Critério | Evidência | Status |
|----|----------|-----------|--------|
| RR-FE-EQUIPE-01 | Padrão de rotas admin (referência) | `frontend/src/constants/routes.ts` (SINGULAR_*), `singular.routes.ts` | ✅ |
| RR-FE-EQUIPE-02 | Auth + mocks E2E reutilizáveis | `frontend/test/e2e/support/auth-mock.ts`, `singular-api-mock.ts` (padrão) | ✅ |
| RR-FE-EQUIPE-03 | Contrato `/api/v1/equipes` estável | `EquipeController`, specs `api.md` | ✅ |
| RR-FE-EQUIPE-04 | PKG-FE-01..06 definidos em spec | `tasks.md` matriz PKG-FE | ✅ |
| RR-FE-EQUIPE-05 | Workstream registrado no registry | `construction/registry.yaml` workstream frontend | ✅ |
| RR-FE-EQUIPE-06 | Pasta `construction/frontend/features/FT-EQUIPE/` | manifest, state, session, PKG-FE-01..06 | ✅ |
| RR-FE-EQUIPE-07 | Cliente HTTP áreas na UI | Sem `area.service` dedicado ainda | ⚠️ **Planejado PKG-FE-01** (não bloqueia Readiness) |
| RR-FE-EQUIPE-08 | Select colaborador (líder) | Opcional NC-02 | ⚠️ **Planejado PKG-FE-02/04** (não bloqueia Readiness) |

Itens RR-FE-EQUIPE-05/06 são **artefatos de bootstrap** do comando `Execute Feature`, não pré-requisitos de especificação.

---

# Bloqueadores

| ID | Descrição | Bloqueia? |
|----|-----------|-----------|
| — | Nenhum bloqueador identificado | — |

---

# Resultado

| Métrica | Valor |
|---------|-------|
| DoR obrigatório | 10 / 10 |
| Dependências FEATURE_APPROVED | 8 / 8 |
| Bloqueadores | 0 |

**Prontidão:** ✅ **PRONTA** para **`Execute Feature FT-EQUIPE`** (workstream **frontend**), seguindo golden template FT-SINGULAR (`pkg-fe-01` … `pkg-fe-06`).

---

# Ações imediatas recomendadas

1. ~~`Execute Feature FT-EQUIPE`~~ — **concluído 2026-07-17**
2. **PKG-FE-01** — types, `equipe.service.ts`, rotas, i18n, cliente mínimo de áreas.
3. Reutilizar padrões de `construction/frontend/features/FT-SINGULAR/` (não duplicar spec em construction além de `frontend-tasks.md` espelhado se necessário).

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.0 | 2026-07-17 | Readiness pré-Construction frontend |
