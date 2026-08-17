# Oracle / DDL / JPA Reconciliation — Etapa 4

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Etapa | 4 — Reconciliação Oracle / DDL / JPA |
| Data | 2026-08-13 |
| Executor | Agente (reconciliação estática) |
| Status | **ETAPA 4 — CONCLUÍDA** |

---

## 1. Objetivo

Determinar e classificar divergências entre Oracle real, DDL/documentação do banco, mapeamentos JPA/Hibernate e contratos de domínio, estabelecendo a fonte de verdade (SSOT) e o drift existente **antes** de qualquer correção em entidades JPA.

Esta execução priorizou evidência documental e comparação estática código × DDL. A validação runtime (Oracle homolog + Hibernate `ddl-auto=validate`) permanece bloqueada por ausência de acesso Oracle neste ambiente.

---

## 2. Escopo

### Incluído

- Inventário SSOT `database/` (baseline, validation, ddl, migrations, security)
- Inventário das 6 entidades JPA mapeadas ao schema `UNMPORTCOM`
- Comparação estática JPA × DDL (`003-create-tables.sql`, `002-create-sequences.sql`, `004-create-constraints.sql`)
- Revalidação de achados históricos (INFRA-BE-03, auditoria 25+5 divergências)
- Classificação de grants documentados vs objetos usados pelo backend
- Busca classificada por anti-padrões (Flyway, PostgreSQL, H2, validação Hibernate)
- Critérios de desbloqueio para conclusão futura

### Excluído (conforme restrições da etapa)

- Alteração do Oracle real (produção ou homolog)
- Criação de entidades JPA para as 17 tabelas sem mapeamento (escopo de Features)
- Implementação de features
- Correções de infraestrutura local (Etapa 5): `docker-compose.yml`, Oracle XE local
- Execução de DDL destrutivo ou DML

---

## 3. Modelo de acesso Oracle

Modelo oficial (DEC-DB-024):

```text
UNMPORTCOM (schema owner — DDL, migrations, DBA)
        │
        │ GRANT SELECT, INSERT, UPDATE, DELETE (tabelas)
        │ GRANT SELECT (sequences)
        ▼
UNMPORTCOM_APP_ROLE
        │
        │ GRANT ROLE TO user
        ▼
UNMPORTCOM_APP  ← único usuário JDBC do backend
```

| Papel | Usuário/objeto | Uso |
|-------|----------------|-----|
| Schema owner | `UNMPORTCOM` | Objetos físicos; **proibido** como `spring.datasource.username` |
| Application user | `UNMPORTCOM_APP` | Conexão Spring Boot / Hibernate / testes `@IntegrationTest` |
| Role corporativa | `UNMPORTCOM_APP_ROLE` | Agregação de privilégios DML + SELECT em sequences |

Configuração backend:

| Item | Valor | Fonte |
|------|-------|-------|
| `spring.datasource.username` | `${SPRING_DATASOURCE_USERNAME}` → `UNMPORTCOM_APP` | `.env.example`, `application.yaml` |
| `hibernate.default_schema` | `UNMPORTCOM` | `application.yaml`, `@Table(schema="UNMPORTCOM")` |
| `connection-init-sql` | `ALTER SESSION SET CURRENT_SCHEMA = UNMPORTCOM` | `application.yaml`, `application-test.yaml` |
| Runtime `ddl-auto` | `none` | `application.yaml` (DEC-DB-019) |
| Teste `ddl-auto` | `validate` | `application-test.yaml` (DEC-DB-023) |

---

## 4. Evidências utilizadas

| # | Evidência | Tipo | Confiabilidade |
|---|-----------|------|----------------|
| E1 | `database/baseline/oracle-baseline-2026-07-22.md` | SSOT físico homologado | Alta (documental) |
| E2 | `database/validation/oracle-schema-validation-2026-07-22.md` | Inspeção Oracle DB-SYNC-99 | Alta (histórica) |
| E3 | `database/reports/sync-report-2026-07-22.md` | Encerramento Oracle × DDL | Alta (histórica) |
| E4 | `database/ddl/000-install.sql` … `901-validation.sql` | Implementação greenfield | Alta (versionada) |
| E5 | `database/security/grants/` + `V900`/`V902` | Grants documentados | Alta (documental) |
| E6 | 6 entidades JPA em `backend/src/main/java/.../entity/` | Código atual | Alta |
| E7 | `database/reports/infra-be-03-jpa-schema-alignment.md` | Correções JPA 2026-07-23 | Média (histórica) |
| E8 | `SchemaOracleAuditTest.java` | Ferramenta de auditoria runtime | Não executada |
| E9 | Consultas `ALL_*` em Oracle homolog/dev | Evidência live | **Ausente** |

