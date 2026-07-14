# Roadmap

## Objetivo

Definir a evolução planejada do projeto ao longo do tempo.

Este documento apresenta:

* visão de entregas;
* releases previstas;
* marcos do projeto;
* evolução incremental do produto;
* dependências entre fases;
* objetivos de cada entrega;
* sprints de construção do backend.

O roadmap possui caráter estratégico e deve ser revisado periodicamente conforme o avanço do projeto.

---

# Visão Geral

## Estratégia de Entrega

A solução será construída de forma incremental.

Cada release deve gerar valor de negócio mensurável e reduzir riscos técnicos antes da próxima etapa.

Princípios:

* entregas pequenas e frequentes;
* redução contínua de riscos;
* validação constante com stakeholders;
* evolução incremental da arquitetura;
* preparação contínua para operação.

---

# Linha do Tempo

```text
Discovery ──► Domain ──► Architecture ──► Solution Design ──► Implementation
                                                                    │
                                                                    ▼
                                                          Construction (Sprints)
                                                                    │
                                                                    ▼
                                              MVP (Etapas 1–5) ──► Release 1 ──► ...
```

---

# Roadmap Executivo

| Fase            | Objetivo                     | Status      |
| --------------- | ---------------------------- | ----------- |
| Discovery       | Entendimento do problema     | 🟩 Concluído |
| Domain          | Modelagem do domínio         | 🟩 Concluído |
| Architecture    | Definição arquitetural       | 🟩 Concluído |
| Solution Design | Desenho detalhado da solução | 🟩 Concluído |
| Implementation  | Padrões e estrutura técnica  | 🟩 Concluído |
| Construction    | Preparação e implementação   | 🟨 Em andamento |
| MVP             | Primeira entrega funcional   | Planejado   |
| Release 1       | Primeira expansão funcional  | Planejado   |
| Release 2       | Escalabilidade e evolução    | Planejado   |
| Release 3       | Consolidação da plataforma   | Planejado   |

---

# Sprints de Construção — Backend

## Sprint 0 — Infraestrutura Transversal

| Item        | Valor        |
| ----------- | ------------ |
| Status      | **CONCLUÍDA** |
| Data encerramento | 2026-07-08 |
| Golden Feature | — (fundação) |
| Build       | SUCCESS (`mvn clean verify`) |
| Testes      | 106 testes unitários |

### Objetivo

Estabelecer a baseline técnica transversal do backend: bootstrap, shared modules, configuration, logging e integração Oracle.

### Entregáveis Implementados

| # | Entregável              | Pacote / Artefato                                      |
| - | ----------------------- | ------------------------------------------------------ |
| 1 | Bootstrap               | `PortalComunicacaoApplication`, `pom.xml`, perfis YAML |
| 2 | Shared DTO              | `shared/dto/` — ApiResponse, PageResponse, ErrorResponse, ValidationErrorResponse, FieldValidationError |
| 3 | Shared Exception        | `shared/exception/` — GlobalExceptionHandler, exceções de negócio |
| 4 | Shared Validation       | `shared/validation/` — @Uuid, @EnumValue, @NotBlankIfPresent, @NullOrSize |
| 5 | Shared Constants        | `shared/constants/` — ApiConstants, HeaderConstants, SecurityConstants, DateTimeConstants |
| 6 | Shared Util             | `shared/util/` — DateTimeUtils, UuidUtils, CollectionUtils, PaginationUtils |
| 7 | Configuration           | `configuration/` — Jackson, Locale, Async, ApplicationProperties |
| 8 | Infrastructure Logging  | `infrastructure/logging/` — CorrelationIdFilter, MDC, LoggingConfiguration |
| 9 | Refinement              | Alinhamento Oracle, Jackson 3, pacote `br.com.unimedceara.portalcomunicacao` |
| 10 | Auditoria              | Validação de aderência docs ↔ código ↔ arquitetura   |
| 11 | Consolidação documental | `docs/implementation/`, `docs/construction/backend/` sincronizados |

### Critério de Saída

* Projeto compila e executa localmente
* Todos os testes passam
* Conexão Oracle validada com schema provisionado pelo DBA (baseline DDL — DEC-DB-019)
* Infraestrutura transversal **congelada**

### Backlog Adiado (Sprint futura)

MapStruct, OpenAPI/Swagger, REST Controllers, SecurityFilterChain, bounded contexts, entidades JPA, métricas Micrometer/Prometheus, OpenTelemetry, request logging estruturado.

---

## Sprint 1 — Autenticação

| Item           | Valor              |
| -------------- | ------------------ |
| Status         | **EM PREPARAÇÃO**  |
| Golden Feature | **FT-AUTH**        |
| Especificação  | `specs/features/authentication/` |
| Dependência    | Sprint 0 concluída |

