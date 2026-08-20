---
description: Revisar aderência à spec sem editar código (somente review).
argument-hint: FT-<DOMAIN> ou descrição do diff
---

Modo Review. Somente parecer. Review não altera código nem artefatos da Feature. Review de Spec pode atualizar exclusivamente o campo `status` de `feature.yaml` para efetivar uma transição de estado válida.

Pedido: $ARGUMENTS

Carregar em ambos os contextos:

1. `specs/features/<slug>/feature.yaml`
2. `specs/features/<slug>/specification.md`
3. `specs/foundation/review-process.md`

Há dois contextos distintos — não misturar.

## Review de Spec

Quando: `status: READY_FOR_REVIEW`.

Carregar também: artefatos obrigatórios da Feature. Confirmar DoR-Spec/Gate 1 em `specs/foundation/definition-of-ready.md` somente se o parecer exigir.

Efeito em `status` — atualizar somente o campo `status` em `specs/features/<slug>/feature.yaml`:

- PASS: `READY_FOR_REVIEW` → `APPROVED` (`status: APPROVED`).
- FAIL / devolução: `READY_FOR_REVIEW` → `DRAFT` (`status: DRAFT`). Motivo na evidência da revisão.

Não persistir `REWORK`. Não usar `status.specification`. Gate 1 não atribui `APPROVED`.

## Review de PR

Quando: durante `IMPLEMENTING`.

Carregar também: diff; testes e evidências; critérios de aceite relevantes (`acceptance-tests.md` quando existir). Consultar `docs/architecture/` e `docs/implementation/` só se o parecer exigir.

Efeito: não altera `feature.yaml` automaticamente; não substitui Validate. Gate 3 é a verificação formal. `DONE` exige também DoD e Gate 6.

Regras:

- Precedência: `specs/` > `docs/` > código.
- Review não altera código nem artefatos da Feature. Review de Spec pode atualizar exclusivamente o campo `status` de `feature.yaml`.
- Não carregar `.cursor/` nem o framework v4.1 como base da revisão cotidiana.
- Dependência exclusiva de Gate/Close v4.1 para concluir: parar e solicitar decisão humana.
