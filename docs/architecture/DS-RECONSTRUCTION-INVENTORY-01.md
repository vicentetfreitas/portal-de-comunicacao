# DS-RECONSTRUCTION-INVENTORY-01 — Inventário de reconstrução (leitura, sem alterações)

| Campo | Valor |
|---|---|
| **ID** | DS-RECONSTRUCTION-INVENTORY-01 |
| **Status** | `READ_ONLY_INVENTORY` |
| **Precedentes** | [`DS-RECONSTRUCTION-SCOPE-01`](decisions/DS-RECONSTRUCTION-SCOPE-01.md) §17, [`AUDITORIA-DS-FIGMA-01`](decisions/AUDITORIA-DS-FIGMA-01.md) |
| **Fontes** | 20 arquivos `Ds*` lidos por completo (código), `_layout/_palette/_spacing/_radius/design-tokens.scss`, `AppHeader/AppSidebar/AppFooter.vue` + `sidebar/*.vue`, contagem de consumidores por `grep`, `construction/17-frontend-e2e-behavior-policy.md`, listagem de `pages/`, evidência Figma já registrada |

Não altera arquivos, não decide as pendências §15 do SCOPE-01, não cria ADR/`FT-DS`. Classificações são **preliminares**.

---

## 1. Átomos

| Item | Classificação | Evidência | Nota |
|---|---|---|---|
| `DsButton` | **PRESERVE** | 23 consumidores; único ponto de acesso a `q-btn` (0 usos diretos de `q-btn` fora de `ds/`) | Encapsulamento 100% efetivo |
| `DsIcon` | **PRESERVE** | 4 consumidores diretos + usado internamente por 6 outros `Ds*`; único ponto de `q-icon` | Base de composição de quase todo o DS |
| `DsInput` | **PRESERVE** | 6 consumidores; único ponto de `q-input`; contrato E2E de erro (`role="alert"`) documentado em `construction/17` | — |
| `DsAvatar` | **PRESERVE** | 2 consumidores diretos + usado por `DsProfileSummary`/`AppHeader`; único ponto de `q-avatar` | Baixo uso direto, mas nó de composição |
| `DsSelect` | **RECONSIDER** | 5 consumidores; único ponto de `q-select`; sem evidência de variantes/estado no Figma auditado | Não há Figma correspondente para validar API |
| `DsBadge` | **PRESERVE** | 7 consumidores; único ponto de `q-badge`; contrato E2E (`role="status"`) documentado | Padrão de status (Ativa/Inativa) já em produção real (Equipe/Singular) |

## 2. Moléculas

| Item | Classificação | Evidência | Nota |
|---|---|---|---|
| `DsNavItem` | **PRESERVE** | Consumido via `SidebarMenuItem.vue` (AppShell), que é um wrapper fino (i18n/rota) — não duplicação | Resolve parcialmente a ambiguidade §11: é composição em camadas, não overlap |
| `DsProfileSummary` | **PRESERVE** | Consumido via `SidebarProfile.vue` (AppShell), mesmo padrão de wrapper fino | Idem acima |
| `DsCard` | **PRESERVE** | 14 consumidores reais (Equipe/Singular/Colaborador/páginas raiz); documentado em `construction/17` como wrapper "sem contrato próprio, pass-through" | Peça de infraestrutura mais usada depois de `DsButton`/`DsPageHeader` |
| `DsPageHeader` | **PRESERVE** | 21 consumidores; contrato E2E (`<h1>` via `getByRole('heading')`) documentado | Segundo item mais adotado do DS |
| `DsBreadcrumbs` | **RECONSIDER** | Apenas 2 consumidores; sem evidência de uso no Figma auditado (breadcrumb não aparece nas 8 telas) | Baixa adoção, sem referência visual |
| `DsSearchInput` | **PRESERVE** | 2 consumidores, mas 1 é `AppHeader.vue` (chrome estrutural) | Uso estrutural justifica manter mesmo com baixa contagem |
| `DsDialog` | **RECONSIDER** | 3 consumidores; nenhuma tela do Figma auditado mostra modal (frames estáticos, sem estado de overlay) | Sem evidência visual, não sem uso real |
| `DsActionCard` | **PRESERVE** | 4 consumidores; corresponde a "Botão Equipe"/"Botão Arquivos" na tela Areas do Figma (não implementada) | Padrão validado por uso real + candidato Figma |
| `DsContentCard` | **RETIRE candidato** | **0 consumidores em produção — único uso é `pages/showcase.vue`** | Corresponde ao card de notícia do Figma, mas a tela Home/Notícia nunca foi implementada |
| `DsContentCardCompact` | **RETIRE candidato** | **0 consumidores em produção — único uso é `pages/showcase.vue`** | Mesma lacuna acima |
| `DsServiceCard` | **RETIRE candidato** | **0 consumidores em produção — único uso é `pages/showcase.vue`** | Corresponde à tela Serviços do Figma, nunca implementada |
| `DsSectionHeader` | **RETIRE candidato** | **0 consumidores em produção — único uso é `pages/showcase.vue`** | Ambíguo com `DsPageHeader`; sem uso real para desambiguar |

