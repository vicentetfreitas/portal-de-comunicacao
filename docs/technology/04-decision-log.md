# 04-decision-log.md

# Decision Log

## Objetivo

Este documento registra as decisões arquiteturais, tecnológicas e estratégicas do Portal de Comunicação Digital (PCD).

Seu propósito é:

* Preservar o histórico de decisões
* Evitar retrabalho
* Facilitar auditorias
* Apoiar novos integrantes da equipe
* Justificar escolhas técnicas
* Reduzir divergências arquiteturais

---

# Processo de Decisão

Toda decisão relevante deve possuir:

| Campo         | Obrigatório |
| ------------- | ----------- |
| ID            | Sim         |
| Título        | Sim         |
| Data          | Sim         |
| Status        | Sim         |
| Contexto      | Sim         |
| Decisão       | Sim         |
| Consequências | Sim         |

---

# Status Possíveis

| Status     | Descrição         |
| ---------- | ----------------- |
| Proposed   | Em avaliação      |
| Approved   | Aprovada          |
| Rejected   | Rejeitada         |
| Superseded | Substituída       |
| Deprecated | Em descontinuação |

---

# DEC-001 — Banco de Dados Oficial

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-001    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Tecnologia |

---

## Contexto

A fundação inicial do projeto foi construída utilizando PostgreSQL como banco de dados padrão.

Durante a revisão tecnológica verificou-se que o ambiente corporativo utiliza Oracle Database como plataforma oficial.

---

## Decisão

O banco de dados oficial do projeto será:

```text
Oracle Database 23ai
```

Banco local:

```text
Oracle XE
```

---

## Consequências

### Positivas

* Alinhamento com ambiente corporativo
* Redução de riscos de implantação
* Compatibilidade com infraestrutura existente

### Negativas

* Ajustes nas migrations já existentes
* Necessidade de driver Oracle

---

# DEC-002 — Linguagem Backend

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-002    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Backend    |

---

## Contexto

O sistema necessita de uma plataforma moderna, robusta e aderente ao ecossistema corporativo.

---

## Decisão

A linguagem oficial será:

```text
Java 25
```

---

## Consequências

### Positivas

* Plataforma moderna
* Melhor desempenho
* Evolução contínua do ecossistema Java

### Negativas

* Necessidade de atualização dos ambientes de desenvolvimento

---

# DEC-003 — Framework Backend

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-003    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Backend    |

---

## Contexto

O projeto necessita de produtividade, segurança e integração com o ecossistema Java.

---

## Decisão

Framework oficial:

```text
Spring Boot 4.x
```

---

## Consequências

### Positivas

* Ecossistema consolidado
* Forte suporte corporativo
* Integração com Spring Security
* Integração com Spring Data

---

# DEC-004 — Framework Frontend

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-004    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Frontend   |

---

## Contexto

O portal necessita de uma interface moderna, produtiva e de fácil manutenção.

---

## Decisão

Frontend baseado em:

```text
Vue 3
Quasar Framework
TypeScript
```

---

## Alternativas Avaliadas

* Angular
* React
* Next.js

---

## Consequências

### Positivas

* Alta produtividade
* Curva de aprendizado reduzida
* Excelente biblioteca de componentes

---

# DEC-005 — Arquitetura de Software

## Informações

| Campo     | Valor       |
| --------- | ----------- |
| ID        | DEC-005     |
| Data      | 2026-06-22  |
| Status    | Approved    |
| Categoria | Arquitetura |

---

## Contexto

O sistema deverá evoluir por múltiplos módulos e equipes.

---

## Decisão

A arquitetura oficial será baseada em:

```text
DDD
Clean Architecture
Hexagonal Architecture
```

---

## Consequências

### Positivas

* Alta manutenibilidade
* Baixo acoplamento
* Escalabilidade organizacional

---

# DEC-006 — Estratégia de Banco

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-006    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Dados      |

---

## Decisão

