# Frontend Production Discovery Report

**Version:** 1.1  
**Date:** 2026-07-15  
**Scope:** Inventário do frontend legado em produção (referência externa ao repositório TO-BE)  
**Repository note:** O diretório `frontend/` neste repositório contém o scaffold Quasar da Sprint 0 — não o inventário completo descrito abaixo.  
**Type:** Inventory only — no recommendations, no migration analysis

---

## 1. Executive Summary

### Purpose

The **Portal de Comunicação** is an internal communication and document management web application for Unimed Ceará and affiliated singulars (cooperatives). It provides role-based access to organizational content: news ("Fique por Dentro"), documents, folders, areas, teams, collaborators, and administrative management of the portal structure.

### Frontend Technology

- **Framework:** Vue 3 (Composition API, `<script setup>`)
- **UI Framework:** Quasar v2
- **Language:** TypeScript
- **Build:** Quasar App Vite (`@quasar/app-vite`)
- **Deployment mode:** SPA (history router); SSR and PWA build scripts exist

### Project Size

| Metric | Approximate count |
|---|---:|
| Total `.vue` files in `src/` | 294 |
| Page-level views (`pages/`) | 96 |
| Components (`components/`) | 193 |
| Layouts | 4 |
| Services | 18 |
| Composables | 67 files (64 functions) |
| Pinia stores | 11 modules |
| Route path definitions | 100 |
| TypeScript type files | ~24 |

### Major Modules

1. **Authentication & Session** — JWT login via WordPress REST API, role selection, onboarding
2. **Collaborator Area** — Profile, documents, folders, news, federation, public area
3. **Administration** — Singulares, areas, teams, collaborators, users, documents, folders, permissions
4. **Documents & Folders** — Upload, gallery, sharing, folder tree, permissions
5. **Design System (`components/ds/`)** — Atomic design components (atoms, molecules, organisms)
6. **Collaboration & Notifications** — Collaboration hub, notification center
7. **Onboarding** — First-access setup and admin approval of access requests

---

## 2. Technology Inventory

| Category | Technology |
|---|---|
| **Framework** | Vue 3.5.x |
| **UI Library** | Quasar 2.16.x |
| **State Management** | Pinia 3.x (modular auth stores + domain stores) |
| **HTTP Client** | Axios 1.2.x (two instances: `api`, `cmsApi`) |
| **Router** | Vue Router 4.x (history mode) |
| **Authentication** | JWT Bearer tokens via WordPress REST API (`portaldecomunicacao/v1`); optional email gate (Zimbra IMAP/SMTP) via `AuthService` |
| **Package Manager** | Yarn 3.6.4 (specified in `packageManager` field) |
| **Build Tool** | Vite 5.x via Quasar App Vite 2.x |
| **Internationalization** | vue-i18n 9.x (`pt-BR` default, `en-US` available) |
| **Charts** | Not present as a dedicated chart library; Quasar icons used for analytics UI references |
| **Editors** | `DsRichTextEditor` (custom DS component); `DsSqlEditor` (admin tooling) |
| **Upload libraries** | Custom `DsFileUpload`, `useFileUpload`, `UploadsService`, Quasar `QUploader` patterns |
| **Icons** | Material Design Icons v7 (`@quasar/extras`), MDI via Quasar icon set `mdi-v7` |
| **CSS Framework** | Quasar SCSS variables + custom design tokens |
| **Theme** | Custom CSS variables (`design-tokens.scss`), Quasar `quasar.variables.scss`, `useTheme` composable (light/dark/auto) |
| **Fonts** | Unimed Sans, Unimed Serif, Unimed Slab, Unimed Brush (referenced in `fonts.css`); Inter as CSS fallback in tokens |
| **Utilities** | Custom composables (`useFilters`, `useCache`, `useCrudOperations`, etc.), SCSS mixins (`_responsive.scss`, `_utilities.scss`) |
| **Testing** | Vitest (unit), Playwright (e2e) |
| **Linting** | ESLint 9, Stylelint 16, Prettier 3 |

---

## 3. Folder Structure

```
frontend/
├── public/                    # Static assets (logo SVG, images placeholder)
├── src/
│   ├── App.vue
│   ├── boot/                  # Quasar boot files (axios, i18n, auth, theme, seed, etc.)
│   ├── components/
│   │   ├── admin/
│   │   ├── app/               # AppHeader, AppSidebar, AppFooter
│   │   ├── areas/
│   │   ├── auth/
│   │   ├── collaborator/
│   │   ├── collaboration/
│   │   ├── design-system/     # Legacy/alternate DS examples
│   │   ├── dialogs/
│   │   ├── documents/
│   │   ├── ds/                # Primary design system (atoms/molecules/organisms)
│   │   ├── folders/
│   │   ├── form/
│   │   ├── menus/
│   │   ├── notifications/
│   │   ├── search/
│   │   ├── shared/
│   │   ├── sidebar/
│   │   ├── ui/
│   │   └── user/
│   ├── composables/
│   │   ├── documents/
│   │   ├── folders/
│   │   ├── forms/
│   │   └── layout/
│   ├── config/
│   ├── constants/             # Routes, RBAC, layout config
│   ├── css/                   # Global SCSS, tokens, login styles
│   ├── design-tokens/         # icons.tokens.ts
│   ├── i18n/                  # pt-BR.json, en-US.json
│   ├── layouts/               # AuthLayout, MainLayout, AdminLayout, PublicLayout
│   ├── pages/
│   │   ├── admin/             # (+ areas/, colaboradores/, teams/, user-edit/, users/)
│   │   ├── areas/
│   │   ├── collaboration-hub/
│   │   ├── collaborator/
│   │   ├── documents/
│   │   ├── guest/
│   │   ├── notifications/
│   │   ├── onboarding/
│   │   ├── public/
│   │   ├── shared/
│   │   ├── singular/
│   │   ├── singular-admin/
│   │   └── team-admin/
│   ├── plugins/
│   ├── router/                # routes, guards, middleware, aliases
│   ├── services/              # API service layer
│   ├── stores/                # Pinia stores (+ auth/ submodule)
│   ├── styles/
│   ├── types/
│   └── utils/
├── quasar.config.ts
├── package.json
└── generate-tokens.cjs
```

---

## 4. Screen Inventory

Screens are grouped by module. Routes use Vue Router history mode. Dynamic segments: `:singularSlug`, `:areaSlug`, `:equipeSlug`, `:usuarioSlug`, `:colaborador_slug`.

**Complexity legend:** Low = static/simple form; Medium = lists + forms; High = multi-component workflows, trees, uploads, or heavy API orchestration.

---

