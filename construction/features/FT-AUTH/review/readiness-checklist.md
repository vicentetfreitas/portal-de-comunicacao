# Readiness Checklist — FT-AUTH (Authentication)

| Item | Valor |
|------|-------|
| Feature Code | FT-AUTH |
| Specification | v2.2 |
| Escopo avaliado | **Sprint 1 — Backend** |
| Status | **Aprovada** |
| Data | 2026-07-09 |
| Executor | auditor |
| Rodada | Pós-correção RN-AUTH-013 / RNF-AUTH-006 |

---

# Critérios de Prontidão — Sprint 1 Backend

| ID | Critério | Evidência | Status |
|----|----------|-----------|--------|
| RR-AUTH-01 | Endpoints `/api/v1/auth/*` implementados | `AuthController` | ✅ |
| RR-AUTH-02 | JWT HMAC-SHA256 com TTL 15 min | `JwtTokenService` | ✅ |
| RR-AUTH-03 | Refresh Token opaco + hash SHA-256 | `RefreshTokenService`, `AUTH_SESSAO` | ✅ |
| RR-AUTH-04 | Cookies HttpOnly + Secure + SameSite | `AuthCookieService` | ✅ |
| RR-AUTH-05 | CSRF em POST mutáveis | `SecurityFilterChainIntegrationTest` | ✅ |
| RR-AUTH-06 | Integração Zimbra abstrata | `ZimbraIdentityProviderClient` | ✅ |
| RR-AUTH-07 | Limite 3 sessões simultâneas | `SessionService.enforceSessionLimit` | ✅ |
| RR-AUTH-08 | Auditoria sem dados sensíveis | `AuthAuditService` | ✅ |
| RR-AUTH-09 | Baseline DDL + `AUTH_SESSAO` no schema | DBA / `database/ddl/` | ✅ |
| RR-AUTH-10 | 14 cenários AC-AUTH aprovados | `AuthAcceptanceIntegrationTest` | ✅ |
| RR-AUTH-11 | Revogação administrativa (RF-AUTH-010 + RN-AUTH-013) | `AdminSessionController`, `SessionAdministratorAuthorizationService` | ✅ |
| RR-AUTH-12 | Frontend auth (FE-001..011) | — | ➖ N/A — fora do escopo Sprint 1 |
| RR-AUTH-13 | RNF-AUTH-006 — `timeout-ms` = 10000 consumido | `application.yaml`, `RestClientConfiguration` | ✅ |
| RR-AUTH-14 | `mvn clean verify` SUCCESS | 188 testes, 0 falhas | ✅ |
| RR-AUTH-15 | Review aprovado | `reconciliation-report.md` | ✅ Aprovado com ressalvas |
| RR-AUTH-16 | Audit conforme | `construction-audit.md` | ⚠️ Parcialmente Conforme (desvios não bloqueadores) |

---

# Bloqueadores anteriores — resolvidos

| # | Item | Referência spec | Status |
|---|------|-----------------|--------|
| 1 | Autorização administrativa — RN-AUTH-013 (HTTP 403) | RF-AUTH-010, RN-AUTH-013 | ✅ Resolvido |
| 2 | Timeout Zimbra — `application.zimbra.timeout-ms` = 10000 | RNF-AUTH-006 | ✅ Resolvido |

---

# Dívida técnica documentada (não bloqueadora)

| Item | Impacto Sprint 1 |
|------|------------------|
| Boundary `JwtAuthenticationFilter` → `accesscontrol` | Nenhum — evolução PF |
| RF-AUTH-011 códigos HTTP callback | Nenhum — comportamento funcional OK |
| `AuthProperties` em pacote compartilhado | Nenhum — refatoração futura |

---

# Resultado

**Aprovada** para encerramento da **Sprint 1 Backend**. Todos os critérios obrigatórios do escopo backend estão atendidos. Frontend permanece **fora de escopo** e não bloqueia esta avaliação. Dívida técnica arquitetural registrada para sprints/evoluções posteriores.
