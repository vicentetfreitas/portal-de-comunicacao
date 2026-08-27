# Acceptance Tests

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Atualizar / Alterar Status / Mover) |
| Versão | 1.0 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-GESTAO |
| Feature | Gestão de Pastas e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Critérios de aceitação da manutenção de pastas e documentos. Estratégia de testes:
`docs/implementation/08-testing-strategy.md` — não duplicada aqui.

Contexto comum aos cenários de sucesso: colaborador com atribuição ativa
`ADMINISTRADOR` num nível X (Área/Federação/Singular/Equipe); a(s) pasta(s) alvo
têm `PERMISSAO_PASTA` (`TIP_DESTINATARIO=X`, mesmo `COD_DESTINATARIO`,
`TIP_ACESSO='EDICAO'`).

---

## Gestão de pastas

## AT-DOC-GESTAO-001 — Criar subpasta com cópia de grants

### Tipo

Happy Path

### Cenário — subpasta herda a audiência da pai (snapshot)

**Given** colaborador `ADMINISTRADOR` com grant `EDICAO` na pasta P (ativa); P tem 3
linhas de `PERMISSAO_PASTA` (`AREA` LEITURA, `AREA` EDICAO, `FEDERACAO` LEITURA)
**When** `POST /api/v1/pastas/{P}/subpastas` com `nome`
**Then** `201`; nova `PASTA` com `COD_PASTA_PAI = P`, `FLG_ATIVO='S'`; existem
exatamente 3 linhas de `PERMISSAO_PASTA` da nova pasta, idênticas às de P; a
subpasta aparece em `GET /api/v1/pastas` para os mesmos colaboradores que enxergam P

### Cenário — nome em branco

**When** `POST /api/v1/pastas/{id}/subpastas` com `nome` vazio
**Then** `400`; nenhuma pasta criada

### Cenário — pasta-pai arquivada

**Given** pasta-pai com `FLG_ATIVO='N'`
**When** `POST /api/v1/pastas/{id}/subpastas`
**Then** `404`

---

## AT-DOC-GESTAO-002 — Renomear pasta

### Tipo

Happy Path / Negative

### Cenário — sucesso

**When** `PATCH /api/v1/pastas/{id}` com `nome` novo
**Then** `200`; `NOM_PASTA` atualizado; `COD_PASTA_PAI` e `PERMISSAO_PASTA`
inalterados

### Cenário — payload vazio

**When** `PATCH /api/v1/pastas/{id}` sem nenhum campo
**Then** `400`

### Cenário — nome em branco

**When** `PATCH /api/v1/pastas/{id}` com `nome` = `""`
**Then** `400`

---

## AT-DOC-GESTAO-003 — Mover pasta

### Tipo

Happy Path / Negative

### Cenário — sucesso

**Given** grant `EDICAO` na pasta X e na pasta destino D (ambas ativas)
**When** `PATCH /api/v1/pastas/{X}` com `codPastaPai = D`
**Then** `200`; `COD_PASTA_PAI` de X = D; `PERMISSAO_PASTA` de X inalterada

### Cenário — ciclo: mover para descendente

**Given** pasta X com subpasta Y (e Y com subpasta Z); grant `EDICAO` em todas
**When** `PATCH /api/v1/pastas/{X}` com `codPastaPai = Z`
**Then** `409`; hierarquia inalterada

### Cenário — mover para si mesma

**When** `PATCH /api/v1/pastas/{X}` com `codPastaPai = X`
**Then** `409`

### Cenário — grant só na pasta movida

**Given** grant `EDICAO` em X mas não em D
**When** `PATCH /api/v1/pastas/{X}` com `codPastaPai = D`
**Then** `403`

---

## AT-DOC-GESTAO-004 — Arquivar pasta

### Tipo

Happy Path / Negative

### Cenário — sucesso (pasta vazia)

**Given** pasta sem subpasta ativa e sem documento `ATIVO`/`ARQUIVADO`
**When** `DELETE /api/v1/pastas/{id}`
**Then** `204`; `FLG_ATIVO='N'`; pasta some de `GET /api/v1/pastas`

### Cenário — pasta com documento ativo

**Given** pasta com 1 documento `STA_DOCUMENTO='ATIVO'`
**When** `DELETE /api/v1/pastas/{id}`
**Then** `409`; `FLG_ATIVO` continua `'S'`

### Cenário — pasta com subpasta ativa

**When** `DELETE /api/v1/pastas/{id}` (pasta com 1 subpasta `FLG_ATIVO='S'`)
**Then** `409`

### Cenário — pasta só com documento EXPIRADO

**Given** pasta cujos únicos documentos estão `STA_DOCUMENTO='EXPIRADO'`
**When** `DELETE /api/v1/pastas/{id}`
**Then** `204` — `EXPIRADO` não conta como conteúdo bloqueante

### Cenário — pasta já arquivada

**Given** pasta com `FLG_ATIVO='N'`
**When** `DELETE /api/v1/pastas/{id}`
**Then** `409`

---

## Gestão de documentos já enviados

## AT-DOC-GESTAO-005 — Enviar nova versão

### Tipo

Happy Path / Negative

### Cenário — sucesso

**Given** documento `ATIVO` com versão atual `NUM_VERSAO=1` (`application/pdf`),
grant `EDICAO` na pasta
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

## AT-DOC-GESTAO-006 — Editar metadados do documento

### Tipo

Happy Path / Negative

### Cenário — sucesso

**When** `PATCH /api/v1/documentos/{id}` com `titulo` novo e `descricao`
**Then** `200`; `TIT_DOCUMENTO`/`DSC_DOCUMENTO` atualizados; nenhuma nova
`DOCUMENTO_VERSAO`; `COD_CATEGORIA_DOCUMENTAL` inalterado

