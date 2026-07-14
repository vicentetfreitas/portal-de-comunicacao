# Configuration Module

| Item | Valor |
|------|-------|
| Módulo | Configuration |
| Prefixo | PF-CONF |
| Pacote | `configuration/` |
| Pacote Construction | PKG-01 |
| Status | Não iniciado |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Estender a configuração da Sprint 0 com properties tipadas e validadas para os módulos Security, Persistence e Integration da Platform Foundation.

---

# Escopo

## Inclui

- `SecurityProperties` — JWT, CSRF, CORS
- `PersistenceProperties` — datasource, pool, JPA/Hibernate
- `IntegrationProperties` — timeout, retry, circuit breaker
- `ZimbraProperties` — estrutura de configuração Zimbra (sem implementação)
- Beans `@EnableConfigurationProperties` por módulo
- Testes de binding e validação por perfil

## Não inclui

- `ApplicationProperties` (Sprint 0 — existente, não alterar)
- Jackson, Locale, Async (Sprint 0 — existentes)
- Segredos em código-fonte
- Lógica de negócio

---

# Responsabilidades

| Componente | Responsabilidade |
|------------|------------------|
| SecurityProperties | Parâmetros de JWT (issuer, TTL), CSRF, CORS origins |
| PersistenceProperties | URL datasource, pool size, dialect, show-sql |
| IntegrationProperties | Timeout connect/read, retry max attempts, circuit breaker threshold |
| ZimbraProperties | authUrl, validateUrl, timeout (estrutura para FT-AUTH) |
| *PropertiesConfiguration | Habilitar e registrar beans de properties |

---

# Limites

- Sem beans de domínio
- Sem SecurityFilterChain (módulo Security — PKG-03)
- Sem datasource bean (módulo Persistence — PKG-02)
- Sem cliente HTTP (módulo Integration — PKG-04)
- Alterações em `ApplicationProperties` existente proibidas sem justificativa formal

---

# Configuration Contract Rule

Properties deste módulo são **contratos técnicos** — não autorizam implementação dos componentes consumidores.

```text
SecurityProperties     → consumida por PKG-03 (não implementa JWT/SecurityFilterChain aqui)
PersistenceProperties  → consumida por PKG-02 (não implementa JPA aqui)
IntegrationProperties  → consumida por PKG-04 (não implementa RestClient aqui)
ZimbraProperties       → consumida por PKG-04 (não implementa ZimbraClient aqui)
```

Governança: `construction/04-construction-rules.md` (R-11) e Construction Orchestrator §5.9.

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| Sprint 0 Configuration | `configuration/jackson/`, `locale/`, `async/`, `properties/ApplicationProperties` | Concluído |
| `docs/implementation/06-database-standards.md` | Padrões Oracle | Consultivo |
| `specs/features/authentication/decisions.md` | DA-AUTH-006, DA-AUTH-008 | Consultivo |

---

# Componentes Esperados

```text
configuration/
├── async/                    (Sprint 0)
├── jackson/                  (Sprint 0)
├── locale/                   (Sprint 0)
└── properties/
    ├── ApplicationProperties.java              (Sprint 0)
    ├── ApplicationPropertiesConfiguration.java   (Sprint 0)
    ├── SecurityProperties.java
    ├── SecurityPropertiesConfiguration.java
    ├── PersistenceProperties.java
    ├── PersistencePropertiesConfiguration.java
    ├── IntegrationProperties.java
    ├── IntegrationPropertiesConfiguration.java
    ├── ZimbraProperties.java
    └── ZimbraPropertiesConfiguration.java
```

---

# Ordem de Construção

```text
PF-CONF-001 (SecurityProperties)
    → PF-CONF-002 (PersistenceProperties)
    → PF-CONF-003 (IntegrationProperties)
    → PF-CONF-004 (ZimbraProperties)
    → PF-CONF-005 (Testes por perfil)
```

---

# Critérios de Aceite

1. Todas as properties carregam via `@ConfigurationProperties` com prefixo documentado
2. Validação `@NotBlank`, `@Min`, `@Max` onde aplicável
3. Properties funcionam nos perfis `local`, `dev`, `hml`
4. Segredos referenciados via variáveis de ambiente (`${JWT_SECRET:}`)
5. Testes unitários de binding aprovados
6. `mvn clean verify` — SUCCESS

---

# Definition of Done do Módulo

- [ ] Todas as tarefas PF-CONF-* concluídas
- [ ] Testes unitários aprovados
- [ ] `review.md` validado
- [ ] Build SUCCESS
- [ ] Documentação YAML de exemplo por perfil

---

# Relação com Demais Camadas

| Camada | Relação |
|--------|---------|
| Sprint 0 | Estende sem alterar |
| Persistence (PF-PERS) | Consome PersistenceProperties |
| Security (PF-SEC) | Consome SecurityProperties |
| Integration (PF-INT) | Consome IntegrationProperties, ZimbraProperties |
| FT-AUTH | ZimbraProperties preparada para TASK-AUTH-INF-001 |

---

# Rastreabilidade

- `docs/implementation/02-repository-structure.md` § Configuration
- `docs/construction/backend/01-project-bootstrap.md` § ConfigurationProperties
- `construction/03-construction-packages.md` § PKG-01
