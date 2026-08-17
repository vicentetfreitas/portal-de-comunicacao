# Reconciliação PD-CARGO-01/02/03 — CARGO × COLABORADOR × VÍNCULO ORGANIZACIONAL

| Campo | Valor |
|-------|-------|
| Artefato | cargo-vinculo-reconciliation-pd-cargo-01-02-03.md |
| Camada | Construction / Review |
| Versão | **2.0** |
| Data | 2026-08-14 |
| Categoria documental | Evidence |
| Status | **EVIDÊNCIA** — reconciliação DEC-DB-027 **encerrada** por **DH-CARGO-01** (2026-08-17); ver `docs/governance/03-open-decisions.md` |
| Precede | [`organizational-authorization-formalization-etapa6.md`](organizational-authorization-formalization-etapa6.md) |
| Supersedes | v1.0 deste artefato (hipótese N cargos / `ATRIBUICAO_CARGO` — **revogada**) |

---

## 1. Resumo executivo

| Tema | Conclusão |
|------|-----------|
| **Cardinalidade cargo** | **DECIDIDA:** `COLABORADOR` 1 ── 1 `CARGO` quando CARGO atribuído (**DH-CARGO-01** — não exige CARGO na criação) |
| **Cardinalidade vínculo** | **DECIDIDA (DEC-FA-003):** `COLABORADOR` 1 ── N vínculos organizacionais |
| **PD-CARGO-03** | **Recomendação:** cargo em **COLABORADOR** (Hipótese A); **não** no vínculo (Hipótese B redundante) |
| **PD-CARGO-01** | Catálogo `CARGO` + `COLABORADOR.COD_CARGO` FK; `NOM_CARGO` necessário; `DSC_CARGO` sem requisito explícito |
| **ATRIBUICAO_CARGO** | **Não necessária** para cardinalidade 1:1 |
| **Histórico de cargo** | **Fora do escopo atual** — sem requisito explícito no repositório |
| **AS-IS** | Sem entidade/tabela/API/FE de cargo; vínculo único via FKs em `COLABORADOR` |
| **Implementação** | Modelo formalizado em DEC-DB-027; obrigatoriedade na criação **superseded** por **DH-CARGO-01** (2026-08-17) |

**Complemento (2026-08-17 — DH-CARGO-01):** CARGO permanece domínio com persistência própria. **Obrigatoriedade na criação** de DEC-DB-027 foi **superseded**. Ver `database/model/05-decisions-and-risks.md` § Supersession parcial.

---

## 2. Decisões de domínio consideradas

| ID | Decisão | Status nesta reconciliação |
|----|---------|---------------------------|
| **DEC-ORG-002** | `CARGO` entidade independente; função organizacional; **CARGO ≠ PAPEL ≠ ADMIN_***; cargo não concede autorização | Confirmada |
| **DEC-FA-003** | N vínculos organizacionais; um Contexto Ativo na sessão | Confirmada |
| **Cardinalidade cargo (responsável)** | **1 colaborador = 1 cargo** (cargo do cadastro atual) | **Confirmada — não reabrir** |
| **DEC-ORG-001** | Federação → Singular → Área → Equipe; equipe opcional | Confirmada |
| **DEC-DB-020** | Vínculo cadastral ≠ `PAPEL_ATRIBUICAO` | Confirmada; estendida a cargo ≠ papel |

**Separação obrigatória de cardinalidades:**

```text
COLABORADOR × CARGO              → 1 : 1
COLABORADOR × VÍNCULO            → 1 : N   (DEC-FA-003)
COLABORADOR × PAPEL (ADMIN_*)    → 1 : N   (PAPEL_ATRIBUICAO — autorização)
```

---

## 3. AS-IS encontrado (evidências)

### 3.1 CARGO / cargo / função

| Camada | Evidência | Resultado |
|--------|-----------|-----------|
| `database/ddl/` | Sem tabela `CARGO`; sem `COD_CARGO` em `COLABORADOR` | **GAP** |
| `V007` / `VAL-DB-02` | Remove coluna legada `DES_CARGO` | Atributo texto histórico, **não** entidade |
| `02-logical-model.md` | 23 entidades — **sem** CARGO | **GAP** |
| `backend/` | Zero referências a `cargo`, `CARGO`, `DES_CARGO` | **GAP** |
| `frontend/` | Zero referências a cargo | **GAP** |
| `specs/features/colaborador/` | Sem campo cargo na API | **GAP** |
| `DEC-DB-016` | Rejeitou entidade `CARGO` (2026-07-10) | **CONFLICT** histórico → superseded parcial por DEC-ORG-002 |

