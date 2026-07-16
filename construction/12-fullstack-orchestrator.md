# Full Stack Orchestrator — Engineering Framework v4.1

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Framework | **Engineering Framework v4.1** |
| Camada | Construction |
| Tipo | **SSOT — Orquestração Full Stack** |
| Status | **Stable** |
| Versão | **4.1** |
| Baseline | **4.0** (retrocompatível) |
| Última atualização | 2026-07-16 |
| Golden Template | **FT-SINGULAR** |
| Changelog | `construction/CHANGELOG.md` |
| Decisões v4.1 | `construction/14-framework-decisions-v4.1.md` |
| Retrocompatibilidade | v4.0 e v3.2 — paths e artefatos legados preservados |

---

# Objetivo

Transformar o Engineering Framework em um **orquestrador Full Stack** onde:

1. A **Feature** (`FT-<DOMAIN>`) é a unidade de construção e encerramento.
2. O **Workstream** (backend, frontend, cms, mobile…) é o modelo genérico de execução por camada.
3. O **estado agregado da Feature** é **calculado** a partir dos Workstreams — nunca duplicado manualmente.
4. O **Feature Manifest** permanece **SSOT de descoberta** da Feature.
5. O comando `Execute FT-<FEATURE>` resolve automaticamente Workstream e PKG ativos.
6. *(v4.1)* **Capabilities** opcionais agrupam funcionalidades sem alterar o lifecycle por Workstream.
7. *(v4.1)* O **ponto de entrada do usuário** é exclusivamente a Feature — o Orchestrator resolve Workstream, Capability, PKG e dependências.

Esta evolução **não altera** código de negócio, specs, APIs nem artefatos de Features já concluídas.

---

# Princípio Fundamental

```text
Feature (FT-<DOMAIN>)
  ├── execution_strategy: sequential | parallel  (v4.1 — opcional, padrão sequential)
  ├── capabilities[] (opcional — v4.1)
  │     ├── tasks → specs
  │     ├── pkgs → por workstream
  │     └── acceptance → specs
  ├── Workstream: backend     → construction/features/FT-X/
  ├── Workstream: frontend    → construction/frontend/features/FT-X/
  ├── Workstream: cms         → (futuro)
  └── Workstream: mobile      → (futuro)
```

| Unidade | Papel |
|---------|-------|
| **Feature** | Identidade, dependências, handoff, encerramento global — **único ponto de entrada do usuário** |
| **Feature Manifest** | **SSOT** — descoberta de artefatos, workstreams, capabilities, specs, implementação |
| **Capability** *(v4.1, opcional)* | Agrupador funcional reutilizável — organiza Tasks, PKGs e Acceptance sem alterar lifecycle |
| **Workstream** | Execução por camada — Session, PKGs, State próprio |
| **Workstream State** | **SSOT operacional** da camada (`construction-state.yaml`) |
| **Estado agregado** | **Computado** pelo Orchestrator (STATE-AGG-01) |
| **PKG** | Unidade de implementação dentro de um Workstream |
| **Task** | Referência a backlog em `specs/features/<slug>/tasks.md` |
| **Acceptance** | Referência a critérios em `specs/features/<slug>/acceptance-tests.md` |

---

# Regras Formais (v4.0 + v4.1)

| ID | Regra | Descrição |
|----|-------|-----------|
| REGISTRY-01 | Registry unificado | Consultar `construction/registry.yaml` para índice de Features e Workstreams |
| SSOD-01 | Manifest primeiro | `feature-manifest.yaml` descobre artefatos — sem paths hardcoded |
| STATE-AGG-01 | Estado agregado computado | Estado global da Feature derivado dos Workstreams — não persistir cópia divergente |
| STATE-WS-01 | State por Workstream | Cada Workstream possui `construction-state.yaml` como SSOT local |
| WS-ORDER-01 | Ordem de Workstreams | Workstreams executam por `order` ascendente; sucessor aguarda predecessor `closed` |
| WS-ROUTING-01 | Roteamento de PKG | `pkg-{NN}` → backend; `pkg-fe-{NN}` → frontend; `pkg-fe-s0-{NN}` → frontend-foundation |
| CMD-FT-01 | Comando unificado | `Execute FT-<FEATURE>` resolve Workstream/PKG ativo via algoritmo §8 |
| CMD-FEATURE-01 | Entrada exclusiva Feature | Usuário interage somente via comandos `FT-<FEATURE>` — Orchestrator resolve internamente |
| CAP-01 | Capability opcional | `capabilities[]` no manifest é opcional; ausência preserva comportamento v4.0 |
| CAP-02 | Current Capability | Registry expõe `current_capability` computado quando capabilities existem |
| EXEC-STRAT-01 | Execution Strategy | `execution_strategy` padrão `sequential`; `parallel` reservado — não altera Orchestrator atual |
| HANDOFF-01 | Handoff por dependências | Transição entre Features usa `dependencies` do Manifest + registry |
| RETRO-01 | Retrocompatibilidade | Paths v3.2/v4.0, registries legados e comandos internos permanecem válidos |
| VAL-01 | Validation Summary | Resumo padronizado de validação em todo PKG — `templates/pkg-validation-summary.md` |

