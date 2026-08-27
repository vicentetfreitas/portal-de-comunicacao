# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — somente navegação/leitura; estende `FT-DOCUMENTO`) |
| Versão | 1.0 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-NAVEGACAO |
| Feature | Navegação de Pastas e Documentos |
| Domínio | DOCUMENTO |
| Tipo | Incremento de leitura sobre `FT-DOCUMENTO` (`DONE`, hoje lista plana) |

---

# Objetivo

Permitir que o colaborador **navegue pela hierarquia** de pastas e documentos da sua
Área (Contexto Ativo), em vez da lista plana atual de `FT-DOCUMENTO`. Dois modos
complementares:

1. **Explorador** — grade/lista de cards; clicar numa pasta entra nela e mostra o
   conteúdo daquele nível (subpastas + documentos); `breadcrumb` do caminho e volta.
2. **Árvore** — painel lateral com a hierarquia de pastas expansível; clicar num nó
   posiciona o explorador naquela pasta.

Mais busca por nome, alternância grade/lista e `deep-link` do estado de navegação
na URL.

**Referência visual:** `docs/figma/home/Captura de tela 2026-08-27 155612.png` — modelo
do app legado de produção: URL com `?folder_path=/federacao/marketing`, título = caminho
atual ("Marketing"), busca "Buscar documentos, pastas...", filtro, toggle grade/lista,
cards de pasta (ícone laranja + badge "PASTA" + rótulo + data de atualização).
Fluxo de entrada: Área → hub → botão **"Arquivos e Documentos"**
(`docs/figma/home/Captura de tela 2026-08-26 084844.png`).

**Discovery:** `docs/discovery/frontend-production-discovery.md` §4.5
(`CollaboratorAreaDocumentsPage`, `PublicAreaPage`, `MyFoldersPage` + `UserFolderTree`)
e §5.

**Por que uma Feature própria, não reabrir `FT-DOCUMENTO`:** `FT-DOCUMENTO` está `DONE`
(leitura plana entregue). A hierarquia é evolução de UX + extensão de contrato de API —
mesmo padrão de `FT-DOCUMENTO-UPLOAD` e `FT-DOCUMENTO-GESTAO` (Features irmãs de
`FT-DOCUMENTO`). `specs/foundation/feature-yaml.md` não define `DONE → DRAFT`.

---

# Escopo

## Incluído — a decidir no Review (proposta)

- **Explorador de pastas** — a partir das **raízes visíveis** (ver § Raízes), o
  colaborador entra numa pasta e vê: as subpastas ativas daquele nível + os documentos
  daquele nível (`STA_DOCUMENTO != EXPIRADO`, como `RF-DOCUMENTO-004`).
- **Breadcrumb / voltar** — caminho da pasta atual (nomes das pastas ancestrais
  **visíveis**); clicar num segmento navega para aquele nível; ação de voltar um nível.
- **Árvore de pastas** — painel com a hierarquia das pastas visíveis, expansível;
  seleção sincroniza com o explorador.
- **Busca** — por trecho do nome de pasta ou documento, **dentro do escopo visível**
  ao Contexto Ativo. MVP: busca no cliente sobre o conjunto já carregado.
- **Alternância grade / lista** — mesma informação, dois layouts.
- **Deep-link** — a pasta atual e o modo (explorador/árvore, grade/lista) refletidos
  na URL (query param), de modo que recarregar ou compartilhar o link reabra o mesmo
  ponto.
- **Contagem/indicadores no card** — nome, data de atualização (`DAT_ATUALIZACAO` da
  pasta), e opcionalmente contagem de itens.

## Fora do Escopo

- **Escrita** (criar subpasta, renomear, mover, arquivar) — é `FT-DOCUMENTO-GESTAO`.
  A navegação apenas consome; os menus de gestão de `FT-DOCUMENTO-GESTAO` continuam
  aparecendo para `ADMINISTRADOR` nos nós do explorador/árvore.
- **Upload / download / nova versão** — `FT-DOCUMENTO` / `FT-DOCUMENTO-UPLOAD` /
  `FT-DOCUMENTO-GESTAO`.
- **Navegar pastas de outras Áreas via Federação** — o app novo abre a navegação na
  **Área do Contexto Ativo** (`/app/area/arquivos`). O modelo legado navega
  `/app/federacao/:area/...`; reconciliar com `FT-FEDERACAO-COLABORADOR` é decisão de
  produto para uma iteração futura (ver § Decisão pendente).
- **Rótulo "Público" / "Privado"** como campo — não existe. A tela legada rotula pastas
  por `PERMISSAO_PASTA` (decisão de `FT-DOCUMENTO`, 2026-08-26: "público" = grant também
  em `FEDERACAO`/`SINGULAR`; "privado" = grant só no nível `AREA`). Se a UI exibir esse
  rótulo, ele é **derivado** dos grants, nunca um novo campo.
- **Filtro avançado** (por tipo de mídia, data, categoria) — o mock mostra um controle
  de filtro; o **conteúdo** do filtro fica para decisão de produto (ver § Decisão
  pendente). MVP pode entregar só o toggle grade/lista.
