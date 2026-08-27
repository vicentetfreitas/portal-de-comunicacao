# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Fase 1: criação; Fase 2: update/soft-delete/versão) |
| Versão | 2.0 |
| Status | DRAFT (Fase 2 — aguarda Review de Spec) |
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

Consolida a rastreabilidade entre `specification.md`, `use-cases.md`, `api.md`,
`acceptance-tests.md`, `decisions.md` e `tasks.md`.

---

# Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK | Fase | Estado |
|----|----|----|-----|----|----|------|--------|
| RF-DOC-UPLOAD-001 | — | UC-DOC-UPLOAD-001 | POST /api/v1/pastas/{id}/documentos | AT-DOC-UPLOAD-001 | TK-DOC-UPLOAD-001, -002, -003 | 1 | Implementado |
| RF-DOC-UPLOAD-002 | BR-012 | UC-DOC-UPLOAD-002 | (transversal — toda escrita) | AT-DOC-UPLOAD-002 | TK-DOC-UPLOAD-002, -005, -006 | 1 + 2 | Fase 1 implementada; Fase 2 DRAFT |
| RF-DOC-UPLOAD-003 | — | UC-DOC-UPLOAD-003 | (transversal — toda escrita) | AT-DOC-UPLOAD-003 | TK-DOC-UPLOAD-002, -005, -006 | 1 + 2 | Fase 1 implementada; Fase 2 DRAFT |
| RF-DOC-UPLOAD-004 | BR-016, BR-017 | UC-DOC-UPLOAD-004 | POST /api/v1/pastas/{id}/subpastas | AT-DOC-UPLOAD-004 | TK-DOC-UPLOAD-004, -005 | 2 | DRAFT |
| RF-DOC-UPLOAD-005 | BR-016 | UC-DOC-UPLOAD-005 | PATCH /api/v1/pastas/{id} | AT-DOC-UPLOAD-005 | TK-DOC-UPLOAD-005 | 2 | DRAFT |
| RF-DOC-UPLOAD-006 | BR-016, BR-017 | UC-DOC-UPLOAD-006 | PATCH /api/v1/pastas/{id} | AT-DOC-UPLOAD-006 | TK-DOC-UPLOAD-005 | 2 | DRAFT |
| RF-DOC-UPLOAD-007 | BR-016 | UC-DOC-UPLOAD-007 | DELETE /api/v1/pastas/{id} | AT-DOC-UPLOAD-007 | TK-DOC-UPLOAD-005 | 2 | DRAFT |
| RF-DOC-UPLOAD-008 | BR-015 | UC-DOC-UPLOAD-008 | POST /api/v1/documentos/{id}/versoes | AT-DOC-UPLOAD-008 | TK-DOC-UPLOAD-006 | 2 | DRAFT |
| RF-DOC-UPLOAD-009 | BR-015 | UC-DOC-UPLOAD-009 | PATCH /api/v1/documentos/{id} | AT-DOC-UPLOAD-009 | TK-DOC-UPLOAD-006 | 2 | DRAFT |
| RF-DOC-UPLOAD-010 | BR-018 | UC-DOC-UPLOAD-010 | DELETE /api/v1/documentos/{id} | AT-DOC-UPLOAD-010 | TK-DOC-UPLOAD-006 | 2 | DRAFT |
| RF-DOC-UPLOAD-011 | BR-015, BR-016 | UC-DOC-UPLOAD-011 | PATCH /api/v1/documentos/{id} | AT-DOC-UPLOAD-011 | TK-DOC-UPLOAD-006 | 2 | DRAFT |

---

# Rastreabilidade das Decisões (Fase 2)

