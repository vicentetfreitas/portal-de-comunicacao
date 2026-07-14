# User Stories — Portal de Comunicação

## Objetivo

Transformar funcionalidades documentadas em histórias de usuário rastreáveis a features (`02-features.md`).

**Regras aplicadas:**

- Nenhuma história sem feature vinculada.
- Critérios de aceite extraídos exclusivamente de documentação existente.
- Atores conforme `docs/solution-design/01-solution-overview.md`: Colaborador, Gestor, Administrador.

**Data de consolidação:** 2026-06-22  
**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado em 2026-06-22

---

## EPIC-001 / FEATURE-001 — Infraestrutura

### US-001

**Feature:** FEATURE-001  
**Como** desenvolvedor  
**Quero** subir todos os containers da arquitetura alvo em Local e Dev  
**Para** habilitar construção dos bounded contexts

**Critérios:**

- Stack completa sobe em Local e Dev (`10-delivery-roadmap.md` Etapa 1)
- Persistência isolada por ambiente (ADR-011)
- Reverse Proxy roteia Frontend e WordPress com HTTPS
- Secrets não versionados

**Origem:** `docs/solution-design/10-delivery-roadmap.md` — Critérios de Prontidão Etapa 1; `docs/implementation/01-implementation-backlog.md` — INF-001 a INF-005

---

## EPIC-003 / FEATURE-020 — Autenticação

### US-010

**Feature:** FEATURE-020  
**Como** colaborador  
**Quero** realizar login com e-mail corporativo  
**Para** acessar o portal

**Critérios:**

- Login corporativo via Zimbra em Dev (`10-delivery-roadmap.md` Etapa 2)
- Sessão estabelecida e validada por operação (ADR-005)
- Auditoria de login registrada (BR-005)

**Origem:** `docs/construction/delivery/01-mvp.md` — Login; `docs/implementation/01-implementation-backlog.md` — ACC-001, ACC-002

### US-011

**Feature:** FEATURE-020  
**Como** colaborador  
**Quero** realizar logout  
**Para** encerrar minha sessão no portal

**Critérios:**

- Sessão encerrada no Backend
- Estado de sessão no cliente atualizado

**Origem:** `docs/construction/delivery/01-mvp.md` — Logout

---

## EPIC-003 / FEATURE-021 — Sessão

### US-012

**Feature:** FEATURE-021  
**Como** colaborador autenticado  
**Quero** manter controle de sessão  
**Para** operar no portal de forma contínua e segura

**Critérios:**

- Sessão estabelecida e validada por operação (ADR-005)
- Decisão efetiva de acesso no Backend — Frontend não decide (ADR-005, ADR-006)

**Origem:** `docs/construction/delivery/01-mvp.md` — Controle de sessão; `docs/solution-design/10-delivery-roadmap.md` Etapa 2

---

## EPIC-003 / FEATURE-023 — Autorização

### US-013

**Feature:** FEATURE-023  
**Como** colaborador  
**Quero** que o controle de acesso restrinja minhas operações conforme papel e escopo  
**Para** acessar apenas recursos autorizados

**Critérios:**

- Autorização por papel/escopo em operação sensível
- Backend decide — Frontend não (ADR-005)

**Origem:** `docs/construction/delivery/01-mvp.md` — Controle de acesso; `docs/solution-design/10-delivery-roadmap.md` Etapa 2

---

## EPIC-003 / FEATURE-025 — Auditoria

### US-014

**Feature:** FEATURE-025  
**Como** administrador  
**Quero** consultar registro de ações e histórico operacional  
**Para** auditar operações no portal

**Critérios:**

- Auditoria de login e alteração organizacional (BR-005)
- Eventos de governança registrados

**Origem:** `docs/construction/delivery/01-mvp.md` — Auditoria (Registro de ações, Histórico operacional)

---

## EPIC-002 / FEATURE-010 a 014 — Organização

### US-020

**Feature:** FEATURE-010  
**Como** administrador  
**Quero** administrar singulares  
**Para** manter a estrutura federativa da organização

**Critérios:**

- CRUD Singulares funcional (`01-implementation-backlog.md` — ORG-002)
- CRUD organizacional básico funcional (ADR-013)

**Origem:** `docs/domain/05-bounded-contexts.md`; `docs/implementation/01-implementation-backlog.md`

### US-021

