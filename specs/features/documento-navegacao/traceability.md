# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — navegação/leitura) |
| Versão | 1.2 |
| Status | DONE |
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

Todas fechadas em 2026-08-27 (`decisions.md` v1.1):

| Decisão | Resolução | Afeta |
|---------|-----------|-------|
| D-01 contrato de API | ✅ **(a)** — estender `PastaResponse` (`pastaPaiId`, `dataAtualizacao`) | RF-001/003/006, `api.md`, TK-DOC-NAV-001 |
| D-02 filtro | ✅ **fora do MVP** | § Escopo (removido) |
| D-03 contexto | ✅ **só a Área do Contexto Ativo** | § Escopo, `FT-FEDERACAO-COLABORADOR` (não tocada) |
| D-04 busca | ✅ **client-side** | RF-004, TK-DOC-NAV-004 |
| D-05 rótulo Público/Privado | ✅ **omitir no MVP** | card do explorador |
| D-06 id na URL | ✅ **id numérico** (`?pasta=<COD_PASTA>`) | RF-006, TK-DOC-NAV-002/003 |
| D-07 persistência grade/lista | ✅ **DEC-FA-005** | RF-005, TK-DOC-NAV-004 |

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

## Bloqueiam `READY_FOR_REVIEW`

Nenhum — as 7 decisões de produto foram fechadas (`decisions.md` v1.1).

## Dívidas aceitas

- **Sem suíte E2E Playwright dedicada** (`AT-DOC-NAV-*`) — decisão do usuário 2026-08-28
  (fechar com unit + integração Oracle + `AT-DOC-NAV-001` em `PastaAcceptanceIntegrationTest`).
- Árvore expandida por padrão (`default-expand-all`) em vez de "recolhida por padrão"
  (`tasks.md` § Componentes Esperados — não é AT).
- Resultado de busca de documento posiciona o explorador na pasta mas **não destaca** o
  documento (AT-DOC-NAV-004 diz "e o destaca").
- Preferência grade/lista em `localStorage` por dispositivo (D-07 = `DEC-FA-005`), não na
  URL — o § Escopo menciona URL de forma mais solta; D-07 governa.
- Busca e árvore montadas client-side sobre `GET /api/v1/pastas` — assume volume "baixo"
  por Área (premissa herdada de `FT-DOCUMENTO`). Se crescer → lazy por nível + busca
  server-side numa iteração futura (fora desta Feature).
- `PastaResponse` estendido é propriedade desta Feature, não reabre `FT-DOCUMENTO`
  (`DONE`) — mudança aditiva/retrocompatível.
- Rótulo "Público/Privado", filtro e navegação por Federação ficam para iteração futura.

---

# Validações Obrigatórias

- [x] Todos os RF possuem UC, API (ou nota "cliente") e AT
- [x] Todos os AT possuem RF associado
- [x] Todas as TK possuem RF associado
- [x] Nenhum endpoint sem justificativa funcional
- [x] Decisões de produto fechadas (`decisions.md` v1.1 — D-01..D-07)
- [x] Matriz consistente com os demais artefatos

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — RF/UC/API/AT/TK-DOC-NAV-001..007; 6 decisões abertas registradas como bloqueio de `READY_FOR_REVIEW` |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Decisões D-01..D-07 fechadas; nenhum bloqueio de `READY_FOR_REVIEW` restante; checklist de validação completo |
| 1.2 | 2026-08-28 | Claude Code (Review) | Fechamento — 4 tasks implementadas (`api b87f34d`; `app cf3f119`→`2969042`); `mvn verify` 400/0/2, `test:unit` 228; Gate 3 + Gate 6 PASS. Dívidas aceitas registradas. Header → `DONE`. |
