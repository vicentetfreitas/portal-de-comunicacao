# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Atualizar / Alterar Status / Mover; estende `FT-DOCUMENTO` e `FT-DOCUMENTO-UPLOAD`) |
| Versão | 1.1 |
| Status | APPROVED |
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

Decomposição funcional de `FT-DOCUMENTO-GESTAO` em unidades de implementação. Não
representa cronograma. Estende `FT-DOCUMENTO` (leitura, `DONE`) e
`FT-DOCUMENTO-UPLOAD` (upload, `IMPLEMENTING`) — reaproveita entidades,
`PermissaoPastaDomainService`/`ensureUploadGrant`, `ObjectStorageClient.upload`,
`MediaCategoryResolver`, `PastaController`/`DocumentoController`.

Feature em **`APPROVED`** — Review de Spec APPROVED WITH MINOR ISSUES (2026-08-27,
NC-A corrigida em `traceability.md` v1.1). DoR-Implementation avaliado com
verificação JDBC das pré-condições de banco (ver TK-DOC-GESTAO-001).

---

## TK-DOC-GESTAO-001 — Script de banco: sequences de PASTA e PERMISSAO_PASTA

### Objetivo

Produzir `V010` (SQL simples, execução manual na IDE do banco — `DEC-DB-019`, sem
Flyway) criando as sequences que faltam para a **escrita de pastas**:

- `SQ_PASTA` (`PASTA.COD_PASTA`) — `RF-DOC-GESTAO-001` insere via JPA
  `GenerationType.SEQUENCE`.
- `SQ_PERMISSAO_PASTA` (`PERMISSAO_PASTA.COD_PERMISSAO_PASTA`) — a cópia snapshot de
  grants (`decisions.md` D-04) insere N linhas.

**Verificado via JDBC no Oracle TST (`UNMPORTCOM_APP`, 2026-08-27):**

- `SQ_PASTA` e `SQ_PERMISSAO_PASTA` **não existem** (as sequences visíveis de
  documento são só `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_COD_DOCUMENTO`,
  `SQ_DOCUMENTO_VERSAO`).
- Tabelas `UNMPORTCOM.PASTA` e `UNMPORTCOM.PERMISSAO_PASTA` **existem** e o
  `UNMPORTCOM_APP_ROLE` já tem `SELECT/INSERT/UPDATE/DELETE` nas duas — **só falta a
  sequence + o `GRANT SELECT` da sequence**.
- Ambas as tabelas estão com **0 linhas** → `V010` usa `START WITH 1` (não há id
  existente a evitar).
- Colunas sem `IDENTITY` (DDL: `NUMBER(19) NOT NULL`) → PK via sequence.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-001 (bloqueante — sem sequence não há `INSERT` de pasta nem de grant)

### Dependências

- Nenhuma. Pré-check JDBC já executado (acima).

### Componentes Esperados

- **[feito — 2026-08-27]** `database/migrations/V010__pasta_permissao_pasta_sequences.sql`
  (análogo ao `V009`): 2× `CREATE SEQUENCE ... START WITH 1 INCREMENT BY 1 CACHE 20
  NOCYCLE` + 2× `GRANT SELECT ... TO UNMPORTCOM_APP_ROLE` (DEC-DB-024) + seção de
  conferência. Sem alteração de tabela.
- **[feito — 2026-08-27]** `database/migrations/VAL-DB-04-verify-pasta-write-prereqs.sql`:
  conferência read-only pré-`V010`.
- **[feito — 2026-08-27]** `database/migrations/README.md`: linha `V010` + reconciliação
  greenfield (12 → 16 sequences).

### Critérios de Conclusão

- **[pendente]** `V010` executado pelo usuário na IDE do banco.
- **[pendente]** Validado via consulta read-only (JDBC): `SQ_PASTA` e
  `SQ_PERMISSAO_PASTA` criadas, ambas com `GRANT SELECT` para `UNMPORTCOM_APP_ROLE`.

---

## TK-DOC-GESTAO-002 — Gestão de pastas (backend)

### Objetivo

Endpoints de escrita de `PASTA`, autorizados por papel `ADMINISTRADOR` + grant
`EDICAO` (`specification.md` § Modelo de Autorização):

- `POST /api/v1/pastas/{id}/subpastas` — criar subpasta + **copiar** as linhas de
  `PERMISSAO_PASTA` da pasta-pai (mesma transação).
- `PATCH /api/v1/pastas/{id}` — renomear (`nome`/`descricao`) e/ou mover
  (`codPastaPai`), com prevenção de ciclo (destino ≠ pasta nem descendente → `409`)
  e grant `EDICAO` também na pasta destino.
