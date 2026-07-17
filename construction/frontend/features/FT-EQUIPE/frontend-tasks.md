# Frontend Tasks — FT-EQUIPE

| Campo | Valor |
|--------|--------|
| Feature ID | FT-EQUIPE |
| Camada | Frontend Construction |
| Versão | 1.0 |
| Status | READY |
| Owner | construction-orchestrator |
| Spec SSOT | `specs/features/equipe/tasks.md` (TK-EQUIPE-FE-*) |

---

# Objetivo

Backlog técnico frontend. Rastreabilidade: RF-FE → AT-FE → PKG.

---

# TASK-EQUIPE-FE-001 — Página de Cadastro

- **RF:** RF-FE-EQUIPE-001 / RF-EQUIPE-001
- **AT:** AT-FE-EQUIPE-001
- **PKG:** PKG-FE-02 (após FE-01)
- **Rota:** `/app/administrador/equipes/novo`
- **Campos:** `areaId`, `name`, `description?`, `leaderId?`

---

# TASK-EQUIPE-FE-002 — Página de Detalhe

- **RF:** RF-FE-EQUIPE-002
- **AT:** AT-FE-EQUIPE-002
- **PKG:** PKG-FE-03
- **Rota:** `/app/administrador/equipes/:id`

---

# TASK-EQUIPE-FE-003 — Página de Listagem

- **RF:** RF-FE-EQUIPE-003
- **AT:** AT-FE-EQUIPE-003
- **PKG:** PKG-FE-03
- **Filtros:** `status`, `areaId`, `name`

---

# TASK-EQUIPE-FE-004 — Página de Edição

- **RF:** RF-FE-EQUIPE-004
- **AT:** AT-FE-EQUIPE-004
- **PKG:** PKG-FE-04
- **RN-EQUIPE-007:** `areaId` somente leitura

---

# TASK-EQUIPE-FE-005 — Status na UI

- **RF:** RF-FE-EQUIPE-005
- **AT:** AT-FE-EQUIPE-005
- **PKG:** PKG-FE-05
- **Componente:** `EquipeStatusDialog.vue`

---

# Matriz PKG × Task

| PKG | Tasks |
|-----|-------|
| PKG-FE-01 | Base (types, equipe + area client, routes, i18n stubs) |
| PKG-FE-02 | TASK-EQUIPE-FE-001 |
| PKG-FE-03 | TASK-EQUIPE-FE-002, TASK-EQUIPE-FE-003 |
| PKG-FE-04 | TASK-EQUIPE-FE-004 |
| PKG-FE-05 | TASK-EQUIPE-FE-005 |
| PKG-FE-06 | AT-FE-EQUIPE-001..005, hub, encerramento |

---

# AT-FE (resumo)

| ID | AT Backend |
|----|------------|
| AT-FE-EQUIPE-001 | AT-EQUIPE-001 |
| AT-FE-EQUIPE-002 | AT-EQUIPE-002 |
| AT-FE-EQUIPE-003 | AT-EQUIPE-003 |
| AT-FE-EQUIPE-004 | AT-EQUIPE-004 |
| AT-FE-EQUIPE-005 | AT-EQUIPE-005 |

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.0 | 2026-07-17 | Bootstrap Execute Feature |
