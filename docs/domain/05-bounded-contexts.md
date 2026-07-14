# Bounded Contexts

## Objetivo

Identificar os possíveis Bounded Contexts do **Portal de Comunicação** — fronteiras linguísticas e conceituais onde a linguagem, as regras, as responsabilidades e o significado dos conceitos são coerentes entre si.

Este documento consolida os agrupamentos naturais e conceitos aprovados em `04-domain-concepts.md`, `03-ubiquitous-language.md`, `02-business-glossary.md` e `01-vision.md`. Não define arquitetura, microsserviços, módulos técnicos, agregados ou eventos de domínio.

---

## Critérios Utilizados

Os contextos foram identificados com base nos critérios abaixo, aplicados exclusivamente sobre conceitos e relações já aprovados:

| Critério | Descrição |
| -------- | --------- |
| Linguagem própria | Conjunto de termos com significado consistente dentro da fronteira |
| Regras próprias | Conjunto de restrições e comportamentos coerentes entre si |
| Conceitos próprios | Conceitos cuja definição de negócio é primariamente exercida no contexto |
| Responsabilidades próprias | Propósito distinto e reconhecível no domínio |
| Agrupamentos naturais evidenciados | Coerência com os quatro agrupamentos documentados em `04-domain-concepts` |
| Independência conceitual | Capacidade de descrever o contexto sem depender de vocabulário técnico ou de estrutura de código |

**Critérios explicitamente excluídos:** menus, telas, módulos de código, APIs, tabelas de banco de dados e divisões de infraestrutura.

---

## Candidatos Identificados

| Contexto | Justificativa |
| -------- | ------------- |
| Organização Corporativa | Hierarquia federativa multi-singular, vínculos de colaboradores e integração ao contexto organizacional possuem linguagem, regras e responsabilidades distintas da gestão de conteúdo |
| Gestão Documental | Publicação, organização, visibilidade e compartilhamento de documentos e pastas constituem vocabulário e regras próprias de exposição e audiência de recursos |
| Controle de Acesso | Papéis, autorização por escopo, solicitações de permissão, auditoria e autenticação corporativa formam fronteira de governança distinta da estrutura organizacional e do conteúdo |
| Comunicação Interna | Notificações, canais de publicação, busca transversal e engajamento entre colaboradores possuem responsabilidades de informação e interação distintas dos demais contextos |

---

## Bounded Contexts Propostos

### Organização Corporativa

#### Objetivo

Estruturar e manter a hierarquia federativa multi-singular da Unimed Ceará, os vínculos operacionais de colaboradores e o contexto organizacional que orienta a operação no portal.

#### Conceitos Principais

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

#### Responsabilidades

- Definir e manter a hierarquia Federação → Singular → Área → Equipe
- Vincular colaboradores a singular e área (e eventualmente equipe)
- Estabelecer o contexto organizacional que delimita a visão do colaborador
- Integrar novos colaboradores ao contexto adequado por meio do onboarding
- Identificar singulares por meio do código Unimed

#### Regras Relevantes

- Área pertence a uma singular; equipe pertence a uma área
- Colaborador possui vínculo operacional a singular e área
- Colaborador sem área vinculada pode ser impedido de operar no portal
- Onboarding vincula colaborador à singular e área adequadas
- Singular agrupa áreas, colaboradores e documentos no escopo organizacional

#### Linguagem Característica

Federação, singular, área, equipe, colaborador, contexto organizacional, onboarding, código Unimed, vínculo organizacional, integração de novos colaboradores.

#### Evidências

- `04-domain-concepts` — Agrupamento "Organização corporativa multi-singular"; Conceitos de Estrutura Organizacional
- `03-ubiquitous-language` — Vocabulário por Contexto: Estrutura Organizacional
- `01-vision` — Contexto Organizacional; Fluxo de Valor (passos 1–3)
- `02-business-glossary` — Escopo "Organização corporativa"

---

### Gestão Documental

#### Objetivo

Publicar, organizar e controlar a exposição de documentos e pastas no portal, definindo visibilidade, compartilhamento e limites de armazenamento conforme escopo organizacional.

#### Conceitos Principais

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

#### Responsabilidades

