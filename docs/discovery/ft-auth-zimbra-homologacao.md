# FT-AUTH — Homologação do Login com Zimbra

| Item | Valor |
|------|-------|
| Feature | FT-AUTH |
| Data | 2026-07-20 |
| Ambiente | Corporativo (`mail-app.unimedceara.com.br`) |
| Referência legado | `docs/discovery/05-current-integrations.md` (Zimbra IMAP/SMTP/SOAP) |
| Papel documental | **SSOT operacional** do protocolo de integração Zimbra (DA-AUTH-012) |

> A arquitetura normativa (`specs/architecture/authentication-architecture.md`) referencia este documento para hosts, ordem de tentativa e variáveis de ambiente. Não duplicar o protocolo em outros artefatos.

---

## Objetivo

Permitir que colaborador com credenciais válidas no Zimbra autentique no Portal, com perfil carregado do Oracle, emissão de JWT, sessão e acesso às rotas protegidas — **sem alterar contratos públicos da API, JWT, sessão ou autorização**.

---

## Estratégia homologada

### Validação de credenciais (única responsabilidade do Zimbra)

Ordem de tentativa (alinhada ao legado `ZimbraAuth.php`):

1. **IMAP** (`application.zimbra.imap-*`) — primária.
2. **SMTP AUTH** (`application.zimbra.smtp-*`) — fallback.
3. **SOAP `AuthRequest`** (`application.zimbra.soap-url`) — fallback quando mail não autentica.

Após autenticação IMAP/SMTP bem-sucedida, a identidade mínima (`email`, `displayName`, `zimbraId`) é obtida via SOAP; se SOAP falhar, usa-se fallback com e-mail autenticado.

### Início de login (`GET /api/v1/auth/login`)

Redireciona para a **página de login do Portal** (`application.zimbra.login-page-url` / `AUTH_LOGIN_PAGE_URL`), com `state` anti-CSRF e `callback` — **não** para OAuth/JSON fictício (`auth-url` / `validate-url`).

### Login com credenciais (`POST /api/v1/auth/login`)

Formulário do frontend envia `email`, `password`, `remember_me` e `state` opcional; backend valida no Zimbra pela estratégia acima e conclui o fluxo (cookies + redirect ao frontend).

### Callback (`GET /api/v1/auth/callback`)

Compatibilidade mantida: token opaco validado via SOAP `AuthRequest` com `authToken`.

### Pós-autenticação (Portal)

- Colaborador localizado ou criado no **Oracle** (`ColaboradorService`).
- JWT e Refresh Token emitidos; sessão em `AUTH_SESSAO`.
- Permissões carregadas do banco (não do Zimbra).
- Zimbra **não** é consultado após o login.

---

## Configuração canônica

| Propriedade | Variável de ambiente | Uso |
|-------------|----------------------|-----|
| `application.zimbra.login-page-url` | `AUTH_LOGIN_PAGE_URL` | Redirect do `GET /auth/login` |
| `application.zimbra.imap-host` | `ZIMBRA_IMAP_HOST` | Validação IMAP |
| `application.zimbra.imap-port` | `ZIMBRA_IMAP_PORT` | Porta IMAP |
| `application.zimbra.imap-ssl` | `ZIMBRA_IMAP_SSL` | IMAPS |
| `application.zimbra.smtp-host` | `ZIMBRA_SMTP_HOST` | Fallback SMTP |
| `application.zimbra.smtp-port` | `ZIMBRA_SMTP_PORT` | Porta SMTP |
| `application.zimbra.smtp-ssl` | `ZIMBRA_SMTP_SSL` | SSL SMTP |
| `application.zimbra.smtp-starttls` | `ZIMBRA_SMTP_STARTTLS` | STARTTLS |
| `application.zimbra.soap-url` | `ZIMBRA_SOAP_URL` | SOAP identidade / fallback auth |
| `application.zimbra.timeout-ms` | `ZIMBRA_TIMEOUT_MS` | Timeout IMAP/SMTP/HTTP (padrão 10000) |
| `application.auth.frontend-redirect-url` | `AUTH_FRONTEND_REDIRECT_URL` | Redirect pós-login |

