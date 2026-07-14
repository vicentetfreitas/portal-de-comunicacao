# Solution Overview — Portal de Comunicação

## Objetivo

Este documento apresenta a **visão executiva da solução alvo** do Portal de Comunicação da Unimed Ceará. Transforma a arquitetura aprovada (`10-target-architecture.md`) em uma descrição implementável da solução — componentes, princípios, ambientes, integrações, segurança e evolução — sem produzir código, classes, endpoints ou infraestrutura executável.

É o primeiro artefato substantivo da camada **Solution Design**, destinado a orientar os documentos subsequentes (`02` a `10`) e a futura camada de **Implementation**.

**Rastreabilidade:** `docs/architecture/08-decision-records.md`, `docs/architecture/09-risk-assessment.md`, `docs/architecture/10-target-architecture.md`, `docs/solution-design/00-solution-design-index.md`.

---

# Visão Executiva

## Objetivo do portal

O Portal de Comunicação é uma **aplicação web corporativa** que centraliza comunicação interna, gestão documental e governança de acesso para a Unimed Ceará — organização cooperativa de saúde com estrutura **federativa multi-singular**.

A solução permite que colaboradores autorizados consultem e publiquem documentos no contexto organizacional correto (singular, área, equipe ou pessoal), com rastreabilidade, confidencialidade e controle de acesso por papéis e escopo.

## Público-alvo

| Ator | Papel na solução |
| ---- | ---------------- |
| Colaborador | Usuário principal; consulta e publica documentos conforme permissões |
| Gestor | Gestão operacional de equipes, áreas e conteúdo departamental |
| Administrador | Estrutura organizacional, usuários, políticas e auditoria |
| Parceiro Autorizado | Acesso externo restrito conforme política institucional (OQ-002 em aberto) |
| Convidado | Acesso a conteúdos públicos com perfil limitado (BR-033) |

## Capacidades principais

| Capacidade | Bounded context | Status arquitetural |
| ---------- | --------------- | --------------------- |
| Estrutura organizacional e vínculos | Organização Corporativa | ATIVO |
| Publicação e gestão documental | Gestão Documental | ATIVO |
| Autenticação, autorização e auditoria | Controle de Acesso | ATIVO (governança avançada PARCIAL) |
| Notificações e busca transversal | Comunicação Interna | ATIVO com confiança reduzida |
| Onboarding de colaboradores | Organização Corporativa | PARCIAL (OQ-001) |
| Solicitação de permissão a recursos privados | Controle de Acesso | PARCIAL (OQ-003) |
| Comunicados institucionais | Comunicação Interna / Gestão Documental | PARCIAL (OQ-004) |

## Escopo funcional

### Dentro do escopo da solução alvo

- Interface web para todos os atores documentados.
- Backend centralizado com quatro módulos lógicos alinhados aos bounded contexts.
- CMS WordPress desacoplado para conteúdo institucional.
- Persistência transacional de metadados e armazenamento separado de binários documentais.
- Integração com Zimbra para autenticação corporativa.
- Quatro ambientes segregados: local, dev, hml e prod.
- Notificações in-app centralizadas no backend.
- Busca unificada como projeção de consulta filtrada por autorização.

### Fora do escopo da solução alvo (estado final)

- API Backend Legado — a ser descomissionado (ADR-015 provisório).
- Provisionamento de identidade corporativa — permanece no Zimbra.
- Microsserviços por bounded context — não adotados (ADR-001).
- Decisão efetiva de autorização no frontend — proibida (ADR-005, ADR-006).

**Nível de confiança:** Médio-Alto para núcleo organizacional, documental e de acesso; Médio para Comunicação Interna e capacidades PARCIAL.

---

# Princípios da Solução

Princípios derivados dos ADRs aceitos. A solução implementável deve materializá-los sem exceção.

