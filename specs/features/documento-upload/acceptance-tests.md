# Acceptance Tests

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Fase 1: criação; Fase 2: update/soft-delete/versão) |
| Versão | 2.0 |
| Status | DRAFT (Fase 2 — aguarda Review de Spec) |
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

Critérios de aceitação da escrita em Gestão Documental. Estratégia de testes:
`docs/implementation/08-testing-strategy.md` — não duplicada aqui.

Contexto comum aos cenários de sucesso: colaborador com atribuição ativa
`ADMINISTRADOR` num nível X (Área/Federação/Singular/Equipe); a(s) pasta(s) alvo
têm `PERMISSAO_PASTA` (`TIP_DESTINATARIO=X`, mesmo `COD_DESTINATARIO`,
`TIP_ACESSO='EDICAO'`).

---

## [Fase 1]

## AT-DOC-UPLOAD-001 — Upload de arquivo em pasta existente

### Tipo

Happy Path

### Cenário — sucesso

**Given** colaborador `ADMINISTRADOR` (nível Área) com grant `EDICAO` na pasta alvo
**When** `POST /api/v1/pastas/{id}/documentos` com um `application/pdf` e título
**Then** `201`; documento em `GET /api/v1/pastas` com `STA_DOCUMENTO='ATIVO'`; versão
atual aponta para o binário enviado; `COD_CATEGORIA_DOCUMENTAL` = `Documentos`;
`DOCUMENTO.COD_COLABORADOR` e `DOCUMENTO_VERSAO.COD_COLABORADOR` = colaborador
autenticado

### Cenário — sucesso em outro nível (Federação)

**Given** colaborador `ADMINISTRADOR` (nível Federação), pasta com grant `EDICAO`
`TIP_DESTINATARIO='FEDERACAO'`
**When** `POST /api/v1/pastas/{id}/documentos`
**Then** `201`

### Cenário — categoria derivada do tipo de mídia

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO`; categorias
`Documentos`/`Imagens`/`Vídeos`/`Outros` existem
**When** upload de `image/png`, depois `video/mp4`, depois `application/zip`
**Then** os `DOCUMENTO` recebem `COD_CATEGORIA_DOCUMENTAL` de `Imagens`, `Vídeos` e
`Outros` — sem parâmetro de categoria

---

## AT-DOC-UPLOAD-002 — Negar operação de escrita sem papel/grant compatível

### Tipo

Authorization

### Cenário — sem papel ADMINISTRADOR

**Given** colaborador com atribuição ativa `COLABORADOR`, pasta com grant `EDICAO`
compatível com o nível dessa atribuição
**When** qualquer operação de escrita da Feature (upload, PATCH/DELETE de pasta ou
documento, nova versão)
**Then** `403` — papel incompatível

### Cenário — ADMINISTRADOR sem grant de edição no nível certo

**Given** colaborador `ADMINISTRADOR` na Área A; pasta cujo único grant `EDICAO` é
para a Área B
**When** qualquer operação de escrita sobre essa pasta ou seus documentos
**Then** `403` explícito — nunca `404` disfarçado

### Cenário — ADMINISTRADOR com grant só de leitura

**Given** colaborador `ADMINISTRADOR` (nível Área), pasta com `PERMISSAO_PASTA`
`TIP_ACESSO='LEITURA'` apenas
**When** qualquer operação de escrita
**Then** `403` — `LEITURA` não autoriza escrita

### Cenário — mover: grant só na origem

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta de origem mas não
na pasta destino
**When** `PATCH /api/v1/documentos/{id}` com `codPasta` da pasta destino (ou
`PATCH /api/v1/pastas/{id}` com `codPastaPai`)
**Then** `403`

---

## AT-DOC-UPLOAD-003 — Operação para recurso inexistente

### Tipo

Negative

### Cenário — pasta não existe

**Given** identificador de pasta inexistente
**When** `POST /api/v1/pastas/{id}/documentos`, `POST .../subpastas`, `PATCH`/`DELETE`
`/api/v1/pastas/{id}`
**Then** `404`

### Cenário — documento não existe

**Given** identificador de documento inexistente
**When** `POST /api/v1/documentos/{id}/versoes`, `PATCH`/`DELETE`
`/api/v1/documentos/{id}`
**Then** `404`

### Cenário — pasta destino de mover não existe

**Given** documento válido; `codPasta` inexistente no request
**When** `PATCH /api/v1/documentos/{id}`
**Then** `404`

---

## [Fase 2] — Gestão de pastas

## AT-DOC-UPLOAD-004 — Criar subpasta com cópia de grants

### Tipo

Happy Path

### Cenário — subpasta herda a audiência da pai (snapshot)

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta P; P tem 3 linhas
de `PERMISSAO_PASTA` (`AREA` LEITURA, `AREA` EDICAO, `FEDERACAO` LEITURA)
**When** `POST /api/v1/pastas/{P}/subpastas` com `nome`
**Then** `201`; nova `PASTA` com `COD_PASTA_PAI = P`, `FLG_ATIVO='S'`; existem
exatamente 3 linhas de `PERMISSAO_PASTA` da nova pasta, idênticas às de P; a
subpasta aparece em `GET /api/v1/pastas` para os mesmos colaboradores que enxergam P

### Cenário — nome em branco

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta-pai
**When** `POST /api/v1/pastas/{id}/subpastas` com `nome` vazio
**Then** `400`; nenhuma pasta criada

---

## AT-DOC-UPLOAD-005 — Renomear pasta

### Tipo

Happy Path

### Cenário — sucesso

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta
**When** `PATCH /api/v1/pastas/{id}` com `nome` novo
**Then** `200`; `NOM_PASTA` atualizado; `COD_PASTA_PAI` e `PERMISSAO_PASTA`
inalterados

### Cenário — payload vazio

**When** `PATCH /api/v1/pastas/{id}` sem nenhum campo
**Then** `400`

---

## AT-DOC-UPLOAD-006 — Mover pasta

### Tipo

Happy Path / Negative

### Cenário — sucesso

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta X e na pasta
destino D
**When** `PATCH /api/v1/pastas/{X}` com `codPastaPai = D`
**Then** `200`; `COD_PASTA_PAI` de X = D; `PERMISSAO_PASTA` de X inalterada

### Cenário — ciclo: mover para descendente

**Given** pasta X com subpasta Y (e Y com subpasta Z); grant `EDICAO` em todas
**When** `PATCH /api/v1/pastas/{X}` com `codPastaPai = Z`
**Then** `409`; hierarquia inalterada

### Cenário — mover para si mesma

**When** `PATCH /api/v1/pastas/{X}` com `codPastaPai = X`
**Then** `409`

---

## AT-DOC-UPLOAD-007 — Arquivar pasta

### Tipo

Happy Path / Negative

### Cenário — sucesso (pasta vazia)

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO`; pasta sem subpasta ativa e
sem documento `ATIVO`/`ARQUIVADO`
**When** `DELETE /api/v1/pastas/{id}`
**Then** `204`; `FLG_ATIVO='N'`; pasta some de `GET /api/v1/pastas`

