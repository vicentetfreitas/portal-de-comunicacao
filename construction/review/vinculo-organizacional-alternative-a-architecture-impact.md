# Análise arquitetural — Alternativa A: COLABORADOR somente após vínculo organizacional

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/vinculo-organizacional-alternative-a-architecture-impact.md` |
| Data | 2026-08-14 |
| Tipo | Análise arquitetural e de governança — **sem implementação** |
| Status | **EVIDÊNCIA PARA EVOLUÇÃO FUTURA** |
| DH-02 | **CONFIRMADA** — 1 vínculo por COLABORADOR |
| DH-03 | **Alternativa A selecionada pelo decisor** |
| DH-04 | **Pendente de formalização** |
| DH-PA-01 | **APROVADA** (2026-08-15) — credencial temporária de Primeiro Acesso |

**Classificação usada:** `FATO` · `INFERÊNCIA` · `LACUNA` · `CONTRADIÇÃO` · `IMPLEMENTAÇÃO AS-IS` · `TO-BE (decisor)`.

> **Atualização (2026-08-15):** **DH-PA-01** aprovada. Registro em `docs/governance/03-open-decisions.md`. Texto analítico abaixo permanece como evidência histórica.

**Restrições cumpridas:** nenhum código, DDL, migration, JPA, API, frontend, teste, seed, decisão ou artefato de governança foi alterado.

---

## 1. Objetivo

Determinar como o sistema atual precisa evoluir para suportar a **Alternativa A** (DH-03) e a nova regra de negócio **domínio do e-mail → Singular**, sem implementar mudanças.

Vínculo mínimo TO-BE:

```text
FEDERAÇÃO + SINGULAR + ÁREA (+ EQUIPE opcional)
```

---

## 2. Decisões já estabelecidas

| ID | Decisão | Status |
|----|---------|--------|
| **DH-02** | 1 vínculo organizacional por COLABORADOR | **Confirmada** |
| **DH-03** | COLABORADOR só criado após vínculo mínimo completo | **Selecionada (Alternativa A)** |
| **DH-04** | Validade do estado somente-Federação | **Pendente** |
| **DEC-DB-028** | Proposta não aprovada | Vigente como proposta |
| **DEC-FA-003** | N vínculos + Contexto Ativo | Vigente até supersession |

### Nova regra de negócio (a formalizar)

> O domínio do e-mail corporativo identifica a **Singular** do usuário. O usuário **não** escolhe Singular livremente quando o domínio já a determina.

Seleção do usuário: **Área** (obrigatória) → **Equipe** (opcional).

---

## 3. Fluxo TO-BE

```text
1.  Usuário informa e-mail
2.  Zimbra valida identidade
3.  Sistema extrai domínio do e-mail
4.  Domínio → Singular (resolução automática)
5.  Federação → derivada da Singular (COD_FEDERACAO)
6.  Usuário seleciona Área (filtrada pela Singular)
7.  Usuário seleciona Equipe (opcional; filtrada pela Área)
8.  Vínculo mínimo completo definido
9.  COLABORADOR criado (Fed + Sing + Área [+ Equipe])
10. Sessão operacional estabelecida
11. Contexto Ativo estabelecido
```

---

## 4. Arquitetura AS-IS

### 4.1 Cadeia de autenticação atual

```text
POST /auth/login ou GET /auth/callback
    ↓
IdentityCredentialValidator (Zimbra)
    ↓
AuthenticationService.finalizeLogin()
    ↓
ColaboradorService.locateOrCreate()     ← cria COLABORADOR (só COD_FEDERACAO)
    ↓
SessionService.createSession(colaborador)
    ↓
AUTH_SESSAO (COD_COLABORADOR NOT NULL)
    ↓
JwtTokenService.issueToken(colaboradorId, ...)   ← sub = COD_COLABORADOR
    ↓
Cookies access_token + refresh_token
    ↓
