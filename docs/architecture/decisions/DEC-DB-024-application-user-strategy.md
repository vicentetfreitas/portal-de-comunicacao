# DEC-DB-024 — Estratégia de Usuário de Aplicação Oracle (Schema Owner × Application User)

| Campo | Valor |
|-------|--------|
| **ID** | DEC-DB-024 |
| **Status** | APPROVED |
| **Data** | 2026-07-23 |
| **Relacionado** | DEC-DB-019, DEC-DB-023, GOV-DB-01 |
| **SSOT operacional** | `database/security/` |

---

## Contexto

O Portal de Comunicação utiliza o schema Oracle **`UNMPORTCOM`** como **schema owner** (tabelas, sequences, índices, constraints, comentários). O backend Spring Boot / Hibernate conecta com o usuário **`UNMPORTCOM_APP`**, que possui apenas `CREATE SESSION` e a role corporativa **`UNMPORTCOM_APP_ROLE`**.

Durante validação com `spring.jpa.hibernate.ddl-auto=validate`, o Hibernate reportou:

```text
Schema validation: missing table [unmportcom.federacao]
```

Investigação:

| Fato | Evidência |
|------|-----------|
| A tabela existe | `UNMPORTCOM.FEDERACAO` presente no owner |
| Conexão da aplicação | `UNMPORTCOM_APP` |
| Privilégios do app user | `ALL_TAB_PRIVS` vazio para `UNMPORTCOM_APP` |
| Causa raiz | Ausência (ou não reaplicação) de **GRANT** dos objetos do owner para a role/usuário da aplicação |

Não é defeito de mapeamento JPA: as entidades usam `@Table(name = "...", schema = "UNMPORTCOM")` corretamente.

---

## Problema

Sem privilégios `SELECT` (e demais DML necessários) sobre tabelas e sequences do owner, o usuário da aplicação:

1. Não enxerga objetos em catálogo de metadados → falha na validação Hibernate.
2. Não consegue executar DML em runtime.
3. Não consegue consumir sequences para geração de PK (JPA `GenerationType.SEQUENCE`).

Provisionar apenas o DDL estrutural (`002`–`006`) **sem** a camada de segurança (`007` / `database/security/`) deixa o ambiente **inutilizável** para o backend.

---

## Decisão

Adotar oficialmente o modelo **Schema Owner × Application User** com **separação de privilégios via role**:

```text
UNMPORTCOM (owner)
    │  GRANT SELECT, INSERT, UPDATE, DELETE (tabelas)
    │  GRANT SELECT (sequences)
    ▼
UNMPORTCOM_APP_ROLE
    │  GRANT role TO user
    ▼
UNMPORTCOM_APP (conexão Spring Boot / Hibernate)
```

Regras obrigatórias:

| Regra | Descrição |
|-------|-----------|
| R-01 | O backend **nunca** conecta como `UNMPORTCOM`. |
| R-02 | O backend conecta **exclusivamente** como `UNMPORTCOM_APP`. |
| R-03 | Todo objeto do owner usado pela aplicação recebe **GRANT** para `UNMPORTCOM_APP_ROLE`. |
| R-04 | **Não** utilizar synonyms para tabelas do domínio; JPA referencia `schema = "UNMPORTCOM"`. |
| R-05 | Scripts de segurança são versionados em `database/security/` e executados pelo **DBA** (não pela aplicação). |
| R-06 | Toda **nova tabela** ou **sequence** em migration/baseline deve incluir grants correspondentes (ou reaplicar `V900`/`V902`). |

---

## Consequências

### Positivas

- Princípio do menor privilégio: app user sem `CREATE TABLE`, `DROP`, DDL.
- Auditoria centralizada via role.
- Reproducibilidade entre DEV/HML/PRD com os mesmos scripts.
- Alinhamento com `ddl/001-create-users.sql` e `ddl/007-create-grants.sql`.

### Negativas / custos

- DBA deve executar camada de segurança após DDL e após evoluções brownfield.
- Ambientes onde apenas o owner foi provisionado exibem erros de Hibernate até grants serem aplicados.
- Duplicidade controlada: `ddl/007-create-grants.sql` (install greenfield) e `database/security/V900*` (SSOT documentada e reaplicável).

### Hibernate

- `ddl-auto=validate` exige que `UNMPORTCOM_APP` **veja** tabelas do schema `UNMPORTCOM` via privilégios.
- Não é necessário synonym; é necessário **GRANT**.

### Flyway / migrations

- **Flyway não é utilizado** na aplicação (DEC-DB-019).
- Scripts em `database/migrations/` são executados pelo DBA; quando criam objetos, devem repetir o padrão de grant (ex.: `V003` em `AUTH_SESSAO`) ou o DBA reaplica `database/security/V900__application_user_grants.sql` e `V902__application_user_sequences.sql`.

---

## Responsabilidades

