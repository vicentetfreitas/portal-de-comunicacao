# Discovery — Current Modules

## Objetivo

Mapear os módulos funcionais existentes atualmente no Portal de Comunicação, com base exclusiva em evidências do código-fonte (CMS WordPress, frontend Quasar e backend PHP legado).

**Nível de confiança da descoberta:** Alto para módulos com Controller + Service + frontend; Médio para módulos `PARCIAL` (evidência apenas em um lado da stack); Baixo para itens `LEGADO` ou endpoints referenciados sem implementação localizada.

---

## Resumo Executivo

| Módulo | Responsabilidade | Status |
|---|---|---|
| Autenticação e Sessão | Login, logout, JWT, contexto de sessão e integração Zimbra | ATIVO |
| Usuários | CRUD, provisionamento, perfil, busca e vínculo organizacional | ATIVO |
| RBAC | Papéis, capabilities, auditoria e reset de roles canônicos | ATIVO |
| Singulares | Gestão de unidades singulares (organizações) | ATIVO |
| Áreas | Gestão hierárquica de áreas departamentais | ATIVO |
| Equipes | Gestão de times vinculados a áreas | ATIVO |
| Colaboradores | Gestão e visualização de colaboradores por área/singular | ATIVO |
| Documentos | Upload, listagem, download, permissões e compartilhamento | ATIVO |
| Pastas e Diretórios | Árvore hierárquica de pastas e criação automática | ATIVO |
| Permissões de Pastas | Regras granulares de acesso por hierarquia de pasta | ATIVO |
| Solicitação de Permissões | Fluxo de pedido/aprovação de acesso a recursos privados | PARCIAL |
| Notificações | Listagem, leitura, SSE e configurações de notificações | ATIVO |
| Onboarding | Seleção inicial de singular/área para novos usuários | PARCIAL |
| Auditoria | Registro e consulta de eventos de auditoria | ATIVO |
| Armazenamento e Upload | Quotas, validação de upload e gestão de uso | ATIVO |
| Configuração do Portal | Configurações públicas, ACF e metadados do portal | ATIVO |
| API — Saúde e Utilitários | Ping, status, versão e configurações públicas | ATIVO |
| Diagnóstico | Endpoints de diagnóstico e correção de upload | ATIVO |
| Busca Global | Busca unificada em documentos, áreas, singulares e colaboradores | PARCIAL |
| Analytics | Dashboard de métricas administrativas | PARCIAL |
| Comunicados | Gestão de comunicados corporativos (admin) | PARCIAL |
| Fique por Dentro | Feed de notícias/publicações para colaboradores | PARCIAL |
| Convidados | Hub e rotas de gestão de usuários convidados | PARCIAL |
| Central de Colaboração | Hub de colaboração com comentários e atividades | PARCIAL |
| Navegação e Interface | Contexto de navegação, UI global e ações rápidas | ATIVO |
| Cache | Cache via WordPress Transients com invalidação por tags | ATIVO |
| Backend PHP Legado | Rotas HTTP de auth, documentos e pastas (serviço separado) | LEGADO |

---

## Módulo: Autenticação e Sessão

### Responsabilidade

Autenticar usuários via Zimbra e WordPress, emitir/validar JWT e expor contexto de sessão (`/auth/me`, `/auth/context`).

### Evidências

#### Controllers

- `AuthController`

#### Services

- Não há Service dedicado; lógica em classes Auth

#### Auth / Middleware

- `JWT`, `ZimbraAuth`, `Session`, `BackendSync`
- `AuthMiddleware`

#### Frontend

- `LoginPage`, `LogoutPage`, `NoAreaAccessPage`, `DebugAuthPage`
- Stores: `auth-core`, `auth-roles`, `auth-context`
- Services: `AuthService`, `reliableLogoutService`

### Dependências

WordPress, Zimbra (IMAP/SMTP/SOAP), JWT, RBAC

### Status

ATIVO

---

## Módulo: Usuários

### Responsabilidade

Gerenciar usuários WordPress: listagem, criação, edição, provisionamento automático, perfil e vínculo a área/singular.

### Evidências

#### Controllers

- `UsersController`

#### Services

- `UsersService`

#### Frontend

- `UserListPageNew`, `UserCreatePage`, `UserEditPage`, `UserIndexPage`, `UserPage`, `UserPermissionsPage`, `UserSessionsPage`, `AdminUsersPage`, `VincularAreaSetorPage`
- Store: `user`
- Service: `users.service`

### Dependências

