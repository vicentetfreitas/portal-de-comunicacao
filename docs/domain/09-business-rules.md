# Regras de Negócio

## Objetivo

Consolidar as regras de negócio do **Portal de Comunicação** — invariantes, restrições, políticas, validações e decisões obrigatórias que governam o domínio.

Este documento reúne regras derivadas dos Aggregates, Domain Events e Context Map aprovados em `08-aggregates.md`, `07-domain-events.md` e `06-context-map.md`. Não modela implementação, regras técnicas, validações de interface, banco de dados ou APIs.

---

## Critérios Utilizados

As regras foram identificadas a partir das fontes abaixo, sem redescoberta de domínio:

| Fonte | Uso |
| ----- | --- |
| Invariantes | Regras que não podem ser violadas dentro do limite de cada aggregate (`08-aggregates`) |
| Eventos | Regras que condicionam ou resultam de Domain Events aprovados (`07-domain-events`) |
| Responsabilidades | Políticas derivadas dos Bounded Contexts (`05-bounded-contexts`, consultado para lacunas) |
| Relacionamentos | Regras transversais derivadas de dependências entre contextos (`06-context-map`) |
| Restrições documentadas | Políticas institucionais e de confidencialidade já aprovadas na camada Domain |

Cada regra recebe identificador único (`BR-XXX`) para rastreabilidade em arquitetura e implementação futuras.

---

## Regras Gerais do Domínio

| Código | Regra |
| ------ | ----- |
| BR-001 | O portal é destinado a colaboradores e parceiros autorizados da Unimed Ceará |
| BR-002 | Toda operação relevante no portal exige contexto organizacional válido (singular, área e eventual equipe) |
| BR-003 | Autorização para ações no portal depende de papel de negócio e contexto organizacional |
| BR-004 | Conteúdo e informações do portal são confidenciais e de uso profissional, não destinados a divulgação externa |
| BR-005 | Eventos relevantes de controle de acesso e alterações governadas devem ser registrados em auditoria |
| BR-006 | Agregados distintos referenciam-se por identificadores de negócio; consistência entre agregados é mediada por Domain Events |

---

## Regras de Organização Corporativa

| Código | Regra | Impacto |
| ------ | ----- | ------- |
| BR-007 | Área pertence a uma singular | Define hierarquia departamental e escopo organizacional |
| BR-008 | Equipe pertence a uma área | Delimita agrupamento operacional dentro da estrutura departamental |
| BR-009 | Colaborador operacional possui vínculo a singular e área; colaborador **persistido** possui Federação, Singular e Área obrigatórias (DH-04, DEC-DB-028) | Pré-requisito para operação, publicação e autorização no portal |
| BR-010 | Colaborador operacional autenticado possui pelo menos um vínculo organizacional com Área; sem Área não há operação nem navegação operacional; após persistência o login recupera o vínculo; toda navegação operacional ocorre no Contexto Ativo; identidade autenticada sem COLABORADOR persistido permanece não operacional | Impede operação sem contexto; alinha autenticação × primeiro acesso (DEC-FA-002, DH-03) |
| BR-011 | O primeiro acesso (onboarding) estabelece o vínculo mínimo e cria o COLABORADOR antes da operação plena: domínio → Singular; seleção de Área; Equipe opcional; Contexto Ativo após vínculo completo. **CARGO não participa** do fluxo de Primeiro Acesso e **não bloqueia** a criação do COLABORADOR nem o estado operacional (DH-PA-03) | Integra colaborador ao contexto correto (DEC-FA-001, DH-03, DEC-ORG-003); substitui fluxos legados de solicitação administrativa |
| BR-012 | Contexto organizacional / Contexto Ativo combina federação, singular e área de forma coerente (equipe opcional) | Orienta visão, escopo documental e autorização |
| BR-013 | Singular agrupa áreas e colaboradores no escopo organizacional | Delimita unidade de gestão e referência de escopo |
| BR-014 | Código Unimed identifica singular de forma única no contexto da federação | Garante identificação institucional da unidade cooperativa |
| BR-040 | Hierarquia oficial: Federação → Singular → Área → Equipe → Colaborador | Estrutura organizacional única do domínio (DEC-ORG-001) |
| BR-041 | Um colaborador pode possuir N vínculos organizacionais; a sessão possui um único Contexto Ativo (`federationId`, `singularId`, `areaId`) | Multi-contexto oficial (DEC-FA-003). **Supersession parcial (2026-08-14):** cardinalidade **N vínculos cadastrais** superseded por DH-02/DEC-DB-028 (1 vínculo); **Contexto Ativo único** mantido |
| BR-042 | Após Contexto Ativo resolvido, a Home é determinada pelo backend; o frontend apenas renderiza | Home dinâmica (DEC-FA-004) |
| BR-043 | O domínio do e-mail corporativo autenticado determina a Singular da identidade; resolução é autoridade do backend; o usuário não seleciona Singular diferente da determinada pelo domínio; a Área é selecionada dentro da Singular resolvida; a Equipe é opcional | Primeiro acesso e onboarding (DEC-ORG-003, DH-03, DH-PA-02) |
| BR-044 | Cada domínio de e-mail determina no máximo uma Singular; cada Singular possui no máximo um domínio de e-mail; quando o domínio autenticado não possuir Singular cadastrada, o Primeiro Acesso não prossegue automaticamente e o frontend informa o usuário; o sistema deverá possuir posteriormente capacidade administrativa para cadastrar a associação domínio → Singular pelo Administrador do Sistema | Cardinalidade e comportamento do mapeamento (DH-PA-02). Persistência preparada no repositório (`SINGULAR.DES_DOMINIO_EMAIL`, `UK_SINGULAR_DOMINIO_EMAIL`, V008); execução DBA no Oracle pendente (GAP-028-04) |
| BR-045 | CARGO representa função organizacional do colaborador e é **distinto** de PAPEL (autorização). CARGO **não é requisito** para criação/cadastro de COLABORADOR em **qualquer** fluxo (Primeiro Acesso, administrativo ou futuro). CARGO **não é requisito** para autenticação nem para estado operacional. CARGO **não determina** PAPEL. A definição de CARGO **poderá ocorrer posteriormente** (DH-CARGO-01, DH-PA-03, DEC-ORG-002) | Separação CARGO/PAPEL; obrigatoriedade na criação removida (supersession parcial DEC-DB-027) |

