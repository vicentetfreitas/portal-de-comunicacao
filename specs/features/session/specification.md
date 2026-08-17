# Specification — FT-SESSION

| Item | Valor |
|------|-------|
| Feature | FT-SESSION |
| Status | APPROVED (evolução documental 2026-07-24; reconciliado 2026-08-17) |
| Decisões | DEC-FA-002, DEC-FA-003, DEC-ORG-001, DH-02; REF-DB-CTX-01 |

## Objetivo

Após autenticação FT-AUTH, expor e manter o **estado de sessão** do colaborador — vínculo organizacional e **Contexto Ativo** (projeção derivada) — para consumo do Portal.

A **condução do Primeiro Acesso** e o **encerramento do onboarding** pertencem a **FT-PRIMEIRO-ACESSO**.

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

## Non-goals

- Login/logout/refresh (FT-AUTH).
- Wizard de onboarding e criação de COLABORADOR (FT-PRIMEIRO-ACESSO).
- UI de seleção entre N vínculos (superseded).
- CMS / permissões editoriais.
- Persistência de contexto em `AUTH_SESSAO`.
- Persistência separada de Contexto Ativo além das FKs de `COLABORADOR`.

## Relação com primeiro acesso

FT-AUTH autentica a identidade (Zimbra).  
FT-SESSION hidrata identidade + vínculo quando COLABORADOR existir.  
**FT-PRIMEIRO-ACESSO** conduz onboarding, cria COLABORADOR com vínculo completo e estabelece operação; Contexto Ativo é derivado do vínculo.

Fluxos legados de onboarding CMS/frontend: **obsoletos** no TO-BE — ver `specs/features/primeiro-acesso/specification.md`.