### 3.2 Vínculo organizacional

| Camada | Evidência | Resultado |
|--------|-----------|-----------|
| DDL `COLABORADOR` | `COD_FEDERACAO` (NN), `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE` opcionais | **1 conjunto** de FKs |
| JPA `ColaboradorEntity` | Mesmas 4 FKs + `gestorId` | AS-IS single-link |
| `ColaboradorDomainService.resolveOrganizationalLinks` | Valida cadeia singular→área→equipe | Coerência **único** conjunto |
| `/auth/me` | `organizationalLinks` — **objeto único** | **CONFLICT** com DEC-FA-003 N vínculos |
| `specs/features/session/specification.md` | “um vínculo por tipo” AS-IS documentado | **GAP** TO-BE |
| `specs/features/primeiro-acesso/traceability.md` | **INC-PA-001**: 1 vínculo AS-IS vs BR-041 N vínculos | **CONFLICT** classificado |

### 3.3 GESTOR / LÍDER (FKs legadas — não reinterpretar como CARGO)

| Artefato | Uso AS-IS | Relação com CARGO TO-BE |
|----------|-----------|-------------------------|
| `COLABORADOR.COD_GESTOR` | Gestor direto (auto-ref); API `managerId`; RN-006/008 | **Relacionamento** colaborador↔colaborador — **não** catálogo CARGO |
| `AREA.COD_GESTOR` | Gestor único da área; API `managerId` | **Responsabilidade** legada FK — **não** entidade CARGO |
| `EQUIPE.COD_LIDER` | Líder único; API `leaderId` | **Pendente** PD-07 — **não** entidade CARGO |
| Backend | `validateManager`, `validateLeader` | Valida colaborador ativo existente |
| Frontend | `managerId` (colaborador); `leaderId` (equipe forms) | Identificador de **pessoa**, não título de cargo |
| Specs FT-AREA/FT-EQUIPE | RN referenciam DEC-DB-015 FK | Documentação AS-IS |

**Veredito:** nova decisão CARGO **não** reinterpreta automaticamente `COD_GESTOR`/`COD_LIDER`.

### 3.4 PAPEL / autorização (paralelo estrutural, domínio distinto)

| Artefato | Conteúdo |
|----------|----------|
| `PAPEL` + `PAPEL_ATRIBUICAO` | Catálogo + atribuição **com escopo** — modelo de **autorização** |
| `loadPermissions()` | Lista vazia — não implementado |
| Seed `ADMINISTRADOR`, `GESTOR_DOCUMENTAL`, … | **DRIFT** nomenclatura vs `ADMIN_*` TO-BE |

### 3.5 Histórico de cargo

| Busca | Resultado |
|-------|-----------|
| Specs, BR, domain, `docs/` | **Sem** requisito de histórico/vigência de **cargo** |
| `PAPEL_ATRIBUICAO` | Vigência para **papel** — não transferível como requisito de cargo |

**Classificação:** histórico de cargo = **fora do escopo atual**; pendência futura opcional (integração RH / auditoria de função).

---

## 4. PD-CARGO-01 — análise do catálogo `CARGO`

### 4.1 Perguntas respondidas

| Pergunta | Resposta |
|----------|----------|
| Melhor estrutura conceitual? | Tabela catálogo **`CARGO`** + FK **`COLABORADOR.COD_CARGO`** (1:1) |
| `NOM_CARGO` necessário? | **Sim** — exemplos de domínio são nomes institucionais (`Gestor de Tecnologia da Informação`) |
| `DSC_CARGO` necessário? | **Sem requisito explícito** — opcional/futuro; não criar por convenção alone |
| Atributos adicionais justificados? | Ver tabela abaixo |
| Alinhamento Oracle? | Espelhar padrão `PAPEL` / entidades org (`FLG_ATIVO`, auditoria) |

### 4.2 Atributos candidatos vs padrão do projeto

