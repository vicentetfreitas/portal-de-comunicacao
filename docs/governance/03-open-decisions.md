# Open Decisions

## Objetivo

Centralizar todas as decisões pendentes do projeto que possam impactar negócio, arquitetura, solução, implementação, construção ou entrega.

Este documento funciona como um registro de acompanhamento para decisões ainda não concluídas e deve ser utilizado como fonte oficial para monitoramento de definições críticas.

Após aprovação, cada decisão deve ser movida para sua documentação definitiva ou para os respectivos ADRs (Architecture Decision Records).

---

# Processo de Gestão de Decisões

## Fluxo oficial (alinhado à governança documental)

```text
Open Question
      ↓
Discussão
      ↓
DEC (alternativas + aprovação)
      ↓
Implementação / Registro definitivo (ADR, technology decision-log, specs)
```

Fonte normativa do fluxo: `docs/governance/07-documentation-architecture.md`.

Fluxo operacional interno deste registro (complementar, não substitui o fluxo acima):

```text
Identificação
      ↓
Análise
      ↓
Avaliação de Alternativas
      ↓
Aprovação
      ↓
Registro Definitivo
      ↓
Encerramento
```

**Proibido:** criar DEC “aberta” que apenas repita uma Open Question sem alternativas prontas para aprovação.

**IDs:** únicos no repositório — consultar também `docs/technology/04-decision-log.md` e ADRs antes de atribuir ID.

---

# Status Possíveis

| Status               | Descrição                |
| -------------------- | ------------------------ |
| Aberta               | Ainda não analisada      |
| Em Análise           | Avaliação em andamento   |
| Aguardando Aprovação | Pronta para decisão      |
| Aprovada             | Decisão tomada           |
| Rejeitada            | Alternativa descartada   |
| Cancelada            | Não será mais necessária |

---

# Criticidade

| Nível   | Descrição                                |
| ------- | ---------------------------------------- |
| Baixa   | Impacto localizado                       |
| Média   | Impacto moderado                         |
| Alta    | Impacto relevante                        |
| Crítica | Impacta arquitetura ou negócio principal |

---

# Resumo Executivo

| Status               | Quantidade |
| -------------------- | ---------- |
| Abertas              | 3          |
| Em Análise           | 0          |
| Aguardando Aprovação | 0          |
| Aprovadas            | 4          |
| Rejeitadas           | 0          |
| Canceladas           | 2          |

---

# Registro de Decisões

## DEC-001

### Título

Definição da estratégia de autenticação e autorização.

### Categoria

Arquitetura.

### Contexto

A solução necessita de um mecanismo centralizado para autenticação, autorização e gestão de identidade.

### Alternativas

* Keycloak
* Auth0
* Azure AD
* Solução própria
* Serviço Corporativo de Autenticação

### Impacto

Crítico.

### Responsável

Arquiteto de Software.

### Prazo

Sprint 1 (FT-AUTH)

### Status

**Aprovada** (2026-07-24).

### Decisão

Autenticação via **Zimbra** (Identity Provider) com arquitetura **Stateless** (JWT próprio + Refresh Token em cookies HttpOnly). Autorização permanece no Portal (banco). Protocolo de integração: **proxy de credenciais IMAP/SMTP/SOAP** (DA-AUTH-012).

### Registro definitivo

- ADRs: ADR-003, ADR-005, ADR-006 (`docs/architecture/08-decision-records.md`)
- Decisões de Feature: `specs/features/authentication/decisions.md` (DA-AUTH-001 a DA-AUTH-012)
- Arquitetura: `specs/architecture/authentication-architecture.md`
- Homologação operacional: `docs/discovery/ft-auth-zimbra-homologacao.md`

---

## DEC-002

### Título

Definição da estratégia de observabilidade.

### Categoria

Plataforma.

### Contexto

Necessidade de monitoramento operacional, métricas, logs e rastreamento distribuído.

### Alternativas

* Prometheus + Grafana
* Elastic Stack
* Datadog
* New Relic

### Impacto

Alta.

### Responsável

Arquitetura.

### Prazo

Sprint futura

### Status

Aberta — parcialmente endereçada na Sprint 0 (Correlation ID, Actuator). Métricas e dashboards pendentes.

---

## DEC-003

### Título

Definição da estratégia de mensageria.

### Categoria

Arquitetura.

### Contexto

Necessidade de processamento assíncrono e desacoplamento entre componentes.

### Alternativas

* RabbitMQ
* Kafka
* AWS SQS
* Sem mensageria

### Impacto

Alta.

### Responsável

Arquitetura.

### Prazo

Sprint futura

### Status

Aberta.

---

## DEC-004

### Título

Definição da estratégia de deploy.

### Categoria

Infraestrutura.

### Contexto

Necessidade de definir modelo de implantação e operação.

### Alternativas

* Docker Compose
* Kubernetes
* OpenShift
* Cloud Managed Platform

### Impacto

Alta.

### Responsável

DevOps.

### Prazo

