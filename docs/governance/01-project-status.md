# Project Status — State Index

## Objetivo

Este documento é o **índice central do estado atual** do projeto. Ele não é fonte normativa de requisitos, decisões, arquitetura ou schema — cada um desses continua vivendo no seu próprio SSOT (`specs/`, `docs/domain/`, `docs/architecture/`, `docs/technology/`, `database/`). Aqui só se consolida, com apontamento para a fonte:

* progresso das camadas documentais;
* status por feature (com evidência de código, não só de spec);
* marcos alcançados;
* checkpoint mais recente e sua evidência;
* pendências/divergências conhecidas (registradas aqui, corrigidas na fonte);
* critério para revalidação completa vs. validação incremental.

**Não tratar como SSOT:** `construction/registry.yaml`, `session.md`, `pkg-XX/status.md` (mecanismos Construction v4.1 — ver `CLAUDE.md`). Onde este documento cita evidência de implementação, a evidência é o próprio código/teste no repositório, não esses artefatos.

---

# Informações Gerais

| Item                   | Valor                                      |
| ---------------------- | ------------------------------------------ |
| Projeto                | Portal de Comunicação                      |
| Status Geral           | Baseline documental **APROVADA COM RESSALVAS** (Exit Gate 2026-07-24) |
| Versão da Documentação | 2.0 (Exit Gate)                                |
| Última Atualização     | 2026-08-26                                 |
| Responsável            | Project Manager / Arquitetura              |
| Próxima Revisão        | Etapa 5 — infraestrutura local (Oracle)    |

---

# SSOTs referenciados por este índice

| Necessidade | SSOT |
| ----------- | ---- |
| Modelo de organização e execução do projeto (camadas, Package/Task, ciclo operacional) | `docs/governance/10-project-organization.md` |
| Precedência / fluxo diário | `specs/foundation/minimal-ssot.md`, `specs/foundation/development-workflow.md` |
| Foundation (aplicação) — infra transversal (shell, router, sessão, design system) | código em `frontend/`, `backend/` + `docs/implementation/` |
| Especificação por feature | `specs/features/<slug>/` (`feature.yaml` = status oficial da spec) |
| Decisões (organizacional/negócio) | `docs/governance/03-open-decisions.md` |
| Decisões (arquitetura) | `docs/architecture/08-decision-records.md` |
| Decisões (tecnologia) | `docs/technology/04-decision-log.md` (**catálogo duplicado com colisão de ID conhecida — ver Pendências**) |
| Escopo MVP | `docs/backlog/04-mvp-scope.md` |
| Roadmap arquitetural (Etapas 1–6) | `docs/solution-design/10-delivery-roadmap.md` |
| Roadmap executivo | `docs/governance/05-roadmap.md` (**desatualizado desde 2026-07-08 — ver Pendências**) |
| Arquitetura documental / classificação SSOT-Evidence-Working-Archive | `docs/governance/07-documentation-architecture.md` |
| Auditoria estrutural mais recente | `docs/audit/12-structural-simplification-audit-w0-w1.md` + `docs/governance/structural-simplification-plan-w2.md` (D1–D9) |
| Inventário de decisões (DEC/GAP-DEC) | `docs/audit/13-decision-inventory.md` |
| Levantamento factual de status de auditorias/decisões | `docs/audit/14-governance-audit-inventory-status.md` (2026-08-20 — mais atual sobre o que já foi aplicado de D1–D9) |
| Levantamento de artefatos para próxima onda de migração | `docs/audit/15-migration-planning-inventory.md` |

---

# Resumo Executivo

## Situação Atual

O projeto concluiu a **Sprint 0 do backend**, múltiplas features de **Etapa 2** (org + auth/sessão) e a **revalidação SSOT (Etapa 3)** em 2026-08-14. Desde então, **FT-PRIMEIRO-ACESSO**, **FT-COLABORADOR** e **FT-AREA-COLABORADOR** encerraram formalmente (`DONE`) em 2026-08-26 — as três únicas features com implementação concluída e sem checkpoint de fechamento ficaram resolvidas nesta data. FT-COLABORADOR incluiu localizar o E2E Playwright (`AT-FE-COLABORADOR-001..005`) que faltava no checkpoint anterior; FT-PRIMEIRO-ACESSO exigiu migrar `feature.yaml` do esquema legado (`status.specification`) para o escalar atual e corrigir drift em `traceability.md` (duas dívidas documentais aceitas e registradas: sem AT dedicado à conclusão do wizard, sem `tasks.md`).

