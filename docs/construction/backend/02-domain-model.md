# Domain Model

**Fonte normativa MVP:** `docs/audit/10-mvp-consolidation-audit.md`  
**Pré-requisito:** `docs/governance/history/phase1-frontend-construction-report.md`

## Objetivo

Definir a implementação concreta dos modelos de domínio aprovados na camada Domain.

---

# Princípios

O domínio deve ser:

* Independente de frameworks
* Independente de banco
* Independente de APIs

---

# Estrutura

```text
domain
├── model
├── valueobject
├── event
├── service
├── repository
└── exception
```

---

# Entidades

Devem possuir:

* Identidade própria
* Regras de negócio
* Consistência interna

Exemplo:

```java
public class Comunicado {
    private ComunicadoId id;
    private String title;
}
```

---

# Value Objects

Imutáveis.

Sem identidade.

Exemplo:

```java
public record Email(String value) {}
```

---

# Domain Services

Devem conter regras que envolvem múltiplas entidades.

---

# Domain Events

Eventos relevantes do negócio.

Exemplos MVP:

* ComunicadoCreated
* DocumentPublished
* NotificationDelivered

---

## Obsoleto (fora do MVP)

> Não implementar — removidos por `docs/audit/10-mvp-consolidation-audit.md`.

* CampaignFinished
* MessagePublished
* CampaignStarted
* MessageSent

---

# Regras

Não utilizar:

* EntityManager
* RestClient
* Controllers
* Framework annotations

Dentro do domínio.

---

# Critérios de Aceite

* Domínio isolado
* Alta coesão
* Baixo acoplamento
* Testes unitários
