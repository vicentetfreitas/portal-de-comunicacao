# Environment Strategy — Portal de Comunicação

## Objetivo

Este documento formaliza a **estratégia de ambientes** da solução do Portal de Comunicação da Unimed Ceará. Detalha como os containers definidos em `03-container-architecture.md` e implantados conforme `04-deployment-architecture.md` operam em **quatro ambientes segregados** — Local, Dev, Hml e Prod — estabelecendo isolamento, paridade, configuração, promoção e governança operacional.

Materializa ADR-011 (ambientes isolados com persistência segregada) e a estratégia Docker de `.cursor/rules/architecture/docker-strategy.mdc`, sem produzir artefatos executáveis (Docker Compose, YAML, manifests, scripts ou pipelines).

**Rastreabilidade:** `docs/solution-design/01-solution-overview.md`, `docs/solution-design/03-container-architecture.md`, `docs/solution-design/04-deployment-architecture.md`, `docs/architecture/08-decision-records.md`, `docs/architecture/09-risk-assessment.md`, `docs/architecture/10-target-architecture.md`.

---

# Princípios da Estratégia de Ambientes

Princípios transversais que regem todos os ambientes da solução.

## Isolamento

Cada ambiente possui **persistência, configuração e credenciais exclusivas**. Nenhum compartilhamento de Banco de Dados, Armazenamento de Arquivos, Banco WordPress ou secrets entre Local, Dev, Hml e Prod (ADR-011, BR-004). Dados de produção **nunca** são replicados para ambientes inferiores sem anonimização ou substituição por conjuntos representativos não produtivos.

## Paridade

Todos os ambientes replicam a **mesma topologia lógica de containers**: Reverse Proxy, Frontend Web, Backend API, CMS WordPress, Banco de Dados, Armazenamento de Arquivos, Banco WordPress e Observabilidade (quando habilitada). Diferem em configuração, dados, criticidade operacional e exposição a sistemas externos — não em estrutura de componentes.

## Reprodutibilidade

Qualquer ambiente deve poder ser **recriado integralmente** a partir de artefatos versionados (imagens de container, definições de compose por ambiente, variáveis de ambiente) e procedimentos documentados. Volumes persistentes são recriáveis ou restauráveis conforme política do ambiente.

## Segurança

Segredos **não são versionados** em repositório. Credenciais de serviço, tokens de integração e certificados TLS são segregados por ambiente. Autenticação de colaboradores permanece no Zimbra (ADR-003); o portal não provisiona identidade corporativa. Conteúdo confidencial de uso profissional (BR-004) recebe proteção proporcional à criticidade do ambiente.

## Rastreabilidade

Toda promoção de versão entre ambientes deve ser **identificável**: artefato implantado, origem (commit ou tag), responsável pela execução, data e resultado de validações mínimas. Eventos de governança de negócio permanecem no componente Auditoria do Backend; logs técnicos complementam via Observabilidade.

## Governança

Alterações em ambientes Dev, Hml e Prod seguem **controle de mudanças** proporcional à criticidade. Produção recebe alterações exclusivamente via promoção aprovada a partir de Hml. Capacidades PARCIAL não devem ser promovidas a ATIVAS em Prod sem encerramento das Open Questions relacionadas (R-007).

## Promoção controlada

Fluxo unidirecional de versão da solução: **Local → Dev → Hml → Prod**. Nenhum salto de ambiente é permitido sem validação mínima documentada na etapa anterior. Rollback é procedimento operacional excepcional, não substituto de homologação.

---

# Matriz de Ambientes

Comparativo consolidado dos quatro ambientes da solução.

| Dimensão | Local | Dev | Hml | Prod |
| -------- | ----- | --- | --- | ---- |
| **Objetivo** | Desenvolvimento individual; validação rápida; depuração | Integração contínua; testes de equipe; validação de contratos | Homologação funcional; aceite de negócio; gate antes de produção | Operação institucional; uso real por colaboradores e gestores |
| **Usuários** | Desenvolvedor individual | Equipe técnica (desenvolvimento, QA) | Equipe técnica, gestores de negócio, usuários-chave para aceite | Colaboradores, gestores, administradores, parceiros autorizados e convidados (conforme política) |
| **Criticidade** | Baixa | Média | Alta | Crítica |
| **Persistência** | Volumes locais; dados descartáveis | Volumes isolados; sem dados de produção | Volumes isolados; dados representativos não produtivos | Volumes isolados; dados operacionais confidenciais; backup obrigatório |
| **Integrações** | Zimbra simulado ou mock; Webhook/E-mail desabilitados | Zimbra de teste; Webhook/E-mail opcionais ou desabilitados | Zimbra de pré-produção; Webhook/E-mail opcionais | Zimbra corporativo obrigatório; Webhook/E-mail conforme política institucional |
| **Monitoramento** | Opcional; mínimo para depuração | Habilitado; validação de sinais básicos | Habilitado; paridade operacional com Prod | Habilitado; alertas e dashboards operacionais completos |
| **Disponibilidade** | Best-effort; interrupções aceitáveis | Desejável em horário de trabalho da equipe | Alta durante janelas de homologação | Máxima operacional; recuperação imediata em falha de componentes críticos |

