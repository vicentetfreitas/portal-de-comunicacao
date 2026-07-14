# Security Implementation

## Objetivo

Definir os padrões, controles e práticas de segurança obrigatórios para implementação do backend do Portal de Comunicação.

Este documento estabelece os requisitos mínimos de segurança que devem ser aplicados durante todo o ciclo de desenvolvimento, implantação e operação da solução.

---

# Escopo

Esta documentação cobre:

* Autenticação
* Autorização
* Segurança de APIs
* Proteção de dados
* Gestão de segredos
* Auditoria
* Criptografia
* Segurança de integrações
* Segurança de infraestrutura
* Segurança de desenvolvimento

Não cobre:

* Políticas corporativas organizacionais
* Segurança física
* Segurança de estações de trabalho

---

# Princípios de Segurança

Toda implementação deve seguir os princípios:

* Least Privilege
* Defense in Depth
* Zero Trust
* Secure by Default
* Secure by Design
* Fail Securely
* Separation of Duties

---

# Arquitetura de Segurança

```text
Client
   │
   ▼
API Gateway
   │
   ▼
Authentication Layer
   │
   ▼
Authorization Layer
   │
   ▼
Application Services
   │
   ▼
Domain
```

---

# Classificação de Dados

## Público

Dados sem restrição.

Exemplos:

* Conteúdo institucional
* Informações públicas

---

## Interno

Dados acessíveis apenas internamente.

Exemplos:

* Configurações
* Logs operacionais

---

## Confidencial

Dados restritos.

Exemplos:

* Usuários
* Perfis
* Configurações administrativas

---

## Sensível

Dados protegidos por legislação.

Exemplos:

* Dados pessoais
* Dados de identificação
* Informações sujeitas à LGPD

---

# Autenticação

## Padrão Corporativo

Utilizar:

* OAuth2
* OpenID Connect
* JWT

---

## Não Permitido

* Basic Authentication em produção
* Usuários hardcoded
* Tokens estáticos

---

# JWT

Todo token deve possuir:

```json
{
  "sub": "user-id",
  "iat": 1700000000,
  "exp": 1700003600,
  "roles": ["ADMIN"]
}
```

---

## Requisitos

Obrigatório validar:

* Assinatura
* Expiração
* Audience
* Issuer

---

# Tempo de Expiração

| Tipo          | Duração                       |
| ------------- | ----------------------------- |
| Access Token  | 15 minutos                    |
| Refresh Token | 8 horas                       |
| API Token     | Conforme política corporativa |

---

# Autorização

## Modelo

RBAC

Role Based Access Control

---

## Exemplo

```text
ADMIN

MANAGER

OPERATOR

VIEWER
```

---

# Regras

Autorização deve ocorrer:

* Na API
* Na aplicação
* Em operações críticas

---

## Exemplo

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

# Segurança de APIs

Toda API deve possuir:

* HTTPS
* JWT
* Rate Limiting
* Audit Trail
* Correlation ID

---

# Headers Obrigatórios

```http
Strict-Transport-Security
```

```http
X-Content-Type-Options
```

```http
X-Frame-Options
```

```http
Referrer-Policy
```

```http
Content-Security-Policy
```

---

# CORS

Restringir origens autorizadas.

---

## Correto

```yaml
allowed-origins:
  - https://portal.com.br
```

---

## Incorreto

```yaml
allowed-origins:
  - "*"
```

---

# Rate Limiting

Proteção obrigatória contra abuso.

---

## Exemplo

```text
100 requisições/minuto
```

por usuário.

---

# Validação de Entrada

Toda entrada deve ser validada.

---

## Utilizar

```java
@NotNull

@NotBlank

@Size

@Pattern
```

---

# Nunca Confiar

* Query Parameters
* Headers
* Body
* Cookies

---

# OWASP Top 10

Toda implementação deve mitigar:

---

## Broken Access Control

Validar permissões.

---

## Cryptographic Failures

Criptografia adequada.

---

## Injection

Utilizar:

* JPA
* Prepared Statements

Nunca concatenar SQL.

---

## Insecure Design

Aplicar Security by Design.

---

## Security Misconfiguration

Configurações seguras por padrão.

---

## Vulnerable Components

Dependências monitoradas.

---

## Authentication Failures

JWT e OAuth2.

---

## Integrity Failures

Assinaturas e validações.

---

## Logging Failures

Auditoria adequada.

---

## SSRF

Restringir integrações externas.

---

# Proteção Contra SQL Injection

