# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — cria documento/versão/binário; sem CRUD de pasta) |
| Versão | 1.4 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-UPLOAD |
| Feature | Upload de Arquivos e Documentos |
| Domínio | DOCUMENTO |
| Tipo | Incremento de escrita sobre `FT-DOCUMENTO` (DONE, somente leitura) |
| Status | APPROVED |

---

# Objetivo

Permitir que colaboradores com papel `ADMINISTRADOR` — na atribuição escopada a Federação, Singular, Área ou Equipe (`PAPEL_ATRIBUICAO`) — enviem (upload) um novo arquivo para uma pasta já existente para a qual essa atribuição tem grant de edição, sem criar/editar/excluir pastas nem gerenciar versões de documentos já enviados.

**Origem:** `specs/features/arquivos/specification.md` § Fora do Escopo já previa isso: *"Upload, edição ou exclusão de arquivos pelo colaborador — decisão de produto (2026-08-26), não só ausência no Figma. Fica para Feature futura se priorizado."* Esta é essa Feature futura, com escopo deliberadamente reduzido ao caso mínimo (decisões do usuário, 2026-08-27 — ver § Escopo).

**Por que uma Feature nova, não uma reabertura de `FT-DOCUMENTO`:** `FT-DOCUMENTO` está `DONE` (MVP somente leitura entregue e fechado) e o próprio texto do seu Fora do Escopo já apontava para uma Feature futura, não para uma nova versão da mesma. `specs/foundation/feature-yaml.md` não define transição `DONE → DRAFT`; abrir uma Feature nova preserva o histórico de `FT-DOCUMENTO` intacto e mantém a máquina de estados oficial sem exceções.

---

# Escopo

## Incluído — decidido (2026-08-27)

- Upload de um arquivo para uma pasta (`PASTA`) já existente, criando `DOCUMENTO` (novo) + `DOCUMENTO_VERSAO` (versão inicial, `NUM_VERSAO=1`, `FLG_VERSAO_ATUAL='S'`) + `ARQUIVO_BINARIO` (novo).
- Autorização por papel: **somente** colaborador cuja atribuição ativa (Contexto Ativo) tem `papel = ADMINISTRADOR` — **reaproveitando o papel já existente em `PAPEL`**, escopado via `PAPEL_ATRIBUICAO` (`COD_FEDERACAO`/`COD_SINGULAR`/`COD_AREA`/`COD_EQUIPE`, um populado por atribuição). **Não** cria papéis novos (`ADMINISTRADOR_AREA`, `ADMINISTRADOR_SINGULAR`, `ADMINISTRADOR_FEDERACAO`, `ADMINISTRADOR_EQUIPE` não existem como valores de `PAPEL` — ver § Modelo de Autorização).
- Metadados mínimos no upload: título do documento e o próprio arquivo (nome, mime, tamanho, hash — derivados do arquivo enviado).
- **Categoria documental (`COD_CATEGORIA_DOCUMENTAL`) derivada automaticamente do tipo de mídia do arquivo** (`ARQUIVO_BINARIO.TIP_MIME` → `Documentos` | `Imagens` | `Vídeos` | `Outros`) — sem seletor no request nem no frontend, sem configuração. Decisão do usuário (2026-08-27): `CATEGORIA_DOCUMENTAL` está vazia no banco e a taxonomia do seed histórico (`Normativos`/`Manuais`/…) não reflete o produto; passa a ser tipo de mídia. Ver § Modelo de Dados → *Categorização por tipo de mídia*.

## Fora do Escopo — decidido (2026-08-27)

