# Reconciliação PD-02 / PD-03 — Vínculo Organizacional N × Unidade Folha

| Campo | Valor |
|-------|-------|
| Artefato | vinculo-organizacional-reconciliation-pd-02-03.md |
| Camada | Construction / Review |
| Versão | **1.0** |
| Data | 2026-08-14 |
| Categoria documental | Evidence |
| Status | **ETAPA 8 — CONCLUÍDA** (análise; decisão formal pendente) |
| Complemento | [`vinculo-organizacional-reconciliation-pd-02-03-complementar.md`](vinculo-organizacional-reconciliation-pd-02-03-complementar.md) v1.0 — semântica FKs × nova regra Fed/Sing/Área obrigatórios |
| Precede | [`cargo-vinculo-reconciliation-pd-cargo-01-02-03.md`](cargo-vinculo-reconciliation-pd-cargo-01-02-03.md) |
| Relaciona | [`organizational-authorization-formalization-etapa6.md`](organizational-authorization-formalization-etapa6.md) |

---

## 1. Resumo executivo

| Tema | Conclusão |
|------|-----------|
| **Cardinalidade N** | **FATO** — DEC-FA-003 aprovada; BR-041; specs FT-PRIMEIRO-ACESSO/FT-SESSION |
| **AS-IS persistência** | **FATO** — 1 conjunto de FKs em `COLABORADOR` (`COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE` opcionais; `COD_FEDERACAO` NN) |
| **AS-IS runtime** | **FATO** — API/sessão/JWT expõem **um** objeto `organizationalLinks` |
| **Conflito AS-IS × TO-BE** | **CONFLICT** — INC-PA-001, INC-PA-003 documentados |
| **PD-02 (estrutura N)** | **RECOMENDAÇÃO** — tabela `VINCULO_ORGANIZACIONAL` (1 linha = 1 vínculo); **não** manter FKs múltiplas em `COLABORADOR` |
| **PD-03 (unidade folha)** | **RECOMENDAÇÃO** — **ÁREA obrigatória** + **EQUIPE opcional** por vínculo (Alternativa D) |
| **FKs mínimas do vínculo** | **RECOMENDAÇÃO** — `COD_COLABORADOR`, `COD_AREA` (NN), `COD_EQUIPE` (NULL); **derivar** singular e federação |
| **PD-12 (`COD_FEDERACAO`)** | **DECISÃO PENDENTE** — identidade institucional vs vínculo; não resolvida nesta etapa |
| **OQ-008 (N equipes/área)** | **DECISÃO PENDENTE** — Cenário D tecnicamente suportável; sem BR explícita |
| **Implementação** | **Bloqueada** — aguarda DEC de banco formal (ex.: DEC-DB-028) após aprovação humana |

```text
TO-BE recomendado (conceitual — não implementado):

COLABORADOR ──1── N── VINCULO_ORGANIZACIONAL
                         ├── COD_AREA      (NN)  → unidade folha base
                         └── COD_EQUIPE    (NULL) → refinamento opcional
                              ↓
                         AREA → SINGULAR → FEDERACAO (derivado)
```

---

## 2. Decisões normativas utilizadas

| ID | Decisão | Uso nesta reconciliação |
|----|---------|-------------------------|
| **DEC-FA-003** | N vínculos + Contexto Ativo | **FATO** — cardinalidade N não reaberta |
| **DEC-FA-002** | Colaborador operacional exige Área | **FATO** — vínculo válido exige área (RN-PA-001) |
| **DEC-ORG-001** | Federação → Singular → Área → Equipe | **FATO** — hierarquia oficial |
| **DEC-ORG-002** | CARGO ≠ PAPEL; 1 cargo/colaborador | **FATO** — ortogonal ao vínculo |
| **DEC-DB-027** | `COLABORADOR.COD_CARGO` NN | **FATO** — não reaberto; sem `COD_CARGO` no vínculo |
| **DEC-DB-020** | FK org em `COLABORADOR` ≠ `PAPEL_ATRIBUICAO` | **FATO** — vínculo ≠ autorização |
| **DEC-DB-015/016** | `COD_GESTOR`/`COD_LIDER` AS-IS | **FATO** — não reinterpretados |
| **BR-010, BR-011, BR-012** | Área obrigatória; contexto coerente | **FATO** — specs e domínio |
| **BR-040, BR-041, BR-042** | Hierarquia; N vínculos; Home dinâmica | **FATO** — FT-PRIMEIRO-ACESSO |
| **REF-DB-CTX-01** | Sem `COD_*_CTX` em `AUTH_SESSAO` | **FATO** — contexto fora do DDL de sessão |

