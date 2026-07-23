# Colaboradores API

| Item | Valor |
|------|-------|
| Feature | FT-COLABORADOR |
| Base path | `/api/v1/colaboradores` |
| Contrato SDD | `specs/features/colaborador/api.md` |
| Controller | `ColaboradorController` |

---

## Autorização

| Operação | Autenticação | Admin |
|----------|--------------|-------|
| `GET` | Sim | Não |
| `POST`, `PUT`, `PATCH` | Sim | Sim |

---

## POST /api/v1/colaboradores

### Request — `CreateColaboradorRequest`

```json
{
  "federationId": 1,
  "singularId": 1,
  "areaId": 10,
  "teamId": 20,
  "managerId": 3,
  "name": "Maria Silva",
  "email": "maria.silva@unimedceara.com.br",
  "zimbraId": "zimbra-maria",
  "biography": "Biografia opcional"
}
```

| Campo | Tipo | Obrigatório | Validação |
|-------|------|-------------|-----------|
| `federationId` | long | Sim | `@NotNull` |
| `singularId` | long | Não | Consistência com área/equipe |
| `areaId` | long | Não | Área ativa na singular |
| `teamId` | long | Não | Equipe ativa na área |
| `managerId` | long | Não | Gestor ativo, ≠ self |
| `name` | string | Sim | `@NotBlank`, máx. 255 |
| `email` | string | Sim | `@Email`, máx. 255, único |
| `zimbraId` | string | Sim | `@NotBlank`, máx. 255, único |
| `biography` | string | Não | máx. 4000 |
| `birthDate` | instant | Não | ISO-8601 |
| `hireDate` | instant | Não | ISO-8601 |

### Response 201 — `ColaboradorResponse`

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "success": true,
  "data": {
    "id": 100,
    "federationId": 1,
    "singularId": 1,
    "areaId": 10,
    "teamId": 20,
    "managerId": 3,
    "name": "Maria Silva",
    "email": "maria.silva@unimedceara.com.br",
    "zimbraId": "zimbra-maria",
    "biography": "Biografia opcional",
    "status": "ACTIVE",
    "birthDate": null,
    "hireDate": null,
    "lastAccessAt": null,
    "createdAt": "2026-07-16T17:00:00Z",
    "updatedAt": null
  }
}
```

| Código | Condição |
|--------|----------|
| 201 | Criado |
| 422 | E-mail/Zimbra duplicado, vínculos inconsistentes |

---

## GET /api/v1/colaboradores/{id}

`ApiResponse<ColaboradorResponse>` — **200** / **401** / **404**

---

## GET /api/v1/colaboradores

### Query parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `status` | enum | `ACTIVE`, `INACTIVE` |
| `singularId` | long | Filtro |
| `areaId` | long | Filtro |
| `teamId` | long | Filtro por equipe |
| `name` | string | Busca parcial |
| `email` | string | Busca parcial |
| `page`, `size`, `sort` | — | Paginação |

### Response 200

`ApiResponse<PageResponse<ColaboradorResponse>>`

---

## PUT /api/v1/colaboradores/{id}

### Request — `UpdateColaboradorRequest`

Campos editáveis: `name`, vínculos organizacionais, `zimbraId`, `biography`, `birthDate`, `hireDate`. **Sem** `federationId` nem `email` (imutáveis após cadastro).

---

## PATCH /api/v1/colaboradores/{id}/status

### Request — `UpdateColaboradorStatusRequest`

```json
{ "status": "INACTIVE" }
```

| Código | Condição |
|--------|----------|
| 422 | Subordinados ativos vinculados |

---

## Relação com autenticação

Colaboradores autenticam via FT-AUTH (`/api/v1/auth/*`). O endpoint `/api/v1/auth/me` retorna a identidade do colaborador logado — não substitui o CRUD deste recurso.

Identidade vs gestão:

| Necessidade | Endpoint |
|-------------|----------|
| Quem está logado | `GET /api/v1/auth/me` |
| CRUD colaborador | `/api/v1/colaboradores` |
