# Domain Events

## Objetivo

Identificar os principais Domain Events do **Portal de Comunicação** — acontecimentos relevantes para o negócio que representam fatos consumados, alteram o estado do domínio ou produzem consequências observáveis em outros contextos.

Este documento consolida eventos derivados dos Bounded Contexts, do Context Map e dos conceitos aprovados em `06-context-map.md`, `05-bounded-contexts.md` e `04-domain-concepts.md`. Não modela eventos técnicos, de API, de banco de dados ou operações CRUD genéricas.

---

## Critérios Utilizados

| Critério | Descrição |
| -------- | --------- |
| Mudança de estado relevante | O evento representa um fato de negócio consumado, não uma intenção ou operação técnica |
| Consequência de negócio | O evento produz efeito observável no domínio ou em processos subsequentes |
| Impacto em outros contextos | O evento atravessa fronteiras de contexto ou habilita ações em contextos consumidores |
| Geração de notificações | O evento é candidato a comunicação ao colaborador via Comunicação Interna |
| Alteração de permissões ou exposição | O evento modifica quem pode acessar ou ver conteúdo |
| Sustentação por evidência | O evento é derivado do fluxo de valor, responsabilidades e regras documentados nos contextos aprovados |
| Linguagem de negócio | Nomeação em termos ubíquos; pergunta orientadora: "O que aconteceu de relevante para o negócio?" |

**Critérios explicitamente excluídos:** eventos de persistência, eventos de API, eventos de interface, operações CRUD sem significado de negócio (ex.: "registro atualizado" sem consequência domain).

---

## Eventos Identificados

| Evento | Contexto Principal | Descrição |
| ------ | ------------------ | --------- |
| Colaborador Integrado | Organização Corporativa | Colaborador concluiu onboarding e foi vinculado à singular e área adequadas |
| Contexto Organizacional Estabelecido | Organização Corporativa | Combinação de singular, área e equipe foi definida para orientar a operação do colaborador |
| Vínculo Organizacional Alterado | Organização Corporativa | Vínculo do colaborador a singular, área ou equipe foi modificado |
| Estrutura Organizacional Alterada | Organização Corporativa | Singular, área ou equipe foi criada ou reestruturada por administração |
| Colaborador Autenticado | Controle de Acesso | Colaborador foi identificado por credenciais de e-mail corporativo |
| Papel Atribuído | Controle de Acesso | Papel de negócio foi definido para o colaborador em escopo organizacional |
| Solicitação de Permissão Registrada | Controle de Acesso | Pedido formal de acesso a recurso privado foi submetido ao responsável |
| Permissão Concedida | Controle de Acesso | Responsável pelo recurso aprovou acesso a recurso privado |
| Permissão Negada | Controle de Acesso | Responsável pelo recurso negou acesso a recurso privado |
| Perfil de Convidado Habilitado | Controle de Acesso | Pessoa recebeu perfil de acesso restrito a conteúdos públicos |
| Evento de Controle Registrado em Auditoria | Controle de Acesso | Evento relevante de controle de acesso ou alteração foi registrado para consulta |
| Documento Publicado | Gestão Documental | Artefato de comunicação foi disponibilizado no portal com escopo organizacional |
| Documento Organizado em Pasta | Gestão Documental | Documento foi posicionado na estrutura hierárquica de pastas |
| Visibilidade Definida | Gestão Documental | Nível de exposição (público ou privado por escopo) foi aplicado a documento ou pasta |
| Compartilhamento Definido | Gestão Documental | Regra de audiência foi estabelecida para documento ou pasta |
| Quota de Armazenamento Ultrapassada | Gestão Documental | Colaborador atingiu o limite de espaço atribuído para armazenamento |
| Notificação Dirigida ao Colaborador | Comunicação Interna | Comunicação de evento relevante foi entregue ao colaborador no portal |
| Publicação em Fique por Dentro Realizada | Comunicação Interna | Informação interna foi publicada no canal destinado a colaboradores |
| Comunicado Institucional Publicado | Comunicação Interna / Gestão Documental | Comunicação corporativa formal foi publicada |

---

## Eventos por Contexto

### Organização Corporativa

