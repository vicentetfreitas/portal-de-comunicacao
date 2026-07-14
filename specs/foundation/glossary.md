# Glossary

## Objetivo

Este documento estabelece o vocabulário oficial utilizado nas especificações do Portal de Comunicação.

Seu objetivo é garantir que desenvolvedores, analistas, arquitetos e agentes de IA utilizem os mesmos termos com o mesmo significado durante todo o ciclo de desenvolvimento.

Caso exista divergência entre documentos, este glossário define a terminologia adotada pelas especificações (`specs/`).

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

# Convenções

* Todos os documentos de `specs/` devem utilizar os termos definidos neste glossário.
* Novos termos somente devem ser adicionados quando representarem conceitos recorrentes no projeto.
* Este documento define a terminologia oficial utilizada pelas especificações.
* Alterações neste glossário devem ser avaliadas quanto ao impacto nas especificações existentes.

---

# Referências

* `docs/domain/02-business-glossary.md`
* `docs/domain/03-ubiquitous-language.md`
* `docs/domain/04-domain-concepts.md`
* `specs/foundation/conventions.md`
* `specs/foundation/principles.md`
