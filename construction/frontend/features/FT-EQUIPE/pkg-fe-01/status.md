# PKG-FE-01 — Equipe Module Scaffold & API Client

| Campo | Valor |
|--------|--------|
| Feature | FT-EQUIPE (Frontend) |
| PKG | PKG-FE-01 |
| Status | **DONE** |
| Data início | 2026-07-17 |
| Data conclusão | 2026-07-17 |
| DoR | ✅ Satisfeito |
| Executor | feature-implementer |

---

## Escopo

Infraestrutura do módulo `organization/equipe`: tipos, `equipe.service.ts`, `area.service.ts`, `useEquipeForm.ts`, rotas, páginas stub, i18n.

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| API paths | `frontend/src/config/organization.ts` | ✅ |
| Types equipe | `frontend/src/types/organization/equipe.types.ts` | ✅ |
| Types area | `frontend/src/types/organization/area.types.ts` | ✅ |
| Service equipe | `frontend/src/services/organization/equipe.service.ts` | ✅ |
| Service area | `frontend/src/services/organization/area.service.ts` | ✅ |
| Barrel | `frontend/src/services/organization/index.ts` | ✅ |
| Composable | `frontend/src/composables/organization/useEquipeForm.ts` | ✅ |
| Rotas | `frontend/src/router/routes/organization/equipe.routes.ts` | ✅ |
| Registry | `frontend/src/router/routes/index.ts` | ✅ |
| Pages | `frontend/src/pages/organization/equipe/*.vue` (5) | ✅ |
| i18n | `frontend/src/i18n/pt-BR.ts` — `equipe.*` | ✅ |
| Route constants | `frontend/src/constants/routes.ts` | ✅ |
| Teste service | `frontend/test/unit/organization/equipe.service.spec.ts` | ✅ |
| Routes test | `frontend/test/unit/router/equipe.routes.spec.ts` | ✅ |

## VALIDATION SUMMARY

Status
PASS

Validation

✓ yarn lint:check
✓ yarn typecheck
✓ yarn test:unit
✓ yarn build

Correções aplicadas

• **TS2379** — `DsSelect` props alinhadas a `DsInput` (`string | undefined`)
• **oxfmt** — `EquipeBasicInfoSection.vue`, `EquipeInfoCard.vue` (header alinhado ao singular + `role="status"`)
• **oxlint** — imports/variáveis não usados em `useEquipeList.ts` e `useEquipeForm.spec.ts`
• **Gate PKG-FE-01** — script `revalidate-pkg-fe-01.sh` sem E2E (BUILD-01)

Revalidation

✓ lint:check
✓ typecheck
✓ test:unit (85 testes)
✓ build

Evidence

`evidence/build-verify-2026-07-17.log`

## Próximo PKG

**PKG-FE-02** — Create Equipe Page
