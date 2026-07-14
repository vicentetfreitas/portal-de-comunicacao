# API Implementation

**Fonte normativa MVP:** `docs/audit/10-mvp-consolidation-audit.md`  
**Pré-requisito:** `docs/governance/history/phase1-frontend-construction-report.md`

## Objetivo

Definir os padrões, diretrizes e regras para implementação das APIs REST do Portal de Comunicação.

Este documento estabelece como os contratos expostos externamente devem ser projetados, implementados, versionados, documentados e mantidos.

---

# Escopo

Esta documentação cobre:

* APIs REST
* Contratos HTTP
* Controllers
* DTOs
* Mapeamentos
* Validações
* Tratamento de erros
* Versionamento
* Documentação OpenAPI

Não cobre:

* Regras de negócio
* Persistência
* Integrações externas
* Segurança da aplicação

Esses temas possuem documentos específicos.

---

# Arquitetura da Camada API

A API representa apenas a camada de exposição.

Ela não contém regras de negócio.

Fluxo esperado:

```text
HTTP Request
    ↓
Controller
    ↓
Request DTO
    ↓
Application Service
    ↓
Domain
    ↓
Application Service
    ↓
Response DTO
    ↓
HTTP Response
```

---

# Estrutura de Diretórios

```text
interfaces
└── rest
    ├── controller
    ├── request
    ├── response
    ├── mapper
    ├── exception
    └── documentation
```

---

# Convenções de Endpoints

## Regras Gerais

Utilizar substantivos.

Não utilizar verbos.

### Correto

```http
GET /comunicados
GET /comunicados/{id}
POST /comunicados
```

### Incorreto

```http
GET /getComunicados
POST /createComunicado
```

---

# Convenções de URI

## Recursos MVP

```http
/api/v1/comunicados
```

```http
/api/v1/notifications
```

```http
/api/v1/documents
```

**Rastreabilidade:** FEATURE-041 (Comunicados), FEATURE-040 (Notificações), FEATURE-030 (Documentos).

---

## Recursos Obsoletos (fora do MVP)

> **OBSOLETO** — removidos por `docs/audit/10-mvp-consolidation-audit.md`. Não implementar.

```http
/api/v1/campaigns          # FEATURE-070 removida — C-002
/api/v1/messages           # FEATURE-046 removida — C-004
/api/v1/campaigns/{campaignId}/messages
```

---

# Versionamento

Toda API deve ser versionada.

Padrão:

```http
/api/v1
```

Exemplo:

```http
/api/v1/comunicados
```

Nova quebra de contrato:

```http
/api/v2/comunicados
```

---

# Métodos HTTP

## GET

Consulta de recursos.

```http
GET /comunicados
```

---

## POST

Criação de recursos.

```http
POST /comunicados
```

---

## PUT

Atualização completa.

```http
PUT /comunicados/{id}
```

---

## PATCH

Atualização parcial.

```http
PATCH /comunicados/{id}
```

---

## DELETE

Remoção lógica ou física.

```http
DELETE /comunicados/{id}
```

---

# Controllers

## Responsabilidades

Controllers devem:

* Receber requisições
* Validar DTOs
* Invocar Application Services
* Retornar respostas

Controllers não devem:

* Executar regras de negócio
* Realizar acesso a banco
* Chamar integrações externas

---

## Exemplo

```java
@RestController
@RequestMapping("/api/v1/comunicados")
@RequiredArgsConstructor
public class ComunicadoController {

    private final CreateComunicadoUseCase useCase;

    @PostMapping
    public ResponseEntity<ComunicadoResponse> create(
            @Valid @RequestBody ComunicadoRequest request) {

        var response = useCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
```

---

# DTOs

## Request DTO

Responsável pela entrada da API.

```java
public record ComunicadoRequest(
        String title,
        String description
) {}
```

---

## Response DTO

Responsável pela saída da API.

```java
public record ComunicadoResponse(
        UUID id,
        String title,
        Instant createdAt
) {}
```

