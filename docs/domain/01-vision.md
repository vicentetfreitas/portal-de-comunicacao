# Visão de Domínio

## Resumo Executivo

O **Portal de Comunicação** é um sistema de gestão e comunicação interna da **Unimed Ceará**, voltado a colaboradores e parceiros autorizados. Seu domínio central concentra-se na **organização corporativa multi-singular** (singulares, áreas, equipes e colaboradores), na **gestão e compartilhamento controlado de documentos** com visibilidade hierárquica, e no **controle de acesso por papéis e escopo organizacional**.

A Discovery consolidou **27 capacidades funcionais**, das quais **18 estão operacionais**, **8 parciais** e **1 legada**. O núcleo de negócio — estrutura organizacional, documentos, pastas, permissões, usuários, notificações e auditoria — possui evidência convergente em múltiplos documentos. Capacidades periféricas (comunicados, analytics, colaboração, convidados) permanecem com lacunas de confirmação.

---

## Problema de Negócio

O sistema resolve a necessidade de **centralizar a comunicação interna e a gestão documental** de uma organização de saúde cooperativa com estrutura federativa, onde múltiplas unidades singulares, áreas departamentais e equipes precisam compartilhar informações de forma **controlada, rastreável e confidencial**.

**Necessidade atendida:** permitir que colaboradores autorizados acessem documentos e informações institucionais no contexto correto (singular, área, equipe ou pessoal), enquanto administradores estruturam a organização, definem quem pode ver ou publicar conteúdo e registram eventos relevantes.

**Resultado buscado:** disponibilizar um ponto único de acesso a documentos públicos e privados, com governança por papéis, visibilidade por escopo organizacional e fluxos de solicitação de acesso quando o colaborador não possui permissão direta.

---

## Objetivo do Produto

Oferecer um **portal de comunicação interna** para a Unimed Ceará que integre:

- gestão da estrutura organizacional (singulares, áreas, equipes, colaboradores);
- publicação, organização e consulta de documentos em pastas hierárquicas;
- controle de quem acessa cada recurso, conforme papel e vínculo organizacional;
- notificação de eventos relevantes ao colaborador;
- integração de novos usuários ao contexto organizacional adequado.

Evidência: módulos ATIVOS em `01-current-modules.md`; descrição institucional em `PublicLayout.vue` e `README.md`.

---

## Contexto Organizacional

O sistema se encaixa na **Unimed Ceará** como plataforma de **comunicação e gestão documental interna**, servindo colaboradores e parceiros autorizados da organização.

| Área / Papel | Participação no processo |
|---|---|
| **Federação / Singular** | Unidade organizacional que agrupa áreas, colaboradores e documentos; pode representar a própria Unimed Ceará ou outras singulares da federação |
| **Área departamental** | Setor vinculado a uma singular; delimita escopo de documentos, pastas e colaboradores |
| **Equipe** | Agrupamento operacional dentro de uma área |
| **Colaborador** | Usuário operacional com vínculo a singular, área e eventualmente equipe |
| **Administração** | Papéis com gestão global, por singular ou por área — usuários, estrutura, documentos e auditoria |
| **Convidado** | Acesso limitado a documentos e conteúdos públicos |

**Áreas impactadas:** todas as unidades singulares cadastradas, suas áreas e equipes, e os colaboradores vinculados — conforme hierarquia documentada em `03-current-data-model.md` e regras de compartilhamento em `sharing.ts`.

---

## Principais Capacidades de Negócio

