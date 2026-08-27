# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Atualizar / Alterar Status / Mover sobre `PASTA` e `DOCUMENTO` já existentes; cria `PASTA` e `DOCUMENTO_VERSAO`) |
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
| Tipo | Incremento de escrita sobre `FT-DOCUMENTO` (leitura, `DONE`) e `FT-DOCUMENTO-UPLOAD` (upload, `IMPLEMENTING`) |

---

# Objetivo

Permitir que colaboradores com papel `ADMINISTRADOR` — na atribuição escopada a
Federação, Singular, Área ou Equipe (`PAPEL_ATRIBUICAO`) — **mantenham** o acervo
documental das pastas para as quais essa atribuição tem grant de edição
(`PERMISSAO_PASTA.TIP_ACESSO='EDICAO'`):

- **organizar a hierarquia de pastas** — criar subpasta, renomear, mover, arquivar;
- **manter os documentos já enviados** — enviar nova versão, editar metadados,
  arquivar, mover entre pastas.

Sem gerenciar quem tem acesso (`PERMISSAO_PASTA`) pela aplicação — isso permanece
dado institucional.

**Origem:** `specs/features/arquivos/specification.md` § Fora do Escopo previa
*"Upload, edição ou exclusão de arquivos pelo colaborador — decisão de produto
(2026-08-26) [...]. Fica para Feature futura se priorizado."* `FT-DOCUMENTO-UPLOAD`
cobriu o upload; esta Feature cobre a manutenção de pastas e documentos, conforme
decisões do usuário de 2026-08-27 (ver `decisions.md`).

**Por que uma Feature própria, não uma fase de `FT-DOCUMENTO-UPLOAD`:** parecer de
Review de Spec (2026-08-27) — `FT-DOCUMENTO-UPLOAD` já está `IMPLEMENTING` e a
máquina de estado (`specs/foundation/feature-yaml.md`) não tem transição
`IMPLEMENTING → READY_FOR_REVIEW`. Uma Feature própria tem ciclo limpo
`DRAFT → READY_FOR_REVIEW → APPROVED → IMPLEMENTING → DONE`.

---

# Escopo

## Incluído — decidido (2026-08-27)

Todas as operações abaixo exigem o **mesmo modelo de autorização**: atribuição
ativa `ADMINISTRADOR` + grant `EDICAO` compatível na pasta alvo (para operações de
documento, a pasta do documento). Ver § Modelo de Autorização e `decisions.md` D-02.

**Pastas:**

- **Criar subpasta** de uma pasta existente (`COD_PASTA_PAI` = pasta alvo). Na
  criação, o backend **copia** para a nova pasta as linhas de `PERMISSAO_PASTA` da
  pasta-pai (snapshot — `decisions.md` D-04). `FLG_ATIVO='S'`.
- **Renomear** pasta (`NOM_PASTA`, opcionalmente `DSC_PASTA`).
- **Mover** pasta (alterar `COD_PASTA_PAI`) para outra pasta destino — exige grant
  `EDICAO` na pasta movida **e** na pasta destino. Proibido mover para si mesma ou
  para descendente (ciclo) → `409`. Não altera `PERMISSAO_PASTA` (`decisions.md` D-08).
- **Arquivar** pasta (soft-delete, `FLG_ATIVO='N'`) — só se a pasta não tiver
  subpasta ativa nem documento `ATIVO`/`ARQUIVADO`; caso contrário `409`
  (`decisions.md` D-05).

**Documentos já enviados:**

- **Nova versão** — novo binário para um `DOCUMENTO` `ATIVO`: cria `ARQUIVO_BINARIO`
  e `DOCUMENTO_VERSAO` (`NUM_VERSAO` = atual+1, `FLG_VERSAO_ATUAL='S'`,
  `COD_COLABORADOR` = autenticado, `DSC_ALTERACAO` = nota opcional); a versão
  anterior vira `FLG_VERSAO_ATUAL='N'`. `COD_CATEGORIA_DOCUMENTAL` **re-derivado** do
  novo `TIP_MIME` (`decisions.md` D-07).
- **Editar metadados** — `TIT_DOCUMENTO` e/ou `DSC_DOCUMENTO`. Não toca binário,
  versão nem categoria.
- **Arquivar** documento — `STA_DOCUMENTO='ATIVO' → 'ARQUIVADO'`. "Excluir" resolve
  para arquivar (`decisions.md` D-06); documento arquivado continua visível na
  leitura conforme `RF-DOCUMENTO-004` de `FT-DOCUMENTO`.
