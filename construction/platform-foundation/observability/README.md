# Observability Module

| Item | Valor |
|------|-------|
| Módulo | Observability |
| Prefixo | PF-OBS |
| Pacote | `infrastructure/observability/` |
| Pacote Construction | PKG-06 |
| Status | Não iniciado |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Estender a observabilidade da Sprint 0 (Correlation ID + MDC) com métricas Micrometer, request logging estruturado e health indicators customizados.

---

# Escopo

## Inclui

- `MetricsConfiguration` — registry Micrometer com convenções de naming
- `RequestLoggingFilter` — log estruturado de requisições HTTP
- `DatabaseHealthIndicator` — saúde da conexão Oracle
- Configuração Actuator endpoints
- Integração Correlation ID → logs de requisição (emissão completa)

## Não inclui

- OpenTelemetry / distributed tracing (adiado)
- Dashboards Grafana (adiado)
- Métricas de negócio (Features)
- Alertas operacionais (infraestrutura)

---

# Responsabilidades

| Componente | Responsabilidade |
|------------|------------------|
| MetricsConfiguration | Registrar métricas HTTP, JVM, custom |
| RequestLoggingFilter | Log method, path, status, duration, correlationId |
| DatabaseHealthIndicator | Verificar conectividade Oracle |
| Actuator config | Expor health, metrics, info |

---

# Limites

- Sem dados sensíveis em logs (tokens, credenciais, PII)
- Sem métricas de domínio
- OpenTelemetry fora do escopo

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| PF-WEB | Web (request logging) | Pendente |
| PF-PERS | Persistence (DB health) | Pendente |
| PF-INT | Integration (métricas outbound) | Pendente |
| Sprint 0 Logging | `infrastructure/logging/` | Concluído |
| `docs/implementation/09-observability-standards.md` | Padrões | Consultivo |

---

# Componentes Esperados

```text
infrastructure/observability/
├── config/
│   └── MetricsConfiguration.java
├── filter/
│   └── RequestLoggingFilter.java
└── health/
    └── DatabaseHealthIndicator.java
```

---

# Convenção de Métricas

Conforme decisão CD-S1A-005 (pendente). Proposta default:

```text
portal.<modulo>.<metrica>
```

Exemplos:

```text
portal.http.requests.total
portal.http.requests.duration
portal.integration.calls.total
portal.database.connections.active
```

---

# Ordem de Construção

```text
PF-OBS-001 (MetricsConfiguration)
    → PF-OBS-002 (RequestLoggingFilter)
    → PF-OBS-003 (DatabaseHealthIndicator)
    → PF-OBS-004 (Actuator endpoints config)
    → PF-OBS-005 (Testes observabilidade)
```

---

# Critérios de Aceite

1. Métricas HTTP registradas via Micrometer
2. Request logging estruturado com Correlation ID
3. DatabaseHealthIndicator reporta status Oracle
4. Actuator `/actuator/health` inclui componente database
5. Actuator endpoints com segurança adequada
6. Nenhum dado sensível em logs

---

# Definition of Done do Módulo

- [ ] Todas as tarefas PF-OBS-* concluídas
- [ ] Testes aprovados
- [ ] `review.md` validado
- [ ] Build SUCCESS
- [ ] FT-AUTH pode registrar métricas de auth (TASK-AUTH-BE-012)

---

# Rastreabilidade

- `docs/implementation/09-observability-standards.md`
- `docs/construction/infrastructure/04-observability.md`
- `construction/03-construction-packages.md` § PKG-06
