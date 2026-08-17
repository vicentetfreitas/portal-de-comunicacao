# Traceability — FT-PRIMEIRO-ACESSO

## Identificação

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Template | crud-feature@1.1 |
| Status | APPROVED |
| Versão | 1.2 (consolidação estrutural 2026-08-17) |

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
| RF-PA-009 | RN-PA-004 · BR-010, BR-044 | UC-PA-009 | PA-API-006 (pend.) | AT-PA-003 | TK-PA-008 | PARTIAL — API pendente |
| RF-PA-010 | RN-PA-001 | UC-PA-010 | PA-API-005 | AT-PA-008 | TK-PA-009 | RECONCILED |
| RF-PA-011 | DH-03, DH-04 · BR-011, BR-043 | UC-PA-002 | PA-API-006 (pend.) | PENDING | PENDING | PARTIAL — AT e TK pendentes; gap documental conhecido |

## Cobertura

| Item | Total | Cobertos | Pendentes |
|------|-------|----------|-----------|
| RF vigentes | 8 | 6 | 2 (RF-PA-009 API pend.; RF-PA-011 AT/TK) |
| UC vigentes | 6 | 6 | 0 |
| API vigentes | 2 (+ PA-API-006 pend.) | 2 | 1 (PA-API-006) |
| AT vigentes | 7 (+ 3 superseded histórico) | 6 RF cobertos | RF-PA-011 sem AT |
| TK | — | 6 TK vigentes referenciados | RF-PA-011 TK PENDING; TK superseded removidos |

**Cobertura completa:** não — pendências explícitas em RF-PA-009 (API), RF-PA-011 (AT, TK).

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
- [x] RF-PA-011 mantido com AT e TK PENDING
- [x] PA-API-006 registrado como pendente de implementação
- [ ] Cobertura AT completa para todos RF — **não atingido** (RF-PA-011)
- [ ] Cobertura TK completa para todos RF — **não atingido** (RF-PA-011)
- [ ] Implementação PA-API-006 — **fora desta etapa**
