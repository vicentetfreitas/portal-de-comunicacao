# PKG Status — {{PKG_CODE}}

| Item | Valor |
|------|-------|
| Feature Code | {{FEATURE_CODE}} |
| PKG | {{PKG_CODE}} — {{PKG_NAME}} |
| Prefixo tarefas | {{TASK_PREFIX}} |
| Responsável | construction-engineer |
| Estado | NOT_STARTED |
| Início | — |
| Conclusão | — |

---

# Regras de Escrita (PARALLEL-01 / STATE-05)

Artefatos que o PKG pode atualizar:

| Artefato | Responsabilidade |
|----------|------------------|
| `construction-state.yaml` | Estado operacional global (SSOT) |
| `pkg-XX/status.md` (este arquivo) | Histórico detalhado do PKG |

| Proibido durante PKG |
|---------------------|
| `session.md` |
| `review.md`, `progress`, `traceability` |
| `closure-report.md` |

Consultar estado via `construction-state.yaml` (STATE-02).  
Consultar contexto via `session.md` (Snapshot) — **não modificar** (SESSION-01).

---

# Contexto

Antes de abrir documentos, consultar (SSOD-01):

1. Manifesto SSOD: `construction/features/{{FEATURE_CODE}}/feature-manifest.yaml`
2. Construction State: `artifacts.state` no manifesto (STATE-02)
3. Snapshot: `artifacts.session` no manifesto — **não modificar** (SESSION-01)
4. Cache de contexto interno

Exploração da árvore da Feature é **proibida** (RULE-CONTEXT-01).

---

# Tarefas

| ID | Descrição | Estado |
|----|-----------|--------|
| | | ⬜ |

---

# Implementação

## Arquivos criados/alterados

```text
(listar paths)
```

## Validações locais executadas (BUILD-01)

> **VAL-01:** ao concluir o PKG, preencher a seção **VALIDATION SUMMARY** abaixo.  
> Template e regras: `construction/templates/pkg-validation-summary.md`  
> Log completo: `evidence/build-verify-YYYY-MM-DD.log` (scripts em `construction/templates/pkg-evidence-run-*.sh`)

**Proibido:** `mvn clean verify` (reservado ao Encerramento da Feature).

### VALIDATION SUMMARY

Status
NOT_RUN

Validation

✗ (pendente)

Correções aplicadas

• (nenhuma)

Revalidation

✗ (pendente)

Evidence

evidence/build-verify-YYYY-MM-DD.log

---

# Critérios locais

| Critério | Atendido |
|----------|----------|
| Código implementado conforme escopo | ⬜ |
| Testes do módulo passando | ⬜ |
| Sem violação de boundary | ⬜ |
| Sem violação de configuration contract | ⬜ |

---

# Bloqueios

| Motivo | Desde | Ação |
|--------|-------|------|
| — | — | — |

---

# Resumo operacional

(3–8 linhas — o que foi feito, pendências locais. **Não** duplicar VALIDATION SUMMARY.)

---

# Notas

- Não atualizar documentação global neste PKG
- Review, audit, build completo → Encerramento da Feature
- Leitura adicional de documento só se ausente no Snapshot (registrar motivo abaixo)

### Leitura excepcional (se aplicável)

| Documento | Motivo | Data |
|-----------|--------|------|
| — | — | — |
