# Prioritization — Portal de Comunicação

## Objetivo

Classificar épicos, features e entregas conforme prioridade documentada em Solution Design, Implementation e Construction.

**Critérios aplicados:**

| Prioridade | Critério |
| ---------- | -------- |
| **P0** | Bloqueia arquitetura ou MVP |
| **P1** | Necessário para MVP |
| **P2** | Pós-MVP |
| **P3** | Evoluções futuras |

**Fontes:** `docs/solution-design/10-delivery-roadmap.md`, `docs/implementation/01-implementation-backlog.md`, `docs/construction/delivery/01-mvp.md`, `docs/construction/delivery/02-release-plan.md`

**Data de consolidação:** 2026-06-22  
**Última reconciliação:** 2026-08-14 — FEATURE-016 alinhada a DEC-FA-001 e `04-mvp-scope.md`  
**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado em 2026-06-22

---

## Ordem de Implementação Documentada

**Origem:** `docs/solution-design/10-delivery-roadmap.md` — Dependências Entre Etapas

```text
1. Fundação da Plataforma
2. Núcleo Organizacional (Organização + Acesso)
3. Gestão Documental
4. Comunicação Interna
5. Migração Operacional
6. Descomissionamento
```

**Autorização de início:** `docs/implementation/00-architecture-readiness.md` — GO para Etapa 1.

---

## P0 — Bloqueia arquitetura / MVP

### Épicos

- **EPIC-001** — Fundação da Plataforma
- **EPIC-002** — Organização Corporativa (upstream ADR-013)
- **EPIC-003** — Controle de Acesso (auth/autorização pré-requisito)

**Justificativa:** Etapas 1 e 2 sem dependências upstream satisfeitas bloqueiam toda construção TO-BE (`10-delivery-roadmap.md` — "Impacto se violada: Nenhuma construção TO-BE possível" / "demais fluxos bloqueados").

### Features

| Feature | Justificativa |
| ------- | ------------- |
| FEATURE-001 a FEATURE-005 | Critérios de prontidão Etapa 1 |
| FEATURE-015, FEATURE-010 a FEATURE-014 | Capacidades obrigatórias Etapa 2 — estrutura organizacional |
| FEATURE-020 a FEATURE-025, FEATURE-028 | Autenticação Zimbra, sessão, autorização — obrigatórias Etapa 2 |
| FEATURE-016 | Primeiro acesso / resolução de Contexto Ativo — bloqueia operação pós-auth (DEC-FA-001; FT-PRIMEIRO-ACESSO) |
| FEATURE-004 | Observabilidade base desde Etapa 1; incluída Release 1 |

### User Stories

US-001, US-010 a US-014, US-020 a US-024, US-080

---

## P1 — Necessário para MVP

### Épicos

- **EPIC-004** — Gestão Documental (Etapa 3 — Solution Design)
- **EPIC-005** — Comunicação Interna (Etapa 4 — notificações ATIVO)
- **EPIC-006** — Migração Operacional (Etapa 5 — MVP produtivo com legado)

**Justificativa:** Capacidades obrigatórias Etapas 3–5 (`10-delivery-roadmap.md` — Capacidades Prioritárias Obrigatórias). Release 1 = MVP (`02-release-plan.md`).

### Features — Solution Design (obrigatórias)

| Feature | Etapa | Capacidade |
| ------- | ----- | ---------- |
| FEATURE-030 a FEATURE-037 | 3 | Publicação, consulta, download, metadado/binário |
| FEATURE-040 | 4 | Notificações in-app unificadas |
| FEATURE-050 a FEATURE-052 | 5 | Migração AS-IS → TO-BE |

### Features — Comunicação Interna (MVP)

| Feature | Etapa | Capacidade | Classificação |
| ------- | ----- | ---------- | ------------- |
| FEATURE-040 | 4 | Notificações in-app unificadas | Obrigatório |
| FEATURE-041 | 4 | Comunicados institucionais | PARCIAL — OQ-004 |
| FEATURE-049 | 4 | Apresentação de Comunicação | Obrigatório |

