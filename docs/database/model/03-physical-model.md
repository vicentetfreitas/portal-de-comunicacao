# Modelo Físico

| Item           | Valor                                |
| -------------- | ------------------------------------ |
| Projeto        | Portal de Comunicação                |
| Sistema        | Portal de Comunicação — Unimed Ceará |
| Banco de Dados | Oracle Database                      |
| Schema         | UNMPORTCOM                           |
| Versão         | 4.7                                  |
| Status         | Approved                             |
| Baseline DDL   | `docs/database/ddl/` (sincronizado)  |

---

## 1 Objetivo

Definir a implementação física do modelo de dados do Portal de Comunicação no schema UNMPORTCOM.

Este documento estabelece tabelas, chaves, Sequences Oracle, índices, Restrições e estratégias de implementação Oracle.

**Hierarquia:** `02-conceptual-model.md` → `02-logical-model.md` → **este documento** → `ddl/`.

---

## 2 Escopo

Abrange todas as entidades físicas do Portal de Comunicação:

- Organização Corporativa
- Gestão Documental
- Controle de Acesso
- Comunicação Interna
- Configuração

Não abrange DDL executável, scripts de evolução DBA ou objetos de outros schemas corporativos.

---

## 3 Padrões Oracle

O banco de dados é considerado a fonte oficial para geração das chaves primárias, garantindo independência da camada de persistência e compatibilidade com scripts DDL executados manualmente.

### Banco

- Oracle Database

### Charset

- AL32UTF8

### Schema

- UNMPORTCOM

### Versionamento

- DDL como fonte oficial do baseline em `docs/database/ddl/`
- Evoluções estruturais pós-baseline em `docs/database/migrations/`, executadas pelo DBA (DEC-DB-019)

### Convenções de Naming

Referência corporativa: **Padrão para Nomenclatura de Banco de Dados Oracle (Unimed Ceará)** — detalhamento em `docs/implementation/06-database-standards.md`.

**Compatibilidade Oracle 11g:** todo identificador ≤ 30 caracteres. Truncamento via Glossário Oficial de Abreviações (mesmo documento).

| Prefixo | Significado              |
| ------- | ------------------------ |
| PK_     | Chave Primária           |
| FK_     | Chave Estrangeira        |
| UK_     | Restrição de Unicidade   |
| CK_     | Restrição CHECK          |
| IDX_    | Índice (`IDX_<TABELA>_<SUFIXO>`) |
| SQ_     | Sequence (`SQ_<TABELA>_<CAMPO_PK>`) |
| VW_     | View                     |
| PR_     | Procedure                |
| FC_     | Function                 |
| PKG_    | Package                  |

### Tipos Padronizados

| Uso             | Tipo Oracle   |
| --------------- | ------------- |
| Chaves Primárias | NUMBER(19)   |
| Datas           | TIMESTAMP(6)  |
| Flags           | CHAR(1)       |
| Textos Curtos   | VARCHAR2(255) |
| Textos Longos   | CLOB          |

### Convenções Temporais

- Todas as colunas temporais utilizam `TIMESTAMP(6)`
- Compatível com Oracle, Hibernate/JPA e auditoria com precisão de microssegundos

### Convenções para Flags

- Tipo: `CHAR(1)`
- Valores permitidos: `S` (Sim), `N` (Não)
- Default recomendado: `'N'` para flags de estado; `'S'` para flags de ativação

### Auditoria

As convenções gerais de auditoria são definidas na seção
"6. Estratégias Arquiteturais".

Todas as entidades auditáveis deverão seguir aquele padrão.

### Status e temporalidade

Padrão adotado na DDL para controle de vigência e rastreio temporal:

| Coluna          | Tipo          | Default      |
| --------------- | ------------- | ------------ |
| FLG_ATIVO       | CHAR(1)       | 'S'          |
| DAT_CADASTRO    | TIMESTAMP(6)  | SYSTIMESTAMP |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | NULL         |

- `S` = registro ativo; `N` = registro inativo
- `DAT_CADASTRO` e `DAT_ATUALIZACAO` registram criação e última alteração
- Nem todas as entidades possuem `FLG_ATIVO` (conforme DDL)
- DELETE físico permitido apenas em tabelas temporárias, cache e logs técnicos com política de retenção

### Estratégia de Sequences Oracle (DEC-DB-018)

- Chaves primárias surrogate `NUMBER(19)`, uma Sequence dedicada por entidade
- Nomenclatura: `SQ_<TABELA>_<CAMPO_PK>` — truncar com glossário quando > 30 caracteres (DEC-DB-017)
- Tipo: `NUMBER(19)`, incremento 1, `CACHE 20`
- **Sem `DEFAULT SQ_*.NEXTVAL` nas colunas PK do DDL** — o DDL não gera IDs automaticamente
- **Geração via aplicação:** Hibernate/JPA com `@SequenceGenerator` + `GenerationType.SEQUENCE`
- **Geração via script SQL:** `SQ_<TABELA>_<CAMPO>.NEXTVAL` explícito em `INSERT` (ex.: `008-initial-data.sql`)
- Proibido: `IDENTITY`, `AUTO_INCREMENT`, UUID, `@GeneratedValue(strategy = IDENTITY)`

### Estratégia de Restrições

**Chaves Primárias**

- Toda entidade possui Chave Primária surrogate `NUMBER(19)` com prefixo `PK_`

**Chaves Estrangeiras**

- Nomenclatura `FK_<TABELA_ORIGEM>_<TABELA_DESTINO>` ou `FK_<TABELA>_<REFERENCIA>`
- Integridade referencial garantida no banco quando aplicável
- Chaves Estrangeiras não possuem valor Default

**Restrições de Unicidade**

| Restrição                      | Colunas                                                              |
| ------------------------------- | -------------------------------------------------------------------- |
| UK_FEDERACAO_SIGLA              | SIG_FEDERACAO                                                        |
| UK_FEDERACAO_COD_UNIMED         | COD_UNIMED                                                           |
| UK_SINGULAR_SIGLA               | SIG_SINGULAR                                                         |
| UK_SINGULAR_COD_UNIMED          | COD_UNIMED                                                           |
| UK_COLABORADOR_EMAIL            | DES_EMAIL                                                            |
| UK_COLABORADOR_CPF              | NUM_CPF                                                              |
| UK_COLABORADOR_ZIMBRA           | ID_ZIMBRA                                                            |
| UK_AUTH_SESSAO_ID               | ID_SESSAO                                                            |
| UK_AUTH_SESSAO_HASH             | HASH_REFRESH_TOKEN                                                   |
| UK_CATEGORIA_DOCUMENTAL_NOME    | NOM_CATEGORIA                                                        |
| UK_ARQUIVO_HASH                 | HASH_ARQUIVO                                                         |
| UK_DOCUMENTO_VERSAO             | COD_DOCUMENTO, NUM_VERSAO                                            |
| UK_COMPARTILHAMENTO             | TIP_ORIGEM, COD_ORIGEM, TIP_DESTINATARIO, COD_DESTINATARIO, TIP_ACESSO |
| UK_PAPEL_NOME                   | NOM_PAPEL                                                            |
| UK_CONFIG_PORT_FED | COD_FEDERACAO                                                       |

**Restrições CHECK**

