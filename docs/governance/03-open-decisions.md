# Open Decisions

## Objetivo

Centralizar todas as decisões pendentes do projeto que possam impactar negócio, arquitetura, solução, implementação, construção ou entrega.

Este documento funciona como um registro de acompanhamento para decisões ainda não concluídas e deve ser utilizado como fonte oficial para monitoramento de definições críticas.

Após aprovação, cada decisão deve ser movida para sua documentação definitiva ou para os respectivos ADRs (Architecture Decision Records).

---

# Processo de Gestão de Decisões

## Fluxo oficial (alinhado à governança documental)

```text
Open Question
      ↓
Discussão
      ↓
DEC (alternativas + aprovação)
      ↓
Implementação / Registro definitivo (ADR, technology decision-log, specs)
```

Fonte normativa do fluxo: `docs/governance/07-documentation-architecture.md`.

Fluxo operacional interno deste registro (complementar, não substitui o fluxo acima):

```text
Identificação
      ↓
Análise
      ↓
Avaliação de Alternativas
      ↓
Aprovação
      ↓
Registro Definitivo
      ↓
Encerramento
```

**Proibido:** criar DEC “aberta” que apenas repita uma Open Question sem alternativas prontas para aprovação.

**IDs:** únicos no repositório — consultar também `docs/technology/04-decision-log.md` e ADRs antes de atribuir ID.

---

# Status Possíveis

| Status               | Descrição                |
| -------------------- | ------------------------ |
| Aberta               | Ainda não analisada      |
| Em Análise           | Avaliação em andamento   |
| Aguardando Aprovação | Pronta para decisão      |
| Aprovada             | Decisão tomada           |
| Rejeitada            | Alternativa descartada   |
| Cancelada            | Não será mais necessária |

---

# Criticidade

| Nível   | Descrição                                |
| ------- | ---------------------------------------- |
| Baixa   | Impacto localizado                       |
| Média   | Impacto moderado                         |
| Alta    | Impacto relevante                        |
| Crítica | Impacta arquitetura ou negócio principal |

---

# Resumo Executivo

| Status               | Quantidade |
| -------------------- | ---------- |
| Abertas              | 3          |
| Em Análise           | 0          |
| Aguardando Aprovação | 0          |
| Aprovadas            | 17         |
| Rejeitadas           | 0          |
| Canceladas           | 2          |

---

# Registro de Decisões

## DEC-001

### Título

Definição da estratégia de autenticação e autorização.

### Categoria

Arquitetura.

### Contexto

A solução necessita de um mecanismo centralizado para autenticação, autorização e gestão de identidade.

### Alternativas

* Keycloak
* Auth0
* Azure AD
* Solução própria
* Serviço Corporativo de Autenticação

### Impacto

Crítico.

### Responsável

Arquiteto de Software.

### Prazo

Sprint 1 (FT-AUTH)

### Status

**Aprovada** (2026-07-24).

### Decisão

Autenticação via **Zimbra** (Identity Provider) com arquitetura **Stateless** (JWT próprio + Refresh Token em cookies HttpOnly). Autorização permanece no Portal (banco). Protocolo de integração: **proxy de credenciais IMAP/SMTP/SOAP** (DA-AUTH-012).

### Registro definitivo

- ADRs: ADR-003, ADR-005, ADR-006 (`docs/architecture/08-decision-records.md`)
- Decisões de Feature: `specs/features/authentication/decisions.md` (DA-AUTH-001 a DA-AUTH-012)
- Arquitetura: `specs/architecture/authentication-architecture.md`
- Homologação operacional: `docs/discovery/ft-auth-zimbra-homologacao.md`

---

## DEC-002

### Título

Definição da estratégia de observabilidade.

### Categoria

Plataforma.

### Contexto

Necessidade de monitoramento operacional, métricas, logs e rastreamento distribuído.

### Alternativas

* Prometheus + Grafana
* Elastic Stack
* Datadog
* New Relic

### Impacto

Alta.

### Responsável

Arquitetura.

### Prazo

Sprint futura

### Status

Aberta — parcialmente endereçada na Sprint 0 (Correlation ID, Actuator). Métricas e dashboards pendentes.

---

## DEC-003

### Título

Definição da estratégia de mensageria.

### Categoria

Arquitetura.

### Contexto

Necessidade de processamento assíncrono e desacoplamento entre componentes.

### Alternativas

* RabbitMQ
* Kafka
* AWS SQS
* Sem mensageria

### Impacto

Alta.

### Responsável

Arquitetura.

### Prazo

Sprint futura

### Status

Aberta.

---

## DEC-004

### Título

Definição da estratégia de deploy.

### Categoria

Infraestrutura.

### Contexto

Necessidade de definir modelo de implantação e operação.

### Alternativas

* Docker Compose
* Kubernetes
* OpenShift
* Cloud Managed Platform

### Impacto

Alta.

### Responsável

DevOps.

### Prazo

Sprint futura

### Status

Aberta.

---

## DEC-005

### Título

Definição da estratégia de versionamento.

### Categoria

Governança.

### Contexto

Padronizar releases e evolução do produto.

### Alternativas

* Semantic Versioning
* Calendar Versioning
* Release Train

### Impacto

Média.

### Responsável

Tech Lead.

### Prazo

2026-07-08

### Status

**Aprovada** — Semantic Versioning adotado (`0.0.1-SNAPSHOT` em `backend/pom.xml`).

### Registro definitivo

`docs/implementation/11-bootstrap-roadmap.md`, `backend/pom.xml`.

---

## DEC-006

### Título

Definição da estratégia de testes automatizados.

### Categoria

Qualidade.

### Contexto

Estabelecer cobertura mínima e tipos de testes obrigatórios.

### Alternativas

* Unitários apenas
* Unitários + Integração
* Pirâmide completa de testes
* Estratégia baseada em risco

### Impacto

Alta.

### Responsável

QA Lead.

### Prazo

2026-07-08

### Status

**Aprovada** — Unitários + Integração (integração a partir de Sprint 1). Build obrigatório via `mvn clean verify`. Evidência: 106 testes unitários na Sprint 0.

### Registro definitivo

`docs/construction/backend/01-project-bootstrap.md` § Build.

---

## DEC-007

### Título

Definição da estratégia de banco de dados.

### Categoria

Dados.

### Contexto

Necessidade de consolidar a tecnologia principal de persistência.

### Alternativas

* PostgreSQL
* Oracle
* SQL Server
* MySQL

