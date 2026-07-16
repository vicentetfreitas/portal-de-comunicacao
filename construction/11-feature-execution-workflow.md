# Feature Execution Workflow — Engineering Framework

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Framework | **Engineering Framework** |
| Camada | Construction |
| Tipo | **SSOT — Workflow de Execução** |
| Status | **Stable** |
| Versão | **3.2** (+ **v4.0 / v4.1 Full Stack** — ver `12-fullstack-orchestrator.md`) |
| Última atualização | 2026-07-16 |
| Changelog | `construction/CHANGELOG.md` |

---

# Objetivo

Estabelecer o padrão **permanente** de execução orientado à Feature para todas as construções futuras do projeto.

Esta versão evolui o workflow v3.1 com:

- **Feature Identity** — identificador oficial `FT-<DOMAIN>` como chave estável
- **SSOD** — `feature-manifest.yaml` como Single Source of Discovery
- **Dois níveis de Construction State** — Platform Foundation (histórico) e Features (`construction/features/<FEATURE_CODE>/`)
- **Construction State** como SSOT do estado operacional
- Session imutável, Snapshot estruturado, cache com invalidação formal
- Build incremental e PKGs independentes

**Status Stable (v3.2):** validado com **FEATURE_APPROVED** da FT-AUTH. Evoluído para **v4.0 Full Stack Orchestrator** e refinado em **v4.1** (Capabilities, CMD-FEATURE-01) — este documento permanece SSOT **por Workstream**; agregação Feature em `construction/12-fullstack-orchestrator.md`.

---

# Relação com v4.0 / v4.1 Full Stack

| v3.2 (este documento) | v4.0 / v4.1 |
|----------------------|------|
| Unidade de execução = Feature path | Unidade de execução = **Workstream** dentro da Feature |
| `construction-state.yaml` por path | `construction-state.yaml` = **Workstream State** |
| `Execute Feature <CODE>` | `Execute FT-<CODE>` resolve Workstream ativo (v4.1: entrada exclusiva Feature) |
| Registry por camada | `construction/registry.yaml` unificado (+ `current_capability` v4.1) |

O workflow de três fases (Sessão → PKGs → Encerramento) aplica-se **a cada Workstream**. Capabilities (v4.1) organizam funcionalidades **sem alterar** este lifecycle.

---

# Princípio Fundamental

| Unidade | Papel |
|---------|-------|
| **Feature Code** | Identificador oficial estável (`FT-AUTH`) — chave de diretório e comandos |
| **Feature Manifest (SSOD)** | **Single Source of Discovery** — localização de todos os artefatos |
| **Construction State (SSOT)** | Estado operacional — fase, PKG ativo, progresso, review, audit |
| **PKG** | Implementação focada, testes locais, histórico em `status.md` |

---

# Regras de Governança do Framework

| ID | Regra | Descrição |
|----|-------|-----------|
| **RULE-01** | Specification Approved | Nenhuma Feature inicia sem specification aprovada (Definition of Ready atendida) |
| **RULE-02** | Framework congelado | O Engineering Framework permanece congelado durante a execução da Feature. Evoluções somente entre Features ou Sprints |
| **RULE-03** | Spec ↔ API alinhados | Nenhuma implementação inicia enquanto existir divergência entre Specification e API Contract |
| **RULE-04** | Retrospective obrigatória | Toda Feature **FEATURE_APPROVED** gera Sprint Retrospective antes da próxima Feature. Alterações no framework somente a partir dessas evidências e fora de Feature em andamento |

## Evolução futura do framework

Mudanças no Engineering Framework devem ser motivadas por **necessidades recorrentes em múltiplas Features**, documentadas em retrospectivas — não por ajustes pontuais de uma única implementação.

```text
FEATURE_APPROVED → Sprint Retrospective → evidências → planejamento entre Sprints → CHANGELOG
```

---

# Feature Identity

## Convenção oficial