**Lacuna crítica:** sem `.env` com `SPRING_DATASOURCE_*` apontando para Oracle autorizado, não foi possível coletar E9 nem executar validação Hibernate em runtime.

---

## 5. SSOT do banco

Hierarquia oficial (`database/GOVERNANCE.md`):

| Prioridade | Camada | Artefato |
|:----------:|--------|----------|
| 1 | Baseline física homologada | `database/baseline/oracle-baseline-2026-07-22.md` |
| 2 | Evidências de validação Oracle | `database/validation/oracle-schema-validation-2026-07-22.md` |
| 3 | Scripts DDL versionados | `database/ddl/` |
| 4 | Migrações brownfield | `database/migrations/` (V003–V007; **não** Flyway) |
| 5 | Documentação complementar | `database/model/`, `database/reports/` |

**Conclusão SSOT:** para agentes sem Oracle, prevalece a baseline homologada 2026-07-22, implementada por `database/ddl/`. Migrations são histórico brownfield; greenfield usa apenas `000-install.sql`.

**Oracle real** é a autoridade final quando acessível; nesta execução não foi consultado. Estado documental indica sincronização Oracle × DDL encerrada (DB-SYNC-99).

---

## 6. Inventário Oracle

Fonte: baseline + validation 2026-07-22 (evidência indireta; **não revalidado live**).

| Objeto | Quantidade homologada |
|--------|----------------------:|
| Tabelas | 23 |
| Primary Keys | 23 |
| Foreign Keys | 36 |
| UNIQUE | 11 |
| CHECK | 172 |
| Índices | 95 |
| Sequences | 12 |
| Triggers | 0 |
| Views | 0 |
| Synonyms | 0 |

### Tabelas (23)

| # | Tabela | Entidade JPA |
|---|--------|:------------:|
| 1 | FEDERACAO | Sim |
| 2 | SINGULAR | Sim |
| 3 | ENDERECO | Não |
| 4 | CONTATO | Não |
| 5 | AREA | Sim |
| 6 | EQUIPE | Sim |
| 7 | COLABORADOR | Sim |
| 8 | ONBOARDING_SOLICITACAO | Não |
| 9 | CATEGORIA_DOCUMENTAL | Não |
| 10 | PASTA | Não |
| 11 | DOCUMENTO | Não |
| 12 | ARQUIVO_BINARIO | Não |
| 13 | DOCUMENTO_VERSAO | Não |
| 14 | COMPARTILHAMENTO | Não |
| 15 | AUTH_SESSAO | Sim |
| 16 | PAPEL | Não |
| 17 | PAPEL_ATRIBUICAO | Não |
| 18 | PERMISSAO_PASTA | Não |
| 19 | SOLICITACAO_PERMISSAO | Não |
| 20 | REGISTRO_AUDITORIA | Não |
| 21 | COMUNICADO | Não |
| 22 | NOTIFICACAO | Não |
| 23 | CONFIGURACAO_PORTAL | Não |

### Sequences (12)

`SQ_FEDERACAO_COD_FEDERACAO`, `SQ_SINGULAR_COD_SINGULAR`, `SQ_AREA_COD_AREA`, `SQ_EQUIPE_COD_EQUIPE`, `SQ_COLABORADOR`, `SQ_ONBOARD_SOLIC`, `SQ_DOCUMENTO_COD_DOCUMENTO`, `SQ_REG_AUDIT_COD_REG_AUDIT`, `SQ_AUTH_SESSAO`, `SQ_COMUNICADO_COD_COMUNICADO`, `SQ_NOTIFICACAO_COD_NOTIFICACAO`, `SQ_CONFIG_PORT_COD_CONFIG_PORT`.

---

## 7. Inventário DDL

### Greenfield (oficial)