WordPress, RBAC, Autenticação

### Status

ATIVO

---

## Módulo: RBAC

### Responsabilidade

Definir papéis canônicos, capabilities `portal_*`, auditoria de roles e operações de limpeza/reset.

### Evidências

#### Controllers

- `RBACController`

#### Services

- `RBACService`

#### Frontend

- `UserRolesSection`, `UserPermissionsPage`, `OrganizationPermissions`, `DepartmentPermissionsPage`
- Store: `auth-roles`

#### Outros

- `ensure-admin-caps.php` (MU-plugin auxiliar)

### Dependências

WordPress (roles/capabilities)

### Status

ATIVO

---

## Módulo: Singulares

### Responsabilidade

CRUD e estatísticas de singulares (CPT `singular`), incluindo áreas e colaboradores vinculados.

### Evidências

#### Controllers

- `SingularesController`

#### Services

- `SingularesService`

#### Content

- CPT `singular` registrado em `Bootstrap.php`

#### Frontend

- `OrganizationIndexPage`, `OrganizationList`, `OrganizationCreatePage`, `OrganizationEditPage`, `OrganizationShowPage`, `SinglePage`, `MySingularPage`
- Service: `singulares.service`

### Dependências

WordPress, RBAC, ACF

### Status

ATIVO

---

## Módulo: Áreas

### Responsabilidade

CRUD de áreas (taxonomia `portal_area`), hierarquia, estatísticas e relação com colaboradores/equipes.

### Evidências

#### Controllers

- `AreasController`

#### Services

- `AreasService`

#### Content

- Taxonomia `portal_area` em `Bootstrap.php`

#### Frontend

- `AreasListPage`, `DepartmentIndexPage`, `DepartmentCreatePage`, `DepartmentShowPage`, `AreaSettingsPage`, `AreaPage`, `CollaboratorAreaPage`
- Service: `AreasService`

### Dependências

WordPress, Singulares, RBAC

### Status

ATIVO

---

## Módulo: Equipes

### Responsabilidade

CRUD de equipes (CPT `team`, taxonomia `team`), membros e vínculo com áreas.

### Evidências

#### Controllers

- `TeamsController`

#### Services

- `TeamsService`

#### Content

- CPT `team` e taxonomia `team` em `Bootstrap.php`

#### Frontend

- `TeamListPage`, `TeamCreatePage`, `TeamDetailsPage`, `TeamMembersPage`, `TeamViewPage`, `MyTeamPage`
- `TeamAdministratorDocumentsPage`
- Service: `TeamsService`

### Dependências

WordPress, Áreas, RBAC

### Status

ATIVO

---

## Módulo: Colaboradores

### Responsabilidade

Listar, criar e gerenciar colaboradores vinculados a áreas e singulares.

### Evidências

#### Controllers

- Rotas em `AreasController` (`/areas/{id}/colaboradores`)
- Rotas em `SingularesController` (`/singulares/{id}/colaboradores`)

#### Services

- `UsersService` (papéis de colaborador)
- `CollaboratorsService` (frontend)

#### Frontend

- `CollaboratorIndexPage`, `CollaboratorCreatePage`, `CollaboratorManagement`, `CollaboratorLink`, `AdminCollaboratorsPage`
- `CollaboratorListPage`, `CollaboratorPage`, `CollaboratorHomePage`, `CollaboratorOrganizationPage`
- `ProfilePage`, `ProfileEditPage`

### Dependências

Usuários, Áreas, Singulares, RBAC

### Status

ATIVO

---

## Módulo: Documentos

### Responsabilidade

Gerenciar documentos (CPT `portal_documento`): upload, listagem, download, estatísticas, permissões e compartilhamento.

### Evidências

#### Controllers

- `DocumentsController`

#### Services

- `DocumentsService`, `StorageService`

#### Content

- `DocumentsManager` (CPT `portal_documento`)

#### Helpers

- `ImageQualityValidator`, `ResponsiveImageGenerator`

#### Frontend

- `DocumentIndexPage`, `DocumentsWithFoldersPage`, `DocumentsPage`, `PublicDocuments`, `PrivateDocuments`
- `MyDocumentsPage`, `CollaboratorAreaDocumentsPage`, `SingularAdministratorDocumentsPage`
- Store: `documents`
- Services: `documentosService`, `UploadsService`, `DocumentSharingService`

### Dependências

WordPress, Pastas, Armazenamento, RBAC

### Status

ATIVO

---

## Módulo: Pastas e Diretórios

