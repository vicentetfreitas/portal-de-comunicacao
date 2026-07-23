# Conceitos de Domínio

## Objetivo

Identificar e organizar os conceitos centrais do domínio do **Portal de Comunicação**, consolidando o que já foi aprovado em `01-vision.md`, `02-business-glossary.md` e `03-ubiquitous-language.md`.

Este documento mapeia os conceitos de negócio e suas relações — não modela entidades técnicas, tabelas, classes, APIs ou componentes. Serve como base para documentos posteriores de Bounded Contexts, Domain Events, Aggregates e Regras de Negócio.

---

## Visão Geral dos Conceitos

O domínio do Portal de Comunicação organiza-se em quatro grupos conceituais sustentados pelos documentos aprovados:

| Grupo Conceitual | Descrição | Conceitos Principais |
| ---------------- | --------- | -------------------- |
| Estrutura Organizacional | Hierarquia federativa multi-singular que estrutura pessoas, escopos e vínculos operacionais | Federação, Singular, Área, Equipe, Colaborador, Contexto organizacional, Onboarding |
| Gestão Documental | Publicação, organização e exposição controlada de artefatos de comunicação | Documento, Pasta, Visibilidade, Compartilhamento, Recurso privado, Recurso público |
| Controle de Acesso | Autorização por papéis, escopo organizacional e fluxos formais de concessão de acesso | Papel, Solicitação de permissão, Responsável pelo recurso, Auditoria, Autenticação corporativa |
| Comunicação Interna | Canais e mecanismos de informação, notificação e interação entre colaboradores | Notificação, Comunicado, Fique por Dentro, Central de Colaboração, Busca unificada |

O **Portal de Comunicação** e a **Unimed Ceará** enquadram o domínio como produto e contexto institucional, respectivamente.

---

## Conceitos Fundamentais

Conceitos centrais que sustentam o funcionamento do domínio, conforme `01-vision` Conceitos Centrais e candidatos da linguagem ubíqua.

| Conceito | Descrição | Importância para o Domínio |
| -------- | --------- | -------------------------- |
| Singular | Unidade organizacional da federação que agrupa áreas, colaboradores e documentos | Núcleo da estrutura multi-unidade; delimita escopo organizacional |
| Área | Setor ou departamento de nível único vinculado a uma singular (ou à federação); delimita escopo de documentos, pastas e colaboradores | Unidade departamental; o detalhamento operacional é feito por Equipes |
| Equipe | Agrupamento operacional de colaboradores dentro de uma área | Detalhamento operacional da área (DEC-DB-022) |
| Colaborador | Pessoa com vínculo operacional a singular e área, identificada no portal para consulta e publicação de conteúdo conforme permissões | Ator central do fluxo de valor do portal |
| Documento | Artefato de comunicação ou arquivo gerenciado no portal, com visibilidade e escopo organizacional definidos | Objeto principal de comunicação e gestão documental |
| Pasta | Estrutura hierárquica que organiza documentos por contexto organizacional ou pessoal | Organização hierárquica essencial para localização e controle de acesso |
| Compartilhamento | Regra de negócio que define quem pode acessar um documento ou pasta (pessoal, setor, federação, singulares ou colaboradores específicos) | Define a audiência de cada recurso documental |
| Visibilidade | Nível de exposição de um documento ou pasta — público ou privado conforme escopo (singular, área, colaborador) | Classifica a exposição de recursos documentais |
| Papel | Papel de negócio que determina o que uma pessoa pode fazer no portal e em qual escopo organizacional | Vocabulário unificado para autorização por perfil de negócio |
| Contexto organizacional | Combinação de singular, área e equipe que delimita a visão e as ações do colaborador no portal | Orienta navegação, autorização e experiência operacional |

---

## Conceitos de Estrutura Organizacional