Redirect frontend
```

### 4.2 Componentes acoplados a COLABORADOR

| Componente | Acoplamento |
|------------|-------------|
| `AUTH_SESSAO` | FK `COD_COLABORADOR` NOT NULL |
| `AuthSessaoEntity` | `@ManyToOne(optional = false)` → `ColaboradorEntity` |
| `SessionService.createSession` | Parâmetro `ColaboradorEntity` obrigatório |
| `SessionService.enforceSessionLimit` | Query por `colaboradorId` |
| JWT `sub` | `String.valueOf(colaboradorId)` — parse obrigatório como `long` |
| `JwtAuthenticatedPrincipal` | Campo `long colaboradorId` (primitivo, não nullable) |
| `JwtAuthenticationFilter` | Constrói principal com `colaboradorId` do JWT |
| `AuthenticationService.getAuthenticatedUser` | `findById(principal.colaboradorId())` |
| `AuthenticationService.refreshAccessToken` | `sessao.getColaborador()` |
| `/auth/me` | Retorna `colaboradorId`, `organizationalLinks` do COLABORADOR |
| `session.store.ts` | Hidrata via `/auth/me`; exige `organizationalLinks` |
| Endpoints protegidos | Exigem JWT válido com `sub` = colaboradorId |

### 4.3 Federação AS-IS

**FATO:** `locateOrCreate` usa `authProperties.defaultFederationId()` — **não** deriva domínio do e-mail.

**FATO:** `AuthProperties` expõe `defaultFederationId` e `sessionAdministratorEmails` — sem mapeamento domínio→Singular.

---

## 5. Dependência sessão → COLABORADOR

### 5.1 Por que impede a Alternativa A diretamente

A Alternativa A exige:

```text
Zimbra OK → identidade válida → SEM COLABORADOR → onboarding → COLABORADOR → sessão operacional
```

O AS-IS exige:

```text
Zimbra OK → COLABORADOR (locateOrCreate) → AUTH_SESSAO → JWT → qualquer request autenticado
```

**FATO:** Não existe caminho no código atual para emitir sessão/JWT **sem** `ColaboradorEntity` persistido.

**FATO:** Toda request autenticada (exceto endpoints públicos) passa por `JwtAuthenticationFilter`, que exige JWT com `sub` parseável como `colaboradorId`.

**INFERÊNCIA:** O onboarding (listar Áreas, submeter vínculo) **não pode** usar o mesmo mecanismo de sessão operacional sem redesign — endpoints como `GET /api/v1/areas?singularId=` exigem autenticação (`SecurityConfiguration`: `.anyRequest().authenticated()`), mas o usuário pré-COLABORADOR não pode obter JWT operacional pelo fluxo atual.

### 5.2 Diagrama da dependência

```text
                    ┌─────────────────┐
                    │  Zimbra (IdP)   │
                    └────────┬────────┘
                             │ email, zimbraId, name
                             ▼
              ┌──────────────────────────────┐
              │   finalizeLogin (AS-IS)      │
              │   locateOrCreate ────────────┼──► COLABORADOR (obrigatório hoje)
              └──────────────┬───────────────┘
                             │
              ┌──────────────▼───────────────┐
              │   AUTH_SESSAO                │
              │   COD_COLABORADOR NOT NULL   │◄── bloqueio estrutural
              └──────────────┬───────────────┘
                             │
              ┌──────────────▼───────────────┐
              │   JWT (sub=colaboradorId)    │
              └──────────────┬───────────────┘
                             │
              ┌──────────────▼───────────────┐
              │   /auth/me, APIs protegidas  │
              └──────────────────────────────┘
