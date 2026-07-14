# Authentication

## Objetivo

Definir os padrões de autenticação, gerenciamento de sessão, autorização e proteção de acesso do frontend do Portal de Comunicação.

Este documento estabelece os mecanismos necessários para garantir acesso seguro à aplicação, protegendo recursos, usuários e informações sensíveis.

**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado Fase 1 Frontend em 2026-06-22

---

# Escopo

Esta documentação cobre:

* Login
* Logout
* Sessão
* JWT
* OAuth2
* OpenID Connect
* Refresh Token
* Middleware
* Proteção de rotas
* Controle de permissões
* RBAC

Não cobre:

* Segurança do backend
* Criptografia de banco
* Gestão de identidade corporativa

---

# Princípios

Toda implementação deve seguir:

* Zero Trust
* Least Privilege
* Secure by Default
* Defense in Depth
* Session Security

---

# Arquitetura

```text id="x3rlv7"
Frontend
    │
    ▼
Identity Provider
    │
    ▼
Access Token
    │
    ▼
Backend APIs
```

---

# Padrão Oficial

Utilizar:

```text id="h3ggz7"
OAuth2
```

e

```text id="53l4rq"
OpenID Connect (OIDC)
```

---

# Não Permitido

* Autenticação customizada
* Senhas armazenadas localmente
* Tokens persistidos em localStorage
* Sessões não expiradas

---

# Fluxo de Autenticação

```text id="d5zqkp"
Usuário
   │
   ▼
Login
   │
   ▼
Identity Provider
   │
   ▼
Access Token
   │
   ▼
Frontend
   │
   ▼
Backend API
```

---

# Identity Provider

A solução deve ser compatível com:

```text id="zv8l6x"
Keycloak
```

---

```text id="p0d7n6"
Azure AD
```

---

```text id="xj4o7j"
Auth0
```

---

```text id="7z7s8o"
AWS Cognito
```

---

# Tokens

## Access Token

Responsável por autenticação das APIs.

---

## Refresh Token

Responsável pela renovação da sessão.

---

# Estrutura JWT

Exemplo:

```json id="8z7m3v"
{
  "sub": "123",
  "email": "user@portal.com",
  "roles": [
    "ADMIN"
  ],
  "exp": 9999999999
}
```

---

# Claims Obrigatórias

```text id="hkl4h7"
sub

exp

iat

iss

aud

roles
```

---

# Armazenamento de Tokens

## Obrigatório

Utilizar:

```text id="k7m5zh"
HttpOnly Cookie
```

---

## Não Permitido

```javascript id="zzg4lb"
localStorage
```

---

```javascript id="t4sm85"
sessionStorage
```

para tokens de autenticação.

---

# Cookies

Configuração obrigatória:

```text id="wv0jdb"
HttpOnly

Secure

SameSite
```

---

# Sessão

A sessão deve ser baseada em token.

---

# Tempo de Vida

## Access Token

```text id="c75o3n"
15 minutos
```

---

## Refresh Token

```text id="tq4tfc"
8 horas
```

---

# Renovação Automática

A aplicação deve renovar tokens automaticamente.

---

# Fluxo

```text id="59h5pp"
Access Token Expirado
          │
          ▼
Refresh Token
          │
          ▼
Novo Access Token
```

---

# Logout

O logout deve:

1. Invalidar sessão.
2. Remover cookies.
3. Encerrar contexto do usuário.
4. Redirecionar para login.

---

# Fluxo

```text id="f6gs1m"
Logout
   │
   ▼
Revogar Token
   │
   ▼
Limpar Sessão
   │
   ▼
Login
```

---

# Estrutura de Diretórios

```text id="l2k8dx"
src
├── authentication
│   ├── hooks
│   ├── services
│   ├── providers
│   ├── guards
│   ├── middleware
│   └── types
```

---

# Authentication Service

Responsável por:

* Login
* Logout
* Refresh
* Recuperação de sessão

---

# Exemplo

```typescript id="d3br9i"
auth.service.ts
```

---

# Session Provider

Responsável por disponibilizar:

```text id="m1kj3q"
User

Roles

Permissions

Tenant
```

---

# Exemplo

```typescript id="ibjvho"
SessionProvider
```

---

# Auth Hook

Exemplo:

```typescript id="5njlwm"
useAuth()
```

---

# Retorno

```typescript id="wckjgo"
user

roles

isAuthenticated

logout()
```

---

# Middleware

Toda rota protegida deve ser validada.

---

# Responsabilidades

* Verificar sessão
* Validar token
* Redirecionar login
* Bloquear acesso indevido