Toda alteração estrutural será realizada exclusivamente através de scripts DDL versionados, executados pelo DBA (`database/ddl/` e `database/migrations/`).

> **Atualização DEC-DB-019 (2026-07-10):** Flyway não é utilizado. Ver `database/model/05-decisions-and-risks.md`.

## Proibido

```text
ALTER TABLE manual
CREATE TABLE manual
DROP TABLE manual
```

em ambientes controlados.

---

# DEC-007 — Estratégia de APIs

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-007    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Integração |

---

## Decisão

Padrão de APIs:

```text
REST
```

Versionamento:

```text
/api/v1
```

Documentação:

```text
OpenAPI 3
Swagger
```

---

# DEC-008 — Segurança

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-008    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Segurança  |

---

## Decisão

Autenticação:

```text
JWT
```

Autorização:

```text
RBAC
```

Framework:

```text
Spring Security
```

---

# DEC-009 — Estratégia de Containers

## Informações

| Campo     | Valor          |
| --------- | -------------- |
| ID        | DEC-009        |
| Data      | 2026-06-22     |
| Status    | Approved       |
| Categoria | Infraestrutura |

---

## Decisão

Containerização oficial:

```text
Docker
```

Orquestração local:

```text
Docker Compose
```

---

## Consequências

### Positivas

* Reprodutibilidade
* Padronização dos ambientes
* Simplificação do onboarding

---

# DEC-010 — Monorepo

## Informações

| Campo     | Valor       |
| --------- | ----------- |
| ID        | DEC-010     |
| Data      | 2026-06-22  |
| Status    | Superseded (ver DEC-015) |
| Categoria | Repositório |

---

## Decisão

O projeto será mantido em um único repositório contendo:

```text
backend/
frontend/
docs/
```

---

## Relação com outras decisões

- **Superseded por DEC-015 (Separação em Repositórios Independentes, Approved, 2026-08-26)** — o projeto passa de monorepo para três repositórios independentes (backend, frontend, CMS), cada um com versionamento e containerização próprios. Este registro é preservado como histórico; não reflete mais a estrutura vigente.

---

# DEC-011 — Tecnologias Explicitamente Rejeitadas

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-011    |
| Data      | 2026-06-22 |
| Status    | Rejected   |
| Categoria | Tecnologia |

---

## Decisão

As tecnologias abaixo não fazem parte do MVP:

```text
PostgreSQL
MongoDB
MySQL
Redis
Kafka
RabbitMQ
Angular
NestJS
Node.js Backend
```

---

## Motivo

Redução de complexidade e alinhamento ao escopo inicial.

---

# DEC-012 — Estratégia de Observabilidade

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-012    |
| Data      | 2026-06-22 |
| Status    | Approved   |
| Categoria | Operação   |

---

## Decisão

Stack oficial:

```text
Micrometer
Prometheus
Grafana
Spring Boot Actuator
```

---

# DEC-013 — Estratégia de Armazenamento de Documentos

## Informações

| Campo     | Valor      |
| --------- | ---------- |
| ID        | DEC-013    |
| Data      | 2026-08-20 |
| Status    | Approved   |
| Categoria | Tecnologia |

---

## Contexto

ADR-004 (`docs/architecture/08-decision-records.md`) já decide a fronteira lógica do domínio de Gestão Documental: metadados residem no Banco de Dados (Oracle) e binários residem em um container lógico distinto, "Armazenamento de Arquivos", coordenado pelo componente Gestão de Armazenamento na API Backend. Essa fronteira é reafirmada de forma consistente em `docs/solution-design/07-data-ownership.md` e `docs/solution-design/06-integration-contracts.md`, e é compatível com o schema físico já instalado (`ARQUIVO_BINARIO.URL_ARQUIVO`, que referencia o binário por URL em vez de persisti-lo como BLOB/CLOB no Oracle).

