# Architecture Index

## Objetivo

Documento de navegação da camada Architecture.

Centraliza:

* fontes de negócio;
* fontes técnicas;
* artefatos arquiteturais;
* estado atual da camada;
* dependências documentais;
* referências para evolução futura.

---

# Estado Atual

## Discovery

Status: CONCLUÍDO

Camada considerada estável e somente leitura.

Todos os artefatos de Discovery foram produzidos, validados e consolidados pelas camadas posteriores.

---

## Domain

Status: CONCLUÍDO

Camada considerada estável e somente leitura.

Todos os bounded contexts, aggregates, regras de negócio e questões abertas foram documentados.

Questões abertas permanecem registradas em:

```text
docs/domain/10-open-questions.md
```

---

## Architecture

Status: CONCLUÍDO

Camada considerada estável e somente leitura.

Todos os artefatos arquiteturais foram produzidos, revisados e consolidados.

A evolução futura da arquitetura deve ocorrer através de:

* ADRs;
* revisão de riscos;
* atualização da arquitetura alvo;
* encerramento formal de Open Questions.

---

# Fontes de Negócio

## Primárias

* Domain Vision
* Business Rules
* Aggregates
* Open Questions

## Artefatos Recomendados

* docs/domain/01-vision.md
* docs/domain/05-bounded-contexts.md
* docs/domain/06-context-map.md
* docs/domain/08-aggregates.md
* docs/domain/09-business-rules.md
* docs/domain/10-open-questions.md

---

# Fontes Técnicas

## Discovery

* Current Architecture
* Technical Debt

## Artefatos Recomendados

* docs/discovery/07-current-architecture.md
* docs/discovery/08-technical-debt.md

---

# Artefatos Architecture

| Documento                     | Status      | Objetivo                                         |
| ----------------------------- | ----------- | ------------------------------------------------ |
| 01-system-context.md          | ✅ Concluído | Contexto do sistema, atores, fronteiras e fluxos |
| 02-container-diagram.md       | ✅ Concluído | Containers, responsabilidades e dependências     |
| 03-component-diagram.md       | ✅ Concluído | Componentes de negócio e módulos internos        |
| 04-integrations.md            | ✅ Concluído | Integrações externas e contratos                 |
| 05-data-architecture.md       | ✅ Concluído | Arquitetura de dados e ownership                 |
| 06-security-architecture.md   | ✅ Concluído | Autenticação, autorização e auditoria            |
| 07-deployment-architecture.md | ✅ Concluído | Topologia lógica e implantação                   |
| 08-decision-records.md        | ✅ Concluído | ADRs e decisões arquiteturais                    |
| 09-risk-assessment.md         | ✅ Concluído | Avaliação consolidada de riscos arquiteturais    |
| 10-target-architecture.md     | ✅ Concluído | Arquitetura alvo e roadmap arquitetural          |

---

# Artefatos de Referência Obrigatória

Os documentos abaixo consolidam toda a arquitetura produzida e devem ser considerados fontes primárias para futuras evoluções:

* docs/architecture/08-decision-records.md
* docs/architecture/09-risk-assessment.md
* docs/architecture/10-target-architecture.md

Esses documentos concentram:

* decisões arquiteturais;
* restrições;
* riscos;
* trade-offs;
* roadmap;
* arquitetura alvo.

---

# Fluxo de Dependência

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

Regras:

* Domain consome Discovery.
* Architecture consome Discovery e Domain.
* Design consome Architecture.
* Discovery não depende de camadas posteriores.
* Domain não modifica Discovery.
* Architecture não modifica Discovery.
* Architecture não modifica Domain.
* Design não modifica Discovery, Domain ou Architecture.

---

# Estado de Encerramento da Camada

A camada Architecture encontra-se formalmente encerrada.

Documentos finais da camada:

* 08-decision-records.md
* 09-risk-assessment.md
* 10-target-architecture.md

A arquitetura possui:

* visão arquitetural consolidada;
* riscos catalogados;
* ADRs registrados;
* roadmap arquitetural definido;
* critérios de prontidão documentados.

Não é necessário retornar às camadas Discovery ou Domain para iniciar a próxima fase do projeto.

---

# Próxima Camada

```text
docs/solution-design/
```

Objetivo:

Transformar a arquitetura aprovada em uma solução implementável.

A camada Design será responsável por definir:

* visão da solução;
* módulos implementáveis;
* contratos de API;
* modelo lógico de dados;
* contratos de integração;
* observabilidade;
* estratégia de testes;
* roadmap de implementação.

---

# Próximo Artefato

```text
docs/design/01-solution-overview.md
```

Fontes primárias obrigatórias:

* docs/architecture/10-target-architecture.md
* docs/architecture/09-risk-assessment.md
* docs/architecture/08-decision-records.md

Fontes secundárias:

* demais documentos da camada Architecture.

---

# Critério de Saída da Camada Architecture

A camada Architecture somente pode ser considerada concluída quando:

* todos os artefatos de 01 a 10 estiverem concluídos;
* ADRs principais estiverem registrados;
* riscos estiverem catalogados;
* arquitetura alvo estiver definida;
* roadmap arquitetural estiver documentado.

Todos os critérios foram atendidos.

Status final:

```text
Architecture = CONCLUÍDA
```
