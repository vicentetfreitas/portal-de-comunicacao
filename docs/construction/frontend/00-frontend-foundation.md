# Frontend Foundation — Sprint 0

| Item | Valor |
|------|-------|
| Sprint | **Sprint 0 — Frontend Foundation** |
| Camada | Construction |
| Tipo | Especificação de construção — define **o que** será entregue |
| Status | **READY_FOR_REVIEW** |
| Versão | 1.1 |
| Data | 2026-07-15 |

---

# 1. Purpose

O **Frontend Foundation (Sprint 0)** estabelece a infraestrutura compartilhada do Portal de Comunicação antes da implementação de qualquer Feature de negócio.

O backend integrado foi validado na Sprint de Integração. O frontend legado em produção possui ~294 componentes Vue, 100 rotas e acoplamento ao CMS WordPress (`portaldecomunicacao/v1`). A nova solução consome o backend Spring Boot via `/api/v1`, com autenticação Stateless por cookies HttpOnly e RBAC (conforme `specs/features/authentication/`).

Sem esta fundação, nenhuma Feature frontend — começando por **FT-AUTH** — pode ser implementada de forma consistente, testável e alinhada à stack oficial.

Esta Sprint entrega exclusivamente a **plataforma transversal**: bootstrap, tema, design system base, layouts, roteamento, cliente HTTP, integração de autenticação (fundação), componentes compartilhados e infraestrutura de testes.

---

# 2. Objectives

A Sprint 0 deve:

1. **Inicializar** o projeto frontend com a stack oficial (Vue 3, Quasar, TypeScript, Pinia, Axios).
2. **Estabelecer** a identidade visual Unimed (tema, tokens, tipografia, ícones) como base reutilizável.
3. **Disponibilizar** um design system mínimo (átomos e moléculas essenciais) para composição das Features.
4. **Construir** os layouts e o app shell (header, sidebar, footer) sem telas de negócio.
5. **Configurar** a infraestrutura de roteamento (Vue Router, history mode, guards estruturais, rotas utilitárias).
6. **Implementar** o cliente HTTP para consumo do backend `/api/v1`, com suporte a cookies, CSRF e envelope `ApiResponse`.
7. **Preparar** a fundação de integração de autenticação compatível com FT-AUTH (sem implementar fluxos de login/logout).
8. **Habilitar** a infraestrutura de testes (Vitest, Playwright) e qualidade de código.
9. **Garantir** que o artefato compile, execute localmente e integre-se ao ambiente Docker Compose existente.

---

# 3. Scope

## 3.1 Bootstrap

| Entregável | Descrição |
|------------|-----------|
| Projeto Quasar App Vite | Estrutura inicial em `frontend/` com Vue 3, TypeScript e Quasar Framework |
| Organização de diretórios | Conforme `docs/technology/02-development-standards.md`: `pages`, `layouts`, `components`, `services`, `stores`, `router` |
| Configuração de ambientes | Variáveis externas por ambiente (Local, DEV, HML, PRD) — sem valores hardcoded |
| Build e execução local | Scripts de desenvolvimento, build de produção (SPA, history router) e integração com Docker Compose |
| Pinia | Configuração do gerenciamento de estado global (stores vazias ou de infraestrutura) |
| Qualidade de código | Ferramentas de lint e formatação configuradas e executáveis via pipeline |
| i18n base | Infraestrutura de internacionalização com locale padrão `pt-BR` (sem tradução de Features) |

## 3.2 Theme

| Entregável | Descrição |
|------------|-----------|
| Design tokens | Cores, tipografia, espaçamento, raios, sombras e elevação alinhados à marca Unimed |
| Quasar variables | `quasar.variables.scss` sincronizado com tokens CSS |
| Tipografia corporativa | Referência às fontes Unimed (Sans, Serif, Slab) documentadas no discovery |
| Modo claro/escuro | Suporte a variantes light/dark da marca (infraestrutura de tema, sem telas de configuração de negócio) |
| Ícones | Material Design Icons v7 via Quasar (`mdi-v7`) |

**Evidência de referência (produção):** cor primária `#007B5E`, escala de espaçamento 8px, breakpoints responsivos.

## 3.3 Design System

