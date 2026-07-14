# Persistence — Backlog Técnico

| Módulo | Persistence |
| Prefixo | PF-PERS |
| Pacote | PKG-02 |
| Última atualização | 2026-07-08 |

---

# Tarefas

| ID | Descrição | Prioridade | Dependências | Estimativa | Critério de Conclusão |
|----|-----------|------------|--------------|------------|----------------------|
| PF-PERS-001 | Implementar `JpaConfiguration` — EntityManagerFactory, TransactionManager, dialect Oracle explícito | Alta | PF-CONF-002 | M (8h) | Contexto Spring Data JPA inicializa; log sem erro de dialect |
| PF-PERS-002 | Implementar `BaseEntity` com `@MappedSuperclass` — campo `id` (UUID), equals/hashCode por id | Alta | PF-PERS-001 | P (4h) | Classe abstrata testável com entidade concreta de teste |
| PF-PERS-003 | Implementar `AuditableEntity` estendendo BaseEntity — createdAt, updatedAt com `@CreationTimestamp`/`@UpdateTimestamp` | Alta | PF-PERS-002 | P (4h) | Campos de auditoria populados automaticamente em teste |
| PF-PERS-004 | Implementar `BaseRepository<T extends BaseEntity, ID>` estendendo `JpaRepository` | Alta | PF-PERS-002 | P (4h) | CRUD funcional com entidade de teste |
| PF-PERS-005 | Implementar `PersistenceException` e registrar handler no `GlobalExceptionHandler` existente | Média | PF-PERS-001 | P (4h) | DataAccessException mapeada para ErrorResponse 500 |
| PF-PERS-006 | Criar testes de integração — transação de leitura contra Oracle (perfil local) | Alta | PF-PERS-001 a 005 | M (8h) | Teste confirma conexão e transação read-only funcional |

---

# Estimativa Total

| Métrica | Valor |
|---------|-------|
| Tarefas | 6 |
| Estimativa | 3 dias |
| Prioridade dominante | Alta |

---

# Convenções DDL (Referência — DEC-DB-019)

```text
V{n}__{descricao_snake_case}.sql

Exemplos futuros (FT-AUTH):
V2__create_auth_sessao.sql
V3__create_colaborador.sql
```

---

# Referências

- `README.md` — Visão do módulo
- `review.md` — Critérios de revisão
