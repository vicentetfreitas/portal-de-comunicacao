# Decisões Arquiteturais, Premissas e Questões em Aberto

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Sistema | Portal de Comunicação — Unimed Ceará |
| Banco de Dados | Oracle Database |
| Schema | UNMPORTCOM |
| Versão | 4.7 |
| Status | APPROVED |
| Base | Domain + Architecture + Database |

---

# 1. Objetivo

Documentar as decisões arquiteturais consolidadas, as premissas estruturais adotadas pelo projeto e as questões funcionais ainda pendentes de definição.

Este documento possui caráter de governança arquitetural e serve como referência para evolução da solução, garantindo rastreabilidade entre as decisões de arquitetura e sua implementação.

## Escopo

Este documento contempla:

- decisões arquiteturais consolidadas;
- premissas estruturais do banco de dados;
- decisões de governança;
- questões funcionais ainda pendentes.

Este documento não define o modelo físico nem substitui os artefatos de implementação (DDL Oracle e scripts de evolução em `database/migrations/`).

---

# 2. Situação da Arquitetura

## Status Geral

| Item | Situação |
|------|----------|
| Modelo Conceitual | APPROVED — `02-conceptual-model.md` |
| Modelo Lógico | APPROVED — `02-logical-model.md` |
| Modelo Físico | APPROVED — `03-physical-model.md` v4.7 |
| Arquitetura Oracle | APPROVED |
| Administração do Schema (DBA) | APPROVED — DEC-DB-019 |
| Estratégia de Auditoria | APPROVED |
| Estratégia de Compartilhamento | APPROVED |
| Estratégia de Versionamento | APPROVED |

O modelo lógico está documentado em `02-logical-model.md`. O modelo físico em `03-physical-model.md` é a referência oficial da implementação e está sincronizado com `database/ddl/`.

## Situação Atual

Não existem riscos arquiteturais conhecidos que impeçam a implementação do banco de dados.

As pendências existentes são exclusivamente funcionais e poderão ser tratadas sem impacto na modelagem física.

---

# 3. Decisões Arquiteturais Consolidadas

| Código | Decisão | Situação |
|---------|---------|----------|
| DEC-DB-001 | Federação persistida como entidade própria | APPROVED |
| DEC-DB-002 | Hierarquia organizacional Federação → Singular → Área → Equipe | APPROVED |
| DEC-DB-003 | Versionamento documental obrigatório | APPROVED |
| DEC-DB-004 | Comunicado como entidade independente | APPROVED |
| DEC-DB-005 | Compartilhamento corporativo unificado | APPROVED |
| DEC-DB-006 | Herança de permissões entre pastas | APPROVED |
| DEC-DB-007 | Auditoria corporativa centralizada | APPROVED |
| DEC-DB-009 | Tabela `AUTH_SESSAO` para Refresh Token stateless (FT-AUTH) | APPROVED |
| DEC-DB-010 | Coluna `ID_ZIMBRA` em `COLABORADOR` para integração Zimbra | APPROVED |
| DEC-DB-011 | Ausência de `NUM_MATRICULA` na versão atual (identidade via `COD_COLABORADOR`, e-mail e Zimbra) | APPROVED |
| DEC-DB-012 | Federação e Singular — identidade institucional mínima | APPROVED |
| DEC-DB-013 | Endereços e contatos em `ENDERECO` e `CONTATO` com FK explícitas (sem polimorfismo) | APPROVED |
| DEC-DB-014 | Comunicação via `CONTATO`; liderança via `COLABORADOR` somente com caso de uso de múltiplos responsáveis | APPROVED |
| DEC-DB-015 | Refinamento final organizacional: `NOM_LOCAL`, `CONTATO` estendido a Área/Equipe, gestor/líder por FK, tipos sem CHECK rígido | APPROVED |
| DEC-DB-016 | Refinamento final de `COLABORADOR`: perfil intrínseco, `CONTATO` estendido a Colaborador, `COD_GESTOR` auto-referência, `ID_ZIMBRA` obrigatório | APPROVED |
| DEC-DB-017 | Adequação à nomenclatura corporativa Oracle Unimed Ceará — limite 30 caracteres e Glossário Oficial de Abreviações | APPROVED |
| DEC-DB-018 | Geração de PK via JPA/Sequences — sem `DEFAULT SQ_*.NEXTVAL` no DDL; `NEXTVAL` explícito apenas em scripts SQL | APPROVED |
| DEC-DB-019 | Schema Oracle administrado pelo DBA via baseline DDL oficial; Flyway não utilizado | APPROVED |
| DEC-DB-020 | `COLABORADOR` no baseline DDL mantém FKs organizacionais opcionais; JPA alinhado ao modelo físico (não ao legado Flyway V2) | APPROVED |
| DEC-DB-022 | Área em nível único — sem auto-referência; detalhamento operacional via Equipe | APPROVED |
| DEC-DB-023 | Testes de integração backend: Oracle único, `ddl-auto=validate`, sem limpeza automática na fase migração | APPROVED |
| DEC-DB-024 | Usuário de aplicação Oracle: `UNMPORTCOM` (owner) × `UNMPORTCOM_APP` (conexão) via `UNMPORTCOM_APP_ROLE` e `database/security/` | APPROVED |

