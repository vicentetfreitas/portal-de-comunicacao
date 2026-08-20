# Glossary

## Objetivo

Este documento é o SSOT da **terminologia de engenharia e framework** (SDD, estados da Feature, DoR, Gates, Review, DoD).

Conceitos de **negócio** têm SSOT em `docs/domain/02-business-glossary.md` (linguagem ubíqua em `03-ubiquitous-language.md`). Em divergência de termo de negócio, prevalece o glossário de domínio. Em divergência de termo de processo/framework, prevalece este documento.

---

# Termos do Domínio

## Área

Unidade organizacional da Unimed Ceará responsável por produzir, administrar ou consumir informações e conteúdos no Portal de Comunicação.

---

## Campanha

Conjunto organizado de comunicações relacionadas a um objetivo específico, podendo possuir período de vigência, público-alvo, documentos e publicações associados.

---

## Colaborador

Pessoa pertencente à organização que pode acessar o Portal de Comunicação conforme suas permissões.

Um colaborador pode possuir um ou mais perfis de acesso.

---

## Compartilhamento

Mecanismo que concede acesso a um conteúdo para usuários, equipes, áreas ou outros grupos autorizados.

---

## Documento

Conteúdo institucional gerenciado pelo Portal de Comunicação.

Um Documento possui ciclo de vida, versionamento, regras de visibilidade e permissões de acesso.

Um Documento não representa necessariamente um arquivo físico.

---

## Equipe

Grupo organizacional formado por colaboradores pertencentes a uma determinada área ou contexto organizacional.

---

## Perfil de Acesso

Conjunto de permissões atribuídas a um usuário ou colaborador para executar operações dentro do sistema.

---

## Publicação

Disponibilização de um conteúdo para seu público-alvo.

Uma publicação pode estar associada a um Documento ou a uma Campanha.

---

## Usuário

Identidade autenticada que acessa o Portal de Comunicação.

O usuário é responsável pela execução das operações permitidas pelo seu Perfil de Acesso.

---

## Versão do Documento

Representa uma revisão específica de um Documento.

Cada versão possui seu próprio histórico, data de criação e metadados.

---

## Visibilidade

Conjunto de regras que determina quais usuários ou grupos podem visualizar determinado conteúdo.

---

# Termos de Especificação (SDD)

## Stakeholder

Pessoa, área ou organização interessada ou impactada por uma Feature ou pelo sistema.

## Critério de Aceite

Condição objetiva que deve ser satisfeita para que uma funcionalidade seja considerada correta.

Os critérios de aceite são utilizados como referência para validação funcional e testes.

---

## Caso de Uso

Descrição das interações entre atores e o sistema para atingir um objetivo específico.

---

## Especificação

Documento que descreve completamente uma funcionalidade, incluindo objetivos, regras de negócio, requisitos, critérios de aceite e tarefas necessárias para sua implementação.

---

## Feature

Unidade funcional de desenvolvimento.

Cada Feature possui sua própria especificação e pode resultar em alterações no backend, frontend, banco de dados, infraestrutura ou documentação.

---

## Regra de Negócio

Restrição, política ou comportamento obrigatório definido pelo domínio do sistema.

Toda implementação deve respeitar as regras de negócio estabelecidas.

---

## Requisito Funcional

Capacidade que o sistema deve oferecer ao usuário.

---

## Requisito Não Funcional

Característica de qualidade do sistema, como desempenho, segurança, disponibilidade, escalabilidade ou observabilidade.

---

## Tarefa

Atividade técnica necessária para implementar uma Feature.

Uma tarefa pode envolver desenvolvimento, testes, documentação, infraestrutura ou banco de dados.

---

# Termos do Framework (ciclo da Feature)

## status (feature.yaml)

SSOT do estado da Feature. Valores persistentes: `DRAFT`, `READY_FOR_REVIEW`, `APPROVED`, `IMPLEMENTING`, `DONE`. Contrato: `feature-yaml.md`.

`REWORK` não é estado persistente. Retrabalho de spec: `READY_FOR_REVIEW` → `DRAFT`.

## DoR-Spec

Contexto da Definition of Ready que autoriza `DRAFT` → `READY_FOR_REVIEW`.

## DoR-Implementation

Contexto da Definition of Ready que autoriza `APPROVED` → `IMPLEMENTING`.

## Gate

Verificação formal de critérios. Não é estado. No fluxo mínimo: Gate 1, 3 e 6 obrigatórios; 2, 4 e 5 condicionais.

## Review de Spec

Revisão da especificação em `READY_FOR_REVIEW`. Resultado: `APPROVED` ou retorno a `DRAFT`.

## Review de PR

Revisão de aderência spec/código/testes durante `IMPLEMENTING` (Gate 3). Não substitui Validate. Não altera `status` automaticamente.

## Validate

Atividade de evidência (testes/CI). Não é estado. Não altera `feature.yaml`.

---

# Convenções

* Termos de **framework** (estados, DoR, Gates, Review, DoD): este glossário.
* Termos de **negócio**: `docs/domain/02-business-glossary.md`.
* Novos termos de framework somente quando recorrentes.
* Alterações neste glossário devem ser avaliadas quanto ao impacto no processo SDD.

---

# Referências

* `docs/domain/02-business-glossary.md` — SSOT de conceitos de negócio
* `docs/domain/03-ubiquitous-language.md`
* `specs/foundation/feature-yaml.md`
* `specs/foundation/conventions.md`
* `specs/foundation/principles.md`