| Aspecto | Padrão | Exemplo |
|---------|--------|---------|
| Formato | `FT-<DOMAIN>` | `FT-AUTH`, `FT-DOCUMENTS` |
| Caixa | MAIÚSCULAS | `FT-AUTH` |
| Diretório | `construction/features/<FEATURE_CODE>/` | `construction/features/FT-AUTH/` |
| Comando | `Execute Feature <FEATURE_CODE>` | `Execute Feature FT-AUTH` |
| Slug (specs) | minúsculas | `authentication` → `specs/features/authentication/` |

Índice global: `construction/features/registry.yaml`

---

# Feature Manifest (SSOD)

O `feature-manifest.yaml` é o **Single Source of Discovery** — ponto de entrada obrigatório para localizar artefatos.

## Fluxo de descoberta (SSOD-01)

```text
registry.yaml (opcional) → feature-manifest.yaml → artefatos descobertos → construction-state.yaml (estado)
```

Nenhum agente deve usar paths hardcoded. Todo processo inicia no manifesto.

## Artefatos descobertos via manifesto

| Seção | Descobre |
|-------|----------|
| `specification.path` | Especificação SDD |
| `construction.path` | Raiz da construção |
| `artifacts.*` | State, execution plan, session, review, reports, PKGs |
| `implementation.*` | Código backend/frontend |
| `dependencies.*` | Platform Foundation e outras Features |
| `metrics.*` | Contadores para dashboards |

Template: `construction/templates/feature-manifest.yaml` (v2, role: ssod)

---

# Construction State (SSOT)

Artefato oficial do estado de execução — **um arquivo por unidade de construção**:

| Unidade | Caminho do State |
|---------|------------------|
| Platform Foundation (Sprint 1A) | `construction/platform-foundation/construction-state.yaml` |
| Feature de negócio | `construction/features/<FEATURE_CODE>/construction-state.yaml` |

Template: `construction/templates/construction-state.yaml` (v3)

O estado da Platform Foundation permanece **encerrado** (`phase: closed`) como registro histórico. Features de negócio possuem ciclo independente — o andamento de FT-AUTH **não** altera o state da fundação.

O Construction State responde imediatamente:

- qual Feature está em execução;
- qual fase está ativa;
- qual PKG está em execução;
- quais PKGs foram concluídos ou permanecem pendentes;
- qual versão do Snapshot está ativa;
- se o cache é válido;
- estado de Review, Audit, Build, Readiness e Closure.

**Consulta obrigatória antes de qualquer outro artefato operacional** (STATE-02).

---

# Regras Formais

| ID | Regra | Descrição |
|----|-------|-----------|
| SSOD-01 | Manifesto primeiro | Consultar `feature-manifest.yaml` antes de qualquer artefato — sem paths hardcoded |
| STATE-01 | Construction State é SSOT | Estado operacional exclusivamente em `construction-state.yaml` |
| STATE-02 | Consulta de estado | Consultar Construction State para fase, PKG ativo, progresso |
| STATE-03 | Manifest = SSOD | `feature-manifest.yaml` descobre artefatos — nunca inferir paths |
| STATE-04 | Session = contexto | `session.md` é Snapshot imutável — nunca progresso |
| STATE-05 | PKG Status = histórico | `pkg-XX/status.md` detalha o PKG — nunca estado global |
| STATE-06 | Resultados finais | Review, Audit e Closure refletem resultado final; estado corrente no State |
| SESSION-01 | Session imutável | `session.md` é READ ONLY após criação; somente `Execute Feature` cria/recria |
| CACHE-01 | Cache de contexto | Não reler documento se informação já está no Snapshot |
| CACHE-02 | Reutilização obrigatória | Sem evento de invalidação, reutilizar Session ativa |
| BUILD-01 | Build incremental | `mvn clean verify` somente no Encerramento |
| VAL-01 | Validation Summary | Resumo único em `pkg-XX/status.md`; logs em `evidence/` — ver `templates/pkg-validation-summary.md` |
| PARALLEL-01 | Independência de PKGs | Cada PKG altera `construction-state.yaml` e `pkg-XX/status.md` |
| RULE-CONTEXT-01 | Hierarquia de consulta | SSOD → State → Snapshot → Cache → documento |

