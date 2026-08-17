# Baseline — Saneamento Documental

| Campo | Valor |
|-------|-------|
| Artefato | `construction/review/baseline-saneamento.md` |
| Data | 2026-08-17 |
| Tipo | Relatório de saneamento (documentação/governança) |
| Escopo | Propagação de decisões vigentes pós-checkpoint de baseline |
| Restrição | Código, DDL, scripts SQL e testes **não alterados** |

---

## 1. Objetivo

Transformar as conclusões do [checkpoint de baseline](checkpoint-baseline-primeiro-acesso.md) e da [investigação Contexto Ativo × DH-02](contexto-ativo-dh02-investigacao.md) em documentação normativa coerente, sem criar novas decisões, sem reabrir decisões existentes e sem alterar implementação.

---

## 2. Decisões utilizadas como fundamento

| ID | Uso no saneamento |
|----|-------------------|
| **DH-02** | 1 vínculo cadastral; Contexto Ativo derivado |
| **DH-03** | COLABORADOR persistido após vínculo completo; `locateOrCreate` superseded |
| **DH-04** | Federação + Singular + Área obrigatórios; Equipe opcional |
| **DH-PA-01** | Credencial temporária; sem AUTH_SESSAO operacional no PA |
| **DH-PA-02** | Wizard domínio → Singular; bloqueio domínio sem Singular |
| **DH-PA-03** | COLABORADOR criado ao final do PA |
| **DH-CARGO-01** | CARGO opcional na criação; supersession parcial DEC-DB-027 |
| **DEC-FA-003** | Supersession parcial P1/P4 (N vínculos); P2/P3/P5 mantidos |
| **DEC-DB-028** | Modelo TO-BE vínculo (documentado; DDL pendente) |
| **Conclusão Contexto Ativo** | Sem persistência separada; projeção do vínculo |

---

## 3. Artefatos reconciliados

| Artefato | Alteração | Fundamento | Tipo |
|----------|-----------|------------|------|
| `specs/features/authentication/decisions.md` — DA-AUTH-011 | Supersession parcial `locateOrCreate`/sessão no login; regra vigente TO-BE | DH-03, DH-PA-01 | supersession |
| `specs/features/session/specification.md` | RN-SESSION-003 superseded; Contexto Ativo derivado; DEC-FA-003 tabela | DH-02 | atualização |
| `specs/features/primeiro-acesso/specification.md` | TO-BE completo: responsabilidades, RF/RN, §6 Contexto Ativo, §11 consolidado | DH-*, DH-PA-*, DH-CARGO-01 | atualização |
| `specs/features/primeiro-acesso/flows.md` | Fluxo TO-BE wizard; superseded N-vínculos | DH-02, DH-03 | atualização |
| `specs/features/primeiro-acesso/state-machine.md` | Estados TO-BE; superseded SelectingContext etc. | DH-02, DH-PA-01 | atualização |
| `specs/features/primeiro-acesso/api.md` | PA-API-001/002/003 superseded; 004/005 vigentes; 006 pendente | DH-02, DH-03 | atualização |
| `specs/features/primeiro-acesso/use-cases.md` | UC-002 onboarding; UC-003/004/005/007 superseded | DH-02, DH-03 | atualização |
| `specs/features/primeiro-acesso/acceptance-tests.md` | AT reconciliados; AT-002/005/009 superseded | DH-02 | atualização |
| `specs/features/primeiro-acesso/traceability.md` | INC-PA-001..004 encerradas; RF superseded marcados | DH-02 | atualização |
| `specs/features/primeiro-acesso/README.md` | Status APPROVED unificado | feature.yaml | alinhamento de status |
| `specs/features/primeiro-acesso/tasks.md` | Status APPROVED | feature.yaml | alinhamento de status |
| `docs/domain/02-business-glossary.md` | Contexto Ativo derivado; Onboarding wizard | DH-02, DH-03 | correção documental |
| `docs/domain/04-domain-concepts.md` | Colaborador 1 vínculo; PA wizard | DH-02 | correção documental |
| `docs/domain/01-vision.md` | Fluxo de valor TO-BE | DH-03 | correção documental |
| `docs/domain/10-open-questions.md` | OQ-027 supersession nota | DH-02 | referência cruzada |
| `database/GOVERNANCE.md` | §6 AS-IS × TO-BE | DEC-DB-028, GAPs | atualização |
| `database/model/README.md` | Nota AS-IS/TO-BE | GOVERNANCE §6 | referência cruzada |
| `construction/review/primeiro-acesso-blocking-decisions-package.md` | Nota DEC-DB-027 NOT NULL superseded em §3 | DH-CARGO-01 | correção documental |