| Restrição                      | Definição                                                                                              |
| ------------------------------- | ------------------------------------------------------------------------------------------------------ |
| CK_FEDERACAO_ATIVO              | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_SINGULAR_ATIVO               | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_ENDERECO_PROPRIETARIO        | CHECK (exatamente um de COD_FEDERACAO ou COD_SINGULAR preenchido)                                        |
| CK_ENDERECO_PRINCIPAL           | CHECK (FLG_PRINCIPAL IN ('S','N'))                                                                       |
| CK_CONTATO_PROPRIETARIO         | CHECK (exatamente um de COD_FEDERACAO, COD_SINGULAR, COD_AREA, COD_EQUIPE ou COD_COLABORADOR preenchido) |
| CK_CONTATO_PRINCIPAL            | CHECK (FLG_PRINCIPAL IN ('S','N'))                                                                       |
| CK_AREA_ATIVO                   | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_EQUIPE_ATIVO                 | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_COLABORADOR_ATIVO            | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_AUTH_SESSAO_FLG_REMEMBER     | CHECK (FLG_REMEMBER_ME IN ('S','N'))                                                                   |
| CK_AUTH_SESSAO_FLG_REVOGADA     | CHECK (FLG_REVOGADA IN ('S','N'))                                                                      |
| CK_PAPEL_ATIVO                  | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_PAPEL_ATRIBUICAO_ATIVO       | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_CATEGORIA_DOCUMENTAL_ATIVO   | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_PASTA_HERDA_PERMISSAO        | CHECK (FLG_HERDA_PERMISSAO IN ('S','N'))                                                               |
| CK_PASTA_ATIVO                  | CHECK (FLG_ATIVO IN ('S','N'))                                                                         |
| CK_DOCUMENTO_STATUS             | CHECK (STA_DOCUMENTO IN ('ATIVO','ARQUIVADO','EXPIRADO'))                                              |
| CK_DOCUMENTO_VERSAO_ATUAL       | CHECK (FLG_VERSAO_ATUAL IN ('S','N'))                                                                  |
| CK_ONBOARDING_STATUS            | CHECK (STA_SOLICITACAO IN ('PENDENTE','APROVADA','REJEITADA'))                                         |
| CK_SOLICITACAO_STATUS           | CHECK (STA_SOLICITACAO IN ('PENDENTE','APROVADA','REJEITADA'))                                         |
| CK_COMPARTILHAMENTO_ORIGEM      | CHECK (TIP_ORIGEM IN ('DOCUMENTO','PASTA','COMUNICADO'))                                               |
| CK_COMPARTILHAMENTO_DESTINO     | CHECK (TIP_DESTINATARIO IN ('FEDERACAO','SINGULAR','AREA','EQUIPE','COLABORADOR'))                    |
| CK_COMPARTILHAMENTO_ACESSO      | CHECK (TIP_ACESSO IN ('LEITURA','DOWNLOAD','EDICAO','ADMINISTRACAO'))                                  |
| CK_PERMISSAO_PASTA_DESTINO      | CHECK (TIP_DESTINATARIO IN ('FEDERACAO','SINGULAR','AREA','EQUIPE','COLABORADOR'))                    |
| CK_PERMISSAO_PASTA_ACESSO       | CHECK (TIP_ACESSO IN ('LEITURA','DOWNLOAD','EDICAO','ADMINISTRACAO'))                                  |
| CK_COMUNICADO_PUBLICADO         | CHECK (FLG_PUBLICADO IN ('S','N'))                                                                     |
| CK_COMUNICADO_DESTAQUE          | CHECK (FLG_DESTAQUE IN ('S','N'))                                                                      |
| CK_NOTIFICACAO_LIDA             | CHECK (FLG_LIDA IN ('S','N'))                                                                          |
| CK_NOTIFICACAO_TIPO             | CHECK (TIP_NOTIFICACAO IN ('SISTEMA','DOCUMENTO','COMUNICADO','PERMISSAO','ONBOARDING'))               |
| CK_CONFIG_ONBOARDING            | CHECK (FLG_ONBOARDING_ATIVO IN ('S','N'))                                                              |
| CK_CONFIG_NOTIFICACAO_EMAIL     | CHECK (FLG_NOTIFICACAO_EMAIL IN ('S','N'))                                                             |
| CK_CONFIG_COMUNICADO_DESTAQUE   | CHECK (FLG_COMUNICADO_DESTAQUE IN ('S','N'))                                                           |

### Estratégia de Índices

- Índice em todas as Chaves Estrangeiras
- Índices de pesquisa em colunas de busca frequente (email, status, datas)
- Índices em colunas de filtro (flags, tipos)
- Índices compostos quando consultas frequentes envolverem múltiplas colunas de filtragem ou ordenação
- Nomenclatura: `IDX_<TABELA>_<SUFIXO>` — sufixo = coluna abreviada ou sequencial numérico; truncar com glossário quando > 30 caracteres (DEC-DB-017)
- Definição detalhada por entidade na seção 5
- Os índices descritos neste documento representam a estratégia inicial de modelagem
- A definição definitiva dos índices deverá considerar o perfil real de consultas, planos de execução (EXPLAIN PLAN), estatísticas do Oracle e requisitos de desempenho identificados durante a implementação

### Estratégia de Versionamento

Relacionamento documental:

```text
DOCUMENTO 1:N DOCUMENTO_VERSAO
DOCUMENTO_VERSAO N:1 ARQUIVO_BINARIO
```

Regras:

- Documento nunca é sobrescrito
- Toda alteração gera nova versão
- Histórico imutável com rastreabilidade completa
- Apenas uma versão atual (`FLG_VERSAO_ATUAL = 'S'`) por documento
- Unicidade de número de versão por documento via `UK_DOCUMENTO_VERSAO`
- A regra de versão atual única poderá exigir validação pela camada de domínio e/ou mecanismos adicionais do banco

---

## 4 Organização do Modelo

### Organização Corporativa

Estrutura organizacional da Federação e Singulares.

| Entidade               | Função                                      |
| ---------------------- | ------------------------------------------- |
| FEDERACAO              | Federação Unimed administradora do portal   |
| SINGULAR               | Singulares vinculadas à Federação           |
| ENDERECO               | Endereços da Federação ou Singular          |
| CONTATO                | Canais de comunicação (Federação, Singular, Área, Equipe ou Colaborador) |
| AREA                   | Áreas organizacionais hierárquicas          |
| EQUIPE                 | Equipes vinculadas às áreas                 |
| COLABORADOR            | Usuários internos autenticados              |
| ONBOARDING_SOLICITACAO | Solicitações de cadastro e ativação         |

### Gestão Documental

Acervo documental corporativo com versionamento e compartilhamento.

| Entidade             | Função                                      |
| -------------------- | ------------------------------------------- |
| CATEGORIA_DOCUMENTAL | Classificação de documentos                 |
| PASTA                | Estrutura hierárquica de armazenamento      |
| DOCUMENTO            | Metadados do documento corporativo          |
| ARQUIVO_BINARIO      | Metadados de arquivos em armazenamento externo |
| DOCUMENTO_VERSAO     | Versionamento de documentos publicados      |
| COMPARTILHAMENTO     | Compartilhamento genérico de recursos       |

### Controle de Acesso

Autorização, permissões e auditoria operacional.

| Entidade              | Função                                      |
| --------------------- | ------------------------------------------- |
| AUTH_SESSAO           | Sessões de autenticação (Refresh Token)     |
| PAPEL                 | Perfis de acesso do portal                  |
| PAPEL_ATRIBUICAO      | Atribuição de papéis com escopo organizacional |
| PERMISSAO_PASTA       | Permissões explícitas em pastas             |
| SOLICITACAO_PERMISSAO | Solicitações de acesso a pastas/documentos  |
| REGISTRO_AUDITORIA    | Histórico de operações auditáveis            |

### Comunicação

Comunicação interna e notificações.

| Entidade    | Função                                      |
| ----------- | ------------------------------------------- |
| COMUNICADO  | Comunicados institucionais                  |
| NOTIFICACAO | Notificações geradas por eventos do portal  |

### Configuração

Parâmetros globais por Federação.

| Entidade            | Função                                      |
| ------------------- | ------------------------------------------- |
| CONFIGURACAO_PORTAL | Configurações globais do portal             |

---

## 5 Modelo Físico

### FEDERACAO

#### Objetivo

Representar a Federação Unimed administradora do Portal de Comunicação.

#### Responsabilidade

Manter dados cadastrais da Federação e servir como raiz organizacional do modelo.

#### Relacionamentos

- FEDERACAO (1) ── (N) ENDERECO via `COD_FEDERACAO`
- FEDERACAO (1) ── (N) CONTATO via `COD_FEDERACAO`

#### Sequence Oracle

- SQ_FEDERACAO_COD_FEDERACAO

#### Estrutura Física

| Coluna          | Tipo          | Obrigatório | Default      | Restrição |
| --------------- | ------------- | ----------- | ------------ | ---------- |
| COD_FEDERACAO   | NUMBER(19)    | Sim         | -                                        | PK         |
| NOM_FEDERACAO   | VARCHAR2(200) | Sim         | -            | -          |
| SIG_FEDERACAO   | VARCHAR2(30)  | Sim         | -            | UK         |
| COD_UNIMED      | VARCHAR2(20)  | Sim         | -            | UK         |
| NUM_REGISTRO_ANS | VARCHAR2(20) | Sim         | -            | -          |
| URL_SITE        | VARCHAR2(300) | Não         | NULL         | -          |
| DSC_FEDERACAO   | CLOB          | Não         | NULL         | -          |
| FLG_ATIVO       | CHAR(1)       | Sim         | 'S'          | CK         |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_FEDERACAO
- UK_FEDERACAO_SIGLA
- UK_FEDERACAO_COD_UNIMED
- CK_FEDERACAO_ATIVO

#### Índices

- IDX_FEDERACAO_SIGLA
- IDX_FEDERACAO_COD_UNIMED

#### Observações

