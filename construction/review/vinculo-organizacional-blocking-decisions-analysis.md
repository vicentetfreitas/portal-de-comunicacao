# Análise das decisões bloqueantes — proposta DEC-DB-028

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/vinculo-organizacional-blocking-decisions-analysis.md` |
| Data | 2026-08-14 |
| Tipo | Análise de governança — **sem decisão de negócio** |
| Status | **EVIDÊNCIA PARA DECISÃO HUMANA** |
| Decisão alvo | DEC-DB-028 (proposta, **não aprovada**) |

**Classificação usada:** `FATO` · `INFERÊNCIA` · `HIPÓTESE` · `RECOMENDAÇÃO TÉCNICA`.

**Restrições cumpridas:** nenhum código, DDL, migration, JPA, API, frontend, teste, seed, decisão ou artefato existente foi alterado. DEC-DB-028 **não** foi criada/aprovada. DEC-FA-003 **não** foi alterada.

---

## 1. Objetivo

Subsidiar a decisão humana sobre as **quatro questões bloqueantes** (DH-01 a DH-04) identificadas em `construction/review/vinculo-organizacional-decision-proposal.md`, para eventual aprovação ou rejeição da proposta **DEC-DB-028 — Modelo de vínculo organizacional único do COLABORADOR**.

Esta análise produz **somente** fatos, evidências, impactos, inconsistências, dependências, riscos e consequências de alternativas. **Não toma decisões de negócio.**

---

## 2. Estado de governança

| Premissa | Status |
|----------|--------|
| DEC-DB-028 | Proposta — **não aprovada** |
| DEC-FA-003 | **Vigente** — nenhuma supersession aprovada |
| DEC-ORG-002, DEC-DB-027, DEC-DB-015/016/020 | Vigentes — não alteradas |
| Implementação | **Não alterada** nesta etapa |
| Artefatos PD-02/03 de vínculo | **Ausentes** no repositório (ver §3) |

---

## 3. Fontes analisadas

### Prioridade 1 — artefatos de vínculo

| Fonte | Status |
|-------|--------|
| `construction/review/vinculo-organizacional-decision-proposal.md` | Encontrada |
| `construction/review/vinculo-organizacional-reconciliation-cardinality-reassessment.md` | Encontrada |

### Prioridade 2 — governança e modelo

| Fonte | Uso |
|-------|-----|
| `docs/governance/03-open-decisions.md` | DEC-FA-001, 002, 003; DEC-ORG-001, 002 |
| `database/model/05-decisions-and-risks.md` | DEC-DB-015, 016, 020, 027 |
| `database/model/03-physical-model.md` | Nullability; colaborador federativo |
| `database/ddl/003-create-tables.sql`, `004-create-constraints.sql` | Schema COLABORADOR |

### Prioridade 3 — código e contratos

| Fonte | Uso |
|-------|-----|
| `ColaboradorEntity.java`, `ColaboradorService.java` | `locateOrCreate`; FKs |
| `ColaboradorApplicationService.java`, `ColaboradorDomainService.java` | CRUD admin; validação hierárquica parcial |
| `AuthenticationService.java`, `JwtTokenService.java` | Login; `/auth/me`; claims JWT |
| `AuthenticatedUserResponse.java`, `ColaboradorOrganizationalLinksResponse.java` | Contrato API |
| `frontend/src/stores/session.store.ts`, `frontend/src/auth/types.ts` | Sessão; `organizationalLinks` |
| `ColaboradorTestBuilder.java`, `IntegrationTestDatabaseCleaner.java` | Fixtures federation-only |
| `frontend/test/e2e/support/auth-mock.ts` | Mock com `singularId`/`areaId` null |

### Prioridade 4 — specs e domínio

| Fonte | Uso |
|-------|-----|
| `specs/features/session/specification.md` | RN-SESSION-002/003; multi-contexto |
| `specs/features/primeiro-acesso/*` | Fluxos N=0/1/N>1; `contexts[]` proposto |
| `specs/features/authentication/api.md` | `/auth/me` |
| `docs/domain/09-business-rules.md` | BR-009..012, BR-041 |
| `construction/features/FT-PRIMEIRO-ACESSO/execution-plan.md` | CDD-PA-01 N vínculos |

### Ausentes (não inventados)

| Fonte solicitada | Impacto na análise |
|------------------|-------------------|
| `vinculo-organizacional-reconciliation-pd-02-03.md` | **Ausente.** DEC-DB-027 cita PD-02/03 no eixo vínculo N; não há evidência adicional além de `05-decisions-and-risks.md`. **Não bloqueia** DH-02/03/04; **complementa incerteza** em DH-01 sobre histórico de deliberação PD-02/03 |
| `vinculo-organizacional-reconciliation-pd-02-03-complementar.md` | Idem |

---

## 4. DH-01 — Supersession parcial da DEC-FA-003

### 4.1 Texto vigente da DEC-FA-003 (fatos)

**FATO** (`docs/governance/03-open-decisions.md`, aprovada 2026-07-24):

1. Um colaborador pode possuir **N vínculos** organizacionais.
2. A sessão possui um **Contexto Ativo** (`federationId`, `singularId`, `areaId`).
3. Toda navegação operacional utiliza o Contexto Ativo.
4. **RN-SESSION-003** torna-se oficial: seleção quando N > 1.
5. Persistência do Contexto Ativo em FT-PRIMEIRO-ACESSO / FT-SESSION — **sem** `COD_*_CTX` em `AUTH_SESSAO` (REF-DB-CTX-01).
6. Alternativa rejeitada: permanecer fase 1 (1 vínculo em `COLABORADOR`).
7. Encerra OQ-027; atualiza OQ-008 (N áreas aprovado).

### 4.2 Matriz de reconciliação

| Regra/artefato | Evidência atual | Compatível com 1:1? | Impacto se 1:1 for adotado | Observação |
|----------------|-----------------|---------------------|----------------------------|------------|
| **DEC-FA-003 §1 — N vínculos** | Decisão vigente; BR-041; `session/specification.md` L33; `primeiro-acesso/specification.md` RF-PA-003; `FT-PRIMEIRO-ACESSO/execution-plan.md` CDD-PA-01 | **Não** | Regra normativa central torna-se inválida; exige supersession parcial | **FATO** conflito normativo |
| **Alternativa rejeitada — 1 vínculo em COLABORADOR** | DEC-FA-003 tabela "Alternativas consideradas" | **Sim** (seria restaurada) | Inversão explícita de alternativa rejeitada | Supersession parcial obrigatória |
| **Título "Multi-contexto" como N pertinências** | Título e P8 da reconciliação anterior | **Não** no sentido N cadastral | Redefinição semântica necessária | **HIPÓTESE:** "multi-contexto" poderia ser reinterpretado como N papéis — não é texto da DEC-FA-003 |
| **RN-SESSION-003 — seleção N>1** | `session/specification.md` L27; `primeiro-acesso/flows.md` ramo `SelectingContext`; `frontend-flow.md` | **Não** (regra morta) | UI e specs de seleção perdem fundamento | Implementação **não existe** — impacto documental > código |
| **P7 — "N áreas aprovado" (OQ-008)** | DEC-FA-003 §Encerra; `10-open-questions.md` OQ-008 parcial | **Não** para N áreas | Ramo de OQ-008 sobre N áreas precisa revisão | Equipe por área permanece aberta |
| **`contexts[]` TO-BE** | `primeiro-acesso/api.md` PA-API-001; `execution-plan.md` CDD-PA-04 | **Não** (desnecessário ou trivial) | Endpoints propostos `/session/contexts` perdem sentido | **FATO:** não implementado |
| **Contexto Ativo (P2)** | DEC-FA-003 §2; BR-012; `session.store` `activeContext` | **Sim** | Com 1:1, contexto ativo = único vínculo | Nenhuma mudança conceitual |
| **Navegação no Contexto Ativo (P3)** | BR-010; DEC-FA-002 | **Sim** | Mantida | — |
| **REF-DB-CTX-01 — sem COD_*_CTX** | `AuthSessaoEntity.java` comentário; DDL `AUTH_SESSAO`; V006 migration README | **Sim** | Independente de cardinalidade | **FATO** implementado |
| **`organizationalLinks` (fase 1)** | `/auth/me` retorna **um** objeto; `AuthenticatedUserResponse` | **Sim** | Já é modelo 1:1 na API | **FATO** |
| **RN-SESSION-002 — auto-seleção 1 vínculo** | `session.store.ts` L82-86; DEC-FA-001 ramo 1 | **Sim** | Torna-se **único** caminho feliz | Código já implementa |
| **Bloqueio 0 vínculos (DEC-FA-001/002)** | `primeiro-acesso/flows.md` N=0 → Block; BR-010 | **Sim** | Colaborador só-federação = 0 vínculos **válidos** se área obrigatória | Acoplamento com DH-03/DH-04 |
| **Persistência Contexto Ativo (FT-PA)** | `use-cases.md` UC-PA-005; INC-PA-004 lacuna física | **Sim** (simplifica) | Sem lista N, persistência pode ser o próprio vínculo ou estado de sessão | Mecanismo físico **ainda não definido** |
| **BR-041** | `09-business-rules.md` L53 | **Não** | Derivada de DEC-FA-003 P1 — precisaria complemento/supersession | Documento de domínio afetado |
| **DEC-DB-027 §Vínculo N** | `05-decisions-and-risks.md` L507 | **Não** no eixo vínculo | Referência cruzada desatualizada se 1:1 | Cargo 1:1 **não** afetado |
| **Implementação backend N vínculos** | Busca: zero endpoint `/session/contexts`; zero tabela N:N | **N/A** | Impacto de supersession é **governança + specs**, não código N | **FATO** |
| **Testes N vínculos** | Nenhum teste de múltiplos vínculos encontrado | **N/A** | Sem retrabalho de testes N | **FATO** |
| **Frontend seleção N>1** | `session.store` TODO OQ-027; sem tela SelectingContext | **N/A** | Specs/flows afetados; código não | **FATO** |

### 4.3 Documentos que referenciam partes potencialmente supersedidas

| Documento | Referência a N vínculos / seleção |
|-----------|-----------------------------------|
| `docs/governance/03-open-decisions.md` | DEC-FA-003 integral; DEC-FA-001 ramos N |
| `docs/domain/09-business-rules.md` | BR-011, BR-041 |
| `docs/domain/04-domain-concepts.md` | "N áreas possíveis" |
| `docs/frontend/frontend-flow.md` | Seleção N vínculos |
| `specs/features/session/specification.md` | RN-SESSION-003; seção Multi-contexto |
| `specs/features/primeiro-acesso/*` | RF-PA-003; flows SelectingContext; PA-API-001 |
| `construction/features/FT-PRIMEIRO-ACESSO/*` | CDD-PA-01; pkg-01 "Modelo N vínculos" |
| `docs/architecture/10-target-architecture.md` | "modelo N vínculos" |
| `database/model/05-decisions-and-risks.md` | DEC-DB-027 vínculo N |

**FATO.** Nenhuma implementação runtime depende de N vínculos cadastrais. A dependência é **normativa e de planejamento** (specs, construction, domain rules).

### 4.4 Consequências por alternativa (sem escolha)

| Se supersession parcial **não** for aprovada | Se supersession parcial **for** aprovada (hipotético) |
|---------------------------------------------|------------------------------------------------------|
| DEC-DB-028 (1:1) **conflita** com DEC-FA-003 vigente | DEC-FA-003 permanece histórica; itens P1/P4/P6/P8 precisam registro de supersession em DEC-DB-028 |
| Modelo físico atual (1 conjunto FK) permanece "fase 1" normativamente inconsistente | BR-041, specs PA/SESSION, execution-plan FT-PA precisam atualização futura |
| FT-PRIMEIRO-ACESSO continua planejado para N vínculos | CDD-PA-01 e PA-API-001 tornam-se obsoletos ou reduzidos |
| Nenhum retrabalho de código N (porque não existe) | Nenhum retrabalho de código N |

### 4.5 Classificação DH-01

| Tipo | Conteúdo |
|------|----------|
| **FATO** | DEC-FA-003 P1 e P6 conflitam textualmente com 1:1 |
| **FATO** | P2, P3, P5, REF-DB-CTX-01, `organizationalLinks` singular são compatíveis |
| **FATO** | Implementação N vínculos **não existe** |
| **INFERÊNCIA** | Supersession seria **parcial**, nunca total |
| **HIPÓTESE** | Stakeholders podem ter confundido N escopos de papel com N pertinências (DEC-DB-020 separa os eixos) — **não** substitui decisão sobre P1 |

**Decisão humana pendente.** Esta análise **não** recomenda aprovar ou rejeitar a supersession.

---

## 5. DH-02 — Cardinalidade 1:1 como política definitiva

### 5.1 Pergunta analítica

> Quais evidências do sistema sustentam ou contradizem a adoção definitiva de 1:1?

### 5.2 Persistência

| Evidência | Estado atual | Implicação para 1:1 | Risco |
|-----------|--------------|---------------------|-------|
| `COLABORADOR` tem `COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE` escalares | **FATO** DDL `003-create-tables.sql` L158-165 | Máximo **1** conjunto por linha — estrutura **suporta** 1:1 | — |
| Sem tabela associativa de vínculos | **FATO** busca `VINCULO_ORGANIZACIONAL` = 0 | Impossível N pertinências sem novo modelo | Adoção futura de N exigiria **ALTO** retrabalho |
| Sem UK que limite "1 vínculo" explicitamente | **FATO** | Cardinalidade 1:1 é **estrutural** (colunas únicas), não declarada por constraint nomeada | — |
| `COD_SINGULAR`/`COD_AREA` nullable | **FATO** | 1:1 **incompleto** é possível hoje | **RISCO** vs proposta "vínculo completo obrigatório" |
| JPA `ColaboradorEntity` espelha DDL | **FATO** `SchemaOracleAuditTest` | Alinhado | — |
| Possibilidade técnica de múltiplos vínculos | **FATO:** não sem novo schema | Contradiz DEC-FA-003 P1 **implementável** hoje | **CONFLICT** norma vs capacidade técnica |

### 5.3 Domínio

| Evidência | Estado atual | Implicação para 1:1 | Risco |
|-----------|--------------|---------------------|-------|
| BR-041 — N vínculos | **FATO** vigente | **Contradiz** 1:1 definitivo | Governança |
| BR-011 — auto-seleção 1 / escolha N | **FATO** | Ramo N **contradiz** 1:1 | Specs derivadas |
| BR-009/010 — operacional exige área | **FATO** | **Compatível** com 1 vínculo **completo** | Tensão com registro incompleto |
| DEC-ORG-001 — hierarquia + área obrigatória operacional | **FATO** | **Compatível** com 1:1 completo | — |
| `03-physical-model.md` — colaborador federativo só federação | **FATO** L715-716 | **Contradiz** Singular+Área obrigatórias para **todos** | Ver DH-04 |
| DEC-DB-027 — exemplo com 2 áreas de vínculo | **FATO** L515 | **Contradiz** 1:1 no exemplo canônico | Apenas documentação DEC-DB-027 |

### 5.4 Autenticação e sessão

| Evidência | Estado atual | Implicação para 1:1 | Risco |
|-----------|--------------|---------------------|-------|
| `locateOrCreate` cria só `COD_FEDERACAO` | **FATO** `ColaboradorService.java` L48-56 | Vínculo **incompleto** após login | **RISCO** operacional |
| `/auth/me` → um `organizationalLinks` | **FATO** `AuthenticationService` | **Sustenta** 1:1 na API | — |
| JWT claims `fid`/`sid`/`aid`/`tid` únicos | **FATO** `JwtTokenService` | **Sustenta** 1 conjunto | — |
| `session.store` promove único link a `activeContext` | **FATO** L79-86 | **Sustenta** 1:1 no FE | TODO OQ-027 indica spec N não implementada |
| FT-PRIMEIRO-ACESSO não implementado | **FATO** PA-API proposto | Fluxos N=0/1/N>1 são **especificação**, não runtime | — |
| Persistência contexto ativo | **FATO** INC-PA-004 — lacuna | Independente de N vs 1 | — |

### 5.5 API

| Evidência | Estado atual | Implicação para 1:1 | Risco |
|-----------|--------------|---------------------|-------|
| `AuthenticatedUserResponse` — objeto único | **FATO** | Compatível | — |
| `CreateColaboradorRequest` — um conjunto de IDs | **FATO** | Compatível | Singular/Área opcionais na API |
| PA-API-001 `GET /session/contexts` | **FATO** proposto, **não** implementado | Contradiz 1:1 se implementado como lista N | Nenhum impacto runtime hoje |
| CRUD colaborador substitui FKs (não adiciona vínculo) | **FATO** `ColaboradorApplicationService` update | Compatível com 1:1 | — |

### 5.6 Frontend

| Evidência | Estado atual | Implicação para 1:1 | Risco |
|-----------|--------------|---------------------|-------|
| Sem tela SelectingContext | **FATO** | Não há UX N | — |
| `auth-mock.ts` — `singularId`/`areaId` null | **FATO** | Assume vínculo **incompleto** possível | Testes E2E |
| `useColaboradorForm` — singular/area nullable | **FATO** | Cadastro admin permite incompleto | — |

### 5.7 Testes

| Padrão | Evidência | Implicação |
|--------|-----------|------------|
| **Zero vínculos válidos** (só federação) | `ColaboradorTestBuilder.forFederation()` sem singular/area; `auth-mock` nulls; `IntegrationTestDatabaseCleaner` zera FKs | **Sustenta** cenário incompleto, **contradiz** vínculo completo obrigatório |
| **Exatamente um vínculo completo** | `ColaboradorAcceptanceIntegrationTest` seed com hierarchy; `OrganizationalTestFixtures` | **Sustenta** 1:1 completo em CRUD admin |
| **Múltiplos vínculos** | **Nenhum** teste encontrado | **FATO:** código não testa N | DEC-FA-003 P1 sem espelho em testes |

### 5.8 Síntese DH-02

| Sustenta 1:1 | Contradiz 1:1 |
|--------------|---------------|
| DDL/JPA/API/FE fase 1 (um conjunto de FKs) | BR-041, DEC-FA-003, specs FT-PA N>1 |
| `/auth/me`, JWT, session.store | Modelo físico "colaborador federativo" |
| CRUD admin com um conjunto | DEC-DB-027 exemplo 2 áreas |
| Ausência de implementação N | Construction CDD-PA-01 planejado para N |

**INFERÊNCIA:** o sistema **já opera** como 1:1 na prática (código), mas **normativamente** ainda aponta para N (DEC-FA-003, BR-041, specs).

**Decisão humana pendente.**

---

## 6. DH-03 — Vínculo completo no registro vs `locateOrCreate`

### 6.1 `locateOrCreate` — implementação real ou conceito?

**FATO.** Implementação real em:

- `ColaboradorService.locateOrCreate()` — L32-37
- Invocado por `AuthenticationService.finalizeLogin()` — L159
- Documentado em DEC-DB-020, `specs/features/colaborador/specification.md`, `FT-AUTH` reports

**FATO.** Comportamento de criação (`createColaborador`):

```text
Preenche: email, nome, zimbraId, ativo, federacaoId (= authProperties.defaultFederationId())
NÃO preenche: singularId, areaId, equipeId, gestorId
```

**FATO.** `syncIdentity` em colaborador existente atualiza apenas `zimbraId` e `nome` — **não** altera vínculos.

**FATO.** DEC-DB-020 aprova explicitamente: *"Login FT-AUTH (`locateOrCreate`) pode criar colaborador apenas com `COD_FEDERACAO` [...] FKs organizacionais permanecem NULL até onboarding/admin"*.

### 6.2 Alternativa A — Vínculo completo persistido no registro

| Aspecto | Avaliação | Evidência |
|---------|-----------|-----------|
| Campos obrigatórios | `COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA` NOT NULL; `COD_EQUIPE` opcional | Proposta DEC-DB-028 |
| Quando o vínculo precisa existir | Na criação do registro (login ou admin) | **CONFLICT** com DEC-DB-020 e `locateOrCreate` atual |
| Primeiro acesso | Colaborador já teria vínculo completo ou **não** seria criado no login | Elimina estado "autenticado sem área" no registro |
| Login | `locateOrCreate` **falharia** ou precisaria de dados org não disponíveis no IdP Zimbra | **FATO:** IdP fornece email/nome/zimbraId — não singular/área |
| Integridade referencial | Exige Singular/Área válidas na criação | Validação em `ColaboradorDomainService` existe para CRUD admin |
| Dados incompletos existentes | `IntegrationTestDatabaseCleaner` e `locateOrCreate` produzem incompletos | Backfill necessário em implementação futura |
| Migração (se aprovada futuramente) | `ALTER` NOT NULL + backfill ou exclusão | **ALTO** se dados reais incompletos |

### 6.3 Alternativa B — Resolução operacional via `locateOrCreate`

| Aspecto | Avaliação | Evidência |
|---------|-----------|-----------|
| Quando vínculo não existe | No primeiro login de identidade nova; após cleaner em testes | **FATO** |
| Localização | Por email (case-insensitive) ou zimbraId | **FATO** L33-34 |
| Criação determinística | Sim — sempre `defaultFederationId` + identidade IdP | **FATO** |
| Dados necessários | Email, displayName, zimbraId do IdP; federação de config | **FATO** |
| Duplicidade | UK `DES_EMAIL`, UK `ID_ZIMBRA` | **FATO** constraints |
| Concorrência | `@Transactional` no service; busca antes de criar | **FATO** — idempotência **parcial** (duas threads podem race antes de UK) |
| Primeiro acesso | DEC-FA-001/002: bloqueio operacional se 0 vínculos **válidos** | **INFERÊNCIA:** autenticação ≠ operação plena |
| Reautenticação | `syncIdentity` não completa vínculo | Vínculo permanece incompleto até admin/onboarding |
| Sessão | `/auth/me` retorna links null em singular/area | **FATO** mocks e comportamento esperado |

### 6.4 Alternativa C — Híbrida (evidência derivada, não na proposta original)

**INFERÊNCIA** a partir de DEC-FA-002 e DEC-DB-020:

| Política | Descrição |
|----------|-----------|
| **C1 — Registro permissivo, operação restrita** | `locateOrCreate` cria só federação; operação exige área (BR-010); vínculo completo via FT-COLABORADOR/admin |
| **C2 — Registro estrito** | Não criar colaborador sem Singular+Área; login falha ou redireciona para provisionamento |

**FATO.** O AS-IS atual corresponde a **C1**. A proposta DEC-DB-028 (itens 3-5 NOT NULL) aproxima-se de **C2** no registro.

### 6.5 Matriz comparativa

| Aspecto | Registro completo (A) | `locateOrCreate` incompleto (B/C1) | Evidência atual | Risco/impacto |
|---------|----------------------|-----------------------------------|-----------------|---------------|
| Login primeiro acesso IdP | Precisa fonte de Singular/Área | Cria só federação | Zimbra não fornece org | **ALTO** para A |
| DEC-DB-020 | **Conflito** | **Alinhado** | Decisão vigente | Governança |
| DEC-FA-002 bloqueio sem área | Operação bloqueada de qualquer forma | Operação bloqueada | BR-010 | Compatível |
| CRUD admin | Já pode criar completo | Idem | `ColaboradorAcceptanceIntegrationTest` | — |
| Testes | Fixtures federation-only **quebram** | **Compatível** | `ColaboradorTestBuilder.forFederation` | **MÉDIO** para A |
| FT-PRIMEIRO-ACESSO | Menos estados intermediários | Estado "autenticado, não operacional" | `flows.md` N=0 | **MÉDIO** |

**Decisão humana pendente.** Três alternativas relevantes: **A**, **B/C1**, **C2**.

---

## 7. DH-04 — Exceção colaborador somente-Federação

### 7.1 Perguntas analíticas

1. É tecnicamente suportado hoje?
2. É explicitamente permitido pelo domínio?
3. Algum fluxo depende dele?
4. Existem fixtures/dados representando o cenário?
5. Impacto de tornar Singular+Área obrigatórios?
6. Impacto de permitir a exceção?

### 7.2 Matriz de evidências

| Evidência | Situação atual | Impacto se Singular+Área obrigatórios | Impacto se exceção permitida | Observação |
|-----------|----------------|--------------------------------------|------------------------------|------------|
| **DDL** `COD_SINGULAR`/`COD_AREA` NULL | **FATO** permitido | `ALTER NOT NULL` + backfill | Sem mudança DDL | — |
| **`03-physical-model.md` L715-716** | **FATO** documenta "colaborador federativo: demais vínculos nulos" | **CONFLICT** com proposta DEC-DB-028 itens 4-5 | Mantém exceção explícita | Documento físico oficial |
| **DEC-DB-020** | **FATO** login pode deixar FKs NULL | Contradiz obrigatoriedade no registro | Alinhado | — |
| **DEC-FA-002** | **FATO** "colaborador **operacional**" exige área | **INFERÊNCIA:** não-operacional pode existir sem área | Distingue identidade vs operação | Tensão com proposta "todo colaborador" |
| **DEC-ORG-001** | **FATO** "todo colaborador operacional pertence obrigatoriamente a uma Área" | Compatível se exceção = não-operacional | — | — |
| **`locateOrCreate`** | **FATO** cria só federação | Login deixa de funcionar sem mudança | Comportamento atual preservado | Acoplado a DH-03 |
| **`ColaboradorTestBuilder.forFederation()`** | **FATO** singular/area default null | Testes precisam sempre setar hierarchy | Padrão atual preservado | — |
| **`auth-mock.ts`** | **FATO** singularId/areaId null | E2E mocks ajustados | Preservado | — |
| **`IntegrationTestDatabaseCleaner`** | **FATO** zera singular/area/equipe | Padrão de limpeza assume nullable | — | — |
| **BR-010 bloqueio sem área** | **FATO** | Operacional bloqueado — exceção não opera | Identidade pode existir | — |
| **Seeds/DML colaborador** | **FATO** sem seed de colaborador no `008-initial-data.sql` | N/A | N/A | Sem dados produtivos no repo |
| **API CRUD** | **FATO** `CreateColaboradorRequest` — singular/area opcionais | Validação API precisaria endurecer | Admin pode criar só federação | — |
| **Fluxo primeiro acesso N=0** | **FATO** `flows.md` — 0 vínculos → Block | Colaborador só-federação = 0 vínculos **válidos** se RN-PA-001 exige singular+area | Bloqueio operacional esperado | **Compatível** com exceção no registro |

### 7.3 Respostas objetivas

| # | Pergunta | Resposta classificada |
|---|----------|----------------------|
| 1 | Tecnicamente suportado? | **FATO:** sim — DDL nullable + `locateOrCreate` + API opcional |
| 2 | Permitido pelo domínio? | **FATO:** sim para "colaborador federativo" (`03-physical-model.md`); **FATO:** não para operacional (DEC-FA-002, BR-010) |
| 3 | Fluxo depende? | **FATO:** login FT-AUTH via `locateOrCreate`; **FATO:** testes usam federation-only; **INFERÊNCIA:** não há fluxo de **operação** que dependa de só-federação |
| 4 | Fixtures/dados? | **FATO:** `ColaboradorTestBuilder.forFederation`, `auth-mock`, cleaner — sem evidência de dados Oracle reais no repo |
| 5 | Impacto obrigatoriedade | **ALTO** em login, testes, DEC-DB-020, modelo físico federativo |
| 6 | Impacto permitir exceção | **MÉDIO** — mantém AS-IS; tensão com proposta DEC-DB-028 itens 4-5; operação já bloqueada por BR-010 |

**Decisão humana pendente.**

---

## 8. Análise cruzada

### 8.1 Dependências entre decisões

| Decisão | Depende de | Pode ser decidida isoladamente? | Motivo |
|---------|------------|--------------------------------|--------|
| **DH-01** (supersession DEC-FA-003) | **DH-02** | **Não** | Supersession de P1 só faz sentido se 1:1 for política definitiva |
| **DH-02** (cardinalidade 1:1) | — | **Parcialmente** | Pode ser rejeitada mantendo DEC-FA-003; aprovação **implica** DH-01 |
| **DH-03** (registro vs locateOrCreate) | **DH-04** | **Parcialmente** | Política de registro incompleto define se exceção federativa é transitória ou permanente |
| **DH-04** (exceção só-federação) | **DH-03** | **Não** completamente | Permitir exceção **é** essencialmente escolher registro incompleto (B/C1) |

### 8.2 Conflitos transversais

| Camada | Conflito com proposta DEC-DB-028 | Conflito com DEC-FA-003 vigente |
|--------|----------------------------------|-------------------------------|
| **Governança** | DEC-FA-003 P1, P6 | — |
| **Domínio** | BR-041; colaborador federativo | BR-041 (se 1:1 rejeitado, sem conflito) |
| **Modelo físico** | Singular/Área nullable | N vínculos sem tabela N |
| **DEC-DB-020** | locateOrCreate com FKs NULL | — |
| **Código** | **Nenhum** para N vínculos (não implementado) | Código já é fase 1 (1 conjunto) |
| **Specs FT-PA** | N>1, contexts[] | — |
| **Testes** | federation-only fixtures vs NOT NULL | — |

### 8.3 Ordem sugerida para o decisor (informacional, não prescritiva)

```text
DH-02 (1:1 sim/não)
    ↓ se sim
DH-01 (supersession parcial DEC-FA-003)
    ↓
DH-03 (registro completo vs incompleto no login)
    ↓
DH-04 (exceção federativa permanente ou eliminada)
```

**INFERÊNCIA:** DH-02 é a decisão raiz; DH-01 é consequência normativa; DH-03 e DH-04 são consequências de persistência.

---

## 9. Impactos potenciais de implementação (somente análise)

Impactos **futuros** se as decisões forem aprovadas — **nada implementado nesta etapa**.

### 9.1 Por decisão

| Área | DH-01 aprovada | DH-02 confirmada | DH-03 registro completo | DH-03 locateOrCreate mantido | DH-04 sem exceção | DH-04 com exceção |
|------|----------------|------------------|-------------------------|------------------------------|-------------------|-------------------|
| Oracle/DDL | NENHUM | BAIXO (já 1:1) | **ALTO** NOT NULL | NENHUM | **ALTO** | NENHUM |
| Migrations | NENHUM | NENHUM | **ALTO** | NENHUM | **ALTO** | NENHUM |
| JPA | NENHUM | BAIXO | **MÉDIO** nullable | NENHUM | **MÉDIO** | NENHUM |
| Services | NENHUM | BAIXO | **ALTO** `locateOrCreate` | NENHUM | **MÉDIO** | NENHUM |
| APIs | NENHUM | BAIXO | **MÉDIO** validação create | NENHUM | **MÉDIO** | NENHUM |
| Auth/sessão | NENHUM | BAIXO | **ALTO** login | NENHUM | **BAIXO** | NENHUM |
| Frontend | NENHUM | BAIXO | **MÉDIO** forms/mocks | NENHUM | **MÉDIO** | BAIXO |
| Testes | NENHUM | BAIXO | **ALTO** fixtures federation-only | NENHUM | **ALTO** | BAIXO |
| Documentação | **ALTO** BR-041, specs PA/SESSION | **MÉDIO** | **MÉDIO** DEC-DB-020 | **BAIXO** | **MÉDIO** modelo físico | **BAIXO** |
| Dados existentes | NENHUM | BAIXO | **BLOQUEANTE** se backfill impossível | NENHUM | **BLOQUEANTE** | NENHUM |

### 9.2 Classificação global por DH (se aprovada isoladamente)

| Decisão | Impacto implementação dominante |
|---------|--------------------------------|
| DH-01 | Documentação/specs — **ALTO**; código — **BAIXO** |
| DH-02 | Documentação — **MÉDIO**; código — **BAIXO** (já AS-IS estrutural) |
| DH-03 (registro completo) | Auth + DDL + testes — **ALTO/BLOQUEANTE** |
| DH-03 (locateOrCreate) | Nenhum adicional — **BAIXO** |
| DH-04 (sem exceção) | Mesmo que DH-03 registro completo — **ALTO** |
| DH-04 (com exceção) | Tensão documental com DEC-DB-028 — **MÉDIO** governança |

---

## 10. Evidências insuficientes para decisão

| Informação ausente | Onde deveria estar | Por que relevante | Decisão afetada | Bloqueante? |
|--------------------|-------------------|-------------------|-----------------|-------------|
| Artefatos PD-02/03 de vínculo | `construction/review/` | Histórico de deliberação sobre N vínculos vs `VINCULO_ORGANIZACIONAL` | DH-01 | **Complementar** — DEC-FA-003 e reconciliação cardinalidade cobrem o essencial |
| Contagem de colaboradores Oracle com `COD_SINGULAR`/`COD_AREA` NULL | Ambiente DBA / relatório runtime | Dimensiona backfill e viabilidade de NOT NULL | DH-03, DH-04 | **Bloqueante** para implementação futura; **complementar** para decisão de princípio |
| Requisito de negócio explícito para "colaborador federativo" não operacional | `docs/domain/`, stakeholders | Define se exceção DH-04 é requisito real ou artefato técnico | DH-04 | **Bloqueante** para política definitiva |
| Casos reais de colaborador em N áreas (negócio) | Product / RH / stakeholders | Valida se DEC-FA-003 P1 reflete necessidade real | DH-01, DH-02 | **Bloqueante** para rejeitar 1:1 com segurança |
| Mecanismo físico de persistência do Contexto Ativo | FT-PRIMEIRO-ACESSO / INC-PA-004 | Independente de N vs 1:1, mas afeta DH-03 operacional | DH-03 | **Complementar** |
| Fonte de Singular/Área no login Zimbra | Integração IdP / RH | Se existir feed automático, Alternativa A ganha viabilidade | DH-03 | **Complementar** — sem isso, A no login é **difícil** |

**FATO.** Nenhum documento ausente foi inventado para preencher lacunas.

---

## 11. Perguntas objetivas para decisão humana

### DH-01

**APROVO ou NÃO APROVO** a supersession parcial da DEC-FA-003 nos pontos identificados (P1 N vínculos; alternativa rejeitada "1 vínculo em COLABORADOR"; RN-SESSION-003; título como N pertinências; ramo "N áreas" de P7; `contexts[]` TO-BE), **mantendo** Contexto Ativo, navegação no contexto, REF-DB-CTX-01, `organizationalLinks` singular, auto-seleção com 1 vínculo e bloqueio com 0 vínculos válidos?

**Evidências a considerar:** §4 — implementação N **não existe**; conflito é **normativo**; ~15 documentos referenciam N vínculos.

---

### DH-02

**CONFIRMO ou NÃO CONFIRMO** a cardinalidade **1:1** do vínculo organizacional como política definitiva?

**Evidências a considerar:** §5 — código/DDL **já são** 1:1 estrutural; domínio DEC-FA-003/BR-041 **contradiz**; nenhum teste de N vínculos.

---

### DH-03

**Qual política de persistência do vínculo no ciclo de vida do colaborador?**

Alternativas identificadas na análise (§6):

| ID | Política | Descrição resumida |
|----|----------|-------------------|
| **A** | Registro completo obrigatório | `COD_SINGULAR` e `COD_AREA` NOT NULL desde a criação (login ou admin) |
| **B/C1** | Registro permissivo, operação restrita | `locateOrCreate` mantém só federação; vínculo completo via admin/onboarding; operação bloqueada sem área (AS-IS + DEC-DB-020) |
| **C2** | Registro estrito sem criação incompleta | Não persistir colaborador sem Singular+Área; login não cria registro até provisionamento |

**Evidências a considerar:** §6 — Zimbra **não** fornece Singular/Área; DEC-DB-020 **aprova** FKs NULL no login; `locateOrCreate` é **código real**.

---

### DH-04

**PERMITO ou NÃO PERMITO** a existência de colaborador **somente-Federação** (sem Singular e Área no registro)?

**Evidências a considerar:** §7 — DDL e modelo físico **permitem**; DEC-FA-002 distingue operacional; fixtures e login **usam** o cenário; operação **já bloquearia** sem área (BR-010).

**Nota:** Esta pergunta é **acoplada** a DH-03 — permitir exceção é incompatível com Alternativa A/C2 de DH-03.

---

## 12. Conclusão técnica

Esta análise **não aprova nem rejeita** nenhuma das quatro decisões bloqueantes nem a DEC-DB-028.

### Fatos e impactos que o decisor deve considerar

**DH-01 — Supersession DEC-FA-003**

- **FATO:** P1 (N vínculos) e P6 (rejeição de 1 vínculo) conflitam textualmente com a proposta 1:1.
- **FATO:** P2, P3, P5 e REF-DB-CTX-01 permanecem compatíveis.
- **FATO:** Nenhuma implementação de N vínculos existe — supersession teria impacto **documental**, não de código N.
- **Impacto:** ~15 artefatos normativos precisariam de atualização futura se DH-01 e DH-02 forem aprovados.

**DH-02 — Cardinalidade 1:1**

- **FATO:** O sistema **estruturalmente** já impede N pertinências (colunas escalares, sem tabela N:N).
- **FATO:** A governança vigente (DEC-FA-003, BR-041) **ainda afirma** N vínculos.
- **Impacto:** Aprovar 1:1 alinha norma ao código; rejeitar mantém tensão norma vs implementação.

**DH-03 — Registro vs locateOrCreate**

- **FATO:** `locateOrCreate` é implementação real que cria colaborador **só com federação**.
- **FATO:** DEC-DB-020 **aprova** esse comportamento.
- **FATO:** IdP Zimbra não fornece Singular/Área no login.
- **Impacto:** Registro completo no login (Alternativa A) **conflita** com AS-IS e DEC-DB-020; exigiria mudança de auth ou fonte externa de vínculo.

**DH-04 — Exceção só-Federação**

- **FATO:** Tecnicamente suportado (DDL nullable, modelo físico, código, testes).
- **FATO:** Domínio distingue operacional (exige área) de federativo (permite nulls no modelo físico).
- **FATO:** Nenhum fluxo de **operação** depende de só-federação.
- **Impacto:** Proibir exceção (DEC-DB-028 itens 4-5) **conflita** com modelo físico, DEC-DB-020 e padrão de testes.

### Evidências que contradizem diretamente

| Proposta / decisão vigente | Contradição |
|----------------------------|-------------|
| DEC-DB-028 (Singular+Área NOT NULL para todos) | `03-physical-model.md` colaborador federativo; `locateOrCreate`; DEC-DB-020 |
| DEC-FA-003 P1 (N vínculos) | DDL/JPA/API fase 1 (1 conjunto); ausência de implementação N |

### Confirmação de não-implementação

| Item | Status |
|------|--------|
| Código, DDL, migrations, JPA, API, FE, testes, seeds | **Não alterados** |
| DEC-FA-003, DEC-DB-028 | **Não alteradas / não criadas** |
| Único artefato produzido | Este documento |

---

```text
STATUS: ANÁLISE CONCLUÍDA

DEC-DB-028: NÃO APROVADA
DEC-FA-003: NÃO ALTERADA
IMPLEMENTAÇÃO: NÃO ALTERADA

DH-01: decisão humana pendente
DH-02: decisão humana pendente
DH-03: decisão humana pendente
DH-04: decisão humana pendente

Artefato:
construction/review/vinculo-organizacional-blocking-decisions-analysis.md
```