| Princípio | ADR | Descrição na solução |
| --------- | --- | -------------------- |
| Backend como núcleo de negócio | ADR-002 | Toda regra de negócio, autenticação, autorização e orquestração de persistência reside no Backend API (Java/Spring Boot). Toda operação de negócio transita pelo backend. |
| Frontend como camada de apresentação | ADR-006 | Frontend Web (Vue) exibe interface, mantém estado de sessão no cliente e consome exclusivamente a API. Sem acesso direto a persistência ou sistemas externos. |
| CMS desacoplado | — | WordPress responsável por conteúdo institucional. Sem regras centrais de negócio, sem acesso ao banco do backend nem às entidades internas. Integração exclusivamente por API. |
| Persistência separada | ADR-004 | Metadados transacionais no Banco de Dados; binários de documentos no Armazenamento de Arquivos. Publicação coordenada pelo backend com atomicidade lógica. |
| Segurança centralizada | ADR-003, ADR-005 | Autenticação via Zimbra; autorização efetiva exclusivamente no backend. Frontend não decide acesso a recursos. |
| Monólito modular | ADR-001, ADR-007 | Um único Backend API hospeda os quatro bounded contexts como módulos lógicos separados (organização, documentos, acesso, comunicação). |
| Referência entre contextos | ADR-009 | Contextos consumidores referenciam dados por identificador de negócio, sem duplicar estado mutável (BR-006). |
| Consistência por aggregate | ADR-010 | Consistência forte dentro de cada aggregate; consistência eventual entre aggregates mediada por eventos. |
| Ambientes isolados | ADR-011 | local, dev, hml e prod com persistência segregada e promoção progressiva de versão. |
| Notificações centralizadas | ADR-012 | Gestão de notificações no backend; sem serviço de notificação independente. |
| Organização upstream | ADR-013 | Nenhum fluxo opera sem vínculo e contexto organizacional válidos. |

---

# Componentes da Solução

## Frontend Web

### Responsabilidades

- Apresentar interface web para todos os atores (colaborador, gestor, administrador, externos).
- Consumir Backend API exclusivamente para operações de negócio.
- Manter estado de sessão e preferências no cliente.
- Exibir notificações in-app e resultados de busca.
- Navegar por módulos de capacidade alinhados ao domínio (organização, documentos, acesso, comunicação).

### Limites

- **Não** contém regras de negócio.
- **Não** efetua decisão de autorização — guards de interface são informativos; decisão efetiva no backend (ADR-005).
- **Não** acessa Banco de Dados, Armazenamento de Arquivos ou Zimbra diretamente.
- **Não** integra-se ao WordPress para operações de negócio central.

### Dependências

| Dependência | Tipo | Finalidade |
| ----------- | ---- | ---------- |
| Backend API | Obrigatória | Todas as operações de negócio |
| — | — | Sem dependência direta de CMS, banco ou armazenamento |

**Tecnologia:** Vue (conforme `.cursor/rules/delivery/implementation-rules.mdc`).

---

## Backend API

### Responsabilidades

- Orquestrar os quatro bounded contexts como módulos lógicos: Organização Corporativa, Gestão Documental, Controle de Acesso, Comunicação Interna.
- Executar regras de negócio, autenticação corporativa (via Zimbra) e autorização efetiva.
- Persistir e recuperar metadados transacionais no Banco de Dados.
- Coordenar armazenamento e recuperação de binários documentais.
- Emitir e entregar notificações in-app.
- Executar busca unificada como projeção read-only filtrada por autorização (ADR-014).
- Registrar eventos de auditoria de governança.
- Expor API consumível pelo Frontend Web e pelo CMS WordPress (integração desacoplada).

### Limites

- **Não** provisiona identidade corporativa — valida credenciais no Zimbra (ADR-003).
- **Não** é decomposto em microsserviços independentes por bounded context (ADR-001).
- **Não** substitui o CMS para conteúdo institucional estático gerenciado no WordPress.
- Coexistência com API Backend Legado é **transitória** — não faz parte do estado alvo final (ADR-015).

### Dependências

| Dependência | Tipo | Finalidade |
| ----------- | ---- | ---------- |
| Banco de Dados | Obrigatória | Metadados, sessão, permissões, organização, notificações |
| Armazenamento de Arquivos | Obrigatória | Binários documentais |
| Zimbra | Obrigatória (crítica) | Validação de identidade corporativa |
| Webhook / E-mail | Opcional | Canais externos de notificação |

**Tecnologia:** Java, Spring Boot, arquitetura modular por bounded context.

---

## CMS WordPress

### Responsabilidades

- Gerenciar conteúdo institucional complementar ao núcleo de negócio do portal.
- Publicar páginas e conteúdos estáticos ou editoriais conforme política institucional.
- Operar de forma autônoma em relação às regras centrais de negócio do backend.

