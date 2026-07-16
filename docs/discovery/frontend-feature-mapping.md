# Frontend Feature Mapping

**Version:** 1.1  
**Date:** 2026-07-15  
**Source:** Inventário do frontend legado em produção (`docs/discovery/frontend-production-discovery.md`)  
**Repository note:** Mapeamento baseado no legado; implementação TO-BE segue `docs/construction/frontend/00-frontend-foundation.md` e `specs/features/`.  
**Reference:** `docs/discovery/frontend-production-discovery.md`  
**Type:** Feature mapping only — no migration, no implementation

---

# FT-AUTH

**Feature name:** FT-AUTH — Authentication & Session

**Business objective:** Allow users to authenticate with institutional credentials, maintain session state, select active role/domain, and access the portal according to session validity. Handle first-access onboarding redirect and users without linked area.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `LoginPage.vue` | `/login` | Email/password login, domain selection, role selection | Medium |
| `LogoutPage.vue` | `/logout` | End session and redirect to login | Low |
| `NoAreaAccessPage.vue` | `/no-area-access` | Notice for authenticated users without linked area | Low |
| `OnboardingPage.vue` | `/app/onboarding` | First-access setup when user lacks singular/area context | Medium |
| Root redirect | `/` | Redirect to login | Low |
| `ErrorNotFound.vue` | `/:catchAll(.*)*` | Page not found | Low |
| `DebugAuthPage.vue` | `/app/administrador/:colaborador_slug/debug-auth` | Temporary auth debugging (administrator) | Low |

**Related layout:** `AuthLayout.vue` wraps login.

**Component without route:** `ErrorNotAuthorized.vue`, `public/HomePage.vue`.

---

## Components

- `RoleSelector`
- `NoAreaAccessNotice`
- `OnboardingBanner`
- `OnboardingRequestDialog`
- `DsCard` (domain selection modal on login)

---

## User Flows

| Flow | Evidence |
|---|---|
| **Login** | Email + password → JWT via `PortalApiService.login()` → token stored → optional domain modal → optional role selection → home route |
| **Logout** | `/logout` → `useAuth().logout({ hard: true })` → clear storage → `/login?switch=1` |
| **Session validation** | Boot hydration from `localStorage`; profile validation on load; 401 → redirect login |
| **Role switch** | `RoleSelector` on login; `ConfigDialog` active role update in sidebar |
| **Forgot password** | Redirect to Zimbra webmail URL by domain (external; no in-app reset) |
| **Onboarding redirect** | Router guard sends users without `singular_id`/`area_id` to `/app/onboarding` |
| **No-area redirect** | Guard sends non-admin users without `area_id` to `/no-area-access` |

---

## Dependencies

- Shared Layout (post-login shell)
- Permissions (role selection uses role definitions)
- Notifications (login error toasts)

---

## Foundation Requirements

- Layout (`AuthLayout`)
- Forms (login form validation)
- Dialogs (domain selection, role selection)
- Theme (login-specific SCSS in `css/components/login/`)
- Notifications (`usePortalToast`, Quasar `Notify`)

---

## Sprint Recommendation

**Sprint 1** — Earliest implementable after Sprint 0 layout and HTTP client shell exist.

---

## MVP

**Mandatory**

---

# FT-SINGULAR

**Feature name:** FT-SINGULAR — Singular (Organization) Management

**Business objective:** Manage Unimed singulars (cooperatives): list, create, view, edit, audit, and assign permissions. Provide singular-level panels for administrators and singular administrators.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `SinglePage.vue` | `/app/:singularSlug` | Singular-level panel | Medium |
| `OrganizationIndexPage.vue` | `/app/administrador/:colaborador_slug/singulares` | Singulares action hub | Low |
| `OrganizationCreatePage.vue` | `.../singulares/novo` | Create singular | Medium |
| `OrganizationList.vue` | `.../singulares/lista` | List singulares | Medium |
| `OrganizationShowPage.vue` | `.../singulares/:singular_identifier` | Singular details | Medium |
| `OrganizationEditPage.vue` | `.../singulares/:singular_identifier/editar` | Edit singular | High |
| `OrganizationPermissions.vue` | `.../singulares/:singular_identifier/permissoes` | Singular permissions | Medium |
| `OrganizationAudit.vue` | `.../administrador/:colaborador_slug/auditoria` | Singular audit log | Medium |
| `CollaboratorOrganizationPage.vue` | `.../minha-singular` | Collaborator view of own singular | Medium |
| `DepartmentManagement.vue` | `.../singulares/:singular_identifier/areas` | Area management within singular | High |

**Page without route:** `collaborator/MySingularPage.vue`.

**Redirect:** `.../singulares/gerenciar` → list.

---

## Components

