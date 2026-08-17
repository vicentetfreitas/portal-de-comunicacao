# Glossário de Negócio

## Objetivo

Estabelecer o vocabulário oficial de negócio do **Portal de Comunicação**, consolidando os termos utilizados por stakeholders, usuários, administradores e pela documentação do sistema.

Este glossário serve como referência estável para documentação de domínio, modelagem DDD, arquitetura, desenvolvimento e comunicação entre negócio e tecnologia.

---

## Escopo

O glossário cobre o domínio do Portal de Comunicação conforme consolidado em `01-vision.md`, incluindo:

- **Organização corporativa:** federação, singulares, áreas, equipes e colaboradores
- **Gestão documental:** documentos, pastas, visibilidade e compartilhamento
- **Controle de acesso:** papéis, contexto organizacional, solicitações de permissão e auditoria
- **Comunicação interna:** notificações, comunicados e publicações internas
- **Atores e processos:** administradores, convidados, parceiros, onboarding e integração de novos colaboradores

**Fora do escopo:** termos técnicos (tabelas, classes, APIs, frameworks, endpoints, tecnologias de infraestrutura).

---

## Termos Oficiais do Domínio

| Termo | Definição de Negócio | Evidência |
|---|---|---|
| Portal de Comunicação | Sistema de comunicação interna e gestão documental da Unimed Ceará para colaboradores e parceiros autorizados | `01-vision` Objetivo do Produto; `PublicLayout.vue` |
| Unimed Ceará | Organização de saúde cooperativa proprietária e operadora do portal | `01-vision` Contexto Organizacional; `AppFooter.vue` |
| Federação | Conjunto organizacional mais amplo no qual a Unimed Ceará e outras singulares coexistem; delimita escopo de compartilhamento institucional | `01-vision` Contexto Organizacional; `sharing.ts` regra FEDERACAO |
| Singular | Unidade organizacional da federação que agrupa áreas, colaboradores e documentos | `01-vision` Conceitos Centrais |
| Área | Setor ou departamento vinculado a uma singular; pode ser hierárquica e delimita escopo de documentos, pastas e colaboradores | `01-vision` Conceitos Centrais |
| Equipe | Agrupamento operacional de colaboradores dentro de uma área | `01-vision` Conceitos Centrais |
| Colaborador | Pessoa com vínculo operacional a singular e área, identificada no portal para consulta e publicação de conteúdo conforme permissões | `01-vision` Conceitos Centrais; Atores |
| Documento | Artefato de comunicação ou arquivo gerenciado no portal, com visibilidade e escopo organizacional definidos | `01-vision` Conceitos Centrais |
| Pasta | Estrutura hierárquica que organiza documentos por contexto organizacional ou pessoal | `01-vision` Conceitos Centrais |
| Visibilidade | Nível de exposição de um documento ou pasta — público ou privado conforme escopo (singular, área, colaborador) | `01-vision` Conceitos Centrais; `README.md` |
| Compartilhamento | Regra de negócio que define quem pode acessar um documento ou pasta (pessoal, setor, federação, singulares ou colaboradores específicos) | `01-vision` Conceitos Centrais; `sharing.ts` |
| Papel | Papel de negócio que determina o que uma pessoa pode fazer no portal e em qual escopo organizacional | `01-vision` Conceitos Centrais; Atores |
| Contexto organizacional | Combinação de federação, singular e área (equipe opcional) que delimita a visão e as ações do colaborador | `01-vision`; DEC-ORG-001 |
| Contexto Ativo | Projeção derivada do único vínculo cadastral do COLABORADOR (`federationId`, `singularId`, `areaId`, `teamId` opcional) em vigor na sessão; base da navegação operacional — **não** é estado cadastral separado (DH-02) | DEC-FA-003 P2/P3; FT-SESSION / FT-PRIMEIRO-ACESSO; `construction/review/contexto-ativo-dh02-investigacao.md` |
| Onboarding | Processo de Primeiro Acesso: wizard de vínculo (domínio → Singular → Área → Equipe opcional) + criação do COLABORADOR antes da operação plena (DH-03) | DEC-FA-001, BR-011; **obsoleto** como solicitação administrativa |
| Home | Painel inicial dinâmico determinado pelo backend após Contexto Ativo | DEC-FA-004 |
| Notificação | Comunicação de evento relevante dirigida ao colaborador dentro do portal | `01-vision` Conceitos Centrais |
| Solicitação de permissão | Pedido formal de acesso a recurso privado, aguardando decisão do responsável | `01-vision` Conceitos Centrais; Fluxo de Valor |
| Auditoria | Registro consultável de eventos de controle de acesso e alterações relevantes | `01-vision` Conceitos Centrais |
| Convidado | Pessoa com perfil de acesso restrito a documentos e conteúdos públicos | `01-vision` Conceitos Centrais; Atores |
| Parceiro autorizado | Pessoa externa à operação cotidiana, com acesso restrito conforme política institucional do portal | `01-vision` Atores; `AppFooter.vue` |
| Administrador global | Responsável pela gestão completa do portal, usuários, estrutura organizacional e auditoria | `01-vision` Atores |
| Administrador de singular | Responsável pela gestão de uma singular, suas áreas vinculadas e colaboradores no escopo | `01-vision` Atores |
| Administrador de área | Responsável pela gestão de uma área, suas equipes, colaboradores e documentos do setor | `01-vision` Atores |
| Proprietário de equipe | Responsável pela gestão de uma equipe, seus membros e documentos no escopo do time | `01-vision` Atores |
| Recurso privado | Documento ou pasta com acesso restrito a escopo ou pessoas definidas | `01-vision` Problema de Negócio; `README.md` |
| Recurso público | Documento ou conteúdo acessível sem restrição de escopo privado | `PublicLayout.vue`; papel Convidado |
| Responsável pelo recurso | Pessoa com autoridade para aprovar ou negar solicitação de acesso a recurso privado | `01-vision` Dependências de Negócio |
| Comunicado | Tipo de conteúdo institucional de comunicação corporativa | `01-vision` Restrições; módulo Comunicados |
| Fique por Dentro | Canal de publicações e informações internas destinado a colaboradores | `01-vision` Capacidades |
| Quota de armazenamento | Limite de espaço atribuído ao colaborador para armazenamento de documentos | `01-vision` Controle de armazenamento |
| Busca unificada | Pesquisa transversal em documentos, áreas, singulares e colaboradores | `01-vision` Capacidades |
| Métricas administrativas | Indicadores de gestão e acompanhamento do portal para administração | `01-vision` Capacidades |
| Central de Colaboração | Espaço de interação entre colaboradores no portal | `01-vision` Capacidades |
| Autenticação corporativa | Identificação do colaborador por meio de credenciais de e-mail da organização | `01-vision` Capacidades |
| Código Unimed | Identificador da unidade cooperativa associada a uma singular | `01-vision` Hipóteses; formulários de singular |
| Conteúdo confidencial | Informação do portal de uso restrito e profissional, não destinada a divulgação externa | `AppFooter.vue` |