---

# Ambiente Local

Ambiente de **estação de trabalho do desenvolvedor**, não exposto publicamente.

## Objetivo

Permitir desenvolvimento individual, validação rápida de alterações e depuração com **paridade comportamental** com os demais ambientes, sem custo operacional de infraestrutura compartilhada.

## Perfil de uso

- Execução da solução completa na máquina do desenvolvedor via Docker.
- Ciclo curto: alteração de código → rebuild/restart de container → validação manual ou automatizada local.
- Sem dependência de disponibilidade de ambientes remotos para desenvolvimento do núcleo.

## Persistência

- Volumes locais dedicados por desenvolvedor.
- Dados **descartáveis** — podem ser recriados a qualquer momento sem impacto institucional.
- Sem backup formal; responsabilidade do desenvolvedor exportar dados de teste quando necessário.

## Integrações simuladas

| Integração | Comportamento |
| ---------- | ------------- |
| **Zimbra** | Simulado ou mock — validação de fluxo de autenticação sem dependência do e-mail corporativo |
| **Webhook** | Desabilitado por padrão |
| **E-mail** | Desabilitado por padrão |
| **API Backend Legado** | Pode ser omitido ou simulado durante desenvolvimento do estado alvo (ADR-015) |

## Limitações

- Não substitui validação em Dev para integração de equipe.
- Observabilidade opcional — sinais limitados ao escopo de depuração individual.
- Sem garantia de paridade de performance ou volume com ambientes superiores.
- Sem exposição pública; acesso restrito ao desenvolvedor.

## Responsabilidades

| Papel | Responsabilidade |
| ----- | ---------------- |
| **Desenvolvedor** | Manter ambiente local funcional; não utilizar dados reais de produção; seguir convenções de configuração |
| **Equipe técnica** | Definir e manter modelo de compose local e variáveis de referência (sem secrets) |

---

# Ambiente Dev

Ambiente **compartilhado pela equipe técnica** para integração contínua e validação coletiva.

## Objetivo

Validar integração entre containers, contratos Frontend ↔ Backend, persistência segregada e comportamento da solução sob uso simultâneo de múltiplos desenvolvedores.

## Perfil de uso

- Deploy automático ou semi-automático a partir de branch de integração.
- Testes de equipe, validação de contratos entre componentes e smoke tests pós-merge.
- Ambiente de referência para reproduzir defeitos reportados fora do Local.

## Persistência

- Volumes isolados exclusivos do ambiente Dev.
- **Proibido** utilizar dados de produção (BR-004, ADR-011).
- Dados podem ser resetados periodicamente conforme política da equipe.
- Retenção curta; backup opcional para recuperação de estado de teste.

## Integrações de teste

| Integração | Comportamento |
| ---------- | ------------- |
| **Zimbra** | Ambiente de teste corporativo ou instância dedicada de validação |
| **Webhook** | Opcional ou desabilitado — testes de canal externo quando necessário |
| **E-mail** | Opcional ou desabilitado |
| **WordPress → Backend** | Integração pontual testável conforme contratos em elaboração |

## Validação contínua

- Build e deploy da solução completa após integração de código.
- Smoke tests de containers críticos: Backend API, Banco de Dados, Frontend Web.
- Verificação de conectividade Backend → Zimbra de teste.
- Regressão básica de fluxos ATIVOS (autenticação, consulta, publicação documental básica).

## Limitações

- Não substitui aceite de negócio — gate formal ocorre em Hml.
- Integrações opcionais podem estar desabilitadas, reduzindo paridade com Prod.
- Disponibilidade best-effort fora de horário de trabalho da equipe.
- Capacidades PARCIAL expostas com ressalva documentada (R-007).

---

# Ambiente Hml

Ambiente de **homologação funcional** e gate obrigatório antes de produção.

## Objetivo

Validar comportamento da solução em condições próximas à produção, permitir aceite de negócio e confirmar critérios mínimos de promoção antes de implantar em Prod.

## Perfil de uso

- Deploy de versões candidatas a produção.
- Testes funcionais completos, cenários de aceite e validação de integrações de pré-produção.
- Janelas de homologação acordadas com gestores de negócio e usuários-chave.

## Aceite de negócio

- Gestores e usuários-chave validam fluxos de valor ATIVOS e capacidades PARCIAL explicitamente expostas.
- Registro formal de aceite ou rejeição com evidências (cenários executados, defeitos encontrados).
- Capacidades bloqueadas por Open Questions (OQ-001, OQ-003, OQ-004, OQ-005) **não** devem ser promovidas como completas sem encerramento das questões.

## Validação funcional

- Execução de suite de testes funcionais alinhada ao escopo implementável.
- Validação de fluxos críticos: autenticação (Zimbra pré-produção), autorização, publicação documental, download autorizado, notificações in-app.
- Verificação de coerência metadado/binário em cenários de publicação e restauração simulada (R-004).
- Testes de integração WordPress ↔ Backend quando contratos estiverem definidos.

## Paridade com produção

