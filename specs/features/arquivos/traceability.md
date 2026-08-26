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

Consolida a rastreabilidade entre `specification.md`, `use-cases.md`, `api.md` e `acceptance-tests.md`. `tasks.md` ainda não existe — não exigido para DoR-Spec (`definition-of-ready.md`); será acrescentado no DoR-Implementation.

---

# Matriz de Rastreabilidade Consolidada

| RF | UC | API | AT | TK | Status |
|----|----|-----|----|----|--------|
| RF-DOCUMENTO-001 | UC-DOCUMENTO-001 | GET /api/v1/pastas | AT-DOCUMENTO-001 | — (DoR-Implementation) | DRAFT |
| RF-DOCUMENTO-002 | UC-DOCUMENTO-002 | GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-002 | — (DoR-Implementation) | DRAFT |
| RF-DOCUMENTO-003 | UC-DOCUMENTO-003 | GET /api/v1/pastas, GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-003 | — (DoR-Implementation) | DRAFT |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 3 | 3 | 0 |
| Casos de Uso | 3 | 3 | 0 |
| Endpoints | 2 | 2 | 0 |
| Acceptance Tests | 3 | 3 | 0 |
| Tasks | — | — | pendente de DoR-Implementation |

---

# Dívidas Documentais Aceitas

- Sem `RN-DOCUMENTO-*` dedicado: regras de negócio (só própria Área, somente leitura) estão em `specification.md` como decisão de produto (2026-08-26), não como `BR-*` formal em `docs/domain/09-business-rules.md` — `BR-019`/`BR-020`/`OQ-011`/`OQ-013` permanecem em aberto no catálogo de domínio para uma eventual Feature de compartilhamento futura, mas não bloqueiam esta Feature (fora de escopo, ver `specification.md`).
- Modelo de dados (`PASTA`/`DOCUMENTO`) proposto em `specification.md`, sem revisão formal de arquitetura/DBA nem DDL — fica para quando a Feature avançar a `APPROVED`/DoR-Implementation.

---

# Validações Obrigatórias

- [x] Todos os RF possuem UC, API e AT
- [x] Todos os AT possuem RF associado
- [ ] Todas as TK possuem RF associado — `tasks.md` não existe ainda (não exigido para DoR-Spec)
- [x] Nenhum endpoint sem justificativa funcional (RF)
- [x] Matriz consistente com `specification.md`, `use-cases.md`, `api.md` e `acceptance-tests.md`

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — cobertura completa de RF/UC/API/AT para DoR-Spec |
