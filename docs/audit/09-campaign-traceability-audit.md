# Campaign Traceability Audit — Conceito "Campanha"

**Projeto:** Portal de Comunicação  
**Data:** 2026-06-22  
**Objetivo:** Determinar a natureza arquitetural e de domínio do conceito "Campanha" com base exclusiva em evidências documentais.

**Fontes pesquisadas:** `docs/domain/`, `docs/architecture/`, `docs/solution-design/`, `docs/implementation/`, `docs/construction/`, `docs/governance/`

**Método:** busca textual por `Campanha`, `campanha`, `Campaign`, `campaign` em cada camada.

---

## Resumo Executivo

| Hipótese | Resultado |
| -------- | --------- |
| **A) Bounded context próprio** | **Não** — evidência não encontrada |
| **B) Feature de Comunicação Interna** | **Não** — evidência não encontrada nas camadas canônicas |
| **C) Requisito descartável do MVP** | **Sim (camadas canônicas)** / **Não (Construction)** — conflito documentado |

### Classificação final

```text
DESCARTÁVEL NO MVP
```

**Justificativa:** Nas camadas prioritárias (Domain → Architecture → Solution Design → Implementation → Governance) o termo **não existe**. O MVP aprovado arquiteturalmente (`10-delivery-roadmap.md` — Etapas 1–5) **não inclui** Campanha. A inclusão ocorre **somente** na camada Construction, sem rastreabilidade upstream. QST-001 (escopo MVP) permanece **Aberta**.

**Nota:** A camada Construction trata Campanha como área funcional **paralela** a Comunicações e Mensagens — não como subordinada ao bounded context Comunicação Interna.

---

## 1. Onde o termo Campanha aparece?

### Camadas sem ocorrência

| Camada | Resultado da busca |
| ------ | ------------------ |
| `docs/domain/` | **Nenhuma ocorrência** |
| `docs/architecture/` | **Nenhuma ocorrência** |
| `docs/solution-design/` | **Nenhuma ocorrência** de "Campanha/campaign" *(módulo "Communication" em `11-platform-decomposition.md` não usa o termo)* |
| `docs/implementation/` | **Nenhuma ocorrência** |
| `docs/governance/` | **Nenhuma ocorrência** |

### Camada Construction — ocorrências documentadas

| Documento | Trecho / Conteúdo |
| --------- | ----------------- |
| `construction/delivery/01-mvp.md` | Seção **"Gestão de Campanhas"** — Criar, Configurar, Publicar, Consultar campanha |
| `construction/delivery/02-release-plan.md` | Release 1 (MVP) — item **"Campanhas"** |
| `construction/delivery/03-cutover-plan.md` | Checklist Go-Live — seção **"Campanhas"** |
| `construction/backend/04-api-implementation.md` | URI `/api/v1/campaigns`; recurso filho `/api/v1/campaigns/{campaignId}/messages`; filtro `GET /campaigns?channel=EMAIL`; idempotência para **"Criação de campanhas"** |
| `construction/backend/03-persistence.md` | Migration `V2__create_campaign.sql` |
| `construction/backend/02-domain-model.md` | Evento de exemplo **"CampaignFinished"** |
| `construction/backend/05-integrations.md` | Evento **"CampaignStarted"** |
| `construction/frontend/01-project-bootstrap.md` | Feature folder `campaigns`; `CampaignForm.tsx`, `useCampaigns.ts`, `campaign.service.ts`, `campaign.types.ts` |
| `construction/frontend/03-routing.md` | Rotas `/campaigns`, `/listCampaigns`; menu autenticado **"Campanhas"** |
| `construction/frontend/04-state-management.md` | Store/query key `["campaigns"]` |
| `construction/frontend/05-api-consumption.md` | Módulo `campaign`; endpoint group `campaigns` |
| `construction/frontend/06-authentication.md` | Rota `/campaigns`; permissão `campaign:create`; matriz RBAC **"Campaigns"** |
| `construction/infrastructure/04-observability.md` | Métricas `campaigns_created`, `campaigns_finished` |

