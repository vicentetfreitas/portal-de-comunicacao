# DH-PA-02 — Análise para Decisão Humana
## Domínio do e-mail → Singular

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/primeiro-acesso-dh-pa-02-analysis.md` |
| Feature | FT-PRIMEIRO-ACESSO |
| Data | 2026-08-15 |
| Tipo | **Evidência para decisão humana** |
| Status | **EVIDÊNCIA UTILIZADA** — DH-PA-02 **APROVADA** (2026-08-15); registro definitivo em `docs/governance/03-open-decisions.md` |
| Relacionado | DEC-ORG-003, BR-043, GAP-028-04, DH-PA-01 (aprovada) |

**Classificação usada:** `FATO` · `INFERÊNCIA` · `RECOMENDAÇÃO TÉCNICA` · `DECISÃO HUMANA PENDENTE`.

**Restrições cumpridas nesta etapa:**

- Nenhum código, DDL, migration, JPA, API, frontend, teste ou seed foi alterado.
- Nenhum artefato em `specs/`, `docs/domain/` ou `docs/governance/03-open-decisions.md` foi alterado.
- Nenhuma DEC foi criada ou aprovada em governança nesta etapa.
- Este documento **não** registra decisão — apenas consolida evidências e perguntas para aprovação humana.

> **Atualização (2026-08-15):** **DH-PA-02 APROVADA.** Decisão formalizada em `docs/governance/03-open-decisions.md`. Este artefato permanece como evidência analítica da etapa de investigação.

**Fontes analíticas complementares (não duplicadas integralmente):**

- `construction/review/primeiro-acesso-blocking-decisions-package.md` §6
- `construction/review/vinculo-organizacional-alternative-a-architecture-impact.md` §10

---

## 1. Resumo executivo

A regra de negócio **DEC-ORG-003** (domínio do e-mail autenticado determina a Singular) está **aprovada**, mas o sistema **não possui** hoje persistência nem resolução runtime para essa relação — **GAP-028-04**. O AS-IS usa `defaultFederationId` no login e ignora o domínio do e-mail. Para implementar o fluxo TO-BE (DH-PA-01 + DH-03), é necessário criar SSOT de domínio→Singular e um serviço de resolução no backend. **DH-PA-02** deve decidir principalmente **cardinalidade** (aliases por Singular) e **comportamento para domínio desconhecido**; a escolha entre coluna em `SINGULAR` ou tabela associativa é **consequência técnica** delegável à engenharia.

---

## 2. Premissas já aprovadas

Estas decisões são **vigentes** e orientam esta análise. **Não devem ser rediscutidas.**

| Decisão | Conteúdo normativo relevante para DH-PA-02 | SSOT |
|---------|---------------------------------------------|------|
| **DH-PA-01** | Zimbra confirma identidade; Portal verifica COLABORADOR; sem COLABORADOR → credencial temporária de Primeiro Acesso; após criação do COLABORADOR → estado operacional | `docs/governance/03-open-decisions.md` |
| **DH-02** | 1 vínculo organizacional por COLABORADOR (FKs escalares) | idem |
| **DH-03** | COLABORADOR somente persistido após vínculo mínimo completo (domínio → Singular → Área → Equipe opcional) | idem |
| **DH-04** | Federação + Singular + Área obrigatórios; Equipe opcional | idem |
| **DEC-ORG-003** | Domínio do e-mail determina Singular; backend é autoridade; usuário não escolhe Singular; Área dentro da Singular resolvida; Federação derivada de `SINGULAR.COD_FEDERACAO` | idem + BR-043 |
| **DEC-DB-028** | Modelo de vínculo único; GAP-028-04 (domínio→Singular) pendente | `database/model/05-decisions-and-risks.md` |
| **DEC-DB-027** | `CARGO` obrigatório na criação do COLABORADOR (ortogonal a DH-PA-02; tratado em DH-PA-03) | idem |

**Fluxo TO-BE normativo (DH-PA-01 + DH-03 + DEC-ORG-003):**

```text
Zimbra autentica
    ↓
