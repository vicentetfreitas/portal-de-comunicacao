# Project Status

## Objetivo

Consolidar o estado atual do projeto em um único local.

Este documento fornece uma visão executiva sobre:

* progresso das camadas documentais;
* prontidão para construção;
* status dos artefatos;
* marcos alcançados;
* próximos passos;
* impedimentos conhecidos.

---

# Informações Gerais

| Item                   | Valor                                      |
| ---------------------- | ------------------------------------------ |
| Projeto                | Portal de Comunicação                      |
| Status Geral           | Baseline documental **APROVADA COM RESSALVAS** (Exit Gate 2026-07-24) |
| Versão da Documentação | 2.0 (Exit Gate)                                |
| Última Atualização     | 2026-07-24                                 |
| Responsável            | Project Manager / Arquitetura              |
| Próxima Revisão        | OQ-001/026/027/028; unificação catálogo DEC |

---

# Resumo Executivo

## Situação Atual

O projeto concluiu a **Sprint 0 do backend** e obteve **APROVADO COM RESSALVAS** no Exit Gate de governança documental (2026-07-24).

SSOT normativo da documentação: `docs/governance/07-documentation-architecture.md` v2.0  
Evidência do Exit Gate: `docs/governance/reconciliation-report.md` § Exit Gate  
Política de repositório: `docs/governance/08-repository-governance.md` v2.0

Categorias obrigatórias: **SSOT | Evidence | Working | Archive**.

Ressalvas conscientes: unificação de catálogos DEC; índice frontend; naming `docs/construction` vs `construction/`; OQs de login/contexto (não bloqueiam a baseline).

**Resultado da Sprint 0 (ainda válido):**

* Baseline da infraestrutura aprovada
* Build `mvn clean verify` — **SUCCESS**
* Infraestrutura transversal **congelada** — alterações restritas a correções críticas
* Features org/auth em evolução conforme `construction/registry.yaml`

---

## Objetivos da Fase Atual

* Implementar a Golden Feature **FT-AUTH** (Sprint 1).
* Manter aderência entre `specs/`, `docs/` e código.
* Evoluir bounded contexts conforme roadmap de entrega do MVP.
* Preservar a baseline arquitetural estabelecida na Sprint 0.

---

# Status por Camada

## Discovery

| Item                 | Status         |
| -------------------- | -------------- |
| Visão do Produto     | 🟩 Concluído   |
| Stakeholders         | 🟩 Concluído   |
| Objetivos de Negócio | 🟩 Concluído   |
| Escopo               | 🟩 Concluído   |
| Requisitos           | 🟩 Concluído   |

Resultado Atual:

```text
🟩 Concluído
```

---

## Domain

| Item              | Status       |
| ----------------- | ------------ |
| Domínio Principal | 🟩 Concluído |
| Glossário         | 🟩 Concluído |
| Processos         | 🟩 Concluído |
| Regras de Negócio | 🟩 Concluído |
| Casos de Uso      | 🟩 Concluído |

Resultado Atual:

```text
🟩 Concluído
```

---

## Architecture

| Item                | Status       |
| ------------------- | ------------ |
| Contexto do Sistema | 🟩 Concluído |
| Containers          | 🟩 Concluído |
| Componentes         | 🟩 Concluído |
| Integrações         | 🟩 Concluído |
| NFRs                | 🟩 Concluído |

Resultado Atual:

```text
🟩 Concluído — baseline congelada
```

---

## Solution Design

| Item      | Status       |
| --------- | ------------ |
| Fluxos    | 🟩 Concluído |
| APIs      | 🟩 Concluído |
| Dados     | 🟩 Concluído |
| Segurança | 🟩 Concluído |
| Migração  | 🟩 Concluído |

Resultado Atual:

```text
🟩 Concluído
```

---

## Implementation

| Item            | Status                                              |
| --------------- | --------------------------------------------------- |
| Backend         | 🟩 Sprint 0 concluída — infraestrutura transversal  |
| Frontend        | ⬜ Não iniciado                                      |
| Banco de Dados  | 🟩 Oracle — baseline DDL (DBA)                      |
| Testes          | 🟩 106 testes unitários — infraestrutura transversal |
| Observabilidade | 🟨 Parcial — Correlation ID + Actuator              |

