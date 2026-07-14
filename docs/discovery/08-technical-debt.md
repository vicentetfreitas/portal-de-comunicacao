# Discovery — Technical Debt

## Objetivo

Consolidar as dívidas técnicas identificadas durante a Discovery (documentos 01–07), sem nova descoberta, sem proposta de correção e sem definição de arquitetura alvo.

**Nível de confiança da consolidação:** Alto — todos os itens possuem rastreabilidade direta às seções Lacunas, Divergências ou Pontos Críticos dos documentos fonte.

**Fontes:** `01-current-modules.md` a `07-current-architecture.md`.

---

## Resumo Executivo

| Categoria | Quantidade |
|---|---|
| Arquitetural | 8 |
| Aplicacional | 12 |
| Segurança | 11 |
| Dados | 8 |
| Integração | 7 |
| Infraestrutura | 7 |
| Documentação | 4 |
| **Total de dívidas** | **57** |

| Criticidade | Quantidade |
|---|---|
| CRÍTICA | 9 |
| ALTA | 21 |
| MÉDIA | 23 |
| BAIXA | 4 |

---

## Dívidas Arquiteturais

| ID | Descrição | Impacto | Criticidade | Evidência |
|---|---|---|---|---|
| TD-ARCH-001 | Backend PHP legado coexistindo com CMS como API principal | Duplicidade de rotas auth/documentos/pastas; `BackendSync` mantém acoplamento | CRÍTICA | `07` Pontos Críticos; `05` Divergências; `01` Módulo LEGADO |
| TD-ARCH-002 | Dois subsistemas de notificações em paralelo (`portal_notifications` + `pdc_notifications`) | Inconsistência de persistência e canais; risco de duplicidade de envio | ALTA | `03` Lacunas; `05` Divergências; `07` Pontos Críticos |
| TD-ARCH-003 | JWT duplicado: emissor nativo + plugin `jwt-auth-minimal` + referências legadas | Ambiguidade de validação e refresh de tokens | CRÍTICA | `05` Divergências; `07` Pontos Críticos |
| TD-ARCH-004 | Dois clientes Axios (`api`, `cmsApi`) para o mesmo CMS | Namespaces divergentes; complexidade de manutenção HTTP | MÉDIA | `05` Divergências |
| TD-ARCH-005 | Acoplamento forte frontend ↔ CMS (dependência total da API REST) | Indisponibilidade do CMS paralisa toda a aplicação | ALTA | `07` Acoplamentos |
| TD-ARCH-006 | `DocumentsController` registrado com rotas desabilitadas; `DocumentsManager` ativo | Duas implementações paralelas de documentos | MÉDIA | `04` Divergências; `01` Lacunas |
| TD-ARCH-007 | `FoldersController` e `FoldersManager` registram rotas duplicadas de pastas | Registro REST duplicado em `rest_api_init` | ALTA | `04` Divergências; `07` Pontos Críticos |
| TD-ARCH-008 | Server Nginx buildado no CI mas não implantado em dev/prd | Proxy unificado existe apenas no ambiente local | MÉDIA | `06` Componentes Legados; `06` Lacunas |

---

## Dívidas Aplicacionais

| ID | Descrição | Impacto | Criticidade | Evidência |
|---|---|---|---|---|
| TD-APP-001 | 28 endpoints órfãos consumidos pelo frontend sem `register_rest_route` no CMS | Funcionalidades referenciadas falham em runtime | ALTA | `04` Endpoints Órfãos; `07` Pontos Críticos |
| TD-APP-002 | 8 módulos funcionais em status PARCIAL | Capacidades incompletas entre camadas da stack | MÉDIA | `01` Resumo Executivo |
| TD-APP-003 | Onboarding divergente: CMS `options\|select\|status` vs frontend `current\|requests` | Fluxo de onboarding quebrado na store | ALTA | `04` Divergências; `02` Lacunas; `05` Divergências |
| TD-APP-004 | Colaboradores: frontend `/colaboradores` vs CMS sub-recursos de área/singular | CRUD de colaboradores via namespace raiz inoperante | ALTA | `04` Divergências; `05` Integrações Órfãs |
| TD-APP-005 | Document sharing: frontend `/document-sharing/*` sem controller REST no CMS | Compartilhamento via API dedicada inoperante | ALTA | `04` Divergências; `01` Lacunas |
| TD-APP-006 | Permission requests: `/permission-requests` sem controller CMS | Fluxo de solicitação/aprovação inoperante | ALTA | `01` Lacunas; `04` Endpoints Órfãos |
| TD-APP-007 | Analytics: `/analytics/dashboard` e `/admin/metrics` sem backend | Dashboard administrativo inoperante | MÉDIA | `01` Lacunas; `04` Endpoints Órfãos |
| TD-APP-008 | Auth `refresh` implementado em `AuthController` mas não registrado em rotas | Renovação de token via endpoint documentado inoperante | ALTA | `04` Divergências |
| TD-APP-009 | Módulos com evidência apenas frontend (Comunicados, Central de Colaboração, Convidados parcial) | Funcionalidades sem API/persistência dedicada | MÉDIA | `01` Lacunas |
| TD-APP-010 | `UserFoldersAutoCreationService` desabilitado em `Bootstrap.php` | Criação automática de pastas por usuário inativa | BAIXA | `01` Lacunas; `07` Componentes Legados |
| TD-APP-011 | `AnalyticsDashboardPage.vue.disabled` — rota com import desabilitado | Tela de analytics não acessível | BAIXA | `01` Lacunas |
| TD-APP-012 | Auditoria: frontend `/auditoria/singulares` vs CMS `/rbac/audit` | Consulta de auditoria por path divergente | MÉDIA | `04` Divergências; `05` Integrações Órfãs |

