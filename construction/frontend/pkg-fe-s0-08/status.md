# PKG-FE-S0-08 — Shared Components

| Campo | Valor |
|-------|-------|
| Status | **DONE** |
| Sprint | Frontend Foundation (Sprint 0) |
| Dependência | PKG-FE-S0-03, PKG-FE-S0-06 |

## Entregas

### Composables (`src/composables/`)

| Composable | Responsabilidade |
|------------|------------------|
| `useTheme` | Modo claro/escuro/auto; `initTheme()` no boot; `setMode`, `toggle` |
| `useLoading` | Estado de carregamento; `withLoading()` para operações assíncronas |
| `useNotify` | Facade do wrapper DS Notify (`dsNotify*`) |
| `useStandardErrorHandling` | `handleError`, `withErrorHandling`; `registerGlobalHttpErrorHandler()` |
| `useFormValidation` | Regras genéricas: `required`, `email`, `minLength`, `maxLength` |
| `index.ts` | Barrel exports |

### Componentes compartilhados (`src/components/shared/`)

| Componente | Responsabilidade |
|------------|------------------|
| `AppEmptyState` | Estado vazio com ícone, título e slot de ações |
| `AppLoadingSpinner` | Spinner com label acessível |
| `AppLoadingSkeleton` | Skeleton configurável (linhas/tipo) |
| `AppErrorBoundary` | Captura erros de renderização com retry |

### Validação (`src/utils/validation/`)

| Artefato | Detalhe |
|----------|---------|
| `rules.ts` | Regras reutilizáveis e `runValidation()` |

### Constantes (`src/constants/`)

| Artefato | Detalhe |
|----------|---------|
| `layouts.ts` | `LAYOUT_NAMES`, `DEFAULT_LAYOUT` |
| `breadcrumbs.ts` | `BREADCRUMB_ICONS` (ícones estruturais MDI) |
| `index.ts` | Barrel (rotas + layouts + breadcrumbs) |

### Integração HTTP / feedback

| Artefato | Detalhe |
|----------|---------|
| `boot/feedback.ts` | Registra `setGlobalHttpErrorHandler` → `dsNotifyError` |
| `response.interceptor.ts` | `dispatchHttpError()` em falhas HTTP (exceto retry 401) |
| `boot/theme.ts` | Delega para `initTheme()` de `useTheme` |
| `quasar.config.ts` | Boot `feedback` após `auth` |

### Políticas

- Toast global suprime erros `authentication` (401 — FT-AUTH)
- Mensagens genéricas via `getDefaultHttpErrorMessage()` (sem regras de negócio)
- Wrapper Notify permanece em `components/ds/organisms/ds-notify` (PKG-FE-S0-03)

## Validações locais

| Comando | Resultado |
|---------|-----------|
| `yarn typecheck` | ✅ exit 0 |
| `yarn build` | ✅ exit 0 (`dist/spa`) |

## Critérios (AC-FE-S0-015)

| Critério | Atendido |
|----------|----------|
| Infraestrutura toast/notify para erro genérico | ✅ |
| Composables `useLoading`, `useStandardErrorHandling`, `useTheme` | ✅ |
| Validação base (required, email) | ✅ |
| Constantes de layout e breadcrumbs | ✅ |
| Feedback visual (spinner, skeleton, empty, error boundary) | ✅ |
| Sem telas ou regras de negócio | ✅ |

## Notas

- Features devem usar `useStandardErrorHandling` em catches locais para evitar toast duplicado com interceptor
- Testes automatizados de fumaça — escopo PKG-FE-S0-09
