# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — somente leitura) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO |
| Feature | Arquivos e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Consolida a rastreabilidade entre `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `tasks.md`.

---

# Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK | Status |
|----|----|----|-----|----|----|--------|
| RF-DOCUMENTO-001 | — | UC-DOCUMENTO-001 | GET /api/v1/pastas | AT-DOCUMENTO-001 | TK-DOCUMENTO-001, TK-DOCUMENTO-003 | APPROVED |
| RF-DOCUMENTO-002 | — | UC-DOCUMENTO-002 | GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-002 | TK-DOCUMENTO-002, TK-DOCUMENTO-003 | APPROVED |
| RF-DOCUMENTO-003 | BR-012 | UC-DOCUMENTO-003 | GET /api/v1/pastas, GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-003 | TK-DOCUMENTO-001, TK-DOCUMENTO-002 | APPROVED |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 3 | 3 | 0 |
| Regras de Negócio | 1 (BR-012, catálogo de domínio) | 1 | 0 |
| Casos de Uso | 3 | 3 | 0 |
| Endpoints | 2 | 2 | 0 |
| Acceptance Tests | 3 | 3 | 0 |
| Tasks | 3 | 3 | 0 |

---

# Dívidas Documentais Aceitas

- `BR-019`/`BR-020`/`OQ-011`/`OQ-013` permanecem em aberto no catálogo de domínio para uma eventual Feature de compartilhamento futura entre Áreas/Singulares — não se aplicam a esta Feature (fora de escopo, decisão de produto 2026-08-26, ver `specification.md`).
- Modelo de dados (`PASTA`/`DOCUMENTO`) identificado em `specification.md`, mas sem revisão formal de arquitetura/DBA nem DDL — dependência de execução registrada em `tasks.md` (TK-DOCUMENTO-001), não bloqueia DoR-Implementation.
- Provisionamento do Object Storage (DEC-013 aprovada) — dependência de execução registrada em `tasks.md` (TK-DOCUMENTO-002), não bloqueia DoR-Implementation.

---

# Validações Obrigatórias

- [x] Todos os RF possuem UC, API e AT
- [x] Todos os AT possuem RF associado
- [x] Todas as TK possuem RF associado
- [x] Nenhum endpoint sem justificativa funcional (RF)
- [x] Matriz consistente com `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `tasks.md`

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — cobertura completa de RF/UC/API/AT para DoR-Spec |
