# API Contract

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1 |
| Status | STABLE |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | ${FEATURE_ID} |
| Feature | ${FEATURE_NAME} |
| Domínio | ${DOMAIN} |
| Recurso | ${RESOURCE_NAME} |
| Base Path | ${API_BASE_PATH} |

---

# Objetivo

Este documento especifica exclusivamente o contrato funcional da API desta Feature.

Este artefato descreve:

- recursos expostos;
- endpoints da Feature;
- payloads específicos;
- regras específicas;
- rastreabilidade.

Este documento não redefine padrões arquiteturais já estabelecidos pelo projeto.

---

# Dependências

Esta Feature herda integralmente os padrões definidos em:

```text
docs/implementation/07-api-standards.md
```

Incluindo:

- convenções REST;
- versionamento;
- DTO Standards;
- ApiResponse;
- PageResponse;
- ErrorResponse;
- paginação;
- filtros;
- ordenação;
- autenticação;
- autorização;
- observabilidade;
- auditoria;
- tratamento de erros;
- códigos HTTP.

Em caso de divergência, prevalece sempre o documento corporativo.

---

# Recurso

## Nome

```
${RESOURCE_NAME}
```

---

## URI Base

```
${API_BASE_PATH}
```

---

# Endpoints

## RF-${DOMAIN}-001 — Criar

| Campo | Valor |
|--------|--------|
| Método | POST |
| Endpoint | ${API_BASE_PATH} |
| Caso de Uso | UC-${DOMAIN}-001 |

### Objetivo

Criar um novo recurso.

### Request DTO

```
<Create${ENTITY_NAME}Request>
```

### Response DTO

```
${ENTITY_NAME}Response
```

---

## RF-${DOMAIN}-002 — Consultar por Identificador

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | ${API_BASE_PATH}/{${PRIMARY_KEY}} |
| Caso de Uso | UC-${DOMAIN}-002 |

### Response DTO

```
${ENTITY_NAME}Response
```

---

## RF-${DOMAIN}-003 — Listar

| Campo | Valor |
|--------|--------|
| Método | GET |
| Endpoint | ${API_BASE_PATH} |
| Caso de Uso | UC-${DOMAIN}-003 |

### Filtros Específicos

Documentar apenas filtros exclusivos desta Feature.

Exemplo:

| Campo | Tipo | Obrigatório |
|--------|------|-------------|
| status | Enum | Não |
| nome | String | Não |

---

### Response DTO

```
PageResponse<${ENTITY_NAME}Response>
```

---

## RF-${DOMAIN}-004 — Atualizar

| Campo | Valor |
|--------|--------|
| Método | PUT |
| Endpoint | ${API_BASE_PATH}/{${PRIMARY_KEY}} |
| Caso de Uso | UC-${DOMAIN}-004 |

### Request DTO

```
Update${ENTITY_NAME}Request
```

### Response DTO

```
${ENTITY_NAME}Response
```

---

## RF-${DOMAIN}-005 — Alterar Status

| Campo | Valor |
|--------|--------|
| Método | PATCH |
| Endpoint | ${API_BASE_PATH}/{${PRIMARY_KEY}}/status |
| Caso de Uso | UC-${DOMAIN}-005 |

### Request DTO

```
Update${ENTITY_NAME}StatusRequest
```

### Response DTO

```
${ENTITY_NAME}Response
```

---

# DTOs da Feature

Este documento descreve apenas DTOs específicos da Feature.

Os padrões para DTOs encontram-se definidos em:

```text
docs/implementation/07-api-standards.md
```

## Create${ENTITY_NAME}Request

Documentar apenas os atributos específicos.

---

## Update${ENTITY_NAME}Request

Documentar apenas os atributos específicos.

---

## ${ENTITY_NAME}Response

Documentar apenas os atributos públicos do recurso.

---

# Regras Específicas da API

Registrar apenas regras exclusivas desta Feature.

Exemplos:

- campos obrigatórios;
- campos imutáveis;
- filtros específicos;
- restrições de atualização;
- operações condicionais.

Não repetir regras globais.

---

# Matriz de Rastreabilidade

| Endpoint | RF | UC | AT |
|-----------|----|----|----|
| POST ${API_BASE_PATH} | RF-${DOMAIN}-001 | UC-${DOMAIN}-001 | AT-${DOMAIN}-001 |
| GET ${API_BASE_PATH}/{${PRIMARY_KEY}} | RF-${DOMAIN}-002 | UC-${DOMAIN}-002 | AT-${DOMAIN}-002 |
| GET ${API_BASE_PATH} | RF-${DOMAIN}-003 | UC-${DOMAIN}-003 | AT-${DOMAIN}-003 |
| PUT ${API_BASE_PATH}/{${PRIMARY_KEY}} | RF-${DOMAIN}-004 | UC-${DOMAIN}-004 | AT-${DOMAIN}-004 |
| PATCH ${API_BASE_PATH}/{${PRIMARY_KEY}}/status | RF-${DOMAIN}-005 | UC-${DOMAIN}-005 | AT-${DOMAIN}-005 |

---

# Critérios de Conformidade

Este contrato será considerado conforme quando:

- utilizar exclusivamente os padrões definidos em `docs/implementation/07-api-standards.md`;
- não duplicar convenções corporativas;
- documentar apenas aspectos específicos da Feature;
- possuir rastreabilidade completa entre RF, UC e AT;
- manter compatibilidade com a arquitetura do projeto.