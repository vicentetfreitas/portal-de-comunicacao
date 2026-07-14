# Feature Session — {{FEATURE_NAME}}

| Item | Valor |
|------|-------|
| Feature Code | {{FEATURE_CODE}} |
| Feature Slug | {{FEATURE_SLUG}} |
| Sprint | {{SPRINT}} |
| Data da sessão | {{DATE}} |
| Agente | {{AGENT}} |
| SSOD | `construction/features/{{FEATURE_CODE}}/feature-manifest.yaml` |
| Estado operacional | `construction/features/{{FEATURE_CODE}}/construction-state.yaml` |
| Imutabilidade | **READ ONLY** após criação (SESSION-01) |

---

# Regra SESSION-01 / STATE-04

Esta Session é **imutável** durante toda a execução da Feature.

- Somente `Execute Feature` pode criar ou recriar esta Session.
- Nenhum PKG pode modificar este documento.
- A Session representa **conhecimento carregado (Snapshot)**, não progresso.

Progresso operacional: `construction/features/{{FEATURE_CODE}}/construction-state.yaml` (SSOT).  
Histórico detalhado por PKG: `construction/features/{{FEATURE_CODE}}/pkg-XX/status.md`.

---

# Snapshot de Contexto

Visão condensada da Feature. O agente deve consultar **preferencialmente** este Snapshot antes de abrir qualquer documento.

## Feature

| Campo | Valor |
|-------|-------|
| Code | {{FEATURE_CODE}} |
| Slug | {{FEATURE_SLUG}} |
| Tipo | {{FEATURE_TYPE}} |
| Objetivo | {{OBJECTIVE}} |

## Objetivos

{{SNAPSHOT_OBJECTIVES}}

## Premissas

{{SNAPSHOT_PREMISES}}

## Restrições

{{SNAPSHOT_CONSTRAINTS}}

## Contratos

{{SNAPSHOT_CONTRACTS}}

## Dependências

{{SNAPSHOT_DEPENDENCIES}}

## Decisões

{{SNAPSHOT_DECISIONS}}

## PKGs

Resumo estrutural dos PKGs (estado operacional em `construction-state.yaml`):

| PKG | Nome | Dependências |
|-----|------|--------------|
| PKG-01 | | |
| PKG-02 | | |

## Artefatos

Resumo dos artefatos carregados via Manifest (não reler — ver CACHE-01):

| Camada | Artefato | Pontos-chave |
|--------|----------|--------------|
| Especificação | | |
| Construction | | |
| Engenharia | | |

## Riscos

{{SNAPSHOT_RISKS}}

## Pendências

{{SNAPSHOT_PENDING}}

---

# Definition of Ready

| Critério | Atendido |
|----------|----------|
| Manifesto presente (`feature-manifest.yaml`) | ⬜ |
| specification completa | ⬜ |
| tasks com backlog | ⬜ |
| acceptance-tests definidos | ⬜ |
| dependências conhecidas | ⬜ |
| decisões bloqueantes resolvidas | ⬜ |

---

# Validação de Consistência

| Verificação | Resultado |
|-------------|-----------|
| Manifesto válido e completo | ⬜ |
| Sem conflito specs vs docs | ⬜ |
| Ordem de PKGs válida | ⬜ |
| DoR atendida | ⬜ |

---

# Cache de Contexto

Hierarquia obrigatória durante PKGs:

```text
Documento → Snapshot → Cache
```

| Regra | Descrição |
|-------|-----------|
| CACHE-01 | Nenhum documento relido se informação já está no Snapshot |
| CACHE-02 | Reutilizar Session ativa salvo evento de invalidação |
| RULE-CONTEXT-01 | Consultar Snapshot/Cache antes de abrir documento adicional |

### Eventos de invalidação (recriar Session)

- alteração da specification, api, use cases, domain, decisions
- alteração de ADR relacionada
- inclusão ou remoção de PKG
- alteração do Manifest

---

# Próximo Passo

Consultar `construction/features/{{FEATURE_CODE}}/construction-state.yaml` para o PKG ativo e próximas ações.
