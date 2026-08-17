# Acceptance Tests

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

Este documento define os critérios de aceitação verificáveis da Feature FT-SINGULAR.

Estratégia de testes, ferramentas e cobertura: `docs/implementation/08-testing-strategy.md`.

---

# Convenções

```text
AT-SINGULAR-001
AT-SINGULAR-002
...
```

---

# AT-SINGULAR-001 — Criar Singular

### Objetivo

Validar cadastro de singular com sucesso e rejeição de violações de negócio.

### Prioridade

Must

### Tipo

Happy Path / Negative / Business Rule / Authorization

### Requisitos Funcionais Relacionados

- RF-SINGULAR-001

### Casos de Uso Relacionados

- UC-SINGULAR-001

### Regras de Negócio Relacionadas

- RN-SINGULAR-001, RN-SINGULAR-002, RN-SINGULAR-003, RN-SINGULAR-004

### Pré-condições

- Usuário administrador global autenticado
- Federação ativa existente no banco (seed ou referência)

### Cenário — Happy Path

#### Given

Federação `federationId=1` ativa e sem singular com sigla "UNI-CE".

#### When

`POST /api/v1/singulares` com `{ "federationId": 1, "name": "Unimed Ceará", "acronym": "UNI-CE", "unimedCode": 42, "registroAns": "000042" }`.

#### Then

- HTTP 201
- Resposta contém `id`, `name=Unimed Ceará`, `status=ACTIVE`, `federationId=1`
- Registro persistido em `SINGULAR` com `FLG_ATIVO='S'`

### Cenário — Sigla duplicada

#### Given

Singular ativa com sigla "UNI-CE" já cadastrada.

#### When

`POST /api/v1/singulares` com mesma sigla e demais campos válidos.

#### Then

- HTTP 422
- Nenhum novo registro criado

### Cenário — Código Unimed duplicado

#### Given

Singular ativa com `unimedCode` 42 já cadastrado.

#### When

`POST /api/v1/singulares` com mesmo código Unimed e demais campos válidos.

#### Then

- HTTP 422
- Nenhum novo registro criado

### Cenário — Federação inválida

#### Given

`federationId` inexistente ou inativo.

#### When

`POST /api/v1/singulares` com payload válido exceto federação.

#### Then

- HTTP 422
- Nenhum novo registro criado

### Cenário — Não autorizado

#### Given

Usuário autenticado sem papel administrativo global.

#### When

`POST /api/v1/singulares` com payload válido.

#### Then

- HTTP 403

### Resultado Esperado

Cadastro respeita regras de negócio e autorização.

---

# AT-SINGULAR-002 — Consultar Singular por Identificador

### Objetivo

Validar consulta de singular existente e tratamento de inexistente.

### Prioridade

Must

### Tipo

Happy Path / Negative

### Requisitos Funcionais Relacionados

- RF-SINGULAR-002

### Casos de Uso Relacionados

- UC-SINGULAR-002

### Cenário — Happy Path

#### Given

Singular `id=1` existente.

#### When

`GET /api/v1/singulares/1`.

#### Then

- HTTP 200
- Resposta contém dados cadastrais da singular

### Cenário — Inexistente

#### When

`GET /api/v1/singulares/999999`.

#### Cenário — Não autenticado

#### When

Requisição sem autenticação.

#### Then

- HTTP 401

---

# AT-SINGULAR-003 — Listar Singulares

### Objetivo

Validar listagem paginada com filtros.

### Prioridade

Must

### Tipo

Happy Path

### Requisitos Funcionais Relacionados

- RF-SINGULAR-003

### Casos de Uso Relacionados

- UC-SINGULAR-003

### Cenário — Listagem com filtro de status

#### Given

Singulares ativas e inativas no banco.

#### When

`GET /api/v1/singulares?status=ACTIVE&page=0&size=10`.

#### Then

- HTTP 200
- Apenas singulares ativas na página retornada

### Cenário — Listagem vazia

#### When

`GET /api/v1/singulares?name=INEXISTENTE`.

#### Then

- HTTP 200
- Página vazia com metadados de paginação válidos

### Cenário — Parâmetros inválidos

#### When

`GET /api/v1/singulares?page=-1` ou `sort` inválido.

#### Then

- HTTP 400

---

