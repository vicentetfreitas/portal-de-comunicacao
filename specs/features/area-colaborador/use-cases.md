# Use Cases

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

Este documento descreve os casos de uso da Feature FT-AREA-COLABORADOR: os fluxos de leitura do colaborador autenticado sobre a Área do seu Contexto Ativo, consumindo exclusivamente as APIs já `APPROVED` de `FT-AREA` e `FT-EQUIPE`.

Não descreve detalhes de implementação.

---

# Convenções

## Identificação

```text
UC-AREA-COLAB-001
UC-AREA-COLAB-002
UC-AREA-COLAB-003
```

## Fluxos Alternativos

```text
FA-001
FA-002
```

## Fluxos de Exceção

```text
FE-001
FE-002
```

---

# UC-AREA-COLAB-001 — Visualizar hub da Área

### Objetivo

Permitir que o colaborador autenticado acesse um hub com atalhos para as sub-seções da Área do seu Contexto Ativo.

### Prioridade

Must

### Complexidade

Baixa

### Atores

Colaborador autenticado

### Pré-condições

- Sessão válida (`FT-AUTH`/`FT-SESSION`).
- Contexto Ativo resolvido, com Área associada.

### Fluxo Principal

1. O colaborador acessa a rota de hub da Área.
2. O sistema identifica a Área do Contexto Ativo.
3. O sistema exibe o hub com atalhos "Equipe" e "Arquivos e Documentos".

### Fluxos Alternativos

**FA-001 — Selecionar atalho "Equipe":** o colaborador navega para `UC-AREA-COLAB-003`.

**FA-002 — Selecionar atalho "Arquivos e Documentos":** o colaborador navega para `FT-DOCUMENTO` (fora do escopo desta Feature).

### Fluxos de Exceção

**FE-001 — Contexto Ativo sem Área resolvida:** o sistema não exibe o hub; o tratamento de Contexto Ativo ausente pertence à infraestrutura de sessão (`FT-SESSION`), não a esta Feature.

### Pós-condições

Hub exibido, com atalhos prontos para navegação às sub-seções.

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-001

### Regras de Negócio Relacionadas

- Nenhuma regra específica desta Feature. Resolução de Contexto Ativo e sessão herdadas de `FT-AUTH`/`FT-SESSION`.

### Critérios de Aceitação Relacionados

- AT-AREA-COLAB-001
- AT-AREA-COLAB-002

---

# UC-AREA-COLAB-002 — Visualizar dados da Área

### Objetivo

Permitir que o colaborador autenticado visualize nome e descrição da Área do seu Contexto Ativo.

### Prioridade

Must

### Complexidade

Baixa

### Atores

Colaborador autenticado

### Pré-condições

- Sessão válida.
- Contexto Ativo resolvido, com Área associada.

### Fluxo Principal

1. O sistema invoca `GET /api/v1/areas/{id}` (`FT-AREA`, `APPROVED`) com o identificador da Área do Contexto Ativo.
2. A API retorna `AreaResponse` (nome, descrição).
3. O sistema exibe nome e descrição da Área.

### Fluxos Alternativos

Nenhum.

### Fluxos de Exceção

**FE-001 — Área inexistente (HTTP 404):** o sistema exibe estado de erro, sem interromper a navegação para as demais sub-seções.

**FE-002 — Falha de comunicação com a API (timeout, HTTP 5xx):** o sistema exibe estado de erro genérico.

### Pós-condições

Dados da Área exibidos, ou estado de erro tratado.

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-002

### Regras de Negócio Relacionadas

- Nenhuma regra específica desta Feature. Regras de existência/consistência da Área pertencem a `FT-AREA`.

### Critérios de Aceitação Relacionados

- AT-AREA-COLAB-003
- AT-AREA-COLAB-004

---

# UC-AREA-COLAB-003 — Visualizar equipe(s) da Área

### Objetivo

Permitir que o colaborador autenticado visualize as equipes vinculadas à Área do seu Contexto Ativo.

### Prioridade

Must

### Complexidade

Baixa

### Atores

Colaborador autenticado

### Pré-condições

- Sessão válida.
- Contexto Ativo resolvido, com Área associada.

### Fluxo Principal

1. O sistema invoca `GET /api/v1/equipes` (`FT-EQUIPE`, `APPROVED`) filtrado por `areaId`.
2. A API retorna `PageResponse<EquipeResponse>` (nome, descrição por equipe).
3. O sistema lista as equipes vinculadas à Área.

### Fluxos Alternativos

**FA-001 — Área sem equipes vinculadas:** o sistema exibe estado de coleção vazia, sem erro.

### Fluxos de Exceção

**FE-001 — Falha de comunicação com a API (timeout, HTTP 5xx):** o sistema exibe estado de erro genérico.

### Pós-condições

Lista de equipes da Área exibida (ou estado vazio, ou estado de erro tratado).

### Requisitos Funcionais Relacionados

- RF-AREA-COLAB-003

### Regras de Negócio Relacionadas

- Nenhuma regra específica desta Feature. Regras de existência/consistência de Equipe pertencem a `FT-EQUIPE`.

### Observações

Roster de membros individuais (nome, cargo, e-mail, telefone) está fora do escopo — ver "Decisões de produto" em `specification.md`. Este caso de uso cobre exclusivamente dados de Equipe (`EquipeResponse`), não pessoas.

### Critérios de Aceitação Relacionados

- AT-AREA-COLAB-005
- AT-AREA-COLAB-006
- AT-AREA-COLAB-007

---

# Casos de Uso Opcionais

Nenhum além dos três acima — Feature exclusivamente de leitura, sem operações de importar/exportar/aprovar/publicar/arquivar/restaurar/sincronizar.

---

# Matriz de Rastreabilidade

| Caso de Uso | RF | RN | RNF | API | Teste |
|--------------|----|----|-----|-----|--------|
| UC-AREA-COLAB-001 | RF-AREA-COLAB-001 | — | — | — (composição de rota) | AT-AREA-COLAB-001, AT-AREA-COLAB-002 |
| UC-AREA-COLAB-002 | RF-AREA-COLAB-002 | — | — | GET /api/v1/areas/{id} | AT-AREA-COLAB-003, AT-AREA-COLAB-004 |
| UC-AREA-COLAB-003 | RF-AREA-COLAB-003 | — | — | GET /api/v1/equipes | AT-AREA-COLAB-005, AT-AREA-COLAB-006, AT-AREA-COLAB-007 |

---

# Critérios de Conformidade

Este documento é considerado conforme quando:

- todos os casos de uso estiverem rastreados;
- cada caso de uso possuir pelo menos um requisito funcional associado;
- todos os fluxos estiverem documentados quando aplicáveis;
- não existirem casos de uso sem critérios de aceitação;
- mantiver consistência com `specification.md` e com os contratos `APPROVED` de `FT-AREA`/`FT-EQUIPE` (nenhum contrato novo introduzido por esta Feature).

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-20 | Engineering Framework | Criação — fechamento documental DoR-Spec |
