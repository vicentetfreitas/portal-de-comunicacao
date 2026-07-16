# Frontend Tasks — FT-SINGULAR

| Campo | Valor |
|--------|--------|
| Feature ID | FT-SINGULAR |
| Camada | Frontend Construction |
| Versão | 1.0 |
| Status | READY |
| Owner | construction-orchestrator |

---

# Objetivo

Backlog técnico frontend da Feature FT-SINGULAR. Complementa `specs/features/singular/tasks.md` (backend).

Rastreabilidade: RF/AT backend → UI frontend → PKG.

---

# Convenções

```text
TASK-SINGULAR-FE-001
TASK-SINGULAR-FE-002
...
AT-FE-SINGULAR-001  (critérios de aceite frontend)
```

---

# TASK-SINGULAR-FE-001 — Página de Cadastro de Singular

### Objetivo

Implementar fluxo de cadastro via formulário consumindo `POST /api/v1/singulares`.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-001

### Critérios de Aceitação Relacionados

- AT-SINGULAR-001 → **AT-FE-SINGULAR-001**

### Dependências

- PKG-FE-01 (service, types)
- FT-AUTH (sessão autenticada, CSRF)
- Frontend Foundation (forms, DS)

### Componentes Esperados

- `pages/organization/singular/SingularCreatePage.vue`
- `components/organization/singular/SingularForm.vue`
- `components/organization/singular/SingularBasicInfoSection.vue`
- `composables/useSingularForm.ts`

### Critérios de Conclusão

- Administrador autenticado acessa `/app/administrador/singulares/novo`
- Submit válido redireciona para detalhe ou listagem com toast de sucesso
- Erros 422/403/400 exibidos ao usuário
- Campos: `federacaoId`, `name`, `acronym`, `codigoUnimed`

---

# TASK-SINGULAR-FE-002 — Página de Detalhe de Singular

### Objetivo

Exibir dados cadastrais de uma singular via `GET /api/v1/singulares/{id}`.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-002

### Critérios de Aceitação Relacionados

- AT-SINGULAR-002 → **AT-FE-SINGULAR-002**

### Dependências

- PKG-FE-01

### Componentes Esperados

- `pages/organization/singular/SingularDetailPage.vue`
- `components/organization/singular/SingularInfoCard.vue`

### Critérios de Conclusão

- Rota `/app/administrador/singulares/:id` exibe dados da API
- 404 redireciona ou exibe estado vazio/erro amigável
- Ações: editar, alterar status (links para PKG-FE-04/05)

---

# TASK-SINGULAR-FE-003 — Página de Listagem de Singulares

### Objetivo

Listar singulares com paginação, ordenação e filtros via `GET /api/v1/singulares`.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-003

### Critérios de Aceitação Relacionados

- AT-SINGULAR-003 → **AT-FE-SINGULAR-003**

### Dependências

- PKG-FE-01
- DS DataTable base (Foundation)

### Componentes Esperados

- `pages/organization/singular/SingularListPage.vue`
- `components/organization/singular/SingularFilters.vue`
- `composables/useSingularList.ts`

### Critérios de Conclusão

- Filtros: `status`, `name`, `acronym`, `codigoUnimed`, `federacaoId`
- Paginação e sort conforme API
- Link para detalhe e ação de criar nova singular
- Listagem vazia com estado visual adequado

---

# TASK-SINGULAR-FE-004 — Página de Edição de Singular

### Objetivo

Atualizar dados cadastrais via `PUT /api/v1/singulares/{id}`.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-004

### Critérios de Aceitação Relacionados

- AT-SINGULAR-004 → **AT-FE-SINGULAR-004**

### Dependências

- TASK-SINGULAR-FE-001 (formulário reutilizável)
- TASK-SINGULAR-FE-002 (carregar dados existentes)

### Componentes Esperados

- `pages/organization/singular/SingularEditPage.vue`
- Reuso de `SingularForm.vue` (modo edit)

### Critérios de Conclusão

- `federacaoId` exibido como somente leitura (RN-SINGULAR-007)
- Submit válido atualiza e exibe confirmação
- Erros 422/403/404 tratados

---

# TASK-SINGULAR-FE-005 — Alteração de Status na UI

### Objetivo

Permitir ativação/inativação via `PATCH /api/v1/singulares/{id}/status`.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-005

### Critérios de Aceitação Relacionados

- AT-SINGULAR-005 → **AT-FE-SINGULAR-005**

### Dependências

- TASK-SINGULAR-FE-002

### Componentes Esperados

- `components/organization/singular/SingularStatusDialog.vue`
- Integração na `SingularDetailPage.vue`

### Critérios de Conclusão

- Confirmação obrigatória antes de inativar
- Badge de status atualizado após sucesso
- Erro 422 (áreas ativas) exibido com mensagem clara

---

# Matriz PKG × Task

| PKG | Tasks |
|-----|-------|
| PKG-FE-01 | Base (types, service, routes) |
| PKG-FE-02 | TASK-SINGULAR-FE-001 |
| PKG-FE-03 | TASK-SINGULAR-FE-002, TASK-SINGULAR-FE-003 |
| PKG-FE-04 | TASK-SINGULAR-FE-004 |
| PKG-FE-05 | TASK-SINGULAR-FE-005 |
| PKG-FE-06 | AT-FE-SINGULAR-001..005, hub, encerramento |

---

# Critérios de Aceite Frontend (AT-FE)

| ID | Descrição | AT Backend |
|----|-----------|------------|
| AT-FE-SINGULAR-001 | E2E cadastro happy path + erro 422 sigla duplicada | AT-SINGULAR-001 |
| AT-FE-SINGULAR-002 | E2E consulta detalhe + 404 | AT-SINGULAR-002 |
| AT-FE-SINGULAR-003 | E2E listagem com filtro status + paginação | AT-SINGULAR-003 |
| AT-FE-SINGULAR-004 | E2E edição happy path + sigla duplicada | AT-SINGULAR-004 |
| AT-FE-SINGULAR-005 | E2E inativação + bloqueio 422 | AT-SINGULAR-005 |

---

# Histórico

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-16 | construction-orchestrator | Backlog frontend inicial |