| Capacidade | Descrição | Evidências |
|---|---|---|
| Autenticação corporativa | Identificação do colaborador via credenciais de e-mail corporativo | `01` Autenticação e Sessão; `05` Zimbra |
| Gestão de usuários | Cadastro, perfil, provisionamento e vínculo organizacional | `01` Usuários; `03` Usuário/Colaborador |
| Controle de acesso por papéis | Autorização conforme papel e escopo (global, singular, área, equipe, pessoal) | `02` RBAC; `01` RBAC |
| Gestão de singulares | Administração de unidades organizacionais da federação | `01` Singulares; `03` Singular |
| Gestão de áreas | Administração hierárquica de setores departamentais | `01` Áreas; `03` Área |
| Gestão de equipes | Administração de times vinculados a áreas | `01` Equipes; `03` Equipe |
| Gestão de colaboradores | Visualização e administração de colaboradores por área ou singular | `01` Colaboradores |
| Gestão de documentos | Publicação, consulta, download e classificação de documentos | `01` Documentos; `03` Documento |
| Organização em pastas | Estrutura hierárquica de diretórios com visibilidade por escopo | `01` Pastas; `README.md` |
| Permissões de pastas | Regras granulares de acesso por hierarquia de pasta | `01` Permissões de Pastas |
| Solicitação de acesso | Pedido e resposta (aprovar/negar) para recursos privados | `01` Solicitação de Permissões (PARCIAL); `README.md` |
| Notificações | Comunicação de eventos ao colaborador no portal | `01` Notificações |
| Integração de novos usuários | Seleção inicial de singular e área para novos colaboradores | `01` Onboarding (PARCIAL) |
| Auditoria | Registro e consulta de eventos de controle de acesso | `01` Auditoria; `03` Registro de Auditoria |
| Controle de armazenamento | Quotas e uso de espaço por colaborador | `01` Armazenamento; `03` quotas em Usuário |
| Configuração do portal | Parâmetros institucionais e metadados do portal | `01` Configuração do Portal |
| Busca unificada | Pesquisa transversal em documentos, áreas, singulares e colaboradores | `01` Busca Global (PARCIAL) |
| Métricas administrativas | Indicadores para administração do portal | `01` Analytics (PARCIAL) |
| Comunicados corporativos | Gestão de comunicados institucionais | `01` Comunicados (PARCIAL) |
| Informações internas | Feed de publicações para colaboradores | `01` Fique por Dentro (PARCIAL) |
| Gestão de convidados | Administração de usuários com acesso limitado | `01` Convidados (PARCIAL) |
| Colaboração | Espaço de interação entre colaboradores | `01` Central de Colaboração (PARCIAL) |

---

## Principais Atores

| Ator | Responsabilidade | Evidências |
|---|---|---|
| Administrador global | Gestão completa do portal, usuários, estrutura e auditoria | `02` role `administrator` |
| Administrador de singular | Gestão da singular, áreas vinculadas e colaboradores no escopo | `02` role `singular_administrator` |
| Administrador de área | Gestão da área, equipes, colaboradores e documentos do setor | `02` role `area_administrator` |
| Proprietário de equipe | Gestão da equipe, membros e documentos no escopo do time | `02` role `team_owner` |
| Colaborador | Consulta e publicação de documentos conforme permissões; operação no contexto singular/área/equipe | `02` role `collaborator`; `03` Colaborador |
| Convidado | Acesso a documentos e conteúdos públicos | `02` role `visitor` |
| Parceiro autorizado | Acesso restrito conforme política institucional do portal | `PublicLayout.vue`, `AppFooter.vue` |

---

## Conceitos Centrais do Domínio

| Conceito | Descrição | Evidências |
|---|---|---|
| Singular | Unidade organizacional da federação que agrupa áreas, colaboradores e documentos | `03` Entidade Singular |
| Área | Setor ou departamento vinculado a uma singular; pode ser hierárquica | `03` Entidade Área |
| Equipe | Agrupamento de colaboradores dentro de uma área | `03` Entidade Equipe |
| Colaborador | Pessoa vinculada operacionalmente a singular e área, com identidade de acesso ao portal | `03` Entidade Colaborador |
| Documento | Artefato de comunicação ou arquivo gerenciado no portal, com visibilidade e escopo definidos | `03` Entidade Documento |
| Pasta | Estrutura hierárquica que organiza documentos por contexto organizacional ou pessoal | `03` Entidade Pasta |
| Visibilidade | Nível de exposição do documento ou pasta (público, privado por singular, área ou colaborador) | `README.md`; `sharing.ts` |
| Compartilhamento | Regra que define quem pode acessar um recurso (eu, setor, federação, singulares, colaboradores) | `sharing.ts`; `03` Compartilhamento de Documento |
| Papel (RBAC) | Papel de negócio que determina o que o usuário pode fazer no portal | `02` Roles; `03` Papel RBAC |
| Contexto organizacional | Combinação de singular, área e equipe que delimita a visão do colaborador | `02` Contextos de Acesso |
| Notificação | Comunicação de evento relevante ao colaborador dentro do portal | `03` Notificação |
| Solicitação de permissão | Pedido de acesso a recurso privado aguardando decisão do responsável | `01` Solicitação de Permissões |
| Auditoria | Registro de eventos de controle de acesso e alterações relevantes | `03` Registro de Auditoria |
| Onboarding | Processo de vinculação inicial do colaborador a singular e área | `01` Onboarding |
| Convidado | Usuário com perfil de acesso restrito a conteúdos públicos | `02` role `visitor` |

