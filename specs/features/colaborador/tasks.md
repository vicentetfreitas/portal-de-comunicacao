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
| RF-FE-COLABORADOR-005 (Status) | `ColaboradorListPage.vue`/`ColaboradorInfoCard.vue` | Não verificada nesta rodada — não investigada a fundo, escopo desta sessão foi fechar a Edição |

**Gap conhecido**: `AT-FE-COLABORADOR-001` a `005` (Playwright, padrão `test/e2e/singular/` e `test/e2e/equipe/` com mock-store dedicado — `support/singular-api-mock.ts`) ainda não têm um equivalente `test/e2e/colaborador/`. Esta rodada adicionou apenas `test/e2e/colaborador/colaborador-edit.spec.ts` (mocks inline, cobrindo especificamente RF-FE-004 e o bug de hidratação acima) — não a suíte completa no padrão `*-api-mock.ts` que as demais Features CRUD usam. Próximo passo natural antes de fechar a Feature.
