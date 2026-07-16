# State Management

| Item | Valor |
|------|-------|
| Camada | Construction — Frontend |
| Sprint | Sprint 0 — Frontend Foundation |
| Versão | 1.1 |
| Status | Reconciliado — stack oficial DEC-004 |
| Especificação prevalecente | `00-frontend-foundation.md` |
| Artefatos operacionais | `construction/frontend/` |

> Guia complementar de estado. Server state: Pinia stores + Axios services + composables.

---

## Objetivo

Definir a estratégia de gerenciamento de estado do Portal de Comunicação.

Este documento estabelece como os dados devem ser armazenados, compartilhados, sincronizados e atualizados na aplicação frontend, garantindo previsibilidade, performance e escalabilidade.

**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado Fase 1 Frontend em 2026-06-22

---

# Escopo

Esta documentação cobre:

* Estado local
* Estado global
* Server State
* Cache
* Sincronização de dados
* Persistência de estado
* Compartilhamento entre componentes
* Estratégias de atualização

Não cobre:

* Autenticação
* Navegação
* Design System

---

# Princípios

Todo gerenciamento de estado deve seguir:

* Single Source of Truth
* Predictability
* Performance
* Simplicidade
* Reutilização
* Escalabilidade

---

# Classificação dos Estados

A aplicação possui quatro categorias principais de estado.

```text id="3t6g9v"
Client State

Server State

UI State

Session State
```

---

# Client State

Estado controlado exclusivamente pelo frontend.

Exemplos:

* Sidebar aberta
* Modal visível
* Tema da aplicação
* Preferências locais

---

# Server State

Estado proveniente do backend.

Exemplos:

* Colaboradores e estrutura organizacional
* Comunicados
* Notificações
* Documentos
* Configurações

---

# UI State

Estado temporário da interface.

Exemplos:

* Loading
* Paginação
* Filtros
* Ordenação
* Seleções

---

# Session State

Estado associado ao usuário autenticado.

Exemplos:

* Perfil
* Permissões
* Tenant atual
* Contexto organizacional

---

# Estratégia Oficial

## Server State

Utilizar:

```text id="0vr9bq"
Pinia stores + Axios services + composables
```

---

## Client State

Utilizar:

```text id="o4uk2h"
Zustand
```

---

## Estado Local

Utilizar:

```text id="lvcz77"
useState
```

---

## Estado Derivado

Utilizar:

```text id="sc8rj5"
useMemo
```

---

# Não Utilizar

Evitar:

```text id="qf8q8e"
Redux
MobX
Flux
```

Salvo necessidade arquitetural futura.

---

# Arquitetura

```text id="p4x3dr"
UI
 │
 ▼
Hooks
 │
 ▼
Query Layer
 │
 ▼
API Layer
 │
 ▼
Backend
```

---

# Estrutura

```text id="5hhlol"
src
├── stores
├── hooks
├── providers
├── services
└── features
```

---

# Zustand

Responsável por estados globais do frontend.

---

## Casos de Uso

* Sidebar
* Preferências
* Contexto visual
* Dados compartilhados leves

---

## Exemplo

```typescript id="fc89j9"
interface UiStore {
  sidebarOpen: boolean;
}
```

---

# Organização

```text id="wuwr3u"
stores
├── ui.store.ts
├── preferences.store.ts
├── tenant.store.ts
└── session.store.ts
```

---

# Regras para Stores

Stores não devem:

* Consumir APIs diretamente
* Executar regras de negócio
* Conter lógica de autenticação

---

# Pinia + Axios (Server State)

Responsável pelo gerenciamento do Server State: **Pinia stores** para estado derivado e cache em memória; **Axios services** para requisições HTTP; **composables** para orquestração e revalidação.

---

# Benefícios

* Cache automático
* Revalidação
* Deduplicação
* Retry
* Paginação
* Mutations

---

# Estrutura

```text id="v62svx"
features
├── comunicados
│   ├── hooks
│   ├── services
│   └── queries
├── notifications
└── documents
```

---

# Queries

Responsáveis por leitura.

---

## Exemplo

```typescript id="j4h4rj"
useComunicadosQuery()

useNotificationsQuery()

useDocumentsQuery()
```

---

# Mutations

Responsáveis por escrita.

---

## Exemplo

```typescript id="ddc83e"
useCreateComunicadoMutation()
```

---

# Query Keys

Devem seguir padrão consistente.

---

## Exemplo

```typescript id="e4rzzd"
["comunicados"]

["comunicados", id]

["notifications"]

["documents"]
```

**Rastreabilidade:** FEATURE-041, FEATURE-040, FEATURE-030 (`docs/audit/10-mvp-consolidation-audit.md`).

---

# Evitar

```typescript id="3vkm8f"
["data"]

["list"]
```

---

# Cache