SSOT operacional: `specs/foundation/minimal-ssot.md`
SSOT normativo documental: `docs/governance/07-documentation-architecture.md` v2.0
Estado de implementação: evidência direta em código/testes do repositório (git log, `backend/src`, `frontend/src`) — **não** `construction/registry.yaml`

**Progresso de construção (evidência: `feature.yaml` de cada feature + presença/execução de código e testes; ver detalhe na tabela de features abaixo):**

* FT-AUTH, FT-AREA, FT-SINGULAR, FT-EQUIPE, FT-SESSION — **closed**
* FT-COLABORADOR — **DONE** (`feature.yaml`, commit `a7be2ca`, 2026-08-26). BE closed (Controller/Service/Repository/Entity + testes verdes); FE implementado (5 rotas, service, composable, testes unitários); **E2E Playwright presente** (`frontend/test/e2e/colaborador/colaborador.spec.ts`, cobre `AT-FE-COLABORADOR-001..005`, commit `d00358e`) — corrige registro anterior deste documento, que dava a E2E como não localizada
* FT-PRIMEIRO-ACESSO — **DONE** (`feature.yaml`, commit `170f005`, 2026-08-26). BE implementado e testado (`PrimeiroAcessoController`, `PrimeiroAcessoApplicationService`, 9 testes de aceitação verdes cobrindo PA-API-006 e PA-API-007) e FE implementado (`usePrimeiroAcessoPage.ts` + página, rota registrada) — corrige registro anterior deste documento, que descrevia BE como `not_started` e o fechamento como não confirmado. Duas dívidas documentais aceitas em `traceability.md`: RF-PA-011 sem AT formal dedicado à conclusão do wizard; sem `tasks.md` nesta feature
* FT-AREA-COLABORADOR — **DONE** (`feature.yaml`, commit `2a9c953`, 2026-08-26). Spec passou de `DRAFT` para `APPROVED` (2026-08-20/21) e as 4 tasks (`tasks.md` v1.1) foram implementadas: hub, dados da Área, equipes da Área — TK-AREA-COLAB-004 (item de nav dedicado) foi **superseded** por decisão explícita de produto (2026-08-26): a entrada "Áreas" foi removida do menu principal a favor de "Federação", que cobre o mesmo escopo (ver `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`); rota `/app/area` permanece funcional — corrige registro anterior deste documento, que dava a implementação como não iniciada
* Integration sprint-03-org-backend — **APPROVED** (2026-07-14)

---

## Objetivos da Fase Atual

* FT-COLABORADOR, FT-AREA-COLABORADOR e FT-PRIMEIRO-ACESSO confirmadas `DONE` em 2026-08-26 — nenhuma feature implementada aguarda checkpoint de fechamento neste momento.
* Consolidar no git o trabalho já presente no working tree (`docker-compose.yml` → Oracle, `.github/workflows/backend.yml`).
* Validar ambiente local Oracle (Etapa 5).
* Manter aderência entre `specs/`, `docs/` e código.
* Evoluir Etapas 3–5 do MVP conforme `docs/backlog/04-mvp-scope.md`.

---

# Status por Camada

## Discovery

| Item                 | Status         |
| -------------------- | -------------- |
| Visão do Produto     | 🟩 Concluído   |
| Stakeholders         | 🟩 Concluído   |
| Objetivos de Negócio | 🟩 Concluído   |
| Escopo               | 🟩 Concluído   |
| Requisitos           | 🟩 Concluído   |

Resultado Atual:

```text
🟩 Concluído
```

---

## Domain

| Item              | Status       |
| ----------------- | ------------ |
| Domínio Principal | 🟩 Concluído |
| Glossário         | 🟩 Concluído |
| Processos         | 🟩 Concluído |
| Regras de Negócio | 🟩 Concluído |
| Casos de Uso      | 🟩 Concluído |

Resultado Atual:

```text
🟩 Concluído
```

---

## Architecture

| Item                | Status       |
| ------------------- | ------------ |
| Contexto do Sistema | 🟩 Concluído |
| Containers          | 🟩 Concluído |
| Componentes         | 🟩 Concluído |
| Integrações         | 🟩 Concluído |
| NFRs                | 🟩 Concluído |

Resultado Atual:

