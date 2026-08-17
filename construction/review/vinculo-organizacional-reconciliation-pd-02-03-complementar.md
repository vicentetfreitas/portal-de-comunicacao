# Reconciliação complementar PD-02 / PD-03 — Semântica das FKs × N vínculos

| Campo | Valor |
|-------|-------|
| Artefato | vinculo-organizacional-reconciliation-pd-02-03-complementar.md |
| Camada | Construction / Review |
| Versão | **1.0** |
| Data | 2026-08-14 |
| Categoria documental | Evidence |
| Status | **ETAPA 8.x — CONCLUÍDA** (análise complementar; decisão formal pendente) |
| Complementa | [`vinculo-organizacional-reconciliation-pd-02-03.md`](vinculo-organizacional-reconciliation-pd-02-03.md) v1.0 |
| Relaciona | [`organizational-authorization-formalization-etapa6.md`](organizational-authorization-formalization-etapa6.md) |

---

## 1. Objetivo

Reconciliar PD-02 e PD-03 contra o **AS-IS real** do repositório, incorporando a **nova regra de domínio confirmada** (Federação + Singular + Área obrigatórias por colaborador; Equipe opcional), sem implementar alterações.

Foco desta etapa complementar:

- determinar a **semântica** de `COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA` e `COD_EQUIPE` em `COLABORADOR` diante de DEC-FA-003 (N vínculos);
- confrontar a regra confirmada com DDL, JPA, API, frontend, testes e specs;
- reavaliar a recomendação de `VINCULO_ORGANIZACIONAL` da reconciliação v1.0;
- preparar insumos para futura **DEC-DB-028** — **sem aprovar decisão**.

---

## 2. Regra de domínio confirmada (entrada desta etapa)

Confirmada pelo responsável do projeto nesta etapa:

```text
Todo COLABORADOR pertence obrigatoriamente a:
  - uma FEDERAÇÃO
  - uma SINGULAR
  - uma ÁREA

EQUIPE é opcional.
```

Exemplos válidos:

```text
COLABORADOR → Federação Unimed Ceará, Singular Fortaleza, Área Tecnologia, Equipe NULL
COLABORADOR → Federação Unimed Ceará, Singular Fortaleza, Área Tecnologia, Equipe Desenvolvimento
```

**INFERÊNCIA:** ausência de equipe **não** significa ausência de vínculo organizacional.

**Classificação:** regra de domínio **confirmada** para TO-BE; **não** altera DEC-FA-003 nem DEC-DB-027.

---

## 3. Decisões consideradas

| ID | Conteúdo | Status nesta análise |
|----|----------|---------------------|
| **DEC-FA-003** | N vínculos + Contexto Ativo | **FATO** — não reaberta |
| **DEC-FA-002** | Colaborador operacional exige Área | **FATO** — alinhada à nova regra |
| **DEC-ORG-001 / BR-040** | Hierarquia Federação → Singular → Área → Equipe | **FATO** |
| **DEC-ORG-002** | CARGO independente | **FATO** — não reaberto |
| **DEC-DB-027** | 1 cargo/colaborador; `COD_CARGO` NN | **FATO** — não reaberto |
| **DEC-DB-021** | Federação única (Unimed Ceará) | **FATO** — impacta Cenário 6 |
| **DEC-DB-020** | Vínculo cadastral ≠ `PAPEL_ATRIBUICAO` | **FATO** |
| **BR-041** | N vínculos | **FATO** |
| **BR-012 / RN-PA-001** | Contexto: federação + singular + área; equipe opcional | **FATO** — alinhado à nova regra **por vínculo** |
| **Regra confirmada (§2)** | Fed+Sing+Área obrigatórios no colaborador | **Entrada** — conflita parcialmente com AS-IS físico |

---

## 4. AS-IS de COLABORADOR

### 4.1 Inventário DDL Oracle (FATO)

Fonte: `database/ddl/003-create-tables.sql` L158–179; `004-create-constraints.sql` L193–232; `005-create-indexes.sql` L107–126.

