# Investigação — Contexto Ativo × DH-02

| Campo | Valor |
|-------|-------|
| Artefato | `construction/review/contexto-ativo-dh02-investigacao.md` |
| Data | 2026-08-17 |
| Tipo | Investigação read-only |
| Escopo | Necessidade normativa/funcional de Contexto Ativo separado do vínculo cadastral |
| Restrição | Nenhuma alteração no repositório além deste relatório |

**Decisões vigentes assumidas (não reabertas):** DH-02, DH-03, DH-04, DH-CARGO-01.

---

## 1. Onde "Contexto Ativo" é definido ou utilizado

### Definição normativa

| Fonte | Definição / uso |
|-------|-----------------|
| `docs/domain/02-business-glossary.md` | Vínculo organizacional **em vigor na sessão** (`federationId`, `singularId`, `areaId`); base da navegação operacional |
| `docs/governance/03-open-decisions.md` — DEC-FA-003 P2/P3 | Contexto Ativo mínimo na sessão; toda navegação operacional o utiliza (**MANTIDO** após DH-02) |
| `docs/domain/09-business-rules.md` — BR-010, BR-012, BR-041 | Navegação no Contexto Ativo; supersession parcial de BR-041: **1 vínculo cadastral**; Contexto Ativo único **mantido** |
| `specs/features/primeiro-acesso/specification.md` §6 | Modelo conceitual: `federationId`, `singularId`, `areaId`, `teamId` opcional — **mesmos campos do vínculo em `COLABORADOR`** |
| `specs/features/session/specification.md` — RN-SESSION-004/005 | Refletido em sessão de aplicação; mínimo `federationId` + `singularId` + `areaId` |
| `database/model/05-decisions-and-risks.md` — DEC-DB-028 | Vínculo único nas FKs escalares; **não** define entidade Contexto Ativo separada |

### Implementação

| Local | Comportamento |
|-------|---------------|
| `frontend/src/stores/session.store.ts` | `activeContext` e `availableContext` separados de `organizationalLinks` no modelo de store; na prática `activeContext := organizationalLinks` ao hidratar |
| `backend/.../AuthenticationService.organizationalLinksFrom()` | Expõe FKs de `ColaboradorEntity` em `/auth/me` |
| `AuthSessaoEntity` | **Sem** `COD_*_CTX` (REF-DB-CTX-01) |

**FATO:** Não existe tabela, coluna dedicada nem entidade JPA de Contexto Ativo. O único vínculo cadastral está nas FKs de `COLABORADOR`.

---

## 2. Qual problema funcional deveria resolver

### Problema original (pré-DH-02)

DEC-FA-003 (2026-07-24) introduziu Contexto Ativo para resolver:

- Colaborador com **N vínculos cadastrais** (ex.: Área A e Área B).
- Sessão com **um** contexto operacional selecionado entre N.
- Navegação, Home e escopo documental sempre no contexto escolhido.

**Evidência:** `docs/governance/03-open-decisions.md` — DEC-FA-003 §Decisão.1–4; RN-SESSION-003; RN-PA-003.

### Problema após DH-02

Com **1 vínculo cadastral** por COLABORADOR (DH-02, DEC-DB-028), o problema de **escolha entre N vínculos** deixa de existir no eixo cadastral:

| Item DEC-FA-003 | Status pós-DH-02 |
|-----------------|------------------|
| P1 — N vínculos | **SUPERSEDED** |
| P4 — seleção quando N>1 | **SUPERSEDED** |
| P2 — Contexto Ativo na sessão | **MANTIDO** |
| P3 — navegação no Contexto Ativo | **MANTIDO** |

**FATO:** A governança mantém o **conceito** Contexto Ativo (P2/P3), mas remove a razão funcional de **estado separado** do vínculo: não há conjunto de vínculos para escolher.

### Problemas que permanecem (sem exigir estado separado)

| Problema | Resolução sob DH-02 |
|----------|---------------------|
| Bloquear operação sem vínculo completo (BR-010, DEC-FA-002) | Gate até COLABORADOR com vínculo mínimo (DH-03/DH-04) ou credencial temporária (DH-PA-01) |
| Onboarding cria vínculo antes de operar (BR-011, DH-03) | Wizard PA → INSERT `COLABORADOR` com FKs completas |
| Home dinâmica (DEC-FA-004, BR-042) | Backend usa escopo organizacional — derivável das FKs |
| Navegação sempre em escopo coerente (BR-012) | FKs do único vínculo |