### Impacto

Alta.

### Responsável

Arquitetura.

### Prazo

2026-07-08

### Status

**Aprovada** — Oracle Database (schema `UNMPORTCOM`, driver `ojdbc11`, baseline DDL administrado pelo DBA — DEC-DB-019).

### Registro definitivo

`docs/implementation/06-database-standards.md`, `docs/construction/backend/01-project-bootstrap.md`, `backend/pom.xml`.

---

## Item provisório cancelado — Multi-contexto (login/sessão)

### Status

**Cancelado** (2026-07-24) — Gate Final.

### Motivo

1. Violava o fluxo **Open Question → Discussão → DEC → Implementação** (ainda é OQ-027 / OQ-008).
2. Recebeu temporariamente o rótulo “DEC-008”, **em colisão** com `docs/technology/04-decision-log.md` **DEC-008 — Segurança**. IDs de DEC não podem ser reutilizados entre catálogos.

### Registro oficial da pergunta

`docs/domain/10-open-questions.md` — **OQ-027**, **OQ-008**.

Quando respondida: criar DEC no catálogo correto (`docs/governance/03-open-decisions.md` ou technology, conforme natureza) com **novo ID** não colidente.

---

## Item provisório cancelado — Painel inicial (home route)

### Status

**Cancelado** (2026-07-24) — Gate Final.

### Motivo

1. Ainda é **OQ-028**, não decisão aprovada.
2. Rótulo temporário “DEC-009” **colidia** com `docs/technology/04-decision-log.md` **DEC-009 — Containers**.

### Registro oficial da pergunta

`docs/domain/10-open-questions.md` — **OQ-028**.

---

## DEC-FA-001 — Onboarding e primeiro acesso (encerra OQ-001)

### Título

Fluxo oficial de onboarding = resolução/seleção de Contexto Ativo (não solicitação administrativa).

### Categoria

Negócio + Arquitetura.

### Criticidade

Crítica.

### Status

**Aprovada** (2026-07-24).

### Alternativas consideradas

| Alternativa | Resultado |
| ----------- | --------- |
| Seleção direta de singular/área (legado CMS) | Incorporada como base do fluxo de contexto |
| Solicitação com aprovação administrativa (legado frontend `/onboarding/requests`) | **Rejeitada** como fluxo oficial de primeiro acesso |
| Onboarding como Feature dedicada de primeiro acesso | **Adotada** |

### Decisão

1. O onboarding oficial **não** é tela de solicitação/aprovação administrativa.
2. Após autenticação, o Portal carrega os vínculos do colaborador e:
   - **1 vínculo** → seleciona automaticamente o Contexto Ativo e entra no Portal;
   - **N vínculos** → usuário escolhe o Contexto Ativo e entra no Portal;
   - **0 vínculos válidos** → acesso operacional **bloqueado**.
3. A Feature **FT-PRIMEIRO-ACESSO** é dona desse fluxo (ver `specs/features/primeiro-acesso/`).

### Encerra

OQ-001.

### Registro definitivo

`docs/domain/09-business-rules.md` (BR-010, BR-011, BR-040+), `specs/features/primeiro-acesso/specification.md`, `docs/frontend/frontend-flow.md`.

---

## DEC-FA-002 — BR-010 e vínculo organizacional obrigatório (encerra OQ-026)

### Título

Colaborador operacional autenticado possui vínculo; navegação exige Contexto Ativo.

### Categoria

Negócio.

### Criticidade

Crítica.

### Status

**Aprovada** (2026-07-24).

### Decisão

1. Todo colaborador **operacional** possui pelo menos um vínculo organizacional com **Área**.
2. O sistema **não admite** colaborador operacional sem Área.
3. O login recupera o(s) vínculo(s) do colaborador.
4. A navegação operacional ocorre **sempre** dentro de um **Contexto Ativo**.
5. Sem vínculo válido → bloqueio de acesso operacional (não confundir com falha de autenticação Zimbra).

### Encerra

OQ-026.

### Registro definitivo

`docs/domain/09-business-rules.md` (BR-010 revisada), `specs/features/session/specification.md`, `specs/features/primeiro-acesso/specification.md`.

### Complemento (2026-08-14 — DH-02, DH-03, DH-04)

| Item DEC-FA-002 | Relação com decisões DH |
|-----------------|-------------------------|
| Operacional exige Área | **Mantido** — vínculo mínimo persistido inclui Área (DH-04) |
| Bloqueio sem vínculo válido | **Mantido** — identidade autenticada **sem** COLABORADOR permanece não operacional até onboarding |
| Login recupera vínculo(s) | **Complementado** — primeiro acesso **cria** o vínculo completo antes de persistir COLABORADOR (DH-03); logins subsequentes recuperam |
| Contexto Ativo | **Mantido** — estabelecido após COLABORADOR com vínculo completo |

**Distinção normativa:** **identidade autenticada** (Zimbra) ≠ **COLABORADOR persistido**. A ausência de COLABORADOR durante onboarding **não** viola DEC-FA-002; viola apenas operação plena.

---

## DEC-FA-003 — Multi-contexto e Contexto Ativo (encerra OQ-027)

### Título

Suporte a N vínculos organizacionais com um Contexto Ativo na sessão.

### Categoria

Negócio + Arquitetura.

### Criticidade

Alta.

### Status

**Aprovada** (2026-07-24).

### Alternativas consideradas

| Alternativa | Resultado |
| ----------- | --------- |
| Permanecer fase 1 (1 vínculo em `COLABORADOR`) | **Rejeitada** para a entrega alvo |
| Multi-contexto (N vínculos) + Contexto Ativo | **Adotada** |

### Decisão

1. Um colaborador pode possuir **N vínculos** organizacionais (ex.: Área A, B, C).
2. A sessão possui um **Contexto Ativo** contendo no mínimo: `federationId`, `singularId`, `areaId`.
3. Toda navegação operacional utiliza o Contexto Ativo.
4. RN-SESSION-003 deixa de ser backlog e passa a regra oficial (seleção quando N > 1).
5. Persistência do Contexto Ativo é responsabilidade de **FT-PRIMEIRO-ACESSO** / FT-SESSION — **sem** reintroduzir `COD_*_CTX` em `AUTH_SESSAO` (REF-DB-CTX-01 permanece). Mecanismo físico a especificar na Feature (fora desta atividade documental).

### Encerra

