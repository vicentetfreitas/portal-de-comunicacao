# Frontend Construction — Workstream Frontend (v4.1)

| Item | Valor |
|------|-------|
| Camada | Construction |
| Escopo | Workstream **frontend** + Frontend Foundation (Sprint 0) |
| Versão | 4.0 |
| Registry | `construction/registry.yaml` |
| Última atualização | 2026-07-16 |

---

# Objetivo

Centralizar o **Workstream frontend** das Features de negócio. Na v4.1, backend e frontend são Workstreams da mesma Feature (`FT-<DOMAIN>`), orquestrados via `construction/12-fullstack-orchestrator.md`.

---

# Relação com Backend Construction

```text
construction/features/FT-SINGULAR/            ← Workstream backend (closed)
construction/frontend/features/FT-SINGULAR/   ← Workstream frontend (closed)
construction/registry.yaml                      ← Índice unificado v4.1
```

O backend e o frontend compartilham:

- **Code** oficial (`FT-SINGULAR`)
- **Specification** em `specs/features/singular/`
- **API contract** em `specs/features/singular/api.md`

O frontend possui PKGs, tasks e critérios de aceite **próprios** (camada construction), derivados da spec backend e do mapeamento legado.

---

# Estrutura padrão por Feature frontend

```text
construction/frontend/features/<FEATURE_CODE>/
├── feature-manifest.yaml      ← SSOD frontend (consultar primeiro)
├── construction-state.yaml    ← SSOT estado operacional
├── execution-plan.md          ← Ponto de entrada da execução
├── frontend-tasks.md          ← Backlog técnico frontend
├── session.md                 ← Snapshot (Execute Feature — frontend)
├── pkg-fe-01/ … pkg-fe-N/
│   ├── status.md              ← VALIDATION SUMMARY (VAL-01) + entregas
│   └── evidence/              ← build-verify-*.log (opcional, ART-01)
├── review/
└── reports/
```

Índice global: `construction/frontend/registry.yaml`

---

# Dependências transversais

| Dependência | Caminho | Status |
|-------------|---------|--------|
| Frontend Foundation | `construction/frontend/construction-state.yaml` | ✅ `FEATURE_APPROVED` (PKG-FE-S0-01..10) |
| FT-AUTH (frontend) | `construction/features/FT-AUTH/construction-state.yaml` | ✅ `FEATURE_APPROVED` |
| Backend da Feature | `construction/features/<CODE>/` | API disponível e validada |

---

# Comandos

```text
Execute Feature FT-SINGULAR (frontend)  → State → Manifest → Snapshot → Session
Execute PKG-FE-XX FT-SINGULAR           → PKG focado (requer phase: execution)
Encerrar Feature FT-SINGULAR (frontend) → Closure, review, audit, build frontend
```

Referência de workflow: `construction/11-feature-execution-workflow.md` v3.2 (adaptado à camada frontend).

---

# Features registradas

Consultar `registry.yaml`.
