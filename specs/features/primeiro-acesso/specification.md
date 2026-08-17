# Feature Specification — FT-PRIMEIRO-ACESSO

| Campo | Valor |
|--------|--------|
| Template | crud-feature@1.1 (artefatos adaptados a workflow) |
| Versão | 1.0 |
| Status | APPROVED |
| Owner | Product Architecture |
| Decisões | DEC-FA-001, DEC-FA-002, DEC-FA-003, DEC-FA-004, DEC-ORG-001, DEC-ORG-003, DEC-CMS-001, DEC-DB-028, DH-02, DH-03, DH-04, DH-PA-01, DH-PA-02, DH-PA-03, DH-CARGO-01 |
| Categoria documental | SSOT |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-PRIMEIRO-ACESSO |
| Feature | Primeiro acesso e resolução de contexto |
| Domínio | SESSION / ORGANIZAÇÃO |
| Tipo | Workflow de primeiro acesso operacional |
| Status | APPROVED |

---

# 1. Objetivo

## Problema resolvido

Após a autenticação (FT-AUTH), o colaborador pode ainda não possuir `COLABORADOR` persistido com vínculo completo (DH-03, DH-PA-01). Sem esta Feature, o Portal não sabe:

- se o Primeiro Acesso é necessário;
- como conduzir o wizard de vínculo (domínio → Singular → Área → Equipe opcional);
- quando criar o `COLABORADOR`;
- como bloquear operação em estados inválidos (ex.: domínio sem Singular — BR-044);
- qual Home apresentar após vínculo completo.

Com COLABORADOR existente e vínculo completo (DH-02), o **Contexto Ativo** é **derivado** do único vínculo cadastral — não há estado cadastral separado nem seleção entre vínculos.

## Responsabilidades (exclusivas)

| # | Responsabilidade |
|---|------------------|
| 1 | Verificar necessidade de Primeiro Acesso após autenticação |
| 2 | Conduzir wizard de onboarding: domínio → Singular → Área → Equipe (opcional) — DH-PA-02, DEC-ORG-003, DH-04 |
| 3 | Criar `COLABORADOR` somente após vínculo completo — DH-03, DH-PA-03 |
| 4 | Derivar Contexto Ativo do vínculo único (projeção em sessão/UI; sem persistência separada — DH-02) |
| 5 | Encerrar o onboarding / primeiro acesso |
| 6 | Obter a Home dinâmica do backend |
| 7 | Redirecionar o frontend para a Home recebida |
| 8 | Bloquear acesso operacional em estados inválidos (domínio sem Singular, falhas de vínculo) |

### Responsabilidades superseded (modelo N vínculos — pré-DH-02)

| # | Responsabilidade histórica | Status |
|---|--------------------------|--------|
| — | Seleção de Contexto Ativo entre N vínculos | **SUPERSEDED** (DH-02) |
| — | Persistência separada de Contexto Ativo | **SUPERSEDED** — derivar de FKs de `COLABORADOR` |
| — | Alteração de Contexto Ativo em sessão operacional | **SUPERSEDED** (RF-PA-007, RN-PA-006) |

## Limites

- Inicia **após** autenticação bem-sucedida (FT-AUTH).
- Consome e atualiza estado via FT-SESSION (store / representação de sessão).
- Não autentica, não emite tokens, não gerencia CSRF/cookies.
- Não cadastra estrutura organizacional (FT-AREA, FT-SINGULAR, FT-EQUIPE, FT-COLABORADOR).

## Fora do escopo

- Login, logout, refresh, Zimbra (FT-AUTH).
- Hidratação genérica `/auth/me` como dono (FT-SESSION); esta Feature orquestra resolução/seleção.
- CRUD de Federação/Singular/Área/Equipe/Colaborador.
- Matriz de papéis/permissões avançada (Feature futura).
- Conteúdo editorial CMS (DEC-CMS-001).
- Provisionamento administrativo de vínculos (FT-COLABORADOR).
- Definição do layout visual da Home (apenas consumo do contrato).

