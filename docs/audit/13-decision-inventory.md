# Inventário de Decisões do Projeto

**Documento:** `docs/audit/13-decision-inventory.md`
**Categoria documental:** Working (diagnóstico/governança — não é SSOT)
**Data:** 2026-08-20
**Etapa:** DECISION-INVENTORY-01 (D5). **Não avança para D6.**
**Autor:** Levantamento assistido (Claude Code), leitura somente, sem alterações físicas em SSOT, código, banco ou infraestrutura.

---

## Objetivo

Produzir um inventário único das decisões arquiteturais, tecnológicas, de domínio e de produto do Portal de Comunicação, classificando cada uma como **Aprovada, Proposta, Implícita, Conflitante, Lacuna ou Histórica**, para dar ao proprietário do projeto uma visão consolidada do que já está decidido, do que está em aberto, do que está em conflito e do que precisa de decisão humana antes da próxima fase. Este documento é diagnóstico: não aprova, não revoga e não substitui nenhum SSOT existente.

## Escopo e método

Varredura somente-leitura, feita por três levantamentos paralelos e independentes, cobrindo:

- `docs/governance/` (incl. `docs/governance/history/`), `docs/architecture/` (incl. `docs/architecture/decisions/`), `docs/solution-design/`, `docs/technology/`
- `specs/foundation/`, `specs/domain/` (hoje vazio), `specs/features/*` (14 features), `docs/domain/`, `construction/review/*`
- `database/` (ddl, migrations, model), `backend/` (configuração e código), `frontend/` (stack e módulos de auth), `docker-compose.yml`, `.github/workflows/`

Não foi assumido que uma decisão existe apenas por haver implementação, nem que deixou de existir por não estar implementada — cada achado abaixo distingue explicitamente **decisão** de **configuração/implementação**. As decisões já discutidas em etapas anteriores (D2–D4, referentes a Oracle, Cargo/Vínculo e Primeiro Acesso) foram incorporadas via `construction/review/*`.

Convenção de ID: decisões com identificador oficial mantêm o ID original (`DEC-XXX`, `DH-XXX`, `ADR-XXX`, `D1..D9` do Plano W2, `PD-XXX`, `RV-XXX`). Lacunas sem decisão formal recebem identificador **local e não oficial** `GAP-DEC-NNN`.

---

## Decisões aprovadas