---

## DEC-DB-001 — Estrutura Organizacional

### Decisão

A Federação será persistida como entidade própria, representando o nível máximo da estrutura organizacional.

---

## DEC-DB-002 — Hierarquia Organizacional

### Estrutura

```text
FEDERACAO
    └── SINGULAR
            └── AREA
                    └── EQUIPE
```

### Regras

- Todo colaborador pertence obrigatoriamente à Federação.
- Os vínculos com Singular, Área e Equipe dependem do contexto organizacional.

---

## DEC-DB-003 — Gestão Documental

### Estratégia

```text
DOCUMENTO
      1
      │
      └────── N
DOCUMENTO_VERSAO
      │
      N
      │
      1
ARQUIVO_BINARIO
```

### Regras

- Documento nunca é sobrescrito.
- Toda alteração gera nova versão.
- O histórico é imutável.
- Apenas metadados permanecem no Oracle.
- Arquivos binários são armazenados externamente.

---

## DEC-DB-004 — Comunicação

### Decisão

A entidade COMUNICADO possui persistência própria e permanece independente da gestão documental.

---

## DEC-DB-005 — Compartilhamento

### Estratégia

A entidade COMPARTILHAMENTO centraliza o compartilhamento corporativo de:

- Documentos
- Pastas
- Comunicados

Escopos suportados:

- Federação
- Singular
- Área
- Equipe
- Colaborador

Permissões:

- Leitura
- Download
- Edição
- Administração

A integridade referencial do modelo polimórfico é garantida pela camada de domínio.

---

## DEC-DB-006 — Permissões

A herança de permissões entre pastas é controlada por:

```text
FLG_HERDA_PERMISSAO
```

Apenas permissões explícitas são persistidas.

---

## DEC-DB-007 — Auditoria

A entidade REGISTRO_AUDITORIA centraliza a auditoria corporativa.

Características:

- retenção mínima de cinco anos;
- preparada para particionamento temporal;
- auditoria das principais entidades de negócio.

---

## DEC-DB-008 — Geração de Identificadores

O Oracle Database é responsável pela geração das chaves primárias.

Todas as entidades utilizam:

- Sequence Oracle dedicada;

Essa estratégia garante consistência entre scripts DDL, cargas manuais e aplicação.

---

## DEC-DB-011 — Identidade do Colaborador sem Matrícula

### Decisão

O Portal de Comunicação **não utiliza número de matrícula corporativa** (`NUM_MATRICULA`) na versão atual.

A identidade do colaborador é composta por:

- identificador interno (`COD_COLABORADOR`);
- e-mail institucional (`DES_EMAIL`);
- identificador Zimbra (`ID_ZIMBRA`), quando disponível.

### Justificativa

- Decisão YAGNI alinhada ao fluxo FT-AUTH (autenticação Zimbra e criação automática no login).
- Integração futura com RH ou sistema corporativo poderá reintroduzir `NUM_MATRICULA` via migração dedicada.

### Status

**APPROVED** (2026-07-10)

---

## DEC-DB-012 — Federação e Singular: Identidade Institucional Mínima

### Decisão

`FEDERACAO` permanece como raiz organizacional com **identidade institucional estável** apenas: nome, sigla, código Unimed, registro ANS, site, descrição e auditoria.

