# DH-PA-03 — Análise para Decisão Humana
## Política de CARGO na criação do COLABORADOR no Primeiro Acesso

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/primeiro-acesso-dh-pa-03-analysis.md` |
| Feature | FT-PRIMEIRO-ACESSO |
| Data | 2026-08-15 |
| Tipo | **Análise para decisão humana** |
| Categoria documental | Evidence |
| Status | **EVIDÊNCIA ANALÍTICA** — DH-PA-03 **APROVADA** (2026-08-17); registro definitivo em `docs/governance/03-open-decisions.md` |
| IDs relacionados | DH-PA-03, DEC-DB-027, DEC-ORG-002, DH-03, DH-PA-01, DH-PA-02 |

**Classificação usada:** `FATO` · `INFERÊNCIA` · `RECOMENDAÇÃO TÉCNICA` · `DECISÃO HUMANA PENDENTE`

**Restrições cumpridas nesta etapa (análise original):**

- Nenhum código, DDL, migration, JPA, API, frontend, teste ou seed foi alterado na elaboração desta evidência.
- DH-PA-03 foi **aprovada e formalizada** em `docs/governance/03-open-decisions.md` (2026-08-17).
- DEC-DB-027 **não foi alterada** (texto histórico preservado); reconciliação **encerrada** por **DH-CARGO-01** (2026-08-17).

**Atualização pós-decisão (2026-08-17):** **DH-CARGO-01** aprovada — CARGO não obrigatório na criação de qualquer COLABORADOR; reconciliação DEC-DB-027 **encerrada**. Ver governança e `primeiro-acesso-dh-pa-03-db-reconciliation.md` §14.

**Artefato equivalente anterior:** não existia `primeiro-acesso-dh-pa-03-analysis.md`. Evidência parcial consolidada em `construction/review/primeiro-acesso-blocking-decisions-package.md` §7 (DH-PA-03) e `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md` v2.0. Este documento **aprofunda** a questão DH-PA-03 sem substituir DEC-DB-027.

---

## 1. Resumo executivo

**FATO.** DEC-DB-027 exige catálogo `CARGO` e `COLABORADOR.COD_CARGO NOT NULL` na criação, mas o repositório AS-IS não possui tabela `CARGO`, coluna `COD_CARGO`, entidade JPA, API ou UI de cargo. **FATO.** O fluxo normativo de Primeiro Acesso (DH-03, DH-PA-01, DH-PA-02) define domínio → Singular → Área → Equipe opcional → criação do COLABORADOR, **sem passo de CARGO**. **INFERÊNCIA.** Existe **lacuna de negócio**: a obrigatoriedade de cargo na criação está decidida (DEC-DB-027), mas **a origem do valor de CARGO no onboarding self-service não está definida**. **DECISÃO HUMANA PENDENTE.** DH-PA-03 deve escolher **quem determina o CARGO** e **como** (seleção pelo usuário vs atribuição sistêmica), incluindo governança sobre autodeclaração.

---

## 2. Premissas já aprovadas

Somente as relevantes para DH-PA-03. **Não reabrir.**

| Decisão | Conteúdo normativo relevante |
|---------|------------------------------|
| **DH-02** | 1 vínculo organizacional por COLABORADOR (FKs escalares) |
| **DH-03** | COLABORADOR só persistido após vínculo mínimo completo (Alternativa A) |
| **DH-04** | Federação + Singular + Área obrigatórios; Equipe opcional |
| **DEC-ORG-003** | Domínio do e-mail determina a Singular |
| **DEC-DB-028** | Modelo de vínculo único; GAPs de implementação registrados |
| **DEC-DB-027** | Catálogo `CARGO` + `COLABORADOR.COD_CARGO NOT NULL` na criação; 1:1 cargo/colaborador; cargo **não** no vínculo; CARGO ≠ PAPEL |
| **DEC-ORG-002** | CARGO = função organizacional independente; exemplos institucionais (Presidente, Gestor de TI, Analista…) |
| **DH-PA-01** | Credencial temporária de Primeiro Acesso; COLABORADOR criado após vínculo completo; promoção ao operacional |
| **DH-PA-02** | Domínio → Singular 1:1; domínio sem Singular bloqueia PA automaticamente |

**Fluxo normativo TO-BE (com lacuna de CARGO):**

```text
Zimbra autentica
  → Portal verifica COLABORADOR
  → não existe → Primeiro Acesso (credencial temporária — DH-PA-01)
  → domínio → Singular (DH-PA-02)
  → Área (+ Equipe opcional — DH-04)
  → ??? CARGO (DH-PA-03 — PENDENTE)
  → criação do COLABORADOR (DH-03 + DEC-DB-027)
  → estado operacional
