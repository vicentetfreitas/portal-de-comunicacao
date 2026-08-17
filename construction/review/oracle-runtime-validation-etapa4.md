# Oracle Runtime Validation — Etapa 4

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Etapa | 4B — Validação Runtime Oracle / DDL / JPA |
| Data/hora | 2026-08-13T20:22:35-03:00 |
| Executor | Agente (validação runtime) |
| Status | **ETAPA 4 — CONCLUÍDA** |

---

## 1. Objetivo

Comprovar, com evidência runtime real (sem simulação), que o backend conecta ao Oracle homolog como `UNMPORTCOM_APP`, que grants e objetos JPA são acessíveis, que Hibernate `ddl-auto=validate` executa sem erros e que a suíte de testes Oracle passa integralmente.

---

## 2. Ambiente validado

| Item | Valor |
|------|-------|
| Host | `ractst-scan.unimedce.com.br` (`10.20.1.44`) |
| Porta | `1521` |
| Service name | `unmtst.unimedce.com.br` |
| URL pattern | `jdbc:oracle:thin:@<host>:<port>/<service>` |
| Oracle version | 11g Enterprise Edition **11.2.0.4.0** (64-bit) |
| JDBC driver | Oracle JDBC driver **23.26.2.0.0** |
| Hibernate dialect | `org.hibernate.community.dialect.OracleLegacyDialect` |
| Config source | `.env` na raiz (importado por `application-test.yaml`) |
| Java / Maven | OpenJDK 25.0.3 / Apache Maven 3.6.3 |

### Variáveis de ambiente

| Variável | `.env.example` | `.env` | Observação |
|----------|:--------------:|:------:|------------|
| `SPRING_DATASOURCE_URL` | presente | presente | Mascarado |
| `SPRING_DATASOURCE_USERNAME` | `UNMPORTCOM_APP` | presente | Confirmado runtime |
| `SPRING_DATASOURCE_PASSWORD` | presente | presente | Mascarado |
| `SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA` | `UNMPORTCOM` | ausente | Default YAML aplica `UNMPORTCOM` |
| `ORACLE_SESSION_CURRENT_SCHEMA_SQL` | presente | ausente | Default YAML aplica `ALTER SESSION SET CURRENT_SCHEMA = UNMPORTCOM` |

**Nota operacional:** `.env` contém line endings CRLF; `source ../.env` funciona para Maven mas emite avisos `$'\r'`. Não bloqueante nesta execução.

---

## 3. Conectividade

| Verificação | Comando / método | Resultado |
|-------------|------------------|-----------|
| Rede TCP | `nc -zv -w 5 ractst-scan.unimedce.com.br 1521` | **SUCCESS** |
| JDBC pool (Hikari) | Testes Spring Boot perfil `test` | **SUCCESS** |
| `mvn compile` | `cd backend && mvn compile -q` | **exit 0** |

---

## 4. Usuário efetivo

| Verificação | Evidência | Resultado |
|-------------|-----------|-----------|
| Username configurado | `SPRING_DATASOURCE_USERNAME` em `.env` | `UNMPORTCOM_APP` |
| `SELECT USER FROM DUAL` | `ApplicationUserConnectionIntegrationTest.shouldConnectAsApplicationUserNotSchemaOwner` | **`UNMPORTCOM_APP`** |
| Não conecta como owner | Mesmo teste — assertion `≠ UNMPORTCOM` | **PASS** |

---

## 5. Schema

| Item | Valor |
|------|-------|
| Schema owner (objetos físicos) | `UNMPORTCOM` |
| `hibernate.default_schema` | `UNMPORTCOM` |
| `connection-init-sql` | `ALTER SESSION SET CURRENT_SCHEMA = UNMPORTCOM` |
| Visibilidade tabela `FEDERACAO` | `ALL_TABLES` count ≥ 1 — `ApplicationUserConnectionIntegrationTest` |

### Objetos JPA auditados (6 tabelas)

