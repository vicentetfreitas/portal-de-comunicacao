# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Atualizar / Alterar Status / Mover) |
| Versão | 1.0 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-GESTAO |
| Feature | Gestão de Pastas e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Consolida a rastreabilidade entre `specification.md`, `use-cases.md`, `api.md`,
`acceptance-tests.md`, `decisions.md` e `tasks.md`.

---

# Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK |
|----|----|----|-----|----|----|
| RF-DOC-GESTAO-001 | BR-016, BR-017 | UC-DOC-GESTAO-001 | POST /api/v1/pastas/{id}/subpastas | AT-DOC-GESTAO-001 | TK-DOC-GESTAO-001, TK-DOC-GESTAO-002 |
| RF-DOC-GESTAO-002 | — (DC-4) | UC-DOC-GESTAO-002 | PATCH /api/v1/pastas/{id} | AT-DOC-GESTAO-002 | TK-DOC-GESTAO-002 |
| RF-DOC-GESTAO-003 | BR-016, BR-017 | UC-DOC-GESTAO-003 | PATCH /api/v1/pastas/{id} | AT-DOC-GESTAO-003 | TK-DOC-GESTAO-002 |
| RF-DOC-GESTAO-004 | — (DC-4) | UC-DOC-GESTAO-004 | DELETE /api/v1/pastas/{id} | AT-DOC-GESTAO-004 | TK-DOC-GESTAO-002 |
| RF-DOC-GESTAO-005 | — (DC-4) | UC-DOC-GESTAO-005 | POST /api/v1/documentos/{id}/versoes | AT-DOC-GESTAO-005 | TK-DOC-GESTAO-003 |
| RF-DOC-GESTAO-006 | — (DC-4) | UC-DOC-GESTAO-006 | PATCH /api/v1/documentos/{id} | AT-DOC-GESTAO-006 | TK-DOC-GESTAO-003 |
| RF-DOC-GESTAO-007 | BR-018 | UC-DOC-GESTAO-007 | DELETE /api/v1/documentos/{id} | AT-DOC-GESTAO-007 | TK-DOC-GESTAO-003 |
| RF-DOC-GESTAO-008 | BR-015, BR-016 | UC-DOC-GESTAO-008 | PATCH /api/v1/documentos/{id} | AT-DOC-GESTAO-008 | TK-DOC-GESTAO-003 |
| RF-DOC-GESTAO-009 | BR-012 | UC-DOC-GESTAO-009 | (transversal) | AT-DOC-GESTAO-009 | TK-DOC-GESTAO-002, TK-DOC-GESTAO-003 |
| RF-DOC-GESTAO-010 | — (DC-2) | UC-DOC-GESTAO-010 | (transversal) | AT-DOC-GESTAO-010 | TK-DOC-GESTAO-002, TK-DOC-GESTAO-003 |

**RN `—` (DC-4):** operações puramente estruturais ou de ciclo de vida (renomear
pasta, arquivar pasta, nova versão, editar metadados) não têm Regra de Negócio
dedicada em `docs/domain/09-business-rules.md`. Não são requisitos órfãos — cada uma
tem UC, API e AT. Governança pode catalogar uma BR posteriormente (ver
`decisions.md` DC-4).

---

# Rastreabilidade das Decisões

