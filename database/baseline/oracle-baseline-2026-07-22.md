# Oracle Physical Baseline — Portal de Comunicação

**Schema:** `UNMPORTCOM`  
**Status:** BASELINE HOMOLOGADA  
**Data da homologação:** 2026-07-22  
**Atividades:** DB-BL-02 · DB-BL-03 · **DB-SYNC-99** (sincronização Oracle × DDL encerrada)

---

# Introdução

Este documento é a **Single Source of Truth (SSOT)** do **estado homologado** do schema Oracle `UNMPORTCOM` na data indicada.

O banco foi validado por inspeção estrutural no ambiente homologado (inventário final **DB-SYNC-99**). Agentes sem acesso ao Oracle devem utilizar **este arquivo** como referência oficial do snapshot homologado.

O Oracle homologado está **sincronizado** com a implementação versionada em `database/ddl/` (greenfield). Evidência complementar: `database/validation/oracle-schema-validation-2026-07-22.md` e `database/ddl/901-validation.sql`.

---

# Objetivo

Representar de forma clara e auditável o **estado homologado** do schema Oracle, conforme validação estrutural de 2026-07-22 e encerramento **DB-SYNC-99**.

---

# Escopo

Contempla, como intenção documental:

- Estrutura física
- Tabelas
- Colunas
- Chaves primárias
- Chaves estrangeiras
- Constraints (UNIQUE, CHECK)
- Índices
- Sequences
- Comentários
- Relacionamentos
- Objetos auxiliares (triggers, views, synonyms — inexistentes na homologação)

**Não contempla dados (DML).**

---

# Estatísticas

Contagens agregadas da homologação estrutural (inspeção Oracle):

| Item | Quantidade |
|------|-----------:|
| Tabelas | 23 |
| PK | 23 |
| FK | 36 |
| UNIQUE | 11 |
| CHECK | 172 |
| Índices | 95 |
| Sequences | 12 |
| Triggers | 0 |
| Views | 0 |
| Synonyms | 0 |
| Objetos inválidos | 0 |
| Objetos `BIN$` | 0 |

> Contagens validadas na homologação final (**DB-SYNC-99**). Evidência: `database/validation/oracle-schema-validation-2026-07-22.md`.

---

# Inventário de Tabelas

Lista nominal homologada das **23** tabelas do schema:

| # | Tabela |
|---|--------|
| 1 | FEDERACAO |
| 2 | SINGULAR |
| 3 | ENDERECO |
| 4 | CONTATO |
| 5 | AREA |
| 6 | EQUIPE |
| 7 | COLABORADOR |
| 8 | ONBOARDING_SOLICITACAO |
| 9 | CATEGORIA_DOCUMENTAL |
| 10 | PASTA |
| 11 | DOCUMENTO |
| 12 | ARQUIVO_BINARIO |
| 13 | DOCUMENTO_VERSAO |
| 14 | COMPARTILHAMENTO |
| 15 | AUTH_SESSAO |
| 16 | PAPEL |
| 17 | PAPEL_ATRIBUICAO |
| 18 | PERMISSAO_PASTA |
| 19 | SOLICITACAO_PERMISSAO |
| 20 | REGISTRO_AUDITORIA |
| 21 | COMUNICADO |
| 22 | NOTIFICACAO |
| 23 | CONFIGURACAO_PORTAL |

**Nota de localização (não normativa):** o script `database/ddl/003-create-tables.sql` contém a implementação DDL das tabelas; a lista acima é a evidência nominal **nesta baseline**.

---

# Inventário de Sequences

Lista nominal homologada das **12** sequences:

| Sequence | Utilização (tabela / contexto) |
|----------|--------------------------------|
| SQ_AREA_COD_AREA | AREA |
| SQ_AUTH_SESSAO | AUTH_SESSAO |
| SQ_COLABORADOR | COLABORADOR |
| SQ_COMUNICADO_COD_COMUNICADO | COMUNICADO |
| SQ_CONFIG_PORT_COD_CONFIG_PORT | CONFIGURACAO_PORTAL |
| SQ_DOCUMENTO_COD_DOCUMENTO | DOCUMENTO |
| SQ_EQUIPE_COD_EQUIPE | EQUIPE |
| SQ_FEDERACAO_COD_FEDERACAO | FEDERACAO |
| SQ_NOTIFICACAO_COD_NOTIFICACAO | NOTIFICACAO |
| SQ_ONBOARD_SOLIC | ONBOARDING_SOLICITACAO |
| SQ_REG_AUDIT_COD_REG_AUDIT | REGISTRO_AUDITORIA |
| SQ_SINGULAR_COD_SINGULAR | SINGULAR |

A ausência de sequence para determinada tabela **não** deve ser interpretada como inconsistência estrutural, salvo evidência em contrário na homologação.

**Nota de localização (não normativa):** `database/ddl/002-create-sequences.sql`.

---

# Estrutura conhecida

Detalhamento nominal **além das estatísticas** existente nesta baseline, por tabela.

## AREA

### Primary Key (nome)

- `PK_AREA`

### Colunas (nomes)

- COD_AREA
- COD_SINGULAR
- NOM_AREA
- SIG_AREA
- DSC_AREA
- COD_GESTOR
- FLG_ATIVO
- DAT_CADASTRO
- DAT_ATUALIZACAO

### Foreign Keys (coluna → tabela destino)

- COD_SINGULAR → SINGULAR
- COD_GESTOR → COLABORADOR

### Índices (nomes)

- PK_AREA
- IDX_AREA_SINGULAR
- IDX_AREA_GESTOR