| Script | Conteúdo |
|--------|----------|
| `001-create-users.sql` | `UNMPORTCOM`, `UNMPORTCOM_APP`, `UNMPORTCOM_APP_ROLE` |
| `002-create-sequences.sql` | 12 sequences |
| `003-create-tables.sql` | 23 tabelas |
| `004-create-constraints.sql` | PK, FK, UK, CHECK |
| `005-create-indexes.sql` | 95 índices |
| `006-create-comments.sql` | Comentários |
| `007-create-grants.sql` | Espelha `database/security/grants/` |
| `901-validation.sql` | Contagens pós-instalação |

**Verificação nominal:** 23 `CREATE TABLE` em `003` e 12 `CREATE SEQUENCE` em `002` coincidem com baseline § Inventário.

### Brownfield (histórico, não SSOT greenfield)

| Script | Propósito |
|--------|-----------|
| `V003__auth_sessao_and_colaborador_zimbra.sql` | Evolução legada pré-homologação |
| `V004__colaborador_corporate_columns.sql` | Alinha brownfield ao baseline COLABORADOR |
| `V006__drop_auth_sessao_organizational_context.sql` | Remove colunas `COD_*_CTX` de AUTH_SESSAO |
| `V007__colaborador_ssot_alignment.sql` | Remove `DES_CARGO`/`NUM_CPF`; alinha nomes |

### Divergência baseline ↔ DDL

Nenhuma divergência nominal identificada na comparação estática (pós DB-SYNC-99).

---

## 8. Inventário JPA

| Entidade | Tabela | Sequence JPA | Sequence DDL | Colunas mapeadas |
|----------|--------|--------------|--------------|------------------:|
| `FederacaoEntity` | FEDERACAO | SQ_FEDERACAO_COD_FEDERACAO | Idem | 10 |
| `SingularEntity` | SINGULAR | SQ_SINGULAR_COD_SINGULAR | Idem | 9 |
| `AreaEntity` | AREA | SQ_AREA_COD_AREA | Idem | 9 |
| `EquipeEntity` | EQUIPE | SQ_EQUIPE_COD_EQUIPE | Idem | 8 |
| `ColaboradorEntity` | COLABORADOR | SQ_COLABORADOR | Idem | 16 |
| `AuthSessaoEntity` | AUTH_SESSAO | SQ_AUTH_SESSAO | Idem | 10 (+ `@ManyToOne` colaborador) |

Repositórios Spring Data existem para as 6 entidades. Nenhuma outra `@Entity` em `src/main`.

### Comparação estática JPA × DDL (6 tabelas)

| Tabela.Coluna | DDL | JPA | Status |
|---------------|-----|-----|--------|
| FEDERACAO.COD_UNIMED | NUMBER(3) NOT NULL | Integer precision=3 | Alinhado |
| FEDERACAO.DSC_FEDERACAO | CLOB | @Lob String | Alinhado (validar runtime Hibernate) |
| SINGULAR.COD_UNIMED | NUMBER(3) NOT NULL | Integer precision=3 | Alinhado |
| SINGULAR.NUM_REGISTRO_ANS | VARCHAR2(20) NOT NULL | String length=20 | Alinhado |
| COLABORADOR.DES_BIOGRAFIA | VARCHAR2(4000) | String length=4000 (sem @Lob) | Alinhado |
| COLABORADOR.ID_ZIMBRA | VARCHAR2(255) NOT NULL | String length=255 | Alinhado |
| AREA/EQUIPE.DSC_* | CLOB | @Lob String | Alinhado |
| *_FLG_* | CHAR(1) | @JdbcTypeCode(CHAR) String length=1 | Alinhado |
| DAT_* | TIMESTAMP(6) | Instant | Alinhado (validar runtime) |
| AUTH_SESSAO.COD_COLABORADOR | FK → COLABORADOR | @ManyToOne @JoinColumn | Alinhado |
| AUTH_SESSAO.ID_SESSAO | UK | unique=true | Alinhado |
| AUTH_SESSAO.HASH_REFRESH_TOKEN | UK | unique=true | Alinhado |

**Nenhuma divergência estática JPA × DDL** identificada nas 6 entidades após INFRA-BE-03.

---

## 9. Matriz de drift

Legenda de classificação: ver § critérios da etapa.

### 9.1 Entidades mapeadas (JPA × DDL × Oracle documental)

