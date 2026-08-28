# Auditoria de Reconciliação — Jira PUC × Estado Real do Projeto

| Campo | Valor |
|-------|-------|
| Tipo | Auditoria **somente leitura** (comparativa) |
| Data | 2026-08-27 |
| Autor | Claude Code (modo auditoria) |
| Objeto | Projeto Jira `PUC` — "Portal Unimed Ceará" (`unimedceara.atlassian.net`) |
| Referência de verdade | Código do repositório, `specs/`, `docs/` (nesta ordem) |
| Alterações executadas | **Nenhuma** (Jira, código e specs intactos) |

> **Convenção de marcação** — ao longo do documento:
> **[FATO]** = verificável agora no repositório ou no Jira; **[INFERÊNCIA]** = leitura do autor a partir dos fatos; **[RECOMENDAÇÃO]** = ação proposta, **não executada**.

---

## 1. Resumo executivo

**[FATO]** O projeto Jira `PUC` contém **217 issues**: 8 Epics, 41 Tarefas e 168 Subtarefas. A issue mais recém-criada é de **2026-01-14** (PUC-211..PUC-217); a última edição de qualquer issue é de **2026-04-01** (reparent de PUC-217). Desde então o Jira está **congelado**.

**[FATO]** No mesmo período (junho–agosto de 2026) o repositório recebeu a maior parte da implementação: Foundation da aplicação, CRUD de Organização (Singular, Área, Equipe, Colaborador), Autenticação Zimbra + sessão, Primeiro Acesso, Área do Colaborador, Federação, Gestão Documental (consulta/download), além de toda a reestruturação documental (`specs/`, `docs/governance/`), separação em repositórios (DEC-015) e pipeline de CI.

**[INFERÊNCIA]** O Jira PUC representa uma **fotografia de discovery/planejamento de meados de 2025 a início de 2026**, feita antes do fluxo atual orientado a `specs/features/<slug>/` + `feature.yaml`. Ele **não acompanhou** a execução. O resultado:

- **Epics desatualizados**: 6 dos 8 Epics estão "Tarefas pendentes" embora a maioria de seus filhos esteja "Concluído" ou já implementada no código.
- **Aceite nunca fechado**: dezenas de Tarefas "Concluído" têm **todas as subtarefas** (critérios de aceite) ainda "Tarefas pendentes" — padrão sistemático, não pontual.
- **Duplicação**: CRUD de Colaborador aparece em 3+ issues; Singular em 2; Área em 3; "gerenciar usuários/papéis" em 4.
- **GAP estrutural**: features inteiras implementadas e fechadas (`FT-EQUIPE`, `FT-SESSION`, `FT-FEDERACAO-COLABORADOR`, Foundation, tema, CI, DEC-015, E2E) **não têm nenhuma issue** no Jira.
- O próprio **framework de trabalho** (specs/DoR/DoD/feature.yaml, Etapas 1–5 do MVP) não tem representação no Jira.

**[INFERÊNCIA]** O Jira PUC, no estado atual, **não é utilizável como espelho de progresso**. Ele serve hoje como um repositório de requisitos brutos de origem (útil como insumo histórico), mas divergiu do projeto a ponto de induzir a erro quem o consultar para saber "o que está pronto".

**[RECOMENDAÇÃO]** Rebaselinar o Jira PUC para refletir **produto → épicos → features → trabalho**, com granularidade de feature/capacidade, mantendo `specs/features/<slug>/` como detalhamento. Ver §7 e §8.

---

## 2. Estado atual do projeto (repositório)

### 2.1 Fonte

**[FATO]** `docs/governance/01-project-status.md` (atualizado 2026-08-26) é o índice de estado. Status geral: *"Baseline documental APROVADA COM RESSALVAS; Etapa 2 avançada; Etapas 3–5 pendentes"*. Andamento estimado ali: ~45%.

### 2.2 Features (`specs/features/*/feature.yaml` + evidência de código)

