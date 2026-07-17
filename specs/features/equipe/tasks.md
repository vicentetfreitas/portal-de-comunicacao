# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-EQUIPE |
| Feature | Equipe |
| Domínio | EQUIPE |

---

# Objetivo

Decomposição funcional da Feature FT-EQUIPE em unidades de implementação rastreáveis, incluindo backend (concluído) e camada frontend administrativa.

Este documento não representa planejamento de Construction (PKGs, cronograma). Mapeamento PKG-FE previsto em `specification-frontend.md` e espelhado em FT-SINGULAR (`construction/frontend/features/FT-SINGULAR/frontend-tasks.md`).

---

# Convenções

```text
TK-EQUIPE-001        — backend
TK-EQUIPE-FE-001     — frontend
AT-FE-EQUIPE-001     — aceite E2E frontend
```

---

# Backend — Concluído

## TK-EQUIPE-001 — Implementar Cadastro de Equipe

### Objetivo

Implementar cadastro via `POST /api/v1/equipes`.

### Rastreabilidade

- RF-EQUIPE-001, UC-EQUIPE-001, AT-EQUIPE-001

### Status

**COMPLETE** (Construction FT-EQUIPE backend)

---

## TK-EQUIPE-002 — Implementar Consulta por Identificador

### Objetivo

`GET /api/v1/equipes/{id}`.

### Rastreabilidade

- RF-EQUIPE-002, UC-EQUIPE-002, AT-EQUIPE-002

### Status

**COMPLETE**

---

## TK-EQUIPE-003 — Implementar Listagem de Equipes

### Objetivo

`GET /api/v1/equipes` com paginação e filtros.

### Rastreabilidade

- RF-EQUIPE-003, UC-EQUIPE-003, AT-EQUIPE-003

### Status

**COMPLETE**

---

## TK-EQUIPE-004 — Implementar Atualização de Equipe

### Objetivo

`PUT /api/v1/equipes/{id}` sem alteração de `areaId`.

### Rastreabilidade

- RF-EQUIPE-004, UC-EQUIPE-004, AT-EQUIPE-004

### Status

**COMPLETE**

---

## TK-EQUIPE-005 — Implementar Alteração de Status

### Objetivo

`PATCH /api/v1/equipes/{id}/status`.

### Rastreabilidade

- RF-EQUIPE-005, UC-EQUIPE-005, AT-EQUIPE-005

### Status

**COMPLETE**

---

# Frontend — Pendente de implementação

## TK-EQUIPE-FE-001 — Página de Cadastro de Equipe

### Objetivo

Formulário de cadastro consumindo `POST /api/v1/equipes`.

### Requisitos Funcionais Relacionados

- RF-FE-EQUIPE-001, RF-EQUIPE-001

### Critérios de Aceitação Relacionados

- AT-FE-EQUIPE-001

### Dependências

- FT-AUTH, Frontend Foundation
- Cliente HTTP equipe (PKG-FE-01)
- Select de área (`GET /api/v1/areas`)

### Componentes Esperados (indicativos)

- `pages/organization/equipe/EquipeCreatePage.vue`
- `components/organization/equipe/EquipeForm.vue`
- `composables/useEquipeForm.ts`

### Critérios de Conclusão

- Rota `/app/administrador/equipes/novo`
- Submit válido redireciona para detalhe ou listagem com feedback de sucesso
- Erros 422/403/400 exibidos ao usuário

---

## TK-EQUIPE-FE-002 — Página de Detalhe de Equipe

### Objetivo

Exibir equipe via `GET /api/v1/equipes/{id}`.

### Requisitos Funcionais Relacionados

- RF-FE-EQUIPE-002, RF-EQUIPE-002

### Critérios de Aceitação Relacionados

- AT-FE-EQUIPE-002

### Componentes Esperados

- `pages/organization/equipe/EquipeDetailPage.vue`
- `components/organization/equipe/EquipeInfoCard.vue`

### Critérios de Conclusão

- Rota `/app/administrador/equipes/:id`
- 404 com estado amigável
- Links para editar e alterar status

---

## TK-EQUIPE-FE-003 — Página de Listagem de Equipes

### Objetivo

Listagem com paginação e filtros via `GET /api/v1/equipes`.

