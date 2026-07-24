# Casos de Uso — Authentication

| Item | Valor |
|------|-------|
| Feature ID | **FT-AUTH** |
| Projeto | Portal de Comunicação |
| Camada | Features |
| Status | **Approved** |
| Versão | 2.1 |
| Última atualização | 2026-07-09 |

---

## Objetivo

Descrever os fluxos de interação relacionados à autenticação Stateless com JWT, Refresh Token e integração Zimbra.

Complementa `specification.md` e `specs/architecture/authentication-architecture.md`.

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador | Pessoa que deseja acessar o Portal |
| Administrador | Pessoa autorizada a revogar sessões de colaboradores (RF-AUTH-010) |
| Zimbra | Provedor de Identidade — valida credenciais e confirma identidade |
| Portal (Backend) | Emite tokens, gerencia sessão, carrega permissões do banco |
| Frontend | Inicia login, consome identidade, aciona renovação |

---

# Fluxo dos Casos de Uso

```text
UC-AUTH-001 (Login via Zimbra)
        ↓
UC-AUTH-004 (Consultar Identidade) ←── durante sessão
UC-AUTH-005 (Renovar Access Token)  ←── automático
        ↓
UC-AUTH-003 (Acessar Recurso Protegido) ←── contínuo
        ↓
UC-AUTH-002 (Logout)          UC-AUTH-006 (Revogação Administrativa)
```

---

# UC-AUTH-001 — Autenticar Colaborador (Login)

## Objetivo

Permitir que um colaborador autorizado acesse o Portal via Zimbra e receba tokens de sessão.

### Atores

Colaborador, Zimbra, Portal (Backend), Frontend

### Pré-condições

- Colaborador possui conta válida no Zimbra
- Zimbra disponível
- Portal operacional

### Fluxo Principal

1. Colaborador solicita acesso ao Portal
2. Frontend exibe página de login do Portal (`/auth`) — opcionalmente iniciada via `GET /api/v1/auth/login` (302 + `state`)
3. Colaborador informa e-mail corporativo e senha
4. Frontend envia `POST /api/v1/auth/login` (form-urlencoded; CSRF; `state` opcional)
5. Backend autentica no Zimbra (IMAP → SMTP → SOAP) — **única consulta ao IdP**
6. Backend obtém identidade mínima (`email`, `displayName`, `zimbraId`)
7. Backend localiza ou cria Colaborador no banco
8. Backend verifica autorização para utilizar o Portal (colaborador ativo)
9. Backend verifica limite de sessões simultâneas (máx. 3)
10. Backend emite Access Token (JWT, 15 min) e Refresh Token (8h ou 30d)
11. Backend registra sessão no banco (`AUTH_SESSAO`)
12. Backend define Cookies HttpOnly + Secure (`access_token`, `refresh_token`)
13. Backend redireciona Frontend para área autenticada

> Sequência normativa completa: `specs/architecture/authentication-architecture.md`. Protocolo Zimbra: DA-AUTH-012.

### Fluxos Alternativos

#### FA-001 — Credenciais inválidas

1. Zimbra identifica credenciais inválidas
2. Autenticação recusada; nenhum token emitido
3. Colaborador permanece não autenticado

#### FA-002 — Colaborador sem autorização no Portal

1. Zimbra autentica o colaborador
2. Portal verifica que colaborador não possui autorização para o Portal
3. Acesso negado; nenhum token emitido

#### FA-003 — Zimbra indisponível

1. Portal não consegue comunicar com Zimbra
2. Processo interrompido; HTTP 503
3. Colaborador informado da indisponibilidade

#### FA-004 — Limite de sessões excedido

1. Colaborador possui 3 sessões ativas
2. Portal revoga a sessão mais antiga (`FLG_REVOGADA = S`)
3. Access Token da sessão revogada permanece válido até TTL de 15 minutos (RN-AUTH-012)
4. Fluxo principal continua normalmente

#### FA-005 — "Lembrar-me" habilitado

1. Colaborador habilita "Lembrar-me" no login
2. Refresh Token emitido com TTL de 30 dias
3. Access Token mantém TTL de 15 minutos

### Fluxos de Exceção

- **FE-001** — Timeout Zimbra (10s): processo interrompido; HTTP 503
- **FE-002** — Resposta inválida do Zimbra: autenticação não concluída; HTTP 400
- **FE-003** — State/nonce inválido: callback rejeitado; HTTP 400

### Regras Relacionadas

RN-AUTH-001 a RN-AUTH-005, RN-AUTH-009, RN-AUTH-010, RN-AUTH-012

### Requisitos Relacionados

RF-AUTH-001, RF-AUTH-002, RF-AUTH-003, RF-AUTH-008, RF-AUTH-009, RF-AUTH-011

