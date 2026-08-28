# Levantamento Factual — Inventários, Auditorias e Status de Decisões (DEC/GAP-DEC)

**Projeto:** Portal de Comunicação
**Data:** 2026-08-20
**Categoria documental:** Working (transitório — levantamento factual, não é SSOT)
**Responsável:** Claude Code (sessão de levantamento) — validação: Vicente Freitas
**Escopo:** somente leitura. Nenhum arquivo do projeto foi movido, renomeado ou alterado como parte deste levantamento (exceto a criação deste artefato).
**Objetivo:** consolidar, sem propor mudanças, (1) todos os documentos de inventário/auditoria de documentação já existentes no repositório, (2) um resumo do mais recente desse tipo, (3) o status de todos os `DEC-XXX`/`GAP-DEC-XXX` abertos ou pendentes, e (4) quais desses artefatos já ficaram desatualizados em relação ao estado atual do repositório.

---

## 1. Documentos de inventário/auditoria encontrados

Busca por nome (`inventory`, `inventário`, `audit`, `auditoria`, `structural-simplification`) e por `docs/governance/*` relacionado a análise da própria documentação. Resultados de código/testes (`SchemaOracleAuditTest`, `AuditableEntity.java`, `rxjs/audit.js` etc.) foram excluídos por não serem documentos de governança.

| Arquivo | Data | Escopo | Tipo |
|---|---|---|---|
| `docs/audit/09-campaign-traceability-audit.md` | 2026-06-22 | Investiga se "Campanha" é conceito de domínio válido | Auditoria pontual (conceito específico) |
| `docs/audit/10-mvp-consolidation-audit.md` | 2026-06-22 | Alinhamento tecnológico/arquitetural/escopo do MVP | Auditoria de consolidação (contém nota posterior de correção sobre Flyway/DEC-DB-019) |
| `docs/audit/11-final-readiness-audit.md` | 2026-06-22 | Prontidão para iniciar implementação, com base no `10-...` | Auditoria de prontidão |
| `docs/audit/12-structural-simplification-audit-w0-w1.md` | **2026-08-20 12:54** | Auditoria completa da documentação/estrutura do repositório (SSOT/Working/obsoleto, duplicação, complexidade) | **Auditoria de documentação** |
| `docs/governance/structural-simplification-plan-w2.md` | 2026-08-20 14:11 | Plano de decisões em resposta ao `12-...` (D1–D9) | Plano/decisão (não é auditoria de documentos em si) |
| `docs/audit/13-decision-inventory.md` | 2026-08-20 16:06 | Inventário de **decisões** arquiteturais (A/P/I/C/G/H) — não classifica documentos | Inventário de decisões (escopo diferente) |
| `docs/architecture/DS-RECONSTRUCTION-INVENTORY-01.md` | 2026-08-19 19:14 | Inventário de componentes do Design System frontend (`Ds*`) | Inventário de código/UI — fora do escopo de governança documental |
| `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md` | — | Auditoria de evidência visual Figma vs. frontend legado | Auditoria de design, não de documentação |

Não há outros artefatos em `docs/governance/` dedicados a auditar a própria documentação além do `12-...` e do `13-...` (que tem natureza diferente — ver Seção 4).

---

## 2. Resumo do inventário/auditoria mais recente com esse escopo — `docs/audit/12-structural-simplification-audit-w0-w1.md`

**Data:** 2026-08-20 · **Categoria:** Working (transitório) · **Escopo:** somente leitura/análise.

### Volume analisado

| Área | Arquivos | Papel declarado |
|---|---|---|
| `docs/` | 144 | Conhecimento permanente |
| `specs/` | 86 | Comportamento esperado |
| `construction/` | 245 | Execução histórica v4.1 (Archive) |
| `engineering/` | 23 | Sprint de integração pós-construction |
| `.cursor/` | 43 | Regras de agentes |
| `database/` | ~40 | SSOT de schema Oracle |

O documento não analisa arquivo a arquivo; agrupa em **15 grupos** (Seção 3, "Auditoria Documental") com uma classificação por letra (A, B, D, E, F, G, H, I, J). **Observação factual:** o documento **não contém uma legenda explícita** definindo essas letras — o significado só é inferível pelo texto ao lado de cada uma (ex. "A SSOT", "I Obsoleto", "F Histórico/Evidence", "G Duplicado", "J Sem função identificável").

