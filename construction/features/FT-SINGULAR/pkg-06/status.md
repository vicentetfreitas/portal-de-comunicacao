# PKG-06 — Acceptance & Closure

| Campo | Valor |
|--------|--------|
| Feature | FT-SINGULAR |
| PKG | 06 |
| Status | **DONE** |
| Data | 2026-07-14 |
| Executor | feature-implementer |

---

# Escopo

Validação final da suíte de aceite FT-SINGULAR e verificação de build completo (`mvn clean verify`).

## Entregas

| Componente | Descrição | Status |
|------------|-----------|--------|
| `SingularAcceptanceIntegrationTest` | Suíte AT-SINGULAR-001 a AT-SINGULAR-005 | ✅ |
| `SingularDomainServiceTest` | Validações RN-001, RN-003, RN-006 | ✅ |
| `SingularMapperTest` | Mapeamento entity → response | ✅ |
| `application-local.yaml` | Correção regressão `ConfigurationPropertiesLocalProfileTest` | ✅ |
| `mvn clean verify` | BUILD-01 — verificação completa | ✅ |

---

# Cobertura de Aceite

| AT | Cenários automatizados |
|----|------------------------|
| AT-SINGULAR-001 | Cadastro 201, sigla duplicada 422, código Unimed duplicado 422, federação inválida 422, não autorizado 403 |
| AT-SINGULAR-002 | Consulta 200, inexistente 404, não autenticado 401 |
| AT-SINGULAR-003 | Filtro status ACTIVE, página vazia |
| AT-SINGULAR-004 | Atualização 200, sigla duplicada 422, federação inativa 422, inexistente 404, não autorizado 403 |
| AT-SINGULAR-005 | Inativação 200, bloqueio com área ativa 422, reativação 200, inexistente 404 |

---

# Validação de Build

| Verificação | Resultado |
|-------------|-----------|
| `SingularAcceptanceIntegrationTest` | ✅ PASS (19 cenários) |
| `SingularDomainServiceTest` | ✅ PASS |
| `SingularMapperTest` | ✅ PASS |
| `mvn clean verify` | ✅ BUILD SUCCESS |
| Regressão FT-AREA / FT-AUTH | ✅ Sem falhas |

---

# Rastreabilidade

| Task | AT |
|------|-----|
| TK-SINGULAR-001 | AT-SINGULAR-001 |
| TK-SINGULAR-002 | AT-SINGULAR-002 |
| TK-SINGULAR-003 | AT-SINGULAR-003 |
| TK-SINGULAR-004 | AT-SINGULAR-004 |
| TK-SINGULAR-005 | AT-SINGULAR-005 |

---

# Observações

- OQ-SINGULAR-001: reativação valida federação ativa (comportamento defensivo, não bloqueante).
- OQ-020: autorização administrativa via `sessionAdministratorEmails` (padrão FT-AREA).
- AT-SINGULAR-003 cenário `page=-1` → 400: não aplicável — `PaginationUtils` normaliza página negativa (padrão corporativo).

---

# Próximo passo

**Close FT-SINGULAR** — encerramento formal (closure-report, review, audit, `phase: closed`).
