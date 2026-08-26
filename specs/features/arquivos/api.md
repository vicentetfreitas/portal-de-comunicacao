# API Contract

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
| Recurso | pastas, documentos |
| Base Path | /api/v1/pastas, /api/v1/documentos |

---

# Objetivo

Contrato funcional da API de leitura de pastas/arquivos. Herda integralmente `docs/implementation/07-api-standards.md` (ApiResponse, PageResponse, ErrorResponse, paginação, autenticação/autorização, auditoria). Este documento só descreve o que é específico desta Feature.

---

# Endpoints

## RF-DOCUMENTO-001 — Listar pastas e arquivos da Área

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/pastas |
| Caso de Uso | UC-DOCUMENTO-001 |

### Objetivo

Listar as pastas vinculadas à Área do Contexto Ativo do colaborador autenticado, cada uma com seus documentos. Área **nunca** é parâmetro de request — sempre derivada do Contexto Ativo da sessão (mesmo padrão de `FT-AREA-COLABORADOR`).

### Response DTO

```
PageResponse<PastaResponse>
```

---

## RF-DOCUMENTO-002 — Baixar arquivo

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/documentos/{id}/download |
| Caso de Uso | UC-DOCUMENTO-002 |

### Objetivo

Retornar o binário do documento via Object Storage (DEC-013). Backend é único consumidor do storage (ADR-004) — nunca retorna URL direta do storage ao cliente.

### Response

Stream binário + `Content-Disposition: attachment` (padrão HTTP, não DTO JSON).

---

## RF-DOCUMENTO-003 — Restringir por Área (transversal)

| Campo | Valor |
|--------|--------|
| Aplica-se a | Ambos os endpoints acima |
| Caso de Uso | UC-DOCUMENTO-003 |

### Regra

Todo `PASTA`/`DOCUMENTO` retornado ou baixado deve pertencer à Área do Contexto Ativo do colaborador. Recurso de Área diferente → `403` explícito (`ErrorResponse` padrão), nunca omissão silenciosa (404 disfarçado) nem filtragem que mascare a existência do recurso para outra Área.

Fundamento de domínio: `BR-012` (`docs/domain/09-business-rules.md`) — Contexto Ativo orienta escopo documental e autorização.

---

# DTOs da Feature

## PastaResponse

| Campo | Tipo | Observação |
|---|---|---|
| id | Long | |
| nome | String | |
| documentos | List\<DocumentoResponse\> | Sem paginação própria — volume esperado baixo por pasta (ver Figma auditado) |

## DocumentoResponse

| Campo | Tipo | Observação |
|---|---|---|
| id | Long | |
| nome | String | Título exibido |
| formato | String | Texto livre (ex. "pptx", "doc e pdf") — Figma não usa enum fechado |
| tamanhoBytes | Long | |

Não expor `CHV_OBJETO_STORAGE` (chave interna do Object Storage) no DTO — nunca URL direta do storage ao cliente (ADR-004).

---

# Regras Específicas da API

- Nenhum endpoint de criação/atualização/exclusão nesta Feature — somente leitura (decisão de produto 2026-08-26, ver `specification.md`).
- `GET /api/v1/pastas` não aceita parâmetro de Área — sempre a do Contexto Ativo.

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| GET /api/v1/pastas | RF-DOCUMENTO-001 | UC-DOCUMENTO-001 | AT-DOCUMENTO-001 |
| GET /api/v1/documentos/{id}/download | RF-DOCUMENTO-002 | UC-DOCUMENTO-002 | AT-DOCUMENTO-002 |
| (transversal, ambos) | RF-DOCUMENTO-003 | UC-DOCUMENTO-003 | AT-DOCUMENTO-003 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — 2 endpoints GET, sem CRUD completo (somente leitura) |