O que permanecia em aberto era exclusivamente o **mecanismo tecnológico concreto** desse "Armazenamento de Arquivos" — esta entrada esteve registrada como Proposed, com três alternativas candidatas: Filesystem, Oracle SecureFiles e Object Storage. `specs/features/arquivos/specification.md` (FT-DOCUMENTO) identifica essa lacuna como bloqueante para a implementação da feature de Arquivos e Documentos.

---

## Decisão

O Portal utilizará:

```text
Object Storage compatível com S3
```

para o armazenamento de arquivos operacionais/privados (binários documentais).

Esta decisão formaliza apenas a **estratégia tecnológica** (protocolo/paradigma de armazenamento). Não escolhe fornecedor, produto, infraestrutura de produção, bucket, região, política de retenção, criptografia específica, mecanismo de URL assinada, estratégia de upload, nem limites funcionais de arquivo — essas permanecem decisões técnicas/operacionais posteriores, a serem registradas separadamente quando necessárias.

---

## Alternativas Consideradas

| Alternativa | Motivo de rejeição |
| ----------- | ------------------- |
| Filesystem | Não escalável horizontalmente entre réplicas; sem versionamento/replicação nativos; amarra o binário ao host do container, dificultando portabilidade entre ambientes. |
| Oracle SecureFiles | Acopla o armazenamento binário ao mesmo banco transacional já usado para metadados, contrariando a separação de responsabilidades já decidida em ADR-004 e aumentando volume/risco operacional do banco de produção. |

Não é escolhido, nesta decisão, um fornecedor ou produto específico de Object Storage — essa é uma decisão posterior e distinta.

---

## Relação com outras decisões

- **ADR-004 (Separação entre Metadados e Binários, Aceita)** — esta decisão não altera nem substitui ADR-004; apenas preenche a lacuna que ADR-004 deixou em aberto (o "como" do container lógico "Armazenamento de Arquivos"). A separação metadado (Oracle) × binário (Object Storage) permanece exatamente como ADR-004 já definia.
- **Ownership documental (`docs/solution-design/07-data-ownership.md`)** — o owner lógico dos binários documentais continua sendo Gestão Documental, via o componente Armazenamento de Arquivos; esta decisão apenas concretiza a tecnologia por trás desse owner, sem alterar a atribuição de responsabilidade.
- **Contratos de integração (`docs/solution-design/06-integration-contracts.md`)** — o contrato Backend → Armazenamento de Arquivos já previa acesso "síncrono — protocolo de objetos/arquivos"; Object Storage S3-compatível é consistente com esse protocolo já documentado.

---

## Consequências

### Positivas

* Desbloqueia a modelagem e implementação de FT-DOCUMENTO (Arquivos e Documentos), hoje impedida por esta lacuna.
* Mantém a separação metadado/binário já decidida em ADR-004, sem acoplar armazenamento binário ao Oracle transacional.
* Preserva portabilidade entre provedores (protocolo S3 é amplamente suportado, self-hosted ou gerenciado).

### Negativas

* Introduz um novo componente lógico de infraestrutura ainda não provisionado em nenhum ambiente.
* Fornecedor/produto concreto, política de retenção, criptografia e estratégia de upload permanecem indefinidos e são pré-requisitos para a implementação real de FT-DOCUMENTO.

---

# DEC-015 — Separação em Repositórios Independentes

## Informações

| Campo     | Valor       |
| --------- | ----------- |
| ID        | DEC-015     |
| Data      | 2026-08-26  |
| Status    | Approved    |
| Categoria | Repositório |

---

## Contexto

DEC-010 (2026-06-22, Approved) definiu monorepo único (`backend/`, `frontend/`, `docs/`). Nesse momento o projeto continha apenas o backend Java/Spring Boot e o frontend Vue 3/Quasar, sem componente de CMS.

O projeto passa a exigir que backend, frontend e CMS (WordPress — já definido como stack em `docs/technology/01-technology-stack.md` e com fronteira lógica isolada por DEC-CMS-001 em `docs/governance/03-open-decisions.md`) evoluam, versionem e sejam containerizados de forma independente. O CMS ainda não existe como código neste repositório — não é uma extração de código existente, é um componente novo.

