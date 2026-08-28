# Levantamento — Artefatos para Planejamento da Próxima Etapa de Migração

**Projeto:** Portal de Comunicação
**Data:** 2026-08-20
**Categoria documental:** Working (transitório — levantamento factual, não é SSOT)
**Responsável:** Claude Code (sessão de levantamento) — validação: Vicente Freitas
**Escopo:** somente leitura. Nenhum arquivo do projeto foi criado, movido, renomeado ou alterado como parte deste levantamento (exceto a criação deste artefato).
**Objetivo:** descobrir, sem propor solução, se o projeto já possui os artefatos necessários para planejar as próximas ondas de migração — evitando duplicar inventário, roadmap ou mapeamento já existentes.
**Método:** busca por palavras-chave (`legado`, `onda`/`wave`, `migra`, `inventário`, `regra de negócio`, `produção`) + leitura de conteúdo dos artefatos mais relevantes encontrados.

---

## 1. Inventário funcional/legado (AS-IS)

| Artefato | Finalidade | Tipo/Status | Aplicável hoje? |
|---|---|---|---|
| `docs/discovery/01-current-modules.md` | 27 módulos funcionais do sistema legado (WordPress + CMS + backend PHP), com Controllers/Services/Frontend como evidência; classificação ATIVO/PARCIAL/LEGADO | Discovery — APROVADO COM RESSALVAS | Sim — inventário funcional primário do legado |
| `docs/discovery/_validation/modules-validation.md` | Valida o inventário acima; lista pendências (endpoints sem controller, backend legado sem `src/`) | Validação complementar | Sim |
| `docs/discovery/frontend-production-discovery.md` (v1.1, 2026-07-15) | Inventário **exaustivo** do frontend legado em produção: 294 `.vue`, 96 páginas, 193 componentes, rotas, RBAC, formulários, telas CRUD, tema, integrações | Inventory only — sem recomendação/migração | Sim — o mais detalhado do repositório |
| `docs/discovery/02-current-rbac.md`, `03-current-data-model.md`, `04-current-endpoints.md`, `05-current-integrations.md`, `06-current-infrastructure.md`, `07-current-architecture.md` | Demais fatias da Discovery AS-IS (RBAC, modelo de dados, ~98 endpoints, integrações, infraestrutura, arquitetura) | Discovery — série 01–07, todos APROVADO COM RESSALVAS | Sim (não lidos linha a linha neste levantamento, apenas referenciados/citados por outros documentos) |
| `docs/discovery/08-technical-debt.md` | Consolida **57 dívidas técnicas** (9 críticas) extraídas de 01–07, sem propor correção | Consolidação — APROVADO COM RESSALVAS | Sim |
| `docs/discovery/09-known-issues.md` | Issues conhecidas do legado (não lido em detalhe neste levantamento) | Discovery | Provável, não verificado em profundidade |
| `docs/discovery/ft-auth-zimbra-homologacao.md` | Homologação específica da integração Zimbra no legado | Discovery pontual | Não verificado em detalhe |

---

## 2. Funcionalidades e módulos (backlog / TO-BE)

| Artefato | Finalidade | Tipo/Status | Aplicável hoje? |
|---|---|---|---|
| `docs/backlog/01-epics.md`, `02-features.md`, `03-user-stories.md` | Catálogo formal: 7 Épicos, 36 Features (`FEATURE-001`…`FEATURE-070`), 24 User Stories | Backlog derivado de Solution Design | Sim (conteúdo não lido linha a linha; referenciado via `05-prioritization.md`) |
| `docs/backlog/04-mvp-scope.md` | Escopo MVP **oficial único** (Etapas 1–5 do roadmap) | **SSOT** do escopo MVP | Sim |
| `docs/backlog/05-prioritization.md` | P0–P3 por Épico/Feature/Story + relatório de consolidação do backlog | Priorização — reconciliado 2026-08-14 | Sim |
| `specs/features/*/` (14 pastas: `area`, `area-colaborador`, `arquivos`, `authentication`, `colaborador`, `equipe`, `federacao`, `home`, `noticia`, `perfil`, `primeiro-acesso`, `servicos`, `session`, `singular`) | Especificação corrente (SSOT) das features em construção | SSOT operacional | Sim — é o que está de fato sendo implementado |

