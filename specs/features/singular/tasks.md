# Feature Tasks

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

Decomposição funcional da Feature FT-SINGULAR em unidades de implementação rastreáveis.

Este documento não representa planejamento de Construction (PKGs, cronograma).

---

# Convenções

```text
TK-SINGULAR-001
TK-SINGULAR-002
...
```

---

# TK-SINGULAR-001 — Implementar Cadastro de Singular

### Objetivo

Implementar o cadastro de singular via `POST /api/v1/singulares`, incluindo validações de negócio e persistência.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-001

### Casos de Uso Relacionados

- UC-SINGULAR-001

### Critérios de Aceitação Relacionados

- AT-SINGULAR-001

### Dependências

- FT-AUTH (autenticação)
- Federação ativa (seed ou referência mínima)
- Platform Foundation (persistência, validação, exceções)
- `SingularEntity` existente (evoluir de scaffold FT-AREA)

### Componentes Esperados

- `SingularRepository` (evolução)
- `SingularDomainService` (validações RN-SINGULAR-001 a 004)
- `SingularApplicationService`
- `CreateSingularRequest`, `SingularResponse`, `SingularMapper`
- `SingularController`
- Testes de integração e unidade

### Critérios de Conclusão

- RF-SINGULAR-001 implementado
- AT-SINGULAR-001 aprovado
- Rastreabilidade íntegra

---

# TK-SINGULAR-002 — Implementar Consulta por Identificador

### Objetivo

Implementar consulta de singular por `id` via `GET /api/v1/singulares/{id}`.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-002

### Casos de Uso Relacionados

- UC-SINGULAR-002

### Critérios de Aceitação Relacionados

- AT-SINGULAR-002

### Dependências

- TK-SINGULAR-001 (entity e repository base)

### Componentes Esperados

- `SingularApplicationService`
- `SingularController`
- `SingularResponse`, `SingularMapper`
- Testes

### Critérios de Conclusão

- RF-SINGULAR-002 implementado
- AT-SINGULAR-002 aprovado

---

# TK-SINGULAR-003 — Implementar Listagem de Singulares

### Objetivo

Implementar listagem paginada com filtros via `GET /api/v1/singulares`.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-003

### Casos de Uso Relacionados

- UC-SINGULAR-003

### Critérios de Aceitação Relacionados

- AT-SINGULAR-003

### Dependências

- TK-SINGULAR-001

### Componentes Esperados

- `SingularRepository` / Specification para filtros
- `SingularApplicationService`
- `SingularController`
- Integração com `PageResponse`
- Testes

### Critérios de Conclusão

- RF-SINGULAR-003 implementado
- AT-SINGULAR-003 aprovado

---

# TK-SINGULAR-004 — Implementar Atualização de Singular

### Objetivo

Implementar atualização cadastral via `PUT /api/v1/singulares/{id}` com imutabilidade de federação.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-004

### Casos de Uso Relacionados

- UC-SINGULAR-004

### Critérios de Aceitação Relacionados

- AT-SINGULAR-004

### Dependências

- TK-SINGULAR-001

### Componentes Esperados

- `SingularDomainService` (unicidade sigla/código Unimed)
- `UpdateSingularRequest`
- `SingularApplicationService`
- `SingularController`
- Testes

### Critérios de Conclusão

- RF-SINGULAR-004 implementado
- AT-SINGULAR-004 aprovado

---

# TK-SINGULAR-005 — Implementar Alteração de Status

### Objetivo

Implementar ativação/inativação lógica via `PATCH /api/v1/singulares/{id}/status`.

### Requisitos Funcionais Relacionados

- RF-SINGULAR-005

### Casos de Uso Relacionados

- UC-SINGULAR-005

### Critérios de Aceitação Relacionados

- AT-SINGULAR-005

### Dependências

- TK-SINGULAR-001
- Consulta a áreas ativas vinculadas (integração com FT-AREA / `AreaRepository`)

### Componentes Esperados

- `SingularDomainService` (RN-SINGULAR-006)
- `UpdateSingularStatusRequest`
- `SingularApplicationService`
- `SingularController`
- Testes

### Critérios de Conclusão

- RF-SINGULAR-005 implementado
- AT-SINGULAR-005 aprovado

---

# Matriz de Rastreabilidade

| Task | RF | UC | AT |
|------|----|----|----|
| TK-SINGULAR-001 | RF-SINGULAR-001 | UC-SINGULAR-001 | AT-SINGULAR-001 |
| TK-SINGULAR-002 | RF-SINGULAR-002 | UC-SINGULAR-002 | AT-SINGULAR-002 |
| TK-SINGULAR-003 | RF-SINGULAR-003 | UC-SINGULAR-003 | AT-SINGULAR-003 |
| TK-SINGULAR-004 | RF-SINGULAR-004 | UC-SINGULAR-004 | AT-SINGULAR-004 |
| TK-SINGULAR-005 | RF-SINGULAR-005 | UC-SINGULAR-005 | AT-SINGULAR-005 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- todas as tarefas estiverem associadas a RF, UC e AT;
- não representar planejamento de construction;
- mantiver consistência com os demais artefatos da Feature, incluindo `traceability.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | Tasks iniciais FT-SINGULAR |
| 1.1.1 | 2026-07-14 | Specification Engineer | Refinamento Gate 1 — status APPROVED |
