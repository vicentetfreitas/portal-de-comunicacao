# Getting Started — API

## Base URL

```text
http://localhost:8080/api/v1
```

Em ambientes não locais, substituir host/porta conforme configuração de deploy.

---

## Autenticação

O backend utiliza autenticação **stateless** com JWT em **cookie HttpOnly** — não há suporte a `Authorization: Bearer` no filtro de segurança.

| Cookie | Conteúdo | Path | TTL |
|--------|----------|------|-----|
| `access_token` | JWT (HS256) | `/` | 15 min |
| `refresh_token` | UUID opaco | `/api/v1/auth` | 8h (ou 30d com `remember_me`) |
| `XSRF-TOKEN` | Token CSRF | `/` | Sessão |

**Claims JWT:** `sub` (colaboradorId), `sid` (sessionId), `email`, `name`, `iat`, `exp`, `iss`.

---

## Fluxo de login

```text
1. GET /api/v1/auth/login?remember_me=false
   → 302 redirect para Zimbra

2. Zimbra autentica o usuário

3. GET /api/v1/auth/callback?token=...&state=...
   → 302 redirect para frontend + Set-Cookie (access_token, refresh_token)

4. Requisições autenticadas enviam cookies automaticamente (withCredentials)
```

**Redirect pós-login:** `application.auth.frontend-redirect-url` (padrão `http://localhost:4200/`).

---

## CSRF

Quando `application.security.csrf-enabled=true` (padrão), requisições mutáveis autenticadas exigem:

```http
X-XSRF-TOKEN: {valor do cookie XSRF-TOKEN}
```

Aplica-se a: `POST`, `PUT`, `PATCH`, `DELETE`.

---

## Headers recomendados

| Header | Obrigatório | Descrição |
|--------|-------------|-----------|
| `Content-Type: application/json` | Sim (body JSON) | Payload de entrada |
| `X-XSRF-TOKEN` | Sim (mutações com cookie) | Proteção CSRF |
| `X-Correlation-Id` | Não | Rastreamento — gerado se ausente |

---

## Autorização para escrita

Operações de criação/atualização/status em recursos organizacionais exigem que o e-mail do JWT conste em:

```yaml
application.auth.session-administrator-emails
```

Leituras (`GET`) exigem apenas autenticação válida.

---

## Identidade do usuário autenticado

```http
GET /api/v1/auth/me
Cookie: access_token=...
```

Retorna `AuthenticatedUserResponse` — ver [authentication.md](./authentication.md).

---

## Health check público

```http
GET /api/v1/health
```

Não requer autenticação.

---

## OpenAPI / Swagger

| Recurso | URL |
|---------|-----|
| OpenAPI JSON | `/v3/api-docs` |
| Swagger UI | `/swagger-ui.html` |

Endpoints públicos — não exigem autenticação para consulta da documentação.

---

## Exemplo — requisição autenticada (curl)

```bash
# Após login via browser (cookies obtidos)
curl -s http://localhost:8080/api/v1/areas/1 \
  -H "Cookie: access_token=...; XSRF-TOKEN=..." \
  -H "X-Correlation-Id: $(uuidgen)"
```

---

## Próximos passos

1. [conventions.md](./conventions.md) — envelopes e paginação
2. [authentication.md](./authentication.md) — fluxo completo
3. [postman/Portal.postman_collection.json](./postman/Portal.postman_collection.json) — coleção pronta
