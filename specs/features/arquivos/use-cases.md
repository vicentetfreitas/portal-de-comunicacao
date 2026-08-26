# Use Cases

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — somente leitura, sem Cadastrar/Atualizar/Alterar Status) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO |
| Feature | Arquivos e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Casos de uso da Feature de leitura de pastas/arquivos vinculados à Área do Contexto Ativo. CRUD Base do template não se aplica integralmente — feature é somente leitura (ver `specification.md` § Escopo).

---

## UC-DOCUMENTO-001 — Listar pastas e arquivos da Área

### Objetivo

Exibir ao colaborador as pastas e arquivos vinculados à Área do seu Contexto Ativo.

### Prioridade

Must

### Complexidade

Baixa

### Atores

Colaborador autenticado.

### Pré-condições

Colaborador possui Contexto Ativo resolvido (Área definida).

### Fluxo Principal

1. Colaborador acessa a tela de Arquivos.
2. Sistema identifica a Área do Contexto Ativo.
3. Sistema retorna as pastas vinculadas a essa Área, cada uma com seus arquivos (título, formato).
4. Sistema exibe a listagem.

### Fluxos de Exceção

- **FE-001:** Área sem pastas cadastradas → sistema exibe coleção vazia (não é erro).

### Pós-condições

Colaborador visualiza as pastas/arquivos da própria Área.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-001

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-001

---

## UC-DOCUMENTO-002 — Baixar arquivo

### Objetivo

Permitir ao colaborador baixar um arquivo específico de uma pasta da própria Área.

### Prioridade

Must

### Complexidade

Baixa

### Atores

Colaborador autenticado.

### Pré-condições

Arquivo existe e está vinculado a uma pasta da Área do Contexto Ativo do colaborador.

### Fluxo Principal

1. Colaborador seleciona um arquivo na listagem.
2. Sistema valida que o arquivo pertence à Área do Contexto Ativo do colaborador (ver UC-DOCUMENTO-003).
3. Sistema retorna o binário do arquivo (via Object Storage, DEC-013 — Backend nunca expõe o binário diretamente do storage, ADR-004).

### Fluxos de Exceção

- **FE-001:** Arquivo inexistente → 404.
- **FE-002:** Arquivo pertence a outra Área → ver UC-DOCUMENTO-003 (403).

### Pós-condições

Download do arquivo iniciado.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-002

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-002

---

## UC-DOCUMENTO-003 — Negar acesso a arquivo/pasta fora da Área

### Objetivo

Garantir que nenhuma listagem ou download exponha pastas/arquivos de Área diferente da do Contexto Ativo do colaborador.

### Prioridade

Must

### Complexidade

Baixa

### Atores

Colaborador autenticado.

### Pré-condições

Colaborador tenta listar ou baixar recurso de Área diferente da própria (ex.: manipulação direta de identificador).

### Fluxo Principal

1. Colaborador solicita pasta/arquivo por identificador.
2. Sistema compara a Área do recurso com a Área do Contexto Ativo.
3. Áreas divergem → sistema nega o acesso.

### Fluxos de Exceção

- **FE-001:** Áreas divergem → 403 explícito (nunca filtragem silenciosa/404 disfarçado).

### Pós-condições

Acesso negado e registrado (auditoria padrão da plataforma).

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-003

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-003

---

# Casos de Uso Fora do Escopo

Cadastrar, Atualizar, Alterar Status (CRUD Base do template) — não se aplicam; Feature é somente leitura (`specification.md` § Escopo, decisão de produto 2026-08-26).

---

# Matriz de Rastreabilidade

| Caso de Uso | RF | API | Teste |
|--------------|----|----|--------|
| UC-DOCUMENTO-001 | RF-DOCUMENTO-001 | GET /api/v1/pastas | AT-DOCUMENTO-001 |
| UC-DOCUMENTO-002 | RF-DOCUMENTO-002 | GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-002 |
| UC-DOCUMENTO-003 | RF-DOCUMENTO-003 | GET /api/v1/pastas, GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-003 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — 3 UCs, CRUD Base não aplicável (somente leitura) |
