# Baseline — Validação Pré-Commit

| Campo | Valor |
|-------|-------|
| Artefato | `construction/review/baseline-pre-commit-validation.md` |
| Data | 2026-08-17 |
| Tipo | Auditoria final somente leitura |
| Escopo | Primeiro Acesso, Sessão, Autenticação (`locateOrCreate`), Banco, Scripts |
| Restrição | Nenhuma alteração em código, DDL, specs ou governança |

---

## 1. Resultado executivo

# **BASELINE APROVÁVEL PARA COMMIT**

A documentação normativa no escopo auditado está reconciliada com as decisões humanas vigentes (DH-02, DH-03, DH-04, DH-PA-01/02/03, DH-CARGO-01, conclusão Contexto Ativo × DH-02). AS-IS (código e scripts) e TO-BE (specs e governança) estão explicitamente distinguidos. Pendências de implementação são conhecidas, documentadas e não exigem nova decisão humana antes do commit.

---

## 2. Evidências por área

### Primeiro Acesso

**Artefatos auditados:** `specification.md`, `flows.md`, `state-machine.md`, `api.md`, `traceability.md`, `use-cases.md`, `acceptance-tests.md`.

**Fato:** Specs PA em status APPROVED (reconciliado 2026-08-17). Fluxo TO-BE documentado: autenticação → verificação COLABORADOR → wizard (domínio → Singular → Área → Equipe opcional) → criação COLABORADOR (DH-03) → derivação Contexto Ativo → Home → Operacional. Artefatos do modelo N vínculos marcados SUPERSEDED.

**Verificação das 11 regras TO-BE (specs):**

| # | Regra | Evidência | Arquivo/seção | Status |
|---|-------|-----------|---------------|--------|
| 1 | Autenticação não cria COLABORADOR operacional | FT-AUTH hand-off; sem criação no login; DH-PA-01 credencial temporária | `specification.md` §1, §7, §11; `api.md` PA-API-006 requisitos | **ALINHADO** |
| 2 | Primeiro Acesso constrói o vínculo | Wizard `OnboardingWizard`; UC-PA-002 | `flows.md` Fluxo Principal; `state-machine.md` estados TO-BE | **ALINHADO** |
| 3 | Federação obrigatória | RN-PA-001; DH-04 | `specification.md` §2 RN-PA-001; §6 Contexto Ativo | **ALINHADO** |
| 4 | Singular obrigatória | RN-PA-001; DH-04; DH-PA-02 | `specification.md` §2; `flows.md` | **ALINHADO** |
| 5 | Área obrigatória | RN-PA-001; DH-04; BR-010 | `specification.md` §2 RN-PA-001 | **ALINHADO** |
| 6 | Equipe opcional | DH-04; Contexto Ativo `teamId` opcional | `specification.md` §6; `api.md` PA-API-005 | **ALINHADO** |
| 7 | COLABORADOR somente após vínculo completo | DH-03; estado `CreatingColaborador` após wizard | `specification.md` §1, §11; `state-machine.md` transições | **ALINHADO** |
| 8 | CARGO não é requisito | DH-CARGO-01; BR-045; PA-API-006 requisitos | `specification.md` §2 BR-045; `api.md` § PA-API-006 | **ALINHADO** |
| 9 | Não existe seleção entre múltiplos vínculos | RF/RN superseded; estados superseded | `specification.md` §2 RN-PA-003; `state-machine.md` § superseded | **ALINHADO** |
| 10 | Contexto Ativo derivado do vínculo | Projeção de FKs; sem persistência separada | `specification.md` §6; RN-PA-002, RN-PA-005 | **ALINHADO** |
| 11 | Home após estabelecimento operacional | `LoadingHome` → `Operational`; RF-PA-005/006 | `flows.md`; `state-machine.md`; PA-API-004 | **ALINHADO** |

**Inconsistências documentais menores (não normativas):**

