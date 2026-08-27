# API Contract

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — POST/PATCH/DELETE sobre pastas e documentos) |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-GESTAO |
| Feature | Gestão de Pastas e Documentos |
| Domínio | DOCUMENTO |
| Recurso | pastas, documentos (escrita) |
| Base Path | /api/v1/pastas, /api/v1/documentos |

---

# Objetivo

Contrato funcional da manutenção de pastas e documentos. Herda integralmente
`docs/implementation/07-api-standards.md` (ApiResponse, ErrorResponse,
autenticação/autorização, auditoria) e `specs/features/arquivos/api.md` (DTOs de
leitura reaproveitados — `PastaResponse`, `DocumentoResponse`). Este documento só
descreve o que é específico desta Feature.

Regras transversais a **todos** os endpoints:

- Contexto Ativo **nunca** é parâmetro de request — sempre derivado da sessão
  (`JwtAuthenticatedPrincipal`).
- Autorização: atribuição ativa `ADMINISTRADOR` + `PERMISSAO_PASTA`
  (`TIP_ACESSO='EDICAO'`) compatível com o nível dessa atribuição na(s) pasta(s)
  alvo (`specification.md` § Modelo de Autorização). Sem isso → `403` explícito.
- Estado do recurso: ver `specification.md` § Regras transversais de estado do
  recurso (pasta `FLG_ATIVO='N'` → `404` para escrita; documento não-`ATIVO` → `409`
  para nova versão/editar/mover; "já arquivado" → `409`).
- `401` (não autenticado) e Primeiro Acesso/Contexto Ativo não resolvido — padrão
  corporativo, não redocumentado aqui.
- `COD_COLABORADOR` (autor de versão) sempre da sessão, nunca do request.
- Teto de tamanho de arquivo: `spring.servlet.multipart.max-file-size` = `25MB`
  (override `DOCUMENTO_MAX_FILE_SIZE`, herdado de `FT-DOCUMENTO-UPLOAD`); acima → `413`.
- `PATCH` é atualização parcial: campo ausente **ou `null`** = "não alterar";
  **`descricao: ""` (string vazia) = limpar** (`DSC_*` para `NULL`). Implementação
  (2026-08-27): sem dependência de `JsonNullable`, `null` explícito não é distinguível
  de ausente — a limpeza usa string vazia. `nome`/`titulo` enviados em branco → `400`.
- `DELETE` de pasta e de documento são **soft** (nunca `DELETE` físico de linha).

---

# Endpoints — Gestão de pastas

## RF-DOC-GESTAO-001 — Criar subpasta

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/pastas/{id}/subpastas |
| Content-Type | `application/json` |
| Caso de Uso | UC-DOC-GESTAO-001 |

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
| 404 | Pasta `{id}` não existe ou está `FLG_ATIVO='N'` |

---

## RF-DOC-GESTAO-002 / RF-DOC-GESTAO-003 — Renomear e/ou mover pasta

| Campo | Valor |
|--------|--------|
| Método | PATCH |
| Endpoint | /api/v1/pastas/{id} |
| Content-Type | `application/json` |
| Casos de Uso | UC-DOC-GESTAO-002, UC-DOC-GESTAO-003 |

### Objetivo

Atualização parcial da pasta `{id}` (ativa). Ao menos um campo. A presença de
`codPastaPai` dispara as validações extras de **mover** (grant também na pasta
destino + prevenção de ciclo).

### Request

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| nome | String | Não* | `NOM_PASTA` — não em branco quando presente |
| descricao | String | Não* | `DSC_PASTA` |
| codPastaPai | Long | Não* | Nova pasta-pai (mover). Ativa. Não pode ser `{id}` nem um descendente de `{id}`. |

\* pelo menos um dos três.

### Response

`ApiResponse<PastaResponse>` — a pasta atualizada.

### Códigos

| Código | Condição |
|---|---|
| 200 | Pasta atualizada |
| 400 | Nenhum campo enviado, ou `nome` em branco |
| 403 | Sem papel `ADMINISTRADOR` ativo; sem grant `EDICAO` na pasta `{id}`; ou (com `codPastaPai`) sem grant `EDICAO` na pasta destino |
| 404 | Pasta `{id}` ou `codPastaPai` não existe ou está `FLG_ATIVO='N'` |
| 409 | `codPastaPai` = `{id}` ou descendente de `{id}` (ciclo) |

---

## RF-DOC-GESTAO-004 — Arquivar pasta (soft-delete)

| Campo | Valor |
|--------|--------|
| Método | DELETE |
| Endpoint | /api/v1/pastas/{id} |
| Caso de Uso | UC-DOC-GESTAO-004 |

### Objetivo

Marcar `FLG_ATIVO='N'`. Não remove linha. Não apaga binários nem documentos.

### Response

`204 No Content`.

### Códigos

| Código | Condição |
|---|---|
| 204 | Pasta arquivada |
| 403 | Sem papel `ADMINISTRADOR` ativo ou sem grant `EDICAO` na pasta `{id}` |
| 404 | Pasta `{id}` não existe |
| 409 | Pasta tem subpasta ativa ou documento `ATIVO`/`ARQUIVADO`; **ou** pasta já `FLG_ATIVO='N'` |

---

# Endpoints — Gestão de documentos já enviados

