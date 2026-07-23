# Discovery — Current Data Model

## Objetivo

Mapear o modelo de dados atual do sistema Portal de Comunicação com base exclusiva em evidências do código-fonte (CMS WordPress MU-plugin, frontend Quasar e estruturas de persistência referenciadas).

**Nível de confiança da descoberta:** Alto para entidades com Service/Manager + tabela/CPT/taxonomia localizados; Médio para metadados ACF com divergência de slug; Baixo para objetos referenciados apenas no frontend sem backend correspondente.

**Dependências utilizadas:** módulos de `docs/discovery/01-current-modules.md` (validação: APROVADO COM RESSALVAS); RBAC de `docs/discovery/02-current-rbac.md` (validação: APROVADO COM RESSALVAS). Roles e módulos não foram redescobertos.

---

## Resumo Executivo

| Entidade | Tipo | Origem | Status |
|---|---|---|---|
| Usuário | WordPress User | `wp_users`, `wp_usermeta` | Confirmado |
| Colaborador | WordPress User | `wp_users`, `wp_usermeta` (role `collaborator`) | Confirmado |
| Singular | CPT | `wp_posts` (`singular`) | Confirmado |
| Área | Taxonomia | `wp_terms` (`portal_area`) | Confirmado |
| Equipe | Taxonomia | `wp_terms` (`team`) | Confirmado |
| Equipe (CPT) | CPT | `wp_posts` (`team`) | Registrado; uso operacional divergente |
| Documento | CPT | `wp_posts` (`portal_documento`) | Confirmado |
| Pasta | Taxonomia | `wp_terms` (`portal_pasta`) | Confirmado |
| Arquivo Físico | Objeto Virtual | `wp-content/uploads/portaldecomunicacao/` | Confirmado |
| Registro de Auditoria | Tabela Customizada | `{prefix}audit_log` | Confirmado |
| Notificação (Portal) | Tabela Customizada | `{prefix}portal_notifications` | Confirmado |
| Auditoria de Notificação | Tabela Customizada | `{prefix}portal_notification_audit` | Confirmado |
| Notificação (PDC) | Tabela Customizada | `{prefix}pdc_notifications` | Confirmado |
| Fila de Notificação | Tabela Customizada | `{prefix}pdc_notifications_queue` | Confirmado |
| Configurações do Portal | ACF | `wp_options` (options page `pc_configuracoes_portal`) | Confirmado |
| Configurações SEO | ACF | `wp_options` (options page `pc_seo_padrao`) | Confirmado |
| Papel RBAC | Objeto Virtual | `wp_usermeta` (`wp_capabilities`), `wp_options` (`wp_user_roles`) | Confirmado |
| Compartilhamento de Documento | ACF / Metadado | `wp_postmeta` (`portal_doc_sharing`) | Confirmado |
| Solicitação de Permissão | Objeto Virtual | Frontend (`PermissionRequestService`) | Sem persistência localizada |
| Solicitação de Onboarding (frontend) | Objeto Virtual | Frontend (`onboarding` store) | Sem persistência localizada |
| Métricas Analytics | Objeto Virtual | Frontend (`analytics` store) | Sem persistência localizada |
| Comunicado | Objeto Virtual | Frontend (rotas admin) | Sem persistência localizada |

---

## Entidades Identificadas

### Entidade: Usuário

#### Tipo

WordPress User

#### Origem

`wp_users`, `wp_usermeta`

#### Responsabilidade

Identidade de acesso ao portal, autenticação (Zimbra/JWT), vínculo organizacional e armazenamento de quotas.

#### Campos Principais

- `ID`, `user_login`, `user_email`
- `portal_singular_id`, `portal_area_id`, `portal_team_id`
- `portal_storage_quota`, `portal_storage_usage`

#### Relacionamentos

```
Usuário
 ├── pertence a Singular (portal_singular_id)
 ├── pertence a Área (portal_area_id)
 ├── pertence a Equipe (portal_team_id)
 ├── possui Papéis RBAC (wp_capabilities)
 └── associado a Pastas (wp_term_relationships)
```

#### Evidências