**Nota sobre CARGO (instrução da etapa):** o briefing cita `DSC_CARGO` obrigatório e `FLG_ATIVO` default `N`. **DEC-DB-027** (SSOT de banco) define catálogo mínimo **sem** `DSC_CARGO` por padrão e `FLG_ATIVO` alinhado a catálogos (`'S'`). Esta reconciliação **não modifica DEC-DB-027**; registra **DRIFT** documental apenas se necessário em impacto de CARGO (seção 15).

---

## 3. Hierarquia organizacional

### 3.1 Cadeia DDL (FATO)

```text
FEDERACAO (COD_FEDERACAO PK)
    ↑ FK NOT NULL
SINGULAR (COD_SINGULAR PK, COD_FEDERACAO)
    ↑ FK nullable
AREA (COD_AREA PK, COD_SINGULAR, COD_GESTOR)
    ↑ FK NOT NULL
EQUIPE (COD_EQUIPE PK, COD_AREA, COD_LIDER)
    ↑ FKs nullable (org) + gestor
COLABORADOR (COD_FEDERACAO NN; COD_SINGULAR, COD_AREA, COD_EQUIPE, COD_GESTOR nullable)
```

Fonte: `database/ddl/003-create-tables.sql` L40–179; constraints `004-create-constraints.sql`.

### 3.2 JPA (FATO)

Entidades `FederacaoEntity`, `SingularEntity`, `AreaEntity`, `EquipeEntity`, `ColaboradorEntity` usam **campos `Long` escalares** — sem `@ManyToOne` nas FKs organizacionais.

### 3.3 Equipe opcional no vínculo (INFERÊNCIA)

| Evidência | Conclusão |
|-----------|-----------|
| BR-012, RN-PA-001 | `teamId` opcional no Contexto Ativo |
| `03-physical-model.md` L717 | Colaborador de singular: área obrigatória; equipe opcional |
| RN-005 FT-COLABORADOR | singular → área; equipe na área |
| DEC-ORG-001 / D-04 (Etapa 6) | Equipe opcional no vínculo operacional |

**INFERÊNCIA:** o domínio **permite** vínculo somente em Área, sem Equipe (Cenário A).

---

## 4. AS-IS de COLABORADOR

### 4.1 Inventário de colunas organizacionais

| Campo | Tipo Oracle | Nullable | FK | Referência | Índice | JPA | API (CRUD) | API (`/auth/me`) | FE |
|-------|-------------|----------|-----|------------|--------|-----|------------|------------------|-----|
| `COD_COLABORADOR` | `NUMBER(19)` | NOT NULL | PK | — | PK | `id` | `id` | `userId` | — |
| `COD_FEDERACAO` | `NUMBER(19)` | **NOT NULL** | Sim | `FEDERACAO` | `IDX_COLABORADOR_FEDERACAO` | `federacaoId` | `federationId` | `organizationalLinks.federationId` | `federationId` |
| `COD_SINGULAR` | `NUMBER(19)` | NULL | Sim | `SINGULAR` | `IDX_COLABORADOR_SINGULAR` | `singularId` | `singularId` | `.singularId` | `singularId` |
| `COD_AREA` | `NUMBER(19)` | NULL | Sim | `AREA` | `IDX_COLABORADOR_AREA` | `areaId` | `areaId` | `.areaId` | `areaId` |
| `COD_EQUIPE` | `NUMBER(19)` | NULL | Sim | `EQUIPE` | `IDX_COLABORADOR_EQUIPE` | `equipeId` | **`teamId`** | **`.teamId`** | **`teamId`** |
| `COD_GESTOR` | `NUMBER(19)` | NULL | Sim | `COLABORADOR` (self) | `IDX_COLABORADOR_GESTOR` | `gestorId` | **`managerId`** | *ausente* | `managerId` |
| `COD_CARGO` | — | — | — | — | — | — | — | — | — |

**FATO:** `COD_CARGO` **inexistente** em DDL/JPA/API/FE (DEC-DB-027 apenas TO-BE).

### 4.2 Semântica AS-IS documentada (FATO)

`database/model/03-physical-model.md` L711–717:

| Perfil | `COD_FEDERACAO` | `COD_SINGULAR` | `COD_AREA` | `COD_EQUIPE` |
|--------|-----------------|----------------|------------|--------------|
| Colaborador federativo | NN | null | null | null |
| Colaborador de singular | NN | esperado | esperado | opcional |

### 4.3 Validação de coerência (FATO)

`ColaboradorDomainService.resolveOrganizationalLinks()` (`backend/.../ColaboradorDomainService.java` L77–116):

- Resolve equipe → área; área → singular.
- Rejeita equipe fora da área informada; área fora da singular.
- **Não valida** `federacaoId` contra cadeia singular.
- Retorna `OrganizationalContext(singularId, areaId, teamId)` — **sem federação**.

`locateOrCreate` (FT-AUTH): define apenas `federacaoId = defaultFederationId`; demais FKs org **null**.

### 4.4 Cardinalidade AS-IS (FATO)

