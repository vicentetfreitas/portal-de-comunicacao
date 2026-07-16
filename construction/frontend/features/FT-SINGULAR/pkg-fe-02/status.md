# PKG-FE-02 — Create Singular Page

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR (Frontend) |
| PKG | FE-02 |
| Status | **DONE** |
| Data início | 2026-07-16 |
| Data conclusão | 2026-07-16 |
| Executor | feature-implementer |

---

# Escopo

Página e formulário de cadastro de singular com integração `POST /api/v1/singulares`.

## Entregas

| Componente | Caminho | Status |
|------------|---------|--------|
| Página cadastro | `pages/organization/singular/SingularCreatePage.vue` | ✅ |
| Formulário | `components/organization/singular/SingularForm.vue` | ✅ |
| Seção básica | `components/organization/singular/SingularBasicInfoSection.vue` | ✅ |
| Field errors helper | `mapSingularFieldErrors` em `useSingularForm.ts` | ✅ |
| i18n | `singular.form.*`, `singular.create.success` | ✅ |
| Testes | `test/unit/composables/useSingularForm.spec.ts` | ✅ |

## Comportamento

- Rota `/app/administrador/singulares/novo`
- Campos: `federationId` (readonly), `name`, `acronym`, `unimedCode`
- Validação client-side via `useSingularForm.validateCreate()`
- Sucesso → notify + redirect detalhe (`singular-detail`)
- Cancelar → listagem
- Erros 400/403/422 → toast (`useStandardErrorHandling`) + erros de campo quando disponíveis

---

# Rastreabilidade

| Task | AT |
|------|-----|
| TASK-SINGULAR-FE-001 | AT-FE-SINGULAR-001 (base UI — E2E em PKG-FE-06) |

---

# Validação

| Verificação | Resultado |
|-------------|-----------|
| Vitest — `useSingularForm` | ✅ |
| Integração `singularService.create` | ✅ (página) |

---

# Próximo PKG

**PKG-FE-03** — List & Detail Pages