- `UsersService.php`, `AuthController.php`, `StorageService.php`

---

### Entidade: Colaborador

#### Tipo

WordPress User

#### Origem

`wp_users`, `wp_usermeta` (role `collaborator` e metadados ACF por role)

#### Responsabilidade

Representação de colaborador operacional vinculado a singular/área; exposto na API e no frontend como entidade de negócio derivada do usuário.

#### Campos Principais

- `id`, `matricula`, `cargo`, `status`
- `portal_singular_id`, `portal_area_id`
- `email`, `telefone`

#### Relacionamentos

```
Colaborador
 ├── pertence a Singular
 ├── pertence a Área
 └── possui Documentos (via post_author / metadados)
```

#### Evidências

- `UsersService.php`, `group_colaborador_cargo.json`, `group_colaborador_info_pessoal.json`, `frontend/src/types/api-contracts.ts` (`Colaborador`)

---

### Entidade: Singular

#### Tipo

CPT

#### Origem

`wp_posts` (`post_type = singular`), `wp_postmeta`, campos ACF

#### Responsabilidade

Unidade organizacional (empresa singular) da federação; agrupa áreas e colaboradores.

#### Campos Principais

- `ID`, `post_title`, `post_name` (slug)
- `portal_cnpj`, `portal_razao_social`, `portal_cod_unimed`
- `responsavel_id`, `portal_administrators`

#### Relacionamentos

```
Singular
 ├── possui Áreas (singular_id em term_meta)
 ├── possui Colaboradores (portal_singular_id)
 └── possui Documentos (portal_doc_singular_id)
```

#### Evidências

- `Bootstrap.php`, `SingularesService.php`, `group_pc_singular_dados_basicos.json`, `group_singular_contatos.json`

---

### Entidade: Área

#### Tipo

Taxonomia

#### Origem

`wp_terms` / `wp_termmeta` (`taxonomy = portal_area`)

#### Responsabilidade

Setor/departamento vinculado a uma singular; pode ser hierárquica; referência para documentos, pastas e usuários.

#### Campos Principais

- `term_id`, `name`, `slug`, `parent`
- `singular_id`, `area_manager`, `area_contacts`
- `folder_id`

#### Relacionamentos

```
Área
 ├── pertence a Singular (singular_id)
 ├── possui Equipes (area_id em team term_meta)
 ├── possui Colaboradores (portal_area_id)
 └── possui Documentos/Pastas (area_id)
```

#### Evidências

- `AreasService.php` (`TAXONOMY = portal_area`), `Bootstrap.php`, `group_area_informacoes.json`

---

### Entidade: Equipe

#### Tipo

Taxonomia

#### Origem

`wp_terms` / `wp_termmeta` (`taxonomy = team`)

#### Responsabilidade

Time vinculado a área e singular; referência organizacional para usuários e visibilidade de documentos.

#### Campos Principais

- `term_id`, `name`, `slug`
- `area_id`, `singular_id`, `team_leader`
- `team_icon`, `team_color`

#### Relacionamentos

```
Equipe
 ├── pertence a Área (area_id)
 ├── pertence a Singular (singular_id)
 └── possui Colaboradores (portal_team_id)
```

#### Evidências

- `TeamsService.php` (`TAXONOMY = team`), `Bootstrap.php`

---

### Entidade: Equipe (CPT)

#### Tipo

CPT

#### Origem

`wp_posts` (`post_type = team`)

#### Responsabilidade

CPT registrado em `Bootstrap.php`; `UsersService` referencia `post_type === 'team'` para slug, enquanto `TeamsService` opera sobre taxonomia `team`.

#### Campos Principais

- `ID`, `post_title`, `post_name`

#### Relacionamentos

Relação operacional com Área/Usuário não confirmada de forma unificada (coexistência com taxonomia `team`).

#### Evidências

- `Bootstrap.php` (`register_post_type('team')`), `UsersService.php` (linha com `post_type === 'team'`)

---

### Entidade: Documento

#### Tipo

CPT

#### Origem

`wp_posts` (`post_type = portal_documento`), `wp_postmeta` (`portal_doc_*`)