- `OrganizationBasicInfoSection`
- `OrganizationCompanyInfoSection`
- `DsLinkSummaryCard`
- `DsPageHeader`
- `DsFormCard`
- `DsInfoDialog` (Minha Singular dialog in sidebar)
- `SidebarSingularAdminMenu`
- `SidebarAdministratorMenu` (Singulares menu item)

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Singular list, show page, panel (`SinglePage`), Minha Singular |
| **Create** | `OrganizationCreatePage` |
| **Edit** | `OrganizationEditPage` |
| **Search** | List filters via services |
| **Permissions** | `OrganizationPermissions` |
| **Audit** | `OrganizationAudit` |

---

## Dependencies

- Authentication
- Permissions
- Shared Layout
- Areas (singular contains areas via `DepartmentManagement`)

---

## Foundation Requirements

- Layout (admin hub pattern)
- Breadcrumb (`DsBreadcrumbs`, route meta)
- Forms (`useSingularForm`, `OrganizationBasicInfoSection`)
- DataTable / list cards (`OrganizationList`, `DsListCard`)
- Dialogs (`DsInfoDialog`)

---

## Sprint Recommendation

**Sprint 2** — Requires authenticated admin shell (Sprint 0–1).

---

## MVP

**Mandatory** (organizational root entity)

---

# FT-AREA

**Feature name:** FT-AREA — Area (Department) Management

**Business objective:** Manage organizational areas: create, list, view, edit, configure settings, link to sectors, and manage area-level permissions. Provide area panels and collaborator area views.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `AreaPage.vue` | `/app/:singularSlug/:areaSlug` | Area-level panel | Medium |
| `DepartmentIndexPage.vue` | `.../administrador/:colaborador_slug/areas` | Areas action hub | Low |
| `DepartmentCreatePage.vue` | `.../areas/novo`, `.../areas/editar/:slug?` | Create/edit area | High |
| `AreasListPage.vue` | `.../areas/lista` | List areas | Medium |
| `DepartmentShowPage.vue` | `.../areas/:idOrSlug` | Area details | Medium |
| `DepartmentPermissionsPage.vue` | `.../areas/:id/permissoes` | Area permissions | Medium |
| `AreaSettingsPage.vue` | `.../areas/settings` | Area settings | Medium |
| `VincularAreaSetorPage.vue` | `.../estruturas/areas/vincular` | Link area to sector | Medium |
| `CollaboratorAreaPage.vue` | `.../minha-area` | Collaborator area presentation | Medium |
| `DepartmentManagement.vue` | `.../areas/equipes` (legacy) | Teams by area listing | Medium |

**Archived:** `DepartmentCreatePage.old.vue` (not routed).

---

## Components

- `EditAreaDialog` (sidebar)
- `ContactFormItem`
- `areas/` component folder
- `SidebarAreaAdminMenu`
- `DsCreateAreaFolderDialog`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Area list, show, panel (`AreaPage`), Minha Área |
| **Create** | `DepartmentCreatePage` (novo) |
| **Edit** | `DepartmentCreatePage` (editar), `EditAreaDialog` |
| **Search** | Sidebar area search in collaborator/area-admin menus |
| **Permissions** | `DepartmentPermissionsPage` |
| **Link** | `VincularAreaSetorPage` |

---

## Dependencies

- Authentication
- Permissions
- Singular (areas belong to singular context)
- Shared Layout
- Collaborators (area collaborator management route)

---

## Foundation Requirements

- Layout
- Breadcrumb
- Forms (`ContactsManager`, phone masks)
- DataTable / lists
- Dialogs (`EditAreaDialog`)

---

## Sprint Recommendation

**Sprint 2** — Depends on auth; logically follows or parallels FT-SINGULAR.

---

## MVP

**Mandatory**

---

# FT-EQUIPE

**Feature name:** FT-EQUIPE — Team Management

**Business objective:** Create and manage teams within areas: list, create, edit, view details, manage members and team-scoped permissions.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `TeamListPage.vue` | `.../teams/lista` | List teams | Medium |
| `TeamCreatePage.vue` | `.../teams/novo`, `.../teams/editar/:slug` | Create/edit team | High |
| `TeamDetailsPage.vue` | `.../teams/:idOrSlug`, `.../teams/:id/permissoes` | Team details and permissions | Medium |
| `TeamMembersPage.vue` | `.../teams/:idOrSlug/membros` | Manage team members | Medium |
| `MyTeamPage.vue` | `.../meu-time` | Team view for admin/area admin | Medium |
| `TeamViewPage.vue` | `.../equipes/:teamId` | View team | Medium |
| `TeamAdministratorDocumentsPage.vue` | `.../documentos-time` | Team-scoped documents | High |
| `PlaceholderPage.vue` | `.../areas/equipes/novo`, `.../areas/equipes/editar/:id` | Legacy team form stubs | Low |

