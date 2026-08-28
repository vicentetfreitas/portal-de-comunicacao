# Organização e Execução do Projeto

| Item | Valor |
|------|-------|
| Artefato | `docs/governance/10-project-organization.md` |
| Status | Approved |
| Categoria documental | **SSOT** |
| Camada dona | Governança |
| Responsável | Project Manager / Arquitetura |
| Data | 2026-08-21 |
| Complementa | `07-documentation-architecture.md`, `specs/foundation/development-workflow.md`, `specs/foundation/minimal-ssot.md` |

## Propósito

Estabelecer **como o projeto se organiza** e **como uma mudança o percorre**, com simplicidade e sem retrabalho.

Este documento não redefine metodologia, não substitui especificações, decisões, regras de negócio, arquitetura alvo, DoR/DoD nem o fluxo diário da Feature. Aponta para os SSOTs existentes.

Não é o State Index. Não é o fluxo SDD detalhado. Não é o mapa de precedência.

---

## Princípios

1. Cada conhecimento tem **um** SSOT. Consultar `minimal-ssot.md` e `07-documentation-architecture.md`.
2. Feature é unidade de **negócio**. Package é unidade de **implementação**. Task é **trabalho executável**.
3. Package **não** é camada documental obrigatória. Task **não** vira documento permanente.
4. Jira acompanha trabalho. Não duplica specification, decisão ou regra.
5. Git registra implementação. CI/CD valida e entrega.
6. Governance registra decisões e regras. Architecture define estrutura técnica.
7. Specification é a fonte do comportamento esperado.
8. Foundation (aplicação) é transversal às features.
9. State Index centraliza o estado do projeto para evitar auditoria completa a cada ciclo.
10. Desenvolvimento, migração e definição de regras de negócio podem avançar **em paralelo**, desde que respeitem os SSOTs e os pontos de decisão humana.
11. Decisão humana permanece explícita em escopo, regra de negócio, trade-off ou avanço relevante.
12. Não criar etapa, documento, gate ou artefato só para representar atividade já controlada por artefato existente.

---

## Camadas

```text
GOVERNANÇA
→ decisões, regras de negócio, princípios e estado

ARQUITETURA
→ estrutura técnica e decisões arquiteturais

FOUNDATION
→ infraestrutura transversal da aplicação
  (shell, router, navegação, design system, sessão, infra técnica)

FEATURE
→ capacidade de negócio

SPECIFICATION
→ o que a feature deve fazer

PLANNING
→ decomposição da feature em unidades executáveis

PACKAGE
→ unidade de implementação

TASK
→ trabalho concreto necessário para executar um package

ENGINEERING
→ padrões de implementação, testes, qualidade, segurança e CI/CD

GIT
→ implementação versionada

CI/CD
→ validação automática, integração e entrega

JIRA/SCRUM
→ acompanhamento e cadência (não é SSOT)

STATE INDEX
→ visão consolidada do estado atual do projeto
```

### Responsabilidade e SSOT (não copiar o conteúdo)

| Camada | Responsabilidade | Onde está a verdade | Não é |
|--------|------------------|---------------------|-------|
| Governança | Decisões, regras, princípios, estado do projeto | `docs/governance/`, `docs/domain/09-business-rules.md` | Implementação |
| Arquitetura | Estrutura técnica e ADRs | `docs/architecture/`, `docs/technology/` | Spec de feature |
| Foundation | Infra transversal da **aplicação** | código em `frontend/` / `backend/` + `docs/implementation/` | `specs/foundation/` (processo SDD — homônimo) |
| Feature | Capacidade de negócio | identidade em `specs/features/<slug>/feature.yaml` | Package ou ticket Jira |
| Specification | Comportamento esperado | `specs/features/<slug>/` | Código ou Jira |
| Planning | Decompor a feature | `specs/features/<slug>/tasks.md` | Estado da feature |
| Package | Incremento implementável | agrupamento em `tasks.md` | pasta `pkg-XX/`, `status.md`, Session, orchestrator v4.1 |
| Task | Trabalho concreto | itens em `tasks.md` | documento permanente; não exige ticket |
| Engineering | Como implementar com qualidade | `docs/implementation/`, testes, `.github/workflows/` | Produto / domínio |
| Git | Registro da implementação | repositório | SSOT de comportamento ou de estado |
| CI/CD | Validar e entregar | `.github/workflows/` (plataforma vigente: GitHub Actions) | SSOT de estado |
| Jira/Scrum | Cadência e acompanhamento | board, quando usado | requisitos, arquitetura, regras |
| State Index | Onde o **projeto** está | `docs/governance/01-project-status.md` | estado da Feature (`feature.yaml`) |

