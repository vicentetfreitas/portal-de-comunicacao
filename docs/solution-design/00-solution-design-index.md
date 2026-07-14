# Solution Design Index

## Objetivo

Este documento é o **índice de navegação da camada Solution Design** do Portal de Comunicação.

A camada Solution Design tem como finalidade **transformar a arquitetura aprovada em uma solução implementável**, definindo visão da solução, responsabilidades técnicas, estratégia de implantação, contratos de integração, ownership de dados, segurança operacional, migração e roadmap de entrega.

A camada **não implementa código**, **não produz artefatos executáveis** e **não redefine decisões arquiteturais** já registradas. Ela materializa, em nível de solução, o que a camada Architecture consolidou, preparando a futura camada de Implementation.

---

# Estado Atual

## Discovery

Status: **CONCLUÍDO**

Camada estável e somente leitura. Módulos, integrações, arquitetura atual e dívidas técnicas documentados e abstraídos nas camadas posteriores.

## Domain

Status: **CONCLUÍDO**

Camada estável e somente leitura. Bounded contexts, aggregates, regras de negócio e questões abertas documentados. Questões abertas permanecem em `docs/domain/10-open-questions.md`.

## Architecture

Status: **CONCLUÍDO**

Camada estável e somente leitura. Dez artefatos arquiteturais produzidos (`01-system-context.md` a `10-target-architecture.md`), com ADRs, riscos catalogados, arquitetura alvo e roadmap arquitetural definidos.

## Solution Design

Status: **EM ANDAMENTO**

Camada ativa. Responsável por definir a solução alvo consumível pela Implementation.

---

# Dependências

## Fluxo documental entre camadas

```text
Discovery
    ↓
Domain
    ↓
Architecture
    ↓
Solution Design
    ↓
Implementation
```

## Regras de dependência

| Camada | Consome | Não altera |
| ------ | ------- | ---------- |
| Domain | Discovery | Discovery |
| Architecture | Discovery, Domain | Discovery, Domain |
| Solution Design | Architecture (e referências indiretas a Domain e Discovery via Architecture) | Discovery, Domain, Architecture |
| Implementation | Solution Design, Architecture | Discovery, Domain, Architecture, Solution Design |

**Solution Design consome Architecture** como fonte primária. Decisões, riscos, restrições e roadmap arquitetural já consolidados em `08-decision-records.md`, `09-risk-assessment.md` e `10-target-architecture.md` **não devem ser redescobertos nem contraditos**.

Camadas anteriores permanecem **congeladas**. Qualquer necessidade de alteração em Domain ou Architecture deve seguir processo formal de governança (ADR, encerramento de OQ, revisão de riscos) antes de impactar artefatos de Solution Design.

---

# Artefatos da Camada

| Documento | Status | Objetivo |
| --------- | ------ | -------- |
| `00-solution-design-index.md` | ✅ Concluído | Índice de navegação, dependências, governança e critérios da camada |
| `01-solution-overview.md` | 🔄 Próximo | Visão executiva da solução alvo; escopo implementável; alinhamento com arquitetura aprovada |
| `02-system-context.md` | ⏳ Pendente | Contexto da solução: atores, sistemas externos e fronteiras implementáveis |
| `03-container-architecture.md` | ⏳ Pendente | Containers da solução, responsabilidades técnicas e mapeamento aos bounded contexts |
| `04-deployment-architecture.md` | ⏳ Pendente | Topologia de implantação lógica e física; comunicação entre componentes |
| `05-environment-strategy.md` | ⏳ Pendente | Estratégia de ambientes (local, dev, hml, prod); isolamento e paridade |
| `06-integration-contracts.md` | ⏳ Pendente | Contratos de integração com Zimbra, canais opcionais e interfaces entre camadas |
| `07-data-ownership.md` | ⏳ Pendente | Ownership de dados na solução; persistência; fronteiras entre repositórios |
| `08-security-architecture.md` | ⏳ Pendente | Autenticação, autorização, auditoria e proteção de dados na solução alvo |
| `09-migration-strategy.md` | ⏳ Pendente | Estratégia de migração do baseline atual para a solução alvo (legado, notificações, contratos) |
| `10-delivery-roadmap.md` | ⏳ Pendente | Roadmap de entrega alinhado ao roadmap arquitetural; sequência de capacidades implementáveis |

---

# Fontes Primárias

Documentos obrigatórios da camada Architecture que fundamentam toda a Solution Design:

| Documento | Conteúdo relevante |
| --------- | ------------------- |
| `docs/architecture/08-decision-records.md` | 14 ADRs aceitos, 1 provisório (ADR-015), decisões pendentes, consequências e trade-offs |
| `docs/architecture/09-risk-assessment.md` | 32 riscos catalogados (3 críticos, 14 altos), mitigações e priorização |
| `docs/architecture/10-target-architecture.md` | Baseline vs. alvo, 18 lacunas, roadmap arquitetural, critérios de prontidão |