Regras v3.2 (SESSION-01, CACHE-01, BUILD-01, PARALLEL-01, RULE-CONTEXT-01, VAL-01) **permanecem** aplicáveis **por Workstream**.

---

# Modelo de Workstream

## Tipos suportados

| Tipo | Path legado | Descrição |
|------|-------------|-----------|
| `platform-foundation` | `construction/platform-foundation/` | Infraestrutura backend (Sprint 1A) |
| `frontend-foundation` | `construction/frontend/` | Infraestrutura frontend (Sprint 0) |
| `backend` | `construction/features/<CODE>/` | API e domínio backend |
| `frontend` | `construction/frontend/features/<CODE>/` | UI e consumo de API |
| `cms` | `construction/cms/features/<CODE>/` | Conteúdo CMS (reservado) |
| `mobile` | `construction/mobile/features/<CODE>/` | App mobile (reservado) |

## Estrutura por Workstream

```text
<workstream-root>/
├── feature-manifest.yaml      ← SSOD do Workstream (pode ser o manifest raiz da Feature)
├── construction-state.yaml    ← SSOT operacional do Workstream
├── execution-plan.md
├── session.md                 ← Snapshot imutável (SESSION-01)
├── pkg-XX/ ou pkg-fe-XX/
│   └── status.md
├── review/
├── reports/
└── closure-report.md
```

## Convenção de PKGs

| Padrão | Workstream |
|--------|------------|
| `pkg-01` … `pkg-NN` | backend |
| `pkg-fe-01` … `pkg-fe-NN` | frontend |
| `pkg-fe-s0-01` … `pkg-fe-s0-NN` | frontend-foundation |
| `pkg-01` … `pkg-08` | platform-foundation |

---

# Feature Manifest como SSOT

O **Feature Manifest** na raiz backend (`construction/features/<CODE>/feature-manifest.yaml`) é o **ponto de entrada único** da Feature.

## Responsabilidades do Manifest

| Seção | Conteúdo |
|-------|----------|
| `feature` | Identidade oficial (`code`, `id`, `name`) |
| `specification` | Path SDD em `specs/features/` |
| `construction` | Raiz backend + referências a outros Workstreams |
| `construction.frontend` | Path, manifest e state do Workstream frontend |
| `workstreams` | *(v4.0 — novas Features)* Lista declarativa de Workstreams |
| `execution_strategy` | *(v4.1 — opcional)* `sequential` (padrão) ou `parallel` — orienta Orchestrator futuro |
| `capabilities` | *(v4.1 — opcional)* Agrupadores funcionais com tasks, pkgs e acceptance |
| `dependencies` | Features e fundações predecessoras |
| `artifacts` | Índice navegável de artefatos |
| `implementation` | Paths de código por camada |
| `pkgs` | Catálogo de PKGs por Workstream |

## FT-SINGULAR (Golden Template)

Referência canônica: `construction/golden-template/FT-SINGULAR.md`

```text
construction/features/FT-SINGULAR/feature-manifest.yaml     ← SSOT Feature
construction/features/FT-SINGULAR/construction-state.yaml   ← Workstream backend
construction/frontend/features/FT-SINGULAR/                 ← Workstream frontend
```

Features concluídas **não precisam** ser migradas — o registry unificado documenta os Workstreams implicitamente.

---

# Capabilities (v4.1 — opcional)

Capabilities são **agrupadores funcionais opcionais** que organizam Features maiores sem alterar o lifecycle existente por Workstream.

## Hierarquia da Feature

