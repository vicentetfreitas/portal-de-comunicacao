# Equipes API

| Item | Valor |
|------|-------|
| Feature | FT-EQUIPE |
| Base path | `/api/v1/equipes` |
| Contrato SDD | `specs/features/equipe/api.md` |
| Controller | `EquipeController` |

---

## Autorização

| Operação | Autenticação | Admin |
|----------|--------------|-------|
| `GET` | Sim | Não |
| `POST`, `PUT`, `PATCH` | Sim | Sim |

---

## POST /api/v1/equipes

### Request — `CreateEquipeRequest`

```json
{
  "areaId": 10,
  "name": "Equipe Alpha",
  "description": "Equipe operacional",
  "leaderId": 5
}
```

| Campo | Tipo | Obrigatório | Validação |
|-------|------|-------------|-----------|
| `areaId` | long | Sim | `@NotNull`, área ativa |
| `name` | string | Sim | `@NotBlank`, máx. 200 |
| `description` | string | Não | — |
| `leaderId` | long | Não | Colaborador ativo |

### Response 201 — `EquipeResponse`

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "success": true,
  "data": {
    "id": 20,
    "areaId": 10,
    "name": "Equipe Alpha",
    "description": "Equipe operacional",
    "leaderId": 5,
    "status": "ACTIVE",
    "createdAt": "2026-07-16T17:00:00Z",
    "updatedAt": null
  }
}
```

| Código | Condição |
|--------|----------|
| 201 | Criada |
| 422 | Área inativa, nome duplicado na área |

---

## GET /api/v1/equipes/{id}

`ApiResponse<EquipeResponse>` — **200** / **401** / **404**

---

## GET /api/v1/equipes

### Query parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `status` | enum | `ACTIVE`, `INACTIVE` |
| `areaId` | long | Filtro por área |
| `name` | string | Busca parcial |
| `page`, `size`, `sort` | — | Paginação |

### Response 200

`ApiResponse<PageResponse<EquipeResponse>>`

---

## PUT /api/v1/equipes/{id}

### Request — `UpdateEquipeRequest`

```json
{
  "name": "Equipe Alpha Renomeada",
  "description": "Nova descrição",
  "leaderId": 6
}
```

`areaId` **imutável** após criação.

---

## PATCH /api/v1/equipes/{id}/status

### Request — `UpdateEquipeStatusRequest`

```json
{ "status": "INACTIVE" }
```

| Código | Condição |
|--------|----------|
| 422 | Colaboradores ativos vinculados |

---

## Fluxo completo (exemplo cross-feature)

```text
POST /api/v1/singulares        → singularId
POST /api/v1/areas             → areaId (singularId)
POST /api/v1/equipes            → teamId (areaId)
POST /api/v1/colaboradores     → colaborador (teamId, areaId, singularId)
```

Evidência: `OrgCrossFeatureIntegrationTest`.
