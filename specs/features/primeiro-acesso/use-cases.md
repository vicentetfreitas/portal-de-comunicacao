# Use Cases — FT-PRIMEIRO-ACESSO

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Status | APPROVED (reconciliado 2026-08-17) |
| Versão | 1.1 |

---

# Convenções

```text
UC-PA-001 … UC-PA-010
FA-001 … (fluxos alternativos)
FE-001 … (fluxos de exceção)
```

Mapeamento pedido → ID oficial:

| Pedido | ID |
|--------|-----|
| UC-01 Primeiro Login | UC-PA-001 |
| UC-02 Login com um vínculo | UC-PA-002 |
| UC-03 Login com múltiplos vínculos | UC-PA-003 |
| UC-04 Seleção de Contexto | UC-PA-004 |
| UC-05 Persistência do Contexto | UC-PA-005 |
| UC-06 Recuperação da Home | UC-PA-006 |
| UC-07 Alteração de Contexto | UC-PA-007 |
| UC-08 Reentrada no sistema | UC-PA-008 |
| UC-09 Usuário sem vínculo | UC-PA-009 |
| UC-10 Contexto inválido | UC-PA-010 |

---

# UC-PA-001 — Hand-off pós-autenticação

### Objetivo

Iniciar o fluxo de Primeiro Acesso imediatamente após autenticação bem-sucedida e verificar necessidade de onboarding.

### Atores

- Colaborador autenticado (ou identidade com credencial temporária PA — DH-PA-01)
- Sistema (Frontend / Backend)

### Pré-condições

- FT-AUTH concluiu autenticação; identidade válida
- FT-SESSION pode hidratar identidade (quando COLABORADOR existir)

### Pós-condições

- Feature em estado `CheckingColaborador`
- Encaminhamento para onboarding (UC-PA-002) ou derivação de contexto (UC-PA-008)

### Fluxo principal

1. Frontend detecta autenticação válida.
2. Sistema inicia FT-PRIMEIRO-ACESSO (`Authenticated` → `CheckingColaborador`).
3. Sistema verifica existência de COLABORADOR com vínculo completo (DH-03, DH-04).
4. Se ausente/incompleto → UC-PA-002 (onboarding).
5. Se completo → derivar Contexto Ativo → UC-PA-006 / UC-PA-008.

### Exceções

**FE-001 — Falha ao carregar vínculos**

1. Backend indisponível ou erro 5xx.
2. Sistema transita para `Error`.
3. Colaborador pode tentar novamente sem novo login (enquanto sessão AUTH válida).

---

# UC-PA-002 — Onboarding: construção do vínculo e criação do COLABORADOR

### Objetivo

Conduzir o wizard de Primeiro Acesso, coletar vínculo organizacional completo e criar `COLABORADOR` (DH-03, DH-PA-02, DH-PA-03).

### Atores

- Colaborador autenticado (credencial temporária PA — DH-PA-01)
- Sistema

### Pré-condições

- UC-PA-001 em andamento
- Identidade sem COLABORADOR persistido ou vínculo incompleto

### Pós-condições

- COLABORADOR criado com vínculo completo (Federação + Singular + Área + Equipe opcional)
- Contexto Ativo derivado do vínculo (DH-02)
- Sem CARGO obrigatório (DH-CARGO-01)

### Fluxo principal

1. Sistema resolve domínio do e-mail → Singular (DEC-ORG-003, DH-PA-02).
2. Se domínio sem Singular → UC-PA-009 (bloqueio).
3. Colaborador seleciona Área (+ Equipe opcional) dentro da Singular.
4. Sistema cria COLABORADOR com vínculo completo (DH-03).
5. Sistema deriva Contexto Ativo das FKs.
6. Sistema executa UC-PA-006.

### Exceções

**FE-001 — Domínio sem Singular cadastrada**

1. Encaminha UC-PA-009 (bloqueio informativo — BR-044).

**FE-002 — Falha na criação do COLABORADOR**

1. Estado `Error`; retry sem novo login se credencial PA válida.

---

# UC-PA-003 — [SUPERSEDED] Login com múltiplos vínculos

