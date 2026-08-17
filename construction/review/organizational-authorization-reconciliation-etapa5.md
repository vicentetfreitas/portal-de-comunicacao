# Reconciliação — Modelo Organizacional, Cargos e Autorização — Etapa 5

| Campo | Valor |
|-------|-------|
| Artefato | organizational-authorization-reconciliation-etapa5.md |
| Camada | Construction / Review |
| Versão | 1.0 |
| Data | 2026-08-14 |
| Categoria documental | Evidence |
| Status | **ETAPA 5 — CONCLUÍDA** (reconciliação estática) |
| Sucessor | [`organizational-authorization-formalization-etapa6.md`](organizational-authorization-formalization-etapa6.md) — AS-IS vs TO-BE |

---

## 1. Objetivo

Realizar reconciliação técnica entre as camadas que definem e implementam:

1. **Modelo organizacional** — hierarquia Federação → Singular → Área → Equipe → Colaborador e vínculos em `COLABORADOR`.
2. **Cargos** — atributo descritivo vs entidade de negócio; gestor/líder como relacionamento.
3. **Autorização** — papéis (`PAPEL` / `PAPEL_ATRIBUICAO`), escopo organizacional e implementação incremental no backend/frontend.

Eliminar ambiguidades entre domínio, specs, DDL, JPA e código operacional, classificando divergências sem alterar produção.

**Não incluído:** implementação de features, correção de código, DDL destrutivo, alteração Oracle.

---

## 2. Escopo analisado

| Camada | Artefatos |
|--------|-----------|
| Domínio | `docs/domain/` (01-vision, 02-glossary, 03-ubiquitous-language, 05-bounded-contexts, 08-aggregates, 09-business-rules, 10-open-questions) |
| Specs | `specs/features/{area,equipe,singular,colaborador,authentication,session,primeiro-acesso}/` |
| Specs domínio | `specs/domain/05-permission-model.md` |
| Banco | `database/model/`, `database/ddl/`, `database/migrations/` (V007) |
| Backend | `organization/`, `accesscontrol/` — 6 entidades JPA + serviços de authz |
| Frontend | `session.store`, guards, rotas admin, E2E mocks |
| Construction | reconciliation reports FT-AREA, FT-COLABORADOR, FT-SINGULAR, FT-EQUIPE |
| Etapas anteriores | `state-reconciliation-etapa2.md`, `ssot-reconciliation-etapa3.md`, `oracle-ddl-jpa-reconciliation-etapa4.md` |

---

## 3. SSOT e precedência (aplicada)

Hierarquia conforme `specs/foundation/minimal-ssot.md` e Etapa 3:

```text
specs/features/*/specification.md, api.md  → implementação Feature
docs/domain/09-business-rules.md            → BR transversais
database/ddl/ + database/model/03-physical-model.md → schema físico
docs/domain/01-vision.md                    → atores e papéis de negócio (conceitual)
construction/*/review/reconciliation-report.md → evidência por Feature
```

**Separação canônica (DEC-DB-020):**

| Conceito | SSOT persistência | SSOT autorização |
|----------|-------------------|------------------|
| Onde o colaborador está cadastrado / filtra admin | `COLABORADOR.COD_*` (FKs organizacionais) | — |
| O que o colaborador pode fazer | — | `PAPEL` + `PAPEL_ATRIBUICAO` (+ futuras `PERMISSAO_*`) |
| Quem está logado | `AUTH_SESSAO` + identidade em `COLABORADOR` | — |
| Contexto operacional da sessão | Contrato FT-SESSION (`/auth/me`) — **não** colunas em `AUTH_SESSAO` (REF-DB-CTX-01) | — |

---

## 4. Modelo organizacional — estado reconciliado

### 4.1 Hierarquia oficial

Fonte: DEC-DB-021, FT-SESSION, `02-logical-model.md`.

```text
Federação (FEDERACAO) — raiz única; COD_UNIMED 979 (cadastro)
    ↓
Singular (SINGULAR)
    ↓
Área (AREA) — nível único (DEC-DB-022; sem COD_AREA_PAI)
    ↓
Equipe (EQUIPE)
    ↓
Colaborador (COLABORADOR)
```

