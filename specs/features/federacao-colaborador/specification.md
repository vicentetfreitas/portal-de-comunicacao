# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Feature de leitura, sobre backend já aprovado) |
| Versão | 1.0 |
| Status | DONE |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-FEDERACAO-COLABORADOR |
| Feature | Federação — Navegação do Colaborador |
| Domínio | AREA / SINGULAR (consumo, não CRUD) |
| Tipo | Frontend de leitura sobre `FT-AREA`, `FT-SINGULAR`, `FT-EQUIPE`, `FT-COLABORADOR` (todas `APPROVED`) |
| Status | DONE |

---

# Objetivo

Permitir que o colaborador autenticado navegue, a partir do diretório "Federação"/"Singulares" da sidebar (`AppSidebar.vue`), para qualquer Área ou Singular da federação — não apenas a Área do próprio Contexto Ativo (essa já é `FT-AREA-COLABORADOR`, `DONE`, inalterada por esta Feature). Consome exclusivamente APIs já `APPROVED`; nenhum contrato novo de backend.

**Origem:** decisão de produto explícita do usuário em sessão interativa (2026-08-26), não um Specify/Readiness formal prévio — esta especificação foi escrita **depois** da implementação, para documentar o que foi decidido e construído. Ver `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md` (nota de acompanhamento da mesma data) para o histórico da conversa/decisões.

**Fonte de evidência visual:** três capturas de tela de `portaldecomunicacao.unimedceara.com.br` (site externo, live) fornecidas pelo usuário como referência — não é o arquivo Figma auditado em `AUDITORIA-DS-FIGMA-01.md`. Usadas apenas para orientar layout/fluxo; nenhum dado ali (roster com cargo/telefone, "Contato setorial") foi copiado sem uma fonte real neste projeto — ver "Decisões de produto" abaixo.

---

# Escopo

## Incluído