- **Gerenciar hierarquia de pastas** (criar, renomear, mover, excluir `PASTA`) — decisão de produto explícita do usuário: só upload de arquivo em pasta já existente nesta entrega. Coberto pela Feature irmã **`FT-DOCUMENTO-GESTAO`** (`specs/features/documento-gestao/`, `DRAFT` — 2026-08-27).
- **Gerenciar o documento após o upload inicial**: sem editar metadados, sem enviar nova versão (`DOCUMENTO_VERSAO` adicional) de um documento existente, sem excluir/arquivar (`STA_DOCUMENTO`) — decisão de produto explícita do usuário. Coberto por **`FT-DOCUMENTO-GESTAO`**.
- **Gerenciar `PERMISSAO_PASTA`** (criar/revogar grants) pela aplicação — fora de escopo; grants de `EDICAO` necessários para esta Feature são dado institucional (`database/dml/`), não uma tela da aplicação.
- **Papel `GESTOR_DOCUMENTAL`** — já existe em `PAPEL` (seed, `database/ddl/008-initial-data.sql`) e nominalmente parece feito para gestão documental, mas o usuário só autorizou `ADMINISTRADOR` escopado nesta entrega. Não estender a este papel sem nova decisão de produto.
- **Escolha de categoria no upload** — a categoria é derivada do tipo de mídia (ver § Incluído); um seletor de categoria (e a reconciliação da taxonomia de `CATEGORIA_DOCUMENTAL` com o produto) fica para uma Feature futura de categorização documental.
- **`Comunicado` como categoria de documento** — decisão do usuário (2026-08-27): `Comunicado` é uma **publicação** (título, descrição, imagem de destaque opcional, arquivos anexos) servida por API do WordPress integrada ao backend (`FT-NOTICIA` / repo `portal-comunicacao-cms`), **não** um valor de `CATEGORIA_DOCUMENTAL`. Resolve o conflito registrado em `docs/domain/10-open-questions.md` OQ-004. Integração WordPress↔backend está fora do escopo desta Feature.
- **Quota de armazenamento por colaborador** (`BR-023`, `docs/domain/09-business-rules.md`) — regra de domínio já catalogada, não implementada aqui; upload não valida quota nesta entrega. Risco aceito, registrado como dívida.
- Migração de arquivos do CMS/legado — fora do escopo da reconstrução (`DS-RECONSTRUCTION-SCOPE-01` §3).

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador com atribuição ativa `ADMINISTRADOR` (Federação, Singular, Área ou Equipe) | Único ator autorizado a fazer upload — restrito às pastas com grant `EDICAO` compatível com o nível dessa atribuição |
| Colaborador sem atribuição `ADMINISTRADOR` ativa | Sem acesso a este RF; continua restrito à leitura já coberta por `FT-DOCUMENTO` |

---

# Modelo de Autorização

Reaproveita integralmente o mecanismo multi-nível de `FT-DOCUMENTO` (`PERMISSAO_PASTA`), estendido com a checagem de papel:

```text
upload autorizado quando:
  atribuição ativa do colaborador (Contexto Ativo, JwtAuthenticatedPrincipal.papelAtribuicaoId)
    → PAPEL_ATRIBUICAO.COD_PAPEL → PAPEL.NOM_PAPEL = 'ADMINISTRADOR'
  E existe PERMISSAO_PASTA da pasta alvo com
    TIP_DESTINATARIO/COD_DESTINATARIO correspondente ao nível populado nessa
    PAPEL_ATRIBUICAO (o mesmo nível que já resolve o Contexto Ativo:
    COD_FEDERACAO → FEDERACAO, COD_SINGULAR → SINGULAR, COD_AREA → AREA,
    COD_EQUIPE → EQUIPE)
    E TIP_ACESSO = 'EDICAO'
```

**Decisão técnica (2026-08-27):** upload mapeia para `TIP_ACESSO='EDICAO'` (schema já prevê `LEITURA | DOWNLOAD | EDICAO | ADMINISTRACAO`, `EDICAO`/`ADMINISTRACAO` não usados por `FT-DOCUMENTO`). `ADMINISTRACAO` fica reservado para uma futura Feature de gestão de grants/pastas — coerente com `EDICAO` = "escrever conteúdo" vs `ADMINISTRACAO` = "administrar quem tem acesso". Ajustável se o Product Owner discordar — nenhum código depende disso ainda.

**Sem grant `EDICAO` compatível → `403` explícito**, mesmo padrão de `RF-DOCUMENTO-003` (nunca `404` disfarçado).

**Nota:** a atribuição `ADMINISTRADOR` precisa estar **ativa** (Contexto Ativo atual), não apenas existir entre as atribuições elegíveis do colaborador — mesmo padrão de resolução de sessão já usado por `FT-SESSION`/`FT-AREA-COLABORADOR`.

---

# Requisitos Funcionais

