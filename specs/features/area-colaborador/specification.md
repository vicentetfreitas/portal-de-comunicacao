# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Feature de leitura, sobre backend já aprovado) |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-AREA-COLABORADOR |
| Feature | Área — Visão do Colaborador |
| Domínio | AREA (consumo, não CRUD) |
| Tipo | Frontend de leitura sobre `FT-AREA` + `FT-EQUIPE` (ambas `APPROVED`) |
| Status | APPROVED |

---

# Objetivo

Permitir que o colaborador autenticado (não administrador) visualize a Área do seu Contexto Ativo — hub com atalhos, informações da Área, e equipe(s) vinculada(s) (nome, descrição) — consumindo as APIs já implementadas e `APPROVED` de `FT-AREA` (`/api/v1/areas/{id}`) e `FT-EQUIPE` (`/api/v1/equipes`).

**Distinção de `specification-frontend.md` de `FT-EQUIPE`:** aquela spec cobre a UI **administrativa** de CRUD de equipes (`/app/administrador/equipes`). Esta Feature é a visão de **autoatendimento** (self-service) do colaborador comum sobre sua própria Área — mesmos dados de leitura, ator e propósito diferentes, sem sobreposição de rotas.

**Fonte de evidência visual:** inspeção direta do arquivo Figma auditado em `AUDITORIA-DS-FIGMA-01.md` — frames `Areas` (node `64:285`, hub com atalhos "Equipe" e "Arquivos e Documentos"), `Areas - Equipe` (node `64:874`, lista de membros com nome/cargo/e-mail/telefone + "Contato setorial"). **Nota de rastreabilidade:** esses node IDs e o bloco "Contato setorial" não estão documentados em `AUDITORIA-DS-FIGMA-01.md` — aquele documento cobre inventário de átomos/moléculas de Design System, não o detalhamento funcional de conteúdo por tela.

---

# Escopo

## Incluído

- Rota de hub da Área do Contexto Ativo, com atalhos para "Equipe" e "Arquivos e Documentos" (este último aponta para `FT-DOCUMENTO`, fora do escopo desta Feature).
- Página de visualização da Área (nome, descrição) — consumindo `GET /api/v1/areas/{id}`.
- Página de visualização da(s) Equipe(s) vinculada(s) à Área (nome, descrição da equipe) — consumindo `GET /api/v1/equipes` filtrado por `areaId`.
- Somente leitura — nenhuma escrita nesta Feature.

## Fora do Escopo

- Roster de membros individuais da equipe (nome, cargo, e-mail, telefone por pessoa) — não obtível pelos contratos atuais; ver "Decisões de produto" abaixo.
- Qualquer operação de escrita (cadastro/edição/status) — já coberta por `FT-AREA`/`FT-EQUIPE` (admin).
- Arquivos e Downloads da Área — `FT-DOCUMENTO` (`specs/features/arquivos/`).
- Contato setorial da Área (e-mail/WhatsApp institucional, visível no frame `Areas - Equipe`) — ver "Decisões de produto" abaixo.

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

## RF-AREA-COLAB-003 — Visualizar equipe(s) da Área

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-COLAB-003 |
| Descrição | O sistema deve listar as equipes vinculadas à Área (nome, descrição), consumindo `GET /api/v1/equipes` filtrado por `areaId` (`FT-EQUIPE`, já `APPROVED`). Não inclui roster de membros individuais — ver "Decisões de produto". |

---

# Decisões de produto

## Contato setorial — decidido: fora do MVP

O frame Figma `Areas - Equipe` mostra um bloco "Contato setorial" (e-mail + WhatsApp institucional da Área) que **não existe** no modelo atual — `FT-AREA` exclui explicitamente "Gestão de contatos institucionais da área (entidade CONTATO)" do seu escopo (`specs/features/area/specification.md`).

**Decisão (2026-08-20):** fora do MVP desta Feature. Duas alternativas permanecem em aberto para uma Feature futura — não para esta especificação:

1. Campo simples adicional em `AREA` (menor escopo, requer alteração em `FT-AREA`).
2. Entidade `CONTATO`, já prevista como "Feature futura" em `FT-AREA` (escopo maior).

## Roster de membros da equipe — decidido: reduzido a dados de Equipe

O frame Figma `Areas - Equipe` mostra membros individuais (nome, cargo, e-mail, telefone). Nenhum contrato `APPROVED` hoje devolve esse roster: `GET /api/v1/equipes` (`FT-EQUIPE`) retorna registros de Equipe — `id, areaId, name, description, leaderId, status` —, não uma lista de pessoas; `FT-EQUIPE` exclui explicitamente "Gestão de membros/colaboradores" do próprio escopo. `FT-COLABORADOR` (`ColaboradorResponse`) não tem `cargo` (modelo TO-BE, `DEC-DB-027`/`DH-CARGO-01`) nem `telefone` (pertence a `CONTATO`, não exposto em nenhum contrato de colaborador).

**Decisão (2026-08-20):** `RF-AREA-COLAB-003` reduzido a exibir dados da(s) Equipe(s) vinculada(s) à Área (nome, descrição), consumindo somente `GET /api/v1/equipes`, já `APPROVED`. Roster de membros individuais fica fora do escopo desta Feature; `FT-COLABORADOR` não é introduzido como dependência.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| FT-AREA | Bloqueante (satisfeita) | `APPROVED`; `GET /api/v1/areas/{id}` já existe |
| FT-EQUIPE | Bloqueante (satisfeita) | `APPROVED`; `GET /api/v1/equipes` já existe, com filtro `areaId` já em uso (`EquipeFilters.vue`) |
| FT-DOCUMENTO | Não bloqueante | Atalho "Arquivos e Documentos" do hub aponta para lá; ausência não impede esta Feature |
| Design System (`ds/`) | Não bloqueante | `DsCard`, `DsActionCard`, `DsDataTable`/lista já `CONFORME` |

---

# Impactos

| Camada | Impacto |
|---|---|
| Backend | Nenhum. Nenhum endpoint novo, nenhuma alteração em código de `FT-AREA`/`FT-EQUIPE`. Consumo integral dos contratos já implementados e `APPROVED`. |
| Frontend | Impacto principal da Feature: novas rotas/páginas (hub, visualização de Área, visualização de Equipe(s) da Área — ver `tasks.md`); composables de leitura; reuso de `area.service.ts`/`equipe.service.ts` e respectivos `types/organization/*.types.ts` já existentes (nenhum client novo); reuso de componentes DS já `CONFORME` (`DsCard`, `DsActionCard`, `DsDataTable`/lista); novas strings de i18n; reuso do guard de sessão/Contexto Ativo já existente (`FT-AUTH`/`FT-SESSION`). |
| Banco de Dados | Nenhum. Nenhuma alteração de schema, tabela, coluna ou migração. |
| APIs | Nenhuma alteração de contrato. Esta Feature não expõe API própria (`api.md` não criado) — consome, sem modificar, `GET /api/v1/areas/{id}` e `GET /api/v1/equipes`, ambos `APPROVED`. |
| Segurança | Nenhuma alteração de autenticação ou autorização. A restrição a "própria Área" é aplicada apenas na camada de UI/roteamento via Contexto Ativo — os endpoints consumidos não têm escopo de autorização por vínculo organizacional (pendência de hardening já registrada na Review de Spec desta Feature, não bloqueante, fora deste escopo). |
| Integrações | Nenhuma integração externa nova. Consumo interno de APIs já existentes do próprio Portal. |
| Observabilidade | Nenhum requisito específico. Herda logging/telemetria padrão do frontend já em uso; nenhum novo instrumento ou ponto de falha assíncrono além de chamadas HTTP padrão já cobertas pela infraestrutura existente. |

---

# Fontes

`docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md` (inventário de Design System — não cobre os node IDs/conteúdo funcional citados acima); `specs/features/area/specification.md`, `api.md`; `specs/features/equipe/specification.md`, `api.md`, `specification-frontend.md`.