| Verificação | Resultado |
|-------------|-----------|
| DDL baseline 23 tabelas | ✅ Alinhado ao modelo lógico |
| DEC-DB-022 removido de specs FT-AREA | ✅ v1.2.0+ sem hierarquia entre áreas |
| JPA organizacional (4 entidades) | ✅ `FederacaoEntity`, `SingularEntity`, `AreaEntity`, `EquipeEntity` |
| JPA colaborador (FT-COLABORADOR) | ✅ `ColaboradorEntity` com FKs `COD_FEDERACAO`, `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE`, `COD_GESTOR` |
| Gestor de área / líder de equipe | ✅ `AREA.COD_GESTOR`, `EQUIPE.COD_LIDER` → `COLABORADOR` (DEC-DB-015) |
| Gestor direto colaborador | ✅ `COLABORADOR.COD_GESTOR` auto-referência (DEC-DB-016) |
| Canais de comunicação | ✅ `CONTATO` XOR 5 proprietários (DEC-DB-013/014/016) — **sem** entidade JPA ainda |

### 4.2 Features organizacionais — spec ↔ implementação

| Feature | Registry | Reconciliation report | Parecer |
|---------|----------|----------------------|---------|
| FT-SINGULAR | closed | `FT-SINGULAR/review/` | Aprovado com ressalvas (authz incremental) |
| FT-AREA | closed | `FT-AREA/review/` | Aprovado com ressalvas |
| FT-EQUIPE | closed | `FT-EQUIPE/review/` | Aprovado com ressalvas |
| FT-COLABORADOR | execution (FE) | `FT-COLABORADOR/review/` | Aprovado Sprint 3 BE |
| FT-SESSION | closed | Etapa 3 | Coerente |
| FT-PRIMEIRO-ACESSO | execution | Etapa 3 PD-01 | Spec READY_FOR_REVIEW |

Padrão transversal nas ressalvas: **authz via lista de e-mails administradores** (OQ-020 pendente), **cross-BC** `organization` → `accesscontrol` para gestor e validação de colaborador.

### 4.3 Vínculo colaborador — AS-IS vs TO-BE

| Aspecto | AS-IS (implementado) | TO-BE (documentado) | Classificação |
|---------|----------------------|---------------------|---------------|
| Vínculos em `COLABORADOR` | Um conjunto de FKs (`singular`, `area`, `equipe`) | N vínculos possíveis (DEC-FA-003 / OQ-027) | **PENDING_DECISION** — migration futura |
| `/auth/me` organizationalLinks | Snapshot das FKs do registro | Lista de contextos disponíveis | **INTENTIONAL_GAP** |
| Contexto Ativo | Auto-resolve com um vínculo (RN-SESSION-002) | Seleção quando N > 1 (FT-PRIMEIRO-ACESSO) | **WIP** |
| BR-010 (área obrigatória para operar) | Documentada; login pode criar colaborador sem FKs | Onboarding resolve vínculo | **WIP** (FT-PRIMEIRO-ACESSO) |

### 4.4 Área em nível de Federação vs Singular

| Fonte | Regra para `AREA.COD_SINGULAR` |
|-------|-------------------------------|
| `03-physical-model.md` | Nullable — área pode pertencer à Federação quando `COD_SINGULAR` nulo |
| DEC-DB-021 | FK singular **ou** federação (conceitual) |
| FT-AREA RN-AREA-001 | Singular **obrigatória** e imutável |
| `AreaDomainService.validateActiveSingular` | Exige `singularId` válido no cadastro |

**Divergência ORG-04:** modelo físico permite área sem singular; spec e implementação FT-AREA exigem singular. **Classificação:** `PENDING_DECISION` — alinhar spec ao físico (área federativa) ou restringir DDL com `NOT NULL` + CHECK.

---

## 5. Cargos — estado reconciliado

### 5.1 Terminologia

