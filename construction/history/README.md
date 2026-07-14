# History — Construction Layer

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Status | Ativo |
| Versão | 1.0 |
| Última atualização | 2026-07-09 |

---

# Objetivo

Registrar o histórico evolutivo da camada `construction/` — governança da construção do Portal de Comunicação.

---

# Escopo

Eventos significativos relacionados à camada Construction na raiz do repositório (`construction/`).

Documentação operacional complementar permanece em `docs/construction/`.

---

# Relação com Demais Camadas

| Camada | Responsabilidade | Relação |
|--------|------------------|---------|
| `docs/construction/` | Guias operacionais de construção (Sprint 0) | Complementar |
| `construction/` | Governança da Platform Foundation (Sprint 1A+) | Esta camada |
| `docs/governance/history/` | Relatórios históricos de fases | Referência |

---

# Linha do Tempo

## 2026-06-22 — Fase 1 Frontend Construction

Relatório: `docs/governance/history/phase1-frontend-construction-report.md`

Limpeza documental da camada `docs/construction/frontend/` e `docs/construction/infrastructure/`.

---

## 2026-07-08 — Sprint 0 Backend Encerrada

Relatório: `docs/governance/history/phase2-backend-construction-report.md`

- Bootstrap Maven/Spring Boot 4.1
- Shared modules (DTO, Exception, Validation, Constants, Util)
- Configuration básica (Jackson, Locale, Async, Properties)
- Infrastructure Logging (Correlation ID, MDC)
- Integração Oracle + baseline DDL (DBA)
- 106 testes unitários — BUILD SUCCESS
- Infraestrutura transversal **congelada**

---

## 2026-07-08 — Sprint 1A Documentação Aprovada

Evento: Criação e preenchimento integral da camada `construction/`.

**Artefatos produzidos:**

- Governança: README, 01–09
- Platform Foundation: 7 módulos × (README, tasks, review)
- Review: construction-audit, readiness-checklist, completion-report, reconciliation-report

**Status:** Documentação aprovada. Implementação **não iniciada**.

**Próximo passo:** Iniciar PKG-01 (Configuration Foundation).

---

## 2026-07-09 — Workflow Orientado à Feature

**Evento:** Refatoração permanente do workflow de construção.

**Mudança:** Execução dividida em Sessão da Feature → PKGs focados → Encerramento consolidado.

**Artefatos:** `11-feature-execution-workflow.md`, templates em `construction/templates/`, `platform-foundation/session.md`, `pkg-XX/status.md`.

**Impacto:** Redução de releituras, progresso local por PKG, review/audit/build completo apenas no encerramento.

---

## 2026-07-09 — Feature Identity e SSOD (v3.2)

**Evento:** Institucionalização de identificadores `FT-<DOMAIN>` e manifesto como SSOD.

**Mudança:**

- Diretórios por code: `construction/features/FT-AUTH/` (não mais `authentication/`)
- `feature-manifest.yaml` evoluído para Single Source of Discovery
- `construction/features/registry.yaml` — índice de Features
- Convenção documentada em `construction/features/README.md`

**Retrocompatibilidade:** `construction/features/authentication/README.md` redireciona para `FT-AUTH`.

**Referência:** `construction/11-feature-execution-workflow.md` v3.2

---

## 2026-07-09 — Construction State por Feature (v3.1)

**Evento:** Evolução do framework para estado independente por Feature.

**Mudança:**

- Platform Foundation permanece em `construction/platform-foundation/` (`phase: closed`)
- Features de negócio em `construction/features/<id>/` com `construction-state.yaml`, `execution-plan.md`, `review/`, `reports/`
- Primeira Feature: `construction/features/FT-AUTH/` (code: FT-AUTH, slug: authentication)

**Referência:** `construction/11-feature-execution-workflow.md` v3.1

---

## 2026-07-09 — Sprint 1A Encerrada

**Evento:** Implementação e auditoria da Platform Foundation concluídas.

**Entregas:**

- PKG-01 a PKG-07 implementados no backend
- PKG-08 Construction Audit — **APROVADA**
- `mvn clean verify` — 158 testes, BUILD SUCCESS
- Decisões CD-S1A-001 a CD-S1A-005 encerradas

**Status:** Sprint 1A **CONCLUÍDA**. Handoff para FT-AUTH.

**Referências:** `review/completion-report.md`, `review/construction-audit.md`, `09-progress.md`

---

## 2026-07-09 — Engineering Framework v3.2 Stable

**Evento:** Framework estabilizado após **FEATURE_APPROVED** da FT-AUTH (primeira Feature de negócio).

**Marco:**

- Engineering Framework **v3.2** — status **Stable**
- Base oficial para desenvolvimento das próximas Features
- Regras RULE-01 a RULE-04 institucionalizadas
- `construction/CHANGELOG.md` criado

**Evidências:**

- FT-AUTH: `construction/features/FT-AUTH/construction-state.yaml` — `FEATURE_APPROVED`
- Retrospectiva: `construction/history/sprint-01-retrospective.md`
- Governança: `construction/README.md`, `construction/04-construction-rules.md`

**Política de evolução:** alterações no framework somente entre Features/Sprints, motivadas por necessidades recorrentes em múltiplas Features (não ajustes pontuais).

---

# Eventos Futuros (A Preencher)

| Data | Evento | Referência |
|------|--------|------------|
| — | Sprint 2 — próxima Feature | `construction/features/registry.yaml` |

---

# Referências

- `docs/governance/history/phase2-backend-construction-report.md`
- `docs/governance/05-roadmap.md`
- `../09-progress.md`