```

---

## 3. Evidências encontradas

### 3.1 Governança

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| `docs/governance/03-open-decisions.md` — DEC-ORG-002 | CARGO é entidade de domínio; função organizacional; **não concede autorização**; persistência TO-BE em DEC-DB-027 | **FATO** |
| `docs/governance/03-open-decisions.md` — DEC-DB-027 (ref.) | Catálogo + `COD_CARGO NOT NULL`; proíbe nullable temporário; encerra PD-CARGO-01/02/03 | **FATO** |
| `docs/governance/03-open-decisions.md` — DH-03 | COLABORADOR após vínculo completo; fluxo **não menciona CARGO** | **FATO** |
| `docs/governance/03-open-decisions.md` — DH-PA-03 | **Pendente** — política de CARGO na criação self-service | **FATO** |
| `database/model/05-decisions-and-risks.md` — DEC-DB-027 | Texto integral TO-BE; seed sugerido (Presidente, Gestor TI, Analista…); **sem implementação** | **FATO** |
| `database/model/05-decisions-and-risks.md` — DEC-DB-016 | Rejeitou entidade CARGO (2026-07-10) — **superseded** por DEC-ORG-002 / DEC-DB-027 | **FATO** |
| `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md` v2.0 | AS-IS GAP total; Hipótese A (cargo em COLABORADOR); histórico de cargo fora de escopo | **FATO** |
| `construction/review/primeiro-acesso-blocking-decisions-package.md` §7 | Alternativas C1/C2/C3; C3 rejeitada; sub-questões SQ-C03-* | **FATO** (evidência analítica) |

### 3.2 Domínio

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| `docs/domain/09-business-rules.md` — BR-011 | Primeiro acesso: domínio → Singular; Área; Equipe opcional; **sem CARGO** | **FATO** |
| `docs/domain/09-business-rules.md` — BR-043, BR-044 | Domínio/Singular; alinhadas a DH-PA-02 | **FATO** |
| `docs/domain/02-business-glossary.md` | **Sem** termo canônico "cargo" / "CARGO" | **FATO** — lacuna terminológica |
| `docs/domain/04-domain-concepts.md` | Colaborador, vínculo, Papel — **sem** entidade CARGO | **FATO** |
| `construction/review/organizational-authorization-formalization-etapa6.md` §12 | CARGO ≠ PAPEL ≠ ADMIN_*; Gestor TO-BE é **nome de cargo**, não FK | **FATO** |
| `construction/review/organizational-authorization-reconciliation-etapa5.md` §5.3 | CAR-02: integração RH futura **OUT_OF_SCOPE**; cargo fora do MVP AS-IS | **FATO** |

**INFERÊNCIA.** CARGO representa **função organizacional institucional** (DEC-ORG-002), ortogonal a vínculo e autorização — **não** é mero detalhe técnico de banco, embora a persistência ainda não exista.

### 3.3 Banco

Investigação baseada em DDL baseline, migrations, modelo físico e seeds versionados. **Consulta Oracle runtime não realizada** — credenciais ausentes em `.env.example` / ambiente local não provisionado para leitura.

| Pergunta | Resposta | Classificação |
|----------|----------|---------------|
| 1. `COD_CARGO` é realmente NOT NULL? | **AS-IS:** coluna **inexistente**. **TO-BE (DEC-DB-027):** **NOT NULL** aprovado, **não implementado** | **FATO** |
| 2. Existe tabela/catálogo CARGO? | **Não** em `database/ddl/003-create-tables.sql` | **FATO** |
| 3. Existem registros de CARGO? | **Não** — sem tabela; `database/ddl/008-initial-data.sql` **sem** seed de cargo | **FATO** |
| 4. Existe FK COLABORADOR → CARGO? | **Não** AS-IS; prevista TO-BE em DEC-DB-027 | **FATO** |
| 5. Existe CARGO padrão? | **Não** | **FATO** |
| 6. Valor especial/default? | **Não** | **FATO** |
| 7. Regra associando CARGO a Singular/Área/usuário? | **Não** encontrada | **FATO** |

**Evidências adicionais:**

| Artefato | Detalhe | Classificação |
|----------|---------|---------------|
| `database/ddl/003-create-tables.sql` — `COLABORADOR` | Colunas: identidade + FKs org + gestor; **sem** `COD_CARGO` | **FATO** |
| `database/migrations/V007__colaborador_ssot_alignment.sql` | Remove coluna legada `DES_CARGO` (texto livre) | **FATO** |
| `database/model/03-physical-model.md` — §COLABORADOR | 23 entidades AS-IS; **sem** CARGO; TO-BE em DEC-DB-027 referenciado | **FATO** |
| `database/model/04-entity-catalog.md` | **Sem** entrada CARGO | **FATO** |
| `database/model/02-logical-model.md` | 23 entidades — **sem** CARGO (citado em reconciliação PD-CARGO) | **FATO** |
| `database/ddl/003-create-tables.sql` — `ONBOARDING_SOLICITACAO` | Exige `COD_COLABORADOR NOT NULL` — fluxo legado de solicitação **pós**-colaborador; **sem** campo cargo | **FATO** |

**INFERÊNCIA.** Ao implementar DEC-DB-027, qualquer INSERT de COLABORADOR (incluindo onboarding) exigirá `COD_CARGO` válido — impossível hoje sem migration + seed.

### 3.4 Backend

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| `ColaboradorEntity.java` | **Sem** campo `cargoId` / `COD_CARGO` | **FATO** |
| `ColaboradorService.createColaborador` (login) | Persiste identidade + `COD_FEDERACAO` default; FKs org NULL; **sem cargo** | **FATO** |
| `ColaboradorApplicationService.create` (CRUD admin) | Exige admin org; preenche vínculos; **sem cargo** | **FATO** |
| `CreateColaboradorRequest` / `UpdateColaboradorRequest` | **Sem** `cargoId` | **FATO** |
| `specs/features/colaborador/api.md` | Contrato **sem** cargo | **FATO** |
| Grep `backend/**` por `cargo`, `CARGO`, `Cargo` | **Zero** referências | **FATO** |
| `AuthenticationService.finalizeLogin` | `locateOrCreate` → cria COLABORADOR antes da sessão (AS-IS; **CONFLICT** com DH-03) | **FATO** |

**INFERÊNCIA.** Backend **consegue** criar COLABORADOR sem CARGO **apenas porque** DEC-DB-027 não foi implementada — comportamento AS-IS **não** é normativo TO-BE.

### 3.5 Specs

| Artefato | Menção a CARGO | Classificação |
|----------|----------------|---------------|
| `specs/features/primeiro-acesso/*` | **Nenhuma** ocorrência de cargo/CARGO/COD_CARGO | **FATO** — **LACUNA** |
| `specs/features/primeiro-acesso/specification.md` §11 | Modelo TO-BE vínculo documentado; **CARGO omitido** | **FATO** — **LACUNA** |
| `specs/features/primeiro-acesso/api.md` | Endpoints de contexto/sessão; **sem** complete onboarding com cargo | **FATO** — **LACUNA** |
| `specs/features/colaborador/specification.md` | RN-001..009 **sem** cargo; onboarding fora de escopo | **FATO** |
| `specs/features/authentication/*` | **Sem** cargo | **FATO** |
| Grep `specs/**` | **Zero** matches cargo/CARGO | **FATO** |

**CONFLICT documental (não bloqueante para deliberação):** DEC-DB-027 e FT-COLABORADOR divergem — decisão de persistência aprovada vs spec/API AS-IS sem campo.

### 3.6 Legado

| Evidência | Conteúdo | Classificação |
|-----------|----------|---------------|
| `docs/discovery/03-current-data-model.md` — Colaborador WP | Campo `cargo` entre atributos principais; origem ACF/`group_colaborador_cargo.json` | **FATO** — evidência histórica AS-IS |
| Legado | **Não** evidencia quem preenchia `cargo` (usuário vs admin vs RH) | **FATO** — lacuna histórica |
| Portal novo V007 | Remove `DES_CARGO`; abandona atributo texto | **FATO** |
| CAR-02 (etapa 5) | Integração RH para cargo = **OUT_OF_SCOPE** | **FATO** |

**INFERÊNCIA.** Legado tratava cargo como **atributo do colaborador** (texto), não como catálogo normalizado — **não** normativo para TO-BE DEC-DB-027.

---

## 4. Estado atual do CARGO

### 4.1 Representação

| Camada | Estado AS-IS | Estado TO-BE (aprovado, não implementado) |
|--------|--------------|-------------------------------------------|
| Domínio | DEC-ORG-002 define conceito; glossário **sem** termo | Mantido |
| Oracle | Sem `CARGO`; sem `COD_CARGO`; `DES_CARGO` removido | Tabela catálogo + FK NN |
| JPA | Ausente | `CargoEntity` + relação obrigatória |
| API | Ausente | `cargoId` esperado (DEC-DB-027 consequências) |
| Frontend | Ausente | Seleção ou default implícito (DH-PA-03) |
| Primeiro Acesso | **Não tratado** | Bloqueado por DH-PA-03 |

### 4.2 Utilização

**FATO.** Nenhum fluxo operacional do Portal novo utiliza CARGO. **FATO.** Criação automática no login (`locateOrCreate`) e CRUD administrativo persistem colaborador **sem** cargo. **FATO.** DEC-DB-027 declara cargo obrigatório na criação TO-BE e proíbe `nullable=true` como atalho.

### 4.3 Natureza do CARGO (negócio vs técnico)

| Hipótese | Evidência | Veredito |
|----------|-----------|----------|
| Informação do colaborador (autodeclaração) | **Nenhuma** BR/spec autoriza | **Sem evidência** |
| Informação da organização | DEC-ORG-002 — nomes institucionais | **FATO** (conceito) |
| Classificação administrativa | Distinto de PAPEL/ADMIN_* | **FATO** |
| Derivada do vínculo (Área/Equipe) | **Nenhuma** regra | **Sem evidência** |
| Proveniente de outro sistema (Zimbra/RH) | CAR-02 OUT_OF_SCOPE; sem integração | **Sem evidência** |
| Apenas requisito técnico de banco | DEC-DB-027 ancora em DEC-ORG-002 (domínio) | **INFERÊNCIA:** é **requisito de negócio** com persistência pendente |

> **Registro explícito:** Não há evidência suficiente para delegar a autodeclaração de CARGO ao usuário.

---

## 5. Conflito/Lacuna

### 5.1 Tensão central

**FATO.** DEC-DB-027: `COLABORADOR.COD_CARGO NOT NULL` na criação. **FATO.** DH-03: COLABORADOR criado ao final do onboarding após vínculo organizacional. **FATO.** Fluxos aprovados de PA (DH-PA-01, DH-PA-02, BR-011, BR-043) **não definem** origem de CARGO.

**Classificação:** **LACUNA DE NEGÓCIO** (DH-PA-03) — não é conflito que invalide DEC-DB-027 ou DH-03; é **gap de política operacional** entre decisões já aprovadas.

### 5.2 Relação DEC-DB-027 × DH-03

| Aspecto | DEC-DB-027 | DH-03 | Relação |
|---------|------------|-------|---------|
| Momento | Criação do COLABORADOR | Após vínculo completo | **Compatível** |
| Campo cargo | Obrigatório | Silencioso | **Lacuna** — falta **fonte do valor** |
| Implementação | Pendente | Pendente | **GAP de implementação** derivado |

**FATO.** Não existe forma **já definida** de obter CARGO no Primeiro Acesso. **INFERÊNCIA.** Implementar DH-03 sem DH-PA-03 forçaria decisão técnica implícita (ex.: default hardcoded) — **inaceitável** para governança.

### 5.3 Alternativa C4 — CARGO fora do Primeiro Acesso

| Variante | Viabilidade | Classificação |
|----------|-------------|---------------|
| Criar COLABORADOR sem cargo; completar depois | **Incompatível** com DEC-DB-027 | **FATO** |
| Estado intermediário persistido sem COLABORADOR | Compatível com DH-PA-01 (credencial temporária) | **FATO** |
| Adiar criação do COLABORADOR até cargo conhecido | Compatível conceitualmente; **não** especificado | **INFERÊNCIA** — exigiria fluxo adicional não aprovado |

**Veredito:** adiar **atribuição** de cargo para pós-PA **rejeitado** por DEC-DB-027; adiar **criação** do COLABORADOR **contradiz** DH-03/DH-PA-01.

### 5.4 Outros GAPs/conflitos registrados

| ID | Descrição | Tipo |
|----|-----------|------|
| GAP-CARGO-IMPL | DEC-DB-027 aprovada sem DDL/JPA/API | Implementação pendente |
| LACUNA-PA-CARGO | Fluxo PA sem passo CARGO | Negócio — **DH-PA-03** |
| LACUNA-GLOSSARIO | Termo CARGO ausente do glossário | Documentação |
| CONFLICT-SPEC-027 | Specs colaborador/PA vs DEC-DB-027 | Documental |
| CONFLICT-AS-IS-027 | Código cria colaborador sem cargo | Implementação AS-IS |

---

## 6. Alternativas identificadas

Somente alternativas **sustentadas por evidências**. C3 administrativa posterior **excluída** (DEC-DB-027).

### C1 — Usuário seleciona CARGO no Primeiro Acesso

**Descrição:** Step adicional (ou combinado) no wizard; catálogo `CARGO` seed; usuário escolhe `cargoId` antes do INSERT final.

**Evidências:** DEC-ORG-002 (cargo como função organizacional nomeada); DEC-DB-027 (catálogo + FK); `primeiro-acesso-blocking-decisions-package.md` §7.4 C1; consequências DEC-DB-027 citam "formulário exige cargo".

### C2 — Sistema atribui CARGO padrão

**Descrição:** Backend atribui automaticamente um registro seed (ex.: `NOM_CARGO = "Colaborador"`) no complete onboarding; sem step de UI; edição posterior via FT-COLABORADOR admin.

**Evidências:** `primeiro-acesso-blocking-decisions-package.md` §7.4 C2; DEC-DB-027 menciona seed inicial; proibição de nullable **não** proíbe default sistêmico.

### C3 — CARGO de fonte externa (Zimbra / RH / outro sistema)

**Descrição:** Obter cargo de integração corporativa ou atributo IdP.

**Evidências:** CAR-02 marca integração RH **OUT_OF_SCOPE**; **nenhum** atributo Zimbra mapeado para cargo no backend; **nenhum** endpoint de integração.

**Status:** **Sem sustentação suficiente** para apresentar como alternativa viável no MVP — registrada como **hipótese não evidenciada**, não como opção de checklist.

### C4 — Atribuição administrativa posterior (rejeitada)

**Status:** **REJEITADA** — incompatível com DEC-DB-027 (`COD_CARGO NOT NULL` na criação). Evidência: `primeiro-acesso-blocking-decisions-package.md` §7.4 C3.

---

## 7. Análise das alternativas

### 7.1 C1 — Seleção pelo usuário

| Dimensão | Análise |
|----------|---------|
| **Vantagens** | Alinha semanticamente a DEC-ORG-002; cargo nomeado desde o primeiro cadastro; evita valor genérico |
| **Desvantagens** | Wizard mais longo; exige catálogo seed e governança; risco de autodeclaração incorreta |
| **Primeiro Acesso** | Novo step ou campo; depende de API listar cargos |
| **Criação COLABORADOR** | INSERT com `COD_CARGO` escolhido |
| **Banco** | `CARGO` + seed **amplo** (mínimo: lista institucional) |
| **Backend** | Endpoint catálogo + validação cargo ativo |
| **Frontend** | UI seleção; i18n labels |
| **Riscos negócio** | Autodeclaração sem evidência de legitimidade; títulos incorretos |
| **Riscos técnicos** | Catálogo vazio bloqueia onboarding |
| **Dependências** | DDL DEC-DB-027; seed DML; DH-PA-01 (sessão pré-COLABORADOR) |

**FATO.** Não há catálogo disponível hoje. **INFERÊNCIA.** Faz sentido de negócio **se** a organização aceitar autodeclaração ou lista fechada curta — **DECISÃO HUMANA PENDENTE**.

### 7.2 C2 — Cargo default sistêmico

| Dimensão | Análise |
|----------|---------|
| **Vantagens** | Onboarding mais curto; satisfaz NOT NULL; implementação previsível |
| **Desvantagens** | Cargo genérico pode não refletir função real; depende de processo admin posterior |
| **Primeiro Acesso** | Sem step extra |
| **Criação COLABORADOR** | INSERT com `COD_CARGO` = constante/lookup |
| **Banco** | `CARGO` + **1** registro seed mínimo |
| **Backend** | Resolução interna do default |
| **Frontend** | Baixo impacto PA |
| **Riscos negócio** | Significado institucional do default; dados imprecisos até correção admin |
| **Riscos técnicos** | Baixo |
| **Dependências** | DDL DEC-DB-027; **decisão do `NOM_CARGO` canônico** |

**FATO.** Não existe CARGO padrão definido nem decisão aprovando C2. **INFERÊNCIA.** C2 resolve integridade, não semântica — exige validação de negócio do nome default.

### 7.3 Comparativo C1 vs C2

| Critério | C1 | C2 |
|----------|----|----|
| DEC-DB-027 | ✅ | ✅ |
| DEC-ORG-002 (semântica) | Mais forte | Mais fraca |
| Evidência autodeclaração | Ausente | N/A |
| UX PA | Mais steps | Menor fricção |
| Catálogo seed MVP | Maior | Mínimo (1 registro) |
| FT-COLABORADOR pós-PA | Edição cargo | Edição cargo |

---

## 8. Segurança e governança

| Tema | Análise | Classificação |
|------|---------|---------------|
| Autodeclaração de CARGO | Sem BR/spec que autorize; exemplos DEC-ORG-002 são títulos institucionais | **FATO** + registro explícito acima |
| CARGO × autorização | CARGO **não** concede permissão (DEC-ORG-002, DEC-DB-027) | **FATO** |
| Risco C1 | Usuário declara cargo elevado (ex.: "Diretor") sem validação | **INFERÊNCIA** — risco de governança de dados |
| Risco C2 | Dados de função imprecisos; correção depende de admin | **INFERÊNCIA** |
| Auditoria | DEC-DB-027 prevê auditoria em catálogo; PA ainda sem eventos de cargo | **FATO** |
| Separação PAPEL | Enforcement de `PAPEL_ATRIBUICAO` mínimo é **futuro** (DH-07) — **não** bloqueia PA núcleo | **FATO** (blocking package SQ-C03-04) |

---

## 9. Impactos

### 9.1 Banco

- **Ambas C1/C2:** `CREATE TABLE CARGO`, sequence, FK, grants (DEC-DB-027).
- **C1:** seed amplo; possível regra cargo inativo (PD-CARGO-01-R — pendente).
- **C2:** seed mínimo (1 cargo default).

### 9.2 Backend

- `CargoEntity`, validação na criação onboarding e CRUD admin.
- **C1:** API listagem cargos; validação `cargoId` no complete onboarding.
- **C2:** lookup interno do default.

### 9.3 Frontend

- **C1:** step seleção no wizard PA.
- **C2:** nenhum step; possível exibição read-only opcional.

### 9.4 API

- Contrato complete onboarding (LA-03 derivada) deve incluir ou omitir `cargoId` conforme DH-PA-03.
- FT-COLABORADOR: evolução para `cargoId` (DEC-DB-027 consequência — delegável).

### 9.5 Primeiro Acesso e COLABORADOR

- INSERT final do wizard (DH-03) **bloqueado** até DH-PA-03 + implementação DEC-DB-027.
- Coerência DH-PA-01: credencial temporária sobrevive ao wizard **incluindo** resolução de cargo (step ou implícito).

### 9.6 Relação DH-PA-01 / DH-PA-02

**FATO.** DH-PA-01 e DH-PA-02 **não** alterados. **INFERÊNCIA.** CARGO entra **após** Área/Equipe e **antes/immediatemente na** criação do COLABORADOR — único ponto lógico no fluxo aprovado.

---

## 10. Menor conjunto de decisões humanas

Questões que **realmente** exigem autoridade humana (nível regra de negócio):

| # | Decisão humana pendente |
|---|-------------------------|
| **DH-PA-03.1** | **Quem determina o CARGO** na criação self-service do COLABORADOR no Primeiro Acesso: **(A)** o próprio usuário (seleção) ou **(B)** o sistema (atribuição automática de cargo padrão)? |
| **DH-PA-03.2** | *(Condicional A — C1)* A autodeclaração de CARGO pelo usuário é **aceitável** institutionalmente? Se sim, qual **catálogo mínimo** no MVP (`NOM_CARGO` permitidos)? |
| **DH-PA-03.3** | *(Condicional B — C2)* Qual o **`NOM_CARGO` canônico** do cargo padrão sistêmico (ex.: "Colaborador", "Analista") e esse valor é **aceitável** como função inicial de todo novo ingresso? |
| **DH-PA-03.4** | *(Opcional derivada)* O cargo atribuído no PA (C1 ou C2) pode ser **alterado pelo próprio usuário** antes de concluir o onboarding, ou apenas por **administrador** após o PA? |

**Não transformar em decisão humana:** DDL, FK, JPA, endpoints, DTOs, algoritmo de lookup, layout do step, seed SQL, migration — **delegável à engenharia** após DH-PA-03.

**Redução:** se DH-PA-03.1 = B (C2), DH-PA-03.2 torna-se **N/A**; permanecem **DH-PA-03.3** e opcionalmente **DH-PA-03.4**.

---

## 11. Pontos delegáveis à engenharia

| Tema | Motivo |
|------|--------|
| Estrutura física `CARGO` (DEC-DB-027) | Já aprovada |
| Migration + sequence + grants | Consequência DEC-DB-027 |
| `CargoEntity`, repository, mapper | Padrão projeto |
| Endpoint `GET /cargos` ou subset | Derivado de C1 |
| Campo `cargoId` em DTOs | Contrato técnico |
| Constante/lookup do default (C2) | Implementação pós DH-PA-03.3 |
| Testes integração wizard + cargo | DoD |
| Reconciliação specs FT-PA / FT-COLABORADOR | specification-engineer |
| UK `NOM_CARGO`, cargo inativo | PD-CARGO-01-R — futuro |
| Glossário "Cargo" | Documentação |

---

## 12. Recomendação técnica

> **RECOMENDAÇÃO TÉCNICA DA IA — NÃO É DECISÃO HUMANA.**

| Cenário | Sugestão |
|---------|----------|
| Fechar vertical PA com menor fricção e correção admin posterior | **C2** — cargo default sistêmico + seed mínimo; FT-COLABORADOR para refinamento |
| Priorizar aderência semântica a DEC-ORG-002 desde o ingresso | **C1** — seleção no wizard com catálogo seed fechado **se** DH-PA-03.2 aprovar autodeclaração |

**Em ambos os casos:** implementação de DDL `CARGO` (DEC-DB-027) é **pré-requisito técnico** — a decisão DH-PA-03 é sobre **política de origem do valor**, não sobre nullable.

**Nenhuma alternativa foi selecionada neste documento.**

---

## 13. Perguntas para o decisor

1. Na criação do COLABORADOR no Primeiro Acesso, o CARGO deve ser **escolhido pelo usuário (C1)** ou **atribuído automaticamente pelo sistema (C2)**?
2. Se **C1**: o colaborador pode **autodeclarar** seu cargo? Quais cargos compõem o **catálogo mínimo** aceitável no MVP?
3. Se **C2**: qual é o **nome institucional** do cargo padrão (`NOM_CARGO`) para todos os novos ingressos?
4. O cargo definido no Primeiro Acesso pode ser **corrigido pelo usuário** durante o onboarding, ou **somente por administrador** depois?
5. A opção de **adiar cargo** (criar colaborador sem cargo) permanece **rejeitada** conforme DEC-DB-027? *(Confirmación — esperada: sim.)*

---

## 14. Fora do escopo

- Implementação de código, DDL, migration, seeds, JPA, API, frontend.
- Alteração de DEC-DB-027, DH-PA-01, DH-PA-02, DH-03, `03-open-decisions.md`.
- Registro de DH-PA-03 como aprovada.
- Integração RH / Zimbra para cargo (sem evidência MVP).
- Histórico de cargos, `ATRIBUICAO_CARGO`, cargo no vínculo (PD-CARGO encerrados).
- Enforcement de `PAPEL_ATRIBUICAO` mínimo (DH-07).
- Persistência física de Contexto Ativo (LA-06 / INC-PA-004).
- Resolução de conflitos spec legado (INC-PA-001..006).

---

## Referências

| Fonte | Uso |
|-------|-----|
| `construction/review/primeiro-acesso-blocking-decisions-package.md` | Pacote DH-PA-01/02/03; alternativas C1/C2 |
| `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md` v2.0 | AS-IS GAP; modelo TO-BE |
| `docs/governance/03-open-decisions.md` | DEC-ORG-002, DEC-DB-027, DH-03/04, DH-PA-01/02, DH-PA-03 pendente |
| `database/model/05-decisions-and-risks.md` | DEC-DB-027 integral; DEC-DB-016; GAP-028-* |
| `database/ddl/003-create-tables.sql` | Schema AS-IS `COLABORADOR` |
| `database/migrations/V007__colaborador_ssot_alignment.sql` | Remoção `DES_CARGO` |
| `docs/domain/09-business-rules.md` | BR-011, BR-043, BR-044 |
| `specs/features/primeiro-acesso/specification.md` | Lacuna CARGO no PA |
| `specs/features/colaborador/api.md` | Contrato sem cargo |
| `backend/.../ColaboradorService.java` | AS-IS criação sem cargo |
| `backend/.../ColaboradorApplicationService.java` | CRUD admin sem cargo |
| `docs/discovery/03-current-data-model.md` | Legado campo `cargo` |
| `construction/review/organizational-authorization-reconciliation-etapa5.md` | CAR-02 RH OUT_OF_SCOPE |

---

| Versão | 1.1 |
|--------|-----|
| Status | EVIDÊNCIA ANALÍTICA — DH-PA-03 **APROVADA**; **DH-CARGO-01** **APROVADA** (2026-08-17); reconciliação DEC-DB-027 **encerrada** |
