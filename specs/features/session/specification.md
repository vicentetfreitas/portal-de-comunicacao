# Specification — FT-SESSION

| Item | Valor |
|------|-------|
| Feature | FT-SESSION |
| Status | APPROVED (evolução documental 2026-07-24; reconciliado 2026-08-17; evolução PAPEL_ATRIBUICAO 2026-08-20) |
| Decisões | DEC-FA-002, DEC-FA-003, DEC-ORG-001, DEC-DB-020, DH-02; REF-DB-CTX-01 |

## Objetivo

Após autenticação FT-AUTH, expor e manter o **estado de sessão** do colaborador — vínculo organizacional (Contexto Ativo, projeção derivada) e **contexto operacional** (atribuição de papel ativa, `PAPEL_ATRIBUICAO`) — para consumo do Portal.

A **condução do Primeiro Acesso** e o **encerramento do onboarding** pertencem a **FT-PRIMEIRO-ACESSO**.

**Nota de rastreabilidade (2026-08-20):** a seção "Contexto operacional por atribuição de papel" abaixo documenta uma evolução implementada diretamente nesta especificação de Feature, sem novo `DEC-XXX`/`DH-XXX` dedicado — apenas `DEC-DB-020` (`PAPEL_ATRIBUICAO` ortogonal ao vínculo) já aprovado é referenciado. Se este projeto exigir formalização de decisão para mudanças de comportamento de sessão, isso permanece pendente de registro em `docs/governance/03-open-decisions.md`.

## Hierarquia organizacional (DEC-ORG-001)

```text
Federação → Singular → Área → Equipe → Colaborador
```

Colaborador operacional exige Área. Sem vínculo válido não há operação (BR-010).

## Contexto Ativo × vínculo cadastral (DH-02)

Com **1 vínculo cadastral** por COLABORADOR (DH-02, DEC-DB-028), o Contexto Ativo **não** constitui estado cadastral independente:

```text
COLABORADOR
    ↓
único vínculo cadastral (FKs escalares)
    ↓
Contexto Ativo = projeção derivada para sessão/UI/navegação
```

- **Não** introduzir persistência separada de Contexto Ativo apenas para representar o vínculo.
- `AUTH_SESSAO` **não** persiste `COD_*_CTX` (REF-DB-CTX-01).
- O vínculo é persistido nas FKs de `COLABORADOR`; a store de sessão expõe `activeContext` como espelho derivado.

## Regras oficiais

- **RN-SESSION-001:** Vínculo derivado do colaborador autenticado (`federationId`, `singularId`, `areaId`, `teamId` opcional).
- **RN-SESSION-002:** Com COLABORADOR persistido e vínculo completo, o Contexto Ativo é **derivado automaticamente** do único vínculo (orquestrado por FT-PRIMEIRO-ACESSO na primeira entrada; FT-SESSION na hidratação).
- **RN-SESSION-003:** ~~Com múltiplos vínculos, o usuário escolhe o Contexto Ativo~~ — **SUPERSEDED** (DH-02, 2026-08-14). Não há seleção entre N vínculos cadastrais. *Parte histórica preservada abaixo.*
- **RN-SESSION-004:** O Contexto Ativo é refletido na sessão de aplicação e em contratos de identidade (`/auth/me`); `AUTH_SESSAO` **não** persiste `COD_*_CTX` (REF-DB-CTX-01).
- **RN-SESSION-005:** Contexto Ativo mínimo: `federationId`, `singularId`, `areaId`. Toda navegação operacional usa esse contexto.

### RN-SESSION-003 — texto histórico (superseded no eixo cadastral)

> Com **múltiplos** vínculos, o usuário escolhe o Contexto Ativo **após** autenticação e **antes** da Home (FT-PRIMEIRO-ACESSO).

**Status:** **SUPERSEDED** por DH-02 (1 vínculo cadastral). A regra de **navegação no Contexto Ativo** (RN-SESSION-005) permanece vigente.

## Multi-contexto (DEC-FA-003 — supersession parcial)

| Item DEC-FA-003 | Status após DH-02 |
|-----------------|-------------------|
| N vínculos cadastrais | **SUPERSEDED** |
| Contexto Ativo na sessão | **MANTIDO** — derivado do único vínculo |
| Navegação no Contexto Ativo | **MANTIDO** |
| Seleção quando N>1 (RN-SESSION-003) | **SUPERSEDED** |
| Sem `COD_*_CTX` em `AUTH_SESSAO` | **MANTIDO** |

