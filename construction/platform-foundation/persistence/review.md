# Persistence — Review



| Módulo | Persistence |

| Prefixo | PF-PERS |

| Última atualização | 2026-07-09 |



---



# Critérios de Revisão



1. JPA configurado para Oracle (schema UNMPORTCOM)

2. Nenhuma entidade de domínio criada

3. V1__baseline.sql não alterado

4. BaseEntity/AuditableEntity sem regras de negócio

5. Exceções de persistência mapeadas no handler existente

6. Testes de integração Oracle aprovados



---



# Checklist Técnico



| # | Item | Status |

|---|------|--------|

| 1 | JpaConfiguration operacional | ✅ |

| 2 | BaseEntity com @MappedSuperclass | ✅ |

| 3 | AuditableEntity com timestamps automáticos | ✅ |

| 4 | BaseRepository funcional | ✅ |

| 5 | PersistenceException + handler | ✅ |

| 6 | Teste integração Oracle aprovado | ✅ |

| 7 | Dialect Oracle configurado explicitamente | ✅ |

| 8 | `mvn clean verify` — SUCCESS | ⬜ |

| 9 | Sem regressão Sprint 0 | ⬜ |



---



# Riscos



| Risco | Mitigação |

|-------|-----------|

| CR-S1A-004 Oracle 11.2 dialect warnings | Dialect explícito (`org.hibernate.dialect.OracleDialect`); monitorar logs de startup |

| Entidade de domínio criada prematuramente | Apenas `TestAuditableEntity` em `src/test` |

| Conflito com baseline DDL | `docs/database/ddl/` como fonte oficial |



---



# Pontos de Auditoria



- Verificar ausência de `@Entity` de domínio em `infrastructure/persistence/`

- Verificar schema UNMPORTCOM respeitado

- Verificar transações não aplicadas em repositories (apenas Application layer)

- Verificar compatibilidade com FT-AUTH (AuditableEntity extensível)



---



# Definition of Done do Módulo



| Critério | Atendido |

|----------|----------|

| PF-PERS-001 a PF-PERS-006 concluídas | ✅ |

| Testes aprovados | ✅ (módulo persistence) |

| Build SUCCESS | ⬜ |

| Checklist 100% | ⬜ |

| Progresso atualizado | ✅ |



**Módulo aprovado:** ⬜ Sim / ⬜ Não



**Revisor:** _________________ **Data:** _________