| ID | Tema | Estado | Evidência | Impacto | Escopo | Decisor |
|---|---|---|---|---|---|---|
| DEC-CMS-001 | Fronteira Portal × WordPress: CMS é exclusivamente provedor de conteúdo; não controla Federação/Singular/Área/Equipe/Perfis/Permissões | Aprovada 2026-07-24 | `docs/governance/03-open-decisions.md:1182-1209`; materializada em `docs/solution-design/01-solution-overview.md:150-177` | Muito alto | Arquitetura/Produto | Humano/projeto |
| DEC-001 (tech) | Oracle Database 23ai é o banco oficial; local = Oracle XE | Aprovada 2026-06-22 | `docs/technology/04-decision-log.md` DEC-001; reafirmada em `docs/governance/03-open-decisions.md` DEC-007 | Muito alto | Tecnologia/Dados | Humano/projeto |
| DEC-006 (tech) | Evolução de schema exclusivamente por DDL versionado, executado pelo DBA; Flyway não é utilizado | Aprovada | `docs/technology/01-technology-stack.md:155-170`; `04-decision-log.md` DEC-006 | Alto | Tecnologia/Dados | Humano/projeto |
| DEC-DB-024 | Usuário de aplicação `UNMPORTCOM_APP` distinto do schema owner `UNMPORTCOM`, via role `UNMPORTCOM_APP_ROLE`; backend nunca conecta como owner | Approved 2026-07-23 | `docs/architecture/decisions/DEC-DB-024-application-user-strategy.md` (regras R-01..R-06) | Alto | Segurança/Dados | Técnico/DBA |
| DEC-001 (governance) | Autenticação via Zimbra (IdP) + arquitetura Stateless (JWT próprio + Refresh Token em cookie HttpOnly); autorização permanece no Portal | Aprovada 2026-07-24 | `docs/governance/03-open-decisions.md` DEC-001; `ADR-003` em `docs/architecture/08-decision-records.md:113-141` | Muito alto | Arquitetura/Segurança | Humano/projeto |
| DEC-008/009 (tech) | JWT+RBAC / Spring Security; containerização Docker oficial | Approved | `docs/technology/04-decision-log.md` | Alto | Tecnologia | Humano/projeto |
| DEC-DB-022 | Área em nível único (hierarquia entre áreas removida) | Aprovada, v1.2.0 histórica | `specs/features/area/specification.md` | Médio | Domínio | Humano/projeto |
| DEC-DB-015 | Gestor de Área / Líder de Equipe referenciam colaborador ativo; múltiplos líderes fora de escopo | Aprovada | `specs/features/area/`, `specs/features/equipe/specification.md` | Médio | Domínio | Humano/projeto |
| DH-02 | Modelo organizacional: 1 vínculo cadastral por COLABORADOR (não N vínculos); Contexto Ativo é projeção derivada das FKs, não estado persistido | Aprovada 2026-08-14 | `docs/domain/09-business-rules.md` BR-041; `specs/features/session/specification.md` (RN-SESSION-003 marcada SUPERSEDED) | Muito alto | Domínio/Arquitetura | Humano/projeto |
| DH-CARGO-01 | CARGO não é obrigatório na criação de COLABORADOR (supersede parcialmente DEC-DB-027); cardinalidade 1:1 CARGO↔COLABORADOR quando atribuído mantida | Aprovada, reconciliação encerrada 2026-08-17 | `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md`; `docs/domain/09-business-rules.md` BR-045 | Alto | Domínio | Humano/projeto |
| DH-PA-01 | Modelo de credencial temporária pré-COLABORADOR (identidade autenticada sem colaborador persistido) | Aprovada 2026-08-15 | `construction/review/primeiro-acesso-blocking-decisions-package.md` (alternativa escolhida registrada em `docs/governance/03-open-decisions.md`, fora do escopo desta leitura confirmar qual das M1/M2/M3) | Alto | Arquitetura/Segurança | Humano/projeto |
| DH-PA-02 | Domínio de e-mail determina Singular 1:1; domínio não cadastrado bloqueia Primeiro Acesso | Aprovada 2026-08-15 | `docs/domain/09-business-rules.md` BR-043/BR-044; `construction/review/primeiro-acesso-blocking-decisions-package.md` | Alto | Domínio | Humano/projeto |
| — | `specs/foundation/*` (minimal-ssot, development-workflow, path-conventions, agent-commands, DoR, DoD) — fluxo `DRAFT→READY_FOR_REVIEW→APPROVED→IMPLEMENTING→DONE` substitui cerimônia PKG/Session/Registry v4.1 | Approved/STABLE | `specs/foundation/*.md` | Alto | Processo/Governança | Humano/projeto |
| FT-FEDERACAO, FT-SINGULAR, FT-AREA, FT-EQUIPE, FT-COLABORADOR, FT-AUTH, FT-SESSION, FT-PRIMEIRO-ACESSO | Especificações de produto/domínio aprovadas para a camada organizacional e autenticação | APPROVED | `specs/features/<slug>/feature.yaml` | Alto | Produto/Domínio | Humano/projeto |
| FT-AREA-COLABORADOR | Tela de Área do Colaborador (somente leitura, sem roster de membros, sem contato setorial no MVP) | Mudou DRAFT→APPROVED nesta sessão (2026-08-20) | `git diff` em `specs/features/area-colaborador/feature.yaml` e `specification.md` | Médio | Produto | Humano/projeto |
| D1 (Plano W2) | GitHub mantido como plataforma de repositório (GitLab descartado) | Resolvida | `docs/governance/structural-simplification-plan-w2.md` | Médio | Infraestrutura | Humano/projeto |
| D2 (Plano W2) | `docs/architecture/11-target-repository-structure.md` obsoleto → arquivado | Confirmado e **executado** nesta sessão | Arquivo movido para `docs/governance/history/11-target-repository-structure.md` (visível no `git status`) | Médio | Governança documental | Humano/projeto |
| D3 (Plano W2) | `specs/domain/00-06` (modelo de Conteúdo) obsoleto → arquivado, responsabilidade no WordPress (DEC-CMS-001) | Confirmado e **executado** nesta sessão | Arquivos movidos para `docs/governance/history/` (visível no `git status`, staged) | Médio | Governança documental | Humano/projeto |

## Decisões propostas