Permitido:

```java
repository.findById(id);
```

---

Não permitido:

```java
String sql =
 "select * from users where id = "
 + id;
```

---

# Proteção Contra XSS

Escapar conteúdo exibido.

Validar entradas.

Sanitizar conteúdo rico.

---

# Proteção Contra CSRF

Obrigatória para aplicações baseadas em sessão.

Para APIs JWT stateless:

```java
csrf.disable();
```

quando tecnicamente justificável.

---

# Criptografia

## Em Trânsito

Obrigatório:

```text
TLS 1.2+
```

Preferencial:

```text
TLS 1.3
```

---

## Em Repouso

Dados sensíveis devem ser criptografados.

---

## Algoritmos Permitidos

Hash:

```text
BCrypt
Argon2
```

---

Criptografia:

```text
AES-256
```

---

Assinaturas:

```text
RSA-2048+
ECDSA
```

---

# Gestão de Senhas

Nunca armazenar:

```text
Texto puro
```

---

Sempre utilizar:

```text
BCrypt
```

ou

```text
Argon2
```

---

# Gestão de Segredos

Segredos nunca devem estar:

* No código
* No Git
* Em arquivos compartilhados

---

## Utilizar

```text
Vault
```

```text
AWS Secrets Manager
```

```text
Azure Key Vault
```

```text
Environment Variables
```

---

# Auditoria

Eventos críticos devem ser auditados.

---

## Registrar

* Login
* Logout
* Criação
* Alteração
* Exclusão
* Mudança de perfil
* Falhas de autenticação

---

# Formato

```json
{
  "user": "123",
  "action": "CREATE_COMUNICADO",
  "timestamp": "2026-01-01T10:00:00Z"
}
```

---

# Logs

Logs devem ser estruturados.

---

## Registrar

* Correlation ID
* Usuário
* Operação
* Resultado

---

## Nunca Registrar

* Senhas
* JWT
* Access Tokens
* Refresh Tokens
* Chaves privadas

---

# LGPD

Todos os componentes devem respeitar:

* Finalidade
* Necessidade
* Transparência
* Segurança
* Prestação de contas

---

# Dados Pessoais

Minimizar coleta.

Coletar apenas o necessário.

---

# Exclusão

Dados devem suportar:

* Anonimização
* Exclusão lógica
* Exclusão definitiva

conforme regra de negócio.

---

# Segurança de Integrações

Toda integração externa deve possuir:

* HTTPS
* Timeout
* Retry controlado
* Circuit Breaker
* Autenticação

---

# Segurança de Webhooks

Obrigatório validar:

* Assinatura
* Origem
* Timestamp
* Replay Attack

---

# Dependências

Todas as dependências devem ser monitoradas.

---

## Ferramentas

* OWASP Dependency Check
* Snyk
* Dependabot

---

# Segurança de Containers

Imagens devem:

* Ser minimalistas
* Ser atualizadas
* Não executar como root

---

## Obrigatório

```dockerfile
USER appuser
```

---

# Segurança de Banco

Obrigatório:

* Usuários distintos por ambiente
* Menor privilégio possível
* Criptografia de conexão

---

# Segurança de Build

Pipeline deve executar:

* SAST
* Dependency Scan
* Secret Scan

---

# Segurança de Deploy

Obrigatório:

* Aprovação controlada
* Logs de auditoria
* Rollback documentado

---

# Security Testing

Executar:

* Unit Security Tests
* Integration Security Tests
* Vulnerability Scans

---

# Checklist de Segurança

Antes de publicar uma release:

* [ ] HTTPS habilitado
* [ ] JWT validado
* [ ] Roles configuradas
* [ ] Rate Limit aplicado
* [ ] CORS restritivo
* [ ] Headers de segurança configurados
* [ ] Logs auditáveis
* [ ] Dados sensíveis protegidos
* [ ] Secrets externalizados
* [ ] Dependências verificadas
* [ ] Vulnerability Scan executado
* [ ] Testes de segurança aprovados

---

# Critérios de Aceite

A implementação será considerada aderente quando:

* Utilizar autenticação centralizada.
* Possuir autorização baseada em papéis.
* Implementar proteção contra OWASP Top 10.
* Possuir criptografia adequada.
* Proteger dados sensíveis.
* Possuir trilha de auditoria.
* Possuir gestão segura de segredos.
* Atender aos requisitos da LGPD.
* Possuir validação automatizada no pipeline.
* Estar alinhada com Architecture, Solution Design e Construction Standards.
