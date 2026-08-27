# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Fase 1: só criação; Fase 2: update/soft-delete/versão; backend estende `FT-DOCUMENTO`) |
| Versão | 2.0 |
| Status | Fase 1 concluída · Fase 2 DRAFT (aguarda Review de Spec + DoR-Implementation) |
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

Decomposição funcional de `FT-DOCUMENTO-UPLOAD` em unidades de implementação. Não
representa cronograma. Estende `FT-DOCUMENTO` (backend/frontend `DONE`) —
reaproveita entidades, `PermissaoPastaDomainService`, `PastaController`/
`DocumentoController`.

| Fase | Tasks | Estado |
|------|-------|--------|
| **Fase 1** — upload de um arquivo | TK-DOC-UPLOAD-001, -002, -003 | ✅ concluídas (2026-08-27) |
| **Fase 2** — gestão de pastas e documentos | TK-DOC-UPLOAD-004, -005, -006, -007 | ⏳ DRAFT — não iniciar antes de Review de Spec (`specification.md` v2.0) **e** DoR-Implementation da Fase 2 |

---

## TK-DOC-UPLOAD-001 — Script de banco Fase 1: sequences + categorias — ✅ CONCLUÍDA (2026-08-27)

`V009` executado e validado (JDBC): `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_VERSAO`
(cache 20) + `GRANT SELECT` p/ `UNMPORTCOM_APP_ROLE`; `CATEGORIA_DOCUMENTAL` com 4
linhas ativas (`Documentos`/`Imagens`/`Vídeos`/`Outros`, IDs 1–4). RF: RF-DOC-UPLOAD-001.

---

## TK-DOC-UPLOAD-002 — Upload de documento (backend) — ✅ CONCLUÍDA (2026-08-27, `portal-comunicacao-api` `57c22d9`)

`POST /api/v1/pastas/{id}/documentos`; `PastaApplicationService.uploadDocumento`;
`@GeneratedValue` nas 3 entidades; `ObjectStorageClient.upload` + `S3ObjectStorageClient`;
`PermissaoPastaDomainService.ensureUploadGrant` (papel `ADMINISTRADOR` + `EDICAO`);
`MediaCategoryResolver`; `400`/`413`. `DocumentoUploadAcceptanceIntegrationTest`.
`./mvnw clean verify` = 341 testes, 0 falhas. RF: RF-DOC-UPLOAD-001/002/003.

---

## TK-DOC-UPLOAD-003 — Upload na página de Arquivos (frontend) — ✅ CONCLUÍDA (2026-08-27, `portal-comunicacao-app` `5f887f2`)

`AreaColaboradorUploadDialog.vue` + botão "Enviar arquivo" por pasta, visível só p/
`activeAssignment.papel === 'ADMINISTRADOR'`; `PastaApiService.uploadDocumento`;
`useAreaColaboradorArquivos` (`canUpload`, `enviarDocumento`, `uploadingPastaId`).
`yarn typecheck` + `yarn test:unit` (200) verdes. RF: RF-DOC-UPLOAD-001.

---

## TK-DOC-UPLOAD-004 — Script de banco Fase 2: sequences de PASTA e PERMISSAO_PASTA

### Objetivo

Produzir `V010` (SQL simples, execução manual na IDE do banco — `DEC-DB-019`, sem
Flyway) criando as sequences que faltam para a **escrita de pastas**:

- `SQ_PASTA` (`PASTA.COD_PASTA`) — `RF-DOC-UPLOAD-004` insere via JPA
  `GenerationType.SEQUENCE`.
- `SQ_PERMISSAO_PASTA` (`PERMISSAO_PASTA.COD_PERMISSAO_PASTA`) — a cópia snapshot de
  grants (`decisions.md` D-04) insere N linhas.

Confirmado em 2026-08-27: `database/ddl/002-create-sequences.sql` tem 12 sequences
do baseline homologado e **nenhuma** é de `PASTA`/`PERMISSAO_PASTA`; as tabelas
(`003-create-tables.sql`) não têm coluna `IDENTITY`.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-004 (bloqueante — sem sequence não há `INSERT` de pasta nem de grant)

