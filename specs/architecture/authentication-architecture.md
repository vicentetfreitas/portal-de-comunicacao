# Arquitetura de Autenticação

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Architecture (Specs) |
| Feature | FT-AUTH |
| Versão | 1.1 |
| Status | Approved |
| Última atualização | 2026-07-24 |

**Fonte normativa da Feature:** `specs/features/authentication/`

**Protocolo operacional Zimbra (SSOT de homologação):** `docs/discovery/ft-auth-zimbra-homologacao.md`

---

# Visão Geral

O Portal de Comunicação adota arquitetura **Stateless** para gerenciamento de sessão.

O **Zimbra** atua exclusivamente como **Provedor de Identidade (Identity Provider)**. O Portal consulta o Zimbra **apenas durante o login** para validar credenciais e confirmar identidade.

O protocolo de integração homologado **não** é OAuth/OIDC redirect para o Zimbra. É **proxy de credenciais** no Portal: o frontend coleta e-mail/senha na página de login do Portal; o backend valida no Zimbra via **IMAP → SMTP AUTH → SOAP** (nessa ordem), alinhado ao legado corporativo (DA-AUTH-012).

Após autenticação bem-sucedida, o Portal:

1. Localiza ou cria automaticamente o colaborador no banco de dados.
2. Emite um **Access Token** (JWT próprio, TTL 15 minutos).
3. Emite um **Refresh Token** (TTL 8 horas; até 30 dias com "Lembrar-me").
4. Armazena ambos em **Cookies HttpOnly + Secure**.
5. Controla integralmente a sessão — nenhuma requisição subsequente consulta o Zimbra.

As **permissões da aplicação** são carregadas exclusivamente do **banco de dados do Portal**, nunca do Zimbra.

---

# Responsabilidades

## Zimbra (Identity Provider)

| Responsabilidade | Detalhe |
|------------------|---------|
| Validar credenciais | E-mail corporativo e senha informados pelo usuário |
| Confirmar identidade | Retornar dados mínimos de identidade ao Portal |
| Disponibilidade | Serviço externo crítico — indisponibilidade impede novos logins |

**Não é responsabilidade do Zimbra:**

- Emitir tokens de sessão do Portal
- Fornecer permissões da aplicação
- Manter sessão operacional do Portal

## Portal (Backend)

| Responsabilidade | Detalhe |
|------------------|---------|
| Orquestrar login | Expor `GET/POST /auth/login` e `GET /auth/callback`; coletar credenciais via página do Portal |
| Consultar Zimbra | Apenas na etapa de login (IMAP/SMTP/SOAP) |
| Gerenciar colaborador | Localizar ou criar registro no banco |
| Emitir tokens | Access Token (JWT) e Refresh Token |
| Armazenar tokens | Cookies HttpOnly + Secure |
| Renovar sessão | Via Refresh Token automático |
| Revogar sessão | Logout, expiração e revogação administrativa |
| Controlar sessões simultâneas | Máximo 3 dispositivos por usuário |
| Carregar permissões | Do banco de dados do Portal |
| Proteger recursos | Validar Access Token em cada requisição |
| Auditoria | Login, logout, falha, expiração, renovação |
| CSRF | Proteção obrigatória em fluxos autenticados por Cookie |

## Portal (Frontend)

| Responsabilidade | Detalhe |
|------------------|---------|
| Iniciar login | Página `/auth` com e-mail/senha; `POST /api/v1/auth/login` (ou iniciar via `GET /api/v1/auth/login`) |
| Consumir identidade | Via `/api/v1/auth/me` |
| Renovação transparente | Interceptador HTTP aciona refresh quando Access Token expira |
| Proteger rotas | Guards baseados em estado autenticado |
| CSRF | Enviar token CSRF em requisições mutáveis |

**Proibições do Frontend:**

- Armazenar tokens em LocalStorage ou SessionStorage
- Acessar cookies HttpOnly via JavaScript
- Consultar Zimbra diretamente

---

# Diagrama Textual