### Camada derivada (referência cruzada)

| Documento | Conteúdo |
| --------- | -------- |
| `docs/backlog/01-epics.md` | EPIC-008 Gestão de Campanhas — marcado **CONFLITO** |
| `docs/audit/08-execution-readiness-validation.md` | Conflito C-002 documentado |

**Conclusão:** Campanha aparece **exclusivamente na camada Construction** (e artefatos derivados do backlog). **Ausente** em Domain, Architecture, Solution Design, Implementation e Governance.

---

## 2. Qual responsabilidade de negócio é atribuída a Campanha?

### Construction — única fonte com responsabilidade explícita

**Documento origem:** `docs/construction/delivery/01-mvp.md`  
**Trecho utilizado:** seção "Gestão de Campanhas"

| Operação documentada | Responsabilidade inferível do texto *(sem ampliar escopo)* |
| -------------------- | ----------------------------------------------------------- |
| Criar campanha | Iniciar uma campanha |
| Configurar campanha | Definir parâmetros da campanha |
| Publicar campanha | Disponibilizar campanha |
| Consultar campanha | Consultar campanhas existentes |

**Documento origem:** `docs/construction/backend/04-api-implementation.md`  
**Trecho utilizado:** filtros e idempotência

- Filtro por canal: `GET /campaigns?channel=EMAIL`
- Operação crítica idempotente: criação de campanhas

**Documento origem:** `docs/construction/backend/05-integrations.md`  
**Trecho utilizado:** eventos

- `CampaignStarted` — evento de integração/mensageria documentado como exemplo

**Documento origem:** `docs/construction/infrastructure/04-observability.md`  
**Trecho utilizado:** métricas de negócio

- `campaigns_created`, `campaigns_finished` — indicadores operacionais

### Camadas canônicas

**Evidência não encontrada** de responsabilidade de negócio atribuída a "Campanha" em Domain, Architecture, Solution Design ou Implementation.

**Comunicação Interna (Domain)** — conceitos documentados **sem** Campanha:

**Documento origem:** `docs/domain/05-bounded-contexts.md` — Comunicação Interna

- Notificação, Comunicado, Fique por Dentro, Central de Colaboração, Busca unificada, Métricas administrativas

---

## 3. Existe entidade de domínio Campanha?

**Não.**

| Fonte consultada | Resultado |
| ---------------- | --------- |
| `docs/domain/05-bounded-contexts.md` | Campanha **não listada** entre conceitos de nenhum bounded context |
| `docs/domain/08-aggregates.md` | Aggregate Comunicação Interna — conceitos: Notificação, Comunicado, Fique por Dentro, Central de Colaboração, Busca unificada, Métricas administrativas — **sem Campanha** |
| `docs/domain/04-domain-concepts.md` | *(busca)* — nenhuma ocorrência |
| `docs/domain/03-ubiquitous-language.md` | *(busca)* — nenhuma ocorrência |
| `docs/domain/02-business-glossary.md` | *(busca)* — nenhuma ocorrência |

### Construction — referência técnica, não entidade de domínio formal

**Documento origem:** `docs/construction/backend/02-domain-model.md`  
**Trecho utilizado:** Domain Events — exemplos

```text
CampaignFinished
```

**Documento origem:** `docs/construction/backend/03-persistence.md`  
**Trecho utilizado:** migrations

```text
V2__create_campaign.sql
```

**Observação:** Construction documenta artefatos técnicos (evento exemplo, migration SQL) **sem** definir entidade Campanha na camada Domain congelada.

---

## 4. Existe componente arquitetural Campanha?

**Não.**

**Documento origem:** `docs/architecture/03-component-diagram.md`  
**Trecho utilizado:** catálogo de componentes Comunicação Interna

| Componente documentado | Campanha |
| ---------------------- | -------- |
| Gestão de Notificações | — |
| Gestão de Comunicados | — |
| Canal Fique por Dentro | — |
| Central de Colaboração | — |
| Busca Unificada | — |
| Métricas Administrativas | — |

