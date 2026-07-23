# Execution Plan — FT-COLABORADOR Frontend

| Item | Valor |
|------|-------|
| Feature Code | **FT-COLABORADOR** |
| Camada | Frontend |
| Sprint | **3** |
| Golden Template | FT-EQUIPE / FT-SINGULAR |
| SSOD | `construction/frontend/features/FT-COLABORADOR/feature-manifest.yaml` |

---

# Objetivo

CRUD administrativo de Colaboradores em `/app/administrador/colaboradores`, consumindo `/api/v1/colaboradores` e selects de singular, área, equipe e gestor.

---

# Rotas

```text
/app/administrador/colaboradores
/app/administrador/colaboradores/lista
/app/administrador/colaboradores/novo
/app/administrador/colaboradores/:id
/app/administrador/colaboradores/:id/editar
```

---

# PKGs

| PKG | Escopo |
|-----|--------|
| PKG-FE-01 | Types, `colaborador.service.ts`, `useColaboradorForm`, rotas, páginas stub, i18n |
| PKG-FE-02 | Cadastro |
| PKG-FE-03 | Listagem + detalhe |
| PKG-FE-04 | Edição |
| PKG-FE-05 | Status |
| PKG-FE-06 | E2E + closure |

---

# Dependências

Frontend Foundation, FT-AUTH, backends FT-COLABORADOR, FT-SINGULAR, FT-AREA, FT-EQUIPE — **FEATURE_APPROVED**.
