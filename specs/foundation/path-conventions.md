# Path Conventions

| Campo | Valor |
|--------|--------|
| Artefato | path-conventions.md |
| Camada | Foundation |
| Versão | 1.0 |
| Status | Approved — Etapa 2/3 2026-08-13 |
| Categoria documental | SSOT |

## Objetivo

SSOT para descoberta de paths no fluxo simplificado. Substitui `feature-manifest.yaml` como fonte de localização de artefatos.

Consultar este documento antes de inferir paths no repositório.

---

## Specs (SSOT funcional)

```text
specs/features/<slug>/
├── feature.yaml
├── specification.md
├── use-cases.md
├── api.md
├── acceptance-tests.md
├── tasks.md          ← plano principal de implementação
├── traceability.md
├── flows.md          (quando aplicável)
├── state-machine.md  (quando aplicável)
└── decisions.md      (quando aplicável)
```

| Campo | Convenção |
|-------|-----------|
| `<slug>` | kebab-case do domínio (`singular`, `primeiro-acesso`, `authentication`) |
| Feature code | `FT-<DOMAIN>` em `feature.yaml` (`FT-SINGULAR`, `FT-PRIMEIRO-ACESSO`) |
| Mapeamento slug ↔ code | Ver `construction/registry.yaml` campo `slug` |

---

## Backend

```text
backend/src/main/java/br/com/unimedceara/portalcomunicacao/<module>/
backend/src/test/java/br/com/unimedceara/portalcomunicacao/<module>/
```

| Domínio | Módulo típico | Exemplo |
|---------|---------------|---------|
| Organização | `organization/` | FT-SINGULAR, FT-EQUIPE, FT-COLABORADOR |
| Autenticação | `accesscontrol/` | FT-AUTH, FT-SESSION |
| Primeiro acesso | `accesscontrol/` ou módulo dedicado conforme spec | FT-PRIMEIRO-ACESSO |
| Área | `organization/` | FT-AREA |

Padrões de implementação: `docs/implementation/`.

Validação: `cd backend && mvn clean verify`

---

## Frontend

```text
frontend/src/
├── pages/<domain>/
├── components/<domain>/
├── services/<domain>/
├── composables/
├── stores/
├── router/guards/
├── router/routes/
└── types/<domain>/
```

| Domínio | Exemplo de paths | Feature |
|---------|------------------|---------|
| organization | `pages/organization/singular/` | FT-SINGULAR |
| auth | `stores/auth-store.ts`, `router/guards/` | FT-AUTH, FT-SESSION |
| session | `stores/session.store.ts` | FT-SESSION, FT-PRIMEIRO-ACESSO |

Validação: lint, typecheck, unit tests; E2E no closure da feature.

---

## Database

```text
database/
├── baseline/          ← prioridade 1 (schema truth)
├── ddl/
├── migrations/      ← Flyway V*.sql
├── model/           ← complementar
└── GOVERNANCE.md    ← precedência
```

---

## Construction (legado — transição)

Existem para Features históricas e transição. **Não usar** para descobrir paths de implementação.

```text
construction/features/<FEATURE_CODE>/     ← backend workstream
construction/frontend/features/<FEATURE_CODE>/  ← frontend workstream
```

Índice unificado: `construction/registry.yaml` (REGISTRY-01).

Estado operacional por workstream: `construction-state.yaml` — não é SSOT de paths.

---

## Golden references

| Referência | Uso |
|------------|-----|
| `specs/features/authentication/` | Spec completa de referência |
| `construction/golden-template/FT-SINGULAR.md` | Full Stack histórico |
| `specs/features/singular/` | CRUD completo |

---

## Proibido

- Paths hardcoded sem consultar este documento ou a spec da feature
- Inferir localização via `feature-manifest.yaml` em novas features
- Explorar o repositório arbitrariamente sem contexto de feature