- Contém apenas identidade institucional estável da federação — **não** é tabela de configuração operacional do portal
- Branding (logo, favicon) permanece em `CONFIGURACAO_PORTAL` (`URL_LOGO`, `URL_FAVICON`)
- Endereços e contatos institucionais em `ENDERECO` e `CONTATO` (DEC-DB-013) — não colunas em `FEDERACAO`

---

### SINGULAR

#### Objetivo

Representar Singulares vinculadas à Federação.

#### Responsabilidade

Manter cadastro das cooperativas singulares e sua associação com a Federação.

#### Relacionamentos

- FK_SINGULAR_FEDERACAO → FEDERACAO (COD_FEDERACAO)
- SINGULAR (1) ── (N) ENDERECO via `COD_SINGULAR`
- SINGULAR (1) ── (N) CONTATO via `COD_SINGULAR`

#### Sequence Oracle

- SQ_SINGULAR_COD_SINGULAR

#### Estrutura Física

| Coluna          | Tipo          | Obrigatório | Default      | Restrição |
| --------------- | ------------- | ----------- | ------------ | ---------- |
| COD_SINGULAR    | NUMBER(19)    | Sim         | -                                        | PK         |
| COD_FEDERACAO   | NUMBER(19)    | Sim         | -            | FK         |
| NOM_SINGULAR    | VARCHAR2(200) | Sim         | -            | -          |
| SIG_SINGULAR    | VARCHAR2(30)  | Sim         | -            | UK         |
| COD_UNIMED      | VARCHAR2(20)  | Sim         | -            | UK         |
| FLG_ATIVO       | CHAR(1)       | Sim         | 'S'          | CK         |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_SINGULAR
- FK_SINGULAR_FEDERACAO
- UK_SINGULAR_SIGLA
- UK_SINGULAR_COD_UNIMED
- CK_SINGULAR_ATIVO

#### Índices

- IDX_SINGULAR_FEDERACAO
- IDX_SINGULAR_SIGLA
- IDX_SINGULAR_COD_UNIMED

#### Observações

- Representa cooperativas filiadas — apenas identidade própria (nome, sigla, código Unimed)
- Não duplica atributos da federação (registro ANS, site institucional da federação, etc.)
- Endereços e contatos em `ENDERECO` e `CONTATO` (DEC-DB-013)

---

### ENDERECO

#### Objetivo

Representar localizações físicas da Federação ou de uma Singular.

#### Responsabilidade

Persistir endereços postais sem duplicar dados em `FEDERACAO` ou `SINGULAR`.

#### Relacionamentos

- FK_ENDERECO_FEDERACAO → FEDERACAO (COD_FEDERACAO) — opcional
- FK_ENDERECO_SINGULAR → SINGULAR (COD_SINGULAR) — opcional
- Regra: exatamente um proprietário (`CK_ENDERECO_PROPRIETARIO`)

#### Sequence Oracle

- SQ_ENDERECO_COD_ENDERECO

#### Estrutura Física

| Coluna          | Tipo          | Obrigatório | Default      | Restrição |
| --------------- | ------------- | ----------- | ------------ | ---------- |
| COD_ENDERECO    | NUMBER(19)    | Sim         | - | PK         |
| COD_FEDERACAO   | NUMBER(19)    | Não         | NULL         | FK         |
| COD_SINGULAR    | NUMBER(19)    | Não         | NULL         | FK         |
| NOM_LOCAL       | VARCHAR2(100) | Sim         | -            | -          |
| TIP_ENDERECO    | VARCHAR2(30)  | Sim         | -            | -          |
| DES_LOGRADOURO  | VARCHAR2(200) | Sim         | -            | -          |
| NUM_ENDERECO    | VARCHAR2(20)  | Não         | NULL         | -          |
| DES_COMPLEMENTO | VARCHAR2(100) | Não         | NULL         | -          |
| NOM_BAIRRO      | VARCHAR2(100) | Sim         | -            | -          |
| NOM_CIDADE      | VARCHAR2(100) | Sim         | -            | -          |
| SIG_UF          | CHAR(2)       | Sim         | -            | -          |
| NUM_CEP         | VARCHAR2(8)   | Sim         | -            | -          |
| FLG_PRINCIPAL   | CHAR(1)       | Sim         | 'N'          | CK         |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_ENDERECO
- FK_ENDERECO_FEDERACAO
- FK_ENDERECO_SINGULAR
- CK_ENDERECO_PROPRIETARIO
- CK_ENDERECO_PRINCIPAL

#### Índices

- IDX_ENDERECO_FEDERACAO
- IDX_ENDERECO_SINGULAR

#### Observações

- `NOM_LOCAL` identifica o local de forma legível (ex.: Sede Administrativa, Atendimento Centro)
- `TIP_ENDERECO` validado pela aplicação — sem CHECK rígido no banco (DEC-DB-015)
- Unicidade de endereço principal por proprietário: validada pela camada de domínio

---

### CONTATO

#### Objetivo

Representar canais de comunicação da Federação, Singular, Área, Equipe ou Colaborador.

#### Responsabilidade

Centralizar telefones, e-mails, WhatsApp, ramal e demais canais — sem dados de endereço.

#### Relacionamentos

- FK_CONTATO_FEDERACAO → FEDERACAO (COD_FEDERACAO) — opcional
- FK_CONTATO_SINGULAR → SINGULAR (COD_SINGULAR) — opcional
- FK_CONTATO_AREA → AREA (COD_AREA) — opcional
- FK_CONTATO_EQUIPE → EQUIPE (COD_EQUIPE) — opcional
- FK_CONTATO_COLABORADOR → COLABORADOR (COD_COLABORADOR) — opcional
- Regra: exatamente um proprietário (`CK_CONTATO_PROPRIETARIO`)

#### Sequence Oracle

- SQ_CONTATO_COD_CONTATO

#### Estrutura Física

| Coluna          | Tipo          | Obrigatório | Default      | Restrição |
| --------------- | ------------- | ----------- | ------------ | ---------- |
| COD_CONTATO     | NUMBER(19)    | Sim         | - | PK         |
| COD_FEDERACAO   | NUMBER(19)    | Não         | NULL         | FK         |
| COD_SINGULAR    | NUMBER(19)    | Não         | NULL         | FK         |
| COD_AREA        | NUMBER(19)    | Não         | NULL         | FK         |
| COD_EQUIPE      | NUMBER(19)    | Não         | NULL         | FK         |
| COD_COLABORADOR | NUMBER(19)    | Não         | NULL         | FK         |
| TIP_CONTATO     | VARCHAR2(30)  | Sim         | -            | -          |
| DSC_CONTATO     | VARCHAR2(200) | Não         | NULL         | -          |
| DES_VALOR       | VARCHAR2(255) | Sim         | -            | -          |
| DES_HORARIO     | VARCHAR2(200) | Não         | NULL         | -          |
| FLG_PRINCIPAL   | CHAR(1)       | Sim         | 'N'          | CK         |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_CONTATO
- FK_CONTATO_FEDERACAO
- FK_CONTATO_SINGULAR
- FK_CONTATO_AREA
- FK_CONTATO_EQUIPE
- FK_CONTATO_COLABORADOR
- CK_CONTATO_PROPRIETARIO
- CK_CONTATO_PRINCIPAL

#### Índices

- IDX_CONTATO_FEDERACAO
- IDX_CONTATO_SINGULAR
- IDX_CONTATO_AREA
- IDX_CONTATO_EQUIPE
- IDX_CONTATO_COLABORADOR

#### Observações

- `TIP_CONTATO` validado pela aplicação — sem CHECK rígido no banco (DEC-DB-015)
- Único repositório de canais — não criar entidades `EMAIL`, `PHONE`, `WHATSAPP` (DEC-DB-014/016)
- Proprietários: Federação, Singular, Área, Equipe ou Colaborador (DEC-DB-016)
- Não armazena informações de endereço

---

### AREA

#### Objetivo

Representar áreas organizacionais da Federação e das Singulares.

#### Responsabilidade

Estruturar hierarquia organizacional com suporte a auto-referência, gestão e contatos institucionais.

#### Relacionamentos

- FK_AREA_SINGULAR → SINGULAR (COD_SINGULAR)
- FK_AREA_PAI → AREA (COD_AREA)
- FK_AREA_GESTOR → COLABORADOR (COD_GESTOR)
- AREA (1) ── (N) CONTATO via `COD_AREA`
- AREA (1) ── (N) EQUIPE via `EQUIPE.COD_AREA`

#### Sequence Oracle

- SQ_AREA_COD_AREA

#### Estrutura Física

