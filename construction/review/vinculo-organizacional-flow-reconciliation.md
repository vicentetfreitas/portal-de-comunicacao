# Reconciliação de fluxo — Criação do COLABORADOR e vínculo organizacional

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/vinculo-organizacional-flow-reconciliation.md` |
| Data | 2026-08-14 |
| Tipo | Análise de reconciliação — **sem decisão de negócio** |
| Status | **EVIDÊNCIA PARA DECISÃO HUMANA** |
| Decisões alvo | DH-03, DH-04 (pendentes) |
| Premissa fixa | DH-02 — 1 vínculo organizacional por COLABORADOR (**confirmada**) |

**Classificação usada:** `FATO` · `INFERÊNCIA` · `CONTRADIÇÃO` · `AMBIGUIDADE` · `LACUNA` · `IMPLEMENTAÇÃO AS-IS` · `TO-BE (hipótese do decisor, não formalizada)`.

**Restrições cumpridas:** nenhum código, DDL, migration, JPA, API, frontend, teste, seed, decisão, spec ou artefato de governança foi alterado. DH-03 e DH-04 **não** foram formalizadas.

---

## 1. Objetivo

Executar reconciliação específica de negócio, domínio, persistência e implementação entre o **fluxo de onboarding/login definido pelo decisor** e as fontes:

- DEC-DB-020
- DEC-FA-002
- BR-010
- BR-041
- FT-PRIMEIRO-ACESSO

Determinar contradições, ambiguidades e lacunas **antes** de formalizar DH-03 (registro completo vs `locateOrCreate`) e DH-04 (colaborador somente-Federação).

---

## 2. Fluxo definido pelo decisor

```text
Login (e-mail)
    ↓
Autenticação Zimbra (identidade válida)
    ↓
Identificação da Federação (ex.: @unimedceara.com.br, @cariri.com.br → Unimed Ceará)
    ↓
Seleção obrigatória: Singular → Área → Equipe (opcional)
    ↓
Criação/complementação do COLABORADOR com vínculo completo
    ↓
Autorização operacional
```

**Hipótese a validar (não assumida como decisão):** estado definitivo = Federação + Singular + Área, Equipe opcional.

**TO-BE (decisor):** domínio do e-mail **não** determina Singular, Área ou Equipe — apenas Federação.

---

## 3. Premissas já decididas

| ID | Decisão | Status |
|----|---------|--------|
| **DH-02** | Um COLABORADOR possui **1 único** vínculo organizacional | **CONFIRMADA** — não reaberta nesta análise |

Modelo esperado após DH-02 (se compatível com fontes):

```text
1 COLABORADOR
    └── 1 VÍNCULO
            ├── FEDERAÇÃO (obrigatório)
            ├── SINGULAR (a confirmar)
            ├── ÁREA (a confirmar)
            └── EQUIPE (opcional)