| Feature (slug) | ID | `feature.yaml` | Evidência de código | Etapa MVP |
|---|---|---|---|---|
| authentication | FT-AUTH | `specification: APPROVED` (esquema legado) → **closed** | `AuthController`, `PrimeiroAcessoController`, IdP Zimbra client, sessão JWT, `AuthSessaoEntity` | 2 |
| session | FT-SESSION | `specification: APPROVED` → **closed** | Contexto Ativo, `AdminSessionController`, `session.store` (FE) | 2 |
| singular | FT-SINGULAR | `specification: APPROVED` → **closed** | `SingularController` + service + `SingularEntity`; 5 páginas FE + E2E | 2 |
| area | FT-AREA | `specification: APPROVED` → **closed** | `AreaController` + services + `AreaEntity` | 2 |
| equipe | FT-EQUIPE | `specification: APPROVED` → **closed** | `EquipeController` + services + `EquipeEntity`; 5 páginas FE + E2E | 2 |
| colaborador | FT-COLABORADOR | **DONE** (2026-08-26) | `ColaboradorController` + services + `ColaboradorEntity`; 5 páginas FE; E2E `AT-FE-COLABORADOR-001..005` | 2 |
| primeiro-acesso | FT-PRIMEIRO-ACESSO | **DONE** (2026-08-26) | `PrimeiroAcessoApplicationService` (9 testes de aceite); `usePrimeiroAcessoPage.ts` + página | 2 |
| area-colaborador | FT-AREA-COLABORADOR | **DONE** (2026-08-26) | páginas/composables `area-colaborador/*`, rotas; Hub + Dados da Área + Equipes da Área | 2/3 |
| federacao-colaborador | FT-FEDERACAO-COLABORADOR | **DONE** | `FederacaoController` + service; `federacao.service` (FE), páginas Federação + E2E | 2 |
| federacao | *(sem `feature.yaml`)* | só `specification.md` + `api.md` | coberto na prática por FT-FEDERACAO-COLABORADOR | 2 |
| arquivos | FT-DOCUMENTO | **DONE** (somente leitura) | `DocumentoController`/`PastaController` + services; `PermissaoPasta` multi-nível; entidades `Documento/Versao/ArquivoBinario/Pasta`; página FE de listagem+download | 3 |
| documento-upload | FT-DOCUMENTO-UPLOAD | **DRAFT** (não versionado em git ainda — `?? specs/features/documento-upload/`) | entidades de persistência existem; escopo de escrita ainda não implementado | 3 |
| perfil | FT-PERFIL | **DRAFT** | `perfil.routes.ts` + `pages/perfil/` (esqueleto) | 2/3 |
| home | FT-HOME | **DRAFT** | `/app` é spike sancionado sem DoR (ver memória do projeto) | 1/4 |
| noticia | FT-NOTICIA | **DRAFT** | nenhuma — sem backend/frontend de notícia/comunicado | 4 |
| servicos | FT-SERVICOS | **DRAFT** | inerte (ver memória do projeto) | — |

### 2.3 Componentes

**[FATO] Backend** (`backend/src/main/java/.../portalcomunicacao/`): bounded contexts `organization` (Federacao, Singular, Area, Equipe), `accesscontrol` (Auth, PrimeiroAcesso, Colaborador, AdminSession, `PapelAtribuicao*` RBAC — adicionado 2026-08-20), `documento` (Pasta, Documento, versão, binário, `PermissaoPasta`). Persistência Oracle; 8 entidades JPA de domínio + auditáveis. Observabilidade: Correlation ID + Actuator (parcial). CI: `.github/workflows/backend.yml` ("Opção B", exclui testes de integração Oracle).

**[FATO] Frontend** (`frontend/src/`): Foundation (app-shell, router com guards, `session.store`, design system, persistência de tema — DEC-FA-005), serviços HTTP (axios, CSRF, correlation-id, interceptors), serviços de domínio (auth, organization/{singular,area,equipe,federacao,colaborador}, documento). Páginas: `auth`, `primeiro-acesso`, `organization/{singular,equipe,colaborador}` (Hub/Lista/Criar/Detalhe/Editar), `federacao`, `area-colaborador`, `perfil`, `admin`, `app`. E2E Playwright: `bootstrap`, `federacao`, `singular`, `equipe`, `app-shell`, `colaborador`.

**[FATO] Banco** (`database/`): baseline Oracle homologada; 6 entidades JPA alinhadas (Etapa 4 auditada 2026-08-14). `docker-compose.yml` usa `gvenzl/oracle-free` + MinIO (não há mais Postgres, não há serviço WordPress).

**[FATO] CMS**: `wordpress/` no monorepo está **vazio**. Por DEC-015 (2026-08-26), o CMS foi para repositório próprio (`portal-comunicacao-cms`) com scaffold inicial; `docs/architecture/decisions/DS-RECONSTRUCTION-SCOPE-01.md` trata a descoberta do CMS legado como "RETIRE".

### 2.4 Decisões consolidadas relevantes para esta auditoria

**[FATO]**
- **DEC-015** (2026-08-26): fim do monorepo único; `backend/`, `frontend/`, `cms` em repositórios GitLab próprios; `specs/`/`docs/`/`database/` permanecem no monorepo (destino final em aberto).
- **DEC-007**: banco Oracle (supersede menções a PostgreSQL).
- **DEC-FA-001**: Primeiro Acesso / Contexto Ativo.
- **DEC-FA-005**: persistência de preferência de tema.
- **AUDITORIA-DS-FIGMA-01**: entrada de menu "Áreas" **removida** em favor de "Federação" (2026-08-26); `TK-AREA-COLAB-004` superseded.
- MVP oficial = Etapas 1–5 (`docs/backlog/04-mvp-scope.md`). Excluídos: Campanhas, Mensagens, Painel de Métricas (FEATURE-044), Descomissionamento (Etapa 6).

### 2.5 Trabalho em andamento

**[FATO]** Branch atual `feature/area-colaborador`. Working tree: ajustes de testes (auth/session/federação/colaborador), spec nova `documento-upload/` não versionada. Próximas ações do índice de status: commitar `docker-compose.yml`/CI, validar ambiente local Oracle (Etapa 5).

---

## 3. Estado atual do Jira (PUC)

**[FATO]**

| Métrica | Valor |
|---|---|
| Total de issues | 217 |
| Epics | 8 |
| Tarefas | 41 |
| Subtarefas | 168 |
| Tarefas "Concluído" | 18 |
| Tarefas "Tarefas pendentes" | 23 |
| Epics "Concluído" | 0 |
| Subtarefas "Concluído" | 31 (de 168) |
| Issue mais recente (criação) | PUC-217 — 2026-01-14 |
| Última edição de qualquer issue | 2026-04-01 |
| Fluxo de status | apenas 2 estados: "Tarefas pendentes" → "Concluído" (sem "Em andamento") |

