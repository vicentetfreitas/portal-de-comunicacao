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
| Última Atualização     | 2026-08-14                                 |
| Responsável            | Project Manager / Arquitetura              |
| Próxima Revisão        | Etapa 5 — infraestrutura local (Oracle)    |

---

# Resumo Executivo

## Situação Atual

O projeto concluiu a **Sprint 0 do backend**, múltiplas features de **Etapa 2** (org + auth/sessão) e a **revalidação SSOT (Etapa 3)** em 2026-08-14.

SSOT operacional: `specs/foundation/minimal-ssot.md`  
SSOT normativo documental: `docs/governance/07-documentation-architecture.md` v2.0  
Estado de implementação: `construction/registry.yaml` + git/CI

**Progresso de construção (indicativo):**

* FT-AUTH, FT-AREA, FT-SINGULAR, FT-EQUIPE, FT-SESSION — **closed**
* FT-COLABORADOR — BE closed, FE em execução
* FT-PRIMEIRO-ACESSO — spec **APPROVED**; BE not_started, FE em execução
* Integration sprint-03-org-backend — **APPROVED** (2026-07-14)

---

## Objetivos da Fase Atual

* Concluir **FT-COLABORADOR** (FE) e implementar **FT-PRIMEIRO-ACESSO** (BE + FE).
* Validar ambiente local Oracle (Etapa 5).
* Manter aderência entre `specs/`, `docs/` e código.
* Evoluir Etapas 3–5 do MVP conforme `docs/backlog/04-mvp-scope.md`.

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
| Backend         | 🟩 Etapa 2 parcial — org + auth/sessão implementados |
| Frontend        | 🟩 Foundation + org CRUD (Singular, Equipe); Colaborador e Primeiro Acesso em execução |
| Banco de Dados  | 🟩 Oracle — baseline homologado; 6 entidades JPA alinhadas (Etapa 4) |
| Testes          | 🟩 Unit + integração Oracle (features org/auth) |
| Observabilidade | 🟨 Parcial — Correlation ID + Actuator              |

Resultado Atual:

```text
🟨 Em andamento — Etapa 2 parcial; Etapas 3–5 pendentes
```

---

## Construction

| Item            | Status                                              |
| --------------- | --------------------------------------------------- |
| Ambiente Local  | 🟨 Backend + FE dev operáveis; Oracle externo obrigatório |
| Docker          | 🟨 Compose na raiz — Postgres legado; alinhar na Etapa 5 |
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
| M9    | Integration sprint-03-org-backend APPROVED | 2026-07-14 |
| M10   | FT-AUTH, FT-SINGULAR (BE+FE), FT-EQUIPE, FT-SESSION closed | 2026-08 |
| M11   | Revalidação SSOT Etapa 3 + auditoria JPA Etapa 4 | 2026-08-14 |

---

## Em Andamento

| Marco | Descrição                              | Status          |
| ----- | -------------------------------------- | --------------- |
| M12   | FT-COLABORADOR (FE) + FT-PRIMEIRO-ACESSO | Em execução   |
| M13   | Etapa 5 — infraestrutura local Oracle  | Planejado       |

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

**Andamento geral estimado:** ~45% (Etapas 1–2 parcialmente implementadas; documental e comunicação pendentes).

---

# Impedimentos

## Impedimentos Atuais

Nenhum impedimento bloqueando FT-COLABORADOR (FE) ou FT-PRIMEIRO-ACESSO. Ambiente local requer Oracle acessível (`UNMPORTCOM_APP`).

---

# Próximas Ações

## Curto Prazo

1. Concluir FT-COLABORADOR (FE) e implementar FT-PRIMEIRO-ACESSO (BE + FE).
2. Etapa 5 — alinhar `docker-compose.yml` e docs de infra ao Oracle.
3. Manter aderência ao fluxo simplificado (`specs/foundation/development-workflow.md`).

---

# Histórico de Atualizações

| Data       | Autor           | Alteração                                              |
| ---------- | --------------- | ------------------------------------------------------ |
| YYYY-MM-DD | Project Manager | Criação inicial do documento                           |
| 2026-06-22 | Reconciliação   | Atualização pós-consolidação MVP                       |
| 2026-07-08 | Governança      | Encerramento oficial da Sprint 0 — baseline congelada  |
| 2026-08-14 | Revalidação     | Etapa 3 SSOT aprovada com ressalvas; Etapa 4 JPA audit |