- **Grant individual por colaborador** e **herança viva de permissão** — fora, como em
  `FT-DOCUMENTO` (`OQ-012`).
- **Paginação de documentos dentro de uma pasta** — mantém a premissa de `FT-DOCUMENTO`
  ("volume baixo por pasta"); reavaliar se um nível tiver muitos itens.

---

# Raízes

O que aparece no nível inicial do explorador / no topo da árvore:

- Uma pasta visível cujo `COD_PASTA_PAI` é **nulo**; **ou**
- Uma pasta visível cujo `COD_PASTA_PAI` aponta para uma pasta **não visível** ao
  Contexto Ativo (raiz órfã) — para não esconder conteúdo acessível atrás de um
  ancestral sem grant.

O caminho (`breadcrumb`) para uma raiz órfã começa nela mesma (não expõe o ancestral
sem grant).

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador autenticado (qualquer papel) | Navega pela hierarquia de pastas/documentos visíveis à sua Área do Contexto Ativo (mesma visibilidade de `FT-DOCUMENTO`). |
| Colaborador com atribuição ativa `ADMINISTRADOR` | Além de navegar, vê os menus de gestão de `FT-DOCUMENTO-GESTAO` nos nós. |

---

# Requisitos Funcionais

## RF-DOC-NAV-001 — Explorar o conteúdo de uma pasta

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-NAV-001 |
| Descrição | O sistema deve permitir ao colaborador entrar numa pasta visível e listar, daquele nível: as subpastas ativas (`FLG_ATIVO='S'`) e os documentos (`STA_DOCUMENTO != 'EXPIRADO'`). O nível inicial mostra as raízes (§ Raízes). |
| Regra de Negócio | `BR-016` (organização em pasta), `BR-017` |

## RF-DOC-NAV-002 — Caminho e navegação entre níveis

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-NAV-002 |
| Descrição | O sistema deve exibir o caminho (`breadcrumb`) da pasta atual — composto pelos nomes das pastas ancestrais **visíveis** — e permitir navegar para qualquer segmento do caminho e voltar um nível. Para uma raiz órfã, o caminho começa nela. |

## RF-DOC-NAV-003 — Árvore de pastas

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-NAV-003 |
| Descrição | O sistema deve oferecer uma visualização em árvore da hierarquia de pastas **visíveis** ao Contexto Ativo, com nós expansíveis/recolhíveis. Selecionar um nó posiciona o explorador nessa pasta. |

## RF-DOC-NAV-004 — Buscar pastas e documentos

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-NAV-004 |
| Descrição | O sistema deve permitir buscar por trecho do nome de pasta ou documento e apresentar os resultados **restritos ao escopo visível** ao Contexto Ativo, indicando em que pasta cada resultado está. MVP: busca no cliente sobre o conjunto carregado; endpoint de busca dedicado fica para iteração futura se o volume exigir. |

## RF-DOC-NAV-005 — Alternar grade / lista

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-NAV-005 |
| Descrição | O sistema deve permitir alternar entre grade (cards) e lista para o conteúdo do nível atual, mantendo a mesma informação. A preferência é persistida por dispositivo (mesmo mecanismo de `DEC-FA-005` — tema). |

## RF-DOC-NAV-006 — Estado de navegação na URL

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-NAV-006 |
| Descrição | O sistema deve refletir na URL a pasta atual e o modo de visualização, de forma que recarregar a página ou compartilhar o link reabra o mesmo ponto de navegação. O identificador na URL não deve expor recursos fora do escopo do Contexto Ativo (um link para pasta sem grant → `403` na resolução, nunca conteúdo). |

## RF-DOC-NAV-007 — Restringir à visibilidade do Contexto Ativo (transversal)

| Campo | Valor |
|--------|--------|
| Identificador | RF-DOC-NAV-007 |
| Descrição | Toda pasta/documento apresentado (explorador, árvore, busca, breadcrumb) deve ter `PERMISSAO_PASTA` (`TIP_ACESSO` incluindo `LEITURA`) compatível com algum nível do Contexto Ativo do colaborador (`RF-DOCUMENTO-003`). Requisição direta a uma pasta sem grant compatível → `403` explícito (nunca `404` disfarçado nem filtragem silenciosa). |
| Regra de Negócio | `BR-012`, `BR-018`, `BR-020` |

---

# Contrato de API — lacuna a resolver

`GET /api/v1/pastas` hoje devolve `PageResponse<PastaResponse>` com **todas** as pastas
visíveis ao Contexto Ativo, em lista plana. `PastaResponse` expõe apenas `id`, `nome`,
`documentos[]` — **não** há `COD_PASTA_PAI` nem `DAT_ATUALIZACAO`, então o cliente não
consegue montar a hierarquia nem exibir a data do card.

Opções (ver `decisions.md` D-01):