| Conceito | Papel no Domínio | Relacionamentos |
| -------- | ---------------- | --------------- |
| Federação | Conjunto organizacional mais amplo no qual a Unimed Ceará e outras singulares coexistem; delimita escopo de compartilhamento institucional | Singular pertence/compõe Federação |
| Singular | Unidade organizacional que agrupa áreas, colaboradores e documentos | Pertence à Federação; contém Áreas; vincula Colaboradores e Documentos |
| Área | Setor departamental que delimita escopo de documentos, pastas e colaboradores | Pertence a Singular; contém Equipes; vincula Colaboradores e Documentos |
| Equipe | Agrupamento operacional dentro de uma área | Pertence a Área; Colaborador pode pertencer a Equipe |
| Colaborador | Pessoa com vínculo operacional a singular e área | Vinculado a Singular e Área; pode pertencer a Equipe; possui Contexto organizacional |
| Contexto organizacional | Combinação de singular, área e equipe que delimita visão e ações do colaborador | Delimita visão do Colaborador |
| Código Unimed | Identificador da unidade cooperativa associada a uma singular | Associado a Singular |
| Onboarding | Processo de vinculação inicial do colaborador à singular e área adequadas | Vincula Colaborador; seleciona Singular e Área |
| Unimed Ceará | Organização de saúde cooperativa proprietária e operadora do portal | Contexto institucional; delimita política de acesso |

---

## Conceitos de Gestão Documental

| Conceito | Papel no Domínio | Relacionamentos |
| -------- | ---------------- | --------------- |
| Documento | Artefato de comunicação ou arquivo com visibilidade e escopo organizacional definidos | Organizado em Pasta; vinculado a Singular e Área (escopo); publicado por Colaborador; classificado por Visibilidade e Compartilhamento |
| Pasta | Estrutura hierárquica que organiza documentos por contexto organizacional ou pessoal | Contém Documentos; organizada no contexto de Singular e Área; pode ser pessoal de Colaborador |
| Visibilidade | Nível de exposição de documento ou pasta — público ou privado por escopo | Classifica exposição de Documento e Pasta |
| Compartilhamento | Regra de negócio que define quem pode acessar documento ou pasta | Define acesso a Documento e Pasta |
| Recurso privado | Documento ou pasta com acesso restrito a escopo ou pessoas definidas | Destino de Solicitação de permissão |
| Recurso público | Documento ou conteúdo acessível sem restrição de escopo privado | Acessado por Convidado |
| Quota de armazenamento | Limite de espaço atribuído ao colaborador para armazenamento de documentos | Limita uso de armazenamento do Colaborador |
| Conteúdo confidencial | Informação de uso restrito e profissional, não destinada a divulgação externa | Restrição aplicável a Documentos e conteúdos do portal |

---

## Conceitos de Controle de Acesso

| Conceito | Papel no Domínio | Relacionamentos |
| -------- | ---------------- | --------------- |
| Papel | Papel de negócio que determina o que uma pessoa pode fazer no portal e em qual escopo organizacional | Atribuído a Colaborador; eventos registrados em Auditoria |
| Administrador global | Responsável pela gestão completa do portal, usuários, estrutura organizacional e auditoria | Gestão institucional do portal |
| Administrador de singular | Responsável pela gestão de uma singular, suas áreas vinculadas e colaboradores no escopo | Administra Singular |
| Administrador de área | Responsável pela gestão de uma área, suas equipes, colaboradores e documentos do setor | Administra Área |
| Proprietário de equipe | Responsável pela gestão de uma equipe, seus membros e documentos no escopo do time | Gestão de Equipe |
| Convidado | Pessoa com perfil de acesso restrito a documentos e conteúdos públicos | Acessa Recurso público |
| Parceiro autorizado | Pessoa externa à operação cotidiana, com acesso restrito conforme política institucional do portal | Política institucional de acesso (relação operacional em lacuna) |
| Solicitação de permissão | Pedido formal de acesso a recurso privado, aguardando decisão do responsável | Solicita acesso a Recurso privado; submetida a Responsável pelo recurso |
| Responsável pelo recurso | Pessoa com autoridade para aprovar ou negar solicitação de acesso a recurso privado | Decide Solicitação de permissão |
| Auditoria | Registro consultável de eventos de controle de acesso e alterações relevantes | Registra eventos de Papel e controle de acesso |
| Autenticação corporativa | Identificação do colaborador por meio de credenciais de e-mail da organização | Pré-requisito de acesso do Colaborador ao portal |

