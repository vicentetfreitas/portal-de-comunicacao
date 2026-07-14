# Feature Closure Report — {{FEATURE_NAME}}

| Item | Valor |
|------|-------|
| Feature Code | {{FEATURE_CODE}} |
| Feature Slug | {{FEATURE_SLUG}} |
| Sprint | {{SPRINT}} |
| Data encerramento | {{DATE}} |
| Estado final | FEATURE_APPROVED / BLOCKED |
| SSOD | `construction/features/{{FEATURE_CODE}}/feature-manifest.yaml` |

---

# Fluxo de Encerramento

```text
Closure → Review → Audit → Readiness
```

Documentos consolidados nesta fase (não durante PKGs):

- `construction/09-progress.md`
- `review.md` (módulos)
- traceability, changelog, métricas
- `closure-report.md` (este documento)

---

# PKGs Executados

| PKG | Estado final | Tarefas | Testes locais |
|-----|--------------|---------|---------------|
| PKG-01 | DONE | / | ✅ |
| PKG-02 | DONE | / | ✅ |

---

# Tarefas Concluídas

| ID | PKG | Descrição | Status |
|----|-----|-----------|--------|
| | | | |

---

# Arquivos Alterados

```text
(lista consolidada por camada)
```

---

# Validações Completas (BUILD-01)

| Validação | Comando / método | Resultado |
|-----------|------------------|-----------|
| Build completo | `mvn clean verify` | |
| Testes unitários | | |
| Testes integração | | |
| Cobertura | | |
| SDD / DoD | | |

> Build completo (`mvn clean verify`) executado **exclusivamente** nesta fase.

---

# Auditorias

## Review (`reviewer`)

| Item | Resultado |
|------|-----------|
| Boundary compliance | |
| Configuration contract | |
| Qualidade de código | |
| Parecer | Aprovado / Reprovado |

## Audit (`auditor`)

| Item | Resultado |
|------|-----------|
| Conformidade specs | |
| Conformidade docs | |
| Rastreabilidade | |
| Parecer | Conforme / Não conforme |

---

# Readiness

| Checklist | Referência | Resultado |
|-----------|------------|-----------|
| Feature Readiness | | |
| RC-* / RR-* | | |

---

# Documentação Atualizada

| Documento | Ação |
|-----------|------|
| `construction/09-progress.md` | |
| `review.md` (módulos) | |
| traceability | |
| changelog | |
| métricas | |

---

# Session

| Item | Valor |
|------|-------|
| Session utilizada | `construction/features/{{FEATURE_CODE}}/session.md` |
| Invalidada | Não — reutilizada durante execução |
| Próxima execução | Recriar Session somente se evento CACHE-02 |

---

# Pendências

| Item | Severidade | Responsável |
|------|------------|-------------|
| | | |

---

# Handoff

Próxima Feature ou consumidor:

```text
(descrever)
```