## 3. Organismos

| Item | Classificação | Evidência | Nota |
|---|---|---|---|
| `DsDataTable` | **PRESERVE** | 3 consumidores; único ponto de `q-table`; contrato E2E (`DsDataTable` pass-through) documentado | — |
| `DsFormCard` | **RECONSIDER** | 4 consumidores; é apenas `DsCard` com título fixo — sobreposição funcional quase total | Candidato a fusão com `DsCard` (props opcionais), não a retirada |
| `ds-notify` | **PRESERVE** | Runtime (Quasar `Notify`), não visual estático — não comparável ao Figma | Função utilitária, sem ambiguidade |

## 4. Tokens — correção ao baseline do SCOPE-01

| Categoria | Classificação | Evidência | Nota |
|---|---|---|---|
| Primitivos (`_palette`, `_spacing`, `_radius`, `_typography`, `_borders`, `_breakpoints`) | **PRESERVE o papel; RECONSIDER a origem** | Valores concretos existem e são usados (`--color-primary-500: #007b5e` etc.) | Paleta cita "Unimed brand" e "discovery", não o arquivo Figma auditado — origem seguinda não confirmada contra este Figma |
| Semânticos (`--color-surface-*`, `--color-text-*`) | **PRESERVE** | Camada existe e é usada por `AppHeader`/`AppSidebar`/`AppFooter` | — |
| **De componente** (`--text-page-title-*`, `--text-section-title-*`, `--text-card-title-*`) | **PRESERVE — correção factual ao §6** | `_text-styles.scss`/`design-tokens.scss` já nomeiam tokens por papel de componente (page-title, section-title, card-title) | O SCOPE-01 §6 afirma "de componente: não há camada explícita" — **isso não é mais exato**; a camada existe, ainda que parcial |
| `_layout.scss` (header 64px / footer 48px / sidebar 280px) | **RECONSIDER — discrepância não resolvida** | Comentário no código: "Layout tokens — shell dimensions (**Figma-aligned**)". Medição real do Figma auditado: topbar ≈132px, footer ≈74px, sidebar ≈347px | **Contradição factual**: o código afirma alinhamento com Figma que a auditoria não confirma. Não decidido aqui qual lado está desatualizado |

## 5. AppShell × DS — ambiguidade do §11 parcialmente resolvida

**Constatação:** `AppSidebar.vue` **não duplica** `DsNavItem`/`DsProfileSummary`. Ele delega a `SidebarMenuItem.vue` e `SidebarProfile.vue` (dentro de `components/app/sidebar/`), que são wrappers finos — adicionam apenas i18n e estado de rota ativa, e internamente consomem `DsNavItem`/`DsProfileSummary` diretamente. `AppHeader.vue` e `AppFooter.vue` usam `DsButton`, `DsIcon`, `DsBadge`, `DsSearchInput`, `DsAvatar` da mesma forma.

**Classificação:** `AppHeader.vue` / `AppSidebar.vue` / `AppFooter.vue` → **PRESERVE** como camada de composição legítima (não precisa de "rebuild"); a sobreposição citada em §11 é estrutura correta, não drift.

## 6. Quasar direto vs. encapsulado (insumo para §5)

**Constatação:** `q-btn`, `q-input`, `q-select`, `q-icon`, `q-avatar`, `q-badge`, `q-card`, `q-dialog`, `q-table`, `q-breadcrumbs` têm **0 ocorrências diretas fora de `ds/`** — encapsulamento 100% respeitado.