Homônimo: **Foundation (aplicação)** ≠ **`specs/foundation/`** (DoR, workflow, path conventions).

Package neste modelo **não revive** o Engineering Framework v4.1. Ver seção Conflitos.

---

## Relação entre as camadas

```text
Governança / Arquitetura / Foundation (aplicação)
        ↓ autorizam e restringem
Feature
        ↓
Specification  →  Planning (tasks.md)
        ↓                ↓
   Requirements /    Package → Tasks
   Acceptance
        ↓
Engineering → Code (Git) → Tests → CI/CD → Review
        ↓
State (feature.yaml + State Index)
```

Fluxo externo (cadência, sem ser SSOT):

```text
FEATURE → JIRA/SCRUM → PACKAGE → GIT → CI/CD → DONE
```

Jira, se existir, aponta para a Feature/Package. Não copia specification nem decisão.

---

## Ciclo operacional

```text
STATE
  → PLAN
  → DECISÃO          (humana, se houver escopo / regra / trade-off / avanço)
  → IMPLEMENT
  → VALIDATE
  → REVIEW
  → DECISÃO          (humana: merge local, PR, retorno, DONE, mudança de escopo)
  → UPDATE STATE
  → NEXT PLAN
```

Este é o **fluxo principal** e roda **integralmente em localhost** — IMPLEMENT, VALIDATE e REVIEW não dependem de GitHub, PR ou CI externo.

```text
                 (opcional, externo)
DECISÃO  ──┬──►  PR → CI → MERGE  ──►  UPDATE STATE
           └──►  merge local (git)  ─►  UPDATE STATE
```

PR/CI/MERGE no GitHub são o caminho de **publicação e revisão externa** — usados quando existirem colaboradores externos, exigência de auditoria remota ou preferência do responsável. Não são pré-requisito para o ciclo local se completar: branch de Feature + commits (Convenção Git) já são a evidência mínima suficiente. Sem PR, o fechamento local é registrado como `READY_FOR_LOCAL_MERGE` (ver Contrato — REVIEW, campo `PR_READINESS`).

Mapeamento ao fluxo já aprovado (não substitui `development-workflow.md`):

| Passo | Equivalente já existente |
|-------|--------------------------|
| STATE | `Status` + State Index; Feature em `feature.yaml` |
| PLAN | Specify / `tasks.md` (packages + tasks) |
| DECISÃO | DoR, aprovação de spec, DEC, avanço de wave |
| IMPLEMENT | `Implement` após `IMPLEMENTING` |
| VALIDATE | `Validate` — evidência; não altera `feature.yaml` (Contrato — VALIDATE, abaixo) |
| REVIEW | Review de PR (`review-process.md`) — Contrato — REVIEW (Feature), abaixo |
| PR / CI / MERGE | Opcional, externo (GitHub) — Convenção Git; `.github/workflows/`; evidência = commit de merge |
| UPDATE STATE | `feature.yaml` (Feature); State Index (projeto) |
| NEXT PLAN | próximo package, feature ou wave |

Máquina da Feature permanece:

```text
DRAFT → READY_FOR_REVIEW → APPROVED → IMPLEMENTING → DONE
```

---

## Fluxo de uma Feature

```text
necessidade
  → Specification (DRAFT → APPROVED)
  → Planning em tasks.md (packages + tasks)
  → DoR-Implementation → IMPLEMENTING
  → executar packages
  → Validate + Review de PR
  → DoD → DONE
  → atualizar State Index se o marco for de projeto
```

Detalhe cotidiano, Gates, DoR e DoD: `specs/foundation/development-workflow.md`, `definition-of-ready.md`, `definition-of-done.md`.

---

## Fluxo de um Package

```text
Package (incremento da Feature)
  → Tasks em tasks.md
  → código + testes (Git)
  → Validate da camada alterada
  → Review de PR quando o incremento for entregue
  → evidência em Git/CI — sem status.md por package
```

Um package fecha quando o incremento é observável (código + teste acordado na spec). Não exige artefato `pkg-XX/`. Features históricas podem ainda ter rótulos PKG-FE-NN em `tasks.md`; isso é nomenclatura de agrupamento, não cerimônia v4.1.

