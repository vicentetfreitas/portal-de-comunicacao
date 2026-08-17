# Decision Impact Analysis — Vínculo Organizacional e FT-PRIMEIRO-ACESSO

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/vinculo-organizacional-decision-impact-analysis.md` |
| Data | 2026-08-14 |
| Tipo | Análise de impacto orientada à decisão — **sem decisão de negócio** |
| Status | **EVIDÊNCIA PARA DECISÃO HUMANA** |
| Decisões alvo | DH-03, DH-04, DH-01 (impacto potencial) |
| Premissa fixa | DH-02 — 1 vínculo por COLABORADOR (**confirmada**) |

**Classificação usada:** `FATO` · `INFERÊNCIA` · `RISCO` · `LACUNA` · `IMPLEMENTAÇÃO AS-IS` · `DECISÃO DOCUMENTADA`.

**Restrições cumpridas:** nenhum código, DDL, migration, JPA, API, frontend, teste, seed, decisão ou artefato de governança foi alterado.

---

## 1. Objetivo

Preparar subsídios para que o decisor humano formalize:

- **DH-03** — momento e estratégia de persistência do `COLABORADOR`;
- **DH-04** — validade ou não do estado somente-Federação;
- posteriormente **DH-01** — supersession parcial da DEC-FA-003.

Esta análise compara alternativas A/B/C, cenários DH-04, impactos documentais e de implementação, e dependências — **sem escolher vencedor**.

---

## 2. Estado de governança

| Item | Status |
|------|--------|
| **DH-02** | **APROVADA** — 1 vínculo organizacional por COLABORADOR |
| **DEC-DB-028** | Proposta — **não aprovada** |
| **DEC-FA-003** | **Vigente** — não alterada nesta etapa |
| **DEC-DB-020, DEC-FA-002** | Vigentes |
| **FT-PRIMEIRO-ACESSO** | Spec APPROVED; implementação **não iniciada** |
| **FT-COLABORADOR** | APPROVED; CRUD admin implementado |
| **Implementação** | **Não alterada** |

### Fontes primárias consultadas

| # | Fonte | Status |
|---|-------|--------|
| 1 | `construction/review/vinculo-organizacional-flow-reconciliation.md` | Encontrada |
| 2 | `construction/review/vinculo-organizacional-blocking-decisions-analysis.md` | Encontrada |
| 3 | `construction/review/vinculo-organizacional-decision-proposal.md` | Encontrada |
| 4–7 | DEC-DB-020, DEC-FA-002, DEC-FA-003 | `05-decisions-and-risks.md`, `03-open-decisions.md` |
| 8–10 | BR-010, BR-012, BR-041 | `docs/domain/09-business-rules.md` |
| 11–12 | FT-PRIMEIRO-ACESSO, FT-COLABORADOR | `specs/features/*` |
| 13–14 | Modelo físico, decisões DB | `database/model/` |
| 15 | DDL | `database/ddl/003-create-tables.sql`, `004-create-constraints.sql` |
| 16–18 | JPA, `ColaboradorService`, `AuthenticationService` | `backend/` |
| 19–21 | APIs, frontend, testes | Conforme §12 |

**Ausentes (não inventados):** `vinculo-organizacional-reconciliation-pd-02-03.md` e complementar.

---

## 3. Premissas decididas

```text
COLABORADOR
    ↓
1 vínculo organizacional (DH-02)
    ├── Federação
    ├── Singular
    ├── Área
    └── Equipe opcional
```

O fluxo de negócio informado pelo decisor (não formalizado como decisão):

```text
Login → Zimbra → Federação (domínio e-mail)
    → seleção Singular → Área → Equipe (opcional)
    → criação/complementação COLABORADOR → operação
```

---

## 4. Problema central

Três modelos coexistem sem decisão unificadora:

| Modelo | Descrição | Evidência |
|--------|-----------|-----------|
| **AS-IS** | `locateOrCreate` no login cria `COLABORADOR` só com `COD_FEDERACAO`; FKs org NULL | DEC-DB-020; `ColaboradorService.java` |
| **Fluxo decisor** | Vínculo definido por seleção hierárquica; COLABORADOR criado/complementado **depois** | Reconciliação C-01, C-02, C-03 |
| **FT-PRIMEIRO-ACESSO** | Vínculo **pré-provisionado**; PA resolve/seleciona **Contexto Ativo** entre vínculos existentes | `specification.md` §Limites; PA-API-003 |

**Tensão crítica:** FT-PRIMEIRO-ACESSO não cadastra vínculo nem atualiza FKs de `COLABORADOR`; fluxo decisor exige que o próprio usuário estabeleça o vínculo no onboarding.

---

## 5. Alternativa A — Registro completo antes de persistir COLABORADOR

### 5.1 Fluxo

```text
Zimbra → Federação → seleção Singular → Área → Equipe (opcional)
    → persistência COLABORADOR com vínculo completo
```

### 5.2 Análise por dimensão

| Dimensão | Avaliação | Evidência / consequência |
|----------|-----------|--------------------------|
| **Integridade referencial** | **Favorável** no insert | `ColaboradorDomainService.resolveOrganizationalLinks` já valida hierarquia no CRUD admin |
| **Modelo de domínio** | **Alinhado** a BR-009, BR-012, DEC-ORG-001, proposta DEC-DB-028 | Todo registro nasce completo (Fed+Sing+Área) |
| **Primeiro acesso** | **Redesenho necessário** | FT-PA pressupõe colaborador pré-existente; aqui onboarding **cria** o registro |
| **Transação** | **Simples** — um INSERT atômico com vínculo completo | Requer endpoint/UC novo de auto-cadastro |
| **Falha durante onboarding** | Sem registro órfão em `COLABORADOR` | Identidade só em sessão temporária (ver C) ou não persistida |
| **Abandono** | Sem linha `COLABORADOR` incompleta | Usuário recomeça seleção; depende de como identidade é mantida entre etapas |
| **Retry** | Idempotente por UK `DES_EMAIL`/`ID_ZIMBRA` no INSERT final | Duplo submit na mesma seleção → conflito UK (tratável) |
| **Idempotência** | **Alta** no momento final | Sem estado parcial persistido |
| **Concorrência** | **Média** — duas sessões completando mesmo e-mail | UK impede duplicata; segunda falha ou reutiliza existente |
| **Autenticação** | **Impacto alto** | Login atual chama `locateOrCreate` **antes** de seleção — precisaria adiar criação ou separar auth de cadastro |
| **Sessão** | **Impacto alto** | `AUTH_SESSAO` referencia `COD_COLABORADOR` hoje; sem colaborador, sessão precisa outro âncora ou criação tardia |
| **Autorização** | **Simples** pós-criação | Operacional só após registro completo |
| **API** | **Nova** — endpoint de auto-vínculo/onboarding; PA-API-003 não cobre UPDATE de FKs | FT-COLABORADOR exclui onboarding |
| **Frontend** | **Novo** wizard Singular→Área→Equipe pós-login | Incompatível com `SelectingContext` entre vínculos pré-existentes |
| **Testes** | **Alto retrabalho** | `ColaboradorTestBuilder.forFederation()`, `auth-mock` nulls, cleaner — padrão federation-only **incompatível** |
| **DEC-DB-020** | **Incompatível** | Decisão vigente autoriza `locateOrCreate` com FKs NULL no login |
| **DEC-FA-002** | **Compatível** | Operacional sempre com Área |
| **BR-010** | **Compatível** | Sem operação até vínculo completo |
| **BR-041** | **Parcial** | Formato do vínculo ok; cardinalidade N ainda conflita com DH-02 |
| **FT-PRIMEIRO-ACESSO** | **Incompatível conceitualmente** | PA carrega vínculos existentes; A **cria** vínculo no onboarding |
| **Dados existentes** | **Backfill** se registros incompletos existirem em ambiente real | `locateOrCreate` já produziu incompletos |
| **Complexidade** | **Alta** | Novo fluxo auth↔cadastro; mudança em `finalizeLogin` |
| **Risco** | **Alto** | Gap entre auth (precisa sessão) e persistência tardia |

---

## 6. Alternativa B — Registro inicial permissivo + complementação posterior

### 6.1 Fluxo

```text
Zimbra → Federação → COLABORADOR provisório (só COD_FEDERACAO)
    → Singular → Área → Equipe (opcional) → complementação do vínculo
```

### 6.2 Separação transitório × permanente

| Estado | Permitido por B? | Evidência |
|--------|------------------|-----------|
| **Transitório** (só Federação durante onboarding) | **Sim** — núcleo da alternativa | DEC-DB-020; AS-IS `locateOrCreate` |
| **Permanente** (só Federação como destino final) | **Possível** se complementação nunca ocorrer | Modelo físico “colaborador federativo”; **não decidido** (DH-04) |

### 6.3 Análise por dimensão

| Dimensão | Avaliação | Evidência / consequência |
|----------|-----------|--------------------------|
| **Significado estado incompleto** | Identidade autenticada, **não operacional** | DEC-FA-002 item 5; BR-010 |
| **Duração incompleto** | Indefinida até complementação ou abandono | **LACUNA** — sem TTL ou job de limpeza |
| **Integridade** | **Parcial** no registro | FKs NULL permitidas no DDL |
| **Autorização durante incompleto** | **Bloqueada** para operação | BR-010; JWT pode carregar `sid`/`aid` null |
| **Concorrência** | `locateOrCreate` @Transactional; race antes de UK | **Risco baixo** |
| **Idempotência** | **Alta** no login (find by email/zimbraId) | `ColaboradorService` L33-36 |
| **Rollback** | Sem rollback automático de provisório abandonado | Registros órfãos possíveis |
| **Falha na complementação** | Colaborador permanece incompleto | Mesmo que abandono |
| **Abandono** | Linha `COLABORADOR` só-Federação persiste | **RISCO** órfãos; acoplado a DH-04 |
| **Retry** | Login reutiliza registro; complementação retentável | Sem endpoint de complementação hoje |
| **Órfãos/incompletos** | **RISCO médio-alto** | AS-IS já produz esse estado |
| **DEC-DB-020** | **Compatível** | Texto explícito |
| **DEC-FA-002** | **Compatível** | Distingue operacional vs identidade |
| **BR-010** | **Compatível** | Bloqueio até Área + Contexto Ativo |
| **FT-PRIMEIRO-ACESSO** | **Incompatível** com fluxo decisor | PA: N=0 → Block, não wizard; não atualiza FKs |
| **Sessão/contexto** | Sessão com `COD_COLABORADOR` incompleto; `/auth/me` links null | `session.store` promove links a `activeContext` mesmo incompletos (TODO OQ-026) |
| **API** | Falta contrato de **complementação** de FKs | PA-API-003 só Contexto Ativo |
| **Frontend** | Precisa wizard + distinguir Blocked vs onboarding ativo | Não implementado |
| **Testes** | **Compatível** com AS-IS | `forFederation()`, `auth-mock` nulls |
| **Complexidade** | **Média** — menor que A/C se mantém `locateOrCreate` | Exige novo passo de complementação |
| **Risco** | **Médio** — órfãos; semântica DH-04 |

**FATO:** AS-IS implementa **B no login**; **não** implementa complementação no onboarding.

---

## 7. Alternativa C — Não criar COLABORADOR até vínculo mínimo completo

### 7.1 Fluxo

```text
Zimbra → Federação → Singular → Área → Equipe (opcional) → COLABORADOR criado
```

### 7.2 Análise por dimensão

| Dimensão | Avaliação | Evidência / consequência |
|----------|-----------|--------------------------|
| **Entidade temporária** | **Necessária** entre auth e criação | Opções: sessão anônima, claim JWT sem `COD_COLABORADOR`, tabela staging — **nenhuma existe** |
| **Identificação entre etapas** | Via sessão FT-AUTH **sem** `COLABORADOR` | `AUTH_SESSAO.COD_COLABORADOR` é NOT NULL hoje — **bloqueante estrutural** |
| **Sessão** | **Impacto bloqueante** | `SessionService.createSession(colaborador, ...)` exige entidade |
| **Persistência** | Um INSERT final completo | Alinhado a integridade |
| **Retry / idempotência** | Idempotente no INSERT final | Sessão intermediária precisa política de expiração |
| **Abandono** | Sem `COLABORADOR` órfão | Possível lixo em sessão/staging |
| **Duplicidade** | UK no INSERT | Ok |
| **Zimbra / auth** | **Alto impacto** | `finalizeLogin` deve **não** chamar `locateOrCreate` |
| **Autorização** | Bloqueada até criação | Compatível BR-010 |
| **Primeiro acesso** | Onboarding **é** criação do colaborador | Redesenho FT-PA como dono da criação |
| **API** | Novo contrato auth+onboarding | `/auth/me` sem `colaboradorId` até fim — breaking change |
| **Frontend** | Wizard antes de hidratação de sessão “completa” | Inversão do boot atual |
| **Testes** | **Alto** — todos os fluxos auth assumem `COD_COLABORADOR` | `AuthAcceptanceIntegrationTest`, etc. |
| **DEC-DB-020** | **Incompatível** | Autoriza criação parcial no login |
| **FT-PRIMEIRO-ACESSO** | **Incompatível** | Pressupõe colaborador autenticado com identidade persistida |
| **Complexidade** | **Muito alta** | Mudança em auth, sessão, JWT, `/auth/me` |
| **Risco** | **Bloqueante** sem redesign de sessão | `AUTH_SESSAO` acoplada a `COLABORADOR` |

**INFERÊNCIA:** C é a alternativa de **maior impacto estrutural** apesar de integridade nominalmente superior.

---

## 8. Comparação das alternativas

Legenda: **F** favorável · **N** neutro · **D** desfavorável · **I** incompatível com vigente

| Critério | A — Completo | B — Provisório + complementação | C — Criar após vínculo |
|----------|:------------:|:-------------------------------:|:----------------------:|
| Integridade referencial | F | N | F |
| Modelo de domínio | F | N | F |
| Primeiro acesso | D | N | D |
| Sessão | D | N | I |
| Autorização | F | F | F |
| Transação | F | N | F |
| Idempotência | F | F | N |
| Concorrência | N | F | N |
| Abandono do onboarding | F | D | F |
| Retry | F | N | N |
| Segurança | F | N | N |
| API | D | N | D |
| Frontend | D | N | D |
| Testes | D | F | D |
| Banco/JPA | N* | F | D |
| Compatibilidade DEC-DB-020 | I | F | I |
| Compatibilidade DEC-FA-002 | F | F | F |
| Compatibilidade BR-010 | F | F | F |
| Compatibilidade BR-041 | N | N | N |
| Compatibilidade FT-PRIMEIRO-ACESSO | I | D | I |
| Complexidade | Alta | Média | Muito alta |
| Risco | Alto | Médio | Bloqueante |

\* A exige `ALTER NOT NULL` em Singular/Área se política definitiva (proposta DEC-DB-028).

**Observação:** Nenhuma alternativa é totalmente compatível com FT-PRIMEIRO-ACESSO **como especificado** e com o **fluxo decisor** simultaneamente. B é a mais alinhada ao **AS-IS** e DEC-DB-020; A e C alinham-se melhor ao **fluxo decisor** no momento da persistência, com custos distintos em sessão (C > A > B).

---

## 9. DH-04 — cenários de somente-Federação

### 9.1 Definições

| Estado | Descrição |
|--------|-----------|
| **Permanente** | `COLABORADOR` com só `COD_FEDERACAO`; destino final válido (não operacional) |
| **Temporário** | Mesmo shape, apenas durante onboarding até complementação |
| **Inválido** | Não permitido; usuário deve completar seleção antes de existir/persistir vínculo aceitável |

### 9.2 Matriz comparativa

| Critério | Permanente | Temporário | Inválido |
|----------|:----------:|:----------:|:--------:|
| Compatibilidade com fluxo decisor | D | F | F |
| DEC-DB-020 | F | F | D |
| DEC-FA-002 | F* | F | F |
| BR-010 | F* | F | F |
| FT-PRIMEIRO-ACESSO | D** | D** | I |
| Segurança | F — sem operação | F — janela limitada | F |
| Modelo de domínio | F — `03-physical-model.md` | N — DEC-DB-020 | F — BR-009 |
| Complexidade | Baixa (AS-IS) | Média — TTL/limpeza? | Alta — força A ou C |
| Impacto futuro | Mantém população não operacional | Exige política de transição e complementação | Alinha a vínculo sempre completo |

\* Operacional bloqueado — identidade pode existir.  
\*\* FT-PA trata como N=0 vínculos válidos → `Blocked`, não distingue transitório vs permanente.

### 9.3 Consequências por escolha (para o decisor)

| Se **permanente** | Se **temporário** | Se **inválido** |
|-------------------|-------------------|-----------------|
| Preserva “colaborador federativo” do modelo físico | Exige complementação obrigatória + possível limpeza de órfãos | Elimina `locateOrCreate` parcial; força A ou C |
| `locateOrCreate` AS-IS permanece | B com prazo/política de abandono | DEC-DB-020 precisaria supersession |
| Tensão com fluxo decisor (destino = Sing+Área) | Alinhado a DEC-DB-020 e B | Alinhado a proposta DEC-DB-028 |
| População autenticável não operacional | Risco de órfãos se abandono | Maior integridade cadastral |

---

## 10. Conflito com FT-PRIMEIRO-ACESSO

### 10.1 O que FT-PRIMEIRO-ACESSO pressupõe? (com evidências)

| Pressuposto | Evidência | Resposta |
|-------------|-----------|----------|
| Vínculo pré-existente | `specification.md` §Limites: “Provisionamento administrativo de vínculos (FT-COLABORADOR)”; RN-PA-001 | **Sim** — vínculo válido exige FKs preenchidas no colaborador |
| Colaborador pré-existente | UC-PA-001: “solicita vínculos do colaborador”; FT-AUTH hand-off | **Sim** |
| Singular/Área pré-existentes | RN-PA-001: `federationId`, `singularId`, `areaId` não nulos e ativos | **Sim** — na estrutura org e no vínculo do colaborador |
| Contexto pré-existente | UC-PA-008 reentrada com contexto persistido | Opcional — pode ser criado na sessão |
| Seleção apenas de contexto | DEC-FA-001: carrega vínculos e seleciona Contexto Ativo | **Sim** — não cria vínculo |
| Criação de colaborador | Fora do escopo explícito | **Não** |
| Atualização de FKs | PA-API-003: persiste Contexto Ativo; RN-PA-008 pertencimento | **Não** — valida contra vínculos existentes |

### 10.2 O que o novo fluxo pressupõe?

| Pressuposto | Evidência |
|-------------|-----------|
| Estrutura org cadastrada (Singulares, Áreas, Equipes) | Fluxo decisor §3.3 |
| Usuário **seleciona** hierarquia | Fluxo decisor |
| Vínculo **estabelecido** na seleção | Reconciliação C-02 |
| COLABORADOR criado/complementado **após** seleção | Fluxo decisor §3.4 |

### 10.3 Avaliação de desfecho documental (sem executar)

| Opção | Quando se aplica | Evidência |
|-------|------------------|-----------|
| **1. Manter** FT-PRIMEIRO-ACESSO | Se vínculos forem sempre pré-provisionados (admin) | Spec atual; DEC-FA-001 original |
| **2. Complementar** | Adicionar UC/API de auto-vínculo; PA mantém Contexto Ativo pós vínculo completo | Fluxo decisor + partes de PA (Home, Blocked, estados) |
| **3. Supersession parcial** | Remover N vínculos, `contexts[]`, SelectingContext N>1; substituir por wizard hierárquico | DH-02 + fluxo decisor |
| **4. Redesenho total** | PA deixa de ser “resolução de contexto” e passa a ser “onboarding de vínculo” | C-02, C-03 reconciliação |

**FATO:** Com DH-02 + fluxo decisor, opções **2 ou 4** são as mais plausíveis; **manter sem alteração** permanece **incompatível** com auto-seleção de vínculo.

**FATO:** Implementação runtime de FT-PA **não existe** — impacto é **documental e de planejamento**, não retrabalho de código PA.

---

## 11. Ciclo de vida do COLABORADOR

### 11.1 Estados necessários pelo fluxo decisor

```text
IDENTIDADE_AUTENTICADA (Zimbra)
        ↓
FEDERACAO_IDENTIFICADA
        ↓
VINCULO_EM_SELECAO (Singular → Área → Equipe?)
        ↓
VINCULO_COMPLETO (Fed + Sing + Área [+ Equipe])
        ↓
CONTEXTO_ATIVO_PERSISTIDO
        ↓
COLABORADOR_OPERACIONAL
```

### 11.2 Estados documentados / implementados

| Estado | Onde | AS-IS |
|--------|------|-------|
| Autenticado sem contexto resolvido | `state-machine.md` `Authenticated`, `LoadingContexts` | **Não implementado** (FT-PA) |
| Colaborador só-Federação | DEC-DB-020; modelo físico | **Implementado** via `locateOrCreate` |
| 0 vínculos válidos | `Blocked` (UC-PA-009) | **Não implementado**; session.store trata links null como ready |
| 1 vínculo auto | `PersistingContext` direto | **Parcial** — session.store auto-promove |
| N vínculos seleção | `SelectingContext` | **Não implementado** |
| Operacional | `Operational` | **Não implementado** (sem guard PA) |
| CRUD admin completo | FT-COLABORADOR | **Implementado** |

### 11.3 Lacunas de transição

| Transição | Status |
|-----------|--------|
| Auth → seleção hierárquica | **Inexistente** |
| Auth → complementação FKs | **Inexistente** |
| Só-Federação → vínculo completo (onboarding) | **Inexistente** (só admin PUT) |
| Vínculo completo → Contexto Ativo | **Especificada** (PA), não implementada |
| Seleção entre N vínculos | **Especificada**, contradiz DH-02 |

### 11.4 Estados proibidos (por fontes vigentes)

| Estado | Fonte |
|--------|-------|
| Operacional sem Área | DEC-FA-002, BR-010 |
| Contexto Ativo inconsistente | BR-012, RN-PA-001 |
| N vínculos cadastrais | **DH-02** (confirmada) — contradiz BR-041 até DH-01 |

---

## 12. `locateOrCreate` — AS-IS × regra

### 12.1 Comportamento documentado

| Aspecto | Detalhe |
|---------|---------|
| **Onde** | `ColaboradorService.locateOrCreate` ← `AuthenticationService.finalizeLogin` |
| **Entradas** | `IdentityValidationResult`: email, displayName, zimbraId |
| **Localização** | `findByEmailIgnoreCase` OR `findByZimbraId` |
| **Criação** | email, nome, zimbraId, ativo=S, `federacaoId=defaultFederationId`, dataCadastro |
| **Não preenche** | singularId, areaId, equipeId, gestorId |
| **Atualização** | `syncIdentity`: só zimbraId, nome |
| **Transação** | `@Transactional` |
| **Idempotência** | Re-login encontra existente; criação única por UK |
| **Duplicidade** | UK `DES_EMAIL`, UK `ID_ZIMBRA` |
| **Estado posterior esperado** | Complementação por admin/onboarding — **não implementada** |

### 12.2 Classificação por comportamento

| Comportamento | Classificação |
|---------------|---------------|
| Criar colaborador no login após Zimbra | **DECISÃO DOCUMENTADA** (DEC-DB-020) + **IMPLEMENTAÇÃO AS-IS** |
| Só `COD_FEDERACAO` na criação | **DECISÃO DOCUMENTADA** (DEC-DB-020) |
| `defaultFederationId` fixo (não domínio e-mail) | **IMPLEMENTAÇÃO AS-IS** — **COMPORTAMENTO NÃO GOVERNADO** para multi-domínio |
| Não completar FKs no auth | **DECISÃO DOCUMENTADA** (DEC-DB-020) |
| Ausência de complementação no onboarding | **LACUNA** / **COMPORTAMENTO NÃO GOVERNADO** |
| Operação com links null | **COMPORTAMENTO NÃO GOVERNADO** — session.store promove a activeContext (TODO OQ-026) |

**INFERÊNCIA:** `locateOrCreate` **não é** regra de negócio TO-BE do fluxo decisor; é **decisão documentada + implementação** alinhadas entre si, mas **incompletas** para o onboarding hierárquico.

---

## 13. Impacto documental

Legenda: **SI** sem impacto · **AN** atualização necessária · **SN** supersession necessária · **ND** nova decisão necessária

### 13.1 Por alternativa (DH-03)

| Documento | A | B | C |
|-----------|---|---|---|
| DEC-DB-020 | SN | SI | SN |
| DEC-FA-002 | SI | SI | SI |
| DEC-FA-003 | AN (DH-01) | AN | AN |
| BR-010 | SI | SI | SI |
| BR-041 | SN/AN | AN | AN |
| FT-PRIMEIRO-ACESSO | SN | AN | SN |
| FT-COLABORADOR | AN (incluir onboarding ou referenciar PA) | AN | AN |
| Modelo físico (`03-physical-model.md`) | AN (federativo vs obrigatório) | AN (transitório) | AN |
| `05-decisions-and-risks.md` | ND (DEC-DB-028?) | AN nota DH-03 | SN |
| Specs AUTH (`decisions.md` DA-AUTH-011) | AN | SI | AN |
| Traceability PA/COLABORADOR | AN | AN | AN |

### 13.2 Por cenário DH-04

| Documento | Permanente | Temporário | Inválido |
|-----------|:----------:|:----------:|:--------:|
| Modelo físico (federativo) | SI | AN | SN |
| DEC-DB-020 | AN | SI | SN |
| DEC-FA-002 | SI | SI | SI |
| Proposta DEC-DB-028 | SN | AN | SI |
| FT-PRIMEIRO-ACESSO (RN-PA-004) | AN | AN | AN |

**Nenhum documento foi alterado nesta etapa.**

---

## 14. Impacto de implementação (estimativa futura)

### 14.1 Por alternativa

| Camada | A | B | C |
|--------|---|---|---|
| **Banco** | MÉDIO — NOT NULL futuro; backfill | BAIXO — mantém nullable | ALTO — sessão sem COLABORADOR |
| **Backend** | ALTO — adiar/remover locateOrCreate; novo onboarding | MÉDIO — endpoint complementação | BLOQUEANTE — redesign sessão |
| **API** | ALTO — novo contrato onboarding | MÉDIO — PUT vínculo self-service | BLOQUEANTE — `/auth/me` pré-colaborador |
| **Frontend** | ALTO — wizard | MÉDIO — wizard + estados | BLOQUEANTE — boot invertido |
| **Testes** | ALTO | BAIXO | BLOQUEANTE |

### 14.2 Por cenário DH-04

| Camada | Permanente | Temporário | Inválido |
|--------|:----------:|:----------:|:--------:|
| Banco | NENHUM | BAIXO (job limpeza?) | MÉDIO (NOT NULL) |
| Backend | NENHUM | BAIXO | MÉDIO |
| API/FE | BAIXO (UX Blocked) | MÉDIO | MÉDIO |
| Testes | NENHUM | BAIXO | MÉDIO |

---

## 15. Impactos potenciais na DEC-FA-003

Premissa: DH-02 = SIM. DH-03/DH-04 ainda pendentes.

| Elemento DEC-FA-003 | Impacto potencial | Motivo |
|---------------------|-------------------|--------|
| **P1 — N vínculos** | **SUPERSESSION NECESSÁRIA** | Textualmente contradiz DH-02; código já é 1 slot FK |
| **P2 — Contexto Ativo** | **SEM IMPACTO** | Compatível com 1:1 |
| **P3 — Navegação no Contexto Ativo** | **SEM IMPACTO** | BR-010/DEC-FA-002 mantidos |
| **P4 — RN-SESSION-003 seleção N>1** | **SUPERSESSION NECESSÁRIA** | Regra morta com 1 vínculo |
| **P5 — REF-DB-CTX-01** | **SEM IMPACTO** | Independente de cardinalidade |
| **P6 — Alternativa 1 vínculo rejeitada** | **ATUALIZAÇÃO NECESSÁRIA** | Inversão se DH-01 aprovar supersession |
| **P7 — N áreas (OQ-008)** | **SUPERSESSION PARCIAL** | “N áreas” perde sentido cadastral com 1:1 |
| **`contexts[]` (PA-API-001)** | **SUPERSESSION NECESSÁRIA** | Lista N desnecessária; pode virar 0/1 vínculo |
| **Contexto Ativo** | **SEM IMPACTO** conceitual | Com 1:1, ativo = único vínculo |
| **Navegação** | **SEM IMPACTO** | — |
| **`organizationalLinks`** | **SEM IMPACTO** | API já retorna objeto único |

**Dependência:** DH-01 só é acionável após DH-02 (confirmada). DH-03/DH-04 influenciam **como** o único vínculo é estabelecido, não a cardinalidade em si.

---

## 16. Dependências entre decisões

```text
DH-02 (CONFIRMADA)
    ↓
DH-01 (supersession DEC-FA-003 P1,P4,P6,P7) — depende de DH-02
    ↓
DH-03 (momento persistência) ←→ DH-04 (somente-Federação)
    ↓
Redesenho / complemento FT-PRIMEIRO-ACESSO
    ↓
Eventual DEC-DB-028 (proposta — não aprovada)
```

| Relação | Descrição |
|---------|-----------|
| DH-03 ↔ DH-04 | B + transitório: mesma política; B + permanente: permite órfãos; A/C + inválido: elimina somente-Federação como destino |
| DH-03 → FT-PA | A/C exigem PA como **criador** de vínculo; B exige **complementação** além do PA atual |
| DH-04 → FT-PA | Permanente: N=0 Blocked permanente; Transitório: PA deve distinguir Blocked vs wizard |
| DH-01 ⊥ código | Supersession é **governança** — implementação N não existe |

---

## 17. Evidências insuficientes

| Lacuna | Impacto |
|--------|---------|
| Mapeamento domínio e-mail → `COD_FEDERACAO` | Fluxo decisor não implementável sem ND |
| Política de abandono / TTL de provisório | DH-04 transitório |
| Dados Oracle reais com colaboradores incompletos | Migração/backfill |
| Artefatos PD-02/03 de vínculo | Histórico DEC-DB-027 |
| OQ-007 — evento Colaborador Integrado pós-contexto | Ciclo de vida |
| Mecanismo físico Contexto Ativo (INC-PA-004) | Independente de A/B/C, mas bloqueia PA |

---

## 18. Perguntas para decisão humana

### DH-03

1. O `COLABORADOR` pode ser persistido no login **antes** da seleção organizacional?
2. Se sim, quais FKs mínimas (`COD_FEDERACAO` apenas)?
3. Quem completa o vínculo — onboarding self-service, admin, ou ambos?
4. `locateOrCreate` permanece no login ou é substituído/adado?
5. Qual transação define “primeiro acesso concluído” — INSERT completo, UPDATE de FKs, ou persistência de Contexto Ativo?

### DH-04

1. Colaborador somente-Federação é categoria de negócio **permanente** (não operacional)?
2. Ou **somente** estado transitório até complementação obrigatória?
3. Ou **inválido** — proibindo persistência sem Singular+Área?
4. Qual política para registros abandonados no onboarding?

### FT-PRIMEIRO-ACESSO

1. PA continua como resolução de Contexto Ativo ou passa a incluir **criação/complementação de vínculo**?
2. Estado `Blocked` (N=0) coexiste com wizard de seleção hierárquica?

### DH-01 (preparação)

1. Quais itens de DEC-FA-003 serão supersededos formalmente após DH-02?

---

## 19. Conclusão técnica

Existem **três alternativas distintas** para DH-03 com perfis de impacto diferentes:

| Alternativa | Alinhamento principal | Custo principal |
|-------------|----------------------|-----------------|
| **A** | Fluxo decisor, integridade cadastral, DEC-FA-002 | Incompatível com DEC-DB-020 e `locateOrCreate`; redesign auth/sessão |
| **B** | DEC-DB-020 e AS-IS | Lacuna de complementação; tensão com FT-PA e fluxo decisor; risco órfãos (DH-04) |
| **C** | Integridade máxima, fluxo decisor (persistência tardia) | `AUTH_SESSAO` acoplada a `COLABORADOR` — impacto **bloqueante** |

Para DH-04, as três políticas (permanente / transitório / inválido) são **todas parcialmente sustentadas** por fontes diferentes; nenhuma é deduzível sem escolha humana. O modelo físico favorece **permanente**; DEC-DB-020 favorece **transitório**; o fluxo decisor favorece **inválido como estado definitivo**.

O conflito com FT-PRIMEIRO-ACESSO é **conceitual e documental**: a spec atual é **resolução de contexto entre vínculos pré-existentes**; o fluxo decisor é **estabelecimento de vínculo por seleção hierárquica**. Com DH-02 confirmada, FT-PA requer pelo menos **complementação** ou **redesenho parcial/total** — não manutenção integral do modelo N vínculos + provisionamento admin.

**O decisor deve considerar:**

- Zimbra **não fornece** Singular/Área — qualquer alternativa que persista vínculo completo exige **UI + API** pós-auth.
- AS-IS já implementa **B no login** sem complementação — gap operacional real.
- C é tecnicamente a mais disruptiva por acoplamento sessão↔colaborador.
- DH-04 não é independente de DH-03: permitir somente-Federação **permanente** é consequência natural de B sem complementação obrigatória.

**Esta análise não recomenda alternativa nem aprova decisão.**

---

## Referências

| Artefato | Caminho |
|----------|---------|
| Reconciliação de fluxo | `construction/review/vinculo-organizacional-flow-reconciliation.md` |
| Decisões bloqueantes | `construction/review/vinculo-organizacional-blocking-decisions-analysis.md` |
| Proposta DEC-DB-028 | `construction/review/vinculo-organizacional-decision-proposal.md` |
| Código auth/colaborador | `ColaboradorService.java`, `AuthenticationService.java`, `ColaboradorApplicationService.java` |
| Frontend sessão | `frontend/src/stores/session.store.ts` |
| FT-PRIMEIRO-ACESSO | `specs/features/primeiro-acesso/` |
| FT-COLABORADOR | `specs/features/colaborador/specification.md` |

---

*Análise de impacto — implementação e governança não alteradas.*
