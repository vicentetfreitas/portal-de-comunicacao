# SSOT Reconciliation — Etapa 3

| Campo | Valor |
|--------|--------|
| Artefato | ssot-reconciliation-etapa3.md |
| Camada | Construction / Review |
| Versão | 1.0 |
| Data | 2026-08-13 |
| Categoria documental | Evidence |

---

## 1. Objetivo

Determinar e formalizar a fonte autoritativa de verdade (SSOT) para cada categoria de informação relevante do projeto Portal de Comunicação.

Eliminar ambiguidades documentais e estabelecer hierarquia explícita:

```text
SSOT → derivados → operacionais → legados
```

**Não incluído:** implementação de features, alteração de código, DDL, infraestrutura.

---

## 2. Escopo analisado

| Área | Artefatos |
|------|-----------|
| `docs/` | governance (11 files), domain, architecture, backlog, solution-design |
| `specs/` | foundation (11+ files), features (8 slugs), architecture |
| `construction/` | registry.yaml, README, features/*, frontend/features/*, review/, golden-template |
| `.cursor/` | rules (19), agents (8), orchestrator (3), prompts (6) |
| `database/` | GOVERNANCE.md, baseline, ddl, migrations |
| CI | `.github/workflows/frontend.yml` |

Features com reconciliação detalhada: **FT-SESSION**, **FT-PRIMEIRO-ACESSO**.

Registry: 7 features backend + 5 frontend workstreams + 2 foundations.

---

## 3. Princípios de precedência

**Decisão (formalizada em `specs/foundation/minimal-ssot.md`):**

1. Cada informação tem exatamente um SSOT.
2. `specs/` prevalece sobre `docs/` para implementação.
3. `docs/domain/09-business-rules.md` é SSOT de BR transversais — features referenciam.
4. `specs/features/*/api.md` prevalece sobre `docs/api/` (Evidence).
5. Estado de implementação: `feature.yaml` + git/CI > `construction-state.yaml` > `registry.yaml` > `feature-manifest.status`.
6. `tasks.md` é plano principal — PKG/execution-plan são legado.
7. `.cursor/` consome SSOT — não é SSOT paralelo.
8. Conflitos não são resolvidos silenciosamente — registrados com classificação.

**Dependência de camadas:**

```text
docs/ → specs/ → construction/ → .cursor/
```

---

## 4. Mapa de SSOT

Ver tabela completa em [`specs/foundation/minimal-ssot.md`](../../specs/foundation/minimal-ssot.md).

| Informação | SSOT |
|------------|------|
| Visão / escopo MVP | `docs/domain/01-vision.md`, `docs/backlog/04-mvp-scope.md` |
| Requisitos funcionais | `specs/features/*/specification.md` |
| Regras de negócio | `docs/domain/09-business-rules.md` |
| Glossário | `docs/domain/02-business-glossary.md`, `03-ubiquitous-language.md` |
| Identidade feature | `specs/features/<slug>/feature.yaml` |
| Casos de uso | `specs/features/*/use-cases.md` |
| Fluxos / estados | `specs/features/*/flows.md`, `state-machine.md` |
| API SDD | `specs/features/*/api.md` |
| Modelo físico | `database/` (baseline > ddl > migrations) |
| Modelo lógico | `docs/architecture/05-data-architecture.md` |
| ADR | `docs/architecture/08-decision-records.md` |
| DEC governança | `docs/governance/03-open-decisions.md` |
| Tarefas / plano | **`specs/features/*/tasks.md`** |
| Fluxo diário | `specs/foundation/development-workflow.md` |
| Paths | `specs/foundation/path-conventions.md` |
| Estado implementação | `feature.yaml` + git/CI |
| Governança documental | `docs/governance/07-documentation-architecture.md` |
| Mapa SSOT operacional | `specs/foundation/minimal-ssot.md` |

---

## 5. Matriz de artefatos derivados

| SSOT | Derivados |
|------|-----------|
| `specification.md` | `traceability.md`, RF/RN em artefatos sibling |
| `use-cases.md` | entradas em `traceability.md` |
| `api.md` | contratos em `acceptance-tests.md` |
| `docs/domain/09-business-rules.md` | BR refs em specs |
| `feature.yaml` | registry entries (indicativo) |
| `tasks.md` | PKG mapping tables (referência legado) |
| `minimal-ssot.md` | `project-index.mdc`, `construction/README.md` header |
| `07-documentation-architecture.md` | categorias SSOT/Evidence/Working/Archive |

---

## 6. Artefatos operacionais

| Artefato | Papel |
|----------|-------|
| `construction-state.yaml` | Espelho operacional por workstream (reconcilável) |
| `construction/registry.yaml` | Índice de paths e workstreams |
| `.github/workflows/` | Evidência de build/CI |
| git history / PR | Evidência de implementação |
| `docs/api/` | Espelho operacional de API implementada |
| `docs/governance/01-project-status.md` | Status executivo do projeto |
| PKG `evidence/*.log` | Evidência local de validação (quando existe) |

---

## 7. Artefatos legados

| Artefato | Papel legado | Substituto |
|----------|--------------|------------|
| `feature-manifest.yaml` | Índice de paths v3.2/v4.1 | `path-conventions.md` |
| `execution-plan.md` | Planejamento PKG | `tasks.md` |
| `session.md` | Snapshot de sessão fechada | specs direto |
| `pkg-XX/status.md` | Histórico local PKG | CI logs / PR |
| `frontend-tasks.md` | Tasks FE separadas | `specs/features/*/tasks.md` |
| `construction/11-12-*.md` | Workflow v3.2/v4.1 | `development-workflow.md` |
| `feature-construction-workflow.mdc` | Regras PKG always-off | `minimal-ssot.md` |
| `construction/features/registry.yaml` | Registry legado | `construction/registry.yaml` |
| `construction/frontend/registry.yaml` | Registry legado FE | `construction/registry.yaml` |
| Agents ARCHIVED | auditor, construction-engineer, etc. | reviewer + development-workflow |
| Orchestrators ARCHIVED | construction-orchestrator | development-workflow |

---

## 8. Conflitos encontrados

### C1 — Artefatos Etapa 2 ausentes

| Campo | Valor |
|-------|-------|
| Arquivo A | Referências em 15+ artefatos |
| Arquivo B | Ficheiros não existiam |
| Informação | minimal-ssot, path-conventions, development-workflow, 09-framework-simplification-scope |
| Precedência | Criar consolidando artefatos existentes |
| Justificativa | Links quebrados impediam navegação SSOT |
| Impacto | Agentes e humanos sem mapa operacional |
| Ação | **RESOLVED** — criados na Etapa 3 |
| Classificação | RESOLVED |

### C2 — Dual workflow Cursor

| Campo | Valor |
|-------|-------|
| Arquivo A | `project-index.mdc` (always-on) — specs/tasks.md |
| Arquivo B | `feature-construction-workflow.mdc`, `token-economy.mdc` — manifest/PKG |
| Informação | Quem é SSOT de paths e progresso |
| Precedência | `minimal-ssot.md` + `project-index.mdc` |
| Justificativa | Etapa 2 simplificação aprovada |
| Impacto | Agentes on-demand podem seguir fluxo legado |
| Ação | Marcar regras legadas como Archive |
| Classificação | ACCEPTED_LEGACY (regras marcadas) |

### C3 — registry.yaml header

| Campo | Valor |
|-------|-------|
| Arquivo A | Header: "Feature Manifest permanece SSOT" |
| Arquivo B | `discovery.status_policy` aponta minimal-ssot |
| Informação | Role do manifest |
| Precedência | minimal-ssot.md |
| Ação | Corrigir header |
| Classificação | RESOLVED |

### C4 — FT-PRIMEIRO-ACESSO estado incoerente

| Campo | Valor |
|-------|-------|
| Fontes | feature.yaml READY_FOR_REVIEW; BE not_started; FE execution; manifests cruzados; PKG NOT_STARTED; git WIP |
| Informação | Fase real da feature |
| Precedência | feature.yaml para spec phase; git/CI para implementação |
| Inferência | FE implementação adiantada sem spec APPROVED (viola DoR) |
| Impacto | Risco de implementar contra spec não aprovada |
| Ação | Alinhar manifests; documentar PENDING_DECISION |
| Classificação | PENDING_DECISION |

### C5 — FT-SESSION

| Campo | Valor |
|-------|-------|
| Estado | Coerente (closed/APPROVED) |
| Ressalva | feature.yaml formato legacy; artefatos spec incompletos |
| Ação | Normalizar feature.yaml v1.1 |
| Classificação | RESOLVED (gap artefatos aceito — feature pré-simplificação) |

### C6 — INC-PA-001..006

| Campo | Valor |
|-------|-------|
| Fonte | `traceability.md` |
| Informação | Inconsistências domain/specs/database |
| Ação | Não auto-corrigir — decisão de produto |
| Classificação | PENDING_DECISION |

### C7 — specs/README obsoleto

| Campo | Valor |
|-------|-------|
| Informação | "apenas Foundation" vs 8+ features existentes |
| Ação | Atualizar escopo |
| Classificação | RESOLVED |

### C8 — DEC ID collision

| Campo | Valor |
|-------|-------|
| Fontes | `governance/03` vs `technology/04-decision-log.md` |
| Classificação | PENDING_DECISION (Gate Final 2026-07-24) |

---

## 9. Reconciliação de FT-SESSION

### Fato

| Fonte | Estado |
|-------|--------|
| `feature.yaml` | APPROVED (formato legacy) |
| `specification.md` | Funcional SSOT — multi-contexto aprovado, implementação deferida a FT-PRIMEIRO-ACESSO |
| Construction BE | `phase: closed`, `completed: []`, sem PKG folders |
| Construction FE | `phase: closed`, FEATURE_APPROVED, sem PKG folders |
| Registry | `status: closed`, pkgs_total 4/2 (stale — sem folders) |
| Git | Implementação phase-1 (single link) em código |

### Inferência

Feature **fechada** no construction com escopo FE encerrado. Evolução multi-contexto pertence a FT-PRIMEIRO-ACESSO.

### Artefatos spec ausentes

`api.md`, `tasks.md`, `traceability.md` — gap aceito (feature fechada pré-template crud-feature@1.1 completo).

### Classificação

**Coerente** com ressalvas documentadas. INC-PA-002 liga FT-SESSION phase-1 vs multi-contexto.

---

## 10. Reconciliação de FT-PRIMEIRO-ACESSO

### Comparação spec interna

| Artefato | Consistência |
|----------|--------------|
| use-cases.md ↔ flows.md ↔ state-machine.md | Alinhados |
| api.md ↔ acceptance-tests.md | Alinhados |
| traceability.md ↔ tasks.md | Alinhados (INC-* explícitos) |
| tasks.md | SSOT de plano declarado; PKG mapping como referência legado |

### Comparação cross-layer

| Fonte | Valor |
|-------|-------|
| `feature.yaml` | `READY_FOR_REVIEW` |
| BE construction-state | `not_started` |
| FE construction-state | `execution`, pkg-fe-01 |
| BE manifest (antes) | `phase: execution` — **conflito** |
| FE manifest (antes) | `phase: not_started` — **conflito** |
| PKG status files | Todos NOT_STARTED |
| Git WIP | session.store.ts, auth changes (evidência FE parcial) |
| Registry | execution aggregate |

### Cadeia Spec → Tasks → Implementation

```text
Spec (READY_FOR_REVIEW) ≠ Implementation State (FE WIP)
Tasks (TK-PA-*) = plano definido, não executado em PKG files
CI/Review = não validado (WIP não merged)
```

### Decisão pendente

Aprovar spec (`APPROVED`) antes de continuar FE, ou pausar FE até review.

---

## 11. Reconciliação de .cursor

### Fato

| Item | Estado |
|------|--------|
| Always-on | `project-index.mdc` apenas (metadata) |
| Agents ativos | specification-engineer, backend-engineer, reviewer |
| Agents archived | auditor, construction-engineer, feature-implementer, platform-architect |
| Orchestrators archived | 3 stubs |
| Links quebrados (antes) | minimal-ssot, path-conventions, development-workflow, 09-framework |
| Legacy rules | feature-construction-workflow, construction-flow, token-economy PKG refs |

### Inferência

`.cursor` estava em transição — always-on simplificado mas on-demand rules mantinham v4.1.

### Ação

- Links resolvidos após criar foundation files
- Regras legadas marcadas Archive
- backend-engineer/reviewer atualizados (remover refs archived agents)

### Classificação

**RESOLVED** — `.cursor` consome SSOT, não compete.

---

## 12. Reconciliação de construction

### Role explícita

| Papel | Sim/Não |
|-------|---------|
| Fonte de especificação | **Não** — specs/ é SSOT |
| Estado operacional (transição) | **Sim** — construction-state.yaml |
| Evidência de execução histórica | **Sim** — PKG, closure-report |
| Plano de implementação | **Não** — tasks.md |
| Índice de paths | **Parcial** — registry.yaml (legado manifests) |

### Registry

- Índice unificado REGISTRY-01
- `status` fields indicativos — não SSOT
- `discovery.status_policy` alinhado a minimal-ssot

### Classificação

**RESOLVED** — construction não compete com specs para definição de produto.

---

## 13. Reconciliação de governance

| Documento | Categoria | Papel |
|-----------|-----------|-------|
| `07-documentation-architecture.md` | SSOT | Arquitetura documental |
| `09-framework-simplification-scope.md` | SSOT | Escopo Etapa 2 |
| `03-open-decisions.md` | SSOT | DECs projeto |
| `04-open-questions.md` | SSOT | OQs projeto |
| `01-project-status.md` | Operacional | Status executivo |
| `reconciliation-report.md` | Evidence | Exit Gate 2026-07-24 |
| `history/*` | Archive | Relatórios fase 1/2 |

**Sem decisões contraditórias novas.** DEC ID collision permanece PENDING_DECISION.

Governance não compete com domain/specs — camadas distintas com precedência em minimal-ssot.

---

## 14. Alterações necessárias

| # | Alteração | Status |
|---|-----------|--------|
| 1 | Criar minimal-ssot, path-conventions, development-workflow | **Done** |
| 2 | Criar 09-framework-simplification-scope | **Done** |
| 3 | Criar state-reconciliation-etapa2 retrospectivo | **Done** |
| 4 | Corrigir registry.yaml header | **Done** |
| 5 | Alinhar FT-PRIMEIRO-ACESSO manifests | **Done** |
| 6 | Normalizar session/feature.yaml v1.1 | **Done** |
| 7 | Atualizar specs/README.md | **Done** |
| 8 | Marcar regras .cursor legadas | **Done** |
| 9 | Notas legado em execution-plan/PKG FT-PA | **Done** |
| 10 | Atualizar backend-engineer, reviewer | **Done** |

---

## 15. Decisões pendentes

| ID | Tema | Classificação |
|----|------|---------------|
| PD-01 | FT-PRIMEIRO-ACESSO: aprovar spec vs continuar FE WIP | PENDING_DECISION |
| PD-02 | INC-PA-001..006 (domain/database/specs) | PENDING_DECISION |
| PD-03 | DEC ID collision governance vs technology | PENDING_DECISION |
| PD-04 | API namespace FT-SESSION vs FT-PRIMEIRO-ACESSO `/session/*` | PENDING_DECISION |
| PD-05 | FT-COLABORADOR FE pkg-fe-02 state vs folder gap | PENDING_DECISION |

**Nenhum bloqueia Etapa 3 documental.**

---

## 16. Critérios de conclusão

| Critério | Status |
|----------|--------|
| SSOT por categoria identificado | ✅ |
| Derivados identificados | ✅ |
| Operacionais identificados | ✅ |
| Legados identificados | ✅ |
| Conflitos silenciosos eliminados | ✅ |
| FT-SESSION coerente | ✅ |
| FT-PRIMEIRO-ACESSO documentado | ✅ (PD-01) |
| tasks.md plano principal | ✅ |
| construction não compete specs | ✅ |
| .cursor não SSOT paralelo | ✅ |
| Governance precedência clara | ✅ |
| Referências obsoletas corrigidas/classificadas | ✅ |
| Sem alteração código produção | ✅ |
| Sem alteração banco | ✅ |
| Relatório produzido | ✅ |

---

## 17. Conclusão

**Fato:** O projeto operava em dual-framework — Etapa 2 atualizou referências sem completar artefatos foundation SSOT.

**Decisão:** Formalizar precedência em `minimal-ssot.md`; construction e .cursor legado classificados; tasks.md confirmado como plano principal.

**Recomendação:** Antes de continuar implementação FT-PRIMEIRO-ACESSO, resolver PD-01 (aprovar spec) e INC-PA-001..004.

**Status:** **ETAPA 3 — CONCLUÍDA** (com decisões pendentes documentadas).

---

## Validação final — classificação de problemas

| Problema | Classificação |
|----------|---------------|
| Artefatos Etapa 2 ausentes | RESOLVED |
| feature-manifest como SSOT em rules legadas | ACCEPTED_LEGACY |
| PKG como plano principal | RESOLVED |
| Links minimal-ssot/path-conventions | RESOLVED |
| FT-PA estado FE WIP vs spec | PENDING_DECISION |
| INC-PA-* | PENDING_DECISION |
| DEC ID collision | PENDING_DECISION |
| FT-SESSION feature.yaml legacy | RESOLVED |
| registry header manifest SSOT | RESOLVED |
| FT-COLABORADOR pkg folder gap | PENDING_DECISION |