---

# 2. Regras de Negócio Consolidadas

SSOT: `docs/domain/09-business-rules.md`. Esta Feature **referencia**, não reescreve.

| Código | Uso nesta Feature |
|--------|-------------------|
| **BR-010** | Sem vínculo com Área → bloqueio operacional; identidade sem COLABORADOR → onboarding; navegação no Contexto Ativo derivado |
| **BR-011** | Primeiro acesso = wizard de vínculo + criação de COLABORADOR antes da operação plena; CARGO não participa (DH-PA-03) |
| **BR-012** | Contexto coerente: federação + singular + área (+ equipe opcional) |
| **BR-040** | Hierarquia Federação → Singular → Área → Equipe → Colaborador |
| **BR-041** | 1 vínculo cadastral (DH-02); Contexto Ativo único derivado do vínculo |
| **BR-042** | Home determinada pelo backend após Contexto Ativo resolvido |
| **BR-043** | Domínio → Singular (DEC-ORG-003, DH-PA-02) |
| **BR-044** | Domínio sem Singular bloqueia PA automaticamente |
| **BR-045** | CARGO não obrigatório na criação (DH-CARGO-01) |

### Regras locais da Feature (não substituem BR)

| Código | Regra | Status |
|--------|-------|--------|
| RN-PA-001 | Vínculo válido exige `federationId`, `singularId` e `areaId` não nulos e entidades ativas na hierarquia | Vigente |
| RN-PA-002 | COLABORADOR com vínculo completo → Contexto Ativo derivado automaticamente do único vínculo, sem UI de seleção | Vigente (reconciliado DH-02) |
| RN-PA-003 | ~~Com N>1 vínculos → UI de seleção~~ | **SUPERSEDED** (DH-02) |
| RN-PA-004 | Identidade autenticada sem COLABORADOR → conduzir onboarding (credencial temporária — DH-PA-01); domínio sem Singular → bloqueio (BR-044) | Vigente (reconciliado) |
| RN-PA-005 | Contexto Ativo derivado das FKs de `COLABORADOR`; não usar `COD_*_CTX` em `AUTH_SESSAO`; sem persistência separada | Vigente (reconciliado) |
| RN-PA-006 | ~~Troca de contexto invalida Home anterior~~ | **SUPERSEDED** (DH-02) |
| RN-PA-007 | Reentrada com COLABORADOR e vínculo válido → derivar Contexto Ativo das FKs → LoadingHome sem wizard | Vigente (reconciliado) |
| RN-PA-008 | ~~Contexto selecionado deve pertencer ao conjunto de vínculos~~ | **SUPERSEDED** — com 1 vínculo, validação é de integridade do vínculo (RN-PA-001) |

---

# 3. Atores

| Ator | Descrição |
|------|-----------|
| Colaborador autenticado | Usuário com sessão FT-AUTH válida |
| Sistema (Backend) | Resolve vínculos, valida contexto, persiste, calcula Home |
| Sistema (Frontend) | Orquestra estados do fluxo PA (wizard de onboarding), renderiza Home |

---

# 4. Requisitos Funcionais

