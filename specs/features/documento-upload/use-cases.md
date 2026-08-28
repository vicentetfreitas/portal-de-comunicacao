# Use Cases

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — só Cadastrar; sem Atualizar/Alterar Status/Excluir) |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-UPLOAD |
| Feature | Upload de Arquivos e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Casos de uso do upload de arquivo por administrador escopado. CRUD Base do template não se aplica integralmente — só "Cadastrar" (criar documento); sem Atualizar/Alterar Status/Excluir nesta entrega (ver `specification.md` § Escopo).

---

## UC-DOC-UPLOAD-001 — Enviar arquivo para pasta existente

### Objetivo

Permitir que um colaborador com atribuição ativa `ADMINISTRADOR` envie um novo arquivo para uma pasta já existente, à qual essa atribuição tem grant de edição.

### Prioridade

Must

### Complexidade

Média (cria três registros relacionados — `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO` — e grava o binário no Object Storage)

### Atores

Colaborador com atribuição ativa `ADMINISTRADOR` (Federação, Singular, Área ou Equipe).

### Pré-condições

- Colaborador possui atribuição ativa (Contexto Ativo) com papel `ADMINISTRADOR`.
- A pasta alvo existe e possui `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) para o nível dessa atribuição.

### Fluxo Principal

1. Administrador seleciona uma pasta (já listada via `FT-DOCUMENTO`, `GET /api/v1/pastas`) e envia um arquivo com título. Não escolhe categoria nem escopo.
2. Sistema valida a atribuição ativa (papel `ADMINISTRADOR`) e o grant `EDICAO` da pasta para o nível dessa atribuição (ver UC-DOC-UPLOAD-002).
3. Sistema deriva a categoria do `TIP_MIME` do arquivo (`Documentos`/`Imagens`/`Vídeos`/`Outros`), grava o binário no Object Storage (DEC-013) e cria, de forma atômica, `ARQUIVO_BINARIO`, `DOCUMENTO` (`STA_DOCUMENTO='ATIVO'`, `COD_CATEGORIA_DOCUMENTAL` = categoria derivada, `COD_COLABORADOR` = colaborador autenticado) e `DOCUMENTO_VERSAO` (`NUM_VERSAO=1`, `FLG_VERSAO_ATUAL='S'`, `COD_COLABORADOR` = mesmo colaborador).
4. Sistema confirma o upload; o documento passa a aparecer na listagem (`GET /api/v1/pastas`) para todos com `PERMISSAO_PASTA` de leitura na pasta.

### Fluxos de Exceção

- **FE-001:** Pasta inexistente → `404` (ver UC-DOC-UPLOAD-003).
- **FE-002:** Sem papel `ADMINISTRADOR` ativo ou sem grant `EDICAO` compatível → `403` (ver UC-DOC-UPLOAD-002).
- **FE-003:** Falha ao gravar no Object Storage → erro explícito, nenhum registro parcial (`DOCUMENTO`/`DOCUMENTO_VERSAO`/`ARQUIVO_BINARIO`) persistido — operação atômica.

### Pós-condições

Documento visível na listagem da pasta para quem tem `PERMISSAO_PASTA` de leitura, imediatamente após o upload.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-001

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-001

---

## UC-DOC-UPLOAD-002 — Negar upload sem papel/grant compatível

### Objetivo

Garantir que somente colaboradores com atribuição ativa `ADMINISTRADOR` e grant `EDICAO` compatível possam enviar arquivos.

### Prioridade

Must

### Complexidade

Média (mesma checagem multi-nível de `PERMISSAO_PASTA` já usada por `FT-DOCUMENTO`, mais a checagem de papel)

### Atores

Colaborador autenticado (qualquer papel).

### Pré-condições

Colaborador tenta upload para uma pasta cuja atribuição ativa não é `ADMINISTRADOR`, ou é `ADMINISTRADOR` mas sem grant `EDICAO` no nível dessa atribuição para a pasta alvo.

### Fluxo Principal

1. Colaborador solicita upload informando a pasta alvo.
2. Sistema resolve a atribuição ativa (Contexto Ativo) e seu papel.
3. Papel diferente de `ADMINISTRADOR`, ou nenhum `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) compatível com o nível dessa atribuição → sistema nega o upload.

### Fluxos de Exceção

- **FE-001:** Papel/grant incompatível → `403` explícito (nunca `404` disfarçado nem silêncio).

### Pós-condições

Upload negado e registrado (auditoria padrão da plataforma).

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-002

### Regras de Negócio Relacionadas

- `BR-012` (`docs/domain/09-business-rules.md`)

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-002

---

## UC-DOC-UPLOAD-003 — Upload para pasta inexistente

### Objetivo

Garantir resposta explícita quando o identificador de pasta informado não existe.

### Prioridade

Must

### Complexidade

Baixa

### Atores

Colaborador com atribuição ativa `ADMINISTRADOR`.

### Pré-condições

Identificador de pasta informado não corresponde a nenhuma `PASTA` existente.

### Fluxo Principal

1. Administrador solicita upload informando um `COD_PASTA` inexistente.
2. Sistema não localiza a pasta.
3. Sistema retorna `404`.

### Pós-condições

Nenhum registro criado.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-003

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-003

---

# Casos de Uso Fora do Escopo

Atualizar (editar metadados/nova versão), Alterar Status (excluir/arquivar) e qualquer CRUD de `PASTA` — não se aplicam nesta entrega (`specification.md` § Escopo, decisão de produto 2026-08-27). Candidatos a Feature futura quando priorizados.

---

# Matriz de Rastreabilidade

| Caso de Uso | RF | API | Teste |
|--------------|----|----|--------|
| UC-DOC-UPLOAD-001 | RF-DOC-UPLOAD-001 | POST /api/v1/pastas/{id}/documentos | AT-DOC-UPLOAD-001 |
| UC-DOC-UPLOAD-002 | RF-DOC-UPLOAD-002 | POST /api/v1/pastas/{id}/documentos | AT-DOC-UPLOAD-002 |
| UC-DOC-UPLOAD-003 | RF-DOC-UPLOAD-003 | POST /api/v1/pastas/{id}/documentos | AT-DOC-UPLOAD-003 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 3 UCs (upload, negação por papel/grant, pasta inexistente) |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review: fluxo principal de UC-DOC-UPLOAD-001 explicita categoria derivada do `TIP_MIME` e `COD_COLABORADOR` da sessão |
