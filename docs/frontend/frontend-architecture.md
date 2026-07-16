# Frontend Architecture

**Status:** Approved  
**Sprint:** Frontend Foundation  
**Objetivo:** Definir a arquitetura base do frontend do Portal de Comunicação, estabelecendo responsabilidades, organização dos módulos e padrões de desenvolvimento.

**Documentos relacionados:** `frontend-structure.md` (estrutura física e SSOT das stores) · `frontend-flow.md` (fluxo de inicialização)

---

# Visão Geral

O frontend será desenvolvido utilizando **Vue 3**, **TypeScript** e **Quasar Framework**, seguindo uma arquitetura modular, desacoplada e orientada a Features.

Toda funcionalidade de negócio deverá consumir uma infraestrutura comum provida pela Frontend Foundation.

---

# Princípios Arquiteturais

- Separação clara entre infraestrutura e negócio.
- Backend é responsável pelas regras de negócio.
- Frontend é responsável pela experiência do usuário.
- Estado global centralizado.
- Componentes reutilizáveis.
- Comunicação exclusivamente através da API.
- Todas as Features seguem a mesma estrutura.
- Nenhuma Feature implementa autenticação ou autorização própria.

---

# Estrutura da Aplicação

```text
App
 │
 ├── Router
 │
 ├── Layout
 │
 ├── Session (`src/stores/session.store.ts`)
 │
 ├── Features
 │
 └── Shared
```

---

# Camadas

## Application

Responsável pela inicialização da aplicação.

Inclui:

- Bootstrap
- Configurações globais
- Plugins
- Providers
- Inicialização da sessão (`auth.store.ts` → `session.store.ts`)

---

## Router

Responsável pela navegação.

Responsabilidades:

- definição das rotas
- lazy loading
- route guards
- redirecionamentos
- tratamento de rotas inexistentes

---

## Layouts

Responsáveis pela estrutura visual.

Exemplos:

- Public Layout
- Auth Layout
- Dashboard Layout

Os layouts não contêm regras de negócio.

---

## Features

Cada funcionalidade será organizada em um módulo independente.

Exemplo:

```text
features/
    area/
    equipe/
    singular/
    colaborador/
    documento/
```

Cada Feature deverá conter apenas código relacionado ao seu domínio.

---

## Shared

Componentes reutilizados por toda aplicação.

Exemplos:

- Buttons
- Inputs
- Tables
- Dialogs
- Cards
- Loading
- Empty State
- Error State

---

# Gerenciamento de Estado

O estado da aplicação divide-se em **infraestrutura global** (`src/stores/`) e **estado por Feature** (`src/features/<feature>/stores/`).

```text
src/stores/                    ← Infraestrutura global (Frontend Foundation)
├── auth.store.ts              ← Autenticação (SSOT conceitual)
├── session.store.ts           ← Contexto do usuário (SSOT conceitual)
└── app.store.ts               ← Estado de UI global (opcional)

src/features/<feature>/stores/ ← Apenas estado da Feature
└── <feature>.store.ts
```

---

## Stores Globais (`src/stores/`)

Reservadas à **Frontend Foundation**. Nenhuma Feature de negócio define ou substitui estas stores.

| Store | Arquivo | Papel |
|-------|---------|-------|
| **Auth** | `src/stores/auth.store.ts` | Ciclo de autenticação — login, logout, refresh, expiração, cookies, CSRF |
| **Session** | `src/stores/session.store.ts` | Contexto resolvido — usuário, organização, área/equipe ativa, permissões, menus |
| **App** | `src/stores/app.store.ts` | Preferências e UI global (sidebar, tema) — sem domínio de negócio |

Definição conceitual completa de Auth e Session: ver `frontend-structure.md` § `stores/`.

**Regras:**

- Auth Store **não** armazena dados de negócio nem contexto organizacional.
- Session Store **não** executa login, logout ou refresh — consome resultado da Auth Store e do backend.
- Features **não** criam stores globais paralelas.

---

## Feature Stores (`features/<feature>/stores/`)

Cada Feature possui stores **apenas** para estado local ao seu domínio.

Exemplo:

```text
features/area/stores/area.store.ts
features/equipe/stores/equipe.store.ts
features/singular/stores/singular.store.ts
```

Responsáveis somente por:

- listagens e filtros da Feature
- formulários e seleção local
- cache em memória do módulo

**Proibido:**

- duplicar usuário, permissões ou contexto (usar `session.store.ts`)
- implementar autenticação (usar `auth.store.ts`)
- stores de uma Feature em `src/stores/`

Comunicação entre Features: `session.store.ts`, services compartilhados ou API — nunca import direto de outra Feature store.

---

# Comunicação com Backend

Toda comunicação ocorrerá através de Services.

```text
View

↓

Composable

↓

Service

↓

HTTP Client

↓

API
```

As Views nunca realizarão chamadas HTTP diretamente.

---

# API Client

Responsável por:

- Axios
- interceptors
- refresh automático
- tratamento de erro
- timeout
- headers
- CSRF
- autenticação

Será compartilhado por toda aplicação.

---

# Composables

Responsáveis por lógica reutilizável.

Exemplos:

- usePagination
- useFilters
- useDialog
- useLoading
- usePermissions
- useNotifications

Não deverão conter regras de negócio específicas de uma Feature.

---

# Componentes

Divisão recomendada:

```text
shared/
    components/

features/
    area/
        components/

    equipe/
        components/
```

Componentes de Feature não devem ser reutilizados por outros módulos.

---

# Navegação

Fluxo principal:

```text
Splash (auth.store.ts)

↓

Login (auth.store.ts)

↓

Callback (auth.store.ts)

↓

Loading Session (session.store.ts)

↓

Resolve Context (session.store.ts)

↓

Dashboard

↓

Feature (features/<feature>/stores/)
```

Toda navegação depende de `session.store.ts` inicializada após `auth.store.ts`.

---

# Controle de Acesso

O frontend não decide permissões.

Fluxo:

```text
Backend

↓

Perfil / Permissões (API)

↓

session.store.ts

↓

Router Guard

↓

Tela
```

Caso o usuário não possua acesso:

- ocultar menu
- bloquear navegação
- apresentar página 403 quando necessário

---

# Organização por Feature

Cada módulo deverá seguir a mesma estrutura.

```text
feature/

    pages/

    components/

    services/

    stores/

    composables/

    models/

    types/

    validators/
```

Essa estrutura deverá ser mantida em todas as Features.

---

# Tratamento de Erros

Erros serão tratados em três níveis.

## Global

- falha de autenticação
- sessão expirada
- indisponibilidade da API

---

## Feature

- validações
- regras específicas
- mensagens ao usuário

---

## Componente

- erros locais
- estados de carregamento
- estados vazios

---

# Convenções

- TypeScript obrigatório.
- Composition API.
- Componentes pequenos e reutilizáveis.
- Lazy loading para páginas.
- Services desacoplados das Views.
- Stores sem acesso direto ao DOM.
- Nenhuma chamada HTTP em componentes visuais.

---

# Dependências

A Frontend Foundation deverá estar concluída antes da implementação das Features:

- FT-AREA
- FT-SINGULAR
- FT-EQUIPE
- FT-COLABORADOR
- FT-USUÁRIO
- FT-DOCUMENTO
- FT-COMUNICADO
- FT-NOTIFICAÇÃO

---

# Resultado Esperado

Ao concluir a Frontend Foundation, o Portal deverá possuir uma arquitetura única, consistente e reutilizável, permitindo que todas as Features sejam implementadas seguindo os mesmos padrões de organização, comunicação, gerenciamento de estado e navegação.