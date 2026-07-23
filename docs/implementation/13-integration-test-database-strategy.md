# Estratégia de Banco — Testes de Integração (Backend)

| Item | Valor |
|------|-------|
| Decisão | **DEC-DB-023** |
| Status | APPROVED |
| Referência DDL | `database/ddl/` (DEC-DB-019) |

---

## Objetivo

Testes de integração validam a aplicação sobre o **Oracle oficial**, com schema governado pelo DBA. O Hibernate atua apenas como ORM e **valida** o mapeamento (`ddl-auto=validate` no perfil `test`).

---

## Configuração

| Propriedade | Valor |
|-------------|--------|
| Perfil Oracle integrado | `test` (`application-test.yaml`) |
| Perfil fatia (sem Oracle/JPA) | `test-slice` (`application-test-slice.yaml`, `@PlatformFoundationSliceTest`) |
| Datasource | `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` (`UNMPORTCOM_APP`), `SPRING_DATASOURCE_PASSWORD` |
| Sessão JDBC | `ORACLE_SESSION_CURRENT_SCHEMA_SQL` → `ALTER SESSION SET CURRENT_SCHEMA = UNMPORTCOM` (Hikari `connection-init-sql`) |
| `spring.jpa.hibernate.ddl-auto` | `validate` |
| Schema Hibernate | `UNMPORTCOM` (owner dos objetos; distinto do usuário JDBC) |
| Limpeza automática | `application.persistence.integration-test-cleanup.enabled=false` (padrão) |

No perfil `test`, `mvn test` importa o `.env` da raiz do repositório (`optional:file:../.env[.properties]`, mesmo contrato do perfil `local`).

---

## Governança

- O DBA é responsável por tabelas, sequences, constraints e índices.
- A aplicação **não** executa DDL em nenhum ambiente, inclusive testes.
- Durante a **fase de migração**, os dados carregados no Oracle de teste/homologação são parte do ambiente de validação e **não** devem ser apagados automaticamente pela suíte.

---

## Limpeza de dados (`IntegrationTestDatabaseCleaner`)

Implementação preservada em `backend/src/test/java/.../support/database/`.

- **Desligada** no fluxo Oracle padrão (`@IntegrationTest` sem `@EnableIntegrationTestDatabaseCleanup`).
- Reativação futura: `application.persistence.integration-test-cleanup.enabled=true` + `@EnableIntegrationTestDatabaseCleanup` na classe de teste.

---

## Categorias de teste

### Categoria 1 — Somente leitura (prioridade na migração)

Características: consultam dados existentes; não dependem de `deleteAll` nem de banco vazio.

| Classe | Observação |
|--------|------------|
| `OraclePersistenceIntegrationTest` | Nativo `SELECT 1 FROM DUAL`; requer `SPRING_DATASOURCE_URL` (perfil `local`) |
| `ApplicationUserConnectionIntegrationTest` | `SELECT USER FROM DUAL` — exige `UNMPORTCOM_APP` (DEC-DB-024) |

*Evolução:* extrair cenários GET puros das suítes de aceite para esta categoria, usando IDs do DML de migração (`database/dml/`).

### Categoria 2 — Mutação (POST / PUT / PATCH / DELETE)

Alteram persistência ou sessão. **Nesta fase** não há isolamento seletivo nem limpeza automática; podem acrescentar registros ao Oracle de teste.

| Classe | Escopo |
|--------|--------|
| `AuthAcceptanceIntegrationTest` | Autenticação, sessão, refresh, logout, admin sessions |
| `AuthFlowIntegrationTest` | Fluxo login/callback/me/refresh/logout |
| `AreaAcceptanceIntegrationTest` | CRUD áreas |
| `SingularAcceptanceIntegrationTest` | CRUD singulares |
| `EquipeAcceptanceIntegrationTest` | CRUD equipes |
| `ColaboradorAcceptanceIntegrationTest` | CRUD colaboradores |
| `OrgCrossFeatureIntegrationTest` | Hierarquia organizacional completa |

Decisão sobre limpeza seletiva: **evolução futura**, após a migração.

---

## Testes fora do escopo Oracle integrado

| Tipo | Motivo |
|------|--------|
| Slices `pf-*-test.properties` | JPA/DataSource excluídos — sem banco |
| `AuditableEntityJpaTest` | Desabilitado (antes: H2 + `create-drop`; incompatível com DEC-DB-023) |

---

## Evoluções futuras (não implementadas)

- Limpeza seletiva apenas de linhas criadas pelos testes de mutação.
- Suíte read-only ampliada contra seeds documentados.
- Testcontainers Oracle **somente** se Oracle corporativo deixar de atender CI e o custo de manutenção for justificado.
