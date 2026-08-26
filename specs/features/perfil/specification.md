# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — autoatendimento, não CRUD administrativo) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PERFIL |
| Feature | Perfil do Usuário |
| Domínio | COLABORADOR (visão de autoatendimento) |
| Tipo | Frontend de leitura/edição sobre dados já existentes de `FT-COLABORADOR` |
| Status | DRAFT |

---

# Objetivo

Permitir que o colaborador autenticado visualize e edite os próprios dados de perfil — nome, cargo, e-mail, ramal, outros contatos — sem acessar dados de terceiros.

**Fonte de evidência visual:** `AUDITORIA-DS-FIGMA-01.md` — frame `Perfil do Usuário` (node `192:114`): campos Nome, Cargo, E-mail, Ramal, Outros contatos; link "Editar perfil" já presente no bloco de sidebar reutilizado em todas as telas (`DsProfileSummary`, já `CONFORME`).

---

# Escopo

## Incluído

- Página de visualização do próprio perfil.
- Página de edição do próprio perfil (campos editáveis — ver decisão pendente).

## Fora do Escopo

- Visualização/edição de perfil de terceiros — permanece em `FT-COLABORADOR` (admin).
- Alteração de e-mail — `UpdateColaboradorRequest` (`FT-COLABORADOR`) já não inclui `email` no payload de atualização; esta Feature herda a mesma restrição.
- Alteração de vínculo organizacional (Área, Equipe, gestor) — pertence a `FT-COLABORADOR`/fluxos administrativos, não a autoatendimento.

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador autenticado | Leitura e edição limitada dos próprios dados |

---

# Requisitos Funcionais

## RF-PERFIL-001 — Visualizar próprio perfil

| Campo | Valor |
|--------|--------|
| Identificador | RF-PERFIL-001 |
| Descrição | O sistema deve exibir os dados de perfil do usuário autenticado. |

## RF-PERFIL-002 — Editar próprio perfil

| Campo | Valor |
|--------|--------|
| Identificador | RF-PERFIL-002 |
| Descrição | O sistema deve permitir que o usuário autenticado edite os campos editáveis do próprio perfil (ver decisão pendente para a lista exata). |

---

# Decisão de produto/arquitetura pendente

Verificação do backend existente antes desta especificação encontrou uma lacuna real de dados, não apenas de UI:

1. **Campos ausentes no contrato atual:** `GET /api/v1/auth/me` (`AuthenticatedUserResponse`) expõe `id`, `email`, `name`, `permissions` — não expõe `cargo`, `ramal` nem "outros contatos", todos visíveis no frame Figma. `ColaboradorResponse` (`GET /api/v1/colaboradores/{id}`, admin) também não lista explicitamente `cargo`/`ramal`/telefone entre seus campos documentados (`specs/features/colaborador/api.md`) — `DEC-ORG-002` menciona `CARGO` como entidade de domínio própria, o que sugere um relacionamento ainda não exposto neste contrato. **Decisão pendente:** esses campos existem no modelo de dados e só não estão expostos, ou precisam ser adicionados?
2. **Autorização de autoatendimento — CONFIRMADO bloqueado (2026-08-26):** não era mais "não confirmado" — foi verificado diretamente no código do backend. `ColaboradorApplicationService.update()` (o único endpoint de escrita de `FT-COLABORADOR`, `PUT /api/v1/colaboradores/{id}`) chama `organizationAuthorizationService.ensureOrganizationAdministrator(colaboradorId)`, que delega a `SessionAdministratorAuthorizationService.ensureSessionAdministrator()` — essa checagem compara o e-mail do solicitante contra uma **allowlist fixa de e-mails admin** (`AuthProperties.sessionAdministratorEmails()`), **sem nenhuma exceção para "o próprio colaborador"**. Um colaborador comum (fora da allowlist) que tente `PUT` sobre o próprio `id` recebe `403 Forbidden`. Isso confirma, por evidência de código (não suposição), que autoatendimento real de escrita **não é possível hoje** sem uma das duas mudanças: (a) endpoint dedicado de self-service (ex.: `PUT /api/v1/auth/me`) sem a checagem de admin, ou (b) alterar `ensureOrganizationAdministrator`/`ensureSessionAdministrator` para permitir `colaboradorId == id` como caso adicional válido.
3. **Rota `/perfil/editar` com contexto de equipe** — o mapeamento do legado (`docs/discovery/frontend-feature-mapping.md:711`) registra uma variante de rota "with/without team routes" para edição de perfil; não há evidência no Figma auditado (só 1 tela) de que essa variação seja necessária no TO-BE.

Nenhuma dessas três perguntas é decidível por evidência de código isoladamente — a primeira segue em aberto; a segunda agora tem resposta concreta (bloqueado) mas ainda depende de uma decisão de produto/arquitetura sobre qual caminho (a) ou (b) seguir.

## Implementação interina no frontend (2026-08-26) — não satisfaz RF-PERFIL-001/002

Por pedido explícito do usuário ("torne funcional o botão editar perfil"), `/app/perfil` (`PerfilPage.vue`) foi implementada como uma **interina**, não como o RF-PERFIL-002 real:

- **E-mail de login**: real, somente leitura (`GET /auth/me`, já carregado, zero chamada nova).
- **Nome, Cargo, E-mail adicional, Telefones, Ramais, Celulares**: editáveis, mas persistidos **só em `localStorage` do navegador** (`usePerfilLocalFields.ts`, chave por `colaborador.id`) — não chegam ao backend. Mesmo `Nome`, que tem campo real em `UpdateColaboradorRequest`, não pode ser salvo de verdade pelo motivo do item 2 acima.
- UI deixa isso explícito para o usuário (aviso permanente na página, não é um detalhe escondido).

**Isto não fecha `RF-PERFIL-001`/`002`** nem muda o `status: DRAFT` desta Feature — é um paliativo de UX enquanto a decisão de arquitetura do item 2 não é tomada. Quando houver endpoint/regra real, migrar `usePerfilLocalFields` para consumir a API de verdade é o próximo passo natural (a página já está estruturada para isso — trocar a fonte de dado, não o layout).

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| FT-COLABORADOR | Bloqueante parcial | `APPROVED`, mas contrato atual não cobre todos os campos exibidos no Figma nem autorização de autoatendimento |
| FT-AUTH | Bloqueante (satisfeita) | `GET /api/v1/auth/me` já existe e cobre parte dos dados |
| Design System (`ds/`) | Não bloqueante | `DsProfileSummary`, `DsInput`, `DsAvatar` já `CONFORME` |

---

# Fontes

`docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`; `backend/.../AuthController.java`, `AuthenticatedUserResponse.java`; `specs/features/colaborador/api.md`; `docs/governance/03-open-decisions.md` (DEC-ORG-002); `docs/discovery/frontend-feature-mapping.md`.
