# Open Questions

## Objetivo

Consolidar todas as questões abertas identificadas durante a construção da camada **Domain** do Portal de Comunicação.

Este documento registra incertezas remanescentes — não responde perguntas. Serve como entrada para validação com stakeholders, especialistas de negócio ou futuras etapas de descoberta, sem necessidade de revisitar toda a documentação Domain.

---

## Critérios Utilizados

As questões foram consolidadas exclusivamente a partir de lacunas, ambiguidades, pontos de atenção e itens de baixa confiança já documentados em:

| Fonte | Tipo de incerteza |
| ----- | ----------------- |
| `09-business-rules.md` | Ambiguidades, lacunas de regras, regras incompletas |
| `08-aggregates.md` | Pontos de atenção, lacunas de consistência, aggregates com baixa confiança |
| `07-domain-events.md` | Eventos com baixa confiança, lacunas de ciclo de vida, processos de reversão não documentados |

Nenhuma nova descoberta foi realizada neste documento histórico. Encerramentos de OQ (2026-07-24) estão em `docs/governance/03-open-decisions.md` e nas seções atualizadas abaixo — o restante permanece como registro de lacunas originais.

---

## Questões Críticas

Perguntas cuja resposta pode alterar significativamente o domínio, os aggregates ou as regras de negócio.

| ID | Questão | Impacto | Status |
| -- | ------- | ------- | ------ |
| OQ-001 | Qual é o fluxo oficial de onboarding? | Colaborador Integrado, BR-011 | **Encerrada** — DEC-FA-001 |
| OQ-002 | Parceiro autorizado e convidado são perfis distintos? Quais critérios de elegibilidade e permissões aplicam a cada um? | Afeta BR-001, BR-033 e governança de acesso externo | Aberta |
| OQ-003 | O fluxo de solicitação de permissão opera de ponta a ponta (registro → decisão → notificação)? | Valida BR-029 a BR-032 e eventos do aggregate Controle de Acesso | Aberta |
| OQ-004 | Comunicado é categoria de documento, publicação institucional independente ou ambos com regras distintas? | Define fronteira entre Gestão Documental e Comunicação Interna; afeta BR-039 | **Encerrada** — DEC-CMS-002 (2026-08-27): Comunicado é publicação do CMS (WordPress), não categoria de documento; `CATEGORIA_DOCUMENTAL` passa a ser tipo de mídia |
| OQ-005 | Compartilhamento definido em Gestão Documental e acesso efetivo em Controle de Acesso devem ser sempre equivalentes? | Risco de divergência entre audiência e permissão; afeta BR-020 e BR-003 | Aberta |
| OQ-006 | Existe processo formal de revogação de permissão após Permissão Concedida? | Ciclo de vida de acesso incompleto nos aggregates Gestão Documental e Controle de Acesso | Aberta |

---

## Questões de Organização Corporativa

| ID | Questão | Impacto | Status |
| -- | ------- | ------- | ------ |
| OQ-007 | Quais pré-condições de negócio definem o evento Colaborador Integrado após resolução do Contexto Ativo (FT-PRIMEIRO-ACESSO)? | Detalhar evento pós DEC-FA-001 | Aberta (reescopo) |
| OQ-008 | Colaborador pode pertencer a múltiplas equipes na mesma área? | N áreas aprovadas (DEC-FA-003); resta granularidade de equipe | Aberta (parcial) |
| OQ-009 | Qual o processo de negócio para alteração de vínculo organizacional (singular, área, equipe) após integração? | Impacta Vínculo Organizacional Alterado | Aberta |
| OQ-010 | Representações divergentes de equipe como agrupamento organizacional estão consolidadas em um único modelo? | Pode exigir revisão de BR-008 | Aberta |

---

## Questões de Gestão Documental