| Aspecto | Paridade esperada |
| ------- | ----------------- |
| Topologia de containers | Idêntica |
| Configuração de rede e zonas | Equivalente em estrutura |
| Integração Zimbra | Pré-produção (não mock) |
| TLS | Habilitado na fronteira |
| Observabilidade | Habilitada com retenção proporcional |
| Volumes persistentes | Isolados; dados representativos não produtivos |

Diferem de Prod: volume de dados, carga de usuários, política de backup de longo prazo e SLA operacional.

## Limitações

- Dados representativos — **não** dados operacionais reais de colaboradores em escala de produção.
- Canais opcionais (Webhook, E-mail) podem permanecer desabilitados conforme política.
- Indisponibilidade aceitável fora de janelas de homologação agendadas.
- Coexistência transitória com API Backend Legado, se ainda aplicável, deve ser documentada em `09-migration-strategy.md` (ADR-015, R-005).

---

# Ambiente Prod

Ambiente de **operação institucional** — uso real pelo corpo organizacional da Unimed Ceará.

## Objetivo

Disponibilizar o Portal de Comunicação para colaboradores, gestores e administradores em operação contínua, com proteção de dados confidenciais e governança operacional rigorosa.

## Perfil de uso

- Acesso diário por colaboradores autorizados conforme papéis e escopo organizacional.
- Publicação e consulta documental; gestão organizacional; governança de acesso; notificações in-app.
- Operação 24×7 conforme política institucional (objetivos qualitativos — sem SLA numérico nesta camada).

## Operação institucional

- Alterações de versão **somente** via promoção aprovada Hml → Prod.
- Janelas de manutenção comunicadas previamente aos stakeholders.
- Monitoramento contínuo de componentes críticos (R-001, R-002, R-003).
- Comunicação operacional com gestão de e-mail corporativo para indisponibilidade do Zimbra.

## Proteção de dados

- Dados operacionais confidenciais (BR-004) em persistência isolada e protegida.
- Backup periódico obrigatório de Banco de Dados, Armazenamento de Arquivos e Banco WordPress.
- Proibido copiar dados de Prod para ambientes inferiores sem anonimização formal.
- Acesso operacional à Zona de Persistência restrito a operadores autorizados.

## Disponibilidade

| Componente | Expectativa |
| ---------- | ----------- |
| Backend API | Máxima — recuperação imediata (R-001) |
| Banco de Dados | Máxima — backup contínuo; restauração testada (R-002) |
| Integração Zimbra | Monitorada — novos logins dependem de disponibilidade externa (R-003) |
| Frontend Web / Reverse Proxy | Alta — redeploy rápido |
| Armazenamento de Arquivos | Alta — backup periódico coordenado com metadados |
| CMS WordPress | Média — complementar ao núcleo |
| Webhook / E-mail | Baixa — best-effort |

## Governança

- Controle de mudanças formal para qualquer alteração em Prod.
- Aprovação de negócio e técnica documentada para promoções.
- Auditoria de governança registrada pelo Backend (BR-005); catálogo de eventos pendente (OQ-019).
- Incidentes críticos seguem procedimentos de recuperação definidos neste documento e em `04-deployment-architecture.md`.

---

# Estratégia de Configuração

Gestão de parâmetros operacionais segregados por ambiente.

## Variáveis de ambiente

| Categoria | Exemplos lógicos | Segregação |
| --------- | ---------------- | ---------- |
| Identificação do ambiente | Nome do ambiente (local, dev, hml, prod) | Obrigatória por ambiente |
| Conectividade Backend | Host e porta do Banco de Dados, Armazenamento | Exclusiva por ambiente |
| Integração Zimbra | Endpoint, credenciais de serviço | Por tier de integração (mock, teste, pré-prod, corporativo) |
| Frontend | URL base da API Backend | Por ambiente |
| Observabilidade | Nível de log, destino de exportação | Proporcional à criticidade |
| Feature flags | Capacidades PARCIAL expostas | Restritas em Prod conforme R-007 |

Configuração **externa às imagens** de container — nunca embutida em build (`.cursor/rules/architecture/docker-strategy.mdc`).

## Secrets

- Credenciais de banco, armazenamento, Zimbra, certificados TLS e tokens de serviço.
- Armazenados em mecanismo de secrets por ambiente — **não versionados** em repositório.
- Rotação periódica de credenciais de integração, especialmente Zimbra e banco de dados.
- Segregação absoluta: secret de Prod **nunca** reutilizado em Dev, Hml ou Local.

## Configurações externas

- Certificados TLS gerenciados por ambiente na fronteira Reverse Proxy.
- Parâmetros institucionais transversais (quotas, políticas de visibilidade) persistidos no Banco de Dados do núcleo — não em arquivos de configuração estáticos quando mutáveis por administradores.
- Configuração WordPress (conteúdo, plugins) independente do núcleo.

## Versionamento

| Artefato | Versionado | Não versionado |
| -------- | ---------- | -------------- |
| Definições de compose por ambiente | Sim | — |
| Templates de variáveis de ambiente (sem valores) | Sim | — |
| Imagens de container (tags) | Sim (registry) | — |
| Valores de secrets | — | Sim |
| Certificados e chaves privadas | — | Sim |
| Dados de persistência | — | Sim (backup separado) |

## Segregação por ambiente