```

---

## 4. DEC-DB-020

### 4.1 Definição (FATO)

Fonte: `database/model/05-decisions-and-risks.md` — **APPROVED** (2026-07-20).

| Item | Decisão |
|------|---------|
| Colunas em `COLABORADOR` | `COD_FEDERACAO` obrigatório; `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE`, `COD_GESTOR` **opcionais** |
| `PAPEL_ATRIBUICAO` | Escopo de autorização; **não substitui** FKs organizacionais em `COLABORADOR` |
| Login FT-AUTH (`locateOrCreate`) | **Pode** criar colaborador apenas com `COD_FEDERACAO`, identidade e `ID_ZIMBRA`; FKs organizacionais permanecem **NULL** até onboarding/admin |
| Reconciliação DEC-DB-027 (2026-08-14) | `locateOrCreate` com FKs org NULL **mantido** para vínculo AS-IS |

### 4.2 Respostas analíticas

| # | Pergunta | Evidência |
|---|----------|-----------|
| 1 | O que estabelece sobre FKs nulas? | FKs org **podem ser NULL** no schema e na criação por login |
| 2 | Em que momento podem ser nulas? | Na criação por `locateOrCreate` e **até** onboarding ou cadastro administrativo |
| 3 | Nulabilidade é… | **Estado temporário de onboarding/admin** (texto explícito); não define exceção de negócio permanente — isso está no modelo físico (§9) |
| 4 | `locateOrCreate` relacionado? | **Sim**, explicitamente na linha de decisão do login FT-AUTH |
| 5 | Permite criar antes de Singular/Área? | **Sim** |
| 6 | Permite permanecer sem Singular/Área? | **Não proíbe** no DDL; modelo físico documenta colaborador federativo (ver §9) |
| 7 | Fluxo decisor exige complementaridade posterior? | **Compatível** com complementação posterior; **incompatível** com adiar criação até vínculo completo |
| 8 | Conflito com novo fluxo? | **Parcial** — ver matriz |

### 4.3 Matriz DEC-DB-020 × fluxo decisor

| Regra DEC-DB-020 | Comportamento fluxo decisor | Resultado |
|------------------|----------------------------|-----------|
| `COD_FEDERACAO` obrigatório na criação | Federação identificada antes da seleção | **COMPATÍVEL** |
| FKs org NULL permitidas na criação | Criação **após** seleção Singular/Área/Equipe | **CONTRADIÇÃO** (momento de persistência) |
| FKs NULL até onboarding/admin | Onboarding = seleção hierárquica pelo usuário | **AMBIGUIDADE** (“onboarding” não especifica auto-seleção vs admin) |
| `locateOrCreate` no login | COLABORADOR criado/complementado após seleção | **CONTRADIÇÃO** (se interpretado como única forma de criação) |
| Vínculo definitivo com Singular+Área | Hipótese do decisor (não é decisão DEC-DB-020) | **LACUNA** nesta decisão |

---

## 5. DEC-FA-002

### 5.1 Definição integral (FATO)

Fonte: `docs/governance/03-open-decisions.md` — **Aprovada** (2026-07-24).

1. Todo colaborador **operacional** possui pelo menos um vínculo organizacional com **Área**.
2. O sistema **não admite** colaborador operacional sem Área.
3. O login recupera o(s) vínculo(s) do colaborador.
4. A navegação operacional ocorre **sempre** dentro de um **Contexto Ativo**.
5. Sem vínculo válido → bloqueio de acesso operacional (≠ falha de autenticação Zimbra).

Registro: BR-010 revisada, `specs/features/session/specification.md`, `specs/features/primeiro-acesso/specification.md`.

### 5.2 Análise da afirmação “operação exige Área”

| Interpretação | Suportada por evidência? |
|---------------|-------------------------|
| **A.** Área necessária apenas para **operar** | **Sim** — “colaborador **operacional**”, “sem vínculo válido → bloqueio **operacional**” |
| **B.** Área obrigatória para o **vínculo cadastral** em qualquer estado | **Não** — DEC-DB-020 e modelo físico permitem FKs NULL; decisão restringe **operacional** |
| **C.** Distinção colaborador federativo vs operacional | **Sim, implícita** — modelo físico (`03-physical-model.md`) documenta “colaborador federativo” (só `COD_FEDERACAO`); DEC-FA-002 não o proíbe, apenas exige Área para operação |

### 5.3 Matriz DEC-FA-002 × fluxo decisor

| Regra DEC-FA-002 | Fluxo decisor | Compatibilidade | Evidência |
|------------------|---------------|-----------------|-----------|
| Operacional exige Área | Estado final com Área selecionada | **COMPATÍVEL** | Item 1–2 da decisão |
| Bloqueio sem vínculo válido | Bloqueio entre auth e conclusão da seleção | **COMPATÍVEL** | Item 5; BR-010 |
| Login recupera vínculo(s) | Vínculo ainda não existe até seleção | **AMBIGUIDADE** | “Recupera” pressupõe vínculo pré-existente; fluxo decisor **cria** vínculo na seleção |
| Contexto Ativo obrigatório para navegar | Após seleção + persistência | **COMPATÍVEL** | Item 4 |
| Colaborador somente-Federação | Hipótese transitória vs permanente | **AMBIGUIDADE** | Não define se federativo é válido fora da operação |
| Singular obrigatória no vínculo | Seleção obrigatória de Singular no fluxo decisor | **COMPATÍVEL** (estado final) | BR-009, BR-012, RN-PA-001 exigem `singularId` no vínculo válido |

---

## 6. BR-010

### 6.1 Definição oficial (FATO)

Fonte: `docs/domain/09-business-rules.md`.

> Colaborador operacional autenticado possui pelo menos um vínculo organizacional com Área; sem Área não há operação nem navegação operacional; o login recupera o(s) vínculo(s); toda navegação operacional ocorre no Contexto Ativo.

Impacto declarado: impede operação sem contexto; alinha autenticação × primeiro acesso (DEC-FA-002).

### 6.2 Condições de bloqueio

| Aspecto | Evidência |
|---------|-----------|
| Quando bloquear | Ausência de vínculo com **Área** ou ausência de **Contexto Ativo** válido |
| “Sem Área” significa | Ausência de vínculo **operacional** com Área — **não** é falha de autenticação |
| Vínculo federativo (só Federação) | **Não** satisfaz BR-010 para operação (INFERÊNCIA a partir de BR-009 + DEC-FA-002) |
| Colaborador temporário sem Área | **Permitido** entre auth e resolução de contexto — BR-011 / FT-PRIMEIRO-ACESSO tratam esse intervalo |
| Bloqueio antes ou depois da seleção | **Depois** da autenticação, **antes** da operação plena |

### 6.3 Sequência temporal — ponto de aplicação

```text
Login
  ↓                          [BR-010: NÃO aplica — ainda sem exigência operacional plena]