OQ-027. Atualiza interpretação de OQ-008 (N áreas aprovado; detalhe de N equipes por área permanece se aplicável).

### Registro definitivo

`specs/features/session/specification.md`, `docs/domain/04-domain-concepts.md`, `docs/domain/02-business-glossary.md`.

### Supersession parcial (2026-08-14 — DH-02, DEC-DB-028)

| Item DEC-FA-003 | Status após DH-02 |
|-----------------|-------------------|
| **P1** — N vínculos organizacionais | **SUPERSEDED** — 1 vínculo por COLABORADOR (DH-02) |
| **P2** — Contexto Ativo (`federationId`, `singularId`, `areaId`) | **MANTIDO** |
| **P3** — Navegação no Contexto Ativo | **MANTIDO** |
| **P4** — RN-SESSION-003 seleção quando N>1 | **SUPERSEDED** — sem seleção entre N vínculos cadastrais |
| **P5** — REF-DB-CTX-01 (sem `COD_*_CTX` em `AUTH_SESSAO`) | **MANTIDO** |
| **P6** — Alternativa “1 vínculo em COLABORADOR” rejeitada | **SUPERSEDED** — restaurada como alvo normativo (DH-02) |
| **P7** — ramo “N áreas” (OQ-008) | **SUPERSEDED** no eixo cadastral |
| `contexts[]` / PA-API-001 (lista N) | **SUPERSEDED** conceitualmente — ver evolução FT-PRIMEIRO-ACESSO |
| `organizationalLinks` singular | **MANTIDO** — compatível com 1:1 |

**Nota:** o texto histórico da decisão **não foi apagado**. A supersession parcial será refletida em BR-041, specs e implementação em etapas posteriores. **DH-01** (formalização integral da supersession) permanece pendente de registro dedicado se exigido pelo processo.

---

## DEC-FA-004 — Home dinâmica (encerra OQ-028)

### Título

Home resolvida pelo backend após Contexto Ativo.

### Categoria

Arquitetura + Planejamento.

### Criticidade

Alta.

### Status

**Aprovada** (2026-07-24).

### Decisão

1. A Home é **dinâmica**.
2. Após o Contexto Ativo resolvido, o Portal solicita a Home apropriada ao **backend**.
3. O frontend **apenas renderiza** a Home recebida — sem regras fixas de landing no cliente.
4. Placeholder `/app` deixa de ser a definição oficial (pode existir só até a Feature implementar o contrato).

### Encerra

OQ-028.

### Registro definitivo

`docs/frontend/frontend-flow.md`, `specs/features/primeiro-acesso/specification.md`.

---

## DEC-FA-005 — Tema claro/escuro e referência visual da Home

### Título

Seleção explícita e persistência de tema; frame Figma `Home` (node `7:3`) como referência visual oficial da Home desktop.

### Categoria

Produto/UX + Frontend Foundation.

### Criticidade

Média.

### Status

**Aprovada** (2026-08-25).

### Contexto

Infraestrutura de tema (`useTheme` composable, tokens `data-theme` claro/escuro) já existe desde os pacotes legados de Frontend Foundation (`PKG-FE-S0-02`/`PKG-FE-S0-08`, Construction v4.1), mas toggle de UI e persistência nunca foram entregues por nenhum dos dois pacotes. Não havia decisão formal registrada sobre o comportamento esperado de tema, nem sobre qual frame Figma é a referência oficial da Home desktop. Ambas as lacunas foram identificadas em investigação de reconciliação Figma × implementação desta sessão.

### Decisão

1. O usuário deve poder selecionar explicitamente o tema da aplicação entre **Claro** e **Escuro** — a aplicação não deve depender exclusivamente da preferência do navegador/sistema operacional (`prefers-color-scheme`).
2. A preferência de tema selecionada deve ser **persistida** e permanecer após recarregar a aplicação.
3. O frame Figma **Home** (arquivo `WHDHRAMXXslmxOIzK2dbJG`, node **`7:3`**) é a referência visual oficial para a reconciliação da Home desktop do colaborador.
4. O **tema claro** é a referência visual atualmente disponível no Figma para essa validação — o arquivo Figma não possui frame em tema escuro.
5. Medições/análise de código (comentários citando valores extraídos do Figma, comparação programática de propriedades CSS) **não substituem** validação visual formal contra o Figma. A conformidade visual deve ser verificada separadamente antes de qualquer item ser considerado definitivamente conforme.

### Não incluído (implementação futura)

- Implementação do toggle de UI, mecanismo de persistência e qualquer alteração em `useTheme.ts`, `boot/theme.ts`, tokens CSS, ou componentes de shell (`AppHeader`/`AppSidebar`/`AppFooter`/`AppShell`) — não fazem parte desta decisão; ficam para implementação e revisão separadas.
- Esta DEC **não autoriza retroativamente** nenhuma alteração já presente no working tree relacionada a Home/AppShell — essas mudanças serão avaliadas em revisão separada.

### Registro definitivo

A definir na implementação — provável `docs/frontend/frontend-flow.md` (comportamento de tema) e `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md` (referência de frame para reconciliação da Home). Não é uma Feature.

---

## DEC-ORG-001 — Hierarquia organizacional oficial

### Título

Estrutura Federação → Singular → Área → Equipe → Colaborador.

### Categoria

Domínio.

### Criticidade

Crítica.

### Status

**Aprovada** (2026-07-24).

### Decisão

Hierarquia oficial:

```text
Federação
 └── Singular
      └── Área
           └── Equipe
                └── Colaborador
```

- Todo colaborador operacional pertence **obrigatoriamente** a uma Área.
- Área pertence a uma Singular; Singular pertence a uma Federação.
- Não existe colaborador operacional sem vínculo organizacional.

### Registro definitivo

`docs/domain/04-domain-concepts.md`, `docs/domain/09-business-rules.md`, `docs/domain/01-vision.md`.

---

## DEC-ORG-002 — CARGO como entidade de domínio independente

### Título

Cargo/função organizacional distinto de papel de autorização.

### Categoria

Domínio — Organização Corporativa.

### Criticidade

Crítica.

### Status

**Aprovada** (2026-08-14).

### Contexto

A DEC-DB-016 (2026-07-10) rejeitou a entidade `CARGO` sob YAGNI. A formalização Etapa 6.x redefine o conceito de domínio: cargo é função organizacional, não permissão.

### Decisão

