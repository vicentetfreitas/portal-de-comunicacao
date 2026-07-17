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
| Feature ID | FT-EQUIPE |
| Feature | Equipe |
| Domínio | EQUIPE |

---

# Objetivo

Critérios de aceitação verificáveis da Feature FT-EQUIPE: testes de API (backend) e cenários E2E (frontend administrativo).

Estratégia: `docs/implementation/08-testing-strategy.md`.

---

# Convenções

```text
AT-EQUIPE-001       — API / integração backend
AT-FE-EQUIPE-001    — Playwright (frontend)
```

---

# Backend (API)

## AT-EQUIPE-001 — Criar Equipe

### Cenários

- Happy path: área ativa, nome único na área → HTTP 201
- Nome duplicado na mesma área (equipe ativa) → HTTP 422
- Não autenticado → HTTP 401
- Não autorizado → HTTP 403

### Rastreabilidade

RF-EQUIPE-001, UC-EQUIPE-001, TK-EQUIPE-001

---

## AT-EQUIPE-002 — Consultar Equipe

### Cenários

- Por id existente → HTTP 200
- Id inexistente → HTTP 404
- Não autenticado → HTTP 401

### Rastreabilidade

RF-EQUIPE-002, UC-EQUIPE-002, TK-EQUIPE-002

---

## AT-EQUIPE-003 — Listar Equipes

### Cenários

- Filtros `status`, `areaId`, `name` e paginação
- Lista vazia → página vazia válida

### Rastreabilidade

RF-EQUIPE-003, UC-EQUIPE-003, TK-EQUIPE-003

---

## AT-EQUIPE-004 — Atualizar Equipe

### Cenários

- Atualização com sucesso → HTTP 200
- Nome duplicado na área → HTTP 422
- `areaId` não enviado no PUT (imutabilidade)

### Rastreabilidade

RF-EQUIPE-004, UC-EQUIPE-004, TK-EQUIPE-004

---

## AT-EQUIPE-005 — Alterar Status

### Cenários

- Inativar equipe sem colaboradores ativos → sucesso
- Inativar com colaboradores ativos → HTTP 422 (*Equipe possui colaboradores ativos vinculados*)
- Reativar equipe inativa → sucesso

### Rastreabilidade

RF-EQUIPE-005, UC-EQUIPE-005, TK-EQUIPE-005

---

# Frontend (E2E)

Pré-condição comum: administrador autenticado (mock FT-AUTH); API mockada ou ambiente de teste com dados seed.

## AT-FE-EQUIPE-001 — Cadastro na UI

### Objetivo

Validar fluxo de cadastro e erro de nome duplicado.

### Requisitos

RF-FE-EQUIPE-001 → RF-EQUIPE-001

### Cenário — Happy path

- **Given** área disponível no mock e sem equipe ativa com nome "Equipe Alpha"
- **When** usuário preenche área, nome e submete em `/app/administrador/equipes/novo`
- **Then** redireciona para detalhe com heading do nome e status *Ativa*

### Cenário — Nome duplicado

- **Given** equipe ativa "Equipe Alpha" na mesma área
- **When** cadastro com mesmo nome e área
- **Then** mensagem de erro visível contendo *Já existe equipe ativa com este nome na área*

### Backend relacionado

AT-EQUIPE-001

---

## AT-FE-EQUIPE-002 — Detalhe na UI

### Cenário — Consulta

- **Given** equipe id=1 no mock
- **When** navega para `/app/administrador/equipes/1`
- **Then** exibe nome, área e status

### Cenário — Não encontrado

- **When** id inexistente
- **Then** estado de erro ou redirecionamento amigável (sem quebra de layout)

### Backend relacionado

AT-EQUIPE-002

---

## AT-FE-EQUIPE-003 — Listagem na UI

### Cenário — Filtro e paginação

- **Given** múltiplas equipes no mock (incluindo inativas em página seguinte se paginado)
- **When** aplica filtro de status *Ativa* em `/app/administrador/equipes/lista`
- **Then** apenas equipes ativas visíveis na página atual; controles de paginação funcionais

### Cenário — Lista vazia

- **Given** nenhuma equipe
- **Then** estado vazio com ação para criar nova equipe

### Backend relacionado

AT-EQUIPE-003

---

## AT-FE-EQUIPE-004 — Edição na UI

### Cenário — Happy path

- **Given** equipe existente
- **When** altera nome em `/app/administrador/equipes/:id/editar` e salva
- **Then** detalhe reflete novo nome; `areaId` não editável

### Cenário — Nome duplicado

- **When** nome conflitante na mesma área
- **Then** erro 422 exibido ao usuário

### Backend relacionado

AT-EQUIPE-004

---

## AT-FE-EQUIPE-005 — Status na UI

### Cenário — Inativação com confirmação

- **Given** equipe ativa sem bloqueio
- **When** usuário confirma inativação no diálogo
- **Then** status exibe *Inativa*

### Cenário — Bloqueio RN-006

- **Given** API retorna 422 por colaboradores ativos
- **When** tentativa de inativar
- **Then** mensagem *Equipe possui colaboradores ativos vinculados* visível

### Backend relacionado

AT-EQUIPE-005

---

# Matriz AT-FE × AT Backend

| AT-FE | AT Backend |
|-------|------------|
| AT-FE-EQUIPE-001 | AT-EQUIPE-001 |
| AT-FE-EQUIPE-002 | AT-EQUIPE-002 |
| AT-FE-EQUIPE-003 | AT-EQUIPE-003 |
| AT-FE-EQUIPE-004 | AT-EQUIPE-004 |
| AT-FE-EQUIPE-005 | AT-EQUIPE-005 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | AT backend resumidos |
| 1.1 | 2026-07-17 | Specification Engineer | AT-FE-EQUIPE-001..005 e estrutura Gate 1 |
