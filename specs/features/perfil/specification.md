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
2. **Autorização de autoatendimento:** `PUT /api/v1/colaboradores/{id}` (`FT-COLABORADOR`) tem como ator documentado apenas "Administrador" — não está confirmado que o próprio colaborador pode invocar esse endpoint sobre seu próprio `id`. Precisa de decisão explícita (reaproveitar o endpoint com regra de autorização adicional "self", ou criar endpoint dedicado `/api/v1/auth/me` com `PUT`).
3. **Rota `/perfil/editar` com contexto de equipe** — o mapeamento do legado (`docs/discovery/frontend-feature-mapping.md:711`) registra uma variante de rota "with/without team routes" para edição de perfil; não há evidência no Figma auditado (só 1 tela) de que essa variação seja necessária no TO-BE.

Nenhuma dessas três perguntas é decidível por evidência de código isoladamente — a primeira e a segunda são achados concretos (não suposições) que bloqueiam `RF-PERFIL-001`/`002` até resposta.

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
