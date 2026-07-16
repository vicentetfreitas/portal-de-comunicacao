# PKG-FE-05 — Status Change UI

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR (Frontend) |
| PKG | FE-05 |
| Status | **DONE** |
| Data início | 2026-07-16 |
| Data conclusão | 2026-07-16 |
| Executor | feature-implementer |

---

# Escopo

Interface para ativação e inativação lógica de singulares.

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| Dialog status | `components/organization/singular/SingularStatusDialog.vue` | ✅ |
| Helpers status | `composables/organization/singular-status.ts` | ✅ |
| Integração detalhe | `pages/organization/singular/SingularDetailPage.vue` | ✅ |
| Testes | `test/unit/composables/singular-status.spec.ts` | ✅ |
| Testes | `test/unit/components/SingularStatusDialog.spec.ts` | ✅ |

## Comportamento

- Botão "Ativar" ou "Inativar" conforme status atual
- Dialog de confirmação (inativação com aviso de áreas ativas; reativação com confirmação)
- PATCH com `{ status: 'ACTIVE' | 'INACTIVE' }`
- Erro 422 — mensagem de negócio do backend via `handleError`
- Badge atualizado após sucesso (atualização reativa de `singular`)

---

# Rastreabilidade

| Task | AT |
|------|-----|
| TASK-SINGULAR-FE-005 | AT-FE-SINGULAR-005 |

---

# Validação

| Verificação | Resultado |
|-------------|-----------|
| Vitest — helpers + dialog | ✅ |
| Playwright — inativação e bloqueio | ⬜ PKG-FE-06 |

---

# Próximo PKG

**PKG-FE-06** — Admin Hub, Tests & Closure
