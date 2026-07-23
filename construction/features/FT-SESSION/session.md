# Session snapshot — FT-SESSION (READ ONLY)

feature: FT-SESSION
created_at: "2026-07-20T17:30:00-03:00"

objective: >
  Expor contexto organizacional após autenticação (vínculos do COLABORADOR e claims JWT),
  sem persistência em AUTH_SESSAO (REF-DB-CTX-01 encerrado).

scope_phase_1:
  - organizationalLinks em GET /api/v1/auth/me
  - Resolução automática de contexto a partir do COLABORADOR no login
  - Claims organizacionais no access token

out_of_scope:
  - Troca de contexto multi-vínculo avançada (fase 2)
  - Correção ORA-00904 em COLABORADOR (V004 / baseline)