### 4.1 Public / Auth

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Root Redirect | `/` | Redirects to login | No | — | Low |
| Login | `/login` | User authentication (email + password, domain selection, role selection) | No (guest only) | `LoginPage`, `RoleSelector`, `DsCard` | Medium |
| Logout | `/logout` | Session termination and redirect | No | `LogoutPage` | Low |
| No Area Access | `/no-area-access` | Notice for authenticated users without linked area | Yes | `NoAreaAccessPage`, `NoAreaAccessNotice` | Low |
| Error 404 | `/:catchAll(.*)*` | Page not found | No | `ErrorNotFound` | Low |
| Error Not Authorized | (component exists: `ErrorNotAuthorized.vue`; no active route found in `routes.ts`) | Unauthorized access message | — | `ErrorNotAuthorized` | Low |

---

### 4.2 Onboarding

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Onboarding | `/app/onboarding` | First-access configuration for users without singular/area context | Yes | `OnboardingPage`, `OnboardingBanner`, `OnboardingRequestDialog` | Medium |
| Admin Onboarding Requests | `/app/administrador/:colaborador_slug/onboarding` | Admin review of onboarding requests | Yes (administrator) | `OnboardingRequestsPage` | Medium |

---

### 4.3 Shared Authenticated

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| App Root Redirect | `/app` | Redirects to admin dashboard | Yes | — | Low |
| Documents (shared) | `/app/documentos` | Shared documents view | Yes | `DocumentsPage`, `DocumentList`, `DsDocumentGalleryView` | High |
| Collaboration Hub | `/app/colaboracao` | Central collaboration area | Yes | `CollaborationPage`, `CommentsSection` | Medium |
| Notification Center | `/app/notificacoes` | User notifications | Yes | `NotificationCenterPage` | Medium |

---

### 4.4 Dynamic Entity Panels

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Singular Panel | `/app/:singularSlug` | Singular-level panel | Yes (administrator, singular_administrator) | `SinglePage` | Medium |
| Area Panel | `/app/:singularSlug/:areaSlug` | Area-level panel | Yes (administrator, singular_administrator, area_administrator) | `AreaPage` | Medium |
| Collaborator Panel (no team) | `/app/:singularSlug/:areaSlug/:usuarioSlug` | Collaborator home panel | Yes | `CollaboratorPage` | Medium |
| Collaborator Panel (with team) | `/app/:singularSlug/:areaSlug/:equipeSlug/:usuarioSlug` | Collaborator panel with team context | Yes | `CollaboratorPage` | Medium |

---

### 4.5 Collaborator Sub-Pages

Base path pattern: `/app/:singularSlug/:areaSlug/:equipeSlug?/:usuarioSlug/...`

| Name | Route suffix | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Collaborator Home | `/home` | Collaborator landing | Yes | `CollaboratorHomePage` | Low |
| Profile | `/perfil` | View user profile | Yes | `ProfilePage` | Medium |
| Profile Edit | `/perfil/editar` | Edit user profile | Yes | `ProfileEditPage` | Medium |
| My Information | `/informacoes` | User information (reuses ProfilePage) | Yes | `ProfilePage` | Medium |
| My Documents | `/documentos` | Personal documents | Yes | `MyDocumentsPage`, `DocumentList` | High |
| My Folders | `/minhas-pastas` | Personal folder tree | Yes | `MyFoldersPage`, `UserFolderTree` | High |
| My Area | `/minha-area` | Collaborator area view | Yes | `CollaboratorAreaPage` | Medium |
| Area Documents | `/documentos-area` | Area-scoped documents | Yes | `CollaboratorAreaDocumentsPage` | High |
| Team Documents | `/documentos-time` | Team documents (admin roles) | Yes | `TeamAdministratorDocumentsPage` | High |
| Singular Documents | `/documentos-singular` | Singular documents | Yes | `SingularAdministratorDocumentsPage` | High |
| My Singular | `/minha-singular` | Organization info | Yes | `CollaboratorOrganizationPage` | Medium |
| Federation | `/federacao` | Federation content | Yes | `FederationPage` | Medium |
| Public Area | `/area-publica` | Public-facing area content | Yes | `PublicAreaPage` | High |
| Stay Informed | `/fique-por-dentro` | News feed | Yes | `StayInformedPage`, `NewsCard` | Medium |
| Stay Informed (all) | `/fique-por-dentro/noticias` | Full news listing | Yes | `StayInformedAllPostsPage` | Medium |
| Stay Informed Post | `/fique-por-dentro/:postSlug` | Single news article | Yes | `StayInformedPostPage` | Medium |

**Note:** Profile edit without team uses route `colaborador.profile.edit.noteam` at `/:singularSlug/:areaSlug/:usuarioSlug/perfil/editar`.

---

### 4.6 Administration

Base path: `/app/administrador/:colaborador_slug/...` (some convidados/documentos routes omit `:colaborador_slug`).

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Admin Redirect | `/app/administrador` | Redirect to user slug | Yes (administrator) | — | Low |
| Admin Dashboard | `.../dashboard` or `.../:colaborador_slug` | Administrator dashboard | Yes (administrator) | `AdminDashboard`, `DsDashboardCard` | Medium |
| Admin Wizard | `.../assistente` | Configuration wizard | Yes (administrator) | `AdminWizardPage`, `DsFormStepper` | High |
| Debug Auth | `.../debug-auth` | Auth debugging (temporary) | Yes (administrator) | `DebugAuthPage` | Low |
| Announcements | `.../comunicados` | Portal announcements | Yes (administrator) | `AnnouncementsPage` | Medium |
| Audit (Singulares) | `.../auditoria` | Organization audit | Yes (administrator) | `OrganizationAudit` | Medium |
| Permission Requests | `.../permissoes` | Access permission requests | Yes (administrator) | `PermissionRequestsPage` | Medium |
| My Team | `.../meu-time` | Team management view | Yes (administrator, area_administrator) | `MyTeamPage` | Medium |
| Team View | `.../equipes/:teamId` | View team details | Yes (administrator, area_administrator) | `TeamViewPage` | Medium |
| Tools | `/app/administrador/ferramentas` | Admin tools | Yes (administrator) | `ToolsPage` | Medium |
| Settings | `/app/administrador/configuracoes` | Portal settings | Yes (administrator) | `SettingsPage` | Medium |
| Components Showcase | `.../componentes-showcase` | DS component demonstration | Yes (administrator, area_administrator) | `ComponentsShowcasePage` | Low |
| Link Area & Sector | `.../estruturas/areas/vincular` | Link area to sector | Yes (administrator) | `VincularAreaSetorPage` | Medium |