| Coluna          | Tipo          | Obrigatório | Default      | Restrição |
| --------------- | ------------- | ----------- | ------------ | ---------- |
| COD_AREA        | NUMBER(19)    | Sim         | -            | PK         |
| COD_SINGULAR    | NUMBER(19)    | Não         | -            | FK         |
| COD_AREA_PAI    | NUMBER(19)    | Não         | -            | FK         |
| NOM_AREA        | VARCHAR2(200) | Sim         | -            | -          |
| SIG_AREA        | VARCHAR2(30)  | Não         | -            | -          |
| DSC_AREA        | CLOB          | Não         | NULL         | -          |
| COD_GESTOR      | NUMBER(19)    | Não         | NULL         | FK         |
| FLG_ATIVO       | CHAR(1)       | Sim         | 'S'          | CK         |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_AREA
- FK_AREA_SINGULAR
- FK_AREA_PAI
- FK_AREA_GESTOR
- CK_AREA_ATIVO

#### Índices

- IDX_AREA_SINGULAR
- IDX_AREA_PAI
- IDX_AREA_GESTOR

#### Observações

- COD_SINGULAR nulo indica área da Federação
- Contatos institucionais via `CONTATO.COD_AREA` — não colunas de canal em `AREA`
- `COD_GESTOR` — gestor único; tabela de associação somente se Feature exigir múltiplos gestores (DEC-DB-015)

---

### EQUIPE

#### Objetivo

Representar equipes vinculadas às áreas organizacionais.

#### Responsabilidade

Agrupar colaboradores em unidades operacionais dentro de uma área.

#### Relacionamentos

- FK_EQUIPE_AREA → AREA (COD_AREA)
- FK_EQUIPE_LIDER → COLABORADOR (COD_LIDER)
- EQUIPE (1) ── (N) CONTATO via `COD_EQUIPE`
- EQUIPE (1) ── (N) COLABORADOR via `COLABORADOR.COD_EQUIPE` (membros)

#### Sequence Oracle

- SQ_EQUIPE_COD_EQUIPE

#### Estrutura Física

| Coluna          | Tipo          | Obrigatório | Default      | Restrição |
| --------------- | ------------- | ----------- | ------------ | ---------- |
| COD_EQUIPE      | NUMBER(19)    | Sim         | -          | PK         |
| COD_AREA        | NUMBER(19)    | Sim         | -            | FK         |
| NOM_EQUIPE      | VARCHAR2(200) | Sim         | -            | -          |
| DSC_EQUIPE      | CLOB          | Não         | NULL         | -          |
| COD_LIDER       | NUMBER(19)    | Não         | NULL         | FK         |
| FLG_ATIVO       | CHAR(1)       | Sim         | 'S'          | CK         |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_EQUIPE
- FK_EQUIPE_AREA
- FK_EQUIPE_LIDER
- CK_EQUIPE_ATIVO

#### Índices

- IDX_EQUIPE_AREA
- IDX_EQUIPE_LIDER

#### Observações

- Contatos institucionais via `CONTATO.COD_EQUIPE`
- `COD_LIDER` — líder único; tabela de associação somente se Feature exigir múltiplos líderes (DEC-DB-015)
- Membros: `COLABORADOR.COD_EQUIPE`

---

### COLABORADOR

#### Objetivo

Representar o perfil do colaborador no Portal — atributos intrínsecos, vínculo organizacional e gestor direto.

#### Responsabilidade

Manter identificação, vínculo organizacional, dados pessoais/profissionais e referência ao gestor. Canais de comunicação em `CONTATO`.

#### Relacionamentos

- FK_COLABORADOR_FEDERACAO → FEDERACAO (COD_FEDERACAO)
- FK_COLABORADOR_SINGULAR → SINGULAR (COD_SINGULAR)
- FK_COLABORADOR_AREA → AREA (COD_AREA)
- FK_COLABORADOR_EQUIPE → EQUIPE (COD_EQUIPE)
- FK_COLABORADOR_GESTOR → COLABORADOR (COD_GESTOR) — auto-referência
- COLABORADOR (1) ── (N) CONTATO via `COD_COLABORADOR`

#### Sequence Oracle

- SQ_COLABORADOR_COD_COLABORADOR

#### Estrutura Física

| Coluna            | Tipo          | Obrigatório | Default                | Restrição |
| ----------------- | ------------- | ----------- | ---------------------- | ---------- |
| COD_COLABORADOR   | NUMBER(19)    | Sim         | - | PK         |
| COD_FEDERACAO     | NUMBER(19)    | Sim         | -                      | FK         |
| COD_SINGULAR      | NUMBER(19)    | Não         | -                      | FK         |
| COD_AREA          | NUMBER(19)    | Não         | -                      | FK         |
| COD_EQUIPE        | NUMBER(19)    | Não         | -                      | FK         |
| COD_GESTOR        | NUMBER(19)    | Não         | NULL                   | FK         |
| NOM_COLABORADOR   | VARCHAR2(200) | Sim         | -                      | -          |
| DES_EMAIL         | VARCHAR2(255) | Sim         | -                      | UK         |
| DES_CARGO         | VARCHAR2(100) | Não         | NULL                   | -          |
| ID_ZIMBRA         | VARCHAR2(255) | Não         | NULL                   | UK         |
| NUM_CPF           | VARCHAR2(11)  | Não         | -                      | UK         |
| DES_BIOGRAFIA     | CLOB          | Não         | NULL                   | -          |
| FLG_ATIVO         | CHAR(1)       | Sim         | 'S'                    | CK         |
| DAT_NASCIMENTO    | TIMESTAMP(6)  | Não         | NULL                   | -          |
| DAT_CONTRATACAO   | TIMESTAMP(6)  | Não         | NULL                   | -          |
| DAT_ULTIMO_ACESSO | TIMESTAMP(6)  | Não         | NULL                   | -          |
| DAT_CADASTRO      | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP           | -          |
| DAT_ATUALIZACAO   | TIMESTAMP(6)  | Não         | NULL                   | -          |

#### Restrições

- PK_COLABORADOR
- FK_COLABORADOR_FEDERACAO
- FK_COLABORADOR_SINGULAR
- FK_COLABORADOR_AREA
- FK_COLABORADOR_EQUIPE
- FK_COLABORADOR_GESTOR
- UK_COLABORADOR_EMAIL
- UK_COLABORADOR_CPF
- UK_COLABORADOR_ZIMBRA
- CK_COLABORADOR_ATIVO

#### Índices

- IDX_COLABORADOR_EMAIL
- IDX_COLABORADOR_FEDERACAO
- IDX_COLABORADOR_SINGULAR
- IDX_COLABORADOR_AREA
- IDX_COLABORADOR_EQUIPE
- IDX_COLABORADOR_GESTOR
- IDX_COLABORADOR_ZIMBRA

#### Observações

- COD_FEDERACAO é obrigatório para todos os colaboradores
- COD_SINGULAR, COD_AREA e COD_EQUIPE dependem do contexto organizacional
- Colaboradores da Federação não possuem obrigatoriedade de vínculo com Singular, Área ou Equipe
- Colaborador federativo: COD_FEDERACAO obrigatório; demais vínculos nulos
- Colaborador de singular: COD_FEDERACAO, COD_SINGULAR e COD_AREA obrigatórios; COD_EQUIPE opcional
- O escopo de atuação é determinado exclusivamente pela entidade PAPEL_ATRIBUICAO
- `DES_EMAIL` é e-mail de identidade/login (FT-AUTH/DEC-DB-011) — canais adicionais em `CONTATO` (DEC-DB-016)
- `DES_CARGO` — cargo como atributo simples; sem entidade `CARGO` (DEC-DB-016)
- `COD_GESTOR` — gestor direto único; auto-referência sem tabelas de hierarquia (DEC-DB-016)
- `DAT_CONTRATACAO` — aniversário de empresa derivado na aplicação
- Canais do colaborador: `CONTATO.COD_COLABORADOR`
- `ID_ZIMBRA` é preenchido na autenticação via Zimbra (FT-AUTH); colaboradores cadastrados por onboarding podem permanecer sem identificador Zimbra até o primeiro login
- Identidade do colaborador: `COD_COLABORADOR`, `DES_EMAIL` e `ID_ZIMBRA` (quando disponível). Sem `NUM_MATRICULA` na versão atual (DEC-DB-011)

---

### AUTH_SESSAO

#### Objetivo

Persistir sessões de autenticação stateless do Portal — Refresh Token, revogação e `session_id`.

#### Responsabilidade

Controlar continuidade da autenticação após o login: hash do Refresh Token, expiração, revogação (logout, limite de sessões, revogação administrativa).

#### Relacionamentos

- FK_AUTH_SESSAO_COLABORADOR → COLABORADOR (COD_COLABORADOR)