| ID | Descrição | Prioridade | UC | Status |
|----|-----------|------------|-----|--------|
| RF-PA-001 | Após autenticação, verificar necessidade de Primeiro Acesso | Must | UC-PA-001 | Vigente |
| RF-PA-002 | Derivar Contexto Ativo automaticamente do vínculo único quando COLABORADOR completo | Must | UC-PA-002, 008 | Vigente |
| RF-PA-003 | ~~Exigir seleção quando múltiplos vínculos~~ | — | UC-PA-003, 004 | **SUPERSEDED** |
| RF-PA-004 | ~~Persistir Contexto Ativo separadamente~~ | — | UC-PA-005 | **SUPERSEDED** — derivar de `COLABORADOR` |
| RF-PA-005 | Obter Home dinâmica do backend após Contexto Ativo resolvido | Must | UC-PA-006 | Vigente |
| RF-PA-006 | Redirecionar/renderizar Home recebida sem regra fixa no frontend | Must | UC-PA-006 | Vigente |
| RF-PA-007 | ~~Permitir alteração de Contexto Ativo em sessão~~ | — | UC-PA-007 | **SUPERSEDED** |
| RF-PA-008 | Na reentrada, derivar Contexto Ativo do vínculo do COLABORADOR | Must | UC-PA-008 | Vigente |
| RF-PA-009 | Conduzir onboarding ou bloquear conforme regras (domínio, vínculo) | Must | UC-PA-009 | Vigente |
| RF-PA-010 | Rejeitar e recuperar de vínculo/Contexto inválido | Must | UC-PA-010 | Vigente |
| RF-PA-011 | Conduzir wizard de vínculo e criar COLABORADOR ao final | Must | UC-PA-002 | Vigente (DH-03) |

---

# 5. Cenários de rastreabilidade (UC-PA-*)

Identificadores de cenário para rastreabilidade — não representam artefatos separados. UC superseded (003, 004, 005, 007) omitidos do normativo vigente.

| UC | Objetivo | RF | Pré-condição resumida | Resultado esperado |
|----|----------|-----|----------------------|-------------------|
| **UC-PA-001** | Hand-off pós-autenticação; verificar necessidade de PA | RF-PA-001 | FT-AUTH concluído; identidade válida (credencial PA se sem COLABORADOR — DH-PA-01) | Estado `CheckingColaborador`; encaminha onboarding (UC-PA-002) ou derivação (UC-PA-008) |
| **UC-PA-002** | Wizard de vínculo e criação de COLABORADOR | RF-PA-002, RF-PA-011 | Sem COLABORADOR ou vínculo incompleto | COLABORADOR criado (DH-03); Contexto derivado (DH-02); segue UC-PA-006 |
| **UC-PA-006** | Obter e renderizar Home do backend | RF-PA-005, RF-PA-006 | Contexto Ativo derivado válido; `LoadingHome` | Home renderizada; estado `Operational` |
| **UC-PA-008** | Reentrada com COLABORADOR existente | RF-PA-008 | Sessão AUTH válida; COLABORADOR com vínculo completo | Deriva contexto → `LoadingHome` → `Operational` (sem wizard) |
| **UC-PA-009** | Onboarding necessário ou bloqueio de negócio | RF-PA-009 | COLABORADOR ausente **ou** domínio sem Singular (BR-044) | Onboarding (UC-PA-002) **ou** `Blocked`; sem Home operacional |
| **UC-PA-010** | Contexto inválido | RF-PA-010 | Contexto presente mas inválido (RN-PA-001) | Invalida contexto; re-resolve (onboarding, bloqueio ou retry) |

### Exceções transversais (cenários)

| Código | Situação | Comportamento |
|--------|----------|---------------|
| FE-001 | Falha ao carregar vínculos / infra 5xx | `Error`; retry sem novo login se AUTH/credencial PA válida |
| FE-001 (onboarding) | Domínio sem Singular | UC-PA-009 → `Blocked` (BR-044, DH-PA-02) |
| FE-002 (onboarding) | Falha criação COLABORADOR | `Error`; retry se credencial PA válida |
| FE-001 (Home) | Home indisponível | Retry (RNF-PA-006); contexto derivado mantido |
| FE-002 (Home) | Descriptor inválido | `Error`; frontend não inventa Home |
| FE-001 (reentrada) | Sessão AUTH expirada | Encaminha FT-AUTH (fora desta Feature) |

---

# 6. Fluxo funcional TO-BE