---

## Conceitos de Comunicação Interna

| Conceito | Papel no Domínio | Relacionamentos |
| -------- | ---------------- | --------------- |
| Notificação | Comunicação de evento relevante dirigida ao colaborador dentro do portal | Dirigida a Colaborador; comunica resultados de processos (ex.: solicitação de permissão) |
| Comunicado | Tipo de conteúdo institucional de comunicação corporativa | Relacionado a Documento (categoria) e a módulo corporativo (ambiguidade residual) |
| Fique por Dentro | Canal de publicações e informações internas destinado a colaboradores | Feed de informações para Colaboradores |
| Central de Colaboração | Espaço de interação entre colaboradores no portal | Interação entre Colaboradores (escopo em lacuna) |
| Busca unificada | Pesquisa transversal em documentos, áreas, singulares e colaboradores | Localiza Documentos, Áreas, Singulares e Colaboradores |
| Métricas administrativas | Indicadores de gestão e acompanhamento do portal para administração | Suporte à gestão institucional do portal (indicadores em lacuna) |

---

## Relacionamentos Conceituais

Relações evidenciadas em `02-business-glossary` Termos Relacionados e fluxo de valor de `01-vision`.

| Conceito Origem | Relacionamento | Conceito Destino |
| --------------- | -------------- | ---------------- |
| Singular | pertence / compõe | Federação |
| Área | pertence a | Singular |
| Equipe | pertence a | Área |
| Colaborador | vinculado a | Singular |
| Colaborador | vinculado a | Área |
| Colaborador | pode pertencer a | Equipe |
| Documento | vinculado a (escopo) | Singular |
| Documento | vinculado a (escopo) | Área |
| Documento | organizado em | Pasta |
| Documento | publicado por / de autoria | Colaborador |
| Pasta | organizada no contexto de | Singular |
| Pasta | organizada no contexto de | Área |
| Pasta | pode ser pessoal de | Colaborador |
| Compartilhamento | define acesso a | Documento |
| Compartilhamento | define acesso a | Pasta |
| Visibilidade | classifica exposição de | Documento |
| Visibilidade | classifica exposição de | Pasta |
| Papel | atribuído a | Colaborador |
| Contexto organizacional | delimita visão de | Colaborador |
| Solicitação de permissão | solicita acesso a | Recurso privado |
| Solicitação de permissão | submetida a | Responsável pelo recurso |
| Notificação | dirigida a | Colaborador |
| Auditoria | registra eventos de | Papel |
| Onboarding | vincula | Colaborador |
| Onboarding | seleciona | Singular |
| Onboarding | seleciona | Área |
| Administrador de área | administra | Área |
| Administrador de singular | administra | Singular |
| Convidado | acessa | Recurso público |
| Quota de armazenamento | limita uso de | Colaborador |

---

## Conceitos Centrais do Núcleo do Domínio

Conceitos indispensáveis para o funcionamento do sistema, conforme problema de negócio e fluxo de valor em `01-vision`.

| Conceito | Justificativa |
| -------- | ------------- |
| Colaborador | Ator central do fluxo de valor; toda operação cotidiana do portal parte do vínculo organizacional e das permissões do colaborador |
| Singular | Unidade base da estrutura federativa multi-singular; delimita escopo de pessoas, documentos e gestão administrativa |
| Área | Delimita escopo departamental de documentos, pastas e colaboradores; requisito para operação no portal |
| Documento | Objeto principal de comunicação e gestão documental; razão de existência do portal como repositório de informação |
| Pasta | Organização hierárquica que estrutura documentos e suporta regras de visibilidade e compartilhamento |
| Compartilhamento | Regra de negócio que define quem acessa cada recurso; materializa o controle de audiência no domínio |
| Visibilidade | Classificação público/privado por escopo; expressa o nível de exposição de cada recurso documental |
| Papel | Determina o que cada pessoa pode fazer no portal e em qual escopo organizacional |
| Contexto organizacional | Combina singular, área e equipe para orientar visão, navegação e autorização do colaborador |

