# Repository Structure

## Documento

```text
docs/implementation/02-repository-structure.md
```

---

# Objetivo

Definir a estrutura oficial do repositório do Portal de Comunicação.

Este documento estabelece:

* organização física do código
* separação de responsabilidades
* convenções de diretórios
* limites entre Backend, Frontend, Infraestrutura e Documentação

A estrutura definida aqui é obrigatória para toda a camada Implementation.

---

# Princípios

## Separação por Responsabilidade

O repositório deve refletir a arquitetura aprovada.

Cada diretório possui responsabilidade única.

Evitar:

* diretórios genéricos
* agrupamentos sem ownership
* código compartilhado sem contexto definido

---

## Alinhamento Arquitetural

A estrutura deve seguir:

```text
Domain
Architecture
Solution Design
```

e não decisões locais de implementação.

---

## Evolução Controlada

Novos diretórios de primeiro nível somente podem ser criados mediante justificativa arquitetural.

---

# Estrutura Raiz

```text
portal-comunicacao/

├── backend/
├── frontend/
├── infra/
├── docs/
├── scripts/
├── .cursor/
├── .github/
├── .gitignore
├── README.md
```

---

# Diretório Docs

## Objetivo

Centralizar documentação do projeto.

---

## Estrutura

```text
docs/

├── discovery/
├── domain/
├── architecture/
├── solution-design/
├── implementation/
├── delivery/
└── decisions/
```

---

## Responsabilidades

### discovery

Documentação AS-IS.

---

### domain

Conhecimento de negócio.

---

### architecture

ADRs e arquitetura alvo.

---

### solution-design

Materialização arquitetural.

---

### implementation

Documentação técnica da implementação.

---

### delivery

Planejamento operacional.

---

### decisions

Registros complementares aprovados.

---

# Diretório Backend

## Objetivo

Implementação do Backend Java.

---

## Tecnologia

```text
Java
Spring Boot
Maven
```

---

## Estrutura

```text
backend/

├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       └── db/migration/
│   └── test/
├── pom.xml
└── README.md
```

---

# Estrutura de Código Backend

```text
src/main/java/br/com/unimedceara/portalcomunicacao
```

---

## Pacote Principal (Sprint 0)

```text
backend/src/main/java/br/com/unimedceara/portalcomunicacao/

├── PortalComunicacaoApplication.java
├── configuration/
├── infrastructure/
└── shared/
```

A Sprint 0 consolida exclusivamente a infraestrutura transversal. Bounded contexts e camadas de domínio serão adicionados incrementalmente a partir da Sprint 1.

---

# Configuration

## Objetivo

Configurações compartilhadas da aplicação na raiz do pacote principal.

Sem regras de negócio, sem dependência de Features e sem configuração de autenticação, autorização ou integrações externas.

---

## Estrutura

```text
configuration/

├── async/
│   └── AsyncConfiguration.java
├── jackson/
│   └── JacksonConfiguration.java
├── locale/
│   └── LocaleConfiguration.java
└── properties/
    ├── ApplicationProperties.java
    └── ApplicationPropertiesConfiguration.java
```

---

# Infrastructure

## Objetivo

Infraestrutura técnica compartilhada da aplicação.

---

## Estrutura

```text
infrastructure/

└── logging/
    ├── CorrelationIdFilter.java
    ├── CorrelationIdGenerator.java
    ├── LoggingConfiguration.java
    ├── LoggingConstants.java
    └── MdcUtils.java
```

A observabilidade transversal reside em `infrastructure/logging/`, e não em `shared/`.

---

# Shared

## Objetivo

Componentes compartilhados entre bounded contexts.

Uso restrito.

---

## Estrutura

```text
shared/

├── constants/
├── dto/
├── exception/
├── util/
└── validation/
    ├── annotation/
    ├── group/
    ├── payload/
    └── validator/
```

---

## Restrições

É proibido colocar regras de negócio em:

```text
shared/
```

---

## Recursos

```text
backend/src/main/resources/

├── application.yaml
├── application-local.yaml
├── application-dev.yaml
├── application-hml.yaml
└── db/
    └── migration/
        └── V1__baseline.sql
```

---

# Diretório Frontend

## Objetivo

Implementação da aplicação Web.

---

## Tecnologia

```text
Vue
TypeScript
Vite
```

---

## Estrutura

```text
frontend/

├── src/
├── public/
├── docs/
├── tests/
├── docker/
├── package.json
└── README.md
```

---

# Estrutura Frontend

```text
src/

├── app/
├── modules/
├── shared/
├── router/
├── layouts/
├── pages/
├── assets/
└── services/
```

---

# Modules

Organização por bounded context.

```text
modules/

├── organization/
├── accesscontrol/
├── documentmanagement/
└── internalcommunication/
```

---

# Shared Frontend

```text
shared/

├── components/
├── composables/
├── utils/
├── validation/
└── security/
```

---

## Restrições

Não colocar regras de negócio em:

```text
shared/
```

---

# Diretório Infra

## Objetivo

Infraestrutura executável.

---

## Estrutura

```text
infra/

├── local/
├── dev/
├── hml/
├── prod/
├── nginx/
├── storage/
├── monitoring/
└── scripts/
```

---

# Ambientes

## Local

```text
infra/local/
```

Recursos para desenvolvimento local.

---

## Dev

```text
infra/dev/
```

Ambiente compartilhado de desenvolvimento.

---

## Hml

```text
infra/hml/
```

Homologação.

---

## Prod

```text
infra/prod/
```

Produção.

---

# Estrutura Docker

```text
infra/

├── local/docker-compose.yml
├── dev/docker-compose.yml
├── hml/docker-compose.yml
└── prod/docker-compose.yml
```

---

# Diretório Scripts

## Objetivo

Automações operacionais.

---

## Estrutura

```text
scripts/

├── database/
├── migration/
├── backup/
├── restore/
└── utilities/
```

---

## Restrições

Não armazenar scripts temporários.

Não armazenar testes manuais.

---

# Diretório .cursor

## Objetivo

Governança dos agentes IA.

---

## Estrutura

```text
.cursor/

└── rules/
```

---

# Estrutura de Rules

```text
.cursor/rules/

├── process/
├── architecture/
├── implementation/
├── security/
└── delivery/
```

---

# Diretório .github

## Objetivo

Automação de integração contínua.

---

## Estrutura

```text
.github/

├── workflows/
├── pull_request_template.md
└── CODEOWNERS
```

---

# Convenções de Nomeação

## Diretórios

Utilizar:

```text
lowercase
```

Exemplo:

```text
documentmanagement
accesscontrol
organization
```

---

## Arquivos

Utilizar:

```text
kebab-case
```

Exemplo:

```text
user-service.java
document-controller.java
security-config.java
```

---

## Classes Java

Utilizar:

```text
PascalCase
```

Exemplo:

```java
DocumentController
UserService
PermissionValidator
```

---

# Estrutura Proibida

Não criar:

```text
common/
commons/
misc/
temp/
utils-global/
new/
test2/
backup/
old/
legacy2/
```

Esses diretórios geram acoplamento e perda de ownership.

---

# Critério de Evolução

Mudanças estruturais somente quando:

* novo ADR aprovado
* novo bounded context aprovado
* alteração arquitetural formal

---

# Critério de Conformidade

Toda implementação deve ser rastreável a:

```text
docs/domain
docs/architecture
docs/solution-design
```

A estrutura física do repositório deve refletir a arquitetura aprovada e não preferências individuais de desenvolvedores.

---

# Conclusão

Esta estrutura estabelece a organização oficial do repositório do Portal de Comunicação.

Qualquer divergência deve ser tratada como desvio arquitetural e submetida ao processo de governança definido pela camada Architecture.