```

---

## 6. Estado pré-COLABORADOR

### 6.1 Respostas às perguntas de investigação

| # | Pergunta | Resposta | Evidência |
|---|----------|----------|-----------|
| 1 | Autenticar sem `COD_COLABORADOR`? | **Não** no modelo atual | `finalizeLogin` sempre chama `locateOrCreate` antes de sessão |
| 2 | Sessão intermediária? | **Não** | Nenhuma tabela/entidade de sessão de identidade sem colaborador |
| 3 | Token só-identidade? | **Não** | JWT sempre emitido com `sub` = colaboradorId |
| 4 | JWT exige `COD_COLABORADOR`? | **Sim, efetivamente** | `JwtTokenService.validateAndParse`: `Long.parseLong(subject)` obrigatório; `JwtAuthenticatedPrincipal.colaboradorId` é `long` |
| 5 | `AUTH_SESSAO` exige `COD_COLABORADOR`? | **Sim, estruturalmente** | DDL L360 `NOT NULL`; JPA `@JoinColumn(nullable = false)` |
| 6 | Outro identificador estável pré-colaborador? | **Parcial** — `email`, `zimbraId` do IdP; `sessionId` UUID só existe **após** criar sessão com colaborador | `IdentityValidationResult` |
| 7 | Diferencia autenticação de autorização? | **Parcial** | Auth = JWT válido; autorização operacional = vínculo com Área (BR-010) — mas ambos assumem colaborador persistido |
| 8 | Diferencia identidade de contexto? | **Conceitualmente sim** (REF-DB-CTX-01 removeu contexto de `AUTH_SESSAO`); **implementação não** — claims org vêm do COLABORADOR no JWT | `AuthSessaoEntity` comentário L24-25 |

### 6.2 Três estados conceituais vs AS-IS

| Estado | TO-BE (decisor) | AS-IS |
|--------|-----------------|-------|
| **A — Identidade autenticada** | Zimbra OK; sem COLABORADOR; sem vínculo | **Não existe** — `locateOrCreate` cria COLABORADOR imediatamente |
| **B — Onboarding organizacional** | Auth OK; Singular resolvida; seleção Área/Equipe | **Não existe** — sem sessão pré-operacional; sem wizard |
| **C — Sessão operacional** | COLABORADOR + vínculo + Contexto Ativo | **Parcial** — auth+sessão existem, mas vínculo pode estar incompleto (só Federação) |

**LACUNA ARQUITETURAL:** Estados A e B não têm representação em persistência, token nem SecurityContext.

---

## 7. `AUTH_SESSAO`

### 7.1 Campos (DDL + JPA)

Fonte: `database/ddl/003-create-tables.sql` L355-376; `AuthSessaoEntity.java`.

| Campo | Obrigatório? | Função | Depende de COLABORADOR? | Impacto Alternativa A |
|-------|:------------:|--------|:-----------------------:|----------------------|
| `COD_SESSAO` | Sim (PK) | Surrogate key | Não | Sem impacto direto |
| `ID_SESSAO` | Sim (UK) | Identificador público da sessão (`session_id` no JWT `sid`) | Não | Pode servir âncora de sessão pré-operacional **se** modelo permitir sessão sem colaborador |
| `COD_COLABORADOR` | **Sim** | Colaborador autenticado | **Sim** | **BLOQUEANTE** — impede sessão pré-COLABORADOR sem alteração futura |
| `HASH_REFRESH_TOKEN` | Sim (UK) | Continuidade de sessão (refresh) | Indiretamente — refresh carrega colaborador | Refresh atual falha sem colaborador na sessão |
| `DES_DISPOSITIVO` | Não | User-Agent | Não | Sem impacto |
| `FLG_REMEMBER_ME` | Sim | TTL estendido do refresh | Não | Sem impacto |
| `DAT_CRIACAO` | Sim | Auditoria | Não | Sem impacto |
| `DAT_EXPIRACAO` | Sim | Expiração refresh | Não | Política de TTL para onboarding a definir |
| `FLG_REVOGADA` | Sim | Revogação | Não | Sem impacto |
| `DAT_REVOGACAO` | Não | Revogação | Não | Sem impacto |

**FATO (REF-DB-CTX-01):** Colunas `COD_*_CTX` foram removidas de `AUTH_SESSAO` (migration V006). Contexto organizacional **não** é persistido nesta tabela.

### 7.2 Operações de sessão

| Operação | Dependência COLABORADOR | Evidência |
|----------|----------------------|-----------|
| Criação | **Obrigatória** | `SessionService.createSession(ColaboradorEntity, ...)` |
| Refresh | **Obrigatória** | `sessao.getColaborador()` → `issueAccessToken` |
| Limite simultâneo | Por `colaboradorId` | `findByColaborador_Id...` |
| Revogação | Via refresh ou sessionId | Independente do vínculo |
| Logout | Audita `colaborador.getId()` | Assume colaborador na sessão |

---

## 8. `AuthenticationService`

### 8.1 Sequência AS-IS detalhada

| # | Momento | Ação | Componente |
|---|---------|------|------------|
| 1 | Login/callback | Valida credenciais/token Zimbra | `IdentityCredentialValidator` |
| 2 | Pós-Zimbra | `locateOrCreate(identity)` | `ColaboradorService` |
| 3 | Pós-criação | Verifica `colaborador.isAtivo()` | `finalizeLogin` |
| 4 | Sessão | `createSession(colaborador, rememberMe, userAgent)` | `SessionService` |
| 5 | JWT | `issueAccessToken(colaborador, sessionId)` — inclui FKs atuais | `JwtTokenService` |
| 6 | Cookies | `access_token`, `refresh_token` | `AuthCookieService` |
| 7 | Auditoria | `logLoginSuccess(colaboradorId, sessionId)` | `AuthAuditService` |
| 8 | Contexto Ativo | **Não ocorre** no auth | FT-PRIMEIRO-ACESSO (não implementado) |
| 9 | Autorização | `loadPermissions()` → `[]` (vazio) | Stub |

### 8.2 Sequência TO-BE (Alternativa A)

```text
Zimbra
 ↓
Identidade autenticada (email, zimbraId, name)
 ↓
[Sessão pré-COLABORADOR]          ← não existe
 ↓
Resolver domínio → Singular
 ↓
Federação ← Singular.COD_FEDERACAO
 ↓
Usuário seleciona Área (+ Equipe?)
 ↓
Validar hierarquia (Singular→Área→Equipe)
 ↓
INSERT COLABORADOR (vínculo completo)
 ↓
Promover / criar sessão operacional
 ↓
JWT operacional (sub=colaboradorId, claims org)
 ↓