**Feature:** FEATURE-011  
**Como** administrador  
**Quero** administrar áreas  
**Para** organizar setores departamentais vinculados a singulares

**Critérios:**

- CRUD Áreas funcional (ORG-003)
- Área pertence a uma singular (`05-bounded-contexts.md`)

**Origem:** `docs/domain/05-bounded-contexts.md`; ORG-003

### US-022

**Feature:** FEATURE-012  
**Como** gestor  
**Quero** administrar equipes  
**Para** agrupar colaboradores dentro de áreas

**Critérios:**

- CRUD Equipes funcional (ORG-004)
- Equipe pertence a uma área (`05-bounded-contexts.md`)

**Origem:** `docs/domain/05-bounded-contexts.md`; ORG-004

### US-023

**Feature:** FEATURE-013  
**Como** gestor  
**Quero** visualizar e administrar colaboradores por escopo  
**Para** operar sobre colaboradores da minha singular ou área

**Critérios:**

- CRUD Colaboradores funcional (ORG-005)

**Origem:** `docs/architecture/03-component-diagram.md`; ORG-005

### US-024

**Feature:** FEATURE-014  
**Como** administrador  
**Quero** gerenciar vínculos organizacionais  
**Para** associar colaboradores a singular, área e equipe

**Critérios:**

- CRUD Vínculos funcional (ORG-006)
- Colaborador possui vínculo operacional a singular e área (`05-bounded-contexts.md`)

**Origem:** `docs/domain/05-bounded-contexts.md`; ORG-006

---

## EPIC-004 / FEATURE-030 a 036 — Gestão Documental

### US-030

**Feature:** FEATURE-030  
**Como** colaborador  
**Quero** publicar e consultar documentos  
**Para** disponibilizar e acessar conteúdo no escopo organizacional

**Critérios:**

- Publicação metadado + binário coordenada (`10-delivery-roadmap.md` Etapa 3)
- Publicação e consulta documental operacionais (`01-implementation-backlog.md` Etapa 3)

**Origem:** `docs/solution-design/10-delivery-roadmap.md`; DOC-001, DOC-009

### US-031

**Feature:** FEATURE-031  
**Como** colaborador  
**Quero** organizar documentos em pastas  
**Para** estruturar conteúdo por contexto organizacional

**Critérios:**

- Estrutura documental por escopo organizacional (`10-delivery-roadmap.md` Etapa 3)

**Origem:** DOC-002; `docs/solution-design/10-delivery-roadmap.md`

### US-032

**Feature:** FEATURE-034  
**Como** colaborador  
**Quero** fazer upload e download autorizado de arquivos  
**Para** publicar e obter binários documentais

**Critérios:**

- Download autorizado via Backend (ADR-004, ADR-005)
- Quota bloqueia publicação quando excedida (BR-023)

**Origem:** `docs/solution-design/10-delivery-roadmap.md` Etapa 3; DOC-005, DOC-006

### US-033

**Feature:** FEATURE-033  
**Como** colaborador  
**Quero** definir compartilhamento de documentos  
**Para** controlar audiência autorizada

**Critérios:**

- Visibilidade e compartilhamento persistidos
- Autorização validada antes de entrega (ADR-005)
- Ressalva OQ-005 documentada

**Origem:** `docs/solution-design/10-delivery-roadmap.md` Etapa 3; DOC-004

### US-034

**Feature:** FEATURE-035  
**Como** colaborador  
**Quero** buscar documentos  
**Para** localizar conteúdo autorizado

**Critérios:**

- Busca retorna apenas resultados autorizados (ADR-014)

**Origem:** `docs/solution-design/10-delivery-roadmap.md` Etapa 3; DOC-008, DOC-009

---

## EPIC-005 / FEATURE-040 — Notificações

### US-040

**Feature:** FEATURE-040  
**Como** colaborador  
**Quero** receber notificações in-app  
**Para** ser informado de eventos relevantes

**Critérios:**

- Notificações in-app persistidas e entregues via Frontend (ADR-012)
- Subsistema único — sem duplicidade (L-009, R-006)
- Notificações unificadas funcionando ponta a ponta (`01-implementation-backlog.md` Etapa 4)

**Origem:** `docs/solution-design/10-delivery-roadmap.md` Etapa 4; COM-001, COM-002

---

## EPIC-005 / FEATURE-041 — Comunicados

### US-041

**Feature:** FEATURE-041  
**Como** colaborador  
**Quero** criar comunicado  
**Para** publicar informação no portal

