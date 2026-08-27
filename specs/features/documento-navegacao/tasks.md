# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — navegação/leitura; estende `FT-DOCUMENTO`) |
| Versão | 1.0 |
| Status | DRAFT — não iniciar antes de Review de Spec (`APPROVED`) e DoR-Implementation |
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

Decomposição preliminar. **Depende das decisões abertas** (`decisions.md` D-01..D-06) —
as tasks abaixo assumem a proposta do `/specify` (D-01(a), busca client-side, só Área
do Contexto Ativo, sem filtro no MVP). Se o Review decidir diferente, revisar.

---

## TK-DOC-NAV-001 — Estender `PastaResponse` (backend)

### Objetivo

Adicionar `pastaPaiId` (`COD_PASTA_PAI`) e `dataAtualizacao` (`DAT_ATUALIZACAO`) ao
`PastaResponse`, mantendo `GET /api/v1/pastas` retrocompatível (`RF-DOC-NAV-001/003/006`).

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-001, RF-DOC-NAV-003, RF-DOC-NAV-006

### Componentes Esperados

- `PastaResponse` (record) — 2 campos novos.
- `PastaMapper.toResponse` — mapear `pasta.getPastaPaiId()` e `pasta.getDataAtualizacao()`
  (a entidade `PastaEntity` já tem os dois getters).
- Sem mudança em `PastaApplicationService.list` além do mapper.
- Testes: `PastaAcceptanceIntegrationTest` — assertar `pastaPaiId`/`dataAtualizacao`
  no payload; hierarquia de fixture (`PastaTestBuilder.pastaPai(...)` já existe).

### Critérios de Conclusão

- `GET /api/v1/pastas` devolve os campos novos; clientes antigos não quebram.
- `./mvnw clean verify` verde.

---

## TK-DOC-NAV-002 — Explorador de pastas (frontend)

### Objetivo

Trocar a lista plana de `AreaColaboradorArquivosPage` por navegação drill-in:
nível inicial = raízes; entrar numa pasta; `breadcrumb`; voltar
(`RF-DOC-NAV-001`, `RF-DOC-NAV-002`).

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-001, RF-DOC-NAV-002, RF-DOC-NAV-007

### Componentes Esperados

- `useAreaColaboradorArquivos` — a partir da lista de `PastaResponse` (agora com
  `pastaPaiId`), construir o índice pai→filhos; estado `pastaAtualId`; derivar
  `raízes`, `conteúdoDoNível`, `caminho` (breadcrumb, parando na raiz órfã).
- Componente de `breadcrumb` (reusar `DsBreadcrumbs` se servir).
- Card/linha de pasta (ícone, nome, data) e reuso da linha de documento existente.
- Os menus de gestão de `FT-DOCUMENTO-GESTAO` continuam nos nós (não regridir).
- Deep-link: `?pasta=<id>` na rota (`RF-DOC-NAV-006`); resolver id → se não visível na
  lista carregada, mostrar "sem acesso".
- Testes unit: raízes; entrar/voltar; raiz órfã; deep-link válido/ inválido.

### Critérios de Conclusão

- AT-DOC-NAV-001, -002, -006 (parte explorador), -007 atendidos.
- `yarn typecheck` + `yarn test:unit` verdes.

---

## TK-DOC-NAV-003 — Painel de árvore (frontend)

### Objetivo

Painel lateral com a hierarquia expansível; seleção sincroniza com o explorador
(`RF-DOC-NAV-003`).

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-003, RF-DOC-NAV-006

### Componentes Esperados

- Componente de árvore (`q-tree` do Quasar, ou componente próprio) alimentado pelo
  mesmo índice pai→filhos do explorador.
- Nós recolhidos por padrão a partir das raízes; expandir/recolher; selecionar → seta
  `pastaAtualId`.
- Alternância explorador ⇄ árvore refletida na URL.
- Testes unit: hierarquia; subárvore sem pastas visíveis não aparece; seleção move o
  explorador.

### Critérios de Conclusão

- AT-DOC-NAV-003, -006 (parte árvore) atendidos.

---

## TK-DOC-NAV-004 — Busca e toggle grade/lista (frontend)

### Objetivo

Busca client-side por nome (pasta/documento) no escopo carregado; toggle grade/lista
com preferência persistida (`RF-DOC-NAV-004`, `RF-DOC-NAV-005`).

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-004, RF-DOC-NAV-005

### Componentes Esperados

- Campo de busca ("Buscar documentos, pastas...") → filtra o índice já carregado;
  resultados com a pasta de origem; selecionar posiciona o explorador.
- Toggle grade/lista; persistência via o mecanismo de `DEC-FA-005`.
- Testes unit: busca encontra/ não-vaza/ vazio; toggle persiste.

### Critérios de Conclusão

- AT-DOC-NAV-004, -005 atendidos.

---

# Matriz de Rastreabilidade

| Task | RF | UC | AT |
|------|----|----|----|
| TK-DOC-NAV-001 | RF-DOC-NAV-001/003/006 | UC-DOC-NAV-001/003/006 | AT-DOC-NAV-001/003/006 |
| TK-DOC-NAV-002 | RF-DOC-NAV-001/002/007 | UC-DOC-NAV-001/002/007 | AT-DOC-NAV-001/002/006/007 |
| TK-DOC-NAV-003 | RF-DOC-NAV-003/006 | UC-DOC-NAV-003 | AT-DOC-NAV-003/006 |
| TK-DOC-NAV-004 | RF-DOC-NAV-004/005 | UC-DOC-NAV-004/005 | AT-DOC-NAV-004/005 |

---

# Critérios de Conformidade

Conforme quando: todas as tasks têm ≥1 RF; não representa cronograma; consistente com
`specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md`, `decisions.md`,
`traceability.md`. **As tasks pressupõem D-01..D-06 fechadas** — reabrir se o Review
mudar as decisões.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — TK-DOC-NAV-001 (BE — `PastaResponse` + `pastaPaiId`), -002 (explorador FE), -003 (árvore FE), -004 (busca + grade/lista FE). Preliminares — dependem das decisões abertas. |