#### Singulares (Organizations)

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Singulares Hub | `.../singulares` | Singulares action hub | Yes (administrator) | `OrganizationIndexPage` | Low |
| New Singular | `.../singulares/novo` | Create singular | Yes (administrator) | `OrganizationCreatePage` | Medium |
| Singular List | `.../singulares/lista` | List singulares | Yes (administrator) | `OrganizationList` | Medium |
| Singular Show | `.../singulares/:singular_identifier` | Singular details | Yes (administrator) | `OrganizationShowPage` | Medium |
| Singular Edit | `.../singulares/:singular_identifier/editar` | Edit singular | Yes (administrator) | `OrganizationEditPage` | High |
| Singular Permissions | `.../singulares/:singular_identifier/permissoes` | Singular permissions | Yes (administrator) | `OrganizationPermissions` | Medium |
| Singular Areas | `.../singulares/:singular_identifier/areas` | Area management for singular | Yes (administrator) | `DepartmentManagement` | High |

#### Areas (Departments)

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Areas Hub | `.../areas` | Areas action hub | Yes (administrator) | `DepartmentIndexPage` | Low |
| New Area | `.../areas/novo` | Create area | Yes (administrator) | `DepartmentCreatePage` | High |
| Edit Area | `.../areas/editar/:slug?` | Edit area | Yes (administrator) | `DepartmentCreatePage` | High |
| Area List | `.../areas/lista` | List areas | Yes (administrator) | `AreasListPage` | Medium |
| Area Show | `.../areas/:idOrSlug` | Area details | Yes (administrator) | `DepartmentShowPage` | Medium |
| Area Permissions | `.../areas/:id/permissoes` | Area permissions | Yes (administrator) | `DepartmentPermissionsPage` | Medium |
| Area Settings | `.../areas/settings` | Area settings | Yes (administrator) | `AreaSettingsPage` | Medium |
| Area Teams (legacy) | `.../areas/equipes` | Teams by area | Yes (administrator) | `DepartmentManagement` | Medium |
| New Team (legacy placeholder) | `.../areas/equipes/novo` | New team form | Yes (administrator) | `PlaceholderPage` | Low |
| Edit Team (legacy placeholder) | `.../areas/equipes/editar/:id` | Edit team form | Yes (administrator) | `PlaceholderPage` | Low |

#### Teams (Canonical)

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Teams List | `.../teams/lista` | List teams | Yes (administrator) | `TeamListPage` | Medium |
| New Team | `.../teams/novo` | Create team | Yes (administrator) | `TeamCreatePage` | High |
| Team Details | `.../teams/:idOrSlug` | Team details | Yes (administrator) | `TeamDetailsPage` | Medium |
| Edit Team | `.../teams/editar/:slug` | Edit team | Yes (administrator) | `TeamCreatePage` | High |
| Team Members | `.../teams/:idOrSlug/membros` | Manage team members | Yes (administrator) | `TeamMembersPage` | Medium |
| Team Permissions | `.../teams/:id/permissoes` | Team permissions | Yes (administrator) | `TeamDetailsPage` | Medium |

#### Collaborators

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Collaborators Hub | `.../colaboradores` | Collaborators action hub | Yes (administrator) | `CollaboratorIndexPage` | Low |
| Collaborator List | `.../colaboradores/lista` | List collaborators | Yes (administrator) | `AdminCollaboratorsPage` | Medium |
| Manage Collaborators | `.../colaboradores/gerenciar` | Manage collaborators | Yes (administrator) | `AdminCollaboratorsPage` | Medium |
| New Collaborator | `.../colaboradores/novo` | Create collaborator | Yes (administrator) | `CollaboratorCreatePage` | High |
| Edit Collaborator (placeholder) | `.../colaboradores/:id/editar` | Edit collaborator | Yes (administrator) | `PlaceholderPage` | Low |
| Collaborator Audit (placeholder) | `.../colaboradores/:id/auditoria` | Collaborator audit | Yes (administrator) | `PlaceholderPage` | Low |
| Link Collaborator | `.../colaboradores/vincular` | Link collaborator to area | Yes (administrator) | `CollaboratorLink` | Medium |
| Collaborator Management (by area) | `.../colaboradores/:singular_id/:area_id` | Area collaborator management | Yes (administrator) | `CollaboratorManagement` | High |

#### Users

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Users Hub | `.../usuarios` | User management hub | Yes (administrator) | `UserIndexPage` | Low |
| New User | `.../usuarios/novo` | Create user | Yes (administrator) | `UserCreatePage` | High |
| User List | `.../usuarios/lista` | List users | Yes (administrator) | `UserListPageNew`, `DsUsersTable` | Medium |
| Edit User | `.../usuarios/editar/:username` | Edit user | Yes (administrator) | `UserEditPage`, `UserEditSidebar`, section components | High |
| User Sessions | `.../usuarios/sessoes` | Active sessions | Yes (administrator) | `UserSessionsPage` | Medium |
| User Permissions | `.../usuarios/permissoes` | Roles and permissions | Yes (administrator) | `UserPermissionsPage` | Medium |
| User Reports (placeholder) | `.../usuarios/relatorios` | User reports | Yes (administrator) | `PlaceholderPage` | Low |
| User Audits (placeholder) | `.../usuarios/auditorias` | User audits | Yes (administrator) | `PlaceholderPage` | Low |
| User Settings (placeholder) | `.../usuarios/configuracoes` | User settings | Yes (administrator) | `PlaceholderPage` | Low |

#### Guests (Convidados)

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Guests Hub | `/app/administrador/convidados` | Guests action hub | Yes (administrator) | `InvitedIndexPage` | Low |
| New Guest | `.../convidados/novo` | Register guest | Yes (administrator) | `CadastrarConvidadosPage` | Medium |
| Guest List | `.../convidados/lista` | List guests | Yes (administrator) | `InvitedIndexPage` | Medium |
| Guest Documents (placeholder) | `.../convidados/documentos` | Guest documents | Yes (administrator) | `PlaceholderPage` | Low |
| Manage Guests (placeholder) | `.../convidados/gerenciar` | Manage guests | Yes (administrator) | `PlaceholderPage` | Low |
| Guest Reports (placeholder) | `.../convidados/relatorios` | Guest reports | Yes (administrator) | `PlaceholderPage` | Low |
| Guest Audits (placeholder) | `.../convidados/auditorias` | Guest audits | Yes (administrator) | `PlaceholderPage` | Low |
| Guest Settings (placeholder) | `.../convidados/configuracoes` | Guest settings | Yes (administrator) | `PlaceholderPage` | Low |