| Coluna | Existe | Tipo Oracle | Nullable DDL | FK | Referência | Índice |
|--------|--------|-------------|--------------|-----|------------|--------|
| `COD_COLABORADOR` | Sim | `NUMBER(19)` | NOT NULL | PK | — | PK |
| `COD_FEDERACAO` | Sim | `NUMBER(19)` | **NOT NULL** | Sim | `FEDERACAO` | `IDX_COLABORADOR_FEDERACAO` |
| `COD_SINGULAR` | Sim | `NUMBER(19)` | **NULL** | Sim | `SINGULAR` | `IDX_COLABORADOR_SINGULAR` |
| `COD_AREA` | Sim | `NUMBER(19)` | **NULL** | Sim | `AREA` | `IDX_COLABORADOR_AREA` |
| `COD_EQUIPE` | Sim | `NUMBER(19)` | **NULL** | Sim | `EQUIPE` | `IDX_COLABORADOR_EQUIPE` |
| `COD_GESTOR` | Sim | `NUMBER(19)` | NULL | Sim | `COLABORADOR` (self) | `IDX_COLABORADOR_GESTOR` |
| `COD_CARGO` | **Não** | — | — | — | — | — |

**FATO:** não há CHECK no Oracle exigindo coerência entre `COD_SINGULAR`, `COD_AREA` e `COD_EQUIPE`.

### 4.2 JPA (FATO)

`ColaboradorEntity.java`:

| Coluna | Campo JPA | `nullable` JPA |
|--------|-----------|----------------|
| `COD_FEDERACAO` | `federacaoId` | `false` |
| `COD_SINGULAR` | `singularId` | default (true) |
| `COD_AREA` | `areaId` | default (true) |
| `COD_EQUIPE` | `equipeId` | default (true) |
| `COD_GESTOR` | `gestorId` | default (true) |

### 4.3 API / DTOs (FATO)

| Camada | Campo API | Obrigatório no contrato |
|--------|-----------|-------------------------|
| `CreateColaboradorRequest` | `federationId` | `@NotNull` |
| | `singularId`, `areaId`, `teamId`, `managerId` | opcionais |
| `ColaboradorResponse` | todos os 4 IDs org + `managerId` | expostos (podem ser null) |
| `ColaboradorOrganizationalLinksResponse` | `federationId`, `singularId`, `areaId`, `teamId` | snapshot sessão |
| `specs/features/colaborador/api.md` | idem | `federationId` obrigatório; demais opcionais |

### 4.4 Frontend (FATO)

| Artefato | Comportamento |
|----------|---------------|
| `colaborador.types.ts` | 4 campos org no CRUD |
| `useColaboradorForm.ts` | FKs org opcionais no form |
| `session.store.ts` | **um** `organizationalLinks`; `activeContext` = mesmo objeto |
| `auth/types.ts` | `federationId: number \| null` no tipo de links |

### 4.5 Testes (FATO)

| Teste | Pressuposto |
|-------|-------------|
| `ColaboradorAcceptanceIntegrationTest` | Create com fed+sing+area+team completos |
| `ColaboradorTestBuilder.forFederation()` | Apenas `federacaoId` |
| `auth.service.spec.ts` | `federationId` only; demais null |
| `auth.guard.spec.ts` | Vínculo único completo **ou** só federação |
| `session.store.spec.ts` | Vínculo único; active = links |

**Classificação:** testes refletem **AS-IS** (vínculo único ou federação-only); **não** codificam N vínculos nem obrigatoriedade de singular/área no DDL.

### 4.6 Documentação física AS-IS (FATO — diverge da nova regra)

`database/model/03-physical-model.md` L715–717:

| Perfil documentado | `COD_FEDERACAO` | `COD_SINGULAR` | `COD_AREA` | `COD_EQUIPE` |
|--------------------|-----------------|----------------|------------|--------------|
| Colaborador federativo | NN | null | null | null |
| Colaborador de singular | NN | esperado | esperado | opcional |

**CONFLICT:** documentação AS-IS permite colaborador **sem** singular e área; a nova regra **proíbe** isso no TO-BE.

### 4.7 FT-AUTH `locateOrCreate` (FATO)

`ColaboradorService.createColaborador()` L48–56: preenche **apenas** `federacaoId` (default), identidade e `FLG_ATIVO`. `singularId`, `areaId`, `equipeId` permanecem **null**.

**CONFLICT:** fluxo de login cria colaborador **incompatível** com a nova regra confirmada (sem singular/área).

---

## 5. Hierarquia organizacional

### 5.1 Evidências DDL (FATO)

```text
FEDERACAO (COD_FEDERACAO PK)
    ↑ FK NOT NULL
SINGULAR (COD_SINGULAR PK, COD_FEDERACAO)
    ↑ FK nullable (COD_SINGULAR em AREA)
AREA (COD_AREA PK, COD_SINGULAR nullable, COD_GESTOR)
    ↑ FK NOT NULL
EQUIPE (COD_EQUIPE PK, COD_AREA, COD_LIDER)
```

