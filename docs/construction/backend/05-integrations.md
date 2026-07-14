# Integrations

**Fonte normativa MVP:** `docs/audit/10-mvp-consolidation-audit.md`  
**Pré-requisito:** `docs/governance/history/phase1-frontend-construction-report.md`

## Objetivo

Definir os padrões arquiteturais, técnicos e operacionais para implementação das integrações do Portal de Comunicação com sistemas externos.

Este documento estabelece como serviços externos devem ser consumidos, monitorados, protegidos e evoluídos ao longo do ciclo de vida da solução.

---

# Escopo

Esta documentação cobre:

* APIs REST externas
* Webhooks
* Mensageria
* Integrações síncronas
* Integrações assíncronas
* Tratamento de falhas
* Resiliência
* Observabilidade
* Segurança de integrações

Não cobre:

* Regras de negócio
* Persistência
* APIs expostas pelo sistema

---

# Princípios Arquiteturais

Toda integração deve seguir os princípios:

* Baixo acoplamento
* Alta observabilidade
* Resiliência
* Idempotência
* Segurança
* Rastreabilidade

O domínio nunca deve conhecer detalhes de integração.

---

# Arquitetura de Integração

Fluxo recomendado:

```text
Application Service
        │
        ▼
Integration Gateway
        │
        ▼
External Client
        │
        ▼
External System
```

---

# Estrutura de Diretórios

```text
infrastructure
└── integration
    ├── client
    ├── gateway
    ├── dto
    ├── mapper
    ├── configuration
    ├── exception
    └── webhook
```

---

# Padrão Gateway

Toda integração deve ser abstraída por um Gateway.

---

## Exemplo

```java
public interface NotificationGateway {

    NotificationResult send(
            NotificationCommand command);

}
```

---

## Implementação

```java
@Component
@RequiredArgsConstructor
public class NotificationGatewayImpl
        implements NotificationGateway {

    private final NotificationClient client;

}
```

---

# Clients HTTP

Todos os acessos externos devem ser centralizados.

---

## Permitido

* RestClient
* WebClient

---

## Não Permitido

* HTTP calls diretamente em Services
* HTTP calls em Controllers
* HTTP calls em Repositories

---

# Configuração do Client

```java
@Configuration
public class RestClientConfiguration {

    @Bean
    RestClient restClient(
            RestClient.Builder builder) {

        return builder.build();
    }

}
```

---

# DTOs de Integração

Nunca reutilizar DTOs da API pública.

---

## Correto

```text
integration
├── dto
│   ├── request
│   └── response
```

---

## Exemplo

```java
public record ExternalMessageRequest(
        String title,
        String content
) {}
```

---

# Timeout

Toda integração deve possuir timeout explícito.

---

## Padrão

| Tipo          | Timeout     |
| ------------- | ----------- |
| Consulta      | 5 segundos  |
| Escrita       | 10 segundos |
| Processamento | 30 segundos |

---

# Retry

Toda operação recuperável deve utilizar retry controlado.

---

## Exemplos

Permitido:

* Timeout
* Erro temporário
* HTTP 429
* HTTP 503

Não permitido:

* HTTP 400
* HTTP 401
* HTTP 403
* HTTP 404

---

## Exemplo

```java
@Retryable(
    maxAttempts = 3
)
```

---

# Circuit Breaker

Integrações críticas devem possuir proteção contra cascata de falhas.

---

## Objetivos

Evitar:

* Saturação do sistema
* Tempestade de requisições
* Queda em cadeia

---

## Ferramenta

Resilience4j

---

## Exemplo

```java
@CircuitBreaker(
    name = "notification-api"
)
```

---

# Bulkhead

Integrações críticas devem ser isoladas.

---

## Exemplo

```java
@Bulkhead(
    name = "notification-api"
)
```

---

# Rate Limiting

Sistemas externos com restrição de consumo devem possuir limitação local.

---

## Exemplo

```java
@RateLimiter(
    name = "notification-api"
)
```

---

# Idempotência

Toda operação de criação externa deve suportar idempotência.

---

## Exemplo

```http
Idempotency-Key
```

---

# Versionamento

Integrações devem ser versionadas.

---

## Exemplo

