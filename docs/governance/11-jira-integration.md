# Integração Jira ↔ Documentação

| Item | Valor |
|------|-------|
| Artefato | `docs/governance/11-jira-integration.md` |
| Status | Approved |
| Categoria documental | **SSOT** (da relação Jira ↔ projeto; **não** de requisitos, decisões ou regras) |
| Camada dona | Governança |
| Responsável | Project Manager / Arquitetura |
| Data | 2026-08-28 |
| Complementa | `10-project-organization.md`, `06-traceability-rules.md`, `docs/backlog/01-epics.md`, `docs/backlog/02-features.md`, `specs/foundation/development-workflow.md` |
| Decisão de origem | Ativação do **D9** (`docs/governance/structural-simplification-plan-w2.md`) — "mapeamento Jira ↔ `feature.yaml`", que estava adiado. Abordagem definida por decisão humana em 2026-08-28 (ver §10). |

---

## 1. Propósito

Estabelecer **como o Jira do projeto (`PUC` — "Portal Unimed Ceará") se relaciona com a documentação**, de forma que:

- o Jira volte a refletir o estado real do projeto;
- a documentação (`specs/`, `docs/`) permaneça a **única fonte de verdade**;
- a sincronização seja barata e, onde possível, automática.

Este documento **não** cria uma camada nova. A camada `JIRA/SCRUM` já existe em `10-project-organization.md` ("acompanhamento e cadência — não é SSOT"). Aqui apenas se **ativa e operacionaliza** essa camada.

O diagnóstico que motivou este trabalho está em `docs/jira-reconciliation-audit.md` (auditoria somente-leitura, 2026-08-27).

---

## 2. Princípio inegociável — quem é a verdade

```text
specs/  >  docs/  >  código  >  Jira
```

- **Jira é projeção.** Nunca guarda requisito funcional, regra de negócio, decisão, contrato de API ou critério de aceite normativo. Esses vivem em `specs/features/<slug>/` e `docs/`.
- Um card do Jira **aponta** para o artefato-fonte; não o copia.
- Em qualquer divergência, o Jira está errado por definição. A correção é no Jira, não na documentação.
- Estado oficial de uma feature continua em `specs/features/<slug>/feature.yaml`. O Jira **reflete** esse estado; não o define.

---

## 3. Modelo canônico

O Jira espelha a hierarquia **produto → épico → feature → trabalho** que já existe na documentação:

| Nível Jira | Espelha | Fonte |
|---|---|---|
| **Epic** | `EPIC-001` … `EPIC-007` | `docs/backlog/01-epics.md` |
| **Story** = tipo **"Tarefa"** (o projeto `PUC` é team-managed e só tem os tipos Epic / Tarefa / Subtarefa — não há tipo "Story" nem "Bug") | `FEATURE-0XX` | `docs/backlog/02-features.md` |
| Vínculo de detalhe | `specs/features/<slug>/` | quando a feature tem spec detalhada |
| **Sub-tarefa** | divisão de execução de uma Story | opcional |
| **Bug** = "Tarefa" + label `bug` | defeito | sem Epic guarda-chuva |

### Regras de modelagem

1. **1 Story por `FEATURE-0XX`.** Não criar Story por arquivo, classe, endpoint ou task de `tasks.md`.
2. **Descrição da Story** = 3 linhas fixas:
   - `Feature canônica: FEATURE-0XX — <nome>` (link para `docs/backlog/02-features.md`)
   - `Spec detalhada: specs/features/<slug>/` (ou "sem spec detalhada")
   - `Estado (feature.yaml): <status>` + 1 frase de escopo
3. **Proibido replicar `acceptance-tests.md` como sub-tarefas.** Foi a causa-raiz do "aceite nunca fechado" no board legado (auditoria §3.2). Critério de aceite é normativo e vive na spec.
4. **Sub-tarefa** só quando ajuda a paralelizar execução dentro de uma Story. É descartável.
5. **Bug** = "Tarefa" com label `bug` + link "Relates" para a Story afetada. O "Epic Bug" (PUC-49) do board legado é um antipadrão — não repetir.
6. Trabalho transversal sem `FEATURE-0XX` (ex.: reestruturação documental, DEC-015, CI) entra como Story sob o Epic mais próximo (`EPIC-001` na maioria dos casos) com `Feature canônica: — (trabalho de plataforma/governança)`.