| Objeto | Oracle (doc) | DDL | JPA | Divergência | SSOT | Ação |
|--------|--------------|-----|-----|-------------|------|------|
| FEDERACAO | Baseline | 003 | FederacaoEntity | — | Oracle/DDL | Nenhuma (estático) |
| SINGULAR | Baseline | 003 | SingularEntity | — | Oracle/DDL | Nenhuma (estático) |
| AREA | Baseline | 003 | AreaEntity | — | Oracle/DDL | Nenhuma (estático) |
| EQUIPE | Baseline | 003 | EquipeEntity | — | Oracle/DDL | Nenhuma (estático) |
| COLABORADOR | Baseline | 003 | ColaboradorEntity | — | Oracle/DDL | Nenhuma (estático) |
| AUTH_SESSAO | Baseline | 003 | AuthSessaoEntity | — | Oracle/DDL | Nenhuma (estático) |
| SQ_* (6 usadas) | 12 seq. baseline | 002 | @SequenceGenerator | — | Oracle/DDL | Nenhuma (estático) |

### 9.2 Tabelas sem entidade JPA (17)

| Objeto | Oracle (doc) | DDL | JPA | Divergência | SSOT | Ação |
|--------|--------------|-----|-----|-------------|------|------|
| ENDERECO … CONFIGURACAO_PORTAL (17) | Existe | 003 | Ausente | DOCUMENTATION_GAP | N/A (Feature) | Mapear na Feature correspondente; **não** criar entidade nesta etapa |

### 9.3 Achados históricos revalidados

| Item histórico | Estado atual (estático) | Classificação |
|----------------|-------------------------|---------------|
| 25 tabelas/colunas ausentes | 17 tabelas sem JPA por design; colunas das 6 entidades presentes no DDL | Reclassificado — não é drift Oracle×DDL |
| 5 sequences ausentes | Sequences de tabelas sem entidade JPA | Reclassificado — escopo Feature |
| SINGULAR.COD_UNIMED tipo | Integer precision=3 | **Resolvido** (INFRA-BE-03) |
| DES_BIOGRAFIA CLOB | VARCHAR2(4000) em DDL e JPA | **Resolvido** — achado histórico obsoleto |
| Sequences SQ_COLABORADOR_COD_* / SQ_AUTH_SESSAO_COD_* | Nomes corrigidos para SQ_COLABORADOR / SQ_AUTH_SESSAO | **Resolvido** |
| FEDERACAO missing table (Hibernate) | Causa: grants (DEC-DB-024) | ACCESS_GRANT_GAP — revalidar live |
| Conexão como UNMPORTCOM owner | Config usa UNMPORTCOM_APP | **Resolvido** em código ativo |
| Grants UNMPORTCOM_APP efetivos | Scripts SSOT completos | ACCESS_GRANT_GAP — evidência live ausente |

### 9.4 Specs vs implementação

| Objeto | Spec | JPA/API | Divergência | SSOT | Ação |
|--------|------|---------|-------------|------|------|
| codigoUnimed | `specs/features/singular/api.md` — String, máx. 20 | Integer NUMBER(3), API numérica | DOCUMENTATION_GAP | Oracle/DDL | PENDING_DECISION — atualizar specs na evolução FT-SINGULAR |

---

## 10. Divergências de tipos

| ID | Objeto | Detalhe | Classificação | Resolvido? |
|----|--------|---------|---------------|:----------:|
| T-01 | SINGULAR.COD_UNIMED | NUMBER(3) vs String/Long (histórico) | TYPE_INCOMPATIBILITY → corrigido | Sim |
| T-02 | COLABORADOR.DES_BIOGRAFIA | CLOB vs VARCHAR2 (histórico) | Obsoleto — DDL e JPA são VARCHAR2(4000) | Sim |
| T-03 | CLOB × @Lob (FEDERACAO, AREA, EQUIPE) | Padrão Hibernate | ORACLE_CORRECT | Pendente validação runtime |
| T-04 | TIMESTAMP(6) × Instant | Padrão projeto | ORACLE_CORRECT | Pendente validação runtime |
| T-05 | specs singular codigoUnimed String | API/spec vs NUMBER(3) | DOCUMENTATION_GAP | Não (PENDING_DECISION) |

---

## 11. Divergências estruturais

| ID | Objeto | Detalhe | Classificação | Ação |
|----|--------|---------|---------------|------|
| S-01 | 17 tabelas sem @Entity | Escopo Features futuras | DOCUMENTATION_GAP | Nenhuma nesta etapa |
| S-02 | Colunas das 6 tabelas mapeadas | JPA cobre todas as colunas DDL | — | Nenhuma |

Nenhuma coluna mapeada no JPA ausente no DDL `003`.