---

## Dívidas de Segurança

| ID | Descrição | Impacto | Criticidade | Evidência |
|---|---|---|---|---|
| TD-SEC-001 | Guards frontend permissivos — não aplicam `meta.roles` | Usuários autenticados acessam rotas sem checagem de role | CRÍTICA | `02` Lacunas; `07` Pontos Críticos |
| TD-SEC-002 | `canAccessRoute()` existe mas não é aplicado pelo guard ativo | Matriz de rotas RBAC do frontend sem efeito | CRÍTICA | `02` Lacunas |
| TD-SEC-003 | Endpoints públicos de dados organizacionais (`GET /singulares`, `/teams`) | Exposição de dados sem JWT | CRÍTICA | `04` Endpoints Públicos Sensíveis; `02` Lacunas |
| TD-SEC-004 | Endpoints de debug de documentos sem autenticação (`/documentos-debug`, `/documentos-fix-paths`) | Listagem e migração de documentos sem auth | CRÍTICA | `04` Endpoints Públicos Sensíveis |
| TD-SEC-005 | Divergência de roles: `team_administrator` (frontend) vs `team_owner` (CMS) | Autorização inconsistente por camada | ALTA | `02` Lacunas; `07` Pontos Críticos |
| TD-SEC-006 | `singular_owner` referenciado em `DocumentsController` — não canônico no `RBACService` | Check de upload com role inexistente no modelo canônico | ALTA | `02` Lacunas |
| TD-SEC-007 | Roles fantasmas no frontend (`federacao_owner`, `guest` em `menu-rbac.ts`) sem correspondência CMS | Menu e permissões baseados em roles não operacionais | MÉDIA | `02` Lacunas |
| TD-SEC-008 | `area_administrator` ausente em `ROLE_PERMISSIONS` de `auth-roles.ts` | Mapeamento incompleto de permissões frontend | MÉDIA | `02` Lacunas |
| TD-SEC-009 | `portal_rbac_manage` usada em `RBACController` mas ausente em `CANONICAL_CAPABILITIES` | Capability de administração RBAC fora do catálogo canônico | MÉDIA | `02` Lacunas |
| TD-SEC-010 | `/status` expõe lista de controllers registrados sem autenticação | Vazamento de metadados internos da API | MÉDIA | `04` Endpoints Públicos Sensíveis |
| TD-SEC-011 | Logout `reliableLogoutService` usa chave `auth_token` divergente de `VITE_TOKEN_STORAGE_KEY` | Invalidação de sessão no servidor pode falhar silenciosamente | ALTA | `05` Divergências |

---

## Dívidas de Dados

