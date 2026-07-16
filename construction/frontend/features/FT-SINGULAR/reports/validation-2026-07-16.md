# Validation Report — FT-SINGULAR Frontend (PKG-FE-06)

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR (Frontend) |
| Data | 2026-07-16 |
| PKG | FE-06 |

---

# Comandos de validação

Executar no diretório `frontend/`:

```bash
yarn lint:check
yarn test:unit
yarn test:e2e:install
yarn test:e2e
yarn build
```

---

# Escopo de testes automatizados

## Vitest

| Suite | Arquivo |
|-------|---------|
| Service | `test/unit/organization/singular.service.spec.ts` |
| Form composable | `test/unit/composables/useSingularForm.spec.ts` |
| List composable | `test/unit/composables/useSingularList.spec.ts` |
| Status helpers | `test/unit/composables/singular-status.spec.ts` |
| Status dialog | `test/unit/components/SingularStatusDialog.spec.ts` |
| Routes | `test/unit/router/singular.routes.spec.ts` |

## Playwright (AT-FE-001..005)

| Arquivo | Cenários |
|---------|----------|
| `test/e2e/singular/singular.spec.ts` | 10 testes (5 AT + hub) |

Mocks: `test/e2e/support/auth-mock.ts`, `singular-api-mock.ts`

---

# Observações

- E2E utiliza mocks de `/api/v1/auth/me` e `/api/v1/singulares**` — não exige backend em execução.
- Validação de build/lint/e2e deve ser confirmada localmente ou em CI antes do merge.
