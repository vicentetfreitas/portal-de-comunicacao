# Tratamento de Erros

Handler global: `GlobalExceptionHandler` · Entry point 401: `RestAuthenticationEntryPoint`.

---

## Formato — `ErrorResponse`

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "status": 404,
  "error": "RESOURCE_NOT_FOUND",
  "message": "Área não encontrada",
  "path": "/api/v1/areas/999"
}
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `timestamp` | `Instant` | Momento do erro |
| `status` | `int` | Código HTTP |
| `error` | `string` | Código identificador (`UPPER_SNAKE_CASE`) |
| `message` | `string` | Mensagem legível |
| `path` | `string` | URI da requisição |

---

## Validação — `ValidationErrorResponse`

Estende `ErrorResponse` com array `errors`:

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "path": "/api/v1/areas",
  "errors": [
    { "field": "name", "message": "must not be blank" }
  ]
}
```

---

## Mapeamento exceção → HTTP

| Exceção | HTTP | `error` |
|---------|------|---------|
| `ResourceNotFoundException` | 404 | `RESOURCE_NOT_FOUND` |
| `UnauthorizedException` / não autenticado | 401 | `UNAUTHORIZED` |
| `ForbiddenException` | 403 | `FORBIDDEN` |
| `ConflictException` | 409 | `CONFLICT` |
| `ValidationException` | 422 | `VALIDATION_ERROR` |
| `BusinessException` | 422 | `BUSINESS_RULE_VIOLATION` |
| `MethodArgumentNotValidException` | 400 | `VALIDATION_ERROR` |
| `ConstraintViolationException` | 400 | `VALIDATION_ERROR` |
| `IntegrationUnavailableException` | 503 | `INTEGRATION_UNAVAILABLE` |
| `IntegrationException` | 502 | `INTEGRATION_ERROR` |
| `PersistenceException` / `DataAccessException` | 500 | `PERSISTENCE_ERROR` |
| `Exception` genérica | 500 | `INTERNAL_SERVER_ERROR` |

---

## Regras de negócio (422)

Violações de domínio retornam HTTP **422** com `error: "BUSINESS_RULE_VIOLATION"` e mensagem específica. Exemplos por recurso:

### Singulares

- `Já existe singular com esta sigla`
- `Já existe singular com este código Unimed`
- `Singular possui áreas ativas vinculadas`

### Áreas

- `Singular inexistente` / `Singular inativa`
- `Já existe área ativa com este nome na singular`
- `Gestor inexistente` / `Gestor inativo`
- `Área possui equipes ativas vinculadas`

### Equipes

- `Área inexistente` / `Área inativa`
- `Já existe equipe ativa com este nome na área`
- `Líder inexistente` / `Líder inativo`
- `Equipe possui colaboradores ativos vinculados`

### Colaboradores

- `Já existe colaborador com este e-mail` / `Já existe colaborador com este CPF`
- `Colaborador não pode ser gestor de si mesmo`
- `Gestor inexistente` / `Gestor inativo`
- `Equipe inexistente` / `Equipe inativa` / `Equipe não pertence à área informada`
- `Área inexistente` / `Área inativa` / `Área não pertence à singular informada`
- `Singular inexistente` / `Singular inativa`
- `Colaborador possui subordinados ativos`

---

## Autenticação (401/403)

| Situação | HTTP | `error` |
|----------|------|---------|
| Cookie `access_token` ausente ou inválido | 401 | `UNAUTHORIZED` |
| Refresh token expirado/revogado | 401 | `UNAUTHORIZED` |
| E-mail não está em `session-administrator-emails` (escrita) | 403 | `FORBIDDEN` |
| CSRF inválido | 403 | `FORBIDDEN` |
| Colaborador inativo | 403 | `FORBIDDEN` |

---

## Integração Zimbra (503)

Login/callback retornam **503** com `INTEGRATION_UNAVAILABLE` quando o Zimbra está indisponível.
