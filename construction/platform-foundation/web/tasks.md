# Web — Backlog Técnico

| Módulo | Web |
| Prefixo | PF-WEB |
| Pacote | PKG-05 |
| Última atualização | 2026-07-08 |

---

# Tarefas

| ID | Descrição | Prioridade | Dependências | Estimativa | Critério de Conclusão |
|----|-----------|------------|--------------|------------|----------------------|
| PF-WEB-001 | Criar estrutura `interfaces/rest/` — diretórios controller, response, config | Alta | PF-SEC-001 | P (2h) | Estrutura conforme `docs/implementation/02-repository-structure.md` |
| PF-WEB-002 | Implementar `HealthController` (`GET /api/v1/health`) e `HealthResponse` com `ApiResponse` | Alta | PF-WEB-001 | P (4h) | Endpoint retorna 200 com status, application name e version |
| PF-WEB-003 | Registrar `/api/v1/health` na whitelist do SecurityFilterChain | Alta | PF-WEB-002, PF-SEC-001 | P (2h) | Health acessível sem autenticação |
| PF-WEB-004 | Implementar `OpenApiConfiguration` com SpringDoc conforme CD-S1A-002 | Média | PF-WEB-002, CD-S1A-002 | M (8h) | Swagger UI em `/swagger-ui.html`; health documentado |
| PF-WEB-005 | Criar testes `@WebMvcTest(HealthController)` e teste de integração do health endpoint | Alta | PF-WEB-001 a 004 | M (8h) | Mínimo 3 cenários: 200 OK, content-type JSON, estrutura ApiResponse |

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
- `construction/07-open-decisions.md` — CD-S1A-002, CD-S1A-003
