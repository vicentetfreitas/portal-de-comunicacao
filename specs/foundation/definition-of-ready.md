# Definition of Ready (DoR)

## Objetivo

Este documento define os critérios mínimos que uma Feature deve atender antes de iniciar sua implementação.

O objetivo é garantir que o desenvolvimento comece apenas quando houver entendimento suficiente do problema, do comportamento esperado e do impacto da solução, reduzindo retrabalho, ambiguidades e decisões tomadas durante a codificação.

A Definition of Ready aplica-se a todas as Features descritas em `specs/features/`.

---

# Critérios Obrigatórios

Uma Feature é considerada **Ready** quando atender a todos os critérios abaixo.

## 1. Objetivo definido

A Feature possui uma descrição clara do problema que pretende resolver e do valor esperado para o negócio.

---

## 2. Escopo delimitado

O escopo da Feature está claramente definido, incluindo o que faz parte e o que está explicitamente fora do escopo.

---

## 3. Regras de negócio documentadas

As regras de negócio necessárias para implementar a Feature estão documentadas e não apresentam ambiguidades conhecidas.

---

## 4. Casos de uso definidos

Os principais fluxos de interação entre os atores e o sistema foram documentados.

Quando aplicável, devem existir também os fluxos alternativos e de exceção.

---

## 4.1 Contrato de API definido

Quando a Feature expõe APIs, `api.md` deve estar completo e consistente com casos de uso e requisitos.

---

## 4.2 Rastreabilidade consolidada

Quando o template oficial exigir (ex.: `crud-feature`), `traceability.md` deve estar completo e consistente com os demais artefatos.

---

## 5. Critérios de aceite definidos

A Feature possui critérios de aceite objetivos e verificáveis.

Cada critério deve permitir confirmar se o comportamento esperado foi implementado corretamente.

---

## 6. Dependências identificadas

Dependências técnicas ou funcionais estão identificadas.

Exemplos:

* outras Features;
* serviços externos;
* integrações;
* banco de dados;
* autenticação;
* infraestrutura.

---

## 7. Impactos conhecidos

Os principais impactos da Feature foram identificados.

Quando aplicável, devem ser considerados impactos em:

* backend;
* frontend;
* banco de dados;
* APIs;
* segurança;
* integrações;
* observabilidade.

---

## 8. Tarefas identificadas

A implementação foi decomposta em tarefas técnicas suficientes para orientar o desenvolvimento.

---

# Critérios Condicionais

Os critérios abaixo devem ser atendidos quando forem aplicáveis à Feature.

## API

As alterações de contrato da API estão especificadas.

---

## Banco de Dados

As alterações no modelo de dados foram identificadas.

---

## Segurança

Alterações em autenticação, autorização ou permissões foram especificadas.

---

## Migração

Quando houver impacto sobre sistemas legados, a estratégia de migração deve estar documentada.

---

## Integrações

Integrações novas ou modificadas devem possuir contratos e responsabilidades definidos.

---

# Uma Feature NÃO está Ready quando

A implementação depende de decisões ainda não tomadas.

Ou quando existirem dúvidas relevantes sobre:

* regras de negócio;
* comportamento esperado;
* responsabilidades;
* integrações;
* critérios de aceite.

Nesses casos, a implementação deve ser adiada até que as pendências sejam resolvidas.

---

# Checklist de Validação

Antes de iniciar o desenvolvimento, confirme:

* Objetivo definido.
* Escopo delimitado.
* Regras de negócio documentadas.
* Casos de uso definidos.
* Contrato de API definido (quando aplicável).
* Rastreabilidade consolidada (quando exigida pelo template).
* Critérios de aceite definidos.
* Dependências identificadas.
* Impactos conhecidos.
* Tarefas identificadas.
* Terminologia validada conforme glossary.md


Caso qualquer item obrigatório não seja atendido, a Feature não deve iniciar sua implementação.

---

# Responsabilidades

## Product Owner / Analista

Responsável por garantir que a Feature possua contexto funcional suficiente.

---

## Arquiteto

Responsável por validar impactos arquiteturais e dependências técnicas.

---

## Desenvolvedor

Responsável por confirmar que existem informações suficientes para iniciar a implementação sem depender de hipóteses ou interpretações.

---

# Referências

* `specs/foundation/principles.md`
* `specs/foundation/workflow.md`
* `specs/foundation/glossary.md`
* `specs/foundation/feature-yaml.md`
