# Engineering Framework — Sprint de Integração

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Framework | **Engineering Framework — Integration Layer** |
| Camada | `engineering/` |
| Tipo | Governança de Integração Backend |
| Status | **Stable** |
| Versão | **1.0.0** |
| Última atualização | 2026-07-14 |
| Changelog | `engineering/CHANGELOG.md` |

---

# Objetivo

Formalizar a **Sprint de Integração** como etapa oficial do ciclo de engenharia do projeto — reutilizável por qualquer conjunto de Features construídas com o Construction Framework.

A Sprint de Integração **não é uma Feature**. É uma etapa de **Engenharia** que valida o backend integrado após o encerramento de um grupo de Features (`FEATURE_APPROVED`).

---

# Relação com outras camadas

```text
specs/          → contratos e regras de negócio (fonte da verdade funcional)
    ↓
construction/   → construção por Feature (PKGs, review, closure)
    ↓
engineering/    → validação integrada do backend construído
    ↓
backend/        → implementação (não alterada nesta fase documental)
```

| Camada | Papel |
|--------|-------|
| `construction/` | Construir Features isoladamente com PKGs |
| `engineering/integration/` | Validar o sistema integrado entre Features |
| `docs/` | Padrões técnicos consultivos |

---

# Estrutura

```text
engineering/
├── README.md
├── CHANGELOG.md
├── templates/                          ← Templates reutilizáveis
│   ├── integration-manifest.yaml
│   ├── integration-state.yaml
│   ├── integration-plan.md
│   ├── integration-checklist.md
│   ├── integration-report.md
│   ├── integration-readiness.md
│   └── issues.md
└── integration/
    ├── README.md
    ├── 01-integration-sprint-workflow.md   ← SSOT
    ├── 02-integration-rules.md
    ├── 03-phases-and-activities.md
    ├── 04-state-machine.md
    ├── 05-entry-exit-criteria.md
    ├── registry.yaml
    └── sprints/<SPRINT_ID>/              ← Instância por sprint
        ├── integration-manifest.yaml       ← SSOD
        ├── integration-state.yaml          ← SSOT
        ├── integration-plan.md
        ├── integration-checklist.md
        ├── integration-report.md
        ├── integration-readiness.md
        └── issues.md
```

---

# Princípios

| Princípio | Descrição |
|-----------|-----------|
| **Manual primeiro** | Processo consolidado manualmente antes de qualquer automação |
| **Reutilizável** | Templates e workflow aplicáveis a qualquer sprint de integração |
| **Rastreável** | Checklist com ID, critério, evidência e status |
| **Independente de Feature** | Não altera specs, APIs nem código durante a documentação |
| **Evidência obrigatória** | Nenhum item aprovado sem evidência registrada |

---

# SSOT e SSOD

| Artefato | Papel |
|----------|-------|
| `01-integration-sprint-workflow.md` | **SSOT** — workflow oficial da Sprint de Integração |
| `integration-manifest.yaml` | **SSOD** — descoberta de paths e escopo (SSOD-INT-01) |
| `integration-state.yaml` | **SSOT operacional** — fase, progresso, métricas (STATE-INT-01) |

---

# Início rápido

1. Consultar `engineering/integration/01-integration-sprint-workflow.md`
2. Verificar critérios de entrada em `05-entry-exit-criteria.md`
3. Criar instância em `integration/sprints/<SPRINT_ID>/` a partir dos templates
4. Preencher `integration-plan.md` com escopo e fluxos cross-feature
5. Executar `integration-checklist.md` manualmente
6. Registrar issues em `issues.md`
7. Produzir `integration-report.md` e `integration-readiness.md`
8. Transicionar estados conforme `04-state-machine.md`

---

# Referências

- Construction Framework: `construction/11-feature-execution-workflow.md`
- Processo de engenharia: `.cursor/rules/process/process-lifecycle.mdc`
- Progresso do projeto: `construction/09-progress.md`
