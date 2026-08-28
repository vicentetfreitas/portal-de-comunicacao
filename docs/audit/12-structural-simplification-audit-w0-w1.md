# Structural Simplification Audit — W0/W1

**Projeto:** Portal de Comunicação
**Data:** 2026-08-20
**Categoria documental:** Working (transitório — ver critério de remoção)
**Responsável:** Claude Code (sessão de análise) — validação e decisão: Vicente Freitas
**Camada dona:** Governança documental / arquitetura de repositório
**SSOT correspondente:** nenhum ainda — este documento alimenta decisões que, uma vez aprovadas, devem ser incorporadas a `specs/foundation/minimal-ssot.md` e/ou `docs/governance/07-documentation-architecture.md`. Após incorporação, este artefato deve ser removido (Git preserva o histórico).
**Critério de remoção (PENDING_REMOVAL quando):** as decisões pendentes da Seção 8 forem resolvidas e as ações resultantes (consolidação, arquivamento físico, correção de conteúdo obsoleto) estiverem aplicadas — ou o conteúdo tiver sido incorporado a um SSOT permanente.
**Escopo:** somente leitura/análise. Nenhum arquivo do projeto foi movido, renomeado, criado (exceto este) ou alterado como parte da auditoria.

---

## 1. Resumo Executivo

**Estado atual:** o projeto já passou por uma primeira tentativa de simplificação — "Etapa 2/3", concluída em **2026-08-13/14** — que criou `specs/foundation/minimal-ssot.md`, `development-workflow.md` e `docs/governance/09-framework-simplification-scope.md` como camada operacional simplificada. Essa tentativa **não removeu nada fisicamente**: apenas empilhou uma nova camada de SSOT sobre o framework legado (`construction/` v4.1, `engineering/`, `.cursor/`). O resultado líquido foi **mais arquivos**, não menos.

**Principais fontes de complexidade:**
- Quatro sistemas de governança de processo coexistindo: `specs/foundation/` (novo, Etapa 2), `construction/` (v4.1, 245 arquivos), `engineering/` (integração, 23 arquivos), `.cursor/` (43 `.mdc`, com auto-arquivamento parcial em `.cursor/archive/`).
- `docs/` com 144 arquivos em 13 subpastas; `specs/` com 86; sobreposição declarada entre `docs/architecture/` × `docs/solution-design/` × `specs/architecture/`.
- Três catálogos de decisão em paralelo (`docs/governance/03-open-decisions.md`, `docs/architecture/08-decision-records.md`, `docs/technology/04-decision-log.md`) com **colisão de IDs já reconhecida pelo próprio projeto**.
- Documentos que apontam para documentos que apontam para documentos: `minimal-ssot.md` ↔ `development-workflow.md` ↔ `framework-simplification-scope.md` ↔ `agent-commands.md` ↔ `path-conventions.md` formam um grafo denso que qualquer agente precisa atravessar antes de tocar código.

**Principais riscos:**
- Um documento hoje classificado como **SSOT** (`docs/architecture/11-target-repository-structure.md`) descreve uma stack **diferente da aprovada** (Next.js, PostgreSQL, CMS/WordPress) — contradiz `docs/technology/01-technology-stack.md`, que é o documento correto e vigente.
- `docker-compose.yml` na raiz ainda sobe `postgres:16-alpine`, apesar de Oracle ser o banco aprovado — já reconhecido em `docs/governance/01-project-status.md` como pendência ("Etapa 5").
- O repositório Git está no **GitHub** (`github-personal:vicentetfreitas/portal-de-comunicacao`), e a stack aprovada declara GitHub/GitHub Actions oficialmente — o objetivo de preparar para **GitLab** precisa de decisão explícita antes de qualquer ação.
- Não existe pipeline de CI para o backend (`.github/workflows/` só tem `frontend.yml`).

---

## 2. Baseline

### Árvore estrutural relevante (raiz)

