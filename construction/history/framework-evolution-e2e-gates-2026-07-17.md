# Framework Evolution — E2E Gates (BUILD-02 / E2E-01)

| Item | Valor |
|------|-------|
| Data | 2026-07-17 |
| Versão framework | v4.1.4 |
| SSOT política | `construction/16-frontend-validation-gates.md` |
| Decisão | DL-EF-4.2-010 |

---

## 1. Problema identificado

Durante a construção de workstreams frontend, PKGs incrementais (PKG-FE-01..05) eram validados com `FULL_VALIDATION=1`, que executava **lint, testes unitários, build e a suíte E2E Playwright compartilhada**. Falhas em specs de outras features, flakiness ou débito E2E acumulado **bloqueavam o PASS** de PKGs cujo escopo não incluía correção daqueles testes. Isso gerava decisões repetitivas (“corrigir E2E agora ou adiar?”), prompts recorrentes e atraso artificial na cadeia de PKGs.

---

## 2. Decisão arquitetural adotada

- **Gate PKG (BUILD-02):** obrigatório para **PASS** em PKG-FE-01..05 (e PKGs frontend incrementais equivalentes): `yarn lint:check`, `yarn typecheck`, `yarn test:unit`, `yarn build`. **Sem E2E.**
- **Gate E2E (E2E-01):** `yarn test:e2e` obrigatório **somente** no **PKG-FE-06** (E2E Stabilization & Feature Closure). `FEATURE_APPROVED` no workstream frontend exige PKG-FE-06 com validação E2E verde.
- **Runner:** `FULL_VALIDATION=1` = Gate PKG; adicionar `E2E_VALIDATION=1` apenas no closure PKG.
- **Novo papel formal do PKG-FE-06:** concentrar estabilização da suíte E2E da feature e encerramento — sem criar workstream extra; reutiliza o último PKG do golden template CRUD frontend.

Qualidade preservada: E2E continua obrigatório antes do encerramento; apenas deixa de ser gate de cada incremento.

---

## 3. Arquivos modificados

| Artefato | Alteração |
|----------|-----------|
| `construction/16-frontend-validation-gates.md` | Novo — SSOT |
| `construction/templates/pkg-evidence-run-frontend.sh` | `E2E_VALIDATION=1`; `FULL_VALIDATION` sem E2E |
| `construction/04-construction-rules.md` | R-25, R-26 |
| `construction/14-framework-decisions-v4.1.md` | DL-EF-4.2-010 |
| `construction/templates/pkg-validation-summary.md` | Matriz PKG vs closure |
| `construction/templates/pkg-status.md` | Instruções runner |
| `construction/templates/pkg-artifact-model.md` | Exemplos runner |
| `construction/11-feature-execution-workflow.md` | BUILD-02 / E2E-01 |
| `construction/12-fullstack-orchestrator.md` | Regras BUILD-02 / E2E-01 |
| `construction/golden-template/FT-SINGULAR.md` | PKG-FE-06, matriz de testes |
| `construction/README.md` | Índice + tabela BUILD-02 / E2E-01 |
| `construction/CHANGELOG.md` | v4.1.4 |
| `.cursor/orchestrator/construction-orchestrator.mdc` | Gates frontend |
| `.cursor/rules/workflows/feature-construction-workflow.mdc` | BUILD-02, E2E-01 |
| `.cursor/rules/core/project-index.mdc` | Ponteiro doc 16 |
| `specs/foundation/definition-of-done.md` | Suplemento construction frontend |
| `specs/foundation/agent-commands.md` | Referência doc 16 |
| `construction/frontend/features/FT-EQUIPE/pkg-fe-06/status.md` | `E2E_VALIDATION=1` |
| `construction/frontend/features/FT-EQUIPE/closure-report.md` | Comando closure |

---

## 4. Impacto no fluxo de construção

| Momento | Antes | Depois |
|---------|-------|--------|
| PKG-FE-01..05 | PASS condicionado a E2E verde (suíte inteira) | PASS com Gate PKG apenas |
| Avanço para próximo PKG | Bloqueado por falhas E2E fora do escopo | Desbloqueado quando Gate PKG = PASS |
| PKG-FE-06 | Mistura closure + E2E sem gate explícito | **Único** ponto com E2E obrigatório (E2E-01) |
| FEATURE_APPROVED (FE) | Ambíguo se E2E era por-PKG | Exige PKG-FE-06 PASS com `E2E_VALIDATION=1` |

Backend inalterado (BUILD-01: `mvn clean verify` no encerramento).

---

## 5. Fluxo antes × depois

```text
ANTES
  PKG-FE-NN → FULL_VALIDATION=1 → lint + unit + E2E (suite) + build
            → falha E2E alheia → BLOCK / não PASS → PKG seguinte parado

DEPOIS
  PKG-FE-01..05 → FULL_VALIDATION=1 → Gate PKG → PASS → próximo PKG
  PKG-FE-06     → FULL_VALIDATION=1 + E2E_VALIDATION=1 → Gate PKG + E2E → closure → FEATURE_APPROVED
```

---

## 6. Redução de complexidade operacional

- **Uma regra por fase:** incremento = build quality; closure = E2E + encerramento.
- **Menos decisões ad hoc:** não é mais necessário negociar se “este PKG” deve corrigir E2E global.
- **Runner único com flags explícitas:** elimina ambiguidade de `FULL_VALIDATION` incluir E2E.
- **Rastreabilidade:** R-25, R-26, DL-EF-4.2-010, doc 16 e PKG-FE-06 no golden template documentam *quando* E2E é obrigatório.
- **Executor:** fecha PKGs com script Gate PKG; reserva tempo de estabilização E2E para PKG-FE-06.

---

## Critérios de aceite (verificação)

| Critério | Atendido |
|----------|----------|
| Fluxo mais simples para quem executa Features | Sim — PASS incremental sem E2E |
| Nenhum PKG bloqueado por E2E de outro escopo | Sim — BUILD-02 |
| Responsabilidade E2E explícita | Sim — PKG-FE-06 / E2E-01 |
| Reduz prompts e decisões repetitivas | Sim — política SSOT em doc 16 |