```text
🟩 Concluído — baseline congelada
```

---

## Solution Design

| Item      | Status       |
| --------- | ------------ |
| Fluxos    | 🟩 Concluído |
| APIs      | 🟩 Concluído |
| Dados     | 🟩 Concluído |
| Segurança | 🟩 Concluído |
| Migração  | 🟩 Concluído |

Resultado Atual:

```text
🟩 Concluído
```

---

## Implementation

| Item            | Status                                              |
| --------------- | --------------------------------------------------- |
| Backend         | 🟩 Etapa 2 avançada — org + auth/sessão + Colaborador + Primeiro Acesso implementados e testados; atribuição de papel (role) adicionada em 2026-08-20 (`PapelAtribuicaoService`, ainda não vinculada formalmente a nenhum `feature.yaml` — ver Pendências) |
| Frontend        | 🟩 Foundation + org CRUD (Singular, Equipe, Colaborador) + Primeiro Acesso + FT-AREA-COLABORADOR (`DONE`, 2026-08-26) implementados |
| Banco de Dados  | 🟩 Oracle — baseline homologado; 6 entidades JPA alinhadas (Etapa 4) |
| Testes          | 🟩 Unit + integração Oracle (features org/auth/colaborador/primeiro-acesso); E2E Playwright cobre `colaborador`, `equipe`, `singular`, `federacao`, `app-shell` (FT-COLABORADOR confirmado, `AT-FE-COLABORADOR-001..005`) |
| Observabilidade | 🟨 Parcial — Correlation ID + Actuator              |

Resultado Atual:

```text
🟨 Em andamento — Etapa 2 avançada; Etapas 3–5 pendentes
```

---

## Construction

| Item            | Status                                              |
| --------------- | --------------------------------------------------- |
| Ambiente Local  | 🟨 Backend + FE dev operáveis; Oracle externo obrigatório |
| Docker          | 🟨 `docker-compose.yml` já alterado para `gvenzl/oracle-free` no working tree — **ainda não commitado** (D5 de `structural-simplification-plan-w2.md`) |
| CI/CD           | 🟨 `.github/workflows/backend.yml` presente no working tree, **ainda não commitado**; cobre `mvn clean verify` excluindo testes de integração dependentes de Oracle ("Opção B" — D6/GAP-DEC-003 em `docs/audit/14-...`) |
| Observabilidade | 🟨 Parcial — logging e Correlation ID implementados |

Resultado Atual:

```text
🟨 Em andamento — bootstrap backend concluído; Docker/CI corrigidos localmente, pendente de commit
```

---

## Delivery

| Item         | Status         |
| ------------ | -------------- |
| MVP          | 🟨 Planejado   |
| Release Plan | 🟩 Documentado |
| Cutover Plan | 🟩 Documentado |

Resultado Atual:

```text
🟨 Planejado — aguardando features do MVP
```

---

## Audit

| Item                    | Status                                      |
| ----------------------- | ------------------------------------------- |
| Consistência Documental | 🟩 Reconciliação MVP + Sprint 0 aplicada    |
| Validação de Domínio    | 🟩 Concluída                                |
| Validação Arquitetural  | 🟩 Baseline aprovada                        |
| Readiness Report        | 🟩 Sprint 0 — auditoria final registrada    |

Resultado Atual:

```text
🟩 Concluído — para escopo da Sprint 0
```

---

# Marcos do Projeto

## Concluídos

