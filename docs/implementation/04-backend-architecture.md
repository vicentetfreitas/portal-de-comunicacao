# Backend Architecture

## Documento

```text
docs/implementation/04-backend-architecture.md
```

---

# Objetivo

Definir a arquitetura interna do Backend do Portal de Comunicação.

Este documento materializa os ADRs aprovados e estabelece como os bounded contexts serão implementados dentro do monólito modular.

Define:

* organização dos módulos
* comunicação entre contextos
* estrutura interna
* casos de uso
* persistência
* integrações
* eventos
* transações
* observabilidade

Não define:

* regras de negócio
* contratos de negócio
* infraestrutura de deployment

Esses assuntos pertencem às camadas anteriores.

---

# Visão Geral

## Arquitetura Adotada

O Backend será implementado como:

```text
Monólito Modular
```

Conforme:

```text
ADR-001
```

---

## Objetivo

Permitir:

* baixo acoplamento
* alta coesão
* evolução incremental
* simplicidade operacional

sem introduzir complexidade de microsserviços.

---

# Stack Oficial

## Linguagem

```text
Java 25
```

---

## Framework

```text
Spring Boot 4
```

---

## Build

```text
Maven
```

---

## Persistência

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

# Estrutura Geral

```text
backend/

└── src/main/java/br/com/unimed/pdc

    ├── bootstrap
    ├── shared

    ├── organization
    ├── accesscontrol
    ├── documentmanagement
    └── internalcommunication
```

---

# Bounded Contexts

## Organização Corporativa

Responsável por:

* singulares
* áreas
* equipes
* colaboradores
* vínculos

Owner funcional:

```text
Organização Corporativa
```

---

## Controle de Acesso

Responsável por:

* autenticação
* sessão
* autorização
* papéis
* escopos

Owner funcional:

```text
Controle de Acesso
```

---

## Gestão Documental

Responsável por:

* documentos
* pastas
* compartilhamentos
* categorias

Owner funcional:

```text
Gestão Documental
```

---

## Comunicação Interna

Responsável por:

* notificações
* comunicados
* canais

Owner funcional:

```text
Comunicação Interna
```

---

# Estrutura Interna dos Contextos

Todos os contextos devem seguir:

```text
<context>

├── application
├── domain
├── infrastructure
└── interfaces
```

---

# Domain Layer

## Responsabilidade

Contém:

* entidades
* value objects
* regras de domínio
* eventos de domínio

---

## Proibições

Não conhecer:

* Spring
* banco
* REST
* storage
* DTOs

---

# Application Layer

## Responsabilidade

Implementar casos de uso.

Exemplos:

```text
CreateDocumentUseCase
AuthenticateUserUseCase
PublishNotificationUseCase
```

---

## Responsabilidades

* orquestração
* validação de fluxo
* transações

---

## Proibições

Não acessar banco diretamente.

---

# Infrastructure Layer

## Responsabilidade

Implementar adaptadores.

Exemplos:

```text
JPA
Storage
Zimbra
Email
Webhook
```

---

## Componentes

```text
repository
integration
storage
configuration
```

---

# Interfaces Layer

## Responsabilidade

Exposição externa.

Exemplos:

```text
REST Controllers
Request DTOs
Response DTOs
Mappers
```

---

## Regra

Nenhuma regra de negócio pode existir nesta camada.

---

# Comunicação Entre Contextos

## Princípio

Bounded Contexts não acessam internamente as entidades de outros contextos.

---

## Permitido

```text
Identificadores
Contratos
Interfaces
```

---

## Proibido

```text
Repository de outro contexto
Entidade de outro contexto
Tabela de outro contexto
```

---

# Exemplo

Correto:

```text
Document
→ ownerId
→ areaId
→ permissionId
```

---

Incorreto:

```text
Document
→ UserEntity
→ TeamEntity
```

---

# Shared Kernel

## Objetivo

Componentes técnicos compartilhados.

---

## Estrutura

```text
shared

├── security
├── exception
├── auditing
├── validation
├── observability
└── configuration
```

---

## Restrições

Não conter:

* regras de negócio
* entidades funcionais
* casos de uso

---

# Persistência

## Princípio

Ownership único.

Conforme:

```text
07-data-ownership.md
```

---

## Regras

Somente o owner grava.

Leituras compartilhadas apenas via contrato.

---

# Repositories

Devem existir apenas em:

```text
infrastructure/repository
```

---

## Proibido

```text
Controller
UseCase
```

acessarem EntityManager diretamente.

---

# Transações

## Responsabilidade

Application Layer.

---

## Regra

Transações devem encapsular:

```text
um caso de uso
```

---

## Evitar

Transações distribuídas entre contextos.

---

# Integrações Externas

## Zimbra

Responsável:

```text
accesscontrol
```

---

## Storage

Responsável:

```text
documentmanagement
```

---

## Webhook

Responsável:

```text
internalcommunication
```

---

# Eventos de Domínio

## Objetivo

Reduzir acoplamento.

---

## Exemplos

```text
DocumentCreated
PermissionGranted
NotificationSent
```

---

# Regras

Eventos representam fatos consumados.

Nunca comandos.

---

# Observabilidade

Todos os casos de uso devem produzir:

```text
logs estruturados
```

---

## Exemplo

```json
{
  "event": "DOCUMENT_CREATED",
  "documentId": "123",
  "userId": "456"
}
```

---

# Tratamento de Erros

## Estrutura

```text
shared/exception
```

---

## Obrigatório

Todas as exceções devem possuir:

* código
* mensagem
* contexto

---

# Segurança

## Autenticação

Responsável:

```text
Controle de Acesso
```

---

## Autorização

Executada exclusivamente no Backend.

Conforme:

```text
ADR-005
```

---

## Proibido

Validação de permissão apenas no Frontend.

---

# APIs

## Estrutura

```text
/api/v1
```

---

## Controllers

Devem:

* receber request
* validar request
* chamar use case
* retornar response

---

## Não Devem

* acessar banco
* implementar regras
* realizar integrações

---

# Testes

## Unitários

Cobrir:

* domínio
* use cases

---

## Integração

Cobrir:

* banco
* storage
* integrações externas

---

## Contrato

Cobrir:

* requests
* responses

---

# Critérios Arquiteturais

Toda implementação deve responder:

## Ownership definido?

```text
SIM
```

---

## Contexto definido?

```text
SIM
```

---

## Caso de uso identificado?

```text
SIM
```

---

## Contrato existente?

```text
SIM
```

---

## ADR respeitado?

```text
SIM
```

---

# Critérios de Não Conformidade

São considerados desvios arquiteturais:

* regras em controllers
* acesso direto entre contextos
* entidades compartilhadas
* repositories fora da infraestrutura
* autorização fora do backend
* lógica de negócio no shared
* dependências circulares

---

# Conclusão

O Backend do Portal de Comunicação será implementado como um monólito modular orientado por bounded contexts, com separação clara entre domínio, aplicação, infraestrutura e interfaces.

A arquitetura busca maximizar coesão, minimizar acoplamento e garantir aderência aos ADRs aprovados, permitindo evolução incremental da solução sem necessidade de fragmentação prematura em microsserviços.