#### Documents (Admin)

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Documents Hub | `/app/administrador/documentos` | Documents action hub | Yes (administrator) | `DocumentIndexPage` | Low |
| Admin Documents (slug) | `.../:colaborador_slug/documentos` | Shared documents (admin context) | Yes (administrator, area_administrator) | `DocumentsPage` | High |
| Folder Management | `.../:colaborador_slug/pastas` | Folder structure management | Yes (administrator, area_administrator) | `FoldersManagementPage`, `FolderList` | High |
| Public Documents | `.../documentos/publicos` | Public document listing | Yes (administrator) | `PublicDocuments` | Medium |
| Private Documents | `.../documentos/privados` | Private document listing | Yes (administrator) | `PrivateDocuments` | Medium |
| New Document (placeholder) | `.../documentos/novo` | Create document | Yes (administrator) | `PlaceholderPage` | Low |
| Document List (placeholder) | `.../documentos/lista` | Document list | Yes (administrator) | `PlaceholderPage` | Low |
| Filtered Docs (placeholder) | `.../documentos/filtrados` | Filtered documents | Yes (administrator) | `PlaceholderPage` | Low |
| Manage Docs (placeholder) | `.../documentos/gerenciar` | Manage documents | Yes (administrator) | `PlaceholderPage` | Low |
| Doc Reports (placeholder) | `.../documentos/relatorios` | Document reports | Yes (administrator) | `PlaceholderPage` | Low |
| Doc Audits (placeholder) | `.../documentos/auditorias` | Document audits | Yes (administrator) | `PlaceholderPage` | Low |
| Doc Settings (placeholder) | `.../documentos/configuracoes` | Document settings | Yes (administrator) | `PlaceholderPage` | Low |

#### Permission Shortcuts (placeholders / redirects)

| Name | Route | Purpose | Auth | Main Components | Complexity |
|---|---|---|---|---|---|
| Singular Permissions | `.../permissoes/singulares` | Singular permissions shortcut | Yes (administrator) | `PlaceholderPage` | Low |
| Area Permissions Shortcut | `.../permissoes/areas` | Area permissions shortcut | Yes (administrator) | `PlaceholderPage` | Low |
| Collaborator Permissions | `.../permissoes/colaboradores` | Redirect to collaborator manage | Yes (administrator) | — | Low |
| Guest Permissions | `.../permissoes/convidados` | Redirect to guest manage | Yes (administrator) | — | Low |

---

### 4.7 Pages Without Active Routes

The following page files exist under `pages/` but are not referenced in `routes.ts`:

| File | Notes |
|---|---|
| `DashboardPage.vue` | Comment in routes: dashboard route removed |
| `pages/OnboardingPage.vue` (root) | Duplicate; active route uses `pages/onboarding/OnboardingPage.vue` |
| `guest/GuestHomePage.vue` | No route found |
| `documents/DocumentsWithFoldersPage.vue` | No route found |
| `collaborator/CollaboratorListPage.vue` | No route found |
| `collaborator/FolderTreePage.vue` | No route found |
| `collaborator/MySingularPage.vue` | No route found |
| `public/HomePage.vue` | No route found |
| `admin/AuditPage.vue` | Commented out in routes; replaced by `OrganizationAudit.vue` |
| `admin/UserPage.vue` | Legacy; canonical routes use `UserIndexPage` / `UserEditPage` |
| `admin/users/AdminUsersPage.vue` | No route found |
| `admin/areas/DepartmentCreatePage.old.vue` | Archived copy |

---

## 5. Navigation

### Layouts

| Layout | Usage |
|---|---|
| `AuthLayout.vue` | Login page wrapper |
| `MainLayout.vue` | Primary authenticated shell: header, sidebar, footer, router-view |
| `AdminLayout.vue` | Exists; admin-specific layout variant |
| `PublicLayout.vue` | Public pages layout |

### Navigation Flow

```
/  →  /login
/login  →  (auth success)  →  role selection (if multiple)  →  /app/... (home by role)
/app  →  /app/administrador/:colaborador_slug/dashboard (default redirect)
Unauthenticated access to protected route  →  /login?redirect=...
User without area (non-admin)  →  /no-area-access
User needs onboarding  →  /app/onboarding
/logout  →  clear session  →  /login?switch=1
```

### Menus

Sidebar menus are **role-conditional** in `AppSidebar.vue`, rendering one of four menu components:

| Menu Component | Visible when |
|---|---|
| `SidebarCollaboratorMenu` | `activeRole === collaborator` (and not higher admin roles) |
| `SidebarAreaAdminMenu` | `activeRole === area_administrator` |
| `SidebarSingularAdminMenu` | `activeRole === singular_administrator` |
| `SidebarAdministratorMenu` | `activeRole === administrator` |

**Administrator menu items:** Dashboard, Singulares, Áreas, Colaboradores, Convidados, Pastas (expansion: Todos os Documentos, Estrutura de Pastas), Comunicados, Auditoria.

**Collaborator menu items:** Profile card, Página Inicial (Fique por Dentro), Federação (expandable area list with search), Singulares (expandable), Minha Singular, Minha Área, Meus Documentos, Minhas Pastas, Área Pública, Sistemas e Serviços (dialog).

**Area admin and singular admin menus** share similar structure with federation/areas/singulares expansions and area-admin-specific actions (create team, create folder).

### Breadcrumbs

- Route `meta.breadcrumb` defined per route in `routes.ts`
- `DsBreadcrumbs` molecule component
- `DsPageHeader` organism accepts breadcrumb props
- Icons mapped via `BreadcrumbIcons` in `constants/routes`

### Sidebars

- Desktop: `AppSidebar` in 3-column layout (`gt-sm`), toggleable via `AppHeader`
- Mobile: sidebar hidden on small screens (`v-show="leftDrawerOpen"`, `gt-sm` class); header toggle opens drawer

### Topbars

- `AppHeader`: search, sidebar toggle, logout, accessibility dialog, role/context display

### Footer

- `AppFooter`: accessibility features including Libras (`libras-enabled="true"`)

### Navigation Tree (ASCII)

```
Portal de Comunicação
├── Público
│   ├── Login
│   ├── Logout
│   └── 404
├── Autenticado (/app)
│   ├── Onboarding
│   ├── Documentos (compartilhados)
│   ├── Colaboração
│   ├── Notificações
│   ├── Painéis dinâmicos
│   │   ├── Singular /:singularSlug
│   │   ├── Área /:singularSlug/:areaSlug
│   │   └── Colaborador /:singularSlug/:areaSlug/[:equipeSlug/]:usuarioSlug
│   │       ├── Home
│   │       ├── Perfil / Editar
│   │       ├── Documentos / Pastas
│   │       ├── Minha Área / Área Pública
│   │       ├── Federação
│   │       └── Fique por Dentro (+ notícias)
│   └── Administração (/app/administrador/:colaborador_slug)
│       ├── Dashboard
│       ├── Singulares (hub, lista, novo, show, edit, permissões, áreas)
│       ├── Áreas (hub, lista, novo, edit, show, permissões, settings)
│       ├── Equipes (lista, novo, show, edit, membros, permissões)
│       ├── Colaboradores (hub, lista, novo, vincular, gerenciar)
│       ├── Usuários (hub, lista, novo, edit, sessões, permissões)
│       ├── Convidados (hub, lista, novo)
│       ├── Documentos / Pastas
│       ├── Comunicados
│       ├── Auditoria
│       ├── Permissões (solicitações)
│       ├── Onboarding (solicitações)
│       ├── Assistente (wizard)
│       ├── Ferramentas
│       └── Configurações
└── Legado: /administrador/* → redirect /app/administrador/*
```

