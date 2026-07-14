# Migrations — Evolução Estrutural (DBA)

| Item | Valor |
|------|-------|
| Schema | UNMPORTCOM |
| Baseline | `docs/database/ddl/` |
| Status | Ativo |

---

## Objetivo

Registrar scripts de evolução estrutural **após** o baseline DDL corporativo.

O schema Oracle é administrado pelo **DBA** através do baseline DDL oficial do projeto. A aplicação pressupõe schema previamente criado.

Instalações **greenfield** devem utilizar exclusivamente `ddl/000-install.sql`.

---

## Baseline oficial

```text
ddl/000-install.sql
ddl/001-create-users.sql
ddl/002-create-sequences.sql
ddl/003-create-tables.sql
ddl/004-create-constraints.sql
ddl/005-create-indexes.sql
ddl/006-create-comments.sql
ddl/007-create-grants.sql
ddl/008-initial-data.sql
ddl/901-validation.sql
```

---

## Scripts pós-baseline

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V003 | `V003__auth_sessao_and_colaborador_zimbra.sql` | Evolução incremental legada — **desnecessário** se `000-install.sql` já aplicado |

Evoluções futuras: novos scripts versionados nesta pasta, executados pelo DBA.

---

## Ordem de execução

```text
Greenfield:
  ddl/001-create-users.sql (SYS/DBA)
  ddl/000-install.sql      (UNMPORTCOM)
  ddl/901-validation.sql

Evolução pós-baseline:
  migrations/V00X__<descricao>.sql (DBA)
```

---

## Referência

DEC-DB-019 — Flyway não é utilizado. Baseline DDL é a única fonte oficial do schema.