## RF-DOC-UPLOAD-001 — Upload de arquivo em pasta existente

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-001 |
| Descrição | O sistema deve permitir que um colaborador com atribuição ativa `ADMINISTRADOR` envie um arquivo para uma pasta existente, criando um novo `DOCUMENTO` (`STA_DOCUMENTO='ATIVO'`, `COD_CATEGORIA_DOCUMENTAL` derivado do `TIP_MIME`, `COD_COLABORADOR` = colaborador autenticado), sua `DOCUMENTO_VERSAO` inicial (`NUM_VERSAO=1`, `FLG_VERSAO_ATUAL='S'`, `COD_COLABORADOR` = mesmo colaborador) e o `ARQUIVO_BINARIO` correspondente no Object Storage (DEC-013). |

## RF-DOC-UPLOAD-002 — Restringir upload por papel e grant de edição

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-002 |
| Descrição | O sistema deve negar o upload (`403`) quando a atribuição ativa do colaborador não tem papel `ADMINISTRADOR`, ou quando não existe `PERMISSAO_PASTA` com `TIP_ACESSO='EDICAO'` para o nível dessa atribuição na pasta alvo. |
| Regra de Negócio | `BR-012` (Contexto Ativo orienta autorização) — `docs/domain/09-business-rules.md` |

## RF-DOC-UPLOAD-003 — Pasta inexistente

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-UPLOAD-003 |
| Descrição | O sistema deve retornar `404` ao tentar upload para um identificador de pasta que não existe. |

---

# Decisões resolvidas no Review (2026-08-27)

Não conformidades do Review de Spec (`docs/…` — commit `7d643eb`), agora fechadas nesta spec:

- **Origem de `COD_CATEGORIA_DOCUMENTAL`** (era "a definir"): derivada de `ARQUIVO_BINARIO.TIP_MIME` → `Documentos` / `Imagens` / `Vídeos` / `Outros`. Sem parâmetro, sem config. Ver § Modelo de Dados → *Categorização por tipo de mídia*.
- **`DOCUMENTO_VERSAO.COD_COLABORADOR`** (não constava no modelo de dados): é o colaborador autenticado que fez o upload — mesmo valor de `DOCUMENTO.COD_COLABORADOR`. Nenhum `COD_COLABORADOR` vem do request.
- **Códigos de erro de validação do multipart**: `400` para `titulo`/`arquivo` ausentes ou arquivo vazio (herda `docs/implementation/07-api-standards.md`); explicitados em `api.md`.

---

# Decisão de produto/arquitetura pendente

Nenhuma pendência de **spec**. Situação das pendências de **execução** (para o DoR-Implementation):