| Perspectiva | Modelo |
|-------------|--------|
| Físico | **1 linha** `COLABORADOR` = **1 conjunto** de FKs org |
| Lógico | `02-logical-model.md` — FKs opcionais diretas em `COLABORADOR` |
| Specs TO-BE | N vínculos — **GAP** |

**Classificação:** **CONFLICT** com DEC-FA-003 / BR-041 (INC-PA-001).

### 4.5 CHECK de hierarquia no Oracle (FATO)

Não existe CHECK em `COLABORADOR` impedindo combinações inconsistentes (ex.: `COD_SINGULAR=10`, `COD_AREA=20` de outra singular). Coerência é **aplicação** (`resolveOrganizationalLinks`).

---

## 5. AS-IS de FEDERAÇÃO / SINGULAR / ÁREA / EQUIPE

| Entidade | PK | FK ascendente | Observação |
|----------|-----|---------------|------------|
| `FEDERACAO` | `COD_FEDERACAO` | — | Raiz institucional (DEC-DB-021) |
| `SINGULAR` | `COD_SINGULAR` | `COD_FEDERACAO` NN | |
| `AREA` | `COD_AREA` | `COD_SINGULAR` **nullable** | Área pode existir sem singular no DDL |
| `EQUIPE` | `COD_EQUIPE` | `COD_AREA` NN | Sempre subordinada a uma área |
| `COLABORADOR` | `COD_COLABORADOR` | 4 FKs org + gestor | Denormalizado — flat link |

**FATO:** a cadeia derivável mínima para equipe é `EQUIPE → AREA → SINGULAR → FEDERACAO`.

**INFERÊNCIA:** para vínculo em **área sem equipe**, derivação é `AREA → SINGULAR → FEDERACAO`.

---

## 6. AS-IS da API

### 6.1 CRUD colaborador (FATO)

`specs/features/colaborador/api.md`: `federationId` (obrigatório create), `singularId?`, `areaId?`, `teamId?`, `managerId?`.

RN-005: contexto coerente singular→área; equipe na área.

### 6.2 Sessão / autenticação (FATO)

`GET /api/v1/auth/me` (`specs/features/authentication/api.md` L223–244):

```json
"organizationalLinks": {
  "federationId": 1,
  "singularId": 10,
  "areaId": 20,
  "teamId": null
}
```

- **Objeto único** — não array.
- `AuthenticationService.organizationalLinksFrom()` mapeia entidade → 4 campos.
- JWT embute os mesmos IDs (`JwtTokenService`).

### 6.3 TO-BE proposto (FATO — não implementado)

`specs/features/primeiro-acesso/api.md`:

- `GET /api/v1/session/contexts` → `contexts[]` (N vínculos).
- Evolução `/auth/me`: manter `organizationalLinks` fase 1 + `organizationalContexts[]` / `activeContext`.

**Classificação:** **GAP** implementação; **CONFLICT** INC-PA-003.

### 6.4 Forma canônica do vínculo nas specs (FATO)

Contexto Ativo / vínculo válido (`specification.md` FT-PRIMEIRO-ACESSO L140–148, RN-PA-001):

```text
federationId  (obrigatório)
singularId    (obrigatório)
areaId        (obrigatório)
teamId        (opcional)
```

**INFERÊNCIA:** o contrato de domínio trata o vínculo como **snapshot derivável** de área (+ equipe opcional), não como escolha livre de nível hierárquico.

---

## 7. AS-IS do frontend

| Artefato | Comportamento |
|----------|---------------|
| `auth/types.ts` | `ColaboradorOrganizationalLinks` — 4 campos |
| `session.store.ts` L50–86 | `organizationalLinks` único; `activeContext = organizationalLinks` |
| `useColaboradorForm.ts` | Form CRUD — um conjunto de FKs |
| Comentário L23 | Multi-contexto **fora do escopo** (legado OQ-027) |

**FATO:** FE pressupõe **1 colaborador → 1 singular → 1 área → 1 equipe** (equipe opcional).

**Classificação:** **GAP** vs DEC-FA-003.

---

## 8. AS-IS dos testes

| Teste | Modelo codificado |
|-------|-------------------|
| `session.store.spec.ts` | Vínculo completo único; `activeContext === organizationalLinks` |
| `auth.service.spec.ts` | `federationId` only (demais null) |
| `auth.guard.spec.ts` | Vínculo único mock |
| `ColaboradorAcceptanceIntegrationTest` | CRUD com hierarquia; rejeita equipe inválida |
| `ColaboradorDomainServiceTest` | Gestor/inativação — **sem** testes de `resolveOrganizationalLinks` |

**FATO:** nenhum teste implementa N vínculos ou seleção de contexto.

---

## 9. PD-02 — análise de N vínculos

### 9.1 Pergunta

