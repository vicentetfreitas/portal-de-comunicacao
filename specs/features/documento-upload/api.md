# API Contract

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Fase 1: POST; Fase 2: POST/PATCH/DELETE) |
| Versão | 2.0 |
| Status | DRAFT (Fase 2 — aguarda Review de Spec) |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-UPLOAD |
| Feature | Upload de Arquivos e Documentos |
| Domínio | DOCUMENTO |
| Recurso | pastas, documentos (escrita) |
| Base Path | /api/v1/pastas, /api/v1/documentos |

---

# Objetivo

Contrato funcional da escrita em Gestão Documental. Herda integralmente
`docs/implementation/07-api-standards.md` (ApiResponse, ErrorResponse,
autenticação/autorização, auditoria) e `specs/features/arquivos/api.md` (DTOs de
leitura reaproveitados — `PastaResponse`, `DocumentoResponse`). Este documento só
descreve o que é específico desta Feature.

Regras transversais a **todos** os endpoints abaixo:

- Contexto Ativo **nunca** é parâmetro de request — sempre derivado da sessão
  (`JwtAuthenticatedPrincipal`).
- Autorização: atribuição ativa `ADMINISTRADOR` + `PERMISSAO_PASTA`
  (`TIP_ACESSO='EDICAO'`) compatível com o nível dessa atribuição na(s) pasta(s)
  alvo (`specification.md` § Modelo de Autorização). Sem isso → `403` explícito.
- `401` (não autenticado) e Primeiro Acesso/Contexto Ativo não resolvido — padrão
  corporativo, não redocumentado aqui.
- `COD_COLABORADOR` (autor de documento/versão) sempre da sessão, nunca do request.
- Teto de tamanho de arquivo: `spring.servlet.multipart.max-file-size` = `25MB`
  (override `DOCUMENTO_MAX_FILE_SIZE`); acima → `413`.

---

# Endpoints — [Fase 1]

## RF-DOC-UPLOAD-001 — Upload de arquivo em pasta existente

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/pastas/{id}/documentos |
| Content-Type | `multipart/form-data` |
| Caso de Uso | UC-DOC-UPLOAD-001 |

### Request (multipart/form-data)

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| arquivo | file | Sim | Binário. Não vazio. `NOM_ARQUIVO`/`TIP_MIME`/`QTD_TAMANHO_BYTES`/`HASH_ARQUIVO` derivados dele. |
| titulo | String | Sim | `DOCUMENTO.TIT_DOCUMENTO` (não em branco) |

Sem campo de categoria (derivada do `TIP_MIME`) nem de escopo (da atribuição ativa).

### Response

`ApiResponse<DocumentoResponse>` (DTO de `specs/features/arquivos/api.md`).

### Códigos

| Código | Condição |
|---|---|
| 201 | Documento criado |
| 400 | `arquivo` ausente/vazio ou `titulo` ausente/em branco |
| 403 | Sem papel `ADMINISTRADOR` ativo ou sem grant `EDICAO` na pasta `{id}` |
| 404 | Pasta `{id}` não existe |
| 413 | Arquivo acima do teto |

---

# Endpoints — [Fase 2] Gestão de pastas

## RF-DOC-UPLOAD-004 — Criar subpasta

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/pastas/{id}/subpastas |
| Content-Type | `application/json` |
| Caso de Uso | UC-DOC-UPLOAD-004 |

### Objetivo

Criar uma `PASTA` filha de `{id}` e copiar para ela, na mesma transação, todas as
linhas de `PERMISSAO_PASTA` de `{id}` (snapshot — `decisions.md` D-04).

### Request

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| nome | String | Sim | `NOM_PASTA` (não em branco, ≤ 200 chars) |
| descricao | String | Não | `DSC_PASTA` |

### Response

`ApiResponse<PastaResponse>` — a subpasta criada (`documentos` vazio).

### Códigos

| Código | Condição |
|---|---|
| 201 | Subpasta criada; grants da pasta-pai copiados |
| 400 | `nome` ausente/em branco ou acima do limite |
| 403 | Sem papel `ADMINISTRADOR` ativo ou sem grant `EDICAO` na pasta-pai `{id}` |
| 404 | Pasta `{id}` não existe |

---

## RF-DOC-UPLOAD-005 / RF-DOC-UPLOAD-006 — Renomear e/ou mover pasta

| Campo | Valor |
|--------|--------|
| Método | PATCH |
| Endpoint | /api/v1/pastas/{id} |
| Content-Type | `application/json` |
| Casos de Uso | UC-DOC-UPLOAD-005, UC-DOC-UPLOAD-006 |

### Objetivo