#### Responsabilidade

Arquivo lógico do portal com metadados de visibilidade, escopo organizacional e estatísticas de acesso.

#### Campos Principais

- `ID`, `post_title`, `post_author`
- `portal_doc_file_url`, `portal_doc_visibility`
- `portal_doc_area_id`, `portal_doc_singular_id`

#### Relacionamentos

```
Documento
 ├── pertence a Usuário (post_author)
 ├── vinculado a Singular/Área (post_meta)
 ├── classificado em Pastas (wp_term_relationships)
 └── possui Compartilhamento (portal_doc_sharing)
```

#### Evidências

- `DocumentsManager.php`, `MetaFields.php`, `DocumentsService.php`, `group_documentos_metadata.json`

---

### Entidade: Pasta

#### Tipo

Taxonomia

#### Origem

`wp_terms` / `wp_termmeta` (`taxonomy = portal_pasta`)

#### Responsabilidade

Diretório hierárquico de documentos; metadados de visibilidade, caminho físico e escopo organizacional.

#### Campos Principais

- `term_id`, `name`, `slug`, `parent`
- `folder_path`, `user_path`, `visibility`
- `area_id`, `singular_id`

#### Relacionamentos

```
Pasta
 ├── possui Subpastas (parent hierárquico)
 ├── contém Documentos (taxonomy em portal_documento)
 └── associada a Usuários (wp_term_relationships, object_id = user_id)
```

#### Evidências

- `FoldersManager.php` (`TAXONOMY = portal_pasta`), `FoldersService.php`, `UsersService.php`

---

### Entidade: Arquivo Físico

#### Tipo

Objeto Virtual

#### Origem

Sistema de arquivos: `wp_upload_dir()['basedir']/portaldecomunicacao/documentos/{folder_path}`

#### Responsabilidade

Binário persistido em disco referenciado pelo metadado `portal_doc_file_url` do documento.

#### Campos Principais

- `path`, `url`
- `mime_type`, `file_size` (espelhados em post_meta)

#### Relacionamentos

```
Arquivo Físico
 └── pertence a Documento (portal_doc_file_url)
```

#### Evidências

- `FoldersService.php` (upload em `/portaldecomunicacao/documentos/`), `MetaFields.php`

---

### Entidade: Registro de Auditoria

#### Tipo

Tabela Customizada

#### Origem

`{wpdb->prefix}audit_log` (ex.: `unmpc_audit_log` em comentário; construtor usa `$wpdb->prefix . 'audit_log'`)

#### Responsabilidade

Log unificado de eventos do sistema (`event_type`, entidade afetada, usuário, contexto JSON).

#### Campos Principais

- `id`, `event_type`, `entity_type`, `entity_id`
- `user_id`, `context`, `created_at`

#### Relacionamentos

```
Registro de Auditoria
 └── referencia entidades genéricas (document, user, area, team, etc.)
```

#### Evidências

- `AuditService.php` (método `log`, insert em colunas listadas)

---

### Entidade: Notificação (Portal)

#### Tipo

Tabela Customizada

#### Origem

`{prefix}portal_notifications`

#### Responsabilidade

Notificações in-app por usuário com tipo, prioridade, status de leitura e metadados.

#### Campos Principais

- `id`, `user_id`, `title`, `type`
- `status`, `priority`, `created_at`

#### Relacionamentos

```
Notificação (Portal)
 └── pertence a Usuário (user_id)
```

#### Evidências

- `NotificationsService.php` (`ensureTablesExist`, CRUD)

---

### Entidade: Auditoria de Notificação

#### Tipo

Tabela Customizada

#### Origem

`{prefix}portal_notification_audit`

#### Responsabilidade

Trilha de ações sobre notificações do serviço principal (`NotificationsService`).

#### Campos Principais

- `id`, `notification_id`, `user_id`, `action`
- `metadata`, `created_at`

#### Relacionamentos

```
Auditoria de Notificação
 └── referencia Notificação (Portal) (notification_id)
```

#### Evidências

- `NotificationsService.php` (`ensureTablesExist`)

