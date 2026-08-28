# AUDITORIA-DS-FIGMA-01 — Inventário de reconstrução (Figma × código)

| Campo | Valor |
|-------|--------|
| **ID** | AUDITORIA-DS-FIGMA-01 |
| **Status** | `READ_ONLY_INVENTORY` |
| **Data** | 2026-08-19 |
| **Categoria documental** | Auditoria (insumo de leitura; não é ADR aprovado nem Feature) |
| **Camada dona** | Frontend Foundation (não é Feature de negócio) |
| **Mantenedor** | Architecture / Frontend Foundation |
| **Precedente** | [`DS-RECONSTRUCTION-SCOPE-01`](./DS-RECONSTRUCTION-SCOPE-01.md) — executa o próximo passo definido em seu §17 |
| **Fonte Figma** | `WHDHRAMXXslmxOIzK2dbJG` ("Portal de Comunicação"), node `0:1`, acesso via Figma MCP (`whoami`, `get_metadata`, `get_libraries`, `get_variable_defs`) |

Este documento é o inventário de leitura solicitado no §17 do `DS-RECONSTRUCTION-SCOPE-01`. Não autoriza implementação, criação de `FT-DS`, alteração de tokens/componentes/Quasar/Figma, nem migração de Features. Classificações abaixo são **candidatas**, não decisões finais — as pendências do §15 do SCOPE-01 continuam abertas.

---

## Contexto de acesso ao Figma

- Conta autenticada confirmada via `whoami` (Vicente T Freitas).
- Arquivo acessível via `get_metadata`; página única ("Page 1"), sem página/frame dedicado a Design System, Foundations ou Components — apenas 8 telas de produto: Login, Home, Notícia, Areas, Areas - Equipe, Areas - Arquivos e Documentos, Perfil do Usuário, Serviços.
- `get_libraries` mostra 7 bibliotecas externas conectadas ao arquivo: "Monalisa Araújo's team library" (conteúdo não inspecionado), Material 3 Design Kit, Simple Design System, e kits Apple (iOS/iPadOS 26/27, macOS 26/27, watchOS 26, visionOS 26). Nenhuma evidência de tokens/estilos custom locais.
- `get_variable_defs` falhou: exige seleção ativa no Figma desktop, não suportado remotamente sobre um nodeId de arquivo. Tokens/variáveis do lado Figma permanecem **não verificados**, não "inexistentes".

## Átomos

| Item | Classificação candidata | Evidência Figma | Fato/inferência |
|---|---|---|---|
| `DsButton` | REBUILD | "Botão Login/entrar", botões de nav e ações — mas montados manualmente por tela, não como componente único | Fato: nomes "Botão X" recorrentes. Inferência: correspondem a `DsButton` |
| `DsIcon` | REBUILD | Instances `edit`, `gmail_groups`, `Icon Button`, `Bookmark`, `Home`, `list`, `download` | Fato: instâncias nomeadas recorrentes — candidato mais forte de todo o inventário |
| `DsAvatar` | REBUILD | Instance `person` + `Ellipse 6` no bloco "Olá, Monalisa!" | Fato: padrão repetido em 6 telas |
| `DsInput` | REBUILD | Campos "Usuário:"/"Senha:" no Login (rounded-rectangle + text) | Fato: elementos existem. Inferência: seriam `DsInput`; não são instância de componente no Figma |
| `DsBadge` | RECONSIDER — sem evidência | Nenhuma ocorrência identificada nas 8 telas | Fato: ausência observada, não prova ausência de uso futuro |
| `DsSelect` | RECONSIDER — sem evidência | Nenhum dropdown/select nas 8 telas | Idem acima |

## Moléculas

