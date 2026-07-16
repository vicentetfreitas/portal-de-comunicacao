# API — Portal de Comunicação

| Item | Valor |
|------|-------|
| Sprint | **API-DOCS-01** |
| Base URL | `/api/v1` |
| Versão documentada | Estado atual do backend (2026-07-16) |
| OpenAPI | `/v3/api-docs` · Swagger UI `/swagger-ui.html` |
| Padrões corporativos | `docs/implementation/07-api-standards.md` |
| Contratos SDD | `specs/features/*/api.md` |

---

## Objetivo

Documentação operacional da API REST do backend Spring Boot — reflete **exclusivamente** a implementação existente, sem alterar código ou contratos.

---

## Índice

| Documento | Escopo | Status |
|-----------|--------|--------|
| [getting-started.md](./getting-started.md) | Autenticação, cookies, CSRF, ambiente | Implementado |
| [conventions.md](./conventions.md) | Envelopes, paginação, headers | Implementado |
| [errors.md](./errors.md) | Formato e códigos de erro | Implementado |
| [authentication.md](./authentication.md) | `/api/v1/auth/*`, admin sessions | Implementado |
| [singulares.md](./singulares.md) | `/api/v1/singulares` | Implementado |
| [areas.md](./areas.md) | `/api/v1/areas` | Implementado |
| [equipes.md](./equipes.md) | `/api/v1/equipes` | Implementado |
| [colaboradores.md](./colaboradores.md) | `/api/v1/colaboradores` | Implementado |
| [usuarios.md](./usuarios.md) | `/api/v1/usuarios` | **Sem implementação** |
| [documentos.md](./documentos.md) | `/api/v1/documentos` | **Sem implementação** |
| [discrepancies.md](./discrepancies.md) | Divergências spec/docs vs código | Registro |
| [postman/](./postman/) | Coleção e ambiente Postman (v2 — scripts de validação) |
| [validation/](./validation/) | Homologação API-VALIDATION-01 |
| [discrepancies.md](./discrepancies.md) | Divergências spec/docs vs código | Implementado |

---

## Endpoints implementados (27)

| Recurso | Endpoints | Feature |
|---------|-----------|---------|
| Health | 1 | Platform Foundation |
| Authentication | 5 | FT-AUTH |
| Admin Sessions | 1 | FT-AUTH (RF-AUTH-010) |
| Singulares | 5 | FT-SINGULAR |
| Áreas | 5 | FT-AREA |
| Equipes | 5 | FT-EQUIPE |
| Colaboradores | 5 | FT-COLABORADOR |

---

## Cobertura estimada

| Métrica | Valor |
|---------|-------|
| Controllers implementados documentados | **7/7** (100%) |
| Endpoints implementados documentados | **27/27** (100%) |
| Recursos planejados sem implementação | 2 (`usuarios`, `documentos`) |
| Features com contrato em `specs/` e API ativa | 5/5 |

---

## Referências

- `specs/features/authentication/api.md`
- `specs/features/singular/api.md`
- `specs/features/area/api.md`
- `specs/features/equipe/api.md`
- `specs/features/colaborador/api.md`
- `docs/implementation/07-api-standards.md`
- `docs/implementation/10-security-implementation.md`