---

# Fluxo

```text id="prpcaj"
Request
   │
   ▼
Middleware
   │
   ▼
Authenticated?
   │
 ┌─┴─┐
 │   │
Sim Não
 │   │
 ▼   ▼
Página Login
```

---

# Rotas Públicas

Exemplos:

```text id="qulm8i"
/login

/forgot-password

/reset-password
```

---

# Rotas Protegidas

Exemplos:

```text id="7kef8y"
/dashboard

/comunicados

/notifications

/documents

/settings
```

**Rastreabilidade:** FEATURE-041, FEATURE-040, FEATURE-030 (`docs/audit/10-mvp-consolidation-audit.md`).

---

## Obsoleto (fora do MVP)

> Não proteger nem implementar.

```text
/campaigns
/messages
```

---

# Autorização

Autenticação não substitui autorização.

---

# Modelo

RBAC

Role Based Access Control

---

# Perfis

```text id="f6v0ih"
ADMIN

MANAGER

OPERATOR

VIEWER
```

---

# Exemplo

| Recurso        | VIEWER | OPERATOR | MANAGER | ADMIN |
| -------------- | ------ | -------- | ------- | ----- |
| Dashboard      | ✔      | ✔        | ✔       | ✔     |
| Comunicados    | ✔      | ✔        | ✔       | ✔     |
| Notificações   | ✔      | ✔        | ✔       | ✔     |
| Documentos     | ✔      | ✔        | ✔       | ✔     |
| Administração  | ✖      | ✖        | ✖       | ✔     |

---

# Route Guards

Implementar proteção baseada em perfil.

---

# Exemplo

```typescript id="7drwq5"
<RoleGuard role="ADMIN">
```

---

# Component Guards

Proteção também deve ocorrer na interface.

---

## Exemplo

```typescript id="k6rl5o"
<Can permission="comunicado:create">
```

---

# Permissões

Modelo recomendado:

```text id="pxlz0m"
comunicado:view

comunicado:create

comunicado:update

comunicado:delete

notification:view

document:view

document:upload
```

---

# Contexto do Usuário

Disponibilizar:

```typescript id="aw7vkn"
id

name

email

roles

permissions
```

---

# Recuperação de Sessão

Ao abrir a aplicação:

```text id="yz54kp"
Verificar Cookie

Recuperar Usuário

Restaurar Sessão
```

---

# Expiração

Sessões expiradas devem:

* Encerrar contexto
* Limpar cache
* Redirecionar login

---

# Tratamento de Erros

## 401

Usuário não autenticado.

---

## 403

Usuário sem permissão.

---

## Fluxo

```text id="dgkv4v"
401 → Login

403 → Unauthorized
```

---

# Página Unauthorized

```text id="6nzv3m"
/unauthorized
```

---

# Integração com API Client

Todos os requests devem enviar:

```http id="mlk3w6"
Authorization: Bearer TOKEN
```

---

# Interceptors

Responsáveis por:

* Incluir token
* Renovar sessão
* Tratar 401
* Tratar 403

---

# Segurança

Nunca registrar:

* Senhas
* Access Token
* Refresh Token

---

# Observabilidade

Registrar:

* Login
* Logout
* Falhas de autenticação
* Expiração de sessão
* Renovação de token

---

# Eventos

```text id="vwz3hj"
LOGIN_SUCCESS

LOGIN_FAILURE

LOGOUT

TOKEN_REFRESH
```

---

# Testes

Validar:

* Login
* Logout
* Refresh Token
* Middleware
* Guards
* Expiração
* Permissões

---

# Testes Automatizados

Cobrir:

```text id="w4n2xm"
SessionProvider

useAuth

Route Guards

Middleware

Auth Service
```

---

# Checklist

Antes de publicar:

* [ ] OAuth2 configurado
* [ ] OIDC configurado
* [ ] JWT validado
* [ ] Cookies seguros configurados
* [ ] Middleware implementado
* [ ] Guards implementados
* [ ] RBAC implementado
* [ ] Refresh Token implementado
* [ ] Logout implementado
* [ ] Testes implementados

---

# Critérios de Aceite

A implementação será considerada aderente quando:

* Utilizar OAuth2/OIDC.
* Utilizar JWT.
* Armazenar tokens de forma segura.
* Possuir renovação automática de sessão.
* Proteger rotas autenticadas.
* Implementar RBAC.
* Possuir tratamento de expiração.
* Possuir observabilidade dos eventos de autenticação.
* Atender aos requisitos de segurança definidos na camada Backend.
* Seguir integralmente os padrões estabelecidos neste documento.