**Redirect:** `.../teams` → `teams/lista`.

---

## Components

- `TeamCreateDialog`
- `DsCreateTeamFolderDialog`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Team list, details, `MyTeamPage`, `TeamViewPage` |
| **Create** | `TeamCreatePage`, `TeamCreateDialog` (sidebar action) |
| **Edit** | `TeamCreatePage` (edit route) |
| **Members** | `TeamMembersPage` |
| **Permissions** | Team permissions route (reuses `TeamDetailsPage`) |

---

## Dependencies

- Authentication
- Permissions
- Areas (teams scoped to areas)
- Documents (team document route)
- Folders (`DsCreateTeamFolderDialog`)
- Shared Layout

---

## Foundation Requirements

- Layout
- Forms
- DataTable
- Dialogs (`TeamCreateDialog`)

---

## Sprint Recommendation

**Sprint 3** — Requires area context (Sprint 2).

---

## MVP

**Optional** (implemented in production; not all roles require teams — routes support optional `:equipeSlug`)

---

# FT-COLABORADOR

**Feature name:** FT-COLABORADOR — Collaborator & Guest Management

**Business objective:** Manage collaborators and guests (convidados): register, list, link to areas, and provide collaborator navigation panels.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `CollaboratorPage.vue` | `/app/:singularSlug/:areaSlug/[:equipeSlug/]:usuarioSlug` | Collaborator panel | Medium |
| `CollaboratorHomePage.vue` | `.../home` | Collaborator landing | Low |
| `CollaboratorIndexPage.vue` | `.../colaboradores` | Collaborators action hub | Low |
| `AdminCollaboratorsPage.vue` | `.../colaboradores/lista`, `.../gerenciar` | List/manage collaborators | Medium |
| `CollaboratorCreatePage.vue` | `.../colaboradores/novo` | Create collaborator | High |
| `CollaboratorManagement.vue` | `.../colaboradores/:singular_id/:area_id` | Area collaborator management | High |
| `CollaboratorLink.vue` | `.../colaboradores/vincular` | Link collaborator to area | Medium |
| `InvitedIndexPage.vue` | `/app/administrador/convidados`, `.../lista` | Guests hub and list | Medium |
| `CadastrarConvidadosPage.vue` | `.../convidados/novo` | Register guest | Medium |
| `PlaceholderPage.vue` | `.../colaboradores/:id/editar`, `.../auditoria`; guest manage/reports/audits/settings/docs | Stubs | Low |

**Pages without route:** `CollaboratorListPage.vue`, `guest/GuestHomePage.vue`.

---

## Components

- `CollaboratorLinkForm`
- `CollaboratorShareDialog`
- `SidebarCollaboratorMenu`
- `DsUserBindingCard`
- `dialogs/CollaboratorDetailsDialog`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Collaborator panel, list pages |
| **Create** | `CollaboratorCreatePage`, `CadastrarConvidadosPage` |
| **Edit** | Placeholder only (`colaboradores/:id/editar`) |
| **Link** | `CollaboratorLink` |
| **Search** | Collaborator list filters |

---

## Dependencies

- Authentication
- Permissions
- Areas
- Singular
- Shared Layout
- Users (overlap with FT-USUARIO for identity)

---

## Foundation Requirements

- Layout
- Forms (`useUserForm`, contact masks)
- DataTable (`AdminCollaboratorsPage`)
- Dialogs

---

## Sprint Recommendation

**Sprint 3** — Requires area/singular structure (Sprint 2).

---

## MVP

**Mandatory** (core portal audience)

---

# FT-DOCUMENTO

**Feature name:** FT-DOCUMENTO — Document Management

**Business objective:** Upload, list, view, download, share, and manage documents across personal, area, team, singular, and admin scopes with visibility controls.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `DocumentsPage.vue` | `/app/documentos`, `.../administrador/:colaborador_slug/documentos` | Shared/admin documents | High |
| `MyDocumentsPage.vue` | `.../documentos` (collaborator path) | Personal documents | High |
| `CollaboratorAreaDocumentsPage.vue` | `.../documentos-area` | Area-scoped documents | High |
| `TeamAdministratorDocumentsPage.vue` | `.../documentos-time` | Team documents | High |
| `SingularAdministratorDocumentsPage.vue` | `.../documentos-singular` | Singular documents | High |
| `DocumentIndexPage.vue` | `/app/administrador/documentos` | Documents action hub | Low |
| `PublicDocuments.vue` | `.../documentos/publicos` | Public document listing | Medium |
| `PrivateDocuments.vue` | `.../documentos/privados` | Private document listing | Medium |
| `PlaceholderPage.vue` | `.../documentos/novo`, `lista`, `filtrados`, `gerenciar`, `relatorios`, `auditorias`, `configuracoes` | Admin document stubs | Low |

