# Feature Session — Authentication

| Item | Valor |
|------|-------|
| Feature Code | FT-AUTH |
| Feature Slug | authentication |
| Sprint | 1 |
| Data da sessão | 2026-07-09 |
| Agente | construction-orchestrator |
| SSOD | `construction/features/FT-AUTH/feature-manifest.yaml` |
| Estado operacional | `construction/features/FT-AUTH/construction-state.yaml` |
| Imutabilidade | **READ ONLY** após criação (SESSION-01) |

---

# Regra SESSION-01 / STATE-04

Esta Session é **imutável** durante toda a execução da Feature.

- Somente `Execute Feature` pode criar ou recriar esta Session.
- Nenhum PKG pode modificar este documento.
- A Session representa **conhecimento carregado (Snapshot)**, não progresso.

Progresso operacional: `construction/features/FT-AUTH/construction-state.yaml` (SSOT).  
Histórico detalhado por PKG: `construction/features/FT-AUTH/pkg-XX/status.md`.

---

# Snapshot de Contexto

## Feature

| Campo | Valor |
|-------|-------|
| Code | FT-AUTH |
| Slug | authentication |
| Tipo | business_feature |
| Objetivo | Autenticação stateless via Zimbra, JWT próprio, Refresh Token em cookies HttpOnly |

## Objetivos

- Autenticar colaboradores via Zimbra (consulta única no login)
- Emitir Access Token (JWT, 15 min) e Refresh Token (8h / 30d com "Lembrar-me")
- Armazenar tokens em Cookies HttpOnly + Secure
- Renovar Access Token automaticamente via Refresh Token
- Disponibilizar identidade via `GET /api/v1/auth/me`
- Encerrar sessões (logout, expiração, revogação administrativa)
- Limitar sessões simultâneas a 3 dispositivos
- Auditar eventos de autenticação

## Premissas

- Zimbra disponível e operacional para novos logins
- Platform Foundation encerrada (`phase: closed`)
- Infraestrutura Sprint 0 (DTOs, exception handler, security skeleton) disponível
- HTTPS em todos os ambientes
- Permissões carregadas do banco do Portal (não do Zimbra)

## Restrições

- Sem HTTP Session (Servlet Session)
- Sem armazenamento de credenciais no Portal
- Sem consulta ao Zimbra após o login
- Sem tokens em LocalStorage/SessionStorage
- Prefixo `/api/v1` obrigatório
- Não alterar Platform Foundation

## Contratos

| Endpoint | Método | Autenticação |
|----------|--------|--------------|
| `/api/v1/auth/login` | GET | Público |
| `/api/v1/auth/callback` | GET | Fluxo Zimbra |
| `/api/v1/auth/me` | GET | Cookie `access_token` |
| `/api/v1/auth/refresh` | POST | Cookie `refresh_token` + CSRF |
| `/api/v1/auth/logout` | POST | Cookies + CSRF |

**Cookies:** `access_token` (JWT, 15 min), `refresh_token` (UUID, 8h/30d), `XSRF-TOKEN` (CSRF).

**JWT claims:** `sub`, `sid`, `email`, `name`, `iat`, `exp`, `iss` — sem permissões no token.

**Respostas:** `ApiResponse<T>` (sucesso), `ErrorResponse` (erro).

## Dependências

| Dependência | Status |
|-------------|--------|
| Platform Foundation | ✅ `phase: closed` |
| `specs/features/authentication/` | ✅ Approved |
| `specs/architecture/authentication-architecture.md` | ✅ Approved |
| Infraestrutura de testes (PKG-07 PF) | ✅ Disponível |

## Decisões