---

# Novo Fluxo

```text
Execute Feature <FEATURE_CODE>
        ↓
Consultar feature-manifest.yaml (SSOD)
        ↓
Atualizar construction-state.yaml (phase, snapshot, cache)
        ↓
Carregar documentos (paths do Manifest)
        ↓
Criar Snapshot → Congelar Session (imutável)
        ↓
Execute PKG-01 → … → PKG-N
        ↓
Closure → Review → Audit → Readiness
```

---

# Modelo em Três Fases

```text
┌─────────────────────────────────────────────────────────┐
│  FASE 1 — Sessão da Feature          (uma vez)          │
│  SSOD → State → carregar → Snapshot → session.md        │
└───────────────────────────┬─────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│  FASE 2 — Execução dos PKGs          (por PKG)          │
│  State → Snapshot/Cache → código → testes → status.md   │
└───────────────────────────┬─────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│  FASE 3 — Encerramento da Feature    (uma vez)          │
│  State · Build completo · review · audit · relatório    │
└─────────────────────────────────────────────────────────┘
```

---

# Fase 1 — Sessão da Feature

## Quando

Uma única vez, antes do primeiro PKG — ou quando ocorrer evento de invalidação (CACHE-02).

## Entrada

Comando: `Execute Feature <nome>`

## Atividades

1. Atualizar `construction/features/<FEATURE_CODE>/construction-state.yaml` — `phase: session`, snapshot, cache
2. Localizar `construction/features/<FEATURE_CODE>/feature-manifest.yaml`
3. Carregar artefatos **exclusivamente** via Manifest — **sem exploração da árvore**
4. Validar Definition of Ready
5. Validar dependências entre PKGs
6. Montar **Snapshot estruturado** (visão condensada)
7. Produzir e **congelar** `session.md` (SESSION-01)
8. Atualizar `construction-state.yaml` — `phase: execution`, `current_pkg`, `pending`

## Manifesto obrigatório

```text
construction/features/<FEATURE_CODE>/feature-manifest.yaml
```

Template: `construction/templates/feature-manifest.yaml`

O Manifesto é a **única fonte oficial** para localização de artefatos. O agente nunca deve procurar documentos por exploração.

## Snapshot estruturado

O Snapshot em `session.md` deve conter:

```text
Feature · Objetivos · Premissas · Restrições · Contratos
Dependências · Decisões · PKGs · Artefatos · Riscos · Pendências
```

O agente consulta **preferencialmente** o Snapshot antes de abrir qualquer documento.

## Saída obrigatória

```text
construction/features/<FEATURE_CODE>/construction-state.yaml   ← SSOT estado operacional
construction/features/<FEATURE_CODE>/session.md                ← Snapshot congelado (READ ONLY)
```

Templates: `construction/templates/construction-state.yaml`, `construction/templates/feature-session.md`

## Estado da Feature

Registrado em `construction-state.yaml`:

```yaml
state:
  phase: execution
snapshot:
  immutable: true
cache:
  valid: true
```

PKGs podem ser executados somente com `phase: execution` e cache válido.

## Proibições

- iniciar PKG sem `session.md` concluída
- modificar `session.md` durante execução de PKGs
- explorar repositório fora do Manifest

---

# Cache de Contexto

Hierarquia obrigatória durante uma Feature:

```text
Construction State → Snapshot → Cache → Documento
```

Antes de abrir qualquer arquivo:

```text
A informação já existe no Construction State ou no Snapshot?
```

