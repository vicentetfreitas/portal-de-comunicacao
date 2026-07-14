# Persistence Module

| Item | Valor |
|------|-------|
| Módulo | Persistence |
| Prefixo | PF-PERS |
| Pacote | `infrastructure/persistence/` |
| Pacote Construction | PKG-02 |
| Status | Em revisão |
| Versão | 1.0 |
| Última atualização | 2026-07-09 |

---

# Objetivo

Estabelecer a camada de persistência reutilizável sobre Oracle Database, fornecendo entidades base, repositórios base e convenções JPA para Features futuras. Schema administrado pelo DBA (DEC-DB-019).

---

# Escopo

## Inclui

- `JpaConfiguration` — EntityManagerFactory, TransactionManager
- `BaseEntity` — id (UUID ou Long), equals/hashCode
- `AuditableEntity` — createdAt, updatedAt, createdBy, updatedBy
- `BaseRepository<T, ID>` — interface Spring Data JPA
- `PersistenceException` — exceção de infraestrutura
- Mapeamento de exceções JPA no `GlobalExceptionHandler`
- Convenções de evolução estrutural via scripts DDL em `docs/database/` (DBA)

## Não inclui

- Entidades de domínio (Colaborador, AUTH_SESSAO — FT-AUTH)
- Queries de negócio
- Migrations de Features (exceto convenções)
- Bounded contexts

---

# Responsabilidades

| Componente | Responsabilidade |
|------------|------------------|
| JpaConfiguration | Configurar JPA/Hibernate para Oracle UNMPORTCOM |
| BaseEntity | Campos e comportamento comum de todas as entidades |
| AuditableEntity | Auditoria temporal e de usuário |
| BaseRepository | Operações CRUD base via Spring Data |
| PersistenceException | Encapsular erros de persistência |

---

# Limites

- Sem regras de negócio em entidades base
- Sem `@Entity` de domínio
- Sem alteração de `V1__baseline.sql` (Sprint 0)
- Transações `@Transactional` apenas documentadas para camada Application (Features)

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| PF-CONF (PersistenceProperties) | Configuration | Pendente |
| Sprint 0 baseline DDL | `docs/database/ddl/` | Concluído |
| Oracle JDBC | `ojdbc11` | Concluído |
| `docs/implementation/06-database-standards.md` | Padrões Oracle | Consultivo |

---

# Componentes Esperados

```text
infrastructure/persistence/
├── config/
│   └── JpaConfiguration.java
├── entity/
│   ├── BaseEntity.java
│ └── AuditableEntity.java
├── repository/
│   └── BaseRepository.java
└── exception/
    └── PersistenceException.java
```

---

# Ordem de Construção

```text
PF-PERS-001 (JpaConfiguration)
    → PF-PERS-002 (BaseEntity)
    → PF-PERS-003 (AuditableEntity)
    → PF-PERS-004 (BaseRepository)
    → PF-PERS-005 (PersistenceException + handler)
    → PF-PERS-006 (Testes integração Oracle)
```

---

# Critérios de Aceite

1. Contexto JPA inicializa sem erro com Oracle
2. BaseEntity e AuditableEntity disponíveis para extensão
3. BaseRepository funcional com entidade de teste
4. PersistenceException mapeada no GlobalExceptionHandler
5. Convenções de evolução DDL documentadas (`docs/database/ddl/` e `migrations/`)
6. Teste de transação de leitura aprovado

---

# Definition of Done do Módulo

- [ ] Todas as tarefas PF-PERS-* concluídas
- [ ] Testes unitários e integração aprovados
- [ ] `review.md` validado
- [ ] Build SUCCESS
- [ ] DatabaseHealthIndicator preparado (consumido por Observability)

---

# Relação com FT-AUTH

| Componente Foundation | Uso FT-AUTH |
|-----------------------|-------------|
| BaseEntity / AuditableEntity | Entidade AUTH_SESSAO estende AuditableEntity |
| BaseRepository | AuthSessaoRepository estende BaseRepository |
| DDL conventions (DBA) | `AUTH_SESSAO` no baseline `docs/database/ddl/` |
| JpaConfiguration | Contexto para TASK-AUTH-DB-001, DB-002 |

---

# Rastreabilidade

- `docs/construction/backend/03-persistence.md`
- `docs/implementation/06-database-standards.md`
- `construction/03-construction-packages.md` § PKG-02
