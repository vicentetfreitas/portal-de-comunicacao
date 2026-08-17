# Aggregates

## Objetivo

Identificar os Aggregates conceituais do **Portal de Comunicação** — conjuntos de conceitos que devem permanecer consistentes como unidade de negócio, protegendo invariantes e gerando Domain Events aprovados.

Este documento consolida agregados derivados dos Bounded Contexts, do Context Map, dos conceitos e dos eventos documentados em `07-domain-events.md`, `06-context-map.md`, `05-bounded-contexts.md` e `04-domain-concepts.md`. Não modela classes, tabelas, entidades técnicas ou implementação.

---

## Critérios Utilizados

| Critério | Descrição |
| -------- | --------- |
| Consistência de negócio | Conceitos que devem mudar de estado de forma atômica do ponto de vista do domínio |
| Invariantes | Regras que não podem ser violadas dentro do limite do agregado |
| Responsabilidade clara | Um ponto de controle para mudanças de estado e aplicação de regras |
| Geração de eventos | Mudanças relevantes produzem Domain Events aprovados em `07-domain-events` |
| Proteção de regras | O agregado encapsula decisões que afetam exposição, vínculo, autorização ou comunicação |
| Alinhamento com contextos | Um agregado por Bounded Context aprovado em `05-bounded-contexts` |
| Referência entre agregados | Agregados distintos referenciam-se por identificadores de negócio, sem compartilhar estado mutável |

**Critérios explicitamente excluídos:** estrutura de banco de dados, classes, módulos técnicos, transações de implementação.

---

## Candidatos Identificados

| Aggregate | Justificativa |
| --------- | ------------- |
| Organização Corporativa | Hierarquia federativa, vínculos de colaborador e contexto organizacional exigem consistência conjunta; mudanças geram eventos de integração e estabelecimento de contexto |
| Gestão Documental | Documento, pasta, visibilidade e compartilhamento devem permanecer coerentes para definir exposição e audiência de cada recurso |
| Controle de Acesso | Papéis, solicitações de permissão e decisões de autorização formam unidade de governança com regras que não podem ser violadas isoladamente |
| Comunicação Interna | Notificações e publicações em canais internos devem permanecer consistentes com destinatário e conteúdo comunicado |

---

## Aggregate: Organização Corporativa

### Propósito

Garantir a consistência da estrutura federativa multi-singular, dos vínculos operacionais do colaborador e do contexto organizacional que orienta a operação no portal.

### Conceitos Relacionados

| Conceito |
| -------- |
| Federação |
| Singular |
| Área |
| Equipe |
| Colaborador |
| Contexto organizacional |
| Onboarding |
| Código Unimed |
| Unimed Ceará |

### Invariantes

- Área pertence a uma singular; equipe pertence a uma área
- Colaborador possui vínculo operacional a singular e área
- Colaborador sem área vinculada não pode operar no portal
- Onboarding vincula colaborador à singular e área adequadas antes da operação plena
- Contexto organizacional combina singular, área e eventual equipe de forma coerente
- Singular agrupa áreas e colaboradores no escopo organizacional
- Código Unimed identifica singular de forma única no contexto da federação

### Eventos Relacionados

| Evento |
| ------ |
| Colaborador Integrado |
| Contexto Organizacional Estabelecido |
| Vínculo Organizacional Alterado |
| Estrutura Organizacional Alterada |

### Limite de Consistência

Dentro deste agregado devem permanecer consistentes: a hierarquia Federação → Singular → Área → Equipe, o vínculo do colaborador à singular e área, e o contexto organizacional resultante. Alterações na estrutura ou nos vínculos são controladas como unidade — não é permitido colaborador operacional sem área vinculada, nem equipe fora de uma área, nem área sem singular.

Referências a documentos e papéis em outros agregados utilizam identificadores de singular, área, equipe e colaborador sem duplicar o estado organizacional.

---

## Aggregate: Gestão Documental

### Propósito

Garantir a consistência da publicação, organização e exposição de documentos e pastas, incluindo visibilidade, compartilhamento e limites de armazenamento no escopo organizacional.

### Conceitos Relacionados

