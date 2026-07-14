# Discovery — Current Architecture

## Objetivo

Consolidar a arquitetura atual do Portal de Comunicação sintetizando exclusivamente os resultados aprovados dos documentos Discovery 01–06, sem nova descoberta.

**Nível de confiança da consolidação:** Alto onde os seis documentos convergem; Médio onde há divergências documentadas entre camadas; Baixo para componentes marcados como PARCIAL, ÓRFÃO ou LEGADO.

**Fontes:** `01-current-modules.md`, `02-current-rbac.md`, `03-current-data-model.md`, `04-current-endpoints.md`, `05-current-integrations.md`, `06-current-infrastructure.md`.

---

## Resumo Executivo

| Domínio | Situação |
|---|---|
| Aplicação | SPA Quasar/Vue + CMS WordPress (MU-plugin) como API principal; 27 módulos funcionais (18 ATIVO, 8 PARCIAL, 1 LEGADO) |
| Dados | MySQL externo + WordPress Core (CPTs, taxonomias, user meta) + 5 tabelas customizadas + filesystem de uploads |
| Segurança | JWT nativo + 6 roles canônicos + 47 capabilities `portal_*` + context switching via headers |
| Integrações | Zimbra (auth), backend PHP legado, SSE, webhooks, email; 9 integrações órfãs no frontend |
| Infraestrutura | Docker Swarm + Traefik (dev/prd), Nginx local, GitLab CI + Harbor; MySQL e GFS externos |

---

## Visão Geral da Arquitetura

O Portal de Comunicação é uma aplicação web composta por SPA Quasar/Vue (frontend) que consome a API REST WordPress `portaldecomunicacao/v1` exposta pelo CMS. O CMS opera como camada de aplicação e persistência lógica sobre WordPress Core, com MU-plugin dedicado (`portaldecomunicacao`) contendo Controllers, Services e lógica de autenticação (JWT, Zimbra, Session).

Os dados residem em MySQL externo (sem container gerenciado), WordPress (`wp_users`, `wp_posts`, `wp_terms`, `wp_postmeta`, `wp_usermeta`) e filesystem `wp-content/uploads/portaldecomunicacao/`. Tabelas customizadas armazenam auditoria e notificações (dois subsistemas paralelos).

A autenticação corporativa utiliza Zimbra (IMAP/SMTP/SOAP); após login, JWT transportado via `Authorization: Bearer`. Autorização combina roles WordPress, capabilities `portal_*`, ACL por documento/pasta e headers de contexto (`X-Active-Role`, `X-User-Area-Id`, `X-User-Team-Id`).

Integrações ativas incluem SSE para notificações, `wp_mail`/webhooks para canais opcionais e `BackendSync` para backend PHP legado. Um serviço backend separado (`backend/routes/api.php`) coexiste como legado.

Em infraestrutura, três ambientes (local, dev/homologação, produção) executam containers Docker via Swarm, com Traefik publicando frontend e CMS em dev/prd, e Nginx reverso unificando stack local. Deploy via GitLab CI e Harbor Registry.

---

## Camada de Apresentação

### Frontend

| Componente | Finalidade |
|---|---|
| SPA Quasar/Vue | Interface do portal; roteamento client-side, layouts e páginas por módulo |
| Stores Pinia | Estado de auth (`auth-core`, `auth-roles`, `auth-context`), usuário, documentos, notificações, onboarding, analytics |
| Services TypeScript | Clientes HTTP (`PortalApiService`, `AuthService`, `AreasService`, `TeamsService`, `GlobalSearchService`, etc.) |
| Axios (`api`, `cmsApi`) | Consumo REST com interceptors JWT, headers RBAC e cache-buster em dev |
| `boot/axios.ts` | SSOT de URLs (`PORTAL_API_BASE`, `WP_REST_ROOT`) e health check `/status` |

### Módulos Funcionais