---

### Entidade: Notificação (PDC)

#### Tipo

Tabela Customizada

#### Origem

`{prefix}pdc_notifications`

#### Responsabilidade

Canal alternativo de notificações em banco (`NotificationChannel`, canal `database`).

#### Campos Principais

- `id`, `user_id`, `title`, `message`
- `type`, `status`, `created_at`

#### Relacionamentos

```
Notificação (PDC)
 └── pertence a Usuário (user_id)
```

#### Evidências

- `NotificationChannel.php` (`createTable`)

---

### Entidade: Fila de Notificação

#### Tipo

Tabela Customizada

#### Origem

`{prefix}pdc_notifications_queue`

#### Responsabilidade

Fila agendada de envio de notificações multicanal.

#### Campos Principais

- `id`, `user_id`, `scheduled_at`, `status`
- `channels`, `sent_at`

#### Relacionamentos

```
Fila de Notificação
 └── pertence a Usuário (user_id)
```

#### Evidências

- `NotificationChannel.php` (`createTable`, tabela `pdc_notifications_queue`)

---

### Entidade: Configurações do Portal

#### Tipo

ACF

#### Origem

`wp_options` via ACF options page `pc_configuracoes_portal`

#### Responsabilidade

Configurações globais do portal (logo, e-mail de suporte, textos institucionais).

#### Campos Principais

- `pc_brand_logo`, `pc_support_email`
- `pc_portal_name`, `pc_welcome_message`

#### Relacionamentos

Entidade singleton; consumida pelo frontend via API de configuração pública.

#### Evidências

- `group_pc_configuracoes_portal.json`, `ConfigController` (módulo Configuração do Portal em doc 01)

---

### Entidade: Configurações SEO

#### Tipo

ACF

#### Origem

`wp_options` via ACF options page `pc_seo_padrao`

#### Responsabilidade

Metadados SEO padrão do portal.

#### Campos Principais

- título padrão, descrição padrão, robots

#### Relacionamentos

Entidade singleton; sem vínculo hierárquico.

#### Evidências

- `group_pc_seo_padrao.json`

---

### Entidade: Papel RBAC

#### Tipo

Objeto Virtual

#### Origem

`wp_usermeta` (`wp_capabilities`), `wp_options` (`wp_user_roles`); capabilities `portal_*` em `RBACService`

#### Responsabilidade

Papéis canônicos (`administrator`, `singular_administrator`, `area_administrator`, `team_owner`, `collaborator`, `visitor`) e capabilities associadas.

#### Campos Principais

- role slug, capabilities array
- `X-Active-Role` (contexto de sessão, não persistido como entidade)

#### Relacionamentos

```
Usuário ↔ Papel (N:N via wp_capabilities)
```

#### Evidências

- `RBACService.php`, `docs/discovery/02-current-rbac.md`

---

### Entidade: Compartilhamento de Documento

#### Tipo

ACF / Metadado

#### Origem

`wp_postmeta` (`portal_doc_sharing`, tipo `object`)

#### Responsabilidade

Regras de compartilhamento embutidas no documento; consumidas por `DocumentSharingService` no frontend.

#### Campos Principais

- estrutura `sharing` (objeto em post_meta)

#### Relacionamentos

```
Compartilhamento de Documento
 └── pertence a Documento
```

#### Evidências

- `MetaFields.php` (`sharing` em meta fields), `frontend/src/services/DocumentSharingService.ts`

---

### Entidade: Solicitação de Permissão

#### Tipo

Objeto Virtual

#### Origem

DTO `PermissionRequest` em `frontend/src/services/PermissionRequestService.ts`; endpoint `/portaldecomunicacao/v1/permission-requests`

#### Responsabilidade

Fluxo de pedido/aprovação de acesso a documento ou pasta privada.

#### Campos Principais

- `id`, `document_id`, `folder_id`, `requester_id`
- `status`, `resource_type`

#### Relacionamentos

```
Solicitação de Permissão
 ├── referencia Documento ou Pasta
 └── referencia Usuário (requester, owner)
```

#### Evidências

