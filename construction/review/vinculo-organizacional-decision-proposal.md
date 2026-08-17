# Proposta de decisão — modelo de vínculo organizacional único do COLABORADOR

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/vinculo-organizacional-decision-proposal.md` |
| Data | 2026-08-14 |
| Tipo | **Proposta consolidada** — DH-02/03/04 aprovadas |
| Status | **CONSOLIDADA** — DEC-DB-028 **APPROVED** (2026-08-14) |
| Decisão alvo | DEC-DB-028 |

**Classificação usada:** `FATO` · `GAP` · `CONFLICT` · `RISCO` · `RECOMENDAÇÃO` · `DECISÃO HUMANA NECESSÁRIA`.

**Restrições:** decisões DH-02/03/04 e DEC-DB-028 **aprovadas** em 2026-08-14. Implementação, DDL e código **não** alterados nesta etapa.

---

## 0. Estado pós-decisão (2026-08-14)

| Decisão | Status |
|---------|--------|
| DH-02 — 1 vínculo por COLABORADOR | **APROVADA** |
| DH-03 — Alternativa A (persistir após vínculo completo) | **APROVADA** |
| DH-04 — Fed+Sing+Área obrigatórios; Equipe opcional | **APROVADA** |
| DEC-ORG-003 — domínio e-mail → Singular | **APROVADA** |
| DEC-DB-028 | **APPROVED** — `database/model/05-decisions-and-risks.md` |
| Sessão pré-COLABORADOR | **DH-PA-01 aprovada** (2026-08-15) — implementação pendente |
| Domínio e-mail → Singular (cardinalidade e comportamento) | **DH-PA-02 aprovada** (2026-08-15) — implementação física pendente (GAP-028-04) |
| `locateOrCreate` AS-IS | **GAP** — não normativo; correção em etapa futura |

## 1. Resumo executivo

Esta proposta consolidou o texto da **DEC-DB-028** (*"Modelo de vínculo organizacional único do COLABORADOR"*), **aprovada** em 2026-08-14 após decisões humanas DH-02, DH-03 e DH-04.

**Premissa normativa vigente:** cada colaborador possui **exatamente um** vínculo, representado pelas FKs em `COLABORADOR`, com Federação, Singular e Área obrigatórias e Equipe opcional. CARGO e PAPEL permanecem eixos independentes.

**Respostas consolidadas (decisão tomada):**

| # | Pergunta | Resposta |
|---|----------|-------------------|
| 1 | COLABORADOR possui exatamente 1 vínculo? | **Sim** — proposta central da DEC-DB-028 |
| 2 | Vínculo nas FKs de COLABORADOR? | **Sim** — manter modelo AS-IS estrutural |
| 3 | Criar VINCULO_ORGANIZACIONAL? | **Não** — desnecessário para 1:1 |
| 4–6 | Federação / Singular / Área obrigatórias? | **Sim** — `NOT NULL` (Singular e Área exigem endurecimento vs AS-IS) |
| 7 | Equipe opcional? | **Sim** — `COD_EQUIPE` permanece `NULL` permitido |
| 8 | Hierarquia Federação → Singular → Área → Equipe? | **Sim** — preservar; integridade ainda é **GAP** |
| 9–10 | Vínculo independente de CARGO e PAPEL? | **Sim** — alinhado a DEC-ORG-002, DEC-DB-020, DEC-DB-027 |
| 11 | PAPEL_ATRIBUICAO para 1..N papéis? | **Sim** — estrutura já existe |
| 12 | COLABORADOR como papel mínimo? | **Sim** — proposta; enforcement é **DECISÃO HUMANA NECESSÁRIA** |
| 13–14 | DEC-FA-003: o que supersede? | **Parcialmente** itens de N vínculos; **manter** Contexto Ativo e REF-DB-CTX-01 |

**Implementação:** AS-IS (`locateOrCreate`, DDL nullable) permanece até etapas futuras — ver GAP-028-* em DEC-DB-028.

---

## 2. Objetivo

Documentar a **proposta consolidada** que originou a **DEC-DB-028 APPROVED** (2026-08-14), preservando o histórico analítico e as referências às fontes consultadas.

---

## 3. Fontes analisadas

### Fontes primárias (obrigatórias)

| Fonte | Status | Uso |
|-------|--------|-----|
| `construction/review/vinculo-organizacional-reconciliation-cardinality-reassessment.md` | **Encontrada** | Reconciliação técnica AS-IS; Alternativa A vs B; matriz DEC-FA-003 |
| `docs/governance/03-open-decisions.md` | **Encontrada** | DEC-FA-003, DEC-ORG-002 (texto integral) |
| `database/model/05-decisions-and-risks.md` | **Encontrada** | DEC-DB-015, 016, 020, 027 (texto integral) |

### Fontes solicitadas (não localizadas)

| Fonte solicitada | Status |
|------------------|--------|
| `construction/review/vinculo-organizacional-reconciliation-pd-02-03.md` | **Não encontrada** no repositório |
| `construction/review/vinculo-organizacional-reconciliation-pd-02-03-complementar.md` | **Não encontrada** no repositório |

**FATO.** Referências a PD-02/PD-03 de vínculo aparecem em `database/model/05-decisions-and-risks.md` (DEC-DB-027, seção “Vínculo (operação)”) como pendências históricas encerradas no eixo cargo, mas os artefatos de reconciliação PD-02/03 **não estão versionados** nos paths indicados. Esta proposta **não** os inventa; deriva evidência da reconciliação de cardinalidade e das decisões vigentes.

### Fontes complementares (evidência AS-IS)

`database/ddl/003-create-tables.sql`, `004-create-constraints.sql`, `008-initial-data.sql`; `database/model/02-logical-model.md`, `03-physical-model.md`; `ColaboradorEntity.java`, `ColaboradorService.java`, `ColaboradorDomainService.java`, `AuthenticationService.java`; `frontend/src/stores/session.store.ts`, `frontend/src/auth/types.ts`; `specs/features/session/specification.md`, `specs/features/primeiro-acesso/api.md`; `docs/domain/09-business-rules.md`; `docs/api/discrepancies.md`.

**Nenhuma** dessas fontes foi alterada.

---

## 4. Premissas atuais

Estas premissas **orientam esta proposta**; não substituem aprovação formal:

```text
COLABORADOR
  ├── 1 CARGO obrigatório          (DEC-DB-027 — já aprovada, não implementada)
  ├── 1 VÍNCULO ORGANIZACIONAL     (proposta DEC-DB-028)
  └── 1..N PAPÉIS                  (PAPEL_ATRIBUICAO — estrutura AS-IS)