| Atributo | Padrão projeto (`PAPEL`, `FEDERACAO`, …) | Justificativa domínio | Recomendação |
|----------|--------------------------------------------|----------------------|--------------|
| `COD_CARGO` | PK `NUMBER(19)` + sequence `SQ_CARGO_COD_CARGO` | Identificador surrogate | **FATO** — padrão baseline |
| `NOM_CARGO` | `NOM_PAPEL` `VARCHAR2(100)` NN | Nome da função (exemplos DEC-ORG-002) | **RECOMENDAÇÃO** — obrigatório |
| `DSC_CARGO` | `DSC_PAPEL` `CLOB` opcional em `PAPEL` | Sem BR/spec pedindo descrição de cargo | **PENDENTE** — omitir no catálogo mínimo |
| `FLG_ATIVO` | Padrão `CHAR(1)` `'S'/'N'` + CK | Inativar cargo sem apagar catálogo | **RECOMENDAÇÃO** — alinhado a catálogos |
| `DAT_CADASTRO` | `DEFAULT SYSTIMESTAMP NOT NULL` | Auditoria institucional | **RECOMENDAÇÃO** — padrão projeto |
| `DAT_ATUALIZACAO` | Nullable `TIMESTAMP(6)` | Auditoria de alteração | **RECOMENDAÇÃO** — padrão projeto |

**Catálogo mínimo recomendado (conceitual, não DDL):**

```text
CARGO
  COD_CARGO        PK
  NOM_CARGO        NOT NULL
  FLG_ATIVO        NOT NULL DEFAULT 'S'
  DAT_CADASTRO     NOT NULL
  DAT_ATUALIZACAO  NULL
```

### 4.3 Alternativas de catálogo (analisadas — não implementadas)

| ID | Modelo | Vantagens | Desvantagens | Veredito |
|----|--------|-----------|--------------|----------|
| **K-01** | `CARGO` catálogo + `COLABORADOR.COD_CARGO` | 1:1 simples; normalizado; consulta direta | Exige FK nullable até cadastro | **RECOMENDADA** |
| **K-02** | `NOM_CARGO` texto em `COLABORADOR` | Sem tabela | Duplica strings; sem catálogo; viola entidade DEC-ORG-002 | **Rejeitada** |
| **K-03** | `CARGO` + `ATRIBUICAO_CARGO` | Flexível | **Desnecessária** para 1:1; complexidade extra | **Rejeitada** (cardinalidade decidida) |
| **K-04** | Reuso `PAPEL` | Já existe DDL | Viola DEC-ORG-002 (CARGO ≠ PAPEL) | **Rejeitada** |

---

## 5. PD-CARGO-02 — cardinalidade 1:1 (DECISÃO CONFIRMADA)

### 5.1 Regra (não reaberta)

```text
COLABORADOR  1 ───── 1  CARGO
```

- Um colaborador possui **somente um cargo** no contexto do cadastro atual.
- **Não** investigar `COLABORADOR → N CARGOS`.
- **Não** usar `ATRIBUICAO_CARGO` para multiplicar cargos.

### 5.2 AS-IS vs regra 1:1

| Aspecto | AS-IS | Suporta 1:1? |
|---------|-------|--------------|
| Persistência cargo | Inexistente | **GAP** — nada a violar ainda |
| `DES_CARGO` removido | Sem campo cargo | Neutro |
| Múltiplos campos que poderiam ser “função” | Apenas FKs org + gestor | Gestor ≠ cargo |

**Divergências:** nenhuma implementação contradiz 1:1 hoje; **falta** mecanismo para 1 cargo.

### 5.3 Histórico vs cardinalidade atual

| Conceito | Decisão |
|----------|---------|
| Cargo **atual** | 1:1 — **confirmado** |
| Histórico de cargos anteriores | **Fora do escopo** — sem requisito explícito |
| Vigência `DAT_INICIO`/`DAT_FIM` em cargo | **Não assumir** — pendência futura se RH/auditoria exigir |

Implementar histórico exigiria tabela de movimentação — **não** `ATRIBUICAO_CARGO` para N cargos simultâneos.

---

## 6. PD-CARGO-03 — cargo × vínculo organizacional

### 6.1 Hipóteses (análise obrigatória)

#### Hipótese A — cargo no colaborador

```text
COLABORADOR
    COD_CARGO → CARGO

Vínculos (N):
    Área TI
    Área Financeiro
    Equipe Desenvolvimento
```