Cada ambiente possui conjunto **completo e independente** de variáveis e secrets. Não há arquivo `.env` compartilhado entre ambientes. Referência de nomes de variáveis pode ser comum; valores são sempre exclusivos.

---

# Estratégia de Persistência

Política de dados duráveis por tipo de repositório e ambiente. Alinhada a ADR-004 e ADR-011.

## Banco de Dados

| Aspecto | Local | Dev | Hml | Prod |
| ------- | ----- | --- | --- | ---- |
| Conteúdo | Metadados transacionais do núcleo | Idem | Idem | Idem |
| Volume | Local descartável | Isolado | Isolado | Isolado |
| Backup | Não aplicável | Opcional | Periódico | Periódico automatizado |
| Restauração | Recriação | Recriação ou restore de teste | Testada antes de promoções | Procedimento documentado; prioridade 1 (R-002) |
| Retenção | Descartável | Curta | Média | Conforme política institucional |

## Armazenamento de Arquivos

| Aspecto | Local | Dev | Hml | Prod |
| ------- | ----- | --- | --- | ---- |
| Conteúdo | Binários documentais | Idem | Idem | Idem |
| Volume | Local descartável | Isolado | Isolado | Isolado |
| Backup | Não aplicável | Opcional | Periódico; coordenado com metadados | Periódico; coordenado com metadados |
| Restauração | Recriação | Recriação | Testada; verificação metadado/binário | Prioridade 2; reconciliação R-004 |
| Retenção | Descartável | Curta | Média | Contínua; sujeita a quotas (BR-023) |
| Isolamento | Por desenvolvedor | Por ambiente | Por ambiente | Por ambiente |

## Banco WordPress

| Aspecto | Local | Dev | Hml | Prod |
| ------- | ----- | --- | --- | ---- |
| Conteúdo | Conteúdo editorial CMS | Idem | Idem | Idem |
| Backup | Não aplicável | Opcional | Independente do núcleo | Independente do núcleo |
| Restauração | Recriação | Recriação | Não bloqueia fluxo principal | Não bloqueia fluxo principal do portal |
| Isolamento | Separado do Banco do núcleo | Separado | Separado | Separado |

## Backup

| Ambiente | Política |
| -------- | -------- |
| **Local** | Sem backup formal |
| **Dev** | Opcional; snapshots para recuperação de estado de teste |
| **Hml** | Periódico; testes de restauração antes de validar candidatos a Prod |
| **Prod** | Automatizado e periódico para Banco de Dados, Armazenamento e Banco WordPress; retenção conforme política institucional |

Backup de metadados e binários deve ser **coordenado** para permitir reconciliação em restauração (R-004).

## Restauração

Ordem lógica de restabelecimento (conforme `04-deployment-architecture.md`):

1. Banco de Dados (metadados) — prioridade imediata.
2. Armazenamento de Arquivos — com verificação de referências.
3. Backend API — após persistência disponível.
4. Frontend Web, Reverse Proxy, WordPress.
5. Validação de integração Zimbra.

Procedimento de reconciliação para registros metadado/binário órfãos documentado operacionalmente.

## Retenção

- **Prod:** conforme política institucional de retenção documental e governança.
- **Hml:** ciclo médio; dados resetáveis entre ciclos de homologação.
- **Dev e Local:** curta ou descartável.

## Isolamento

Instância ou volume **exclusivo** por ambiente. Proibido:

- Compartilhar volume de Banco de Dados entre ambientes.
- Compartilhar repositório de binários entre ambientes.
- Restaurar backup de Prod diretamente em Dev ou Hml sem anonimização.

---

# Estratégia de Integrações

Comportamento de sistemas externos por ambiente. Contratos detalhados reservados a `06-integration-contracts.md`.

## Zimbra

| Ambiente | Tier | Criticidade | Comportamento |
| -------- | ---- | ----------- | ------------- |
| **Local** | Simulação / mock | Baixa (desenvolvimento) | Validação de fluxo sem dependência do serviço corporativo |
| **Dev** | Teste | Média | Instância ou endpoint de teste; validação de autenticação real |
| **Hml** | Pré-produção | Alta | Paridade comportamental com Prod; gate de homologação de login |
| **Prod** | Corporativo | **Crítica** (R-003) | Única fonte de identidade documentada (ADR-003); novos logins dependem de disponibilidade |

Indisponibilidade do Zimbra: novos logins bloqueados; sessões ativas podem sustentar operação temporária — comportamento a formalizar (R-028).

## Webhook

| Ambiente | Status | Observação |
| -------- | ------ | ---------- |
| **Local** | Desabilitado | — |
| **Dev** | Opcional | Testes de callback quando necessário |
| **Hml** | Opcional | Validação de entrega externa |
| **Prod** | Conforme política | Best-effort; não bloqueia operação principal (R-032) |

## E-mail

| Ambiente | Status | Observação |
| -------- | ------ | ---------- |
| **Local** | Desabilitado | — |
| **Dev** | Opcional | Sandbox ou relay de teste |
| **Hml** | Opcional | Validação de templates e entrega |
| **Prod** | Conforme política | Canal alternativo de notificação; in-app permanece primário (ADR-012) |