### Limites

- **Não** contém regras centrais de negócio (publicação documental, autorização, organização).
- **Não** acessa diretamente o banco de dados do backend nem entidades internas do backend.
- **Não** efetua decisões de autorização sobre recursos documentais governados pelo backend.
- Integrações com o núcleo do portal ocorrem **exclusivamente por API** do Backend.

### Dependências

| Dependência | Tipo | Finalidade |
| ----------- | ---- | ---------- |
| Backend API | Quando necessário | Integração pontual por API; sem acoplamento de persistência |
| Banco de Dados próprio | Interna ao CMS | Persistência de conteúdo WordPress |

**Tecnologia:** WordPress (conforme regras de delivery).

---

## Banco de Dados

### Responsabilidades

- Persistir metadados transacionais: estrutura organizacional, vínculos, documentos (metadados), pastas, visibilidade, compartilhamento, papéis, permissões, sessão, solicitações, auditoria e notificações.
- Suportar consistência transacional dentro dos limites de cada aggregate.
- Servir como fonte de verdade para todos os metadados de negócio do backend.

### Limites

- **Não** armazena binários de documentos — responsabilidade do Armazenamento de Arquivos (ADR-004).
- **Não** é acessado diretamente pelo Frontend Web ou pelo CMS WordPress.
- **Não** compartilha persistência entre ambientes (ADR-011).

### Dependências

| Dependência | Tipo | Finalidade |
| ----------- | ---- | ---------- |
| Backend API | Consumidor exclusivo | Leitura e escrita transacional |

---

## Armazenamento de Arquivos

### Responsabilidades

- Persistir binários de documentos publicados via Gestão Documental.
- Disponibilizar binários para download autorizado pelo backend.
- Suportar crescimento de volume documental; sujeito a quotas por colaborador (BR-023).

### Limites

- **Não** armazena metadados de negócio — referência mantida no Banco de Dados.
- **Não** é acessado diretamente pelo Frontend Web.
- Publicação deve ser coordenada pelo backend para evitar inconsistência metadado/binário (R-004).

### Dependências

| Dependência | Tipo | Finalidade |
| ----------- | ---- | ---------- |
| Backend API | Consumidor exclusivo | Upload, download e reconciliação com metadados |

---

# Visão dos Ambientes

A solução implanta-se em **quatro ambientes segregados**, alinhados a ADR-011 e à estratégia Docker documentada em `.cursor/rules/architecture/docker-strategy.mdc`.

| Ambiente | Objetivo | Criticidade | Persistência |
| -------- | -------- | ----------- | ------------ |
| **Local** | Desenvolvimento individual; validação rápida; paridade comportamental com demais ambientes | Baixa | Isolada; dados descartáveis |
| **Dev** | Integração contínua; testes de equipe; validação de contratos entre componentes | Média | Isolada; sem dados de produção |
| **Hml** | Homologação funcional; aceite de negócio; gate antes de produção | Alta | Isolada; dados representativos não produtivos |
| **Prod** | Operação institucional; colaboradores e gestores em uso real | Crítica | Isolada; dados operacionais confidenciais |

### Princípios transversais

- **Paridade:** mesma topologia de componentes em todos os ambientes; diferem configuração, dados e exposição a sistemas externos.
- **Isolamento:** nenhum compartilhamento de persistência entre ambientes.
- **Promoção:** fluxo local → dev → hml → prod para versões da solução.
- **Zimbra:** ambiente de teste/simulação em local e dev; pré-produção em hml; corporativo obrigatório em prod.

Configuração por ambiente via variáveis de ambiente e arquivos `.env` segregados — sem valores de segredos na documentação.

---

# Estratégia de Integração

Visão de alto nível das integrações. Contratos detalhados serão definidos em `06-integration-contracts.md`.

## Frontend → Backend

- Frontend Web consome **exclusivamente** a Backend API para todas as operações de negócio.
- Comunicação síncrona por API REST.
- Sessão autenticada estabelecida após validação no Zimbra; token ou mecanismo equivalente gerenciado pelo backend.
- Notificações in-app entregues via consulta ou streaming do backend ao frontend.
- **Lacuna a resolver:** endpoints órfãos documentados (L-010, R-008) — contratos devem ser inventariados e alinhados antes da implementação completa.

## WordPress → Backend

