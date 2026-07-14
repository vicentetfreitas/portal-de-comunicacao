# Testing — Backlog Técnico

| Módulo | Testing |
| Prefixo | PF-TEST |
| Pacote | PKG-07 |
| Última atualização | 2026-07-08 |

---

# Tarefas

| ID | Descrição | Prioridade | Dependências | Estimativa | Critério de Conclusão |
|----|-----------|------------|--------------|------------|----------------------|
| PF-TEST-001 | Configurar perfil `test` e `application-test.yaml` com datasource conforme CD-S1A-001 | Alta | CD-S1A-001, PF-PERS-001 | M (8h) | Perfil test carrega sem erro; datasource funcional |
| PF-TEST-002 | Implementar `@IntegrationTest` e `AbstractIntegrationTest` com `@SpringBootTest(webEnvironment = RANDOM_PORT)` | Alta | PF-TEST-001 | M (8h) | Classe base utilizável; contexto Spring carrega |
| PF-TEST-003 | Implementar `TestSecurityContextFactory` — simula JWT em SecurityContext para testes | Alta | PF-SEC-003, PF-TEST-002 | M (8h) | Teste com contexto autenticado funcional |
| PF-TEST-004 | Criar teste de integração end-to-end do health endpoint usando AbstractIntegrationTest | Alta | PF-TEST-002, PF-WEB-002 | P (4h) | GET /api/v1/health → 200 com ApiResponse |
| PF-TEST-005 | Documentar convenções de teste — nomenclatura, organização, quando usar @WebMvcTest vs @IntegrationTest | Média | PF-TEST-001 a 004 | P (4h) | Seção em README.md do módulo Testing |

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
- `construction/07-open-decisions.md` — CD-S1A-001