**Total:** 30 componentes catalogados — **nenhum** nomeado Campanha ou Gestão de Campanhas.

**Documento origem:** `docs/solution-design/11-platform-decomposition.md`  
**Trecho utilizado:** módulo Backend "Communication"

Responsável por: comunicados, publicações, distribuição, histórico — **sem menção a Campanha**.

**Documento origem:** `docs/implementation/01-implementation-backlog.md`  
**Trecho utilizado:** BE-002 módulos

```text
organization
access-control
document-management
internal-communication
```

**Sem módulo `campaign` ou equivalente.**

---

## 5. Existe API planejada para Campanha?

**Sim — exclusivamente na camada Construction.**

**Documento origem:** `docs/construction/backend/04-api-implementation.md`

| Endpoint documentado | Descrição |
| -------------------- | --------- |
| `/api/v1/campaigns` | Recurso REST de campanhas |
| `/api/v1/campaigns/{campaignId}/messages` | Recurso filho — mensagens de campanha |
| `GET /campaigns?channel=EMAIL` | Filtro por canal |

**Documento origem:** `docs/construction/frontend/05-api-consumption.md` — grupo de endpoints `campaigns`

**Camadas canônicas:** **Evidência não encontrada** de API Campanha em Solution Design (`06-integration-contracts.md` — busca sem ocorrência) ou Implementation.

---

## 6. Existe fluxo de negócio específico para Campanha?

**Parcialmente documentado — somente Construction.**

### Fluxo funcional MVP

**Documento origem:** `docs/construction/delivery/01-mvp.md`

```text
Criar campanha → Configurar campanha → Publicar campanha → Consultar campanha
```

### Fluxo de validação Go-Live

**Documento origem:** `docs/construction/delivery/03-cutover-plan.md`  
**Trecho utilizado:** checklist — seção "Campanhas" com item `[ ] OK`

### Fluxo de eventos (Construction)

**Documento origem:** `docs/construction/backend/05-integrations.md`

```text
CampaignStarted → (cadeia não detalhada além do nome do evento)
CampaignFinished (02-domain-model.md)
```

### Fluxo de navegação (Frontend Construction)

**Documento origem:** `docs/construction/frontend/03-routing.md`

- Rota `/campaigns` — acesso Manager/Admin
- Menu autenticado: Dashboard, **Comunicações**, **Campanhas**, **Mensagens**, Administração

**Camadas canônicas:** **Evidência não encontrada** de fluxo de negócio Campanha em Domain (processos, casos de uso), Solution Design ou Implementation.

---

## 7. Campanha depende de Comunicação Interna?

**Evidência não encontrada nas camadas canônicas.**

| Camada | Resultado |
| ------ | --------- |
| Domain | Sem conceito Campanha; sem dependência documentada |
| Architecture | Sem componente Campanha; Comunicação Interna não referencia Campanha |
| Solution Design | Sem referência |
| Implementation | Sem referência |

### Construction — relações documentadas (não equivalem a Comunicação Interna)

**Documento origem:** `docs/construction/backend/04-api-implementation.md`

```http
/api/v1/campaigns/{campaignId}/messages
```

**Interpretação documental estrita:** Campanha possui recurso filho **messages** na API Construction. **Não documenta** dependência com bounded context Comunicação Interna nem com componente Gestão de Notificações/Comunicados.

**Documento origem:** `docs/construction/backend/05-integrations.md` — eventos listados em paralelo:

```text
CommunicationCreated
CampaignStarted
MessageSent
NotificationDelivered
```

Eventos **coexistem** sem dependência explícita documentada entre Campanha e Comunicação Interna.

**Resposta:** **Não documentado** que Campanha dependa de Comunicação Interna.

---

## 8. Comunicação Interna depende de Campanha?

**Não.**

**Documento origem:** `docs/domain/05-bounded-contexts.md` — Comunicação Interna  
Responsabilidades: notificações, Fique por Dentro, comunicados, Central de Colaboração, busca, métricas — **sem referência a Campanha**.

