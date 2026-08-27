# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — escrita sobre `FT-DOCUMENTO`; Fase 1 = criação, Fase 2 = update/soft-delete/versão) |
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
| Tipo | Incremento de escrita sobre `FT-DOCUMENTO` (DONE, somente leitura) |
| `feature.yaml` `status` | `IMPLEMENTING` (Fase 1 code-complete; Fase 2 em elaboração) |

---

# Fases

Esta Feature é entregue em fases. O `feature.yaml` reflete o ciclo da Feature como
um todo; cada fase tem seu próprio corpo de artefato, Review de Spec e
DoR-Implementation.

| Fase | Escopo | Estado |
|------|--------|--------|
| **Fase 1** | Upload de **um** arquivo para uma pasta existente (cria `DOCUMENTO` + `DOCUMENTO_VERSAO` inicial + `ARQUIVO_BINARIO`). | **Code-complete** — TK-DOC-UPLOAD-001/002/003 concluídas (`portal-comunicacao-api` `57c22d9`, `portal-comunicacao-app` `5f887f2`). Pendências só de homologação (grants institucionais, provisionamento do Object Storage). |
| **Fase 2** | **Gestão de pastas** (criar subpasta, renomear, mover, arquivar) e **gestão de documentos já enviados** (nova versão, editar metadados, arquivar, mover entre pastas). | **DRAFT** — este documento. Aguarda Review de Spec e DoR-Implementation antes de TK-DOC-UPLOAD-004+. |

Conteúdo marcado **[Fase 1]** já foi revisado e implementado — não reabrir sem
decisão formal. Conteúdo marcado **[Fase 2]** é a matéria desta revisão.

---

# Objetivo

Permitir que colaboradores com papel `ADMINISTRADOR` — na atribuição escopada a
Federação, Singular, Área ou Equipe (`PAPEL_ATRIBUICAO`) — **produzam e mantenham**
o acervo documental das pastas para as quais essa atribuição tem grant de edição
(`PERMISSAO_PASTA.TIP_ACESSO='EDICAO'`):

- **[Fase 1]** enviar um novo arquivo para uma pasta existente;
- **[Fase 2]** organizar a hierarquia de pastas (criar, renomear, mover, arquivar) e
  manter os documentos já enviados (nova versão, metadados, arquivamento, mover).

Sem gerenciar quem tem acesso (`PERMISSAO_PASTA`) pela aplicação — isso permanece
dado institucional.

**Origem:** `specs/features/arquivos/specification.md` § Fora do Escopo previa
*"Upload, edição ou exclusão de arquivos pelo colaborador — decisão de produto
(2026-08-26) [...]. Fica para Feature futura se priorizado."* A Fase 1 cobriu o
upload; a Fase 2 cobre edição/exclusão e a gestão de pastas, conforme decisões do
usuário de 2026-08-27 (ver `decisions.md`).

---

# Escopo

## [Fase 1] Incluído — decidido (2026-08-27), implementado

- Upload de um arquivo para uma pasta (`PASTA`) já existente, criando `DOCUMENTO`
  (novo) + `DOCUMENTO_VERSAO` (`NUM_VERSAO=1`, `FLG_VERSAO_ATUAL='S'`) +
  `ARQUIVO_BINARIO` (novo).
- Autorização por papel: somente colaborador cuja atribuição ativa tem
  `papel = ADMINISTRADOR`, escopado via `PAPEL_ATRIBUICAO`.
- Metadados mínimos no upload: título do documento e o próprio arquivo.
- **Categoria documental derivada automaticamente do `TIP_MIME`** (`Documentos` |
  `Imagens` | `Vídeos` | `Outros`) — sem seletor. Ver § Categorização por tipo de mídia.

## [Fase 2] Incluído — decidido (2026-08-27), a implementar

Todas as operações abaixo exigem o **mesmo modelo de autorização** da Fase 1
(atribuição ativa `ADMINISTRADOR` + grant `EDICAO` compatível na pasta alvo — para
operações de documento, a pasta do documento). Ver § Modelo de Autorização e
`decisions.md` D-02.

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
  para arquivar nesta fase (`decisions.md` D-06); documento arquivado continua
  visível na leitura conforme `RF-DOCUMENTO-004` de `FT-DOCUMENTO`.