# AT-SINGULAR-004 — Atualizar Singular

### Objetivo

Validar atualização cadastral e imutabilidade de federação.

### Prioridade

Must

### Tipo

Happy Path / Business Rule

### Requisitos Funcionais Relacionados

- RF-SINGULAR-004

### Casos de Uso Relacionados

- UC-SINGULAR-004

### Regras de Negócio Relacionadas

- RN-SINGULAR-001, RN-SINGULAR-002, RN-SINGULAR-003, RN-SINGULAR-004, RN-SINGULAR-007

### Cenário — Happy Path

#### Given

Singular `id=1` existente.

#### When

`PUT /api/v1/singulares/1` com `{ "name": "Unimed Ceará Atualizada", "acronym": "UNI-CE", "unimedCode": 42, "registroAns": "000042" }`.

#### Then

- HTTP 200
- `name` atualizado
- `federationId` inalterado

### Cenário — Sigla duplicada em outra singular

#### When

`PUT /api/v1/singulares/1` com sigla já utilizada por outra singular.

#### Then

- HTTP 422

### Cenário — Federação vinculada inativa (FE-004 / RN-SINGULAR-001)

#### Given

Singular `id=1` vinculada à federação `federationId=1`, cuja federação está inativa.

#### When

`PUT /api/v1/singulares/1` com payload válido de atualização.

#### Then

- HTTP 422
- Dados da singular permanecem inalterados

### Cenário — Singular inexistente

#### When

`PUT /api/v1/singulares/999999` com payload válido.

#### Then

- HTTP 404

### Cenário — Não autorizado

#### Given

Usuário autenticado sem permissão no escopo.

#### When

`PUT /api/v1/singulares/1` com payload válido.

#### Then

- HTTP 403

---

# AT-SINGULAR-005 — Alterar Status da Singular

### Objetivo

Validar ativação/inativação lógica e restrição por áreas ativas.

### Prioridade

Must

### Tipo

Happy Path / Business Rule

### Requisitos Funcionais Relacionados

- RF-SINGULAR-005

### Casos de Uso Relacionados

- UC-SINGULAR-005

### Regras de Negócio Relacionadas

- RN-SINGULAR-005, RN-SINGULAR-006

### Cenário — Inativação com sucesso

#### Given

Singular ativa sem áreas ativas vinculadas.

#### When

`PATCH /api/v1/singulares/{id}/status` com `{ "status": "INACTIVE" }`.

#### Then

- HTTP 200
- `status=INACTIVE`
- `FLG_ATIVO='N'`

### Cenário — Inativação bloqueada

#### Given

Singular com área ativa vinculada (FT-AREA).

#### When

`PATCH /api/v1/singulares/{id}/status` com `{ "status": "INACTIVE" }`.

#### Then

- HTTP 422
- Status permanece ativo

### Cenário — Reativação

#### Given

Singular inativa.

#### When

`PATCH /api/v1/singulares/{id}/status` com `{ "status": "ACTIVE" }`.

#### Then

- HTTP 200
- `status=ACTIVE`

### Cenário — Singular inexistente

#### When

`PATCH /api/v1/singulares/999999/status` com `{ "status": "INACTIVE" }`.

#### Then

- HTTP 404

---

# Matriz de Rastreabilidade

| AT | RF | UC |
|----|----|----|
| AT-SINGULAR-001 | RF-SINGULAR-001 | UC-SINGULAR-001 |
| AT-SINGULAR-002 | RF-SINGULAR-002 | UC-SINGULAR-002 |
| AT-SINGULAR-003 | RF-SINGULAR-003 | UC-SINGULAR-003 |
| AT-SINGULAR-004 | RF-SINGULAR-004 | UC-SINGULAR-004 |
| AT-SINGULAR-005 | RF-SINGULAR-005 | UC-SINGULAR-005 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- todos os cenários Must estiverem associados a RF e UC;
- cenários cobrirem happy path e exceções de negócio documentadas;
- mantiver consistência com `specification.md`, `use-cases.md` e `traceability.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | Acceptance tests iniciais FT-SINGULAR |
| 1.1.1 | 2026-07-14 | Specification Engineer | Refinamento Gate 1 — NC-02 a NC-05 (cobertura RN-001, 401, 400, 404, 403) |