VÍNCULO ORGANIZACIONAL (proposto)
  ├── COD_FEDERACAO  NOT NULL
  ├── COD_SINGULAR   NOT NULL
  ├── COD_AREA       NOT NULL
  └── COD_EQUIPE     NULL permitido

Hierarquia: FEDERAÇÃO → SINGULAR → ÁREA → EQUIPE

Não existe premissa de N vínculos organizacionais por colaborador.
```

**Papéis propostos:**

- Mínimo obrigatório: `COLABORADOR`
- Adicionais independentes: `ADMIN_FEDERACAO`, `ADMIN_SINGULAR`, `ADMIN_AREA`, `ADMIN_EQUIPE`
- Sem herança automática entre `ADMIN_*`
- Sem inferência de `ADMIN_*` a partir de CARGO

---

## 5. Reconciliação do vínculo 1:1

### 5.1 Respostas às questões de governança

| # | Questão | Proposta | Evidência | Classificação |
|---|---------|----------|-----------|---------------|
| 1 | COLABORADOR possui exatamente 1 vínculo? | **Sim** | DDL: um conjunto de FKs escalares; sem tabela N:N | **FATO** estrutural + **proposta** normativa |
| 2 | Vínculo nas FKs de COLABORADOR? | **Sim** | DEC-DB-016, DEC-DB-020; comentário DDL da tabela | **FATO** + **RECOMENDAÇÃO** |
| 3 | VINCULO_ORGANIZACIONAL desnecessário? | **Sim** para 1:1 | Zero ocorrências no repositório; reconciliação cardinalidade | **RECOMENDAÇÃO** |
| 4 | COD_FEDERACAO obrigatório? | **Sim** | Já `NOT NULL` no DDL e JPA | **FATO** |
| 5 | COD_SINGULAR obrigatório? | **Sim** (proposta) | Hoje `NULL` no DDL | **GAP** AS-IS → proposta TO-BE |
| 6 | COD_AREA obrigatório? | **Sim** (proposta) | Hoje `NULL`; BR-010/DEC-FA-002 exigem área para operar | **GAP** AS-IS → proposta TO-BE |
| 7 | COD_EQUIPE opcional? | **Sim** | DEC-DB-015; modelo físico | **FATO** |
| 8 | Hierarquia preservada? | **Sim** (conceito) | DEC-ORG-001, BR-040 | **FATO** normativo; **GAP** enforcement |
| 9 | Vínculo independente de CARGO? | **Sim** | DEC-DB-027 item 6; DEC-ORG-002 | **FATO** |
| 10 | Vínculo independente de PAPEL? | **Sim** | DEC-DB-020 | **FATO** |
| 11 | PAPEL_ATRIBUICAO para 1..N? | **Sim** | `02-logical-model.md` | **FATO** |
| 12 | COLABORADOR papel mínimo? | **Sim** (proposta) | Seed `008-initial-data.sql`; sem enforcement | **GAP** runtime |
| 13 | Partes DEC-FA-003 superseded? | **Parcial** — ver §11 | DEC-FA-003 P1, P4, P6, P8 | **DECISÃO HUMANA NECESSÁRIA** |
| 14 | Partes DEC-FA-003 vigentes? | P2, P3, P5; bloqueio 0 vínculos | DEC-FA-001/002 | **FATO** |

### 5.2 Conflitos residuais conhecidos

| Conflito | Descrição | Tratamento na proposta |
|----------|-----------|------------------------|
| DEC-FA-003 P1 vs 1:1 | Decisão vigente aprova N vínculos | Supersession **parcial** na DEC-DB-028 |
| DEC-FA-003 P6 | Rejeitou “1 vínculo em COLABORADOR” como alvo | Supersession **parcial** — restaurar como alvo aprovado |
| DEC-DB-020 | Login pode criar colaborador só com federação | **Complementar** DEC-DB-020 na implementação futura (não nesta etapa) |
| DEC-DB-027 §Vínculo | Cita “1 COLABORADOR → N vínculos (DEC-FA-003)” | **Complementar** referência cruzada após DEC-DB-028 |
| BR-041 | “N vínculos organizacionais” | **Complementar** / supersession parcial derivada de DEC-FA-003 |

---

## 6. Avaliação das FKs de COLABORADOR

### 6.1 Modelo físico proposto

```text
COLABORADOR
  COD_FEDERACAO  NOT NULL   → FEDERACAO
  COD_SINGULAR   NOT NULL   → SINGULAR
  COD_AREA       NOT NULL   → AREA
  COD_EQUIPE     NULL       → EQUIPE (opcional)
