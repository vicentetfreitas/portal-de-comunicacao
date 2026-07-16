# PKG-FE-S0-06 — HTTP Client

| Campo | Valor |
|-------|-------|
| Status | **DONE** |
| Sprint | Frontend Foundation (Sprint 0) |
| Dependência | PKG-FE-S0-01 |

## Entregas

### Tipos (`src/types/api/`)

| Tipo | Descrição |
|------|-----------|
| `ApiResponse<T>` | Envelope de sucesso (FT-AUTH / backend) |
| `ErrorResponseBody` | Envelope de erro padronizado |
| `ValidationErrorResponseBody` | Erro de validação com `errors[]` |
| `PageResponse<T>` | Paginação alinhada ao backend |
| `ProblemDetails` | RFC 7807 — extensão futura |
| `ApiError` | Erro normalizado + `normalizeApiError()` |

### Cliente HTTP (`src/services/http/`)

| Artefato | Responsabilidade |
|----------|------------------|
| `axios-instance.ts` | Instância única Axios (`getHttpClient`, `setupHttpClient`) |
| `base-api-client.ts` | `BaseApiClient` com unwrap de `ApiResponse` |
| `csrf.ts` | Leitura cookie `XSRF-TOKEN` |
| `correlation-id.ts` | Header `X-Correlation-Id` |
| `interceptors/request.interceptor.ts` | CSRF em métodos mutáveis, correlation ID |
| `interceptors/response.interceptor.ts` | Normalização de erros + hook `setUnauthorizedHandler` (FT-AUTH) |
| `error-handler.ts` | Tratamento centralizado + `setGlobalHttpErrorHandler` |

### Configuração

| Artefato | Detalhe |
|----------|---------|
| `config/env.ts` | `apiBaseUrl`, `apiTimeoutMs` |
| `config/http.ts` | Nomes CSRF/correlation e métodos mutáveis |
| `boot/http.ts` | Inicialização do cliente no bootstrap |
| `package.json` | Dependência `axios` |

### Contrato HTTP

- `baseURL`: `VITE_API_BASE_URL` (esperado `/api/v1`)
- `withCredentials: true`
- Timeout configurável (`VITE_API_TIMEOUT_MS`, default 30000)
- CSRF: header `X-XSRF-TOKEN` em POST/PUT/PATCH/DELETE quando cookie presente
- Sem chamadas de negócio nem endpoints de Features

## Validações locais

| Comando | Resultado |
|---------|-----------|
| `yarn install` | ✅ exit 0 |
| `yarn typecheck` | ✅ exit 0 |
| `yarn build` | ✅ exit 0 (`dist/spa`) |

## Critérios (AC-FE-S0-009, AC-FE-S0-010, AC-FE-S0-012)

| Critério | Atendido |
|----------|----------|
| Cliente aponta para `/api/v1` com `withCredentials` | ✅ |
| CSRF em requisições mutáveis | ✅ |
| Tipos `ApiResponse` / erro | ✅ |
| Interceptors request/response | ✅ |
| Sem autenticação/APIs de negócio | ✅ |

## Notas

- `setUnauthorizedHandler()` — extensão FT-AUTH (refresh/retry 401)
- `setGlobalHttpErrorHandler()` — feedback visual em PKG-FE-S0-08
- Feature services devem estender `BaseApiClient`