---

## 12. Divergências de constraints

| ID | Objeto | DDL | JPA | Classificação | Ação |
|----|--------|-----|-----|---------------|------|
| C-01 | UK_FEDERACAO_SIGLA, UK_SINGULAR_* | UNIQUE em DDL | Não declarado em @Table | ORACLE_CORRECT | Índices/UK no banco; validação em domínio/repository |
| C-02 | FK_AUTH_SESSAO_COLABORADOR | FK em DDL | @ManyToOne | — | Alinhado |
| C-03 | FK_AREA_GESTOR, FK_EQUIPE_LIDER | FK em DDL | Long nullable (sem @ManyToOne) | ORACLE_CORRECT | Mapeamento por ID é válido |

Nenhuma CONSTRAINT_MISMATCH estática identificada.

---

## 13. Divergências de sequences

| Sequence | DDL | JPA | Grants (doc) | Status |
|----------|-----|-----|--------------|--------|
| SQ_FEDERACAO_COD_FEDERACAO | Sim | FederacaoEntity | Sim | Alinhado |
| SQ_SINGULAR_COD_SINGULAR | Sim | SingularEntity | Sim | Alinhado |
| SQ_AREA_COD_AREA | Sim | AreaEntity | Sim | Alinhado |
| SQ_EQUIPE_COD_EQUIPE | Sim | EquipeEntity | Sim | Alinhado |
| SQ_COLABORADOR | Sim | ColaboradorEntity | Sim | Alinhado |
| SQ_AUTH_SESSAO | Sim | AuthSessaoEntity | Sim | Alinhado |
| SQ_ONBOARD_SOLIC … SQ_CONFIG_* (6 restantes) | Sim | Sem entidade | Sim | Escopo Feature |

`V902` inclui bloco brownfield para sequences legadas `SQ_AUTH_SESSAO_COD_SESSAO` / `SQ_COLABORADOR_COD_COLABORADOR` se existirem — nomenclatura **não** usada pelo JPA atual.

---

## 14. Divergências de índices

Índices são responsabilidade do DDL (`005-create-indexes.sql`); JPA não declara `@Index` nas entidades atuais.

| Escopo | Índices DDL relevantes | Divergência JPA |
|--------|------------------------|-----------------|
| 6 tabelas mapeadas | IDX_* + UK-backed indexes em 005 | Nenhuma — índices são físicos Oracle |

Validação live de índices: **bloqueada** (sem Oracle).

---

## 15. Divergências de grants

### Documentação (SSOT `database/security/`)

| Camada | Cobertura |
|--------|-----------|
| `grants/001-baseline-tables.sql` | 23 tabelas → UNMPORTCOM_APP_ROLE |
| `grants/002-baseline-sequences.sql` | 12 sequences → UNMPORTCOM_APP_ROLE |
| `grants/003-role-to-application-user.sql` | Role → UNMPORTCOM_APP |
| `V900__application_user_grants.sql` | Reaplicação brownfield (tabelas) |
| `V902__application_user_sequences.sql` | Reaplicação brownfield (sequences) |

### Objetos exigidos pelas 6 entidades JPA

| Objeto | Grant documentado |
|--------|:-----------------:|
| FEDERACAO, SINGULAR, AREA, EQUIPE, COLABORADOR, AUTH_SESSAO | Sim |
| SQ_FEDERACAO_COD_FEDERACAO … SQ_AUTH_SESSAO (6) | Sim |

### Grants efetivos no ambiente

| Verificação | Status |
|-------------|--------|
| `VAL-SEC-01-verify-application-privileges.sql` | **Não executado** — sem Oracle |
| `validate-application-user.sql` | **Não executado** — sem Oracle |
| `ApplicationUserConnectionIntegrationTest` | **Não executado** — sem Oracle |

**Classificação:** ACCESS_GRANT_GAP — scripts SSOT corretos; efetividade no ambiente alvo não comprovada.

---

## 16. Divergências de configuração