| Papel | Responsabilidade |
|-------|------------------|
| **DBA** | Criar usuários (`001`), aplicar DDL, aplicar `database/security/`, validar com `VAL-SEC-01`, conceder role ao app user |
| **Engenharia** | Manter inventário de objetos em `database/security/` alinhado ao baseline; atualizar grants em novas migrations |
| **Backend** | Manter `spring.datasource` com `UNMPORTCOM_APP`; `hibernate.default_schema=UNMPORTCOM`; **não** alterar owner na conexão |
| **DevOps** | Secrets do app user por ambiente; nunca deployar credenciais do owner na aplicação |

---

## Estratégia de provisionamento

### Greenfield

```text
1. SYS/DBA  → ddl/001-create-users.sql
2. UNMPORTCOM → ddl/000-install.sql (inclui 007-create-grants.sql)
3. DBA      → database/security/VAL-SEC-01-verify-application-privileges.sql (como UNMPORTCOM_APP)
4. Opcional → reaplicar database/security/V900 + V902 se 007 estiver desatualizado
```

### Brownfield (migrations)

```text
1. UNMPORTCOM → migrations/V00X__*.sql
2. UNMPORTCOM → database/security/V900__application_user_grants.sql (idempotente)
3. UNMPORTCOM → database/security/V902__application_user_sequences.sql (idempotente)
4. UNMPORTCOM_APP → VAL-SEC-01
```

---

## Estratégia de evolução

Ao adicionar **nova tabela** `NOVA_TABELA`:

```sql
-- Executar como UNMPORTCOM, após CREATE TABLE
GRANT SELECT, INSERT, UPDATE, DELETE ON UNMPORTCOM.NOVA_TABELA TO UNMPORTCOM_APP_ROLE;
```

Ao adicionar **nova sequence** `SQ_NOVA`:

```sql
GRANT SELECT ON UNMPORTCOM.SQ_NOVA TO UNMPORTCOM_APP_ROLE;
```

Atualizar também:

- `database/ddl/007-create-grants.sql` (greenfield)
- `database/security/V900__application_user_grants.sql` / `V902__application_user_sequences.sql`

---

## Inventário de objetos (baseline homologado 2026-07-22)

### Tabelas (23)

`FEDERACAO`, `SINGULAR`, `ENDERECO`, `CONTATO`, `AREA`, `EQUIPE`, `COLABORADOR`, `ONBOARDING_SOLICITACAO`, `CATEGORIA_DOCUMENTAL`, `PASTA`, `DOCUMENTO`, `ARQUIVO_BINARIO`, `DOCUMENTO_VERSAO`, `COMPARTILHAMENTO`, `AUTH_SESSAO`, `PAPEL`, `PAPEL_ATRIBUICAO`, `PERMISSAO_PASTA`, `SOLICITACAO_PERMISSAO`, `REGISTRO_AUDITORIA`, `COMUNICADO`, `NOTIFICACAO`, `CONFIGURACAO_PORTAL`.

### Sequences (baseline `002-create-sequences.sql` — 12)

`SQ_FEDERACAO_COD_FEDERACAO`, `SQ_SINGULAR_COD_SINGULAR`, `SQ_AREA_COD_AREA`, `SQ_EQUIPE_COD_EQUIPE`, `SQ_COLABORADOR`, `SQ_ONBOARD_SOLIC`, `SQ_DOCUMENTO_COD_DOCUMENTO`, `SQ_REG_AUDIT_COD_REG_AUDIT`, `SQ_AUTH_SESSAO`, `SQ_COMUNICADO_COD_COMUNICADO`, `SQ_NOTIFICACAO_COD_NOTIFICACAO`, `SQ_CONFIG_PORT_COD_CONFIG_PORT`.

### Sequences adicionais (brownfield / JPA)

Alguns ambientes possuem sequences com nomenclatura estendida usada pelo JPA:

- `SQ_AUTH_SESSAO_COD_SESSAO` (`V003`, `AuthSessaoEntity`)
- `SQ_COLABORADOR_COD_COLABORADOR` (relatórios de nomenclatura corporativa; conceder se existir)

`V902` inclui grants para ambos os conjuntos quando o objeto existir.

### Views / materialized views / packages

**Nenhum** objeto `VIEW`, `MATERIALIZED VIEW`, `PACKAGE`, `PROCEDURE` ou `FUNCTION` de domínio está versionado no baseline para consumo direto do backend. `V903` documenta essa ausência.

### Synonyms

**Não utilizados** para o backend. Ver `database/security/V901__application_user_synonyms.sql` e `OPERATIONS.md`.

---

## Exemplo de validação (application user)

```sql
-- Conectar como UNMPORTCOM_APP
SELECT table_name, privilege
  FROM all_tab_privs
 WHERE owner = 'UNMPORTCOM'
   AND grantee IN ('UNMPORTCOM_APP', 'UNMPORTCOM_APP_ROLE')
 ORDER BY table_name, privilege;
```

Resultado esperado: privilégios sobre as 23 tabelas (via role ou usuário).

---

## Referências

- `database/security/README.md`
- `database/security/OPERATIONS.md`
- `database/security/CHECKLIST-DBA.md`
- `database/ddl/001-create-users.sql`
- `database/ddl/007-create-grants.sql`
- `database/model/05-decisions-and-risks.md` (entrada DEC-DB-024)