---

## Convenção Git

Conecta os identificadores já existentes (`FT-*`, `PKG-*`, `TK-*`) ao Git sem criar identificador novo. Vale **a partir de agora**; não reescreve histórico existente.

```text
Feature (feature.yaml)
  → Package/Task (tasks.md — TK-*, sem branch própria)
    → Branch: feature/<ft-slug>
      → Commit: Conventional Commits, scope = <ft-slug>
        → corpo: Refs: FT-<CODE> [TK-<ID>] quando aplicável
          → PR (opcional, externo): um por Feature — evidência de Gate 3 quando publicado
            → CI: workflows atuais, inalterados
              → State: feature.yaml (Gate 3/DoD) + State Index quando o marco for de projeto
```

| Elemento | Regra |
|----------|-------|
| Branch | `feature/<ft-slug>` — uma por **Feature**, não por Package/Task. **Obrigatória**, mesmo sem PR. |
| Commit | Conventional Commits; `scope` = slug da Feature (`specs/features/<slug>/`), nunca o `FT-CODE` nem um domínio genérico. **Obrigatório**. |
| Rastreabilidade | Corpo do commit: `Refs: FT-<CODE> TK-<ID>` quando houver task associada; `Refs: FT-<CODE>` quando for nível de Feature/estado |
| PR | **Opcional, externo.** Quando existir GitHub/PR: um por Feature (não por Task/Package); título `FT-<CODE>: <nome da Feature>`; corpo referencia as `TK-*` entregues; é a evidência de Gate 3. Quando não existir: `REVIEW` local + merge local (Git) já satisfazem o ciclo — ver Contrato — REVIEW, `READY_FOR_LOCAL_MERGE`. |
| Histórico | Commits anteriores a esta convenção não são alterados |
| Jira | Permanece opcional; convenção funciona sem Jira. Nem Jira nem GitHub são SSOT — SSOT de estado continua em `feature.yaml`. |
| CI/CD | Sem alteração nesta etapa — `push`/`pull_request` já cobrem branch e PR quando usados |

---

## Contrato — VALIDATE

Formaliza o estágio `VALIDATE` do Ciclo operacional. Não é gate humano (esse é `REVIEW` / `DECISÃO`); é checagem de evidência antes deles.

**Entrada:** implementação (código + testes) + plano (`tasks.md`) + decisões já tomadas + especificação/AT (`specification.md`, `acceptance-tests.md`).

**Verificar:**

- alinhamento com o plano aprovado (`tasks.md`);
- critérios de aceite (`acceptance-tests.md` — AT-*);
- evidências de testes (resultado real de execução, não inspeção de código);
- typecheck/lint/build quando aplicável à camada alterada;
- Git (branch, commit, escopo — conforme "Convenção Git");
- complexidade arquitetural (camadas/serviços/artefatos além do necessário);
- alterações desnecessárias (fora do escopo do plano).

**Restrições — VALIDATE não:**

- altera código;
- altera specs;
- altera estado (`feature.yaml`, State Index);
- cria artefato;
- corrige o que encontra;
- declara a Task `DONE` automaticamente.

**Saída obrigatória:**

```text
STATUS:
SPEC_ALIGNMENT:
ACCEPTANCE:
TEST_EVIDENCE:
GIT:
ARCHITECTURAL_COMPLEXITY:
ISSUES:
RECOMMENDATION:
FILES_CHANGED:
```

`STATUS: PASS` significa **evidência suficiente para seguir para Review/decisão** — não significa Task concluída. Conclusão de Task/Feature permanece em `REVIEW` + `DECISÃO` (Gate 3, DoD), conforme `definition-of-done.md`.

---

## Contrato — REVIEW (Feature)

Formaliza, ao nível de **Feature**, o comando `Review` (modo Review de PR — `agent-commands.md` §3.5, `review-process.md`) já existente. Não cria comando novo; dá contrato operacional ao que já era nomeado.

Roda **depois** das Tasks da Feature terem passado por `VALIDATE` individualmente. Não é gate humano (esse é `DECISÃO`); é a consolidação da evidência antes dele.

**Entrada:** Feature em execução + tasks concluídas + evidências dos `VALIDATE`s de cada task + `specification.md` + `acceptance-tests.md` + `traceability.md` + `feature.yaml` + decisões relevantes já tomadas no ciclo.

**Verificar:**