---

## 6. Authentication

### Login Flow

1. User enters institutional email and password on `LoginPage`
2. Email validated (format + Zimbra domain rules)
3. Login via `useAuth` → `auth-core` store → `PortalApiService.login()` (JWT from WordPress REST API)
4. Token stored in `localStorage` (key from `VITE_TOKEN_STORAGE_KEY` or `getStorageKey('token')`)
5. User profile built from JWT response (roles, capabilities, context)
6. If multiple roles: `RoleSelector` modal for active role selection
7. If multiple domains/organizations: domain selection modal
8. Post-login navigation via `post-login-navigation` utilities and `auth-context` store (`getHomeRoute()`)

**Alternate path:** `AuthService.loginWithEmailGate()` — IMAP/SMTP validation via backend before JWT issuance.

### Logout

- Route `/logout` renders `LogoutPage`
- Calls `useAuth().logout({ hard: true })`
- Clears Pinia auth stores, localStorage, cookies (`reliableLogoutService`, `cookie-utils`)
- BroadcastChannel `portal_auth` notifies other tabs
- Redirect to `/login?switch=1` after delay

### Session Validation

- On app boot: `boot-auth` hydrates token/user from localStorage
- `auth-core.validateSession()` / profile fetch validates token with API
- 401 responses trigger session cleanup or silent refresh attempt

### Token Storage

- **Primary:** `localStorage` — JWT token and serialized user object
- **Keys:** Environment-driven via `getStorageKey()` (`VITE_TOKEN_STORAGE_KEY`, user key)
- **Headers:** `Authorization: Bearer <token>`, `X-Active-Role`, `X-User-Area-Id`, `X-User-Team-Id`, `X-Request-Id`
- **Cookies:** Not used for auth (`withCredentials: false`); cookies cleared on logout

### Session Expiration

- Axios response interceptor handles 401/403 → user notification and redirect to login
- `useStandardErrorHandling` maps 401 to "Sua sessão expirou. Faça login novamente."
- `auth-core` attempts token refresh on 401 during hydration before clearing session

### Refresh

- `auth-core` contains refresh logic calling API on 401 during session hydration
- No dedicated refresh-token rotation library; `refresh_token` referenced in cookie cleanup utilities

### Forgot Password

- Link on login: "Verifique sua senha do e-mail"
- `handleForgotPassword()` opens Zimbra webmail URL in new tab (domain-specific: unimedceara.com.br, unimedcariri.com.br, unimedsobral.com.br, etc.)
- No in-app password reset form

### Redirects

| Condition | Destination |
|---|---|
| Unauthenticated + protected route | `/login?redirect=<path>` |
| Guest on login while authenticated | Home route by role (unless `?switch=1`) |
| No area linked (non-admin) | `/no-area-access` |
| Needs onboarding | `/app/onboarding` |
| Legacy `/administrador/*` | `/app/administrador/*` |
| Root `/` | `/login` |
| `/app` | `admin.dashboard` |

---

## 7. Authorization

### Roles

Defined in `types/roles.ts`:

| Role | Value |
|---|---|
| Administrator | `administrator` |
| Singular Administrator | `singular_administrator` |
| Area Administrator | `area_administrator` |
| Team Administrator | `team_administrator` |
| Collaborator | `collaborator` |
| Visitor | `visitor` |

Each role has associated permissions (e.g., `portal_manage_all`, `portal_view_documents`) and optional `routePrefix`.

### Profiles

- User profile stored in auth store (`UserProfile` type) with `roles`, `capabilities`, `context` (singular_id, area_id, slugs, team_slug, colaborador_slug)
- Active role selectable when user has multiple roles (`portal_active_role` in localStorage)

### Permissions

- Permission strings checked via `useAuth().hasPermission()`, `hasAllPermissions()`, `hasAnyPermission()`
- Route meta may include `roles` and `capabilities` arrays
- `ROLE_DEFINITIONS` maps roles to permission arrays

### Menu Visibility

- Sidebar menus gated by `hasActiveRole()` in each `Sidebar*Menu` component
- Only one primary role menu shown at a time based on `activeRole`

### Route Guards

| Guard | File | Behavior |
|---|---|---|
| `authPermissionGuard` | `router/guards.ts` | Global guard: public routes pass; unauthenticated → login; no area → no-area-access; **authenticated users currently allowed through (permissive)** |
| `rbacGuard` | `router/guards/rbac.ts` | Full RBAC check (guestOnly, adminOnly, roles, capabilities) — defined but global guard in use is `authPermissionGuard` |
| Onboarding guard | `router/index.ts` | Redirects users without singular/area context to onboarding |
| Alias resolver | `router/index.ts` | Resolves route name aliases to canonical names |
| `canonicalRedirectGuard` | `router/middleware/canonical-redirect.ts` | Canonical URL redirects |
| `conditionalImport` | `utils/lazy-loading.ts` | Lazy-loads components based on user roles |

### Component Guards

- `v-if="hasActiveRole(...)"` patterns in sidebar and admin components
- `usePermissions`, `useAreaPermissions` composables for feature-level checks
- `useAuth().isAdmin`, `isCollaborator`, etc. computed properties

---

## 8. Forms

### Forms Inventory (by domain)

| Domain | Forms / Pages |
|---|---|
| Login | Email, password, domain selection, role selection |
| User management | `UserCreatePage`, `UserEditPage` (identification, roles, preferences sections) |
| Organization | `OrganizationCreatePage`, `OrganizationEditPage`, `OrganizationBasicInfoSection`, `OrganizationCompanyInfoSection` |
| Areas | `DepartmentCreatePage`, `EditAreaDialog`, `AreaSettingsPage` |
| Collaborators | `CollaboratorCreatePage`, `CollaboratorLinkForm` |
| Teams | `TeamCreatePage`, `TeamCreateDialog` |
| Profile | `ProfileEditPage`, `ProfilePage` |
| Documents | `DsDocumentMetadataForm`, upload dialogs |
| Folders | `DsCreateFolderDialog`, `DsCreateAreaFolderDialog`, `DsCreateTeamFolderDialog`, `RenameFolderDialog` |
| Contacts | `ContactsManager`, `ContactFormItem`, `AddressForm` |
| Guests | `CadastrarConvidadosPage` |
| Onboarding | `OnboardingPage`, `OnboardingRequestDialog` |
| Permissions | `OrganizationPermissions`, `DepartmentPermissionsPage`, `UserPermissionsPage` |

