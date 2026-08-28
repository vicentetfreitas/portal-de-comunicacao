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
| Última Atualização     | 2026-08-28                                 |
| Responsável            | Project Manager / Arquitetura              |
| Próxima Revisão        | Etapa 5 — infraestrutura local (Oracle)    |

---

# Estado Atual (leitura rápida)

> Seção de leitura mínima. Para a maioria das perguntas de estado/próximo passo, **basta esta seção**.
> As demais seções abaixo (Status por Camada, Marcos, Indicadores, Critério de Revalidação) são
> referência sob demanda. Narrativas dos checkpoints históricos: `docs/governance/history/16-state-index-checkpoints.md`.

**Etapa:** MVP Etapa 2 avançada + bloco **Gestão Documental** em andamento. Baseline documental
APROVADA COM RESSALVAS (Exit Gate 2026-07-24). Nenhum impedimento ativo.

**Repositórios (DEC-015):** deixou de ser monorepo. `backend/` → `portal-comunicacao-api`,
`frontend/` → `portal-comunicacao-app`, CMS → `portal-comunicacao-cms` (scaffold). `specs/`,
`docs/`, `construction/`, `database/` **permanecem neste monorepo** (SSOT compartilhada).
Pontos abertos: onde a SSOT compartilhada vive em definitivo (#2), hospedagem/tema CMS (#5).

## Features — status (SSOT de estado: `specs/features/<slug>/feature.yaml`)

| Feature | slug | Status | Nota |
|---|---|---|---|
| FT-AUTH / FT-AREA / FT-SINGULAR / FT-EQUIPE / FT-SESSION | — | closed | Etapa 2 org + auth/sessão |
| FT-COLABORADOR | `colaborador` | DONE | BE+FE+E2E (`AT-FE-COLABORADOR-001..005`) |
| FT-PRIMEIRO-ACESSO | `primeiro-acesso` | DONE | 2 dívidas documentais aceitas (sem AT do wizard, sem `tasks.md`) |
| FT-AREA-COLABORADOR | `area-colaborador` | DONE | TK-004 superseded (nav "Federação" cobre) |
| FT-FEDERACAO-COLABORADOR | `federacao-colaborador` | DONE | escrita retroativamente |
| FT-DOCUMENTO (leitura) | `arquivos` | DONE | — |
| FT-DOCUMENTO-UPLOAD | `documento-upload` | DONE | 2026-08-28. Gate 3 + Gate 6 PASS. Grants `PERMISSAO_PASTA` de produção = dado institucional (DBA); homologação validada via `V011`. Dívida aceita: sem E2E Playwright |
| FT-DOCUMENTO-GESTAO | `documento-gestao` | DONE | 2026-08-28. `mvn verify` 399/0/2; Gate 3 + Gate 6 PASS. MinIO provisionado (`docker-compose.yml` no repo `api`), caminho real validado end-to-end. Dívida aceita: sem E2E Playwright |
| FT-DOCUMENTO-NAVEGACAO | `documento-navegacao` | IMPLEMENTING | 2026-08-28. DoR-Implementation PASS. TK-DOC-NAV-001 (BE `PastaResponse` +2 campos) + 002/003/004 (FE). 5 ressalvas do Review de Spec carregam para o `/implement` |
| FT-HOME / FT-NOTICIA / FT-PERFIL / FT-SERVICOS | — | DRAFT | inertes; `/app` Home é spike sancionado sem DoR |

## Próximas ações (ordem)

1. **FT-DOCUMENTO-NAVEGACAO:** `/implement` TK-DOC-NAV-001 (BE) → 002/003/004 (FE); fechar as 5 ressalvas do Review de Spec no caminho.
2. Abrir MR `development` → `stage`/`main` em `portal-comunicacao-api` / `-app` quando quiser promover o bloco Gestão Documental (FT-DOCUMENTO / UPLOAD / GESTAO fechadas).
3. Provisionar grants `PERMISSAO_PASTA` `EDICAO` institucionais nas pastas de produção (DBA) — não bloqueia features, é dado operacional.
4. Commitar working tree pendente do monorepo: `docker-compose.yml` (Oracle), `.github/workflows/backend.yml`.
5. Etapa 5 — validar ambiente local Oracle ponta a ponta.

## Pendências abertas (detalhe na seção "Pendências / Divergências Registradas")

`#4` roadmap desatualizado · `#5` colisão de ID entre 3 catálogos de decisão ·
`#8` execução da migração de repositórios (proteção de branch GitLab manual pendente) ·
`#9` `11-platform-decomposition.md` cita stack errada (GAP-DEC-004) · `#10` convenção de commit cross-cutting ·
`#3` `PapelAtribuicaoService` sem `feature.yaml` associada.
Baixa severidade / registro apenas: `#1`, `#2`, `#6`, `#7`.

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
| Separação em repositórios independentes (backend/frontend/CMS) | `docs/technology/04-decision-log.md` — **DEC-015** (Approved, 2026-08-26; supersede DEC-010 Monorepo) |
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

> Estado corrente e próximas ações: seção **`# Estado Atual (leitura rápida)`** acima.
> Esta seção guarda o contexto histórico e os ponteiros de SSOT.

## Contexto

Sprint 0 do backend concluída (2026-07-08); features de Etapa 2 (org + auth/sessão) e revalidação
SSOT Etapa 3 (2026-08-14). FT-COLABORADOR, FT-PRIMEIRO-ACESSO e FT-AREA-COLABORADOR encerradas
`DONE` em 2026-08-26 (ver `docs/governance/history/16-state-index-checkpoints.md`). Desde 2026-08-27,
foco no bloco Gestão Documental (FT-DOCUMENTO*).

* SSOT operacional: `specs/foundation/minimal-ssot.md`
* SSOT normativo documental: `docs/governance/07-documentation-architecture.md` v2.0
* Estado de implementação: evidência direta em código/testes do repositório — **não** `construction/registry.yaml`
* Estado de cada Feature: `specs/features/<slug>/feature.yaml` (ver tabela na leitura rápida)

**Separação em repositórios (DEC-015, 2026-08-26):** resumo na leitura rápida; registro completo
em `docs/technology/04-decision-log.md` § DEC-015. Pontos em aberto: onde a SSOT compartilhada
vive em definitivo (#2), hospedagem/tema CMS (#5), proteção de branch GitLab (manual).

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
| M18   | **Bloco Gestão Documental fechado** — FT-DOCUMENTO (leitura), FT-DOCUMENTO-UPLOAD e FT-DOCUMENTO-GESTAO todos `DONE` (Gate 3 + Gate 6); MinIO provisionado e caminho de storage validado end-to-end | 2026-08-28 |

---

## Em Andamento

| Marco | Descrição                              | Status          |
| ----- | -------------------------------------- | --------------- |
| M19   | FT-DOCUMENTO-NAVEGACAO — DoR-Spec / readiness | Planejado |
| M20   | Etapa 5 — infraestrutura local Oracle  | Planejado       |

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

Lista corrente e ordenada: seção **`# Estado Atual (leitura rápida)` → Próximas ações (ordem)**.

---

# Checkpoints de Reconciliação

As narrativas dos checkpoints (evidência consultada + correção aplicada) foram movidas para
`docs/governance/history/16-state-index-checkpoints.md` em 2026-08-28 para manter este índice
de leitura mínima. Último checkpoint: **2026-08-28** (redução do índice; Pendência #11 atendida).
Anteriores: 2026-08-26 (FT-COLABORADOR / FT-AREA-COLABORADOR `DONE`), 2026-08-21 (FT-PRIMEIRO-ACESSO BE, FT-AREA-COLABORADOR).

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
| 8 | DEC-015 (separação em repositórios) aprovada 2026-08-26. Ponto em Aberto 6 (CI por repositório) **resolvido** para backend/frontend. **2026-08-27:** `portal-comunicacao-api` e `portal-comunicacao-app` **ressincronizados via `git subtree split`** (preservando histórico; substitui o snapshot squash, que precedia o módulo `documento` e ~1 semana de trabalho); `development`/`stage` de ambos recriadas e force-pushed. `api` compila standalone (`./mvnw clean test-compile` OK). Ver `docs/technology/04-decision-log.md` § DEC-015 → Progresso da Execução. Seguem abertos: #2 (onde vivem `specs/`/`docs/`/`construction/`/`database/` pós-divisão — commits de spec/governança continuam só no monorepo), #5 (hospedagem/tema do CMS — CI ali é só placeholder), resync do `portal-comunicacao-cms` quando houver código. Proteção de branch no GitLab (`stage`/`main`) não configurada — fora do alcance de CLI, ação manual pendente do usuário na UI do GitLab | `docs/technology/04-decision-log.md` § DEC-015 | Média — bloqueia execução completa da migração de repositórios, não bloqueia trabalho de feature |
| 9 | `docs/solution-design/11-platform-decomposition.md` descreve Frontend em **Next.js** e banco **PostgreSQL** — contradiz a stack aprovada (Vue 3/Quasar, DEC-007 Oracle) e o `docker-compose.yml` atual (só Oracle, sem Postgres). Mesmo padrão do documento já arquivado em `docs/governance/history/11-target-repository-structure.md`. Já registrado como GAP-DEC-004 (`docs/audit/13-decision-inventory.md`), ainda **Aberto** | `docs/solution-design/11-platform-decomposition.md` — reclassificar (Archive) ou corrigir | Média |
| 10 | `docs/governance/10-project-organization.md` § Convenção Git exige `scope` de commit = slug de Feature e proíbe domínio genérico, mas não prevê trabalho cross-cutting sem Feature associada (governança/infra, ex.: DEC-015, branches/CI dos repositórios divididos). Commit `e5b9ce6` (`docs(governance): ...`) usa escopo genérico por essa lacuna — decisão do usuário: manter como está, registrar como gap em vez de corrigir | `docs/governance/10-project-organization.md` § Convenção Git — definir convenção para commits não ligados a Feature (branch/scope dedicados?) | Baixa |
| 11 | **Parcialmente atendida (2026-08-28).** Bloco Gestão Documental agora refletido em `# Estado Atual (leitura rápida)` e na tabela de Features (FT-DOCUMENTO `DONE`; FT-DOCUMENTO-UPLOAD `IMPLEMENTING` code-complete, pendências de homologação; FT-DOCUMENTO-GESTAO `IMPLEMENTING` 4/4 tasks; FT-DOCUMENTO-NAVEGACAO `DRAFT` D-01..D-07 fechadas). Reconciliação feita com evidência **local** deste monorepo (`feature.yaml`, `tasks.md`, `git log`). **2026-08-28:** commits pushados para `origin/development`; **bloco Gestão Documental fechado** — FT-DOCUMENTO, FT-DOCUMENTO-UPLOAD e FT-DOCUMENTO-GESTAO todos `DONE` (Gate 3 + Gate 6; `mvn verify` 399/0/2; MinIO provisionado e caminho real validado end-to-end). **Segue em aberto:** `docs/jira-reconciliation-audit.md` (commit `c69e46c`) não executado; DEC-CMS-002 não integrada às seções de decisão deste índice; checkpoint completo quando priorizado. | checkpoint completo quando priorizado | Baixa |

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
| 2026-08-28 | Redução do índice | Adicionada seção `# Estado Atual (leitura rápida)` no topo; narrativas dos checkpoints 2026-08-21/26 movidas para `docs/governance/history/16-state-index-checkpoints.md`; Pendência #11 atendida (bloco Gestão Documental refletido na tabela de Features) |
| 2026-08-28 | Fechamento de feature | FT-DOCUMENTO-GESTAO e FT-DOCUMENTO-UPLOAD `IMPLEMENTING` → `DONE` (Gate 3 + Gate 6 PASS; `mvn verify` 399/0/2; MinIO provisionado e caminho real validado). Bloco Gestão Documental fechado (M18). Próxima: FT-DOCUMENTO-NAVEGACAO `/readiness` |