1. **CARGO** é entidade de domínio **independente**.
2. **CARGO** representa o cargo/função organizacional ocupado pelo colaborador.
3. Exemplos: Presidente; Diretor de Tecnologia; Gestor de Tecnologia da Informação; Analista de Sistemas; Desenvolvedor de Sistemas.
4. **CARGO ≠ PAPEL** (autorização).
5. **CARGO ≠ ADMIN_*** (papéis administrativos: `ADMIN_FEDERACAO`, `ADMIN_SINGULAR`, `ADMIN_AREA`, `ADMIN_EQUIPE`).
6. Ocupar um cargo **não concede** automaticamente permissões administrativas.

**Exemplo:**

```text
Colaborador: Vicente Freitas
Cargo: Gestor de Tecnologia da Informação
Papéis: ADMIN_AREA → Tecnologia da Informação; ADMIN_AREA → Financeiro
```

### Supersede (parcial)

DEC-DB-016 — item **Rejeitado: Entidade `CARGO`**. O restante de DEC-DB-016 permanece vigente para o schema AS-IS até revisão explícita.

### Persistência TO-BE

Formalizada em **DEC-DB-027** (`database/model/05-decisions-and-risks.md`): catálogo `CARGO` + vínculo com `COLABORADOR`. **Sem implementação** até etapa de DDL dedicada. **DH-CARGO-01** (2026-08-17) estabelece que CARGO **não é requisito** na criação/cadastro de COLABORADOR — supersession parcial de itens obrigatórios de DEC-DB-027 (ver § DH-CARGO-01 e DEC-DB-027 em `05-decisions-and-risks.md`).

### Não incluído (implementação futura)

- Tabela física `CARGO`, migration ou entidade JPA — ver DEC-DB-027 (formalizado; implementação pendente).
- Remoção de `COD_GESTOR`, `AREA.COD_GESTOR`, `EQUIPE.COD_LIDER`.
- Alteração de API, frontend ou testes.

### Registro definitivo

`construction/review/organizational-authorization-formalization-etapa6.md` (seção 12), `database/model/05-decisions-and-risks.md` (DEC-DB-016 reconciliação, **DEC-DB-027**).

---

## Decisões humanas — vínculo organizacional (DH)

Decisões de negócio aprovadas pelo decisor (2026-08-14). Consolidadas normativamente em **DEC-DB-028** (`database/model/05-decisions-and-risks.md`). Implementação e DDL **não** alterados nesta etapa.

### DH-02 — Cardinalidade do vínculo

**Status:** **APROVADA** (2026-08-14).

Um **COLABORADOR** possui exatamente **1** vínculo organizacional, representado pelas FKs escalares em `COLABORADOR`:

```text
Federação + Singular + Área (+ Equipe opcional)
```

### DH-03 — Momento de persistência (Alternativa A)

**Status:** **APROVADA** (2026-08-14).

O **COLABORADOR** somente é **criado/persistido** após o vínculo organizacional mínimo estar definido:

```text
Zimbra → identidade autenticada → domínio → Singular → Área → Equipe (opcional)
    → vínculo completo → criação do COLABORADOR
```

- **`locateOrCreate` no login** deixa de ser comportamento **normativo** de criação antecipada.
- **Implementação AS-IS** (`ColaboradorService.locateOrCreate` em `finalizeLogin`) permanece até etapa de implementação — registrada como **GAP** (ver DEC-DB-020 supersession parcial).

### DH-04 — Vínculo mínimo persistido

**Status:** **APROVADA** (2026-08-14).

Um **COLABORADOR** **não pode** existir persistentemente somente com Federação.

| Campo | Obrigatoriedade no COLABORADOR persistido |
|-------|---------------------------------------------|
| `COD_FEDERACAO` | NOT NULL |
| `COD_SINGULAR` | NOT NULL |
| `COD_AREA` | NOT NULL |
| `COD_EQUIPE` | NULL permitido |

**Estado permitido (persistido):** Federação + Singular + Área (+ Equipe opcional).

**Estado não permitido (persistido):** somente Federação.

**Estado transitório permitido:** identidade autenticada (Zimbra) **antes** da criação do COLABORADOR — **não** implica COLABORADOR incompleto persistido.

### Pendências arquiteturais (vínculo organizacional)

- Implementação física do mapeamento **domínio → Singular** (ver DEC-ORG-003, BR-043, **DH-PA-02** aprovada 2026-08-15; **GAP-028-04** — artefatos no repositório; execução DBA da V008 no Oracle pendente).

---

## Decisões humanas — Primeiro Acesso (DH-PA)

Decisões de negócio/arquitetura aprovadas pelo decisor. Coerentes com **DH-02**, **DH-03**, **DH-04**, **DEC-ORG-003**, **DEC-DB-028** e **DEC-FA-001..004**. Detalhes de implementação da credencial temporária **não** fazem parte desta decisão — permanecem delegados à engenharia.

### DH-PA-01 — Credencial temporária de Primeiro Acesso

**Status:** **APROVADA** (2026-08-15).

**Categoria:** Negócio / Arquitetura — Primeiro Acesso.

#### Decisão

1. O **Zimbra** é responsável por confirmar a identidade do usuário.
2. O **Portal** determina se é **Primeiro Acesso** verificando se existe **COLABORADOR** correspondente à identidade autenticada.
3. Quando **não** existe COLABORADOR, o Portal utiliza uma **credencial temporária**, de **escopo restrito ao Primeiro Acesso**, **sem persistir `AUTH_SESSAO` operacional**.
4. Após a conclusão do vínculo organizacional e **criação do COLABORADOR**, o usuário passa ao **estado operacional**.

#### Fluxo normativo

```text
Credenciais
    ↓
Zimbra autentica
    ↓
Portal identifica a identidade
    ↓
Portal verifica existência de COLABORADOR
    │
    ├── SIM → acesso operacional
    │
    └── NÃO → credencial temporária
              ↓
            Primeiro Acesso
              ↓
            Onboarding
              ↓
            vínculo organizacional
              ↓
            criação do COLABORADOR
              ↓
            acesso operacional
```

#### Consequências derivadas (não são decisões separadas)

- **`locateOrCreate` no login** deixa de ser comportamento normativo quando a identidade ainda não possui COLABORADOR com vínculo completo — alinhado a **DH-03** e **GAP-028-01**.
- A identidade autenticada **pode existir sem COLABORADOR persistido** durante o onboarding — coerente com **DH-03** e **DEC-DB-028** (item 6).

