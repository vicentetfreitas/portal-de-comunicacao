# Integration Plan — Template

| Item | Valor |
|------|-------|
| Sprint ID | {{SPRINT_ID}} |
| Nome | {{SPRINT_NAME}} |
| Status | PLANNED |
| Versão | 1.0 |
| Última atualização | {{DATE}} |

---

# Objetivo

{{OBJETIVO_DA_SPRINT}}

---

# Escopo

## Features incluídas

| Feature Code | Nome | Sprint Construction | Status |
|--------------|------|---------------------|--------|
| {{FEATURE_CODE}} | {{FEATURE_NAME}} | {{N}} | FEATURE_APPROVED |

## Fora de escopo

- Features não listadas acima
- Frontend
- Alterações de código durante esta sprint documental

---

# Pré-requisitos

| ID | Pré-requisito | Status |
|----|---------------|--------|
| ENT-01 | Features encerradas | ⬜ |
| ENT-02 | Platform Foundation aprovada | ⬜ |
| ENT-03 | Build verde | ⬜ |

---

# Fluxos Cross-Feature (XFT)

## {{FLOW_ID}} — {{FLOW_NAME}}

```text
{{STEP_1}}
    ↓
{{STEP_2}}
    ↓
{{STEP_N}}
```

| Passo | Feature | Ação | Endpoint |
|-------|---------|------|----------|
| 1 | {{CODE}} | {{ACTION}} | {{ENDPOINT}} |

---

# Fases de Validação

| Fase | Itens estimados | Responsável | Prazo |
|------|-----------------|-------------|-------|
| ENV | — | validator | — |
| INF | — | validator | — |
| API | — | validator | — |
| DB | — | validator | — |
| XFT | — | validator | — |
| FUN | — | validator | — |
| OUT | — | integration-lead | — |

---

# Riscos Conhecidos

| ID | Risco | Mitigação |
|----|-------|-----------|
| — | — | — |

---

# Referências

- Manifest: `integration-manifest.yaml`
- Checklist: `integration-checklist.md`
- Workflow: `../01-integration-sprint-workflow.md`
