---
description: Executar tasks.md após DoR, sem redefinir produto.
argument-hint: FT-<DOMAIN> [task]
---

Modo Implement. Executar o plano da Feature autorizada. Não redefinir produto.

Pedido: $ARGUMENTS

Carregar somente:

1. `specs/features/<slug>/feature.yaml` — autoriza somente se `status: IMPLEMENTING`.
2. `specs/features/<slug>/specification.md`
3. `specs/features/<slug>/api.md` quando existir.
4. `specs/features/<slug>/tasks.md` (ou a task indicada) e artefatos diretamente relacionados à task.
5. `specs/foundation/path-conventions.md`
6. `docs/implementation/` apenas na camada tocada.

Regras:

- Sem `status: IMPLEMENTING`, não implementar (DoR-Implementation não autorizou). Sem spec suficiente: não inventar produto.
- Executar `tasks.md` respeitando a spec. Não alterar comportamento de produto fora da spec.
- Não criar Session, PKG, Snapshot ou Construction Cache.
- Não transitar `status` para `DONE`.
- Dependência exclusiva de framework v4.1: parar e solicitar decisão humana.
