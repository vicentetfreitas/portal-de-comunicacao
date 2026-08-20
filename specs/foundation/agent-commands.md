# Catálogo de Comandos para Agentes

| Campo | Valor |
|--------|--------|
| Artefato | agent-commands.md |
| Camada | Foundation |
| Versão | 1.3 |
| Status | STABLE |
| Categoria documental | SSOT |

---

# 1. Objetivo

Este documento define o **catálogo oficial de comandos** utilizados para invocar atividades do processo SDD do Portal de Comunicação.

Os comandos substituem prompts extensos que repetem instruções já documentadas no projeto. Cada comando indica **qual atividade executar**; a lógica, critérios, artefatos e validações permanecem nos documentos oficiais referenciados.

**Este catálogo não é um framework, DSL ou processo paralelo.** É apenas uma convenção de invocação para agentes.

O fluxo cotidiano é o da Etapa 2 (`specs/foundation/development-workflow.md`):

```text
DRAFT → READY_FOR_REVIEW → APPROVED → IMPLEMENTING → DONE
```

Estado em `feature.yaml`. Comandos invocam atividades; não redefinem a máquina de estados.

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
| Paths | `specs/foundation/path-conventions.md` | slug kebab-case |

## O que o agente deve fazer ao receber um comando

1. Identificar a atividade correspondente neste catálogo.
2. Consultar **exclusivamente** os documentos oficiais referenciados.
3. Executar a atividade e suas etapas internas conforme o **workflow vigente** (`development-workflow.md` para o cotidiano).
4. Emitir o resultado esperado — sem reinterpretar o processo.
5. Se a tarefa depender **exclusivamente** de Session, PKG, Snapshot, Cache ou orchestrator v4.1: **não improvisar equivalência**; interromper e solicitar decisão humana.

---

# 3. Catálogo cotidiano (Etapa 2)

Seis comandos de alto nível. São o padrão para Claude Code e para o trabalho diário.

| # | Comando | Intenção |
|---|---------|----------|
| 1 | `Specify` | Produzir ou evoluir a Specification |
| 2 | `Readiness` | Avaliar DoR-Spec e/ou DoR-Implementation (não é estado) |
| 3 | `Implement` | Executar `tasks.md` após `IMPLEMENTING` autorizado |
| 4 | `Validate` | Evidência da camada alterada; **não** altera `feature.yaml` |
| 5 | `Review` | Review de Spec ou Review de PR (distintos) |
| 6 | `Status` | Consultar estado sem alterar artefatos |

Estes comandos **não** reimplementam DoR, DoD, Gates nem governança: apenas invocam os documentos oficiais.

---

## 3.1 `Specify`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Produzir, evoluir ou refinar os artefatos de Specification conforme o template em `feature.yaml`. |
| **Quando utilizar** | Elaboração inicial; retorno após lacuna documental; refinamento da spec. |
| **Artefatos envolvidos** | `feature.yaml`; artefatos do template em `specs/features/<slug>/`. |
| **Resultado esperado** | Artefatos consistentes com `feature.yaml`; spec utilizável para Readiness. |
| **Não faz** | Implementar código no lugar da especificação. |
| **Referências** | `specs/foundation/workflow.md`; `specs/foundation/feature-yaml.md`; `specs/templates/`; a feature em `specs/features/<slug>/`. |

Etapas internas (sem comando próprio): validação cruzada entre artefatos da spec; consulta a `docs/domain/` e `docs/architecture/` quando a feature o exigir.

---

## 3.2 `Readiness`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Avaliar Definition of Ready no contexto pedido. Readiness **não** é estado. |
| **Quando utilizar** | DoR-Spec: antes de `DRAFT` → `READY_FOR_REVIEW`. DoR-Implementation: antes de `APPROVED` → `IMPLEMENTING`. |
| **Artefatos envolvidos** | `feature.yaml`; artefatos em `specs/features/<slug>/`; `tasks.md` no DoR-Implementation. |
| **Resultado esperado** | Prontidão confirmada ou lista de bloqueadores. **Não** transitar para `IMPLEMENTING` nem implementar código enquanto o DoR-Implementation não estiver atendido. |
| **Referências** | `specs/foundation/definition-of-ready.md`; `specs/foundation/feature-yaml.md`. |

---

## 3.3 `Implement`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Executar o plano de implementação da feature. |
| **Quando utilizar** | Após Readiness de implementação aprovado (`status: IMPLEMENTING`). |
| **Artefatos envolvidos** | `specs/features/<slug>/tasks.md` (plano principal); spec da feature; `docs/implementation/` conforme a camada. |
| **Resultado esperado** | Tasks executadas sem redefinir comportamento de produto. |
| **Não faz** | Criar Session, PKG, Snapshot ou Cache; alterar regras de negócio fora da spec. |
| **Referências** | `specs/foundation/development-workflow.md`; `specs/foundation/path-conventions.md`; `docs/implementation/`. |

Substitui, no fluxo cotidiano, `Execute Feature` + `PKG-<NN>` (ver `construction/history/agent-commands-catalog-appendix-2026-08-20.md`).

---