`SINGULAR` permanece com identidade da cooperativa filiada: nome, sigla, código Unimed e vínculo à federação.

**Não criar** nesta versão:

| Avaliado | Decisão | Motivo |
|----------|---------|--------|
| `ENDERECO_FEDERACAO` | Rejeitado | Nenhuma Feature aprovada exige endereços persistidos no Oracle |
| Entidade de contatos (SAC, vendas, ouvidoria) | Rejeitado | Requisito institucional sem Feature no escopo atual; WordPress cobre conteúdo complementar |
| Entidade de redes sociais | Rejeitado | Sem necessidade dinâmica documentada |
| Colunas de redes sociais em `FEDERACAO` | Rejeitado | YAGNI — adicionar quando houver Feature |
| Logo em `FEDERACAO` | Rejeitado | Reutilizar `CONFIGURACAO_PORTAL.URL_LOGO` e `URL_FAVICON` |
| Diretoria executiva (entidade ou normalização) | Rejeitado | Sem caso de uso de mandatos/histórico; exibição futura via WordPress ou campo textual sob demanda |

### Justificativa

- Portal de Comunicação é focado em organização operacional, documentos e autenticação — não CMS institucional.
- WordPress permanece desacoplado para conteúdo institucional complementar.
- Separação clara: `FEDERACAO` (identidade) × `CONFIGURACAO_PORTAL` (parâmetros do portal).

### Status

**APPROVED** (2026-07-10)

> **Nota:** Endereços e contatos foram posteriormente introduzidos por **DEC-DB-013**, refinando as rejeições de `ENDERECO_FEDERACAO` e entidade de contatos desta decisão.

---

## DEC-DB-013 — Endereço e Contato Institucionais

### Decisão

Introduzir duas entidades reutilizáveis com responsabilidade única:

- **`ENDERECO`** — localizações físicas
- **`CONTATO`** — canais de comunicação (telefone, SAC, ouvidoria, WhatsApp, e-mail institucional)

Cada registro pertence a **exatamente uma** organização: `FEDERACAO` **ou** `SINGULAR`, via FKs nullable mutuamente exclusivas (`CK_*_PROPRIETARIO`).

### Rejeitado

- Tabelas polimórficas (`TIP_ENTIDADE` / `COD_ENTIDADE`)
- Tabelas de proprietário genérico
- Entidades separadas por tipo de contato ou por canal
- Colunas de endereço/contato embutidas em `FEDERACAO` ou `SINGULAR`

### Justificativa

- Elimina duplicação futura entre federação e singulares mantendo baixo acoplamento.
- Integridade referencial explícita (FK) sem framework genérico.
- Dois tipos de dado distintos (localização × comunicação) justificam duas entidades — não uma abstração unificada.

### Status

**APPROVED** (2026-07-10)

---

## DEC-DB-014 — Comunicação e Liderança sem Proliferação de Entidades

### Decisão

Em vez de criar entidades separadas para `EMAIL`, `GESTOR` e `LÍDER`:

- reutilizar **`CONTATO`** para os meios de comunicação (telefone, e-mail, WhatsApp etc.);
- representar gestores/líderes por relacionamento com **`COLABORADOR`** somente se houver um caso de uso que exija **múltiplos responsáveis simultâneos**.

Isso mantém o modelo enxuto e evita proliferar entidades que representam apenas um único atributo.

### Rejeitado

- Entidades `EMAIL`, `GESTOR`, `LÍDER` ou equivalentes de atributo único
- Normalização prematura de liderança sem Feature aprovada

### Status

**APPROVED** (2026-07-10)

---

## DEC-DB-015 — Refinamento Final do Modelo Organizacional

### Decisão

| Item | Decisão |
|------|---------|
| `ENDERECO.NOM_LOCAL` | Adicionado — identificador legível do local (complementa `TIP_ENDERECO`) |
| `TIP_ENDERECO` / `TIP_CONTATO` | Mantidos sem CHECK rígido — validação na aplicação |
| `CONTATO` em Área/Equipe | `COD_AREA` e `COD_EQUIPE` com XOR de proprietário estendido |
| Gestor de área | `AREA.COD_GESTOR` → `COLABORADOR` (FK direta, único) |
| Líder de equipe | `EQUIPE.COD_LIDER` → `COLABORADOR` (FK direta, único) |
| Membros de equipe | `COLABORADOR.COD_EQUIPE` existente — sem nova entidade |
| Canais por entidade | Reutilizar `CONTATO` — sem `EMAIL_*`, `TELEFONE_*`, `GESTOR_*`, `LIDER_*` |