---

## 4. Mapa de status

| `feature.yaml` / evidência | Status Jira |
|---|---|
| `DRAFT`, `READY_FOR_REVIEW`, `APPROVED` (spec pronta, sem implementação) | **A fazer** |
| `IMPLEMENTING` | **Em andamento** |
| `DONE` / feature `closed` | **Concluído** |
| sem spec, mas há evidência de código + teste no repositório | **Concluído** |
| sem spec, planejado em `docs/backlog/` | **A fazer** |
| feature marcada pós-MVP / opcional (`04-mvp-scope.md`) | **A fazer** + label `pos-mvp` |

> **Nota operacional:** o board `PUC` (board 106) já tem os 3 estados — "Itens Pendentes" / "Em andamento" / "Concluídos". Nenhum board novo é necessário; um único board filtrado/agrupado por Epic é suficiente. As issues do board legado (auditoria) usavam só 2 estados porque nunca foram movidas para "Em andamento".

---

## 5. Onde vive o vínculo

1. **SSOT do mapeamento** = a tabela da §11 deste documento (`EPIC-0XX` / `FEATURE-0XX` ↔ chave `PUC-XXX`).
2. **Proposto (não aplicado nesta rodada):** campo opcional `jira: PUC-XXX` em cada `feature.yaml`. Exige evoluir o schema em `specs/foundation/feature-yaml.md` via `/specify` — fica registrado como follow-up.
3. Enquanto o campo não existe, a §11 é a referência única. Não espalhar o número do PUC por outros artefatos.

---

## 6. Disciplina de sincronização (manual)

Obrigatória enquanto a automação (§8) não estiver ativa, e como rede de segurança depois.

| Gatilho | Ação |
|---|---|
| `feature.yaml` promovido (`APPROVED → IMPLEMENTING`, `IMPLEMENTING → DONE`, etc.) | Transicionar a Story `PUC-XXX` correspondente **na mesma mudança** (mesmo commit lógico / mesma sessão). |
| Feature nova criada em `specs/features/<slug>/` | Criar/associar a Story sob o Epic correto; atualizar §11. |
| Fechamento de feature via Contrato REVIEW (sem PR) | Incluir a transição da Story no checklist de fechamento. |
| Checkpoint do State Index (`01-project-status.md`) | Conferir divergências Jira × State Index; registrar como pendência se houver. |

**Adendo proposto ao DoD** (`specs/foundation/definition-of-done.md`, §"Antes de transitar para DONE"): acrescentar item *"Story Jira da feature transicionada para Concluído (ou N/A registrado)"*. Não aplicado nesta rodada — requer edição do artefato de foundation.

---

## 7. O que **não** fazer

- Não abrir card para pedir status ("qual o andamento de X") — o State Index responde.
- Não usar o Jira como backlog paralelo que diverge de `docs/backlog/`.
- Não fechar Story sem evidência (`feature.yaml: DONE` ou código+teste).
- Não recriar a explosão de sub-tarefas de aceite.
- Não editar `docs/`/`specs/` para "bater" com o Jira.

---

## 8. Automação Git ↔ Jira

Objetivo: link commit/MR ↔ issue e transição de status disparados por commit/MR.

### 8.0 Topologia (o que a automação cobre e o que não cobre)

| Onde | Repositório | Host | Cobre automação? |
|---|---|---|---|
| **Código backend** | `portal-comunicacao-api` | `gitlab.unimedceara.com.br:unimedceara/portal-comunicacao/` | ✅ GitLab → Jira |
| **Código frontend** | `portal-comunicacao-app` | idem | ✅ GitLab → Jira |
| **Código CMS** | `portal-comunicacao-cms` | idem | ✅ (quando houver trabalho) |
| **`specs/` + `docs/` + `feature.yaml` + `database/`** | `portal-de-comunicacao` (monorepo) | GitHub pessoal (`vicentetfreitas/`) | ❌ — fora do GitLab |

