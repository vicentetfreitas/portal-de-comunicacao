# Execution Plan — Template

| Item | Valor |
|------|-------|
| Feature Code | {{FEATURE_CODE}} |
| Feature Slug | {{FEATURE_SLUG}} |
| Sprint | {{SPRINT}} |
| Status | **Não iniciada** |
| SSOD | `construction/features/{{FEATURE_CODE}}/feature-manifest.yaml` |
| Construction State | `construction/features/{{FEATURE_CODE}}/construction-state.yaml` |

---

# Objetivo

{{OBJECTIVE}}

Consultar `feature-manifest.yaml` (SSOD) antes de qualquer outro artefato.

---

# Escopo

## Inclui

{{SCOPE_INCLUDES}}

## Não inclui

{{SCOPE_EXCLUDES}}

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| Platform Foundation | `construction/platform-foundation/construction-state.yaml` | Verificar `phase: closed` |

---

# Sequência de PKGs

| PKG | Nome | Escopo resumido |
|-----|------|-----------------|
| PKG-01 | | |

---

# Critérios de entrada (Definition of Ready)

1. Platform Foundation encerrada
2. Especificação Approved (path em `feature-manifest.yaml` → `specification.path`)
3. `Execute Feature {{FEATURE_CODE}}` executado

---

# Critérios de saída (Definition of Done)

1. Todos os PKGs concluídos
2. `construction-state.yaml` com `phase: closed`
3. Review, Audit e Readiness aprovados
4. `mvn clean verify` — SUCCESS

---

# Status

| Métrica | Valor |
|---------|-------|
| Fase | `not_started` |
| Próxima ação | `Execute Feature {{FEATURE_CODE}}` |
