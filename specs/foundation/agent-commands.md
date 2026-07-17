# Catálogo de Comandos para Agentes

| Campo | Valor |
|--------|--------|
| Artefato | agent-commands.md |
| Camada | Foundation |
| Versão | 1.1 |
| Status | STABLE |

---

# 1. Objetivo

Este documento define o **catálogo oficial de comandos** utilizados para invocar atividades do processo SDD do Portal de Comunicação.

Os comandos substituem prompts extensos que repetem instruções já documentadas no projeto. Cada comando indica **qual atividade executar**; a lógica, critérios, artefatos e validações permanecem nos documentos oficiais referenciados.

**Este catálogo não é um framework, DSL ou processo paralelo.** É apenas uma convenção de invocação para agentes.

O catálogo contém **apenas comandos de alto nível** — intenções significativas do usuário. Etapas internas do workflow (validação cruzada, review, audit, build) são executadas pelo agente conforme os documentos oficiais, sem comando próprio.

---

# 2. Convenção

## Formato geral

```text
<Comando> <Feature>
```

## Identificador da Feature

| Aspecto | Padrão | Exemplo |
|---------|--------|---------|
| Formato | `FT-<DOMAIN>` | `FT-AREA`, `FT-AUTH` |
| Caixa | MAIÚSCULAS | `FT-AREA` |
| Contrato | `specs/foundation/feature-yaml.md` | — |

## Comandos parametrizados

```text
Gate <N> <Feature>
PKG-<NN> <Feature>
```

Exemplos:

```text
Gate 1 FT-AREA
PKG-02 FT-AUTH
```

## O que o agente deve fazer ao receber um comando

1. Identificar a atividade correspondente neste catálogo.
2. Consultar **exclusivamente** os documentos oficiais referenciados.
3. Executar a atividade e suas etapas internas conforme o workflow vigente.
4. Emitir o resultado esperado — sem reinterpretar o processo.

---

# 3. Catálogo

O catálogo possui **8 comandos essenciais**, organizados por intenção do usuário.

| # | Comando | Intenção |
|---|---------|----------|
| 1 | `Specify` | Produzir ou evoluir a Specification |
| 2 | `Gate` | Executar um Quality Gate |
| 3 | `Readiness` | Validar prontidão pré-Construction |
| 4 | `Execute Feature` | Iniciar Sessão da Feature |
| 5 | `PKG-<NN>` | Implementar um Construction Package |
| 6 | `Close` | Encerrar a Feature |
| 7 | `Status` | Consultar estado |
| 8 | `Report` | Gerar relatório |

---

## 3.1 `Specify`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Produzir, evoluir ou refinar os artefatos de Specification conforme o template em `feature.yaml`. |
| **Quando utilizar** | Elaboração inicial; retorno após `REWORK`; refinamento pós-`Gate 1` (correção de NCs documentais). |
| **Artefatos envolvidos** | `feature.yaml`; artefatos do template em `specs/features/<feature>/`. |
| **Resultado esperado** | Artefatos completos e consistentes; `READY_FOR_REVIEW` (elaboração) ou `APPROVED` (refinamento com Specification congelada). |
| **Referências** | `.cursor/rules/workflows/specification-flow.mdc`; `.cursor/orchestrator/specification-orchestrator.mdc`; `specs/templates/<template>/`. |

**Etapas internas** (sem comando próprio): validação cruzada entre artefatos (Specification-flow, Etapa 9); ajustes pontuais de não conformidades do `Gate 1`.

---

## 3.2 `Gate`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Executar um Quality Gate formal da Feature. |
| **Sintaxe** | `Gate <N> <Feature>` — onde `<N>` é `1` a `6`. |
| **Quando utilizar** | Conforme o momento do ciclo de vida (ver tabela abaixo). |
| **Resultado esperado** | Parecer ou status do Gate (`APPROVED`, `REWORK`, `REJECTED`, etc.) conforme `feature-quality-gates.md`. |
| **Referências** | `specs/foundation/feature-quality-gates.md`; `specs/foundation/review-process.md` (Gate 1); `specs/foundation/definition-of-done.md` (Gate 6). |

| Gate | Nome | Momento típico |
|------|------|----------------|
| 1 | Specification Ready | Com `status.specification: READY_FOR_REVIEW` |
| 2 | Architecture Review | Antes ou durante Construction |
| 3 | Implementation Review | Após PKGs ou no encerramento |
| 4 | Documentation Review | Após implementação |
| 5 | Feature Readiness Review | No encerramento (também executado por `Close`) |
| 6 | Definition of Done | Após `Close` e Gates anteriores aprovados |

**Nota:** `Gate 5` é etapa interna de `Close`. Utilize `Gate 5` apenas quando precisar executar esse Gate de forma isolada.

---

