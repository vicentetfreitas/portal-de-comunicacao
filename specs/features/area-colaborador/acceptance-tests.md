# Acceptance Tests

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Feature de leitura, sobre backend já aprovado) |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-AREA-COLABORADOR |
| Feature | Área — Visão do Colaborador |
| Domínio | AREA-COLAB |

---

# Objetivo

Este documento define os critérios de aceitação da Feature FT-AREA-COLABORADOR.

Especifica apenas os critérios funcionais. Estratégias de teste, níveis, ferramentas e cobertura mínima são definidos em `docs/implementation/08-testing-strategy.md` — não duplicados aqui.

---

# AT-AREA-COLAB-001 — Exibir hub da Área

### Prioridade

Must

### Tipo

Happy Path

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-001

### Casos de Uso Relacionados

- UC-AREA-COLAB-001

### Pré-condições

Colaborador autenticado, com Contexto Ativo resolvido e Área associada.

### Cenário

**Given** colaborador autenticado com Contexto Ativo resolvido
**When** acessa a rota de hub da Área
**Then** o sistema exibe atalhos para "Equipe" e "Arquivos e Documentos"

### Resultado Esperado

Hub da Área exibido com os dois atalhos.

---

# AT-AREA-COLAB-002 — Bloquear acesso sem sessão válida

### Prioridade

Must

### Tipo

Authorization

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-001

### Casos de Uso Relacionados

- UC-AREA-COLAB-001

### Pré-condições

Nenhuma sessão válida.

### Cenário

**Given** usuário não autenticado
**When** tenta acessar qualquer rota desta Feature
**Then** o sistema nega o acesso conforme política já definida por `FT-AUTH`/`FT-SESSION`

### Resultado Esperado

Acesso negado; nenhum dado da Área ou Equipe exposto.

### Observações

Este critério valida a integração com o guard de sessão já existente — não introduz nova regra de autorização.

---

# AT-AREA-COLAB-003 — Exibir dados da Área

### Prioridade

Must

### Tipo

Happy Path

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-002

### Casos de Uso Relacionados

- UC-AREA-COLAB-002

### Pré-condições

Área do Contexto Ativo existe e está acessível via `GET /api/v1/areas/{id}`.

### Cenário

**Given** colaborador autenticado com Contexto Ativo resolvido e Área existente
**When** acessa a página de visualização da Área
**Then** o sistema exibe nome e descrição obtidos de `GET /api/v1/areas/{id}`

### Resultado Esperado

Nome e descrição da Área exibidos, consistentes com a resposta da API.

---

# AT-AREA-COLAB-004 — Área inexistente

### Prioridade

Should

### Tipo

Negative

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-002

### Casos de Uso Relacionados

- UC-AREA-COLAB-002

### Pré-condições

Identificador de Área do Contexto Ativo não corresponde a registro existente.

### Cenário

**Given** `GET /api/v1/areas/{id}` retorna HTTP 404
**When** o sistema carrega a página de visualização da Área
**Then** o sistema exibe estado de erro apropriado, sem quebrar a navegação

### Resultado Esperado

Estado de erro tratado; demais sub-seções da Feature permanecem acessíveis.

---

# AT-AREA-COLAB-005 — Listar equipes da Área

### Prioridade

Must

### Tipo

Happy Path

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-003

### Casos de Uso Relacionados

- UC-AREA-COLAB-003

### Pré-condições

Área com uma ou mais equipes vinculadas.

### Cenário

**Given** Área com equipes vinculadas
**When** acessa a página de Equipe(s) da Área
**Then** o sistema lista as equipes (nome, descrição) obtidas de `GET /api/v1/equipes` filtrado por `areaId`

### Resultado Esperado

Lista de equipes exibida, consistente com a resposta da API.

---

# AT-AREA-COLAB-006 — Área sem equipes vinculadas

### Prioridade

Should

### Tipo

Boundary

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-003

### Casos de Uso Relacionados

- UC-AREA-COLAB-003

### Pré-condições

Área sem equipes vinculadas.

### Cenário

**Given** `GET /api/v1/equipes?areaId=` retorna coleção vazia
**When** acessa a página de Equipe(s) da Área
**Then** o sistema exibe estado de coleção vazia, sem erro

### Resultado Esperado

Estado vazio exibido de forma explícita, sem tratamento de erro.

---

# AT-AREA-COLAB-007 — Falha de comunicação com a API

### Prioridade

Should

### Tipo

Negative

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-002
- RF-AREA-COLAB-003

### Casos de Uso Relacionados

- UC-AREA-COLAB-002
- UC-AREA-COLAB-003

### Pré-condições

API de áreas ou de equipes indisponível (timeout ou HTTP 5xx).

### Cenário

**Given** `GET /api/v1/areas/{id}` ou `GET /api/v1/equipes` retorna erro de comunicação
**When** o sistema tenta carregar os dados correspondentes
**Then** o sistema exibe estado de erro genérico, sem quebrar a aplicação

### Resultado Esperado

Estado de erro tratado; aplicação permanece navegável.

---

# Cenários Negativos Cobertos

- usuário não autenticado (AT-AREA-COLAB-002);
- recurso inexistente (AT-AREA-COLAB-004);
- coleção vazia (AT-AREA-COLAB-006);
- falha de comunicação com API (AT-AREA-COLAB-007).

Não aplicável a esta Feature (somente leitura, sem escrita): payload inválido, conflito de dados, violação de regra de negócio de escrita.

---

# Matriz de Rastreabilidade

| Teste | RF | UC | RN |
|--------|----|----|----|
| AT-AREA-COLAB-001 | RF-AREA-COLAB-001 | UC-AREA-COLAB-001 | — |
| AT-AREA-COLAB-002 | RF-AREA-COLAB-001 | UC-AREA-COLAB-001 | — |
| AT-AREA-COLAB-003 | RF-AREA-COLAB-002 | UC-AREA-COLAB-002 | — |
| AT-AREA-COLAB-004 | RF-AREA-COLAB-002 | UC-AREA-COLAB-002 | — |
| AT-AREA-COLAB-005 | RF-AREA-COLAB-003 | UC-AREA-COLAB-003 | — |
| AT-AREA-COLAB-006 | RF-AREA-COLAB-003 | UC-AREA-COLAB-003 | — |
| AT-AREA-COLAB-007 | RF-AREA-COLAB-002, RF-AREA-COLAB-003 | UC-AREA-COLAB-002, UC-AREA-COLAB-003 | — |

---

# Critérios de Conformidade

Este documento é considerado conforme quando:

- todos os requisitos funcionais possuírem pelo menos um critério de aceitação;
- todos os critérios estiverem rastreados;
- não existirem cenários sem requisito associado;
- mantiver consistência com `specification.md` e `use-cases.md`;
- não duplicar padrões definidos em `docs/implementation/08-testing-strategy.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-20 | Engineering Framework | Criação — fechamento documental DoR-Spec |