### Responsabilidade

Gerenciar taxonomia `portal_pasta`, árvore de pastas, criação automática hierárquica e diretórios físicos em uploads.

### Evidências

#### Controllers

- `FoldersController`

#### Services

- `FoldersService`, `HierarchicalFoldersAutoCreationService`, `UserFoldersAutoCreationService`

#### Content

- `FoldersManager` (taxonomia `portal_pasta`)

#### Frontend

- `FoldersManagementPage`, `FolderTreePage`, `MyFoldersPage`
- Services: `FoldersService`, `DirectoryService`

### Dependências

WordPress, Documentos, Permissões de Pastas

### Status

ATIVO (`UserFoldersAutoCreationService` desabilitado em `Bootstrap.php` — ver LEGADO em lacunas)

---

## Módulo: Permissões de Pastas

### Responsabilidade

Validar permissões granulares de criação/acesso a pastas conforme hierarquia singular/área/time/usuário.

### Evidências

#### Controllers

- Endpoints em `FoldersController` (`/pastas/permissions-debug`, `/pastas/{id}/users`)

#### Services

- `FoldersPermissionsService`

#### Frontend

- `PermissionRequestsPage` (gestão de solicitações — ver módulo Solicitação de Permissões)
- Componentes em `folders/` e `documents/`

### Dependências

Pastas, RBAC, Usuários

### Status

ATIVO

---

## Módulo: Solicitação de Permissões

### Responsabilidade

Permitir solicitação, listagem e resposta (aprovar/negar) de acesso a documentos/pastas privados.

### Evidências

#### Controllers

- Não localizado controller para `/permission-requests`

#### Services

- Não localizado service dedicado no CMS

#### Frontend

- `PermissionRequestsPage`
- Service: `PermissionRequestService` (endpoint `/portaldecomunicacao/v1/permission-requests`)

### Dependências

Documentos, Pastas, Notificações (inferido pelo fluxo descrito no service — **não confirmado em backend**)

### Status

PARCIAL

---

## Módulo: Notificações

### Responsabilidade

Criar, listar, marcar como lida e transmitir notificações (incluindo SSE e configurações).

### Evidências

#### Controllers

- `NotificationsController`

#### Services

- `NotificationsService`, `NotificationsManager`, `NotificationChannel`

#### Frontend

- `NotificationCenterPage`
- Store: `notifications`

### Dependências

WordPress, Usuários

### Status

ATIVO

---

## Módulo: Onboarding

### Responsabilidade

Fluxo de primeiro acesso: opções de singular/área, seleção e verificação de status de onboarding.

### Evidências

#### Controllers

- `OnboardingController` (`/onboarding/options`, `/onboarding/select`, `/onboarding/status`)

#### Services

- `UsersService`, `SingularesService`, `AreasService` (usados pelo controller)

#### Frontend

- `OnboardingPage`, `OnboardingRequestsPage`
- Store: `onboarding` (chama `/onboarding/current`, `/onboarding/requests` — **não encontrados no controller**)

### Dependências

Usuários, Singulares, Áreas

### Status

PARCIAL

---

## Módulo: Auditoria

### Responsabilidade

Registrar eventos de auditoria em tabela dedicada e expor consulta via API e telas admin.

### Evidências

#### Controllers

- `RBACController` (endpoint `/rbac/audit`)

#### Services

- `AuditService`

#### Frontend

- `AuditPage`, `OrganizationAudit`

### Dependências

WordPress (wpdb), RBAC

### Status

ATIVO

---

## Módulo: Armazenamento e Upload

### Responsabilidade

Controlar quotas por usuário, validar tamanho de arquivo e uso de armazenamento.

### Evidências

#### Controllers

- Indireto via `DocumentsController` e uploads WordPress

#### Services

- `StorageService`

#### Frontend

- `UploadsService`, componentes de upload em `documents/` e `design-system/`

### Dependências

WordPress, Documentos

### Status

ATIVO

---

## Módulo: Configuração do Portal

### Responsabilidade

Expor configurações públicas do portal (logo, suporte, SEO) via ACF e endpoint REST.

### Evidências

#### Controllers

- `EndpointsRegistry` (`/configuracoes-publicas`)

#### Services

- Não há service dedicado

#### Content

- `ACFManager`, `MetaFields`
- ACF JSON: `group_pc_configuracoes_portal.json`

#### Frontend

- `SettingsPage`, `AreaSettingsPage`
- Store: `config`

### Dependências

WordPress, ACF

### Status

