---
description: Produzir ou evoluir a specification da feature (não implementar código).
argument-hint: FT-<DOMAIN>
---

Modo Specify. Produzir ou atualizar a especificação da Feature. Não implementar código.

Feature: $ARGUMENTS

Carregar somente:

1. `specs/features/<slug>/feature.yaml` — identidade e `status`.
2. `specs/foundation/feature-yaml.md` — contrato da Feature.
3. `specs/foundation/path-conventions.md` — artefatos e paths.
4. Artefatos existentes em `specs/features/<slug>/`.

Consultar `docs/domain/` e `docs/architecture/` apenas se a spec exigir. Não duplicar regras já em `docs/domain/09-business-rules.md`.

Regras:

- Trabalhar só em `specs/features/<slug>/`.
- `status` persistente: `DRAFT` | `READY_FOR_REVIEW` | `APPROVED` | `IMPLEMENTING` | `DONE`. Não gravar `REWORK` nem `status.specification`.
- Specify elabora a spec (tipicamente em `DRAFT`). Não transitar para `APPROVED`, `IMPLEMENTING` ou `DONE`.
- Não implementar código. Não alterar `backend/`, `frontend/` nem `database/`.
- Se a tarefa exigir exclusivamente Session/PKG/Snapshot/orchestrator v4.1: parar e solicitar decisão humana.
