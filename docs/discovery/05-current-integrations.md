# Discovery — Current Integrations

## Objetivo

Mapear integrações existentes no sistema Portal de Comunicação com base exclusiva em evidências do código-fonte e configurações localizadas.

**Nível de confiança da descoberta:** Alto para integrações com classe/serviço dedicado e uso ativo; Médio para plugins WordPress e canais opcionais; Baixo para referências frontend sem implementação backend correspondente.

**Dependências utilizadas:** módulos (`01`), RBAC (`02`), entidades (`03`), endpoints (`04`). Validações anteriores: APROVADO COM RESSALVAS.

---

## Resumo Executivo

| Categoria | Quantidade |
|---|---|
| Autenticação | 6 |
| APIs internas | 6 |
| APIs / serviços externos corporativos | 2 |
| Email e notificações | 4 |
| Upload e armazenamento | 3 |
| WordPress / plugins | 4 |
| Organizacionais (internas) | 3 |
| Infraestrutura e deploy | 4 |
| Integrações órfãs (frontend) | 9 |
| Dependências técnicas externas | 9 |

---

## Integrações Identificadas

### Integração: Zimbra (IMAP / SMTP / SOAP)

#### Tipo

Autenticação

#### Origem

CMS

#### Finalidade

Autenticar colaboradores via servidores de e-mail corporativos Zimbra por domínio de e-mail, com fallback IMAP/SMTP e SOAP configurável.

#### Evidências

- `ZimbraAuth.php`
- `AuthController.php`
- `compose.my-local.yml` (variáveis `ZIMBRA_*`)

#### Status

ATIVA

---

### Integração: JWT Portal (nativo)

#### Tipo

Autenticação

#### Origem

Frontend + CMS

#### Finalidade

Emitir e validar tokens JWT após login; transporte via header `Authorization: Bearer` nas requisições Axios.

#### Evidências

- `JWT.php`
- `AuthController.php`
- `boot/axios.ts`

#### Status

ATIVA

---

### Integração: JWT Auth WordPress (`jwt-auth/v1`)

#### Tipo

Autenticação

#### Origem

Frontend + CMS (plugin)

#### Finalidade

Validação alternativa de token via namespace WordPress `jwt-auth/v1`; plugin `jwt-auth-minimal` instalado no CMS.

#### Evidências

- `jwt-auth-minimal/jwt-auth-minimal.php`
- `AuthService.ts` (`/jwt-auth/v1/token/validate`)
- `PortalApiService.ts` (comentário fallback)

#### Status

PARCIAL

---

### Integração: WordPress Users

#### Tipo

WordPress

#### Origem

CMS

#### Finalidade

Persistir identidade, roles e metadados organizacionais dos usuários do portal.

#### Evidências

- `UsersService.php`
- `UsersController.php`

#### Status

ATIVA

---

### Integração: Session (JWT + user meta)

#### Tipo

Autenticação

#### Origem

Frontend + CMS

#### Finalidade

Persistir metadados de sessão (token JWT, IP, user agent, última atividade) em user meta WordPress; frontend armazena token em `localStorage`.

#### Evidências

- `Session.php`
- `boot/axios.ts` (`VITE_TOKEN_STORAGE_KEY`, `localStorage`)
- `storage-guards.ts`

#### Status

ATIVA

---

### Integração: WordPress REST API (`wp-json`)

#### Tipo

API REST

#### Origem

Frontend + CMS

#### Finalidade

Expor e consumir APIs HTTP do WordPress; base para namespace `portaldecomunicacao/v1`.

#### Evidências

- `boot/axios.ts` (`WP_REST_ROOT`)
- `EndpointsRegistry.php`

#### Status

ATIVA

---

### Integração: API Portal (`portaldecomunicacao/v1`)

#### Tipo

API REST

#### Origem

Frontend + CMS

#### Finalidade

API principal do portal: autenticação, usuários, documentos, pastas, notificações e demais módulos ATIVOS.

#### Evidências

- `EndpointsRegistry.php`
- `PortalApiService.ts`
- `04-current-endpoints.md`

#### Status

ATIVA

---

### Integração: Onboarding API

#### Tipo

API REST

#### Origem

Frontend + CMS

#### Finalidade

Fluxo de seleção inicial de singular/área para novos usuários via `/onboarding/options`, `/onboarding/select` e `/onboarding/status`.

#### Evidências

- `OnboardingController.php`
- `OnboardingPage.vue`, `onboarding.ts`
- `04-current-endpoints.md`

