# Feature Session — Equipe (Frontend)

| Item | Valor |
|------|-------|
| Feature Code | FT-EQUIPE |
| Feature Slug | equipe |
| Camada | Frontend |
| Sprint | 3 |
| Session | **v1** |
| Data da sessão | 2026-07-17 |
| Agente | construction-orchestrator |
| SSOD | `construction/frontend/features/FT-EQUIPE/feature-manifest.yaml` |
| Estado operacional | `construction/frontend/features/FT-EQUIPE/construction-state.yaml` |
| Imutabilidade | **READ ONLY** após criação (SESSION-01) |

---

# Regra SESSION-01 / STATE-04

Esta Session é **imutável** durante toda a execução do workstream frontend.

Progresso operacional: `construction-state.yaml` (SSOT).

---

# Snapshot de Contexto

## Feature

| Campo | Valor |
|-------|-------|
| Code | FT-EQUIPE |
| Slug | equipe |
| Tipo | business_feature (CRUD — Frontend MVP) |
| Objetivo | UI administrativa CRUD de Equipes via `/api/v1/equipes` |

## Objetivos

- Hub administrativo de equipes
- Cadastrar equipe (área, nome, descrição, líder opcional)
- Listar com paginação e filtros (`status`, `areaId`, `name`)
- Detalhe, edição (`areaId` read-only), ativar/inativar
- E2E AT-FE-EQUIPE-001..005 com mocks (padrão FT-SINGULAR)

## Premissas

- Frontend Foundation e FT-AUTH **FEATURE_APPROVED**
- FT-EQUIPE backend **FEATURE_APPROVED** — API estável
- FT-AREA backend **FEATURE_APPROVED** — listagem de áreas
- Especificação `specification-frontend.md` v1.0 **APPROVED**
- Referência de implementação: `construction/frontend/features/FT-SINGULAR/`

## Restrições

- RN no backend apenas; validações de formato no cliente
- Sem membros/permissões/documentos por equipe nesta entrega
- Service layer obrigatória (DS-EQUIPE-FE-02)
- Não alterar backend nem contratos em `api.md`

## Contratos (consumo UI)

| Endpoint | Método | Uso |
|----------|--------|-----|
| `/api/v1/equipes` | POST | Cadastro |
| `/api/v1/equipes/{id}` | GET | Detalhe |
| `/api/v1/equipes` | GET | Listagem |
| `/api/v1/equipes/{id}` | PUT | Edição |
| `/api/v1/equipes/{id}/status` | PATCH | Status |
| `/api/v1/areas` | GET | Select/filtro área |

**DTOs:** `CreateEquipeRequest`, `UpdateEquipeRequest`, `UpdateEquipeStatusRequest`, `EquipeResponse`.

## Rotas TO-BE

```text
/app/administrador/equipes
/app/administrador/equipes/lista
/app/administrador/equipes/novo
/app/administrador/equipes/:id
/app/administrador/equipes/:id/editar
```

## PKGs

| PKG | Nome | Tasks |
|-----|------|-------|
| PKG-FE-01 | Equipe Module Scaffold & API Client | Base |
| PKG-FE-02 | Create Equipe Page | TASK-EQUIPE-FE-001 |
| PKG-FE-03 | List & Detail Pages | TASK-EQUIPE-FE-002, FE-003 |
| PKG-FE-04 | Edit Equipe Page | TASK-EQUIPE-FE-004 |
| PKG-FE-05 | Status Change UI | TASK-EQUIPE-FE-005 |
| PKG-FE-06 | Admin Hub, Tests & Closure | AT-FE-EQUIPE-001..005 |

## Artefatos de spec

| Artefato | Path |
|----------|------|
| Frontend spec | `specs/features/equipe/specification-frontend.md` |
| Tasks | `specs/features/equipe/tasks.md` (TK-EQUIPE-FE-*) |
| AT-FE | `specs/features/equipe/acceptance-tests.md` |
| Construction tasks | `frontend-tasks.md` |

## Pendências

- Código `organization/equipe` ainda não existe em `frontend/src/` (PKG-FE-01..06)

---

# Definition of Ready

**Satisfeito** — Readiness 2026-07-17.

---

# Próximo passo

Executar **PKG-FE-01**.