| Item | Classificação candidata | Evidência Figma | Fato/inferência |
|---|---|---|---|
| `DsNavItem` | RECONSIDER (fronteira DS×AppShell, §11) | "Botão Página inicial/Áreas/Minha Singular/Serviços" duplicados em 6 telas, cada um com ícone-instância | Fato: padrão idêntico repetido; não é componente único no Figma — mesma ambiguidade do código |
| `DsProfileSummary` | RECONSIDER (fronteira DS×AppShell, §11) | Bloco "Olá, Monalisa!" + avatar + "Editar perfil" duplicado em 6 telas | Fato: padrão idêntico repetido, não componentizado no Figma |
| `DsContentCard` | REBUILD | Cards "Bem-estar 2025 - Colaborativa" em Home/Notícia | Fato: padrão visual repetido (rounded-rectangle + texto + imagem) |
| `DsContentCardCompact` | REBUILD | Cards menores "Noticia 2/3" no Home | Inferência: variante compacta do mesmo padrão |
| `DsServiceCard` | REBUILD | Linhas "Service Desk/Zimbra/CapacitaCoop/Faculdade Unimed" na tela Serviços | Fato: 4 blocos com mesma estrutura (retângulo + ícone + texto) |
| `DsActionCard` | REBUILD | "Botão Equipe"/"Botão Arquivos e Documentos" na tela Areas | Fato: estrutura ícone+texto+fundo, repetida 2x |
| `DsPageHeader` | RECONSIDER | Títulos "Comunicação e Marketing", "Meu Perfil", "Serviços" | Inferência: título de seção, mas sem componente Figma correspondente para confirmar limite com `DsSectionHeader` |
| `DsSectionHeader` | RECONSIDER | "Fique por dentro" (Home/Notícia) | Mesma ambiguidade acima — não dá para diferenciar `PageHeader` de `SectionHeader` só pela metadata |
| `DsCard` (genérico) | RECONSIDER | Sobreposição conceitual com `DsContentCard`/`DsServiceCard` | Sem evidência específica isolada |
| `DsBreadcrumbs` | RETIRE candidato — sem evidência | Não observado em nenhuma das 8 telas | Fato: ausência nas telas auditadas |
| `DsDialog` | RETIRE candidato — sem evidência | Nenhum modal/overlay nas 8 telas (frames estáticos) | Idem — pode existir em estado não capturado (ex.: interação), não é conclusivo |
| `DsSearchInput` | RETIRE candidato — sem evidência | Não observado | Idem |

## Organismos

| Item | Classificação candidata | Evidência Figma | Fato/inferência |
|---|---|---|---|
| `DsFormCard` | RECONSIDER | Possível envoltório do bloco de Login (usuário+senha+botão) | Inferência fraca — metadata mostra elementos soltos, não um frame único nomeado "form" |
| `DsDataTable` | RETIRE candidato — sem evidência | Nenhuma tabela nas 8 telas | Fato: ausência observada |
| `ds-notify` | N/A (runtime, não visual estático) | Não aplicável a screenshot estático | — |

## Tokens

| Categoria (código) | Classificação SCOPE-01 §3 | Evidência Figma | Observação |
|---|---|---|---|
| `_palette.scss` | RECONSIDER | Verde recorrente ("Verde 1"), fundo de sidebar, ellipses decorativas — cores visíveis mas **valores não extraídos** (`get_variable_defs` bloqueado) | Não dá para comparar hex/paleta código vs Figma nesta auditoria |
| `_spacing.scss` | RECONSIDER | Inferível apenas por x/y/width/height brutos, não por tokens nomeados | Requer `get_design_context`/`get_variable_defs`, não executado |
| `_typography.scss` / `_text-styles.scss` | dentro de RECONSIDER | Textos presentes, fonte/peso não extraídos | Idem |
| `_radius.scss` | dentro de RECONSIDER | Uso extensivo de `rounded-rectangle` em cards, campos, botões — padrão visual consistente | Fato: padrão existe; valores não confirmados |
| `_shadows.scss` | dentro de RECONSIDER | Não observável via metadata (sem dado de efeito) | Sem evidência |
| `_breakpoints.scss` | dentro de RECONSIDER | Arquivo Figma só tem frames 1920×1080 (desktop) | **Lacuna**: nenhuma variante mobile/tablet para validar breakpoints do código |
| `_borders.scss` | dentro de RECONSIDER | Não observável via metadata (sem stroke data) | Sem evidência |
| `_layout.scss` (header 64px / footer 48px / sidebar 280px) | dentro de RECONSIDER; fronteira DS×AppShell | Topbar Figma ≈132px altura ("Rectangle 2"), footer ≈74px ("Rectangle 8"), sidebar ≈347px largura ("Rectangle 12") | **Discrepância factual**: dimensões do Figma não batem com as constantes atuais do código (64/48/280) — achado relevante para o inventário, não resolvido aqui |