## 3.3 `Readiness`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Validar Definition of Ready e dependências **antes** de iniciar Construction. |
| **Quando utilizar** | Com `status.specification: APPROVED`; imediatamente antes de `Execute Feature`. |
| **Artefatos envolvidos** | `feature.yaml`; artefatos em `specs/features/<feature>/`; dependências declaradas. |
| **Resultado esperado** | Prontidão confirmada ou lista de bloqueadores. |
| **Referências** | `specs/foundation/definition-of-ready.md`; `specs/templates/crud-feature/README.md`. |

**Distinção:** `Readiness` é pré-Construction. `Gate 5` (Feature Readiness Review) é pós-implementação e integra `Close`.

---

## 3.4 `Execute Feature`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Iniciar a **Sessão da Feature** — carregar contexto via SSOD, criar Snapshot e congelar `session.md`. |
| **Quando utilizar** | Após `Readiness` aprovado; antes do primeiro PKG. |
| **Artefatos envolvidos** | `feature-manifest.yaml`; `construction-state.yaml`; `session.md`; artefatos descobertos via manifesto. |
| **Resultado esperado** | `phase: execution`; Session imutável; contexto pronto para PKGs. |
| **Referências** | `construction/11-feature-execution-workflow.md`; `.cursor/rules/workflows/feature-construction-workflow.mdc`. |

---

## 3.5 `PKG-<NN>`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Executar um Construction Package (implementação focada). |
| **Sintaxe** | `PKG-01 FT-AREA`, `PKG-02 FT-AREA`, etc. |
| **Quando utilizar** | Com Session ativa; respeitando dependências entre PKGs. |
| **Artefatos envolvidos** | `construction-state.yaml`; `pkg-<NN>/status.md` (VALIDATION SUMMARY); `pkg-<NN>/evidence/`; código; `session.md` (somente leitura). |
| **Resultado esperado** | PKG em `DONE` ou `BLOCKED`; seção **VALIDATION SUMMARY** em `status.md` (status: `PASS`, `BUILD_FAILURE`, `ENVIRONMENT_FAILURE` ou `PENDING_REVALIDATION`); resumo operacional curto. |
| **Referências** | `construction/11-feature-execution-workflow.md`; `construction/templates/pkg-validation-summary.md`. |

---

## 3.6 `Close`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Encerrar formalmente a Feature — executa a sequência consolidada de encerramento. |
| **Quando utilizar** | Todos os PKGs em `DONE`; Session ativa. |
| **Artefatos envolvidos** | `construction-state.yaml`; `review/`; `reports/`; `construction/09-progress.md`. |
| **Resultado esperado** | `phase: closed`; `closure-report.md`; estados finais de review, audit, readiness e build. |
| **Referências** | `construction/11-feature-execution-workflow.md` (Fase 3); `.cursor/orchestrator/construction-orchestrator.mdc`. |

**Etapas internas** (sem comando próprio):

```text
Closure → Review → Audit → Readiness (Gate 5) → Build (mvn clean verify)
```

Alias aceito no Engineering Framework: `Encerrar Feature <Feature>`.

---

## 3.7 `Status`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Consultar estado atual da Feature sem alterar artefatos. |
| **Quando utilizar** | A qualquer momento; para decidir o próximo comando. |
| **Artefatos envolvidos** | `feature.yaml`; `construction-state.yaml`; `pkg-<NN>/status.md`; `session.md`. |
| **Resultado esperado** | Resumo: fase, PKG ativo, Gates pendentes, bloqueios. |
| **Referências** | `construction/11-feature-execution-workflow.md` (STATE-02); `specs/foundation/feature-yaml.md`. |

---

## 3.8 `Report`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Gerar relatório sobre a Feature. |
| **Quando utilizar** | Quando o usuário precisar de consolidação documental explícita (progresso, PKG, encerramento). |
| **Artefatos envolvidos** | `reports/` da Feature; artefatos de spec e construction conforme escopo solicitado. |
| **Resultado esperado** | Relatório estruturado. O escopo (progresso, PKG, closure) pode ser indicado na mesma linha do comando. |
| **Referências** | `construction/11-feature-execution-workflow.md`. |

Exemplo com escopo explícito: `Report closure FT-AUTH` — não constitui comando separado; é `Report` com qualificador textual.

---

# 4. Regras

## 4.1 Governança

| ID | Regra |
|----|-------|
| CMD-01 | Comandos **não alteram** o processo SDD, Gates, templates ou workflows. |
| CMD-02 | Comandos **apenas invocam** atividades já definidas nos documentos oficiais. |
| CMD-03 | Toda lógica permanece nos artefatos oficiais — **nunca** duplicada no prompt. |
| CMD-04 | Etapas internas de um comando **não** geram novos comandos no catálogo. |
| CMD-05 | Consultar SSOT/SSOD antes de inferir caminhos (`feature.yaml`, `feature-manifest.yaml`, `construction-state.yaml`). |

