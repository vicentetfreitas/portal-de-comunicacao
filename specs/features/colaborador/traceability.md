# Traceability — FT-COLABORADOR

# Backend (API)

| RF | RN | UC | API | AT | TK | Status |
|----|----|----|-----|----|----|--------|
| RF-COLABORADOR-001 | RN-001 a 006 | UC-001 | POST /api/v1/colaboradores | AT-001 | TK-001 | COMPLETE |
| RF-COLABORADOR-002 | — | UC-002 | GET /api/v1/colaboradores/{id} | AT-002 | TK-002 | COMPLETE |
| RF-COLABORADOR-003 | — | UC-003 | GET /api/v1/colaboradores | AT-003 | TK-003 | COMPLETE |
| RF-COLABORADOR-004 | RN-005,006,009 | UC-004 | PUT /api/v1/colaboradores/{id} | AT-004 | TK-004 | COMPLETE |
| RF-COLABORADOR-005 | RN-007,008 | UC-005 | PATCH /api/v1/colaboradores/{id}/status | AT-005 | TK-005 | COMPLETE |

---

# Frontend (Administrativo)

| RF-FE | RF | UC | Rota principal | AT-FE | TK | Status |
|-------|-----|-----|----------------|-------|----|--------|
| RF-FE-COLABORADOR-001 | RF-COLABORADOR-001 | UC-001 | `/app/administrador/colaboradores/novo` | AT-FE-COLABORADOR-001 | TK-001 | COMPLETE |
| RF-FE-COLABORADOR-002 | RF-COLABORADOR-002 | UC-002 | `/app/administrador/colaboradores/:id` | AT-FE-COLABORADOR-002 | TK-002 | COMPLETE |
| RF-FE-COLABORADOR-003 | RF-COLABORADOR-003 | UC-003 | `/app/administrador/colaboradores/lista` | AT-FE-COLABORADOR-003 | TK-003 | COMPLETE |
| RF-FE-COLABORADOR-004 | RF-COLABORADOR-004 | UC-004 | `/app/administrador/colaboradores/:id/editar` | AT-FE-COLABORADOR-004 | TK-004 | COMPLETE |
| RF-FE-COLABORADOR-005 | RF-COLABORADOR-005 | UC-005 | (diálogo no detalhe) | AT-FE-COLABORADOR-005 | TK-005 | COMPLETE |

Evidência AT-FE: `frontend/test/e2e/colaborador/colaborador.spec.ts` (mock-store dedicado `support/colaborador-api-mock.ts`).
