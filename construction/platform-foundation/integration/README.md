# Integration Module

| Item | Valor |
|------|-------|
| Módulo | Integration |
| Prefixo | PF-INT |
| Pacote | `infrastructure/integration/` |
| Pacote Construction | PKG-04 |
| Status | Não iniciado |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Estabelecer infraestrutura para consumo de sistemas externos com resiliência, observabilidade e baixo acoplamento, preparando a integração Zimbra para FT-AUTH.

---

# Escopo

## Inclui

- `RestClientConfiguration` — cliente HTTP centralizado (Spring RestClient)
- `CorrelationIdInterceptor` — propagação de `X-Correlation-Id` em chamadas outbound
- `IntegrationException` hierarchy — exceções de integração
- Configuração de timeout, retry e circuit breaker
- Interface `IdentityProviderClient` — contrato abstrato para Zimbra
- Padrão Gateway base

## Não inclui

- Implementação concreta `ZimbraClient` (FT-AUTH — TASK-AUTH-INT-001)
- Regras de autenticação ou validação de credenciais
- Webhooks
- Mensageria

---

# Responsabilidades

| Componente | Responsabilidade |
|------------|------------------|
| RestClientConfiguration | Bean RestClient com timeout e interceptors |
| CorrelationIdInterceptor | Propagar Correlation ID do MDC para outbound |
| IntegrationException | Exceção base de integração |
| IntegrationUnavailableException | Sistema externo indisponível (503) |
| IdentityProviderClient | Contrato para validação de identidade (Zimbra) |

---

# Limites

- Domínio nunca conhece detalhes de integração (Gateway pattern)
- Sem implementação Zimbra na Sprint 1A
- Sem credenciais em código

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| PF-CONF (IntegrationProperties, ZimbraProperties) | Configuration | Pendente |
| PF-SEC | Security | Pendente |
| Sprint 0 CorrelationId | `infrastructure/logging/` | Concluído |
| `docs/construction/backend/05-integrations.md` | Padrões de integração | Consultivo |
| DA-AUTH-008 | Zimbra consultado apenas no login | Aprovado |

---

# Componentes Esperados

```text
infrastructure/integration/
├── config/
│   └── RestClientConfiguration.java
├── client/
│   └── IdentityProviderClient.java
├── exception/
│   ├── IntegrationException.java
│   └── IntegrationUnavailableException.java
└── interceptor/
    └── CorrelationIdInterceptor.java
```

---

# Contrato IdentityProviderClient

```java
public interface IdentityProviderClient {
    IdentityValidationResult validateIdentity(IdentityValidationRequest request);
}
```

Implementação concreta (`ZimbraIdentityProviderClient`) será criada em FT-AUTH.

---

# Ordem de Construção

```text
PF-INT-001 (RestClientConfiguration)
    → PF-INT-002 (CorrelationIdInterceptor)
    → PF-INT-003 (IntegrationException hierarchy)
    → PF-INT-004 (Resiliência — timeout/retry/circuit breaker)
    → PF-INT-005 (IdentityProviderClient interface + teste mock)
```

---

# Critérios de Aceite

1. RestClient configurado com timeout de IntegrationProperties
2. Correlation ID propagado em chamadas outbound
3. IntegrationException mapeada no GlobalExceptionHandler
4. Resiliência configurada (conforme CD-S1A-004)
5. IdentityProviderClient interface definida e testada com mock
6. Teste com WireMock ou mock server aprovado

---

# Definition of Done do Módulo

- [ ] Todas as tarefas PF-INT-* concluídas
- [ ] Testes aprovados
- [ ] `review.md` validado
- [ ] Build SUCCESS
- [ ] FT-AUTH pode implementar ZimbraClient (TASK-AUTH-INT-001)

---

# Rastreabilidade

- `docs/construction/backend/05-integrations.md`
- `specs/architecture/authentication-architecture.md`
- `construction/03-construction-packages.md` § PKG-04