**Page without route:** `documents/DocumentsWithFoldersPage.vue`.

---

## Components

- `DocumentList`
- `DocumentCard`
- `DocumentActions`
- `DocumentMetadata`
- `DocumentFolderBreadcrumb`
- `BulkDeleteDocuments`
- `DsDocumentCard`
- `DsDocumentGalleryView`
- `DsDocumentGalleryCard`
- `DsDocumentMetadataForm`
- `DsFileUpload`
- `AreaDocumentsToolbar`
- `PublicAreaToolbar`
- `shared/DocumentCard`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Document lists and gallery views per scope |
| **Upload** | `DsFileUpload`, `useDocumentUpload`, `UploadsService` |
| **Download** | `documentosService` blob download, signed URLs |
| **Delete** | `DocumentActions`, `BulkDeleteDocuments` |
| **Search** | Document list filters (type, visibility) |
| **Share** | `DocumentSharingService`, `CollaboratorShareDialog` |

---

## Dependencies

- Authentication
- Permissions
- Folders (folder breadcrumb, folder-scoped listing)
- Areas / Teams / Singular (scope context)
- Search (global search includes documents)
- Shared Layout
- Notifications

---

## Foundation Requirements

- Layout
- DataTable / gallery (`DsDataTable`, `DsDocumentGalleryView`)
- Forms (`DsDocumentMetadataForm`)
- File upload (`DsFileUpload`)
- Dialogs
- Toolbar (`DsStickyToolbar`, `AreaDocumentsToolbar`)

---

## Sprint Recommendation

**Sprint 3** — Earliest after auth and organizational context; folder structure (FT-PASTA) commonly used together.

---

## MVP

**Mandatory**

---

# FT-COMUNICADO

**Feature name:** FT-COMUNICADO — Communications & News

**Business objective:** Publish and consume portal communications: admin announcements (comunicados) and collaborator news feed ("Fique por Dentro").

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `AnnouncementsPage.vue` | `/app/administrador/:colaborador_slug/comunicados` | Admin announcements management | Medium |
| `StayInformedPage.vue` | `.../fique-por-dentro` | News feed (collaborator home content) | Medium |
| `StayInformedAllPostsPage.vue` | `.../fique-por-dentro/noticias` | Full news listing | Medium |
| `StayInformedPostPage.vue` | `.../fique-por-dentro/:postSlug` | Single news article | Medium |

**Sidebar:** Collaborator "Página Inicial" routes to `colaborador.fiquePorDentro`.

---

## Components

- `NewsCard`
- `DsRichTextEditor` (content editing where used)
- `InfiniteScrollList`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | News feed, post detail, all posts listing |
| **Publish** | `AnnouncementsPage` (admin comunicados) |
| **Search** | Post listing navigation by slug |

---

## Dependencies

- Authentication
- Permissions (admin vs collaborator routes)
- Shared Layout
- Areas (news scoped to collaborator path context)

---

## Foundation Requirements

- Layout
- Cards (`NewsCard`, `DsCard`)
- Breadcrumb
- Rich text editor (`DsRichTextEditor`)

---

## Sprint Recommendation

**Sprint 4** — Requires authenticated collaborator navigation (Sprint 1–3).

---

## MVP

**Mandatory** (core communication purpose of portal)

---

# FT-NOTIFICACAO

**Feature name:** FT-NOTIFICACAO — Notifications

**Business objective:** Display user notifications in a dedicated center and surface alerts via toast/notify patterns.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `NotificationCenterPage.vue` | `/app/notificacoes` | Central notification listing | Medium |

---

## Components

- `components/notifications/` folder
- `PortalToast` (DS organism)
- `usePortalToast`, `useNotify`, `portal-notify.ts`
- `stores/notifications.ts`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Notification center page |
| **Receive** | Toast notifications across app (login errors, API errors, uploads) |

---

## Dependencies

- Authentication
- Shared Layout

---

## Foundation Requirements

- Notifications (`PortalToast`, Quasar `Notify`)
- Layout (header may surface notification entry)

---

## Sprint Recommendation

**Sprint 4** — Earliest after auth shell; no hard dependency on other business features.

---

## MVP

**Optional** (center exists; toasts used cross-cutting)

---

# FT-BUSCA

**Feature name:** FT-BUSCA — Global Search

**Business objective:** Search portal content (documents, users, entities) from the application header and display consolidated results.

---

## Existing Screens

No dedicated search results page route. Search is integrated in `AppHeader` with results via `GlobalSearchResults` component.

---

## Components

- `GlobalSearchResults`
- `DsSearchInput`
- `GlobalSearchService`

---

