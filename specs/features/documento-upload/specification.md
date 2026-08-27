# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — cria documento/versão/binário; sem CRUD de pasta) |
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
| Tipo | Incremento de escrita sobre `FT-DOCUMENTO` (DONE, somente leitura) |
| Status | DRAFT |

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

## Fora do Escopo — decidido (2026-08-27)

- **Gerenciar hierarquia de pastas** (criar, renomear, mover, excluir `PASTA`) — decisão de produto explícita do usuário: só upload de arquivo em pasta já existente nesta entrega. **Registrado para evoluir em próxima fase** quando priorizado.
- **Gerenciar o documento após o upload inicial**: sem editar metadados, sem enviar nova versão (`DOCUMENTO_VERSAO` adicional) de um documento existente, sem excluir/arquivar (`STA_DOCUMENTO`) — decisão de produto explícita do usuário. Registrado para evoluir em próxima fase.
- **Gerenciar `PERMISSAO_PASTA`** (criar/revogar grants) pela aplicação — fora de escopo; grants de `EDICAO` necessários para esta Feature são dado institucional (`database/dml/`), não uma tela da aplicação.
- **Papel `GESTOR_DOCUMENTAL`** — já existe em `PAPEL` (seed, `database/ddl/008-initial-data.sql`) e nominalmente parece feito para gestão documental, mas o usuário só autorizou `ADMINISTRADOR` escopado nesta entrega. Não estender a este papel sem nova decisão de produto.
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
| Descrição | O sistema deve permitir que um colaborador com atribuição ativa `ADMINISTRADOR` envie um arquivo para uma pasta existente, criando um novo `DOCUMENTO` (`STA_DOCUMENTO='ATIVO'`), sua `DOCUMENTO_VERSAO` inicial (`NUM_VERSAO=1`, `FLG_VERSAO_ATUAL='S'`) e o `ARQUIVO_BINARIO` correspondente no Object Storage (DEC-013). |

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

# Decisão de produto/arquitetura pendente

1. **Sequences Oracle ausentes — BLOQUEANTE DE EXECUÇÃO (não de spec).** `database/ddl/002-create-sequences.sql` só tem `SQ_DOCUMENTO_COD_DOCUMENTO` entre as tabelas desta Feature. `ARQUIVO_BINARIO` (`COD_ARQUIVO_BINARIO`) e `DOCUMENTO_VERSAO` (`COD_DOCUMENTO_VERSAO`) **não têm sequence** — nunca precisaram, porque `FT-DOCUMENTO` só lia essas tabelas. Banco é DBA-administrado (`DEC-DB-019`, `database/README.md`) — a aplicação não cria schema. **Ação necessária:** propor migration Flyway (`database/migrations/V009__...sql`, seguindo o padrão de `V003`-`V008` já existentes) criando as duas sequences, para o DBA revisar e executar. Sem isso, `TK-DOC-UPLOAD-001` (backend) não pode ser implementada.
2. **Dados institucionais — grants `EDICAO` ausentes.** Mesmo com o papel/mecanismo corretos, upload só funciona em pastas que já tenham `PERMISSAO_PASTA` com `TIP_ACESSO='EDICAO'` para o nível correspondente. Se o dado seed atual só tem `LEITURA`/`DOWNLOAD` (mesmo caso de `FT-DOCUMENTO`), isso é carga institucional (`database/dml/`) a confirmar/propor ao DBA — não uma tela desta Feature (grants continuam fora do escopo de escrita da aplicação, ver § Fora do Escopo).
3. **`ObjectStorageClient` (backend, `documento/application/port/ObjectStorageClient.java`) só tem `download(referenciaObjeto)`.** Precisa de um método de escrita (ex. `upload(referenciaObjeto, InputStream, tamanho, tipoMime)`) — contrato novo sobre o mesmo Object Storage S3-compatível já decidido (DEC-013), sem mudar de provedor.
4. **Tamanho/tipo de arquivo aceitos** — não definido pelo usuário nesta sessão. Sem limite explícito, backend deve pelo menos aplicar um teto operacional razoável (a definir em `tasks.md`/implementação) para não aceitar upload ilimitado — decisão técnica mínima de segurança, não uma feature de quota (`BR-023`, fora de escopo).