| Entregável | Descrição |
|------------|-----------|
| Estrutura atômica | Organização `components/ds/` em atoms, molecules e organisms (pastas e convenções) |
| Átomos essenciais | Componentes base: botão, input, select, ícone, badge, avatar |
| Moléculas essenciais | Componentes base: card, breadcrumbs, search input, page header, modal/dialog |
| Organismos de infraestrutura | Toast/notify wrapper, data table base, form card |
| Showcase de validação | Página ou rota de demonstração dos componentes base (equivalente funcional ao `ComponentsShowcasePage` do legado, sem dependência de negócio) |

**Limite de escopo:** componentes de domínio (documentos, pastas, usuários, notícias) ficam fora desta Sprint.

## 3.4 Layouts

| Entregável | Descrição |
|------------|-----------|
| `AuthLayout` | Wrapper para fluxos de autenticação (estrutura vazia, pronta para FT-AUTH) |
| `MainLayout` | Shell autenticado: header, sidebar, footer, área de conteúdo (`router-view`) |
| `AdminLayout` | Variante de layout administrativo (estrutura, sem menus de negócio) |
| `PublicLayout` | Layout para páginas públicas |
| App shell | `AppHeader`, `AppSidebar`, `AppFooter` como componentes estruturais (sem itens de menu de Features) |
| Responsividade | Comportamento mobile/tablet/desktop conforme breakpoints documentados no discovery |

## 3.5 Routing

| Entregável | Descrição |
|------------|-----------|
| Vue Router 4 | History mode (SPA) |
| Rotas utilitárias | Raiz (`/` → redirect), página 404 (`/:catchAll(.*)*`), rota de placeholder autenticado |
| Infraestrutura de guards | Interface e registro de guards globais (autenticação, autorização) — implementação de regras de negócio em Sprint posterior |
| Meta de rota | Convenção para `meta` (título, breadcrumb, roles, capabilities) |
| Lazy loading | Padrão de importação condicional de páginas |
| Redirects estruturais | `/app` como área autenticada (destino placeholder até FT-AUTH) |

**Limite de escopo:** rotas de Features (login, onboarding, CRUDs, painéis dinâmicos) ficam fora desta Sprint.

## 3.6 HTTP Client

| Entregável | Descrição |
|------------|-----------|
| Instância Axios | Cliente HTTP configurado para o backend Spring Boot |
| Base URL | Prefixo `/api/v1` via configuração externa por ambiente |
| Credenciais | `withCredentials: true` para cookies HttpOnly |
| Envelope de resposta | Parsing de `ApiResponse<T>` (sucesso) e `ErrorResponse` (erro) conforme contrato FT-AUTH |
| Interceptors | Request (CSRF, correlation ID) e response (tratamento centralizado de erros HTTP) |
| Timeout | Configurável por ambiente |
| Tratamento de erros | Mapeamento de status HTTP para feedback visual (infraestrutura, sem mensagens de negócio) |

**Evidência de contrato:** `specs/features/authentication/api.md` — cookies `access_token`, `refresh_token`, `XSRF-TOKEN`; header `X-XSRF-TOKEN` em requisições mutáveis.

## 3.7 Authentication Integration

Fundação técnica para FT-AUTH. **Não** inclui telas, fluxos ou regras de autenticação.

| Entregável | Descrição |
|------------|-----------|
| Política de armazenamento | Proibição de tokens em `localStorage`/`sessionStorage` (RN-AUTH-007) |
| CSRF | Mecanismo de leitura do cookie `XSRF-TOKEN` e envio do header `X-XSRF-TOKEN` em POST/PUT/DELETE |
| Auth store (Pinia) | Store estrutural para estado de sessão (sem lógica de login/logout) |
| Hooks de interceptor | Pontos de extensão para renovação automática (`401 → refresh → retry`) a serem completados em FT-AUTH |
| Guard scaffolding | Estrutura de guards de rota preparada para proteção de rotas autenticadas |
| AuthLayout integrado | Layout de autenticação disponível para rotas públicas de auth |

**Dependência explícita:** endpoints de autenticação do backend (FT-AUTH backend) não são pré-requisito de construção da fundação; a fundação deve ser validável com mocks ou backend indisponível.

## 3.8 Shared Components

| Entregável | Descrição |
|------------|-----------|
| Feedback visual | Loading states (skeleton, spinner), empty states, error boundaries |
| Notificações | Infraestrutura de toast/notify (Quasar Notify + wrapper DS) |
| Formulários base | Composable ou utilitário de validação reutilizável (regras genéricas: required, email) |
| Composables transversais | `useLoading`, `useStandardErrorHandling`, `useTheme` (infraestrutura) |
| Constantes | Rotas nomeadas, configuração de layout, ícones de breadcrumb (estrutura) |
| Tipos base | Tipos TypeScript compartilhados (`ApiResponse`, `ErrorResponse`, paginação) |

