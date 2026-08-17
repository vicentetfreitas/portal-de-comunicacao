# Acceptance Tests — FT-PRIMEIRO-ACESSO

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Status | APPROVED (reconciliado 2026-08-17) |
| Versão | 1.1 |

Estratégia: `docs/implementation/08-testing-strategy.md`.  
Este documento define critérios **testáveis**; não implementa testes automatizados.

---

# AT-PA-001 — COLABORADOR existente entra com contexto derivado

### RF / UC / BR

RF-PA-002 · UC-PA-008 · BR-011, BR-041 · RN-PA-002

### Given

Colaborador autenticado com COLABORADOR persistido e vínculo completo (DH-02).

### When

Inicia sessão / reentrada.

### Then

- Não exibe wizard de onboarding
- Contexto Ativo derivado do único vínculo (`organizationalLinks`)
- Home é solicitada e apresentada
- Estado final `Operational`

---

# AT-PA-002 — [SUPERSEDED] Múltiplos vínculos exigem seleção

> **SUPERSEDED** (DH-02, 2026-08-17). Texto histórico preservado.

### RF / UC / BR (histórico)

RF-PA-003 · UC-PA-003, 004 · BR-041 · RN-PA-003

### Given

Colaborador autenticado com N>1 vínculos válidos.

### When

Inicia primeiro acesso.

### Then

- Estado `SelectingContext`
- Operação bloqueada até seleção
- Após seleção válida → persistência → Home → `Operational`

---

# AT-PA-003 — Identidade sem COLABORADOR conduz onboarding

### RF / UC / BR

RF-PA-001, RF-PA-011 · UC-PA-001, 002, 009 · BR-010, BR-011 · RN-PA-004

### Given

Identidade autenticada (credencial temporária PA) sem COLABORADOR persistido.

### When

Inicia Primeiro Acesso.

### Then

- Estado `OnboardingWizard` (não `Operational`)
- Nenhuma Home operacional até criação do COLABORADOR
- Credencial PA / autenticação não força logout automático
- Rotas operacionais inacessíveis

---

# AT-PA-004 — Contexto Ativo derivado consistente na sessão

### RF / UC / BR

RF-PA-002 · UC-PA-008 · BR-010, BR-041 · RN-PA-005

### Given

COLABORADOR com vínculo completo.

### When

Navega em rotas operacionais / recarrega com sessão válida.

### Then

- `activeContext` = projeção de `organizationalLinks` (FT-SESSION)
- Não há divergência entre store e `GET /auth/me`

---

# AT-PA-005 — [SUPERSEDED] Troca de contexto altera operação

> **SUPERSEDED** (DH-02, RF-PA-007, 2026-08-17). Texto histórico preservado.

### RF / UC / BR (histórico)

RF-PA-007 · UC-PA-007 · BR-042 · RN-PA-006

### Given

Usuário `Operational` com N>1 vínculos.

### When

Altera Contexto Ativo.

### Then

- Novo contexto persistido
- Home anterior descartada
- Nova Home carregada do backend
- Estado retorna a `Operational`

---

# AT-PA-006 — Home exclusiva do backend

### RF / UC / BR

RF-PA-005, 006 · UC-PA-006 · BR-042 · DEC-FA-004

### Given

Contexto Ativo válido.

### When

`GET /session/home` retorna descriptor H.

### Then

- Frontend renderiza H
- Frontend não aplica fallback de landing fixa (`/app`) como regra de negócio

---

# AT-PA-007 — Reentrada com COLABORADOR e vínculo válido

### RF / UC / BR

RF-PA-008 · UC-PA-008 · RN-PA-007

### Given

COLABORADOR com vínculo completo; nova carga da aplicação com AUTH ok.

### When

Reentrada.

### Then

- Deriva Contexto Ativo das FKs → `LoadingHome` sem wizard
- Resultado `Operational`

---

# AT-PA-008 — Contexto inválido é recuperado

### RF / UC / BR

RF-PA-010 · UC-PA-010 · RN-PA-001, RN-PA-008

### Given

Contexto Ativo persistido aponta para área inativa / não pertencente.

### When

Validação na reentrada ou operação.

### Then

- Contexto invalidado
- Fluxo re-resolve vínculo (onboarding ou bloqueio)
- Não permanece `Operational` com contexto inválido

---

# AT-PA-009 — [SUPERSEDED] Seleção rejeita contexto alheio

> **SUPERSEDED** (DH-02, 2026-08-17). Sem seleção entre vínculos. Texto histórico preservado.

### RF / UC / BR (histórico)

RF-PA-003 · UC-PA-004 · RN-PA-008 · RNF-PA-002

### Given

Usuário em `SelectingContext`.

### When

Envia `PUT /session/context` com vínculo de outro colaborador.

### Then

- HTTP 403 ou 422
- Permanece sem Contexto Ativo operacional

---

# AT-PA-010 — CMS não participa

### RF / BR / DEC

RNF-PA-003 · DEC-CMS-001

### Given

Fluxo de primeiro acesso em execução.

### When

Observa dependências de autorização/contexto.

### Then

- Nenhuma chamada ao CMS para resolver vínculo, contexto ou Home operacional
- Autorização permanece no Portal