| Evento | Descrição | Consequência |
| ------ | --------- | ------------ |
| Colaborador Integrado | Onboarding concluiu vinculação do colaborador à singular e área | Habilita atribuição de papel e operação no contexto organizacional correto |
| Contexto Organizacional Estabelecido | Singular, área e eventual equipe definidos como referência do colaborador | Delimita visão, escopo documental e autorização nos contextos consumidores |
| Vínculo Organizacional Alterado | Colaborador passou a estar vinculado a outra singular, área ou equipe | Pode alterar escopo de documentos, pastas e permissões efetivas |
| Estrutura Organizacional Alterada | Administrador criou ou reestruturou singular, área ou equipe | Redefine hierarquia federativa e escopos disponíveis no portal |

### Gestão Documental

| Evento | Descrição | Consequência |
| ------ | --------- | ------------ |
| Documento Publicado | Artefato de comunicação disponibilizado com escopo e autoria definidos | Conteúdo passa a ser consultável conforme visibilidade, compartilhamento e permissões |
| Documento Organizado em Pasta | Documento posicionado em pasta hierárquica no contexto organizacional ou pessoal | Estrutura de localização e herança de regras de pasta aplicáveis |
| Visibilidade Definida | Documento ou pasta classificado como público ou privado por escopo | Define exposição do recurso e distinção entre recurso público e privado |
| Compartilhamento Definido | Regra de audiência estabelecida (pessoal, setor, federação, singulares ou colaboradores) | Determina quem pode acessar o recurso; interage com governança de Controle de Acesso |
| Quota de Armazenamento Ultrapassada | Uso de armazenamento do colaborador excedeu o limite atribuído | Pode impedir nova publicação de documentos até regularização |

### Controle de Acesso

| Evento | Descrição | Consequência |
| ------ | --------- | ------------ |
| Colaborador Autenticado | Identidade corporativa validada por credenciais de e-mail da organização | Pré-requisito para estabelecimento de contexto, papel e operação no portal |
| Papel Atribuído | Papel de negócio definido em escopo (global, singular, área, equipe ou pessoal) | Determina o que o colaborador pode fazer e em qual escopo organizacional |
| Solicitação de Permissão Registrada | Pedido formal de acesso a recurso privado encaminhado ao responsável | Inicia fluxo de decisão de concessão de acesso |
| Permissão Concedida | Responsável pelo recurso aprovou solicitação de acesso | Colaborador passa a poder acessar recurso privado; candidato a notificação |
| Permissão Negada | Responsável pelo recurso negou solicitação de acesso | Acesso ao recurso privado permanece restrito; candidato a notificação |
| Perfil de Convidado Habilitado | Pessoa recebeu perfil com acesso restrito a recursos públicos | Limita operação a documentos e conteúdos públicos |
| Evento de Controle Registrado em Auditoria | Evento de papel ou controle de acesso registrado para consulta | Garante rastreabilidade e governança institucional |

### Comunicação Interna

| Evento | Descrição | Consequência |
| ------ | --------- | ------------ |
| Notificação Dirigida ao Colaborador | Comunicação de evento relevante entregue ao colaborador | Informa resultado de processos (ex.: permissão concedida ou negada) ou eventos do domínio |
| Publicação em Fique por Dentro Realizada | Informação interna publicada no canal de feed para colaboradores | Disponibiliza conteúdo institucional no canal de informações internas |
| Comunicado Institucional Publicado | Comunicação corporativa formal publicada | Informação institucional disponível a colaboradores; fronteira com Gestão Documental em lacuna |

---

## Fluxo de Eventos Relevantes

### Fluxo principal — integração e operação do colaborador

Fluxo sustentado pela sequência de negócio em `06-context-map` e pelo fluxo de valor documentado nos contextos aprovados.

1. **Colaborador Autenticado** — identidade corporativa validada
2. **Colaborador Integrado** — vínculo a singular e área estabelecido por onboarding
3. **Contexto Organizacional Estabelecido** — singular, área e eventual equipe definidos
4. **Papel Atribuído** — autorização de negócio configurada conforme escopo
5. **Documento Publicado** — conteúdo disponibilizado no escopo organizacional
6. **Visibilidade Definida** e **Compartilhamento Definido** — exposição e audiência do recurso estabelecidas

