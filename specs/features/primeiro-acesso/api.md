# API Contract — FT-PRIMEIRO-ACESSO

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Status | APPROVED (reconciliado 2026-08-17) |
| Versão | 1.1 |
| Prefixo | `/api/v1` |
| Padrões | `docs/implementation/07-api-standards.md` |

> **Contrato normativo TO-BE** — onboarding (PA-API-006 / PA-API-007) definido abaixo. `GET /session/home` (PA-API-004) permanece proposto.

Autenticação: cookie `access_token` (FT-AUTH) ou credencial temporária PA (DH-PA-01 — mecanismo delegado à engenharia). CSRF nas mutações conforme padrão corporativo.

---

# Visão geral dos contratos

| ID | Método | Endpoint | Objetivo | Status |
|----|--------|----------|----------|--------|
| PA-API-001 | GET | `/api/v1/session/contexts` | ~~Listar N vínculos~~ | **SUPERSEDED** (DH-02) |
| PA-API-002 | GET | `/api/v1/session/context` | ~~Consultar Contexto Ativo persistido separadamente~~ | **SUPERSEDED** — derivar de `/auth/me` |
| PA-API-003 | PUT | `/api/v1/session/context` | ~~Selecionar/persistir Contexto Ativo~~ | **SUPERSEDED** — vínculo em `COLABORADOR` |
| PA-API-004 | GET | `/api/v1/session/home` | Obter Home dinâmica | **Vigente** (proposto) |
| PA-API-005 | GET | `/api/v1/auth/me` | Identidade + vínculo único | **Existente** |
| PA-API-006 | GET | `/api/v1/auth/primeiro-acesso/areas` | Listar áreas ativas da Singular resolvida | **Vigente** |
| PA-API-007 | POST | `/api/v1/auth/primeiro-acesso` | Concluir PA: criar COLABORADOR e promover sessão | **Vigente** |

---

# PA-API-004 — Home dinâmica (vigente)

### Endpoint

`GET /api/v1/session/home`

### Objetivo

Obter a Home determinada pelo backend para o Contexto Ativo derivado do vínculo do COLABORADOR (BR-042, DEC-FA-004).

### Pré-condição

COLABORADOR autenticado com vínculo completo; Contexto Ativo derivado das FKs.

### Response 200

```json
{
  "success": true,
  "data": {
    "home": {
      "type": "route",
      "path": "/app/home",
      "title": "Painel",
      "params": {}
    }
  }
}
```

### Erros

| HTTP | Situação |
|------|----------|
| 401 | Não autenticado |
| 409 | COLABORADOR ausente ou vínculo incompleto |
| 422 | Vínculo inválido |
| 500 / 503 | Falha ao resolver Home |

### UC

UC-PA-006, 008

---

# PA-API-005 — GET /auth/me (vigente)

### Endpoint existente

`GET /api/v1/auth/me` (FT-AUTH / FT-SESSION)

### Contrato TO-BE

Expõe identidade e **único** vínculo organizacional via `organizationalLinks`:

```json
{
  "federationId": 1,
  "singularId": 10,
  "areaId": 20,
  "teamId": null
}
```

- **Não** expor `organizationalContexts[]` (modelo N vínculos — superseded).
- `activeContext` na store frontend = projeção derivada de `organizationalLinks` (DH-02).

### UC

UC-PA-001, 002, 008

---

# PA-API-006 — Listar áreas do Primeiro Acesso

### Endpoint

`GET /api/v1/auth/primeiro-acesso/areas`

### Objetivo

Listar áreas **ativas** da Singular determinada pelo domínio do e-mail autenticado (BR-043, DEC-ORG-003, DH-PA-02). O cliente **não** envia `singularId`.

### Autorização

JWT `typ=pa` (`PRIMEIRO_ACESSO`). Sessão operacional recebe 403. Sem cookie: 401.

### Response 200

```json
{
  "success": true,
  "data": [
    { "id": 20, "name": "Tecnologia da Informação", "acronym": "TI" }
  ]
}
```

### Erros

| HTTP | Situação |
|------|----------|
| 401 | Não autenticado |
| 403 | JWT operacional ou sem `PRIMEIRO_ACESSO` |
| 422 | Domínio sem Singular (`PA_DOMAIN_NO_SINGULAR`) |

---

# PA-API-007 — Concluir Primeiro Acesso

### Endpoint

`POST /api/v1/auth/primeiro-acesso`

### Objetivo

Criar `COLABORADOR` com vínculo completo (Federação + Singular resolvida + Área) e promover a credencial PA a sessão operacional (DH-03, DH-PA-01). Identidade sai do JWT (`email`, `zid`); o payload **não** aceita `codColaborador` nem `singularId`.

### Autorização

JWT `typ=pa`. CSRF nas mutações. Sessão operacional: 403.

### Request

```json
{
  "areaId": 20,
  "teamId": null
}
```

`areaId` obrigatório. `teamId` opcional (Equipe dentro da Área — DH-04).

### Response 200

`Set-Cookie` substitui `access_token` (JWT operacional) e define `refresh_token`. Corpo: identidade operacional (`primeiroAcesso: false`), mesmo contrato de `GET /auth/me`.

### Erros

| HTTP | Situação |
|------|----------|
| 400 | `areaId` ausente |
| 401 | Não autenticado |
| 403 | JWT operacional |
| 409 | COLABORADOR já existe para a identidade |
| 422 | Área inexistente, inativa, de outra Singular, ou domínio sem Singular |

### Efeitos

- Persiste `COLABORADOR` (`COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE` opcional)
- Cria `AUTH_SESSAO` operacional
- Substitui cookies; `/auth/me` passa a `primeiroAcesso=false`

---

# Contratos superseded (histórico — pré-DH-02)

## PA-API-001 — Listar contextos (N vínculos)

**SUPERSEDED** por DH-02. Com 1 vínculo, usar `GET /auth/me` → `organizationalLinks`.

*Texto histórico preservado em revisões anteriores do repositório.*

## PA-API-002 — Consultar Contexto Ativo separado

**SUPERSEDED.** Contexto Ativo derivado das FKs de `COLABORADOR`; sem store separado.

## PA-API-003 — Selecionar / persistir Contexto Ativo

**SUPERSEDED.** Vínculo persistido no `COLABORADOR` durante onboarding (DH-03); sem PUT de contexto.

---

# Relação Frontend ↔ Backend (TO-BE)

```text
FT-AUTH login (ou credencial temporária PA)
    ↓
Verificar COLABORADOR (/auth/me ou equivalente)
    ↓
├─ ausente → GET /auth/primeiro-acesso/areas (PA-API-006)
│              → POST /auth/primeiro-acesso (PA-API-007)
│              → criar COLABORADOR + cookies operacionais
└─ presente → derivar Contexto Ativo de organizationalLinks
    ↓
GET /session/home (PA-API-004)
    ↓
Render Home + Operational
```