**Documento origem:** `docs/architecture/03-component-diagram.md`  
**Trecho utilizado:** "Gestão de Notificações é acionada por eventos de Controle de Acesso e Gestão Documental" — **sem Campanha**.

**Documento origem:** `docs/solution-design/10-delivery-roadmap.md` — Etapa 4 Comunicação Interna  
Dependências documentadas: Etapa 2 (identidade); Etapa 3 recomendada (eventos documentais) — **sem Campanha**.

**Resposta:** **Evidência não encontrada** de dependência de Comunicação Interna em Campanha.

---

## Análise das Hipóteses

### A) Bounded context próprio

| Critério | Evidência | Atende? |
| -------- | --------- | ------- |
| Listado em `05-bounded-contexts.md` | 4 contextos: Organização, Documental, Acesso, Comunicação Interna | **Não** |
| Aggregate próprio em `08-aggregates.md` | 4 aggregates — sem Campanha | **Não** |
| Componente/módulo arquitetural | Ausente em `03-component-diagram.md` | **Não** |
| Módulo backend Implementation | `internal-communication` — sem campaign | **Não** |

**Conclusão A:** **Rejeitada** — evidência não encontrada.

---

### B) Feature de Comunicação Interna

| Critério | Evidência | Atende? |
| -------- | --------- | ------- |
| Campanha listada entre conceitos de Comunicação Interna | Notificação, Comunicado, etc. — **sem Campanha** | **Não** |
| Componente "Gestão de Campanhas" em Architecture | Ausente | **Não** |
| Mapeamento explícito Construction → Comunicação Interna | Ausente | **Não** |
| Estrutura Construction | `communications`, `campaigns`, `messages` como **features paralelas** (`frontend/01-project-bootstrap.md`) | Sugere área **separada**, não subfeature |

**Conclusão B:** **Rejeitada** — evidência não encontrada nas camadas canônicas. Construction organiza Campanha como módulo **sibling** de Comunicações, não como subordinado documentado a Comunicação Interna.

---

### C) Requisito descartável do MVP

| Fonte | Campanha no MVP? | Evidência |
| ----- | ---------------- | --------- |
| `10-delivery-roadmap.md` (Etapas 1–5) | **Não** | Capacidades obrigatórias: auth, org, documental, notificações, migração — sem Campanha |
| `01-solution-overview.md` | **Não** | Capacidades principais sem Campanha |
| `01-implementation-backlog.md` | **Não** | COM-001 a COM-008 — comunicados, notificações — sem campanha |
| `construction/delivery/01-mvp.md` | **Sim** | Gestão de Campanhas incluída |
| `construction/delivery/02-release-plan.md` | **Sim** | Release 1 = MVP inclui Campanhas |
| `governance/05-roadmap.md` QST-001 | **Indeterminado** | "Quais funcionalidades compõem o escopo mínimo do MVP?" — **Aberta** |

**Conclusão C:** **Parcialmente confirmada** — nas camadas prioritárias Campanha **não compõe** o MVP TO-BE; na Construction **compõe** Release 1/MVP. Conflito **não resolvido** (QST-001 Aberta).

---

## Classificação Final

```text
DESCARTÁVEL NO MVP
```

### Critério de classificação aplicado

Prioridade de decisão documentada (`08-backlog-agent.mdc`, `08-execution-readiness-validation.md`):

```text
Domain → Architecture → Solution Design → Implementation → Construction → Governance
```

Nas cinco primeiras camadas **Campanha não existe**. O MVP arquitetural (`10-delivery-roadmap.md`) **não a inclui**. Portanto, para execução alinhada à arquitetura aprovada (GO em `00-architecture-readiness.md`), Campanha é **requisito descartável** até decisão formal contrária.

### Classificações rejeitadas