- `PermissionRequestService.ts`; ausência de controller/tabela no CMS (validação módulos)

---

### Entidade: Solicitação de Onboarding (frontend)

#### Tipo

Objeto Virtual

#### Origem

`frontend/src/stores/onboarding.ts` (`OnboardingRequest`); endpoints `/onboarding/current`, `/onboarding/requests`

#### Responsabilidade

Modelo de solicitação com status de revisão administrativa; diverge do fluxo CMS (`/onboarding/select` grava `user_meta`).

#### Campos Principais

- `id`, `user_id`, `reason`, `status`
- `department`, `manager_email`

#### Relacionamentos

```
Solicitação de Onboarding
 └── referencia Usuário
```

#### Evidências

- `onboarding.ts`, `OnboardingController.php` (fluxo alternativo sem tabela dedicada)

---

### Entidade: Métricas Analytics

#### Tipo

Objeto Virtual

#### Origem

`frontend/src/stores/analytics.ts`; endpoint `/analytics/dashboard`

#### Responsabilidade

Agregação de métricas administrativas consumidas pelo dashboard.

#### Campos Principais

Não foi possível determinar schema persistido (resposta API não localizada no CMS).

#### Relacionamentos

Sem relacionamentos persistidos localizados.

#### Evidências

- `analytics.ts`; ausência de endpoint no CMS (validação módulos)

---

### Entidade: Comunicado

#### Tipo

Objeto Virtual

#### Origem

Rotas e páginas admin no frontend (`admin.comunicados`); sem CPT, taxonomia ou tabela no CMS

#### Responsabilidade

Comunicados corporativos referenciados na UI administrativa.

#### Campos Principais

Não foi possível determinar.

#### Relacionamentos

Não confirmados.

#### Evidências

- `AdminDashboard.vue` (navegação); ausência de `comunicado` em `cms/wp-content/mu-plugins/`

---

## Relacionamentos Identificados

### Relacionamentos 1:N

| Origem | Destino | Evidência |
|---|---|---|
| Singular | Áreas | `AreasService` — `singular_id` em `wp_termmeta` |
| Singular | Colaboradores | `portal_singular_id` em `wp_usermeta` |
| Singular | Documentos | `portal_doc_singular_id` em `wp_postmeta` |
| Área | Equipes | `TeamsService` — `area_id` em term_meta de `team` |
| Área | Colaboradores | `portal_area_id` em `wp_usermeta` |
| Área | Documentos | `portal_doc_area_id` em `wp_postmeta` |
| Equipe | Colaboradores | `portal_team_id` em `wp_usermeta` |
| Usuário | Documentos | `post_author` em `wp_posts` |
| Usuário | Notificações | `user_id` em `portal_notifications` / `pdc_notifications` |
| Pasta | Documentos | Taxonomia `portal_pasta` em `portal_documento` |
| Singular | Pastas | `singular_id` em term_meta de `portal_pasta` |
| Área | Pastas | `area_id` em term_meta de `portal_pasta` |

---

### Relacionamentos N:N

| Origem | Destino | Evidência |
|---|---|---|
| Usuário | Papéis RBAC | `wp_capabilities` em `wp_usermeta` |
| Documento | Pastas | `wp_term_relationships` (post ↔ term) |
| Usuário | Pastas | `wp_term_relationships` (`object_id` = `user_id`, taxonomy `portal_pasta`) — `UsersService::cleanFolderPermissionsForUser` |

---

### Relacionamentos Hierárquicos

| Entidade | Tipo | Evidência |
|---|---|---|
| Pasta | `parent` em termo `portal_pasta` | `FoldersManager.php` (`hierarchical => true`) |
| Área | Cadastro em `portal_area` | `AreasService`, `formatArea` |
| Equipe | taxonomia `team` hierárquica | `Bootstrap.php` (`hierarchical => true`) |

---

## Mapeamento por Módulo

Referência: 27 módulos de `docs/discovery/01-current-modules.md`.

