# Execution Plan — FT-PRIMEIRO-ACESSO (Frontend)

> **Legado (Archive)** — plano principal: [`../../../../specs/features/primeiro-acesso/tasks.md`](../../../../specs/features/primeiro-acesso/tasks.md).

| Item | Valor |
|------|-------|
| Feature Code | **FT-PRIMEIRO-ACESSO** |
| Workstream | frontend |
| Status | **Não iniciada** |
| SSOD | `feature-manifest.yaml` |
| Backend | WS backend deve estar `closed` |
| Gates | BUILD-02 (01–05); E2E-01/02 (06) |

---

# Objetivo

Máquina de estados client-side, APIs `/session/*`, UI seleção, Home dinâmica, integração `session.store` (BR-042 — sem landing fixa).

---

# Escopo

## Inclui

Client HTTP · estados · auto-select / seleção / bloqueio · Home · troca/reentrada · unit + E2E

## Não inclui

Backend · CMS · alterar contratos sem `api.md`

---

# Dependências

| Dependência | Tipo |
|-------------|------|
| Frontend Foundation | obrigatória |
| FT-AUTH FE | obrigatória |
| FT-SESSION store | obrigatória |
| FT-PRIMEIRO-ACESSO backend `closed` | bloqueante |

---

# Sequência de PKGs

| PKG | Nome | TK |
|-----|------|-----|
| pkg-fe-01 | Scaffold & API client | TK-PA-010 |
| pkg-fe-02 | Auto-select & Blocked | TK-PA-002,008 |
| pkg-fe-03 | Selection UI | TK-PA-003 |
| pkg-fe-04 | Home render | TK-PA-005 |
| pkg-fe-05 | Change & Reentry | TK-PA-006,007,009 |
| pkg-fe-06 | E2E & Closure | AT-PA-* |

Ordem: 01→…→06.

---

# Definition of Ready

1. Backend WS `closed`
2. Foundation + FT-AUTH FE ok
3. Session FE via Orchestrator

# Definition of Done

1. pkg-fe-01…06 DONE
2. BUILD-02 + E2E-01
3. WS frontend `closed`

---

# Próxima ação

Após backend closed → `Execute FT-PRIMEIRO-ACESSO` (Orchestrator roteia FE).