- Publicar e organizar documentos em pastas hierárquicas
- Classificar a exposição de documentos e pastas por visibilidade (público ou privado por escopo)
- Definir regras de compartilhamento que determinam quem pode acessar cada recurso
- Distinguir recursos privados de recursos públicos
- Controlar quotas de armazenamento por colaborador
- Aplicar restrições de conteúdo confidencial e uso profissional

#### Regras Relevantes

- Documento é organizado em pasta e vinculado a escopo organizacional (singular, área)
- Pasta pode ser organizada no contexto de singular, área ou ser pessoal de colaborador
- Visibilidade classifica exposição de documento ou pasta — público ou privado conforme escopo
- Compartilhamento define acesso a documento ou pasta (pessoal, setor, federação, singulares ou colaboradores específicos)
- Recurso privado é documento ou pasta com acesso restrito; recurso público é acessível sem restrição de escopo privado
- Informações do portal são confidenciais e de uso profissional

#### Linguagem Característica

Documento, pasta, visibilidade, compartilhamento, recurso privado, recurso público, quota de armazenamento, conteúdo confidencial, publicação, organização hierárquica, escopo documental.

#### Evidências

- `04-domain-concepts` — Agrupamento "Gestão e compartilhamento documental"; Conceitos de Gestão Documental
- `03-ubiquitous-language` — Vocabulário por Contexto: Gestão Documental
- `01-vision` — Problema de Negócio; Restrições de visibilidade e compartilhamento
- `02-business-glossary` — Escopo "Gestão documental"

---

### Controle de Acesso

#### Objetivo

Goverar quem pode acessar e operar no portal, por meio de papéis, escopo organizacional, fluxos formais de concessão de acesso, autenticação corporativa e auditoria.

#### Conceitos Principais

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

#### Responsabilidades

- Atribuir papéis de negócio que determinam o que uma pessoa pode fazer e em qual escopo
- Autenticar colaboradores por credenciais de e-mail corporativo
- Formalizar pedidos de acesso a recursos privados por solicitação de permissão
- Permitir que o responsável pelo recurso aprove ou negue solicitações
- Registrar eventos de controle de acesso e alterações relevantes em auditoria
- Administrar perfis de acesso restrito (convidado, parceiro autorizado)
- Exercer gestão administrativa em escopos global, singular, área e equipe

#### Regras Relevantes

- Autorização depende de papel e contexto organizacional (singular, área, equipe)
- Acesso restrito a colaboradores e parceiros autorizados da Unimed Ceará
- Autenticação vinculada a domínios de e-mail corporativos
- Solicitação de permissão é submetida ao responsável pelo recurso
- Convidado possui acesso restrito a documentos e conteúdos públicos
- Auditoria registra eventos de papéis e controle de acesso

#### Linguagem Característica

Papel, autenticação corporativa, solicitação de permissão, responsável pelo recurso, auditoria, administrador, convidado, parceiro autorizado, autorização, escopo de acesso, governança.

#### Evidências

- `04-domain-concepts` — Agrupamento "Autorização e governança de acesso"; Conceitos de Controle de Acesso
- `03-ubiquitous-language` — Vocabulário por Contexto: Controle de Acesso
- `01-vision` — Fluxo de Valor (passos 5–7); Dependências de Negócio; Restrições de autorização
- `02-business-glossary` — Escopo "Controle de acesso"

---

### Comunicação Interna

#### Objetivo

Informar, notificar e engajar colaboradores por meio de canais de comunicação interna, publicações institucionais e mecanismos de localização transversal de conteúdo e pessoas.

#### Conceitos Principais

| Conceito |
| -------- |
| Notificação |
| Comunicado |
| Fique por Dentro |
| Central de Colaboração |
| Busca unificada |
| Métricas administrativas |

#### Responsabilidades

- Comunicar eventos relevantes ao colaborador por meio de notificações
- Disponibilizar canal de publicações e informações internas (Fique por Dentro)
- Publicar comunicados institucionais de comunicação corporativa
- Oferecer espaço de interação entre colaboradores (Central de Colaboração)
- Permitir pesquisa transversal em documentos, áreas, singulares e colaboradores
- Exibir indicadores de gestão e acompanhamento do portal para administração

