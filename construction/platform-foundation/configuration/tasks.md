# Configuration — Backlog Técnico

| Módulo | Configuration |
| Prefixo | PF-CONF |
| Pacote | PKG-01 |
| Última atualização | 2026-07-08 |

---

# Tarefas

| ID | Descrição | Prioridade | Dependências | Estimativa | Critério de Conclusão |
|----|-----------|------------|--------------|------------|----------------------|
| PF-CONF-001 | Implementar `SecurityProperties` com prefixo `application.security` — campos: jwtIssuer, jwtAccessTtlMinutes, csrfEnabled, corsAllowedOrigins | Alta | Sprint 0 | P (4h) | Classe com `@ConfigurationProperties` + validação `@NotBlank` nos campos obrigatórios; teste `SecurityPropertiesTest` aprovado |
| PF-CONF-002 | Implementar `PersistenceProperties` com prefixo `application.persistence` — campos: poolMaxSize, poolMinIdle, showSql, ddlAuto | Alta | PF-CONF-001 | P (4h) | Classe validada; datasource URL permanece em `application.yaml` padrão Spring; teste aprovado |
| PF-CONF-003 | Implementar `IntegrationProperties` com prefixo `application.integration` — campos: connectTimeoutMs, readTimeoutMs, maxRetryAttempts, circuitBreakerThreshold | Alta | PF-CONF-001 | P (4h) | Valores default seguros (timeout 5s, retry 3); teste aprovado |
| PF-CONF-004 | Implementar `ZimbraProperties` com prefixo `application.zimbra` — campos: authUrl, validateUrl, timeoutMs | Alta | PF-CONF-003 | P (4h) | Estrutura definida; URLs via `${ZIMBRA_AUTH_URL:}` ; sem implementação de client |
| PF-CONF-005 | Criar `*PropertiesConfiguration` para cada properties e testes de carregamento nos perfis local, dev, hml | Alta | PF-CONF-001 a 004 | M (8h) | 4 classes Configuration + testes `@SpringBootTest` por perfil aprovados |

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
