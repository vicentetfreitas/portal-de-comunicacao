# PKG-FE-02 — Create Equipe Page

| Campo | Valor |
|--------|--------|
| Feature | FT-EQUIPE (Frontend) |
| PKG | PKG-FE-02 |
| Status | **DONE** |
| Data início | 2026-07-17 |
| Data conclusão | 2026-07-17 |
| Dependência | PKG-FE-01 (**PASS**) |
| Task | TASK-EQUIPE-FE-001 |
| AT | AT-FE-EQUIPE-001 (E2E em PKG-FE-06) |
| Executor | feature-implementer |

---

## Escopo

Página e formulário de cadastro de equipe com integração `POST /api/v1/equipes`.

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| Página cadastro | `pages/organization/equipe/EquipeCreatePage.vue` | ✅ |
| Formulário | `components/organization/equipe/EquipeForm.vue` | ✅ |
| Seção básica | `components/organization/equipe/EquipeBasicInfoSection.vue` | ✅ |
| Áreas ativas | `composables/organization/useEquipeAreaOptions.ts` | ✅ |
| Field errors | `mapEquipeFieldErrors` em `useEquipeForm.ts` | ✅ |
| i18n | `equipe.create.success`, hints de formulário | ✅ |
| Testes | `test/unit/organization/useEquipeForm.spec.ts` | ✅ |

## Comportamento

- Rota `/app/administrador/equipes/novo`
- Select de área via `GET /api/v1/areas` (ativas)
- Validação client-side `validateCreate()`
- Sucesso → notify + redirect detalhe (`equipeDetailPath`)
- Cancelar → listagem
- Erros 422 → campos + `useStandardErrorHandling`

## VALIDATION SUMMARY

Status
PASS

Validation

✓ yarn lint:check
✓ yarn typecheck
✓ yarn test:unit
✓ yarn build

Correções aplicadas

• (nenhuma nesta execução — implementação já presente; revalidação pós PKG-FE-01)

Revalidation

✓ lint:check
✓ typecheck
✓ test:unit (85 testes)
✓ build

Evidence

`evidence/build-verify-2026-07-17.log`

## Rastreabilidade

| Task | AT |
|------|-----|
| TASK-EQUIPE-FE-001 | AT-FE-EQUIPE-001 |

## Próximo PKG

**PKG-FE-03** — List & Detail Pages
