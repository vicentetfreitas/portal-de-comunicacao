# Decisions

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO-NAVEGACAO |
| Feature | Navegação de Pastas e Documentos |
| Domínio | DOCUMENTO |
| Status | DRAFT — decisões **fechadas** (2026-08-27); pronta para DoR-Spec |

---

# Objetivo

Registro rastreável das decisões de produto/arquitetura desta Feature. As 7 decisões
abaixo foram fechadas pelo usuário em **2026-08-27** (D-01..D-03 diretamente;
D-04..D-06 pela adoção das propostas do `/specify`, coerentes com D-01..D-03).

---

## D-01 — Contrato de API para a hierarquia  ✅ **(a) — estender `PastaResponse`**

`PastaResponse` ganha `pastaPaiId` (`COD_PASTA_PAI`) e `dataAtualizacao`
(`DAT_ATUALIZACAO`). `GET /api/v1/pastas` **não muda de comportamento** — continua
devolvendo todas as pastas ativas visíveis ao Contexto Ativo, agora com os dois campos.
Aditivo e retrocompatível; nenhum endpoint novo; nenhuma tabela/coluna nova.

- `FT-DOCUMENTO` está `DONE` — a extensão do DTO é propriedade **desta** Feature
  (documentada no `api.md` daqui).
- Se o volume por Área crescer no futuro → evoluir para lazy por nível
  (`GET /api/v1/pastas?pastaPaiId={id}`) sem quebrar a UI.

---

## D-02 — Filtro  ✅ **fora do MVP**

O controle de filtro do mock legado (`155612.png`) **não entra** nesta entrega. MVP =
**busca + toggle grade/lista**. O filtro (e o critério que ele aplica) fica para uma
iteração futura, quando o Product Owner definir o que filtra.

---

## D-03 — Contexto de navegação  ✅ **só a Área do Contexto Ativo**

A navegação abre em `/app/area/arquivos` — as pastas da Área que o colaborador está
usando (Contexto Ativo), exatamente a visibilidade de `FT-DOCUMENTO` hoje. **Não** há,
nesta entrega, navegação de pastas de outras Áreas a partir da Federação (modelo legado
`/app/federacao/:areaSlug/...`) — fica para uma iteração futura que integre com
`FT-FEDERACAO-COLABORADOR`.

---

## D-04 — Busca: cliente ou servidor  ✅ **cliente**

Como D-01=(a) traz todas as pastas visíveis numa resposta, a busca filtra o conjunto já
carregado no cliente (nome de pasta/documento, `contains` case-insensitive). **Sem**
endpoint de busca nesta versão. Server-side entra junto com a eventual evolução para
lazy por nível.

---

## D-05 — Rótulo "Público / Privado" no card  ✅ **omitir no MVP**

O card não exibe rótulo "Público/Privado". Não existe campo para isso (`FT-DOCUMENTO`,
2026-08-26: a diferença é quais níveis têm `PERMISSAO_PASTA`, não um booleano) e a API
não expõe os destinatários dos grants da pasta. Exibir esse rótulo (derivado dos grants)
fica para uma iteração futura, se priorizado.

---

## D-06 — Identificador de pasta na URL  ✅ **id numérico** (`?pasta=<COD_PASTA>`)

O estado de navegação usa o id numérico da pasta (`?pasta=122`) mais o modo de
visualização. Caminho legível (`?folder_path=/marketing/publico`, como o legado) é
evolução cosmética futura — exige resolver caminho → id e lidar com nomes que mudam.

---

## D-07 — Persistência da preferência grade/lista  ✅ **DEC-FA-005**

Mesmo mecanismo da persistência de tema — por dispositivo, no cliente. Nenhuma tabela,
nenhum endpoint.

---

# Questões que esta Feature deliberadamente NÃO resolve

| Questão | Posição |
|---------|---------|
| Navegar pastas de outras Áreas via Federação | Fora de escopo (D-03) — iteração futura |
| Filtro avançado (tipo/data/categoria) | Fora de escopo (D-02) — iteração futura |
| Rótulo Público/Privado, grant individual, herança viva | Fora — como em `FT-DOCUMENTO` |
| Paginação de documentos dentro de uma pasta | Fora — mantém premissa "volume baixo" de `FT-DOCUMENTO` |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-27 | Claude Code (Specify) | Criação — D-01..D-06 abertas, D-07 proposta |
| 1.1 | 2026-08-27 | Claude Code (Specify) | **Decisões fechadas** pelo usuário: D-01=(a), D-02 fora do MVP, D-03 só Área do Contexto Ativo; D-04 cliente, D-05 omitir, D-06 id numérico (propostas adotadas). Feature pronta para DoR-Spec. |