```text
Feature
  ├── Workstreams        → execução por camada (lifecycle inalterado)
  ├── Capabilities     → agrupamento funcional (opcional)
  │     ├── Tasks      → referência a specs/features/<slug>/tasks.md
  │     ├── PKGs       → mapeados por workstream
  │     └── Acceptance → referência a acceptance-tests.md
  └── PKGs             → unidade de implementação (dentro de Workstream)
```

## Quando usar

| Cenário | Capabilities |
|---------|--------------|
| Feature CRUD simples (ex.: FT-SINGULAR) | **Não declarar** — Workstreams + PKGs suficientes |
| Feature multi-domínio ou epic | Declarar capabilities para navegação e rastreabilidade |
| Reutilização de padrão funcional | Capability `id` estável entre Features |

## Estrutura no Manifest

```yaml
execution_strategy: sequential   # padrão — omitir equivale a sequential

capabilities:
  - id: crud-core
    name: CRUD Core
    order: 1
    optional: false
    depends_on_capabilities: []
    tasks:
      - ../../../specs/features/<slug>/tasks.md
    workstreams:
      - backend
      - frontend
    pkgs:
      backend: [pkg-01, pkg-02, pkg-03, pkg-04]
      frontend: [pkg-fe-01, pkg-fe-02, pkg-fe-03, pkg-fe-04]
    acceptance:
      - ../../../specs/features/<slug>/acceptance-tests.md
```

## Regras (CAP-01)

| Regra | Descrição |
|-------|-----------|
| Opcional | Ausência de `capabilities[]` → comportamento idêntico ao v4.0 |
| Não substitui Workstream | Lifecycle (Session → PKGs → Closure) permanece **por Workstream** |
| Ordem | Capabilities executam por `order` quando `execution_strategy: sequential` |
| Dependências | `depends_on_capabilities` dentro da mesma Feature |
| Registry | Orchestrator computa `current_capability` e atualiza índice (CAP-02) |

## Algoritmo — Current Capability (CAP-02)

```text
Se manifest.capabilities ausente ou vazio:
  current_capability = null

Senão:
  Para cada Capability C (ordenado por C.order):
    Se TODOS PKGs de C em TODOS workstreams de C estão DONE:
      continuar
    Senão:
      current_capability = C.id
      parar

  Se todas capabilities concluídas:
    current_capability = null
```

O Orchestrator **não altera** o manifest da Feature para persistir `current_capability` — o registry é o índice consolidado.

---

# Execution Strategy (v4.1 — opcional)

A Feature pode declarar como prefere organizar a execução:

| Valor | Descrição | Comportamento atual |
|-------|-----------|---------------------|
| `sequential` | **Padrão** — Workstreams e Capabilities por ordem | Idêntico ao v4.0 (WS-ORDER-01) |
| `parallel` | Workstreams/Capabilities independentes em paralelo | **Reservado** — Orchestrator trata como `sequential` até implementação futura |

```yaml
# Forma simples (recomendada)
execution_strategy: sequential

# Forma expandida (futuro)
execution_strategy:
  workstreams: sequential
  capabilities: sequential
```

**EXEC-STRAT-01:** omitir `execution_strategy` equivale a `sequential`. Nenhuma Feature existente precisa declarar o campo.

---

# Estado Agregado (STATE-AGG-01)

O Orchestrator **calcula** o estado da Feature a partir dos Workstreams registrados.

## Algoritmo de agregação de fase

```text
Para cada Workstream W da Feature F (ordenado por W.order):

1. Ler W.state.phase de construction-state.yaml

2. Fase agregada F.phase:
   - Se TODOS W.phase = closed        → F.phase = closed
   - Se ALGUM W.phase = execution     → F.phase = execution
   - Se ALGUM W.phase = session         → F.phase = session
   - Se ALGUM W.phase = closure         → F.phase = closure
   - Se ALGUM W.phase = not_started     → F.phase = not_started (e predecessors closed)

3. Closure agregado F.closure:
   - FEATURE_APPROVED somente se TODOS W.closure.final_state = FEATURE_APPROVED

4. PKG ativo agregado:
   - Primeiro Workstream com phase ∈ {session, execution} e current_pkg ≠ null
   - Dentro dele: current_pkg do Workstream State

5. Métricas agregadas:
   - pkgs_completed = Σ W.pkgs_completed
   - pkgs_total = Σ W.pkgs_total
```

## Proibições

