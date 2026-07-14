# Engineering Framework

| Item | Valor |
|------|-------|
| Nome | **Engineering Framework** |
| Version | **v3.2** |
| Status | **Stable** |
| Projeto | Portal de Comunicação |
| Camada | `construction/` |
| Última atualização | 2026-07-09 |
| Histórico de versões | `CHANGELOG.md` |

---

# Objetivo

Estabelecer a governança oficial da construção do Portal de Comunicação — desde a **Platform Foundation** até **Features de negócio** — com workflow orientado à Feature, rastreabilidade e critérios de encerramento.

O framework foi **estabilizado** após o **FEATURE_APPROVED** da **FT-AUTH** (primeira Feature de negócio) e passa a ser a **base oficial** para as próximas Features.

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

├── README.md                      — Este documento (Engineering Framework v3.2 Stable)
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
├── 11-feature-execution-workflow.md — **SSOT workflow orientado à Feature (v3.2)**
├── templates/
│   ├── feature-manifest.yaml      — **SSOD** (Single Source of Discovery)
│   ├── construction-state.yaml    — SSOT estado operacional (v3)
│   ├── execution-plan.md          — Template plano de execução
│   ├── feature-session.md         — Snapshot congelado (SESSION-01)
│   ├── pkg-status.md
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
├── features/                      — Features de negócio (Feature Identity + SSOD)
│   ├── README.md                  — Convenção FT-<DOMAIN>
│   ├── registry.yaml              — Índice de Features
│   └── FT-AUTH/                   — Authentication (Sprint 1)
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
Execute Feature platform-foundation    ← State → Manifest → Snapshot → Session
        ↓
PKG-01 → PKG-02 → ... → PKG-08         ← State + pkg-XX/status.md
        ↓
Encerrar Feature platform-foundation   ← State, Closure → Review → Audit → Readiness
```

Detalhamento em `06-development-order.md` e `11-feature-execution-workflow.md` (v3.2).

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
| PARALLEL-01 | PKG altera `construction-state.yaml` + `pkg-XX/status.md` |
| RULE-CONTEXT-01 | State → Snapshot → Cache → documento |

---

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