#### Deliberadamente não decidido (delegado à engenharia)

A DH-PA-01 **não** estabelece:

- mecanismo técnico da credencial (JWT ou outro);
- identificador técnico da identidade (`email`, `zimbraId` ou outro);
- claims, `sub`, `typ`, scopes, authorities;
- TTL, refresh, cookies, CSRF;
- `jti`, invalidação, proteção contra replay;
- contrato técnico definitivo do token;
- detalhes de `/auth/me`, guards ou `SecurityContext`;
- mecanismo técnico de promoção para o estado operacional.

A engenharia deverá propor solução técnica **compatível** com esta decisão.

#### Pendências relacionadas (não resolvidas por DH-PA-01)

| ID | Tema | Status |
|----|------|--------|
| **DH-PA-02** | Domínio e-mail → Singular (cardinalidade e comportamento) | **APROVADA** (2026-08-15) |
| **DH-PA-03** | Política de CARGO no Primeiro Acesso | **APROVADA** (2026-08-17) |

Evidência analítica: `construction/review/primeiro-acesso-blocking-decisions-package.md`, `construction/review/primeiro-acesso-dh-pa-03-analysis.md`.

#### Registro definitivo

Esta seção. Encerra lacuna **LA-01** e **GAP-028-03** (decisão de negócio); implementação permanece como GAP até etapa de código.

---

### DH-PA-02 — Domínio do e-mail → Singular

**Status:** **APROVADA** (2026-08-15).

**Categoria:** Negócio / Arquitetura — Primeiro Acesso.

#### Contexto

**DEC-ORG-003** estabelece que o domínio do e-mail autenticado determina a Singular. **DH-PA-02** define as restrições de cardinalidade e o comportamento necessários para que essa regra seja executável de forma **determinística** no fluxo de Primeiro Acesso (**DH-PA-01**), sem alterar a regra de negócio fundamental.

Evidência analítica: `construction/review/primeiro-acesso-dh-pa-02-analysis.md`, `construction/review/primeiro-acesso-blocking-decisions-package.md` §6.

#### Decisão

##### DH-PA-02.1 — Cardinalidade domínio → Singular

1. Uma Singular **não pode** possuir múltiplos domínios de e-mail.
2. A relação normativa é: **um domínio determina exatamente uma Singular**.
3. Um mesmo domínio **não pode** determinar mais de uma Singular.

##### DH-PA-02.2 — Domínio sem Singular

Quando o domínio do e-mail autenticado **não possuir** Singular cadastrada:

1. O Portal **não prossegue** automaticamente com o Primeiro Acesso.
2. O frontend **informa** ao usuário que não foi possível determinar sua Singular.
3. O sistema **deverá possuir posteriormente** capacidade administrativa para cadastrar ou configurar a associação domínio → Singular.
4. Essa configuração será realizada pelo **Administrador do Sistema**.

#### Relação com DEC-ORG-003

**DH-PA-02 não altera DEC-ORG-003.** Complementa a regra de negócio com cardinalidade determinística e comportamento para domínio sem mapeamento cadastrado.

#### Fluxo normativo (complemento a DH-PA-01)

```text
credencial temporária (DH-PA-01)
    ↓
domínio do e-mail autenticado
    │
    ├── domínio com Singular cadastrada
    │       ↓
    │   exatamente uma Singular (DH-PA-02.1)
    │       ↓
    │   seleção de Área → Equipe (opcional) → criação do COLABORADOR
    │
    └── domínio sem Singular cadastrada (DH-PA-02.2)
            ↓
        frontend informa o usuário
            ↓
        Primeiro Acesso não prossegue automaticamente
```

#### Consequências derivadas (não são decisões separadas)

- A resolução domínio → Singular deve ser **determinística**.
- O Primeiro Acesso **não pode prosseguir** quando não houver Singular correspondente ao domínio autenticado.
- A implementação **deve impedir** inconsistências em que um domínio determine mais de uma Singular.
- O sistema **deverá possuir posteriormente** capacidade administrativa de cadastro da associação domínio → Singular pelo Administrador do Sistema.

#### Deliberadamente não decidido (delegado à engenharia)

**DH-PA-02 não estabelece:**

- forma física de persistência (coluna, tabela, constraints, índices, migrations);
- JPA, repository, service, endpoint, DTO;
- algoritmo de resolução e normalização técnica do domínio;
- implementação da tela administrativa, texto definitivo da mensagem no frontend, menu, fluxo administrativo ou permissões técnicas do Administrador do Sistema.

A engenharia deverá propor solução técnica **compatível** com esta decisão.

#### Pendências relacionadas (não resolvidas por DH-PA-02)

| ID | Tema | Status |
|----|------|--------|
| **DH-PA-03** | Política de CARGO no Primeiro Acesso | **APROVADA** (2026-08-17) |
| **GAP-028-04** | Implementação física do mapeamento domínio → Singular | Artefatos no repositório (DDL, V008, DML); execução DBA no Oracle pendente |
| **RECONCILIAÇÃO-DEC-DB-027** | Reconciliação DH-PA-03 × DEC-DB-027 | **Encerrada** (2026-08-17) — **DH-CARGO-01** |

#### Registro definitivo

Esta seção. Encerra decisão de negócio de **LA-02** e **GAP-028-04** (nível de decisão); artefatos de persistência preparados no repositório; aplicação da V008 no Oracle permanece com o DBA.

---

### DH-PA-03 — CARGO no Primeiro Acesso

**Status:** **APROVADA** (2026-08-17).

**Categoria:** Negócio / Arquitetura — Primeiro Acesso.

#### Contexto

**DEC-ORG-002** define **CARGO** como função organizacional independente de **PAPEL** e **ADMIN_***. **DEC-DB-027** aprovou catálogo `CARGO` e `COLABORADOR.COD_CARGO NOT NULL` na criação do colaborador. Os fluxos de Primeiro Acesso já aprovados (**DH-PA-01**, **DH-PA-02**, **DH-03**, **DH-04**) definem vínculo organizacional (domínio → Singular → Área → Equipe opcional) **sem etapa de CARGO**. **DH-PA-03** estabelece a política de negócio sobre o papel de CARGO nesse fluxo.

Evidência analítica: `construction/review/primeiro-acesso-dh-pa-03-analysis.md`, `construction/review/primeiro-acesso-blocking-decisions-package.md` §7.

#### Decisão

