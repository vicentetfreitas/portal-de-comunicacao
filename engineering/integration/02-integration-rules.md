# Integration Rules — Engineering Framework

| Item | Valor |
|------|-------|
| Camada | `engineering/integration/` |
| Versão | 1.0 |
| Status | Stable |

---

# Regras de Governança

| ID | Regra | Descrição | Violação |
|----|-------|-----------|----------|
| **RULE-INT-01** | Features encerradas | Todas as Features do escopo devem estar `FEATURE_APPROVED` antes do início | Bloqueio de início |
| **RULE-INT-02** | Sem alteração de código | Não alterar código, specs, APIs ou Features durante a sprint documental | Rejeição de entrega |
| **RULE-INT-03** | Evidência obrigatória | Item sem evidência não pode ser `APPROVED` | Item permanece `PENDING` |
| **RULE-INT-04** | Manual primeiro | Execução manual; sem scripts, agentes ou orquestradores | Fora de escopo |
| **RULE-INT-05** | Issues rastreadas | Toda não-conformidade em `issues.md` | Perda de rastreabilidade |
| **RULE-INT-06** | Framework congelado | Não alterar templates/workflow durante execução da sprint | Evolução entre sprints |

---

# Regras de Checklist

| ID | Regra | Descrição |
|----|-------|-----------|
| **CHK-INT-01** | ID único | Cada item possui ID no formato `INT-<FASE>-<NNN>` |
| **CHK-INT-02** | Critério explícito | Critério de aprovação mensurável e verificável |
| **CHK-INT-03** | Status fechado | Status permitidos: `PENDING`, `APPROVED`, `BLOCKED`, `WAIVED` |
| **CHK-INT-04** | Waiver justificado | `WAIVED` exige justificativa, aprovador e data |
| **CHK-INT-05** | Must vs Should | Itens Must bloqueiam readiness; Should geram issue se pendentes |

---

# Regras de Evidência

| ID | Regra | Descrição |
|----|-------|-----------|
| **EVD-INT-01** | Rastreável | Evidência aponta para artefato verificável (teste, log, comando, URL) |
| **EVD-INT-02** | Reproduzível | Outro validador deve poder reproduzir a verificação |
| **EVD-INT-03** | Datada | Registrar data e executor da verificação |
| **EVD-INT-04** | Runtime isolado | Logs de build em `backend/runtime/` quando aplicável |

---

# Regras de Issues

| ID | Regra | Descrição |
|----|-------|-----------|
| **ISS-INT-01** | Severidade | `CRITICAL`, `HIGH`, `MEDIUM`, `LOW` |
| **ISS-INT-02** | Status | `OPEN`, `IN_PROGRESS`, `RESOLVED`, `DEFERRED` |
| **ISS-INT-03** | Bloqueio | `CRITICAL` aberta bloqueia transição para `APPROVED` |
| **ISS-INT-04** | Vínculo | Issue referencia item do checklist quando aplicável |

---

# Regras de State

| ID | Regra | Descrição |
|----|-------|-----------|
| **STATE-INT-01** | SSOT | `integration-state.yaml` é a única fonte do estado operacional |
| **STATE-INT-02** | Transição sequencial | Não pular estados sem critérios atendidos |
| **STATE-INT-03** | Retrocesso | Retrocesso permitido com justificativa em `integration-report.md` |
| **STATE-INT-04** | Métricas | Atualizar métricas a cada mudança de status de item |

---

# Regras de Escopo

| ID | Regra | Descrição |
|----|-------|-----------|
| **SCOPE-INT-01** | Features explícitas | Escopo listado em `integration-manifest.yaml` |
| **SCOPE-INT-02** | Platform Foundation | Fundação considerada pré-requisito implícito |
| **SCOPE-INT-03** | Cross-feature | Fluxos XFT documentados no plano antes da execução |
| **SCOPE-INT-04** | Fora de escopo | Features não listadas não são validadas nesta sprint |
