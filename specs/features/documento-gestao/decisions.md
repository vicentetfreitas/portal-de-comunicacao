# Decisions

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-GESTAO |
| Feature | Gestão de Pastas e Documentos |
| Domínio | DOCUMENTO |
| Status | DRAFT |

---

# Objetivo

Registro rastreável das decisões de produto/arquitetura desta Feature. Tomadas
pelo usuário em **2026-08-27**, respondendo a perguntas diretas em sessão de
Specify. Originalmente propostas como "Fase 2" de `FT-DOCUMENTO-UPLOAD`; extraídas
para esta Feature irmã (opção 1a do parecer de Review de Spec, 2026-08-27) para
que o incremento tenha ciclo de estado próprio (`DRAFT → …`).

Contexto herdado sem alteração:

- `FT-DOCUMENTO` (`DONE`) — leitura de pastas/documentos (`GET /api/v1/pastas`,
  `GET /api/v1/documentos/{id}/download`), visibilidade por `PERMISSAO_PASTA`
  multi-nível, filtro `STA_DOCUMENTO='EXPIRADO'` e `PASTA.FLG_ATIVO='N'`.
- `FT-DOCUMENTO-UPLOAD` (`IMPLEMENTING`) — upload de um arquivo para pasta
  existente por `ADMINISTRADOR` escopado; `PermissaoPastaDomainService.ensureUploadGrant`,
  `ObjectStorageClient.upload`, `MediaCategoryResolver`, teto `25MB` → `413`.

---

## D-01 — Escopo da Feature

Para o colaborador com atribuição ativa `ADMINISTRADOR`:

- **Pastas:** criar subpasta, renomear, mover (reparent), arquivar (soft-delete).
- **Documentos já enviados:** enviar nova versão, editar metadados (título/descrição),
  arquivar, mover entre pastas.

Nenhuma capacidade não listada acima.

---

## D-02 — Autorização: só `ADMINISTRADOR` escopado

Mantém o modelo de `FT-DOCUMENTO-UPLOAD` sem ampliação: a operação exige atribuição
ativa (Contexto Ativo) com `PAPEL.NOM_PAPEL='ADMINISTRADOR'` **e** `PERMISSAO_PASTA`
(`TIP_ACESSO='EDICAO'`) compatível com o nível dessa atribuição na pasta alvo.

- **`GESTOR_DOCUMENTAL` não é estendido** a esta Feature — o papel existe no seed
  (`database/ddl/008-initial-data.sql`) mas seu uso permanece adiado.
- Nenhum papel novo (`ADMINISTRADOR_AREA` etc.).

---

## D-03 — A aplicação não gerencia `PERMISSAO_PASTA`

Não há tela nem endpoint para criar/revogar grants de pasta. Grants continuam
sendo dado institucional (`database/dml/`), administrados fora da aplicação.

