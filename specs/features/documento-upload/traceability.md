# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — só criação) |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-UPLOAD |
| Feature | Upload de Arquivos e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Consolida a rastreabilidade entre `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `tasks.md`.

---

# Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK | Status |
|----|----|----|-----|----|----|--------|
| RF-DOC-UPLOAD-001 | — | UC-DOC-UPLOAD-001 | POST /api/v1/pastas/{id}/documentos | AT-DOC-UPLOAD-001 | TK-DOC-UPLOAD-001, TK-DOC-UPLOAD-002, TK-DOC-UPLOAD-003 | APPROVED |
| RF-DOC-UPLOAD-002 | BR-012 | UC-DOC-UPLOAD-002 | POST /api/v1/pastas/{id}/documentos | AT-DOC-UPLOAD-002 | TK-DOC-UPLOAD-002 | APPROVED |
| RF-DOC-UPLOAD-003 | — | UC-DOC-UPLOAD-003 | POST /api/v1/pastas/{id}/documentos | AT-DOC-UPLOAD-003 | TK-DOC-UPLOAD-002 | APPROVED |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 3 | 3 | 0 |
| Regras de Negócio | 1 (BR-012) | 1 | 0 |
| Casos de Uso | 3 | 3 | 0 |
| Endpoints | 1 | 1 | 0 |
| Acceptance Tests | 3 | 3 | 0 |
| Tasks | 3 | 3 | 0 |

---

# Dívidas Documentais Aceitas

- `BR-023` (quota de armazenamento, `docs/domain/09-business-rules.md`) catalogada mas **não implementada** nesta Feature — decisão de produto explícita (ver `specification.md` § Fora do Escopo).
- Papel `GESTOR_DOCUMENTAL` (seed em `PAPEL`) não estendido a esta Feature — só `ADMINISTRADOR` autorizado, decisão de produto explícita.
- **Sem seletor de categoria no upload** — categoria derivada do `TIP_MIME`. A reconciliação da taxonomia de `CATEGORIA_DOCUMENTAL` com o produto e um seletor ficam para Feature futura de categorização.
- Tamanho máximo de arquivo aceito não definido pelo usuário — a decidir em `tasks.md`/implementação (`specification.md` § Decisão de produto/arquitetura pendente, item 5); acima do teto → `413`.
- **Bloqueante de execução, não de spec:** sequences `SQ_ARQUIVO_BINARIO`/`SQ_DOCUMENTO_VERSAO`/`SQ_CAT_DOC_COD_CAT_DOC` inexistentes — `TK-DOC-UPLOAD-001` propõe migration para o DBA; `TK-DOC-UPLOAD-002` não pode iniciar antes dela ser executada.
- **Bloqueante de execução, não de spec:** `CATEGORIA_DOCUMENTAL` vazia no banco — DML das 4 categorias de mídia proposto em `TK-DOC-UPLOAD-001`.
- **Bloqueante de execução, não de spec:** grants `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) podem não existir ainda nos dados institucionais — a confirmar com o DBA antes de `TK-DOC-UPLOAD-002`.
- **Ação de governança (monorepo, não bloqueia a Feature):** registrar em `docs/technology/04-decision-log.md` e `docs/domain/10-open-questions.md` (OQ-004) a redefinição de `CATEGORIA_DOCUMENTAL` (tipo de mídia) e `Comunicado` = publicação WordPress.

---

# Validações Obrigatórias

- [x] Todos os RF possuem UC, API e AT
- [x] Todos os AT possuem RF associado
- [x] Todas as TK possuem RF associado (exceto TK-DOC-UPLOAD-001, ligada à Feature como um todo — bloqueio de banco, sem RF de produto próprio)
- [x] Nenhum endpoint sem justificativa funcional (RF)
- [x] Matriz consistente com `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `tasks.md`

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — cobertura completa de RF/UC/API/AT/TK para futura DoR-Spec |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review de Spec — status APPROVED; dívidas atualizadas (categoria por mídia, `CATEGORIA_DOCUMENTAL` vazia, sequence de categoria, ação de governança OQ-004) |