| ID | Tema | Estado | Evidência | Impacto | Escopo | Ação necessária |
|---|---|---|---|---|---|---|
| DEC-013 | Estratégia de armazenamento de documentos (Filesystem / Oracle SecureFiles / Object Storage) | **Proposed**, sem data de aprovação | `docs/technology/04-decision-log.md:504-521` | Muito alto | Tecnologia/Dados | Decidir |
| DEC-014 | Estratégia de notificações | Proposed | `docs/technology/04-decision-log.md` | Médio | Tecnologia | Decidir |
| DEC-002/003/004 (governance) | Itens abertos no catálogo de decisões de governança | Aberta | `docs/governance/03-open-decisions.md` | Variável | Variável | Decidir |
| D4 (Plano W2) | Storage — recomendação (não decisão formal) por Object Storage S3-compatível (MinIO em dev) | **P0, aberta** — mesma questão que DEC-013 | `docs/governance/structural-simplification-plan-w2.md` §3 | Muito alto | Tecnologia/Dados | Decidir |
| D5 (Plano W2) | `docker-compose.yml` sobe Postgres, deveria ser Oracle | Reconhecida, não resolvida | `docs/governance/01-project-status.md:156`; `docs/audit/12-...:26,39`; `docs/governance/structural-simplification-plan-w2.md` | Alto | Infraestrutura | Decidir/reconciliar |
| D6 (Plano W2) | Ausência de CI backend validando contra Oracle real ("Opção A" adiada) | Não resolvida — CI atual roda "Opção B" (exclui testes de integração) | `.github/workflows/backend.yml` (cabeçalho cita "Opção B, D6, 2026-08-20") | Alto | Qualidade/CI | Decidir |
| D7 (Plano W2) | Unificação de `minimal-ssot.md` × `07-documentation-architecture.md` e dos catálogos de decisão (namespace de ID) | Sem bloqueio imediato, não resolvida | `docs/governance/structural-simplification-plan-w2.md` | Médio | Governança documental | Decidir (não urgente) |
| D8 (Plano W2) | Timing do arquivamento físico de `construction/` | Gated por fechamento de features em andamento | `docs/governance/structural-simplification-plan-w2.md` | Baixo | Governança documental | Aguardar marco |
| D9 (Plano W2) | Mapeamento Jira ↔ `feature.yaml` | Adiado | `docs/governance/structural-simplification-plan-w2.md` | Baixo | Processo | Adiar |
| GAP-DEC-009 | Forma exata do contrato de Home dinâmica (DEC-FA-004 aprova o princípio, não o formato de resposta) | Proposta em especificação, contrato não fechado | `specs/features/home/specification.md` (DRAFT) | Médio | Arquitetura/Produto | Decidir |
| GAP-DEC-010 | Escopo técnico da integração CMS WordPress para notícias | "Ativo — escopo a detalhar" | `docs/solution-design/06-integration-contracts.md`; `specs/features/noticia/specification.md` (DRAFT) | Médio | Integração | Decidir |

## Decisões implícitas

| Tema | Evidência | Observação |
|---|---|---|
| `hibernate.ddl-auto=none` em todos os profiles; nenhuma dependência Flyway/Liquibase no `pom.xml` | `backend/src/main/resources/application.yaml:71`; `database/migrations/README.md` | Confirma governança "brownfield DBA-owned" — decisão implícita consistente com DEC-006, mas nunca formalizada como ADR próprio |
| JWT implementado manualmente (HMAC-SHA256, sem JJWT/Nimbus) | `backend/.../accesscontrol/application/service/JwtTokenService.java` | Escolha de não usar lib de mercado — decisão de implementação, não eleva a arquitetura |
| Sessão via cookie HttpOnly + CSRF habilitado; política ativa anti-localStorage no frontend | `application.yaml` (`csrf-enabled: true`); `frontend/src/auth/storage-policy.ts` (RN-AUTH-007) | Reforça DEC-001 (stateless), mas o mecanismo concreto (cookie+CSRF) não tem ADR dedicado |
| Zimbra acessado via IMAP/SMTP/SOAP (proxy de credenciais), não OAuth/OIDC | `application.yaml` bloco `zimbra:`; `.env` | Coerente com DA-AUTH-012 citado em DEC-001, mecanismo implementado como documentado |
| `ARQUIVO_BINARIO.URL_ARQUIVO VARCHAR2(2000)`, sem coluna BLOB | `database/ddl/003-create-tables.sql:287-299` | Decide implicitamente "binário fora do Oracle", mas não decide o provedor — antecede e é consistente com o problema em aberto DEC-013 |
| Tabelas `CARGO` e `VINCULO_ORGANIZACIONAL` não existem fisicamente no schema | `database/model/05-decisions-and-risks.md`; ausência confirmada em `database/ddl/003-create-tables.sql` | Gap entre decisão documentada (TO-BE, DEC-DB-027/DH-CARGO-01) e schema físico instalado |
| `docker-compose.yml` não modela nenhum serviço/rede WordPress | `docker-compose.yml` (serviços: `database`, `backend`, `frontend`) | A fronteira Portal×WordPress (DEC-CMS-001) não está representada na infraestrutura local — sugere integração tratada como sistema externo |
| `OracleLegacyDialect` escolhido por compatibilidade com Oracle 11g (produção/TST), não com o container `oracle-free:23-slim` de dev | `application.yaml` comentário; `.env` aponta para `ractst-scan.unimedce.com.br` | Revela que o time desenvolve contra Oracle corporativo real (TST), com o compose como caminho alternativo, não primário |
| Módulo de Documento (`DOCUMENTO`/`ARQUIVO_BINARIO`/`DOCUMENTO_VERSAO`) tem schema pronto mas nenhuma classe Java implementada | Busca em `backend/src/main/java` sem resultado para `Documento`/`ArquivoBinario` | Schema à frente do código — reforça que FT-DOCUMENTO ainda não tem base de implementação |

