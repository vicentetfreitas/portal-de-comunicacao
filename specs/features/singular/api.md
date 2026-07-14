# API Contract

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-SINGULAR |
| Feature | Singular |
| Domínio | SINGULAR |
| Recurso | singulares |
| Base Path | /api/v1/singulares |

---

# Objetivo

Este documento especifica exclusivamente o contrato funcional da API da Feature FT-SINGULAR.

Padrões corporativos herdados de `docs/implementation/07-api-standards.md` (ApiResponse, PageResponse, ErrorResponse, paginação, ordenação, autenticação, códigos HTTP).

---

# Dependências

Esta Feature herda integralmente os padrões definidos em:

```text
docs/implementation/07-api-standards.md
```

Em caso de divergência, prevalece o documento corporativo.

---

# Recurso

## Nome

```
singulares
```

## URI Base

```
/api/v1/singulares
```

---

# Endpoints

## RF-SINGULAR-001 — Criar Singular

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/singulares |
| Caso de Uso | UC-SINGULAR-001 |
| Autenticação | Obrigatória |
| Autorização | Administrador global |

### Objetivo

Criar uma nova singular organizacional.

### Request DTO

```
CreateSingularRequest
```

### Response DTO

```
ApiResponse<SingularResponse>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 201 | Singular criada |
| 400 | Payload inválido |
| 401 | Não autenticado |
| 403 | Não autorizado |
| 422 | Violação de regra de negócio |

---

## RF-SINGULAR-002 — Consultar por Identificador

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/singulares/{id} |
| Caso de Uso | UC-SINGULAR-002 |
| Autenticação | Obrigatória |

### Response DTO

```
ApiResponse<SingularResponse>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 200 | Singular encontrada |
| 401 | Não autenticado |
| 404 | Singular inexistente |

---

## RF-SINGULAR-003 — Listar Singulares

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/singulares |
| Caso de Uso | UC-SINGULAR-003 |
| Autenticação | Obrigatória |

### Filtros Específicos

| Campo | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| status | Enum (`ACTIVE`, `INACTIVE`) | Não | Filtra por status lógico |
| federacaoId | Long | Não | Filtra por federação |
| name | String | Não | Busca parcial por nome (case insensitive) |
| acronym | String | Não | Busca parcial por sigla |
| codigoUnimed | String | Não | Busca exata ou parcial por código Unimed |

Parâmetros corporativos de paginação e ordenação: `page`, `size`, `sort` (ex.: `sort=name,asc`).

### Response DTO

```
ApiResponse<PageResponse<SingularResponse>>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 200 | Listagem retornada (pode ser vazia) |
| 400 | Parâmetros inválidos |
| 401 | Não autenticado |

---

## RF-SINGULAR-004 — Atualizar Singular

| Campo | Valor |
|--------|--------|
| Método | PUT |
| Endpoint | /api/v1/singulares/{id} |
| Caso de Uso | UC-SINGULAR-004 |
| Autenticação | Obrigatória |
| Autorização | Administrador no escopo da singular |

### Request DTO

```
UpdateSingularRequest
```

### Response DTO

```
ApiResponse<SingularResponse>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 200 | Singular atualizada |
| 400 | Payload inválido |
| 401 | Não autenticado |
| 403 | Não autorizado |
| 404 | Singular inexistente |
| 422 | Violação de regra de negócio |

---

## RF-SINGULAR-005 — Alterar Status

| Campo | Valor |
|--------|--------|
| Método | PATCH |
| Endpoint | /api/v1/singulares/{id}/status |
| Caso de Uso | UC-SINGULAR-005 |
| Autenticação | Obrigatória |
| Autorização | Administrador global |

### Request DTO

```
UpdateSingularStatusRequest
```

### Response DTO

```
ApiResponse<SingularResponse>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 200 | Status alterado |
| 400 | Status inválido |
| 401 | Não autenticado |
| 403 | Não autorizado |
| 404 | Singular inexistente |
| 422 | Inativação bloqueada |

---

# DTOs da Feature

Padrões de DTO: `docs/implementation/07-api-standards.md`.

## CreateSingularRequest

| Campo | Tipo | Obrigatório | Validação |
|--------|------|-------------|-----------|
| federacaoId | Long | Sim | Federação existente e ativa |
| name | String | Sim | Máx. 200 caracteres; não vazio |
| acronym | String | Sim | Máx. 30 caracteres; única global |
| codigoUnimed | String | Sim | Máx. 20 caracteres; único global |

## UpdateSingularRequest

| Campo | Tipo | Obrigatório | Validação |
|--------|------|-------------|-----------|
| name | String | Sim | Máx. 200 caracteres |
| acronym | String | Sim | Máx. 30 caracteres; única global |
| codigoUnimed | String | Sim | Máx. 20 caracteres; único global |

**Imutável:** `federacaoId` não faz parte do payload de atualização (RN-SINGULAR-007).

## UpdateSingularStatusRequest

| Campo | Tipo | Obrigatório | Validação |
|--------|------|-------------|-----------|
| status | Enum | Sim | `ACTIVE` ou `INACTIVE` |

## SingularResponse

| Campo | Tipo | Descrição |
|--------|------|-----------|
| id | Long | Identificador (`COD_SINGULAR`) |
| federacaoId | Long | Federação proprietária |
| name | String | Nome da singular |
| acronym | String | Sigla |
| codigoUnimed | String | Código Unimed |
| status | Enum | `ACTIVE` ou `INACTIVE` |
| createdAt | Instant | Data de cadastro |
| updatedAt | Instant | Data da última atualização (nullable) |

---

# Regras Específicas da API

- Identificador de path `{id}` mapeia `COD_SINGULAR`.
- Status API `ACTIVE`/`INACTIVE` mapeia `FLG_ATIVO` `S`/`N`.
- Não expor entidades JPA diretamente.
- Requisições mutáveis autenticadas por cookie exigem CSRF conforme FT-AUTH.
- Endpoint DELETE não faz parte desta Feature.

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| POST /api/v1/singulares | RF-SINGULAR-001 | UC-SINGULAR-001 | AT-SINGULAR-001 |
| GET /api/v1/singulares/{id} | RF-SINGULAR-002 | UC-SINGULAR-002 | AT-SINGULAR-002 |
| GET /api/v1/singulares | RF-SINGULAR-003 | UC-SINGULAR-003 | AT-SINGULAR-003 |
| PUT /api/v1/singulares/{id} | RF-SINGULAR-004 | UC-SINGULAR-004 | AT-SINGULAR-004 |
| PATCH /api/v1/singulares/{id}/status | RF-SINGULAR-005 | UC-SINGULAR-005 | AT-SINGULAR-005 |

---

# Critérios de Conformidade

Este contrato será considerado conforme quando:

- utilizar exclusivamente os padrões definidos em `docs/implementation/07-api-standards.md`;
- não duplicar convenções corporativas;
- documentar apenas aspectos específicos da Feature;
- possuir rastreabilidade completa entre RF, UC e AT;
- mantiver compatibilidade com a arquitetura do projeto e com `traceability.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | Contrato API inicial FT-SINGULAR |
| 1.1.1 | 2026-07-14 | Specification Engineer | Refinamento Gate 1 — status APPROVED |
