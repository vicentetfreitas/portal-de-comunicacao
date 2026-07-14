# Discovery — Current Endpoints

## Objetivo

Mapear os endpoints atuais do sistema Portal de Comunicação com base exclusiva em evidências do código-fonte (CMS WordPress REST, frontend Quasar e backend PHP legado).

**Namespace principal:** `portaldecomunicacao/v1` (prefixo completo: `/wp-json/portaldecomunicacao/v1`).

**Nível de confiança da descoberta:** Alto para rotas com `register_rest_route` localizado; Médio para rotas com registro duplicado (`FoldersManager` + `FoldersController`); Baixo para consumo frontend sem `register_rest_route` correspondente.

**Dependências utilizadas:** módulos (`01-current-modules.md`), RBAC (`02-current-rbac.md`), entidades (`03-current-data-model.md`). Validações anteriores: APROVADO COM RESSALVAS.

---

## Resumo Executivo

| Categoria | Quantidade |
|---|---|
| Endpoints CMS (`portaldecomunicacao/v1`) | ~98 registros de rota |
| Endpoints públicos (`__return_true` ou equivalente) | 18 |
| Endpoints protegidos (JWT / Role / Capability / ACL) | ~80 |
| Endpoints órfãos (frontend sem backend localizado) | 28 |
| Endpoints legados (`backend/routes/api.php`) | 20 |
| Controllers CMS com rotas ativas | 12 (+ `EndpointsRegistry`, `DocumentsManager`, `FoldersManager`) |
| Controllers CMS sem rotas ativas | 1 (`DocumentsController` — `registerRoutes` retorna cedo) |

---

## Endpoints Identificados

> URI relativa ao namespace `portaldecomunicacao/v1`. Entidades conforme `03-current-data-model.md`.

### Módulo: Autenticação e Sessão

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| POST | `/auth/login` | AuthController | Público | ATIVO |
| GET | `/auth/me` | AuthController | JWT | ATIVO |
| OPTIONS | `/auth/me` | AuthController | Público | ATIVO |
| POST | `/auth/logout` | AuthController | Público | ATIVO |
| GET | `/auth/context` | AuthController | JWT | ATIVO |
| POST | `/auth/refresh` | — | — | ÓRFÃO |
| GET | `/auth/validate` | — | — | ÓRFÃO |

**Consumo frontend:** `AuthService`, `PortalApiService`, `auth-core` store, `NavigationContextService`.

**Namespaces externos consumidos:** `jwt-auth/v1/token/validate`, `/user/me`, `/logout`, `/token/refresh` (`AuthService.ts` — fora de `portaldecomunicacao/v1`).

---

### Módulo: Usuários

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/users` | UsersController | Capability | ATIVO |
| POST | `/users` | UsersController | Role | ATIVO |
| GET | `/users/{id}` | UsersController | JWT / ACL | ATIVO |
| GET | `/users/by-username/{username}` | UsersController | JWT / ACL | ATIVO |
| PUT/PATCH | `/users/{id}` | UsersController | Capability | ATIVO |
| DELETE | `/users/{id}` | UsersController | Role | ATIVO |
| POST | `/users/provision` | UsersController | Role | ATIVO |
| GET | `/users/search` | UsersController | JWT | ATIVO |
| POST | `/users/{id}/grant-role` | UsersController | Role | ATIVO |
| POST | `/users/{id}/revoke-role` | UsersController | Role | ATIVO |
| POST | `/users/{id}/bind` | UsersController | Role | ATIVO |
| GET | `/users/{id}/folder-stats` | UsersController | JWT / ACL | ATIVO |
| POST | `/users/{id}/move-directories` | UsersController | Role | ATIVO |
| GET | `/users/profile` | UsersController | JWT | ATIVO |
| PUT/PATCH | `/users/profile` | UsersController | JWT | ATIVO |
| GET | `/users/provisioned-recently` | — | — | ÓRFÃO |
| PUT | `/users/{id}/status` | — | — | ÓRFÃO |

**Entidades:** Usuário, Colaborador, Papel RBAC, Pasta.

---

### Módulo: RBAC

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/rbac/audit` | RBACController | Role | ATIVO |
| POST | `/rbac/cleanup` | RBACController | Role | ATIVO |
| POST | `/rbac/reset-canonical` | RBACController | Role | ATIVO |
| GET | `/rbac/can-grant` | — | — | ÓRFÃO |
| POST | `/rbac/grant-role` | — | — | ÓRFÃO |
| POST | `/rbac/revoke-role` | — | — | ÓRFÃO |
| GET | `/rbac/hierarchy` | — | — | ÓRFÃO |
| GET | `/permissions` | — | — | ÓRFÃO |
| GET | `/permissions/check/{permission}` | — | — | ÓRFÃO |