Como representar `COLABORADOR 1 → N VÍNCULOS`?

### 9.2 Alternativas avaliadas

#### Alt-1 — Manter FKs em `COLABORADOR`

| Critério | Avaliação |
|----------|-----------|
| Cardinalidade N | ❌ Uma linha = um conjunto |
| Múltiplas áreas | ❌ Impossível sem colunas repetidas ou JSON |
| Normalização | ❌ |
| Migração | Baixa curto prazo | **Rejeitada** |

#### Alt-2 — `VINCULO_ORGANIZACIONAL` (tabela de associação)

| Critério | Avaliação |
|----------|-----------|
| Cardinalidade N | ✅ 1 linha por vínculo |
| Normalização | ✅ |
| Alinhamento specs | ✅ `contexts[]` |
| Oracle/JPA | Padrão projeto (similar `PAPEL_ATRIBUICAO`) |
| Migração | Média — migrar FKs atuais para N linhas |
| Risco | Baixo-médio | **Recomendada** |

#### Alt-3 — Estrutura existente no repo

**FATO:** não existe tabela, entidade JPA nem API de vínculo separada. `ONBOARDING_SOLICITACAO` é legado (DEC-FA-001 — sem mapeamento TO-BE).

### 9.3 Respostas objetivas PD-02

| # | Pergunta | Resposta | Classificação |
|---|----------|----------|---------------|
| 1 | Estrutura para N vínculos? | Tabela `VINCULO_ORGANIZACIONAL` (1:N com `COLABORADOR`) | **RECOMENDAÇÃO** |
| 2 | Entidade do vínculo? | `VINCULO_ORGANIZACIONAL` | **RECOMENDAÇÃO** |
| 3 | PK do vínculo? | `COD_VINCULO_ORGANIZACIONAL` + sequence `SQ_VINCULO_ORGANIZACIONAL` (padrão DEC-DB-018) | **RECOMENDAÇÃO** |
| 4 | Unidade folha? | Ver PD-03 (seção 10) | **RECOMENDAÇÃO** |
| 5 | FKs necessárias? | `COD_COLABORADOR`, `COD_AREA` (NN), `COD_EQUIPE` (NULL) | **RECOMENDAÇÃO** |
| 6 | FKs deriváveis? | `COD_SINGULAR`, `COD_FEDERACAO` via `AREA`/`SINGULAR` | **INFERÊNCIA** |
| 7 | Dois vínculos mesma área? | Sim, se equipes diferentes; **não** duplicar área sem equipe (UK) | **RECOMENDAÇÃO** + **DECISÃO PENDENTE** (UK) |
| 8 | Vínculos em áreas diferentes? | **Sim** — caso central DEC-FA-003 | **FATO** (domínio) |
| 9 | Várias equipes? | **Sim** — Cenário D; OQ-008 parcial | **INFERÊNCIA** |
| 10 | Ausência de equipe? | `COD_EQUIPE IS NULL` | **RECOMENDAÇÃO** |
| 11 | Integridade? | FK + validação domínio (equipe ∈ área) | **RECOMENDAÇÃO** |
| 12 | Substituir ou evoluir? | **Substituir** semântica das FKs org em `COLABORADOR` por tabela de vínculo | **RECOMENDAÇÃO** |

---

## 10. PD-03 — análise da unidade folha

### 10.1 Alternativas

| Alt | Modelo | Cenário A (só área) | Cenário B (equipe) | Cenário C (N vínculos) | Cenário D (2 equipes/área) | Veredito |
|-----|--------|---------------------|--------------------|-----------------------|---------------------------|----------|
| **A** | Vínculo → `AREA` only | ✅ | ⚠️ perde granularidade equipe | ✅ | ❌ | Insuficiente |
| **B** | Vínculo → `EQUIPE` only | ❌ | ✅ | ⚠️ | ✅ | **Rejeitada** — viola BR-010/RN-PA-001 |
| **C** | `AREA` **OU** `EQUIPE` | ✅ | ✅ | ⚠️ ambiguidade | ✅ | **Rejeitada** — integridade/consulta complexas |
| **D** | `AREA` NN + `EQUIPE` NULL | ✅ | ✅ | ✅ | ✅ | **Recomendada** |

### 10.2 Alternativa D — detalhe conceitual

```text
VINCULO_ORGANIZACIONAL
    COD_COLABORADOR  → COLABORADOR
    COD_AREA         → AREA        (NOT NULL)  ← unidade folha base
    COD_EQUIPE       → EQUIPE      (NULL)      ← refinamento opcional

Derivação:
    AREA.COD_SINGULAR → SINGULAR.COD_FEDERACAO
```

Snapshot de Contexto Ativo (API):

```text
federationId  ← derivado
singularId    ← derivado (AREA → SINGULAR)
areaId        ← COD_AREA
teamId        ← COD_EQUIPE (ou null)
```

