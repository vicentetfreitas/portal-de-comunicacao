# Linguagem Ubíqua

## Objetivo

Estabelecer o vocabulário oficial e consistente do **Portal de Comunicação**, derivado dos conceitos já aprovados em `01-vision.md` e `02-business-glossary.md`.

A Linguagem Ubíqua deve ser utilizada de forma uniforme por stakeholders, negócio, produto, arquitetura, desenvolvimento e documentação futura do domínio. Este documento consolida terminologia — não cria novos conceitos nem altera definições aprovadas.

---

## Princípios da Linguagem

| Princípio | Descrição |
| --------- | --------- |
| Um conceito, um nome oficial | Cada conceito de domínio possui um único termo preferencial definido neste documento |
| Definições imutáveis | Definições reproduzem o glossário aprovado; alterações exigem revisão formal do glossário |
| Contexto explícito | Termos com múltiplos sentidos devem ser qualificados pelo contexto de uso |
| Sinônimos controlados | Sinônimos só são aceitos quando registrados neste documento; documentação futura deve preferir o termo oficial |
| Termos ambíguos eliminados | Ambiguidades identificadas no glossário são resolvidas por decisão documentada, sem inventar novos conceitos |
| Linguagem de negócio | Vocabulário orientado ao domínio; termos técnicos (classes, tabelas, APIs, frameworks) ficam fora do escopo |
| Rastreabilidade | Toda decisão terminológica remete aos documentos de domínio aprovados |

---

## Vocabulário Oficial