**Achado relevante:** não existe um artefato explícito que cruze os identificadores `FEATURE-0XX` do backlog, os rótulos `FT-XXX` da Discovery/legado (`docs/discovery/frontend-feature-mapping.md`), e os slugs de `specs/features/`. Os três vocabulários de nomeação coexistem sem tabela de equivalência localizada (verificado `specs/features/FEATURE_BASELINE.md` — é um documento de governança de estrutura de Feature, não uma tabela de correspondência).

---

## 3. Ondas/waves ou roadmap de migração

| Artefato | Finalidade | Tipo/Status | Aplicável hoje? |
|---|---|---|---|
| `docs/solution-design/10-delivery-roadmap.md` | **O roadmap arquitetural formal**: 6 Etapas sequenciais (1 Fundação → 2 Núcleo Organizacional → 3 Gestão Documental → 4 Comunicação Interna → 5 Migração Operacional → 6 Descomissionamento), com dependências, critérios de prontidão, riscos e capacidades por etapa | Solution Design — CONCLUÍDA (encerra a camada) | Sim — é a referência de "ondas" mais estruturada do repositório |
| `docs/solution-design/09-migration-strategy.md` | Estratégia de migração AS-IS→TO-BE: convivência, fases lógicas de transição (Fase 0–5), critérios de rollback, descomissionamento | Solution Design — CONCLUÍDA | Sim — alimenta diretamente o `10-delivery-roadmap.md` |
| `docs/governance/05-roadmap.md` | Roadmap "executivo": linha do tempo Discovery→Release 3, sprints de backend (Sprint 0 concluída, Sprint 1 "EM PREPARAÇÃO" = FT-AUTH), marcos M1–M9 | Governança — **última atualização registrada: 2026-07-08** | **Parcialmente desatualizado** (ver conflito abaixo) |
| `docs/governance/01-project-status.md` | Status executivo mais recente (2026-08-14): FT-AUTH/FT-AREA/FT-SINGULAR/FT-EQUIPE/FT-SESSION **closed**; FT-COLABORADOR BE closed/FE em execução; FT-PRIMEIRO-ACESSO spec approved, BE not_started | Governança — mais recente | Sim |
| `docs/discovery/frontend-feature-mapping.md` | Mapeia 15 features do legado (`FT-AUTH`…`FT-AREA-PUBLICA`) para "Sprints" 0–4 com base em dependências técnicas do próprio legado; matriz de dependências e MVP por feature | Discovery — **explicitamente não é plano de projeto** ("Sprint assignments reflect earliest implementable order... not a project plan defined in the repository") | Sim, como insumo — não como plano oficial |
| `docs/construction/delivery/01-mvp.md`, `02-release-plan.md`, `03-cutover-plan.md` | MVP, plano de releases e plano de cutover (Go-Live, janela, rollback, hypercare) | Construction/Delivery — conteúdo não lido em profundidade | Não verificado em detalhe |

**Conflito registrado (não resolvido aqui):** `docs/governance/05-roadmap.md` descreve Sprint 1 (FT-AUTH) como **"EM PREPARAÇÃO"**, enquanto `docs/governance/01-project-status.md` (mais recente, 2026-08-14) afirma que FT-AUTH, FT-AREA, FT-SINGULAR, FT-EQUIPE e FT-SESSION já estão **"closed"** e que o trabalho atual é FT-COLABORADOR/FT-PRIMEIRO-ACESSO. `05-roadmap.md` não parece ter sido atualizado desde 2026-07-08.

---

## 4. Mapeamento legado → projeto atual

| Artefato | Finalidade | Tipo/Status | Aplicável hoje? |
|---|---|---|---|
| `docs/discovery/frontend-feature-mapping.md` | Mapeamento **tela a tela** do frontend legado para as 15 features `FT-*`, com componentes, fluxos, dependências e classificação MVP | Discovery — mapeamento explícito legado→features | Sim — é o mapeamento mais direto encontrado |
| `docs/solution-design/09-migration-strategy.md` | Mapeamento **conceitual** de dados (metadados, documentos, binários, permissões, auditoria, notificações, identidade) e integrações AS-IS→TO-BE, por domínio; matriz de descomissionamento | Solution Design — conceitual, **sem SQL/scripts** | Sim, como estratégia — não como execução |
| `database/reports/*` (`db-bl-02-report.md`, `etapa-04-jpa-ddl-audit.md`, `infra-db-01-application-user-migration.md`, `sync-report-2026-07-22.md`, `report-data-model-documentation-consolidation.md`, etc.) | Reconciliação do schema Oracle TO-BE | Database — não lidos em profundidade neste levantamento | Escopo é o schema Oracle novo, não necessariamente mapeamento MySQL/WordPress legado → Oracle; **precisa verificação dedicada** antes de assumir cobertura |
| `construction/review/*-reconciliation-*.md` (24+ relatórios ad-hoc: `vinculo-organizacional-*`, `cargo-vinculo-*`, `oracle-ddl-jpa-*`) | Reconciliações pontuais durante construção de features específicas | Construction — histórico, **não é SSOT** (conforme `CLAUDE.md`) | Não usar como fonte de planejamento — são evidências pontuais de features já em construção, não mapeamento sistemático legado→atual |