```text
portal-de-comunicacao/
├── CLAUDE.md                  ← mecanismo de execução (não SSOT)
├── docker-compose.yml         ← ainda referencia Postgres (gap conhecido)
├── backend/                   ← Java 25 + Spring Boot 4, estrutura DDD por módulo
├── frontend/                  ← Vue 3 + Quasar + TS
├── database/                  ← SSOT do schema Oracle (baseline > ddl > migrations)
├── docs/                      ← 144 .md — conhecimento permanente (13 subpastas)
├── specs/                     ← 86 .md — comportamento esperado (foundation + features)
├── construction/              ← 245 arquivos — Engineering Framework v4.1 (legado, "Archive" declarado)
├── engineering/                ← 23 arquivos — Sprint de Integração (pós-construction)
├── .cursor/                    ← 43 .mdc — regras de agentes Cursor (parcialmente já arquivado)
├── reports/project/            ← 1 relatório órfão na raiz (fora de docs/audit)
└── .github/workflows/          ← só frontend.yml (sem CI backend)
```

Não existe diretório `legacy/`. A separação ativo/histórico hoje é **apenas declarativa** (via `minimal-ssot.md`), não estrutural.

### Volume documental por área

| Área | Arquivos | Papel declarado |
|---|---|---|
| `docs/` | 144 | Conhecimento permanente (SSOT por tipo de informação) |
| `specs/` | 86 | Comportamento esperado (SSOT funcional) |
| `construction/` | 245 | Execução histórica v4.1 — **Archive**, não fluxo diário |
| `engineering/` | 23 | Sprint de integração pós-construction |
| `.cursor/` | 43 | Automação de agentes Cursor — não carregar como contexto cotidiano |
| `database/` | ~40 | SSOT de schema Oracle |

### SSOTs identificados (declarados nos próprios documentos)

| Informação | SSOT declarado |
|---|---|
| Precedência geral | `specs/foundation/minimal-ssot.md` |
| Governança documental | `docs/governance/07-documentation-architecture.md` |
| Estado de Feature | `specs/features/<slug>/feature.yaml` |
| Stack tecnológica | `docs/technology/01-technology-stack.md` |
| Schema físico | `database/` (baseline > ddl > migrations) |
| Regras de negócio | `docs/domain/09-business-rules.md` |

Existem hoje **dois documentos disputando o papel de "SSOT do SSOT"** — `minimal-ssot.md` (operacional, Etapa 2/3) e `07-documentation-architecture.md` (documental, Exit Gate). Eles se citam mutuamente como "complementares", mas um novo agente precisa ler os dois para entender precedência.

---

## 3. Auditoria Documental