- **Mover** documento entre pastas (alterar `COD_PASTA`) — exige grant `EDICAO` na
  pasta de origem **e** na pasta destino (ativa).

## Fora do Escopo — decidido (2026-08-27)

- **Gerenciar `PERMISSAO_PASTA`** (criar/revogar grants) pela aplicação — sem tela,
  sem endpoint (`decisions.md` D-03). `TIP_ACESSO='ADMINISTRACAO'` permanece reservado.
- **Exclusão física ou lógica real de documento** — nesta Feature só arquivamento
  (`decisions.md` D-06). Exclusão de verdade (sumir da leitura) = Feature futura +
  migration.
- **Desarquivar** documento (`ARQUIVADO` → `ATIVO`) e **reativar** pasta
  (`FLG_ATIVO='N' → 'S'`) — Feature futura.
- **Exclusão de pasta em cascata** — só soft-delete de pasta vazia (`decisions.md` D-05).
- **Herança viva de permissão** entre pastas (`FLG_HERDA_PERMISSAO`, `OQ-012`,
  `BR-017`) — usa cópia snapshot (`decisions.md` D-04), não herança.
- **Papel `GESTOR_DOCUMENTAL`** — não estendido (`decisions.md` D-02).
- **Escolha de categoria** na nova versão — sempre derivada do `TIP_MIME`.
- **Quota de armazenamento por colaborador** (`BR-023`) — não validada. Risco aceito.
- **Upload de documento novo** — coberto por `FT-DOCUMENTO-UPLOAD` (não redefinido aqui).
- **Migração de arquivos do CMS/legado** — fora do escopo da reconstrução
  (`DS-RECONSTRUCTION-SCOPE-01` §3).

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador com atribuição ativa `ADMINISTRADOR` (Federação, Singular, Área ou Equipe) | Único ator autorizado — restrito às pastas com grant `EDICAO` compatível com o nível dessa atribuição. Pode gerir a hierarquia de pastas e manter os documentos já enviados dessas pastas. |
| Colaborador sem atribuição `ADMINISTRADOR` ativa | Sem acesso a esta Feature; continua restrito à leitura de `FT-DOCUMENTO` e ao upload de `FT-DOCUMENTO-UPLOAD` (que também exige `ADMINISTRADOR`). |

---

# Modelo de Autorização

Reaproveita integralmente o mecanismo multi-nível de `FT-DOCUMENTO`
(`PERMISSAO_PASTA`) mais a checagem de papel de `FT-DOCUMENTO-UPLOAD`. **Vale para
toda operação desta Feature:**

```text
operação de escrita autorizada quando:
  atribuição ativa do colaborador (Contexto Ativo, JwtAuthenticatedPrincipal.papelAtribuicaoId)
    → PAPEL_ATRIBUICAO.COD_PAPEL → PAPEL.NOM_PAPEL = 'ADMINISTRADOR'
  E existe PERMISSAO_PASTA da PASTA ALVO com
    TIP_DESTINATARIO/COD_DESTINATARIO correspondente ao nível populado nessa
    PAPEL_ATRIBUICAO (COD_FEDERACAO → FEDERACAO, COD_SINGULAR → SINGULAR,
    COD_AREA → AREA, COD_EQUIPE → EQUIPE)
    E TIP_ACESSO = 'EDICAO'
```

**Pasta alvo por operação:**

| Operação | Pasta(s) sobre a(s) qual(is) o grant `EDICAO` é exigido |
|----------|--------------------------------------------------------|
| Nova versão / editar metadados / arquivar documento | a pasta do documento (`DOCUMENTO.COD_PASTA`) |
| Criar subpasta | a pasta-pai (`COD_PASTA_PAI`) |
| Renomear / arquivar pasta | a própria pasta |
| Mover pasta | a pasta movida **e** a pasta destino |
| Mover documento | a pasta de origem **e** a pasta destino |

**Decisão técnica:** toda a Feature mapeia para `TIP_ACESSO='EDICAO'`.
`ADMINISTRACAO` fica reservado para uma futura Feature de gestão de grants
(`decisions.md` D-03).

**Sem grant `EDICAO` compatível → `403` explícito**, mesmo padrão de
`RF-DOCUMENTO-003` (nunca `404` disfarçado). Em operações que exigem grant em duas
pastas, basta faltar em uma.

