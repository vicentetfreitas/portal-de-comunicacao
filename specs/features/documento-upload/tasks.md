# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — só criação, backend estende `FT-DOCUMENTO`) |
| Versão | 1.0 |
| Status | DRAFT |
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

Decomposição funcional de `FT-DOCUMENTO-UPLOAD` em unidades de implementação. Não representa planejamento de construção/cronograma — responsabilidade de `construction/`. **Este `tasks.md` é plano prospectivo (Feature em `DRAFT`)** — não autoriza implementação; `IMPLEMENTING` exige DoR-Implementation, ainda não avaliado.

**Natureza da Feature:** estende `FT-DOCUMENTO` (backend/frontend já existentes e `DONE`) — reaproveita entidades, `PermissaoPastaDomainService`, `PastaController`/`DocumentoController`. Bloqueada por uma dependência de execução fora do controle da aplicação (sequences Oracle — ver TK-DOC-UPLOAD-001).

---

## TK-DOC-UPLOAD-001 — Propor migration das sequences ausentes (banco)

### Objetivo

Documentar, para revisão e execução do DBA, a migration Flyway que cria `SQ_ARQUIVO_BINARIO` e `SQ_DOCUMENTO_VERSAO` — inexistentes hoje (`database/ddl/002-create-sequences.sql` só tem `SQ_DOCUMENTO_COD_DOCUMENTO`). **A aplicação não executa DDL** (`DEC-DB-019`) — esta task produz o script proposto em `database/migrations/`, não o aplica.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-001 (bloqueante — sem sequence não há `INSERT`)

### Dependências

- Nenhuma — é o primeiro bloqueio a resolver.

### Componentes Esperados

- Script `database/migrations/V009__documento_upload_sequences.sql` (nome definitivo a confirmar contra o próximo `V*` livre no momento da execução), seguindo o padrão de `V003`-`V008` já existentes.
- Atualização de `database/migrations/README.md` registrando a nova migration.

### Critérios de Conclusão

- Script revisado e aprovado pelo DBA.
- Sequences criadas no ambiente antes de `TK-DOC-UPLOAD-002`.

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

- **TK-DOC-UPLOAD-001** (sequences executadas no ambiente).
- `ObjectStorageClient` — adicionar método de escrita (`documento/application/port/`); `PermissaoPastaDomainService` — estender para checar `TIP_ACESSO='EDICAO'` e papel `ADMINISTRADOR` da atribuição ativa (não duplicar a checagem multi-nível já existente).
- Grants `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) já provisionados nas pastas de teste/homologação (dado institucional, `database/dml/`).

### Componentes Esperados

- Repository: `INSERT` em `DocumentoRepository`, `DocumentoVersaoRepository`, `ArquivoBinarioRepository` (já existem via `JpaRepository`, sem repositório novo).
- Application Service: novo método (ex. `PastaApplicationService.uploadDocumento(...)` ou serviço dedicado) — orquestra checagem de papel/grant, gravação no storage e persistência atômica (rollback se o storage falhar).
- Controller: novo método em `PastaController` (`POST /api/v1/pastas/{id}/documentos`).
- Testes (unit + aceitação): sucesso multi-nível (Área/Federação), 403 sem papel `ADMINISTRADOR`, 403 sem grant `EDICAO`, 403 com grant só `LEITURA`, 404 pasta inexistente.

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

- Extensão do service client `services/documento/` (novo método `upload`).
- Extensão do composable `useAreaColaboradorArquivos.ts` (ação de upload, estado de progresso/erro).
- Controle de visibilidade do botão por papel da atribuição ativa (esconder para não-`ADMINISTRADOR`, não apenas desabilitar — evita vazar a existência do recurso a quem não pode usá-lo).
- Tratamento de erro (403 — mensagem, não crash; 404 — mensagem).
- Testes (unit): botão visível/oculto por papel; upload bem-sucedido; erro tratado sem crash.

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