| Grupo | Localização | Categoria | Observação |
|---|---|---|---|
| Specs de feature ativas | `specs/features/<slug>/` (14 features) | **A/B** SSOT/Implementação | Função real clara; ponto de entrada correto para trabalho diário |
| Foundation (13 docs) | `specs/foundation/` | **A** SSOT | Denso e auto-referenciado; ~metade do conteúdo de `agent-commands.md` é histórico (Apêndices A/B/C) misturado ao operacional |
| `docs/domain`, `docs/architecture` (núcleo), `docs/implementation`, `docs/technology` | `docs/` | **A** SSOT | Função real; `docs/technology/01-technology-stack.md` é o documento correto e atualizado |
| `docs/architecture/11-target-repository-structure.md` | `docs/architecture/` | **I** Obsoleto | Descreve Next.js/PostgreSQL/CMS — contradiz a stack aprovada. Classificado como SSOT mas com conteúdo errado |
| `docs/solution-design/` (11 docs) | `docs/solution-design/` | **E/G** Referência com sobreposição | `02-system-context.md`, `03-container-architecture.md` duplicam conceitos já em `docs/architecture/01-system-context.md`, `02-container-diagram.md` |
| `docs/audit/` (12 relatórios) | `docs/audit/` | **F** Histórico/Evidence | Auditorias de fase já concluídas; corretamente classificadas como Evidence pelo próprio projeto |
| `docs/governance/03-open-decisions.md`, `docs/architecture/08-decision-records.md`, `docs/technology/04-decision-log.md` | `docs/` | **G** Duplicado (reconhecido) | Colisão de IDs já documentada como defeito não resolvido |
| `construction/` inteiro | raiz | **F** Histórico | Já declarado "Archive" por `minimal-ssot.md`, mas fisicamente ocupa o mesmo nível de `docs/`/`specs/` — nenhum sinal estrutural de que é histórico |
| `construction/review/` (24 relatórios ad-hoc) | `construction/review/` | **F/H** Histórico/Intermediário | Nomes como `vinculo-organizacional-blocking-decisions-analysis.md`, `contexto-ativo-dh02-investigacao.md` — típicos relatórios "Working" que deveriam ter sido incorporados e removidos, não arquivados indefinidamente |
| `engineering/integration/` | `engineering/` | **D/F** Operação/parcialmente histórico | Sprint única (`sprint-03-org-backend`) já `APPROVED`; framework genérico mantido para uma única execução concreta |
| `.cursor/archive/` | `.cursor/` | **F** Histórico | Já auto-arquivado — bom sinal, mas convive com `.cursor/rules|prompts|orchestrator` ativos sem separação de custo de contexto |
| `agent-commands.md` Apêndices A/B/C | `specs/foundation/` | **G/H** Duplicado/Intermediário dentro de um doc ativo | ~140 linhas de conteúdo histórico dentro do documento mais consultado no fluxo diário |
| `reports/project/stabilization-report.md` | raiz `reports/` | **J** Sem função identificável clara | Único arquivo nesse diretório na raiz, fora de `docs/audit/` onde relatórios equivalentes vivem |
| `specs/domain/` (content-model, publication-model...) | `specs/domain/` | **Decisão pendente** (ver Seção 8, item 3) | Modelo de domínio de conteúdo/CMS, distinto do domínio de negócio em `docs/domain/`. Pode ser bounded context legítimo e ativo, ou resíduo de concepção anterior — não classificável sem confirmação humana |

### Candidatos a consolidação (não executar agora)
- `docs/solution-design/02-system-context.md` + `docs/architecture/01-system-context.md` → mesmo conceito, dois lugares.
- Três catálogos de decisão → um único catálogo com namespace de ID.
- `docs/frontend/` + `docs/implementation/05-frontend-architecture.md` + `docs/construction/frontend/` → três lugares para arquitetura frontend, já sinalizado no próprio `07-documentation-architecture.md` como pendente de revisão.

### Candidatos a remoção (Working já incorporado / sem função)
- `construction/review/*` — relatórios de investigação pontual já resolvidos.
- Apêndices A/B/C de `agent-commands.md` → mover para `construction/history/`, não deletar (Git preserva; o documento ativo fica mais leve).

---

## 4. Auditoria Arquitetural

### Backend

Estrutura DDD por módulo, razoavelmente limpa em nível estrutural:

```text
backend/src/main/java/.../portalcomunicacao/
├── accesscontrol/{domain,application,infrastructure,interfaces}   (43 arquivos)
├── organization/{domain,application,infrastructure,interfaces}    (45 arquivos)
├── infrastructure/{integration,logging,observability,persistence,security}  (36 arquivos)
├── shared/{constants,dto,exception,util,validation}                (31 arquivos)
├── configuration/{async,jackson,locale,persistence,properties}     (17 arquivos)
└── interfaces/rest/                                                 (4 arquivos)
```

**Observação estrutural:** existe `interfaces/rest/` no nível raiz **e** `interfaces/` dentro de cada módulo (`accesscontrol/interfaces/`, `organization/interfaces/`). Não foi possível, com leitura superficial, determinar se isso é intencional (interfaces globais vs. por módulo) ou uma camada artificial duplicada.

Fora isso, não há sinal de over-engineering — a divisão por camada é padrão e o volume por módulo é proporcional às features implementadas.

### Frontend

Organização convencional Quasar (pages/components/services/composables/stores/router/types por domínio):

