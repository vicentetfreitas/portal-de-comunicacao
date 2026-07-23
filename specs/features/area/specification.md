# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-AREA |
| Feature | Área |
| Domínio | AREA |
| Tipo | CRUD Reference Implementation |
| Status | APPROVED |

---

# Objetivo

Permitir o cadastro, consulta, listagem, atualização e ativação/inativação de **Áreas** organizacionais no Portal de Comunicação, como unidade departamental de **nível único** vinculada a uma **Singular** (ou à Federação), com referência opcional a gestor. Equipes representam o detalhamento operacional da área.

A Feature materializa o agregado **Organização Corporativa** para o conceito **Área**, habilitando a estrutura organizacional necessária para vínculos de colaboradores, equipes e escopos documentais.

**Fonte consultiva:** `docs/domain/04-domain-concepts.md`, `docs/domain/09-business-rules.md` (BR-007, BR-013, BR-034), `database/model/02-logical-model.md` (AREA).

---

# Escopo

## Incluído

- Cadastro de área vinculada a uma singular
- Consulta de área por identificador
- Listagem paginada com filtros e ordenação corporativos
- Atualização de dados cadastrais da área
- Ativação e inativação lógica (sem exclusão física)
- Validação de integridade referencial com singular e colaborador gestor
- Auditoria de criação e atualização (campos de auditoria)
- Exposição via API REST versionada (`/api/v1/areas`)

## Fora do Escopo

- Exclusão física de registros
- Gestão de equipes (FT-EQUIPE)
- Gestão de singulares (FT-SINGULAR)
- Gestão de colaboradores e vínculos organizacionais (Features futuras)
- Gestão de contatos institucionais da área (entidade CONTATO)
- Onboarding e solicitação de vínculo (Features futuras)
- Matriz completa de permissões por papel (OQ-020 — dependência futura)
- Importação, exportação, workflow de aprovação e versionamento
- Frontend da Feature
- Busca unificada transversal (Feature futura)

---

# Atores

| Ator | Descrição |
|------|-----------|
| Administrador Global | Responsável pela gestão institucional em escopo federativo |
| Administrador de Singular | Responsável pela gestão de áreas da sua singular |
| Administrador de Área | Responsável pela gestão da própria área (consulta e manutenção limitada conforme autorização) |
| Usuário Autenticado | Colaborador autenticado via FT-AUTH; acesso de leitura conforme política de autorização |

---

# Requisitos Funcionais

## RF-AREA-001 — Cadastrar Área

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-001 |
| Descrição | O sistema deve permitir cadastrar uma nova área informando singular, nome e demais atributos opcionais (sigla, descrição, gestor). |
| Prioridade | Must |
| Casos de Uso | UC-AREA-001 |

## RF-AREA-002 — Consultar Área por Identificador

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-002 |
| Descrição | O sistema deve permitir consultar uma área existente pelo seu identificador. |
| Prioridade | Must |
| Casos de Uso | UC-AREA-002 |

## RF-AREA-003 — Listar Áreas

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-003 |
| Descrição | O sistema deve permitir listar áreas com paginação, ordenação e filtros corporativos (status, singular, nome, sigla). |
| Prioridade | Must |
| Casos de Uso | UC-AREA-003 |

## RF-AREA-004 — Atualizar Área

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-004 |
| Descrição | O sistema deve permitir atualizar os dados cadastrais de uma área existente, respeitando imutabilidade de atributos definidos em regra de negócio. |
| Prioridade | Must |
| Casos de Uso | UC-AREA-004 |

## RF-AREA-005 — Alterar Status da Área

| Campo | Valor |
|--------|--------|
| Identificador | RF-AREA-005 |
| Descrição | O sistema deve permitir ativar ou inativar logicamente uma área sem remoção física do registro. |
| Prioridade | Must |
| Casos de Uso | UC-AREA-005 |

---

# Regras de Negócio

## RN-AREA-001 — Vínculo obrigatório com Singular