| Opção | Descrição | Trade-off |
|-------|-----------|-----------|
| **(a)** Estender `PastaResponse` com `pastaPaiId` e `dataAtualizacao` | `GET /api/v1/pastas` continua devolvendo todas as visíveis; o cliente monta árvore e explorador. Aditivo e retrocompatível. | Simples; volume "baixo" (premissa de `FT-DOCUMENTO`) mantém-se aceitável para montar a árvore inteira de uma vez. |
| **(b)** Endpoint dedicado `GET /api/v1/pastas/arvore` | Devolve estrutura aninhada pronta. | Um endpoint a mais; duplica a lógica de visibilidade. |
| **(c)** Navegação lazy por nível — `GET /api/v1/pastas?pastaPaiId={id}` (ausente = raízes) | Carrega só o nível pedido. | Escala melhor; mais chamadas; a árvore precisa de N chamadas para expandir. |

**Proposta:** **(a)** para o MVP (menor superfície, retrocompatível). Se o volume por
Área crescer, evoluir para **(c)** numa iteração — sem quebrar a UI.

Esta Feature **não cria tabelas** e **não altera o modelo de dados** — só lê
`PASTA.COD_PASTA_PAI` e `PASTA.DAT_ATUALIZACAO`, colunas já existentes
(`database/ddl/003-create-tables.sql`).

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| `FT-DOCUMENTO` (`DONE`) | Pré-requisito | `GET /api/v1/pastas`, `PastaResponse`/`DocumentoResponse`, resolução de visibilidade multi-nível (`PermissaoPastaDomainService`) |
| `FT-DOCUMENTO-GESTAO` (`IMPLEMENTING`) | Consumidor recíproco | Os menus de gestão (criar subpasta, renomear, mover, arquivar) passam a viver nos nós do explorador/árvore em vez da lista plana |
| `FT-AREA-COLABORADOR` (`DONE`) | Consumidor | O hub da Área linka para "Arquivos e Documentos" (esta navegação) |
| `DEC-FA-005` (persistência de preferência) | Reuso | Preferência grade/lista persistida pelo mesmo mecanismo do tema |

---

# Decisão de produto/arquitetura pendente

Ver `decisions.md`. Resumo dos pontos que bloqueiam `READY_FOR_REVIEW`:

1. **Contrato de API** — opção (a), (b) ou (c). Proposta: (a).
2. **Filtro** — o mock mostra um controle de filtro. O que ele filtra (tipo de mídia?
   data? categoria?) é decisão de produto. Alternativa: cortar o filtro do MVP e
   entregar só busca + toggle grade/lista.
3. **Contexto de navegação** — só a Área do Contexto Ativo (`/app/area/arquivos`), ou
   também navegar pastas de outras Áreas a partir da Federação (modelo legado
   `/app/federacao/:area/...`)? Impacta `FT-FEDERACAO-COLABORADOR`.
4. **Busca** — client-side sobre o conjunto carregado (MVP) vs. endpoint de busca
   server-side (necessário se a opção (c) de API for escolhida, ou se o volume crescer).
5. **Rótulo "Público/Privado"** no card — exibir (derivado dos grants) ou omitir.
6. **Formato do identificador de pasta na URL** — `?pasta=<COD_PASTA>` (simples) vs.
   `?folder_path=/marketing/publico` (legível, como o legado; exige resolver caminho→id).

---

# Modelo de Dados

**Somente leitura.** Nenhuma tabela nova, nenhuma coluna nova. Usa de `PASTA`
(`003-create-tables.sql`) as colunas já existentes:

```text
PASTA
├── COD_PASTA        (id — já exposto)
├── COD_PASTA_PAI    (FK → PASTA, nullable — a expor: hierarquia)
├── NOM_PASTA        (nome — já exposto)
├── DSC_PASTA        (descrição — opcional a expor)
├── FLG_ATIVO        (só 'S' navegável)
└── DAT_ATUALIZACAO  (a expor: data do card)
```

Visibilidade: `PERMISSAO_PASTA` multi-nível (`FEDERACAO`/`SINGULAR`/`AREA`/`EQUIPE`),
`TIP_ACESSO` incluindo `LEITURA` — **exatamente** a regra de `FT-DOCUMENTO`
(`RF-DOCUMENTO-003`), sem alteração.

---

# Fontes

`specs/features/arquivos/` (`FT-DOCUMENTO` — leitura, `DONE`);
`specs/features/documento-gestao/` (escrita de pastas/documentos);
`specs/features/area-colaborador/` (hub que linka para Arquivos);
`docs/figma/home/Captura de tela 2026-08-27 155612.png` e `...2026-08-26 084844.png`;
`docs/discovery/frontend-production-discovery.md` §4.5, §5;
`database/ddl/003-create-tables.sql` (`PASTA.COD_PASTA_PAI`, `DAT_ATUALIZACAO`);
`docs/domain/09-business-rules.md` (BR-012, BR-016, BR-017, BR-018, BR-020);
`docs/technology/04-decision-log.md` (DEC-FA-005 — persistência de preferência).

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — DRAFT. Navegação hierárquica (explorador + árvore + busca + grade/lista + deep-link) como Feature irmã de `FT-DOCUMENTO`; RF-DOC-NAV-001..007; lacuna de contrato de API (`pastaPaiId`/`dataAtualizacao`) e 6 decisões pendentes registradas |
