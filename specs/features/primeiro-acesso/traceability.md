# Traceability — FT-PRIMEIRO-ACESSO

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Status | APPROVED (reconciliado 2026-08-17) |
| Versão | 1.1 |

---

# Cadeia oficial

```text
DEC → BR → RF → UC → Fluxos / Estados → API → AT → TK → Feature
```

---

# OQs encerradas ↔ DEC

| OQ | DEC | Status |
|----|-----|--------|
| OQ-001 | DEC-FA-001 | Encerrada |
| OQ-026 | DEC-FA-002 | Encerrada |
| OQ-027 | DEC-FA-003 | Encerrada (supersession parcial DH-02) |
| OQ-028 | DEC-FA-004 | Encerrada |
| — | DEC-ORG-001, DEC-ORG-003 | Vigente |
| — | DH-02, DH-03, DH-04 | Vigente |
| — | DH-PA-01, DH-PA-02, DH-PA-03 | Vigente |
| — | DH-CARGO-01 | Vigente |
| — | DEC-CMS-001 | Fronteira CMS |

---

# DEC → BR → RF (TO-BE)

| DEC | BR | RF |
|-----|----|----|
| DEC-FA-001, DH-03 | BR-011 | RF-PA-001, RF-PA-011 |
| DEC-FA-002, DH-04 | BR-010 | RF-PA-001, 009, 010 |
| DEC-FA-003 (P2/P3), DH-02 | BR-041, BR-012 | RF-PA-002, 008 |
| DEC-FA-004 | BR-042 | RF-PA-005, 006 |
| DEC-ORG-001, DEC-ORG-003 | BR-040, BR-043, BR-044 | RF-PA-011 |
| DH-PA-01 | BR-010 | RF-PA-001 |
| DH-CARGO-01 | BR-045 | — (CARGO fora do PA) |
| DEC-CMS-001 | — | RNF-PA-003 / AT-PA-010 |

### RF superseded

| RF | Motivo |
|----|--------|
| RF-PA-003 | Seleção N vínculos — DH-02 |
| RF-PA-004 | Persistência separada de Contexto Ativo — derivar de COLABORADOR |
| RF-PA-007 | Troca de contexto em sessão — DH-02 |

---

# Matriz consolidada RF → UC → API → AT → TK

| RF | RN/BR | UC | API | AT | TK | Status |
|----|-------|----|-----|----|----|--------|
| RF-PA-001 | BR-010,011 · RN-PA-004 | UC-PA-001 | PA-API-005 | AT-PA-001 | TK-PA-001 | RECONCILED |
| RF-PA-002 | BR-041 · RN-PA-002 | UC-PA-002, 008 | PA-API-005 | AT-PA-001 | TK-PA-002 | RECONCILED |
| RF-PA-003 | — | UC-PA-003, 004 | PA-API-001,003 | AT-PA-002 | TK-PA-003 | **SUPERSEDED** |
| RF-PA-004 | RN-PA-005 | UC-PA-005 | PA-API-002,003 | AT-PA-004 | TK-PA-004 | **SUPERSEDED** |
| RF-PA-005 | BR-042 | UC-PA-006 | PA-API-004 | AT-PA-006 | TK-PA-005 | RECONCILED |
| RF-PA-006 | BR-042 | UC-PA-006 | PA-API-004 | AT-PA-006 | TK-PA-005 | RECONCILED |
| RF-PA-007 | RN-PA-006 | UC-PA-007 | PA-API-003,004 | AT-PA-005 | TK-PA-006 | **SUPERSEDED** |
| RF-PA-008 | RN-PA-007 | UC-PA-008 | PA-API-005, 004 | AT-PA-007 | TK-PA-007 | RECONCILED |
| RF-PA-009 | BR-010,044 · RN-PA-004 | UC-PA-009 | PA-API-006 (pend.) | AT-PA-003 | TK-PA-008 | RECONCILED |
| RF-PA-010 | RN-PA-001 | UC-PA-010 | PA-API-005 | AT-PA-008,009 | TK-PA-009 | RECONCILED |
| RF-PA-011 | DH-03, DH-04 | UC-PA-002 | PA-API-006 (pend.) | — | — | RECONCILED |

---

# Inconsistências — reconciliação (2026-08-17)

| ID | Descrição original | Resolução |
|----|-------------------|-----------|
| INC-PA-001 | N vínculos vs 1 vínculo | **Encerrada** — BR-041 supersession + DH-02 |
| INC-PA-002 | Frontend fase 1 vs multi-contexto | **Encerrada** — modelo TO-BE = 1 vínculo derivado |
| INC-PA-003 | `/auth/me` singular vs `contexts[]` | **Encerrada** — `organizationalLinks` único mantido |
| INC-PA-004 | Persistência física Contexto Ativo | **Encerrada** — derivar de FKs `COLABORADOR`; sem store separado |
| INC-PA-005 | Template crud-feature | **Mantida** — nota processual; não bloqueia |
| INC-PA-006 | OQ-007 evento Colaborador Integrado | **Aberta** — fora escopo PA core |

---

# Cobertura

| Dimensão | Completo? |
|----------|-----------|
| Objetivo / escopo TO-BE | Sim |
| BR referenciadas | Sim |
| UC reconciliados | Sim (superseded marcados) |
| Fluxos + Mermaid TO-BE | Sim |
| Máquina de estados TO-BE | Sim |
| Contratos vigentes + superseded | Sim |
| Pendência PA-API-006 | Registrada |
| Tasks Construction registry | Fora escopo |
