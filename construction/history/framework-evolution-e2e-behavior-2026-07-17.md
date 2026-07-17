# Framework Evolution — E2E Behavioral Policy (E2E-02)

| Item | Valor |
|------|-------|
| Data | 2026-07-17 |
| Versão framework | v4.1.5 |
| SSOT | `construction/17-frontend-e2e-behavior-policy.md` |
| Decisão | DL-EF-4.2-011 |

---

## Problema recorrente

Specs Playwright acoplados a implementação (botão `"2"` no QTable, classes `.q-*`, expects de ordenação incorretos), gerando falhas sem mudança de comportamento do usuário e retrabalho no PKG-FE-06.

## Causa raiz no framework

- **E2E-01** definia *quando* rodar E2E, não *como* escrever specs.
- Ausência de SSOT sobre locators e contratos DS vs Quasar.
- PKG-FE-06 sem checklist explícito de conformidade de testes.
- `pkg-validation-summary` mencionava “strict mode” pontualmente, sem política geral.

## Decisão

**E2E-02** — documento único de política comportamental + guia `frontend/test/e2e/README.md`.

## Arquivos tocados

Ver CHANGELOG [4.1.5].

## Antes × Depois

| Antes | Depois |
|-------|--------|
| Decisão ad hoc por spec | SSOT E2E-02 + R-27 |
| Locators Quasar/CSS aceitos implicitamente | Lista do que validar / proibir |
| Paginação: botão `"2"` | Controle acessível + assert de conteúdo |
| DS sem contrato para E2E | DsInput alert, DsBadge status, labels i18n |

## Redução de complexidade

- Uma referência para agentes e revisores no PKG-FE-06.
- Menos prompts “corrigir teste ou UI?” — critérios explícitos.
- DS pode evoluir markup se contrato público for preservado.

## Critérios de aceite

| Critério | Atendido |
|----------|----------|
| Problema recorrente | Sim — estabilização FT-EQUIPE/FT-SINGULAR |
| Menos manutenção de testes | Sim — locators resilientes |
| Menos decisões repetitivas | Sim — SSOT |
| Resiliência ao DS | Sim — contrato público limitado |
| Cobertura funcional | Sim — mapeamento AT-FE mantido |