**Nota:** a atribuição `ADMINISTRADOR` precisa estar **ativa** (Contexto Ativo
atual), não apenas existir entre as elegíveis — mesmo padrão de `FT-SESSION` /
`FT-AREA-COLABORADOR`.

---

# Regras transversais de estado do recurso

Aplicam-se a todos os RFs (fecham NC-1..NC-3 do Review de Spec — `decisions.md`
DC-1..DC-3):

| Situação | Resultado |
|----------|-----------|
| Pasta ou documento alvo **nunca existiu** | `404` |
| Pasta com `FLG_ATIVO='N'` alvo de qualquer escrita que **não** seja re-arquivar (criar subpasta nela, renomear/mover ela, mover documento para/de ela) | `404` (pasta arquivada = inexistente para escrita) |
| Documento não-`ATIVO` (`ARQUIVADO`/`EXPIRADO`) alvo de nova versão, edição de metadados ou mover | `409` (o documento ainda existe e é visível na leitura; a operação conflita com o ciclo de vida) |
| `DELETE` (arquivar) de pasta já `FLG_ATIVO='N'` | `409` |
| `DELETE` (arquivar) de documento já `ARQUIVADO`/`EXPIRADO` | `409` |

---

# Requisitos Funcionais

## Gestão de pastas

### RF-DOC-GESTAO-001 — Criar subpasta com cópia de grants

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-001 |
| Descrição | O sistema deve permitir que um colaborador `ADMINISTRADOR` com grant `EDICAO` numa pasta P (ativa) crie uma nova `PASTA` filha (`COD_PASTA_PAI = P`, `FLG_ATIVO='S'`, `NOM_PASTA` obrigatório não em branco, `DSC_PASTA` opcional). Na mesma transação, o sistema **copia** para a nova pasta todas as linhas de `PERMISSAO_PASTA` de P (`TIP_DESTINATARIO`, `COD_DESTINATARIO`, `TIP_ACESSO` — cópia idêntica), de modo que a subpasta nasça com a mesma audiência da pai. Pasta-pai inexistente ou `FLG_ATIVO='N'` → `404`. |
| Regra de Negócio | `BR-016`, `BR-017` |
| Decisão | `decisions.md` D-04 |

### RF-DOC-GESTAO-002 — Renomear pasta

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-002 |
| Descrição | O sistema deve permitir alterar `NOM_PASTA` (obrigatório quando enviado — não em branco) e/ou `DSC_PASTA` de uma pasta ativa cuja atribuição ativa tem grant `EDICAO`. Não altera hierarquia nem permissões. Requisição sem nenhum campo → `400`. |
| Regra de Negócio | — (operação estrutural; `decisions.md` DC-4) |

### RF-DOC-GESTAO-003 — Mover pasta

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-003 |
| Descrição | O sistema deve permitir alterar `COD_PASTA_PAI` de uma pasta ativa para outra pasta destino ativa, exigindo grant `EDICAO` (no nível da atribuição ativa) **tanto na pasta movida quanto na pasta destino**. O sistema deve rejeitar (`409`) mover a pasta para ela mesma ou para qualquer descendente seu (prevenção de ciclo). As linhas de `PERMISSAO_PASTA` da pasta movida **não** são alteradas. |
| Regra de Negócio | `BR-016`, `BR-017` |
| Decisão | `decisions.md` D-08 |

### RF-DOC-GESTAO-004 — Arquivar pasta (soft-delete)

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-004 |
| Descrição | O sistema deve permitir marcar `FLG_ATIVO='N'` numa pasta ativa cuja atribuição ativa tem grant `EDICAO`, **somente se** a pasta não tiver nenhuma subpasta com `FLG_ATIVO='S'` nem nenhum `DOCUMENTO` com `STA_DOCUMENTO IN ('ATIVO','ARQUIVADO')`. Caso contrário → `409`. Pasta já `FLG_ATIVO='N'` → `409`. Pasta com `FLG_ATIVO='N'` deixa de aparecer na listagem de `FT-DOCUMENTO` e não aceita novas operações de escrita. |
| Regra de Negócio | — (operação de ciclo de vida; `decisions.md` DC-4) |
| Decisão | `decisions.md` D-05 |

## Gestão de documentos já enviados