> **CARGO não é requisito para a criação nem para a existência operacional do COLABORADOR durante o Primeiro Acesso. O COLABORADOR pode entrar em estado operacional sem CARGO. O CARGO poderá ser definido posteriormente, por outro fluxo ou processo.**

#### Consequências derivadas (não são decisões separadas)

- CARGO **não bloqueia** o Primeiro Acesso;
- CARGO **não é requisito** para autenticação;
- CARGO **não é requisito** para conclusão do vínculo organizacional;
- CARGO **não participa** da determinação da Singular;
- CARGO **não participa** da determinação da Área;
- CARGO **não participa** da determinação da Equipe;
- CARGO **não determina** o PAPEL;
- CARGO **não deve ser utilizado** como substituto de PAPEL;
- a **ausência de CARGO não impede** o COLABORADOR de atingir o estado operacional;
- a definição de CARGO **poderá ocorrer posteriormente**;
- o processo responsável pela definição posterior de CARGO será tratado **fora desta decisão**, salvo regra existente que já determine esse processo.

#### Relação CARGO / PAPEL

| Conceito | Representação |
|----------|---------------|
| **CARGO** | Função/cargo organizacional do colaborador (DEC-ORG-002) |
| **PAPEL** | Função de autorização do usuário no sistema |

**Preservado explicitamente:**

- **Não** presumir `CARGO → PAPEL`;
- **Não** presumir `PAPEL → CARGO`;
- **Não** criar herança automática entre os dois.

**DH-PA-03** reforça que CARGO não é requisito para autenticação nem para o fluxo de Primeiro Acesso.

#### Fluxo normativo (complemento a DH-PA-01 e DH-PA-02)

```text
Zimbra autentica
    ↓
Portal verifica COLABORADOR
    ↓
COLABORADOR inexistente
    ↓
Primeiro Acesso
    ↓
credencial temporária (DH-PA-01)
    ↓
domínio → Singular (DH-PA-02)
    ↓
Área
    ↓
Equipe opcional (DH-04)
    ↓
criação do COLABORADOR (DH-03)
    ↓
estado operacional

                 CARGO
                   │
                   └── não bloqueia o fluxo
                       poderá ser definido posteriormente
```

CARGO **não** constitui etapa obrigatória do wizard de Primeiro Acesso. **Não** introduzir etapa de seleção de CARGO no fluxo normativo.

#### Complemento normativo (DH-CARGO-01, 2026-08-17)

A política de **DH-PA-03** no eixo Primeiro Acesso está **alinhada** e **subsumida** pela decisão de escopo geral **DH-CARGO-01** (CARGO não obrigatório na criação/cadastro de qualquer COLABORADOR). A reconciliação **DH-PA-03 × DEC-DB-027** foi **encerrada** por **DH-CARGO-01** — ver § DH-CARGO-01 e supersession parcial em `database/model/05-decisions-and-risks.md` (DEC-DB-027).

#### Registro definitivo

Esta seção. Encerra decisão de negócio de política de CARGO no Primeiro Acesso. Reconciliação com **DEC-DB-027** encerrada em **DH-CARGO-01**.

---

### DH-CARGO-01 — CARGO opcional na criação do COLABORADOR

**Status:** **APROVADA** (2026-08-17).

**Categoria:** Negócio / Domínio — CARGO × COLABORADOR.

**Encerra:** **RECONCILIAÇÃO-DEC-DB-027**, **GAP-028-06** (nível de governança); questão **R1** (`construction/review/primeiro-acesso-r1-dec-db-027-applicability.md`).

#### Contexto

**DEC-ORG-002** e **DEC-DB-027** formalizaram **CARGO** como domínio com persistência própria. **DEC-DB-027** (2026-08-14) estabeleceu, entre outros, que CARGO era **obrigatório no momento da criação** de qualquer colaborador e que `COLABORADOR.COD_CARGO` seria **NOT NULL**, com proibição de `nullable=true` para sustentar essa obrigatoriedade. **DH-PA-03** (2026-08-17) estabeleceu que CARGO não é requisito no **Primeiro Acesso**. A análise em `construction/review/primeiro-acesso-dh-pa-03-db-reconciliation.md` classificou **conflito normativo (C)** entre DH-PA-03 e os itens de obrigatoriedade na criação de DEC-DB-027. O decisor humano deliberou a regra de **escopo geral** abaixo, aplicável a **qualquer** fluxo de criação/cadastro de COLABORADOR.

Evidência: `construction/review/primeiro-acesso-r1-dec-db-027-applicability.md`, `construction/review/primeiro-acesso-dh-pa-03-db-reconciliation.md`.

#### Decisão

> **CARGO é um domínio do sistema e possuirá persistência própria, porém CARGO não é requisito para o cadastro/criação do COLABORADOR. O COLABORADOR pode existir sem CARGO. A atribuição de CARGO ocorrerá posteriormente, em fluxo ainda não definido.**

#### Regra normativa resultante

> **CARGO não é obrigatório para a criação/cadastro de qualquer COLABORADOR.**

**DEC-DB-027 não pode estabelecer CARGO como requisito obrigatório para a criação de qualquer COLABORADOR** — os itens conflitantes de DEC-DB-027 foram **superseded** (ver supersession parcial registrada em `database/model/05-decisions-and-risks.md` § DEC-DB-027). O texto histórico de DEC-DB-027 **permanece** no repositório.

#### Escopo

Esta decisão é **geral** e abrange **qualquer** fluxo de criação/cadastro de COLABORADOR, incluindo sem limitação a:

- Primeiro Acesso (**DH-PA-03** — consequências mantidas e alinhadas);
- cadastro administrativo (FT-COLABORADOR);
- criação automática legada (`locateOrCreate` — AS-IS, não normativo);
- demais fluxos de criação existentes ou futuros.

**Não** se trata de exceção restrita ao Primeiro Acesso.

#### O que permanece decidido

- **CARGO** continua sendo **domínio do sistema** (DEC-ORG-002, DEC-DB-027 item 1);
- **CARGO** possuirá **persistência própria** (catálogo — DEC-DB-027);
- **CARGO ≠ PAPEL**; **CARGO ≠ ADMIN_*** (DEC-ORG-002, DEC-DB-027 item 5);
- **CARGO não concede autorização** automaticamente;
- **CARGO não faz parte do vínculo organizacional** (DEC-DB-027 item 6);
- **atribuição posterior** de CARGO permanece prevista;
- exclusões de DEC-DB-027 mantidas (`ATRIBUICAO_CARGO`, `COD_CARGO` no vínculo, etc.);
- **DH-PA-01**, **DH-PA-02**, **DH-PA-03**, **DH-03**, **DH-04** permanecem **intactas**.