- **Mover** documento entre pastas (alterar `COD_PASTA`) — exige grant `EDICAO` na
  pasta de origem **e** na pasta destino (ativa).

## Fora do Escopo — decidido (2026-08-27)

- **Gerenciar `PERMISSAO_PASTA`** (criar/revogar grants) pela aplicação — sem tela,
  sem endpoint (`decisions.md` D-03). Grants de `EDICAO` são dado institucional
  (`database/dml/`). `TIP_ACESSO='ADMINISTRACAO'` permanece reservado.
- **Exclusão física ou lógica real de documento** — nesta fase só arquivamento
  (`decisions.md` D-06). Exclusão de verdade (sumir da leitura) = fase futura +
  migration.
- **Desarquivar** documento (`ARQUIVADO` → `ATIVO`) e **reativar** pasta
  (`FLG_ATIVO='N' → 'S'`) — fase futura.
- **Exclusão de pasta em cascata** — só soft-delete de pasta vazia (`decisions.md` D-05).
- **Herança viva de permissão** entre pastas (`FLG_HERDA_PERMISSAO`, `OQ-012`,
  `BR-017`) — a Fase 2 usa cópia snapshot (`decisions.md` D-04), não herança.
- **Papel `GESTOR_DOCUMENTAL`** — não estendido (`decisions.md` D-02).
- **Escolha de categoria** no upload ou na nova versão — sempre derivada do `TIP_MIME`.
- **`Comunicado` como categoria de documento** — é publicação do CMS (DEC-CMS-002),
  não `CATEGORIA_DOCUMENTAL`.
- **Quota de armazenamento por colaborador** (`BR-023`) — não validada. Risco aceito.
- **Migração de arquivos do CMS/legado** — fora do escopo da reconstrução
  (`DS-RECONSTRUCTION-SCOPE-01` §3).

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador com atribuição ativa `ADMINISTRADOR` (Federação, Singular, Área ou Equipe) | Único ator autorizado — restrito às pastas com grant `EDICAO` compatível com o nível dessa atribuição. Pode: enviar arquivo, gerir a hierarquia de pastas e manter os documentos já enviados dessas pastas. |
| Colaborador sem atribuição `ADMINISTRADOR` ativa | Sem acesso a esta Feature; continua restrito à leitura de `FT-DOCUMENTO`. |

---

# Modelo de Autorização

Reaproveita integralmente o mecanismo multi-nível de `FT-DOCUMENTO`
(`PERMISSAO_PASTA`), estendido com a checagem de papel. **Vale para toda operação
de escrita desta Feature** (Fase 1 e Fase 2):

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
| Upload / nova versão / editar metadados / arquivar documento | a pasta do documento (`DOCUMENTO.COD_PASTA`) |
| Criar subpasta | a pasta-pai (`COD_PASTA_PAI`) |
| Renomear / arquivar pasta | a própria pasta |
| Mover pasta | a pasta movida **e** a pasta destino |
| Mover documento | a pasta de origem **e** a pasta destino |

**Decisão técnica (2026-08-27):** toda a Fase 2 mapeia para `TIP_ACESSO='EDICAO'`.
`ADMINISTRACAO` fica reservado para uma futura Feature de gestão de grants
(`decisions.md` D-03).

**Sem grant `EDICAO` compatível → `403` explícito**, mesmo padrão de
`RF-DOCUMENTO-003` (nunca `404` disfarçado).

**Nota:** a atribuição `ADMINISTRADOR` precisa estar **ativa** (Contexto Ativo
atual), não apenas existir entre as elegíveis — mesmo padrão de `FT-SESSION` /
`FT-AREA-COLABORADOR`.

---

# Requisitos Funcionais

## [Fase 1]

