# PKG-FE-S0-05 — Routing

| Campo | Valor |
|-------|-------|
| Status | **DONE** |
| Sprint | Frontend Foundation (Sprint 0) |
| Dependência | PKG-FE-S0-04 |

## Entregas

### Router modular (`src/router/`)

| Módulo | Responsabilidade |
|--------|------------------|
| `routes/structural.routes.ts` | Redirects estruturais (`/login` → `/auth`) |
| `routes/foundation.routes.ts` | Rotas foundation com lazy loading e meta padronizada |
| `routes/index.ts` | Registry modular + catch-all 404 |
| `guards/auth.guard.ts` | Scaffold autenticação (FT-AUTH) |
| `guards/authorization.guard.ts` | Scaffold autorização via `meta.roles` / `meta.capabilities` |
| `guards/document-title.guard.ts` | Título do documento via `meta.pageTitleKey` |
| `auth/auth-context.ts` | Stub de contexto auth (sem store de domínio) |

### RouteMeta padronizado (`types/router-meta.ts`)

- `layout`, `pageTitleKey`, `documentTitle`
- `showBreadcrumbs`, `breadcrumbs`
- `public`, `requiresAuth`, `guestOnly`
- `roles`, `capabilities`

### Constantes e configuração

- `constants/routes.ts` — paths e names centralizados
- `config/router.ts` — flags `enforceAuthentication` / `enforceAuthorization` (desabilitadas na Sprint 0)

### Rotas foundation (lazy loading)

| Rota | Layout | Meta relevante |
|------|--------|----------------|
| `/` | public | `public: true` |
| `/showcase` | main | breadcrumbs |
| `/auth` | auth | `guestOnly: true` |
| `/app` | main | `requiresAuth: true` |
| `/admin` | admin | `requiresAuth`, `roles: ['ADMIN']` |
| `/unauthorized` | public | página de acesso negado |
| `/*` | public | 404 |

### Outras alterações

- `filenameBasedRouting: false` — rotas manuais modulares com `import()` lazy
- `i18n/instance.ts` — instância compartilhada (boot + guards)
- Páginas `app/index.vue` e `unauthorized.vue` criadas
- Meta removida dos SFCs — fonte única em `foundation.routes.ts`

## Validações locais

| Comando | Resultado |
|---------|-----------|
| `yarn typecheck` | ✅ exit 0 |
| `yarn build` | ✅ exit 0 (`dist/spa`) |

## Critérios (AC-FE-S0-008, AC-FE-S0-014)

| Critério | Atendido |
|----------|----------|
| Vue Router history mode | ✅ |
| Rota 404 funcional | ✅ |
| Redirect estrutural `/login` → `/auth` | ✅ |
| Área `/app` placeholder autenticada | ✅ |
| Guards globais registrados | ✅ |
| Meta conventions (título, roles, capabilities) | ✅ |
| Lazy loading em todas as páginas | ✅ |
| Sem autenticação/API/stores de domínio | ✅ |

## Notas

- Guards com enforcement desabilitado — ativação em FT-AUTH via `routerGuardConfig` e `setAuthContext()`
- Features adicionam rotas em módulos dedicados seguindo o mesmo padrão