---

## 4. Artefatos não alterados

| Artefato | Motivo |
|----------|--------|
| `backend/`, `frontend/` | Restrição explícita — pendências registradas abaixo |
| `database/ddl/`, `database/dml/`, `database/migrations/` | Restrição — AS-IS preservado; TO-BE em GOVERNANCE §6 |
| `docs/governance/03-open-decisions.md` | Já vigente e completo; não requer alteração |
| `docs/domain/09-business-rules.md` | BR-041 já contém supersession DH-02 |
| `database/model/05-decisions-and-risks.md` | DEC-DB-027/028 já com supersession registrada |
| `construction/review/*` (demais 20 arquivos) | Evidência histórica — classificados §8 |
| `.cursor/`, `construction/` framework legado | Apenas classificação §9 — sem remoção |
| Testes (`frontend/test/`, `backend/src/test/`) | Restrição — gaps registrados §6 |

---

## 5. Conflitos resolvidos por decisão existente

| Conflito | Resolução aplicada |
|----------|-------------------|
| **DA-AUTH-011** × DH-03/DH-PA-01 | Supersession formalizada em `authentication/decisions.md`; texto histórico preservado |
| **RN-SESSION-003** × DH-02 | Marcada SUPERSEDED; RN-SESSION-002/005 mantidas |
| **RF-PA-007** / **RN-PA-006** | Marcados SUPERSEDED em specification, traceability, acceptance-tests |
| **RF-PA-003/004**, **RN-PA-003/008** | SUPERSEDED (modelo N vínculos / persistência separada) |
| **CARGO** obrigatório na criação | DH-CARGO-01; nota em blocking-decisions-package §3 |
| **Contexto Ativo** como estado cadastral | Derivado do vínculo (DH-02); INC-PA-004 encerrada |
| **INC-PA-001..003** | Encerradas em traceability.md |

---

## 6. Pendências de implementação

### Código (PENDÊNCIA DE IMPLEMENTAÇÃO)

| Item | Violação | Decisão |
|------|----------|---------|
| `ColaboradorService.locateOrCreate()` em `finalizeLogin` | Cria COLABORADOR no login sem vínculo completo | DH-03, DH-PA-01 |
| `SessionService.createSession()` imediato após login | AUTH_SESSAO operacional antes do PA | DH-PA-01 |
| Ausência de wizard PA / credencial temporária | PA não implementado | DH-PA-01/02/03 |
| `session.store` auto-assign sem gate operacional | Não bloqueia sem Área | DEC-FA-002, DH-04 |
| Ausência de APIs PA (onboarding, home) | PA-API-004/006 não existem | specs PA |

### DDL / banco (PENDÊNCIA DE IMPLEMENTAÇÃO)

| GAP | Descrição |
|-----|-----------|
| GAP-028-01 | Remover/deslocar `locateOrCreate` |
| GAP-028-02 | NOT NULL `COD_SINGULAR`/`COD_AREA` (DEC-DB-028) |
| GAP-028-03 | Credencial temporária PA |
| GAP-028-04 | Mapeamento domínio → Singular |
| GAP-028-05 | FT-PRIMEIRO-ACESSO implementação |
| TO-BE CARGO | Tabela `CARGO` — domínio aprovado, DDL pendente |

### Testes (atualização futura — não executada)

| Categoria | Exemplos |
|-----------|----------|
| Comportamento obsoleto | Testes que assumem `locateOrCreate` implícito sem gate PA |
| Ausentes para TO-BE | AT-PA onboarding, domínio sem Singular, credencial temporária |
| A atualizar na implementação | `session.store.spec.ts`, `auth.guard.spec.ts` — gates PA |

---

## 7. Pendências de decisão humana

**NENHUMA** identificada nesta etapa.

A conclusão Contexto Ativo × DH-02 ([contexto-ativo-dh02-investigacao.md](contexto-ativo-dh02-investigacao.md)) encerrou a questão sem decisão pendente. PA-API-006 (contrato onboarding) permanece **pendência de implementação** delegada à engenharia — não é lacuna de negócio.