---

## Sinônimos Encontrados

| Termo Oficial | Sinônimos Encontrados | Evidência |
|---|---|---|
| Área | Setor | `sharing.ts` label "Privado à área"; referências a "contatos setoriais" na interface |
| Pasta | Diretório | `01` módulo "Pastas e Diretórios"; `README.md` "Diretórios Hierárquicos" |
| Equipe | Time | Uso corrente em `02` role `team_owner` ("gestão de time") |
| Convidado | Visitante | `02` role `visitor` descrita como "Convidado" |
| Federação | Unimed Ceará (no escopo de compartilhamento) | `sharing.ts` label "Privado à Unimed Ceará" para regra FEDERACAO |
| Singular | Unimed (no cadastro organizacional) | Campos `cod_unimed`, `nome_unimed` em formulários de singular |
| Solicitação de permissão | Solicitação de acesso | `01-vision` Capacidades ("Solicitação de acesso") |
| Onboarding | Integração de novos usuários | `01-vision` Capacidades |
| Administrador global | Administrador | `02` role `administrator` |
| Colaborador | Usuário (no contexto operacional do portal) | `02` role `collaborator` descrita como "Usuário" |
| Gestão de convidados | Convidados | `01` módulo Convidados; rotas `admin.convidados` |

---

## Termos Ambíguos

