# Feature Specification — Camada Frontend

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.0 |
| Status | APPROVED |
| Owner | Engineering Framework |
| Camada | Frontend (Administrativa) |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-COLABORADOR |
| Feature | Colaborador |
| Domínio | COLABORADOR |
| Workstream | Frontend |
| Especificação backend | `specification.md` (APPROVED) |

---

# Objetivo

Disponibilizar ao **Administrador** autenticado CRUD administrativo de **Colaboradores** com vínculo organizacional, consumindo `/api/v1/colaboradores` e APIs auxiliares (`singulares`, `areas`, `equipes`, listagem de colaboradores para gestor).

Padrão de UI: **FT-SINGULAR** / **FT-EQUIPE** (hub, listagem, formulários, detalhe, status, E2E com mocks).

**Referência consultiva:** `docs/discovery/frontend-feature-mapping.md` (FT-COLABORADOR — MVP administrativo; fora de escopo onboarding, convidados e telas legadas com `:colaborador_slug`).

---

# Escopo

## Incluído

- Rotas `/app/administrador/colaboradores`
- Hub, cadastro, listagem paginada, detalhe, edição (e-mail somente leitura na edição)
- Filtros alinhados à API: `status`, `singularId`, `areaId`, `teamId`, `name`, `email`
- Ativação/inativação com confirmação (RN-008)
- Erros HTTP 400, 401, 403, 404, 422 em português
- i18n `colaborador.*`
- AT-FE-COLABORADOR-001 a **005** (Playwright)

## Fora do Escopo

- Onboarding, vínculo convidado, exclusão física
- Rotas legadas `/app/administrador/:colaborador_slug/...`
- Alteração de contratos backend

---

# Navegação e Rotas

| Rota | Tela | RF-FE |
|------|------|-------|
| `/app/administrador/colaboradores` | Hub | — |
| `/app/administrador/colaboradores/lista` | Listagem | RF-FE-COLABORADOR-003 |
| `/app/administrador/colaboradores/novo` | Cadastro | RF-FE-COLABORADOR-001 |
| `/app/administrador/colaboradores/:id` | Detalhe | RF-FE-COLABORADOR-002 |
| `/app/administrador/colaboradores/:id/editar` | Edição | RF-FE-COLABORADOR-004 |

---

# Requisitos Funcionais (Frontend)

## RF-FE-COLABORADOR-001 — Cadastrar na UI

Formulário com `POST /api/v1/colaboradores`. Campos conforme `api.md` (`federationId` padrão seed até FT-FEDERACAO).

## RF-FE-COLABORADOR-002 — Detalhe

`GET /api/v1/colaboradores/{id}`.

## RF-FE-COLABORADOR-003 — Listagem

`GET /api/v1/colaboradores` com paginação e filtros.

## RF-FE-COLABORADOR-004 — Edição

`PUT /api/v1/colaboradores/{id}` — sem alterar e-mail (RN-009).

## RF-FE-COLABORADOR-005 — Status

`PATCH /api/v1/colaboradores/{id}/status`.

---

# Critérios de Aceite Frontend

| ID | Backend |
|----|---------|
| AT-FE-COLABORADOR-001 | AT-COLABORADOR-001 |
| AT-FE-COLABORADOR-002 | AT-COLABORADOR-002 |
| AT-FE-COLABORADOR-003 | AT-COLABORADOR-003 |
| AT-FE-COLABORADOR-004 | AT-COLABORADOR-004 |
| AT-FE-COLABORADOR-005 | AT-COLABORADOR-005 |