Constraints: `FK_SINGULAR_FEDERACAO`, `FK_AREA_SINGULAR`, `FK_EQUIPE_AREA` (`004-create-constraints.sql`).

### 5.2 Observações (FATO + RISCO)

| Relação | Evidência | Risco |
|---------|-----------|-------|
| SINGULAR → FEDERACAO | `COD_FEDERACAO NOT NULL` em `SINGULAR` | — |
| AREA → SINGULAR | `COD_SINGULAR` **nullable** em `AREA` | **GAP** — área pode existir sem singular no DDL |
| EQUIPE → AREA | `COD_AREA NOT NULL` | — |
| Uma área, N equipes | Cardinalidade 1:N implícita no modelo | **FATO** |

**INFERÊNCIA:** para áreas operacionais do portal, `AREA.COD_SINGULAR` deveria estar preenchido; o DDL **não garante**.

---

## 6. Semântica das FKs em COLABORADOR (síntese)

Análise das hipóteses diante de DEC-FA-003:

| Hipótese | Evidência AS-IS | Compatível com N vínculos? | Veredito |
|----------|-----------------|---------------------------|----------|
| **1. Vínculo principal** | Colunas únicas; mapeadas 1:1 a `organizationalLinks` | ❌ Só 1 vínculo | **INFERÊNCIA** — representa o vínculo **único** AS-IS, não “principal” documentado |
| **2. Contexto ativo** | `AuthenticationService.organizationalLinksFrom()` copia colunas para sessão/JWT | ⚠️ Parcial — não há persistência de contexto em DDL | **INFERÊNCIA** — comportamento de **snapshot de sessão**, não entidade de contexto |
| **3. Âncora de pertencimento** | `COD_FEDERACAO` sempre preenchido; singular/área opcionais no DDL | ❌ Incompleta para nova regra | **Parcial** — só federação é âncora real no AS-IS |
| **4. Vínculo original/primário** | Sem campo ou flag “primário” no repositório | — | **Não evidenciado** |
| **5. Conflito estrutural com DEC-FA-003** | 4 colunas = 1 conjunto; specs exigem N | ❌ | **FATO — CONFLICT** (INC-PA-001) |

**Conclusão sintética (INFERÊNCIA):** no AS-IS, as quatro colunas funcionam como **único conjunto de vínculo operacional** (mais âncora de federação para identidade/login). **Não** há evidência de semântica de “vínculo principal” ou “âncora completa” quando N > 1. Com DEC-FA-003, as colunas **não podem** representar todos os vínculos — **conflito estrutural**.

---

## 7. Análise de COD_FEDERACAO

### 7.1 Respostas às questões específicas

| # | Pergunta | Resposta | Classificação |
|---|----------|----------|---------------|
| 1 | Por que obrigatório? | Identidade institucional; FK NOT NULL no DDL; RN-001 FT-COLABORADOR; default em `locateOrCreate` | **FATO** |
| 2 | Semântica | **Dupla no AS-IS:** (a) pertencimento institucional à federação; (b) componente do único `organizationalLinks` | **INFERÊNCIA** |

### 7.2 Coexistência com N vínculos

| Aspecto | Análise |
|---------|---------|
| DEC-DB-021 (federação única) | Todos os vínculos ∈ mesma federação | **INFERÊNCIA** |
| Armazenar em cada vínculo? | **Não necessário** se derivável via `AREA → SINGULAR → FEDERACAO` | **RECOMENDAÇÃO** |
| Manter em `COLABORADOR`? | **Sim** como âncora de identidade/login (FT-AUTH) | **RECOMENDAÇÃO** — PD-12 |

---

## 8. Análise de COD_SINGULAR

| # | Pergunta | Resposta | Classificação |
|---|----------|----------|---------------|
| 3 | Por que obrigatório? | **Nova regra confirmada** — não refletida no DDL AS-IS | **CONFLICT** AS-IS × TO-BE |
| 4 | Semântica AS-IS | Parte do conjunto único de vínculo operacional; opcional no Oracle | **FATO** |

**FATO:** `BR-009` exige vínculo a singular e área para colaborador **operacional** — alinhado à nova regra no domínio, **não** no DDL.