### Requisitos Funcionais Relacionados

- RF-FE-EQUIPE-003, RF-EQUIPE-003

### Critérios de Aceitação Relacionados

- AT-FE-EQUIPE-003

### Componentes Esperados

- `pages/organization/equipe/EquipeListPage.vue`
- `components/organization/equipe/EquipeFilters.vue`
- `composables/useEquipeList.ts`

### Critérios de Conclusão

- Filtros: `status`, `areaId`, `name`
- Paginação e ordenação conforme API
- Estado vazio adequado

---

## TK-EQUIPE-FE-004 — Página de Edição de Equipe

### Objetivo

Atualização via `PUT /api/v1/equipes/{id}`.

### Requisitos Funcionais Relacionados

- RF-FE-EQUIPE-004, RF-EQUIPE-004

### Critérios de Aceitação Relacionados

- AT-FE-EQUIPE-004

### Dependências

- TK-EQUIPE-FE-001 (formulário reutilizável)
- TK-EQUIPE-FE-002 (carga de dados)

### Critérios de Conclusão

- `areaId` somente leitura (RN-EQUIPE-007)
- Erros 422/403/404 tratados

---

## TK-EQUIPE-FE-005 — Alteração de Status na UI

### Objetivo

Ativação/inativação via `PATCH /api/v1/equipes/{id}/status`.

### Requisitos Funcionais Relacionados

- RF-FE-EQUIPE-005, RF-EQUIPE-005

### Critérios de Aceitação Relacionados

- AT-FE-EQUIPE-005

### Componentes Esperados

- `components/organization/equipe/EquipeStatusDialog.vue`

### Critérios de Conclusão

- Confirmação antes de inativar
- Badge de status atualizado após sucesso
- Mensagem clara para bloqueio RN-EQUIPE-006

---

# Matriz PKG-FE × Task (Construction)

| PKG | Tasks |
|-----|-------|
| PKG-FE-01 | Base (types, service, routes, i18n) |
| PKG-FE-02 | TK-EQUIPE-FE-001 |
| PKG-FE-03 | TK-EQUIPE-FE-002, TK-EQUIPE-FE-003 |
| PKG-FE-04 | TK-EQUIPE-FE-004 |
| PKG-FE-05 | TK-EQUIPE-FE-005 |
| PKG-FE-06 | AT-FE-EQUIPE-001..005, hub, encerramento |

---

# Matriz de Rastreabilidade

| Task | RF / RF-FE | UC | AT / AT-FE | Status |
|------|------------|-----|------------|--------|
| TK-EQUIPE-001 | RF-EQUIPE-001 | UC-EQUIPE-001 | AT-EQUIPE-001 | COMPLETE |
| TK-EQUIPE-002 | RF-EQUIPE-002 | UC-EQUIPE-002 | AT-EQUIPE-002 | COMPLETE |
| TK-EQUIPE-003 | RF-EQUIPE-003 | UC-EQUIPE-003 | AT-EQUIPE-003 | COMPLETE |
| TK-EQUIPE-004 | RF-EQUIPE-004 | UC-EQUIPE-004 | AT-EQUIPE-004 | COMPLETE |
| TK-EQUIPE-005 | RF-EQUIPE-005 | UC-EQUIPE-005 | AT-EQUIPE-005 | COMPLETE |
| TK-EQUIPE-FE-001 | RF-FE-EQUIPE-001 | UC-EQUIPE-001 | AT-FE-EQUIPE-001 | DEFINED |
| TK-EQUIPE-FE-002 | RF-FE-EQUIPE-002 | UC-EQUIPE-002 | AT-FE-EQUIPE-002 | DEFINED |
| TK-EQUIPE-FE-003 | RF-FE-EQUIPE-003 | UC-EQUIPE-003 | AT-FE-EQUIPE-003 | DEFINED |
| TK-EQUIPE-FE-004 | RF-FE-EQUIPE-004 | UC-EQUIPE-004 | AT-FE-EQUIPE-004 | DEFINED |
| TK-EQUIPE-FE-005 | RF-FE-EQUIPE-005 | UC-EQUIPE-005 | AT-FE-EQUIPE-005 | DEFINED |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-17 | Specification Engineer | Tasks backend + frontend FT-EQUIPE |