### Comentário de tabela

Áreas organizacionais da Federação e das Singulares.

---

## Demais tabelas (22)

Não há nesta baseline inventário nominal de colunas, PK, FK, UNIQUE, CHECK, índices ou comentários por tabela, exceto o que consta em § Estatísticas e § Relacionamentos (parcial).

### ARQUIVO_BINARIO

Única menção adicional na versão anterior da baseline: remissão à localização da implementação DDL (`database/ddl/003-create-tables.sql` e scripts `004`–`006`). **Nenhum detalhe colunar ou de constraints está registrado neste documento para ARQUIVO_BINARIO.**

---

# Relacionamentos

Evidência nominal **parcial** (não exaustiva das **36** FK homologadas):

| Tabela origem | Coluna | Tabela destino |
|---------------|--------|----------------|
| AREA | COD_SINGULAR | SINGULAR |
| AREA | COD_GESTOR | COLABORADOR |

Inventário completo de Foreign Keys: `database/ddl/004-create-constraints.sql` (implementação versionada alinhada ao Oracle homologado).

---

# Sincronização Oracle × DDL

A atividade **DB-SYNC-99** encerrou a convergência entre o Oracle homologado e os DDLs oficiais.

| Marco | Situação |
|-------|----------|
| `PK_FEDERACAO` / `PK_CATEGORIA_DOCUMENTAL` | Criadas no Oracle; **23** PK homologadas |
| FKs dependentes das PKs acima | Materializadas; total **36** FK homologadas |
| Exceções documentais anteriores (ausência de PK) | **Encerradas** — não vigentes no snapshot atual |

Relatório de encerramento: `database/reports/sync-report-2026-07-22.md`.

---

# Convenções

## Chaves primárias

Padrão de nomenclatura: `PK_<TABELA>`.

## Chaves estrangeiras

Padrão de nomenclatura: `FK_<ORIGEM>_<DESTINO>`.

## Triggers

Não existem na homologação. Identificadores via aplicação / sequences.

## Views

Não existem.

## Synonyms

Não existem.

## Objetos Oracle `BIN$` (evidência histórica)

Durante a inspeção estrutural inicial foram encontrados nomes internos Oracle iniciados por `BIN$` em alguns relatórios de constraints e chaves primárias.

Validação complementar na homologação **DB-BL-03**:

- **não existem objetos `BIN$`** no schema no snapshot homologado atual;
- Recycle Bin vazia;
- ausência de objetos inválidos;
- inexistência de impactos estruturais declarados.

**Política para evoluções futuras:**

- objetos `BIN$` são artefatos da Oracle Recycle Bin;
- **não** fazem parte do modelo oficial;
- **devem ser ignorados** em comparações e em DDL versionado.

---

# Lacunas documentais

## Matriz de cobertura

| Categoria | Cobertura | Fonte nesta baseline |
|-----------|-----------|----------------------|
| Tabelas | **COMPLETO** | § Inventário de Tabelas (23 nomes) |
| Colunas | **PARCIAL** | § Estrutura conhecida — somente **AREA** (9 colunas) |
| Primary Keys | **PARCIAL** | Estatística **23** PK homologadas; `PK_AREA` nominal; demais PK em `ddl/004` |
| Foreign Keys | **PARCIAL** | 2 relações de **AREA** + estatística **36** FK; inventário nominal em `ddl/004` |
| UNIQUE | **PARCIAL** | Estatística **11** homologada; lista nominal em `ddl/004` |
| CHECK | **PARCIAL** | Estatística **172** homologada; política `SYS_C*` / `NOT NULL` conforme Oracle |
| Índices | **PARCIAL** | Nomes em **AREA** (3) + estatística **95**; inventário global em `ddl/005` |
| Sequences | **COMPLETO** | § Inventário de Sequences (12 nomes) |
| Comentários | **PARCIAL** | Comentário de tabela **AREA** apenas; sem comentários de coluna |
| Relacionamentos | **PARCIAL** | Mesmas 2 linhas de FK de **AREA**; **36** FK não enumeradas neste arquivo |

## Detalhamento nominal

Para comparação objeto a objeto, utilizar os DDLs versionados (`003`–`006`) como espelho da homologação **DB-SYNC-99**, subordinados a este documento em caso de divergência de **contagens** ou política de governança.

Informações **não consolidadas integralmente** neste markdown (opcional em evoluções futuras da baseline):

- Colunas (nome, tipo, nulabilidade, default) para as **22** tabelas sem detalhe além de AREA
- Comentários de coluna para todas as tabelas
- Matriz completa FK → PK em texto (além de `ddl/004`)

---

# Governança pós-homologação

A sincronização Oracle × DDL (**DB-SYNC-99**) está **concluída**. Novas alterações estruturais seguem `database/GOVERNANCE.md` (baseline + validation, ou `migrations/` brownfield).

---

# Referências

| Artefato | Papel |
|----------|--------|
| `database/validation/oracle-schema-validation-2026-07-22.md` | Evidência homologada (DB-SYNC-99) |
| `database/GOVERNANCE.md` | Precedência e políticas da camada `database/` |
| `database/ddl/000-install.sql` … `901-validation.sql` | Implementação versionada (greenfield) |
| `database/reports/sync-report-2026-07-22.md` | Relatório final de sincronização Oracle × DDL |
| `database/reports/db-bl-02-report.md` | Normalização documental DB-BL-02 |

---

**Fim da baseline homologada 2026-07-22 (DB-BL-02 · DB-SYNC-99).**
