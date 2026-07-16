# CHANGELOG — Engineering Framework

| Item | Valor |
|------|-------|
| Nome | **Engineering Framework** |
| Versão atual | **v4.1.1** |
| Baseline | **v4.0** |
| Status | **Stable** |
| Camada | `construction/` |
| Última atualização | 2026-07-16 |

---

## [4.1.1] — 2026-07-16 — Validation Summary (VAL-01)

### Marco

- Padronização da **apresentação** de validação em todos os PKGs — processo BUILD-01 inalterado.

### Adicionado

- Regra **VAL-01** / **R-22** — Validation Summary em `pkg-XX/status.md`
- Templates: `pkg-validation-summary.md`, `pkg-implementation-report.md`
- Scripts de evidência: `pkg-evidence-run-frontend.sh`, `pkg-evidence-run-backend.sh`
- Decisão **DL-EF-4.1-007** em `14-framework-decisions-v4.1.md`
- Seção VALIDATION SUMMARY em `templates/pkg-status.md`
- Orchestrator: emissão obrigatória do resumo ao concluir PKG

### Preservado

- Critérios de aceite e fluxo de execução dos PKGs
- BUILD-01 — comandos permitidos / proibidos
- Logs completos em `evidence/` (não no relatório principal)

---

## [4.1.0] — 2026-07-16 — Capabilities & Operational Model

### Marco

- Refinamento incremental sobre v4.0 — **retrocompatibilidade total**.
- **Capabilities** opcionais como agrupadores funcionais no Feature Manifest.
- **Execution Strategy** declarativa (`sequential` padrão; `parallel` reservado).
- Registry com **Current Capability** computado (CAP-02).
- Modelo operacional simplificado — **entrada exclusiva Feature** (CMD-FEATURE-01).

### Adicionado

- Seção `capabilities[]` opcional em `templates/feature-manifest-v4.yaml`
- Campo `execution_strategy` opcional no manifest (padrão: `sequential`)
- `current_capability` em `construction/registry.yaml` (registry v3, framework 4.1)
- `construction/14-framework-decisions-v4.1.md` — decisões DL-EF-4.1-001..006
- Regras CAP-01, CAP-02, EXEC-STRAT-01, CMD-FEATURE-01
- Comandos oficiais: `Execute`, `Continue`, `Review`, `Close FT-<FEATURE>`

### Preservado (RETRO-01)

- Todos os manifests, states e PKGs de Features FEATURE_APPROVED
- FT-SINGULAR como Golden Template **sem migração**
- Comportamento Orchestrator v4.0 quando capabilities ausentes
- `execution_strategy` omitido = `sequential` = WS-ORDER-01
- Comandos legados (`Execute PKG-XX`, `Encerrar Feature`)

### Validação

- FT-SINGULAR: `current_capability: null`; manifest v2 inalterado
- Documentação: `12-fullstack-orchestrator.md` v4.1, `golden-template/FT-SINGULAR.md`

### Referências

- `construction/14-framework-decisions-v4.1.md`
- `construction/12-fullstack-orchestrator.md`

---

## [4.0.0] — 2026-07-16 — Full Stack Orchestrator

### Marco

- Evolução do framework para **orquestrador Full Stack** com **FT-SINGULAR** como Golden Template.
- Feature como unidade de construção; Workstream como modelo genérico de camada.
- Estado agregado **computado** (STATE-AGG-01) — sem duplicar states.
- Comando `Execute FT-<FEATURE>` resolve Workstream e PKG ativos.
- Retrocompatibilidade total com v3.2 e Features FEATURE_APPROVED.

### Adicionado

- `construction/12-fullstack-orchestrator.md` — SSOT orquestração v4.0
- `construction/registry.yaml` — registry unificado Features + Workstreams
- `construction/golden-template/FT-SINGULAR.md` — Golden Template Full Stack
- `construction/13-framework-decisions-v4.md` — decisões DL-EF-4.0-001..009
- `construction/templates/feature-manifest-v4.yaml`
- `construction/templates/workstream-state.yaml`
- Regras REGISTRY-01, STATE-AGG-01, WS-ORDER-01, WS-ROUTING-01, CMD-FT-01, HANDOFF-01, RETRO-01

### Preservado (RETRO-01)

