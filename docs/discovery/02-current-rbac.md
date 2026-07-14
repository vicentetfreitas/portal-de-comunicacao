# Discovery — Current RBAC

## Objetivo

Mapear o modelo atual de autorização e controle de acesso do Portal de Comunicação, com base exclusiva em evidências do código-fonte (CMS WordPress, frontend Quasar e infraestrutura de proxy).

**Referência de módulos:** `docs/discovery/01-current-modules.md` (27 módulos — não alterados neste documento).

**Nível de confiança:** Alto para roles/capabilities canônicos do CMS (`RBACService`); Médio para matriz frontend (`rbac-routes.ts`, `auth-roles.ts`); Baixo onde há divergência explícita entre camadas.

---

## Resumo Executivo

| Role | Escopo | Status |
|---|---|---|
| `administrator` | Global — todas as capabilities `portal_*` | ATIVO |
| `singular_administrator` | Singular — gestão de singular e áreas vinculadas | ATIVO |
| `area_administrator` | Área — gestão de área, equipes, colaboradores e documentos da área | ATIVO |
| `team_owner` | Equipe — gestão de time e membros (CMS) | ATIVO |
| `collaborator` | Usuário — leitura privada e visualização de posts | ATIVO |
| `visitor` | Convidado — leitura de documentos públicos e posts | ATIVO |
| `team_administrator` | Equipe — definido apenas no frontend | PARCIAL |
| `singular_owner` | Referenciado em `DocumentsController` (não canônico no CMS) | PARCIAL |

---

## Roles Identificadas

### Role: administrator

#### Responsabilidade

Papel WordPress com acesso total ao portal. Recebe todas as capabilities `portal_*` canônicas via `RBACService::getCapabilitiesForRole()`.

#### Evidências

- `RBACService.php` — `CANONICAL_ROLES`, `getCapabilitiesForRole('administrator')`
- `ensure-admin-caps.php` — adiciona capabilities ao role `administrator`
- Frontend: `SYSTEM_ROLES`, `auth-roles.ts` (`isAdmin`), `ROLE_DEFINITIONS`

#### Permissões

Todas as 40+ capabilities listadas em `CANONICAL_CAPABILITIES` (`portal_manage_all`, `portal_manage_users`, `portal_view_audit`, etc.).

#### Restrições

Nenhuma restrição de escopo organizacional evidenciada no CMS para este role.

---

### Role: singular_administrator

#### Responsabilidade

Gestão de singular(es) associada(s): editar/visualizar singular e gerenciar áreas vinculadas.

#### Evidências

- `RBACService.php` — capabilities: `portal_manage_singular`, `portal_edit_singular`, `portal_view_singular`, `portal_manage_areas`, `portal_view_areas`
- `UsersController::checkAdminPermission()` — permitido listar/gerenciar usuários
- Frontend: `SYSTEM_ROLES`, `auth-roles.ts`, guards excluem exigência de `area_id`

#### Permissões

Capabilities de singular e área (leitura/gestão), sem `portal_manage_all`.

#### Restrições

Guards frontend (`guards.ts`): não exige `context.area_id` para `singular_administrator`.

---

### Role: area_administrator

#### Responsabilidade

Administração de área departamental: equipes, colaboradores, upload e leitura de documentos privados da área.

#### Evidências

- `RBACService.php` — `portal_view_areas`, `portal_edit_areas`, `portal_manage_teams`, `portal_upload_docs`, `portal_read_private_docs`
- `DocumentsController::checkCreatePermission()` — upload permitido
- `FoldersController::checkManagePermission()` — gerenciamento permitido
- `auth-roles.ts` — prioriza `area_administrator` quando `user.context.area_id` presente

#### Permissões

Gestão de área/equipe/colaborador e documentos na área (via capabilities e headers de contexto).

#### Restrições

Filtro por área via header `X-User-Area-Id` em documentos e pastas (`DocumentsController`, `FoldersPermissionsService`).

---

### Role: team_owner

#### Responsabilidade

Proprietário de equipe: editar time, relacionar colaboradores, atribuir membros e acessar documentos privados.

#### Evidências

- `RBACService.php` — `CANONICAL_ROLES['team_owner']`, capabilities: `portal_edit_team`, `portal_assign_team_members`, `portal_upload_docs`
- `Bootstrap.php` — CPT `team` com capabilities `portal_manage_teams`, etc.

#### Permissões