```text
Authenticated (FT-AUTH — identidade)
        ↓
CheckingColaborador
        ↓
   ┌────┴────────────────┐
   │                     │
COLABORADOR          sem COLABORADOR
com vínculo          ou vínculo incompleto
completo                  │
   │                     OnboardingWizard
   │                     (domínio → Singular → Área → Equipe opt.)
   │                           ↓
   │                     CreatingColaborador (DH-03)
   │                           │
   └───────────┬───────────────┘
               ↓
    DeriveContext (Contexto Ativo = vínculo — DH-02)
               ↓
         LoadingHome (UC-PA-006)
               ↓
          Operational
```

### Fluxos alternativos

| ID | Descrição | UCs |
|----|-----------|-----|
| FA-001 | Reentrada com COLABORADOR — omite wizard | UC-PA-008 → UC-PA-006 |
| FA-002 | Retry após falha de Home — mesmo contexto derivado | UC-PA-006 |

### Fluxos de exceção

| ID | Descrição | UCs / estados |
|----|-----------|---------------|
| FE-001 | Domínio sem Singular (BR-044) | OnboardingWizard → `Blocked` |
| FE-002 | Vínculo inválido no COLABORADOR | invalidate → UC-PA-010 |
| FE-003 | Falha de infraestrutura | OnboardingWizard / CreatingColaborador / LoadingHome → `Error` |

---

# 7. Máquina de estados e transições

### Estados (TO-BE)

| Estado | Descrição | Operação permitida? |
|--------|-----------|---------------------|
| `Authenticated` | Identidade válida (FT-AUTH ou credencial temporária PA) | Não |
| `CheckingColaborador` | Verifica COLABORADOR e integridade do vínculo | Não |
| `OnboardingWizard` | Coleta vínculo: domínio → Singular → Área → Equipe (opt.) | Não |
| `CreatingColaborador` | Persiste COLABORADOR com vínculo completo (DH-03) | Não |
| `DeriveContext` | Deriva Contexto Ativo do vínculo único (DH-02) | Não (transiente) |
| `LoadingHome` | Solicita Home ao backend | Não |
| `Operational` | Contexto derivado + Home ok | Sim |
| `Blocked` | Bloqueio de negócio (ex.: domínio sem Singular) | Não |
| `Error` | Falha recuperável do fluxo | Não |

Estados superseded (pré-DH-02): `LoadingContexts`, `SelectingContext`, `PersistingContext`, `ChangingContext` — não fazem parte do TO-BE vigente.

### Eventos e transições principais

| De | Evento | Condição | Para |
|----|--------|----------|------|
| Authenticated | `AUTH_OK` | — | CheckingColaborador |
| CheckingColaborador | `COLABORADOR_COMPLETE` | vínculo válido (RN-PA-001) | DeriveContext |
| CheckingColaborador | `COLABORADOR_MISSING` | — | OnboardingWizard |
| OnboardingWizard | `DOMAIN_NO_SINGULAR` | — | Blocked |
| OnboardingWizard | `WIZARD_COMPLETE` | — | CreatingColaborador |
| CreatingColaborador | `COLABORADOR_CREATED` | — | DeriveContext |
| CreatingColaborador | `FAILURE` | — | Error |
| DeriveContext | `CONTEXT_DERIVED` | — | LoadingHome |
| LoadingHome | `HOME_LOADED` | — | Operational |
| LoadingHome | `FAILURE` | — | Error |
| Error | `RETRY` | auth/credencial válida | estado anterior recuperável |

### Códigos de falha lógica

| Código | Estado | Comportamento |
|--------|--------|---------------|
| `PA_DOMAIN_NO_SINGULAR` | Blocked | Domínio sem Singular; informar usuário (BR-044) |
| `PA_VINCULO_INVALID` | Error / OnboardingWizard | Vínculo inconsistente |
| `PA_CREATE_FAILED` | Error | Falha ao criar COLABORADOR |
| `PA_HOME_FAILED` | Error | Retry mantendo contexto derivado |

