# Use Cases

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

Este documento descreve os casos de uso da Feature FT-SINGULAR.

Estabelece a rastreabilidade entre Specification, API Contract, Acceptance Tests e Regras de Negócio.

---

# Convenções

## Identificação

```text
UC-SINGULAR-001
UC-SINGULAR-002
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

# UC-SINGULAR-001 — Cadastrar Singular

### Objetivo

Cadastrar uma nova singular organizacional vinculada a uma federação.

### Prioridade

Must

### Complexidade

Média

### Atores

- Administrador Global

### Pré-condições

- Usuário autenticado (FT-AUTH)
- Usuário possui permissão administrativa global
- Federação de destino existe e está ativa

### Fluxo Principal

1. O ator informa `federationId`, `name`, `acronym` e `unimedCode`.
2. O sistema valida obrigatoriedade, formatos e regras de negócio (RN-SINGULAR-001 a RN-SINGULAR-004).
3. O sistema persiste a singular com status ativo e auditoria de criação.
4. O sistema retorna os dados da singular criada.

### Fluxos de Exceção

**FE-001 — Sigla duplicada**

1. Já existe singular com a mesma sigla.
2. O sistema rejeita a operação (HTTP 422).

**FE-002 — Código Unimed duplicado**

1. Já existe singular com o mesmo código Unimed.
2. O sistema rejeita a operação (HTTP 422).

**FE-003 — Federação inválida**

1. `federacaoId` não existe ou está inativa.
2. O sistema rejeita a operação (HTTP 422).

**FE-004 — Não autorizado**

1. Usuário autenticado sem papel administrativo global.
2. O sistema rejeita a operação (HTTP 403).

### Pós-condições

- Singular persistida com `FLG_ATIVO = 'S'`.
- Registro disponível para vínculo em FT-AREA.

---

# UC-SINGULAR-002 — Consultar Singular por Identificador

### Objetivo

Obter os dados cadastrais de uma singular pelo identificador.

### Prioridade

Must

### Complexidade

Baixa

### Atores

- Administrador Global
- Administrador de Singular
- Usuário Autenticado (leitura conforme escopo)

### Pré-condições

- Usuário autenticado (FT-AUTH)

### Fluxo Principal

1. O ator informa o identificador da singular.
2. O sistema localiza o registro.
3. O sistema retorna os dados da singular.

### Fluxos de Exceção

**FE-001 — Singular inexistente**

1. Identificador não corresponde a registro existente.
2. O sistema retorna HTTP 404.

---

# UC-SINGULAR-003 — Listar Singulares

### Objetivo

Listar singulares com paginação, ordenação e filtros.

### Prioridade

Must

### Complexidade

Média

### Atores

- Administrador Global
- Administrador de Singular
- Usuário Autenticado (leitura conforme escopo)

### Pré-condições

- Usuário autenticado (FT-AUTH)

### Fluxo Principal

1. O ator solicita listagem com parâmetros opcionais de filtro, paginação e ordenação.
2. O sistema aplica filtros (`status`, `federationId`, `name`, `acronym`, `unimedCode`).
3. O sistema retorna página de resultados (pode ser vazia).

### Fluxos de Exceção

**FE-001 — Parâmetros inválidos**

1. Parâmetros de paginação ou ordenação inválidos.
2. O sistema retorna HTTP 400.

---

# UC-SINGULAR-004 — Atualizar Singular

### Objetivo

Atualizar dados cadastrais de uma singular existente.

### Prioridade

Must

### Complexidade

Média

### Atores

- Administrador Global
- Administrador de Singular (escopo limitado)

### Pré-condições

- Usuário autenticado (FT-AUTH)
- Singular existente
- Federação vinculada ativa (RN-SINGULAR-001)

### Fluxo Principal

1. O ator informa identificador e payload com `name`, `acronym` e `unimedCode`.
2. O sistema valida regras RN-SINGULAR-002 a RN-SINGULAR-004 e RN-SINGULAR-007.
3. O sistema atualiza o registro e auditoria.
4. O sistema retorna dados atualizados.

### Fluxos de Exceção

**FE-001 — Sigla ou código Unimed duplicado**

1. Outra singular já utiliza sigla ou código Unimed informado.
2. O sistema rejeita a operação (HTTP 422).

**FE-002 — Singular inexistente**

1. Identificador inválido.
2. O sistema retorna HTTP 404.

**FE-003 — Não autorizado**

1. Usuário sem permissão no escopo.
2. O sistema retorna HTTP 403.

**FE-004 — Federação vinculada inativa**

1. A federação vinculada à singular está inativa.
2. O sistema rejeita a operação (HTTP 422).

---

# UC-SINGULAR-005 — Alterar Status da Singular

### Objetivo

Ativar ou inativar logicamente uma singular.

### Prioridade

Must

### Complexidade

Média

### Atores

- Administrador Global

### Pré-condições

- Usuário autenticado (FT-AUTH)
- Singular existente

### Fluxo Principal

1. O ator informa identificador e novo status (`ACTIVE` ou `INACTIVE`).
2. O sistema valida RN-SINGULAR-005 e, se inativação, RN-SINGULAR-006.
3. O sistema atualiza `FLG_ATIVO` e auditoria.
4. O sistema retorna dados atualizados.

### Fluxos Alternativos

**FA-001 — Reativação**

1. Singular inativa recebe status `ACTIVE`.
2. O sistema reativa sem restrição de áreas dependentes.

### Fluxos de Exceção

**FE-001 — Inativação bloqueada**

1. Singular possui áreas ativas vinculadas.
2. O sistema rejeita a operação (HTTP 422).

**FE-002 — Singular inexistente**

1. Identificador inválido.
2. O sistema retorna HTTP 404.

---

# Matriz de Rastreabilidade

| UC | RF | RN |
|----|----|----|
| UC-SINGULAR-001 | RF-SINGULAR-001 | RN-SINGULAR-001 a 004 |
| UC-SINGULAR-002 | RF-SINGULAR-002 | — |
| UC-SINGULAR-003 | RF-SINGULAR-003 | — |
| UC-SINGULAR-004 | RF-SINGULAR-004 | RN-SINGULAR-001, 002 a 004, 007 |
| UC-SINGULAR-005 | RF-SINGULAR-005 | RN-SINGULAR-005, 006 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- todos os casos de uso estiverem associados a RF;
- fluxos principais, alternativos e de exceção estiverem documentados;
- mantiver consistência com `specification.md`, `api.md` e `traceability.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | Casos de uso iniciais FT-SINGULAR |
| 1.1.1 | 2026-07-14 | Specification Engineer | Refinamento Gate 1 — NC-01 (FE-004 federação inativa em UC-SINGULAR-004) |
