# Singulares API

| Item | Valor |
|------|-------|
| Feature | FT-SINGULAR |
| Base path | `/api/v1/singulares` |
| Contrato SDD | `specs/features/singular/api.md` |
| Controller | `SingularController` |

---

## Autorização

| Operação | Autenticação | Admin |
|----------|--------------|-------|
| `GET` (list/detail) | Sim | Não |
| `POST`, `PUT`, `PATCH` | Sim | Sim (`session-administrator-emails`) |

Mutações exigem CSRF quando habilitado.

---

## POST /api/v1/singulares

Cria singular organizacional.

### Request — `CreateSingularRequest`

```json
{
  "federationId": 1,
  "name": "Singular Norte",
  "acronym": "SN",
  "unimedCode": "12345"
}
```

| Campo | Tipo | Obrigatório | Validação |
|-------|------|-------------|-----------|
| `federationId` | long | Sim | `@NotNull` |
| `name` | string | Sim | `@NotBlank`, máx. 200 |
| `acronym` | string | Sim | `@NotBlank`, máx. 30, único global |
| `unimedCode` | string | Sim | `@NotBlank`, máx. 20, único global |

### Response 201 — `SingularResponse`

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "success": true,
  "data": {
    "id": 1,
    "federationId": 1,
    "name": "Singular Norte",
    "acronym": "SN",
    "unimedCode": "12345",
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
| 422 | Sigla/código duplicado |

---

## GET /api/v1/singulares/{id}

### Response 200

`ApiResponse<SingularResponse>`

| Código | Condição |
|--------|----------|
| 200 | Encontrada |
| 401 | Não autenticado |
| 404 | Inexistente |

---

## GET /api/v1/singulares

Listagem paginada.

### Query parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `status` | `ACTIVE` \| `INACTIVE` | Filtro por status |
| `federationId` | long | Filtro por federação |
| `name` | string | Busca parcial (case insensitive) |
| `acronym` | string | Busca parcial |
| `unimedCode` | string | Busca |
| `page`, `size`, `sort` | — | Paginação corporativa |

### Response 200

`ApiResponse<PageResponse<SingularResponse>>`

---

## PUT /api/v1/singulares/{id}

### Request — `UpdateSingularRequest`

```json
{
  "name": "Singular Norte Atualizada",
  "acronym": "SNA",
  "unimedCode": "12346"
}
```

| Campo | Tipo | Obrigatório |
|-------|------|-------------|
| `name` | string | Sim |
| `acronym` | string | Sim |
| `unimedCode` | string | Sim |

`federationId` **não** é atualizável.

| Código | Condição |
|--------|----------|
| 200 | Atualizada |
| 400 | Validação |
| 401/403 | Auth |
| 404 | Inexistente |
| 422 | Regra de negócio |

---

## PATCH /api/v1/singulares/{id}/status

### Request — `UpdateSingularStatusRequest`

```json
{ "status": "INACTIVE" }
```

| Código | Condição |
|--------|----------|
| 200 | Status alterado |
| 422 | Áreas ativas vinculadas (inativação) |

---

## Fluxo organizacional relacionado

```text
Singular (ativa)
  └── Área(s)
        └── Equipe(s)
              └── Colaborador(es)
```

Inativar singular com áreas ativas → **422**.