**Consequência:** a **flip autoritativa de status** (`feature.yaml` → `DONE`) acontece no monorepo, que o GitLab não vê. A automação GitLab move a Story quando o dev **empurra código** (`#em-andamento`, e `Closes` no MR), mas o fechamento formal (`DONE`) continua dependendo da **disciplina manual da §6** — ou, no futuro, de um *GitHub for Jira* no monorepo (§8.4).

### 8.1 Convenção de referência

- **Branch:** `feature/<slug>` (convenção atual). Opcional para auto-associação: `feature/PUC-XXX-<slug>`.
- **Commit:** rodapé `Refs PUC-XXX` em todo commit de código ligado a uma Story. (O rodapé `Co-Authored-By` de commits do agente permanece.)
- **Merge Request:** `PUC-XXX` no título **ou** descrição → cross-link + comentário na issue. Acrescentar **`Closes PUC-XXX`** na descrição do MR que conclui a parte de código da feature → dispara a transição configurada no merge.
- Commits de plataforma/governança sem Story: sem rodapé `Refs`.

### 8.2 Integração GitLab (self-managed) → Jira Cloud — método recomendado

O GitLab corporativo é **self-managed** (`gitlab.unimedceara.com.br`); o Jira é **Cloud** (`unimedceara.atlassian.net`). O caminho mais simples e robusto é a **integração Jira nativa do GitLab**, configurada **no grupo** `unimedceara/portal-comunicacao` (cascateia para os 3 projetos):

**GitLab → grupo `portal-comunicacao` → Settings → Integrations → Jira:**

| Campo | Valor |
|---|---|
| Web URL | `https://unimedceara.atlassian.net` |
| Jira API URL | *(vazio — igual à Web URL, correto p/ Cloud)* |
| Email or username | e-mail de uma conta Jira **de serviço** (licenciada, com permissão de transição/comentário no projeto `PUC`) |
| API token / Password | token gerado em `https://id.atlassian.com/manage-profile/security/api-tokens` |
| Jira project key | `PUC` |
| Enable comments | ✅ |
| Enable Jira transitions | ✅ → **Custom transitions**: `31` (mapeado para "Concluído" no workflow do board 106; `11`=Itens Pendentes, `21`=Em andamento) |
| Trigger on | Commit **e** Merge request |

Após salvar: **Test settings**. Um commit com `PUC-254` passa a criar comentário + cross-link na issue; um MR com `Closes PUC-254` transiciona a Story para Concluído no merge.

### 8.3 Opção avançada — painel de desenvolvimento (GitLab for Jira Cloud app)

Para ver branches/commits/MRs/deploys **dentro** da issue do Jira (painel "Development"), instalar adicionalmente o app **GitLab for Jira Cloud** para instância self-managed:
- Requer **admin da instância GitLab**: *Admin → Settings → General → "GitLab for Jira App"* (gera a configuração de conexão).
- Instalar o app **GitLab for Jira Cloud** a partir do Atlassian Marketplace no site `unimedceara.atlassian.net` e vincular à instância.
- Exige que o GitLab alcance `*.atlassian.net` / `api.atlassian.com` por HTTPS de saída.
- Só fazer se o time quiser o painel; a §8.2 já entrega link + transição.

### 8.4 Monorepo (docs/specs) — sem automação por ora

`specs/`/`docs/`/`feature.yaml` estão em GitHub pessoal. Opções, em ordem de esforço: (a) manter a disciplina manual da §6 [**escolhido**]; (b) instalar *GitHub for Jira* nesse repositório; (c) espelhar o monorepo num projeto GitLab. Revisitar quando o destino definitivo da SSOT compartilhada for decidido (DEC-015 Ponto em Aberto 2).

### 8.5 Limites