- Itens do diretório "Federação" (Áreas) e "Singulares" na sidebar tornam-se navegáveis (antes: lista somente-leitura, decisão revertida nesta Feature).
- Hub de uma Área arbitrária da federação (nome, descrição, atalhos "Equipe"/"Arquivos e Documentos") — `GET /api/v1/areas/{id}` (`FT-AREA`).
- Página "Equipe" de uma Área: roster de colaboradores ativos vinculados a ela (nome, e-mail reais — `GET /api/v1/colaboradores?areaId=`, `FT-COLABORADOR`, já usado por `ColaboradorListPage.vue`, mesmo contrato; cargo/telefone/ramal/"Contato setorial" **mockados**, ver "Decisões de produto"), em lista simples sem borda/card.
- Página de uma Singular arbitrária: nome/sigla + suas Áreas ativas, cada uma navegável ao hub de Área acima — `GET /api/v1/singulares/{id}` (`FT-SINGULAR`) + `GET /api/v1/areas?singularId=` (`FT-AREA`).
- Renomeação dos rótulos do menu: "Singular" → "Singulares", "Serviços" → "Sistemas e Serviços" (decisão de produto explícita, substitui a leitura literal do Figma usada anteriormente).
- Botão de voltar ao lado do título de cada página (`DsPageHeader`'s `show-back`), não mais no header — ver "Decisões de produto".
- `/app/perfil`: destino real (visualização) para "Editar perfil" da sidebar — ver "Decisões de produto".

## Fora do Escopo

- Roster com cargo/telefone/ramal/"Contato setorial" **reais** — mockados por decisão explícita (ver "Decisões de produto"); não existem em nenhum contrato `APPROVED` (`ColaboradorResponse` não tem `cargo` nem `telefone`; `AREA` não tem contato institucional).
- Formulário de edição real de perfil — `FT-PERFIL` continua `DRAFT`; `/app/perfil` é só visualização.
- Qualquer tela real de "Arquivos e Documentos" — `FT-DOCUMENTO` (`specs/features/arquivos/`) continua `DRAFT`. O atalho existe, desabilitado.
- Escrita/edição a partir desta navegação — somente leitura.
- Alteração da Feature `FT-AREA-COLABORADOR` ("minha área") — permanece intocada, com seu próprio escopo reduzido (equipes, não pessoas).

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador autenticado | Qualquer usuário com sessão válida; navega livremente por qualquer Área/Singular da própria federação (sem restrição adicional de vínculo — mesma observação de "Segurança" já registrada em `FT-AREA-COLABORADOR`) |

---

# Requisitos Funcionais

## RF-FEDCOLAB-001 — Navegar para o hub de uma Área

| Campo | Valor |
|--------|--------|
| Descrição | Clicar num item do diretório "Federação" navega para `/app/federacao/areas/:id`, exibindo nome/descrição da Área e atalhos "Equipe"/"Arquivos e Documentos" (este último desabilitado). |

## RF-FEDCOLAB-002 — Visualizar roster de colaboradores da Área

| Campo | Valor |
|--------|--------|
| Descrição | `/app/federacao/areas/:id/equipe` lista colaboradores ativos da Área (nome, e-mail), consumindo `GET /api/v1/colaboradores?areaId=`. |

## RF-FEDCOLAB-003 — Navegar para uma Singular e suas Áreas

| Campo | Valor |
|--------|--------|
| Descrição | Clicar num item do diretório "Singulares" navega para `/app/federacao/singulares/:id`, exibindo nome/sigla e as Áreas ativas vinculadas, cada uma navegável ao seu hub (RF-FEDCOLAB-001). |

---

# Decisões de produto

## Roster com nome + e-mail — decidido: sim, mas só com dado real

Usuário pediu roster de pessoas (como nas capturas do site externo). Nenhuma API devolve isso via `FT-EQUIPE` (que só tem dados de Equipe, não de pessoas) — mas `GET /api/v1/colaboradores` (já `APPROVED`, `FT-COLABORADOR`) aceita filtro `areaId` e devolve `name`/`email` reais por colaborador. RF-FEDCOLAB-002 usa esse caminho.

**Atualização (2026-08-26, 2ª rodada):** usuário pediu explicitamente `cargo`, telefone(s)/ramal(is) e "Contato setorial" também, autorizando mock temporário ("pode mocar os dados por enquanto") já que nenhum contrato aprovado tem esses campos. `useFederacaoAreaRoster.ts` mocka esses quatro campos (determinístico por `colaborador.id`, não aleatório) — `name`/`email` continuam reais. Sem fonte real de dado hoje; provavelmente exige mudança de contrato de backend (`ColaboradorResponse` não tem `cargo`/`telefone`; `AreaResponse` não tem contato institucional) — ver `tasks.md` para o gap explícito.

## "Contato setorial" — decidido: fora do escopo (sem fonte de dado)

Mesma decisão/motivo já registrado em `FT-AREA-COLABORADOR` — `AREA` não tem campo de contato institucional. Não fabricado.

## "Arquivos e Documentos" — decidido: mantido inerte

`FT-DOCUMENTO` segue `DRAFT`. Atalho existe (`DsActionCard disabled`, rótulo "Em breve"), sem tela real.

## Renomeação de rótulos do menu — decidido: "Singulares" e "Sistemas e Serviços"

Substitui os rótulos anteriores ("Singular", "Serviços" — este último era leitura literal do Figma, node 7:3 "Botão Serviços"). Decisão de produto explícita nesta sessão, documentada aqui e em `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`.

## Botão de voltar — decidido: ao lado do título da página, não no header

**Atualização (2026-08-26, 2ª rodada):** o botão de voltar (adicionado numa rodada anterior desta mesma sessão, no `AppHeader.vue` perto da logo) foi removido de lá e passou a viver dentro de `DsPageHeader.vue` (novo prop `show-back`/evento `back`), ao lado do título de cada página — não mais um controle global do shell. Aplicado às três páginas desta Feature (Área, Equipe, Singular) e a `/app/perfil` (ver abaixo). Outras páginas do app (Colaborador, Singular/Equipe administrativos) **perderam** o botão de voltar nesta troca — não foi pedido para elas, e o mecanismo agora é opt-in por página, não automático.

## Botões "Equipe"/"Arquivos e Documentos" — decidido: formato de botão com ícone, não card

**Atualização (2026-08-26, 2ª rodada):** trocado de `DsActionCard` (card grande, ícone+label+descrição) para `DsButton` com `DsIcon` inline — usuário pediu para seguir o layout do Figma (frame "Areas", não acessível nesta sessão por esgotamento de cota do plano Starter do MCP Figma) e "formato de botão com ícones". Implementado com o padrão de botão já usado no resto do app (`DsButton`+`DsIcon`), sem confirmação pixel-a-pixel contra o Figma real.

## "Editar perfil" — decidido: funcional, mas só visualização (FT-PERFIL segue DRAFT)

**Atualização (2026-08-26, 2ª rodada):** o botão "Editar perfil" da sidebar (visível, mas sem destino desde a fundação do projeto — `FT-PERFIL` é `DRAFT`) agora navega para `/app/perfil`, uma página nova e simples que mostra nome/e-mail da sessão já carregada (`GET /auth/me`, zero chamada nova) — sem formulário de edição real, já que `FT-PERFIL` não tem spec aprovada para isso. "Funcional" aqui significa "navega e mostra dado real", não "edição completa".

## Lista da Equipe — decidido: linhas sem borda/card, não tabela

**Atualização (2026-08-26, 2ª rodada):** a listagem de colaboradores da Área (RF-FEDCOLAB-002) trocou de `DsDataTable` (com bordas) para uma lista simples (`<ul>`/`<li>`, sem card nem tabela) — usuário observou que "os itens internos" do projeto não usam esse formato em outras páginas (referência: itens do diretório da sidebar). Cada linha tem um separador fino (`border-bottom`), não uma caixa com borda ao redor do item inteiro.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| FT-AREA | Bloqueante (satisfeita) | `APPROVED`; `GET /api/v1/areas/{id}`, `GET /api/v1/areas?singularId=` já existem |
| FT-SINGULAR | Bloqueante (satisfeita) | `APPROVED`; `GET /api/v1/singulares/{id}` já existe |
| FT-COLABORADOR | Bloqueante (satisfeita) | `APPROVED`; `GET /api/v1/colaboradores?areaId=` já existe (mesmo contrato de `ColaboradorListPage.vue`) |
| FT-DOCUMENTO | Não bloqueante | `DRAFT`; atalho aponta para lá, permanece inerte |
| FT-AREA-COLABORADOR | Nenhuma (paralela) | Feature irmã, mesmo padrão, escopo distinto ("minha área" vs. "qualquer área") — não modificada |

---

# Impactos

| Camada | Impacto |
|---|---|
| Backend | Nenhum. Nenhum endpoint novo, nenhuma alteração em contrato existente. |
| Frontend | Novas rotas/páginas (`FederacaoAreaHubPage`, `FederacaoAreaEquipePage`, `FederacaoSingularPage`); composables de leitura (`composables/federacao/`); `SidebarDirectorySection.vue`/`AppSidebar.vue` passam a gerar `to` navegável por item; `useLayoutMeta.ts` ganhou resolução de `:param` em breadcrumbs dinâmicos (reuso genérico, não específico desta Feature); novas strings i18n (`federacao.*`); renomeação de `layout.sidebar.singularLabel`/`servicesLabel`. |
| Banco de Dados | Nenhum. |
| APIs | Nenhuma alteração de contrato — consumo read-only de contratos já `APPROVED`. |
| Segurança | Nenhuma alteração de autenticação/autorização. Mesma observação de `FT-AREA-COLABORADOR`: os endpoints consumidos não têm escopo de autorização por vínculo organizacional — pendência de hardening já registrada, não introduzida por esta Feature. |

---

# Validação

- `test/e2e/federacao/federacao.spec.ts` — 2 cenários (hub de Área com Equipe navegável, botões ícone, campos mockados e botão de voltar; Singular → Áreas → hub de Área).
- `test/e2e/app-shell/app-shell.spec.ts` — botão de voltar removido do header confirmado; "Editar perfil" navegando para `/app/perfil` com dado real.
- Suíte completa do frontend verde no momento do fechamento (2ª rodada): 183 testes unitários, 40 testes E2E.

---

# Fontes

`specs/features/area-colaborador/specification.md` (padrão replicado); `specs/features/area/`, `specs/features/singular/`, `specs/features/colaborador/` (APIs consumidas); `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md` (nota de acompanhamento 2026-08-26).