| Termo Oficial | Definição Oficial | Contexto de Uso |
| ------------- | ----------------- | --------------- |
| Portal de Comunicação | Sistema de comunicação interna e gestão documental da Unimed Ceará para colaboradores e parceiros autorizados | Referência ao produto como um todo |
| Unimed Ceará | Organização de saúde cooperativa proprietária e operadora do portal | Contexto institucional e política de acesso |
| Federação | Conjunto organizacional mais amplo no qual a Unimed Ceará e outras singulares coexistem; delimita escopo de compartilhamento institucional | Estrutura organizacional e regras de compartilhamento |
| Singular | Unidade organizacional da federação que agrupa áreas, colaboradores e documentos | Hierarquia organizacional, escopo de documentos e gestão administrativa |
| Área | Setor ou departamento vinculado a uma singular; pode ser hierárquica e delimita escopo de documentos, pastas e colaboradores | Estrutura departamental, permissões e contexto do colaborador |
| Equipe | Agrupamento operacional de colaboradores dentro de uma área | Gestão de times, escopo de documentos e papéis de equipe |
| Colaborador | Pessoa com vínculo operacional a singular e área, identificada no portal para consulta e publicação de conteúdo conforme permissões | Ator central do fluxo de valor; operação cotidiana no portal |
| Documento | Artefato de comunicação ou arquivo gerenciado no portal, com visibilidade e escopo organizacional definidos | Gestão documental, publicação e consulta de conteúdo |
| Pasta | Estrutura hierárquica que organiza documentos por contexto organizacional ou pessoal | Organização de documentos, navegação e controle de acesso |
| Visibilidade | Nível de exposição de um documento ou pasta — público ou privado conforme escopo (singular, área, colaborador) | Classificação de exposição de recursos documentais |
| Compartilhamento | Regra de negócio que define quem pode acessar um documento ou pasta (pessoal, setor, federação, singulares ou colaboradores específicos) | Definição de audiência e acesso a recursos |
| Papel | Papel de negócio que determina o que uma pessoa pode fazer no portal e em qual escopo organizacional | Autorização, RBAC e gestão de permissões |
| Contexto organizacional | Combinação de singular, área e equipe que delimita a visão e as ações do colaborador no portal | Navegação, autorização e experiência operacional do colaborador |
| Notificação | Comunicação de evento relevante dirigida ao colaborador dentro do portal | Comunicação de eventos e resultados de processos |
| Solicitação de permissão | Pedido formal de acesso a recurso privado, aguardando decisão do responsável | Fluxo de concessão de acesso a recursos restritos |
| Auditoria | Registro consultável de eventos de controle de acesso e alterações relevantes | Rastreabilidade e governança |
| Onboarding | Processo de vinculação inicial do colaborador à singular e área adequadas | Integração de novos colaboradores ao contexto organizacional |
| Convidado | Pessoa com perfil de acesso restrito a documentos e conteúdos públicos | Perfil de acesso externo com escopo público delimitado |
| Parceiro autorizado | Pessoa externa à operação cotidiana, com acesso restrito conforme política institucional do portal | Política institucional de acesso; comunicação com stakeholders |
| Administrador global | Responsável pela gestão completa do portal, usuários, estrutura organizacional e auditoria | Gestão institucional do portal |
| Administrador de singular | Responsável pela gestão de uma singular, suas áreas vinculadas e colaboradores no escopo | Gestão por unidade organizacional |
| Administrador de área | Responsável pela gestão de uma área, suas equipes, colaboradores e documentos do setor | Gestão departamental |
| Proprietário de equipe | Responsável pela gestão de uma equipe, seus membros e documentos no escopo do time | Gestão de equipe operacional |
| Recurso privado | Documento ou pasta com acesso restrito a escopo ou pessoas definidas | Controle de acesso a conteúdo restrito |
| Recurso público | Documento ou conteúdo acessível sem restrição de escopo privado | Conteúdo aberto a convidados e consulta pública |
| Responsável pelo recurso | Pessoa com autoridade para aprovar ou negar solicitação de acesso a recurso privado | Decisão em solicitações de permissão |
| Comunicado | Tipo de conteúdo institucional de comunicação corporativa | Comunicação interna; requer qualificação de contexto (categoria vs. módulo) |
| Fique por Dentro | Canal de publicações e informações internas destinado a colaboradores | Feed de informações internas |
| Quota de armazenamento | Limite de espaço atribuído ao colaborador para armazenamento de documentos | Controle de uso de armazenamento |
| Busca unificada | Pesquisa transversal em documentos, áreas, singulares e colaboradores | Localização de conteúdo e pessoas no portal |
| Métricas administrativas | Indicadores de gestão e acompanhamento do portal para administração | Gestão e acompanhamento institucional |
| Central de Colaboração | Espaço de interação entre colaboradores no portal | Colaboração entre colaboradores |
| Autenticação corporativa | Identificação do colaborador por meio de credenciais de e-mail da organização | Acesso e identidade no portal |
| Código Unimed | Identificador da unidade cooperativa associada a uma singular | Cadastro e identificação de singulares |
| Conteúdo confidencial | Informação do portal de uso restrito e profissional, não destinada a divulgação externa | Política de confidencialidade e restrições de uso |

---

## Sinônimos Permitidos

Registrar apenas quando inevitável. Em documentação futura, preferir sempre o termo oficial.

| Termo Oficial | Sinônimo | Observação |
| ------------- | -------- | ---------- |
| Área | Setor | Aceito em interfaces e regras de compartilhamento ("Privado à área"); preferir **Área** em documentação de domínio |
| Solicitação de permissão | Solicitação de acesso | Aceito em descrições de capacidade; preferir **Solicitação de permissão** em processos formais |
| Onboarding | Integração de novos usuários | Aceito em descrições de capacidade; preferir **Onboarding** como termo de processo |
| Equipe | Time | Uso corrente em gestão operacional; preferir **Equipe** em documentação de domínio |
| Administrador global | Administrador | Aceito quando o escopo global estiver explícito no contexto |
| Federação | Unimed Ceará | Permitido apenas no rótulo de compartilhamento "Privado à Unimed Ceará"; não substitui **Federação** como conceito organizacional |
| Singular | Unimed | Permitido apenas em campos de cadastro (`cod_unimed`, `nome_unimed`); não substitui **Singular** em documentação de domínio |

