# 07 - Documentation Architecture

## Objetivo

Este documento estabelece a arquitetura documental oficial do Portal de Comunicação.

Seu objetivo é definir claramente a responsabilidade de cada camada documental, evitando sobreposição de conteúdo, duplicação de informações e múltiplas fontes de verdade (SSOT).

Este documento possui caráter normativo e deverá ser utilizado como referência para toda evolução da documentação do projeto.

---

# Princípios

A documentação do projeto é organizada em camadas.

Cada camada possui uma responsabilidade única.

Nenhum documento poderá assumir responsabilidades pertencentes a outra camada.

Cada informação deverá possuir apenas uma Fonte Oficial de Verdade (Single Source of Truth - SSOT).

---

# Arquitetura Documental

A documentação do projeto está organizada em quatro camadas principais.

```
                        Projeto

                           │

        ┌──────────────────┼──────────────────┐

        │                  │                  │

      docs/             specs/         construction/

                                               │

                                           .cursor/
```

Cada camada possui objetivos distintos.

---

# docs/

## Responsabilidade

Documentar permanentemente o sistema.

Esta camada descreve o produto e sua arquitetura.

Não documenta execução de tarefas.

Não documenta progresso.

Não documenta estado da construção.

## Exemplos

Arquitetura

Banco de Dados

Tecnologia

Infraestrutura

Padrões

Segurança

Governança

Modelo de Domínio

---

# specs/

## Responsabilidade

Especificar o comportamento esperado do sistema.

Esta camada representa a especificação funcional.

Não contém implementação.

Não contém evidências de execução.

Não contém acompanhamento da construção.

## Exemplos

Feature Specification

Use Cases

API Contract

Acceptance Tests

Business Rules

Definition of Ready

Definition of Done

Quality Gates

Templates

Workflow metodológico

---

# construction/

## Responsabilidade

Controlar a execução da construção.

Esta camada representa o estado atual do desenvolvimento.

É responsável por registrar planejamento, progresso, auditorias, revisões e evidências produzidas durante a implementação.

Nenhuma especificação funcional deverá ser criada nesta camada.

## Exemplos

Construction State

Execution Plan

Feature Manifest

PKGs

Status

Progress

Reports

Review

Audit

Decision Log

Closure Report

---

# .cursor/

## Responsabilidade

Automatizar o processo de desenvolvimento.

Esta camada define o comportamento dos agentes.

Não documenta arquitetura.

Não documenta domínio.

Não documenta regras de negócio.

Não documenta especificações.

As regras existentes nesta camada deverão apenas executar os processos definidos pelas demais camadas.

## Exemplos

Agents

Rules

Workflows

Orchestrator

Prompts

Automation

---

# Fonte Oficial de Verdade (SSOT)

Cada categoria de informação possui exatamente uma fonte oficial.

| Informação | SSOT |
|------------|------|
| Arquitetura | docs/architecture |
| Modelo de Domínio | docs/domain |
| Tecnologia | docs/technology |
| Banco de Dados | docs/database |
| Implementação | docs/implementation |
| Especificação Funcional | specs/features |
| Processo SDD | specs/foundation |
| Estado da Construção | construction/ |
| Execução da Feature | construction/features |
| Automação | .cursor |

Nenhuma informação deverá ser duplicada entre camadas.

---

# Regras de Dependência

As camadas possuem dependências unidirecionais.

```
docs
   ↑

specs

   ↑

construction

   ↑

.cursor
```

Significado:

- `docs/` não depende de nenhuma outra camada.
- `specs/` utiliza informações presentes em `docs/`.
- `construction/` utiliza `docs/` e `specs/`.
- `.cursor/` automatiza processos definidos nas demais camadas.

É proibido inverter essas dependências.

---

# Regras de Evolução

Novos documentos somente poderão ser criados quando:

- não existir documento equivalente;
- não existir SSOT para aquela informação;
- a responsabilidade estiver claramente definida.

Caso exista dúvida sobre a localização de um novo documento, deverá ser realizada uma análise de responsabilidade antes de sua criação.

---

# Critérios de Validação

Uma arquitetura documental é considerada consistente quando:

- cada informação possui uma única fonte oficial;
- não existem responsabilidades sobrepostas;
- não existem documentos duplicados;
- todas as camadas possuem responsabilidades claramente definidas;
- as dependências entre camadas são respeitadas.

---

# Governança

A evolução da documentação deverá respeitar esta arquitetura.

Nenhum documento poderá alterar a responsabilidade de uma camada sem atualização prévia deste artefato.

Este documento constitui a referência oficial da arquitetura documental do Portal de Comunicação.