## Fontes secundárias (consulta sob demanda)

| Documento | Uso |
| --------- | --- |
| `docs/architecture/00-architecture-index.md` | Navegação e estado de encerramento da Architecture |
| `docs/architecture/01-system-context.md` a `07-deployment-architecture.md` | Detalhamento por dimensão arquitetural |
| `docs/domain/09-business-rules.md` | Validação de regras de negócio na solução |
| `docs/domain/10-open-questions.md` | Lacunas que impactam escopo implementável |

## Regras de consulta

Seguir ordem definida em `.cursor/rules/core/token-economy.mdc`:

1. Documento atual em construção
2. Índices da camada atual (`00-solution-design-index.md`)
3. Fontes primárias Architecture (`08`, `09`, `10`)
4. Demais artefatos Architecture
5. Domain e Discovery somente quando a Architecture não responder à necessidade

---

# Critério de Entrada

A camada Solution Design somente pode ser iniciada quando **todos** os critérios abaixo forem atendidos:

| Critério | Status | Evidência |
| -------- | ------ | --------- |
| Discovery concluída | ✅ Atendido | Camada estável; artefatos `01` a `08` |
| Domain concluída | ✅ Atendido | Bounded contexts, aggregates, BRs e OQs documentados |
| Architecture concluída | ✅ Atendido | Artefatos `01` a `10`; encerramento formal em `10-target-architecture.md` |
| ADRs principais registrados | ✅ Atendido | ADR-001 a ADR-014 aceitos; ADR-015 provisório |
| Riscos catalogados e priorizados | ✅ Atendido | 32 riscos em `09-risk-assessment.md` |
| Arquitetura alvo definida | ✅ Atendido | Baseline, alvo, lacunas e roadmap em `10-target-architecture.md` |
| Prontidão parcial para desenvolvimento documentada | ✅ Atendido | Núcleo ATIVO liberado com ressalvas; capacidades PARCIAL identificadas |

**Conclusão:** critérios de entrada **atendidos**. A camada Solution Design está autorizada a iniciar produção de artefatos.

---

# Critério de Saída

A camada Solution Design será considerada **CONCLUÍDA** quando:

| Critério | Descrição |
| -------- | --------- |
| Artefatos completos | Todos os documentos `01` a `10` produzidos e revisados |
| Rastreabilidade | Cada artefato referencia ADRs, riscos e requisitos de `10-target-architecture.md` |
| Solução implementável | Visão da solução, containers, deployment, ambientes, contratos, dados, segurança e migração definidos sem ambiguidade impeditiva |
| Roadmap de entrega | Sequência de capacidades e dependências documentada em `10-delivery-roadmap.md` |
| Sem código | Nenhum artefato executável (código, docker-compose final, pipelines, manifests) produzido nesta camada |
| Sem redefinição arquitetural | ADRs existentes preservados; divergências registradas como novos ADRs, não como alteração retroativa |
| Riscos atualizados | Impactos da solução nos riscos críticos e altos (R-001, R-002, R-003, R-005, R-006, R-009, R-010, R-011, R-014, R-015) avaliados |
| Prontidão para Implementation | Critério de entrada da camada Implementation atendido conforme `.cursor/rules/delivery/implementation-rules.mdc` |

Status final esperado:

```text
Solution Design = CONCLUÍDA
```

---

# Fluxo Documental

Representação do encadeamento de camadas e responsabilidades:

```text
┌─────────────┐
│  Discovery  │  Evidências do estado atual (código, integrações, dívidas)
└──────┬──────┘
       ↓
┌─────────────┐
│   Domain    │  Modelo de negócio (contextos, aggregates, regras, OQs)
└──────┬──────┘
       ↓
┌─────────────┐
│ Architecture│  Decisões, riscos, topologia lógica, arquitetura alvo
└──────┬──────┘
       ↓
┌─────────────────┐
│ Solution Design │  Solução implementável (contratos, deployment, roadmap)
└──────┬──────────┘
       ↓
┌─────────────────┐
│ Implementation  │  Código, infraestrutura executável, testes, entrega
└─────────────────┘
```

Cada seta representa **consumo unidirecional**. Não há retroalimentação que modifique camadas anteriores sem processo formal de governança.

---

# Governança

## Princípios obrigatórios

