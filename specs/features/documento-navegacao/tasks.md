# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — navegação/leitura; estende `FT-DOCUMENTO`) |
| Versão | 1.1 |
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

Decomposição funcional. As 7 decisões estão fechadas (`decisions.md` v1.1): D-01=(a)
estender `PastaResponse`; busca client-side; só Área do Contexto Ativo; sem filtro nem
rótulo Público/Privado no MVP; id numérico na URL; preferência grade/lista via
`DEC-FA-005`.

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

### Critérios de Conclusão — ✅ ATENDIDA (2026-08-28, `portal-comunicacao-api` `b87f34d`, branch `development`)

- ✅ `PastaResponse` (record) + `pastaPaiId` (`Long|null`) + `dataAtualizacao` (`Instant|null`);
  `PastaMapper.toResponse` mapeia os getters Lombok já existentes; assinatura do mapper
  inalterada — `PastaApplicationService.list` e `PastaGestaoApplicationService` intactos.
  Aditivo/retrocompatível (sem `@JsonInclude(NON_NULL)` global — campos nulos serializam).
- ✅ `PastaTestBuilder.atualizadaEm(Instant)`; `PastaAcceptanceIntegrationTest` novo teste
  `AT-DOC-NAV-001` (pasta filha expõe `pastaPaiId` da pai + `dataAtualizacao`; raiz ambos null).
- ✅ `./mvnw clean verify` = **400 testes, 0 falhas, 2 skipped**.

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

### Critérios de Conclusão — ✅ ATENDIDA (2026-08-28, `portal-comunicacao-app` `cf3f119`, branch `development`)

- ✅ AT-DOC-NAV-001, -002, -006 (parte explorador), -007 atendidos.
- ✅ `PastaApiService.listAll()` agrega todas as páginas de `GET /api/v1/pastas` (D-04;
  fecha a ressalva #3 do Review de Spec). `useAreaColaboradorArquivos` ganhou índice
  pai→filhos, `pastaAtualId`, `raizes` (raiz órfã incluída), `subpastasVisiveis`,
  `documentosDoNivel`, `caminho` (breadcrumb), `entrarNaPasta`/`voltarUmNivel`/
  `irParaPasta`, `resolverDeepLink` (id fora do escopo → `deepLinkSemAcesso`, nunca o
  conteúdo — fecha as ressalvas #1/#2 na prática: sem endpoint de resolução por id, o
  "sem acesso" é client-side).
- ✅ `AreaColaboradorArquivosPage`: toolbar (voltar + breadcrumb + Enviar arquivo /
  Nova subpasta no nível), grade de subpastas clicáveis, documentos do nível, estados
  "pasta vazia" e "sem acesso", deep-link `?pasta=<id>` ↔ URL. Menus de gestão mantidos.
- ✅ `yarn typecheck` limpo; `yarn test:unit` **220 verdes** (+11).

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

### Critérios de Conclusão — ✅ ATENDIDA (2026-08-28, `portal-comunicacao-app` `bc2907b`, branch `development`)

- ✅ AT-DOC-NAV-003, -006 (parte árvore) atendidos.
- ✅ `arvoreNodes` (computed) — hierarquia em formato `q-tree` (`{ id, label, children? }`),
  folhas sem `children`, subárvore sem pastas visíveis omitida, guarda anti-ciclo.
- ✅ Página: botão de alternância + painel lateral `<q-tree default-expand-all>`, seleção
  de nó → `entrarNaPasta` (sincroniza explorador + breadcrumb). Estado do painel na URL
  (`?arvore=1`). Layout duas colunas, colapsa no mobile.
- ✅ `test:unit` 223 verdes.

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

### Critérios de Conclusão — ✅ ATENDIDA (2026-08-28, `portal-comunicacao-app` `2969042`, branch `development`)

- ✅ AT-DOC-NAV-004, -005 atendidos.
- ✅ `termoBusca`/`buscando`/`resultadosBusca` — busca client-side por trecho do nome
  (pasta ou documento), `contains` case-insensitive, restrita ao conjunto visível, com o
  caminho legível da pasta; `abrirResultado` posiciona o explorador e limpa o termo.
- ✅ `modoVisualizacao` (`grade`/`lista`) persistido em `localStorage` por dispositivo
  (mesmo padrão de `DEC-FA-005`/`useTheme`, com try/catch). Página: `DsSearchInput` +
  botão de alternância; lista de resultados substitui o explorador enquanto há termo;
  modificador `--lista` na grade.
- ✅ `test:unit` 228 verdes.

---

# Estado da Feature

**Código completo (2026-08-28) — as 4 tasks feitas.** `feature.yaml` permanece
`IMPLEMENTING` (falta `/validate` → `/review` de PR / Gate 3 → DoD / Gate 6). Commits em
`origin/development` **não pushados**: `portal-comunicacao-api` `b87f34d`;
`portal-comunicacao-app` `cf3f119` → `bc2907b` → `2969042`. Sem suíte E2E Playwright
dedicada — mesma dívida aceita de FT-DOCUMENTO-GESTAO / -UPLOAD.

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
`traceability.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — TK-DOC-NAV-001 (BE — `PastaResponse` + `pastaPaiId`), -002 (explorador FE), -003 (árvore FE), -004 (busca + grade/lista FE). Preliminares — dependem das decisões abertas. |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Decisões fechadas — removido o caveat "depende das decisões abertas"; tasks consolidadas. |
| 1.2 | 2026-08-28 | Claude Code (Implement) | **TK-DOC-NAV-001 concluída** — `PastaResponse` + `pastaPaiId`/`dataAtualizacao` (`portal-comunicacao-api` `b87f34d`); `mvn clean verify` 400/0/2. Restam TK-DOC-NAV-002/003/004 (frontend). |
| 1.3 | 2026-08-28 | Claude Code (Implement) | **TK-DOC-NAV-002 concluída** — explorador drill-in (`portal-comunicacao-app` `cf3f119`): `listAll()` paginado agregado, índice pai→filhos, breadcrumb, raiz órfã, deep-link `?pasta=<id>`. `test:unit` 220 verdes. Restam TK-DOC-NAV-003 (árvore) e -004 (busca + grade/lista). |
| 1.4 | 2026-08-28 | Claude Code (Implement) | **TK-DOC-NAV-003 concluída** — painel de árvore `q-tree` (`app` `bc2907b`), `?arvore=1`. `test:unit` 223. |
| 1.5 | 2026-08-28 | Claude Code (Implement) | **TK-DOC-NAV-004 concluída** — busca client-side + toggle grade/lista persistido (`app` `2969042`). `test:unit` 228. **Código completo — as 4 tasks feitas.** |
