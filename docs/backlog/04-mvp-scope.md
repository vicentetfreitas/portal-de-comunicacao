# MVP Scope — Portal de Comunicação

## Objetivo

Definir o escopo MVP oficial único do projeto.

**Fonte normativa (SSOT do escopo MVP):** este documento (`docs/backlog/04-mvp-scope.md`)  
**Evidence da consolidação:** `docs/audit/10-mvp-consolidation-audit.md` (não evolui regras novas)  
**Origem arquitetural:** `docs/solution-design/10-delivery-roadmap.md` — Etapas 1–5  
**Data de reconciliação:** 2026-06-22  
**Categoria documental:** SSOT

---

## MVP Oficial

O MVP produtivo corresponde à conclusão das **Etapas 1 a 5** do roadmap arquitetural, com capacidades **ATIVAS** validadas em Hml e migração núcleo AS-IS → TO-BE executada em Produção.

---

## Módulos do MVP

| # | Módulo | Bounded context / Natureza | Etapa | Épico |
| - | ------ | -------------------------- | ----- | ----- |
| 1 | Fundação da Plataforma | Transversal (infraestrutura) | 1 | EPIC-001 |
| 2 | Organização Corporativa | Organização Corporativa | 2 | EPIC-002 |
| 3 | Controle de Acesso | Controle de Acesso | 2 | EPIC-003 |
| 4 | Gestão Documental | Gestão Documental | 3 | EPIC-004 |
| 5 | Comunicação Interna | Comunicação Interna | 4 | EPIC-005 |
| 6 | Migração Operacional | Transversal | 5 | EPIC-006 |

**Excluídos do MVP:** EPIC-007 (Descomissionamento — Etapa 6, pós-MVP).

---

## Capacidades Obrigatórias

| Capacidade | Bounded context | Etapa | Feature |
| ---------- | --------------- | ----- | ------- |
| Autenticação Zimbra + sessão | Controle de Acesso | 2 | FEATURE-020, FEATURE-021 |
| Primeiro acesso / Contexto Ativo | Controle de Acesso + Organização | 2 | FEATURE-016 (DEC-FA-001; FT-PRIMEIRO-ACESSO) |
| Autorização papel + escopo | Controle de Acesso | 2 | FEATURE-022, FEATURE-023 |
| Estrutura organizacional | Organização Corporativa | 2 | FEATURE-010 a FEATURE-015, FEATURE-017 |
| Publicação e consulta documental | Gestão Documental | 3 | FEATURE-030, FEATURE-031 |
| Download autorizado | Gestão Documental + Controle de Acesso | 3 | FEATURE-034 |
| Separação metadado/binário | Gestão Documental | 3 | FEATURE-034 |
| Notificações in-app unificadas | Comunicação Interna | 4 | FEATURE-040 |
| Migração núcleo AS-IS → TO-BE | Transversal | 5 | FEATURE-050 a FEATURE-052 |
| Observabilidade base (logs, health) | Transversal | 1 | FEATURE-004 |

---

## Capacidades PARCIAL no MVP

| Capacidade | Feature | Condição | Etapa |
| ---------- | ------- | -------- | ----- |
| Comunicados institucionais | FEATURE-041 | OQ-004 | 4 |
| Auditoria inicial | FEATURE-025 | OQ-019 | 2 |
| Compartilhamento ↔ autorização | FEATURE-033 | OQ-005 | 3 |

**Tratamento:** PARCIAL conforme R-007 — expostas com limitação; não bloqueiam conclusão do núcleo ATIVO.

---

## Épicos Oficiais

| ID | Nome | Etapa | Status MVP |
| -- | ---- | ----- | ---------- |
| EPIC-001 | Fundação da Plataforma | 1 | Obrigatório |
| EPIC-002 | Organização Corporativa | 2 | Obrigatório |
| EPIC-003 | Controle de Acesso | 2 | Obrigatório |
| EPIC-004 | Gestão Documental | 3 | Obrigatório |
| EPIC-005 | Comunicação Interna | 4 | Obrigatório (núcleo + PARCIAL) |
| EPIC-006 | Migração Operacional | 5 | Obrigatório |

---

## Features Oficiais do MVP

FEATURE-001 a FEATURE-005, FEATURE-010 a FEATURE-015, FEATURE-016, FEATURE-017, FEATURE-020 a FEATURE-025, FEATURE-028, FEATURE-029, FEATURE-030 a FEATURE-037, FEATURE-040, FEATURE-041 (PARCIAL), FEATURE-049, FEATURE-050 a FEATURE-052.

**Removidas do MVP:** FEATURE-044 (Métricas Administrativas — pós-MVP), FEATURE-046 (Mensagens), FEATURE-070 (Campanhas).

---

## User Stories Oficiais

| Stories | Feature | Status |
| ------- | ------- | ------ |
| US-001, US-080, US-081 | Fundação / Observabilidade | Obrigatório |
| US-010 a US-014 | Acesso | Obrigatório |
| US-020 a US-024 | Organização | Obrigatório |
| US-030 a US-034 | Documental | Obrigatório |
| US-040 | Notificações | Obrigatório |
| US-041 a US-044 | Comunicados | PARCIAL (OQ-004) |
| US-070, US-071 | Migração | Obrigatório |

---

## Ordem de Implementação

```text
EPIC-001 → EPIC-002 + EPIC-003 → EPIC-004 → EPIC-005 → EPIC-006
```

**Documento origem:** `docs/solution-design/10-delivery-roadmap.md` — Dependências Entre Etapas.

---

## Exclusões do MVP

| Item | Motivo | Decisão |
| ---- | ------ | ------- |
| Gestão de Campanhas / EPIC-008 | Sem bounded context Domain | `10-mvp-consolidation-audit.md` C-002 |
| Gestão de Mensagens / FEATURE-046 | Sem conceito de domínio | `10-mvp-consolidation-audit.md` C-004 |
| Painel Operacional / FEATURE-044 | Métricas Administrativas — opcional R-016 | `10-mvp-consolidation-audit.md` C-003 |
| Descomissionamento / EPIC-007 | Etapa 6 — pós-MVP | `10-delivery-roadmap.md` |
| Aplicativo Mobile, Multi-idioma, IA, Analytics avançado | Fora do escopo | `01-mvp.md` — Não Incluído |

---

## Conflitos Resolvidos

| ID | Resolução | Referência |
| -- | --------- | ---------- |
| C-001 | Gestão Documental **mantida** no MVP Etapa 3 | `10-mvp-consolidation-audit.md` |
| C-002 | Campanhas **removida** do MVP | `10-mvp-consolidation-audit.md` |
| C-003 | Painel Operacional → Métricas Administrativas **pós-MVP** | `10-mvp-consolidation-audit.md` |
| C-004 | Comunicações → Comunicados; Mensagens **removidas** | `10-mvp-consolidation-audit.md` |
| C-005 | QST-001 **encerrada** — escopo consolidado | `docs/governance/05-roadmap.md` |

---

## MVP Identificado?

**SIM** — definição única consolidada em `docs/audit/10-mvp-consolidation-audit.md`, sincronizada neste documento.
