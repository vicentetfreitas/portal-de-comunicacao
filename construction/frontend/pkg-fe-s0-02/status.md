# PKG-FE-S0-02 — Theme

| Campo | Valor |
|-------|-------|
| Status | **DONE** |
| Sprint | Frontend Foundation (Sprint 0) |
| Dependência | PKG-FE-S0-01 |

## Entregas

- Design tokens SCSS/CSS em `frontend/src/css/tokens/` (cores, tipografia, espaçamento, radius, sombras, breakpoints)
- `design-tokens.scss` com variáveis CSS para temas light/dark (`data-theme`)
- `quasar.variables.scss` sincronizado com paleta Unimed (`#007B5E`)
- `fonts.scss` com referências às fontes corporativas Unimed (fallback Inter/system)
- Boot `theme.ts` — infraestrutura light/dark via Quasar Dark + `prefers-color-scheme`
- Ícones MDI v7 configurados em `quasar.config.ts`

## Validações locais

| Comando | Resultado |
|---------|-----------|
| `yarn typecheck` | ✅ exit 0 |
| `yarn build` | ✅ exit 0 (dist/spa) |

## Critérios (AC-FE-S0-005)

| Critério | Atendido |
|----------|----------|
| Cor primária Unimed `#007B5E` | ✅ |
| Tokens CSS presentes | ✅ |
| Tipografia corporativa referenciada | ✅ |
| Suporte light/dark (infraestrutura) | ✅ |
| MDI v7 via Quasar | ✅ |

## Notas

- Arquivos de fonte Unimed não versionados — fallback tipográfico conforme R-FE-S0-04
- Composable `useTheme` (troca manual/auto) — escopo PKG-FE-S0-08
