# CHECKPOINT — BASELINE DO PRIMEIRO ACESSO

| Campo | Valor |
|-------|-------|
| Artefato | `construction/review/checkpoint-baseline-primeiro-acesso.md` |
| Data | 2026-08-17 |
| Tipo | Auditoria de baseline (somente leitura) |
| Escopo | FT-PRIMEIRO-ACESSO + saneamento documental/estrutural do projeto |
| Restrição | Nenhuma alteração foi feita no repositório além deste relatório |

---

## 1. Resumo executivo

**Primeiro Acesso está conforme?** **Não.** As decisões humanas vigentes (DH-PA-01/02/03, DH-02/03/04, DH-CARGO-01) definem um fluxo TO-BE distinto do AS-IS. O backend ainda executa `locateOrCreate` no login, cria `COLABORADOR` incompleto, emite sessão operacional (`AUTH_SESSAO` + JWT) imediatamente. Frontend e APIs de PA não existem.

**Bloqueadores:** (1) implementação AS-IS viola DH-03 e DH-PA-01; (2) artefatos PA (`flows.md`, `state-machine.md`, `api.md`, etc.) descrevem modelo pré-DH-02 não reconciliado; (3) conflito normativo não formalizado entre DA-AUTH-011 e DH-03/DH-PA-01; (4) lacuna de persistência do Contexto Ativo (INC-PA-004).

**Decisões humanas necessárias:** formalizar supersession DA-AUTH-011; escolher mecanismo de persistência do Contexto Ativo; autorizar reconciliação dos artefatos PA para TO-BE (ou declarar escopo de implementação incremental).

**Banco consistente?** **Parcialmente.** DDL greenfield, baseline 2026-07-22 e JPA mapeadas estão alinhados (AS-IS). TO-BE (DEC-DB-028 NOT NULL em `COD_SINGULAR`/`COD_AREA`, tabela `CARGO`, mapeamento domínio→Singular) **não** está no DDL.

**Scripts consistentes?** **Sim para AS-IS homologado.** `000-install.sql` orquestra cadeia documentada. Lacuna: não existe script SSOT único para evolução brownfield além de `migrations/` (sem Flyway).

**Documentação com excesso relevante?** **Sim.** `construction/review/` (22 relatórios), framework v4.1 legado em `.cursor/` e `construction/`, specs PA em estado misto (APPROVED vs READY_FOR_REVIEW), supersessions parciais não propagadas em todos os consumidores.

**Camadas candidatas a simplificação?** **Sim.** Orchestrators construction, agentes arquivados mas ainda presentes, `feature-manifest`/`pkg-XX/status.md`, duplicação `construction/` vs `specs/`.

**Estado de baseline:** **BASELINE CONHECIDA, PENDENTE DE SANEAMENTO**

---

## 2. Fontes de verdade (SSOT)

| Fonte | Responsabilidade | Autoridade | Estado |
|-------|------------------|------------|--------|
| `specs/foundation/minimal-ssot.md` | Mapa de precedência operacional | **Normativa** (Etapa 2 aprovada) | Atual |
| `specs/features/<slug>/feature.yaml` | Identidade e status de spec da feature | **Normativa** | Atual |
| `specs/features/<slug>/specification.md` | Comportamento/contrato da feature | **Normativa** (`specs/` > `docs/` > código) | PA: APPROVED com ressalvas §11 |
| `specs/features/<slug>/api.md`, `flows.md`, etc. | Contratos derivados | **Normativa derivada** | PA: READY_FOR_REVIEW — modelo parcialmente superseded |
| `docs/domain/09-business-rules.md` | Regras de negócio transversais | **Normativa** | Atual (supersessions registradas) |
| `docs/governance/03-open-decisions.md` | Decisões humanas aprovadas | **Normativa** | Atual (DH-PA-*, DH-CARGO-01) |
| `database/GOVERNANCE.md` + `baseline/` + `ddl/` | Schema físico Oracle | **Normativa** (baseline > ddl > migrations) | AS-IS homologado |
| `database/model/05-decisions-and-risks.md` | DEC-DB-* e GAPs | **Normativa** (TO-BE parcial) | Atual com GAPs |
| `specs/features/authentication/decisions.md` | DA-AUTH-* | **Normativa feature** | **Desatualizada** vs DH-03/PA |
| `construction/registry.yaml` | Índice de features/paths | **Operacional indicativo** | Não é SSOT de status |
| `construction/features/*/construction-state.yaml` | Estado por workstream | **Operacional** | PA: `not_started` |
| `construction/review/*` | Evidência analítica | **Evidence** — não normativa | Histórica + operacional misto |
| `.cursor/rules/core/project-index.mdc` | Navegação agentes | **Operacional** | Atual (3 agentes ativos) |
| Código (`backend/`, `frontend/`) | Implementação | **Evidence** — não normativa | AS-IS divergente do TO-BE PA |

### Multiplicidade de fonte de verdade

| Tema | Fontes em disputa | Classificação |
|------|-------------------|---------------|
| Momento de criação do COLABORADOR no login | DA-AUTH-011 (`specs/`) vs DH-03/DH-PA-01 (`docs/governance/`) | **MULTIPLICIDADE** — conflito não formalizado |
| Fluxo de PA (N vínculos vs wizard TO-BE) | `specs/features/primeiro-acesso/flows.md` vs `specification.md` §11 + governança | **Documental** — specification.md prevalece com ressalva explícita |
| Estado de implementação PA | `feature.yaml` (spec APPROVED) vs `construction-state.yaml` (`not_started`) vs código (inexistente) | **Operacional** — não conflito normativo |
| Obrigatoriedade de CARGO na criação | DEC-DB-027 texto original vs DH-CARGO-01 | **Encerrado** — supersession registrada |

---

## 3. Inventário estrutural do projeto