- `DELETE /api/v1/pastas/{id}` — arquivar (`FLG_ATIVO='N'`); `409` se a pasta tiver
  subpasta ativa ou documento `ATIVO`/`ARQUIVADO`, ou se já estiver arquivada.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-001, -002, -003, -004, -009, -010

### Casos de Uso Relacionados

- UC-DOC-GESTAO-001..004, -009, -010

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-001..004, -009, -010

### Dependências

- **TK-DOC-GESTAO-001** (`SQ_PASTA`, `SQ_PERMISSAO_PASTA` no ambiente).
- `@GeneratedValue(SEQUENCE)` em `PastaEntity` e `PermissaoPastaEntity` (hoje só
  lidas por `FT-DOCUMENTO`).
- Generalizar `PermissaoPastaDomainService.ensureUploadGrant` → método genérico
  `ensureEdicaoGrant(pasta, atribuiçãoAtiva)` reutilizável por todas as operações;
  não duplicar a checagem multi-nível existente.
- Consulta de subárvore (prevenção de ciclo em RF-003; guarda de pasta não-vazia em
  RF-004).
- Grants `PERMISSAO_PASTA` (`EDICAO`) provisionados nas pastas de teste/homologação.

### Componentes Esperados

- Application Service: `criarSubpasta`, `atualizarPasta` (renomear/mover),
  `arquivarPasta`. A cópia snapshot de grants roda na mesma transação do `INSERT` da
  pasta.
- Controller: novos métodos em `PastaController` (`POST .../subpastas`, `PATCH`,
  `DELETE`).
- Validação de request (`400`): `nome` em branco; payload `PATCH` vazio.
- `GlobalExceptionHandler`: conflito de ciclo, de pasta não-vazia e de pasta já
  arquivada → `409`; pasta inativa alvo de escrita → `404`.
- Testes (unit + aceitação): AT-DOC-GESTAO-001..004, -009, -010.

### Critérios de Conclusão

- RF-DOC-GESTAO-001..004 (+ transversais) implementados; ATs correspondentes atendidos.
- `./mvnw clean verify` verde.
- Rastreabilidade íntegra.

---

## TK-DOC-GESTAO-003 — Gestão de documentos já enviados (backend)

### Objetivo

Endpoints de escrita sobre `DOCUMENTO` existente, autorizados por papel
`ADMINISTRADOR` + grant `EDICAO` na pasta do documento:

- `POST /api/v1/documentos/{id}/versoes` — nova versão (`multipart`): novo
  `ARQUIVO_BINARIO` + `DOCUMENTO_VERSAO` (`NUM_VERSAO`=atual+1,
  `FLG_VERSAO_ATUAL='S'`, `COD_COLABORADOR` da sessão, `DSC_ALTERACAO` opcional);
  rebaixa a versão anterior; **re-deriva** `COD_CATEGORIA_DOCUMENTAL` do novo
  `TIP_MIME`; atômico, storage por último; `409` se documento não-`ATIVO`; `413`.
- `PATCH /api/v1/documentos/{id}` — editar `titulo`/`descricao` e/ou mover
  (`codPasta`, com grant `EDICAO` também na pasta destino ativa); `409` se documento
  não-`ATIVO`.
- `DELETE /api/v1/documentos/{id}` — arquivar (`STA_DOCUMENTO='ARQUIVADO'`); `409`
  se já não-`ATIVO`.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-005, -006, -007, -008, -009, -010

### Casos de Uso Relacionados

- UC-DOC-GESTAO-005..008, -009, -010

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-005..008, -009, -010

### Dependências

- `SQ_DOCUMENTO_VERSAO`, `SQ_ARQUIVO_BINARIO` (já criadas no `V009`).
- `ensureEdicaoGrant` genérico (de TK-DOC-GESTAO-002) — consolidar num único ponto.
- `MediaCategoryResolver` (de `FT-DOCUMENTO-UPLOAD`) reutilizado para a re-derivação
  (D-07).
- `CK_DOCUMENTO_VERSAO_ATUAL` — rebaixar a versão anterior na mesma transação do
  `INSERT` da nova.
- `ObjectStorageClient.upload` (de `FT-DOCUMENTO-UPLOAD`).

### Componentes Esperados

- Application Service: `criarNovaVersao`, `atualizarDocumento` (metadados/mover),
  `arquivarDocumento`.
