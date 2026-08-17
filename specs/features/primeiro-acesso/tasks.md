# Tasks — FT-PRIMEIRO-ACESSO (SDD ↔ Construction)

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Status | APPROVED (reconciliado 2026-08-17) |
| Versão | 1.1 |
| Papel no Framework | Referência de Task (Orchestrator CAP/Task) — `12-fullstack-orchestrator.md` |

---

# Papel deste arquivo

**SSOT de backlog e plano de implementação** (Etapa 2 simplificação).

| Artefato | Papel |
|----------|-------|
| `tasks.md` (este) | IDs TK-* + ordem + mapeamento RF/AT — **plano principal** |
| `execution-plan.md` | Legado — CDDs e riscos; não duplicar tasks |
| `pkg-*/status.md` | Legado — preferir CI logs no PR |
| `path-conventions.md` | Paths de código |

Framework: [`specs/foundation/development-workflow.md`](../../foundation/development-workflow.md).

---

# Identificadores e mapeamento PKG

| ID | Escopo | RF / AT | Backend PKG | Frontend PKG |
|----|--------|---------|-------------|--------------|
| TK-PA-001 | Resolução/listagem vínculos | RF-PA-001 · AT-PA-001..003 | pkg-02, pkg-04 | pkg-fe-01 |
| TK-PA-002 | Auto-seleção N=1 | RF-PA-002 · AT-PA-001 | pkg-02 | pkg-fe-02 |
| TK-PA-003 | Seleção N>1 | RF-PA-003 · AT-PA-002,009 | pkg-02 | pkg-fe-03 |
| TK-PA-004 | Persistência Contexto Ativo | RF-PA-004 · AT-PA-004 | **pkg-01**, pkg-02 | pkg-fe-01 |
| TK-PA-005 | Home dinâmica | RF-PA-005,006 · AT-PA-006 | **pkg-03** | pkg-fe-04 |
| TK-PA-006 | Alteração de contexto | RF-PA-007 · AT-PA-005 | pkg-02, pkg-03 | pkg-fe-05 |
| TK-PA-007 | Reentrada | RF-PA-008 · AT-PA-007 | pkg-02, pkg-04 | pkg-fe-05 |
| TK-PA-008 | Bloqueio sem vínculo | RF-PA-009 · AT-PA-003 | pkg-02 | pkg-fe-02 |
| TK-PA-009 | Contexto inválido | RF-PA-010 · AT-PA-008 | pkg-02 | pkg-fe-05 |
| TK-PA-010 | Integração FT-SESSION + guards | Matriz resp. | pkg-04 | **pkg-fe-01** |
| TK-PA-011 | Observabilidade/auditoria | RNF-PA-004,005 | **pkg-05** | pkg-fe-06 (evidência) |

---

# Lacunas fechadas no Planning (CDD)

| Lacuna SDD | Fechamento |
|------------|------------|
| INC-PA-001 N vínculos | CDD-PA-01 em `execution-plan.md` (backend) |
| INC-PA-004 persistência | CDD-PA-02 |
| INC-PA-003 /auth/me | CDD-PA-04 |
| Shape Home | CDD-PA-03 |

Detalhe físico (DDL) permanece no **pkg-01** — decisão de desenho já tomada; implementação não iniciada.
