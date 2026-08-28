# Traceability — FT-SESSION

## Identificação

| Campo | Valor |
|--------|--------|
| Feature ID | FT-SESSION |
| Template | crud-feature@1.1 |
| Status | DONE (feature.yaml migrado do esquema legado `status.specification` → `status: DONE` escalar em 2026-08-28, Pendência #12; mesmo procedimento de FT-PRIMEIRO-ACESSO) |
| Versão | 1.0 (criação 2026-08-28 — Pendência #12) |

## Objetivo

Matriz de rastreabilidade entre as regras oficiais de FT-SESSION (`RN-SESSION-*`),
o contrato exposto (`GET /api/v1/auth/me`, `POST /api/v1/auth/atribuicoes/{id}/ativar`,
`/auth/refresh`), o código de produção e os testes que os cobrem.

Não duplica regras, fluxos ou decisões — ver `specification.md`. `docs/domain/09-business-rules.md`
permanece SSOT das `BR-*`. Esta feature **não possui** artefatos `api.md`, `acceptance-tests.md`
nem `tasks.md`: o contrato de sessão vive em FT-AUTH (`specs/features/authentication/api.md`)
e a condução do onboarding em FT-PRIMEIRO-ACESSO. A cadeia rastreável aqui é:

```text
RN-SESSION → contrato/comportamento → código → teste
```

## Escopo da Cadeia

Somente `RN-SESSION-*` vigentes. `RN-SESSION-003` (seleção entre N vínculos cadastrais) está
**SUPERSEDED** por DH-02 (1 vínculo cadastral) e fica fora da matriz vigente — ver seção própria abaixo.

Fora de escopo (registrado, não rastreado): matriz de permissões por papel e gestão
(criação/edição/revogação) de `PAPEL_ATRIBUICAO` — `OQ-020`, Feature futura de administração.

## Matriz de Rastreabilidade Consolidada

| RN | Contrato / comportamento | Código de produção | Teste | Decisão | Status |
|----|--------------------------|--------------------|-------|---------|--------|
| RN-SESSION-001 | `organizationalLinks` (`federationId`, `singularId`, `areaId`, `teamId?`) em `/auth/me` | `AuthenticationService.organizationalLinksFrom(ColaboradorEntity)`; `ColaboradorDomainService.resolveOrganizationalLinks`; DTO `ColaboradorOrganizationalLinksResponse`; `AuthenticatedUserResponse.organizationalLinks` | BE: `AuthFlowIntegrationTest.shouldCompleteLoginCallbackMeRefreshAndLogoutFlow`; `AuthAcceptanceIntegrationTest.shouldPromotePrimeiroAcessoCredentialToOperationalSession`. FE: `session.store.spec.ts` "hydrates session from /auth/me and auto-resolves active context" | DEC-FA-002, DH-02 | COVERED |
| RN-SESSION-002 | Contexto Ativo derivado automaticamente do único vínculo (hidratação FT-SESSION; primeira entrada orquestrada por FT-PRIMEIRO-ACESSO) | Store de sessão `hydrate` (deriva `activeContext` do único vínculo); `AuthenticationService` fluxo `/auth/me` | FE: `session.store.spec.ts` "hydrates session from /auth/me and auto-resolves active context", "force rehydrates after a prior successful load". BE: `PrimeiroAcessoAcceptanceIntegrationTest.shouldPromotePrimeiroAcessoCredentialToOperationalSession` | DH-02, DEC-FA-003 (parte mantida) | COVERED |
| RN-SESSION-004 | Contexto Ativo refletido em `/auth/me`; `AUTH_SESSAO` **não** persiste `COD_*_CTX` | `AuthenticatedUserResponse`; ausência de colunas de contexto em `AuthSessaoEntity`; migração `database/migrations/V006__drop_auth_sessao_organizational_context.sql` | BE: `SchemaOracleAuditTest` (audita ausência de contexto organizacional em `AUTH_SESSAO`); `AuthFlowIntegrationTest` (me/refresh) | REF-DB-CTX-01 | COVERED |
| RN-SESSION-005 | Contexto Ativo mínimo (`federationId`, `singularId`, `areaId`); navegação operacional usa esse contexto | Getter `activeContext` da store de sessão; guardas de navegação do Portal | FE: `session.store.spec.ts` (asserções de `activeContext`); `auth.service.spec.ts` "fetches current user from /auth/me" | DEC-FA-003 (navegação mantida) | COVERED |
| RN-SESSION-006 | Elegibilidade da atribuição: pertence ao colaborador autenticado, `FLG_ATIVO = 'S'`, dentro da vigência | `PapelAtribuicaoService.listElegiveis` / `findElegivel`; `PapelAtribuicaoEntity.isElegivel(Instant)`; `PapelAtribuicaoRepository.findByColaborador_Id` / `findByIdAndColaborador_Id` | BE: `PapelAtribuicaoServiceTest.listElegiveisShouldExcludeInactiveAndOutOfVigenciaAssignments`, `findElegivelShouldReturnEmptyWhenAssignmentBelongsToAnotherColaborador`, `findElegivelShouldReturnEmptyWhenAssignmentIsInactive`, `findElegivelShouldReturnEmptyWhenIdIsNull`; `SessionAtribuicaoAcceptanceIntegrationTest.shouldRejectSelectingAnotherUsersAssignment`, `shouldRejectSelectingInactiveAssignment`, `shouldRejectSelectingAssignmentOutsideVigencia` | DEC-DB-020 | COVERED |
| RN-SESSION-007 | Exatamente 1 atribuição elegível → seleção automática, sem ação do colaborador (regra de estado: login, `/auth/me` e refresh) | `PapelAtribuicaoService.resolveAutomatica`; `AuthenticationService` (login L223-225; `/auth/me` L349-352, comentário `RN-SESSION-007`) | BE: `PapelAtribuicaoServiceTest.shouldAutoSelectWhenExactlyOneEligibleAssignment`; `SessionAtribuicaoAcceptanceIntegrationTest.shouldAutoSelectWhenExactlyOneEligibleAssignment`, `shouldAutoSelectOnMeWhenAssignmentBecomesEligibleAfterLogin`. FE: `session.store.spec.ts` "exposes a single eligible assignment as already active" | DEC-DB-020 | COVERED |
| RN-SESSION-008 | > 1 (ou 0) elegíveis → sem seleção automática; seleção explícita via `POST /api/v1/auth/atribuicoes/{papelAtribuicaoId}/ativar`; a unidade selecionável é a atribuição, nunca o vínculo | `PapelAtribuicaoService.resolveAutomatica` (retorna vazio); `AuthController` `@PostMapping("/atribuicoes/{papelAtribuicaoId}/ativar")` | BE: `PapelAtribuicaoServiceTest.shouldNotAutoSelectWhenMultipleEligibleAssignments`, `shouldNotAutoSelectWhenNoEligibleAssignments`; `SessionAtribuicaoAcceptanceIntegrationTest.shouldNotAutoSelectWhenMultipleEligibleAssignments`, `shouldActivateValidOwnAssignment`. FE: `session.store.spec.ts` "signals that selection is needed when multiple assignments and none active" | DEC-DB-020 | COVERED |
| RN-SESSION-009 | Ativação/troca sempre revalidada no backend (pertencimento, `FLG_ATIVO`, vigência); troca substitui o Access Token sem afetar sessão (Refresh Token / `AUTH_SESSAO`) e sem novo login | `PapelAtribuicaoService.findElegivel` (revalida contra o banco); `AuthenticationService` (ativação/troca emitindo novo Access Token, L287-291) | BE: `SessionAtribuicaoAcceptanceIntegrationTest.shouldSwitchAssignmentWithoutNewLogin`, `shouldRejectSelectingAnotherUsersAssignment`, `shouldRejectSelectingInactiveAssignment`, `shouldRejectSelectingAssignmentOutsideVigencia`. FE: `session.store.spec.ts` "selects an assignment without a new login and updates active context"; `auth.service.spec.ts` "activates a role assignment without sending a body" | DEC-DB-020 | COVERED |
| RN-SESSION-010 | Access Token representa a atribuição pelo claim estável `atribId` (nunca reutiliza `fid`/`singularId`/`areaId`/`teamId`); `/auth/refresh` preserva a atribuição enquanto elegível, senão reaplica RN-SESSION-007/008; permissões nunca no token | `JwtTokenService` (claim `atribId` — emissão L104, leitura L222); `PapelAtribuicaoService.resolveParaRefresh`; `AuthenticationService` fluxo refresh (L287) | BE: `PapelAtribuicaoServiceTest.resolveParaRefreshShouldKeepPreviousAssignmentWhenStillEligible`, `resolveParaRefreshShouldFallBackToAutomaticSelectionWhenPreviousNoLongerEligible`, `resolveParaRefreshShouldReturnEmptyWhenNoPreviousAndMultipleEligible`; `SessionAtribuicaoAcceptanceIntegrationTest.shouldPreserveActiveAssignmentAcrossRefresh`, `shouldDropActiveAssignmentOnRefreshWhenNoLongerEligible` | DEC-DB-020 | COVERED |

## RN-SESSION-003 — superseded (fora da matriz vigente)

| RN | Situação | Referência |
|----|----------|-----------|
| RN-SESSION-003 | **SUPERSEDED** por DH-02 (2026-08-14) — 1 vínculo cadastral por COLABORADOR (DEC-DB-028); não há seleção entre N vínculos cadastrais. A seleção **de atribuição de papel** (RN-SESSION-008) é distinta e permanece vigente. A regra de navegação no Contexto Ativo migrou para RN-SESSION-005. | `specification.md` § "RN-SESSION-003 — texto histórico"; `docs/governance/03-open-decisions.md` — DEC-FA-003 § Supersession parcial |

## Cobertura

| Item | Total | Cobertos | Pendentes |
|------|-------|----------|-----------|
| RN-SESSION vigentes | 9 (001, 002, 004–010) | 9 | 0 |
| RN-SESSION superseded | 1 (003) | — | — (fora de escopo por DH-02) |

**Cobertura completa:** sim. Todas as `RN-SESSION-*` vigentes têm código de produção e teste
associados. Evidência agregada: `PapelAtribuicaoServiceTest` (10 testes) +
`SessionAtribuicaoAcceptanceIntegrationTest` (10 testes) + `AuthFlowIntegrationTest` +
`SchemaOracleAuditTest` + cobertura de front-end em `session.store.spec.ts` / `auth.service.spec.ts`.

## Validações Obrigatórias

| Validação | Resultado |
|-----------|-----------|
| Matriz contém somente `RN-SESSION-*` vigentes | OK |
| `RN-SESSION-003` fora da matriz vigente, com registro de supersession | OK |
| Cada RN vigente mapeada a código de produção existente | OK |
| Cada RN vigente mapeada a teste existente (nome verificável) | OK |
| Sem `RF-*`/`UC-*`/`AT-*`/`TK-*` inventados (feature sem esses artefatos) | OK |
| Decisões preservadas (DEC-FA-002, DEC-FA-003, DEC-ORG-001, DEC-DB-020, DH-02, REF-DB-CTX-01) | OK |
| `OQ-020` (gestão de `PAPEL_ATRIBUICAO` + matriz de permissões) marcado como fora de escopo | OK |

## Notas

- **Dívida de decisão (herdada da spec):** a "Nota de rastreabilidade (2026-08-20)" de
  `specification.md` registra que a evolução `PAPEL_ATRIBUICAO` (RN-SESSION-006..010) foi
  incorporada à spec sem `DEC-XXX`/`DH-XXX` dedicado, apenas referenciando `DEC-DB-020`.
  Se o projeto exigir formalização, isso permanece pendente de registro em
  `docs/governance/03-open-decisions.md` — não é lacuna de implementação nem de teste.
- Referências de linha (`Lxxx`) são indicativas da versão de 2026-08-28; usar os nomes de
  método/DTO como âncora estável.

## Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.0 | 2026-08-28 | Criação (Pendência #12). Matriz `RN-SESSION-*` → código → teste; migração de `feature.yaml` para `status: DONE` escalar registrada. |
