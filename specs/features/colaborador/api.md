# API Contract — FT-COLABORADOR

Base Path: `/api/v1/colaboradores`

| Método | Endpoint | RF |
|--------|----------|-----|
| POST | /api/v1/colaboradores | RF-001 |
| GET | /api/v1/colaboradores/{id} | RF-002 |
| GET | /api/v1/colaboradores | RF-003 |
| PUT | /api/v1/colaboradores/{id} | RF-004 |
| PATCH | /api/v1/colaboradores/{id}/status | RF-005 |

## CreateColaboradorRequest

`federationId`, `singularId?`, `areaId?`, `teamId?`, `managerId?`, `name`, `email`, `jobTitle?`, `cpf?`, `zimbraId?`, `biography?`

## UpdateColaboradorRequest

`name`, `singularId?`, `areaId?`, `teamId?`, `managerId?`, `jobTitle?`, `cpf?`, `biography?` (sem email)

## ColaboradorResponse

`id`, `federationId`, `singularId`, `areaId`, `teamId`, `managerId`, `name`, `email`, `jobTitle`, `cpf`, `zimbraId`, `biography`, `status`, `createdAt`, `updatedAt`
