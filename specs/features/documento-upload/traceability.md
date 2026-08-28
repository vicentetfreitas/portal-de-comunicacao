# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — só criação) |
| Versão | 1.4 |
| Status | DONE |
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
- **Sem suíte E2E Playwright dedicada** (`AT-DOC-UPLOAD-*`) — decisão do usuário 2026-08-28 (fechar com cobertura unit + integração Oracle + smoke test manual end-to-end). Mesma dívida de `FT-DOCUMENTO-GESTAO`.
- Teto de tamanho de arquivo definido na implementação: **25 MB** (`spring.servlet.multipart.max-file-size`, override por `DOCUMENTO_MAX_FILE_SIZE`); acima → `413`.
- ✅ **RESOLVIDO (2026-08-27):** sequences `SQ_ARQUIVO_BINARIO`/`SQ_DOCUMENTO_VERSAO` — `V009` executado e validado (JDBC); grants para `UNMPORTCOM_APP_ROLE` concedidos. (`SQ_CAT_DOC_COD_CAT_DOC` = reconciliação greenfield, não bloqueio.)
- ✅ **RESOLVIDO (2026-08-27):** `CATEGORIA_DOCUMENTAL` — `V009` inseriu `Documentos`/`Imagens`/`Vídeos`/`Outros` (IDs 1–4), validado.
- ✅ **RESOLVIDO (2026-08-28):** Object Storage (DEC-013) — MinIO provisionado (`docker-compose.yml` em `portal-comunicacao-api`); caminho real upload → download validado end-to-end.
- **Bloqueante de execução, não de spec:** grants `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) nas pastas de **produção** são dado institucional (DBA). Homologação: `V011` provisionou a pasta 122 com grant `EDICAO` para a Área TI — AT-DOC-UPLOAD-001 validado contra ela.
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
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review de Spec — status APPROVED; dívidas atualizadas (categoria por mídia, `CATEGORIA_DOCUMENTAL` vazia, ação de governança OQ-004) |
| 1.2 | 2026-08-27 | Claude Code | Ajuste: `V009` só cria `SQ_ARQUIVO_BINARIO`/`SQ_DOCUMENTO_VERSAO` (bloqueio real); `SQ_CAT_DOC_COD_CAT_DOC` = reconciliação greenfield |
| 1.3 | 2026-08-27 | Claude Code | `V009` executado e validado — 2 dívidas de execução (sequences, categorias) fechadas |
| 1.4 | 2026-08-28 | Claude Code (Review) | Fechamento — `mvn verify` 399/0/2 (`DocumentoUploadAcceptanceIntegrationTest` 9/9); MinIO provisionado, caminho real validado; teto 25 MB registrado; dívida "sem E2E Playwright" registrada. Gate 3 + Gate 6 PASS. Header → `DONE`. |