## User Flows

| Flow | Evidence |
|---|---|
| **Search** | Header search input → `GlobalSearchService` → `GlobalSearchResults` |

---

## Dependencies

- Authentication
- Documents (searchable content)
- Users (searchable entities)
- Shared Layout (header integration)

---

## Foundation Requirements

- Header (`AppHeader`)
- Search input (`DsSearchInput`)
- Results panel (`GlobalSearchResults`)

---

## Sprint Recommendation

**Sprint 4** — Requires indexed content from FT-DOCUMENTO and entity modules.

---

## MVP

**Optional**

---

# FT-PERFIL

**Feature name:** FT-PERFIL — User Profile

**Business objective:** Allow users to view and edit their own profile, contacts, and preferences within collaborator context.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `ProfilePage.vue` | `.../perfil`, `.../informacoes` | View profile and information | Medium |
| `ProfileEditPage.vue` | `.../perfil/editar` (with/without team routes) | Edit profile | Medium |

**Sidebar:** Profile card opens `UserInfoDialog`; edit navigates to profile edit route.

---

## Components

- `UserInfoDialog`
- `DsSidebarProfile`
- `DsUserInfoCard`
- `DsUserAvatar`
- `ContactsManager` (profile contacts)
- `useUserProfile`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | `ProfilePage`, sidebar `UserInfoDialog` |
| **Edit** | `ProfileEditPage` |
| **Search** | — |

---

## Dependencies

- Authentication
- Shared Layout
- Areas (profile in collaborator path context)

---

## Foundation Requirements

- Layout
- Forms (masked phone fields)
- Dialogs (`UserInfoDialog`)
- Avatar (`DsAvatar`, `DsUserAvatar`)

---

## Sprint Recommendation

**Sprint 2** — Earliest after auth (Sprint 1); minimal entity dependencies.

---

## MVP

**Mandatory**

---

# FT-PASTA

**Feature name:** FT-PASTA — Folder Management

**Business objective:** Organize documents in hierarchical folder structures: create, rename, navigate trees, and manage folder permissions per area/team/user scope.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `FoldersManagementPage.vue` | `/app/administrador/:colaborador_slug/pastas` | Admin folder structure management | High |
| `MyFoldersPage.vue` | `.../minhas-pastas` | Personal folder tree | High |

**Page without route:** `collaborator/FolderTreePage.vue`.

---

## Components

- `FolderList`
- `UserFolderTree`
- `FolderPermissionManager`
- `FolderStatsCard`
- `RenameFolderDialog`
- `AreaFolderTreeSidebar`
- `DsCreateFolderDialog`
- `DsCreateAreaFolderDialog`
- `DsCreateTeamFolderDialog`
- `DsAreaFoldersManagementDialog`
- `DocumentFolderBreadcrumb`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Folder trees (`UserFolderTree`, `FolderList`) |
| **Create** | Create folder dialogs (sidebar + admin) |
| **Edit** | `RenameFolderDialog` |
| **Delete** | Folder management actions in `useFolderManagement` |
| **Navigate** | `useFolderNavigation`, breadcrumb |
| **Permissions** | `FolderPermissionManager`, `useFolderPermissions` |

---

## Dependencies

- Authentication
- Permissions
- Documents (documents live in folders)
- Areas / Teams (folder scope)
- Shared Layout

---

## Foundation Requirements

- Layout
- Tree navigation (`DsNavigationTree`, `AreaFolderTreeSidebar`)
- Dialogs (create/rename folder)
- Drag-and-drop (`useDragAndDrop`)

---

## Sprint Recommendation

**Sprint 3** — Tightly coupled with FT-DOCUMENTO; same sprint earliest.

---

## MVP

**Mandatory** (document organization depends on folders in production)

---

# FT-USUARIO

**Feature name:** FT-USUARIO — User Administration

**Business objective:** Administer portal users: create, list, edit, manage sessions, and configure user-level settings (reports/settings routes are stubs).

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `UserIndexPage.vue` | `.../usuarios` | Users action hub | Low |
| `UserCreatePage.vue` | `.../usuarios/novo` | Create user | High |
| `UserListPageNew.vue` | `.../usuarios/lista` | List users | Medium |
| `UserEditPage.vue` | `.../usuarios/editar/:username` | Edit user | High |
| `UserSessionsPage.vue` | `.../usuarios/sessoes` | Active sessions | Medium |
| `PlaceholderPage.vue` | `.../usuarios/relatorios`, `auditorias`, `configuracoes` | Stubs | Low |

**Sub-sections (within UserEditPage):** `UserIdentificationSection`, `UserRolesSection`, `UserPreferencesSection`, `UserEditSidebar`, modals (`ChangeAreaModal`, `LinkAreaModal`, `UnlinkAreaModal`).