| Módulo | Status |
|---|---|
| Autenticação e Sessão | ATIVO |
| Usuários | ATIVO |
| RBAC | ATIVO |
| Singulares | ATIVO |
| Áreas | ATIVO |
| Equipes | ATIVO |
| Colaboradores | ATIVO |
| Documentos | ATIVO |
| Pastas e Diretórios | ATIVO |
| Permissões de Pastas | ATIVO |
| Solicitação de Permissões | PARCIAL |
| Notificações | ATIVO |
| Onboarding | PARCIAL |
| Auditoria | ATIVO |
| Armazenamento e Upload | ATIVO |
| Configuração do Portal | ATIVO |
| API — Saúde e Utilitários | ATIVO |
| Diagnóstico | ATIVO |
| Busca Global | PARCIAL |
| Analytics | PARCIAL |
| Comunicados | PARCIAL |
| Fique por Dentro | PARCIAL |
| Convidados | PARCIAL |
| Central de Colaboração | PARCIAL |
| Navegação e Interface | ATIVO |
| Cache | ATIVO |
| Backend PHP Legado | LEGADO |

*Fonte: `01-current-modules.md` — 27 módulos.*

---

## Camada de Aplicação

### CMS

| Componente | Finalidade |
|---|---|
| WordPress Core | Runtime CMS, usuários nativos, REST base `wp-json` |
| MU-Plugin `portaldecomunicacao` | API principal, regras de negócio, autenticação e RBAC |
| Controllers REST | 12 controllers ativos + `EndpointsRegistry`, `DocumentsManager`, `FoldersManager` |
| Middleware | `AuthMiddleware`, `CorsMiddleware`, `RateLimitMiddleware` |
| Classes Auth | `JWT`, `ZimbraAuth`, `Session`, `BackendSync` |
| Plugins WordPress | ACF, `jwt-auth-minimal`, SMTP (`easy-wp-smtp`, `wp-mail-smtp`) |
| ACF JSON (`acf-json/`) | Metadados estruturados de singulares, áreas, colaboradores, documentos |

### Serviços

| Serviço | Módulo |
|---|---|
| UsersService | Usuários |
| RBACService | RBAC |
| SingularesService | Singulares |
| AreasService | Áreas |
| TeamsService | Equipes |
| DocumentsService | Documentos |
| FoldersService | Pastas e Diretórios |
| FoldersPermissionsService | Permissões de Pastas |
| HierarchicalFoldersAutoCreationService | Pastas e Diretórios |
| StorageService | Armazenamento e Upload |
| NotificationsService / NotificationsManager | Notificações |
| NotificationChannel | Notificações (email, webhook, banco) |
| AuditService | Auditoria |
| CacheService | Cache |

### APIs

| Namespace | Quantidade de Endpoints |
|---|---|
| `portaldecomunicacao/v1` | ~98 registros de rota (~80 protegidos, 18 públicos) |
| `jwt-auth/v1` | Parcial (validação alternativa no frontend) |
| `backend/routes/api.php` | 20 rotas legadas |
| Endpoints órfãos (frontend sem CMS) | 28 |

*Fonte: `04-current-endpoints.md`.*

---

## Camada de Segurança

### Autenticação

| Mecanismo | Evidência |
|---|---|
| Zimbra (IMAP/SMTP/SOAP) | `ZimbraAuth.php`, `AuthController.php` |
| JWT nativo (HS256) | `JWT.php`, `AuthController.php` |
| JWT plugin WordPress | `jwt-auth-minimal` (PARCIAL) |
| Session + user meta | `Session.php`, `localStorage` (`boot/axios.ts`) |
| Login público | `POST /auth/login` — `__return_true` |

### Autorização

| Componente | Evidência |
|---|---|
| 6 roles canônicos CMS | `RBACService::CANONICAL_ROLES` |
| 47 capabilities `portal_*` | `RBACService::CANONICAL_CAPABILITIES` |
| ACL por documento/pasta | `DocumentsService`, `FoldersPermissionsService` |
| Checks por controller | `checkJwtAuth`, `checkAdminPermission`, `checkUserAccess` |
| Guards frontend | `guards.ts` (permissivo — não aplica `meta.roles`) |