**Entidades:** Papel RBAC, Registro de Auditoria.

---

### Módulo: Singulares

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/singulares` | SingularesController | Público | ATIVO |
| GET | `/singulares/stats` | SingularesController | JWT | PARCIAL |
| GET | `/singulares/{id}` | SingularesController | Público | ATIVO |
| POST | `/singulares` | SingularesController | JWT | ATIVO |
| PUT/PATCH | `/singulares/{id}` | SingularesController | JWT | ATIVO |
| DELETE | `/singulares/{id}` | SingularesController | JWT | ATIVO |
| GET | `/singulares/{id}/areas` | SingularesController | Público | ATIVO |
| POST | `/singulares/{id}/areas/{area_id}` | SingularesController | Role | ATIVO |
| DELETE | `/singulares/{id}/areas/{area_id}` | SingularesController | JWT | ATIVO |
| GET | `/singulares/{id}/colaboradores` | SingularesController | JWT | ATIVO |
| GET | `/singulares/check-duplicate` | — | — | ÓRFÃO |
| GET | `/singulares/{id}/permissions` | — | — | ÓRFÃO |
| POST | `/singulares/{id}/permissions` | — | — | ÓRFÃO |
| DELETE | `/singulares/{id}/permissions/{userId}` | — | — | ÓRFÃO |
| GET | `/singulares/list` | — | — | ÓRFÃO |

**Entidades:** Singular, Área, Colaborador.

---

### Módulo: Áreas

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/areas` | AreasController | JWT | ATIVO |
| GET | `/areas/hierarchy` | AreasController | JWT | ATIVO |
| GET | `/areas/stats` | AreasController | JWT | ATIVO |
| GET | `/areas/{id}` | AreasController | JWT | ATIVO |
| POST | `/areas` | AreasController | JWT | ATIVO |
| PUT/PATCH | `/areas/{id}` | AreasController | JWT | ATIVO |
| DELETE | `/areas/{id}` | AreasController | JWT | ATIVO |
| GET | `/areas/{id}/equipes` | AreasController | JWT | PARCIAL |
| GET | `/areas/{id}/colaboradores` | AreasController | JWT | ATIVO |
| POST | `/areas/{id}/colaboradores` | AreasController | JWT | ATIVO |
| DELETE | `/areas/{id}/colaboradores/{user_id}` | AreasController | JWT | ATIVO |
| GET | `/areas/{id}/users` | — | — | ÓRFÃO |
| POST | `/areas/{id}/teams` | TeamsController | JWT | ATIVO |

**Entidades:** Área, Equipe, Colaborador.

---

### Módulo: Equipes

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/teams` | TeamsController | Público | ATIVO |
| GET | `/teams/stats` | TeamsController | JWT | ATIVO |
| GET | `/teams/{id}` | TeamsController | Público | ATIVO |
| GET | `/teams/{slug}` | TeamsController | Público | ATIVO |
| PUT/PATCH | `/teams/{slug}` | TeamsController | JWT | ATIVO |
| POST | `/teams` | TeamsController | JWT | ATIVO |
| PUT/PATCH | `/teams/{id}` | TeamsController | JWT | ATIVO |
| DELETE | `/teams/{id}` | TeamsController | JWT | ATIVO |
| GET | `/areas/{area_id}/teams` | TeamsController | Público | ATIVO |
| POST | `/teams/{id}/members` | TeamsController | JWT | ATIVO |
| DELETE | `/teams/{id}/members/{user_id}` | TeamsController | JWT | ATIVO |

**Entidades:** Equipe, Área, Colaborador.

---

### Módulo: Colaboradores

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/areas/{id}/colaboradores` | AreasController | JWT | ATIVO |
| GET | `/singulares/{id}/colaboradores` | SingularesController | JWT | ATIVO |
| GET | `/colaboradores` | — | — | ÓRFÃO |
| GET | `/colaboradores/{id}` | — | — | ÓRFÃO |
| GET | `/colaboradores/email/{email}` | — | — | ÓRFÃO |
| GET | `/colaboradores/stats` | — | — | ÓRFÃO |