**Critérios:**

- Fluxo documentado em `01-mvp.md` — Gestão de Comunicados
- Comunicados identificados como PARCIAL se OQ-004 aberta (`10-delivery-roadmap.md` Etapa 4)

**Origem:** `docs/construction/delivery/01-mvp.md`; `docs/audit/10-mvp-consolidation-audit.md` — C-004

### US-042

**Feature:** FEATURE-041  
**Como** colaborador  
**Quero** editar comunicado  
**Para** corrigir ou atualizar informação publicada

**Origem:** `docs/construction/delivery/01-mvp.md` — Gestão de Comunicados

### US-043

**Feature:** FEATURE-041  
**Como** colaborador  
**Quero** consultar comunicado  
**Para** acessar informações publicadas

**Origem:** `docs/construction/delivery/01-mvp.md` — Gestão de Comunicados

### US-044

**Feature:** FEATURE-041  
**Como** colaborador  
**Quero** excluir comunicado  
**Para** remover informação publicada

**Origem:** `docs/construction/delivery/01-mvp.md` — Gestão de Comunicados

**Status:** PARCIAL — OQ-004

---

## EPIC-006 / FEATURE-050 — Migração

### US-070

**Feature:** FEATURE-050  
**Como** equipe de migração  
**Quero** migrar dados organizacionais, documentais, permissões e auditoria  
**Para** transferir operação AS-IS → TO-BE

**Critérios:**

- Dados núcleo migrados ou estratégia de corte validada
- Reconciliação metadado/binário (R-004)
- Backup Prod pré-corte (R-002)

**Origem:** `docs/solution-design/10-delivery-roadmap.md` Etapa 5; MIG-001 a MIG-004

### US-071

**Feature:** FEATURE-051  
**Como** equipe de migração  
**Quero** validar migração e testar rollback  
**Para** garantir transição segura

**Critérios:**

- Zero regressão crítica em Hml — aceite negócio
- Rollback documentado (`09-migration-strategy.md`)

**Origem:** MIG-005 a MIG-007; `docs/solution-design/10-delivery-roadmap.md` Etapa 5

---

## EPIC-001 / MVP — Critérios Globais

### US-080

**Feature:** FEATURE-004  
**Como** operador  
**Quero** observabilidade ativa na plataforma  
**Para** monitorar saúde operacional do MVP

**Critérios:**

- Observabilidade ativa (`01-mvp.md` — Critérios de Aceite)
- Logs básicos e health (`10-delivery-roadmap.md` Etapa 1)
- Incluída em Release 1 (`02-release-plan.md`)

**Origem:** `docs/construction/delivery/01-mvp.md`; `docs/construction/delivery/02-release-plan.md`

### US-081

**Feature:** FEATURE-001  
**Como** stakeholder  
**Quero** que o MVP atenda critérios de aceite documentados  
**Para** autorizar Go-Live

**Critérios:**

- Todos os fluxos críticos operacionais
- Não existirem defeitos críticos
- Testes aprovados
- Segurança validada
- Observabilidade ativa

**Origem:** `docs/construction/delivery/01-mvp.md` — Critérios de Aceite

---

## Matriz Epic → Feature → Story

| Epic | Feature | Stories |
| ---- | ------- | ------- |
| EPIC-001 | FEATURE-001 | US-001 |
| EPIC-001 | FEATURE-004 | US-080 |
| EPIC-001 | FEATURE-001 | US-081 |
| EPIC-002 | FEATURE-010–014 | US-020 a US-024 |
| EPIC-003 | FEATURE-020–025 | US-010 a US-014 |
| EPIC-004 | FEATURE-030–035 | US-030 a US-034 |
| EPIC-005 | FEATURE-040 | US-040 |
| EPIC-005 | FEATURE-041 | US-041 a US-044 |
| EPIC-006 | FEATURE-050–051 | US-070, US-071 |

**Total:** 24 user stories

**Removidas do MVP** (`docs/audit/10-mvp-consolidation-audit.md`): US-045 a US-048 (Mensagens), US-050 a US-051 (Métricas Administrativas), US-060 a US-063 (Campanhas).

---

## Histórias com Status PARCIAL

| Story | Motivo | Referência |
| ----- | ------ | ---------- |
| US-041 a US-044 | Comunicados PARCIAL — OQ-004 | `10-delivery-roadmap.md` |