### 10.3 Respostas objetivas PD-03

| # | Pergunta | Resposta | Classificação |
|---|----------|----------|---------------|
| 1 | Unidade folha = Federação? | **Não** — derivada | **INFERÊNCIA** |
| 2 | Singular? | **Não** — derivada via área | **INFERÊNCIA** |
| 3 | Área? | **Sim** — âncora obrigatória | **RECOMENDAÇÃO** |
| 4 | Equipe? | **Opcional** — folha refinada quando informada | **RECOMENDAÇÃO** |
| 5 | Área + equipe opcional? | **Sim** — melhor aderência ao domínio | **RECOMENDAÇÃO** |
| 6 | Evidência para Área OU Equipe exclusivo? | **Não** — specs exigem área sempre | **FATO** |
| 7 | Melhor aderência domínio? | Alternativa D | **RECOMENDAÇÃO** |
| 8 | Menor redundância? | D — não duplicar `COD_SINGULAR`/`COD_FEDERACAO` no vínculo | **RECOMENDAÇÃO** |
| 9 | Autorização futura? | D — deriva todos os níveis para `PAPEL_ATRIBUICAO` | **INFERÊNCIA** |
| 10 | Integração Oracle/JPA? | D — espelha `resolveOrganizationalLinks` existente | **RECOMENDAÇÃO** |

### 10.4 Validação dos cenários obrigatórios

#### Cenário A — apenas Área

```text
VINCULO: COD_AREA=TI, COD_EQUIPE=NULL
→ federationId, singularId derivados
```

**RECOMENDAÇÃO:** suportado.

#### Cenário B — Equipe

```text
VINCULO: COD_AREA=TI, COD_EQUIPE=Desenvolvimento
(validar EQUIPE.COD_AREA = COD_AREA)
```

**RECOMENDAÇÃO:** suportado.

#### Cenário C — múltiplos vínculos

```text
Linha 1: AREA TI + EQUIPE Desenvolvimento
Linha 2: AREA Financeiro + COD_EQUIPE NULL
```

**RECOMENDAÇÃO:** suportado; cargo único em `COLABORADOR.COD_CARGO` (DEC-DB-027).

#### Cenário D — duas equipes na mesma área

```text
Linha 1: AREA TI + EQUIPE Desenvolvimento
Linha 2: AREA TI + EQUIPE Infraestrutura
```

**INFERÊNCIA:** suportável; **DECISÃO PENDENTE** confirmação de negócio (OQ-008).

---

## 11. Alternativas de modelagem — matriz consolidada

| Alternativa | Domínio | N vínculos | Normalização | Oracle | JPA | API | FE | Authz futura | Migração | Risco |
|-------------|---------|------------|--------------|--------|-----|-----|-----|--------------|----------|-------|
| FKs em `COLABORADOR` | ❌ | ❌ | ❌ | Simples | Atual | Atual | Atual | ❌ | N/A | **Alto** — bloqueia DEC-FA-003 |
| `VINCULO` + `COD_AREA` only | ⚠️ | ✅ | ✅ | Médio | Novo | Novo | Novo | ✅ | Média | Médio — perde equipe |
| `VINCULO` + `COD_AREA` + `COD_EQUIPE` opt | ✅ | ✅ | ✅ | Médio | Novo | Novo | Novo | ✅ | Média | **Baixo** |
| `VINCULO` AREA XOR EQUIPE | ⚠️ | ✅ | ⚠️ | Médio | Complexo | Complexo | Complexo | ⚠️ | Alta | **Alto** |

---

## 12. Normalização

### 12.1 Princípio (RECOMENDAÇÃO)

> Não duplicar no vínculo informação determinável pela cadeia `EQUIPE → AREA → SINGULAR → FEDERACAO`.

### 12.2 FKs no vínculo TO-BE recomendado

| FK no vínculo | Necessária? | Motivo |
|---------------|-------------|--------|
| `COD_COLABORADOR` | **Sim** | Dono do vínculo |
| `COD_AREA` | **Sim** | Unidade folha base; BR-010 |
| `COD_EQUIPE` | **Opcional** | Granularidade operacional |
| `COD_SINGULAR` | **Não** | Derivável `AREA.COD_SINGULAR` |
| `COD_FEDERACAO` | **Não** | Derivável `SINGULAR.COD_FEDERACAO` |

### 12.3 FKs atuais em `COLABORADOR` (futuro)

| Coluna AS-IS | TO-BE recomendado |
|--------------|-------------------|
| `COD_SINGULAR` | **Migrar** para `VINCULO_ORGANIZACIONAL`; remover após migration |
| `COD_AREA` | Idem |
| `COD_EQUIPE` | Idem |
| `COD_FEDERACAO` | Ver PD-12 — manter provisoriamente como identidade |