| Decisão | Afeta | Onde está normatizada |
|---------|-------|------------------------|
| D-01 escopo Fase 2 | RF-004..011 | `specification.md` § Escopo |
| D-02 só `ADMINISTRADOR` | RF-002 | `specification.md` § Modelo de Autorização |
| D-03 app não gere grants | Fora de escopo | `specification.md` § Escopo |
| D-04 snapshot de grants | RF-004 | `specification.md` RF-DOC-UPLOAD-004 |
| D-05 soft-delete de pasta vazia | RF-007 | `specification.md` RF-DOC-UPLOAD-007 |
| D-06 excluir = arquivar | RF-010 | `specification.md` RF-DOC-UPLOAD-010 |
| D-07 re-derivar categoria na versão | RF-008 | `specification.md` RF-DOC-UPLOAD-008 |
| D-08 mover não toca grants | RF-006, RF-011 | `specification.md` RF-DOC-UPLOAD-006/011 |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 11 | 11 | 0 |
| Regras de Negócio | BR-012, BR-015, BR-016, BR-017, BR-018 | 5 | 0 |
| Casos de Uso | 11 | 11 | 0 |
| Endpoints | 7 (1 Fase 1 + 6 Fase 2) | 7 | 0 |
| Acceptance Tests | 11 | 11 | 0 |
| Tasks | 7 (3 Fase 1 + 4 Fase 2) | 7 | 0 |

---

# Dívidas Documentais Aceitas

- `BR-023` (quota de armazenamento) — catalogada, não implementada (herdado da Fase 1).
- Papel `GESTOR_DOCUMENTAL` não estendido — só `ADMINISTRADOR` (`decisions.md` D-02).
- Sem seletor de categoria — sempre derivada do `TIP_MIME` (Fase 1 e nova versão).
- **Exclusão real de documento** — nesta fase só arquivamento (`decisions.md` D-06);
  exclusão lógica que suma da leitura = fase futura + migration.
- **Desarquivar documento / reativar pasta** — fora de escopo desta fase.
- `OQ-006` (revogação de permissão), `OQ-011` (alterar exposição pós-publicação),
  `OQ-012`/`BR-017` (herança de pasta) — **não resolvidas** por esta Feature; D-04
  as evita via snapshot, não as fecha.

## Bloqueantes de execução (não de spec)

- ✅ **Fase 1 (2026-08-27):** `SQ_ARQUIVO_BINARIO`/`SQ_DOCUMENTO_VERSAO` +
  `CATEGORIA_DOCUMENTAL` — `V009` executado e validado.
- ⏳ **Fase 2:** `SQ_PASTA` e `SQ_PERMISSAO_PASTA` **não existem** no baseline
  (12 sequences homologadas, nenhuma de pasta/permissão; tabelas sem `IDENTITY`).
  `RF-DOC-UPLOAD-004` insere em ambas → requer `V010` (TK-DOC-UPLOAD-004).
- ⏳ Grants `PERMISSAO_PASTA` (`EDICAO`) institucionais nas pastas de homologação —
  mesma pendência da Fase 1.
- ⏳ Provisionamento do Object Storage (DEC-013).

## Ação de governança (monorepo, não bloqueia a Feature)

- Referenciar D-04 (snapshot de grants) e D-06 (excluir = arquivar) em
  `docs/domain/10-open-questions.md` (OQ-011, OQ-012) e, se adotado,
  `docs/technology/04-decision-log.md`.

---

# Validações Obrigatórias

- [x] Todos os RF possuem UC, API e AT
- [x] Todos os AT possuem RF associado
- [x] Todas as TK possuem RF associado (exceto TK-DOC-UPLOAD-001 e -004, ligadas a
      bloqueios de banco sem RF de produto próprio)
- [x] Nenhum endpoint sem justificativa funcional (RF)
- [x] Matriz consistente com os demais artefatos

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0–1.3 | 2026-08-27 | Claude Code | Fase 1 — cobertura completa; `V009` executado |
| 2.0 | 2026-08-27 | Claude Code (Specify) | Fase 2 (DRAFT): RF/UC/API/AT/TK-004..011; rastreabilidade das 8 decisões; bloqueante `SQ_PASTA`/`SQ_PERMISSAO_PASTA` (`V010`) |
