# Integration — Review

| Módulo | Integration |
| Prefixo | PF-INT |
| Última atualização | 2026-07-08 |

---

# Critérios de Revisão

1. RestClient centralizado — sem clients ad-hoc em Features
2. Correlation ID propagado em outbound
3. Exceções de integração mapeadas no handler existente
4. IdentityProviderClient é interface — sem implementação Zimbra
5. Resiliência configurada conforme decisão CD-S1A-004
6. Contrato alinhado a authentication-architecture.md

---

# Checklist Técnico

| # | Item | Status |
|---|------|--------|
| 1 | RestClientConfiguration operacional | ⬜ |
| 2 | CorrelationIdInterceptor funcional | ⬜ |
| 3 | IntegrationException hierarchy | ⬜ |
| 4 | Handler 503 para indisponibilidade | ⬜ |
| 5 | Resiliência (timeout/retry/circuit breaker) | ⬜ |
| 6 | IdentityProviderClient interface definida | ⬜ |
| 7 | Teste mock aprovado | ⬜ |
| 8 | `mvn clean verify` — SUCCESS | ⬜ |

---

# Riscos

| Risco | Mitigação |
|-------|-----------|
| CR-S1A-006 Ausência Zimbra para testes | WireMock; contrato validado antes FT-AUTH |
| CD-S1A-004 decisão pendente | Resolver antes de PF-INT-004 |
| Acoplamento domínio-integração | Gateway pattern obrigatório |

---

# Pontos de Auditoria

- Verificar ausência de ZimbraClient concreto
- Verificar propagação Correlation ID
- Verificar contrato IdentityProviderClient vs arquitetura auth
- Verificar timeout default seguro (≤ 10s)

---

# Definition of Done do Módulo

| Critério | Atendido |
|----------|----------|
| PF-INT-001 a PF-INT-005 concluídas | ⬜ |
| Testes aprovados | ⬜ |
| Build SUCCESS | ⬜ |
| RB-04 readiness review atendido | ⬜ |

**Módulo aprovado:** ⬜ Sim / ⬜ Não

**Revisor:** _________________ **Data:** _________