## Integrações futuras

Novas integrações externas exigem:

- Documentação em `06-integration-contracts.md`.
- Avaliação de impacto em riscos e ADRs.
- **Novo ADR** se alterar containers, autenticação ou topologia de deployment.
- Definição de comportamento por ambiente antes de exposição em Prod.

## Comportamento por ambiente — resumo

| Integração | Local | Dev | Hml | Prod |
| ---------- | ----- | --- | --- | ---- |
| Zimbra | Mock/simulação | Teste | Pré-produção | Corporativo (obrigatório) |
| Webhook | Off | Opcional | Opcional | Política |
| E-mail | Off | Opcional | Opcional | Política |
| WordPress → Backend | Local | Dev | Hml | Prod |
| API Backend Legado | Omitido/simulado | Transitório* | Transitório* | Transitório* |

*Coexistência provisória conforme ADR-015; descomissionamento em `09-migration-strategy.md`.

---

# Estratégia de Observabilidade

Coleta transversal de sinais operacionais. Stack específica reservada à Implementation.

## Logs

| Origem | Conteúdo | Retenção por ambiente |
| ------ | -------- | --------------------- |
| Backend API | Operações, erros, integrações, autenticação | Local: mínima; Dev: curta; Hml/Prod: proporcional |
| Frontend Web | Erros de cliente (quando encaminhados) | Idem |
| Reverse Proxy | Acesso HTTP, códigos de resposta | Idem |
| WordPress | Erros e acesso CMS | Idem |
| Integrações | Falhas Zimbra, Webhook, E-mail | Crítico em Prod |

## Métricas

Indicadores alinhados a `10-target-architecture.md` seção 11:

- Disponibilidade de Backend API, Banco de Dados e integração Zimbra.
- Latência de operações transacionais e autenticação.
- Volume documental e utilização de armazenamento (BR-023, R-029).
- Taxa de falha em canais opcionais (R-032).
- Incidentes de inconsistência metadado/binário (R-004).

## Alertas

| Alerta | Ambientes | Severidade |
| ------ | --------- | ---------- |
| Backend API indisponível | Dev, Hml, Prod | Crítica (R-001) |
| Banco de Dados indisponível | Dev, Hml, Prod | Crítica (R-002) |
| Falha de autenticação Zimbra | Hml, Prod | Crítica (R-003) |
| Erros críticos de aplicação | Dev, Hml, Prod | Alta |
| Espaço de armazenamento elevado | Hml, Prod | Média (R-029) |

Local: alertas opcionais; foco em logs de depuração.

## Auditoria

- **Governança de negócio:** componente Auditoria no Backend — eventos de autenticação, papéis, permissões, alterações organizacionais (BR-005). Independente de ambiente na lógica; dados segregados por persistência do ambiente.
- **Auditoria técnica:** logs de acesso, tentativas de autenticação, erros de autorização via Observabilidade — complementar, não substitutiva.

## Monitoramento

| Ambiente | Escopo |
| -------- | ------ |
| **Local** | Opcional; saúde de containers para depuração |
| **Dev** | Dashboards básicos; disponibilidade de integração |
| **Hml** | Paridade estrutural com Prod; validação de alertas |
| **Prod** | Monitoramento completo; acompanhamento de R-001, R-002, R-003, R-015 |

## Diferenças entre ambientes

| Dimensão | Local | Dev | Hml | Prod |
| -------- | ----- | --- | --- | ---- |
| Observabilidade habilitada | Opcional | Sim | Sim | Sim |
| Retenção de logs | Horas/dias | Dias | Semanas | Conforme política |
| Alertas operacionais | Não | Básicos | Completos (pré-Prod) | Completos |
| Dashboards | — | Equipe técnica | Equipe + operação | Operadores autorizados |
| Correlação de requisições | Desenvolvimento | Sim | Sim | Sim |

Observabilidade **não substitui** auditoria de governança de negócio. Métricas administrativas de negócio em aberto (OQ-022).

---

# Estratégia de Promoção

Fluxo unidirecional de versão da solução entre ambientes.

## Local → Dev

| Aspecto | Definição |
| ------- | --------- |
| **Gatilho** | Merge ou integração em branch compartilhada após validação local |
| **Artefato** | Imagem ou build identificado por commit/tag |
| **Validação mínima** | Build bem-sucedido; smoke test de containers; ausência de regressão crítica conhecida |
| **Responsável** | Pipeline de integração contínua ou operador técnico autorizado |
| **Rollback** | Deploy da versão anterior em Dev |

**Critérios mínimos:**

- Código integrado sem conflitos pendentes.
- Containers sobem integralmente (Backend, Frontend, Banco, Armazenamento, Proxy).
- Autenticação funcional contra Zimbra de teste.
- Fluxos ATIVOS básicos executáveis.

## Dev → Hml

| Aspecto | Definição |
| ------- | --------- |
| **Gatilho** | Versão estável em Dev; solicitação formal de homologação |
| **Artefato** | Mesma identificação de versão (commit/tag) promovida |
| **Validação mínima** | Suite de testes de integração; validação de contratos; ausência de defeitos críticos abertos |
| **Responsável** | Líder técnico ou operador autorizado |
| **Rollback** | Revert para versão homologada anterior em Hml |