Atualização parcial da pasta `{id}`. Ao menos um campo deve estar presente. A
presença de `codPastaPai` dispara as validações extras de **mover** (grant também na
pasta destino + prevenção de ciclo).

### Request

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| nome | String | Não* | `NOM_PASTA` — não em branco quando presente |
| descricao | String | Não* | `DSC_PASTA` |
| codPastaPai | Long | Não* | Nova pasta-pai (mover). Não pode ser `{id}` nem um descendente de `{id}`. |

\* pelo menos um dos três.

### Response

`ApiResponse<PastaResponse>` — a pasta atualizada.

### Códigos

| Código | Condição |
|---|---|
| 200 | Pasta atualizada |
| 400 | Nenhum campo enviado, ou `nome` em branco |
| 403 | Sem papel `ADMINISTRADOR` ativo; sem grant `EDICAO` na pasta `{id}`; ou (quando `codPastaPai` presente) sem grant `EDICAO` na pasta destino |
| 404 | Pasta `{id}` ou `codPastaPai` não existe |
| 409 | `codPastaPai` = `{id}` ou descendente de `{id}` (ciclo) |

---

## RF-DOC-UPLOAD-007 — Arquivar pasta (soft-delete)

| Campo | Valor |
|--------|--------|
| Método | DELETE |
| Endpoint | /api/v1/pastas/{id} |
| Caso de Uso | UC-DOC-UPLOAD-007 |

### Objetivo

Marcar `FLG_ATIVO='N'`. Não remove linha. Não apaga binários nem documentos.

### Response

`204 No Content`.

### Códigos

| Código | Condição |
|---|---|
| 204 | Pasta arquivada |
| 403 | Sem papel `ADMINISTRADOR` ativo ou sem grant `EDICAO` na pasta `{id}` |
| 404 | Pasta `{id}` não existe (ou já `FLG_ATIVO='N'` — tratada como inexistente para escrita) |
| 409 | Pasta tem subpasta ativa ou documento `ATIVO`/`ARQUIVADO` |

---

# Endpoints — [Fase 2] Gestão de documentos já enviados

## RF-DOC-UPLOAD-008 — Enviar nova versão

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/documentos/{id}/versoes |
| Content-Type | `multipart/form-data` |
| Caso de Uso | UC-DOC-UPLOAD-008 |

### Request (multipart/form-data)

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| arquivo | file | Sim | Novo binário. Não vazio. |
| descricaoAlteracao | String | Não | `DOCUMENTO_VERSAO.DSC_ALTERACAO` |

### Comportamento

Cria `ARQUIVO_BINARIO` + `DOCUMENTO_VERSAO` (`NUM_VERSAO` = atual+1,
`FLG_VERSAO_ATUAL='S'`, `COD_COLABORADOR` da sessão); rebaixa a versão anterior
(`FLG_VERSAO_ATUAL='N'`); re-deriva `DOCUMENTO.COD_CATEGORIA_DOCUMENTAL` do novo
`TIP_MIME`. Atômico; storage por último.

### Response

`ApiResponse<DocumentoResponse>` — o documento com a versão atual já apontando para
o novo binário.

### Códigos

| Código | Condição |
|---|---|
| 201 | Nova versão criada |
| 400 | `arquivo` ausente/vazio |
| 403 | Sem papel `ADMINISTRADOR` ativo ou sem grant `EDICAO` na pasta do documento |
| 404 | Documento `{id}` não existe |
| 409 | Documento não está `ATIVO` (`ARQUIVADO`/`EXPIRADO`) |
| 413 | Arquivo acima do teto |

---

## RF-DOC-UPLOAD-009 / RF-DOC-UPLOAD-011 — Editar metadados e/ou mover documento

| Campo | Valor |
|--------|--------|
| Método | PATCH |
| Endpoint | /api/v1/documentos/{id} |
| Content-Type | `application/json` |
| Casos de Uso | UC-DOC-UPLOAD-009, UC-DOC-UPLOAD-011 |

### Objetivo

Atualização parcial do documento `{id}`. Ao menos um campo. A presença de `codPasta`
dispara as validações de **mover** (grant também na pasta destino, que deve estar
ativa).

### Request

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| titulo | String | Não* | `TIT_DOCUMENTO` — não em branco quando presente |
| descricao | String | Não* | `DSC_DOCUMENTO` |
| codPasta | Long | Não* | Nova pasta do documento (mover) — `FLG_ATIVO='S'` |

\* pelo menos um dos três. Não altera binário, versões nem categoria.

### Response

`ApiResponse<DocumentoResponse>`.

### Códigos

