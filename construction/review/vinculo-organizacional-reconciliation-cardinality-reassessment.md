# Reconciliação técnica e de governança — cardinalidade de vínculo organizacional

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/vinculo-organizacional-reconciliation-cardinality-reassessment.md` |
| Data | 2026-08-14 |
| Tipo | Reconciliação analítica (sem implementação) |
| Status | **EVIDÊNCIA PARA DECISÃO HUMANA** — não formaliza DEC-DB-028 |
| Escopo | COLABORADOR × VÍNCULO × CARGO × PAPEL vs AS-IS e decisões vigentes |

**Classificação usada neste relatório:** `FATO` · `GAP` · `CONFLICT` · `INFERÊNCIA` · `RECOMENDAÇÃO` · `DECISÃO PENDENTE`.

**Restrições cumpridas:** nenhum código, DDL, migration, JPA, API, frontend, teste, seed ou decisão existente foi alterado. DEC-FA-003 não foi modificada. DEC-DB-028 não foi criada.

---

## 1. Resumo executivo

Para o modelo **AS-IS** do Portal de Comunicação, **não é necessário criar `VINCULO_ORGANIZACIONAL`** para representar um único vínculo organizacional: as FKs já existentes em `COLABORADOR` (`COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE`) já materializam **no máximo um** conjunto de posicionamento organizacional por colaborador.

A nova premissa a validar (1 colaborador → 1 vínculo completo + 1 cargo + 1..N papéis, com papel mínimo `COLABORADOR`) **é compatível com a estrutura física AS-IS** (cardinalidade 1:1 implícita nas colunas de `COLABORADOR`) e **é compatível com `PAPEL_ATRIBUICAO`** (já 1..N). Ela **conflita com a DEC-FA-003 no ponto de N vínculos de pertinência**, não no ponto de Contexto Ativo.

Respostas objetivas às perguntas de sucesso:

| Pergunta | Resposta classificada |
|----------|------------------------|
| É necessário criar `VINCULO_ORGANIZACIONAL`, ou as FKs de `COLABORADOR` já representam o único vínculo? | **RECOMENDAÇÃO:** se a cardinalidade 1:1 for aprovada, as FKs de `COLABORADOR` já representam o vínculo. Entidade própria só se justifica se N vínculos, histórico ou vigência forem requisitos confirmados. **Não é decisão.** |
| Como reconciliar `COLABORADOR → 1..N PAPÉIS` com papel mínimo `COLABORADOR` vs `PAPEL`/`PAPEL_ATRIBUICAO` e DEC-FA-003? | **FATO:** `PAPEL_ATRIBUICAO` já suporta 1..N. **FATO:** o seed já contém `NOM_PAPEL = 'COLABORADOR'`. **GAP:** não há JPA, atribuição automática nem exposição em `/auth/me`. **CONFLICT:** DEC-FA-003 trata N **vínculos de pertinência**; N **papéis/escopos** já são ortogonais (DEC-DB-020) e não exigem N linhas de vínculo. |

A eventual DEC-DB-028 **não deve ser formalizada** até decisão humana sobre: (a) manter ou supersedir parcialmente DEC-FA-003; (b) obrigatoriedade de Singular/Área vs colaborador só-federação; (c) papel mínimo e catálogo `ADMIN_*`.

---

## 2. Escopo

### Incluído

- Levantamento AS-IS de COLABORADOR, FKs organizacionais, PAPEL, PAPEL_ATRIBUICAO, sessão/auth, frontend e specs.
- Validação da nova cardinalidade 1:1 (premissa **não aprovada**).
- Reconciliação item a item da DEC-FA-003.
- Comparação Alternativa A (FKs em COLABORADOR) vs Alternativa B (entidade `VINCULO_ORGANIZACIONAL`).
- Separação conceitual VÍNCULO × CARGO × PAPEL.
- Matriz de divergências e decisões afetadas.

### Excluído (não executado)

- Alteração de código, DDL, migrations, JPA, API, frontend, testes, seeds.
- Alteração de decisões existentes (incluindo DEC-FA-003).
- Criação de DEC-DB-028.
- Correção de inconsistências encontradas.
- Transformação de recomendações em decisões aprovadas.

---

## 3. Fontes analisadas

### Schema e modelo de dados (prioridade de evidência estrutural)

| Fonte | Uso |
|-------|-----|
| `database/ddl/003-create-tables.sql` | Definição física de `COLABORADOR`, `PAPEL`, `PAPEL_ATRIBUICAO`, `AREA`, `EQUIPE`, `AUTH_SESSAO` |
| `database/ddl/004-create-constraints.sql` | PK/UK/FK/CK; ausência de XOR hierárquico e de UK em atribuição |
| `database/ddl/005-create-indexes.sql` | Índices de FKs e `IDX_PAPEL_ATRIBUICAO_ESCOPOS` |
| `database/ddl/006-create-comments.sql` | Comentários oficiais das colunas de vínculo e escopo de papel |
| `database/ddl/008-initial-data.sql` | Seed de `PAPEL` (`ADMINISTRADOR`, `GESTOR_DOCUMENTAL`, `EDITOR`, `COLABORADOR`) |
| `database/model/02-conceptual-model.md` | Regras conceituais; autorização por atribuição, não por hierarquia |
| `database/model/02-logical-model.md` | Cardinalidade lógica COLABORADOR 1──N PAPEL_ATRIBUICAO |
| `database/model/03-physical-model.md` | Obrigatoriedade AS-IS das FKs; colaborador federativo; PAPEL_ATRIBUICAO |
| `database/model/04-entity-catalog.md` | 23 tabelas; ausência de `CARGO` e de `VINCULO_ORGANIZACIONAL` |
| `database/model/05-decisions-and-risks.md` | Texto integral DEC-DB-015, 016, 020, 027 |
| `database/migrations/V007__colaborador_ssot_alignment.sql` | Remoção de `DES_CARGO` (atributo texto, não entidade) |
| `database/migrations/README.md` | DEC-DB-020 / V004 |

### Backend, API, autenticação e testes

| Fonte | Uso |
|-------|-----|
| `ColaboradorEntity.java` | FKs escalares; sem coleção de vínculos; sem `COD_CARGO` |
| `ColaboradorService.java` | `locateOrCreate` com apenas `COD_FEDERACAO` |
| `ColaboradorApplicationService.java` / `CreateColaboradorRequest.java` / `ColaboradorResponse.java` | Cadastro 1 conjunto de FKs; Singular/Área/Equipe opcionais |
| `ColaboradorDomainService.java` | Validação parcial equipe→área→singular; **não** valida federação |
| `AuthenticationService.java` / `AuthenticatedUserResponse.java` / `ColaboradorOrganizationalLinksResponse.java` | `/auth/me`: um objeto `organizationalLinks`; `permissions = []` |
| `JwtTokenService.java` | Claims `fid`/`sid`/`aid`/`tid` — um conjunto por token |
| `SessionAdministratorAuthorizationService.java` | Admin por lista de e-mails, não por `PAPEL` |
| `OrganizationAuthorizationService.java` | Delega ao serviço acima |
| Testes de colaborador / auth / `ColaboradorDomainServiceTest` | Sem cobertura de integridade federação↔singular |
| Busca no backend por `PapelEntity` / `PapelAtribuicao` | **Zero** entidades JPA |

### Frontend, sessão e autorização

| Fonte | Uso |
|-------|-----|
| `frontend/src/auth/types.ts` | `organizationalLinks` singular; `roles?` opcional |
| `frontend/src/stores/session.store.ts` | Copia o único link para `availableContext` e `activeContext` |
| `frontend/src/types/organization/colaborador.types.ts` | Cadastro com FKs opcionais; sem cargo |
| `frontend/src/router/guards/authorization.guard.ts` + `frontend/src/config/router.ts` | Guard existe; `enforceAuthorization: false` |
| Specs `authentication/api.md`, `session/specification.md`, `primeiro-acesso/*` | Contrato fase 1 vs TO-BE N contextos |

### Domínio, governança e specs

| Fonte | Uso |
|-------|-----|
| `docs/governance/03-open-decisions.md` | DEC-FA-001, 002, 003; DEC-ORG-001, 002; referência DEC-DB-027 |
| `docs/domain/04-domain-concepts.md`, `09-business-rules.md`, `10-open-questions.md` | BR-009..012, BR-027/028/034, BR-040/041; OQ-008/020 |
| `docs/api/discrepancies.md` | DISC-002, DISC-005 (permissions vazias; RBAC não implementado) |
| `specs/features/authentication/api.md` | `/auth/me` com um `organizationalLinks` |
| `specs/features/primeiro-acesso/api.md` | `contexts[]` proposto; evolução de `/auth/me` |

**Busca estrutural:** `VINCULO_ORGANIZACIONAL` / `VinculoOrganizacional` — **zero ocorrências** no repositório (código, DDL, modelo, specs).

---

## 4. Nova premissa de domínio (a validar — não aprovada)

```text
COLABORADOR
  ├── 1 CARGO obrigatório
  ├── 1 VÍNCULO ORGANIZACIONAL obrigatório
  └── 1..N PAPÉIS

VÍNCULO ORGANIZACIONAL
  ├── 1 FEDERAÇÃO obrigatório
  ├── 1 SINGULAR obrigatório
  ├── 1 ÁREA obrigatório
  └── 0..1 EQUIPE opcional

PAPÉIS
  └── mínimo 1 papel por colaborador
      └── papel mínimo obrigatório: COLABORADOR
```

**DECISÃO PENDENTE:** esta premissa ainda não é decisão. Todas as seções abaixo confrontam-na com evidência; não a tratam como vigente.

---

## 5. AS-IS real

### 5.1 COLABORADOR — persistência

**FATO.** `database/ddl/003-create-tables.sql`:

| Coluna | Nullability DDL | Entidade referenciada | JPA (`ColaboradorEntity`) |
|--------|-----------------|----------------------|---------------------------|
| `COD_COLABORADOR` | NOT NULL (PK) | — | `id` |
| `COD_FEDERACAO` | **NOT NULL** | `FEDERACAO` | `federacaoId` `nullable = false` |
| `COD_SINGULAR` | **NULL** | `SINGULAR` | `singularId` nullable |
| `COD_AREA` | **NULL** | `AREA` | `areaId` nullable |
| `COD_EQUIPE` | **NULL** | `EQUIPE` | `equipeId` nullable |
| `COD_GESTOR` | NULL | `COLABORADOR` (auto-FK) | `gestorId` nullable |
| `COD_CARGO` | **inexistente** | — | inexistente |

**FATO.** Comentário DDL: *"Perfil do colaborador no Portal — atributos intrínsecos e vínculo organizacional"*. O vínculo está modelado **como colunas do próprio colaborador**, não como tabela associativa.

**FATO.** Não há tabela `VINCULO_ORGANIZACIONAL`. Catálogo oficial: 23 entidades (`04-entity-catalog.md`).

**FATO.** Não há `COD_CARGO`. `DES_CARGO` foi removido por V007 (atributo texto legado). DEC-DB-027 (CARGO TO-BE) está **aprovada e não implementada**.

### 5.2 Cardinalidade física do vínculo

**FATO.** Um colaborador possui **no máximo um** `COD_SINGULAR`, um `COD_AREA` e um `COD_EQUIPE`. Não existe coleção, tabela N:N nem `@OneToMany` de vínculos.

**FATO.** `/auth/me` expõe **um** objeto `organizationalLinks` (`federationId`, `singularId`, `areaId`, `teamId`), preenchido a partir das colunas de `COLABORADOR` (`AuthenticationService.organizationalLinksFrom`).

**FATO.** JWT carrega um único conjunto `fid`/`sid`/`aid`/`tid`.

**FATO.** Frontend `session.store.ts` trata `organizationalLinks` como o único contexto disponível e o promove automaticamente a `activeContext` (RN-SESSION-002 de facto). Comentário no código: *"Multi-context selection is out of scope (OQ-027)"* — apesar de OQ-027 estar encerrada documentalmente pela DEC-FA-003.

**FATO.** Cadastro administrativo (`CreateColaboradorRequest`) aceita um único `federationId` (obrigatório) e `singularId` / `areaId` / `teamId` opcionais. Update **substitui** o mesmo conjunto — não adiciona vínculo.

### 5.3 Hierarquia organizacional AS-IS

| Entidade | Relação física | Nullability relevante |
|----------|----------------|------------------------|
| `SINGULAR.COD_FEDERACAO` | NOT NULL → `FEDERACAO` | Singular sempre tem federação |
| `AREA.COD_SINGULAR` | **NULL** → `SINGULAR` | Área pode existir sem singular |
| `EQUIPE.COD_AREA` | NOT NULL → `AREA` | Equipe sempre tem área |
| `AREA.COD_GESTOR` | NULL → `COLABORADOR` | DEC-DB-015 |
| `EQUIPE.COD_LIDER` | NULL → `COLABORADOR` | DEC-DB-015 |

**FATO.** Modelo físico (`03-physical-model.md`): *"Colaboradores da Federação não possuem obrigatoriedade de vínculo com Singular, Área ou Equipe"*; colaborador de singular exigiria federação+singular+área na **documentação** de observação, **não** em CHECK/NOT NULL.

**GAP.** Não há constraint Oracle que garanta:

- `COLABORADOR.COD_SINGULAR` pertence a `COLABORADOR.COD_FEDERACAO`;
- `COLABORADOR.COD_AREA` pertence a `COLABORADOR.COD_SINGULAR`;
- `COLABORADOR.COD_EQUIPE` pertence a `COLABORADOR.COD_AREA`.

**FATO (parcial na aplicação).** `ColaboradorDomainService.resolveOrganizationalLinks` valida equipe→área e área→singular **quando** o cadastro administrativo passa por esse método. **Não** recebe nem valida `federationId`. `locateOrCreate` **não** chama esse método.

**GAP.** `ColaboradorDomainServiceTest` não cobre as regras hierárquicas (apenas gestor e desativação).

### 5.4 PAPEL e PAPEL_ATRIBUICAO

**FATO.** Tabelas existem no baseline. Cardinalidade lógica: `COLABORADOR 1 ── N PAPEL_ATRIBUICAO` e `PAPEL 1 ── N PAPEL_ATRIBUICAO`.

**FATO.** Escopos de atribuição (`COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE`) são **todos nullable**. Sem CK XOR. Sem UK `(COD_COLABORADOR, COD_PAPEL, escopos)`. Sem herança entre papéis.

**FATO.** Seed `008-initial-data.sql`:

- `ADMINISTRADOR`
- `GESTOR_DOCUMENTAL`
- `EDITOR`
- `COLABORADOR` — *"Usuário padrão do Portal."*

**FATO.** **Não** existem no seed: `ADMIN_FEDERACAO`, `ADMIN_SINGULAR`, `ADMIN_AREA`, `ADMIN_EQUIPE`. Esses nomes aparecem em DEC-ORG-002 / DEC-DB-027 como papéis administrativos de domínio, não como dados persistidos.

**FATO.** Não há JPA de `PAPEL` / `PAPEL_ATRIBUICAO`. `loadPermissions` retorna `Collections.emptyList()`. Backend `/auth/me` **não** expõe `roles`. Frontend declara `roles?: string[]` e o guard de autorização está **desligado** (`enforceAuthorization: false`). Admin de escrita organizacional usa lista de e-mails (`session-administrator-emails`) — DISC-005.

**FATO.** Nenhum INSERT em `PAPEL_ATRIBUICAO` nos seeds/DML analisados. Criação de colaborador (login ou API) **não** atribui papel.

### 5.5 Contexto ativo e AUTH_SESSAO

**FATO.** `AUTH_SESSAO` não possui `COD_*_CTX` (REF-DB-CTX-01). Contexto ativo **não** é persistido no banco AS-IS; o frontend deriva do único `organizationalLinks`.

**FATO.** Specs de FT-PRIMEIRO-ACESSO propõem `GET /session/contexts` com `contexts[]` — **contrato proposto, não implementado**.

### 5.6 Documentação de domínio vs código

| Camada | Cardinalidade de vínculo | Classificação |
|--------|--------------------------|---------------|
| DDL / JPA / API / FE cadastro | 0..1 conjunto de FKs por colaborador | **FATO** AS-IS |
| DEC-FA-003, BR-041, specs PA/SESSION | N vínculos + 1 Contexto Ativo | **FATO** normativo TO-BE |
| DEC-DB-020 / `locateOrCreate` | FKs org opcionais após login | **FATO** AS-IS + decisão vigente |
| DEC-ORG-001 / BR-009 / DEC-FA-002 | Operacional exige Área | **FATO** normativo; **GAP** vs nullability |

---

## 6. Reconciliação COLABORADOR × VÍNCULO

### 6.1 O AS-IS já representa um único vínculo?

**FATO.** Sim, no sentido estrutural: um colaborador não pode persistir dois pares (singular, área) simultaneamente. O “vínculo” AS-IS **é** o conjunto das FKs em `COLABORADOR`.

**GAP vs nova premissa.** Esse único conjunto **não é obrigatoriamente completo**: Singular, Área e Equipe podem ser NULL. Login cria colaborador só com federação.

### 6.2 As FKs em COLABORADOR representam esse vínculo?

**FATO.** Sim. Comentário de tabela, DEC-DB-016 (*"Vínculo organizacional: `COD_SINGULAR`, `COD_AREA` e `COD_EQUIPE` existentes — sem alteração estrutural"*), DEC-DB-015 (*membros de equipe = `COLABORADOR.COD_EQUIPE`*) e DEC-DB-020 (*FKs organizacionais ≠ `PAPEL_ATRIBUICAO`*) tratam essas colunas como o vínculo cadastral.

### 6.3 Existe estrutura que suporte N vínculos?

**FATO.** Não. Nenhuma tabela, coleção JPA, endpoint de lista de vínculos implementado, nem seed com duas pertinências.

**FATO.** O único desenho N está em **documentação/specs** (`contexts[]`, BR-041, DEC-FA-003, exemplo DEC-DB-027 *"Área TI; Área Financeiro"*).

### 6.4 Há evidência real de necessidade de múltiplos vínculos?

| Evidência | Tipo | Interpretação |
|-----------|------|----------------|
| DEC-FA-003 item 1; BR-041; OQ-027 encerrada | Normativa | Negócio **aprovou** N vínculos de pertinência |
| Specs FT-PRIMEIRO-ACESSO (UI se N>1) | Normativa derivada | Fluxo desenhado para N |
| Exemplo DEC-ORG-002: `ADMIN_AREA` → TI e Financeiro | Normativa de **papel** | N **escopos de autorização**, não necessariamente N pertinências |
| Código, DDL, DML, testes | Empírica | **Nenhuma** instância de colaborador com duas áreas |
| OQ-008 (N equipes na mesma área) | Aberta | Nem N equipes está fechado |
| DEC-DB-020 | Vigente | Escopo de papel é independente do cadastro |

**INFERÊNCIA (não promover a fato):** parte da pressão por N vínculos pode ter **confundido** “administrar N áreas” (papel) com “pertencer a N áreas” (vínculo). A DEC-DB-020 já separa esses eixos. Esta inferência **não** supersede DEC-FA-003.

### 6.5 Conflito com regras de negócio existentes

| Regra | Relação com 1:1 obrigatório completo |
|-------|--------------------------------------|
| BR-041 / DEC-FA-003 | **CONFLICT** — N vínculos |
| BR-011 / DEC-FA-001 (ramo N>1) | **CONFLICT potencial** — ramo fica inaplicável se N for impossível |
| BR-009 / BR-010 / DEC-FA-002 | **COMPATÍVEL** no núcleo (exige Área para operar); redação “pelo menos um” admite N |
| BR-012 | **COMPATÍVEL** — contexto = fed+singular+área (+equipe opcional) |
| BR-040 / DEC-ORG-001 | **COMPATÍVEL** com vínculo completo; **GAP** vs área sem singular no DDL |
| DEC-DB-002 / DEC-DB-020 (FKs opcionais) | **CONFLICT** com Singular/Área NOT NULL para **todo** colaborador |
| DEC-DB-015 (0..1 equipe via coluna) | **COMPATÍVEL** com equipe opcional |

---

## 7. Reconciliação DEC-FA-003

Texto integral consultado em `docs/governance/03-open-decisions.md`. Status atual: **Aprovada** (2026-07-24). **Não alterada nesta reconciliação.**

### 7.1 Premissas extraídas

| # | Premissa da DEC-FA-003 |
|---|------------------------|
| P1 | Um colaborador pode possuir **N vínculos** organizacionais (ex.: Área A, B, C) |
| P2 | A sessão possui um **Contexto Ativo** mínimo: `federationId`, `singularId`, `areaId` |
| P3 | Toda navegação operacional utiliza o Contexto Ativo |
| P4 | RN-SESSION-003 torna-se oficial: seleção quando N > 1 |
| P5 | Persistência do Contexto Ativo é de FT-PRIMEIRO-ACESSO / FT-SESSION; **sem** `COD_*_CTX` em `AUTH_SESSAO` |
| P6 | Alternativa rejeitada: permanecer fase 1 (1 vínculo em `COLABORADOR`) |
| P7 | Encerra OQ-027; atualiza OQ-008 (N áreas aprovado; N equipes por área permanece aberto) |
| P8 | Título/escopo: “multi-contexto” = N vínculos + 1 contexto ativo |

`organizationalLinks` e `contexts[]` **não** são texto da DEC-FA-003; são contratos derivados (FT-AUTH fase 1 e FT-PRIMEIRO-ACESSO TO-BE).

### 7.2 Tabela de reconciliação

| Item da DEC-FA-003 | Premissa atual | Nova regra | Resultado | Evidência |
|--------------------|----------------|------------|-----------|-----------|
| P1 — N vínculos de pertinência | Vigente em governança; **não** implementada | 1 vínculo obrigatório | **CONFLITANTE** | DEC-FA-003 §Decisão.1 vs premissa 1:1; DDL sem N |
| P2 — Contexto Ativo (`federationId`, `singularId`, `areaId`) | Vigente | Vínculo completo tem os mesmos campos | **COMPATÍVEL** | Com 1 vínculo, contexto ativo = o vínculo |
| P3 — Navegação no Contexto Ativo | Vigente | Não altera a regra de navegação | **COMPATÍVEL** | BR-012; `session.store` já usa `activeContext` |
| P4 — RN-SESSION-003 (UI se N>1) | Vigente documentalmente; não implementada | N nunca ocorre se 1:1 for aprovado | **SUPERSEDED POTENCIAL** (somente se 1:1 for decisão) | Specs PA RN-PA-003; store TODO OQ-027 |
| P5 — Sem `COD_*_CTX` em `AUTH_SESSAO` | Vigente e implementada | Não exige colunas de contexto | **COMPATÍVEL** / **NÃO RELACIONADA** à cardinalidade de pertinência | DDL `AUTH_SESSAO`; REF-DB-CTX-01 |
| P6 — Rejeição explícita de “1 vínculo em COLABORADOR” | Vigente como **alvo TO-BE** | Restaura o modelo rejeitado como alvo | **CONFLITANTE** | Tabela “Alternativas consideradas” da DEC |
| P7 — OQ-027 encerrada; OQ-008 parcial | Vigente | 0..1 equipe responderia OQ-008 como “não” | **COMPATÍVEL** em OQ-027-contexto; **SUPERSEDED POTENCIAL** em “N áreas aprovado” | `10-open-questions.md` OQ-008 |
| P8 — Multi-contexto (N pertinências) | Vigente | Um único contexto permanente | **CONFLITANTE** no sentido de N pertinências; **COMPATÍVEL** se “multi-contexto” for redefinido como N papéis | Distinção DEC-DB-020 |
| `organizationalLinks` (derivado) | Fase 1 entregue: objeto único | Objeto único = o vínculo | **COMPATÍVEL** com 1:1; **GAP** vs TO-BE `contexts[]` | `/auth/me`; `types.ts` |
| `contexts[]` (derivado, não implementado) | Proposto em PA-API-001 | Lista de 0 ou 1 elemento, ou desnecessário | **SUPERSEDED POTENCIAL** se 1:1 | `specs/features/primeiro-acesso/api.md` |
| Relação colaborador ↔ organização | FKs em COLABORADOR (AS-IS) + N TO-BE não modelado | 1 conjunto de FKs obrigatórias (exceto equipe) | **COMPATÍVEL** com AS-IS físico; **CONFLITANTE** com o TO-BE da DEC | DDL + P1 |

### 7.3 Supersession — se vier a ocorrer, será PARCIAL

**FATO.** Não se deve marcar a DEC-FA-003 inteira como superseded.

| Permaneceria vigente (se 1:1 for aprovado) | Exigiria revisão / supersession parcial |
|--------------------------------------------|-----------------------------------------|
| P2 Contexto Ativo | P1 N vínculos |
| P3 Navegação no contexto | P4 RN-SESSION-003 (fica vacuamente verdadeira ou morta) |
| P5 Sem contexto em `AUTH_SESSAO` | P6 rejeição de 1 vínculo em COLABORADOR |
| Bloqueio com 0 vínculos válidos (DEC-FA-001/002) | P8 “multi-contexto” como N pertinências |
| Auto-seleção com 1 vínculo (RN-SESSION-002) | Interpretação de OQ-008 “N áreas aprovado” |

**DECISÃO PENDENTE:** somente decisão humana pode supersedir parcialmente a DEC-FA-003. Esta reconciliação **não** o faz.

---

## 8. Avaliação de VINCULO_ORGANIZACIONAL

Questão central: **ainda é tecnicamente necessário?**

### 8.1 Alternativa A — FKs diretamente em COLABORADOR

```text
COLABORADOR
  ├── COD_FEDERACAO NOT NULL
  ├── COD_SINGULAR  NOT NULL   ← mudança vs AS-IS (hoje NULL)
  ├── COD_AREA      NOT NULL   ← mudança vs AS-IS (hoje NULL)
  └── COD_EQUIPE    NULL
```

| Critério | Avaliação |
|----------|-----------|
| Consistência com AS-IS | **Alta** — já é o modelo físico |
| Cardinalidade 1:1 | **Nativa** — uma coluna não admite N |
| Normalização | Desnormalização controlada (FKs no dono); padrão já usado por DEC-DB-015 para equipe |
| Integridade referencial | FKs existem; **falta** coerência hierárquica (GAP) |
| Simplicidade | Máxima |
| Evolução futura para N | **Custo alto** — exigiria migração posterior para tabela associativa |
| Impacto Oracle | `ALTER` de nullability + possível backfill; sem `CREATE TABLE` nova |
| Impacto JPA/API/FE/testes | Ajuste de obrigatoriedade; contrato já é um objeto |
| Risco de overengineering | Baixo |

### 8.2 Alternativa B — entidade VINCULO_ORGANIZACIONAL

```text
COLABORADOR 1 ── 1 VINCULO_ORGANIZACIONAL
                  ├── FEDERAÇÃO
                  ├── SINGULAR
                  ├── ÁREA
                  └── EQUIPE opcional
```

| Critério | Avaliação |
|----------|-----------|
| Consistência com AS-IS | **Baixa** — tabela **não existe**; duplicaria o que já está em COLABORADOR |
| Cardinalidade 1:1 | Exigiria UK `COD_COLABORADOR` — equivalente a colunas no pai |
| Normalização | Forma mais “entidade de relacionamento”; benefício nulo se 1:1 sem histórico |
| Integridade | Poderia concentrar CHECKs hierárquicos; os mesmos CHECKs cabem na Alternativa A |
| Simplicidade | Pior: JOIN extra, JPA extra, API extra, seeds extra |
| Evolução para N | **Melhor** se N for requisito confirmado (basta relaxar UK) |
| Impacto Oracle/JPA/API/FE/testes | Alto: nova tabela, migration, rewrite de `/auth/me`, cadastro, sessão |
| Risco de overengineering | **Alto** sob cardinalidade 1:1 |

### 8.3 Quando B se justificaria

**RECOMENDAÇÃO (não decisão):** B só se um destes requisitos for **confirmado por decisão humana**:

1. Manter DEC-FA-003 P1 (N pertinências) — então A é estruturalmente incapaz.
2. Histórico / vigência de vínculos (`DAT_INICIO`/`DAT_FIM`) como requisito explícito — **não** encontrado no AS-IS.
3. Vínculo como agregado com ciclo de vida independente do colaborador.

Nenhum desses três está implementado. (1) está apenas em governança.

### 8.4 Recomendação técnica

**RECOMENDAÇÃO:**

1. Se a nova cardinalidade **1 colaborador → 1 vínculo** for aprovada: **Alternativa A**. Não criar `VINCULO_ORGANIZACIONAL`. Endurecer nullability e, em etapa posterior de implementação, constraints/validações hierárquicas.
2. Se DEC-FA-003 P1 for **mantida integralmente**: **Alternativa B** (ou tabela associativa equivalente) torna-se necessária; as FKs atuais em COLABORADOR seriam legado a migrar. Isso **não** é a nova premissa.
3. Não criar entidade 1:1 “por se a DEC-FA-003 menciona N contextos”. A menção normativa, sozinha, não materializa necessidade física enquanto P1 não for reafirmada.

**DECISÃO PENDENTE:** escolha A vs B e destino de DEC-FA-003 P1.

---

## 9. Avaliação das FKs de COLABORADOR

Regra a validar: exatamente 1 Federação, 1 Singular, 1 Área, 0..1 Equipe.

### 9.1 Coluna a coluna

| Coluna | AS-IS | TO-BE a validar | Entidade | FK existe? | Coerência hierárquica | Constraint adicional |
|--------|-------|-----------------|----------|------------|----------------------|----------------------|
| `COD_FEDERACAO` | Obrigatória | Obrigatória | `FEDERACAO` | Sim | Raiz | Já NOT NULL |
| `COD_SINGULAR` | Opcional | Obrigatória | `SINGULAR` | Sim | Deve pertencer à federação do colaborador | **GAP:** sem CHECK/`federationId` no domain service |
| `COD_AREA` | Opcional | Obrigatória | `AREA` | Sim | Deve pertencer à singular do colaborador | **GAP:** app valida área→singular só no CRUD admin; `AREA.COD_SINGULAR` é nullable |
| `COD_EQUIPE` | Opcional | Opcional (0..1) | `EQUIPE` | Sim | Se preenchida, deve pertencer à área | App valida equipe→área no CRUD admin; sem CHECK Oracle |
| `COD_GESTOR` | Opcional | Fora da premissa de vínculo | `COLABORADOR` | Sim | Pessoa, não cargo (DEC-DB-027) | Auto-FK; não é vínculo org |

### 9.2 Relação entre as quatro colunas

**FATO.** São um **único vetor** de posicionamento, não quatro vínculos independentes. Preencher equipe sem área é rejeitado no CRUD admin (a área é inferida da equipe). Federação **não** entra nesse resolve.

**CONFLICT potencial com DEC-DB-020 / `locateOrCreate`:** login cria colaborador com `COD_SINGULAR`/`COD_AREA`/`COD_EQUIPE` nulos. Sob a nova premissa isso seria colaborador **inválido** (0 vínculo completo), alinhado a DEC-FA-001/002 (bloqueio operacional), mas **desalinhado** da criação automática atual.

**DECISÃO PENDENTE:** a obrigatoriedade de Singular+Área vale para **todo** colaborador (incluindo o recém-autenticado) ou só para colaborador **operacional** (DEC-FA-002)? A nova premissa diz “obrigatório”; o AS-IS distingue federativo vs operacional (`03-physical-model.md`).

### 9.3 Integridade hierárquica EQUIPE → ÁREA → SINGULAR → FEDERAÇÃO

| Garantia | Oracle | Aplicação | Resultado |
|----------|--------|-----------|-----------|
| Singular ∈ Federação do colaborador | Não | Não | **GAP** |
| Área ∈ Singular do colaborador | Não | Parcial (CRUD admin) | **GAP** |
| Equipe ∈ Área do colaborador | Não | Parcial (CRUD admin) | **GAP** |
| Área ∈ alguma Singular (DEC-ORG-001) | Não (`AREA.COD_SINGULAR` NULL) | Área pode ser federativa | **CONFLICT** DEC-ORG-001 vs DEC-DB-022/modelo físico |

**Não corrigido** (escopo analítico).

---

## 10. Reconciliação VÍNCULO × CARGO × PAPEL

### 10.1 Separação conceitual (referência DEC-ORG-002 e DEC-DB-027)

| Eixo | Significado | Persistência AS-IS | Persistência TO-BE já decidida |
|------|-------------|--------------------|--------------------------------|
| **Vínculo** | Onde o colaborador está na estrutura | FKs em `COLABORADOR` | **DECISÃO PENDENTE** (esta reconciliação) |
| **Cargo** | Qual função organizacional ocupa | Inexistente (`DES_CARGO` removido) | DEC-DB-027: `CARGO` + `COLABORADOR.COD_CARGO NOT NULL` — **não implementado** |
| **Papel** | Qual autorização/responsabilidade possui | `PAPEL` + `PAPEL_ATRIBUICAO` (sem uso runtime) | 1 COLABORADOR → N atribuições (já no modelo lógico) |

### 10.2 Validações pedidas

| Afirmação | Resultado | Evidência |
|-----------|-----------|-----------|
| CARGO ≠ PAPEL | **COMPATÍVEL** | DEC-ORG-002.4; DEC-DB-027.5 |
| CARGO ≠ ADMIN_* | **COMPATÍVEL** | DEC-ORG-002.5 |
| VÍNCULO ≠ CARGO | **COMPATÍVEL** | DEC-DB-027: cargo não vai no vínculo; PD-CARGO-03 Hipótese A |
| VÍNCULO ≠ PAPEL | **COMPATÍVEL** | DEC-DB-020: FKs cadastrais ≠ `PAPEL_ATRIBUICAO` |
| Cargo não concede ADMIN_* | **COMPATÍVEL** | DEC-ORG-002.6; exemplo Vicente / Gestor TI |
| Papel não altera o vínculo | **COMPATÍVEL** | Escopos de `PAPEL_ATRIBUICAO` são colunas próprias, não atualizam `COLABORADOR.COD_*` |
| COLABORADOR → 1 CARGO obrigatório | **COMPATÍVEL** com DEC-DB-027; **GAP** de implementação | Sem tabela/coluna/API/FE |

**FATO.** Não há contradição entre DEC-ORG-002, DEC-DB-027 e a nova regra no eixo cargo. A nova regra **reafirma** o já aprovado.

**CONFLICT residual (eixo vínculo, não cargo):** DEC-DB-027 ainda documenta *"1 COLABORADOR → N vínculos (DEC-FA-003)"* como eixo ortogonal ao cargo, e cita `VINCULO_ORGANIZACIONAL` como opção TO-BE **fora** da própria DEC-DB-027. Isso não contradiz 1 cargo; contradiz a **nova** cardinalidade de vínculo.

---

## 11. Reconciliação de PAPEL 1..N

Nova regra: `COLABORADOR → 1..N PAPÉIS`, mínimo obrigatório `COLABORADOR`. Premissa vigente: `ADMIN_*` independentes; sem herança.

### 11.1 Respostas pontuais

| # | Questão | Resposta | Classificação |
|---|---------|----------|---------------|
| 1 | `PAPEL_ATRIBUICAO` suporta 1..N? | Sim: N linhas por colaborador; só PK surrogate | **FATO** |
| 2 | Mesmo colaborador pode ter múltiplos papéis? | Sim, estruturalmente; runtime não lê a tabela | **FATO** + **GAP** |
| 3 | Mesmo papel em múltiplos escopos? | Sim: sem UK; FKs de escopo independentes. Ex. conceitual DEC-ORG-002 (ADMIN_AREA × 2 áreas) cabe aqui **sem** N vínculos | **FATO** |
| 4 | `COLABORADOR` como papel mínimo? | Seed já tem `NOM_PAPEL = 'COLABORADOR'`. Não há atribuição automática, CHECK, nem API | **FATO** (catálogo) + **GAP** (obrigatoriedade) + **DECISÃO PENDENTE** (promover a RN) |
| 5 | `ADMIN_*` permanecem independentes? | Em governança (DEC-ORG-002) sim. No banco **não existem** esses códigos; existe `ADMINISTRADOR` genérico | **GAP** de catálogo |
| 6 | Existe herança entre `ADMIN_*`? | Não no DDL, JPA, seeds ou regras. OQ-012 é herança de **pastas**, não de papéis | **FATO** |
| 7 | Escopo do papel representado corretamente? | Colunas de escopo existem; sem XOR; todos NULL = papel global (`03-physical-model.md`). BR-028 pede escopo válido — **tensão** com papel global | **GAP** / **CONFLICT** documental BR-028 vs observação física |
| 8 | Gaps domínio × banco × backend × frontend | Ver §11.2 | **GAP** |

### 11.2 Gaps entre camadas

```text
Domínio (BR-027, BR-034, DEC-ORG-002 ADMIN_*)
    ≠  Seed (ADMINISTRADOR, GESTOR_DOCUMENTAL, EDITOR, COLABORADOR)
    ≠  Runtime (permissions=[], sem JPA, admin por e-mail)
    ≠  Frontend (roles? opcional, guard desligado)
```

**FATO.** Não inferir que `ADMIN_AREA` concede `ADMIN_SINGULAR` ou `ADMIN_FEDERACAO`. Não há código de composição/herança. Hierarquia organizacional (FKs de vínculo) **não** é hierarquia de papéis.

### 11.3 Relação com DEC-FA-003

N papéis **não** exigem N vínculos. `PAPEL_ATRIBUICAO` já é o lugar de N escopos de autorização. Reconciliar 1..N papéis com DEC-FA-003:

- **COMPATÍVEL** se DEC-FA-003 for lida como “N contextos de **autorização/operação de papel**”.
- **CONFLITANTE** se DEC-FA-003 P1 for lida literalmente como “N pertinências cadastrais”.
- A nova regra de papel mínimo **não** conflita com P2–P5.

**RECOMENDAÇÃO:** tratar papel mínimo `COLABORADOR` como regra de **autorização** (linha em `PAPEL_ATRIBUICAO`, possivelmente escopo global ou igual ao vínculo), não como substituto do vínculo.

---

## 12. Matriz de divergências

| Conceito | AS-IS | TO-BE a validar | DEC relacionada | Resultado | Classificação |
|----------|-------|-----------------|-----------------|-----------|---------------|
| Federação | Entidade + FK NOT NULL em COLABORADOR | 1 obrigatória no vínculo | DEC-ORG-001, DEC-DB-001/002 | Alinhado | **FATO** |
| Singular | FK opcional; `AREA.COD_SINGULAR` nullable | 1 obrigatória no vínculo | DEC-ORG-001, DEC-DB-020 | Nullability conflita | **CONFLICT** + **GAP** hierárquico |
| Área | FK opcional; operacional exige área (BR-010) | 1 obrigatória | DEC-FA-002, DEC-ORG-001 | Norma vs schema | **GAP** |
| Equipe | FK opcional; 0..1 físico | 0..1 | DEC-DB-015, OQ-008 | Alinhado a 0..1; OQ-008 aberta | **FATO** / **DECISÃO PENDENTE** OQ-008 |
| Vínculo | Colunas em COLABORADOR; 0..1 conjunto | 1 conjunto obrigatório completo | DEC-DB-016, DEC-FA-003 | Estrutura 1:1 sim; N normativo não | **FATO** + **CONFLICT** |
| Colaborador | Identidade + FKs org opcionais (exceto federação) | 1 cargo + 1 vínculo + 1..N papéis | DEC-DB-016/020/027 | Cargo e papéis: gaps; vínculo: decisão | **GAP** |
| Cargo | Inexistente | 1 obrigatório | DEC-ORG-002, DEC-DB-027 | Já decidido; não implementado | **FATO** / **GAP** impl. |
| Papel | Catálogo seed 4 nomes; sem runtime | 1..N; mínimo COLABORADOR | BR-027, DEC-ORG-002 | Estrutura 1..N ok; mínimo não enforced | **FATO** + **GAP** |
| Papel/Atribuição | Tabela 1..N com escopos nullable | 1..N atribuições; ADMIN_* independentes | DEC-DB-020 | Compatível; ADMIN_* ausentes no seed | **GAP** catálogo |
| Contexto ativo | Derivado do único `organizationalLinks` | Com 1 vínculo = o vínculo | DEC-FA-003 P2–P3 | Compatível | **FATO** |
| `organizationalLinks` | Objeto único em `/auth/me` | Suffice se 1:1 | FT-AUTH / FT-SESSION | Compatível com 1:1 | **FATO** |
| `contexts[]` | Não implementado | Desnecessário se 1:1 | FT-PRIMEIRO-ACESSO | Superseed potencial | **INFERÊNCIA** condicional |
| `COD_FEDERACAO` | NOT NULL | NOT NULL | DEC-DB-002/020 | Alinhado | **FATO** |
| `COD_SINGULAR` | NULL permitido | NOT NULL | DEC-DB-020 vs nova regra | Conflito de obrigatoriedade | **CONFLICT** |
| `COD_AREA` | NULL permitido | NOT NULL | DEC-DB-020 vs DEC-FA-002 | Conflito schema vs operação | **CONFLICT** / **GAP** |
| `COD_EQUIPE` | NULL permitido | NULL permitido | DEC-DB-015 | Alinhado | **FATO** |
| `VINCULO_ORGANIZACIONAL` | Inexistente | Só se N ou histórico | DEC-FA-003, DEC-DB-027 (fora de escopo) | Não necessário para 1:1 | **RECOMENDAÇÃO** |
| `ADMIN_*` | Só no texto DEC-ORG-002 | Papéis independentes | DEC-ORG-002, OQ-020 | Catálogo não reconciliado | **GAP** / **DECISÃO PENDENTE** |

---

## 13. Decisões afetadas

Nenhuma decisão foi alterada. Classificação abaixo é **analítica**.

| Decisão | Situação se a nova premissa for aprovada | Situação se for rejeitada (mantém N vínculos) |
|---------|------------------------------------------|-----------------------------------------------|
| **DEC-FA-003** | **Parcialmente superseded** (P1, P4, P6, P8). P2, P3, P5 **mantidas** | **Mantida** integralmente; exige modelo N (Alternativa B) |
| **DEC-ORG-002** | **Mantida** — cargo ≠ papel ≠ ADMIN_* | **Mantida** |
| **DEC-DB-027** | **Mantida** no cargo; **complementar** no parágrafo que cita N vínculos / `VINCULO_ORGANIZACIONAL` | **Mantida**; N vínculos continua PD explícito fora da DEC |
| **DEC-DB-015** | **Mantida** — gestor/líder por FK; equipe 0..1 em COLABORADOR | **Mantida** |
| **DEC-DB-016** | **Mantida** nas FKs existentes; **complementar** se Singular/Área virarem NOT NULL (quebra o congelamento 2026-07-10 nesse ponto) | **Mantida** para AS-IS |
| **DEC-DB-020** | **Complementar / conflito parcial**: FKs org ≠ papel **permanece**; login com FKs NULL **conflita** com vínculo obrigatório completo | **Mantida** |
| **DEC-ORG-001** | **Mantida** (hierarquia + área obrigatória operacional). **GAP** `AREA.COD_SINGULAR` NULL permanece | **Mantida** |
| **DEC-FA-002** | **Mantida** (operação exige área). Redação “pelo menos um” continua verdadeira se máximo = 1 | **Mantida** |
| **DEC-FA-001** | **Complementar**: ramo N>1 torna-se inaplicável; ramos 0 e 1 permanecem | **Mantida** (ramo N necessário) |
| **DEC-DB-002** | **Complementar**: “vínculos dependem do contexto” vs sempre completo | **Mantida** |
| **DEC-DB-022** | Não afetada no núcleo (área nível único); tensão com área sem singular | Não afetada |

**Não afetadas (no mérito desta premissa):** DEC-DB-011, 013, 018, 019, 023, 024; DEC-CMS-001; REF-DB-CTX-01.

**Não criar:** DEC-DB-028.

---

## 14. Decisões ainda pendentes

Antes de qualquer DEC-DB-028, decisão **humana** precisa fechar:

| ID | Pergunta | Por que bloqueia |
|----|----------|------------------|
| PD-VINC-01 | A DEC-FA-003 P1 (N pertinências) permanece, é superseded **parcialmente**, ou é reinterpretada como N papéis/escopos? | Define se Alternativa A é lícita |
| PD-VINC-02 | Todo colaborador tem vínculo completo (fed+singular+área), ou apenas o **operacional** (DEC-FA-002), permitindo federativo só com federação? | Define NOT NULL de `COD_SINGULAR`/`COD_AREA` e o destino de `locateOrCreate` |
| PD-VINC-03 | OQ-008: 0..1 equipe (nova premissa) ou N equipes na mesma área? | Define se `COD_EQUIPE` único basta |
| PD-VINC-04 | Papel mínimo `COLABORADOR` é RN de criação (atribuição automática) e/ou CHECK? Escopo global ou igual ao vínculo? | Define seeds, JPA e `/auth/me.roles` |
| PD-VINC-05 | Catálogo `ADMIN_*` substitui, convive ou é alias de `ADMINISTRADOR` do seed? Matriz OQ-020? | Sem isso, 1..N papéis não tem vocabulário único |
| PD-VINC-06 | Integridade hierárquica: CHECK Oracle, só aplicação, ou ambos? | GAP atual não é decisão |
| PD-VINC-07 | Com 1:1, FT-PRIMEIRO-ACESSO perde a UI de seleção N>1? Contexto Ativo permanece como conceito de sessão? | Impacto de specs PA/SESSION sem tocar DEC-FA-003 agora |
| PD-CARGO (já DEC-DB-027) | Implementação de `CARGO` — **não** reabrir cardinalidade 1:1 cargo | Ortogonal; não misturar com DEC-DB-028 |

---

## 15. Recomendações técnicas

Todas as linhas abaixo são **RECOMENDAÇÃO**, não decisão.

1. **Não criar `VINCULO_ORGANIZACIONAL`** enquanto a cardinalidade aprovada for 1:1. As FKs de `COLABORADOR` já são o vínculo.
2. **Não inferir N pertinências** a partir de N papéis. Manter DEC-DB-020: cadastro ≠ autorização.
3. **Se P1 da DEC-FA-003 for reafirmada**, aí sim modelar tabela associativa; as FKs atuais não suportam N.
4. **Se 1:1 for aprovado**, formalizar **supersession parcial** da DEC-FA-003 (não total) numa DEC futura (DEC-DB-028 ou DEC-FA-00x), preservando Contexto Ativo e REF-DB-CTX-01.
5. Tratar `COLABORADOR` (seed) como candidato a papel mínimo; **não** implementar atribuição automática até PD-VINC-04.
6. Não mapear `ADMINISTRADOR` (seed) para `ADMIN_FEDERACAO` por conveniência. Catálogo administrativo é PD-VINC-05.
7. Não endurecer NOT NULL de Singular/Área no DDL até PD-VINC-02 (colaborador federativo vs operacional).
8. Não usar esta reconciliação como autorização para migration, JPA ou alteração de DEC-FA-003.

---

## 16. Conclusão

1. **O AS-IS suporta COLABORADOR → exatamente 1 vínculo?**  
   **FATO:** suporta **no máximo 1** conjunto de FKs. **GAP:** não exige que o conjunto esteja completo (Singular/Área podem ser NULL).

2. **O modelo atual de FKs em COLABORADOR é suficiente para representar esse vínculo?**  
   **FATO:** sim, para cardinalidade 1:1. É exatamente assim que o schema, o JPA, a API e o `/auth/me` já funcionam.

3. **`VINCULO_ORGANIZACIONAL` ainda é tecnicamente necessário?**  
   **RECOMENDAÇÃO:** **não**, se 1:1 for o alvo. **Sim**, se N pertinências (DEC-FA-003 P1) forem reafirmadas. A existência da DEC-FA-003 **não** cria a tabela.

4. **Quais partes da DEC-FA-003 estão realmente em conflito?**  
   P1 (N vínculos), P6 (rejeição de 1 vínculo em COLABORADOR), P8 no sentido de multi-pertinência. P4 fica inaplicável.

5. **Quais partes poderiam permanecer vigentes?**  
   P2 Contexto Ativo, P3 navegação no contexto, P5 sem `COD_*_CTX`. RN-SESSION-002 (auto-seleção com 1 vínculo) torna-se o único caminho feliz.

6. **COLABORADOR → 1..N PAPÉIS é compatível com o modelo atual?**  
   **FATO:** sim, via `PAPEL_ATRIBUICAO`. **GAP:** sem uso runtime.

7. **O papel COLABORADOR pode ser tratado como papel mínimo obrigatório?**  
   **FATO:** o nome já existe no catálogo seed. **DECISÃO PENDENTE** promovê-lo a RN e persistir atribuição. Não há herança a partir dele.

8. **Existe contradição entre DEC-ORG-002, DEC-DB-027 e a nova regra?**  
   **No eixo cargo/papel: não.** A nova regra alinha-se. **No eixo vínculo:** DEC-DB-027 ainda referencia N vínculos DEC-FA-003 como TO-BE separado — conflito com a **nova** 1:1, não entre ORG-002 e DB-027.

9. **Quais decisões humanas faltam antes da DEC-DB-028?**  
   PD-VINC-01 a PD-VINC-07 (§14), em especial o destino de DEC-FA-003 P1 e a obrigatoriedade de Singular/Área para não-operacionais.

10. **Recomendação técnica (não é decisão):**  
    Adotar **Alternativa A** (FKs em `COLABORADOR`) **condicional** à aprovação humana de 1:1 e à supersession **parcial** da DEC-FA-003. Representar N responsabilidades em `PAPEL_ATRIBUICAO`, com papel mínimo `COLABORADOR` como RN futura. Não criar entidade `VINCULO_ORGANIZACIONAL` só para envelopar um 1:1 já persistido.

---

## 17. Critérios para futura DEC-DB-028

A DEC-DB-028 **só deve ser redigida** depois que um decisor humano registrar, no mínimo:

| Critério | Conteúdo mínimo da DEC futura |
|----------|-------------------------------|
| Cardinalidade de pertinência | 1:1 **ou** N, com referência explícita a DEC-FA-003 (manter / supersedir parcial) |
| Persistência do vínculo | Alternativa A (FKs) **ou** Alternativa B (tabela), com justificativa de não-overengineering |
| Nullability | `COD_SINGULAR` / `COD_AREA` NOT NULL para quem; exceção federativa ou não |
| Equipe | Confirmar 0..1 e fechar OQ-008 |
| Papel 1..N | Confirmar `PAPEL_ATRIBUICAO` como SSOT; papel mínimo `COLABORADOR` sim/não e momento da atribuição |
| `ADMIN_*` | Relação com seed `ADMINISTRADOR`; ausência de herança |
| Fora de escopo | Não reabrir DEC-ORG-002 / DEC-DB-027 (cargo); não reintroduzir `COD_*_CTX` |
| Implementação | A DEC de governança **não** executa DDL nesta etapa; plano de implementação posterior |

**Critério de recusa:** qualquer texto que aprove `VINCULO_ORGANIZACIONAL` apenas porque “a DEC-FA-003 fala em N contextos”, sem PD-VINC-01 resolvido, deve ser rejeitado como formalização prematura.

---

## Apêndice A — Confirmação de não-implementação

| Item | Status |
|------|--------|
| Código backend/frontend | Não alterado |
| DDL / migrations / seeds | Não alterados |
| JPA / API / testes | Não alterados |
| DEC-FA-003 e demais decisões | Não alteradas |
| DEC-DB-028 | **Não criada** |
| Único artefato produzido | Este relatório |

---

## Apêndice B — Mapa rápido AS-IS (evidência-chave)

```text
COLABORADOR (AS-IS)
  COD_FEDERACAO  NOT NULL  ──► FEDERACAO
  COD_SINGULAR   NULL      ──► SINGULAR
  COD_AREA       NULL      ──► AREA
  COD_EQUIPE     NULL      ──► EQUIPE
  COD_GESTOR     NULL      ──► COLABORADOR
  COD_CARGO      (não existe)
  ── 1..N PAPEL_ATRIBUICAO (tabela existe; runtime não usa)
         COD_PAPEL + escopos nullable

PAPEL seed: ADMINISTRADOR | GESTOR_DOCUMENTAL | EDITOR | COLABORADOR
ADMIN_FEDERACAO / ADMIN_SINGULAR / ADMIN_AREA / ADMIN_EQUIPE: só governança

GET /auth/me.organizationalLinks = { federationId, singularId, areaId, teamId }  // 1 objeto
GET /auth/me.permissions = []
GET /auth/me.roles = (campo backend inexistente)

VINCULO_ORGANIZACIONAL = não existe
```
