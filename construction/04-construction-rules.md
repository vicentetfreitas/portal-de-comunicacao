# Construction Rules — Engineering Framework

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Framework | **Engineering Framework v3.2** |
| Status | **Stable** |
| Camada | Construction |
| Última atualização | 2026-07-17 |
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

O schema Oracle é administrado pelo DBA através do baseline DDL oficial (`database/ddl/`). Evoluções pós-baseline em `database/migrations/`. Flyway não é utilizado (DEC-DB-019).

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

## R-25 — Frontend PKG Gate (BUILD-02)

Validação obrigatória para **PASS** em PKGs frontend incrementais (ex.: PKG-FE-01..05):

```text
yarn lint:check
yarn typecheck
yarn test:unit
yarn build
```

| Regra | Descrição |
|-------|-----------|
| BUILD-02-01 | **Proibido** exigir `yarn test:e2e` para `PASS` em PKG-FE-01..05 |
| BUILD-02-02 | `FULL_VALIDATION=1` no runner frontend = Gate PKG (sem E2E) — ver `16-frontend-validation-gates.md` |
| BUILD-02-03 | Scripts `frontend/scripts/revalidate-pkg-fe-NN.sh` devem alinhar-se ao Gate PKG |

## R-26 — E2E Stabilization Gate (E2E-01)

| Regra | Descrição |
|-------|-----------|
| E2E-01-01 | `yarn test:e2e` é obrigatório **apenas** no PKG de closure frontend (ex.: **PKG-FE-06**) |
| E2E-01-02 | Evidência: `FULL_VALIDATION=1 E2E_VALIDATION=1` + `pkg-evidence-run-frontend.sh` |
| E2E-01-03 | Estabilização da suíte Playwright compartilhada é responsabilidade explícita do PKG-FE-06 |
| E2E-01-04 | Workstream frontend `FEATURE_APPROVED` requer PKG-FE-06 com Gate PKG + Gate E2E em `PASS` |

Referência: `construction/16-frontend-validation-gates.md`

## R-27 — E2E Behavioral Policy (E2E-02)

| Regra | Descrição |
|-------|-----------|
| E2E-02-01 | Specs em `frontend/test/e2e/` validam **comportamento funcional** — SSOT `17-frontend-e2e-behavior-policy.md` |
| E2E-02-02 | **Proibido** acoplar specs a classes `.q-*` / `.ds-*`, estrutura interna Quasar ou paginação numérica inexistente no QTable |
| E2E-02-03 | PKG-FE-06 inclui revisão E2E-02 antes de `PASS` com `E2E_VALIDATION=1` |
| E2E-02-04 | Contratos públicos do DS (alert em campo, status em badge, labels i18n) são os únicos detalhes de implementação permitidos como locator |

Referência: `frontend/test/e2e/README.md`

## R-28 — Handoff entre Features (HANDOFF-01)

| Regra | Descrição |
|-------|-----------|
| HANDOFF-01-01 | **Padrão:** dependência de feature **sem** `scope` → predecessor `FEATURE_APPROVED` no registry (todos Workstreams) |
| HANDOFF-01-02 | **Exceção:** dependência **com** `scope: backend\|frontend` + `required_phase` (padrão `closed`) → validar `construction-state.yaml` do Workstream alvo (STATE-WS-01) |
| HANDOFF-01-03 | Ausência de `scope` → sempre regra padrão — nunca inferir exceção |
| HANDOFF-01-04 | Fundações (`dependencies.construction`) e `depends_on_workstreams` intra-Feature não usam `scope` |

SSOT: `construction/12-fullstack-orchestrator.md` § Handoff · Decisão: DL-EF-4.1-012

## R-22 — Validation Summary (VAL-01)

Ao concluir um PKG, o relatório principal (`pkg-XX/status.md`) deve conter a seção **VALIDATION SUMMARY** no formato de `construction/templates/pkg-validation-summary.md`.

| Regra | Descrição |
|-------|-----------|
| Resumo único | Status, comandos, correções por causa raiz, revalidação e path da evidência |
| Logs completos | Somente em `pkg-XX/evidence/*.log` |
| Status | `PASS`, `BUILD_FAILURE`, `ENVIRONMENT_FAILURE` ou `PENDING_REVALIDATION` (VAL-02) |
| Proibido | Stack traces, logs extensos ou tabelas de exit code no relatório principal |
| Processo | Comandos de validação inalterados (BUILD-01) — apenas apresentação |

## R-23 — Validation Lifecycle (VAL-02)

Estende VAL-01 com o ciclo de vida do status de validação. **Não altera** BUILD-01 nem critérios de aceite dos PKGs.

| Regra | Descrição |
|-------|-----------|
| VAL-02-01 | `PENDING_REVALIDATION` — correções concluídas documentadas; aguarda apenas reexecução completa do pipeline |
| VAL-02-02 | `BUILD_FAILURE` — **exclusivamente** falhas comprovadas durante execução completa do pipeline |
| VAL-02-03 | `ENVIRONMENT_FAILURE` — impossibilidade de executar comandos por ambiente (nunca falha de compilação/teste) |
| VAL-02-04 | Determinação de status por **condições observáveis** (evidência, correções, revalidação) — **sem** prioridade fixa entre estados |
| VAL-02-05 | Proibido `BUILD_FAILURE` quando correções já foram aplicadas e a revalidação completa ainda não ocorreu — usar `PENDING_REVALIDATION` |
| Template | `construction/templates/pkg-validation-summary.md` (VAL-01 + VAL-02) |

## R-24 — PKG Artifact Model (ART-01)

Conjunto mínimo de artefatos por PKG. **Não altera** BUILD-01, VAL-01 nem critérios de aceite.

| Regra | Descrição |
|-------|-----------|
| ART-01-01 | Obrigatório: `pkg-XX/status.md` (inclui VALIDATION SUMMARY) |
| ART-01-02 | Opcional: `pkg-XX/evidence/build-verify-YYYY-MM-DD.log` quando validação executada |
| ART-01-03 | **Proibido** gerar `implementation-report.md`, cópias de `run-bv.sh`, `verification-log-*.md` |
| ART-01-04 | Scripts de evidência: somente `construction/templates/pkg-evidence-run-*.sh` com `PKG_DIR=...` |
| ART-01-05 | `reports/` na Feature: apenas incidentes transversais — não duplicar validação por PKG |
| Referência | `construction/templates/pkg-artifact-model.md` |

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