| Estrutura | Finalidade | Consumidores | Fonte de verdade | Estado |
|-----------|------------|--------------|------------------|--------|
| `backend/` | API Java/Spring Boot | Frontend, CI | Código + `specs/features/*/api.md` | Operacional (FT-AUTH, org CRUD) |
| `frontend/` | SPA Vue/Quasar | Usuários | Código + specs frontend | Operacional (auth, org parcial) |
| `database/ddl/` | Schema greenfield Oracle | DBA, deploy | `database/GOVERNANCE.md` | Homologado AS-IS |
| `database/migrations/` | Evolução brownfield | DBA | `migrations/README.md` | Parcial (V003–V007) |
| `database/model/` | Modelo lógico/físico/decisões | Engenharia, specs | Complementar ao DDL | Parcialmente desatualizado (logical v1.5) |
| `docs/` | Visão, domínio, arquitetura, governança | Humanos, specs | Referenciado por specs | Ativo com supersessions |
| `specs/` | SSOT de features e foundation | Implementação, CI | **Primário** | Ativo — PA em transição |
| `construction/features/` | Estado construction legado | Agentes (transição) | `construction-state.yaml` | PA: não iniciado |
| `construction/frontend/features/` | Workstream FE legado | — | `frontend-tasks.md` (legado) | PA: planejado |
| `construction/review/` | Relatórios de reconciliação | Decisor humano | Evidence | 22 artefatos |
| `construction/registry.yaml` | Índice unificado features | Navegação | Indicativo | Atual |
| `.cursor/agents/` | Agentes Cursor | IDE | `project-index.mdc` (3 ativos) | Transição (arquivados coexistem) |
| `.cursor/rules/` | Regras alwaysApply/on-demand | Agentes | `minimal-ssot.md` | Simplificado Etapa 2 |
| `.cursor/orchestrator/` | Orquestração construction | Legado v4.1 | Archive candidato | Reduzido |
| `.cursor/archive/` | Cópias arquivadas | Nenhum operacional | Histórico | Candidato legado |
| `legacy/` | Código legado | — | — | **Inexistente** no repositório |
| `.github/workflows/` | CI | PR/build | Evidence principal | Ativo |
| `specs/foundation/` | DoR/DoD, paths, workflow | Todo desenvolvimento | **Normativa** Etapa 2 | Aprovado |

---

## 4. Estado das decisões humanas

| ID | Fonte | Status | Regra (resumo) | Escopo | Impacta PA? |
|----|-------|--------|----------------|--------|-------------|
| **DH-PA-01** | `docs/governance/03-open-decisions.md` | Aprovada 2026-08-15 | Credencial temporária PA; sem `AUTH_SESSAO` operacional; COLABORADOR após vínculo | PA + auth | **Sim** |
| **DH-PA-02** | idem | Aprovada 2026-08-15 | Domínio→Singular 1:1; domínio sem Singular bloqueia PA | PA | **Sim** |
| **DH-PA-03** | idem | Aprovada 2026-08-17 | CARGO não requisito no PA; não é passo do wizard | PA | **Sim** |
| **DH-CARGO-01** | idem | Aprovada 2026-08-17 | CARGO opcional em qualquer criação de COLABORADOR; supersede NOT NULL de DEC-DB-027 | Global colaborador | **Sim** |
| **DH-02** | idem | Aprovada 2026-08-14 | 1 vínculo por COLABORADOR (FKs escalares) | Vínculo | **Sim** |
| **DH-03** | idem | Aprovada 2026-08-14 | COLABORADOR só após vínculo completo; `locateOrCreate` no login não normativo | PA + auth | **Sim** |
| **DH-04** | idem | Aprovada 2026-08-14 | Mínimo: Federação+Singular+Área NOT NULL; Equipe opcional | Vínculo | **Sim** |
| **DEC-ORG-002** | idem | Aprovada 2026-08-14 | CARGO entidade domínio independente de PAPEL | Domínio | Sim (futuro) |
| **DEC-ORG-003** | idem | Aprovada 2026-08-14 | Domínio e-mail determina Singular; Área selecionada pelo usuário | PA | **Sim** |
| **DEC-DB-027** | `database/model/05-decisions-and-risks.md` | Aprovada; supersession parcial | Catálogo CARGO; CARGO fora do vínculo; NOT NULL na criação **superseded** | Dados | Sim |
| **DEC-DB-028** | idem | Aprovada 2026-08-14 | 1 vínculo; NOT NULL singular/área no TO-BE; sem tabela VINCULO | Dados | **Sim** |
| **DEC-FA-001** | `03-open-decisions.md` | Aprovada | Onboarding = resolução Contexto Ativo (não solicitação admin) | PA | **Sim** |
| **DEC-FA-002** | idem | Aprovada | Operacional exige Área; bloqueio sem vínculo | PA + sessão | **Sim** |
| **DEC-FA-003** | idem | Aprovada; **P1 superseded** | N vínculos → **superseded** por DH-02; Contexto Ativo único mantido | Sessão | Parcial |
| **DEC-FA-004** | idem | Aprovada | Home dinâmica via backend | PA | **Sim** |
| **DEC-ORG-001** | idem | Aprovada | Hierarquia Fed→Singular→Área→Equipe→Colaborador | Domínio | Sim |
| **DA-AUTH-011** | `specs/features/authentication/decisions.md` | Approved | Login: Zimbra → locate/create colaborador → sessão | FT-AUTH | **Conflita com DH-03** |

### Supersession / encerramentos

| Decisão original | Superseded por | Status |
|------------------|----------------|--------|
| DEC-FA-003 P1 (N vínculos cadastrais) | DH-02, DEC-DB-028 | Formalizado em governança |
| DEC-DB-027 itens NOT NULL criação | DH-CARGO-01 | Encerrado 2026-08-17 |
| `locateOrCreate` normativo | DH-03, DH-PA-01 | Aprovado — **não propagado** em DA-AUTH-011 nem código |
| OQ-001, OQ-026, OQ-027, OQ-028 | DEC-FA-001..004 | Encerradas |

### Pendentes (lacunas, não decisões abertas em governança)

| GAP | Tema | Decisão fechada? | Implementação |
|-----|------|------------------|---------------|
| GAP-028-03 | Credencial temporária | Sim (DH-PA-01) | **Pendente** |
| GAP-028-04 | Mapeamento domínio→Singular | Sim (DH-PA-02) | **Pendente** |
| GAP-028-01 | `locateOrCreate` AS-IS | Sim (DH-03) | **Pendente** |
| INC-PA-004 | Persistência Contexto Ativo | **Não** | **Lacuna** |

---

## 5. Estado real do Primeiro Acesso (AS-IS)

```text
Entrada (POST /auth/login ou GET /auth/callback)
  ↓
Autenticação Zimbra (ZimbraCredentialValidator / ZimbraIdentityProviderAdapter)
  ↓
Identificação (IdentityValidationResult: email, displayName, zimbraId)
  ↓
Verificação de Primeiro Acesso — AUSENTE (não há gate PA)
  ↓
ColaboradorService.locateOrCreate(identity)
  ├─ encontrado → syncIdentity
  └─ não encontrado → createColaborador (email, nome, zimbraId, federacaoId default, ativo=S)
       singularId/areaId/equipeId = NULL
  ↓
Verificação ativo (colaborador.isAtivo())
  ↓
SessionService.createSession() → AUTH_SESSAO persistida
  ↓
JwtTokenService.issueToken() + cookies access/refresh
  ↓
Redirect frontend (authProperties.frontendRedirectUrl)
  ↓
Frontend: GET /auth/me → organizationalLinks → activeContext auto-atribuído
  ↓
Estado operacional: usuário autenticado com sessão plena mesmo sem vínculo completo
```