| Item | Configuração | Avaliação |
|------|--------------|-----------|
| Banco oficial | Oracle JDBC `ojdbc11` em `pom.xml` | Conforme |
| Flyway | Ausente em `pom.xml` e runtime | Conforme (DEC-DB-019) |
| PostgreSQL | Ausente em `backend/` | Conforme |
| H2 como substituto Oracle | Apenas `pf-*-test.properties` (legado deprecado) | Não usado por testes ativos (`test-slice` exclui JPA) |
| `ddl-auto=none` runtime | `application.yaml` | Correto |
| `ddl-auto=validate` testes | `application-test.yaml` | Correto |
| Username JDBC | `${SPRING_DATASOURCE_USERNAME}` — `.env.example` = UNMPORTCOM_APP | Correto |
| `UNMPORTCOM` como username | Não encontrado em config ativa | Conforme |
| `docker-compose.yml` | PostgreSQL 16 | **Fora de escopo Etapa 4** → Etapa 5 |

---

## 17. Hibernate validation

| Aspecto | Estado |
|---------|--------|
| Política | `ddl-auto=validate` no perfil `test` |
| Pré-condições | Oracle acessível + UNMPORTCOM_APP + grants + `connection-init-sql` |
| Execução nesta etapa | **Bloqueada** — sem `.env` / Oracle |
| Risco documentado | Ambiente sem V900/V902 → `missing table [unmportcom.federacao]` (DEC-DB-024) |

**Comando de desbloqueio:**

```bash
# .env na raiz com UNMPORTCOM_APP
cd backend && mvn test -Dtest=PortalComunicacaoApplicationTests,ApplicationUserConnectionIntegrationTest
```

Critério de sucesso: bootstrap Spring com `ddl-auto=validate` sem erros de schema.

---

## 18. Testes

| Teste | Perfil | Oracle necessário | Executado | Resultado |
|-------|--------|:-----------------:|:---------:|-----------|
| `mvn compile` | — | Não | Sim | **PASS** (exit 0) |
| `ApplicationPropertiesTest`, `SecurityPropertiesTest`, `PersistencePropertiesTest` | test-slice | Não | Sim | **PASS** (exit 0) |
| `@IntegrationTest` / `application-test.yaml` | test | Sim | Não | Bloqueado (sem Oracle) |
| `SchemaOracleAuditTest` | test | Sim | Não | Bloqueado (sem Oracle) |
| `ApplicationUserConnectionIntegrationTest` | test | Sim | Não | Bloqueado (sem Oracle) |
| `OracleHibernateCompatibilityIntegrationTest` | test | Sim | Não | Bloqueado (sem Oracle) |

**Nota:** a estratégia de testes está corretamente documentada (INFRA-DB-02): fatias PF sem Oracle; integração com Oracle único.

---

## 19. Correções realizadas

Nesta execução **nenhuma correção em código, DDL ou entidades JPA** foi necessária: a comparação estática JPA × DDL não identificou drift comprovado além dos itens já resolvidos em INFRA-BE-03.

| Artefato | Ação |
|----------|------|
| `construction/review/oracle-ddl-jpa-reconciliation-etapa4.md` | **Criado** (este relatório) |

---

## 20. Decisões pendentes

| ID | Item | Tipo | Evidência faltante / decisão |
|----|------|------|------------------------------|
| PD-01 | Validação Hibernate `ddl-auto=validate` | Bloqueio técnico | Oracle homolog + `.env` com UNMPORTCOM_APP |
| PD-02 | Grants efetivos UNMPORTCOM_APP | Bloqueio DBA | Executar VAL-SEC-01 no ambiente alvo |
| PD-03 | Revalidação Oracle live vs baseline 2026-07-22 | Bloqueio técnico | Consultas read-only ALL_TABLES / ALL_TAB_COLUMNS |
| PD-04 | `codigoUnimed` String em specs vs Integer em API/JPA | PENDING_DECISION | Atualizar `specs/features/singular/api.md` e artefatos relacionados |
| PD-05 | Remoção `pf-*-test.properties` (H2 legado) | PENDING_DECISION | Etapa 5+ / higiene de testes |

---

## 21. Itens encaminhados para Etapa 5+

| ID | Item | Etapa sugerida |
|----|------|----------------|
| F5-01 | `docker-compose.yml` ainda referencia PostgreSQL 16 | Etapa 5 — infraestrutura local |
| F5-02 | Documentos legados em `docs/` com referências PostgreSQL | Etapa 5 / higiene documental |
| F5-03 | Remoção arquivos `pf-*-test.properties` (H2 deprecado) | Etapa 5 / higiene testes |
| F5-04 | CI: falhar build se `SPRING_DATASOURCE_USERNAME` ≠ UNMPORTCOM_APP | Etapa 5 |
| F5-05 | Unificar `OraclePersistenceIntegrationTest` no perfil `test` | Etapa 5 (opcional) |
| F5-06 | Atualização specs FT-SINGULAR (`codigoUnimed`) | Feature / governança specs |

