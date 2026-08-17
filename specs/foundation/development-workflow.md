# Development Workflow (Simplified)

| Campo | Valor |
|--------|--------|
| Artefato | development-workflow.md |
| Camada | Foundation |
| Versão | 1.0 |
| Status | Approved — Etapa 2/3 2026-08-13 |
| Categoria documental | SSOT |

## Objetivo

Fluxo diário de entrega no modelo simplificado (Etapa 2). Substitui a cerimônia PKG/Session/Registry para trabalho rotineiro.

O Engineering Framework v4.1 (`construction/11-feature-execution-workflow.md`, `12-fullstack-orchestrator.md`) permanece como **Archive** para Features históricas.

---

## Fluxo principal

```text
specs/features/<slug>/ → tasks.md → código → CI → PR review
```

### Etapas

1. **Consultar spec** — `specs/features/<slug>/` + `feature.yaml`
2. **Verificar DoR** — [`definition-of-ready.md`](definition-of-ready.md)
3. **Executar tasks** — [`tasks.md`](tasks.md) é o plano principal
4. **Implementar** — paths em [`path-conventions.md`](path-conventions.md)
5. **Validar** — CI + testes locais
6. **Review** — PR; evidência em logs de CI
7. **Verificar DoD** — [`definition-of-done.md`](definition-of-done.md)

---

## Substituições (legado → simplificado)

| Legado | Substituto |
|--------|------------|
| `feature-manifest` paths | `path-conventions.md` |
| `execution-plan.md` | `tasks.md` |
| `session.md` | specs direto |
| `pkg-XX/status.md` | CI logs / PR |
| `registry.status` | `construction-state.yaml` ou estado derivado (git/CI) |
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

Para Features com workstreams históricos, `construction-state.yaml` pode existir como espelho operacional reconcilável. Não é obrigatório criar novos PKGs ou sessions.

Opcional no closure: `closure-report.md` em `construction/features/<CODE>/`.

---

## Proibido

- Implementar sem spec que atenda DoR
- Tratar `pkg-XX/status.md` como SSOT de progresso global
- Tratar `construction/registry.yaml` status como SSOT
- Explorar repositório sem `path-conventions.md`

---

## Referências

- [`minimal-ssot.md`](minimal-ssot.md) — mapa de precedência
- [`workflow.md`](workflow.md) — ciclo SDD geral
- [`docs/governance/09-framework-simplification-scope.md`](../../docs/governance/09-framework-simplification-scope.md) — escopo Etapa 2
- [`construction/README.md`](../../construction/README.md) — framework legado v4.1