- Não criar `feature-state.yaml` em Features já encerradas sem solicitação explícita.
- Não duplicar `phase` ou `current_pkg` em manifestos — Workstream State é SSOT local.
- Não inferir progresso sem consultar cada `construction-state.yaml` (STATE-WS-01).

---

# Registry Unificado

**Arquivo:** `construction/registry.yaml`

Substitui como **fonte preferencial** os índices dispersos:

| Legado | Status v4.0 |
|--------|-------------|
| `construction/features/registry.yaml` | Alias — mantido para retrocompatibilidade |
| `construction/frontend/registry.yaml` | Alias — mantido para retrocompatibilidade |
| `construction/registry.yaml` | **SSOD de índice global** |

## Conteúdo do registry

```yaml
foundations:   # platform-foundation, frontend-foundation
features:      # FT-<DOMAIN>
  - code: FT-SINGULAR
    current_capability: null   # v4.1 — null quando sem capabilities ou encerrada
    workstreams:
      - id: backend
        type: backend
        order: 1
        state: features/FT-SINGULAR/construction-state.yaml
      - id: frontend
        type: frontend
        order: 2
        depends_on_workstreams: [backend]
```

### Current Capability (v4.1)

| Campo | Descrição |
|-------|-----------|
| `current_capability` | `id` da Capability em execução; `null` quando ausente, não aplicável ou Feature encerrada |
| Fonte | Computado pelo Orchestrator a partir de manifest + states (CAP-02) |
| Obrigatório | Não — ausência equivale a `null` (RETRO-01) |

---

# Comandos Oficiais (v4.1)

## Ponto de entrada do usuário (CMD-FEATURE-01)

O usuário interage **exclusivamente** com a Feature. O Orchestrator resolve internamente:

- Workstream ativo
- Capability atual (quando declarada)
- PKG em execução
- Dependências e handoff
- Estado agregado

```text
Usuário                          Orchestrator (interno)
────────                         ──────────────────────
Execute FT-<FEATURE>      →      Sessão | PKG | primeiro passo
Continue FT-<FEATURE>     →      Retomar PKG/Workstream ativo
Review FT-<FEATURE>       →      Review/Audit do Workstream ou Feature
Close FT-<FEATURE>        →      Encerramento (todos Workstreams)
```

| Comando | Comportamento |
|---------|---------------|
| `Execute FT-<FEATURE>` | Inicia ou executa próximo passo — Orchestrator resolve Workstream/Capability/PKG (§8) |
| `Continue FT-<FEATURE>` | Equivalente operacional a `Execute` quando Feature já em `session` ou `execution` |
| `Review FT-<FEATURE>` | Dispara fase de review do Workstream ativo ou consolidação pré-encerramento |
| `Close FT-<FEATURE>` | Encerra Workstreams pendentes + consolida Feature (alias: `Encerrar Feature`) |

## Comandos internos (Orchestrator — não expostos ao usuário)

O Orchestrator emite internamente conforme algoritmo §8:

| Comando interno | Quando |
|-----------------|--------|
| `Execute Feature <CODE>` (Workstream) | Sessão de Workstream |
| `Execute PKG-XX FT-<FEATURE>` | PKG explícito resolvido |
| `Execute PKG-FE-XX FT-<FEATURE>` | PKG frontend |
| `Encerrar Workstream` | Workstream sem PKGs pendentes |

## Comandos legados (RETRO-01)

| Comando | Status |
|---------|--------|
| `Execute PKG-XX FT-<FEATURE>` | Válido — usuário avançado ou diagnóstico |
| `Execute Feature <FEATURE_CODE>` | **Legado v3.2** — equivalente a iniciar sessão do Workstream ativo |
| `Encerrar Feature <FEATURE_CODE>` | Alias de `Close FT-<FEATURE>` |

---

# Algoritmo do Orchestrator — `Execute FT-<FEATURE>` / `Continue FT-<FEATURE>`