| Grupo | Categoria (como grafada no documento) |
|---|---|
| 14 features ativas (`specs/features/`) | A/B — SSOT/Implementação |
| Foundation (13 docs, `specs/foundation/`) | A — SSOT |
| `docs/domain`, `docs/architecture` (núcleo), `docs/implementation`, `docs/technology` | A — SSOT |
| `docs/architecture/11-target-repository-structure.md` | I — Obsoleto (classificado como SSOT mas com conteúdo errado) |
| `docs/solution-design/` (11 docs) | E/G — Referência com sobreposição |
| `docs/audit/` (12 relatórios) | F — Histórico/Evidence |
| 3 catálogos de decisão (`governance/03`, `architecture/08`, `technology/04`) | G — Duplicado (reconhecido) |
| `construction/` inteiro | F — Histórico |
| `construction/review/` (24 relatórios ad-hoc) | F/H — Histórico/Intermediário |
| `engineering/integration/` | D/F — Operação/parcialmente histórico |
| `.cursor/archive/` | F — Histórico |
| Apêndices A/B/C de `agent-commands.md` | G/H — Duplicado/Intermediário dentro de doc ativo |
| `reports/project/stabilization-report.md` | J — Sem função identificável |
| `specs/domain/` (content-model etc.) | Decisão pendente (não classificável sem confirmação humana) |

### Principais problemas identificados

- **Duplicação/sobreposição:** três catálogos de decisão com colisão de ID reconhecida; `docs/solution-design/02-system-context.md` × `docs/architecture/01-system-context.md`; três lugares para arquitetura frontend (`docs/frontend/`, `docs/implementation/05-frontend-architecture.md`, `docs/construction/frontend/`); dois documentos disputando "SSOT do SSOT" (`minimal-ssot.md` × `07-documentation-architecture.md`).
- **Contradição:** `docs/architecture/11-target-repository-structure.md`, classificado como SSOT, descrevia Next.js/PostgreSQL/CMS — contradizia `docs/technology/01-technology-stack.md` (stack aprovada).
- **Decisões não formalizadas:** `docker-compose.yml` desalinhado (Postgres em vez de Oracle); ausência de CI backend; GitLab vs. GitHub sem decisão explícita; `specs/domain/` sem classificação confirmada.
- **Gaps estruturais:** "legado" (`construction/`) é só declarativo, sem separação física; Apêndices históricos misturados dentro de `agent-commands.md`, documento mais consultado no fluxo diário.

### Recomendações — implementadas vs. pendentes

| Recomendação (Seções 8/9/10 do audit-12) | Status observado neste levantamento |
|---|---|
| D1 — GitLab vs. GitHub | **Implementada/decidida** — GitHub mantido, registrado em `plan-w2` |
| D2 — Arquivar `docs/architecture/11-target-repository-structure.md` | **Implementada** — arquivo movido para `docs/governance/history/11-target-repository-structure.md` (confirmado via `git status`) |
| D3 — Confirmar/arquivar `specs/domain/` | **Implementada** — 9 arquivos movidos para `docs/governance/history/` (confirmado via `git status`, staged) |
| D4 — Storage de arquivos (provedor concreto) | **Aprovada posteriormente** — `DEC-013` em `docs/technology/04-decision-log.md` foi promovida a Approved (Object Storage S3-compatible) em ação separada, mesma data. `audit-12` e `plan-w2` ainda não refletem isso. |
| D5 — `docker-compose.yml` deveria usar Oracle, não Postgres | **Achado relevante:** o `docker-compose.yml` do working tree **já está usando `gvenzl/oracle-free:23-slim`**, não mais `postgres:16-alpine` (confirmado via `git diff`, mudança presente mas ainda não commitada). `audit-12` (12:54) e `plan-w2` (14:11) afirmam que o Postgres "ainda" está lá — isso não corresponde ao estado atual do arquivo. |
| D6 — Ausência de CI backend | **Parcialmente endereçada, não como previsto:** existe agora `.github/workflows/backend.yml` (untracked), como "Opção B" (exclui testes de integração dependentes de Oracle) — a auditoria original só constatou a ausência total, não previu essa alternativa. |
| D7 — Unificar catálogos de decisão | **Não implementada** — colisão de ID ainda existe. |
| D8 — Timing do arquivamento de `construction/` | **Não implementada** — gated por features em transição. |
| Extração de Apêndices A/B/C de `agent-commands.md` (Seção 10) | **Implementada** — commit `15fd1e8 docs(foundation): extrair apêndices históricos de agent-commands.md para construction/history`. |
| Seção 9 (arquitetura documental mínima hipotética) | **Não implementada** — o próprio documento é explícito: "hipotética — não implementada". |

