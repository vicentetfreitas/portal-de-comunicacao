# API — Authentication

| Item | Valor |
|------|-------|
| Feature ID | **FT-AUTH** |
| Projeto | Portal de Comunicação |
| Camada | Features |
| Status | **Approved** |
| Versão | 2.2 |
| Última atualização | 2026-07-09 |

---

## Objetivo

Definir os contratos da API de autenticação Stateless com JWT, Refresh Token, Cookies HttpOnly e integração Zimbra.

Arquitetura completa: `specs/architecture/authentication-architecture.md`

Padrões de resposta: `docs/implementation/07-api-standards.md` (Sprint 0).

---

# Visão Geral

O Portal autentica colaboradores via **Zimbra** (consulta única no login), emite **JWT próprio** e **Refresh Token**, armazena em **Cookies HttpOnly + Secure** e controla sessão sem HTTP Session.

Prefixo oficial: `/api/v1`

---

# Fluxo das Chamadas

```text
Frontend ──GET /api/v1/auth/login──► Backend ──redirect──► Zimbra
Zimbra ──callback──► GET /api/v1/auth/callback ──► Backend (emite cookies)
Frontend ──GET /api/v1/auth/me──► Backend (valida JWT do cookie)
Frontend ──POST /api/v1/auth/refresh──► Backend (renova Access Token)
Frontend ──POST /api/v1/auth/logout──► Backend (revoga + remove cookies)
Administrador ──revoga session_id──► Backend (RF-AUTH-010; ver seção abaixo)
```

---

# Cookies

| Cookie | Conteúdo | Flags | TTL | Path |
|--------|----------|-------|-----|------|
| `access_token` | JWT (Access Token) | HttpOnly, Secure, SameSite=Strict | 15 min | `/` |
| `refresh_token` | UUID opaco (Refresh Token) | HttpOnly, Secure, SameSite=Strict | 8h / 30d | `/api/v1/auth` |
| `XSRF-TOKEN` | Token CSRF | Secure, SameSite=Strict | Sessão | `/` |

**Proibido:** armazenar tokens em LocalStorage ou SessionStorage.

---

# CSRF

Requisições mutáveis (`POST`, `PUT`, `DELETE`) autenticadas por Cookie devem incluir:

```http
X-XSRF-TOKEN: {valor do cookie XSRF-TOKEN}
```

---

# Padrões de Resposta

## Sucesso — ApiResponse\<T\>

```json
{
  "timestamp": "2026-07-08T20:00:00Z",
  "success": true,
  "data": { }
}
```

## Erro — ErrorResponse

```json
{
  "timestamp": "2026-07-08T20:00:00Z",
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Sessão expirada",
  "path": "/api/v1/auth/me"
}
```

---

# Endpoints

| Operação | Método | Endpoint | Autenticação |
|----------|--------|----------|--------------|
| Iniciar login | GET | `/api/v1/auth/login` | Não |
| Callback Zimbra | GET | `/api/v1/auth/callback` | Fluxo Zimbra |
| Consultar identidade | GET | `/api/v1/auth/me` | Cookie `access_token` |
| Renovar Access Token | POST | `/api/v1/auth/refresh` | Cookie `refresh_token` + CSRF |
| Encerrar sessão | POST | `/api/v1/auth/logout` | Cookie + CSRF |
| Revogar sessão (admin) | DELETE | `/api/v1/admin/sessions/{sessionId}` | Cookie `access_token` + CSRF |

---

# Operações

## AUTH-API-001 — Iniciar Login

### Endpoint

`GET /api/v1/auth/login`

### Query Parameters

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `remember_me` | boolean | Não | Habilita TTL estendido do Refresh Token (30 dias) |

### Comportamento

1. Gera `state` anti-CSRF e armazena temporariamente
2. Redireciona ao Zimbra (`ZIMBRA_AUTH_URL`)

### Respostas

| Código | Situação |
|--------|----------|
| 302 | Redirecionamento ao Zimbra |
| 503 | Zimbra indisponível |
| 500 | Erro interno |

### Requisitos

RF-AUTH-001 | UC-AUTH-001 | AC-AUTH-001, AC-AUTH-014

---

## AUTH-API-002 — Callback de Autenticação

### Endpoint

`GET /api/v1/auth/callback`

### Comportamento

1. Valida `state` anti-CSRF
2. Consulta Zimbra para confirmar identidade (**única consulta**)
3. Localiza ou cria Colaborador no banco
4. Verifica autorização para o Portal
5. Verifica limite de sessões (máx. 3)
6. Emite Access Token (JWT) e Refresh Token
7. Registra sessão em `AUTH_SESSAO`
8. Define cookies HttpOnly + Secure
9. Redireciona Frontend para área autenticada

### Respostas

