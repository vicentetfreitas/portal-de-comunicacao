# API Consumption

## Objetivo

Definir os padrões para consumo das APIs do Portal de Comunicação pelo frontend.

Este documento estabelece as diretrizes para comunicação com serviços backend, gerenciamento de requisições HTTP, tratamento de erros, cache, observabilidade e integração com o gerenciamento de estado da aplicação.

**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado Fase 1 Frontend em 2026-06-22

---

# Escopo

Esta documentação cobre:

* API Client
* Requisições HTTP
* Contratos
* DTOs
* Tratamento de erros
* Interceptors
* Cache
* Integração com TanStack Query
* Observabilidade
* Versionamento de APIs

Não cobre:

* Autenticação
* Navegação
* Gerenciamento de estado interno

---

# Princípios

Toda integração frontend-backend deve seguir:

* Baixo acoplamento
* Tipagem forte
* Observabilidade
* Resiliência
* Reutilização
* Rastreabilidade

---

# Arquitetura

Fluxo padrão:

```text id="6e1bxy"
Page
 │
 ▼
Hook
 │
 ▼
Query / Mutation
 │
 ▼
Service
 │
 ▼
Api Client
 │
 ▼
Backend API
```

---

# Estrutura de Diretórios

```text id="8qpgsx"
src
├── services
│   ├── api
│   ├── comunicado
│   ├── notification
│   └── document
│
├── hooks
│
├── features
│
└── types
```

---

# API Client

Toda comunicação HTTP deve utilizar um client centralizado.

---

# Não Permitido

```typescript id="a8zkg8"
fetch(...)
```

diretamente em componentes.

---

```typescript id="w7vvhh"
axios(...)
```

diretamente em páginas.

---

# Permitido

```typescript id="uhy7fi"
comunicadoService.list()
```

---

# Estrutura

```text id="xf4m9k"
services
└── api
    ├── api-client.ts
    ├── interceptors.ts
    ├── api-error.ts
    └── api-config.ts
```

---

# Configuração Base

Exemplo:

```typescript id="8alj1w"
const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL
});
```

---

# Timeout

Todas as chamadas devem possuir timeout.

---

## Padrão

| Operação | Timeout |
| -------- | ------- |
| Consulta | 10s     |
| Escrita  | 15s     |
| Upload   | 60s     |

---

# Versionamento

Toda API deve ser consumida através de versão explícita.

---

## Exemplo

```text id="5g8e58"
/api/v1/comunicados

/api/v1/notifications

/api/v1/documents
```

**Rastreabilidade:** FEATURE-041, FEATURE-040, FEATURE-030 (`docs/audit/10-mvp-consolidation-audit.md`).

---

## Obsoleto (fora do MVP)

> Não consumir — removidos por reconciliação MVP.

```text
/api/v1/campaigns
/api/v1/messages
/api/v1/communications
```

---

# Services

Responsáveis pela comunicação com APIs.

---

## Exemplo

```typescript id="2sjxqh"
comunicado.service.ts
```

---

# Estrutura

```typescript id="7fflcm"
export const comunicadoService = {
  list,
  findById,
  create,
  update,
  remove
};
```

---

# Responsabilidades

Services devem:

* Consumir APIs
* Transformar respostas
* Tratar contratos

---

# Não Devem

* Renderizar UI
* Manipular componentes
* Controlar navegação

---

# DTOs

Toda API deve possuir contratos tipados.

---

## Request

```typescript id="tx12lu"
CreateComunicadoRequest
```

---

## Response

```typescript id="3jkvwq"
ComunicadoResponse
```

---

# Estrutura

```text id="4hnjlwm"
types
├── requests
└── responses
```

---

# Tipagem

Nunca utilizar:

```typescript id="qjlwmx"
any
```

---

Preferir:

```typescript id="m3hy1r"
interface

type
```

---

# Exemplo

```typescript id="upnddz"
export interface ComunicadoResponse {
  id: string;
  title: string;
}
```

---

# Integração com TanStack Query

Toda consulta deve ser encapsulada.

---

## Exemplo

```typescript id="j47s67"
useComunicadosQuery()
```

---

# Fluxo

```text id="o3n6rj"
Hook
 ↓
Query
 ↓
Service
 ↓
API
```

---

# Query Keys

Padronização obrigatória.

---

## Exemplo

```typescript id="r5m8lv"
["comunicados"]

["comunicados", id]

["notifications"]

["documents"]
```

---

# Mutations

Operações de escrita.

---

## Exemplo

```typescript id="yv0o5i"
useCreateComunicadoMutation()
```

