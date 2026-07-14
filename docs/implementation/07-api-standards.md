# API Standards

## Documento

```text
docs/implementation/07-api-standards.md
```

---

# Objetivo

Definir os padrões oficiais para construção, evolução e governança das APIs do Portal de Comunicação.

Este documento estabelece:

* convenções REST
* versionamento
* contratos
* DTOs
* paginação
* filtros
* ordenação
* autenticação
* autorização
* tratamento de erros
* observabilidade

---

# Escopo

Aplica-se a:

```text
APIs REST
Controllers
DTOs
Requests
Responses
Integrações Frontend ↔ Backend
```

---

# Princípios

## API First

Toda API deve existir formalmente em:

```text
06-integration-contracts.md
```

antes da implementação.

---

## Contrato Estável

Consumidores não devem ser impactados por alterações internas do Backend.

---

## Consistência

Todas as APIs devem seguir os mesmos padrões.

Evitar exceções por módulo.

---

## Versionamento Explícito

Toda API deve ser versionada.

---

# Estrutura Base

## Prefixo

Formato obrigatório:

```text
/api/v1
```

---

## Exemplos

```text
/api/v1/documents
/api/v1/folders
/api/v1/permissions
/api/v1/notifications
```

---

# Convenções REST

## Criar

```http
POST /api/v1/documents
```

---

## Consultar Lista

```http
GET /api/v1/documents
```

---

## Consultar Item

```http
GET /api/v1/documents/{id}
```

---

## Atualizar

```http
PUT /api/v1/documents/{id}
```

---

## Atualização Parcial

```http
PATCH /api/v1/documents/{id}
```

---

## Remover

```http
DELETE /api/v1/documents/{id}
```

---

# Recursos

## Utilizar Substantivos

Correto:

```text
documents
folders
users
permissions
notifications
```

---

## Evitar Verbos

Incorreto:

```text
createDocument
findFolder
removePermission
```

---

# Nomenclatura

## Paths

Utilizar:

```text
kebab-case
```

---

Exemplo:

```text
document-shares
user-permissions
```

---

# DTO Standards

## Requests

Formato:

```java
CreateDocumentRequest
UpdateFolderRequest
GrantPermissionRequest
```

---

## Responses

Formato:

```java
DocumentResponse
FolderResponse
PermissionResponse
```

---

## Regra

DTOs são contratos.

Nunca expor entidades JPA diretamente.

---

# Response Pattern

## ApiResponse

Envelope padrão para respostas de sucesso.

Classe: `br.com.unimedceara.portalcomunicacao.shared.dto.ApiResponse`

| Campo       | Tipo      | Obrigatório | Descrição                          |
| ----------- | --------- | ----------- | ---------------------------------- |
| `timestamp` | `Instant` | Sim         | Momento da resposta (ISO-8601)     |
| `success`   | `boolean` | Sim         | Indica sucesso da operação         |
| `message`   | `String`  | Não         | Mensagem descritiva (omitida se nula) |
| `data`      | `T`       | Não         | Payload da resposta                |

---

## Exemplo — Sucesso com dados

```json
{
  "timestamp": "2026-07-08T14:00:00Z",
  "success": true,
  "data": {
    "id": 1,
    "title": "Documento exemplo"
  }
}
```

---

## Exemplo — Sucesso com mensagem

```json
{
  "timestamp": "2026-07-08T14:00:00Z",
  "success": true,
  "message": "Documento criado com sucesso",
  "data": {
    "id": 1
  }
}
```

---

# Response Collection

## PageResponse

Estrutura independente para respostas paginadas.

Classe: `br.com.unimedceara.portalcomunicacao.shared.dto.PageResponse`

