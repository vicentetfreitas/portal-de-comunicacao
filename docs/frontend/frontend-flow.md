# Frontend Flow

**Status:** Approved  
**Sprint:** Frontend Foundation  
**Objetivo:** Definir o fluxo principal da aplicação antes da implementação das funcionalidades de negócio.

**Documentos relacionados:** `frontend-architecture.md` § Gerenciamento de Estado · `frontend-structure.md` § `stores/` · `specs/features/session/specification.md` · `specs/architecture/authentication-architecture.md`

---

# Estado de implementação (2026-07-24)

| Etapa do fluxo | Estado | SSOT |
|----------------|--------|------|
| Auth store / login / refresh / CSRF | Parcialmente implementado | FT-AUTH + `frontend/src/stores/auth-store.ts` |
| Session store / perfil / contexto / permissões / menu | **Não implementado** | Este documento (alvo) + FT-SESSION fase 1 (`organizationalLinks` apenas) |
| Seleção de contexto (N vínculos) | **Não implementado** — backlog | RN-SESSION-003; OQ-027 |
| Dashboard pós-contexto | Placeholder `/app` | OQ-028 |

Este documento permanece a **fonte oficial do fluxo alvo** do frontend. Não duplicar regras de autenticação Zimbra aqui — ver arquitetura AUTH.

# Visão Geral

O frontend do Portal de Comunicação será construído a partir de um fluxo único de inicialização da aplicação.

Todas as funcionalidades deverão partir de um contexto de sessão resolvido, evitando que cada Feature implemente sua própria lógica de autenticação, autorização ou carregamento de dados do usuário.

---

# Fluxo Principal

```text
Splash
    │
    ▼
Verificar Sessão (auth.store.ts)
    │
    ├── Não autenticado
    │        │
    │        ▼
    │      Login (auth.store.ts)
    │        │
    │        ▼
    │     Callback FT-AUTH (auth.store.ts)
    │
    └── Sessão válida
             │
             ▼
      Loading Session (session.store.ts)
             │
             ▼
      Carregar Perfil (session.store.ts)
             │
             ▼
      Resolver Contexto (session.store.ts)
             │
             ▼
      Carregar Permissões (session.store.ts)
             │
             ▼
      Construir Menu (session.store.ts)
             │
             ▼
      Dashboard
             │
             ▼
      Módulos (features/<feature>/stores/ sob demanda)
```

---

# Resolução do Contexto

Após a autenticação, o frontend solicitará ao backend todas as informações necessárias para inicializar a sessão.

O backend será responsável por resolver:

- organização
- área
- equipe
- papéis
- permissões
- dashboard inicial

O frontend não deverá possuir regras de negócio baseadas em domínio de e-mail, papéis ou permissões.

Toda decisão será centralizada no backend.

---

# Seleção de Contexto

Quando existir apenas um contexto disponível para o usuário, a seleção ocorrerá automaticamente.

Exemplo:

```text
Usuário

↓

Federação

↓

Área Comunicação

↓

Equipe Comunicação Interna

↓

Dashboard
```

Quando existirem múltiplas áreas ou equipes, o frontend solicitará a escolha antes de concluir a inicialização da sessão.

```text
Login

↓

Selecionar Área

↓

Selecionar Equipe (quando aplicável)

↓

Dashboard
```

---

# Session Store

**Arquivo:** `src/stores/session.store.ts`

Fonte única de **contexto** da aplicação (não confundir com autenticação).

Responsabilidades:

- usuário autenticado
- organização ativa
- área ativa
- equipe ativa
- perfis
- permissões
- menus
- dashboard
- contexto atual

Inicializada após Auth Store confirmar sessão válida e backend retornar perfil/contexto.

---

# Auth Store

**Arquivo:** `src/stores/auth.store.ts`

Responsável **exclusivamente** por autenticação (não armazena contexto de negócio).

Responsabilidades:

- login
- logout
- refresh
- expiração do access token
- cookies (`access_token`, `refresh_token`)
- CSRF (`XSRF-TOKEN` / `X-XSRF-TOKEN`)

---

# Stores por Feature

Durante o fluxo de inicialização, **apenas** Auth Store e Session Store participam.

Stores em `src/features/<feature>/stores/` são carregadas sob demanda ao navegar para cada módulo — não interferem no bootstrap da aplicação.

---

# Dashboard

O Dashboard será carregado somente após a conclusão da resolução do contexto.

Nenhuma Feature deverá realizar autenticação ou resolução de permissões individualmente.

Todas deverão consumir exclusivamente `src/stores/session.store.ts`.

---

# Princípios

- Backend é responsável pelas regras de negócio.
- Frontend apenas apresenta o contexto resolvido.
- A autenticação ocorre uma única vez.
- O contexto da sessão é carregado uma única vez.
- Menus e permissões são construídos dinamicamente.
- Todas as Features reutilizam o mesmo contexto via `src/stores/session.store.ts`.
- Autenticação centralizada em `src/stores/auth.store.ts`.
- Estado de domínio permanece em `src/features/<feature>/stores/`.
- O fluxo de inicialização é único para toda a aplicação.

---

# Dependências

Este fluxo deverá ser implementado antes das seguintes Features:

- FT-AREA
- FT-SINGULAR
- FT-EQUIPE
- FT-COLABORADOR
- FT-USUÁRIO
- FT-DOCUMENTO
- FT-COMUNICADO
- FT-NOTIFICAÇÃO

Nenhuma Feature de negócio deverá iniciar sem que o fluxo de sessão esteja operacional.

---

# Resultado Esperado

Após a conclusão da Frontend Foundation, todo usuário deverá acessar o Portal seguindo um fluxo único de autenticação, resolução de contexto e carregamento da aplicação, garantindo consistência, reutilização e baixo acoplamento entre as Features.