---

## Fluxo de Valor de Alto Nível

1. O **colaborador** autentica-se com identidade corporativa (FT-AUTH).
2. O Portal verifica se existe **COLABORADOR** com vínculo completo (DH-03).
3. Se necessário, **FT-PRIMEIRO-ACESSO** conduz onboarding (domínio → Singular → Área → Equipe opcional) e cria o COLABORADOR.
4. O **Contexto Ativo** é derivado do único vínculo cadastral (DH-02); o backend determina a **Home** dinâmica; o frontend renderiza.
5. O colaborador **navega** no Contexto Ativo derivado e consulta **documentos** e **pastas** conforme visibilidade e permissões.
6. Quando não possui acesso a um recurso privado, pode **solicitar permissão** ao responsável.
7. O responsável **aprova ou nega** a solicitação; o colaborador é **notificado** do resultado.
8. **Administradores** estruturam Federação → Singular → Área → Equipe → Colaborador; **auditoria** registra eventos relevantes.

Hierarquia oficial (DEC-ORG-001): Federação → Singular → Área → Equipe → Colaborador.


---

## Dependências de Negócio

| Dependência | Papel no Negócio | Evidência |
|---|---|---|
| Identidade corporativa por e-mail | Autenticação de colaboradores com conta de e-mail da organização | `05` Zimbra; domínios em `AdminCollaboratorsPage.vue` |
| Estrutura federativa multi-singular | Organização em unidades cooperativas com áreas e equipes próprias | `03` Singular; `sharing.ts` regra SINGULARES/FEDERACAO |
| Política de confidencialidade interna | Restrição de acesso a colaboradores e parceiros autorizados | `AppFooter.vue` |
| Responsável pelo recurso | Decisão de concessão de acesso a documentos ou pastas privadas | `01` Solicitação de Permissões; `README.md` |

---

## Restrições de Negócio Identificadas

| Restrição | Evidência |
|---|---|
| Acesso restrito a colaboradores e parceiros autorizados da Unimed Ceará | `AppFooter.vue` |
| Informações do portal são confidenciais e de uso profissional | `AppFooter.vue` |
| Autenticação vinculada a domínios de e-mail corporativos (ex.: unimedceara.com.br) | `05` Zimbra; `AdminCollaboratorsPage.vue` |
| Documentos e pastas possuem visibilidade por escopo (público, singular, área, pessoal) | `README.md`; `sharing.ts` |
| Autorização depende de papel e contexto organizacional (singular, área, equipe) | `02` Contextos de Acesso |
| Colaborador sem área vinculada pode ser impedido de operar no portal | `02` guard para `/no-area-access` |
| Categoria de documento inclui "Comunicado" como tipo de conteúdo institucional | `DocumentEditDialog.vue` |

---

## Hipóteses a Validar

| Hipótese | Justificativa |
|---|---|
| Singulares representam unidades cooperativas distintas dentro da federação Unimed, não apenas a sede | Regra de compartilhamento SINGULARES ("outras Unimeds"); campos `cod_unimed` e `nome_unimed` em formulários de singular |
| "Federação" no compartilhamento corresponde ao conjunto de colaboradores da Unimed Ceará | Label em `sharing.ts`: "Privado à Unimed Ceará" |
| Fluxo de solicitação de permissões é processo de negócio com aprovação pelo proprietário do recurso | `README.md` descreve aprovação/negação; UI `PermissionRequestsPage` referenciada em `01` |
| Convidados correspondem a parceiros externos com acesso somente a conteúdo público | Role `visitor` com `portal_read_public_docs`; menção a "parceiros autorizados" no rodapé |
| "Fique por Dentro" é canal de publicações internas para colaboradores | Módulo homônimo em `01`; capabilities `portal_*_post` em `02` |
| Comunicados são publicações formais de comunicação corporativa, distintas de documentos genéricos | Categoria "Comunicado" em formulário de documento; módulo Comunicados em `01` |

