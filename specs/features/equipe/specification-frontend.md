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
| Feature ID | FT-EQUIPE |
| Feature | Equipe |
| Domínio | EQUIPE |
| Workstream | Frontend |
| Especificação backend | `specification.md` (APPROVED) |

---

# Objetivo

Disponibilizar ao **Administrador** autenticado uma experiência web para cadastro, consulta, listagem, edição e alteração de status de **Equipes**, consumindo a API REST `/api/v1/equipes` já implementada no backend.

A UI segue o padrão administrativo estabelecido em **FT-SINGULAR** (hub, listagem, formulários, detalhe, diálogo de status, testes E2E com mocks de API).

**Referência consultiva:** `docs/discovery/frontend-feature-mapping.md` (FT-EQUIPE — escopo MVP administrativo, sem telas legadas de membros ou documentos por equipe).

---

# Escopo

## Incluído

- Rotas administrativas sob `/app/administrador/equipes`
- Hub com atalhos para listagem e cadastro
- Formulário de cadastro (`areaId`, `name`, `description?`, `leaderId?`)
- Formulário de edição (`name`, `description?`, `leaderId?`; `areaId` somente leitura)
- Listagem paginada com filtros alinhados à API (`status`, `areaId`, `name`)
- Página de detalhe com ações para editar e alterar status
- Diálogo de confirmação para ativação/inativação
- Tratamento de erros HTTP 400, 401, 403, 404 e 422 com mensagens em português (corpo `ApiResponse` / regras de negócio)
- Internacionalização (`pt-BR`) para rótulos e mensagens da Feature
- Critérios de aceite frontend **AT-FE-EQUIPE-001** a **AT-FE-EQUIPE-005** (Playwright, mocks de auth e API)

## Fora do Escopo

- Gestão de membros da equipe, permissões por equipe e documentos escopados por equipe (telas legadas / Features futuras)
- Exclusão física de registros
- Matriz completa de autorização por papel (OQ-020) além do gate administrativo já exigido pela API
- Implementação backend ou alteração de contratos em `api.md`

---

# Atores

| Ator | Descrição |
|------|-----------|
| Administrador autenticado | Usuário com sessão válida (FT-AUTH) e permissão administrativa para operações de escrita na API |

Leitura e escrita na UI assumem o mesmo perfil exigido pelos endpoints administrativos documentados em `api.md`.

---

# Navegação e Rotas

| Rota | Tela | RF-FE |
|------|------|-------|
| `/app/administrador/equipes` | Hub | — |
| `/app/administrador/equipes/lista` | Listagem | RF-FE-EQUIPE-003 |
| `/app/administrador/equipes/novo` | Cadastro | RF-FE-EQUIPE-001 |
| `/app/administrador/equipes/:id` | Detalhe | RF-FE-EQUIPE-002 |
| `/app/administrador/equipes/:id/editar` | Edição | RF-FE-EQUIPE-004 |

Constantes de rota e helpers de URL devem seguir o padrão de `frontend/src/constants/routes.ts` (FT-SINGULAR).

---

# Requisitos Funcionais (Frontend)

## RF-FE-EQUIPE-001 — Cadastrar Equipe na UI

| Campo | Valor |
|--------|--------|
| Identificador | RF-FE-EQUIPE-001 |
| Descrição | Formulário de cadastro com submit via `POST /api/v1/equipes`. |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-001 |
| Requisito backend | RF-EQUIPE-001 |

Campos obrigatórios: `areaId`, `name`. Opcionais: `description`, `leaderId`.

Seleção de área: consumir `GET /api/v1/areas` (FT-AREA backend) para popular select; exibir apenas áreas utilizáveis conforme resposta da API.

## RF-FE-EQUIPE-002 — Consultar Equipe na UI

| Campo | Valor |
|--------|--------|
| Identificador | RF-FE-EQUIPE-002 |
| Descrição | Exibir dados de equipe via `GET /api/v1/equipes/{id}`. |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-002 |
| Requisito backend | RF-EQUIPE-002 |

## RF-FE-EQUIPE-003 — Listar Equipes na UI

| Campo | Valor |
|--------|--------|
| Identificador | RF-FE-EQUIPE-003 |
| Descrição | Tabela/lista com paginação, ordenação e filtros via `GET /api/v1/equipes`. |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-003 |
| Requisito backend | RF-EQUIPE-003 |

Filtros mínimos: `status`, `areaId`, `name` (paridade com query params da API).

## RF-FE-EQUIPE-004 — Editar Equipe na UI

