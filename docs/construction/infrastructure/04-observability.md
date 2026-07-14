# Observability

## Objetivo

Definir a estratégia de observabilidade do Portal de Comunicação.

Este documento estabelece os padrões para monitoramento, métricas, logs, rastreamento distribuído, alertas e visibilidade operacional da plataforma.

A observabilidade deve permitir identificar, diagnosticar e resolver problemas rapidamente, reduzindo indisponibilidades e aumentando a confiabilidade da solução.

---

# Escopo

Esta documentação cobre:

* Logs
* Métricas
* Tracing
* Dashboards
* Alertas
* Monitoramento de infraestrutura
* Monitoramento de aplicações
* Monitoramento de integrações
* SLI
* SLO
* SLA

Não cobre:

* Segurança operacional
* Gestão de incidentes
* Disaster Recovery

---

# Objetivos

Garantir:

* Visibilidade operacional
* Diagnóstico rápido
* Detecção precoce de falhas
* Observação de comportamento
* Monitoramento de experiência do usuário

---

# Princípios

Toda observabilidade deve ser:

* Centralizada
* Estruturada
* Correlacionável
* Automatizada
* Auditável
* Escalável

---

# Os Três Pilares

A observabilidade da plataforma será baseada em:

```text id="ahm2g4"
Logs

Métricas

Tracing
```

---

# Arquitetura

```text id="g8lx3a"
Applications
      │
      ▼
OpenTelemetry
      │
      ▼
+------------------+
| Metrics          |
| Logs             |
| Traces           |
+------------------+
      │
      ▼
Observability Stack
      │
      ▼
Grafana
```

---

# Stack Oficial

## Instrumentação

```text id="t8v1ik"
OpenTelemetry
```

---

## Métricas

```text id="tzcbkp"
Prometheus
```

---

## Dashboards

```text id="xxk4qx"
Grafana
```

---

## Logs

```text id="a4k9g6"
Loki
```

---

## Tracing

```text id="q1xtbz"
Tempo
```

---

## Frontend Errors

```text id="sxqvyo"
Sentry
```

---

# Correlation ID

Todas as requisições devem possuir:

```http id="j5x9fo"
X-Correlation-Id
```

---

# Fluxo

```text id="87d8r0"
Frontend
    │
    ▼
Backend
    │
    ▼
Integrações
```

Mesmo Correlation ID.

---

# Logging

## Objetivo

Permitir rastreamento completo das operações.

---

# Formato

Logs obrigatoriamente estruturados.

---

## Exemplo

```json id="cyrq2l"
{
  "timestamp": "2026-01-01T10:00:00Z",
  "level": "INFO",
  "service": "communication-api",
  "correlationId": "abc123",
  "message": "Communication created"
}
```

---

# Campos Obrigatórios

```text id="p9k7ja"
timestamp

level

service

environment

correlationId

message
```

---

# Níveis

## TRACE

Diagnóstico detalhado.

---

## DEBUG

Desenvolvimento.

---

## INFO

Operações normais.

---

## WARN

Situações anormais.

---

## ERROR

Falhas.

---

# Não Registrar

* Senhas
* Tokens
* Refresh Tokens
* Dados pessoais sensíveis
* Chaves privadas

---

# Centralização

Todos os logs devem ser enviados para:

```text id="xqdb6g"
Loki
```

---

# Métricas

## Objetivo

Acompanhar comportamento operacional.

---

# Backend

Utilizar:

```text id="6o80bi"
Micrometer
```

---

# Frontend

Utilizar:

```text id="g9yz9z"
OpenTelemetry
```

---

# Métricas Obrigatórias

## HTTP

```text id="11zj8m"
http_requests_total

http_request_duration
```

---

## JVM

```text id="sv7yob"
jvm_memory_used

jvm_threads
```

---

## Banco

```text id="b4l7mw"
db_connections

db_query_duration
```

---

## Integrações

```text id="cnq1ol"
integration_requests_total

integration_errors_total
```

---

## Frontend

```text id="u1r5mu"
page_load_time

js_errors_total
```

---

# Business Metrics

Monitorar indicadores de negócio.

---

## Exemplos MVP

```text id="spqv7a"
comunicados_created

documents_published

notifications_delivered
```

**Rastreabilidade:** EPIC-004 (Documentos), EPIC-005 (Comunicação Interna).

---

## Obsoleto (fora do MVP)

> Não implementar — removidos por `docs/audit/10-mvp-consolidation-audit.md`.

```text
communications_created
messages_sent
campaigns_created
campaigns_finished
```

---

# Tracing

## Objetivo

Permitir rastreamento distribuído.

---

# Ferramenta

```text id="4syjhn"
OpenTelemetry
```

---

# Coletor

```text id="i67g5h"
OpenTelemetry Collector
```

---

# Armazenamento

```text id="c9izw8"
Tempo
```

---

# Fluxo

