# Security — Review

| Módulo | Security |
| Prefixo | PF-SEC |
| Última atualização | 2026-07-08 |

---

# Critérios de Revisão

1. Arquitetura stateless — sem HTTP Session
2. CSRF configurado para fluxos com cookies (preparação FT-AUTH)
3. JwtAuthenticationFilter é esqueleto — sem emissão de tokens
4. Nenhum fluxo Zimbra implementado
5. ErrorResponse padronizado para 401
6. Whitelist mínima e documentada

---

# Checklist Técnico

| # | Item | Status |
|---|------|--------|
| 1 | SecurityFilterChain STATELESS | ⬜ |
| 2 | CSRF CookieCsrfTokenRepository ativo | ⬜ |
| 3 | JwtAuthenticationFilter na chain | ⬜ |
| 4 | RestAuthenticationEntryPoint com ErrorResponse | ⬜ |
| 5 | CORS configurado | ⬜ |
| 6 | `/api/v1/health` acessível sem auth | ⬜ |
| 7 | Rota protegida retorna 401 sem token | ⬜ |
| 8 | Testes segurança aprovados | ⬜ |
| 9 | `mvn clean verify` — SUCCESS | ⬜ |

---

# Riscos

| Risco | Mitigação |
|-------|-----------|
| CR-S1A-001 Escopo invadir FT-AUTH | Sem login/callback/JWT emission |
| CR-S1A-002 Dependência circular | Configurar por URL patterns |
| CSRF bloquear health endpoint | Whitelist correta |

---

# Pontos de Auditoria

- Verificar `SessionCreationPolicy.STATELESS`
- Verificar ausência de `HttpSession` usage
- Verificar ausência de endpoints `/api/v1/auth/*`
- Verificar integração com SecurityConstants (Sprint 0)
- Verificar extensibilidade para FT-AUTH

---

# Definition of Done do Módulo

| Critério | Atendido |
|----------|----------|
| PF-SEC-001 a PF-SEC-006 concluídas | ⬜ |
| Testes aprovados | ⬜ |
| Build SUCCESS | ⬜ |
| Checklist 100% | ⬜ |
| RB-01 e RB-02 do readiness review atendidos | ⬜ |

**Módulo aprovado:** ⬜ Sim / ⬜ Não

**Revisor:** _________________ **Data:** _________
