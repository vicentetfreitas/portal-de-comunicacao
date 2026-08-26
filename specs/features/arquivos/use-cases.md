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
2. Sistema identifica o Contexto Ativo (federação, singular, área, equipe).
3. Sistema retorna as pastas com `PERMISSAO_PASTA` (`TIP_ACESSO=LEITURA`) para algum desses níveis, cada uma com seus documentos `ATIVO`/`ARQUIVADO` (ver UC-DOCUMENTO-004).
4. Sistema exibe a listagem.

### Fluxos de Exceção

- **FE-001:** Nenhuma pasta com permissão para o Contexto Ativo → sistema exibe coleção vazia (não é erro).

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

Documento existe, está `ATIVO` ou `ARQUIVADO`, e sua pasta possui `PERMISSAO_PASTA` (`TIP_ACESSO=DOWNLOAD`) para algum nível do Contexto Ativo do colaborador.

### Fluxo Principal

1. Colaborador seleciona um documento na listagem.
2. Sistema valida a permissão de download da pasta do documento (ver UC-DOCUMENTO-003).
3. Sistema resolve a versão atual (`DOCUMENTO_VERSAO.FLG_VERSAO_ATUAL='S'`) e retorna o binário via Object Storage (DEC-013 — Backend nunca expõe `URL_ARQUIVO` diretamente ao cliente, ADR-004).

### Fluxos de Exceção

- **FE-001:** Documento inexistente ou `EXPIRADO` → 404 (ver UC-DOCUMENTO-004).
- **FE-002:** Pasta sem permissão de `DOWNLOAD` para o Contexto Ativo → ver UC-DOCUMENTO-003 (403).

### Pós-condições

Download do arquivo iniciado.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-002

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-002

---

## UC-DOCUMENTO-003 — Negar acesso a pasta/documento sem permissão

### Objetivo

Garantir que nenhuma listagem ou download exponha pasta/documento para o qual não existe `PERMISSAO_PASTA` correspondente a algum nível do Contexto Ativo do colaborador.

### Prioridade

Must

### Complexidade

Média (multi-nível: Federação, Singular, Área, Equipe — não uma comparação simples)

### Atores

Colaborador autenticado.

### Pré-condições

Colaborador tenta listar ou baixar recurso cuja pasta não possui `PERMISSAO_PASTA` para nenhum nível do seu Contexto Ativo (ex.: manipulação direta de identificador, ou pasta restrita a outra Área/Equipe).

### Fluxo Principal

1. Colaborador solicita pasta/documento por identificador.
2. Sistema busca `PERMISSAO_PASTA` da pasta filtrando por `TIP_DESTINATARIO`/`COD_DESTINATARIO` compatível com `federationId`, `singularId`, `areaId` ou `teamId` do Contexto Ativo.
3. Nenhum grant compatível com o `TIP_ACESSO` necessário (`LEITURA` ou `DOWNLOAD`) → sistema nega o acesso.

### Fluxos de Exceção

- **FE-001:** Nenhum grant compatível → 403 explícito (nunca filtragem silenciosa/404 disfarçado).

### Pós-condições

Acesso negado e registrado (auditoria padrão da plataforma).

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-003

### Regras de Negócio Relacionadas

- `BR-012`, `BR-018`, `BR-020` (`docs/domain/09-business-rules.md`)

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-003

---

## UC-DOCUMENTO-004 — Ocultar documento expirado

### Objetivo

Garantir que documento com `STA_DOCUMENTO = 'EXPIRADO'` nunca apareça na listagem nem seja baixável, mesmo com permissão de pasta válida.

### Prioridade

Must

### Complexidade

Baixa

### Atores

Colaborador autenticado.

### Pré-condições

Documento com `STA_DOCUMENTO = 'EXPIRADO'` existe na pasta consultada.

### Fluxo Principal

1. Sistema resolve os documentos de uma pasta (listagem) ou um documento específico (download).
2. Sistema filtra por `STA_DOCUMENTO IN ('ATIVO', 'ARQUIVADO')`.
3. Documento `EXPIRADO` não aparece na listagem; download retorna 404.

### Pós-condições

Documentos `ATIVO`/`ARQUIVADO` visíveis; `EXPIRADO` invisível.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-004

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-004

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
| UC-DOCUMENTO-004 | RF-DOCUMENTO-004 | GET /api/v1/pastas, GET /api/v1/documentos/{id}/download | AT-DOCUMENTO-004 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — 3 UCs, CRUD Base não aplicável (somente leitura) |
| 1.2 | 2026-08-26 | Claude Code (Specify) | Reconciliação com schema físico real: UC-003 revisado para permissão multi-nível (`PERMISSAO_PASTA`); UC-004 novo (ocultar `EXPIRADO`) |