### Rejeitado

- Entidades `EMAIL`, `PHONE`, `WHATSAPP`, `GESTOR`, `LIDER`
- Tabelas de associação para gestor/líder (sem Feature de múltiplos responsáveis)
- Lookup tables / ENUM para tipos de endereço e contato
- Endereços embutidos em `CONTATO`

### Status

**APPROVED** (2026-07-10) — **congelamento do modelo organizacional para DDL**

---

## DEC-DB-016 — Refinamento Final do Domínio COLABORADOR

### Decisão

| Item | Decisão |
|------|---------|
| `CONTATO.COD_COLABORADOR` | Estende propriedade de contato ao colaborador — XOR de 5 proprietários |
| Canais do colaborador | Telefone, celular, ramal, WhatsApp, e-mails adicionais exclusivamente em `CONTATO` |
| `DES_EMAIL` em `COLABORADOR` | Mantido como e-mail de identidade/login (FT-AUTH/DEC-DB-011) — não duplica canal de contato |
| `ID_ZIMBRA` | Obrigatório e único — identidade Zimbra (FT-AUTH) |
| `COD_GESTOR` | FK auto-referência em `COLABORADOR` — gestor direto único |
| `DES_BIOGRAFIA` | `VARCHAR2(4000)` nullable |
| `DAT_NASCIMENTO` / `DAT_CONTRATACAO` | `TIMESTAMP(6)` nullable |
| Vínculo organizacional | `COD_SINGULAR`, `COD_AREA` e `COD_EQUIPE` existentes — sem alteração estrutural |

### Rejeitado

- Entidade `CARGO` (sem Feature de gestão de cargos, hierarquia de cargos ou integração RH)
- Entidades `EMAIL`, `TELEFONE`, `CELULAR`, `RAMAL`, `WHATSAPP`
- Entidades `GESTOR`, `HIERARQUIA`, `ORGANOGRAMA`
- Atributos de comunicação duplicados em `COLABORADOR`
- Coluna derivada de aniversário de contratação
- Propriedade polimórfica de contato

### Status

**APPROVED** (2026-07-10) — **congelamento do domínio COLABORADOR para DDL**

---

## DEC-DB-020 — Alinhamento COLABORADOR (JPA, DDL e domínio)

### Contexto

Durante a validação FT-AUTH (VAL-DB-01/02) ocorreu `ORA-00904: "COD_SINGULAR": invalid identifier` ao persistir colaborador no login. Surgiu a hipótese de que colunas organizacionais teriam sido removidas do modelo em favor de `PAPEL_ATRIBUICAO`.

### Decisão

| Item | Decisão |
|------|---------|
| Fonte de verdade do schema físico | `database/ddl/` (baseline) + `database/model/03-physical-model.md` (DEC-DB-019) |
| Colunas em `COLABORADOR` | `COD_FEDERACAO` (obrigatório), `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE`, `COD_GESTOR` (opcionais — contexto administrativo FT-COLABORADOR) |
| `PAPEL_ATRIBUICAO` | Define **escopo de autorização** (o que o colaborador pode fazer); **não substitui** as FKs organizacionais em `COLABORADOR` para cadastro e filtros administrativos |
| Login FT-AUTH (`locateOrCreate`) | Pode criar colaborador apenas com `COD_FEDERACAO`, identidade e `ID_ZIMBRA`; FKs organizacionais permanecem **NULL** até onboarding/admin |
| Entidade JPA `ColaboradorEntity` | Deve refletir o baseline DDL; **não** reverter para o `COLABORADOR` mínimo do legado Flyway `V2__access_control.sql` |
| Oracle sem `COD_SINGULAR` | Schema **incompleto** em relação ao baseline — corrigir via DBA (`000-install.sql` greenfield ou `migrations/V004__colaborador_corporate_columns.sql` em brownfield) |

### Rejeitado

- Remover `COD_SINGULAR` / `COD_AREA` / `COD_EQUIPE` / `COD_GESTOR` da entidade JPA apenas para contornar `ORA-00904`
- Tratar scripts Flyway legados (`backend/.../V2__access_control.sql`) como referência de schema
- Evolução estrutural via Flyway na aplicação