## 3.9 Testing Infrastructure

| Entregável | Descrição |
|------------|-----------|
| Vitest | Configuração de testes unitários com Vue Test Utils |
| Playwright | Configuração de testes E2E com projeto base |
| Scripts de teste | Comandos `test`, `test:unit`, `test:e2e` executáveis |
| Testes de fumaça | Pelo menos um teste unitário e um E2E de bootstrap (aplicação inicia, rota 404 renderiza) |
| Integração CI | Testes incluídos no pipeline GitHub Actions |

---

# 4. Out of Scope

Os itens abaixo são **intencionalmente excluídos** da Sprint 0.

## 4.1 Business Features

| Feature | Motivo |
|---------|--------|
| FT-AUTH (telas e fluxos) | Sprint 1 — depende da fundação desta Sprint |
| FT-SINGULAR | Sprint 2 |
| FT-AREA | Sprint 2 |
| FT-PERMISSAO | Sprint 2 |
| FT-PERFIL | Sprint 2 |
| FT-EQUIPE | Sprint 3 |
| FT-COLABORADOR | Sprint 3 |
| FT-DOCUMENTO | Sprint 3 |
| FT-PASTA | Sprint 3 |
| FT-USUARIO | Sprint 3 |
| FT-COMUNICADO | Sprint 4 |
| FT-NOTIFICACAO | Sprint 4 |
| FT-BUSCA | Sprint 4 |
| FT-FEDERACAO | Sprint 4 |
| FT-AREA-PUBLICA | Sprint 4 |

## 4.2 CRUDs e Telas de Negócio

- Listagens, formulários, hubs e painéis de qualquer entidade (singulares, áreas, equipes, colaboradores, usuários, documentos, pastas, comunicados).
- Placeholder pages de administração com ações de negócio.
- Onboarding, seleção de papel, seleção de domínio e fluxos de primeiro acesso.
- Dashboard administrativo com métricas de negócio.
- Wizard de configuração (`AdminWizardPage`).
- Hub de colaboração, ferramentas e configurações do portal.

## 4.3 Integrações Excluídas

| Item | Motivo |
|------|--------|
| CMS WordPress | Backend oficial é Spring Boot `/api/v1`; APIs legadas `portaldecomunicacao/v1` não fazem parte da nova fundação |
| Zimbra (fluxo de login) | Implementação do redirect e callback em FT-AUTH (Sprint 1) |
| Email gate (IMAP/SMTP) | Não previsto na arquitetura TO-BE de autenticação |
| VLibras / integrações acessórias | Fora do MVP Etapa 1 |

## 4.4 Outros

- Migração de componentes do frontend legado.
- Multi-idioma além da infraestrutura base (`en-US` completo).
- PWA, SSR e builds alternativos.
- Observabilidade avançada (Sentry, OpenTelemetry) — não constam na stack oficial.
- Analytics e rastreamento de eventos.
- Regras de negócio, contratos de API de Features e critérios de aceite de Features.

---

# 5. Inputs

Documentos utilizados como única fonte de evidência para esta especificação:

| Documento | Uso |
|-----------|-----|
| `docs/discovery/frontend-production-discovery.md` | Inventário do legado: stack, estrutura, layouts, DS, tema, roteamento, padrões de produção |
| `docs/discovery/frontend-feature-mapping.md` | Mapeamento Features × Sprints; Shared Infrastructure = Sprint 0; cadeia de dependências |
| `docs/technology/01-technology-stack.md` | Stack oficial: Vue 3, Quasar, TypeScript, Pinia, Axios, Vitest, Playwright |
| `docs/technology/02-development-standards.md` | Estrutura de diretórios, convenções, padrões de código frontend |
| `docs/technology/03-environment-strategy.md` | Estratégia de ambientes, configuração externa, build único |
| `docs/technology/04-decision-log.md` | Decisões DEC-004 (Vue/Quasar), DEC-007 (REST `/api/v1`), DEC-008 (JWT/RBAC) |
| `docs/construction/delivery/01-mvp.md` | Etapa 1 — Fundação da Plataforma; escopo MVP; dependências frontend |
| `specs/features/authentication/specification.md` | Arquitetura de autenticação TO-BE; restrições de sessão e cookies |
| `specs/features/authentication/api.md` | Contratos HTTP, cookies, CSRF, envelope de resposta |
| `specs/features/authentication/tasks.md` | Tarefas frontend (TASK-AUTH-FE-001 a FE-011) como referência de dependência da fundação |
| `specs/features/authentication/use-cases.md` | Fluxos de autenticação previstos (implementação em Sprint posterior) |
| `specs/features/authentication/acceptance-tests.md` | Critérios de aceite de FT-AUTH (não aplicáveis integralmente à Sprint 0) |
| `specs/features/authentication/decisions.md` | Decisões arquiteturais de autenticação |

