# PKG-FE-03 — List & Detail Pages

| Campo | Valor |
|--------|--------|
| Feature | FT-EQUIPE (Frontend) |
| PKG | PKG-FE-03 |
| Status | **DONE** |
| Data conclusão | 2026-07-17 |
| Tasks | TASK-EQUIPE-FE-002, TASK-EQUIPE-FE-003 |

---

## Entregas

| Componente | Caminho |
|------------|---------|
| List page | `EquipeListPage.vue` |
| Detail page | `EquipeDetailPage.vue` |
| Filters | `EquipeFilters.vue` |
| Info card | `EquipeInfoCard.vue` |
| Composable | `useEquipeList.ts` |
| Teste | `useEquipeList.spec.ts` |
| i18n | `equipe.list.*`, `equipe.detail.*` |

## Comportamento

- Listagem paginada com filtros `status`, `areaId`, `name`
- Detalhe via `GET /api/v1/equipes/{id}` com 404 amigável
- Ações: voltar à lista, editar (status em PKG-FE-05)

## VALIDATION SUMMARY

Status
PENDING_REVALIDATION

```bash
cd frontend && yarn lint:check && yarn typecheck && yarn test:unit --run
```

## Próximo PKG

**PKG-FE-04** — READY