### Validation

- **Quasar rules:** Inline `:rules` on `q-input` / `q-form` (required, email format)
- **`useFormValidation` composable:** `requiredRule`, `emailRule`, `slugRule`, `cpfRule`, `cnpjRule`, `phoneRule`, min/max length
- **`useUserValidation`:** User-specific validation
- **`useEntityForm`:** Entity form orchestration
- **Zimbra email validation:** `validateZimbraEmail` in RBAC constants and `ZimbraValidationService`

### Masks

Quasar `mask` prop used on inputs (no external mask library):

| Mask pattern | Usage |
|---|---|
| `(##) #####-####` | Mobile phone |
| `(##) ####-####` | Landline phone |
| `#####-###` | CEP (postal code) |
| `##.###.###/####-##` | CNPJ |
| `##/##/####` | Date |
| `+55 ## #####-####` | International phone |

### Uploads

- `DsFileUpload` organism
- `useFileUpload`, `useDocumentUpload` composables
- `UploadsService`, `documentosService` upload endpoints
- Drag-and-drop: `useDragAndDrop` composable

### Editors

- `DsRichTextEditor` — rich text in forms
- `DsSqlEditor` — SQL editor (admin tools context)

### Autocomplete

- `DsUserAutocomplete`, `DsUserSearch`, `AdminUserSearchField`
- `useUserSearch` composable

### Dynamic Fields

- `ContactsManager` — dynamic contact list (add/remove contacts by type)
- `EntityFormPage` — generic entity form driven by configuration
- `entity-form-configs.ts` — form field definitions per entity type
- `DsFormStepper` — multi-step wizard forms

---

## 9. CRUD Screens

| Entity | Listing | Filters | Pagination | Sorting | Create | Edit | Delete | Export | Import |
|---|---|---|---|---|---|---|---|---|---|
| **Singulares** | `OrganizationList` | Yes (via services) | API-driven | — | `OrganizationCreatePage` | `OrganizationEditPage` | In list/show | — | — |
| **Áreas** | `AreasListPage` | Yes | API-driven | — | `DepartmentCreatePage` | `DepartmentCreatePage` (edit mode) | — | — | — |
| **Equipes** | `TeamListPage` | Yes | API-driven | — | `TeamCreatePage` | `TeamCreatePage` (edit) | — | — | — |
| **Colaboradores** | `AdminCollaboratorsPage` | Yes | API-driven | — | `CollaboratorCreatePage` | Placeholder | — | — | — |
| **Usuários** | `UserListPageNew` | Yes (`useFilters`) | `DsDataTable` / API | Column sort | `UserCreatePage` | `UserEditPage` | In table actions | — | — |
| **Convidados** | `InvitedIndexPage` | — | — | — | `CadastrarConvidadosPage` | — | — | — | — |
| **Documentos** | `DocumentsPage`, `MyDocumentsPage`, `PublicDocuments`, `PrivateDocuments` | Yes (type, visibility, folder) | API pagination | — | Upload dialogs | Metadata edit | `DocumentActions`, bulk delete | Download via API | Upload only |
| **Pastas** | `FoldersManagementPage`, `FolderList` | — | — | — | Create folder dialogs | `RenameFolderDialog` | Folder delete | — | — |
| **Comunicados** | `AnnouncementsPage` | — | — | — | In page | In page | — | — | — |
| **Notícias** | `StayInformedPage`, `StayInformedAllPostsPage` | — | Infinite scroll | — | CMS-driven | — | — | — | — |

**Note:** Several admin hub routes point to `PlaceholderPage.vue` for create/list/manage/report actions not yet implemented.

---

## 10. Shared Components

### Design System (`components/ds/`)

**Atoms (10):** `DsAvatar`, `DsBackButton`, `DsBadge`, `DsButton`, `DsIcon`, `DsInput`, `DsProgressBar`, `DsSelect`, `DsSidebarProfile`, `DsSidebarMenuItem`

**Molecules (27):** `DsActionButtons`, `DsAssigneeChip`, `DsBreadcrumbs`, `DsCard`, `DsCommentItem`, `DsDeadlineInfo`, `DsDocumentCard`, `DsFolderCardSkeleton`, `DsGrid`, `DsLazyImage`, `DsLinkSummaryCard`, `DsRichTextEditor`, `DsRoleChip`, `DsSearchInput`, `DsStatusBadge`, `DsStatusSelect`, `DsTabPanel`, `DsUserAutocomplete`, `DsUserAvatar`, `DsUserSelector`, `DsUserBindingCard`, `DsUserCard`, `DsUserInfoCard`, `DsUserPermissionCard`, `DsUserSearch`, `DsViewModeToggle`, `DsVisibilityBanner`, `DsWorkflowProgress`, `UserCard`

**Organisms (27+):** `DsAreaFoldersManagementDialog`, `DsCardGrid`, `DsCreateFolderDialog`, `DsCreateAreaFolderDialog`, `DsCreateTeamFolderDialog`, `DsDashboardCard`, `DsDataTable`, `DsDetailsDialog`, `DsDocumentMetadataForm`, `DsDocumentGalleryCard`, `DsDocumentGalleryView`, `DsDrawer`, `DsFileUpload`, `DsFormCard`, `DsFormDialog`, `DsFormStepper`, `DsInfoDialog`, `DsListCard`, `DsModal`, `DsNavigationTree`, `DsPageHeader`, `DsRoleHierarchyCard`, `DsSqlEditor`, `DsUsersTable`, `DsStatsCard`, `DsStickyToolbar`, `DsTable`, `DsWorkflowCard`, `DsWorkflowHistoryItem`, `DsWorkflowStepper`, `InfiniteScrollList`, `PortalToast`, `TeamCreateDialog`, `NewsCard`, `AreaDocumentsToolbar`, `AreaFolderTreeSidebar`, `PublicAreaToolbar`

### Domain Components (non-DS)

| Category | Examples |
|---|---|
| App shell | `AppHeader`, `AppSidebar`, `AppFooter` |
| Documents | `DocumentList`, `DocumentCard`, `DocumentActions`, `DocumentMetadata`, `DocumentFolderBreadcrumb` |
| Folders | `FolderList`, `UserFolderTree`, `FolderPermissionManager`, `FolderStatsCard` |
| Search | `GlobalSearchResults` |
| Auth | `RoleSelector`, `NoAreaAccessNotice` |
| Admin | `ActionCard`, `BulkDeleteDocuments`, `CollaboratorLinkForm`, `OrganizationBasicInfoSection` |
| Collaboration | `CommentsSection`, `CollaboratorShareDialog` |
| Shared | `OnboardingBanner`, `OnboardingRequestDialog`, `DocumentCard` |
| Sidebar | `SidebarCollaboratorMenu`, `SidebarAdministratorMenu`, `ConfigDialog`, `UserInfoDialog` |

