# 02-development-standards.md

# Development Standards

## Objetivo

Este documento define os padrões obrigatórios de desenvolvimento do Portal de Comunicação Digital (PCD).

Seu objetivo é garantir:

* Consistência arquitetural
* Padronização do código
* Facilidade de manutenção
* Escalabilidade
* Qualidade técnica
* Redução de dívida técnica

Todos os artefatos de construção devem respeitar as regras aqui estabelecidas.

---

# Princípios Arquiteturais

## Domain Driven Design (DDD)

O projeto deve ser organizado por domínio de negócio.

Não é permitido estruturar o sistema por camadas técnicas globais.

❌ Não permitido

```text
controllers/
services/
repositories/
entities/
```

✅ Obrigatório

```text
organization/
accesscontrol/
documentmanagement/
internalcommunication/
```

Cada bounded context deve possuir sua própria estrutura interna.

---

## Clean Architecture

As regras de negócio não podem depender de frameworks.

Dependências devem apontar para dentro.

Fluxo permitido:

```text
Controller
    ↓
Application
    ↓
Domain
```

Fluxo proibido:

```text
Domain
    ↓
Controller
```

---

## Arquitetura Hexagonal

Toda integração externa deve ocorrer através de portas e adaptadores.

### Porta

```java
public interface NotificationGateway {
    void send(Notification notification);
}
```

### Adaptador

```java
public class EmailNotificationGateway
        implements NotificationGateway {
}
```

Regra:

Nenhuma regra de negócio pode depender diretamente de:

* Banco de dados
* APIs externas
* Frameworks
* Mensageria

---

# Organização do Repositório

## Backend

```text
backend/
├── src/main/java/br/com/unimed/pcd
│
├── bootstrap
│
├── organization
├── accesscontrol
├── documentmanagement
└── internalcommunication
```

---

## Frontend

```text
frontend/
├── src
│   ├── pages
│   ├── layouts
│   ├── components
│   ├── services
│   ├── stores
│   └── router
```

---

# Convenções de Código

## Idioma

Código fonte deve ser escrito em inglês.

### Permitido

```java
Document
Notification
User
Organization
```

### Proibido

```java
Documento
Notificacao
Usuario
Empresa
```

---

## Idioma de Negócio

Termos de negócio podem permanecer em português apenas:

* documentação
* OpenAPI descriptions
* mensagens para usuários

---

# Convenção de Nomes

## Classes

```java
DocumentService
CreateDocumentUseCase
DocumentRepository
DocumentController
```

PascalCase obrigatório.

---

## Métodos

```java
createDocument()
findById()
publishDocument()
```

camelCase obrigatório.

---

## Constantes

```java
MAX_UPLOAD_SIZE
DEFAULT_PAGE_SIZE
```

UPPER_SNAKE_CASE obrigatório.

---

# Estrutura de Pacotes

Cada bounded context deve seguir:

```text
documentmanagement
│
├── domain
│   ├── entity
│   ├── valueobject
│   ├── service
│   └── repository
│
├── application
│   ├── usecase
│   ├── dto
│   └── mapper
│
├── infrastructure
│   ├── persistence
│   ├── integration
│   └── configuration
│
└── interfaces
    ├── rest
    └── event
```

---

# APIs REST

## Versionamento

Obrigatório:

```text
/api/v1
```

Exemplo:

```text
/api/v1/documents
/api/v1/communications
/api/v1/users
```

---

## Convenções HTTP

| Operação            | Método |
| ------------------- | ------ |
| Criar               | POST   |
| Consultar           | GET    |
| Atualizar           | PUT    |
| Atualização Parcial | PATCH  |
| Remover             | DELETE |

---

## Status Codes

| Código | Uso                 |
| ------ | ------------------- |
| 200    | Sucesso             |
| 201    | Criado              |
| 204    | Sem conteúdo        |
| 400    | Requisição inválida |
| 401    | Não autenticado     |
| 403    | Sem permissão       |
| 404    | Não encontrado      |
| 409    | Conflito            |
| 422    | Erro de negócio     |
| 500    | Erro interno        |

---

# DTOs

DTOs devem utilizar Java Records.

Exemplo:

```java
public record CreateDocumentRequest(
        String title,
        String content
) {
}
```

---

# Persistência

## ORM

Obrigatório:

```text
Spring Data JPA
Hibernate
```

---

## Banco

Obrigatório:

```text
Oracle Database
```

---

## Evolução do schema (DBA)

Obrigatório (DEC-DB-019):

```text
Baseline DDL — docs/database/ddl/
```

Toda alteração estrutural deve ser refletida em script DDL versionado, executado pelo DBA.

---

# Tratamento de Exceções

Obrigatório:

```java
@RestControllerAdvice
```

Estrutura padrão:

```json
{
  "timestamp": "",
  "code": "",
  "message": "",
  "details": []
}
```

---

# Logs

## Framework

```text
SLF4J
Logback
```

---

## Regras

Nunca registrar:

* Senhas
* Tokens
* Dados sensíveis
* Informações pessoais protegidas

---

# Segurança

## Autenticação

```text
JWT
```

---

## Autorização

```text
RBAC
```

Role Based Access Control.

---

## Senhas

Obrigatório:

```text
BCrypt
```

---

# Testes

## Cobertura mínima

| Tipo           | Cobertura |
| -------------- | --------- |
| Domain         | 90%       |
| Application    | 80%       |
| Infrastructure | 70%       |
| Global         | 80%       |

---

## Testes Unitários

Ferramentas:

```text
JUnit 5
Mockito
```

---

## Testes de Integração

Ferramentas:

```text
Spring Test
Testcontainers
```

---

## Testes E2E

Frontend:

```text
Playwright
```

---

# Qualidade de Código

## Requisitos

Todo Pull Request deve:

* Compilar
* Executar testes
* Passar análise estática
* Atualizar documentação quando necessário

---

## Proibido

* Código comentado
* TODO sem ticket
* Métodos acima de 100 linhas
* Classes acima de 1000 linhas
* Duplicação de lógica

---

# Observabilidade

Obrigatório:

```text
Spring Boot Actuator
Micrometer
Prometheus
Grafana
```

---

# Documentação

Toda funcionalidade implementada deve atualizar:

* OpenAPI
* ADRs quando necessário
* Diagramas afetados
* Backlog
* Artefatos de governança

---

# Critérios de Aceitação

Uma implementação será considerada concluída apenas quando:

* Código implementado
* Testes aprovados
* Documentação atualizada
* Pipeline aprovado
* Revisão concluída

---

# Conformidade

O não cumprimento deste documento caracteriza desvio arquitetural e deve ser registrado em auditoria técnica.

---

# Status

| Item                   | Status      |
| ---------------------- | ----------- |
| DDD                    | Obrigatório |
| Clean Architecture     | Obrigatório |
| Hexagonal Architecture | Obrigatório |
| Java 25                | Obrigatório |
| Spring Boot 4.x        | Obrigatório |
| Oracle                 | Obrigatório |
| Baseline DDL (DBA)     | Obrigatório — DEC-DB-019 |
| Vue 3                  | Obrigatório |
| Quasar                 | Obrigatório |
| TypeScript             | Obrigatório |
