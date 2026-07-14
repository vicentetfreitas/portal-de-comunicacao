# Sprint Retrospective — Sprint 1

| Item | Valor |
|------|-------|
| Sprint | 1 (Sprint 1A — Platform Foundation + FT-AUTH) |
| Período | 2026-07-08 a 2026-07-09 |
| Status final | PF **APROVADA** · FT-AUTH **FEATURE_APPROVED** (Sprint 1 Backend) |
| Testes finais | 158 (PF) → 188 (FT-AUTH) |
| Fontes | Artefatos em `construction/platform-foundation/`, `construction/features/FT-AUTH/`, `construction/review/`, `construction/09-progress.md` |

---

## 1. O que funcionou?

- **Sequência PF → Feature.** A Platform Foundation (8 PKGs, 37 tarefas) foi concluída e auditada antes do handoff para FT-AUTH, permitindo reutilizar `AbstractIntegrationTest`, `RestClient`, `SecurityFilterChain` e properties sem retrabalho estrutural.
- **Decomposição em PKGs.** Tanto PF (PKG-01..08) quanto FT-AUTH (PKG-01..06) permitiram progresso incremental com `pkg-XX/status.md` e validações locais por pacote.
- **Workflow orientado à Feature (v3.x).** `feature-manifest.yaml` como SSOD, `session.md` imutável como Snapshot e `construction-state.yaml` como SSOT de progresso reduziram ambiguidade sobre o que estava em execução.
- **Cobertura de aceite automatizada.** FT-AUTH encerrou com **14/14** cenários AC-AUTH em `AuthAcceptanceIntegrationTest` e `mvn clean verify` com **0 falhas**.
- **Delimitação de escopo na spec v2.2.** Explicitar Sprint 1 como **backend-only** evitou bloqueio indevido por frontend (FE-001..011) durante Review e Readiness.
- **PF sem conflitos.** Auditoria Sprint 1A (`construction/review/construction-audit.md`) não registrou conflitos documentação ↔ implementação.

---

## 2. O que não funcionou?

- **Evolução da spec durante a execução.** A specification FT-AUTH evoluiu para v2.1 (sessão/revogação) e v2.2 (RN-AUTH-013, RNF-AUTH-006, escopo Sprint 1), gerando implementação inicial desalinhada e necessidade de segunda rodada de correção + Review/Audit/Readiness.
- **Encerramento prematuro.** O primeiro ciclo de Closure registrou `FEATURE_BLOCKED` (`closure-report.md`) por gaps em RN-AUTH-013 e RNF-AUTH-006 — requisitos que entraram na spec após parte da implementação.
- **Acoplamento PF ↔ Feature não resolvido.** `JwtAuthenticationFilter` (PF) depende de `accesscontrol` (Feature); `AuthProperties` fora do bounded context. Detectado no Review, permanece como dívida técnica.
- **Propriedades duplicadas de timeout.** `IntegrationProperties` e `ZimbraProperties` coexistiram; `RestClientConfiguration` consumia a primeira enquanto RNF-AUTH-006 exigia a segunda — gap só identificado na auditoria.
- **RF-AUTH-010 em ciclo separado.** Revogação administrativa exigiu implementação, sincronização de artefatos spec e nova validação, alongando o encerramento além dos 6 PKGs originais.
- **Artefatos de progresso desatualizados.** `construction/09-progress.md` e `closure-report.md` refletem estado intermediário (`FEATURE_BLOCKED`), enquanto `construction-state.yaml` já registra `FEATURE_APPROVED` — risco de leitura incorreta do estado real.

---

## 3. O que consumiu mais tempo ou tokens?

| Fator | Evidência |
|-------|-----------|
| **Refino e re-leitura da specification** (v2.1 → v2.2) | Múltiplas rodadas Review; bloqueadores RN-AUTH-013 / RNF-AUTH-006 |
| **Segundo ciclo Review → Audit → Readiness** | Após implementação dos bloqueadores (`blockers-rn-auth-013-rnf-auth-006-implementation-report.md`) |
| **RF-AUTH-010 + rastreabilidade** | Implementação admin, relatório dedicado, atualização de api/use-cases/acceptance-tests/tasks na spec |
| **Reconciliação spec ↔ código** | `reconciliation-report.md` (FT-AUTH) com gaps detalhados por RF/RNF |
| **Infraestrutura de governança no mesmo dia** | Workflow v3.1/v3.2, Feature Identity (`FT-AUTH`), templates e `registry.yaml` (`construction/history/README.md`) |

A implementação dos PKGs core (login, sessão, refresh, logout) foi relativamente linear; o custo concentrado-se em **pós-implementação**: spec drift, auditoria e correção de bloqueadores.

---

## 4. O que atrasou a Sprint?

1. **Elevação de requisitos na spec v2.2** — authz administrativa (HTTP 403) e timeout Zimbra 10s não estavam implementados no primeiro encerramento.
2. **RF-AUTH-010 fora do plano inicial de PKGs** — endpoint admin e TASK-AUTH-BE-020 adicionados após PKG-06.
3. **Gap inicial de cobertura AC** — `09-progress.md` citou cenários AC-AUTH sem cobertura antes da consolidação 14/14.
4. **Duas rodadas formais de encerramento** — BLOCKED → correção → re-Review → APPROVED.
5. **Refatoração de workflow mid-sprint** — adoção de Construction State por Feature e SSOD v3.2 no mesmo dia da execução FT-AUTH.

