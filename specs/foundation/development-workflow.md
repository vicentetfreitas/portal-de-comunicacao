# Development Workflow (Simplified)

| Campo | Valor |
|--------|--------|
| Artefato | development-workflow.md |
| Camada | Foundation |
| Versão | 1.1 |
| Status | Approved — Etapa 2/3 2026-08-13 |
| Categoria documental | SSOT |

## Objetivo

Fluxo diário de entrega no modelo simplificado (Etapa 2). Substitui a cerimônia PKG/Session/Registry para trabalho rotineiro.

O Engineering Framework v4.1 (`construction/11-feature-execution-workflow.md`, `12-fullstack-orchestrator.md`) permanece como **Archive** para Features históricas.

---

## Fluxo principal

```text
DRAFT → READY_FOR_REVIEW → APPROVED → IMPLEMENTING → DONE
```

Retrabalho de spec: `READY_FOR_REVIEW` → `DRAFT`.

```text
specs/features/<slug>/ → (DoR-Implementation) → tasks.md → código → Validate → PR review (Gate 3) → DoD (Gate 6)
```

SSOT de estado: `feature.yaml`. `tasks.md` = plano. Git/CI/testes = evidência.

### Etapas

1. **Specify** — `DRAFT`; artefatos em `specs/features/<slug>/`
2. **DoR-Spec + Gate 1** — transição para `READY_FOR_REVIEW` ([`definition-of-ready.md`](definition-of-ready.md), [`feature-quality-gates.md`](feature-quality-gates.md))
3. **Review de Spec** — `APPROVED` ou retorno a `DRAFT` ([`review-process.md`](review-process.md))
4. **DoR-Implementation** — Readiness de implementação; então `IMPLEMENTING`
5. **Executar tasks** — [`specs/features/<slug>/tasks.md`](../features/) é o plano principal, não o estado
6. **Implementar** — paths em [`path-conventions.md`](path-conventions.md)
7. **Validar** — evidência (CI + testes locais); **não** altera `feature.yaml`
8. **Review de PR** — Gate 3; evidência em logs de CI / parecer
9. **DoD + Gate 6** — [`definition-of-done.md`](definition-of-done.md) → `DONE`

---

## Substituições (legado → simplificado)

| Legado | Substituto |
|--------|------------|
| `feature-manifest` paths | `path-conventions.md` |
| `execution-plan.md` | `tasks.md` |
| `session.md` | specs direto |
| `pkg-XX/status.md` | CI logs / PR |
| `registry.status` | `feature.yaml` (estado); git/CI (evidência) |
| Orchestrator `Execute PKG-XX` | tasks TK-* sequenciais |
| `frontend-tasks.md` | `specs/features/<slug>/tasks.md` |

---

## Agentes Cursor ativos

| Agente | Responsabilidade |
|--------|------------------|
| `specification-engineer` | Artefatos em `specs/` |
| `backend-engineer` | Java/Spring em `backend/` |
| `reviewer` | Review técnico (sem editar) |

Regras on-demand: `.cursor/rules/reference/backend-implementation.mdc`, `frontend-implementation.mdc`.

---

## Validação

| Camada | Comando |
|--------|---------|
| Backend | `cd backend && mvn clean verify` |
| Frontend | lint, typecheck, unit; E2E no closure |
| CI | `.github/workflows/` |

---

## Construction em transição

Para Features com workstreams históricos, `construction-state.yaml` pode existir como espelho operacional reconcilável. **Não** é SSOT de estado da Feature.

Não é obrigatório criar novos PKGs ou sessions.

Opcional no closure: `closure-report.md` em `construction/features/<CODE>/` (evidência, não estado).

---

## Proibido

- Implementar sem `APPROVED` e sem DoR-Implementation
- Tratar Git, CI, `tasks.md` ou `construction-state.yaml` como SSOT de estado
- Tratar `pkg-XX/status.md` como SSOT de progresso global
- Tratar `construction/registry.yaml` status como SSOT
- Explorar repositório sem `path-conventions.md`

---

## Referências

- [`minimal-ssot.md`](minimal-ssot.md) — mapa de precedência
- [`workflow.md`](workflow.md) — ciclo SDD geral
- [`docs/governance/09-framework-simplification-scope.md`](../../docs/governance/09-framework-simplification-scope.md) — escopo Etapa 2
- [`construction/README.md`](../../construction/README.md) — framework legado v4.1
