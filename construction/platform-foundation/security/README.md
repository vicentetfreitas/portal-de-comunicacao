# Security Module

| Item | Valor |
|------|-------|
| Módulo | Security |
| Prefixo | PF-SEC |
| Pacote | `infrastructure/security/` |
| Pacote Construction | PKG-03 |
| Status | Não iniciado |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Estabelecer infraestrutura de segurança stateless reutilizável, preparando o SecurityFilterChain, CSRF e filtro JWT esqueleto para extensão por FT-AUTH.

---

# Escopo

## Inclui

- `SecurityConfiguration` — SecurityFilterChain stateless (sem HTTP Session)
- `CsrfConfiguration` — CookieCsrfTokenRepository
- `JwtAuthenticationFilter` — esqueleto de validação JWT (extensível)
- `RestAuthenticationEntryPoint` — resposta 401 padronizada via ErrorResponse
- `CorsConfiguration` — origins configuráveis via SecurityProperties
- Whitelist de endpoints públicos

## Não inclui

- Fluxo login/callback Zimbra (FT-AUTH)
- Emissão de JWT ou Refresh Token (FT-AUTH)
- Tabela AUTH_SESSAO (FT-AUTH)
- Autorização baseada em permissões (Features futuras)
- PasswordEncoder (credenciais no Zimbra)

---

# Responsabilidades

| Componente | Responsabilidade |
|------------|------------------|
| SecurityConfiguration | Configurar filter chain stateless |
| CsrfConfiguration | Proteção CSRF para cookies |
| JwtAuthenticationFilter | Extrair e validar JWT de cookie (esqueleto) |
| RestAuthenticationEntryPoint | Responder 401 com ErrorResponse |
| CorsConfiguration | CORS para frontend |

---

# Limites

- Sem HTTP Session (`SessionCreationPolicy.STATELESS`)
- Sem implementação de login/logout
- Sem consulta ao Zimbra
- Filtro JWT valida estrutura, não emite tokens

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| PF-CONF (SecurityProperties) | Configuration | Pendente |
| PF-PERS | Persistence | Pendente (testes) |
| Sprint 0 SecurityConstants | `shared/constants/SecurityConstants.java` | Concluído |
| `specs/architecture/authentication-architecture.md` | Arquitetura stateless | Consultivo |
| DA-AUTH-005 | Stateless sem HTTP Session | Aprovado |

---

# Componentes Esperados

```text
infrastructure/security/
├── config/
│   ├── SecurityConfiguration.java
│   ├── CsrfConfiguration.java
│   └── CorsConfiguration.java
├── filter/
│   └── JwtAuthenticationFilter.java
└── entrypoint/
    └── RestAuthenticationEntryPoint.java
```

---

# Endpoints Públicos (Whitelist)

| Endpoint | Motivo |
|----------|--------|
| `/actuator/health` | Health check |
| `/api/v1/health` | Health check da fundação |
| `/v3/api-docs/**` | OpenAPI (se habilitado) |
| `/swagger-ui/**` | Swagger UI (se habilitado) |

Endpoints `/api/v1/auth/*` serão adicionados por FT-AUTH.

---

# Ordem de Construção

```text
PF-SEC-001 (SecurityConfiguration stateless)
    → PF-SEC-002 (CsrfConfiguration)
    → PF-SEC-003 (JwtAuthenticationFilter esqueleto)
    → PF-SEC-004 (RestAuthenticationEntryPoint)
    → PF-SEC-005 (CorsConfiguration)
    → PF-SEC-006 (Testes segurança base)
```

---

# Critérios de Aceite

1. SecurityFilterChain configurado como STATELESS
2. CSRF token repository ativo para cookies
3. JwtAuthenticationFilter na chain (esqueleto funcional)
4. Endpoints públicos acessíveis sem autenticação
5. Rotas protegidas retornam 401 sem token
6. CORS configurado conforme SecurityProperties
7. Testes de segurança base aprovados

---

# Definition of Done do Módulo

- [ ] Todas as tarefas PF-SEC-* concluídas
- [ ] Testes aprovados
- [ ] `review.md` validado
- [ ] Build SUCCESS
- [ ] FT-AUTH pode estender filter chain (TASK-AUTH-BE-001)

---

# Relação com FT-AUTH

| Componente Foundation | Uso FT-AUTH |
|-----------------------|-------------|
| SecurityFilterChain stateless | TASK-AUTH-BE-001 |
| CSRF base | TASK-AUTH-BE-002 |
| JwtAuthenticationFilter | Extensão para validação JWT real |
| RestAuthenticationEntryPoint | Tratamento 401 em fluxos auth |
| Whitelist | Adicionar `/api/v1/auth/login`, `/callback` |

---

# Rastreabilidade

- `docs/construction/backend/06-security.md`
- `specs/features/authentication/decisions.md` — DA-AUTH-003, DA-AUTH-005
- `construction/03-construction-packages.md` § PKG-03