| Módulo | Entidades |
|---|---|
| Autenticação e Sessão | Usuário, Papel RBAC |
| Usuários | Usuário, Colaborador |
| RBAC | Papel RBAC, Usuário |
| Singulares | Singular |
| Áreas | Área, Singular |
| Equipes | Equipe, Equipe (CPT), Área |
| Colaboradores | Colaborador, Usuário, Singular, Área |
| Documentos | Documento, Arquivo Físico, Compartilhamento de Documento |
| Pastas e Diretórios | Pasta, Documento |
| Permissões de Pastas | Pasta, Usuário |
| Solicitação de Permissões | Solicitação de Permissão, Documento, Pasta |
| Notificações | Notificação (Portal), Notificação (PDC), Fila de Notificação, Auditoria de Notificação |
| Onboarding | Usuário, Singular, Área, Solicitação de Onboarding (frontend) |
| Auditoria | Registro de Auditoria |
| Armazenamento e Upload | Usuário (`portal_storage_*`), Arquivo Físico, Documento |
| Configuração do Portal | Configurações do Portal, Configurações SEO |
| API — Saúde e Utilitários | Configurações do Portal |
| Diagnóstico | Pasta, Documento |
| Busca Global | Documento, Área, Singular, Colaborador |
| Analytics | Métricas Analytics |
| Comunicados | Comunicado |
| Fique por Dentro | Não foi possível determinar entidade persistida |
| Convidados | Usuário (role `visitor`) |
| Central de Colaboração | Não foi possível determinar entidade persistida |
| Navegação e Interface | Usuário, Papel RBAC (contexto) |
| Cache | `wp_options` / transients (infraestrutura WP) |
| Backend PHP Legado | Documento, Pasta (rotas legadas; `backend/src` ausente) |

---

## Persistência

### WordPress Core

| Estrutura | Uso |
|---|---|
| `wp_users` | Usuários e colaboradores |
| `wp_usermeta` | Vínculos organizacionais, quotas, capabilities, ACF de usuário |
| `wp_posts` | CPTs `singular`, `team`, `portal_documento` |
| `wp_postmeta` | Metadados de documentos (`portal_doc_*`), ACF de singular |
| `wp_terms` / `wp_term_taxonomy` | Taxonomias `portal_area`, `portal_pasta`, `team` |
| `wp_termmeta` | Metadados de área, pasta e equipe |
| `wp_term_relationships` | Documento↔Pasta, Usuário↔Pasta |
| `wp_options` | Roles globais, opções ACF (`pc_*`), transients (cache) |

Prefixo de tabelas: `$wpdb->prefix` (comentários referenciam `unmpc_`; Dockerfile usa `wp_`).

---

### CPTs

| CPT | Módulo |
|---|---|
| `singular` | Singulares |
| `portal_documento` | Documentos |
| `team` | Equipes (registrado; operação principal via taxonomia `team`) |

---

### Taxonomias

| Taxonomia | Módulo |
|---|---|
| `portal_area` | Áreas |
| `portal_pasta` | Pastas e Diretórios, Documentos |
| `team` | Equipes |

---

### Tabelas Customizadas

| Tabela | Finalidade |
|---|---|
| `{prefix}audit_log` | Auditoria unificada de eventos |
| `{prefix}portal_notifications` | Notificações in-app (NotificationsService) |
| `{prefix}portal_notification_audit` | Auditoria de notificações |
| `{prefix}pdc_notifications` | Notificações canal database (NotificationChannel) |
| `{prefix}pdc_notifications_queue` | Fila de envio de notificações |

---

### ACF

| Grupo | Entidade |
|---|---|
| `group_pc_singular_dados_basicos` / `group_singular_dados_basicos` | Singular (CPT `singular`) |
| `group_singular_contatos` | Singular |
| `group_pc_area_informacoes` / `group_area_informacoes` | Área (location: taxonomy `area` — diverge de `portal_area`) |
| `group_colaborador_info_pessoal` | Colaborador (user_role) |
| `group_colaborador_cargo` | Colaborador (user_role) |
| `group_documentos_metadata` | Documento (location: `documento` — diverge de `portal_documento`) |
| `group_pc_configuracoes_portal` | Configurações do Portal |
| `group_pc_seo_padrao` | Configurações SEO |