### Cenário — pasta com documento ativo

**Given** pasta com 1 documento `STA_DOCUMENTO='ATIVO'`
**When** `DELETE /api/v1/pastas/{id}`
**Then** `409`; `FLG_ATIVO` continua `'S'`

### Cenário — pasta com subpasta ativa

**Given** pasta com 1 subpasta `FLG_ATIVO='S'`
**When** `DELETE /api/v1/pastas/{id}`
**Then** `409`

### Cenário — pasta só com documento EXPIRADO

**Given** pasta cujos únicos documentos estão `STA_DOCUMENTO='EXPIRADO'`
**When** `DELETE /api/v1/pastas/{id}`
**Then** `204` — `EXPIRADO` não conta como conteúdo bloqueante

---

## [Fase 2] — Gestão de documentos já enviados

## AT-DOC-UPLOAD-008 — Enviar nova versão

### Tipo

Happy Path / Negative

### Cenário — sucesso

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta; documento `ATIVO`
com versão atual `NUM_VERSAO=1` (`application/pdf`)
**When** `POST /api/v1/documentos/{id}/versoes` com um `image/png` e
`descricaoAlteracao`
**Then** `201`; nova `DOCUMENTO_VERSAO` `NUM_VERSAO=2`, `FLG_VERSAO_ATUAL='S'`,
`COD_COLABORADOR` = autenticado, `DSC_ALTERACAO` gravado; a versão 1 passa a
`FLG_VERSAO_ATUAL='N'`; `DOCUMENTO.COD_CATEGORIA_DOCUMENTAL` re-derivado para
`Imagens`; `GET /api/v1/documentos/{id}/download` serve o novo binário

### Cenário — documento arquivado

**Given** documento `STA_DOCUMENTO='ARQUIVADO'`
**When** `POST /api/v1/documentos/{id}/versoes`
**Then** `409`

### Cenário — arquivo vazio

**When** `POST /api/v1/documentos/{id}/versoes` sem `arquivo`
**Then** `400`

### Cenário — falha no Object Storage

**Given** documento `ATIVO`; storage indisponível
**When** `POST /api/v1/documentos/{id}/versoes`
**Then** erro explícito; nenhuma `DOCUMENTO_VERSAO`/`ARQUIVO_BINARIO` nova
persistida; versão atual segue sendo a anterior — cenário de integração

