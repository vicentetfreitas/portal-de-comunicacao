# PKG-FE-S0-07 — Authentication Integration

| Campo | Valor |
|-------|-------|
| Status | **DONE** |
| Sprint | Frontend Foundation (Sprint 0) |
| Dependência | PKG-FE-S0-05, PKG-FE-S0-06 |

## Entregas

### Auth module (`src/auth/`)

| Artefato | Responsabilidade |
|----------|------------------|
| `types.ts` | `AuthenticatedUser`, status de sessão, interfaces FT-AUTH (`AuthSessionHydrator`, `AuthLoginHandler`, etc.) |
| `auth-context.ts` | Contexto global para guards e HTTP (`setAuthContext`, `authContext`) |
| `auth-context-bridge.ts` | Mapeamento store Pinia → `AuthContext` |
| `bind-auth-context.ts` | `bindAuthStoreToContext()` |
| `storage-policy.ts` | RN-AUTH-007 — detecção e guarda DEV contra tokens em `localStorage`/`sessionStorage` |
| `csrf.ts` | `validateCsrfInfrastructure()` — validação estática da infra CSRF (PKG-FE-S0-06) |
| `index.ts` | Barrel exports |

### Auth store (`src/stores/auth-store.ts`)

| Capacidade | Detalhe |
|------------|---------|
| Estado estrutural | `status`, `user`, `isAuthenticated`, `permissions`, `roles` |
| Autorização | `hasRole`, `hasAnyRole`, `hasCapability`, `hasAnyCapability` |
| Sessão | `setSession`, `clearSession`, `markUnauthenticated`, `hydrateSession` (stub — sem API) |

### Composable e contratos

| Artefato | Responsabilidade |
|----------|------------------|
| `composables/useAuth.ts` | Facade reativa para componentes |
| `config/auth.ts` | `AUTH_API_PATHS`, `AUTH_COOKIE_NAMES` (HttpOnly — não lidos pelo JS) |
| `services/auth/auth-contracts.ts` | Contrato `AuthService` e tipos `AuthMeApiResponse` para FT-AUTH |

### Integração

| Artefato | Detalhe |
|----------|---------|
| `boot/auth.ts` | Storage guard, bind store→context, `setUnauthorizedHandler` (sem refresh), validação CSRF em DEV |
| `quasar.config.ts` | Boot `auth` após `http` |
| `router/guards/auth.guard.ts` | Import de `@/auth` |
| `router/guards/authorization.guard.ts` | Import de `@/auth` |
| `router/auth/auth-context.ts` | Re-export deprecado → `@/auth` |

### Políticas e limites de escopo

- **Sem** login, logout, refresh token ou chamadas HTTP de autenticação
- **Sem** JWT em `localStorage` ou `sessionStorage` (guarda em DEV + auditoria)
- Cookies `access_token` / `refresh_token` permanecem HttpOnly — apenas `XSRF-TOKEN` é lido pelo cliente
- `setUnauthorizedHandler` limpa sessão e retorna `false` (retry delegado a FT-AUTH)
- Guards com `enforceAuthentication` / `enforceAuthorization` ainda desabilitados em `config/router.ts`

## Validações locais

| Comando | Resultado |
|---------|-----------|
| `yarn typecheck` | ✅ exit 0 |
| `yarn build` | ✅ exit 0 (`dist/spa`) |

## Critérios (AC-FE-S0-011, AC-FE-S0-012)

| Critério | Atendido |
|----------|----------|
| Auth store Pinia estrutural | ✅ |
| Ausência de tokens em storage local | ✅ |
| CSRF validado (infraestrutura PKG-FE-S0-06) | ✅ |
| Pontos de extensão refresh (`setUnauthorizedHandler`) | ✅ |
| Guard scaffolding integrado ao contexto | ✅ |
| Interfaces preparadas para FT-AUTH | ✅ |
| Sem login/API/refresh implementados | ✅ |

## Notas

- FT-AUTH implementará `hydrateSession()` via `GET /auth/me`, login redirect e refresh em `setUnauthorizedHandler`
- `validateCsrfInfrastructure()` pode ser reutilizado em testes de fumaça (PKG-FE-S0-09)
- Ativação de enforcement de rotas: `routerGuardConfig.enforceAuthentication = true` em FT-AUTH