---

## 11. Layout Components

| Component | Location | Role |
|---|---|---|
| `AppHeader` | `components/app/AppHeader.vue` | Top bar: logo, search, menu toggle, user menu, logout |
| `AppSidebar` | `components/app/AppSidebar.vue` | Role-based navigation sidebar |
| `AppFooter` | `components/app/AppFooter.vue` | Footer with accessibility (Libras) |
| `SidebarFooter` | `components/sidebar/SidebarFooter.vue` | Sidebar bottom section |
| `DsPageHeader` | DS organisms | Page title + breadcrumbs + actions |
| `DsStickyToolbar` | DS organisms | Sticky action toolbar |
| `DsDashboardCard` | DS organisms | Dashboard metric cards |
| `DsStatsCard` | DS organisms | Statistics display |
| `DsCard` / `DsCardGrid` | DS | Card layouts |
| `AreaFolderTreeSidebar` | DS organisms | Folder tree sidebar for documents |
| `OnboardingBanner` | `components/shared/` | Onboarding prompt banner |

---

## 12. Theme

### Primary Colors

- `--color-primary: #007B5E` (Unimed green)
- `--color-primary-light: #00A075`
- `--color-primary-dark: #005A43`

### Secondary Colors

- `--color-secondary: #F5F5F5`
- `--color-secondary-dark: #E0E0E0`

### Semantic Colors

- Success `#2E7D32`, Warning `#EF6C00`, Error `#C62828`, Info `#1565C0`

### Typography

- **Primary font (tokens):** Inter with system fallbacks
- **Corporate fonts (fonts.css):** Unimed Sans, Unimed Serif, Unimed Slab, Unimed Brush
- **Scale:** `--text-xs` (12px) through `--text-5xl` (48px)
- **Weights:** 300–900

### Spacing

- 8px-based scale: `--spacing-xs` (4px) through `--spacing-5xl` (64px)
- Component and section semantic spacing variables

### Border Radius

- Defined in `design-tokens.scss` (radius tokens for cards, buttons, inputs)

### Shadows

- Elevation tokens in design-tokens (card, modal, dropdown levels)

### Icons

- MDI v7 via Quasar (`mdi-*`, `mdi-v7` icon set)
- `design-tokens/icons.tokens.ts` for icon name constants
- `useDesignSystemIcons` composable

### Dark Mode

- Supported via `useTheme` composable and `useUiStore`
- Modes: `light`, `dark`, `auto` (follows `prefers-color-scheme`)
- Applied via `data-theme` attribute on `<html>`
- Toggle available in sidebar `ConfigDialog`

### Themes

- Single brand theme (Unimed) with light/dark variants
- Quasar variables in `quasar.variables.scss` aligned with CSS custom properties

---

## 13. Responsive Behavior

### Breakpoints (`_responsive.scss`)

| Name | Min width |
|---|---:|
| xs | 0 |
| sm | 600px |
| md | 960px |
| lg | 1280px |
| xl | 1440px |
| xxl | 1920px |

### Mobile (< 600px)

- Sidebar hidden by default (`gt-sm`); toggled via header
- Full-width content (`col-12`)
- Quasar responsive grid classes on layouts

### Tablet (600px – 959px)

- Partial grid: `col-sm-3` sidebar, `col-sm-8` content when open
- `col-md-11` container width

### Desktop (≥ 960px)

- Sidebar visible in 3-column layout
- Centered content area (`col-lg-10 col-xl-10` wrapper)
- Content narrows when sidebar closed (`col-md-8 col-lg-6` centered)

### Responsive Menus

- Sidebar collapses on mobile; drawer toggle in header
- Expansion items in sidebar for nested navigation
- Area/singular lists with search filters in sidebar

### Responsive Tables

- `DsDataTable`, `DsTable` with horizontal scroll patterns
- Quasar `q-table` responsive props in admin lists
- Card/grid view toggle via `DsViewModeToggle` on some document views

---

## 14. API Consumption

### Base URL

Configured via environment variables:

```
VITE_CMS_BASE_URL          → WordPress CMS host
VITE_WP_JSON_PREFIX        → /wp-json (default)
VITE_API_NAMESPACE         → portaldecomunicacao/v1 (default)
PORTAL_API_BASE            → {CMS}/wp-json/{namespace}
```

Dev proxy: `/cms/wp-json` → CMS container.

### Services

| Service | Responsibility |
|---|---|
| `PortalApiService` | Primary API: auth, users, singulares, areas, documents, config |
| `AuthService` | WordPress JWT + email gate login |
| `singulares.service` | Singular CRUD |
| `AreasService` | Area CRUD |
| `CollaboratorsService` | Collaborator operations |
| `users.service` | User management |
| `documentosService` | Document operations |
| `FoldersService` | Folder tree CRUD |
| `UploadsService` | File uploads |
| `DocumentSharingService` | Document sharing |
| `TeamsService` | Team management |
| `DirectoryService` | Directory lookups |
| `GlobalSearchService` | Global search |
| `NavigationContextService` | Navigation context resolution |
| `PermissionRequestService` | Permission requests |
| `ZimbraValidationService` | Email validation |
| `reliableLogoutService` | Logout orchestration |
| `BaseEntityService` | Base class for entity services |

### HTTP Client

- Two Axios instances: `api` (portal namespace), `cmsApi` (full WP REST root)
- 30s timeout (`api`), 15s (`cmsApi`)
- Request interceptors: Bearer token, `X-Active-Role`, `X-User-Area-Id`, `X-User-Team-Id`, `X-Request-Id`
- Dev cache-buster query param `_cb`

### Authentication

- JWT in `Authorization: Bearer` header
- No cookie-based session

### Error Handling

- `useStandardErrorHandling` composable
- `error-normalizer.ts` — user-friendly Portuguese messages
- `errorHandler.ts` — status code mapping
- Quasar `Notify` / `usePortalToast` for user feedback
- 401 → session expired message + redirect to login

### Retries

- No global Axios retry interceptor documented
- Manual retry patterns in specific composables

### Uploads

- Multipart via `UploadsService` / `PortalApiService` document upload endpoints
- `DsFileUpload` component with progress
- Download via signed URLs / download tokens (`download_token`, `token_expires_at`)

### Downloads

- `documentosService` blob download via `cmsApi`
- `PortalApiService` download URL generation

---

## 15. External Integrations

| Integration | Usage |
|---|---|
| **WordPress CMS** | Primary backend via REST API (`portaldecomunicacao/v1`); content, users, taxonomies |
| **JWT Auth (WordPress plugin)** | Authentication tokens |
| **Zimbra Email** | Institutional email validation; password recovery redirects to Zimbra webmail per domain |
| **Email Gate (IMAP/SMTP)** | Optional login path via `AuthService.loginWithEmailGate()` |
| **VLibras / Libras** | Accessibility in `AppFooter` (`libras-enabled`) |
| **BroadcastChannel API** | Cross-tab logout synchronization |
| **Analytics** | `stores/analytics.ts`, `useEventTracking` — route to analytics dashboard exists but is **disabled/commented** in routes |

