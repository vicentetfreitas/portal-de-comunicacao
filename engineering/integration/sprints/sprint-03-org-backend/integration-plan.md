# Integration Plan — sprint-03-org-backend

| Item | Valor |
|------|-------|
| Sprint ID | sprint-03-org-backend |
| Nome | Organização Corporativa — Backend |
| Construction Sprint | 3 |
| Status | PLANNED |
| Versão | 1.0 |
| Última atualização | 2026-07-14 |

---

# Objetivo

Validar a integração do backend de Organização Corporativa após o encerramento das Features de autenticação e cadastros hierárquicos (Singular → Área → Equipe → Colaborador), garantindo conformidade de ambiente, infraestrutura, APIs, persistência, fluxos cross-feature e regras de negócio.

---

# Escopo

## Features incluídas

| Feature Code | Nome | Sprint | Status Construction |
|--------------|------|--------|---------------------|
| FT-AUTH | Authentication | 1 | FEATURE_APPROVED |
| FT-SINGULAR | Singular | 3 | FEATURE_APPROVED |
| FT-AREA | Área | 2 | FEATURE_APPROVED |
| FT-EQUIPE | Equipe | 3 | FEATURE_APPROVED |
| FT-COLABORADOR | Colaborador | 3 | FEATURE_APPROVED |

## Fora de escopo

- Frontend e consumo de API pelo cliente web
- Gestão Documental e demais domínios não listados
- Alteração de código, specs ou APIs nesta fase documental
- Automação ou orquestração do processo

---

# Pré-requisitos

| ID | Pré-requisito | Status |
|----|---------------|--------|
| ENT-01 | Features encerradas (5/5 FEATURE_APPROVED) | ✅ |
| ENT-02 | Platform Foundation aprovada | ✅ |
| ENT-03 | Build verde (`mvn clean verify`) | ✅ (2026-07-14) |
| ENT-04 | Manifesto instanciado | ✅ |
| ENT-05 | Plano definido | ✅ |
| ENT-06 | Checklist instanciado (40 itens Must) | ✅ |
| ENT-07 | Specs disponíveis | ✅ |
| ENT-08 | Ambiente de teste (profile `test`) | ✅ |

---

# APIs no Escopo

| Feature | Base Path | Operações |
|---------|-----------|-----------|
| FT-AUTH | `/api/v1/auth` | login, callback, me, refresh, logout |
| FT-SINGULAR | `/api/v1/singulares` | CRUD + status |
| FT-AREA | `/api/v1/areas` | CRUD + status |
| FT-EQUIPE | `/api/v1/equipes` | CRUD + status |
| FT-COLABORADOR | `/api/v1/colaboradores` | CRUD + status |

---

# Fluxos Cross-Feature (XFT)

## xft-org-01 — Hierarquia Organizacional Completa

```text
Login / Sessão (FT-AUTH)
    ↓
Singular (FT-SINGULAR)
    ↓
Área vinculada à Singular (FT-AREA)
    ↓
Equipe vinculada à Área (FT-EQUIPE)
    ↓
Colaborador vinculado à hierarquia (FT-COLABORADOR)
```

| Passo | Feature | Ação | Endpoint |
|-------|---------|------|----------|
| 1 | FT-AUTH | Obter contexto autenticado | `GET /api/v1/auth/me` |
| 2 | FT-SINGULAR | Criar Singular | `POST /api/v1/singulares` |
| 3 | FT-AREA | Criar Área com `singularId` | `POST /api/v1/areas` |
| 4 | FT-EQUIPE | Criar Equipe com `areaId` | `POST /api/v1/equipes` |
| 5 | FT-COLABORADOR | Criar Colaborador com vínculos | `POST /api/v1/colaboradores` |
| 6 | FT-COLABORADOR | Listar por `equipeId` / `areaId` | `GET /api/v1/colaboradores` |
| 7 | FT-EQUIPE | Tentar inativar com colaboradores ativos | `PATCH /api/v1/equipes/{id}/status` |

## xft-org-02 — Validação de Dependências

| Passo | Ação | Resultado esperado |
|-------|------|-------------------|
| 1 | Inativar Área com Equipes ativas | 409 ou regra RN conforme spec |
| 2 | Inativar Equipe com Colaboradores ativos | Bloqueio (RN-EQUIPE-006) |
| 3 | Criar Colaborador com `areaId` inválido | 422 Unprocessable Entity |

---

# Fases de Validação

| Fase | Itens | Foco |
|------|-------|------|
| ENV | 7 | Build, startup, datasource, actuator, profiles |
| INF | 7 | JWT, cookies, CORS, security, exception handler |
| API | 8 | CRUD por recurso, paginação, filtros, HTTP |
| DB | 6 | FK, constraints, auditoria, soft delete |
| XFT | 5 | Fluxos xft-org-01 e xft-org-02 |
| FUN | 5 | Acceptance tests, RN-*, casos negativos |
| OUT | 4 | Issues, riscos, métricas, readiness |

---

# Riscos Conhecidos

| ID | Risco | Mitigação |
|----|-------|-----------|
| RSK-01 | OQ-020 — autorização incremental (admin por e-mail) | Documentar como ressalva; validar comportamento atual |
| RSK-02 | Warnings H2 schema teardown em testes | Não bloqueante; registrar em issues se persistir |
| RSK-03 | Specs parciais (singular/equipe/colaborador) | Validar contra código + acceptance tests + closure reports |

---

# Referências

- Manifest: `integration-manifest.yaml`
- Checklist: `integration-checklist.md`
- Registry Features: `construction/features/registry.yaml`
- Progress: `construction/09-progress.md`