A store FT-SESSION reflete vínculo único (`organizationalLinks`), `activeContext` como projeção derivada (DH-02) e `isReady` quando `Operational`. A máquina de estados define o fluxo de PA; a store não redefine regras.

---

# 8. Requisitos Não Funcionais

| ID | Categoria | Requisito |
|----|-----------|-----------|
| RNF-PA-001 | Desempenho | Resolução de vínculos + Home (caminho 1 vínculo) ≤ 2s p95 em condições normais de rede local/dev |
| RNF-PA-002 | Segurança | Somente o colaborador autenticado acessa seus vínculos/contexto; validação server-side de pertencimento (RN-PA-008) |
| RNF-PA-003 | Segurança | CMS não participa de autorização nem de contexto (DEC-CMS-001) |
| RNF-PA-004 | Auditoria | Registrar eventos: contexto selecionado, contexto alterado, bloqueio sem vínculo, contexto inválido |
| RNF-PA-005 | Observabilidade | Métricas/logs: tempo `CheckingColaborador` e `OnboardingWizard`, falhas `LoadingHome`, bloqueios (`Blocked`), taxa de conclusão do wizard |
| RNF-PA-006 | Disponibilidade | Falha ao obter Home não deve corromper Contexto Ativo já persistido; permitir retry |
| RNF-PA-007 | Usabilidade | Wizard de onboarding com passos legíveis (Singular resolvida, Área, Equipe opcional) |
| RNF-PA-008 | Rastreabilidade | Estados e transições alinhados a § 7 desta especificação; correlacionar requestId nos logs de fluxo |

---

# 9. Contexto Ativo — modelo conceitual

O Contexto Ativo **não** é entidade cadastral separada. Com DH-02, é **projeção derivada** do único vínculo de `COLABORADOR`:

```text
COLABORADOR
    ↓
único vínculo cadastral (FKs escalares)
    ↓
Contexto Ativo (projeção para sessão/UI/navegação)
├── federationId  (obrigatório)
├── singularId    (obrigatório)
├── areaId        (obrigatório)
└── teamId        (opcional)
```

- **Não** introduzir tabela, coluna, entidade ou persistência adicional para Contexto Ativo.
- `activeContext` em store de sessão representa a projeção derivada, não um segundo vínculo selecionável.
- REF-DB-CTX-01: `AUTH_SESSAO` não armazena contexto organizacional.

---

# 10. Matriz de Responsabilidades

| Responsabilidade | FT-AUTH | FT-SESSION | FT-PRIMEIRO-ACESSO |
|------------------|---------|------------|---------------------|
| Login / logout / refresh | ✅ | ❌ | ❌ |
| Cookies / CSRF / JWT / credencial temporária PA | ✅ | ❌ | consome |
| Hidratar identidade (`/auth/me`) | ❌ | ✅ | consome |
| Guardar user + vínculo + `activeContext` derivado na store | ❌ | ✅ | atualiza após PA |
| Wizard de onboarding (domínio → vínculo) | ❌ | ❌ | ✅ |
| Criar COLABORADOR após vínculo completo | ❌ | ❌ | ✅ |
| Derivar Contexto Ativo do vínculo | ❌ | ✅ (hidratação) | ✅ (pós-onboarding) |
| Obter Home dinâmica | ❌ | ❌ | ✅ |
| Renderizar Home | ❌ | ❌ | ✅ (frontend da Feature) |
| Bloquear estados inválidos (domínio sem Singular, etc.) | ❌ | indica estado | ✅ |
| CMS conteúdo | ❌ | ❌ | ❌ (DEC-CMS-001) |

Detalhamento de contratos: `api.md`. Cenários, fluxos e estados: § 5–7 desta especificação.

---

# 11. Impacto Arquitetural