```

### 6.2 Comparativo AS-IS × proposta

| Coluna | AS-IS (DDL) | Proposta DEC-DB-028 | Delta |
|--------|-------------|---------------------|-------|
| `COD_FEDERACAO` | NOT NULL | NOT NULL | Nenhum |
| `COD_SINGULAR` | NULL | NOT NULL | Endurecer nullability + backfill |
| `COD_AREA` | NULL | NOT NULL | Endurecer nullability + backfill |
| `COD_EQUIPE` | NULL | NULL (0..1) | Nenhum na nullability |

**FATO.** API (`CreateColaboradorRequest`) e JPA já aceitam Singular/Área opcionais — implementação futura precisará alinhar validação.

**FATO.** `locateOrCreate` (FT-AUTH) persiste colaborador com apenas `COD_FEDERACAO` — incompatível com vínculo completo obrigatório no cadastro, mas **compatível** com bloqueio operacional (DEC-FA-001/002) até onboarding completar o vínculo.

**DECISÃO HUMANA NECESSÁRIA:** ~~o vínculo completo é obrigatório no registro vs operação~~ — **RESOLVIDA (DH-03/DH-04, 2026-08-14):** vínculo completo obrigatório **no registro**; identidade autenticada pode existir antes sem COLABORADOR persistido.

---

## 7. Avaliação de VINCULO_ORGANIZACIONAL

### 7.1 Recomendação

**Manter o vínculo organizacional diretamente em COLABORADOR.**

`VINCULO_ORGANIZACIONAL` **não é necessário** sob a premissa 1:1 desta proposta.

### 7.2 Análise técnica

| Critério | FKs em COLABORADOR | Tabela VINCULO_ORGANIZACIONAL 1:1 |
|----------|-------------------|-----------------------------------|
| Normalização | Desnormalização controlada; padrão já adotado (DEC-DB-015: equipe via coluna) | Entidade espelho com UK em `COD_COLABORADOR` — sem ganho semântico |
| Cardinalidade 1:1 | Nativa (colunas escalares) | Exigiria UK 1:1 — equivalente funcional |
| Integridade referencial | FKs existem; falta coerência hierárquica transversal | Mesmos CHECKs poderiam ir na tabela filha — não justifica nova entidade |
| Simplicidade | Alta — JPA, API, `/auth/me` já usam um objeto | JOIN extra; nova entidade; rewrite amplo |
| Consistência AS-IS | **Total** | **Nula** — tabela inexistente |
| Consistência domínio | Comentário DDL: vínculo **é** atributo do colaborador | Só se vínculo tiver ciclo de vida próprio |
| Impacto futuro para N | Custo alto para evoluir a N depois | Facilita N — **irrelevante** se N está excluído desta decisão |
| Overengineering | Baixo | **Alto** para 1:1 sem histórico/vigência |

### 7.3 Quando VINCULO_ORGANIZACIONAL seria necessário

Evidências que **justificariam** a segunda hipótese (nenhuma vigente nesta proposta):

1. Reafirmação de N pertinências (DEC-FA-003 P1) — **excluído** pelo escopo atual.
2. Histórico/vigência de vínculos (`DAT_INICIO`/`DAT_FIM`) — **não** requisitado no repositório.
3. Vínculo com agregado e ciclo de vida independente do colaborador — **não** evidenciado.

**Conclusão:** sem essas evidências, criar `VINCULO_ORGANIZACIONAL` apenas para envelopar 1:1 seria overengineering.

---

## 8. Integridade da hierarquia

Regras a verificar:

```text
COLABORADOR.COD_SINGULAR  ∈  SINGULAR onde SINGULAR.COD_FEDERACAO = COLABORADOR.COD_FEDERACAO
COLABORADOR.COD_AREA      ∈  AREA onde AREA.COD_SINGULAR = COLABORADOR.COD_SINGULAR
COLABORADOR.COD_EQUIPE    ∈  EQUIPE onde EQUIPE.COD_AREA = COLABORADOR.COD_AREA  (quando informado)
```

### 8.1 Matriz de garantias

| Relação | Oracle (DDL) | Aplicação | Classificação |
|---------|----------------|-----------|---------------|
| `COLABORADOR.COD_SINGULAR` → federação do colaborador | Não garante | Não valida `federationId` em `resolveOrganizationalLinks` | **GAP** |
| `COLABORADOR.COD_AREA` → singular do colaborador | Não garante | Parcial: CRUD admin valida área→singular | **GAP** |
| `COLABORADOR.COD_EQUIPE` → área do colaborador | Não garante | Parcial: CRUD admin valida equipe→área | **GAP** |
| `AREA.COD_SINGULAR` obrigatório para área do vínculo | `AREA.COD_SINGULAR` é **NULL** permitido | Áreas federativas existem no modelo | **CONFLICT** com leitura estrita DEC-ORG-001 vs DEC-DB-022 |
| `locateOrCreate` | Não passa por validação hierárquica | Cria sem Singular/Área | **RISCO** de dados incompletos |
| Testes `ColaboradorDomainServiceTest` | — | Não cobrem hierarquia | **GAP** de evidência |

**RECOMENDAÇÃO (implementação futura, fora desta etapa):** constraints compostas ou validação de domínio obrigatória na criação/atualização de colaborador; não incluir na DEC-DB-028 de governança, mas registrar como consequência.

**Não corrigido** nesta etapa, conforme restrições.

---

## 9. Vínculo × Cargo × Papel

### 9.1 Separação conceitual

| Eixo | Significado | Onde persiste (AS-IS) | Onde persiste (TO-BE aprovado) |
|------|-------------|----------------------|-------------------------------|
| **VÍNCULO** | Pertencimento organizacional (onde está) | `COLABORADOR.COD_FEDERACAO/SINGULAR/AREA/EQUIPE` | Mesmo — proposta DEC-DB-028 |
| **CARGO** | Função/cargo ocupado (o que é na organização) | Inexistente (`DES_CARGO` removido) | `CARGO` + `COLABORADOR.COD_CARGO` (DEC-DB-027) |
| **PAPEL** | Responsabilidade/autorização (o que pode fazer) | `PAPEL` + `PAPEL_ATRIBUICAO` | Idem + enforcement futuro |

### 9.2 Validações

| Regra | Resultado | Base |
|-------|-----------|------|
| CARGO ≠ VÍNCULO | **Compatível** | DEC-DB-027 item 6: cargo não no vínculo |
| CARGO ≠ PAPEL | **Compatível** | DEC-ORG-002.4; DEC-DB-027.5 |
| VÍNCULO ≠ PAPEL | **Compatível** | DEC-DB-020: FKs cadastrais ≠ `PAPEL_ATRIBUICAO` |
| COLABORADOR → 1 CARGO | **Compatível** (já decidido) | DEC-DB-027; **GAP** implementação |
| COLABORADOR → 1..N PAPÉIS | **Compatível** (estrutura) | `PAPEL_ATRIBUICAO`; **GAP** runtime |
| Cargo não concede ADMIN_* | **Compatível** | DEC-ORG-002.6 |
| Papel não altera vínculo | **Compatível** | Escopos em `PAPEL_ATRIBUICAO` não atualizam `COLABORADOR.COD_*` |

**FATO.** N papéis com N escopos **não** exigem N vínculos. Exemplo DEC-ORG-002 (ADMIN_AREA em TI e Financeiro) cabe em `PAPEL_ATRIBUICAO` com um único vínculo cadastral.

---

## 10. Regra de papéis 1..N

### 10.1 Proposta de domínio

- Todo `COLABORADOR` possui **pelo menos um** `PAPEL`.
- Papel mínimo obrigatório: `COLABORADOR`.
- Papéis adicionais independentes: `ADMIN_FEDERACAO`, `ADMIN_SINGULAR`, `ADMIN_AREA`, `ADMIN_EQUIPE`.
- Sem herança: `ADMIN_AREA` ⊄ `ADMIN_SINGULAR` ⊄ `ADMIN_FEDERACAO`.

### 10.2 AS-IS por camada

| Camada | Evidência | GAP |
|--------|-----------|-----|
| **PAPEL** (DDL) | Tabela com `NOM_PAPEL` UK | — |
| **PAPEL_ATRIBUICAO** | 1..N por colaborador; escopos nullable; sem UK duplicata | Sem XOR de escopo; BR-028 vs papel global |
| **Seeds** | `ADMINISTRADOR`, `GESTOR_DOCUMENTAL`, `EDITOR`, `COLABORADOR` | **Sem** `ADMIN_FEDERACAO` etc. |
| **Backend** | Zero JPA de papel; `loadPermissions()` → `[]` | RBAC não implementado (DISC-002, DISC-005) |
| **`/auth/me`** | `permissions: []`; sem campo `roles` no DTO Java | Frontend declara `roles?` opcional |
| **Autenticação** | Admin por `session-administrator-emails` | Não usa `PAPEL_ATRIBUICAO` |
| **Frontend** | `enforceAuthorization: false` | Guard desligado |

### 10.3 Divergência de nomenclatura (não resolvida nesta etapa)

| Termo | Onde aparece | Observação |
|-------|--------------|------------|
| `COLABORADOR` | Seed `PAPEL`; proposta papel mínimo | Homônimo entidade × papel — risco de ambiguidade em specs |
| `ADMINISTRADOR` | Seed `PAPEL` | Genérico; não mapeado a `ADMIN_*` |
| `ADMIN` | `frontend` routes (`roles: ["ADMIN"]`) | Não existe no seed |
| `ADMIN_FEDERACAO` / `ADMIN_SINGULAR` / `ADMIN_AREA` / `ADMIN_EQUIPE` | DEC-ORG-002, proposta | **Ausentes** no banco |

**DECISÃO HUMANA NECESSÁRIA:** catálogo e nomenclatura `ADMIN_*` vs `ADMINISTRADOR` — **NÃO BLOQUEANTE** para o núcleo da DEC-DB-028 (vínculo), mas **BLOQUEANTE** para implementação de autorização.

### 10.4 Escopo do papel mínimo COLABORADOR

**DECISÃO HUMANA NECESSÁRIA:** atribuição de `COLABORADOR` com escopo global (todos escopos NULL em `PAPEL_ATRIBUICAO`) ou escopo alinhado ao vínculo (`COD_FEDERACAO`/`COD_SINGULAR`/`COD_AREA` preenchidos)?

**RISCO:** BR-028 exige escopo válido; observação física permite papel global quando todos escopos são NULL — tensão documental.

---

## 11. Reconciliação da DEC-FA-003

**FATO.** DEC-FA-003 permanece **Aprovada** (2026-07-24). Esta proposta **não** a altera. Propõe tratamento para eventual supersession **parcial** se a DEC-DB-028 for aprovada.

### 11.1 Tabela de reconciliação

| Elemento DEC-FA-003 | Estado atual | Nova regra (1:1) | Tratamento proposto | Justificativa |
|---------------------|--------------|------------------|---------------------|---------------|
| **Decisão §1 — N vínculos** (ex.: Área A, B, C) | Vigente; não implementado | 1 vínculo obrigatório | **SUPERSEDE PARCIALMENTE** | Incompatível com cardinalidade proposta; núcleo do conflito |
| **Alternativa rejeitada — “1 vínculo em COLABORADOR”** | Rejeitada como alvo TO-BE | Adotada como modelo | **SUPERSEDE PARCIALMENTE** | Inverte alternativa da DEC; requer registro explícito na DEC-DB-028 |
| **Título “Multi-contexto” como N pertinências** | Vigente | Um único contexto de pertinência | **SUPERSEDE PARCIALMENTE** | Redefinir escopo do título na DEC-DB-028; não apagar DEC-FA-003 |
| **Contexto Ativo** (`federationId`, `singularId`, `areaId`) | Vigente | Igual ao único vínculo | **MANTER** | Com 1:1, contexto ativo = vínculo |
| **Navegação operacional no Contexto Ativo** | Vigente | Inalterada | **MANTER** | BR-012; `session.store` |
| **RN-SESSION-003 — seleção quando N > 1** | Vigente documentalmente | Nunca ocorre | **SUPERSEDE PARCIALMENTE** | Regra morta; specs PA RN-PA-003 ficam obsoletas no ramo N>1 |
| **Sem `COD_*_CTX` em AUTH_SESSAO** (REF-DB-CTX-01) | Vigente e implementado | Inalterado | **MANTER** | Independente da cardinalidade |
| **Persistência do Contexto Ativo (FT-PA / FT-SESSION)** | Vigente; mecanismo não implementado | Simplifica: contexto = vínculo | **COMPLEMENTAR** | FT-PRIMEIRO-ACESSO pode persistir contexto sem lista N |
| **Encerramento OQ-027** | Vigente | Contexto ativo permanece; “N áreas” deixa de valer | **COMPLEMENTAR** | Reinterpretar OQ-027 no registro da DEC-DB-028 |
| **OQ-008 — N áreas aprovado** (via P7) | Parcialmente encerrado como “N áreas” | 1 área por colaborador | **SUPERSEDE PARCIALMENTE** | Ramo “N áreas”; equipe por área permanece aberto |
| **`organizationalLinks` (derivado FT-AUTH)** | Objeto único em `/auth/me` | Objeto único = vínculo | **MANTER** | Já alinhado ao 1:1 |
| **`contexts[]` / PA-API-001 (derivado)** | Proposto, não implementado | Desnecessário ou lista de 1 elemento | **SUPERSEDE PARCIALMENTE** | Contrato TO-BE multi-contexto obsoleto para pertinência |
| **`activeContext` (derivado FT-PA)** | Derivado do único link no FE | = vínculo | **MANTER** | Compatível |
| **Auto-seleção com 1 vínculo (RN-SESSION-002)** | Vigente | Único caminho | **MANTER** | Torna-se sempre o fluxo feliz |
| **Bloqueio com 0 vínculos válidos (DEC-FA-001/002)** | Vigente | Mantido | **MANTER** | Primeiro acesso continua bloqueando sem área |
| **Multi-contexto como N papéis/escopos** | Não é texto da DEC-FA-003 | N papéis em PAPEL_ATRIBUICAO | **NÃO AFETADO** | Ortogonalidade DEC-DB-020 |
| **Implementação atual (código)** | Fase 1 = 1 link | Alinhado | **NÃO AFETADO** | Código já não implementa N |
| **BR-041** | Derivada de DEC-FA-003 P1 | 1 vínculo | **COMPLEMENTAR** | Atualizar após aprovação DEC-DB-028 (fora desta etapa) |

### 11.2 Síntese A / B / C / D

| Categoria | Itens |
|---------|-------|
| **A) Incompatíveis com 1:1** | Decisão §1 (N vínculos); alternativa rejeitada “1 vínculo”; título como N pertinências; RN-SESSION-003; ramo “N áreas” de P7 |
| **B) Superseed parcial proposto** | Mesmos itens de (A) — **não** a DEC inteira |
| **C) Continuam válidos** | Contexto Ativo; navegação no contexto; REF-DB-CTX-01; `organizationalLinks`; auto-seleção; bloqueio 0 vínculos |
| **D) Independentes do número de vínculos** | REF-DB-CTX-01; separação vínculo/autorização; N papéis; DEC-FA-004 (Home dinâmica) |

---

## 12. Impacto nas decisões existentes

| Decisão | Impacto | Tratamento proposto |
|---------|---------|---------------------|
| **DEC-FA-003** | **Alto** — conflito em N vínculos; compatível em Contexto Ativo | **SUPERSEDE PARCIALMENTE** itens P1, P4, P6, P8 e ramo “N áreas” de P7; **MANTER** P2, P3, P5. **Não reescrever** o artefato original — registrar referência cruzada na DEC-DB-028 |
| **DEC-ORG-002** | **Baixo** — cargo ≠ papel ≠ ADMIN_* reforçado | **MANTER** integralmente |
| **DEC-DB-027** | **Médio** — cargo 1:1 mantido; parágrafo “N vínculos / VINCULO_ORGANIZACIONAL” desatualizado | **MANTER** decisão de cargo; **COMPLEMENTAR** referência: vínculo 1:1 em COLABORADOR conforme DEC-DB-028. **Não alterar** DEC-DB-027 nesta etapa |
| **DEC-DB-015** | **Baixo** — equipe opcional via `COD_EQUIPE`; gestor/líder por FK | **MANTER** |
| **DEC-DB-016** | **Médio** — FKs org “sem alteração estrutural” vs NOT NULL Singular/Área | **MANTER** princípios (gestor, contato, identidade); **COMPLEMENTAR** nullability na implementação futura |
| **DEC-DB-020** | **Médio** — distinção vínculo ≠ papel mantida; login com FKs NULL | **MANTER** princípio vínculo ≠ autorização; **COMPLEMENTAR** interpretação: vínculo incompleto permitido no registro somente se decisor confirmar (ver §6.2) |

---

## 13. Proposta de DEC-DB-028

> **Status desta seção:** PROPOSTA PARA REVISÃO — **NÃO APPROVED**

### Metadados sugeridos

| Campo | Valor proposto |
|-------|----------------|
| ID | DEC-DB-028 |
| Título | Modelo de vínculo organizacional único do COLABORADOR |
| Categoria | Dados — persistência Oracle + domínio organizacional |
| Criticidade | Crítica |
| Pré-requisitos | DEC-ORG-001, DEC-ORG-002, DEC-DB-015, DEC-DB-016, DEC-DB-020 |
| Relação DEC-FA-003 | Supersession **parcial** dos itens identificados em §11 |

### Texto proposto (15 itens avaliados)

| # | Proposta | Avaliação | Evidência / ressalva |
|---|----------|-----------|----------------------|
| 1 | Cada COLABORADOR possui **exatamente um** vínculo organizacional | **Suportada** | DDL 1:1 implícito; conflita DEC-FA-003 P1 até supersession |
| 2 | O vínculo é representado **diretamente em COLABORADOR** | **Suportada** | AS-IS; DEC-DB-016/020 |
| 3 | `COD_FEDERACAO` é **obrigatório** | **Suportada** | Já NOT NULL |
| 4 | `COD_SINGULAR` é **obrigatório** | **Suportada com GAP** | Proposta exige ALTER; hoje NULL |
| 5 | `COD_AREA` é **obrigatório** | **Suportada com GAP** | Proposta exige ALTER; hoje NULL |
| 6 | `COD_EQUIPE` é **opcional** | **Suportada** | DEC-DB-015; 0..1 |
| 7 | Hierarquia **FEDERAÇÃO → SINGULAR → ÁREA → EQUIPE** | **Suportada** | DEC-ORG-001; enforcement é GAP |
| 8 | **Não criar** `VINCULO_ORGANIZACIONAL` para 1:1 | **Suportada** | Reconciliação cardinalidade §8 |
| 9 | **CARGO** permanece **independente** do vínculo | **Suportada** | DEC-DB-027 item 6 |
| 10 | **PAPEL** permanece **independente** do vínculo | **Suportada** | DEC-DB-020 |
| 11 | `PAPEL_ATRIBUICAO` representa **1..N** COLABORADOR ↔ PAPEL | **Suportada** | Modelo lógico/físico |
| 12 | **`COLABORADOR`** é o papel mínimo obrigatório | **Suportada com DECISÃO HUMANA NECESSÁRIA** | Seed existe; sem enforcement; escopo da atribuição aberto |
| 13 | **`ADMIN_*`** são papéis **independentes** | **Suportada conceitualmente** | DEC-ORG-002; catálogo físico é GAP |
| 14 | Nenhum **`ADMIN_*`** inferido automaticamente por **CARGO** | **Suportada** | DEC-ORG-002.6 |
| 15 | Nenhum **`ADMIN_*`** possui herança automática de outro **`ADMIN_*`** | **Suportada** | Sem código de herança; premissa explícita |

### Redação consolidada sugerida (para aprovação humana)

1. Todo **COLABORADOR** possui **exatamente um** vínculo organizacional.
2. O vínculo é persistido pelas colunas `COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA` e `COD_EQUIPE` em `COLABORADOR` — **não** se cria tabela `VINCULO_ORGANIZACIONAL` para cardinalidade 1:1.
3. `COD_FEDERACAO`, `COD_SINGULAR` e `COD_AREA` são **NOT NULL** para todo colaborador com vínculo válido.
4. `COD_EQUIPE` é **opcional** (0 ou 1).
5. A hierarquia organizacional do vínculo respeita **Federação → Singular → Área → Equipe**.
6. **CARGO** (DEC-DB-027) e **PAPEL** (`PAPEL_ATRIBUICAO`) são eixos **ortogonais** ao vínculo.
7. Todo colaborador possui **pelo menos um** papel; o mínimo é **`COLABORADOR`**.
8. Papéis `ADMIN_FEDERACAO`, `ADMIN_SINGULAR`, `ADMIN_AREA`, `ADMIN_EQUIPE` são **independentes** entre si e **não** são concedidos por cargo.
9. Esta decisão **supersede parcialmente** a DEC-FA-003 nos pontos de **N vínculos de pertinência**, mantendo **Contexto Ativo**, navegação no contexto e **REF-DB-CTX-01**.
10. **Implementação** (DDL, JPA, API, seeds, specs) fica **fora** do escopo desta decisão de governança.

### Exclusões explícitas da DEC-DB-028 proposta

- Criação de tabela `CARGO` ou `COD_CARGO` (DEC-DB-027 — decisão separada já aprovada).
- Catálogo definitivo `ADMIN_*` vs `ADMINISTRADOR`.
- Constraints hierárquicas Oracle (consequência de implementação).
- Alteração de `AREA.COD_SINGULAR` nullable (DEC-DB-022 — decisão separada se necessário).

---

## 14. Decisões humanas pendentes

Somente questões com evidência real de abertura. Classificação: **BLOQUEANTE** impede aprovação segura da DEC-DB-028; **NÃO BLOQUEANTE** pode ser tratada em decisão ou implementação posterior.

| # | Questão | Classificação | Motivo |
|---|---------|---------------|--------|
| DH-01 | **Aprovar supersession parcial da DEC-FA-003** (P1, P4, P6, P8, ramo N áreas de P7) | **BLOQUEANTE** | Sem isso, DEC-DB-028 conflita decisão vigente de N vínculos |
| DH-02 | **Aprovar cardinalidade 1:1** como política definitiva (vs manter N) | **BLOQUEANTE** | É o núcleo da proposta — decisor deve confirmar ou rejeitar |
| DH-03 | Vínculo completo obrigatório **no registro** vs apenas para **operação** (`locateOrCreate`) | **BLOQUEANTE** | Afeta interpretação dos itens 3–5 e DEC-DB-020 |
| DH-04 | **Obrigatoriedade de Singular e Área** para todo colaborador (incl. federativo) | **BLOQUEANTE** se leitura estrita; **NÃO BLOQUEANTE** se exceção federativa for mantida | Tensão com `03-physical-model.md` “colaborador federativo” |
| DH-05 | **Obrigatoriedade de Federação** | **NÃO BLOQUEANTE** para aprovação | Já NOT NULL no AS-IS |
| DH-06 | **Opcionalidade de Equipe** | **NÃO BLOQUEANTE** | Alinhada ao AS-IS e DEC-DB-015; OQ-008 (N equipes) é periférica |
| DH-07 | **Papel mínimo COLABORADOR** — promover a RN e momento de atribuição | **NÃO BLOQUEANTE** para núcleo vínculo; **BLOQUEANTE** para closure de autorização | Pode constar na DEC-DB-028 como princípio; enforcement é implementação |
| DH-08 | **Escopo de PAPEL_ATRIBUICAO** para papel mínimo (global vs vínculo) | **NÃO BLOQUEANTE** para DEC-DB-028 núcleo | Tensão BR-028 vs modelo físico |
| DH-09 | **Nomenclatura ADMIN_* vs ADMINISTRADOR vs ADMIN (frontend)** | **NÃO BLOQUEANTE** para DEC-DB-028 | Decisão de catálogo/autorização separada (OQ-020) |

### Questões que **não** permanecem abertas nesta proposta

| Questão | Motivo do encerramento analítico |
|---------|----------------------------------|
| Criar `VINCULO_ORGANIZACIONAL` para 1:1? | Proposta recomenda **não**, com evidência |
| PAPEL_ATRIBUICAO suporta 1..N? | **FATO** estrutural |
| CARGO independente do vínculo? | **FATO** DEC-DB-027 |
| Herança entre ADMIN_*? | **FATO** inexistente no código |

---

## 15. Recomendação técnica

**RECOMENDAÇÃO (não é decisão):**

1. **Aprovar** a DEC-DB-028 proposta **se e somente se** o decisor humano confirmar DH-01, DH-02 e DH-03.
2. **Manter** o vínculo nas FKs de `COLABORADOR`; **não** criar `VINCULO_ORGANIZACIONAL`.
3. **Endurecer** `COD_SINGULAR` e `COD_AREA` para `NOT NULL` na fase de implementação, com plano de backfill para registros incompletos.
4. **Supersedir parcialmente** a DEC-FA-003 — **nunca** integralmente.
5. **Manter** DEC-ORG-002 e DEC-DB-027 no eixo cargo; **complementar** DEC-DB-027 com referência ao vínculo 1:1.
6. Tratar papel mínimo `COLABORADOR` e catálogo `ADMIN_*` como **decisões complementares** (DH-07, DH-09), não bloqueantes para fechar o modelo de vínculo.
7. Registrar integridade hierárquica como **consequência de implementação** (CHECK ou domínio), não como item bloqueante da governança DEC-DB-028.

---

## 16. Critérios para aprovação posterior

O decisor humano deve poder marcar **APROVO** ou **NÃO APROVO** verificando:

### Critérios obrigatórios (todos devem ser aceitos para aprovar)

- [ ] Aceita **1 vínculo** por colaborador como política definitiva.
- [ ] Aceita **supersession parcial** da DEC-FA-003 (itens §11.1), **sem** apagar a decisão original.
- [ ] Aceita vínculo nas **FKs de COLABORADOR** sem `VINCULO_ORGANIZACIONAL`.
- [ ] Aceita **Federação + Singular + Área obrigatórias** e **Equipe opcional**.
- [ ] Aceita que **CARGO** e **PAPEL** permanecem independentes do vínculo.
- [ ] Aceita que implementação (DDL, código, specs) é **etapa posterior**.

### Critérios desejáveis (podem ser condicionantes)

- [ ] Define tratamento de `locateOrCreate` (DH-03).
- [ ] Define se colaborador federativo sem Singular/Área continua existindo (DH-04).
- [ ] Define escopo do papel mínimo `COLABORADOR` (DH-08).
- [ ] Agenda decisão de catálogo `ADMIN_*` (DH-09).

### Critérios de rejeição explícita

Rejeitar a proposta se:

- O negócio **reafirmar N vínculos** de pertinência (DEC-FA-003 P1 integral).
- For exigido **histórico de vínculos** com vigência sem nova decisão de escopo.
- For exigida entidade `VINCULO_ORGANIZACIONAL` apenas por formalismo, sem requisito 1:N ou histórico.

---

## Apêndice A — Coerência DEC-DB-027 (CARGO)

Verificação solicitada sem alterar DEC-DB-027.

| Item DEC-DB-027 | Coerência com proposta |
|-----------------|------------------------|
| CARGO catálogo independente | **Mantida** |
| 1 COLABORADOR → 1 CARGO | **Mantida** |
| `COLABORADOR.COD_CARGO NOT NULL` | **Mantida** (TO-BE não implementado) |
| CARGO ≠ vínculo | **Mantida** — reforçada pela DEC-DB-028 proposta |
| Atributos `COD_CARGO`, `NOM_CARGO`, `FLG_ATIVO`, auditoria | **Mantidos** conforme DEC-DB-027 |
| `DSC_CARGO` | DEC-DB-027 classifica como **“Não criar por padrão”**; `DSC_PAPEL` existe em `PAPEL` mas não é requisito para `CARGO`. A lista do escopo da atividade menciona `DSC_CARGO` — **não** alterar DEC-DB-027 nesta etapa |

**CONFLICT documental (não bloqueante para vínculo):** DEC-DB-027 §“Separação conceitual” ainda cita *"1 COLABORADOR → N vínculos (DEC-FA-003)"* e `VINCULO_ORGANIZACIONAL`. A DEC-DB-028 proposta **complementaria** essa referência após aprovação.

---

## Apêndice B — Confirmação de não-implementação

| Item | Status |
|------|--------|
| DEC-DB-028 criada/aprovada | **Não** |
| DEC-FA-003, DEC-ORG-002, DEC-DB-027 alteradas | **Não** |
| Código, DDL, migrations, JPA, API, FE, testes, seeds | **Não alterados** |
| Único artefato produzido | Este arquivo |

---

## Apêndice C — Checklist do decisor

```text
[ ] Li a proposta DEC-DB-028 (§13)
[ ] Entendo que 1 vínculo = FKs em COLABORADOR
[ ] Entendo que Federação, Singular e Área são obrigatórias na proposta
[ ] Entendo que Equipe é opcional
[ ] Entendo que VINCULO_ORGANIZACIONAL não será criado para 1:1
[ ] Entendo que CARGO e PAPEL são independentes do vínculo
[ ] Entendo que COLABORADOR é papel mínimo proposto (enforcement futuro)
[ ] Entendo que ADMIN_* são independentes e sem herança
[ ] Entendo que DEC-FA-003 será parcialmente superseded (não apagada)
[ ] Decido: APROVO / NÃO APROVO / APROVO COM RESSALVAS (especificar DH-01 a DH-09)
```