---

## Decisão

O projeto passa a ser mantido em **três repositórios independentes**, cada um com:

- versionamento próprio (tags/releases);
- Dockerfile e imagem de container próprios;
- pipeline de CI próprio.

```text
1. Backend    — Java / Spring Boot
2. Frontend   — Vue 3 / Quasar
3. CMS        — WordPress
```

Esta decisão **substitui (supersede) DEC-010**.

---

## Escopo desta decisão

Formaliza apenas a estratégia diretiva: separação em repositórios independentes, com versionamento e containerização próprios por repositório.

**Não decide nesta entrada** (ver "Pontos em Aberto"): nomes/organização dos novos repositórios; onde passam a viver `specs/`, `docs/` e `construction/` (hoje SSOT compartilhada entre backend e frontend num único repositório); estratégia de orquestração local pós-divisão; se o histórico Git é preservado na separação; sequenciamento/timeline da migração; hospedagem e customização (tema/plugins) do CMS.

---

## Relação com outras decisões

- **Substitui DEC-010** (Monorepo, Approved 2026-06-22) — ver nota de superseded no próprio registro de DEC-010.
- **Não altera DEC-009** (Estratégia de Containers: Docker + Docker Compose) — cada repositório continua usando Docker; Docker Compose passa a operar por repositório (ambiente local de cada serviço), não mais um único `docker-compose.yml` cobrindo todos os serviços. A orquestração de um ambiente local integrado (os três serviços juntos) é um Ponto em Aberto desta decisão.
- **Compatível com DEC-CMS-001** (Fronteira do CMS, Approved 2026-07-24) — CMS já era tratado como exclusivamente provedor de conteúdo, sem participar da estrutura organizacional; esta decisão apenas dá a esse componente, já isolado logicamente, um repositório e ciclo de release próprios.
- Não altera as demais decisões tecnológicas (DEC-001 a DEC-008, DEC-011 a DEC-013).

---

## Plano de Migração (alto nível)

| Fase | Escopo |
|------|--------|
| 0 | Resolver os Pontos em Aberto abaixo (nomes de repositório, home de `specs/`/`docs/`/`construction/`, estratégia de orquestração local) |
| 1 | Backend → repositório próprio; adaptar `backend/Dockerfile` existente; pipeline CI (`mvn clean verify` + build/push de imagem); primeira tag |
| 2 | Frontend → repositório próprio; adaptar `frontend/Dockerfile` existente; pipeline CI (lint, typecheck, unit, E2E + build/push de imagem); primeira tag |
| 3 | CMS → novo repositório WordPress (imagem base + customizações, se houver); pipeline CI mínima |
| 4 | Orquestração local integrada (os três serviços juntos) via mecanismo definido na Fase 0 |
| 5 | Arquivar/tornar somente-leitura o monorepo atual |

Sequenciamento exato, prazos e responsáveis ficam para o planejamento de execução (fora do escopo deste registro de decisão).

---

## Pontos em Aberto

Bloqueiam a **execução** da migração — não bloqueiam o registro desta decisão diretiva:

1. Nomes e organização/conta dos três novos repositórios.
2. Onde vivem `specs/`, `docs/` e `construction/` após a divisão — repositório de governança próprio vs. um dos três repositórios como "primário" referenciado pelos demais.
3. ~~Estratégia de orquestração local pós-divisão para subir os três serviços juntos (compose "meta"/infra próprio, git submodules, ou scripts).~~ **RESOLVIDO (2026-08-26)** — decisão humana: nenhuma orquestração combinada. Cada repositório sobe sozinho (backend via `mvnw`, frontend via `yarn dev`, CMS quando existir); integração entre serviços é validada por contrato de API, não por ambiente local combinado. `docker-compose.yml` do monorepo original deixa de ser o mecanismo de referência pós-divisão completa.
4. Preservação (ou não) do histórico Git de `backend/` e `frontend/` na separação (ex.: `git subtree split` / `git filter-repo`) vs. repositórios novos a partir do zero.
5. CMS: hospedagem, tema/plugins customizados vs. WordPress stock.
6. Redesenho de `.github/workflows/` (hoje pensado para um único repositório) por repositório.

