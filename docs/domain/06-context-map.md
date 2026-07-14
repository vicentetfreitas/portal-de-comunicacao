# Context Map

## Objetivo

Documentar como os Bounded Contexts aprovados do **Portal de Comunicação** se relacionam em termos de negócio — dependências, fluxo de informação, responsabilidades predominantes, conceitos compartilhados e fronteiras sensíveis.

Este documento consolida os contextos e relações definidos em `05-bounded-contexts.md`. Não modela arquitetura, integrações técnicas, APIs, eventos, agregados ou microsserviços.

---

## Contextos Identificados

Contextos aprovados em `05-bounded-contexts.md`, sem alteração de nomenclatura ou responsabilidades.

| Contexto | Responsabilidade Principal |
| -------- | -------------------------- |
| Organização Corporativa | Estruturar e manter a hierarquia federativa multi-singular, vínculos de colaboradores e contexto organizacional |
| Gestão Documental | Publicar, organizar e controlar a exposição de documentos e pastas com visibilidade e compartilhamento |
| Controle de Acesso | Goverar quem pode acessar e operar no portal por papéis, escopo, solicitações de permissão e auditoria |
| Comunicação Interna | Informar, notificar e engajar colaboradores por canais internos, publicações e busca transversal |

---

## Visão Geral dos Relacionamentos

Relacionamentos aprovados em `05-bounded-contexts`, com justificativa derivada do fluxo de valor e dos relacionamentos conceituais documentados.

| Contexto Origem | Relacionamento | Contexto Destino | Justificativa |
| --------------- | -------------- | ---------------- | ------------- |
| Organização Corporativa | fornece informações para | Gestão Documental | Documentos e pastas são vinculados a escopo organizacional (singular, área, colaborador) |
| Organização Corporativa | fornece informações para | Controle de Acesso | Autorização depende de contexto organizacional (singular, área, equipe) e vínculo do colaborador |
| Organização Corporativa | fornece informações para | Comunicação Interna | Notificações e publicações são dirigidas a colaboradores com vínculo organizacional definido |
| Gestão Documental | depende de | Organização Corporativa | Escopo documental requer singular, área e colaborador como referência organizacional |
| Controle de Acesso | depende de | Organização Corporativa | Papéis e autorização operam sobre escopos definidos pela estrutura organizacional |
| Controle de Acesso | governa | Gestão Documental | Define quem pode acessar e publicar documentos e pastas conforme papel e permissões |
| Comunicação Interna | depende de | Organização Corporativa | Destinatários e escopo de comunicação requerem identificação do colaborador e seu contexto |
| Comunicação Interna | utiliza | Gestão Documental | Busca unificada e comunicados consultam ou referenciam conteúdo documental |
| Comunicação Interna | notifica sobre | Controle de Acesso | Notificações comunicam resultados de processos de acesso (ex.: solicitação de permissão) |
| Gestão Documental | é consultada por | Comunicação Interna | Busca unificada e canais de publicação acessam documentos e pastas como fonte de informação |

---

## Dependências Conceituais

| Contexto | Depende De | Motivo |
| -------- | ---------- | ------ |
| Gestão Documental | Organização Corporativa | Documento e pasta vinculados a escopo de singular, área e colaborador |
| Controle de Acesso | Organização Corporativa | Papel e autorização dependem de contexto organizacional e vínculo do colaborador |
| Comunicação Interna | Organização Corporativa | Colaborador é destinatário de notificações e publicações internas |
| Comunicação Interna | Gestão Documental | Busca unificada e comunicados utilizam conteúdo documental como fonte |
| Gestão Documental | Controle de Acesso | Acesso efetivo a recursos documentais é governado por papéis e permissões |
| Comunicação Interna | Controle de Acesso | Notificações informam resultados de processos de concessão de acesso |

*Organização Corporativa não depende conceitualmente dos demais contextos — ocupa posição upstream no mapa.*

---

## Fluxo de Informação de Negócio

### Contextos que produzem informações