| Marco | Descrição                                              | Data       |
| ----- | ------------------------------------------------------ | ---------- |
| M1    | Discovery Aprovado                                     | 2026-06-22 |
| M2    | Domain Aprovado                                        | 2026-06-22 |
| M3    | Architecture Aprovada                                  | 2026-06-22 |
| M4    | Solution Design Aprovado                               | 2026-06-22 |
| M5    | Implementation Documentada                             | 2026-06-22 |
| M6    | Reconciliação MVP Consolidation                        | 2026-06-22 |
| M7    | **Sprint 0 Backend — Infraestrutura Transversal**      | 2026-07-08 |
| M8    | **Baseline Arquitetural Backend — Aprovada e Congelada** | 2026-07-08 |
| M9    | Integration sprint-03-org-backend APPROVED | 2026-07-14 |
| M10   | FT-AUTH, FT-SINGULAR (BE+FE), FT-EQUIPE, FT-SESSION closed | 2026-08 |
| M11   | Revalidação SSOT Etapa 3 + auditoria JPA Etapa 4 | 2026-08-14 |
| M12   | FT-PRIMEIRO-ACESSO — spec consolidada (5 artefatos) | 2026-08-17 |
| M13   | Auditoria estrutural W0–W1 + Plano W2 (D1–D9) | 2026-08-20 |
| M14   | FT-COLABORADOR (BE+FE) e FT-PRIMEIRO-ACESSO (BE+FE) — código e testes presentes no repositório (evidência de código; fechamento formal pendente de checkpoint — ver Pendências) | 2026-08-20/21 |
| M15   | FT-COLABORADOR — fechamento formal (`DONE`), E2E Playwright (`AT-FE-COLABORADOR-001..005`) confirmado | 2026-08-26 |
| M16   | FT-AREA-COLABORADOR — spec `APPROVED` → implementada → fechamento formal (`DONE`) | 2026-08-26 |
| M17   | FT-PRIMEIRO-ACESSO — fechamento formal (`DONE`); `feature.yaml` migrado do esquema legado; drift de `traceability.md` corrigido | 2026-08-26 |

---

## Em Andamento

| Marco | Descrição                              | Status          |
| ----- | -------------------------------------- | --------------- |
| M18   | Etapa 5 — infraestrutura local Oracle  | Planejado       |

---

## Planejados

* Entrega do MVP (Etapas 1–5)
* Go Live
* Release 1, 2 e 3

---

# Indicadores de Prontidão

| Indicador            | Status | Observação                                      |
| -------------------- | ------ | ----------------------------------------------- |
| Escopo Definido      | 🟩     | MVP consolidado (Etapas 1–5)                    |
| Requisitos Validados | 🟩     | Domain e specs foundation concluídos            |
| Arquitetura Validada | 🟩     | Baseline congelada pós-Sprint 0                  |
| Solução Detalhada    | 🟩     | Solution Design concluído                       |
| Construção Preparada | 🟩     | Infraestrutura backend operacional              |
| MVP Planejado        | 🟩     | Roadmap e backlog alinhados                     |

**Andamento geral estimado:** ~45% (Etapas 1–2 parcialmente implementadas; documental e comunicação pendentes).

---

# Impedimentos

## Impedimentos Atuais

Nenhum impedimento ativo. Ambiente local requer Oracle acessível (`UNMPORTCOM_APP`). Nenhum dos itens da seção Pendências abaixo é bloqueante — todos são de registro/reclassificação, não de capacidade técnica.

---

# Próximas Ações

## Curto Prazo

1. Commitar o que já está pronto no working tree: `docker-compose.yml` (Oracle), `.github/workflows/backend.yml`.
2. Etapa 5 — validar ambiente local Oracle de ponta a ponta com o `docker-compose.yml` já corrigido.
3. Manter aderência ao fluxo simplificado (`specs/foundation/development-workflow.md`).

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

---

# Pendências / Divergências Registradas

Registradas aqui por escopo desta reconciliação (não corrigidas — correção pertence a cada fonte):

| # | Pendência | Onde corrigir | Severidade |
|---|---|---|---|
| 1 | `docker-compose.yml` já corrigido para Oracle no working tree, mas não commitado (D5) | commit + `docs/governance/03-open-decisions.md` | Baixa |
| 2 | `.github/workflows/backend.yml` (CI, "Opção B") não commitado; "Opção A" (CI com Oracle real) permanece sem decisão (D6/GAP-DEC-003) | `docs/governance/03-open-decisions.md` | Baixa |
| 3 | `PapelAtribuicaoService`/entidades de papel (commit `9306f94`) implementadas sem `feature.yaml`/spec associada nesta reconciliação | classificar contra spec existente (`session`/`authentication`) ou abrir nova feature | Média |
| 4 | `docs/governance/05-roadmap.md` desatualizado desde 2026-07-08 (ainda descreve FT-AUTH como "EM PREPARAÇÃO") — contradiz este documento | `docs/governance/05-roadmap.md` | Média |
| 5 | Três catálogos de decisão com colisão de ID (`docs/governance/03-open-decisions.md`, `docs/architecture/08-decision-records.md`, `docs/technology/04-decision-log.md`) — D7/GAP-DEC-006, não implementada | catálogos de decisão | Baixa (sem bloqueio operacional) |
| 6 | `docs/audit/12-...` e `structural-simplification-plan-w2.md` contêm 2 achados já defasados (D4 storage e D5 docker-compose) — já mapeado em `docs/audit/14-...` | não requer nova auditoria; já registrado | Baixa |
| 7 | FT-PRIMEIRO-ACESSO: `traceability.md` sem AT formal dedicado à conclusão do wizard (RF-PA-011) e sem `tasks.md` — dívidas aceitas no fechamento, não bloqueiam DoD | `specs/features/primeiro-acesso/` — trabalho de `/specify` se e quando priorizado | Baixa |