- escopo da Feature × tasks concluídas;
- Acceptance Tests (`AT-*`) × evidências existentes;
- requisitos funcionais (`RF-*`) × implementação;
- pendências e ressalvas conhecidas;
- coerência do estado da Feature (`feature.yaml` × evidência real);
- prontidão para fechamento — PR externo (se usado) ou merge local;
- aderência à Convenção Git.

**Restrições — REVIEW não:**

- repete testes já executados nos `VALIDATE`s das tasks;
- refaz `VALIDATE` de task;
- altera código;
- corrige o que encontra;
- altera `feature.yaml` automaticamente;
- abre PR automaticamente;
- declara a Feature `DONE` automaticamente;
- cria documento ou artefato novo.

**Continuidade de evidência** (não reexecutar o que já existe):

```text
EVIDÊNCIA PRODUZIDA   → REUTILIZAR
EVIDÊNCIA AUSENTE     → PRODUZIR
EVIDÊNCIA DIVERGENTE  → INVESTIGAR
EVIDÊNCIA SUFICIENTE  → AVANÇAR
```

**Saída obrigatória:**

```text
STATUS:
SCOPE:
ACCEPTANCE:
EVIDENCE:
PENDING:
FEATURE_STATE:
PR_READINESS:
RECOMMENDATION:
FILES_CHANGED:
```

`PR_READINESS` usa o vocabulário já existente — `READY` / `NOT_READY` — sem novo status:

- **Sem GitHub/PR em uso:** `READY` equivale a `READY_FOR_LOCAL_MERGE` — a Feature pode ser mergeada localmente (`git merge` da branch de Feature), sem depender de PR/CI externo.
- **Com GitHub/PR em uso:** `READY` significa pronta para abrir o PR único da Feature.

Regras:

1. `PR_READINESS: READY` (local ou via PR) **não** significa que o merge/PR deve ocorrer automaticamente — é insumo para a `DECISÃO` humana.
2. Pendência bloqueante → `PR_READINESS: NOT_READY`.
3. Pendências não bloqueantes são listadas em `PENDING`, não escondidas.
4. Feature pronta → o agente **recomenda** (não executa) o próximo passo: merge local ou PR único da Feature, conforme a Convenção Git e a preferência já registrada para o ciclo.
5. REVIEW não substitui a decisão humana nem o Gate 3/DoD (`definition-of-done.md`) — Gate 3 é satisfeito pela Review de PR **ou**, sem GitHub, pela REVIEW de Feature local equivalente.

### PENDING → Task

Todo PENDING listado pela REVIEW é classificado, dentro do próprio `RECOMMENDATION`, em uma das três categorias:

1. **NECESSÁRIO_PARA_CONCLUIR** — pertence ao escopo/Objetivo já declarado da Feature (RF/UC já existentes); sua ausência impede a Feature de entregar o que a spec já promete. REVIEW **propõe** uma Task (rascunho: Objetivo, RF/UC/AT reaproveitados — nunca inventados —, Dependências, Componentes Esperados). Não grava em `tasks.md`.
2. **GAP_CONHECIDO** — fora do escopo já declarado. Permanece em `PENDING`, sem Task.
3. **NOVA_NECESSIDADE** — exigiria RF/escopo que a spec atual não cobre. Não vira Task da Feature — encaminhar para `Specify` (evolução de spec), mecanismo já existente.

Achado de UI/navegação com evidência Figma associada (citação em `specification.md`, `AUDITORIA-DS-FIGMA-01.md`, ou integração Figma disponível no Claude Code): a proposta reaproveita essa evidência antes de propor (EVIDÊNCIA AUSENTE → PRODUZIR, inclusive via integração Figma, quando necessário).

**Aprovação:** uma Task proposta só é gravada em `tasks.md` — SSOT único, mesmo template `TK-*` já usado — mediante aprovação explícita do usuário. Sem aprovação, o PENDING permanece PENDING: nenhum artefato paralelo (`pending.md`, registry, backlog) guarda a proposta entre execuções — a evidência é o próprio output da REVIEW mais recente.

---

## State Index

Ponto de entrada para **onde o projeto está** (fase, wave, o que está em andamento/bloqueado, próxima etapa, evidência, quando revalidar).

Arquivo: `docs/governance/01-project-status.md`.

Consulta futura:

```text
CONSULTAR STATE INDEX
        ↓
Verificar mudanças desde o último checkpoint (Git)
        ↓
    NÃO ──────► continuar
        │
       SIM
        ↓
Analisar só a área afetada → atualizar State Index → continuar
```