## Decisões conflitantes

| # | Fonte A | Fonte B | Conflito | Fonte mais recente/vigente | Impacto | Decisão humana? |
|---|---|---|---|---|---|---|
| 1 | `docker-compose.yml` (serviço `database: postgres:16-alpine`, hoje `postgres` ainda listado no `git status` como modificado) | `docs/technology/01-technology-stack.md` DEC-001 (Oracle aprovado) | Infraestrutura local desalinhada da stack tecnológica aprovada | DEC-001 (tech) — Oracle é a decisão vigente | Alto | Sim — ver D5 |
| 2 | `docs/governance/03-open-decisions.md` | `docs/architecture/08-decision-records.md` + `docs/technology/04-decision-log.md` | Namespaces de ID `DEC-XXX` colidem entre os três catálogos (ex.: `DEC-008`/`DEC-009` usados com sentidos diferentes) | Nenhuma — coexistem, problema reconhecido não bloqueante em `docs/governance/reconciliation-report.md:269` | Médio | Sim — ver D7 |
| 3 | `docs/solution-design/11-platform-decomposition.md` (Next.js, PostgreSQL, Redis) | `docs/technology/01-technology-stack.md` (mesmas tecnologias listadas como "Explicitamente Não Utilizadas", DEC-011 Rejected) + `docs/solution-design/01-solution-overview.md` | Documento não arquivado descreve stack rejeitada; não consta no índice oficial `docs/solution-design/00-solution-design-index.md`; **não foi capturado pela auditoria W0/W1 nem pelo Plano W2** | `01-technology-stack.md`/DEC-011 | Alto | Sim — achado novo, recomendo tratamento igual ao já dado a `11-target-repository-structure.md` |
| 4 | `specs/features/singular/api.md` (`codigoUnimed` como String) | Implementação JPA/API (`codigoUnimed` como Integer/`NUMBER(3)`) | Tipo de dado divergente entre especificação e código | Não determinado — `PD-04`, `PENDING_DECISION` | Médio | Sim |
| 5 | JPA `SINGULAR.numRegistroAns` (`NOT NULL`) | Oracle live (`nullable=true`) | Constraint divergente entre mapeamento e schema real | Não determinado — `RV-01`, pendente confirmação do DBA | Baixo/Médio | Sim (técnica, mas requer confirmação do DBA) |
| 6 | Modelo TO-BE aprovado (DH-03/DEC-DB-028: COLABORADOR só é criado após vínculo completo) | `AuthenticationService.finalizeLogin` ainda executa `locateOrCreate`, criando COLABORADOR incompleto com apenas Federação | Implementação não migrada para a decisão já aprovada | DH-03/DEC-DB-028 (decisão já vigente) | Alto | Não — é dívida de implementação de uma decisão já tomada, delegável |

## Decisões históricas