#### Sequence Oracle

- SQ_AUTH_SESSAO_COD_SESSAO

#### Estrutura Física

| Coluna             | Tipo          | Obrigatório | Default                              | Restrição |
| ------------------ | ------------- | ----------- | ------------------------------------ | ---------- |
| COD_SESSAO         | NUMBER(19)    | Sim         | -    | PK         |
| ID_SESSAO          | VARCHAR2(36)  | Sim         | -                                    | UK         |
| COD_COLABORADOR    | NUMBER(19)    | Sim         | -                                    | FK         |
| HASH_REFRESH_TOKEN | VARCHAR2(255) | Sim         | -                                    | UK         |
| DES_DISPOSITIVO    | VARCHAR2(255) | Não         | NULL                                 | -          |
| FLG_REMEMBER_ME    | CHAR(1)       | Sim         | 'N'                                  | CK         |
| DAT_CRIACAO        | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP                         | -          |
| DAT_EXPIRACAO      | TIMESTAMP(6)  | Sim         | -                                    | -          |
| FLG_REVOGADA       | CHAR(1)       | Sim         | 'N'                                  | CK         |
| DAT_REVOGACAO      | TIMESTAMP(6)  | Não         | NULL                                 | -          |

#### Restrições

- PK_AUTH_SESSAO
- UK_AUTH_SESSAO_ID
- UK_AUTH_SESSAO_HASH
- FK_AUTH_SESSAO_COLABORADOR
- CK_AUTH_SESSAO_FLG_REMEMBER
- CK_AUTH_SESSAO_FLG_REVOGADA

#### Índices

- IDX_AUTH_SESSAO_COLABORADOR

#### Observações

- Não armazena Access Token (JWT) — validado localmente com TTL de 15 minutos
- `ID_SESSAO` é o identificador público exposto em operações administrativas (RF-AUTH-010)
- Feature: FT-AUTH (`specs/features/authentication/specification.md`)

---

### ONBOARDING_SOLICITACAO

#### Objetivo

Registrar solicitações de cadastro e ativação de acesso ao portal.

#### Responsabilidade

Controlar fluxo de onboarding com status e datas de processamento.

#### Relacionamentos

- FK_COLABORADOR_ONBOARDING → COLABORADOR (COD_COLABORADOR)

#### Sequence Oracle

- SQ_ONBOARD_SOLIC

#### Estrutura Física

| Coluna                     | Tipo         | Obrigatório | Default      | Restrição |
| -------------------------- | ------------ | ----------- | ------------ | ---------- |
| COD_ONBOARDING_SOLICITACAO | NUMBER(19)   | Sim         | - | PK         |
| COD_COLABORADOR            | NUMBER(19)   | Sim         | -            | FK         |
| STA_SOLICITACAO            | VARCHAR2(30) | Sim         | 'PENDENTE'   | CK         |
| DSC_OBSERVACAO             | CLOB         | Não         | NULL         | -          |
| DAT_SOLICITACAO            | TIMESTAMP(6) | Sim         | SYSTIMESTAMP | -          |
| DAT_PROCESSAMENTO          | TIMESTAMP(6) | Não         | NULL         | -          |

#### Restrições

- PK_ONBOARDING_SOLICITACAO
- FK_COLABORADOR_ONBOARDING
- CK_ONBOARDING_STATUS

#### Índices

- Nenhum

#### Observações

- Status: PENDENTE, APROVADA, REJEITADA

---

### CATEGORIA_DOCUMENTAL

#### Objetivo

Classificar documentos corporativos por categoria.

#### Responsabilidade

Manter taxonomia documental utilizada na publicação de documentos.

#### Relacionamentos

- Nenhuma Chave Estrangeira

#### Sequence Oracle

- SQ_CAT_DOC_COD_CAT_DOC

#### Estrutura Física

| Coluna                   | Tipo          | Obrigatório | Default      | Restrição |
| ------------------------ | ------------- | ----------- | ------------ | ---------- |
| COD_CATEGORIA_DOCUMENTAL | NUMBER(19)    | Sim         | - | PK         |
| NOM_CATEGORIA            | VARCHAR2(150) | Sim         | -            | UK         |
| DSC_CATEGORIA            | CLOB          | Não         | NULL         | -          |
| FLG_ATIVO                | CHAR(1)       | Sim         | 'S'          | CK         |
| DAT_CADASTRO             | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO          | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_CATEGORIA_DOCUMENTAL
- UK_CATEGORIA_DOCUMENTAL_NOME
- CK_CATEGORIA_DOCUMENTAL_ATIVO

#### Índices

- IDX_CATEGORIA_DOCUMENTAL_NOME

#### Observações

- Nenhuma

---

### PASTA

#### Objetivo

Estruturar armazenamento documental em hierarquia de pastas.

#### Responsabilidade

Organizar documentos com suporte a herança de permissões.

#### Relacionamentos

- FK_PASTA_PAI → PASTA (COD_PASTA)

#### Sequence Oracle

- SQ_PASTA_COD_PASTA

#### Estrutura Física

| Coluna              | Tipo          | Obrigatório | Default      | Restrição |
| ------------------- | ------------- | ----------- | ------------ | ---------- |
| COD_PASTA           | NUMBER(19)    | Sim         | -           | PK         |
| COD_PASTA_PAI       | NUMBER(19)    | Não         | -            | FK         |
| NOM_PASTA           | VARCHAR2(200) | Sim         | -            | -          |
| DSC_PASTA           | CLOB          | Não         | NULL         | -          |
| FLG_HERDA_PERMISSAO | CHAR(1)       | Sim         | 'S'          | CK         |
| FLG_ATIVO           | CHAR(1)       | Sim         | 'S'          | CK         |
| DAT_CADASTRO        | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO     | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_PASTA
- FK_PASTA_PAI
- CK_PASTA_HERDA_PERMISSAO
- CK_PASTA_ATIVO

#### Índices

- IDX_PASTA_PAI
- IDX_PASTA_NOME

#### Observações

- FLG_HERDA_PERMISSAO controla propagação de permissões da pasta pai

---

### DOCUMENTO

#### Objetivo

Armazenar metadados de documentos corporativos.

#### Responsabilidade

Gerenciar ciclo de vida documental (publicação, expiração, status).

#### Relacionamentos

- FK_DOCUMENTO_COLABORADOR → COLABORADOR (COD_COLABORADOR)
- FK_DOCUMENTO_CATEGORIA → CATEGORIA_DOCUMENTAL (COD_CATEGORIA_DOCUMENTAL)
- FK_DOCUMENTO_PASTA → PASTA (COD_PASTA)

#### Sequence Oracle

- SQ_DOCUMENTO_COD_DOCUMENTO

#### Estrutura Física

| Coluna                   | Tipo          | Obrigatório | Default      | Restrição |
| ------------------------ | ------------- | ----------- | ------------ | ---------- |
| COD_DOCUMENTO            | NUMBER(19)    | Sim         | -       | PK         |
| COD_CATEGORIA_DOCUMENTAL | NUMBER(19)    | Sim         | -            | FK         |
| COD_PASTA                | NUMBER(19)    | Sim         | -            | FK         |
| COD_COLABORADOR          | NUMBER(19)    | Sim         | -            | FK         |
| TIT_DOCUMENTO            | VARCHAR2(300) | Sim         | -            | -          |
| DSC_DOCUMENTO            | CLOB          | Não         | NULL         | -          |
| STA_DOCUMENTO            | VARCHAR2(30)  | Sim         | 'ATIVO'      | CK         |
| DAT_PUBLICACAO           | TIMESTAMP(6)  | Não         | NULL         | -          |
| DAT_EXPIRACAO            | TIMESTAMP(6)  | Não         | NULL         | -          |
| DAT_CADASTRO             | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO          | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_DOCUMENTO
- FK_DOCUMENTO_COLABORADOR
- FK_DOCUMENTO_CATEGORIA
- FK_DOCUMENTO_PASTA
- CK_DOCUMENTO_STATUS

#### Índices

- IDX_DOCUMENTO_TITULO
- IDX_DOCUMENTO_STATUS
- IDX_DOCUMENTO_DATA_PUBLICACAO
- IDX_DOCUMENTO_PASTA
- IDX_DOCUMENTO_CATEGORIA

#### Observações

- Conteúdo binário armazenado via DOCUMENTO_VERSAO e ARQUIVO_BINARIO
- Status: ATIVO, ARQUIVADO, EXPIRADO

---

### ARQUIVO_BINARIO

#### Objetivo

Registrar metadados de arquivos armazenados externamente.

#### Responsabilidade

Manter referência, integridade (hash) e características técnicas do arquivo.