| ID | Descrição | Impacto | Criticidade | Evidência |
|---|---|---|---|---|
| TD-DAT-001 | Entidades virtuais sem persistência (PermissionRequest, OnboardingRequest frontend, Analytics, Comunicados) | Dados de negócio sem origem verificável no backend | ALTA | `03` Validação 1/5; `07` Pontos Críticos |
| TD-DAT-002 | CPT `team` e taxonomia `team` coexistem com uso operacional divergente | Ambiguidade de modelo de equipes | ALTA | `03` Lacunas; Validação 6 |
| TD-DAT-003 | ACF com slugs divergentes (`area` vs `portal_area`, `documento` vs `portal_documento`) | Metadados estruturados podem não aplicar aos CPTs/taxonomias corretos | MÉDIA | `03` Lacunas |
| TD-DAT-004 | `group_pc_area_informacoes` com `fields` vazio no JSON localizado | Configuração ACF de área incompleta | BAIXA | `03` Lacunas |
| TD-DAT-005 | Compartilhamento: metadado `portal_doc_sharing` existe; API `/document-sharing` ausente | Relacionamento de compartilhamento sem contrato REST confirmado | ALTA | `03` Lacunas; `04` Divergências |
| TD-DAT-006 | `audit_log`: insert em `AuditService`; `CREATE TABLE` não localizado no repositório | Schema de auditoria sem evidência de migração versionada | MÉDIA | `03` Lacunas |
| TD-DAT-007 | Fique por Dentro e Central de Colaboração sem entidade de dados confirmada | Módulos PARCIAIS sem modelo persistido | MÉDIA | `03` Validação 2 |
| TD-DAT-008 | Onboarding: CMS persiste em `user_meta`; frontend modela `OnboardingRequest` com endpoints distintos | Dois modelos de dados para o mesmo fluxo | ALTA | `03` Lacunas |

---

## Dívidas de Integração

| ID | Descrição | Impacto | Criticidade | Evidência |
|---|---|---|---|---|
| TD-INT-001 | 9 integrações órfãs no frontend (analytics, document-sharing, permission-requests, uploads/files, colaboradores, onboarding, organizations, zimbra-email, admin/metrics) | Chamadas HTTP sem destino no CMS | ALTA | `05` Integrações Órfãs |
| TD-INT-002 | Backend legado: 20 rotas em `api.php` com `backend/src/` ausente | Integração declarada sem implementação verificável | CRÍTICA | `04` Endpoints Legados; `06` Lacunas |
| TD-INT-003 | Plugin JWT (`jwt-auth-minimal`) em status PARCIAL coexistindo com JWT nativo | Validação alternativa de token com comportamento indefinido | CRÍTICA | `05` Integrações; `07` Componentes Legados |
| TD-INT-004 | Canais email (`wp_mail`) e webhook em status PARCIAL | Notificações por email/webhook podem não entregar | MÉDIA | `05` Notificações |
| TD-INT-005 | `/validate/zimbra-email` referenciado no frontend sem rota CMS | Validação de e-mail corporativo inoperante | MÉDIA | `05` Integrações Órfãs |
| TD-INT-006 | Namespaces mistos no frontend (`portaldecomunicacao/v1`, `jwt-auth/v1`, rotas WP legadas) | Múltiplos contratos de autenticação coexistindo | ALTA | `04` Divergências; `05` Divergências |
| TD-INT-007 | Múltiplos nomes de variáveis de ambiente para CMS (`VITE_CMS_BASE_URL`, `VITE_API_BASE`, `VITE_PORTAL_API_BASE`) | Risco de configuração incorreta entre ambientes | MÉDIA | `05` Divergências |

---

## Dívidas de Infraestrutura

| ID | Descrição | Impacto | Criticidade | Evidência |
|---|---|---|---|---|
| TD-INF-001 | MySQL externo sem container gerenciado nos compose ativos | Dependência crítica não versionada no repositório | CRÍTICA | `06` Lacunas; `07` Pontos Críticos |
| TD-INF-002 | Diretório `envs/` referenciado sem arquivos versionados | Configuração de ambiente não reproduzível pelo repositório | ALTA | `06` Lacunas |
| TD-INF-003 | `build-images.sh` referenciado no CI sem arquivo no repositório | Pipeline de build depende de script externo não localizado | ALTA | `06` Lacunas |
| TD-INF-004 | Redis em `docker-compose.cache.yml` isolado, sem integração ao stack principal | Cache proposto não operacional; arquivos `redis/` ausentes | MÉDIA | `06` Lacunas; Componentes Legados |
| TD-INF-005 | Variáveis sensíveis (`HML_ENV`, `PRD_ENV`, `PRD_SECRET`, credenciais registry) apenas em CI/CD | Configuração obrigatória sem origem localizada | ALTA | `06` Configurações; Validação 6 |
| TD-INF-006 | Ambiente `stage` compartilha configuração com `development` | Distinção operacional entre homologação e stage não evidenciada | MÉDIA | `06` Lacunas |
| TD-INF-007 | Local usa banco externo de homologação (MySQL removido do compose local) | Ambiente local acoplado a infraestrutura externa | MÉDIA | `06` Ambientes; Lacunas |

---

## Dívidas de Documentação