| Código | Situação | Body |
|--------|----------|------|
| 302 | Sucesso — redireciona com cookies | — |
| 400 | Resposta Zimbra inválida / state inválido | ErrorResponse |
| 401 | Autenticação não realizada | ErrorResponse |
| 403 | Colaborador sem autorização no Portal | ErrorResponse |
| 503 | Zimbra indisponível / timeout | ErrorResponse |
| 500 | Erro interno | ErrorResponse |

### Exemplo — Erro 403

```json
{
  "timestamp": "2026-07-08T20:00:00Z",
  "status": 403,
  "error": "FORBIDDEN",
  "message": "Colaborador sem autorização para acessar o Portal",
  "path": "/api/v1/auth/callback"
}
```

### Requisitos

RF-AUTH-001, RF-AUTH-002, RF-AUTH-003, RF-AUTH-008, RF-AUTH-009 | UC-AUTH-001

---

## AUTH-API-003 — Consultar Colaborador Autenticado

### Endpoint

`GET /api/v1/auth/me`

### Autenticação

Cookie `access_token` (JWT)

### Comportamento

1. Valida JWT do cookie (assinatura, expiração)
2. Carrega dados do colaborador e permissões do banco
3. Retorna `ApiResponse<AuthenticatedUserResponse>`

### Resposta 200 — ApiResponse

```json
{
  "timestamp": "2026-07-08T20:00:00Z",
  "success": true,
  "data": {
    "id": 42,
    "email": "colaborador@unimedceara.com.br",
    "name": "João Silva",
    "permissions": ["DOCUMENT_READ", "DOCUMENT_WRITE"],
    "sessionId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  }
}
```

### AuthenticatedUserResponse

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | long | `COD_COLABORADOR` |
| `email` | string | E-mail corporativo |
| `name` | string | Nome do colaborador |
| `permissions` | string[] | Permissões do banco do Portal |
| `sessionId` | string | Identificador da sessão |

### Respostas

| Código | Situação | Body |
|--------|----------|------|
| 200 | Sucesso | ApiResponse\<AuthenticatedUserResponse\> |
| 401 | Token ausente, expirado ou inválido | ErrorResponse |
| 500 | Erro interno | ErrorResponse |

### Requisitos

RF-AUTH-005, RF-AUTH-007 | UC-AUTH-004 | AC-AUTH-007

---

## AUTH-API-004 — Encerrar Sessão (Logout)

### Endpoint

`POST /api/v1/auth/logout`

### Autenticação

Cookies + CSRF (`X-XSRF-TOKEN`)

### Comportamento

1. Valida CSRF
2. Revoga Refresh Token da sessão atual no banco (`FLG_REVOGADA = S`)
3. Remove cookies `access_token` e `refresh_token`
4. Registra auditoria (logout)

> **Nota (RN-AUTH-012):** o Access Token **não** é invalidado imediatamente — permanece válido até o TTL natural de 15 minutos. Após expiração, renovação com Refresh revogado retorna HTTP 401.

### Respostas

| Código | Situação |
|--------|----------|
| 204 | Sessão encerrada (sem body) |
| 403 | CSRF inválido — ErrorResponse |
| 500 | Erro interno — ErrorResponse |

### Requisitos

RF-AUTH-006 | RN-AUTH-008, RN-AUTH-012 | UC-AUTH-002 | AC-AUTH-004

---

## AUTH-API-005 — Renovar Access Token (Refresh)

### Endpoint

`POST /api/v1/auth/refresh`

### Autenticação

Cookie `refresh_token` + CSRF (`X-XSRF-TOKEN`)

### Comportamento

1. Valida CSRF
2. Extrai Refresh Token do cookie
3. Valida no banco (hash, expiração, não revogado — inclui revogação por logout, administrativa ou limite de sessões)
4. Emite novo Access Token (JWT, 15 min)
5. Atualiza cookie `access_token`
6. Registra auditoria (renovação)

Quando o Refresh Token está revogado (`FLG_REVOGADA = S`), inclusive após **revogação administrativa** (RF-AUTH-010, RN-AUTH-011):

- retorna HTTP 401;
- remove cookies `access_token` e `refresh_token`;
- o frontend deve redirecionar para novo login.

> **Nota (RN-AUTH-012):** a revogação não invalida o Access Token imediatamente. Enquanto o JWT não expirar, requisições autenticadas por cookie continuam aceitas (validação local). A continuidade da sessão é interrompida na primeira tentativa de renovação após expiração do Access Token.

### Resposta 200 — ApiResponse

```json
{
  "timestamp": "2026-07-08T20:15:00Z",
  "success": true,
  "message": "Access token renovado",
  "data": null
}
```

### Respostas

| Código | Situação | Body |
|--------|----------|------|
| 200 | Access Token renovado | ApiResponse |
| 401 | Refresh expirado ou revogado — cookies removidos | ErrorResponse |
| 403 | CSRF inválido | ErrorResponse |
| 500 | Erro interno | ErrorResponse |