Contexto Ativo (= vínculo único, DH-02)
```

### 8.3 Gap principal

| Etapa TO-BE | Suporte AS-IS |
|-------------|---------------|
| Auth sem COLABORADOR | **Ausente** |
| Manter usuário autenticado durante onboarding | **Ausente** |
| APIs de listagem Área/Equipe para usuário em onboarding | **Bloqueadas** — exigem JWT com colaboradorId |
| Criação COLABORADOR pós-seleção | Existe via admin `ColaboradorApplicationService.create` — **não** self-service |
| `finalizeLogin` sem `locateOrCreate` | **Não implementado** |

---

## 9. `locateOrCreate`

### 9.1 AS-IS

| Aspecto | Comportamento |
|---------|---------------|
| **Invocação** | `AuthenticationService.finalizeLogin` L159 |
| **Busca** | `findByEmailIgnoreCase` OR `findByZimbraId` |
| **Criação** | email, nome, zimbraId, ativo=S, `federacaoId=defaultFederationId`, dataCadastro |
| **Não preenche** | singularId, areaId, equipeId, gestorId |
| **Atualização** | `syncIdentity`: só zimbraId, nome |
| **Transação** | `@Transactional` |
| **Idempotência** | Re-login encontra existente; UK email/zimbraId |
| **Concorrência** | Race teórica antes de UK |
| **Classificação** | **DECISÃO DOCUMENTADA** (DEC-DB-020) + **IMPLEMENTAÇÃO AS-IS** |

### 9.2 TO-BE (Alternativa A)

| Aspecto | Comportamento exigido |
|---------|----------------------|
| **Momento** | **Após** resolução Singular + seleção Área (+ Equipe) |
| **Campos mínimos** | `COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA` NOT NULL; `COD_EQUIPE` opcional |
| **Federação** | Derivada da Singular resolvida — **não** `defaultFederationId` fixo |
| **Singular** | Resolvida por domínio e-mail — usuário não escolhe |
| **Localização pré-criação** | Por email/zimbraId para evitar duplicata no INSERT final |
| **No login** | **Não** criar COLABORADOR — substituir ou bifurcar `finalizeLogin` |

### 9.3 Gap

| Item | Gap |
|------|-----|
| Remover/deslocar `locateOrCreate` do login | **Obrigatório** |
| Novo serviço de criação pós-onboarding | **Ausente** (só admin CRUD) |
| Sessão entre login e criação | **Ausente** |
| Resolução domínio→Singular | **Ausente** |
| `defaultFederationId` | **Substituir** por derivação via Singular |

---

## 10. Domínio do e-mail → Singular

### 10.1 Investigação AS-IS

| Local | Existe mapeamento domínio→Singular? | Evidência |
|-------|--------------------------------------|-----------|
| Tabela `SINGULAR` | **Não** — sem coluna de domínio | DDL L58-69; `SingularEntity` |
| Tabela dedicada de domínios | **Não** encontrada | Busca em `database/` |
| `CONTATO` | Canais institucionais — **não** modelado para domínio de e-mail de identidade | `03-physical-model.md` §CONTATO |
| `AuthProperties` | `defaultFederationId` — federação fixa por config | `AuthProperties.java` |
| `ColaboradorService` | Usa `defaultFederationId` na criação | L54 |
| Backend hardcode | **Não** encontrado para domínio→Singular | Grep sem match |
| Frontend hardcode | **Não** investigado em profundidade; sem wizard de onboarding | `session.store` TODOs |
| Testes | Emails `@unimedceara.com.br` em fixtures — **sem** assert de resolução de Singular | `ColaboradorAcceptanceIntegrationTest` |
| Documentação | BR-026: domínios corporativos Unimed Ceará — **sem** mapeamento a Singular | `09-business-rules.md` L80 |

### 10.2 Onde deve existir o SSOT?

**LACUNA — necessita decisão humana.** Opções arquiteturais conceituais (sem escolha):

| Opção | Prós | Contras |
|-------|------|---------|
| Coluna/tabela em `SINGULAR` (ex.: `DES_DOMINIO_EMAIL` ou `SINGULAR_DOMINIO`) | Coeso com entidade org; query direta | Requer DDL; múltiplos domínios por Singular |
| Tabela `DOMINIO_ORGANIZACIONAL` N:1 `SINGULAR` | Flexível para domínios alternativos | Nova entidade + governança |
| Configuração (`application.yaml`) | Rápida para poucos domínios | Não escalável; fora do SSOT de org |
| `specs/` + seed DML | Rastreável | Ainda precisa persistência |

**INFERÊNCIA:** SSOT natural = **camada Organização Corporativa** (`SINGULAR` ou entidade associada), pois a regra vincula identidade institucional à cooperativa.

### 10.3 Questões da regra domínio→Singular

| # | Questão | Classificação |
|---|---------|---------------|
| 1 | Comparação exata do domínio? | **INDEFINIDO** — necessita decisão humana |
| 2 | Normalização (lowercase, trim)? | **INDEFINIDO** — email já normalizado lowercase em `createColaborador` |
| 3 | Domínios alternativos por Singular? | **INDEFINIDO** — exemplos decisor usam domínios distintos por Singular |
| 4 | Uma Singular, múltiplos domínios? | **INDEFINIDO** — provável (ex.: `@cariri.com.br` e alias) |
| 5 | Um domínio, múltiplas Singulares? | **INDEFINIDO** — deve ser proibido para resolução determinística |
| 6 | Domínio nulo/inválido? | **DEFINIDO parcial** — Zimbra rejeita; extração pós-`@` é técnica |
| 7 | Domínio não cadastrado? | **INDEFINIDO** — bloquear login? onboarding? mensagem? |
| 8 | Domínio mapeia para >1 Singular? | **INDEFINIDO** — ambiguidade; deve falhar ou decidir |
| 9 | Singular inativa? | **DEFINIDO parcial** — `ColaboradorDomainService` rejeita Singular inativa em vínculos; mesma regra deve aplicar na resolução |
| 10 | Federação via Singular? | **DEFINIDO** — `SINGULAR.COD_FEDERACAO NOT NULL`; Federação derivável da Singular resolvida |

**CONTRADIÇÃO POTENCIAL:** Exemplos do decisor (`@unimedceara.com.br` → Unimed Ceará; `@unimedcariri.com.br` → Unimed Cariri) implicam **duas Singulares** na mesma Federação. BR-026 fala em “domínios corporativos da Unimed Ceará” sem distinguir Singular — **complemento normativo necessário**.

---

## 11. Singular → Área → Equipe

### 11.1 Singular

| Aspecto | TO-BE | AS-IS |
|---------|-------|-------|
| Origem | Resolvida por domínio e-mail | `defaultFederationId` no login; Singular null |
| Escolha usuário | **Proibida** quando domínio determina | N/A |

### 11.2 Área

| Aspecto | Evidência |
|---------|-----------|
| Filtro por Singular | `AreaRepository.findByFilters(singularId, ...)`; `GET /areas?singularId=` |
| Obrigatoriedade | TO-BE: obrigatória no vínculo mínimo; RN-PA-001; BR-009/010 |
| Validação backend | `ColaboradorDomainService.resolveOrganizationalLinks` — Área pertence à Singular |
| Endpoint listagem | `GET /api/v1/areas` — **requer autenticação**; não exige admin |
| Singular de outra coop | Backend rejeita: "Área não pertence à singular informada" |

### 11.3 Equipe

| Aspecto | Evidência |
|---------|-----------|
| Opcional | DDL `COD_EQUIPE` nullable; TO-BE decisor |
| Filtro | Por `COD_AREA` em `EquipeRepository` |
| Validação | Equipe deve pertencer à Área informada |
| Área sem equipes | **Permitido** — `teamId=null` |

### 11.4 Casos extremos

| Caso | Comportamento AS-IS | TO-BE |
|------|---------------------|-------|
| Singular sem Áreas | Listagem vazia | **LACUNA** — bloquear onboarding? |
| Área sem Equipes | Lista equipes vazia; `teamId=null` | Compatível |
| Usuário envia Área de outra Singular | Backend rejeita | Compatível se validação mantida |
| Área inativa | Rejeitada em `resolveOrganizationalLinks` | Compatível |

---

## 12. Autenticação × identidade × vínculo × autorização × contexto

| Estado | Autenticado | COLABORADOR | Vínculo | Autorização operacional | Contexto Ativo |
|--------|:-----------:|:-----------:|:-------:|:-----------------------:|:--------------:|
| Pós-Zimbra (TO-BE) | Sim | Não | Não | **Não** | Não |
| Onboarding (TO-BE) | Sim | Não | Em definição | **Não** | Não |
| Pós-vínculo / operacional | Sim | Sim | Completo | **Sim** (BR-010) | Sim |
| AS-IS pós-login | Sim | Sim (pode ser só Fed) | Incompleto possível | **Não** se sem Área (normativo); **não enforced** no código | Promovido automaticamente em `session.store` mesmo incompleto |

### Dependências atuais de autorização

| Mecanismo | Depende de |
|-----------|------------|
| JWT válido | `colaboradorId`, `sessionId`, `email`, `name` |
| Admin org (`OrganizationAuthorizationService`) | `colaboradorId` + lista `sessionAdministratorEmails` |
| Permissões | Stub vazio — futuro `PAPEL_ATRIBUICAO` |
| Guards frontend | `authStore.isAuthenticated` — não verifica vínculo completo |
| BR-010 | Vínculo com Área + Contexto Ativo — **não implementado** em guards |

**FATO:** O sistema **diferencia conceitualmente** autenticação (FT-AUTH) de contexto operacional (FT-PA), mas **implementação atual funde** identidade e colaborador no mesmo token desde o login.

---

## 13. FT-PRIMEIRO-ACESSO

### 13.1 Modelo atual vs TO-BE

| Aspecto | FT-PA atual | TO-BE Alternativa A |
|---------|-------------|---------------------|
| Pré-requisito | Vínculo **pré-provisionado** (FT-COLABORADOR) | Vínculo **criado** no onboarding |
| Fluxo | Carregar vínculos → auto/selecionar Contexto Ativo | Resolver Singular → selecionar Área/Equipe → **criar** COLABORADOR → Contexto Ativo |
| N=0 vínculos | `Blocked` | **Não aplicável** antes da criação — wizard ativo |
| N>1 vínculos | `SelectingContext` | **Incompatível** com DH-02 |
| PA-API-003 | Persiste Contexto Ativo; **não** atualiza FKs | Precisa **criar** colaborador + contexto |
| `LoadingContexts` | Lista vínculos existentes | Substituir por resolução domínio + listagem Áreas |

### 13.2 Classificação de impacto por artefato PA

| Parte | Destino |
|-------|---------|
| Estados `Authenticated`, `LoadingHome`, `Operational`, `Blocked` (pós-criação) | **Complementar** / reutilizar |
| `SelectingContext`, N>1, `contexts[]` | **Supersession necessária** (DH-02) |
| RN-PA-001 (vínculo válido) | **Manter** — alinhado ao vínculo mínimo |
| RN-PA-004 (0 vínculos → Blocked) | **Supersession parcial** — antes da criação é wizard, não Blocked |
| UC-PA-001..004 (seleção entre vínculos) | **Supersession** — substituir por wizard Área/Equipe |
| UC-PA-005 (persistência contexto) | **Complementar** — após criação COLABORADOR |
| PA-API-001 `GET /session/contexts` | **Supersession** com DH-02 |
| Novo contrato onboarding | **Nova decisão/contrato necessário** |

---

## 14. DEC-DB-020

### 14.1 Compatibilidade com Alternativa A

| Trecho DEC-DB-020 | Alternativa A | Resultado |
|-------------------|---------------|-----------|
| FKs org opcionais em `COLABORADOR` | Vínculo completo na criação | **Compatível** no DDL (nullable não impede INSERT completo) |
| Login `locateOrCreate` pode criar só com `COD_FEDERACAO`; FKs NULL até onboarding/admin | **Não** criar no login; criar com vínculo completo | **CONTRADIÇÃO** |

### 14.2 Tratamento do conflito

| Opção | Tipo |
|-------|------|
| Supersession parcial de DEC-DB-020 (item login `locateOrCreate`) | **SUPERSESSION NECESSÁRIA** |
| Complemento documental mantendo FKs nullable no DDL mas proibindo INSERT incompleto | **COMPLEMENTO** + nova decisão DH-03 formal |
| Manter DEC-DB-020 e Alternativa A | **Incompatível** sem supersession |

**FATO:** Alternativa A **não pode** ser implementada mantendo o trecho vigente sobre `locateOrCreate` no login sem alteração governança.

---

## 15. DEC-FA-002

| Regra | Compatibilidade Alternativa A |
|-------|------------------------------|
| Operacional exige Área | **Compatível** — COLABORADOR nasce com Área |
| Não admite operacional sem Área | **Compatível** |
| Login recupera vínculo(s) | **Reinterpretação necessária** — primeiro login **cria** vínculo; logins subsequentes recuperam |
| Contexto Ativo para navegação | **Compatível** após criação |
| Bloqueio sem vínculo válido | **Compatível** durante onboarding (pré-COLABORADOR) |

**Impacto:** **Complemento** — DEC-FA-002 permanece válida para operação; onboarding passa a ser **criação** do vínculo, não recuperação de pré-provisionado.

---

## 16. BR-010 / BR-012

### BR-010

| Aspecto | Alternativa A |
|---------|---------------|
| Sem Área → sem operação | Aplica **após** criação; durante onboarding usuário **não é operacional** |
| Bloqueio | Durante estados A/B (pré-COLABORADOR) — bloqueio operacional, auth pode existir |
| Momento | **Após** persistência COLABORADOR com Área |

### BR-012

| Aspecto | Alternativa A |
|---------|---------------|
| Contexto coerente Fed+Sing+Área (+Equipe) | **Compatível** — vínculo nasce completo |
| Validação hierárquica | Já existe em `ColaboradorDomainService` |

---

## 17. BR-041

Com **DH-02 confirmada**:

| Elemento BR-041 | Status |
|-----------------|--------|
| "N vínculos organizacionais" | **Incompatível** — supersession via DH-01 |
| "Sessão possui um Contexto Ativo" | **Compatível** |
| Formato `federationId`, `singularId`, `areaId` | **Compatível** |
| Seleção entre N vínculos | **Obsoleto** com 1:1 |

---

## 18. DEC-FA-003

| Elemento | Impacto Alternativa A |
|----------|----------------------|
| P1 — N vínculos | **SUPERSESSION NECESSÁRIA** (DH-02) |
| P2 — Contexto Ativo | **MANUTENÇÃO** |
| P3 — Navegação no Contexto Ativo | **MANUTENÇÃO** |
| P4 — RN-SESSION-003 seleção N>1 | **SUPERSESSION NECESSÁRIA** |
| P5 — REF-DB-CTX-01 | **MANUTENÇÃO** |
| P6 — Alternativa 1 vínculo rejeitada | **ATUALIZAÇÃO NECESSÁRIA** (inversão) |
| P7 — N áreas | **SUPERSESSION PARCIAL** |
| `contexts[]` | **SUPERSESSION NECESSÁRIA** |
| Contexto Ativo | **MANUTENÇÃO** — com 1:1 = único vínculo |
| `organizationalLinks` | **MANUTENÇÃO** — API já retorna objeto único |

Alternativa A **não resolve** DH-01 automaticamente, mas **alinha** o onboarding a 1 vínculo criado no primeiro acesso.

---

## 19. Impacto no banco

**FATO (decisor):** Nenhum colaborador cadastrado atualmente — **sem legado** para migração de dados.

### COLABORADOR

| Coluna | Nullable DDL | TO-BE persistido | Gap |
|--------|:------------:|:----------------:|-----|
| `COD_FEDERACAO` | NOT NULL | NOT NULL | Sem gap |
| `COD_SINGULAR` | NULL | **NOT NULL** (política) | Futuro `ALTER` + app validation |
| `COD_AREA` | NULL | **NOT NULL** (política) | Idem |
| `COD_EQUIPE` | NULL | NULL permitido | Sem gap |

### Outras estruturas

| Estrutura | Impacto Alternativa A |
|-----------|----------------------|
| `AUTH_SESSAO` | Possível `COD_COLABORADOR` nullable **ou** tabela de sessão de identidade — **decisão arquitetural pendente** |
| `SINGULAR` / nova tabela domínio | **Nova estrutura provável** para SSOT domínio→Singular |
| `ONBOARDING_SOLICITACAO` | Legado; exige `COD_COLABORADOR` — **não serve** para pré-colaborador (DEC-FA-001 obsoletizou fluxo) |
| Migrations | Necessárias **após** decisões — não nesta etapa |
| FKs / índices | Sem impacto imediato além de NOT NULL futuro |

---

## 20. Modelos arquiteturais candidatos

### Modelo 1 — Sessão de identidade separada

```text
IDENTIDADE (email, zimbraId)
    ↓