**Nota:** colaboradores expostos via sub-recursos de área/singular e `/users`; namespace `/colaboradores` referenciado em `CollaboratorsService.ts` sem controller CMS.

---

### Módulo: Documentos

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/documentos` | DocumentsManager | ACL | ATIVO |
| POST | `/documentos` | DocumentsManager | ACL | ATIVO |
| GET | `/documentos/{id}` | DocumentsManager | ACL | ATIVO |
| PUT | `/documentos/{id}` | DocumentsManager | ACL | ATIVO |
| PATCH | `/documentos/{id}` | DocumentsManager | ACL | ATIVO |
| DELETE | `/documentos/{id}` | DocumentsManager | ACL | ATIVO |
| GET | `/documentos/{id}/download` | DocumentsManager | ACL | ATIVO |
| POST | `/documentos/{id}/download` | DocumentsManager | ACL | PARCIAL |
| GET | `/documentos/stats` | DocumentsManager | Capability | ATIVO |
| GET | `/documentos-debug` | DocumentsManager | Público | PARCIAL |
| POST | `/documentos-fix-paths` | DocumentsManager | Público | PARCIAL |
| GET | `/documentos-debug-folders` | DocumentsManager | Público | PARCIAL |
| POST | `/documentos/{id}/share` | DocumentsController | — | PARCIAL |
| GET | `/documentos/{id}/permissions` | DocumentsController | — | PARCIAL |

**Entidades:** Documento, Arquivo Físico, Compartilhamento de Documento, Pasta.

**Nota:** `DocumentsController::registerRoutes()` desabilitado; rotas ativas em `DocumentsManager`. Endpoints `share`/`permissions` existem apenas comentados no controller.

---

### Módulo: Pastas e Diretórios

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/pastas` | FoldersController / FoldersManager | ACL | ATIVO |
| POST | `/pastas` | FoldersController / FoldersManager | ACL | ATIVO |
| GET | `/pastas/tree` | FoldersController / FoldersManager | ACL | ATIVO |
| GET | `/pastas/{id}` | FoldersController / FoldersManager | ACL | ATIVO |
| PUT/PATCH | `/pastas/{id}` | FoldersController / FoldersManager | ACL | ATIVO |
| DELETE | `/pastas/{id}` | FoldersController / FoldersManager | ACL | ATIVO |
| GET | `/pastas/permissions-debug` | FoldersController | Role | PARCIAL |
| GET | `/pastas/area-users` | FoldersController | ACL | PARCIAL |
| POST | `/pastas/fix-slugs` | FoldersController | Role | PARCIAL |
| PUT | `/pastas/{id}/users` | FoldersController | ACL | ATIVO |
| DELETE | `/pastas/{id}/users/{user_id}` | FoldersController | ACL | ATIVO |
| GET | `/folders` | FoldersController / FoldersManager | ACL | ATIVO |
| POST | `/folders` | FoldersController / FoldersManager | ACL | ATIVO |
| GET | `/folders/{id}` | FoldersController / FoldersManager | ACL | ATIVO |
| PUT/PATCH | `/folders/{id}` | FoldersController / FoldersManager | ACL | ATIVO |
| DELETE | `/folders/{id}` | FoldersController / FoldersManager | ACL | ATIVO |

**Entidades:** Pasta, Usuário, Documento.

---