### Cenário — payload vazio

**When** `PATCH /api/v1/documentos/{id}` sem campos
**Then** `400`

### Cenário — documento arquivado

**Given** documento `STA_DOCUMENTO='ARQUIVADO'`
**When** `PATCH /api/v1/documentos/{id}` com `titulo` novo
**Then** `409`

---

## AT-DOC-GESTAO-007 — Arquivar documento

### Tipo

Happy Path / Negative

### Cenário — sucesso

**Given** documento `ATIVO`, grant `EDICAO`
**When** `DELETE /api/v1/documentos/{id}`
**Then** `204`; `STA_DOCUMENTO='ARQUIVADO'`; documento **ainda aparece** em
`GET /api/v1/pastas` (RF-DOCUMENTO-004 de `FT-DOCUMENTO` — inalterado)

### Cenário — já arquivado

**Given** documento `STA_DOCUMENTO='ARQUIVADO'`
**When** `DELETE /api/v1/documentos/{id}`
**Then** `409`

---

## AT-DOC-GESTAO-008 — Mover documento entre pastas

### Tipo

Happy Path / Authorization / Negative

### Cenário — sucesso

**Given** documento `ATIVO`; grant `EDICAO` na pasta de origem O e na pasta destino
D (`FLG_ATIVO='S'`)
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
**Then** `404`

### Cenário — documento arquivado

**Given** documento `STA_DOCUMENTO='ARQUIVADO'`
**When** `PATCH /api/v1/documentos/{id}` com `codPasta` de destino válido
**Then** `409`

---

## Transversais

## AT-DOC-GESTAO-009 — Negar operação sem papel/grant compatível

### Tipo

Authorization

### Cenário — sem papel ADMINISTRADOR

**Given** colaborador com atribuição ativa `COLABORADOR`, pasta com grant `EDICAO`
compatível com o nível dessa atribuição
**When** qualquer operação da Feature (POST subpasta, PATCH/DELETE pasta ou
documento, POST versão)
**Then** `403`

### Cenário — ADMINISTRADOR sem grant no nível certo

**Given** colaborador `ADMINISTRADOR` na Área A; pasta cujo único grant `EDICAO` é
para a Área B
**When** qualquer operação sobre essa pasta ou seus documentos
**Then** `403` explícito — nunca `404` disfarçado

### Cenário — ADMINISTRADOR com grant só de leitura

**Given** colaborador `ADMINISTRADOR` (nível Área), pasta com `PERMISSAO_PASTA`
`TIP_ACESSO='LEITURA'` apenas
**When** qualquer operação de escrita
**Then** `403` — `LEITURA` não autoriza escrita

---

## AT-DOC-GESTAO-010 — Operação para recurso inexistente ou inativo

### Tipo

Negative

### Cenário — pasta não existe

**When** `POST .../subpastas`, `PATCH`/`DELETE` `/api/v1/pastas/{id}` com id
inexistente
**Then** `404`

### Cenário — documento não existe

**When** `POST /api/v1/documentos/{id}/versoes`, `PATCH`/`DELETE`
`/api/v1/documentos/{id}` com id inexistente
**Then** `404`

### Cenário — pasta destino de mover não existe

**When** `PATCH /api/v1/documentos/{id}` (ou `.../pastas/{id}`) com `codPasta`/
`codPastaPai` inexistente
**Then** `404`

### Cenário — escrita em pasta arquivada (não re-arquivamento)

**Given** pasta `FLG_ATIVO='N'`
**When** `POST .../subpastas` ou `PATCH /api/v1/pastas/{id}` (renomear)
**Then** `404`

---

# Cenários Negativos (transversais)

- Não autenticado → `401`.
- Sem Contexto Ativo resolvido → padrão `FT-PRIMEIRO-ACESSO`/`FT-SESSION`.
- Arquivo acima do teto (`25MB`) → `413` (nova versão).
- Categoria de mídia resolvida ausente em `CATEGORIA_DOCUMENTAL` → erro de
  configuração explícito (fail-fast) — cenário de configuração.

---

# Matriz de Rastreabilidade

| Teste | RF | UC |
|--------|----|----|
| AT-DOC-GESTAO-001 | RF-DOC-GESTAO-001 | UC-DOC-GESTAO-001 |
| AT-DOC-GESTAO-002 | RF-DOC-GESTAO-002 | UC-DOC-GESTAO-002 |
| AT-DOC-GESTAO-003 | RF-DOC-GESTAO-003 | UC-DOC-GESTAO-003 |
| AT-DOC-GESTAO-004 | RF-DOC-GESTAO-004 | UC-DOC-GESTAO-004 |
| AT-DOC-GESTAO-005 | RF-DOC-GESTAO-005 | UC-DOC-GESTAO-005 |
| AT-DOC-GESTAO-006 | RF-DOC-GESTAO-006 | UC-DOC-GESTAO-006 |
| AT-DOC-GESTAO-007 | RF-DOC-GESTAO-007 | UC-DOC-GESTAO-007 |
| AT-DOC-GESTAO-008 | RF-DOC-GESTAO-008 | UC-DOC-GESTAO-008 |
| AT-DOC-GESTAO-009 | RF-DOC-GESTAO-009 | UC-DOC-GESTAO-009 |
| AT-DOC-GESTAO-010 | RF-DOC-GESTAO-010 | UC-DOC-GESTAO-010 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — AT-DOC-GESTAO-001..010; cobre estados de recurso (arquivado/inativo) por NC-1..NC-3 do Review de Spec |
