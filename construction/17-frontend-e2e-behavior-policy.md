# Frontend E2E — Política de Comportamento (E2E-02)

| Item | Valor |
|------|-------|
| Regra | **E2E-02** — testes Playwright orientados a comportamento funcional |
| Versão | 1.0 |
| Data | 2026-07-17 |
| Decisão | DL-EF-4.2-011 |
| Complementa | **E2E-01** (`16-frontend-validation-gates.md`) — *quando* rodar E2E |
| Escopo | *Como* escrever e revisar specs em `frontend/test/e2e/` |
| Guia de execução | `frontend/test/e2e/README.md` |

---

## Problema que esta política resolve

Specs E2E acoplados a **implementação** (Quasar, classes CSS, paginação interna) quebram sem mudança de comportamento visível ao usuário. Isso gera retrabalho no **PKG-FE-06** e decisões repetitivas sobre “corrigir teste ou UI”.

---

## O que um teste E2E deve validar

| Categoria | Exemplos |
|-----------|----------|
| Fluxos completos | Cadastro → redirecionamento → detalhe; edição → confirmação |
| Navegação | URLs, breadcrumbs, ações de hub/lista |
| Filtros | Resultado visível coerente com filtro aplicado |
| Paginação **funcional** | Conteúdo da página muda; item da página 2 não aparecia na 1 |
| Mensagens | Toasts, erros de negócio, estados vazios (texto i18n) |
| Permissões / auth | Rotas protegidas, mocks de sessão |
| Estados de UI | Loading → conteúdo; 404 amigável; badge de status |
| Integração UI ↔ API (mock) | Contrato HTTP refletido na tela (422 → mensagem no formulário) |

Cada spec **AT-FE-*** deve mapear a cenários em `specs/features/<feature>/acceptance-tests.md`, não a estrutura do DOM.

---

## O que um teste E2E nunca deve validar

| Proibido | Motivo |
|----------|--------|
| Classes CSS (`.q-*`, `.ds-*`, utilitários) | Detalhe de tema/biblioteca |
| Estrutura HTML específica do Quasar | Não é contrato de produto |
| Botões de paginação **numéricos** (`"2"`, `"3"`) no `QTable` | QTable usa **Anterior/Próxima** (`aria-label` i18n), não numeração |
| Ordem de itens **sem** alinhar ao `sort` da API/mock | Evita falsos negativos (ex.: `Ativa 11` na página 1 com `name,asc` lexicográfico) |
| Wrappers internos do DS sem contrato abaixo | `DsCard`, slots Quasar, `q-table__*` |
| Ordem de renderização / quantidade de nós DOM | Frágil e irrelevante ao usuário |
| Detalhes de biblioteca (ícones MDI, densidade Quasar) | Implementação |

**Exceção:** validar contrato **público** documentado nesta política (seção Design System).

---

## Locators — ordem de preferência

1. **Texto e rótulos visíveis ao usuário** — `getByLabel`, `getByText`, `getByRole('heading', { name })` com strings de `i18n` / acceptance tests.
2. **Papéis ARIA do contrato DS** — `role="alert"` em erro de campo; `role="status"` em badge de status (ver abaixo).
3. **Controles com nome acessível estável** — ex.: botão **"Próxima página"** (Quasar `pt-BR`) para avançar paginação quando o cenário exige mudar de página.
4. **Escopo semântico amplo** — `getByRole('table')` + texto na tabela; região da página (`main`), não `.ds-data-table`.

| Evitar | Preferir |
|--------|----------|
| `.ds-data-table .q-table__bottom` | `getByRole('button', { name: 'Próxima página' })` no contexto da listagem |
| `getByRole('button', { name: '2' })` | Avançar página + assert em **texto de linha** esperado na página 2 |
| `getByRole('option', { name: 'Ativa' })` sem `exact: true` | `exact: true` quando rótulos se contêm (`Ativa` ⊂ `Inativa`) |

---

## Paginação — contrato funcional

| Fato | Implicação para E2E |
|------|---------------------|
| `rowsPerPage` default **10** (`useEquipeList` / `useSingularList`) | Segunda página só com **> 10** registros **após filtro** |
| Ordenação default **`name,asc`** (API + mocks E2E) | Calcular qual **nome** deve aparecer na página 2 (ordenação lexicográfica `pt-BR`) |
| QTable Quasar | Interação: **Próxima página** / **Página anterior**; assert: linha visível / ausente |

**Padrão de assert de paginação:**

```text
Given 12+ registros ativos após filtro
When  usuário avança uma página (controle "Próxima página")
Then  um registro que só existe na página 2 está visível
And   um registro típico da página 1 não está visível na tabela
```

Não fixar índice numérico no nome (`Ativa 11`) como proxy de “página 2” sem derivar do sort.

---

## Design System — contrato público validável em E2E

Estes comportamentos são **intencionais** e podem ser usados em locators:

| Componente | Contrato | Locator típico |
|------------|----------|----------------|
| **DsInput** (erro de campo) | Mensagem com `role="alert"` e `aria-label` = texto do erro | `getByRole('alert', { name: '...' })` |
| **DsBadge** (status equipe/singular no detalhe) | `role="status"` + `aria-label` = rótulo i18n (`Ativa` / `Inativa`) | `getByRole('status', { name: 'Ativa' })` |
| **DsPageHeader** | Título em `<h1>` com texto da página | `getByRole('heading', { name })` |
| **Formulários** | Labels i18n em `DsInput` / `DsSelect` | `getByLabel('Nome')`, etc. |

**Não** faz parte do contrato: classes `ds-*`, markup interno do `q-input`, slots do `q-table`.

---

## Implementação interna (não contrato)

| Camada | Exemplos |
|--------|----------|
| Quasar | `q-table__bottom`, `q-btn`, ícones de seta, `QPagination` numérico ausente no QTable |
| Wrappers DS | Estrutura de `DsCard`, `DsDataTable` como pass-through |
| Composables | `useEquipeList` — testar efeito na UI, não chamadas internas |

Mudanças nessas camadas **não** devem exigir alteração de E2E se o comportamento funcional for preservado.

---

## PKG-FE-06 — checklist de estabilização (E2E-01 + E2E-02)

Ao fechar **PKG-FE-06**:

1. Gate PKG + `E2E_VALIDATION=1` (`16-frontend-validation-gates.md`).
2. Todo **AT-FE-*** da Feature com spec em `test/e2e/<feature>/`.
3. Revisão **E2E-02**: sem locators proibidos; paginação e sort alinhados aos mocks.
4. Suíte **completa** `yarn test:e2e` verde (`PLAYWRIGHT_SINGLE_WORKER=1` no runner de evidência).

Registrar violações corrigidas na seção **Correções aplicadas** do `VALIDATION SUMMARY`.

---

## Rastreabilidade

| Artefato | Uso |
|----------|-----|
| `specs/features/*/acceptance-tests.md` | Cenários Given/When/Then |
| `frontend/test/e2e/support/*-api-mock.ts` | Dados e sort alinhados à API |
| `construction/17-frontend-e2e-behavior-policy.md` | Política E2E-02 (este documento) |
| `construction/16-frontend-validation-gates.md` | Gates BUILD-02 / E2E-01 |

---

## Referências

- `construction/04-construction-rules.md` — **R-27** (E2E-02)
- `construction/14-framework-decisions-v4.1.md` — DL-EF-4.2-011
- `construction/history/framework-evolution-e2e-behavior-2026-07-17.md`
