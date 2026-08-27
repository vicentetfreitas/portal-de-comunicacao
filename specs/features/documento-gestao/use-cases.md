# Use Cases

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Atualizar / Alterar Status / Mover; cria `PASTA` e `DOCUMENTO_VERSAO`) |
| Versão | 1.0 |
| Status | DRAFT |
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

Casos de uso da manutenção do acervo documental por administrador escopado.

Ator comum a todos: **colaborador com atribuição ativa `ADMINISTRADOR`** (Federação,
Singular, Área ou Equipe). Pré-condição comum: grant `EDICAO` compatível com o
nível dessa atribuição na(s) pasta(s) alvo (`specification.md` § Modelo de
Autorização). Exceções comuns: papel/grant incompatível → `403` (UC-DOC-GESTAO-009);
recurso inexistente ou pasta inativa → `404` (UC-DOC-GESTAO-010); ver
`specification.md` § Regras transversais de estado do recurso.

---

## Gestão de pastas

## UC-DOC-GESTAO-001 — Criar subpasta

### Prioridade

Must

### Complexidade

Média (cria `PASTA` e replica N linhas de `PERMISSAO_PASTA` na mesma transação)

### Pré-condições

Administrador tem grant `EDICAO` na pasta-pai (ativa) escolhida.

### Fluxo Principal

1. Administrador escolhe uma pasta existente e informa o nome (e descrição opcional)
   da nova subpasta.
2. Sistema valida papel `ADMINISTRADOR` e grant `EDICAO` na pasta-pai.
3. Sistema cria a `PASTA` (`COD_PASTA_PAI` = pasta-pai, `FLG_ATIVO='S'`) e **copia**
   para ela todas as linhas de `PERMISSAO_PASTA` da pasta-pai.
4. Sistema confirma; a subpasta aparece na listagem para a mesma audiência da pai.

### Fluxos de Exceção

- **FE-001:** Pasta-pai inexistente ou `FLG_ATIVO='N'` → `404`.
- **FE-002:** Sem papel/grant na pasta-pai → `403`.
- **FE-003:** Nome em branco → `400`.
- **FE-004:** Pasta-pai sem nenhuma linha `PERMISSAO_PASTA` → criação permitida,
  subpasta sem grants, aviso de auditoria (`decisions.md` D-04).

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-001

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-001

---

## UC-DOC-GESTAO-002 — Renomear pasta

### Prioridade

Must

### Complexidade

Baixa

### Pré-condições

Administrador tem grant `EDICAO` na pasta; a pasta está ativa.

### Fluxo Principal

1. Administrador informa novo nome e/ou nova descrição da pasta.
2. Sistema valida papel/grant e a existência da pasta ativa.
3. Sistema atualiza `NOM_PASTA`/`DSC_PASTA`. Hierarquia e permissões inalteradas.

### Fluxos de Exceção

- **FE-001:** Pasta inexistente ou `FLG_ATIVO='N'` → `404`.
- **FE-002:** Sem papel/grant → `403`.
- **FE-003:** Requisição sem campos, ou nome enviado em branco → `400`.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-002

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-002

---

## UC-DOC-GESTAO-003 — Mover pasta

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

- **FE-001:** Pasta movida ou pasta destino inexistente ou `FLG_ATIVO='N'` → `404`.
- **FE-002:** Sem papel/grant em qualquer das duas → `403`.
- **FE-003:** Destino = pasta movida ou um descendente dela (ciclo) → `409`.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-003

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-003

---

## UC-DOC-GESTAO-004 — Arquivar pasta

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
- **FE-004:** Pasta já `FLG_ATIVO='N'` → `409`.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-004

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-004

---

## Gestão de documentos já enviados

## UC-DOC-GESTAO-005 — Enviar nova versão de documento

### Prioridade

Must

### Complexidade

Média (cria binário + versão, rebaixa a versão atual, re-deriva categoria, grava no
storage — atômico)

### Pré-condições

Administrador tem grant `EDICAO` na pasta (ativa) do documento; documento
`STA_DOCUMENTO='ATIVO'`.

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

- RF-DOC-GESTAO-005

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-005

---

## UC-DOC-GESTAO-006 — Editar metadados do documento

### Prioridade

Must

### Complexidade

Baixa

### Pré-condições

Administrador tem grant `EDICAO` na pasta (ativa) do documento; documento `ATIVO`.

### Fluxo Principal

1. Administrador informa novo título e/ou nova descrição.
2. Sistema valida papel/grant e que o documento está `ATIVO`.
3. Sistema atualiza `TIT_DOCUMENTO`/`DSC_DOCUMENTO`. Binário, versões e categoria
   inalterados.

### Fluxos de Exceção

- **FE-001:** Documento inexistente → `404`.
- **FE-002:** Sem papel/grant → `403`.
- **FE-003:** Requisição sem campos, ou título enviado em branco → `400`.
- **FE-004:** Documento não-`ATIVO` → `409`.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-006

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-006

---

## UC-DOC-GESTAO-007 — Arquivar documento

### Prioridade

Must

### Complexidade

Baixa

### Pré-condições

Administrador tem grant `EDICAO` na pasta (ativa) do documento; documento `ATIVO`.

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
(`RF-DOCUMENTO-004`). Desarquivar está fora do escopo desta Feature.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-007

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-007

---

## UC-DOC-GESTAO-008 — Mover documento entre pastas