**Legacy / unrouted:** `UserPage.vue`, `admin/users/AdminUsersPage.vue`.

**Note:** `UserPermissionsPage` mapped to FT-PERMISSAO.

---

## Components

- `DsUsersTable`
- `DsUserSearch`
- `DsUserAutocomplete`
- `AdminUsersManager`
- `AdminUserSearchField`
- `form/AdminUsersManager`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | User list, edit page, sessions page |
| **Create** | `UserCreatePage` |
| **Edit** | `UserEditPage` with sections |
| **Search** | User list filters, `DsUserSearch` |
| **Sessions** | `UserSessionsPage` (view/terminate) |

---

## Dependencies

- Authentication
- Permissions
- Areas (link/unlink area modals)
- Shared Layout
- Notifications

---

## Foundation Requirements

- Layout
- DataTable (`DsUsersTable`, `DsDataTable`)
- Forms (`useUserForm`, `useUserValidation`)
- Dialogs (area link modals)
- Breadcrumb

---

## Sprint Recommendation

**Sprint 3** — Requires auth and permission model (Sprint 1–2).

---

## MVP

**Mandatory** (admin user management is active)

---

# FT-PERMISSAO

**Feature name:** FT-PERMISSAO — Permissions & Access Control

**Business objective:** Manage access requests, role/permission assignments at singular, area, team, and user levels, and approve onboarding access.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `PermissionRequestsPage.vue` | `.../permissoes` | Access permission requests | Medium |
| `OnboardingRequestsPage.vue` | `.../onboarding` | Admin onboarding request approval | Medium |
| `OrganizationPermissions.vue` | `.../singulares/:id/permissoes` | Singular permissions | Medium |
| `DepartmentPermissionsPage.vue` | `.../areas/:id/permissoes` | Area permissions | Medium |
| `UserPermissionsPage.vue` | `.../usuarios/permissoes` | User roles and permissions | Medium |
| `PlaceholderPage.vue` | `.../permissoes/singulares`, `.../permissoes/areas` | Permission shortcut stubs | Low |

**Redirects:** `permissoes/colaboradores` → collaborator manage; `permissoes/convidados` → guest manage.

**Cross-cutting:** Route guards (`authPermissionGuard`, `rbacGuard`), `usePermissions`, `useAreaPermissions`, role meta on routes.

---

## Components

- `DsUserPermissionCard`
- `DsRoleChip`
- `DsRoleHierarchyCard`
- `FolderPermissionManager`
- `RoleSelector` (login role pick)

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Permission pages per entity level |
| **Approve / Reject** | `PermissionRequestsPage`, `OnboardingRequestsPage` |
| **Grant / Revoke** | `UserPermissionsPage`, entity permission pages |

---

## Dependencies

- Authentication
- Users
- Singular
- Areas
- Teams
- Folders (folder permissions)
- Shared Layout

---

## Foundation Requirements

- Layout
- Forms
- DataTable
- Dialogs
- Route guards (authorization infrastructure)

---

## Sprint Recommendation

**Sprint 2** — Role definitions and guards needed early; entity permission screens need Sprint 2 entities.

---

## MVP

**Mandatory**

---

# FT-FEDERACAO

**Feature name:** FT-FEDERACAO — Federation

**Business objective:** Present federation-level content and navigation to areas and systems/services across the Unimed federation structure.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `FederationPage.vue` | `.../federacao` | Federation content page | Medium |

**Sidebar:** "Federação" expansion with searchable area list in collaborator, area-admin, and singular-admin menus. `SystemsServicesDialog` for systems and services (sidebar dialog, not a routed page).

---

## Components

- `SystemsServicesDialog`
- `SidebarCollaboratorMenu` (Federação section)
- `SidebarAreaAdminMenu` (Federação section)
- `SidebarSingularAdminMenu` (Federação section)

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | `FederationPage` |
| **Search** | Area search within Federação sidebar expansion |
| **Navigate** | Area links from federation menu to `colaborador.area` |

---

## Dependencies

- Authentication
- Areas (area list in sidebar)
- Shared Layout

---

## Foundation Requirements

- Layout
- Sidebar (expandable menu sections)
- Dialogs (`SystemsServicesDialog`)
- Search input (sidebar area filter)

---

## Sprint Recommendation

**Sprint 4** — Requires area listing and collaborator navigation (Sprint 2–3).

---

## MVP

**Optional**

---

# FT-AREA-PUBLICA

**Feature name:** FT-AREA-PUBLICA — Public Area

**Business objective:** Display public-facing area content and documents accessible to authenticated collaborators within their area context.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `PublicAreaPage.vue` | `.../area-publica` | Public area content and documents | High |

**Sidebar:** "Área Pública" menu item in collaborator menu.

