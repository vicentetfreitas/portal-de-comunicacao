# Database Standards

## Documento

```text
docs/implementation/06-database-standards.md
```

---

# Objetivo

Definir os padrões oficiais de modelagem, persistência, versionamento e governança do banco de dados do Portal de Comunicação.

Este documento estabelece:

* padrões de modelagem
* convenções de nomenclatura
* ownership de dados
* versionamento de schema (DBA)
* scripts DDL
* auditoria
* índices
* integridade referencial
* performance

---

# Escopo

Aplica-se a:

```text
Oracle Database
Scripts DDL (DBA)
Tabelas
Views
Índices
Constraints
Sequences
Auditoria
```

---

# Princípios Fundamentais

## Ownership

Toda estrutura de dados deve possuir owner definido.

Fonte:

```text
07-data-ownership.md
```

---

## Evolução Controlada

Nenhuma alteração estrutural pode ocorrer diretamente no banco.

Toda alteração deve ser realizada por script DDL versionado, executado pelo DBA.

---

## Banco como Persistência

O banco armazena estado.

Não deve conter:

* regras de negócio
* fluxos operacionais
* decisões funcionais

---

# Tecnologia Oficial

## Banco

```text
Oracle Database
```

Driver: `oracle.jdbc.OracleDriver`

Dependência Maven: `ojdbc11`

---

## Administração do schema (DEC-DB-019)

O schema Oracle é administrado pelo DBA através do baseline DDL oficial do projeto.

A aplicação pressupõe schema previamente criado. Flyway não é utilizado.

## Driver

Gerenciado exclusivamente pelo Backend.

---

# Estratégia de Schema

## Schema Único

O Portal de Comunicação utilizará:

```text
UNMPORTCOM
```

como schema padrão.

Charset: `AL32UTF8`

---

## Schema owner × application user (DEC-DB-024)

| Conceito | Valor | Uso |
|----------|-------|-----|
| Schema owner | `UNMPORTCOM` | Objetos físicos; DDL/migrations (DBA); `hibernate.default_schema` e `@Table(schema = "UNMPORTCOM")` |
| Application user | `UNMPORTCOM_APP` | **Único** `spring.datasource.username` (runtime, testes, jobs) |
| Role | `UNMPORTCOM_APP_ROLE` | Privilégios DML + `SELECT` em sequences |

SSOT de grants: `database/security/`. Atividade de consolidação: `database/reports/infra-db-01-application-user-migration.md`.

---

## Organização Lógica

A separação entre bounded contexts ocorre através de:

* ownership
* convenções
* pacotes
* contratos

e não por schemas distintos.

---

## Baseline

O schema físico é provisionado pelo DBA a partir da DDL oficial:

```text
database/ddl/
```

Baseline oficial:

```text
000-install.sql
001-create-users.sql
002-create-sequences.sql
003-create-tables.sql
004-create-constraints.sql
005-create-indexes.sql
006-create-comments.sql
007-create-grants.sql
008-initial-data.sql
901-validation.sql
```

Evoluções estruturais pós-baseline em:

```text
database/migrations/
```

Executadas exclusivamente pelo DBA — não pela aplicação.

# Convenções de Nomenclatura

## Referência corporativa

Padrão oficial da Unimed Ceará:

```text
Padrão para Nomenclatura de Banco de Dados Oracle (Unimed Ceará)
```

Este documento **internaliza** as regras corporativas para o Portal de Comunicação. Não criar padrão paralelo.

---

## Compatibilidade Oracle 11g

Todo identificador Oracle (`TABELA`, `COLUNA`, `SEQUENCE`, `CONSTRAINT`, `INDEX`, `VIEW`, `TRIGGER`, `PROCEDURE`, `FUNCTION`, `PACKAGE`, `SYNONYM`) deve respeitar o limite de **30 caracteres**.

Quando a nomenclatura padrão exceder esse limite, utilizar exclusivamente o **Glossário Oficial de Abreviações** (seção abaixo). Não utilizar abreviações livres.

Objetos Oracle que já estejam em conformidade com o padrão corporativo **não devem ser renomeados** apenas para utilizar abreviações.