| ID | Decisão |
|----|---------|
| DA-AUTH-001 | Autenticação centralizada via Zimbra |
| DA-AUTH-002 | Separação autenticação/autorização |
| DA-AUTH-003 | Stateless — JWT + Refresh Token em cookies |
| DA-AUTH-005 | Sem HTTP Session |
| DA-AUTH-006 | JWT próprio do Portal (TTL 15 min) |
| DA-AUTH-007 | Refresh Token opaco em cookie HttpOnly |
| DA-AUTH-008 | Consulta única ao Zimbra (apenas login) |
| DA-AUTH-009 | Permissões do banco do Portal |
| DA-AUTH-010 | Máximo 3 sessões simultâneas |

## PKGs

| PKG | Nome | Tarefas principais | Dependências |
|-----|------|-------------------|--------------|
| PKG-01 | Security & Tokens | BE-001, BE-002, BE-013..015 | PF security skeleton |
| PKG-02 | Zimbra Integration | INT-001..005, BE-007 | PKG-01 |
| PKG-03 | Login & Callback | BE-003, BE-004, BE-009 | PKG-02 |
| PKG-04 | Session & Refresh | BE-014, BE-016..017, BE-019 | PKG-03 |
| PKG-05 | Identity & Access | BE-005, BE-006, BE-008, BE-010, BE-018 | PKG-04 |
| PKG-06 | Audit & Closure | BE-011, BE-012, QA-* | PKG-05 |

Ordem obrigatória: PKG-01 → PKG-02 → … → PKG-06.

## Artefatos

| Camada | Artefato | Pontos-chave |
|--------|----------|--------------|
| Especificação | `specification.md` | RF-AUTH-001..011, RN-AUTH-001..010 |
| API | `api.md` | AUTH-API-001..005, cookies, CSRF |
| Use Cases | `use-cases.md` | UC-AUTH-001..005 |
| Tasks | `tasks.md` | BE, INT, DB, QA backlog |
| Decisions | `decisions.md` | DA-AUTH-001..010 |
| Acceptance | `acceptance-tests.md` | AC-AUTH-001..014 |
| Arquitetura | `authentication-architecture.md` | AUTH_SESSAO, fluxos, segurança |
| Backend | `accesscontrol/` bounded context | domain → application → infrastructure → interfaces |
| Migration | `V2__access_control.sql` | AUTH_SESSAO, COLABORADOR (mínimo) |

## Riscos

| Risco | Mitigação |
|-------|-----------|
| Indisponibilidade Zimbra | HTTP 503, Resilience4j circuit breaker |
| Endpoints Zimbra corporativos variáveis | Contrato abstrato `IdentityProviderClient` |
| COLABORADOR exige FK organizacional | Auto-criação com federação padrão configurável |
| Escopo frontend fora dos PKGs BE | Backend completo; FE em escopo paralelo |

## Pendências

Nenhuma bloqueante — especificação Approved, L-01/L-02 eliminados.

---

# Definition of Ready

| Critério | Atendido |
|----------|----------|
| Manifesto presente (`feature-manifest.yaml`) | ✅ |
| specification completa | ✅ |
| tasks com backlog | ✅ |
| acceptance-tests definidos | ✅ |
| dependências conhecidas | ✅ |
| decisões bloqueantes resolvidas | ✅ |

---

# Validação de Consistência

| Verificação | Resultado |
|-------------|-----------|
| Manifesto válido e completo | ✅ |
| Sem conflito specs vs docs | ✅ |
| Ordem de PKGs válida | ✅ |
| DoR atendida | ✅ |

---

# Cache de Contexto

Hierarquia obrigatória durante PKGs:

```text
Construction State → Snapshot → Cache → Documento
```

| Regra | Descrição |
|-------|-----------|
| CACHE-01 | Nenhum documento relido se informação já está no Snapshot |
| CACHE-02 | Reutilizar Session ativa salvo evento de invalidação |
| RULE-CONTEXT-01 | Consultar Snapshot/Cache antes de abrir documento adicional |

---

# Próximo Passo

Consultar `construction/features/FT-AUTH/construction-state.yaml` — PKG ativo: `pkg-01`.