| ID | Questão | Impacto |
| -- | ------- | ------- |
| OQ-011 | Como alterar compartilhamento ou visibilidade após publicação de documento ou pasta? | BR-019 e BR-020 sem processo de reversão documentado |
| OQ-012 | Quais regras de herança de permissões ou visibilidade aplicam-se na hierarquia de pastas? | BR-017 referencia estrutura hierárquica sem regras de herança detalhadas |
| OQ-013 | Federação como escopo de compartilhamento ("Privado à Unimed Ceará") é equivalente à federação como estrutura organizacional? | BR-020 pode aplicar "federação" com sentidos distintos |
| OQ-014 | Documento e pasta constituem sublimites de consistência distintos dentro do aggregate Gestão Documental? | Granularidade interna do aggregate não detalhada |
| OQ-015 | Qual política de negócio aplica-se quando quota de armazenamento é ultrapassada além do bloqueio de publicação? | Consequências de BR-023 além do evento documentado |

---

## Questões de Controle de Acesso

| ID | Questão | Impacto |
| -- | ------- | ------- |
| OQ-016 | Quem é o responsável pelo recurso em cada escopo (pessoal, área, singular, federação)? | BR-030 e BR-031 dependem de critério não formalizado |
| OQ-017 | Existe evento e regra de negócio para revogação ou expiração de permissão concedida? | Lacuna de ciclo de vida após Permissão Concedida |
| OQ-018 | O perfil parceiro autorizado possui regras operacionais distintas do convidado? | BR-001 sem detalhamento; evento Perfil de Parceiro Autorizado Habilitado não estabilizado |
| OQ-019 | Quais eventos de controle de acesso são obrigatoriamente registrados em auditoria? | BR-005 sem catálogo fechado de eventos auditáveis |
| OQ-020 | Papéis administrativos (administrador global, de singular, de área, proprietário de equipe) possuem limites de ação documentados por escopo? | BR-034 sem matriz de permissões por papel |

---

## Questões de Comunicação Interna

| ID | Questão | Impacto |
| -- | ------- | ------- |
| OQ-021 | Qual é o escopo de negócio da Central de Colaboração além do nome de interface? | Sem invariantes de mutação no aggregate Comunicação Interna |
| OQ-022 | Quais indicadores compõem métricas administrativas e qual léxico oficial deve ser adotado? | BR-039 e aggregate Comunicação Interna sem vocabulário confirmado |
| OQ-023 | Fique por Dentro possui processo de publicação, aprovação e audiência formalizado? | Evento Publicação em Fique por Dentro Realizada com baixa confiança |
| OQ-024 | Busca unificada possui regras de escopo, filtros ou restrições de visibilidade além da consulta sem mutação? | BR-038 cobre apenas preservação de estado dos agregados fonte |
| OQ-025 | Quais eventos de negócio além de notificação devem ser emitidos pela Comunicação Interna? | Aggregate Comunicação Interna com confiança documentada como baixa a média |

---

## Questões de Autenticação, Sessão e Navegação (pós FT-AUTH)

| ID | Tipo | Questão | Status |
| -- | ---- | ------- | ------ |
| OQ-026 | Negócio | BR-010 no login vs navegação operacional? | **Encerrada** — DEC-FA-002 |
| OQ-027 | Negócio + Arquitetura | Multi-contexto nesta entrega? | **Encerrada** — DEC-FA-003 |
| OQ-028 | Arquitetura + Planejamento | Quem define Home / contrato? | **Encerrada** — DEC-FA-004 |

Registro das DECs: `docs/governance/03-open-decisions.md`.

### Encerramentos 2026-07-24 (resumo)

| OQ | DEC | Decisão em uma linha |
| -- | --- | -------------------- |
| OQ-001 | DEC-FA-001 | Onboarding = resolução/seleção de Contexto Ativo (FT-PRIMEIRO-ACESSO); solicitação admin obsoleta como primeiro acesso |
| OQ-026 | DEC-FA-002 | Vínculo com Área obrigatório para operação; login carrega vínculos; navegação no Contexto Ativo |
| OQ-027 | DEC-FA-003 | N vínculos + Contexto Ativo — **supersession parcial DH-02** (1 vínculo cadastral; Contexto Ativo derivado) |
| OQ-028 | DEC-FA-004 | Home dinâmica definida pelo backend |

---

## Questões Relacionadas a Regras de Negócio

