# Persistence

**Fonte normativa MVP:** `docs/audit/10-mvp-consolidation-audit.md`  
**Pré-requisito:** `docs/governance/history/phase1-frontend-construction-report.md`

## Objetivo

Definir a implementação da persistência da aplicação.

---

# Banco de Dados

PostgreSQL.

**Rastreabilidade:** `docs/implementation/06-database-standards.md`, `docs/implementation/04-backend-architecture.md`.

---

# Estratégia

Persistência via:

* Spring Data JPA
* Hibernate

---

# Estrutura

```text
infrastructure
└── persistence
    ├── entity
    ├── repository
    ├── mapper
    └── specification
```

---

# Evolução do schema (DBA)

O schema Oracle é administrado pelo DBA através do baseline DDL oficial (`docs/database/ddl/`). A aplicação pressupõe schema previamente criado (DEC-DB-019). Flyway não é utilizado.

Estrutura de referência:

```text
docs/database/ddl/          — baseline oficial
docs/database/migrations/   — evoluções pós-baseline (DBA)
```

---

## Obsoleto (fora do MVP)

> Não implementar — removidos por `docs/audit/10-mvp-consolidation-audit.md`.

```text
V2__create_campaign.sql
V3__create_message.sql
```

---

# Convenções

## Tabelas

snake_case

## Colunas

snake_case

## Chaves

PK:

```text
id
```

FK:

```text
comunicado_id
document_id
```

---

# Índices

Criar índices para:

* Busca frequente
* Chaves estrangeiras
* Campos de integração

---

# Transações

Utilizar:

```java
@Transactional
```

apenas na camada Application.

---

# Critérios de Aceite

* Todas as alterações versionadas
* Sem DDL manual
* Rollback possível
* Scripts auditáveis