## 3.4 `Validate`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Executar as validações apropriadas à camada alterada e registrar evidência observável. |
| **Quando utilizar** | Após implementação; antes ou durante preparação de PR. |
| **Resultado esperado** | Distinção entre falha de implementação, falha de ambiente e ausência de evidência. |
| **Referências** | `specs/foundation/development-workflow.md` (Validação); CI em `.github/workflows/`. |

| Camada | Comando / evidência |
|--------|---------------------|
| Backend | `cd backend && mvn clean verify` |
| Frontend | lint, typecheck, unit; E2E no closure da feature |
| CI | `.github/workflows/` |

No cotidiano, logs de CI / PR são **evidência**, não SSOT de estado (`feature.yaml`).

---

## 3.5 `Review`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Review de Spec (antes de `APPROVED`) ou Review de PR (durante `IMPLEMENTING`). |
| **Quando utilizar** | Revisão da especificação; ou revisão de PR / somente review de código. |
| **Resultado esperado** | Parecer. Review de Spec pode transitar `APPROVED` ou `READY_FOR_REVIEW` → `DRAFT` (motivo na evidência). Review de PR **não** altera `feature.yaml` automaticamente e **não** substitui Validate. |
| **Referências** | `specs/foundation/review-process.md`; spec da feature; `specs/foundation/definition-of-done.md` no encerramento. |

---

## 3.6 `Status`

| Campo | Valor |
|-------|--------|
| **Finalidade** | Consultar estado atual da Feature sem alterar artefatos. |
| **Quando utilizar** | A qualquer momento; para decidir o próximo comando. |
| **Artefatos envolvidos** | `feature.yaml` (SSOT de estado); `tasks.md` (plano); git/CI (evidência); `construction-state.yaml` somente como espelho histórico. |
| **Resultado esperado** | Resumo: `status`, DoR, tasks, evidências, bloqueios. Não tratar registry/CI/`tasks.md` como SSOT de estado. |
| **Referências** | `specs/foundation/feature-yaml.md`; `specs/foundation/minimal-ssot.md`; `specs/foundation/development-workflow.md`. |

---

# 4. Regras (catálogo cotidiano)

## 4.1 Governança

| ID | Regra |
|----|-------|
| CMD-01 | Comandos **não alteram** o processo SDD, Gates, templates ou workflows. |
| CMD-02 | Comandos **apenas invocam** atividades já definidas nos documentos oficiais. |
| CMD-03 | Toda lógica permanece nos artefatos oficiais — **nunca** duplicada no prompt. |
| CMD-04 | Etapas internas de um comando **não** geram novos comandos no catálogo cotidiano. |
| CMD-05 | Consultar `path-conventions.md` e `feature.yaml` antes de inferir caminhos. Não usar `feature-manifest.yaml` para localizar artefatos em features novas. |

## 4.2 Dependências

| ID | Regra |
|----|-------|
| CMD-07 | `Readiness` avalia DoR-Spec e/ou DoR-Implementation (`definition-of-ready.md`). Não é estado. |
| CMD-17 | `Implement` exige `status: IMPLEMENTING` (DoR-Implementation atendido). |
| CMD-18 | `Review` no modo somente revisão **não altera** código nem artefatos de produto. |
| CMD-19 | Comandos da cerimônia v4.1 **não** são padrão do fluxo cotidiano nem do Claude Code. Dependência exclusiva desses mecanismos: interromper e solicitar decisão humana. |

## 4.3 Restrições

| ID | Regra |
|----|-------|
| CMD-12 | `Status` não altera artefatos. |
| CMD-20 | Não tratar `construction/registry.yaml` status, `session.md` ou `pkg-XX/status.md` como SSOT de produto ou de progresso global. |

---

# 5. Exemplos (Etapa 2)

```text
Specify FT-AREA
Readiness FT-AREA
Implement FT-AREA
Validate FT-AREA
Review FT-AREA
Status FT-AREA
```

Consulta:

```text
Status FT-AUTH
```

---

# 6. Mapa de referências (cotidiano)

| Comando | Documento principal |
|---------|---------------------|
| `Specify` | `specs/foundation/workflow.md`; `specs/foundation/feature-yaml.md` |
| `Readiness` | `specs/foundation/definition-of-ready.md` |
| `Implement` | `specs/foundation/development-workflow.md`; `path-conventions.md` |
| `Validate` | `specs/foundation/development-workflow.md` |
| `Review` | `review-process.md` (Spec vs PR); spec da feature |
| `Status` | `feature-yaml.md`; `minimal-ssot.md` |
| Identidade | `specs/foundation/feature-yaml.md` |
| Precedência | `specs/foundation/minimal-ssot.md` |

---

# 7. Evolução

Novos comandos somente quando representarem intenções **já formalizadas** e **não absorvíveis** por comando existente.

Antes de adicionar: verificar se a atividade é etapa interna de outro comando.

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.0 | 2026-07-13 | Catálogo inicial de comandos para agentes |
| 1.1 | 2026-07-13 | Consolidação minimalista — redução de redundâncias |
| 1.2 | 2026-08-19 | Alinhamento ao fluxo Etapa 2; cerimônia v4.1 como referência histórica |
| 1.3 | 2026-08-19 | Máquina de estados da Feature; DoR-Spec / DoR-Implementation; Review de Spec vs PR |