### Fluxo — concessão de acesso a recurso privado

Sustentado pelo fluxo de valor (passos 5–6) e pelas responsabilidades de Controle de Acesso e Comunicação Interna.

1. **Solicitação de Permissão Registrada** — colaborador solicita acesso a recurso privado
2. **Permissão Concedida** ou **Permissão Negada** — responsável pelo recurso decide
3. **Notificação Dirigida ao Colaborador** — resultado comunicado ao solicitante
4. **Evento de Controle Registrado em Auditoria** — decisão e alteração relevante rastreadas

### Fluxo — estruturação administrativa

Sustentado pelas responsabilidades de administradores documentadas em `05-bounded-contexts`.

1. **Estrutura Organizacional Alterada** — singular, área ou equipe criada ou reestruturada
2. **Vínculo Organizacional Alterado** ou **Colaborador Integrado** — colaboradores vinculados ao novo contexto
3. **Papel Atribuído** — papéis administrativos e operacionais configurados no escopo
4. **Evento de Controle Registrado em Auditoria** — alterações relevantes registradas

---

## Eventos que Cruzam Contextos

| Evento | Contexto Origem | Contextos Impactados |
| ------ | --------------- | -------------------- |
| Colaborador Integrado | Organização Corporativa | Controle de Acesso, Gestão Documental, Comunicação Interna |
| Contexto Organizacional Estabelecido | Organização Corporativa | Controle de Acesso, Gestão Documental |
| Vínculo Organizacional Alterado | Organização Corporativa | Controle de Acesso, Gestão Documental |
| Estrutura Organizacional Alterada | Organização Corporativa | Gestão Documental, Controle de Acesso |
| Colaborador Autenticado | Controle de Acesso | Organização Corporativa, Gestão Documental |
| Papel Atribuído | Controle de Acesso | Gestão Documental |
| Solicitação de Permissão Registrada | Controle de Acesso | Gestão Documental, Comunicação Interna |
| Permissão Concedida | Controle de Acesso | Gestão Documental, Comunicação Interna |
| Permissão Negada | Controle de Acesso | Comunicação Interna |
| Documento Publicado | Gestão Documental | Controle de Acesso, Comunicação Interna |
| Visibilidade Definida | Gestão Documental | Controle de Acesso |
| Compartilhamento Definido | Gestão Documental | Controle de Acesso |
| Quota de Armazenamento Ultrapassada | Gestão Documental | Gestão Documental (bloqueio de publicação) |
| Notificação Dirigida ao Colaborador | Comunicação Interna | — (evento de saída; consome fatos de outros contextos) |
| Comunicado Institucional Publicado | Comunicação Interna / Gestão Documental | Comunicação Interna, Gestão Documental |
| Evento de Controle Registrado em Auditoria | Controle de Acesso | Todos os contextos cujos eventos são rastreados |

---

## Eventos Centrais do Domínio

Eventos fundamentais para o fluxo de valor do portal, conforme `06-context-map` sequência de negócio.

| Evento | Justificativa |
| ------ | ------------- |
| Colaborador Integrado | Pré-requisito upstream; sem vínculo organizacional o colaborador não opera no contexto correto |
| Contexto Organizacional Estabelecido | Delimita visão, escopo documental e referência de autorização para todos os contextos consumidores |
| Papel Atribuído | Materializa governança de quem pode operar e em qual escopo |
| Documento Publicado | Razão de existência do portal como repositório de comunicação interna |
| Compartilhamento Definido | Define audiência de cada recurso; núcleo do controle de exposição documental |
| Visibilidade Definida | Classifica exposição público/privado; distingue recurso público de privado |
| Solicitação de Permissão Registrada | Formaliza concessão de acesso quando não há permissão direta |
| Permissão Concedida | Materializa decisão de acesso a recurso privado |
| Notificação Dirigida ao Colaborador | Comunica consequências de processos relevantes ao ator central do domínio |

---

## Eventos de Suporte