---

## Regras de Gestão Documental

| Código | Regra | Impacto |
| ------ | ----- | ------- |
| BR-015 | Documento deve estar vinculado a escopo organizacional (singular, área) | Conteúdo publicado no contexto departamental correto |
| BR-016 | Documento deve ser organizado em pasta | Estrutura hierárquica e localização de conteúdo |
| BR-017 | Pasta é organizada no contexto de singular, área ou é pessoal de colaborador | Delimita escopo e herança de regras de organização |
| BR-018 | Visibilidade classifica exposição de documento ou pasta como público ou privado conforme escopo | Define nível de exposição do recurso |
| BR-019 | Compartilhamento deve ser coerente com a visibilidade do recurso | Evita contradição entre audiência e classificação de exposição |
| BR-020 | Compartilhamento define audiência do recurso (pessoal, setor, federação, singulares ou colaboradores específicos) | Determina quem pode acessar o recurso documental |
| BR-021 | Recurso privado possui acesso restrito a escopo ou pessoas definidas | Habilita fluxo de solicitação de permissão |
| BR-022 | Recurso público é acessível sem restrição de escopo privado | Permite acesso por convidados a conteúdos públicos |
| BR-023 | Quota de armazenamento limita o espaço atribuído ao colaborador | Controla uso de armazenamento; ultrapassagem impede nova publicação até regularização |
| BR-024 | Recurso privado não pode ter exposição pública sem reclassificação explícita de visibilidade | Protege confidencialidade e consistência de exposição |

---

## Regras de Controle de Acesso

| Código | Regra | Impacto |
| ------ | ----- | ------- |
| BR-025 | Colaborador deve ser autenticado por credenciais de e-mail corporativo da organização | Identidade válida para operação no portal |
| BR-026 | Autenticação vinculada a domínios de e-mail corporativos da Unimed Ceará | Restringe identidade a contas institucionais |
| BR-027 | Papel determina o que a pessoa pode fazer no portal e em qual escopo organizacional | Governança de ações por perfil de negócio |
| BR-028 | Papel não pode existir sem referência de escopo organizacional válida | Evita autorização sem contexto |
| BR-029 | Solicitação de permissão deve referenciar recurso privado | Formaliza pedido de acesso a conteúdo restrito |
| BR-030 | Solicitação de permissão deve ser submetida ao responsável pelo recurso | Inicia fluxo de decisão de concessão de acesso |
| BR-031 | Decisão de permissão (concessão ou negação) é exclusiva do responsável pelo recurso | Garante autoridade de decisão sobre recurso privado |
| BR-032 | Solicitação de permissão não pode ser decidida sem responsável identificado | Protege integridade do fluxo de aprovação |
| BR-033 | Convidado possui acesso restrito a documentos e conteúdos públicos | Limita operação de perfis externos ao escopo público |
| BR-034 | Papéis administrativos operam em escopos definidos (global, singular, área, equipe) | Delimita gestão institucional por nível organizacional |

