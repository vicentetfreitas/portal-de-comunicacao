# Package Dependency Graph — Frontend Foundation (Sprint 0)

| Item | Valor |
|------|-------|
| Sprint | Sprint 0 — Frontend Foundation |
| Versão | 1.0 |
| Data | 2026-07-15 |

---

## Ordem de Execução Oficial

```text
PKG-FE-S0-01 (Project Bootstrap)
    │
    ├── PKG-FE-S0-02 (Theme)
    │       └── PKG-FE-S0-03 (Design System)
    │               ├── PKG-FE-S0-04 (Layouts)
    │               │       └── PKG-FE-S0-05 (Routing)
    │               │               ├── PKG-FE-S0-07 (Auth Integration) ← também depende de S0-06
    │               │               └── PKG-FE-S0-09 (Testing) ← também depende de S0-01
    │               └── PKG-FE-S0-08 (Shared Components) ← também depende de S0-06
    │
    └── PKG-FE-S0-06 (HTTP Client)
            ├── PKG-FE-S0-07 (Authentication Integration)
            └── PKG-FE-S0-08 (Shared Components)
```

---

## Sequência Linear Recomendada

Para execução sequencial sem paralelismo:

```text
PKG-FE-S0-01
    ↓
PKG-FE-S0-02
    ↓
PKG-FE-S0-03
    ↓
PKG-FE-S0-04
    ↓
PKG-FE-S0-05
    ↓
PKG-FE-S0-06
    ↓
PKG-FE-S0-07
    ↓
PKG-FE-S0-08
    ↓
PKG-FE-S0-09
```

Esta sequência respeita todas as dependências declaradas em `00-frontend-foundation.md` §8.

---

## Diagrama de Dependências

```mermaid
flowchart TD
    S01[PKG-FE-S0-01 Bootstrap]
    S02[PKG-FE-S0-02 Theme]
    S03[PKG-FE-S0-03 Design System]
    S04[PKG-FE-S0-04 Layouts]
    S05[PKG-FE-S0-05 Routing]
    S06[PKG-FE-S0-06 HTTP Client]
    S07[PKG-FE-S0-07 Auth Integration]
    S08[PKG-FE-S0-08 Shared Components]
    S09[PKG-FE-S0-09 Testing]

    S01 --> S02
    S02 --> S03
    S03 --> S04
    S04 --> S05
    S01 --> S06
    S05 --> S07
    S06 --> S07
    S03 --> S08
    S06 --> S08
    S01 --> S09
    S05 --> S09
```

---

## Dependências Externas

| Package | Dependência externa |
|---------|---------------------|
| PKG-FE-S0-01 | Backend integrado validado; Docker Compose local |
| PKG-FE-S0-07 | Contrato FT-AUTH (`specs/features/authentication/api.md`) — validável com mocks |

---

## Próximo após Sprint 0

```text
Sprint 1 → FT-AUTH (TASK-AUTH-FE-001 a FE-011)
```

Fonte: `docs/discovery/frontend-feature-mapping.md` — Sprint Dependency Chain.