Autenticação Zimbra
  ↓                          [BR-010: NÃO — identidade ok ≠ operação]
Identificação Federação
  ↓                          [BR-010: NÃO — vínculo ainda incompleto é esperado no fluxo decisor]
Seleção Singular
  ↓                          [BR-010: NÃO — seleção em andamento]
Seleção Área
  ↓                          [BR-010: NÃO — até persistir vínculo/contexto]
Seleção Equipe (opcional)
  ↓
Criação/complementação COLABORADOR
  ↓                          [BR-010: AINDA NÃO — falta Contexto Ativo persistido]
Persistência Contexto Ativo + vínculo completo
  ↓                          [BR-010: APLICA — vínculo com Área + Contexto Ativo]
Autorização operacional
```

**FATO (FT-PRIMEIRO-ACESSO):** entre autenticação e Contexto Ativo, estados `LoadingContexts`, `SelectingContext`, `Blocked` **não** permitem operação — alinhado a BR-010.

**AMBIGUIDADE:** BR-010 diz “login recupera vínculo(s)” — no fluxo decisor o vínculo pode **ainda não existir** no login; recuperação seria vazia até complementação.

### 6.4 Compatibilidade com fluxo decisor

| Aspecto | Resultado |
|---------|-----------|
| Bloqueio operacional até Área + Contexto Ativo | **COMPATÍVEL** |
| Auth permanece com vínculo incompleto | **COMPATÍVEL** (DEC-FA-002 item 5) |
| “Recupera vínculos no login” | **AMBIGUIDADE** com criação de vínculo na seleção |

---

## 7. BR-041

### 7.1 Definição oficial (FATO)

> Um colaborador pode possuir **N vínculos** organizacionais; a sessão possui um único Contexto Ativo (`federationId`, `singularId`, `areaId`).

Base normativa: DEC-FA-003 (multi-contexto).

### 7.2 Análise

| Tema | Evidência |
|------|-----------|
| Contexto vs vínculo | Contexto Ativo = projeção de **um** vínculo na sessão |
| Cardinalidade colaborador | **N vínculos** por colaborador (BR-041 / DEC-FA-003) |
| Forma do contexto | `federationId` + `singularId` + `areaId` (+ `teamId` opcional em BR-012 / RN-PA-001) |
| Contexto sem Singular/Área | **Inválido** para operação (RN-PA-001, BR-012) |
| Primeiro acesso | Aplica — auto-seleção (N=1) ou UI (N>1) |
| Pressupõe `COLABORADOR → contexto`? | **Sim** — vínculos derivados do colaborador (RN-SESSION-001) |

### 7.3 Relação com DH-02

| Fonte | Cardinalidade prescrita |
|-------|------------------------|
| **DH-02 (confirmada)** | **1** vínculo por COLABORADOR |
| **BR-041 / DEC-FA-003** | **N** vínculos |
| **IMPLEMENTAÇÃO AS-IS** | **1** slot de FKs em `COLABORADOR` (nullable) |
| **FT-PRIMEIRO-ACESSO** | N vínculos; `contexts[]`; N=0/1/N>1 |

**CONTRADIÇÃO:** BR-041 (e DEC-FA-003, FT-PRIMEIRO-ACESSO) vs **DH-02**.

### 7.4 Compatibilidade do **formato** do vínculo com fluxo decisor

O **formato** Federação + Singular + Área + Equipe opcional é **COMPATÍVEL** com BR-012, RN-PA-001 e Contexto Ativo — desde que cardinalidade seja 1 (DH-02).

---

## 8. FT-PRIMEIRO-ACESSO

### 8.1 Artefatos analisados

| Artefato | Status |
|----------|--------|
| `specification.md` | APPROVED |
| `use-cases.md` | READY_FOR_REVIEW |
| `flows.md` | READY_FOR_REVIEW |
| `state-machine.md` | READY_FOR_REVIEW |
| `api.md` | READY_FOR_REVIEW (contratos propostos) |
| `acceptance-tests.md` | READY_FOR_REVIEW |
| `traceability.md` | READY_FOR_REVIEW |
| `tasks.md` | READY_FOR_REVIEW |

### 8.2 Modelo de fluxo documentado (FATO)

```text
Authenticated → LoadingContexts → (N=0 → Blocked | N=1 → Auto-select | N>1 → SelectingContext)
    → PersistingContext → LoadingHome → Operational
