# Engineering Framework

| Item | Valor |
|------|-------|
| Nome | **Engineering Framework** |
| Version | **v4.1** |
| Baseline | **v4.0** |
| Status | **Stable** |
| Projeto | Portal de Comunicação |
| Camada | `construction/` |
| Última atualização | 2026-07-16 |
| Histórico de versões | `CHANGELOG.md` |

---

# Objetivo

Estabelecer a governança oficial da construção do Portal de Comunicação — desde a **Platform Foundation** até **Features de negócio** — com workflow orientado à Feature, rastreabilidade e critérios de encerramento.

O framework foi **estabilizado** na v3.2 após **FEATURE_APPROVED** da **FT-AUTH**, evoluiu para **v4.0 Full Stack Orchestrator** e recebeu refinamento **v4.1** (Capabilities, Execution Strategy) com **FT-SINGULAR** como Golden Template.

---

# Full Stack Orchestrator (v4.1)

A partir da v4.0, o framework opera como **orquestrador Full Stack**. A v4.1 adiciona organização opcional por **Capabilities** e simplifica o modelo operacional:

| Conceito | Descrição |
|----------|-----------|
| **Feature** | Unidade de construção (`FT-<DOMAIN>`) — **único ponto de entrada do usuário** |
| **Capability** *(v4.1, opcional)* | Agrupador funcional — Tasks, PKGs, Acceptance |
| **Workstream** | Camada executável (backend, frontend, cms, mobile…) |
| **Execution Strategy** *(v4.1)* | `sequential` (padrão) ou `parallel` (reservado) |
| **Estado agregado** | Computado a partir dos Workstreams (STATE-AGG-01) |
| **Registry unificado** | `construction/registry.yaml` — inclui `current_capability` |
| **Comandos** | `Execute`, `Continue`, `Review`, `Close FT-<FEATURE>` |

Documentação: `construction/12-fullstack-orchestrator.md`  
Golden Template: `construction/golden-template/FT-SINGULAR.md`  
Decisões: `construction/13-framework-decisions-v4.md`, `construction/14-framework-decisions-v4.1.md`

---

# Escopo

## Inclui

- Governança da construção da Platform Foundation
- Definição dos módulos de infraestrutura compartilhada
- Backlog técnico rastreável por módulo
- Ordem oficial de construção e dependências
- Critérios de revisão, readiness e auditoria
- Acompanhamento de progresso da Sprint 1A

## Não inclui

- Regras de negócio ou domínio
- Implementação de Features (ex.: FT-AUTH)
- Alteração de arquitetura ou especificações
- Substituição da documentação da Sprint 0 em `docs/`

---

# Responsabilidades

| Papel | Responsabilidade |
|-------|------------------|
| Arquiteto de Software | Definir ordem, pacotes, dependências e critérios de qualidade |
| Tech Lead Backend | Executar e revisar implementação dos módulos |
| Desenvolvedor Backend | Implementar tarefas conforme backlog técnico |
| QA | Validar critérios de aceite e cobertura de testes |
| Governança | Auditar aderência entre documentação, código e arquitetura |

---

# Relação com as Demais Camadas

```text
docs/ (engenharia — consultivo)
        ↓
specs/ (especificação — fonte da verdade)
        ↓
construction/ (governança da construção — Sprint 1A)
        ↓
engineering/ (governança da integração — Sprint de Integração)
        ↓
backend/ (implementação)
```

| Camada | Relação |
|--------|---------|
| `specs/foundation/` | Princípios, DoR, DoD e convenções aplicáveis |
| `specs/features/FEATURE_BASELINE.md` | Padrão de rastreabilidade para Features consumidoras |
| `specs/features/authentication/` | Primeira consumidora após Platform Foundation |
| `specs/architecture/` | Decisões arquiteturais que a fundação deve preservar |
| `docs/implementation/` | Padrões técnicos de implementação |
| `docs/construction/` | Guias operacionais de construção (Sprint 0) |
| `docs/governance/` | Roadmap, riscos e decisões do projeto |
| **`engineering/integration/`** | **Sprint de Integração — validação backend integrado** |

---

# Estrutura da Camada