| Resposta | Ação |
|----------|------|
| SIM (State) | Não inferir estado de outros artefatos |
| SIM (Snapshot) | Não abrir o arquivo |
| NÃO | Abrir apenas o documento necessário |

### RULE-CONTEXT-01

Nenhum PKG pode abrir, interpretar ou explorar novamente documentos já carregados na Session ativa.

Ordem de consulta:

1. Construction State (`construction-state.yaml`)
2. Snapshot (`session.md`)
3. Cache de contexto interno
4. Documento adicional (somente se ausente no Snapshot)

Exploração da árvore da Feature durante PKG é **proibida**, salvo invalidação da Session.

---

# Invalidação da Session (CACHE-02)

A Session somente pode ser invalidada quando ocorrer **pelo menos um** dos eventos:

- alteração da specification
- alteração da api
- alteração dos use cases
- alteração do domain
- alteração das decisões
- alteração de ADR relacionada
- inclusão de novo PKG
- remoção de PKG
- alteração do Manifest

Caso contrário: **reutilizar obrigatoriamente** a Session existente.

---

# Fase 2 — Execução dos PKGs

## Quando

Após Sessão da Feature ativa. Repetido por PKG.

## Entrada

Comando: `Execute PKG-XX`

Pré-requisito: `construction-state.yaml` com `phase: execution` e cache válido; `session.md` existente e **não invalidada**.

## Responsabilidades

| Atividade | Permitido |
|-----------|-----------|
| Consultar Construction State | ✅ |
| Consultar Snapshot/Cache | ✅ |
| Implementar código | ✅ |
| Implementar testes | ✅ |
| Validações locais incrementais | ✅ |
| Atualizar `construction-state.yaml` | ✅ |
| Atualizar `pkg-XX/status.md` | ✅ |

## Artefatos de escrita obrigatórios

```text
construction/features/<FEATURE_CODE>/construction-state.yaml   ← estado operacional (SSOT)
construction/features/<FEATURE_CODE>/pkg-XX/status.md          ← histórico detalhado do PKG
```

Templates: `construction/templates/construction-state.yaml`, `construction/templates/pkg-status.md`, `construction/templates/pkg-validation-summary.md`

### Atualizações no Construction State (por PKG)

Ao concluir um PKG:

```yaml
state:
  current_pkg: pkg-XX+1   # próximo PKG ou null se último
  completed: [..., pkg-XX]
  pending: [pkg-XX+1, ...]
metadata:
  updated_at: <timestamp>
```

## Validações locais permitidas (BUILD-01)

```text
mvn test
mvn verify -pl backend
mvn test -Dtest=<classe ou pacote>
mvn -pl backend -am test
mvn compile
```

## Proibido durante PKG

```text
mvn clean verify    → reservado exclusivamente ao Encerramento
```

## Proibições (PARALLEL-01)

| Proibido durante PKG | Responsável |
|---------------------|-------------|
| Modificar `session.md` | SESSION-01 / STATE-04 |
| Inferir estado sem consultar `construction-state.yaml` | STATE-02 |
| Atualizar `09-progress.md` | Encerramento |
| Atualizar `review.md` | Encerramento |
| Atualizar progress, traceability, changelog | Encerramento |
| Gerar `closure-report.md` | Encerramento |
| Executar `reviewer` ou `auditor` | Encerramento |
| Explorar árvore da Feature | RULE-CONTEXT-01 |
| Reler documentos no Snapshot | CACHE-01 |

## Estado do PKG

Estado global em `construction-state.yaml`. Detalhamento em `pkg-XX/status.md`:

| Estado | Significado |
|--------|-------------|
| `NOT_STARTED` | Não iniciado (em `pending`) |
| `IN_PROGRESS` | Em implementação (`current_pkg`) |
| `DONE` | Implementação e testes locais concluídos (em `completed`) |
| `BLOCKED` | Bloqueio técnico — motivo em `status.md` |

**Nota:** `APPROVED` é atribuído apenas no Encerramento, após review e audit consolidados.

