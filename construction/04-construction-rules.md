# Construction Rules — Engineering Framework

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Framework | **Engineering Framework v3.2** |
| Status | **Stable** |
| Camada | Construction |
| Última atualização | 2026-07-09 |
| Changelog | `CHANGELOG.md` |

---

# Objetivo

Estabelecer as regras de governança que regem a construção da Platform Foundation, Features de negócio e o workflow orientado à Feature.

Estas regras garantem aderência arquitetural, rastreabilidade e qualidade sem alterar especificações ou decisões da Sprint 0.

---

# Regras de Governança do Engineering Framework

Regras institucionais válidas para **todas as Features** a partir da estabilização v3.2 (pós-FT-AUTH).

| ID | Regra | Descrição |
|----|-------|-----------|
| **RULE-01** | Specification Approved | Nenhuma Feature inicia sem **Specification Approved** — a specification deve atender Definition of Ready (`specs/foundation/definition-of-ready.md`) |
| **RULE-02** | Framework congelado | O Engineering Framework permanece **congelado** durante a execução da Feature. Evoluções do workflow, templates ou regras somente **entre Features ou Sprints** |
| **RULE-03** | Spec ↔ API Contract | Nenhuma implementação inicia enquanto existir **divergência entre Specification e API Contract** (`specification.md` ↔ `api.md`) |
| **RULE-04** | Sprint Retrospective | Toda Feature com status **FEATURE_APPROVED** deve gerar **Sprint Retrospective** (`construction/history/sprint-XX-retrospective.md`) antes do início da próxima Feature. A retrospectiva registra aprendizados, decisões permanentes e melhorias de processo. Alterações no Engineering Framework somente podem ser **planejadas** a partir dessas evidências e **executadas fora** de Feature em andamento |

## Política de evolução do framework

Futuras evoluções do Engineering Framework devem ser motivadas por **necessidades recorrentes observadas em múltiplas Features**, e não por ajustes específicos de uma única implementação.

Evidências primárias: Sprint Retrospectives. Registro de versões: `CHANGELOG.md`.

---

# Escopo

Aplica-se a toda atividade de construção: Platform Foundation (Sprint 1A — histórico), Features de negócio e encerramento consolidado (Review → Audit → Readiness).

---

# Princípios Fundamentais

## 1. Especificação Prevalece

```text
specs/ → docs/ → construction/ → código
```

Nenhuma implementação pode contradizer especificações ou arquitetura aprovada.

## 2. Sprint 0 Congelada

Componentes entregues na Sprint 0 não devem ser refatorados exceto para correção de defeito comprovado. Extensões são permitidas; substituições não.

## 3. Infraestrutura, Não Domínio

A Platform Foundation contém apenas capacidades técnicas compartilhadas. Regras de negócio pertencem a Features em `specs/features/`.

## 4. Reutilização Obrigatória

Features futuras devem consumir componentes da fundação. É proibido duplicar infraestrutura já disponível na Platform Foundation.

## 5. Incrementalidade

Cada pacote (PKG-01 a PKG-07) deve ser concluído e validado antes do próximo. Não avançar com dependências pendentes.

---

# Regras de Implementação

## R-01 — Localização de Código

| Tipo | Pacote obrigatório |
|------|-------------------|
| Configuração | `configuration/` |
| Persistência | `infrastructure/persistence/` |
| Segurança | `infrastructure/security/` |
| Integração | `infrastructure/integration/` |
| API REST | `interfaces/rest/` |
| Observabilidade | `infrastructure/observability/` |
| Testes de suporte | `src/test/.../support/` |

Bounded contexts (`organization/`, `accesscontrol/`, etc.) são proibidos na Sprint 1A.

## R-02 — Dependências entre Pacotes

Um módulo só pode depender de módulos anteriores na ordem oficial (ver `06-development-order.md`). Dependências circulares são proibidas.

## R-03 — Shared Modules

O pacote `shared/` (Sprint 0) é consumido, não modificado, exceto para adição de constantes ou utilitários transversais aprovados pelo Tech Lead.