O Glossário Oficial de Abreviações deve ser utilizado **exclusivamente quando necessário** para atender ao limite de 30 caracteres do Oracle 11g.

Truncamento aplicável a:

* `SQ_<TABELA>_<CAMPO>` → abreviar tabela e campo conforme glossário
* `PK_<TABELA>`, `FK_<ORIGEM>_<DESTINO>`, `UK_<TABELA>_<CAMPO>`, `CK_<TABELA>_<REGRA>`
* `IDX_<TABELA>_<SUFIXO>` — sufixo pode ser abreviação de coluna ou sequencial numérico (`01`, `02`) em índices compostos

---

## Glossário Oficial de Abreviações

Abreviações **obrigatórias** e **reutilizáveis** em tabelas, sequences, índices, constraints e triggers:

| Termo completo | Abreviação |
|----------------|------------|
| ONBOARDING | ONBOARD |
| SOLICITACAO | SOLIC |
| COLABORADOR | COLAB |
| DOCUMENTO | DOC |
| CONFIGURACAO | CONFIG |
| AUTENTICACAO | AUTH |
| FEDERACAO | FED |
| COMUNICADO | COMUN |
| CATEGORIA | CAT |
| DOCUMENTAL | DOC |
| ARQUIVO | ARQ |
| BINARIO | BIN |
| VERSAO | VERS |
| COMPARTILHAMENTO | COMPART |
| PERMISSAO | PERM |
| REGISTRO | REG |
| AUDITORIA | AUDIT |
| NOTIFICACAO | NOTIF |
| ATRIBUICAO | ATRIB |
| PORTAL | PORT |
| DESTINATARIO | DEST |
| PUBLICACAO | PUBL |
| PUBLICADO | PUBL |
| ENTIDADE | ENT |
| EVENTO | EVT |
| STATUS | STA |

**Regra:** nunca criar abreviações diferentes para o mesmo termo. **Não abreviar** quando o nome completo `SQ_<TABELA>_<CAMPO_PK>` já estiver em conformidade e ≤ 30 caracteres.

**Exemplo de sequence truncada (somente quando > 30 caracteres):**

```text
SQ_ONBOARDING_SOLICITACAO_COD_ONBOARDING_SOLICITACAO  →  SQ_ONBOARD_SOLIC
```

**Exemplo mantido sem abreviação (conforme e ≤ 30 caracteres):**

```text
SQ_COLABORADOR_COD_COLABORADOR  →  mantido (30 caracteres)
```

---

## Tabelas

* Letras maiúsculas
* Singular
* Substantivos — sem verbos ou preposições
* Máximo 30 caracteres
* Separador de palavras compostas: `_`

Exemplos:

```text
FEDERACAO
COLABORADOR
DOCUMENTO
CATEGORIA_DOCUMENTAL
```

---

## Colunas

Prefixos corporativos obrigatórios:

| Prefixo | Significado        |
| ------- | ------------------ |
| COD_    | Código / identificador |
| NUM_    | Número             |
| DAT_    | Data / timestamp   |
| NOM_    | Nome               |
| DSC_    | Descrição / texto longo |
| VLR_    | Valor monetário    |
| QTD_    | Quantidade         |
| FLG_    | Flag (S/N)         |

Extensões já consolidadas no modelo do portal (manter consistência):

| Prefixo | Significado        |
| ------- | ------------------ |
| DES_    | Descrição / texto curto (ex.: `DES_EMAIL` — identidade FT-AUTH) |
| SIG_    | Sigla              |
| TIP_    | Tipo / enumeração  |
| HASH_   | Hash               |
| STA_    | Status             |
| ID_     | Identificador externo (ex.: `ID_ZIMBRA`) |
| URL_    | URL                |

Exemplos:

```text
DAT_CADASTRO
DAT_ATUALIZACAO
FLG_ATIVO
COD_FEDERACAO
DES_EMAIL
```

---

## Chaves Primárias

Coluna:

```text
COD_<ENTIDADE>
```

Tipo:

```sql
NUMBER(19) PRIMARY KEY
```

Constraint:

```text
PK_<TABELA>
```

Truncar `PK_<TABELA>` se exceder 30 caracteres (usar glossário).

---

## Chaves Estrangeiras