- Integração **pontual e desacoplada** por API do backend.
- WordPress não acessa banco nem entidades internas do backend.
- CMS não participa de fluxos de autorização, publicação documental ou governança organizacional.
- Escopo exato de integração a detalhar em `06-integration-contracts.md`.

## Backend → Sistemas Externos

| Integração | Criticidade | Descrição |
| ---------- | ----------- | --------- |
| Zimbra | Crítica | Validação de credenciais de e-mail corporativo; única fonte de identidade documentada (ADR-003, R-003) |
| Webhook | Opcional | Entrega de notificações a sistemas destino configurados por destinatário |
| E-mail corporativo | Opcional | Canal alternativo de notificação; não bloqueia operação principal |

**Integração residual (transitória):** API Backend Legado — sincronização parcial durante migração; a ser eliminada conforme `09-migration-strategy.md` (ADR-015, R-005).

---

# Estratégia de Segurança

Baseada nos ADRs e na arquitetura de segurança aprovada (`06-security-architecture.md`).

## Autenticação

- Colaboradores autenticam-se com credenciais de e-mail corporativo validadas no **Zimbra** (ADR-003, BR-025, BR-026).
- Backend API consome Zimbra; portal **não provisiona** contas de e-mail.
- Perfis externos (parceiro, convidado) sujeitos a definição operacional pendente (OQ-002).

## Autorização

- **Toda decisão efetiva de acesso** no Backend API (ADR-005).
- Mecanismos: papel e escopo organizacional, compartilhamento, permissão de pasta, solicitação formal aprovada pelo responsável pelo recurso.
- Gestão de Compartilhamento e Autorização são responsabilidades **distintas** com integração obrigatória (ADR-008, OQ-005).
- Frontend exibe interface conforme resposta do backend; não bloqueia acesso por decisão própria.

## Sessão

- Sessão autenticada estabelecida pelo backend após validação no Zimbra.
- Estado de sessão referenciado em operações subsequentes; validação em cada operação sensível.
- Comportamento de sessão ativa durante indisponibilidade do Zimbra a formalizar (R-028).

## Auditoria

- Eventos de governança registrados pelo componente Auditoria no Controle de Acesso (BR-005).
- Eventos documentados: autenticação, atribuição de papel, solicitação/concessão/negação de permissão, alterações organizacionais.
- Catálogo fechado de eventos obrigatórios pendente (OQ-019, L-015).

## Rastreabilidade

- Decisões de acesso, atribuição de papéis e alterações organizacionais devem ser registráveis e consultáveis por administradores no escopo de atuação.
- Auditoria cobre governança de negócio; não substitui logs técnicos de infraestrutura.

---

# Estratégia de Evolução

Alinhada ao roadmap arquitetural de `10-target-architecture.md` e aos riscos de `09-risk-assessment.md`.

## Remoção do legado

| Fase | Ação |
| ---- | ---- |
| Curto prazo | Documentar plano de descomissionamento e critérios de paridade de rotas |
| Médio prazo | Executar migração; eliminar API Backend Legado e mecanismos de autenticação duplicados |
| Estado alvo | Backend API único como caminho principal; ADR-015 encerrado ou substituído |

**Riscos associados:** R-005, R-013.

## Unificação de notificações

| Fase | Ação |
| ---- | ---- |
| Médio prazo | Consolidar subsistemas paralelos em modelo unificado no backend |
| Estado alvo | Uma fonte de verdade para notificações in-app; canais externos opcionais |

**Riscos associados:** R-006.

## Fechamento das Open Questions prioritárias

| OQ | Tema | Impacto na solução |
| -- | ---- | ------------------- |
| OQ-001 | Fluxo oficial de onboarding | Gate de entrada; módulo organização |
| OQ-003 | Solicitação de permissão ponta a ponta | Governança de recursos privados |
| OQ-005 | Compartilhamento ↔ acesso efetivo | Contrato entre módulos documentos e acesso |
| OQ-004 | Ownership de comunicado | Fronteira documentos/comunicação |
| OQ-002 | Parceiro vs. convidado | Perfis externos |
| OQ-006, OQ-017 | Revogação de permissão | Ciclo de vida de acesso |

## Mitigação dos riscos críticos

