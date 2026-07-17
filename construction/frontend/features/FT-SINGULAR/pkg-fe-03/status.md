# PKG-FE-03 — List & Detail Pages

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR (Frontend) |
| PKG | PKG-FE-03 |
| Status | **DONE** |
| Data início | 2026-07-16 |
| Data conclusão | 2026-07-16 |
| Dependência | PKG-FE-01 |
| Executor | feature-implementer |

---

## Escopo

Listagem paginada com filtros e página de detalhe da singular.

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| Listagem | `pages/organization/singular/SingularListPage.vue` | ✅ |
| Detalhe | `pages/organization/singular/SingularDetailPage.vue` | ✅ |
| Filtros | `components/organization/singular/SingularFilters.vue` | ✅ |
| Card info | `components/organization/singular/SingularInfoCard.vue` | ✅ |
| Composable list | `composables/organization/useSingularList.ts` | ✅ |
| Testes | `test/unit/composables/useSingularList.spec.ts` | ✅ |

## Comportamento

**Listagem** — paginação/filtros via `singularService.list`, `DsDataTable`, empty state, ações criar/ver detalhe.

**Detalhe** — `singularService.getById`, badge de status, ações editar/voltar; alterar status integrado em PKG-FE-05.

## VALIDATION SUMMARY

Status
PASS

Validation

✓ yarn typecheck
✓ yarn test
✓ yarn build

Correções aplicadas

• (nenhuma)

Revalidation

✓ typecheck
✓ test
✓ build

Evidence

evidence/build-verify-2026-07-16.log

## Rastreabilidade

| Task | AT |
|------|-----|
| TASK-SINGULAR-FE-002 | AT-FE-SINGULAR-002 |
| TASK-SINGULAR-FE-003 | AT-FE-SINGULAR-003 |

E2E Playwright: escopo PKG-FE-06.

## Próximo PKG

**PKG-FE-04** — Edit Singular Page