| Área | Impacto |
|------|---------|
| FT-AUTH | Hand-off pós-autenticação; credencial temporária PA (DH-PA-01); sem criação de COLABORADOR no login |
| FT-SESSION | Expõe vínculo único + `activeContext` derivado; sem seleção N vínculos |
| FT-COLABORADOR | COLABORADOR criado pelo PA (onboarding), não por `locateOrCreate` no login |
| FT-AREA / FT-SINGULAR | Referenciados no wizard e validação de vínculo |
| Frontend | Wizard PA; guards; remove landing fixa `/app` como oficial |
| Backend | Contratos em `api.md`; onboarding — **detalhe técnico de API pendente de implementação** |
| Persistência | Vínculo nas FKs de `COLABORADOR`; sem store separado de Contexto Ativo |
| CARGO | Não participa do PA (DH-PA-03, DH-CARGO-01); domínio TO-BE sem DDL implementado |
| Autorização | Operação no Contexto Ativo derivado; `PAPEL_ATRIBUICAO` ortogonal (DEC-DB-020) |
| Navegação / Home | Home dinâmica pós-contexto derivado |
| CMS | **Nenhuma alteração** (DEC-CMS-001) |

---

# 12. Artefatos da Feature

| Artefato | Conteúdo |
|----------|----------|
| `feature.yaml` | Identidade e dependências |
| `specification.md` | Este documento — SSOT funcional (objetivo, BR, RF, cenários, fluxos, estados) |
| `api.md` | Contratos de API |
| `acceptance-tests.md` | Critérios de aceite testáveis |
| `traceability.md` | Matriz RF → RN → UC → API → AT → TK |

---

# 13. Consistência com o SSOT

Esta especificação alinha-se a:

- `docs/governance/03-open-decisions.md` (DEC-FA-*, DEC-ORG-*, DH-*, DH-PA-*, DH-CARGO-01)
- `docs/domain/09-business-rules.md`
- `docs/frontend/frontend-flow.md`
- `specs/features/session/specification.md`
- `construction/review/contexto-ativo-dh02-investigacao.md`

---

# 14. Governança — modelo normativo consolidado

Decisões vigentes que definem o TO-BE desta Feature:

| Decisão | Regra aplicável |
|---------|-----------------|
| **DH-02** | 1 vínculo cadastral por COLABORADOR; Contexto Ativo derivado |
| **DH-03** | COLABORADOR persistido somente após vínculo completo |
| **DH-04** | Federação + Singular + Área obrigatórios; Equipe opcional |
| **DH-PA-01** | Credencial temporária PA; sem `AUTH_SESSAO` operacional durante onboarding |
| **DH-PA-02** | Domínio → Singular 1:1; domínio sem Singular bloqueia PA |
| **DH-PA-03** | COLABORADOR criado ao final do PA; CARGO não participa |
| **DH-CARGO-01** | CARGO opcional em qualquer criação de COLABORADOR |
| **DEC-ORG-003** | Domínio determina Singular; Área selecionada pelo usuário |
| **DEC-DB-028** | Modelo físico de vínculo único (TO-BE em DDL — pendente migration) |

### Fluxo normativo TO-BE

```text
Autenticação (FT-AUTH)
    ↓
identidade validada
    ↓
COLABORADOR existe com vínculo completo?
    ├─ não → onboarding (credencial temporária — DH-PA-01)
    │         domínio → Singular → Área → Equipe (opcional)
    │         → criar COLABORADOR (DH-03)
    └─ sim  → derivar Contexto Ativo do vínculo (DH-02)
    ↓
Home dinâmica (DEC-FA-004)
    ↓
Operacional
```

**Reconciliação documental:** concluída em 2026-08-17 (`construction/review/baseline-saneamento.md`). Artefatos PA alinhados ao TO-BE; implementação de código permanece pendente.

**GAPs de implementação (não resolvidos nesta etapa):** GAP-028-01 (`locateOrCreate`), GAP-028-03 (credencial temporária), GAP-028-04 (mapeamento domínio→Singular), GAP-028-02 (NOT NULL em DDL).