Sprint futura

### Status

Aberta.

---

## DEC-005

### Título

Definição da estratégia de versionamento.

### Categoria

Governança.

### Contexto

Padronizar releases e evolução do produto.

### Alternativas

* Semantic Versioning
* Calendar Versioning
* Release Train

### Impacto

Média.

### Responsável

Tech Lead.

### Prazo

2026-07-08

### Status

**Aprovada** — Semantic Versioning adotado (`0.0.1-SNAPSHOT` em `backend/pom.xml`).

### Registro definitivo

`docs/implementation/11-bootstrap-roadmap.md`, `backend/pom.xml`.

---

## DEC-006

### Título

Definição da estratégia de testes automatizados.

### Categoria

Qualidade.

### Contexto

Estabelecer cobertura mínima e tipos de testes obrigatórios.

### Alternativas

* Unitários apenas
* Unitários + Integração
* Pirâmide completa de testes
* Estratégia baseada em risco

### Impacto

Alta.

### Responsável

QA Lead.

### Prazo

2026-07-08

### Status

**Aprovada** — Unitários + Integração (integração a partir de Sprint 1). Build obrigatório via `mvn clean verify`. Evidência: 106 testes unitários na Sprint 0.

### Registro definitivo

`docs/construction/backend/01-project-bootstrap.md` § Build.

---

## DEC-007

### Título

Definição da estratégia de banco de dados.

### Categoria

Dados.

### Contexto

Necessidade de consolidar a tecnologia principal de persistência.

### Alternativas

* PostgreSQL
* Oracle
* SQL Server
* MySQL

### Impacto

Alta.

### Responsável

Arquitetura.

### Prazo

2026-07-08

### Status

**Aprovada** — Oracle Database (schema `UNMPORTCOM`, driver `ojdbc11`, baseline DDL administrado pelo DBA — DEC-DB-019).

### Registro definitivo

`docs/implementation/06-database-standards.md`, `docs/construction/backend/01-project-bootstrap.md`, `backend/pom.xml`.

---

## Item provisório cancelado — Multi-contexto (login/sessão)

### Status

**Cancelado** (2026-07-24) — Gate Final.

### Motivo

1. Violava o fluxo **Open Question → Discussão → DEC → Implementação** (ainda é OQ-027 / OQ-008).
2. Recebeu temporariamente o rótulo “DEC-008”, **em colisão** com `docs/technology/04-decision-log.md` **DEC-008 — Segurança**. IDs de DEC não podem ser reutilizados entre catálogos.

### Registro oficial da pergunta

`docs/domain/10-open-questions.md` — **OQ-027**, **OQ-008**.

Quando respondida: criar DEC no catálogo correto (`docs/governance/03-open-decisions.md` ou technology, conforme natureza) com **novo ID** não colidente.

---

## Item provisório cancelado — Painel inicial (home route)

### Status

**Cancelado** (2026-07-24) — Gate Final.

### Motivo

1. Ainda é **OQ-028**, não decisão aprovada.
2. Rótulo temporário “DEC-009” **colidia** com `docs/technology/04-decision-log.md` **DEC-009 — Containers**.

### Registro oficial da pergunta

`docs/domain/10-open-questions.md` — **OQ-028**.

---

# Decisões Críticas Abertas

| ID      | Decisão                    | Sprint prevista |
| ------- | -------------------------- | --------------- |
| DEC-002 | Estratégia de observabilidade | Sprint futura |
| DEC-003 | Estratégia de mensageria   | Sprint futura   |
| DEC-004 | Estratégia de deploy       | Sprint futura   |

> **Atenção:** IDs DEC em `docs/technology/04-decision-log.md` (ex.: DEC-008 Segurança, DEC-009 Containers) são catálogo **distinto**. Novas DECs de governança devem consultar ambos os catálogos antes de atribuir ID. Bloqueantes de login/sessão: QST-008 + OQ-001/026/027/028.

---

# Dependências

## Discovery

* Objetivos do produto
* Restrições do negócio

---

## Architecture

* ADRs
* NFRs
* Integrações

---

## Solution Design

* APIs
* Segurança
* Dados

---

## Implementation

* Tecnologias
* Frameworks
* Padrões de desenvolvimento

---

# Critérios de Encerramento

Uma decisão somente pode ser encerrada quando:

* existir justificativa registrada;
* existir aprovação formal;
* impactos forem documentados;
* documentação alvo for atualizada.

---

# Histórico de Atualizações

| Data       | Autor           | Alteração                                              |
| ---------- | --------------- | ------------------------------------------------------ |
| YYYY-MM-DD | Project Manager | Criação inicial do documento                           |
| 2026-07-08 | Governança      | DEC-005, DEC-006, DEC-007 aprovadas na Sprint 0; DEC-001 vinculada a FT-AUTH |
| 2026-07-24 | Governança      | DEC-001 aprovada; itens multi-contexto/painel cancelados (OQ prematuras + colisão ID com technology DEC-008/009) |
