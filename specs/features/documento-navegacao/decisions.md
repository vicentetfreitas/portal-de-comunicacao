# Decisions

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-NAVEGACAO |
| Feature | Navegação de Pastas e Documentos |
| Domínio | DOCUMENTO |
| Status | DRAFT — decisões **abertas**, a fechar antes de `READY_FOR_REVIEW` |

---

# Objetivo

Registrar as decisões de produto/arquitetura desta Feature. Nesta versão (`DRAFT`) a
maioria está **em aberto** — o `/specify` levanta as opções; o Product Owner / usuário
decide antes do Gate 1.

---

## D-01 — Contrato de API para a hierarquia  🔶 ABERTA

`GET /api/v1/pastas` devolve lista plana; `PastaResponse` não expõe `COD_PASTA_PAI` nem
`DAT_ATUALIZACAO`.

| Opção | O que muda | Recomendação |
|-------|-----------|--------------|
| **(a)** Estender `PastaResponse` (`pastaPaiId`, `dataAtualizacao`) | Aditivo, retrocompatível; cliente monta a árvore de uma vez | **Proposta para o MVP** |
| **(b)** `GET /api/v1/pastas/arvore` (estrutura aninhada) | Endpoint novo | — |
| **(c)** Lazy por nível `GET /api/v1/pastas?pastaPaiId={id}` | Escala melhor; N chamadas p/ expandir a árvore | Evolução futura se o volume crescer |

**Impacto se (a):** `FT-DOCUMENTO` está `DONE` — a extensão do DTO é propriedade **desta**
Feature (documentada no `api.md` daqui), aditiva e sem quebrar contrato existente.

---

## D-02 — Filtro  🔶 ABERTA

O mock legado (`155612.png`) mostra um controle de filtro ao lado da busca. **O que ele
filtra** não está definido: tipo de mídia? intervalo de data? categoria documental?

| Opção | |
|-------|--|
| Cortar o filtro do MVP — entregar só **busca + toggle grade/lista** | menor escopo |
| Filtro por tipo de mídia (Documentos/Imagens/Vídeos/Outros — as categorias de `V009`) | alinhado a `DEC-CMS-002` |
| Filtro por data de atualização | comum em explorador |

---

## D-03 — Contexto de navegação  🔶 ABERTA

O app novo abre Arquivos na **Área do Contexto Ativo** (`/app/area/arquivos`). O modelo
legado (`155612.png`) navega `/app/federacao/:areaSlug/...` — o colaborador escolhe a
Área pela nav da Federação e vê os arquivos daquela Área.

| Opção | |
|-------|--|
| **MVP: só a Área do Contexto Ativo** | menor escopo; consistente com `FT-DOCUMENTO` atual |
| Navegar pastas de qualquer Área visível a partir da Federação | alinha ao legado; toca `FT-FEDERACAO-COLABORADOR`; a visibilidade já é multi-nível, então tecnicamente o backend já suporta |

---

## D-04 — Busca: cliente ou servidor  🔶 ABERTA

- **Cliente** (MVP): filtra o conjunto já carregado por `GET /api/v1/pastas`. Simples;
  funciona bem se a opção (a) de D-01 for escolhida (tudo já vem).
- **Servidor**: endpoint de busca (`GET /api/v1/documentos?q=...` ou similar).
  Necessário se D-01 = (c) (lazy) ou se o volume não couber numa resposta.

Depende de D-01.

---

## D-05 — Rótulo "Público / Privado" no card  🔶 ABERTA

A tela legada rotula cada pasta com "Público" ou "Privado". **Não existe** campo para
isso — decisão de `FT-DOCUMENTO` (2026-08-26): a diferença é **quais níveis** têm
`PERMISSAO_PASTA`, não um booleano.

| Opção | |
|-------|--|
| Exibir o rótulo, **derivado** dos grants (grant em `FEDERACAO`/`SINGULAR` → "Público"; só `AREA`/`EQUIPE` → "Privado") | exige a API expor os destinatários dos grants da pasta (hoje não expõe) |
| Omitir o rótulo no MVP | menor escopo; nada a expor a mais |

---

## D-06 — Identificador de pasta na URL  🔶 ABERTA

`RF-DOC-NAV-006` exige o estado de navegação na URL.

| Opção | Exemplo | Trade-off |
|-------|---------|-----------|
| Id numérico | `?pasta=122` | Simples; opaco |
| Caminho legível | `?folder_path=/marketing/publico` | Como o legado; exige resolver caminho → id (nomes podem colidir/mudar) |
| Slug estável | `?pasta=marketing-publico` | Precisa de coluna de slug (não existe) → fora de escopo |

Proposta: **id numérico** no MVP; caminho legível é evolução cosmética.

---

## D-07 — Persistência da preferência grade/lista  ✅ decidida (proposta)

Mesmo mecanismo de `DEC-FA-005` (persistência da preferência de tema) — por dispositivo,
não no servidor. Nenhuma tabela, nenhum endpoint.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — D-01..D-06 abertas, D-07 proposta. A fechar antes de `READY_FOR_REVIEW`. |