### Módulo: Permissões de Pastas

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/pastas/permissions-debug` | FoldersController | Role | PARCIAL |
| GET | `/pastas/area-users` | FoldersController | ACL | PARCIAL |

**Regra ACL:** `FoldersPermissionsService::canCreateFolder`, `canAccess` (evidência em `FoldersController`).

---

### Módulo: Solicitação de Permissões

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/permission-requests` | — | — | ÓRFÃO |
| POST | `/permission-requests` | — | — | ÓRFÃO |
| PUT | `/permission-requests/{id}` | — | — | ÓRFÃO |

**Consumo:** `PermissionRequestService.ts`.

---

### Módulo: Notificações

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/notifications` | NotificationsController | JWT | ATIVO |
| POST | `/notifications` | NotificationsController | Role | ATIVO |
| POST | `/notifications/{id}/read` | NotificationsController | JWT | ATIVO |
| POST | `/notifications/mark-all-read` | NotificationsController | JWT | ATIVO |
| GET | `/notifications/sse` | NotificationsController | JWT | ATIVO |
| GET | `/notifications/stats` | NotificationsController | JWT | ATIVO |
| GET | `/notifications/settings` | NotificationsController | JWT | ATIVO |
| PUT | `/notifications/settings` | NotificationsController | JWT | ATIVO |

**Entidades:** Notificação (Portal).

---

### Módulo: Onboarding

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/onboarding/options` | OnboardingController | JWT | ATIVO |
| POST | `/onboarding/select` | OnboardingController | JWT | ATIVO |
| GET | `/onboarding/status` | OnboardingController | JWT | ATIVO |
| GET | `/onboarding/current` | — | — | ÓRFÃO |
| POST | `/onboarding/requests` | — | — | ÓRFÃO |
| GET | `/onboarding/requests` | — | — | ÓRFÃO |
| POST | `/onboarding/requests/{id}/{action}` | — | — | ÓRFÃO |

**Entidades:** Usuário, Singular, Área, Solicitação de Onboarding (frontend).

---

### Módulo: Auditoria

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/rbac/audit` | RBACController | Role | ATIVO |
| GET | `/auditoria/singulares` | — | — | ÓRFÃO |

**Entidades:** Registro de Auditoria, Papel RBAC.

---

### Módulo: Armazenamento e Upload

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| POST | `/uploads/public` | — | — | ÓRFÃO |
| POST | `/uploads/private` | — | — | ÓRFÃO |
| GET | `/files/public` | — | — | ÓRFÃO |
| GET | `/files/private` | — | — | ÓRFÃO |
| GET | `/files/{id}` | — | — | ÓRFÃO |
| DELETE | `/files/{id}` | — | — | ÓRFÃO |

**Consumo:** `UploadsService.ts`. Upload de documentos também via `POST /documentos` (DocumentsManager).

---

### Módulo: Configuração do Portal

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/configuracoes-publicas` | EndpointsRegistry | Público | ATIVO |
| GET | `/configuracoes` | — | — | ÓRFÃO |

**Entidades:** Configurações do Portal.

---

### Módulo: API — Saúde e Utilitários

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/ping` | EndpointsRegistry | Público | ATIVO |
| GET | `/status` | EndpointsRegistry | Público | ATIVO |
| GET | `/version` | EndpointsRegistry | Público | ATIVO |
| GET | `/configuracoes-publicas` | EndpointsRegistry | Público | ATIVO |

---

### Módulo: Diagnóstico

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/diagnostics/upload` | DiagnosticsController | Capability | PARCIAL |
| POST | `/diagnostics/fix` | DiagnosticsController | Capability | PARCIAL |

---

### Módulo: Busca Global

Sem endpoints dedicados. Consome `GET /documentos`, `GET /areas`, `GET /singulares`, `GET /users/search` via `GlobalSearchService.ts`.

---

### Módulo: Analytics

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| GET | `/analytics/dashboard` | — | — | ÓRFÃO |
| GET | `/admin/metrics` | — | — | ÓRFÃO |

**Consumo:** `analytics.ts` store, `PortalApiService`.

---

### Módulo: Comunicados

Sem `register_rest_route` localizado no CMS.

---

### Módulo: Fique por Dentro

Sem `register_rest_route` localizado no CMS.

---

### Módulo: Convidados