**Critérios mínimos:**

- Todos os critérios Dev → Hml anteriores atendidos em Dev.
- Testes de integração Frontend ↔ Backend aprovados para escopo da versão.
- Integração Zimbra de pré-produção validada.
- Persistência isolada confirmada — sem vazamento de configuração de Prod.
- Defeitos críticos e altos resolvidos ou explicitamente aceitos com registro.

## Hml → Prod

| Aspecto | Definição |
| ------- | --------- |
| **Gatilho** | Aceite de negócio formal; janela de deploy aprovada |
| **Artefato** | **Exatamente** a mesma versão homologada em Hml — sem alterações |
| **Validação mínima** | Aceite documentado; checklist de promoção; backup pré-deploy |
| **Responsável** | Operador autorizado + aprovação de gestão técnica e negócio |
| **Rollback** | Restauração da versão anterior; procedimento de recovery se necessário |

**Critérios mínimos:**

- Aceite de negócio registrado para escopo da versão.
- Testes de restauração de backup validados em Hml (quando aplicável).
- Procedimento de rollback documentado e ensaiado.
- Comunicação prévia aos stakeholders para janela de manutenção (quando necessária).
- Capacidades PARCIAL identificadas e comunicadas — não promovidas como completas (R-007).
- Nenhum salto direto Local → Hml ou Local → Prod.

### Diagrama de promoção

```text
Local ──(merge + CI)──→ Dev ──(estabilização + testes)──→ Hml ──(aceite)──→ Prod
  │                        │                              │                  │
  └─ desenvolvimento         └─ integração equipe           └─ homologação     └─ operação
     individual                                                    negócio          institucional
```

---

# Estratégia de Recuperação

Procedimentos esperados por ambiente, relacionados aos riscos críticos e de continuidade. Endereça lacuna L-011 (R-015).

## R-001 — API Backend como ponto único de processamento

| Ambiente | Procedimento esperado |
| -------- | --------------------- |
| **Local** | Reinicialização manual do container; persistência intacta |
| **Dev** | Reinicialização automatizada ou manual; monitoramento de saúde; smoke test pós-recovery |
| **Hml** | Reinicialização com verificação de integridade; registro de incidente |
| **Prod** | Reinicialização imediata; escalação operacional; comunicação a stakeholders se indisponibilidade prolongada; decisão futura de escalabilidade horizontal (R-014) |

**Prioridade de recuperação:** 1 — imediata (todos os ambientes com operação ativa).

## R-002 — Banco de Dados como persistência central

| Ambiente | Procedimento esperado |
| -------- | --------------------- |
| **Local** | Recriação de volume; seed de dados de teste |
| **Dev** | Restore de snapshot de teste ou recriação; validação de schema |
| **Hml** | Restore de backup periódico; teste de integridade antes de retomar homologação |
| **Prod** | Restore de backup automatizado; prioridade máxima; verificação de coerência de permissões, compartilhamento e organização; tempo de recovery a definir na Implementation |

**Prioridade de recuperação:** 1 — imediata.

## R-003 — Dependência única do Zimbra

| Ambiente | Procedimento esperado |
| -------- | --------------------- |
| **Local** | Fallback para mock; desenvolvimento não bloqueado |
| **Dev** | Aguardar restauração de Zimbra de teste; sessões ativas podem continuar (R-028) |
| **Hml** | Comunicação com área de e-mail corporativo; homologação de login suspensa até restauração |
| **Prod** | Escalacao para gestão de e-mail corporativo (dependência externa); operação com sessões ativas; novos logins bloqueados; comunicação institucional |

**Prioridade de recuperação:** 2 — alta (novos acessos).

## R-015 — Continuidade operacional não especificada

Este documento estabelece **prioridades e procedimentos esperados** por ambiente. Mecanismos técnicos (ferramentas, RTO/RPO numéricos, failover automático) serão definidos na camada Implementation.

| Ambiente | Expectativa de continuidade |
| -------- | --------------------------- |
| **Local** | Sem SLA; recuperação manual pelo desenvolvedor |
| **Dev** | Recuperação em horário de trabalho; perda de dados aceitável |
| **Hml** | Recuperação antes de retomar ciclo de homologação; testes de backup periódicos |
| **Prod** | Procedimentos documentados; backup testado; ordem de restabelecimento: Persistência → Backend → Frontend/Proxy → Integrações |

### Matriz de prioridade de recuperação (transversal)

| Prioridade | Componentes |
| ---------- | ------------- |
| **1 — Imediata** | Banco de Dados, Backend API |
| **2 — Alta** | Armazenamento de Arquivos, integração Zimbra, Frontend Web, Reverse Proxy |
| **3 — Média** | CMS WordPress, Observabilidade |
| **4 — Baixa** | Webhook, E-mail |

### Cenários por ambiente

