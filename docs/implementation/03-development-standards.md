# Development Standards

## Documento

```text
docs/implementation/03-development-standards.md
```

---

# Objetivo

Definir os padrões obrigatórios de desenvolvimento do Portal de Comunicação.

Este documento estabelece:

* padrões de código
* convenções arquiteturais
* organização de módulos
* regras de APIs
* regras de persistência
* testes
* observabilidade
* segurança

Todos os componentes implementados devem seguir estes padrões.

---

# Escopo

Aplica-se a:

```text
Backend
Frontend
Infraestrutura
Scripts
Testes
```

---

# Princípios Gerais

## Clareza

O código deve ser escrito para ser lido.

Priorizar:

* simplicidade
* legibilidade
* previsibilidade

---

## Coesão

Cada componente deve possuir responsabilidade única.

Evitar:

* classes gigantes
* serviços genéricos
* componentes multifuncionais

---

## Baixo Acoplamento

Bounded Contexts devem permanecer independentes.

Comunicação entre contextos deve ocorrer por contratos explícitos.

---

## Rastreabilidade

Toda implementação deve ser rastreável a:

```text
Domain
Architecture
Solution Design
Implementation Backlog
```

---

# Stack Oficial

## Backend

```text
Java 25
Spring Boot 4
Maven
```

---

## Frontend

```text
Vue 3
TypeScript
Vite
```

---

## Banco de Dados

```text
PostgreSQL
```

---

## Armazenamento

```text
Storage dedicado
```

Conforme ADR-004.

---

## Containers

```text
Docker
Docker Compose
```

---

# Backend Standards

## Arquitetura

Estrutura obrigatória:

```text
<context>

├── application
├── domain
├── infrastructure
└── interfaces
```

---

## Domain

Responsável por:

* entidades
* regras de negócio
* value objects
* eventos de domínio

Não deve conhecer:

* banco
* HTTP
* framework

---

## Application

Responsável por:

* casos de uso
* orquestração
* transações

Não deve conter:

* regras de persistência
* controllers

---

## Infrastructure

Responsável por:

* banco
* storage
* integrações externas
* mensageria

---

## Interfaces

Responsável por:

* REST
* DTOs
* mapeamentos externos

---

# Controllers

## Regras

Controllers devem ser finos.

Permitido:

```java
request
validation
response
```

Proibido:

```java
business rules
queries complexas
persistência
```

---

# Services

Services representam casos de uso.

Exemplo:

```text
CreateDocumentUseCase
AuthenticateUserUseCase
PublishCommunicationUseCase
```

Evitar:

```text
DocumentService
UserService
GenericService
```

quando concentrarem múltiplas responsabilidades.

---

# DTO Standards

## Request

Formato:

```java
CreateDocumentRequest
```

---

## Response

Formato:

```java
DocumentResponse
```

---

## Regras

DTOs nunca devem ser reutilizados entre contextos.

Cada contrato possui DTO próprio.

---

# Exceptions

## Estrutura

```text
shared/exception
```

---

## Obrigatório

Toda exceção deve:

* possuir código
* possuir mensagem clara
* ser rastreável em logs

---

## Proibido

```java
throw new RuntimeException(...)
```

---

# Persistência

## Ownership

Seguir:

```text
07-data-ownership.md
```

---

## Regras

Somente o owner escreve.

Leitura compartilhada apenas conforme contrato.

---

## Migrations

Obrigatórias.

Não alterar schema manualmente.

---

## Nomenclatura

Tabelas:

```text
snake_case
```

Exemplo:

```text
organizational_unit
document_share
user_permission
```

---

# APIs REST

## Convenções

Recursos devem utilizar substantivos.

Correto:

```text
/api/v1/documents
/api/v1/folders
/api/v1/permissions
```

---

## Evitar

```text
/api/v1/createDocument
/api/v1/getFolder
```

---

# Versionamento

Formato:

```text
/api/v1
```

---

# Responses

Formato padrão:

```json
{
  "data": {},
  "metadata": {},
  "timestamp": ""
}
```

---

# Frontend Standards

## Estrutura

Organização por bounded context.

```text
modules/
```

---

## Componentes

Responsabilidade única.

Evitar componentes acima de:

```text
300 linhas
```

salvo exceções justificadas.

---

## Páginas

Responsáveis apenas por:

* composição
* navegação
* carregamento inicial

---

## Serviços

Responsáveis por:

* chamadas HTTP
* integração com APIs

---

## Estado

Estado deve permanecer próximo do módulo proprietário.

Evitar stores globais para regras específicas.

---

# TypeScript

Obrigatório:

```text
strict mode
```

---

## Proibido

```typescript
any
```

salvo justificativa documentada.

---

# Segurança

## Backend

Responsável por:

* autenticação
* autorização
* auditoria

---

## Frontend

Nunca toma decisão de autorização.

---

## Secrets

Nunca versionar:

```text
tokens
senhas
certificados
```

---

# Logging

## Obrigatório

Logs estruturados.

Exemplo:

```json
{
  "event": "DOCUMENT_CREATED",
  "documentId": "123",
  "userId": "456"
}
```

---

## Proibido

```java
System.out.println()
```

---

# Observabilidade

Toda funcionalidade deve possuir:

* logs
* tratamento de erro
* rastreabilidade

---

# Testes

## Unitários

Cobrir:

* regras de negócio
* validações
* casos de uso

---

## Integração

Cobrir:

* banco
* storage
* APIs
* integrações externas

---

## Contrato

Validar:

* requests
* responses
* compatibilidade

---

# Qualidade de Código

## Obrigatório

* código limpo
* métodos pequenos
* nomes explícitos
* responsabilidade única

---

## Evitar

* classes utilitárias gigantes
* helpers genéricos
* lógica duplicada

---

# Revisão de Código

Checklist obrigatório:

## Arquitetura

* respeita ADRs
* respeita bounded context

## Segurança

* sem exposição de dados
* autorização validada

## Qualidade

* testes existentes
* logs existentes
* tratamento de erro existente

## Observabilidade

* eventos rastreáveis

---

# Critérios de Conclusão

Uma implementação somente é considerada concluída quando:

* compila
* possui testes
* possui logs
* respeita ownership
* respeita contratos
* respeita ADRs
* respeita padrões deste documento

---

# Critérios de Não Conformidade

São considerados defeitos arquiteturais:

* lógica de negócio em controller
* autorização no frontend
* acesso direto entre bounded contexts
* DTO compartilhado entre contextos
* alteração manual de banco
* uso de RuntimeException genérica
* secrets versionados
* bypass de contratos

---

# Conclusão

Este documento estabelece os padrões oficiais de desenvolvimento do Portal de Comunicação.

Todos os componentes da solução devem seguir estas diretrizes para garantir consistência, manutenibilidade, rastreabilidade e aderência à arquitetura aprovada.