- Toda a configuração de §8.2–8.3 **exige ação humana** (admin de grupo GitLab + conta/token Jira). Não é executável via CLI/MCP — não há `glab` nem token nesta sessão.
- A automação cobre **link e transição**. Não cria Stories, não sincroniza descrição, não fecha por `feature.yaml` — isso é a §6 e o mapa da §11.

---

## 9. Procedimento de ativação

### 9.1 Ações humanas — automação GitLab → Jira

**Pré-requisitos a reunir:**
1. Uma **conta Jira de serviço** licenciada (ex.: `automacao-gitlab@unimedceara...`), adicionada ao projeto `PUC` com permissão de *Transition Issues* e *Add Comments*. (Pode-se usar uma conta pessoal para testar, mas serviço é melhor.)
2. **API token** dessa conta: `https://id.atlassian.com/manage-profile/security/api-tokens` → *Create API token* → copiar.
3. Papel **Maintainer/Owner** no grupo GitLab `unimedceara/portal-comunicacao`.

**Configuração (GitLab):**
4. Grupo `unimedceara/portal-comunicacao` → **Settings → Integrations → Jira** → preencher conforme a tabela da §8.2 → **Save changes** → **Test settings**.
   *(Alternativa: configurar por projeto em cada um dos 3 repos, mesmos valores.)*
5. Confirmar que `gitlab.unimedceara.com.br` tem saída HTTPS para `unimedceara.atlassian.net` (necessário para a integração nativa).

**Validação:**
6. Num branch de teste em `portal-comunicacao-api`, fazer um commit com `Refs PUC-244` na mensagem e push → conferir comentário/cross-link em PUC-244.
7. Abrir um MR com `Closes PUC-244` na descrição, fazer merge → conferir se PUC-244 foi para *Concluído*.

**Board `PUC`:** já OK — board 106 tem os 3 estados. Nenhuma ação.

**Opcional (painel Development):** §8.3 — requer admin da instância GitLab.

### 9.2 Ações do agente (Claude Code, via MCP)

1. ✅ Criar os 7 Épicos (`EPIC-001…007`) — PUC-218…PUC-224.
2. ✅ Criar as 44 Stories (`FEATURE-0XX`) — PUC-225…PUC-268, com status inicial e labels.
3. ✅ Comentar os 8 Épicos legados com o mapeamento para a nova estrutura (§11b).
4. Preencher §11 / §11b com as chaves geradas.

> O encerramento em massa das 217 issues legadas **não** é feito via MCP — é operação de Bulk Change (§9.3). O classificador de segurança do agente bloqueia transições de estado em lote sobre issues pré-existentes, e a API não faz "adicionar label" em massa sem sobrescrever os labels atuais.

### 9.3 Bulk Change das issues legadas (ação humana no Jira)

1. **Issues → Buscar** com o JQL:
   `project = PUC AND created < "2026-08-28" AND labels != legado-discovery`
   (217 issues — as criadas antes da migração).
2. Selecionar tudo → **Bulk change** (edição em massa).
3. **Editar** → *Alterar rótulos* → **Adicionar** `legado-discovery` (não substituir — preserva `rbac`, `audit`, etc.).
4. **Transição** → *Concluído* (mesma seleção; ignora as que já estão concluídas).
5. Confirmar.
6. **Filtro do board 106:** adicionar `AND labels != legado-discovery` ao filtro do quadro, para o board mostrar só a estrutura nova. As legadas continuam acessíveis por `project = PUC AND labels = legado-discovery`.

---

## 10. Estado da migração (registro vivo)

| Fase | Descrição | Estado | Data |
|---|---|---|---|
| 0 | Este documento (regra, modelo, mapa, automação) | **Concluída** | 2026-08-28 |
| 1 | 7 Épicos criados no Jira (PUC-218…PUC-224) | **Concluída** | 2026-08-28 |
| 2 | 44 Stories criadas (PUC-225…PUC-268) e vinculadas aos Épicos; status inicial aplicado | **Concluída** | 2026-08-28 |
| 3 | Migração das issues legadas | **Parcial** — 8 Épicos legados comentados+mapeados (§11b); mapa completo legado→novo documentado. Falta o Bulk Change (label + encerrar + filtro do board) — §9.3, ação humana | 2026-08-28 |
| 4 | Automação GitLab → Jira | Pendente — procedimento self-managed + Jira Cloud especificado (§8.2, §9.1); requer conta/token Jira de serviço + Maintainer no grupo GitLab. Monorepo (docs) fica na disciplina manual §6 | — |