| Item | Evidência | Impacto |
|------|-----------|---------|
| Ator Frontend ainda menciona "UI de seleção" | `specification.md` §3 linha 117 | Resíduo pré-DH-02; contradiz §2 RN-PA-002 vigente |
| Limite da Feature menciona "resolução/seleção" | `specification.md` §1 Limites linha 71 | Ambiguidade lexical; wizard é o fluxo vigente |
| RNF-PA-005 referencia métricas `SelectingContext` | `specification.md` §5 RNF-PA-005 | Estado superseded; métrica histórica não atualizada |

Nenhuma dessas inconsistências reabre decisão humana nem invalida o TO-BE consolidado em §11.

### Session

**Artefato:** `specs/features/session/specification.md`.

**Fato:** RN-SESSION-003 marcada SUPERSEDED (DH-02). Contexto Ativo definido como projeção derivada do único vínculo. `AUTH_SESSAO` não persiste `COD_*_CTX` (REF-DB-CTX-01). Primeiro Acesso e criação de COLABORADOR explicitamente fora do escopo (FT-PRIMEIRO-ACESSO).

**Status:** Alinhado ao TO-BE. Sem contradição normativa com DH-02.

### Authentication

**Artefatos:** `specs/features/authentication/decisions.md` (DA-AUTH-011); código AS-IS.

**Fato normativo (TO-BE):** DA-AUTH-011 com supersession parcial registrada (2026-08-17). `locateOrCreate` no login, criação automática de COLABORADOR e `AUTH_SESSAO` operacional imediata para identidade em PA estão **SUPERSEDED** por DH-03 e DH-PA-01. Regra vigente documentada em `decisions.md` § Regra vigente.

**Fato implementação (AS-IS):**

```154:174:backend/src/main/java/br/com/unimedceara/portalcomunicacao/accesscontrol/application/service/AuthenticationService.java
    private URI finalizeLogin(
            IdentityValidationResult identity,
            boolean rememberMe,
            HttpServletRequest request,
            HttpServletResponse response) {
        ColaboradorEntity colaborador = colaboradorService.locateOrCreate(identity);
        // ...
        SessionService.SessionCreationResult session = sessionService.createSession(colaborador, rememberMe, dispositivo);
```

`ColaboradorService.createColaborador()` persiste apenas `COD_FEDERACAO` (default); `COD_SINGULAR` e `COD_AREA` permanecem nulos.

**Classificação:** IMPLEMENTAÇÃO PENDENTE (GAP-028-01). Comportamento futuro determinado pelas decisões existentes; não há decisão humana pendente.

### Banco

**Artefatos:** `database/GOVERNANCE.md` §6, `database/model/03-physical-model.md`, `database/model/05-decisions-and-risks.md`, `database/model/04-entity-catalog.md`, DDL/DML.

| Elemento | AS-IS existe? | TO-BE decidido? | Implementado (TO-BE)? | Bloqueia baseline? |
|----------|---------------|-----------------|----------------------|-------------------|
| `COLABORADOR` | Sim (`003-create-tables.sql`) | Sim (DH-03, DH-04, DEC-DB-028) | Parcial — FKs org nullable no DDL | **Não** — GAP documentado |
| Vínculo organizacional (FKs escalares) | Sim — `COD_FEDERACAO` NOT NULL; `COD_SINGULAR`/`COD_AREA`/`COD_EQUIPE` nullable | Sim — NOT NULL Singular+Área (DH-04) | Não no DDL | **Não** — migration futura (GAP-028-02) |
| `CARGO` (tabela) | Não | Sim (DEC-DB-027 + DH-CARGO-01 opcional) | Não | **Não** |
| `COD_CARGO` | Não | Sim — opcional na criação (DH-CARGO-01) | Não | **Não** |
| `AUTH_SESSAO` | Sim — sem colunas `COD_*_CTX` | Sim (REF-DB-CTX-01) | Sim — alinhado | **Não** |
| `ONBOARDING_SOLICITACAO` | Sim (DDL) | Legado AS-IS; não usado no TO-BE PA | Documentado como reservada/legado | **Não** |
| Mapeamento domínio → Singular | Não (tabela/coluna) | Sim (DH-PA-02, DEC-ORG-003) | Não | **Não** — GAP-028-04 |