Coluna:

```text
COD_<ENTIDADE_REFERENCIADA>
```

Constraint:

```text
FK_<TABELA_ORIGEM>_<TABELA_DESTINO>
```

Truncar com glossário quando exceder 30 caracteres.

Exemplo:

```text
FK_REGISTRO_AUDITORIA_COLABORADOR  →  FK_REG_AUDIT_COLAB
```

---

## Índices

Formato padrão:

```text
IDX_<TABELA>_<SUFIXO>
```

O sufixo identifica a coluna (abreviada) ou sequência numérica em índices compostos (`01`, `02`).

Exemplos:

```text
IDX_COLABORADOR_EMAIL
IDX_COMPART_DEST
IDX_COMUN_PUBL_01
```

Truncar com glossário quando exceder 30 caracteres.

---

## Constraints

| Prefixo | Significado            |
| ------- | ---------------------- |
| PK_     | Chave Primária         |
| FK_     | Chave Estrangeira      |
| UK_     | Unicidade              |
| CK_     | Restrição CHECK        |

Exemplos:

```text
UK_COLABORADOR_EMAIL
FK_DOC_DOC_VERS
CK_DOCUMENTO_STATUS
UK_CONFIG_PORT_FED
```

---

## Sequences

Formato:

```text
SQ_<TABELA>_<CAMPO_PK>
```

Tipo: `NUMBER(19)`, incremento 1, `CACHE 20` (Padrão Unimed Ceará)

Quando truncado:

```text
SQ_<TABELA_ABREV>_<CAMPO_ABREV>
```

ou, quando o campo é implícito na tabela composta:

```text
SQ_ONBOARD_SOLIC
```

---

## Views

Prefixo obrigatório:

```text
VW_
```

---

## Procedures

Prefixo obrigatório:

```text
PR_
```

Utilizar substantivos — nunca verbos.

---

## Functions

Prefixo obrigatório:

```text
FC_
```

---

## Packages

Prefixo obrigatório:

```text
PKG_
```

---

## Synonyms

Seguir o padrão corporativo Unimed Ceará para o tipo de objeto referenciado.

---

# Identificadores

## Estratégia (DEC-DB-018)

Chaves primárias surrogate `NUMBER(19)` geradas por **Oracle Sequences**, sem `DEFAULT` no DDL.

| Camada | Responsabilidade |
|--------|------------------|
| **JPA/Hibernate** | `@SequenceGenerator` + `GenerationType.SEQUENCE` |
| **Scripts SQL** | `SQ_<TABELA>_<CAMPO>.NEXTVAL` explícito em `INSERT` |

O Oracle Database é a fonte dos identificadores; a aplicação invoca a sequence via JPA.

**Proibido no DDL:** `DEFAULT SQ_*.NEXTVAL` em colunas PK.

---

## Proibido

```text
UUID
IDENTITY
AUTO_INCREMENT
SERIAL
BIGSERIAL
@GeneratedValue(strategy = IDENTITY)
```

---

# Tipos Padronizados

| Uso              | Tipo Oracle    |
| ---------------- | -------------- |
| Chaves Primárias | NUMBER(19)     |
| Datas            | TIMESTAMP(6)   |
| Flags            | CHAR(1)        |
| Textos Curtos    | VARCHAR2(255)  |
| Textos Longos    | CLOB           |

---

# Flags

Tipo: `CHAR(1)`

Valores permitidos:

```text
S (Sim)
N (Não)
```

Default recomendado: `'N'` para flags de estado; `'S'` para flags de ativação.

---

# Ownership de Dados

## Regra Fundamental

Somente o owner grava.

---

## Exemplo

```text
DocumentManagement
→ DOCUMENTO
→ CATEGORIA_DOCUMENTAL
→ COMPARTILHAMENTO
```

---

```text
AccessControl
→ PAPEL
→ PERMISSAO
→ SESSAO
```

---

## Proibido

Um contexto alterar tabelas de outro contexto.

---

# Integridade Referencial

## Obrigatória

Toda relação deve possuir:

```text
Foreign Key
```

quando aplicável.

---

## Exceções

Somente mediante justificativa arquitetural aprovada.

---

# Auditoria

