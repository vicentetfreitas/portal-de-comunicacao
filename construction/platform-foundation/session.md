# Feature Session — platform-foundation

| Item | Valor |
|------|-------|
| Feature | platform-foundation |
| Sprint | 1A — Platform Foundation |
| Data da sessão | 2026-07-09 |
| Agente | construction-orchestrator |
| Manifesto | `construction/platform-foundation/feature-manifest.yaml` |
| Estado operacional | `construction/platform-foundation/construction-state.yaml` |
| Imutabilidade | **READ ONLY** após criação (SESSION-01) |

---

# Objetivo da Feature

Estabelecer a Platform Foundation — infraestrutura técnica compartilhada (Configuration, Persistence, Security, Integration, Web, Observability, Testing) reutilizável por Features futuras, especialmente FT-AUTH.

---

# Definition of Ready

| Critério | Atendido |
|----------|----------|
| `construction/03-construction-packages.md` completo | ✅ |
| Backlog PF-* por módulo | ✅ |
| Ordem de desenvolvimento definida | ✅ |
| PKG-01 dependências (Sprint 0) | ✅ |
| Decisões bloqueantes críticas | ✅ (CD-S1A-* não bloqueiam início) |

---

# PKGs (estrutura)

| PKG | Nome | Dependências |
|-----|------|--------------|
| PKG-01 | Configuration Foundation | Sprint 0 |
| PKG-02 | Persistence Foundation | PKG-01 |
| PKG-03 | Security Foundation | PKG-01, PKG-02 |
| PKG-04 | Integration Foundation | PKG-01, PKG-03 |
| PKG-05 | Web Foundation | PKG-01, PKG-03 |
| PKG-06 | Observability Foundation | PKG-04, PKG-05 |
| PKG-07 | Testing Foundation | PKG-01..06 |
| PKG-08 | Construction Audit | PKG-01..07 |

Estado operacional (PKG ativo, concluídos, pendentes): `construction-state.yaml`.

---

# Contexto Carregado

## Construction

| Artefato | Pontos-chave |
|----------|--------------|
| `03-construction-packages.md` | Escopo, boundaries, critérios por PKG |
| `04-construction-rules.md` | R-01 a R-15; STATE-01 a STATE-06 |
| `06-development-order.md` | Ordem sequencial; paralelismo PKG-04/PKG-05 |
| `platform-foundation/*/tasks.md` | Backlog PF-* por módulo |
| `11-feature-execution-workflow.md` | Workflow orientado à Feature (v3.0) |

## Engenharia (`docs/`)

| Artefato | Uso |
|----------|-----|
| `implementation/06-database-standards.md` | Oracle UNMPORTCOM, DDL (DBA), JPA |
| `implementation/02-repository-structure.md` | Pacotes backend |
| `construction/backend/*` | Guias complementares |

## Specs

| Artefato | Uso |
|----------|-----|
| `specs/foundation/` | DoR, DoD, convenções |
| `specs/features/authentication/` | Consumidor futuro (FT-AUTH) — não implementar na S1A |

---

# Boundaries e Contratos

- **Configuration Contract (R-11):** Properties não autorizam implementação de runtime
- **Architectural Boundaries:** componentes proibidos por PKG em `03-construction-packages.md` §5
- **R-10:** proibição de antecipação FT-AUTH

---

# Cache — Não Reler

Os artefatos acima não devem ser relidos durante execução de PKGs.

Consultar `construction-state.yaml` para estado operacional e `pkg-XX/status.md` para histórico detalhado do PKG ativo.
