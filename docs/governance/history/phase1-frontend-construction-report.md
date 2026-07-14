# Fase 1 — Relatório de Limpeza Frontend Construction

**Projeto:** Portal de Comunicação  
**Data:** 2026-06-22  
**Escopo:** Somente `docs/construction/frontend/`  
**Fonte normativa:** `docs/audit/10-mvp-consolidation-audit.md`  
**Pré-requisito:** `docs/governance/reconciliation-report.md`

**Fase 2 (pendente):** Backend Construction, persistence, integrations, observability.

---

## Objetivo

Eliminar referências ativas a Campanhas, Mensagens e nomenclatura `communications` que poderiam induzir implementação fora do MVP, alinhando os guias de bootstrap, estado, API, autenticação e roteamento ao léxico oficial.

---

## Arquivos Alterados

| # | Arquivo | Status |
| - | ------- | ------ |
| 1 | `construction/frontend/01-project-bootstrap.md` | Atualizado |
| 2 | `construction/frontend/03-routing.md` | Atualizado + correção |
| 3 | `construction/frontend/04-state-management.md` | Atualizado |
| 4 | `construction/frontend/05-api-consumption.md` | Atualizado |
| 5 | `construction/frontend/06-authentication.md` | Atualizado |
| 6 | `construction/frontend/02-design-system.md` | **Não alterado** |

---

## Alterações por Arquivo

### 1. `01-project-bootstrap.md`

| Seção | Antes | Depois |
| ----- | ----- | ------ |
| Cabeçalho | Sem referência MVP | Referência `10-mvp-consolidation-audit.md` |
| `features/` | `communications`, `campaigns`, `messages`, `authentication` | `comunicados`, `notifications`, `documents`, `organization`, `access-control`, `authentication` |
| Exemplo de feature | `communications/` | `comunicados/` + rastreabilidade FEATURE-041, 040, 030 |
| Componentes | `CommunicationCard`, `CampaignForm` | `ComunicadoCard`, `NotificationBell`, `DocumentExplorer` |
| Hooks | `useCommunication`, `useCampaigns` | `useComunicados`, `useNotifications`, `useDocuments` |
| Services | `communication.service`, `campaign.service` | `comunicado.service`, `notification.service`, `document.service` |
| Types | `communication.types`, `campaign.types` | `comunicado.types`, `notification.types`, `document.types` |
| Novo | — | Seção **Obsoleto** listando artefatos Campanhas/Mensagens proibidos |

---

### 2. `03-routing.md`

| Seção | Antes | Depois |
| ----- | ----- | ------ |
| Cabeçalho | Sem referência Fase 1 | Referência MVP oficial |
| Layout autenticado | Texto corrompido (`Mensagens`, `Administração` soltos) | Corrigido — lista limpa Dashboard → Configurações |
| Rotas MVP | Já em `/comunicados` (reconciliação anterior) | Mantido; confirmado |
| Seção Obsoleto | `/campaigns`, `/messages` | Mantida intencionalmente |

**Correção adicional:** removido fragmento malformado introduzido na reconciliação anterior (linhas órfãs após bloco de código).

---

### 3. `04-state-management.md`

| Seção | Antes | Depois |
| ----- | ----- | ------ |
| Cabeçalho | Sem referência MVP | Referência Fase 1 |
| Server State exemplos | Usuários, Comunicações, Campanhas, Mensagens | Colaboradores, Comunicados, Notificações, Documentos |
| Estrutura features | `communications/` único | `comunicados/`, `notifications/`, `documents/` |
| Queries | `useCommunicationsQuery()` | `useComunicadosQuery()`, `useNotificationsQuery()`, `useDocumentsQuery()` |
| Mutations | `useCreateCommunicationMutation()` | `useCreateComunicadoMutation()` |
| Query Keys | `["communications"]`, `["campaigns"]` | `["comunicados"]`, `["notifications"]`, `["documents"]` |
| Estrutura recomendada | `useCommunications.ts`, `communication.service.ts` | `useComunicados.ts`, `comunicado.service.ts` |

---

### 4. `05-api-consumption.md`

| Seção | Antes | Depois |
| ----- | ----- | ------ |
| Cabeçalho | Sem referência MVP | Referência Fase 1 |
| `services/` | `communication`, `campaign`, `message` | `comunicado`, `notification`, `document` |
| Endpoints MVP | `/api/v1/communications` | `/api/v1/comunicados`, `/api/v1/notifications`, `/api/v1/documents` |
| Endpoints obsoletos | — | Seção explícita: `campaigns`, `messages`, `communications` |
| Service/DTOs | `communicationService`, `CommunicationResponse` | `comunicadoService`, `ComunicadoResponse` |
| Query Keys | `communications`, `campaigns` | `comunicados`, `notifications`, `documents` |
| Feature isolation | `communications`, `campaigns`, `messages` | `comunicados`, `notifications`, `documents` |

