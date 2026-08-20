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

## Referências

- [`DS-RECONSTRUCTION-SCOPE-01`](./DS-RECONSTRUCTION-SCOPE-01.md)
- [`frontend/src/components/ds/`](../../../../frontend/src/components/ds/)
- [`frontend/src/css/tokens/`](../../../../frontend/src/css/tokens/)
- [`frontend/src/components/app/`](../../../../frontend/src/components/app/)
