# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — só criação, backend estende `FT-DOCUMENTO`) |
| Versão | 1.2 |
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

Decomposição funcional de `FT-DOCUMENTO-UPLOAD` em unidades de implementação. Não representa planejamento de construção/cronograma — responsabilidade de `construction/`. **Este `tasks.md` é plano prospectivo (Feature em `APPROVED`)** — não autoriza implementação; `IMPLEMENTING` exige DoR-Implementation, ainda não avaliado.

**Natureza da Feature:** estende `FT-DOCUMENTO` (backend/frontend já existentes e `DONE`) — reaproveita entidades (`CategoriaDocumentalEntity`/`Repository` inclusive), `PermissaoPastaDomainService`, `PastaController`/`DocumentoController`. Bloqueada por dependências de execução fora do controle da aplicação (sequences Oracle ausentes + `CATEGORIA_DOCUMENTAL` vazia — ver TK-DOC-UPLOAD-001).

---

## TK-DOC-UPLOAD-001 — Script de banco: sequences + categorias de mídia

### Objetivo

Produzir o script SQL (execução **manual** na IDE do banco — `DEC-DB-019`, sem Flyway) que cria o que falta para a escrita em Gestão Documental. A aplicação não executa DDL/DML institucional.

Ausências confirmadas em 2026-08-27 (`ddl/002-create-sequences.sql` só tem `SQ_DOCUMENTO_COD_DOCUMENTO`; `CATEGORIA_DOCUMENTAL` e as tabelas de documento com 0 linhas no Oracle TST):

- `SQ_ARQUIVO_BINARIO` (`ARQUIVO_BINARIO.COD_ARQUIVO_BINARIO`) — bloqueio: backend insere via JPA `GenerationType.SEQUENCE`
- `SQ_DOCUMENTO_VERSAO` (`DOCUMENTO_VERSAO.COD_DOCUMENTO_VERSAO`) — idem
- 4 linhas em `CATEGORIA_DOCUMENTAL`: `Documentos`, `Imagens`, `Vídeos`, `Outros` (`FLG_ATIVO='S'`, ID explícito — a aplicação nunca insere categoria)

`SQ_CAT_DOC_COD_CAT_DOC` (referenciada por `008-initial-data.sql`) **não** é necessária aqui — é item de reconciliação greenfield do baseline.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-001 (bloqueante — sem sequence não há `INSERT`; sem categoria a FK `NOT NULL` não é satisfazível)

### Dependências

- Nenhuma — é o primeiro bloqueio a resolver.

### Componentes Esperados

- **[proposto — 2026-08-27]** `database/migrations/V009__documento_upload_sequences_e_categorias.sql` (SQL simples): 2× `CREATE SEQUENCE` + 2× `GRANT SELECT` p/ `UNMPORTCOM_APP_ROLE` (DEC-DB-024) + 4× `INSERT` em `CATEGORIA_DOCUMENTAL` (ID 1–4) + `COMMIT` + seção de conferência.
- **[proposto — 2026-08-27]** `database/migrations/VAL-DB-03-verify-documento-upload-prereqs.sql`: conferência read-only pré-V009.
- **[feito — 2026-08-27]** `database/migrations/README.md`: linha V009 + seção "O que é esta pasta (não é Flyway)" + "Reconciliação greenfield pendente".

### Critérios de Conclusão

- `V009` **executado** no ambiente (usuário/DBA na IDE) e validado (Claude confere via consulta read-only).
- Sequences criadas + grants + 4 categorias ativas antes de `TK-DOC-UPLOAD-002`.
- Reconciliação greenfield do baseline (`002`/`007`/`008`/`V902`) — ver `README.md` — registrada para o DBA.

---

## TK-DOC-UPLOAD-002 — Implementar upload de documento (backend)

### Objetivo

Endpoint `POST /api/v1/pastas/{id}/documentos` (multipart) criando `DOCUMENTO`/`DOCUMENTO_VERSAO`/`ARQUIVO_BINARIO`, autorizado por papel `ADMINISTRADOR` + grant `EDICAO` (`specification.md` § Modelo de Autorização).

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-001, RF-DOC-UPLOAD-002, RF-DOC-UPLOAD-003

### Casos de Uso Relacionados

- UC-DOC-UPLOAD-001, UC-DOC-UPLOAD-002, UC-DOC-UPLOAD-003

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-001, AT-DOC-UPLOAD-002, AT-DOC-UPLOAD-003

### Dependências