### Evidências por etapa

| Etapa | Onde | Evidência |
|-------|------|-----------|
| Autenticação | `AuthenticationService.finalizeLogin()` | `backend/.../AuthenticationService.java` L154–174 |
| Identificação | `IdentityValidationResult` | Retorno Zimbra adapter |
| Localização COLABORADOR | `ColaboradorService.locateOrCreate()` | `ColaboradorService.java` L32–36 |
| Criação COLABORADOR | `createColaborador()` privado | `ColaboradorService.java` L48–56 |
| Vínculo | FKs em `ColaboradorEntity` | Apenas `federacaoId` no auto-create |
| Sessão | `SessionService.createSession()` | Antes de JWT, mesma transação login |
| API | `AuthController` | `/api/v1/auth/*` — sem `/primeiro-acesso` ou `/session/context*` |
| Frontend | `auth-store`, `session.store`, `auth.guard` | Sem rotas PA; TODOs OQ-027/028 |
| Transação | `@Transactional` em `AuthenticationService` | Login atômico locate+session |

**APIs participantes (AS-IS):** `POST/GET /auth/login`, `GET /auth/callback`, `GET /auth/me`, `POST /auth/refresh`, `POST /auth/logout`.

**Componentes frontend:** `pages/auth/`, `stores/auth-store.ts`, `stores/session.store.ts`, `router/guards/auth.guard.ts`.

**Persistência:** `COLABORADOR` (criação parcial), `AUTH_SESSAO` (sessão operacional imediata).

---

## 6. Regras do Primeiro Acesso — reconciliação normativa

| Regra | Fonte normativa | Implementação | Spec | Teste | Estado |
|-------|-----------------|---------------|------|-------|--------|
| Credencial temporária sem AUTH_SESSAO operacional | DH-PA-01 | JWT+AUTH_SESSAO imediatos | §11 reconhece gap | Ausente | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** |
| Identidade sem COLABORADOR durante PA | DH-03, DH-PA-01 | `locateOrCreate` sempre cria | §11 TO-BE | Ausente | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** |
| Domínio→Singular 1:1 | DH-PA-02, BR-044 | Sem código/resolução | BR-044 em domain | Ausente | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** |
| Domínio sem Singular bloqueia PA | DH-PA-02.2 | Sem gate | flows.md FE antigo | Ausente | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** |
| Usuário seleciona Área (não Singular) | DEC-ORG-003 | Admin CRUD only | UC-PA-* antigos | Ausente | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** |
| Equipe opcional | DH-04 | Nullable em entity | Alinhado conceitual | Parcial admin | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** (PA) |
| COLABORADOR após vínculo completo | DH-03 | Criado no login sem vínculo | §11 TO-BE | Ausente | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** |
| CARGO não obrigatório no PA | DH-PA-03, DH-CARGO-01, BR-045 | Sem CARGO (alinhado por ausência) | §11 alinhado | N/A | **CONFORME** (ausência) |
| 1 vínculo por COLABORADOR | DH-02 | FKs escalares | flows N-vínculo desatualizado | N/A | **DIVERGÊNCIA DOCUMENTAL** |
| Bloqueio operacional sem Área | DEC-FA-002, BR-010 | Sem bloqueio pós-login | RN-PA-004 | Ausente | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** |
| Contexto Ativo não em AUTH_SESSAO | REF-DB-CTX-01 | `AuthSessaoEntity` sem COD_*_CTX | Alinhado | Implícito | **CONFORME** |
| Persistência Contexto Ativo | RN-PA-005 | Indefinida | INC-PA-004 | Ausente | **LACUNA** |
| Home dinâmica pós-contexto | DEC-FA-004 | `/app` placeholder | PA-API-004 proposed | Ausente | **DIVERGÊNCIA DE IMPLEMENTAÇÃO** |
| Auto-seleção N=1 vínculo | RN-PA-002 | `session.store` auto-assign links | Modelo antigo | Unit session | **DIVERGÊNCIA DOCUMENTAL** + impl parcial |
| Seleção N>1 vínculos | RN-PA-003 (superseded cadastral) | Não implementado | flows.md ainda descreve | Ausente | **DIVERGÊNCIA DOCUMENTAL** |
| `locateOrCreate` no login | DA-AUTH-011 | **Implementado** | FT-AUTH boundary | Ausente auto-create | **CONFLITO NORMATIVO** vs DH-03 |

---

## 7. Implementação × especificação

### Divergências críticas

1. **PA não implementado** — `construction-state.yaml`: `phase: not_started`, 0/5 pkgs.
2. **`locateOrCreate` no login** viola DH-03 e DH-PA-01; é o único fluxo de "primeiro acesso" existente.
3. **Sessão operacional imediata** para COLABORADOR com `singularId`/`areaId` null — viola DEC-FA-002 operacional.
4. **APIs PA propostas** (`/session/contexts`, `/session/context`, `/session/home`) — zero implementação backend/frontend.
5. **Artefatos spec PA** (`flows.md`, `state-machine.md`, `use-cases.md`, `api.md`) descrevem fluxo N-vínculos pré-provisionados; `specification.md` §11 declara que não representam TO-BE integralmente.
6. **FT-SESSION** (`specification.md` L31–37) ainda menciona N vínculos TO-BE não especificado; RN-SESSION-003 (seleção N>1) superseded cadastralmente por DH-02.

### Conformidades parciais

- Separação auth-store / session-store no frontend (fundação FT-SESSION fase 1).
- `organizationalLinks` em `/auth/me` compatível com modelo 1-vínculo AS-IS.
- REF-DB-CTX-01 respeitado em JPA e entity.

---

## 8. Testes

### Cobertura existente (relevante a PA)

| Área | Arquivo | O que cobre | Relação PA |
|------|---------|-------------|------------|
| Auth fluxo | `AuthAcceptanceIntegrationTest.java` | Login callback com colaborador **pré-semeado** | Não testa auto-create |
| Auth fluxo | `AuthFlowIntegrationTest.java` | login→me→refresh→logout | Sem gate vínculo |
| Session FE | `session.store.spec.ts` | Hydrate, auto `activeContext` | Modelo fase 1, não PA TO-BE |
| Auth guard | `auth.guard.spec.ts` | requiresAuth, hydrate | Sem bloqueio operacional |
| Colaborador admin | `ColaboradorAcceptanceIntegrationTest.java` | CRUD com vínculo completo | Fluxo admin, não PA |

