# Tasks — FT-COLABORADOR

| Task | RF | AT |
|------|----|-----|
| TK-001 | RF-001 | AT-001 |
| TK-002 | RF-002 | AT-002 |
| TK-003 | RF-003 | AT-003 |
| TK-004 | RF-004 | AT-004 |
| TK-005 | RF-005 | AT-005 |

---

## Progresso — camada frontend (`specification-frontend.md`)

Evidência (Git/código/testes), não estado — `feature.yaml` (`IMPLEMENTING`) continua a fonte de estado.

| RF-FE | Tela | Situação em 2026-08-26 |
|-------|------|------------------------|
| RF-FE-COLABORADOR-001 (Cadastrar) | `ColaboradorCreatePage.vue` | Implementada (pré-existente) |
| RF-FE-COLABORADOR-002 (Detalhe) | `ColaboradorDetailPage.vue` | Implementada (pré-existente) |
| RF-FE-COLABORADOR-003 (Listagem) | `ColaboradorListPage.vue` | Implementada (pré-existente) |
| RF-FE-COLABORADOR-004 (Edição) | `ColaboradorEditPage.vue` | **Implementada nesta rodada** — era um stub-placeholder (`colaborador.stub.placeholder`) sem formulário real. Reaproveita `ColaboradorForm`/`useColaboradorForm` (já tinham `validateUpdate`/`toUpdateRequest`/`mapColaboradorToForm` prontos, não usados por nenhuma tela ainda), seguindo o padrão de `SingularEditPage.vue`/`EquipeEditPage.vue` (prop `mode`, e-mail somente leitura — RN-009). Corrigido de quebra um bug de hidratação em `useColaboradorOrganizationalOptions` (`loadOptionsFor`): os watchers de `singularId`/`areaId` zeravam o campo seguinte de forma incondicional a cada mudança, o que — sem a guarda de hidratação — apagava silenciosamente `areaId`/`teamId` reais logo após o `reset()` os carregar da API. |
| RF-FE-COLABORADOR-005 (Status) | `ColaboradorDetailPage.vue`, `ColaboradorStatusDialog.vue` (novo), `composables/organization/colaborador-status.ts` (novo) | **Implementada nesta rodada** — não existia nenhuma UI de ativar/inativar (`ColaboradorInfoCard.vue` só renderizava o badge, sem ação). Construída espelhando exatamente o padrão de `EquipeDetailPage.vue`/`EquipeStatusDialog.vue`/`equipe-status.ts`: botão de status na página de detalhe, diálogo de confirmação, chamada a `colaboradorService.updateStatus()`. Backend confirmado como já gated por `ensureOrganizationAdministrator`/RN-008 (`validateDeactivation`) em `ColaboradorApplicationService`/`ColaboradorDomainService`. |

**Gap fechado nesta rodada**: `test/e2e/colaborador/colaborador.spec.ts` — suíte única no padrão `test/e2e/singular/`/`test/e2e/equipe/`, com mock-store dedicado `support/colaborador-api-mock.ts` (`installColaboradorApiMock`/`installColaboradorOrgOptionsMock`), cobrindo `AT-FE-COLABORADOR-001` a `005` (Cadastro, Detalhe, Listagem, Edição, Status) + Hub administrativo. Substitui `colaborador-edit.spec.ts` (mocks inline, só RF-FE-004), removido. Mock usa `BUSINESS_RULE_VIOLATION` sem `errors[]` para duplicidade de e-mail/zimbraId e para o bloqueio RN-008 ("Colaborador possui subordinados ativos"), confirmado como o formato real que `ColaboradorDomainService`/`BusinessException` produz — distinto do `VALIDATION_ERROR` com `errors[]` usado por Singular/Equipe para duplicidade de sigla/nome.

`yarn typecheck`, `yarn test:unit --run` (183 testes) e `yarn playwright test` (46 testes, suíte completa) passam sem regressão. Implementação de código completa para as 5 ATs; `feature.yaml` permanece `IMPLEMENTING` — fechamento para `DONE` depende do gate Validate + Review (fora do escopo desta rodada).