| Tema | O que mudou | Motivo/decisão sucessora | Evidência |
|---|---|---|---|
| Modelo de Conteúdo como Aggregate Root (`specs/domain/00-06`) | Arquivado, responsabilidade movida para o WordPress | DEC-CMS-001 (Aprovada) + D3 (Plano W2) | `docs/governance/history/00-domain-overview.md` etc. |
| Estrutura-alvo de repositório com Next.js/PostgreSQL/Redis (`docs/architecture/11-target-repository-structure.md`) | Arquivado — contradiz `docs/technology/01-technology-stack.md` | D2 (Plano W2) | `docs/governance/history/11-target-repository-structure.md` |
| RN-SESSION-003 (seleção de Contexto Ativo entre N vínculos) | Superseded — modelo mudou para 1 vínculo cadastral | DH-02 (2026-08-14) | `specs/features/session/specification.md` |
| RF-PA-003/004/007 e estados `LoadingContexts`/`SelectingContext`/`PersistingContext`/`ChangingContext` | Superseded, ligados ao mesmo modelo N-vínculos | DH-02 | `specs/features/primeiro-acesso/specification.md` |
| DEC-DB-027 (CARGO obrigatório na criação de COLABORADOR) | Parcialmente superseded — CARGO passa a ser opcional | DH-CARGO-01 (2026-08-17) | `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md` |
| PostgreSQL como fundação inicial do projeto | Revertido para Oracle após verificação do ambiente corporativo | DEC-001 (tech, 2026-06-22) | `docs/technology/04-decision-log.md` (contexto da própria DEC-001) |

*Nota de governança: em todos os casos acima, o texto histórico foi preservado nos artefatos vigentes (não removido), com rastro explícito de supersessão — boa prática já em uso no projeto, não é conflito não resolvido.*

---

## Lacunas de decisão

| ID (não oficial) | Pergunta a responder | Por que importa | O que fica bloqueado | Dependências |
|---|---|---|---|---|
| GAP-DEC-001 | Qual o provedor concreto de storage de arquivos: Filesystem, Oracle SecureFiles ou Object Storage? | Sem isso, `DOCUMENTO`/`ARQUIVO_BINARIO`/`DOCUMENTO_VERSAO` não podem ser implementados no backend | FT-DOCUMENTO (feature de maior risco do lote atual) inteira | DEC-013, D4, ADR-004 (fronteira lógica já aceita, mas sem tecnologia) |
| GAP-DEC-002 | `docker-compose.yml` deve subir Oracle (qual imagem/estratégia) e alinhar-se a qual ambiente de referência (Oracle Free local vs. TST corporativo)? | Ambiente de desenvolvimento local hoje diverge do banco realmente usado pelo time (`.env` aponta para TST real) | Paridade dev/prod, onboarding de novos devs | D5 |
| GAP-DEC-003 | CI backend deve testar contra Oracle real ("Opção A") ou manter exclusão de testes de integração ("Opção B", vigente)? | Testes de integração/aceitação (Auth, Colaborador, Equipe, Área, Singular, Federação, cross-feature) hoje só rodam localmente/manualmente | Confiabilidade do gate de PR | D6 |
| GAP-DEC-004 | `docs/solution-design/11-platform-decomposition.md` deve ser arquivado como obsoleto (mesmo tratamento de `11-target-repository-structure.md`)? | Conteúdo conflita diretamente com a stack aprovada e não está sinalizado como Archive; achado não capturado pela auditoria W0/W1 | Integridade do SSOT de solution-design | Nenhuma — ação isolada |
| GAP-DEC-005 | `codigoUnimed` é String ou Integer/NUMBER(3)? | Divergência spec × implementação já em produção de código | Consistência de contrato de API para FT-SINGULAR | PD-04 |
| GAP-DEC-006 | Como unificar os três catálogos de decisão (`governance/03`, `architecture/08`, `technology/04`) sob um namespace único? | Colisão de IDs já reconhecida, mas não bloqueante hoje | Rastreabilidade de longo prazo | D7 |
| GAP-DEC-007 | Quando arquivar fisicamente `construction/`? | Framework v4.1 não é fluxo diário, mas ainda referenciado por features em andamento | Limpeza documental | D8, fechamento de features ativas |
| GAP-DEC-008 | `SINGULAR.NUM_REGISTRO_ANS` deve ser NOT NULL (JPA) ou nullable (Oracle live)? | Constraint mismatch identificado em validação runtime | Consistência schema × JPA para FT-SINGULAR | RV-01, confirmação do DBA |
| GAP-DEC-009 | Qual o formato exato do contrato de resposta da Home dinâmica? | DEC-FA-004 aprova o princípio (backend resolve, frontend só renderiza) mas não a forma | FT-HOME (DRAFT) | DEC-FA-004 |
| GAP-DEC-010 | Qual o escopo técnico da integração com o CMS WordPress para notícias? | Classificado como "ativo — escopo a detalhar" há tempo | FT-NOTICIA (DRAFT) | DEC-CMS-001 |
| GAP-DEC-011 | `GET /api/v1/auth/me` deve expor `cargo`/`ramal`/outros contatos do colaborador? | Dado necessário para a tela de Perfil no Figma, hoje ausente do contrato | FT-PERFIL (DRAFT) | FT-COLABORADOR (contrato de resposta) |
| GAP-DEC-012 | FT-SERVICOS: lista estática ou administrável? Link externo simples ou SSO? | Única tela do lote sem precedente no frontend legado | FT-SERVICOS (DRAFT) | Nenhuma decisão anterior aplicável |
| GAP-DEC-013 (segurança, fora do eixo arquitetural) | O `.env` da raiz, aparentemente versionado no git com credenciais reais (senha Oracle `UNMPORTCOM_APP`, segredo JWT, host Zimbra corporativo), deve ser removido do histórico e os segredos rotacionados? | Exposição de credenciais reais de ambiente corporativo (TST) | Segurança operacional — não bloqueia arquitetura, mas é urgente | Nenhuma — ação de segurança isolada, fora do escopo de "decisão arquitetural" |