| Termo | Camada | Significado |
|-------|--------|-------------|
| **Cargo** (RH / descritivo) | Negócio informal | Função institucional do colaborador (ex.: analista, coordenador) |
| **Papel** | Domínio + DDL | Responsabilidade de **autorização** (`PAPEL`, `PAPEL_ATRIBUICAO`) |
| **Gestor / Líder** | Modelo organizacional | Relacionamento FK (`COD_GESTOR`, `COD_LIDER`) — **não** é cargo nem papel de autorização |

Glossário (`02-business-glossary.md`) não define "cargo" como termo canônico — reduz ambiguidade com "papel".

### 5.2 Decisões consolidadas

| Decisão | Conteúdo | Status reconciliação |
|---------|----------|----------------------|
| DEC-DB-016 | Entidade `CARGO` **rejeitada** (YAGNI) | ✅ Consistente em specs FT-COLABORADOR (sem cargo) |
| DEC-DB-016 (histórico) | `DES_CARGO` aprovado temporariamente em refinamento 2026-07-10 | ⚠️ **Supersedido** |
| V007 migration | Remove `DES_CARGO` e `NUM_CPF` — alinha a FT-COLABORADOR SSOT | ✅ Baseline DDL sem `DES_CARGO` |
| `05-decisions-and-risks.md` v4.7 | Corpo DEC-DB-016 **não** lista `DES_CARGO` | ✅ Alinhado ao baseline atual |

### 5.3 Divergências cargos

| ID | Descrição | Classificação |
|----|-----------|---------------|
| CAR-01 | Remoção `DES_CARGO` via V007 sem DEC-DB explícita de reversão | **DOCUMENTATION_GAP** — registrar DEC-DB-026 ou nota em DEC-DB-016 |
| CAR-02 | Expectativa de "cargo" em integração RH futura | **OUT_OF_SCOPE** — sem Feature |
| CAR-03 | Gestor/líder confundidos com "cargo administrativo" em camadas UI | **TERMINOLOGY_RISK** — usar ubiquitous language |

**Conclusão cargos:** Não existe entidade nem atributo persistido de cargo no modelo congelado. Cargo é **fora do escopo MVP**. Gestão institucional usa **papéis de autorização** e **relacionamentos de liderança**, não cargos RH.

---

## 6. Autorização — estado reconciliado

### 6.1 Modelo de domínio (TO-BE)

| Fonte | Papéis / atores documentados |
|-------|------------------------------|
| `01-vision.md` | Administrador global, de singular, de área, proprietário de equipe, colaborador, convidado |
| `09-business-rules.md` BR-003, BR-034 | Autorização por papel + escopo (global, singular, área, equipe) |
| `10-open-questions.md` OQ-020 | Matriz de permissões por papel administrativo — **aberta** |
| `specs/domain/05-permission-model.md` | Draft — Editor, Revisor, Publicador, Leitor (foco documental) |
| DDL seed `008-initial-data.sql` | `ADMINISTRADOR`, `GESTOR_DOCUMENTAL`, `EDITOR`, `COLABORADOR` |

### 6.2 Modelo físico (autorização)

| Tabela | Função | JPA |
|--------|--------|-----|
| `PAPEL` | Catálogo de papéis | ❌ Sem entidade |
| `PAPEL_ATRIBUICAO` | Papel + colaborador + escopo (`COD_FEDERACAO` … `COD_EQUIPE`) | ❌ Sem entidade |
| `PERMISSAO_PASTA` | Permissão documental | ❌ Sem entidade |
| `SOLICITACAO_PERMISSAO` | Fluxo de solicitação | ❌ Sem entidade |
| `REGISTRO_AUDITORIA` | Auditoria | ❌ Sem entidade |

Seed inicial popula 4 papéis; **não** há seed de `PAPEL_ATRIBUICAO` no baseline.

### 6.3 Implementação atual (AS-IS)

