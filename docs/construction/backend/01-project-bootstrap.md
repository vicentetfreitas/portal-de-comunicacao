# Project Bootstrap

**Fonte normativa MVP:** `docs/audit/10-mvp-consolidation-audit.md`

## Objetivo

Definir o processo de inicialização e preparação do backend do Portal de Comunicação.

Este documento estabelece os padrões necessários para que qualquer desenvolvedor consiga criar, executar e evoluir o projeto de forma consistente.

---

# Escopo

Abrange:

* Estrutura inicial do projeto
* Dependências
* Configurações
* Ambientes
* Build
* Infraestrutura transversal da Sprint 0

Não abrange:

* Implementação de regras de negócio
* Modelagem de domínio
* APIs REST
* Integrações

---

# Stack Tecnológica

## Linguagem

Java 25

## Framework

Spring Boot 4.1.0

## Serialização

Jackson 3 (via Spring Boot 4 — `tools.jackson.databind.json.JsonMapper`)

## Build

Maven

## Banco de Dados

Oracle Database

Schema: `UNMPORTCOM`

Driver: `oracle.jdbc.OracleDriver` (`ojdbc11`)

**Rastreabilidade:** `docs/implementation/04-backend-architecture.md`, `docs/implementation/06-database-standards.md`.

## Containerização

Docker

Docker Compose

## Observabilidade (Sprint 0)

SLF4J + MDC (Correlation ID)

Spring Boot Actuator

---

# Estrutura de Diretórios

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/br/com/unimedceara/portalcomunicacao/
│   │   │   ├── PortalComunicacaoApplication.java
│   │   │   ├── configuration/
│   │   │   │   ├── async/
│   │   │   │   ├── jackson/
│   │   │   │   ├── locale/
│   │   │   │   └── properties/
│   │   │   ├── infrastructure/
│   │   │   │   └── logging/
│   │   │   └── shared/
│   │   │       ├── constants/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── util/
│   │   │       └── validation/
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-local.yaml
│   │       ├── application-dev.yaml
│   │       ├── application-hml.yaml
│   │       └── db/migration/
│   └── test/
├── pom.xml
└── README.md
```

---

# Dependências Obrigatórias (Sprint 0)

* spring-boot-starter-webmvc
* spring-boot-starter-validation
* spring-boot-starter-data-jpa
* spring-boot-starter-actuator
* spring-boot-starter-security
* ojdbc11
* lombok

> **DEC-DB-019:** `spring-boot-starter-flyway` e `flyway-database-oracle` não fazem parte da arquitetura oficial. Dependências legadas no `pom.xml` devem ser removidas.

---

# Backlog Técnico (adiado da Sprint 0)

Os itens abaixo estão previstos na arquitetura, mas **não fazem parte do bootstrap da Sprint 0**:

| Item                        | Sprint prevista |
| --------------------------- | --------------- |
| MapStruct                   | Sprint futura   |
| OpenAPI 3 / Swagger UI      | Sprint futura   |
| Micrometer / Prometheus     | Sprint futura   |
| OpenTelemetry               | Sprint futura   |
| Grafana                     | Sprint futura   |

---

# Configuração

## ConfigurationProperties

Propriedades da aplicação carregadas com prefixo `application`:

| Propriedade | Validação   | Valor padrão       |
| ----------- | ----------- | ------------------ |
| `name`      | `@NotBlank` | portal-comunicacao |
| `version`   | `@NotBlank` | 0.0.1-SNAPSHOT     |
| `timezone`  | `@NotBlank` | UTC                |
| `locale`    | `@NotBlank` | pt-BR              |

Habilitado via `ApplicationPropertiesConfiguration` (`@EnableConfigurationProperties`).

---

## Jackson

`JacksonConfiguration` customiza o `JsonMapper`:

* desabilita `WRITE_DATES_AS_TIMESTAMPS` (datas como ISO-8601)
* registra módulos via `findAndAddModules()`

---

## Locale

`LocaleConfiguration` define a localidade padrão a partir de `application.locale`.

---

## Async

`AsyncConfiguration` habilita `@EnableAsync` com executor `applicationTaskExecutor` (core=2, max=4, queue=50).

---

## Validation

Bean Validation provido automaticamente pelo `spring-boot-starter-validation`.

Não há `ValidationConfiguration` customizada na Sprint 0.

Validadores customizados em `shared/validation/`:

* `@Uuid`
* `@EnumValue`
* `@NotBlankIfPresent`
* `@NullOrSize`

---

## Logging

Infraestrutura em `infrastructure/logging/`:

* `CorrelationIdFilter` — propaga `X-Correlation-Id` via header e MDC
* `LoggingConfiguration` — registra filtro com `HIGHEST_PRECEDENCE`
* `MdcUtils` — wrapper SLF4J MDC

---

## Banco de Dados

```yaml
spring:
  datasource:
    driver-class-name: oracle.jdbc.OracleDriver
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: ${application.persistence.pool-max-size}
      minimum-idle: ${application.persistence.pool-min-idle}
  jpa:
    open-in-view: false
    show-sql: false
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.OracleDialect
        default_schema: UNMPORTCOM