`TIP_ACESSO='ADMINISTRACAO'` permanece **reservado** para uma futura Feature de
gestão de grants — esta Feature mapeia para `EDICAO` (`EDICAO` = "escrever conteúdo
e estrutura" vs `ADMINISTRACAO` = "administrar quem tem acesso").

Consequência: esta Feature **não resolve** `OQ-006` (revogação de permissão) nem
`OQ-011` (alterar compartilhamento após publicação).

---

## D-04 — Subpasta nova copia (snapshot) os grants da pasta-pai

Como a visibilidade é 100% governada por `PERMISSAO_PASTA` e a aplicação não
gerencia grants (D-03), uma pasta recém-criada sem grant seria invisível a
todos — inclusive ao criador.

**Decisão:** no momento da criação, o backend **copia** para a nova pasta todas as
linhas de `PERMISSAO_PASTA` da pasta-pai (`TIP_DESTINATARIO`, `COD_DESTINATARIO`,
`TIP_ACESSO`) — um *snapshot*, não uma herança viva.

- `FLG_HERDA_PERMISSAO` **não** é usado como mecanismo (permanece no default do
  schema; a resolução de acesso ignora esse campo, igual a `FT-DOCUMENTO`).
- `OQ-012` / `BR-017` (herança de permissão na hierarquia de pastas) **permanecem
  abertas** — a cópia snapshot não as resolve, apenas evita o problema da pasta órfã.
- Se a pasta-pai não tiver nenhuma linha `PERMISSAO_PASTA` (caso que não deve
  ocorrer para uma pasta visível), a subpasta nasce sem grants — a criação é
  permitida, mas o backend registra aviso de auditoria.
- Ajustes posteriores nos grants da subpasta (divergir da pai) são ação do DBA.

---

## D-05 — Excluir pasta = soft-delete, bloqueado se não-vazia

"Excluir pasta" resolve para `FLG_ATIVO='N'` (coluna já existente em `PASTA`).

Só é permitido quando a pasta **não tem**:

- nenhuma subpasta com `FLG_ATIVO='S'`; **e**
- nenhum `DOCUMENTO` com `STA_DOCUMENTO IN ('ATIVO','ARQUIVADO')`.

Caso contrário → `409 Conflict`. O administrador precisa mover ou arquivar o
conteúdo antes. Sem exclusão em cascata nesta Feature (evita remoção acidental de
subárvore inteira).

---

## D-06 — Excluir documento = arquivar (sem exclusão real nesta Feature)

`STA_DOCUMENTO` só admite `ATIVO | ARQUIVADO | EXPIRADO` (CHECK
`CK_DOCUMENTO_STATUS`, `database/ddl/004-create-constraints.sql`). Não há estado
de "excluído" e `DOCUMENTO` não tem `FLG_ATIVO`.

**Decisão:** nesta Feature "excluir documento" resolve para `STA_DOCUMENTO='ARQUIVADO'`.

- Documento `ARQUIVADO` **continua visível na leitura** conforme `RF-DOCUMENTO-004`
  de `FT-DOCUMENTO` (comportamento inalterado — não se mexe numa Feature `DONE`).
- Exclusão lógica de verdade (documento some da leitura do colaborador) exige nova
  migration (novo valor de `STA_DOCUMENTO` ou `FLG` na tabela) + ajuste da query de
  `FT-DOCUMENTO` → **Feature futura**, registrada como dívida.
- Desarquivar (`ARQUIVADO` → `ATIVO`) fica **fora do escopo** desta Feature.

---

## D-07 — Nova versão re-deriva a categoria do novo binário

Ao enviar `DOCUMENTO_VERSAO` adicional, `DOCUMENTO.COD_CATEGORIA_DOCUMENTAL` é
**re-derivado** do `TIP_MIME` do novo `ARQUIVO_BINARIO`, pela mesma regra de
`FT-DOCUMENTO-UPLOAD` (`Documentos`/`Imagens`/`Vídeos`/`Outros`, resolvida por
`NOM_CATEGORIA`).

Racional: a categoria é sempre "o tipo de mídia do arquivo atual" — manter a
categoria antiga após trocar um PDF por um MP4 seria incoerente.

---

## D-08 — Mover não altera `PERMISSAO_PASTA`

Mover uma pasta (`COD_PASTA_PAI`) ou um documento (`COD_PASTA`) **não** cria, copia
nem remove grants. Como os grants são explícitos por pasta (D-04) e não há herança
viva, a visibilidade do recurso movido é exatamente a que ele já tinha.

Consequência: mover **não** é uma forma de reclassificar exposição — `OQ-011`
segue sem ser tocada por esta Feature.

Prevenção de ciclo: mover uma pasta para ela mesma ou para qualquer descendente
seu → `409 Conflict`.

---

# Decisões de consistência de estado (do Review de Spec, 2026-08-27)

Refinamentos que fecham as não conformidades NC-1..NC-4 do parecer:

| Ref | Regra |
|-----|-------|
| DC-1 (NC-1) | Documento com `STA_DOCUMENTO` diferente de `ATIVO` (`ARQUIVADO`/`EXPIRADO`) **recusa** nova versão, edição de metadados e mover → `409`. Só o próprio `DELETE` (arquivar) reage diferente (DC-3). |
| DC-2 (NC-2) | **Regra transversal:** pasta com `FLG_ATIVO='N'` é tratada como **inexistente para escrita** — criar subpasta nela, renomear/mover ela, ou mover documento para/de ela → `404`. Documento `EXPIRADO` idem para operações que não sejam arquivar. |
| DC-3 (NC-3) | "Já arquivado" é **`409`** de forma uniforme: `DELETE` de pasta já `FLG_ATIVO='N'` → `409`; `DELETE` de documento já `ARQUIVADO`/`EXPIRADO` → `409`. Nunca `404` disfarçado nem `204` idempotente silencioso. |
| DC-4 (NC-4) | Mapeamento de Regra de Negócio: operações puramente estruturais/de ciclo de vida (renomear pasta, arquivar pasta, nova versão, editar metadados, arquivar documento) **não** têm RN dedicada e são marcadas `RN: —` na rastreabilidade, com nota. Governança pode catalogar uma BR depois. |

---

# Questões de domínio que esta Feature deliberadamente NÃO resolve

| Questão | Posição |
|---------|---------|
| `OQ-006` — revogação formal de permissão | Fora de escopo (D-03) |
| `OQ-011` — alterar compartilhamento/visibilidade após publicação | Fora de escopo (D-03, D-08) |
| `OQ-012` / `BR-017` — herança de permissão na hierarquia de pastas | Evitada por snapshot (D-04), não resolvida |
| `BR-023` — quota de armazenamento | Fora de escopo (herdado de `FT-DOCUMENTO-UPLOAD`) |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — D-01..D-08 + DC-1..DC-4; extraído da proposta "Fase 2" de `FT-DOCUMENTO-UPLOAD` (opção 1a do Review de Spec) |