### Dependências

- Nenhuma — primeiro bloqueio da Fase 2. Pré-check: consulta read-only
  (`USER_SEQUENCES`) confirmando a ausência antes de propor o script.

### Componentes Esperados

- `database/migrations/V010__pasta_permissao_pasta_sequences.sql` (análogo ao
  `V009`): 2× `CREATE SEQUENCE ... START WITH <MAX(id)+1> INCREMENT BY 1 CACHE 20
  NOCYCLE` + 2× `GRANT SELECT ... TO UNMPORTCOM_APP_ROLE` (DEC-DB-024) + seção de
  conferência. `START WITH` calculado a partir do maior `COD_*` existente nas
  tabelas (pastas de leitura de `FT-DOCUMENTO` já podem ter linhas).
- `database/migrations/VAL-DB-04-verify-pasta-write-prereqs.sql`: conferência
  read-only pré-`V010`.
- `database/migrations/README.md`: linha `V010`.

### Critérios de Conclusão

- `V010` executado pelo usuário na IDE do banco.
- Validado via consulta read-only: `SQ_PASTA` e `SQ_PERMISSAO_PASTA` criadas, ambas
  com `GRANT SELECT` para `UNMPORTCOM_APP_ROLE`, `START WITH` > maior id existente.

---

## TK-DOC-UPLOAD-005 — Gestão de pastas (backend)

### Objetivo

Endpoints de escrita de `PASTA`, autorizados por papel `ADMINISTRADOR` + grant
`EDICAO` (`specification.md` § Modelo de Autorização):

- `POST /api/v1/pastas/{id}/subpastas` — criar subpasta + **copiar** as linhas de
  `PERMISSAO_PASTA` da pasta-pai (mesma transação).
- `PATCH /api/v1/pastas/{id}` — renomear (`nome`/`descricao`) e/ou mover
  (`codPastaPai`), com prevenção de ciclo (destino ≠ pasta nem descendente → `409`)
  e grant `EDICAO` também na pasta destino.
- `DELETE /api/v1/pastas/{id}` — arquivar (`FLG_ATIVO='N'`); `409` se a pasta tiver
  subpasta ativa ou documento `ATIVO`/`ARQUIVADO`.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-004, RF-DOC-UPLOAD-005, RF-DOC-UPLOAD-006, RF-DOC-UPLOAD-007,
  RF-DOC-UPLOAD-002, RF-DOC-UPLOAD-003

### Casos de Uso Relacionados

- UC-DOC-UPLOAD-004..007, UC-DOC-UPLOAD-002, UC-DOC-UPLOAD-003

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-004..007, AT-DOC-UPLOAD-002, AT-DOC-UPLOAD-003

### Dependências

- **TK-DOC-UPLOAD-004** (`SQ_PASTA`, `SQ_PERMISSAO_PASTA` no ambiente).
- `@GeneratedValue(SEQUENCE)` em `PastaEntity` e `PermissaoPastaEntity` (hoje só
  lidas por `FT-DOCUMENTO`).
- Generalizar `PermissaoPastaDomainService.ensureUploadGrant` → método genérico
  `ensureEdicaoGrant(pasta, atribuiçãoAtiva)` reutilizável por todas as operações;
  não duplicar a checagem multi-nível existente.
- Consulta de subárvore para prevenção de ciclo (`RF-DOC-UPLOAD-006`) e para a
  guarda de pasta não-vazia (`RF-DOC-UPLOAD-007`).
- Grants `PERMISSAO_PASTA` (`EDICAO`) provisionados nas pastas de teste/homologação
  (dado institucional).

### Componentes Esperados

- Application Service: métodos `criarSubpasta`, `atualizarPasta` (renomear/mover),
  `arquivarPasta`. A cópia snapshot de grants roda na mesma transação do `INSERT`
  da pasta.
- Controller: novos métodos em `PastaController` (`POST .../subpastas`, `PATCH`,
  `DELETE`).