| Contexto | Informações produzidas |
| -------- | ---------------------- |
| Organização Corporativa | Hierarquia federativa (federação, singular, área, equipe); vínculos de colaborador; contexto organizacional; resultado de onboarding |
| Gestão Documental | Documentos e pastas organizados; classificação de visibilidade; regras de compartilhamento; distinção entre recurso privado e recurso público |
| Controle de Acesso | Papéis atribuídos; decisões de autorização; resultado de solicitações de permissão; registros de auditoria; identidade autenticada |
| Comunicação Interna | Notificações; publicações em canais internos; resultados de busca transversal |

### Contextos que consomem informações

| Contexto | Informações consumidas | Origem |
| -------- | ---------------------- | ------ |
| Gestão Documental | Escopo organizacional (singular, área, colaborador) | Organização Corporativa |
| Controle de Acesso | Contexto organizacional; vínculo de colaborador | Organização Corporativa |
| Controle de Acesso | Classificação de recurso privado/público | Gestão Documental |
| Comunicação Interna | Identidade e contexto do colaborador | Organização Corporativa |
| Comunicação Interna | Conteúdo documental | Gestão Documental |
| Comunicação Interna | Resultados de processos de acesso | Controle de Acesso |
| Gestão Documental | Decisões de autorização e papéis | Controle de Acesso |

### Contextos que governam conceitos compartilhados

| Conceito compartilhado | Contexto governante | Contextos que consomem a definição |
| ---------------------- | ------------------- | ------------------------------------ |
| Colaborador | Organização Corporativa | Gestão Documental, Controle de Acesso, Comunicação Interna |
| Contexto organizacional | Organização Corporativa | Controle de Acesso, Gestão Documental |
| Singular, Área, Equipe | Organização Corporativa | Gestão Documental, Controle de Acesso |
| Federação | Organização Corporativa (estrutura); Gestão Documental (escopo de compartilhamento) | Gestão Documental, Organização Corporativa |
| Recurso privado / Recurso público | Gestão Documental (classificação); Controle de Acesso (decisão de acesso) | Controle de Acesso, Gestão Documental |
| Compartilhamento | Gestão Documental (audiência); Controle de Acesso (efetivação de acesso) | Controle de Acesso |
| Comunicado | Gestão Documental (categoria); Comunicação Interna (publicação) — governança em lacuna | Comunicação Interna, Gestão Documental |

### Sequência de negócio (fluxo de valor)

1. **Organização Corporativa** estabelece vínculos e contexto do colaborador (incluindo onboarding).
2. **Controle de Acesso** atribui papel e valida autorização conforme contexto organizacional.
3. **Gestão Documental** publica e organiza conteúdo no escopo definido; classifica visibilidade e compartilhamento.
4. **Controle de Acesso** governa o acesso efetivo a recursos documentais; processa solicitações de permissão.
5. **Comunicação Interna** notifica o colaborador sobre eventos relevantes e disponibiliza canais de informação.

*Sequência derivada do fluxo de valor em `01-vision` e dos relacionamentos em `05-bounded-contexts`.*

---

## Conceitos Compartilhados

| Conceito | Contexto Responsável | Contextos Consumidores |
| -------- | -------------------- | ---------------------- |
| Colaborador | Organização Corporativa | Gestão Documental, Controle de Acesso, Comunicação Interna |
| Contexto organizacional | Organização Corporativa | Controle de Acesso, Gestão Documental |
| Federação | Organização Corporativa | Gestão Documental |
| Singular | Organização Corporativa | Gestão Documental, Controle de Acesso |
| Área | Organização Corporativa | Gestão Documental, Controle de Acesso |
| Equipe | Organização Corporativa | Controle de Acesso |
| Documento | Gestão Documental | Comunicação Interna |
| Recurso privado | Gestão Documental | Controle de Acesso |
| Recurso público | Gestão Documental | Controle de Acesso |
| Compartilhamento | Gestão Documental | Controle de Acesso |
| Comunicado | Gestão Documental (categoria) / Comunicação Interna (publicação) — responsabilidade em lacuna | Comunicação Interna, Gestão Documental |