---

### 5. `06-authentication.md`

| Seção | Antes | Depois |
| ----- | ----- | ------ |
| Cabeçalho | Sem referência MVP | Referência Fase 1 |
| Rotas protegidas | `/communications`, `/campaigns`, `/messages` | `/comunicados`, `/notifications`, `/documents` |
| Rotas obsoletas | — | Seção `/campaigns`, `/messages` — não implementar |
| Matriz RBAC | Communications, **Campaigns** | Comunicados, Notificações, Documentos |
| Component Guard | `campaign:create` | `comunicado:create` |
| Permissões | `communication:view/create/update/delete` | `comunicado:*`, `notification:view`, `document:view/upload` |

---

### 6. `02-design-system.md` — não alterado

| Ocorrência | Motivo |
| ---------- | ------ |
| "Mensagem de ajuda" | Texto de UI (help text), **não** conceito de domínio "Gestão de Mensagens" |

---

## Mapeamento MVP Aplicado

| Módulo frontend | Rota | API | Feature | Épico |
| --------------- | ---- | --- | ------- | ----- |
| Comunicados | `/comunicados` | `/api/v1/comunicados` | FEATURE-041 | EPIC-005 |
| Notificações | `/notifications` | `/api/v1/notifications` | FEATURE-040 | EPIC-005 |
| Documentos | `/documents` | `/api/v1/documents` | FEATURE-030–037 | EPIC-004 |
| Organização | *(rotas em EPIC-002)* | — | FEATURE-010–017 | EPIC-002 |
| Acesso | `/login`, etc. | — | FEATURE-020–029 | EPIC-003 |

---

## Itens Removidos (não implementar)

| Conceito | Onde estava | Ação |
| -------- | ----------- | ---- |
| `features/campaigns/` | bootstrap | Removido — seção Obsoleto |
| `features/messages/` | bootstrap | Removido — seção Obsoleto |
| `CampaignForm`, `useCampaigns`, etc. | bootstrap | Listados como obsoletos |
| `["campaigns"]` query key | state, api | Removido |
| `/campaigns`, `/messages` rotas ativas | auth, routing | Movidas para Obsoleto |
| `campaign:create` permissão | auth | Substituída por `comunicado:create` |
| `communications` como módulo ativo | todos | Renomeado → `comunicados` |

---

## Verificação Pós-Fase 1

Busca em `docs/construction/frontend/`:

| Padrão | Ocorrências restantes | Contexto |
| ------ | --------------------- | -------- |
| `campaign`, `Campaign` | 4 arquivos | Apenas seções **Obsoleto** ou listas de proibição |
| `communications` | 1 arquivo | Seção **Obsoleto** em `05-api-consumption.md` |
| `comunicados` | 5 arquivos | Padrão ativo MVP |
| `Mensagem de ajuda` | 1 arquivo | `02-design-system.md` — UI, ignorar |

**Resultado:** Nenhuma referência ativa a Campanhas/Mensagens como escopo de implementação.

---

## Riscos Residuais (Fase 2)

Estes itens **ainda podem** induzir erro se o desenvolvedor consultar documentos fora do frontend:

| Arquivo | Risco |
| ------- | ----- |
| `construction/backend/04-api-implementation.md` | Exemplos mistos `communications` (ativo) vs `comunicados` (MVP) |
| `construction/backend/03-persistence.md` | `V2__create_campaign.sql` |
| `construction/backend/02-domain-model.md` | `CampaignFinished` |
| `construction/backend/05-integrations.md` | `CampaignStarted`, `/webhooks/messages` |
| `construction/infrastructure/04-observability.md` | `campaigns_created`, `messages_sent` |
| `audit/08-execution-readiness-validation.md` | Corpo histórico com MVP antigo |
| `audit/11-final-readiness-audit.md` | Snapshot pré-reconciliação |

---

## Conformidade

| Critério | Status |
| -------- | ------ |
| Novos requisitos criados? | **Não** |
| Novos módulos criados? | **Não** — apenas renomeação léxica |
| Decisões arquiteturais alteradas? | **Não** |
| Alinhado ao audit 10? | **Sim** |
| Frontend Construction seguro para bootstrap? | **Sim** |

---

## Próximo Passo

**Fase 2 — Backend Construction + Infraestrutura:**

1. `backend/04-api-implementation.md` — alinhar exemplos ativos a `comunicados`
2. `backend/03-persistence.md` — remover/marcar `V2__create_campaign.sql`
3. `backend/02-domain-model.md` — substituir exemplos Campaign
4. `backend/05-integrations.md` — remover eventos Campanha
5. `infrastructure/04-observability.md` — métricas MVP oficiais

Aguardar aprovação para executar Fase 2.