Sem endpoints dedicados. Usa rotas de Usuários com role `visitor`.

---

### Módulo: Central de Colaboração

Sem `register_rest_route` localizado no CMS.

---

### Módulo: Navegação e Interface

Sem endpoints dedicados. Consome `GET /auth/context` e entidades organizacionais.

---

### Módulo: Cache

Sem endpoints REST. Opera via WordPress transients (interno).

---

### Módulo: Compartilhamento de Documentos (transversal)

| Método | Endpoint | Controller | Segurança | Status |
|---|---|---|---|---|
| POST | `/document-sharing/set` | — | — | ÓRFÃO |
| GET | `/document-sharing/{resource}` | — | — | ÓRFÃO |
| POST | `/document-sharing/validate` | — | — | ÓRFÃO |
| DELETE | `/document-sharing/{resource}` | — | — | ÓRFÃO |

**Consumo:** `DocumentSharingService.ts`. Metadado `portal_doc_sharing` existe no CMS; API REST dedicada não localizada.

---

### Módulo: Backend PHP Legado

Origem: `backend/routes/api.php`. Prefixo base não definido no arquivo (rotas relativas).

| Método | Endpoint | Controller legado | Segurança | Status |
|---|---|---|---|---|
| POST | `/auth/login` | AuthController | Público | LEGADO |
| POST | `/auth/validate` | AuthController | JWT | LEGADO |
| GET | `/auth/health` | AuthController | Público | LEGADO |
| GET/POST | `/documents`, `/documentos` | DocumentController | JWT | LEGADO |
| GET/PUT/DELETE | `/documents/{id}`, `/documentos/{id}` | DocumentController | JWT | LEGADO |
| GET | `/documents/{id}/download`, `/documentos/{id}/download` | DocumentController | JWT | LEGADO |
| GET | `/documents/{id}/permissions`, `/documentos/{id}/permissions` | DocumentController | JWT | LEGADO |
| GET/POST/PUT/DELETE | `/folders`, `/folders/tree`, `/folders/{id}` | FolderController | JWT | LEGADO |
| POST | `/folders/{id}/move` | FolderController | JWT | LEGADO |
| GET | `/folders/{id}/permissions` | FolderController | JWT | LEGADO |

**Nota:** `backend/src` ausente na validação de módulos.

---

## Mapeamento Controller → Endpoint

| Controller | Endpoint(s) |
|---|---|
| EndpointsRegistry | `/ping`, `/status`, `/version`, `/configuracoes-publicas` |
| AuthController | `/auth/login`, `/auth/me`, `/auth/logout`, `/auth/context` |
| UsersController | `/users`, `/users/{id}`, `/users/by-username/{username}`, `/users/provision`, `/users/search`, `/users/{id}/grant-role`, `/users/{id}/revoke-role`, `/users/{id}/bind`, `/users/{id}/folder-stats`, `/users/{id}/move-directories`, `/users/profile` |
| RBACController | `/rbac/audit`, `/rbac/cleanup`, `/rbac/reset-canonical` |
| SingularesController | `/singulares`, `/singulares/stats`, `/singulares/{id}`, `/singulares/{id}/areas`, `/singulares/{id}/colaboradores` |
| AreasController | `/areas`, `/areas/hierarchy`, `/areas/stats`, `/areas/{id}`, `/areas/{id}/equipes`, `/areas/{id}/colaboradores` |
| TeamsController | `/teams`, `/teams/stats`, `/teams/{id}`, `/teams/{slug}`, `/areas/{area_id}/teams`, `/teams/{id}/members` |
| DocumentsManager | `/documentos`, `/documentos/{id}`, `/documentos/{id}/download`, `/documentos/stats`, `/documentos-debug`, `/documentos-fix-paths`, `/documentos-debug-folders` |
| DocumentsController | *(desabilitado — rotas comentadas)* |
| FoldersController | `/pastas`, `/pastas/tree`, `/pastas/{id}`, `/pastas/{id}/users`, `/pastas/permissions-debug`, `/pastas/area-users`, `/pastas/fix-slugs`, `/folders`, `/folders/{id}` |
| FoldersManager | `/pastas`, `/pastas/tree`, `/pastas/{id}`, `/folders`, `/folders/{id}` *(duplicado)* |
| NotificationsController | `/notifications`, `/notifications/{id}/read`, `/notifications/mark-all-read`, `/notifications/sse`, `/notifications/stats`, `/notifications/settings` |
| OnboardingController | `/onboarding/options`, `/onboarding/select`, `/onboarding/status` |
| DiagnosticsController | `/diagnostics/upload`, `/diagnostics/fix` |