---

## Conceitos de Suporte

Conceitos auxiliares que habilitam processos e governança sem constituir o núcleo estrutural do domínio.

| Conceito | Papel de Suporte |
| -------- | ---------------- |
| Federação | Delimita escopo institucional de compartilhamento entre singulares |
| Equipe | Detalhamento operacional da área (nível único de área; DEC-DB-022) |
| Onboarding | Integra novos colaboradores ao contexto organizacional adequado |
| Solicitação de permissão | Formaliza concessão de acesso a recursos privados quando não há permissão direta |
| Responsável pelo recurso | Exerce decisão de aprovação ou negação em solicitações de permissão |
| Notificação | Comunica eventos relevantes e resultados de processos ao colaborador |
| Auditoria | Garante rastreabilidade de eventos de controle de acesso |
| Autenticação corporativa | Identifica o colaborador por credenciais de e-mail da organização |
| Recurso privado / Recurso público | Classificam recursos documentais quanto à restrição de acesso |
| Quota de armazenamento | Controla limite de espaço por colaborador |
| Código Unimed | Identifica unidade cooperativa associada a uma singular |
| Conteúdo confidencial | Expressa restrição de uso profissional e não divulgação externa |
| Busca unificada | Facilita localização transversal de conteúdo e pessoas |
| Administrador global / de singular / de área / Proprietário de equipe | Papéis de gestão administrativa em diferentes escopos |
| Convidado / Parceiro autorizado | Perfis de acesso com escopo restrito ou institucional |

---

## Conceitos com Ambiguidade Residual

Ambiguidades ainda não resolvidas, herdadas dos documentos aprovados. Não foram criadas novas classificações.

| Conceito | Ambiguidade | Impacto |
| -------- | ----------- | ------- |
| Parceiro autorizado | Sem definição operacional distinta de Convidado; critérios de elegibilidade não formalizados | Impossibilita vocabulário unificado para acesso externo |
| Convidado | Papel com acesso público limitado, mas política institucional menciona "parceiros autorizados" sem critérios detalhados | Sobreposição entre perfil operacional e termo institucional |
| Comunicado | Aparece como categoria de documento e como módulo corporativo distinto | Incerteza se comunicado é tipo de documento ou conceito próprio |
| Onboarding | Pode significar seleção direta de singular/área ou fluxo de solicitação com aprovação administrativa | Processo de integração de novos colaboradores ambíguo |
| Equipe | Referenciada como agrupamento organizacional, com representações divergentes documentadas | Risco de inconsistência na gestão de times |
| Federação | Usada como escopo de compartilhamento (Unimed Ceará) e como identificador organizacional em navegação | Pode confundir escopo institucional com unidade singular |
| Solicitação de permissão | Fluxo descrito na documentação do produto com capacidade parcial confirmada | Concessão de acesso a recursos privados pode estar incompleta |
| Central de Colaboração | Nome de interface sem escopo de colaboração estabilizado | Interação entre colaboradores indefinida como conceito |
| Métricas administrativas | Indicadores exibidos sem léxico de indicadores confirmado | Gestão do portal sem vocabulário de métricas estabilizado |

---

## Possíveis Agrupamentos Naturais

Agrupamentos observados no domínio, sem utilizar terminologia de Bounded Context. Preparam o terreno para `05-bounded-contexts.md`.