---

# Modelo de Dados

**Físico, já instalado** — mesmas tabelas de `specs/features/arquivos/specification.md` § Modelo de Dados (`PASTA`, `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`, `PERMISSAO_PASTA`), mais `PAPEL`/`PAPEL_ATRIBUICAO` (`accesscontrol`, já usados por `FT-SESSION`). Esta Feature **não cria tabelas novas** — apenas passa a fazer `INSERT` em três delas (`DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`), o que exige as sequences do item 1 acima.

```text
DOCUMENTO (INSERT novo)
├── COD_DOCUMENTO (PK — via nova sequence a confirmar: SQ_DOCUMENTO_COD_DOCUMENTO já existe)
├── COD_PASTA (FK → PASTA, pasta alvo do upload)
├── COD_CATEGORIA_DOCUMENTAL (FK — obrigatória; origem a definir em tasks.md: categoria padrão ou parâmetro do upload)
├── COD_COLABORADOR (FK — autor: o próprio colaborador que fez upload)
├── TIT_DOCUMENTO (do request)
└── STA_DOCUMENTO = 'ATIVO' (sempre, na criação)

DOCUMENTO_VERSAO (INSERT novo — requer sequence nova, ver Decisão #1)
├── COD_DOCUMENTO_VERSAO (PK)
├── COD_DOCUMENTO (FK → DOCUMENTO recém-criado)
├── COD_ARQUIVO_BINARIO (FK → ARQUIVO_BINARIO recém-criado)
├── NUM_VERSAO = 1
└── FLG_VERSAO_ATUAL = 'S'

ARQUIVO_BINARIO (INSERT novo — requer sequence nova, ver Decisão #1)
├── COD_ARQUIVO_BINARIO (PK)
├── NOM_ARQUIVO, TIP_MIME, QTD_TAMANHO_BYTES, HASH_ARQUIVO (derivados do arquivo enviado)
└── URL_ARQUIVO (referência ao objeto gravado no Object Storage — nunca exposta ao cliente, ADR-004)
```

**Resolução de RF-DOC-UPLOAD-002 (autorização):** ver § Modelo de Autorização.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| `FT-DOCUMENTO` (`DONE`) | Pré-requisito | Reaproveita entidades, `PermissaoPastaDomainService` (estender, não duplicar) e o padrão de `PastaController`/`DocumentoController` |
| Sequences `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_VERSAO` | Bloqueante (execução) | Não existem — propor migration Flyway para o DBA (ver Decisão #1). Aplicação não cria schema (`DEC-DB-019`) |
| Grants `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) para os níveis pretendidos | Bloqueante (execução) | Dado institucional (`database/dml/`), a confirmar com o DBA (ver Decisão #2) |
| `ObjectStorageClient` — método de upload | Bloqueante (execução) | Contrato novo sobre a mesma decisão de storage (DEC-013) |
| Provisionamento do Object Storage (DEC-013) | Bloqueante (execução) | Mesma pendência já registrada em `FT-DOCUMENTO`/`docs/governance/01-project-status.md` |

---

# Fontes

`specs/features/arquivos/specification.md` (Feature predecessora, § Fora do Escopo origina esta Feature); `database/ddl/002-create-sequences.sql`, `003-create-tables.sql`, `008-initial-data.sql` (schema físico e seed de `PAPEL`); `docs/domain/09-business-rules.md` (BR-012, BR-015 a BR-024 — Gestão Documental); `docs/technology/04-decision-log.md` (DEC-013); `database/README.md`/`GOVERNANCE.md` (DEC-DB-019 — banco DBA-administrado); `database/migrations/README.md` (padrão de migration brownfield, `V003`-`V008`).

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — decompõe o "Fora do Escopo" de `FT-DOCUMENTO` em Feature própria (upload apenas, sem CRUD de pasta/documento), conforme decisões de produto do usuário nesta sessão |
