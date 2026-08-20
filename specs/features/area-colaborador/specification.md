# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Feature de leitura, sobre backend já aprovado) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-AREA-COLABORADOR |
| Feature | Área — Visão do Colaborador |
| Domínio | AREA (consumo, não CRUD) |
| Tipo | Frontend de leitura sobre `FT-AREA` + `FT-EQUIPE` (ambas `APPROVED`) |
| Status | DRAFT |

---

# Objetivo

Permitir que o colaborador autenticado (não administrador) visualize a Área do seu Contexto Ativo — hub com atalhos, informações da Área, e equipe (membros e contatos) — consumindo as APIs já implementadas e `APPROVED` de `FT-AREA` (`/api/v1/areas/{id}`) e `FT-EQUIPE` (`/api/v1/equipes`).

**Distinção de `specification-frontend.md` de `FT-EQUIPE`:** aquela spec cobre a UI **administrativa** de CRUD de equipes (`/app/administrador/equipes`). Esta Feature é a visão de **autoatendimento** (self-service) do colaborador comum sobre sua própria Área — mesmos dados de leitura, ator e propósito diferentes, sem sobreposição de rotas.

**Fonte de evidência visual:** inspeção direta do arquivo Figma auditado em `AUDITORIA-DS-FIGMA-01.md` — frames `Areas` (node `64:285`, hub com atalhos "Equipe" e "Arquivos e Documentos"), `Areas - Equipe` (node `64:874`, lista de membros com nome/cargo/e-mail/telefone + "Contato setorial"). **Nota de rastreabilidade:** esses node IDs e o bloco "Contato setorial" não estão documentados em `AUDITORIA-DS-FIGMA-01.md` — aquele documento cobre inventário de átomos/moléculas de Design System, não o detalhamento funcional de conteúdo por tela.

---

# Escopo

## Incluído

- Rota de hub da Área do Contexto Ativo, com atalhos para "Equipe" e "Arquivos e Documentos" (este último aponta para `FT-DOCUMENTO`, fora do escopo desta Feature).
- Página de visualização da Área (nome, descrição) — consumindo `GET /api/v1/areas/{id}`.
- Página de visualização da Equipe da Área (membros: nome, cargo, e-mail, telefone) — consumindo `GET /api/v1/equipes` filtrado por `areaId`.
- Somente leitura — nenhuma escrita nesta Feature.

## Fora do Escopo

- Qualquer operação de escrita (cadastro/edição/status) — já coberta por `FT-AREA`/`FT-EQUIPE` (admin).
- Arquivos e Downloads da Área — `FT-DOCUMENTO` (`specs/features/arquivos/`).
- Contato setorial da Área (e-mail/WhatsApp institucional, visível no frame `Areas - Equipe`) — ver "Decisão de produto pendente" abaixo.

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador autenticado | Qualquer usuário com sessão válida e Contexto Ativo resolvido; acesso de leitura à própria Área |

---

# Requisitos Funcionais

## RF-AREA-COLAB-001 — Visualizar hub da Área

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-COLAB-001 |
| Descrição | O sistema deve exibir um hub com atalhos para as sub-seções da Área do Contexto Ativo. |

## RF-AREA-COLAB-002 — Visualizar dados da Área

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-COLAB-002 |
| Descrição | O sistema deve exibir nome e descrição da Área, consumindo `GET /api/v1/areas/{id}` (`FT-AREA`, já `APPROVED`). |

## RF-AREA-COLAB-003 — Visualizar equipe da Área

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-COLAB-003 |
| Descrição | O sistema deve listar os membros da equipe vinculada à Área (nome, cargo, e-mail, telefone), consumindo `GET /api/v1/equipes` filtrado por `areaId` (`FT-EQUIPE`, já `APPROVED`). |

---

# Decisão de produto pendente

O frame Figma `Areas - Equipe` mostra um bloco "Contato setorial" (e-mail + WhatsApp institucional da Área) que **não existe** no modelo atual — `FT-AREA` exclui explicitamente "Gestão de contatos institucionais da área (entidade CONTATO)" do seu escopo (`specs/features/area/specification.md`). Duas alternativas, não decidíveis por esta especificação:

1. Tratar como campo simples adicional em `AREA` (menor escopo, requer alteração em `FT-AREA`).
2. Tratar como a entidade `CONTATO` já prevista como "Feature futura" em `FT-AREA` — escopo maior, nova Feature.

Enquanto não decidido, `RF-AREA-COLAB-002/003` cobrem só os dados já disponíveis (nome, descrição, membros) — o bloco de contato setorial fica fora do MVP desta Feature.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| FT-AREA | Bloqueante (satisfeita) | `APPROVED`; `GET /api/v1/areas/{id}` já existe |
| FT-EQUIPE | Bloqueante (satisfeita) | `APPROVED`; `GET /api/v1/equipes` já existe, com filtro `areaId` já em uso (`EquipeFilters.vue`) |
| FT-DOCUMENTO | Não bloqueante | Atalho "Arquivos e Documentos" do hub aponta para lá; ausência não impede esta Feature |
| Design System (`ds/`) | Não bloqueante | `DsCard`, `DsActionCard`, `DsDataTable`/lista já `CONFORME` |

---

# Fontes

`docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md` (inventário de Design System — não cobre os node IDs/conteúdo funcional citados acima); `specs/features/area/specification.md`, `api.md`; `specs/features/equipe/specification.md`, `api.md`, `specification-frontend.md`.