### 3.1 Epics

| Epic | Título | Status Jira | Nº filhos diretos |
|---|---|---|---|
| PUC-16 | Autenticação & Acesso (Zimbra + RBAC) | Tarefas pendentes | 7 tarefas |
| PUC-17 | Administração (usuários: colaboradores, áreas, singulares) | Tarefas pendentes | 13 tarefas |
| PUC-23 | Documentos & Mídias (armazenamento, regras, auditoria) | Tarefas pendentes | 6 tarefas |
| PUC-29 | Login & Navegabilidade | Tarefas pendentes | 2 tarefas |
| PUC-33 | Observabilidade & Governança | Tarefas pendentes | 4 tarefas |
| PUC-34 | CMS/WordPress (integrações e boas práticas) | Tarefas pendentes | 3 tarefas |
| PUC-35 | Área do Colaborador | Tarefas pendentes | 2 tarefas |
| PUC-49 | Bug | Tarefas pendentes | 1 tarefa (PUC-50) |

**[FATO]** `PUC-49` é do tipo **Epic** mas seu conteúdo é "Bug". **[FATO]** `PUC-20`, `PUC-77` e `PUC-217` são Tarefas **sem Epic pai** (órfãs). **[FATO]** `PUC-217` ("Revisão de Requisitos") recebeu subtarefas reparentadas de vários Epics (PUC-121, PUC-129, PUC-131, PUC-140, PUC-141, PUC-213).

### 3.2 Padrão de aceite não fechado

**[FATO]** Exemplos de Tarefa "Concluído" cujas subtarefas de critério de aceite permanecem "Tarefas pendentes": PUC-1 (subt. PUC-2..5), PUC-6 (PUC-107..110), PUC-7 (PUC-111..114), PUC-8 (PUC-9..13), PUC-22 (PUC-140, PUC-141), PUC-43 (PUC-45..48), PUC-59 (PUC-60..64), PUC-65 (PUC-66..70), PUC-77 (PUC-78..82), PUC-19 (PUC-129 reparentada). Apenas PUC-12, PUC-15, PUC-18, PUC-20, PUC-51, PUC-71 têm subtarefas majoritariamente "Concluído".

---

## 4. Matriz de reconciliação

Granularidade: Epic e Tarefa (as 168 subtarefas são tratadas em agregado — ver §3.2 e §6). Colunas: **Correspondência** no repositório/specs · **Estado Jira** · **Estado Real [FATO]** · **Diagnóstico [INFERÊNCIA]** · **Recomendação [RECOMENDAÇÃO, não executada]**.

### 4.1 Epic PUC-16 — Autenticação & Acesso

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-16 (Epic) | FT-AUTH + FT-SESSION + FT-PRIMEIRO-ACESSO + RBAC (`PapelAtribuicao`) | Pendente | Núcleo implementado e fechado; RBAC de delegação parcial (serviço BE existe, sem tela admin) | Desatualizado | Manter como Epic "Controle de Acesso"; marcar núcleo concluído; abrir item separado p/ tela de delegação |
| PUC-1 — Login corporativo via email | FT-AUTH (`AuthController`, IdP Zimbra); login FE | Concluído | Login por e-mail + IdP implementado. **Sem "seleção de domínio"** (descrição pede) | Parcialmente correto | Fechar aceite; mover "seleção de domínio" p/ PUC-30 (ou descartar — ver 4.4) |
| PUC-6 — Redirecionamento por papel | Router guards + Contexto Ativo (FE); mapeamento de rota por papel | Concluído | Redirecionamento por papel/rota implementado (ajustado no bug PUC-50) | Alinhado (aceite aberto) | Fechar subtarefas PUC-107..110 |
| PUC-7 — Papel padrão no primeiro acesso | FT-PRIMEIRO-ACESSO (DEC-FA-001) | Concluído | Wizard de primeiro acesso implementado e testado (9 testes de aceite) | Alinhado, subrepresentado | Renomear p/ "Primeiro Acesso / Contexto Ativo"; vincular a FT-PRIMEIRO-ACESSO |
| PUC-8 — Hierarquia de concessão de papéis (RBAC) | `PapelAtribuicaoService`, `PapelAtribuicaoEntity` (BE); matriz RBAC (`specs`) | Concluído | Matriz e escopo por Federação/Singular/Área/Equipe no BE; **sem UI de concessão** | Parcialmente correto / duplicado c/ PUC-59, PUC-65 | Consolidar PUC-8+PUC-59+PUC-65 em 1 feature "Delegação de papéis (RBAC)"; manter BE done, UI pendente |
| PUC-12 — Esqueci minha senha (Zimbra) | `specs/features/authentication` — **explicitamente fora de escopo** (responsabilidade do Zimbra) | Concluído | Só há link para URL do Zimbra | Obsoleto como feature de produto (é 1 link) | Arquivar/absorver em PUC-1 como nota |
| PUC-31 — Responsividade/acessibilidade da tela de login | Login FE (`pages/auth`) | Pendente | Tela de login existe e é responsiva; conformidade WCAG não auditada formalmente | Parcialmente correto | Manter como item de qualidade; validar contra checklist AA |
| PUC-206 — Papéis do sistema (ADMIN, SINGULAR, ÁREA, COLABORADOR, CONVIDADO) | `docs/domain/` RBAC; `PapelEntity` seed | Pendente | Papéis definidos no domínio e no seed; **CONVIDADO** presente no catálogo, uso mínimo | Desatualizado (já definido) | Fechar; é definição, não trabalho pendente |
| PUC-211 — RBAC baseline: matriz de permissões por papel | `specs` matriz RBAC; `docs/discovery/02-current-rbac.md` | Concluído | Matriz baseline existe | Alinhado | Manter fechado |