```text
┌──────────┐         ┌─────────────────┐         ┌─────────┐
│ Frontend │         │ Portal (Backend) │         │ Zimbra  │
└────┬─────┘         └────────┬────────┘         └────┬────┘
     │                          │                       │
     │  POST /api/v1/auth/login │                       │
     │  (email, password, …)    │                       │
     │─────────────────────────►│                       │
     │                          │  IMAP / SMTP / SOAP   │
     │                          │──────────────────────►│  (única vez)
     │                          │◄── Identidade ────────│
     │                          │                       │
     │                          │ [Localiza/cria Colaborador]
     │                          │ [Emite JWT + Refresh]
     │                          │ [Registra sessão no BD]
     │                          │                       │
     │◄── 302 + Cookies HttpOnly│                       │
     │    (access + refresh)    │                       │
     │                          │                       │
     │  GET /api/v1/auth/me     │                       │
     │─────────────────────────►│  (valida JWT local)   │
     │◄── ApiResponse ──────────│  (permissões do BD)   │
     │                          │                       │
     │  POST /api/v1/auth/refresh│                      │
     │─────────────────────────►│  (valida refresh BD)  │
     │◄── Novo Access Token ────│                       │
     │                          │                       │
     │  POST /api/v1/auth/logout│                      │
     │─────────────────────────►│  (revoga refresh)    │
     │◄── Cookies removidos ────│                       │
```

Variantes de entrada (mesmo `finalizeLogin`):

- `GET /api/v1/auth/login` → 302 para página de login do Portal (`AUTH_LOGIN_PAGE_URL`) com `state`
- `GET /api/v1/auth/callback?token&state` → valida token opaco via SOAP `authToken`

---

# Sequência de Autenticação (Login)

```text
1. Usuário acessa a página de login do Portal (/auth)
2. Usuário informa e-mail corporativo e senha (opcional "Lembrar-me")
3. Frontend envia POST /api/v1/auth/login (form-urlencoded + CSRF; state opcional)
4. Backend valida state anti-CSRF quando presente
5. Backend autentica no Zimbra: IMAP → SMTP AUTH → SOAP (única consulta ao IdP)
6. Backend obtém identidade mínima (email, displayName, zimbraId)
7. Backend localiza ou cria Colaborador no banco
8. Backend verifica autorização para utilizar o Portal (ex.: colaborador ativo)
9. Backend verifica limite de sessões simultâneas (máx. 3)
10. Backend emite Access Token (JWT, 15 min) e Refresh Token (8h ou 30d)
11. Backend registra sessão no banco (session_id, device, refresh_token_hash)
12. Backend define Cookies HttpOnly + Secure
13. Backend redireciona Frontend para área autenticada (AUTH_FRONTEND_REDIRECT_URL)
14. Frontend consulta GET /api/v1/auth/me
```

---

# Sequência de Renovação (Refresh)

```text
1. Frontend detecta Access Token expirado (401 ou expiração iminente)
2. Frontend envia POST /api/v1/auth/refresh (cookie refresh_token + CSRF)
3. Backend valida Refresh Token no banco (não revogado, não expirado)
4. Backend emite novo Access Token (JWT, 15 min)
5. Backend atualiza cookie access_token
6. Backend registra evento de auditoria (renovação)
7. Frontend repete requisição original
```

Se Refresh Token expirado ou revogado: cookies removidos, redirecionamento para login.

---

# Sequência de Logout

```text
1. Usuário solicita logout
2. Frontend envia POST /api/v1/auth/logout (cookies + CSRF)
3. Backend revoga Refresh Token no banco
4. Backend remove cookies access_token e refresh_token
5. Backend registra evento de auditoria (logout)
6. Frontend redireciona para tela pública
```

---

# Gerenciamento de Sessão

## Modelo Stateless

O Portal **não utiliza HTTP Session** (Servlet Session). A sessão é representada por:

| Componente | Tipo | Armazenamento | TTL |
|------------|------|---------------|-----|
| Access Token | JWT assinado | Cookie HttpOnly | 15 minutos |
| Refresh Token | Token opaco (UUID) | Cookie HttpOnly + registro no BD | 8 horas (padrão) / 30 dias ("Lembrar-me") |
| Metadados de sessão | Registro relacional | Banco de dados do Portal | Vinculado ao Refresh Token |

## Estrutura do Access Token (JWT)

| Claim | Tipo | Descrição |
|-------|------|-----------|
| `sub` | string | Identificador do colaborador (`COD_COLABORADOR`) |
| `sid` | string | Identificador da sessão (`session_id`) |
| `email` | string | E-mail corporativo |
| `name` | string | Nome do colaborador |
| `iat` | number | Timestamp de emissão |
| `exp` | number | Timestamp de expiração (15 min) |

