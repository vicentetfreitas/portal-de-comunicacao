# PKG Validation Summary — Template (VAL-01)

| Item | Valor |
|------|-------|
| Regra | VAL-01 |
| Aplicável | Todo PKG (backend, frontend, foundation) |
| Relatório principal | `pkg-XX/status.md` |
| Log completo | `pkg-XX/evidence/build-verify-YYYY-MM-DD.log` |
| Processo de validação | **Inalterado** (BUILD-01) — apenas apresentação |

---

## Objetivo

Padronizar a saída de validação de todos os PKGs: **um único resumo** no relatório principal; logs completos somente em `evidence/`.

---

## Formato obrigatório

Copiar o bloco abaixo em `pkg-XX/status.md` ao concluir o PKG (substituir placeholders).

```markdown
## VALIDATION SUMMARY

Status
PASS

Validation

✓ yarn typecheck
✓ yarn test
✓ yarn build

Correções aplicadas

• (nenhuma)

Revalidation

✓ typecheck
✓ test
✓ build

Evidence

evidence/build-verify-YYYY-MM-DD.log
```

---

## Status

| Valor | Quando usar |
|-------|-------------|
| `PASS` | Todos os comandos de validação do workstream passaram |
| `BUILD_FAILURE` | Comando executou e falhou (compilação, testes, build) |
| `ENVIRONMENT_FAILURE` | Comando **não pôde ser executado** (runtime indisponível, PATH, permissão) |

**Proibido:** classificar erro de compilação ou teste como `ENVIRONMENT_FAILURE`.

---

## Validation — comandos por workstream

Listar **somente** os comandos efetivamente executados. Usar `✓` (passou) ou `✗` (falhou).

| Workstream | Comandos padrão |
|------------|-----------------|
| **frontend** / **frontend-foundation** | `yarn typecheck`, `yarn test`, `yarn build` |
| **backend** / **platform-foundation** | `mvn test` (ou escopo do PKG), `mvn compile -pl backend` quando aplicável |

Exemplo backend:

```markdown
Validation

✓ mvn test -pl backend -am
✓ mvn compile -pl backend
```

---

## Correções aplicadas

- Agrupar por **causa raiz**, nunca por ocorrência individual.
- Omitir stack traces, trechos de log e listas longas de arquivos.
- Se não houve correção: `• (nenhuma)`.

Exemplo:

```markdown
Correções aplicadas

• TS2379 — props opcionais do DS incompatíveis com exactOptionalPropertyTypes
• TS2345 — rotas programáticas fora do RouteNamedMap file-based
• Vitest — mock HTTP com hoisting inválido; q-dialog teleportado fora do wrapper
```

---

## Revalidation

Repetir os **mesmos** comandos da seção Validation após correções.

- `✓` — passou na revalidação final
- `✗` — ainda falhando (PKG permanece `BLOCKED` ou `IN_PROGRESS`)

Se não houve falha inicial, duplicar o resultado de Validation.

---

## Evidence

- Path **relativo** à pasta do PKG: `evidence/build-verify-YYYY-MM-DD.log`
- O log deve conter saída completa dos comandos e, ao final:

```text
EXIT_TYPECHECK=<code>    # frontend — omitir linhas não aplicáveis no backend
EXIT_TEST=<code>
EXIT_BUILD=<code>
```

Para backend, usar linhas equivalentes, por exemplo:

```text
EXIT_MVN_TEST=<code>
EXIT_MVN_COMPILE=<code>
```

---

## Regras de apresentação (VAL-01)

| Regra | Descrição |
|-------|-----------|
| VAL-01-01 | `VALIDATION SUMMARY` é a **única** seção de validação no relatório principal |
| VAL-01-02 | Não incluir tabelas extensas de exit code no `status.md` |
| VAL-01-03 | Não incluir stack traces no resumo |
| VAL-01-04 | Logs completos **somente** em `evidence/*.log` |
| VAL-01-05 | `implementation-report.md` (se existir) **não** duplica validação — referencia `status.md` |
| VAL-01-06 | Orchestrator e agentes emitem o resumo no encerramento do PKG |

---

## Script de evidência (frontend)

Copiar para `pkg-XX/evidence/run-bv.sh` e ajustar `FRONTEND` se necessário.

Ver: `construction/templates/pkg-evidence-run-frontend.sh`

---

## Script de evidência (backend)

Copiar para `pkg-XX/evidence/run-bv.sh` e ajustar módulo Maven.

Ver: `construction/templates/pkg-evidence-run-backend.sh`
