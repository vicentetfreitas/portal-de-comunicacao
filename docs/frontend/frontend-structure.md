# Frontend Structure

**Status:** Approved  
**Sprint:** Frontend Foundation  
**Objetivo:** Definir a estrutura física do projeto frontend, padronizando a organização dos módulos, responsabilidades e convenções de desenvolvimento.

**Documentos relacionados:** `frontend-architecture.md` (camadas e princípios) · `frontend-flow.md` (bootstrap e stores no fluxo)

---

# Princípios

A estrutura do frontend deve ser:

- Modular
- Orientada a Features
- Escalável
- Consistente
- Baixo acoplamento
- Alta reutilização

Cada diretório possui uma responsabilidade única e bem definida.

---

# Estrutura do Projeto

```text
src/
│
├── app/
├── assets/
├── boot/
├── components/
├── composables/
├── css/
├── features/
├── layouts/
├── pages/
├── router/
├── services/
├── stores/
├── types/
├── utils/
│
├── App.vue
└── main.ts
```

---

# app/

Responsável pela inicialização da aplicação.

Exemplos:

- configuração global
- providers
- constantes
- configuração da aplicação

---

# assets/

Recursos estáticos.

Exemplos:

- imagens
- ícones
- logos
- fontes

---

# boot/

Arquivos de bootstrap do Quasar.

Exemplos:

- axios
- i18n
- dayjs
- plugins
- máscaras
- configuração global

Cada arquivo deverá possuir uma única responsabilidade.

---

# components/

Componentes compartilhados.

Exemplo:

```text
components/

Button/

Card/

DataTable/

Dialog/

Form/

Input/

Loading/

PageHeader/

Pagination/

Search/

StatusBadge/
```

Nenhum componente deste diretório poderá depender de uma Feature específica.

---

# composables/

Lógicas reutilizáveis.

Exemplos:

```text
useApi()

useDialog()

useFilters()

useLoading()

usePagination()

usePermissions()

useValidation()
```

Todos deverão ser independentes do domínio.

---

# css/

Estilos globais.

Exemplo:

```text
css/

variables.scss

theme.scss

animations.scss

utilities.scss
```

---

# features/

Contém todas as funcionalidades de negócio.

Estrutura:

```text
features/

area/

singular/

equipe/

colaborador/

documento/

usuario/

comunicado/

notificacao/
```

Cada Feature é completamente isolada.

---

# Estrutura de uma Feature

```text
feature/

components/

composables/

models/

pages/

services/

stores/

types/

validators/
```

## components/

Componentes exclusivos da Feature.

---

## composables/

Lógica reutilizável apenas pela Feature.

---

## models/

Interfaces e modelos.

---

## pages/

Páginas da Feature.

---

## services/

Comunicação com backend.

Não deve conter regras visuais.

---

## stores/

Estado **exclusivo da Feature**.

Somente stores do domínio da Feature neste diretório — nunca Auth, Session ou App.

Exemplo:

```text
features/area/stores/area.store.ts
features/singular/stores/singular.store.ts
```

Não substituir `src/stores/session.store.ts`. Consumir contexto global via Session Store.

---

## types/

Tipos TypeScript específicos.

---

## validators/

Validações da Feature.

---

# layouts/

Layouts da aplicação.

Exemplo:

```text
layouts/

AuthLayout

DashboardLayout

PublicLayout
```

Responsáveis apenas pela composição visual.

---

# pages/

Páginas institucionais ou globais.

Exemplo:

```text
Login

Callback

403

404

500
```

As páginas de negócio pertencem às respectivas Features.

---

# router/

Configuração de rotas.

Estrutura sugerida:

```text
router/

index.ts

guards/

routes/
```

---

## guards/

Exemplos:

- auth
- permissions
- context
- guest

---

## routes/

Separação por módulos.

Exemplo:

```text
auth.routes.ts

area.routes.ts

equipe.routes.ts

documento.routes.ts
```

---

# services/

Infraestrutura compartilhada.

Exemplo:

```text
ApiClient

Http

Interceptors

Storage

Session

Notification
```

Nenhum service deste diretório implementa regras de negócio.

---

# stores/

Stores de **infraestrutura global** — Frontend Foundation.

Somente três categorias neste diretório:

```text
stores/
├── auth.store.ts       ← SSOT conceitual — autenticação
├── session.store.ts    ← SSOT conceitual — contexto do usuário
└── app.store.ts        ← UI global (opcional)
```

Nenhuma store de Feature de negócio pertence a `src/stores/`.

---

## `auth.store.ts`

**Caminho:** `src/stores/auth.store.ts`

Responsável **exclusivamente** pelo ciclo de autenticação (FT-AUTH).

| Responsabilidade | Descrição |
|------------------|-----------|
| `login` | Iniciar fluxo redirect Zimbra |
| `logout` | Encerrar sessão e limpar cookies |
| `refresh` | Renovar access token |
| `isAuthenticated` | Indicador derivado do token válido |
| `expiresAt` | Controle de expiração do access token |
| `cookies` / `csrf` | Coordenação com política HttpOnly + `X-XSRF-TOKEN` |

**Não contém:** usuário, organização, permissões, menus ou dados de negócio.

**Consome:** `services/` (ApiClient, AuthService).

**Consumida por:** router guards (auth), boot axios (interceptors), fluxo de callback.

---

## `session.store.ts`

**Caminho:** `src/stores/session.store.ts`

Responsável pelo **contexto resolvido** após autenticação.

| Responsabilidade | Descrição |
|------------------|-----------|
| `user` | Colaborador autenticado (`/api/v1/auth/me`) |
| `organization` | Federação / singular ativa |
| `activeArea` | Área selecionada |
| `activeTeam` | Equipe selecionada |
| `roles` | Papéis retornados pelo backend |
| `permissions` | Permissões para guards e menus |
| `menus` | Estrutura de navegação dinâmica |
| `dashboard` | Rota inicial resolvida |
| `context` | Snapshot do contexto atual |

**Não contém:** login, logout, refresh, cookies ou CSRF.

**Consome:** Auth Store (sessão válida), `services/` (SessionService, API).

**Consumida por:** layouts, router guards (permissions, context), **todas as Features**.

Todas as Features deverão consumir exclusivamente esta store para contexto global.

---

## `app.store.ts` (opcional)

Estado de UI transversal: sidebar, preferências visuais, tema. Sem domínio de negócio.

---

## Stores por Feature

Localização obrigatória:

```text
src/features/<feature>/stores/<feature>.store.ts
```

Exemplos: `area.store.ts`, `equipe.store.ts`, `documento.store.ts`.

Regras: ver `frontend-architecture.md` § Feature Stores.

---

# types/

Tipos compartilhados.

Exemplo:

```text
ApiResponse

PageResponse

ErrorResponse

SelectOption

Pagination
```

---

# utils/

Funções utilitárias.

Exemplos:

```text
date.ts

format.ts

mask.ts

number.ts

string.ts
```

Sem dependência do Vue.

---

# Convenções

## Imports

Preferir aliases.

Exemplo:

```typescript
import { useSessionStore } from '@/stores/session.store';
```

---

## Componentes

- Um componente por arquivo.
- Responsabilidade única.
- Nomes em PascalCase.

---

## Services

- Uma responsabilidade.
- Sem acesso ao DOM.
- Sem manipulação de componentes.

---

## Stores

- Estado apenas.
- Sem chamadas diretas à interface.
- Sem dependência de componentes.
- **Globais** somente em `src/stores/` (auth, session, app).
- **Por Feature** somente em `src/features/<feature>/stores/`.

---

## Features

Nenhuma Feature poderá acessar diretamente outra Feature.

A comunicação ocorrerá através de:

- `src/stores/session.store.ts`
- Services compartilhados (`src/services/`)
- API

---

# Fluxo de Dependências

```text
View

↓

Composable

↓

Service

↓

API
```

A camada superior nunca deverá ser utilizada pela inferior.

---

# Organização de Novas Features

Toda nova Feature deverá seguir exatamente esta estrutura.

Não será permitido criar diretórios paralelos ou alterar o padrão estabelecido sem aprovação arquitetural.

---

# Resultado Esperado

Ao final da Frontend Foundation, toda implementação deverá seguir esta estrutura, garantindo padronização, baixo acoplamento, facilidade de manutenção e evolução contínua do Portal de Comunicação.