**Decisão humana de 2026-08-28 (abordagem):**
- Board legado: **estrutura nova + migrar só o válido** (não corrigir 217 in-place; marcar o restante como legado).
- Execução: **híbrida** — o agente cria a estrutura nova via MCP; fechamentos e edições em massa nas issues antigas só após aprovação de lote.
- Sincronização: **regra documentada (§6) + automação de integração (§8)**.
- Escopo da Fase 2: **MVP + pós-MVP**, as features fora do MVP com label `pos-mvp`.
- Board: manter o board 106 existente (3 estados já presentes); sem board novo.

---

## 11. Mapa canônico EPIC / FEATURE ↔ Jira

Todas as chaves `PUC` geradas nas Fases 1–2 (2026-08-28). Épicos: PUC-218…PUC-224. Stories: PUC-225…PUC-268. `Estado real` conforme `feature.yaml` + evidência de código.

### EPIC-001 — Fundação da Plataforma · Etapa 1 · **PUC-218**

| PUC | FEATURE | Nome | Spec detalhada | Estado real | Status Jira |
|---|---|---|---|---|---|
| PUC-225 | FEATURE-001 | Infraestrutura e Ambientes | — (`docker-compose.yml`, DEC-013/DEC-015) | Parcial — Oracle+MinIO; ambiente local Oracle em validação | Em andamento |
| PUC-226 | FEATURE-002 | Backend Bootstrap | — (Sprint 0) | Concluído | Concluído |
| PUC-227 | FEATURE-003 | Frontend Bootstrap | — (Foundation da aplicação) | Concluído | Concluído |
| PUC-228 | FEATURE-004 | Observabilidade Base | — | Parcial — Correlation ID + Actuator | Em andamento |
| PUC-229 | FEATURE-005 | Segurança Base | `specs/features/authentication/` (cookies/CSRF) | Parcial — cookies HttpOnly/Secure, CSRF, TLS | Em andamento |
| PUC-230 | _(plataforma)_ | Separação em repositórios + CI por repo (DEC-015 / D6) | `docs/technology/04-decision-log.md` §DEC-015 | Em andamento | Em andamento |
| PUC-231 | _(plataforma)_ | Persistência de preferência de tema (DEC-FA-005) | — | Concluído | Concluído |

### EPIC-002 — Organização Corporativa · Etapa 2 · **PUC-219**

| PUC | FEATURE | Nome | Spec detalhada | Estado real | Status Jira |
|---|---|---|---|---|---|
| PUC-232 | FEATURE-010 | Gestão de Singulares | `specs/features/singular/` (FT-SINGULAR) | closed | Concluído |
| PUC-233 | FEATURE-011 | Gestão de Áreas | `specs/features/area/` (FT-AREA) | closed | Concluído |
| PUC-234 | FEATURE-012 | Gestão de Equipes | `specs/features/equipe/` (FT-EQUIPE) | closed | Concluído |
| PUC-235 | FEATURE-013 | Gestão de Colaboradores | `specs/features/colaborador/` (FT-COLABORADOR) | DONE | Concluído |
| PUC-236 | FEATURE-014 | Gestão de Vínculos Organizacionais | `specs/features/colaborador/` + `session/` | Concluído | Concluído |
| PUC-237 | FEATURE-015 | Modelo Organizacional | `docs/domain/` + entidades JPA | Concluído | Concluído |
| PUC-238 | FEATURE-016 | Onboarding / Primeiro Acesso | `specs/features/primeiro-acesso/` (DONE) + `home/` (FT-HOME, DRAFT) | Wizard DONE; Home dinâmica DRAFT | Em andamento |
| PUC-239 | FEATURE-017 | Apresentação Organizacional | `specs/features/federacao-colaborador/` + `area-colaborador/` | DONE | Concluído |