1. ~~**Sequences Oracle ausentes.**~~ ✅ **RESOLVIDO (2026-08-27)** — `V009` executado: `SQ_ARQUIVO_BINARIO` e `SQ_DOCUMENTO_VERSAO` criadas + `GRANT SELECT` para `UNMPORTCOM_APP_ROLE`, validado via JDBC. `SQ_DOCUMENTO_COD_DOCUMENTO` já existia. `SQ_CAT_DOC_COD_CAT_DOC` (referenciada por `008-initial-data.sql`) não é necessária — a aplicação nunca insere categoria; é item de reconciliação greenfield do baseline (`database/migrations/README.md`).
2. ~~**`CATEGORIA_DOCUMENTAL` sem dados.**~~ ✅ **RESOLVIDO (2026-08-27)** — `V009` inseriu `Documentos`/`Imagens`/`Vídeos`/`Outros` (IDs 1–4, `FLG_ATIVO='S'`), validado via JDBC.
3. **Dados institucionais — grants `EDICAO` ausentes.** Upload só funciona em pastas com `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) para o nível da atribuição. Se o seed atual só tem `LEITURA`/`DOWNLOAD` (caso de `FT-DOCUMENTO`), é carga institucional (`database/dml/`) a confirmar/propor ao DBA — não uma tela desta Feature.
4. **`ObjectStorageClient` só tem `download(referenciaObjeto)`.** Precisa de método de escrita (ex. `upload(referenciaObjeto, InputStream, tamanho, tipoMime)`) — contrato novo sobre o mesmo Object Storage S3-compatível (DEC-013), sem mudar de provedor.
5. **Teto de tamanho de arquivo** — sem limite explícito definido. Backend deve aplicar um teto operacional razoável (a definir em `tasks.md`/implementação; retorna `413`) — decisão técnica mínima de segurança, não a quota de `BR-023` (fora de escopo).
6. **Registro de decisão da taxonomia** — a redefinição de `CATEGORIA_DOCUMENTAL` (doc institucional → tipo de mídia) e `Comunicado` = publicação WordPress precisa ser registrada em `docs/technology/04-decision-log.md` e `docs/domain/10-open-questions.md` (OQ-004). É ação de governança no monorepo, não bloqueia a implementação desta Feature.

---

# Modelo de Dados

**Físico, já instalado** — mesmas tabelas de `specs/features/arquivos/specification.md` § Modelo de Dados (`PASTA`, `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`, `PERMISSAO_PASTA`, `CATEGORIA_DOCUMENTAL`), mais `PAPEL`/`PAPEL_ATRIBUICAO` (`accesscontrol`, já usados por `FT-SESSION`). Esta Feature **não cria tabelas novas** — apenas passa a fazer `INSERT` em três delas (`DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`) e a **ler** `CATEGORIA_DOCUMENTAL`, o que exige as sequences e o DML dos itens 1–2 acima.

```text
DOCUMENTO (INSERT novo)
├── COD_DOCUMENTO (PK — SQ_DOCUMENTO_COD_DOCUMENTO, já existe)
├── COD_PASTA (FK → PASTA, pasta alvo do upload)
├── COD_CATEGORIA_DOCUMENTAL (FK NOT NULL — derivado do TIP_MIME do arquivo; ver "Categorização por tipo de mídia" abaixo. Nunca vem do request)
├── COD_COLABORADOR (FK NOT NULL — colaborador autenticado que fez o upload; da sessão, não do request)
├── TIT_DOCUMENTO (do request)
└── STA_DOCUMENTO = 'ATIVO' (sempre, na criação)