### Status

**APPROVED** (2026-07-20) — governança VAL-DB-02

---

## DEC-DB-017 — Adequação à Nomenclatura Corporativa Oracle

### Decisão

| Item | Decisão |
|------|---------|
| Referência | Padrão para Nomenclatura de Banco de Dados Oracle (Unimed Ceará) |
| Limite Oracle 11g | 30 caracteres para todo identificador |
| Truncamento | Glossário **somente** quando identificador > 30 caracteres |
| Revisão | `SQ_COLABORADOR_COD_COLABORADOR` restaurada — abreviação desnecessária revertida |
| Escopo | Sequences, FKs, UKs e índices que excediam 30 caracteres |
| Tabelas/colunas | Mantidas — sem alteração de regras de negócio |
| Views/Procedures/Functions/Packages | Não existem no baseline — prefixos `VW_`, `PR_`, `FC_`, `PKG_` documentados para evolução |

### Renomeações aplicadas

Ver relatório `database/reports/report-oracle-naming-compliance.md`.

### Status

**APPROVED** (2026-07-10)

---

## DEC-DB-018 — Geração de Identificadores via JPA

### Decisão

| Item | Decisão |
|------|---------|
| DDL | Sem `DEFAULT SQ_*.NEXTVAL` nas colunas PK |
| Aplicação | Hibernate/JPA com `@SequenceGenerator` + `GenerationType.SEQUENCE` |
| Scripts SQL | `SQ_<TABELA>_<CAMPO>.NEXTVAL` explícito em `INSERT` |
| `008-initial-data.sql` | PKs informadas via `.NEXTVAL` |
| Modelo físico | Coluna Default das PKs = `-`; sequence documentada na seção da entidade |

### Status

**APPROVED** (2026-07-10)

---

## DEC-DB-019 — Administração do Schema pelo DBA

### Contexto

O projeto adota baseline DDL corporativo em `database/ddl/`, executado pelo DBA. Referências anteriores indicavam Flyway como mecanismo de versionamento ou evolução do schema Oracle na aplicação Spring Boot.

### Decisão

O schema Oracle é administrado pelo DBA através do baseline DDL oficial do projeto. A aplicação pressupõe um schema previamente criado. Flyway não é utilizado para criação, gerenciamento nem evolução estrutural do banco.

### Consequências

- Baseline oficial: `database/ddl/` (`000-install.sql` … `901-validation.sql`)
- Evoluções estruturais: scripts em `database/migrations/`, executados pelo DBA
- Aplicação: `ddl-auto: none`; sem migração automática na inicialização
- Dependências Flyway no backend devem ser removidas (recomendação — pendente implementação)

---

## DEC-DB-023 — Estratégia de Banco para Testes de Integração (Backend)

### Contexto

Os testes de integração (`@IntegrationTest`, perfil `test`) validam persistência, transações e APIs sobre o mesmo modelo físico governado pelo DBA. Durante a migração, o Oracle de teste/homologação já contém dados institucionais que devem ser preservados.

### Decisão

| Item | Decisão |
|------|---------|
| Banco | **Exclusivamente Oracle** provisionado pelo DBA (`SPRING_DATASOURCE_*`) |
| Fonte estrutural | DDL oficial em `database/ddl/` — aplicação **não** cria, altera nem remove objetos |
| Hibernate (perfil `test`) | `spring.jpa.hibernate.ddl-auto: validate` — apenas verifica compatibilidade JPA × schema |
| Proibido em testes de integração | H2, Testcontainers (nesta fase), scripts DDL paralelos, `create` / `update` / `create-drop` |
| Dados na fase migração | Reutilizar dados existentes; **sem** limpeza automática global |
| `IntegrationTestDatabaseCleaner` | Mantido no código; **desligado** por padrão (`application.persistence.integration-test-cleanup.enabled=false`) |
| Categorias de teste | Leitura (prioridade na migração) vs mutação (documentadas; isolamento seletivo futuro) |

### Consequências