## AppShell × DS × Feature (overlaps, §11/§10)

| Item código | Overlap Figma | Classificação candidata |
|---|---|---|
| `AppHeader.vue` | Topbar (Rectangle 2 + "Verde 1") duplicado em 7/8 telas | RECONSIDER — reforça achado do SCOPE-01 de fronteira indefinida |
| `AppSidebar.vue` + `DsNavItem` + `DsProfileSummary` | Bloco sidebar completo duplicado em 6/8 telas | RECONSIDER — mesmo ponto do §11, agora com evidência visual concreta |
| `AppFooter.vue` | Rodapé com copyright duplicado em 5/8 telas | RECONSIDER |
| `components/organization/equipe/*` (Feature) | Tela "Areas - Equipe" (Larissa Macedo, Monalisa Araújo) | PRESERVE como Feature — acoplado a domínio/dados, conforme princípio §10 |
| `pages/showcase.vue` | Sem equivalente no Figma (arquivo não tem catálogo) | RECONSIDER — decisão de ferramenta permanece pendente (§14/§15.7) |

## Principais lacunas do inventário

1. Nenhum valor de token (cor/tipografia/espaçamento) extraído — bloqueio de ferramenta, não de decisão.
2. Discrepância de dimensões de layout (header/footer/sidebar) entre código e Figma não explicada — pode ser drift do handoff ou tela desatualizada.
3. `DsBadge`, `DsSelect`, `DsBreadcrumbs`, `DsDialog`, `DsSearchInput`, `DsDataTable` sem evidência nas 8 telas — não confirma retirada, só ausência de uso nesta amostra.
4. Sem confirmação da origem das instâncias de ícone (biblioteca externa vs. local) — afeta se `DsIcon` deve mapear 1:1 ou exigir normalização.

Este inventário não decide as pendências §15.1–8 do `DS-RECONSTRUCTION-SCOPE-01`, não cria ADR, não altera código, tokens, Figma ou `construction/registry.yaml`.

---

## Nota de acompanhamento (2026-08-25)

O item 2 das lacunas acima (discrepância de dimensões de layout header/footer/sidebar) foi reconciliado com o baseline Figma (`docs/figma/home/`) em sessão de refinamento visual do App Shell: dimensões (`_layout.scss`), posicionamento horizontal do sidebar/conteúdo (`--layout-sidebar-inset`, `--layout-content-gutter`) e altura intrínseca do painel do sidebar foram corrigidos contra medições precisas do frame `Home`. Este inventário permanece como registro histórico do estado em 2026-08-19 — a documentação viva e atual dessas dimensões é o próprio `frontend/src/css/tokens/_layout.scss` (comentários citam as medições Figma usadas).

---

## Nota de acompanhamento (2026-08-25, 2ª rodada — comparação objetiva 1920×1080)

Segunda rodada de reconciliação visual, comparando `docs/figma/home/home.png`/`home.txt` (frame `Home`, node `7:3`) contra Chromium real em 1920×1080. Achados e correções:

1. **Alinhamento vertical heading↔sidebar** (rodada anterior): confirmado correto — `sidebarTop === headingTop === 216px`, ambos 87px abaixo do header, como no Figma.
2. **Scrollbar horizontal do sidebar — causa raiz real**: não era mais o bug de `mini`-rail (já eliminado na rodada anterior). Reproduzida com dado sintético: um nome/sigla de diretório sem espaços (ex.: acrônimo longo) não tinha `overflow-wrap`, forçando o painel ~105px além do rail (medido via `scrollWidth`/`clientWidth` em toda a árvore do drawer, não só no elemento raiz). Corrigido em `SidebarDirectorySection.vue` (`min-width: 0` na cadeia flex + `overflow-wrap`/`word-break` nos nós de texto `__item-name`/`__item-subtitle`). Validado com `scrollWidth === clientWidth` em todos os estados (padrão, expandido, texto longo, mobile) — guarda de regressão em `test/e2e/app-shell/app-shell.spec.ts`.
3. **Header — composição**: Figma mostra apenas a marca Unimed Ceará, alinhada à direita; o código tem busca/Sair/avatar sem evidência no Figma (gap de produto já registrado, mantido — ver `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md` linha 88 e memória do agente). Reposicionado o wordmark textual para a direita (ao lado de busca/Sair/avatar), mais fiel à composição do Figma. **Gap arquitetural**: não existe asset real da marca Unimed no repositório (`frontend/src/assets/` só tem o placeholder padrão do Quasar) e o download via Figma MCP não pôde ser feito nesta sessão (cota do plano Starter esgotada) — o wordmark textual permanece até haver o asset real.
4. **"Editar perfil"**: estava totalmente oculto (`show-edit="false"` em `AppShell.vue`). Restaurado como visualmente preparado (sem rota, `FT-PERFIL` segue inexistente) e re-estilizado para a tonalidade/itálico do Figma (`#585C65`, 10px, itálico — antes usava o estilo `link` padrão do DS, verde/sublinhado).
5. **Navegação**: ordem confirmada — Página inicial → Federação → Singular → Serviços → Áreas. Rótulo "Serviços" (não "Sistemas e Serviços", conforme texto literal do Figma). Verificado no roteador real (`router/routes/`) que não há outra página/rota funcional do colaborador fora do menu — `Área → Equipe` já está corretamente aninhada (breadcrumb), não é item órfão.

**Gaps mantidos (decisão de produto/feature, não CSS)**: busca/Sair/avatar no header; "Minha Singular" self-service (fora do escopo de `FT-SINGULAR`); "Serviços" (`FT-SERVICOS` DRAFT); cards de notícia mockados (`FT-NOTICIA` inexistente) — todos já registrados nas memórias do agente desta feature, sem mudança de status nesta rodada.

---

## Nota de acompanhamento (2026-08-26, refinamento da toolbar)

Terceira rodada de refinamento visual, focada na composição da toolbar (`AppHeader.vue`) — decisões de produto explícitas do usuário, não achados de reconciliação Figma (o Figma continua mostrando apenas a marca Unimed, conforme nota anterior):

1. **Logo movida para a esquerda** (primeiro elemento da toolbar), substituindo o posicionamento à direita da rodada anterior.
2. **Menu hambúrguer**: oculto ≥960px (mesmo breakpoint da sidebar) — a sidebar não tem mais controle de recolhimento no desktop, permanece sempre aberta ali; segue como único mecanismo de abrir/fechar a sidebar no mobile (overlay). Corrigidos/reescritos os testes E2E que assumiam recolhimento no desktop (`app-shell.spec.ts`).
3. **Avatar removido da toolbar** (o usuário se referia a ele como "editar perfil"; não havia botão de editar perfil separado ali — o "Editar perfil" real só existe na sidebar, inalterado). Identidade do usuário passa a aparecer só na sidebar.
4. **Busca**: virou botão-lupa; ao clicar revela o `DsSearchInput` com botão de fechar, em vez de ficar sempre visível.
5. Tema/Sair mantidos, mesma ordem.

**Gaps mantidos**: `DsSearchInput` segue não-funcional (sem submit handler) — mudança de UX, não de comportamento.

## Nota de acompanhamento (2026-08-26, 2ª rodada — alinhamento shell + diretórios)

Quarta rodada, disparada por duas capturas de tela do usuário mostrando desalinhamentos reais entre header/sidebar/cards a 1920×1080. Achados e correções:

1. **Logo × sidebar**: `AppHeader.vue`'s toolbar usava `padding: 0 var(--spacing-md)` nos dois lados — a logo (primeiro elemento, ver nota acima) ficava ~60–100px à esquerda do painel da sidebar. Padding-left passou a usar `var(--layout-sidebar-inset)`, o mesmo token que posiciona o painel da sidebar.
2. **Sidebar — indentação inconsistente**: `SidebarDirectorySection.vue` (blocos "Federação"/"Singular") não herdava o `padding: 0 var(--spacing-sm)` que `SidebarSection.vue` aplica aos itens de navegação comuns — ícones ficavam 8px à esquerda dos demais (medido via Playwright: 132px vs 140px a 1920px). Corrigido adicionando o mesmo padding ao componente.
3. **Sidebar — espaçamento vertical zero entre blocos**: `SidebarMenu`/`SidebarDirectorySection` são elementos irmãos soltos dentro do `q-scroll-area` de `AppSidebar.vue`, sem `gap` entre si (0px medido). Envolvidos em um novo wrapper `.app-sidebar__menu` (`display:flex; flex-direction:column; gap: var(--spacing-md)`), aumentando o espaçamento entre "Página inicial"/"Federação"/"Singular"/"Serviços"/"Áreas".
4. **Título "Fique por dentro" não aparentava negrito apesar de `font-weight:800` no CSS**: causa raiz em `src/css/fonts.scss` — a família "Unimed Sans" declarava um único `@font-face` com `font-weight: 300 900` (faixa) apontando para o arquivo estático `UnimedSans-Regular.otf`. Uma faixa de peso só funciona com fonte variável; num arquivo estático, todo peso pedido dentro da faixa (300–900) resolvia para o mesmo arquivo Regular, sem erro e sem síntese do navegador — `getComputedStyle` acusava "800" mas o glifo renderizado era Regular. Corrigido registrando um `@font-face` por peso/estilo real (Light/Regular/SemiBold/Bold/ExtraBold/Black × normal/italic), usando os arquivos já presentes localmente em `frontend/fonts/`. `public/fonts/README.md` atualizado com a tabela completa e a explicação do bug, para não reintroduzi-lo.
5. **Cards da Home mais curtos que a sidebar** (bordas inferiores desalinhadas — até ~122px de diferença medido via Playwright a 1920×1080): a sidebar estica para preencher a altura disponível do viewport (`AppSidebar.vue`, `max-height` formula); os cards da Home tinham alturas fixas/limitadas por `clamp()` medidos do Figma (frame estático, sem essa dinâmica). Decisão explícita do usuário: aumentar os cards se necessário para alinhar. `pages/app/index.vue` passou a reservar o mesmo orçamento vertical disponível (`.app-page`, fórmula espelhada da altura de `.app-shell__page`) e o card primário cresce para preenchê-lo (`flex:1` + `align-items:stretch`, herdado de `.ds-content-card`), convertendo o `max-height` da imagem primária em `min-height`. **Escopo deliberadamente limitado a viewports com altura ≥1000px** (`@media (min-width:960px) and (min-height:1000px)`) — nas alturas menores já testadas e endurecidas contra scroll (900/768/720, ver comentário do próprio arquivo sobre `-webkit-line-clamp`), o novo orçamento fica menor que o conteúdo natural dos cards (não-operante) e o alinhamento continua parcial ali (gap residual de 22–138px, inalterado desde antes desta rodada) — trade-off deliberado para não arriscar reintroduzir scroll numa faixa de viewport já validada.
6. **Federação — área do colaborador em destaque**: `useFederationAreaDirectory` (`useOrganizationDirectory.ts`) agora marca `isOwnArea` comparando cada área com `activeContext.areaId` (FT-SESSION) e ordena essa entrada para o topo da lista (sort estável, resto mantém ordem por nome vinda da API). `SidebarDirectorySection.vue` renderiza um badge "Minha área" (`DsBadge`) e destaque de fundo no item.
7. **Singular — exclusão de "Unimed Ceará"**: decisão explícita do usuário — "Unimed Ceará" representa a própria federação, não uma Singular real; algumas federações carregam uma linha de Singular auto-referencial para ela. `useSingularDirectory` passou a filtrar por nome (`unimed ceará`, case-insensitive) antes de expor a lista. Filtro de produto no frontend, não uma correção de dado no backend.
8. **Bug lateral corrigido em `DsBadge`**: ao usar `label` sem slot customizado, o texto renderizava duplicado ("Minha áreaMinha área") — `DsBadge.vue` tinha `<slot>{{ label }}</slot>` dentro de `<q-badge :label="label">`; o `QBadge` do Quasar mescla (`hMergeSlot`) o conteúdo do slot com o texto do `label` prop sempre que um slot é passado, mesmo vazio — bug pré-existente, não introduzido nesta rodada, mas bloqueava o badge "Minha área" acima. Corrigido removendo o fallback de texto do slot (`<slot />`), deixando o `label` prop ser a única fonte do texto quando nenhum slot customizado é fornecido.

