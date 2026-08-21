# FT-SESSION

SSOT funcional: `specification.md`.

| Tema | Estado |
|------|--------|
| Vínculos em `/auth/me` (`organizationalLinks`) | Fase 1 entregue |
| Multi-contexto + Contexto Ativo (vínculo) | **Aprovado** (DEC-FA-003) — implementação sob FT-PRIMEIRO-ACESSO + evolução FT-SESSION |
| Contexto de vínculo em `AUTH_SESSAO` | **Proibido** (REF-DB-CTX-01) |
| Contexto operacional por atribuição de papel (`PAPEL_ATRIBUICAO`) | **Entregue** (2026-08-20) — `eligibleAssignments`/`activeAssignment` em `/auth/me`, `POST /api/v1/auth/atribuicoes/{id}/ativar` |

Baseline: `database/ddl/003-create-tables.sql`. Brownfield histórico: `database/migrations/V006__drop_auth_sessao_organizational_context.sql`.
