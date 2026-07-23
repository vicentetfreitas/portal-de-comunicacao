# Acceptance Tests

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
| Feature ID | FT-AREA |
| Feature | Área |
| Domínio | AREA |

---

# Objetivo

Este documento define os critérios de aceitação verificáveis da Feature FT-AREA.

Estratégia de testes, ferramentas e cobertura: `docs/implementation/08-testing-strategy.md`.

---

# Convenções

```text
AT-AREA-001
AT-AREA-002
...
```

---

# AT-AREA-001 — Criar Área

### Objetivo

Validar cadastro de área com sucesso e rejeição de violações de negócio.

### Prioridade

Must

### Tipo

Happy Path / Negative / Business Rule / Authorization

### Requisitos Funcionais Relacionados

- RF-AREA-001

### Casos de Uso Relacionados

- UC-AREA-001

### Regras de Negócio Relacionadas

- RN-AREA-001, RN-AREA-002, RN-AREA-003, RN-AREA-006

### Pré-condições

- Usuário administrador autenticado
- Singular ativa existente no banco

### Cenário — Happy Path

#### Given

Singular `singularId=1` ativa e sem área com nome "Financeiro".

#### When

`POST /api/v1/areas` com `{ "singularId": 1, "name": "Financeiro", "acronym": "FIN" }`.

#### Then

- HTTP 201
- Resposta contém `id`, `name=Financeiro`, `status=ACTIVE`, `singularId=1`
- Registro persistido em `AREA` com `FLG_ATIVO='S'`

### Cenário — Nome duplicado

#### Given

Área ativa "Financeiro" já cadastrada na singular 1.

#### When

`POST /api/v1/areas` com mesmo nome na singular 1.

#### Then

- HTTP 422
- Nenhum novo registro criado

### Cenário — Gestor inválido (FE-002)

#### Given

`managerId` não referencia colaborador ativo existente.

#### When

`POST /api/v1/areas` com `managerId` inválido e demais campos válidos.

#### Then

- HTTP 422
- Nenhum novo registro criado

### Cenário — Não autorizado

#### Given

Usuário autenticado sem papel administrativo.

#### When

`POST /api/v1/areas` com payload válido.

#### Then

- HTTP 403

### Resultado Esperado

Cadastro respeita regras de negócio e autorização.

---

# AT-AREA-002 — Consultar Área por Identificador

### Objetivo

Validar consulta por id existente e inexistente.

### Prioridade

Must

### Tipo

Happy Path / Negative / Authorization

### Requisitos Funcionais Relacionados

- RF-AREA-002

### Casos de Uso Relacionados

- UC-AREA-002

### Cenário — Happy Path

#### Given

Área cadastrada com `id=10`.

#### When

`GET /api/v1/areas/10`.

#### Then

- HTTP 200
- Corpo contém dados da área

### Cenário — Inexistente

#### When

`GET /api/v1/areas/999999`.

#### Then

- HTTP 404

### Cenário — Não autenticado

#### When

Requisição sem autenticação.

#### Then

- HTTP 401

---

# AT-AREA-003 — Listar Áreas

### Objetivo

Validar listagem paginada, filtros e coleção vazia.

### Prioridade

Must

### Tipo

Happy Path / Boundary

### Requisitos Funcionais Relacionados

- RF-AREA-003

### Casos de Uso Relacionados

- UC-AREA-003

### Cenário — Paginação e filtro

#### Given

Múltiplas áreas cadastradas, sendo 2 ativas na singular 1.

#### When

`GET /api/v1/areas?singularId=1&status=ACTIVE&page=0&size=10&sort=name,asc`.

#### Then

- HTTP 200
- `PageResponse` com `content`, `totalElements`, `size`, `number`
- Apenas áreas da singular 1 e status ACTIVE

### Cenário — Coleção vazia

#### When

`GET /api/v1/areas?name=INEXISTENTE`.

#### Then

- HTTP 200
- `content` vazio

---

# AT-AREA-004 — Atualizar Área

### Objetivo

Validar atualização cadastral, imutabilidade de singular e regras de negócio.