| Cenário | Local / Dev | Hml | Prod |
| ------- | ----------- | --- | ---- |
| Falha total do ambiente | Recriação completa | Restore de backup + redeploy | Restore + redeploy + comunicação |
| Falha parcial do Backend | Restart container | Restart + validação | Restart + monitoramento + escalação |
| Falha do Zimbra | Mock / aguardar teste | Suspender testes de login | Sessões ativas; escalação externa |
| Inconsistência metadado/binário | Ignorar ou recriar | Reconciliação manual | Procedimento de reconciliação (R-004) |

---

# Estratégia Docker

Modelagem **conceitual** dos arquivos de compose por ambiente. Sem geração de arquivos, YAML ou configurações executáveis nesta camada (`.cursor/rules/architecture/docker-strategy.mdc`, `.cursor/rules/process/solution-design-phase.mdc`).

## docker-compose.local.yml

| Aspecto | Definição conceitual |
| ------- | -------------------- |
| **Finalidade** | Orquestrar solução completa na estação de trabalho do desenvolvedor |
| **Containers** | Reverse Proxy, Frontend Web, Backend API, CMS WordPress, Banco de Dados, Armazenamento de Arquivos, Banco WordPress |
| **Observabilidade** | Opcional — perfil de depuração |
| **Volumes** | Locais; descartáveis; nomes prefixados para evitar colisão entre desenvolvedores |
| **Rede** | Rede interna isolada; sem exposição pública além de portas locais |
| **Configuração** | Variáveis via arquivo `.env` local (não versionado); Zimbra mock |
| **Integrações** | Webhook e E-mail desabilitados por padrão |

## docker-compose.dev.yml

| Aspecto | Definição conceitual |
| ------- | -------------------- |
| **Finalidade** | Ambiente compartilhado de integração contínua |
| **Containers** | Mesma topologia de Local |
| **Observabilidade** | Habilitada |
| **Volumes** | Persistentes e isolados do ambiente Dev |
| **Rede** | Acesso restrito à equipe técnica |
| **Configuração** | Secrets de Dev; Zimbra de teste |
| **Integrações** | Webhook/E-mail opcionais via perfil ou flag |

## docker-compose.hml.yml

| Aspecto | Definição conceitual |
| ------- | -------------------- |
| **Finalidade** | Homologação funcional com paridade estrutural a Prod |
| **Containers** | Mesma topologia; recursos proporcionais |
| **Observabilidade** | Habilitada com retenção ampliada |
| **Volumes** | Persistentes; isolados; backup periódico |
| **Rede** | TLS na fronteira; comunicação interna preferencialmente criptografada |
| **Configuração** | Secrets de Hml; Zimbra pré-produção |
| **Integrações** | Paridade com Prod para Zimbra; demais conforme política |

## docker-compose.prod.yml

| Aspecto | Definição conceitual |
| ------- | -------------------- |
| **Finalidade** | Operação institucional |
| **Containers** | Mesma topologia; configuração de produção |
| **Observabilidade** | Habilitada; alertas operacionais |
| **Volumes** | Persistentes; backup automatizado; isolamento máximo |
| **Rede** | TLS obrigatório; menor superfície de exposição |
| **Configuração** | Secrets de Prod; Zimbra corporativo |
| **Integrações** | Zimbra obrigatório; Webhook/E-mail conforme política institucional |

### Princípios transversais Docker

| Princípio | Aplicação |
| --------- | --------- |
| **Paridade** | Mesmos serviços em todos os composes; diferem configuração e perfis |
| **Isolamento** | Nenhum volume ou rede compartilhado entre composes de ambientes distintos |
| **Reprodutibilidade** | Composes versionados; imagens com tags identificáveis |
| **Segregação** | Configuração externa às imagens; secrets por ambiente |
| **Proibição** | Valores reais de senhas, tokens e certificados na documentação |

---

# Governança Operacional

Controles que regem operação, mudanças e responsabilidades nos ambientes.

## Controle de mudanças

| Ambiente | Rigidez | Processo |
| -------- | ------- | -------- |
| **Local** | Baixa | Livre para desenvolvedor |
| **Dev** | Média | Deploy via integração contínua; comunicação à equipe |
| **Hml** | Alta | Deploy de versão candidata; registro de solicitação |
| **Prod** | Máxima | Promoção aprovada; janela de manutenção; backup pré-deploy |

Alterações de infraestrutura que impactem containers, integrações ou persistência em Prod exigem validação em Hml prévia.

## Responsabilidades

| Papel | Local | Dev | Hml | Prod |
| ----- | ----- | --- | --- | ---- |
| **Desenvolvedor** | Manter ambiente; validar localmente | Contribuir para estabilidade | Apoiar homologação | — |
| **Equipe técnica / DevOps** | Definir composes e templates | Operar deploy e monitoramento | Operar deploy; executar testes de backup | Operar deploy; incidentes; recovery |
| **QA** | — | Testes de integração | Testes funcionais e aceite | — |
| **Gestor de negócio** | — | — | Aceite formal | Requisitos operacionais |
| **Administrador do portal** | — | — | Validar fluxos de governança | Operação de usuários e políticas |

## Rastreabilidade

- Promoções registram: versão (commit/tag), ambiente origem e destino, executor, data, resultado de validações.
- Incidentes em Prod registram: componente afetado, ação de recovery, tempo de indisponibilidade (quando mensurável).
- Alterações de configuração segregadas por ambiente com histórico auditável.