## Campos Obrigatórios

Todas as entidades auditáveis devem possuir:

```sql
DAT_CADASTRO    TIMESTAMP(6)  DEFAULT SYSTIMESTAMP
DAT_ATUALIZACAO TIMESTAMP(6)
```

---

## Exclusão

Preferir:

```text
soft delete via FLG_ATIVO
```

quando aplicável.

---

Campos:

```sql
FLG_ATIVO CHAR(1) DEFAULT 'S'
```

---

DELETE físico permitido apenas em tabelas temporárias, cache e logs técnicos com política de retenção.

---

# Versionamento (DBA)

## Obrigatório

Toda alteração estrutural deve ser refletida em script DDL versionado em `database/ddl/` ou `database/migrations/`, para execução pelo DBA.

---

## Estrutura

```text
database/ddl/          — baseline oficial
database/migrations/   — evoluções pós-baseline
```

---

## Convenção (migrations)

```text
V00X__<descricao>.sql
```

---

# Configuração JPA

```yaml
spring:
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: none
```

A aplicação não executa DDL nem migrações na inicialização (DEC-DB-019).

## Testes de integração (DEC-DB-023)

No perfil `test`, utilizar `ddl-auto: validate` contra o Oracle provisionado pelo DBA (`SPRING_DATASOURCE_*`). Detalhes: `docs/implementation/13-integration-test-database-strategy.md`.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

Proibido em testes de integração: `create`, `update`, `create-drop`, H2 como substituto do Oracle oficial.

# Proibições

Nunca:

```text
alterar tabela manualmente fora do processo DBA
alterar produção sem script DDL versionado
a aplicação criar ou alterar schema (ddl-auto diferente de none)
```

---

# Índices

## Regra

Criar índices apenas para:

* chaves estrangeiras
* filtros frequentes
* consultas críticas

---

## Evitar

Indexação excessiva.

---

# Consultas

## Backend

Responsável por toda consulta.

---

## Proibido

Frontend acessar banco.

---

# Views

## Permitidas

Somente para:

```text
consulta
relatórios
projeções
```

---

## Proibido

Implementar regra de negócio em views.

---

# Transações

## Responsabilidade

Application Layer.

---

## Regra

Cada transação deve representar:

```text
um caso de uso
```

---

# Armazenamento de Arquivos

Conforme ADR-004.

---

## Banco

Armazena:

```text
metadados
```

---

## Storage

Armazena:

```text
binários
```

---

## Proibido

```text
BLOB para documentos
armazenamento de binários no banco
```

Metadados de arquivo utilizam referências (`COD_ARQUIVO`), não o conteúdo binário.

---

# Performance

## Objetivos

Garantir:

* consultas previsíveis
* crescimento controlado
* manutenção simplificada

---

## Monitorar

* tempo de consulta
* índices não utilizados
* crescimento de tabelas

---

# Dados Sensíveis

## Obrigatório

Criptografia quando aplicável.

---

## Nunca Armazenar

```text
senhas em texto puro
tokens sem proteção
segredos de integração
```

---

# Testes

## Obrigatórios

Validação de:

* scripts DDL (baseline e evoluções)
* constraints
* índices
* integridade referencial

---

# Critérios de Conformidade

Toda tabela deve responder:

## Possui owner?

```text
SIM
```

---

## Possui script DDL versionado?

```text
SIM
```

---

## Possui auditoria?

```text
SIM
```

---

## Possui chave primária NUMBER(19) com Sequence?

```text
SIM
```

---

## Respeita ownership?

```text
SIM
```

---

# Não Conformidades

São considerados desvios arquiteturais:

* alteração manual de schema fora do processo DBA
* ausência de script DDL versionado
* ausência de ownership
* uso de UUID ou IDENTITY
* armazenamento de binários no banco
* tabelas compartilhadas entre contextos
* regras de negócio em views

---

# Conclusão

O banco de dados do Portal de Comunicação deve atuar como mecanismo de persistência governado por ownership explícito, scripts DDL versionados (DBA) e integridade controlada no Oracle Database.

Toda evolução estrutural deve ser rastreável aos bounded contexts definidos na arquitetura e ao ownership documentado na camada Solution Design.