AUTH_SESSAO_IDENTIDADE (sem COD_COLABORADOR)
    ↓
ONBOARDING
    ↓
COLABORADOR criado
    ↓
AUTH_SESSAO operacional (ou promoção)
```

| Critério | Avaliação |
|----------|-----------|
| Segurança | **Favorável** — separação clara de privilégios |
| Complexidade | **Alta** — nova tabela, dois tipos de sessão |
| Compatibilidade atual | **Baixa** |
| Persistência | Nova entidade |
| Refresh/expiração | Política independente para onboarding |
| CSRF/cookies | Possível cookie distinto ou claim `typ=onboarding` |
| API | Novos endpoints onboarding autenticados por sessão identidade |
| Frontend | Fluxo login → onboarding → app |
| Banco | Nova tabela |
| Testes | Suite separada onboarding |

### Modelo 2 — JWT pré-operacional

```text
Zimbra → JWT onboarding (sub=email ou zimbraId, typ=onboarding)
    → seleção Área/Equipe
    → COLABORADOR → JWT operacional (sub=colaboradorId)
```

| Critério | Avaliação |
|----------|-----------|
| Segurança | **Média** — requer claims `typ`/`scope` e validação rigorosa |
| Complexidade | **Média-Alta** — dois perfis de JWT, rotação no complete |
| Compatibilidade atual | **Média** — reutiliza cookies/JWT infra |
| Persistência | Refresh pode usar tabela identidade ou JWT curto sem refresh |
| `JwtTokenService` | **Gap** — `sub` hoje é sempre `colaboradorId` long |
| `JwtAuthenticatedPrincipal` | **Gap** — `colaboradorId` é `long` não opcional |
| API | Filtro distingue onboarding vs operacional |
| Frontend | Troca de token após complete |
| Testes | Dois perfis de token |

### Modelo 3 — `AUTH_SESSAO` adaptada

```text
AUTH_SESSAO com COD_COLABORADOR NULL inicialmente
    → onboarding
    → UPDATE COD_COLABORADOR
