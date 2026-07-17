# PKG-FE-06 — E2E Stabilization, Admin Hub & Closure

| Campo | Valor |
|--------|--------|
| Feature | FT-EQUIPE (Frontend) |
| PKG | PKG-FE-06 |
| Papel | **E2E-01** — único PKG com gate Playwright obrigatório |
| Status | **DONE** |
| Data conclusão | 2026-07-17 |

---

## Entregas

| Componente | Caminho |
|------------|---------|
| Hub | `EquipeHubPage.vue` (PKG-FE-01) |
| Nav admin | `constants/navigation.ts` |
| E2E | `test/e2e/equipe/equipe.spec.ts` |
| Mocks | `test/e2e/support/equipe-api-mock.ts` |
| Encerramento | `closure-report.md` |

## AT-FE-EQUIPE

| AT | Cenários |
|----|----------|
| 001 | Cadastro + nome duplicado |
| 002 | Detalhe + 404 |
| 003 | Filtro status + paginação |
| 004 | Edição + duplicado |
| 005 | Inativação + bloqueio RN-006 |
| Hub | Ações rápidas |

## VALIDATION SUMMARY

Status
**PASS**

Validation

Gate PKG + Gate E2E (E2E-01):

```bash
bash frontend/scripts/revalidate-pkg-fe-06.sh
```

Correções aplicadas

• `DsInput` — mensagem de erro com `role="alert"` e `aria-label` (duplicidade AT-001/004)
• `useEquipeList` / `useSingularList` — `loading` inicial `true` (evita empty state antes do fetch)
• E2E equipe/singular — `exact: true` em opções Ativa/Inativa e botão Ativar; listagem com `waitForResponse` + **Próxima página** (E2E-02)
• Helpers E2E — `gotoEquipeCreate` / `gotoEquipeEdit` (wait API + controles visíveis; AT-004 happy path)
• Runner E2E — `PLAYWRIGHT_SINGLE_WORKER=1` (serial) sem `CI=1` (preserva `reuseExistingServer`)
• Script `frontend/scripts/revalidate-pkg-fe-06.sh`

Revalidation

✅ `revalidate-pkg-fe-06.sh` — `EXIT_LINT=0`, `EXIT_TYPECHECK=0`, `EXIT_TEST_UNIT=0`, `EXIT_TEST_E2E=0`, `EXIT_BUILD=0`

Evidence

`evidence/build-verify-2026-07-17.log` — revalidação 2026-07-17T10:53:32-03:00; suíte Playwright 22 passed

## Encerramento

Workstream frontend → **FEATURE_APPROVED** somente após **PASS** com `E2E_VALIDATION=1` e log `EXIT_TEST_E2E=0`.
