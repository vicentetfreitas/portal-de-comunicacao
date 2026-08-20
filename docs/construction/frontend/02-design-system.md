# Design System

| Item | Valor |
|------|-------|
| Camada | Construction — Frontend |
| Sprint | Sprint 0 — Frontend Foundation |
| Versão | 1.1 |
| Status | Reconciliado — stack oficial DEC-004 |
| Especificação prevalecente | `00-frontend-foundation.md` |
| Artefatos operacionais | `construction/frontend/` |
| Escopo de reconstrução | [`docs/architecture/decisions/DS-RECONSTRUCTION-SCOPE-01.md`](../../architecture/decisions/DS-RECONSTRUCTION-SCOPE-01.md) |

> Guia complementar de design system. Ícones: MDI via Quasar (`mdi-v7`).
>
> Para o **alvo da reconstrução** (Quasar + Figma + Claude), prevalece `DS-RECONSTRUCTION-SCOPE-01`. Este guia permanece referência histórica da Sprint 0.

---

## Objetivo

Definir os padrões visuais, componentes, comportamentos e diretrizes de experiência do usuário do Portal de Comunicação.

Este documento estabelece uma linguagem visual única para toda a plataforma, garantindo consistência, acessibilidade, reutilização de componentes e escalabilidade da interface.

---

# Escopo

Esta documentação cobre:

* Identidade visual
* Design Tokens
* Tipografia
* Cores
* Espaçamentos
* Componentes
* Layout
* Responsividade
* Acessibilidade
* Padrões de UX

Não cobre:

* Regras de negócio
* Navegação
* Autenticação
* Integrações

---

# Princípios do Design System

Toda interface deve seguir:

* Consistência
* Clareza
* Simplicidade
* Acessibilidade
* Escalabilidade
* Reutilização
* Responsividade

---

# Arquitetura do Design System

```text
design-system
├── tokens
├── foundations
├── components
├── patterns
├── templates
└── guidelines
```

---

# Design Tokens

Os tokens representam a fonte única da verdade visual.

Nenhum componente deve utilizar valores hardcoded.

---

# Estrutura

```text
tokens
├── colors
├── typography
├── spacing
├── radius
├── shadows
├── z-index
└── breakpoints
```

---

# Cores

## Princípios

As cores devem transmitir:

* Confiança
* Clareza
* Legibilidade
* Acessibilidade

---

# Paleta Primária

```text
Primary-50
Primary-100
Primary-200
Primary-300
Primary-400
Primary-500
Primary-600
Primary-700
Primary-800
Primary-900
```

---

# Paleta Secundária

```text
Secondary-50
Secondary-100
Secondary-200
Secondary-300
Secondary-400
Secondary-500
Secondary-600
Secondary-700
Secondary-800
Secondary-900
```

---

# Cores Semânticas

## Success

```text
Success-500
```

Utilizada para:

* Sucesso
* Confirmações
* Operações concluídas

---

## Warning

```text
Warning-500
```

Utilizada para:

* Atenção
* Alertas
* Validações

---

## Error

```text
Error-500
```

Utilizada para:

* Falhas
* Erros
* Bloqueios

---

## Info

```text
Info-500
```

Utilizada para:

* Informações
* Notificações
* Feedback neutro

---

# Escala de Cinzas

```text
Gray-50
Gray-100
Gray-200
Gray-300
Gray-400
Gray-500
Gray-600
Gray-700
Gray-800
Gray-900
```

---

# Tipografia

## Fonte Principal

```text
Inter
```

---

## Fonte Alternativa

```text
sans-serif
```

---

# Escala Tipográfica

## Display

```text
Display-Large
Display-Medium
Display-Small
```

---

## Headings

```text
H1
H2
H3
H4
H5
H6
```

---

## Body

```text
Body-Large
Body-Medium
Body-Small
```

---

## Caption

```text
Caption
```

---

# Hierarquia

| Elemento | Uso              |
| -------- | ---------------- |
| H1       | Título principal |
| H2       | Seção            |
| H3       | Subseção         |
| Body     | Conteúdo         |
| Caption  | Apoio            |

---

# Espaçamentos

Utilizar escala baseada em múltiplos de 4.

---

## Tokens

```text
0
4
8
12
16
20
24
32
40
48
64
80
96
```

---

# Border Radius

```text
radius-xs
radius-sm
radius-md
radius-lg
radius-xl
radius-full
```

---

# Sombras

```text
shadow-xs
shadow-sm
shadow-md
shadow-lg
shadow-xl
```

