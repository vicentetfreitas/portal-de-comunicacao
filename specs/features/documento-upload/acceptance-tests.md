# Acceptance Tests

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — só criação) |
| Versão | 1.0 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-UPLOAD |
| Feature | Upload de Arquivos e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Critérios de aceitação do upload de documento. Estratégia de testes, ferramentas e cobertura: `docs/implementation/08-testing-strategy.md` — não duplicado aqui.

---

## AT-DOC-UPLOAD-001 — Upload de arquivo em pasta existente

### Tipo

Happy Path

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-001

### Casos de Uso Relacionados

- UC-DOC-UPLOAD-001

### Cenário — sucesso

**Given** colaborador com atribuição ativa `ADMINISTRADOR` no nível Área, e a pasta alvo tem `PERMISSAO_PASTA` (`TIP_DESTINATARIO='AREA'`, mesmo `COD_DESTINATARIO`, `TIP_ACESSO='EDICAO'`)
**When** solicita `POST /api/v1/pastas/{id}/documentos` com um arquivo e título
**Then** recebe `201`, o documento passa a aparecer em `GET /api/v1/pastas` com `STA_DOCUMENTO='ATIVO'` e a versão atual aponta para o binário enviado

### Cenário — sucesso em outro nível (Federação)

**Given** colaborador com atribuição ativa `ADMINISTRADOR` no nível Federação, e a pasta alvo tem `PERMISSAO_PASTA` (`TIP_DESTINATARIO='FEDERACAO'`, mesma federação, `TIP_ACESSO='EDICAO'`)
**When** solicita `POST /api/v1/pastas/{id}/documentos`
**Then** recebe `201` — grant de Federação vale para qualquer Administrador daquela federação, mesmo padrão multi-nível de `FT-DOCUMENTO`

---

## AT-DOC-UPLOAD-002 — Negar upload sem papel/grant compatível

### Tipo

Authorization

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-002

### Casos de Uso Relacionados

- UC-DOC-UPLOAD-002

### Cenário — colaborador sem papel ADMINISTRADOR

**Given** colaborador com atribuição ativa `COLABORADOR` (não `ADMINISTRADOR`), pasta com `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) compatível com o nível dessa atribuição
**When** solicita `POST /api/v1/pastas/{id}/documentos`
**Then** recebe `403` — papel incompatível, mesmo com grant de pasta existente

### Cenário — ADMINISTRADOR sem grant de edição no nível certo

**Given** colaborador com atribuição ativa `ADMINISTRADOR` no nível Área (área A), pasta cujo único `PERMISSAO_PASTA` com `TIP_ACESSO='EDICAO'` é para a Área B
**When** solicita `POST /api/v1/pastas/{id}/documentos`
**Then** recebe `403` explícito — nunca `404` disfarçado nem sucesso silencioso

### Cenário — ADMINISTRADOR com grant só de leitura (sem EDICAO)

**Given** colaborador com atribuição ativa `ADMINISTRADOR` no nível Área, pasta com `PERMISSAO_PASTA` (`TIP_ACESSO='LEITURA'` apenas, mesmo nível)
**When** solicita `POST /api/v1/pastas/{id}/documentos`
**Then** recebe `403` — `LEITURA` não autoriza upload, só `EDICAO`

---

## AT-DOC-UPLOAD-003 — Upload para pasta inexistente

### Tipo

Negative

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-003

### Casos de Uso Relacionados

- UC-DOC-UPLOAD-003

### Cenário — pasta não existe

**Given** identificador de pasta que não existe
**When** solicita `POST /api/v1/pastas/{id}/documentos`
**Then** recebe `404`

---

# Cenários Negativos (transversais)

- Usuário não autenticado → `401` (padrão corporativo, não redocumentado aqui).
- Usuário autenticado sem Contexto Ativo resolvido → padrão de Primeiro Acesso/Sessão (`FT-PRIMEIRO-ACESSO`/`FT-SESSION`), não redocumentado aqui.
- Falha ao gravar no Object Storage → erro explícito; nenhum `DOCUMENTO`/`DOCUMENTO_VERSAO`/`ARQUIVO_BINARIO` parcial persistido (ver UC-DOC-UPLOAD-001, FE-003) — cenário de integração, não coberto por teste de aceitação isolado nesta entrega.

---

# Matriz de Rastreabilidade

| Teste | RF | UC |
|--------|----|----|
| AT-DOC-UPLOAD-001 | RF-DOC-UPLOAD-001 | UC-DOC-UPLOAD-001 |
| AT-DOC-UPLOAD-002 | RF-DOC-UPLOAD-002 | UC-DOC-UPLOAD-002 |
| AT-DOC-UPLOAD-003 | RF-DOC-UPLOAD-003 | UC-DOC-UPLOAD-003 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 3 ATs cobrindo os 3 RFs |