---

## Consequências

### Positivas

* Ciclos de release independentes por componente — backend, frontend e CMS podem versionar e publicar sem acoplamento.
* Imagens Docker menores e mais específicas por serviço; pipelines de CI mais rápidos e focados.
* Fronteira de responsabilidade mais clara entre os três componentes, alinhada à fronteira lógica já definida por DEC-CMS-001.
* CMS pode evoluir com ciclo e tooling próprios (WordPress), sem se acoplar ao monorepo Java/Vue.

### Negativas

* `specs/`, `docs/` e `construction/` são hoje SSOT compartilhada entre backend e frontend num único repositório; a divisão exige decidir onde essa fonte única de verdade passa a viver antes de executar a migração.
* Coordenação cross-repo (mudanças que tocam o contrato de API backend↔frontend simultaneamente) fica mais fricciosa; passa a exigir processo próprio (versionamento de contrato, changelog compartilhado).
* Orquestração local hoje é um único `docker-compose.yml` (database + backend + frontend); precisa de um novo mecanismo pós-divisão.
* Histórico de commits do monorepo não migra automaticamente para os três novos repositórios sem trabalho dedicado, caso sua preservação seja desejada.
* `.github/workflows/` atual precisa ser redesenhado por repositório.

---

## Progresso da Execução

| Data | Marco |
|------|-------|
| 2026-08-26 | Fase 2 (parcial) — snapshot inicial do `frontend/` extraído para repositório local próprio (`/home/projects/portal-de-comunicacao-frontend`, fora do monorepo), `git init` sem histórico preservado (Ponto em Aberto 4 resolvido: sem preservação), 1 commit. **Sem remote configurado** — repositório remoto ainda não criado, nada foi publicado fora da máquina local. `specs/`, `docs/` e `construction/` **não** foram duplicados — permanecem exclusivamente neste monorepo (resolve parcialmente o Ponto em Aberto 2: SSOT continua aqui). Ponto em Aberto 1 (nome/organização definitiva do repositório) **não decidido** — fica para quando o repositório remoto for criado. Pontos 3 (orquestração local), 5 (CMS) e 6 (CI por repositório) seguem em aberto. |
| 2026-08-26 | Fase 1 (parcial) — mesmo tratamento aplicado ao `backend/`: snapshot extraído para `/home/projects/portal-de-comunicacao-backend`, sem histórico, sem remote. `database/` (DDL/baseline Oracle, DEC-DB-019) confirmado **fora** do escopo de migração — é SSOT administrado pelo DBA, sem acoplamento de runtime com `backend/` (sem Flyway/Liquibase; schema não gerenciado pela aplicação), fica junto de `specs/`/`docs/`/`construction/` no monorepo. Gaps pré-existentes documentados no novo repositório: `docker-compose.yml` referencia `backend/Dockerfile`, que nunca existiu; nenhuma orquestração local própria (Ponto 3 segue em aberto). |

---

# Decisões Pendentes

## DEC-014

### Título

Estratégia de notificações.

### Status

Proposed

### Alternativas

```text
Email
Push
SMS
Microsoft Teams
```

---

# Histórico de Revisões

| Data       | Alteração                               |
| ---------- | --------------------------------------- |
| 2026-06-22 | Criação inicial do registro de decisões |
| 2026-08-26 | DEC-015 (Separação em Repositórios Independentes) registrada; supersede DEC-010 (Monorepo) |

---

# Governança

Toda alteração arquitetural relevante deverá:

1. Atualizar este documento.
2. Atualizar os artefatos impactados.
3. Atualizar auditorias relacionadas.
4. Registrar justificativa da mudança.

---

# Status

Documento ativo e obrigatório para governança técnica do Portal de Comunicação Digital.
