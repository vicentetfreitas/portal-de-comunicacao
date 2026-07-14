# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1 |
| Status | STABLE |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | ${FEATURE_ID} |
| Feature | ${FEATURE_NAME} |
| Domínio | ${DOMAIN} |

---

# Objetivo

Este documento consolida a rastreabilidade completa da Feature.

Responsabilidade exclusiva: relacionar Requisitos, Regras de Negócio, Casos de Uso, API, Acceptance Tests e Tasks em uma visão única.

Este documento **não substitui** os demais artefatos. Não duplica requisitos, contratos ou cenários — apenas consolida os vínculos entre eles.

---

# Escopo da Cadeia

```text
RF
 ↓
RN
 ↓
UC
 ↓
API
 ↓
AT
 ↓
TK
```

---

# Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK | Status |
|----|----|----|-----|----|----|--------|
| RF-${DOMAIN}-001 | RN-${DOMAIN}-001 | UC-${DOMAIN}-001 | POST ${API_BASE_PATH} | AT-${DOMAIN}-001 | TK-${DOMAIN}-001 | |
| RF-${DOMAIN}-002 | RN-${DOMAIN}-002 | UC-${DOMAIN}-002 | GET ${API_BASE_PATH}/{${PRIMARY_KEY}} | AT-${DOMAIN}-002 | TK-${DOMAIN}-002 | |
| RF-${DOMAIN}-003 | RN-${DOMAIN}-003 | UC-${DOMAIN}-003 | GET ${API_BASE_PATH} | AT-${DOMAIN}-003 | TK-${DOMAIN}-003 | |
| RF-${DOMAIN}-004 | RN-${DOMAIN}-004 | UC-${DOMAIN}-004 | PUT ${API_BASE_PATH}/{${PRIMARY_KEY}} | AT-${DOMAIN}-004 | TK-${DOMAIN}-004 | |
| RF-${DOMAIN}-005 | RN-${DOMAIN}-005 | UC-${DOMAIN}-005 | PATCH ${API_BASE_PATH}/{${PRIMARY_KEY}}/status | AT-${DOMAIN}-005 | TK-${DOMAIN}-005 | |

Preencher a coluna **Status** com `COMPLETE` quando a linha estiver validada.

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | | | |
| Regras de Negócio | | | |
| Casos de Uso | | | |
| Endpoints | | | |
| Acceptance Tests | | | |
| Tasks | | | |

---

# Validações Obrigatórias

Antes de encerrar a fase de Specification:

- [ ] Todos os RF possuem UC, API, AT e TK
- [ ] Todos os AT possuem RF associado
- [ ] Todas as TK possuem RF associado
- [ ] Nenhum endpoint sem justificativa funcional (RF)
- [ ] Matriz consistente com `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `tasks.md`

---

# Critérios de Conclusão

A rastreabilidade será considerada **COMPLETE** quando:

- 100% dos Requisitos Funcionais estiverem cobertos na matriz;
- não existirem artefatos órfãos;
- não houver divergência entre este documento e os demais artefatos.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | YYYY-MM-DD | Engineering Framework | Criação do template |
| 1.1 | 2026-07-13 | Engineering Framework | Formalização como artefato oficial (Sprint Framework v1.1) |