| Observação | Detalhe |
|---|---|
| `frontend/src/features/` | Contém apenas `.gitkeep` — diretório vestigial de uma organização "por feature" nunca adotada. Candidato a remoção. |
| `frontend/src/stores/` | Apenas 3 arquivos, contra 21 em `composables/` — maior parte do estado/lógica reside em composables, não em Pinia stores. Pode ser intencional; vale confirmar se há regra explícita de quando usar um ou outro. |
| `components/ds/{atoms,molecules,organisms}` | Design System em Atomic Design, já em evolução controlada — fora do escopo desta auditoria por instrução explícita. |
| `docker-compose.yml` (serviços backend/frontend) | Alinhado ao stack; o desalinhamento está no serviço `database` (Postgres), não em backend/frontend. |

Nenhuma reescrita é necessária — os problemas de complexidade estão concentrados em **documentação e governança de processo**, não em código.

---

## 5. Legacy / Deprecated

**Situação atual:** não existe separação física. `construction/` funciona *de fato* como camada legada (o próprio `CLAUDE.md` e `minimal-ssot.md` dizem isso em texto), mas estrutural e visualmente ocupa o mesmo nível hierárquico que `docs/` e `specs/` — sem sinal físico de que é histórico até a leitura da tabela de precedência.

**Política mínima recomendada (não implementada agora):**
1. Um único critério: *"se um artefato só serve para reconstituir decisões já tomadas, ele é Archive; se ainda orienta trabalho futuro, é SSOT."*
2. Sinal físico, não só declarativo — Archive deveria estar em um local que nenhum fluxo de leitura padrão (`CLAUDE.md` → `specs/foundation/` → feature) atravessa por acidente.
3. Nenhuma automação de agente deve precisar ler `construction/` ou `.cursor/` para completar o fluxo diário — isso já é regra escrita, falta ser regra estrutural.

---

## 6. Preparação para GitLab/Jira

**Realidade atual:** remote Git é GitHub (`github-personal:vicentetfreitas/portal-de-comunicacao`); `docs/technology/01-technology-stack.md` aprova formalmente "Repositório: GitHub" e "CI/CD: GitHub Actions"; nenhum vestígio de GitLab ou Jira encontrado em nenhum artefato do repositório.

### Pré-requisitos estruturais (GitLab)
- Decisão explícita: migrar hospedagem para GitLab, ou manter GitHub e preparar apenas padrões compatíveis (branch naming, MR/PR templates, quality gates)?
- CI backend hoje não existe — antes de portar pipelines para GitLab CI, o pipeline mínimo (`mvn clean verify` + testes) precisa existir em algum lugar primeiro.
- `docker-compose.yml` precisa refletir Oracle (não Postgres) antes de qualquer pipeline de build/test confiável.

### Pré-requisitos estruturais (Jira)
- Nenhum artefato hoje carrega identificador externo de rastreabilidade (Epic/Story/Task/Bug). `feature.yaml` tem `FT-<DOMAIN>` como identidade interna — decidir se esse código vira o vínculo com Jira ou se um novo campo é necessário.
- O fluxo `DRAFT → READY_FOR_REVIEW → APPROVED → IMPLEMENTING → DONE` em `feature.yaml` já é um estado-máquina utilizável como espelho de status Jira — não precisa ser reinventado, só mapeado.

Nenhuma implementação foi feita nesta fase.

---

## 7. Contexto do Claude

**O que já está bom:** `CLAUDE.md` na raiz é enxuto e já aponta corretamente para `specs/foundation/minimal-ssot.md`, e já proíbe carregar `.cursor/` e tratar `construction/` como SSOT. O ponto de entrada não é o problema.

**O que consome contexto desnecessariamente hoje:**
- `specs/foundation/` tem 13 arquivos fortemente interligados; entender precedência frequentemente exige ler `minimal-ssot.md` + `development-workflow.md` + `framework-simplification-scope.md` juntos.
- `agent-commands.md` (367 linhas) carrega inline ~140 linhas de catálogo histórico (Apêndices A/B/C) que a tarefa cotidiana nunca precisa.
- `docs/governance/07-documentation-architecture.md` (259 linhas) é necessário só quando a tarefa é sobre criar/mover documentação — hoje não há sinal de que é opcional.