- `backend/src/test/resources/application-test.yaml` aponta para Oracle e `validate`.
- `@IntegrationTest` não registra listener de limpeza; reativação futura via `@EnableIntegrationTestDatabaseCleanup`.
- Catálogo de suítes: `docs/implementation/13-integration-test-database-strategy.md`.
- Testes slice (`pf-*`) sem JPA permanecem sem datasource.
- Evolução pós-migração: limpeza seletiva de dados de teste, se necessário.

### Status

**APPROVED** (2026-07-21)

---

# 4. Premissas Arquiteturais

## Organização

- Existe apenas uma Federação.
- Toda Singular pertence à Federação.
- Endereços e contatos institucionais em `ENDERECO` e `CONTATO` (DEC-DB-013).
- Canais de comunicação institucional exclusivamente em `CONTATO` (DEC-DB-014).
- Gestor de área: `AREA.COD_GESTOR` → `COLABORADOR` (DEC-DB-015).
- Líder de equipe: `EQUIPE.COD_LIDER` → `COLABORADOR` (DEC-DB-015).
- Contatos de área/equipe/colaborador: `CONTATO.COD_AREA` / `CONTATO.COD_EQUIPE` / `CONTATO.COD_COLABORADOR` (DEC-DB-015/016).
- Toda Área pertence a uma Singular.
- Toda Equipe pertence a uma Área.

## Colaboradores

- Todo colaborador pertence obrigatoriamente à Federação.
- O escopo de atuação é determinado por PAPEL_ATRIBUICAO.
- Identidade: `COD_COLABORADOR`, `DES_EMAIL` (login FT-AUTH) e `ID_ZIMBRA` (obrigatório). Sem `NUM_MATRICULA` (DEC-DB-011).
- Gestor direto: `COLABORADOR.COD_GESTOR` → `COLABORADOR` (DEC-DB-016).
- Canais de comunicação do colaborador: `CONTATO.COD_COLABORADOR` (DEC-DB-016).

## Autenticação (FT-AUTH)

- Sessões persistidas em `AUTH_SESSAO` — hash do Refresh Token, revogação e `session_id`.
- Access Token (JWT) não é persistido no banco.

## Gestão Documental

- Todo documento possui versionamento obrigatório.
- Apenas uma versão pode ser considerada atual.
- Arquivos binários permanecem fora do Oracle.

## Compartilhamento

- Modelo polimórfico.
- Compartilhamento por escopo organizacional.
- Apenas permissões explícitas são persistidas.

## Auditoria

- Auditoria centralizada.
- Retenção mínima de cinco anos.

## Persistência

- Baseline estrutural definido pela DDL oficial em `database/ddl/`.
- Evoluções pós-baseline via scripts em `database/migrations/`, executados pelo DBA (DEC-DB-019).
- Nomenclatura Oracle conforme padrão corporativo Unimed Ceará — limite 30 caracteres (DEC-DB-017).
- Geração de PK via JPA/Sequences — sem `DEFAULT` no DDL (DEC-DB-018).

---

# 5. Questões Funcionais em Aberto

## OQ-001 — Processo de Onboarding

### Situação

A entidade ONBOARDING_SOLICITACAO está definida.

O fluxo operacional permanece pendente de validação funcional.

### Impacto

- APIs
- Fluxo operacional
- Estados do processo

### Prioridade

Alta

---

## OQ-002 — Usuário Convidado

### Situação

Necessária definição da estratégia para usuários convidados.

### Recomendação

Utilizar o papel organizacional `CONVIDADO`, evitando a criação de uma nova entidade.

### Prioridade

Alta

---

# 6. Resumo Executivo

## Situação

A arquitetura do banco de dados encontra-se consolidada.

Todas as decisões estruturais necessárias para implementação foram aprovadas.

As questões pendentes são exclusivamente funcionais e não impedem a implementação da camada de persistência.

---

# 7. Próxima Etapa

A implementação deverá utilizar como referência:

- `database/model/02-conceptual-model.md`;
- `database/model/02-logical-model.md`;
- `database/model/03-physical-model.md`;
- decisões arquiteturais deste documento;
- `database/ddl/` (baseline oficial);
- `database/migrations/` (evoluções estruturais pós-baseline — execução DBA).

```text
database/ddl/
├── 000-install.sql
├── 001-create-users.sql
├── 002-create-sequences.sql
├── 003-create-tables.sql
├── 004-create-constraints.sql
├── 005-create-indexes.sql
├── 006-create-comments.sql
├── 007-create-grants.sql
├── 008-initial-data.sql
├── 009-configuracao-portal.sql
├── 900-drop-all.sql
├── 901-validation.sql
└── 902-compile-invalid-objects.sql
```

