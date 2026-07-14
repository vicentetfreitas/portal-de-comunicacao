# Feature Session — Equipe

| Item | Valor |
|------|-------|
| Feature Code | FT-EQUIPE |
| Feature Slug | equipe |
| Sprint | 3 |
| Data da sessão | 2026-07-14 |
| Imutabilidade | **READ ONLY** (SESSION-01) |

---

# Snapshot de Contexto

## Objetivos

- CRUD de Equipes vinculadas a Áreas ativas
- Líder opcional (colaborador ativo)
- Inativação bloqueada com colaboradores ativos vinculados
- API `/api/v1/equipes`

## Premissas

- FT-AREA encerrada — áreas disponíveis para vínculo
- FT-AUTH encerrada — JWT via cookie
- Tabela `EQUIPE` provisionada (DDL DBA)

## Contratos

| Endpoint | Método | Autorização (escrita) |
|----------|--------|----------------------|
| `/api/v1/equipes` | POST | Administrador |
| `/api/v1/equipes/{id}` | GET | Autenticado |
| `/api/v1/equipes` | GET | Autenticado |
| `/api/v1/equipes/{id}` | PUT | Administrador |
| `/api/v1/equipes/{id}/status` | PATCH | Administrador |

## PKGs Planejados

PKG-01 a PKG-06 conforme `execution-plan.md`.