---

## 22. Critérios de conclusão

| Critério | Status |
|----------|:------:|
| Oracle confirmado como banco oficial | Sim (documentação + código) |
| UNMPORTCOM confirmado como schema owner | Sim |
| UNMPORTCOM_APP confirmado como usuário da aplicação | Sim (config) |
| Estratégia de grants reconciliada (documental) | Sim |
| Grants efetivos validados no ambiente | **Sim** (runtime 2026-08-13) |
| SSOT físico do banco identificado | Sim |
| DDL comparado contra Oracle | Parcial (via DB-SYNC-99; live pendente) |
| JPA comparado contra DDL (estático) | Sim |
| JPA comparado contra Oracle live | **Sim** (6 tabelas + 6 sequences via `SchemaOracleAuditTest`) |
| Divergências classificadas | Sim |
| Sequences / constraints / índices verificados (estático) | Sim |
| Hibernate validation validado | **Sim** (`ddl-auto=validate`, `mvn test` exit 0) |
| Nenhuma divergência mascarada | Sim |
| PostgreSQL / H2 substituto / Flyway não introduzidos | Sim |
| Banco produção não alterado | Sim |
| Relatório Etapa 4 produzido | Sim |
| Itens não resolvidos classificados | Sim |
| Working tree somente alterações Etapa 4 | Sim (apenas este relatório) |

---

## 23. Runtime Validation (ETAPA 4B)

| Campo | Valor |
|-------|-------|
| Data/hora | 2026-08-13T20:22:35-03:00 |
| Ambiente | Oracle homolog — `ractst-scan.unimedce.com.br:1521` / service `unmtst.unimedce.com.br` |
| Executor | Agente (validação runtime) |
| Artefato detalhado | `construction/review/oracle-runtime-validation-etapa4.md` |

### Conectividade

| Verificação | Resultado |
|-------------|-----------|
| `.env` presente com `SPRING_DATASOURCE_*` | Sim |
| TCP `ractst-scan.unimedce.com.br:1521` | **SUCCESS** (`10.20.1.44`) |
| JDBC (Hikari + `ojdbc11`) | **SUCCESS** |
| `mvn compile` | **PASS** (exit 0) |

### Usuário efetivo

| Verificação | Resultado |
|-------------|-----------|
| `SPRING_DATASOURCE_USERNAME` configurado | `UNMPORTCOM_APP` (mascarado em `.env`) |
| `SELECT USER FROM DUAL` | **`UNMPORTCOM_APP`** — `ApplicationUserConnectionIntegrationTest` |
| Conexão como owner `UNMPORTCOM` | **Não** (assertion explícita) |

### Schema e grants

| Verificação | Resultado |
|-------------|-----------|
| `hibernate.default_schema` | `UNMPORTCOM` (default YAML; ausente em `.env`, não bloqueante) |
| `connection-init-sql` | `ALTER SESSION SET CURRENT_SCHEMA = UNMPORTCOM` (default YAML) |
| Visibilidade `UNMPORTCOM.FEDERACAO` via `ALL_TABLES` | **≥ 1** — grants efetivos confirmados |
| Sequences JPA (6) via `ALL_SEQUENCES` | **F=0** no audit — todas presentes |
| `VAL-SEC-01-verify-application-privileges.sql` | Não executado isoladamente; grants inferidos por testes + Hibernate validate |

### Hibernate `ddl-auto=validate`

| Aspecto | Resultado |
|---------|-----------|
| Perfil `test` (`application-test.yaml`) | `ddl-auto: validate` |
| `PortalComunicacaoApplicationTests` | **PASS** — `EntityManagerFactory` sem erro de schema |
| `OracleHibernateCompatibilityIntegrationTest` | **PASS** — dialect `OracleLegacyDialect`, Oracle **11.2.0.4** |
| Erros `missing table` / `wrong column type` | **Nenhum** |

### Testes

| Comando / suíte | Resultado |
|-----------------|-----------|
| `mvn test -Dtest=ApplicationUserConnectionIntegrationTest,OracleHibernateCompatibilityIntegrationTest,SchemaOracleAuditTest,PortalComunicacaoApplicationTests` | **5/5 PASS**, BUILD SUCCESS |
| `mvn test` (suíte completa) | **244 tests**, 0 failures, 0 errors, 4 skipped, BUILD SUCCESS |