### Contextos

| Contexto | Evidência |
|---|---|
| Global (`administrator`) | `portal_manage_all`, endpoints públicos de saúde |
| Singular | `user.context.singular_id`, rotas `/singulares/*`, CPT `singular` |
| Área | `X-User-Area-Id`, taxonomia `portal_area`, `FoldersPermissionsService` |
| Equipe | `X-User-Team-Id`, CPT/taxonomia `team`, role `team_owner` |
| Usuário (pessoal) | `colaborador_slug`, pastas privadas em filesystem |
| Role switching | `X-Active-Role` via `auth-roles.setActiveRole()` → Axios interceptors |

*Fonte: `02-current-rbac.md`.*

---

## Camada de Dados

### Entidades Principais

| Entidade | Tipo |
|---|---|
| Usuário | WordPress User |
| Colaborador | WordPress User (derivado) |
| Singular | CPT (`singular`) |
| Área | Taxonomia (`portal_area`) |
| Equipe | Taxonomia (`team`) / CPT (`team`) — uso divergente |
| Documento | CPT (`portal_documento`) |
| Pasta | Taxonomia (`portal_pasta`) |
| Arquivo Físico | Objeto Virtual (filesystem) |
| Registro de Auditoria | Tabela customizada (`audit_log`) |
| Notificação (Portal) | Tabela customizada (`portal_notifications`) |
| Notificação (PDC) | Tabela customizada (`pdc_notifications` + fila) |
| Papel RBAC | Objeto Virtual (`wp_usermeta`, `wp_options`) |
| Configurações do Portal | ACF (`wp_options`) |

### Persistência

| Estrutura | Finalidade |
|---|---|
| `wp_users` / `wp_usermeta` | Identidade, roles, contexto organizacional, quotas |
| `wp_posts` (CPTs) | Singulares, documentos, equipes (CPT) |
| `wp_terms` (taxonomias) | Áreas, pastas, equipes (taxonomia) |
| `wp_postmeta` | Metadados de documentos, compartilhamento (`portal_doc_sharing`) |
| `{prefix}audit_log` | Eventos de auditoria RBAC |
| `{prefix}portal_notifications` | Notificações in-app (subsistema 1) |
| `{prefix}pdc_notifications` + `_queue` | Notificações e fila (subsistema 2) |
| `wp-content/uploads/portaldecomunicacao/` | Binários de documentos |
| WordPress Transients | Cache com invalidação por tags |

*Fonte: `03-current-data-model.md`.*

---

## Camada de Integração

| Integração | Tipo | Status |
|---|---|---|
| Zimbra (IMAP/SMTP/SOAP) | Autenticação | ATIVA |
| JWT Portal (nativo) | Autenticação | ATIVA |
| JWT Auth WordPress (`jwt-auth/v1`) | Autenticação | PARCIAL |
| API Portal (`portaldecomunicacao/v1`) | API REST | ATIVA |
| WordPress REST (`wp-json`) | API REST | ATIVA |
| Backend PHP Legado | API REST | LEGADA |
| BackendSync (CMS → Backend) | API REST | PARCIAL |
| Notificações SSE | Streaming | ATIVA |
| Email (`wp_mail`) | Email | PARCIAL |
| Webhook (`pdc_webhook_url`) | API REST | PARCIAL |
| Filesystem uploads | Storage | ATIVA |
| MySQL externo | Banco Externo | ATIVA |
| Global Search (composição) | API REST | ATIVA |
| Analytics / Document Sharing / Permission Requests | API REST | ÓRFÃ |

*Fonte: `05-current-integrations.md`.*

---

## Camada de Infraestrutura