#### Status

ATIVA

---

### Integração: Global Search (composição)

#### Tipo

API REST

#### Origem

Frontend

#### Finalidade

Busca unificada agregando chamadas paralelas a `/documentos`, `/areas`, `/singulares` e `/users/search` (sem endpoint dedicado).

#### Evidências

- `GlobalSearchService.ts`
- `04-current-endpoints.md`

#### Status

ATIVA

---

### Integração: Singulares / Áreas / Equipes

#### Tipo

API REST

#### Origem

Frontend + CMS

#### Finalidade

Consumo de entidades organizacionais via namespace `portaldecomunicacao/v1`; sem integração externa LDAP/AD/SSO localizada.

#### Evidências

- `SingularesController.php`, `AreasController.php`, `TeamsController.php`
- `singulares.service.ts`, `AreasService.ts`, `TeamsService.ts`

#### Status

ATIVA

---

### Integração: Backend PHP Legado

#### Tipo

API REST

#### Origem

CMS + Backend Legado + Infraestrutura

#### Finalidade

Sincronizar usuários/tokens (`BackendSync`) e expor rotas legadas de auth/documentos/pastas; proxy Nginx dedicado ao serviço backend.

#### Evidências

- `BackendSync.php`
- `backend/routes/api.php`
- `server/nginx.conf` (`backend-upstream`)

#### Status

LEGADA

---

### Integração: BackendSync (CMS → Backend)

#### Tipo

API REST

#### Origem

CMS

#### Finalidade

Sincronizar usuário, validar token e notificar login/logout no backend via cURL quando `PORTAL_BACKEND_URL` e secret configurados.

#### Evidências

- `BackendSync.php` (`/api/auth/sync-user`, `/api/auth/validate-token`)

#### Status

PARCIAL

---

### Integração: MySQL (WordPress)

#### Tipo

Banco Externo

#### Origem

CMS

#### Finalidade

Persistência relacional do WordPress (usuários, posts, termos, tabelas customizadas, transients).

#### Evidências

- `compose.my-local.yml` (`WORDPRESS_DB_*`)
- `AuditService.php`, `NotificationsService.php`

#### Status

ATIVA

---

### Integração: Filesystem de Uploads

#### Tipo

Storage

#### Origem

CMS + Infraestrutura

#### Finalidade

Armazenar binários em `wp-content/uploads/portaldecomunicacao/documentos/` referenciados por metadados de documento.

#### Evidências

- `FoldersService.php`
- `DocumentsManager.php`
- `compose.my-local.yml` (volume uploads)

#### Status

ATIVA

---

### Integração: WordPress Media / Uploads HTTP

#### Tipo

Storage

#### Origem

Infraestrutura

#### Finalidade

Servir arquivos de upload via proxy Nginx com CORS para consumo pelo frontend.

#### Evidências

- `server/nginx.conf` (`/wp-content/uploads/`)

#### Status

ATIVA

---

### Integração: Notificações SSE

#### Tipo

Streaming

#### Origem

Frontend + CMS

#### Finalidade

Push de notificações em tempo real via Server-Sent Events em `/notifications/sse`.

#### Evidências

- `NotificationsController.php`
- `notifications.ts` (`EventSource`)

#### Status

ATIVA

---

### Integração: Notificações em Banco

#### Tipo

Fila

#### Origem

CMS

#### Finalidade

Persistir notificações in-app em tabelas `portal_notifications` e `pdc_notifications` (+ fila `pdc_notifications_queue`).

#### Evidências

- `NotificationsService.php`
- `NotificationChannel.php`

#### Status

ATIVA

---

### Integração: Email (`wp_mail`)

#### Tipo

Email

#### Origem

CMS

#### Finalidade

Enviar notificações por e-mail via `wp_mail` no canal `email` de `NotificationChannel`.

#### Evidências

- `NotificationChannel.php` (`sendEmail`)

#### Status

PARCIAL

---

### Integração: Webhook de Notificações

#### Tipo

API REST

#### Origem

CMS

#### Finalidade

POST JSON para URL configurada em `pdc_webhook_url` (user meta) por usuário destinatário.

#### Evidências

- `NotificationChannel.php` (`sendWebhook`, `wp_remote_post`)

#### Status

PARCIAL

---

### Integração: SMTP WordPress (plugins)

#### Tipo

Email

#### Origem

CMS (plugins)

#### Finalidade