### RF-DOC-GESTAO-005 — Enviar nova versão de documento

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-005 |
| Descrição | O sistema deve permitir enviar um novo binário para um `DOCUMENTO` com `STA_DOCUMENTO='ATIVO'` cuja pasta (ativa) tem grant `EDICAO` compatível. Cria um novo `ARQUIVO_BINARIO` e um novo `DOCUMENTO_VERSAO` (`NUM_VERSAO` = maior `NUM_VERSAO` atual + 1, `FLG_VERSAO_ATUAL='S'`, `COD_COLABORADOR` = colaborador autenticado, `DSC_ALTERACAO` = nota opcional do request); a versão anteriormente atual passa a `FLG_VERSAO_ATUAL='N'`. `DOCUMENTO.COD_CATEGORIA_DOCUMENTAL` é re-derivado do `TIP_MIME` do novo binário. Operação atômica; binário gravado por último (rollback se o storage falhar). Documento não-`ATIVO` → `409`. Arquivo ausente/vazio → `400`; acima do teto (`25MB`, herdado de `FT-DOCUMENTO-UPLOAD`) → `413`. |
| Regra de Negócio | — (nova versão não altera vínculo de escopo; `decisions.md` DC-4) |
| Decisão | `decisions.md` D-07 |

### RF-DOC-GESTAO-006 — Editar metadados do documento

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-006 |
| Descrição | O sistema deve permitir alterar `TIT_DOCUMENTO` (não em branco quando enviado) e/ou `DSC_DOCUMENTO` de um `DOCUMENTO` `ATIVO` cuja pasta (ativa) tem grant `EDICAO`. Não altera binário, versões nem categoria. Requisição sem nenhum campo → `400`. Documento não-`ATIVO` → `409`. |
| Regra de Negócio | — (operação estrutural; `decisions.md` DC-4) |
| Decisão | `decisions.md` DC-1 |

### RF-DOC-GESTAO-007 — Arquivar documento

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-007 |
| Descrição | O sistema deve permitir alterar `STA_DOCUMENTO` de `ATIVO` para `ARQUIVADO` num documento cuja pasta (ativa) tem grant `EDICAO`. Nesta Feature não há exclusão física nem lógica — "excluir documento" resolve para arquivar. Documento já `ARQUIVADO` ou `EXPIRADO` → `409`. O documento `ARQUIVADO` permanece visível na leitura conforme `RF-DOCUMENTO-004` de `FT-DOCUMENTO`. |
| Regra de Negócio | `BR-018` (classificação de exposição do recurso) |
| Decisão | `decisions.md` D-06, DC-3 |

### RF-DOC-GESTAO-008 — Mover documento entre pastas

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-008 |
| Descrição | O sistema deve permitir alterar `DOCUMENTO.COD_PASTA` de um documento `ATIVO` para outra pasta destino com `FLG_ATIVO='S'`, exigindo grant `EDICAO` (no nível da atribuição ativa) **tanto na pasta de origem quanto na pasta destino**. Pasta destino inexistente ou `FLG_ATIVO='N'` → `404`; sem grant compatível em qualquer das duas → `403`; documento não-`ATIVO` → `409`. |
| Regra de Negócio | `BR-015`, `BR-016` |
| Decisão | `decisions.md` D-08, DC-1 |

## Transversais

### RF-DOC-GESTAO-009 — Restringir escrita por papel e grant de edição

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-009 |
| Descrição | O sistema deve negar (`403`) qualquer operação desta Feature quando a atribuição ativa do colaborador não tem papel `ADMINISTRADOR`, ou quando não existe `PERMISSAO_PASTA` com `TIP_ACESSO='EDICAO'` para o nível dessa atribuição na(s) pasta(s) alvo da operação (ver § Modelo de Autorização — tabela de pasta alvo). |
| Regra de Negócio | `BR-012` |

### RF-DOC-GESTAO-010 — Recurso inexistente ou inativo

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-GESTAO-010 |
| Descrição | O sistema deve retornar `404` quando o identificador de pasta ou de documento alvo (incluindo pasta-pai ou pasta destino em operações de criação/mover) não corresponde a nenhum registro, **ou** corresponde a uma pasta `FLG_ATIVO='N'` (tratada como inexistente para escrita, exceto o próprio `DELETE` de re-arquivamento, que retorna `409`). Ver § Regras transversais de estado do recurso. |
| Regra de Negócio | — |
| Decisão | `decisions.md` DC-2 |

---

# Modelo de Dados