---

# 6. Official Stack

Conforme `docs/technology/01-technology-stack.md` e `docs/technology/04-decision-log.md` (DEC-004).

## 6.1 Frontend

| Categoria | Tecnologia |
|-----------|------------|
| Framework | Vue 3 |
| UI Framework | Quasar Framework |
| Linguagem | TypeScript |
| Build | Quasar App Vite |
| Modo de deploy | SPA (Vue Router history mode) |
| Estado global | Pinia |
| HTTP | Axios |
| Roteamento | Vue Router 4 |

## 6.2 Testes

| Categoria | Tecnologia |
|-----------|------------|
| Unitários | Vitest + Vue Test Utils |
| E2E | Playwright |

## 6.3 Integração

| Categoria | Tecnologia |
|-----------|------------|
| API Backend | REST `/api/v1` (Spring Boot) |
| Autenticação | JWT em cookies HttpOnly + CSRF (contrato FT-AUTH) |
| Autorização | RBAC (infraestrutura de guards; regras em Features) |

## 6.4 Ambientes e Entrega

| Categoria | Tecnologia |
|-----------|------------|
| Containerização | Docker |
| Orquestração local | Docker Compose |
| CI/CD | GitHub Actions |
| Controle de versão | Git (GitHub) |

## 6.5 Explicitamente Não Utilizadas

Conforme stack oficial — não fazem parte desta Sprint:

```text
Next.js / React
Angular
Node.js Backend
Tailwind CSS (como stack principal)
WordPress REST API (CMS legado)
localStorage / sessionStorage para tokens
```

---

# 7. Deliverables

Ao final da Sprint 0, o repositório deve conter:

| # | Deliverable | Critério de existência |
|---|-------------|------------------------|
| D-01 | Projeto `frontend/` inicializado | `quasar.config.ts`, `package.json`, build funcional |
| D-02 | Estrutura de diretórios oficial | `pages`, `layouts`, `components`, `services`, `stores`, `router` |
| D-03 | Configuração multi-ambiente | Variáveis externas para Local, DEV, HML, PRD |
| D-04 | Tema Unimed aplicado | Tokens CSS, Quasar variables, fontes referenciadas |
| D-05 | Design system base | Átomos, moléculas e organisms de infraestrutura em `components/ds/` |
| D-06 | Quatro layouts | `AuthLayout`, `MainLayout`, `AdminLayout`, `PublicLayout` |
| D-07 | App shell | `AppHeader`, `AppSidebar`, `AppFooter` (estrutura sem menus de negócio) |
| D-08 | Roteamento base | History mode, 404, redirects estruturais, guards scaffolding |
| D-09 | Cliente HTTP | Axios com `/api/v1`, credentials, CSRF, envelopes tipados |
| D-10 | Fundação de autenticação | Auth store estrutural, política anti-localStorage, hooks de interceptor |
| D-11 | Componentes compartilhados | Toast, loading, error handling, composables transversais |
| D-12 | Infraestrutura de testes | Vitest + Playwright configurados com testes de fumaça |
| D-13 | Pipeline CI | Lint, testes e build no GitHub Actions |
| D-14 | Página showcase DS | Rota de demonstração dos componentes base |
| D-15 | Documentação de construção | Status dos PKGs em `construction/frontend/pkg-fe-s0-XX/status.md` |

---

# 8. Sprint Packages

## PKG-FE-S0-01 — Project Bootstrap

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-01 |
| **Name** | Project Bootstrap |
| **Purpose** | Inicializar o projeto Quasar/Vue 3/TypeScript com estrutura, ambientes, Pinia, i18n base e qualidade de código |
| **Dependencies** | Backend Sprint 0 aprovado; Docker Compose local operacional |
| **Expected Deliverable** | Projeto compilável e executável em `frontend/`; diretórios oficiais; scripts dev/build/test; configuração de ambientes externa |

