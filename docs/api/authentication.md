# Authentication API

| Item | Valor |
|------|-------|
| Feature | FT-AUTH |
| Base path | `/api/v1/auth` |
| Admin path | `/api/v1/admin/sessions` |
| Contrato SDD | `specs/features/authentication/api.md` |
| Controller | `AuthController`, `AdminSessionController` |

---

## Visão geral do fluxo

```text
GET  /auth/login     → 302 página de login do Portal (credenciais)
POST /auth/login     → valida Zimbra + 302 Frontend + cookies
GET  /auth/callback  → 302 Frontend + cookies (token opaco)
GET  /auth/me        → identidade (autenticado)
POST /auth/refresh   → renova JWT
POST /auth/logout    → 204 revoga sessão
DELETE /admin/sessions/{sessionId} → 204 revogação admin
```

---

## GET /api/v1/auth/login

Inicia autenticação redirecionando à página de login do Portal (coleta de credenciais; validação no Zimbra via `POST /auth/login`).

| Aspecto | Valor |
|---------|-------|
| Autenticação | Não |
| CSRF | Não |

### Query parameters

| Parâmetro | Tipo | Padrão | Descrição |
|-----------|------|--------|-----------|
| `remember_me` | boolean | `false` | TTL estendido do refresh token (30 dias) |

### Respostas

| Código | Descrição |
|--------|-----------|
| 302 | Redirect para `application.zimbra.login-page-url` |
| 503 | Zimbra indisponível |

---

## POST /api/v1/auth/login

Valida e-mail e senha no Zimbra (IMAP/SMTP/SOAP) e conclui login.

| Aspecto | Valor |
|---------|-------|
| Autenticação | Não |
| CSRF | Sim (quando habilitado) |
| Content-Type | `application/x-www-form-urlencoded` |

### Form parameters

| Parâmetro | Obrigatório | Descrição |
|-----------|-------------|-----------|
| `email` | Sim | E-mail corporativo |
| `password` | Sim | Senha Zimbra |
| `remember_me` | Não | TTL estendido do refresh |
| `state` | Não | State anti-CSRF do `GET /login` |

### Respostas

| Código | Descrição |
|--------|-----------|
| 302 | Sucesso — redirect `frontend-redirect-url` + Set-Cookie |
| 401 | Credenciais inválidas |
| 403 | Colaborador inativo |
| 503 | Zimbra indisponível |

---

## GET /api/v1/auth/callback

Processa retorno do Zimbra.

### Query parameters

| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| `token` | string | Sim* | Token Zimbra |
| `state` | string | Sim | State anti-CSRF |

### Respostas

| Código | Descrição |
|--------|-----------|
| 302 | Sucesso — redirect `frontend-redirect-url` + Set-Cookie |
| 400 | State inválido |
| 401 | Identidade Zimbra inválida |
| 403 | Colaborador inativo ou sem autorização |
| 503 | Zimbra indisponível |

### Cookies definidos

`access_token`, `refresh_token` (ver [getting-started.md](./getting-started.md)).

---

## GET /api/v1/auth/me

Retorna identidade do colaborador autenticado.

| Aspecto | Valor |
|---------|-------|
| Autenticação | Cookie `access_token` |
| CSRF | Não |

### Response 200

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "success": true,
  "data": {
    "id": 1,
    "email": "colaborador@unimedceara.com.br",
    "name": "Colaborador Teste",
    "permissions": [],
    "sessionId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "organizationalLinks": {
      "federationId": 1,
      "singularId": null,
      "areaId": null,
      "teamId": null
    }
  }
}
```

### AuthenticatedUserResponse

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | long | `COD_COLABORADOR` |
| `email` | string | E-mail corporativo |
| `name` | string | Nome |
| `permissions` | string[] | **Atualmente sempre `[]`** (placeholder) |
| `sessionId` | string | Identificador da sessão |
| `organizationalLinks` | object | Vínculos de `COLABORADOR` (`federationId`, `singularId`, `areaId`, `teamId`) |

### Respostas

| Código | Descrição |
|--------|-----------|
| 200 | Sucesso |
| 401 | Token ausente, expirado ou inválido |
| 403 | Colaborador inativo |

---

## POST /api/v1/auth/refresh

Renova o access token a partir do refresh token.

| Aspecto | Valor |
|---------|-------|
| Autenticação | Cookie `refresh_token` |
| CSRF | Sim (`X-XSRF-TOKEN`) |
| Body | Nenhum |

### Response 200

```json
{
  "timestamp": "2026-07-16T17:15:00Z",
  "success": true,
  "message": "Access token renovado",
  "data": null
}
```

### Respostas

| Código | Descrição |
|--------|-----------|
| 200 | Novo `access_token` no Set-Cookie |
| 401 | Refresh expirado/revogado — cookies removidos |
| 403 | CSRF inválido |

---

## POST /api/v1/auth/logout

Encerra a sessão atual.

| Aspecto | Valor |
|---------|-------|
| CSRF | Sim |
| Body | Nenhum |

### Respostas

| Código | Descrição |
|--------|-----------|
| 204 | Sessão revogada, cookies removidos |
| 403 | CSRF inválido |

**Nota:** o access token permanece válido até expirar (15 min) — ver RN-AUTH-012 na spec.

---

## DELETE /api/v1/admin/sessions/{sessionId}

Revogação administrativa de sessão (RF-AUTH-010).

| Aspecto | Valor |
|---------|-------|
| Autenticação | Cookie `access_token` |
| Autorização | E-mail em `session-administrator-emails` |
| CSRF | Sim |

### Path parameters

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `sessionId` | string | `session_id` público da sessão alvo |

### Respostas

| Código | Descrição |
|--------|-----------|
| 204 | Sessão revogada (idempotente se já revogada) |
| 401 | Não autenticado |
| 403 | Não administrador / CSRF inválido |
| 404 | Sessão não encontrada |

---

## Configuração relevante

```yaml
application.security.jwt-access-ttl-minutes: 15
application.security.refresh-token-ttl-hours: 8
application.security.refresh-token-remember-me-days: 30
application.security.max-concurrent-sessions: 3
application.auth.session-administrator-emails: [...]
```

---

## Health (relacionado)

```http
GET /api/v1/health
```

Público — ver [README.md](./README.md).

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "success": true,
  "data": {
    "status": "UP",
    "application": "portal-comunicacao",
    "version": "0.0.1-SNAPSHOT"
  }
}
```