---

## Regras de Comunicação Interna

| Código | Regra | Impacto |
| ------ | ----- | ------- |
| BR-035 | Notificação deve ser dirigida a colaborador identificado no portal | Garante destinatário válido da comunicação |
| BR-036 | Notificação deve comunicar evento ou resultado de processo relevante | Assegura propósito informativo da notificação |
| BR-037 | Fique por Dentro destina-se a colaboradores como canal de informações internas | Delimita audiência do feed institucional |
| BR-038 | Busca unificada consulta documentos, áreas, singulares e colaboradores sem alterar estado dos agregados consultados | Preserva consistência dos agregados fonte |
| BR-039 | Publicação em canal interno deve definir escopo de audiência | Controla exposição de informações institucionais |

---

## Regras Críticas do Domínio

Regras cuja violação compromete o funcionamento do negócio.

| Código | Regra | Justificativa |
| ------ | ----- | ------------- |
| BR-009 | Colaborador operacional possui vínculo a singular e área | Ator central do fluxo de valor; sem vínculo não há contexto operacional válido |
| BR-010 | Colaborador operacional autenticado exige vínculo com Área e Contexto Ativo para navegar; identidade autenticada sem COLABORADOR persistido não é operacional | Impedimento operacional e de primeiro acesso (DEC-FA-002, DH-03) |
| BR-003 | Autorização depende de papel e contexto organizacional | Governança fundamental de quem pode agir no portal |
| BR-019 | Compartilhamento coerente com visibilidade | Protege confidencialidade e evita exposição indevida |
| BR-031 | Decisão de permissão exclusiva do responsável pelo recurso | Materializa governança de acesso a recursos privados |
| BR-001 | Acesso restrito a colaboradores e parceiros autorizados | Política institucional de confidencialidade do portal |
| BR-004 | Conteúdo confidencial e de uso profissional | Restrição institucional de uso do portal |
| BR-005 | Registro em auditoria de eventos relevantes | Rastreabilidade e governança institucional |

---

## Regras Relacionadas a Eventos

| Evento | Regras Relacionadas |
| ------ | ------------------- |
| Colaborador Integrado | BR-009, BR-011, BR-012 |
| Contexto Organizacional Estabelecido | BR-002, BR-012 |
| Vínculo Organizacional Alterado | BR-009, BR-012 |
| Estrutura Organizacional Alterada | BR-007, BR-008, BR-013 |
| Colaborador Autenticado | BR-025, BR-026, BR-001 |
| Papel Atribuído | BR-003, BR-027, BR-028, BR-034 |
| Solicitação de Permissão Registrada | BR-029, BR-030, BR-032 |
| Permissão Concedida | BR-031, BR-021, BR-036 |
| Permissão Negada | BR-031, BR-021, BR-036 |
| Perfil de Convidado Habilitado | BR-033, BR-001 |
| Evento de Controle Registrado em Auditoria | BR-005 |
| Documento Publicado | BR-015, BR-016, BR-023 |
| Documento Organizado em Pasta | BR-016, BR-017 |
| Visibilidade Definida | BR-018, BR-019, BR-021, BR-022, BR-024 |
| Compartilhamento Definido | BR-019, BR-020, BR-003 |
| Quota de Armazenamento Ultrapassada | BR-023 |
| Notificação Dirigida ao Colaborador | BR-035, BR-036 |
| Publicação em Fique por Dentro Realizada | BR-037, BR-039 |
| Comunicado Institucional Publicado | BR-039, BR-004 — fronteira em lacuna |

---

## Regras Relacionadas a Aggregates

| Aggregate | Regras Protegidas |
| --------- | ----------------- |
| Organização Corporativa | BR-007, BR-008, BR-009, BR-010, BR-011, BR-012, BR-013, BR-014, BR-040, BR-041, BR-042, BR-043, BR-044, BR-045 |
| Gestão Documental | BR-015, BR-016, BR-017, BR-018, BR-019, BR-020, BR-021, BR-022, BR-023, BR-024, BR-004 |
| Controle de Acesso | BR-001, BR-003, BR-005, BR-025, BR-026, BR-027, BR-028, BR-029, BR-030, BR-031, BR-032, BR-033, BR-034 |
| Comunicação Interna | BR-035, BR-036, BR-037, BR-038, BR-039 |
| Transversal (todos) | BR-002, BR-006 |

---

## Restrições de Negócio

