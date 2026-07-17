# Features — Construction Framework v4.1

| Item | Valor |
|------|-------|
| Camada | Construction |
| Escopo | Workstream **backend** das Features de negócio |
| Versão | 4.1 |
| Registry | `construction/registry.yaml` (unificado) |
| Golden Template | FT-SINGULAR — `construction/golden-template/FT-SINGULAR.md` |
| Última atualização | 2026-07-16 |

---

# Objetivo

Centralizar o ciclo de construção do **Workstream backend** de Features de negócio. Na v4.1, cada Feature (`FT-<DOMAIN>`) pode ter múltiplos Workstreams e **Capabilities** opcionais — este diretório é o Workstream `backend`. Descoberta via **SSOT** (`feature-manifest.yaml` na raiz da Feature).

> **v4.1:** consultar `construction/12-fullstack-orchestrator.md` e `construction/registry.yaml` antes deste README.

---

# Feature Identity

Cada Feature possui um **identificador oficial estável** usado como chave em todo o Construction Framework.

## Convenção oficial

| Aspecto | Padrão | Exemplo |
|---------|--------|---------|
| Formato | `FT-<DOMAIN>` | `FT-AUTH`, `FT-DOCUMENTS`, `FT-NEWS` |
| Caixa | **MAIÚSCULAS** | `FT-AUTH` (não `ft-auth`) |
| Separador | Hífen | `FT-NOTIFICATIONS` |
| Diretório | `construction/features/<code>/` | `construction/features/FT-AUTH/` |
| Comando | `Execute Feature <code>` | `Execute Feature FT-AUTH` |
| Slug | Domínio em minúsculas (camada `specs/`) | `authentication` → `specs/features/authentication/` |

O **code** é a chave oficial. O **slug** referencia a camada de especificação e permanece em `specs/features/<slug>/`.

---

# Feature Manifest como SSOD

O `feature-manifest.yaml` é o **Single Source of Discovery (SSOD)** da Feature.

## Regra SSOD-01

Todo agente ou automação deve **iniciar** consultando o manifesto. É proibido usar paths hardcoded para localizar artefatos da Feature.

## Fluxo de descoberta

```text
registry.yaml (opcional) → feature-manifest.yaml (SSOD) → artefatos descobertos
```

A partir do manifesto são descobertos:

- Specification (`specification.path`)
- Construction State (`artifacts.state`)
- Execution Plan (`artifacts.execution_plan`)
- Session, Review, Reports, PKGs
- Implementação (`implementation.backend`, `implementation.frontend`)
- Dependências e métricas

---

# Modelo de dois níveis

| Nível | Localização | Papel |
|-------|-------------|--------|
| **Platform Foundation** | `construction/platform-foundation/` | Infraestrutura — **encerrada** (`phase: closed`) |
| **Features de negócio** | `construction/features/<FEATURE_CODE>/` | Ciclo independente por Feature |

---

# Estrutura padrão por Feature

```text
construction/features/<FEATURE_CODE>/
├── feature-manifest.yaml      ← SSOD (consultar primeiro)
├── construction-state.yaml    ← SSOT estado operacional
├── execution-plan.md          ← Ponto de entrada da execução
├── session.md                 ← Snapshot (Execute Feature)
├── pkg-01/ … pkg-N/
│   ├── status.md              ← VALIDATION SUMMARY (VAL-01) + entregas
│   └── evidence/              ← build-verify-*.log (opcional, ART-01)
├── review/
├── reports/
└── closure-report.md
```

Índice global: `construction/features/registry.yaml`

---

# Features registradas

| Code | Slug | Nome | Sprint | Estado |
|------|------|------|--------|--------|
| `FT-AUTH` | authentication | Authentication | 1 | `not_started` |

---

# Como adicionar nova Feature

1. Definir **code** conforme convenção `FT-<DOMAIN>`
2. Registrar em `construction/features/registry.yaml`
3. Criar `construction/features/<FEATURE_CODE>/` a partir dos templates
4. Preencher `feature-manifest.yaml` (SSOD) com todos os paths
5. Preencher `construction-state.yaml` e `execution-plan.md`
6. Executar `Execute Feature <FEATURE_CODE>`

---

# Referências

- `construction/11-feature-execution-workflow.md` — Workflow v3.2
- `construction/templates/feature-manifest.yaml` — Template SSOD
- `construction/platform-foundation/` — Fundação (histórico)
