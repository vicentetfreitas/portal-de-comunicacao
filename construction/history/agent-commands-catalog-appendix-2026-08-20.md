# Catálogo de Comandos — Apêndices Históricos (v1.0 → v1.2)

| Item | Valor |
|------|-------|
| Origem | `specs/foundation/agent-commands.md` |
| Extraído em | 2026-08-20 |
| Categoria documental | Archive |
| Camada | Construction (histórico) |
| Escopo | Apêndices A, B, C do catálogo de comandos — não evolui |

---

# Apêndice A — Referência histórica (catálogo v1.1 / framework v4.1)

Este apêndice **não é o fluxo cotidiano**. Preserva rastreabilidade. Archive operacional: `docs/governance/09-framework-simplification-scope.md`; `construction/11-feature-execution-workflow.md`.

Não eliminar. Não usar como comandos padrão do Claude Code.

## A.1 Comandos históricos

| Comando | Intenção histórica | Substituto cotidiano |
|---------|--------------------|----------------------|
| `Gate <N>` | Quality Gate formal 1–6 | DoR (`Readiness`) / DoD / review de PR conforme `development-workflow.md` |
| `Execute Feature` | Iniciar Sessão da Feature (SSOD, Snapshot, `session.md`) | `Implement` após `Readiness` |
| `PKG-<NN>` | Construction Package com Session ativa | tasks TK-* em `tasks.md` |
| `Close` | Encerramento formal v4.1 (Closure → Review → Audit → Gate 5 → Build) | Validate + Review + DoD + CI/PR |
| `Report` | Relatório de progresso/PKG/closure | Solicitação explícita; não faz parte do catálogo cotidiano |

### `Gate`

Sintaxe histórica: `Gate <N> <Feature>` — `<N>` de `1` a `6`.

Referências: `specs/foundation/feature-quality-gates.md`; `specs/foundation/review-process.md` (Gate 1); `specs/foundation/definition-of-done.md` (Gate 6).

`Gate 5` era etapa interna de `Close`.

### `Execute Feature`

Iniciava Sessão: `feature-manifest.yaml`; `construction-state.yaml`; `session.md`.

Referências: `construction/11-feature-execution-workflow.md`; `.cursor/rules/workflows/feature-construction-workflow.mdc`.

### `PKG-<NN>`

Sintaxe: `PKG-01 FT-AREA`. Exigia Session ativa (SESSION-01).

Referências: `construction/11-feature-execution-workflow.md`; `construction/templates/pkg-validation-summary.md`; workstreams frontend `construction/16-frontend-validation-gates.md`, `construction/17-frontend-e2e-behavior-policy.md`.

### `Close`

Exigia todos os PKGs em `DONE`. Produzia `closure-report.md` e `phase: closed`.

Referências: `construction/11-feature-execution-workflow.md` (Fase 3); `.cursor/orchestrator/construction-orchestrator.mdc`.

Alias histórico: `Encerrar Feature <Feature>`.

### `Report`

Não alterava artefatos. Exemplo: `Report closure FT-AUTH`.

Referência: `construction/11-feature-execution-workflow.md`.

## A.2 Regras históricas (v1.1) — não aplicar ao cotidiano

| ID | Regra |
|----|-------|
| CMD-05 (v1.1) | Consultar SSOT/SSOD (`feature.yaml`, `feature-manifest.yaml`, `construction-state.yaml`). |
| CMD-06 | `Gate 1` exige `READY_FOR_REVIEW`. |
| CMD-08 | `Execute Feature` exige Readiness pré-Construction aprovado. |
| CMD-09 | `PKG-<NN>` exige Session ativa (`SESSION-01`). |
| CMD-10 | `Close` exige todos os PKGs em `DONE`. |
| CMD-11 | Nenhum Gate pode ser ignorado — use `Gate <N>` ou absorva via `Close` quando aplicável. |
| CMD-12 (parcial) | `Report` não altera artefatos. |
| CMD-13 | Durante `PKG-<NN>`, é proibido `Close`, build global (`mvn clean verify`) ou atualização de documentação global. |
| CMD-14 | Ao concluir `PKG-<NN>`, preencher **VALIDATION SUMMARY** em `pkg-<NN>/status.md`. |
| CMD-15 | Status de validação: `PASS` \| `BUILD_FAILURE` \| `ENVIRONMENT_FAILURE` \| `PENDING_REVALIDATION`. |
| CMD-16 | Artefatos por PKG: somente `status.md` + `evidence/*.log` opcional. |

