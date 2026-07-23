# 01-technology-stack.md

# Technology Stack

## Objetivo

Este documento define oficialmente a stack tecnológica aprovada para o Portal de Comunicação Digital (PCD).

Todas as decisões arquiteturais, implementações, documentações, pipelines e artefatos do projeto devem seguir as tecnologias descritas neste documento.

---

# Visão Geral da Stack

| Camada                  | Tecnologia                        |
| ----------------------- | --------------------------------- |
| Frontend Web            | Vue 3 + Quasar Framework          |
| Linguagem Frontend      | TypeScript                        |
| Gerenciamento de Estado | Pinia                             |
| Comunicação HTTP        | Axios                             |
| Backend                 | Spring Boot                       |
| Linguagem Backend       | Java 25                           |
| Build Backend           | Maven                             |
| Banco de Dados          | Oracle Database                   |
| Evolução do Schema      | Baseline DDL (DBA) — DEC-DB-019   |
| Autenticação            | JWT + OAuth2 Resource Server      |
| Documentação API        | OpenAPI 3                         |
| Containerização         | Docker                            |
| Orquestração Local      | Docker Compose                    |
| Controle de Versão      | Git                               |
| Repositório             | GitHub                            |
| CI/CD                   | GitHub Actions                    |
| Observabilidade         | Micrometer + Prometheus + Grafana |
| Logs                    | SLF4J + Logback                   |
| Testes Backend          | JUnit 5 + Mockito                 |
| Testes Frontend         | Vitest                            |
| Testes E2E              | Playwright                        |

---

# Backend

## Linguagem

```text
Java 25
```

### Motivos

* Última versão LTS adotada pelo projeto
* Melhor suporte a virtual threads
* Melhorias de desempenho
* Evolução contínua da plataforma Java

---

## Framework

```text
Spring Boot 4.x
```

### Componentes Principais

```text
spring-boot-starter-web
spring-boot-starter-validation
spring-boot-starter-security
spring-boot-starter-actuator
spring-boot-starter-data-jpa
springdoc-openapi
```

### Padrão Arquitetural

```text
DDD
Hexagonal Architecture
Clean Architecture
```

---

# Frontend

## Framework

```text
Vue 3
```

### Biblioteca de Componentes

```text
Quasar Framework
```

### Linguagem

```text
TypeScript
```

### Estado Global

```text
Pinia
```

### Comunicação HTTP

```text
Axios
```

### Benefícios

* Alta produtividade
* Excelente integração com Vue
* Componentização robusta
* Suporte nativo a PWA
* Excelente experiência para times corporativos

---

# Banco de Dados

## SGBD Oficial

```text
Oracle Database 23ai
```

### Ambientes

| Ambiente        | Banco     |
| --------------- | --------- |
| Local           | Oracle XE |
| Desenvolvimento | Oracle    |
| Homologação     | Oracle    |
| Produção        | Oracle    |

---

## Persistência

```text
Spring Data JPA
Hibernate ORM
```

---

## Evolução do schema (DBA)

```text
Baseline DDL — database/ddl/
```

### Política (DEC-DB-019)

O schema Oracle é administrado pelo DBA através do baseline DDL oficial do projeto. A aplicação pressupõe schema previamente criado. Flyway não é utilizado.

Estrutura baseline:

```text
000-install.sql … 901-validation.sql
```

---

# APIs

## Estilo Arquitetural

```text
REST
```

## Contratos

```text
OpenAPI 3
Swagger UI
```

## Padrões

### Formato

```json
{
  "data": {},
  "metadata": {},
  "errors": []
}
```

### Versionamento

```text
/api/v1
```

Exemplo:

```text
/api/v1/comunicados
/api/v1/documentos
/api/v1/notificacoes
```

---

# Segurança

## Autenticação

```text
JWT
```

## Autorização

```text
Role Based Access Control (RBAC)
```

## Framework

```text
Spring Security
```

---

# Observabilidade

## Métricas

```text
Micrometer
```

## Monitoramento

```text
Prometheus
Grafana
```

## Health Checks

```text
Spring Boot Actuator
```

Endpoints:

```text
/actuator/health
/actuator/info
/actuator/metrics
```

---

# Containerização

## Docker

Utilizado para:

* Backend
* Oracle Local
* Ferramentas auxiliares

---

## Docker Compose

Utilizado para:

* Ambiente local
* Desenvolvimento integrado
* Execução simplificada do projeto

---

# Controle de Versão

## Ferramenta

```text
Git
```

## Plataforma

```text
GitHub
```

---

# CI/CD

## Plataforma

```text
GitHub Actions
```

### Pipeline mínimo

1. Build
2. Testes automatizados
3. Análise estática
4. Empacotamento
5. Deploy

---

# Testes

## Backend

```text
JUnit 5
Mockito
Spring Test
Testcontainers
```

---

## Frontend

```text
Vitest
Vue Test Utils
```

---

## End-to-End

```text
Playwright
```

---

# Tecnologias Explicitamente Não Utilizadas

As tecnologias abaixo não fazem parte da stack oficial do projeto.

```text
Angular
React
Next.js
NestJS
Node.js Backend
MongoDB
MySQL
PostgreSQL
Redis (MVP)
Kafka (MVP)
RabbitMQ (MVP)
```

A adoção futura de qualquer uma dessas tecnologias deverá passar pelo processo formal de decisão arquitetural (ADR/DEC).

---

# Status

| Item            | Status   |
| --------------- | -------- |
| Java 25         | Aprovado |
| Spring Boot 4.x | Aprovado |
| Vue 3           | Aprovado |
| Quasar          | Aprovado |
| Oracle Database | Aprovado |
| Baseline DDL (DBA) | Aprovado — DEC-DB-019 |
| Docker          | Aprovado |
| GitHub Actions  | Aprovado |

---

# Referências

* Oracle Database Architecture Standards
* Spring Boot Reference Documentation
* Vue.js Documentation
* Quasar Framework Documentation
* OpenAPI Specification
* Oracle DDL baseline (`database/ddl/`)