### Regras PA sem teste

- Credencial temporária (DH-PA-01)
- Wizard domínio→Singular→Área (DH-PA-02, DH-03)
- Bloqueio sem vínculo válido (RN-PA-004)
- Criação COLABORADOR apenas após vínculo
- APIs PA-API-001..005
- Home dinâmica (DEC-FA-004)
- Domínio sem Singular (BR-044)

### Testes vs spec

| Observação | Classificação |
|------------|---------------|
| `session.store` auto-atribui context sem validar Área | **Acoplado à impl fase 1**, não contradiz spec PA TO-BE (não implementado) |
| Nenhum teste asserta `locateOrCreate` para identidade nova | **AUSÊNCIA DE TESTE** — não é falha de regra |
| Acceptance tests PA (AT-PA-001..010) | **Não implementados** no código |

---

## 9. Banco — reconciliação

### Estrutura AS-IS (homologada 2026-07-22)

| Elemento | Documentado | DDL | JPA | Estado |
|----------|-------------|-----|-----|--------|
| 23 tabelas | `04-entity-catalog.md` | `003-create-tables.sql` | 6 entidades | **CONFORME** |
| `COLABORADOR` | `03-physical-model.md` | Nullable `COD_SINGULAR`/`COD_AREA` | `ColaboradorEntity` | **CONFORME AS-IS** |
| `AUTH_SESSAO` | Sem contexto org | Sem COD_*_CTX (V006 brownfield) | `AuthSessaoEntity` | **CONFORME** |
| `CARGO` | DEC-DB-027 TO-BE | **Ausente** | **Ausente** | **GAP TO-BE** |
| `VINCULO_ORGANIZACIONAL` | DEC-DB-028: não necessário | Ausente | Ausente | **CONFORME** decisão |
| Mapeamento domínio→Singular | DH-PA-02, GAP-028-04 | **Sem tabela/coluna** | **Sem entidade** | **DIVERGÊNCIA TO-BE** |
| `COD_SINGULAR`/`COD_AREA` NOT NULL | DEC-DB-028 TO-BE | Nullable no DDL | Nullable JPA | **DIVERGÊNCIA TO-BE** (esperada pré-migration) |

### Comportamento

- `locateOrCreate` persiste COLABORADOR com apenas `COD_FEDERACAO` — **permitido pelo DDL AS-IS**, **proibido pelo TO-BE normativo** (DH-03/DEC-DB-028).
- Integridade referencial: FKs existem; nullability permite estado intermediário não desejado normativamente.

### Relatório JPA×DDL

`database/reports/etapa-04-jpa-ddl-audit.md` (2026-08-14): **0 divergências críticas** nas 6 entidades mapeadas vs DDL greenfield.

---

## 10. Scripts do banco

### Script SSOT oficial

**FATO:** `database/ddl/000-install.sql` é o orquestrador SSOT para greenfield, documentado em `database/GOVERNANCE.md`.

### Inventário

| Script | Objetos | Dependências | Ordem | Documentação | Estado |
|--------|---------|--------------|-------|--------------|--------|
| `001-create-users.sql` | Users/roles | SYS | 1 (DBA) | GOVERNANCE | OK |
| `000-install.sql` | Orquestra 002–009 + dml/001 | 001 | 2 | Inline + GOVERNANCE | OK |
| `002-create-sequences.sql` | 12 sequences | — | 3 | model/04 | OK |
| `003-create-tables.sql` | 23 tables | 002 | 4 | model/03 | OK |
| `004-create-constraints.sql` | PK/FK/UK/CHECK | 003 | 5 | model/03 | OK |
| `005-create-indexes.sql` | Índices | 004 | 6 | model/03 | OK |
| `006-create-comments.sql` | Comments | 005 | 7 | — | OK |
| `007-create-grants.sql` | Grants APP role | 006 | 8 | security/ | OK |
| `008-initial-data.sql` | Bootstrap técnico | 007 | 9 | — | OK |
| `dml/001-federacao.sql` | Federação seed | 008 | 10 | dml/README | OK |
| `009-configuracao-portal.sql` | Config portal | dml/001 | 11 | — | OK |
| `dml/002–004` | Singulares, áreas, equipes | 001 | Pós-install manual | dml/README | OK — não no 000-install |
| `dml/005-colaboradores.sql` | Placeholder vazio | — | — | README (FT-AUTH) | OK |
| `migrations/V003–V007` | Brownfield auth/colaborador | COLABORADOR existente | Sequencial | migrations/README | OK |
| `900-drop-all.sql` | Drop completo | — | Dev only | — | OK |
| `901-validation.sql` | Validação pós-install | install completo | Final | GOVERNANCE | OK |

### Lacunas de governança

| Lacuna | Detalhe |
|--------|---------|
| **LACUNA DE GOVERNANÇA** | Não existe script SSOT único para aplicar TO-BE (DEC-DB-028 NOT NULL, CARGO, domínio→Singular) — apenas GAPs documentados |
| **LACUNA DE GOVERNANÇA** | Brownfield: migrations sem runner automatizado (DEC-DB-019: sem Flyway) — ordem manual |

---

## 11. CARGO — verificação

**Decisão vigente (não reabrir):** CARGO é domínio com persistência própria; **não** é requisito para cadastro/criação de COLABORADOR. DEC-DB-027 NOT NULL na criação **superseded** por DH-CARGO-01.

| Camada | Alinhado? | Evidência |
|--------|-----------|-----------|
| `docs/domain/09-business-rules.md` BR-045 | Sim | CARGO não obrigatório |
| `docs/governance/03-open-decisions.md` | Sim | DH-CARGO-01 encerra reconciliação |
| `specs/features/primeiro-acesso/specification.md` §11 | Sim | CARGO omitido do wizard |
| `specs/features/colaborador/specification.md` | Verificar em implementação admin | Admin create exige vínculo, não cargo |
| Implementação backend | Sim (por ausência) | Sem entidade/API CARGO |
| DDL | N/A (CARGO não existe) | Consistente com "não obrigatório" |
| `database/model/05-decisions-and-risks.md` DEC-DB-027 | Parcial | Texto histórico NOT NULL ainda presente com nota supersession |
| `construction/review/primeiro-acesso-dh-pa-03-analysis.md` | **Obsoleto** | Ainda lista DH-PA-03 como pendente em trechos; status atualizado no cabeçalho |

