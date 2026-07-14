# Integration — Backlog Técnico

| Módulo | Integration |
| Prefixo | PF-INT |
| Pacote | PKG-04 |
| Última atualização | 2026-07-08 |

---

# Tarefas

| ID | Descrição | Prioridade | Dependências | Estimativa | Critério de Conclusão |
|----|-----------|------------|--------------|------------|----------------------|
| PF-INT-001 | Implementar `RestClientConfiguration` — bean RestClient com connect/read timeout de IntegrationProperties | Alta | PF-CONF-003 | M (8h) | RestClient bean disponível; timeout configurável; teste aprovado |
| PF-INT-002 | Implementar `CorrelationIdInterceptor` — propaga `X-Correlation-Id` do MDC em requests outbound | Alta | PF-INT-001 | P (4h) | Header presente em chamada mock; teste com MockRestServiceServer |
| PF-INT-003 | Implementar `IntegrationException` e `IntegrationUnavailableException`; registrar no GlobalExceptionHandler (503) | Alta | PF-INT-001 | P (4h) | IntegrationUnavailableException → ErrorResponse 503 |
| PF-INT-004 | Configurar resiliência — timeout, retry e circuit breaker conforme CD-S1A-004 | Alta | PF-INT-001, CD-S1A-004 | M (8h) | Retry em falha transitória; circuit breaker abre após threshold |
| PF-INT-005 | Definir interface `IdentityProviderClient` com DTOs request/response; teste com implementação mock | Alta | PF-INT-001 a 004 | M (8h) | Interface alinhada a `authentication-architecture.md`; mock test aprovado |

---

# Estimativa Total

| Métrica | Valor |
|---------|-------|
| Tarefas | 5 |
| Estimativa | 2 dias |
| Prioridade dominante | Alta |

---

# Referências

- `README.md` — Visão do módulo
- `review.md` — Critérios de revisão
- `construction/07-open-decisions.md` — CD-S1A-004