Plugins `easy-wp-smtp` e `wp-mail-smtp` instalados; podem intermediar envio SMTP do WordPress (uso indireto via `wp_mail`).

#### Evidências

- `cms/wp-content/plugins/easy-wp-smtp/`
- `cms/wp-content/plugins/wp-mail-smtp/`

#### Status

PARCIAL

---

### Integração: ACF (Advanced Custom Fields)

#### Tipo

WordPress

#### Origem

CMS

#### Finalidade

Metadados estruturados de singulares, colaboradores, documentos e configurações via JSON em `acf-json/`.

#### Evidências

- `ACFManager.php`
- `cms/wp-content/mu-plugins/acf-json/`

#### Status

ATIVA

---

### Integração: CORS Middleware

#### Tipo

API REST

#### Origem

CMS

#### Finalidade

Controlar origins permitidos para requisições cross-origin entre frontend e CMS.

#### Evidências

- `CorsMiddleware.php`
- `Bootstrap.php`

#### Status

ATIVA

---

### Integração: Axios (clientes HTTP)

#### Tipo

API REST

#### Origem

Frontend

#### Finalidade

Cliente HTTP principal (`api` → namespace portal) e cliente CMS (`cmsApi` → `wp-json` raiz) com interceptors JWT e headers RBAC.

#### Evidências

- `boot/axios.ts`
- `PortalApiService.ts`

#### Status

ATIVA

---

### Integração: Nginx Reverse Proxy

#### Tipo

API REST

#### Origem

Infraestrutura

#### Finalidade

Rotear frontend, CMS WordPress e backend PHP em hosts virtuais distintos na stack local.

#### Evidências

- `server/nginx.conf`

#### Status

ATIVA

---

### Integração: Traefik

#### Tipo

API REST

#### Origem

Infraestrutura

#### Finalidade

Rede pública Docker `traefik-public` para exposição de serviços em ambientes compose (dev/prd).

#### Evidências

- `compose.my-local.yml`
- `compose.dev.yml`, `compose.prd.yml`

#### Status

ATIVA

---

### Integração: GitLab CI / Registry Docker

#### Tipo

API REST

#### Origem

Infraestrutura

#### Finalidade

Build e deploy de imagens `frontend`, `cms` e `backend` via pipeline GitLab e registry corporativo.

#### Evidências

- `.gitlab-ci.yml` (`REGISTRY_UNIMED_*`, `deploy_image`)

#### Status

ATIVA

---

### Integração: Analytics Dashboard

#### Tipo

Analytics

#### Origem

Frontend

#### Finalidade

Consumir métricas administrativas em `/analytics/dashboard` (sem implementação CMS localizada).

#### Evidências

- `analytics.ts`
- `04-current-endpoints.md`

#### Status

ÓRFÃ

---

### Integração: Document Sharing API

#### Tipo

API REST

#### Origem

Frontend

#### Finalidade

Gerenciar regras de compartilhamento via `/document-sharing/*` (sem controller CMS localizado).

#### Evidências

- `DocumentSharingService.ts`
- `04-current-endpoints.md`

#### Status

ÓRFÃ

---

### Integração: Permission Requests API

#### Tipo

API REST

#### Origem

Frontend

#### Finalidade

Fluxo de solicitação/aprovação de acesso em `/permission-requests` (sem controller CMS localizado).

#### Evidências

- `PermissionRequestService.ts`
- `04-current-endpoints.md`

#### Status

ÓRFÃ

---

### Integração: Uploads/Files API

#### Tipo

Storage

#### Origem

Frontend

#### Finalidade

Upload e listagem em `/uploads/*` e `/files/*` (sem `register_rest_route` localizado no CMS).

#### Evidências

- `UploadsService.ts`
- `04-current-endpoints.md`

#### Status

ÓRFÃ

---

### Integração: Colaboradores API (`/colaboradores`)

#### Tipo

API REST

#### Origem

Frontend

#### Finalidade

CRUD de colaboradores em namespace dedicado; CMS expõe sub-recursos em `/areas/{id}/colaboradores`.

#### Evidências

- `CollaboratorsService.ts`
- `PortalApiService.ts`

#### Status

ÓRFÃ

---

### Integração: Auth Email Gate (backend-style)

#### Tipo

Autenticação

#### Origem

Frontend

#### Finalidade

`loginWithEmailGate` posta em `/auth/login` esperando `gate_info` (IMAP/SMTP); método não referenciado em outros arquivos frontend.

