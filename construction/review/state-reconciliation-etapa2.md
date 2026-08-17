# State Reconciliation — Etapa 2 (Retrospectivo)

| Campo | Valor |
|--------|--------|
| Artefato | state-reconciliation-etapa2.md |
| Camada | Construction / Review |
| Versão | 1.0 |
| Data | 2026-08-13 |
| Categoria documental | Evidence |

## Objetivo

Retrospectivo do estado encontrado na reconciliação Etapa 3. Documenta o que a Etapa 2 alterou vs o que ficou pendente.

**Nota:** Este artefato foi produzido na Etapa 3 porque não existia no working tree após a Etapa 2.

---

## Alterações confirmadas (Etapa 2)

| Artefato | Alteração |
|----------|-----------|
| `.cursor/rules/core/project-index.mdc` | Simplificado; always-on; aponta minimal-ssot |
| `construction/README.md` | Secção fluxo simplificado no topo |
| `construction/registry.yaml` | `discovery.status_policy` aponta minimal-ssot |
| `specs/README.md` | Lista foundation Etapa 2 (parcial) |
| `specs/features/primeiro-acesso/tasks.md` | Declarado SSOT de plano |
| `construction/features/FT-PRIMEIRO-ACESSO/construction-state.yaml` | Nota reconciliação Etapa 2 |
| `construction/frontend/features/FT-PRIMEIRO-ACESSO/construction-state.yaml` | Nota reconciliação Etapa 2 |
| Agents/orchestrators | Marcados ARCHIVED com stubs |

---

## Pendências encontradas (Etapa 2 incompleta)

| Artefato esperado | Status na reconciliação Etapa 3 |
|-------------------|----------------------------------|
| `specs/foundation/minimal-ssot.md` | **Criado na Etapa 3** |
| `specs/foundation/path-conventions.md` | **Criado na Etapa 3** |
| `specs/foundation/development-workflow.md` | **Criado na Etapa 3** |
| `docs/governance/09-framework-simplification-scope.md` | **Criado na Etapa 3** |
| `construction/review/state-reconciliation-etapa2.md` | **Este documento** |

---

## Estado de Features na reconciliação

| Feature | Registry | Spec | Observação |
|---------|----------|------|------------|
| FT-SESSION | closed | APPROVED | Coerente; feature.yaml formato legacy |
| FT-PRIMEIRO-ACESSO | execution | READY_FOR_REVIEW | FE WIP adiantado; manifests desalinhados |
| FT-COLABORADOR | execution (FE) | — | pkg-fe-02 state vs só pkg-fe-01 folder |

---

## Conclusão Etapa 2

A Etapa 2 **simplificou referências e regras Cursor** mas **não completou os artefatos foundation SSOT**. A Etapa 3 completa essa lacuna e formaliza precedência.

Ver [`ssot-reconciliation-etapa3.md`](ssot-reconciliation-etapa3.md) para reconciliação completa.
