# Construction Roadmap — Sprint 1A

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Sprint | 1A — Platform Foundation |
| Status | Aprovado |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Definir o roadmap de construção da Platform Foundation, estabelecendo fases, entregáveis, dependências e critérios de transição entre módulos.

---

# Escopo

Este roadmap cobre exclusivamente a Sprint 1A — construção da infraestrutura compartilhada do backend.

Não cobre implementação de FT-AUTH nem de outras Features.

---

# Pré-requisitos

| Pré-requisito | Status | Evidência |
|---------------|--------|-----------|
| Sprint 0 encerrada | Concluído | `docs/governance/history/phase2-backend-construction-report.md` |
| Foundation Baseline | Concluído | `specs/foundation/` |
| Feature Baseline | Concluído | `specs/features/FEATURE_BASELINE.md` |
| FT-AUTH especificada | Aprovada | `specs/features/authentication/` |
| Arquitetura aprovada | Concluída | `docs/architecture/`, `specs/architecture/` |
| Build Sprint 0 | SUCCESS | `mvn clean verify` — 106 testes |

---

# Visão da Sprint 1A

```text
Sprint 0 (Concluída)
    │
    │  Bootstrap, Shared, Configuration básica, Logging
    │
    ▼
Sprint 1A (Platform Foundation)
    │
    │  Configuration → Persistence → Security → Integration
    │  → Web → Observability → Testing → Audit
    │
    ▼
Sprint 1 (FT-AUTH)
    │
    │  Autenticação Zimbra, JWT, Sessões
    │
    ▼
Sprints Futuras (Features de Negócio)
```

---

# Fases de Construção

## Fase 1 — Configuration Extension

| Item | Valor |
|------|-------|
| Módulo | Configuration |
| Pacote | PF-CONF |
| Dependência | Sprint 0 |
| Duração estimada | 2 dias |

**Entregáveis:**

- Properties tipadas para Security, Persistence e Integration
- Validação `@ConfigurationProperties` com grupos
- Documentação de variáveis por ambiente

**Critério de saída:** Properties carregadas e validadas em todos os perfis (`local`, `dev`, `hml`).

---

## Fase 2 — Persistence Layer

| Item | Valor |
|------|-------|
| Módulo | Persistence |
| Pacote | PF-PERS |
| Dependência | Fase 1 |
| Duração estimada | 3 dias |

**Entregáveis:**

- `JpaConfiguration` operacional
- `BaseEntity` / `AuditableEntity`
- `BaseRepository`
- Convenções de evolução DDL documentadas (DEC-DB-019)
- Testes de conexão Oracle

**Critério de saída:** Contexto JPA inicializa; transação de leitura funcional contra Oracle.

---

## Fase 3 — Security Foundation

| Item | Valor |
|------|-------|
| Módulo | Security |
| Pacote | PF-SEC |
| Dependência | Fases 1, 2 |
| Duração estimada | 3 dias |

**Entregáveis:**

- `SecurityFilterChain` stateless
- Configuração CSRF para cookies
- Filtro JWT esqueleto
- Whitelist de endpoints públicos
- Testes de segurança base

**Critério de saída:** Requisições sem token rejeitadas em rotas protegidas; endpoints públicos acessíveis.

---

## Fase 4 — Integration Infrastructure

| Item | Valor |
|------|-------|
| Módulo | Integration |
| Pacote | PF-INT |
| Dependência | Fases 1, 3 |
| Duração estimada | 2 dias |

**Entregáveis:**

- Cliente HTTP configurado (RestClient)
- Timeout, retry e circuit breaker
- Propagação Correlation ID outbound
- Interface `IdentityProviderClient`
- Hierarquia `IntegrationException`

**Critério de saída:** Cliente HTTP executa chamada mock com Correlation ID propagado.

---

## Fase 5 — Web Layer

| Item | Valor |
|------|-------|
| Módulo | Web |
| Pacote | PF-WEB |
| Dependência | Fases 1, 3 |
| Duração estimada | 2 dias |

**Entregáveis:**

- Estrutura `interfaces/rest/`
- `HealthController` — `GET /api/v1/health`
- Configuração OpenAPI 3
- Integração com SecurityFilterChain

**Critério de saída:** Health endpoint documentado e acessível; Swagger UI operacional.

---

## Fase 6 — Observability

| Item | Valor |
|------|-------|
| Módulo | Observability |
| Pacote | PF-OBS |
| Dependência | Fases 4, 5 |
| Duração estimada | 2 dias |

**Entregáveis:**

- Métricas Micrometer com convenções
- Request logging estruturado
- Health indicators (database)
- Actuator endpoints configurados

**Critério de saída:** Métricas expostas; logs estruturados com Correlation ID em requisições HTTP.

---

## Fase 7 — Testing Infrastructure

| Item | Valor |
|------|-------|
| Módulo | Testing |
| Pacote | PF-TEST |
| Dependência | Todas as fases anteriores |
| Duração estimada | 2 dias |

**Entregáveis:**

- `@IntegrationTest` e `AbstractIntegrationTest`
- Utilitários de teste de segurança
- Configuração de banco de testes
- Documentação de convenções

**Critério de saída:** Pelo menos um teste de integração end-to-end do health endpoint aprovado.

---

## Fase 8 — Construction Audit

| Item | Valor |
|------|-------|
| Atividade | Auditoria |
| Dependência | Fases 1–7 |
| Duração estimada | 1 dia |

**Entregáveis:**

- `review/construction-audit.md` preenchido
- `review/readiness-checklist.md` validado
- `review/completion-report.md` emitido

**Critério de saída:** Auditoria **APROVADA**; Platform Foundation pronta para FT-AUTH.

---

# Cronograma Resumido

| Fase | Módulo | Estimativa | Acumulado |
|------|--------|------------|-----------|
| 1 | Configuration | 2d | 2d |
| 2 | Persistence | 3d | 5d |
| 3 | Security | 3d | 8d |
| 4 | Integration | 2d | 10d |
| 5 | Web | 2d | 12d |
| 6 | Observability | 2d | 14d |
| 7 | Testing | 2d | 16d |
| 8 | Audit | 1d | 17d |

**Duração total estimada:** 17 dias úteis.

---

# Marcos

| Marco | Descrição | Critério |
|-------|-----------|----------|
| M-S1A-01 | Configuration + Persistence | JPA operacional |
| M-S1A-02 | Security + Integration | FilterChain + HTTP client |
| M-S1A-03 | Web + Observability | Health + métricas |
| M-S1A-04 | Testing completo | Integração validada |
| M-S1A-05 | Audit aprovado | Pronto para FT-AUTH |

---

# Transição para FT-AUTH

A Sprint 1 (FT-AUTH) somente inicia quando:

1. Marco M-S1A-05 atingido
2. `05-readiness-review.md` aprovado
3. Nenhum bloqueio em `07-open-decisions.md` com criticidade Alta ou Crítica
4. Tech Lead confirma disponibilidade da fundação

---

# Riscos ao Roadmap

Consultar `08-open-risks.md` para riscos que podem impactar o cronograma.

---

# Referências

- `06-development-order.md` — Ordem detalhada
- `03-construction-packages.md` — Pacotes de entrega
- `09-progress.md` — Acompanhamento
