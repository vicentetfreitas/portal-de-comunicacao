# Operação — Schema Owner × Application User

Documentação de operação para equipes de **DBA**, **DevOps** e **engenharia backend** (DEC-DB-024).

---

## 1. Schema Owner vs Application User

| | **UNMPORTCOM** (owner) | **UNMPORTCOM_APP** (application user) |
|---|------------------------|----------------------------------------|
| **Função** | Dono dos objetos; DDL; migrations; DML institucional | Conexão exclusiva do Spring Boot |
| **Privilégios típicos** | `CREATE TABLE`, `CREATE SEQUENCE`, … | `CREATE SESSION` + `UNMPORTCOM_APP_ROLE` |
| **Quota** | `UNLIMITED` em `USERS` | `0` (não cria objetos) |
| **Uso na aplicação** | **Proibido** | **Obrigatório** |
| **Credenciais** | Apenas DBA / pipelines de schema | Secrets do ambiente (K8s, vault, `.env` local) |

O owner **possui** os dados e estruturas. O application user **acessa** o que foi explicitamente concedido via **GRANT**.

---

## 2. Por que usuários distintos

1. **Menor privilégio** — comprometimento da credencial da aplicação não permite `DROP TABLE` nem alteração de schema.
2. **Separação de funções** — DBA evolui schema; aplicação só consome.
3. **Auditoria** — privilégios agrupados em `UNMPORTCOM_APP_ROLE`.
4. **Conformidade** — padrão corporativo Oracle (owner + app role).

---

## 3. Impacto no Hibernate / Spring Boot

Configuração esperada (ex.: `application-test.yaml`):

```yaml
spring:
  datasource:
    username: UNMPORTCOM_APP
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        default_schema: UNMPORTCOM
```

- **`default_schema=UNMPORTCOM`**: metadados JPA referenciam tabelas no owner.
- **`validate`**: Hibernate consulta catálogo Oracle; o usuário da conexão **precisa** ter privilégio `SELECT` (mínimo) sobre essas tabelas.
- Erro `missing table [unmportcom.<tabela>]` com tabela existente no owner → **quase sempre** falta de GRANT, não erro de entidade.

**Synonyms:** não são necessários porque o schema está explícito no `@Table` e os grants são no objeto real do owner. Ver `V901__application_user_synonyms.sql`.

---

## 4. Impacto em Flyway / migrations

| Mecanismo | Portal de Comunicação |
|-----------|------------------------|
| Flyway no backend | **Não** (DEC-DB-019) |
| `database/migrations/` | Scripts SQL executados pelo **DBA** (brownfield) |
| `database/security/` | Grants executados pelo **DBA** após DDL/migrations |

Ao criar objetos em migration:

1. `CREATE TABLE` / `CREATE SEQUENCE` como `UNMPORTCOM`.
2. Imediatamente: `GRANT ... TO UNMPORTCOM_APP_ROLE` (padrão `V003` para `AUTH_SESSAO`).
3. Ou reaplicar `V900` / `V902` no final do change window.

Não adicionar estes scripts ao classpath da aplicação.

---

## 5. Impacto na manutenção

| Atividade | Owner | App user |
|-----------|-------|----------|
| Nova tabela | `CREATE` + `GRANT` | Nenhuma alteração de usuário |
| Nova sequence | `CREATE` + `GRANT SELECT` | Idem |
| Rebuild de ambiente | Install completo | Reexecutar V900/V902 |
| Rotação de senha app | — | `ALTER USER UNMPORTCOM_APP IDENTIFIED BY ...` |
| Hibernate validate falhou | Verificar grants | Não trocar para owner na datasource |

Manter sincronizados:

- `database/ddl/007-create-grants.sql` (greenfield)
- `database/security/V900__application_user_grants.sql`
- `database/security/V902__application_user_sequences.sql`

---

## 6. Sequences e JPA

O backend usa `@SequenceGenerator(sequenceName = "...")` com nomes corporativos. O baseline `002-create-sequences.sql` define 12 sequences; alguns ambientes brownfield também possuem:

- `SQ_AUTH_SESSAO_COD_SESSAO`
- `SQ_COLABORADOR_COD_COLABORADOR`

`V902` concede grants no baseline e, via bloco PL/SQL, nas sequences estendidas **se existirem**. Sem `GRANT SELECT` na sequence, inserts com PK gerada falham em runtime.

---

## 7. Objetos não versionados

Análise de `database/`:

| Tipo | Quantidade no baseline | Grants |
|------|------------------------|--------|
| Tabelas | 23 | `V900` |
| Sequences | 12 (+ opcionais brownfield) | `V902` |
| Views | 0 | `V903` (n/a) |
| Materialized views | 0 | n/a |
| Packages / procedures / functions | 0 consumidos pelo backend | n/a |
| Synonyms | 0 para app | **Não criar** (`V901`) |

---

## 8. Exemplo de provisionamento mínimo (correção de ambiente)

```text
sqlplus UNMPORTCOM/<senha>@<tns>
@database/security/V900__application_user_grants.sql
@database/security/V902__application_user_sequences.sql

sqlplus UNMPORTCOM_APP/<senha>@<tns>
@database/security/VAL-SEC-01-verify-application-privileges.sql
```

Reiniciar aplicação com `ddl-auto=validate` e confirmar ausência de `SchemaValidationException`.