---

## Lacunas Encontradas

- **CPT `team` vs taxonomia `team`:** ambos registrados; `TeamsService` usa taxonomia; `UsersService` referencia CPT para slug.
- **ACF com slugs divergentes:** grupos de área apontam taxonomy `area` (CMS usa `portal_area`); grupo de documento aponta `documento` (CMS usa `portal_documento`).
- **`group_pc_area_informacoes`:** `fields` vazio no JSON localizado.
- **Duplicidade de notificações:** `portal_notifications` e `pdc_notifications` coexistem em serviços distintos.
- **`audit_log`:** insert localizado em `AuditService`; `CREATE TABLE` não localizado no repositório.
- **Solicitação de Permissão:** frontend e endpoint referenciados; controller/tabela ausentes no CMS.
- **Onboarding:** CMS persiste vínculo em `user_meta` (`OnboardingController`); frontend modela `OnboardingRequest` com endpoints diferentes.
- **Analytics e Comunicados:** consumo no frontend sem entidade/tabela/CPT localizada.
- **Fique por Dentro / Central de Colaboração:** módulos PARCIAIS sem estrutura de dados confirmada.
- **Backend legado:** `backend/src` ausente; persistência não verificável nessa camada.
- **Compartilhamento (`/document-sharing`):** metadado `portal_doc_sharing` existe; controller REST dedicado não localizado.

---

## Cobertura da Descoberta

### Entidades Cobertas

| Categoria | Quantidade | Observação |
|---|---|---|
| WordPress User | 2 views (Usuário, Colaborador) | Mesma origem física |
| CPT | 3 registrados | 2 operacionais confirmados + `team` divergente |
| Taxonomia | 3 | Todas com Service dedicado |
| Tabela customizada | 5 | 2 subsistemas de notificação |
| ACF / options | 2 singletons + metadados embutidos | Divergências de location |
| Objeto virtual (frontend) | 4 | Sem persistência localizada |

### Relacionamentos Cobertos

| Tipo | Quantidade |
|---|---|
| 1:N | 12 confirmados |
| N:N | 3 confirmados |
| Hierárquicos | 3 confirmados |

### Estruturas Persistidas Cobertas

| Tipo | Cobertura |
|---|---|
| WordPress core | 8 estruturas mapeadas |
| CPTs | 3/3 registrados classificados |
| Taxonomias | 3/3 classificadas |
| Tabelas custom | 5 localizadas |
| ACF JSON | 9 grupos inventariados |

---

## Resultado da Validação

### Validação 1

Todas as entidades possuem origem identificada?

**NÃO** — `Comunicado`, `Métricas Analytics`, `Solicitação de Permissão` e `Solicitação de Onboarding (frontend)` não possuem origem de persistência localizada.

### Validação 2

Todos os módulos possuem entidades associadas?

**NÃO** — `Fique por Dentro` e `Central de Colaboração` sem entidade confirmada; `Analytics` e `Comunicados` apenas com objetos virtuais.

### Validação 3

Todos os CPTs identificados foram classificados?

**SIM** — `singular`, `portal_documento`, `team`.

### Validação 4

Todas as taxonomias identificadas foram classificadas?

**SIM** — `portal_area`, `portal_pasta`, `team`.

### Validação 5

Existem entidades utilizadas pelo frontend sem persistência localizada?

**SIM** — `PermissionRequest`, `OnboardingRequest` (modelo frontend), métricas analytics, comunicados.

### Validação 6

Existem relacionamentos não confirmados?

**SIM** — relação operacional CPT `team` vs taxonomia `team`; vínculo de compartilhamento via API `/document-sharing` vs apenas `portal_doc_sharing`.

---

## Status Final

**APROVADO COM RESSALVAS**

Ressalvas principais: objetos virtuais do frontend sem backend, coexistência CPT/taxonomia `team`, divergência de slugs ACF, duplicidade de tabelas de notificação e módulos PARCIAIS sem modelo de dados confirmado. Não bloqueia continuidade da Discovery conforme validações anteriores.