| Regra | Questão Relacionada |
| ----- | ------------------- |
| BR-001 | Quem qualifica como parceiro autorizado e com quais permissões? (OQ-002, OQ-018) |
| BR-011 | Fluxo oficial de primeiro acesso e evento Colaborador Integrado? (DEC-FA-001; OQ-007) |
| BR-010 | Vínculo/Área e Contexto Ativo — **encerrada** via DEC-FA-002 |
| BR-017 | Como funciona herança de regras na hierarquia de pastas? (OQ-012) |
| BR-019 | Como alterar visibilidade ou compartilhamento após definição inicial? (OQ-011) |
| BR-020 | Federação no compartilhamento equivale à federação organizacional? (OQ-013) |
| BR-029 a BR-032 | O fluxo de solicitação de permissão está completo em produção? (OQ-003, OQ-016) |
| BR-031 | Quem é responsável pelo recurso em cada tipo de escopo? (OQ-016) |
| BR-033 | Convidado e parceiro autorizado são equivalentes operacionalmente? (OQ-002) |
| BR-039 | Comunicado institucional segue regras de documento ou de canal interno? — **encerrada** via DEC-CMS-002: canal interno / publicação (CMS), não documento |

---

## Questões Relacionadas a Eventos

| Evento | Questão Relacionada |
| ------ | ------------------- |
| Colaborador Integrado | Qual fluxo de onboarding gera este evento e com quais pré-condições? (OQ-001, OQ-007) |
| Solicitação de Permissão Registrada | O fluxo completo até notificação está operacional? (OQ-003) |
| Permissão Concedida | Existe evento de revogação correspondente? (OQ-006, OQ-017) |
| Compartilhamento Definido | Alteração posterior gera qual evento? (OQ-011) |
| Comunicado Institucional Publicado | Comunicação Interna (CMS/WordPress) — **encerrada** via DEC-CMS-002 |
| Publicação em Fique por Dentro Realizada | Qual processo de negócio sustenta este evento? (OQ-023) |
| Perfil de Parceiro Autorizado Habilitado | Este evento deve existir no catálogo oficial? (OQ-018) |

---

## Questões Relacionadas a Aggregates

| Aggregate | Questão Relacionada |
| --------- | ------------------- |
| Organização Corporativa | Qual fluxo consolidado de onboarding e modelo de equipe? (OQ-001, OQ-008, OQ-010) |
| Gestão Documental | Regras de herança em pastas? (OQ-012, OQ-014) — Comunicado saiu do escopo (DEC-CMS-002) |
| Controle de Acesso | Ciclo de vida completo de solicitação e revogação de permissão? (OQ-003, OQ-006, OQ-017) |
| Controle de Acesso | Invariantes operacionais de parceiro autorizado? (OQ-002, OQ-018) |
| Gestão Documental ↔ Controle de Acesso | Como garantir equivalência entre compartilhamento e acesso efetivo? (OQ-005) |
| Comunicação Interna | Quais invariantes de mutação além de notificação? (OQ-021, OQ-022, OQ-025) |

---

## Questões Relacionadas a Papéis e Permissões

| Tema | Questão |
| ---- | ------- |
| Papéis administrativos | Qual matriz de ações permitidas por administrador global, de singular, de área e proprietário de equipe? (OQ-020) |
| Papel de colaborador | Quais ações mínimas exige o papel operacional padrão? |
| Responsável pelo recurso | Como identificar o responsável por documento ou pasta em cada escopo? (OQ-016) |
| Convidado | Existe processo de gestão de convidados além do acesso a conteúdo público? |
| Parceiro autorizado | Critérios de elegibilidade, permissões e distinção de convidado? (OQ-002, OQ-018) |
| Solicitação de permissão | Quem pode solicitar, com qual frequência e para quais tipos de recurso? (OQ-003) |
| Revogação | Quem pode revogar permissão concedida e em quais condições? (OQ-006, OQ-017) |

---

## Questões Relacionadas a Compartilhamento e Visibilidade

