# API Contract

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-AREA |
| Feature | Área |
| Domínio | AREA |
| Recurso | areas |
| Base Path | /api/v1/areas |

---

# Objetivo

Este documento especifica exclusivamente o contrato funcional da API da Feature FT-AREA.

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
areas
```

## URI Base

```
/api/v1/areas
```

---

# Endpoints

## RF-AREA-001 — Criar Área

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | /api/v1/areas |
| Caso de Uso | UC-AREA-001 |
| Autenticação | Obrigatória |
| Autorização | Administrador no escopo da singular |

### Objetivo

Criar uma nova área organizacional.

### Request DTO

```
CreateAreaRequest
```

### Response DTO

```
ApiResponse<AreaResponse>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 201 | Área criada |
| 400 | Payload inválido |
| 401 | Não autenticado |
| 403 | Não autorizado |
| 422 | Violação de regra de negócio |

---

## RF-AREA-002 — Consultar por Identificador

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/areas/{id} |
| Caso de Uso | UC-AREA-002 |
| Autenticação | Obrigatória |

### Response DTO

```
ApiResponse<AreaResponse>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 200 | Área encontrada |
| 401 | Não autenticado |
| 404 | Área inexistente |

---

## RF-AREA-003 — Listar Áreas

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | /api/v1/areas |
| Caso de Uso | UC-AREA-003 |
| Autenticação | Obrigatória |

### Filtros Específicos

| Campo | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| status | Enum (`ACTIVE`, `INACTIVE`) | Não | Filtra por status lógico |
| singularId | Long | Não | Filtra por singular |
| name | String | Não | Busca parcial por nome (case insensitive) |
| acronym | String | Não | Busca parcial por sigla |

Parâmetros corporativos de paginação e ordenação: `page`, `size`, `sort` (ex.: `sort=name,asc`).

### Response DTO

```
ApiResponse<PageResponse<AreaResponse>>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 200 | Listagem retornada (pode ser vazia) |
| 400 | Parâmetros inválidos |
| 401 | Não autenticado |

---

## RF-AREA-004 — Atualizar Área

| Campo | Valor |
|--------|--------|
| Método | PUT |
| Endpoint | /api/v1/areas/{id} |
| Caso de Uso | UC-AREA-004 |
| Autenticação | Obrigatória |
| Autorização | Administrador no escopo da área |

### Request DTO

```
UpdateAreaRequest
```

### Response DTO

```
ApiResponse<AreaResponse>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 200 | Área atualizada |
| 400 | Payload inválido |
| 401 | Não autenticado |
| 403 | Não autorizado |
| 404 | Área inexistente |
| 422 | Violação de regra de negócio |

---

## RF-AREA-005 — Alterar Status

| Campo | Valor |
|--------|--------|
| Método | PATCH |
| Endpoint | /api/v1/areas/{id}/status |
| Caso de Uso | UC-AREA-005 |
| Autenticação | Obrigatória |
| Autorização | Administrador no escopo da singular |

### Request DTO

```
UpdateAreaStatusRequest
```

### Response DTO

```
ApiResponse<AreaResponse>
```

### Códigos HTTP

| Código | Condição |
|--------|----------|
| 200 | Status alterado |
| 400 | Status inválido |
| 401 | Não autenticado |
| 403 | Não autorizado |
| 404 | Área inexistente |
| 422 | Inativação bloqueada |

---

# DTOs da Feature

Padrões de DTO: `docs/implementation/07-api-standards.md`.

## CreateAreaRequest

| Campo | Tipo | Obrigatório | Validação |
|--------|------|-------------|-----------|
| singularId | Long | Sim | Singular existente e ativa |
| name | String | Sim | Máx. 200 caracteres; não vazio |
| acronym | String | Não | Máx. 30 caracteres |
| description | String | Não | Texto livre |
| managerId | Long | Não | Colaborador ativo |

## UpdateAreaRequest

| Campo | Tipo | Obrigatório | Validação |
|--------|------|-------------|-----------|
| name | String | Sim | Máx. 200 caracteres |
| acronym | String | Não | Máx. 30 caracteres |
| description | String | Não | Texto livre |
| managerId | Long | Não | Colaborador ativo |

**Imutável:** `singularId` não faz parte do payload de atualização (RN-AREA-009).

## UpdateAreaStatusRequest

| Campo | Tipo | Obrigatório | Validação |
|--------|------|-------------|-----------|
| status | Enum | Sim | `ACTIVE` ou `INACTIVE` |

## AreaResponse

| Campo | Tipo | Descrição |
|--------|------|-----------|
| id | Long | Identificador (`COD_AREA`) |
| singularId | Long | Singular proprietária |
| name | String | Nome da área |
| acronym | String | Sigla (nullable) |
| description | String | Descrição (nullable) |
| managerId | Long | Gestor (nullable) |
| status | Enum | `ACTIVE` ou `INACTIVE` |
| createdAt | Instant | Data de cadastro |
| updatedAt | Instant | Data da última atualização (nullable) |

---

# Regras Específicas da API

- Identificador de path `{id}` mapeia `COD_AREA`.
- Status API `ACTIVE`/`INACTIVE` mapeia `FLG_ATIVO` `S`/`N`.
- Não expor entidades JPA diretamente.
- Requisições mutáveis autenticadas por cookie exigem CSRF conforme FT-AUTH.
- Endpoint DELETE não faz parte desta Feature.

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| POST /api/v1/areas | RF-AREA-001 | UC-AREA-001 | AT-AREA-001 |
| GET /api/v1/areas/{id} | RF-AREA-002 | UC-AREA-002 | AT-AREA-002 |
| GET /api/v1/areas | RF-AREA-003 | UC-AREA-003 | AT-AREA-003 |
| PUT /api/v1/areas/{id} | RF-AREA-004 | UC-AREA-004 | AT-AREA-004 |
| PATCH /api/v1/areas/{id}/status | RF-AREA-005 | UC-AREA-005 | AT-AREA-005 |

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
| 1.0 | 2026-07-13 | Specification Engineer | Contrato API inicial FT-AREA |
| 1.1 | 2026-07-13 | Specification Engineer | Sincronização Specification Framework v1.1 |
| 1.2.0 | 2026-07-21 | Engineering Framework | Remoção hierarquia entre áreas (DEC-DB-022) |
