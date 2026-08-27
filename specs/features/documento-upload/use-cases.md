# Use Cases

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Fase 1: Cadastrar; Fase 2: Atualizar / Alterar Status / Mover) |
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

Casos de uso da produção e manutenção do acervo documental por administrador
escopado.

- **[Fase 1]** UC-DOC-UPLOAD-001..003 — upload e suas negações. Implementados.
- **[Fase 2]** UC-DOC-UPLOAD-004..011 — gestão de pastas e de documentos já
  enviados. Este documento (DRAFT).

Ator comum a todos: **colaborador com atribuição ativa `ADMINISTRADOR`** (Federação,
Singular, Área ou Equipe). Pré-condição comum: grant `EDICAO` compatível com o
nível dessa atribuição na(s) pasta(s) alvo (`specification.md` § Modelo de
Autorização). Exceção comum: papel/grant incompatível → `403` (ver
UC-DOC-UPLOAD-002); recurso inexistente → `404` (ver UC-DOC-UPLOAD-003).

---

## [Fase 1]

## UC-DOC-UPLOAD-001 — Enviar arquivo para pasta existente

### Prioridade

Must

### Atores

Colaborador com atribuição ativa `ADMINISTRADOR`.

### Pré-condições

- Colaborador possui atribuição ativa (Contexto Ativo) com papel `ADMINISTRADOR`.
- A pasta alvo existe e possui `PERMISSAO_PASTA` (`TIP_ACESSO='EDICAO'`) para o nível
  dessa atribuição.

### Fluxo Principal

1. Administrador seleciona uma pasta e envia um arquivo com título. Não escolhe
   categoria nem escopo.
2. Sistema valida a atribuição ativa (papel `ADMINISTRADOR`) e o grant `EDICAO`.
3. Sistema deriva a categoria do `TIP_MIME`, grava o binário no Object Storage
   (DEC-013) e cria, de forma atômica, `ARQUIVO_BINARIO`, `DOCUMENTO`
   (`STA_DOCUMENTO='ATIVO'`, `COD_COLABORADOR` = autenticado) e `DOCUMENTO_VERSAO`
   (`NUM_VERSAO=1`, `FLG_VERSAO_ATUAL='S'`).
4. Sistema confirma; o documento passa a aparecer na listagem (`GET /api/v1/pastas`).

### Fluxos de Exceção

- **FE-001:** Pasta inexistente → `404`.
- **FE-002:** Sem papel `ADMINISTRADOR` ativo ou sem grant `EDICAO` → `403`.
- **FE-003:** Falha ao gravar no Object Storage → erro explícito, nenhum registro
  parcial persistido (operação atômica).

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-001

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-001

---

## UC-DOC-UPLOAD-002 — Negar operação de escrita sem papel/grant compatível

### Prioridade

Must

### Atores

Colaborador autenticado (qualquer papel).

### Pré-condições

Colaborador tenta uma operação de escrita desta Feature (Fase 1 ou Fase 2) para
uma pasta cuja atribuição ativa não é `ADMINISTRADOR`, ou é `ADMINISTRADOR` mas sem
grant `EDICAO` no nível dessa atribuição para a(s) pasta(s) alvo.

### Fluxo Principal

1. Colaborador solicita a operação informando a pasta/documento alvo.
2. Sistema resolve a atribuição ativa (Contexto Ativo) e seu papel.
3. Papel diferente de `ADMINISTRADOR`, ou nenhum `PERMISSAO_PASTA`
   (`TIP_ACESSO='EDICAO'`) compatível → sistema nega.

### Fluxos de Exceção

- **FE-001:** Papel/grant incompatível → `403` explícito (nunca `404` disfarçado
  nem silêncio). Em operações que exigem grant em duas pastas (mover), basta faltar
  em uma.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-002

### Regras de Negócio Relacionadas

- `BR-012`

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-002

---

## UC-DOC-UPLOAD-003 — Operação para recurso inexistente

### Prioridade

Must

### Atores

Colaborador com atribuição ativa `ADMINISTRADOR`.

### Pré-condições

O identificador de pasta ou documento informado (incluindo pasta-pai ou pasta
destino) não corresponde a nenhum registro.

### Fluxo Principal

1. Administrador solicita a operação informando um identificador inexistente.
2. Sistema não localiza o recurso.
3. Sistema retorna `404`. Nenhum registro criado ou alterado.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-003

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-003

---

## [Fase 2] — Gestão de pastas

## UC-DOC-UPLOAD-004 — Criar subpasta

### Prioridade

Must

### Complexidade

Média (cria `PASTA` e replica N linhas de `PERMISSAO_PASTA` na mesma transação)

### Pré-condições

Administrador tem grant `EDICAO` na pasta-pai escolhida.

### Fluxo Principal

1. Administrador escolhe uma pasta existente e informa o nome (e descrição opcional)
   da nova subpasta.
2. Sistema valida papel `ADMINISTRADOR` e grant `EDICAO` na pasta-pai.
3. Sistema cria a `PASTA` (`COD_PASTA_PAI` = pasta-pai, `FLG_ATIVO='S'`) e **copia**
   para ela todas as linhas de `PERMISSAO_PASTA` da pasta-pai (mesmos
   `TIP_DESTINATARIO`/`COD_DESTINATARIO`/`TIP_ACESSO`).
