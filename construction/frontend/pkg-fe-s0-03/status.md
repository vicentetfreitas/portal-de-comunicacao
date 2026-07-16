# PKG-FE-S0-03 — Design System

| Campo | Valor |
|-------|-------|
| Status | **DONE** |
| Sprint | Frontend Foundation (Sprint 0) |
| Dependência | PKG-FE-S0-02 |

## Entregas

### Estrutura atômica (`components/ds/`)

| Camada | Componentes |
|--------|-------------|
| **Átomos** | `DsButton`, `DsInput`, `DsSelect`, `DsIcon`, `DsBadge`, `DsAvatar` |
| **Moléculas** | `DsCard`, `DsBreadcrumbs`, `DsSearchInput`, `DsPageHeader`, `DsDialog` |
| **Organismos** | `DsDataTable`, `DsFormCard`, `ds-notify` (wrapper Notify) |

### Infraestrutura

- Barrel export: `components/ds/index.ts`
- Tipos compartilhados: `components/ds/types.ts`
- Estilos DS com tokens CSS: `components/ds/ds.scss`
- Página showcase: `pages/showcase.vue` (rota `/showcase`)
- Plugin Quasar Notify habilitado
- i18n pt-BR para textos da showcase

## Validações locais

| Comando | Resultado |
|---------|-----------|
| `yarn typecheck` | ✅ exit 0 |
| `yarn build` | ✅ exit 0 (dist/spa) |

## Critérios (AC-FE-S0-006)

| Critério | Atendido |
|----------|----------|
| Átomos essenciais exportados | ✅ |
| Moléculas essenciais exportadas | ✅ |
| Organismos de infraestrutura | ✅ |
| Convenção `Ds*` | ✅ |
| Página showcase renderiza componentes | ✅ |
| Estilos usam exclusivamente design tokens | ✅ |
| Sem regras de negócio / telas de features | ✅ |

## Notas

- Brandbook não consultado — tokens PKG-FE-S0-02 suficientes
- Layouts dedicados e roteamento estrutural — escopo PKG-FE-S0-04/05
- Composable `useTheme` — escopo PKG-FE-S0-08
