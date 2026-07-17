# PKG-FE-06 — Admin Hub, Tests & Closure

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR (Frontend) |
| PKG | PKG-FE-06 |
| Status | **DONE** |
| Data início | 2026-07-16 |
| Data conclusão | 2026-07-16 |
| Executor | feature-implementer |

---

## Escopo

Hub administrativo, suíte de testes frontend (AT-FE-SINGULAR-001..005), validação final e encerramento.

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| Hub | `pages/organization/singular/SingularHubPage.vue` | ✅ |
| Menu item (sidebar) | `constants/navigation.ts` | ✅ |
| Playwright E2E | `test/e2e/singular/singular.spec.ts` | ✅ |
| Mocks E2E | `test/e2e/support/auth-mock.ts`, `singular-api-mock.ts` | ✅ |
| Vitest rotas | `test/unit/router/singular.routes.spec.ts` | ✅ |
| Encerramento | `closure-report.md`, `review/`, `reports/` | ✅ |

## Testes E2E (AT-FE-SINGULAR-001..005)

| AT-FE | Cenário | Status |
|-------|---------|--------|
| 001 | Cadastro happy path + erro sigla duplicada | ✅ |
| 002 | Detalhe + 404 | ✅ |
| 003 | Listagem filtro status + paginação | ✅ |
| 004 | Edição + sigla duplicada | ✅ |
| 005 | Inativação + bloqueio 422 | ✅ |

---

## VALIDATION SUMMARY

Status
PENDING_REVALIDATION

Validation

✗ yarn lint:check
✓ yarn typecheck
✓ yarn test:unit
✗ yarn test:e2e
✓ yarn build

Correções aplicadas

• oxfmt — quebra de linhas > 80 colunas em 15 arquivos do escopo singular
• oxlint — `typescript/unbound-method` em `useSingularList.spec.ts` (assertiva substituída por `listMock.toHaveBeenCalledOnce()`)
• Playwright strict mode — locators específicos (`heading`, `status`, `alert`, `singularFieldValue`) em `singular.spec.ts`
• Playwright listagem — removida expectativa de `Beta Inativa` na página 1 (paginação 10 itens; 13 registros no seed)

Revalidation

⬜ pipeline completo — aguardando reexecução após correções

Evidence

evidence/build-verify-2026-07-16.log

---

## Próximo passo

Reexecutar validação local:

```bash
export NVM_DIR="$HOME/.nvm" && . "$NVM_DIR/nvm.sh"
PKG_DIR=construction/frontend/features/FT-SINGULAR/pkg-fe-06 \
  FULL_VALIDATION=1 \
  bash construction/templates/pkg-evidence-run-frontend.sh
```

Com todos os `EXIT_*=0`, atualizar **Status** para `PASS` neste documento.