**Fato:** `database/GOVERNANCE.md` §6 e `03-physical-model.md` § COLABORADOR distinguem explicitamente AS-IS (scripts) de TO-BE (decisões). Documentação suficientemente clara sobre o que existe hoje versus o que será implementado depois.

### Scripts

**Artefatos:** `database/ddl/000-install.sql`, `003-create-tables.sql`, `database/dml/`, `database/dml/README.md`.

**Fato:**

- `000-install.sql` orquestra instalação do schema AS-IS (002–009, dml/001).
- `003-create-tables.sql` define `COLABORADOR` com `COD_SINGULAR`/`COD_AREA` nullable — consistente com baseline 2026-07-22.
- `005-colaboradores.sql` sem INSERT; README descreve passo reservado.
- Nenhum script afirma implementar NOT NULL de vínculo ou tabela `CARGO`.
- `GOVERNANCE.md` §6 declara scripts como AS-IS homologado; TO-BE em decisões sem alteração de DDL nesta etapa.

**Observação menor:** `database/dml/README.md` § Colaboradores menciona "login Zimbra (FT-AUTH)" como origem de colaboradores — descreve comportamento AS-IS atual, não TO-BE normativo. Não contradiz `GOVERNANCE.md` §6 quando interpretado no conjunto.

**Status scripts:** Coerentes com identificação AS-IS/baseline. TO-BE distinguido do AS-IS.

---

## 3. Conflitos encontrados

| ID | Evidência | Impacto | Tipo |
|----|-----------|---------|------|
| C-01 | `AuthenticationService.finalizeLogin()` chama `locateOrCreate` + `createSession` | Código AS-IS viola DH-03 e DH-PA-01; documentado em DA-AUTH-011 supersession e `baseline-saneamento.md` §6 | **IMPLEMENTAÇÃO PENDENTE** |
| C-02 | `ColaboradorService.createColaborador()` persiste COLABORADOR com FKs org incompletas | Mesmo que C-01; GAP-028-01 | **IMPLEMENTAÇÃO PENDENTE** |
| C-03 | Ausência de wizard PA, credencial temporária e endpoints PA no backend/frontend | PA não implementado; specs TO-BE prontas | **IMPLEMENTAÇÃO PENDENTE** |
| C-04 | PA-API-006 sem contrato HTTP fixado | Engenharia deve definir rotas/payloads na implementação; requisitos normativos listados em `api.md` | **IMPLEMENTAÇÃO PENDENTE** |
| C-05 | DDL: `COD_SINGULAR`/`COD_AREA` nullable vs DEC-DB-028 NOT NULL | Migration futura; GAP-028-02 documentado | **IMPLEMENTAÇÃO PENDENTE** |
| C-06 | Tabela `CARGO` e `COD_CARGO` ausentes no DDL | DEC-DB-027 TO-BE; DH-CARGO-01 torna opcional na criação | **IMPLEMENTAÇÃO PENDENTE** |
| C-07 | `specification.md` §3 ator Frontend: "UI de seleção" | Resíduo textual pré-DH-02; §2 e §6 normativos corretos | **DOCUMENTAÇÃO** |
| C-08 | RNF-PA-005 referencia `SelectingContext` (estado superseded) | Métrica de observabilidade desatualizada | **DOCUMENTAÇÃO** |

Nenhum conflito classificado como **DECISÃO HUMANA**.

---

## 4. Decisões humanas necessárias

# **NENHUMA**

Nenhuma pergunta cuja resposta não possa ser determinada pelas decisões já aprovadas foi identificada no escopo auditado.

---

## 5. Pendências de implementação

### Código

| Item | Decisão de referência | GAP |
|------|----------------------|-----|
| Remover/superseder `locateOrCreate` no login | DH-03, DH-PA-01, DA-AUTH-011 | GAP-028-01 |
| Credencial temporária PA (sem AUTH_SESSAO operacional) | DH-PA-01 | GAP-028-03 |
| Wizard PA + APIs onboarding | DH-PA-02, DH-PA-03 | PA-API-006 |
| API Home dinâmica | DEC-FA-004, PA-API-004 | — |
| Gate operacional no frontend (sem Área → sem operação) | DH-04, DEC-FA-002 | `session.store` auto-assign |