| Risco | Mitigação na solução |
| ----- | -------------------- |
| R-001 API Backend SPOF | Requisitos de disponibilidade e monitoramento; decisão futura de escalabilidade (R-014) |
| R-002 Banco de Dados SPOF | Requisitos de continuidade e recuperação; prioridade de restauração imediata |
| R-003 Zimbra único | Requisitos de disponibilidade da integração; plano de continuidade de sessões ativas |
| R-015 Continuidade não especificada | Definir em `04-deployment-architecture.md` e `05-environment-strategy.md` |

---

# Restrições Arquiteturais

## ADRs obrigatórios

A solução **deve** respeitar ADR-001 a ADR-014 sem exceção. ADR-015 (legado) aplica-se apenas durante fase de migração.

| ADR | Restrição para a solução |
| --- | ------------------------ |
| ADR-001 | Monólito modular — sem microsserviços |
| ADR-002 | Backend central — sem BFF |
| ADR-003 | Zimbra — sem autenticação paralela sem novo ADR |
| ADR-004 | Metadados e binários em repositórios distintos |
| ADR-005 | Autorização exclusivamente no backend |
| ADR-006 | Frontend apenas apresentação |
| ADR-007 | Quatro contextos em um backend |
| ADR-008 | Compartilhamento e autorização separados |
| ADR-011 | Ambientes com persistência isolada |
| ADR-012 | Notificações no backend |

## Riscos críticos

| ID | Risco | Implicação para Solution Design |
| -- | ----- | ------------------------------- |
| R-001 | API Backend SPOF | Deployment e continuidade devem endereçar disponibilidade |
| R-002 | Banco de Dados SPOF | Estratégia de backup e recuperação obrigatória |
| R-003 | Zimbra único | Integração de autenticação é dependência crítica não substituível sem ADR |

## Limitações conhecidas

- 25 Open Questions em `docs/domain/10-open-questions.md` — algumas bloqueiam capacidades PARCIAL.
- Capacidades PARCIAL não devem ser promovidas a ATIVAS sem encerramento das OQs relacionadas.
- Mecanismos técnicos de backup, réplica e failover ainda não especificados (R-015).
- Estratégia de escalabilidade horizontal indefinida (R-014).

## Trade-offs aceitos

| Trade-off | ADR / Risco | Justificativa |
| --------- | ----------- | ------------- |
| Backend como ponto único de falha | ADR-001, R-001 | Simplicidade operacional e coordenação entre contextos |
| Dependência do Zimbra | ADR-003, R-003 | Alinhamento com identidade corporativa por e-mail |
| Separação metadado/binário com risco de inconsistência parcial | ADR-004, R-004 | Escalabilidade independente de volume de binários |
| Compartilhamento e autorização como responsabilidades distintas | ADR-008, R-009 | Alinhamento com domínio; exige integração explícita |
| Consistência eventual entre aggregates | ADR-010 | Fronteiras de aggregate preservadas |

---

# Critérios de Prontidão

## Para iniciar Implementation (camada seguinte)

A Implementation somente pode iniciar após **conclusão da camada Solution Design** (artefatos `01` a `10`), conforme `.cursor/rules/delivery/implementation-rules.mdc`.

## Para considerar a solução pronta para implementação do núcleo

| Critério | Status atual | Condição para prontidão |
| -------- | ------------ | ----------------------- |
| ADRs aceitos materializados na solução | Parcial | Todos os artefatos Solution Design `02`–`10` concluídos |
| Contratos Frontend ↔ Backend alinhados | Pendente | L-010 resolvida em `06-integration-contracts.md` |
| Requisitos de continuidade definidos | Pendente | L-011 resolvida em `04` e `05` |
| OQ-005 (compartilhamento ↔ autorização) | Pendente | Decisão de negócio registrada antes de governança completa |
| Legado com plano de migração | Pendente | `09-migration-strategy.md` concluído |
| Riscos críticos com mitigação documentada | Pendente | Plano em deployment e environment strategy |

## Escopo implementável imediato (após Solution Design completo)

| Escopo | Pronto? |
| ------ | ------- |
| Núcleo organizacional + documental + acesso (ATIVO) | Sim, com ressalvas (OQ-005, L-010) |
| Governança avançada (solicitação/revogação) | Não — OQs em aberto |
| Onboarding | Não — OQ-001 |
| Comunicação Interna e perfis externos | Não — OQ-002, OQ-004 |
| Eliminação de legado | Não — migração pendente |