4. Sistema confirma; a subpasta aparece na listagem para a mesma audiência da pai.

### Fluxos de Exceção

- **FE-001:** Pasta-pai inexistente → `404`.
- **FE-002:** Sem papel/grant na pasta-pai → `403`.
- **FE-003:** Nome em branco → `400`.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-004

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-004

---

## UC-DOC-UPLOAD-005 — Renomear pasta

### Prioridade

Must

### Complexidade

Baixa

### Pré-condições

Administrador tem grant `EDICAO` na pasta; a pasta está ativa.

### Fluxo Principal

1. Administrador informa novo nome e/ou nova descrição da pasta.
2. Sistema valida papel/grant e a existência da pasta.
3. Sistema atualiza `NOM_PASTA`/`DSC_PASTA`. Hierarquia e permissões inalteradas.

### Fluxos de Exceção

- **FE-001:** Pasta inexistente → `404`.
- **FE-002:** Sem papel/grant → `403`.
- **FE-003:** Requisição sem campos, ou nome enviado em branco → `400`.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-005

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-005

---

## UC-DOC-UPLOAD-006 — Mover pasta

### Prioridade

Must

### Complexidade

Média (checagem de grant em duas pastas + prevenção de ciclo na subárvore)

### Pré-condições

Administrador tem grant `EDICAO` na pasta movida **e** na pasta destino; ambas ativas.

### Fluxo Principal

1. Administrador escolhe a pasta a mover e a nova pasta-pai.
2. Sistema valida papel/grant nas duas pastas.
3. Sistema verifica que a pasta destino não é a própria pasta movida nem um
   descendente dela.
4. Sistema atualiza `COD_PASTA_PAI`. `PERMISSAO_PASTA` da pasta movida **não** muda.

### Fluxos de Exceção

- **FE-001:** Pasta movida ou pasta destino inexistente → `404`.
- **FE-002:** Sem papel/grant em qualquer das duas → `403`.
- **FE-003:** Destino = pasta movida ou um descendente dela (ciclo) → `409`.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-006

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-006

---

## UC-DOC-UPLOAD-007 — Arquivar pasta

### Prioridade

Must

### Complexidade

Média (guarda de pasta não-vazia)

### Pré-condições

Administrador tem grant `EDICAO` na pasta; a pasta está ativa.

### Fluxo Principal

1. Administrador solicita arquivar a pasta.
2. Sistema valida papel/grant.
3. Sistema verifica que a pasta não tem subpasta ativa nem documento
   `ATIVO`/`ARQUIVADO`.
4. Sistema marca `FLG_ATIVO='N'`. A pasta some da listagem de `FT-DOCUMENTO`.

### Fluxos de Exceção

- **FE-001:** Pasta inexistente → `404`.
- **FE-002:** Sem papel/grant → `403`.
- **FE-003:** Pasta com subpasta ativa ou documento `ATIVO`/`ARQUIVADO` → `409`
  (mover ou arquivar o conteúdo antes).

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-007

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-007

---

## [Fase 2] — Gestão de documentos já enviados

## UC-DOC-UPLOAD-008 — Enviar nova versão de documento

### Prioridade

Must

### Complexidade

Média (cria binário + versão, rebaixa a versão atual, re-deriva categoria, grava no
storage — atômico)

### Pré-condições

Administrador tem grant `EDICAO` na pasta do documento; documento `STA_DOCUMENTO='ATIVO'`.

### Fluxo Principal

1. Administrador seleciona um documento e envia o novo arquivo (e nota de alteração
   opcional).
2. Sistema valida papel/grant e que o documento está `ATIVO`.
3. Sistema grava o novo binário no Object Storage e, de forma atômica: cria
   `ARQUIVO_BINARIO`; cria `DOCUMENTO_VERSAO` (`NUM_VERSAO` = atual+1,
   `FLG_VERSAO_ATUAL='S'`, `COD_COLABORADOR` = autenticado, `DSC_ALTERACAO`);
   rebaixa a versão anterior para `FLG_VERSAO_ATUAL='N'`; re-deriva
   `DOCUMENTO.COD_CATEGORIA_DOCUMENTAL` do novo `TIP_MIME`.
4. Sistema confirma; `GET /api/v1/documentos/{id}/download` passa a servir o novo
   binário.

### Fluxos de Exceção

- **FE-001:** Documento inexistente → `404`.
- **FE-002:** Sem papel/grant → `403`.
- **FE-003:** Documento `ARQUIVADO` ou `EXPIRADO` → `409`.
- **FE-004:** Arquivo ausente/vazio → `400`; acima do teto → `413`.
- **FE-005:** Falha no Object Storage → erro explícito, nenhuma versão parcial
  persistida.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-008

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-008

---

## UC-DOC-UPLOAD-009 — Editar metadados do documento

### Prioridade

Must

### Complexidade

Baixa

### Pré-condições

Administrador tem grant `EDICAO` na pasta do documento.

### Fluxo Principal

