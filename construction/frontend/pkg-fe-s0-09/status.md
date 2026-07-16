# PKG-FE-S0-09 — Testing Infrastructure

| Campo | Valor |
|-------|-------|
| Status | **DONE** |
| Sprint | Frontend Foundation (Sprint 0) |
| Dependência | PKG-FE-S0-01, PKG-FE-S0-05 |
| Validado em | 2026-07-16 |

## Entregas

### Ferramentas

| Ferramenta | Configuração |
|------------|--------------|
| Vitest | `vitest.config.ts` — happy-dom, Quasar plugin, coverage v8 |
| Playwright | `playwright.config.ts` — Chromium, webServer `yarn dev` (:9000) |
| Setup | `test/vitest/setup.ts` — Quasar + Notify + `iconSet` mdi-v7 |
| Validação | `scripts/validate-fe-s0-09.sh` + `yarn test:validate` |

### Scripts (`package.json`)

| Script | Comando |
|--------|---------|
| `test` | `yarn typecheck && vitest run --coverage` |
| `test:unit` | `vitest run --coverage` |
| `test:unit:watch` | `vitest` (watch) |
| `test:e2e` | `playwright test` |
| `test:e2e:install` | `playwright install chromium --with-deps` |
| `test:validate` | Pipeline completo FE-S0-09 (log em `reports/`) |

### Testes unitários (`test/unit/` — 16 arquivos, 38 testes)

| Área | Arquivos |
|------|----------|
| Auth infra | `auth/csrf.spec.ts`, `auth/storage-policy.spec.ts` |
| Composables | `useLoading`, `useFormValidation`, `useTheme`, `useStandardErrorHandling` |
| Guards | `auth.guard`, `authorization.guard` |
| HTTP | `csrf`, `request/response.interceptor`, `error-handler`, `base-api-client` |
| Shared components | `shared-components.spec.ts` (+ `DsButton`) |
| Infraestrutura | `constants`, `routes` |

### Testes E2E (`test/e2e/`)

| Arquivo | Cenário |
|---------|---------|
| `bootstrap.spec.ts` | Home carrega; rota inexistente renderiza 404 |

### CI

| Artefato | Detalhe |
|----------|---------|
| `.github/workflows/frontend.yml` | lint → test → Playwright → build |

## Correções aplicadas (2026-07-16)

| Issue | Correção |
|-------|----------|
| Import CSS inexistente `@quasar/extras/mdi-v7/material-design-icons.css` | Removido; `iconSet` mdi-v7 no plugin Quasar (`setup.ts`) |
| `useTheme` toggle flaky entre testes | `beforeEach` reseta modo para `light` |

## Validações locais

| Comando | Resultado |
|---------|-----------|
| `yarn typecheck` | ✅ exit 0 |
| `yarn test:unit` | ✅ **38/38** testes, 16 arquivos — exit 0 |
| Cobertura (v8) | Stmts **50%**, Branch **67.31%**, Funcs **57.89%**, Lines **50%** |
| `yarn test:e2e` | ⏳ executar `yarn test:validate` ou pipeline CI |
| `yarn build` | ⏳ executar `yarn test:validate` ou pipeline CI |
| `yarn test` | ✅ typecheck + unit (via `yarn test`) |

## Critérios (AC-FE-S0-016, AC-FE-S0-017, AC-FE-S0-018)

| Critério | Atendido |
|----------|----------|
| Vitest operacional com testes unitários | ✅ |
| Playwright configurado com E2E estrutural | ✅ |
| Pipeline GitHub Actions | ✅ |
| Relatório de cobertura gerado | ✅ |
| Sem testes de Features / APIs reais | ✅ |

## Notas

- Sprint 0 Frontend Foundation: **9/9 PKGs** concluídos
- E2E/build: validar com `yarn test:validate` em WSL (Playwright + Quasar dev)
