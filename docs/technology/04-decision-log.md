# 04-decision-log.md

# Decision Log

## Objetivo

Este documento registra as decisões arquiteturais, tecnológicas e estratégicas do Portal de Comunicação Digital (PCD).

Seu propósito é:

* Preservar o histórico de decisões
* Evitar retrabalho
* Facilitar auditorias
* Apoiar novos integrantes da equipe
* Justificar escolhas técnicas
* Reduzir divergências arquiteturais

---

# Processo de Decisão

Toda decisão relevante deve possuir:

| Campo         | Obrigatório |
| ------------- | ----------- |
| ID            | Sim         |
| Título        | Sim         |
| Data          | Sim         |
| Status        | Sim         |
| Contexto      | Sim         |
| Decisão       | Sim         |
| Consequências | Sim         |

---

# Status Possíveis

| Status     | Descrição         |
| ---------- | ----------------- |
| Proposed   | Em avaliação      |
| Approved   | Aprovada          |
| Rejected   | Rejeitada         |
| Superseded | Substituída       |
| Deprecated | Em descontinuação |

---

# DEC-001 — Banco de Dados Oficial

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-001    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Tecnologia |

---

## Contexto

A fundação inicial do projeto foi construída utilizando PostgreSQL como banco de dados padrão.

Durante a revisão tecnológica verificou-se que o ambiente corporativo utiliza Oracle Database como plataforma oficial.

---

## Decisão

O banco de dados oficial do projeto será:

```text
Oracle Database 23ai
```

Banco local:

```text
Oracle XE
```

---

## Consequências

### Positivas

* Alinhamento com ambiente corporativo
* Redução de riscos de implantação
* Compatibilidade com infraestrutura existente

### Negativas

* Ajustes nas migrations já existentes
* Necessidade de driver Oracle

---

# DEC-002 — Linguagem Backend

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-002    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Backend    |

---

## Contexto

O sistema necessita de uma plataforma moderna, robusta e aderente ao ecossistema corporativo.

---

## Decisão

A linguagem oficial será:

```text
Java 25
```

---

## Consequências

### Positivas

* Plataforma moderna
* Melhor desempenho
* Evolução contínua do ecossistema Java

### Negativas

* Necessidade de atualização dos ambientes de desenvolvimento

---

# DEC-003 — Framework Backend

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-003    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Backend    |

---

## Contexto

O projeto necessita de produtividade, segurança e integração com o ecossistema Java.

---

## Decisão

Framework oficial:

```text
Spring Boot 4.x
```

---

## Consequências

### Positivas

* Ecossistema consolidado
* Forte suporte corporativo
* Integração com Spring Security
* Integração com Spring Data

---

# DEC-004 — Framework Frontend

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-004    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Frontend   |

---

## Contexto

O portal necessita de uma interface moderna, produtiva e de fácil manutenção.

---

## Decisão

Frontend baseado em:

```text
Vue 3
Quasar Framework
TypeScript
```

---

## Alternativas Avaliadas

* Angular
* React
* Next.js

---

## Consequências

### Positivas

* Alta produtividade
* Curva de aprendizado reduzida
* Excelente biblioteca de componentes

---

# DEC-005 — Arquitetura de Software

## Informações

| Campo     | Valor       |
| --------- | ----------- |
| ID        | DEC-005     |
| Data      | 2026-06-22  |
| Status    | Approved    |
| Categoria | Arquitetura |

---

## Contexto

O sistema deverá evoluir por múltiplos módulos e equipes.

---

## Decisão

A arquitetura oficial será baseada em:

```text
DDD
Clean Architecture
Hexagonal Architecture
```

---

## Consequências

### Positivas

* Alta manutenibilidade
* Baixo acoplamento
* Escalabilidade organizacional

---

# DEC-006 — Estratégia de Banco

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-006    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Dados      |

---

## Decisão

Toda alteração estrutural será realizada exclusivamente através de scripts DDL versionados, executados pelo DBA (`database/ddl/` e `database/migrations/`).

> **Atualização DEC-DB-019 (2026-07-10):** Flyway não é utilizado. Ver `database/model/05-decisions-and-risks.md`.

## Proibido

```text
ALTER TABLE manual
CREATE TABLE manual
DROP TABLE manual
```

em ambientes controlados.

---

# DEC-007 — Estratégia de APIs

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-007    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Integração |

---

## Decisão

Padrão de APIs:

```text
REST
```

Versionamento:

```text
/api/v1
```

Documentação:

```text
OpenAPI 3
Swagger
```

---

# DEC-008 — Segurança

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-008    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Segurança  |

---

## Decisão

Autenticação:

```text
JWT
```

Autorização:

```text
RBAC
```

Framework:

```text
Spring Security
```

---

# DEC-009 — Estratégia de Containers

## Informações

| Campo     | Valor          |
| --------- | -------------- |
| ID        | DEC-009        |
| Data      | 2026-06-22     |
| Status    | Approved       |
| Categoria | Infraestrutura |

---

## Decisão

Containerização oficial:

```text
Docker
```

Orquestração local:

```text
Docker Compose
```

---

## Consequências

### Positivas

* Reprodutibilidade
* Padronização dos ambientes
* Simplificação do onboarding

---

# DEC-010 — Monorepo

## Informações

| Campo     | Valor       |
| --------- | ----------- |
| ID        | DEC-010     |
| Data      | 2026-06-22  |
| Status    | Approved    |
| Categoria | Repositório |

---

## Decisão

O projeto será mantido em um único repositório contendo:

```text
backend/
frontend/
docs/
```

---

# DEC-011 — Tecnologias Explicitamente Rejeitadas

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-011    |
| Data      | 2026-06-22 |
| Status    | Rejected   |
| Categoria | Tecnologia |

---

## Decisão

As tecnologias abaixo não fazem parte do MVP:

```text
PostgreSQL
MongoDB
MySQL
Redis
Kafka
RabbitMQ
Angular
NestJS
Node.js Backend
```

---

## Motivo

Redução de complexidade e alinhamento ao escopo inicial.

---

# DEC-012 — Estratégia de Observabilidade

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-012    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Operação   |

---

## Decisão

Stack oficial:

```text
Micrometer
Prometheus
Grafana
Spring Boot Actuator
```

---

# Decisões Pendentes

## DEC-013

### Título

Estratégia de armazenamento de documentos.

### Status

Proposed

### Alternativas

```text
Filesystem
Oracle SecureFiles
Object Storage
```

---

## DEC-014

### Título

Estratégia de notificações.

### Status

Proposed

### Alternativas

```text
Email
Push
SMS
Microsoft Teams
```

---

# Histórico de Revisões

| Data       | Alteração                               |
| ---------- | --------------------------------------- |
| 2026-06-22 | Criação inicial do registro de decisões |

---

# Governança

Toda alteração arquitetural relevante deverá:

1. Atualizar este documento.
2. Atualizar os artefatos impactados.
3. Atualizar auditorias relacionadas.
4. Registrar justificativa da mudança.

---

# Status

Documento ativo e obrigatório para governança técnica do Portal de Comunicação Digital.
