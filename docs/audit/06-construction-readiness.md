# Construction Readiness Assessment

## Objetivo

Avaliar a prontidão da solução para início da construção.

Este documento consolida os resultados das auditorias realizadas nas camadas:

* Discovery
* Domain
* Architecture
* Solution Design
* Implementation

O objetivo é determinar se existe informação suficiente, consistência suficiente e maturidade suficiente para iniciar a construção do produto com risco controlado.

---

# Escopo Avaliado

## Discovery

Objetivo:

Validar entendimento do problema.

Status:

* NÃO AVALIADO
* APROVADO
* APROVADO COM RESSALVAS
* REPROVADO

---

## Domain

Objetivo:

Validar entendimento do negócio.

Status:

* NÃO AVALIADO
* APROVADO
* APROVADO COM RESSALVAS
* REPROVADO

---

## Architecture

Objetivo:

Validar arquitetura alvo.

Status:

* NÃO AVALIADO
* APROVADO
* APROVADO COM RESSALVAS
* REPROVADO

---

## Solution Design

Objetivo:

Validar desenho detalhado da solução.

Status:

* NÃO AVALIADO
* APROVADO
* APROVADO COM RESSALVAS
* REPROVADO

---

## Implementation

Objetivo:

Validar padrões de construção.

Status:

* NÃO AVALIADO
* APROVADO
* APROVADO COM RESSALVAS
* REPROVADO

---

# Checklist de Prontidão

## Entendimento do Problema

| Critério                     | Status |
| ---------------------------- | ------ |
| Problema claramente definido |        |
| Objetivos definidos          |        |
| Escopo definido              |        |
| Stakeholders identificados   |        |
| Restrições identificadas     |        |

Resultado:

* APROVADO
* REPROVADO

---

## Entendimento do Domínio

| Critério              | Status |
| --------------------- | ------ |
| Processos mapeados    |        |
| Regras documentadas   |        |
| Glossário definido    |        |
| Contextos definidos   |        |
| Eventos identificados |        |

Resultado:

* APROVADO
* REPROVADO

---

## Arquitetura

| Critério              | Status |
| --------------------- | ------ |
| Arquitetura aprovada  |        |
| NFRs cobertos         |        |
| Integrações definidas |        |
| Riscos identificados  |        |
| ADRs documentadas     |        |

Resultado:

* APROVADO
* REPROVADO

---

## Solution Design

| Critério                 | Status |
| ------------------------ | ------ |
| APIs definidas           |        |
| Contratos definidos      |        |
| Modelo de dados definido |        |
| Ownership definido       |        |
| Fluxos detalhados        |        |

Resultado:

* APROVADO
* REPROVADO

---

## Implementation

| Critério                      | Status |
| ----------------------------- | ------ |
| Estrutura de código definida  |        |
| Estratégia de testes definida |        |
| Pipeline definida             |        |
| Observabilidade definida      |        |
| Segurança definida            |        |

Resultado:

* APROVADO
* REPROVADO

---

# Matriz de Maturidade

| Área            | Score |
| --------------- | ----- |
| Discovery       |       |
| Domain          |       |
| Architecture    |       |
| Solution Design |       |
| Implementation  |       |

---

## Classificação

| Score  | Classificação             |
| ------ | ------------------------- |
| 90-100 | Pronto para Construção    |
| 80-89  | Pronto com Baixo Risco    |
| 70-79  | Pronto com Risco Moderado |
| 60-69  | Necessita Ajustes         |
| < 60   | Não Pronto                |

---

# Dependências Críticas

## Dependências Externas

| ID | Dependência | Status | Impacto |
| -- | ----------- | ------ | ------- |

---

## Dependências Internas

| ID | Dependência | Status | Impacto |
| -- | ----------- | ------ | ------- |

---

# Riscos Bloqueadores

Registrar apenas riscos que possam impedir o início da construção.

| ID | Risco | Impacto | Ação Necessária |
| -- | ----- | ------- | --------------- |

---

# Lacunas Identificadas

## Documentação

| Item | Descrição |
| ---- | --------- |

---

## Arquitetura

| Item | Descrição |
| ---- | --------- |

---

## Implementação

| Item | Descrição |
| ---- | --------- |

---

# Decisão de Go / No-Go

## Critérios de Go

Todos os itens abaixo devem estar atendidos:

* Discovery aprovado
* Domain aprovado
* Architecture aprovada
* Solution Design aprovado
* Implementation aprovada
* Sem riscos bloqueadores abertos
* Sem lacunas críticas abertas

---

## Resultado

### GO

A construção pode iniciar.

ou

### GO COM RESSALVAS

A construção pode iniciar mediante acompanhamento dos riscos registrados.

ou

### NO-GO

A construção não deve iniciar até que os itens bloqueadores sejam resolvidos.

---

# Plano de Ações

| Prioridade | Ação | Responsável | Prazo |
| ---------- | ---- | ----------- | ----- |

---

# Aprovações

| Papel                   | Nome | Data | Aprovação |
| ----------------------- | ---- | ---- | --------- |
| Product Owner           |      |      |           |
| Especialista de Domínio |      |      |           |
| Arquiteto de Software   |      |      |           |
| Tech Lead               |      |      |           |
| Sponsor                 |      |      |           |

---

# Parecer Executivo

## Resumo

Descrever o estado geral da solução.

---

## Principais Riscos

Listar os riscos mais relevantes para a construção.

---

## Recomendações

Listar recomendações para execução do MVP.

---

## Decisão Final

* GO
* GO COM RESSALVAS
* NO-GO