**INFERÊNCIA:** com N vínculos, `COD_SINGULAR` em `COLABORADOR` não pode listar todas as singulares — só um valor ou null.

**RECOMENDAÇÃO:** `COD_SINGULAR` pertence ao **vínculo** (derivável via `COD_AREA`), não ao cadastro base quando N > 1.

---

## 9. Análise de COD_AREA

| # | Pergunta | Resposta | Classificação |
|---|----------|----------|---------------|
| 5 | Por que obrigatório? | BR-010, DEC-FA-002, RN-PA-001, nova regra confirmada | **FATO** (domínio) / **GAP** (DDL nullable) |
| 6 | Semântica AS-IS | Unidade organizacional mínima do vínculo único; filtro em listagens | **FATO** |

**FATO:** `ColaboradorRepository.findByFilters` filtra por `areaId` diretamente em `COLABORADOR`.

**INFERÊNCIA:** com N vínculos, `COD_AREA` em `COLABORADOR` representa no máximo **um** dos vínculos (o AS-IS) ou snapshot de contexto — **não** o conjunto completo.

---

## 10. Análise de COD_EQUIPE

| # | Pergunta | Resposta | Classificação |
|---|----------|----------|---------------|
| 7 | Por que opcional? | BR-012, RN-PA-001, nova regra, `03-physical-model.md` L717 | **FATO** |
| 8 | Colaborador em área sem equipe? | `COD_EQUIPE = NULL` com `COD_AREA` preenchida | **FATO** (AS-IS) / **RECOMENDAÇÃO** (TO-BE) |

**FATO:** `resolveOrganizationalLinks` aceita `teamId = null` com `areaId` preenchido.

**INFERÊNCIA:** equipe ausente ≠ sem vínculo; vínculo válido = área (+ equipe opcional).

---

## 11. N vínculos — coexistência com as quatro colunas

### 11.1 Tensão estrutural (FATO)

```text
DEC-FA-003:  1 COLABORADOR → N vínculos
AS-IS:       1 COLABORADOR → 1 × (COD_FEDERACAO, COD_SINGULAR, COD_AREA, COD_EQUIPE)
Nova regra:  cada vínculo = Fed + Sing + Área + Equipe?
```

### 11.2 Modelo conceitual reconciliado (INFERÊNCIA + RECOMENDAÇÃO)

| Dado | Pertence a | Motivo |
|------|------------|--------|
| Identidade (`NOM`, `DES_EMAIL`, `ID_ZIMBRA`, `COD_CARGO`) | `COLABORADOR` | DEC-DB-027; 1:1 |
| `COD_FEDERACAO` (identidade) | `COLABORADOR` | FT-AUTH; PD-12 |
| `COD_GESTOR` | `COLABORADOR` | Reporting; PD-04 |
| Cada vínculo (área + equipe opcional) | **`VINCULO_ORGANIZACIONAL`** (futuro) | N linhas |
| Fed/Sing/Área/Equipe do **contexto ativo** | Sessão/aplicação (não DDL hoje) | REF-DB-CTX-01 |
| `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE` em `COLABORADOR` | **Legado AS-IS** | Redundantes ou snapshot — **DECISÃO PENDENTE** |

### 11.3 Respostas 9–12 (questões específicas)

| # | Pergunta | Resposta | Classificação |
|---|----------|----------|---------------|
| 9 | Como N vínculos coexistem com as 4 colunas? | **Não coexistem plenamente** — colunas suportam 1 vínculo; N exige estrutura adicional | **FATO** |
| 10 | Representam vínculo principal? | **Não documentado**; AS-IS = vínculo único | **INFERÊNCIA** |
| 11 | Representam âncora de pertencimento? | `COD_FEDERACAO` sim (identidade); demais = vínculo único, não âncora completa | **INFERÊNCIA** |
| 12 | Conflito com DEC-FA-003? | **Sim** — estrutural (INC-PA-001) | **FATO** |

### 11.4 Respostas 13–20