| Conceito |
| -------- |
| Documento |
| Pasta |
| Visibilidade |
| Compartilhamento |
| Recurso privado |
| Recurso público |
| Quota de armazenamento |
| Conteúdo confidencial |

### Invariantes

- Documento é organizado em pasta e vinculado a escopo organizacional (singular, área)
- Pasta é organizada no contexto de singular, área ou é pessoal de colaborador — nunca em escopo inconsistente
- Visibilidade classifica exposição como público ou privado conforme escopo (singular, área, colaborador)
- Compartilhamento define audiência coerente com a visibilidade do recurso
- Recurso privado possui acesso restrito a escopo ou pessoas definidas; recurso público é acessível sem restrição de escopo privado
- Quota de armazenamento limita o uso do colaborador; publicação não pode violar o limite sem consequência de negócio
- Conteúdo do portal é confidencial e de uso profissional

### Eventos Relacionados

| Evento |
| ------ |
| Documento Publicado |
| Documento Organizado em Pasta |
| Visibilidade Definida |
| Compartilhamento Definido |
| Quota de Armazenamento Ultrapassada |
| Comunicado Institucional Publicado |

### Limite de Consistência

Dentro deste agregado devem permanecer consistentes: cada documento ou pasta com seu escopo organizacional, posição hierárquica, classificação de visibilidade e regra de compartilhamento. Visibilidade e compartilhamento não podem contradizer-se — um recurso privado não pode ter exposição pública sem reclassificação explícita.

O escopo organizacional (singular, área, colaborador) é referenciado a partir do agregado Organização Corporativa; a efetivação de acesso é governada pelo agregado Controle de Acesso.

---

## Aggregate: Controle de Acesso

### Propósito

Garantir a consistência da governança de acesso ao portal — papéis, autenticação corporativa, fluxo de solicitação de permissão, decisões do responsável pelo recurso e rastreabilidade em auditoria.

### Conceitos Relacionados

| Conceito |
| -------- |
| Papel |
| Autenticação corporativa |
| Solicitação de permissão |
| Responsável pelo recurso |
| Auditoria |
| Administrador global |
| Administrador de singular |
| Administrador de área |
| Proprietário de equipe |
| Convidado |
| Parceiro autorizado |

### Invariantes

- Autorização depende de papel e contexto organizacional (singular, área, equipe)
- Autenticação vinculada a domínios de e-mail corporativos da organização
- Acesso restrito a colaboradores e parceiros autorizados da Unimed Ceará
- Solicitação de permissão referencia recurso privado e é submetida ao responsável pelo recurso
- Permissão concedida ou negada é decisão exclusiva do responsável pelo recurso
- Convidado possui acesso restrito a documentos e conteúdos públicos
- Papel determina o que a pessoa pode fazer e em qual escopo organizacional
- Eventos relevantes de controle de acesso são registrados em auditoria

### Eventos Relacionados

| Evento |
| ------ |
| Colaborador Autenticado |
| Papel Atribuído |
| Solicitação de Permissão Registrada |
| Permissão Concedida |
| Permissão Negada |
| Perfil de Convidado Habilitado |
| Evento de Controle Registrado em Auditoria |

### Limite de Consistência

Dentro deste agregado devem permanecer consistentes: a atribuição de papéis por escopo, o ciclo de vida de cada solicitação de permissão (registrada → concedida ou negada) e o registro de auditoria correspondente. Uma solicitação não pode ser decidida sem responsável identificado; papel não pode existir sem referência de escopo organizacional válida.

Referências a colaborador e contexto organizacional vêm do agregado Organização Corporativa; referências a recurso privado vêm do agregado Gestão Documental.

---

## Aggregate: Comunicação Interna

### Propósito

Garantir a consistência da comunicação de eventos relevantes e publicações em canais internos, assegurando que notificações e conteúdos institucionais sejam dirigidos ao colaborador correto.

### Conceitos Relacionados

| Conceito |
| -------- |
| Notificação |
| Comunicado |
| Fique por Dentro |
| Central de Colaboração |
| Busca unificada |
| Métricas administrativas |

