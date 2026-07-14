# Testes de Aceitação — Authentication

| Item | Valor |
|------|-------|
| Feature ID | **FT-AUTH** |
| Projeto | Portal de Comunicação |
| Camada | Features |
| Status | **Approved** |
| Versão | 2.1 |
| Última atualização | 2026-07-09 |

---

## Objetivo

Definir critérios de validação da Feature Authentication com arquitetura Stateless, JWT, Refresh Token e integração Zimbra.

---

# Cenários de Aceitação

## AC-AUTH-001 — Autenticação com sucesso

### Objetivo

Validar login via Zimbra com emissão de tokens em Cookies HttpOnly.

### Pré-condições

- Colaborador com conta válida no Zimbra
- Zimbra disponível

### Fluxo

1. Colaborador acessa Portal
2. `GET /api/v1/auth/login` → redirect Zimbra
3. Colaborador autentica no Zimbra
4. `GET /api/v1/auth/callback` → cookies emitidos
5. `GET /api/v1/auth/me` → identidade retornada

### Resultado Esperado

- Cookies `access_token` e `refresh_token` definidos (HttpOnly + Secure)
- `ApiResponse` com dados do colaborador
- Permissões carregadas do banco do Portal
- Nenhuma consulta ao Zimbra após callback

### RF | UC | API | TASK

RF-AUTH-001, 002, 003, 008 | UC-AUTH-001 | AUTH-API-001, 002, 003 | BE-003, BE-004, BE-013–015

---

## AC-AUTH-002 — Credenciais inválidas

### Objetivo

Validar que credenciais inválidas no Zimbra não geram sessão.

### Resultado Esperado

- Zimbra recusa autenticação
- Nenhum cookie emitido
- Nenhuma sessão registrada no banco

### RF | UC | API | TASK

RF-AUTH-011 | UC-AUTH-001 | AUTH-API-002 | BE-009

---

## AC-AUTH-003 — Colaborador sem autorização no Portal

### Objetivo

Validar que colaborador autenticado no Zimbra sem autorização no Portal é bloqueado.

### Resultado Esperado

- HTTP 403 no callback
- Nenhum cookie emitido
- `ErrorResponse` com `FORBIDDEN`

### RF | UC | API | TASK

RF-AUTH-007 | UC-AUTH-001, UC-AUTH-003 | AUTH-API-002 | BE-010

---

## AC-AUTH-004 — Logout

### Objetivo

Validar encerramento de sessão com revogação de Refresh Token.

### Pré-condições

- Sessão autenticada válida

### Fluxo

1. `POST /api/v1/auth/logout` com CSRF
2. Cookies removidos

### Resultado Esperado

- HTTP 204
- Refresh Token revogado no banco (`FLG_REVOGADA = S`)
- Cookies `access_token` e `refresh_token` removidos
- `GET /api/v1/auth/me` retorna 401

### RF | UC | API | TASK

RF-AUTH-006 | UC-AUTH-002 | AUTH-API-004 | BE-006, BE-017

---

## AC-AUTH-005 — Access Token expirado

### Objetivo

Validar comportamento quando Access Token expira.

### Pré-condições

- Sessão com Access Token expirado
- Refresh Token ainda válido

### Fluxo

1. `GET /api/v1/auth/me` → HTTP 401
2. Frontend aciona `POST /api/v1/auth/refresh`
3. Novo Access Token emitido
4. `GET /api/v1/auth/me` → HTTP 200

### Resultado Esperado

- Renovação automática via Refresh Token
- Colaborador permanece autenticado sem novo login

### RF | UC | API | TASK

RF-AUTH-004, 007 | UC-AUTH-003, UC-AUTH-005 | AUTH-API-003, 005 | BE-016, FE-007

---

## AC-AUTH-006 — Zimbra indisponível

### Objetivo

Validar tratamento de indisponibilidade do Zimbra.

### Pré-condições

- Zimbra indisponível ou timeout (>10s)

### Resultado Esperado

- HTTP 503
- `ErrorResponse` com mensagem apropriada
- Nenhuma sessão criada
- Evento de auditoria registrado

### RF | UC | API | TASK

RF-AUTH-011 | UC-AUTH-001 | AUTH-API-001, 002 | INT-003

---

## AC-AUTH-007 — Consultar colaborador autenticado

### Objetivo

Validar retorno de identidade via `ApiResponse`.

### Pré-condições

