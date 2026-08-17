# Epics — Portal de Comunicação

## Objetivo

Consolidar épicos derivados exclusivamente da documentação oficial do projeto.

**Prioridade de decisão em conflitos:** Domain → Architecture → Solution Design → Implementation → Construction → Governance.

**Data de consolidação:** 2026-06-22  
**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado em 2026-06-22

---

## Módulos Identificados

| Módulo | Origem | Descrição |
| ------ | ------ | --------- |
| Fundação da Plataforma | `docs/solution-design/10-delivery-roadmap.md` Etapa 1 | Infraestrutura TO-BE: ambientes, persistência, proxy, observabilidade base, esqueletos Backend/Frontend/CMS |
| Organização Corporativa | `docs/domain/05-bounded-contexts.md` | Hierarquia federativa, singulares, áreas, equipes, colaboradores, vínculos |
| Controle de Acesso | `docs/domain/05-bounded-contexts.md` | Autenticação, autorização, papéis, escopos, auditoria |
| Gestão Documental | `docs/domain/05-bounded-contexts.md` | Publicação, pastas, visibilidade, compartilhamento, armazenamento documental |
| Comunicação Interna | `docs/domain/05-bounded-contexts.md` | Notificações, comunicados, busca transversal, engajamento |
| Migração Operacional | `docs/solution-design/10-delivery-roadmap.md` Etapa 5 | Transferência AS-IS → TO-BE |
| Descomissionamento | `docs/solution-design/10-delivery-roadmap.md` Etapa 6 | Remoção de componentes legados |

---

## EPIC-001

**Nome:** Fundação da Plataforma

**Objetivo:** Disponibilizar a plataforma TO-BE mínima operável — infraestrutura, persistência segregada, fronteira segura e esqueletos de aplicação — habilitando construção dos bounded contexts.

**Etapa arquitetural:** 1

**Origem:**

- `docs/solution-design/10-delivery-roadmap.md` — Etapa 1
- `docs/implementation/01-implementation-backlog.md` — INF-001 a SEC-002, BE-001 a BE-004, FE-001 a FE-004, OBS-001 a OBS-003

**Critério de conclusão documentado:** Todos os containers da arquitetura alvo executando em ambiente Local e Dev.

---

## EPIC-002

**Nome:** Organização Corporativa

**Objetivo:** Estruturar e manter a hierarquia federativa multi-singular, vínculos operacionais de colaboradores e contexto organizacional.

**Etapa arquitetural:** 2

**Bounded context:** Organização Corporativa

**Origem:**

- `docs/domain/05-bounded-contexts.md`
- `docs/architecture/03-component-diagram.md` — componentes Organização Corporativa
- `docs/solution-design/10-delivery-roadmap.md` — Etapa 2
- `docs/implementation/01-implementation-backlog.md` — ORG-001 a ORG-006, FE-006

**Status arquitetural:** ATIVO (onboarding/primeiro acesso — DEC-FA-001; FT-PRIMEIRO-ACESSO)

---

## EPIC-003

**Nome:** Controle de Acesso

**Objetivo:** Garantir autenticação corporativa via Zimbra, sessão, autorização centralizada por papel e escopo, e auditoria inicial.

**Etapa arquitetural:** 2

**Bounded context:** Controle de Acesso

**Origem:**

- `docs/domain/05-bounded-contexts.md`
- `docs/architecture/03-component-diagram.md` — componentes Controle de Acesso
- `docs/solution-design/10-delivery-roadmap.md` — Etapa 2
- `docs/construction/delivery/01-mvp.md` — Autenticação, Auditoria
- `docs/implementation/01-implementation-backlog.md` — ACC-001 a ACC-007, FE-005, FE-007

**Status arquitetural:** ATIVO (solicitação de permissão e perfis externos PARCIAL)

**Critério de conclusão documentado:** Login corporativo funcional utilizando autenticação via Zimbra e autorização centralizada.

---

## EPIC-004

