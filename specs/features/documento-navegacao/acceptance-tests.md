# Acceptance Tests

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

Critérios de aceitação da navegação hierárquica. Estratégia de testes:
`docs/implementation/08-testing-strategy.md`.

Contexto comum: colaborador autenticado num Contexto Ativo (nível X); existe a
hierarquia `Raiz > Sub A > Sub A1` e um documento `doc1` em `Sub A`, todos com
`PERMISSAO_PASTA` (`LEITURA`, nível X). `Sub B` só tem grant para outro nível
(não visível).

---

## AT-DOC-NAV-001 — Explorar o conteúdo de uma pasta

### Cenário — nível inicial mostra as raízes

**Given** as pastas visíveis são `Raiz` (sem pai), `Sub A` (pai `Raiz`), `Sub A1`
(pai `Sub A`)
**When** o colaborador abre "Arquivos e Documentos"
**Then** vê **apenas `Raiz`** no nível inicial (as demais são descendentes)

### Cenário — entrar numa pasta

**When** seleciona `Raiz`
**Then** vê `Sub A` (subpasta) e nenhum documento; o `breadcrumb` mostra `Raiz`
**When** seleciona `Sub A`
**Then** vê `Sub A1` e `doc1`; `breadcrumb` = `Raiz > Sub A`

### Cenário — raiz órfã

**Given** `Sub A1` é visível mas seu pai `Sub A` **não** é visível ao Contexto Ativo
**When** o colaborador abre "Arquivos e Documentos"
**Then** `Sub A1` aparece como raiz; o `breadcrumb` dela começa em `Sub A1` (não cita `Sub A`)

### Cenário — pasta vazia

**When** entra numa pasta sem subpasta ativa nem documento
**Then** estado vazio ("pasta vazia")

---

## AT-DOC-NAV-002 — Caminho (breadcrumb) e voltar

**Given** o colaborador está em `Raiz > Sub A > Sub A1`
**When** clica no segmento `Raiz` do `breadcrumb`
**Then** o explorador volta para `Raiz`, o conteúdo e a URL são atualizados
**When** usa "voltar" a partir de `Sub A`
**Then** vai para `Raiz`

---

## AT-DOC-NAV-003 — Árvore de pastas

### Cenário — hierarquia visível

**When** abre o painel de árvore
**Then** vê `Raiz` como nó de topo; expandindo, vê `Sub A`; expandindo, vê `Sub A1`;
**não** vê `Sub B`

### Cenário — seleção sincroniza com o explorador

**When** seleciona `Sub A` na árvore
**Then** o explorador posiciona-se em `Sub A` (mesmo resultado de AT-DOC-NAV-001)

---

## AT-DOC-NAV-004 — Buscar

### Cenário — encontra por nome

**When** busca por "A1"
**Then** o resultado inclui `Sub A1`, indicando que está em `Raiz > Sub A`

### Cenário — não vaza fora do escopo

**Given** existe `Sub B` (não visível) cujo nome contém "B"
**When** busca por "B"
**Then** `Sub B` **não** aparece nos resultados

### Cenário — nada encontrado

**When** busca por um trecho sem correspondência
**Then** estado vazio ("nada encontrado")

---

## AT-DOC-NAV-005 — Alternar grade / lista

**When** o colaborador alterna para "lista"
**Then** o mesmo conteúdo é re-renderizado em lista
**When** recarrega a página
**Then** a preferência "lista" é mantida (persistência por dispositivo — `DEC-FA-005`)

---

## AT-DOC-NAV-006 — Deep-link

### Cenário — link reabre no mesmo ponto

**Given** uma URL que aponta para a pasta `Sub A` em modo árvore
**When** o colaborador acessa essa URL
**Then** o explorador abre em `Sub A`, com o painel de árvore ativo

### Cenário — link para pasta sem acesso

**Given** uma URL que aponta para `Sub B` (sem grant para o Contexto Ativo)
**When** o colaborador acessa essa URL
**Then** a UI mostra "sem acesso" (`403` na resolução) — nunca o conteúdo de `Sub B`

### Cenário — link para pasta inexistente

**When** a URL aponta para um id de pasta que não existe
**Then** `404` (ou redireciona para a raiz com aviso — conforme decisão de UX)

---

## AT-DOC-NAV-007 — Restrição de visibilidade (transversal)

**Given** `Sub B` só tem grant para outro nível
**Then** `Sub B` nunca aparece no explorador, na árvore, na busca nem em nenhum
`breadcrumb`; requisição direta a `Sub B` → `403` explícito (nunca `404` disfarçado)

---

# Cenários Negativos (transversais)

- Não autenticado → `401`.
- Sem Contexto Ativo resolvido → padrão `FT-PRIMEIRO-ACESSO`/`FT-SESSION`.
- Documento `EXPIRADO` → nunca aparece na navegação (`RF-DOCUMENTO-004`, herdado).

---

# Matriz de Rastreabilidade

| Teste | RF | UC |
|--------|----|----|
| AT-DOC-NAV-001 | RF-DOC-NAV-001 | UC-DOC-NAV-001 |
| AT-DOC-NAV-002 | RF-DOC-NAV-002 | UC-DOC-NAV-002 |
| AT-DOC-NAV-003 | RF-DOC-NAV-003 | UC-DOC-NAV-003 |
| AT-DOC-NAV-004 | RF-DOC-NAV-004 | UC-DOC-NAV-004 |
| AT-DOC-NAV-005 | RF-DOC-NAV-005 | UC-DOC-NAV-005 |
| AT-DOC-NAV-006 | RF-DOC-NAV-006 | UC-DOC-NAV-006 |
| AT-DOC-NAV-007 | RF-DOC-NAV-007 | UC-DOC-NAV-007 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — AT-DOC-NAV-001..007 |