- Access Token válido

### Resultado Esperado

- HTTP 200 com `ApiResponse<AuthenticatedUserResponse>`
- Campos: `id`, `email`, `name`, `permissions`, `sessionId`
- Permissões originadas do banco do Portal
- Nenhum dado sensível exposto

### RF | UC | API | TASK

RF-AUTH-005 | UC-AUTH-004 | AUTH-API-003 | BE-005, FE-003

---

## AC-AUTH-008 — Refresh válido

### Objetivo

Validar renovação de Access Token com Refresh Token válido.

### Pré-condições

- Refresh Token válido e não revogado

### Fluxo

1. `POST /api/v1/auth/refresh` com CSRF
2. Novo cookie `access_token` emitido

### Resultado Esperado

- HTTP 200 com `ApiResponse`
- Novo Access Token com TTL de 15 minutos
- Refresh Token inalterado
- Auditoria de renovação registrada

### RF | UC | API | TASK

RF-AUTH-004 | UC-AUTH-005 | AUTH-API-005 | BE-016

---

## AC-AUTH-009 — Refresh expirado

### Objetivo

Validar comportamento quando Refresh Token expira.

### Pré-condições

- Refresh Token expirado

### Resultado Esperado

- HTTP 401 com `ErrorResponse`
- Cookies removidos
- Colaborador redirecionado para login

### RF | UC | API | TASK

RF-AUTH-004 | UC-AUTH-005 | AUTH-API-005 | BE-016, FE-007

---

## AC-AUTH-010 — Revogação administrativa

### Objetivo

Validar que sessão revogada administrativamente não pode ser renovada.

### Pré-condições

- Sessão ativa revogada via operação administrativa

### Resultado Esperado

- `POST /api/v1/auth/refresh` retorna HTTP 401
- Cookies removidos
- Colaborador deve autenticar novamente

### RF | UC | API | TASK

RF-AUTH-010 | UC-AUTH-006 | DELETE /api/v1/admin/sessions/{sessionId} | BE-020

---

## AC-AUTH-011 — Limite de sessões simultâneas

### Objetivo

Validar limite de 3 dispositivos por colaborador.

### Pré-condições

- Colaborador com 3 sessões ativas

### Fluxo

1. Novo login em 4º dispositivo
2. Sessão mais antiga revogada automaticamente

### Resultado Esperado

- Novo login bem-sucedido
- Sessão mais antiga com `FLG_REVOGADA = S`
- Máximo 3 sessões ativas mantidas

### RF | UC | API | TASK

RF-AUTH-009 | UC-AUTH-001 | AUTH-API-002 | BE-019

---

## AC-AUTH-012 — Cookies HttpOnly

### Objetivo

Validar que tokens são armazenados exclusivamente em Cookies HttpOnly + Secure.

### Resultado Esperado

- Cookies `access_token` e `refresh_token` possuem flags HttpOnly e Secure
- Tokens ausentes em LocalStorage e SessionStorage
- JavaScript não consegue ler cookies de token

### RF | UC | API | TASK

RF-AUTH-003 | UC-AUTH-001 | AUTH-API-002 | BE-015, SEC-002

---

## AC-AUTH-013 — "Lembrar-me"

### Objetivo

Validar TTL estendido do Refresh Token com "Lembrar-me".

### Pré-condições

- Login com `remember_me=true`

### Resultado Esperado

- Refresh Token com TTL de 30 dias
- Access Token mantém TTL de 15 minutos
- `FLG_REMEMBER_ME = S` na sessão

### RF | UC | API | TASK

RF-AUTH-002 | UC-AUTH-001 FA-005 | AUTH-API-001, 002 | BE-014

---

## AC-AUTH-014 — Falha do Zimbra no callback

### Objetivo

Validar tratamento de falha Zimbra durante callback.

### Resultado Esperado

- HTTP 503 ou 400 conforme tipo de falha
- Nenhum cookie emitido
- Auditoria de falha registrada

### RF | UC | API | TASK

RF-AUTH-011 | UC-AUTH-001 FA-003 | AUTH-API-002 | INT-003, BE-009

---

# Matriz de Cobertura