```

**Premissa central:** vínculos são **carregados** do colaborador já autenticado — **não criados** pelo fluxo.

**FATO (`specification.md` §Limites):**

- Não cadastra estrutura organizacional.
- Provisionamento administrativo de vínculos → **FT-COLABORADOR** (fora do escopo).
- FT-COLABORADOR = fonte de vínculos.

**FATO (`api.md` PA-API-003):** `PUT /session/context` persiste **Contexto Ativo** — valida pertencimento aos vínculos do colaborador (RN-PA-008); **não** atualiza FKs de `COLABORADOR`.

### 8.3 Matriz etapa × fluxo decisor

| Etapa | FT-PRIMEIRO-ACESSO | Fluxo decisor | Resultado |
|-------|-------------------|---------------|-----------|
| Entrada: e-mail + Zimbra | Hand-off pós FT-AUTH | Igual | **COMPATÍVEL** |
| Identificação Federação | Implícita no vínculo carregado | Domínio e-mail → Federação | **LACUNA** na spec (não modela mapeamento por domínio) |
| Descoberta Singular/Área/Equipe | Lista vínculos **existentes** | Navega hierarquia cadastrada | **CONTRADIÇÃO** |
| Seleção Singular obrigatória | Só se vínculo já contém singular | Usuário escolhe singular da estrutura | **CONTRADIÇÃO** |
| Seleção Área obrigatória | Idem | Usuário escolhe área | **CONTRADIÇÃO** |
| Equipe opcional | Em vínculo existente | Usuário escolhe opcionalmente | **CONTRADIÇÃO** (origem do dado) |
| Listas vazias (0 Singular/Área) | N=0 → `Blocked` (UC-PA-009) | Fluxo decisor não define | **LACUNA** no fluxo decisor |
| Persistência COLABORADOR | **Não** — assume pré-cadastro | Cria/complementa após seleção | **CONTRADIÇÃO** |
| `locateOrCreate` | Fora do escopo (FT-AUTH); cria só Federação | Complementa após seleção | **CONTRADIÇÃO** com hipótese “registro completo na seleção” |
| FKs nulas | Vínculo inválido (RN-PA-001) → N=0 → Blocked | Transitório até seleção | **CONTRADIÇÃO** (FT trata como bloqueio, não onboarding ativo) |
| Pós-onboarding: Contexto Ativo | Persistência dedicada (lacuna física INC-PA-004) | Necessário para operar | **COMPATÍVEL** (estado final) |
| Múltiplos vínculos | N>1 → UI seleção (BR-041) | DH-02 = 1 vínculo | **CONTRADIÇÃO** |
| Usuário já cadastrado | Carrega vínculo(s) | Pode pular seleção se completo | **COMPATÍVEL** (cenário N=1) |
| Usuário parcialmente cadastrado | N=0 → Blocked | Deveria continuar seleção | **CONTRADIÇÃO** |

### 8.4 Casos especiais

| Caso | FT-PRIMEIRO-ACESSO | Fluxo decisor |
|------|-------------------|---------------|
| Zero Singular na estrutura | N=0 → Blocked | **LACUNA** — não definido |
| Zero Área | N=0 → Blocked | **LACUNA** |
| Zero Equipe | `teamId: null` válido | **COMPATÍVEL** |
| Múltiplas Singulares | N vínculos distintos | Seleção hierárquica (1 escolha) | **CONTRADIÇÃO** com DH-02 se N vínculos |
| Usuário novo (só Federação no DB) | Blocked | Wizard de seleção | **CONTRADIÇÃO** |

### 8.5 Inconsistências já registradas na spec (FATO)

`traceability.md` INC-PA-001: modelo físico AS-IS (1 vínculo) vs BR-041 (N vínculos).

---

## 9. `locateOrCreate` — AS-IS × TO-BE

### 9.1 Cadeia de chamada (IMPLEMENTAÇÃO AS-IS)

```text
AuthenticationService.finalizeLogin()
    → ColaboradorService.locateOrCreate(identity)
    → sessionService.createSession()
    → issueAccessToken()  // inclui FKs atuais do colaborador no JWT