#### Relacionamentos

- É referenciado pela entidade DOCUMENTO_VERSAO.

#### Sequence Oracle

- SQ_ARQ_BIN_COD_ARQ_BIN

#### Estrutura Física

| Coluna              | Tipo           | Obrigatório | Default      | Restrição |
| ------------------- | -------------- | ----------- | ------------ | ---------- |
| COD_ARQUIVO_BINARIO | NUMBER(19)     | Sim         | - | PK         |
| NOM_ARQUIVO         | VARCHAR2(500)  | Sim         | -            | -          |
| URL_ARQUIVO         | VARCHAR2(2000) | Sim         | -            | -          |
| TIP_MIME            | VARCHAR2(200)  | Sim         | -            | -          |
| QTD_TAMANHO_BYTES   | NUMBER(19)     | Sim         | -            | -          |
| HASH_ARQUIVO        | VARCHAR2(128)  | Sim         | -            | UK         |
| DAT_CADASTRO        | TIMESTAMP(6)   | Sim         | SYSTIMESTAMP | -          |

#### Restrições

- PK_ARQUIVO_BINARIO
- UK_ARQUIVO_HASH

#### Índices

- IDX_ARQUIVO_HASH

#### Observações

- Binários armazenados fora do banco; Oracle mantém somente metadados

---

### DOCUMENTO_VERSAO

#### Objetivo

Versionar documentos publicados com histórico imutável.

#### Responsabilidade

Controlar versões, versão atual e vínculo com arquivo binário.

#### Relacionamentos

- FK_DOC_DOC_VERS → DOCUMENTO (COD_DOCUMENTO)
- FK_ARQUIVO_DOCUMENTO_VERSAO → ARQUIVO_BINARIO (COD_ARQUIVO_BINARIO)
- FK_COLAB_DOC_VERS → COLABORADOR (COD_COLABORADOR)

#### Sequence Oracle

- SQ_DOC_VERS_COD_DOC_VERS

#### Estrutura Física

| Coluna               | Tipo         | Obrigatório | Default      | Restrição |
| -------------------- | ------------ | ----------- | ------------ | ---------- |
| COD_DOCUMENTO_VERSAO | NUMBER(19)   | Sim         | - | PK         |
| COD_DOCUMENTO        | NUMBER(19)   | Sim         | -            | FK         |
| COD_ARQUIVO_BINARIO  | NUMBER(19)   | Sim         | -            | FK         |
| COD_COLABORADOR      | NUMBER(19)   | Sim         | -            | FK         |
| NUM_VERSAO           | NUMBER(10)   | Sim         | -            | -          |
| DSC_ALTERACAO        | CLOB         | Não         | NULL         | -          |
| FLG_VERSAO_ATUAL     | CHAR(1)      | Sim         | 'N'          | CK         |
| DAT_VERSAO           | TIMESTAMP(6) | Sim         | SYSTIMESTAMP | -          |

#### Restrições

- PK_DOCUMENTO_VERSAO
- FK_DOC_DOC_VERS
- FK_ARQUIVO_DOCUMENTO_VERSAO
- FK_COLAB_DOC_VERS
- CK_DOCUMENTO_VERSAO_ATUAL
- UK_DOCUMENTO_VERSAO

#### Índices

- IDX_DOCUMENTO_VERSAO_DOCUMENTO
- IDX_DOCUMENTO_VERSAO_ATUAL
- IDX_DOCUMENTO_VERSAO_DATA

#### Observações

- Apenas um registro com FLG_VERSAO_ATUAL = 'S' por COD_DOCUMENTO
- UK_DOCUMENTO_VERSAO garante unicidade de (COD_DOCUMENTO, NUM_VERSAO)
- A regra de versão atual única deverá ser implementada pela camada de domínio e/ou por mecanismos adicionais do banco

---

### COMPARTILHAMENTO

#### Objetivo

Controlar compartilhamento genérico de documentos, pastas e comunicados.

#### Responsabilidade

Definir origem, destinatário e tipo de acesso de forma polimórfica.

#### Relacionamentos

- Modelo polimórfico sem Chaves Estrangeiras físicas para TIP_ORIGEM/COD_ORIGEM e TIP_DESTINATARIO/COD_DESTINATARIO

#### Sequence Oracle

- SQ_COMPART_COD_COMPART

#### Estrutura Física

| Coluna               | Tipo         | Obrigatório | Default      | Restrição |
| -------------------- | ------------ | ----------- | ------------ | ---------- |
| COD_COMPARTILHAMENTO | NUMBER(19)   | Sim         | - | PK         |
| TIP_ORIGEM           | VARCHAR2(30) | Sim         | -            | CK         |
| COD_ORIGEM           | NUMBER(19)   | Sim         | -            | -          |
| TIP_DESTINATARIO     | VARCHAR2(30) | Sim         | -            | CK         |
| COD_DESTINATARIO     | NUMBER(19)   | Sim         | -            | -          |
| TIP_ACESSO           | VARCHAR2(30) | Sim         | -            | CK         |
| DAT_CADASTRO         | TIMESTAMP(6) | Sim         | SYSTIMESTAMP | -          |

#### Restrições

- PK_COMPARTILHAMENTO
- UK_COMPARTILHAMENTO
- CK_COMPARTILHAMENTO_ORIGEM
- CK_COMPARTILHAMENTO_DESTINO
- CK_COMPARTILHAMENTO_ACESSO

#### Índices

- IDX_COMPART_DEST
- IDX_COMPARTILHAMENTO_ORIGEM

#### Observações

- TIP_ORIGEM: DOCUMENTO, PASTA, COMUNICADO
- TIP_DESTINATARIO: FEDERACAO, SINGULAR, AREA, EQUIPE, COLABORADOR
- TIP_ACESSO: LEITURA, DOWNLOAD, EDICAO, ADMINISTRACAO
- Não é possível implementar integridade referencial por meio de Chaves Estrangeiras devido ao modelo polimórfico
- A validação deverá ser realizada pela camada de domínio e/ou por mecanismos adicionais do banco

---

### PAPEL

#### Objetivo

Definir perfis de acesso utilizados pelo portal.

#### Responsabilidade

Catalogar papéis com nome, descrição e status de ativação.

#### Relacionamentos

- Nenhum relacionamento de entrada.

#### Sequence Oracle

- SQ_PAPEL_COD_PAPEL

#### Estrutura Física

| Coluna          | Tipo          | Obrigatório | Default      | Restrição |
| --------------- | ------------- | ----------- | ------------ | ---------- |
| COD_PAPEL       | NUMBER(19)    | Sim         | -           | PK         |
| NOM_PAPEL       | VARCHAR2(100) | Sim         | -            | UK         |
| DSC_PAPEL       | CLOB          | Não         | NULL         | -          |
| FLG_ATIVO       | CHAR(1)       | Sim         | 'S'          | CK         |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_PAPEL
- UK_PAPEL_NOME
- CK_PAPEL_ATIVO

#### Índices

- IDX_PAPEL_NOME

#### Observações

- Nenhuma

---

### PAPEL_ATRIBUICAO

#### Objetivo

Associar papéis a colaboradores com escopo organizacional.

#### Responsabilidade

Controlar vigência e escopo de autorização por estrutura organizacional.

#### Relacionamentos

- FK_PAPEL_ATRIB_COLAB → COLABORADOR (COD_COLABORADOR)
- FK_PAPEL_ATRIBUICAO_PAPEL → PAPEL (COD_PAPEL)
- FK_PAPEL_ATRIBUICAO_FEDERACAO → FEDERACAO (COD_FEDERACAO)
- FK_PAPEL_ATRIBUICAO_SINGULAR → SINGULAR (COD_SINGULAR)
- FK_PAPEL_ATRIBUICAO_AREA → AREA (COD_AREA)
- FK_PAPEL_ATRIBUICAO_EQUIPE → EQUIPE (COD_EQUIPE)

#### Sequence Oracle

- SQ_PAPEL_ATRIB_COD_PAPEL_ATRIB

#### Estrutura Física

| Coluna               | Tipo         | Obrigatório | Default      | Restrição |
| -------------------- | ------------ | ----------- | ------------ | ---------- |
| COD_PAPEL_ATRIBUICAO | NUMBER(19)   | Sim         | - | PK         |
| COD_COLABORADOR      | NUMBER(19)   | Sim         | -            | FK         |
| COD_PAPEL            | NUMBER(19)   | Sim         | -            | FK         |
| COD_FEDERACAO        | NUMBER(19)   | Não         | -            | FK         |
| COD_SINGULAR         | NUMBER(19)   | Não         | -            | FK         |
| COD_AREA             | NUMBER(19)   | Não         | -            | FK         |
| COD_EQUIPE           | NUMBER(19)   | Não         | -            | FK         |
| DAT_INICIO_VIGENCIA  | TIMESTAMP(6) | Sim         | SYSTIMESTAMP | -          |
| DAT_FIM_VIGENCIA     | TIMESTAMP(6) | Não         | NULL         | -          |
| FLG_ATIVO            | CHAR(1)      | Sim         | 'S'          | CK         |