---

## Sinônimos Descontinuados

| Termo Antigo | Utilizar | Motivo |
| ------------ | -------- | ------ |
| Visitante | Convidado | Papel oficial de negócio com acesso a conteúdo público |
| Diretório | Pasta | Termo oficial para estrutura hierárquica de organização documental |
| Organização | Singular | Vocabulário paralelo identificado em interfaces; singular é o termo de domínio |
| Administrador de equipe | Proprietário de equipe | Nomenclatura canônica de negócio para o papel de gestão de equipe |
| Proprietário de singular | Administrador de singular | Papel canônico de negócio para gestão de singular |
| Usuário | Colaborador | Na documentação de domínio, quando se refere à pessoa com vínculo operacional |
| Gestão de convidados | Convidados | Preferir o termo do ator/perfil (**Convidado**) ou a capacidade com nome oficial |

---

## Termos Ambíguos Resolvidos

Decisões adotadas com base nos conceitos aprovados. Não criam novos conceitos.

| Termo | Decisão Adotada | Justificativa |
| ----- | --------------- | ------------- |
| Usuário | Usar **Colaborador** na documentação de domínio; reservar "usuário" apenas para referência à identidade de acesso quando indispensável e explicitamente qualificada | Glossário define Colaborador como ator operacional central; "usuário" designa identidade genérica no portal |
| Colaborador | Termo oficial para pessoa com vínculo operacional a singular e área | Conceito central aprovado em `01-vision` |
| Convidado | Termo oficial do **papel de acesso** com escopo público limitado | Ator definido em `01-vision`; distinto de política institucional |
| Parceiro autorizado | Termo institucional de **política de acesso**; não é sinônimo automático de Convidado | Definição operacional ainda em lacuna; manter separados até validação com stakeholders |
| Equipe | Termo oficial para agrupamento operacional de colaboradores dentro de uma área | Conceito central aprovado; não substituir por representações alternativas não documentadas |
| Comunicado | Qualificar sempre o contexto: **categoria de documento** ou **módulo corporativo**; não usar genericamente | Conflito documentado entre categoria em formulário de documento e módulo Comunicados |
| Onboarding | Termo oficial do processo de vinculação inicial à singular e área | Capacidade aprovada em `01-vision`; fluxos internos (seleção vs. solicitação) permanecem em lacuna |
| Federação | Termo oficial para escopo institucional de compartilhamento entre singulares; não usar como sinônimo de Singular | Delimita compartilhamento institucional; distinto de unidade singular |
| Organização | Substituir por **Singular** na documentação de domínio | Vocabulário paralelo identificado no glossário; Singular é o termo oficial |
| Proprietário de equipe | Termo oficial do papel de gestão de equipe | Ator canônico em `01-vision`; substitui "administrador de equipe" em outras camadas |

---

## Vocabulário por Contexto

### Estrutura Organizacional

| Termo | Definição |
| ----- | --------- |
| Federação | Conjunto organizacional mais amplo no qual a Unimed Ceará e outras singulares coexistem; delimita escopo de compartilhamento institucional |
| Singular | Unidade organizacional da federação que agrupa áreas, colaboradores e documentos |
| Área | Setor ou departamento vinculado a uma singular; pode ser hierárquica e delimita escopo de documentos, pastas e colaboradores |
| Equipe | Agrupamento operacional de colaboradores dentro de uma área |
| Colaborador | Pessoa com vínculo operacional a singular e área, identificada no portal para consulta e publicação de conteúdo conforme permissões |
| Contexto organizacional | Combinação de singular, área e equipe que delimita a visão e as ações do colaborador no portal |
| Código Unimed | Identificador da unidade cooperativa associada a uma singular |
| Onboarding | Processo de vinculação inicial do colaborador à singular e área adequadas |
| Unimed Ceará | Organização de saúde cooperativa proprietária e operadora do portal |