---

# Invalidação

Após operações de escrita.

---

## Exemplo

```typescript id="8k8llh"
queryClient.invalidateQueries()
```

---

# Paginação

Sempre suportar paginação do backend.

---

## Exemplo

```typescript id="cg4gng"
?page=0&size=20
```

---

# Ordenação

Representar explicitamente.

---

## Exemplo

```typescript id="jafgvz"
?sort=createdAt,desc
```

---

# Filtros

Representar via query params.

---

## Exemplo

```typescript id="ttlsy6"
?status=ACTIVE
```

---

# Uploads

Utilizar:

```typescript id="7kr7e0"
multipart/form-data
```

---

# Download de Arquivos

Utilizar:

```typescript id="4cmhrw"
blob
```

---

# Interceptors

Todos os requests devem passar por interceptors globais.

---

# Request Interceptor

Responsável por:

* Correlation ID
* Token
* Headers padrão
* Rastreabilidade

---

## Exemplo

```typescript id="hn3y04"
Authorization

X-Correlation-Id
```

---

# Response Interceptor

Responsável por:

* Tratamento de erros
* Logging
* Refresh Token

---

# Tratamento de Erros

Toda resposta de erro deve ser normalizada.

---

# Estrutura

```typescript id="jewxt0"
ApiError
```

---

## Exemplo

```typescript id="rm8wzn"
{
  code: "VALIDATION_ERROR",
  message: "Campo obrigatório"
}
```

---

# Categorias de Erro

## Validação

```text id="y3g2m6"
400
422
```

---

## Autenticação

```text id="aj9y8m"
401
```

---

## Autorização

```text id="szun90"
403
```

---

## Não Encontrado

```text id="7hy16j"
404
```

---

## Conflito

```text id="a5d8ra"
409
```

---

## Erro Interno

```text id="ij2m2n"
500
```

---

# Retry

Permitido apenas para erros transitórios.

---

## Exemplos

```text id="b0nqlg"
429

502

503

504
```

---

# Não Fazer Retry

```text id="2uxtm0"
400

401

403

404
```

---

# Cache

Responsabilidade do TanStack Query.

---

## Não Implementar

Cache manual.

---

# Revalidação

Controlada por:

```typescript id="6fjvpi"
staleTime
```

---

```typescript id="sjayz5"
cacheTime
```

---

# Observabilidade

Todas as chamadas devem registrar:

* Endpoint
* Método
* Tempo de resposta
* Status HTTP
* Correlation ID

---

# Correlation ID

Obrigatório.

---

## Header

```http id="rn6vry"
X-Correlation-Id
```

---

# Logs

Registrar:

```text id="vzgzv7"
Request

Response

Error
```

---

# Nunca Registrar

* Senhas
* Tokens
* Dados sensíveis
* Informações pessoais protegidas

---

# Feature Isolation

Cada domínio deve possuir seus próprios services.

---

## Exemplo

```text id="2jmwuj"
features
├── comunicados
├── notifications
└── documents
```

---

# Comunicação Entre Features

Sempre através de:

* Hooks
* Services
* Shared Components

---

# Mocking

Obrigatório para testes.

---

## Ferramenta

```text id="9q5v9d"
MSW
```

---

# Testes

Validar:

* Services
* Queries
* Mutations
* Erros
* Timeouts
* Uploads

---

# Estrutura Recomendada

```text id="nhtkln"
features
└── comunicados
    ├── services
    │   └── comunicado.service.ts
    │
    ├── hooks
    │   ├── useComunicados.ts
    │   └── useCreateComunicado.ts
    │
    ├── types
    │   ├── requests
    │   └── responses
    │
    └── queries
```

---

# Checklist

Antes de integrar uma API:

* [ ] Service criado
* [ ] DTOs tipados
* [ ] Query criada
* [ ] Mutation criada
* [ ] Interceptors configurados
* [ ] Tratamento de erro implementado
* [ ] Cache configurado
* [ ] Logs configurados
* [ ] Testes implementados

---

# Critérios de Aceite

A implementação será considerada aderente quando:

* Todo acesso HTTP ocorrer através do API Client.
* Não existirem chamadas HTTP em componentes.
* Todas as APIs estiverem tipadas.
* Todas as integrações utilizarem TanStack Query.
* Erros estiverem normalizados.
* Logs e rastreabilidade estiverem implementados.
* Cache estiver configurado.
* Testes automatizados cobrirem os principais fluxos.
* Os padrões definidos neste documento forem seguidos por todas as equipes de frontend.