### Prioridade

Must

### Tipo

Happy Path / Negative / Business Rule

### Requisitos Funcionais Relacionados

- RF-AREA-004

### Casos de Uso Relacionados

- UC-AREA-004

### Regras de Negócio Relacionadas

- RN-AREA-001, RN-AREA-002, RN-AREA-003, RN-AREA-006, RN-AREA-009

### Cenário — Happy Path

#### Given

Área `id=10` com nome "Financeiro".

#### When

`PUT /api/v1/areas/10` com `{ "name": "Financeiro e Controladoria", "acronym": "FIN" }`.

#### Then

- HTTP 200
- Nome atualizado
- `updatedAt` preenchido

### Cenário — Singular imutável

#### When

Payload de atualização tenta alterar singular (campo ignorado ou rejeitado).

#### Then

- `singularId` permanece inalterado no registro

### Cenário — Singular vinculada inativa (FE-004 / RN-AREA-001)

#### Given

Área `id=10` vinculada à singular `singularId=1`, cuja singular está inativa.

#### When

`PUT /api/v1/areas/10` com payload válido de atualização.

#### Then

- HTTP 422
- Dados da área permanecem inalterados

---

# AT-AREA-005 — Alterar Status da Área

### Objetivo

Validar ativação, inativação e bloqueios de integridade.

### Prioridade

Must

### Tipo

Happy Path / Negative / Business Rule

### Requisitos Funcionais Relacionados

- RF-AREA-005

### Casos de Uso Relacionados

- UC-AREA-005

### Regras de Negócio Relacionadas

- RN-AREA-007, RN-AREA-008

### Cenário — Inativar com sucesso

#### Given

Área ativa sem equipes ativas.

#### When

`PATCH /api/v1/areas/{id}/status` com `{ "status": "INACTIVE" }`.

#### Then

- HTTP 200
- `status=INACTIVE`
- Registro permanece na base (`FLG_ATIVO='N'`)

### Cenário — Inativação bloqueada por equipe ativa

#### Given

Área com equipe ativa vinculada.

#### When

Tentativa de inativação.

#### Then

- HTTP 422
- Status permanece ACTIVE

### Cenário — Reativar

#### Given

Área inativa com singular ativa.

#### When

`PATCH` com `{ "status": "ACTIVE" }`.

#### Then

- HTTP 200
- `status=ACTIVE`

---

# Cenários Negativos Transversais

| Cenário | Endpoint | Resultado |
|---------|----------|-----------|
| Payload inválido (name vazio) | POST, PUT | HTTP 400 |
| Recurso inexistente | PUT, PATCH | HTTP 404 |
| Usuário não autenticado | Todos | HTTP 401 |
| CSRF ausente em mutação por cookie | POST, PUT, PATCH | HTTP 403 (conforme FT-AUTH) |

---

# Matriz de Rastreabilidade

| Teste | RF | UC | RN |
|--------|----|----|----|
| AT-AREA-001 | RF-AREA-001 | UC-AREA-001 | RN-AREA-001 a 006 |
| AT-AREA-002 | RF-AREA-002 | UC-AREA-002 | — |
| AT-AREA-003 | RF-AREA-003 | UC-AREA-003 | — |
| AT-AREA-004 | RF-AREA-004 | UC-AREA-004 | RN-AREA-001, 002 a 006, 009 |
| AT-AREA-005 | RF-AREA-005 | UC-AREA-005 | RN-AREA-007, 008 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- todos os requisitos funcionais possuírem pelo menos um critério de aceitação;
- todos os critérios estiverem rastreados;
- mantiver consistência com Specification, Casos de Uso, API Contract e `traceability.md`;
- não duplicar padrões de `docs/implementation/08-testing-strategy.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-13 | Specification Engineer | Critérios de aceitação iniciais FT-AREA |
| 1.1 | 2026-07-13 | Specification Engineer | Sincronização Specification Framework v1.1 |
| 1.2.0 | 2026-07-21 | Engineering Framework | Remoção hierarquia entre áreas (DEC-DB-022) |