| # | Pergunta | Resposta | Classificação |
|---|----------|----------|---------------|
| 13 | O que permanece em `COLABORADOR`? | Identidade, `COD_FEDERACAO`, `COD_CARGO`, `COD_GESTOR`, auditoria | **RECOMENDAÇÃO** |
| 14 | O que vai ao vínculo? | `COD_AREA` (NN), `COD_EQUIPE` (NULL) por linha | **RECOMENDAÇÃO** |
| 15 | Fed/Sing no vínculo ou derivadas? | **Derivadas** via `AREA → SINGULAR → FEDERACAO` | **RECOMENDAÇÃO** |
| 16 | Vínculos em áreas diferentes? | **Sim** — BR-041, DEC-FA-003 | **FATO** |
| 17 | Vínculos em singulares diferentes? | **Não definido** explicitamente; **compatível** com N áreas em singulares distintas | **DECISÃO PENDENTE** |
| 18 | Vínculos em federações diferentes? | **Proibido** no domínio atual (DEC-DB-021 — federação única) | **INFERÊNCIA** |
| 19 | Múltiplas equipes na mesma área? | **Não definido** (OQ-008); tecnicamente suportável com N linhas | **DECISÃO PENDENTE** |
| 20 | Decidido vs pendente? | Ver seção 21 | — |

---

## 12. Cenários analisados

Legenda: **D** = domínio (regra confirmada + decisões aprovadas); **A** = AS-IS implementado.

| Cenário | Descrição | Domínio | AS-IS | Classificação |
|---------|-----------|---------|-------|---------------|
| **1** | Fed A, Sing A, Área TI, sem equipe | **Permitido** | Possível via CRUD se sing+area preenchidos; **não** via `locateOrCreate` | D: OK / A: **GAP** login |
| **2** | Fed A, Sing A, Área TI, Equipe Dev | **Permitido** | Suportado no CRUD | D: OK / A: OK |
| **3** | Dois vínculos, mesma área, equipes diferentes | **Não definido** (OQ-008); compatível com N vínculos | **Incompatível** (1 `COD_EQUIPE`) | **DECISÃO PENDENTE** + **CONFLICT** AS-IS |
| **4** | Vínculos em duas áreas diferentes | **Permitido** (BR-041) | **Incompatível** | D: OK / A: **CONFLICT** |
| **5** | Vínculos em duas singulares diferentes | **Não definido** | **Incompatível** | **DECISÃO PENDENTE** |
| **6** | Vínculos em duas federações | **Proibido** (DEC-DB-021) | **Incompatível** | D: **Proibido** / A: N/A |

---

## 13. VINCULO_ORGANIZACIONAL — reavaliação

A recomendação da reconciliação v1.0 **não é decisão aprovada**. Reavaliação diante da nova regra:

### 13.1 Estrutura candidata

```text
VINCULO_ORGANIZACIONAL (conceitual)
  COD_VINCULO_ORGANIZACIONAL  PK
  COD_COLABORADOR             FK NOT NULL
  COD_AREA                    FK NOT NULL    ← unidade folha base
  COD_EQUIPE                  FK NULL        ← opcional
  FLG_ATIVO, DAT_CADASTRO, DAT_ATUALIZACAO
```

### 13.2 Atributos — análise

| Atributo | Obrigatório? | Armazenar ou derivar? | Justificativa |
|----------|--------------|----------------------|---------------|
| `COD_AREA` | **Sim** | Armazenar | Âncora do vínculo; BR-010; nova regra |
| `COD_EQUIPE` | **Opcional** | Armazenar quando houver | Nova regra; BR-012 |
| `COD_SINGULAR` | Conceitualmente sim por vínculo | **Derivar** `AREA.COD_SINGULAR` | Normalização; evita cadeia inconsistente |
| `COD_FEDERACAO` | Conceitualmente sim por vínculo | **Derivar** `SINGULAR.COD_FEDERACAO` | DEC-DB-021; federação única |

### 13.3 Coerência com nova regra (INFERÊNCIA)

Cada **linha** de vínculo implica, por derivação:

```text
VÍNCULO → AREA (NN) → SINGULAR → FEDERACAO
         → EQUIPE (opcional) → AREA
```

Vínculo sem equipe: `COD_EQUIPE NULL` — **válido**; singular e federação derivadas da área.

### 13.4 Risco na derivação (FATO)

Se `AREA.COD_SINGULAR` for NULL, singular e federação **não são deriváveis**. Domínio e RN-PA-001 pressupõem singular válida — **GAP** no DDL de `AREA`.

### 13.5 Veredito vs v1.0

| Aspecto | v1.0 | Complementar |
|---------|------|--------------|
| `COD_AREA` NN | Recomendado | **Mantido** — reforçado pela nova regra |
| `COD_EQUIPE` opcional | Recomendado | **Mantido** |
| Derivar Sing/Fed | Recomendado | **Mantido** — cada vínculo ainda “tem” sing/fed por derivação |
| Obrigatoriedade no `COLABORADOR` | Não exigia sing/area NN no DDL | **Nova regra** exige no TO-BE — **não** nas colunas legadas se vínculo for separado |

