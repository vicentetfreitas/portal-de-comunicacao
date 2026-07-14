# PKG-05 — Status Change

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR |
| PKG | 05 |
| Status | **DONE** |
| Data | 2026-07-14 |
| Executor | feature-implementer |

---

# Escopo

Implementação de ativação/inativação lógica via `PATCH /api/v1/singulares/{id}/status`.

## Entregas

| Componente | Descrição | Status |
|------------|-----------|--------|
| `AreaRepository.existsBySingularIdAndAtivo` | Integração FT-AREA para RN-006 | ✅ |
| `SingularDomainService.validateDeactivation` | RN-SINGULAR-006 | ✅ |
| `SingularApplicationService.updateStatus` | RF-SINGULAR-005 | ✅ |
| `SingularController` PATCH `/{id}/status` | Endpoint de status | ✅ |
| `SingularDomainServiceTest` | Teste de inativação bloqueada | ✅ |
| `SingularAcceptanceIntegrationTest` | AT-SINGULAR-005 | ✅ |

## Regras implementadas

| RN | Validação |
|----|-----------|
| RN-SINGULAR-005 | Inativação lógica (`FLG_ATIVO = 'N'`) |
| RN-SINGULAR-006 | Bloqueio se houver áreas ativas vinculadas |

## Cenários AT-SINGULAR-005

| Cenário | HTTP |
|---------|------|
| Inativação com sucesso | 200 |
| Inativação bloqueada (área ativa) | 422 |
| Reativação | 200 |
| Singular inexistente | 404 |

---

# Rastreabilidade

| Task | AT |
|------|-----|
| TK-SINGULAR-005 | AT-SINGULAR-005 |

---

# Próximo PKG

**PKG-06** — Acceptance & Closure