Capabilities de equipe e upload/leitura privada.

#### Restrições

Não mapeado em `SYSTEM_ROLES` nem `ROLE_PERMISSIONS_MATRIX` do frontend (`rbac-routes.ts`).

---

### Role: collaborator

#### Responsabilidade

Colaborador com acesso básico: visualizar áreas, colaboradores, documentos privados e posts.

#### Evidências

- `RBACService.php` — `portal_view_areas`, `portal_view_collaborators`, `portal_read_private_docs`, `portal_view_posts`
- `FoldersController::checkManagePermission()` — explicitamente permitido
- `Bootstrap.php` — role canônico inicializado se ausente
- Frontend: `SYSTEM_ROLES`, `routes.ts` meta `roles` inclui `collaborator`

#### Permissões

Leitura e colaboração; upload condicionado por `checkCreatePermission`/`checkManagePermission` (não por capability `portal_upload_docs` default).

#### Restrições

Guards (`guards.ts`): usuários sem `context.area_id` redirecionados para `/no-area-access` (exceto admin/singular_admin).

---

### Role: visitor

#### Responsabilidade

Convidado com acesso somente a conteúdo público.

#### Evidências

- `RBACService.php` — `portal_read_public_docs`, `portal_view_posts`
- `RBACService::cleanup()` — usuários de roles não canônicos migrados para `visitor`
- Frontend: `types/roles.ts` (`VISITOR`), `usePermissions.ts`, rotas `admin.convidados.*`

#### Permissões

Leitura de documentos públicos e visualização de posts.

#### Restrições

Capabilities default não incluem `portal_read_private_docs` nem `portal_upload_docs`.

---

### Role: team_administrator (frontend)

#### Responsabilidade

Administrador de equipe — definido no frontend como papel distinto de `team_owner`.

#### Evidências

- `types/roles.ts` — `TEAM_ADMINISTRATOR: 'team_administrator'`
- `usePermissions.ts` — capabilities próprias
- `UsersController` comentário menciona `team_administrator` como negado em `checkAdminPermission`

#### Permissões

Frontend: `portal_manage_own_team`, `portal_invite_team_members` (`ROLE_DEFINITIONS`).

#### Restrições

**Não consta** em `RBACService::CANONICAL_ROLES`. Divergência com `team_owner` do CMS.

---

## Capabilities Identificadas

Tabela consolidada das capabilities `portal_*` canônicas em `RBACService::CANONICAL_CAPABILITIES`:

| Capability | Módulo | Evidência |
|---|---|---|
| `portal_manage_users` | Usuários | `RBACService`, `UsersController` |
| `portal_rbac_grant_role` | RBAC | `RBACService` |
| `portal_rbac_revoke_role` | RBAC | `RBACService` |
| `portal_view_roles` | RBAC | `RBACService` |
| `portal_manage_all` | Global | `RBACService`, `ensure-admin-caps.php` |
| `portal_manage_singular` | Singulares | `RBACService`, CPT `singular` |
| `portal_create_singular` | Singulares | `RBACService`, `Bootstrap.php` |
| `portal_edit_singular` | Singulares | `RBACService` |
| `portal_view_singular` | Singulares | `RBACService` |
| `portal_delete_singular` | Singulares | `RBACService` |
| `portal_read_singular` | Singulares | `RBACService` |
| `portal_manage_areas` | Áreas | `RBACService`, taxonomia `portal_area` |
| `portal_create_areas` | Áreas | `RBACService` |
| `portal_edit_areas` | Áreas | `RBACService` |
| `portal_view_areas` | Áreas | `RBACService` |
| `portal_relate_areas_collaborators` | Colaboradores | `RBACService` |
| `portal_manage_teams` | Equipes | `RBACService`, CPT `team` |
| `portal_create_team` | Equipes | `RBACService` |
| `portal_edit_team` | Equipes | `RBACService` |
| `portal_view_teams` | Equipes | `RBACService` |
| `portal_relate_teams_collaborators` | Equipes | `RBACService` |
| `portal_assign_team_members` | Equipes | `RBACService` |
| `portal_manage_collaborators` | Colaboradores | `RBACService` |
| `portal_create_collaborator` | Colaboradores | `RBACService` |
| `portal_edit_collaborator` | Colaboradores | `RBACService` |
| `portal_view_collaborators` | Colaboradores | `RBACService` |
| `portal_delete_collaborator` | Colaboradores | `RBACService` |
| `portal_manage_visitors` | Convidados | `RBACService` |
| `portal_view_visitors` | Convidados | `RBACService` |
| `portal_convert_visitor` | Convidados | `RBACService` |
| `portal_upload_docs` | Documentos | `RBACService` |
| `portal_read_private_docs` | Documentos | `RBACService` |
| `portal_approve_docs` | Documentos | `RBACService` |
| `portal_read_public_docs` | Documentos | `RBACService`, role `visitor` |
| `portal_manage_documents` | Documentos | `RBACService`, `ensure-admin-caps.php` |
| `portal_document_manage_folders` | Pastas | `RBACService`, `FoldersController` |
| `portal_document_view_documents` | Documentos | `RBACService`, `FoldersController` |
| `portal_document_edit_documents` | Documentos | `RBACService` |
| `portal_document_read_documents` | Documentos | `RBACService` |
| `portal_manage_posts` | Fique por Dentro | `RBACService` |
| `portal_create_post` | Fique por Dentro | `RBACService` |
| `portal_edit_post` | Fique por Dentro | `RBACService` |
| `portal_view_posts` | Fique por Dentro | `RBACService` |
| `portal_publish_post` | Fique por Dentro | `RBACService` |
| `portal_view_audit` | Auditoria | `RBACService` |
| `portal_export_audit` | Auditoria | `RBACService` |
| `portal_manage_permissions` | Permissões de Pastas | `RBACService` |
| `portal_view_analytics` | Analytics | `RBACService`, `ensure-admin-caps.php` |