**RECOMENDAÇÃO:** não usar `COLABORADOR` como armazenamento de N vínculos.

---

## 13. Integridade referencial

### 13.1 Regras TO-BE recomendadas

| Regra | Mecanismo |
|-------|-----------|
| Área existe e ativa | FK + domínio |
| Equipe existe e ativa | FK + domínio |
| Equipe pertence à área do vínculo | Domínio (`resolveOrganizationalLinks` pattern) |
| Singular/federação coerentes | **Derivados** — elimina `COD_SINGULAR` inconsistente no vínculo |
| Duplicata área sem equipe | UK `(COD_COLABORADOR, COD_AREA)` WHERE `COD_EQUIPE IS NULL` — **DECISÃO PENDENTE** |
| Duplicata equipe | UK `(COD_COLABORADOR, COD_EQUIPE)` WHERE `COD_EQUIPE IS NOT NULL` — **RECOMENDAÇÃO** |

### 13.2 Inconsistência AS-IS exemplificada

```text
COLABORADOR.COD_SINGULAR = 10
COLABORADOR.COD_AREA = 20   -- AREA 20 pertence à SINGULAR 15
```

**FATO:** possível no Oracle hoje; mitigado parcialmente na aplicação.

**INFERÊNCIA:** modelo TO-BE com `COD_AREA` único no vínculo **reduz** esse risco.

---

## 14. Impacto em autorização

### 14.1 Separação vínculo × papel (FATO)

- Vínculo: onde o colaborador opera (`VINCULO_ORGANIZACIONAL` TO-BE).
- Autorização: `PAPEL_ATRIBUICAO` com escopo `COD_FEDERACAO` / `COD_SINGULAR` / `COD_AREA` / `COD_EQUIPE`.

**FATO:** não inferir `ADMIN_*` a partir do vínculo.

### 14.2 Suporte futuro `ADMIN_*` (INFERÊNCIA)

Com Alternativa D, cada vínculo permite derivar:

| Papel TO-BE | Escopo derivável do vínculo |
|-------------|----------------------------|
| `ADMIN_FEDERACAO` | `FEDERACAO` via cadeia |
| `ADMIN_SINGULAR` | `SINGULAR` via área |
| `ADMIN_AREA` | `COD_AREA` |
| `ADMIN_EQUIPE` | `COD_EQUIPE` quando não null |

**INFERÊNCIA:** modelo suficiente para autorização contextual futura (PD-08/09/10 permanecem abertos).

---

## 15. Impacto em CARGO

| Aspecto | Impacto |
|---------|---------|
| DEC-DB-027 | **Nenhuma alteração** nesta etapa |
| `COLABORADOR.COD_CARGO` | Permanece no colaborador (1:1) — **ortogonal** aos N vínculos |
| `VINCULO_ORGANIZACIONAL.COD_CARGO` | **Não criar** (DEC-DB-027) |
| Cenários A–D | Um cargo; N vínculos — **compatível** |

**FATO:** implementação de CARGO (DEC-DB-027) e de vínculo N são eixos independentes, mas **PKG-FE-02** depende de ambos estabilizados.

---

## 16. Impacto em COD_GESTOR / COD_LIDER

| FK | Relação com vínculo | Decisão nesta etapa |
|----|---------------------|---------------------|
| `COLABORADOR.COD_GESTOR` | Reporting line — não é vínculo | **Sem alteração** (PD-04) |
| `AREA.COD_GESTOR` | Responsável área — não é vínculo | **Sem alteração** (PD-05) |
| `EQUIPE.COD_LIDER` | Líder equipe — não é vínculo | **Sem alteração** (PD-06/07) |

---

## 17. PD-12 — COD_FEDERACAO

### 17.1 AS-IS (FATO)

| Aspecto | Evidência |
|---------|-----------|
| Existe | `COD_FEDERACAO NOT NULL` em DDL |
| Significado documentado | Identidade institucional / pertencimento à federação |
| Uso FT-AUTH | `locateOrCreate` — único FK org preenchido |
| Uso operacional | Exposto em `organizationalLinks` mesmo sem área |
| Conflito N vínculos | Colaborador com vínculos em singulares da **mesma** federação: redundante; federações diferentes: **não evidenciado** no domínio (DEC-DB-021 — federação única) |

### 17.2 Análise

| Pergunta | Resposta | Classificação |
|----------|----------|---------------|
| Representa vínculo? | **Parcialmente** no AS-IS (único conjunto FK) | **INFERÊNCIA** |
| Representa identidade? | **Sim** — default federation no login | **FATO** |
| Redundante com vínculos? | Operacionalmente sim, se todos os vínculos ∈ mesma federação | **INFERÊNCIA** |
| Derivável? | Sim, via qualquer vínculo válido | **INFERÊNCIA** |
| Conflita com N vínculos? | **Não** se federação única (DEC-DB-021) | **INFERÊNCIA** |
| Remover? | **Não** nesta etapa | **FATO** (instrução) |

