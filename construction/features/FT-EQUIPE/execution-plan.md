# Execution Plan — FT-EQUIPE (Equipe)

| Item | Valor |
|------|-------|
| Feature Code | **FT-EQUIPE** |
| Feature Slug | equipe |
| Sprint | 3 |
| Status | **Encerrada — APPROVED** |
| SSOD | `construction/features/FT-EQUIPE/feature-manifest.yaml` |

---

# Objetivo

Implementar CRUD de Equipes organizacionais — cadastro, consulta, listagem paginada, atualização e ativação/inativação lógica via `/api/v1/equipes`.

Evolui o scaffold mínimo de `EquipeEntity` criado em FT-AREA.

---

# Sequência de PKGs

| PKG | Nome | Escopo |
|-----|------|--------|
| PKG-01 | Equipe Persistence Evolution | Evoluir EquipeEntity, EquipeRepository, ColaboradorEntity.equipeId |
| PKG-02 | Create Equipe | POST, validações RN-001 a 004 |
| PKG-03 | Read & List | GET/{id}, GET listagem |
| PKG-04 | Update Equipe | PUT/{id}, imutabilidade areaId |
| PKG-05 | Status Change | PATCH/{id}/status, bloqueio inativação |
| PKG-06 | Acceptance & Closure | Suíte AT-EQUIPE-* |

---

# Dependências

| Dependência | Status |
|-------------|--------|
| Platform Foundation | ✅ closed |
| FT-AUTH | ✅ closed |
| FT-AREA | ✅ closed |
| Especificação FT-EQUIPE | ✅ APPROVED |