Resultado Atual:

```text
🟨 Em andamento — fundação backend concluída; features pendentes
```

---

## Construction

| Item            | Status                                              |
| --------------- | --------------------------------------------------- |
| Ambiente Local  | 🟩 Backend operacional (perfil `local`)             |
| Docker          | 🟨 Planejado — Sprint futura                        |
| CI/CD           | ⬜ Não iniciado                                      |
| Observabilidade | 🟨 Parcial — logging e Correlation ID implementados |

Resultado Atual:

```text
🟨 Em andamento — bootstrap backend concluído
```

---

## Delivery

| Item         | Status         |
| ------------ | -------------- |
| MVP          | 🟨 Planejado   |
| Release Plan | 🟩 Documentado |
| Cutover Plan | 🟩 Documentado |

Resultado Atual:

```text
🟨 Planejado — aguardando features do MVP
```

---

## Audit

| Item                    | Status                                      |
| ----------------------- | ------------------------------------------- |
| Consistência Documental | 🟩 Reconciliação MVP + Sprint 0 aplicada    |
| Validação de Domínio    | 🟩 Concluída                                |
| Validação Arquitetural  | 🟩 Baseline aprovada                        |
| Readiness Report        | 🟩 Sprint 0 — auditoria final registrada    |

Resultado Atual:

```text
🟩 Concluído — para escopo da Sprint 0
```

---

# Marcos do Projeto

## Concluídos

| Marco | Descrição                                              | Data       |
| ----- | ------------------------------------------------------ | ---------- |
| M1    | Discovery Aprovado                                     | 2026-06-22 |
| M2    | Domain Aprovado                                        | 2026-06-22 |
| M3    | Architecture Aprovada                                  | 2026-06-22 |
| M4    | Solution Design Aprovado                               | 2026-06-22 |
| M5    | Implementation Documentada                             | 2026-06-22 |
| M6    | Reconciliação MVP Consolidation                        | 2026-06-22 |
| M7    | **Sprint 0 Backend — Infraestrutura Transversal**      | 2026-07-08 |
| M8    | **Baseline Arquitetural Backend — Aprovada e Congelada** | 2026-07-08 |

---

## Em Andamento

| Marco | Descrição                              | Status          |
| ----- | -------------------------------------- | --------------- |
| M9    | Sprint 1 — FT-AUTH (Golden Feature)    | Em Preparação   |

---

## Planejados

* Entrega do MVP (Etapas 1–5)
* Go Live
* Release 1, 2 e 3

---

# Indicadores de Prontidão

| Indicador            | Status | Observação                                      |
| -------------------- | ------ | ----------------------------------------------- |
| Escopo Definido      | 🟩     | MVP consolidado (Etapas 1–5)                    |
| Requisitos Validados | 🟩     | Domain e specs foundation concluídos            |
| Arquitetura Validada | 🟩     | Baseline congelada pós-Sprint 0                  |
| Solução Detalhada    | 🟩     | Solution Design concluído                       |
| Construção Preparada | 🟩     | Infraestrutura backend operacional              |
| MVP Planejado        | 🟩     | Roadmap e backlog alinhados                     |

**Andamento geral estimado:** ~25% (documentação e fundação técnica concluídas; features de negócio pendentes).

---

# Impedimentos

## Impedimentos Atuais

Nenhum impedimento bloqueando o início da Sprint 1 (FT-AUTH).

---

# Próximas Ações

## Curto Prazo

1. Iniciar Sprint 1 — implementação da Golden Feature **FT-AUTH**.
2. Configurar `SecurityFilterChain` e integração com Serviço Corporativo de Autenticação.
3. Manter aderência à baseline congelada da Sprint 0.
4. Revisar riscos e decisões abertas ao final da Sprint 1.

---

# Histórico de Atualizações

| Data       | Autor           | Alteração                                              |
| ---------- | --------------- | ------------------------------------------------------ |
| YYYY-MM-DD | Project Manager | Criação inicial do documento                           |
| 2026-06-22 | Reconciliação   | Atualização pós-consolidação MVP                       |
| 2026-07-08 | Governança      | Encerramento oficial da Sprint 0 — baseline congelada  |