#### Evidências

- `AuthService.ts`

#### Status

PARCIAL

---

### Integração: Zimbra Email Validation

#### Tipo

Autenticação

#### Origem

Frontend

#### Finalidade

Validar domínio/e-mail corporativo via `POST /validate/zimbra-email` antes do login.

#### Evidências

- `PortalApiService.ts` (`validateZimbraEmail`)

#### Status

ÓRFÃ

---

### Integração: Organizations API

#### Tipo

API REST

#### Origem

Frontend

#### Finalidade

Listar organizações em `GET /organizations` para formulários administrativos.

#### Evidências

- `PortalApiService.ts` (`getOrganizations`)

#### Status

ÓRFÃ

---

## APIs Internas

| Integração | Namespace / Rota | Status |
|---|---|---|
| Portal REST | `portaldecomunicacao/v1` | ATIVA |
| WordPress REST | `wp-json` | ATIVA |
| JWT plugin WP | `jwt-auth/v1` | PARCIAL |
| Onboarding | `/onboarding/options\|select\|status` | ATIVA |
| Organizacionais | `/singulares`, `/areas`, `/teams` | ATIVA |
| Backend legado | rotas em `backend/routes/api.php` | LEGADA |
| Nginx API host | proxy `backend-upstream` | LEGADA |

---

## APIs Externas

| Sistema | Tipo | Evidência | Status |
|---|---|---|---|
| Zimbra (mail corporativo) | Autenticação IMAP/SMTP/SOAP | `ZimbraAuth.php`, `ZIMBRA_*` env | ATIVA |
| Webhook usuário (`pdc_webhook_url`) | HTTP POST outbound | `NotificationChannel.php` | PARCIAL |
| Registry Docker GitLab | Deploy | `.gitlab-ci.yml` | ATIVA |
| LDAP / AD / SSO | — | Não localizado | — |

---

## Upload e Armazenamento

| Integração | Evidência | Status |
|---|---|---|
| Filesystem `uploads/portaldecomunicacao/` | `FoldersService.php`, volume Docker | ATIVA |
| Proxy HTTP uploads | `server/nginx.conf` | ATIVA |
| `POST /documentos` (upload lógico) | `DocumentsManager.php` | ATIVA |
| `/uploads/*`, `/files/*` (API dedicada) | `UploadsService.ts` | ÓRFÃ |

---

## Notificações

| Integração | Evidência | Status |
|---|---|---|
| SSE `/notifications/sse` | `NotificationsController.php`, `notifications.ts` | ATIVA |
| Banco `portal_notifications` | `NotificationsService.php` | ATIVA |
| Banco `pdc_notifications` + fila | `NotificationChannel.php` | ATIVA |
| Email `wp_mail` | `NotificationChannel.php` | PARCIAL |
| Webhook outbound | `NotificationChannel.php` | PARCIAL |

---

## Analytics

| Integração | Evidência | Status |
|---|---|---|
| `/analytics/dashboard` | `analytics.ts` | ÓRFÃ |
| `/admin/metrics` | `PortalApiService.ts` | ÓRFÃ |
| Performance monitor (comentado) | `performance-monitor.ts` | PARCIAL |

---

## Dependências Externas

| Dependência | Tipo | Uso |
|---|---|---|
| Axios | Cliente HTTP | Requisições frontend → CMS/backend |
| JWT (HS256) | Autenticação | Tokens portal (`JWT.php`) |
| WordPress REST | Framework API | Base `wp-json` |
| SSE (`EventSource`) | Streaming | Notificações tempo real |
| PHP cURL / `wp_remote_*` | HTTP client | Zimbra, BackendSync, webhooks |
| PHP IMAP (extensão) | Autenticação | Zimbra IMAP em `ZimbraAuth.php` |
| MySQL | Banco | Persistência WordPress |
| Firebase PHP-JWT (vendor backend) | Biblioteca | Dependência em `backend/vendor` |
| Fetch API (browser) | Cliente HTTP | Download de arquivos e logout (`fetch`) |

---

## Integrações Órfãs

Referenciadas no frontend ou em `04-current-endpoints.md` sem implementação localizada no CMS:

- `/analytics/dashboard`, `/admin/metrics`
- `/document-sharing/*`
- `/permission-requests`
- `/uploads/*`, `/files/*`
- `/colaboradores` (namespace raiz)
- `/onboarding/current`, `/onboarding/requests` (diverge de `/onboarding/options|select|status`)
- `/auditoria/singulares` (CMS expõe `/rbac/audit`)
- `/configuracoes` (CMS expõe `/configuracoes-publicas`)
- `/portaldecomunicacao/organizations`
- `/validate/zimbra-email`

