# Framework Decisions — Engineering Framework v4.0

| Item | Valor |
|------|-------|
| Framework | Engineering Framework |
| Versão | 4.0 |
| Data | 2026-07-16 |
| Golden Template | FT-SINGULAR |
| Escopo | Evolução do Construction Framework — sem alteração de Features/código |

---

# DL-EF-4.0-001 — Feature como unidade de construção

| Campo | Valor |
|-------|-------|
| Decisão | A Feature (`FT-<DOMAIN>`) é a unidade oficial de construção, encerramento e handoff |
| Contexto | v3.2 tratava backend e frontend como registries separados |
| Alternativa rejeitada | Manter camadas como unidades independentes sem agregação |
| Impacto | Registry unificado; comandos `Execute FT-*` |
| Retrocompatibilidade | Paths `construction/features/` e `construction/frontend/features/` preservados |

---

# DL-EF-4.0-002 — Workstream como modelo genérico

| Campo | Valor |
|-------|-------|
| Decisão | Introduzir Workstream como abstração de camada executável (backend, frontend, cms, mobile…) |
| Contexto | Necessidade de orquestrar múltiplas camadas sem duplicar identidade da Feature |
| Implementação | `construction/registry.yaml` → `workstreams[]` por Feature |
| Impacto | Novas camadas adicionam tipo sem reestruturar Features existentes |
| Golden Template | FT-SINGULAR: workstreams `backend` (order 1) + `frontend` (order 2) |

---

# DL-EF-4.0-003 — Estado agregado computado (STATE-AGG-01)

| Campo | Valor |
|-------|-------|
| Decisão | O estado global da Feature é **calculado** pelo Orchestrator a partir dos Workstreams |
| Contexto | Evitar duplicação e divergência entre state files |
| Implementação | Algoritmo em `construction/12-fullstack-orchestrator.md` §5 |
| Proibição | Não criar `feature-state.yaml` em Features já encerradas |
| SSOT local | `construction-state.yaml` permanece SSOT **por Workstream** |

---

# DL-EF-4.0-004 — Feature Manifest como SSOT único

| Campo | Valor |
|-------|-------|
| Decisão | `construction/features/<CODE>/feature-manifest.yaml` permanece SSOT de descoberta da Feature |
| Contexto | Consolidar navegação sem fragmentar em múltiplos manifestos concorrentes |
| Workstream frontend | Manifest local é SSOD do Workstream; referenciado pelo manifest raiz |
| Impacto | SSOD-01 inalterado; registry é índice, não substituto do manifest |

---

# DL-EF-4.0-005 — Registry unificado

| Campo | Valor |
|-------|-------|
| Decisão | `construction/registry.yaml` substitui como fonte preferencial os registries legados |
| Legado preservado | `construction/features/registry.yaml`, `construction/frontend/registry.yaml` como aliases |
| Conteúdo | Features + Workstreams + foundations |
| Regra | REGISTRY-01 |

---

# DL-EF-4.0-006 — Comando `Execute FT-<FEATURE>`

| Campo | Valor |
|-------|-------|
| Decisão | O Orchestrator resolve Workstream e PKG ativos a partir de `Execute FT-<FEATURE>` |
| Algoritmo | `construction/12-fullstack-orchestrator.md` §6 |
| Comandos legados | `Execute PKG-XX`, `Execute Feature <CODE>` permanecem válidos |
| Roteamento PKG | WS-ROUTING-01 — prefixo determina Workstream |

---

# DL-EF-4.0-007 — Handoff via dependências existentes

| Campo | Valor |
|-------|-------|
| Decisão | Handoff entre Features usa `dependencies` já presentes nos manifests |
| Protocolo | HANDOFF-01 — predecessor FEATURE_APPROVED antes de sucessor iniciar |
| Workstream order | `depends_on_workstreams` no registry para ordem intra-Feature |
| Exemplo | FT-SINGULAR frontend depende de backend Workstream closed |

---

# DL-EF-4.0-008 — Retrocompatibilidade v3.2 (RETRO-01)

| Campo | Valor |
|-------|-------|
| Decisão | Nenhum artefato de Feature concluída é alterado nesta evolução |
| Preservado | Paths, states, manifests, PKGs, closure reports |
| Workflow v3.2 | `11-feature-execution-workflow.md` aplica-se por Workstream |
| Migração | Opcional e incremental apenas para novas Features |

---

# DL-EF-4.0-009 — FT-SINGULAR como Golden Template

| Campo | Valor |
|-------|-------|
| Decisão | FT-SINGULAR é a referência canônica Full Stack do framework v4.0 |
| Evidência | 6 PKGs backend + 6 PKGs frontend — ambos FEATURE_APPROVED |
| Documentação | `construction/golden-template/FT-SINGULAR.md` |
| Validação | Registry `golden: true`; STATE-AGG-01 produz FEATURE_APPROVED |

---

# Referências

- `construction/CHANGELOG.md` — [4.0.0]
- `construction/12-fullstack-orchestrator.md`
- `construction/14-framework-decisions-v4.1.md` — evolução incremental v4.1
- `construction/registry.yaml`
