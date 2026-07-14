# Entry and Exit Criteria — Integration Sprint

| Item | Valor |
|------|-------|
| Camada | `engineering/integration/` |
| Versão | 1.0 |
| Status | Stable |

---

# Critérios de Entrada (Definition of Ready — Integration)

A Sprint de Integração **só pode iniciar** quando todos os critérios abaixo forem atendidos.

| ID | Critério | Verificação | Bloqueante |
|----|----------|-------------|------------|
| **ENT-01** | Features encerradas | Todas as Features do escopo com `closure.status: done` e `FEATURE_APPROVED` | Sim |
| **ENT-02** | Platform Foundation aprovada | `construction/platform-foundation/construction-state.yaml` → `phase: closed` | Sim |
| **ENT-03** | Build verde | `mvn clean verify` SUCCESS na branch de integração | Sim |
| **ENT-04** | Manifesto instanciado | `integration-manifest.yaml` preenchido com escopo e artefatos | Sim |
| **ENT-05** | Plano definido | `integration-plan.md` com escopo, fluxos XFT e dependências | Sim |
| **ENT-06** | Checklist instanciado | `integration-checklist.md` derivado do template | Sim |
| **ENT-07** | Specs disponíveis | `specification.path` de cada Feature acessível | Sim |
| **ENT-08** | Ambiente de teste | Profile `test` ou `local` configurado e documentado | Sim |

---

# Critérios de Saída (Definition of Done — Integration)

A Sprint de Integração **só pode ser aprovada** quando todos os critérios Must abaixo forem atendidos.

| ID | Critério | Verificação | Bloqueante |
|----|----------|-------------|------------|
| **EXT-01** | Checklist Must completo | 100% itens Must com status ≠ `PENDING` | Sim |
| **EXT-02** | Evidências registradas | 100% itens Must `APPROVED` ou `WAIVED` com justificativa | Sim |
| **EXT-03** | Fases executadas | ENV, INF, API, DB, XFT, FUN, OUT concluídas | Sim |
| **EXT-04** | Fluxos XFT validados | Todos os fluxos do plano executados | Sim |
| **EXT-05** | Issues CRITICAL | Zero issues `CRITICAL` abertas | Sim |
| **EXT-06** | Relatório publicado | `integration-report.md` versão final | Sim |
| **EXT-07** | Readiness aprovada | `integration-readiness.md` → decisão `APPROVED` | Sim |
| **EXT-08** | State atualizado | `integration-state.yaml` → `phase: completed` | Sim |
| **EXT-09** | Registry atualizado | `engineering/integration/registry.yaml` | Sim |
| **EXT-10** | Riscos documentados | Riscos residuais em `integration-report.md` | Não (Should) |

---

# Critérios de Aprovação (Readiness)

Detalhamento em `integration-readiness.md` por instância. Critérios genéricos:

| ID | Critério | Descrição |
|----|----------|-----------|
| **RDY-01** | Ambiente estável | Aplicação inicia; datasource OK; actuator UP |
| **RDY-02** | Segurança integrada | Auth, JWT, cookies, CORS, CSRF validados |
| **RDY-03** | APIs conformes | CRUD, paginação, filtros, status HTTP conforme spec |
| **RDY-04** | Persistência íntegra | FK, auditoria, soft delete, relacionamentos OK |
| **RDY-05** | Fluxos cross-feature | Cadeia documentada executada com sucesso |
| **RDY-06** | Regras de negócio | Casos positivos e negativos cobertos |
| **RDY-07** | Rastreabilidade | Issues vinculadas a checklist e specs |
| **RDY-08** | Sem bloqueios | Nenhum item Must `BLOCKED` |

---

# Critérios de Rejeição

A sprint é **rejeitada** (`readiness.status: rejected`) quando:

- existir item Must `BLOCKED` sem plano de resolução;
- existir issue `CRITICAL` aberta;
- fluxo XFT obrigatório falhar sem waiver aprovado;
- evidências forem insuficientes ou não reproduzíveis;
- escopo divergir do manifesto sem atualização formal.

Rejeição exige plano de ação em `issues.md` e retrocesso de estado conforme `04-state-machine.md`.

---

# Waivers

Itens `WAIVED` são permitidos somente quando:

1. Severidade não é `CRITICAL`;
2. Justificativa documentada no checklist;
3. Aprovador identificado (Reviewer ou Auditor);
4. Issue `DEFERRED` criada em `issues.md` com prazo de resolução;
5. Risco residual registrado em `integration-report.md`.

Waivers em itens Must de segurança (INF) ou integridade (DB) exigem aprovação explícita do Integration Lead.