#### Regras Relevantes

- Notificação é dirigida ao colaborador e comunica resultados de processos relevantes (ex.: resultado de solicitação de permissão)
- Comunicado é tipo de conteúdo institucional de comunicação corporativa (requer qualificação de contexto)
- Fique por Dentro destina-se a colaboradores como feed de informações internas
- Busca unificada abrange documentos, áreas, singulares e colaboradores

#### Linguagem Característica

Notificação, comunicado, fique por dentro, central de colaboração, busca unificada, métricas administrativas, publicação interna, engajamento, informação institucional.

#### Evidências

- `04-domain-concepts` — Agrupamento "Comunicação e engajamento interno"; Conceitos de Comunicação Interna
- `03-ubiquitous-language` — Vocabulário por Contexto: Comunicação Interna
- `01-vision` — Capacidades de Negócio (notificações, comunicados, fique por dentro, busca, analytics, colaboração)
- `02-business-glossary` — Escopo "Comunicação interna"

---

## Relacionamentos Entre Contextos

| Contexto Origem | Relacionamento | Contexto Destino |
| --------------- | -------------- | ---------------- |
| Organização Corporativa | fornece informações para | Gestão Documental |
| Organização Corporativa | fornece informações para | Controle de Acesso |
| Organização Corporativa | fornece informações para | Comunicação Interna |
| Gestão Documental | depende de | Organização Corporativa |
| Controle de Acesso | depende de | Organização Corporativa |
| Controle de Acesso | governa | Gestão Documental |
| Comunicação Interna | depende de | Organização Corporativa |
| Comunicação Interna | utiliza | Gestão Documental |
| Comunicação Interna | notifica sobre | Controle de Acesso |
| Gestão Documental | é consultada por | Comunicação Interna |

*Relações derivadas do fluxo de valor em `01-vision` e dos relacionamentos conceituais em `04-domain-concepts`.*

---

## Conceitos Compartilhados

Conceitos que atravessam múltiplos contextos com significado relacionado, mas responsabilidade primária em um deles.

| Conceito | Contextos Relacionados |
| -------- | ---------------------- |
| Colaborador | Organização Corporativa (vínculo); Gestão Documental (autoria); Controle de Acesso (papel); Comunicação Interna (destinatário) |
| Contexto organizacional | Organização Corporativa (definição); Controle de Acesso (autorização); Gestão Documental (escopo de visão) |
| Federação | Organização Corporativa (estrutura); Gestão Documental (escopo de compartilhamento institucional) |
| Singular | Organização Corporativa (estrutura); Gestão Documental (escopo documental); Controle de Acesso (escopo de papel) |
| Área | Organização Corporativa (estrutura); Gestão Documental (escopo documental); Controle de Acesso (escopo de papel) |
| Equipe | Organização Corporativa (agrupamento); Controle de Acesso (escopo de papel do proprietário de equipe) |
| Recurso privado / Recurso público | Gestão Documental (classificação); Controle de Acesso (decisão de acesso e solicitação) |
| Comunicado | Gestão Documental (categoria de documento); Comunicação Interna (publicação institucional) |

---

## Conceitos Exclusivos

Conceitos cuja responsabilidade primária pertence predominantemente a um único contexto.

| Contexto | Conceitos Exclusivos |
| -------- | -------------------- |
| Organização Corporativa | Onboarding, Código Unimed, Unimed Ceará |
| Gestão Documental | Pasta, Visibilidade, Compartilhamento, Quota de armazenamento, Conteúdo confidencial |
| Controle de Acesso | Papel, Autenticação corporativa, Solicitação de permissão, Responsável pelo recurso, Auditoria, Convidado, Parceiro autorizado, Administrador global, Administrador de singular, Administrador de área, Proprietário de equipe |
| Comunicação Interna | Notificação, Fique por Dentro, Central de Colaboração, Busca unificada, Métricas administrativas |

*Conceitos compartilhados (Colaborador, Documento, Federação, Singular, Área, Equipe, Comunicado) possuem papel primário em um contexto e são referenciados nos demais.*

---

## Pontos de Atenção