**Físico, já instalado** — `database/ddl/003-create-tables.sql` (baseline
DBA-administrado, `DEC-DB-019`). Mesmas tabelas de `specs/features/arquivos/`
(`PASTA`, `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`, `PERMISSAO_PASTA`,
`CATEGORIA_DOCUMENTAL`) + `PAPEL`/`PAPEL_ATRIBUICAO`. Esta Feature **não cria
tabelas novas**.

```text
PASTA
├── INSERT (RF-001: criar subpasta) — COD_PASTA_PAI = pasta alvo, NOM_PASTA/DSC_PASTA
│                                     do request, FLG_ATIVO='S'
├── UPDATE NOM_PASTA/DSC_PASTA (RF-002: renomear)
├── UPDATE COD_PASTA_PAI (RF-003: mover — com checagem anti-ciclo)
└── UPDATE FLG_ATIVO='N' (RF-004: arquivar — com guarda de pasta não-vazia)

PERMISSAO_PASTA
└── INSERT (RF-001: cópia snapshot dos grants da pasta-pai para a subpasta nova)
    — nunca UPDATE/DELETE por esta Feature (decisions.md D-03)

DOCUMENTO
├── UPDATE TIT_DOCUMENTO/DSC_DOCUMENTO (RF-006: editar metadados)
├── UPDATE COD_CATEGORIA_DOCUMENTAL (RF-005: re-derivada na nova versão)
├── UPDATE COD_PASTA (RF-008: mover documento)
└── UPDATE STA_DOCUMENTO='ARQUIVADO' (RF-007: arquivar)

DOCUMENTO_VERSAO
├── INSERT (RF-005: nova versão — NUM_VERSAO = max+1, FLG_VERSAO_ATUAL='S',
│           DSC_ALTERACAO opcional, COD_COLABORADOR da sessão)
└── UPDATE FLG_VERSAO_ATUAL='N' na versão anteriormente atual (RF-005)

ARQUIVO_BINARIO
└── INSERT (RF-005: novo binário da nova versão)
```

`CK_DOCUMENTO_VERSAO_ATUAL` (`database/ddl/004-create-constraints.sql`) exige no
máximo uma versão `FLG_VERSAO_ATUAL='S'` por documento — a nova versão deve
rebaixar a anterior na mesma transação.

## Categorização por tipo de mídia (RF-005)

`CATEGORIA_DOCUMENTAL` classifica o documento pelo **tipo de mídia do arquivo
atual**. O backend resolve `COD_CATEGORIA_DOCUMENTAL` a partir de
`ARQUIVO_BINARIO.TIP_MIME`, buscando por `NOM_CATEGORIA` (`FLG_ATIVO='S'`) — mesma
tabela e mesma lógica de `FT-DOCUMENTO-UPLOAD`:

| `NOM_CATEGORIA` | Regra sobre `TIP_MIME` |
|---|---|
| `Documentos` | `application/pdf`, `application/msword`, `application/vnd.openxmlformats-officedocument.*`, `application/vnd.ms-excel`, `text/plain`, `text/csv`, `application/vnd.oasis.opendocument.*` |
| `Imagens` | `image/*` |
| `Vídeos` | `video/*` |
| `Outros` | qualquer outro `TIP_MIME` |

- As 4 categorias são dado institucional (`V009`, já executado) — a aplicação nunca
  cria categoria.
- Categoria resolvida inexistente no banco → erro de configuração explícito
  (fail-fast), nunca `DOCUMENTO` sem categoria.