**Referências históricas a CARGO obrigatório:** DEC-DB-027 §resumo em `03-open-decisions.md` (com complemento DH-CARGO-01); `primeiro-acesso-blocking-decisions-package.md` §3 ainda cita NOT NULL — **documentação histórica não atualizada em todos os pontos**.

---

## 12. Documentação — classificação

| Documento | Função | Consumidor | Autoridade | Sobreposição | Estado |
|-----------|--------|------------|------------|--------------|--------|
| `specs/foundation/minimal-ssot.md` | Precedência SSOT | Agentes, devs | Normativa | Com `07-documentation-architecture` | **ATUAL E NECESSÁRIO** |
| `docs/governance/03-open-decisions.md` | Decisões humanas | Todos | Normativa | Parcial com `database/model/05` | **ATUAL E NECESSÁRIO** |
| `docs/domain/09-business-rules.md` | BR transversais | Specs | Normativa | Referenciado por specs | **ATUAL E NECESSÁRIO** |
| `specs/features/primeiro-acesso/specification.md` | SSOT PA | Implementação | Normativa | Com flows/api desatualizados | **ATUAL — ressalva §11** |
| `specs/features/primeiro-acesso/flows.md` | Fluxos PA | Dev | Derivada | Duplica modelo antigo | **CANDIDATO A CONSOLIDAÇÃO** |
| `specs/features/primeiro-acesso/state-machine.md` | Estados PA | Dev | Derivada | Modelo N-vínculos | **REVISÃO HUMANA NECESSÁRIA** |
| `specs/features/primeiro-acesso/api.md` | Contratos PA | Dev | Derivada | PA-API proposed | **REVISÃO HUMANA NECESSÁRIA** |
| `specs/features/authentication/decisions.md` DA-AUTH-011 | Boundary FT-AUTH | Auth impl | Normativa feature | Conflita DH-03 | **CONTRADITÓRIO** |
| `specs/features/session/specification.md` | FT-SESSION | Session impl | Normativa | RN-SESSION-003 N>1 | **CANDIDATO A CONSOLIDAÇÃO** |
| `database/model/02-logical-model.md` | Modelo lógico | DBA | Complementar | Com physical/decisions | **OBSOLETO** (2026-07-10) |
| `database/model/05-decisions-and-risks.md` | DEC-DB-* | DBA, eng | Normativa dados | Com governança | **ATUAL E NECESSÁRIO** |
| `construction/11-14`, golden-template | Framework v4.1 | Legado | Archive | Com foundation workflow | **CANDIDATO A LEGADO** |
| `construction/registry.yaml` | Índice features | Navegação | Operacional | Com minimal-ssot | **ATUAL (transição)** |
| `docs/governance/09-framework-simplification-scope.md` | Escopo simplificação | Agentes | Normativa | — | **ATUAL E NECESSÁRIO** |

---

## 13. Construction / Review — classificação

| Relatório | Função atual | Estado |
|-----------|--------------|--------|
| `primeiro-acesso-blocking-decisions-package.md` | Evidência decisões DH-PA-* (encerradas) | **HISTÓRICO** — referência |
| `primeiro-acesso-dh-pa-02-analysis.md` | Análise DH-PA-02 | **HISTÓRICO** |
| `primeiro-acesso-dh-pa-03-analysis.md` | Análise DH-PA-03/CARGO | **HISTÓRICO** (trechos desatualizados) |
| `cargo-vinculo-reconciliation-pd-cargo-01-02-03.md` | Reconciliação CARGO | **HISTÓRICO** — superseded |
| `vinculo-organizacional-*` (9 arquivos) | Reconciliação DH-02/03/04 | **HISTÓRICO** — decisões fechadas |
| `organizational-authorization-*` (2 arquivos) | Autorização org Etapa 5–6 | **HISTÓRICO** |
| `ssot-reconciliation-etapa3.md` | Reconciliação SSOT Etapa 3 | **HISTÓRICO** |
| `state-reconciliation-etapa2.md` | Estado Etapa 2 | **HISTÓRICO** |
| `oracle-ddl-jpa-reconciliation-etapa4.md` | DDL×JPA | **Evidence válida** — baseline técnica |
| `oracle-runtime-validation-etapa4.md` | Validação runtime | **Evidence** |
| `construction-audit.md`, `reconciliation-report.md`, `completion-report.md` | Auditorias gerais | **HISTÓRICO** |
| `readiness-checklist.md` | Checklist | **SEM CONSUMIDOR** operacional claro |
| **`checkpoint-baseline-primeiro-acesso.md`** | **Este checkpoint** | **OPERACIONAL** |

**Referências cruzadas:** `specification.md` §11 e `03-open-decisions.md` referenciam pacote DH-PA. Nenhum relatório review é SSOT normativo.

**Consolidação possível (pós-decisão):** 9 arquivos `vinculo-organizacional-*` → um índice histórico; 3 arquivos `primeiro-acesso-dh-pa-*` → arquivo único de evidência.

---

## 14. Specs — classificação

| Spec | SSOT? | Alinhada impl? | Duplica docs/? | Consumida? |
|------|-------|----------------|----------------|------------|
| `authentication/` | Sim | Sim (FT-AUTH closed) | Parcial arquitetura | **Sim** — golden ref |
| `session/` | Sim | Parcial fase 1 | `docs/frontend/` | **Sim** |
| `primeiro-acesso/` | Sim (identidade) | **Não** — não implementado | DEC-FA em governança | Planejada |
| `colaborador/` | Sim | Sim admin CRUD | — | **Sim** |
| `singular/`, `area/`, `equipe/`, `federacao/` | Sim | Parcial | — | **Sim** |
| `FEATURE_BASELINE.md` | Template/guia | — | — | Referência |
| `foundation/*` | Sim | N/A | Substitui construction workflow | **Sim** — Etapa 2 |

---

## 15. Framework / .cursor / agentes