```text
1. REGISTRY-01
   Ler construction/registry.yaml → localizar Feature FT-X

2. SSOD-01
   Ler construction/features/FT-X/feature-manifest.yaml
   Se capabilities[] presente → identificar current_capability (CAP-02)
   Ler execution_strategy (padrão: sequential se ausente)

3. STATE-AGG-01
   Para cada Workstream em registry.features[FT-X].workstreams:
     Ler construction-state.yaml do Workstream
     Coletar phase, current_pkg, completed, pending, closure

4. Validar dependências (HANDOFF-01)
   Para cada dependência em manifest.dependencies:
     Verificar Feature predecessora closed + FEATURE_APPROVED no registry

5. Selecionar Workstream ativo (WS-ORDER-01)
   W* = primeiro Workstream onde:
     - predecessors (depends_on_workstreams) estão closed
     - phase ∉ {closed}
   Se nenhum → Feature encerrada → sugerir handoff / próxima Feature

6. Decidir ação em W*
   a) phase = not_started → Execute Feature (sessão) em W*
   b) phase = session → completar sessão → execution
   c) phase = execution + current_pkg → Execute PKG (current_pkg) em W*
   d) phase = execution + current_pkg = null + pending vazio → Encerrar Workstream W*
   e) phase = closure → Encerrar Workstream W*

7. Emitir comando resolvido (interno — CMD-FEATURE-01)
   Exemplo: "Execute PKG-FE-04 FT-SINGULAR" ou "Execute Feature FT-AREA (backend)"
   Atualizar registry.current_capability quando aplicável (CAP-02)

Nota: `Continue FT-<FEATURE>` reutiliza este algoritmo — não há ramo distinto no Orchestrator v4.1.
```

## Roteamento de PKG por prefixo (WS-ROUTING-01)

| Prefixo do PKG | Workstream |
|----------------|------------|
| `pkg-fe-s0-` | frontend-foundation |
| `pkg-fe-` | frontend |
| `pkg-` | backend ou platform-foundation |

---

# Handoff entre Features (HANDOFF-01)

O handoff utiliza dependências **já existentes** nos manifests — sem novo modelo paralelo.

## Pré-condições de handoff

Para Feature sucessora `FT-B` iniciar Workstream `W`:

| Critério | Fonte |
|----------|-------|
| Specification APPROVED | `specs/features/<slug>/` |
| DoR satisfeito | `execution-plan.md` + STATE |
| Dependências `FEATURE_APPROVED` | `manifest.dependencies.features` |
| Workstreams predecessoras closed | `registry.yaml` + STATE-AGG-01 |
| Fundações aprovadas | `registry.foundations` |

## Protocolo de handoff

```text
Feature predecessora FT-A → FEATURE_APPROVED (todos Workstreams closed)
        ↓
Registry atualizado (construction/registry.yaml)
        ↓
09-progress.md atualizado no encerramento
        ↓
Execute FT-B
        ↓
Orchestrator valida HANDOFF-01
        ↓
Inicia Workstream order=1 de FT-B
```

## Exemplo — FT-SINGULAR frontend

```text
Pré-requisitos (já documentados em construction-state frontend):
  - frontend-foundation: FEATURE_APPROVED
  - FT-AUTH frontend: FEATURE_APPROVED
  - FT-SINGULAR backend: FEATURE_APPROVED
  - API contract: APPROVED

Handoff: backend Workstream closed → frontend Workstream liberado (order: 2)
```

---

# Retrocompatibilidade (RETRO-01)

## Preservado sem alteração

| Item | Status |
|------|--------|
| `construction/features/FT-*/` | Paths backend — inalterados |
| `construction/frontend/features/FT-*/` | Paths frontend — inalterados |
| `construction-state.yaml` por Workstream | SSOT local — inalterado |
| `feature-manifest.yaml` existentes | SSOT — inalterados |
| Comandos `Execute PKG-XX` | Válidos |
| Workflow v3.2 (`11-feature-execution-workflow.md`) | Válido por Workstream |
| Features FEATURE_APPROVED | Estado preservado |

## Evolução incremental (novas Features)

Novas Features **podem** adotar:

- Seção `workstreams:` no manifest v4 (`templates/feature-manifest-v4.yaml`)
- Seção `capabilities:` para agrupamento funcional *(v4.1)*
- `execution_strategy:` para declarar preferência de execução *(v4.1)*
- Registro único em `construction/registry.yaml`
- Workstreams adicionais (cms, mobile) sem mover backend/frontend

## Mapeamento v4.0 → v4.1

| v4.0 | v4.1 |
|------|------|
| Sem capabilities | `capabilities[]` opcional |
| WS-ORDER-01 implícito | `execution_strategy: sequential` explícito (equivalente) |
| Comandos mistos usuário/Orchestrator | CMD-FEATURE-01 — entrada exclusiva Feature |
| Registry sem capability | `current_capability: null` |