- O backend **sempre** resolve por `NOM_CATEGORIA`, nunca por ID fixo.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| `FT-DOCUMENTO` (`DONE`) | Pré-requisito | Reaproveita entidades, a query de leitura (`GET /api/v1/pastas`, `GET /api/v1/documentos/{id}/download` — já filtram `PASTA.FLG_ATIVO='N'` e `STA_DOCUMENTO='EXPIRADO'`), e o `PermissaoPastaDomainService` |
| `FT-DOCUMENTO-UPLOAD` (`IMPLEMENTING`) | Pré-requisito | Reaproveita `PermissaoPastaDomainService.ensureUploadGrant` (generalizar → `ensureEdicaoGrant`), `ObjectStorageClient.upload` + `S3ObjectStorageClient`, `MediaCategoryResolver`, teto `25MB`/`413`, e o `@GeneratedValue` já aplicado a `Documento/DocumentoVersao/ArquivoBinario` |
| Sequences `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_VERSAO` | ✅ resolvida | `V009` executado e validado (2026-08-27) — `RF-005` reusa `SQ_DOCUMENTO_VERSAO` |
| Sequences `SQ_PASTA`, `SQ_PERMISSAO_PASTA` | **Bloqueante (execução)** | **Não existem** — o baseline homologado tem 12 sequences (`database/ddl/002-create-sequences.sql`) e nenhuma é de `PASTA`/`PERMISSAO_PASTA`; as tabelas não têm coluna `IDENTITY`. `RF-DOC-GESTAO-001` faz `INSERT` em ambas. Requer `V010` análogo ao `V009` (2× `CREATE SEQUENCE START WITH <MAX(id)+1>` + `GRANT SELECT` p/ `UNMPORTCOM_APP_ROLE`, DEC-DB-024). Confirmar via JDBC no DoR-Implementation. Ver `tasks.md` TK-DOC-GESTAO-001. |
| Grants `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) nas pastas de homologação | Bloqueante (execução) | Dado institucional (`database/dml/`), mesma pendência de `FT-DOCUMENTO-UPLOAD` |
| Provisionamento do Object Storage (DEC-013) | Bloqueante (execução) | Mesma pendência já registrada em `FT-DOCUMENTO` |

**Nenhuma outra migration é necessária:** renomear/mover são `UPDATE`; arquivamento
de pasta usa `FLG_ATIVO` (coluna existente); arquivamento de documento usa
`STA_DOCUMENTO='ARQUIVADO'` (valor já no CHECK); nova versão usa `SQ_DOCUMENTO_VERSAO`
(criada no `V009`).

---

# Decisão de produto/arquitetura pendente

Nenhuma pendência de **spec** conhecida — as 8 decisões (D-01..D-08) e os 4
refinamentos de consistência (DC-1..DC-4) estão em `decisions.md`. Situação das
pendências de **execução** (para o DoR-Implementation):

1. **`SQ_PASTA` / `SQ_PERMISSAO_PASTA` ausentes.** Bloqueia `RF-DOC-GESTAO-001`.
   Propor `V010`, execução manual pelo DBA (`DEC-DB-019`). Confirmar ausência via
   JDBC antes.
2. **Grants `EDICAO` institucionais.** Mesma pendência de `FT-DOCUMENTO-UPLOAD`.
3. **Exclusão real de documento (dívida registrada, não bloqueia).** Se o produto
   quiser que "excluir" some da leitura, será preciso um novo valor de
   `STA_DOCUMENTO` (ou `FLG` em `DOCUMENTO`) + ajuste da query de `FT-DOCUMENTO` —
   Feature futura (`decisions.md` D-06).
4. **Registro de governança.** D-04 (snapshot de grants) e D-06 (excluir = arquivar)
   a referenciar em `docs/domain/10-open-questions.md` (OQ-011, OQ-012) e, se
   adotado, `docs/technology/04-decision-log.md`. Ação no monorepo, não bloqueia.

---

# Fontes

`specs/features/arquivos/` (`FT-DOCUMENTO`, `DONE` — schema físico e modelo de
leitura); `specs/features/documento-upload/` (`FT-DOCUMENTO-UPLOAD` — modelo de
autorização, `ensureUploadGrant`, `MediaCategoryResolver`, `ObjectStorageClient`);
`specs/features/documento-gestao/decisions.md`; `database/ddl/002-create-sequences.sql`
(12 sequences do baseline — sem `SQ_PASTA`/`SQ_PERMISSAO_PASTA`),
`003-create-tables.sql`, `004-create-constraints.sql`;
`database/migrations/V009__documento_upload_sequences_e_categorias.sql`;
`docs/domain/09-business-rules.md` (BR-012, BR-015, BR-016, BR-017, BR-018, BR-023);
`docs/domain/10-open-questions.md` (OQ-006, OQ-011, OQ-012 — abertas);
`docs/technology/04-decision-log.md` (DEC-013, DEC-CMS-002, DEC-DB-019, DEC-DB-024);
parecer de Review de Spec de `FT-DOCUMENTO-UPLOAD` Fase 2 (2026-08-27, NC-1..NC-4).

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — extraída da proposta "Fase 2" de `FT-DOCUMENTO-UPLOAD` (opção 1a do Review de Spec). RF-DOC-GESTAO-001..010; regras transversais de estado do recurso fechando NC-1..NC-3; RN revisadas (NC-4); dependência `SQ_PASTA`/`SQ_PERMISSAO_PASTA` (`V010`) registrada |
