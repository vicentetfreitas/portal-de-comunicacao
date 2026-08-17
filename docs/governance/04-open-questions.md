# Open Questions

## Objetivo

Consolidar perguntas e dúvidas ainda não respondidas que possam impactar o andamento do projeto.

Este documento não substitui os artefatos específicos de cada camada.

Seu objetivo é fornecer uma visão centralizada das questões abertas que dependem de validação, investigação ou decisão futura.

---

# Diretrizes

Uma questão deve ser registrada quando:

* impedir avanço de uma atividade;
* gerar risco significativo;
* impactar arquitetura ou negócio;
* depender de aprovação externa;
* exigir investigação adicional.

---

# Classificação

## Categoria

| Categoria   | Descrição                     |
| ----------- | ----------------------------- |
| Negócio     | Regras, processos e objetivos |
| Domínio     | Conceitos e modelagem         |
| Arquitetura | Decisões arquiteturais        |
| Integração  | Sistemas externos             |
| Segurança   | Controles e requisitos        |
| Dados       | Persistência e governança     |
| Operação    | Infraestrutura e suporte      |

---

## Prioridade

| Nível   | Descrição                    |
| ------- | ---------------------------- |
| Baixa   | Pode aguardar                |
| Média   | Deve ser resolvida em breve  |
| Alta    | Impacta planejamento         |
| Crítica | Bloqueia evolução do projeto |

---

## Status

| Status     | Descrição                      |
| ---------- | ------------------------------ |
| Aberta     | Sem resposta                   |
| Em Análise | Investigação em andamento      |
| Respondida | Resposta obtida                |
| Encerrada  | Não requer mais acompanhamento |

---

# Resumo Executivo

| Status      | Quantidade |
| ----------- | ---------- |
| Abertas     | 6          |
| Em Análise  | 0          |
| Respondidas | 2          |
| Encerradas  | 1          |

---

# Registro de Questões

## QST-001

### Pergunta

Quais funcionalidades compõem o escopo mínimo do MVP?

### Categoria

Negócio.

### Impacto

Alto.

### Responsável

Product Owner.

### Status

Encerrada.

### Resposta

O escopo mínimo do MVP foi consolidado em `docs/audit/10-mvp-consolidation-audit.md`, correspondente às **Etapas 1–5** de `docs/solution-design/10-delivery-roadmap.md`.

**Épicos oficiais:** EPIC-001 a EPIC-006.

**Excluídos:** EPIC-007 (pós-MVP), EPIC-008/Campanhas, FEATURE-046/Mensagens, FEATURE-044/Métricas Administrativas de negócio.

**Sincronização aplicada:** `docs/governance/reconciliation-report.md` — 2026-06-22.

---

## QST-002

### Pergunta

Existem integrações obrigatórias para a primeira entrega?

### Categoria

Integração.

### Impacto

Alto.

### Responsável

Arquitetura.

### Status

**Respondida** (2026-07-24).

### Resposta

Sim. Integração obrigatória: **Zimbra** como Identity Provider (IMAP/SMTP/SOAP). Homologação: `docs/discovery/ft-auth-zimbra-homologacao.md`. Arquitetura: `specs/architecture/authentication-architecture.md` (DA-AUTH-012).

---

## QST-003

### Pergunta

Existem requisitos regulatórios ou legais que impactam a solução?

### Categoria

Negócio.

### Impacto

Crítico.

### Responsável

Especialista de Negócio.

### Status

Aberta.

---

## QST-004

### Pergunta

Quais requisitos de disponibilidade devem ser atendidos?

### Categoria

Arquitetura.

### Impacto

Alto.

### Responsável

Arquiteto de Software.

### Status

Aberta.

---

## QST-005

### Pergunta

Quais requisitos de retenção de dados devem ser considerados?

### Categoria

Dados.

### Impacto

Alto.

### Responsável

Data Owner.

### Status

Aberta.

---

## QST-006

### Pergunta

Existe necessidade de auditoria completa das operações?

### Categoria

Segurança.

### Impacto

Alto.

### Responsável

Security Lead.

### Status

Aberta.

### Observação Sprint 0

Infraestrutura de logging e Correlation ID implementada (`infrastructure/logging/`). Auditoria completa de operações de negócio permanece pendente — escopo definido nas Features futuras.

---

## QST-007

### Pergunta

Quais métricas serão utilizadas para medir sucesso do produto?

### Categoria

Negócio.

### Impacto

Médio.

### Responsável

Product Manager.

### Status

Aberta.

---

## QST-008

### Pergunta

Quais bloqueantes de autenticação/sessão devem ser resolvidos antes do novo fluxo de login?

### Categoria

Arquitetura / Domínio.

### Impacto

Crítico.

### Responsável

Arquitetura + Product Owner.

### Status

Aberta (bloqueantes de auth/sessão **parcialmente encerrados**).

### Observação

Não duplicar conteúdo de domínio. Acompanhar em:

| Bloqueante | Tipo | SSOT | Status |
|------------|------|------|--------|
| Fluxo oficial de primeiro acesso | Negócio | DEC-FA-001, FT-PRIMEIRO-ACESSO | **Encerrado** |
| BR-010 / vínculos obrigatórios | Negócio | DEC-FA-002 | **Encerrado** |
| Multi-contexto nesta entrega | Negócio + Arquitetura | DEC-FA-003 | **Encerrado** |
| Painel inicial / home route | Arquitetura + Planejamento | DEC-FA-004 | **Encerrado** |
| Alinhamento OAuth → protocolo homologado | — | DA-AUTH-012 | **Encerrado** |
| Modelo físico N vínculos | Dados | FT-SESSION / FT-PRIMEIRO-ACESSO (especificação futura) | Pendente implementação |
| OQ-007 (evento Colaborador Integrado) | Domínio | `docs/domain/10-open-questions.md` | Aberta |

DEC provisórias multi-contexto/painel canceladas no Gate Final foram **substituídas** por DEC-FA-003 e DEC-FA-004 (IDs sem colisão com technology).

---

# Questões Críticas

As questões abaixo possuem potencial de bloqueio para evolução do projeto.

| ID      | Questão                    |
| ------- | -------------------------- |
| QST-003 | Requisitos regulatórios    |
| QST-004 | Disponibilidade da solução |
| QST-005 | Retenção de dados          |
| QST-008 | Bloqueantes auth/sessão    |

---

# Critérios de Encerramento

Uma questão pode ser encerrada quando:

* existir resposta formal;
* a resposta estiver documentada;
* os impactos forem tratados;
* não houver dependências pendentes.

---

# Dependências

## Discovery

* Visão
* Objetivos
* Escopo

---

## Domain

* Regras de negócio
* Casos de uso
* Glossário

---

## Architecture

* NFRs
* ADRs
* Integrações

---

## Solution Design

* APIs
* Dados
* Segurança

---

# Histórico de Atualizações

| Data       | Autor           | Alteração                                              |
| ---------- | --------------- | ------------------------------------------------------ |
| YYYY-MM-DD | Project Manager | Criação inicial do documento                           |
| 2026-06-22 | Reconciliação   | QST-001 encerrada — `10-mvp-consolidation-audit.md`    |
| 2026-07-08 | Governança      | Revisão pós-Sprint 0 — correção de conteúdo; observações QST-002 e QST-006 |
