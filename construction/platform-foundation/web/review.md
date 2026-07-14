# Web — Review

| Módulo | Web |
| Prefixo | PF-WEB |
| Última atualização | 2026-07-08 |

---

# Critérios de Revisão

1. Apenas health endpoint na Sprint 1A
2. Respostas seguem ApiResponse (Sprint 0)
3. Controllers sem lógica de negócio
4. OpenAPI documenta endpoints existentes
5. Health na whitelist de segurança
6. Prefixo `/api/v1` respeitado

---

# Checklist Técnico

| # | Item | Status |
|---|------|--------|
| 1 | Estrutura interfaces/rest/ criada | ⬜ |
| 2 | HealthController operacional | ⬜ |
| 3 | ApiResponse wrapper aplicado | ⬜ |
| 4 | Whitelist segurança configurada | ⬜ |
| 5 | OpenAPI / Swagger UI operacional | ⬜ |
| 6 | GlobalExceptionHandler integrado | ⬜ |
| 7 | Testes WebMvcTest aprovados | ⬜ |
| 8 | Teste integração health aprovado | ⬜ |
| 9 | `mvn clean verify` — SUCCESS | ⬜ |

---

# Riscos

| Risco | Mitigação |
|-------|-----------|
| CD-S1A-002 SpringDoc incompatível SB 4 | Validar versão antes de PF-WEB-004 |
| Controller com lógica de negócio | Code review; health apenas retorna status |
| Endpoints auth antecipados | Apenas /api/v1/health permitido |

---

# Pontos de Auditoria

- Verificar ausência de controllers de domínio
- Verificar contrato ApiResponse no health
- Verificar documentação OpenAPI atualizada
- Verificar versionamento /api/v1

---

# Definition of Done do Módulo

| Critério | Atendido |
|----------|----------|
| PF-WEB-001 a PF-WEB-005 concluídas | ⬜ |
| Testes aprovados | ⬜ |
| Build SUCCESS | ⬜ |
| RB-05 readiness review atendido | ⬜ |

**Módulo aprovado:** ⬜ Sim / ⬜ Não

**Revisor:** _________________ **Data:** _________