| Critério | Avaliação |
|----------|-----------|
| Regra 1 cargo | ✅ Um FK |
| DEC-FA-003 N vínculos | ✅ Ortogonal — vínculos em tabela/FK separada |
| Normalização | ✅ Sem repetir `COD_CARGO` por vínculo |
| Exemplo Vicente | ✅ Um cargo; três vínculos; dois `ADMIN_AREA` |
| Oracle/JPA | FK simples em `COLABORADOR`; join direto |
| API | `cargoId` / `cargoName` em `ColaboradorResponse` |

#### Hipótese B — cargo no vínculo

```text
VINCULO_ORGANIZACIONAL
    COD_CARGO → CARGO   (repetido em cada linha)
```

| Critério | Avaliação |
|----------|-----------|
| Regra 1 cargo | ⚠️ Mesmo `COD_CARGO` em N linhas — **redundância** |
| Consistência | Risco de divergência se vínculos têm `COD_CARGO` diferentes |
| Evidência de requisito | **Nenhuma** no repositório |
| Pergunta “repetir cargo em cada vínculo?” | **Sim** — viola espírito de única atribuição |

**Veredito PD-CARGO-03:**

| Classificação | Conteúdo |
|---------------|----------|
| **RECOMENDAÇÃO** | **Hipótese A** — `COLABORADOR.COD_CARGO` → catálogo `CARGO` |
| **REJEIÇÃO analítica** | Hipótese B — sem evidência; redundante com 1:1 |
| **FATO** | N vínculos **não alteram** modelagem de cargo; alteram **vínculo** (PD-02) |

### 6.2 `ATRIBUICAO_CARGO` e `CARGO` no vínculo

| Pergunta | Resposta |
|----------|----------|
| Existe necessidade de `ATRIBUICAO_CARGO`? | **Não** para cardinalidade 1:1 confirmada |
| Associar `CARGO` a `VINCULO_ORGANIZACIONAL`? | **Não recomendado** — duplicação de `COD_CARGO` |

---

## 7. CARGO × COLABORADOR × VÍNCULO × PAPEL (modelo integrado TO-BE)

### 7.1 Cenário de validação — Vicente Freitas

```text
Colaborador: Vicente Freitas
  COD_CARGO → Gestor de Tecnologia da Informação     (1 cargo — COLABORADOR)

Vínculos (N — VINCULO_ORGANIZACIONAL ou evolução PD-02):
  - Área TI
  - Área Financeiro
  - Equipe Desenvolvimento

Papéis (N — PAPEL_ATRIBUICAO):
  - ADMIN_AREA → TI
  - ADMIN_AREA → Financeiro

Validações:
  ✅ Cargo Gestor de TI ≠ ADMIN_AREA automaticamente
  ✅ ADMIN_AREA em TI e Financeiro sem ADMIN_SINGULAR/EQUIPE
  ✅ Três vínculos sem três cargos
```

### 7.2 Três eixos independentes

| Eixo | Cardinalidade | Persistência TO-BE candidata |
|------|---------------|------------------------------|
| Cargo (função) | 1:1 | `COLABORADOR.COD_CARGO` |
| Vínculo (operação) | 1:N | `VINCULO_ORGANIZACIONAL` (PD-02) |
| Autorização | 1:N | `PAPEL_ATRIBUICAO` (existente) |

---

## 8. Impacto DEC-ORG-002

| Verificação | Achados | Classificação |
|-------------|---------|---------------|
| CARGO ≠ PAPEL | Sem implementação que misture | **OK** (GAP = ausência) |
| CARGO ≠ ADMIN_* | Sem código FE `ADMIN` ligado a cargo | **OK** |
| Cargo concede autorização? | Não no código; `GESTOR_DOCUMENTAL` é **papel** seed | **DRIFT** seed DDL vs TO-BE |
| GESTOR = PAPEL/ADMIN? | `managerId` = FK pessoa; `sessionAdministratorEmails` = authz | **GAP** terminológico, não CONFLICT de implementação cargo |
| Docs tratam gestor como autorização? | `01-vision` separa atores administradores de colaborador | **OK** conceitual |

**Não corrigido** nesta etapa.

---

## 9. Impacto DEC-FA-003

