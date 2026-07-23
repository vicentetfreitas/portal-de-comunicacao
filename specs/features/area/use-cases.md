# Use Cases

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

Este documento descreve os casos de uso da Feature FT-AREA.

Estabelece a rastreabilidade entre Specification, API Contract, Acceptance Tests e Regras de Negócio.

---

# Convenções

## Identificação

```text
UC-AREA-001
UC-AREA-002
...
```

## Fluxos Alternativos

```text
FA-001
FA-002
...
```

## Fluxos de Exceção

```text
FE-001
FE-002
...
```

---

# UC-AREA-001 — Cadastrar Área

### Objetivo

Cadastrar uma nova área organizacional vinculada a uma singular.

### Prioridade

Must

### Complexidade

Média

### Atores

- Administrador Global
- Administrador de Singular

### Pré-condições

- Usuário autenticado (FT-AUTH)
- Usuário possui permissão administrativa no escopo da singular informada
- Singular de destino existe e está ativa

### Fluxo Principal

1. O ator informa `singularId`, `name` e atributos opcionais (`acronym`, `description`, `managerId`).
2. O sistema valida obrigatoriedade, formatos e regras de negócio (RN-AREA-001 a RN-AREA-003, RN-AREA-006).
3. O sistema persiste a área com status ativo e auditoria de criação.
4. O sistema retorna os dados da área criada.

### Fluxos Alternativos

**FA-001 — Cadastro sem gestor**

1. O ator não informa `managerId`.
2. O sistema cadastra a área sem gestor associado.

### Fluxos de Exceção

**FE-001 — Nome duplicado na singular**

1. Já existe área ativa com o mesmo nome na singular.
2. O sistema rejeita a operação (HTTP 422).

**FE-002 — Gestor inválido**

1. `managerId` não referencia colaborador ativo.
2. O sistema rejeita a operação (HTTP 422).

**FE-003 — Não autorizado**

1. Usuário sem permissão administrativa no escopo.
2. O sistema retorna HTTP 403.

### Pós-condições

- Nova área registrada na tabela `AREA` com `FLG_ATIVO = 'S'`.
- Identificador `COD_AREA` gerado pela sequence corporativa.

### Requisitos Funcionais Relacionados

- RF-AREA-001

### Regras de Negócio Relacionadas

- RN-AREA-001, RN-AREA-002, RN-AREA-003, RN-AREA-006

### Requisitos Não Funcionais Relacionados

- RNF-AREA-001, RNF-AREA-002, RNF-AREA-003, RNF-AREA-004, RNF-AREA-005

### Critérios de Aceitação Relacionados

- AT-AREA-001

---

# UC-AREA-002 — Consultar Área por Identificador

### Objetivo

Consultar os dados de uma área existente.

### Prioridade

Must

### Complexidade

Baixa

### Atores

- Usuário Autenticado

### Pré-condições

- Usuário autenticado (FT-AUTH)

### Fluxo Principal

1. O ator informa o identificador da área.
2. O sistema localiza o registro.
3. O sistema retorna os dados públicos da área.

### Fluxos de Exceção

**FE-001 — Área inexistente**

1. Identificador não encontrado.
2. O sistema retorna HTTP 404.

**FE-002 — Não autenticado**

1. Usuário sem sessão válida.
2. O sistema retorna HTTP 401.

### Pós-condições

- Nenhuma alteração de estado.

### Requisitos Funcionais Relacionados

- RF-AREA-002

### Regras de Negócio Relacionadas

- —

### Requisitos Não Funcionais Relacionados

- RNF-AREA-001, RNF-AREA-003

### Critérios de Aceitação Relacionados

- AT-AREA-002

---

# UC-AREA-003 — Listar Áreas

### Objetivo

Listar áreas utilizando paginação, ordenação e filtros corporativos.

### Prioridade

Must

### Complexidade

Média

### Atores

- Usuário Autenticado

### Pré-condições

- Usuário autenticado (FT-AUTH)

### Fluxo Principal

1. O ator solicita listagem com parâmetros opcionais (`page`, `size`, `sort`, `status`, `singularId`, `name`, `acronym`).
2. O sistema aplica filtros e ordenação.
3. O sistema retorna `PageResponse` com as áreas encontradas.

### Fluxos Alternativos

**FA-001 — Listagem vazia**

1. Nenhum registro atende aos filtros.
2. O sistema retorna página vazia com metadados de paginação.

### Fluxos de Exceção

**FE-001 — Parâmetros de paginação inválidos**

1. Parâmetros fora dos limites corporativos.
2. O sistema retorna HTTP 400.

### Pós-condições

- Nenhuma alteração de estado.

### Requisitos Funcionais Relacionados

- RF-AREA-003

### Regras de Negócio Relacionadas

- —

### Requisitos Não Funcionais Relacionados

- RNF-AREA-001, RNF-AREA-003

