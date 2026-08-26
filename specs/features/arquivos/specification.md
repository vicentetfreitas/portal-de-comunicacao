# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — gestão de binários, não entidade de domínio tradicional) |
| Versão | 1.1 |
| Status | READY_FOR_REVIEW |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO |
| Feature | Arquivos e Documentos |
| Domínio | DOCUMENTO |
| Tipo | Nova capacidade — sem Feature backend existente |
| Status | READY_FOR_REVIEW |

---

# Objetivo

Permitir que o colaborador autenticado visualize, liste e baixe arquivos/documentos vinculados à sua Área — pastas com múltiplos arquivos, organizados por categoria (ex. "Logotipos", "Modelos de apresentações", "Papel timbrado", conforme frame Figma auditado).

**Diferença crítica em relação às demais Features deste lote:** esta é a **única** das 7 telas sem nenhuma Feature backend existente que a sustente, mesmo parcialmente. `docs/solution-design/06-integration-contracts.md:61` cataloga "Binários documentais" → "Armazenamento de Arquivos" como integração de infraestrutura **ATIVO**, mas isso é a existência de um *serviço de armazenamento*, não de uma *Feature de domínio* com modelo de dados, autorização e contrato de API.

**Fonte de evidência visual:** `AUDITORIA-DS-FIGMA-01.md` — frame `Areas - Arquivos e Downloads` (node `64:939`), mostra pastas ("PASTAS") com múltiplos itens de arquivo, cada um com título e formato (ex. "Formatos pptx", "Formatos doc e pdf").

---

# Escopo

## Incluído — decidido (2026-08-26)

- Listagem de arquivos/pastas vinculados à Área do Contexto Ativo.
- Download de arquivo individual.
- Metadados mínimos por arquivo: título, formato.
- **Somente leitura** — decisão de produto confirmada: sem upload/edição/exclusão pelo colaborador nesta entrega.
- **Visibilidade por Permissão de Pasta multi-nível** — decisão de produto (2026-08-26, revisada após reconciliação com o schema físico real): pasta/documento visível ao colaborador quando existe um grant de acesso (`PERMISSAO_PASTA`) para `FEDERACAO`, `SINGULAR`, `AREA` ou `EQUIPE` correspondente ao nível do Contexto Ativo do colaborador. Ver § Modelo de Dados e RF-DOCUMENTO-003.
- **Somente documentos `ATIVO` ou `ARQUIVADO`** — `EXPIRADO` nunca aparece na listagem nem pode ser baixado (RF-DOCUMENTO-004).

## Fora do Escopo

- Upload, edição ou exclusão de arquivos pelo colaborador — decisão de produto (2026-08-26), não só ausência no Figma. Fica para Feature futura se priorizado.
- Grant individual por colaborador (`PERMISSAO_PASTA.TIP_DESTINATARIO = COLABORADOR`) — existe no schema físico, mas não decidido para esta entrega; fica para iteração futura.
- Herança de permissão entre pastas (`PASTA.FLG_HERDA_PERMISSAO`) — existe no schema físico; `BR-017`/`OQ-012` seguem em aberto no catálogo de domínio; esta Feature resolve apenas o caso concreto de "toda Área tem pasta(s) com grant direto", sem implementar herança de pasta pai para filha.
- Alteração de compartilhamento/visibilidade após publicação (`OQ-011`) — fora de escopo, sem tela administrativa nesta entrega.
- Migração de arquivos do CMS/legado — fora do escopo desta reconstrução (`DS-RECONSTRUCTION-SCOPE-01` §3, "Discovery CMS: RETIRE").

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador autenticado | Leitura de arquivos vinculados à própria Área do Contexto Ativo — sem acesso a arquivos de outras Áreas nesta entrega |

---

# Requisitos Funcionais

## RF-DOCUMENTO-001 — Listar pastas/arquivos da Área

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOCUMENTO-001 |
| Descrição | O sistema deve listar as pastas e arquivos vinculados à Área do Contexto Ativo. |

## RF-DOCUMENTO-002 — Baixar arquivo

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOCUMENTO-002 |
| Descrição | O sistema deve permitir o download de um arquivo específico. |

