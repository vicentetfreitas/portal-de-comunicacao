# INFRA-DB-02 — Auditoria de contextos de teste (backend)

| Campo | Valor |
|-------|-------|
| Atividade | INFRA-DB-02 |
| Pré-requisito | INFRA-DB-01 / DEC-DB-024 |
| Data | 2026-07-23 |

---

## 1. Causa raiz

O erro `Schema validation: missing table [unmportcom.federacao]` **não** indicava ausência física da tabela nem usuário JDBC incorreto isoladamente.

Havia **duas linhas de teste concorrentes**:

| Linha | Perfil | DataSource | Hibernate `validate` |
|-------|--------|------------|----------------------|
| **A — Oracle integrado** | `test` | `SPRING_DATASOURCE_*` / `UNMPORTCOM_APP` | Sim (`application-test.yaml`) |
| **B — Fatia PF (legado)** | `local` (default) + `pf-*-test.properties` | H2 em memória | Exclusão pretendida de JPA |

Os **~53 erros** vinham da linha **A**: com `UNMPORTCOM_APP`, o Hibernate validava metadados no owner `UNMPORTCOM` sem `ALTER SESSION SET CURRENT_SCHEMA`, e o extrator JDBC não localizava `FEDERACAO`.

Testes de wiring (properties, security slice, async) usavam **linha A desnecessariamente** (`@ActiveProfiles("test")`), amplificando falhas de contexto.

`ApplicationUserConnectionIntegrationTest` passava porque **excluía** `HibernateJpaAutoConfiguration` — apenas DataSource.

---

## 2. Inventário de contextos Spring (pós-consolidação)

### Perfil `test` — Oracle único (DEC-DB-023)

| Mecanismo | Classes |
|-----------|---------|
| `@IntegrationTest` | FT-AUTH, FT-COLABORADOR, FT-AREA, FT-EQUIPE, FT-SINGULAR, cross-org, `HealthEndpointE2ETest`, `AbstractIntegrationTest` |
| `@ActiveProfiles("test")` | `PortalComunicacaoApplicationTests`, `SchemaOracleAuditTest`, `ApplicationUserConnectionIntegrationTest` |
| `@ActiveProfiles("local")` + env | `OraclePersistenceIntegrationTest` (opcional, `SPRING_DATASOURCE_URL`) |

Configuração: `application-test.yaml` + `application.yaml` (dialect, `default_schema`) + `.env`.

### Perfil `test-slice` — sem Oracle/JPA

| Mecanismo | Classes |
|-----------|---------|
| `@PlatformFoundationSliceTest` | Properties PF, security slice, health MockMvc, observability, async, locale, logging |
| `@ActiveProfiles({"test-slice", "local\|dev\|hml"})` | `ConfigurationProperties*ProfileTest` |

Configuração: `application-test-slice.yaml` (exclui DataSource/JPA).

### Legado (não referenciado por testes ativos)

| Artefato | Estado |
|----------|--------|
| `pf-*-test.properties` | Deprecado; mantido para referência |
| `@DataJpaTest` `AuditableEntityJpaTest` | `@Disabled` (DEC-DB-023) |

---

## 3. DataSource e Hibernate — estratégia única

| Item | Valor oficial |
|------|----------------|
| Usuário JDBC | `UNMPORTCOM_APP` |
| Schema Hibernate | `UNMPORTCOM` |
| `ddl-auto` (perfil `test`) | `validate` |
| `connection-init-sql` (Hikari) | `ALTER SESSION SET CURRENT_SCHEMA = UNMPORTCOM` |
| Variável | `ORACLE_SESSION_CURRENT_SCHEMA_SQL` (`.env.example`) |
| Metadados (perfil `test`) | `OracleIntegrationTestPersistenceConfiguration` → `jdbc_metadata_extraction_strategy=individually` |

Runtime (não-teste) usa o mesmo `connection-init-sql` em `application.yaml`.

---

## 4. Relatório de correções

| Arquivo | Alteração |
|---------|-----------|
| `application.yaml` | Hikari `connection-init-sql` para application user |
| `application-test.yaml` | Idem + alinhamento Oracle |
| `application-test-slice.yaml` | **Novo** — perfil fatia sem JPA |
| `PlatformFoundationSliceTest` | **Nova** meta-anotação |
| `OracleIntegrationTestPersistenceConfiguration` | Customizer Hibernate (perfil `test`) |
| 15+ classes de teste PF | Migradas para `test-slice` |
| `ConfigurationPropertiesProfileTest` | `test-slice` + `local`/`dev`/`hml` |
| `ApplicationUserConnectionIntegrationTest` | Assert `ALL_TABLES` para `FEDERACAO` |
| `13-integration-test-database-strategy.md` | Documentação dual-profile |
| `.env.example` | `ORACLE_SESSION_CURRENT_SCHEMA_SQL` |

---

## 5. Evidências esperadas

```bash
cd backend && mvn test
```

| Métrica | Antes (referência) | Depois (esperado) |
|---------|-------------------|-------------------|
| Tests run | ~239 | ~241 (+2 asserts conexão) |
| Errors | ~53–58 | **0** |
| Failures | 0 | 0 |
| Skipped | 3 | 3 |

Pré-condição: `.env` com `SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP` e grants em `database/security/`.

---

## 6. Features validadas

Todas as Features com `@IntegrationTest` compartilham **um** contexto Oracle (`test`) com a mesma política de DataSource e sessão JDBC.

---

## 7. Recomendações

1. Remover `pf-*-test.properties` em release futura após período de depreciação.
2. Unificar `OraclePersistenceIntegrationTest` no perfil `test` (opcional).
3. CI: falhar build se `SPRING_DATASOURCE_USERNAME` ≠ `UNMPORTCOM_APP`.

---

Relatório de auditoria detalhado (tabelas por classe): ver seção 2 deste documento.

Correções aplicadas: seção 4.