| Componente | Consumidor | Uso demonstrável | Problema | Responsabilidade operacional |
|------------|------------|------------------|----------|------------------------------|
| `project-index.mdc` | Always apply | Sim | — | **MANTER** |
| `minimal-ssot.md` + `development-workflow.md` | Dev flow | Sim | — | **MANTER** |
| `backend-engineer.mdc` | Implementação BE | Sim | — | **MANTER** |
| `specification-engineer.mdc` | Specs | Sim | — | **MANTER** |
| `reviewer.mdc` | Review | Sim | — | **MANTER** |
| `construction-engineer.mdc` (ativo) | — | Não no index | Sobreposição com backend-engineer | **CANDIDATO A LEGADO** |
| `feature-implementer.mdc`, `platform-architect.mdc`, `auditor.mdc` | — | Arquivados parcialmente | Duplicação archive/ | **CONSOLIDAR/LEGAR** |
| `orchestrator/*.mdc` | Construction v4.1 | Substituído por tasks.md | Complexidade | **LEGAR** |
| `rules/workflows/feature-construction-workflow.mdc` | — | Marcado ARCHIVED | — | **LEGAR** |
| `construction/features/FT-PRIMEIRO-ACESSO/` | — | `not_started` | Duplica specs | **OPERACIONAL transição** |
| `.cursor/archive/` | Nenhum | Cópias | Redundância | **MANTER como archive** |

---

## 16. Camadas candidatas a simplificação

| Estrutura | Classificação | Justificativa |
|-----------|---------------|---------------|
| `specs/foundation/` + `development-workflow.md` | **MANTER** | SSOT fluxo Etapa 2 |
| `specs/features/` | **MANTER** | SSOT comportamento |
| `construction/review/` (pós-baseline) | **CONSOLIDAR** | 22 relatórios → índice + histórico |
| `construction/11-14`, orchestrators | **LEGAR** | Substituídos por foundation |
| `feature-manifest.yaml` por feature | **LEGAR** | `path-conventions.md` substitui |
| `pkg-XX/status.md` | **LEGAR** | CI/PR substitui |
| `.cursor/agents/` duplicados | **CONSOLIDAR** | 3 ativos no index |
| `database/model/02-logical-model.md` | **REVISÃO** | Desatualizado vs physical |
| Artefatos PA não reconciliados | **DECISÃO HUMANA** | Atualizar vs implementar incremental |
| `legacy/` | **N/A** | Diretório inexistente |

---

## 17. Conflitos (somente conflitos reais)

### CONFLITO-01 — Momento de criação do COLABORADOR no login

**Fonte A:** `specs/features/authentication/decisions.md` — DA-AUTH-011  
**Regra:** "O primeiro login [...] consiste em: autenticação Zimbra → **localização/criação do colaborador** → verificação de ativo → **criação da sessão**."

**Fonte B:** `docs/governance/03-open-decisions.md` — DH-03, DH-PA-01  
**Regra:** COLABORADOR **somente persistido após vínculo completo**; identidade pode existir sem COLABORADOR durante PA; credencial temporária **sem AUTH_SESSAO operacional**; `locateOrCreate` no login **deixa de ser normativo**.

**Evento afetado:** Todo login de identidade Zimbra sem COLABORADOR pré-existente com vínculo completo.

**Por que são incompatíveis:** DA-AUTH-011 exige criação de COLABORADOR e sessão no mesmo fluxo de login. DH-03/PA-01 proíbem sessão operacional e criação prematura de COLABORADOR.

**Decisões relacionadas:** DH-PA-01, DH-03, GAP-028-01, GAP-028-03.

**Decisão humana necessária:** **SIM** — formalizar supersession de DA-AUTH-011 (atualizar spec auth ou registrar supersession explícita em governança).

---

### CONFLITO-02 — RN-SESSION-003 (seleção N>1) vs DH-02 (1 vínculo cadastral)

**Fonte A:** `specs/features/session/specification.md` — RN-SESSION-003  
**Regra:** "Com **múltiplos** vínculos, o usuário escolhe o Contexto Ativo."

**Fonte B:** `docs/governance/03-open-decisions.md` — DH-02, supersession DEC-FA-003 P1  
**Regra:** 1 COLABORADOR = **exatamente 1 vínculo** cadastral (FKs escalares).

**Evento afetado:** Seleção de contexto entre N vínculos pré-existentes.

**Por que são incompatíveis:** Com 1 vínculo cadastral, N>1 seleção cadastral é impossível. RN-SESSION-003 pressupõe N vínculos no modelo de dados.

**Decisões relacionadas:** DEC-FA-003 (parcial superseded), DH-02, INC-PA-001.

**Decisão humana necessária:** **SIM** — atualizar FT-SESSION para refletir supersession (seleção N>1 **obsoleta** no eixo cadastral) ou redefinir se "múltiplos contextos" terá outra semântica futura.

**Nota:** `docs/domain/09-business-rules.md` BR-041 já registra supersession parcial — conflito persiste em spec FT-SESSION não atualizada.

---

## 18. Lacunas de decisão

| ID | Questão | Por que é lacuna |
|----|---------|------------------|
| **LACUNA-01** | Mecanismo físico de persistência do Contexto Ativo (INC-PA-004) | Apenas REF-DB-CTX-01 define o que **não** usar (`AUTH_SESSAO`); não há decisão sobre cookie, JWT claim, tabela dedicada ou cache |
| **LACUNA-02** | Fluxo posterior de atribuição de CARGO | DH-CARGO-01 diz que CARGO pode ser posterior; **nenhum fluxo** definido (admin? self-service?) |
| **LACUNA-03** | Escopo de reconciliação dos artefatos PA antes da implementação | `specification.md` §11 adia evolução de UC/API/estados — não define se implementação segue artefatos antigos ou TO-BE direto |
| **LACUNA-04** | PA-API-002: 409 vs 200+flag para contexto inválido | Ambiguidade em `api.md`; altera contrato |

**Não são lacunas:** implementação pendente de decisões já tomadas (GAP-028-03/04); ausência de testes; código incompleto.

---

## 19. Cards de decisão humana

### DEC-CHK-01 — Formalizar supersession de DA-AUTH-011

**Questão:** Como encerrar o conflito entre DA-AUTH-011 (locate/create + sessão no login) e DH-03/DH-PA-01?

**Fatos:**
- DA-AUTH-011 aprovado 2026-07-10, texto inalterado.
- DH-03 e DH-PA-01 aprovados 2026-08-14/15 com regras incompatíveis.
- Código implementa DA-AUTH-011.

**Evidências:**
- `specs/features/authentication/decisions.md` — DA-AUTH-011
- `docs/governance/03-open-decisions.md` — DH-03, DH-PA-01
- `AuthenticationService.finalizeLogin()` — `locateOrCreate` + `createSession`

**Decisões relacionadas:** DH-PA-01, DH-03, GAP-028-01.

**Por que não pode ser resolvido pelas decisões existentes:** Precedência `specs/` > `docs/` deixa ambiguidade sem registro formal de supersession em `specs/`.