---

# Validação

Toda validação de entrada deve utilizar Bean Validation.

Exemplo:

```java
public record ComunicadoRequest(

        @NotBlank
        @Size(max = 150)
        String title,

        @NotBlank
        String description

) {}
```

---

# Mapeamento

Utilizar MapStruct.

Não realizar mapeamentos manuais repetitivos.

Exemplo:

```java
@Mapper(componentModel = "spring")
public interface ComunicadoMapper {

    Comunicado toDomain(
            ComunicadoRequest request);

    ComunicadoResponse toResponse(
            Comunicado comunicado);

}
```

---

# Paginação

Listagens devem utilizar paginação.

Exemplo:

```http
GET /comunicados?page=0&size=20
```

---

## Resposta

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

---

# Ordenação

Suporte obrigatório.

Exemplo:

```http
GET /comunicados?sort=createdAt,desc
```

---

# Filtros

Filtros devem ser explícitos.

Exemplo:

```http
GET /comunicados?status=ACTIVE
```

---

# Tratamento de Erros

Todas as APIs devem retornar erros padronizados.

---

## Estrutura

```json
{
  "timestamp": "2026-01-10T10:30:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "path": "/api/v1/comunicados"
}
```

---

# Status HTTP

## 200

Operação realizada com sucesso.

---

## 201

Recurso criado.

---

## 204

Sem conteúdo.

---

## 400

Erro de validação.

---

## 401

Não autenticado.

---

## 403

Não autorizado.

---

## 404

Recurso não encontrado.

---

## 409

Conflito de negócio.

---

## 422

Regra de negócio inválida.

---

## 500

Erro interno.

---

# Exception Handler Global

Implementação obrigatória.

Exemplo:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

}
```

---

# Idempotência

Operações críticas devem suportar idempotência.

Exemplo:

```http
Idempotency-Key:
```

Utilizar principalmente para:

* Publicação de comunicados
* Upload de documentos
* Disparo de notificações

---

## Obsoleto (fora do MVP)

> Não implementar — removidos por `docs/audit/10-mvp-consolidation-audit.md`.

* Idempotência para campanhas (`FEATURE-070`)
* Idempotência para mensagens (`FEATURE-046`)
* Endpoints `/api/v1/communications`, `/api/v1/campaigns`, `/api/v1/messages`

---

# Correlation ID

Todas as requisições devem possuir rastreabilidade.

Header:

```http
X-Correlation-Id
```

Se não informado, deve ser gerado automaticamente.

---

# Observabilidade

Registrar:

* Endpoint
* Tempo de execução
* Status HTTP
* Correlation ID

Nunca registrar:

* Senhas
* Tokens
* Dados sensíveis

---

# OpenAPI

Toda API deve ser documentada.

---

## Configuração

```java
@OpenAPIDefinition(
        info = @Info(
                title = "Portal de Comunicação API",
                version = "v1"
        )
)
```

---

# Swagger

Disponível em:

```http
/swagger-ui.html
```

ou

```http
/swagger-ui/index.html
```

---

# Boas Práticas

## Fazer

* APIs pequenas
* Contratos estáveis
* DTOs específicos
* Versionamento explícito
* Validação de entrada
* Paginação
* Documentação OpenAPI

---

## Evitar

* Expor entidades JPA
* Retornar Exception Stack Trace
* Lógica de negócio em Controllers
* DTOs genéricos
* Endpoints sem versionamento

---

# Critérios de Aceite

A implementação da API será considerada aderente quando:

* Todos os endpoints seguirem padrão REST
* Possuírem documentação OpenAPI
* Utilizarem DTOs dedicados
* Possuírem validação de entrada
* Possuírem tratamento global de exceções
* Possuírem rastreabilidade via Correlation ID
* Possuírem paginação para consultas
* Não contenham regras de negócio nos Controllers
* Sejam compatíveis com os padrões definidos na camada Architecture e Solution Design