### RF-DOC-UPLOAD-001 — Upload de arquivo em pasta existente

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-001 |
| Descrição | O sistema deve permitir que um colaborador com atribuição ativa `ADMINISTRADOR` envie um arquivo para uma pasta existente, criando um novo `DOCUMENTO` (`STA_DOCUMENTO='ATIVO'`, `COD_CATEGORIA_DOCUMENTAL` derivado do `TIP_MIME`, `COD_COLABORADOR` = colaborador autenticado), sua `DOCUMENTO_VERSAO` inicial (`NUM_VERSAO=1`, `FLG_VERSAO_ATUAL='S'`) e o `ARQUIVO_BINARIO` correspondente no Object Storage (DEC-013). |

### RF-DOC-UPLOAD-002 — Restringir escrita por papel e grant de edição

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-002 |
| Descrição | O sistema deve negar (`403`) **qualquer operação de escrita desta Feature** (Fase 1 e Fase 2) quando a atribuição ativa do colaborador não tem papel `ADMINISTRADOR`, ou quando não existe `PERMISSAO_PASTA` com `TIP_ACESSO='EDICAO'` para o nível dessa atribuição na(s) pasta(s) alvo da operação (ver § Modelo de Autorização — tabela de pasta alvo). |
| Regra de Negócio | `BR-012` (Contexto Ativo orienta autorização) |

### RF-DOC-UPLOAD-003 — Recurso inexistente

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-003 |
| Descrição | O sistema deve retornar `404` quando o identificador de pasta ou de documento alvo da operação (incluindo pasta-pai ou pasta destino em operações de criação/mover) não corresponde a nenhum registro existente. |

## [Fase 2] — Gestão de pastas

### RF-DOC-UPLOAD-004 — Criar subpasta com cópia de grants

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-004 |
| Descrição | O sistema deve permitir que um colaborador `ADMINISTRADOR` com grant `EDICAO` numa pasta P crie uma nova `PASTA` filha (`COD_PASTA_PAI = P`, `FLG_ATIVO='S'`, `NOM_PASTA` obrigatório, `DSC_PASTA` opcional). Na mesma transação, o sistema **copia** para a nova pasta todas as linhas de `PERMISSAO_PASTA` de P (`TIP_DESTINATARIO`, `COD_DESTINATARIO`, `TIP_ACESSO` — cópia idêntica), de modo que a subpasta nasça com a mesma audiência da pai. |
| Regra de Negócio | `BR-016`, `BR-017` (organização em pasta / contexto) |
| Decisão | `decisions.md` D-04 |

### RF-DOC-UPLOAD-005 — Renomear pasta

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-005 |
| Descrição | O sistema deve permitir alterar `NOM_PASTA` (obrigatório quando enviado — não em branco) e/ou `DSC_PASTA` de uma pasta ativa cuja atribuição ativa tem grant `EDICAO`. Não altera hierarquia nem permissões. |

### RF-DOC-UPLOAD-006 — Mover pasta

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-006 |
| Descrição | O sistema deve permitir alterar `COD_PASTA_PAI` de uma pasta ativa para outra pasta destino ativa, exigindo grant `EDICAO` (no nível da atribuição ativa) **tanto na pasta movida quanto na pasta destino**. O sistema deve rejeitar (`409`) mover a pasta para ela mesma ou para qualquer descendente seu (prevenção de ciclo). As linhas de `PERMISSAO_PASTA` da pasta movida **não** são alteradas. |
| Decisão | `decisions.md` D-08 |

### RF-DOC-UPLOAD-007 — Arquivar pasta (soft-delete)

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-007 |
| Descrição | O sistema deve permitir marcar `FLG_ATIVO='N'` numa pasta cuja atribuição ativa tem grant `EDICAO`, **somente se** a pasta não tiver nenhuma subpasta com `FLG_ATIVO='S'` nem nenhum `DOCUMENTO` com `STA_DOCUMENTO IN ('ATIVO','ARQUIVADO')`. Caso contrário → `409`. Pasta com `FLG_ATIVO='N'` deixa de aparecer na listagem de `FT-DOCUMENTO` e não aceita novas operações de escrita. |
| Decisão | `decisions.md` D-05 |

## [Fase 2] — Gestão de documentos já enviados

