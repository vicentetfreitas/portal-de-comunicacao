# State Machine — Integration Sprint

| Item | Valor |
|------|-------|
| Camada | `engineering/integration/` |
| Versão | 1.0 |
| Status | Stable |

---

# Diagrama de Estados

```text
                    ┌─────────────┐
                    │   PLANNED   │
                    └──────┬──────┘
                           │ critérios de entrada OK
                           │ plano + checklist instanciados
                           ▼
                    ┌─────────────┐
                    │ IN_PROGRESS │
                    └──────┬──────┘
                           │ primeira fase em execução
                           ▼
                    ┌─────────────┐
                    │ VALIDATION  │◄────┐
                    └──────┬──────┘     │ revalidação
                           │ todas fases executadas     │
                           │ evidências registradas     │
                           ▼                            │
                    ┌─────────────┐                     │
                    │   REVIEW    │─────────────────────┘
                    └──────┬──────┘
                           │ review concluída
                           ▼
                    ┌─────────────┐
                    │    READY    │
                    └──────┬──────┘
                           │ readiness aprovada
                           ▼
                    ┌─────────────┐
                    │  APPROVED   │
                    └──────┬──────┘
                           │ relatório final publicado
                           ▼
                    ┌─────────────┐
                    │  COMPLETED  │
                    └─────────────┘
```

---

# Estados

| Estado | Descrição | Atividades permitidas |
|--------|-----------|----------------------|
| **PLANNED** | Sprint planejada, artefatos instanciados | Preencher plano, preparar checklist |
| **IN_PROGRESS** | Execução iniciada | Atualizar plano, iniciar validação |
| **VALIDATION** | Fases ENV–FUN em execução | Executar checklist, registrar evidências |
| **REVIEW** | Validação concluída, em revisão | Revisar evidências, contestar waivers |
| **READY** | Revisão OK, aguardando readiness | Executar readiness checklist |
| **APPROVED** | Integração aprovada | Publicar relatório final |
| **COMPLETED** | Sprint encerrada | Somente consulta |

---

# Critérios de Transição

## PLANNED → IN_PROGRESS

| Critério | Verificação |
|----------|-------------|
| Features do escopo `FEATURE_APPROVED` | `construction/features/registry.yaml` |
| Critérios de entrada atendidos | `05-entry-exit-criteria.md` |
| `integration-plan.md` preenchido | Escopo, fluxos, dependências |
| `integration-checklist.md` instanciado | Itens Must presentes |
| `integration-manifest.yaml` válido | SSOD-INT-01 |

## IN_PROGRESS → VALIDATION

| Critério | Verificação |
|----------|-------------|
| Primeira fase iniciada | Pelo menos 1 item com evidência |
| `integration-state.yaml` atualizado | `validation.active_phase` definida |

## VALIDATION → REVIEW

| Critério | Verificação |
|----------|-------------|
| Todas as fases executadas | ENV, INF, API, DB, XFT, FUN concluídas |
| 100% itens Must com status | Nenhum Must em `PENDING` |
| `integration-report.md` rascunho | Resumo por fase preenchido |
| Issues registradas | `issues.md` reflete findings |

## REVIEW → READY

| Critério | Verificação |
|----------|-------------|
| Revisão técnica concluída | `review.status: done` no state |
| Waivers aprovados | Justificativa e aprovador registrados |
| Issues CRITICAL resolvidas ou deferidas formalmente | `issues.md` |

## READY → APPROVED

| Critério | Verificação |
|----------|-------------|
| `integration-readiness.md` executado | Todos critérios Must ✅ |
| Zero issues CRITICAL abertas | `issues.md` |
| Métricas dentro do limiar | `metrics.blocked == 0` (Must) |

## APPROVED → COMPLETED

| Critério | Verificação |
|----------|-------------|
| `integration-report.md` final publicado | Versão final datada |
| `registry.yaml` atualizado | `status: completed` |
| `construction/09-progress.md` atualizado | Próximo passo definido |

---

# Retrocesso

| De | Para | Condição |
|----|------|----------|
| REVIEW | VALIDATION | Evidências insuficientes — registrar em report |
| READY | REVIEW | Readiness rejeitada |
| APPROVED | READY | Issue CRITICAL descoberta pós-aprovação (raro) |

Retrocesso exige entrada em `integration-report.md` com data, motivo e responsável.

---

# Campos de Estado (`integration-state.yaml`)

```yaml
state:
  phase: planned  # planned | in_progress | validation | review | ready | approved | completed

validation:
  active_phase: null  # ENV | INF | API | DB | XFT | FUN | OUT
  phases_completed: []

review:
  status: pending  # pending | in_progress | done

readiness:
  status: pending  # pending | in_progress | approved | rejected

approval:
  status: pending  # pending | approved | rejected
  approved_at: null
  approved_by: null
```

---

# Mapeamento para Registry

| `phase` no state | `status` no registry |
|------------------|---------------------|
| planned | planned |
| in_progress, validation, review, ready | in_progress |
| approved | approved |
| completed | completed |