| Campo | Valor |
|--------|--------|
| Identificador | RF-FE-EQUIPE-004 |
| Descrição | Formulário de edição via `PUT /api/v1/equipes/{id}`. |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-004 |
| Requisito backend | RF-EQUIPE-004 |

`areaId` exibido somente leitura (RN-EQUIPE-007). Payload de atualização não envia `areaId`.

## RF-FE-EQUIPE-005 — Alterar Status na UI

| Campo | Valor |
|--------|--------|
| Identificador | RF-FE-EQUIPE-005 |
| Descrição | Ativar/inativar via `PATCH /api/v1/equipes/{id}/status` com confirmação do usuário. |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-005 |
| Requisito backend | RF-EQUIPE-005 |

Mensagens esperadas da API (exibição ao usuário):

- Conflito de nome: *Já existe equipe ativa com este nome na área*
- Bloqueio de inativação: *Equipe possui colaboradores ativos vinculados*

---

# Comportamento de Regras de Negócio na UI

| RN | Comportamento na UI |
|----|---------------------|
| RN-EQUIPE-001 | Impedir submit sem área; exibir erro se API rejeitar área inválida/inativa |
| RN-EQUIPE-002 | Validação de nome obrigatório no cliente; mensagem de campo |
| RN-EQUIPE-003 | Exibir erro 422 após submit duplicado |
| RN-EQUIPE-004 | Erro 422 se `leaderId` inválido; campo opcional |
| RN-EQUIPE-005 | Sem ação de exclusão; apenas status |
| RN-EQUIPE-006 | Exibir mensagem de negócio ao falhar inativação |
| RN-EQUIPE-007 | `areaId` não editável após cadastro |

---

# Requisitos Não Funcionais (Frontend)

## RNF-FE-EQUIPE-001 — Autenticação

Todas as rotas exigem sessão autenticada (FT-AUTH); redirecionamento ou bloqueio conforme Foundation.

## RNF-FE-EQUIPE-002 — Design System

Componentes de formulário, tabela, diálogo, alertas e layout administrativo conforme Frontend Foundation.

## RNF-FE-EQUIPE-003 — Acessibilidade

Rótulos associados a campos; status e alertas com papéis ARIA adequados (padrão FT-SINGULAR).

## RNF-FE-EQUIPE-004 — Testes E2E

Playwright com mocks de API e auth administrativo; cenários AT-FE-EQUIPE-001 a 005 em `frontend/test/e2e/equipe/`.

---

# Dependências

| Dependência | Tipo | Descrição |
|-------------|------|-----------|
| Frontend Foundation | Plataforma | Rotas `/app`, DS, i18n, HTTP client |
| FT-AUTH | Feature | Sessão e CSRF |
| FT-EQUIPE (backend) | Feature | API `/api/v1/equipes` — **FEATURE_APPROVED** |
| FT-AREA (backend) | Feature | Listagem de áreas para select e filtro |
| FT-COLABORADOR (backend) | Feature | Validação de `leaderId` na API; select de líder opcional via `GET /api/v1/colaboradores` quando implementado na UI |

**Definition of Ready (frontend):** especificação frontend completa; backend FT-EQUIPE integrado; FT-AUTH e Foundation disponíveis.

---

# Rastreabilidade

Consolidada em `traceability.md` (colunas TK-FE, AT-FE, RF-FE).

| RF-FE | RF backend | UC | AT-FE | TK-FE |
|-------|------------|-----|-------|-------|
| RF-FE-EQUIPE-001 | RF-EQUIPE-001 | UC-EQUIPE-001 | AT-FE-EQUIPE-001 | TK-EQUIPE-FE-001 |
| RF-FE-EQUIPE-002 | RF-EQUIPE-002 | UC-EQUIPE-002 | AT-FE-EQUIPE-002 | TK-EQUIPE-FE-002 |
| RF-FE-EQUIPE-003 | RF-EQUIPE-003 | UC-EQUIPE-003 | AT-FE-EQUIPE-003 | TK-EQUIPE-FE-003 |
| RF-FE-EQUIPE-004 | RF-EQUIPE-004 | UC-EQUIPE-004 | AT-FE-EQUIPE-004 | TK-EQUIPE-FE-004 |
| RF-FE-EQUIPE-005 | RF-EQUIPE-005 | UC-EQUIPE-005 | AT-FE-EQUIPE-005 | TK-EQUIPE-FE-005 |

Planejamento de Construction (PKG-FE-01..06) permanece em `construction/frontend/features/FT-EQUIPE/` após aprovação — espelha FT-SINGULAR.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-17 | Specification Engineer | Especificação frontend administrativa FT-EQUIPE |
