# CHANGELOG — Engineering Framework (Integration Layer)

| Item | Valor |
|------|-------|
| Nome | **Engineering Framework — Integration Layer** |
| Versão atual | **v1.0.0** |
| Status | **Stable** |
| Camada | `engineering/` |
| Última atualização | 2026-07-14 |

---

## [1.0.0] — 2026-07-14 — Stable

### Marco

- Sprint de Integração formalizada como etapa oficial do ciclo de engenharia.
- Processo manual consolidado — **sem orquestrador, sem automação, sem agentes**.
- Base documental preparada para futuro Engineering Orchestrator.

### Adicionado

- **Integration Identity** — identificador `sprint-<NN>-<scope>` para instâncias de sprint.
- **SSOD** — `integration-manifest.yaml` como Single Source of Discovery (SSOD-INT-01).
- **Integration State** — `integration-state.yaml` como SSOT operacional (STATE-INT-01).
- **State machine** — `PLANNED → IN_PROGRESS → VALIDATION → REVIEW → READY → APPROVED → COMPLETED`.
- **7 fases de validação** — ENV, INF, API, DB, XFT, FUN, OUT.
- **Templates reutilizáveis** — manifest, state, plan, checklist, report, readiness, issues.
- **Workflow SSOT** — `engineering/integration/01-integration-sprint-workflow.md`.
- **Primeira instância** — `sprint-03-org-backend` (Organização Corporativa).

### Referências

- `engineering/integration/01-integration-sprint-workflow.md`
- `engineering/integration/04-state-machine.md`
- `engineering/integration/05-entry-exit-criteria.md`
