# Routing

## Objetivo

Definir a arquitetura de navegação, organização de rotas, layouts, proteção de acesso e estratégias de roteamento do Portal de Comunicação.

Este documento estabelece os padrões que garantem previsibilidade, segurança e escalabilidade na navegação da aplicação.

**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado Fase 1 Frontend em 2026-06-22

---

# Escopo

Esta documentação cobre:

* Estrutura de rotas
* Navegação
* Layouts
* Rotas públicas
* Rotas protegidas
* Controle de acesso
* Breadcrumbs
* Deep Links
* Tratamento de páginas especiais

Não cobre:

* Autenticação
* Consumo de APIs
* Gerenciamento de estado

---

# Estratégia de Navegação

O frontend deve utilizar:

```text
Next.js App Router
```

---

# Objetivos

A navegação deve ser:

* Escalável
* Segura
* Previsível
* SEO Friendly
* Responsiva
* Baseada em domínio

---

# Estrutura de Rotas

Organização por funcionalidade.

```text
src
└── app
    ├── (public)
    ├── (authenticated)
    ├── login
    ├── dashboard
    ├── comunicados
    ├── notifications
    ├── documents
    ├── settings
    ├── unauthorized
    ├── not-found
    └── error
```

---

# Convenções

## Utilizar

```text
/comunicados

/notifications

/documents
```

**Rastreabilidade:** FEATURE-041, FEATURE-040, FEATURE-030 (`docs/audit/10-mvp-consolidation-audit.md`).

---

## Obsoleto (fora do MVP)

> Não implementar — removidos por reconciliação MVP.

```text
/campaigns
/messages
/listCampaigns
```

---

# Estrutura por Módulo

Exemplo:

```text
app
└── comunicados
    ├── page.tsx
    ├── loading.tsx
    ├── error.tsx
    ├── not-found.tsx
    ├── create
    │   └── page.tsx
    └── [id]
        ├── page.tsx
        └── edit
            └── page.tsx
```

---

# Padrão de URLs

## Lista

```text
/comunicados
```

---

## Detalhe

```text
/comunicados/{id}
```

---

## Criação

```text
/comunicados/create
```

---

## Edição

```text
/comunicados/{id}/edit
```

---

# Route Groups

Utilizar Route Groups para separar contextos.

---

## Público

```text
(public)
```

---

## Autenticado

```text
(authenticated)
```

---

# Layouts

Cada grupo de rotas deve possuir layout próprio.

---

## Público

```text
Login
Recuperação de senha
Acesso externo
```

---

## Autenticado

```text
Dashboard
Comunicados
Notificações
Documentos
Configurações
```

---

# Estrutura

```text
app
├── (public)
│   ├── layout.tsx
│   └── login
│
└── (authenticated)
    ├── layout.tsx
    ├── dashboard
    └── comunicados
```

---

# Layout Público

Responsável por:

* Login
* Recuperação de senha
* Convites
* Acesso externo

---

# Layout Autenticado

Responsável por:

* Sidebar
* Header
* Breadcrumb
* Navegação principal
* Perfil do usuário

---

# Rotas Públicas

Não exigem autenticação.

Exemplos:

```text
/login

/forgot-password

/reset-password

/unauthorized
```

---

# Rotas Protegidas

Exigem autenticação.

Exemplos:

```text
/dashboard

/comunicados

/notifications

/documents

/settings
```

---

# Middleware de Proteção

Toda rota protegida deve ser validada.

---

## Fluxo

```text
Request
   ↓
Middleware
   ↓
Validação Token
   ↓
Autorizado?
   ↓
Sim → Página
Não → Login
```

---

# Middleware

Exemplo:

```typescript
export function middleware() {

}
```

---

# Controle de Permissões

A autenticação não substitui autorização.

---

# Modelo

RBAC

```text
ADMIN

MANAGER

OPERATOR

VIEWER
```

---