---

## PKG-FE-S0-02 — Theme

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-02 |
| **Name** | Theme |
| **Purpose** | Estabelecer identidade visual Unimed: design tokens, Quasar variables, tipografia, ícones e suporte light/dark |
| **Dependencies** | PKG-FE-S0-01 |
| **Expected Deliverable** | Arquivos de tokens SCSS/CSS; `quasar.variables.scss` alinhado; fontes corporativas referenciadas; tema aplicável globalmente |

---

## PKG-FE-S0-03 — Design System

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-03 |
| **Name** | Design System |
| **Purpose** | Criar biblioteca de componentes base (atoms, molecules, organisms de infraestrutura) reutilizáveis pelas Features |
| **Dependencies** | PKG-FE-S0-02 |
| **Expected Deliverable** | Componentes DS base exportados; convenção de nomenclatura `Ds*`; página showcase funcional |

---

## PKG-FE-S0-04 — Layouts

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-04 |
| **Name** | Layouts |
| **Purpose** | Entregar os quatro layouts e o app shell responsivo (header, sidebar, footer) |
| **Dependencies** | PKG-FE-S0-02, PKG-FE-S0-03 |
| **Expected Deliverable** | `AuthLayout`, `MainLayout`, `AdminLayout`, `PublicLayout`; `AppHeader`, `AppSidebar`, `AppFooter`; comportamento responsivo validado |

---

## PKG-FE-S0-05 — Routing

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-05 |
| **Name** | Routing |
| **Purpose** | Configurar Vue Router (history mode), rotas utilitárias, meta conventions, lazy loading e scaffolding de guards |
| **Dependencies** | PKG-FE-S0-04 |
| **Expected Deliverable** | Router operacional; rota 404; redirects `/` e `/app`; guards registrados (sem regras de negócio); layouts associados às rotas |

---

## PKG-FE-S0-06 — HTTP Client

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-06 |
| **Name** | HTTP Client |
| **Purpose** | Implementar cliente Axios para backend `/api/v1` com credentials, CSRF, envelopes tipados e tratamento centralizado de erros |
| **Dependencies** | PKG-FE-S0-01 |
| **Expected Deliverable** | Boot file ou módulo de API; tipos `ApiResponse`/`ErrorResponse`; interceptors request/response; timeout configurável |

---

## PKG-FE-S0-07 — Authentication Integration

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-07 |
| **Name** | Authentication Integration |
| **Purpose** | Preparar fundação técnica de autenticação compatível com FT-AUTH: auth store, política de cookies, CSRF, hooks de renovação e guard scaffolding |
| **Dependencies** | PKG-FE-S0-05, PKG-FE-S0-06 |
| **Expected Deliverable** | Auth store Pinia (estrutural); ausência de tokens em storage local; CSRF funcional em requisições mutáveis; pontos de extensão para refresh e proteção de rotas |

---

## PKG-FE-S0-08 — Shared Components

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-08 |
| **Name** | Shared Components |
| **Purpose** | Entregar componentes e composables transversais: notificações, loading, error handling, validação base e constantes |
| **Dependencies** | PKG-FE-S0-03, PKG-FE-S0-06 |
| **Expected Deliverable** | Toast/notify wrapper; composables `useLoading`, `useStandardErrorHandling`, `useTheme`; tipos e constantes compartilhados |

---

## PKG-FE-S0-09 — Testing Infrastructure

| Campo | Valor |
|-------|-------|
| **Identifier** | PKG-FE-S0-09 |
| **Name** | Testing Infrastructure |
| **Purpose** | Configurar Vitest, Playwright e integração CI com testes de fumaça |
| **Dependencies** | PKG-FE-S0-01, PKG-FE-S0-05 |
| **Expected Deliverable** | Vitest e Playwright operacionais; teste unitário de componente base; teste E2E de bootstrap (app carrega, 404 funciona); pipeline CI executando testes |

---

### Ordem de Execução