### Objetivo

Implementar autenticação integrada ao Serviço Corporativo de Autenticação, estabelecendo sessão autenticada e proteção de recursos.

### Entregáveis Previstos

* SecurityFilterChain configurado
* Integração com Serviço Corporativo de Autenticação
* Endpoints de login, callback e logout
* Testes unitários e de integração conforme `specs/features/authentication/`

---

## Sprints Futuras (Backend)

| Sprint | Épico / Feature | Descrição resumida              |
| ------ | --------------- | ------------------------------- |
| 2+     | EPIC-002        | Organização Corporativa         |
| 3+     | EPIC-003        | Controle de Acesso (autorização) |
| 4+     | EPIC-004        | Gestão Documental               |
| 5+     | EPIC-005        | Comunicação Interna             |

Ordem oficial conforme `docs/backlog/04-mvp-scope.md` e `docs/audit/10-mvp-consolidation-audit.md`.

---

# MVP

## Objetivo

Validar a proposta de valor principal do produto.

## Escopo

MVP oficial = **Etapas 1–5** de `docs/solution-design/10-delivery-roadmap.md`.

**Épicos:** EPIC-001 a EPIC-006.

**Excluídos:** EPIC-007 (pós-MVP), EPIC-008/Campanhas, FEATURE-046/Mensagens, FEATURE-044/Métricas Administrativas de negócio.

## Critérios de Sucesso

* Fluxo principal operacional.
* Usuários conseguem executar o caso de uso principal.
* Indicadores básicos monitorados.
* Operação estável.

---

# Release 1

## Objetivo

Expandir capacidades do MVP.

## Possíveis Entregas

* melhorias funcionais;
* refinamento de UX;
* novas integrações;
* melhorias de observabilidade;
* automações operacionais.

---

# Release 2

## Objetivo

Aumentar capacidade operacional e escalabilidade.

## Possíveis Entregas

* otimizações de performance;
* escalabilidade horizontal;
* processamento assíncrono;
* novos canais de integração;
* automação avançada.

---

# Release 3

## Objetivo

Consolidar a plataforma e preparar evolução de longo prazo.

---

# Marcos do Projeto

| Marco | Nome                              | Status      | Data       |
| ----- | --------------------------------- | ----------- | ---------- |
| M1    | Discovery Aprovado                | 🟩 Concluído | 2026-06-22 |
| M2    | Domain Aprovado                   | 🟩 Concluído | 2026-06-22 |
| M3    | Architecture Aprovada             | 🟩 Concluído | 2026-06-22 |
| M4    | Solution Design Aprovado          | 🟩 Concluído | 2026-06-22 |
| M5    | Construction Ready (Backend S0)   | 🟩 Concluído | 2026-07-08 |
| M6    | MVP Go Live                       | Planejado   | —          |
| M7    | Release 1                         | Planejado   | —          |
| M8    | Release 2                         | Planejado   | —          |
| M9    | Release 3                         | Planejado   | —          |

---

# Dependências Estratégicas

## Negócio

* aprovação dos requisitos — 🟩 concluída;
* validação dos objetivos — 🟩 concluída;
* alinhamento dos stakeholders — 🟩 concluída.

## Arquitetura

* decisões arquiteturais de fundação — 🟩 concluídas (Sprint 0);
* NFRs definidos — 🟩 concluído;
* integrações de autenticação — 🟨 Sprint 1 (FT-AUTH).

## Tecnologia

* stack tecnológica definida — 🟩 Java 25, Spring Boot 4.1, Oracle;
* ambiente operacional preparado — 🟩 perfil local;
* pipelines estabelecidos — ⬜ pendente.

---

# Indicadores de Evolução

## Negócio

* funcionalidades entregues: 0 (fundação técnica);
* valor gerado: baseline de infraestrutura;
* satisfação dos usuários: N/A (pré-MVP).

## Tecnologia

* cobertura de testes: 106 testes unitários (infraestrutura);
* estabilidade operacional: build SUCCESS;
* indicadores de qualidade: GlobalExceptionHandler, validação e logging padronizados.

---

# Processo de Revisão

O roadmap deve ser revisado:

* ao final de cada release;
* ao final de cada sprint;
* após mudanças significativas de escopo;
* durante auditorias do projeto.

---

# Histórico de Atualizações

| Data       | Autor           | Alteração                                              |
| ---------- | --------------- | ------------------------------------------------------ |
| YYYY-MM-DD | Project Manager | Criação inicial do documento                           |
| 2026-06-22 | Reconciliação   | QST-001 encerrada — referência ao audit 10             |
| 2026-07-08 | Governança      | Sprint 0 CONCLUÍDA; Sprint 1 EM PREPARAÇÃO — FT-AUTH   |
