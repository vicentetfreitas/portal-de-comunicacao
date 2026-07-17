# API Contract

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-EQUIPE |
| Feature | Equipe |
| Domínio | EQUIPE |
| Recurso | equipes |
| Base Path | /api/v1/equipes |

---

# Endpoints

## RF-EQUIPE-001 — Criar Equipe

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/equipes |
| Autenticação | Obrigatória |
| Autorização | Administrador |

**Request:** `CreateEquipeRequest` — `areaId`, `name`, `description?`, `leaderId?`

**Response:** `ApiResponse<EquipeResponse>` — HTTP 201

---

## RF-EQUIPE-002 — Consultar por Identificador

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/equipes/{id} |

**Response:** `ApiResponse<EquipeResponse>` — HTTP 200/404

---

## RF-EQUIPE-003 — Listar Equipes

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/equipes |

**Filtros:** `status`, `areaId`, `name` + paginação corporativa.

**Response:** `ApiResponse<PageResponse<EquipeResponse>>`

---

## RF-EQUIPE-004 — Atualizar Equipe

| Campo | Valor |
|--------|--------|
| Método | PUT |
| Endpoint | /api/v1/equipes/{id} |
| Autorização | Administrador |

**Request:** `UpdateEquipeRequest` — `name`, `description?`, `leaderId?` (sem `areaId`)

---

## RF-EQUIPE-005 — Alterar Status

| Campo | Valor |
|--------|--------|
| Método | PATCH |
| Endpoint | /api/v1/equipes/{id}/status |
| Autorização | Administrador |

**Request:** `UpdateEquipeStatusRequest` — `status` (`ACTIVE`/`INACTIVE`)

---

# DTOs

## EquipeResponse

`id`, `areaId`, `name`, `description`, `leaderId`, `status`, `createdAt`, `updatedAt`

Padrões corporativos: `docs/implementation/07-api-standards.md`

---

# Consumo pela camada frontend

| Aspecto | Diretriz |
|---------|----------|
| Base URL | Mesmo host da aplicação; prefixo `/api/v1/equipes` |
| Autenticação | Cookie/sessão FT-AUTH; CSRF em mutações |
| Tipos | Espelhar DTOs deste documento em `frontend/src/types/organization/equipe.types.ts` (Construction) |
| Listagem de áreas | `GET /api/v1/areas` para filtro e select em cadastro (FT-AREA) |
| Líder opcional | `leaderId` validado pela API; select de colaboradores opcional via `/api/v1/colaboradores` |
| Status | Valores `ACTIVE` / `INACTIVE` no PATCH de status |

Nenhuma alteração de contrato é introduzida pelo workstream frontend; ver `specification-frontend.md`.

---

# Histórico

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | Contrato inicial |
| 1.1 | 2026-07-17 | Specification Engineer | Notas de consumo frontend |
