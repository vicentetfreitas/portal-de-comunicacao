# Database

## Objetivo

Esta camada concentra os artefatos do banco Oracle do **Portal de Comunicação**: modelagem e DDL baseline. A DDL é a **única fonte oficial** da estrutura física.

O schema Oracle é administrado pelo **DBA** através do baseline DDL oficial do projeto. A aplicação pressupõe um schema previamente criado.

---

## Organização

```text
docs/database/
├── README.md
├── model/
├── ddl/
├── dml/
├── migrations/
└── rollback/
```

---

## Responsabilidade das pastas

| Pasta | Responsabilidade |
|-------|------------------|
| `model/` | Especificação do schema — conceitual, lógico, físico, catálogo e decisões |
| `ddl/` | Baseline oficial da estrutura física (fonte única da verdade) |
| `dml/` | Cargas iniciais e dados de referência (`ddl/008-initial-data.sql`) |
| `migrations/` | Scripts de evolução estrutural pós-baseline (referência para o DBA) |
| `rollback/` | Scripts de reversão de migrações (evolução futura) |

Grants e permissões estão centralizados em `ddl/007-create-grants.sql`.

---

## Fluxo da arquitetura

```text
Model
  ↓
DDL baseline (DBA)
  ↓
Aplicação (schema pré-existente)
```

- **DDL** define e materializa a estrutura do schema `UNMPORTCOM`.
- **DBA** executa criação e evolução estrutural; a aplicação não gerencia o schema.

Detalhamento do modelo de dados: `model/`.

### Artefatos do modelo (`model/`)

| Documento | Camada | Versão |
|-----------|--------|--------|
| `01-schema.md` | Escopo do schema | 3.2 |
| `02-conceptual-model.md` | Modelo conceitual | 1.0 |
| `02-logical-model.md` | Modelo lógico | 1.0 |
| `03-physical-model.md` | Modelo físico | 4.6 |
| `04-entity-catalog.md` | Catálogo de entidades | — |
| `05-decisions-and-risks.md` | Decisões e riscos | 4.1 |

---

## Estrutura DDL oficial

```text
ddl/
├── 000-install.sql
├── 001-create-users.sql
├── 002-create-sequences.sql
├── 003-create-tables.sql
├── 004-create-constraints.sql
├── 005-create-indexes.sql
├── 006-create-comments.sql
├── 007-create-grants.sql
├── 008-initial-data.sql
├── 900-drop-all.sql
├── 901-validation.sql
└── 902-compile-invalid-objects.sql
```

---

## Ordem de execução da DDL

```text
1. 001-create-users.sql        (como SYS/DBA)
2. 000-install.sql             (como UNMPORTCOM; orquestra 002–008)
   — ou executar 002–008 individualmente
3. 901-validation.sql          (validação pós-instalação)
```

Scripts auxiliares: `900-drop-all.sql`, `902-compile-invalid-objects.sql`.

---

## Administração do schema (DEC-DB-019)

| Papel | Responsabilidade |
|-------|------------------|
| DDL (`ddl/`) | Fonte oficial da estrutura física — baseline completo |
| DBA | Execução de `000-install.sql` e evoluções em `migrations/` |

Alterações estruturais devem ser refletidas na DDL baseline ou em scripts versionados em `migrations/` para execução pelo DBA — nunca pela aplicação.

### Consolidação Sprint 1 (2026-07-10)

- Baseline DDL atualizado com `AUTH_SESSAO` e coluna `ID_ZIMBRA` em `COLABORADOR` (FT-AUTH).
- Modelo físico v4.7 — refinamento final de `COLABORADOR` (DEC-DB-016); nomenclatura corporativa Oracle (DEC-DB-017).
- Relatório de conformidade: `oracle-naming-compliance-report.md`.
- Modelos conceitual e lógico criados (`02-conceptual-model.md`, `02-logical-model.md`).
- DEC-DB-019 — schema administrado pelo DBA; Flyway não utilizado.
- Documentação do modelo sincronizada com baseline DDL — ver `data-model-documentation-consolidation-report.md`.

---

## Convenções principais

| Item | Valor |
|------|-------|
| Banco | Oracle Database 11g+ |
| Schema | UNMPORTCOM |
| Charset | AL32UTF8 |
| Padrão corporativo | Padrão para Nomenclatura de Banco de Dados Oracle (Unimed Ceará) |
| Limite identificadores | 30 caracteres (Oracle 11g) |
| Abreviações | Glossário em `docs/implementation/06-database-standards.md` |
| Sequence | `SQ_<TABELA>_<CAMPO_PK>` (truncar com glossário se necessário) |
| PK surrogate | `NUMBER(19)` |

Prefixos de objetos, tipos de colunas, auditoria e controle de vigência: `model/03-physical-model.md` e `docs/implementation/06-database-standards.md`.