### RF-DOC-UPLOAD-008 — Enviar nova versão de documento

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-008 |
| Descrição | O sistema deve permitir enviar um novo binário para um `DOCUMENTO` com `STA_DOCUMENTO='ATIVO'` cuja pasta tem grant `EDICAO` compatível. Cria um novo `ARQUIVO_BINARIO` e um novo `DOCUMENTO_VERSAO` (`NUM_VERSAO` = maior `NUM_VERSAO` atual + 1, `FLG_VERSAO_ATUAL='S'`, `COD_COLABORADOR` = colaborador autenticado, `DSC_ALTERACAO` = nota opcional do request); a versão anteriormente atual passa a `FLG_VERSAO_ATUAL='N'`. `DOCUMENTO.COD_CATEGORIA_DOCUMENTAL` é re-derivado do `TIP_MIME` do novo binário. Operação atômica; binário gravado por último (rollback se o storage falhar). Documento não-`ATIVO` → `409`. |
| Decisão | `decisions.md` D-07 |

### RF-DOC-UPLOAD-009 — Editar metadados do documento

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-009 |
| Descrição | O sistema deve permitir alterar `TIT_DOCUMENTO` (não em branco quando enviado) e/ou `DSC_DOCUMENTO` de um `DOCUMENTO` cuja pasta tem grant `EDICAO`. Não altera binário, versões nem categoria. Requisição sem nenhum campo → `400`. |

### RF-DOC-UPLOAD-010 — Arquivar documento

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-010 |
| Descrição | O sistema deve permitir alterar `STA_DOCUMENTO` de `ATIVO` para `ARQUIVADO` num documento cuja pasta tem grant `EDICAO`. Nesta fase não há exclusão física nem lógica — "excluir documento" resolve para arquivar. Documento já `ARQUIVADO` ou `EXPIRADO` → `409`. O documento `ARQUIVADO` permanece visível na leitura conforme `RF-DOCUMENTO-004` de `FT-DOCUMENTO`. |
| Decisão | `decisions.md` D-06 |

### RF-DOC-UPLOAD-011 — Mover documento entre pastas

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-011 |
| Descrição | O sistema deve permitir alterar `DOCUMENTO.COD_PASTA` para outra pasta destino com `FLG_ATIVO='S'`, exigindo grant `EDICAO` (no nível da atribuição ativa) **tanto na pasta de origem quanto na pasta destino**. Pasta destino inexistente → `404`; sem grant compatível em qualquer das duas → `403`. |
| Decisão | `decisions.md` D-08 |

---

# Modelo de Dados

**Físico, já instalado** — mesmas tabelas de `specs/features/arquivos/specification.md`
§ Modelo de Dados (`PASTA`, `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`,
`PERMISSAO_PASTA`, `CATEGORIA_DOCUMENTAL`), mais `PAPEL`/`PAPEL_ATRIBUICAO`. Esta
Feature **não cria tabelas novas**.

## [Fase 1] Escritas

`INSERT` em `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`; leitura de
`CATEGORIA_DOCUMENTAL`. Ver Versão 1.3 desta spec (histórico) e `api.md` para o
detalhe das colunas.

## [Fase 2] Escritas

```text
PASTA
├── INSERT (RF-004: criar subpasta) — COD_PASTA_PAI = pasta alvo, NOM_PASTA/DSC_PASTA
│                                     do request, FLG_ATIVO='S'
├── UPDATE NOM_PASTA/DSC_PASTA (RF-005: renomear)
├── UPDATE COD_PASTA_PAI (RF-006: mover — com checagem anti-ciclo)
└── UPDATE FLG_ATIVO='N' (RF-007: arquivar — com guarda de pasta não-vazia)

PERMISSAO_PASTA
└── INSERT (RF-004: cópia snapshot dos grants da pasta-pai para a subpasta nova)
    — nunca UPDATE/DELETE por esta Feature (decisions.md D-03)

DOCUMENTO
├── UPDATE TIT_DOCUMENTO/DSC_DOCUMENTO (RF-009: editar metadados)
├── UPDATE COD_CATEGORIA_DOCUMENTAL (RF-008: re-derivada na nova versão)
├── UPDATE COD_PASTA (RF-011: mover documento)
└── UPDATE STA_DOCUMENTO='ARQUIVADO' (RF-010: arquivar)

DOCUMENTO_VERSAO
├── INSERT (RF-008: nova versão — NUM_VERSAO = max+1, FLG_VERSAO_ATUAL='S',
│           DSC_ALTERACAO opcional)
└── UPDATE FLG_VERSAO_ATUAL='N' na versão anteriormente atual (RF-008)

ARQUIVO_BINARIO
└── INSERT (RF-008: novo binário da nova versão)
```