### EPIC-003 — Controle de Acesso · Etapa 2 · **PUC-220**

| PUC | FEATURE | Nome | Spec detalhada | Estado real | Status Jira |
|---|---|---|---|---|---|
| PUC-240 | FEATURE-020 | Autenticação Corporativa (Zimbra) | `specs/features/authentication/` (FT-AUTH) | closed | Concluído |
| PUC-241 | FEATURE-021 | Gestão de Sessão | `specs/features/session/` (FT-SESSION) | closed | Concluído |
| PUC-242 | FEATURE-022 | Gestão de Papéis | — (`PapelAtribuicaoService`) | Parcial — mecanismo escopado no BE; sem UI | Em andamento |
| PUC-243 | FEATURE-023 | Autorização por Escopo | — (application-service `@PreAuthorize`) | Concluído (núcleo) | Concluído |
| PUC-244 | FEATURE-024 | Gestão de Permissões de Pastas | `specs/features/arquivos/` + `documento-gestao/` | Enforcement multi-nível DONE (leitura+escrita); administração = D-03/DBA (fora) | Concluído |
| PUC-245 | FEATURE-025 | Auditoria | — (`AuditableEntity`) | Parcial | A fazer |
| PUC-246 | FEATURE-026 | Gestão de Solicitações de Permissão | — | Não iniciado (OQ-003) | A fazer · `pos-mvp` |
| PUC-247 | FEATURE-027 | Gestão de Perfis Externos (convidado/parceiro) | — | Não iniciado (OQ-002) | A fazer · `pos-mvp` |
| PUC-248 | FEATURE-028 | Apresentação de Autenticação | `specs/features/authentication/` | closed | Concluído |
| PUC-249 | FEATURE-029 | Administração de Permissões (UI) | — | Não iniciado | A fazer |

### EPIC-004 — Gestão Documental · Etapa 3 · **PUC-221**

| PUC | FEATURE | Nome | Spec detalhada | Estado real | Status Jira |
|---|---|---|---|---|---|
| PUC-250 | FEATURE-030 | Gestão de Documentos | `specs/features/arquivos/` + `documento-gestao/` (ambos DONE 2026-08-28) | Concluído | Concluído |
| PUC-251 | FEATURE-031 | Gestão de Pastas | `specs/features/documento-navegacao/` (DONE 2026-08-28) | Concluído — explorador drill-in + árvore + busca | Concluído |
| PUC-252 | FEATURE-032 | Gestão de Visibilidade | `specs/features/arquivos/` (`PERMISSAO_PASTA`) | Parcial | A fazer |
| PUC-253 | FEATURE-033 | Gestão de Compartilhamento | — | Não iniciado (OQ-005) | A fazer |
| PUC-254 | FEATURE-034 | Gestão de Armazenamento (upload/download) | `specs/features/documento-upload/` (DONE 2026-08-28, MinIO provisionado) | Concluído — quota BR-023 fica p/ futuro | Concluído |
| PUC-255 | FEATURE-035 | Busca Documental | — | Não iniciado | A fazer |
| PUC-256 | FEATURE-036 | Apresentação Documental | `specs/features/arquivos/` + `documento-navegacao/` (DONE) | Concluído — explorador + grade/lista + upload UI | Concluído |
| PUC-257 | FEATURE-037 | Modelo de Categoria Documental | — (`CategoriaDocumentalEntity`) | Concluído (modelo) | Concluído |
| PUC-258 | _(pós-MVP)_ | Deduplicação por hash · Conversão WebP | — (`docs/domain/09-business-rules.md`) | Não iniciado | A fazer · `pos-mvp` |

### EPIC-005 — Comunicação Interna · Etapa 4 · **PUC-222**

