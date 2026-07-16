# Matriz de Testes — API Validation

| Sprint | API-VALIDATION-01 |
|--------|-------------------|
| Endpoints | 27 implementados |
| Casos Java (aceite) | 50 |
| Casos Postman | 58 |

Legenda: ✅ homologado · ⚠️ parcial (depende Zimbra) · — não aplicável

---

## Health

| Endpoint | Cenário | Tipo | Fonte | Status |
|----------|---------|------|-------|--------|
| `GET /health` | 200 + ApiResponse + status UP | Positivo | HealthControllerIntegrationTest | ✅ |
| `GET /health` | Sem autenticação | Positivo | SecurityFilterChainIntegrationTest | ✅ |
| `GET /v3/api-docs` | OpenAPI disponível | Positivo | HealthControllerIntegrationTest | ✅ |

---

## Authentication

| Endpoint | Cenário | Tipo | Fonte | Status |
|----------|---------|------|-------|--------|
| `GET /auth/login` | 302 redirect Zimbra | Positivo | AuthAcceptance AC-AUTH-001 | ⚠️ |
| `GET /auth/login` | 503 Zimbra down | Negativo | AuthAcceptance AC-AUTH-006 | ✅ |
| `GET /auth/callback` | Cookies HttpOnly emitidos | Positivo | AuthAcceptance AC-AUTH-001 | ⚠️ |
| `GET /auth/callback` | 403 colaborador inativo | Negativo | AuthAcceptance AC-AUTH-003 | ✅ |
| `GET /auth/callback` | 503 Zimbra failure | Negativo | AuthAcceptance AC-AUTH-014 | ✅ |
| `GET /auth/me` | 200 AuthenticatedUserResponse | Positivo | AuthAcceptance AC-AUTH-007 | ✅ |
| `GET /auth/me` | 401 sem cookie | Negativo | RestAuthenticationEntryPoint | ✅ |
| `POST /auth/refresh` | 200 renova token | Positivo | AuthAcceptance AC-AUTH-008 | ✅ |
| `POST /auth/refresh` | 401 refresh expirado | Negativo | AuthAcceptance AC-AUTH-009 | ✅ |
| `POST /auth/refresh` | 401 pós revogação admin | Negativo | AuthAcceptance AC-AUTH-010 | ✅ |
| `POST /auth/logout` | 204 + cookies limpos | Positivo | AuthAcceptance AC-AUTH-004 | ✅ |
| `DELETE /admin/sessions/{id}` | 204 revoga sessão | Positivo | AuthAcceptance AC-AUTH-010 | ✅ |
| `DELETE /admin/sessions/{id}` | 404 sessão inexistente | Negativo | AdminSessionController | ✅ |

---

## Singulares

| Endpoint | Cenário | Tipo | Fonte | Status |
|----------|---------|------|-------|--------|
| `POST /singulares` | 201 create | Positivo | SingularAcceptanceIntegrationTest | ✅ |
| `POST /singulares` | 422 sigla duplicada | Negativo | SingularAcceptanceIntegrationTest | ✅ |
| `GET /singulares/{id}` | 200 | Positivo | SingularAcceptanceIntegrationTest | ✅ |
| `GET /singulares/{id}` | 401 sem auth | Negativo | SingularAcceptanceIntegrationTest | ✅ |
| `GET /singulares` | 200 paginação + filtros | Positivo | SingularAcceptanceIntegrationTest | ✅ |
| `PUT /singulares/{id}` | 200 update | Positivo | SingularAcceptanceIntegrationTest | ✅ |
| `PATCH /singulares/{id}/status` | 200 inactivate | Positivo | SingularAcceptanceIntegrationTest | ✅ |

---

## Áreas