### Invariantes

- Notificação é dirigida a colaborador identificado no portal
- Notificação comunica evento ou resultado de processo relevante (ex.: permissão concedida ou negada)
- Fique por Dentro destina-se a colaboradores como canal de informações internas
- Busca unificada consulta documentos, áreas, singulares e colaboradores sem alterar o estado dos agregados consultados
- Comunicado institucional é publicação de comunicação corporativa (requer qualificação de contexto quando categoria de documento)

### Eventos Relacionados

| Evento |
| ------ |
| Notificação Dirigida ao Colaborador |
| Publicação em Fique por Dentro Realizada |
| Comunicado Institucional Publicado |

### Limite de Consistência

Dentro deste agregado devem permanecer consistentes: cada notificação com destinatário e conteúdo coerente com o fato de negócio que a originou, e cada publicação em canal interno com escopo de audiência definido. Notificações não são emitidas sem destinatário colaborador identificável.

Este agregado consome fatos dos demais agregados (permissão decidida, documento publicado) sem controlar o estado que os originou. Busca unificada e métricas administrativas operam como consulta ou exibição, sem invariantes de mutação consolidadas.

---

## Relacionamento Entre Aggregates

| Aggregate Origem | Relacionamento | Aggregate Destino |
| ---------------- | -------------- | ----------------- |
| Organização Corporativa | fornece referência de escopo para | Gestão Documental |
| Organização Corporativa | fornece referência de contexto para | Controle de Acesso |
| Organização Corporativa | fornece referência de destinatário para | Comunicação Interna |
| Gestão Documental | fornece referência de recurso para | Controle de Acesso |
| Controle de Acesso | governa acesso efetivo sobre | Gestão Documental |
| Controle de Acesso | fornece fatos para notificação em | Comunicação Interna |
| Gestão Documental | fornece conteúdo consultável para | Comunicação Interna |
| Comunicação Interna | consome fatos de | Organização Corporativa, Gestão Documental, Controle de Acesso |

*Relacionamentos derivados de `06-context-map`. Agregados referenciam-se por identificadores de negócio; consistência entre agregados é eventual, mediada por Domain Events.*

---

## Invariantes Críticas do Domínio

| Invariante | Aggregate Responsável |
| ---------- | --------------------- |
| Colaborador operacional possui vínculo a singular e área | Organização Corporativa |
| Hierarquia Federação → Singular → Área → Equipe respeitada | Organização Corporativa |
| Documento vinculado a escopo organizacional válido | Gestão Documental |
| Visibilidade e compartilhamento coerentes no mesmo recurso | Gestão Documental |
| Autorização depende de papel e contexto organizacional | Controle de Acesso |
| Solicitação de permissão decidida pelo responsável pelo recurso | Controle de Acesso |
| Acesso restrito a colaboradores e parceiros autorizados | Controle de Acesso |
| Notificação dirigida a colaborador identificado | Comunicação Interna |
| Conteúdo do portal é confidencial e de uso profissional | Gestão Documental |
| Quota de armazenamento respeitada na publicação | Gestão Documental |

---

## Eventos Geradores de Mudança

| Evento | Aggregate Responsável |
| ------ | --------------------- |
| Colaborador Integrado | Organização Corporativa |
| Contexto Organizacional Estabelecido | Organização Corporativa |
| Vínculo Organizacional Alterado | Organização Corporativa |
| Estrutura Organizacional Alterada | Organização Corporativa |
| Documento Publicado | Gestão Documental |
| Documento Organizado em Pasta | Gestão Documental |
| Visibilidade Definida | Gestão Documental |
| Compartilhamento Definido | Gestão Documental |
| Quota de Armazenamento Ultrapassada | Gestão Documental |
| Comunicado Institucional Publicado | Gestão Documental / Comunicação Interna — responsabilidade em lacuna |
| Colaborador Autenticado | Controle de Acesso |
| Papel Atribuído | Controle de Acesso |
| Solicitação de Permissão Registrada | Controle de Acesso |
| Permissão Concedida | Controle de Acesso |
| Permissão Negada | Controle de Acesso |
| Perfil de Convidado Habilitado | Controle de Acesso |
| Evento de Controle Registrado em Auditoria | Controle de Acesso |
| Notificação Dirigida ao Colaborador | Comunicação Interna |
| Publicação em Fique por Dentro Realizada | Comunicação Interna |