| Princípio | Descrição |
| --------- | --------- |
| Sem código | Nenhum código-fonte, script, manifest ou pipeline executável nesta camada |
| Sem redefinição arquitetural | ADRs aceitos (ADR-001 a ADR-014) são invioláveis; Solution Design os materializa, não os revisa |
| ADRs novos quando necessário | Mudanças que exijam novo container, banco, serviço, mecanismo de autenticação ou alteração de deployment exigem **novo ADR** na camada Architecture antes de prosseguir |
| Riscos rastreados | Todo artefato deve considerar impacto nos riscos de `09-risk-assessment.md`; riscos críticos e altos exigem plano de mitigação na solução |
| Sem diagramas de implementação | Modelar solução e deployment; não detalhar classes, pacotes ou estruturas de código |
| Sem tecnologias não documentadas | Stack e componentes somente conforme regras em `.cursor/rules/delivery/implementation-rules.mdc` e `.cursor/rules/architecture/*` |
| Um artefato por execução | Produzir apenas o documento solicitado; não gerar versões alternativas nem arquivos auxiliares |

## ADRs que a solução deve respeitar

| ADR | Decisão | Implicação para Solution Design |
| --- | ------- | -------------------------------- |
| ADR-001 | Monólito modular | Backend único com módulos por bounded context |
| ADR-002 | API Backend central | Núcleo de negócio no backend; sem BFF documentado |
| ADR-003 | Zimbra externo | Integração de identidade corporativa obrigatória |
| ADR-004 | Metadados × binários | Dois repositórios lógicos na solução |
| ADR-005 | Autorização no backend | Sem decisão efetiva de acesso no frontend |
| ADR-006 | Frontend apresentação | Vue consumindo API exclusivamente |
| ADR-007 | Quatro contextos / um backend | Estrutura modular no backend |
| ADR-008 | Compartilhamento ≠ autorização | Contratos distintos com integração obrigatória |
| ADR-011 | Três ambientes isolados | local, dev, hml, prod com persistência segregada |
| ADR-012 | Notificações no backend | Sem serviço de notificação independente |
| ADR-015 | Legado provisório | Migração documentada em `09-migration-strategy.md` |

## Riscos prioritários para acompanhamento

Riscos que exigem atenção explícita em todos os artefatos da camada:

| ID | Risco | Severidade |
| -- | ----- | ---------- |
| R-001 | API Backend como ponto único de processamento | Crítica |
| R-002 | Banco de Dados como persistência central | Crítica |
| R-003 | Dependência única do Zimbra | Crítica |
| R-005 | Coexistência API Backend Legado | Alta |
| R-006 | Dois subsistemas de notificação | Alta |
| R-009 | Divergência compartilhamento vs. autorização | Alta |
| R-010 | Fluxo de solicitação de permissão incompleto | Alta |
| R-011 | Revogação de permissão não documentada | Alta |
| R-014 | Escalabilidade horizontal indefinida | Alta |
| R-015 | Continuidade operacional não especificada | Alta |

## Lacunas arquiteturais com impacto em Solution Design

Lacunas de `10-target-architecture.md` que condicionam escopo implementável:

| ID | Lacuna | Artefato Solution Design impactado |
| -- | ------ | ------------------------------------ |
| L-001 | Onboarding indefinido | `01`, `06`, `10` |
| L-003 | Compartilhamento ↔ autorização | `06`, `08` |
| L-008 | Legado em coexistência | `09`, `10` |
| L-009 | Notificações duplicadas | `06`, `09` |
| L-010 | Endpoints órfãos | `06`, `10` |
| L-011 | Continuidade operacional | `04`, `05` |

---

# Próximo Artefato

```text
docs/solution-design/01-solution-overview.md
```

## Objetivo do próximo artefato

Produzir a **visão executiva da solução alvo**, consolidando escopo implementável, alinhamento com ADRs, impacto nos riscos prioritários e relação com o baseline documentado na Architecture.

## Fontes primárias obrigatórias

* `docs/architecture/10-target-architecture.md`
* `docs/architecture/09-risk-assessment.md`
* `docs/architecture/08-decision-records.md`

## Fontes secundárias

* `docs/architecture/02-container-diagram.md`
* `docs/architecture/07-deployment-architecture.md`
* `.cursor/rules/process/solution-design-phase.mdc`
* `.cursor/rules/delivery/implementation-rules.mdc`

---

## Referências de governança

| Regra | Caminho |
| ----- | ------- |
| Solution Design Phase | `.cursor/rules/process/solution-design-phase.mdc` |
| Document Dependencies | `.cursor/rules/process/document-dependencies.mdc` |
| Token Economy | `.cursor/rules/core/token-economy.mdc` |
| Deployment Modeling | `.cursor/rules/architecture/deployment-modeling.mdc` |
| Docker Strategy | `.cursor/rules/architecture/docker-strategy.mdc` |
| Implementation Rules | `.cursor/rules/delivery/implementation-rules.mdc` |

---

*Nenhum código foi produzido para a construção deste artefato.*