DOCUMENTO_VERSAO (INSERT novo — requer sequence nova, ver Decisão #1)
├── COD_DOCUMENTO_VERSAO (PK)
├── COD_DOCUMENTO (FK → DOCUMENTO recém-criado)
├── COD_ARQUIVO_BINARIO (FK → ARQUIVO_BINARIO recém-criado)
├── COD_COLABORADOR (FK NOT NULL — autor desta versão; na criação inicial = DOCUMENTO.COD_COLABORADOR = colaborador que fez o upload)
├── NUM_VERSAO = 1
└── FLG_VERSAO_ATUAL = 'S'

ARQUIVO_BINARIO (INSERT novo — requer sequence nova, ver Decisão #1)
├── COD_ARQUIVO_BINARIO (PK)
├── NOM_ARQUIVO, TIP_MIME, QTD_TAMANHO_BYTES, HASH_ARQUIVO (derivados do arquivo enviado)
└── URL_ARQUIVO (referência ao objeto gravado no Object Storage — nunca exposta ao cliente, ADR-004)
```

## Categorização por tipo de mídia

`CATEGORIA_DOCUMENTAL` passa a classificar o documento pelo **tipo de mídia do arquivo** (decisão do usuário, 2026-08-27 — a tabela está vazia no banco e a taxonomia do seed histórico não reflete o produto). O backend resolve `COD_CATEGORIA_DOCUMENTAL` a partir de `ARQUIVO_BINARIO.TIP_MIME`, buscando a categoria por `NOM_CATEGORIA` (`FLG_ATIVO='S'`):

| `NOM_CATEGORIA` | Regra sobre `TIP_MIME` (indicativo — refinar em `tasks.md`) |
|---|---|
| `Documentos` | `application/pdf`, `application/msword`, `application/vnd.openxmlformats-officedocument.*`, `application/vnd.ms-excel`, `text/plain`, `text/csv`, `application/vnd.oasis.opendocument.*` |
| `Imagens` | `image/*` |
| `Vídeos` | `video/*` |
| `Outros` | qualquer outro `TIP_MIME` |

- As 4 categorias são dado institucional criado pelo script `V009` — não são criadas pela aplicação.
- Se a categoria resolvida não existir no banco → erro explícito de configuração (fail-fast), nunca `DOCUMENTO` sem categoria.
- IDs (`COD_CATEGORIA_DOCUMENTAL`) não são determinísticos (sequence) — o backend **sempre** resolve por `NOM_CATEGORIA`, nunca por ID fixo.

**Resolução de RF-DOC-UPLOAD-002 (autorização):** ver § Modelo de Autorização.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| `FT-DOCUMENTO` (`DONE`) | Pré-requisito | Reaproveita entidades (`CategoriaDocumentalEntity`/`Repository` já existem), `PermissaoPastaDomainService` (estender, não duplicar) e o padrão de `PastaController`/`DocumentoController` |
| Sequences `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_VERSAO` | Bloqueante (execução) | Não existem — script `V009` (SQL simples), execução manual (ver Decisão #1). Aplicação não cria schema (`DEC-DB-019`, sem Flyway) |
| Linhas de `CATEGORIA_DOCUMENTAL` (`Documentos`/`Imagens`/`Vídeos`/`Outros`) | Bloqueante (execução) | Tabela vazia no banco — `INSERT` no `V009` (ver Decisão #2) |
| Grants `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) para os níveis pretendidos | Bloqueante (execução) | Dado institucional (`database/dml/`), a confirmar com o DBA (ver Decisão #3) |
| `ObjectStorageClient` — método de upload | Bloqueante (execução) | Contrato novo sobre a mesma decisão de storage (DEC-013) |
| Provisionamento do Object Storage (DEC-013) | Bloqueante (execução) | Mesma pendência já registrada em `FT-DOCUMENTO`/`docs/governance/01-project-status.md` |

---

# Fontes

`specs/features/arquivos/specification.md` (Feature predecessora, § Fora do Escopo origina esta Feature); `database/ddl/002-create-sequences.sql`, `003-create-tables.sql` (schema físico — `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`, `CATEGORIA_DOCUMENTAL`), `008-initial-data.sql` (seed de `PAPEL`; seed de `CATEGORIA_DOCUMENTAL` referencia sequence inexistente); `docs/domain/09-business-rules.md` (BR-012, BR-015 a BR-024 — Gestão Documental); `docs/domain/10-open-questions.md` (OQ-004 — `Comunicado` categoria vs publicação, resolvido para publicação WordPress); `docs/technology/04-decision-log.md` (DEC-013); `database/README.md`/`GOVERNANCE.md` (DEC-DB-019 — banco DBA-administrado); `database/migrations/README.md` (padrão de migration brownfield, `V003`-`V008`); evidência de banco: `CATEGORIA_DOCUMENTAL` vazia no Oracle TST e sequences ausentes verificadas via JDBC (2026-08-27).

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — decompõe o "Fora do Escopo" de `FT-DOCUMENTO` em Feature própria (upload apenas, sem CRUD de pasta/documento), conforme decisões de produto do usuário nesta sessão |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review de Spec (`7d643eb`): `COD_CATEGORIA_DOCUMENTAL` derivado do `TIP_MIME` (`Documentos`/`Imagens`/`Vídeos`/`Outros`), sem parâmetro nem config; `DOCUMENTO_VERSAO.COD_COLABORADOR` explicitado; `Comunicado` deixa de ser categoria (é publicação WordPress — OQ-004); `CATEGORIA_DOCUMENTAL` vazia + `SQ_CAT_DOC_COD_CAT_DOC` ausente adicionadas às pendências de execução; erros `400`/`413` do multipart explicitados |
| 1.2 | 2026-08-27 | Claude Code | Ajuste após feedback: só `SQ_ARQUIVO_BINARIO`/`SQ_DOCUMENTO_VERSAO` são bloqueio (app não insere categoria); `SQ_CAT_DOC_COD_CAT_DOC` vira item de reconciliação greenfield; remoção do termo "Flyway" (DEC-DB-019); categorias criadas por `INSERT` de ID explícito no `V009` |
| 1.3 | 2026-08-27 | Claude Code | `V009` executado e validado — pendências de execução #1 (sequences) e #2 (categorias) resolvidas |
| 1.4 | 2026-08-27 | Claude Code (Specify) | § Fora do Escopo: gestão de pastas e de documentos após o upload passa a apontar para a Feature irmã `FT-DOCUMENTO-GESTAO` (`DRAFT`), em vez de "próxima fase" genérica. Sem mudança de escopo desta Feature. |
