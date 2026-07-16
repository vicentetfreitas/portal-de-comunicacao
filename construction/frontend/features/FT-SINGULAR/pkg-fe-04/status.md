# PKG-FE-04 — Edit Singular Page

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR (Frontend) |
| PKG | FE-04 |
| Status | **DONE** |
| Data início | 2026-07-16 |
| Data conclusão | 2026-07-16 |
| Executor | feature-implementer |

---

# Escopo

Página de edição reutilizando formulário de cadastro em modo edit.

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| Página edição | `pages/organization/singular/SingularEditPage.vue` | ✅ |
| Form mode edit | `SingularForm.vue` (prop `mode: 'edit'`) | ✅ |
| Testes | `test/unit/composables/useSingularForm.spec.ts` (update) | ✅ |

## Comportamento

- Rota: `/app/administrador/singulares/:id/editar`
- Carrega dados via `getById` antes de renderizar formulário
- `federationId` somente leitura (modo edit em `SingularBasicInfoSection`)
- Submit via PUT; sucesso → redirect detalhe
- Cancel → retorno ao detalhe

---

# Rastreabilidade

| Task | AT |
|------|-----|
| TASK-SINGULAR-FE-004 | AT-FE-SINGULAR-004 |

---

# Validação

| Verificação | Resultado |
|-------------|-----------|
| Vitest — `validateUpdate` / `toUpdateRequest` | ✅ |
| Playwright — edição happy path | ⬜ PKG-FE-06 |

---

# Próximo PKG

**PKG-FE-05** — Status Change UI