**Achado:** não há um documento único e sistemático "campo a campo" / "tabela a tabela" do schema MySQL+CPT+taxonomia do WordPress legado para o schema Oracle novo — o que existe é (a) mapeamento de **telas/funcionalidades** (`frontend-feature-mapping.md`) e (b) estratégia **conceitual** de dados por domínio (`09-migration-strategy.md`), explicitamente sem scripts.

---

## 5. Regras de negócio

| Artefato | Finalidade | Tipo/Status | Aplicável hoje? |
|---|---|---|---|
| `docs/domain/09-business-rules.md` | Catálogo de 45 regras de negócio (`BR-001`–`BR-045`), derivadas de aggregates/domain-events/context-map; regras críticas, ambiguidades e lacunas remanescentes explicitadas | **SSOT** — Domain, confiança Médio-Alto | Sim |
| `specs/features/*/specification.md` | Regras de negócio específicas por feature (não lidas individualmente neste levantamento) | SSOT por feature | Provável, não verificado em detalhe |

**Nota:** `09-business-rules.md` declara explicitamente que **nenhuma fonte Discovery foi usada** — ou seja, as regras de negócio vieram do domínio modelado, não de engenharia reversa do legado. Isso significa que regras de negócio *implícitas apenas no comportamento do sistema legado* podem não estar cobertas por este catálogo.

---

## 6. Validações contra produção

| Artefato | Finalidade | Tipo/Status | Aplicável hoje? |
|---|---|---|---|
| `docs/api/discrepancies.md` | 12 divergências (`DISC-001`–`DISC-012`) entre specs e implementação do backend novo (nomenclatura, autorização, CSRF, endpoints ausentes) | API-DOCS-01, 2026-07-16 — "registrar sem corrigir" | Sim |
| `docs/api/validation/homologation-report.md` | Homologação de 27/27 endpoints do backend novo via testes Java (MockMvc+Oracle) + Postman/Newman | API-VALIDATION-01, 2026-07-16 — **APROVADO** | Sim, mas com escopo limitado |
| `docs/api/validation/test-matrix.md` | Matriz de testes referenciada pelo relatório acima | Não lido em detalhe | Referenciado |
| `database/validation/oracle-schema-validation-2026-07-22.md` | Validação do schema Oracle | Não lido em detalhe | Referenciado |

**Achado importante:** os artefatos de "validação" encontrados comparam o **backend novo contra suas próprias specs/testes** (contrato interno TO-BE), não o **sistema novo contra o comportamento real do sistema de produção legado**. Não foi localizado nenhum artefato de validação funcional/comportamental do tipo "saída do legado em produção vs. saída equivalente do novo sistema" (ex.: comparação de dados reais migrados, paridade funcional testada ponta a ponta contra produção). Se esse tipo de validação é necessário para a próxima etapa, é uma lacuna a decidir, não a assumir como coberta.

---

## 7. Dependências e priorização de migração

| Artefato | Finalidade | Tipo/Status | Aplicável hoje? |
|---|---|---|---|
| `docs/backlog/05-prioritization.md` | Matriz Épico→Feature→Story→MVP→Prioridade (P0–P3) | Priorização reconciliada | Sim |
| `docs/solution-design/10-delivery-roadmap.md` (seção "Dependências Entre Etapas") | Dependências formais entre as 6 etapas, com "impacto se violada" | Solution Design — CONCLUÍDA | Sim |
| `docs/discovery/frontend-feature-mapping.md` (Feature Matrix / Sprint Dependency Chain) | Dependências entre features do legado, derivadas de evidência técnica (não de decisão de projeto) | Discovery | Sim, como insumo técnico |
| `docs/architecture/09-risk-assessment.md` | Riscos (R-001…R-032) referenciados extensivamente pelo roadmap e pela estratégia de migração | Architecture — não lido em profundidade neste levantamento | Referenciado por múltiplos documentos citados acima; provavelmente central para priorização de riscos de migração |