*Todas marcadas como hipóteses — requerem validação com stakeholders de negócio.*

---

## Lacunas de Conhecimento

| Lacuna | Impacto |
|---|---|
| Comunicados sem modelo de persistência confirmado | Capacidade de negócio não verificável de ponta a ponta |
| Analytics e métricas administrativas sem backend confirmado | Indicadores de gestão podem não refletir dados reais |
| Central de Colaboração sem entidade ou processo documentado | Escopo de colaboração entre colaboradores indefinido |
| Solicitação de permissões sem persistência no backend | Fluxo de aprovação pode estar incompleto em produção |
| Onboarding com dois modelos de fluxo (vínculo direto vs. solicitação com aprovação) | Processo de integração de novos colaboradores ambíguo |
| Escopo e critérios de "parceiro autorizado" não detalhados | Limite entre colaborador e parceiro externo não formalizado |
| Relação operacional entre equipe como agrupamento e equipe como registro alternativo | Modelo organizacional de equipes pode gerar inconsistência |
| Processo de negócio para convidados além do acesso a conteúdo público | Gestão de convidados parcialmente evidenciada |

*Fonte: lacunas consolidadas em `03`, `08` e status PARCIAL em `01`.*

---

## Conflitos Encontrados

| Item | Discovery | Código / Outra fonte | Observação |
|---|---|---|---|
| Onboarding | CMS vincula colaborador a singular/área via seleção direta (`options`, `select`, `status`) | Frontend modela solicitações com aprovação administrativa (`current`, `requests`) | Dois fluxos de integração de novos colaboradores coexistem sem consolidação |
| Solicitação de permissões | Módulo PARCIAL — sem controller ou persistência localizada | README e interface descrevem fluxo completo com aprovação/negação | Capacidade de negócio prometida na documentação do produto não confirmada na Discovery |
| Comunicados | Módulo PARCIAL — objeto virtual sem persistência | Formulário de documento inclui categoria "Comunicado" | Pode haver sobreposição entre "comunicado" como tipo de documento e módulo Comunicados |
| Rastreamento de eventos / LGPD | Não documentado na Discovery | README menciona "Event Tracking LGPD-compliant" | Afirmação de negócio/compliance não sustentada pelos documentos Discovery |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
|---|---|---|
| Alto | Estrutura organizacional (singular, área, equipe, colaborador), documentos, pastas, papéis RBAC, notificações, auditoria | Múltiplas evidências convergentes em `01`, `02`, `03` com status ATIVO |
| Médio | Onboarding, solicitação de permissões, busca global, convidados, compartilhamento | Evidência em uma ou duas camadas; lacunas ou divergências documentadas |
| Baixo | Comunicados, analytics, central de colaboração, fique por dentro | Módulos PARCIAIS; sem persistência ou processo de negócio confirmado |

A classificação geral é **Médio-Alto** porque o núcleo do domínio está bem sustentado, mas capacidades periféricas e fluxos de aprovação permanecem com ressalvas da Discovery (todos os documentos 01–08 com status APROVADO COM RESSALVAS).

---

## Fontes Utilizadas

### Documentos Discovery (fonte primária)

- `docs/discovery/01-current-modules.md`
- `docs/discovery/02-current-rbac.md`
- `docs/discovery/03-current-data-model.md`
- `docs/discovery/04-current-endpoints.md`
- `docs/discovery/05-current-integrations.md`
- `docs/discovery/06-current-infrastructure.md`
- `docs/discovery/07-current-architecture.md`
- `docs/discovery/08-technical-debt.md`

### Documentos adicionais consultados

- `README.md` — visão de produto e descrição de fluxos de permissões e diretórios
- `frontend/src/types/sharing.ts` — regras de compartilhamento e escopos de visibilidade
- `frontend/src/layouts/PublicLayout.vue` — posicionamento institucional do portal
- `frontend/src/components/app/AppFooter.vue` — restrições de acesso e confidencialidade

### Validações realizadas no código

- Confirmação de domínios de e-mail corporativos e mapeamento organizacional (`AdminCollaboratorsPage.vue`)
- Confirmação de categoria "Comunicado" em formulário de documento (`DocumentEditDialog.vue`)
- Confirmação de labels de compartilhamento por federação, singular e setor (`sharing.ts`)
- Nenhuma redescoberta de módulos, entidades ou papéis além do já documentado na Discovery