- **TK-DOC-UPLOAD-001** (sequences executadas + `CATEGORIA_DOCUMENTAL` populada no ambiente).
- `ObjectStorageClient` — adicionar método de escrita (`documento/application/port/`); `PermissaoPastaDomainService` — estender para checar `TIP_ACESSO='EDICAO'` e papel `ADMINISTRADOR` da atribuição ativa (não duplicar a checagem multi-nível já existente).
- Grants `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) já provisionados nas pastas de teste/homologação (dado institucional, `database/dml/`).

### Componentes Esperados

- Repository: `INSERT` em `DocumentoRepository`, `DocumentoVersaoRepository`, `ArquivoBinarioRepository` (já existem via `JpaRepository`, sem repositório novo); leitura via `CategoriaDocumentalRepository` (já existe — adicionar finder por `NOM_CATEGORIA` + `FLG_ATIVO`).
- Resolução de categoria: mapear `TIP_MIME` → `NOM_CATEGORIA` (`Documentos`/`Imagens`/`Vídeos`/`Outros`) e buscar o `COD_CATEGORIA_DOCUMENTAL` — nunca ID fixo; falha explícita (fail-fast) se a categoria não existir. Definir a tabela `TIP_MIME` → categoria (base em `specification.md` § Categorização por tipo de mídia).
- `COD_COLABORADOR` de `DOCUMENTO` e `DOCUMENTO_VERSAO`: do `JwtAuthenticatedPrincipal`, nunca do request.
- Teto de tamanho de arquivo: aplicar limite operacional (`spring.servlet.multipart.max-file-size` + validação explícita → `413`); valor definido aqui.
- Application Service: novo método (ex. `PastaApplicationService.uploadDocumento(...)` ou serviço dedicado) — orquestra checagem de papel/grant, resolução de categoria, gravação no storage e persistência atômica (rollback se o storage falhar).
- Controller: novo método em `PastaController` (`POST /api/v1/pastas/{id}/documentos`); validação de `arquivo`/`titulo` → `400`.
- Testes (unit + aceitação): sucesso multi-nível (Área/Federação); categoria derivada por `TIP_MIME` (pdf→`Documentos`, png→`Imagens`, mp4→`Vídeos`, zip→`Outros`); `COD_COLABORADOR` = autenticado; `403` sem papel `ADMINISTRADOR`; `403` sem grant `EDICAO`; `403` com grant só `LEITURA`; `404` pasta inexistente; `400` request inválido.

### Critérios de Conclusão

- RF-DOC-UPLOAD-001/002/003 implementados.
- AT-DOC-UPLOAD-001/002/003 atendidos.
- Testes aprovados.
- Rastreabilidade íntegra.

---

## TK-DOC-UPLOAD-003 — Implementar upload na página de Arquivos (frontend)

### Objetivo

Botão de upload na página `AreaColaboradorArquivosPage.vue` (já existente, `FT-DOCUMENTO`), visível **apenas** quando a atribuição ativa do colaborador é `ADMINISTRADOR` — mesma fonte de verdade de papel já usada pela sessão (`useSession`/`activeAssignment`).

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-001

### Casos de Uso Relacionados

- UC-DOC-UPLOAD-001

### Dependências

- TK-DOC-UPLOAD-002 (endpoint deve existir antes do consumo).

### Componentes Esperados

- Extensão do service client `services/documento/` (novo método `upload` — `multipart/form-data` com `arquivo` + `titulo`; sem campo de categoria).
- Extensão do composable `useAreaColaboradorArquivos.ts` (ação de upload, estado de progresso/erro).
- Controle de visibilidade do botão por papel da atribuição ativa (esconder para não-`ADMINISTRADOR`, não apenas desabilitar — evita vazar a existência do recurso a quem não pode usá-lo).
- Tratamento de erro (`400` — validação; `403` — mensagem, não crash; `404` — mensagem; `413` — arquivo grande demais).
- Testes (unit): botão visível/oculto por papel; upload bem-sucedido; erros tratados sem crash.

### Critérios de Conclusão

- RF-DOC-UPLOAD-001 consumido na UI, restrito a `ADMINISTRADOR`.
- Testes aprovados.
- Rastreabilidade íntegra.

---

# Matriz de Rastreabilidade

| Task | RF | UC | AT |
|------|----|----|----|
| TK-DOC-UPLOAD-001 | RF-DOC-UPLOAD-001 | — | — |
| TK-DOC-UPLOAD-002 | RF-DOC-UPLOAD-001, RF-DOC-UPLOAD-002, RF-DOC-UPLOAD-003 | UC-DOC-UPLOAD-001, UC-DOC-UPLOAD-002, UC-DOC-UPLOAD-003 | AT-DOC-UPLOAD-001, AT-DOC-UPLOAD-002, AT-DOC-UPLOAD-003 |
| TK-DOC-UPLOAD-003 | RF-DOC-UPLOAD-001 | UC-DOC-UPLOAD-001 | AT-DOC-UPLOAD-001 |

---

# Critérios de Conformidade

Este documento é considerado conforme quando:

- todas as tarefas estiverem associadas a pelo menos um requisito funcional;
- não representar planejamento da construção;
- manter consistência com `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `traceability.md`.

---

# Responsabilidades

## specs/

Define **o que** deverá ser implementado.

## construction/

Define **como**, **quando**, **por quem** e **em qual ordem** — fora do escopo deste documento.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 3 tasks (1 migration/DBA, 1 backend, 1 frontend) |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review: TK-DOC-UPLOAD-001 passa a cobrir `SQ_CAT_DOC_COD_CAT_DOC` + DML das 4 categorias de mídia; TK-DOC-UPLOAD-002 detalha resolução de categoria por `TIP_MIME`, `COD_COLABORADOR` da sessão, teto `413` e `400` |
| 1.2 | 2026-08-27 | Claude Code | TK-DOC-UPLOAD-001: script `V009` (SQL simples, 2 sequences + grants + 4 `INSERT` de categoria) + `VAL-DB-03` propostos; README explica a pasta (não é Flyway); `SQ_CAT_DOC_COD_CAT_DOC` sai do escopo (app não insere categoria) |
