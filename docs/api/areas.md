# Áreas API

| Item | Valor |
|------|-------|
| Feature | FT-AREA |
| Base path | `/api/v1/areas` |
| Contrato SDD | `specs/features/area/api.md` |
| Controller | `AreaController` |

---

## Autorização

| Operação | Autenticação | Admin |
|----------|--------------|-------|
| `GET` | Sim | Não |
| `POST`, `PUT`, `PATCH` | Sim | Sim |

---

## POST /api/v1/areas

### Request — `CreateAreaRequest`

```json
{
  "singularId": 1,
  "parentAreaId": null,
  "name": "Financeiro",
  "acronym": "FIN",
  "description": "Área financeira",
  "managerId": 2
}
```

| Campo | Tipo | Obrigatório | Validação |
|-------|------|-------------|-----------|
| `singularId` | long | Sim | `@NotNull` |
| `parentAreaId` | long | Não | Mesma singular, sem ciclo |
| `name` | string | Sim | `@NotBlank`, máx. 200 |
| `acronym` | string | Não | máx. 30 |
| `description` | string | Não | — |
| `managerId` | long | Não | Colaborador ativo |

### Response 201 — `AreaResponse`

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "success": true,
  "data": {
    "id": 10,
    "singularId": 1,
    "parentAreaId": null,
    "name": "Financeiro",
    "acronym": "FIN",
    "description": "Área financeira",
    "managerId": 2,
    "status": "ACTIVE",
    "createdAt": "2026-07-16T17:00:00Z",
    "updatedAt": null
  }
}
```

| Código | Condição |
|--------|----------|
| 201 | Criada |
| 400 | Validação bean |
| 401 | Não autenticado |
| 403 | Não administrador |
| 422 | Nome duplicado, singular inativa, etc. |

### Exemplo de erro 422 (nome duplicado)

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "status": 422,
  "error": "BUSINESS_RULE_VIOLATION",
  "message": "Já existe área ativa com este nome na singular",
  "path": "/api/v1/areas"
}
```

---

## GET /api/v1/areas/{id}

`ApiResponse<AreaResponse>` — **200** / **401** / **404**

---

## GET /api/v1/areas

### Query parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `status` | enum | `ACTIVE`, `INACTIVE` |
| `singularId` | long | Filtro por singular |
| `name` | string | Busca parcial |
| `acronym` | string | Busca parcial |
| `page`, `size`, `sort` | — | Paginação |

### Response 200

`ApiResponse<PageResponse<AreaResponse>>`

---

## PUT /api/v1/areas/{id}

### Request — `UpdateAreaRequest`

```json
{
  "parentAreaId": null,
  "name": "Financeiro Corporativo",
  "acronym": "FCORP",
  "description": "Atualizado",
  "managerId": 3
}
```

`singularId` **imutável** após criação.

| Código | Condição |
|--------|----------|
| 200 | Atualizada |
| 404 | Inexistente |
| 422 | Ciclo hierárquico, gestor inativo, etc. |

---

## PATCH /api/v1/areas/{id}/status

### Request — `UpdateAreaStatusRequest`

```json
{ "status": "INACTIVE" }
```

| Código | Condição |
|--------|----------|
| 200 | Status alterado |
| 422 | Equipes ou áreas filhas ativas |

---

## Hierarquia

Áreas suportam `parentAreaId` para subáreas na mesma singular. Ciclos são rejeitados com **422**.