### Exemplo — Erro 401 (Refresh expirado)

```json
{
  "timestamp": "2026-07-08T20:15:00Z",
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Refresh token expirado",
  "path": "/api/v1/auth/refresh"
}
```

### Requisitos

RF-AUTH-004, RF-AUTH-010 | RN-AUTH-011, RN-AUTH-012 | UC-AUTH-005 | AC-AUTH-008, AC-AUTH-009, AC-AUTH-010

---

## Revogação Administrativa (RF-AUTH-010)

### Endpoint

`DELETE /api/v1/admin/sessions/{sessionId}`

### Autenticação

Cookie `access_token` + CSRF (`X-XSRF-TOKEN`)

### Path Parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `sessionId` | string | Identificador público da sessão (`session_id`) |

### Comportamento

1. Administrador autenticado identifica `session_id` da sessão alvo
2. Backend marca `FLG_REVOGADA = S` em `AUTH_SESSAO`
3. Backend registra auditoria (revogação administrativa)
4. Cookies do colaborador **não** são removidos neste momento
5. Renovações futuras via AUTH-API-005 retornam HTTP 401 e removem cookies
6. Access Token residual permanece válido até TTL de 15 minutos (RN-AUTH-012)
7. Novo login obrigatório para criar nova sessão

### Respostas

| Código | Situação |
|--------|----------|
| 204 | Sessão revogada ou já revogada (idempotente) |
| 401 | Não autenticado |
| 403 | CSRF inválido |
| 404 | Sessão não encontrada |
| 500 | Erro interno |

### Regras

RN-AUTH-011, RN-AUTH-012

### Requisitos

RF-AUTH-010 | UC-AUTH-006 | AC-AUTH-010 | TASK-AUTH-BE-020

---

# Códigos de Resposta

| Código | Significado |
|--------|-------------|
| 200 | Operação com sucesso (ApiResponse) |
| 204 | Logout sem body |
| 302 | Redirecionamento (login, callback) |
| 400 | Requisição inválida |
| 401 | Não autenticado / token expirado |
| 403 | Sem autorização / CSRF inválido |
| 500 | Erro interno |
| 503 | Zimbra indisponível |

---

# Tratamento de Erros

| Situação | Código | error |
|----------|--------|-------|
| Credenciais inválidas (Zimbra) | 401 | UNAUTHORIZED |
| Colaborador sem autorização | 403 | FORBIDDEN |
| Access Token expirado | 401 | UNAUTHORIZED |
| Refresh Token expirado | 401 | UNAUTHORIZED |
| Refresh Token revogado | 401 | UNAUTHORIZED |
| CSRF inválido | 403 | FORBIDDEN |
| State/nonce inválido | 400 | VALIDATION_ERROR |
| Zimbra indisponível | 503 | SERVICE_UNAVAILABLE |
| Timeout Zimbra | 503 | SERVICE_UNAVAILABLE |
| Erro interno | 500 | INTERNAL_SERVER_ERROR |

---

# Auditoria

Eventos registrados (sem dados sensíveis):

- Login bem-sucedido
- Login falho
- Logout
- Renovação de Access Token
- Expiração de sessão
- Revogação administrativa
- Indisponibilidade Zimbra

---

# Rastreabilidade

| API | RF | RN | UC | AC | TASK |
|-----|-----|-----|-----|-----|------|
| AUTH-API-001 | RF-AUTH-001 | RN-AUTH-002 | UC-AUTH-001 | AC-AUTH-001, AC-AUTH-014 | BE-003 |
| AUTH-API-002 | RF-AUTH-001–003, 008, 009 | RN-AUTH-005, RN-AUTH-009, RN-AUTH-012 | UC-AUTH-001 | AC-AUTH-001, AC-AUTH-002, AC-AUTH-011, AC-AUTH-012, AC-AUTH-013 | BE-004, BE-013–019 |
| AUTH-API-003 | RF-AUTH-005, 007 | RN-AUTH-001, RN-AUTH-006 | UC-AUTH-004 | AC-AUTH-007 | BE-005 |
| AUTH-API-004 | RF-AUTH-006 | RN-AUTH-008, RN-AUTH-012 | UC-AUTH-002 | AC-AUTH-004 | BE-006, BE-017 |
| AUTH-API-005 | RF-AUTH-004, RF-AUTH-010 | RN-AUTH-011, RN-AUTH-012 | UC-AUTH-005 | AC-AUTH-008, AC-AUTH-009, AC-AUTH-010 | BE-016 |
| RF-AUTH-010 (admin) | RF-AUTH-010 | RN-AUTH-011, RN-AUTH-012 | UC-AUTH-006 | AC-AUTH-010 | BE-020 |

---

# Referências

- `specification.md`
- `use-cases.md`
- `specs/architecture/authentication-architecture.md`
- `docs/implementation/07-api-standards.md`