ATIVO

---

## Módulo: API — Saúde e Utilitários

### Responsabilidade

Endpoints públicos de health check, status da API, versão e registro central de rotas utilitárias.

### Evidências

#### Controllers

- `EndpointsRegistry` (`/ping`, `/status`, `/version`, `/configuracoes-publicas`)
- `RestController` (classe base abstrata)

#### Middleware

- `CorsMiddleware`, `RateLimitMiddleware`

#### Frontend

- Consumo indireto via boot e validação de ambiente

### Dependências

WordPress REST API, Bootstrap

### Status

ATIVO

---

## Módulo: Diagnóstico

### Responsabilidade

Endpoints de diagnóstico e correção relacionados a upload.

### Evidências

#### Controllers

- `DiagnosticsController` (`/diagnostics/upload`, `/diagnostics/fix`)

#### Frontend

- `ToolsPage`

### Dependências

WordPress, Documentos/Upload

### Status

ATIVO

---

## Módulo: Busca Global

### Responsabilidade

Buscar documentos, áreas, singulares e colaboradores a partir de um termo único no frontend.

### Evidências

#### Controllers

- Não localizado endpoint `/search` global no CMS

#### Services

- `GlobalSearchService` (agrega chamadas a APIs existentes)

#### Frontend

- Integrado em componentes de busca (via service)

### Dependências

Documentos, Áreas, Singulares, Colaboradores

### Status

PARCIAL

---

## Módulo: Analytics

### Responsabilidade

Exibir métricas e dashboard analítico para administradores.

### Evidências

#### Controllers

- Não localizado endpoint `/analytics/dashboard` no CMS

#### Services

- Capability `portal_view_analytics` em `RBACService`

#### Frontend

- Store: `analytics` (chama `/analytics/dashboard`)
- `AnalyticsDashboardPage.vue.disabled` (rota desabilitada)

### Dependências

RBAC, API REST

### Status

PARCIAL

---

## Módulo: Comunicados

### Responsabilidade

Interface administrativa para criar e listar comunicados corporativos.

### Evidências

#### Controllers

- Não localizado

#### Frontend

- `AnnouncementsPage` (dados mockados em `loadComunicados`)

### Dependências

Não foi possível determinar (sem backend localizado)

### Status

PARCIAL

---

## Módulo: Fique por Dentro

### Responsabilidade

Exibir feed de publicações/notícias para colaboradores a partir de documentos filtrados.

### Evidências

#### Controllers

- Reutiliza `DocumentsController` via `GET /documentos`

#### Frontend

- `StayInformedPage`, `StayInformedPostPage`, `StayInformedAllPostsPage`

### Dependências

Documentos

### Status

PARCIAL

---

## Módulo: Convidados

### Responsabilidade

Hub e rotas administrativas para gestão de usuários convidados (role `visitor` no RBAC).

### Evidências

#### Controllers

- Não localizado controller dedicado

#### Services

- Role `visitor` definido em `RBACService`

#### Frontend

- `InvitedIndexPage`, `CadastrarConvidadosPage`
- Rotas: `admin.convidados.*` em `routes.ts`

### Dependências

Usuários, RBAC

### Status

PARCIAL

---

## Módulo: Central de Colaboração

### Responsabilidade

Hub de colaboração com abas de comentários, versões e aprovações.

### Evidências

#### Controllers

- Não localizado

#### Frontend

- `CollaborationPage`, `CollaborationHub` (sem chamadas `PortalApiService`/`cmsApi` localizadas)

### Dependências

Não foi possível determinar

### Status

PARCIAL

---

## Módulo: Navegação e Interface

### Responsabilidade

Gerenciar contexto de navegação por papel/área, estado de UI e ações rápidas do shell da aplicação.

### Evidências

#### Services

- `NavigationContextService`, `PortalApiService` (cliente HTTP base)

#### Frontend

- Stores: `ui`, `quickActions`
- `AppSidebar`, guards em `router/guards.ts`

### Dependências

Autenticação, RBAC

### Status

ATIVO

---

## Módulo: Cache

### Responsabilidade

Cache de dados via WordPress Transients com namespaces, tags e estatísticas de hit/miss.

### Evidências

#### Services

- `CacheService`

#### Frontend

- Não localizado store dedicado

### Dependências

WordPress

### Status

ATIVO

---

## Módulo: Backend PHP Legado

### Responsabilidade

Serviço PHP separado com rotas HTTP para autenticação, documentos e pastas (paralelo ao CMS).

### Evidências