---

## Aggregates Centrais

| Aggregate | Justificativa |
| --------- | ------------- |
| Organização Corporativa | Contexto upstream; vínculo e contexto organizacional são pré-requisito de todos os demais agregados |
| Gestão Documental | Objeto principal do domínio; documentos e pastas com regras de exposição constituem o núcleo de valor do portal |
| Controle de Acesso | Governa operação efetiva; materializa confidencialidade e concessão de acesso a recursos privados |

---

## Aggregates de Suporte

| Aggregate | Justificativa |
| --------- | ------------- |
| Comunicação Interna | Habilita notificação e canais de informação; depende dos agregados centrais sem controlar seu estado; capacidades periféricas com confiança documentada como baixa a média |

---

## Pontos de Atenção

| Item | Motivo |
| ---- | ------ |
| Compartilhamento (Gestão Documental) vs. acesso efetivo (Controle de Acesso) | Duas unidades de consistência participam da decisão de quem acessa; risco de divergência entre audiência definida e permissão efetiva |
| Comunicado atravessa Gestão Documental e Comunicação Interna | Evento Comunicado Institucional Publicado sem aggregate responsável único estabilizado |
| Colaborador como conceito compartilhado | Definido em Organização Corporativa; referenciado em todos os agregados sem duplicar vínculos |
| Solicitação de permissão com operação parcial documentada | Ciclo de vida do agregado Controle de Acesso pode estar incompleto na prática |
| Onboarding com fluxos coexistentes | Regras de entrada do agregado Organização Corporativa ambíguas |
| Busca unificada e métricas administrativas | Consulta/exibição sem invariantes de mutação claras no agregado Comunicação Interna |
| Parceiro autorizado vs. convidado | Invariantes de perfil externo no agregado Controle de Acesso não formalizadas |

---

## Lacunas Restantes

| Lacuna | Impacto |
| ------ | ------- |
| Dono único do evento Comunicado Institucional Publicado | Limite de consistência entre Gestão Documental e Comunicação Interna indefinido |
| Processos de revogação de permissão e alteração de compartilhamento | Ciclo de vida incompleto nos agregados Gestão Documental e Controle de Acesso |
| Fluxo consolidado de onboarding | **Resolvido no TO-BE** — DEC-FA-001 / BR-011 |
| Central de Colaboração e métricas administrativas | Sem invariantes de mutação estáveis no agregado Comunicação Interna |
| Parceiro autorizado | Perfil sem invariantes operacionais consolidadas no agregado Controle de Acesso |
| Granularidade interna dos agregados | Documento e pasta podem exigir sublimites não detalhados neste documento conceitual |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
| ----- | ------ | ------------- |
| Alto | Aggregates Organização Corporativa, Gestão Documental e Controle de Acesso; invariantes do núcleo; eventos geradores centrais | Alinhamento direto com Bounded Contexts, Domain Events e regras aprovadas |
| Médio | Relacionamentos entre agregados; invariantes críticas transversais; evento Comunicado Institucional Publicado | Sobreposições documentadas entre agregados |
| Baixo | Aggregate Comunicação Interna no todo; Central de Colaboração; métricas; busca unificada como mutação | Capacidades parciais; invariantes de consulta vs. mutação não estabilizadas |

A classificação geral é **Médio-Alto** porque os três agregados centrais possuem propósito, invariantes e eventos estáveis, enquanto o agregado Comunicação Interna e fronteiras entre agregados permanecem sujeitos a validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/07-domain-events.md`
- `docs/domain/06-context-map.md`
- `docs/domain/05-bounded-contexts.md`
- `docs/domain/04-domain-concepts.md`

*Nenhuma fonte adicional foi necessária. Aggregates, invariantes, relacionamentos e lacunas foram consolidados exclusivamente a partir dos documentos de domínio aprovados, conforme a Regra de Ouro.*