### Critérios de Aceitação Relacionados

- AT-AREA-003

---

# UC-AREA-004 — Atualizar Área

### Objetivo

Atualizar os dados cadastrais de uma área existente.

### Prioridade

Must

### Complexidade

Média

### Atores

- Administrador Global
- Administrador de Singular
- Administrador de Área

### Pré-condições

- Usuário autenticado com permissão administrativa no escopo da área
- Área existe

### Fluxo Principal

1. O ator informa o identificador e os dados a atualizar (`name`, `acronym`, `description`, `managerId`).
2. O sistema valida regras de negócio, incluindo imutabilidade de `singularId` (RN-AREA-009).
3. O sistema persiste as alterações e atualiza auditoria.
4. O sistema retorna os dados atualizados.

### Fluxos de Exceção

**FE-001 — Nome duplicado**

1. Nome já utilizado por outra área ativa na mesma singular.
2. O sistema retorna HTTP 422.

**FE-002 — Área inexistente**

1. Identificador não encontrado.
2. O sistema retorna HTTP 404.

**FE-003 — Singular vinculada inativa**

1. A singular vinculada à área está inativa.
2. O sistema rejeita a operação (HTTP 422).

### Pós-condições

- Dados da área atualizados; `DAT_ATUALIZACAO` preenchido.

### Requisitos Funcionais Relacionados

- RF-AREA-004

### Regras de Negócio Relacionadas

- RN-AREA-001, RN-AREA-002, RN-AREA-003, RN-AREA-006, RN-AREA-009

### Requisitos Não Funcionais Relacionados

- RNF-AREA-001, RNF-AREA-002, RNF-AREA-005

### Critérios de Aceitação Relacionados

- AT-AREA-004

---

# UC-AREA-005 — Alterar Status da Área

### Objetivo

Ativar ou inativar logicamente uma área.

### Prioridade

Must

### Complexidade

Média

### Atores

- Administrador Global
- Administrador de Singular

### Pré-condições

- Usuário autenticado com permissão administrativa no escopo da área
- Área existe

### Fluxo Principal

1. O ator informa o identificador e o novo status (`ACTIVE` ou `INACTIVE`).
2. O sistema valida regras de inativação (RN-AREA-008) quando aplicável.
3. O sistema atualiza `FLG_ATIVO` e auditoria.
4. O sistema retorna os dados da área com status atualizado.

### Fluxos Alternativos

**FA-001 — Reativação**

1. O ator informa status `ACTIVE` para área inativa.
2. O sistema reativa a área se singular permanece válida.

### Fluxos de Exceção

**FE-001 — Inativação bloqueada**

1. Área possui equipes ativas.
2. O sistema retorna HTTP 422.

**FE-002 — Status inválido**

1. Valor de status não permitido.
2. O sistema retorna HTTP 400.

### Pós-condições

- Status lógico da área alterado sem exclusão física.

### Requisitos Funcionais Relacionados

- RF-AREA-005

### Regras de Negócio Relacionadas

- RN-AREA-007, RN-AREA-008

### Requisitos Não Funcionais Relacionados

- RNF-AREA-001, RNF-AREA-002, RNF-AREA-005

### Critérios de Aceitação Relacionados

- AT-AREA-005

---

# Matriz de Rastreabilidade

| Caso de Uso | RF | RN | RNF | API | Teste |
|--------------|----|----|-----|-----|--------|
| UC-AREA-001 | RF-AREA-001 | RN-AREA-001 a 006 | RNF-AREA-001 a 005 | POST /api/v1/areas | AT-AREA-001 |
| UC-AREA-002 | RF-AREA-002 | — | RNF-AREA-001, 003 | GET /api/v1/areas/{id} | AT-AREA-002 |
| UC-AREA-003 | RF-AREA-003 | — | RNF-AREA-001, 003 | GET /api/v1/areas | AT-AREA-003 |
| UC-AREA-004 | RF-AREA-004 | RN-AREA-001, 002 a 006, 009 | RNF-AREA-001, 002, 005 | PUT /api/v1/areas/{id} | AT-AREA-004 |
| UC-AREA-005 | RF-AREA-005 | RN-AREA-007, 008 | RNF-AREA-001, 002, 005 | PATCH /api/v1/areas/{id}/status | AT-AREA-005 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- todos os casos de uso estiverem rastreados;
- cada caso de uso possuir pelo menos um requisito funcional associado;
- todos os fluxos estiverem documentados quando aplicáveis;
- não existirem casos de uso sem critérios de aceitação;
- mantiver consistência com a Specification, o API Contract e `traceability.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-13 | Specification Engineer | Casos de uso iniciais FT-AREA |
| 1.1 | 2026-07-13 | Specification Engineer | Sincronização Specification Framework v1.1 |
| 1.2.0 | 2026-07-21 | Engineering Framework | Remoção hierarquia entre áreas (DEC-DB-022) |