## Saída do PKG

**VALIDATION SUMMARY** em `pkg-XX/status.md` (VAL-01) + resumo operacional curto (3–8 linhas).

Não relatório extenso de validação no arquivo principal. Logs completos em `pkg-XX/evidence/`.

Template: `construction/templates/pkg-validation-summary.md`

---

# Fase 3 — Encerramento da Feature

## Quando

Uma única vez, após o último PKG em estado `DONE`.

## Entrada

Comando: `Encerrar Feature <nome>`

## Sequência

```text
Closure → Review → Audit → Readiness
```

## Atualizações consolidadas

| Documento | Ação |
|-----------|------|
| `construction/features/<FEATURE_CODE>/construction-state.yaml` | Estado final — `phase: closed` |
| `construction/09-progress.md` | Atualizar estados finais |
| `construction/features/<FEATURE_CODE>/review/` ou `review/*.md` | Preencher checklists |
| Traceability | Consolidar rastreabilidade |
| Changelog | Registrar alterações |
| Métricas | Atualizar contadores |
| Readiness | Executar checklist completo |

Estado final em `construction-state.yaml`:

```yaml
state:
  phase: closed
review:
  status: done
audit:
  status: done
readiness:
  status: approved
closure:
  status: done
build:
  full_verify: success
```

## Validações completas (BUILD-01)

```text
mvn clean verify          ← exclusivo desta fase
testes completos
testes de integração
cobertura (meta do projeto)
validações SDD (DoD)
```

## Auditorias (consolidadas)

| Agente | Escopo |
|--------|--------|
| `reviewer` | Revisão técnica de todos os PKGs |
| `auditor` | Auditoria de conformidade consolidada |

## Relatório final

```text
construction/features/<FEATURE_CODE>/closure-report.md
```

Template: `construction/templates/feature-closure-report.md`

## Estado final

`FEATURE_APPROVED` — registrado em `construction-state.yaml` com `phase: closed`.

---

# Estrutura de Diretórios

## Platform Foundation (histórico — Sprint 1A)

```text
construction/platform-foundation/
├── construction-state.yaml      ← SSOT (phase: closed — não alterar)
├── feature-manifest.yaml
├── session.md
├── pkg-01/ … pkg-08/
│   └── status.md
└── …
```

Relatórios consolidados da Sprint 1A: `construction/review/` (não mover).

## Feature de negócio (padrão a partir de Sprint 1)

```text
construction/features/<FEATURE_CODE>/
├── construction-state.yaml      ← SSOT estado operacional
├── feature-manifest.yaml        ← estrutura da Feature
├── execution-plan.md            ← ponto de entrada da execução
├── session.md                   ← Snapshot congelado (READ ONLY)
├── pkg-01/
│   └── status.md
├── review/                      ← auditoria e prontidão da Feature
├── reports/                     ← relatórios da Feature
└── closure-report.md
```

---

# Relação entre Artefatos

| Artefato | Responsabilidade | Estado operacional |
|----------|------------------|-------------------|
| `feature-manifest.yaml` | **SSOD** — descoberta de todos os artefatos | ❌ |
| `construction-state.yaml` | Fase, PKG ativo, progresso, review, audit, build | ✅ SSOT |
| `session.md` | Snapshot de contexto (imutável) | ❌ |
| `pkg-XX/status.md` | Histórico detalhado do PKG | ❌ (apenas local) |
| `review.md` | Resultado final do review | ❌ (resultado final) |
| `closure-report.md` | Relatório de encerramento | ❌ (resultado final) |
| `09-progress.md` | Progresso global consolidado | ❌ (encerramento) |

---

# Comandos Oficiais