---

## Components

- `PublicAreaToolbar`
- `DocumentList` (filtered public content)
- `DsVisibilityBanner`

---

## User Flows

| Flow | Evidence |
|---|---|
| **View** | Public area page with filtered documents/posts |
| **Download** | Document download from public area listings |

---

## Dependencies

- Authentication
- Areas
- Documents
- Folders (parent_id taxonomy filtering in page logic)
- Shared Layout

---

## Foundation Requirements

- Layout
- Toolbar (`PublicAreaToolbar`)
- Document gallery/list components
- Breadcrumb

---

## Sprint Recommendation

**Sprint 4** — Requires FT-AREA and FT-DOCUMENTO (Sprint 2–3).

---

## MVP

**Optional** (implemented; distinct from core admin CRUD)

---

# Shared Infrastructure

**Feature name:** Shared Infrastructure

**Business objective:** Application shell, design system, routing, layouts, cross-cutting utilities, and screens that do not map exclusively to one business Feature.

---

## Existing Screens

| Page | Route | Purpose | Complexity |
|---|---|---|---|
| `AdminDashboard.vue` | `/app/administrador/:colaborador_slug`, `.../dashboard` | Default authenticated landing (admin) | Medium |
| `CollaborationPage.vue` | `/app/colaboracao` | Collaboration hub | Medium |
| `AdminWizardPage.vue` | `.../assistente` | Multi-step admin configuration wizard | High |
| `ToolsPage.vue` | `/app/administrador/ferramentas` | Admin tools | Medium |
| `SettingsPage.vue` | `/app/administrador/configuracoes` | Portal settings | Medium |
| `ComponentsShowcasePage.vue` | `.../componentes-showcase` | Design system showcase | Low |
| `PlaceholderPage.vue` | Multiple admin stub routes | Unimplemented action placeholders | Low |
| `DashboardPage.vue` | (no route) | Legacy dashboard | — |

**Layouts:** `MainLayout`, `AuthLayout`, `AdminLayout`, `PublicLayout`.

**App shell:** `AppHeader`, `AppSidebar`, `AppFooter`.

---

## Components

Full Design System (`components/ds/`): atoms, molecules, organisms (~54 exported components).

Cross-cutting: `useTheme`, `useLoading`, `useStandardErrorHandling`, `useCrudOperations`, `useFilters`, i18n, axios boot, router guards.

---

## User Flows

| Flow | Evidence |
|---|---|
| **Navigate** | MainLayout router-view, sidebar menus, breadcrumbs |
| **Theme toggle** | `ConfigDialog` → `useTheme` |
| **Collaborate** | `CollaborationPage`, `CommentsSection` |
| **Configure** | `SettingsPage`, `AdminWizardPage` |

---

## Dependencies

- None (foundation layer)

---

## Foundation Requirements

- Layout (all layouts)
- Header, Sidebar, Footer
- Breadcrumb
- Dialogs (generic `DsModal`, `DsFormDialog`)
- Forms (generic `DsInput`, `DsSelect`)
- DataTable (`DsDataTable`, `DsTable`)
- Notifications (`PortalToast`)
- Theme (`design-tokens.scss`, `useTheme`)

---

## Sprint Recommendation

**Sprint 0**

---

## MVP

**Mandatory** (foundation for all features)

---

# Final Summary

## Feature Matrix

| Feature | Screens | Components | Dependencies | Sprint | MVP |
|---|---:|---:|---|---|---|
| **FT-AUTH** | 7 | 5 | Shared Layout, Permissions, Notifications | Sprint 1 | Mandatory |
| **FT-SINGULAR** | 10 | 8 | Auth, Permissions, Areas, Shared Layout | Sprint 2 | Mandatory |
| **FT-AREA** | 10 | 5 | Auth, Permissions, Singular, Shared Layout | Sprint 2 | Mandatory |
| **FT-EQUIPE** | 8 | 2 | Auth, Permissions, Areas, Documents, Folders, Shared Layout | Sprint 3 | Optional |
| **FT-COLABORADOR** | 10 | 5 | Auth, Permissions, Areas, Singular, Users, Shared Layout | Sprint 3 | Mandatory |
| **FT-DOCUMENTO** | 9 | 14 | Auth, Permissions, Folders, Areas, Teams, Singular, Search, Shared Layout, Notifications | Sprint 3 | Mandatory |
| **FT-COMUNICADO** | 4 | 3 | Auth, Permissions, Shared Layout, Areas | Sprint 4 | Mandatory |
| **FT-NOTIFICACAO** | 1 | 4 | Auth, Shared Layout | Sprint 4 | Optional |
| **FT-BUSCA** | 0 | 3 | Auth, Documents, Users, Shared Layout | Sprint 4 | Optional |
| **FT-PERFIL** | 2 | 6 | Auth, Shared Layout, Areas | Sprint 2 | Mandatory |
| **FT-PASTA** | 2 | 11 | Auth, Permissions, Documents, Areas, Teams, Shared Layout | Sprint 3 | Mandatory |
| **FT-USUARIO** | 6 | 6 | Auth, Permissions, Areas, Shared Layout, Notifications | Sprint 3 | Mandatory |
| **FT-PERMISSAO** | 6 | 5 | Auth, Users, Singular, Areas, Teams, Folders, Shared Layout | Sprint 2 | Mandatory |
| **FT-FEDERACAO** | 1 | 4 | Auth, Areas, Shared Layout | Sprint 4 | Optional |
| **FT-AREA-PUBLICA** | 1 | 3 | Auth, Areas, Documents, Folders, Shared Layout | Sprint 4 | Optional |
| **Shared Infrastructure** | 7+ | 54+ (DS) | — | Sprint 0 | Mandatory |