| RF | AC |
|----|-----|
| RF-AUTH-001 | AC-001, AC-014 |
| RF-AUTH-002 | AC-001, AC-013 |
| RF-AUTH-003 | AC-001, AC-012 |
| RF-AUTH-004 | AC-005, AC-008, AC-009 |
| RF-AUTH-005 | AC-007 |
| RF-AUTH-006 | AC-004 |
| RF-AUTH-007 | AC-003, AC-005 |
| RF-AUTH-008 | AC-001 |
| RF-AUTH-009 | AC-011 |
| RF-AUTH-010 | AC-010 |
| RF-AUTH-011 | AC-002, AC-006, AC-014 |

---

# Matriz de Rastreabilidade — Testes Automatizados

**Status da auditoria (2026-07-09):** 14/14 AC com cobertura automatizada (`mvn clean verify` — 183 testes, 0 falhas).

| AC | RF | Tipo | Classe de Teste | Método |
|----|-----|------|-----------------|--------|
| AC-AUTH-001 | RF-AUTH-001, 002, 003, 008 | API | `AuthAcceptanceIntegrationTest` | `acAuth001_shouldAuthenticateSuccessfullyWithHttpOnlyCookies` |
| AC-AUTH-002 | RF-AUTH-011 | API | `AuthAcceptanceIntegrationTest` | `acAuth002_shouldRejectInvalidCredentialsWithoutSession` |
| AC-AUTH-003 | RF-AUTH-007 | API | `AuthAcceptanceIntegrationTest` | `acAuth003_shouldRejectInactiveColaboradorWithForbidden` |
| AC-AUTH-004 | RF-AUTH-006 | API | `AuthAcceptanceIntegrationTest` | `acAuth004_shouldLogoutRevokeRefreshTokenAndClearCookies` |
| AC-AUTH-005 | RF-AUTH-004, 007 | API + Unit | `AuthAcceptanceIntegrationTest`, `JwtTokenServiceTest` | `acAuth005_shouldRenewAccessTokenWhenExpired`, `acAuth005_shouldRejectExpiredAccessToken` |
| AC-AUTH-006 | RF-AUTH-011 | API | `AuthAcceptanceIntegrationTest` | `acAuth006_shouldReturnServiceUnavailableWhenZimbraUnavailableOnLogin` |
| AC-AUTH-007 | RF-AUTH-005 | API | `AuthAcceptanceIntegrationTest` | `acAuth007_shouldReturnAuthenticatedUserViaApiResponse` |
| AC-AUTH-008 | RF-AUTH-004 | API | `AuthAcceptanceIntegrationTest` | `acAuth008_shouldRefreshAccessTokenWithValidRefreshToken` |
| AC-AUTH-009 | RF-AUTH-004 | API | `AuthAcceptanceIntegrationTest` | `acAuth009_shouldRejectExpiredRefreshToken` |
| AC-AUTH-010 | RF-AUTH-010 | API | `AuthAcceptanceIntegrationTest` | `acAuth010_shouldRejectRefreshAfterAdministrativeRevocation` |
| AC-AUTH-011 | RF-AUTH-009 | Integration | `AuthAcceptanceIntegrationTest` | `acAuth011_shouldRevokeOldestSessionWhenFourthDeviceLogsIn` |
| AC-AUTH-012 | RF-AUTH-003 | API + Unit | `AuthAcceptanceIntegrationTest`, `AuthCookieServiceTest` | `acAuth012_shouldSetHttpOnlyAndSecureFlagsOnTokenCookies`, `acAuth012_shouldConfigureHttpOnlyAndSecureOn*` |
| AC-AUTH-013 | RF-AUTH-002 | API | `AuthAcceptanceIntegrationTest` | `acAuth013_shouldExtendRefreshTokenTtlWithRememberMe` |
| AC-AUTH-014 | RF-AUTH-011 | API | `AuthAcceptanceIntegrationTest` | `acAuth014_shouldHandleZimbraFailureOnCallback` |

Anotação: `@AcceptanceCriterion("AC-AUTH-XXX")` em `backend/src/test/java/.../accesscontrol/`.

Cobertura complementar do fluxo feliz: `AuthFlowIntegrationTest.shouldCompleteLoginCallbackMeRefreshAndLogoutFlow`.

**Nota de implementação:** cenários AC-006 e AC-014 validam HTTP 503 com `error: INTEGRATION_UNAVAILABLE` (código em `IntegrationUnavailableException`), conforme handler global — divergente do rótulo `SERVICE_UNAVAILABLE` em `api.md`.

---

# Referências

- `specification.md`
- `use-cases.md`
- `api.md`
- `specs/architecture/authentication-architecture.md`