```text
v1
v2
```

Mudanças incompatíveis devem gerar nova versão.

---

# Mapeamento

MapStruct deve ser utilizado para transformação.

---

## Exemplo

```java
@Mapper(componentModel = "spring")
public interface NotificationMapper {

}
```

---

# Tratamento de Erros

Nunca propagar exceções técnicas diretamente.

---

## Correto

```java
throw new NotificationUnavailableException();
```

---

## Incorreto

```java
throw new HttpServerErrorException();
```

---

# Exceções

Criar exceções específicas por integração.

---

## Exemplo

```java
NotificationTimeoutException

NotificationUnavailableException

NotificationAuthenticationException
```

---

# Webhooks

Integrações baseadas em eventos externos devem utilizar endpoints dedicados.

---

## Estrutura

```text
interfaces
└── webhook
```

---

## Exemplo

```http
POST /webhooks/notifications
```

---

## Obsoleto (fora do MVP)

> Não implementar — removidos por `docs/audit/10-mvp-consolidation-audit.md`.

```http
POST /webhooks/messages
```

---

# Segurança de Webhooks

Obrigatório validar:

* Assinatura
* Origem
* Timestamp
* Replay attack

---

# Mensageria

Quando aplicável, utilizar eventos assíncronos.

---

## Casos Recomendados

* Notificações
* Processamentos longos
* Integrações desacopladas

---

# Eventos

Padrão MVP:

```text
ComunicadoCreated

DocumentPublished

NotificationDelivered
```

---

## Obsoleto (fora do MVP)

> Não implementar — removidos por `docs/audit/10-mvp-consolidation-audit.md`.

```text
CampaignStarted
MessageSent
CommunicationCreated
```

---

# Observabilidade

Toda integração deve gerar logs estruturados.

---

## Registrar

* Correlation Id
* Endpoint externo
* Tempo de resposta
* Status HTTP
* Tentativas

---

## Nunca Registrar

* Senhas
* Tokens
* Segredos
* Dados pessoais sensíveis

---

# Correlation ID

Obrigatório propagar.

---

## Header

```http
X-Correlation-Id
```

---

# Métricas

Registrar:

* Quantidade de chamadas
* Latência
* Falhas
* Retries
* Circuit Breaker Open

---

## Exemplos

```text
integration_requests_total

integration_errors_total

integration_latency_ms
```

---

# Health Checks

Integrações críticas devem possuir monitoramento.

---

## Exemplo

```java
HealthIndicator
```

---

# Configurações

Toda configuração deve ser externa.

---

## application.yml

```yaml
integration:
  notification:
    url: ${NOTIFICATION_URL}
```

---

# Secrets

Nunca armazenar:

* Tokens
* Senhas
* Chaves

Em código-fonte.

---

## Utilizar

```text
Vault

Secrets Manager

Environment Variables
```

---

# Testes

---

## Unitários

Mockar dependências externas.

---

## Integração

Utilizar:

```text
WireMock
```

---

## Contrato

Validar compatibilidade entre sistemas.

---

# Estratégia de Evolução

Mudanças em integrações devem seguir:

1. Nova versão do contrato.
2. Compatibilidade retroativa.
3. Período de coexistência.
4. Remoção controlada.

---

# Checklist de Implementação

Antes de publicar uma integração verificar:

* [ ] Gateway criado
* [ ] Client implementado
* [ ] DTOs separados
* [ ] Timeout configurado
* [ ] Retry configurado
* [ ] Circuit Breaker configurado
* [ ] Logs implementados
* [ ] Métricas implementadas
* [ ] Health Check implementado
* [ ] Testes unitários criados
* [ ] Testes de integração criados
* [ ] Segredos externalizados

---

# Critérios de Aceite

Uma integração será considerada aderente quando:

* Estiver desacoplada do domínio.
* Possuir Gateway dedicado.
* Possuir timeout explícito.
* Possuir tratamento de falhas.
* Possuir retry controlado.
* Possuir observabilidade completa.
* Possuir rastreabilidade por Correlation ID.
* Possuir testes automatizados.
* Não expor detalhes técnicos para camadas superiores.
* Seguir os padrões definidos na camada Architecture e Solution Design.