**Capability referenciada fora da lista canônica:** `portal_rbac_manage` — usada em `RBACController::checkAdminPermission()` mas **ausente** em `CANONICAL_CAPABILITIES`.

**Capabilities WordPress nativas usadas em validações:** `manage_options`, `edit_posts`, `delete_posts` (`RestController`, `DiagnosticsController`).

---

## Matriz RBAC

Mapeamento por módulo (referência `01-current-modules.md`). Permissões = evidência de `permission_callback` ou check dedicado.

| Módulo | Role | Permissão | Evidência |
|---|---|---|---|
| Autenticação e Sessão | Público | `POST /auth/login` sem JWT | `AuthController` — `__return_true` |
| Autenticação e Sessão | Autenticado | `GET /auth/me`, `/auth/context` exige JWT | `AuthController::verifyJwtToken` |
| Usuários | `administrator`, `singular_administrator`, `area_administrator` | Criar/editar/deletar usuários | `UsersController::checkAdminPermission` |
| Usuários | Autenticado | Listar usuários (filtro RBAC no `index`) | `UsersController::checkListUsersPermission` |
| Usuários | Self ou admin | Ver/editar usuário específico | `UsersController::checkUserAccess` |
| RBAC | `administrator` ou `portal_rbac_manage` | Auditoria/reset RBAC | `RBACController::checkAdminPermission` |
| Singulares | Público | `GET /singulares` (listagem) | `SingularesController` — `__return_true` |
| Singulares | Autenticado | CRUD protegido | `SingularesController::checkJwtAuth` |
| Singulares | Admin | Vincular área a singular | `SingularesController::checkAdminPermission` |
| Áreas | Autenticado | Todas as rotas `/areas/*` | `AreasController::checkJwtAuth` |
| Equipes | Público | `GET /teams`, `/teams/{slug}` | `TeamsController` — `__return_true` |
| Equipes | Autenticado | CRUD e membros | `TeamsController::checkJwtAuth` |
| Colaboradores | Autenticado | Rotas em `AreasController`/`SingularesController` | `checkJwtAuth` |
| Documentos | Autenticado | Listagem/stats | `DocumentsController::checkJwtAuth` |
| Documentos | `administrator`, `area_administrator`, `singular_owner`* | Upload | `DocumentsController::checkCreatePermission` |
| Documentos | Autenticado + ACL | Leitura por documento | `DocumentsController::checkReadPermission` → `canAccess()` |
| Pastas | Autenticado | Visualizar árvore | `FoldersController::checkViewPermission` |
| Pastas | `administrator`, `area_administrator`, `collaborator` | Criar/editar pastas | `FoldersController::checkManagePermission` |
| Permissões de Pastas | Autenticado + hierarquia | `FoldersPermissionsService` | Headers + roles DB |
| Notificações | Autenticado | Listar/marcar lidas/SSE | `NotificationsController::checkLoggedIn` |
| Notificações | Admin | Criar notificação | `NotificationsController::checkAdminPermission` |
| Onboarding | Autenticado | `/onboarding/options`, `/select`, `/status` | `OnboardingController::checkJwtAuth` |
| Auditoria | Admin | `GET /rbac/audit` | `RBACController::checkAdminPermission` |
| Configuração do Portal | Público | `GET /configuracoes-publicas` | `EndpointsRegistry` — `__return_true` |
| API — Saúde | Público | `/ping`, `/status`, `/version` | `EndpointsRegistry` — `__return_true` |
| Diagnóstico | `manage_options` | `/diagnostics/*` | `DiagnosticsController` |
| Analytics | Não localizado | Endpoint `/analytics/dashboard` ausente no CMS | `analytics.ts` store |
| Comunicados | Não localizado | Sem validação backend dedicada | `AnnouncementsPage` (mock) |
| Convidados | `visitor` (CMS) | Capabilities `portal_*_visitors` | `RBACService` |
| Backend PHP Legado | JWT + role | Rotas em `backend/routes/api.php` | `AuthMiddleware::requireRole` (código `src/` ausente) |