1. Administrador informa novo título e/ou nova descrição.
2. Sistema valida papel/grant e existência do documento.
3. Sistema atualiza `TIT_DOCUMENTO`/`DSC_DOCUMENTO`. Binário, versões e categoria
   inalterados.

### Fluxos de Exceção

- **FE-001:** Documento inexistente → `404`.
- **FE-002:** Sem papel/grant → `403`.
- **FE-003:** Requisição sem campos, ou título enviado em branco → `400`.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-009

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-009

---

## UC-DOC-UPLOAD-010 — Arquivar documento

### Prioridade

Must

### Complexidade

Baixa

### Pré-condições

Administrador tem grant `EDICAO` na pasta do documento; documento `ATIVO`.

### Fluxo Principal

1. Administrador solicita "excluir"/arquivar o documento.
2. Sistema valida papel/grant e que o documento está `ATIVO`.
3. Sistema marca `STA_DOCUMENTO='ARQUIVADO'`.

### Fluxos de Exceção

- **FE-001:** Documento inexistente → `404`.
- **FE-002:** Sem papel/grant → `403`.
- **FE-003:** Documento já `ARQUIVADO`/`EXPIRADO` → `409`.

### Pós-condições

Documento `ARQUIVADO`; **continua visível** na leitura de `FT-DOCUMENTO`
(`RF-DOCUMENTO-004`). Desarquivar está fora do escopo desta fase.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-010

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-010

---

## UC-DOC-UPLOAD-011 — Mover documento entre pastas

### Prioridade

Must

### Complexidade

Baixa (checagem de grant em duas pastas)

### Pré-condições

Administrador tem grant `EDICAO` na pasta de origem **e** na pasta destino; pasta
destino ativa.

### Fluxo Principal

1. Administrador escolhe o documento e a pasta destino.
2. Sistema valida papel/grant nas duas pastas e que a pasta destino está ativa.
3. Sistema atualiza `DOCUMENTO.COD_PASTA`. Nenhum grant é criado/copiado.

### Fluxos de Exceção

- **FE-001:** Documento ou pasta destino inexistente → `404`.
- **FE-002:** Sem papel/grant em qualquer das duas pastas → `403`.

### Requisitos Funcionais Relacionados

- RF-DOC-UPLOAD-011

### Critérios de Aceitação Relacionados

- AT-DOC-UPLOAD-011

---

# Casos de Uso Fora do Escopo

Desarquivar documento; reativar pasta; exclusão física/lógica real de documento;
exclusão de pasta em cascata; gerenciar `PERMISSAO_PASTA` (grants) pela aplicação;
herança viva de permissão entre pastas. Ver `specification.md` § Escopo e
`decisions.md`.

---

# Matriz de Rastreabilidade

| Caso de Uso | RF | API | Teste |
|--------------|----|----|--------|
| UC-DOC-UPLOAD-001 | RF-DOC-UPLOAD-001 | POST /api/v1/pastas/{id}/documentos | AT-DOC-UPLOAD-001 |
| UC-DOC-UPLOAD-002 | RF-DOC-UPLOAD-002 | (transversal — toda escrita) | AT-DOC-UPLOAD-002 |
| UC-DOC-UPLOAD-003 | RF-DOC-UPLOAD-003 | (transversal — toda escrita) | AT-DOC-UPLOAD-003 |
| UC-DOC-UPLOAD-004 | RF-DOC-UPLOAD-004 | POST /api/v1/pastas/{id}/subpastas | AT-DOC-UPLOAD-004 |
| UC-DOC-UPLOAD-005 | RF-DOC-UPLOAD-005 | PATCH /api/v1/pastas/{id} | AT-DOC-UPLOAD-005 |
| UC-DOC-UPLOAD-006 | RF-DOC-UPLOAD-006 | PATCH /api/v1/pastas/{id} | AT-DOC-UPLOAD-006 |
| UC-DOC-UPLOAD-007 | RF-DOC-UPLOAD-007 | DELETE /api/v1/pastas/{id} | AT-DOC-UPLOAD-007 |
| UC-DOC-UPLOAD-008 | RF-DOC-UPLOAD-008 | POST /api/v1/documentos/{id}/versoes | AT-DOC-UPLOAD-008 |
| UC-DOC-UPLOAD-009 | RF-DOC-UPLOAD-009 | PATCH /api/v1/documentos/{id} | AT-DOC-UPLOAD-009 |
| UC-DOC-UPLOAD-010 | RF-DOC-UPLOAD-010 | DELETE /api/v1/documentos/{id} | AT-DOC-UPLOAD-010 |
| UC-DOC-UPLOAD-011 | RF-DOC-UPLOAD-011 | PATCH /api/v1/documentos/{id} | AT-DOC-UPLOAD-011 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 3 UCs da Fase 1 |
| 1.1 | 2026-08-27 | Claude Code (Specify) | Correções do Review — categoria derivada, `COD_COLABORADOR` da sessão |
| 2.0 | 2026-08-27 | Claude Code (Specify) | Fase 2 (DRAFT): UC-004..011 (gestão de pastas e documentos); UC-002/003 generalizados para toda operação de escrita |