### Gestão Documental

| Termo | Definição |
| ----- | --------- |
| Documento | Artefato de comunicação ou arquivo gerenciado no portal, com visibilidade e escopo organizacional definidos |
| Pasta | Estrutura hierárquica que organiza documentos por contexto organizacional ou pessoal |
| Visibilidade | Nível de exposição de um documento ou pasta — público ou privado conforme escopo (singular, área, colaborador) |
| Compartilhamento | Regra de negócio que define quem pode acessar um documento ou pasta (pessoal, setor, federação, singulares ou colaboradores específicos) |
| Recurso privado | Documento ou pasta com acesso restrito a escopo ou pessoas definidas |
| Recurso público | Documento ou conteúdo acessível sem restrição de escopo privado |
| Quota de armazenamento | Limite de espaço atribuído ao colaborador para armazenamento de documentos |
| Conteúdo confidencial | Informação do portal de uso restrito e profissional, não destinada a divulgação externa |

### Controle de Acesso

| Termo | Definição |
| ----- | --------- |
| Papel | Papel de negócio que determina o que uma pessoa pode fazer no portal e em qual escopo organizacional |
| Administrador global | Responsável pela gestão completa do portal, usuários, estrutura organizacional e auditoria |
| Administrador de singular | Responsável pela gestão de uma singular, suas áreas vinculadas e colaboradores no escopo |
| Administrador de área | Responsável pela gestão de uma área, suas equipes, colaboradores e documentos do setor |
| Proprietário de equipe | Responsável pela gestão de uma equipe, seus membros e documentos no escopo do time |
| Convidado | Pessoa com perfil de acesso restrito a documentos e conteúdos públicos |
| Parceiro autorizado | Pessoa externa à operação cotidiana, com acesso restrito conforme política institucional do portal |
| Solicitação de permissão | Pedido formal de acesso a recurso privado, aguardando decisão do responsável |
| Responsável pelo recurso | Pessoa com autoridade para aprovar ou negar solicitação de acesso a recurso privado |
| Auditoria | Registro consultável de eventos de controle de acesso e alterações relevantes |
| Autenticação corporativa | Identificação do colaborador por meio de credenciais de e-mail da organização |

### Comunicação Interna

| Termo | Definição |
| ----- | --------- |
| Notificação | Comunicação de evento relevante dirigida ao colaborador dentro do portal |
| Comunicado | Tipo de conteúdo institucional de comunicação corporativa |
| Fique por Dentro | Canal de publicações e informações internas destinado a colaboradores |
| Central de Colaboração | Espaço de interação entre colaboradores no portal |
| Busca unificada | Pesquisa transversal em documentos, áreas, singulares e colaboradores |
| Métricas administrativas | Indicadores de gestão e acompanhamento do portal para administração |
| Portal de Comunicação | Sistema de comunicação interna e gestão documental da Unimed Ceará para colaboradores e parceiros autorizados |

---

## Termos Reservados

Termos que exigem uso controlado e qualificação explícita de contexto.

| Termo | Motivo |
| ----- | ------ |
| Federação | Possui duplo sentido: escopo de compartilhamento institucional e identificador em navegação organizacional; qualificar o contexto ao utilizar |
| Singular | Unidade organizacional distinta da Federação; não confundir com a organização como um todo |
| Colaborador | Distinguir de identidade genérica de acesso ("usuário"); usar apenas para pessoa com vínculo operacional a singular e área |
| Parceiro autorizado | Termo institucional sem definição operacional consolidada; não equiparar automaticamente a Convidado |
| Comunicado | Requer qualificação entre categoria de documento e módulo corporativo |
| Onboarding | Processo com fluxos coexistentes não consolidados; qualificar o modelo de vínculo quando necessário |

---

## Termos Proibidos

