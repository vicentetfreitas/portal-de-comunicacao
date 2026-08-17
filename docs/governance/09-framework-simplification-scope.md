# Framework Simplification Scope — Etapa 2

| Item | Valor |
|------|-------|
| Status | Approved — Etapa 2/3 2026-08-13 |
| Categoria documental | SSOT |
| Referência | `specs/foundation/minimal-ssot.md`, `specs/foundation/development-workflow.md` |

## Objetivo

Registrar o escopo aprovado da simplificação do Engineering Framework (Etapa 2), sem redesenhar o framework legado v4.1.

## Fluxo diário (preferencial)

```text
specs/features/<slug>/ → tasks.md → código → CI → PR review
```

## Substitutos aprovados

| Legado | Substituto |
|--------|------------|
| `feature-manifest` paths | `specs/foundation/path-conventions.md` |
| `execution-plan.md` | `specs/features/*/tasks.md` |
| `session.md` | specs direto |
| `pkg-XX/status.md` | CI logs / PR |
| `registry.status` | `construction-state.yaml` ou estado derivado (git/CI) |
| Orchestrator `Execute PKG-XX` | tasks TK-* sequenciais |

## O que permanece (transição)

- `construction/registry.yaml` — índice de paths (status indicativo)
- `construction-state.yaml` — estado por workstream
- `construction/11-14`, golden-template — Archive para Features históricas

## O que não é SSOT

- `feature-manifest.status`, `session.md`, `pkg-XX/status.md`

## Referências

- [`construction/README.md`](../../construction/README.md)
- [`specs/foundation/development-workflow.md`](../../specs/foundation/development-workflow.md)