### 4.2 Epic PUC-17 — Administração

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-17 (Epic) | FT-SINGULAR, FT-AREA, FT-EQUIPE, FT-COLABORADOR + shell admin | Pendente | CRUD de Singular/Área/Equipe/Colaborador implementado e fechado; relatórios não | Desatualizado | Renomear "Organização Corporativa"; separar "Relatórios" (PUC-21) p/ pós-núcleo |
| PUC-15 — [Admin] CRUD de Colaborador | FT-COLABORADOR (DONE) — BE + 5 páginas FE + E2E | Concluído | Implementado e fechado | Alinhado | Manter fechado; é o card canônico de Colaborador |
| PUC-18 — [Admin] CRUD de Singulares | FT-SINGULAR (closed) | Concluído | Implementado e fechado | Alinhado, **duplicado com PUC-71** | Manter PUC-18; marcar PUC-71 como duplicata |
| PUC-19 — [Gestor de Área] CRUD de Áreas e Colaboradores | FT-AREA + FT-COLABORADOR | Concluído | CRUD implementado; recorte "Gestor de Área" (escopo) parcialmente coberto | Parcialmente correto / **sobreposto** a PUC-15, PUC-77, PUC-83 | Consolidar a família Área/Colaborador (ver 4.7) |
| PUC-20 — Painel do Administrador (UX funcional) | app-shell, breadcrumbs, `pages/admin/index.vue`, `PUC-51` | Concluído (órfã) | Shell + breadcrumbs + toolbar implementados; "cards-resumo" mínimos | Parcialmente correto | Vincular ao Epic Fundação; validar cards-resumo |
| PUC-21 — Relatórios/Dashboard | — | Pendente | Não implementado | Sem correspondência (trabalho futuro legítimo) | Manter como feature futura fora do núcleo MVP |
| PUC-51 — Refatorar página raiz conforme Design System | `/app` spike + app-shell + reconciliação de rodapé/Home (commits `baef86d`, `cef1037`) | Concluído | Página raiz/app-shell alinhada ao DS; tema persistido | Alinhado | Manter fechado; mover p/ Epic Fundação |
| PUC-59 — [Admin] Permissões (RBAC): gerenciar permissões | `PapelAtribuicaoService` (BE) | Concluído | BE de atribuição existe; **sem tela de concessão/revogação** | Parcialmente correto / **duplicado** c/ PUC-8, PUC-65 | Consolidar em 1 feature "Delegação de papéis" |
| PUC-65 — [REVISÃO] Usuários: gerenciar acesso ao portal | FT-COLABORADOR (habilitar/desabilitar) + RBAC | Concluído | Habilitar/desabilitar colaborador coberto; export CSV não | Parcialmente correto / **duplicado** | Absorver em PUC-15 + feature "Delegação de papéis" |
| PUC-71 — Singulares: gerenciar cadastro e responsáveis | FT-SINGULAR | Concluído | Implementado | **Duplicata de PUC-18** | Marcar duplicata de PUC-18 |
| PUC-77 — [Admin] CRUD de Áreas | FT-AREA | Concluído (órfã) | Implementado | **Duplicata de PUC-19** (parte Área); órfã | Marcar duplicata; vincular a Epic |
| PUC-83 — Colaboradores: gerenciar colaboradores e vínculos | FT-COLABORADOR (vínculos área/singular) | Pendente | Vínculos implementados em FT-COLABORADOR | Desatualizado (já feito) / **duplicado** c/ PUC-15, PUC-19 | Fechar como coberto por FT-COLABORADOR |
| PUC-89 — Colaboradores: filtro de busca | FT-COLABORADOR (lista/filtros) | Pendente | Lista + filtros implementados na página de Colaborador; persistência de filtro na URL não confirmada | Parcialmente correto | Rebaselinar contra a página real; fechar o que já existe |
| PUC-95 — Colaboradores: listar resultados com detalhes e papéis | `ColaboradorListPage.vue` | Pendente | Listagem com colunas/paginação implementada; export CSV não | Parcialmente correto | Idem PUC-89 |
| PUC-101 — Colaboradores: editar vínculos singular/área | FT-COLABORADOR (`ColaboradorEditPage`) | Pendente | Edição de vínculos implementada | Desatualizado (já feito) | Fechar como coberto |