### Banco

| Item | Decisão | GAP |
|------|---------|-----|
| NOT NULL `COD_SINGULAR`, `COD_AREA` | DEC-DB-028, DH-04 | GAP-028-02 |
| Tabela `CARGO` + `COD_CARGO` opcional | DEC-DB-027, DH-CARGO-01 | — |
| Mapeamento domínio → Singular | DH-PA-02, DEC-ORG-003 | GAP-028-04 |

### Engenharia (contrato técnico, não decisão de negócio)

| Item | Escopo |
|------|--------|
| PA-API-006 | Forma física de endpoints/payloads de onboarding e mecanismo de credencial temporária (DH-PA-01 delega detalhe à engenharia) |

---

## 6. Pendências documentais

| ID | Artefato | Descrição | Severidade |
|----|----------|-----------|------------|
| PD-01 | `specification.md` §3 | Ator Frontend ainda cita "UI de seleção" | Baixa — resíduo pré-DH-02 |
| PD-02 | `specification.md` §5 RNF-PA-005 | Métricas referenciam estados superseded | Baixa |
| PD-03 | `specification.md` §1 Limites | "resolução/seleção" ambíguo | Baixa |
| PD-04 | `database/dml/README.md` § Colaboradores | Descreve AS-IS (login FT-AUTH) sem nota TO-BE explícita no parágrafo | Baixa — coberto por GOVERNANCE §6 |

Nenhuma pendência documental bloqueia o commit da baseline.

---

## 7. Verificação de AS-IS × TO-BE

### Login (`locateOrCreate` × `AUTH_SESSAO`)

| Aspecto | AS-IS (hoje) | TO-BE (decidido) | Classificação |
|---------|--------------|------------------|---------------|
| Pós-autenticação | `locateOrCreate` → cria/encontra COLABORADOR | Verificar PA; não criar COLABORADOR no login | **IMPLEMENTAÇÃO PENDENTE** |
| Sessão | `createSession` imediato → `AUTH_SESSAO` + JWT | Credencial temporária PA; sessão operacional após vínculo completo | **IMPLEMENTAÇÃO PENDENTE** |
| Vínculo no INSERT | Apenas `COD_FEDERACAO` | Federação + Singular + Área (+ Equipe opt.) | **IMPLEMENTAÇÃO PENDENTE** |
| Decisões violadas pelo AS-IS | — | DH-03, DH-PA-01 | Comportamento futuro **já definido** |
| Decisão humana necessária? | — | — | **Não** |

**Evidência AS-IS:** `AuthenticationService.finalizeLogin()` + `ColaboradorService.createColaborador()` (apenas `federacaoId` default).

**Evidência TO-BE:** `authentication/decisions.md` DA-AUTH-011 § Supersession parcial; `primeiro-acesso/specification.md` §11 Fluxo normativo.

### Contexto Ativo

**Conclusão aplicada:** Com DH-02, Contexto Ativo = projeção derivada do único vínculo; sem persistência cadastral independente.

**Verificação:** Nenhum artefato auditado no escopo contradiz normativamente essa conclusão. RN-SESSION-003, RF-PA-003/004/007 e estados `SelectingContext`/`ChangingContext` estão marcados SUPERSEDED.

# **SEM PENDÊNCIA DE DECISÃO**

---

## 8. Verificação de scripts do banco

| Critério | Resultado | Evidência |
|----------|-----------|-----------|
| Scripts identificados como AS-IS/baseline | **Atendido** | `GOVERNANCE.md` §6; `model/README.md` |
| Documentação não afirma que scripts atuais implementam TO-BE pendente | **Atendido** | §6 lista GAPs; `03-physical-model.md` distingue AS-IS/TO-BE em COLABORADOR |
| TO-BE distinguido do AS-IS | **Atendido** | Tabela AS-IS×TO-BE em GOVERNANCE §6 |
| `000-install.sql` coerente com DDL real | **Atendido** | Orquestra 002–009 + dml/001; sem alterações TO-BE embutidas |