| Artefato | Pressupõe 1 área/equipe? | Classificação |
|----------|--------------------------|---------------|
| `COLABORADOR` DDL | 1 conjunto FK | **CONFLICT** com N vínculos |
| `AuthenticationService` | Snapshot único | **CONFLICT** |
| `session.store` | `organizationalLinks` objeto único | **CONFLICT** |
| FT-PRIMEIRO-ACESSO specs | N vínculos, seleção contexto | **TO-BE** documentado |
| INC-PA-001 | Explícito no traceability | **CONFLICT** registrado |
| Cargo 1:1 | Ortogonal a N vínculos | **Sem conflito** |

**Conclusão:** DEC-FA-003 conflita com **vínculo** AS-IS, **não** com modelo de cargo 1:1.

---

## 10. Impacto FKs `COD_GESTOR` / `COD_LIDER`

| FK | Camadas com uso | Representa (AS-IS) | É CARGO? |
|----|-----------------|-------------------|----------|
| `COLABORADOR.COD_GESTOR` | DDL, JPA, API `managerId`, DomainService, FE form, specs RN-006 | Hierarquia reporting / gestor direto | **Não** |
| `AREA.COD_GESTOR` | DDL, JPA, API, AreaDomainService, specs FT-AREA | Responsável único área (pessoa) | **Não** |
| `EQUIPE.COD_LIDER` | DDL, JPA, API `leaderId`, Equipe FE/E2E, specs FT-EQUIPE | Líder único equipe (pessoa) | **Não** (PD-07 aberto) |

**Legado:** DEC-DB-015/016 — permanece vigente AS-IS. **Não remover** nesta etapa.

---

## 11. Normalização e integridade referencial

### 11.1 Duplicação `COD_CARGO` em vínculos (problema Hipótese B)

```text
❌ Problemático (Hipótese B):
  VINCULO TI          COD_CARGO = 3
  VINCULO FINANCEIRO  COD_CARGO = 3
  VINCULO EQUIPE X    COD_CARGO = 3

✅ Normalizado (Hipótese A):
  COLABORADOR         COD_CARGO = 3
  VINCULOS → TI, FINANCEIRO, EQUIPE X (sem COD_CARGO)
```

| Critério | Hipótese A | Hipótese B |
|----------|------------|------------|
| Normalização | ✅ | ❌ redundância |
| Integridade 1 cargo | ✅ um FK | ⚠️ N cópias iguais |
| Manutenção mudança cargo | 1 UPDATE | N UPDATEs |
| JPA | `@ManyToOne` em `ColaboradorEntity` | Repetido em entidade vínculo |
| Consultas | Simples join colaborador→cargo | Agregar vínculos para “cargo” |

---

## 12. Alternativas analisadas — matriz de impacto

| Alternativa | Oracle | JPA | API | Frontend | Testes | Risco |
|-------------|--------|-----|-----|----------|--------|-------|
| **A** `CARGO` + `COLABORADOR.COD_CARGO` | 1 tabela + 1 FK + sequence | `CargoEntity` + field em `ColaboradorEntity` | `cargoId` em DTO colaborador | Select catálogo no form | Estender colaborador tests | Baixo |
| **B** `COD_CARGO` em vínculo | FK em cada linha vínculo | Campo em entidade vínculo | Lista vínculos com cargo duplicado | Complexo | N× consistência | **Alto** redundância |
| **K-03** `ATRIBUICAO_CARGO` | 2 tabelas | 2 entidades | Endpoints extras | — | — | **Rejeitada** (1:1) |
| **K-02** texto em colaborador | 1 coluna | String | `jobTitle` | Text field | — | Sem catálogo |

---

## 13. Conclusão recomendada

### FATO

- Repositório **não implementa** CARGO.
- AS-IS vínculo = **1 conjunto** FK em `COLABORADOR` (**CONFLICT** com DEC-FA-003 documentado em INC-PA-001).
- Cardinalidade **1 colaborador : 1 cargo** confirmada pelo responsável.
- `COD_GESTOR`/`COD_LIDER` são FKs a **colaborador pessoa**, não catálogo CARGO.
- Histórico de cargo **sem requisito** explícito.

### HIPÓTESE (para implementação futura)

- Catálogo institucional `CARGO` com `NOM_CARGO` + auditoria padrão.
- `DSC_CARGO` só se Feature de gestão de cargos exigir.

### RECOMENDAÇÃO

