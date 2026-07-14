# Fase 2 — Relatório Backend Construction + Sprint 0

**Projeto:** Portal de Comunicação  
**Data de encerramento:** 2026-07-08  
**Escopo:** Sprint 0 — infraestrutura transversal do backend  
**Fonte normativa:** `docs/implementation/11-bootstrap-roadmap.md`, `docs/construction/backend/01-project-bootstrap.md`  
**Pré-requisito:** `docs/governance/history/phase1-frontend-construction-report.md`, `docs/governance/reconciliation-report.md`

**Status:** Sprint 0 **ENCERRADA OFICIALMENTE**

---

## Objetivo da Sprint

Estabelecer a fundação técnica transversal do backend do Portal de Comunicação, sem implementar regras de negócio, modelos de domínio ou APIs REST de features.

A Sprint 0 deve permitir que sprints subsequentes implementem Features sobre uma base estável, testada e documentada.

---

## Objetivos da Sprint — Checklist

| Objetivo                                      | Status |
| --------------------------------------------- | ------ |
| Bootstrap do projeto Maven/Spring Boot          | ✅     |
| Módulos shared (DTO, Exception, Validation, Constants, Util) | ✅ |
| Configuration (Jackson, Locale, Async, Properties) | ✅  |
| Infrastructure Logging (Correlation ID, MDC)  | ✅     |
| Integração Oracle + baseline DDL (DBA)        | ✅     |
| Testes unitários da infraestrutura            | ✅     |
| Build reproduzível                            | ✅     |
| Documentação sincronizada                     | ✅     |
| Auditoria de aderência                        | ✅     |

---

## Principais Decisões Arquiteturais

| Decisão | Escolha | Registro |
| ------- | ------- | -------- |
| Linguagem | Java 25 | `backend/pom.xml` |
| Framework | Spring Boot 4.1.0 | `backend/pom.xml` |
| Serialização | Jackson 3 (`JsonMapper`) | `configuration/jackson/JacksonConfiguration.java` |
| Banco de dados | Oracle Database (schema `UNMPORTCOM`) | `application.yaml`, DEC-007 |
| Build | Maven — `mvn clean verify` | `docs/construction/backend/01-project-bootstrap.md` |
| Pacote raiz | `br.com.unimedceara.portalcomunicacao` | Estrutura de diretórios |
| Versionamento | Semantic Versioning (`0.0.1-SNAPSHOT`) | DEC-005 |
| Estratégia de testes | Unitários + verificação em build | DEC-006 |
| Observabilidade (Sprint 0) | SLF4J + MDC + Actuator | Parcial — métricas customizadas adiadas |
| Segurança (Sprint 0) | `spring-boot-starter-security` no classpath | SecurityFilterChain adiado para FT-AUTH |

---

## Infraestrutura Construída

### Bootstrap

```text
backend/
├── pom.xml
├── src/main/java/.../PortalComunicacaoApplication.java
├── src/main/resources/
│   ├── application.yaml
│   ├── application-local.yaml
│   ├── application-dev.yaml
│   ├── application-hml.yaml
│   └── db/migration/V1__baseline.sql
└── src/test/ (20 classes de teste)
```

### Módulos Transversais

| Módulo | Componentes principais |
| ------ | ---------------------- |
| `shared/dto/` | ApiResponse, PageResponse, ErrorResponse, ValidationErrorResponse, FieldValidationError |
| `shared/exception/` | GlobalExceptionHandler, BusinessException, ValidationException, ResourceNotFoundException, UnauthorizedException, ForbiddenException, ConflictException, ExceptionResponseBuilder |
| `shared/validation/` | @Uuid, @EnumValue, @NotBlankIfPresent, @NullOrSize + validadores |
| `shared/constants/` | ApiConstants, HeaderConstants, SecurityConstants, DateTimeConstants |
| `shared/util/` | DateTimeUtils, UuidUtils, CollectionUtils, PaginationUtils |
| `configuration/` | JacksonConfiguration, LocaleConfiguration, AsyncConfiguration, ApplicationProperties |
| `infrastructure/logging/` | CorrelationIdFilter, CorrelationIdGenerator, MdcUtils, LoggingConfiguration, LoggingConstants |

### Integração Oracle

* Driver: `ojdbc11` (`oracle.jdbc.OracleDriver`)
* Baseline DDL — schema administrado pelo DBA (`docs/database/ddl/` — DEC-DB-019)
* Conexão validada em ambiente de testes (Oracle 11.2)

---

## Tecnologias Utilizadas

| Tecnologia | Versão / Detalhe |
| ---------- | ---------------- |
| Java | 25 |
| Spring Boot | 4.1.0 |
| Jackson | 3 (via Spring Boot 4) |
| Maven | Conforme wrapper do projeto |
| Oracle JDBC | ojdbc11 |
| Flyway (legado) | Removido da arquitetura — DEC-DB-019 |
| Lombok | Optional |
| Spring Security | Classpath — configuração pendente (FT-AUTH) |
| Spring Actuator | Classpath — configuração padrão |
| Bean Validation | spring-boot-starter-validation |

---

## Testes

| Métrica | Valor |
| ------- | ----- |
| Classes de teste | 20 |
| Métodos de teste | **106** |
| Tipos | Unitários (shared, configuration, infrastructure/logging) |
| Comando | `mvn clean verify` |
| Resultado | **SUCCESS** — todos os testes passando |