```text id="f3vf8q"
Frontend
     │
     ▼
Backend
     │
     ▼
Database
     │
     ▼
External APIs
```

---

# Spans Obrigatórios

## HTTP

```text id="tjlwm6"
GET

POST

PUT

DELETE
```

---

## Banco

```text id="ckep3d"
SELECT

INSERT

UPDATE
```

---

## Integrações

```text id="5hyvfw"
External Calls
```

---

# Dashboards

Todos os dashboards devem ser mantidos em Grafana.

---

# Dashboard Executivo

Indicadores:

```text id="brhjlwm"
Disponibilidade

Volume

Performance
```

---

# Dashboard Aplicação

Indicadores:

```text id="ivz0z5"
Requests

Errors

Latency
```

---

# Dashboard Infraestrutura

Indicadores:

```text id="v85vg8"
CPU

Memory

Disk

Network
```

---

# Dashboard Banco

Indicadores:

```text id="mtq6ix"
Connections

Queries

Locks
```

---

# Dashboard Integrações

Indicadores:

```text id="9zh3t9"
Success Rate

Latency

Failures
```

---

# Dashboard Frontend

Indicadores:

```text id="i3u44q"
Page Load

Errors

Navigation
```

---

# Alertas

Objetivo:

Detecção automática de problemas.

---

# Categorias

## Crítico

Ação imediata.

---

## Alto

Ação rápida.

---

## Médio

Monitoramento.

---

## Baixo

Acompanhamento.

---

# Alertas Obrigatórios

## Aplicação

```text id="lxws3o"
Error Rate > 5%
```

---

```text id="a3h80z"
Latency > 2 segundos
```

---

```text id="v63n2s"
Availability < 99%
```

---

## Banco

```text id="y9gvz0"
Connection Pool Exhausted
```

---

```text id="6gijr0"
Query Slow
```

---

## Infraestrutura

```text id="lgb5hm"
CPU > 80%
```

---

```text id="dgg2an"
Memory > 85%
```

---

```text id="3o5tkw"
Disk > 80%
```

---

# Notificações

Destinos:

```text id="l4jz0r"
Teams

Slack

Email
```

---

# SLI

Service Level Indicators.

---

## Disponibilidade

```text id="nyg9j9"
Availability
```

---

## Latência

```text id="gyn8ff"
Response Time
```

---

## Erros

```text id="shmk4z"
Error Rate
```

---

# SLO

Service Level Objectives.

---

## API

```text id="owif4y"
99.9%
```

Disponibilidade.

---

## Latência

```text id="cfdm9j"
95% < 500ms
```

---

## Erros

```text id="odjvjl"
< 1%
```

---

# SLA

Compromisso formal.

---

## Produção

```text id="p4txdf"
99.5%
```

Disponibilidade mínima.

---

# Monitoramento de Experiência

Frontend deve monitorar:

---

## Core Web Vitals

```text id="zv1bqz"
LCP

CLS

INP
```

---

# Metas

## LCP

```text id="2hd3u0"
< 2.5s
```

---

## CLS

```text id="sk6p1q"
< 0.1
```

---

## INP

```text id="gl6znq"
< 200ms
```

---

# Health Checks

Todos os componentes devem possuir endpoint de saúde.

---

## Backend

```http id="vpx4xk"
/actuator/health
```

---

## Readiness

```http id="drkv9x"
/actuator/health/readiness
```

---

## Liveness

```http id="jlwm3r"
/actuator/health/liveness
```

---

# Retenção

## Logs

```text id="jg7x1y"
30 dias
```

---

## Métricas

```text id="pdtfwo"
12 meses
```

---

## Traces

```text id="44t7x5"
15 dias
```

---

# Estrutura Recomendada

```text id="dhf65q"
observability
├── dashboards
├── alerts
├── metrics
├── logs
├── tracing
└── runbooks
```

---

# Runbooks

Todo alerta crítico deve possuir:

* Diagnóstico
* Causa provável
* Procedimento
* Escalonamento

---

# Checklist

Antes de produção:

* [ ] Logs estruturados
* [ ] Correlation ID implementado
* [ ] Métricas exportadas
* [ ] Tracing habilitado
* [ ] Dashboards criados
* [ ] Alertas configurados
* [ ] SLO definido
* [ ] SLA definido
* [ ] Runbooks criados
* [ ] Observabilidade validada

---

# Critérios de Aceite

A estratégia de observabilidade será considerada aderente quando:

* Logs, métricas e traces estiverem implementados.
* Toda requisição possuir Correlation ID.
* Dashboards cobrirem backend, frontend e infraestrutura.
* Alertas críticos estiverem automatizados.
* SLI, SLO e SLA estiverem definidos.
* O diagnóstico de incidentes puder ser realizado sem acesso direto aos servidores.
* A plataforma possuir rastreabilidade ponta a ponta entre frontend, backend, banco de dados e integrações externas.