#### Supersession parcial de DEC-DB-027 (resumo)

| Elemento DEC-DB-027 (texto original) | Status após DH-CARGO-01 |
|--------------------------------------|-------------------------|
| Catálogo `CARGO`; persistência própria | **MANTIDO** |
| CARGO ≠ PAPEL; fora do vínculo; exclusões | **MANTIDO** |
| Item 2 — cardinalidade 1:1 quando CARGO atribuído | **MANTIDO** (estado com CARGO; não exige CARGO na criação) |
| Item 4 — CARGO obrigatório na criação | **SUPERSEDED** |
| `COD_CARGO NOT NULL` na criação | **SUPERSEDED** |
| Proibição de `nullable=true` (sustentar obrigatoriedade na criação) | **SUPERSEDED** |
| Diagrama `CREATE COLABORADOR → CARGO obrigatório` | **SUPERSEDED** |
| Consequências TO-BE que exigem `cargoId` obrigatório no cadastro | **SUPERSEDED** como regra universal |

Detalhamento integral: `database/model/05-decisions-and-risks.md` § DEC-DB-027 — Supersession parcial.

#### Deliberadamente não decidido

**DH-CARGO-01 não estabelece:**

- `COD_CARGO` NULL ou NOT NULL;
- default sistêmico de CARGO;
- CARGO obrigatório após prazo ou evento;
- workflow, ator, tela ou API de atribuição posterior;
- seed de catálogo;
- cardinalidade física definitiva no DDL;
- trigger, constraint, migration ou representação JPA da ausência de CARGO.

#### Registro definitivo

Esta seção. Encerra reconciliação normativa **DEC-DB-027 × DH-PA-03**. Implementação física de `CARGO`/`COD_CARGO` permanece **delegada** à engenharia, sem obrigatoriedade de CARGO na criação do COLABORADOR.

---

## DEC-ORG-003 — Domínio de e-mail identifica Singular

### Título

O domínio do e-mail corporativo autenticado determina a Singular da identidade.

### Categoria

Negócio — Organização Corporativa.

### Criticidade

Crítica.

### Status

**Aprovada** (2026-08-14).

### Decisão

1. O **domínio** do e-mail corporativo autenticado (pós-Zimbra) **determina** a Singular associada à identidade.
2. A resolução é **autoridade do backend** — o frontend **não** determina a Singular.
3. O usuário **não pode** selecionar Singular diferente da determinada pelo domínio quando este já a identifica.
4. A **Área** é selecionada pelo usuário **dentro** da Singular determinada.
5. A **Equipe** permanece **opcional**, dentro da Área selecionada.
6. A **Federação** é derivada da Singular resolvida (`SINGULAR.COD_FEDERACAO`).

**Exemplos ilustrativos (não exaustivos):**

| Domínio | Singular |
|---------|----------|
| `unimedcariri.com.br` | Unimed Cariri |
| `unimedceara.com.br` | Unimed Ceará |

### Complemento normativo (DH-PA-02, 2026-08-15)

**DH-PA-02** complementa esta decisão sem alterá-la. Cardinalidade e comportamento para domínio sem Singular cadastrada estão formalizados em **DH-PA-02** (aprovada).

### Lacunas deliberadamente não decididas (implementação física)

A **regra de negócio** e as **restrições de cardinalidade** estão aprovadas (**DEC-ORG-003**, **DH-PA-02**). A **forma física** do mapeamento domínio → Singular permanece delegada à engenharia:

- nome de coluna, tabela ou entidade;
- normalização técnica, wildcard, regex;
- implementação da capacidade administrativa de cadastro da associação.

### Registro definitivo

`docs/domain/09-business-rules.md` (BR-043, BR-044), DEC-DB-028, DH-03/DH-04, **DH-PA-02**.

---

## DEC-DB-027 — Catálogo CARGO e vínculo obrigatório com COLABORADOR (referência)

### Título

Modelo físico TO-BE de `CARGO` e `COLABORADOR.COD_CARGO NOT NULL`.

### Categoria

Dados — persistência Oracle.

### Criticidade

Crítica.

### Status

**Aprovada** (2026-08-14).

### Decisão (resumo)

1. Catálogo `CARGO` (`COD_CARGO`, `NOM_CARGO`, `FLG_ATIVO`, auditoria).
2. `COLABORADOR.COD_CARGO` FK **NOT NULL** — cargo obrigatório na criação.
3. Cardinalidade: 1 colaborador → 1 cargo; 1 cargo → N colaboradores.
4. Sem `ATRIBUICAO_CARGO`; sem `COD_CARGO` no vínculo organizacional.
5. CARGO ≠ PAPEL ≠ ADMIN_* — cargo não concede autorização.

### Complemento normativo (DH-CARGO-01, 2026-08-17)

**DH-CARGO-01** estabelece que CARGO **não é obrigatório** na criação/cadastro de **qualquer** COLABORADOR. Os itens 2 e 4 do resumo acima (`COD_CARGO NOT NULL` / cargo obrigatório na criação) foram **superseded** — ver texto histórico integral em `database/model/05-decisions-and-risks.md` e supersession parcial registrada. **DEC-DB-027 permanece válida** nos elementos não conflitantes (catálogo, separação CARGO/PAPEL, CARGO fora do vínculo).

**Reconciliação DEC-DB-027 × DH-PA-03:** **ENCERRADA** por **DH-CARGO-01**.

### Encerra

PD-CARGO-01, PD-CARGO-02, PD-CARGO-03.

### Registro definitivo

`database/model/05-decisions-and-risks.md` (DEC-DB-027 — texto integral). Evidência: `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md` (v2.0).

---

## DEC-CMS-001 — Fronteira do CMS

### Título

CMS é exclusivamente provedor de conteúdo.

### Categoria

Arquitetura.

### Criticidade

Alta.

### Status

**Aprovada** (2026-07-24) — reforço explícito do desacoplamento já previsto em Solution Design.

### Decisão

O CMS **não** faz parte da estrutura organizacional e **não** controla Federação, Singular, Área, Equipe, Perfis ou Permissões.

O CMS é **exclusivamente** provedor de conteúdo. Autorização e organização permanecem no Portal (Backend).

### Registro definitivo