#### Restrições

- PK_PAPEL_ATRIBUICAO
- FK_PAPEL_ATRIB_COLAB
- FK_PAPEL_ATRIBUICAO_PAPEL
- FK_PAPEL_ATRIBUICAO_FEDERACAO
- FK_PAPEL_ATRIBUICAO_SINGULAR
- FK_PAPEL_ATRIBUICAO_AREA
- FK_PAPEL_ATRIBUICAO_EQUIPE
- CK_PAPEL_ATRIBUICAO_ATIVO

#### Índices

- IDX_PAPEL_ATRIB_COLAB
- IDX_PAPEL_ATRIBUICAO_PAPEL
- IDX_PAPEL_ATRIBUICAO_ESCOPOS

#### Observações

- Se todos os escopos organizacionais estiverem nulos, a atribuição representa um papel global
- Caso exista qualquer escopo preenchido, a atribuição limita-se ao respectivo contexto organizacional

---

### PERMISSAO_PASTA

#### Objetivo

Definir permissões explícitas de acesso a pastas.

#### Responsabilidade

Conceder acesso direto a destinatários organizacionais ou individuais.

#### Relacionamentos

- FK_PERMISSAO_PASTA_PASTA → PASTA (COD_PASTA)

#### Sequence Oracle

- SQ_PERM_PASTA_COD_PERM_PASTA

#### Estrutura Física

| Coluna              | Tipo         | Obrigatório | Default      | Restrição |
| ------------------- | ------------ | ----------- | ------------ | ---------- |
| COD_PERMISSAO_PASTA | NUMBER(19)   | Sim         | - | PK         |
| COD_PASTA           | NUMBER(19)   | Sim         | -            | FK         |
| TIP_DESTINATARIO    | VARCHAR2(30) | Sim         | -            | CK         |
| COD_DESTINATARIO    | NUMBER(19)   | Sim         | -            | -          |
| TIP_ACESSO          | VARCHAR2(30) | Sim         | -            | CK         |
| DAT_CADASTRO        | TIMESTAMP(6) | Sim         | SYSTIMESTAMP | -          |

#### Restrições

- PK_PERMISSAO_PASTA
- FK_PERMISSAO_PASTA_PASTA
- CK_PERMISSAO_PASTA_DESTINO
- CK_PERMISSAO_PASTA_ACESSO

#### Índices

- IDX_PERM_PASTA_DEST
- IDX_PERMISSAO_PASTA_PASTA

#### Observações

- TIP_DESTINATARIO: FEDERACAO, SINGULAR, AREA, EQUIPE, COLABORADOR
- TIP_ACESSO: LEITURA, DOWNLOAD, EDICAO, ADMINISTRACAO

---

### SOLICITACAO_PERMISSAO

#### Objetivo

Registrar solicitações de acesso a pastas ou documentos.

#### Responsabilidade

Controlar fluxo de aprovação de permissões com justificativa e análise.

#### Relacionamentos

- FK_SOLICITACAO_COLABORADOR → COLABORADOR (COD_COLABORADOR)
- FK_SOLICITACAO_PASTA → PASTA (COD_PASTA)
- FK_SOLICITACAO_DOCUMENTO → DOCUMENTO (COD_DOCUMENTO)

#### Sequence Oracle

- SQ_SOLIC_PERM_COD_SOLIC_PERM

#### Estrutura Física

| Coluna                    | Tipo         | Obrigatório | Default      | Restrição |
| ------------------------- | ------------ | ----------- | ------------ | ---------- |
| COD_SOLICITACAO_PERMISSAO | NUMBER(19)   | Sim         | - | PK         |
| COD_COLABORADOR           | NUMBER(19)   | Sim         | -            | FK         |
| COD_PASTA                 | NUMBER(19)   | Não         | -            | FK         |
| COD_DOCUMENTO             | NUMBER(19)   | Não         | -            | FK         |
| STA_SOLICITACAO           | VARCHAR2(30) | Sim         | 'PENDENTE'   | CK         |
| DSC_JUSTIFICATIVA         | CLOB         | Não         | NULL         | -          |
| DAT_SOLICITACAO           | TIMESTAMP(6) | Sim         | SYSTIMESTAMP | -          |
| DAT_ANALISE               | TIMESTAMP(6) | Não         | NULL         | -          |

#### Restrições

- PK_SOLICITACAO_PERMISSAO
- FK_SOLICITACAO_COLABORADOR
- FK_SOLICITACAO_PASTA
- FK_SOLICITACAO_DOCUMENTO
- CK_SOLICITACAO_STATUS

#### Índices

- IDX_SOLICITACAO_STATUS
- IDX_SOLICITACAO_COLABORADOR

#### Observações

- COD_PASTA ou COD_DOCUMENTO deve ser informado conforme escopo da solicitação
- A obrigatoriedade contextual de COD_PASTA ou COD_DOCUMENTO deverá ser validada pela camada de domínio
- Status: PENDENTE, APROVADA, REJEITADA

---

### REGISTRO_AUDITORIA

#### Objetivo

Registrar histórico de operações auditáveis do portal.

#### Responsabilidade

Persistir eventos com dados antes/depois para rastreabilidade completa.

#### Relacionamentos

- FK_REG_AUDIT_COLAB → COLABORADOR (COD_COLABORADOR)

#### Sequence Oracle

- SQ_REG_AUDIT_COD_REG_AUDIT

#### Estrutura Física

| Coluna                 | Tipo          | Obrigatório | Default      | Restrição |
| ---------------------- | ------------- | ----------- | ------------ | ---------- |
| COD_REGISTRO_AUDITORIA | NUMBER(19)    | Sim         | - | PK         |
| COD_COLABORADOR        | NUMBER(19)    | Não         | -            | FK         |
| TIP_EVENTO             | VARCHAR2(100) | Sim         | -            | -          |
| TIP_ENTIDADE           | VARCHAR2(100) | Sim         | -            | -          |
| COD_ENTIDADE           | NUMBER(19)    | Sim         | -            | -          |
| DADOS_ANTES            | CLOB          | Não         | NULL         | -          |
| DADOS_DEPOIS           | CLOB          | Não         | NULL         | -          |
| DAT_EVENTO             | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |

#### Restrições

- PK_REGISTRO_AUDITORIA
- FK_REG_AUDIT_COLAB

#### Índices

- IDX_REGISTRO_AUDITORIA_DATA
- IDX_REG_AUDIT_ENT
- IDX_REG_AUDIT_EVT

#### Observações

- Particionamento recomendado: `PARTITION BY RANGE (DAT_EVENTO)`
- Retenção: 5 anos

---

### COMUNICADO

#### Objetivo

Publicar comunicados institucionais para colaboradores.

#### Responsabilidade

Gerenciar conteúdo, publicação, expiração e destaque de comunicados.

#### Relacionamentos

- FK_COMUNICADO_COLABORADOR → COLABORADOR (COD_COLABORADOR)

#### Sequence Oracle

- SQ_COMUNICADO_COD_COMUNICADO

#### Estrutura Física

| Coluna          | Tipo          | Obrigatório | Default      | Restrição |
| --------------- | ------------- | ----------- | ------------ | ---------- |
| COD_COMUNICADO  | NUMBER(19)    | Sim         | -      | PK         |
| COD_COLABORADOR | NUMBER(19)    | Sim         | -            | FK         |
| TIT_COMUNICADO  | VARCHAR2(300) | Sim         | -            | -          |
| DSC_COMUNICADO  | CLOB          | Sim         | -            | -          |
| DAT_PUBLICACAO  | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_EXPIRACAO   | TIMESTAMP(6)  | Não         | NULL         | -          |
| FLG_PUBLICADO   | CHAR(1)       | Sim         | 'N'          | CK         |
| FLG_DESTAQUE    | CHAR(1)       | Sim         | 'N'          | CK         |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         | -          |

#### Restrições

- PK_COMUNICADO
- FK_COMUNICADO_COLABORADOR
- CK_COMUNICADO_PUBLICADO
- CK_COMUNICADO_DESTAQUE

#### Índices

- IDX_COMUNICADO_PUBLICACAO
- IDX_COMUNICADO_EXPIRACAO
- IDX_COMUNICADO_PUBLICADO
- IDX_COMUNICADO_AUTOR