| Item | Justificativa |
| ---- | ------------- |
| Comunicado atravessa Gestão Documental e Comunicação Interna | Aparece como categoria de documento e como módulo corporativo distinto; fronteira entre contextos não estabilizada |
| Federação com duplo sentido | Escopo de compartilhamento institucional (Gestão Documental) e identificador organizacional (Organização Corporativa) |
| Parceiro autorizado vs. Convidado | Ambos no Controle de Acesso, mas critérios operacionais e distinção institucional não formalizados |
| Onboarding com fluxos coexistentes | Processo de Organização Corporativa com modelos de seleção direta e solicitação com aprovação não consolidados |
| Compartilhamento na fronteira Gestão Documental / Controle de Acesso | Define audiência do recurso (Gestão Documental) mas materializa decisão de quem acessa (Controle de Acesso) |
| Busca unificada como conceito transversal | Pertence a Comunicação Interna, mas consulta conceitos de Organização Corporativa e Gestão Documental |
| Equipe com representações divergentes documentadas | Pode afetar a fronteira de Organização Corporativa |
| Solicitação de permissão com capacidade parcial | Fluxo de Controle de Acesso descrito no produto sem confirmação completa de operação |

---

## Bounded Contexts com Baixa Confiança

| Contexto | Motivo |
| -------- | ------ |
| Comunicação Interna (no todo) | Capacidades periféricas (comunicados, fique por dentro, central de colaboração, métricas) com status parcial na visão de domínio |
| Comunicação Interna — Central de Colaboração | Escopo de interação entre colaboradores indefinido; sem processo de negócio documentado |
| Comunicação Interna — Métricas administrativas | Indicadores sem léxico confirmado e sem modelo de negócio de ponta a ponta |
| Comunicação Interna — Comunicado | Sobreposição com Gestão Documental; incerteza se é conceito próprio ou categoria de documento |

*Os contextos Organização Corporativa, Gestão Documental e Controle de Acesso possuem confiança alta. Comunicação Interna permanece como proposta com ressalvas.*

---

## Lacunas Restantes

| Lacuna | Impacto |
| ------ | ------- |
| Definição operacional de parceiro autorizado vs. convidado | Fronteira do Controle de Acesso para perfis externos não estabilizada |
| Distinção formal entre comunicado (categoria) e comunicado (módulo corporativo) | Fronteira entre Gestão Documental e Comunicação Interna indefinida |
| Fluxo consolidado de onboarding (seleção vs. solicitação) | Processo de Organização Corporativa ambíguo |
| Escopo da Central de Colaboração | Subdomínio de Comunicação Interna sem definição de negócio |
| Solicitação de permissões sem confirmação completa | Processo central do Controle de Acesso pode estar incompleto |
| Representações divergentes de equipe | Pode exigir revisão da fronteira de Organização Corporativa |
| Vocabulário de métricas administrativas | Subdomínio de Comunicação Interna sem léxico confirmado |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
| ----- | ------ | ------------- |
| Alto | Organização Corporativa, Gestão Documental, Controle de Acesso | Agrupamentos naturais evidenciados em `04-domain-concepts`; vocabulário, regras e responsabilidades distintas e convergentes nos documentos aprovados |
| Médio | Relacionamentos entre contextos; conceitos compartilhados (Colaborador, Federação, Comunicado); fronteira Compartilhamento/Controle de Acesso | Relações derivadas do fluxo de valor, mas com sobreposições documentadas |
| Baixo | Comunicação Interna como um todo; Central de Colaboração; Métricas administrativas; Comunicado como conceito exclusivo | Capacidades com status parcial; ambiguidades residuais não resolvidas |

A classificação geral é **Médio-Alto** porque três contextos do núcleo (organização, documentos, acesso) possuem fronteiras linguísticas e conceituais estáveis, enquanto o contexto de Comunicação Interna e algumas fronteiras entre contextos permanecem sujeitas a validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/04-domain-concepts.md`
- `docs/domain/03-ubiquitous-language.md`
- `docs/domain/02-business-glossary.md`
- `docs/domain/01-vision.md`

*Nenhuma fonte adicional foi necessária. Bounded Contexts, relacionamentos, conceitos compartilhados e lacunas foram consolidados exclusivamente a partir dos documentos de domínio aprovados, conforme a Regra de Ouro.*
