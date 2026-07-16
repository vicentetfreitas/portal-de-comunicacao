# PKG-FE-S0-10 — Figma Visual Alignment Foundation

| Campo | Valor |
|--------|--------|
| Sprint | FE-S0-10 |
| PKG | PKG-FE-S0-10 |
| Nome | Figma Visual Alignment Foundation |
| Status | **DONE** |
| Data | 2026-07-16 |
| Executor | construction-engineer |

---

# Escopo

Refinar a Frontend Foundation para alinhar tokens, tipografia, AppShell e Design System aos layouts extraídos do Figma (Login, Home, Notícias, Perfil, Áreas, Equipe, Serviços e Downloads).

Sem lógica de negócio, sem alteração de arquitetura, sem integrações ou contratos de API.

---

# Tokens

## Criados

| Token / Arquivo | Descrição |
|-----------------|-----------|
| `src/css/tokens/_borders.scss` | Larguras e estilo de borda (`thin`, `medium`, `thick`) |
| `src/css/tokens/_layout.scss` | Dimensões do shell (header 64px, footer 48px, sidebar 280px) |
| `src/css/tokens/_text-styles.scss` | Escala tipográfica semântica (Display → Label) |
| `--color-surface-hover` | Estado hover de superfícies |
| `--color-surface-active` | Estado ativo (nav, seleção) |
| `--color-surface-muted` | Fundo suave para inputs/cards |
| `--text-*` (9 estilos) | Variáveis CSS da escala tipográfica |
| `--border-width-*` | Variáveis CSS de borda |
| `--layout-*` | Variáveis CSS de layout do shell |

## Alterados

| Arquivo | Mudança |
|---------|---------|
| `_palette.scss` | Secondary consolidado como alias do gray scale; surfaces adicionadas |
| `design-tokens.scss` | Exportação de borders, layout, text-styles e surfaces |

## Removidos

| Item | Motivo |
|------|--------|
| Escala `$color-secondary-*` duplicada | Consolidada como alias de `$color-gray-*` |

---

# Componentes adicionados

| Componente | Caminho | Tipo |
|------------|---------|------|
| DsNavItem | `components/ds/molecules/DsNavItem.vue` | Navegação |
| DsProfileSummary | `components/ds/molecules/DsProfileSummary.vue` | Perfil |
| DsSectionHeader | `components/ds/molecules/DsSectionHeader.vue` | Cabeçalho de seção |
| DsContentCard | `components/ds/molecules/DsContentCard.vue` | Card horizontal |
| DsContentCardCompact | `components/ds/molecules/DsContentCardCompact.vue` | Card compacto |
| DsServiceCard | `components/ds/molecules/DsServiceCard.vue` | Card de serviço |
| DsActionCard | `components/ds/molecules/DsActionCard.vue` | Botão grande (Área) |
| SidebarProfile | `components/app/sidebar/SidebarProfile.vue` | AppShell |
| SidebarMenu | `components/app/sidebar/SidebarMenu.vue` | AppShell |
| SidebarMenuItem | `components/app/sidebar/SidebarMenuItem.vue` | AppShell |
| SidebarSection | `components/app/sidebar/SidebarSection.vue` | AppShell |

---

# Componentes alterados

| Componente | Mudança |
|------------|---------|
| DsButton | Variante `link`; tamanhos `xs` e `xl` |
| DsCard | Variantes `elevated`, `outlined`, `flat` |
| DsInput | Variantes `outlined`, `filled`, `standard` |
| DsAvatar | Tamanhos `xs` e `xl` |
| DsIcon | Tamanhos `xs` e `xl` |
| AppHeader | Logo, botão voltar, área de usuário, slots |
| AppSidebar | Modularizado; perfil com avatar/saudação/edição |
| AppFooter | Tokens de altura, tipografia e espaçamento |
| AppShell | Layout tokens para min-height |
| `ds.scss` | Estilos de todos os novos componentes e tipografia |
| `showcase.vue` | Demonstração completa dos novos componentes |
| `pt-BR.ts` | Chaves i18n para showcase e sidebar |

---

# Validações

| Comando | Resultado |
|---------|-----------|
| `yarn lint` | ✅ PASS |
| `yarn typecheck` | ✅ PASS |
| `yarn test` | ✅ PASS (16 arquivos, 38 testes) |
| `yarn build` | ✅ PASS (`dist/spa`) |

---

# Evidências

- Showcase (`/showcase`) demonstra escala tipográfica, variantes de átomos, navegação, perfil e cards de conteúdo.
- AppShell renderiza sidebar modular com perfil placeholder (sem store).
- Todos os componentes utilizam design tokens — sem valores hardcoded de cor/tipografia nos novos componentes.

---

# Resumo operacional

PKG-FE-S0-10 consolidou tokens visuais, padronizou escala tipográfica semântica, modularizou a sidebar em quatro subcomponentes, refinou header/footer com tokens de layout, e adicionou sete componentes reutilizáveis ao Design System alinhados aos layouts Figma. Nenhuma lógica de negócio, store de domínio ou chamada HTTP foi introduzida.