## R-04 — Configuração Externa

Segredos (JWT keys, credenciais) nunca em código-fonte. Utilizar variáveis de ambiente ou vault conforme `docs/implementation/`.

## R-05 — Testes Obrigatórios

Toda tarefa PF-* deve possuir teste correspondente. Build deve falhar se testes falharem (`mvn clean verify`).

## R-06 — Evolução do Schema (DBA)

O schema Oracle é administrado pelo DBA através do baseline DDL oficial (`docs/database/ddl/`). Evoluções pós-baseline em `docs/database/migrations/`. Flyway não é utilizado (DEC-DB-019).

## R-07 — APIs de Infraestrutura

Endpoints da fundação usam prefixo `/api/v1`. Apenas health e actuator são permitidos na Sprint 1A.

## R-08 — Logging

Nenhum dado sensível (tokens, credenciais, PII) em logs. Correlation ID obrigatório em requisições HTTP.

## R-09 — OpenAPI

Todo endpoint REST deve ser documentado via OpenAPI 3 antes de ser considerado concluído.

## R-10 — Proibição de Antecipação

É proibido implementar fluxos FT-AUTH (login, callback, JWT emission, AUTH_SESSAO) na Sprint 1A. Apenas infraestrutura preparatória.

## R-11 — Configuration Contract Rule

A existência de uma propriedade de configuração (`@ConfigurationProperties`, seções em `application.yaml`) **não autoriza** a implementação do componente que irá consumi-la.

| Camada | Escopo | Package |
|--------|--------|---------|
| Contrato | Properties tipadas, validação, perfis, YAML | PKG-01 |
| Implementação | Beans de runtime, serviços, filtros, clientes, repositórios | PKG-02..07 |

**Regra:** Properties são contratos técnicos entre Packages. Implementações pertencem exclusivamente ao Package responsável.

**Exemplos:**

- `SecurityProperties` / `application.security.*` **não autorizam** `JwtService`, `SecurityFilterChain`, `AuthenticationManager` (PKG-03)
- `ZimbraProperties` / `application.zimbra.*` **não autorizam** `ZimbraClient`, `RestClient` (PKG-04)
- `PersistenceProperties` / `application.persistence.*` **não autorizam** `EntityManager`, `JpaRepository` (PKG-02)

Violação → `BLOCKED` com motivo `Configuration Contract Violation`. Complementa R-02 e a Architectural Boundary Enforcement do Construction Orchestrator.

## R-12 — Workflow Orientado à Feature

A execução de construção segue o modelo em três fases definido em `construction/11-feature-execution-workflow.md`:

```text
Sessão da Feature → Execução dos PKGs → Encerramento da Feature
```

A Feature é unidade de contexto e encerramento; o PKG é unidade de implementação.

## R-13 — Cache de Contexto

Após a Sessão da Feature (`construction/{platform-foundation|features/<FEATURE_CODE>}/session.md`), os artefatos carregados não devem ser relidos durante a execução dos PKGs, salvo exceção justificada em `pkg-XX/status.md`.

## R-14 — Escopo Documental do PKG

Durante um PKG, o agente atualiza `construction/{platform-foundation|features/<FEATURE_CODE>}/construction-state.yaml` e `construction/{platform-foundation|features/<FEATURE_CODE>}/pkg-XX/status.md`.

É proibido durante PKG atualizar: `09-progress.md`, `review.md` compartilhado, readiness, traceability global, changelog consolidado ou relatórios finais.

## R-15 — Build e Auditoria no Encerramento

- `mvn clean verify` — executar no Encerramento da Feature, não por PKG
- `reviewer` e `auditor` — executar de forma consolidada no Encerramento
- Feature Readiness Review — executar uma vez após o último PKG

Exceção: validações locais por PKG (`mvn test -Dtest=...`, `mvn compile`) são obrigatórias e permitidas.

## R-16 — Session Imutável (SESSION-01)

