# PKG-01 — Singular Scaffold & Persistence

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR |
| PKG | 01 |
| Status | **DONE** |
| Data | 2026-07-14 |
| Executor | feature-implementer |

---

# Escopo

Evolução da camada de persistência e contratos REST base para Singular no bounded context `organization`.

## Entregas

| Componente | Arquivo | Status |
|------------|---------|--------|
| `SingularStatus` | `domain/model/SingularStatus.java` | ✅ |
| `SingularEntity` (evoluída) | `infrastructure/persistence/entity/SingularEntity.java` | ✅ |
| `FederacaoEntity` | `infrastructure/persistence/entity/FederacaoEntity.java` | ✅ |
| `SingularRepository` (queries unicidade) | `infrastructure/persistence/repository/SingularRepository.java` | ✅ |
| `FederacaoRepository` | `infrastructure/persistence/repository/FederacaoRepository.java` | ✅ |
| `CreateSingularRequest` | `interfaces/rest/dto/CreateSingularRequest.java` | ✅ |
| `UpdateSingularRequest` | `interfaces/rest/dto/UpdateSingularRequest.java` | ✅ |
| `UpdateSingularStatusRequest` | `interfaces/rest/dto/UpdateSingularStatusRequest.java` | ✅ |
| `SingularResponse` | `interfaces/rest/dto/SingularResponse.java` | ✅ |
| `SingularMapper` | `interfaces/rest/mapper/SingularMapper.java` | ✅ |
| Teste unitário mapper | `SingularMapperTest.java` | ✅ |

## Fora do escopo (PKGs posteriores)

- `SingularController`, services e domain rules
- `AreaRepository.existsBySingularIdAndAtivo` (PKG-05)
- Query de listagem paginada (PKG-03)
- Testes de aceite AT-SINGULAR-* (PKG-06)

---

# Validação

| Verificação | Resultado |
|-------------|-----------|
| `SingularMapperTest` | ✅ PASS |
| Compilação backend | ✅ |
| Regressão `AreaDomainServiceTest` | ✅ PASS |

---

# Rastreabilidade

| Task | Escopo PKG-01 |
|------|----------------|
| TK-SINGULAR-001 | Base (entity, repository, DTOs, mapper) |

---

# Próximo PKG

**PKG-02** — Create Singular (`POST /api/v1/singulares`)