---

## Decisões que exigem decisão humana

### 1. Storage de arquivos — provedor concreto (DEC-013 / D4 / GAP-DEC-001)
**Contexto:** ADR-004 já decidiu a fronteira lógica (metadados no Oracle, binário em container de armazenamento separado). O schema físico (`ARQUIVO_BINARIO.URL_ARQUIVO`) já pressupõe binário fora do banco. O que falta é escolher o mecanismo concreto.
**Alternativas encontradas nos documentos:** Filesystem simples; Oracle SecureFiles (LOB dentro do banco); Object Storage S3-compatível (self-hosted MinIO em dev, ou provedor corporativo em produção).
**Trade-offs já levantados no Plano W2:** volume Docker/filesystem externo é mais simples mas menos escalável e sem versionamento nativo; SecureFiles mantém tudo no Oracle mas acopla armazenamento binário ao mesmo banco transacional; Object Storage desacopla, escala melhor e tem retenção/versionamento nativo, mas introduz um novo componente de infraestrutura e questões de LGPD/retenção a resolver.
**Consequência de cada alternativa:** Filesystem/SecureFiles simplificam a operação inicial mas podem exigir migração futura se o volume crescer; Object Storage exige decisão adicional sobre self-hosted vs. corporativo antes de poder ser implementado.
**Recomendação técnica registrada no próprio projeto (não é aprovação):** o Plano W2 já registra uma inclinação por Object Storage S3-compatível, mas classifica isso explicitamente como recomendação, não decisão.
**O que permanece como julgamento humano:** escolha final do provedor, política de retenção/backup, e se MinIO self-hosted é aceitável para dados corporativos ou se é necessário um provedor já homologado pela Unimed Ceará.

### 2. `docker-compose.yml` — alinhamento a Oracle (D5 / GAP-DEC-002)
**Contexto:** o compose local sobe Postgres; o time de fato desenvolve contra Oracle real (TST), conforme `.env`.
**Alternativas:** (a) trocar a imagem do serviço `database` para `gvenzl/oracle-free` e usar schema local instalado via DDL do zero; (b) manter o compose como estava e formalizar que o ambiente de referência é o Oracle corporativo (TST) via VPN/rede, sem banco local containerizado.
**Trade-offs:** (a) dá isolamento e reprodutibilidade, mas exige manter dois caminhos de schema sincronizados (baseline DDL local vs. TST); (b) reflete o uso real atual, mas reduz a portabilidade do ambiente de desenvolvimento e mantém dependência de acesso à rede corporativa.
**O que permanece como julgamento humano:** qual caminho é a política oficial de ambiente de desenvolvimento local.

### 3. CI backend contra Oracle real (D6 / GAP-DEC-003)
**Contexto:** o CI atual ("Opção B", 2026-08-20) exclui explicitamente 11 testes de integração/aceitação por dependerem de Oracle real; "Opção A" (provisionar Oracle no CI) foi discutida e adiada.
**Trade-offs:** Opção A aumenta tempo/custo de pipeline e exige uma imagem Oracle no CI (licenciamento/tamanho); Opção B mantém o pipeline rápido mas deixa toda a camada de integração sem cobertura automatizada em PR.
**O que permanece como julgamento humano:** aceitar o risco de qualidade da Opção B por mais tempo, ou investir no provisionamento de Oracle no CI.

### 4. `docs/solution-design/11-platform-decomposition.md` (GAP-DEC-004)
**Contexto:** achado desta varredura, não capturado pela auditoria W0/W1 nem pelo Plano W2. Descreve Next.js/PostgreSQL/Redis, tecnologias explicitamente rejeitadas (DEC-011), e não está marcado como Archive nem consta no índice oficial de solution-design.
**Recomendação técnica:** aplicar o mesmo tratamento já dado a `docs/architecture/11-target-repository-structure.md` (mover para `docs/governance/history/` com cabeçalho Archive/Obsoleto citando DEC-011).
**O que permanece como julgamento humano:** confirmar a reclassificação (é uma ação de governança documental, não uma decisão de arquitetura nova).