```text
PKG-FE-S0-01 (Bootstrap)
    ├── PKG-FE-S0-02 (Theme)
    │       └── PKG-FE-S0-03 (Design System)
    │               └── PKG-FE-S0-04 (Layouts)
    │                       └── PKG-FE-S0-05 (Routing)
    ├── PKG-FE-S0-06 (HTTP Client)
    │       ├── PKG-FE-S0-07 (Auth Integration) ← depende também de PKG-FE-S0-05
    │       └── PKG-FE-S0-08 (Shared Components) ← depende também de PKG-FE-S0-03
    └── PKG-FE-S0-09 (Testing) — pode iniciar após PKG-FE-S0-05
```

---

# 9. Acceptance Criteria

| ID | Critério | Verificação |
|----|----------|-------------|
| AC-FE-S0-001 | O projeto `frontend/` compila sem erros (`build` de produção) | Execução de build no CI |
| AC-FE-S0-002 | A aplicação inicia localmente via Docker Compose ou script dev | Smoke test manual ou E2E |
| AC-FE-S0-003 | A estrutura de diretórios segue `docs/technology/02-development-standards.md` | Revisão de estrutura |
| AC-FE-S0-004 | Variáveis de ambiente são externas; nenhuma URL de API hardcoded | Revisão de configuração |
| AC-FE-S0-005 | Tema Unimed aplicado: cor primária, tipografia e tokens CSS presentes | Inspeção visual + revisão de arquivos de tema |
| AC-FE-S0-006 | Design system base exporta átomos e moléculas essenciais documentados na Seção 3.3 | Página showcase renderiza componentes |
| AC-FE-S0-007 | Quatro layouts renderizam corretamente com app shell responsivo | Teste em viewports mobile, tablet e desktop |
| AC-FE-S0-008 | Vue Router opera em history mode; rota 404 exibe página de erro; `/` redireciona conforme definido | Teste E2E de navegação |
| AC-FE-S0-009 | Cliente HTTP aponta para `/api/v1` com `withCredentials: true` | Revisão de configuração Axios |
| AC-FE-S0-010 | Requisições mutáveis incluem header `X-XSRF-TOKEN` quando cookie CSRF presente | Teste unitário ou integração com mock |
| AC-FE-S0-011 | Nenhum token de autenticação é armazenado em `localStorage` ou `sessionStorage` | Revisão de código (RN-AUTH-007) |
| AC-FE-S0-012 | Envelopes `ApiResponse` e `ErrorResponse` possuem tipos TypeScript | Revisão de tipos |
| AC-FE-S0-013 | Auth store Pinia existe com estrutura para estado de sessão | Revisão de store |
| AC-FE-S0-014 | Guards de rota estão registrados e prontos para extensão | Revisão de router |
| AC-FE-S0-015 | Infraestrutura de toast/notify funciona para feedback de erro genérico | Teste manual ou unitário |
| AC-FE-S0-016 | Vitest executa pelo menos um teste unitário com sucesso | CI verde |
| AC-FE-S0-017 | Playwright executa pelo menos um teste E2E de bootstrap com sucesso | CI verde |
| AC-FE-S0-018 | Pipeline GitHub Actions executa lint, testes e build | CI verde |
| AC-FE-S0-019 | Nenhuma tela ou rota de Feature de negócio está implementada | Revisão de escopo |
| AC-FE-S0-020 | Nenhuma integração com CMS WordPress está presente | Revisão de services e configuração |

---

# 10. Definition of Done

A Sprint 0 — Frontend Foundation será considerada **concluída** quando:

1. **Todos os PKGs** (PKG-FE-S0-01 a PKG-FE-S0-09) estiverem com status `DONE`.
2. **Todos os deliverables** (D-01 a D-15) estiverem presentes no repositório.
3. **Todos os critérios de aceite** (AC-FE-S0-001 a AC-FE-S0-020) estiverem aprovados.
4. O **build de produção** gerar artefato SPA implantável.
5. O **pipeline CI** estiver verde (lint, testes, build).
6. A **revisão técnica** confirmar aderência à stack oficial (`docs/technology/`).
7. A **revisão de escopo** confirmar ausência de Features de negócio e integrações excluídas.
8. Os **artefatos de construction** dos PKGs estiverem atualizados com evidências de validação.

---

# 11. Risks