- Validação de request (`400`): `nome` em branco; payload `PATCH` vazio.
- `GlobalExceptionHandler`: mapear conflito de ciclo e de pasta não-vazia → `409`.
- Testes (unit + aceitação): cópia de grants na criação (AT-004); renomear (AT-005);
  mover ok / ciclo / self (AT-006); arquivar vazio / com documento / com subpasta /
  só com `EXPIRADO` (AT-007); `403` sem papel/grant, inclusive mover com grant só na
  origem (AT-002); `404` pasta/pai/destino inexistente (AT-003).

### Critérios de Conclusão

- RF-DOC-UPLOAD-004..007 implementados; AT-DOC-UPLOAD-004..007 atendidos.
- `./mvnw clean verify` verde.
- Rastreabilidade íntegra.

---

## TK-DOC-UPLOAD-006 — Gestão de documentos já enviados (backend)

### Objetivo

Endpoints de escrita sobre `DOCUMENTO` existente, autorizados por papel
`ADMINISTRADOR` + grant `EDICAO` na pasta do documento:

- `POST /api/v1/documentos/{id}/versoes` — nova versão (`multipart`): novo
  `ARQUIVO_BINARIO` + `DOCUMENTO_VERSAO` (`NUM_VERSAO`=atual+1,
  `FLG_VERSAO_ATUAL='S'`, `COD_COLABORADOR` da sessão, `DSC_ALTERACAO` opcional);
  rebaixa a versão anterior; **re-deriva** `COD_CATEGORIA_DOCUMENTAL` do novo
  `TIP_MIME`; atômico, storage por último; `409` se documento não-`ATIVO`; `413`.
- `PATCH /api/v1/documentos/{id}` — editar `titulo`/`descricao` e/ou mover
  (`codPasta`, com grant `EDICAO` também na pasta destino ativa).
- `DELETE /api/v1/documentos/{id}` — arquivar (`STA_DOCUMENTO='ARQUIVADO'`); `409`
  se já não-`ATIVO`.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-008, RF-DOC-UPLOAD-009, RF-DOC-UPLOAD-010, RF-DOC-UPLOAD-011,
  RF-DOC-UPLOAD-002, RF-DOC-UPLOAD-003

### Casos de Uso Relacionados

- UC-DOC-UPLOAD-008..011, UC-DOC-UPLOAD-002, UC-DOC-UPLOAD-003

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-008..011, AT-DOC-UPLOAD-002, AT-DOC-UPLOAD-003

### Dependências

- `SQ_DOCUMENTO_VERSAO`, `SQ_ARQUIVO_BINARIO` (já criadas no `V009`).
- `ensureEdicaoGrant` genérico (de TK-DOC-UPLOAD-005) — pode ser feito antes ou em
  paralelo; consolidar num único ponto.
- `MediaCategoryResolver` (Fase 1) reutilizado para a re-derivação (D-07).
- `CK_DOCUMENTO_VERSAO_ATUAL` — rebaixar a versão anterior antes/na mesma transação
  do `INSERT` da nova.
- `ObjectStorageClient.upload` (Fase 1).

### Componentes Esperados

- Application Service: `criarNovaVersao`, `atualizarDocumento` (metadados/mover),
  `arquivarDocumento`.
- Controller: novos métodos em `DocumentoController`.
- Validação (`400`): `arquivo` ausente/vazio na versão; payload `PATCH` vazio;
  `titulo` em branco.
- Testes (unit + aceitação): nova versão + rebaixamento + re-derivação de categoria
  (AT-008); `409` documento arquivado; falha de storage sem persistência parcial;
  editar metadados sem tocar versão/categoria (AT-009); arquivar + segue visível na
  leitura (AT-010); mover com grant nas duas pastas / `403` sem grant no destino /
  `404` destino inativo (AT-011).

### Critérios de Conclusão

- RF-DOC-UPLOAD-008..011 implementados; AT-DOC-UPLOAD-008..011 atendidos.
- `./mvnw clean verify` verde.
- Rastreabilidade íntegra.

---

## TK-DOC-UPLOAD-007 — Ações de gestão na página de Arquivos (frontend)