---

## Mapeamento Endpoint → Entidade

| Endpoint | Entidade |
|---|---|
| `/auth/*` | Usuário, Papel RBAC |
| `/users/*` | Usuário, Colaborador, Papel RBAC, Pasta |
| `/rbac/*` | Papel RBAC, Registro de Auditoria |
| `/singulares/*` | Singular, Área, Colaborador |
| `/areas/*` | Área, Equipe, Colaborador |
| `/teams/*` | Equipe, Colaborador, Área |
| `/documentos/*` | Documento, Arquivo Físico, Pasta |
| `/pastas/*`, `/folders/*` | Pasta, Usuário, Documento |
| `/notifications/*` | Notificação (Portal) |
| `/onboarding/*` | Usuário, Singular, Área |
| `/diagnostics/*` | Arquivo Físico, Pasta |
| `/ping`, `/status`, `/version` | — |
| `/configuracoes-publicas` | Configurações do Portal |

---

## Segurança dos Endpoints

### Públicos

| Endpoint | Justificativa |
|---|---|
| `/ping`, `/status`, `/version`, `/configuracoes-publicas` | `permission_callback => '__return_true'` em `EndpointsRegistry` |
| `/auth/login` | `__return_true` — autenticação inicial |
| `/auth/logout` | `__return_true` |
| `/auth/me` (OPTIONS) | `__return_true` — preflight CORS |
| `/singulares`, `/singulares/{id}`, `/singulares/{id}/areas` | `__return_true` em `SingularesController` |
| `/teams`, `/teams/{id}`, `/teams/{slug}`, `/areas/{area_id}/teams` | `__return_true` em `TeamsController` |
| `/documentos-debug`, `/documentos-fix-paths`, `/documentos-debug-folders` | `__return_true` em `DocumentsManager` |
| `/documentos/{id}/download` (OPTIONS) | `__return_true` |

### JWT

| Endpoint | Evidência |
|---|---|
| `/auth/me`, `/auth/context` | `verifyJwtToken` em `AuthController` |
| `/users/profile`, `/areas/*`, `/onboarding/*` | `checkJwtAuth` em controllers |
| `/teams` (mutações), `/notifications/*` | `checkJwtAuth` / `checkLoggedIn` |
| `/documentos/*` (maioria) | `checkJwtAuth` + ACL em `DocumentsManager` |

### Role / Capability

| Endpoint | Role | Capability |
|---|---|---|
| `/rbac/*` | `administrator` | `portal_rbac_manage` |
| `/users` (POST/DELETE/bind) | `administrator` | `checkAdminPermission` |
| `/notifications` (POST) | admin | `checkAdminPermission` |
| `/singulares/{id}/areas` (POST) | admin | `checkAdminPermission` |
| `/diagnostics/*` | — | `manage_options` |
| `/users` (GET list) | — | `checkListUsersPermission` (capabilities `portal_*`) |

### ACL

| Endpoint | Regra |
|---|---|
| `/documentos` | `DocumentsManager::checkReadPermission`, `checkDocumentReadPermission`, `checkCreatePermission` |
| `/pastas`, `/folders` | `FoldersController::checkViewPermission`, `checkManagePermission`; `FoldersPermissionsService` |
| `/pastas/{id}/users` | ACL de vínculo usuário-pasta |

---

## Endpoints Consumidos pelo Frontend