---

## 8. Documentação candidata a limpeza posterior

| Artefato | Classificação | Justificativa |
|----------|---------------|---------------|
| `construction/review/vinculo-organizacional-*` (9 arquivos) | **HISTÓRICO** / CANDIDATO A CONSOLIDAÇÃO | Decisões DH-02/03/04 fechadas |
| `construction/review/primeiro-acesso-dh-pa-03-analysis.md` | **HISTÓRICO** | DH-PA-03/DH-CARGO-01 encerrados; trechos pré-decisão |
| `construction/review/primeiro-acesso-blocking-decisions-package.md` | **HISTÓRICO** (referência) | Evidência decisões DH-PA-* |
| `construction/review/checkpoint-baseline-primeiro-acesso.md` | **ATUAL E NECESSÁRIO** | Checkpoint baseline |
| `construction/review/contexto-ativo-dh02-investigacao.md` | **ATUAL E NECESSÁRIO** | Conclusão Contexto Ativo |
| `construction/review/oracle-ddl-jpa-reconciliation-etapa4.md` | **ATUAL** (evidence técnica) | Baseline JPA×DDL |
| `construction/review/readiness-checklist.md` | **SEM CONSUMIDOR** | Candidato legado |
| `specs/features/primeiro-acesso/use-cases.md` § UC-PA-003/004/005/007 | **SUPERSEDED** (inline) | Preservado como histórico |
| `database/model/02-logical-model.md` | **OBSOLETO** | Data 2026-07-10; não reflete DH-02 |
| `docs/domain/01-vision.md` trechos discovery legado | **HISTÓRICO** parcial | Capacidades CMS legado |

**Nenhum arquivo removido nesta etapa.**

---

## 9. Camadas candidatas a LEGACY / remoção / consolidação

| Estrutura | Função | Consumidor | Evidência uso | Motivo candidato | Risco remoção |
|-----------|--------|------------|---------------|------------------|---------------|
| `.cursor/orchestrator/*` | Construction v4.1 | Legado | Substituído por `development-workflow.md` | **LEGACY** | Médio — agentes podem referenciar |
| `.cursor/agents/construction-engineer.mdc` | PKG construction | Não no project-index | 3 agentes ativos no index | **LEGACY** | Baixo |
| `.cursor/agents/feature-implementer.mdc` | Implementação genérica | Archive parcial | Sobreposição backend-engineer | **CONSOLIDAR** | Baixo |
| `construction/11-14`, golden-template v4.1 | Framework legado | Features históricas | `09-framework-simplification-scope.md` | **LEGACY** | Baixo |
| `feature-manifest.yaml` / `pkg-XX/status.md` | Estado construction | Transição | `minimal-ssot.md` proíbe SSOT | **LEGACY** | Baixo |
| `construction/registry.yaml` status fields | Índice indicativo | Navegação | Documentado como não-SSOT | **MANTER** (transição) | — |
| `construction/features/FT-PRIMEIRO-ACESSO/pkg-*` | Planejamento PKG | Não iniciado | `construction-state.yaml` not_started | **CANDIDATO LEGACY** pós-implementação | Baixo |

---

## 10. Estado da baseline após saneamento

# **BASELINE RECONCILIADA — PRONTA PARA VALIDAÇÃO**

**Critérios atendidos:**

- Nenhuma nova decisão criada
- Nenhuma decisão existente alterada (apenas supersession documental registrada)
- Decisões vigentes refletidas em specs PA, FT-SESSION, DA-AUTH-011
- PA sem documentação normativa conflitante conhecida (artefatos superseded marcados)
- Contexto Ativo tratado como projeção derivada do vínculo
- CARGO não aparece como requisito de criação
- Regras N vínculos reconciliadas com DH-02
- `locateOrCreate` identificado como superseded normativamente
- AS-IS/TO-BE banco separados em `database/GOVERNANCE.md`
- Scripts SQL intactos
- Código intacto com pendências explicitadas
- Candidatos a limpeza identificados sem remoção prematura

**Validação humana recomendada antes do commit:** revisar TO-BE PA (`specification.md` §11, `flows.md`) e confirmar que PA-API-006 como pendência de implementação é aceitável sem contrato fixado.

---

*Fim do relatório de saneamento.*