> **SUPERSEDED** (DH-02, 2026-08-17). Não há N vínculos cadastrais. Texto histórico preservado.

### Objetivo (histórico)

Conduzir o colaborador à seleção obrigatória quando há N>1 vínculos válidos.

### Atores

- Colaborador autenticado
- Sistema

### Pré-condições

- UC-PA-001 em andamento
- N>1 vínculos válidos (BR-041, RN-PA-003)

### Pós-condições

- Sistema em `SelectingContext`
- Lista de vínculos disponível na UI

### Fluxo principal

1. Sistema identifica N>1 vínculos válidos.
2. Sistema apresenta lista para seleção (UC-PA-004).
3. Após seleção válida → UC-PA-005 → UC-PA-006.

### Exceções

**FE-001 — Lista vazia após filtro de validade**

1. Todos inválidos → UC-PA-009.

---

# UC-PA-004 — [SUPERSEDED] Seleção de Contexto

> **SUPERSEDED** (DH-02, 2026-08-17). Sem seleção entre vínculos cadastrais.

### Objetivo (histórico)

Permitir que o colaborador escolha o Contexto Ativo dentre vínculos válidos.

### Atores

- Colaborador autenticado

### Pré-condições

- Estado `SelectingContext`
- Lista de vínculos válidos não vazia

### Pós-condições

- Contexto Ativo candidato definido
- Pronto para persistência

### Fluxo principal

1. Colaborador visualiza vínculos (singular, área, equipe opcional).
2. Colaborador seleciona um vínculo.
3. Sistema valida pertencimento (RN-PA-008).
4. Sistema segue para UC-PA-005.

### Exceções

**FE-001 — Seleção de vínculo não pertencente ao colaborador**

1. Rejeita com erro de autorização/negócio.
2. Mantém `SelectingContext`.

**FE-002 — Cancelamento sem seleção**

1. Colaborador permanece sem Contexto Ativo.
2. Acesso operacional continua bloqueado.

---

# UC-PA-005 — [SUPERSEDED] Persistência do Contexto

> **SUPERSEDED** (DH-02, 2026-08-17). Contexto Ativo derivado das FKs de `COLABORADOR`; sem persistência separada.

### Objetivo (histórico)

Persistir o Contexto Ativo para a sessão/colaborador sem usar `COD_*_CTX` em `AUTH_SESSAO`.

### Atores

- Sistema (Backend)
- FT-SESSION (atualização de estado)

### Pré-condições

- Contexto Ativo candidato válido (RN-PA-001, RN-PA-008)

### Pós-condições

- Contexto Ativo persistido
- FT-SESSION reflete Contexto Ativo
- Estado `PersistingContext` → sucesso

### Fluxo principal

1. Frontend envia seleção (ou auto-seleção) ao backend.
2. Backend valida e persiste (mecanismo físico: lacuna de Construction — REF-DB-CTX-01).
3. Backend confirma Contexto Ativo atual.
4. FT-SESSION atualiza store.

### Exceções

**FE-001 — Falha de persistência**

1. Estado `Error` ou retorno a `SelectingContext` / retry.
2. Contexto Ativo em memória não é considerado operacional até confirmação.

---

# UC-PA-006 — Recuperação da Home

### Objetivo

Obter e apresentar a Home determinada exclusivamente pelo backend (BR-042).

### Atores

- Sistema
- Colaborador autenticado

### Pré-condições

- Contexto Ativo persistido e válido
- Estado `LoadingHome`

### Pós-condições

- Home recebida e renderizada
- Estado `Operational`
- Onboarding/primeiro acesso encerrado

### Fluxo principal

1. Sistema solicita Home ao backend com Contexto Ativo implícito/explícito.
2. Backend retorna descriptor de Home (rota/componente/payload conforme contrato).
3. Frontend **apenas renderiza** o que recebeu (sem regra fixa de landing).
4. Sistema marca primeiro acesso como concluído nesta sessão.

### Exceções

**FE-001 — Home indisponível**

1. Erro transitório → retry (RNF-PA-006).
2. Contexto Ativo permanece; estado pode ser `Error` recuperável.