| ID | Risco | Impacto | Mitigação |
|----|-------|---------|-----------|
| R-FE-S0-01 | **Divergência legado × TO-BE** — o discovery documenta padrões do frontend em produção (WordPress JWT em localStorage, `portaldecomunicacao/v1`) incompatíveis com a arquitetura FT-AUTH | Retrabalho, vulnerabilidades de segurança | Seguir exclusivamente `specs/features/authentication/` e `docs/technology/`; não replicar padrões legados de armazenamento de token |
| R-FE-S0-02 | **Escopo do Design System** — o legado possui ~54 componentes DS; tentativa de portar todos na Sprint 0 | Atraso, diluição da fundação | Limitar DS aos componentes essenciais da Seção 3.3; componentes de domínio ficam nas Features |
| R-FE-S0-03 | **Dependência do backend** — fundação HTTP validada apenas com backend disponível | Bloqueio de testes integrados | Validar com mocks; testes de fumaça independentes de backend; integração real em FT-AUTH |
| R-FE-S0-04 | **Fontes corporativas** — arquivos de fonte Unimed podem não estar versionados (evidência no discovery) | Identidade visual incompleta | Definir fallback tipográfico; registrar dependência de assets corporativos |
| R-FE-S0-05 | **Complexidade de layouts** — o legado possui menus condicionais por papel com alta complexidade | Tentação de antecipar Features de permissão | Entregar shell estrutural sem itens de menu de negócio; menus em FT-PERMISSAO e Features subsequentes |
| R-FE-S0-06 | **CSRF + cookies em ambiente local** — configuração de CORS, SameSite e HTTPS pode dificultar desenvolvimento local | Falhas de autenticação em FT-AUTH | Alinhar com `docs/technology/03-environment-strategy.md`; validar proxy e cookies no Docker Compose |
| R-FE-S0-07 | **Documentação de construction desatualizada** — guias `01–06` reconciliados com DEC-004 em 2026-07-15 | Confusão durante implementação | Prevalece `00-frontend-foundation.md`; artefatos operacionais em `construction/frontend/`; stack em `docs/technology/` |

---

# 12. Next Sprint

Após a conclusão da Sprint 0, a próxima Feature a ser implementada no frontend é:

## FT-AUTH — Authentication & Session (Sprint 1)

| Item | Valor |
|------|-------|
| Feature ID | FT-AUTH |
| Sprint | Sprint 1 |
| Dependência | Sprint 0 concluída (layouts, HTTP client, auth foundation, guards scaffolding) |
| Escopo frontend | TASK-AUTH-FE-001 a TASK-AUTH-FE-011 |
| Entregas principais | Login (redirect Zimbra), logout, `/api/v1/auth/me`, guards de rota, renovação automática de token, tratamento de sessão expirada, CSRF em fluxos, opção "Lembrar-me" |
| MVP | **Mandatory** (`docs/construction/delivery/01-mvp.md` — Etapa 2, Controle de Acesso) |

**Sequência documentada:**

```text
Sprint 0  →  Shared Infrastructure (esta Sprint)
Sprint 1  →  FT-AUTH
Sprint 2  →  FT-SINGULAR, FT-AREA, FT-PERMISSAO, FT-PERFIL
Sprint 3  →  FT-EQUIPE, FT-COLABORADOR, FT-DOCUMENTO, FT-PASTA, FT-USUARIO
Sprint 4  →  FT-COMUNICADO, FT-NOTIFICACAO, FT-BUSCA, FT-FEDERACAO, FT-AREA-PUBLICA
```

Fonte: `docs/discovery/frontend-feature-mapping.md` — Sprint Dependency Chain.

---

# Referências

| Documento | Caminho |
|-----------|---------|
| Frontend Production Discovery | `docs/discovery/frontend-production-discovery.md` |
| Frontend Feature Mapping | `docs/discovery/frontend-feature-mapping.md` |
| Technology Stack | `docs/technology/01-technology-stack.md` |
| Development Standards | `docs/technology/02-development-standards.md` |
| Environment Strategy | `docs/technology/03-environment-strategy.md` |
| Decision Log | `docs/technology/04-decision-log.md` |
| MVP Definition | `docs/construction/delivery/01-mvp.md` |
| Construction Sprint 0 | `construction/frontend/` |
| FT-AUTH Specification | `specs/features/authentication/specification.md` |

---

# Status

**READY_FOR_REVIEW** — Especificação de construção da Sprint 0 — Frontend Foundation.

Artefatos operacionais: `construction/frontend/` (manifest, state, package index, PKG status).

Este documento define o escopo de construção. Não contém implementação, código Vue/Quasar ou guias de desenvolvimento passo a passo.
