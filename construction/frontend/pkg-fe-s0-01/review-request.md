# PKG-FE-S0-01 — Review Request

| Campo | Valor |
|--------|--------|
| Package | PKG-FE-S0-01 — Project Bootstrap |
| Sprint | Frontend Foundation (Sprint 0) |
| Status solicitado | **PENDING_REVIEW** → **DONE** |
| Data | 2026-07-15 |
| Solicitante | feature-implementer |

---

## Resumo

Implementação do bootstrap frontend conforme especificação Sprint 0. Projeto Quasar/Vue 3/TypeScript com estrutura oficial, configuração multi-ambiente, Pinia, i18n pt-BR, history mode, proxy dev e integração Docker Compose.

---

## Artefatos para Review

| Artefato | Caminho |
|----------|---------|
| Implementation Report | `construction/frontend/pkg-fe-s0-01/implementation-report.md` |
| PKG Status | `construction/frontend/pkg-fe-s0-01/status.md` |
| Especificação | `docs/construction/frontend/00-frontend-foundation.md` §3.1, PKG-FE-S0-01 |
| Código | `frontend/` |

---

## Checklist de Review

### Escopo

- [ ] Apenas PKG-FE-S0-01 implementado (sem Theme, DS, Layouts, Axios, Auth, testes E2E)
- [ ] Nenhuma tela ou regra de Feature de negócio
- [ ] Nenhuma integração WordPress ou CMS legado

### Estrutura

- [ ] Diretórios: `pages`, `layouts`, `components`, `services`, `stores`, `router`, `composables`, `types`, `config`, `i18n`
- [ ] `components/ds/{atoms,molecules,organisms}` preparados (vazios)

### Configuração

- [ ] `VITE_APP_ENV` e `VITE_API_BASE_URL` externos (`.env.example`)
- [ ] Sem URLs de API hardcoded em código
- [ ] `vueRouterMode: 'history'`

### i18n

- [ ] Locale padrão `pt-BR`
- [ ] Boot file registrado em `quasar.config.ts`

### Pinia

- [ ] Store factory em `src/stores/index.ts`

### Docker

- [ ] Serviço `frontend` em `docker-compose.yml`
- [ ] `frontend/Dockerfile` com targets `development` e `production`

### Qualidade

- [ ] Scripts `dev`, `build`, `lint`, `lint:check`, `test` (typecheck) em `package.json`
- [ ] `yarn build` exit 0 *(verificar no ambiente local)*
- [ ] `yarn lint:check` exit 0 *(verificar no ambiente local)*
- [ ] `yarn test` exit 0 *(verificar no ambiente local)*

---

## Verificações Pendentes do Reviewer

Devido a bloqueio de ambiente (TLS WSL / UNC Windows), o executor **não confirmou** build em CI local. O reviewer deve executar:

```bash
cd /home/projects/portal-de-comunicacao/frontend
rm -rf node_modules .quasar dist
cp .env.example .env
yarn install && yarn build && yarn lint:check && yarn test
```

**Critério de aprovação:** todos os comandos acima com exit 0 e `dist/spa/index.html` presente.

---

## Veredito Solicitado

| Opção | Condição |
|-------|----------|
| **APPROVED** | Checklist completo + build verde |
| **APPROVED WITH OBSERVATIONS** | Escopo correto; observações menores documentadas |
| **REJECTED** | Escopo violado ou build falha após verificação local |

---

## Impacto no Estado da Sprint

Após aprovação:

1. Atualizar `pkg-fe-s0-01/status.md` → **DONE**
2. Atualizar `sprint-0-state.yaml`: mover PKG-FE-S0-01 para `completed`; `current_pkg: PKG-FE-S0-02`
3. Liberar execução de **PKG-FE-S0-02 — Theme**

---

## Referências

- `construction/frontend/sprint-0-manifest.yaml`
- `construction/frontend/package-index.md`
- `docs/audit/frontend-construction-readiness.md`
