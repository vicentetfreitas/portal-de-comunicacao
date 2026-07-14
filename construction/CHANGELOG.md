# CHANGELOG — Engineering Framework

| Item | Valor |
|------|-------|
| Nome | **Engineering Framework** |
| Versão atual | **v3.2** |
| Status | **Stable** |
| Camada | `construction/` |
| Última atualização | 2026-07-09 |

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
| **Minor** (v3.x) | Novas regras, templates ou fases sem quebra de compatibilidade |
| **Stable** | Primeira Feature de negócio aprovada + retrospectiva emitida |

Evoluções futuras devem ser motivadas por **necessidades recorrentes observadas em múltiplas Features**, documentadas em Sprint Retrospectives — não por ajustes específicos de uma única implementação (ver RULE-04 e `construction/README.md` § Evolução do Framework).