| Campo           | Tipo       | Descrição                              |
| --------------- | ---------- | -------------------------------------- |
| `content`       | `List<T>`  | Elementos da página atual              |
| `page`          | `int`      | Número da página (base zero)           |
| `size`          | `int`      | Quantidade de elementos por página     |
| `totalElements` | `long`     | Total de elementos disponíveis         |
| `totalPages`    | `int`      | Total de páginas                       |
| `first`         | `boolean`  | Indica se é a primeira página          |
| `last`          | `boolean`  | Indica se é a última página            |

---

```json
{
  "content": [
    { "id": 1, "title": "Documento A" },
    { "id": 2, "title": "Documento B" }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 42,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

---

## Constantes de Paginação

Definidas em `ApiConstants`:

| Constante         | Valor  |
| ----------------- | ------ |
| `DEFAULT_PAGE`    | 0      |
| `DEFAULT_SIZE`    | 20     |
| `MAX_PAGE_SIZE`   | 100    |
| `API_BASE_PATH`   | /api/v1 |

---

# Paginação

## Parâmetros

```text
page
size
sort
```

---

## Exemplo

```http
GET /api/v1/documents?page=0&size=20
```

A paginação utiliza base zero (`page=0` é a primeira página).

---

# Ordenação

## Exemplo

```http
GET /api/v1/documents?sort=name,asc
```

---

## Múltiplos Campos

```http
GET /api/v1/documents?sort=status,asc&sort=createdAt,desc
```

---

# Filtros

## Convenção

Utilizar query parameters.

---

Exemplo:

```http
GET /api/v1/documents?status=ACTIVE
```

---

```http
GET /api/v1/documents?ownerId=123
```

---

## Evitar

Endpoints específicos para cada filtro.

---

# Upload de Arquivos

## Endpoint

```http
POST /api/v1/documents/{id}/content
```

---

## Content-Type

```text
multipart/form-data
```

---

## Regra

Binários nunca trafegam em JSON.

---

# Download de Arquivos

## Endpoint

```http
GET /api/v1/documents/{id}/content
```

---

## Controle

Autorização obrigatória.

---

# Status HTTP

## Sucesso

| Código | Uso                      |
| ------ | ------------------------ |
| 200    | Consulta                 |
| 201    | Criação                  |
| 202    | Processamento assíncrono |
| 204    | Exclusão                 |

---

## Cliente

| Código | Uso              |
| ------ | ---------------- |
| 400    | Request inválido |
| 401    | Não autenticado  |
| 403    | Não autorizado   |
| 404    | Não encontrado   |
| 409    | Conflito         |
| 422    | Regra de negócio / validação funcional |

---

## Servidor

| Código | Uso                  |
| ------ | -------------------- |
| 500    | Erro interno         |
| 502    | Integração           |
| 503    | Serviço indisponível |

---

# Tratamento de Erros

## ErrorResponse

Classe: `br.com.unimedceara.portalcomunicacao.shared.dto.ErrorResponse`

| Campo       | Tipo      | Descrição                              |
| ----------- | --------- | -------------------------------------- |
| `timestamp` | `Instant` | Momento do erro (ISO-8601)             |
| `status`    | `int`     | Código HTTP do erro                    |
| `error`     | `String`  | Código identificador do erro           |
| `message`   | `String`  | Mensagem descritiva do erro            |
| `path`      | `String`  | Caminho da requisição                  |

---

## Exemplo — Erro genérico

```json
{
  "timestamp": "2026-07-08T14:00:00Z",
  "status": 404,
  "error": "RESOURCE_NOT_FOUND",
  "message": "Recurso não encontrado",
  "path": "/api/v1/documents/1"
}
```

---

## ValidationErrorResponse

Especialização de `ErrorResponse` para erros de validação.

Classe: `br.com.unimedceara.portalcomunicacao.shared.dto.ValidationErrorResponse`

| Campo adicional | Tipo                          | Descrição                    |
| --------------- | ----------------------------- | ---------------------------- |
| `errors`        | `List<FieldValidationError>`  | Erros por campo              |

---

## FieldValidationError

Classe: `br.com.unimedceara.portalcomunicacao.shared.dto.FieldValidationError`

| Campo     | Tipo     | Descrição                    |
| --------- | -------- | ---------------------------- |
| `field`   | `String` | Nome do campo com erro       |
| `message` | `String` | Mensagem descritiva do erro  |

---

## Exemplo — Erro de validação

```json
{
  "timestamp": "2026-07-08T14:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "path": "/api/v1/documents",
  "errors": [
    { "field": "title", "message": "must not be blank" },
    { "field": "description", "message": "size must be between 1 and 500" }
  ]
}
```

---

## Mapeamento Exceção → HTTP

Tratado por `GlobalExceptionHandler`:

| Exceção                          | HTTP | Código `error`          |
| -------------------------------- | ---- | ----------------------- |
| `ResourceNotFoundException`      | 404  | `RESOURCE_NOT_FOUND`    |
| `UnauthorizedException`          | 401  | `UNAUTHORIZED`          |
| `ForbiddenException`             | 403  | `FORBIDDEN`             |
| `ConflictException`              | 409  | `CONFLICT`              |
| `ValidationException`            | 422  | `VALIDATION_ERROR`      |
| `BusinessException`              | 422  | `ex.getErrorCode()`     |
| `MethodArgumentNotValidException`| 400  | `VALIDATION_ERROR`      |
| `ConstraintViolationException`   | 400  | `VALIDATION_ERROR`      |
| `Exception` (genérica)           | 500  | `INTERNAL_SERVER_ERROR` |

---

# Código de Erro

Formato:

```text
UPPER_SNAKE_CASE
```

---

Exemplos:

```text
DOCUMENT_NOT_FOUND
USER_NOT_AUTHORIZED
INVALID_DOCUMENT_STATUS
```

---

# Autenticação

## Responsabilidade

Controle de Acesso.

---

## Fluxo

```text
Frontend
→ Backend
→ Zimbra
→ Backend
→ Frontend
```

---

# Autorização

## Regra

Executada exclusivamente pelo Backend.

Conforme ADR-005.

---

## Proibido

Delegar autorização ao Frontend.

---

# Auditoria

Operações sensíveis devem registrar:

```text
usuário
ação
recurso
data
resultado
```

---

# Observabilidade

## Logs

Toda requisição deve possuir Correlation ID propagado via header e MDC.

Detalhes em `09-observability-standards.md`.

---

# Correlation ID

Obrigatório.

Implementado por `CorrelationIdFilter` em `infrastructure/logging/`.

---

## Header

```http
X-Correlation-Id
```

O filtro reutiliza o valor recebido ou gera um UUID quando ausente.

---

# Idempotência

## Operações Críticas

Devem suportar idempotência quando aplicável.

---

## Header

```http
Idempotency-Key
```

---

# Compatibilidade

## Regra

Mudanças incompatíveis exigem:

```text
nova versão
```

---

## Exemplo

```text
v1 → v2
```

---

# Documentação

Toda API deve possuir:

* endpoint
* request
* response
* códigos de erro
* exemplos

---

# Critérios de Conformidade

Toda API deve responder:

## Possui contrato?

```text
SIM
```

---

## Possui DTO próprio?

```text
SIM
```

---

## Possui tratamento de erro?

```text
SIM
```

---

## Possui autenticação?

```text
SIM
```

quando aplicável.

---

## Possui auditoria?

```text
SIM
```

quando aplicável.

---

# Não Conformidades

São considerados desvios:

* expor entidades JPA
* retornar exceções internas
* endpoints sem versionamento
* upload em JSON
* autenticação paralela
* autorização no Frontend
* contratos não documentados

---

# Conclusão

As APIs do Portal de Comunicação devem ser consistentes, versionadas, observáveis e governadas por contratos explícitos.

Toda evolução deve preservar compatibilidade, respeitar os ADRs aprovados e garantir que Frontend e Backend permaneçam desacoplados através de contratos estáveis.