| Componente | Tipo | Status |
|---|---|---|
| Frontend (container) | Container | ATIVO |
| CMS WordPress (container) | Container | ATIVO |
| Backend PHP (container) | Container | LEGADO |
| Server Nginx (container) | Container | PARCIAL (local apenas) |
| MySQL (container) | Container | LEGADO (removido; banco externo) |
| Redis (container) | Container | PARCIAL (compose isolado) |
| Traefik | Proxy | ATIVO (dev/prd Swarm) |
| Nginx (server + CMS interno + frontend prod) | Proxy | ATIVO |
| GitLab CI | CI/CD | ATIVO |
| Harbor Registry | CI/CD | ATIVO |
| Docker Swarm | Orquestração | ATIVO (dev/prd) |
| GFS (`/mnt/gfs/*`, `/mnt/portalcom`) | Storage | ATIVO (dev/prd) |
| Rede `traefik-public` | Rede | ATIVO |
| Rede `private` | Rede | ATIVO |

*Fonte: `06-current-infrastructure.md`.*

---

## Fluxo Arquitetural Atual

```
Usuário (navegador)
  → Frontend SPA (Quasar/Vue)
    → Axios (JWT + headers X-Active-Role, X-User-Area-Id, X-User-Team-Id)
      → Proxy (Nginx local ou Traefik em dev/prd)
        → CMS WordPress REST API (portaldecomunicacao/v1)
          → Controllers → Services
            → Persistência (MySQL + CPTs/taxonomias + tabelas customizadas)
            → Filesystem (wp-content/uploads/portaldecomunicacao/)
            → Integrações externas:
                • Zimbra (autenticação login)
                • SSE (notificações tempo real)
                • wp_mail / webhook (canais opcionais)
                • BackendSync → Backend PHP legado (rede private)
```

Em ambiente local, o Nginx `server` unifica frontend (`:3000`), CMS (`:80`) e backend (`:8000`) em hosts virtuais distintos. Em dev/prd, Traefik publica frontend (`APP_HOST`) e CMS (`CMS_HOST`) com TLS; backend permanece na rede `private` sem exposição Traefik ativa.

---

## Acoplamentos Identificados

*Somente acoplamentos documentados nos Discovery 01–06.*

- **Frontend ↔ CMS:** dependência total da API `portaldecomunicacao/v1`; dois clientes Axios (`api`, `cmsApi`); 28 endpoints órfãos no frontend.
- **CMS ↔ WordPress:** MU-plugin sobre Core; roles/capabilities nativos; ACF para metadados; plugins SMTP e JWT.
- **CMS ↔ Banco:** MySQL externo via `WORDPRESS_DB_*`; tabelas customizadas criadas pelo MU-plugin.
- **CMS ↔ Zimbra:** autenticação de login via `ZimbraAuth`; variáveis `ZIMBRA_*` em compose.
- **CMS ↔ Backend legado:** `BackendSync` (cURL) e `PORTAL_BACKEND_URL`; Nginx `backend-upstream` no ambiente local.
- **CMS ↔ Filesystem:** documentos em `uploads/portaldecomunicacao/`; bind mount local ou GFS em Swarm.
- **Frontend ↔ localStorage:** token JWT, role ativo (`portal_active_role`), dados de usuário.
- **CI/CD ↔ Registry ↔ Swarm:** imagens versionadas por commit; deploy `docker stack deploy`.

---

## Componentes Legados

| Componente | Impacto |
|---|---|
| Backend PHP (`backend/routes/api.php`) | 20 rotas paralelas ao CMS; `backend/src/` ausente; `BackendSync` mantém acoplamento |
| Container MySQL | Removido dos compose; dependência de banco externo não versionado |
| Server Nginx em Swarm | Buildado no CI mas comentado em dev/prd; proxy local apenas |
| `DocumentsController` | Registrado mas rotas desabilitadas; `DocumentsManager` é fonte ativa |
| `UserFoldersAutoCreationService` | Desabilitado em `Bootstrap.php` |
| Redis cache | Compose isolado sem integração ao stack principal |
| JWT plugin (`jwt-auth-minimal`) | Coexiste com emissor JWT nativo |

