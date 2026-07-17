# Playwright E2E — Portal de Comunicação

| Item                | Valor                                                                                              |
| ------------------- | -------------------------------------------------------------------------------------------------- |
| Config              | `frontend/playwright.config.ts`                                                                    |
| Comando             | `yarn test:e2e`                                                                                    |
| Closure (PKG-FE-06) | `PLAYWRIGHT_SINGLE_WORKER=1` — ver runner em `construction/templates/pkg-evidence-run-frontend.sh` |

---

## Política oficial (SSOT)

**Toda** escrita ou revisão de spec deve seguir:

```text
construction/17-frontend-e2e-behavior-policy.md   (E2E-02)
construction/16-frontend-validation-gates.md    (E2E-01 — quando executar)
```

Resumo:

- Validar **comportamento do usuário**, não DOM/Quasar/CSS.
- Paginação: **Próxima página** + asserts de conteúdo; não botão `"2"`.
- Dados paginados: alinhar expects ao **sort** da API/mock (`name,asc`).
- Erros de campo: `getByRole('alert', …)` (contrato `DsInput`).
- Status no detalhe: `getByRole('status', …)` (contrato `DsBadge`).

---

## Estrutura

```text
test/e2e/
├── bootstrap.spec.ts
├── equipe/
├── singular/
└── support/          ← auth-mock, *-api-mock (manter sort/filtros alinhados à API)
```

Novas Features: `test/e2e/<slug>/` + mocks em `support/`.

---

## Execução local

```bash
cd frontend
source ~/.nvm/nvm.sh
yarn test:e2e:install    # primeira vez
PLAYWRIGHT_SINGLE_WORKER=1 yarn test:e2e
```

Filtro por arquivo:

```bash
yarn playwright test test/e2e/equipe/equipe.spec.ts
```