`FEDERACAO`, `SINGULAR`, `AREA`, `EQUIPE`, `COLABORADOR`, `AUTH_SESSAO` — todas presentes no Oracle live (categoria A=0 no audit).

**Limitação:** contagem nominal das 23 tabelas baseline não reexecutada via SQL ad-hoc; cobertura focada nos objetos JPA ativos.

---

## 6. Grants

| Verificação | Resultado | Classificação |
|-------------|-----------|---------------|
| Leitura `UNMPORTCOM.FEDERACAO` | Sucesso (count ≥ 1 em `ALL_TABLES`) | Grants efetivos |
| Hibernate `ddl-auto=validate` nas 6 entidades | Sem `ORA-00942` / `missing table` | Grants efetivos |
| `VAL-SEC-01-verify-application-privileges.sql` | Não executado isoladamente | Evidência indireta |
| `validate-application-user.sql` | Não executado isoladamente | Evidência indireta |

**Conclusão:** grants documentados em `database/security/` estão **efetivamente disponíveis** no ambiente homolog testado. Não foi necessário aplicar V900/V902 nesta sessão.

---

## 7. Objetos Oracle

Fonte: `SchemaOracleAuditTest` — consultas read-only em `ALL_TAB_COLUMNS`, `ALL_CONSTRAINTS`, `ALL_SEQUENCES`.

| Categoria audit | Contagem | Interpretação |
|-----------------|:--------:|---------------|
| A — tabela/coluna ausente | 0 | Objetos JPA presentes |
| B — tipo incompatível | 0 | Tipos alinhados |
| C — precisão/escala | 0 | — |
| D | 0 | — |
| E — nullable | **1** | Ver §12 |
| F — sequence ausente | 0 | — |
| G — FK ausente | 0 | — |

---

## 8. Sequences

| Sequence | Entidade JPA | Oracle live |
|----------|--------------|:-----------:|
| `SQ_FEDERACAO_COD_FEDERACAO` | FederacaoEntity | Presente |
| `SQ_SINGULAR_COD_SINGULAR` | SingularEntity | Presente |
| `SQ_AREA_COD_AREA` | AreaEntity | Presente |
| `SQ_EQUIPE_COD_EQUIPE` | EquipeEntity | Presente |
| `SQ_COLABORADOR` | ColaboradorEntity | Presente |
| `SQ_AUTH_SESSAO` | AuthSessaoEntity | Presente |

Categoria F=0 no `SchemaOracleAuditTest`.

---

## 9. Hibernate ddl-auto=validate

| Aspecto | Resultado |
|---------|-----------|
| Perfil | `test` (`application-test.yaml`: `ddl-auto: validate`) |
| `PortalComunicacaoApplicationTests` | **PASS** — contexto Spring + `EntityManagerFactory` |
| `OracleHibernateCompatibilityIntegrationTest` | **PASS** — repositório JPA + dialect Oracle |
| Erros de schema Hibernate | **Nenhum** |
| Warnings | `HHH100123` (fetch size default) — não bloqueante |

Relatório gerado: `backend/runtime/reports/oracle-hibernate-compat.txt`

---

## 10. Testes

### Suíte Oracle focal

```bash
cd backend && mvn test \
  -Dtest=ApplicationUserConnectionIntegrationTest,\
OracleHibernateCompatibilityIntegrationTest,\
SchemaOracleAuditTest,\
PortalComunicacaoApplicationTests
```

| Resultado | Valor |
|-----------|-------|
| Tests run | 5 |
| Failures | 0 |
| Errors | 0 |
| Skipped | 0 |
| BUILD | **SUCCESS** |

### Suíte completa

```bash
cd backend && mvn test
```

| Resultado | Valor |
|-----------|-------|
| Tests run | 244 |
| Failures | 0 |
| Errors | 0 |
| Skipped | 4 |
| BUILD | **SUCCESS** |
| Duração | ~45s |

---

## 11. SchemaOracleAuditTest

**Executado:** sim.

Relatório gerado: `backend/runtime/reports/schema-oracle-audit.txt`

