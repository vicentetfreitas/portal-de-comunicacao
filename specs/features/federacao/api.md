# API Contract — FT-FEDERACAO

Base Path: `/api/v1/federacoes`

| Método | Endpoint | RF |
|--------|----------|-----|
| POST | /api/v1/federacoes | RF-001 |
| GET | /api/v1/federacoes/{id} | RF-002 |
| GET | /api/v1/federacoes | RF-003 |
| PUT | /api/v1/federacoes/{id} | RF-004 |
| PATCH | /api/v1/federacoes/{id}/status | RF-005 |

## CreateFederacaoRequest

`name`, `acronym`, `unimedCode`, `ansRegistration`, `websiteUrl?`, `description?`

## UpdateFederacaoRequest

Mesmos campos de criação.

## FederacaoResponse

`id`, `name`, `acronym`, `unimedCode`, `ansRegistration`, `websiteUrl`, `description`, `status`, `createdAt`, `updatedAt`

## Filtros listagem

`status`, `name`, `acronym`, `unimedCode`, `page`, `size`, `sort`
