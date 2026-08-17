# Reconciliação DH-PA-03 × DEC-DB-027

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/primeiro-acesso-dh-pa-03-db-reconciliation.md` |
| Feature | FT-PRIMEIRO-ACESSO |
| Data | 2026-08-17 |
| Tipo | **Análise e reconciliação** |
| Categoria documental | Evidence |
| Status | **ANÁLISE CONCLUÍDA** — sem implementação |
| IDs relacionados | DH-PA-01, DH-PA-02, DH-PA-03, DH-03, DH-04, DEC-DB-027, DEC-ORG-002, DEC-DB-028, BR-011, BR-045 |

**Classificação usada:** `DECISÃO` · `REGRA DE NEGÓCIO` · `AS-IS` · `ESPECIFICAÇÃO` · `MODELO DE DADOS` · `IMPLEMENTAÇÃO` · `GAP` · `DOCUMENTAÇÃO DESATUALIZADA`

**Restrições cumpridas nesta etapa:**

- Nenhum código, DDL, migration, JPA, API, frontend, teste ou seed foi alterado.
- Nenhum artefato de governança foi alterado.
- Nenhuma decisão humana nova foi criada.
- DEC-DB-027 não foi alterada.

---

## 1. Resumo executivo

### Pergunta principal

> **DH-PA-03 e DEC-DB-027 podem coexistir?**

### Veredito

**Classificação: C — Existe conflito normativo.**

As duas decisões estabelecem requisitos **incompatíveis** para o **mesmo evento de negócio**: a **criação do COLABORADOR** ao final do Primeiro Acesso (DH-03).

| Decisão | Requisito no momento da criação do COLABORADOR no PA |
|---------|------------------------------------------------------|
| **DH-PA-03** | CARGO **não é requisito**; COLABORADOR pode atingir estado operacional **sem CARGO** |
| **DEC-DB-027** | `COLABORADOR.COD_CARGO` **NOT NULL**; CARGO **obrigatório na criação**; proíbe `nullable=true` |

### Implicação prática

| Cenário | Viabilidade |
|---------|-------------|
| Implementar **Primeiro Acesso** conforme DH-PA-01/02/03 **sem** implementar DEC-DB-027 | **Viável** — AS-IS não possui `CARGO`/`COD_CARGO` |
| Implementar **Primeiro Acesso** **e** DEC-DB-027 **simultaneamente**, literais | **Inviável** sem violar DH-PA-03 ou DEC-DB-027 |
| Implementar DEC-DB-027 como escrita hoje | **Bloqueado** até reconciliação formal |

### Menor próximo passo do projeto

1. **Decisão humana** sobre como reconciliar DEC-DB-027 com DH-PA-03 (escopo, nullable, obrigatoriedade diferida ou supersession parcial de item de DEC-DB-027).
2. **Paralelizável:** implementação de PA núcleo (credencial temporária, domínio→Singular, wizard vínculo) **sem** DDL de `CARGO`, pois DEC-DB-027 ainda não está materializada no banco.

---

## 2. Decisões consideradas

Decisões humanas **aprovadas e não reabertas** nesta análise:

| ID | Conteúdo normativo relevante | Fonte |
|----|------------------------------|-------|
| **DH-PA-01** | Credencial temporária; sem `AUTH_SESSAO` operacional; COLABORADOR após vínculo completo | `docs/governance/03-open-decisions.md` |
| **DH-PA-02** | Domínio → Singular 1:1; domínio sem Singular bloqueia PA | idem |
| **DH-PA-03** | CARGO não requisito para criação/operação no PA; definível posteriormente | idem |
| **DH-03** | COLABORADOR persistido **após** vínculo mínimo completo | idem |
| **DH-04** | Federação + Singular + Área obrigatórios; Equipe opcional | idem |
| **DEC-ORG-002** | CARGO = função organizacional; CARGO ≠ PAPEL ≠ ADMIN_* | idem |
| **DEC-DB-028** | 1 vínculo; identidade autenticada pode existir antes do COLABORADOR | `database/model/05-decisions-and-risks.md` |

Decisão investigada (**inalterada** nesta etapa):

| ID | Status | Fonte canônica |
|----|--------|----------------|
| **DEC-DB-027** | APPROVED (2026-08-14); sem implementação | `database/model/05-decisions-and-risks.md` § DEC-DB-027; referência em `docs/governance/03-open-decisions.md` |

---

## 3. Evidências encontradas

### 3.1 Governança — DEC-DB-027 (texto integral investigado)

**Fonte:** `database/model/05-decisions-and-risks.md` (linhas 445–614), espelhada em `docs/governance/03-open-decisions.md` § DEC-DB-027 (referência).

| # | O que DEC-DB-027 determina | Classificação |
|---|---------------------------|---------------|
| 1 | **CARGO** como catálogo próprio (`COD_CARGO`, `NOM_CARGO`, `FLG_ATIVO`, auditoria) | `DECISÃO` / `MODELO DE DADOS` |
| 2 | Cardinalidade **1 COLABORADOR → 1 CARGO**; **CARGO → N COLABORADORES** | `DECISÃO` |
| 3 | **CARGO obrigatório no momento da criação** — "o cadastro não deve permitir colaborador sem cargo" | `DECISÃO` / `REGRA DE NEGÓCIO` |
| 4 | `COLABORADOR.COD_CARGO` FK **NOT NULL** | `DECISÃO` / `MODELO DE DADOS` |
| 5 | Proibição explícita: "Não utilizar `nullable=true` como solução temporária do modelo TO-BE" | `DECISÃO` |
| 6 | CARGO **não** no vínculo organizacional; sem `ATRIBUICAO_CARGO` | `DECISÃO` |
| 7 | CARGO ≠ PAPEL ≠ ADMIN_* | `DECISÃO` |
| 8 | Escopo: modelo físico TO-BE Oracle; **sem implementação** na aprovação | `DECISÃO` |
| 9 | Momento de obrigatoriedade: diagrama `CREATE COLABORADOR → CARGO obrigatório` — **sem exceção** para Primeiro Acesso | `DECISÃO` |
| 10 | Ponto de reconciliação DH-PA-03 já registrado (2026-08-17) — **sem solução escolhida** | `DECISÃO` (meta) |

**Escopo de DEC-DB-027:** persistência Oracle de catálogo `CARGO` e vínculo obrigatório `COLABORADOR.COD_CARGO` na **criação de qualquer colaborador**, não restrito a fluxo administrativo.

**Entidades/campos afetados (TO-BE):** tabela `CARGO`; coluna `COLABORADOR.COD_CARGO`; sequence `SQ_CARGO_COD_CARGO`; FK e índice.

### 3.2 Governança — DH-PA-03

**Fonte:** `docs/governance/03-open-decisions.md` § DH-PA-03.

| Conteúdo | Classificação |
|----------|---------------|
| CARGO não requisito para **criação** nem **existência operacional** no PA | `DECISÃO` / `REGRA DE NEGÓCIO` |
| CARGO não bloqueia PA, autenticação, vínculo organizacional | `REGRA DE NEGÓCIO` (consequência) |
| CARGO poderá ser definido **posteriormente** | `DECISÃO` |
| CARGO **não** é etapa do wizard | `REGRA DE NEGÓCIO` |
| Separação CARGO/PAPEL preservada | `DECISÃO` |
| Itens técnicos (nullable, default, seed, wizard) **deliberadamente não decididos** | `DECISÃO` (limite) |

### 3.3 Domínio

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| `docs/domain/09-business-rules.md` — BR-011 | Fluxo PA: domínio → Singular → Área → Equipe opcional; **CARGO não participa** | `REGRA DE NEGÓCIO` |
| `docs/domain/09-business-rules.md` — BR-045 | CARGO não requisito para PA nem operação; reconciliação DEC-DB-027 pendente | `REGRA DE NEGÓCIO` |
| `docs/domain/02-business-glossary.md` | **Sem** termo canônico "cargo" / "CARGO" | `GAP` |
| `docs/domain/04-domain-concepts.md` | Sem entidade CARGO documentada | `GAP` |

### 3.4 Primeiro Acesso — specs

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| `specs/features/primeiro-acesso/specification.md` §11 | TO-BE: criação COLABORADOR após vínculo; DH-PA-03 aprovada; reconciliação pendente | `ESPECIFICAÇÃO` |
| Grep `specs/features/primeiro-acesso/**` por `cargo`/`CARGO` | **Zero** ocorrências | `ESPECIFICAÇÃO` — PA não modela CARGO |
| `construction/review/primeiro-acesso-dh-pa-03-analysis.md` | Análise pré-decisão; alternativas C1/C2/C3 como evidência histórica | `DOCUMENTAÇÃO` (evidence) |
| `construction/review/primeiro-acesso-blocking-decisions-package.md` §7 | C3 (cargo posterior) marcada **REJEITADA** com base em DEC-DB-027 — **pré-DH-PA-03** | `DOCUMENTAÇÃO DESATUALIZADA` (parcial) |

### 3.5 COLABORADOR — specs e API

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| `specs/features/colaborador/specification.md` | RN-001..009 — **sem** cargo | `ESPECIFICAÇÃO` |
| `specs/features/colaborador/api.md` | DTOs **sem** `cargoId` | `ESPECIFICAÇÃO` |
| `specs/features/authentication/**` | **Sem** menção a CARGO | `ESPECIFICAÇÃO` |

### 3.6 Banco — AS-IS

| Pergunta | Resposta | Classificação |
|----------|----------|---------------|
| Tabela `CARGO` existe? | **Não** — `database/ddl/003-create-tables.sql` | `AS-IS` |
| `COLABORADOR.COD_CARGO` existe? | **Não** — DDL L158–179 | `AS-IS` |
| `COD_CARGO` NOT NULL? | Coluna **inexistente** | `AS-IS` |
| FK COLABORADOR → CARGO? | **Não** | `AS-IS` |
| Seed de CARGO? | **Não** — `database/ddl/008-initial-data.sql` sem cargo | `AS-IS` |
| `DES_CARGO` legado? | Removido por `V007__colaborador_ssot_alignment.sql` | `AS-IS` |
| `database/model/04-entity-catalog.md` | 23 entidades — **sem** CARGO | `AS-IS` / `GAP` TO-BE |

**Campos obrigatórios AS-IS em `COLABORADOR`:** `COD_COLABORADOR`, `COD_FEDERACAO`, `NOM_COLABORADOR`, `DES_EMAIL`, `ID_ZIMBRA`, `FLG_ATIVO`, `DAT_CADASTRO`. `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE` são **nullable**.

### 3.7 Backend — AS-IS

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| `ColaboradorEntity.java` | **Sem** `cargoId` / `COD_CARGO` | `IMPLEMENTAÇÃO` |
| Grep `backend/**` por `cargo`/`CARGO`/`Cargo` | **Zero** referências | `IMPLEMENTAÇÃO` |
| `ColaboradorService.createColaborador` | Login: email, nome, zimbraId, `COD_FEDERACAO` default; **sem cargo** | `IMPLEMENTAÇÃO` |
| `ColaboradorApplicationService.create` | CRUD admin: vínculos org; **sem cargo** | `IMPLEMENTAÇÃO` |
| `AuthenticationService.finalizeLogin` | `locateOrCreate` **antes** da sessão — **CONFLICT** com DH-03/DH-PA-01 | `IMPLEMENTAÇÃO` / `GAP` |
| `CreateColaboradorRequest` | **Sem** `cargoId` | `IMPLEMENTAÇÃO` |

### 3.8 Frontend — AS-IS

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| Grep `frontend/**` por `cargo`/`CARGO` | **Zero** referências | `IMPLEMENTAÇÃO` |
| Wizard PA | **Não** implementado com step de CARGO | `IMPLEMENTAÇÃO` / `GAP` |

### 3.9 Testes — AS-IS

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| Grep `backend/src/test/**` por `cargo`/`CARGO` | **Zero** referências | `IMPLEMENTAÇÃO` |

---

## 4. AS-IS relevante

### 4.1 Fluxo de autenticação e criação de COLABORADOR (implementação atual)

```text
Zimbra autentica
    ↓
AuthenticationService.finalizeLogin
    ↓
ColaboradorService.locateOrCreate(identity)     ← cria COLABORADOR se inexistente
    ↓
SessionService.createSession(colaborador)       ← AUTH_SESSAO com COD_COLABORADOR NOT NULL
    ↓
JWT operacional (sub = colaboradorId)
    ↓
estado operacional (parcial — vínculo org pode ser NULL)
```

**Onde CARGO aparece:** **em nenhum ponto** do fluxo AS-IS.

### 4.2 Criação de COLABORADOR — campos persistidos hoje

| Origem | Campos preenchidos | CARGO |
|--------|-------------------|-------|
| Login (`locateOrCreate`) | email, nome, zimbraId, `COD_FEDERACAO` (config), `FLG_ATIVO` | **Ausente** |
| CRUD admin (`create`) | identidade + FKs org + gestor | **Ausente** |

### 4.3 Obrigatoriedade de COD_CARGO — por fonte

| Fonte | COD_CARGO obrigatório? | Evidência |
|-------|------------------------|-----------|
| Banco AS-IS | **Não** — coluna inexistente | DDL |
| JPA AS-IS | **Não** | `ColaboradorEntity` |
| API AS-IS | **Não** | `api.md`, DTOs |
| Frontend AS-IS | **Não** | grep |
| Código AS-IS | **Não** | grep backend |
| DEC-DB-027 TO-BE | **Sim** — NOT NULL na criação | `05-decisions-and-risks.md` L460, L503 |
| DH-PA-03 | **Não** no Primeiro Acesso | governança |

---

## 5. TO-BE derivado das decisões aprovadas

Fluxo normativo **sem inventar etapas** (DH-PA-01 + DH-PA-02 + DH-PA-03 + DH-03 + DH-04):

```text
Zimbra autentica
    ↓
Portal verifica COLABORADOR
    ↓
COLABORADOR inexistente
    ↓
Primeiro Acesso
    ↓
credencial temporária (sem AUTH_SESSAO operacional)
    ↓
domínio → Singular
    ↓
Área
    ↓
Equipe opcional
    ↓
criação do COLABORADOR          ← DH-03: após vínculo completo
    ↓
estado operacional

                 CARGO
                   │
                   └── não bloqueia o fluxo
                       poderá ser definido posteriormente
```

**Pontos normativos do TO-BE:**

- INSERT final do wizard **não inclui** CARGO (DH-PA-03).
- COLABORADOR persistido com Federação + Singular + Área (+ Equipe opcional) (DH-04).
- Usuário operacional **sem** CARGO atribuído (DH-PA-03).
- Processo de definição posterior de CARGO: **não especificado** (delegado).

---

## 6. Reconciliação DH-PA-03 × DEC-DB-027

### 6.1 Confronto direto

| Dimensão | DH-PA-03 | DEC-DB-027 |
|----------|----------|------------|
| CARGO no wizard PA | **Proibido** como etapa obrigatória | Silencioso no fluxo PA; exige cargo na **criação** |
| INSERT do COLABORADOR no PA | **Permitido sem CARGO** | **Exige** `COD_CARGO NOT NULL` |
| Estado operacional sem CARGO | **Permitido** | **Incompatível** se NOT NULL sem valor |
| Definição posterior de CARGO | **Permitida** | **Incompatível** com item 4 ("obrigatório no momento da criação") |
| Nullable como atalho | Não decidido (delegado) | **Explicitamente proibido** |
| Escopo | Primeiro Acesso (criação self-service) | **Todo** COLABORADOR na criação |

### 6.2 Avaliação das hipóteses de classificação

| Hipótese | Avaliação | Evidência |
|----------|-----------|-----------|
| **A — Não existe conflito** | **Rejeitada** | DEC-DB-027 item 3–4 aplica-se ao evento "criação do COLABORADOR" sem excluir PA; DH-PA-03 nega requisito de CARGO nesse mesmo evento |
| **B — Conflito de escopo** | **Parcialmente aplicável, insuficiente** | DEC-DB-027 é decisão de **modelo de dados**; DH-PA-03 é **regra de fluxo PA**. Porém DEC-DB-027 declara regra de domínio ("obrigatório no momento da criação"), não apenas constraint física opcional |
| **C — Conflito normativo** | **Adotada** | Dois requisitos mutuamente excludentes para o INSERT do PA |
| **D — Evidência insuficiente** | **Rejeitada** | Textos decisórios são explícitos e consultados na fonte canônica |

### 6.3 Tentativas de coexistência analisadas (não aprovadas)

| Caminho | Compatível com DH-PA-03? | Compatível com DEC-DB-027 literal? | Status |
|-------|--------------------------|-------------------------------------|--------|
| INSERT sem `COD_CARGO` | ✅ | ❌ (NOT NULL) | Conflito |
| `COD_CARGO` nullable | ✅ (operacional sem cargo) | ❌ (proíbe nullable) | Conflito |
| Cargo default sistêmico no INSERT (C2) | ⚠️ Usuário operacional com cargo genérico — **não decidido** por DH-PA-03 | ✅ satisfaz NOT NULL | **Exige decisão humana** |
| Seleção de cargo no wizard (C1) | ❌ (DH-PA-03 proíbe etapa) | ✅ | Conflito com DH-PA-03 |
| Adiar criação do COLABORADOR até cargo conhecido | ❌ (viola DH-03/DH-PA-01) | ✅ | Conflito com DH-PA-01/03 |
| Implementar PA sem DDL de CARGO | ✅ | ⚠️ DEC-DB-027 não materializada | **Viável temporariamente** |

### 6.4 Resposta objetiva

**DH-PA-03 e DEC-DB-027 não podem coexistir na implementação literal simultânea do INSERT do Primeiro Acesso.**

Podem coexistir **temporariamente no projeto** porque DEC-DB-027 **não foi implementada** (sem DDL/JPA). Isso é **GAP de implementação**, não reconciliação normativa.

**O conflito exige decisão humana** antes de materializar DEC-DB-027 ou de fechar o contrato de criação do COLABORADOR no PA.

---

## 7. Impacto por camada

| Elemento | Situação atual | Relação com DH-PA-03 | Impacto |
|----------|----------------|----------------------|---------|
| **Regra de negócio** | BR-011, BR-045 alinhadas a DH-PA-03; DEC-DB-027 exige cargo na criação | DH-PA-03 prevalece no eixo PA; DEC-DB-027 prevalece no eixo modelo TO-BE global | **Conflito normativo** entre decisões |
| **DEC-DB-027** | Aprovada; não implementada | Ponto de reconciliação já registrado | **Bloqueia** implementação literal de `COD_CARGO NOT NULL` no PA |
| **COLABORADOR (domínio)** | Criado após vínculo no TO-BE (DH-03); antecipado no AS-IS (`locateOrCreate`) | PA cria sem CARGO | Implementação PA deve **não exigir** cargo no complete |
| **CARGO (domínio)** | Entidade aprovada (DEC-ORG-002); sem persistência | Não participa do PA | Catálogo pode ser criado **depois** da reconciliação |
| **COD_CARGO** | Inexistente AS-IS; NOT NULL TO-BE | PA não fornece valor | **Ponto central** da reconciliação |
| **DDL** | Sem `CARGO`/`COD_CARGO` | PA implementável sem alteração DDL de cargo | DDL de DEC-DB-027 **adiada** até reconciliação |
| **JPA** | Sem `CargoEntity` | Idem | Sem impacto imediato no PA |
| **Backend** | Cria colaborador sem cargo | Alinhado a DH-PA-03; desalinhado a DEC-DB-027 TO-BE | Refatorar `locateOrCreate` (DH-PA-01) independente de CARGO |
| **API** | Sem `cargoId` | Alinhado a DH-PA-03 | Complete onboarding **não deve** exigir `cargoId` |
| **Frontend** | Sem UI de cargo | Alinhado a DH-PA-03 | Wizard **sem** step CARGO |
| **Specification PA** | §11 registra DH-PA-03; sem campo cargo nos fluxos | Parcialmente atualizada | Reconciliar UC/API/estados em etapa posterior |
| **Testes** | Sem cobertura cargo | Neutro | Testes PA não devem assumir CARGO obrigatório |

---

## 8. Menor conjunto de mudanças

### 8.1 Pode ser derivado diretamente (sem nova decisão humana)

| # | Derivação | Base |
|---|-----------|------|
| D1 | Wizard PA **não** inclui step de seleção de CARGO | DH-PA-03 |
| D2 | API de complete onboarding **não** exige `cargoId` | DH-PA-03 |
| D3 | Estado operacional alcançável com COLABORADOR **sem** `COD_CARGO` persistido | DH-PA-03 + AS-IS (coluna ausente) |
| D4 | CARGO **não** participa de resolução domínio→Singular→Área→Equipe | DH-PA-03 |
| D5 | CARGO **não** determina PAPEL nem ADMIN_* | DEC-ORG-002, DH-PA-03, DEC-DB-027 item 5 |
| D6 | Implementação PA núcleo (DH-PA-01/02, DH-03, DH-04) **pode avançar** sem DDL `CARGO` | AS-IS + DEC-DB-027 não implementada |
| D7 | Refatoração auth (`locateOrCreate` fora do login) é **independente** da reconciliação CARGO | DH-PA-01, GAP-028-01 |

### 8.2 Precisa de reconciliação (DH-PA-03 × DEC-DB-027)

| # | Questão | Por que bloqueia |
|---|---------|------------------|
| R1 | **Escopo da obrigatoriedade** de DEC-DB-027 item 3–4: aplica-se ao INSERT do PA ou apenas a cadastros posteriores/administrativos? | Define se PA pode criar COLABORADOR sem `COD_CARGO` |
| R2 | **`COLABORADOR.COD_CARGO` nullable vs NOT NULL** no modelo físico | DH-PA-03 permite operação sem CARGO; DEC-DB-027 proíbe nullable |
| R3 | **Momento** em que `COD_CARGO` se torna obrigatório (se ever) | "Na criação" (DEC-DB-027) vs "posteriormente" (DH-PA-03) |
| R4 | **Cardinalidade 1:1** DEC-DB-027 item 2 quando CARGO ainda não definido | "Todo COLABORADOR possui exatamente um cargo" vs ausência temporária |
| R5 | **Processo** de definição posterior de CARGO (quem, quando, qual fluxo) | DH-PA-03 delega; nenhuma decisão existente define |
| R6 | Atualização de artefatos que assumem DEC-DB-027 literal no PA (`blocking-decisions-package` §7 C3, análise C1/C2) | `DOCUMENTAÇÃO DESATUALIZADA` pós-DH-PA-03 |

### 8.3 Precisa de nova decisão humana (somente se reconciliação não resolver)

| # | Questão | Condição |
|---|---------|----------|
| H1 | **Supersession parcial ou emenda de DEC-DB-027** (itens 3–4 e/ou NOT NULL) | Se R1–R4 não admitirem interpretação compatível |
| H2 | **Cargo default sistêmico** no INSERT (sem step de UI) | Se reconciliação escolher satisfazer NOT NULL sem wizard |
| H3 | **Catálogo mínimo** e governança de cargos | Se CARGO for exigido em fluxos não-PA antes do PA |
| H4 | **Quem pode atribuir** CARGO posteriormente (admin, RH, self-service) | DH-PA-03 não define processo |

> **Nota:** H1–H4 são **perguntas para o decisor**, não novas decisões criadas nesta análise.

---

## 9. Questões que ainda exigem decisão humana

Prioridade para desbloquear implementação **com** DEC-DB-027:

1. **A obrigatoriedade de CARGO na criação (DEC-DB-027 item 4) abrange o Primeiro Acesso?**
   - Se **sim** → conflito com DH-PA-03 permanece; exige emenda de DEC-DB-027 ou DH-PA-03 (proibido reabrir sem autoridade).
   - Se **não** → registrar escopo explícito em DEC-DB-027 (governança futura, fora desta etapa).

2. **COLABORADOR operacional sem `COD_CARGO` persistido é estado válido permanente ou transitório?**
   - DH-PA-03 permite operação sem CARGO; não define prazo nem obrigação futura.

3. **Qual processo aprovado define CARGO posteriormente?**
   - Nenhuma decisão existente no repositório.

4. **Cargo default sistêmico no INSERT (sem UI) satisfaz DH-PA-03?**
   - Ambíguo: tecnicamente haveria `COD_CARGO`, mas semanticamente CARGO não estaria "definido" para o usuário.

---

## 10. Recomendação técnica

> **RECOMENDAÇÃO TÉCNICA — NÃO É DECISÃO HUMANA**

### Menor caminho para PA respeitar DH-PA-01, DH-PA-02 e DH-PA-03

**Fase 1 — Imediata (sem DEC-DB-027 materializada):**

1. Implementar credencial temporária e fluxo PA sem `locateOrCreate` no login.
2. Implementar resolução domínio→Singular (DH-PA-02) e wizard Área/Equipe.
3. Endpoint de complete onboarding cria `COLABORADOR` com vínculo completo **sem** campo `cargoId`.
4. **Não** criar migration `CARGO`/`COD_CARGO` nesta fase.

**Fase 2 — Após reconciliação humana DEC-DB-027 × DH-PA-03:**

Opções técnicas **mutuamente dependentes de H1–H4** (não escolher sem decisor):

| Opção | Descrição | Pré-requisito humano |
|-------|-----------|---------------------|
| **T-A** | `COD_CARGO` **NULL** permitido; NOT NULL apenas em fluxo administrativo de atribuição | Emenda DEC-DB-027 item 4–5 |
| **T-B** | `COD_CARGO` NOT NULL com **seed default** atribuído no INSERT pelo backend (invisível ao usuário PA) | Decisor aceita que "sem CARGO" = sem cargo **semântico**, não sem FK |
| **T-C** | CHECK constraint ou trigger: `COD_CARGO` obrigatório exceto flag/estado de onboarding | Nova regra de modelo — decisão humana |
| **T-D** | Tabela `COLABORADOR` sem `COD_CARGO`; atribuição só via evolução futura de modelo | Supersession ampla de DEC-DB-027 — improvável sem H1 |

**Recomendação de sequência:** executar **Fase 1** em paralelo à deliberação de **R1–R5**; **não** iniciar DDL DEC-DB-027 até resposta a H1.

---

## 11. Análise específica do COLABORADOR

| Pergunta | Resposta | Evidência | Classificação |
|----------|----------|-----------|---------------|
| Quando o COLABORADOR é criado atualmente? | No **login**, via `locateOrCreate` | `AuthenticationService.finalizeLogin` L159; `ColaboradorService` L32–36 | `IMPLEMENTAÇÃO` (AS-IS) |
| Quando deve ser criado (TO-BE)? | Após vínculo completo no **PA** | DH-03, DH-PA-01 | `DECISÃO` |
| Quais campos são obrigatórios AS-IS? | Identidade + `COD_FEDERACAO` | DDL; `createColaborador` | `AS-IS` |
| `COD_CARGO` é obrigatório? | **AS-IS:** não (inexistente). **TO-BE DEC-DB-027:** sim. **DH-PA-03:** não no PA | §3.6, DEC-DB-027, DH-PA-03 | `GAP` |
| Fonte da obrigatoriedade TO-BE | DEC-DB-027 itens 3–4 | `05-decisions-and-risks.md` | `DECISÃO` |
| Código exige CARGO? | **Não** | grep backend | `IMPLEMENTAÇÃO` |
| API exige CARGO? | **Não** | `api.md` | `ESPECIFICAÇÃO` |
| Frontend exige CARGO? | **Não** | grep frontend | `IMPLEMENTAÇÃO` |
| Banco exige CARGO? | **Não** (coluna ausente) | DDL | `AS-IS` |
| Fluxo aprovado para preencher CARGO posteriormente? | **Não** | DH-PA-03 delega; sem spec | `GAP` |
| Decisão de que todo COLABORADOR precisa CARGO imediatamente? | **DEC-DB-027 sim**; **DH-PA-03 não** no PA | Confronto §6 | `Conflito normativo` |

---

## 12. CARGO e PAPEL — verificação explícita

| Relação | Existe no projeto? | Evidência |
|---------|-------------------|-----------|
| **CARGO → PAPEL** (automática) | **Não** | DEC-ORG-002, DEC-DB-027 item 5, BR-045, `organizational-authorization-formalization-etapa6.md` §12 |
| **CARGO → ADMIN_*** | **Não** | DEC-ORG-002 item 5–6 |
| **CARGO → autorização** | **Não** — explicitamente negada | DEC-DB-027: "cargo não concede autorização" |
| **PAPEL → CARGO** | **Não** | DH-PA-03, DEC-ORG-002 |
| Gestor/líder AS-IS (`COD_GESTOR`, `COD_LIDER`) | FK para **colaborador**, não catálogo CARGO | DEC-DB-015; `ColaboradorEntity` |

**DH-PA-03 preservada:** CARGO não determina PAPEL.

---

## 13. Conclusão

### Resposta final

| Pergunta | Resposta |
|----------|----------|
| **DH-PA-03 e DEC-DB-027 podem coexistir?** | **Não** como regras literais no mesmo INSERT do Primeiro Acesso. **Sim** temporariamente no projeto porque DEC-DB-027 não está implementada. |
| **Classificação** | **C — Existe conflito normativo** |
| **O que precisa ser reconciliado?** | Itens R1–R6 (§8.2), centrados em **escopo**, **nullable/NOT NULL** e **momento** de `COD_CARGO` |
| **O que pode avançar sem reconciliação?** | PA núcleo (D1–D7): auth temporária, domínio→Singular, wizard vínculo, criação COLABORADOR **sem** DDL de CARGO |
| **O que bloqueia?** | Materialização de DEC-DB-027 (`CREATE TABLE CARGO`, `ALTER COLABORADOR ADD COD_CARGO NOT NULL`) sem decisão sobre R1–R4 |

### Estado dos bloqueios

```text
DH-PA-01 ✓  (decisão encerrada; implementação pendente)
DH-PA-02 ✓  (decisão encerrada; implementação pendente)
DH-PA-03 ✓  (decisão encerrada)

DEC-DB-027 → reconciliação normativa PENDENTE (conflito C demonstrado)
             implementação física BLOQUEADA até reconciliação
```

### Confirmações desta etapa

- DH-PA-01, DH-PA-02, DH-PA-03 **intactas**
- DEC-DB-027 **intacta** (texto decisório histórico preservado)
- Nenhuma implementação realizada na análise v1.0
- Nenhum código, banco ou DDL alterado na análise v1.0

---

## 14. Atualização pós-decisão — DH-CARGO-01 (2026-08-17)

> **Nota:** Esta seção **não** reescreve a análise histórica (§1–13). Registra o encerramento da reconciliação após decisão humana posterior.

### Decisão humana registrada

**DH-CARGO-01** (`docs/governance/03-open-decisions.md`):

> CARGO é domínio do sistema com persistência própria, porém **não é requisito** para cadastro/criação do COLABORADOR. O COLABORADOR pode existir sem CARGO. Atribuição posterior em fluxo **não definido**.

**Escopo:** geral — **qualquer** fluxo de criação/cadastro (não apenas Primeiro Acesso).

### Tratamento do conflito histórico

| Aspecto | Análise v1.0 (histórica) | Estado atual de governança |
|---------|--------------------------|---------------------------|
| Classificação | **C — Existe conflito normativo** | **Encerrado** por DH-CARGO-01 |
| Coexistência literal DH-PA-03 × DEC-DB-027 | **Não** no INSERT do PA | Incompatibilidade **removida** em nível de negócio |
| RECONCILIAÇÃO-DEC-DB-027 | Pendente | **Encerrada** |
| GAP-028-06 | Pendente | **Encerrado** em governança |
| Implementação bloqueada por reconciliação | Sim | **Não** por conflito normativo; implementação física **delegada** |

### Conclusão atual

1. O conflito foi **identificado** nesta análise (§6 — classificação C).
2. Houve **decisão humana posterior** (**DH-CARGO-01**).
3. A decisão **remove a incompatibilidade normativa** entre obrigatoriedade na criação (DEC-DB-027 itens superseded) e COLABORADOR sem CARGO (DH-PA-03, DH-CARGO-01).
4. **CARGO não é requisito** para criação de COLABORADOR.
5. **DH-PA-03** permanece válida (subsumida/alinhada por DH-CARGO-01 no eixo PA).
6. **DEC-DB-027** permanece **parcialmente válida** (catálogo, CARGO≠PAPEL, fora do vínculo).
7. Trechos conflitantes de DEC-DB-027 foram **superseded** — ver `database/model/05-decisions-and-risks.md` § Supersession parcial.
8. **Reconciliação normativa encerrada.**

### Estado final

```text
DH-PA-01 ✓
DH-PA-02 ✓
DH-PA-03 ✓
DH-CARGO-01 ✓  (escopo geral — criação sem CARGO)

DEC-DB-027 → reconciliação normativa ENCERRADA
             supersession parcial registrada
             implementação física DELEGADA (sem obrigatoriedade na criação)
```

---

| Versão | 1.1 |
|--------|-----|
| Status | ANÁLISE CONCLUÍDA — reconciliação **ENCERRADA** por DH-CARGO-01 (2026-08-17) |

---

## Referências

| Fonte | Uso |
|-------|-----|
| `docs/governance/03-open-decisions.md` | DH-PA-01/02/03, DEC-ORG-002, DEC-DB-027 (ref.) |
| `database/model/05-decisions-and-risks.md` | DEC-DB-027 integral, DEC-DB-028, GAP-028-06 |
| `docs/domain/09-business-rules.md` | BR-011, BR-045 |
| `specs/features/primeiro-acesso/specification.md` | §11 governança PA |
| `specs/features/colaborador/api.md` | Contrato sem cargo |
| `construction/review/primeiro-acesso-dh-pa-03-analysis.md` | Evidência pré-decisão |
| `construction/review/primeiro-acesso-blocking-decisions-package.md` | Pacote bloqueante |
| `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md` | Modelo TO-BE CARGO |
| `database/ddl/003-create-tables.sql` | DDL AS-IS `COLABORADOR` |
| `backend/.../ColaboradorService.java` | `locateOrCreate` |
| `backend/.../ColaboradorApplicationService.java` | CRUD admin |
| `backend/.../ColaboradorEntity.java` | JPA AS-IS |
| `backend/.../AuthenticationService.java` | `finalizeLogin` |
