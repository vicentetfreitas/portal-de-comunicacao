# PKG-03 — Read & List

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR |
| PKG | 03 |
| Status | **DONE** |
| Data | 2026-07-14 |
| Executor | feature-implementer |

---

# Escopo

Implementação de consulta por identificador e listagem paginada com filtros.

## Entregas

| Componente | Descrição | Status |
|------------|-----------|--------|
| `SingularDomainService.loadSingularOrThrow` | Consulta com 404 | ✅ |
| `SingularRepository.findByFilters` | Query paginada com filtros | ✅ |
| `SingularApplicationService.findById` | RF-SINGULAR-002 | ✅ |
| `SingularApplicationService.list` | RF-SINGULAR-003 | ✅ |
| `SingularController` GET `/{id}` e GET | Endpoints de leitura | ✅ |
| `SingularAcceptanceIntegrationTest` | AT-SINGULAR-002, AT-SINGULAR-003 | ✅ |

## Filtros de listagem

`status`, `federacaoId`, `name`, `acronym`, `codigoUnimed` + paginação corporativa (`page`, `size`, `sort`).

---

# Validação

| AT | Cenários |
|----|----------|
| AT-SINGULAR-002 | Happy path, 404, 401 |
| AT-SINGULAR-003 | Filtro status ACTIVE, página vazia |

---

# Rastreabilidade

| Task | AT |
|------|-----|
| TK-SINGULAR-002 | AT-SINGULAR-002 |
| TK-SINGULAR-003 | AT-SINGULAR-003 |

---

# Próximo PKG

**PKG-04** — Update Singular (`PUT /api/v1/singulares/{id}`)
