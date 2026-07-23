# FT-SESSION — escopo encerrado

A persistência de contexto organizacional em `AUTH_SESSAO` (`COD_*_CTX`) foi removida em **REF-DB-CTX-01**.

- Fonte de vínculos organizacionais: entidade `COLABORADOR`
- API: campo `organizationalLinks` em `GET /api/v1/auth/me`
- Endpoints removidos: `GET /auth/contexts`, `POST /auth/context`

Baseline: `database/ddl/003-create-tables.sql`. Brownfield: `database/migrations/V006__drop_auth_sessao_organizational_context.sql`.