**INFERÊNCIA:** Contexto Ativo, após DH-02, funciona como **projeção semântica do único vínculo cadastral** para a camada de sessão/navegação — não como seleção entre alternativas cadastrais.

---

## 3. Cenário legítimo: operar em contexto diferente do vínculo único?

**FATO:** Sob DH-02, o vínculo cadastral é exatamente `Federação + Singular + Área (+ Equipe opcional)` nas FKs de `COLABORADOR`.

**FATO:** O modelo conceitual de Contexto Ativo (`specification.md` §6) usa **os mesmos campos**.

### Cenários analisados

| Cenário | Previsto nas decisões? | Exige Contexto Ativo ≠ vínculo? |
|---------|------------------------|----------------------------------|
| Seleção entre N vínculos cadastrais | Era DEC-FA-003 P1/P4 — **superseded** por DH-02 | Não aplicável |
| Troca de Contexto Ativo em sessão (RF-PA-007, RN-PA-006) | Spec PA ainda lista como Must | **Não** — com 1 vínculo não há outro contexto cadastral para selecionar |
| Múltiplas equipes no mesmo vínculo | Não — `COD_EQUIPE` único nullable (DH-04) | Não |
| Papéis/autorização em múltiplos escopos (`PAPEL_ATRIBUICAO`) | DEC-DB-020, DEC-DB-028 item 8 — **ortogonal** ao vínculo | **Não** — escopo de autorização ≠ Contexto Ativo de navegação (BR-027/BR-034) |
| Administra federação/singular/área fora do próprio vínculo | BR-034 — papel administrativo por escopo | **Não** — é autorização, não segundo vínculo cadastral |
| Identidade autenticada sem COLABORADOR (PA em andamento) | DH-03, DH-PA-01 — estado **transitório** | **Não** — ausência de vínculo, não contexto alternativo |
| Reentrada com contexto persistido (RN-PA-007) | Spec PA | **Não** — com 1 vínculo, reentrada lê FKs de `COLABORADOR` |

**Conclusão parcial:** Nenhum cenário **legítimo e vigente** prevê COLABORADOR com 1 vínculo cadastral operando em contexto organizacional **diferente** desse vínculo.

---

## 4. Múltiplos vínculos, equipes, autorização — criam a necessidade?

| Mecanismo | Estado | Impacto em Contexto Ativo separado |
|-----------|--------|-----------------------------------|
| N vínculos cadastrais | **Superseded** (DH-02, DEC-DB-028) | Eliminava a principal razão de estado separado |
| N equipes no vínculo | **Não previsto** — uma `COD_EQUIPE` opcional | Não cria necessidade |
| `PAPEL_ATRIBUICAO` (1..N papéis/escopos) | DDL existe; backend não consome | Eixo **autorização**, explicitamente separado do vínculo (DEC-DB-020, DEC-DB-028 §8) |
| `OrganizationAuthorizationService` | Whitelist incremental | Não materializa Contexto Ativo alternativo |

**FATO:** `construction/review/vinculo-organizacional-reconciliation-cardinality-reassessment.md` registra que DEC-FA-003 conflita com 1:1 no eixo **N pertinências**, **não** no ponto Contexto Ativo; com 1 vínculo, "contexto ativo = o vínculo".

---

## 5. O contexto pode ser integralmente derivado do único vínculo cadastral?

**Sim**, para todo COLABORADOR persistido sob DH-03/DH-04:

```text
Contexto Ativo ≡ {
  federationId: COLABORADOR.COD_FEDERACAO,
  singularId:   COLABORADOR.COD_SINGULAR,
  areaId:       COLABORADOR.COD_AREA,
  teamId:       COLABORADOR.COD_EQUIPE  // opcional
}
```

**Evidências de alinhamento normativo:**

- DEC-DB-028 §Modelo normativo — mesmos campos.
- BR-041 supersession — "Contexto Ativo único **mantido**" com 1 vínculo.
- DEC-FA-003 supersession — `organizationalLinks` singular **MANTIDO**, compatível com 1:1.
- Implementação AS-IS — `session.store.ts` já deriva `activeContext` de `organizationalLinks`.