| Campo | Valor |
|--------|--------|
| Identificador | RN-AREA-001 |
| Descrição | Toda área deve pertencer a exatamente uma singular válida e ativa. |
| Motivação | BR-007 — Área pertence a uma singular. |
| Impacto | No cadastro, rejeita singular inexistente ou inativa. Na atualização, rejeita a operação quando a singular vinculada à área estiver inativa (`singularId` imutável conforme RN-AREA-009). |
| Requisitos | RF-AREA-001, RF-AREA-004 |

## RN-AREA-002 — Nome obrigatório

| Campo | Valor |
|--------|--------|
| Identificador | RN-AREA-002 |
| Descrição | O nome da área é obrigatório e deve respeitar o limite de 200 caracteres. |
| Motivação | Modelo físico `NOM_AREA NOT NULL`. |
| Impacto | Validação de payload na API. |
| Requisitos | RF-AREA-001, RF-AREA-004 |

## RN-AREA-003 — Unicidade de nome por singular

| Campo | Valor |
|--------|--------|
| Identificador | RN-AREA-003 |
| Descrição | Não pode existir mais de uma área ativa com o mesmo nome na mesma singular. |
| Motivação | Integridade organizacional e usabilidade administrativa. |
| Impacto | Conflito retorna erro de negócio (HTTP 422). |
| Requisitos | RF-AREA-001, RF-AREA-004 |

## RN-AREA-006 — Gestor colaborador válido

| Campo | Valor |
|--------|--------|
| Identificador | RN-AREA-006 |
| Descrição | Quando informado, o gestor deve referenciar colaborador existente e ativo. |
| Motivação | DEC-DB-015 — `AREA.COD_GESTOR` referencia colaborador. |
| Impacto | Cadastro e atualização rejeitam gestor inexistente ou inativo. |
| Requisitos | RF-AREA-001, RF-AREA-004 |

## RN-AREA-007 — Inativação lógica

| Campo | Valor |
|--------|--------|
| Identificador | RN-AREA-007 |
| Descrição | A remoção de área é exclusivamente lógica via alteração de status para inativo (`FLG_ATIVO = 'N'`). |
| Motivação | Padrão corporativo de soft delete (`docs/implementation/06-database-standards.md`). |
| Impacto | Não há endpoint DELETE; uso de PATCH `/status`. |
| Requisitos | RF-AREA-005 |

## RN-AREA-008 — Restrição de inativação

| Campo | Valor |
|--------|--------|
| Identificador | RN-AREA-008 |
| Descrição | Não é permitido inativar área que possua equipes ativas vinculadas. |
| Motivação | Preservar integridade do agregado Organização Corporativa. |
| Impacto | Inativação bloqueada com erro de negócio (HTTP 422). |
| Requisitos | RF-AREA-005 |

## RN-AREA-009 — Singular imutável

| Campo | Valor |
|--------|--------|
| Identificador | RN-AREA-009 |
| Descrição | Após o cadastro, a singular da área não pode ser alterada. |
| Motivação | Evitar inconsistência de escopo e vínculos dependentes. |
| Impacto | Campo `singularId` ausente ou ignorado em atualização. |
| Requisitos | RF-AREA-004 |

---

# Requisitos Não Funcionais

## RNF-AREA-001 — Autenticação obrigatória

| Campo | Valor |
|--------|--------|
| Identificador | RNF-AREA-001 |
| Descrição | Todos os endpoints exigem usuário autenticado via FT-AUTH. |
| Requisitos | RF-AREA-001 a RF-AREA-005 |

## RNF-AREA-002 — Autorização por escopo administrativo

| Campo | Valor |
|--------|--------|
| Identificador | RNF-AREA-002 |
| Descrição | Operações de escrita (criar, atualizar, alterar status) exigem papel administrativo no escopo da singular da área (Administrador Global, Administrador de Singular ou Administrador de Área conforme matriz futura). Operações de leitura exigem autenticação e respeitam escopo organizacional. |
| Requisitos | RF-AREA-001 a RF-AREA-005 |
| Observação | Matriz detalhada depende de Feature de permissões (OQ-020). Regra mínima documentada para implementação incremental. |