```text
construction/

├── README.md                      — Este documento (Engineering Framework v4.0)
├── CHANGELOG.md                   — Histórico de versões do framework
├── 01-platform-foundation.md      — Visão e definição da Platform Foundation
├── 02-construction-roadmap.md     — Roadmap da Sprint 1A
├── 03-construction-packages.md    — Pacotes de construção
├── 04-construction-rules.md       — Regras de governança da construção
├── 05-readiness-review.md         — Checklist de prontidão
├── 06-development-order.md        — Ordem incremental de desenvolvimento
├── 07-open-decisions.md           — Decisões pendentes da construção
├── 08-open-risks.md               — Riscos da construção
├── 09-progress.md                 — Acompanhamento (atualizado no Encerramento da Feature)
├── 11-feature-execution-workflow.md — **SSOT workflow por Workstream (v3.2)**
├── 12-fullstack-orchestrator.md   — **SSOT orquestração Full Stack (v4.1)**
├── 13-framework-decisions-v4.md   — Decisões arquiteturais v4.0
├── 14-framework-decisions-v4.1.md — Decisões arquiteturais v4.1
├── 16-frontend-validation-gates.md — **SSOT gates PKG vs E2E (BUILD-02 / E2E-01)**
├── 17-frontend-e2e-behavior-policy.md — **SSOT política specs Playwright (E2E-02)**
├── registry.yaml                  — **Registry unificado Features + Workstreams (v4.1)**
├── golden-template/               — Golden Template Full Stack (FT-SINGULAR)
├── templates/
│   ├── feature-manifest.yaml      — SSOD v3.2 (legado)
│   ├── feature-manifest-v4.yaml   — SSOD v4.0 Full Stack
│   ├── workstream-state.yaml      — SSOT Workstream v4.0
│   ├── execution-plan.md          — Template plano de execução
│   ├── feature-session.md         — Snapshot congelado (SESSION-01)
│   ├── pkg-status.md
│   ├── pkg-validation-summary.md   ← VAL-01 + VAL-02
│   ├── pkg-artifact-model.md       ← ART-01 modelo mínimo por PKG
│   ├── pkg-evidence-run-frontend.sh
│   ├── pkg-evidence-run-backend.sh
│   └── feature-closure-report.md
├── history/
│   ├── README.md                  — Histórico da camada Construction
│   └── sprint-01-retrospective.md — Retrospectiva Sprint 1 (FT-AUTH + PF)
├── platform-foundation/           — Sprint 1A (encerrada — histórico)
│   ├── construction-state.yaml    — SSOT fundação (phase: closed)
│   ├── session.md
│   ├── pkg-01/ … pkg-08/
│   ├── configuration/ … testing/  — Módulos PF
│   └── …
├── features/                      — Workstream backend das Features
│   ├── README.md                  — Convenção FT-<DOMAIN>
│   ├── registry.yaml              — Alias legado → registry.yaml
│   └── FT-AUTH/                   — …
├── frontend/                      — Frontend foundation + Workstream frontend
│   ├── registry.yaml              — Alias legado → registry.yaml
│       ├── feature-manifest.yaml  — SSOD
│       ├── construction-state.yaml
│       ├── execution-plan.md
│       ├── review/
│       └── reports/
├── authentication/                — Redirecionamento → features/FT-AUTH/
└── review/                        — Relatórios Sprint 1A (histórico — não mover)
    ├── construction-audit.md
    ├── readiness-checklist.md
    ├── completion-report.md
    └── reconciliation-report.md

engineering/                       — Sprint de Integração (v1.0 Stable)
├── README.md
├── CHANGELOG.md
├── templates/
└── integration/
    ├── 01-integration-sprint-workflow.md  — SSOT integração
    ├── registry.yaml
    └── sprints/<SPRINT_ID>/
```

---

# Evidências de Build e Auditoria

Logs de build, relatórios de testes e demais artefatos de runtime são gravados exclusivamente em `backend/runtime/`:

| Tipo | Destino |
|------|---------|
| Logs | `backend/runtime/logs/` |
| Relatórios (Surefire) | `backend/runtime/reports/surefire/` |
| Dumps | `backend/runtime/dumps/` |
| Cobertura | `backend/runtime/coverage/` |

