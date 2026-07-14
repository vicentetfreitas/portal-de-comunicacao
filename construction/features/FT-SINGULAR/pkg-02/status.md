# PKG-02 — Create Singular

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR |
| PKG | 02 |
| Status | **DONE** |
| Data | 2026-07-14 |
| Executor | feature-implementer |

---

# Escopo

Implementação do cadastro de singular via `POST /api/v1/singulares` com validações RN-SINGULAR-001 a RN-SINGULAR-004.

## Entregas

| Componente | Arquivo | Status |
|------------|---------|--------|
| `SingularDomainService` | `application/service/SingularDomainService.java` | ✅ |
| `SingularApplicationService` | `application/service/SingularApplicationService.java` | ✅ |
| `SingularController` (POST) | `interfaces/rest/SingularController.java` | ✅ |
| Testes unitários domínio | `SingularDomainServiceTest.java` | ✅ |
| Testes AT-SINGULAR-001 | `SingularAcceptanceIntegrationTest.java` | ✅ |

## Regras implementadas

| RN | Validação |
|----|-----------|
| RN-SINGULAR-001 | Federação existente e ativa |
| RN-SINGULAR-002 | Nome obrigatório (Bean Validation) |
| RN-SINGULAR-003 | Sigla única global |
| RN-SINGULAR-004 | Código Unimed único global |

## Cenários AT-SINGULAR-001

| Cenário | Status |
|---------|--------|
| Happy path (201) | ✅ |
| Sigla duplicada (422) | ✅ |
| Código Unimed duplicado (422) | ✅ |
| Federação inválida (422) | ✅ |
| Não autorizado (403) | ✅ |

---

# Validação

| Verificação | Resultado |
|-------------|-----------|
| `SingularDomainServiceTest` | ✅ |
| `SingularAcceptanceIntegrationTest` (AT-001) | ✅ |

---

# Rastreabilidade

| Task | AT |
|------|-----|
| TK-SINGULAR-001 | AT-SINGULAR-001 |

---

# Próximo PKG

**PKG-03** — Read & List (`GET /api/v1/singulares`, `GET /api/v1/singulares/{id}`)
