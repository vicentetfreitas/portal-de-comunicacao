# Definition of Done (DoD)

## Objetivo

Este documento define os critérios mínimos que uma Feature deve atender para ser considerada concluída.

Seu objetivo é garantir que todas as Features entregues pelo Portal de Comunicação possuam um padrão consistente de qualidade, documentação, testes e rastreabilidade.

A Definition of Done aplica-se a todas as Features descritas em `specs/features/`.

DoD é **critério**, não estado. A transição `IMPLEMENTING` → `DONE` em `feature.yaml` exige este documento, **Gate 3** (Review de PR) e **Gate 6** (verificação formal do DoD), além de validação realizada e evidências disponíveis.

Validate produz evidência; não altera `status`. Git, CI, logs e `tasks.md` não são SSOT de estado.

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

Antes de transitar `status` para `DONE`, confirme:

* Implementação concluída.
* Critérios de aceite atendidos.
* Regras de negócio implementadas.
* Testes executados (evidência de Validate/CI).
* Qualidade do código validada.
* Review de PR realizada (Gate 3 PASS).
* Banco de dados atualizado quando aplicável.
* APIs atualizadas quando aplicável.
* Segurança validada quando aplicável.
* Documentação atualizada.
* Tarefas concluídas.
* Gate 6 PASS.

Caso qualquer item obrigatório não seja atendido, `status` permanece `IMPLEMENTING`.

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

# Construction — Workstream Frontend (referência histórica)

Não substitui o DoD deste documento nem o `status` em `feature.yaml`. Aplica-se apenas a workstreams construction v4.1 em transição. E2E no closure da Feature permanece evidência (Validate), não estado.

Durante a **construção** histórica, o template CRUD frontend adotava:

| Fase | Critério |
|------|----------|
| PKG-FE-01..05 | Gate PKG: lint, typecheck, testes unitários, build — **PASS** em cada PKG |
| PKG-FE-06 | Gate PKG + **Playwright** (`test:e2e`) — estabilização E2E e encerramento |
| Feature frontend aprovada | PKG-FE-06 com validação E2E verde |

Detalhes: `construction/16-frontend-validation-gates.md`, `construction/17-frontend-e2e-behavior-policy.md` (E2E-02), `construction/golden-template/FT-SINGULAR.md`.

---

# Referências

* `specs/foundation/glossary.md`
* `specs/foundation/principles.md`
* `specs/foundation/workflow.md`
* `specs/foundation/definition-of-ready.md`
* `specs/foundation/feature-yaml.md`
