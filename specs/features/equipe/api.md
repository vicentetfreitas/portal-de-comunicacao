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
