# Golden Template — Full Stack Features

| Item | Valor |
|------|-------|
| Feature Code | **FT-SINGULAR** |
| Papel | **Golden Template** do Engineering Framework v4.0 / v4.1 |
| Status | FEATURE_APPROVED (backend + frontend) |
| Referência specs | `specs/features/singular/` |

---

# Por que FT-SINGULAR

FT-SINGULAR é a primeira Feature do projeto com **ciclo Full Stack completo**:

| Workstream | PKGs | Estado |
|------------|------|--------|
| backend | pkg-01 … pkg-06 | FEATURE_APPROVED |
| frontend | pkg-fe-01 … pkg-fe-06 | FEATURE_APPROVED |

Ela demonstra todos os padrões v4.0 sem exigir migração de Features anteriores.

---

# Mapa de Artefatos

```text
construction/registry.yaml
  └── features[FT-SINGULAR]
        ├── workstream: backend  (order: 1)
        └── workstream: frontend (order: 2, depends_on: backend)

construction/features/FT-SINGULAR/
├── feature-manifest.yaml          ← SSOT Feature (descobre specs + frontend)
├── construction-state.yaml        ← SSOT Workstream backend
├── execution-plan.md
├── session.md
├── pkg-01/ … pkg-06/status.md
├── review/
├── reports/
└── closure-report.md

construction/frontend/features/FT-SINGULAR/
├── feature-manifest.yaml          ← SSOD Workstream frontend
├── construction-state.yaml        ← SSOT Workstream frontend
├── execution-plan.md
├── frontend-tasks.md
├── session.md
├── pkg-fe-01/ … pkg-fe-06/status.md
├── review/
├── reports/
└── closure-report.md

specs/features/singular/           ← Fonte da verdade SDD (compartilhada)
```

---

# Padrões a Replicar

## 1. Feature Manifest (SSOT)

O manifest backend referencia o Workstream frontend:

```yaml
construction:
  path: .
  root: construction/features/FT-SINGULAR/
  frontend:
    path: ../../frontend/features/FT-SINGULAR/
    manifest: ../../frontend/features/FT-SINGULAR/feature-manifest.yaml
    state: ../../frontend/features/FT-SINGULAR/construction-state.yaml
```

## 2. Dois Construction States independentes

- Backend: `phase: closed`, `closure.final_state: FEATURE_APPROVED`
- Frontend: `phase: closed`, `closure.final_state: FEATURE_APPROVED`
- Estado Feature: **computado** — ambos closed → FEATURE_APPROVED

## 3. Sequência de PKGs

| Workstream | Sequência |
|------------|-----------|
| backend | Scaffold → Create → Read/List → Update → Status → Acceptance |
| frontend | Scaffold → Create → List/Detail → Edit → Status UI → Hub/Tests/Closure |

## 4. Dependências e handoff

Frontend só inicia após:

- Frontend Foundation FEATURE_APPROVED
- FT-AUTH frontend FEATURE_APPROVED
- FT-SINGULAR backend FEATURE_APPROVED
- API contract APPROVED

Documentado em `construction/frontend/features/FT-SINGULAR/construction-state.yaml` (DoR).

## 5. Testes por camada

| Camada | Validação |
|--------|-----------|
| backend | `mvn clean verify` no encerramento |
| frontend | Vitest + Playwright + build no encerramento |

## 6. Comandos de execução (v4.1)

```text
Execute FT-SINGULAR              → Orchestrator resolve Workstream/PKG ativo
Continue FT-SINGULAR             → Retomar execução (equivalente quando em andamento)
Review FT-SINGULAR               → Review do Workstream ativo
Close FT-SINGULAR                → Encerramento após todos Workstreams DONE
Execute PKG-03 FT-SINGULAR       → diagnóstico — backend explicit
Execute PKG-FE-03 FT-SINGULAR    → diagnóstico — frontend explicit
```

---

# Compatibilidade v4.1

FT-SINGULAR valida a retrocompatibilidade do refinamento v4.1 **sem migração**:

| Aspecto v4.1 | FT-SINGULAR |
|--------------|-------------|
| `capabilities[]` | **Ausente** — comportamento v4.0 preservado |
| `execution_strategy` | **Ausente** — padrão `sequential` (EXEC-STRAT-01) |
| `current_capability` | `null` no registry |
| Manifest | v2 — inalterado |
| Lifecycle | Session → PKGs → Closure por Workstream |
| CMD-FEATURE-01 | `Execute FT-SINGULAR` resolve backend/frontend internamente |

Features novas **podem** adotar capabilities; FT-SINGULAR permanece referência do padrão Full Stack sem agrupamento funcional.

---

# Checklist para Nova Feature Full Stack

1. Registrar em `construction/registry.yaml` com workstreams `backend` + `frontend`
2. Criar `construction/features/FT-<DOMAIN>/` a partir de templates
3. Após backend FEATURE_APPROVED, criar `construction/frontend/features/FT-<DOMAIN>/`
4. Linkar frontend no manifest raiz (`construction.frontend`)
5. Declarar dependências em `manifest.dependencies`
6. Executar via `Execute FT-<DOMAIN>` — Orchestrator roteia Workstreams por `order`
7. *(Opcional v4.1)* Declarar `capabilities[]` apenas se a Feature exigir agrupamento funcional

---

# Referências

- `construction/12-fullstack-orchestrator.md`
- `construction/14-framework-decisions-v4.1.md`
- `construction/features/FT-SINGULAR/feature-manifest.yaml`
- `construction/frontend/features/FT-SINGULAR/feature-manifest.yaml`
- `specs/features/singular/traceability.md`
