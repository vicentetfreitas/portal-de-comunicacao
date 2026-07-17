# PKG-FE-05 — Status Change UI

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR (Frontend) |
| PKG | PKG-FE-05 |
| Status | **DONE** |
| Data início | 2026-07-16 |
| Data conclusão | 2026-07-16 |
| Dependência | PKG-FE-03 |
| Executor | feature-implementer |

---

## Escopo

Interface para ativação e inativação lógica de singulares.

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| Dialog status | `components/organization/singular/SingularStatusDialog.vue` | ✅ |
| Helpers status | `composables/organization/singular-status.ts` | ✅ |
| Integração detalhe | `pages/organization/singular/SingularDetailPage.vue` | ✅ |
| Testes helpers | `test/unit/composables/singular-status.spec.ts` | ✅ |
| Testes dialog | `test/unit/components/SingularStatusDialog.spec.ts` | ✅ |

## Comportamento

- Botão "Ativar" ou "Inativar" conforme status atual
- Dialog de confirmação (inativação com aviso de áreas ativas; reativação com confirmação)
- PATCH com `{ status: 'ACTIVE' | 'INACTIVE' }`
- Erro 422 — mensagem de negócio do backend via `handleError`
- Badge atualizado após sucesso (atualização reativa de `singular`)

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
| TASK-SINGULAR-FE-005 | AT-FE-SINGULAR-005 |

E2E Playwright (inativação e bloqueio): escopo PKG-FE-06.

## Próximo PKG

**PKG-FE-06** — Admin Hub, Tests & Closure
