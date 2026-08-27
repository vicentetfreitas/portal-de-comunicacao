# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — navegação/leitura) |
| Versão | 1.0 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-NAVEGACAO |
| Feature | Navegação de Pastas e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Consolida a rastreabilidade entre `specification.md`, `use-cases.md`, `api.md`,
`acceptance-tests.md`, `decisions.md` e `tasks.md`.

---

# Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK |
|----|----|----|-----|----|----|
| RF-DOC-NAV-001 | BR-016, BR-017 | UC-DOC-NAV-001 | GET /api/v1/pastas (+ `pastaPaiId`) | AT-DOC-NAV-001 | TK-DOC-NAV-001, TK-DOC-NAV-002 |
| RF-DOC-NAV-002 | — | UC-DOC-NAV-002 | (cliente — breadcrumb do `pastaPaiId`) | AT-DOC-NAV-002 | TK-DOC-NAV-002 |
| RF-DOC-NAV-003 | BR-017 | UC-DOC-NAV-003 | GET /api/v1/pastas (+ `pastaPaiId`) | AT-DOC-NAV-003 | TK-DOC-NAV-001, TK-DOC-NAV-003 |
| RF-DOC-NAV-004 | — | UC-DOC-NAV-004 | (cliente no MVP — D-04) | AT-DOC-NAV-004 | TK-DOC-NAV-004 |
| RF-DOC-NAV-005 | — | UC-DOC-NAV-005 | (nenhuma — preferência local, DEC-FA-005) | AT-DOC-NAV-005 | TK-DOC-NAV-004 |
| RF-DOC-NAV-006 | — | UC-DOC-NAV-006 | GET /api/v1/pastas + rota com `?pasta=<id>` | AT-DOC-NAV-006 | TK-DOC-NAV-002, TK-DOC-NAV-003 |
| RF-DOC-NAV-007 | BR-012, BR-018, BR-020 | UC-DOC-NAV-007 | (transversal — herda RF-DOCUMENTO-003) | AT-DOC-NAV-007 | TK-DOC-NAV-001, TK-DOC-NAV-002 |

**RN `—`:** breadcrumb/voltar, busca e toggle grade/lista são interação de UI sem
Regra de Negócio dedicada em `docs/domain/09-business-rules.md` — não são requisitos
órfãos (cada um tem UC, AT e TK).

---

# Rastreabilidade das Decisões

| Decisão | Estado | Afeta |
|---------|--------|-------|
| D-01 contrato de API | 🔶 ABERTA (proposta: (a)) | RF-001/003/006, `api.md` inteiro, TK-DOC-NAV-001 |
| D-02 filtro | 🔶 ABERTA (pode sair do MVP) | RF-004 / possível RF novo |
| D-03 contexto (Área ativa vs. Federação) | 🔶 ABERTA | § Escopo, `FT-FEDERACAO-COLABORADOR` |
| D-04 busca cliente vs. servidor | 🔶 ABERTA (depende de D-01) | RF-004, TK-DOC-NAV-004 |
| D-05 rótulo Público/Privado | 🔶 ABERTA | card do explorador |
| D-06 formato do id na URL | 🔶 ABERTA (proposta: id numérico) | RF-006, TK-DOC-NAV-002/003 |
| D-07 persistência grade/lista | ✅ proposta (DEC-FA-005) | RF-005, TK-DOC-NAV-004 |

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 7 | 7 | 0 |
| Regras de Negócio | BR-012, BR-016, BR-017, BR-018, BR-020 | 5 | 0 |
| Casos de Uso | 7 | 7 | 0 |
| Endpoints | 1 (estendido) | 1 | 0 |
| Acceptance Tests | 7 | 7 | 0 |
| Tasks | 4 | 4 | 0 |

---

# Dívidas / Bloqueios

## Bloqueiam `READY_FOR_REVIEW` (decisões de produto)

- D-01 (contrato de API), D-02 (filtro), D-03 (contexto), D-04 (busca), D-05 (rótulo),
  D-06 (id na URL) — ver `decisions.md`.

## Dívidas aceitas (se as propostas forem adotadas)

- Busca e árvore montadas client-side sobre `GET /api/v1/pastas` — assume volume "baixo"
  por Área (premissa herdada de `FT-DOCUMENTO`). Se crescer → opção D-01(c) + busca
  server-side numa iteração.
- `PastaResponse` estendido é propriedade desta Feature, não reabre `FT-DOCUMENTO`
  (`DONE`) — mudança aditiva/retrocompatível.
- Rótulo "Público/Privado" e filtro avançado provavelmente ficam para iteração futura.

---

# Validações Obrigatórias

- [x] Todos os RF possuem UC, API (ou nota "cliente") e AT
- [x] Todos os AT possuem RF associado
- [x] Todas as TK possuem RF associado
- [x] Nenhum endpoint sem justificativa funcional
- [ ] Decisões de produto fechadas — **pendente** (D-01..D-06)
- [x] Matriz consistente com os demais artefatos

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — RF/UC/API/AT/TK-DOC-NAV-001..007; 6 decisões abertas registradas como bloqueio de `READY_FOR_REVIEW` |