`construction/{platform-foundation|features/<FEATURE_CODE>}/session.md` é **READ ONLY** após criação.

- Somente `Execute Feature` pode criar ou recriar a Session.
- Nenhum PKG pode modificar seu conteúdo.
- A Session representa conhecimento carregado (Snapshot), não progresso.

## R-17 — Manifesto da Feature

Toda Feature deve possuir `feature-manifest.yaml` como **SSOD** (SSOD-01) em `construction/{platform-foundation|features/<FEATURE_CODE>}/`.

Navegação exclusiva via Manifest — exploração da árvore e paths hardcoded são proibidos.

## R-18 — Cache de Contexto (CACHE-01)

Nenhum documento pode ser relido caso sua informação já esteja presente no Snapshot.

Hierarquia: `Documento → Snapshot → Cache`.

## R-19 — Invalidação da Session (CACHE-02)

A Session somente é invalidada por: alteração de specification, api, use cases, domain, decisions, ADR, inclusão/remoção de PKG ou alteração do Manifest.

Na ausência desses eventos, toda execução de PKG deve reutilizar a Session ativa.

## R-20 — Build Incremental (BUILD-01)

Durante PKG, são permitidos apenas: `mvn test`, `mvn verify -pl backend`, `mvn test -Dtest=...`, `mvn -pl backend -am test`.

`mvn clean verify` é reservado exclusivamente ao Encerramento da Feature.

## R-21 — Independência de PKGs (PARALLEL-01)

Cada PKG altera `construction-state.yaml` e `pkg-XX/status.md`. Não compartilha outros arquivos de escrita com PKGs paralelos.

Proibido durante PKG: modificar `session.md`, `review.md`, `progress`, `traceability`, `closure-report.md`.

## STATE-00 — Dois Níveis de Construction State

| Nível | Caminho | Papel |
|-------|---------|-------|
| Platform Foundation | `construction/platform-foundation/construction-state.yaml` | Infraestrutura — **encerrada**, registro histórico Sprint 1A |
| Feature de negócio | `construction/features/<FEATURE_CODE>/construction-state.yaml` | Ciclo independente por Feature |

O estado da Platform Foundation **não** é sobrescrito pelo andamento das Features.

## STATE-01 — Construction State é SSOT

O `construction-state.yaml` de cada unidade de construção é a **única fonte oficial** do seu estado operacional:

- Fundação: `construction/platform-foundation/construction-state.yaml`
- Features: `construction/features/<FEATURE_CODE>/construction-state.yaml`

Nenhum outro artefato deve armazenar ou ser consultado para inferir: fase ativa, PKG corrente, PKGs concluídos/pendentes, cache, review, audit, build ou readiness.

## STATE-02 — Consulta Prioritária

Agentes devem consultar `construction-state.yaml` **antes** de qualquer outro artefato operacional.

É proibido inferir estado lendo múltiplos documentos (`session.md`, `pkg-XX/status.md`, `09-progress.md`) quando a informação está no Construction State.

## STATE-03 — Manifest é SSOD

`feature-manifest.yaml` é o **Single Source of Discovery** — localiza specification, construction, implementation, artifacts, dependencies e metrics.

Nunca inferir paths hardcoded. Estado de execução pertence ao Construction State, não ao manifesto.

## SSOD-01 — Descoberta via Manifesto

Todo agente ou automação deve **iniciar** consultando `feature-manifest.yaml` (ou `registry.yaml` para listar Features).

É proibido acessar artefatos da Feature sem descobri-los via manifesto.

## STATE-04 — Session Descreve Contexto

`session.md` representa o Snapshot de contexto — imutável após criação (SESSION-01).

Nunca armazena progresso operacional.

## STATE-05 — PKG Status é Histórico Local

`pkg-XX/status.md` armazena histórico detalhado daquele PKG.

Nunca representa estado global da Feature.

## STATE-06 — Resultados Finais

`review.md`, `closure-report.md` e `09-progress.md` refletem resultados finais no Encerramento.