### APIs Utilizadas

AUTH-API-001, AUTH-API-002

### Pós-condições

- Cookies `access_token` e `refresh_token` definidos (HttpOnly + Secure)
- Sessão registrada no banco
- Colaborador autenticado

---

# UC-AUTH-002 — Encerrar Sessão (Logout)

## Objetivo

Encerrar a sessão **atual** do colaborador, iniciado pelo próprio colaborador (RF-AUTH-006), revogando Refresh Token e removendo cookies.

### Atores

Colaborador, Portal (Backend), Frontend

### Pré-condições

- Sessão autenticada válida (ou solicitação idempotente)

### Fluxo Principal

1. Colaborador solicita logout
2. Frontend envia `POST /api/v1/auth/logout` com cookies e token CSRF
3. Backend revoga Refresh Token no banco (`FLG_REVOGADA = S`)
4. Backend remove cookies `access_token` e `refresh_token`
5. Backend registra auditoria (logout)
6. Frontend redireciona para tela pública

### Fluxos Alternativos

#### FA-001 — Sessão inexistente

1. Logout solicitado sem sessão válida
2. Backend retorna sucesso idempotente (204)
3. Cookies removidos se presentes

### Fluxos de Exceção

- **FE-001** — CSRF inválido: HTTP 403

### Regras Relacionadas

RN-AUTH-008, RN-AUTH-012

### Requisitos Relacionados

RF-AUTH-006

### APIs Utilizadas

AUTH-API-004

### Pós-condições

- Refresh Token revogado (`FLG_REVOGADA = S`)
- Cookies removidos
- Renovações futuras dessa sessão impedidas
- Access Token pode permanecer válido até TTL de 15 minutos (RN-AUTH-012); após expiração, renovação retorna HTTP 401

---

# UC-AUTH-003 — Acessar Recurso Protegido

## Objetivo

Garantir que apenas colaboradores autenticados acessem recursos protegidos.

### Atores

Colaborador, Portal (Backend)

### Pré-condições

- Recurso exige autenticação
- Portal operacional

### Fluxo Principal

1. Colaborador solicita recurso protegido
2. Backend extrai Access Token do cookie `access_token`
3. Backend valida assinatura e expiração do JWT (sem consulta ao banco para auth)
4. Backend carrega permissões do banco se necessário
5. Acesso concedido

### Fluxos Alternativos

#### FA-001 — Access Token expirado

1. Backend detecta JWT expirado
2. Retorna HTTP 401
3. Frontend aciona UC-AUTH-005 (Renovação)

#### FA-002 — Colaborador não autenticado

1. Cookie `access_token` ausente ou inválido
2. Acesso bloqueado; HTTP 401
3. Frontend redireciona para login

### Regras Relacionadas

RN-AUTH-001, RN-AUTH-005, RN-AUTH-006

### Requisitos Relacionados

RF-AUTH-007

### APIs Utilizadas

Validação via SecurityFilterChain (não endpoint específico)

### Pós-condições

- Recurso disponibilizado apenas para colaborador autenticado

---

# UC-AUTH-004 — Consultar Colaborador Autenticado

## Objetivo

Disponibilizar ao frontend as informações do colaborador autenticado.

### Atores

Frontend, Portal (Backend)

### Pré-condições

- Access Token válido no cookie

### Fluxo Principal

1. Frontend solicita `GET /api/v1/auth/me`
2. Backend valida Access Token do cookie
3. Backend carrega dados do colaborador e permissões do banco
4. Backend retorna `ApiResponse<AuthenticatedUserResponse>`
5. Frontend utiliza dados para interface e controle de acesso

### Fluxos Alternativos

#### FA-001 — Access Token expirado

1. Backend retorna HTTP 401
2. Frontend aciona UC-AUTH-005

#### FA-002 — Refresh Token também expirado

1. Renovação falha
2. Frontend redireciona para login

### Regras Relacionadas

RN-AUTH-005, RN-AUTH-006

### Requisitos Relacionados

RF-AUTH-005

### APIs Utilizadas

AUTH-API-003

### Pós-condições

- Identidade disponibilizada sem exposição de dados sensíveis

---

# UC-AUTH-005 — Renovar Access Token (Refresh)

## Objetivo

Renovar Access Token expirado utilizando Refresh Token válido, sem novo login.

### Atores

Frontend, Portal (Backend)

### Pré-condições

- Refresh Token válido no cookie `refresh_token`
- Refresh Token não revogado no banco

### Fluxo Principal

1. Frontend detecta Access Token expirado (401 ou expiração iminente)
2. Frontend envia `POST /api/v1/auth/refresh` com cookie `refresh_token` e CSRF
3. Backend valida Refresh Token no banco (hash, expiração, não revogado)
4. Backend emite novo Access Token (JWT, 15 min)
5. Backend atualiza cookie `access_token`
6. Backend registra auditoria (renovação)
7. Frontend repete requisição original