#### Observações

- Compartilhamento via COMPARTILHAMENTO com TIP_ORIGEM = COMUNICADO

---

### NOTIFICACAO

#### Objetivo

Entregar notificações a colaboradores a partir de eventos do portal.

#### Responsabilidade

Controlar envio, leitura e categorização de notificações.

#### Relacionamentos

- FK_NOTIFICACAO_COLABORADOR → COLABORADOR (COD_COLABORADOR)

#### Sequence Oracle

- SQ_NOTIFICACAO_COD_NOTIFICACAO

#### Estrutura Física

| Coluna          | Tipo           | Obrigatório | Default      | Restrição |
| --------------- | -------------- | ----------- | ------------ | ---------- |
| COD_NOTIFICACAO | NUMBER(19)     | Sim         | -     | PK         |
| COD_COLABORADOR | NUMBER(19)     | Sim         | -            | FK         |
| TIT_NOTIFICACAO | VARCHAR2(300)  | Sim         | -            | -          |
| DSC_NOTIFICACAO | VARCHAR2(4000) | Sim         | -            | -          |
| TIP_NOTIFICACAO | VARCHAR2(50)   | Sim         | -            | CK         |
| FLG_LIDA        | CHAR(1)        | Sim         | 'N'          | CK         |
| DAT_LEITURA     | TIMESTAMP(6)   | Não         | NULL         | -          |
| DAT_ENVIO       | TIMESTAMP(6)   | Sim         | SYSTIMESTAMP | -          |

#### Restrições

- PK_NOTIFICACAO
- FK_NOTIFICACAO_COLABORADOR
- CK_NOTIFICACAO_LIDA
- CK_NOTIFICACAO_TIPO

#### Índices

- IDX_NOTIFICACAO_COLABORADOR
- IDX_NOTIFICACAO_LIDA
- IDX_NOTIFICACAO_ENVIO

#### Observações

- TIP_NOTIFICACAO: SISTEMA, DOCUMENTO, COMUNICADO, PERMISSAO, ONBOARDING

---

### CONFIGURACAO_PORTAL

#### Objetivo

Armazenar configurações globais do portal por Federação.

#### Responsabilidade

Parametrizar branding, funcionalidades e políticas operacionais.

#### Relacionamentos

- FK_CONFIG_PORT_FED → FEDERACAO (COD_FEDERACAO)

#### Sequence Oracle

- SQ_CONFIG_PORT_COD_CONFIG_PORT

#### Estrutura Física

| Coluna                       | Tipo           | Obrigatório | Default      | Restrição |
| ---------------------------- | -------------- | ----------- | ------------ | ---------- |
| COD_CONFIGURACAO_PORTAL      | NUMBER(19)     | Sim         | - | PK         |
| COD_FEDERACAO                | NUMBER(19)     | Sim         | -            | FK         |
| NOM_PORTAL                   | VARCHAR2(200)  | Sim         | -            | -          |
| URL_PORTAL                   | VARCHAR2(500)  | Não         | NULL         | -          |
| URL_LOGO                     | VARCHAR2(1000) | Não         | NULL         | -          |
| URL_FAVICON                  | VARCHAR2(1000) | Não         | NULL         | -          |
| DSC_RODAPE                   | VARCHAR2(4000) | Não         | NULL         | -          |
| FLG_ONBOARDING_ATIVO         | CHAR(1)        | Sim         | 'S'          | CK         |
| FLG_NOTIFICACAO_EMAIL        | CHAR(1)        | Sim         | 'S'          | CK         |
| FLG_COMUNICADO_DESTAQUE      | CHAR(1)        | Sim         | 'S'          | CK         |
| QTD_DIAS_EXPIRACAO_DOCUMENTO | NUMBER(5)      | Não         | 365          | -          |
| DAT_CADASTRO                 | TIMESTAMP(6)   | Sim         | SYSTIMESTAMP | -          |
| DAT_ATUALIZACAO              | TIMESTAMP(6)   | Não         | NULL         | -          |

#### Restrições

- PK_CONFIGURACAO_PORTAL
- FK_CONFIG_PORT_FED
- UK_CONFIG_PORT_FED
- CK_CONFIG_ONBOARDING
- CK_CONFIG_NOTIFICACAO_EMAIL
- CK_CONFIG_COMUNICADO_DESTAQUE

#### Índices

- IDX_CONFIG_PORT_FED

#### Observações

- O modelo suporta exatamente uma configuração para cada Federação
- No cenário atual existe apenas uma Federação, resultando em um único registro de configuração

---

## 6 Estratégias Arquiteturais

### Auditoria

As entidades de negócio deverão adotar o seguinte padrão mínimo de auditoria:

| Coluna          | Tipo          | Obrigatório | Default      |
| --------------- | ------------- | ----------- | ------------ |
| DAT_CADASTRO    | TIMESTAMP(6)  | Sim         | SYSTIMESTAMP |
| DAT_ATUALIZACAO | TIMESTAMP(6)  | Não         | NULL         |

Entidades com `FLG_ATIVO` adotam desativação lógica (`S`/`N`), conforme DDL.

O registro das operações é centralizado na entidade `REGISTRO_AUDITORIA`.

Eventos mínimos auditáveis:

- CREATE
- UPDATE
- DELETE
- LOGIN
- LOGOUT
- DOWNLOAD
- PUBLICACAO
- ALTERACAO_PERMISSAO
- RESTORE
- LOGIN_FAILURE

A estratégia foi projetada para suportar crescimento contínuo, retenção mínima de cinco anos e particionamento temporal baseado em DAT_EVENTO.

---

### Performance

- Índices em Chaves Estrangeiras, status, datas e campos de busca
- Metadados documentais no banco; binários em armazenamento externo
- Modelo genérico de COMPARTILHAMENTO evita proliferação de tabelas

### Regras de Domínio

Determinadas regras funcionais não podem ser garantidas exclusivamente por Restrições Oracle:

- Apenas uma versão atual (`FLG_VERSAO_ATUAL = 'S'`) por documento em `DOCUMENTO_VERSAO`
- Integridade referencial do compartilhamento polimórfico em `COMPARTILHAMENTO`
- Obrigatoriedade de `COD_PASTA` ou `COD_DOCUMENTO` conforme contexto em `SOLICITACAO_PERMISSAO`

Essas regras deverão ser implementadas pela camada de domínio e/ou por mecanismos adicionais do banco, quando aplicável.

### Particionamento

- REGISTRO_AUDITORIA: `PARTITION BY RANGE (DAT_EVENTO)`
- Partições mensais ou anuais conforme volume

### Crescimento

- Documentos: crescimento de metadados linear; binários externalizados
- Auditoria: crescimento contínuo mitigado por particionamento
- Notificações: índices por colaborador e data de envio

### Retenção

- REGISTRO_AUDITORIA: 5 anos
- Documentos: histórico imutável garantido pelo DOCUMENTO_VERSAO
- DELETE físico restrito; desativação via `FLG_ATIVO` quando aplicável na DDL

---

## 7 Convenções Gerais

- Schema exclusivo: UNMPORTCOM
- Padrões Oracle definidos na seção 3
- Estratégias arquiteturais definidas na seção 6
- Chaves Estrangeiras sem valor Default
- Restrições nomeadas com prefixos padronizados (PK_, FK_, UK_, CK_, IDX_, SQ_)
- Baseline estrutural via DDL oficial; evoluções pós-baseline via scripts DBA

---

## 8 Referência para DDL

Este documento está **sincronizado** com o baseline oficial em `docs/database/ddl/` (23 tabelas, 23 sequences — validação `ddl/901-validation.sql`).

Fluxo de governança:

```text
Modelo conceitual / lógico → Modelo físico (este documento) → DDL baseline (DBA) → Aplicação
```

Scripts de referência:

- 000-install.sql
- 001-create-users.sql
- 002-create-sequences.sql
- 003-create-tables.sql
- 004-create-constraints.sql
- 005-create-indexes.sql
- 006-create-comments.sql
- 007-create-grants.sql
- 008-initial-data.sql
- 900-drop-all.sql
- 901-validation.sql
- 902-compile-invalid-objects.sql

### Comentários Oracle

Todos os objetos físicos deverão possuir:

- COMMENT ON TABLE
- COMMENT ON COLUMN

para manter documentação do dicionário de dados Oracle.

Artefatos gerados:

- CREATE TABLE
- CREATE SEQUENCE
- CREATE INDEX
- Chave Primária
- Chave Estrangeira
- Restrição de Unicidade
- Restrição CHECK
- BASELINE Oracle UNMPORTCOM