---

## 16. Assets

### Icons

- Material Design Icons (Quasar `@quasar/extras`, icon set `mdi-v7`)
- Icon tokens: `design-tokens/icons.tokens.ts`

### Images

- `public/logo-unimed-ceara.svg` — Unimed Ceará logo
- `public/images/` — placeholder README; login references `/images/image.png` (not committed)
- `DsLazyImage` for lazy-loaded images

### SVG

- Logo SVG in `public/`
- Inline SVG patterns in login page decorative elements

### Fonts

- Referenced in `src/css/fonts.css`: Unimed Sans/Serif/Slab/Brush (`.otf` files expected at `/fonts/`)
- Font files **not committed** in repository under `public/fonts/`

### Logos

- Unimed Ceará logo on login page and header
- Domain-specific branding via login layout

---

## 17. Design Patterns

| Pattern | Occurrences |
|---|---|
| **CRUD Hub** | Admin index pages (Singulares, Áreas, Colaboradores, Usuários, Documentos, Convidados) with action cards linking to list/create/manage |
| **CRUD List + Form** | User list → edit; Organization list → show → edit |
| **Wizard / Stepper** | `AdminWizardPage`, `DsFormStepper`, onboarding flow |
| **Dashboard** | `AdminDashboard` with `DsDashboardCard` / `DsStatsCard` |
| **Card Grid** | Document gallery, news cards, action cards |
| **Master-Detail** | Organization show → edit/permissions; Team details → members |
| **Tree Navigation** | Folder trees (`UserFolderTree`, `AreaFolderTreeSidebar`, `DsNavigationTree`) |
| **Infinite Scroll** | `InfiniteScrollList`, news listings |
| **Search** | Global search in header, `GlobalSearchResults`, sidebar area search |
| **Details Dialog** | `DsDetailsDialog`, `DsInfoDialog` |
| **Timeline / Workflow** | `DsWorkflowStepper`, `DsWorkflowHistoryItem`, `DsWorkflowCard` |
| **Comments / Collaboration** | `CommentsSection`, `CollaborationPage` |
| **Placeholder** | `PlaceholderPage` for unimplemented admin sub-routes |
| **Role-conditional UI** | Sidebar menus, `conditionalImport` route loading |

---

## 18. User Flows

### Login

Email/password → API JWT → store token → (domain select) → (role select) → redirect to home route by role.

### Read News (Fique por Dentro)

Sidebar → Fique por Dentro → news cards → post detail (`StayInformedPostPage`).

### Upload Document

Navigate to documents area → upload button / `DsFileUpload` → metadata form → API upload → gallery/list refresh.

### Search

Header search input → `GlobalSearchService` → `GlobalSearchResults` display.

### Administration

Admin login → Dashboard → hub pages (Singulares/Áreas/Usuários) → list → create/edit → permissions/audit as needed.

### Onboarding

New user without area → onboarding guard → `OnboardingPage` → request created → admin approves in `OnboardingRequestsPage`.

### Document Management (Folders)

Admin → Pastas → `FoldersManagementPage` → create/rename/organize folders → assign permissions → link documents.

### Profile Management

Sidebar profile card → profile view/edit → update contacts, phone masks, save via API.

### Logout

User menu / route → `LogoutPage` → clear all storage → redirect login.

---

## 19. Screens Recommended for MVP

Identification only — screens that appear essential based on core portal purpose and active (non-placeholder) implementations:

| Screen | Rationale |
|---|---|
| Login | Entry point |
| Logout | Session termination |
| No Area Access | Users pending area assignment |
| Onboarding | First-access flow |
| Admin Dashboard | Default authenticated landing |
| Fique por Dentro (+ post detail) | Core communication/news |
| My Documents / Area Documents | Core document access |
| My Folders / Folder Management | Document organization |
| Documents (shared) | Cross-user document sharing |
| Profile / Profile Edit | User identity |
| Collaborator Home | Role landing |
| Singulares List/Create/Edit/Show | Organizational structure |
| Areas List/Create/Edit/Show | Department structure |
| Users List/Create/Edit | User administration |
| Teams List/Create/Members | Team structure |
| Collaborators List/Create | Workforce management |
| Permission Requests / Onboarding Requests | Access governance |
| Notification Center | User alerts |
| Public Area | Published content consumption |
| Announcements (Comunicados) | Official communications |

**Placeholder screens** (hub stubs for reports, audits, settings sub-sections) are present in routing but render `PlaceholderPage.vue`.

---

## 20. Appendix

### Project Statistics

| Item | Count |
|---|---:|
| Pages (`pages/**/*.vue`) | 96 |
| Components (`components/**/*.vue`) | 193 |
| Layouts | 4 |
| Route path definitions | 100 |
| Named routes | 100 |
| Redirect-only routes | 13 |
| Services (`.ts` files) | 18 |
| Composables (`.ts` files) | 67 |
| Pinia store modules | 11 |
| Page modules (top-level `pages/` folders) | 13 |
| Component domain folders | 19 |
| DS components (exported) | ~54 |
| i18n locales | 2 |
| Boot files | 8 |
| CSS/SCSS files | ~30 |
| Type definition files | ~24 |

### Page Modules

`admin`, `areas`, `collaboration-hub`, `collaborator`, `documents`, `guest`, `notifications`, `onboarding`, `public`, `shared`, `singular`, `singular-admin`, `team-admin`

### Key Configuration Files

| File | Purpose |
|---|---|
| `quasar.config.ts` | Build, dev server, boot files, env loading |
| `frontend/package.json` | Dependencies and scripts |
| `src/router/routes.ts` | All route definitions (~1810 lines) |
| `src/constants/routes.ts` | Route names, paths, meta defaults |
| `src/types/roles.ts` | Role and permission definitions |
| `src/boot/axios.ts` | API client configuration |
| `src/css/tokens/design-tokens.scss` | Design token source of truth |

### Environment Variables (VITE_*)

Loaded from `envs/{local|prod}/.env` and optional `secrets.env`; exposed via `import.meta.env`:

- `VITE_CMS_BASE_URL`
- `VITE_WP_JSON_PREFIX`
- `VITE_API_NAMESPACE`
- `VITE_TOKEN_STORAGE_KEY`
- `VITE_SHOW_AXIOS_LOGS`
- `VITE_LOG_LEVEL`
- `VITE_CMS_TIMEOUT_MS`

---

*Report generated from repository evidence only. No source code was modified.*
