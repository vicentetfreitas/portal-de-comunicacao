# API Contract — FT-PRIMEIRO-ACESSO

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Status | APPROVED (reconciliado 2026-08-17) |
| Versão | 1.1 |
| Prefixo | `/api/v1` |
| Padrões | `docs/implementation/07-api-standards.md` |

> **Contrato normativo TO-BE** — endpoints de onboarding e Home ainda **não implementados** no backend homologado. Detalhe técnico de rotas de onboarding **pendente de implementação** (não inventar nesta etapa).

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
| PA-API-006 | — | *onboarding* | Completar PA / criar COLABORADOR | **Pendente implementação** — sem contrato fixado |

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

# PA-API-006 — Onboarding (pendente)

### Status

**Pendência de implementação.** O TO-BE exige endpoints para conduzir o wizard e criar `COLABORADOR` ao final (DH-03, DH-PA-02). Forma física (rotas, payloads, credencial temporária) **não** está decidida nesta etapa — delegada à engenharia na implementação.

### Requisitos normativos (sem inventar contrato)

- Resolver domínio → Singular (backend — DEC-ORG-003, DH-PA-02)
- Permitir seleção de Área (+ Equipe opcional) dentro da Singular
- Criar COLABORADOR somente com vínculo completo (DH-03, DH-04)
- Não exigir CARGO (DH-CARGO-01)
- Não emitir `AUTH_SESSAO` operacional antes da conclusão (DH-PA-01)

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
├─ ausente → Onboarding API (PA-API-006 — pendente)
│              → criar COLABORADOR
└─ presente → derivar Contexto Ativo de organizationalLinks
    ↓
GET /session/home (PA-API-004)
    ↓
Render Home + Operational
```
