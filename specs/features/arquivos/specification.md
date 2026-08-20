# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — gestão de binários, não entidade de domínio tradicional) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO |
| Feature | Arquivos e Documentos |
| Domínio | DOCUMENTO |
| Tipo | Nova capacidade — sem Feature backend existente |
| Status | DRAFT |

---

# Objetivo

Permitir que o colaborador autenticado visualize, liste e baixe arquivos/documentos vinculados à sua Área — pastas com múltiplos arquivos, organizados por categoria (ex. "Logotipos", "Modelos de apresentações", "Papel timbrado", conforme frame Figma auditado).

**Diferença crítica em relação às demais Features deste lote:** esta é a **única** das 7 telas sem nenhuma Feature backend existente que a sustente, mesmo parcialmente. `docs/solution-design/06-integration-contracts.md:61` cataloga "Binários documentais" → "Armazenamento de Arquivos" como integração de infraestrutura **ATIVO**, mas isso é a existência de um *serviço de armazenamento*, não de uma *Feature de domínio* com modelo de dados, autorização e contrato de API.

**Fonte de evidência visual:** `AUDITORIA-DS-FIGMA-01.md` — frame `Areas - Arquivos e Downloads` (node `64:939`), mostra pastas ("PASTAS") com múltiplos itens de arquivo, cada um com título e formato (ex. "Formatos pptx", "Formatos doc e pdf").

---

# Escopo

## Incluído (proposto — depende de decisão de produto)

- Listagem de arquivos/pastas vinculados à Área do Contexto Ativo.
- Download de arquivo individual.
- Metadados mínimos por arquivo: título, formato.

## Fora do Escopo

- Upload, edição ou exclusão de arquivos — não evidenciado no Figma auditado (telas mostram só consumo/leitura).
- Controle de visibilidade/compartilhamento granular — mencionado como `BR-019` em `docs/domain/09-business-rules.md` (`OQ-011`, em aberto), não decidido para esta Feature especificamente.
- Herança de regras em hierarquia de pastas — `BR-017`/`OQ-012`, em aberto.
- Migração de arquivos do CMS/legado — fora do escopo desta reconstrução (`DS-RECONSTRUCTION-SCOPE-01` §3, "Discovery CMS: RETIRE").

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador autenticado | Leitura de arquivos vinculados à própria Área — regras de autorização não decididas (ver abaixo) |

---

# Requisitos Funcionais (propostos — dependem de decisão de produto)

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

---

# Decisão de produto/arquitetura pendente

Ao contrário das demais Features deste lote, aqui **nada é reaproveitável de backend existente** — isto é escopo novo por inteiro. Antes de `READY_FOR_REVIEW`:

1. **Modelo de dados:** entidade `DOCUMENTO`/`PASTA` não existe em `database/model/`. Precisa ser modelada (nome, formato, tamanho, área/escopo, hierarquia de pastas).
2. **Armazenamento físico:** onde os binários residem — object storage, filesystem, ou serviço já provisionado (`docs/solution-design/06-integration-contracts.md:61` confirma que a integração de infraestrutura existe, mas não diz qual serviço concreto)?
3. **Autorização:** quem pode ver quais arquivos — só a própria Área, ou compartilhamento entre áreas/singulares (`BR-019`, `BR-020`, `OQ-011`, `OQ-013`, todos em aberto em `docs/domain/10-open-questions.md`)?
4. **Escopo do MVP:** o Figma auditado mostra só leitura — confirmar que upload/gestão fica para uma Feature futura é decisão de produto, não inferência de ausência no Figma.

Esta é a Feature de maior risco/incerteza das 7 — as perguntas acima envolvem modelagem de dados nova, não apenas UI.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| Serviço de armazenamento de arquivos | Bloqueante | Existência confirmada como integração de infraestrutura; contrato de domínio não existe |
| FT-AREA-COLABORADOR | Consumidor | Hub de Área linka para esta Feature |
| Regras de compartilhamento (`BR-019`, `BR-020`) | Bloqueante parcial | Em aberto — `OQ-011`, `OQ-013` |

---

# Fontes

`docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`; `docs/solution-design/06-integration-contracts.md`; `docs/domain/09-business-rules.md` (BR-017, BR-019, BR-020); `docs/domain/10-open-questions.md` (OQ-011, OQ-012, OQ-013); `docs/architecture/decisions/DS-RECONSTRUCTION-SCOPE-01.md` §3.