| Tema | Questão |
| ---- | ------- |
| Coerência visibilidade/compartilhamento | Quais combinações de visibilidade e compartilhamento são válidas ou proibidas? (OQ-005) |
| Escopo federação | "Privado à Unimed Ceará" no compartilhamento corresponde a qual conjunto de colaboradores? (OQ-013) |
| Recurso público vs. privado | Quais critérios de negócio determinam a classificação inicial? |
| Alteração de exposição | Qual processo para reclassificar recurso privado como público e vice-versa? (OQ-011) |
| Herança em pastas | Filhas herdam visibilidade e compartilhamento da pasta pai? (OQ-012) |
| Compartilhamento por colaboradores específicos | Como se relaciona com solicitação de permissão? (OQ-005) |

---

## Assuntos que Exigem Validação com Stakeholders

| Tema | Motivo |
| ---- | ------ |
| Onboarding e integração de colaboradores | Dois fluxos coexistentes documentados; impacto em todo o fluxo de valor |
| Parceiro autorizado vs. convidado | Termo institucional sem definição operacional consolidada |
| Fluxo de solicitação e revogação de permissão | Processo central do domínio com lacunas de ciclo de vida |
| Comunicado institucional | Fronteira entre Gestão Documental e Comunicação Interna indefinida |
| Compartilhamento e federação | Vocabulário com duplo sentido documentado |
| Central de Colaboração | Capacidade sem escopo de negócio estabilizado |
| Métricas administrativas | Indicadores sem léxico confirmado |
| Responsável pelo recurso | Critério de identificação não formalizado por escopo |

---

## Riscos Associados às Questões Abertas

| Questão | Risco |
| ------- | ----- |
| OQ-001 | Integração inconsistente de novos colaboradores; bloqueio operacional |
| OQ-026 | Sessão criada sem área vs. BR-010; UX e domínio divergentes |
| OQ-027 | Entrega de multi-contexto sem modelo de dados; retrabalho |
| OQ-028 | Painel inicial implícito; Features de negócio sem home route |
| OQ-002 | Acesso externo mal governado; violação de política institucional |
| OQ-003 | Expectativa de fluxo de permissão não atendida; frustração de usuários |
| OQ-004 | ~~Duplicidade conceitual de comunicado~~ — mitigado por DEC-CMS-002 |
| OQ-005 | Colaborador vê recurso na audiência mas não acessa — ou o inverso |
| OQ-006 | Permissões concedidas sem mecanismo de revogação; exposição indevida prolongada |
| OQ-011 | Alteração de exposição sem regras; inconsistência documental |
| OQ-012 | Herança de pastas imprevisível; falhas de acesso em hierarquia |
| OQ-013 | Compartilhamento institucional com escopo errado |
| OQ-021 | Investimento em capacidade sem valor de negócio definido |
| OQ-022 | Indicadores de gestão sem significado de negócio confirmado |

---

## Priorização das Questões

| ID | Prioridade | Justificativa |
| -- | ---------- | ------------- |
| OQ-001 | Crítica | **Encerrada** — DEC-FA-001 |
| OQ-026 | Crítica | **Encerrada** — DEC-FA-002 |
| OQ-027 | Alta | **Encerrada** — DEC-FA-003 |
| OQ-028 | Alta | **Encerrada** — DEC-FA-004 |
| OQ-002 | Alta | Política institucional de acesso sem operacionalização |
| OQ-003 | Alta | Fluxo central de concessão de acesso a recursos privados |
| OQ-004 | Alta | **Encerrada** — DEC-CMS-002 (2026-08-27) |
| OQ-005 | Alta | Risco direto de inconsistência entre exposição e autorização |
| OQ-006 | Alta | Ciclo de vida de permissão incompleto |
| OQ-007 | Alta | Pré-condições do evento Colaborador Integrado (pós DEC-FA-001) |
| OQ-016 | Alta | Pré-requisito para BR-030 e BR-031 |
| OQ-017 | Alta | Complemento direto de OQ-006 |
| OQ-011 | Média | Manutenção documental após publicação |
| OQ-012 | Média | Hierarquia de pastas com impacto em acesso |
| OQ-013 | Média | Terminologia com duplo sentido documentado |
| OQ-018 | Média | Extensão de OQ-002 para perfil específico |
| OQ-020 | Média | Governança administrativa por escopo |
| OQ-023 | Média | Canal interno com baixa confiança documentada |
| OQ-008 | Média | N equipes na mesma área (N áreas já aprovadas) |
| OQ-009 | Baixa | Cenário de manutenção, não fluxo principal |
| OQ-010 | Baixa | Depende de validação técnica e de negócio conjunta |
| OQ-014 | Baixa | Refinamento conceitual de aggregate |
| OQ-015 | Baixa | Consequência secundária de quota |
| OQ-019 | Baixa | Detalhamento de BR-005 |
| OQ-021 | Baixa | Capacidade periférica com status parcial |
| OQ-022 | Baixa | Capacidade periférica sem backend confirmado |
| OQ-024 | Baixa | Consulta sem mutação de estado |
| OQ-025 | Baixa | Escopo amplo do aggregate de suporte |