| Código | Condição |
|---|---|
| 200 | Documento atualizado |
| 400 | Nenhum campo enviado, ou `titulo` em branco |
| 403 | Sem papel `ADMINISTRADOR` ativo; sem grant `EDICAO` na pasta atual do documento; ou (quando `codPasta` presente) sem grant `EDICAO` na pasta destino |
| 404 | Documento `{id}` ou `codPasta` não existe |

---

## RF-DOC-UPLOAD-010 — Arquivar documento

| Campo | Valor |
|--------|--------|
| Método | DELETE |
| Endpoint | /api/v1/documentos/{id} |
| Caso de Uso | UC-DOC-UPLOAD-010 |

### Objetivo

`STA_DOCUMENTO='ATIVO' → 'ARQUIVADO'`. Sem exclusão física/lógica nesta fase
(`decisions.md` D-06). Documento arquivado continua na leitura de `FT-DOCUMENTO`.

### Response

`204 No Content`.

### Códigos

| Código | Condição |
|---|---|
| 204 | Documento arquivado |
| 403 | Sem papel `ADMINISTRADOR` ativo ou sem grant `EDICAO` na pasta do documento |
| 404 | Documento `{id}` não existe |
| 409 | Documento já `ARQUIVADO`/`EXPIRADO` |

---

# DTOs da Feature

Nenhum DTO de response novo — reaproveita `PastaResponse` e `DocumentoResponse` de
`specs/features/arquivos/api.md`. Nenhum DTO expõe `URL_ARQUIVO` (ADR-004).

DTOs de request (JSON) são os descritos acima por endpoint (`CriarSubpastaRequest`,
`AtualizarPastaRequest`, `AtualizarDocumentoRequest`) — nomes finais definidos na
implementação; `07-api-standards.md` rege validação (`400`).

---

# Regras Específicas da API

- `PATCH` é atualização parcial (semântica merge) — campo ausente = "não alterar";
  `descricao: null` explícito = limpar (`DSC_*` para `NULL`).
- Nenhum endpoint aceita parâmetro de escopo/nível — sempre da atribuição ativa.
- Nenhum endpoint aceita `COD_COLABORADOR`, categoria, `NUM_VERSAO` ou
  `FLG_VERSAO_ATUAL` no request — todos derivados/calculados no backend.
- `DELETE` de pasta e de documento são **soft** (nunca `DELETE` físico de linha).
- Mover (pasta ou documento) **não** cria/copia/remove `PERMISSAO_PASTA`
  (`decisions.md` D-08).

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| POST /api/v1/pastas/{id}/documentos | RF-DOC-UPLOAD-001 | UC-DOC-UPLOAD-001 | AT-DOC-UPLOAD-001 |
| (transversal — toda escrita) | RF-DOC-UPLOAD-002 | UC-DOC-UPLOAD-002 | AT-DOC-UPLOAD-002 |
| (transversal — toda escrita) | RF-DOC-UPLOAD-003 | UC-DOC-UPLOAD-003 | AT-DOC-UPLOAD-003 |
| POST /api/v1/pastas/{id}/subpastas | RF-DOC-UPLOAD-004 | UC-DOC-UPLOAD-004 | AT-DOC-UPLOAD-004 |
| PATCH /api/v1/pastas/{id} | RF-DOC-UPLOAD-005, RF-DOC-UPLOAD-006 | UC-DOC-UPLOAD-005, UC-DOC-UPLOAD-006 | AT-DOC-UPLOAD-005, AT-DOC-UPLOAD-006 |
| DELETE /api/v1/pastas/{id} | RF-DOC-UPLOAD-007 | UC-DOC-UPLOAD-007 | AT-DOC-UPLOAD-007 |
| POST /api/v1/documentos/{id}/versoes | RF-DOC-UPLOAD-008 | UC-DOC-UPLOAD-008 | AT-DOC-UPLOAD-008 |
| PATCH /api/v1/documentos/{id} | RF-DOC-UPLOAD-009, RF-DOC-UPLOAD-011 | UC-DOC-UPLOAD-009, UC-DOC-UPLOAD-011 | AT-DOC-UPLOAD-009, AT-DOC-UPLOAD-011 |
| DELETE /api/v1/documentos/{id} | RF-DOC-UPLOAD-010 | UC-DOC-UPLOAD-010 | AT-DOC-UPLOAD-010 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 1 endpoint POST (Fase 1) |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review — categoria não é parâmetro; `400`/`413` |
| 2.0 | 2026-08-27 | Claude Code (Specify) | Fase 2 (DRAFT): 6 endpoints novos (POST subpastas, PATCH/DELETE pastas, POST versoes, PATCH/DELETE documentos); regras transversais consolidadas no topo |
