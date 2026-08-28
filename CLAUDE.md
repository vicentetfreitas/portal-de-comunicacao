# Portal de Comunicação — orientação Claude Code

Mecanismo de execução. **Não é SSOT.** Não duplicar `docs/`, `specs/`, `database/` ou `construction/`.

## Camadas

```text
docs → specs → construction → automação (Cursor e Claude Code)
```

Não inverter. Automação consome o SSOT; não define produto.

## Precedência (comportamento)

```text
specs/ > docs/ > código
```

Detalhes: `specs/foundation/minimal-ssot.md`.

## Consultar primeiro

| Necessidade | Fonte |
|-------------|--------|
| Precedência / o que não é SSOT | `specs/foundation/minimal-ssot.md` |
| Fluxo diário | `specs/foundation/development-workflow.md` |
| Paths | `specs/foundation/path-conventions.md` |
| Invocações | `specs/foundation/agent-commands.md` |
| Organização e execução do projeto (camadas, Convenção Git, contratos Validate/Review) | `docs/governance/10-project-organization.md` |
| Etapa 2 | `docs/governance/09-framework-simplification-scope.md` |
| Arquitetura documental | `docs/governance/07-documentation-architecture.md` |
| Feature | `specs/features/<slug>/` |
| Estado geral do projeto (não de uma Feature) | `docs/governance/01-project-status.md` — ler antes de `03-open-decisions.md`/`04-decision-log.md`/`docs/audit/*` inteiros; usar `/project-status` |
| DoR / DoD | `specs/foundation/definition-of-ready.md`, `definition-of-done.md` |
| Padrões de código | `docs/implementation/` |
| Arquitetura | `docs/architecture/` |
| Regras de negócio | `docs/domain/` |
| Schema | `database/` |
| CI | `.github/workflows/` |
| Design / UI | DS em código: `frontend/src/components/` (`Ds*`) + tokens; frames existentes no Figma (plugin desligado neste projeto — reativar sob demanda: `claude plugin enable figma@claude-plugins-official --scope project`) |

## Guardrails

- Não implementar sem especificação que atenda DoR.
- Componente/página fora do layout Figma: a decisão de design (tokens, estados, comportamento visual) vai na spec da feature (`specs/features/<slug>/`) e reutiliza o DS em código (`frontend/src/components/` `Ds*`). Não há frame para apontar; não reativar o plugin Figma para isso.
- Não explorar o repositório sem consultar `path-conventions.md`.
- Não tratar `construction/registry.yaml` status, `session.md` ou `pkg-XX/status.md` como SSOT.
- Construction v4.1 (Session, PKG, Snapshot, Cache, orchestrator) **não** é o fluxo diário.
- Se a tarefa depender exclusivamente desses mecanismos históricos: identificar, não improvisar equivalência, **parar** e pedir decisão humana.
- Não carregar `.cursor/` nem o framework construction como contexto cotidiano.
- Um único agente; modos em `agent-commands.md` (Specify, Readiness, Implement, Validate, Review, Status).
- Review somente revisão: não editar código.
- Para status/governança amplos (não de uma Feature específica): ler `01-project-status.md` primeiro; não ler `03-open-decisions.md`, `04-decision-log.md` ou `docs/audit/*` por inteiro — usar `grep` por ID quando precisar de item específico.

## Fluxo cotidiano

```text
specs/features/<slug>/ → DoR → tasks.md → código → validação → review → merge local | PR externo → CI → merge
```

PR/GitHub/CI são publicação e revisão **externas**, opcionais — não bloqueiam desenvolvimento, validação ou review locais. Sem PR, o fechamento local usa o vocabulário já existente (`READY_FOR_LOCAL_MERGE`); ver `docs/governance/10-project-organization.md`.

## Validação

| Camada | Como |
|--------|------|
| Backend | `cd backend && mvn clean verify` |
| Frontend | lint, typecheck, unit; E2E no closure |
| CI | `.github/workflows/` |

## Contexto

1. Esta orientação  
2. Natureza da tarefa  
3. SSOT correspondente  
4. Feature e padrões só se necessário  
5. Executar o modo  
6. Validar  

Não varrer árvores; não gerar artefato não pedido; não copiar SSOT para `.claude/`.
