# Traceability — FT-EQUIPE

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Objetivo

Consolidar rastreabilidade backend e frontend da Feature FT-EQUIPE.

Detalhamento: `specification.md`, `specification-frontend.md`, `use-cases.md`, `api.md`, `acceptance-tests.md`, `tasks.md`.

---

# Backend (API)

| RF | RN | UC | API | AT | TK | Status |
|----|----|----|-----|----|----|--------|
| RF-EQUIPE-001 | RN-EQUIPE-001 a 004 | UC-EQUIPE-001 | POST /api/v1/equipes | AT-EQUIPE-001 | TK-EQUIPE-001 | COMPLETE |
| RF-EQUIPE-002 | — | UC-EQUIPE-002 | GET /api/v1/equipes/{id} | AT-EQUIPE-002 | TK-EQUIPE-002 | COMPLETE |
| RF-EQUIPE-003 | — | UC-EQUIPE-003 | GET /api/v1/equipes | AT-EQUIPE-003 | TK-EQUIPE-003 | COMPLETE |
| RF-EQUIPE-004 | RN-EQUIPE-001, 002-004, 007 | UC-EQUIPE-004 | PUT /api/v1/equipes/{id} | AT-EQUIPE-004 | TK-EQUIPE-004 | COMPLETE |
| RF-EQUIPE-005 | RN-EQUIPE-005, 006 | UC-EQUIPE-005 | PATCH /api/v1/equipes/{id}/status | AT-EQUIPE-005 | TK-EQUIPE-005 | COMPLETE |

---

# Frontend (Administrativo)

| RF-FE | RF | UC | Rota principal | AT-FE | TK-FE | Status |
|-------|-----|-----|----------------|-------|-------|--------|
| RF-FE-EQUIPE-001 | RF-EQUIPE-001 | UC-EQUIPE-001 | `/app/administrador/equipes/novo` | AT-FE-EQUIPE-001 | TK-EQUIPE-FE-001 | DEFINED |
| RF-FE-EQUIPE-002 | RF-EQUIPE-002 | UC-EQUIPE-002 | `/app/administrador/equipes/:id` | AT-FE-EQUIPE-002 | TK-EQUIPE-FE-002 | DEFINED |
| RF-FE-EQUIPE-003 | RF-EQUIPE-003 | UC-EQUIPE-003 | `/app/administrador/equipes/lista` | AT-FE-EQUIPE-003 | TK-EQUIPE-FE-003 | DEFINED |
| RF-FE-EQUIPE-004 | RF-EQUIPE-004 | UC-EQUIPE-004 | `/app/administrador/equipes/:id/editar` | AT-FE-EQUIPE-004 | TK-EQUIPE-FE-004 | DEFINED |
| RF-FE-EQUIPE-005 | RF-EQUIPE-005 | UC-EQUIPE-005 | (diálogo no detalhe) | AT-FE-EQUIPE-005 | TK-EQUIPE-FE-005 | DEFINED |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| RF backend | 5 | 5 | 0 |
| RF-FE | 5 | 5 | 0 |
| UC | 5 | 5 | 0 |
| Endpoints | 5 | 5 | 0 |
| AT backend | 5 | 5 | 0 |
| AT-FE | 5 | 5 | 0 |
| TK backend | 5 | 5 (COMPLETE) | 0 |
| TK-FE | 5 | 5 (DEFINED) | 5 impl. |

---

# Validações

- [x] Cada RF-FE mapeia para RF, UC, AT-FE e TK-FE
- [x] Cada AT-FE referencia AT backend correspondente
- [x] Backend traceability mantida após adição do workstream frontend
- [x] `tasks.md` e `acceptance-tests.md` consistentes com esta matriz

---

# Histórico

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | Rastreabilidade backend |
| 1.1 | 2026-07-17 | Specification Reviewer | Gate 1 — status APPROVED |
