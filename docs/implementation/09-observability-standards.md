# Observability Standards

## Documento

```text
docs/implementation/09-observability-standards.md
```

---

# Objetivo

Definir os padrões oficiais de observabilidade do Portal de Comunicação.

Este documento estabelece:

* logging
* monitoramento
* métricas
* rastreamento
* correlação
* alertas
* health checks
* diagnóstico operacional

O objetivo é garantir visibilidade operacional completa da solução.

---

# Escopo

Aplica-se a:

```text
Backend
Frontend
Banco de Dados
Storage
Containers
Proxy
Integrações Externas
```

---

# Princípios

## Observabilidade por Design

Toda funcionalidade deve nascer observável.

Não é permitido implementar funcionalidades sem:

* logs
* métricas
* rastreabilidade

---

## Diagnóstico Rápido

A observabilidade deve permitir identificar:

```text
O que aconteceu?
Onde aconteceu?
Quando aconteceu?
Quem foi impactado?
Qual a causa provável?
```

---

## Correlação

Toda operação deve ser rastreável ponta a ponta.

---

# Pilares de Observabilidade

## Logs

Registrar eventos relevantes.

---

## Métricas

Medir comportamento da aplicação.

---

## Rastreamento

Correlacionar requisições.

---

## Health Checks

Determinar saúde operacional.

---

# Logging

## Estrutura

Todos os logs devem ser:

```text
Estruturados
```

## Destino em disco (backend)

Logs de aplicação são gravados exclusivamente em `backend/runtime/logs/` (ex.: `application.log`).

Não gravar logs na raiz de `backend/` nem em diretórios fora de `runtime/`.

Convenção completa: `docs/construction/backend/01-project-bootstrap.md` § Artefatos de Runtime.

---

## Formato

Preferencialmente JSON.

Na Sprint 0, o padrão de console é texto simples configurado em `application.yaml`.

---

## Exemplo (formato atual — Sprint 0)

```text
2026-07-08 14:00:00 INFO  [http-nio-8080-exec-1] b.c.u.p.s.e.GlobalExceptionHandler - Unexpected error at /api/v1/documents
```

---

## Estado da Sprint 0

O Correlation ID é propagado via MDC e header HTTP, porém o padrão de log atual **não inclui** o Correlation ID na linha de saída.

**Pendência:** incluir `%X{correlationId}` no padrão de log (Sprint futura).

---

# Campos Obrigatórios

Todos os logs devem conter:

```text
timestamp
level
service
environment
event
```

Na Sprint 0, `service` e `environment` ainda não são injetados automaticamente no padrão de console.

**Pendência:** padronizar campos `service` e `environment` no padrão de log (Sprint futura).

---

# Níveis de Log

## ERROR

Falhas que impedem execução.

---

## WARN

Comportamento inesperado.

---

## INFO

Eventos relevantes de negócio.

---

## DEBUG

Diagnóstico técnico.

---

## TRACE

Investigação detalhada.

Não habilitar em produção.

---

# Eventos Obrigatórios

## Controle de Acesso

```text
LOGIN_SUCCESS
LOGIN_FAILURE
SESSION_CREATED
SESSION_EXPIRED
ACCESS_DENIED
```

---

## Gestão Documental

```text
DOCUMENT_CREATED
DOCUMENT_UPDATED
DOCUMENT_DELETED
DOCUMENT_DOWNLOADED
DOCUMENT_SHARED
```

---

## Comunicação

```text
NOTIFICATION_SENT
NOTIFICATION_READ
COMMUNICATION_PUBLISHED
```

---

## Organização

```text
TEAM_CREATED
TEAM_UPDATED
MEMBERSHIP_CHANGED
```

---

# Dados Sensíveis

## Nunca Registrar

```text
Senhas
Tokens
Secrets
Dados pessoais sensíveis
Credenciais
```

---

## Mascaramento

Obrigatório quando aplicável.

---

# Correlation ID

## Objetivo

Rastrear uma operação completa.

---

## Header Oficial

```http
X-Correlation-Id
```

Constante: `HeaderConstants.X_CORRELATION_ID`

---

## Infraestrutura

Responsabilidade de `infrastructure/logging/`:

| Componente               | Responsabilidade                                      |
| ------------------------ | ----------------------------------------------------- |
| `LoggingConfiguration`   | Registra `CorrelationIdFilter` com `HIGHEST_PRECEDENCE` |
| `CorrelationIdFilter`    | Resolve, propaga e limpa o Correlation ID por requisição |
| `CorrelationIdGenerator` | Gera UUID quando header ausente                       |
| `MdcUtils`               | Wrapper sobre SLF4J MDC (`put`, `get`, `remove`, `clear`) |
| `LoggingConstants`       | Chaves MDC e identificadores lógicos                  |

---

## MDC

Chave oficial do Correlation ID no MDC:

```text
correlationId
```

Constante: `LoggingConstants.MDC_CORRELATION_ID`

Chave reservada para uso futuro:

```text
requestId
```

Constante: `LoggingConstants.MDC_REQUEST_ID` (definida, não utilizada na Sprint 0).

---

## Fluxo do Filtro

```text
HTTP Request
    ↓
CorrelationIdFilter (HIGHEST_PRECEDENCE)
    ↓
resolveCorrelationId():
  - se X-Correlation-Id presente e não vazio → reutiliza (trim)
  - senão → CorrelationIdGenerator.generate() (UUID)
    ↓
MdcUtils.put("correlationId", id)
response.setHeader("X-Correlation-Id", id)
    ↓
filterChain.doFilter(...)
    ↓
finally: MdcUtils.clear()
```

---

## Regras

Toda requisição deve:

```text
Receber
Propagar
Registrar
```

o Correlation ID.