### User Stories

US-030 a US-034, US-040, US-041 a US-044, US-070, US-071, US-081

---

## P2 — Pós-MVP

### Épicos

- **EPIC-007** — Descomissionamento (Etapa 6 — depende Etapa 5)

### Features

| Feature | Justificativa |
| ------- | ------------- |
| FEATURE-026, FEATURE-027 | Solicitação permissão, perfis externos — OQ-002, OQ-003 |
| FEATURE-041 (pleno) | Comunicados — após OQ-004 |
| FEATURE-042, FEATURE-043, FEATURE-045 | PARCIAL AS-IS |
| FEATURE-044 | Métricas Administrativas — pós-MVP (C-003) |
| FEATURE-048 | Webhook/E-mail — opcional Etapa 4 |
| FEATURE-060 a FEATURE-062 | Descomissionamento — Etapa 6 |

### Releases

**Release 2** (`02-release-plan.md`): novas integrações, melhorias operacionais, evoluções de UX

---

## P3 — Evoluções futuras

### Exclusões MVP (`01-mvp.md`)

- Aplicativo Mobile
- Multi-idioma
- IA Generativa
- Automações avançadas
- Segmentação avançada
- Analytics avançado
- Integrações não críticas

### Release 3 (`02-release-plan.md`)

Analytics, automações, recursos avançados

### Capacidades opcionais (`10-delivery-roadmap.md`)

- Escalabilidade horizontal — decisão pendente; R-014
- Central de Colaboração plena

---

## Matriz de Priorização Consolidada

| ID | Tipo | Nome | Prioridade | MVP | Etapa |
| -- | ---- | ---- | ---------- | --- | ----- |
| EPIC-001 | Epic | Fundação da Plataforma | P0 | Sim | 1 |
| EPIC-002 | Epic | Organização Corporativa | P0 | Sim | 2 |
| EPIC-003 | Epic | Controle de Acesso | P0 | Sim | 2 |
| EPIC-004 | Epic | Gestão Documental | P1 | Sim (SD) | 3 |
| EPIC-005 | Epic | Comunicação Interna | P1 | Sim (parcial) | 4 |
| EPIC-006 | Epic | Migração Operacional | P1 | Sim | 5 |
| EPIC-007 | Epic | Descomissionamento | P2 | Não | 6 |

---

## Matriz Epic → Feature → Story → MVP → Prioridade

| Epic | Feature | Story | MVP | Prioridade |
| ---- | ------- | ----- | --- | ---------- |
| EPIC-001 | FEATURE-001 | US-001 | Sim | P0 |
| EPIC-001 | FEATURE-004 | US-080 | Sim | P0 |
| EPIC-003 | FEATURE-020 | US-010, US-011 | Sim | P0 |
| EPIC-003 | FEATURE-021 | US-012 | Sim | P0 |
| EPIC-003 | FEATURE-023 | US-013 | Sim | P0 |
| EPIC-003 | FEATURE-016 | — | Sim | P0 |
| EPIC-003 | FEATURE-025 | US-014 | Sim | P0 |
| EPIC-002 | FEATURE-010 | US-020 | Sim (SD) | P0 |
| EPIC-002 | FEATURE-011 | US-021 | Sim (SD) | P0 |
| EPIC-002 | FEATURE-012 | US-022 | Sim (SD) | P0 |
| EPIC-002 | FEATURE-013 | US-023 | Sim (SD) | P0 |
| EPIC-002 | FEATURE-014 | US-024 | Sim (SD) | P0 |
| EPIC-004 | FEATURE-030 | US-030 | Sim (SD) | P1 |
| EPIC-004 | FEATURE-031 | US-031 | Sim (SD) | P1 |
| EPIC-004 | FEATURE-034 | US-032 | Sim (SD) | P1 |
| EPIC-004 | FEATURE-033 | US-033 | Sim (SD) | P1 |
| EPIC-004 | FEATURE-035 | US-034 | Sim (SD) | P1 |
| EPIC-005 | FEATURE-040 | US-040 | Sim | P1 |
| EPIC-005 | FEATURE-041 | US-041–044 | Sim (PARCIAL) | P1 |
| EPIC-006 | FEATURE-050 | US-070 | Sim | P1 |
| EPIC-006 | FEATURE-051 | US-071 | Sim | P1 |