**Validação**: suíte completa `test/e2e/app-shell/app-shell.spec.ts` (14 testes, incluindo 2 novos cobrindo os itens 6/7 acima) + suíte unitária (183 testes) verdes; sem overflow horizontal/vertical introduzido em nenhum dos viewports testados (1920×1080, 1600×900, 1440×900, 1366×768, 1280×720, 390×844 mobile); checado visualmente em tema claro e escuro.

## Nota de acompanhamento (2026-08-26, 3ª rodada — menu, destaque, descrição, botão de voltar)

Quinta rodada, ajustes pontuais pedidos pelo usuário sobre a rodada anterior, mais uma feature nova de navegação:

1. **Ordem do menu**: era Página inicial, Federação, Singular, Serviços, Áreas — passou a Página inicial, Federação, Singular, **Áreas, Serviços** (`AppSidebar.vue`). O bloco "Áreas" (`trailingItems`) precisou de um `v-else-if` irmão do `<template v-if="showOrganizationDirectory">` para continuar renderizando no shell admin (que não mostra Federação/Singular/Serviços) — sem isso, mover `trailingItems` para dentro do bloco condicional faria "Áreas" sumir da sidebar admin.
2. **Federação — destaque simplificado**: supersede o item 6 da nota anterior. O badge "Minha área" (`DsBadge`) e o subtítulo (nome da Singular) foram removidos dos itens de Federação — decisão explícita do usuário ("deixe apenas o nome das áreas"), já que o subtítulo podia literalmente mostrar "Unimed Ceará" (ver item 7 da nota anterior). O destaque da área vinculada ao colaborador agora é só `font-weight: bold` no nome, mantendo o fundo destacado e a ordenação para o topo já existentes. `own-area-badge-label`/i18n `ownAreaBadge` removidos por não terem mais consumidor.
3. **Descrição das notícias pequena demais**: `.app-page__news-secondary-card :deep(.ds-content-card__description)` usava `--text-body-small-size` (14px, medida exata do Figma) — trocado por `--text-body-size` (16px, mesma escala do card primário), floor subido de 12px para 14px. Decisão explícita do usuário de se afastar da medida literal do Figma por legibilidade.
4. **Botão de voltar**: `useLayoutMeta.ts` ganhou `showBackButton`, derivado de `breadcrumbs.length > 1` — reaproveita o array de breadcrumbs já existente por rota (`router/routes/**`) em vez de um novo campo `backTo` paralelo que pudesse divergir dele. `AppShell.vue` passa `show-back-button`/`@back="router.back()"` para `AppHeader.vue` (que já tinha esse mecanismo pronto desde a fundação do componente, nunca conectado). Navegação por histórico do navegador (`router.back()`), não um alvo fixo por rota — evita a ambiguidade de decidir "hub ou listagem" por página. Aparece em toda página com breadcrumbs (inclusive hubs — eles também têm histórico real de navegação, não só páginas "filhas").
5. **FT-COLABORADOR — página de Edição implementada**: `ColaboradorEditPage.vue` era um stub-placeholder; ver `specs/features/colaborador/tasks.md` (seção "Progresso") para o detalhe completo, incluindo um bug de hidratação real corrigido em `useColaboradorOrganizationalOptions`.

**Validação**: suíte `app-shell.spec.ts` (16 testes) + `test/e2e/colaborador/colaborador-edit.spec.ts` (novo) + suíte unitária (183 testes) verdes; typecheck limpo.

## Nota de acompanhamento (2026-08-26, 4ª rodada — Federação navegável)

Sexta rodada. Usuário trouxe três capturas de tela de `portaldecomunicacao.unimedceara.com.br` (site externo, live — não o Figma auditado neste documento) como referência para "Federação > Área > Equipe/Arquivos". Antes de implementar, a comparação com specs já `APPROVED`/`DONE` revelou conflitos reais que foram levados ao usuário via pergunta explícita em vez de decididos sozinho:

