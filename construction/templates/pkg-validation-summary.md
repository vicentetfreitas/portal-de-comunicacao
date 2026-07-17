# PKG Validation Summary — Template (VAL-01 + VAL-02)

| Item | Valor |
|------|-------|
| Regras | **VAL-01** (apresentação) · **VAL-02** (ciclo de vida) |
| Aplicável | Todo PKG (backend, frontend, foundation) |
| Relatório principal | `pkg-XX/status.md` |
| Log completo | `pkg-XX/evidence/build-verify-YYYY-MM-DD.log` |
| Processo de validação | **Inalterado** (BUILD-01) — apenas apresentação e estados |

---

## Objetivo

Padronizar a saída de validação de todos os PKGs: **um único resumo** no relatório principal; logs completos somente em `evidence/`.

VAL-02 estende VAL-01 com o estado **PENDING_REVALIDATION** para PKGs com correções concluídas que aguardam apenas a reexecução completa do pipeline.

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

## Status (VAL-01 + VAL-02)

| Valor | Quando usar |
|-------|-------------|
| `PASS` | Pipeline completo executado; todos os comandos de validação do workstream passaram |
| `BUILD_FAILURE` | Pipeline completo executado; falha **comprovada** (compilação, testes, build) |
| `ENVIRONMENT_FAILURE` | Comando **não pôde ser executado** (runtime indisponível, PATH, permissão) |
| `PENDING_REVALIDATION` | Correções aplicadas documentadas; pipeline completo **ainda não** reexecutado com sucesso após essas correções |

**Proibido:** classificar erro de compilação ou teste como `ENVIRONMENT_FAILURE`.

**Proibido:** usar `BUILD_FAILURE` quando a última execução completa do pipeline ocorreu **antes** das correções documentadas e a revalidação ainda não foi executada — nesse caso, usar `PENDING_REVALIDATION`.

---

## Determinação do status (VAL-02)

Os estados **não possuem prioridade fixa**. O Orchestrator e os agentes de execução determinam o status por **condições observáveis** durante o PKG e pelas evidências produzidas — **sem** ordem de precedência entre estados.

| Condição observável | Status |
|---------------------|--------|
| Evidência de pipeline completo com todos os `EXIT_*=0` (ou equivalente) | `PASS` |
| Evidência de pipeline completo com falha de compilação, teste ou build | `BUILD_FAILURE` |
| Comando de validação não executou por limitação de ambiente | `ENVIRONMENT_FAILURE` |
| Seção **Correções aplicadas** preenchida (correções concluídas); pipeline completo **não** reexecutado com sucesso desde essas correções | `PENDING_REVALIDATION` |

### Transições típicas

```text
NOT_RUN → (pipeline falha) → BUILD_FAILURE
BUILD_FAILURE → (correções documentadas, revalidação pendente) → PENDING_REVALIDATION
PENDING_REVALIDATION → (pipeline passa) → PASS
PENDING_REVALIDATION → (pipeline falha) → BUILD_FAILURE
(qualquer) → (ambiente indisponível) → ENVIRONMENT_FAILURE
```

Avaliar cada condição pelos **fatos** (log em `evidence/`, seção **Correções aplicadas**, seção **Revalidation**) — não por hierarquia entre rótulos de status.

---

## Validation — comandos por workstream

Listar **somente** os comandos efetivamente executados. Usar `✓` (passou) ou `✗` (falhou).

| Workstream | Comandos padrão |
|------------|-----------------|
| **frontend** / **frontend-foundation** | `yarn lint:check`, `yarn typecheck`, `yarn test`, `yarn build` |
| **backend** / **platform-foundation** | `mvn test` (ou escopo do PKG), `mvn compile -pl backend` quando aplicável |

Exemplo backend:

```markdown
Validation

✓ mvn test -pl backend -am
✓ mvn compile -pl backend
```

Quando `Status` for `PENDING_REVALIDATION`, marcar **Revalidation** como pendente (`⬜`) — não repetir falhas da execução anterior em **Validation** se essa seção reflete a última execução **antes** das correções.

---

## Correções aplicadas

- Agrupar por **causa raiz**, nunca por ocorrência individual.
- Omitir stack traces, trechos de log e listas longas de arquivos.
- Se não houve correção: `• (nenhuma)`.
- Quando preenchida com correções concluídas e revalidação ainda não executada → status `PENDING_REVALIDATION`.

Exemplo:

```markdown
Correções aplicadas

• TS2379 — props opcionais do DS incompatíveis com exactOptionalPropertyTypes
• Playwright strict mode — locators ambíguos em singular.spec.ts
```

---

## Revalidation

Repetir os **mesmos** comandos da seção Validation após correções.

| Símbolo | Significado |
|---------|-------------|
| `✓` | Passou na revalidação final |
| `✗` | Ainda falhando |
| `⬜` | Aguardando reexecução do pipeline completo (`PENDING_REVALIDATION`) |

Se não houve falha inicial, duplicar o resultado de Validation com `✓`.

Se correções foram aplicadas e o pipeline ainda não foi reexecutado:

```markdown
Revalidation

⬜ pipeline completo — aguardando reexecução após correções
```

---

## Evidence

- Path **relativo** à pasta do PKG: `evidence/build-verify-YYYY-MM-DD.log`
- O log deve conter saída completa dos comandos e, ao final:

```text
EXIT_LINT=<code>       # frontend — omitir linhas não aplicáveis no backend
EXIT_TYPECHECK=<code>
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
| VAL-01-05 | Não criar `implementation-report.md` — conteúdo em `status.md` (ART-01) |
| VAL-01-06 | Orchestrator e agentes emitem o resumo no encerramento do PKG |

## Regras de ciclo de vida (VAL-02)

| Regra | Descrição |
|-------|-----------|
| VAL-02-01 | `PENDING_REVALIDATION` representa correções concluídas aguardando **somente** revalidação |
| VAL-02-02 | `BUILD_FAILURE` representa **exclusivamente** falhas comprovadas em execução completa do pipeline |
| VAL-02-03 | `ENVIRONMENT_FAILURE` representa impossibilidade de execução por ambiente — nunca falha de build/teste |
| VAL-02-04 | Determinação de status por condições observáveis — **sem** prioridade fixa entre estados |
| VAL-02-05 | Compatível com VAL-01 — estende o conjunto de status; formato do resumo inalterado |

---

## Scripts de evidência

Templates centrais (ART-01 — **não** copiar para cada PKG):

| Workstream | Template |
|------------|----------|
| Frontend | `construction/templates/pkg-evidence-run-frontend.sh` |
| Backend | `construction/templates/pkg-evidence-run-backend.sh` |

```bash
PKG_DIR=construction/.../pkg-fe-06 FULL_VALIDATION=1 \
  bash construction/templates/pkg-evidence-run-frontend.sh
```

Modelo de artefatos: `construction/templates/pkg-artifact-model.md`