---

## 5. Quais melhorias agregaram valor?

- **`session.md` imutável + Snapshot** — contexto congelado; PKGs não reabrem spec completa (SESSION-01, CACHE-01).
- **`construction-state.yaml` por unidade** — PF e FT-AUTH com estado independente; consulta obrigatória antes de inferir progresso (STATE-02).
- **`feature-manifest.yaml` como SSOD** — navegação sem explorar árvore do repositório.
- **Escopo Sprint explícito na specification** — backend-only na v2.2 desbloqueou Readiness sem exigir frontend.
- **Relatórios de implementação por requisito crítico** — `reports/rf-auth-010-*` e `reports/blockers-*` aceleram re-auditoria.
- **Infraestrutura de testes PF (PKG-07)** — `AbstractIntegrationTest`, perfis `pf-*-test.properties`, base para aceite FT-AUTH.
- **Decision Ledger Sprint 1A** — CD-S1A-001..005 (H2 Oracle mode, SpringDoc, Resilience4j, etc.) evitam redecidir fundação.
- **Veredito “Aprovado com ressalvas”** — separa bloqueadores de dívida técnica documentada (boundary PF, RF-AUTH-011).

---

## 6. Quais melhorias foram excesso de engenharia?

- **Mudança de workflow (v3.1 → v3.2) durante a execução da primeira Feature** — valor futuro alto, mas custo cognitivo na Sprint que já entregava FT-AUTH.
- **Camadas redundantes de relatório** — `closure-report.md`, `completion-report.md`, `reconciliation-report.md` e `reports/*` com sobreposição parcial de conteúdo; útil para auditoria, pesado para consumo rápido.
- **Duas fontes de timeout** (`IntegrationProperties` vs `ZimbraProperties`) — complexidade que originou o gap RNF-AUTH-006; uma propriedade canônica desde PF teria evitado retrabalho.
- **`JwtStructureValidator` órfão** — código sem uso registrado como pendência baixa no Review.
- **Sincronização manual de múltiplos artefatos spec** (api, use-cases, acceptance-tests, tasks) a cada elevação de versão — trabalho repetitivo além do mínimo para compilar.

---

## 7. Quais decisões passam a ser padrão do projeto?

| # | Padrão | Origem / Regra |
|---|--------|----------------|
| 1 | **PF antes de Features de negócio** | Sprint 1A aprovada → handoff FT-AUTH |
| 2 | **Fluxo de encerramento** `Closure → Review → Audit → Readiness` | `11-feature-execution-workflow.md`, BUILD-01 |
| 3 | **`mvn clean verify` somente no encerramento** | BUILD-01 — PKGs usam validação incremental |
| 4 | **SSOD via `feature-manifest.yaml`** | SSOD-01, Feature Identity `FT-<DOMAIN>` |
| 5 | **Progresso em `construction-state.yaml`** — Session não carrega progresso | STATE-04, SESSION-01 |
| 6 | **Escopo de Sprint declarado na specification** antes da execução | Spec v2.2 `sprint_scope: backend_only` |
| 7 | **Requisitos novos entram antes ou com novo ciclo de PKG** — não assumir encerramento com spec em drift | Lição RN-AUTH-013 / RNF-AUTH-006 |
| 8 | **Timeout Zimbra canônico:** `application.zimbra.timeout-ms` (padrão 10000) | RNF-AUTH-006 |
| 9 | **Authz administrativa mínima:** lista configurável `application.auth.session-administrator-emails` + HTTP 403 | RN-AUTH-013 |
| 10 | **Testes de aceite como gate de Readiness** — 14/14 AC antes de aprovar | `readiness-checklist.md` RR-AUTH-10 |
| 11 | **H2 Oracle mode para testes de integração** | CD-S1A-001 |
| 12 | **Dívida técnica PF↔Feature documentada no Review** — não bloqueia Sprint se fora do escopo | Reconciliation FT-AUTH pós-correção |

---

## Síntese acionável para próximas Features

1. **Congelar specification** (versão + escopo de Sprint) **antes** de `Execute Feature`; mudanças posteriores exigem nova Session (CACHE-02) ou PKG dedicado.
2. **Mapear RNF de configuração para property única** na PF — evitar propriedades paralelas não consumidas.
3. **Planejar endpoints administrativos e authz no execution-plan** inicial, não como extensão pós-PKG-06.
4. **Manter um único SSOT de veredito** (`construction-state.yaml`) e atualizar artefatos legados no mesmo ciclo de encerramento.
5. **Reutilizar o pacote de Review/Audit/Readiness** com veredito graduado (aprovado / aprovado com ressalvas / reprovado) — não tratar frontend como bloqueador quando spec declara N/A.

---

*Documento gerado a partir dos artefatos de construção produzidos na Sprint 1. Não altera código, framework ou processos existentes.*
