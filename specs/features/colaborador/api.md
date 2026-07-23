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

`federationId`, `singularId?`, `areaId?`, `teamId?`, `managerId?`, `name`, `email`, `zimbraId`, `biography?`, `birthDate?`, `hireDate?`

## UpdateColaboradorRequest

`name`, `singularId?`, `areaId?`, `teamId?`, `managerId?`, `zimbraId`, `biography?`, `birthDate?`, `hireDate?` (sem email)

## ColaboradorResponse

`id`, `federationId`, `singularId`, `areaId`, `teamId`, `managerId`, `name`, `email`, `zimbraId`, `biography`, `status`, `birthDate`, `hireDate`, `lastAccessAt`, `createdAt`, `updatedAt`
