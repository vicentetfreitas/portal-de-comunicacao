# Integration Sprint Workflow — Engineering Framework

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Framework | **Engineering Framework — Integration Layer** |
| Camada | `engineering/integration/` |
| Tipo | **SSOT — Workflow de Integração** |
| Status | **Stable** |
| Versão | **1.0** |
| Última atualização | 2026-07-14 |
| Changelog | `engineering/CHANGELOG.md` |

---

# Objetivo

Estabelecer o padrão **permanente** de execução da Sprint de Integração para validação do backend integrado após o encerramento de um conjunto de Features.

Esta Sprint:

- **não é** uma Feature;
- **não altera** código, specs ou APIs durante a fase documental;
- **não possui** orquestrador nem automação nesta versão;
- **é reutilizável** por qualquer projeto construído com o Construction Framework.

---

# Princípio Fundamental

| Unidade | Papel |
|---------|-------|
| **Sprint ID** | Identificador estável (`sprint-03-org-backend`) — chave de diretório |
| **Integration Manifest (SSOD)** | **Single Source of Discovery** — localização de artefatos e escopo |
| **Integration State (SSOT)** | Estado operacional — fase, progresso, métricas, evidências |
| **Fase de Validação** | Bloco temático executável (ENV, INF, API, DB, XFT, FUN, OUT) |
| **Checklist Item** | Unidade mínima de validação com ID, critério, evidência e status |

---

# Regras de Governança

| ID | Regra | Descrição |
|----|-------|-----------|
| **RULE-INT-01** | Features encerradas | Nenhuma Sprint de Integração inicia sem todas as Features do escopo em `FEATURE_APPROVED` |
| **RULE-INT-02** | Sem alteração de código | Durante a Sprint de Integração documental, não alterar código, specs, APIs nem Features |
| **RULE-INT-03** | Evidência obrigatória | Nenhum item do checklist é aprovado sem evidência registrada |
| **RULE-INT-04** | Manual primeiro | Processo executado manualmente; automação somente após consolidação e aprovação do framework |
| **RULE-INT-05** | Issues rastreadas | Toda não-conformidade registrada em `issues.md` com severidade e status |
| **RULE-INT-06** | Framework congelado | O Integration Framework permanece congelado durante execução da sprint; evoluções entre sprints |

---

# Integration Identity

## Convenção oficial

| Aspecto | Padrão | Exemplo |
|---------|--------|---------|
| Formato | `sprint-<NN>-<scope>` | `sprint-03-org-backend` |
| Caixa | minúsculas, hífen | `sprint-03-org-backend` |
| Diretório | `engineering/integration/sprints/<SPRINT_ID>/` | `.../sprints/sprint-03-org-backend/` |
| Comando | `Execute Integration Sprint <SPRINT_ID>` | `Execute Integration Sprint sprint-03-org-backend` |

Índice global: `engineering/integration/registry.yaml`

---

# Integration Manifest (SSOD)

O `integration-manifest.yaml` é o **Single Source of Discovery** — ponto de entrada obrigatório.

## Fluxo de descoberta (SSOD-INT-01)

```text
registry.yaml (opcional) → integration-manifest.yaml → artefatos → integration-state.yaml (estado)
```

Nenhum agente deve usar paths hardcoded. Todo processo inicia no manifesto.

## Artefatos descobertos via manifesto

| Seção | Descobre |
|-------|----------|
| `scope.features` | Features no escopo da integração |
| `artifacts.*` | State, plan, checklist, report, readiness, issues |
| `validation.phases` | Fases de validação e seus IDs |
| `cross_feature_flows` | Fluxos end-to-end entre Features |
| `metrics.*` | Contadores para dashboards |

Template: `engineering/templates/integration-manifest.yaml`

---

# Integration State (SSOT)

Artefato oficial do estado de execução — **um arquivo por instância de sprint**:

| Unidade | Caminho |
|---------|---------|
| Sprint de Integração | `engineering/integration/sprints/<SPRINT_ID>/integration-state.yaml` |

Template: `engineering/templates/integration-state.yaml`

O Integration State responde imediatamente:

- fase atual (`phase`);
- fase de validação ativa;
- progresso do checklist;
- status de review, readiness e aprovação;
- métricas (itens totais, aprovados, pendentes, bloqueados).

---

# Ciclo de Vida

