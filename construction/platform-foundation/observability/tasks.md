# Observability — Backlog Técnico

| Módulo | Observability |
| Prefixo | PF-OBS |
| Pacote | PKG-06 |
| Última atualização | 2026-07-08 |

---

# Tarefas

| ID | Descrição | Prioridade | Dependências | Estimativa | Critério de Conclusão |
|----|-----------|------------|--------------|------------|----------------------|
| PF-OBS-001 | Implementar `MetricsConfiguration` — registrar métricas HTTP (counter, timer) com naming conforme CD-S1A-005 | Média | PF-WEB-002, CD-S1A-005 | M (8h) | Métricas visíveis em `/actuator/metrics`; naming documentado |
| PF-OBS-002 | Implementar `RequestLoggingFilter` — log estruturado: method, uri, status, durationMs, correlationId | Alta | PF-WEB-002, Sprint 0 logging | M (8h) | Log emitido por requisição; correlationId presente; sem dados sensíveis |
| PF-OBS-003 | Implementar `DatabaseHealthIndicator` — verifica conectividade Oracle | Alta | PF-PERS-001 | P (4h) | `/actuator/health` inclui `db: UP/DOWN` |
| PF-OBS-004 | Configurar Actuator endpoints — expor health, metrics, info; restringir demais via Security | Média | PF-OBS-001 a 003, PF-SEC-001 | P (4h) | Apenas endpoints autorizados expostos |
| PF-OBS-005 | Criar testes — métricas registradas, log emitido, health indicator funcional | Alta | PF-OBS-001 a 004 | M (8h) | Mínimo 3 testes aprovados |

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