**FE-002 — Home vazia/ inválida**

1. Backend retorna contrato inválido.
2. Estado `Error`; não inventar Home no frontend.

---

# UC-PA-007 — [SUPERSEDED] Alteração de Contexto

> **SUPERSEDED** (DH-02, RF-PA-007, 2026-08-17). Sem troca de vínculo cadastral em sessão.

### Objetivo (histórico)

Trocar o Contexto Ativo durante sessão operacional e recalcular Home.

### Atores

- Colaborador autenticado

### Pré-condições

- Estado `Operational`
- N≥2 vínculos válidos (ou vínculo adicional disponível)

### Pós-condições

- Novo Contexto Ativo persistido
- Nova Home carregada
- Estado `Operational`

### Fluxo principal

1. Colaborador solicita troca de contexto.
2. Estado `ChangingContext` → (opcionalmente) `SelectingContext`.
3. UC-PA-004 → UC-PA-005 → UC-PA-006 (RN-PA-006).
4. Retorna a `Operational`.

### Exceções

**FE-001 — Único vínculo**

1. Troca indisponível; mantém contexto atual.

**FE-002 — Novo contexto inválido**

1. UC-PA-010; pode restaurar contexto anterior se ainda válido.

---

# UC-PA-008 — Reentrada no sistema

### Objetivo

Recuperar operação em nova visita/refresh com sessão AUTH válida e COLABORADOR existente.

### Atores

- Colaborador autenticado
- Sistema

### Pré-condições

- Sessão FT-AUTH válida
- COLABORADOR com vínculo completo

### Pós-condições

- Contexto Ativo derivado das FKs → `LoadingHome` → `Operational`
- Se COLABORADOR ausente → UC-PA-001 (onboarding)

### Fluxo principal

1. Frontend hidrata FT-SESSION (`GET /auth/me`).
2. Sistema lê vínculo de `organizationalLinks`.
3. Se vínculo válido (RN-PA-001) → deriva Contexto Ativo → UC-PA-006.
4. Se COLABORADOR ausente → UC-PA-002.
5. Se vínculo inválido → UC-PA-010.

### Exceções

**FE-001 — Sessão AUTH expirada**

1. Encaminha a FT-AUTH (fora desta Feature).

---

# UC-PA-009 — Bloqueio e onboarding necessário

### Objetivo

Tratar identidade autenticada sem COLABORADOR (onboarding) ou bloqueio de negócio (domínio sem Singular).

### Atores

- Colaborador autenticado

### Pré-condições

- Autenticado (ou credencial temporária PA)
- COLABORADOR ausente **ou** domínio sem Singular (BR-044)

### Pós-condições

- Onboarding iniciado (COLABORADOR ausente) **ou** estado `Blocked` (domínio sem Singular)
- Sem Home operacional até resolução

### Fluxo principal

1. **Caso A — sem COLABORADOR:** encaminha UC-PA-002 (onboarding); operação negada até conclusão.
2. **Caso B — domínio sem Singular:** estado `Blocked`; frontend informa usuário (DH-PA-02.2).
3. Autenticação/credencial PA pode permanecer (não é logout automático).

---

# UC-PA-010 — Contexto inválido

### Objetivo

Tratar Contexto Ativo inconsistente, expirado ou não pertencente ao colaborador.

### Atores

- Sistema
- Colaborador autenticado

### Pré-condições

- Contexto Ativo presente porém inválido (RN-PA-001, RN-PA-008) ou entidades inativas

### Pós-condições

- Contexto Ativo limpo ou substituído
- Reentrada no fluxo de resolução ou bloqueio

### Fluxo principal

1. Sistema detecta invalidade (na carga, persistência ou operação).
2. Sistema invalida Contexto Ativo atual.
3. Sistema recarrega vínculo de `COLABORADOR`.
4. Se COLABORADOR ausente → UC-PA-002; se vínculo inválido persistente → UC-PA-009 (bloqueio) ou retry onboarding.

### Exceções

**FE-001 — Loop de invalidação**

1. Após falha repetida → `Error` com opção de suporte/retry.
