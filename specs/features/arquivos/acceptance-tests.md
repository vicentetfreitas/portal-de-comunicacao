# Acceptance Tests

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — somente leitura) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO |
| Feature | Arquivos e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Critérios de aceitação da Feature de leitura de pastas/arquivos. Estratégia de testes, ferramentas e cobertura: `docs/implementation/08-testing-strategy.md` — não duplicado aqui.

---

## AT-DOCUMENTO-001 — Listar pastas e arquivos da Área

### Tipo

Happy Path / Boundary

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-001

### Casos de Uso Relacionados

- UC-DOCUMENTO-001

### Cenário — sucesso

**Given** colaborador autenticado com Contexto Ativo resolvido e Área com pastas cadastradas
**When** solicita `GET /api/v1/pastas`
**Then** recebe a lista de pastas da própria Área, cada uma com seus documentos (nome, formato)

### Cenário — coleção vazia

**Given** colaborador autenticado cuja Área não possui pastas cadastradas
**When** solicita `GET /api/v1/pastas`
**Then** recebe lista vazia (200), não erro

---

## AT-DOCUMENTO-002 — Baixar arquivo

### Tipo

Happy Path / Negative

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-002

### Casos de Uso Relacionados

- UC-DOCUMENTO-002

### Cenário — sucesso

**Given** documento existente vinculado à Área do Contexto Ativo do colaborador
**When** solicita `GET /api/v1/documentos/{id}/download`
**Then** recebe o binário do arquivo via Object Storage (DEC-013)

### Cenário — documento inexistente

**Given** identificador de documento que não existe
**When** solicita `GET /api/v1/documentos/{id}/download`
**Then** recebe `404`

---

## AT-DOCUMENTO-003 — Negar acesso fora da Área

### Tipo

Authorization

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-003

### Casos de Uso Relacionados

- UC-DOCUMENTO-003

### Cenário — listagem não vaza outras Áreas

**Given** colaborador autenticado com Contexto Ativo na Área A, e existem pastas cadastradas na Área B
**When** solicita `GET /api/v1/pastas`
**Then** a resposta não contém nenhuma pasta/documento da Área B

### Cenário — download negado

**Given** documento vinculado à Área B, colaborador autenticado com Contexto Ativo na Área A
**When** solicita `GET /api/v1/documentos/{id}/download` para o documento da Área B
**Then** recebe `403` explícito — nunca `404` disfarçado nem filtragem silenciosa

---

# Cenários Negativos (transversais)

- Usuário não autenticado → `401` (padrão corporativo, não redocumentado aqui).
- Usuário autenticado sem Contexto Ativo resolvido → padrão de Primeiro Acesso/Sessão (`FT-PRIMEIRO-ACESSO`/`FT-SESSION`), não redocumentado aqui.

---

# Matriz de Rastreabilidade

| Teste | RF | UC |
|--------|----|----|
| AT-DOCUMENTO-001 | RF-DOCUMENTO-001 | UC-DOCUMENTO-001 |
| AT-DOCUMENTO-002 | RF-DOCUMENTO-002 | UC-DOCUMENTO-002 |
| AT-DOCUMENTO-003 | RF-DOCUMENTO-003 | UC-DOCUMENTO-003 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — 3 ATs cobrindo os 3 RFs (somente leitura) |