Preenchido apenas onde a substituição é necessária para consistência terminológica.

| Termo | Substituir Por |
| ----- | -------------- |
| Visitante | Convidado |
| Diretório | Pasta |
| Organização (como unidade organizacional) | Singular |
| Administrador de equipe | Proprietário de equipe |
| Proprietário de singular | Administrador de singular |
| Usuário (em contexto operacional de domínio) | Colaborador |

---

## Recomendações para Documentação Futura

**Termos a utilizar**

- Preferir sempre o **Vocabulário Oficial** e as tabelas por contexto deste documento.
- Utilizar nomes completos de papéis quando houver ambiguidade (ex.: **Administrador global**, não apenas "administrador").
- Qualificar **Comunicado**, **Federação** e **Onboarding** com o contexto de uso quando o sentido não for óbvio.
- Manter **Colaborador** como termo padrão para o ator operacional do domínio.

**Termos a evitar**

- Sinônimos descontinuados listados neste documento.
- Vocabulário técnico (tabelas, classes, endpoints, identificadores de código) em documentos de domínio.
- Equiparação automática entre **Convidado** e **Parceiro autorizado**.
- Uso genérico de "usuário", "organização" ou "diretório" sem qualificação.

**Como manter consistência**

- Antes de introduzir um termo novo em documentação de domínio, verificar se já existe entrada no glossário ou nesta linguagem ubíqua.
- Alterações de definição devem ser feitas primeiro em `02-business-glossary.md` e depois refletidas aqui.
- Documentos derivados (`04-domain-concepts.md`, `05-bounded-contexts.md`, etc.) devem reutilizar os termos oficiais sem criar sinônimos paralelos.
- Lacunas terminológicas devem ser registradas, não resolvidas por inferência.

---

## Lacunas Restantes

Lacunas ainda não resolvidas, herdadas do glossário aprovado.

| Lacuna | Impacto |
| ------ | ------- |
| Definição operacional de "parceiro autorizado" vs. "convidado" | Impossibilita vocabulário unificado para acesso externo |
| Termo oficial para módulo "Central de Colaboração" além do nome de interface | Escopo de colaboração entre pessoas não estabilizado |
| Distinção formal entre "comunicado" (categoria) e "comunicado" (módulo corporativo) | Risco de duplicidade conceitual na comunicação interna |
| Termo consolidado para o fluxo de aprovação no onboarding (seleção vs. solicitação) | Integração de novos colaboradores sem vocabulário de processo único |
| Vocabulário oficial para "métricas administrativas" e indicadores exibidos | Gestão do portal sem léxico de indicadores confirmado |
| Termo de negócio para "organização" quando usado como sinônimo de singular em interfaces | Interfaces administrativas podem divergir do glossário oficial |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
| ----- | ------ | ------------- |
| Alto | Singular, área, equipe, colaborador, documento, pasta, papel, compartilhamento, visibilidade, convidado, auditoria, notificação, recurso privado, recurso público | Definições convergentes em `01-vision` e `02-business-glossary` com múltiplas evidências |
| Médio | Onboarding, solicitação de permissão, parceiro autorizado, federação, comunicado, fique por dentro, responsável pelo recurso | Termos aprovados com ambiguidade ou lacuna documentada; decisões terminológicas adotam o termo oficial sem resolver o processo subjacente |
| Baixo | Central de colaboração, métricas administrativas | Evidência parcial; capacidades com status parcial na visão de domínio |

A classificação geral é **Médio-Alto** porque o vocabulário do núcleo organizacional e documental está estável e consolidado, enquanto termos de processos periféricos e perfis externos permanecem sujeitos a validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/02-business-glossary.md`
- `docs/domain/01-vision.md`

*Nenhuma fonte adicional foi necessária. Conceitos, definições, ambiguidades e lacunas foram consolidados exclusivamente a partir dos documentos de domínio aprovados, conforme a Regra de Ouro.*