O estado corrente pertence exclusivamente ao Construction State.

## RULE-CONTEXT-01

Nenhum PKG pode reabrir documentos já carregados na Session ativa.

Ordem de consulta: (1) Manifest SSOD, (2) Construction State, (3) Snapshot, (4) Cache, (5) documento adicional somente se ausente.

Exploração da árvore durante PKG é proibida, salvo invalidação conforme CACHE-02.

---

# Regras de Documentação

## D-01 — Atualização de Progresso

`09-progress.md` e demais documentos compartilhados de progresso são atualizados **apenas no Encerramento da Feature**, não ao final de cada PKG.

Durante PKGs, o progresso operacional reside em `construction/{platform-foundation|features/<FEATURE_CODE>}/construction-state.yaml` (SSOT) e no detalhamento em `pkg-XX/status.md`.

## D-02 — Decisões Pendentes

Decisões não resolvidas devem ser registradas em `07-open-decisions.md`. Não inferir soluções.

## D-03 — Riscos Identificados

Novos riscos da construção devem ser registrados em `08-open-risks.md`.

## D-04 — Review por Módulo

Cada módulo deve ter `review.md` preenchido no **Encerramento da Feature**, após revisão consolidada do `reviewer`. Não preencher `review.md` ao final de cada PKG individual.

## D-05 — Não Duplicar

Não duplicar conteúdo de `docs/implementation/` ou `specs/`. Referenciar artefatos existentes.

---

# Regras de Qualidade

## Q-01 — Build Limpo

`mvn clean verify` deve retornar SUCCESS no **Encerramento da Feature**. Durante PKGs, validações locais (`mvn test`, `mvn compile`) são suficientes.

## Q-02 — Cobertura Mínima

Novos componentes da fundação devem possuir cobertura de testes unitários. Meta: 80% por classe de infraestrutura.

## Q-03 — Code Review

Toda implementação exige revisão de pelo menos um desenvolvedor antes de merge.

## Q-04 — Aderência Arquitetural

Implementação deve seguir `docs/implementation/02-repository-structure.md` e padrões correlatos.

## Q-05 — Compatibilidade Oracle

Toda persistência deve ser validada contra Oracle (schema `UNMPORTCOM`). H2 permitido apenas em testes com perfil dedicado.

---

# Regras de Bloqueio

A implementação deve ser interrompida quando:

1. Especificação ou arquitetura conflitante identificada
2. Decisão pendente com criticidade Alta ou Crítica sem resolução
3. Dependência de módulo anterior não concluída
4. Testes da Sprint 0 em regressão
5. Escopo de Feature detectado na implementação da fundação
6. Implementação motivada apenas pela existência de Configuration Property (R-11)

Registrar bloqueio em `07-open-decisions.md` ou `08-open-risks.md` conforme natureza. Violações R-11 → `Configuration Contract Violation` em `09-progress.md`.

---

# Responsabilidades

| Papel | Responsabilidade |
|-------|------------------|
| Desenvolvedor | Implementar conforme tarefas PF-* e regras R-01 a R-11 |
| Revisor | Validar aderência às regras Q-01 a Q-05 |
| Arquiteto | Resolver bloqueios arquiteturais; aprovar exceções |
| Tech Lead | Autorizar merge; validar ordem de pacotes |

---

# Relação com Outras Camadas

| Camada | Relação |
|--------|---------|
| `specs/foundation/conventions.md` | Convenções de nomenclatura e identificadores |
| `specs/foundation/definition-of-done.md` | DoD adaptada por módulo em `review.md` |
| `docs/implementation/` | Padrões técnicos detalhados |
| `.cursor/rules/process/construction-phase.mdc` | Governança geral de Construction |

---

# Referências

- `CHANGELOG.md` — histórico de versões do Engineering Framework
- `04-construction-rules.md` (este documento)
- `11-feature-execution-workflow.md` — SSOT workflow v3.2 Stable
- `06-development-order.md`
- `platform-foundation/*/review.md`