| Endpoint | Serviço / Store | Status |
|---|---|---|
| `/auth/login` | AuthService, PortalApiService | ATIVO |
| `/auth/me`, `/auth/context` | PortalApiService, NavigationContextService | ATIVO |
| `/auth/refresh` | auth-core, PortalApiService | ÓRFÃO |
| `/users`, `/users/{id}`, `/users/search` | users.service, user store | ATIVO |
| `/users/provision` | PortalApiService | ATIVO |
| `/singulares` | singulares.service | ATIVO |
| `/areas` | AreasService, composables | ATIVO |
| `/teams` | TeamsService, FoldersService | ATIVO |
| `/documentos` | PortalApiService, composables | ATIVO |
| `/pastas`, `/pastas/tree` | FoldersService, PortalApiService | ATIVO |
| `/notifications` | notifications store | ATIVO |
| `/onboarding/options`, `/onboarding/select` | OnboardingPage | ATIVO |
| `/onboarding/current`, `/onboarding/requests` | onboarding store | ÓRFÃO |
| `/permission-requests` | PermissionRequestService | ÓRFÃO |
| `/document-sharing/*` | DocumentSharingService | ÓRFÃO |
| `/analytics/dashboard` | analytics store | ÓRFÃO |
| `/configuracoes-publicas` | config store, boot/axios | ATIVO |
| `/status`, `/ping` | boot/axios, PortalApiService | ATIVO |
| `/colaboradores` | CollaboratorsService, PortalApiService | ÓRFÃO |
| `/auditoria/singulares` | singulares.service, OrganizationAudit | ÓRFÃO |
| `/uploads/*`, `/files/*` | UploadsService | ÓRFÃO |
| `/rbac/can-grant`, `/rbac/grant-role` | PortalApiService | ÓRFÃO |

---

## Endpoints Órfãos

Frontend consome; backend `portaldecomunicacao/v1` não localizado:

- `/auth/refresh`, `/auth/validate`
- `/permissions`, `/permissions/check/{permission}`
- `/rbac/can-grant`, `/rbac/grant-role`, `/rbac/revoke-role`, `/rbac/hierarchy`
- `/colaboradores`, `/colaboradores/{id}`, `/colaboradores/email/{email}`, `/colaboradores/stats`
- `/permission-requests`
- `/document-sharing/*`
- `/analytics/dashboard`, `/admin/metrics`
- `/onboarding/current`, `/onboarding/requests`, `/onboarding/requests/{id}/{action}`
- `/auditoria/singulares`
- `/singulares/check-duplicate`, `/singulares/{id}/permissions`, `/singulares/list`
- `/areas/{id}/users`
- `/users/provisioned-recently`, `/users/{id}/status`
- `/configuracoes`
- `/uploads/public`, `/uploads/private`, `/files/public`, `/files/private`, `/files/{id}`
- `/docs/list`, `/docs/config`, `/docs/timeline`
- `/portaldecomunicacao/organizations` (`PortalApiService.getOrganizations`)

---

## Endpoints Públicos Sensíveis

Endpoints com `__return_true` ou acesso sem autenticação em dados organizacionais:

| Endpoint | Risco evidenciado |
|---|---|
| `/documentos-debug` | Lista documentos sem auth |
| `/documentos-fix-paths` | Migração POST sem auth |
| `/documentos-debug-folders` | Debug pastas/documentos sem auth |
| `/teams`, `/teams/{id}`, `/teams/{slug}` | Dados de equipes sem JWT |
| `/singulares`, `/singulares/{id}` | Dados organizacionais sem JWT |
| `/areas/{area_id}/teams` | Times por área sem JWT |
| `/auth/logout` | POST público |
| `/status` | Expõe lista de controllers registrados |

---

## Endpoints Legados

| Endpoint | Origem |
|---|---|
| `POST /auth/login` | `backend/routes/api.php` |
| `POST /auth/validate` | `backend/routes/api.php` |
| `GET /auth/health` | `backend/routes/api.php` |
| `GET/POST/PUT/DELETE /documents`, `/documentos` | `backend/routes/api.php` |
| `GET /documents/{id}/download`, `/documentos/{id}/download` | `backend/routes/api.php` |
| `GET /documents/{id}/permissions` | `backend/routes/api.php` |
| `GET/POST/PUT/DELETE /folders`, `/folders/tree`, `/folders/{id}` | `backend/routes/api.php` |
| `POST /folders/{id}/move` | `backend/routes/api.php` |
| `GET /folders/{id}/permissions` | `backend/routes/api.php` |