## Resolvidas em 2026-08-26

| # (checkpoint 2026-08-21) | Pendência | Resolução |
|---|---|---|
| 1 | Fechamento formal de FT-COLABORADOR e FT-PRIMEIRO-ACESSO não confirmado | Ambas confirmadas `DONE` (`feature.yaml`, commits `a7be2ca` e `170f005`). |
| 2 | E2E Playwright de FT-COLABORADOR não localizado | Localizado: `frontend/test/e2e/colaborador/colaborador.spec.ts` (commit `d00358e`), cobre `AT-FE-COLABORADOR-001..005`. |
| 3 | `specs/features/area-colaborador/*` e commits recentes não commitados | Commitados (`fec69f2`…`2a9c953`); feature `DONE`; `tasks.md` v1.1 commitado (`6b54595`). |

---

# Critério de Revalidação

**Validação incremental** (basta checar a feature/SSOT tocada + rodar a camada de validação correspondente, `mvn clean verify` / lint-typecheck-unit conforme `CLAUDE.md`) quando:

* uma única feature avança de fase (spec → DoR → implementação → fechamento) sem alterar decisão arquitetural, de stack ou de schema já aprovada;
* checkpoint de rotina entre sessões — confirmar o que mudou desde o último checkpoint deste documento e atualizar só as linhas afetadas.

**Revalidação completa** (reler SSOTs de todas as camadas, não só a feature em questão) é necessária quando:

* uma decisão arquitetural/tecnológica aprovada muda (stack, banco, storage, mensageria, deploy — catálogos de `docs/governance/03`, `docs/architecture/08`, `docs/technology/04`);
* antes de qualquer novo Exit Gate ou fechamento formal de Etapa do MVP (`docs/backlog/04-mvp-scope.md`);
* uma auditoria estrutural (como `docs/audit/12-...`) aponta divergência ainda não resolvida entre dois SSOTs;
* mais de duas features mudam de status desde o checkpoint anterior sem que este documento tenha sido atualizado nesse meio-tempo (caso deste checkpoint: FT-PRIMEIRO-ACESSO + FT-AREA-COLABORADOR).

---

# Histórico de Atualizações

| Data       | Autor           | Alteração                                              |
| ---------- | --------------- | ------------------------------------------------------ |
| YYYY-MM-DD | Project Manager | Criação inicial do documento                           |
| 2026-06-22 | Reconciliação   | Atualização pós-consolidação MVP                       |
| 2026-07-08 | Governança      | Encerramento oficial da Sprint 0 — baseline congelada  |
| 2026-08-14 | Revalidação     | Etapa 3 SSOT aprovada com ressalvas; Etapa 4 JPA audit |
| 2026-08-21 | Reconciliação (State Index) | Documento reposicionado como índice central; corrigido status de FT-PRIMEIRO-ACESSO (BE implementado, não `not_started`); adicionada FT-AREA-COLABORADOR; registradas 9 pendências; adicionados critérios de revalidação completa vs. incremental |
| 2026-08-21 | Baseline (implantação do modelo) | Adicionadas ao mapa de SSOTs as referências a `docs/governance/10-project-organization.md` (modelo de organização) e a Foundation (aplicação); demais seções já representavam o modelo formalizado — sem outras alterações |
| 2026-08-26 | Reconciliação (State Index) | FT-COLABORADOR e FT-AREA-COLABORADOR confirmadas `DONE` contra evidência de código; E2E de FT-COLABORADOR localizado; pendências #1–#3 do checkpoint 2026-08-21 resolvidas; Marcos M15/M16 concluídos |
| 2026-08-26 | Fechamento de feature | FT-PRIMEIRO-ACESSO confirmada `DONE` (`feature.yaml` migrado do esquema legado, commit `170f005`); nenhuma feature implementada permanece sem checkpoint de fechamento; Marco M17 concluído |
