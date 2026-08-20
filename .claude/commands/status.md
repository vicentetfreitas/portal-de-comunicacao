---
description: Consultar estado da feature sem alterar artefatos.
argument-hint: FT-<DOMAIN>
---

Modo Status. Informar estado e progresso. Não alterar artefatos.

Feature: $ARGUMENTS

Fonte principal de estado: `specs/features/<slug>/feature.yaml` (`status`).

Pode consultar:

- `tasks.md` — plano/progresso, não estado
- evidências (Git, CI, testes, logs) — evidência, não estado
- `construction-state.yaml` só como espelho histórico, se existir

Resumir: `status` em `feature.yaml`, DoR aparente, progresso de tasks, evidências, bloqueios.

Regras:

- O estado NUNCA é inferido de Git, CI, testes, logs ou `tasks.md`.
- Valores persistentes: `DRAFT` | `READY_FOR_REVIEW` | `APPROVED` | `IMPLEMENTING` | `DONE`. `REWORK` não é estado.
- `construction/registry.yaml` não é SSOT de produto nem de progresso.
- Não alterar artefatos.