```
table	column	entity	attribute	category	description
SINGULAR	NUM_REGISTRO_ANS	SingularEntity	registroAns	E	JPA nullable=false, Oracle nullable=true

--- MATRIX CATEGORY ---
A	0  B	0  C	0  D	0  E	1  F	0  G	0
```

O teste **passa** (auditoria cataloga divergências sem falhar o build). A divergência E=1 é classificada — não mascarada.

---

## 12. Divergências encontradas

| ID | Objeto | Evidência | Classificação | Ação |
|----|--------|-----------|---------------|------|
| RV-01 | `SINGULAR.NUM_REGISTRO_ANS` | JPA `nullable=false`; Oracle `ALL_TAB_COLUMNS.nullable=Y`; DDL baseline declara `NOT NULL` | **CONSTRAINT_MISMATCH** | DBA verificar constraint efetiva no Oracle live |
| RV-02 | `codigoUnimed` | Specs FT-SINGULAR: String; JPA/API: Integer NUMBER(3) | **PENDING_DECISION** (PD-04) | Decisão de contrato/domínio — não alterado |
| RV-03 | `.env` incompleto vs `.env.example` | Faltam vars JPA/session schema | **CONFIGURATION_GAP** | Mitigado por defaults YAML; recomendado alinhar `.env` local |

Nenhuma divergência foi mascarada. Nenhum dado foi inserido/modificado. Nenhum DDL destrutivo executado.

---

## 13. Evidências

| # | Evidência | Local |
|---|-----------|-------|
| E1 | TCP connectivity | `nc` → port 1521 SUCCESS |
| E2 | JDBC user | `ApplicationUserConnectionIntegrationTest` (2/2 PASS) |
| E3 | Hibernate validate | `PortalComunicacaoApplicationTests` + `OracleHibernateCompatibilityIntegrationTest` |
| E4 | Audit JPA×Oracle | `backend/runtime/reports/schema-oracle-audit.txt` |
| E5 | Compat report | `backend/runtime/reports/oracle-hibernate-compat.txt` |
| E6 | Suíte completa | `mvn test` — 244 tests, BUILD SUCCESS |

---

## 14. Pendências

| ID | Item | Responsável | Bloqueia Etapa 4? |
|----|------|-------------|:-----------------:|
| P1 | Confirmar constraint `NOT NULL` em `SINGULAR.NUM_REGISTRO_ANS` no Oracle live | DBA | Não (classificado) |
| P2 | PD-04 — `codigoUnimed` String vs Integer | Governança specs | Não |
| P3 | Alinhar `.env` com vars JPA de `.env.example` | Dev local | Não |
| P4 | Executar `VAL-SEC-01` / `validate-application-user.sql` como evidência formal DBA | DBA | Não (grants comprovados indiretamente) |
| P5 | Revalidação nominal 23 tabelas vs baseline | DBA / próxima homologação | Não |

---

## 15. Resultado

### Checklist de desbloqueio

| Critério | Status |
|----------|:------:|
| Oracle acessível | ✅ |
| Usuário JDBC = `UNMPORTCOM_APP` | ✅ |
| Schema owner = `UNMPORTCOM` | ✅ |
| Grants necessários disponíveis | ✅ |
| Objetos JPA acessíveis | ✅ |
| Sequences necessárias acessíveis | ✅ |
| Hibernate `ddl-auto=validate` executado | ✅ |
| Hibernate validation sem erros de schema | ✅ |
| Testes de persistência relevantes executados | ✅ |
| `SchemaOracleAuditTest` executado | ✅ |
| Nenhum drift não classificado | ✅ |
| Nenhuma divergência mascarada | ✅ |
| Banco não alterado | ✅ |
| Relatório atualizado | ✅ |

```text
ETAPA 4 — CONCLUÍDA
```

---

## Referências

- `construction/review/oracle-ddl-jpa-reconciliation-etapa4.md` (§ Runtime Validation)
- `database/GOVERNANCE.md`
- `database/security/README.md`
- `backend/src/test/resources/application-test.yaml`
- `backend/src/test/java/.../SchemaOracleAuditTest.java`
