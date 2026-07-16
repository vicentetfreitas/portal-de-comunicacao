# Convenções da API

Padrões observados na implementação atual. Referência corporativa: `docs/implementation/07-api-standards.md`.

---

## Prefixo e versionamento

```text
/api/v1/{recurso}
```

Recursos em português, plural, sem hífen: `singulares`, `areas`, `equipes`, `colaboradores`.

---

## Envelope de sucesso — `ApiResponse<T>`

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "success": true,
  "message": "opcional",
  "data": { }
}
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `timestamp` | `Instant` (ISO-8601) | Momento da resposta |
| `success` | `boolean` | Sempre `true` em sucesso |
| `message` | `string` | Opcional (ex.: refresh token) |
| `data` | `T` | Payload — omitido quando `null` |

---

## Paginação — `PageResponse<T>`

Listagens retornam `ApiResponse<PageResponse<T>>`:

```json
{
  "timestamp": "2026-07-16T17:00:00Z",
  "success": true,
  "data": {
    "content": [],
    "page": 0,
    "size": 20,
    "totalElements": 42,
    "totalPages": 3,
    "first": true,
    "last": false
  }
}
```

### Parâmetros de query

| Parâmetro | Padrão | Máximo | Descrição |
|-----------|--------|--------|-----------|
| `page` | `0` | — | Página (base zero) |
| `size` | `20` | `100` | Itens por página |
| `sort` | `nome` (interno) | — | Ex.: `sort=name,asc` |

**Nota:** o sort padrão nos controllers usa campo interno `nome`; campos expostos na API são em inglês (`name`, `createdAt`, etc.) conforme cada recurso.

---

## Status lógico

Enum JSON: `ACTIVE` | `INACTIVE`

Mapeia `FLG_ATIVO` no banco (`S`/`N`).

---

## Códigos HTTP — sucesso

| Código | Uso na implementação |
|--------|----------------------|
| `200` | Consulta, atualização, listagem |
| `201` | Criação (`POST`) |
| `204` | Logout, revogação de sessão (sem body) |
| `302` | Login e callback (redirect) |

---

## Formato de datas

Campos `createdAt` e `updatedAt` são `Instant` serializados em ISO-8601 UTC.

---

## Content-Type

```http
Content-Type: application/json
```

Upload multipart não está implementado nos recursos atuais.

---

## Correlation ID

Header `X-Correlation-Id` propagado pelo `CorrelationIdFilter`. Se ausente, o backend gera UUID.

---

## Idempotência

Header `Idempotency-Key` documentado em padrões corporativos — **não implementado** nos endpoints atuais.
