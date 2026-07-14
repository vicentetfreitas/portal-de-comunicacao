# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-SINGULAR |
| Feature | Singular |
| Domínio | SINGULAR |

---

# Objetivo

Este documento consolida a rastreabilidade completa da Feature FT-SINGULAR.

Responsabilidade exclusiva: relacionar Requisitos, Regras de Negócio, Casos de Uso, API, Acceptance Tests e Tasks em uma visão única.

Detalhamento funcional permanece em `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `tasks.md`.

---

# Escopo da Cadeia

```text
RF
 ↓
RN
 ↓
UC
 ↓
API
 ↓
AT
 ↓
TK
```

---

# Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK | Status |
|----|----|----|-----|----|----|--------|
| RF-SINGULAR-001 | RN-SINGULAR-001 a 004 | UC-SINGULAR-001 | POST /api/v1/singulares | AT-SINGULAR-001 | TK-SINGULAR-001 | DEFINED |
| RF-SINGULAR-002 | — | UC-SINGULAR-002 | GET /api/v1/singulares/{id} | AT-SINGULAR-002 | TK-SINGULAR-002 | DEFINED |
| RF-SINGULAR-003 | — | UC-SINGULAR-003 | GET /api/v1/singulares | AT-SINGULAR-003 | TK-SINGULAR-003 | DEFINED |
| RF-SINGULAR-004 | RN-SINGULAR-001, 002 a 004, 007 | UC-SINGULAR-004 | PUT /api/v1/singulares/{id} | AT-SINGULAR-004 | TK-SINGULAR-004 | DEFINED |
| RF-SINGULAR-005 | RN-SINGULAR-005, 006 | UC-SINGULAR-005 | PATCH /api/v1/singulares/{id}/status | AT-SINGULAR-005 | TK-SINGULAR-005 | DEFINED |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 5 | 5 | 0 |
| Regras de Negócio | 7 | 7 | 0 |
| Casos de Uso | 5 | 5 | 0 |
| Endpoints | 5 | 5 | 0 |
| Acceptance Tests | 5 | 5 | 0 |
| Tasks | 5 | 5 | 0 |

---

# Validações Obrigatórias

- [x] Todos os RF possuem UC, API, AT e TK
- [x] Todos os AT possuem RF associado
- [x] Todas as TK possuem RF associado
- [x] Nenhum endpoint sem justificativa funcional (RF)
- [x] Matriz consistente com `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `tasks.md`

---

# Critérios de Conclusão

**Traceability Status:** `COMPLETE`

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | Rastreabilidade consolidada FT-SINGULAR |
| 1.1.1 | 2026-07-14 | Specification Engineer | Refinamento Gate 1 — status APPROVED |