```

Variáveis de ambiente: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA` (ver `.env.example`). No perfil `local`, `spring.config.import` carrega `../.env` automaticamente.

Schema provisionado pelo DBA via `docs/database/ddl/` (DEC-DB-019). Sem migração automática na inicialização. DataSource, EntityManagerFactory e TransactionManager são fornecidos pela AutoConfiguration do Spring Boot.

---

# Ambientes

## Local

Desenvolvimento individual. Perfil padrão: `local`.

## Development

Integração contínua. Perfil: `dev`.

## Homologação

Validação funcional. Perfil: `hml`.

## Produção

Ambiente final. Variáveis de ambiente para credenciais.

---

# Artefatos de Runtime

A Platform Foundation adota `backend/runtime/` como **único diretório oficial** para artefatos gerados durante execução, testes, auditorias e validações.

Nenhum log, relatório, dump ou arquivo de cobertura deve ser gravado diretamente na raiz de `backend/`.

## Estrutura oficial

```text
backend/
├── src/
├── target/              # Saída Maven (classes, JAR) — obrigatório pelo build
└── runtime/
    ├── logs/            # Logs de aplicação e build
    ├── reports/         # Relatórios de testes e validações
    ├── dumps/           # Heap dumps e artefatos de depuração
    └── coverage/        # Cobertura de código (JaCoCo e equivalentes)
```

## Convenção evolutiva

Novas ferramentas podem criar subdiretórios internos, preservando esta convenção:

```text
runtime/
├── coverage/
│   ├── jacoco/
│   └── aggregate/
├── dumps/
│   ├── heap/
│   └── thread/
├── logs/
│   ├── application.log
│   ├── build.log
│   └── integration.log
└── reports/
    ├── surefire/
    ├── failsafe/
    ├── mutation/
    └── performance/
```

A adoção desta estrutura **não exige** a criação imediata de todos os subdiretórios — eles serão introduzidos conforme novas ferramentas forem integradas.

## Destinos atuais (Platform Foundation)

| Tipo | Destino | Configuração |
|------|---------|--------------|
| Log da aplicação | `runtime/logs/application.log` | `logback-spring.xml`, `application.yaml` |
| Relatórios Surefire | `runtime/reports/surefire/` | `pom.xml` (`maven-surefire-plugin`) |
| Heap dumps | `runtime/dumps/` | `spring-boot-maven-plugin` (`-XX:HeapDumpPath`) |
| Cobertura (futuro) | `runtime/coverage/` | Propriedade `jacoco.output.directory` em `pom.xml` |

## Versionamento

Conteúdo de `backend/runtime/**` é ignorado pelo Git (exceto `.gitkeep`). Apenas a estrutura de diretórios é versionada.

---

# Build

```bash
mvn clean verify
```

Build deve falhar caso testes falhem.

---

# Critérios de Aceite (Sprint 0)

* Projeto executa localmente
* Build reproduzível (`mvn clean verify`)
* Perfis configurados (local, dev, hml)
* Conexão Oracle configurada
* Schema Oracle provisionado pelo DBA (baseline DDL)
* Infraestrutura transversal implementada:
  * DTOs de API padronizados
  * Tratamento global de exceções
  * Correlation ID via MDC e header
  * ConfigurationProperties validadas
  * Jackson 3 configurado
  * Validadores customizados

---

# Critérios de Aceite (Sprints Futuras)

* Documentação OpenAPI ativa
* Docker funcional com Oracle
* SecurityFilterChain configurado
* REST Controllers com health endpoint
* Métricas e dashboards operacionais
