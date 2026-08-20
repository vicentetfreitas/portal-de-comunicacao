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
| Etapa 2 | `docs/governance/09-framework-simplification-scope.md` |
| Arquitetura documental | `docs/governance/07-documentation-architecture.md` |
| Feature | `specs/features/<slug>/` |
| DoR / DoD | `specs/foundation/definition-of-ready.md`, `definition-of-done.md` |
| Padrões de código | `docs/implementation/` |
| Arquitetura | `docs/architecture/` |
| Regras de negócio | `docs/domain/` |
| Schema | `database/` |
| CI | `.github/workflows/` |

## Guardrails

- Não implementar sem especificação que atenda DoR.
- Não explorar o repositório sem consultar `path-conventions.md`.
- Não tratar `construction/registry.yaml` status, `session.md` ou `pkg-XX/status.md` como SSOT.
- Construction v4.1 (Session, PKG, Snapshot, Cache, orchestrator) **não** é o fluxo diário.
- Se a tarefa depender exclusivamente desses mecanismos históricos: identificar, não improvisar equivalência, **parar** e pedir decisão humana.
- Não carregar `.cursor/` nem o framework construction como contexto cotidiano.
- Um único agente; modos em `agent-commands.md` (Specify, Readiness, Implement, Validate, Review, Status).
- Review somente revisão: não editar código.

## Fluxo cotidiano

```text
specs/features/<slug>/ → DoR → tasks.md → código → validação → CI → PR
```

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