| Camada | Mecanismo | Observação |
|--------|-----------|------------|
| Backend CRUD org/colaborador | `OrganizationAuthorizationService` → `SessionAdministratorAuthorizationService` | Lista configurável `application.auth.session-administrator-emails` |
| Backend sessão admin | Mesmo serviço de e-mail whitelist | Não consulta `PAPEL_ATRIBUICAO` |
| `AuthenticationService.loadPermissions` | Retorna `Collections.emptyList()` | Stub — DA-AUTH-002 parcial |
| `AuthenticatedUserResponse` | `permissions` apenas; **sem** `roles` | Contrato AUTH-API-003 incompleto vs frontend |
| JWT | Identidade + FKs organizacionais no token | Sem claims de papel |
| Frontend rotas admin | `meta.roles: ["ADMIN"]` | Guard scaffold |
| `routerGuardConfig.enforceAuthorization` | **`false`** (default) | Rotas admin acessíveis sem checagem de papel |
| E2E | Mock injeta `roles: ["ADMIN"]` | Frontend-only; não reflete backend |

### 6.4 Mapa de nomenclatura de papéis (drift)

| Camada | Identificador usado |
|--------|---------------------|
| Domínio (`01-vision`) | `administrator`, `singular_administrator`, `area_administrator` |
| DDL `PAPEL.NOM_PAPEL` | `ADMINISTRADOR`, `GESTOR_DOCUMENTAL`, `EDITOR`, `COLABORADOR` |
| Frontend rotas | `ADMIN` |
| E2E mock | `ADMIN` |
| Backend operacional | E-mail em whitelist (sem código de papel) |

**Divergência AUTH-01:** três léxicos paralelos sem tabela de equivalência formal. **Classificação:** `PENDING_DECISION` (PD-AUTHZ-01).

### 6.5 Princípios preservados

| Princípio | Status |
|-----------|--------|
| DA-AUTH-002 — autenticação ≠ autorização | ✅ Zimbra autentica; Portal autoriza (parcial) |
| DEC-DB-020 — FKs org ≠ `PAPEL_ATRIBUICAO` | ✅ Documentado e não violado no código |
| BR-003 — papel + contexto organizacional | ⚠️ Contexto parcial em `/auth/me`; papel não materializado |
| OQ-020 — matriz administrativa | ❌ Aberta — bloqueia authz definitiva |

---

## 7. Tabela consolidada de divergências

| ID | Domínio | Descrição | Severidade | Classificação |
|----|---------|-----------|------------|---------------|
| ORG-01 | Organizacional | Multi-vínculo TO-BE vs FK única AS-IS | Alta | PENDING_DECISION |
| ORG-02 | Organizacional | Cross-BC organization ↔ accesscontrol | Média | ACCEPTED_LEGACY |
| ORG-03 | Organizacional | `CONTATO`/`ENDERECO` sem JPA | Baixa | INTENTIONAL_GAP (Features futuras) |
| ORG-04 | Organizacional | Área federativa (DDL nullable) vs RN-AREA-001 singular obrigatória | Média | PENDING_DECISION |
| CAR-01 | Cargos | V007 remove `DES_CARGO` sem DEC explícita | Baixa | DOCUMENTATION_GAP |
| AUTH-01 | Autorização | Léxico de papéis divergente (domínio / DDL / FE / BE) | Alta | PENDING_DECISION |
| AUTH-02 | Autorização | `PAPEL_ATRIBUICAO` não consumida pelo backend | Alta | IMPLEMENTATION_GAP (planejado) |
| AUTH-03 | Autorização | `/auth/me` sem `roles`; `permissions` vazio | Alta | IMPLEMENTATION_GAP |
| AUTH-04 | Autorização | FE `enforceAuthorization: false` com rotas `roles: ["ADMIN"]` | Média | INTENTIONAL_GAP (pré-OQ-020) |
| AUTH-05 | Autorização | OQ-020 aberta — sem matriz BR-034 | Alta | PENDING_DECISION |
| AUTH-06 | Autorização | `05-permission-model.md` Draft vs DDL seed | Média | DOCUMENTATION_GAP |

---

## 8. Decisões pendentes recomendadas