### 4.3 Epic PUC-23 — Documentos & Mídias

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-23 (Epic) | FT-DOCUMENTO (DONE, leitura) + FT-DOCUMENTO-UPLOAD (DRAFT) | Pendente | Consulta + download + `PermissaoPasta` multi-nível implementados; upload/dedup/webp/painel não | Parcialmente correto | Renomear "Gestão Documental"; refletir entrega em fases (leitura ✓ / escrita / avançado) |
| PUC-22 — Upload com escopo (público/privado) | FT-DOCUMENTO-UPLOAD (DRAFT) — escopo **difere** (admin-escopado, sem escolha pública/privada pelo colaborador) | Concluído | **Upload não implementado**; a spec DONE (FT-DOCUMENTO) é somente leitura | **Incorreto** ("Concluído" não bate) / parcialmente obsoleto | Reabrir/rebaselinar contra FT-DOCUMENTO-UPLOAD; visibilidade é via `PERMISSAO_PASTA`, não toggle no upload |
| PUC-24 — Organização por singular/área/colaborador | `PermissaoPasta` (FEDERACAO/SINGULAR/AREA/EQUIPE) | Pendente | Modelo multi-nível implementado (sem grant individual por colaborador) | Parcialmente correto | Fechar o que FT-DOCUMENTO cobre; grant individual = futuro |
| PUC-25 — Deduplicação via hash | `ArquivoBinarioEntity` tem hash; dedup ativa não implementada | Pendente | Hash persistido; lógica de dedup/reuso não | Sem correspondência funcional (futuro) | Manter como feature futura (escrita) |
| PUC-26 — Auditoria e rastreabilidade de documentos | `AuditableEntity`; trilha específica de documento não | Pendente | Parcial (auditoria base de entidade) | Parcialmente correto | Manter; especificar em FT futura |
| PUC-27 — Imagens: conversão automática para WebP | — | Pendente | Não implementado | Sem correspondência (futuro; `docs/domain` menciona) | Manter como feature futura |
| PUC-28 — Painel de controle de documentos | — | Pendente | Não implementado (sem tela administrativa) | Sem correspondência (futuro) | Manter como feature futura |

### 4.4 Epic PUC-29 — Login & Navegabilidade

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-29 (Epic) | Foundation FE (shell, router, breadcrumbs) | Pendente | Navegação/breadcrumbs/toolbar implementados (E2E `app-shell`) | Desatualizado | Absorver em Epic Fundação; fechar o que está pronto |
| PUC-30 — Campo de e-mail com seleção de domínio | — | Pendente | Login usa campo de e-mail simples; **sem seletor de domínio** | Sem correspondência; decisão de produto não registrada | Decisão de produto necessária: implementar ou descartar. Não está na spec FT-AUTH atual |
| PUC-32 — Navegação consistente (breadcrumbs + ações) | app-shell, breadcrumb store, back-button (memória do projeto) | Pendente | Implementado (breadcrumb-driven back button, toolbar) | Desatualizado (já feito) | Fechar |

### 4.5 Epic PUC-33 — Observabilidade & Governança

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-33 (Epic) | Correlation ID + Actuator; `docs/governance/`; CI | Pendente | Observabilidade parcial; governança documental amplamente reestruturada (W0–W2) | Parcialmente correto; **muito trabalho real não representado** | Renomear "Plataforma & Governança"; puxar GAPs (CI, DEC-015, reestrutura documental) |
| PUC-36 — Coleção Postman sempre atualizada | `docs/api/postman/Portal.postman_collection.json` | Pendente | Coleção existe; automação de atualização em CI não | Parcialmente correto | Manter; automação = item pequeno |
| PUC-37 — Limpeza e padronização de estrutura | Reestruturação W0–W2 (`docs/audit/12`, `structural-simplification-plan-w2.md`), remoção de `.cursor/` | Pendente | **Amplamente executado** (D1–D9 parciais) | Desatualizado | Rebaselinar contra `docs/audit/14-...`; fechar o aplicado |
| PUC-38 — Logs por serviço e health checks | Actuator/health; logging + Correlation ID | Pendente | Health check e logging básicos; dashboards/alertas não | Parcialmente correto | Manter parcial |
| PUC-39 — Consolidação de portas e redes | `docker-compose.yml` (rede `portal-network`, volumes) | Pendente | Compose consolidado (Oracle + MinIO); documentação de bindings parcial | Parcialmente correto | Manter parcial |

### 4.6 Epic PUC-34 — CMS/WordPress

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-34 (Epic) | Repositório `portal-comunicacao-cms` (DEC-015); `DS-RECONSTRUCTION-SCOPE-01` = "RETIRE" | Pendente | CMS fora do monorepo; só scaffold; direção é reconstrução, não hardening do legado | **Potencialmente obsoleto** na forma atual | Redefinir contra DEC-015 + escopo de reconstrução; provavelmente novo Epic "CMS (repo próprio)" com escopo mínimo |
| PUC-40 — Desativar duplicação nativa do WordPress | — (legado) | Pendente | N/A no monorepo atual | Obsoleto (assume WP legado no monorepo) | Descartar ou reescrever p/ o CMS novo |
| PUC-41 — Plugin API Unificada | — | Pendente | N/A | Obsoleto (assume arquitetura legada) | Descartar ou reescrever |
| PUC-42 — Volumes externos p/ uploads/plugins/themes | `docker-compose` do repo CMS | Pendente | Fora deste repo | Obsoleto aqui; pode valer no repo CMS | Mover conceito p/ backlog do repo CMS |