#### Backend

- `backend/routes/api.php` (referencia `App\Controllers\*`)
- `backend/http-server.php`, `backend/index.legacy.php`
- Diretório `backend/src/` **não encontrado**

### Dependências

JWT, MySQL (referenciado em entrypoint)

### Status

LEGADO

---

## Cobertura da Descoberta

### Controllers Cobertos

| Controller | Módulo |
|---|---|
| AuthController | Autenticação e Sessão |
| UsersController | Usuários |
| RBACController | RBAC / Auditoria |
| SingularesController | Singulares |
| AreasController | Áreas / Colaboradores |
| TeamsController | Equipes |
| DocumentsController | Documentos |
| FoldersController | Pastas e Diretórios / Permissões de Pastas |
| NotificationsController | Notificações |
| OnboardingController | Onboarding |
| DiagnosticsController | Diagnóstico |
| EndpointsRegistry | API — Saúde e Utilitários / Configuração do Portal |
| RestController | API — Saúde e Utilitários (classe base) |

### Services Cobertos

| Service | Módulo |
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
| UserFoldersAutoCreationService | Pastas e Diretórios (LEGADO — desabilitado) |
| StorageService | Armazenamento e Upload |
| NotificationsService | Notificações |
| NotificationsManager | Notificações |
| NotificationChannel | Notificações |
| AuditService | Auditoria |
| CacheService | Cache |

### Stores Cobertas

| Store | Módulo |
|---|---|
| auth-core | Autenticação e Sessão |
| auth-roles | RBAC |
| auth-context | Autenticação e Sessão |
| user | Usuários |
| documents | Documentos |
| notifications | Notificações |
| onboarding | Onboarding |
| config | Configuração do Portal |
| analytics | Analytics |
| ui | Navegação e Interface |
| quickActions | Navegação e Interface |

---

## Lacunas Encontradas

### Controllers sem módulo funcional dedicado

- Nenhum controller REST do MU-plugin ficou sem classificação.

### Services sem módulo

- Nenhum service em `portaldecomunicacao/Services/` ficou sem classificação.

### Stores sem módulo

- Nenhuma store Pinia em `frontend/src/stores/` ficou sem classificação.

### Endpoints referenciados no frontend sem implementação localizada no CMS

| Endpoint (frontend) | Service/Store | Observação |
|---|---|---|
| `/permission-requests` | `PermissionRequestService` | Controller não encontrado em `mu-plugins` |
| `/document-sharing` | `DocumentSharingService` | Controller não encontrado em `mu-plugins` |
| `/analytics/dashboard` | `analytics` store | Controller não encontrado em `mu-plugins` |
| `/onboarding/current` | `onboarding` store | Não registrado em `OnboardingController` |
| `/onboarding/requests` | `onboarding` store | Não registrado em `OnboardingController` |

### Endpoints CMS sem módulo frontend dedicado identificado

| Endpoint | Controller | Observação |
|---|---|---|
| `/diagnostics/upload`, `/diagnostics/fix` | DiagnosticsController | Frontend parcial (`ToolsPage`) |
| `/rbac/cleanup`, `/rbac/reset-canonical` | RBACController | Sem tela dedicada localizada |

### Componentes desabilitados ou legados

- `UserFoldersAutoCreationService` — comentado/desabilitado em `Bootstrap.php`
- `backend/src/` — ausente; rotas em `backend/routes/api.php` sem implementação
- `AnalyticsDashboardPage.vue.disabled` — rota com import desabilitado

### Módulos apenas com evidência frontend (sem backend dedicado)

- Comunicados, Central de Colaboração, Convidados (parcial), Busca Global (agregador)

---

## Resultado da Validação

### Validação 1

Todos os Controllers foram classificados?

**SIM**

### Validação 2

Todos os Services foram classificados?

**SIM**

### Validação 3

Todas as Stores foram classificadas?

**SIM**

### Validação 4

Existem módulos sem evidência?

**NÃO** — todos os módulos listados possuem ao menos uma evidência (controller, service, store ou tela). Módulos `PARCIAL` possuem evidência incompleta entre camadas.

### Status Final

**APROVADO COM RESSALVAS**

**Motivo:** existem endpoints referenciados pelo frontend (`permission-requests`, `document-sharing`, `analytics/dashboard`, `onboarding/requests`) sem implementação localizada no CMS, e divergência entre rotas do `OnboardingController` e chamadas da store `onboarding`. O backend legado declara rotas sem código-fonte em `backend/src/`.