Revalidação completa só com gatilho: mudança de arquitetura alvo, nova wave, alteração estrutural relevante, mudança de domínio/DEC, divergência não resolvida, alteração significativa no legado, mudança de escopo.

Estado da **Feature** continua em `feature.yaml`. O State Index não substitui isso.

---

## Trabalho em paralelo

```text
Regras / DEC          ─┐
Arquitetura / Foundation─┤  podem avançar juntos
Feature (spec → code)  ─┤  sem esperar inventário geral
Migração (ondas)       ─┘
```

Sincronizam nos pontos de decisão humana e no State Index. Não sincronizam copiando conteúdo entre artefatos.

---

## Pontos de decisão humana

Obrigatórios quando houver:

- escopo da Feature ou do MVP;
- regra de negócio nova ou alteração de regra vigente;
- trade-off arquitetural ou DEC;
- aprovação de spec (`APPROVED`) ou autorização `IMPLEMENTING`;
- encerramento `DONE`;
- avanço de wave / atualização do State Index;
- divergência spec × implementação (qual origem corrigir).

Fora desses pontos, execução segue spec + `tasks.md` + Engineering.

---

## Como evitar retrabalho e duplicação

- Começar pelo State Index; não reabrir inventário completo.
- Não copiar regra, ADR, spec ou AT para outro documento.
- Não criar package documental, task permanente, gate extra ou status paralelo.
- Não tratar Jira, Git, CI, `tasks.md` ou `construction/registry.yaml` como SSOT de estado.
- Package = agrupamento em `tasks.md`. Task = linha de trabalho. Evidência = Git/CI/PR.

---

## Conflitos com decisões já registradas

Não resolvidos por este documento. Permanecem explícitos:

| # | Tema | Já decidido | Este modelo | Tratamento |
|---|------|-------------|-------------|------------|
| 1 | Plataforma de CI/CD | D1 + `docs/technology/01-technology-stack.md`: **GitHub / GitHub Actions**. GitLab é portabilidade futura. | Camada chamada “GitLab CI/CD” na sessão de origem | A **função** da camada é CI/CD. A **plataforma vigente** permanece GitHub Actions. Trocar para GitLab exige nova decisão humana. |
| 2 | Package × PKG v4.1 | Etapa 2: cerimônia PKG/Session/Registry é Archive; plano = `tasks.md`; sem `pkg-XX/status.md` como estado | Package como unidade de implementação | Package = incremento agrupado em `tasks.md`. **Não** restaura orchestrator, Session, Snapshot, Cache nem `pkg-XX/status.md`. |
| 3 | Jira | D9: mapeamento Jira ↔ `feature.yaml` adiado; Jira “posteriormente” | Jira/Scrum como camada de acompanhamento | **D9 ativado em 2026-08-28** — ver `11-jira-integration.md` (modelo canônico Epic↔`EPIC-0XX` / Story↔`FEATURE-0XX`, mapa de status, sincronização). Jira segue não sendo SSOT nem obrigatório para executar Feature. |
| 4 | `construction/` em `07-documentation-architecture.md` | Ainda listado como camada de execução | Execução = Package/Task/Git/CI | Transição Etapa 2 (D8). Este documento não reabre o timing do arquivamento físico. |
| 5 | Gate 3 / DoD (`definition-of-done.md`) fala em "Review de PR" | Redigido supondo GitHub sempre disponível | Review de PR **ou**, sem GitHub, REVIEW de Feature local equivalente (Contrato — REVIEW) — mesma verificação, publicação diferente | Gate 3 continua obrigatório para `DONE`; o que muda é o mecanismo de publicação, não o requisito de revisão. GitHub e Jira permanecem não-SSOT. |

---

## Referências (não duplicar)

- Estado do projeto — `docs/governance/01-project-status.md`
- Fluxo diário da Feature — `specs/foundation/development-workflow.md`
- Precedência — `specs/foundation/minimal-ssot.md`
- Arquitetura documental — `docs/governance/07-documentation-architecture.md`
- Simplificação Etapa 2 — `docs/governance/09-framework-simplification-scope.md`
- DoR / DoD / comandos — `specs/foundation/definition-of-ready.md`, `definition-of-done.md`, `agent-commands.md`
- Ondas de migração — `docs/solution-design/10-delivery-roadmap.md`
- Stack / CI vigente — `docs/technology/01-technology-stack.md`
