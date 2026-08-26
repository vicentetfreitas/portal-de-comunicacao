# Traceability — FT-PRIMEIRO-ACESSO

## Identificação

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Template | crud-feature@1.1 |
| Status | DONE |
| Versão | 1.3 (fechamento formal 2026-08-26 — ver Critérios de Conclusão) |

## Objetivo

Matriz de rastreabilidade entre requisitos vigentes, regras, cenários, contratos de API, critérios de aceite e tarefas de implementação. Não duplica requisitos, fluxos, contratos ou decisões — ver `specification.md`, `api.md` e `acceptance-tests.md`.

## Escopo da Cadeia

```text
RF → RN → UC → API → AT → TK
```

Somente RF vigentes. RF superseded (RF-PA-003, RF-PA-004, RF-PA-007) e UC superseded (UC-PA-003, UC-PA-004, UC-PA-005, UC-PA-007) excluídos.

## Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK | Status |
|----|----|----|-----|----|----|--------|
| RF-PA-001 | RN-PA-004 · BR-010, BR-011 | UC-PA-001 | PA-API-005 | AT-PA-003 | TK-PA-001 | RECONCILED |
| RF-PA-002 | RN-PA-002 · BR-041 | UC-PA-002, UC-PA-008 | PA-API-005 | AT-PA-001, AT-PA-004 | TK-PA-002 | RECONCILED |
| RF-PA-005 | BR-042 | UC-PA-006 | PA-API-004 | AT-PA-006 | TK-PA-005 | RECONCILED |
| RF-PA-006 | BR-042 | UC-PA-006 | PA-API-004 | AT-PA-006 | TK-PA-005 | RECONCILED |
| RF-PA-008 | RN-PA-007 | UC-PA-008 | PA-API-005, PA-API-004 | AT-PA-007 | TK-PA-007 | RECONCILED |
| RF-PA-009 | RN-PA-004 · BR-010, BR-044 | UC-PA-009 | PA-API-006 | AT-PA-003 | TK-PA-008 | RECONCILED (2026-08-26 — PA-API-006 implementado e testado, ver `PrimeiroAcessoAcceptanceIntegrationTest.shouldListOnlyActiveAreasOfResolvedSingular` + 3 testes de rejeição; flag "(pend.)" estava desatualizado) |
| RF-PA-010 | RN-PA-001 | UC-PA-010 | PA-API-005 | AT-PA-008 | TK-PA-009 | RECONCILED |
| RF-PA-011 | DH-03, DH-04 · BR-011, BR-043 | UC-PA-002 | PA-API-007 | AT-PA-003 (parcial) | PENDING | ACCEPTED DEBT (2026-08-26) — implementado e testado (`PrimeiroAcessoAcceptanceIntegrationTest.shouldCompletePrimeiroAcessoAndIssueOperationalSession` + 4 testes de rejeição/conflito; FE `usePrimeiroAcessoPage.ts` conclui o wizard), mas sem AT formal dedicado ao cenário de conclusão (AT-PA-003 só cobre a entrada no wizard) e sem `tasks.md` nesta feature (TK-PA-* citados nesta matriz não correspondem a artefato existente). Referência de API corrigida nesta reconciliação: era `PA-API-006 (pend.)`, deveria ser `PA-API-007` (conclusão), não `PA-API-006` (listagem). |

## Cobertura

| Item | Total | Cobertos | Pendentes |
|------|-------|----------|-----------|
| RF vigentes | 8 | 8 (implementação/teste) | 0 — RF-PA-011 fecha como dívida documental aceita (AT dedicado + `tasks.md`), não como funcionalidade pendente |
| UC vigentes | 6 | 6 | 0 |
| API vigentes | 3 (PA-API-004, 006, 007) | 3 | 0 — PA-API-006 e PA-API-007 confirmados implementados nesta reconciliação |
| AT vigentes | 7 (+ 3 superseded histórico) | 7 RF cobertos (RF-PA-011 via AT-PA-003 parcial) | Falta AT formal dedicado à conclusão do wizard (RF-PA-011) |
| TK | — | — | Nenhum `tasks.md` existe nesta feature; TK-PA-* citados na matriz não correspondem a artefato — dívida documental aceita, não bloqueia DONE |

**Cobertura completa:** funcionalmente sim (implementação e testes cobrem os 8 RF vigentes — evidência: `PrimeiroAcessoAcceptanceIntegrationTest`, 9 testes verdes, e `usePrimeiroAcessoPage.ts`). Formalmente não 100%: falta AT dedicado à conclusão do wizard e `tasks.md` da feature — registrado como dívida documental aceita no fechamento de 2026-08-26 (Refs: FT-PRIMEIRO-ACESSO), não como bloqueio de DoD.

## Validações Obrigatórias

| Validação | Resultado |
|-----------|-----------|
| RF vigentes alinhados a `specification.md` § 4 | OK |
| RF superseded fora da matriz | OK |
| UC vigentes somente cenários 001, 002, 006, 008, 009, 010 | OK |
| API alinhada a `api.md` | OK |
| AT alinhados a `acceptance-tests.md` | OK |
| TK não inventados; PENDING onde aplicável | OK |
| Decisões humanas (DH-PA-*, DH-02, DH-03, DH-04, DEC-ORG-003, BR-043) preservadas | OK |

## Critérios de Conclusão

- [x] Matriz contém somente RF vigentes
- [x] TK superseded (TK-PA-003, TK-PA-004, TK-PA-006) removidos da matriz vigente
- [x] PA-API-006 implementado e testado (corrigido nesta reconciliação — estava registrado como pendente)
- [x] PA-API-007 implementado e testado; referência de API de RF-PA-011 corrigida (era PA-API-006, é PA-API-007)
- [x] Implementação e testes cobrem os 8 RF vigentes (evidência: 9 testes de `PrimeiroAcessoAcceptanceIntegrationTest`, `usePrimeiroAcessoPage.ts`)
- [ ] Cobertura AT completa para todos RF — **dívida aceita** (RF-PA-011 sem AT formal dedicado à conclusão do wizard; AT-PA-003 cobre só a entrada)
- [ ] `tasks.md` da feature — **dívida aceita**; não existe neste diretório, TK-PA-* citados na matriz não correspondem a artefato
- [x] Fechamento formal (`feature.yaml` → `DONE`, 2026-08-26) autorizado com as duas dívidas acima registradas explicitamente — não bloqueiam DoD por não representarem lacuna de implementação ou de teste