| ID | Descrição | Impacto | Criticidade | Evidência |
|---|---|---|---|---|
| TD-DOC-001 | Todos os documentos Discovery 01–07 com status APROVADO COM RESSALVAS | Base de conhecimento consolidada com ressalvas em todos os domínios | MÉDIA | Status Final de cada doc 01–07 |
| TD-DOC-002 | Arquivos de validação (`_validation/modules-validation.md`, `rbac-validation.md`) referenciados mas não localizados | Rastreabilidade de validação externa incompleta | BAIXA | Dependências obrigatórias dos prompts |
| TD-DOC-003 | Backend legado: persistência não verificável na camada de dados | Modelo de dados do serviço legado indocumentado | MÉDIA | `03` Lacunas |
| TD-DOC-004 | Capabilities e permissões frontend (`admin.full_access`, `dashboard.view`) sem espelho direto no CMS | Matriz de permissões frontend não alinhada ao catálogo canônico | MÉDIA | `02` Lacunas |

---

## Matriz de Risco

| ID | Categoria | Criticidade |
|---|---|---|
| TD-ARCH-001 | Arquitetural | CRÍTICA |
| TD-ARCH-002 | Arquitetural | ALTA |
| TD-ARCH-003 | Arquitetural | CRÍTICA |
| TD-ARCH-004 | Arquitetural | MÉDIA |
| TD-ARCH-005 | Arquitetural | ALTA |
| TD-ARCH-006 | Arquitetural | MÉDIA |
| TD-ARCH-007 | Arquitetural | ALTA |
| TD-ARCH-008 | Arquitetural | MÉDIA |
| TD-APP-001 | Aplicacional | ALTA |
| TD-APP-002 | Aplicacional | MÉDIA |
| TD-APP-003 | Aplicacional | ALTA |
| TD-APP-004 | Aplicacional | ALTA |
| TD-APP-005 | Aplicacional | ALTA |
| TD-APP-006 | Aplicacional | ALTA |
| TD-APP-007 | Aplicacional | MÉDIA |
| TD-APP-008 | Aplicacional | ALTA |
| TD-APP-009 | Aplicacional | MÉDIA |
| TD-APP-010 | Aplicacional | BAIXA |
| TD-APP-011 | Aplicacional | BAIXA |
| TD-APP-012 | Aplicacional | MÉDIA |
| TD-SEC-001 | Segurança | CRÍTICA |
| TD-SEC-002 | Segurança | CRÍTICA |
| TD-SEC-003 | Segurança | CRÍTICA |
| TD-SEC-004 | Segurança | CRÍTICA |
| TD-SEC-005 | Segurança | ALTA |
| TD-SEC-006 | Segurança | ALTA |
| TD-SEC-007 | Segurança | MÉDIA |
| TD-SEC-008 | Segurança | MÉDIA |
| TD-SEC-009 | Segurança | MÉDIA |
| TD-SEC-010 | Segurança | MÉDIA |
| TD-SEC-011 | Segurança | ALTA |
| TD-DAT-001 | Dados | ALTA |
| TD-DAT-002 | Dados | ALTA |
| TD-DAT-003 | Dados | MÉDIA |
| TD-DAT-004 | Dados | BAIXA |
| TD-DAT-005 | Dados | ALTA |
| TD-DAT-006 | Dados | MÉDIA |
| TD-DAT-007 | Dados | MÉDIA |
| TD-DAT-008 | Dados | ALTA |
| TD-INT-001 | Integração | ALTA |
| TD-INT-002 | Integração | CRÍTICA |
| TD-INT-003 | Integração | CRÍTICA |
| TD-INT-004 | Integração | MÉDIA |
| TD-INT-005 | Integração | MÉDIA |
| TD-INT-006 | Integração | ALTA |
| TD-INT-007 | Integração | MÉDIA |
| TD-INF-001 | Infraestrutura | CRÍTICA |
| TD-INF-002 | Infraestrutura | ALTA |
| TD-INF-003 | Infraestrutura | ALTA |
| TD-INF-004 | Infraestrutura | MÉDIA |
| TD-INF-005 | Infraestrutura | ALTA |
| TD-INF-006 | Infraestrutura | MÉDIA |
| TD-INF-007 | Infraestrutura | MÉDIA |
| TD-DOC-001 | Documentação | MÉDIA |
| TD-DOC-002 | Documentação | BAIXA |
| TD-DOC-003 | Documentação | MÉDIA |
| TD-DOC-004 | Documentação | MÉDIA |

---

## Impacto por Domínio