### 4.7 Epic PUC-35 — Área do Colaborador

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-35 (Epic) | FT-AREA-COLABORADOR (DONE) + FT-FEDERACAO-COLABORADOR (DONE) + FT-DOCUMENTO + FT-PERFIL (DRAFT) | Pendente | Hub, Dados da Área, Equipes da Área, Federação navegável e Arquivos implementados; Perfil em DRAFT | **Desatualizado** (quase tudo feito) | Marcar Epic majoritariamente concluído; abrir só "Perfil do colaborador" |
| PUC-43 — Compartilhar arquivos (público/privado) | FT-DOCUMENTO (leitura) — **compartilhamento pelo colaborador está fora de escopo** | Concluído | Colaborador **lê/baixa**; não compartilha/publica | **Incorreto** (o "Concluído" descreve capacidade não entregue) | Rebaselinar: "Consultar e baixar arquivos da Área" = ✓; "compartilhar" = futuro |
| PUC-44 — Editar perfil do colaborador | FT-PERFIL (DRAFT) — `perfil.routes.ts`, `pages/perfil/` | Pendente | Rota/página esqueleto; não funcional | Alinhado (pendente) | Manter; promover FT-PERFIL de DRAFT quando priorizado |

### 4.8 Epic PUC-49 — Bug

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-49 (Epic "Bug") | — | Pendente | Container de bugs | Uso indevido do tipo Epic | Substituir por fluxo de issues tipo Bug (sem Epic guarda-chuva) |
| PUC-50 — Redirecionamento indevido /singular | Router guards / redirect por papel (relacionado a PUC-6; memória "Singular/Serviços gap") | Concluído | Corrigido | Alinhado | Manter fechado; reassociar a Epic Controle de Acesso |

### 4.9 PUC-217 — Revisão de Requisitos (órfã)

| Jira | Correspondência | Estado Jira | Estado Real | Diagnóstico | Recomendação |
|---|---|---|---|---|---|
| PUC-217 | Atividade de discovery/revisão (2026-01 a 2026-04) | Pendente (órfã) | Bucket com subtarefas reparentadas de vários Epics | Ruído organizacional | Encerrar; redistribuir subtarefas úteis, descartar o resto |

---

## 5. GAPs encontrados (trabalho real sem representação adequada no Jira)

**[FATO]** — os itens abaixo existem no repositório/specs e **não têm issue correspondente** (ou têm apenas menção lateral):

| # | Trabalho no projeto | Evidência | Representação no Jira |
|---|---|---|---|
| G1 | **Foundation da Plataforma** (app-shell, router+guards, `session.store`, design system, HTTP client, CSRF, correlation-id) | `frontend/src/services/*`, `frontend/src/router/*`, EPIC-001 em `specs` | Nenhuma (só PUC-20/PUC-51 tangenciam) |
| G2 | **FT-EQUIPE** — CRUD completo de Equipe (BE + 5 páginas FE + E2E) | `EquipeController`, `pages/organization/equipe/*`, `equipe.spec.ts` | **Nenhuma** — Jira só tem Singular/Área/Colaborador |
| G3 | **FT-SESSION** — Contexto Ativo / sessão organizacional | `AdminSessionController`, `session.store`, `feature.yaml` closed | Nenhuma (PUC-7 tangencia) |
| G4 | **FT-FEDERACAO / FT-FEDERACAO-COLABORADOR** — navegação da Federação (substituiu menu "Áreas") | `FederacaoController`, `federacao.service`, `federacao.spec.ts`, AUDITORIA-DS-FIGMA-01 | Nenhuma |
| G5 | **FT-AREA-COLABORADOR** — Hub + Dados da Área + Equipes da Área | `pages/area-colaborador/*`, `feature.yaml` DONE | Epic PUC-35 existe, **sem tarefas** |
| G6 | **FT-DOCUMENTO (leitura)** — listagem + download + `PermissaoPasta` multi-nível | `DocumentoController`, `PermissaoPasta*`, página FE | PUC-23 fala de upload/dedup, **não** de leitura/download |
| G7 | **FT-PRIMEIRO-ACESSO** — wizard completo (DEC-FA-001) | `PrimeiroAcessoApplicationService` (9 testes), página + rota | Só PUC-7 ("papel padrão"), sem o wizard |
| G8 | **Persistência de tema (DEC-FA-005)** | commit `eb0982a` | Nenhuma |
| G9 | **Pipeline CI** — `.github/workflows/backend.yml` + `.gitlab-ci.yml` por repositório | commit `a443ee1`, DEC-015 | PUC-38 tangencia |
| G10 | **DEC-015 — separação em 3 repositórios** (backend/frontend/CMS) + branches development/stage | `docs/technology/04-decision-log.md` §DEC-015 | Nenhuma |
| G11 | **Reestruturação documental W0–W2** (D1–D9), remoção de `.cursor/` | `docs/audit/12..15`, `structural-simplification-plan-w2.md` | PUC-37 tangencia |
| G12 | **Baseline de arquitetura / Sprint 0 backend / auditoria JPA Etapa 4 / homologação Oracle** | `docs/governance/01-project-status.md` M7–M11 | Nenhuma |
| G13 | **Suítes E2E Playwright** (colaborador, equipe, singular, federacao, app-shell) | `frontend/test/e2e/*` | Nenhuma |
| G14 | **Comunicação Interna / Notícias / Notificações in-app** (EPIC-005, FT-NOTICIA) | `specs/features/noticia` (DRAFT) | Nenhuma — Jira não tem Epic de Comunicação Interna |
| G15 | **Migração núcleo AS-IS → TO-BE** (Etapa 5, EPIC-006) | `docs/backlog/04-mvp-scope.md`, `docs/solution-design/10-delivery-roadmap.md` | Nenhuma |
| G16 | **RBAC — atribuição de papel escopada** (`PapelAtribuicaoService`, 2026-08-20) | commit `9306f94` | PUC-8/59/65 tangenciam, mas descrevem UI de delegação, não o mecanismo entregue |
| G17 | **Framework de trabalho** (specs/DoR/DoD/feature.yaml, Etapas 1–5, minimal-ssot) | `specs/foundation/*` | Nenhuma — o Jira não modela o processo real |