### Prioridade

Must

### Complexidade

Baixa (checagem de grant em duas pastas)

### Pré-condições

Administrador tem grant `EDICAO` na pasta de origem **e** na pasta destino; pasta
destino ativa; documento `ATIVO`.

### Fluxo Principal

1. Administrador escolhe o documento e a pasta destino.
2. Sistema valida papel/grant nas duas pastas, que a pasta destino está ativa e que
   o documento está `ATIVO`.
3. Sistema atualiza `DOCUMENTO.COD_PASTA`. Nenhum grant é criado/copiado.

### Fluxos de Exceção

- **FE-001:** Documento ou pasta destino inexistente, ou pasta destino
  `FLG_ATIVO='N'` → `404`.
- **FE-002:** Sem papel/grant em qualquer das duas pastas → `403`.
- **FE-003:** Documento não-`ATIVO` → `409`.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-008

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-008

---

## Transversais

## UC-DOC-GESTAO-009 — Negar operação sem papel/grant compatível

### Prioridade

Must

### Atores

Colaborador autenticado (qualquer papel).

### Pré-condições

Colaborador tenta uma operação desta Feature para uma pasta cuja atribuição ativa
não é `ADMINISTRADOR`, ou é `ADMINISTRADOR` mas sem grant `EDICAO` no nível dessa
atribuição para a(s) pasta(s) alvo.

### Fluxo Principal

1. Colaborador solicita a operação informando a pasta/documento alvo.
2. Sistema resolve a atribuição ativa (Contexto Ativo) e seu papel.
3. Papel diferente de `ADMINISTRADOR`, ou nenhum `PERMISSAO_PASTA`
   (`TIP_ACESSO='EDICAO'`) compatível → sistema nega.

### Fluxos de Exceção

- **FE-001:** Papel/grant incompatível → `403` explícito (nunca `404` disfarçado).
  Em operações que exigem grant em duas pastas (mover), basta faltar em uma.

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-009

### Regras de Negócio Relacionadas

- `BR-012`

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-009

---

## UC-DOC-GESTAO-010 — Operação para recurso inexistente ou inativo

### Prioridade

Must

### Atores

Colaborador com atribuição ativa `ADMINISTRADOR`.

### Pré-condições

O identificador de pasta ou documento informado (incluindo pasta-pai ou pasta
destino) não corresponde a nenhum registro, ou corresponde a uma pasta
`FLG_ATIVO='N'`.

### Fluxo Principal

1. Administrador solicita a operação informando um identificador inexistente ou de
   pasta arquivada.
2. Sistema não localiza o recurso (ou o trata como inexistente para escrita).
3. Sistema retorna `404`. Nenhum registro criado ou alterado. Exceção: `DELETE`
   (re-arquivar) de pasta já `FLG_ATIVO='N'` retorna `409` (`decisions.md` DC-3).

### Requisitos Funcionais Relacionados

- RF-DOC-GESTAO-010

### Critérios de Aceitação Relacionados

- AT-DOC-GESTAO-010

---

# Casos de Uso Fora do Escopo

Upload de documento novo (`FT-DOCUMENTO-UPLOAD`); desarquivar documento; reativar
pasta; exclusão física/lógica real de documento; exclusão de pasta em cascata;
gerenciar `PERMISSAO_PASTA` (grants) pela aplicação; herança viva de permissão
entre pastas. Ver `specification.md` § Escopo e `decisions.md`.

---

# Matriz de Rastreabilidade

| Caso de Uso | RF | API | Teste |
|--------------|----|----|--------|
| UC-DOC-GESTAO-001 | RF-DOC-GESTAO-001 | POST /api/v1/pastas/{id}/subpastas | AT-DOC-GESTAO-001 |
| UC-DOC-GESTAO-002 | RF-DOC-GESTAO-002 | PATCH /api/v1/pastas/{id} | AT-DOC-GESTAO-002 |
| UC-DOC-GESTAO-003 | RF-DOC-GESTAO-003 | PATCH /api/v1/pastas/{id} | AT-DOC-GESTAO-003 |
| UC-DOC-GESTAO-004 | RF-DOC-GESTAO-004 | DELETE /api/v1/pastas/{id} | AT-DOC-GESTAO-004 |
| UC-DOC-GESTAO-005 | RF-DOC-GESTAO-005 | POST /api/v1/documentos/{id}/versoes | AT-DOC-GESTAO-005 |
| UC-DOC-GESTAO-006 | RF-DOC-GESTAO-006 | PATCH /api/v1/documentos/{id} | AT-DOC-GESTAO-006 |
| UC-DOC-GESTAO-007 | RF-DOC-GESTAO-007 | DELETE /api/v1/documentos/{id} | AT-DOC-GESTAO-007 |
| UC-DOC-GESTAO-008 | RF-DOC-GESTAO-008 | PATCH /api/v1/documentos/{id} | AT-DOC-GESTAO-008 |
| UC-DOC-GESTAO-009 | RF-DOC-GESTAO-009 | (transversal — toda operação) | AT-DOC-GESTAO-009 |
| UC-DOC-GESTAO-010 | RF-DOC-GESTAO-010 | (transversal — toda operação) | AT-DOC-GESTAO-010 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — UC-DOC-GESTAO-001..010; extraído da proposta "Fase 2" de `FT-DOCUMENTO-UPLOAD` |