---

## AT-DOC-UPLOAD-009 — Editar metadados do documento

### Tipo

Happy Path / Negative

### Cenário — sucesso

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta do documento
**When** `PATCH /api/v1/documentos/{id}` com `titulo` novo e `descricao`
**Then** `200`; `TIT_DOCUMENTO`/`DSC_DOCUMENTO` atualizados; nenhuma nova
`DOCUMENTO_VERSAO`; `COD_CATEGORIA_DOCUMENTAL` inalterado

### Cenário — payload vazio

**When** `PATCH /api/v1/documentos/{id}` sem campos
**Then** `400`

### Cenário — título em branco

**When** `PATCH /api/v1/documentos/{id}` com `titulo` = `""`
**Then** `400`

---

## AT-DOC-UPLOAD-010 — Arquivar documento

### Tipo

Happy Path / Negative

### Cenário — sucesso

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO`; documento `ATIVO`
**When** `DELETE /api/v1/documentos/{id}`
**Then** `204`; `STA_DOCUMENTO='ARQUIVADO'`; documento **ainda aparece** em
`GET /api/v1/pastas` (RF-DOCUMENTO-004 de `FT-DOCUMENTO` — inalterado)

### Cenário — já arquivado

**Given** documento `STA_DOCUMENTO='ARQUIVADO'`
**When** `DELETE /api/v1/documentos/{id}`
**Then** `409`

---

## AT-DOC-UPLOAD-011 — Mover documento entre pastas

### Tipo

Happy Path / Authorization

### Cenário — sucesso

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta de origem O e na
pasta destino D (`FLG_ATIVO='S'`)
**When** `PATCH /api/v1/documentos/{id}` com `codPasta = D`
**Then** `200`; `DOCUMENTO.COD_PASTA = D`; nenhum `PERMISSAO_PASTA` criado/alterado;
o documento passa a ser listado sob D e não mais sob O

### Cenário — sem grant no destino

**Given** grant `EDICAO` na origem, mas não na pasta destino
**When** `PATCH /api/v1/documentos/{id}` com `codPasta` da pasta destino
**Then** `403`

### Cenário — destino arquivado

**Given** pasta destino com `FLG_ATIVO='N'`
**When** `PATCH /api/v1/documentos/{id}` com `codPasta` dessa pasta
**Then** `404` (pasta inativa tratada como inexistente para escrita)

---

# Cenários Negativos (transversais)

- Não autenticado → `401`.
- Sem Contexto Ativo resolvido → padrão `FT-PRIMEIRO-ACESSO`/`FT-SESSION`.
- Arquivo acima do teto (`25MB`) → `413` (upload e nova versão).
- Categoria de mídia resolvida ausente em `CATEGORIA_DOCUMENTAL` → erro de
  configuração explícito (fail-fast) — cenário de configuração.

---

# Matriz de Rastreabilidade

| Teste | RF | UC |
|--------|----|----|
| AT-DOC-UPLOAD-001 | RF-DOC-UPLOAD-001 | UC-DOC-UPLOAD-001 |
| AT-DOC-UPLOAD-002 | RF-DOC-UPLOAD-002 | UC-DOC-UPLOAD-002 |
| AT-DOC-UPLOAD-003 | RF-DOC-UPLOAD-003 | UC-DOC-UPLOAD-003 |
| AT-DOC-UPLOAD-004 | RF-DOC-UPLOAD-004 | UC-DOC-UPLOAD-004 |
| AT-DOC-UPLOAD-005 | RF-DOC-UPLOAD-005 | UC-DOC-UPLOAD-005 |
| AT-DOC-UPLOAD-006 | RF-DOC-UPLOAD-006 | UC-DOC-UPLOAD-006 |
| AT-DOC-UPLOAD-007 | RF-DOC-UPLOAD-007 | UC-DOC-UPLOAD-007 |
| AT-DOC-UPLOAD-008 | RF-DOC-UPLOAD-008 | UC-DOC-UPLOAD-008 |
| AT-DOC-UPLOAD-009 | RF-DOC-UPLOAD-009 | UC-DOC-UPLOAD-009 |
| AT-DOC-UPLOAD-010 | RF-DOC-UPLOAD-010 | UC-DOC-UPLOAD-010 |
| AT-DOC-UPLOAD-011 | RF-DOC-UPLOAD-011 | UC-DOC-UPLOAD-011 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 3 ATs da Fase 1 |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review — categoria/`COD_COLABORADOR`; negativos `400`/`413` |
| 2.0 | 2026-08-27 | Claude Code (Specify) | Fase 2 (DRAFT): AT-004..011; AT-002/003 ampliados para toda operação de escrita e para documento/pasta destino |