## RF-DOCUMENTO-003 — Restringir visibilidade por Permissão de Pasta

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOCUMENTO-003 |
| Descrição | O sistema deve retornar, na listagem e no download, apenas pastas/documentos para os quais existe `PERMISSAO_PASTA` com `TIP_DESTINATARIO` em (`FEDERACAO`, `SINGULAR`, `AREA`, `EQUIPE`) cujo `COD_DESTINATARIO` corresponda ao nível equivalente do Contexto Ativo do colaborador (`federationId`/`singularId`/`areaId`/`teamId`) e `TIP_ACESSO` incluindo `LEITURA` (listagem) ou `DOWNLOAD` (download). Requisição para recurso sem grant correspondente deve ser negada (403), nunca filtrada silenciosamente. |
| Regra de Negócio | `BR-012` (Contexto Ativo orienta escopo documental e autorização), `BR-018` (visibilidade público/privado conforme escopo), `BR-020` (compartilhamento define audiência) — `docs/domain/09-business-rules.md` |
| Decisão de produto (2026-08-26) | Toda Área possui pasta(s) com grant `TIP_DESTINATARIO='AREA'` visível a todos os colaboradores daquela Área — "pública" (documento também compartilhado com `FEDERACAO`/`SINGULAR`/`EQUIPE`, visível além da Área) e "privada" (grant só no nível `AREA`, restrito aos colaboradores daquela Área) são ambas cobertas pelo mesmo mecanismo de `PERMISSAO_PASTA`; a diferença é **quais níveis** têm grant, não um campo booleano separado. `TIP_DESTINATARIO=COLABORADOR` e herança (`FLG_HERDA_PERMISSAO`) ficam fora desta entrega (ver Escopo). |

## RF-DOCUMENTO-004 — Ocultar documentos expirados

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOCUMENTO-004 |
| Descrição | O sistema deve excluir da listagem e do download documentos com `STA_DOCUMENTO = 'EXPIRADO'`. Documentos `ATIVO` e `ARQUIVADO` permanecem visíveis. |

---

# Decisão de produto/arquitetura pendente

Ao contrário das demais Features deste lote, aqui **nada é reaproveitável de backend existente** — isto é escopo novo por inteiro.

1. **Modelo de dados** — **RECONCILIADO (2026-08-26)** com o schema físico já instalado (§ Modelo de Dados). A versão anterior desta spec propunha um modelo novo (`PASTA.COD_AREA` como FK direta) sem checar `database/ddl/` primeiro — erro de processo, corrigido nesta revisão. O modelo físico já existente é bem mais rico (permissão multi-nível, versionamento, ciclo de vida do documento) e é o que passa a valer.
2. **Armazenamento físico** — **RESOLVIDA (2026-08-20)**. `DEC-013` (`docs/technology/04-decision-log.md`, Approved) define **Object Storage S3-compatible** (ex. MinIO em desenvolvimento, provedor gerenciado em produção — trocável sem mudar contrato de código). Backend é o único consumidor (ADR-004, `docs/solution-design/06-integration-contracts.md:61`). O binário é referenciado via `ARQUIVO_BINARIO.URL_ARQUIVO` (coluna já existente no schema — não é uma chave de objeto nova). **Ainda pendente:** provisionamento concreto do storage no ambiente (não confundir decisão ↔ execução — não bloqueia a spec).
3. **Autorização** — **RESOLVIDA (2026-08-26), revisada:** visibilidade via `PERMISSAO_PASTA` multi-nível (`FEDERACAO`/`SINGULAR`/`AREA`/`EQUIPE`), não uma checagem simples de Área (ver RF-DOCUMENTO-003 e Escopo). Substitui a decisão anterior desta spec ("só própria Área via FK"), que não correspondia ao schema real.
4. **Escopo do MVP** — **RESOLVIDA (2026-08-26):** somente leitura (listar + baixar); upload/edição/exclusão fora desta entrega (ver Escopo). Complementada com filtro de status (RF-DOCUMENTO-004): documentos `EXPIRADO` nunca aparecem.

Todos os itens que bloqueavam `READY_FOR_REVIEW` estão endereçados (decididos ou explicitamente fora de escopo). Esta reconciliação ocorreu **após** a Feature já ter avançado a `IMPLEMENTING` — ver nota em `docs/governance/01-project-status.md` sobre reavaliação de DoR-Implementation antes de qualquer código.

---

# Modelo de Dados

**Físico, já instalado** — `database/ddl/003-create-tables.sql` (baseline DBA-administrado, DEC-DB-019 — `database/` é prioridade 1/schema truth por `specs/foundation/path-conventions.md`). Esta Feature **não cria tabelas novas**; consome o que já existe.