---

## Screen Count by Feature

| Feature | Routed screens | Placeholder/stub screens included |
|---|---:|---|
| FT-AUTH | 7 | 0 |
| FT-SINGULAR | 10 | 0 |
| FT-AREA | 10 | 0 |
| FT-EQUIPE | 6 | 2 |
| FT-COLABORADOR | 8 | 2 |
| FT-DOCUMENTO | 7 | 2 |
| FT-COMUNICADO | 4 | 0 |
| FT-NOTIFICACAO | 1 | 0 |
| FT-BUSCA | 0 | 0 |
| FT-PERFIL | 2 | 0 |
| FT-PASTA | 2 | 0 |
| FT-USUARIO | 5 | 1 |
| FT-PERMISSAO | 5 | 1 |
| FT-FEDERACAO | 1 | 0 |
| FT-AREA-PUBLICA | 1 | 0 |
| Shared Infrastructure | 7 | multiple placeholders |

---

## Cross-Feature Screen Overlap

Some screens serve multiple features by scope:

| Screen | Primary Feature | Secondary Feature |
|---|---|---|
| `DocumentsPage.vue` | FT-DOCUMENTO | Shared (global route) |
| `SingularAdministratorDocumentsPage.vue` | FT-DOCUMENTO | FT-SINGULAR |
| `TeamAdministratorDocumentsPage.vue` | FT-DOCUMENTO | FT-EQUIPE |
| `CollaboratorAreaDocumentsPage.vue` | FT-DOCUMENTO | FT-AREA |
| `OnboardingPage.vue` | FT-AUTH | FT-PERMISSAO |
| `OnboardingRequestsPage.vue` | FT-PERMISSAO | FT-AUTH |
| `DepartmentManagement.vue` (singular areas) | FT-AREA | FT-SINGULAR |
| `UserPermissionsPage.vue` | FT-PERMISSAO | FT-USUARIO |
| `OrganizationPermissions.vue` | FT-PERMISSAO | FT-SINGULAR |
| `DepartmentPermissionsPage.vue` | FT-PERMISSAO | FT-AREA |

---

## Pages Not Mapped to Business Features

| Page | Classification | Reason |
|---|---|---|
| `CollaborationPage.vue` | Shared Infrastructure | No matching Feature in list |
| `AdminWizardPage.vue` | Shared Infrastructure | Cross-entity setup wizard |
| `ToolsPage.vue` | Shared Infrastructure | Admin utilities |
| `SettingsPage.vue` | Shared Infrastructure | Global portal settings |
| `ComponentsShowcasePage.vue` | Shared Infrastructure | DS demonstration |
| `PlaceholderPage.vue` (all routes) | Shared Infrastructure | Generic stub |
| `guest/GuestHomePage.vue` | FT-COLABORADOR (unrouted) | No active route |
| `documents/DocumentsWithFoldersPage.vue` | FT-DOCUMENTO / FT-PASTA (unrouted) | No active route |

---

## Sprint Dependency Chain (earliest implementable)

```
Sprint 0  →  Shared Infrastructure
Sprint 1  →  FT-AUTH
Sprint 2  →  FT-SINGULAR, FT-AREA, FT-PERMISSAO, FT-PERFIL
Sprint 3  →  FT-EQUIPE, FT-COLABORADOR, FT-DOCUMENTO, FT-PASTA, FT-USUARIO
Sprint 4  →  FT-COMUNICADO, FT-NOTIFICACAO, FT-BUSCA, FT-FEDERACAO, FT-AREA-PUBLICA
Later     →  Collaboration hub, analytics (disabled in routes), debug auth, placeholder-only admin hubs
```

---

*Mapping based on repository evidence only. Sprint assignments reflect earliest implementable order given documented dependencies, not a project plan defined in the repository.*