---

## Divergências Encontradas

- **DocumentsController vs DocumentsManager:** controller registrado em `Bootstrap` mas rotas desabilitadas; `DocumentsManager` é a fonte ativa.
- **FoldersController vs FoldersManager:** ambos registram `/pastas` e `/folders` em `rest_api_init` — registro duplicado.
- **Onboarding:** CMS expõe `/onboarding/options|select|status`; frontend store usa `/onboarding/current` e `/onboarding/requests`.
- **Equipes por área:** CMS tem `/areas/{id}/equipes` e `/areas/{area_id}/teams`; frontend usa `/areas/{id}/teams`.
- **Colaboradores:** frontend usa `/colaboradores`; CMS expõe `/areas/{id}/colaboradores` e `/singulares/{id}/colaboradores`.
- **Auditoria:** frontend `/auditoria/singulares`; CMS `/rbac/audit`.
- **Compartilhamento:** frontend `/document-sharing/*`; CMS possui metadado `portal_doc_sharing` sem controller REST dedicado localizado.
- **Auth refresh:** método `refresh` existe em `AuthController` mas não está em `registerRoutes`.
- **Namespaces mistos no frontend:** `portaldecomunicacao/v1` (CMS), `jwt-auth/v1`, rotas WP legadas (`/user/me`).

---

## Cobertura da Descoberta

### Controllers Cobertos

| Controller | Rotas mapeadas | Observação |
|---|---|---|
| EndpointsRegistry | 4 | Completo |
| AuthController | 5 | `refresh` não registrado |
| UsersController | 15 | Completo |
| RBACController | 3 | Completo |
| SingularesController | 10 | Completo |
| AreasController | 11 | Completo |
| TeamsController | 11 | Completo |
| DocumentsManager | 12 | Completo |
| DocumentsController | 0 ativas | Desabilitado |
| FoldersController | 16 | Completo |
| FoldersManager | 10 | Duplicado parcial |
| NotificationsController | 8 | Completo |
| OnboardingController | 3 | Completo |
| DiagnosticsController | 2 | Completo |

### Endpoints Cobertos

| Origem | Quantidade |
|---|---|
| CMS `register_rest_route` | ~98 |
| Backend legado | 20 |
| Órfãos frontend | 28 |

### Módulos Cobertos

| Status | Módulos |
|---|---|
| Com endpoints CMS | 18 |
| Sem endpoints dedicados | 5 (Busca Global, Cache, Navegação, Convidados, Configuração parcial) |
| Apenas órfãos / sem API | 4 (Analytics, Comunicados, Fique por Dentro, Central de Colaboração) |

---

## Resultado da Validação

### Validação 1

Todos os controllers possuem endpoints mapeados?

**SIM** — todos os controllers listados em `Bootstrap::registerControllers()` foram mapeados; `DocumentsController` sem rotas ativas documentado.

### Validação 2

Todos os módulos possuem endpoints associados?

**NÃO** — Comunicados, Fique por Dentro e Central de Colaboração sem API; Cache sem REST.

### Validação 3

Todos os endpoints consumidos pelo frontend foram localizados?

**NÃO** — 28 endpoints órfãos identificados.

### Validação 4

Existem endpoints órfãos?

**SIM**

### Validação 5

Existem endpoints públicos sensíveis?

**SIM** — debug de documentos, listagem pública de singulares/teams, `/status` com metadados internos.

### Validação 6

Existem divergências frontend/backend?

**SIM** — onboarding, colaboradores, auditoria, compartilhamento, registro duplicado de pastas.

### Validação 7

Existem endpoints legados?

**SIM** — 20 rotas em `backend/routes/api.php`.

---

## Status Final

**APROVADO COM RESSALVAS**

Ressalvas: 28 endpoints órfãos, endpoints públicos sensíveis em debug e dados organizacionais, registro duplicado de rotas de pastas, `DocumentsController` desabilitado e coexistência com backend legado. Não bloqueia continuidade da Discovery conforme validações anteriores.