`docs/solution-design/01-solution-overview.md` (limites CMS reforçados), `docs/architecture/` (referência cruzada).

---

## DEC-CMS-002 — Comunicado é publicação do CMS (encerra OQ-004)

### Título

Comunicado é conteúdo do CMS (WordPress), não categoria de documento.

### Categoria

Domínio / Arquitetura.

### Criticidade

Alta.

### Status

**Aprovada** (2026-08-27) — decisão de produto do usuário; encerra OQ-004 e estabiliza a fronteira Gestão Documental ↔ Comunicação Interna.

### Decisão

1. **Comunicado é uma publicação** (título, descrição/conteúdo, imagem de destaque opcional, arquivos anexos) servida por **API do WordPress** integrada ao Backend. Pertence a Comunicação Interna (`FT-NOTICIA`, repositório `portal-comunicacao-cms`). **Não** é um valor de `CATEGORIA_DOCUMENTAL` nem um `DOCUMENTO` do módulo de Gestão Documental. O item conflitante de **BR-039** ("Comunicado institucional segue regras de documento ou de canal interno?") resolve-se por **canal interno / publicação**, não documento.
2. **Consequência — `CATEGORIA_DOCUMENTAL` passa a ser taxonomia por tipo de mídia.** Sem `Comunicado`, e como a taxonomia do seed histórico (`Normativos`/`Manuais`/`Políticas`/`Procedimentos`/`Comunicados` em `database/ddl/008-initial-data.sql`) nunca foi aplicada e não reflete o produto, `CATEGORIA_DOCUMENTAL` passa a classificar o documento pelo **tipo de mídia do arquivo**: `Documentos`, `Imagens`, `Vídeos`, `Outros`. A categoria de um documento é **derivada** do `TIP_MIME` no Backend, não escolhida pelo usuário nesta fase. Um seletor de categoria e a reconciliação plena da taxonomia ficam para uma Feature futura de categorização documental.
3. **Integração WordPress ↔ Backend** (contrato, autenticação, cache, "boas práticas") é escopo de `FT-NOTICIA` / **GAP-DEC-010**, não desta decisão.

### Registro definitivo

`docs/domain/10-open-questions.md` (OQ-004 encerrada), `specs/features/documento-upload/specification.md` § Categorização por tipo de mídia (primeiro consumidor), `docs/domain/09-business-rules.md` BR-039 (referência cruzada). Compatível com DEC-CMS-001 (CMS é exclusivamente provedor de conteúdo).

---

# Decisões Críticas Abertas

| ID      | Decisão                    | Sprint prevista |
| ------- | -------------------------- | --------------- |
| DEC-002 | Estratégia de observabilidade | Sprint futura |
| DEC-003 | Estratégia de mensageria   | Sprint futura   |
| DEC-004 | Estratégia de deploy       | Sprint futura   |

> **Atenção:** IDs `DEC-FA-*`, `DEC-ORG-*`, `DEC-CMS-*` evitam colisão com `docs/technology/04-decision-log.md` (DEC-008+). OQ-001/026/027/028 **encerradas** pelas DECs acima (2026-07-24); OQ-004 **encerrada** por DEC-CMS-002 (2026-08-27).

---

# Dependências

## Discovery

* Objetivos do produto
* Restrições do negócio

---

## Architecture

* ADRs
* NFRs
* Integrações

---

## Solution Design

* APIs
* Segurança
* Dados

---

## Implementation

* Tecnologias
* Frameworks
* Padrões de desenvolvimento

---

# Critérios de Encerramento

Uma decisão somente pode ser encerrada quando:

* existir justificativa registrada;
* existir aprovação formal;
* impactos forem documentados;
* documentação alvo for atualizada.

---

# Histórico de Atualizações

| Data       | Autor           | Alteração                                              |
| ---------- | --------------- | ------------------------------------------------------ |
| YYYY-MM-DD | Project Manager | Criação inicial do documento                           |
| 2026-07-08 | Governança      | DEC-005, DEC-006, DEC-007 aprovadas na Sprint 0; DEC-001 vinculada a FT-AUTH |
| 2026-07-24 | Governança      | DEC-001 aprovada; itens multi-contexto/painel cancelados (OQ prematuras + colisão ID com technology DEC-008/009) |
| 2026-07-24 | Governança      | DEC-FA-001..004, DEC-ORG-001, DEC-CMS-001 aprovadas; OQ-001/026/027/028 encerradas |
| 2026-08-14 | Governança      | DEC-ORG-002 aprovada — CARGO entidade de domínio; DEC-DB-016 parcialmente superseded |
| 2026-08-14 | Governança      | DEC-DB-027 aprovada — catálogo CARGO + COLABORADOR.COD_CARGO NOT NULL; PD-CARGO-01/02/03 encerradas |
| 2026-08-14 | Governança      | DH-02/03/04 aprovadas; DEC-ORG-003 (domínio→Singular); DEC-DB-028; supersession parcial DEC-FA-003 e DEC-DB-020 |
| 2026-08-15 | Governança      | **DH-PA-01** aprovada — credencial temporária de Primeiro Acesso (sem AUTH_SESSAO operacional); LA-01 e GAP-028-03 encerrados em nível de decisão |
| 2026-08-15 | Governança      | **DH-PA-02** aprovada — cardinalidade domínio→Singular (1:1); domínio sem Singular bloqueia PA automaticamente; capacidade administrativa futura; LA-02 e GAP-028-04 encerrados em nível de decisão |
| 2026-08-17 | Governança      | **DH-PA-03** aprovada — CARGO não é requisito para criação nem operação no Primeiro Acesso; reconciliação com DEC-DB-027 registrada como pendente (PONTO DE RECONCILIAÇÃO) |
| 2026-08-17 | Governança      | **DH-CARGO-01** aprovada — CARGO não obrigatório na criação de qualquer COLABORADOR; supersession parcial DEC-DB-027; reconciliação DEC-DB-027 × DH-PA-03 encerrada; R1 decidida (escopo geral) |
| 2026-08-17 | Engenharia      | **GAP-028-04** — artefatos de persistência no repositório (`DES_DOMINIO_EMAIL`, `UK_SINGULAR_DOMINIO_EMAIL`, V008, DML); execução DBA no Oracle pendente |
| 2026-08-25 | Governança      | **DEC-FA-005** aprovada — tema claro/escuro (seleção explícita + persistência) e frame Figma `Home` (node `7:3`) como referência visual oficial da Home desktop |
