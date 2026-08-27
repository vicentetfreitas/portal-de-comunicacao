# API Contract

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — leitura; estende o contrato de `FT-DOCUMENTO`) |
| Versão | 1.0 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-NAVEGACAO |
| Feature | Navegação de Pastas e Documentos |
| Domínio | DOCUMENTO |
| Recurso | pastas (leitura) |
| Base Path | /api/v1/pastas |

---

# Objetivo

Contrato funcional da navegação hierárquica. Herda `docs/implementation/07-api-standards.md`
(ApiResponse, PageResponse, ErrorResponse, autenticação/autorização) e o endpoint de
leitura de `specs/features/arquivos/api.md`. Este documento descreve **só a extensão**
necessária para montar a hierarquia no cliente.

**Depende da decisão D-01 (`decisions.md`).** O contrato abaixo assume a **opção (a)** —
estender `PastaResponse`. Se o Review escolher (b) ou (c), este documento é reescrito.

---

# Endpoints

## RF-DOC-NAV-001/003/006 — Listar pastas visíveis (com hierarquia)

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/pastas |
| Caso de Uso | UC-DOC-NAV-001, UC-DOC-NAV-003, UC-DOC-NAV-006 |
| Mudança | **Sem mudança de comportamento** — continua devolvendo `PageResponse<PastaResponse>` com todas as pastas ativas visíveis ao Contexto Ativo (`RF-DOCUMENTO-001/003`). A resposta ganha os campos novos abaixo. |

O cliente monta o explorador e a árvore a partir dessa lista única (relacionando
`pastaPaiId`). Não há parâmetro de escopo — sempre o Contexto Ativo (`JwtAuthenticatedPrincipal`).

### Códigos

| Código | Condição |
|---|---|
| 200 | Lista (possivelmente vazia) das pastas visíveis |
| 401 | Não autenticado |

---

## RF-DOC-NAV-002 / RF-DOC-NAV-007 — Caminho e visibilidade (transversal)

| Campo | Valor |
|--------|--------|
| Aplica-se a | `GET /api/v1/pastas` e (quando existir) qualquer resolução direta de pasta por id |
| Regra | Toda pasta retornada tem `PERMISSAO_PASTA` (`TIP_ACESSO` incluindo `LEITURA`) compatível com o nível do Contexto Ativo (`RF-DOCUMENTO-003`). O `breadcrumb` é montado no cliente a partir de `pastaPaiId`, parando quando o ancestral não está na lista (raiz órfã). Requisição direta a uma pasta sem grant compatível → `403` explícito. |
| Fundamento | `BR-012`, `BR-018`, `BR-020` |

---

# DTOs da Feature

## PastaResponse — **estendido**

Estende o `PastaResponse` de `specs/features/arquivos/api.md` (aditivo, retrocompatível):

| Campo | Tipo | Novo? | Observação |
|---|---|---|---|
| id | Long | — | `COD_PASTA` |
| nome | String | — | `NOM_PASTA` |
| documentos | List\<DocumentoResponse\> | — | do nível daquela pasta; já filtrados por `STA_DOCUMENTO != EXPIRADO` |
| **pastaPaiId** | Long \| null | ✅ | `COD_PASTA_PAI`. `null` = raiz sem pai. Se o pai não estiver na lista (não visível) → o cliente trata como **raiz órfã**. |
| **dataAtualizacao** | Instant \| null | ✅ | `DAT_ATUALIZACAO` (data exibida no card do mock legado) |
| descricao | String \| null | ✅ (opcional) | `DSC_PASTA` — a incluir só se a UI exibir |

`DocumentoResponse` — inalterado (`id`, `nome`, `formato`, `tamanhoBytes`).
Nenhum DTO expõe `URL_ARQUIVO` (ADR-004).

---

# Regras Específicas da API

- Nenhum endpoint novo no MVP (opção D-01(a)). O explorador, a árvore, o `breadcrumb`
  e a busca são montados no cliente a partir de `GET /api/v1/pastas`.
- A extensão de `PastaResponse` é **aditiva** — clientes de `FT-DOCUMENTO` que ignoram
  os campos novos continuam funcionando.
- Busca (D-04): no MVP é client-side; **não** há endpoint de busca nesta versão.
- Se D-01 evoluir para (c) (lazy por nível): `GET /api/v1/pastas?pastaPaiId={id}`
  (ausente = raízes) — contrato a detalhar nessa iteração.

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| GET /api/v1/pastas (+ `pastaPaiId`, `dataAtualizacao`) | RF-DOC-NAV-001, RF-DOC-NAV-003, RF-DOC-NAV-006 | UC-DOC-NAV-001/003/006 | AT-DOC-NAV-001/003/006 |
| (transversal) | RF-DOC-NAV-002, RF-DOC-NAV-007 | UC-DOC-NAV-002/007 | AT-DOC-NAV-002/007 |
| (sem API — cliente) | RF-DOC-NAV-004, RF-DOC-NAV-005 | UC-DOC-NAV-004/005 | AT-DOC-NAV-004/005 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — assume D-01(a): `PastaResponse` + `pastaPaiId`/`dataAtualizacao`, sem endpoint novo. A revisar se D-01 mudar. |
