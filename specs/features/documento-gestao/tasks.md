# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Atualizar / Alterar Status / Mover; estende `FT-DOCUMENTO` e `FT-DOCUMENTO-UPLOAD`) |
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

Decomposição funcional de `FT-DOCUMENTO-GESTAO` em unidades de implementação. Não
representa cronograma. Estende `FT-DOCUMENTO` (leitura, `DONE`) e
`FT-DOCUMENTO-UPLOAD` (upload, `IMPLEMENTING`) — reaproveita entidades,
`PermissaoPastaDomainService`/`ensureUploadGrant`, `ObjectStorageClient.upload`,
`MediaCategoryResolver`, `PastaController`/`DocumentoController`.

Feature em **`DRAFT`**. Nenhuma task inicia antes de Review de Spec (`APPROVED`) e
DoR-Implementation.

---

## TK-DOC-GESTAO-001 — Script de banco: sequences de PASTA e PERMISSAO_PASTA

### Objetivo

Produzir `V010` (SQL simples, execução manual na IDE do banco — `DEC-DB-019`, sem
Flyway) criando as sequences que faltam para a **escrita de pastas**:

- `SQ_PASTA` (`PASTA.COD_PASTA`) — `RF-DOC-GESTAO-001` insere via JPA
  `GenerationType.SEQUENCE`.
- `SQ_PERMISSAO_PASTA` (`PERMISSAO_PASTA.COD_PERMISSAO_PASTA`) — a cópia snapshot de
  grants (`decisions.md` D-04) insere N linhas.

Confirmado em 2026-08-27: `database/ddl/002-create-sequences.sql` tem 12 sequences
do baseline homologado e **nenhuma** é de `PASTA`/`PERMISSAO_PASTA`; as tabelas
(`003-create-tables.sql`) não têm coluna `IDENTITY`.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-001 (bloqueante — sem sequence não há `INSERT` de pasta nem de grant)

### Dependências

- Nenhuma. Pré-check: consulta read-only (`USER_SEQUENCES`) confirmando a ausência
  antes de propor o script.

### Componentes Esperados

- `database/migrations/V010__pasta_permissao_pasta_sequences.sql` (análogo ao
  `V009`): 2× `CREATE SEQUENCE ... START WITH <MAX(COD_*)+1> INCREMENT BY 1 CACHE 20
  NOCYCLE` + 2× `GRANT SELECT ... TO UNMPORTCOM_APP_ROLE` (DEC-DB-024) + seção de
  conferência. `START WITH` calculado do maior `COD_*` existente (pastas de leitura
  de `FT-DOCUMENTO` já podem ter linhas).
- `database/migrations/VAL-DB-04-verify-pasta-write-prereqs.sql`: conferência
  read-only pré-`V010`.
- `database/migrations/README.md`: linha `V010`.

### Critérios de Conclusão

- `V010` executado pelo usuário na IDE do banco.
- Validado via consulta read-only: `SQ_PASTA` e `SQ_PERMISSAO_PASTA` criadas, ambas
  com `GRANT SELECT` para `UNMPORTCOM_APP_ROLE`, `START WITH` > maior id existente.

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