**Alternativas:**
- **A** — Atualizar DA-AUTH-011: login FT-AUTH autentica identidade; criação COLABORADOR/sessão operacional delegada a PA quando vínculo incompleto.
- **B** — Manter DA-AUTH-011 para logins com COLABORADOR existente; PA com credencial temporária para identidade nova (dois subfluxos explícitos).
- **C** — Registrar em `03-open-decisions.md` supersession explícita com data e referência cruzada; atualizar DA-AUTH-011 em seguida.

**Consequências:**

| Alternativa | Consequência |
|-------------|--------------|
| A | Spec auth única; refactor login obrigatório |
| B | Mais complexo; dois caminhos no auth service |
| C | Mínimo para baseline normativa coerente |

**Escopo:** Resolve conflito normativo CONFLITO-01. Não define implementação técnica da credencial temporária.

**Estado:** DECISÃO HUMANA NECESSÁRIA

---

### DEC-CHK-02 — Persistência do Contexto Ativo

**Questão:** Onde e como persistir o Contexto Ativo após seleção (REF-DB-CTX-01 exclui AUTH_SESSAO)?

**Fatos:**
- INC-PA-004 aberto em `traceability.md`.
- RN-PA-005 proíbe AUTH_SESSAO.COD_*_CTX.
- Reentrada com contexto válido (RN-PA-007) exige persistência entre sessões.

**Evidências:**
- `specs/features/primeiro-acesso/traceability.md` — INC-PA-004
- `specs/features/session/specification.md` — RN-SESSION-004
- `AuthSessaoEntity` — sem campos de contexto

**Decisões relacionadas:** REF-DB-CTX-01, DEC-FA-003, DH-02.

**Por que não pode ser resolvido:** Nenhuma decisão escolhe mecanismo entre alternativas técnicas válidas.

**Alternativas:**
- **A** — Tabela dedicada `CONTEXTO_ATIVO` ou colunas em entidade de perfil.
- **B** — JWT claims + refresh (estado no token, não em AUTH_SESSAO).
- **C** — Cookie assinado separado do access token.
- **D** — Derivar sempre de FKs em COLABORADOR (1 vínculo) — sem persistência separada se DH-02 mantido.

**Consequências:**

| Alternativa | Consequência |
|-------------|--------------|
| A | Migration DDL; modelo explícito |
| B | Sem DDL; limites de tamanho/rotatividade token |
| C | Similar B com separação de concerns |
| D | Simplest se 1 vínculo; troca de contexto futura inviável |

**Escopo:** Persistência do Contexto Ativo. Fora: UI de seleção, Home dinâmica.

**Estado:** DECISÃO HUMANA NECESSÁRIA

---

### DEC-CHK-03 — Estratégia de reconciliação dos artefatos PA

**Questão:** Implementar PA a partir dos artefatos atuais (N-vínculos) ou exigir atualização spec TO-BE antes do código?

**Fatos:**
- `feature.yaml`: specification APPROVED.
- `flows.md`, `state-machine.md`, `api.md`: READY_FOR_REVIEW, modelo antigo.
- `specification.md` §11: artefatos não representam TO-BE integralmente.

**Evidências:**
- `specs/features/primeiro-acesso/specification.md` §11
- `construction/features/FT-PRIMEIRO-ACESSO/construction-state.yaml` — not_started

**Decisões relacionadas:** DH-02, DH-03, DEC-FA-001..004.

**Alternativas:**
- **A** — Atualizar todos artefatos PA (UC, flows, API, states) para TO-BE **antes** de qualquer código.
- **B** — Implementar TO-BE direto usando `specification.md` §11 + governança; artefatos derivados em paralelo.
- **C** — Fase incremental: bloqueio operacional (sem vínculo) primeiro; wizard completo depois.

**Escopo:** Ordem spec→código para PA. Fora: credencial temporária (delegada pós DEC-CHK-01).

**Estado:** DECISÃO HUMANA NECESSÁRIA

---

## 20. Correções determinísticas (sem nova decisão)

| Item | Fonte da regra | Correção determinística | Bloqueia baseline? |
|------|-----------------|-------------------------|-------------------|
| CD-01 | DH-03, DH-PA-01 | Remover `locateOrCreate` de `finalizeLogin`; introduzir gate PA | **Sim** |
| CD-02 | DH-PA-01 | Não emitir AUTH_SESSAO operacional antes conclusão PA | **Sim** |
| CD-03 | DEC-FA-002 | Bloquear navegação operacional quando `areaId` null | **Sim** |
| CD-04 | DA-AUTH-011 vs DH-03 | Atualizar texto DA-AUTH-011 com supersession | **Sim** (normativo) |
| CD-05 | BR-041 supersession | Atualizar `flows.md`, `state-machine.md`, `traceability.md` INC-PA-001 | Não (documental) |
| CD-06 | FT-SESSION RN-SESSION-003 | Marcar superseded ou reescrever para DH-02 | Não (documental) |
| CD-07 | `primeiro-acesso-blocking-decisions-package.md` §3 | Remover referência NOT NULL DEC-DB-027 sem nota DH-CARGO-01 | Não |
| CD-08 | `database/model/02-logical-model.md` | Atualizar nullability COLABORADOR per DEC-DB-028 TO-BE note | Não |
| CD-09 | Status artefatos PA | Alinhar READY_FOR_REVIEW → APPROVED ou NOT_APPROVED após reconciliação | Não |
| CD-10 | `session.store.ts` comentário OQ-027 | Atualizar para DH-02 supersession | Não |
| CD-11 | Testes auth | Adicionar teste identidade nova sem colaborador (quando CD-01 implementado) | Não (pós-impl) |

---

## 21. Bloqueadores do baseline

| ID | Problema | Classificação |
|----|----------|---------------|
| BL-01 | Código viola DH-03/DH-PA-01 (`locateOrCreate` + sessão imediata) | **BLOQUEADOR** |
| BL-02 | CONFLITO-01 não formalizado (DA-AUTH-011 vs DH-03) | **DECISÃO PENDENTE** |
| BL-03 | Artefatos PA derivados desalinhados do TO-BE aprovado | **DECISÃO PENDENTE** (DEC-CHK-03) |
| BL-04 | Persistência Contexto Ativo indefinida | **DECISÃO PENDENTE** (DEC-CHK-02) |
| BL-05 | GAP-028-03/04 — implementação de decisões fechadas | **NÃO BLOQUEADOR** para baseline conhecida; **BLOQUEADOR** para PA completo |
| BL-06 | DDL AS-IS permite COLABORADOR sem Área | **NÃO BLOQUEADOR** baseline; bloqueador para TO-BE migration |
| BL-07 | 22 relatórios review sem índice | **MELHORIA** |

---

## 22. Não bloqueadores