| Endpoint | Cenário | Tipo | Fonte | Status |
|----------|---------|------|-------|--------|
| `POST /areas` | 201 create | Positivo | AreaAcceptanceIntegrationTest AT-AREA-001 | ✅ |
| `POST /areas` | 422 nome duplicado | Negativo | AreaAcceptanceIntegrationTest | ✅ |
| `POST /areas` | 403 não admin | Negativo | AreaAcceptanceIntegrationTest | ✅ |
| `GET /areas/{id}` | 200 | Positivo | AreaAcceptanceIntegrationTest AT-AREA-002 | ✅ |
| `GET /areas/{id}` | 404 | Negativo | AreaAcceptanceIntegrationTest | ✅ |
| `GET /areas/{id}` | 401 | Negativo | AreaAcceptanceIntegrationTest | ✅ |
| `GET /areas` | 200 filtros | Positivo | AreaAcceptanceIntegrationTest AT-AREA-003 | ✅ |
| `GET /areas` | 200 lista vazia | Positivo | AreaAcceptanceIntegrationTest | ✅ |
| `PUT /areas/{id}` | 200 update | Positivo | AreaAcceptanceIntegrationTest AT-AREA-004 | ✅ |
| `PUT /areas/{id}` | 422 ciclo hierárquico | Negativo | AreaAcceptanceIntegrationTest | ✅ |
| `PATCH /areas/{id}/status` | 200 inativar | Positivo | AreaAcceptanceIntegrationTest AT-AREA-005 | ✅ |
| `PATCH /areas/{id}/status` | 422 filhas ativas | Negativo | AreaAcceptanceIntegrationTest | ✅ |
| `PATCH /areas/{id}/status` | 200 reativar | Positivo | AreaAcceptanceIntegrationTest | ✅ |

---

## Equipes

| Endpoint | Cenário | Tipo | Fonte | Status |
|----------|---------|------|-------|--------|
| `POST /equipes` | 201 create | Positivo | EquipeAcceptanceIntegrationTest | ✅ |
| `GET /equipes/{id}` | 200 | Positivo | EquipeAcceptanceIntegrationTest | ✅ |
| `GET /equipes` | 200 listagem | Positivo | EquipeAcceptanceIntegrationTest | ✅ |
| `PUT /equipes/{id}` | 200 update | Positivo | EquipeAcceptanceIntegrationTest | ✅ |
| `PATCH /equipes/{id}/status` | 422 colaboradores ativos | Negativo | EquipeAcceptanceIntegrationTest | ✅ |

---

## Colaboradores

| Endpoint | Cenário | Tipo | Fonte | Status |
|----------|---------|------|-------|--------|
| `POST /colaboradores` | 201 create | Positivo | ColaboradorAcceptanceIntegrationTest | ✅ |
| `POST /colaboradores` | 422 e-mail duplicado | Negativo | ColaboradorAcceptanceIntegrationTest | ✅ |
| `POST /colaboradores` | 422 equipe inválida | Negativo | ColaboradorAcceptanceIntegrationTest | ✅ |
| `GET /colaboradores/{id}` | 200 | Positivo | ColaboradorAcceptanceIntegrationTest | ✅ |
| `GET /colaboradores` | 200 filtro teamId | Positivo | ColaboradorAcceptanceIntegrationTest | ✅ |
| `PUT /colaboradores/{id}` | 200 update | Positivo | ColaboradorAcceptanceIntegrationTest | ✅ |

---

## Cross-Feature

| Fluxo | Cenário | Fonte | Status |
|-------|---------|-------|--------|
| Singular→Área→Equipe→Colaborador | CRUD encadeado + listagem | OrgCrossFeatureIntegrationTest XFT-ORG-01 | ✅ |

---

## Postman — casos adicionais instrumentados

| ID | Descrição | Folder Postman |
|----|-----------|----------------|
| PM-001 | Validação envelope em todas respostas 2xx | Collection test script |
| PM-002 | Response time < 5s | Collection test script |
| PM-003 | 401 GET /areas sem cookie | 03 — Áreas (Negative) |
| PM-004 | 400 POST body inválido | Por recurso |
| PM-005 | PageResponse campos obrigatórios | GET listagens |
| PM-006 | CSRF header em PATCH | Mutações |

---

## Endpoints sem homologação (não implementados)

| Recurso | Motivo |
|---------|--------|
| `/api/v1/documentos` | Sem controller |
| `/api/v1/usuarios` | Sem controller |