\* `singular_owner` usado em `DocumentsController` — role **não canônico** no `RBACService` (canônico: `singular_administrator`).

---

## Contextos de Acesso

### Global

- Role `administrator` com `portal_manage_all` e `manage_options`.
- Endpoints públicos: `/auth/login`, `/ping`, `/status`, `/version`, `/configuracoes-publicas`.
- Evidência: `EndpointsRegistry`, `AuthController`, `ensure-admin-caps.php`.

### Singular

- Contexto em `user.context.singular_id`, `singular_slug` retornado por `/auth/me` e `/auth/context`.
- CPT `singular` com capabilities `portal_*_singular`.
- Filtro de dados por singular em services (evidência indireta em `SingularesService`, rotas aninhadas).
- Frontend: `auth-context.setSingularContext()`, rotas `/app/{singularSlug}/...`.

### Área

- Taxonomia `portal_area`; contexto `area_id`, `area_slug` no JWT/resposta de auth.
- Header `X-User-Area-Id` enviado pelo Axios a partir de `user.context.area_id` (`axios.ts`).
- `DocumentsController` e `FoldersPermissionsService` usam header para filtro/validação RBAC.
- Guard frontend: colaboradores sem `area_id` → `/no-area-access`.

### Equipe

- CPT/taxonomia `team`; contexto `team_slug`, `team_id` em `user.context`.
- Header `X-User-Team-Id` propagado pelo Axios (`axios.ts`, Nginx `server/nginx.conf`).
- Role CMS `team_owner` com capabilities de equipe.

### Usuário

- Escopo pessoal via `colaborador_slug`, pastas em `uploads/portaldecomunicacao/documentos/{folder_path}`.
- `UsersController::checkUserAccess` — self ou admin.
- Pastas com visibilidade `public`/`private` por hierarquia (`FoldersPermissionsService`, `DirectoryService`).

---

## Headers e Contexto

| Header | Finalidade | Evidência |
|---|---|---|
| `Authorization` | JWT Bearer — autenticação stateless | `AuthMiddleware`, `RestController::checkJwtAuth`, `axios.ts` |
| `X-Active-Role` | Role ativo na sessão (role switching) | `axios.ts`, `DocumentsController`, `FoldersController`, `FoldersPermissionsService`, `Bootstrap.php` (CORS) |
| `X-User-Area-Id` | ID da área para filtro RBAC | `axios.ts`, `DocumentsController`, Nginx proxy |
| `X-User-Team-Id` | ID da equipe para contexto | `axios.ts`, Nginx proxy |
| `X-Bypass-Area-Filter` | Bypass de filtro por área (query param) | `axios.ts`, `Bootstrap.php` (CORS allow list) |
| `X-Request-Id` | Correlação de requisição (não RBAC, mas exposto em CORS) | `axios.ts`, `CorsMiddleware` |

### Troca de contexto (role switching)

