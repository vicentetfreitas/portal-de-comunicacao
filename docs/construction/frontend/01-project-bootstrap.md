# Frontend Project Bootstrap

| Item | Valor |
|------|-------|
| Camada | Construction — Frontend |
| Sprint | Sprint 0 — Frontend Foundation |
| Versão | 1.1 |
| Status | Reconciliado — stack oficial DEC-004 |
| Especificação prevalecente | `00-frontend-foundation.md` |
| Artefatos operacionais | `construction/frontend/` |

> Guia complementar de bootstrap. Escopo e Packages: `00-frontend-foundation.md`. Stack: `docs/technology/01-technology-stack.md`.

---

## Objetivo

Definir os padrões de inicialização, estruturação e configuração do frontend do Portal de Comunicação.

Este documento estabelece as diretrizes necessárias para que qualquer desenvolvedor consiga configurar, executar, testar e evoluir o frontend de forma consistente, previsível e alinhada com a arquitetura da solução.

**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado Fase 1 Frontend em 2026-06-22

---

# Escopo

Esta documentação cobre:

* Estrutura inicial do projeto
* Ferramentas de desenvolvimento
* Organização de diretórios
* Build e deploy
* Configuração de ambientes
* Padronização de código
* Qualidade
* Observabilidade frontend

Não cobre:

* Design System
* Navegação
* Estado global
* Consumo de APIs
* Autenticação

Esses assuntos possuem documentos específicos.

---

# Stack Tecnológica

Conforme `docs/technology/01-technology-stack.md` e DEC-004.

## Framework

Vue 3 (Composition API, `<script setup>`)

---

## UI Framework

Quasar Framework (Quasar App Vite)

---

## Linguagem

TypeScript

---

## Runtime

Node.js LTS (build tooling)

---

## Gerenciador de Pacotes

Yarn (recomendado) ou pnpm

---

## Estilização

Quasar SCSS + design tokens (`quasar.variables.scss`, `design-tokens.scss`)

---

## Componentização

Vue SFC (`.vue`)

---

## Estado e HTTP

Pinia (estado global) · Axios (HTTP client)

---

## Qualidade

ESLint / oxlint · Prettier / oxfmt

Husky

Lint-Staged

---

## Testes

Vitest

Testing Library

Playwright

---

## Monitoramento

OpenTelemetry

Sentry

---

# Objetivos da Arquitetura Frontend

O frontend deve ser:

* Escalável
* Modular
* Testável
* Responsivo
* Acessível
* Observável
* Independente da API

---

# Estrutura de Diretórios

```text
src
├── app
├── pages
├── components
│   ├── ui
│   ├── layout
│   ├── forms
│   └── feedback
│
├── features
│   ├── comunicados
│   ├── notifications
│   ├── documents
│   ├── organization
│   ├── access-control
│   └── authentication
│
├── services
│
├── hooks
│
├── stores
│
├── providers
│
├── lib
│
├── types
│
├── utils
│
├── constants
│
├── styles
│
└── tests
```

---

# Organização por Feature

Toda funcionalidade deve ser organizada por domínio funcional.

Exemplo:

```text
features
└── comunicados
    ├── components
    ├── hooks
    ├── services
    ├── pages
    └── types
```

**Rastreabilidade:** FEATURE-041 — Gestão de Comunicados (`docs/audit/10-mvp-consolidation-audit.md`).

Módulos MVP adicionais: `notifications` (FEATURE-040), `documents` (FEATURE-030 a FEATURE-037), `organization` (FEATURE-010 a FEATURE-017), `access-control` (FEATURE-020 a FEATURE-029).

---

# Convenções de Nomenclatura

## Componentes

PascalCase

```text
ComunicadoCard.vue

NotificationBell.vue

DocumentExplorer.vue

UserMenu.vue
```

---

## Hooks

```text
useComunicados.ts

useNotifications.ts

useDocuments.ts
```

---

## Services

```text
comunicado.service.ts

notification.service.ts

document.service.ts
```

---

## Types

```text
comunicado.types.ts

notification.types.ts

document.types.ts
```

---

## Obsoleto (fora do MVP)

> Não criar — removidos por `docs/audit/10-mvp-consolidation-audit.md`.

```text
CampaignForm.vue
useCampaigns.ts
campaign.service.ts
campaign.types.ts
features/campaigns/
features/messages/
```

---

# Configuração de Ambientes

Todos os ambientes devem utilizar variáveis externas.

---

## Local

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

## Homologação

```env
NEXT_PUBLIC_API_URL=https://hml-api.portal.com
```

---

## Produção