---

## Donos dos Conceitos

Contexto responsável pela definição oficial de cada conceito, conforme `05-bounded-contexts` Conceitos Exclusivos e Conceitos Compartilhados.

| Conceito | Contexto Responsável |
| -------- | -------------------- |
| Federação | Organização Corporativa |
| Singular | Organização Corporativa |
| Área | Organização Corporativa |
| Equipe | Organização Corporativa |
| Colaborador | Organização Corporativa |
| Contexto organizacional | Organização Corporativa |
| Onboarding | Organização Corporativa |
| Código Unimed | Organização Corporativa |
| Unimed Ceará | Organização Corporativa |
| Documento | Gestão Documental |
| Pasta | Gestão Documental |
| Visibilidade | Gestão Documental |
| Compartilhamento | Gestão Documental |
| Recurso privado | Gestão Documental |
| Recurso público | Gestão Documental |
| Quota de armazenamento | Gestão Documental |
| Conteúdo confidencial | Gestão Documental |
| Papel | Controle de Acesso |
| Autenticação corporativa | Controle de Acesso |
| Solicitação de permissão | Controle de Acesso |
| Responsável pelo recurso | Controle de Acesso |
| Auditoria | Controle de Acesso |
| Administrador global | Controle de Acesso |
| Administrador de singular | Controle de Acesso |
| Administrador de área | Controle de Acesso |
| Proprietário de equipe | Controle de Acesso |
| Convidado | Controle de Acesso |
| Parceiro autorizado | Controle de Acesso |
| Notificação | Comunicação Interna |
| Fique por Dentro | Comunicação Interna |
| Central de Colaboração | Comunicação Interna |
| Busca unificada | Comunicação Interna |
| Métricas administrativas | Comunicação Interna |
| Comunicado | Gestão Documental e Comunicação Interna — dono não estabilizado |

---

## Contextos Centrais

| Contexto | Justificativa |
| -------- | ------------- |
| Organização Corporativa | Contexto upstream; todos os demais dependem de sua estrutura e vínculos; colaborador e contexto organizacional são pré-requisito do fluxo de valor |
| Gestão Documental | Objeto principal do domínio — documentos e pastas são a razão de existência do portal como repositório de comunicação interna |
| Controle de Acesso | Governa a operação efetiva no portal; materializa restrições de confidencialidade e autorização sobre recursos documentais |

---

## Contextos de Suporte

| Contexto | Justificativa |
| -------- | ------------- |
| Comunicação Interna | Habilita notificação, engajamento e localização de conteúdo; depende dos contextos centrais; capacidades periféricas com confiança documentada como baixa a média |

---

## Dependências Críticas

| Dependência | Motivo |
| ----------- | ------ |
| Gestão Documental → Organização Corporativa | Sem escopo organizacional (singular, área, colaborador), documentos e pastas não possuem contexto de negócio válido |
| Controle de Acesso → Organização Corporativa | Sem contexto organizacional e vínculo de colaborador, papéis e autorização perdem referência de escopo |
| Gestão Documental ← Controle de Acesso | Acesso efetivo a recursos documentais depende de governança de papéis e permissões |
| Comunicação Interna → Organização Corporativa + Gestão Documental + Controle de Acesso | Notificações, busca e publicações requerem colaborador, conteúdo documental e eventos de acesso como insumos |
| Colaborador sem área vinculada | Impede operação no portal — dependência crítica entre Organização Corporativa e todos os consumidores |

---

## Fronteiras Sensíveis