**Conclusão:** este documento estabelece a visão da solução; a prontidão plena para Implementation depende da conclusão dos artefatos `02` a `10` da camada Solution Design.

---

# Dependências para os Próximos Artefatos

Informações deste documento que alimentam os artefatos subsequentes.

## `02-system-context.md`

- Atores e público-alvo (seção Visão Executiva).
- Sistemas externos: Zimbra, webhook, e-mail.
- Fronteiras do portal vs. exclusões (escopo funcional).
- Posicionamento do WordPress no ecossistema.

## `03-container-architecture.md`

- Cinco componentes de solução: Frontend Web, Backend API, CMS WordPress, Banco de Dados, Armazenamento de Arquivos.
- Responsabilidades, limites e dependências de cada componente.
- Mapeamento dos quatro bounded contexts ao Backend API modular.
- Exclusão do API Backend Legado no estado alvo.

## `04-deployment-architecture.md`

- Topologia de implantação: Frontend, Backend, WordPress, Banco, Armazenamento, proxy reverso.
- Zonas de confiança (Usuários, Portal, Dados, Externos).
- Requisitos de continuidade para mitigar R-001, R-002, R-003, R-015.
- Networking, volumes, persistência e observabilidade (sem manifests executáveis).

## Artefatos adicionais (referência)

| Artefato | Informações derivadas deste documento |
| -------- | ------------------------------------- |
| `05-environment-strategy.md` | Visão dos ambientes; paridade; isolamento; Zimbra por ambiente |
| `06-integration-contracts.md` | Estratégia de integração; lacunas L-010, L-003, L-009 |
| `07-data-ownership.md` | Separação metadado/binário; ownership por bounded context |
| `08-security-architecture.md` | Estratégia de segurança; ADRs 003, 005, 008 |
| `09-migration-strategy.md` | Evolução: legado, notificações, endpoints órfãos |
| `10-delivery-roadmap.md` | Escopo implementável; OQs prioritárias; sequência de entrega |

---

# Conclusão

A solução alvo do Portal de Comunicação materializa a arquitetura aprovada em uma **aplicação web modular** composta por Frontend Vue, Backend Java/Spring Boot centralizado, CMS WordPress desacoplado e duas camadas de persistência segregadas, implantada em quatro ambientes isolados e integrada criticamente ao Zimbra para identidade corporativa.

A solução preserva os **14 ADRs aceitos**, aceita os **trade-offs documentados** (ponto único de processamento, dependência Zimbra, consistência eventual) e endereça a evolução em três frentes: **eliminação do legado**, **unificação de notificações** e **fechamento das Open Questions** que bloqueiam capacidades PARCIAL.

O núcleo organizacional, documental e de acesso está **pronto para detalhamento implementável**, condicionado à conclusão dos artefatos Solution Design restantes e à resolução das lacunas L-003, L-010 e L-011 identificadas na arquitetura alvo.

Este documento não produz código, endpoints ou infraestrutura executável. Define a **visão consolidada** que orienta `02-system-context.md` como próximo artefato da camada.

---

## Fontes Utilizadas

| Fonte | Uso |
| ----- | --- |
| `docs/architecture/08-decision-records.md` | Princípios, ADRs, restrições |
| `docs/architecture/09-risk-assessment.md` | Riscos críticos, evolução, trade-offs |
| `docs/architecture/10-target-architecture.md` | Baseline, alvo, lacunas, roadmap |
| `docs/architecture/00-architecture-index.md` | Estado de encerramento da Architecture |
| `docs/architecture/01-system-context.md` | Atores, missão, escopo |
| `docs/solution-design/00-solution-design-index.md` | Governança da camada |
| `.cursor/rules/delivery/implementation-rules.mdc` | Stack alvo (Vue, Java/Spring Boot, WordPress, Docker) |
| `.cursor/rules/architecture/docker-strategy.mdc` | Ambientes e princípios de implantação |

*Nenhum código, endpoint, diagrama de implementação, banco físico, docker-compose ou pipeline foi produzido para a construção deste artefato.*

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo |
| ----- | ------ |
| Alto | Componentes, princípios ADR, stack documentada, ambientes |
| Médio | Escopo WordPress, capacidades PARCIAL, critérios de prontidão condicionados a OQs |