---

## 3. `DEC-XXX` e `GAP-DEC-XXX` abertos/pendentes (todos os temas, não só storage)

### Catálogo `docs/governance/03-open-decisions.md`

| ID | Tema | Status |
|---|---|---|
| DEC-002 | Estratégia de observabilidade | **Aberta** — parcialmente endereçada (Correlation ID, Actuator); métricas/dashboards pendentes |
| DEC-003 | Estratégia de mensageria | **Aberta** |
| DEC-004 | Estratégia de deploy | **Aberta** |
| DEC-005 | Estratégia de versionamento | Aprovada (SemVer) |
| DEC-006 | Estratégia de testes automatizados | Aprovada |
| DEC-007 | Estratégia de banco de dados | Aprovada (Oracle) |
| DEC-ORG-001 | Hierarquia organizacional Federação→Singular→Área→Equipe→Colaborador | Aprovada (2026-07-24) |
| DEC-ORG-002 | CARGO como entidade de domínio independente | Aprovada (2026-08-14) |
| DEC-ORG-003 | Domínio de e-mail identifica Singular | Aprovada (2026-08-14) |
| DEC-DB-027 | Catálogo CARGO + `COD_CARGO NOT NULL` (modelo físico TO-BE) | Aprovada (2026-08-14), **parcialmente superseded** por DH-CARGO-01 (obrigatoriedade removida); implementação física ainda pendente |
| DEC-CMS-001 | Fronteira Portal × WordPress | Aprovada (2026-07-24) |
| DEC-FA-001..004, DH-02/03/04, DH-PA-01/02/03, DH-CARGO-01 | Pacote Primeiro Acesso/vínculo organizacional | Todas Aprovadas — implementação de código ainda pendente em vários itens (ex.: `locateOrCreate` no login ainda não ajustado a DH-03) |
| "DEC-008" (rótulo cancelado) | Multi-contexto (login/sessão) | **Cancelado** (2026-07-24) por violar fluxo OQ→DEC e por colisão de ID com `technology/04` DEC-008 — permanece como OQ-027/OQ-008 |

### Catálogo `docs/technology/04-decision-log.md`

| ID | Tema | Status |
|---|---|---|
| DEC-013 | Estratégia de armazenamento de documentos | **Approved** (2026-08-20) — Object Storage S3-compatible |
| DEC-014 | Estratégia de notificações | **Proposed** — ainda em aberto |

### Catálogo `docs/architecture/08-decision-records.md`

Pendências sem ID de DEC formal (registradas como "Decisões pendentes"):

| Tema | Origem |
|---|---|
| Descomissionamento da API Backend Legado | `02-container`, `07-deployment` |
| Unificação dos subsistemas de notificação | `02-container`, `04-integrations` |
| Estratégia de escalabilidade horizontal | `07-deployment` |
| Definição operacional de perfis externos | `06-security`, OQ-002 |
| Ownership de comunicado | `05-data`, OQ-004 |
| Resolução de endpoints órfãos Frontend ↔ API Backend | `04-integrations` |

Também: **ADR-015** (Coexistência da API Backend Legado) está em status **Provisória**, não Aceita.

### `GAP-DEC-XXX` (identificadores locais, não oficiais, cunhados em `docs/audit/13-decision-inventory.md`)