- Paths `construction/features/` e `construction/frontend/features/`
- `construction-state.yaml` por Workstream
- `11-feature-execution-workflow.md` (v3.2 por Workstream)
- Registries legados como aliases

### Referências

- `construction/13-framework-decisions-v4.md`
- `construction/golden-template/FT-SINGULAR.md`

---

## [3.2.0] — 2026-07-09 — Stable

### Marco

- Framework **estabilizado** após **FEATURE_APPROVED** da **FT-AUTH** — primeira Feature de negócio concluída com sucesso.
- O Engineering Framework passa a ser a **base oficial** para o desenvolvimento das próximas Features.
- Evolução do framework **congelada** durante execução de Features (RULE-02); alterações somente entre Features ou Sprints.

### Adicionado

- **Feature Identity** — identificador oficial `FT-<DOMAIN>` como chave estável de diretório e comandos.
- **SSOD** — `feature-manifest.yaml` como Single Source of Discovery (SSOD-01).
- **Construction State por Feature** — `construction/features/<FEATURE_CODE>/construction-state.yaml` como SSOT operacional.
- **`construction/features/registry.yaml`** — índice global de Features.
- **Regras de governança RULE-01 a RULE-04** — Specification Approved, framework congelado, alinhamento Spec/API, Sprint Retrospective obrigatória.
- **Política de evolução** — mudanças no framework motivadas por necessidades recorrentes em múltiplas Features, não por ajustes pontuais de uma implementação.

### Validado em produção de construção

| Unidade | Resultado | Evidência |
|---------|-----------|-----------|
| Platform Foundation (Sprint 1A) | APROVADA | `construction/review/completion-report.md` |
| FT-AUTH (Sprint 1 Backend) | FEATURE_APPROVED | `construction/features/FT-AUTH/construction-state.yaml` |
| Sprint Retrospective | Emitida | `construction/history/sprint-01-retrospective.md` |

### Referências

- `construction/11-feature-execution-workflow.md` — SSOT do workflow v3.2
- `construction/04-construction-rules.md` — regras de governança e construção

---

## [3.1.0] — 2026-07-09

### Adicionado

- Construction State independente por Feature de negócio.
- Dois níveis: `platform-foundation/` (histórico) e `features/<FEATURE_CODE>/`.
- `execution-plan.md`, `review/` e `reports/` por Feature.
- Primeira Feature estruturada: FT-AUTH.

### Referência

- `construction/history/README.md` — Construction State por Feature (v3.1)

---

## [3.0.0] — 2026-07-09

### Adicionado

- Workflow orientado à Feature em três fases: Sessão → PKGs → Encerramento.
- `construction-state.yaml` como SSOT do estado operacional.
- `session.md` imutável (SESSION-01) — Snapshot, não progresso.
- Cache formal (CACHE-01, CACHE-02, RULE-CONTEXT-01).
- BUILD-01 — `mvn clean verify` somente no encerramento.
- PARALLEL-01 — PKGs independentes.

### Referência

- `construction/11-feature-execution-workflow.md`
- `construction/history/README.md` — Workflow Orientado à Feature

---

## [2.0.0] — 2026-07-09 (pré-Feature Identity)

### Adicionado

- Session imutável e navegação via Manifest.
- Snapshot estruturado e regras de invalidação.

---

## [1.0.0] — 2026-07-08

### Adicionado

- Camada `construction/` — governança da Platform Foundation (Sprint 1A).
- Módulos PF-CONF a PF-TEST com backlog técnico rastreável.
- Review, Audit e Readiness consolidados para Sprint 1A.

### Referência

- `construction/history/README.md` — Sprint 1A Documentação Aprovada

---

## Política de versionamento

| Tipo de mudança | Quando aplicar |
|-----------------|----------------|
| **Major** (v4.0) | Mudança estrutural no workflow, artefatos obrigatórios ou SSOD |
| **Minor** (v4.1, v3.x) | Novas regras opcionais, templates ou campos sem quebra de compatibilidade |
| **Stable** | Primeira Feature de negócio aprovada + retrospectiva emitida |

Evoluções futuras devem ser motivadas por **necessidades recorrentes observadas em múltiplas Features**, documentadas em Sprint Retrospectives — não por ajustes específicos de uma única implementação (ver RULE-04 e `construction/README.md` § Evolução do Framework).