| Classificação | Motivo |
| ------------- | ------ |
| **BOUNDED CONTEXT** | Ausente em Domain e Architecture |
| **FEATURE** | Sem feature arquitetural ou de domínio; apenas EPIC-008 órfão derivado de Construction |
| **SUBFEATURE** | Sem documentação de Campanha como subordinada a Comunicação Interna; relação filho documentada apenas Campanha → Messages (Construction API), não mapeada a Comunicação Interna |

---

## Conflitos Documentais

| ID | Conflito | Evidência |
| -- | -------- | --------- |
| CF-01 | Campanha no MVP Construction vs. ausência no MVP Solution Design | `01-mvp.md` vs. `10-delivery-roadmap.md` |
| CF-02 | Campanha sem entidade/componente canônico | Domain/Architecture vs. Construction |
| CF-03 | QST-001 aberta impede consolidação | `governance/05-roadmap.md` |
| CF-04 | Terminologia paralela Construction: Comunicações / Campanhas / Mensagens vs. Comunicação Interna Domain | `01-mvp.md` vs. `05-bounded-contexts.md` |
| CF-05 | API `/campaigns` planejada em Construction sem contrato Solution Design | `04-api-implementation.md` vs. ausência em `06-integration-contracts.md` |

---

## Recomendações (fundamentadas em evidências)

1. **Encerrar QST-001** registrando se Campanha permanece ou é excluída do MVP — única via formal documentada para resolver CF-01.
2. **Se excluída:** remover ou marcar como obsoletos artefatos Construction (`01-mvp.md`, APIs, migrations, rotas) referentes a Campanha — conflito com camadas canônicas.
3. **Se mantida:** exigir artefato Domain (conceito, regras, aggregate) e Architecture (componente) antes de implementação — requisito de rastreabilidade em `03-development-standards.md`.
4. **Não implementar** EPIC-008 / FEATURE-070 enquanto CF-01 a CF-05 persistirem sem decisão registrada.

---

## Respostas Objetivas — Sumário

| # | Pergunta | Resposta |
| - | -------- | -------- |
| 1 | Onde aparece? | **Somente** `docs/construction/` (+ backlog/audit derivados) |
| 2 | Responsabilidade de negócio? | Criar, configurar, publicar, consultar campanha (`01-mvp.md`) — **apenas Construction** |
| 3 | Entidade de domínio? | **Não** |
| 4 | Componente arquitetural? | **Não** |
| 5 | API planejada? | **Sim** — Construction (`/api/v1/campaigns`) |
| 6 | Fluxo de negócio específico? | **Parcial** — CRUD + eventos + Go-Live checklist — **apenas Construction** |
| 7 | Campanha depende de Comunicação Interna? | **Não documentado** |
| 8 | Comunicação Interna depende de Campanha? | **Não** |

---

## Nível de Confiança

**Alto** — para ausência nas camadas canônicas (busca exaustiva negativa).  
**Alto** — para presença exclusiva em Construction (múltiplos artefatos convergentes).  
**Médio** — para classificação DESCARTÁVEL NO MVP, condicionada a resolução de QST-001.

---

## Fontes Consultadas

| Camada | Documentos |
| ------ | ---------- |
| Domain | `05-bounded-contexts.md`, `08-aggregates.md`, `09-business-rules.md`, `10-open-questions.md` (+ busca global) |
| Architecture | `03-component-diagram.md` (+ busca global) |
| Solution Design | `01-solution-overview.md`, `10-delivery-roadmap.md`, `11-platform-decomposition.md` (+ busca global) |
| Implementation | `01-implementation-backlog.md` (+ busca global) |
| Construction | `delivery/01-mvp.md`, `delivery/02-release-plan.md`, `delivery/03-cutover-plan.md`, `backend/02-domain-model.md`, `backend/03-persistence.md`, `backend/04-api-implementation.md`, `backend/05-integrations.md`, `frontend/01-project-bootstrap.md`, `frontend/03-routing.md`, `frontend/04-state-management.md`, `frontend/05-api-consumption.md`, `frontend/06-authentication.md`, `infrastructure/04-observability.md` |
| Governance | `05-roadmap.md` (QST-001) |