## 4.2 Dependências

| ID | Regra |
|----|-------|
| CMD-06 | `Gate 1` exige `READY_FOR_REVIEW`. |
| CMD-07 | `Readiness` exige `status.specification: APPROVED`. |
| CMD-08 | `Execute Feature` exige Readiness pré-Construction aprovado. |
| CMD-09 | `PKG-<NN>` exige Session ativa (`SESSION-01`). |
| CMD-10 | `Close` exige todos os PKGs em `DONE`. |
| CMD-11 | Nenhum Gate pode ser ignorado — use `Gate <N>` ou absorva via `Close` quando aplicável. |

## 4.3 Restrições

| ID | Regra |
|----|-------|
| CMD-12 | `Status` e `Report` não alteram artefatos. |
| CMD-13 | Durante `PKG-<NN>`, é proibido `Close`, build global (`mvn clean verify`) ou atualização de documentação global. |
| CMD-14 | Ao concluir `PKG-<NN>`, preencher **VALIDATION SUMMARY** em `pkg-<NN>/status.md` (VAL-01 + VAL-02); log completo em `evidence/`. |
| CMD-15 | Status de validação: `PASS` \| `BUILD_FAILURE` \| `ENVIRONMENT_FAILURE` \| `PENDING_REVALIDATION` — determinação por condições observáveis (VAL-02), sem prioridade fixa. |
| CMD-16 | Artefatos por PKG: somente `status.md` + `evidence/*.log` opcional (ART-01) — ver `pkg-artifact-model.md`. |

---

# 5. Exemplos

## Ciclo típico — Feature CRUD

```text
Specify FT-AREA
Gate 1 FT-AREA
Specify FT-AREA
Readiness FT-AREA
Execute Feature FT-AREA
PKG-01 FT-AREA
PKG-02 FT-AREA
Close FT-AREA
Gate 6 FT-AREA
Status FT-AREA
```

## Apenas consulta

```text
Status FT-AUTH
```

## Revisão de Specification (sem Construction)

```text
Gate 1 FT-AREA
```

## Um PKG

```text
PKG-03 FT-AUTH
```

---

# 6. Mapa de referências

| Comando | Documento principal |
|---------|---------------------|
| `Specify` | `.cursor/rules/workflows/specification-flow.mdc` |
| `Gate` | `specs/foundation/feature-quality-gates.md` |
| `Readiness` | `specs/foundation/definition-of-ready.md` |
| `Execute Feature`, `PKG-<NN>`, `Close` | `construction/11-feature-execution-workflow.md` |
| `Status`, `Report` | `construction/11-feature-execution-workflow.md` (STATE-02) |
| Identidade | `specs/foundation/feature-yaml.md` |

---

# 7. Evolução

Novos comandos somente quando representarem intenções **já formalizadas** e **não absorvíveis** por comando existente.

Antes de adicionar: verificar se a atividade é etapa interna de outro comando.

---

# Apêndice — Consolidação v1.0 → v1.1

| Comando v1.0 | Destino v1.1 | Justificativa |
|--------------|--------------|---------------|
| `Validate` | `Specify` | Validação cruzada é Etapa 9 do Specification-flow — consequência natural de `Specify`. |
| `Refine` | `Specify` | Refinamento pós-Gate 1 é evolução documental da Specification — mesma intenção de produção/evolução. |
| `Gate 1` … `Gate 6` (entradas separadas) | `Gate <N>` | Família única parametrizada; mesma mecânica de invocação. |
| `Review` | `Close` | Revisão técnica consolidada é etapa obrigatória do encerramento (Fase 3). |
| `Audit` | `Close` | Auditoria de conformidade é etapa obrigatória do encerramento (Fase 3). |
| `Build` | `Close` | `mvn clean verify` é exclusivo do encerramento (BUILD-01) — detalhe interno, não intenção separada. |
| `Gate 5` (entrada separada) | `Close` (padrão) / `Gate 5` (isolado) | Feature Readiness Review integra `Close`; comando `Gate 5` mantido apenas para execução isolada. |
| `Report Closure`, `Report PKG` (variantes) | `Report` | Escopo indicado como qualificador textual — um único comando de relatório. |

**Comandos mantidos sem alteração de nome:** `Specify`, `Readiness`, `Execute Feature`, `PKG-<NN>`, `Close`, `Status`, `Report`.

**Redução:** 18 entradas de catálogo → **8 comandos essenciais**.

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.0 | 2026-07-13 | Catálogo inicial de comandos para agentes |
| 1.1 | 2026-07-13 | Consolidação minimalista — redução de redundâncias |
