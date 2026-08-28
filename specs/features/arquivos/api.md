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

Listar as pastas com `PERMISSAO_PASTA` (`TIP_ACESSO=LEITURA`) para algum nível do Contexto Ativo do colaborador autenticado (federação, singular, área ou equipe), cada uma com seus documentos `ATIVO`/`ARQUIVADO`. Contexto Ativo **nunca** é parâmetro de request — sempre derivado da sessão (`JwtAuthenticatedPrincipal`, mesmo padrão de `FT-AREA-COLABORADOR`/`PrimeiroAcessoController`).

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

Retornar o binário da versão atual do documento (`DOCUMENTO_VERSAO.FLG_VERSAO_ATUAL='S'` → `ARQUIVO_BINARIO.URL_ARQUIVO`) via Object Storage (DEC-013). Backend é único consumidor do storage (ADR-004) — nunca retorna `URL_ARQUIVO` diretamente ao cliente. Documento `EXPIRADO` (RF-DOCUMENTO-004) → 404.

### Response

Stream binário + `Content-Disposition: attachment` (padrão HTTP, não DTO JSON).

---

## RF-DOCUMENTO-003 — Restringir por Permissão de Pasta (transversal)

| Campo | Valor |
|--------|--------|
| Aplica-se a | Ambos os endpoints acima |
| Caso de Uso | UC-DOCUMENTO-003 |

### Regra

Todo `PASTA`/`DOCUMENTO` retornado ou baixado deve ter `PERMISSAO_PASTA` com `TIP_DESTINATARIO` em (`FEDERACAO`, `SINGULAR`, `AREA`, `EQUIPE`) cujo `COD_DESTINATARIO` corresponda ao nível equivalente do Contexto Ativo (`federationId`/`singularId`/`areaId`/`teamId`), e `TIP_ACESSO` incluindo `LEITURA` (listagem) ou `DOWNLOAD` (download). Sem grant compatível → `403` explícito (`ErrorResponse` padrão), nunca omissão silenciosa (404 disfarçado). `TIP_DESTINATARIO=COLABORADOR` e `FLG_HERDA_PERMISSAO` fora de escopo nesta entrega (ver `specification.md` § Escopo).

Fundamento de domínio: `BR-012`, `BR-018`, `BR-020` (`docs/domain/09-business-rules.md`).

---

## RF-DOCUMENTO-004 — Ocultar documentos expirados (transversal)

| Campo | Valor |
|--------|--------|
| Aplica-se a | Ambos os endpoints acima |
| Caso de Uso | UC-DOCUMENTO-004 |

### Regra

Documento com `STA_DOCUMENTO='EXPIRADO'` nunca aparece em `GET /api/v1/pastas` nem é servido por `GET /api/v1/documentos/{id}/download` (404 nesse caso). `ATIVO` e `ARQUIVADO` permanecem visíveis.

---

# DTOs da Feature

## PastaResponse

| Campo | Tipo | Observação |
|---|---|---|
| id | Long | `COD_PASTA` |
| nome | String | `NOM_PASTA` |
| documentos | List\<DocumentoResponse\> | Sem paginação própria — volume esperado baixo por pasta (ver Figma auditado); já filtrados por `STA_DOCUMENTO != EXPIRADO` |

## DocumentoResponse

| Campo | Tipo | Observação |
|---|---|---|
| id | Long | `COD_DOCUMENTO` |
| nome | String | `TIT_DOCUMENTO` |
| formato | String | Derivado de `ARQUIVO_BINARIO.TIP_MIME` da versão atual (`FLG_VERSAO_ATUAL='S'`) — exibido como texto livre (ex. "pptx"), não enum fechado |
| tamanhoBytes | Long | `ARQUIVO_BINARIO.QTD_TAMANHO_BYTES` da versão atual |

Não expor `URL_ARQUIVO` (referência interna ao Object Storage) no DTO — nunca URL direta do storage ao cliente (ADR-004).

---

# Regras Específicas da API

- Nenhum endpoint de criação/atualização/exclusão nesta Feature — somente leitura (decisão de produto 2026-08-26, ver `specification.md`).
- `GET /api/v1/pastas` não aceita parâmetro de escopo — sempre resolvido do Contexto Ativo (`JwtAuthenticatedPrincipal`).
- Nenhum parâmetro de versão em `GET /api/v1/documentos/{id}/download` — sempre a versão atual (`FLG_VERSAO_ATUAL='S'`); versões antigas fora de escopo desta entrega.

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| GET /api/v1/pastas | RF-DOCUMENTO-001 | UC-DOCUMENTO-001 | AT-DOCUMENTO-001 |
| GET /api/v1/documentos/{id}/download | RF-DOCUMENTO-002 | UC-DOCUMENTO-002 | AT-DOCUMENTO-002 |
| (transversal, ambos) | RF-DOCUMENTO-003 | UC-DOCUMENTO-003 | AT-DOCUMENTO-003 |
| (transversal, ambos) | RF-DOCUMENTO-004 | UC-DOCUMENTO-004 | AT-DOCUMENTO-004 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — 2 endpoints GET, sem CRUD completo (somente leitura) |
| 1.2 | 2026-08-26 | Claude Code (Specify) | Reconciliação com schema físico: RF-003 revisado (permissão multi-nível), RF-004 novo (ocultar EXPIRADO), DTOs mapeados às colunas reais |