**RECOMENDAÇÃO:** estrutura v1.0 **permanece coerente**; a nova regra reforça obrigatoriedade de **área (e sing/fed derivadas) por vínculo**, não a permanência das FKs legadas em `COLABORADOR`.

---

## 14. Normalização

### 14.1 Cenário: `COLABORADOR` FKs + `VINCULO_ORGANIZACIONAL`

| Padrão | Descrição | Classificação |
|--------|-----------|---------------|
| **Duplicação legítima** | `COD_FEDERACAO` em `COLABORADOR` (identidade) + derivada no vínculo | **INFERÊNCIA** — aceitável se semânticas distintas |
| **Redundância** | `COD_SINGULAR/AREA/EQUIPE` em `COLABORADOR` **e** em N vínculos | **RISCO** — mesma informação em dois lugares |
| **Risco de inconsistência** | Colaborador com `COD_AREA=10` e vínculo em `COD_AREA=20` | **RISCO** — sem regra de sincronização documentada |
| **Contexto principal** | Colunas = snapshot do vínculo ativo | **DECISÃO PENDENTE** — possível, não implementado em DDL |
| **Sincronização** | Atualizar `COLABORADOR` ao trocar contexto vs só sessão | **DECISÃO PENDENTE** |

**RECOMENDAÇÃO:** após `VINCULO_ORGANIZACIONAL`, tratar `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE` em `COLABORADOR` como **legado a deprecar** — não como fonte de N vínculos. Qualquer remoção = decisão futura (migration).

**FATO:** nenhuma remoção é feita nesta etapa.

---

## 15. Integridade

### 15.1 Garantias AS-IS

| Regra | Oracle CHECK | Aplicação |
|-------|--------------|-----------|
| `COD_AREA` ∈ `COD_SINGULAR` | **Não** | `resolveOrganizationalLinks` — parcial |
| `COD_SINGULAR` ∈ `COD_FEDERACAO` | **Não** | Não validado em `resolveOrganizationalLinks` |
| `COD_EQUIPE` ∈ `COD_AREA` | **Não** | **Sim** — `ColaboradorDomainService` L88–91 |
| Cadeia completa fed←sing←area | **Não** | **GAP** |

### 15.2 TO-BE recomendado (sem implementar)

| Regra | Mecanismo sugerido |
|-------|-------------------|
| Equipe ∈ área do vínculo | FK + domínio |
| Área com singular para vínculos operacionais | Domínio + eventual NOT NULL em `AREA.COD_SINGULAR` — **DECISÃO PENDENTE** |
| UK vínculo duplicado | UK `(COD_COLABORADOR, COD_AREA)` quando `COD_EQUIPE IS NULL` — **DECISÃO PENDENTE** |

---

## 16. API

| Contrato | Modelo | vs DEC-FA-003 | vs nova regra |
|----------|--------|---------------|---------------|
| `/auth/me` → `organizationalLinks` objeto único | 1 contexto | **CONFLICT** | Objeto pode ter sing/area null (login) — **CONFLICT** |
| `GET /session/contexts` (proposto) | N contextos | TO-BE | Alinhado |
| CRUD colaborador | 1 conjunto FK | AS-IS | sing/area opcionais na API — **CONFLICT** com nova regra |

**FATO:** API atual = **único contexto**; não expõe `contexts[]`.

---

## 17. Frontend

| Aspecto | AS-IS |
|---------|-------|
| Contextos | **Um** (`organizationalLinks` / `activeContext`) |
| N vínculos | Não implementado (TODO OQ-027 em `session.store.ts`) |
| Equipe obrigatória | Não — form trata como opcional |
| Fed/Sing/Área obrigatórios | Não — apenas `federationId` obrigatório no create |

**Classificação:** **GAP** vs DEC-FA-003 e nova regra.

---

## 18. Testes

| Padrão | Evidência de domínio? | Classificação |
|--------|----------------------|---------------|
| Vínculo único completo | Reflete AS-IS feliz-path | Reflexo AS-IS |
| Só federação | Reflete `locateOrCreate` | Reflexo AS-IS |
| N vínculos | Ausente | GAP |
| Equipe opcional | Parcial — testes usam equipe no happy path | Reflexo AS-IS |