- Controller: novos métodos em `DocumentoController`.
- Validação (`400`): `arquivo` ausente/vazio na versão; payload `PATCH` vazio;
  `titulo` em branco.
- `GlobalExceptionHandler`: documento não-`ATIVO` alvo de escrita → `409`.
- Testes (unit + aceitação): AT-DOC-GESTAO-005..008, -009, -010.

### Critérios de Conclusão

- RF-DOC-GESTAO-005..008 (+ transversais) implementados; ATs correspondentes atendidos.
- `./mvnw clean verify` verde.
- Rastreabilidade íntegra.

---

## TK-DOC-GESTAO-004 — Ações de gestão na página de Arquivos (frontend)

### Objetivo

Expor as operações desta Feature em `AreaColaboradorArquivosPage.vue`, **visíveis
apenas** quando `activeAssignment.papel === 'ADMINISTRADOR'` (escondidas, não
desabilitadas — mesma regra de `FT-DOCUMENTO-UPLOAD`):

- Menu por pasta: **nova subpasta**, **renomear**, **mover**, **arquivar**.
- Menu por documento: **nova versão**, **editar** (título/descrição), **mover**,
  **arquivar** ("excluir").

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-001..008 (consumo na UI)

### Casos de Uso Relacionados

- UC-DOC-GESTAO-001..008

### Dependências

- TK-DOC-GESTAO-002 e TK-DOC-GESTAO-003 (endpoints devem existir).

### Componentes Esperados

- Extensão de `services/documento/` (`PastaApiService`, `DocumentoApiService`):
  `criarSubpasta`, `atualizarPasta`, `arquivarPasta`, `criarNovaVersao`,
  `atualizarDocumento`, `arquivarDocumento`.
- Extensão de `useAreaColaboradorArquivos.ts`: ações + estado de progresso/erro por
  operação; recarregar a lista em sucesso.
- Diálogos: novo/renomear pasta, mover (seletor de pasta destino), nova versão
  (seletor de arquivo + nota), editar documento, confirmações de arquivar.
- Tratamento de erro sem crash: `400`, `403`, `404`, `409` (ciclo / pasta não-vazia
  / pasta já arquivada / documento não-`ATIVO`), `413`.
- Testes (unit): visibilidade por papel; cada ação bem-sucedida; erros tratados
  (incl. `409`).

### Critérios de Conclusão

- RF-DOC-GESTAO-001..008 consumidos na UI, restritos a `ADMINISTRADOR`.
- `yarn typecheck` + `yarn test:unit` verdes.
- Rastreabilidade íntegra.

---

# Matriz de Rastreabilidade

| Task | RF | UC | AT |
|------|----|----|----|
| TK-DOC-GESTAO-001 | RF-DOC-GESTAO-001 | — | — |
| TK-DOC-GESTAO-002 | RF-DOC-GESTAO-001/002/003/004/009/010 | UC-DOC-GESTAO-001..004/009/010 | AT-DOC-GESTAO-001..004/009/010 |
| TK-DOC-GESTAO-003 | RF-DOC-GESTAO-005/006/007/008/009/010 | UC-DOC-GESTAO-005..008/009/010 | AT-DOC-GESTAO-005..008/009/010 |
| TK-DOC-GESTAO-004 | RF-DOC-GESTAO-001..008 | UC-DOC-GESTAO-001..008 | AT-DOC-GESTAO-001..008 |

---

# Critérios de Conformidade

Conforme quando: todas as tasks têm ≥1 RF (exceto TK-DOC-GESTAO-001, bloqueio de
banco ligado a RF-DOC-GESTAO-001); não representa cronograma; consistente com
`specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md`, `decisions.md`
e `traceability.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — TK-DOC-GESTAO-001 (`V010`), -002 (pastas BE), -003 (documentos BE), -004 (frontend); extraído da proposta "Fase 2" de `FT-DOCUMENTO-UPLOAD` |
| 1.1 | 2026-08-27 | Claude Code (Specify) | TK-DOC-GESTAO-001: pré-check JDBC executado no Oracle TST — `SQ_PASTA`/`SQ_PERMISSAO_PASTA` confirmadas ausentes; tabelas existem com DML já concedido e **0 linhas** → `V010` usa `START WITH 1`. Mantém `APPROVED`. |
| 1.2 | 2026-08-27 | Claude Code (Implement) | TK-DOC-GESTAO-001: `V010__pasta_permissao_pasta_sequences.sql` + `VAL-DB-04` + linha no README produzidos. Falta o usuário executar `V010` na IDE do banco e a validação JDBC pós-execução. |
