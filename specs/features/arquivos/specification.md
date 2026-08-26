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
- **Visibilidade restrita à própria Área** — decisão de produto confirmada: sem compartilhamento entre áreas/singulares nesta entrega.

## Fora do Escopo

- Upload, edição ou exclusão de arquivos pelo colaborador — decisão de produto (2026-08-26), não só ausência no Figma. Fica para Feature futura se priorizado.
- Compartilhamento entre áreas/singulares — decisão de produto (2026-08-26): fora do escopo desta entrega. `BR-019`/`BR-020`/`OQ-011`/`OQ-013` permanecem em aberto no catálogo de domínio para uma eventual Feature futura de compartilhamento; esta Feature não os implementa.
- Herança de regras em hierarquia de pastas — `BR-017`/`OQ-012`, em aberto; fora de escopo (ver Modelo de Dados Proposto).
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

## RF-DOCUMENTO-003 — Restringir visibilidade à Área do Contexto Ativo

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOCUMENTO-003 |
| Descrição | O sistema deve retornar, na listagem e no download, apenas pastas/arquivos vinculados à Área do Contexto Ativo do colaborador. Requisição para arquivo de outra Área deve ser negada (403), nunca filtrada silenciosamente. |

---

# Decisão de produto/arquitetura pendente

Ao contrário das demais Features deste lote, aqui **nada é reaproveitável de backend existente** — isto é escopo novo por inteiro.

1. **Modelo de dados** — proposto abaixo (§ Modelo de Dados Proposto); ainda não revisado/aprovado tecnicamente (DBA/arquitetura), mas desbloqueado para avançar.
2. **Armazenamento físico** — **RESOLVIDA (2026-08-20)**. `DEC-013` (`docs/technology/04-decision-log.md`, Approved) define **Object Storage S3-compatible** (ex. MinIO em desenvolvimento, provedor gerenciado em produção — trocável sem mudar contrato de código). Backend é o único consumidor (ADR-004, `docs/solution-design/06-integration-contracts.md:61`). **Ainda pendente:** provisionamento concreto do storage no ambiente (não confundir decisão ↔ execução — não bloqueia a spec).
3. **Autorização** — **RESOLVIDA (2026-08-26):** visibilidade restrita à própria Área do Contexto Ativo, sem compartilhamento entre áreas/singulares nesta entrega (ver RF-DOCUMENTO-003 e Escopo).
4. **Escopo do MVP** — **RESOLVIDA (2026-08-26):** somente leitura (listar + baixar); upload/edição/exclusão fora desta entrega (ver Escopo).

Todos os quatro itens que bloqueavam `READY_FOR_REVIEW` estão endereçados (decididos ou explicitamente fora de escopo). Revisão técnica do modelo de dados (item 1) e Gate 1/DoR-Spec completo ficam para `/readiness`.

---

# Modelo de Dados Proposto (para revisão — não aprovado)

Proposta inicial, derivada dos RFs acima e do frame Figma auditado (pastas com múltiplos arquivos, título + formato por item). Sujeita a revisão de arquitetura/DBA antes de qualquer DDL — segue o precedente de `DEC-DB-027`/`DEC-013`: decisão de spec não autoriza schema físico.

```text
PASTA
├── COD_PASTA (PK)
├── NOM_PASTA
├── COD_AREA (FK → AREA — escopo de visibilidade; ver item 3 acima)
├── COD_PASTA_PAI (FK → PASTA, nullable — hierarquia; BR-017/OQ-012 em aberto, não modelar nesta etapa)
└── auditoria (criado_em, criado_por)

DOCUMENTO
├── COD_DOCUMENTO (PK)
├── NOM_DOCUMENTO (título exibido)
├── DSC_FORMATO (ex. "pptx", "doc e pdf" — Figma mostra formato como texto livre, não enum)
├── COD_PASTA (FK → PASTA)
├── CHV_OBJETO_STORAGE (referência ao objeto no Object Storage — DEC-013; nunca o binário no banco, ADR-004)
├── NUM_TAMANHO_BYTES
└── auditoria (criado_em, criado_por)
```

**Não incluído nesta proposta** (fora do escopo desta Feature ou decisão pendente):

- Hierarquia de pastas aninhadas — Figma auditado mostra só um nível; `COD_PASTA_PAI` fica reservado, sem uso até `BR-017`/`OQ-012` serem resolvidas.
- Compartilhamento/visibilidade granular além de `COD_AREA` — depende do item 3 (`BR-019`/`BR-020`).
- Versionamento de arquivo, quotas (`BR-023`, citada em `docs/solution-design/10-delivery-roadmap.md` Etapa 3) — não modelado aqui; avaliar quando a Feature avançar para `APPROVED`.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| Object Storage S3-compatible | Bloqueante (execução) | Decisão resolvida (`DEC-013`); provisionamento no ambiente ainda pendente |
| FT-AREA-COLABORADOR | Consumidor | Hub de Área linka para esta Feature |
| Regras de compartilhamento (`BR-019`, `BR-020`) | Fora de escopo | `OQ-011`/`OQ-013` seguem em aberto no catálogo de domínio, mas esta Feature não os implementa (decisão de produto 2026-08-26: só própria Área) |

---

# Fontes

`docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`; `docs/solution-design/06-integration-contracts.md`; `docs/domain/09-business-rules.md` (BR-017, BR-019, BR-020); `docs/domain/10-open-questions.md` (OQ-011, OQ-012, OQ-013); `docs/architecture/decisions/DS-RECONSTRUCTION-SCOPE-01.md` §3; `docs/technology/04-decision-log.md` (DEC-013).