| Agrupamento | Conceitos Relacionados | Evidência |
| ----------- | ---------------------- | --------- |
| Organização corporativa multi-singular | Federação, Singular, Área, Equipe, Colaborador, Contexto organizacional, Código Unimed, Onboarding | `01-vision` Contexto Organizacional; `03-ubiquitous-language` Vocabulário por Contexto — Estrutura Organizacional |
| Gestão e compartilhamento documental | Documento, Pasta, Visibilidade, Compartilhamento, Recurso privado, Recurso público, Quota de armazenamento, Conteúdo confidencial | `01-vision` Problema de Negócio; `03-ubiquitous-language` Vocabulário por Contexto — Gestão Documental |
| Autorização e governança de acesso | Papel, Solicitação de permissão, Responsável pelo recurso, Auditoria, Autenticação corporativa, Administradores, Convidado, Parceiro autorizado | `01-vision` Fluxo de Valor; `03-ubiquitous-language` Vocabulário por Contexto — Controle de Acesso |
| Comunicação e engajamento interno | Notificação, Comunicado, Fique por Dentro, Central de Colaboração, Busca unificada, Métricas administrativas | `01-vision` Capacidades de Negócio; `03-ubiquitous-language` Vocabulário por Contexto — Comunicação Interna |

---

## Lacunas de Conhecimento

Lacunas herdadas dos documentos aprovados, sem inferência adicional.

| Lacuna | Impacto |
| ------ | ------- |
| Definição operacional de "parceiro autorizado" vs. "convidado" | Impossibilita vocabulário unificado para acesso externo |
| Distinção formal entre "comunicado" (categoria) e "comunicado" (módulo corporativo) | Risco de duplicidade conceitual na comunicação interna |
| Termo consolidado para o fluxo de aprovação no onboarding (seleção vs. solicitação) | Integração de novos colaboradores sem vocabulário de processo único |
| Escopo e critérios de "parceiro autorizado" não detalhados | Limite entre colaborador e parceiro externo não formalizado |
| Relação operacional entre equipe como agrupamento e representações alternativas | Modelo organizacional de equipes pode gerar inconsistência |
| Solicitação de permissões sem confirmação completa de persistência | Fluxo de aprovação pode estar incompleto |
| Central de Colaboração sem entidade ou processo documentado | Escopo de colaboração entre colaboradores indefinido |
| Comunicados e métricas administrativas sem modelo de negócio confirmado de ponta a ponta | Capacidades periféricas não verificáveis integralmente |
| Vocabulário oficial para "métricas administrativas" e indicadores exibidos | Gestão do portal sem léxico de indicadores confirmado |
| Processo de negócio para convidados além do acesso a conteúdo público | Gestão de convidados parcialmente evidenciada |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
| ----- | ------ | ------------- |
| Alto | Estrutura organizacional (singular, área, equipe, colaborador), gestão documental (documento, pasta, visibilidade, compartilhamento), papéis, notificações, auditoria, relacionamentos do núcleo | Conceitos e relações convergentes em `01-vision`, `02-business-glossary` e `03-ubiquitous-language` |
| Médio | Onboarding, solicitação de permissão, convidado, parceiro autorizado, federação, comunicado, fique por dentro, busca unificada | Conceitos aprovados com ambiguidade ou lacuna documentada |
| Baixo | Central de colaboração, métricas administrativas, distinção operacional comunicado (módulo vs. categoria) | Evidência parcial; capacidades com status parcial na visão de domínio |

A classificação geral é **Médio-Alto** porque o mapa conceitual do núcleo organizacional e documental está estável e relacionalmente coerente, enquanto conceitos de processos periféricos e perfis externos permanecem sujeitos a validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/03-ubiquitous-language.md`
- `docs/domain/02-business-glossary.md`
- `docs/domain/01-vision.md`

*Nenhuma fonte adicional foi necessária. Conceitos, relações, ambiguidades e lacunas foram consolidados exclusivamente a partir dos documentos de domínio aprovados, conforme a Regra de Ouro.*