## A.3 Exemplo histórico (não usar no cotidiano)

```text
Specify FT-AREA
Gate 1 FT-AREA
Specify FT-AREA
Readiness FT-AREA
Execute Feature FT-AREA
PKG-01 FT-AREA
PKG-02 FT-AREA
Close FT-AREA
Gate 6 FT-AREA
Status FT-AREA
```

## A.4 Mapa de referências históricas

| Comando | Documento principal |
|---------|---------------------|
| `Specify` (v1.1) | `.cursor/rules/workflows/specification-flow.mdc`; `.cursor/orchestrator/specification-orchestrator.mdc` |
| `Gate` | `specs/foundation/feature-quality-gates.md` |
| `Execute Feature`, `PKG-<NN>`, `Close` | `construction/11-feature-execution-workflow.md` |
| `Status`, `Report` (v1.1) | `construction/11-feature-execution-workflow.md` (STATE-02) |

---

# Apêndice B — Consolidação v1.0 → v1.1

| Comando v1.0 | Destino v1.1 | Justificativa |
|--------------|--------------|---------------|
| `Validate` | `Specify` | Validação cruzada é Etapa 9 do Specification-flow — consequência natural de `Specify`. |
| `Refine` | `Specify` | Refinamento pós-Gate 1 é evolução documental da Specification — mesma intenção de produção/evolução. |
| `Gate 1` … `Gate 6` (entradas separadas) | `Gate <N>` | Família única parametrizada; mesma mecânica de invocação. |
| `Review` | `Close` | Revisão técnica consolidada é etapa obrigatória do encerramento (Fase 3). |
| `Audit` | `Close` | Auditoria de conformidade é etapa obrigatória do encerramento (Fase 3). |
| `Build` | `Close` | `mvn clean verify` é exclusivo do encerramento (BUILD-01) — detalhe interno, não intenção separada. |
| `Gate 5` (entrada separada) | `Close` (padrão) / `Gate 5` (isolado) | Feature Readiness Review integra `Close`; comando `Gate 5` mantido apenas para execução isolada. |
| `Report Closure`, `Report PKG` (variantes) | `Report` | Escopo indicado como qualificador textual — um único comando de relatório. |

**Comandos mantidos sem alteração de nome na v1.1:** `Specify`, `Readiness`, `Execute Feature`, `PKG-<NN>`, `Close`, `Status`, `Report`.

**Redução v1.0 → v1.1:** 18 entradas de catálogo → **8 comandos essenciais**.

---

# Apêndice C — Alinhamento v1.1 → v1.2 (Etapa 2)

Autorizado por `specs/foundation/migrations/SPEC-MIGRATION-CLAUDE-CODE.md` (D2).

| Comando v1.1 | Destino v1.2 | Justificativa |
|--------------|--------------|---------------|
| `Specify` | `Specify` (cotidiano) | Intenção preservada; referências cotidianas passam a `specs/foundation/` (não duplicar lógica em `.cursor/`). |
| `Readiness` | `Readiness` (cotidiano) | DoR permanece obrigatório. |
| `Status` | `Status` (cotidiano) | Consulta sem mutação; fontes passam a `feature.yaml` + git/CI + `minimal-ssot.md`. |
| `Execute Feature` + `PKG-<NN>` | `Implement` | Substituição aprovada na Etapa 2 (tasks TK-*). Histórico no Apêndice A. |
| (validação de build/testes) | `Validate` | Etapa explícita de `development-workflow.md`. Distinto da validação cruzada documental absorvida em `Specify` na v1.1. |
| `Review` (antes em `Close`) | `Review` | Review de PR no fluxo simplificado; modo somente revisão não edita. |
| `Gate <N>` | Apêndice A | Não faz parte do fluxo cotidiano Claude. |
| `Close` | Apêndice A | Cerimônia v4.1. |
| `Report` | Apêndice A | Fora da lista de invocações cotidianas da migração. |
