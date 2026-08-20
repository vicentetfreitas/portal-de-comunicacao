# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Feature de renderização, não CRUD) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-HOME |
| Feature | Home |
| Domínio | HOME |
| Tipo | Resolução dinâmica de landing (backend) + renderização (frontend) |
| Status | DRAFT |

---

# Objetivo

Implementar o contrato de Home definido em `DEC-FA-004` (`docs/governance/03-open-decisions.md`): após o Contexto Ativo ser resolvido (`federationId`, `singularId`, `areaId`), o Portal solicita ao **backend** a Home apropriada para aquele contexto/usuário, e o frontend **apenas renderiza** o que recebe — sem regra fixa de landing no cliente.

Esta Feature substitui o placeholder `/app` (`docs/frontend/frontend-flow.md:82`) pela definição oficial. Sem esta Feature, `DEC-FA-004` permanece decidida mas não implementada.

**Fonte normativa:** `DEC-FA-004` (aprovada, 2026-07-24); `docs/frontend/frontend-flow.md` §"Home"; `docs/domain/10-open-questions.md` (OQ-028, encerrada).

**Fonte de evidência visual:** `AUDITORIA-DS-FIGMA-01.md` — frame `Home` (node `7:3`) mostra: bloco "Fique por dentro" com 3 cards de notícia (padrão `DsContentCard` + 2× `DsContentCardCompact`), chrome de shell (header/sidebar) já implementado via `AppShell`.

---

# Escopo

## Incluído

- Endpoint de backend que resolve a Home para o Contexto Ativo do usuário autenticado (ver "Decisão de produto pendente" abaixo para o contrato exato).
- Página frontend que solicita a Home ao backend após resolução do Contexto Ativo (`session.store.ts`) e renderiza o resultado.
- Estado de carregamento e de erro (Home indisponível) na renderização.
- Estrutura de conteúdo inicial compatível com o frame Figma `Home`: destaque(s) de notícia, sem hardcode de conteúdo no frontend.

## Fora do Escopo

- Modelo de dados/CMS de onde o conteúdo de notícia é originado — pertence a `FT-NOTICIA` (`specs/features/noticia/`). `FT-HOME` consome o que `FT-NOTICIA`/CMS expuser; não duplica esse contrato.
- Personalização avançada (recomendação, analytics) — não evidenciada em nenhum artefato consultado.
- Migração de qualquer Home legada (`docs/discovery/`) — não portar AS-IS.

---

# Atores

| Ator | Descrição |
|------|-----------|
| Usuário autenticado | Qualquer colaborador com sessão válida (FT-AUTH) e Contexto Ativo resolvido (FT-PRIMEIRO-ACESSO/FT-SESSION) |

---

# Requisitos Funcionais

## RF-HOME-001 — Resolver Home para o Contexto Ativo

| Campo | Valor |
|--------|--------|
| Identificador | RF-HOME-001 |
| Descrição | O backend deve expor uma operação que, dado o Contexto Ativo da sessão, retorna a Home apropriada (`DEC-FA-004`, item 2). |

## RF-HOME-002 — Renderizar Home recebida

| Campo | Valor |
|--------|--------|
| Identificador | RF-HOME-002 |
| Descrição | O frontend deve renderizar exatamente o conteúdo resolvido pelo backend, sem aplicar regra própria de landing (`DEC-FA-004`, item 3). |

## RF-HOME-003 — Tratar indisponibilidade da Home

| Campo | Valor |
|--------|--------|
| Identificador | RF-HOME-003 |
| Descrição | Se a resolução de Home falhar, o frontend deve exibir um estado de erro sem quebrar a navegação — comportamento exato é decisão de produto pendente (ver abaixo). |

---

# Decisão de produto pendente — contrato de resolução da Home

`DEC-FA-004` decide **quem** resolve a Home (backend) e **o que** o frontend faz com ela (renderiza) — não decide a **forma** do contrato. Esta especificação não inventa essa forma; questões em aberto:

1. **Formato da resposta:** lista de blocos de conteúdo tipados (ex. `{ type: "noticia-destaque", ... }`) vs. estrutura fixa de seções? Depende de quantos tipos de bloco a Home precisa suportar no MVP (o frame Figma mostra só notícias, mas isso pode ser só a primeira versão).
2. **Origem do conteúdo:** a Home consome `FT-NOTICIA` (que por sua vez depende do CMS, `DEC-CMS-001` — ver `specs/features/noticia/specification.md`) diretamente, ou o backend do Portal agrega e cacheia? Afeta latência e acoplamento.
3. **Granularidade por Contexto Ativo:** a Home varia por área/singular, ou é a mesma para todos os usuários autenticados no MVP? O Figma auditado (uma única tela) não distingue.
4. **Comportamento de erro (RF-HOME-003):** tela de erro dedicada, fallback para lista vazia, ou retry automático?

Nenhuma dessas quatro perguntas é decidível por evidência de código ou Figma — são decisões de produto/arquitetura que devem ser tomadas antes de `READY_FOR_REVIEW`.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| FT-SESSION / Contexto Ativo | Bloqueante | Home só é solicitada após Contexto Ativo resolvido |
| FT-NOTICIA | Bloqueante para conteúdo real | Home consome o mesmo conteúdo que `FT-NOTICIA` expõe; sem `FT-NOTICIA` implementada, Home só pode renderizar estado vazio |
| Design System (`ds/`) | Não bloqueante | `DsContentCard`, `DsContentCardCompact` já existem e estão `CONFORME` (`DS-RECONSTRUCTION-SCOPE-01` §59–60) |

---

# Fontes

`docs/governance/03-open-decisions.md` (DEC-FA-004); `docs/frontend/frontend-flow.md`; `docs/domain/10-open-questions.md` (OQ-028); `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`; `docs/architecture/decisions/DS-RECONSTRUCTION-SCOPE-01.md` §59–60.