### Fluxos Alternativos

#### FA-001 — Refresh Token expirado

1. Backend detecta expiração
2. Remove cookies; HTTP 401
3. Frontend redireciona para login

#### FA-002 — Refresh Token revogado

1. Backend detecta `FLG_REVOGADA = S` (logout, revogação administrativa ou limite de sessões)
2. Remove cookies; HTTP 401
3. Frontend redireciona para login

> Causas de revogação incluem logout (UC-AUTH-002), revogação administrativa (UC-AUTH-006) e limite de sessões (UC-AUTH-001 FA-004). O Access Token pode ainda ser válido até expirar (RN-AUTH-012).

### Fluxos de Exceção

- **FE-001** — CSRF inválido: HTTP 403

### Regras Relacionadas

RN-AUTH-005, RN-AUTH-011, RN-AUTH-012

### Requisitos Relacionados

RF-AUTH-004, RF-AUTH-010

### APIs Utilizadas

AUTH-API-005

### Pós-condições

- Novo Access Token válido por 15 minutos
- Refresh Token inalterado quando renovação bem-sucedida (mesma sessão)

---

# UC-AUTH-006 — Revogar Sessão Administrativamente

## Objetivo

Invalidar uma sessão existente de um colaborador, iniciado por um **administrador**, sem participação do colaborador (RF-AUTH-010).

### Atores

Administrador, Portal (Backend)

### Pré-condições

- Sessão ativa identificada por `session_id`
- Administrador autorizado a revogar sessões

### Fluxo Principal

1. Administrador identifica `session_id` da sessão alvo
2. Administrador aciona API ou serviço administrativo (TASK-AUTH-BE-020)
3. Backend marca `FLG_REVOGADA = S` em `AUTH_SESSAO`
4. Backend registra auditoria (revogação administrativa)
5. Cookies do colaborador **não** são removidos neste momento

### Fluxos Alternativos

#### FA-001 — Sessão já revogada ou inexistente

1. Backend detecta sessão inexistente ou já revogada
2. Operação retorna sucesso idempotente ou erro conforme contrato administrativo

### Efeitos sobre o colaborador

- Renovações futuras via UC-AUTH-005 retornam HTTP 401 e removem cookies
- Access Token residual permanece válido até TTL de 15 minutos (RN-AUTH-012)
- Novo login obrigatório para criar nova sessão
- Sessão revogada **não pode** ser reativada

### Regras Relacionadas

RN-AUTH-011, RN-AUTH-012

### Requisitos Relacionados

RF-AUTH-010

### APIs Utilizadas

`DELETE /api/v1/admin/sessions/{sessionId}`; efeito validado via AUTH-API-005

### Pós-condições

- Refresh Token revogado imediatamente
- Renovações futuras impedidas
- Colaborador deverá realizar novo login

---

# Rastreabilidade

| Caso de Uso | RF | RN | API | AC | TASK |
|-------------|-----|-----|-----|-----|------|
| UC-AUTH-001 | RF-AUTH-001, 002, 003, 008, 009, 011 | RN-AUTH-001–005, RN-AUTH-009, RN-AUTH-010, RN-AUTH-012 | AUTH-API-001, 002 | AC-AUTH-001, AC-AUTH-002, AC-AUTH-011, AC-AUTH-012, AC-AUTH-013, AC-AUTH-014 | BE-003, BE-004, BE-013–019, INT-001 |
| UC-AUTH-002 | RF-AUTH-006 | RN-AUTH-008, RN-AUTH-012 | AUTH-API-004 | AC-AUTH-004 | BE-006, BE-017 |
| UC-AUTH-003 | RF-AUTH-007 | RN-AUTH-001, RN-AUTH-005, RN-AUTH-006 | — | AC-AUTH-003, AC-AUTH-005 | BE-008 |
| UC-AUTH-004 | RF-AUTH-005 | RN-AUTH-005, RN-AUTH-006 | AUTH-API-003 | AC-AUTH-007 | BE-005, FE-003 |
| UC-AUTH-005 | RF-AUTH-004, RF-AUTH-010 | RN-AUTH-005, RN-AUTH-011, RN-AUTH-012 | AUTH-API-005 | AC-AUTH-008, AC-AUTH-009, AC-AUTH-010 | BE-016, FE-007 |
| UC-AUTH-006 | RF-AUTH-010 | RN-AUTH-011, RN-AUTH-012 | DELETE /api/v1/admin/sessions/{sessionId} | AC-AUTH-010 | BE-020 |

---

# Referências

- `specification.md`
- `api.md`
- `specs/architecture/authentication-architecture.md`
- `decisions.md`
