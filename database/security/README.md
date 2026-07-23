# Segurança Oracle — Application User (DEC-DB-024)

**Atividade:** INFRA-DB-01  
**SSOT:** privilégios do usuário de aplicação sobre objetos do schema owner `UNMPORTCOM`.

---

## Arquitetura (imutável)

```text
UNMPORTCOM (schema owner — DDL, migrations, DBA)
        │
        │ GRANT … TO UNMPORTCOM_APP_ROLE
        ▼
UNMPORTCOM_APP_ROLE
        │
        │ GRANT ROLE TO user
        ▼
UNMPORTCOM_APP  ← único usuário de conexão do backend (Spring Boot / Hibernate)
```

| Usuário | Uso |
|---------|-----|
| `UNMPORTCOM` | Owner: tabelas, sequences, índices, constraints. **Proibido** como `spring.datasource.username`. |
| `UNMPORTCOM_APP` | Conexão da aplicação, testes de integração e jobs. |
| `UNMPORTCOM_APP_ROLE` | Agregação de privilégios DML + `SELECT` em sequences. |

O Hibernate continua com `default_schema` / `@Table(schema = "UNMPORTCOM")` — isso qualifica objetos do **owner**, não o usuário JDBC.

---

## Estrutura

```text
security/
├── README.md                 ← este arquivo
├── CHECKLIST.md              ← entrega de Feature com novo objeto Oracle
├── grants/
│   ├── 001-baseline-tables.sql
│   ├── 002-baseline-sequences.sql
│   └── 003-role-to-application-user.sql
└── validate/
    └── validate-application-user.sql
```

Greenfield: `database/ddl/007-create-grants.sql` aplica o mesmo conteúdo na instalação inicial. **Evolução brownfield e reaplicação:** atualizar primeiro `database/security/`, depois espelhar em migration ou instruir DBA.

---

## Fluxo obrigatório (nova Feature)

```text
Novo objeto Oracle (tabela / sequence / view)
        ↓
Atualizar database/ddl/ ou database/migrations/
        ↓
Atualizar database/security/grants/
        ↓
Executar validate/validate-application-user.sql (DBA)
        ↓
Validar backend com SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP
```

Nenhuma Feature com persistência nova está concluída sem atualizar `database/security/`.

---

## Backend

| Variável | Valor esperado |
|----------|----------------|
| `SPRING_DATASOURCE_USERNAME` | `UNMPORTCOM_APP` |
| `SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA` | `UNMPORTCOM` |

Referência: `.env.example`, `docs/implementation/06-database-standards.md`.

---

## Execução (DBA)

1. Conectar como `UNMPORTCOM` (owner).
2. Aplicar scripts em `grants/` na ordem numérica (ou conteúdo equivalente já versionado em migration).
3. Rodar `validate/validate-application-user.sql` como `UNMPORTCOM_APP`.

---

## Referências

- [GOVERNANCE.md](../GOVERNANCE.md) — GOV-DB-05  
- [database/model/05-decisions-and-risks.md](../model/05-decisions-and-risks.md) — DEC-DB-024  
- [reports/infra-db-01-application-user-migration.md](../reports/infra-db-01-application-user-migration.md)
