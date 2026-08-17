# Frontend Flow

**Status:** Approved (atualizado 2026-07-24 — DEC-FA-*)  
**Objetivo:** Definir o fluxo principal da aplicação após autenticação e resolução de contexto.

**Documentos relacionados:** `frontend-architecture.md` · `frontend-structure.md` · `specs/features/session/specification.md` · `specs/features/primeiro-acesso/specification.md` · `specs/architecture/authentication-architecture.md`

---

# Estado de implementação (2026-07-24)

| Etapa do fluxo | Estado | SSOT |
|----------------|--------|------|
| Auth store / login / refresh / CSRF | Implementado (FT-AUTH) | `frontend/src/stores/auth-store.ts` |
| Session store / vínculos / hidratação | Incremento 1 entregue | `frontend/src/stores/session.store.ts` |
| Multi-contexto + Contexto Ativo | **Aprovado** (DEC-FA-003) — implementação pendente | FT-PRIMEIRO-ACESSO + FT-SESSION |
| Seleção de contexto (N vínculos) | **Aprovado** — implementação pendente | DEC-FA-001; FT-PRIMEIRO-ACESSO |
| Home dinâmica | **Aprovada** (DEC-FA-004) — implementação pendente | Backend define; frontend renderiza |
| Onboarding legado (requests admin / CMS select) | **Obsoleto no TO-BE** | Discovery AS-IS apenas |

---

# Fluxo Principal (oficial)

```text
Splash
    │
    ▼
Verificar Sessão (auth.store)
    │
    ├── Não autenticado → Login (FT-AUTH)
    │
    └── Sessão válida
             │
             ▼
      Hidratar Session Store (/auth/me)
             │
             ▼
      FT-PRIMEIRO-ACESSO
             │
             ├── 0 vínculos → Bloquear acesso operacional (BR-010)
             ├── 1 vínculo  → Contexto Ativo automático
             └── N vínculos → Seleção de Contexto Ativo
             │
             ▼
      Persistência do Contexto Ativo
             │
             ▼
      Solicitar Home ao backend (DEC-FA-004)
             │
             ▼
      Renderizar Home
             │
             ▼
      Módulos (features/*/stores sob demanda)
```

Toda navegação operacional ocorre no **Contexto Ativo** (`federationId`, `singularId`, `areaId`).

---

# Session Store

**Arquivo:** `src/stores/session.store.ts`

Estado de sessão: usuário, vínculos disponíveis, Contexto Ativo, indicadores de hidratação.

Não executa login/logout/refresh (auth.store).

---

# Auth Store

**Arquivo:** `src/stores/auth-store.ts` (nome físico atual)

Ciclo de autenticação apenas.

---

# Home

A Home é **dinâmica** (DEC-FA-004). O frontend **não** aplica regras fixas de landing. Placeholder `/app` não é a definição oficial.

---

# CMS

O CMS **não** participa deste fluxo de contexto/autorização (DEC-CMS-001). É provedor de conteúdo apenas.

---

# Princípios

- Backend resolve vínculos, Home e regras de negócio.
- Frontend apresenta contexto resolvido e Home recebida.
- Autenticação uma vez; contexto operacional via FT-PRIMEIRO-ACESSO.
- Features de negócio consomem Session Store / Contexto Ativo — não reinventam onboarding.