| Domínio | Quantidade de Dívidas |
|---|---|
| Aplicação | 20 |
| Dados | 8 |
| Segurança | 11 |
| Integrações | 10 |
| Infraestrutura | 8 |

*Nota: dívidas arquiteturais (8) e de documentação (4) impactam múltiplos domínios; contagem acima atribui cada ID ao domínio primário de impacto.*

---

## Dependências Críticas

| Dependência | Risco |
|---|---|
| MySQL externo | Indisponibilidade impede login, RBAC, documentos e toda persistência WordPress (`06`) |
| Zimbra (IMAP/SMTP/SOAP) | Indisponibilidade impede autenticação corporativa em produção (`05`, `06`) |
| GFS / bind mount uploads | Perda ou indisponibilidade impede upload e download de documentos (`06`) |
| Harbor Registry | Indisponibilidade impede build e deploy de imagens (`06`) |
| WordPress + MU-plugin | Indisponibilidade impede toda API `portaldecomunicacao/v1` (`06`) |
| Traefik (`traefik-public`) | Indisponibilidade impede acesso público em dev/prd (`06`) |

---

## Itens que Bloqueiam Evolução Arquitetural

| ID | Motivo |
|---|---|
| TD-ARCH-001 | Duas APIs paralelas (CMS + backend legado) impedem definição de boundary único |
| TD-ARCH-003 | Múltiplos mecanismos JWT impedem contrato de autenticação unificado |
| TD-SEC-001 | Guards permissivos impedem modelo de autorização confiável no frontend |
| TD-SEC-002 | Matriz de rotas RBAC sem efeito impede enforcement consistente |
| TD-SEC-003 | Endpoints organizacionais públicos impedem endurecimento de segurança |
| TD-SEC-004 | Endpoints de debug públicos impedem fechamento de superfície de ataque |
| TD-INF-001 | Banco externo não versionado impede reprodutibilidade e evolução de schema |
| TD-INT-002 | Backend legado sem `src/` impede descomissionamento ou consolidação segura |
| TD-INT-003 | JWT plugin parcial impede consolidação do fluxo de autenticação |

---

## Resumo Consolidado da Discovery

| Documento | Resultado |
|---|---|
| 01 — Módulos | 27 módulos (18 ATIVO, 8 PARCIAL, 1 LEGADO); endpoints órfãos e backend sem `src/` — **APROVADO COM RESSALVAS** |
| 02 — RBAC | 6 roles, 47 capabilities; guards permissivos e divergências frontend/backend — **APROVADO COM RESSALVAS** |
| 03 — Dados | 22 entidades; 5 virtuais sem persistência; CPT/taxonomia `team` divergente — **APROVADO COM RESSALVAS** |
| 04 — Endpoints | ~98 CMS, 28 órfãos, 20 legados; endpoints públicos sensíveis — **APROVADO COM RESSALVAS** |
| 05 — Integrações | 33 integrações; 9 órfãs; duplicidades JWT/notificações/backend — **APROVADO COM RESSALVAS** |
| 06 — Infraestrutura | 3 ambientes; MySQL externo; envs e scripts ausentes — **APROVADO COM RESSALVAS** |
| 07 — Arquitetura | Consolidação coerente com 01–06; 12 pontos críticos documentados — **APROVADO COM RESSALVAS** |

---

## Resultado da Validação

### Validação 1

Todas as dívidas possuem evidência?

**SIM** — cada ID referencia seção específica dos documentos 01–07.

### Validação 2

Todas as dívidas possuem categoria?

**SIM** — 7 categorias obrigatórias cobertas (Arquitetural, Aplicacional, Segurança, Dados, Integração, Infraestrutura, Documentação).

### Validação 3

Todas as dívidas possuem criticidade?

**SIM** — classificação CRÍTICA, ALTA, MÉDIA ou BAIXA em todos os 57 itens.

### Validação 4

Existem itens sem evidência?

**NÃO** — nenhum item inferido além do documentado em 01–07.

### Validação 5

O documento utiliza exclusivamente informações dos documentos 01–07?

**SIM** — sem nova descoberta; sem proposta de correção.

### Validação 6

O documento representa fielmente o estado atual do sistema?

**SIM** — consolidação alinhada aos status APROVADO COM RESSALVAS dos documentos fonte.

---

## Status Final

**APROVADO COM RESSALVAS**

Ressalvas: 9 dívidas CRÍTICAS concentradas em segurança (guards, endpoints públicos), autenticação (JWT duplicado), coexistência backend legado e dependência de MySQL externo não versionado. Consolidação completa da camada Discovery sem proposta de correção ou roadmap.