**Removidas / obsoletas:** `application.zimbra.auth-url`, `application.zimbra.validate-url`, `ZIMBRA_AUTH_URL`, `ZIMBRA_VALIDATE_URL` (não existem no Zimbra corporativo homologado).

---

## Divergências identificadas e correções

| ID | Divergência | Correção |
|----|-------------|----------|
| D-01 | Cliente `ZimbraIdentityProviderClient` com `authUrl`/`validateUrl` JSON inexistente no Zimbra | Removido; substituído por `ZimbraCredentialValidator` + estratégias IMAP/SMTP/SOAP |
| D-02 | `ZimbraProperties` exigia URLs OAuth fictícias | Mantidos apenas `login-page-url`, IMAP, SMTP, SOAP e timeout |
| D-03 | `GET /auth/login` redirecionava para URL externa incorreta | `ZimbraIdentityProviderAdapter` usa `login-page-url` |
| D-04 | Sem validação real de credenciais (apenas mock HTTP) | `ZimbraImapAuthenticationStrategy`, `ZimbraSmtpAuthenticationStrategy`, `ZimbraSoapIdentityResolver` |
| D-05 | Callback com token opaco não implementado em produção | `validateOpaqueToken` → SOAP `authToken` |
| D-06 | Timeout de integração duplicado / inconsistente | `RestClient` usa `application.zimbra.timeout-ms` (RNF-AUTH-006) |
| D-07 | Frontend sem POST de credenciais | `AuthApiService.submitCredentials` + página de login Figma |

---

## Critérios de aceite homologados

- [x] Credenciais válidas no Zimbra autenticam via IMAP (ou fallback).
- [x] Colaborador resolvido no Oracle após identidade Zimbra.
- [x] JWT e sessão criados; cookies HttpOnly.
- [x] `GET /auth/me` e rotas protegidas após login.
- [x] Indisponibilidade Zimbra → HTTP 503 nos fluxos de login.
- [x] Contratos `/api/v1/auth/*` preservados.

---

## Evidências de teste

Executar no backend:

```bash
mvn test -Dtest=AuthAcceptanceIntegrationTest,AuthFlowIntegrationTest
```

Cenários cobertos: AC-AUTH-001 a AC-AUTH-014 (mocks em perfil `test`), POST `/auth/login` com credenciais, fluxo callback/me/refresh/logout.

---

## Diagnóstico — HTTP 403 no login

| Causa raiz | Resposta API | Mensagem ao usuário | Ação |
|------------|--------------|---------------------|------|
| CSRF ausente no `POST /auth/login` | 403 (sem `ErrorResponse` JSON) | Genérica de permissão (evitar) | Recarregar `/auth`; frontend primará token via `GET /auth/me` antes do POST |
| Colaborador inativo no Portal (`FLG_ATIVO = N`) após Zimbra OK | 403 `error: FORBIDDEN` | Cadastro não autorizado — contatar administrador | Ativar colaborador no Oracle ou cadastrar via FT-COLABORADOR |
| `AUTH_FRONTEND_REDIRECT_URL` / CORS desalinhados com porta do dev server (9000) | Login aparente com falha pós-redirect | Sessão não estabelecida | Alinhar `.env`: `AUTH_FRONTEND_REDIRECT_URL=http://localhost:9000/app`; CORS inclui `http://localhost:9000` |
| `ZIMBRA_IMAP_HOST` incorreto | 401 ou 503 (não 403) | Credenciais inválidas ou serviço indisponível | Usar `mail-app.unimedceara.com.br` (mesmo host SMTP/SOAP) |

**Validação manual com usuário Zimbra válido**

1. Backend em `8080`, frontend Quasar em `9000`, Oracle provisionado (`dml/001-federacao.sql` executado).
2. `POST /api/v1/auth/login` com `email`, `password`, `remember_me` e header `X-XSRF-TOKEN` (cookie `XSRF-TOKEN` obtido em `GET /auth/me`).
3. Esperado: HTTP 302 → `Location: http://localhost:9000/app` e cookies `access_token` / `refresh_token`.
4. `GET /api/v1/auth/me` com cookies → HTTP 200 e `ApiResponse` com e-mail do colaborador.