### 17.3 Veredito PD-12

**DECISÃO PENDENTE** — recomendação preliminar:

- **Manter** `COLABORADOR.COD_FEDERACAO` como **âncora de identidade institucional** (login, FT-AUTH).
- **Não** replicar em `VINCULO_ORGANIZACIONAL`.
- Contexto Ativo deriva `federationId` do vínculo selecionado.

Formalização requer DEC própria ou extensão de DEC-DB-021.

---

## 18. Matriz AS-IS × TO-BE

| Dimensão | AS-IS | TO-BE recomendado |
|----------|-------|-------------------|
| Cardinalidade | 1 conjunto FK/row | N linhas `VINCULO_ORGANIZACIONAL` |
| Unidade folha | Implícita (área e/ou equipe nas colunas) | `COD_AREA` + `COD_EQUIPE` opcional |
| `COD_FEDERACAO` em colaborador | NN | NN (identidade) — PD-12 pendente |
| `COD_SINGULAR/AREA/EQUIPE` em colaborador | Nullable | **Deprecar** → vínculo |
| API `/auth/me` | Objeto único | `contexts[]` + `activeContext` (specs) |
| FE session | 1 contexto | Seleção quando N>1 |
| Cargo | Inexistente | `COLABORADOR.COD_CARGO` (DEC-DB-027) |
| Autorização | `PAPEL_ATRIBUICAO` separado | Sem mudança |
| Integridade cadeia | App-only | FK + domínio no vínculo |

---

## 19. Conflitos e gaps

| ID | Descrição | Classificação |
|----|-----------|---------------|
| INC-PA-001 | 1 vínculo AS-IS vs BR-041 N vínculos | **CONFLICT** |
| INC-PA-003 | `/auth/me` objeto único vs lista N | **CONFLICT** |
| INC-PA-002 | (se aplicável) JWT single context | **GAP** |
| Modelo lógico | Sem entidade vínculo | **GAP** |
| `ColaboradorDomainService` | Não cobre federação | **GAP** |
| OQ-008 | N equipes mesma área | **DECISÃO PENDENTE** |
| OQ-009 | Alteração de vínculo pós-integração | **DECISÃO PENDENTE** — fora PD-02/03 |
| DEC-DB-027 vs briefing etapa | `DSC_CARGO` / `FLG_ATIVO` | **DRIFT** documental — não reaberto |

---

## 20. Recomendação

### 20.1 PD-02 — estrutura

**RECOMENDAÇÃO:** adotar tabela **`VINCULO_ORGANIZACIONAL`** com:

```text
VINCULO_ORGANIZACIONAL (conceitual)
  COD_VINCULO_ORGANIZACIONAL  PK
  COD_COLABORADOR             FK → COLABORADOR  NOT NULL
  COD_AREA                    FK → AREA         NOT NULL
  COD_EQUIPE                  FK → EQUIPE       NULL
  FLG_ATIVO                   NOT NULL DEFAULT 'S'   (padrão catálogo)
  DAT_CADASTRO                NOT NULL
  DAT_ATUALIZACAO             NULL
```

Migrar dados AS-IS: cada `COLABORADOR` com `COD_AREA` preenchida → 1 linha de vínculo; demais FKs org em `COLABORADOR` **deprecar** em etapa futura.

### 20.2 PD-03 — unidade folha

**RECOMENDAÇÃO:** **Alternativa D** — unidade folha = **`AREA`** com **`EQUIPE` opcional**.

- Não usar Alternativa B (só equipe) — viola BR-010.
- Não usar Alternativa C (área XOR equipe) — sem evidência; complexidade desnecessária.

### 20.3 Modelo integrado TO-BE (referência)

```text
COLABORADOR
  ├── COD_CARGO → CARGO           (1:1 — DEC-DB-027)
  ├── COD_FEDERACAO               (identidade — PD-12)
  ├── COD_GESTOR                  (legado — PD-04)
  └── N × VINCULO_ORGANIZACIONAL
         ├── COD_AREA (NN)
         └── COD_EQUIPE (NULL)

Contexto Ativo (sessão) = 1 vínculo selecionado → snapshot derivado
PAPEL_ATRIBUICAO = autorização independente
```

---

## 21. Decisões que ainda exigem aprovação

