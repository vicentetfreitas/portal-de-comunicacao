# Review Process

## Objetivo

Padronizar a revisão das Features utilizando um processo único e reproduzível.

O processo deve ser executado antes da implementação e antes da conclusão da Feature.

---

# Etapa 1 — Validação de Estrutura

Verificar:

- existência de todos os artefatos obrigatórios (incluindo `api.md` e `traceability.md` quando exigidos pelo template);
- organização do diretório da Feature;
- nomenclatura dos arquivos;
- conformidade de `feature.yaml` com `specs/foundation/feature-yaml.md`;

Resultado:

PASS ou FAIL.

---

# Etapa 2 — Validação Individual

Revisar cada documento de forma isolada.

Itens:

- completude;
- clareza;
- consistência interna;
- aderência aos templates.

Resultado:

PASS ou FAIL.

---

# Etapa 3 — Consistência Cruzada

Comparar todos os artefatos.

Verificar:

- requisitos presentes em todos os documentos;
- APIs compatíveis com casos de uso;
- testes cobrindo requisitos;
- tarefas cobrindo implementação.

Resultado:

PASS ou FAIL.

---

# Etapa 4 — Rastreabilidade

Validar que seja possível rastrear:

Requisito

↓

Caso de Uso

↓

API

↓

Teste

↓

Tarefa

O artefato `traceability.md` (quando aplicável) deve refletir a mesma cadeia sem divergências.

Resultado:

PASS ou FAIL.

---

# Etapa 5 — Qualidade

Verificar:

- ambiguidades;
- duplicidades;
- conflitos;
- requisitos órfãos;
- requisitos sem testes;
- tarefas faltantes.

Resultado:

PASS ou FAIL.

---

# Etapa 6 — Parecer Final

A revisão deve produzir um relatório contendo:

## Resumo Executivo

Situação geral da Feature.

## Pontos Positivos

Aspectos que atendem aos critérios.

## Não Conformidades

Lista detalhada dos problemas encontrados.

## Classificação

- APPROVED
- APPROVED WITH MINOR ISSUES
- REWORK REQUIRED
- REJECTED

## Ações Recomendadas

Lista priorizada das correções necessárias.

---

# Regra Geral

Nenhuma Feature pode avançar para a implementação ou ser considerada concluída sem aprovação formal do processo de revisão.