| PUC | FEATURE | Nome | Spec detalhada | Estado real | Status Jira |
|---|---|---|---|---|---|
| PUC-259 | FEATURE-040 | Gestão de Notificações | — | Não iniciado | A fazer |
| PUC-260 | FEATURE-041 | Gestão de Comunicados (OQ-004) | `specs/features/noticia/` (FT-NOTICIA, DRAFT) | Não iniciado — Comunicado = publicação WordPress | A fazer |
| PUC-261 | FEATURE-042 | Canal Fique por Dentro | `specs/features/noticia/` | Não iniciado | A fazer |
| PUC-262 | FEATURE-043 | Busca Unificada | — (ADR-014) | Não iniciado | A fazer · `pos-mvp` |
| PUC-263 | FEATURE-049 | Apresentação de Comunicação | — | Não iniciado | A fazer |
| PUC-264 | FEATURE-044/045/047/048 | Métricas / Central de Colaboração / Segmentação / Integrações de canal | — | Não iniciado | A fazer · `pos-mvp` |

### EPIC-006 — Migração Operacional · Etapa 5 · **PUC-223**

| PUC | FEATURE | Nome | Spec detalhada | Estado real | Status Jira |
|---|---|---|---|---|---|
| PUC-265 | FEATURE-050 | Migração de Dados | `docs/solution-design/09-migration-strategy.md` | Não iniciado — ambiente local Oracle em validação | A fazer |
| PUC-266 | FEATURE-051 | Validação e Reconciliação | idem | Não iniciado | A fazer |
| PUC-267 | FEATURE-052 | Migração de Integrações | idem | Não iniciado | A fazer |

### EPIC-007 — Descomissionamento · Etapa 6 · pós-MVP · **PUC-224**

| PUC | FEATURE | Nome | Estado real | Status Jira |
|---|---|---|---|---|
| PUC-268 | FEATURE-060+ | Remoção Backend PHP / Desativação API CMS de negócio | Não iniciado | A fazer · `pos-mvp` |

### Sem correspondência canônica

| `specs/features/` | Observação |
|---|---|
| `servicos/` (FT-SERVICOS) | DRAFT / inerte — sem `FEATURE-0XX`. Não criar Story até haver decisão de produto. |
| `federacao/` (stub) | Superseded por `federacao-colaborador/` — sem Story própria. |

---

## 11b. Mapa de migração — issues legadas → nova estrutura

Todas as issues legadas (`id < 20180`, criadas até 2026-08-27) recebem label `legado-discovery` e são encerradas (`Concluído`) — via **Bulk Change** do Jira (§9.3). Não são mais backlog ativo; ficam consultáveis por JQL `labels = legado-discovery`.

### Épicos legados

| Legado | Título | → Substituído por |
|---|---|---|
| PUC-16 | Autenticação & Acesso | **PUC-220** (EPIC-003) + PUC-238 (primeiro acesso) |
| PUC-17 | Administração | **PUC-219** (EPIC-002) + itens RBAC em PUC-220 |
| PUC-23 | Documentos & Mídias | **PUC-221** (EPIC-004) |
| PUC-29 | Login & Navegabilidade | **PUC-218** (EPIC-001) — PUC-227, PUC-248 |
| PUC-33 | Observabilidade & Governança | **PUC-218** (EPIC-001) — PUC-228, PUC-230 |
| PUC-34 | CMS/WordPress | **PUC-224** (EPIC-007) / PUC-268; filhas PUC-40/41/42 obsoletas |
| PUC-35 | Área do Colaborador | **PUC-239** (FEATURE-017) sob PUC-219 |
| PUC-49 | Bug (tipo Epic) | sem substituto — bug agora é "Tarefa" + label `bug` |

### Tarefas legadas