| ID | Questão | Por que permanece aberta |
|----|---------|--------------------------|
| **PD-02** | Aprovar tabela `VINCULO_ORGANIZACIONAL` vs alternativa | Recomendação pronta; falta DEC formal (ex. DEC-DB-028) |
| **PD-03** | Confirmar Área + Equipe opcional | Recomendação alinhada a specs; requer ratificação |
| **PD-12** | `COD_FEDERACAO` permanece em `COLABORADOR`? | Identidade vs vínculo — análise inconclusiva para DEC |
| **OQ-008** | Múltiplas equipes na mesma área | Cenário D suportável; sem BR explícita |
| **UK vínculo** | Unicidade `(colaborador, área)` sem equipe | Regra de negócio não documentada |
| **Deprecação FKs** | Quando remover `COD_SINGULAR/AREA/EQUIPE` de `COLABORADOR` | Depende de PD-02 aprovado + plano migration |

**Não transformar recomendação em decisão** — aguardar responsável do projeto.

---

## 22. Impactos futuros (sem implementação)

| Artefato | Impacto |
|----------|---------|
| **Oracle / DDL** | `CREATE TABLE VINCULO_ORGANIZACIONAL`; sequence; FKs; índices; UKs; migration AS-IS → N linhas |
| **Modelo lógico/físico** | Nova entidade; deprecar FKs org em `COLABORADOR` na documentação |
| **JPA** | `VinculoOrganizacionalEntity`; remover campos org de `ColaboradorEntity` (fase 2) |
| **Backend** | `resolveOrganizationalLinks` → validar vínculos; listar N para sessão |
| **API** | `contexts[]`; CRUD colaborador com lista de vínculos; evolução `/auth/me` |
| **Frontend** | Form multi-vínculo; UI seleção contexto (FT-PRIMEIRO-ACESSO) |
| **Testes** | N vínculos; seleção contexto; integridade equipe∈área |
| **FT-PRIMEIRO-ACESSO** | Desbloqueia implementação real de RN-PA-003 |
| **FT-COLABORADOR** | Contrato passa a gerenciar vínculos |
| **Autorização** | Sem alteração em `PAPEL_ATRIBUICAO` nesta etapa |
| **DEC-DB-027 / CARGO** | Implementação paralela possível após DEC vínculo |
| **PKG-FE-02** | Continua bloqueado até DEC formal vínculo + contrato API |

---

## 23. Critério para implementação posterior

Implementação física do vínculo N **somente após**:

1. Aprovação formal de PD-02/PD-03 (DEC de banco, ex. **DEC-DB-028**).
2. Resolução ou aceite explícito de PD-12 e OQ-008.
3. Atualização de specs FT-COLABORADOR e FT-SESSION com contrato N vínculos.
4. Plano de migration DBA (baseline + `database/migrations/`).
5. DEC-DB-027 pode prosseguir em paralelo **após** ou **com** DEC vínculo — ordem sugerida: **vínculo primeiro** (bloqueia FT-PRIMEIRO-ACESSO e contrato colaborador).

---

## 24. Checklist de conclusão (§32)

| Pergunta | Resposta | Classificação |
|----------|----------|---------------|
| Como representar N vínculos? | Tabela `VINCULO_ORGANIZACIONAL` | RECOMENDAÇÃO |
| Unidade folha? | `COD_AREA` + `COD_EQUIPE` opcional | RECOMENDAÇÃO |
| Colaborador só em Área? | `COD_EQUIPE NULL` | RECOMENDAÇÃO |
| Colaborador em Equipe? | `COD_EQUIPE` + `COD_AREA` coerente | RECOMENDAÇÃO |
| Múltiplos vínculos? | N linhas | RECOMENDAÇÃO |
| Derivar singular/federação? | Via `AREA` → `SINGULAR` → `FEDERACAO` | INFERÊNCIA |
| FKs necessárias? | `COD_COLABORADOR`, `COD_AREA`, `COD_EQUIPE?` | RECOMENDAÇÃO |
| Evitar redundância? | Não armazenar singular/federação no vínculo | RECOMENDAÇÃO |
| Integridade? | FK + domínio equipe∈área | RECOMENDAÇÃO |
| Autorização futura? | Escopos deriváveis do vínculo | INFERÊNCIA |
| Convive com CARGO? | Sim — eixos ortogonais | FATO |
| FKs atuais COLABORADOR? | Deprecar após migration | RECOMENDAÇÃO |
| `COD_FEDERACAO`? | PD-12 aberta | DECISÃO PENDENTE |
| Decisões abertas? | PD-02, PD-03, PD-12, OQ-008, UKs | DECISÃO PENDENTE |

```text
ETAPA 8 v1.0 — CONCLUÍDA (análise)
Próximo passo: aprovação humana → DEC-DB-028 (modelo físico vínculo)
```

---

## Referências

- `docs/governance/03-open-decisions.md` (DEC-FA-003, DEC-ORG-001, DEC-DB-027)
- `database/model/05-decisions-and-risks.md`
- `specs/features/primeiro-acesso/specification.md`, `api.md`, `traceability.md`
- `specs/features/session/specification.md`
- `specs/features/colaborador/specification.md`
- `construction/review/organizational-authorization-formalization-etapa6.md`
- `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md`
