# Minimal SSOT Map

| Campo | Valor |
|--------|--------|
| Artefato | minimal-ssot.md |
| Camada | Foundation |
| Versão | 1.0 |
| Status | Approved — Etapa 2/3 2026-08-13 |
| Categoria documental | SSOT |

## Objetivo

Mapa operacional de fontes autoritativas para o fluxo simplificado de desenvolvimento. Complementa [`docs/governance/07-documentation-architecture.md`](../../docs/governance/07-documentation-architecture.md) com precedência explícita para implementação diária.

**Este documento prevalece** sobre `construction/registry.yaml` status manual, `feature-manifest.status` e `pkg-XX/status.md` em conflito de estado.

---

## Hierarquia de precedência

```text
SSOT (definição)
  ↓
artefatos derivados
  ↓
artefatos operacionais
  ↓
artefatos históricos / legados
```

### Conflitos normativos

| Tema | Precedência |
|------|-------------|
| Comportamento a implementar | `specs/` > `docs/` > código |
| Regras de negócio transversais | `docs/domain/09-business-rules.md` — features referenciam, não reescrevem |
| Contrato API | `specs/features/*/api.md` > `docs/api/` (Evidence) |
| Schema físico Oracle | `database/` (baseline > ddl > migrations) |
| Estado de implementação | `feature.yaml` + git/CI > `construction-state.yaml` > `registry.yaml` > `feature-manifest.status` |

### Dependência de camadas

```text
docs/ → specs/ → construction/ → .cursor/
```

É proibido inverter. `.cursor/` consome SSOT — não define produto.

---

## Mapa SSOT por categoria

| Informação | SSOT | Derivado | Operacional | Legado |
|------------|------|----------|-------------|--------|
| Visão / escopo MVP | `docs/domain/01-vision.md`, `docs/backlog/04-mvp-scope.md` | seções de escopo em specs | — | — |
| Requisitos funcionais | `specs/features/*/specification.md` | `traceability.md` | — | — |
| Regras de negócio | `docs/domain/09-business-rules.md` | RF/RN em specs | — | — |
| Glossário | `docs/domain/02-business-glossary.md`, `03-ubiquitous-language.md` | `specs/foundation/glossary.md` | — | termos desalinhados em `03` |
| Identidade de feature | `specs/features/<slug>/feature.yaml` | — | — | — |
| Especificação de feature | `specs/features/<slug>/` | use-cases, flows, state-machine | — | — |
| Casos de uso | `specs/features/*/use-cases.md` | traceability | — | — |
| Fluxos | `specs/features/*/flows.md` | — | — | — |
| Estados | `specs/features/*/state-machine.md` | — | — | — |
| Contrato API (SDD) | `specs/features/*/api.md` | — | `docs/api/` | — |
| Modelo físico | `database/` | `database/model/` | — | — |
| Modelo lógico | `docs/architecture/05-data-architecture.md` | `solution-design/07-data-ownership.md` | — | — |
| Decisões arquiteturais | `docs/architecture/08-decision-records.md` | `decisions/*` | — | — |
| Decisões governança | `docs/governance/03-open-decisions.md` | — | — | colisão IDs com `technology/04` |
| Decisões feature | `specs/features/*/decisions.md` (quando existe) | — | — | — |
| Tarefas / plano | **`specs/features/*/tasks.md`** | `frontend-tasks.md` | — | PKG tables, `execution-plan.md` |
| Fluxo de desenvolvimento | `development-workflow.md` | — | CI + PR | PKG/session/orchestrator |
| Paths de código | `path-conventions.md` | manifests (legado) | — | `feature-manifest.yaml` |
| Estado implementação | `feature.yaml` + git/CI | `construction-state.yaml` | `registry.yaml` (índice) | `manifest.status.phase` |
| CI | `.github/workflows/` | — | logs de build | — |
| Config Cursor | `.cursor/rules/core/project-index.mdc` | rules on-demand | agents ativos | archived agents |
| Registry construction | `construction/registry.yaml` | legacy registries | construction-state | manifest como SSOD |
| Governança documental | `docs/governance/07-documentation-architecture.md` | — | `01-project-status.md` | `history/*` |
| Processo SDD | `specs/foundation/` (DoR, DoD, conventions) | `workflow.md` | — | — |
| Arquitetura AUTH | `specs/architecture/authentication-architecture.md` | `authentication/decisions.md` | — | — |

---

## O que NÃO é SSOT

| Artefato | Papel |
|----------|-------|
| `feature-manifest.yaml` | Índice de paths legado — ver `path-conventions.md` |
| `execution-plan.md` | Histórico de planejamento — ver `tasks.md` |
| `session.md` | Snapshot imutável de sessão fechada — Archive |
| `pkg-XX/status.md` | Histórico local de PKG — Evidence, não estado global |
| `construction/registry.yaml` status fields | Indicativo — derivar de construction-state ou git/CI |
| `.cursor/rules/workflows/feature-construction-workflow.mdc` | Framework v4.1 legado — Archive |

---

## Referências

- [`path-conventions.md`](path-conventions.md) — descoberta de paths
- [`development-workflow.md`](development-workflow.md) — fluxo diário
- [`docs/governance/09-framework-simplification-scope.md`](../../docs/governance/09-framework-simplification-scope.md) — escopo Etapa 2
- [`docs/governance/07-documentation-architecture.md`](../../docs/governance/07-documentation-architecture.md) — arquitetura documental completa