### Cobertura por área

* Shared DTO e Exception — testado
* Shared Validation — testado
* Shared Util — testado
* Configuration — testado
* Infrastructure Logging — testado
* Application bootstrap — smoke test (`PortalComunicacaoApplicationTests`)

---

## Resultado do Build

```text
Comando: mvn clean verify
Resultado: BUILD SUCCESS
Data: 2026-07-08
Ambiente: WSL Ubuntu — Java 25.0.3
```

Build configurado para falhar caso testes falhem.

---

## Refinements Executados

| Refinement | Descrição |
| ---------- | --------- |
| R-001 | Alinhamento de banco: eliminada divergência PostgreSQL (Fase 2 documental) → Oracle na implementação |
| R-002 | Alinhamento de versões: Java 21/Spring Boot 3.4 (bootstrap inicial) → Java 25/Spring Boot 4.1 |
| R-003 | Pacote raiz padronizado: `br.com.unimedceara.portalcomunicacao` |
| R-004 | GlobalExceptionHandler consolidado com ExceptionResponseBuilder |
| R-005 | Validadores customizados (@Uuid, @EnumValue, @NotBlankIfPresent, @NullOrSize) |
| R-006 | Correlation ID via header `X-Correlation-Id` e MDC |
| R-007 | ApplicationProperties com validação `@NotBlank` |
| R-008 | Sincronização `docs/implementation/` e `docs/construction/backend/` com código |

---

## Auditoria Final

| Dimensão | Resultado |
| -------- | --------- |
| Documentação ↔ Implementação | **Alinhadas** — infraestrutura Sprint 0 |
| Implementação ↔ Arquitetura | **Alinhadas** — stack e estrutura conforme `docs/implementation/` |
| Implementação ↔ Tecnologia | **Alinhadas** — Java 25, Spring Boot 4.1, Oracle |
| Escopo Sprint 0 | **Respeitado** — sem APIs de negócio, sem bounded contexts |
| Itens adiados | **Documentados** — backlog técnico pós-Sprint 0 |

**Classificação:** Sprint 0 **APROVADA** — baseline congelada.

---

## Documentação Consolidada

| Documento | Ação |
| --------- | ---- |
| `docs/construction/backend/01-project-bootstrap.md` | Atualizado — critérios Sprint 0 |
| `docs/implementation/02-repository-structure.md` | Atualizado — escopo Sprint 0 |
| `docs/implementation/09-observability-standards.md` | Atualizado — estado parcial Sprint 0 |
| `docs/implementation/11-bootstrap-roadmap.md` | Atualizado — entregáveis e backlog |
| `docs/governance/reconciliation-report.md` | Atualizado — reconciliação Sprint 0 |
| `docs/governance/01-project-status.md` | Atualizado — encerramento Sprint 0 |
| `docs/governance/05-roadmap.md` | Atualizado — Sprint 0 CONCLUÍDA |

---

## Lições Aprendidas

1. **Congelar infraestrutura antes de features** — a baseline transversal reduz retrabalho nas sprints de negócio.
2. **Testes desde a fundação** — 106 testes unitários garantem confiança para evoluir shared modules.
3. **Documentação incremental** — refinements documentados em `docs/implementation/` evitam divergência com código.
4. **Oracle legado** — Oracle 11.2 gera warnings de dialect Hibernate; não bloqueia Sprint 0, monitorar em sprint futura.
5. **Segurança adiada conscientemente** — SecurityFilterChain reservado para FT-AUTH evita implementação prematura.

---

## Estado Final da Sprint

| Aspecto | Estado |
| ------- | ------ |
| Infraestrutura | **Concluída e congelada** |
| Documentação | **Consolidada e alinhada** |
| Arquitetura | **Baseline aprovada** |
| Governança | **Sprint 0 encerrada oficialmente** |
| Testes | **106/106 passando** |
| Build | **SUCCESS** |
| Auditoria | **Aprovada** |
| Próxima etapa | **Sprint 1 — FT-AUTH** |

---

## Itens Explicitamente Fora do Escopo (Preservados como Backlog)

* MapStruct, OpenAPI/Swagger UI
* REST Controllers (incluindo health endpoint dedicado)
* SecurityFilterChain e integração de autenticação
* Bounded contexts (organization, accesscontrol, documentmanagement, internalcommunication)
* Entidades JPA e repositórios
* Métricas Micrometer/Prometheus, OpenTelemetry, Grafana
* Correlation ID no padrão de log (emissão)
* Request logging estruturado
* Docker funcional com Oracle

---

## Conformidade

| Critério | Status |
| -------- | ------ |
| Novos requisitos de negócio criados? | **Não** |
| Modelos de domínio implementados? | **Não** |
| APIs de negócio implementadas? | **Não** |
| Decisões arquiteturais alteradas? | **Não** — consolidadas |
| Alinhado ao MVP oficial? | **Sim** |
| Infraestrutura congelada? | **Sim** |
| Pronto para Sprint 1 (FT-AUTH)? | **Sim** |

---

## Histórico do Documento

| Data       | Evento |
| ---------- | ------ |
| 2026-06-22 | Fase 2 documental — limpeza Construction backend/infrastructure |
| 2026-07-08 | Encerramento oficial Sprint 0 — registro histórico permanente |