1. Usuário com múltiplos roles → `auth-roles.setActiveRole(role)` persiste em `localStorage` (`portal_active_role`, `portal_last_active_role`).
2. Interceptors Axios (`api`, `cmsApi`) leem role ativo e enviam `X-Active-Role`.
3. Backend prioriza header em checks (`DocumentsController`, `FoldersPermissionsService`, `UsersController`).
4. **Validação de segurança:** `DocumentsController::checkDeletePermission` só aceita `X-Active-Role` se o usuário possui o role no DB.
5. Frontend guards **não** revalidam `meta.roles` após autenticação (`guards.ts` — "PERMITINDO ACESSO TOTAL").

---

## Dependência dos Módulos

| Módulo (doc 01) | RBAC Obrigatório | Evidência |
|---|---|---|
| Autenticação e Sessão | JWT para rotas protegidas; login público | `AuthController`, `JWT` |
| Usuários | JWT + checks por operação | `UsersController` |
| RBAC | `checkAdminPermission` / `portal_rbac_manage` | `RBACController` |
| Singulares | JWT (parcialmente público em GET) | `SingularesController` |
| Áreas | JWT em todas as rotas | `AreasController` |
| Equipes | JWT (parcialmente público em GET) | `TeamsController` |
| Colaboradores | JWT via controllers de área/singular | `AreasController` |
| Documentos | JWT + ACL por documento + headers | `DocumentsController`, `DocumentsService` |
| Pastas e Diretórios | JWT + `checkViewPermission`/`checkManagePermission` | `FoldersController` |
| Permissões de Pastas | JWT + `FoldersPermissionsService` | `FoldersPermissionsService` |
| Solicitação de Permissões | Não localizado no CMS | `PermissionRequestService` (frontend) |
| Notificações | JWT (`checkLoggedIn`) | `NotificationsController` |
| Onboarding | JWT | `OnboardingController` |
| Auditoria | Admin | `RBACController` |
| Armazenamento e Upload | Indireto via Documentos | `StorageService` |
| Configuração do Portal | Público (`configuracoes-publicas`) | `EndpointsRegistry` |
| API — Saúde | Público | `EndpointsRegistry` |
| Diagnóstico | `manage_options` | `DiagnosticsController` |
| Busca Global | Depende de APIs subjacentes (JWT) | `GlobalSearchService` |
| Analytics | Capability `portal_view_analytics`; endpoint ausente | `RBACService`, `analytics.ts` |
| Comunicados | Sem RBAC backend evidenciado | `AnnouncementsPage` |
| Fique por Dentro | JWT via `/documentos` | `StayInformedPage` |
| Convidados | Role `visitor` + rotas frontend | `RBACService`, `routes.ts` |
| Central de Colaboração | Sem RBAC API evidenciado | `CollaborationHub.vue` |
| Navegação e Interface | Menu filtrado por roles (`menu-rbac.ts`) | `generateMenuDefinition` |
| Cache | Sem RBAC dedicado | `CacheService` |
| Backend PHP Legado | JWT + middleware role (não operacional) | `backend/routes/api.php` |

---

## Lacunas Encontradas

### Roles

- `team_administrator` (frontend) vs `team_owner` (CMS) — nomenclatura divergente.
- `singular_owner` referenciado em `DocumentsController::checkCreatePermission` — não existe em `CANONICAL_ROLES`.
- `federacao_owner`, `singular_owner`, `guest` em `menu-rbac.ts` — sem correspondência no CMS.
- `area_administrator` ausente em `ROLE_PERMISSIONS` de `auth-roles.ts` (mapeamento incompleto).

### Capabilities

- `portal_rbac_manage` usada em `RBACController` mas ausente em `CANONICAL_CAPABILITIES`.
- Capabilities em `types/roles.ts` (`portal_create_area`, `portal_manage_visitor`) **não** listadas em `CANONICAL_CAPABILITIES`.
- Permissões frontend (`admin.full_access`, `dashboard.view`) em `ROLE_PERMISSIONS_MATRIX` — sem espelho direto no CMS.

### Páginas sem proteção por role

- `guards.ts` declarado "ULTRA-PERMISSIVO": usuários autenticados navegam sem checagem de `meta.roles`.
- `canAccessRoute()` em `rbac-routes.ts` existe mas **não** é aplicado pelo guard ativo.

### Endpoints sem validação RBAC adequada

- `SingularesController`: `GET /singulares`, `GET /singulares/{id}` — `__return_true`.
- `TeamsController`: `GET /teams`, `GET /teams/{slug}`, `GET /areas/{id}/teams` — `__return_true`.
- `DiagnosticsController`: usa `current_user_can('manage_options')` sem `checkJwtAuth` explícito.
- `PermissionRequestService` endpoint `/permission-requests` — controller CMS não localizado.