| Decisão | Afeta | Onde está normatizada |
|---------|-------|------------------------|
| D-01 escopo | RF-001..008 | `specification.md` § Escopo |
| D-02 só `ADMINISTRADOR` | RF-009 | `specification.md` § Modelo de Autorização |
| D-03 app não gere grants | Fora de escopo | `specification.md` § Escopo |
| D-04 snapshot de grants | RF-001 | `specification.md` RF-DOC-GESTAO-001 |
| D-05 soft-delete de pasta vazia | RF-004 | `specification.md` RF-DOC-GESTAO-004 |
| D-06 excluir = arquivar | RF-007 | `specification.md` RF-DOC-GESTAO-007 |
| D-07 re-derivar categoria na versão | RF-005 | `specification.md` RF-DOC-GESTAO-005 |
| D-08 mover não toca grants + anti-ciclo | RF-003, RF-008 | `specification.md` RF-DOC-GESTAO-003/008 |
| DC-1 documento não-`ATIVO` recusa escrita | RF-005, RF-006, RF-008 | `specification.md` § Regras transversais de estado |
| DC-2 pasta inativa = inexistente p/ escrita | RF-010 | `specification.md` RF-DOC-GESTAO-010 |
| DC-3 "já arquivado" → `409` | RF-004, RF-007 | `specification.md` § Regras transversais de estado |
| DC-4 RN `—` em operações estruturais | RF-002/004/005/006 | esta matriz + `decisions.md` DC-4 |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 10 | 10 | 0 |
| Regras de Negócio | BR-012, BR-015, BR-016, BR-017, BR-018 | 5 | 0 |
| Casos de Uso | 10 | 10 | 0 |
| Endpoints | 6 + 2 transversais | 8 | 0 |
| Acceptance Tests | 10 | 10 | 0 |
| Tasks | 3 | 3 | 0 |

---

# Dívidas Documentais Aceitas

- `BR-023` (quota de armazenamento) — catalogada, não implementada (herdado de
  `FT-DOCUMENTO-UPLOAD`).
- Papel `GESTOR_DOCUMENTAL` não estendido — só `ADMINISTRADOR` (`decisions.md` D-02).
- Sem seletor de categoria na nova versão — sempre derivada do `TIP_MIME`.
- **Exclusão real de documento** — nesta Feature só arquivamento (`decisions.md`
  D-06); exclusão lógica que suma da leitura = Feature futura + migration.
- **Desarquivar documento / reativar pasta** — fora de escopo.
- `OQ-006` (revogação de permissão), `OQ-011` (alterar exposição pós-publicação),
  `OQ-012`/`BR-017` (herança de pasta) — **não resolvidas**; D-04 as evita via
  snapshot, não as fecha.
- Operações estruturais sem RN dedicada (DC-4).

## Bloqueantes de execução (não de spec)

- ✅ `SQ_ARQUIVO_BINARIO`/`SQ_DOCUMENTO_VERSAO` — `V009` executado (2026-08-27).
- ⏳ `SQ_PASTA` e `SQ_PERMISSAO_PASTA` **não existem** no baseline (12 sequences
  homologadas, nenhuma de pasta/permissão; tabelas sem `IDENTITY`).
  `RF-DOC-GESTAO-001` insere em ambas → requer `V010` (TK-DOC-GESTAO-001).
- ⏳ Grants `PERMISSAO_PASTA` (`EDICAO`) institucionais nas pastas de homologação.
- ⏳ Provisionamento do Object Storage (DEC-013).

## Ação de governança (monorepo, não bloqueia a Feature)

- Referenciar D-04 (snapshot de grants) e D-06 (excluir = arquivar) em
  `docs/domain/10-open-questions.md` (OQ-011, OQ-012).

---

# Validações Obrigatórias

- [x] Todos os RF possuem UC, API e AT
- [x] Todos os AT possuem RF associado
- [x] Todas as TK possuem RF associado (exceto TK-DOC-GESTAO-001, bloqueio de banco
      sem RF de produto próprio — ligada a RF-DOC-GESTAO-001)
- [x] Nenhum endpoint sem justificativa funcional (RF)
- [x] Matriz consistente com os demais artefatos
- [x] RN `—` justificadas (DC-4), sem requisito órfão

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — matriz RF/UC/API/AT/TK-DOC-GESTAO-001..010; rastreabilidade de D-01..D-08 + DC-1..DC-4; bloqueante `SQ_PASTA`/`SQ_PERMISSAO_PASTA` (`V010`) |
