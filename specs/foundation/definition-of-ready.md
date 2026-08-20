# Definition of Ready (DoR)

## Objetivo

Este documento define os critérios mínimos de prontidão de uma Feature.

Há **um** conceito de Definition of Ready, aplicado em **dois contextos**. Não existem dois DoRs nem estados chamados DoR.

| Contexto | Transição que autoriza |
|----------|------------------------|
| **DoR-Spec** | `DRAFT` → `READY_FOR_REVIEW` |
| **DoR-Implementation** | `APPROVED` → `IMPLEMENTING` |

Readiness é a **avaliação** desses critérios. Não é estado. O estado permanece em `specs/features/<slug>/feature.yaml` (`specs/foundation/feature-yaml.md`).

A Definition of Ready aplica-se a todas as Features descritas em `specs/features/`.

---

# DoR-Spec

Critérios para permitir revisão formal da especificação.

Uma Feature atende DoR-Spec quando:

## 1. Objetivo definido

A Feature possui uma descrição clara do problema que pretende resolver e do valor esperado para o negócio.

## 2. Escopo delimitado

O escopo da Feature está claramente definido, incluindo o que faz parte e o que está explicitamente fora do escopo.

## 3. Regras de negócio documentadas

As regras de negócio necessárias estão documentadas e não apresentam ambiguidades conhecidas. Regras transversais referenciam `docs/domain/09-business-rules.md` — não as reescrever.

## 4. Casos de uso definidos

Os principais fluxos de interação entre os atores e o sistema foram documentados.

Quando aplicável, devem existir também os fluxos alternativos e de exceção.

## 4.1 Contrato de API definido

Quando a Feature expõe APIs, `api.md` deve estar completo e consistente com casos de uso e requisitos.

## 4.2 Rastreabilidade consolidada

Quando o template oficial exigir (ex.: `crud-feature`), `traceability.md` deve estar completo e consistente com os demais artefatos.

## 5. Critérios de aceite definidos

A Feature possui critérios de aceite objetivos e verificáveis.

## 6. Artefatos obrigatórios presentes

Os artefatos exigidos pelo template da Feature existem em `specs/features/<slug>/`.

## 7. Sem bloqueadores conhecidos para a revisão da spec

Não há dúvidas conhecidas que impeçam um revisor de avaliar a especificação.

DoR-Spec **não** exige `tasks.md` completo. Tarefas identificadas pertencem ao DoR-Implementation.

A verificação formal de DoR-Spec no fluxo cotidiano é o **Gate 1**.

---

# DoR-Implementation

Critérios para autorizar implementação. Exige DoR-Spec **ainda válido** e spec em `APPROVED`.

Além dos critérios de DoR-Spec:

## 8. Dependências identificadas

Dependências técnicas ou funcionais estão identificadas.

Exemplos: outras Features; serviços externos; integrações; banco de dados; autenticação; infraestrutura.

## 9. Impactos conhecidos

Os principais impactos da Feature foram identificados.

Quando aplicável: backend; frontend; banco de dados; APIs; segurança; integrações; observabilidade.

## 10. Tarefas identificadas

A implementação foi decomposta em tarefas técnicas suficientes. `tasks.md` deve existir quando o DoR-Implementation o exigir. `tasks.md` é plano/progresso operacional — **não** SSOT de estado.

A Feature **não** entra em `IMPLEMENTING` no primeiro commit. Entra após esta avaliação (Readiness de implementação) estar aprovada.

---

# Critérios Condicionais

Aplicam-se a DoR-Spec ou DoR-Implementation conforme o impacto. Devem estar atendidos **antes de `IMPLEMENTING`** quando aplicáveis à Feature.

## API

As alterações de contrato da API estão especificadas.

## Banco de Dados

As alterações no modelo de dados foram identificadas.

## Segurança

Alterações em autenticação, autorização ou permissões foram especificadas.

## Migração

Quando houver impacto sobre sistemas legados, a estratégia de migração deve estar documentada.

## Integrações

Integrações novas ou modificadas devem possuir contratos e responsabilidades definidos.

---

# Uma Feature NÃO está Ready

**DoR-Spec não atendido:** dúvidas que impedem revisar a spec (objetivo, escopo, BR, comportamento, aceite).

**DoR-Implementation não atendido:** a implementação dependeria de decisões não tomadas, ou faltam tarefas, dependências ou impactos relevantes.

Nesses casos não transitar `status`. A implementação não começa.

---

# Checklist

## Antes de `DRAFT` → `READY_FOR_REVIEW` (DoR-Spec)

* Objetivo definido.
* Escopo delimitado.
* Regras de negócio documentadas.
* Casos de uso definidos.
* Contrato de API definido (quando aplicável).
* Rastreabilidade consolidada (quando exigida pelo template).
* Critérios de aceite definidos.
* Artefatos obrigatórios presentes.
* Sem bloqueadores conhecidos para a revisão da spec.
* Gate 1 verificado.
* Terminologia de negócio conforme `docs/domain/02-business-glossary.md`; termos de framework conforme este `glossary.md`.

## Antes de `APPROVED` → `IMPLEMENTING` (DoR-Implementation)

* Spec em `APPROVED`.
* DoR-Spec ainda válido.
* Dependências identificadas.
* Impactos conhecidos.
* Tarefas identificadas (`tasks.md` quando exigido).
* Critérios condicionais aplicáveis atendidos.

---

# Responsabilidades

## Product Owner / Analista

Garantir contexto funcional suficiente (DoR-Spec).

## Arquiteto

Validar impactos e dependências técnicas (DoR-Implementation, e Gate 2 quando aplicável).

## Desenvolvedor

Confirmar que existem informações suficientes para implementar sem hipóteses (DoR-Implementation).

---

# Referências

* `specs/foundation/feature-yaml.md`
* `specs/foundation/feature-quality-gates.md`
* `specs/foundation/glossary.md`
* `specs/foundation/workflow.md`
* `docs/domain/02-business-glossary.md` — conceitos de negócio