```text
PASTA
├── COD_PASTA (PK)
├── COD_PASTA_PAI (FK → PASTA, nullable — hierarquia; herança fora de escopo, ver Escopo)
├── NOM_PASTA, DSC_PASTA
├── FLG_HERDA_PERMISSAO (existe; não usado nesta entrega — fora de escopo)
└── FLG_ATIVO, DAT_CADASTRO, DAT_ATUALIZACAO

PERMISSAO_PASTA — mecanismo de visibilidade (substitui a antiga proposta de FK direta a Área)
├── COD_PERMISSAO_PASTA (PK)
├── COD_PASTA (FK → PASTA)
├── TIP_DESTINATARIO (CHECK: FEDERACAO | SINGULAR | AREA | EQUIPE | COLABORADOR)
├── COD_DESTINATARIO (id do destinatário, conforme TIP_DESTINATARIO)
└── TIP_ACESSO (CHECK: LEITURA | DOWNLOAD | EDICAO | ADMINISTRACAO)

DOCUMENTO
├── COD_DOCUMENTO (PK)
├── COD_PASTA (FK → PASTA)
├── COD_CATEGORIA_DOCUMENTAL (FK)
├── COD_COLABORADOR (FK — autor/publicador)
├── TIT_DOCUMENTO, DSC_DOCUMENTO
├── STA_DOCUMENTO (CHECK: ATIVO | ARQUIVADO | EXPIRADO — RF-DOCUMENTO-004)
└── DAT_PUBLICACAO, DAT_EXPIRACAO

DOCUMENTO_VERSAO — binário é sempre via versão, nunca coluna direta em DOCUMENTO
├── COD_DOCUMENTO_VERSAO (PK)
├── COD_DOCUMENTO (FK → DOCUMENTO)
├── COD_ARQUIVO_BINARIO (FK → ARQUIVO_BINARIO)
├── NUM_VERSAO
└── FLG_VERSAO_ATUAL (CHECK: S | N — esta Feature só consome a versão com FLG_VERSAO_ATUAL='S')

ARQUIVO_BINARIO
├── COD_ARQUIVO_BINARIO (PK)
├── NOM_ARQUIVO, TIP_MIME
├── URL_ARQUIVO (referência ao objeto no Object Storage, DEC-013 — nunca exposta diretamente ao cliente, ADR-004)
├── QTD_TAMANHO_BYTES
└── HASH_ARQUIVO
```

**Resolução de RF-DOCUMENTO-001 (listar):** `PASTA` com pelo menos um `PERMISSAO_PASTA` cujo `TIP_DESTINATARIO`/`COD_DESTINATARIO` corresponda a algum nível do Contexto Ativo do colaborador (`federationId`/`singularId`/`areaId`/`teamId`) e `TIP_ACESSO` inclua `LEITURA`; documentos dessa pasta com `STA_DOCUMENTO != 'EXPIRADO'`.

**Resolução de RF-DOCUMENTO-002 (baixar):** mesma checagem de `PERMISSAO_PASTA` (`TIP_ACESSO` incluindo `DOWNLOAD`) sobre a pasta do documento; binário obtido via `DOCUMENTO_VERSAO` com `FLG_VERSAO_ATUAL='S'` → `ARQUIVO_BINARIO.URL_ARQUIVO`.

**Não incluído nesta entrega** (existe no schema, decisão explícita de não usar agora — ver Escopo):

- `TIP_DESTINATARIO = COLABORADOR` (grant individual).
- `FLG_HERDA_PERMISSAO` / `COD_PASTA_PAI` (herança entre pastas) — `BR-017`/`OQ-012` seguem em aberto no catálogo de domínio.
- `TIP_ACESSO` de `EDICAO`/`ADMINISTRACAO` — irrelevantes nesta Feature (somente leitura).
- `COD_CATEGORIA_DOCUMENTAL`, `DAT_PUBLICACAO` como filtro — expostos como metadado se necessário, mas não usados em regra de acesso nesta entrega.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| Object Storage S3-compatible | Bloqueante (execução) | Decisão resolvida (`DEC-013`); provisionamento no ambiente ainda pendente; cliente S3/MinIO ainda não existe em `backend/pom.xml` |
| `PERMISSAO_PASTA`/`DOCUMENTO_VERSAO`/`ARQUIVO_BINARIO` (schema físico) | Bloqueante (execução) | Já instalados (baseline DBA); nenhuma migration nova necessária para esta Feature |
| FT-AREA-COLABORADOR | Consumidor | Hub de Área linka para esta Feature |
| Herança de permissão em pastas (`BR-017`, `OQ-012`) | Fora de escopo | Existe no schema (`FLG_HERDA_PERMISSAO`), mas esta Feature não implementa herança nesta entrega |
| Grant individual por colaborador (`TIP_DESTINATARIO=COLABORADOR`) | Fora de escopo | Existe no schema, decisão de produto (2026-08-26) de não usar nesta entrega |

---

# Fontes

`docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`; `docs/solution-design/06-integration-contracts.md`; `docs/domain/09-business-rules.md` (BR-012, BR-017, BR-018, BR-019, BR-020); `docs/domain/10-open-questions.md` (OQ-011, OQ-012, OQ-013); `docs/architecture/decisions/DS-RECONSTRUCTION-SCOPE-01.md` §3; `docs/technology/04-decision-log.md` (DEC-013); `database/ddl/003-create-tables.sql` (schema físico — PASTA, DOCUMENTO, DOCUMENTO_VERSAO, ARQUIVO_BINARIO, PERMISSAO_PASTA); `database/ddl/004-create-constraints.sql` (CK_DOCUMENTO_STATUS, CK_DOCUMENTO_VERSAO_ATUAL).