## RNF-AREA-003 — Padrões de API corporativos

| Campo | Valor |
|--------|--------|
| Identificador | RNF-AREA-003 |
| Descrição | A API deve aderir a `docs/implementation/07-api-standards.md` (versionamento, DTOs, paginação, erros, observabilidade). |
| Requisitos | RF-AREA-001 a RF-AREA-005 |

## RNF-AREA-004 — Persistência Oracle

| Campo | Valor |
|--------|--------|
| Identificador | RNF-AREA-004 |
| Descrição | Dados persistidos na tabela `AREA` do schema `UNMPORTCOM`, utilizando sequence `SQ_AREA_COD_AREA`. Schema provisionado pelo DBA (DEC-DB-019). |
| Requisitos | RF-AREA-001 a RF-AREA-005 |

## RNF-AREA-005 — Auditoria

| Campo | Valor |
|--------|--------|
| Identificador | RNF-AREA-005 |
| Descrição | Registros devem manter `DAT_CADASTRO` e `DAT_ATUALIZACAO` conforme padrão de auditoria da Platform Foundation. |
| Requisitos | RF-AREA-001, RF-AREA-004, RF-AREA-005 |

---

# Dependências

| Dependência | Tipo | Descrição |
|-------------|------|-----------|
| FT-AUTH | Feature | Autenticação e identidade do usuário |
| FT-SINGULAR | Feature | Singular deve existir para vínculo da área |
| Platform Foundation | Infraestrutura | Persistência, API, validação, exceções, observabilidade |
| Tabela AREA (DDL DBA) | Banco de dados | Modelo físico aprovado em `database/ddl/` |
| Colaborador (referência) | Domínio | Validação de gestor (`COD_GESTOR`) |

---

# Restrições

- Exclusão física proibida (RN-AREA-007).
- Alteração de singular após cadastro proibida (RN-AREA-009).
- Implementação backend apenas nesta Feature; frontend em Sprint posterior.
- Não duplicar padrões técnicos de `docs/implementation/`.

---

# Critérios de Aceitação

A Feature estará pronta para implementação quando:

- todos os requisitos funcionais estiverem definidos;
- todas as regras de negócio estiverem documentadas;
- todos os casos de uso estiverem identificados;
- existir rastreabilidade completa entre RF, RN, UC, API, AT e TK.

A Feature estará concluída quando:

- todos os critérios de aceitação (AT-AREA-*) forem aprovados;
- testes automatizados cobrirem os cenários Must;
- não existirem bloqueadores de Readiness Review.

---

# Matriz de Rastreabilidade

A matriz consolidada oficial desta Feature está em `traceability.md`.

Referência resumida:

| Requisito | Caso de Uso | API | Teste |
|-----------|-------------|-----|--------|
| RF-AREA-001 | UC-AREA-001 | POST /api/v1/areas | AT-AREA-001 |
| RF-AREA-002 | UC-AREA-002 | GET /api/v1/areas/{id} | AT-AREA-002 |
| RF-AREA-003 | UC-AREA-003 | GET /api/v1/areas | AT-AREA-003 |
| RF-AREA-004 | UC-AREA-004 | PUT /api/v1/areas/{id} | AT-AREA-004 |
| RF-AREA-005 | UC-AREA-005 | PATCH /api/v1/areas/{id}/status | AT-AREA-005 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- utilizar exclusivamente os placeholders padronizados do framework;
- possuir rastreabilidade completa;
- não duplicar padrões corporativos definidos em outras camadas;
- manter consistência com os templates da Feature e com `traceability.md`;
- servir como fonte oficial para Casos de Uso, API e Critérios de Aceitação.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-13 | Specification Engineer | Especificação inicial FT-AREA |
| 1.1 | 2026-07-13 | Specification Engineer | Sincronização Specification Framework v1.1 |
| 1.2.0 | 2026-07-21 | Engineering Framework | Remoção de hierarquia entre áreas (DEC-DB-022); área em nível único |
