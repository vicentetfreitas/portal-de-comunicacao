# Use Cases

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — somente navegação/leitura) |
| Versão | 1.1 |
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

Casos de uso da navegação hierárquica de pastas/documentos. CRUD Base do template não
se aplica — só leitura/navegação. Ator comum: **colaborador autenticado**; visibilidade
= `PERMISSAO_PASTA` (`LEITURA`) compatível com o Contexto Ativo (herda `RF-DOCUMENTO-003`).

---

## UC-DOC-NAV-001 — Entrar numa pasta e ver seu conteúdo

### Prioridade

Must

### Pré-condições

O colaborador está na página "Arquivos e Documentos" da sua Área (Contexto Ativo).

### Fluxo Principal

1. O sistema apresenta as **raízes visíveis** (pastas sem pai, ou cujo pai não é
   visível — `specification.md` § Raízes) como cards/linhas.
2. O colaborador seleciona uma pasta.
3. O sistema navega para dentro dela e apresenta: as subpastas ativas + os documentos
   daquele nível (`STA_DOCUMENTO != EXPIRADO`).
4. O `breadcrumb` passa a mostrar o caminho até a pasta atual.

### Fluxos de Exceção

- **FE-001:** Pasta sem subpasta nem documento → estado vazio ("pasta vazia").
- **FE-002:** Tentativa de abrir (via URL) uma pasta sem grant compatível → `403`
  (ver UC-DOC-NAV-006).

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-001

### Critérios de Aceitação Relacionados

- AT-DOC-NAV-001

---

## UC-DOC-NAV-002 — Navegar pelo caminho (breadcrumb) e voltar

### Prioridade

Must

### Fluxo Principal

1. Estando dentro de uma subpasta, o colaborador clica num segmento anterior do
   `breadcrumb` (ou na ação "voltar").
2. O sistema reposiciona o explorador naquele nível e atualiza o conteúdo e a URL.

### Fluxos de Exceção

- **FE-001:** A pasta atual é uma raiz órfã → o `breadcrumb` começa nela; não há
  segmento anterior clicável para o ancestral sem grant.

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-002

### Critérios de Aceitação Relacionados

- AT-DOC-NAV-002

---

## UC-DOC-NAV-003 — Navegar pela árvore de pastas

### Prioridade

Should

### Fluxo Principal

1. O colaborador abre o painel de árvore.
2. O sistema apresenta a hierarquia das pastas **visíveis**, com nós recolhidos por
   padrão a partir das raízes.
3. O colaborador expande nós e seleciona uma pasta.
4. O explorador posiciona-se nessa pasta (mesmo efeito de UC-DOC-NAV-001).

### Fluxos de Exceção

- **FE-001:** Subárvore inteira sem pastas visíveis → o nó não aparece.

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-003

### Critérios de Aceitação Relacionados

- AT-DOC-NAV-003

---

## UC-DOC-NAV-004 — Buscar pasta ou documento pelo nome

### Prioridade

Should

### Fluxo Principal

1. O colaborador digita um trecho no campo "Buscar documentos, pastas...".
2. O sistema apresenta as pastas e documentos **visíveis** cujo nome contém o trecho,
   indicando em que pasta cada resultado está.
3. Selecionar um resultado de pasta abre-a no explorador; um resultado de documento
   posiciona o explorador na pasta do documento e o destaca.

### Fluxos de Exceção

- **FE-001:** Nenhum resultado → estado vazio ("nada encontrado").
- **FE-002:** Busca nunca retorna item fora do escopo visível ao Contexto Ativo.

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-004

### Critérios de Aceitação Relacionados

- AT-DOC-NAV-004

---

## UC-DOC-NAV-005 — Alternar grade / lista

### Prioridade

Could

### Fluxo Principal

1. O colaborador aciona o controle grade/lista.
2. O sistema re-renderiza o conteúdo do nível atual no outro layout e persiste a
   preferência (por dispositivo — `DEC-FA-005`).

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-005

### Critérios de Aceitação Relacionados

- AT-DOC-NAV-005

---

## UC-DOC-NAV-006 — Abrir a navegação por link direto (deep-link)

### Prioridade

Must

### Fluxo Principal

1. O colaborador acessa uma URL que carrega o identificador de uma pasta e o modo de
   visualização.
2. O sistema resolve o identificador contra a visibilidade do Contexto Ativo e abre o
   explorador naquela pasta, no modo indicado.

### Fluxos de Exceção

- **FE-001:** Identificador de pasta inexistente → `404` (ou redireciona para a raiz
   com aviso — decisão de UX).
- **FE-002:** Pasta existe mas sem grant compatível → `403` explícito; a UI mostra
   "sem acesso", nunca o conteúdo.

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-006

### Critérios de Aceitação Relacionados

- AT-DOC-NAV-006

---

## UC-DOC-NAV-007 — Restrição de visibilidade (transversal)

### Prioridade

Must

### Fluxo Principal

Toda listagem (explorador, árvore, busca, breadcrumb) filtra por `PERMISSAO_PASTA`
(`LEITURA`) compatível com o Contexto Ativo; requisição direta a recurso sem grant →
`403` explícito.

### Requisitos Funcionais Relacionados

- RF-DOC-NAV-007

### Regras de Negócio Relacionadas

- `BR-012`, `BR-018`, `BR-020`

### Critérios de Aceitação Relacionados

- AT-DOC-NAV-007

---

# Casos de Uso Fora do Escopo

Criar/renomear/mover/arquivar pasta ou documento (`FT-DOCUMENTO-GESTAO`);
upload/download/nova versão (`FT-DOCUMENTO` / `FT-DOCUMENTO-UPLOAD` /
`FT-DOCUMENTO-GESTAO`); navegar pastas de outras Áreas via Federação (fora do escopo por
`decisions.md` D-03 — iteração futura).

---

# Matriz de Rastreabilidade

| Caso de Uso | RF | API | Teste |
|--------------|----|----|--------|
| UC-DOC-NAV-001 | RF-DOC-NAV-001 | GET /api/v1/pastas (+ `pastaPaiId`) | AT-DOC-NAV-001 |
| UC-DOC-NAV-002 | RF-DOC-NAV-002 | (cliente — deriva do mesmo payload) | AT-DOC-NAV-002 |
| UC-DOC-NAV-003 | RF-DOC-NAV-003 | GET /api/v1/pastas (+ `pastaPaiId`) | AT-DOC-NAV-003 |
| UC-DOC-NAV-004 | RF-DOC-NAV-004 | (cliente no MVP — D-04) | AT-DOC-NAV-004 |
| UC-DOC-NAV-005 | RF-DOC-NAV-005 | (nenhuma — preferência local) | AT-DOC-NAV-005 |
| UC-DOC-NAV-006 | RF-DOC-NAV-006 | GET /api/v1/pastas | AT-DOC-NAV-006 |
| UC-DOC-NAV-007 | RF-DOC-NAV-007 | (transversal) | AT-DOC-NAV-007 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — UC-DOC-NAV-001..007 |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Decisões fechadas (`decisions.md` v1.1) — removida a menção "decisão pendente D-03" dos casos fora do escopo; conteúdo dos UC inalterado |