---

## 8. Artefatos correlatos encontrados, mas de escopo diferente do pedido

| Artefato | Por que não se aplica diretamente |
|---|---|
| `specs/foundation/migrations/SPEC-MIGRATION-CLAUDE-CODE.md` | "Migração" aqui é do mecanismo de automação (Cursor → Claude Code), não do sistema legado |
| `docs/audit/12-structural-simplification-audit-w0-w1.md`, `docs/governance/structural-simplification-plan-w2.md`, `docs/audit/13-decision-inventory.md`, `docs/audit/14-governance-audit-inventory-status.md` | Auditam a **documentação/governança do repositório** (SSOT, duplicação, decisões abertas), não o inventário funcional do legado. `14-...md` (2026-08-20) já registra que `12-...` e `plan-w2` estão parcialmente desatualizados quanto a `docker-compose.yml` e à decisão de storage — não precisou ser re-verificado aqui |
| `construction/` (toda a árvore) | Execução histórica v4.1 — conforme `CLAUDE.md`, não é SSOT e não deve ser tratado como fonte de planejamento diário |

---

## Respostas objetivas

**1. Já existe inventário das funcionalidades do sistema de produção?**
Sim. `docs/discovery/01-current-modules.md` (módulos/backend) e `docs/discovery/frontend-production-discovery.md` (frontend, exaustivo) cobrem isso, complementados por `docs/discovery/frontend-feature-mapping.md` (agrupamento em features) e `docs/discovery/08-technical-debt.md` (dívidas). Ambos "APROVADO COM RESSALVAS" — com lacunas explícitas já documentadas (endpoints órfãos, módulos PARCIAL, backend PHP sem `src/`).

**2. Já existe planejamento/ordem de ondas de migração?**
Sim, em nível arquitetural: `docs/solution-design/10-delivery-roadmap.md` define 6 etapas sequenciais com dependências e critérios de prontidão, apoiado por `docs/solution-design/09-migration-strategy.md`. **Não existe**, porém, um cronograma operacional (esse documento se declara explicitamente "não é sprint/cronograma"). O roadmap "executivo" mais operacional (`docs/governance/05-roadmap.md`) existe mas está desatualizado frente ao status real mais recente (`docs/governance/01-project-status.md`).

**3. Já existe mapeamento entre legado e projeto atual?**
Parcialmente. Existe mapeamento de **telas/funcionalidades** (`frontend-feature-mapping.md`) e estratégia **conceitual** de dados/integrações (`09-migration-strategy.md`), mas não uma tabela sistemática campo-a-campo do schema legado (MySQL/WordPress) para o schema Oracle novo, nem uma tabela de equivalência entre os três vocabulários de identificação de feature em uso (`FEATURE-0XX`, `FT-XXX`, slugs de `specs/features/`).

**4. Já existe levantamento de regras de negócio ou validações contra produção?**
Regras de negócio: sim, `docs/domain/09-business-rules.md` (SSOT), mas derivado do domínio modelado, não de engenharia reversa do comportamento do legado. Validações: existem, mas validam o **backend novo contra suas próprias specs** (`docs/api/discrepancies.md`, `docs/api/validation/homologation-report.md`) — não foi localizada validação do novo sistema contra o comportamento real do legado em produção.

**5. Qual conjunto de artefatos existentes deve ser usado como base para o próximo planejamento?**
Como base primária: `docs/discovery/01-current-modules.md` + `frontend-production-discovery.md` + `frontend-feature-mapping.md` (o que existe no legado), `docs/solution-design/10-delivery-roadmap.md` + `09-migration-strategy.md` (ondas e estratégia arquitetural), `docs/backlog/04-mvp-scope.md` + `05-prioritization.md` (o que priorizar), `docs/domain/09-business-rules.md` (regras), e `docs/governance/01-project-status.md` (estado real mais recente, para não planejar sobre uma etapa já concluída). Isso é uma leitura factual de aplicabilidade, não uma recomendação de priorização — cabe decisão humana sobre qual conjunto usar.

---

## Tabela final

