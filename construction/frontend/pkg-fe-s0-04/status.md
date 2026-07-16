# PKG-FE-S0-04 — Layouts

| Campo | Valor |
|-------|-------|
| Status | **DONE** |
| Sprint | Frontend Foundation (Sprint 0) |
| Dependência | PKG-FE-S0-02, PKG-FE-S0-03 |

## Entregas

### Layouts (`src/layouts/`)

| Layout | Uso |
|--------|-----|
| `AuthLayout` | Fluxos de autenticação (estrutura, sem FT-AUTH) |
| `MainLayout` | Shell autenticado com sidebar colapsável |
| `AdminLayout` | Variante administrativa com badge e seção admin |
| `PublicLayout` | Páginas públicas com header/footer simplificados |

### App Shell (`src/components/app/`)

| Componente | Responsabilidade |
|------------|------------------|
| `AppShell` | Orquestra header, sidebar, breadcrumbs, conteúdo e footer |
| `AppHeader` | Brand, toggle menu, busca (DS), badge admin |
| `AppSidebar` | Navegação estrutural, menu colapsável (mini drawer) |
| `AppFooter` | Rodapé institucional |

### Infraestrutura

- `composables/useAppShell.ts` — estado drawer/collapse responsivo (sem store de domínio)
- `composables/useLayoutMeta.ts` — breadcrumbs via `route.meta`
- `constants/navigation.ts` — itens estruturais (home, showcase, admin)
- `types/router-meta.ts` — extensão `RouteMeta` (`layout`, `breadcrumbs`)
- `App.vue` — resolução dinâmica de layout via `route.meta.layout`
- Estilos com tokens: `layouts/layouts.scss` + scoped nos componentes

### Integração Vue Router

| Rota | Layout | Breadcrumbs |
|------|--------|-------------|
| `/` | public | — |
| `/showcase` | main | ✅ |
| `/auth` | auth | — |
| `/admin` | admin | ✅ |
| `/*` (404) | public | — |

Meta definida via bloco `<route lang="yaml">` em cada página.

## Validações locais

| Comando | Resultado |
|---------|-----------|
| `yarn typecheck` | ✅ exit 0 (após correção `useAppShell.ts`) |
| `yarn build` | ✅ exit 0 |

## Critérios (AC-FE-S0-007)

| Critério | Atendido |
|----------|----------|
| Quatro layouts implementados | ✅ |
| App shell (header, sidebar, footer) | ✅ |
| Responsividade mobile/tablet/desktop | ✅ |
| Menu colapsável | ✅ |
| Breadcrumbs quando previsto | ✅ |
| Design System + tokens exclusivamente | ✅ |
| Sem regras de negócio / APIs / stores de domínio | ✅ |

## Notas

- Menus contêm apenas rotas de infraestrutura (sem Features de negócio)
- Guards e redirects estruturais — escopo PKG-FE-S0-05
- `useTheme` — escopo PKG-FE-S0-08