Na Sprint 0, **receber** e **propagar** estão implementados. **Registrar** no output de log é pendência para Sprint futura.

---

## Configuração Atual

```yaml
logging:
  level:
    root: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger - %msg%n"
```

O padrão não inclui `%X{correlationId}`.

**Pendência:** atualizar o padrão de log para incluir o Correlation ID do MDC (Sprint futura).

---

# Request Logging

## Registrar

```text
Método
Endpoint
Status
Duração
Correlation ID
```

Na Sprint 0, o request logging estruturado (método, endpoint, status, duração) ainda não está implementado.

**Pendência:** implementar request logging estruturado com Correlation ID (Sprint futura).

---

## Exemplo (alvo)

```json
{
  "event": "HTTP_REQUEST",
  "method": "GET",
  "endpoint": "/api/v1/documents",
  "status": 200,
  "durationMs": 42,
  "correlationId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

---

# Métricas

## Objetivo

Medir comportamento da plataforma.

Na Sprint 0, apenas `spring-boot-starter-actuator` está no classpath. Métricas customizadas e integração com Prometheus/Grafana não estão configuradas.

**Pendência:** configurar Micrometer, Prometheus e dashboards (Sprint futura).

---

# Métricas de Backend

## Requisições

```text
http_requests_total
```

---

## Latência

```text
http_request_duration
```

---

## Erros

```text
http_errors_total
```

---

## Sessões

```text
active_sessions
```

---

## Integrações

```text
zimbra_requests_total
storage_requests_total
```

---

# Métricas de Gestão Documental

## Documentos

```text
documents_created
documents_downloaded
documents_shared
```

---

## Storage

```text
storage_used
storage_available
```

---

# Métricas de Comunicação

```text
notifications_sent
notifications_read
notification_failures
```

---

# Métricas de Infraestrutura

## Containers

```text
cpu_usage
memory_usage
disk_usage
```

---

## Banco

```text
db_connections
db_query_duration
db_errors
```

---

# Health Checks

## Objetivo

Avaliar disponibilidade.

Na Sprint 0, o Actuator está no classpath mas endpoints de health customizados não foram implementados.

**Pendência:** configurar health checks com verificações de banco e integrações (Sprint futura).

---

# Endpoint

```http
/actuator/health
```

Disponível via `spring-boot-starter-actuator` (configuração padrão do Spring Boot).

---

# Status

## UP

Serviço saudável.

---

## DOWN

Serviço indisponível.

---

## DEGRADED

Serviço parcialmente funcional.

---

# Verificações Obrigatórias

## Backend

```text
Aplicação
Banco
Storage
```

---

## Controle de Acesso

```text
Zimbra
```

---

## Comunicação

```text
Webhook
Email
```

quando aplicável.

---

# Monitoramento

## Componentes Obrigatórios

```text
Backend
Frontend
Banco
Storage
Proxy
```

---

# Componentes Críticos

```text
Backend API
Banco
Zimbra
Storage
```

---

# Alertas

## Criticidade

### Crítico

Ação imediata.

---

### Alto

Correção prioritária.

---

### Médio

Acompanhamento.

---

### Baixo

Registro.

---

# Alertas Obrigatórios

## Backend

```text
Serviço indisponível
Erro acima do limite
Latência excessiva
```

---

## Banco

```text
Falha de conexão
Espaço insuficiente
```

---

## Storage

```text
Falha de escrita
Falha de leitura
```

---

## Zimbra

```text
Autenticação indisponível
Tempo de resposta elevado
```

---

# Auditoria

## Eventos Auditáveis

Devem ser observáveis:

```text
Login
Permissões
Documentos
Compartilhamentos
Publicações
```

---

# Frontend

## Eventos

Registrar:

```text
Erros de navegação
Falhas de API
Falhas de carregamento
```

---

## Nunca Registrar

```text
Credenciais
Tokens
Informações sensíveis
```

---

# Dashboards

## Operacional

Exibir:

```text
Saúde dos serviços
Uso de recursos
Erros
Latência
```

---

## Negócio

Exibir:

```text
Documentos publicados
Downloads
Notificações
Logins
```

---

# Ambientes

## Local

Logs completos.

---

## Dev

Logs completos.

---

## Hml

Observabilidade próxima à produção.

---

## Prod

Observabilidade completa.

---

# Critérios de Conformidade

Toda funcionalidade deve responder:

## Possui logs?

```text
SIM
```

Na Sprint 0: logging básico via SLF4J com nível INFO.

---

## Possui métricas?

```text
SIM
```

Na Sprint 0: **parcial** — Actuator no classpath, sem métricas customizadas.

**Pendência:** métricas operacionais completas (Sprint futura).

---

## Possui rastreamento?

```text
SIM
```

Na Sprint 0: **parcial** — Correlation ID via MDC e header, sem emissão no log.

**Pendência:** Correlation ID no padrão de log (Sprint futura).

---

## Possui health check?

```text
SIM
```

quando aplicável.

Na Sprint 0: Actuator disponível com configuração padrão.

---

# Não Conformidades

São considerados desvios:

* logs sem correlação
* ausência de métricas
* ausência de health checks
* logs contendo secrets
* ausência de monitoramento
* eventos críticos sem rastreabilidade

---

# Critérios de Aprovação

Uma funcionalidade somente pode ser considerada pronta quando:

```text
Observável
Monitorável
Auditável
Diagnósticável
```

---

# Conclusão

A observabilidade do Portal de Comunicação deve permitir rastrear integralmente o comportamento da plataforma, desde a requisição do usuário até os serviços internos e integrações externas.

Toda capacidade implementada deve produzir evidências operacionais suficientes para diagnóstico, monitoramento e auditoria, garantindo aderência aos requisitos arquiteturais e operacionais definidos para a solução.