---

## 19. Gaps e conflitos

| ID | Descrição | Classificação |
|----|-----------|---------------|
| **G1** | DDL: `COD_SINGULAR`/`COD_AREA` nullable vs nova regra | **CONFLICT** |
| **G2** | `locateOrCreate` sem singular/área | **CONFLICT** |
| **G3** | 4 colunas vs N vínculos (DEC-FA-003) | **CONFLICT** (INC-PA-001) |
| **G4** | `/auth/me` objeto único vs N contextos | **CONFLICT** (INC-PA-003) |
| **G5** | `AREA.COD_SINGULAR` nullable — derivação frágil | **GAP** / **RISCO** |
| **G6** | Semântica das colunas quando N>1 | **DECISÃO PENDENTE** |
| **G7** | OQ-008 — N equipes/área | **DECISÃO PENDENTE** |
| **G8** | Vínculos cross-singular | **DECISÃO PENDENTE** |
| **G9** | `03-physical-model.md` “colaborador federativo” | **DRIFT** vs nova regra |

---

## 20. Recomendação (não aprovada)

### 20.1 Modelo TO-BE integrado

```text
COLABORADOR
  ├── identidade (NOM, EMAIL, ZIMBRA, FLG_ATIVO, …)
  ├── COD_FEDERACAO          (âncora identidade — PD-12)
  ├── COD_CARGO → CARGO      (1:1 — DEC-DB-027)
  ├── COD_GESTOR             (legado — PD-04)
  └── N × VINCULO_ORGANIZACIONAL
         ├── COD_AREA        NOT NULL
         └── COD_EQUIPE      NULL

Contexto Ativo (sessão) = 1 vínculo selecionado
  → snapshot { federationId, singularId, areaId, teamId } derivado
```

### 20.2 PD-02

**RECOMENDAÇÃO:** `VINCULO_ORGANIZACIONAL` (1 linha = 1 vínculo). Colunas org em `COLABORADOR` **não** sustentam N vínculos — deprecar semanticamente, remover em etapa futura.

### 20.3 PD-03

**RECOMENDAÇÃO:** unidade folha = **`COD_AREA` obrigatória** + **`COD_EQUIPE` opcional** por vínculo. Federação e singular **derivadas** — coerente com nova regra **por vínculo**, sem armazenar `COD_SINGULAR`/`COD_FEDERACAO` na tabela de vínculo.

### 20.4 Nova regra × implementação

| Requisito confirmado | Onde materializar TO-BE |
|---------------------|-------------------------|
| Fed + Sing + Área por colaborador | **Por vínculo** (derivado), não repetido 4× em `COLABORADOR` |
| Equipe opcional | `COD_EQUIPE NULL` no vínculo |
| N vínculos | N linhas em `VINCULO_ORGANIZACIONAL` |

---

## 21. Decisões humanas restantes

| ID | Questão | Bloqueia DEC-DB-028? |
|----|---------|---------------------|
| **PD-02** | Aprovar `VINCULO_ORGANIZACIONAL` | **Sim** |
| **PD-03** | Confirmar Área NN + Equipe opcional no vínculo | **Sim** |
| **PD-12** | `COD_FEDERACAO` permanece só como identidade em `COLABORADOR`? | **Parcial** |
| **Semântica legado** | Colunas org em `COLABORADOR` = snapshot contexto ativo vs deprecação imediata? | **Sim** |
| **OQ-008** | Múltiplas equipes na mesma área | Recomendado antes de UK |
| **Cross-singular** | Vínculos em singulares diferentes no mesmo colaborador | Recomendado |
| **AREA.COD_SINGULAR** | Exigir NN para áreas operacionais? | Recomendado para derivação |
| **locateOrCreate** | Como criar colaborador sem vínculo até admin/onboarding? | **Sim** — conflito com nova regra |

### Pontos para futura DEC-DB-028

1. Estrutura `VINCULO_ORGANIZACIONAL` (`COD_AREA` NN, `COD_EQUIPE` NULL).
2. Derivação de singular/federação — sem FKs redundantes no vínculo.
3. Deprecação das FKs `COD_SINGULAR`/`COD_AREA`/`COD_EQUIPE` em `COLABORADOR` (cronograma).
4. Tratamento de `COD_FEDERACAO` em `COLABORADOR` (identidade).
5. UKs de unicidade de vínculo.
6. Regras OQ-008 e cross-singular.
7. Alinhamento FT-AUTH `locateOrCreate` com obrigatoriedade de vínculo.