# Permissões por Rota

Exemplo:

| Rota            | Perfil        |
| --------------- | ------------- |
| /dashboard      | Todos         |
| /comunicados    | Todos         |
| /notifications  | Todos         |
| /documents      | Todos         |
| /settings       | Admin         |

---

# Route Guards

Toda rota crítica deve validar:

* Usuário autenticado
* Perfil autorizado
* Contexto válido

---

# Página Unauthorized

Usuário autenticado sem permissão.

```text
/unauthorized
```

---

# Página Not Found

Utilizar:

```text
not-found.tsx
```

---

# Página Error

Utilizar:

```text
error.tsx
```

---

# Loading

Cada módulo deve possuir:

```text
loading.tsx
```

---

# Navegação

Utilizar componentes do Next.js.

---

## Permitido

```typescript
<Link />
```

---

```typescript
router.push()
```

---

## Evitar

```javascript
window.location.href
```

---

# Breadcrumbs

Obrigatório para áreas autenticadas.

---

## Exemplo

```text
Home
 └── Comunicados
      └── Detalhes
```

---

# Estrutura

```typescript
Dashboard
Comunicados
Notifications
Documents
Settings
```

---

# Menu Principal

Deve ser gerado por configuração.

---

## Exemplo

```typescript
const menu = [
  {
    label: "Comunicados",
    path: "/comunicados"
  }
];
```

---

# Deep Linking

Toda página deve suportar acesso direto por URL.

---

# Requisitos

Não depender:

* Navegação anterior
* Estado temporário
* Contexto local

---

# Query Parameters

Utilizar para filtros e pesquisas.

---

## Exemplo

```text
/comunicados?page=1
```

---

```text
/comunicados?status=ACTIVE
```

---

```text
/comunicados?search=email
```

---

# Paginação

Estado deve refletir na URL.

---

## Correto

```text
?page=2
```

---

## Evitar

Paginação apenas em memória.

---

# Ordenação

Representar na URL.

---

## Exemplo

```text
?sort=createdAt,desc
```

---

# Estado de Navegação

Manter sincronizado com URL.

---

# SEO

Páginas públicas devem possuir:

* title
* description
* metadata

---

# Exemplo

```typescript
export const metadata = {
  title: "Portal de Comunicação"
};
```

---

# Performance

Aplicar lazy loading quando necessário.

---

# Utilizar

```typescript
dynamic()
```

---

# Prefetch

Habilitar para rotas frequentemente utilizadas.

---

# Observabilidade

Registrar:

* Navegação
* Erros de rota
* Tempo de carregamento

---

# Analytics

Eventos recomendados:

```text
PAGE_VIEW

PAGE_LOAD

NAVIGATION_ERROR
```

---

# Acessibilidade

A navegação deve permitir:

* Teclado
* Screen readers
* Focus management

---

# Focus

Ao trocar de página:

* Restaurar foco principal
* Atualizar contexto visual

---

# Testes

Validar:

* Rotas públicas
* Rotas protegidas
* Redirecionamentos
* Deep Links
* Breadcrumbs
* Permissões

---

# Checklist

Antes de publicar:

* [ ] Rotas organizadas por domínio
* [ ] Layouts configurados
* [ ] Middleware implementado
* [ ] Guards implementados
* [ ] Breadcrumbs implementados
* [ ] Deep Links suportados
* [ ] Query params suportados
* [ ] Página de erro criada
* [ ] Página not-found criada
* [ ] Testes implementados

---

# Critérios de Aceite

A arquitetura de navegação será considerada aderente quando:

* Todas as rotas seguirem o padrão definido.
* Rotas protegidas estiverem protegidas.
* Permissões forem validadas.
* Navegação for consistente.
* URLs forem amigáveis.
* Deep Links funcionarem corretamente.
* Layouts forem reutilizáveis.
* Houver suporte completo para SEO, acessibilidade e observabilidade.