## Aprovação

| Transição | Aprovador mínimo |
| --------- | ---------------- |
| Local → Dev | Integração contínua (automática) ou peer review |
| Dev → Hml | Líder técnico |
| Hml → Prod | Líder técnico + aceite de negócio + operador autorizado |

Capacidades PARCIAL em Prod requerem registro explícito de limitações conhecidas (R-007).

## Auditoria

- **Negócio:** eventos de governança no Backend por ambiente (dados segregados).
- **Operação:** logs de deploy, alterações de configuração e ações de recovery via Observabilidade.
- Acesso aos ambientes Hml e Prod restrito a operadores autorizados; Local e Dev restritos à equipe técnica.

---

# Dependências para Próximos Artefatos

Como este documento alimenta os artefatos subsequentes da camada Solution Design.

## `06-integration-contracts.md`

- Comportamento de Zimbra, Webhook e E-mail **por ambiente** (seção Estratégia de Integrações).
- Endpoints e protocolos de integração condicionados ao tier de ambiente (mock, teste, pré-prod, corporativo).
- Restrições de rede e firewall derivadas da segregação por ambiente.
- Contratos WordPress ↔ Backend testáveis em Dev e Hml antes de Prod.
- Impacto transitório da API Backend Legado por ambiente durante migração (ADR-015).

## `08-security-architecture.md`

- Segregação de secrets e credenciais por ambiente.
- TLS e criptografia interna em Hml e Prod.
- Proteção de dados confidenciais (BR-004) proporcional à criticidade.
- Comportamento de sessão durante indisponibilidade do Zimbra por ambiente (R-028).
- Matriz de acesso operacional aos ambientes.
- Auditoria de governança vs. auditoria técnica por ambiente.

## `09-migration-strategy.md`

- Coexistência da API Backend Legado em Dev, Hml e Prod durante fase transitória.
- Estratégia de dados de migração — sem cópia de Prod para ambientes inferiores.
- Sequência de validação em Dev → Hml → Prod para descomissionamento do legado.
- Impacto em persistência e integrações durante transição.
- Critérios de promoção específicos para releases de migração.

## `10-delivery-roadmap.md`

- Sequência de entrega alinhada ao fluxo Local → Dev → Hml → Prod.
- Gates de ambiente como marcos do roadmap (integração, homologação, produção).
- Dependência de resolução de L-011 (continuidade) para releases em Prod.
- Capacidades PARCIAL e critérios de exposição por ambiente (R-007).
- Priorização de artefatos de infraestrutura (composes, observabilidade) na Implementation.

---

# Conclusão

A estratégia de ambientes do Portal de Comunicação estabelece **quatro ambientes segregados** — Local, Dev, Hml e Prod — com **topologia idêntica de containers** e **persistência, configuração e credenciais exclusivas** por ambiente, materializando ADR-011 e a estratégia Docker da solução.

Os princípios de **isolamento, paridade, reprodutibilidade, segurança, rastreabilidade, governança e promoção controlada** garantem evolução progressiva da solução desde o desenvolvimento individual até a operação institucional, com **Hml como gate obrigatório** antes de Prod.

A estratégia endereça os riscos críticos **R-001, R-002 e R-003** e a lacuna de continuidade **L-011 (R-015)** com procedimentos esperados por ambiente e matriz de prioridade de recuperação. Integrações externas escalonam de simulação (Local) a corporativo (Prod), preservando ADR-003.

A modelagem conceitual dos composes Docker por ambiente prepara a camada **Implementation** sem produzir artefatos executáveis nesta camada. Os documentos `06-integration-contracts.md`, `08-security-architecture.md`, `09-migration-strategy.md` e `10-delivery-roadmap.md` consumirão as definições aqui consolidadas.

---

## Fontes Utilizadas

| Fonte | Uso |
| ----- | --- |
| `docs/solution-design/01-solution-overview.md` | Visão dos ambientes e princípios |
| `docs/solution-design/03-container-architecture.md` | Containers e persistência |
| `docs/solution-design/04-deployment-architecture.md` | Topologia, zonas, continuidade, ambientes |
| `docs/architecture/08-decision-records.md` | ADR-003, ADR-004, ADR-011, ADR-015 |
| `docs/architecture/09-risk-assessment.md` | R-001, R-002, R-003, R-004, R-007, R-015, R-028, R-029, R-032 |
| `docs/architecture/10-target-architecture.md` | Lacunas L-011; métricas; roadmap |
| `.cursor/rules/architecture/docker-strategy.mdc` | Composes por ambiente; princípios Docker |
| `.cursor/rules/architecture/deployment-modeling.mdc` | Modelagem de deployment |
| `.cursor/rules/process/solution-design-phase.mdc` | Governança da camada |

*Nenhum Docker Compose, YAML, Kubernetes, Terraform, script, pipeline ou configuração executável foi produzido para a construção deste artefato.*

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo |
| ----- | ------ |
| Alto | Matriz de ambientes, princípios, promoção, isolamento, integrações por tier |
| Médio | Procedimentos de recovery detalhados (mecanismos técnicos na Implementation); comportamento de sessão sem Zimbra (R-028) |