```

### 9.2 Comportamento detalhado

| # | Pergunta | AS-IS |
|---|----------|-------|
| 1 | Momento de execução | Imediatamente após validação Zimbra, **antes** de sessão/JWT |
| 2 | Campos de localização | `email` (ignore case) **ou** `zimbraId` |
| 3 | Campos na criação | `email`, `nome`, `zimbraId`, `ativo=S`, `federacaoId`, `dataCadastro` |
| 4 | Cria só com `COD_FEDERACAO`? | **Sim** — `federacaoId = authProperties.defaultFederationId()` |
| 5 | Atualização posterior Singular/Área/Equipe? | **Não** no fluxo de auth; `syncIdentity` só atualiza `zimbraId`/`nome` |
| 6 | Atualização esperada pela documentação? | **Sim** — DEC-DB-020: FKs NULL “até onboarding/admin”; FT-PRIMEIRO-ACESSO não implementa essa atualização |
| 7 | Risco de duplicidade | **Baixo** — busca por email e zimbraId antes de criar |
| 8 | Estado parcial persistido? | **Sim** — colaborador com só `COD_FEDERACAO` é estado persistido válido |
| 9 | Contradiz decisões? | **Alinhado** a DEC-DB-020; **contradiz** hipótese TO-BE do decisor (criar após seleção completa) |

### 9.3 Federação por domínio de e-mail

**IMPLEMENTAÇÃO AS-IS:** `defaultFederationId` via config (`application.auth.default-federation-id`) — **não** deriva domínio do e-mail.

**TO-BE (decisor):** `@unimedceara.com.br` e `@cariri.com.br` → mesma Federação.

**LACUNA:** nenhuma fonte aprovada define mapeamento domínio → `COD_FEDERACAO`.

### 9.4 Complementação de vínculo (AS-IS)

| Caminho | Existe? |
|---------|---------|
| Admin `ColaboradorApplicationService.create/update` | **Sim** — preenche `singularId`, `areaId`, `equipeId` com validação hierárquica |
| Onboarding FT-PRIMEIRO-ACESSO | **Não** — só persiste Contexto Ativo de vínculo **existente** |
| Auth / `locateOrCreate` | **Não** atualiza FKs org |

### 9.5 Classificação

| Dimensão | Classificação |
|----------|---------------|
| `locateOrCreate` atual | **IMPLEMENTAÇÃO AS-IS** alinhada a DEC-DB-020 — **não** é regra de negócio TO-BE do fluxo decisor |
| Criação após seleção hierárquica | **TO-BE (hipótese decisor)** — **sem** implementação nem decisão formal |

---

## 10. Reconciliação temporal

### 10.1 Estado transitório

```text
Usuário autenticado
    → COLABORADOR persistido (possivelmente só COD_FEDERACAO)
    → vínculo incompleto
    → usuário seleciona organização
    → vínculo completo estabelecido