| ID | Tema | Status no momento deste levantamento |
|---|---|---|
| GAP-DEC-001 | Provedor concreto de storage | **Resolvido** — coberto por DEC-013 Approved. O próprio `13-decision-inventory.md` ainda mostra isso como aberto/P0 — está desatualizado. |
| GAP-DEC-002 | `docker-compose.yml` × Oracle | O arquivo já foi alterado no working tree para Oracle; se isso conta como "resolvido" ou apenas "em andamento (não commitado)" não foi determinado neste levantamento — é registro factual apenas. |
| GAP-DEC-003 | CI backend contra Oracle real ("Opção A" vs "B") | Aberto — "Opção B" está em uso, "Opção A" não decidida |
| GAP-DEC-004 | `docs/solution-design/11-platform-decomposition.md` não reclassificado | Aberto |
| GAP-DEC-005 | Tipo de `codigoUnimed` (PD-04) | Aberto |
| GAP-DEC-006 | Unificação dos catálogos de decisão | Aberto |
| GAP-DEC-007 | Timing do arquivamento físico de `construction/` | Aberto |
| GAP-DEC-008 | Nullability de `SINGULAR.NUM_REGISTRO_ANS` (RV-01) | Aberto |
| GAP-DEC-009 | Forma do contrato de FT-HOME | Aberto |
| GAP-DEC-010 | Escopo técnico da integração CMS em FT-NOTICIA | Aberto |
| GAP-DEC-011 | Exposição de `cargo`/`ramal` em `GET /auth/me` | Aberto |
| GAP-DEC-012 | FT-SERVICOS: lista estática vs. administrável | Aberto |
| GAP-DEC-013 | `.env` versionado com credenciais reais (segurança, fora do eixo arquitetural) | Aberto |

---

## 4. Mais de um inventário/auditoria — qual é o mais atual, e o que ficou desatualizado

Há **quatro** artefatos de auditoria/inventário no histórico recente, de naturezas diferentes:

- **09/10/11** (2026-06-22, Evidence/histórico) — auditorias pontuais e de prontidão do MVP, já concluídas; `10-mvp-consolidation-audit.md` já carrega uma nota de correção posterior sobre Flyway (indicando que o próprio projeto já sinalizou desatualização parcial nele).
- **12** (`structural-simplification-audit-w0-w1.md`, 2026-08-20 12:54) — **é a auditoria de documentação mais recente e mais abrangente**, mas **já está parcialmente desatualizada em relação ao estado atual do repositório**, especificamente:
  - afirma que `docker-compose.yml` "ainda sobe `postgres:16-alpine`" — não corresponde mais ao arquivo atual, que já referencia Oracle (mudança não commitada);
  - trata D4 (storage) como decisão em aberto — DEC-013 já foi aprovada.
- **`structural-simplification-plan-w2.md`** (14:11) — não é uma auditoria, é o plano de decisão que responde ao `12-...`; herda as mesmas duas desatualizações acima (D4 "Aberta", D5 "gap técnico" sem menção à correção já presente no arquivo).
- **`13-decision-inventory.md`** (16:06) — é o artefato cronologicamente mais recente, mas tem **escopo diferente**: inventaria *decisões* (A/P/I/C/G/H), não classifica *documentos* como SSOT/Working/obsoleto. Ficou parcialmente desatualizado assim que DEC-013 foi promovida a Approved (foi escrito antes dessa promoção, na mesma sessão) — ainda lista GAP-DEC-001/DEC-013 como "Proposed/P0/aberta".

Em resumo: para "auditoria de documentação" no sentido investigado, `docs/audit/12-structural-simplification-audit-w0-w1.md` é o documento certo e o mais recente do seu tipo — mas dois de seus achados factuais (storage e `docker-compose.yml`) já não refletem o estado atual do repositório, e o mesmo vale para o `structural-simplification-plan-w2.md`, que herda desses achados.

---

## Nota de governança

Este documento é um **levantamento factual**, não uma auditoria nova nem uma proposta de reconciliação. Nenhuma correção foi aplicada aos artefatos citados como desatualizados (`docs/audit/12-...`, `docs/governance/structural-simplification-plan-w2.md`, `docs/audit/13-decision-inventory.md`); nenhum SSOT, código, banco ou infraestrutura foi alterado como parte deste levantamento.