**Exceção transitória (não é estado separado):** durante PA, antes do INSERT do COLABORADOR, não há vínculo cadastral — há **rascunho de onboarding** (Singular/Área/Equipe selecionados). Isso é entrada para criação do vínculo (DH-03), não Contexto Ativo operacional paralelo.

---

## 6. Decisão humana vigente que exija persistência de Contexto Ativo?

| Decisão | Exige persistência? | Exige persistência **separada** do vínculo? |
|---------|---------------------|---------------------------------------------|
| **DEC-FA-003 P5** (MANTIDO) | **Sim** — fora de `AUTH_SESSAO` (REF-DB-CTX-01) | **Não explicitamente** — proíbe colunas em `AUTH_SESSAO`; não proíbe usar FKs de `COLABORADOR` |
| **RN-PA-005** | Não usar `AUTH_SESSAO.COD_*_CTX` | Compatível com persistência no vínculo |
| **RF-PA-004 / RN-PA-007** | "Persistir" / "reentrada com contexto persistido" | Com 1 vínculo, satisfeito pela persistência do `COLABORADOR` |
| **RF-PA-007** | "Alterar Contexto Ativo em sessão" | **Órfão** pós-DH-02 — sem segundo vínculo cadastral; não cria requisito de estado separado |

**FATO:** A decisão vigente P5 exige **algum** mecanismo de persistência do Contexto Ativo fora de `AUTH_SESSAO`. As FKs escalares de `COLABORADOR` (DEC-DB-028) **são** persistência do vínculo e, com DH-02, **são** o Contexto Ativo.

**FATO:** INC-PA-004 (mecanismo físico indefinido) permanece aberto em `traceability.md`, mas a lacuna é **de especificação de implementação**, não de necessidade de entidade separada — resolvível deterministicamente como "derivar de `COLABORADOR`" sem nova decisão de negócio.

### Artefatos pré-DH-02 ainda não reconciliados (não criam requisito novo)

- RF-PA-003, RF-PA-007, RN-PA-003, RN-PA-006, RN-SESSION-003 — pressupõem N vínculos ou troca de contexto.
- `docs/domain/01-vision.md`, `04-domain-concepts.md` — ainda mencionam N áreas em trechos históricos.
- `specs/features/session/specification.md` §Multi-contexto — texto N vínculos não atualizado.

**Classificação:** **divergência documental** frente a DH-02; não evidência de cenário funcional que exija estado separado.

---

## Síntese

| Pergunta | Resposta |
|----------|----------|
| Contexto Ativo ainda é conceito normativo? | **Sim** (DEC-FA-003 P2/P3, BR-010, BR-012) |
| Precisa ser estado **separado** do vínculo cadastral? | **Não**, sob DH-02 |
| Persistência obrigatória? | **Sim** (P5), satisfeita pelas FKs de `COLABORADOR` |
| PAPEL/autorização criam segundo contexto? | **Não** — eixo ortogonal |
| Decisão humana pendente sobre necessidade? | **Não** |

---

CONCLUSÃO:
A

DECISÃO HUMANA NECESSÁRIA:
NÃO

JUSTIFICATIVA:
DH-02 estabelece exatamente 1 vínculo cadastral por COLABORADOR com os mesmos campos do modelo conceitual de Contexto Ativo (`federationId`, `singularId`, `areaId`, `teamId` opcional). A supersession parcial de DEC-FA-003 (P1, P4) remove a única razão funcional de estado separado — seleção entre N vínculos — mantendo P2/P3 apenas como terminologia de navegação. DEC-DB-020 e DEC-DB-028 §8 separam explicitamente vínculo cadastral de `PAPEL_ATRIBUICAO`/autorização; múltiplos escopos de papel não constituem Contexto Ativo alternativo. DEC-FA-003 P5 exige persistência fora de `AUTH_SESSAO`, não exige store independente: as FKs de `COLABORADOR` cumprem esse papel. RF-PA-007 e RN-PA-006 permanecem em specs pré-reconciliação como requisitos órfãos, resolvíveis deterministicamente pela supersession já registrada em governança, sem novo cenário de negócio. O frontend já deriva `activeContext` de `organizationalLinks`, corroborando equivalência operacional.