### 5. `.env` da raiz com credenciais reais versionadas (GAP-DEC-013)
**Contexto:** achado de segurança, fora do eixo de decisões arquiteturais, mas com impacto potencialmente alto (senha Oracle de ambiente TST corporativo, segredo JWT e host Zimbra corporativo aparentam estar em texto plano em um arquivo rastreado pelo git).
**O que permanece como julgamento humano:** confirmar se o arquivo está de fato commitado (não apenas presente no working tree) e decidir sobre remoção do histórico e rotação de segredos — ação urgente e fora do escopo de arquitetura, mas registrada aqui por relevância.

---

## Decisões delegáveis

- Corrigir `AuthenticationService.finalizeLogin` para não mais executar `locateOrCreate` de forma incompleta, alinhando-se ao modelo já aprovado (DH-03/DEC-DB-028) — decisão já tomada, é dívida de implementação.
- Ajustar `docker-compose.yml` para a imagem Oracle **uma vez confirmado** o caminho de referência (item 2 acima) — mecânico depois da decisão humana.
- Alinhar nomenclatura do seed `PAPEL.GESTOR_DOCUMENTAL` para o padrão TO-BE `ADMIN_*` — drift de nomenclatura já identificado em `construction/review/cargo-vinculo-reconciliation-pd-cargo-01-02-03.md`, não reabre modelo.
- Confirmar com o DBA a nullability real de `SINGULAR.NUM_REGISTRO_ANS` e ajustar o mapeamento JPA de acordo (RV-01) — constatação técnica.
- Resolver `PD-04` (`codigoUnimed` String vs. Integer) — é uma correção de consistência spec↔código, não uma nova decisão de arquitetura (a cardinalidade e o domínio de negócio não mudam).
- Reclassificar `docs/solution-design/11-platform-decomposition.md` como Archive, seguindo o padrão já aplicado a `11-target-repository-structure.md` (mecânico, uma vez confirmado pelo item 4 acima).

---

## Priorização

| Prioridade | Itens |
|---|---|
| **P0 — bloqueia arquitetura/implementação** | GAP-DEC-001 / DEC-013 / D4 (storage de arquivos — bloqueia FT-DOCUMENTO por completo) |
| **P1 — decidir antes da próxima fase relevante** | GAP-DEC-002/D5 (docker-compose × Oracle); GAP-DEC-003/D6 (CI backend × Oracle); GAP-DEC-004 (arquivar `11-platform-decomposition.md`); GAP-DEC-005/PD-04 (`codigoUnimed`); GAP-DEC-009 (contrato de Home); conflito #6 (locateOrCreate × DH-03, ainda que delegável na execução, deve ser priorizado por já ter decisão aprovada pendente de aplicação) |
| **P2 — pode aguardar** | GAP-DEC-006/D7 (unificação de catálogos); GAP-DEC-007/D8 (timing arquivamento `construction/`); GAP-DEC-008/RV-01 (nullability); GAP-DEC-010 (escopo CMS notícia); GAP-DEC-011 (dados de perfil); GAP-DEC-012 (FT-SERVICOS) |
| **P3 — detalhe delegável** | D9 (mapeamento Jira); nomenclatura `GESTOR_DOCUMENTAL`→`ADMIN_*` |

*(GAP-DEC-013, o achado de segurança do `.env`, não recebeu P0–P3 por estar fora do eixo arquitetural desta priorização — é sinalizado separadamente como urgente na seção anterior.)*

---

## Dependências entre decisões