---

# Breakpoints

## Mobile

```text
0px - 768px
```

---

## Tablet

```text
769px - 1024px
```

---

## Desktop

```text
1025px+
```

---

# Grid System

Padrão:

```text
12 colunas
```

---

# Layout

## Container

```text
max-width: 1440px
```

---

## Content Width

```text
max-width: 1280px
```

---

# Componentes

Todos os componentes devem ser reutilizáveis.

---

# Estrutura

```text
components
├── atoms
├── molecules
├── organisms
└── layouts
```

---

# Atoms

Componentes básicos.

Exemplos:

```text
Button
Input
Label
Icon
Badge
Spinner
```

---

# Molecules

Combinação de componentes.

Exemplos:

```text
SearchBar
FormField
UserCard
```

---

# Organisms

Blocos complexos.

Exemplos:

```text
Header
Sidebar
DataTable
FiltersPanel
```

---

# Layouts

Estruturas de página.

Exemplos:

```text
AuthenticatedLayout
PublicLayout
AdminLayout
```

---

# Buttons

## Variantes

```text
Primary
Secondary
Ghost
Outline
Danger
```

---

# Estados

```text
Default
Hover
Focus
Disabled
Loading
```

---

# Inputs

Todo input deve possuir:

* Label
* Placeholder
* Estado de erro
* Estado desabilitado
* Mensagem de ajuda

---

# Feedback Components

## Alert

```text
Success
Info
Warning
Error
```

---

## Toast

Mensagens rápidas.

---

## Modal

Confirmações e interações críticas.

---

## Empty State

Obrigatório para listas vazias.

---

# Data Display

## Table

Utilizar paginação.

---

## Card

Utilizar para conteúdos resumidos.

---

## Timeline

Utilizar para histórico.

---

## Badge

Utilizar para status.

---

# Ícones

Biblioteca padrão:

```text
Material Design Icons v7 (mdi-v7) via @quasar/extras
```

---

# Responsividade

Todos os componentes devem funcionar em:

* Mobile
* Tablet
* Desktop

---

# Mobile First

Implementação obrigatória.

---

# Acessibilidade

Obrigatório seguir WCAG 2.1 AA.

---

# Requisitos

Todos os componentes devem possuir:

* Navegação por teclado
* Estados de foco
* Contraste adequado
* Compatibilidade com leitores de tela

---

# Contraste

Mínimo:

```text
4.5:1
```

---

# Focus

Todo componente interativo deve possuir:

```text
focus-visible
```

---

# ARIA

Utilizar quando necessário.

Exemplos:

```html
aria-label

aria-describedby

aria-expanded
```

---

# Experiência do Usuário

Interfaces devem:

* Ser previsíveis
* Reduzir esforço cognitivo
* Evitar sobrecarga visual
* Fornecer feedback imediato

---

# Loading States

Obrigatórios para:

* Consultas
* Envios
* Processamentos

---

# Skeleton

Preferencial para carregamento.

---

# Estados de Tela

Toda página deve possuir:

## Loading

```text
Carregando...
```

---

## Success

```text
Operação concluída
```

---

## Empty

```text
Nenhum resultado encontrado
```

---

## Error

```text
Ocorreu um erro
```

---

# Dark Mode

Preparar componentes para suporte futuro.

---

# Documentação dos Componentes

Todo componente deve possuir:

* Descrição
* Props
* Exemplos
* Estados
* Regras de uso

---

# Storybook

Obrigatório para documentação visual.

Estrutura:

```text
stories
├── atoms
├── molecules
├── organisms
└── layouts
```

---

# Testes Visuais

Validar:

* Estados
* Responsividade
* Acessibilidade

---

# Governança

Novos componentes devem:

1. Ser reutilizáveis.
2. Possuir documentação.
3. Possuir testes.
4. Seguir os tokens oficiais.

---

# Checklist

Antes de publicar um componente:

* [ ] Utiliza Design Tokens
* [ ] Responsivo
* [ ] Acessível
* [ ] Documentado
* [ ] Testado
* [ ] Sem valores hardcoded
* [ ] Compatível com tema global

---

# Critérios de Aceite

O Design System será considerado aderente quando:

* Todos os componentes utilizarem tokens oficiais.
* Não existirem estilos duplicados.
* Houver consistência visual entre módulos.
* Todos os componentes forem acessíveis.
* Todos os componentes estiverem documentados.
* O sistema suportar evolução sem quebra de identidade visual.
* O frontend seguir integralmente os padrões definidos neste documento.