**Direção recomendada (sem implementar):**
- Global (sempre carregado): `CLAUDE.md` + `path-conventions.md`.
- Sob demanda por natureza da tarefa: `minimal-ssot.md` (conflito de precedência), `development-workflow.md` (dúvida de fluxo de estado).
- Nunca carregado por padrão: `construction/`, `engineering/`, `.cursor/`, Apêndices históricos — mover para fora do caminho de leitura natural, não só declarar "não usar".

---

## 8. Decisões Pendentes

| # | Decisão | Por que bloqueia |
|---|---|---|
| 1 | **GitLab vs. GitHub**: migrar hospedagem, ou preparar padrões agnósticos mantendo GitHub? | A stack aprovada hoje diz GitHub. Qualquer trabalho de "preparação GitLab" precisa saber se é migração real ou portabilidade. |
| 2 | **`docs/architecture/11-target-repository-structure.md`**: obsoleto (concepção anterior Next.js/CMS/Postgres) e deve virar Archive, ou ainda representa roadmap futuro válido? | Está classificado como SSOT hoje mas contradiz a stack aprovada e vigente. Não pode ser reclassificado sem confirmação. |
| 3 | **`specs/domain/` (content-model/publication-model)**: bounded context de conteúdo ainda ativo, ou resíduo ligado ao item 2? | Mesma raiz de incerteza — decide se é uma segunda especificação de domínio legítima ou lixo histórico. |
| 4 | **Unificação de `minimal-ssot.md` × `07-documentation-architecture.md`**: continuam complementares, ou fundir em um único mapa de precedência? | Ambos se autodeclaram parcialmente "SSOT do SSOT"; unificar é mudança de governança que precisa de aprovação explícita. |
| 5 | **Escopo do `construction/`**: arquivar fisicamente agora ou manter até `FT-COLABORADOR` e `FT-PRIMEIRO-ACESSO` (em transição) fecharem? | Mover fisicamente antes do fechamento pode quebrar referências ativas dessas duas features em andamento. |

---

## 9. Proposta de Arquitetura Documental Mínima (hipotética — não implementada)

```text
portal-de-comunicacao/
├── CLAUDE.md                         ← ponto de entrada único
├── docs/
│   ├── domain/                       ← regras de negócio (SSOT)
│   ├── architecture/                 ← ADRs + arquitetura alvo (um catálogo de decisão, não três)
│   └── implementation/               ← padrões de código
├── specs/
│   ├── foundation/                   ← 1 doc de precedência + DoR/DoD/paths (fusão dos 13 atuais em menos artefatos)
│   └── features/<slug>/              ← inalterado — já funciona bem
├── database/                         ← inalterado — SSOT de schema, já o mais limpo do repositório
└── archive/                          ← construction/ + engineering/ + .cursor/archive/ + docs/audit histórico,
                                          fisicamente fora do caminho de leitura padrão de qualquer agente
```

Ideia central: um único catálogo de decisões, um único mapa de precedência, e um diretório de arquivo físico — não três camadas de governança paralelas coexistindo com uma quarta camada "simplificada" por cima.

---

## 10. Próxima Menor Etapa

**Extrair os Apêndices A/B/C (catálogo histórico v1.0/v1.1) de `specs/foundation/agent-commands.md` para `construction/history/`.**

- Pequena: um único arquivo, ~140 linhas movidas, nenhuma lógica nova.
- Reversível: `git mv` + ajuste de referência; desfazer é um `git revert`.
- Executável em uma sessão: não depende de nenhuma das 5 decisões pendentes da Seção 8.
- Serve diretamente o objetivo estratégico: reduz o tamanho do documento mais consultado no fluxo diário (`agent-commands.md`), sem tocar em produto, código ou precedência.

---

## Nota sobre localização deste artefato

Este documento foi colocado em `docs/audit/` por ser o local já declarado no próprio `docs/governance/07-documentation-architecture.md` para conteúdo de categoria Evidence/auditoria (`docs/audit/*`), e por já existir um precedente direto de escopo equivalente nesse mesmo diretório (`docs/audit/repository-readiness-review-v1.0.0.md`). A numeração `12-` segue a sequência existente (01–11) sem criar convenção nova. Nenhum outro diretório do repositório foi criado ou alterado para acomodar este artefato.