- Ausência de testes PA (esperado — feature não iniciada)
- CARGO não implementado (decisão: opcional na criação)
- `legacy/` inexistente
- Framework v4.1 legado ainda no repo
- `construction-state.yaml` desatualizado vs git
- Pendência DEC-002 observabilidade
- OQ-007 evento Colaborador Integrado
- Placeholder `/app` no frontend
- 17 tabelas Oracle sem JPA (escopo futuro)

---

## 23. Candidatos a limpeza (pós-commit baseline)

| Categoria | Itens | Ação sugerida |
|-----------|-------|---------------|
| Documentação | 9× `vinculo-organizacional-*` | Consolidar índice histórico |
| Documentação | `primeiro-acesso-dh-pa-03-analysis.md` trechos pendentes | Arquivar ou corrigir |
| Estrutura | `.cursor/agents/` inativos duplicados | Mover para archive |
| Estrutura | `orchestrator/`, `feature-construction-workflow.mdc` | Marcar legado |
| Construction | `pkg-XX/status.md` FT-PRIMEIRO-ACESSO | Ignorar / remover após transição |
| Modelo | `02-logical-model.md` | Atualizar ou marcar obsoleto |

---

## 24. Estado de baseline

# **BASELINE CONHECIDA, PENDENTE DE SANEAMENTO**

O estado do projeto é suficientemente compreendido e documentado. Existem violações de decisões aprovadas, conflitos normativos não formalizados e lacunas de decisão que impedem commit de baseline **como estado normativo coerente**, mas não impedem que o decisor humano planeje o saneamento conscientemente.

---

## DECISÕES QUE PRECISO TOMAR

1. **DEC-CHK-01** — Como formalizar supersession de DA-AUTH-011 por DH-03/DH-PA-01 (CONFLITO-01).
2. **DEC-CHK-02** — Mecanismo de persistência do Contexto Ativo (LACUNA-01 / INC-PA-004).
3. **DEC-CHK-03** — Atualizar artefatos PA para TO-BE antes do código, ou implementar direto da governança (LACUNA-03).
4. **CONFLITO-02** — Confirmar obsolescência de RN-SESSION-003 (seleção N>1 vínculos) após DH-02.

---

## PROBLEMAS QUE NÃO PRECISAM DA MINHA DECISÃO

1. Código `locateOrCreate` no login viola DH-03 — correção determinística após DEC-CHK-01.
2. Sessão operacional emitida antes de PA — correção determinística (CD-02).
3. Ausência de gate operacional sem Área no frontend/guards — correção determinística (CD-03).
4. DA-AUTH-011 texto desatualizado — correção documental (CD-04).
5. Artefatos PA `flows.md`/`state-machine.md` descrevem modelo antigo — correção documental (CD-05).
6. FT-SESSION RN-SESSION-003 desatualizada — correção documental (CD-06).
7. Referências históricas a CARGO NOT NULL sem nota DH-CARGO-01 — correção documental (CD-07).
8. JPA×DDL das 6 entidades — **conforme** AS-IS; sem ação.
9. Scripts greenfield `000-install.sql` — **conformes** documentação.

---

## O QUE PRECISA SER CORRIGIDO ANTES DO COMMIT DE BASELINE

1. Resolver **DEC-CHK-01** (conflito normativo CONFLITO-01) — registro formal.
2. Decidir **DEC-CHK-03** — alinhar spec PA ou declarar escopo de implementação.
3. Atualizar **DA-AUTH-011** ou equivalente supersession em `specs/features/authentication/decisions.md`.
4. Registrar decisão ou delegação explícita para **DEC-CHK-02** se PA incluir reentrada com contexto persistido no MVP.
5. Corrigir status inconsistente: `feature.yaml` APPROVED vs artefatos READY_FOR_REVIEW (pelo menos nota de transição).

**Nota:** Correções de **código** (CD-01..03) são necessárias antes de declarar PA implementado, mas podem ser commit de baseline **documental/normativo** separado do commit de implementação — conforme estratégia do decisor.

---

## O QUE PODE FICAR PARA DEPOIS DO COMMIT

1. Implementação completa FT-PRIMEIRO-ACESSO (wizard, APIs, credencial temporária).
2. Migrations DEC-DB-028 (NOT NULL) e DEC-DB-027 (tabela CARGO).
3. Mapeamento físico domínio→Singular (GAP-028-04).
4. Home dinâmica (DEC-FA-004).
5. Consolidação `construction/review/`.
6. Arquivamento framework v4.1 e agentes legados.
7. Atualização `02-logical-model.md`.
8. Testes AT-PA-001..010.
9. Fluxo de atribuição de CARGO pós-criação (LACUNA-02).
10. OQ-007 e DEC-002.

---

## O QUE PODE SER SIMPLIFICADO/LEGADO/REMOVIDO

| Item | Ação | Justificativa |
|------|------|---------------|
| `construction/11-14`, orchestrators | Legar | Substituídos por `specs/foundation/` |
| `feature-manifest.yaml` paths como SSOT | Legar | `path-conventions.md` |
| `pkg-XX/status.md` | Legar | CI/PR |
| Agentes `.cursor` não listados em project-index | Legar/archive | Sem consumidor |
| 9 relatórios `vinculo-organizacional-*` | Consolidar | Decisões fechadas |
| `flows.md`/`state-machine.md` PA (modelo antigo) | Substituir | Após DEC-CHK-03 |

---

## O QUE NÃO DEVE SER TOCADO

1. **DH-PA-01, DH-PA-02, DH-PA-03** — aprovadas; não reabrir.
2. **DH-CARGO-01** — CARGO opcional na criação; reconciliação DEC-DB-027 encerrada.
3. **DH-02, DH-03, DH-04** — modelo de vínculo e momento de persistência.
4. **DEC-ORG-002, DEC-ORG-003** — semântica CARGO e domínio→Singular.
5. **DEC-FA-001..004** — escopo PA (com supersession P1 de DEC-FA-003 já registrada).
6. **Baseline Oracle 2026-07-22** — AS-IS homologado; evoluir via migrations, não reescrever silenciosamente.
7. **FT-AUTH implementado** — funcional para colaboradores pré-cadastrados; evoluir, não quebrar sem plano.
8. **REF-DB-CTX-01** — contexto fora de AUTH_SESSAO.
9. **`specs/foundation/minimal-ssot.md`** — precedência Etapa 2.
10. **Golden references** — `specs/features/authentication/`, `construction/golden-template/FT-SINGULAR.md`.

---

*Fim do relatório. Nenhuma alteração foi executada no repositório além da criação deste arquivo.*
