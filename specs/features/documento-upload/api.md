# API Contract

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — só criação, sem update/delete) |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-UPLOAD |
| Feature | Upload de Arquivos e Documentos |
| Domínio | DOCUMENTO |
| Recurso | documentos (criação, dentro de uma pasta) |
| Base Path | /api/v1/pastas/{id}/documentos |

---

# Objetivo

Contrato funcional do upload de documento. Herda integralmente `docs/implementation/07-api-standards.md` (ApiResponse, ErrorResponse, autenticação/autorização, auditoria) e `specs/features/arquivos/api.md` (DTOs de leitura reaproveitados). Este documento só descreve o que é específico desta Feature.

---

# Endpoints

## RF-DOC-UPLOAD-001 — Upload de arquivo em pasta existente

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/pastas/{id}/documentos |
| Content-Type | `multipart/form-data` |
| Caso de Uso | UC-DOC-UPLOAD-001 |

### Objetivo

Criar um novo `DOCUMENTO` (com sua `DOCUMENTO_VERSAO` inicial e `ARQUIVO_BINARIO`) na pasta `{id}`, gravando o binário no Object Storage (DEC-013). Contexto Ativo **nunca** é parâmetro de request — sempre derivado da sessão (mesmo padrão de `FT-DOCUMENTO`).

### Request (multipart/form-data)

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| arquivo | file | Sim | Binário do documento. Não vazio. `NOM_ARQUIVO`, `TIP_MIME`, `QTD_TAMANHO_BYTES`, `HASH_ARQUIVO` derivados dele. |
| titulo | String | Sim | `DOCUMENTO.TIT_DOCUMENTO` (não em branco) |

**Não há campo de categoria** — `COD_CATEGORIA_DOCUMENTAL` é derivado do `TIP_MIME` no backend (`specification.md` § Categorização por tipo de mídia). **Não há campo de escopo/nível** — resolvido da atribuição ativa.

### Response

```
ApiResponse<DocumentoResponse>
```

`DocumentoResponse` — mesmo DTO já definido em `specs/features/arquivos/api.md` § DTOs (`id`, `nome`, `formato`, `tamanhoBytes`).

### Códigos de Resposta

| Código | Condição |
|---|---|
| 201 | Upload concluído; documento criado |
| 400 | `arquivo` ausente/vazio ou `titulo` ausente/em branco (validação de request — `07-api-standards.md`) |
| 403 | Atribuição ativa sem papel `ADMINISTRADOR`, ou sem `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) compatível com o nível dessa atribuição na pasta `{id}` |
| 404 | Pasta `{id}` não existe |
| 413 | Arquivo acima do teto de tamanho aceito (ver Regras Específicas) |

---

## RF-DOC-UPLOAD-002 — Restringir por papel e grant de edição (transversal)

| Campo | Valor |
|--------|--------|
| Aplica-se a | POST /api/v1/pastas/{id}/documentos |
| Caso de Uso | UC-DOC-UPLOAD-002 |

### Regra

Requer atribuição ativa com `PAPEL.NOM_PAPEL='ADMINISTRADOR'` **e** `PERMISSAO_PASTA` da pasta `{id}` com `TIP_DESTINATARIO`/`COD_DESTINATARIO` correspondente ao nível dessa atribuição (`FEDERACAO`/`SINGULAR`/`AREA`/`EQUIPE`) e `TIP_ACESSO='EDICAO'`. Sem isso → `403` explícito, nunca omissão silenciosa. Ver `specification.md` § Modelo de Autorização.

Fundamento de domínio: `BR-012` (`docs/domain/09-business-rules.md`).

---

## RF-DOC-UPLOAD-003 — Pasta inexistente (transversal)

| Campo | Valor |
|--------|--------|
| Aplica-se a | POST /api/v1/pastas/{id}/documentos |
| Caso de Uso | UC-DOC-UPLOAD-003 |

### Regra

`{id}` sem `PASTA` correspondente → `404`.

---

# DTOs da Feature

Reaproveita `DocumentoResponse` já definido em `specs/features/arquivos/api.md` — nenhum DTO de response novo. Nenhum DTO expõe `URL_ARQUIVO` (ADR-004).

---

# Regras Específicas da API

- Único endpoint novo desta Feature: `POST /api/v1/pastas/{id}/documentos`. Nenhum `PUT`/`PATCH`/`DELETE` — fora de escopo (`specification.md` § Escopo).
- `POST` não aceita parâmetro de escopo/nível — sempre resolvido da atribuição ativa (`JwtAuthenticatedPrincipal.papelAtribuicaoId`).
- **Categoria do `DOCUMENTO`**: sempre derivada de `TIP_MIME` no backend (`Documentos`/`Imagens`/`Vídeos`/`Outros`, resolvida por `NOM_CATEGORIA`) — nunca no request. Ver `specification.md` § Categorização por tipo de mídia.
- **`COD_COLABORADOR`** de `DOCUMENTO` e `DOCUMENTO_VERSAO`: sempre o colaborador autenticado (sessão) — nunca no request.
- Tamanho máximo de arquivo aceito: **a definir em `tasks.md`/implementação** (`specification.md` § Decisão de produto/arquitetura pendente, item 5); acima do teto → `413`.

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| POST /api/v1/pastas/{id}/documentos | RF-DOC-UPLOAD-001 | UC-DOC-UPLOAD-001 | AT-DOC-UPLOAD-001 |
| (transversal) | RF-DOC-UPLOAD-002 | UC-DOC-UPLOAD-002 | AT-DOC-UPLOAD-002 |
| (transversal) | RF-DOC-UPLOAD-003 | UC-DOC-UPLOAD-003 | AT-DOC-UPLOAD-003 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 1 endpoint POST, reaproveitando DTOs de leitura de `FT-DOCUMENTO` |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review: categoria não é parâmetro (derivada de `TIP_MIME`); `COD_COLABORADOR` da sessão; adicionados `400` (validação de request) e `413` (teto de tamanho) |
