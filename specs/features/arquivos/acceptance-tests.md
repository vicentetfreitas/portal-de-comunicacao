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

**Given** colaborador autenticado com Contexto Ativo resolvido e ao menos uma `PERMISSAO_PASTA` (`TIP_ACESSO=LEITURA`) para algum nível do seu Contexto Ativo
**When** solicita `GET /api/v1/pastas`
**Then** recebe a lista de pastas com permissão, cada uma com seus documentos `ATIVO`/`ARQUIVADO` (nome, formato)

### Cenário — coleção vazia

**Given** colaborador autenticado sem nenhuma `PERMISSAO_PASTA` compatível com seu Contexto Ativo
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

**Given** documento `ATIVO` ou `ARQUIVADO` cuja pasta tem `PERMISSAO_PASTA` (`TIP_ACESSO=DOWNLOAD`) para algum nível do Contexto Ativo do colaborador
**When** solicita `GET /api/v1/documentos/{id}/download`
**Then** recebe o binário da versão atual (`FLG_VERSAO_ATUAL='S'`) via Object Storage (DEC-013)

### Cenário — documento inexistente

**Given** identificador de documento que não existe
**When** solicita `GET /api/v1/documentos/{id}/download`
**Then** recebe `404`

---

## AT-DOCUMENTO-003 — Negar acesso sem permissão de pasta

### Tipo

Authorization

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-003

### Casos de Uso Relacionados

- UC-DOCUMENTO-003

### Cenário — listagem não vaza pastas sem permissão

**Given** colaborador autenticado com Contexto Ativo (federação F, singular S, área A, equipe E), e existe uma pasta com `PERMISSAO_PASTA` apenas para outra Área (B)
**When** solicita `GET /api/v1/pastas`
**Then** a resposta não contém a pasta restrita à Área B

### Cenário — listagem inclui grant de nível superior

**Given** pasta com `PERMISSAO_PASTA` (`TIP_DESTINATARIO=FEDERACAO`, `COD_DESTINATARIO=F`, `TIP_ACESSO=LEITURA`), colaborador com Contexto Ativo na federação F (área/singular quaisquer dentro dela)
**When** solicita `GET /api/v1/pastas`
**Then** a pasta aparece na listagem — grant de Federação vale para todo colaborador da federação, não só de uma Área específica

### Cenário — download negado

**Given** documento cuja pasta só tem `PERMISSAO_PASTA` para uma Área diferente da do Contexto Ativo do colaborador
**When** solicita `GET /api/v1/documentos/{id}/download`
**Then** recebe `403` explícito — nunca `404` disfarçado nem filtragem silenciosa

---

## AT-DOCUMENTO-004 — Ocultar documentos expirados

### Tipo

Boundary / Business Rule

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-004

### Casos de Uso Relacionados

- UC-DOCUMENTO-004

### Cenário — expirado não aparece na listagem

**Given** pasta com permissão válida contendo um documento `ATIVO` e um `EXPIRADO`
**When** solicita `GET /api/v1/pastas`
**Then** a resposta contém o documento `ATIVO`, não contém o `EXPIRADO`

### Cenário — download de expirado

**Given** documento `EXPIRADO` cuja pasta tem permissão válida
**When** solicita `GET /api/v1/documentos/{id}/download`
**Then** recebe `404` (mesmo com permissão de pasta válida)

### Cenário — arquivado permanece visível

**Given** documento `ARQUIVADO` cuja pasta tem permissão válida
**When** solicita `GET /api/v1/pastas` e `GET /api/v1/documentos/{id}/download`
**Then** o documento aparece na listagem e o download é bem-sucedido

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
| AT-DOCUMENTO-004 | RF-DOCUMENTO-004 | UC-DOCUMENTO-004 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — 3 ATs cobrindo os 3 RFs (somente leitura) |
| 1.2 | 2026-08-26 | Claude Code (Specify) | Reconciliação com schema físico: AT-003 revisado (permissão multi-nível, cenário de grant por Federação), AT-004 novo (ocultar EXPIRADO, cenário ARQUIVADO permanece visível) |