1. **PD-CARGO-01:** **encerrada** — catálogo `CARGO` mínimo (K-01) + `COLABORADOR.COD_CARGO` NOT NULL — ver **DEC-DB-027**.
2. **PD-CARGO-02:** **encerrada** — 1:1; sem `ATRIBUICAO_CARGO`.
3. **PD-CARGO-03:** **encerrada** — **Hipótese A**; vínculo N separado (PD-02); **não** repetir cargo no vínculo.
4. ~~Formalizar **DEC-DB-027**~~ — **concluído** (2026-08-14).

### DECISÃO PENDENTE (implementação)

| ID | Questão |
|----|---------|
| PD-CARGO-01-R | `DSC_CARGO` no catálogo? UK em `NOM_CARGO`? |
| PD-CARGO-01-R | Regra de cargo inativo na atribuição? |
| PD-02 | Tabela `VINCULO_ORGANIZACIONAL` vs evolução FK |
| PD-03 | Vínculo folha = área vs equipe vs ambos |
| ~~DEC-DB-027~~ | ~~Aprovação formal catálogo + FK~~ — **aprovada** (2026-08-14) |

---

## 14. Pendências remanescentes

| ID | Status após v2.0 / DEC-DB-027 |
|----|------------------------------|
| PD-CARGO-01 | **Encerrada** — estrutura TO-BE em DEC-DB-027 |
| PD-CARGO-02 | **Encerrada** (domínio) — AS-IS GAP |
| PD-CARGO-03 | **Encerrada** (domínio) — Hipótese A |
| PD-02 / PD-03 | **Abertas** — vínculo N |
| Histórico cargo | **Fora do escopo** / futuro |
| PD-04..07 | **Abertas** — FK gestor/líder legadas |

---

## 15. Impactos futuros (sem implementação)

| Área | Impacto |
|------|---------|
| DDL | `CREATE TABLE CARGO`; `ALTER COLABORADOR ADD COD_CARGO`; sequence; grants |
| JPA | `CargoEntity`; `ColaboradorEntity.cargoId` |
| FT-COLABORADOR API | `cargoId` / embed `cargo` no response |
| FT-COLABORADOR FE | Select catálogo cargos |
| `/auth/me` | Opcional expor cargo do colaborador (não misturar com `roles`) |
| FT-PRIMEIRO-ACESSO | N vínculos independentes de cargo |
| PKG-FE-02 | Bloqueado até DEC-DB-027 + contrato vínculo (PD-02) |
| Seed | Catálogo inicial (Presidente, Gestor TI, Analista, …) |

---

## 16. Critério de conclusão — checklist

| Pergunta | Resposta |
|----------|----------|
| Como CARGO representado conceitualmente? | Entidade catálogo + FK 1:1 em `COLABORADOR` |
| Catálogo mínimo? | `COD_CARGO`, `NOM_CARGO`, `FLG_ATIVO`, auditoria |
| Exatamente um cargo? | **Sim** — decisão confirmada |
| Como relacionar ao colaborador? | `COLABORADOR.COD_CARGO` → `CARGO` |
| Cargo depende do vínculo? | **Não** — recomendação Hipótese A |
| N vínculos altera modelagem cargo? | **Não** — eixos separados |
| Necessidade `ATRIBUICAO_CARGO`? | **Não** (1:1) |
| CARGO em `VINCULO_ORGANIZACIONAL`? | **Não recomendado** |
| Conflitos DEC-ORG-002? | GAP implementação; DRIFT seed `GESTOR_DOCUMENTAL` |
| Conflitos DEC-FA-003? | CONFLICT vínculo AS-IS (INC-PA-001); cargo ortogonal |
| Impacto `COD_GESTOR`/`COD_LIDER`? | Legado pessoa↔pessoa; **não** CARGO |
| Decisão antes de implementar? | **DEC-DB-027** aprovada; pendente **PD-02** vínculo |

```text
ETAPA 7 v2.0 — CONCLUÍDA
```

---

## Referências

- [`organizational-authorization-formalization-etapa6.md`](organizational-authorization-formalization-etapa6.md)
- `docs/governance/03-open-decisions.md` (DEC-ORG-002, DEC-FA-003)
- `database/model/05-decisions-and-risks.md` (DEC-DB-016 reconciliação)
- `specs/features/primeiro-acesso/traceability.md` (INC-PA-001)
- `database/ddl/003-create-tables.sql` (`PAPEL`, `COLABORADOR`)