Claims opcionais de vínculos organizacionais do colaborador (`fid`, `singularId`, `areaId`, `teamId`) refletem `COLABORADOR` na emissão do token — não são persistidos em `AUTH_SESSAO`.

**O Access Token não contém permissões.** Permissões são carregadas do banco a cada requisição que exige autorização.

## Estrutura do Refresh Token

| Atributo | Descrição |
|----------|-----------|
| Valor | UUID v4 opaco (não é JWT) |
| Armazenamento | Hash no banco (`AUTH_SESSAO`) |
| Cookie | `refresh_token` — HttpOnly, Secure, SameSite=Strict |
| TTL padrão | 8 horas |
| TTL "Lembrar-me" | 30 dias |

## Cookies

| Cookie | Conteúdo | Flags | TTL |
|--------|----------|-------|-----|
| `access_token` | JWT | HttpOnly, Secure, SameSite=Strict, Path=/ | 15 min |
| `refresh_token` | UUID opaco | HttpOnly, Secure, SameSite=Strict, Path=/api/v1/auth | 8h / 30d |
| `XSRF-TOKEN` | Token CSRF | Secure, SameSite=Strict, Path=/ (não HttpOnly — legível pelo JS para envio em header) | Sessão |

## Política "Lembrar-me"

Quando o usuário habilita "Lembrar-me" no login:

- Refresh Token TTL estendido para **30 dias**
- Sessão registrada com flag `remember_me = true`
- Access Token TTL permanece **15 minutos** (renovação automática via Refresh)

## Sessões Simultâneas

- Máximo **3 dispositivos** ativos por colaborador
- Ao exceder o limite, a sessão mais antiga é automaticamente revogada
- Cada sessão identificada por `session_id` + metadados de dispositivo

## Invalidação

| Evento | Ação |
|--------|------|
| Logout | Revoga Refresh Token; remove cookies |
| Expiração Access Token | Renovado automaticamente via Refresh |
| Expiração Refresh Token | Remove cookies; exige novo login |
| Revogação administrativa | Invalida `session_id` no banco; próximo refresh falha |
| Limite de sessões | Revoga sessão mais antiga |

---

# Contrato de Integração com Zimbra

> **SSOT operacional:** detalhes de hosts, portas, ordem de tentativa e divergências D-01–D-07 em `docs/discovery/ft-auth-zimbra-homologacao.md`.

## Responsabilidade do Zimbra

```text
ZIMBRA-AUTH-001 — Validar credenciais do usuário
ZIMBRA-AUTH-002 — Retornar identidade mínima autenticada
```

## Dados Mínimos Retornados

| Campo | Obrigatório | Descrição |
|-------|-------------|-----------|
| `email` | Sim | E-mail corporativo do colaborador |
| `displayName` | Sim | Nome de exibição |
| `zimbraId` | Sim | Identificador único no Zimbra |

## Fluxo de Integração (Homologado)

```text
1. GET /api/v1/auth/login → 302 para página de login do Portal
   [AUTH_LOGIN_PAGE_URL / application.zimbra.login-page-url]

2. Usuário informa credenciais na página do Portal

3. POST /api/v1/auth/login → backend valida no Zimbra:
   IMAP (primário) → SMTP AUTH (fallback) → SOAP AuthRequest (fallback)

4. Após mail auth bem-sucedido, identidade mínima via SOAP
   (fallback local se SOAP falhar após mail auth)

5. Variante: GET /api/v1/auth/callback?token&state
   → SOAP AuthRequest com authToken opaco

6. Portal recebe dados mínimos de identidade e conclui sessão
```

**Obsoleto (não usar):** `ZIMBRA_AUTH_URL`, `ZIMBRA_VALIDATE_URL` e qualquer narrativa de OAuth/JSON fictício contra o Zimbra corporativo.

## Tratamento de Indisponibilidade

| Situação | Comportamento |
|----------|---------------|
| Zimbra indisponível no login | HTTP 503; mensagem ao usuário; nenhuma sessão criada |
| Timeout (padrão: 10 segundos) | HTTP 503; processo interrompido |
| Resposta inválida | HTTP 400; autenticação não concluída |
| Credenciais inválidas | Autenticação recusada pelo Zimbra; nenhuma sessão criada |