| Evento | Justificativa |
| ------ | ------------- |
| Colaborador Autenticado | Habilita operação, mas é pré-condição técnica de identidade convertida em fato de negócio |
| Documento Organizado em Pasta | Suporta localização hierárquica; não altera audiência por si só |
| Vínculo Organizacional Alterado | Relevante em manutenção administrativa; não ocorre em todo fluxo cotidiano |
| Estrutura Organizacional Alterada | Relevante para administradores; periférico ao fluxo operacional do colaborador |
| Perfil de Convidado Habilitado | Aplica a perfil restrito; escopo menor que colaborador operacional |
| Evento de Controle Registrado em Auditoria | Rastreabilidade transversal; não altera estado operacional direto |
| Quota de Armazenamento Ultrapassada | Controle de limite; reativo a uso excessivo |
| Publicação em Fique por Dentro Realizada | Canal periférico de comunicação interna |
| Permissão Negada | Complementar a Permissão Concedida no fluxo de solicitação |

---

## Eventos com Baixa Confiança

| Evento | Motivo |
| ------ | ------ |
| Comunicado Institucional Publicado | Comunicado aparece como categoria de documento e como publicação institucional; fronteira entre Gestão Documental e Comunicação Interna não estabilizada |
| Publicação em Fique por Dentro Realizada | Capacidade com status parcial na visão de domínio |
| Colaborador Integrado (fluxo com aprovação) | Onboarding com modelos coexistentes (seleção direta vs. solicitação com aprovação) não consolidados |
| Solicitação de Permissão Registrada | Fluxo descrito no produto com confirmação parcial de operação completa |
| Perfil de Parceiro Autorizado Habilitado | Não incluído no catálogo principal — critérios operacionais de parceiro autorizado vs. convidado não formalizados |

---

## Lacunas Restantes

| Lacuna | Impacto |
| ------ | ------- |
| Fluxo consolidado de onboarding (seleção vs. solicitação com aprovação) | Evento Colaborador Integrado pode ter pré-requisitos de negócio diferentes não documentados |
| Solicitação de permissões sem confirmação completa de operação | Sequência Solicitação → Permissão Concedida/Negada → Notificação pode estar incompleta |
| Distinção comunicado (categoria) vs. comunicado (publicação institucional) | Evento Comunicado Institucional Publicado com contexto de origem indefinido |
| Escopo da Central de Colaboração | Não foi possível identificar eventos de negócio estáveis para interação entre colaboradores |
| Métricas administrativas | Sem léxico de indicadores; eventos de gestão do portal não catalogados |
| Parceiro autorizado vs. convidado | Evento de habilitação de parceiro autorizado não estabilizado |
| Eventos de revogação de permissão ou alteração de compartilhamento | Processos de reversão não explicitados nos documentos aprovados |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
| ----- | ------ | ------------- |
| Alto | Eventos do núcleo: Colaborador Integrado, Contexto Organizacional Estabelecido, Papel Atribuído, Documento Publicado, Visibilidade Definida, Compartilhamento Definido, Solicitação de Permissão Registrada, Permissão Concedida/Negada, Notificação Dirigida ao Colaborador | Derivados diretamente do fluxo de valor e sequência de negócio em `06-context-map` |
| Médio | Colaborador Autenticado, Estrutura Organizacional Alterada, Vínculo Organizacional Alterado, Evento de Controle Registrado em Auditoria, Quota de Armazenamento Ultrapassada | Sustentados por responsabilidades dos contextos, com menor detalhamento de processo |
| Baixo | Comunicado Institucional Publicado, Publicação em Fique por Dentro Realizada, eventos de Central de Colaboração e métricas | Capacidades periféricas com status parcial e ambiguidades documentadas |

A classificação geral é **Médio-Alto** porque o catálogo do fluxo principal (integração → publicação → concessão de acesso → notificação) está estável e coerente com os contextos aprovados, enquanto eventos de comunicação periférica e processos com lacunas permanecem sujeitos a validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/06-context-map.md`
- `docs/domain/05-bounded-contexts.md`
- `docs/domain/04-domain-concepts.md`

*Nenhuma fonte adicional foi necessária. Domain Events, fluxos, eventos transversais e lacunas foram consolidados exclusivamente a partir dos documentos de domínio aprovados, conforme a Regra de Ouro.*
