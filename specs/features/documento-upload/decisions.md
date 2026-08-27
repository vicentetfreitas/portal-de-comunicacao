# Decisions

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-UPLOAD |
| Feature | Upload de Arquivos e Documentos |
| Domínio | DOCUMENTO |
| Status | DRAFT (Fase 2 — aguarda Review de Spec) |

---

# Objetivo

Registro rastreável das decisões de produto/arquitetura que sustentam a **Fase 2**
desta Feature (gestão de pastas e de documentos já enviados). As decisões da
**Fase 1** (upload de um arquivo em pasta existente) estão em `specification.md`
§ Escopo / § Modelo de Autorização e permanecem válidas.

Todas as decisões abaixo foram tomadas pelo usuário em **2026-08-27**, nesta
sessão de Specify, respondendo a perguntas diretas.

---

## D-01 — Escopo da Fase 2

Fase 2 cobre, para o colaborador com atribuição ativa `ADMINISTRADOR`:

- **Pastas:** criar subpasta, renomear, mover (reparent), arquivar (soft-delete).
- **Documentos já enviados:** enviar nova versão, editar metadados (título/descrição),
  arquivar, mover entre pastas.

Não cobre nenhuma capacidade não listada acima.

---

## D-02 — Autorização: só `ADMINISTRADOR` escopado

Mantém o modelo da Fase 1 sem ampliação: a operação exige atribuição ativa
(Contexto Ativo) com `PAPEL.NOM_PAPEL='ADMINISTRADOR'` **e** `PERMISSAO_PASTA`
(`TIP_ACESSO='EDICAO'`) compatível com o nível dessa atribuição na pasta alvo.

- **`GESTOR_DOCUMENTAL` não é estendido** a esta Feature — o papel existe no seed
  (`database/ddl/008-initial-data.sql`) mas seu uso permanece adiado (mesma
  posição da Fase 1).
- Nenhum papel novo (`ADMINISTRADOR_AREA` etc.) é criado.

---

## D-03 — A aplicação não gerencia `PERMISSAO_PASTA`

Não há tela nem endpoint para criar/revogar grants de pasta. Grants continuam
sendo dado institucional (`database/dml/`), administrados fora da aplicação.

`TIP_ACESSO='ADMINISTRACAO'` permanece **reservado** para uma futura Feature de
gestão de grants — a gestão de pastas da Fase 2 mapeia para `EDICAO`, coerente com
`EDICAO` = "escrever conteúdo e estrutura" vs `ADMINISTRACAO` = "administrar quem
tem acesso".

Consequência: esta Feature **não resolve** `OQ-011` (alterar compartilhamento após
publicação) nem `OQ-006` (revogação de permissão).

---

## D-04 — Subpasta nova copia (snapshot) os grants da pasta-pai

Como a visibilidade é 100% governada por `PERMISSAO_PASTA` e a aplicação não
gerencia grants (D-03), uma pasta recém-criada sem grant seria invisível a
todos — inclusive ao criador.

**Decisão:** no momento da criação, o backend **copia** para a nova pasta todas as
linhas de `PERMISSAO_PASTA` da pasta-pai (`TIP_DESTINATARIO`, `COD_DESTINATARIO`,
`TIP_ACESSO`) — um *snapshot*, não uma herança viva.

- `FLG_HERDA_PERMISSAO` **não** é usado como mecanismo (permanece no default do
  schema; a resolução de acesso ignora esse campo, igual à Fase 1 e a `FT-DOCUMENTO`).
- `OQ-012` / `BR-017` (herança de permissão na hierarquia de pastas) **permanecem
  abertas** — a cópia snapshot não as resolve, apenas evita o problema da pasta órfã.
- Ajustes posteriores nos grants da subpasta (divergir da pai) são ação do DBA.

---

## D-05 — Excluir pasta = soft-delete, bloqueado se não-vazia

"Excluir pasta" resolve para `FLG_ATIVO='N'` (coluna já existente em `PASTA`).

Só é permitido quando a pasta **não tem**:

- nenhuma subpasta com `FLG_ATIVO='S'`; **e**
- nenhum `DOCUMENTO` com `STA_DOCUMENTO IN ('ATIVO','ARQUIVADO')`.

Caso contrário → `409 Conflict`. O administrador precisa mover ou arquivar o
conteúdo antes. Sem exclusão em cascata nesta fase (evita remoção acidental de
subárvore inteira).

---

## D-06 — Excluir documento = arquivar (sem exclusão real nesta fase)

`STA_DOCUMENTO` só admite `ATIVO | ARQUIVADO | EXPIRADO` (CHECK
`CK_DOCUMENTO_STATUS`, `database/ddl/004-create-constraints.sql`). Não há estado
de "excluído" e `DOCUMENTO` não tem `FLG_ATIVO`.

**Decisão:** nesta fase "excluir documento" resolve para `STA_DOCUMENTO='ARQUIVADO'`.

- Documento `ARQUIVADO` **continua visível na leitura** conforme `RF-DOCUMENTO-004`
  de `FT-DOCUMENTO` (comportamento inalterado — não se mexe numa Feature `DONE`).
- Exclusão lógica de verdade (documento some da leitura do colaborador) exige nova
  migration (novo valor de `STA_DOCUMENTO` ou `FLG` na tabela) + ajuste da query de
  `FT-DOCUMENTO` → **fase futura**, registrada como dívida.
- Desarquivar (`ARQUIVADO` → `ATIVO`) fica **fora do escopo** desta fase.

---

## D-07 — Nova versão re-deriva a categoria do novo binário

Ao enviar `DOCUMENTO_VERSAO` adicional, `DOCUMENTO.COD_CATEGORIA_DOCUMENTAL` é
**re-derivado** do `TIP_MIME` do novo `ARQUIVO_BINARIO`, pela mesma regra da Fase 1
(`Documentos`/`Imagens`/`Vídeos`/`Outros`, resolvida por `NOM_CATEGORIA`).

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

# Questões de domínio que esta Feature deliberadamente NÃO resolve

| Questão | Posição desta Feature |
|---------|-----------------------|
| `OQ-006` — revogação formal de permissão | Fora de escopo (D-03) |
| `OQ-011` — alterar compartilhamento/visibilidade após publicação | Fora de escopo (D-03, D-08) |
| `OQ-012` / `BR-017` — herança de permissão na hierarquia de pastas | Evitada por snapshot (D-04), não resolvida |
| `BR-023` — quota de armazenamento | Fora de escopo (herdado da Fase 1) |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — 8 decisões de produto da Fase 2 (gestão de pastas e documentos) |