`CK_DOCUMENTO_VERSAO_ATUAL` (`database/ddl/004-create-constraints.sql`) exige no
máximo uma versão `FLG_VERSAO_ATUAL='S'` por documento — a nova versão deve
rebaixar a anterior na mesma transação.

## Categorização por tipo de mídia (Fase 1 e Fase 2)

`CATEGORIA_DOCUMENTAL` classifica o documento pelo **tipo de mídia do arquivo
atual**. O backend resolve `COD_CATEGORIA_DOCUMENTAL` a partir de
`ARQUIVO_BINARIO.TIP_MIME`, buscando por `NOM_CATEGORIA` (`FLG_ATIVO='S'`):

| `NOM_CATEGORIA` | Regra sobre `TIP_MIME` |
|---|---|
| `Documentos` | `application/pdf`, `application/msword`, `application/vnd.openxmlformats-officedocument.*`, `application/vnd.ms-excel`, `text/plain`, `text/csv`, `application/vnd.oasis.opendocument.*` |
| `Imagens` | `image/*` |
| `Vídeos` | `video/*` |
| `Outros` | qualquer outro `TIP_MIME` |

- As 4 categorias são dado institucional (`V009`) — a aplicação nunca cria categoria.
- Categoria resolvida inexistente no banco → erro de configuração explícito
  (fail-fast), nunca `DOCUMENTO` sem categoria.
