---
description: Executar validações da camada alterada e distinguir tipo de falha.
argument-hint: [backend|frontend|feature]
---

Modo Validate. Validar a implementação e produzir evidência. Não altera o estado da Feature.

Escopo: $ARGUMENTS

Carregar somente:

1. `specs/features/<slug>/feature.yaml` — ler; não gravar `status`.
2. `specs/features/<slug>/specification.md`
3. `specs/features/<slug>/acceptance-tests.md` quando existir.
4. Testes e artefatos da camada alterada.

Executar conforme a camada alterada:

- Backend: `cd backend && mvn clean verify`
- Frontend: lint, typecheck e unit; E2E no closure da feature
- CI: `.github/workflows/`

Evidência: saída dos comandos e/ou CI. Distinguir falha de implementação, falha de ambiente e ausência de evidência.

Regras:

- Validate não altera `feature.yaml`. Não define `DRAFT`, `READY_FOR_REVIEW`, `APPROVED`, `IMPLEMENTING` nem `DONE`.
- Git, CI, testes e logs são evidência, não SSOT de estado.
- Não usar `pkg-XX/status.md` como SSOT de progresso.