**Nome:** Gestão Documental

**Objetivo:** Publicar, organizar e controlar exposição de documentos e pastas com separação metadado/binário e governança de acesso.

**Etapa arquitetural:** 3

**Bounded context:** Gestão Documental

**Origem:**

- `docs/domain/05-bounded-contexts.md`
- `docs/architecture/03-component-diagram.md` — componentes Gestão Documental
- `docs/solution-design/10-delivery-roadmap.md` — Etapa 3
- `docs/implementation/01-implementation-backlog.md` — DOC-001 a DOC-010, FE-008 a FE-010

**Status arquitetural:** ATIVO (compartilhamento ↔ autorização com ressalva OQ-005)

**Critério de conclusão documentado:** Publicação e consulta documental operacionais.

**MVP:** Sim — obrigatório (`docs/audit/10-mvp-consolidation-audit.md` Etapa 3).

---

## EPIC-005

**Nome:** Comunicação Interna

**Objetivo:** Centralizar notificações in-app, comunicados e capacidades transversais de informação e engajamento entre colaboradores.

**Etapa arquitetural:** 4

**Bounded context:** Comunicação Interna

**Origem:**

- `docs/domain/05-bounded-contexts.md`
- `docs/architecture/03-component-diagram.md` — componentes Comunicação Interna
- `docs/solution-design/10-delivery-roadmap.md` — Etapa 4
- `docs/construction/delivery/01-mvp.md` — Gestão de Comunicados, Notificações
- `docs/implementation/01-implementation-backlog.md` — COM-001 a COM-008, FE-011, FE-012

**Status arquitetural:** Notificações ATIVO; comunicados, busca, métricas PARCIAL

**Critério de conclusão documentado:** Notificações unificadas funcionando ponta a ponta.

---

## EPIC-006

**Nome:** Migração Operacional

**Objetivo:** Transferir operação AS-IS → TO-BE — dados, integrações e tráfego de usuários — com validação por ambiente.

**Etapa arquitetural:** 5

**Origem:**

- `docs/solution-design/10-delivery-roadmap.md` — Etapa 5
- `docs/solution-design/09-migration-strategy.md`
- `docs/implementation/01-implementation-backlog.md` — MIG-001 a MIG-009
- `docs/construction/delivery/03-cutover-plan.md`

**Critério de conclusão documentado:** Capacidades migradas executando em Produção.

---

## EPIC-007

**Nome:** Descomissionamento

**Objetivo:** Remover componentes AS-IS (Backend PHP, API CMS negócio, JWT duplicado, notificações duplicadas) e encerrar ADR-015.

**Etapa arquitetural:** 6

**Origem:**

- `docs/solution-design/10-delivery-roadmap.md` — Etapa 6
- `docs/implementation/01-implementation-backlog.md` — DEC-001 a DEC-006

**Critério de conclusão documentado:** Arquitetura TO-BE operando sem dependências do legado.

**MVP:** Não — pós-MVP produtivo (depende de Etapa 5).

---

## Conflitos Resolvidos

Conflitos C-001 a C-004 resolvidos em `docs/audit/10-mvp-consolidation-audit.md`. EPIC-008 (Gestão de Campanhas) **removido** do MVP oficial — sem bounded context em Domain/Architecture.

---

## Índice de Épicos

| ID | Nome | Etapa | MVP (ver `04-mvp-scope.md`) |
| -- | ---- | ----- | --------------------------- |
| EPIC-001 | Fundação da Plataforma | 1 | Sim |
| EPIC-002 | Organização Corporativa | 2 | Sim |
| EPIC-003 | Controle de Acesso | 2 | Sim |
| EPIC-004 | Gestão Documental | 3 | Sim |
| EPIC-005 | Comunicação Interna | 4 | Sim (núcleo + PARCIAL) |
| EPIC-006 | Migração Operacional | 5 | Sim |
| EPIC-007 | Descomissionamento | 6 | Não |