```env
NEXT_PUBLIC_API_URL=https://api.portal.com
```

---

# Regras para Variáveis

Utilizar:

```env
NEXT_PUBLIC_
```

apenas para valores necessários no browser.

---

Nunca expor:

* Senhas
* Secrets
* Tokens administrativos
* Chaves privadas

---

# Instalação do Projeto

```bash
yarn install
```

---

# Execução Local

```bash
quasar dev
```

---

# Build

```bash
quasar build
```

---

# Execução Produção

```bash
quasar serve (preview SPA)
```

---

# Estrutura de Configuração

```text
root
├── .env.local
├── .env.hml
├── .env.production
├── next.config.ts
├── tsconfig.json
├── tailwind.config.ts
└── package.json
```

---

# Gerenciamento de Dependências

Toda dependência deve possuir justificativa técnica.

---

## Permitido

* Dependências amplamente adotadas
* Bibliotecas mantidas
* Soluções com documentação ativa

---

## Evitar

* Bibliotecas abandonadas
* Dependências redundantes
* Frameworks sobrepostos

---

# Padrões de Código

## TypeScript Obrigatório

Não utilizar:

```typescript
any
```

---

Preferir:

```typescript
unknown
```

ou tipagens explícitas.

---

# Componentes

Preferir:

```tsx
export function ComunicadoCard() {
}
```

---

Evitar:

```tsx
export default function
```

---

# Imports

Ordem obrigatória:

```typescript
1. Bibliotecas externas

2. Componentes

3. Serviços

4. Tipos

5. Estilos
```

---

# ESLint

Todo código deve passar sem erros.

---

## Execução

```bash
yarn lint
```

---

# Formatação

Utilizar Prettier.

---

## Execução

```bash
yarn format
```

---

# Git Hooks

Executar automaticamente:

* Lint
* Testes
* Formatação

---

# Husky

Exemplo:

```bash
pre-commit
```

---

# Testes

---

## Unitários

Cobrir:

* Hooks
* Componentes
* Utilitários

---

## Integração

Validar:

* Fluxos de tela
* Consumo de APIs
* Estados

---

## E2E

Validar:

* Login
* Navegação
* Jornadas críticas

---

# Cobertura

Meta mínima:

```text
80%
```

---

# Tratamento de Erros

Toda exceção deve possuir tratamento visual.

---

## Exemplos

* Página não encontrada
* Falha de autenticação
* Erro de API
* Timeout

---

# Loading States

Toda operação assíncrona deve possuir feedback.

---

## Exemplos

* Skeleton
* Spinner
* Placeholder

---

# Acessibilidade

Obrigatório seguir WCAG 2.1 AA.

---

## Requisitos

* Labels
* Contraste adequado
* Navegação por teclado
* Leitores de tela

---

# Responsividade

Layouts obrigatórios:

| Dispositivo | Largura          |
| ----------- | ---------------- |
| Mobile      | até 768px        |
| Tablet      | 769px até 1024px |
| Desktop     | acima de 1024px  |

---

# Performance

Metas mínimas:

| Métrica | Meta    |
| ------- | ------- |
| LCP     | < 2.5s  |
| CLS     | < 0.1   |
| INP     | < 200ms |

---

# Observabilidade

Frontend deve registrar:

* Erros
* Exceções
* Performance
* Navegação

---

## Ferramentas

* Sentry
* OpenTelemetry

---

# Segurança Frontend

Nunca armazenar:

* Senhas
* Chaves privadas
* Secrets

---

Evitar:

```javascript
localStorage
```

para informações críticas.

---

# CI/CD

Pipeline deve executar:

```text
Install

Lint

Tests

Build

Security Scan

Deploy
```

---

# Checklist de Inicialização

Antes de iniciar o desenvolvimento verificar:

* [ ] Node LTS instalado
* [ ] Yarn ou pnpm configurado
* [ ] Quasar App Vite configurado
* [ ] Variáveis configuradas
* [ ] Build funcionando
* [ ] ESLint configurado
* [ ] Prettier configurado
* [ ] Husky configurado
* [ ] Testes configurados
* [ ] Quasar SCSS e design tokens configurados
* [ ] Observabilidade configurada

---

# Critérios de Aceite

O frontend será considerado corretamente inicializado quando:

* Estrutura de diretórios estiver criada.
* Ambientes estiverem configurados.
* Build estiver funcionando.
* Pipeline estiver operacional.
* Ferramentas de qualidade estiverem configuradas.
* Testes estiverem habilitados.
* Observabilidade estiver configurada.
* Padrões definidos neste documento forem seguidos por todas as equipes.