| Termo | Ambiguidade Identificada | Impacto |
|---|---|---|
| Usuário | Pode designar qualquer pessoa com identidade no portal ou especificamente o colaborador operacional | Dificulta distinção entre identidade de acesso e papel de negócio |
| Colaborador | Representa pessoa vinculada organizacionalmente, mas compartilha a mesma identidade base de "usuário" no sistema | Dois vocabulários para a mesma pessoa conforme o contexto |
| Convidado | Papel com acesso público limitado, mas "parceiro autorizado" aparece na política institucional sem critérios detalhados | Sobreposição entre convidado e parceiro externo |
| Parceiro autorizado | Mencionado na política de acesso, sem definição operacional distinta de convidado | Critério de elegibilidade e permissões não formalizados |
| Equipe | Referenciada como agrupamento organizacional, mas existem representações divergentes no modelo de dados | Risco de inconsistência na gestão de times |
| Comunicado | Aparece como categoria de documento e como módulo corporativo distinto | Incerteza se comunicado é tipo de documento ou entidade própria |
| Onboarding | Pode significar seleção direta de singular/área ou fluxo de solicitação com aprovação administrativa | Processo de integração de novos colaboradores ambíguo |
| Federação | Usada como escopo de compartilhamento (Unimed Ceará) e como identificador organizacional em navegação | Pode confundir escopo institucional com unidade singular |
| Organização | Referenciada em interfaces administrativas, enquanto o termo oficial de domínio é Singular | Vocabulário paralelo para unidade organizacional |
| Proprietário de equipe | No negócio canônico; em outras camadas aparece "administrador de equipe" | Nomenclatura de papel divergente entre camadas |

---

## Termos Relacionados

| Termo Principal | Relacionado A | Tipo de Relação |
|---|---|---|
| Singular | Federação | pertence / compõe |
| Área | Singular | pertence a |
| Equipe | Área | pertence a |
| Colaborador | Singular | vinculado a |
| Colaborador | Área | vinculado a |
| Colaborador | Equipe | pode pertencer a |
| Documento | Singular | vinculado a (escopo) |
| Documento | Área | vinculado a (escopo) |
| Documento | Pasta | organizado em |
| Documento | Colaborador | publicado por / de autoria |
| Pasta | Singular | organizada no contexto de |
| Pasta | Área | organizada no contexto de |
| Pasta | Colaborador | pode ser pessoal de |
| Compartilhamento | Documento | define acesso a |
| Compartilhamento | Pasta | define acesso a |
| Visibilidade | Documento | classifica exposição de |
| Visibilidade | Pasta | classifica exposição de |
| Papel | Colaborador | atribuído a |
| Contexto organizacional | Colaborador | delimita visão de |
| Solicitação de permissão | Recurso privado | solicita acesso a |
| Solicitação de permissão | Responsável pelo recurso | submetida a |
| Notificação | Colaborador | dirigida a |
| Auditoria | Papel | registra eventos de |
| Onboarding | Colaborador | vincula |
| Onboarding | Singular | seleciona |
| Onboarding | Área | seleciona |
| Administrador de área | Área | administra |
| Administrador de singular | Singular | administra |
| Convidado | Recurso público | acessa |
| Quota de armazenamento | Colaborador | limita uso de |

---

## Acrônimos e Siglas

| Sigla | Significado | Evidência |
|---|---|---|
| Unimed | União de cooperativas de saúde; no contexto do portal, referência à organização cooperativa médica | Nome institucional "Unimed Ceará"; campos `cod_unimed`, `nome_unimed` |

*Demais siglas encontradas na documentação técnica (ex.: identificadores internos de notificação) não constituem vocabulário de negócio e foram excluídas.*

---

## Termos Candidatos para Linguagem Ubíqua