| ID | Questão | Opções | Bloqueia |
|----|---------|--------|----------|
| PD-AUTHZ-01 | Léxico canônico de papéis na API/JWT | (a) Adotar `NOM_PAPEL` DDL; (b) Mapear vision roles → PAPEL; (c) Capabilities separadas | FE guard + BE authz |
| PD-AUTHZ-02 | Estratégia incremental até OQ-020 | Manter e-mail whitelist + evoluir a `PAPEL_ATRIBUICAO` | Features admin |
| PD-ORG-01 | Área sem singular (nível federação) | Alinhar RN-AREA-001 ou restringir DDL | FT-AREA |
| PD-ORG-02 | Modelo N vínculos | Nova tabela de vínculo vs evolução `COLABORADOR` | FT-PRIMEIRO-ACESSO, FT-SESSION |
| PD-CAR-01 | Registrar reversão `DES_CARGO` | Nota em DEC-DB-016 ou DEC-DB-026 | Governança apenas |

---

## 9. Coerências confirmadas (sem ação)

1. Hierarquia DEC-DB-021 / DEC-DB-022 alinhada entre DDL, modelo físico, specs FT-AREA v1.2+ e JPA.
2. Rejeição de entidade `CARGO` consistente entre DEC-DB-016, FT-COLABORADOR e baseline DDL.
3. Separação vínculo cadastral (`COLABORADOR.COD_*`) vs autorização (`PAPEL_ATRIBUICAO`) formalizada em DEC-DB-020 e não violada.
4. Features organizacionais backend fechadas com reconciliation reports convergentes (ressalva authz comum).
5. Etapa 4 — 6 entidades JPA alinhadas ao DDL para o subconjunto organizacional + auth sessão.

---

## 10. Critérios de aceite da etapa

| Critério | Status |
|----------|--------|
| Modelo organizacional reconciliado entre domínio, specs, DDL e JPA | ✅ |
| Cargos vs papéis vs gestor/líder distinguidos e documentados | ✅ |
| Autorização AS-IS vs TO-BE mapeada | ✅ |
| Divergências classificadas (sem resolução silenciosa) | ✅ |
| Decisões pendentes registradas | ✅ |
| Sem alteração de código ou banco | ✅ |
| Relatório produzido | ✅ |

---

## 11. Conclusão

### Resumo executivo

O **modelo organizacional** está **congelado e coerente** entre `database/`, specs das Features FT-SINGULAR/AREA/EQUIPE/COLABORADOR e as 5 entidades JPA de organização + colaborador. Lacunas principais: **multi-vínculo** (TO-BE documentado, AS-IS single-FK) e **área em nível federação** (DDL permite, spec FT-AREA rejeita).

**Cargos** não existem no modelo persistido MVP. `DES_CARGO` foi removido do baseline (V007); entidade `CARGO` permanece rejeitada. Gestor e líder são **relacionamentos**, não cargos.

**Autorização** permanece em **modo incremental**: whitelist de e-mails para operações administrativas; tabelas `PAPEL`/`PAPEL_ATRIBUICAO` existem no DDL com seed parcial mas **não são consumidas** pelo backend; frontend tem scaffold RBAC desabilitado. OQ-020 e matriz BR-034 são o bloqueio para authz definitiva.

### Status final

```text
ETAPA 5 — CONCLUÍDA (reconciliação estática)
```

### Próximos passos (fora desta etapa)

1. Encerrar ou escopar OQ-020 → produzir matriz de permissões referenciada por BR-034.
2. Decidir PD-AUTHZ-01 (léxico) antes de habilitar `enforceAuthorization` no frontend.
3. Implementar `loadPermissions` / `PAPEL_ATRIBUICAO` em Feature de autorização (post Etapa 2 roadmap).
4. Resolver PD-ORG-01 e PD-ORG-02 no âmbito FT-PRIMEIRO-ACESSO / evolução FT-SESSION.
5. Registrar PD-CAR-01 em `05-decisions-and-risks.md` (governança).

---

## Referências

- `specs/foundation/minimal-ssot.md`
- `database/model/05-decisions-and-risks.md` (DEC-DB-015 a 022, DEC-DB-020)
- `specs/features/session/specification.md` (DEC-ORG-001, RN-SESSION-*)
- `specs/features/authentication/decisions.md` (DA-AUTH-002)
- `docs/domain/10-open-questions.md` (OQ-020)
- `construction/review/oracle-ddl-jpa-reconciliation-etapa4.md`
- `construction/features/FT-AREA/review/reconciliation-report.md`
- `construction/features/FT-COLABORADOR/review/reconciliation-report.md`