Fonte: `docs/governance/03-open-decisions.md` — DEC-FA-003 § Supersession parcial.

## Contexto operacional por atribuição de papel (PAPEL_ATRIBUICAO)

Um COLABORADOR pode possuir múltiplas `PAPEL_ATRIBUICAO` (autorização), **ortogonais** ao vínculo cadastral único (DEC-DB-020): o vínculo (Federação/Singular/Área/Equipe) permanece 1:1 (DH-02); as atribuições de papel são 1:N, cada uma com escopo organizacional próprio.

```text
COLABORADOR
    ├── vínculo cadastral único (DH-02) — Contexto Ativo (federação/singular/área)
    └── N PAPEL_ATRIBUICAO (DEC-DB-020) — contexto operacional (papel ativo)
```

- **RN-SESSION-006:** Somente atribuições pertencentes ao colaborador autenticado, com `FLG_ATIVO = 'S'` e dentro da vigência (`DAT_INICIO_VIGENCIA` ≤ agora < `DAT_FIM_VIGENCIA` ou `DAT_FIM_VIGENCIA` nula) são elegíveis como contexto operacional.
- **RN-SESSION-007:** Com exatamente 1 atribuição elegível, o Portal seleciona automaticamente essa atribuição como contexto operacional ativo — sem ação do colaborador.
- **RN-SESSION-008:** Com mais de 1 atribuição elegível (ou 0), o Portal **não** seleciona automaticamente; o colaborador seleciona explicitamente via `POST /api/v1/auth/atribuicoes/{papelAtribuicaoId}/ativar`. A unidade selecionável é a atribuição (`PAPEL_ATRIBUICAO`), nunca o vínculo cadastral.
- **RN-SESSION-009:** A ativação/troca de atribuição é sempre revalidada no backend (pertencimento ao colaborador, `FLG_ATIVO`, vigência) — nunca aceita apenas pelo identificador enviado pelo cliente. Trocar de atribuição substitui o Access Token (contexto operacional) sem afetar a sessão (Refresh Token / `AUTH_SESSAO`) e sem exigir novo login.
- **RN-SESSION-010:** O Access Token representa a atribuição ativa pelo identificador estável de `PAPEL_ATRIBUICAO` (claim `atribId`) — nunca reutiliza os claims de vínculo (`fid`, `singularId`, `areaId`, `teamId`) para esse fim. Renovação (`/auth/refresh`) preserva a atribuição ativa enquanto ela permanecer elegível; caso contrário, reaplica RN-SESSION-007/008. Permissões nunca são incluídas no Access Token.

`GET /api/v1/auth/me` reflete esse contexto: `eligibleAssignments` (todas as atribuições elegíveis) e `activeAssignment` (a atribuição ativa revalidada contra o banco a cada chamada; ausente/nula quando nenhuma foi selecionada).

**Não coberto por esta evolução** (fora de escopo — ver `docs/domain/10-open-questions.md` OQ-020): matriz de permissões por papel; criação/edição/revogação de `PAPEL_ATRIBUICAO` (gestão de atribuições permanece Feature futura de administração).

## Non-goals

- Login/logout/refresh (FT-AUTH).
- Wizard de onboarding e criação de COLABORADOR (FT-PRIMEIRO-ACESSO).
- UI de seleção entre N vínculos cadastrais (superseded — RN-SESSION-003). Distinto da seleção de atribuição de papel (RN-SESSION-008), que é a unidade selecionável.
- CMS / permissões editoriais.
- Persistência de contexto de vínculo em `AUTH_SESSAO`.
- Persistência separada de Contexto Ativo além das FKs de `COLABORADOR`.
- Criação, edição ou revogação de `PAPEL_ATRIBUICAO` (gestão de atribuições — Feature futura).
- Matriz de permissões por papel; cálculo de `permissions` em `/auth/me` (permanece Feature futura, conforme FT-AUTH).

## Relação com primeiro acesso

FT-AUTH autentica a identidade (Zimbra).  
FT-SESSION hidrata identidade + vínculo quando COLABORADOR existir.  
**FT-PRIMEIRO-ACESSO** conduz onboarding, cria COLABORADOR com vínculo completo e estabelece operação; Contexto Ativo é derivado do vínculo.

Fluxos legados de onboarding CMS/frontend: **obsoletos** no TO-BE — ver `specs/features/primeiro-acesso/specification.md`.
