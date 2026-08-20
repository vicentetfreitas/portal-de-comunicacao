# Review Process

## Objetivo

Padronizar dois momentos de revisão, semanticamente distintos. Não são estados. Não substituem Validate nem Gates.

O SSOT de estado permanece em `feature.yaml`.

| Momento | Quando | Objeto | Efeito em `status` |
|---------|--------|--------|-------------------|
| **Review de Spec** | `READY_FOR_REVIEW` | Especificação | `APPROVED` ou `DRAFT` |
| **Review de PR** | durante `IMPLEMENTING` | Spec × código × testes × comportamento | Não altera `feature.yaml` automaticamente; Gate 3 é a verificação formal |

---

# Review de Spec

Ocorre **antes** de `READY_FOR_REVIEW` → `APPROVED`.

Avalia a especificação. Não avalia o pull request.

Se houver problemas:

```text
READY_FOR_REVIEW → DRAFT
```

O motivo do retorno é **evidência da revisão** (parecer). Não persistir `REWORK` em `feature.yaml`.

## Etapa 1 — Validação de Estrutura

Verificar:

- existência dos artefatos obrigatórios (incluindo `api.md` e `traceability.md` quando exigidos pelo template);
- organização do diretório da Feature;
- nomenclatura dos arquivos;
- conformidade de `feature.yaml` com `specs/foundation/feature-yaml.md`.

Resultado: PASS ou FAIL.

## Etapa 2 — Validação Individual

Completude; clareza; consistência interna; aderência aos templates.

Resultado: PASS ou FAIL.

## Etapa 3 — Consistência Cruzada

Requisitos presentes nos documentos; APIs compatíveis com casos de uso; testes cobrindo requisitos; tarefas previstas quando já identificadas.

Resultado: PASS ou FAIL.

## Etapa 4 — Rastreabilidade

Requisito → Caso de Uso → API → Teste → Tarefa (quando `tasks.md` já existir).

`traceability.md` (quando aplicável) sem divergências.

Resultado: PASS ou FAIL.

## Etapa 5 — Qualidade

Ambiguidades; duplicidades; conflitos; requisitos órfãos; requisitos sem testes.

Resultado: PASS ou FAIL.

## Etapa 6 — Parecer Final

A revisão deve produzir evidência contendo:

### Resumo Executivo

Situação da spec.

### Pontos Positivos

Aspectos que atendem aos critérios.

### Não Conformidades

Problemas encontrados (motivo de retorno a `DRAFT`, quando houver).

### Classificação do parecer

- APPROVED — transitar `status` para `APPROVED`
- APPROVED WITH MINOR ISSUES — transitar para `APPROVED` com pendências não bloqueantes registradas na evidência
- RETURN_TO_DRAFT — transitar `READY_FOR_REVIEW` → `DRAFT` (equivalente histórico: “REWORK REQUIRED”; **não** é estado)
- REJECTED — não transitar para `APPROVED`; evidência obrigatória

### Ações Recomendadas

Correções priorizadas.

Nenhuma Feature transita para `APPROVED` sem este parecer.

---

# Review de PR

Ocorre **durante** `IMPLEMENTING`.

Avalia aderência entre spec, código, testes e comportamento implementado.

Não substitui Validate (Validate produz evidência de execução de testes/CI).

A verificação formal correspondente no fluxo mínimo é o **Gate 3**.

O parecer de Review de PR **não** altera `status` por si só. `DONE` exige Gate 3, DoD e Gate 6 (além das demais condições de `feature-yaml.md`).

---

# Regra Geral

- Sem Review de Spec aprovada: não há `APPROVED`.
- Sem Review de PR (Gate 3) e DoD (Gate 6): não há `DONE`.
- Implementação de código exige `IMPLEMENTING`, autorizado pelo DoR-Implementation — não pela Review de Spec isoladamente.
