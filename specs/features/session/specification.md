# Specification — FT-SESSION

## Objetivo

Após autenticação FT-AUTH, expor o **contexto organizacional** do colaborador em `/auth/me` e nas claims do JWT, sem persistir seleção em `AUTH_SESSAO` (REF-DB-CTX-01).

## Regras (fase 1 — implementada)

- **RN-SESSION-001:** Contexto inicial derivado dos vínculos do `COLABORADOR` autenticado (`federationId`, `singularId`, `areaId`, `teamId`).
- **RN-SESSION-002:** Com um único contexto disponível, o sistema resolve automaticamente no login.
- **RN-SESSION-004:** O contexto ativo é refletido no access token (claims organizacionais) e em `organizationalLinks` em `GET /api/v1/auth/me`; `AUTH_SESSAO` persiste apenas dados de sessão (refresh token, revogação, dispositivo).

### Modelo físico atual

`COLABORADOR` possui **um** vínculo de cada tipo (`singularId`, `areaId`, `equipeId`). Não há tabela de múltiplos vínculos nem endpoints de troca de contexto.

## Regras (fase 2 — backlog; não implementar sem decisão)

- **RN-SESSION-003:** Com múltiplos contextos, o usuário deve escolher antes de acessar rotas protegidas de negócio (padrão semelhante a alternância de contas). Momento: **após** autenticação/sessão de identidade; **antes** do painel inicial.

Pré-requisitos de fase 2:

1. Resposta a **OQ-027** / **OQ-008** (`docs/domain/10-open-questions.md`) — multi-contexto é requisito?
2. Evolução do modelo de dados para N vínculos (sem reintroduzir `COD_*_CTX` em `AUTH_SESSAO`).
3. Contrato de seleção consumido pelo fluxo em `docs/frontend/frontend-flow.md`.
4. Após OQ respondida: criar DEC aprovada (fluxo OQ → DEC).

### Composição mínima de um contexto (alvo)

| Dimensão | Campo | Operação plena |
|----------|-------|----------------|
| Federação | `federationId` | Sim |
| Singular | `singularId` | Sim (BR-009) |
| Área | `areaId` | Sim (BR-009, BR-010) |
| Equipe | `teamId` | Opcional (BR-002/BR-012) |
| Papel ativo | role/perfil | Sim para autorização (BR-003) — Feature futura |

## Non-goals

- Persistência de contexto organizacional em `AUTH_SESSAO` (`COD_*_CTX` removidos — REF-DB-CTX-01).
- Endpoints `GET /auth/contexts` e `POST /auth/context` (removidos).
- Gestão de múltiplos vínculos por colaborador (backlog — fase 2).
- Substituição do modelo de permissões (PAPEL).
- Definição do painel inicial / home route (**OQ-028**).

## Relação com primeiro acesso

Auto-create de colaborador no login é de FT-AUTH. Vínculo operacional (singular/área) e onboarding são governados por BR-011 e OQ-001 — **não** por esta Feature na fase 1.