| Termo | Motivo |
|---|---|
| Singular | Núcleo da estrutura organizacional multi-unidade; distingue unidades da federação |
| Área | Unidade departamental recorrente em permissões, documentos e contexto do colaborador |
| Colaborador | Ator central do fluxo de valor; distingue operação diária de outros perfis de acesso |
| Documento | Objeto principal de comunicação e gestão documental |
| Pasta | Organização hierárquica essencial para localização e controle de acesso |
| Compartilhamento | Regra de negócio que define audiência de cada recurso |
| Visibilidade | Classificação público/privado por escopo organizacional |
| Papel | Vocabulário unificado para autorização por perfil de negócio |
| Contexto organizacional | Expressa a combinação singular/área/equipe que orienta a operação |
| Solicitação de permissão | Processo formal de concessão de acesso a recursos privados |
| Federação | Escopo institucional de compartilhamento entre singulares |
| Onboarding | Processo de integração de novos colaboradores ao contexto correto |
| Convidado | Perfil de acesso externo com escopo público delimitado |
| Notificação | Canal de comunicação de eventos relevantes ao colaborador |
| Auditoria | Rastreabilidade de eventos de controle de acesso |

---

## Lacunas de Terminologia

| Lacuna | Impacto |
|---|---|
| Definição operacional de "parceiro autorizado" vs. "convidado" | Impossibilita vocabulário unificado para acesso externo |
| Termo oficial para módulo "Central de Colaboração" além do nome de interface | Escopo de colaboração entre pessoas não estabilizado |
| Distinção formal entre "comunicado" (categoria) e "comunicado" (módulo corporativo) | Risco de duplicidade conceitual na comunicação interna |
| Termo consolidado para o fluxo de aprovação no onboarding (seleção vs. solicitação) | **Obsoleto no TO-BE** — DEC-FA-001 define primeiro acesso como resolução de Contexto Ativo |
| Vocabulário oficial para "métricas administrativas" e indicadores exibidos | Gestão do portal sem léxico de indicadores confirmado |
| Termo de negócio para "organização" quando usado como sinônimo de singular | Interfaces administrativas podem divergir do glossário oficial |

*Fonte: lacunas em `01-vision` Lacunas de Conhecimento.*

---

## Conflitos Terminológicos

| Termo | Conflito Identificado | Fonte |
|---|---|---|
| Onboarding | Seleção direta de singular/área vs. solicitação com aprovação administrativa | `01-vision` Conflitos Encontrados |
| Comunicado | Categoria de documento vs. módulo corporativo independente | `01-vision` Conflitos Encontrados |
| Proprietário de equipe | Termo canônico de negócio vs. "administrador de equipe" em outras camadas | `02-current-rbac` divergência `team_owner` / `team_administrator` |
| Administrador de singular | Papel canônico vs. referência a "proprietário de singular" em validações de upload | `02-current-rbac` divergência `singular_administrator` / `singular_owner` |
| Solicitação de permissão | Fluxo descrito na documentação do produto vs. capacidade parcial na Discovery | `01-vision` Conflitos; `README.md` |
| Federação | Escopo de compartilhamento (Unimed Ceará) vs. identificador de navegação organizacional | `sharing.ts`; `StayInformedPostPage.vue` slug `federacao` |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
|---|---|---|
| Alto | Singular, área, equipe, colaborador, documento, pasta, papel, compartilhamento, visibilidade, convidado, auditoria, notificação | Termos com definição convergente em `01-vision` e múltiplas evidências |
| Médio | Onboarding, solicitação de permissão, parceiro autorizado, federação, comunicado, fique por dentro | Termos com uso evidenciado, mas ambiguidade ou lacuna documentada |
| Baixo | Central de colaboração, métricas administrativas, organização (como sinônimo de singular) | Termos com evidência parcial ou conflito terminológico não resolvido |

A classificação geral é **Médio-Alto** porque o vocabulário do núcleo organizacional e documental está estável, enquanto termos de processos periféricos e perfis externos permanecem sujeitos a validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/01-vision.md`

### Fontes complementares (lacunas e validação de sinônimos)

- `docs/discovery/02-current-rbac.md` — papéis, ambiguidades de nomenclatura
- `docs/discovery/03-current-data-model.md` — relações entre conceitos
- `frontend/src/types/sharing.ts` — regras e labels de compartilhamento
- `README.md` — diretórios hierárquicos e solicitação de permissões
- `frontend/src/layouts/PublicLayout.vue` — posicionamento institucional
- `frontend/src/components/app/AppFooter.vue` — política de acesso e confidencialidade

*Nenhuma redescoberta de domínio além do consolidado em `01-vision.md`.*
