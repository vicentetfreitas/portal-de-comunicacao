# State Index — Histórico de Checkpoints de Reconciliação

| Campo | Valor |
|------|-------|
| Categoria documental | Archive |
| Origem | Extraído de `docs/governance/01-project-status.md` em 2026-08-28 (redução do índice) |
| Motivo | Narrativas de checkpoint são registro histórico, não estado atual; mantê-las no índice inflava a leitura mínima |

O **estado atual** vive em `docs/governance/01-project-status.md`. Este arquivo guarda apenas o
relato dos checkpoints de reconciliação já aplicados — cada um descreve a evidência consultada e a
correção feita naquela data. Consultar só para auditar a linha do tempo das reconciliações.

---

# Checkpoint — 2026-08-28 (Redução do índice)

**Natureza:** redução de `01-project-status.md` para leitura mínima. Adicionada a seção
`# Estado Atual (leitura rápida)` no topo; as narrativas dos checkpoints de 2026-08-21 e
2026-08-26 movidas para este arquivo. Nenhum SSOT, código, spec ou decisão alterado.

**Evidência consultada:**

* `specs/features/*/feature.yaml` — varredura de status de todas as Features.
* `specs/features/documento-gestao/tasks.md` — 4/4 tasks concluídas em 2026-08-27 (`portal-comunicacao-api` `561683e`, `portal-comunicacao-app` `92b7fff`); `feature.yaml` permanece `IMPLEMENTING` (falta DoD / Gate 3 / Gate 6 para `DONE`).
* `specs/features/documento-upload/{tasks,specification}.md` — código completo; pendências de execução para homologação (grants `PERMISSAO_PASTA`, provisionamento MinIO), não de código.
* `specs/features/arquivos/feature.yaml` — `FT-DOCUMENTO` (leitura) `status: DONE`.
* `specs/features/documento-navegacao/feature.yaml` + `git log` `0fa81b9` — spec v1.1, decisões D-01..D-07 fechadas, `DRAFT`.
* `git log` deste monorepo — `V009`/`V010`/`V011` executados; `docs/jira-reconciliation-audit.md` (`c69e46c`) não executado.

**Resultado:** Pendência #11 (índice defasado quanto ao trabalho de 2026-08-27) atendida na
seção de leitura rápida e na tabela de Features. Reconciliação feita com evidência **local**
deste monorepo; o estado de *push* dos repositórios divididos (`portal-comunicacao-api/app`)
não foi verificado nesta passagem e segue como "verificar" na Pendência #8.

---

# Checkpoint — 2026-08-26 (Reconciliação de Estado)

**Natureza:** reconciliação do State Index contra evidência de código (`feature.yaml`, rotas/páginas/composables, testes E2E) para FT-COLABORADOR e FT-AREA-COLABORADOR. Nenhum SSOT, código, spec ou decisão foi alterado nesta reconciliação — apenas este documento.

**Evidência consultada:**

* `specs/features/{colaborador,area-colaborador}/feature.yaml` — ambos `status: DONE`.
* `git log` — commit `a7be2ca` (encerramento FT-COLABORADOR), commit `d00358e` (E2E `AT-FE-COLABORADOR-001..005`), commit `2a9c953` (encerramento FT-AREA-COLABORADOR).
* `frontend/test/e2e/colaborador/colaborador.spec.ts` — confirma as 5 suítes E2E (`AT-FE-COLABORADOR-001..005`) que o checkpoint anterior dava como não localizadas.
* `frontend/src/pages/area-colaborador/*`, `frontend/src/composables/area-colaborador/*`, `frontend/src/router/routes/area-colaborador.routes.ts` — confirmam TK-AREA-COLAB-001..003 implementadas.
* `frontend/src/constants/navigation.ts` (working tree) — comentário registra a decisão de produto de 2026-08-26 que supersede TK-AREA-COLAB-004 (item de nav dedicado removido a favor de "Federação"); `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`.

**Resultado:** este documento estava desatualizado em relação a FT-COLABORADOR (E2E dado como ausente, quando já existe) e a FT-AREA-COLABORADOR (implementação dada como não iniciada, quando a feature já está `DONE`). Corrigido nas seções acima; Pendências #1–#3 do checkpoint anterior (2026-08-21) resolvidas — ver seção Pendências.

---

# Checkpoint — 2026-08-21 (Reconciliação de Estado)

**Natureza:** reconciliação do State Index contra evidência de código e artefatos de auditoria já existentes. Nenhum SSOT, código, spec ou decisão foi alterado nesta reconciliação — apenas este documento.

**Evidência consultada:**

* `git log` (commits `e94a23e`…`9306f94`, 2026-08-17 a 2026-08-20) e `git status`/`git diff` do working tree.
* Código e testes de backend: `PrimeiroAcessoController`, `PrimeiroAcessoApplicationService` (9 testes de aceitação verdes), `ColaboradorController`/`ColaboradorApplicationService` (6 testes de aceitação verdes), `PapelAtribuicaoService` (novo, 2026-08-20).
* Código de frontend: rotas/páginas de `colaborador` (Hub/Lista/Cadastro/Detalhe/Edição, registradas em `router/routes/index.ts`) e de `primeiro-acesso` (`usePrimeiroAcessoPage.ts` + página, registrada em `foundation.routes.ts`).
* `specs/features/{colaborador,primeiro-acesso,area-colaborador}/feature.yaml` e `specification*.md`.
* `docs/audit/12-structural-simplification-audit-w0-w1.md`, `docs/governance/structural-simplification-plan-w2.md`, `docs/audit/13-decision-inventory.md`, `docs/audit/14-governance-audit-inventory-status.md`, `docs/audit/15-migration-planning-inventory.md` (todos 2026-08-20, já existentes — não refeitos aqui).

**Resultado:** este documento estava desatualizado em relação a FT-PRIMEIRO-ACESSO (BE registrado como `not_started`, quando há controller/service/testes) e omitia FT-AREA-COLABORADOR (feature nova desde o último checkpoint). Corrigido nas seções acima.