**[INFERÊNCIA]** G2 (Equipe) e G14 (Comunicação Interna) são os GAPs mais graves: uma é uma feature de organização já entregue e invisível no Jira; a outra é um Epic inteiro do MVP (Etapa 4) ausente.

---

## 6. Issues obsoletas / duplicadas / incorretas

### 6.1 Duplicatas [FATO: sobreposição de escopo]

| Grupo | Issues | Observação |
|---|---|---|
| CRUD de Singular | **PUC-18** (Concluído) ↔ PUC-71 (Concluído) | Mesmo escopo; manter PUC-18 |
| CRUD de Área | PUC-19 (parte) ↔ **PUC-77** (Concluído, órfã) ↔ parte de PUC-59 | Consolidar |
| CRUD / gestão de Colaborador | **PUC-15** (Concluído) ↔ PUC-19 (parte) ↔ PUC-83 (pendente) ↔ PUC-89/95/101 (elaboram lista/busca/vínculos) | PUC-15 é o card canônico; PUC-83 já coberto; PUC-89/95/101 rebaselinar |
| Gestão de usuários / papéis (RBAC) | PUC-8 ↔ **PUC-59** ↔ PUC-65 ↔ PUC-206 ↔ PUC-211 | 5 issues para "papéis + delegação + matriz"; consolidar em 1 feature + 1 definição |

### 6.2 Obsoletas [INFERÊNCIA]

| Issue | Motivo |
|---|---|
| PUC-12 — Esqueci minha senha | `FT-AUTH` põe recuperação de senha **fora de escopo** (Zimbra). É um link, não uma feature. |
| PUC-40, PUC-41, PUC-42 (filhas de PUC-34) | Assumem WordPress legado dentro do monorepo; contexto eliminado por DEC-015 + direção de reconstrução ("RETIRE"). |
| PUC-30 — Seleção de domínio no login | Não está na spec `FT-AUTH` atual; nenhuma decisão de produto registra a necessidade. |
| PUC-217 — Revisão de Requisitos | Bucket de processo, com reparentes; não é trabalho de produto. |
| PUC-49 (Epic "Bug") | Tipo Epic usado como categoria de bug. |

### 6.3 Incorretas (status "Concluído" não corresponde ao repositório) [FATO]

| Issue | Alegação | Realidade |
|---|---|---|
| PUC-22 — Upload com escopo | "Concluído" | Upload **não implementado**; spec de escrita (FT-DOCUMENTO-UPLOAD) está em DRAFT |
| PUC-43 — Compartilhar arquivos (público/privado) | "Concluído" | Colaborador **consulta e baixa**; **não compartilha nem publica** — compartilhamento está explicitamente fora de escopo em `FT-DOCUMENTO` |

**[INFERÊNCIA]** PUC-22 e PUC-43 provavelmente foram marcadas concluídas contra uma entrega anterior (protótipo/CMS) ou por engano; hoje induzem a erro.

---

## 7. Proposta de backlog

**[RECOMENDAÇÃO — não executada]**

Princípio: o Jira representa **produto → épicos → features/capacidades → trabalho**. `specs/features/<slug>/` continua como o detalhamento normativo (requisitos, API, aceite, tasks). Um card do Jira ≈ uma feature (`FT-*`) ou uma capacidade coesa — **nunca** um arquivo/classe/endpoint.

### 7.1 Épicos propostos (alinhados às Etapas 1–5 do MVP)

| Epic | Nome | Etapa | Features (`specs/features/`) | Estado alvo |
|---|---|---|---|---|
| E1 | Fundação da Plataforma | 1 | app-shell/router/design-system, `session` (Contexto Ativo), tema (DEC-FA-005), observabilidade base, CI/CD, DEC-015 (repo split) | Majoritariamente concluído |
| E2 | Organização Corporativa | 2 | `federacao`, `singular`, `area`, `equipe`, `colaborador` (CRUD + vínculos + busca/lista) | Concluído (núcleo) |
| E3 | Controle de Acesso | 2 | `authentication` (Zimbra + sessão), `primeiro-acesso`, RBAC papel+escopo, **Delegação de papéis (UI)**, auditoria de acesso | Núcleo concluído; delegação UI pendente |
| E4 | Área do Colaborador | 2/3 | `area-colaborador`, `federacao-colaborador`, `perfil`, `arquivos` (consulta/download) | Majoritariamente concluído; `perfil` pendente |
| E5 | Gestão Documental | 3 | `arquivos` (leitura ✓), `documento-upload` (escrita), Permissão de Pasta, auditoria documental; **futuro:** dedup por hash, WebP, painel de controle | Leitura concluída; escrita em spec |
| E6 | Comunicação Interna | 4 | `noticia`, notificações in-app, comunicados (parcial — OQ-004) | Não iniciado (specs DRAFT) |
| E7 | Migração Operacional | 5 | infra local Oracle, migração núcleo AS-IS → TO-BE | Não iniciado |
| E8 | CMS (repositório próprio) | — | conforme DEC-015 + `DS-RECONSTRUCTION-SCOPE-01` | Scaffold apenas |
| — | Bugs | contínuo | issues tipo Bug, sem Epic guarda-chuva | fluxo contínuo |

