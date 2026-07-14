# Feature Session — Colaborador

| Feature Code | FT-COLABORADOR |
| Sprint | 3 |
| Imutabilidade | READ ONLY (SESSION-01) |

## Objetivos

- CRUD administrativo de colaboradores com vínculo organizacional
- Preservar fluxo `locateOrCreate` de FT-AUTH
- API `/api/v1/colaboradores`

## Contratos

| Endpoint | Método |
|----------|--------|
| `/api/v1/colaboradores` | POST, GET |
| `/api/v1/colaboradores/{id}` | GET, PUT |
| `/api/v1/colaboradores/{id}/status` | PATCH |