---

# BACKLOG CONSOLIDATION REPORT

## Épicos encontrados

**Quantidade:** 7

| ID | Nome | Origem principal |
| -- | ---- | ---------------- |
| EPIC-001 | Fundação da Plataforma | `10-delivery-roadmap.md` |
| EPIC-002 | Organização Corporativa | `05-bounded-contexts.md` |
| EPIC-003 | Controle de Acesso | `05-bounded-contexts.md` |
| EPIC-004 | Gestão Documental | `05-bounded-contexts.md` |
| EPIC-005 | Comunicação Interna | `05-bounded-contexts.md` |
| EPIC-006 | Migração Operacional | `10-delivery-roadmap.md` |
| EPIC-007 | Descomissionamento | `10-delivery-roadmap.md` |

---

## Features encontradas

**Quantidade:** 36

| Epic | Quantidade |
| ---- | ---------- |
| EPIC-001 | 5 |
| EPIC-002 | 8 |
| EPIC-003 | 10 |
| EPIC-004 | 8 |
| EPIC-005 | 9 |
| EPIC-006 | 3 |
| EPIC-007 | 3 |

---

## User Stories geradas

**Quantidade:** 24

| Categoria | Quantidade |
| --------- | ---------- |
| Fundação / MVP global | 3 |
| Autenticação e Acesso | 5 |
| Organização | 5 |
| Gestão Documental | 5 |
| Comunicação / Notificações | 5 |
| Migração | 2 |

---

## MVP identificado

**SIM** — definição única em `docs/audit/10-mvp-consolidation-audit.md`, sincronizada em `04-mvp-scope.md`. QST-001 encerrada.

---

## Conflitos resolvidos

| ID | Descrição | Resolução |
| -- | --------- | --------- |
| C-001 | Gestão Documental no MVP | Mantida — Etapa 3 |
| C-002 | Campanhas | Removida do MVP |
| C-003 | Painel vs. Métricas Administrativas | Pós-MVP |
| C-004 | Comunicações/Mensagens | Comunicados PARCIAL; Mensagens removidas |
| C-005 | Escopo MVP consolidado | QST-001 encerrada |

---

## Consistência validada

| Regra | Resultado |
| ----- | --------- |
| Toda User Story possui Feature | ✓ |
| Toda Feature possui Epic | ✓ |
| Todo Epic possui origem documental | ✓ |
| Itens MVP possuem rastreabilidade | ✓ |
| Prioridades possuem justificativa documental | ✓ |
| MVP consolidado sincronizado | ✓ |

---

## Nível de confiança

**Alto** — backlog reconciliado com `docs/audit/10-mvp-consolidation-audit.md`.

---

## Recomendação

```text
READY
```

**Justificativa:**

- Backlog materializado e rastreável à documentação oficial.
- MVP único sincronizado em backlog, construction e governança.
- Conflitos C-001 a C-005 resolvidos e aplicados.
- Ordem de implementação derivada de `10-delivery-roadmap.md`.

**Próximo passo documentado:** Sprint planning — fora do escopo desta reconciliação.

---

## Artefatos gerados

| Arquivo | Conteúdo |
| ------- | -------- |
| `01-epics.md` | 7 épicos |
| `02-features.md` | 36 features |
| `03-user-stories.md` | 24 user stories |
| `04-mvp-scope.md` | MVP único oficial |
| `05-prioritization.md` | P0–P3 + matriz + este relatório |