---

## 22. Impactos futuros (sem implementação)

| Artefato | Impacto |
|----------|---------|
| DDL | `VINCULO_ORGANIZACIONAL`; eventual NN em `AREA.COD_SINGULAR`; migration |
| Modelo lógico/físico | Nova entidade; revisar “colaborador federativo” |
| JPA / API / FE | Lista de vínculos; `contexts[]`; obrigatoriedade sing+area |
| FT-AUTH | `locateOrCreate` — colaborador sem vínculo até onboarding |
| FT-PRIMEIRO-ACESSO | Desbloqueio de multi-contexto real |
| DEC-DB-027 / PKG-FE-02 | Bloqueados até DEC-DB-028 + contratos |
| Testes | N vínculos; área sem equipe; login sem vínculo |

---

## 23. Conclusão PD-02

| Campo | Valor |
|-------|-------|
| **Status** | **PRONTA PARA DECISÃO** |
| **Estrutura recomendada** | `VINCULO_ORGANIZACIONAL` (1:N) |
| **Conflito central** | 4 colunas em `COLABORADOR` = 1 vínculo; incompatível com DEC-FA-003 |
| **Nova regra** | Reforça que obrigatoriedade Fed/Sing/Área aplica-se **por vínculo**, não às colunas legadas |
| **Próximo passo** | Aprovação humana → DEC-DB-028 |

---

## 24. Conclusão PD-03

| Campo | Valor |
|-------|-------|
| **Status** | **PRONTA PARA DECISÃO** |
| **Unidade folha** | `COD_AREA` obrigatória; `COD_EQUIPE` opcional |
| **Federação/Singular** | Derivadas pela hierarquia — **não** armazenar no vínculo |
| **Nova regra** | **Coerente** com Alternativa D da reconciliação v1.0 |
| **Próximo passo** | Aprovação humana → DEC-DB-028 |

---

## 25. Status PD-12

| Campo | Valor |
|-------|-------|
| **Status** | **PENDENTE** |
| **Avanço** | `COD_FEDERACAO` classificado como **âncora de identidade** (FT-AUTH), não como vínculo N |
| **Pendência** | Ratificar se permanece NN em `COLABORADOR` quando vínculos estão em tabela separada |

---

## 26. Checklist de conclusão (critério da etapa)

| Critério | Atendido |
|----------|----------|
| Todo colaborador pertence a uma Federação (TO-BE) | ✅ por vínculo derivado + âncora identidade |
| Todo colaborador pertence a uma Singular (TO-BE) | ✅ por vínculo derivado |
| Todo colaborador pertence a uma Área (TO-BE) | ✅ por vínculo (`COD_AREA` NN) |
| Equipe opcional | ✅ |
| Área pode ter N equipes | ✅ (modelo EQUIPE→AREA) |
| N vínculos válido (DEC-FA-003) | ✅ |
| Coexistência das regras explicada | ✅ |
| Dados em COLABORADOR vs vínculo vs derivados | ✅ seções 11, 14, 20 |
| Pontos indefinidos listados | ✅ seção 21 |
| Decisões humanas antes DEC-DB-028 | ✅ seção 21 |

```text
ETAPA 8.x v1.0 — CONCLUÍDA (reconciliação complementar)
Artefato anterior preservado: vinculo-organizacional-reconciliation-pd-02-03.md v1.0
```

---

## Referências

- [`vinculo-organizacional-reconciliation-pd-02-03.md`](vinculo-organizacional-reconciliation-pd-02-03.md) v1.0
- [`organizational-authorization-formalization-etapa6.md`](organizational-authorization-formalization-etapa6.md)
- [`cargo-vinculo-reconciliation-pd-cargo-01-02-03.md`](cargo-vinculo-reconciliation-pd-cargo-01-02-03.md) v2.0
- `database/ddl/003-create-tables.sql`, `004-create-constraints.sql`
- `database/model/03-physical-model.md`, `02-logical-model.md`
- `specs/features/primeiro-acesso/specification.md`, `api.md`, `traceability.md`
- `specs/features/session/specification.md`
- `specs/features/colaborador/api.md`, `specification.md`
- `docs/domain/09-business-rules.md` (BR-009..012, BR-040..041)
- `docs/governance/03-open-decisions.md` (DEC-FA-003, DEC-DB-021)