---

## 9. PA-API-006 — investigação específica

### 1. O que a documentação afirma

- `api.md` tabela: PA-API-006 = onboarding, completar PA / criar COLABORADOR, status **"Pendente implementação — sem contrato fixado"**.
- § PA-API-006: pendência de implementação; TO-BE exige endpoints para wizard e criação de COLABORADOR; forma física (rotas, payloads, credencial temporária) **não decidida nesta etapa** — delegada à engenharia.
- Requisitos normativos listados: domínio→Singular, seleção Área/Equipe, criação com vínculo completo, sem CARGO, sem AUTH_SESSAO operacional antes da conclusão.

### 2. Requisito funcional que depende dele

| RF | Dependência |
|----|-------------|
| RF-PA-009 | Conduzir onboarding — `traceability.md` mapeia PA-API-006 (pend.) |
| RF-PA-011 | Wizard + criar COLABORADOR — `traceability.md` mapeia PA-API-006 (pend.) |

Comportamento funcional **já decidido** por DH-03, DH-PA-01/02/03, DH-04, DH-CARGO-01. PA-API-006 é o identificador do **contrato HTTP** a ser materializado na implementação.

### 3. Contrato de API normativamente definido?

**Não** — rotas, métodos e payloads não estão fixados. Requisitos normativos sim; contrato técnico não.

### 4. Apenas pendência de implementação?

**Sim.**

### 5. Ausência impede consistência da baseline?

**Não.** A baseline documenta TO-BE com contrato técnico explicitamente pendente. Não há divergência entre regras/decisões existentes que exija decisão humana antes do commit.

### Classificação PA-API-006

# **A — apenas pendência de implementação/engenharia**

Requisitos de negócio cobertos por DH-PA-01/02/03, DH-03, DH-04, DH-CARGO-01. Engenharia definirá forma física na fase de implementação.

---

## 10. Limpeza documental (identificação apenas — sem executar)

| Artefato | Motivo | Ação futura sugerida |
|----------|--------|---------------------|
| `construction/features/FT-PRIMEIRO-ACESSO/pkg-*/status.md` | Não é SSOT (`minimal-ssot.md`); estado manual | Tratar como LEGACY pós-implementação |
| `construction/registry.yaml` status | Índice indicativo, não normativo | Manter em transição |
| UCs/ATs superseded com texto histórico completo | Preservação intencional pós-saneamento | Manter; leitores devem priorizar seções TO-BE |
| `ONBOARDING_SOLICITACAO` (DDL) | Legado AS-IS; PA TO-BE não utiliza | Documentado em `04-entity-catalog.md` |

Nenhum artefato no escopo auditado contradiz a baseline de forma a impedir commit quando lido no conjunto com supersessions e GOVERNANCE §6.

---

## 11. Veredito final

### A baseline está suficientemente consistente para commit?

# **SIM**

Documentação normativa reconciliada; AS-IS e TO-BE explicitamente separados; pendências de implementação registradas sem ocultar gaps; nenhuma decisão humana pendente no escopo.

### Existe alguma decisão humana que eu precise tomar antes do commit?

# **NÃO**

### Por que não?

Todas as questões material no escopo são resolúveis pelas decisões já aprovadas:

- **Vínculo único e Contexto Ativo derivado** → DH-02 + conclusão Contexto Ativo × DH-02
- **Momento de criação do COLABORADOR** → DH-03, DH-PA-03
- **Composição do vínculo** → DH-04
- **Fluxo de Primeiro Acesso** → DH-PA-01/02/03
- **CARGO opcional** → DH-CARGO-01
- **`locateOrCreate` no login** → superseded normativamente (DA-AUTH-011); gap de código documentado
- **PA-API-006** → requisitos normativos definidos; contrato HTTP é delegação de engenharia, não lacuna de decisão de negócio

O commit desta baseline **não** implica que o código ou DDL estejam prontos para o TO-BE — apenas que o estado conhecido (AS-IS + decisões + specs reconciliadas) está documentado de forma consistente para iniciar a implementação do Primeiro Acesso.

---

*Fim da validação pré-commit.*