*Fonte: `05-current-integrations.md`, `06-current-infrastructure.md`, `01-current-modules.md`.*

---

## Pontos Críticos Arquiteturais

*Evidências já documentadas — sem proposta de solução.*

- Backend legado coexistente com CMS como API principal (`05`, `06`).
- 28 endpoints órfãos no frontend sem `register_rest_route` no CMS (`04`, `05`).
- JWT duplicado: emissor nativo (`JWT.php`) + plugin `jwt-auth-minimal` + referências a `/token/refresh` (`05`).
- Notificações duplicadas: subsistemas `portal_notifications` e `pdc_notifications` em paralelo (`03`, `05`).
- Dois clientes Axios para o mesmo CMS com namespaces diferentes (`05`).
- Registro duplicado de rotas de pastas (`FoldersController` + `FoldersManager`) (`04`).
- Divergência de roles frontend/backend (`team_administrator` vs `team_owner`, `singular_owner`) (`02`).
- Guards frontend permissivos — não aplicam `meta.roles` (`02`).
- Endpoints públicos sensíveis (singulares, teams, debug de documentos) (`04`).
- Onboarding divergente: CMS `options|select|status` vs frontend `current|requests` (`04`, `05`).
- MySQL e arquivos `.env` ausentes do repositório; configs em variáveis CI/CD (`06`).
- Entidades frontend sem persistência (PermissionRequest, Analytics, Comunicados) (`03`).

---

## Resumo das Descobertas

| Documento | Resultado |
|---|---|
| 01 — Módulos | 27 módulos mapeados (18 ATIVO, 8 PARCIAL, 1 LEGADO); 13 controllers, 16 services, 11 stores — **APROVADO COM RESSALVAS** |
| 02 — RBAC | 6 roles canônicos, 47 capabilities; divergências frontend/backend e guards permissivos — **APROVADO COM RESSALVAS** |
| 03 — Dados | 22 entidades (14 confirmadas, 5 virtuais, 3 divergentes); CPT/taxonomia `team` coexistem — **APROVADO COM RESSALVAS** |
| 04 — Endpoints | ~98 rotas CMS, 28 órfãos, 20 legados; registro duplicado de pastas — **APROVADO COM RESSALVAS** |
| 05 — Integrações | 33 integrações categorizadas; 9 órfãs, duplicidades JWT/notificações/backend — **APROVADO COM RESSALVAS** |
| 06 — Infraestrutura | 3 ambientes, 5 containers (+ Redis opcional); MySQL externo, envs ausentes — **APROVADO COM RESSALVAS** |

---

## Resultado da Validação

### Validação 1

Todos os módulos estão representados?

**SIM** — 27 módulos do doc 01 presentes na seção Camada de Apresentação.

### Validação 2

Todas as entidades principais estão representadas?

**SIM** — 13 entidades confirmadas listadas; objetos virtuais referenciados nos pontos críticos.

### Validação 3

Todas as integrações principais estão representadas?

**SIM** — Zimbra, JWT, backend legado, SSE, uploads, webhooks, email e integrações órfãs documentadas.

### Validação 4

Toda infraestrutura identificada está representada?

**SIM** — containers, redes, proxy, CI/CD, storage e componentes legados do doc 06 incluídos.

### Validação 5

Existem lacunas arquiteturais conhecidas?

**SIM** — endpoints órfãos, duplicidades, componentes legados, entidades sem persistência e configs ausentes no repositório.

### Validação 6

O documento é consistente com os documentos 01–06?

**SIM** — consolidação sem contradição; ressalvas herdadas dos documentos fonte.

---

## Status Final

**APROVADO COM RESSALVAS**

Ressalvas: arquitetura operacional centrada no CMS WordPress com acoplamentos legados (backend PHP, JWT duplicado, notificações duplas), divergências frontend/backend documentadas em todos os domínios, e dependências externas críticas (MySQL, Zimbra, GFS) não versionadas no repositório. Consolidação reflete fielmente o estado atual sem propor alterações.