## RF-DOC-GESTAO-005 — Enviar nova versão

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/documentos/{id}/versoes |
| Content-Type | `multipart/form-data` |
| Caso de Uso | UC-DOC-GESTAO-005 |

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
| 404 | Documento `{id}` não existe (ou está em pasta `FLG_ATIVO='N'`) |
| 409 | Documento não está `ATIVO` (`ARQUIVADO`/`EXPIRADO`) |
| 413 | Arquivo acima do teto |

---

## RF-DOC-GESTAO-006 / RF-DOC-GESTAO-008 — Editar metadados e/ou mover documento

| Campo | Valor |
|--------|--------|
| Método | PATCH |
| Endpoint | /api/v1/documentos/{id} |
| Content-Type | `application/json` |
| Casos de Uso | UC-DOC-GESTAO-006, UC-DOC-GESTAO-008 |

### Objetivo

Atualização parcial do documento `{id}` (`ATIVO`). Ao menos um campo. A presença de
`codPasta` dispara as validações de **mover** (grant também na pasta destino, que
deve estar ativa).

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
| 403 | Sem papel `ADMINISTRADOR` ativo; sem grant `EDICAO` na pasta atual do documento; ou (com `codPasta`) sem grant `EDICAO` na pasta destino |
| 404 | Documento `{id}` ou `codPasta` não existe ou está `FLG_ATIVO='N'` |
| 409 | Documento não está `ATIVO` (`ARQUIVADO`/`EXPIRADO`) |

---

## RF-DOC-GESTAO-007 — Arquivar documento

| Campo | Valor |
|--------|--------|
| Método | DELETE |
| Endpoint | /api/v1/documentos/{id} |
| Caso de Uso | UC-DOC-GESTAO-007 |

### Objetivo

`STA_DOCUMENTO='ATIVO' → 'ARQUIVADO'`. Sem exclusão física/lógica (`decisions.md`
D-06). Documento arquivado continua na leitura de `FT-DOCUMENTO`.

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

DTOs de request (JSON): `CriarSubpastaRequest`, `AtualizarPastaRequest`,
`AtualizarDocumentoRequest` — nomes finais na implementação; `07-api-standards.md`
rege validação (`400`).

**Observação:** `PastaResponse` (de `FT-DOCUMENTO`) hoje expõe `id`/`nome`/
`documentos`. Se o consumo do frontend precisar de `codPastaPai`/`descricao` no
retorno de criar/atualizar pasta, é extensão do DTO em `FT-DOCUMENTO` (fora do
escopo desta Feature) — alternativa: o frontend recarrega a listagem.

---

# Regras Específicas da API

- Nenhum endpoint aceita parâmetro de escopo/nível — sempre da atribuição ativa.
- Nenhum endpoint aceita `COD_COLABORADOR`, categoria, `NUM_VERSAO` ou
  `FLG_VERSAO_ATUAL` no request — todos derivados/calculados no backend.
- Mover (pasta ou documento) **não** cria/copia/remove `PERMISSAO_PASTA`
  (`decisions.md` D-08).
- Upload de documento novo é `POST /api/v1/pastas/{id}/documentos`
  (`FT-DOCUMENTO-UPLOAD`) — não redefinido aqui.

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| POST /api/v1/pastas/{id}/subpastas | RF-DOC-GESTAO-001 | UC-DOC-GESTAO-001 | AT-DOC-GESTAO-001 |
| PATCH /api/v1/pastas/{id} | RF-DOC-GESTAO-002, RF-DOC-GESTAO-003 | UC-DOC-GESTAO-002, UC-DOC-GESTAO-003 | AT-DOC-GESTAO-002, AT-DOC-GESTAO-003 |
| DELETE /api/v1/pastas/{id} | RF-DOC-GESTAO-004 | UC-DOC-GESTAO-004 | AT-DOC-GESTAO-004 |
| POST /api/v1/documentos/{id}/versoes | RF-DOC-GESTAO-005 | UC-DOC-GESTAO-005 | AT-DOC-GESTAO-005 |
| PATCH /api/v1/documentos/{id} | RF-DOC-GESTAO-006, RF-DOC-GESTAO-008 | UC-DOC-GESTAO-006, UC-DOC-GESTAO-008 | AT-DOC-GESTAO-006, AT-DOC-GESTAO-008 |
| DELETE /api/v1/documentos/{id} | RF-DOC-GESTAO-007 | UC-DOC-GESTAO-007 | AT-DOC-GESTAO-007 |
| (transversal) | RF-DOC-GESTAO-009, RF-DOC-GESTAO-010 | UC-DOC-GESTAO-009, UC-DOC-GESTAO-010 | AT-DOC-GESTAO-009, AT-DOC-GESTAO-010 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 6 endpoints (POST subpastas, PATCH/DELETE pastas, POST versoes, PATCH/DELETE documentos) + 2 RFs transversais; extraído da proposta "Fase 2" de `FT-DOCUMENTO-UPLOAD` |
| 1.1 | 2026-08-27 | Claude Code (Implement) | Clarificação de implementação: `PATCH` sem `JsonNullable` — `descricao` limpa via string vazia (`""`), não `null` explícito. Header → APPROVED (Review de Spec + IMPLEMENTING). |