| Contextos Envolvidos | Motivo |
| -------------------- | ------ |
| Gestão Documental ↔ Comunicação Interna | Comunicado aparece como categoria de documento e como publicação institucional; fronteira não estabilizada |
| Gestão Documental ↔ Controle de Acesso | Compartilhamento define audiência do recurso; Controle de Acesso efetiva quem acessa — sobreposição de responsabilidades |
| Organização Corporativa ↔ Gestão Documental | Federação usada como estrutura organizacional e como escopo de compartilhamento institucional |
| Controle de Acesso (interno) | Parceiro autorizado vs. Convidado — critérios operacionais não formalizados |
| Organização Corporativa (interno) | Onboarding com fluxos coexistentes (seleção direta vs. solicitação com aprovação) |
| Comunicação Interna (transversal) | Busca unificada consulta Organização Corporativa e Gestão Documental sem ser dona desses conceitos |
| Organização Corporativa (interno) | Equipe com representações divergentes documentadas |

---

## Riscos de Acoplamento Conceitual

| Risco | Contextos Impactados |
| ----- | -------------------- |
| Comunicado tratado como conceito único sem qualificação de contexto | Gestão Documental, Comunicação Interna |
| Federação com significado duplo (estrutura vs. escopo de compartilhamento) | Organização Corporativa, Gestão Documental |
| Compartilhamento confundido com autorização efetiva | Gestão Documental, Controle de Acesso |
| Colaborador equiparado a identidade de acesso genérica ("usuário") | Organização Corporativa, Controle de Acesso |
| Parceiro autorizado equiparado a Convidado | Controle de Acesso |
| Busca unificada absorvendo responsabilidade de conceitos que não possui | Comunicação Interna, Organização Corporativa, Gestão Documental |
| Solicitação de permissão incompleta gerando expectativa de governança não atendida | Controle de Acesso, Gestão Documental |

---

## Lacunas Restantes

| Lacuna | Impacto |
| ------ | ------- |
| Definição operacional de parceiro autorizado vs. convidado | Fronteira do Controle de Acesso para perfis externos não estabilizada no mapa |
| Distinção formal entre comunicado (categoria) e comunicado (módulo corporativo) | Relação Gestão Documental ↔ Comunicação Interna indefinida |
| Fluxo consolidado de onboarding (seleção vs. solicitação) | Dependência upstream de Organização Corporativa ambígua |
| Escopo da Central de Colaboração | Subdomínio de Comunicação Interna sem definição de negócio |
| Solicitação de permissões sem confirmação completa | Dependência crítica Controle de Acesso ↔ Gestão Documental pode estar incompleta |
| Representações divergentes de equipe | Pode afetar posição de Organização Corporativa como contexto upstream |
| Vocabulário de métricas administrativas | Relação Comunicação Interna com gestão do portal sem léxico confirmado |
| Dono do conceito Comunicado não estabilizado | Governança de conceito compartilhado indefinida no mapa |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
| ----- | ------ | ------------- |
| Alto | Contextos centrais (Organização Corporativa, Gestão Documental, Controle de Acesso); dependências upstream; donos de conceitos exclusivos; sequência do fluxo de valor | Relações estáveis e convergentes em `05-bounded-contexts` e `04-domain-concepts` |
| Médio | Relacionamentos com Comunicação Interna; conceitos compartilhados (Federação, Compartilhamento, Recurso privado/público); fronteiras sensíveis | Relações documentadas com sobreposições e dependências cruzadas |
| Baixo | Comunicação Interna como contexto de suporte; dono do Comunicado; Central de Colaboração; Métricas administrativas | Capacidades parciais; ambiguidades residuais não resolvidas |

A classificação geral é **Médio-Alto** porque o mapa do núcleo (organização → acesso → documentos) está estável e coerente, enquanto as fronteiras envolvendo Comunicação Interna e conceitos compartilhados ambíguos permanecem sujeitas a validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/05-bounded-contexts.md`
- `docs/domain/04-domain-concepts.md`
- `docs/domain/03-ubiquitous-language.md`

*Nenhuma fonte adicional foi necessária. Contextos, relacionamentos, conceitos compartilhados, fronteiras e lacunas foram consolidados exclusivamente a partir dos documentos de domínio aprovados, conforme a Regra de Ouro.*
