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

Objetivo: transições de status disparadas por commit/MR, sem trabalho manual.

### 8.1 Convenção de referência (smart commits)

- **Branch:** `feature/<slug>` (já é a convenção — `10-project-organization.md` §Convenção Git).
- **Commit:** rodapé `Refs PUC-XXX` em todo commit ligado a uma feature com Story.
- **Transição por commit** (quando o app estiver ativo): `PUC-XXX #em-andamento` / `PUC-XXX #concluido` na mensagem (nomes reais das transições confirmados na ativação).
- Commits de plataforma/governança sem Story: sem rodapé `Refs` (lacuna conhecida — `10-project-organization.md` pendência de escopo Git).

### 8.2 Integração de plataforma

| Repositório | App | Observação |
|---|---|---|
| `portal-de-comunicacao` (monorepo — `specs/`, `docs/`, `database/`) | **GitHub for Jira** | fonte da verdade documental; onde vivem as specs |
| `portal-comunicacao-api`, `portal-comunicacao-app` (GitLab corporativo — DEC-015) | **GitLab for Jira Cloud** | código dividido |
| `portal-comunicacao-cms` (GitLab — DEC-015 / DEC-CMS-002) | **GitLab for Jira Cloud** | quando houver trabalho de CMS rastreado |

### 8.3 Limites

- Instalação de app no Marketplace, autorização OAuth e mapeamento de projeto **exigem ação humana** com permissão de admin no Jira e nos repositórios. Não é executável via CLI/MCP. Checklist em §9.
- A automação cobre **transição de status e link commit↔issue**. Não cria Stories nem sincroniza descrição — isso continua sendo a disciplina da §6 e o mapa da §11.

---

## 9. Procedimento de ativação

### 9.1 Ações humanas (admin Jira / repositórios)

1. **Estados do board `PUC`:** já OK — board 106 tem "Itens Pendentes" / "Em andamento" / "Concluídos". Nenhuma ação.
2. **GitHub for Jira:** instalar o app (`https://github.com/marketplace/jira-software-github`), autorizar a org/repo `portal-de-comunicacao`, mapear ao site `unimedceara.atlassian.net`.
3. **GitLab for Jira Cloud:** em cada repo GitLab corporativo, Settings → Integrations → "GitLab for Jira Cloud app" (ou instalar via Jira). Mapear ao mesmo site.
4. **Proteção de branch / smart-commit:** habilitar smart commits no Jira (Settings → Products → DVCS / GitHub → "Enable smart commits").
5. Confirmar que o usuário de serviço tem permissão de transição no projeto `PUC`.

### 9.2 Ações do agente (Claude Code, via MCP — com aprovação)

1. Criar os 7 Épicos (`EPIC-001…007`) — §11.
2. Criar as Stories (`FEATURE-0XX`) em lotes por Épico, status conforme §4.
3. Relacionar as issues legadas válidas (PUC-1..217) às novas Stories; aplicar label `legado-discovery-2025`; fechar duplicatas/obsoletas — **em lote, com aprovação humana explícita** (auditoria §6).
4. Corrigir PUC-22 e PUC-43 (status "Concluído" incorreto — auditoria §6.3).
5. Preencher a §11 com as chaves `PUC-XXX` geradas.

---

## 10. Estado da migração (registro vivo)

| Fase | Descrição | Estado | Data |
|---|---|---|---|
| 0 | Este documento (regra, modelo, mapa, automação) | **Concluída** | 2026-08-28 |
| 1 | 7 Épicos criados no Jira (PUC-218…PUC-224) | **Concluída** | 2026-08-28 |
| 2 | 44 Stories criadas (PUC-225…PUC-268) e vinculadas aos Épicos; status inicial aplicado | **Concluída** | 2026-08-28 |
| 3 | Migração/encerramento das 217 issues legadas | Pendente (requer aprovação de lote) | — |
| 4 | Automação Git↔Jira ativada | Pendente (requer ação humana — §9.1) | — |

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
| PUC-244 | FEATURE-024 | Gestão de Permissões de Pastas | `specs/features/arquivos/` + `documento-gestao/` | Em andamento — leitura DONE; edição IMPLEMENTING | Em andamento |
| PUC-245 | FEATURE-025 | Auditoria | — (`AuditableEntity`) | Parcial | A fazer |
| PUC-246 | FEATURE-026 | Gestão de Solicitações de Permissão | — | Não iniciado (OQ-003) | A fazer · `pos-mvp` |
| PUC-247 | FEATURE-027 | Gestão de Perfis Externos (convidado/parceiro) | — | Não iniciado (OQ-002) | A fazer · `pos-mvp` |
| PUC-248 | FEATURE-028 | Apresentação de Autenticação | `specs/features/authentication/` | closed | Concluído |
| PUC-249 | FEATURE-029 | Administração de Permissões (UI) | — | Não iniciado | A fazer |

### EPIC-004 — Gestão Documental · Etapa 3 · **PUC-221**

| PUC | FEATURE | Nome | Spec detalhada | Estado real | Status Jira |
|---|---|---|---|---|---|
| PUC-250 | FEATURE-030 | Gestão de Documentos | `specs/features/arquivos/` (FT-DOCUMENTO) + `documento-gestao/` | Em andamento — consulta/download DONE; gestão IMPLEMENTING | Em andamento |
| PUC-251 | FEATURE-031 | Gestão de Pastas | `specs/features/documento-navegacao/` (DRAFT) | Em andamento — `PastaController`; navegação hierárquica DRAFT | Em andamento |
| PUC-252 | FEATURE-032 | Gestão de Visibilidade | `specs/features/arquivos/` (`PERMISSAO_PASTA`) | Parcial | A fazer |
| PUC-253 | FEATURE-033 | Gestão de Compartilhamento | — | Não iniciado (OQ-005) | A fazer |
| PUC-254 | FEATURE-034 | Gestão de Armazenamento (upload/download/quota) | `specs/features/documento-upload/` (IMPLEMENTING) | Em andamento — download DONE; upload IMPLEMENTING; quota não | Em andamento |
| PUC-255 | FEATURE-035 | Busca Documental | — | Não iniciado | A fazer |
| PUC-256 | FEATURE-036 | Apresentação Documental | `specs/features/arquivos/` + `documento-navegacao/` | Em andamento — listagem+download DONE | Em andamento |
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

## 12. Histórico

| Data | Autor | Alteração |
|---|---|---|
| 2026-08-28 | Governança | Criação. Ativa o D9. Define modelo canônico, mapa de status, disciplina de sync e plano de automação. Fase 0 concluída; Fases 1–4 pendentes. |