Convenção oficial: `docs/construction/backend/01-project-bootstrap.md` § Artefatos de Runtime.

---

# Platform Foundation

A Platform Foundation é composta por sete módulos de infraestrutura reutilizável:

| Módulo | Prefixo de Tarefas | Pacote Backend |
|--------|-------------------|----------------|
| Configuration | PF-CONF | `configuration/` |
| Persistence | PF-PERS | `infrastructure/persistence/` |
| Security | PF-SEC | `infrastructure/security/` |
| Integration | PF-INT | `infrastructure/integration/` |
| Web | PF-WEB | `interfaces/rest/` |
| Observability | PF-OBS | `infrastructure/observability/` |
| Testing | PF-TEST | `src/test/` (infraestrutura) |

Detalhamento em `01-platform-foundation.md` e em cada subdiretório de `platform-foundation/`.

---

# Ordem Oficial de Construção

```text
Execute FT-<FEATURE>           ← v4.1 — Orchestrator resolve internamente
Continue FT-<FEATURE>          ← v4.1 — retomar execução em andamento
Review FT-<FEATURE>            ← v4.1 — review do Workstream/Feature
Close FT-<FEATURE>             ← v4.1 — encerramento consolidado
Execute Feature <FEATURE_CODE> ← v3.2 legado — Sessão do Workstream ativo
Execute PKG-XX [FT-<FEATURE>]  ← diagnóstico / usuário avançado
Encerrar Feature <FEATURE_CODE>← alias de Close FT-<FEATURE>
```

Detalhamento em `12-fullstack-orchestrator.md` (v4.1) e `11-feature-execution-workflow.md` (v3.2 por Workstream).

## Regras de Governança do Framework (RULE-01 a RULE-04)

| ID | Regra |
|----|-------|
| **RULE-01** | Nenhuma Feature inicia sem **Specification Approved** |
| **RULE-02** | O Engineering Framework permanece **congelado** durante a execução da Feature. Evoluções somente entre Features ou Sprints |
| **RULE-03** | Nenhuma implementação inicia enquanto existir **divergência entre Specification e API Contract** |
| **RULE-04** | Toda Feature com status **FEATURE_APPROVED** deve gerar uma **Sprint Retrospective** antes do início da próxima Feature. A retrospectiva registra aprendizados, decisões permanentes e oportunidades de melhoria. Alterações no Engineering Framework somente podem ser **planejadas** a partir dessas evidências e **executadas fora** da implementação de uma Feature em andamento |

Detalhamento em `04-construction-rules.md` e `11-feature-execution-workflow.md`.

## Regras do Workflow v3.2

| ID | Regra |
|----|-------|
| SSOD-01 | Consultar `feature-manifest.yaml` antes de qualquer artefato — sem paths hardcoded |
| STATE-00 | Dois níveis: `platform-foundation/` (histórico) e `features/<FEATURE_CODE>/` (negócio) |
| STATE-01 | Construction State é SSOT do estado operacional |
| STATE-02 | Consultar `construction-state.yaml` antes de inferir progresso |
| STATE-03 | Manifest descreve estrutura — nunca execução |
| STATE-04 | Session descreve contexto — nunca progresso |
| STATE-05 | PKG Status é histórico local — nunca estado global |
| STATE-06 | Review/Audit/Closure refletem resultado final |
| SESSION-01 | Session imutável — somente `Execute Feature` cria/recria |
| CACHE-01 | Não reler documento presente no Snapshot |
| CACHE-02 | Reutilizar Session salvo evento de invalidação |
| BUILD-01 | `mvn clean verify` somente no Encerramento |
| BUILD-02 | Gate PKG frontend (PKG-FE-01..05) — sem E2E — `16-frontend-validation-gates.md` |
| E2E-01 | `yarn test:e2e` obrigatório no PKG-FE-06 (closure) |
| E2E-02 | Política de comportamento dos specs — `17-frontend-e2e-behavior-policy.md` |
| VAL-01 | **VALIDATION SUMMARY** em `pkg-XX/status.md`; log em `evidence/` |
| ART-01 | Modelo mínimo por PKG — `status.md` + `evidence/*.log` opcional |
| PARALLEL-01 | PKG altera `construction-state.yaml` + `pkg-XX/status.md` |
| RULE-CONTEXT-01 | State → Snapshot → Cache → documento |