Toda consulta deve possuir estratégia explícita.

---

## Exemplo

```typescript id="4syvso"
staleTime: 30000
```

---

## Recomendação

| Tipo             | Stale Time |
| ---------------- | ---------- |
| Catálogo         | 10 min     |
| Dashboard        | 30 s       |
| Configuração     | 5 min      |
| Consulta crítica | 0          |

---

# Invalidação

Após mutações relevantes.

---

## Exemplo

```typescript id="a8v9tm"
queryClient.invalidateQueries()
```

---

# Paginação

Deve utilizar cache em Pinia stores ou composables dedicados (ex.: `useCache`).

---

## Exemplo

```typescript id="3q5vot"
useInfiniteQuery()
```

---

# Prefetch

Utilizar para telas de acesso frequente.

---

## Exemplo

```typescript id="t29a6h"
prefetchQuery()
```

---

# Estado Local

Utilizar apenas para dados temporários.

---

## Exemplo

```typescript id="yrnntt"
const [open, setOpen] = useState(false);
```

---

# Evitar

Promover estado local para global sem necessidade.

---

# Estado Derivado

Utilizar memoização.

---

## Exemplo

```typescript id="gczjz3"
useMemo()
```

---

# Evitar

Duplicação de estado.

---

## Incorreto

```typescript id="zxnlw7"
const users = response.data;

const filteredUsers = state.users;
```

---

# Filtros

Representar filtros na URL sempre que possível.

---

## Correto

```text id="6nrxmv"
?page=1

?status=ACTIVE
```

---

# Incorreto

Filtro apenas em memória.

---

# Persistência

Persistir apenas informações necessárias.

---

## Permitido

* Tema
* Idioma
* Preferências

---

## Evitar

* Tokens
* Dados sensíveis
* Informações críticas

---

# Local Storage

Utilizar somente para:

```text id="w6cc6m"
Preferências
Configurações visuais
```

---

# Session Storage

Utilizar para:

```text id="84m59z"
Contextos temporários
```

---

# Sincronização

O backend é sempre a fonte oficial da verdade.

---

## Regra

Nunca confiar exclusivamente no cache.

---

# Atualização de Dados

Prioridade:

```text id="o85c4q"
Backend

Cache

UI
```

---

# Loading States

Toda consulta deve possuir:

* Loading
* Success
* Empty
* Error

---

## Exemplo

```typescript id="b66l0z"
isLoading

isError

isSuccess
```

---

# Tratamento de Erros

Centralizar erros de API.

---

## Exemplo

```typescript id="7kww4k"
ApiError
```

---

# Optimistic Updates

Utilizar apenas quando houver benefício claro.

---

## Exemplos

* Favoritos
* Seleções
* Configurações

---

# Evitar

Operações críticas.

---

## Exemplo

Não utilizar para:

* Aprovações
* Exclusões irreversíveis
* Processamentos financeiros

---

# Providers

Centralizar providers globais.

---

## Estrutura

```text id="y5hm31"
providers
├── QueryProvider
├── ThemeProvider
├── SessionProvider
└── ErrorProvider
```

---

# Context API

Utilizar apenas para:

* Theme
* Internacionalização
* Configurações globais

---

# Evitar

Grandes volumes de estado.

---

# Performance

Evitar:

* Re-renders desnecessários
* Estados duplicados
* Objetos recriados continuamente

---

# Utilizar

```typescript id="7sxt5m"
memo()

useMemo()

useCallback()
```

quando justificável.

---

# Observabilidade

Registrar:

* Cache Hits
* Cache Misses
* Erros de consulta
* Tempo de carregamento

---

# Testes

Validar:

* Queries
* Mutations
* Stores
* Cache
* Estados de erro
* Estados de loading

---

# Estrutura Recomendada

```text id="sxzkxy"
features
└── comunicados
    ├── hooks
    │   ├── useComunicados.ts
    │   └── useCreateComunicado.ts
    │
    ├── services
    │   └── comunicado.service.ts
    │
    ├── queries
    │   └── comunicado.query.ts
    │
    └── types
```

---

# Checklist

Antes de implementar uma funcionalidade:

* [ ] Estado classificado corretamente
* [ ] Query Key definida
* [ ] Cache definido
* [ ] Loading implementado
* [ ] Error State implementado
* [ ] Empty State implementado
* [ ] Invalidação configurada
* [ ] Testes implementados

---

# Critérios de Aceite

A estratégia de gerenciamento de estado será considerada aderente quando:

* Server State utilizar Pinia stores + Axios services + composables.
* Client State utilizar Zustand.
* Não existirem estados duplicados.
* O backend permanecer como fonte oficial da verdade.
* Cache possuir estratégia explícita.
* Estados de loading e erro forem tratados.
* Houver observabilidade das consultas.
* A implementação seguir os padrões definidos neste documento.