| Restrição | Justificativa |
| --------- | ------------- |
| Acesso restrito a colaboradores e parceiros autorizados da Unimed Ceará | Política institucional documentada nos aggregates e bounded contexts |
| Informações do portal confidenciais e de uso profissional | Restrição de confidencialidade aprovada na camada Domain |
| Autenticação vinculada a domínios de e-mail corporativos | Identidade corporativa como pré-requisito de acesso |
| Documentos e pastas com visibilidade por escopo (público, singular, área, pessoal) | Governança documental hierárquica |
| Colaborador sem área vinculada impedido de operar | Dependência crítica documentada no Context Map |
| Convidado limitado a conteúdos públicos | Perfil de acesso externo com escopo delimitado |
| Decisão de acesso a recurso privado pelo responsável pelo recurso | Fluxo formal de solicitação de permissão |

---

## Ambiguidades Ainda Existentes

| Item | Impacto |
| ---- | ------- |
| ~~Onboarding com fluxos coexistentes~~ | **Resolvido** — DEC-FA-001 / BR-011 (primeiro acesso = resolução de Contexto Ativo; solicitação admin obsoleta como onboarding oficial) |
| Parceiro autorizado vs. convidado | BR-001 e BR-033 não cobrem critérios operacionais de parceiro autorizado |
| Comunicado como categoria de documento vs. publicação institucional | BR-039 sem aggregate responsável único |
| Compartilhamento vs. acesso efetivo | BR-020 e BR-003 podem divergir se audiência definida não coincidir com permissão efetiva |
| Federação como estrutura vs. escopo de compartilhamento | BR-020 pode aplicar "federação" com sentidos distintos |
| Solicitação de permissão com operação parcial documentada | BR-029 a BR-032 podem não refletir operação completa em todos os cenários |

---

## Lacunas Restantes

| Lacuna | Impacto |
| ------ | ------- |
| Regras de revogação de permissão | Ciclo de vida de acesso incompleto após Permissão Concedida |
| Regras de alteração de compartilhamento após publicação | BR-019 e BR-020 sem processo de reversão documentado |
| Regras operacionais de parceiro autorizado | BR-001 sem detalhamento de elegibilidade e permissões |
| Regras da Central de Colaboração | Sem políticas de interação entre colaboradores |
| Regras de métricas administrativas | Sem léxico de indicadores e políticas de exibição |
| Regras consolidadas de comunicado institucional | BR-039 sem distinção formal categoria vs. módulo |
| Implementação física do mapeamento domínio de e-mail → Singular | **DH-PA-02 aprovada** (2026-08-15); cardinalidade e comportamento definidos (BR-044); artefatos no repositório (DDL, V008, DML); execução DBA no Oracle pendente — ver `docs/governance/03-open-decisions.md` (GAP-028-04) |
| Credencial temporária de Primeiro Acesso (sem AUTH_SESSAO operacional) | **DH-PA-01 aprovada** (2026-08-15); implementação técnica pendente — ver `docs/governance/03-open-decisions.md` |
| Política de CARGO na criação do COLABORADOR | **DH-CARGO-01 aprovada** (2026-08-17) — CARGO não obrigatório em qualquer cadastro; reconciliação DEC-DB-027 encerrada — ver `docs/governance/03-open-decisions.md` |
| Regras de herança de permissões em hierarquia de pastas | BR-017 referencia estrutura hierárquica sem regras de herança detalhadas |

---

## Nível de Confiança

**Médio-Alto**

| Faixa | Escopo | Justificativa |
| ----- | ------ | ------------- |
| Alto | BR-001 a BR-034 (núcleo organizacional, documental e de acesso); regras críticas; mapeamento eventos/agregados do fluxo principal | Derivadas diretamente de invariantes aprovadas em `08-aggregates` |
| Médio | BR-035 a BR-039 (comunicação interna); restrições transversais; ambiguidades compartilhamento/acesso | Sustentadas por aggregates com confiança média a baixa |
| Baixo | Regras implícitas de comunicado, parceiro autorizado e Central de Colaboração | Lacunas documentadas; regras não formalizadas nos documentos fonte |

A classificação geral é **Médio-Alto** porque o catálogo do núcleo (vínculo organizacional, exposição documental, autorização e auditoria) está estável e rastreável, enquanto regras de comunicação periférica e perfis externos permanecem sujeitas a validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/08-aggregates.md`
- `docs/domain/07-domain-events.md`
- `docs/domain/06-context-map.md`

### Fonte complementar (lacunas)

- `docs/domain/05-bounded-contexts.md` — regras relevantes por contexto e restrições institucionais

*Nenhuma fonte Discovery foi utilizada. Regras, restrições e lacunas foram consolidadas exclusivamente a partir dos documentos de domínio aprovados, conforme a Regra de Ouro.*