| Necessidade | Artefato existente | Caminho | Status | Observação |
|---|---|---|---|---|
| Inventário funcional/legado | Discovery — Módulos Atuais | `docs/discovery/01-current-modules.md` | APROVADO COM RESSALVAS | 27 módulos; base para toda a Discovery |
| Inventário funcional/legado | Frontend Production Discovery | `docs/discovery/frontend-production-discovery.md` | Inventory only, v1.1 (2026-07-15) | Mais detalhado do repositório (294 telas/componentes) |
| Inventário funcional/legado | Validação de módulos | `docs/discovery/_validation/modules-validation.md` | APROVADO COM RESSALVAS | Pendências não bloqueantes |
| Inventário funcional/legado | Dívida técnica consolidada | `docs/discovery/08-technical-debt.md` | APROVADO COM RESSALVAS | 57 dívidas, 9 críticas |
| Funcionalidades e módulos (TO-BE) | Backlog (épicos/features/stories) | `docs/backlog/01-epics.md`, `02-features.md`, `03-user-stories.md` | Reconciliado 2026-06-22 | Vocabulário `FEATURE-0XX` sem crosswalk para `specs/features/` |
| Funcionalidades e módulos (TO-BE) | Escopo MVP oficial | `docs/backlog/04-mvp-scope.md` | **SSOT** | Fonte normativa do MVP |
| Funcionalidades e módulos (TO-BE) | Features em construção | `specs/features/*/` (14 pastas) | SSOT operacional | O que está de fato sendo implementado agora |
| Ondas/waves de migração | Roadmap de entrega arquitetural | `docs/solution-design/10-delivery-roadmap.md` | CONCLUÍDA | 6 etapas; referência principal de "ondas" |
| Ondas/waves de migração | Estratégia de migração AS-IS→TO-BE | `docs/solution-design/09-migration-strategy.md` | CONCLUÍDA | Convivência, fases, rollback, descomissionamento |
| Ondas/waves de migração | Roadmap executivo/sprints | `docs/governance/05-roadmap.md` | **Desatualizado** (última att. 2026-07-08) | Conflita com `01-project-status.md` sobre status de FT-AUTH |
| Ondas/waves de migração | Status atual do projeto | `docs/governance/01-project-status.md` | Mais recente (2026-08-14) | Usar como fonte de status real |
| Ondas/waves de migração | Mapeamento legado→sprint (técnico) | `docs/discovery/frontend-feature-mapping.md` | Insumo — não é plano de projeto | Explicitamente disclaimed pelo próprio documento |
| Mapeamento legado → atual | Mapeamento de telas/features | `docs/discovery/frontend-feature-mapping.md` | Discovery | Tela a tela, legado → `FT-*` |
| Mapeamento legado → atual | Mapeamento conceitual de dados/integrações | `docs/solution-design/09-migration-strategy.md` | Conceitual, sem SQL | Sem tabela campo-a-campo de schema |
| Mapeamento legado → atual | Reconciliação de schema Oracle | `database/reports/*`, `database/validation/*` | Não verificado em profundidade | Confirmar se cobre legado MySQL→Oracle ou só Oracle interno |
| Regras de negócio | Catálogo de regras de negócio | `docs/domain/09-business-rules.md` | **SSOT**, confiança Médio-Alto | BR-001–BR-045; não derivado do legado |
| Validações contra produção | Divergências spec vs. implementação | `docs/api/discrepancies.md` | Registrado sem correção, 2026-07-16 | Valida backend novo vs. specs, não vs. legado |
| Validações contra produção | Homologação de endpoints | `docs/api/validation/homologation-report.md` | APROVADO, 2026-07-16 | 27/27 endpoints; escopo é o backend novo |
| Validações contra produção | Validação funcional legado vs. novo | — não localizado — | **Lacuna** | Nenhum artefato compara comportamento real de produção legada com o sistema novo |
| Dependências e priorização | Matriz de priorização | `docs/backlog/05-prioritization.md` | Reconciliado 2026-08-14 | P0–P3 por épico/feature/story |
| Dependências e priorização | Dependências entre etapas | `docs/solution-design/10-delivery-roadmap.md` (seção dedicada) | CONCLUÍDA | Formal, com "impacto se violada" |
| Dependências e priorização | Avaliação de riscos | `docs/architecture/09-risk-assessment.md` | Não verificado em profundidade | Referenciado por roadmap e migration-strategy |

---

## Nota de governança

Este documento é um **levantamento factual**, não uma auditoria nova nem uma proposta de reconciliação ou de plano de migração. Nenhum artefato citado foi corrigido, movido ou reclassificado. Itens marcados "não verificado em profundidade" ou "não lido em detalhe" indicam que a existência do arquivo foi confirmada, mas seu conteúdo não foi lido linha a linha neste levantamento — recomenda-se leitura dedicada antes de descartá-los ou assumir cobertura.