## Regras do Workflow v4.1 (Full Stack)

| ID | Regra |
|----|-------|
| REGISTRY-01 | Consultar `construction/registry.yaml` para índice de Features e Workstreams |
| STATE-AGG-01 | Estado Feature computado a partir dos Workstreams — não duplicar |
| STATE-WS-01 | `construction-state.yaml` é SSOT por Workstream |
| WS-ORDER-01 | Workstreams executam por `order`; sucessor aguarda predecessor `closed` |
| WS-ROUTING-01 | Prefixo PKG roteia Workstream (`pkg-` backend, `pkg-fe-` frontend) |
| CMD-FT-01 | `Execute FT-<FEATURE>` resolve Workstream/PKG via Orchestrator |
| CMD-FEATURE-01 | Usuário interage somente via comandos `FT-<FEATURE>` |
| CAP-01 | `capabilities[]` opcional no manifest — ausência = v4.0 |
| CAP-02 | Registry expõe `current_capability` computado |
| EXEC-STRAT-01 | `execution_strategy` padrão `sequential` |
| HANDOFF-01 | Handoff usa `dependencies` do Manifest + registry |
| RETRO-01 | Paths e artefatos v3.2/v4.0 preservados |

Detalhamento: `construction/12-fullstack-orchestrator.md`

# Evolução do Framework

O Engineering Framework **v3.2** está em status **Stable**. Durante a execução de uma Feature, o framework não evolui (RULE-02).

Futuras evoluções devem ser motivadas por **necessidades recorrentes observadas em múltiplas Features**, documentadas em Sprint Retrospectives — **não** por ajustes específicos de uma única implementação.

```text
FEATURE_APPROVED → Sprint Retrospective → evidências consolidadas → planejamento entre Sprints → nova versão do framework
```

Alterações de versão são registradas em `CHANGELOG.md`.

---

# Critérios de Qualidade

Toda documentação e implementação desta camada deve:

- derivar exclusivamente dos baselines e arquitetura aprovados;
- preservar a infraestrutura congelada da Sprint 0;
- não introduzir regras de negócio;
- ser rastreável até tarefas com identificador único;
- possuir critérios de conclusão objetivos;
- manter consistência entre módulos.

---

# Critérios de Conclusão da Sprint 1A

> **Nota:** Sprint 1A (Platform Foundation) foi **concluída** em 2026-07-09. Os critérios abaixo permanecem como registro histórico.

A Sprint 1A será considerada concluída quando:

1. Todos os módulos da Platform Foundation atingirem Definition of Done
2. `05-readiness-review.md` e `review/readiness-checklist.md` aprovados
3. `review/construction-audit.md` executado com resultado **APROVADO**
4. Backend preparado para iniciar FT-AUTH sem dependências circulares
5. Build `mvn clean verify` com sucesso
6. Documentação de progresso atualizada em `09-progress.md`

---

# Rastreabilidade

| Origem | Destino |
|--------|---------|
| Sprint 0 baseline | Módulo Configuration (parcialmente implementado) |
| `docs/implementation/` | Padrões técnicos dos módulos |
| `docs/construction/backend/` | Guias de construção complementares |
| Platform Foundation | FT-AUTH (`specs/features/authentication/`) |
| Tarefas PF-* | Código em `backend/` |

---

# Referências

- `CHANGELOG.md` — histórico de versões do Engineering Framework
- `history/sprint-01-retrospective.md` — retrospectiva Sprint 1
- `docs/governance/history/phase2-backend-construction-report.md` — Sprint 0 encerrada
- `docs/governance/05-roadmap.md` — Roadmap executivo
- `specs/features/FEATURE_BASELINE.md` — Golden Template
- `specs/features/authentication/specification.md` — FT-AUTH
- `docs/implementation/02-repository-structure.md` — Estrutura de pacotes