### Divergências frontend/backend

| Item | CMS | Frontend |
|---|---|---|
| Role de equipe | `team_owner` | `team_administrator` |
| Role singular upload | `singular_administrator` | `singular_owner` em check de upload |
| Matriz de roles | 6 roles canônicos | 4 em `SYSTEM_ROLES`; 6 em `types/roles.ts` |
| Hierarquia | `RBACService` (implícita por capabilities) | 3 definições: `ROLE_PRIORITY`, `ROLE_HIERARCHY` (menu), `ROLE_HIERARCHY` (types) |
| Guard de rotas | N/A | Permissivo — não aplica `meta.roles` |
| Onboarding endpoints | `/options`, `/select`, `/status` | Store chama `/current`, `/requests` |

---

## Cobertura da Descoberta

### Roles Cobertas

| Role | Origem | Documentada |
|---|---|---|
| `administrator` | `RBACService` | Sim |
| `singular_administrator` | `RBACService` | Sim |
| `area_administrator` | `RBACService` | Sim |
| `team_owner` | `RBACService` | Sim |
| `collaborator` | `RBACService` | Sim |
| `visitor` | `RBACService` | Sim |
| `team_administrator` | `types/roles.ts` (frontend) | Sim (PARCIAL) |
| `singular_owner` | `DocumentsController` | Sim (PARCIAL) |

### Capabilities Cobertas

| Grupo | Quantidade | Fonte |
|---|---|---|
| Administração/RBAC | 4 | `CANONICAL_CAPABILITIES` |
| Singular | 6 | `CANONICAL_CAPABILITIES` |
| Área | 5 | `CANONICAL_CAPABILITIES` |
| Equipes | 6 | `CANONICAL_CAPABILITIES` |
| Colaborador | 5 | `CANONICAL_CAPABILITIES` |
| Convidado | 3 | `CANONICAL_CAPABILITIES` |
| Documentos | 9 | `CANONICAL_CAPABILITIES` |
| Postagens | 5 | `CANONICAL_CAPABILITIES` |
| Auditoria/Permissões/Analytics | 4 | `CANONICAL_CAPABILITIES` |
| **Total canônico** | **47** | `RBACService.php` |

### Módulos Cobertos

| Módulo (doc 01) | Mapeamento RBAC |
|---|---|
| 27 módulos listados | 27 referenciados na Matriz RBAC e Dependência dos Módulos |
| Módulos ATIVO (18) | Validação por controller/service evidenciada |
| Módulos PARCIAL (8) | Lacunas de endpoint ou guard documentadas |
| Módulo LEGADO (1) | Backend PHP — rotas declaradas, `src/` ausente |

---

## Resultado da Validação

### Validação 1

Todas as roles foram identificadas?

**SIM** — 6 canônicas no CMS + 2 divergentes (`team_administrator`, `singular_owner`) registradas como PARCIAL.

### Validação 2

Todas as capabilities foram classificadas?

**SIM** — 47 capabilities `portal_*` de `CANONICAL_CAPABILITIES` mapeadas; `portal_rbac_manage` registrada como lacuna.

### Validação 3

Todos os módulos possuem mapeamento RBAC?

**SIM** — todos os 27 módulos do doc 01 possuem linha na Matriz RBAC (com nota quando RBAC não localizado).

### Validação 4

Existem páginas sem proteção identificada?

**SIM** — `guards.ts` não aplica `meta.roles`; rotas autenticadas acessíveis a qualquer usuário logado.

### Validação 5

Existem endpoints sem validação RBAC identificada?

**SIM** — múltiplos `GET` com `__return_true` em Singulares e Teams; endpoints públicos de saúde/config.

### Validação 6

Existe divergência entre frontend e backend?

**SIM** — roles de equipe/singular, matrizes de permissão, guards permissivos e endpoints de onboarding.

---

## Status Final

**APROVADO COM RESSALVAS**

**Motivo:** modelo RBAC do CMS está documentado e operacional para roles/capabilities canônicos, porém as validações 4, 5 e 6 falharam — guards frontend ultra-permissivos, endpoints públicos sem JWT em recursos sensíveis, e divergências estruturais entre nomenclatura e matrizes frontend/backend impedem considerar o modelo atual como consistente e fechado.