**Constatação:** `q-layout/q-header/q-drawer/q-footer/q-toolbar` (chrome do AppShell) e `q-form/q-td/q-banner/q-checkbox/q-page` (usados em formulários de Equipe/Singular) são usados diretamente, sem wrapper DS.

Isso é evidência concreta a favor da distinção já cogitada no §5: "chrome de layout" e "slots/formulário de Feature" como candidatos legítimos a Quasar puro — mas a política normativa (§15.4) continua **pendente**, esta auditoria só fornece a evidência de uso atual.

## 7. Achado crítico de escopo — telas Figma sem página implementada

**Constatação (a mais relevante deste inventário):** `pages/` contém apenas Autenticação, Admin, `app/index.vue`, Organização (Colaborador/Equipe/Singular CRUD), `primeiro-acesso`, `showcase` e `unauthorized`. **Não existe nenhuma página implementada correspondente a**: Home/Feed, Notícia, Areas, Areas-Equipe, Areas-Arquivos e Downloads, Perfil do Usuário, Serviços — as 7 telas de produto identificadas no Figma (além do Login).

Os únicos componentes DS desenhados para esse domínio (`DsContentCard`, `DsContentCardCompact`, `DsServiceCard`, `DsSectionHeader`) não têm consumidor de produção — só aparecem em `pages/showcase.vue`. Ou seja: parte do DS foi construída **antecipando** um produto que ainda não foi implementado.

Isso não decide se essas telas serão implementadas — apenas evidencia que a reconstrução do DS e a implementação de Features estão dessincronizadas nesse ponto.

## 8. Candidatos Feature → DS

**Constatação:** `EquipeInfoCard`, `SingularInfoCard`, `ColaboradorInfoCard` seguem o mesmo padrão (`DsCard` + `DsBadge` de status + `<dl>` de campos), mas cada um está fortemente acoplado a tipos/i18n de domínio (`equipe.detail.fields.*`).

**Classificação:** **PRESERVE como Feature** — consistente com o princípio §10 (acoplado a domínio). Não há evidência suficiente aqui para promover um "InfoCard genérico" ao DS; isso é uma decisão de arquitetura (§15.6), não uma constatação factual.

## 9. Redução de incertezas do §15 do SCOPE-01

| # | Pendência §15 | Efeito deste inventário |
|---|---|---|
| 1 | Papel definitivo do Figma | **Não resolvido** — segue pendente |
| 2 | Taxonomia final de tokens | **Parcialmente informado** — camada "de componente" já existe no código (contradiz premissa de vazio total) |
| 3 | Catálogo-alvo de componentes | **Insumo direto produzido** — este inventário é o catálogo-alvo preliminar solicitado |
| 4 | Política Quasar vs. produto | **Evidência levantada** (§6 acima), decisão normativa não tomada |
| 5 | Estratégia de compatibilidade/migração | **Não resolvido** |
| 6 | Critérios de promoção Feature → DS | **Não resolvido**, um candidato mapeado sem promoção (§8) |
| 7 | Ferramenta de catálogo/regressão visual | **Não resolvido** |
| 8 | ADR de identidade DS-01–DS-10 | **Não resolvido** |

## 10. Constatações vs. decisões pendentes — síntese

**Constatações (fato, verificável no código/Figma nesta sessão):**
- Encapsulamento Quasar é real e consistente para os 10 primitivos centrais.
- 4 moléculas (`DsContentCard`, `DsContentCardCompact`, `DsServiceCard`, `DsSectionHeader`) não têm consumidor de produção.
- AppShell não duplica DS — compõe em camada fina.
- Token de "componente" já existe parcialmente, ao contrário do que o SCOPE-01 §6 registra.
- Dimensões de `_layout.scss` divergem da medição real do Figma auditado, apesar do comentário "Figma-aligned".
- Não há página implementada para 7 das 8 telas do Figma.

**Decisões ainda pendentes (não resolvidas por este inventário):** todas as 8 listadas no §15 do SCOPE-01, incluindo se os 4 componentes órfãos devem ser retirados, mantidos como especificação antecipada, ou revisados quando as telas forem priorizadas.

---

Nenhum arquivo foi alterado, nenhum ADR ou `FT-DS` foi criado, nenhuma Feature foi migrada. Aguardando revisão.
