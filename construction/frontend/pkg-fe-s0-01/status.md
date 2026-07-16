# PKG-FE-S0-01 — Project Bootstrap

| Campo | Valor |
|--------|--------|
| Sprint | Frontend Foundation (Sprint 0) |
| Package | PKG-FE-S0-01 |
| Status | **PENDING_REVIEW** |
| Data | 2026-07-15 |
| Executor | feature-implementer |

---

## Escopo

Inicializar o projeto Quasar/Vue 3/TypeScript com estrutura, ambientes, Pinia, i18n base e qualidade de código.

## Entregas

| Componente | Status |
|------------|--------|
| Projeto Quasar em `frontend/` | ✅ |
| `quasar.config.ts`, `package.json` | ✅ |
| Pinia configurado | ✅ |
| Vue Router base (history mode) | ✅ |
| Diretórios oficiais | ✅ `layouts`, `services`, `components/ds`, `composables`, `types`, `config`, `i18n` |
| vue-i18n base (`pt-BR`) | ✅ `src/boot/i18n.ts`, `src/i18n/pt-BR.ts` |
| Configuração multi-ambiente externa | ✅ `.env.example`, `src/config/env.ts` |
| Integração Docker Compose | ✅ `docker-compose.yml`, `frontend/Dockerfile` |
| Scripts dev/build/lint/test | ✅ |
| Qualidade (oxlint/oxfmt) | ✅ scripts configurados |
| Build verificado em CI local | ⚠️ Pendente reviewer (bloqueio ambiente) |

## Fora de escopo (PKGs posteriores)

- Axios / HTTP client → PKG-FE-S0-06
- Vitest / Playwright → PKG-FE-S0-09
- Theme, Design System, Layouts → PKG-FE-S0-02..04

---

## Validação

| Verificação | Resultado |
|-------------|-----------|
| Estrutura de diretórios | ✅ |
| Config externa sem hardcode | ✅ |
| Sem Features de negócio | ✅ |
| `yarn build` | ⚠️ Pendente — ver `evidence/verification-commands.md` |
| `yarn lint:check` | ⚠️ Pendente |
| `yarn test` | ⚠️ Pendente |

---

## Artefatos

| Documento | Caminho |
|-----------|---------|
| Implementation Report | `implementation-report.md` |
| Review Request | `review-request.md` |
| Verification Commands | `evidence/verification-commands.md` |

---

## Próximo PKG

**PKG-FE-S0-02** — Theme (após aprovação em Review)