| Comando | Fase | Descrição |
|---------|------|-----------|
| `Execute FT-<FEATURE>` | 1/2 | **v4.0** — Orchestrator resolve Workstream + PKG/sessão |
| `Execute Feature <FEATURE_CODE>` | 1 | SSOD → State → Snapshot → Session congelada (Workstream ativo) |
| `Execute PKG-XX [FT-<FEATURE>]` | 2 | PKG focado (roteamento por prefixo — WS-ROUTING-01) |
| `Encerrar Feature <FEATURE_CODE>` | 3 | State, closure, review, audit, build completo |

---

# Migração v3.1 → v3.2

| v3.1 | v3.2 |
|------|------|
| Diretório por slug (`authentication/`) | Diretório por code (`FT-AUTH/`) |
| Manifest como localizador | Manifest como **SSOD** completo |
| Paths dispersos em docs | Descoberta centralizada em `feature-manifest.yaml` |
| Sem registry | `construction/features/registry.yaml` |

---

# Migração v3.0 → v3.1

| v3.0 | v3.1 |
|------|------|
| Um único padrão `construction/<feature>/` | Dois níveis: `platform-foundation/` (histórico) e `features/<id>/` (negócio) |
| Estado global compartilhado | Construction State independente por Feature |
| Review centralizado em `construction/review/` (Sprint 1A) | Review por Feature em `features/<id>/review/` |
| Sem execution-plan por Feature | `execution-plan.md` como ponto de entrada |

---

# Migração v2.0 → v3.0

| v2.0 | v3.0 |
|------|------|
| Estado distribuído (session, status, progress) | `construction-state.yaml` como SSOT |
| `session.md` com progresso de PKGs | Session exclusivamente Snapshot (STATE-04) |
| Consulta Snapshot para estado | Consulta Construction State primeiro (STATE-02) |
| PKG altera apenas `status.md` | PKG altera `construction-state.yaml` + `status.md` |

---

# Migração v1.0 → v2.0

| v1.0 | v2.0 |
|------|------|
| Session mutável / progresso | Session imutável — apenas conhecimento |
| Exploração de artefatos | Navegação exclusiva via Manifest |
| Contexto em tabelas livres | Snapshot estruturado |
| Cache informal | CACHE-01, CACHE-02, RULE-CONTEXT-01 |
| Build completo com exceção | BUILD-01 — incremental obrigatório |
| PKGs com escrita compartilhada | PARALLEL-01 — apenas `status.md` |

---

# Critérios de Aceitação deste Workflow

- [x] Feature Identity (`FT-<DOMAIN>`) como chave oficial
- [x] `feature-manifest.yaml` como SSOD (SSOD-01)
- [x] `registry.yaml` para descoberta em escala
- [x] Dois níveis de Construction State (Platform Foundation + Features)
- [x] `execution-plan.md`, `review/`, `reports/` por Feature
- [x] `construction-state.yaml` como SSOT do estado operacional (STATE-01)
- [x] `session.md` imutável — apenas Snapshot (SESSION-01, STATE-04)
- [x] Snapshot estruturado do contexto
- [x] Regras formais de cache e invalidação
- [x] PKGs reutilizam contexto carregado (RULE-CONTEXT-01)
- [x] Build completo apenas no encerramento (BUILD-01)
- [x] PKGs independentes para paralelização (PARALLEL-01)
- [x] Exploração repetitiva da árvore eliminada do workflow padrão
- [x] Agentes consultam Construction State antes de inferir progresso (STATE-02)

---

# Referências

- `.cursor/rules/workflows/feature-construction-workflow.mdc`
- `.cursor/orchestrator/construction-orchestrator.mdc`
- `construction/templates/feature-manifest.yaml`
- `construction/templates/construction-state.yaml`
- `construction/templates/feature-session.md`
- `construction/templates/pkg-status.md`
- `construction/templates/pkg-validation-summary.md`
- `construction/templates/pkg-implementation-report.md`
- `construction/templates/feature-closure-report.md`
- `construction/04-construction-rules.md` — regras R-12 a R-27, STATE-01 a STATE-06