```

| Critério | Avaliação |
|----------|-----------|
| Segurança | **Média** — mesma tabela, estados diferentes |
| Complexidade | **Média** — menos artefatos que Modelo 1 |
| Compatibilidade atual | **Média-Alta** — evolui entidade existente |
| Persistência | `ALTER` nullable em `COD_COLABORADOR`; JPA `optional=true` |
| Refresh | Deve funcionar antes e depois da promoção |
| JWT | Ainda precisa `sub` alternativo pré-colaborador **ou** JWT só após promoção |
| Queries `enforceSessionLimit` | **Gap** — hoje por `colaboradorId`; pré-colaborador por email? |
| API | `/auth/me` precisa modo pré-operacional |
| Testes | Estados intermediários na mesma tabela |

---

## 21. Comparação dos modelos

| Critério | Modelo 1 — Identidade separada | Modelo 2 — JWT onboarding | Modelo 3 — AUTH_SESSAO adaptada |
|----------|:--------------------------------:|:-------------------------:|:-------------------------------:|
| Segurança | Alta | Média | Média |
| Complexidade | Alta | Média-Alta | Média |
| Compatibilidade arquitetura atual | Baixa | Média | Média-Alta |
| Clareza conceitual | Alta | Média | Média |
| Impacto DDL | Nova tabela | Baixo-Médio | ALTER nullable |
| Impacto JWT/Principal | Médio | **Alto** | Médio-Alto |
| Impacto `/auth/me` | Novo contrato | Dois modos | Dois modos |
| Impacto frontend | Alto | Médio | Médio |
| Impacto testes | Alto | Médio | Médio |
| Risco de regressão auth | Baixo (isolado) | Médio | Médio |

**Nenhum modelo foi selecionado nesta análise.**

---

## 22. Decisões humanas restantes

| ID | Tema | Bloqueia |
|----|------|----------|
| **DH-04** | Somente-Federação: inválido como definitivo (implícito em A) vs política explícita | Formalização governança |
| **DH-01** | Supersession DEC-FA-003 / BR-041 | Specs PA/SESSION |
| **DA-NEW-01** | SSOT domínio e-mail → Singular (estrutura + regras §10.3) | Onboarding |
| **DA-NEW-02** | Modelo de credencial temporária de Primeiro Acesso (§20-21) | Implementação auth | **DH-PA-01 aprovada** (2026-08-15) — ver `docs/governance/03-open-decisions.md` |
| **DA-NEW-03** | Supersession parcial DEC-DB-020 (`locateOrCreate`) | Implementação login |
| **DA-NEW-04** | Contrato API onboarding (criar COLABORADOR self-service) | Backend/FE |
| **DA-NEW-05** | Comportamento domínio não cadastrado / Singular sem Área | UX e bloqueio |
| **DEC-DB-028** | NOT NULL Singular/Área — ainda proposta | DDL futuro |

---

## 23. Contradições e lacunas

### Contradições

| ID | Fontes | Descrição |
|----|--------|-----------|
| **CA-01** | Alternativa A vs DEC-DB-020 | Login não pode criar COLABORADOR parcial; DEC-DB-020 autoriza |
| **CA-02** | Alternativa A vs `finalizeLogin` AS-IS | Código sempre cria colaborador antes da sessão |
| **CA-03** | Alternativa A vs FT-PA | PA assume vínculo pré-provisionado; A cria no onboarding |
| **CA-04** | Domínio→Singular vs BR-026 | BR-026 fala em domínios institucionais sem mapear a Singular |
| **CA-05** | Endpoints onboarding vs Security | APIs de Área/Equipe exigem JWT com `colaboradorId` |

### Lacunas

| ID | Descrição |
|----|-----------|
| **LA-01** | Credencial temporária de Primeiro Acesso | **DH-PA-01 aprovada** (2026-08-15) |
| **LA-02** | SSOT domínio→Singular inexistente |
| **LA-03** | API self-service criação COLABORADOR inexistente |
| **LA-04** | Regras de erro (domínio desconhecido, 0 áreas) |
| **LA-05** | Promoção sessão onboarding → operacional não especificada |
| **LA-06** | Mecanismo físico Contexto Ativo (INC-PA-004) ainda aberto |

---

## 24. Conclusão técnica

### Respostas às 10 perguntas do escopo

| # | Pergunta | Resposta |
|---|----------|----------|
| 1 | Dependência sessão ↔ COLABORADOR? | **Estrutural e transversal:** `AUTH_SESSAO.COD_COLABORADOR NOT NULL`, JWT `sub=colaboradorId`, `JwtAuthenticatedPrincipal`, refresh, `/auth/me`, limite de sessões |
| 2 | Por que impede Alternativa A? | `finalizeLogin` **obriga** criar COLABORADOR antes de qualquer sessão; não há token/sessão para onboarding sem colaborador |
| 3 | Estado de auth antes do COLABORADOR? | **Identidade Zimbra validada** (email, zimbraId, name) com **sessão pré-operacional** — **não implementado** |
| 4 | Alternativas arquiteturais? | **Modelo 1** (sessão identidade separada), **Modelo 2** (JWT onboarding), **Modelo 3** (`AUTH_SESSAO` com colaborador nullable) |
| 5 | Impacto de cada uma? | Ver §20-21 — todos exigem mudança em auth/JWT/principal; Modelo 1 mais isolado; Modelo 3 mais próximo do AS-IS |
| 6 | Onde formalizar domínio→Singular? | **Camada Organização** (persistência em/associada a `SINGULAR`) + decisão normativa — **não existe hoje** |
| 7 | Documentos a atualizar (futuro)? | DEC-DB-020 (supersession login), FT-PA, FT-AUTH (`decisions.md` DA-AUTH-011), FT-COLABORADOR (onboarding), BR-026 ou nova BR, specs session, modelo físico, DEC-FA-003/BR-041 (DH-01) |
| 8 | Decisões humanas restantes? | §22 — DH-04, DH-01, modelo sessão pré-colaborador, SSOT domínio, API onboarding, supersession DEC-DB-020 |
| 9 | Contradições que permanecem? | §23 — DEC-DB-020, FT-PA, security vs onboarding APIs |
| 10 | O que só após formalização? | Remoção/deslocação `locateOrCreate`, DDL domínio→Singular, sessão pré-colaborador, contratos onboarding, redesign FT-PA, guards operacionais, NOT NULL opcional (DEC-DB-028) |

### Síntese

A Alternativa A é **conceitualmente alinhada** a DH-02, BR-009/010/012, DEC-FA-002 e ao fluxo TO-BE do decisor (domínio→Singular, seleção Área/Equipe, criação tardia do COLABORADOR).

É **incompatível** com o pipeline atual `finalizeLogin → locateOrCreate → AUTH_SESSAO → JWT` e com trechos vigentes de **DEC-DB-020** e **FT-PRIMEIRO-ACESSO**.

O **bloqueio arquitetural central** (na data desta análise) era a ausência de credencial autenticada pré-COLABORADOR para o onboarding. **DH-PA-01** (2026-08-15) aprovou credencial temporária de Primeiro Acesso sem `AUTH_SESSAO` operacional; implementação técnica permanece pendente.

A regra **domínio → Singular** exige **nova persistência e decisão de SSOT** — não há suporte no schema, JPA ou código atuais.

**Nenhuma arquitetura final foi definida nesta análise.**

---

## Referências

| Artefato | Caminho |
|----------|---------|
| Análise de impacto | `construction/review/vinculo-organizacional-decision-impact-analysis.md` |
| Reconciliação de fluxo | `construction/review/vinculo-organizacional-flow-reconciliation.md` |
| Auth / sessão | `AuthenticationService.java`, `SessionService.java`, `AuthSessaoEntity.java`, `JwtTokenService.java` |
| DDL | `database/ddl/003-create-tables.sql`, `004-create-constraints.sql` |
| Domínio org | `ColaboradorDomainService.java`, `SingularEntity.java`, `AreaController.java` |
| FT-PA | `specs/features/primeiro-acesso/specification.md` |
| DEC-DB-020 | `database/model/05-decisions-and-risks.md` |

---

*Análise arquitetural — implementação, banco e governança não alterados.*