Portal verifica COLABORADOR
    │
    ├── SIM → acesso operacional
    │
    └── NÃO → credencial temporária (DH-PA-01)
              ↓
            domínio do e-mail
              ↓
            Singular (DEC-ORG-003)
              ↓
            Área (DH-04)
              ↓
            Equipe opcional
              ↓
            criação do COLABORADOR (DH-03)
              ↓
            acesso operacional
```

---

## 3. Evidências encontradas

### 3.1 Governança

| Evidência | Classificação | Referência |
|-----------|---------------|------------|
| DEC-ORG-003 aprovada: domínio pós-Zimbra **determina** Singular; backend é autoridade; usuário **não** escolhe Singular; Área dentro da Singular; Equipe opcional; Federação via `SINGULAR.COD_FEDERACAO` | **FATO** | `docs/governance/03-open-decisions.md` DEC-ORG-003 L838–884 |
| Exemplos ilustrativos: `unimedcariri.com.br` → Unimed Cariri; `unimedceara.com.br` → Unimed Ceará | **FATO** (ilustrativo, não exaustivo) | idem L867–870 |
| Lacunas deliberadas: forma física, cardinalidade, aliases, normalização, wildcard, domínio desconhecido/ambíguo | **FATO** | idem L872–880 |
| DH-PA-02 pendente: persistência física domínio e-mail → Singular | **FATO** | idem L756, L827 |
| DH-PA-01 aprovada (2026-08-15): credencial temporária sem `AUTH_SESSAO` operacional | **FATO** | idem L764–834 |
| GAP-028-04: persistência física domínio → Singular — não aprovada | **FATO** | `database/model/05-decisions-and-risks.md` L973 |

### 3.2 Domínio

| Evidência | Classificação | Referência |
|-----------|---------------|------------|
| BR-043: domínio determina Singular; backend é autoridade; persistência física = **lacuna** | **FATO** | `docs/domain/09-business-rules.md` L55 |
| BR-011: primeiro acesso = domínio → Singular → Área → Equipe opcional → Contexto Ativo | **FATO** | idem L48 |
| BR-026: autenticação vinculada a domínios corporativos da Unimed Ceará | **FATO** | idem L81 |
| BR-026 **não** mapeia domínio a Singular específica | **FATO** | idem — sem menção a Singular |
| Lacuna registrada: persistência física domínio→Singular indefinida | **FATO** | idem L197 |

### 3.3 Banco

| Evidência | Classificação | Referência |
|-----------|---------------|------------|
| Tabela `SINGULAR` sem coluna de domínio de e-mail | **FATO** | `database/ddl/003-create-tables.sql` L58–69 |
| Sem tabela `SINGULAR_DOMINIO` ou equivalente em DDL/migrations | **FATO** | busca em `database/` |
| Constraints em `SINGULAR`: PK, UK em `SIG_SINGULAR` e `COD_UNIMED`, FK para `FEDERACAO` — **sem UK em domínio** | **FATO** | `database/ddl/004-create-constraints.sql` |
| `COLABORADOR.DES_EMAIL` UK; `COD_SINGULAR` nullable — **sem CHECK** ligando sufixo de e-mail a Singular | **FATO** | `database/ddl/003-create-tables.sql` L158–179 |
| Seed `002-singulares.sql`: 3 Singulares (Ceará, Cariri, Sobral) — **sem domínios** | **FATO** | `database/dml/002-singulares.sql` |
| Modelo lógico/físico: `SINGULAR` sem atributo de domínio | **FATO** | `database/model/02-logical-model.md`, `03-physical-model.md`, `04-entity-catalog.md` |

### 3.4 Backend

| Evidência | Classificação | Referência |
|-----------|---------------|------------|
| `SingularEntity` sem campo de domínio | **FATO** | `organization/infrastructure/persistence/entity/SingularEntity.java` |
| `ColaboradorService.createColaborador` usa `authProperties.defaultFederationId()` — **não** resolve domínio | **FATO** | `accesscontrol/application/service/ColaboradorService.java` L48–56 |
| E-mail persistido em lowercase (`identity.email().toLowerCase()`) | **FATO** | idem L50 |
| `findByEmailIgnoreCase` no repositório | **FATO** | idem L33 |
| `ZimbraCredentialValidator.emailDomain()` extrai sufixo pós-`@` apenas para **log diagnóstico** | **FATO** | `accesscontrol/infrastructure/integration/zimbra/ZimbraCredentialValidator.java` L38–39, L111–114 |
| `ColaboradorDomainService.resolveOrganizationalLinks` rejeita Singular inativa | **FATO** | `accesscontrol/application/service/ColaboradorDomainService.java` L107–112 |
| Sem `SingularDomainResolutionService` ou query por domínio em `SingularRepository` | **FATO** | busca em `backend/` |
| `AuthProperties` / `application.yaml`: `default-federation-id` — sem mapa domínio→Singular | **FATO** | `configuration/properties/AuthProperties.java`, `application.yaml` |
| `finalizeLogin` → `locateOrCreate` cria COLABORADOR com `singularId` null no login | **FATO** | `AuthenticationService.java` (via blocking package §4.1–4.2) |
| Sem classes `PrimeiroAcesso*` no backend | **FATO** | busca em `backend/` |

### 3.5 Specs

| Evidência | Classificação | Referência |
|-----------|---------------|------------|
| `specification.md` §11: TO-BE = identidade → domínio → Singular → Área → criação COLABORADOR | **FATO** | `specs/features/primeiro-acesso/specification.md` L224–234 |
| Artefatos PA (`flows.md`, `use-cases.md`, `api.md`, `acceptance-tests.md`) ainda modelam vínculos pré-provisionados e seleção N contextos — **não** representam TO-BE integralmente | **FATO** | idem L232; `flows.md`, `api.md` |
| FT-SINGULAR: CRUD sem campo de domínio; onboarding fora de escopo | **FATO** | `specs/features/singular/specification.md`, `api.md` |
| FT-AUTH / FT-SESSION: identidade via e-mail; **sem** contrato de resolução domínio→Singular | **FATO** | `specs/features/authentication/api.md`, `specs/features/session/specification.md` |
| Nenhum endpoint de resolução de domínio em specs PA | **FATO** | `specs/features/primeiro-acesso/api.md` |

### 3.6 Legado

| Evidência | Classificação | Referência |
|-----------|---------------|------------|
| Sistema legado WordPress: modal de seleção de domínio no login | **FATO** (legado, não SSOT normativo) | `docs/discovery/frontend-production-discovery.md` L157, L484 |
| Domínios legados para recuperação de senha: `unimedceara.com.br`, `unimedcariri.com.br`, `unimedsobral.com.br` | **FATO** (legado, não persistido no novo modelo) | idem L524 |
| Frontend atual (`frontend/src/`): **sem** seleção de domínio no login | **FATO** | busca em `frontend/src/` |
| `ONBOARDING_SOLICITACAO`: tabela DDL reservada; fluxo administrativo legado — **superseded** por primeiro acesso TO-BE | **FATO** | `database/ddl/003-create-tables.sql`; DEC-FA-001 |
| Documento histórico `vinculo-organizacional-flow-reconciliation.md` afirmava domínio **não** determina Singular — **superseded** por DEC-ORG-003 (2026-08-14) | **FATO** | `construction/review/vinculo-organizacional-flow-reconciliation.md` L51 |

---

## 4. Estado atual

### 4.1 Existe relação persistida domínio → Singular?

**FATO:** **Não.** Não há coluna, tabela, configuração SSOT nem seed que persista o mapeamento.

### 4.2 Onde e como está representada?

| Representação | Existe? | Detalhe |
|---------------|---------|---------|
| Coluna em `SINGULAR` | **Não** | DDL sem campo de domínio |
| Tabela de domínios | **Não** | Ausente em `database/` |
| Configuração externa (`application.yaml`) | **Parcial** | Apenas `default-federation-id` — federação fixa, não Singular por domínio |
| Regra implícita no código | **Não** | Nenhuma resolução domínio→Singular implementada |
| Regra de negócio aprovada (sem implementação) | **Sim** | DEC-ORG-003 / BR-043 |

### 4.3 Comportamento AS-IS no login

**FATO:** Após autenticação Zimbra, `ColaboradorService.locateOrCreate` cria ou localiza COLABORADOR com:

- `DES_EMAIL` = e-mail autenticado (lowercase)
- `COD_FEDERACAO` = `defaultFederationId` (configuração)
- `COD_SINGULAR`, `COD_AREA`, `COD_EQUIPE` = **NULL**

**INFERÊNCIA:** O domínio do e-mail é **ignorado** para fins organizacionais no AS-IS.

### 4.4 Comportamento TO-BE esperado (não implementado)

**FATO (normativo):** Durante Primeiro Acesso, o backend deve:

1. Extrair domínio do e-mail autenticado (pós-Zimbra).
2. Resolver Singular correspondente via SSOT persistido.
3. Derivar Federação de `SINGULAR.COD_FEDERACAO`.
4. Apresentar Áreas **somente** da Singular resolvida.
5. **Não** permitir que o usuário escolha outra Singular.

---

## 5. Cardinalidade

### 5.1 O que as evidências permitem afirmar

| Relação | Status | Evidência |
|---------|--------|-----------|
| Uma Singular possui **um** domínio | **Não determinado** | DEC-ORG-003 não fixa cardinalidade; exemplos mostram 1 domínio distinto por Singular, mas não proíbem aliases |
| Uma Singular pode possuir **vários** domínios | **Não determinado** | Lacuna explícita em DEC-ORG-003 L877–878; SQ-P02-03 = INDEFINIDO |
| Um domínio pertence a **uma única** Singular | **Inferência forte, não formalizada** | DEC-ORG-003 exige resolução determinística; ambiguidade inviabilizaria a regra |
| Um domínio pode pertencer a **várias** Singulares | **Não determinado** (deve ser proibido para determinismo) | SQ-P02-04 = INDEFINIDO; recomendação analítica: proibir |

### 5.2 Constraints de schema

**FATO:** Nenhuma constraint de cardinalidade existe — coluna/tabela de domínio inexistente.

### 5.3 Conclusão

> **Cardinalidade não determinada** nas premissas aprovadas. A única relação inferível com segurança é que DEC-ORG-003 **requer** resolução determinística (**INFERÊNCIA**), o que implica **proibir** 1 domínio → N Singulares — mas isso **não está formalmente aprovado** e deve ser confirmado pelo decisor.

### 5.4 Normalização e comparação

| Aspecto | Definido? | Evidência | Classificação |
|---------|-----------|-----------|---------------|
| Lowercase no e-mail completo | Parcialmente | `createColaborador` → `toLowerCase()` | **FATO** |
| Comparação case-insensitive de e-mail | Sim | `findByEmailIgnoreCase` | **FATO** |
| Normalização do domínio (lowercase/trim do sufixo) | **Não** | Extração bruta em `emailDomain()` | **LACUNA** — delegável à engenharia |
| Domínio principal vs alternativo | **Não** | DEC-ORG-003 L878 | **DECISÃO HUMANA PENDENTE** (se aliases permitidos) |
| Subdomínio, wildcard, regex | **Não** | DEC-ORG-003 L879 | **LACUNA** — default técnico: comparação exata |
| Tratamento de espaços | **Não** | — | **LACUNA** — delegável (trim) |
| Domínio desconhecido | **Não** | DEC-ORG-003 L880 | **DECISÃO HUMANA PENDENTE** |

---

## 6. Alternativas

Alternativas **tecnicamente viáveis** suportadas pelas evidências. Nenhuma foi selecionada neste documento.

### 6.1 Tabela comparativa

| Critério | **P1 — Coluna em `SINGULAR`** | **P2 — Tabela `SINGULAR_DOMINIO`** | **P3 — Configuração (`application.yaml`)** |
|----------|-------------------------------|--------------------------------------|---------------------------------------------|
| **Descrição** | `DES_DOMINIO_EMAIL VARCHAR2` na tabela `SINGULAR` | Tabela associativa com FK para `SINGULAR` | Mapa estático em configuração |
| **Representação** | 1 atributo por Singular | N registros por Singular, UK em `DES_DOMINIO` | Chave-valor domínio→singularId |
| **Cardinalidade suportada** | 1 Singular → 1 domínio | 1 Singular → N domínios; 1 domínio → 1 Singular (com UK) | Qualquer (limitado pela manutenção manual) |
| **Vantagens** | Simples; query direta; coeso com catálogo org | Aliases; evolução sem alterar `SINGULAR`; UK garante determinismo | Rápido para protótipo |
| **Desvantagens** | Sem aliases nativos; migration em tabela existente | Mais DDL/JPA/governança; seed e CRUD adicionais | **Não é SSOT** da camada Organização; não escalável |
| **Impacto no banco** | `ALTER TABLE SINGULAR ADD DES_DOMINIO_EMAIL` + UK | `CREATE TABLE SINGULAR_DOMINIO` + FK + UK | Nenhum DDL |
| **Impacto no backend** | Campo em `SingularEntity` + query `findByDominio` | Nova entidade + repositório + serviço de resolução | Bean de configuração — **descartado como SSOT** |
| **Impacto no onboarding** | Resolução no passo 1 do wizard PA | Idem | Idem (mas dados fora do modelo org) |
| **Impacto nos dados existentes** | Seed DML: popular domínio nas 3 Singulares | Seed DML: inserir registros de domínio | Nenhum — mas dados não administráveis |
| **Riscos** | Evolução para aliases exige migration | Complexidade adicional | Divergência config vs banco; bypass de governança org |
| **Evolução** | Limitada a 1 domínio sem refactor | Natural para novos domínios/aliases | Transição apenas |

### 6.2 Domínio desconhecido — investigação específica

| Cenário | Regra existente? | Classificação | Justificativa |
|---------|------------------|---------------|---------------|
| Domínio autenticado sem Singular correspondente | **Não** | **DECISÃO HUMANA PENDENTE** (DA-PA-02a) | DEC-ORG-003 L880 lista explicitamente como lacuna; nenhum UC/flow/API define comportamento |
| Domínio ambíguo (>1 Singular) | **Não** (deve ser impedido por UK) | **INFERÊNCIA** + controle técnico | Determinismo de DEC-ORG-003; UK em `DES_DOMINIO` é engenharia |
| Domínio nulo/inválido (sem `@`) | Parcial | **Controle técnico** | Zimbra rejeita credenciais inválidas; extração retorna `"unknown"` em log |
| Singular resolvida mas **inativa** | Parcial | **INFERÊNCIA** — bloquear | `ColaboradorDomainService` já rejeita Singular inativa em vínculos explícitos |
| Singular sem **Áreas ativas** | **Não** formalizado | **INFERÊNCIA** — bloquear onboarding | DH-04 exige Área obrigatória; sem Área não há vínculo mínimo |
| E-mail de domínio institucional mas Singular não cadastrada | **Não** | **GAP** (LA-04) + **DECISÃO HUMANA PENDENTE** | BR-026 permite domínios institucionais; DEC-ORG-003 não define erro |

---

## 7. Lacunas, GAPs e conflitos

### 7.1 GAPs

| ID | Descrição | Classificação |
|----|-----------|---------------|
| **GAP-028-04** | Persistência física domínio → Singular — não aprovada | **FATO** |
| **LA-02** | SSOT domínio→Singular inexistente | **FATO** |
| **LA-04** | Regras de erro (domínio desconhecido, Singular sem Área) indefinidas | **FATO** |
| **GAP-028-01** | `locateOrCreate` no login contradiz DH-03 | **FATO** (consequência de DH-PA-01, não reabrir) |

### 7.2 Conflitos

| ID | Descrição | Severidade | Resolução |
|----|-----------|------------|-----------|
| **CA-04** | BR-026 fala em "domínios corporativos Unimed Ceará" sem mapear a Singular; DEC-ORG-003 exemplifica múltiplas Singulares com domínios distintos | Baixa | Complementar BR-026 com DEC-ORG-003 — não bloqueante |
| **Specs PA vs TO-BE** | `flows.md`/`api.md` modelam N vínculos pré-provisionados; `specification.md` §11 define TO-BE domain-driven | Média | Reconciliação de specs em etapa posterior — não bloqueia DH-PA-02 |
| **Legado vs TO-BE** | WordPress permitia seleção manual de domínio no login | Informativo | DEC-ORG-003 supersede comportamento legado |
| **AS-IS vs TO-BE** | `defaultFederationId` ignora domínio | Alta (implementação) | Resolvido após DH-PA-02 + implementação PA |
| **Histórico superseded** | `vinculo-organizacional-flow-reconciliation.md` negava domínio→Singular | Informativo | DEC-ORG-003 (2026-08-14) prevalece |

---

## 8. Segurança

Análise restrita à cadeia: **identidade autenticada → domínio → Singular**.

### 8.1 Regras de negócio (aprovadas)

| Item | Classificação |
|------|---------------|
| Backend é autoridade na resolução domínio→Singular | **FATO** (DEC-ORG-003.2) |
| Usuário não pode selecionar Singular diferente da determinada pelo domínio | **FATO** (DEC-ORG-003.3) |
| Frontend não determina Singular | **FATO** (DEC-ORG-003.2) |

### 8.2 Decisões arquiteturais (pendentes ou deriváveis)

| Item | Classificação |
|------|---------------|
| SSOT do mapeamento na camada Organização (banco, não config) | **RECOMENDAÇÃO TÉCNICA** |
| UK em domínio para impedir ambiguidade | **RECOMENDAÇÃO TÉCNICA** |
| Comportamento para domínio desconhecido | **DECISÃO HUMANA PENDENTE** |

### 8.3 Controles técnicos de segurança

| Risco | Mitigação | Classificação |
|-------|-----------|---------------|
| Cliente alterar domínio independentemente do e-mail autenticado | Domínio derivado **server-side** do e-mail retornado pelo Zimbra após autenticação — nunca aceitar domínio como input livre do cliente | **RECOMENDAÇÃO TÉCNICA** |
| Confiança no e-mail retornado pelo Zimbra | Zimbra é fonte de identidade (DH-PA-01); domínio é sufixo do e-mail autenticado | **FATO** |
| Ambiguidade (múltiplas Singulares para mesmo domínio) | UK em `DES_DOMINIO`; falhar se >1 match | **RECOMENDAÇÃO TÉCNICA** |
| Bypass de escopo organizacional via `singularId` no request | Onboarding não deve aceitar `singularId` do cliente; resolver server-side | **RECOMENDAÇÃO TÉCNICA** |
| Normalização inconsistente (`UnimedCeara.com.br` vs `unimedceara.com.br`) | Lowercase + trim no sufixo antes da query | **RECOMENDAÇÃO TÉCNICA** (não é decisão humana) |
| Fallback para `defaultFederationId` | **Remover** no fluxo PA — permitiria bypass de DEC-ORG-003 | **RECOMENDAÇÃO TÉCNICA** |
| Singular inativa resolvida por domínio | Rejeitar — alinhar a `resolveOrganizationalLinks` | **INFERÊNCIA** (delegável) |

---

## 9. Impactos

### 9.1 Banco

- Nova estrutura de persistência (P1 ou P2).
- Seed DML com domínios das Singulares existentes (Ceará, Cariri, Sobral — domínios a confirmar com negócio).
- UK em domínio para garantir determinismo.
- Sem backfill de COLABORADOR necessário (ambiente sem colaboradores cadastrados — **FATO**, DEC-DB-028).

### 9.2 Backend

- Novo serviço de resolução (ex.: `SingularDomainResolutionService`).
- Integração no fluxo de Primeiro Acesso (após credencial temporária DH-PA-01).
- Remoção de dependência de `defaultFederationId` para resolução de Singular no onboarding.
- Reutilização de validação de Singular ativa (`ColaboradorDomainService`).

### 9.3 API

- Endpoint ou passo embutido de resolução (ex.: retorno da Singular resolvida no primeiro passo do onboarding).
- Contrato de erro para domínio desconhecido (depende de DA-PA-02a).
- APIs de listagem de Área filtradas por `singularId` resolvido server-side.

### 9.4 Onboarding (Primeiro Acesso)

- Passo 1 do wizard: Singular **exibida** (não selecionável) após resolução.
- Passo 2: seleção de Área **dentro** da Singular resolvida.
- Bloqueio total se domínio desconhecido (se decisor aprovar DA-PA-02a = bloquear).

### 9.5 Frontend

- Remover/evitar qualquer UI de seleção de Singular no PA.
- Exibir Singular resolvida como informação (read-only).
- Tratar estados de erro (domínio desconhecido, Singular sem Áreas).
- Legado WordPress tinha modal de domínio — **não** replicar no TO-BE.

### 9.6 Testes

- Fixtures com domínios mapeados para as 3 Singulares seed.
- Casos: domínio conhecido, desconhecido, Singular inativa, Singular sem Área, case-insensitive.
- Testes de integração no fluxo PA pós-DH-PA-01.

### 9.7 Coerência com DH-PA-01

**FATO:** A resolução domínio→Singular ocorre **dentro** do fluxo de credencial temporária, **antes** da criação do COLABORADOR e **antes** da seleção de Área. Não impacta usuários com COLABORADOR existente (acesso operacional direto).

---

## 10. Decisões humanas necessárias

**Menor conjunto** de questões que engenharia **não pode** derivar das premissas aprovadas:

| # | ID | Questão | Por que é decisão humana |
|---|-----|---------|--------------------------|
| 1 | **SQ-P02-03** | Uma Singular pode possuir **múltiplos domínios** de e-mail (aliases)? | Afeta cardinalidade e modelo de dados; não está definido em DEC-ORG-003 |
| 2 | **DA-PA-02a** | O que o Portal faz quando o domínio autenticado **não possui** Singular correspondente? | Lacuna explícita em DEC-ORG-003; impacto UX e política de acesso |
| 3 | **SQ-P02-04** | Confirmar que **um domínio pertence a no máximo uma Singular** (proibição de ambiguidade)? | Inferível de DEC-ORG-003, mas não formalmente aprovado |

**Não são decisões humanas** (delegáveis):

- P1 vs P2 (consequência de SQ-P02-03)
- Nome de tabela/coluna, tipo Oracle, índice, JPA, repository, endpoint, DTO, SQL
- Normalização (lowercase, trim, comparação exata)
- Bloqueio de Singular inativa (DA-PA-02b) — **default derivável** de regras existentes
- Bloqueio de Singular sem Área ativa (DA-PA-02c) — **default derivável** de DH-04

---

## 11. Pontos delegáveis à engenharia

Após as decisões humanas acima, a engenharia pode definir autonomamente:

| Área | Escopo delegado |
|------|-----------------|
| **Persistência** | P1 se SQ-P02-03 = Não; P2 se SQ-P02-03 = Sim |
| **DDL/Migration** | `ALTER SINGULAR` ou `CREATE SINGULAR_DOMINIO` + constraints |
| **JPA/Repository** | Entidade, query `findActiveSingularByEmailDomain` |
| **Serviço** | `SingularDomainResolutionService` — extrair domínio, normalizar, resolver, validar ativo |
| **Normalização** | Lowercase + trim do sufixo pós-`@`; comparação exata; sem wildcard |
| **UK/Índice** | `UK_SINGULAR_DOMINIO_DES_DOMINIO` (ou equivalente em P1) |
| **Seed DML** | Popular domínios conhecidos (ex.: `unimedceara.com.br`, `unimedcariri.com.br`, `unimedsobral.com.br`) |
| **API** | Contrato técnico do endpoint de resolução / passo de onboarding |
| **Erro técnico** | HTTP status, error code, i18n — dado comportamento aprovado em DA-PA-02a |
| **DA-PA-02b** | Rejeitar Singular inativa na resolução (padrão existente) |
| **DA-PA-02c** | Rejeitar Singular sem Áreas ativas (padrão DH-04) |
| **Segurança** | Resolução server-side only; sem `singularId` do cliente no PA |
| **Testes** | Unitários, integração, fixtures |
| **Frontend** | Exibição read-only da Singular; tratamento de erro conforme DA-PA-02a |

---

## 12. Recomendação técnica

> **RECOMENDAÇÃO TÉCNICA DA IA — NÃO É DECISÃO HUMANA**

| Cenário (após decisão humana) | Recomendação |
|-------------------------------|--------------|
| SQ-P02-03 = **Não** (1 domínio por Singular, sem aliases) | **P1** — coluna `DES_DOMINIO_EMAIL` em `SINGULAR` com UK |
| SQ-P02-03 = **Sim** (aliases permitidos) | **P2** — tabela `SINGULAR_DOMINIO` com UK em `DES_DOMINIO` |
| SSOT | Persistir no banco (camada Organização) — **rejeitar P3** como SSOT normativo |
| SQ-P02-04 | Implementar UK em domínio — garante proibição técnica de 1 domínio → N Singulares |
| DA-PA-02a (default analítico, se decisor não especificar) | Bloquear onboarding com mensagem clara — usuário autenticado mas sem Singular mapeada não pode prosseguir |
| DA-PA-02b / DA-PA-02c | Bloquear — alinhar a `resolveOrganizationalLinks` e DH-04 |
| Normalização | `domain = email.substringAfter('@').trim().toLowerCase(Locale.ROOT)` |
| Integração PA | Resolver domínio no primeiro passo do wizard, antes de listar Áreas |

---

## 13. Perguntas para o decisor

Objetivas para aprovação/rejeição. **Não constituem decisão até registro em governança.**

### Cardinalidade

- [ ] **SQ-P02-03** — Uma Singular pode possuir múltiplos domínios de e-mail (aliases)?
  - [ ] **Sim** → engenharia implementará tabela associativa (P2)
  - [ ] **Não** → engenharia implementará coluna única em `SINGULAR` (P1)

- [ ] **SQ-P02-04** — Um domínio de e-mail pertence a no máximo uma Singular (proibição de ambiguidade)?
  - [ ] **Sim, proibir** (recomendado para DEC-ORG-003)
  - [ ] **Não** — especificar regra de desempate: _______________

### Domínio desconhecido

- [ ] **DA-PA-02a** — Quando o domínio autenticado não possui Singular correspondente, o Portal deve:
  - [ ] Bloquear onboarding (usuário autenticado, sem prosseguir)
  - [ ] Exibir mensagem com contato administrativo: _______________
  - [ ] Outro: _______________

### Confirmações deriváveis (opcional — silêncio = aceitar default analítico)

- [ ] **DA-PA-02b** — Singular inativa resolvida por domínio: bloquear? (default: **sim**)
- [ ] **DA-PA-02c** — Singular sem Áreas ativas: bloquear onboarding? (default: **sim**, alinha DH-04)

### Persistência (delegada — informar apenas se discordar da consequência)

- [ ] Aceito que a escolha P1/P2 seja consequência automática da resposta de SQ-P02-03
- [ ] **Rejeito P3** (configuração) como SSOT organizacional

### Domínios seed (informação para cadastro inicial — não bloqueia decisão)

Confirmar domínios institucionais para seed (baseado em evidências legadas + DEC-ORG-003):

| Singular (seed) | Domínio sugerido (evidência) | Confirmar |
|-----------------|------------------------------|-----------|
| Unimed Ceará | `unimedceara.com.br` | [ ] |
| Unimed Cariri | `unimedcariri.com.br` | [ ] |
| Unimed Sobral | `unimedsobral.com.br` | [ ] |

---

## Apêndice — Rastreabilidade

| Decisão/Regra | Artefato |
|---------------|----------|
| DEC-ORG-003 | `docs/governance/03-open-decisions.md` |
| BR-043, BR-011, BR-026 | `docs/domain/09-business-rules.md` |
| GAP-028-04 | `database/model/05-decisions-and-risks.md` |
| DH-PA-01/02/03 | `docs/governance/03-open-decisions.md` |
| Análise consolidada prévia | `construction/review/primeiro-acesso-blocking-decisions-package.md` |
| Impacto arquitetural | `construction/review/vinculo-organizacional-alternative-a-architecture-impact.md` |
| TO-BE PA | `specs/features/primeiro-acesso/specification.md` §11 |

---

| Versão | 1.0 |
|--------|-----|
| Status | EVIDÊNCIA PARA DECISÃO HUMANA |
| Próximo passo | Sessão de decisão DH-PA-02 → registro em `docs/governance/03-open-decisions.md` |