1. **Áreas/Singulares da Federação tornam-se navegáveis** — reverte a decisão anterior ("lista somente-leitura, confirmada por decisão explícita") registrada em memória de sessões passadas. Nova Feature `FT-FEDERACAO-COLABORADOR` (`specs/features/federacao-colaborador/`, `DONE`, escrita retroativamente) documenta o quê/porquê — ver esse arquivo para o detalhe completo (RFs, decisões de produto, dependências).
2. **Roster de pessoas por Área**: a 2ª captura mostra nome+e-mail individuais e um bloco "Contato setorial". `FT-AREA-COLABORADOR` já tinha decidido (2026-08-20) excluir exatamente isso por falta de dado em `FT-EQUIPE`. Investigação mais a fundo achou que `FT-COLABORADOR` (`GET /api/v1/colaboradores?areaId=`, já `APPROVED`) **devolve** `name`/`email` reais por pessoa — só não `cargo`/`telefone` (não existem em nenhum contrato) nem "Contato setorial" (não existe em `AREA`). Usuário confirmou querer o roster; construído só com os campos reais, sem fabricar os demais.
3. **"Arquivos e Documentos"**: `FT-DOCUMENTO` segue `DRAFT` — usuário confirmou manter inerte (mesmo padrão de "Serviços"), não construir a tela de pastas da 3ª captura.
4. **Renomeação de menu**: "Singular" → "Singulares", "Serviços" → "Sistemas e Serviços" — pedido explícito do usuário, substitui a leitura literal do Figma usada anteriormente para "Serviços".
5. **Breadcrumbs dinâmicos**: `useLayoutMeta.ts`'s `resolveBreadcrumbTo` — meta de breadcrumb pode usar `:param` (mesma sintaxe de `ROUTE_PATHS`) resolvido contra os params da rota atual, para o botão de voltar (rodada anterior) funcionar corretamente nessas novas páginas dinâmicas. Generalizável a qualquer rota futura, não específico desta rodada.

**Gap que permanece aberto, sinalizado ao usuário, não decidido por mim**: esta Feature foi construída via decisão interativa nesta sessão, não por um Specify/Readiness formal prévio — a spec (`specs/features/federacao-colaborador/`) foi escrita **depois** do código, para documentar o que foi decidido. Fluxo correto daqui em diante para novas Features: Specify → Readiness (DoR) → Implement.

**Validação**: `test/e2e/federacao/federacao.spec.ts` (2 cenários, novo) + suíte completa (183 unitários + 40 E2E, incluindo todas as rodadas anteriores) verdes; typecheck limpo.

## Nota de acompanhamento (2026-08-26, 5ª rodada — Área/Equipe visual, botão de voltar reposicionado)

Sétima rodada. Ajustes visuais sobre `FT-FEDERACAO-COLABORADOR` (rodada anterior) + reposicionamento do botão de voltar + "Editar perfil" funcional. Sem acesso ao Figma nesta rodada — cota do MCP do plano Starter esgotada de novo (mesma limitação já registrada em notas anteriores) — decisões de estilo seguiram o pedido explícito do usuário e os padrões já existentes no projeto, não uma comparação pixel-a-pixel contra o frame "Areas".

1. **Botões "Equipe"/"Arquivos e Documentos"**: de `DsActionCard` (card grande) para `DsButton`+`DsIcon` inline — "formato de botão com ícones", pedido explícito.
2. **Equipe — campos mockados**: cargo, telefone(s), ramal(is) e "Contato setorial" (área) adicionados como **mock explícito e autorizado** ("pode mocar os dados por enquanto") — `name`/`email` continuam reais (`FT-COLABORADOR`). Ver `specs/features/federacao-colaborador/` para o detalhe completo (campos sem fonte real hoje, provável necessidade de mudança de contrato de backend).
3. **Lista da Equipe sem card/borda**: trocada de `DsDataTable` (com `bordered`) para uma lista simples (`<ul>`/`<li>`, separador fino entre linhas) — usuário observou que o projeto não usa cards-com-borda para itens internos em outras páginas.
4. **Botão de voltar reposicionado**: removido de `AppHeader.vue` (perto da logo) — a rodada anterior o colocou ali. Agora vive dentro de `DsPageHeader.vue` (novo `show-back`/`@back`, genérico, DS-level), ao lado do título de cada página, opt-in por página (não mais automático via breadcrumbs em todo o app). Aplicado às páginas de Área/Equipe/Singular da Federação e à nova `/app/perfil`. **Trade-off**: páginas fora dessas (Colaborador, Singular/Equipe administrativos) perderam o botão de voltar — não foi pedido para elas.
5. **"Editar perfil" funcional**: navega para `/app/perfil` (nova página, visualização de nome/e-mail da sessão já carregada — `FT-PERFIL` continua `DRAFT`, sem formulário de edição real).

