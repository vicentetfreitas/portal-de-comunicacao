# Definition of Done (DoD)

## Objetivo

Este documento define os critérios mínimos que uma Feature deve atender para ser considerada concluída.

Seu objetivo é garantir que todas as Features entregues pelo Portal de Comunicação possuam um padrão consistente de qualidade, documentação, testes e rastreabilidade.

A Definition of Done aplica-se a todas as Features descritas em `specs/features/`.

---

# Critérios Obrigatórios

Uma Feature é considerada **Done** somente quando atender a todos os critérios abaixo.

## 1. Implementação concluída

Todos os requisitos definidos na especificação foram implementados.

Não existem funcionalidades previstas parcialmente implementadas.

---

## 2. Critérios de aceite atendidos

Todos os critérios de aceite definidos na especificação foram validados com sucesso.

---

## 3. Regras de negócio implementadas

Todas as regras de negócio documentadas foram respeitadas pela implementação.

Não existem comportamentos conhecidos em desacordo com a especificação.

---

## 4. Testes executados

Os testes previstos para a Feature foram executados com sucesso.

Quando aplicável, incluem:

* testes unitários;
* testes de integração;
* testes funcionais;
* testes de aceitação.

---

## 5. Qualidade do código

O código segue os padrões definidos pelo projeto.

Não existem erros conhecidos que impeçam a utilização normal da Feature.

---

## 6. Banco de Dados

Quando houver alterações no banco de dados:

* migrations foram criadas;
* scripts foram validados;
* modelo atualizado quando necessário.

---

## 7. APIs atualizadas

Quando houver alteração em APIs:

* contratos atualizados;
* documentação atualizada;
* compatibilidade validada.

---

## 8. Segurança validada

Quando aplicável:

* autenticação validada;
* autorização validada;
* permissões verificadas.

---

## 9. Documentação atualizada

Toda documentação impactada pela Feature foi atualizada.

Inclui, quando aplicável:

* especificação;
* contratos (`api.md` quando aplicável);
* rastreabilidade (`traceability.md` quando exigido pelo template);
* regras de negócio;
* documentação técnica.
* rastreabilidade atualizada quando aplicável.

---

## 10. Tarefas concluídas

Todas as tarefas previstas para a Feature foram concluídas ou encerradas formalmente.

Não existem atividades pendentes relacionadas à implementação.

---

# Critérios Condicionais

Devem ser aplicados quando houver impacto correspondente.

## Observabilidade

Logs, métricas e monitoramento foram atualizados quando necessários.

---

## Integrações

Integrações novas ou alteradas foram validadas.

---

## Migração

Quando aplicável, a estratégia de migração foi executada ou validada.

---

## Performance

Quando houver requisitos de desempenho, os resultados foram validados.

---

# Uma Feature NÃO está Done quando

Uma Feature não pode ser considerada concluída quando existir qualquer uma das situações abaixo:

* critérios de aceite pendentes;
* tarefas pendentes;
* documentação desatualizada;
* testes obrigatórios não executados;
* implementação parcial;
* dependência de correção futura para funcionamento esperado.

---

# Checklist de Validação

Antes de encerrar a Feature, confirme:

* Implementação concluída.
* Critérios de aceite atendidos.
* Regras de negócio implementadas.
* Testes executados.
* Qualidade do código validada.
* Banco de dados atualizado quando aplicável.
* APIs atualizadas quando aplicável.
* Segurança validada quando aplicável.
* Documentação atualizada.
* Tarefas concluídas.

Caso qualquer item obrigatório não seja atendido, a Feature não deve ser considerada concluída.

---

# Responsabilidades

## Desenvolvedor

Responsável por garantir que a implementação atende integralmente à especificação.

---

## Revisor Técnico

Responsável por validar a qualidade técnica da implementação e sua conformidade com os padrões do projeto.

---

## Product Owner / Analista

Responsável por validar que a Feature atende às necessidades do negócio e aos critérios de aceite definidos.

---

# Referências

* `specs/foundation/glossary.md`
* `specs/foundation/principles.md`
* `specs/foundation/workflow.md`
* `specs/foundation/definition-of-ready.md`
* `specs/foundation/feature-yaml.md`
