# Security — Backlog Técnico

| Módulo | Security |
| Prefixo | PF-SEC |
| Pacote | PKG-03 |
| Última atualização | 2026-07-08 |

---

# Tarefas

| ID | Descrição | Prioridade | Dependências | Estimativa | Critério de Conclusão |
|----|-----------|------------|--------------|------------|----------------------|
| PF-SEC-001 | Implementar `SecurityConfiguration` com SecurityFilterChain stateless (`SessionCreationPolicy.STATELESS`) | Alta | PF-CONF-001 | M (8h) | Chain configurada; sem HttpSession criada; teste de contexto aprovado |
| PF-SEC-002 | Implementar `CsrfConfiguration` com `CookieCsrfTokenRepository` e header `X-XSRF-TOKEN` | Alta | PF-SEC-001 | M (8h) | CSRF ativo em POST/PUT/DELETE; cookie CSRF emitido; teste aprovado |
| PF-SEC-003 | Implementar `JwtAuthenticationFilter` esqueleto — extrai JWT de cookie, valida estrutura básica (sem emissão) | Alta | PF-SEC-001 | M (8h) | Filtro na chain; request sem JWT em rota protegida → 401 |
| PF-SEC-004 | Implementar `RestAuthenticationEntryPoint` retornando `ErrorResponse` padronizado (401 UNAUTHORIZED) | Alta | PF-SEC-001 | P (4h) | Resposta JSON conforme `shared/dto/ErrorResponse` |
| PF-SEC-005 | Implementar `CorsConfiguration` com origins de `SecurityProperties.corsAllowedOrigins` | Média | PF-CONF-001, PF-SEC-001 | P (4h) | Preflight OPTIONS funcional; teste aprovado |
| PF-SEC-006 | Criar testes de segurança base — endpoints públicos vs protegidos, CSRF, CORS | Alta | PF-SEC-001 a 005 | M (8h) | Mínimo 4 cenários de teste aprovados |

---

# Estimativa Total

| Métrica | Valor |
|---------|-------|
| Tarefas | 6 |
| Estimativa | 3 dias |
| Prioridade dominante | Alta |

---

# Referências

- `README.md` — Visão do módulo
- `review.md` — Critérios de revisão