---

## Recomendações para Próximos Workshops

| Tema | Objetivo |
| ---- | -------- |
| Integração e onboarding | **DEC-FA-001/002** — especificar FT-PRIMEIRO-ACESSO; OQ-007 restante |
| Multi-contexto e sessão | **DEC-FA-003** — supersession parcial DH-02 (1 vínculo); Contexto Ativo derivado |
| Painel inicial | **DEC-FA-004** — contrato de Home no backend |
| Perfis de acesso externo | Definir parceiro autorizado vs. convidado e critérios de elegibilidade |
| Solicitação e revogação de permissão | Confirmar ciclo de vida completo e papel do responsável pelo recurso |
| Compartilhamento e visibilidade | Alinhar regras de exposição, escopo de federação e herança em pastas |
| Comunicados e canais internos | Estabelecer fronteira entre categoria de documento e publicação institucional |
| Governança administrativa | Validar matriz de papéis por escopo organizacional |
| Capacidades periféricas | Decidir escopo de Central de Colaboração, Fique por Dentro e métricas administrativas |

---

## Nível de Confiança Geral da Camada Domain

**Médio-Alto**

| Artefato | Maturidade | Observação |
| -------- | ---------- | ---------- |
| Visão de domínio (`01-vision`) | Alto | Núcleo do problema e fluxo de valor estáveis |
| Glossário (`02-business-glossary`) | Médio-Alto | Vocabulário central consolidado; ambiguidades registradas |
| Linguagem ubíqua (`03-ubiquitous-language`) | Médio-Alto | Termos oficiais definidos; lacunas terminológicas remanescentes |
| Conceitos (`04-domain-concepts`) | Médio-Alto | Mapa conceitual coerente; ambiguidades residuais documentadas |
| Bounded Contexts (`05-bounded-contexts`) | Médio-Alto | Quatro contextos estáveis; Comunicação Interna com ressalvas |
| Context Map (`06-context-map`) | Médio-Alto | Dependências do núcleo claras; fronteiras sensíveis identificadas |
| Domain Events (`07-domain-events`) | Médio-Alto | Fluxo principal catalogado; eventos periféricos com baixa confiança |
| Aggregates (`08-aggregates`) | Médio-Alto | Três aggregates centrais estáveis; Comunicação Interna e fronteiras em lacuna |
| Business Rules (`09-business-rules`) | Médio-Alto | 39 regras catalogadas; lacunas em revogação, parceiro e comunicado |

A classificação geral é **Médio-Alto** porque a camada Domain possui base conceitual sólida para o núcleo (organização → documentos → acesso → notificação), mas questões abertas em perfis externos, ciclo de vida de permissões, comunicados e capacidades periféricas impedem classificação como **Alto** até validação com stakeholders.

---

## Fontes Utilizadas

### Fonte primária

- `docs/domain/09-business-rules.md`
- `docs/domain/08-aggregates.md`
- `docs/domain/07-domain-events.md`

*Nenhuma fonte Discovery ou código foi consultada. Questões consolidadas exclusivamente a partir de lacunas, ambiguidades e itens de baixa confiança já documentados na camada Domain, conforme a Regra de Ouro.*
