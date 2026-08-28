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
| RF-DOCUMENTO-003 | BR-012, BR-018, BR-020 | UC-DOCUMENTO-003 | GET /api/v1/pastas, GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-003 | TK-DOCUMENTO-001, TK-DOCUMENTO-002 | APPROVED |
| RF-DOCUMENTO-004 | — | UC-DOCUMENTO-004 | GET /api/v1/pastas, GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-004 | TK-DOCUMENTO-001, TK-DOCUMENTO-002 | APPROVED |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 4 | 4 | 0 |
| Regras de Negócio | 3 (BR-012, BR-018, BR-020, catálogo de domínio) | 3 | 0 |
| Casos de Uso | 4 | 4 | 0 |
| Endpoints | 2 | 2 | 0 |
| Acceptance Tests | 4 | 4 | 0 |
| Tasks | 3 | 3 | 0 |

---

# Dívidas Documentais Aceitas

- `BR-017`/`OQ-012` (herança de permissão em pastas, `FLG_HERDA_PERMISSAO`) seguem em aberto no catálogo de domínio — esta Feature não implementa herança, só grants diretos por pasta.
- Grant individual por colaborador (`TIP_DESTINATARIO=COLABORADOR`) existe no schema, decisão de produto de não usar nesta entrega.
- Modelo de dados **reconciliado (2026-08-26)** com o schema físico real (`database/ddl/003-create-tables.sql`) — `PASTA`, `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`, `PERMISSAO_PASTA` já instalados; nenhuma migration nova necessária para esta Feature.
- Provisionamento do Object Storage (DEC-013 aprovada) — dependência de execução registrada em `tasks.md` (TK-DOCUMENTO-002), não bloqueia DoR-Implementation. Cliente S3/MinIO ainda não existe em `backend/pom.xml`.

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
| 1.2 | 2026-08-26 | Claude Code (Specify) | Reconciliação com schema físico real: RF-004 novo, BR-018/BR-020 adicionadas |