### 7.2 Regra de granularidade

- **Epic**: módulo do MVP (E1–E8).
- **Feature / História**: 1 por `FT-*` ou capacidade coesa. Descrição aponta para `specs/features/<slug>/`.
- **Subtarefa**: só quando ajuda a dividir execução — **não** replicar critérios de aceite do `acceptance-tests.md` como subtarefas (foi a causa do "aceite nunca fechado").
- **Status**: adotar 3 estados (A fazer / Em andamento / Concluído). "Concluído" exige `feature.yaml: DONE` ou evidência de código+teste.

---

## 8. Ordem recomendada de reconciliação

**[RECOMENDAÇÃO — não executada]**

1. **Congelar e rotular o PUC atual** como "baseline de discovery 2025–2026" (não apagar; é insumo histórico).
2. **Corrigir o modelo de hierarquia**: criar E1–E8; converter PUC-49 ("Bug") em fluxo de issues Bug.
3. **Fechar o que está comprovadamente entregue** (evidência: `feature.yaml` DONE + código/teste): PUC-1, PUC-6, PUC-7, PUC-8 (BE), PUC-15, PUC-18, PUC-19, PUC-20, PUC-32, PUC-50, PUC-51, PUC-83, PUC-101, PUC-206, e o Epic PUC-35.
4. **Marcar duplicatas** (§6.1): PUC-71→PUC-18, PUC-77→PUC-19, e consolidar a família RBAC (PUC-8/59/65).
5. **Corrigir as incorretas** (§6.3): reabrir/rebaselinar PUC-22 e PUC-43 contra o escopo real (leitura entregue; escrita/compartilhamento = futuro).
6. **Criar os GAPs como features nos novos Épicos** (§5): prioridade para G2 (Equipe), G14 (Comunicação Interna), G5/G4 (Área do Colaborador / Federação), G7 (Primeiro Acesso), G6 (Documental leitura), G10 (DEC-015), G9 (CI).
7. **Rebaselinar as pendentes remanescentes** (PUC-21, 24–28, 30, 31, 36–39, 44, 89, 95) contra `specs/features/` + `docs/backlog/04-mvp-scope.md`; parquear as obsoletas (§6.2).
8. **Definir E6 (Comunicação Interna) e E7 (Migração)** a partir das specs/roadmap — hoje totalmente ausentes.
9. **Estabelecer regra de sincronização**: ao promover `feature.yaml` para DONE, atualizar a feature correspondente no Jira; specs/ permanece o detalhe. Registrar essa regra em `docs/governance/` se adotada.

---

## Resultados da auditoria (terminal)

- **Issues analisadas:** 217 (8 Epics, 41 Tarefas, 168 Subtarefas) — matriz detalhada no nível Epic+Tarefa (49 itens).
- **Alinhadas (Jira ≈ realidade):** ~11 tarefas (PUC-6, 15, 18, 20, 32, 50, 51, 101, 206, 211, e Epic PUC-35 de fato pronto).
- **Desatualizadas** (feito, mas Jira não reflete — status ou roll-up): ~9 (PUC-7, 8, 30→feito parcial, 32, 37, 77, 83, 101, 206) + 6 dos 8 Epics.
- **Obsoletas:** 6 (PUC-12, 30, 40, 41, 42, 217) + tipo indevido PUC-49.
- **Duplicidades:** 4 grupos (Singular ×2, Área ×3, Colaborador ×3–6, RBAC/Usuários ×5).
- **Incorretas (status não bate):** 2 (PUC-22, PUC-43).
- **GAPs (trabalho real sem issue):** 17 (G1–G17) — mais graves: FT-EQUIPE, Comunicação Interna (Epic inteiro), Área do Colaborador/Federação, Primeiro Acesso, Documental-leitura, DEC-015, CI.
- **Principais divergências:**
  1. Jira congelado desde ~2026-01/04; execução real ocorreu depois.
  2. Epics nunca refletem o progresso dos filhos.
  3. Critérios de aceite modelados como subtarefas e nunca fechados (sistêmico).
  4. Features fechadas inteiras (Equipe, Sessão, Federação, Foundation) ausentes do Jira.
  5. PUC-22/PUC-43 "Concluído" descrevem capacidade de escrita/compartilhamento que não foi entregue.
  6. Todo o framework specs/DoR/DoD e as Etapas 3–5 do MVP não existem no Jira.
- **Relatório:** `docs/jira-reconciliation-audit.md`

---

`JIRA ALTERADO: NÃO`

`CÓDIGO ALTERADO: NÃO`

`ESPECIFICAÇÕES ALTERADAS: NÃO`
