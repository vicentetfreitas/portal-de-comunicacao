# Frontend Validation Gates — Construction Framework (BUILD-02 / E2E-01)

| Item | Valor |
|------|-------|
| Regras | **BUILD-02** (gates por PKG) · **E2E-01** (suíte Playwright) |
| Versão | 1.0 |
| Data | 2026-07-17 |
| Decisão | DL-EF-4.2-010 em `14-framework-decisions-v4.1.md` |

---

## Problema

A suíte Playwright do frontend é **compartilhada** (`test/e2e/**/*.spec.ts`). Executar `yarn test:e2e` no gate de **cada** PKG (ex.: via `FULL_VALIDATION=1` legado) faz com que:

- falhas em Features ou cenários **fora do escopo** do PKG atual bloqueiem PKG-FE-01..05;
- correções de E2E se misturem com entregas incrementais (scaffold, create, list);
- o executor precise decidir repetidamente se E2E “conta” ou não para o PKG em curso.

---

## Princípio

| Camada | Responsabilidade |
|--------|------------------|
| **PKG-FE-01..05** | Qualidade **incremental** do código entregue no pacote |
| **PKG-FE-06** (closure) | **Estabilização E2E** da Feature + hub/encerramento documental |
| **Encerramento Feature** | `FEATURE_APPROVED` só com E2E da Feature **PASS** no closure PKG |

E2E **não** é critério de `PASS` em PKG-FE-01..05.

---

## Gate PKG (padrão — PKG-FE-01..05 e foundation)

Comandos obrigatórios para `VALIDATION SUMMARY` → `PASS`:

```text
yarn lint:check
yarn typecheck
yarn test:unit
yarn build
```

| Regra | Descrição |
|-------|-----------|
| BUILD-02-01 | Todo PKG frontend documenta **somente** estes comandos em Validation/Revalidation |
| BUILD-02-02 | Falha em `test:e2e` **não** impede `PASS` em PKG-FE-01..05 |
| BUILD-02-03 | Scripts por PKG (`frontend/scripts/revalidate-pkg-fe-NN.sh`) devem implementar o Gate PKG |

Evidência:

```bash
PKG_DIR=construction/frontend/features/FT-<DOMAIN>/pkg-fe-NN \
  FULL_VALIDATION=1 \
  bash construction/templates/pkg-evidence-run-frontend.sh
```

(`FULL_VALIDATION=1` = Gate PKG — **sem** E2E; ver script.)

---

## Gate E2E (obrigatório — apenas PKG de closure)

| Quando | Obrigatório |
|--------|-------------|
| **PKG-FE-06** (ou PKG equivalente “Hub, Tests & Closure” no template CRUD frontend) | Sim |
| PKG-FE-01..05 | **Não** |
| Encerramento workstream frontend (`FEATURE_APPROVED`) | Sim — via PKG-FE-06 `PASS` com E2E |

Comandos adicionais:

```text
yarn test:e2e
```

Escopo esperado do PKG-FE-06:

- implementar ou ajustar specs **AT-FE-*** da Feature em `test/e2e/<feature>/`;
- estabilizar locators, mocks e dados da **própria Feature**;
- falhas em specs de **outras** Features não bloqueiam PKG-FE-01..05; no PKG-FE-06, a suíte **completa** do repositório deve passar antes do encerramento (política de integração frontend).

Evidência:

```bash
PKG_DIR=construction/frontend/features/FT-<DOMAIN>/pkg-fe-06 \
  FULL_VALIDATION=1 E2E_VALIDATION=1 \
  bash construction/templates/pkg-evidence-run-frontend.sh
```

| Regra | Descrição |
|-------|-----------|
| E2E-01-01 | `E2E_VALIDATION=1` **somente** no PKG de closure frontend |
| E2E-01-02 | `PASS` no PKG-FE-06 exige `EXIT_TEST_E2E=0` na evidência |
| E2E-01-03 | Estabilização E2E é responsabilidade **explícita** do PKG-FE-06, não dos PKGs incrementais |
| E2E-01-04 | Runner define `PLAYWRIGHT_SINGLE_WORKER=1` (não `CI=1`) para serializar testes sem desabilitar `reuseExistingServer` |
| E2E-02 | Política de **comportamento** dos specs — `17-frontend-e2e-behavior-policy.md` (obrigatória no PKG-FE-06) |

---

## E2E-02 — Comportamento dos specs (referência)

Ao estabilizar Playwright no **PKG-FE-06**, aplicar **E2E-02** (`construction/17-frontend-e2e-behavior-policy.md`):

- validar fluxos e mensagens, não classes Quasar/CSS;
- paginação via controle acessível (**Próxima página**), não botão numérico inexistente no QTable;
- asserts de listagem alinhados ao `sort` da API/mock.

Guia de execução: `frontend/test/e2e/README.md`.

---

## Estados VAL-02 (inalterados)

`PASS` / `BUILD_FAILURE` / `ENVIRONMENT_FAILURE` / `PENDING_REVALIDATION` aplicam-se ao **pipeline documentado** em cada PKG:

- PKG-FE-01..05: pipeline = Gate PKG;
- PKG-FE-06: pipeline = Gate PKG + Gate E2E.

---

## Definition of Done — Construction (frontend)

Para workstream frontend CRUD (template FT-SINGULAR):

| Critério | Onde validar |
|----------|----------------|
| PKG-FE-01..05 implementados | `status.md` + Gate PKG `PASS` |
| AT-FE cobertos em Playwright | `pkg-fe-06` + `test/e2e/` |
| Suíte E2E verde | `pkg-fe-06` evidência com `E2E_VALIDATION=1` |
| Encerramento | `closure-report.md` + `construction-state.yaml` `FEATURE_APPROVED` |

Specs: critérios de aceite em `specs/features/<feature>/acceptance-tests.md` permanecem fonte; execução E2E obrigatória na Construction apenas no **PKG-FE-06**.

---

## Fluxo

```text
PKG-FE-01 ──Gate PKG PASS──► … ──► PKG-FE-05
                                      │
                                      ▼
                            PKG-FE-06 (E2E Stabilization & Closure)
                            Gate PKG + Gate E2E
                                      │
                                      ▼
                         Workstream FEATURE_APPROVED
```

---

## Referências

- `construction/templates/pkg-evidence-run-frontend.sh`
- `construction/templates/pkg-validation-summary.md`
- `construction/04-construction-rules.md` — R-25, R-26
- `construction/golden-template/FT-SINGULAR.md` — sequência PKG-FE-06