```

| Fonte | Permite? |
|-------|----------|
| DEC-DB-020 | **Sim** — FKs NULL até onboarding/admin |
| DEC-FA-002 / BR-010 | **Sim** — bloqueio operacional, não invalida auth |
| Modelo físico | **Sim** — como estado intermediário **ou** federativo |
| FT-PRIMEIRO-ACESSO | **Parcial** — trata incompleto como N=0 → **Blocked**, não wizard |
| Fluxo decisor | **Sim** — explícito |

### 10.2 Estado permanente somente-Federação

```text
COLABORADOR → COD_FEDERACAO preenchido → COD_SINGULAR/COD_AREA/COD_EQUIPE NULL (permanente)
```

| Fonte | Permite? |
|-------|----------|
| Modelo físico (`03-physical-model.md`) | **Sim** — “Colaborador federativo: COD_FEDERACAO obrigatório; demais vínculos nulos” |
| DEC-DB-020 | **Ambíguo** — “até onboarding/admin” sugere transitório |
| DEC-FA-002 / BR-010 | **Sim, sem operação** — não admite operacional sem Área |
| Fluxo decisor (hipótese definitiva) | **Não** — estado final exige Singular + Área |

### 10.3 Pergunta central

> Um COLABORADOR pode existir temporariamente sem Singular/Área durante o onboarding, ou o sistema deve adiar a criação persistente até vínculo completo?

| Resposta | Evidência |
|----------|-----------|
| **Temporário permitido** | DEC-DB-020, `locateOrCreate` AS-IS, BR-011, DEC-FA-002 (bloqueio operacional) |
| **Adiar criação até completo** | Hipótese do fluxo decisor — **sem** decisão formal; **contradiz** DEC-DB-020 |
| **Permanente somente-Federação** | Modelo físico **permite**; fluxo decisor **nega** como estado definitivo; DEC-FA-002 permite existência **não operacional** |

---

## 11. Reconciliação de cardinalidade

### 11.1 Ocorrências de N vínculos / N contextos

| Fonte | Prescrição |
|-------|------------|
| BR-041 | N vínculos organizacionais |
| DEC-FA-003 | N vínculos + 1 Contexto Ativo |
| DEC-FA-001 item 2 | N vínculos → escolha |
| FT-PRIMEIRO-ACESSO | `contexts[]`; N=0/1/N>1 |
| FT-SESSION RN-SESSION-003 | Múltiplos vínculos → escolha |
| `execution-plan.md` CDD-PA-01 | Entidade/relação N vínculos |
| **DH-02** | **1 vínculo** |

### 11.2 Coerência do fluxo decisor com DH-02

O fluxo decisor (1 seleção hierárquica → 1 vínculo) é **coerente** com DH-02 no **resultado final**.

**INCOMPATÍVEL** com BR-041, DEC-FA-003 e FT-PRIMEIRO-ACESSO na cardinalidade **N** e no modelo de **seleção entre vínculos pré-existentes**.

---

## 12. Classificação por fonte

| Fonte | Compatível | Contraditória | Ambígua | Lacuna |
|-------|----------:|--------------:|--------:|-------:|
| DEC-DB-020 | 2 | 1 | 1 | 1 |
| DEC-FA-002 | 4 | 0 | 2 | 0 |
| BR-010 | 2 | 0 | 1 | 0 |
| BR-041 | 1 | 1 | 0 | 0 |
| FT-PRIMEIRO-ACESSO | 3 | 6 | 0 | 2 |

*Contagens por eixos distintos identificados nas matrizes — não exclusivos.*

---

## 13. Contradições encontradas

### C-01 — Momento de persistência do COLABORADOR

| | |
|--|--|
| **Fonte A** | DEC-DB-020 + `locateOrCreate` AS-IS |
| **Regra A** | Criar colaborador no login com só `COD_FEDERACAO`; FKs NULL até onboarding/admin |
| **Fonte B** | Fluxo decisor (hipótese TO-BE) |
| **Regra B** | Criar/complementar COLABORADOR **após** seleção Singular/Área/Equipe |
| **Incompatibilidade** | Mesmo primeiro acesso não pode ser “criar parcial no login” e “só persistir quando completo” sem decisão sobre qual prevalece |
| **Decisão afetada** | **DH-03** |
| **Gravidade** | **Alta** |

### C-02 — Origem do vínculo: provisionado vs auto-selecionado

| | |
|--|--|
| **Fonte A** | FT-PRIMEIRO-ACESSO (`specification.md`, UC-PA-001..004, PA-API-001/003) |
| **Regra A** | Carregar vínculos **existentes**; seleção entre vínculos; FT-COLABORADOR provisiona; PA não atualiza FKs |
| **Fonte B** | Fluxo decisor |
| **Regra B** | Usuário **seleciona** Singular/Área/Equipe na hierarquia e estabelece vínculo |
| **Incompatibilidade** | Modelos de onboarding diferentes: resolução de contexto vs wizard de vínculo |
| **Decisão afetada** | **DH-03**, escopo FT-PRIMEIRO-ACESSO |
| **Gravidade** | **Crítica** |

### C-03 — Usuário novo com só Federação: Blocked vs wizard

| | |
|--|--|
| **Fonte A** | FT-PRIMEIRO-ACESSO (RN-PA-004, UC-PA-009, AT-PA-003) |
| **Regra A** | 0 vínculos válidos → estado `Blocked`; sem UI de cadastro de vínculo |
| **Fonte B** | Fluxo decisor + `locateOrCreate` (colaborador só Federação) |
| **Regra B** | Após auth, usuário deve **selecionar** organização |
| **Incompatibilidade** | AS-IS + spec atual bloqueiam; fluxo decisor exige continuação |
| **Decisão afetada** | **DH-03**, **DH-04** |
| **Gravidade** | **Crítica** |

### C-04 — Cardinalidade N vínculos vs DH-02

| | |
|--|--|
| **Fonte A** | BR-041, DEC-FA-003, FT-PRIMEIRO-ACESSO |
| **Regra A** | N vínculos; seleção quando N>1 |
| **Fonte B** | DH-02 (confirmada) |
| **Regra B** | 1 vínculo por COLABORADOR |
| **Incompatibilidade** | Normas vigentes contradizem premissa confirmada |
| **Decisão afetada** | DH-01 (supersession DEC-FA-003), FT-PRIMEIRO-ACESSO |
| **Gravidade** | **Alta** (indireta em DH-03/04) |

### C-05 — Estado definitivo: federativo permanente vs vínculo completo

| | |
|--|--|
| **Fonte A** | `database/model/03-physical-model.md` |
| **Regra A** | “Colaborador federativo” — só `COD_FEDERACAO`; demais FKs nulos (**permanente documentado**) |
| **Fonte B** | Fluxo decisor (hipótese) |
| **Regra B** | Estado definitivo = Federação + Singular + Área (+ Equipe opcional) |
| **Incompatibilidade** | Modelo físico admite permanência sem Singular/Área; fluxo decisor nega como estado final |
| **Decisão afetada** | **DH-04** |
| **Gravidade** | **Alta** |

---

## 14. Ambiguidades encontradas

### A-01 — Significado de “onboarding” em DEC-DB-020

“FKs NULL até **onboarding/admin**” não distingue auto-seleção hierárquica (fluxo decisor) de provisionamento administrativo (FT-COLABORADOR) nem de seleção entre vínculos (FT-PRIMEIRO-ACESSO).

### A-02 — DEC-FA-001 “seleção direta singular/área incorporada”

`docs/governance/03-open-decisions.md` registra alternativa legado CMS “incorporada como base do fluxo de contexto”. Pode significar wizard hierárquico **ou** seleção entre vínculos pré-definidos. FT-PRIMEIRO-ACESSO adotou a segunda; fluxo decisor adota a primeira.

### A-03 — Colaborador federativo: permanente ou só transitório

- Modelo físico: permanente válido (não operacional).
- DEC-DB-020: transitório até onboarding/admin.
- DEC-FA-002: não operacional sem Área — não proíbe existência cadastral.

### A-04 — BR-010 “login recupera vínculo(s)”

Compatível com vínculo pré-existente. Ambíguo quando vínculo é **criado** durante o mesmo fluxo de primeiro acesso.

### A-05 — Identificação de Federação

Fluxo decisor: por domínio de e-mail. AS-IS: `defaultFederationId`. Nenhuma decisão formal sobre mapeamento multi-domínio → mesma Federação.

---

## 15. Lacunas (impedem formalizar DH-03 ou DH-04)

| ID | Lacuna | Bloqueia |
|----|--------|----------|
| L-01 | Regra formal: persistir no login (parcial) **ou** após seleção (completo) | **DH-03** |
| L-02 | Contrato/API para completar FKs de `COLABORADOR` no onboarding (não existe; PA-API-003 só Contexto Ativo) | **DH-03** |
| L-03 | Mapeamento domínio e-mail → `COD_FEDERACAO` (multi-domínio → mesma Federação) | **DH-03** |
| L-04 | Comportamento quando estrutura org tem 0 Singular ou 0 Área disponível | **DH-03** |
| L-05 | Definição normativa: colaborador federativo é estado de negócio **permanente** ou **apenas transitório** | **DH-04** |
| L-06 | Supersession formal de BR-041/DEC-FA-003 após DH-02 (cardinalidade 1:1) | Indireta (escopo PA) |

---

## 16. Impactos para DH-03

**Pergunta:** As evidências permitem determinar **quando e como** o `COLABORADOR` deve ser persistido?

**Classificação: `NÃO`**

| Evidência a favor de persistência no login (parcial) | Evidência a favor de persistência após seleção (completa) |
|------------------------------------------------------|----------------------------------------------------------|
| DEC-DB-020 explícito | Fluxo decisor explícito |
| `locateOrCreate` AS-IS implementado | RN-PA-001 exige vínculo completo para “válido” |
| DA-AUTH-011: criação no login é escopo FT-AUTH | FT-PRIMEIRO-ACESSO não atualiza FKs — gap se vínculo nasce na seleção |
| DEC-DB-027 mantém locateOrCreate com FKs NULL | Hipótese TO-BE não formalizada |

**INFERÊNCIA:** Formalizar DH-03 exige escolha humana entre modelos **incompatíveis** documentados em C-01, C-02, C-03 — não deduzível só de evidências convergentes.

---

## 17. Impactos para DH-04

**Pergunta:** As evidências permitem determinar se “somente-Federação” é estado de negócio válido ou apenas transitório de onboarding?

**Classificação: `NÃO`**

| Permanente válido (não operacional) | Apenas transitório |
|-------------------------------------|-------------------|
| Modelo físico: “colaborador federativo” | DEC-DB-020: “até onboarding/admin” |
| DEC-FA-002: operacional exige Área — não proíbe cadastro incompleto | Fluxo decisor: destino é vínculo completo |
| Fixtures/tests AS-IS com federation-only | FT-PRIMEIRO-ACESSO: 0 vínculos → Blocked (não distingue transitório) |

**INFERÊNCIA:** Fontes coexistem sem hierarquia de prevalência definida entre “federativo permanente” (modelo físico) e “transitório até complementação” (DEC-DB-020 / fluxo decisor).

---

## 18. Conclusão — critérios de conclusão

| # | Pergunta | Resposta |
|---|----------|----------|
| 1 | Fluxo decisor compatível com DEC-DB-020? | **Parcial** — Federação ok; momento de criação **contraditório** |
| 2 | Compatível com DEC-FA-002? | **Sim** no estado final operacional; **ambíguo** na origem do vínculo |
| 3 | Compatível com BR-010? | **Sim** no bloqueio operacional; **ambíguo** em “recupera vínculos no login” |
| 4 | Compatível com BR-041? | **Não** — cardinalidade N vs DH-02; formato do vínculo compatível |
| 5 | Compatível com FT-PRIMEIRO-ACESSO? | **Não** — modelo provisionado/Blocked vs wizard/auto-vínculo |
| 6 | `locateOrCreate` é regra de negócio ou implementação transitória? | **Implementação AS-IS** ancorada em DEC-DB-020 — **não** expressa TO-BE do fluxo decisor |
| 7 | COLABORADOR deve existir persistido antes da seleção organizacional? | **Indeterminado** — DEC-DB-020/AS-IS diz **sim** (parcial); fluxo decisor sugere **não** (ou complementar depois) |
| 8 | Estado somente-Federação: permanente, transitório ou inválido? | **Indeterminado** — modelo físico: permanente não operacional; DEC-DB-020: transitório; fluxo decisor: inválido como definitivo |
| 9 | Contradição impede formalizar DH-03? | **Sim** — C-01, C-02, C-03 + lacunas L-01..L-04 |
| 10 | Contradição impede formalizar DH-04? | **Sim** — C-05 + ambiguidade A-03 + lacuna L-05 |

### Síntese

O fluxo decisor é **coerente com DH-02** e com o **estado operacional final** prescrito por DEC-FA-002, BR-010 e BR-012. Porém **não é compatível** com o modelo normativo e especificado de FT-PRIMEIRO-ACESSO (vínculos pré-provisionados, N cardinalidade, Blocked sem wizard) nem com a cardinalidade de BR-041/DEC-FA-003.

DEC-DB-020 e `locateOrCreate` **autorizam** estado parcial persistido no login — alinhado a um onboarding **transitório**, mas **em tensão** com criação apenas após seleção completa.

A existência de **colaborador federativo** no modelo físico impede concluir que somente-Federação é **somente** transitório sem decisão humana (DH-04).

---

## Referências consultadas

| Fonte | Caminho |
|-------|---------|
| DEC-DB-020 | `database/model/05-decisions-and-risks.md` |
| DEC-FA-001, 002, 003 | `docs/governance/03-open-decisions.md` |
| BR-009..012, BR-040..042 | `docs/domain/09-business-rules.md` |
| Modelo físico COLABORADOR | `database/model/03-physical-model.md` |
| FT-PRIMEIRO-ACESSO | `specs/features/primeiro-acesso/*` |
| FT-SESSION | `specs/features/session/specification.md` |
| DA-AUTH-011 | `specs/features/authentication/decisions.md` |
| AS-IS backend | `ColaboradorService.java`, `AuthenticationService.java`, `ColaboradorApplicationService.java` |
| Análises prévias | `construction/review/vinculo-organizacional-blocking-decisions-analysis.md` |

---

*Documento de reconciliação — sem alteração de implementação ou governança.*
