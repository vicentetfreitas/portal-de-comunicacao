# Specification — FT-SESSION

## Objetivo

Após autenticação FT-AUTH, expor o **contexto organizacional** do colaborador em `/auth/me` e nas claims do JWT, sem persistir seleção em `AUTH_SESSAO` (REF-DB-CTX-01).

## Regras (fase 1)

- **RN-SESSION-001:** Contexto inicial derivado dos vínculos do `COLABORADOR` autenticado (`singularId`, `areaId`, `teamId`).
- **RN-SESSION-002:** Com um único contexto disponível, o sistema resolve automaticamente no login.
- **RN-SESSION-003:** Com múltiplos contextos (futuro), o usuário deve escolher antes de acessar rotas protegidas.
- **RN-SESSION-004:** O contexto ativo é refletido no access token (claims organizacionais) e em `organizationalLinks` em `GET /api/v1/auth/me`; `AUTH_SESSAO` persiste apenas dados de sessão (refresh token, revogação, dispositivo).

## Non-goals

- Persistência de contexto organizacional em `AUTH_SESSAO` (`COD_*_CTX` removidos — REF-DB-CTX-01).
- Endpoints `GET /auth/contexts` e `POST /auth/context` (removidos).
- Gestão de múltiplos vínculos por colaborador (backlog).
- Substituição do modelo de permissões (PAPEL).
