# PKG-04 — Update Singular

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR |
| PKG | 04 |
| Status | **DONE** |
| Data | 2026-07-14 |
| Executor | feature-implementer |

---

# Escopo

Implementação de atualização cadastral via `PUT /api/v1/singulares/{id}`.

## Entregas

| Componente | Descrição | Status |
|------------|-----------|--------|
| `SingularDomainService.validateFederacaoActiveForUpdate` | RN-SINGULAR-001 na atualização | ✅ |
| `SingularApplicationService.update` | RF-SINGULAR-004 | ✅ |
| `SingularController` PUT `/{id}` | Endpoint de atualização | ✅ |
| `SingularAcceptanceIntegrationTest` | AT-SINGULAR-004 | ✅ |

## Regras implementadas

| RN | Validação |
|----|-----------|
| RN-SINGULAR-001 | Federação vinculada ativa na atualização |
| RN-SINGULAR-002 | Nome obrigatório (Bean Validation) |
| RN-SINGULAR-003 | Sigla única (excluindo próprio id) |
| RN-SINGULAR-004 | Código Unimed único (excluindo próprio id) |
| RN-SINGULAR-007 | `federacaoId` imutável (ausente do payload) |

## Cenários AT-SINGULAR-004

| Cenário | HTTP |
|---------|------|
| Happy path | 200 |
| Sigla duplicada | 422 |
| Federação inativa | 422 |
| Singular inexistente | 404 |
| Não autorizado | 403 |

---

# Rastreabilidade

| Task | AT |
|------|-----|
| TK-SINGULAR-004 | AT-SINGULAR-004 |

---

# Próximo PKG

**PKG-05** — Status Change (`PATCH /api/v1/singulares/{id}/status`)