---

## Divergências Encontradas

- **Dois clientes Axios** (`api` e `cmsApi`) para o mesmo CMS com namespaces diferentes (`portaldecomunicacao/v1` vs `wp-json` raiz).
- **JWT duplicado:** emissor nativo (`JWT.php`) + plugin `jwt-auth-minimal` + referências a `/user/me` e `/token/refresh` em `AuthService.ts`.
- **Backend legado:** `BackendSync` e Nginx `backend-upstream` coexistem com CMS como API principal; `backend/src` ausente na validação de módulos.
- **Notificações duplicadas:** subsistemas `portal_notifications` e `pdc_notifications` em paralelo.
- **Login paths:** fluxo ativo via `PortalApiService.login` → CMS; `loginWithEmailGate` não referenciado e espera payload diferente.
- **Colaboradores:** frontend aponta `/colaboradores`; CMS usa `/areas/{id}/colaboradores` e `/singulares/{id}/colaboradores`.
- **Variáveis de ambiente:** múltiplos nomes para CMS (`VITE_CMS_BASE_URL`, `VITE_API_BASE`, `VITE_PORTAL_API_BASE`) em `env.d.ts` e `boot/axios.ts`.
- **Onboarding divergente:** CMS expõe `options|select|status`; frontend `onboarding.ts` consome `current` e `requests` inexistentes.
- **Zimbra validate:** `PortalApiService.validateZimbraEmail` posta em `/validate/zimbra-email` sem `register_rest_route` no CMS.
- **Logout fetch:** `reliableLogoutService` usa `fetch` com chave `auth_token` divergente de `VITE_TOKEN_STORAGE_KEY` (`portal_auth_token`).

---

## Cobertura da Descoberta

### Integrações Cobertas

| Tipo | Quantidade | Observação |
|---|---|---|
| ATIVA | 22 | Uso evidenciado em runtime |
| PARCIAL | 9 | Plugin alternativo, canal opcional ou código não referenciado |
| LEGADA | 2 | Backend PHP e rotas `api.php` |
| ÓRFÃ | 9 | Frontend sem backend (lista em seção dedicada) |

### APIs Cobertas

| Camada | Cobertura |
|---|---|
| CMS `portaldecomunicacao/v1` | Completa (via doc 04) |
| WordPress / plugins | jwt-auth, ACF, SMTP plugins |
| Externas corporativas | Zimbra |
| Backend legado | Rotas catalogadas; implementação `src` ausente |

### Dependências Cobertas

| Categoria | Cobertura |
|---|---|
| Clientes HTTP | Axios, Fetch, cURL, wp_remote |
| Auth | JWT, Zimbra, WP Users |
| Persistência | MySQL, filesystem |
| Streaming | SSE |
| Deploy | Docker, Traefik, GitLab |

---

## Resultado da Validação

### Validação 1

Todas as integrações possuem evidência?

**SIM** — integrações listadas possuem arquivo/classe/config referenciada; LDAP/AD não listado por ausência de evidência.

### Validação 2

Existem integrações órfãs?

**SIM** — analytics, document-sharing, permission-requests, uploads/files, colaboradores, onboarding divergente, organizations, zimbra-email validate, admin/metrics.

### Validação 3

Existem integrações duplicadas?

**SIM** — JWT (nativo + plugin), notificações (dois subsistemas), clientes HTTP duplos, backend legado + CMS.

### Validação 4

Existem integrações legadas?

**SIM** — backend PHP (`backend/routes/api.php`, `BackendSync`, Nginx `backend-upstream`).

### Validação 5

Existem URLs externas não documentadas?

**NÃO** — hosts Zimbra e registry referenciados sem expor valores completos; nenhuma integração externa adicional com evidência além das mapeadas.

### Validação 6

Existem dependências externas críticas?

**SIM** — Zimbra (login produção), MySQL (persistência), CMS REST (API principal), filesystem uploads (documentos).

---

## Status Final

**APROVADO COM RESSALVAS**

Ressalvas: integrações órfãs no frontend, duplicidade JWT/notificações/backend legado, canais email/webhook parcialmente implementados e ausência de evidência para LDAP/AD/SSO. Não bloqueia continuidade da Discovery conforme validações anteriores.