## Variáveis de Ambiente (Homologadas)

| Variável | Descrição |
|----------|-----------|
| `AUTH_LOGIN_PAGE_URL` | Página de login do Portal (redirect de `GET /auth/login`) |
| `ZIMBRA_IMAP_HOST` / `ZIMBRA_IMAP_PORT` / `ZIMBRA_IMAP_SSL` | Validação IMAP |
| `ZIMBRA_SMTP_HOST` / `ZIMBRA_SMTP_PORT` / … | Fallback SMTP AUTH |
| `ZIMBRA_SOAP_URL` | SOAP identidade / fallback / callback |
| `ZIMBRA_TIMEOUT_MS` | Timeout de comunicação (padrão: 10000) |
| `AUTH_FRONTEND_REDIRECT_URL` | Redirect pós-login |

---

# Política de Segurança

| Regra | Descrição |
|-------|-----------|
| SEC-001 | HTTPS obrigatório em todos os ambientes |
| SEC-002 | Tokens exclusivamente em Cookies HttpOnly + Secure |
| SEC-003 | Proibido LocalStorage/SessionStorage para tokens |
| SEC-004 | CSRF obrigatório em POST/PUT/DELETE autenticados por Cookie |
| SEC-005 | Access Token TTL fixo em 15 minutos |
| SEC-006 | Refresh Token revogável no banco |
| SEC-007 | Credenciais nunca persistidas pelo Portal |
| SEC-008 | Informações sensíveis nunca em logs |
| SEC-009 | JWT assinado com chave do Portal (não compartilhada com Zimbra) |
| SEC-010 | Máximo 3 sessões simultâneas por colaborador |

---

# Persistência

## Tabela AUTH_SESSAO (conceitual)

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| COD_SESSAO | NUMBER(19) | PK — identificador da sessão |
| COD_COLABORADOR | NUMBER(19) | FK — colaborador |
| HASH_REFRESH_TOKEN | VARCHAR2(255) | Hash do Refresh Token |
| DES_DISPOSITIVO | VARCHAR2(255) | Identificação do dispositivo |
| FLG_REMEMBER_ME | CHAR(1) | S/N |
| DAT_CRIACAO | TIMESTAMP(6) | Criação |
| DAT_EXPIRACAO | TIMESTAMP(6) | Expiração do Refresh |
| FLG_REVOGADA | CHAR(1) | S/N |
| DAT_REVOGACAO | TIMESTAMP(6) | Data de revogação |

DDL executável será definida na implementação conforme `docs/implementation/06-database-standards.md`.

---

# Decisões Arquiteturais

| ID | Decisão | Referência |
|----|---------|------------|
| DA-AUTH-005 | Arquitetura Stateless (sem HTTP Session) | `decisions.md` |
| DA-AUTH-006 | JWT próprio do Portal | `decisions.md` |
| DA-AUTH-007 | Refresh Token com Cookies HttpOnly | `decisions.md` |
| DA-AUTH-008 | Consulta única ao Zimbra (apenas no login) | `decisions.md` |
| DA-AUTH-009 | Permissões mantidas pelo banco do Portal | `decisions.md` |
| DA-AUTH-010 | Sessões simultâneas limitadas (máx. 3) | `decisions.md` |
| DA-AUTH-012 | Protocolo Zimbra = proxy de credenciais IMAP/SMTP/SOAP | `decisions.md` |

---

# Referências

- `specs/features/authentication/specification.md`
- `specs/features/authentication/api.md`
- `specs/features/authentication/decisions.md`
- `docs/discovery/ft-auth-zimbra-homologacao.md`
- `docs/architecture/06-security-architecture.md`
- `docs/architecture/08-decision-records.md` (ADR-003)
- `docs/implementation/07-api-standards.md`

---

# Histórico

| Data | Descrição |
|------|-----------|
| 2026-07-08 | v1.0 — Arquitetura Stateless, JWT, contrato Zimbra abstrato |
| 2026-07-24 | v1.1 — Alinhamento ao protocolo homologado (proxy IMAP/SMTP/SOAP); remoção de narrativa OAuth fictícia |