### SchemaOracleAuditTest

| Categoria | Contagem | Significado |
|-----------|:--------:|-------------|
| A (tabela/coluna ausente) | 0 | — |
| B (tipo incompatível) | 0 | — |
| C (precisão/escala) | 0 | — |
| D | 0 | — |
| E (nullable) | **1** | `SINGULAR.NUM_REGISTRO_ANS` — JPA `nullable=false`, Oracle `nullable=true` |
| F (sequence ausente) | 0 | — |
| G (FK ausente) | 0 | — |

**Classificação:** `CONSTRAINT_MISMATCH` — drift classificado; Hibernate validate não falhou; DDL baseline declara `NOT NULL` — DBA deve confirmar constraint efetiva no Oracle live.

### Divergências runtime (classificadas)

| ID | Objeto | Classificação | Status |
|----|--------|---------------|--------|
| RV-01 | `SINGULAR.NUM_REGISTRO_ANS` nullable | CONSTRAINT_MISMATCH | Classificado — pendente verificação DBA |
| RV-02 | `codigoUnimed` String (specs) vs Integer (JPA) | PENDING_DECISION (PD-04) | Não alterado nesta etapa |
| RV-03 | `.env` sem `SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA` / `ORACLE_SESSION_CURRENT_SCHEMA_SQL` | CONFIGURATION_GAP | Mitigado por defaults em `application.yaml` |

### Limitações

- Contagem completa das 23 tabelas baseline não reexecutada via SQL ad-hoc; cobertura via objetos JPA + audit.
- `validate-application-user.sql` / `VAL-SEC-01` não executados manualmente (evidência indireta via integração).
- `v$parameter.compatible` indisponível (privilégio insuficiente — não bloqueante).

---

## 24. Conclusão

### Resumo executivo

A reconciliação estática confirma que o SSOT do banco está bem definido (`database/GOVERNANCE.md`), que DDL e baseline homologada 2026-07-22 estão alinhados nominalmente (23 tabelas, 12 sequences), e que as **6 entidades JPA** mapeadas estão **alinhadas ao DDL** após as correções de INFRA-BE-03.

A **validação runtime (ETAPA 4B)** desbloqueou a etapa: Oracle homolog acessível, conexão como `UNMPORTCOM_APP`, grants efetivos comprovados por integração, Hibernate `ddl-auto=validate` sem erros, suíte `mvn test` completa verde (244 testes). Permanece **1 achado classificado** no `SchemaOracleAuditTest` (categoria E — nullable em `SINGULAR.NUM_REGISTRO_ANS`) e **PD-04** (`codigoUnimed`) sem decisão de contrato.

### Status final

```text
ETAPA 4 — CONCLUÍDA
```

### Critérios de desbloqueio (atendidos)

1. `.env` com `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP`, senha válida — **atendido**.
2. Grants efetivos no ambiente — **atendido** (Hibernate validate + `FEDERACAO` visível + sequences presentes).
3. `cd backend && mvn test` — **0 errors** (244 tests, 4 skipped).
4. `SchemaOracleAuditTest` — **executado**; E=1 classificado como `CONSTRAINT_MISMATCH` (não bloqueia Hibernate validate).
5. `ApplicationUserConnectionIntegrationTest` — **PASS** (usuário ≠ owner; visibilidade `FEDERACAO`).

### Riscos remanescentes

| Risco | Severidade | Mitigação |
|-------|------------|-----------|
| Ambiente Oracle sem grants reaplicados | Alta | Executar V900/V902 + VAL-SEC-01 |
| Specs FT-SINGULAR desalinhadas do contrato numérico | Média | PD-04 |
| `docker-compose.yml` PostgreSQL induz stack errada localmente | Média | Etapa 5 |
| Baseline 2026-07-22 desatualizada vs Oracle real | Média | Nova homologação DBA + validation |

---

## Referências

- `database/GOVERNANCE.md`
- `database/baseline/oracle-baseline-2026-07-22.md`
- `database/reports/infra-be-03-jpa-schema-alignment.md`
- `database/reports/infra-db-02-test-context-audit.md`
- `docs/architecture/decisions/DEC-DB-024-application-user-strategy.md`
- `backend/src/test/java/.../SchemaOracleAuditTest.java`
