---
description: Validar Definition of Ready; não implementar se houver bloqueio.
argument-hint: FT-<DOMAIN>
---

Modo Readiness. Avaliar se a Feature está pronta para a próxima transição. Readiness não é estado.

Feature: $ARGUMENTS

Carregar somente:

1. `specs/features/<slug>/feature.yaml` — SSOT de estado.
2. `specs/foundation/definition-of-ready.md` — critérios DoR-Spec e DoR-Implementation.
3. Artefatos em `specs/features/<slug>/` (incluir `tasks.md` no DoR-Implementation).

Distinguir o contexto pela transição pedida:

- **DoR-Spec:** `DRAFT` → `READY_FOR_REVIEW`. Gate 1 obrigatório. `tasks.md` completo não é exigência.
- **DoR-Implementation:** ocorre com `status: APPROVED`. Exige DoR-Spec ainda válido, dependências, impactos e tarefas identificadas.
  - PASS: atualizar somente `status` em `specs/features/<slug>/feature.yaml` para `IMPLEMENTING` (`APPROVED` → `IMPLEMENTING`).
  - FAIL: não alterar `status`; permanece `APPROVED`.

Gates 2, 4 e 5 são condicionais e não substituem o DoR. Gates 3 e 6 não pertencem a este comando.

Resultado: prontidão confirmada ou lista de bloqueadores.

Regras:

- Não implementar código enquanto o DoR aplicável não estiver atendido.
- DoR-Implementation altera somente o campo `status` de `feature.yaml`. Não alterar outros campos. Não gravar `REWORK` nem `status.specification`.
- Sem DoR-Spec: não ir a `READY_FOR_REVIEW`.
- Não tratar `construction/registry.yaml`, Git, CI, testes ou `tasks.md` como SSOT de estado.
- Se a prontidão existir apenas no modelo Session/PKG v4.1: parar e solicitar decisão humana.