### Objetivo

Expor as operações da Fase 2 em `AreaColaboradorArquivosPage.vue`, **visíveis apenas**
quando `activeAssignment.papel === 'ADMINISTRADOR'` (escondidas, não desabilitadas —
mesma regra da Fase 1):

- Menu por pasta: **nova subpasta**, **renomear**, **mover**, **arquivar**.
- Menu por documento: **nova versão**, **editar** (título/descrição), **mover**,
  **arquivar** ("excluir").

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-004..011 (consumo na UI)

### Casos de Uso Relacionados

- UC-DOC-UPLOAD-004..011

### Dependências

- TK-DOC-UPLOAD-005 e TK-DOC-UPLOAD-006 (endpoints devem existir).

### Componentes Esperados

- Extensão de `services/documento/` (`PastaApiService`, `DocumentoApiService`):
  `criarSubpasta`, `atualizarPasta`, `arquivarPasta`, `criarNovaVersao`,
  `atualizarDocumento`, `arquivarDocumento`.
- Extensão de `useAreaColaboradorArquivos.ts`: ações + estado de progresso/erro por
  operação; recarregar a lista em sucesso.
- Diálogos: novo/renomear pasta, mover (seletor de pasta destino), nova versão
  (seletor de arquivo + nota), editar documento, confirmações de arquivar.
- Tratamento de erro sem crash: `400` (validação), `403` (mensagem), `404`
  (mensagem), `409` (ciclo / pasta não-vazia / documento não-`ATIVO`), `413`.
- Testes (unit): visibilidade por papel; cada ação bem-sucedida; erros tratados
  (incl. `409`).

### Critérios de Conclusão

- RF-DOC-UPLOAD-004..011 consumidos na UI, restritos a `ADMINISTRADOR`.
- `yarn typecheck` + `yarn test:unit` verdes.
- Rastreabilidade íntegra.

---

# Matriz de Rastreabilidade

| Task | RF | UC | AT |
|------|----|----|----|
| TK-DOC-UPLOAD-001 | RF-DOC-UPLOAD-001 | — | — |
| TK-DOC-UPLOAD-002 | RF-DOC-UPLOAD-001/002/003 | UC-DOC-UPLOAD-001/002/003 | AT-DOC-UPLOAD-001/002/003 |
| TK-DOC-UPLOAD-003 | RF-DOC-UPLOAD-001 | UC-DOC-UPLOAD-001 | AT-DOC-UPLOAD-001 |
| TK-DOC-UPLOAD-004 | RF-DOC-UPLOAD-004 | — | — |
| TK-DOC-UPLOAD-005 | RF-DOC-UPLOAD-002/003/004/005/006/007 | UC-DOC-UPLOAD-002/003/004/005/006/007 | AT-DOC-UPLOAD-002/003/004/005/006/007 |
| TK-DOC-UPLOAD-006 | RF-DOC-UPLOAD-002/003/008/009/010/011 | UC-DOC-UPLOAD-002/003/008/009/010/011 | AT-DOC-UPLOAD-002/003/008/009/010/011 |
| TK-DOC-UPLOAD-007 | RF-DOC-UPLOAD-004..011 | UC-DOC-UPLOAD-004..011 | AT-DOC-UPLOAD-004..011 |

---

# Critérios de Conformidade

Conforme quando: todas as tasks têm ≥1 RF (exceto -001/-004, bloqueios de banco);
não representa cronograma; consistente com `specification.md`, `use-cases.md`,
`api.md`, `acceptance-tests.md`, `decisions.md` e `traceability.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0–1.5 | 2026-08-27 | Claude Code | Fase 1 — 3 tasks; TK-001/002/003 concluídas |
| 2.0 | 2026-08-27 | Claude Code (Specify) | Fase 2 (DRAFT): TK-004 (`V010` — sequences `SQ_PASTA`/`SQ_PERMISSAO_PASTA`), TK-005 (gestão de pastas — backend), TK-006 (gestão de documentos — backend), TK-007 (ações na página de Arquivos — frontend). Fase 1 condensada. |