---

---

## DEC-DB-021 — Organização raiz e hierarquia institucional

### Decisão

A entidade **`FEDERACAO`** é a **única organização raiz** do domínio Organização Corporativa. Não criar entidade paralela (ex.: “Organização”, “Instituição”, “Tenant”).

Hierarquia oficial de persistência:

```text
Federação
    ↓
Singulares
    ↓
Áreas
    ↓
Equipes
    ↓
Colaboradores
```

| Conceito de negócio | Entidade física | Chave institucional (negócio) | Chave técnica (aplicação) |
|---------------------|-----------------|----------------------------------|---------------------------|
| Federação (Unimed Ceará) | `FEDERACAO` | `COD_UNIMED` = **979** | `COD_FEDERACAO` (surrogate) |
| Singular | `SINGULAR` | `COD_UNIMED` (BR-014) | `COD_SINGULAR` + `COD_FEDERACAO` FK |
| Área | `AREA` | `SIG_AREA` no escopo do proprietário | `COD_AREA` + FK singular ou federação |
| Equipe | `EQUIPE` | nome no escopo da área | `COD_EQUIPE` + `COD_AREA` FK |
| Colaborador | `COLABORADOR` | e-mail / Zimbra | `COD_COLABORADOR` + `COD_FEDERACAO` obrigatório |

Primeiro registro institucional da Federação (`dml/001-federacao.sql`):

| Atributo | Valor oficial |
|----------|----------------|
| Nome (`NOM_FEDERACAO`) | Unimed Ceará |
| Código Unimed (`COD_UNIMED`) | 979 (atributo de cadastro — **não** é chave de referência nos DMLs 002–006) |
| Classificação | Federação (entidade `FEDERACAO`) |
| Situação | Ativa (`FLG_ATIVO` = `S`) |

Scripts numerados em `database/dml/`:

| Ordem | Arquivo |
|-------|---------|
| 1 | `001-federacao.sql` |
| 2 | `002-singulares.sql` |
| 3 | `003-areas.sql` |
| 4 | `004-equipes.sql` |
| 5 | `005-colaboradores.sql` (sem carga versionada) |
| 6 | `006-homologacao-opcional.sql` |

### Regras de referência

- O **primeiro INSERT institucional** do Portal é exclusivamente a Federação em `001-federacao.sql`.
- Scripts `002`–`006` resolvem vínculos apenas via **`COD_FEDERACAO`** (subconsulta à Federação raiz ativa). Não usar `COD_UNIMED` como chave técnica de referência.
- `application.auth.default-federation-id` = `COD_FEDERACAO` obtido após `001` (ex.: `SELECT COD_FEDERACAO FROM FEDERACAO WHERE NOM_FEDERACAO = 'Unimed Ceará' AND FLG_ATIVO = 'S'`).
- `002`–`004` e `006` não fazem parte de `000-install.sql` até aprovação da fase de carga institucional complementar; `001` integra o install após `008-initial-data.sql`.

### Justificativa

- Alinha domínio (`docs/domain/`), modelo físico (DEC-DB-012) e requisito de negócio (Unimed Ceará Federação, código 979).
- Evita duplicação de raiz organizacional e relacionamentos implícitos.

### Status

**APPROVED** (2026-07-21)

---

## DEC-DB-022 — Área em nível único

### Decisão

A entidade **AREA** não possui hierarquia entre áreas (sem `COD_AREA_PAI`). Cada área é unidade departamental de **nível único** dentro da Singular ou da Federação.

O detalhamento operacional abaixo da área é exclusivamente modelado por **EQUIPE**.

### Estrutura (inalterada em relação a DEC-DB-002)

```text
Federação → Singular → Área → Equipe → Colaborador
```

### Impacto

- Remoção de coluna, FK e índice de auto-referência no baseline DDL.
- Contrato FT-AREA e API sem `parentAreaId`.
- Regras RN-AREA-004 e RN-AREA-005 (hierarquia entre áreas) revogadas na especificação.

### Status

**APPROVED** (2026-07-21)

---

| Versão | 4.7 |
|--------|-----|
| Status | APPROVED |