```text
PLANNED
    ↓  (critérios de entrada atendidos)
IN_PROGRESS
    ↓  (plano e checklist preparados; validação iniciada)
VALIDATION
    ↓  (todas as fases executadas; evidências registradas)
REVIEW
    ↓  (revisão técnica concluída)
READY
    ↓  (readiness aprovada)
APPROVED
    ↓  (relatório final publicado)
COMPLETED
```

Detalhamento: `04-state-machine.md`

---

# Fases de Validação

| ID | Fase | Escopo |
|----|------|--------|
| **ENV** | Ambiente | Aplicação, datasource, banco, actuator, logs, profiles, variáveis |
| **INF** | Infraestrutura | Autenticação, JWT, cookies, CORS, Security |
| **API** | APIs | CRUD, paginação, ordenação, filtros, status HTTP, mensagens |
| **DB** | Banco | FK, constraints, índices, auditoria, soft delete, relacionamentos |
| **XFT** | Cross-Feature | Fluxos completos entre Features encadeadas |
| **FUN** | Funcional | Regras de negócio, casos positivos e negativos |
| **OUT** | Resultado | Issues, riscos, pendências, aprovação |

Detalhamento: `03-phases-and-activities.md`

---

# Fluxo de Execução Manual

## 1. Planejamento (`PLANNED → IN_PROGRESS`)

1. Verificar critérios de entrada (`05-entry-exit-criteria.md`)
2. Consultar `integration-manifest.yaml` (SSOD-INT-01)
3. Preencher `integration-plan.md` com escopo, dependências e fluxos
4. Instanciar `integration-checklist.md` a partir do template
5. Atualizar `integration-state.yaml` → `phase: in_progress`

## 2. Validação (`IN_PROGRESS → VALIDATION`)

Para cada fase (ENV → INF → API → DB → XFT → FUN):

1. Executar itens do checklist da fase
2. Registrar evidência por item (comando, teste, log, screenshot, referência)
3. Marcar status: `PENDING` | `APPROVED` | `BLOCKED` | `WAIVED`
4. Registrar não-conformidades em `issues.md`
5. Atualizar métricas em `integration-state.yaml`

Transição para `VALIDATION` quando a primeira fase iniciar execução.

## 3. Consolidação (`VALIDATION → REVIEW`)

1. Verificar 100% dos itens Must com status definido
2. Produzir `integration-report.md` com resumo por fase
3. Atualizar `integration-state.yaml` → `phase: review`

## 4. Revisão (`REVIEW → READY`)

1. Revisor técnico valida evidências e issues
2. Confirma ou contesta itens `WAIVED`
3. Atualiza `integration-state.yaml` → `review.status: done`

## 5. Readiness (`READY → APPROVED`)

1. Executar `integration-readiness.md`
2. Verificar critérios de saída (`05-entry-exit-criteria.md`)
3. Decisão: `APPROVED` ou `REJECTED`
4. Atualizar `integration-state.yaml` → `phase: approved`

## 6. Encerramento (`APPROVED → COMPLETED`)

1. Publicar relatório final em `integration-report.md`
2. Atualizar `engineering/integration/registry.yaml`
3. Atualizar `construction/09-progress.md` (próximo passo)
4. Atualizar `integration-state.yaml` → `phase: completed`

---

# Papéis (execução manual)

| Papel | Responsabilidade |
|-------|------------------|
| **Integration Lead** | Coordena a sprint, atualiza state e checklist |
| **Validator** | Executa validações e registra evidências |
| **Reviewer** | Revisa evidências e contesta waivers |
| **Auditor** | Verifica conformidade com specs e framework |

> Nesta versão não há agentes nem orquestradores. Os papéis podem ser exercidos pela mesma pessoa.

---

# Artefatos por Sprint

| Artefato | Obrigatório | Papel |
|----------|-------------|-------|
| `integration-manifest.yaml` | Sim | SSOD — escopo e descoberta |
| `integration-state.yaml` | Sim | SSOT — estado operacional |
| `integration-plan.md` | Sim | Plano de escopo e fluxos |
| `integration-checklist.md` | Sim | Checklist executável |
| `integration-report.md` | Sim | Relatório consolidado |
| `integration-readiness.md` | Sim | Critérios de aprovação |
| `issues.md` | Sim | Registro de issues |

---

# Referências

- Regras: `02-integration-rules.md`
- Fases: `03-phases-and-activities.md`
- State machine: `04-state-machine.md`
- Entrada/saída: `05-entry-exit-criteria.md`
- Construction workflow: `construction/11-feature-execution-workflow.md`