**Validação**: suíte completa (183 unitários + 40 E2E) verde; typecheck limpo.

## Nota de acompanhamento (2026-08-26, 6ª rodada — menu, grids responsivos, perfil editável)

Oitava rodada.

1. **"Áreas" removido do menu**: `MAIN_NAV_ITEMS` (`constants/navigation.ts`) não tem mais a entrada `areaColaborador.hub.title` (cascata: some tanto do shell colaborador quanto do admin, que estende `MAIN_NAV_ITEMS`) — pedido explícito do usuário. `FT-AREA-COLABORADOR` continua `DONE`; a rota/páginas (`/app/area`, `/app/area/equipe`) não foram tocadas, só ficaram sem link na sidebar — a Federação (rodada anterior) já cobre o mesmo caso de uso e mais. 2 testes E2E removidos (dependiam do link agora inexistente), 1 teste unitário invertido (`navigation.spec.ts` agora confirma a ausência, não a presença).
2. **Grids responsivos ("no máximo 10 colunas", "boas práticas de borda")**: `ColaboradorHubPage.vue`, `AreaColaboradorHubPage.vue`, `AreaColaboradorEquipePage.vue` e `FederacaoSingularPage.vue` tinham `grid-template-columns: repeat(2, minmax(0,1fr))` — travado em exatamente 2 colunas para sempre, mesmo em telas largas. `SingularHubPage.vue`/`EquipeHubPage.vue` já usavam `repeat(auto-fit, minmax(240px,1fr))` — os quatro primeiros foram alinhados a esse padrão já existente no projeto. Dentro do `max-width:1280px` do conteúdo (`AppShell.vue`), isso nunca chega perto de 10 colunas na prática — não foi necessário um cap explícito.
3. **"Editar perfil" — achado real de autorização, correção pública ao usuário**: ao investigar como tornar a edição funcional, uma primeira checagem (só no controller REST) concluiu, errada, que não havia checagem de autorização no PUT de colaborador. Uma checagem mais funda (camada de serviço) achou `ensureOrganizationAdministrator`/`ensureSessionAdministrator` — uma allowlist fixa de e-mails admin, sem exceção para "o próprio colaborador". Ou seja: autoatendimento de escrita **não é possível hoje** contra o backend real, nem para o campo `Nome` (que tem campo real no contrato). Essa correção foi levada ao usuário antes de implementar (não depois) — ver `specs/features/perfil/specification.md` (FT-PERFIL, ainda `DRAFT`) para o achado completo e a decisão tomada: `/app/perfil` agora tem um formulário de verdade (Nome, Cargo, E-mail adicional, Telefones, Ramais, Celulares), mas persistido **só em `localStorage`** (`usePerfilLocalFields.ts`) — não no backend. UI deixa isso explícito para o usuário.

**Validação**: 38 testes E2E (2 removidos desta rodada, 1 rescrito — `Editar perfil` — nenhum novo arquivo) + 183 unitários (1 invertido) verdes; typecheck limpo.

## Referências

- [`DS-RECONSTRUCTION-SCOPE-01`](./DS-RECONSTRUCTION-SCOPE-01.md)
- [`frontend/src/components/ds/`](../../../../frontend/src/components/ds/)
- [`frontend/src/css/tokens/`](../../../../frontend/src/css/tokens/)
- [`frontend/src/components/app/`](../../../../frontend/src/components/app/)
