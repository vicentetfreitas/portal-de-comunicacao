# Execution Plan — FT-PRIMEIRO-ACESSO (Backend)

> **Legado (Archive)** — plano principal: [`../../../specs/features/primeiro-acesso/tasks.md`](../../../specs/features/primeiro-acesso/tasks.md). Este arquivo mantém CDDs e riscos históricos.

| Item | Valor |
|------|-------|
| Feature Code | **FT-PRIMEIRO-ACESSO** |
| Feature Slug | primeiro-acesso |
| Workstream | backend |
| Status | **Não iniciada** |
| SSOD | `feature-manifest.yaml` |
| State | `construction-state.yaml` |

---

# Objetivo

Backend do primeiro acesso: N vínculos, Contexto Ativo, Home dinâmica, bloqueio sem vínculo (SDD + DEC-FA-* / DEC-ORG-001 / DEC-CMS-001).

---

# Escopo

## Inclui

- CDD-PA-01/02 (modelo N vínculos + persistência Contexto Ativo)
- PA-API-001…005
- Auditoria/observabilidade (RNF-PA-004/005)
- Aceite backend (AT-PA-*)

## Não inclui

- Frontend · CMS · FT-AUTH ciclo de token · reabrir DEC-FA-*

---

# Construction Design Decisions

| ID | Decisão | PKG |
|----|---------|-----|
| CDD-PA-01 | Entidade/relação N vínculos (federationId, singularId, areaId, teamId?) | pkg-01 |
| CDD-PA-02 | Persistência Contexto Ativo em tabela dedicada (sem `AUTH_SESSAO.COD_*_CTX`) | pkg-01 |
| CDD-PA-03 | Home MVP `{ type:"route", path, title?, params? }` | pkg-03 |
| CDD-PA-04 | `/auth/me` aditivo: `organizationalContexts[]` + `activeContext` | pkg-04 |

---

# Dependências

| Dependência | Tipo |
|-------------|------|
| Platform Foundation | obrigatória |
| FT-AUTH | obrigatória |
| FT-SESSION | obrigatória |
| FT-COLABORADOR | bloqueante (modelo N) |
| FT-AREA / FT-SINGULAR | opcional (referência) |
| Spec APPROVED | Gate RULE-01 |
| CMS | proibida |

---

# Sequência de PKGs

| PKG | Nome | TK |
|-----|------|-----|
| pkg-01 | Modelo + persistência | TK-PA-004 |
| pkg-02 | API contexts/context | TK-PA-001..004,008,009 |
| pkg-03 | API Home | TK-PA-005 |
| pkg-04 | /auth/me + validações | TK-PA-001,010 |
| pkg-05 | Auditoria + aceite + closure | TK-PA-011 |

Ordem: 01→02→03→04→05. Frontend após WS backend `closed`.

---

# Definition of Ready

1. Spec `APPROVED` (hoje READY_FOR_REVIEW)
2. Platform Foundation + FT-AUTH ok
3. CDD-PA-01..04 neste plano
4. `Execute FT-PRIMEIRO-ACESSO` → Session

# Definition of Done (WS backend)

1. pkg-01…05 DONE + Validation Summary
2. `mvn` verify Feature SUCCESS
3. State `phase: closed` → hand-off FE

---

# Validação

Unit/integração por PKG; AT backend em pkg-05; sem CMS.

---

# Próxima ação

Spec APPROVED → `Execute FT-PRIMEIRO-ACESSO`