## Mapeamento v3.2 → v4.0

| v3.2 | v4.0 |
|------|------|
| Feature backend only | Feature com 1 Workstream (`backend`) |
| Feature backend + frontend separados | Feature com 2+ Workstreams |
| `construction/features/registry.yaml` | `construction/registry.yaml` |
| `Execute Feature FT-X` | `Execute FT-X` (resolve Workstream) |
| Estado implícito global | STATE-AGG-01 computado |

---

# Fluxo Full Stack

```text
Execute FT-SINGULAR
        ↓
registry.yaml + feature-manifest.yaml (SSOD)
        ↓
Agregar estados dos Workstreams (backend + frontend)
        ↓
┌─────────────────────────────────────────┐
│ Workstream backend: closed              │
│ Workstream frontend: closed             │
│ → Feature FT-SINGULAR: FEATURE_APPROVED │
└─────────────────────────────────────────┘
        ↓
Handoff → próxima Feature consumidora (ex.: FT-AREA frontend)
```

Para Feature em andamento:

```text
Execute FT-<FEATURE>
        ↓
Workstream ativo = backend (order 1)
        ↓
Execute PKG-03 (current_pkg)
        ↓
…
Encerrar Workstream backend
        ↓
Execute FT-<FEATURE>  → resolve Workstream frontend (order 2)
        ↓
Execute PKG-FE-01 … PKG-FE-N
        ↓
Encerrar Feature (todos Workstreams closed)
```

---

# Validação — FT-SINGULAR como Golden Template

| Critério | Evidência |
|----------|-----------|
| Feature com 2 Workstreams | backend (6 PKGs) + frontend (6 PKGs) |
| Manifest SSOT com link frontend | `construction/features/FT-SINGULAR/feature-manifest.yaml` |
| Sem capabilities (v4.1 opcional) | Manifest v2 — `current_capability: null` no registry |
| execution_strategy ausente | Comportamento padrão `sequential` (EXEC-STRAT-01) |
| CMD-FEATURE-01 | `Execute FT-SINGULAR` resolve Workstreams sem exposição interna |
| States independentes | Dois `construction-state.yaml` — ambos `closed` |
| STATE-AGG-01 | backend closed + frontend closed → FEATURE_APPROVED |
| Registry unificado | `construction/registry.yaml` — `golden: true`, `current_capability: null` |
| Retrocompatibilidade | Nenhum artefato FT-SINGULAR alterado nesta evolução |

Documentação detalhada: `construction/golden-template/FT-SINGULAR.md`

---

# Referências

- `construction/11-feature-execution-workflow.md` — workflow por Workstream (v3.2)
- `construction/registry.yaml` — índice unificado v4.0
- `construction/golden-template/FT-SINGULAR.md` — Golden Template Full Stack
- `construction/templates/feature-manifest-v4.yaml` — template novas Features
- `construction/templates/workstream-state.yaml` — template state por Workstream
- `construction/13-framework-decisions-v4.md` — decisões arquiteturais v4.0
- `construction/14-framework-decisions-v4.1.md` — decisões arquiteturais v4.1
- `.cursor/orchestrator/construction-orchestrator.mdc` — §20 Full Stack Orchestrator, §21 v4.1

---

# Critérios de Aceitação v4.1

- [x] Capabilities opcionais no Feature Manifest (CAP-01)
- [x] Execution Strategy com padrão `sequential` (EXEC-STRAT-01)
- [x] Registry com `current_capability` (CAP-02)
- [x] Ponto de entrada exclusivo Feature (CMD-FEATURE-01)
- [x] Comandos `Execute`, `Continue`, `Review`, `Close FT-*`
- [x] Retrocompatibilidade total v4.0 e v3.2 (RETRO-01)
- [x] FT-SINGULAR validado sem migração obrigatória

## Critérios de Aceitação v4.0 (baseline preservada)

- [x] Feature como unidade de construção
- [x] Workstream como modelo genérico extensível
- [x] Estado agregado computado (STATE-AGG-01)
- [x] Feature Manifest SSOT preservado
- [x] Registry unificado com Workstreams
- [x] Algoritmo `Execute FT-<FEATURE>`
- [x] Handoff formalizado via dependências existentes
- [x] Retrocompatibilidade v3.2
- [x] FT-SINGULAR validado como Golden Template