- **DEC-013/D4 (storage)** é pré-requisito direto de **FT-DOCUMENTO** e, por consequência, de qualquer feature futura de compartilhamento/permissão documental (`BR-019`/`BR-020`, hoje em aberto como `OQ-011`/`OQ-013`).
- **GAP-DEC-002 (docker-compose)** depende implicitamente da decisão sobre qual Oracle é "ambiente de referência" — está ligada à mesma questão de paridade que aparece em `.env` (TST real) vs. container local.
- **GAP-DEC-004 (`11-platform-decomposition.md`)** depende apenas de confirmação — não tem dependência técnica, mas deveria ser resolvida junto com o mesmo ciclo de limpeza documental que já tratou `11-target-repository-structure.md` (D2) e `specs/domain/` (D3).
- **DH-PA-01 (credencial temporária)** já aprovada, mas sua implementação (LA-01 no pacote de decisões bloqueantes) depende da remoção de `locateOrCreate` do fluxo de login — mesma dependência do conflito #6.
- **GAP-DEC-009 (contrato de Home)** depende indiretamente de FT-COLABORADOR/FT-AUTH (fonte dos dados de contexto que a Home deve agregar).
- **GAP-DEC-011 (dados de perfil)** depende de decisão sobre expandir o contrato de `ColaboradorResponse`/`GET /auth/me`, que por sua vez tangencia GAP-DEC-005 (consistência de tipos do mesmo bounded context organizacional).
- **D7 (unificação de catálogos)** não bloqueia nada tecnicamente, mas toda nova decisão registrada em qualquer um dos três catálogos aumenta o custo de uma eventual unificação futura — quanto mais tarde for feita, maior o esforço de reconciliação.

## Impacto no roadmap

- A ausência de decisão sobre **storage de arquivos (P0)** é o único item deste inventário que bloqueia integralmente uma feature já mapeada no roadmap (FT-DOCUMENTO) — sem ela, o lote de 5 telas novas (Figma, 2026-08-20) não pode fechar DoR-Implementation para essa feature específica, embora as outras quatro (`home`, `noticia`, `perfil`, `servicos`) tenham lacunas menores e mais localizadas.
- Os itens **P1** (docker-compose, CI Oracle, `codigoUnimed`, contrato de Home) não bloqueiam trabalho em andamento, mas cada um representa risco crescente quanto mais features novas forem construídas sobre a base atual sem correção — especialmente o desalinhamento CI×Oracle, que deixa toda nova feature backend sem cobertura de integração automatizada em PR.
- O conflito **locateOrCreate × DH-03** é uma decisão já aprovada mas não implementada — é dívida técnica que deveria ser resolvida antes de qualquer novo trabalho sobre Primeiro Acesso, pois a spec já assume o comportamento TO-BE em seu texto normativo vigente.
- Os itens **P2/P3** não têm impacto de curto prazo sobre o roadmap; são convenientes de resolver, mas não bloqueiam nenhuma feature específica hoje.
- O achado de segurança (`.env`) não é um item de roadmap, mas seu risco cresce com o tempo enquanto não for tratado — não é um bloqueio de arquitetura, é uma exposição operacional.

## Conclusão

O Portal de Comunicação tem uma camada de governança de processo (`specs/foundation/`) e um conjunto substancial de decisões de domínio, autenticação e banco de dados **formalmente aprovadas e mutuamente consistentes** — a maior parte das decisões revisadas neste inventário não está em conflito, e onde houve mudança de modelo (ex.: N-vínculos → 1-vínculo, CARGO obrigatório → opcional), o projeto preservou o rastro histórico de forma exemplar, sem ambiguidade remanescente.

O ponto de maior risco arquitetural aberto é único e conhecido pelo próprio projeto: a **decisão de storage de arquivos (DEC-013)**, que bloqueia por completo a implementação de FT-DOCUMENTO. Ao seu redor, há um conjunto de desalinhamentos entre documentação/decisão e infraestrutura/implementação (docker-compose × Oracle, CI × Oracle, `locateOrCreate` × DH-03, `codigoUnimed` String × Integer) que são conhecidos, delegáveis ou de baixo esforço de reconciliação — não representam decisões de arquitetura em aberto, mas dívida de execução sobre decisões já tomadas.

O achado mais relevante desta varredura que **não constava em nenhum inventário anterior do projeto** é `docs/solution-design/11-platform-decomposition.md` — um documento não marcado como obsoleto que descreve uma stack tecnológica já formalmente rejeitada, seguindo exatamente o mesmo padrão de risco que a auditoria W0/W1 já havia identificado e corrigido para `docs/architecture/11-target-repository-structure.md`. Recomenda-se tratá-lo com a mesma governança na próxima rodada de simplificação documental.

Por fim, um achado de segurança fora do eixo arquitetural (possível versionamento de `.env` com credenciais reais) é sinalizado por relevância, mas não faz parte da priorização P0–P3 de decisões arquiteturais deste inventário — requer atenção humana independente e, se confirmado, é urgente.

Nenhuma decisão foi tomada, aprovada, revogada ou alterada por este documento. As duas ações de arquivamento documental (D2 e D3) mencionadas como "já executadas" foram decisões tomadas **antes** desta etapa (Plano W2, mesma sessão) e apenas registradas aqui como contexto — não foram realizadas por este inventário.
