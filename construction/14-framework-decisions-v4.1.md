# Framework Decisions — Engineering Framework v4.1

| Item | Valor |
|------|-------|
| Framework | Engineering Framework |
| Versão | 4.1 |
| Baseline | 4.0 |
| Data | 2026-07-16 |
| Golden Template | FT-SINGULAR |
| Escopo | Refinamento incremental — Capabilities, Execution Strategy, modelo operacional simplificado |

---

# DL-EF-4.1-001 — Capability como agrupador funcional opcional

| Campo | Valor |
|-------|-------|
| Decisão | Introduzir **Capability** como agrupador funcional opcional no Feature Manifest |
| Contexto | Features maiores precisam organização por funcionalidade sem fragmentar identidade ou lifecycle |
| Estrutura | Workstreams → Capabilities → Tasks → PKGs → Acceptance |
| Implementação | Seção opcional `capabilities[]` em `feature-manifest.yaml` |
| Impacto | Organização declarativa; lifecycle por Workstream **inalterado** |
| Retrocompatibilidade | Ausência de `capabilities[]` preserva comportamento v4.0 (CAP-01, RETRO-01) |
| Golden Template | FT-SINGULAR não declara capabilities — `current_capability: null` |

---

# DL-EF-4.1-002 — Execution Strategy declarativa

| Campo | Valor |
|-------|-------|
| Decisão | Feature pode declarar `execution_strategy: sequential \| parallel` |
| Padrão | `sequential` — equivalente ao WS-ORDER-01 do v4.0 |
| Comportamento atual | `parallel` tratado como `sequential` até evolução futura do Orchestrator |
| Implementação | Campo opcional no manifest; template `feature-manifest-v4.yaml` |
| Regra | EXEC-STRAT-01 |
| Retrocompatibilidade | Omitir campo = `sequential` — nenhuma Feature existente precisa migrar |

---

# DL-EF-4.1-003 — Current Capability no Registry

| Campo | Valor |
|-------|-------|
| Decisão | Registry unificado expõe `current_capability` por Feature |
| Valores | `id` da Capability em execução; `null` quando ausente, não aplicável ou encerrada |
| Fonte | Computado pelo Orchestrator (CAP-02) — não persistido no manifest |
| Implementação | `construction/registry.yaml` — campo por entrada em `features[]` |
| Retrocompatibilidade | Ausência do campo equivale a `null`; registry v2 (framework 4.0) permanece válido |

---

# DL-EF-4.1-004 — Ponto de entrada exclusivo Feature (CMD-FEATURE-01)

| Campo | Valor |
|-------|-------|
| Decisão | O usuário interage **exclusivamente** via comandos `FT-<FEATURE>` |
| Comandos oficiais | `Execute`, `Continue`, `Review`, `Close FT-<FEATURE>` |
| Responsabilidade Orchestrator | Resolver Workstream, Capability, PKG, estado e dependências internamente |
| Comandos internos | `Execute Feature`, `Execute PKG-XX` — emitidos pelo Orchestrator, não pelo usuário |
| Legado | `Execute PKG-XX`, `Encerrar Feature` permanecem válidos (RETRO-01) |

---

# DL-EF-4.1-005 — Retrocompatibilidade v4.0 (RETRO-01 estendido)

| Campo | Valor |
|-------|-------|
| Decisão | v4.1 é evolução **incremental** sobre v4.0 — sem migração obrigatória |
| Preservado | Manifests v2/v4 existentes, states, PKGs, paths, workflow v3.2 por Workstream |
| Opcional | `capabilities[]`, `execution_strategy`, `current_capability` |
| Features FEATURE_APPROVED | Nenhum artefato alterado (FT-AUTH, FT-AREA, FT-SINGULAR, FT-EQUIPE, FT-COLABORADOR) |
| Registry | `version: 3`, `framework: "4.1"` — campos novos opcionais |

---

# DL-EF-4.1-006 — FT-SINGULAR como validação de compatibilidade

| Campo | Valor |
|-------|-------|
| Decisão | FT-SINGULAR permanece Golden Template sem migração para capabilities |
| Evidência | Manifest v2 sem `capabilities[]` nem `execution_strategy` |
| Registry | `current_capability: null` — comportamento padrão documentado |
| Validação | STATE-AGG-01, CMD-FEATURE-01 e WS-ORDER-01 inalterados para FT-SINGULAR |
| Documentação | `construction/golden-template/FT-SINGULAR.md` — seção compatibilidade v4.1 |

---

# DL-EF-4.1-007 — Validation Summary padronizado (VAL-01)

| Campo | Valor |
|-------|-------|
| Decisão | Todo PKG documenta validação em formato único **VALIDATION SUMMARY** |
| Contexto | Relatórios extensos de validação por PKG geravam ruído e inconsistência |
| Relatório principal | `pkg-XX/status.md` — seção VALIDATION SUMMARY |
| Evidência | `pkg-XX/evidence/build-verify-YYYY-MM-DD.log` — log completo |
| Processo | Comandos de validação inalterados (BUILD-01) — apenas apresentação |
| Status | `PASS` \| `BUILD_FAILURE` \| `ENVIRONMENT_FAILURE` |
| Template | `construction/templates/pkg-validation-summary.md` |
| Regra | VAL-01 / R-22 em `construction/04-construction-rules.md` |

---

# Referências

- `construction/CHANGELOG.md` — [4.1.0]
- `construction/12-fullstack-orchestrator.md` — SSOT orquestração v4.1
- `construction/13-framework-decisions-v4.md` — baseline v4.0 (DL-EF-4.0-001..009)
- `construction/registry.yaml` — registry v3
- `construction/templates/feature-manifest-v4.yaml` — template com extensões v4.1