| Legado | → Story nova | Nota |
|---|---|---|
| PUC-1 Login corporativo | PUC-240 | "seleção de domínio" não implementada; decisão de produto pendente |
| PUC-6 Redirecionamento por papel | PUC-243 / PUC-241 | |
| PUC-7 Papel padrão primeiro acesso | PUC-238 | |
| PUC-8 Hierarquia de concessão de papéis | PUC-242 | consolidada com PUC-59/65 |
| PUC-12 Esqueci minha senha | PUC-240 | **obsoleta** — recuperação é responsabilidade do Zimbra (fora de escopo FT-AUTH) |
| PUC-15 CRUD de Colaborador | PUC-235 | card canônico de Colaborador |
| PUC-18 CRUD de Singulares | PUC-232 | |
| PUC-19 CRUD de Áreas e Colaboradores | PUC-233 + PUC-235 | |
| PUC-20 Painel do Administrador | PUC-227 / PUC-239 | |
| PUC-21 Relatórios/Dashboard | PUC-264 | pós-MVP |
| PUC-22 Upload com escopo | PUC-254 | status "Concluído" era **incorreto** — upload entregue depois |
| PUC-24 Organização por singular/área/colab | PUC-244 | |
| PUC-25 Deduplicação via hash | PUC-258 | pós-MVP |
| PUC-26 Auditoria de documentos | PUC-245 | |
| PUC-27 Conversão WebP | PUC-258 | pós-MVP |
| PUC-28 Painel de controle de documentos | PUC-256 | |
| PUC-30 Campo e-mail com seleção de domínio | PUC-248 | **obsoleta** — sem spec nem decisão de produto |
| PUC-31 Responsividade/acessibilidade login | PUC-248 | |
| PUC-32 Navegação consistente | PUC-227 | já entregue |
| PUC-36 Coleção Postman | PUC-228 | |
| PUC-37 Limpeza e padronização de estrutura | PUC-230 | amplamente executado (W0–W2) |
| PUC-38 Logs por serviço e health checks | PUC-228 | |
| PUC-39 Consolidação de portas e redes | PUC-225 | |
| PUC-40 / PUC-41 / PUC-42 (CMS legado) | PUC-268 | **obsoletas** — assumiam WP no monorepo |
| PUC-43 Compartilhar arquivos | PUC-250 / PUC-253 | status "Concluído" era **incorreto** — colaborador lê/baixa, não compartilha |
| PUC-44 Editar perfil do colaborador | — (FT-PERFIL, DRAFT) | Story a criar quando a spec sair de DRAFT |
| PUC-50 Redirecionamento indevido /singular | PUC-243 | bug corrigido |
| PUC-51 Refatorar página raiz (DS) | PUC-227 | |
| PUC-59 Permissões (RBAC): gerenciar | PUC-242 + PUC-249 | |
| PUC-65 Usuários: gerenciar acesso | PUC-235 + PUC-249 | |
| PUC-71 Singulares: gerenciar cadastro | PUC-232 | **duplicata de PUC-18** |
| PUC-77 CRUD de Áreas | PUC-233 | **duplicata de PUC-19** |
| PUC-83 Colaboradores: gerenciar vínculos | PUC-235 | já entregue |
| PUC-89 Colaboradores: filtro de busca | PUC-235 | |
| PUC-95 Colaboradores: listar resultados | PUC-235 | |
| PUC-101 Colaboradores: editar vínculos | PUC-235 | já entregue |
| PUC-206 Papéis do sistema | PUC-242 | é definição, não trabalho |
| PUC-211 RBAC baseline: matriz | PUC-242 | |
| PUC-217 Revisão de Requisitos | — | atividade de discovery encerrada; requisitos vivem em `specs/` |

168 sub-tarefas legadas seguem os pais: encerradas junto no Bulk Change.

---

## 12. Histórico

| Data | Autor | Alteração |
|---|---|---|
| 2026-08-28 | Governança | Criação. Ativa o D9. Define modelo canônico, mapa de status, disciplina de sync e plano de automação. |
| 2026-08-28 | Reconciliação Jira | Fases 1–2: 7 Épicos (PUC-218…224) + 44 Stories (PUC-225…268) criados e vinculados. Fase 3 parcial: Épicos legados comentados; mapa §11b. Fase 3 (Bulk Change) e Fase 4 (automação) pendem de ação humana. |