- O backend **sempre** resolve por `NOM_CATEGORIA`, nunca por ID fixo.
- Na nova versão (RF-008) a categoria é re-derivada do novo `TIP_MIME` (`decisions.md` D-07).

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| `FT-DOCUMENTO` (`DONE`) | Pré-requisito | Reaproveita entidades, `PermissaoPastaDomainService` (estender), padrão de `PastaController`/`DocumentoController`, e a query de leitura (`GET /api/v1/pastas`, `GET /api/v1/documentos/{id}/download`) — que já filtra `FLG_ATIVO='N'` (pastas) e `EXPIRADO` (documentos) |
| Sequences `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_VERSAO` (Fase 1) | ✅ resolvida | `V009` executado e validado (2026-08-27) — cobre também a Fase 2 (nova versão reusa `SQ_DOCUMENTO_VERSAO`) |
| Sequences `SQ_PASTA`, `SQ_PERMISSAO_PASTA` (**Fase 2**) | **Bloqueante (execução)** | **Não existem** — o baseline homologado tem 12 sequences e nenhuma delas é de `PASTA`/`PERMISSAO_PASTA`; as tabelas não têm coluna `IDENTITY`. `RF-DOC-UPLOAD-004` faz `INSERT` em ambas. Requer `V010` análogo ao `V009` (2× `CREATE SEQUENCE` + `GRANT SELECT` p/ `UNMPORTCOM_APP_ROLE`, DEC-DB-024). Confirmar via JDBC no DoR-Implementation. Ver TK-DOC-UPLOAD-004. |
| `ObjectStorageClient.upload(...)` | ✅ resolvida (Fase 1) | `S3ObjectStorageClient` já implementado — a nova versão (RF-008) reusa o mesmo método |
| Grants `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) nas pastas de homologação | Bloqueante (execução) | Dado institucional (`database/dml/`), mesma pendência da Fase 1 |
| Provisionamento do Object Storage (DEC-013) | Bloqueante (execução) | Mesma pendência já registrada em `FT-DOCUMENTO` |

**Nenhuma outra migration é necessária para a Fase 2:** renomear/mover são `UPDATE`;
arquivamento de pasta usa `FLG_ATIVO` (coluna existente); arquivamento de documento
usa `STA_DOCUMENTO='ARQUIVADO'` (valor já no CHECK); nova versão usa
`SQ_DOCUMENTO_VERSAO` (criada no `V009`).

---

# Decisão de produto/arquitetura pendente

Nenhuma pendência de **spec** conhecida para a Fase 2 — as 8 decisões estão em
`decisions.md`. Situação das pendências de **execução** (para o DoR-Implementation
da Fase 2):

1. **`SQ_PASTA` / `SQ_PERMISSAO_PASTA` ausentes.** Bloqueia `RF-DOC-UPLOAD-004`.
   Propor `V010` (análogo ao `V009`), execução manual pelo DBA (`DEC-DB-019`, sem
   Flyway). Confirmar ausência via JDBC antes.
2. **Grants `EDICAO` institucionais.** Mesma pendência da Fase 1 — sem grant `EDICAO`
   nas pastas reais, nenhuma operação da Fase 2 funciona em homologação.
3. **Exclusão real de documento (dívida registrada, não bloqueia a Fase 2).** Se o
   produto quiser que "excluir" some da leitura do colaborador, será preciso um novo
   valor de `STA_DOCUMENTO` (ou `FLG` em `DOCUMENTO`) + ajuste da query de
   `FT-DOCUMENTO` — fase futura (`decisions.md` D-06).
4. **Registro de governança.** As decisões D-04 (snapshot de grants) e D-06
   (excluir = arquivar) devem ser referenciadas em `docs/domain/10-open-questions.md`
   (OQ-011, OQ-012) e, se o time adotar, em `docs/technology/04-decision-log.md`.
   Ação no monorepo, não bloqueia a implementação.

---

# Fontes

`specs/features/arquivos/` (Feature predecessora — `FT-DOCUMENTO`, `DONE`);
`specs/features/documento-upload/decisions.md` (decisões da Fase 2);
`database/ddl/002-create-sequences.sql` (12 sequences do baseline — sem
`SQ_PASTA`/`SQ_PERMISSAO_PASTA`), `003-create-tables.sql` (`PASTA`, `DOCUMENTO`,
`DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`, `PERMISSAO_PASTA` — schema físico),
`004-create-constraints.sql` (`CK_DOCUMENTO_STATUS`, `CK_DOCUMENTO_VERSAO_ATUAL`);
`database/migrations/V009__documento_upload_sequences_e_categorias.sql`;
`docs/domain/09-business-rules.md` (BR-012, BR-016, BR-017, BR-018, BR-020, BR-023);
`docs/domain/10-open-questions.md` (OQ-006, OQ-011, OQ-012 — abertas; OQ-004 —
encerrada por DEC-CMS-002); `docs/technology/04-decision-log.md` (DEC-013,
DEC-CMS-002, DEC-DB-019, DEC-DB-024).

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — Fase 1 (upload apenas), decompõe o "Fora do Escopo" de `FT-DOCUMENTO` |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review de Spec (`7d643eb`): categoria derivada do `TIP_MIME`; `COD_COLABORADOR` da sessão; `Comunicado` = publicação WordPress; erros `400`/`413` |
| 1.2 | 2026-08-27 | Claude Code | Ajuste: só `SQ_ARQUIVO_BINARIO`/`SQ_DOCUMENTO_VERSAO` são bloqueio da Fase 1 |
| 1.3 | 2026-08-27 | Claude Code | `V009` executado e validado — pendências de execução #1/#2 da Fase 1 resolvidas |
| **2.0** | **2026-08-27** | **Claude Code (Specify)** | **Fase 2 (DRAFT):** gestão de pastas (RF-004..007) e de documentos já enviados (RF-008..011); RF-002/003 generalizados para toda operação de escrita; `Modelo de Autorização` com tabela de pasta alvo por operação; `decisions.md` novo (D-01..D-08); dependência `SQ_PASTA`/`SQ_PERMISSAO_PASTA` (`V010`) registrada; estrutura em Fases |
