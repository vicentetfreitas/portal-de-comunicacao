# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Feature de consumo de conteúdo, não CRUD) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-NOTICIA |
| Feature | Notícia |
| Domínio | NOTICIA |
| Tipo | Consumo de conteúdo editorial (CMS) |
| Status | DRAFT |

---

# Objetivo

Permitir que o colaborador autenticado consuma conteúdo editorial ("notícias"/comunicação institucional) — feed resumido (usado pela Home, `FT-HOME`) e listagem/detalhe completos — com o conteúdo **originado no CMS**, não no backend do Portal.

**Fonte normativa:** `DEC-CMS-001` (aprovada) — "O CMS é exclusivamente provedor de conteúdo"; `docs/solution-design/06-integration-contracts.md:59` — integração `CMS WordPress → Backend API` classificada **"ATIVO — escopo a detalhar"**.

**Fonte de evidência visual:** `AUDITORIA-DS-FIGMA-01.md` — frame `Notícia` (node `64:48`, detalhe de um item) e bloco "Fique por dentro" no frame `Home` (3 cards). Componentes DS correspondentes: `DsContentCard` (card grande), `DsContentCardCompact` (cards menores), `DsSectionHeader`/título de seção — todos `CONFORME`, sem consumidor de produção (`DS-RECONSTRUCTION-SCOPE-01` §59–60).

---

# Escopo

## Incluído

- Listagem resumida de notícias (para consumo pela Home, `FT-HOME`).
- Listagem completa de notícias.
- Página de detalhe de uma notícia.
- Consumo do conteúdo via a integração CMS já catalogada em `docs/solution-design/06-integration-contracts.md` — esta Feature **detalha** o escopo contratual que esse documento já marca como pendente, não o redefine do zero.

## Fora do Escopo

- Autoria/edição de notícias — permanece no CMS (WordPress), fora do Portal, por `DEC-CMS-001`.
- Qualquer persistência de conteúdo editorial no banco do núcleo — proibido por `DEC-CMS-001` ("WordPress não acessa Banco de Dados do núcleo").
- Comentários, curtidas ou qualquer interação social — não evidenciado em nenhum artefato.
- Migração de conteúdo do CMS legado/Discovery — já decidido `RETIRE` (`DS-RECONSTRUCTION-SCOPE-01` §3).

---

# Atores

| Ator | Descrição |
|------|-----------|
| Usuário autenticado | Qualquer colaborador com sessão válida; leitura apenas |

---

# Requisitos Funcionais

## RF-NOTICIA-001 — Consultar notícias resumidas

| Campo | Valor |
|--------|--------|
| Identificador | RF-NOTICIA-001 |
| Descrição | O sistema deve expor um conjunto resumido de notícias (título, descrição curta, imagem) para consumo pela Home. |

## RF-NOTICIA-002 — Listar notícias

| Campo | Valor |
|--------|--------|
| Identificador | RF-NOTICIA-002 |
| Descrição | O sistema deve permitir listar todas as notícias disponíveis, paginadas. |

## RF-NOTICIA-003 — Consultar notícia por identificador

| Campo | Valor |
|--------|--------|
| Identificador | RF-NOTICIA-003 |
| Descrição | O sistema deve permitir consultar o conteúdo completo de uma notícia específica. |

---

# Decisão de produto/arquitetura pendente

`docs/solution-design/06-integration-contracts.md:59` já classifica esta integração como **"escopo a detalhar"** — esta especificação não fecha essa lacuna, apenas a delimita para o domínio Notícia:

1. **Protocolo de consumo:** o Backend do Portal consulta o CMS (WordPress) via API REST e repassa ao frontend (padrão já usado por outras integrações, `06-integration-contracts.md:72-78`), ou o frontend consome o CMS diretamente? A restrição "WordPress não acessa Banco de Dados do núcleo" não impede nenhuma das duas direções por si só.
2. **Modelo de dados exposto:** quais campos exatos (autor, data de publicação, categoria, tags) o CMS expõe e o Portal precisa? Não há schema documentado em nenhum artefato consultado.
3. **Autenticação/autorização da integração:** notícias são públicas a qualquer autenticado, ou existe segmentação por área/singular? Não evidenciado.
4. **Cache/performance:** conteúdo editorial muda com baixa frequência — decisão de cache (`06-integration-contracts.md` classifica a integração como prioridade "Baixa") impacta o design da API mas não está definida.

Nenhuma dessas perguntas é decidível por evidência de código ou Figma — dependem de definição do contrato real com o time/sistema de CMS.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| Integração CMS WordPress | Bloqueante | Contrato "a detalhar" — `FT-NOTICIA` não pode ser `APPROVED` sem essa definição |
| FT-HOME | Consumidor | Home consome o resumo produzido por `RF-NOTICIA-001` |
| Design System (`ds/`) | Não bloqueante | `DsContentCard`, `DsContentCardCompact`, `DsSectionHeader` já `CONFORME` |

---

# Fontes

`docs/governance/03-open-decisions.md` (DEC-CMS-001); `docs/solution-design/06-integration-contracts.md`; `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`; `docs/architecture/decisions/DS-RECONSTRUCTION-SCOPE-01.md` §3, §59–60.
