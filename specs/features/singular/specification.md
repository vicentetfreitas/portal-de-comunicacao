# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-SINGULAR |
| Feature | Singular |
| Domínio | SINGULAR |
| Tipo | CRUD Reference Implementation |
| Status | APPROVED |

---

# Objetivo

Permitir o cadastro, consulta, listagem, atualização e ativação/inativação de **Singulares** organizacionais no Portal de Comunicação, como unidade cooperativa vinculada à **Federação**, habilitando a estrutura organizacional necessária para áreas, colaboradores, equipes e escopos documentais.

A Feature materializa o agregado **Organização Corporativa** para o conceito **Singular**, evoluindo a entidade mínima introduzida em FT-AREA para CRUD completo e administrável.

**Fonte consultiva:** `docs/domain/09-business-rules.md` (BR-013), `docs/database/model/02-logical-model.md` (SINGULAR), `docs/database/model/03-physical-model.md`.

---

# Escopo

## Incluído

- Cadastro de singular vinculada a uma federação
- Consulta de singular por identificador
- Listagem paginada com filtros e ordenação corporativos
- Atualização de dados cadastrais da singular
- Ativação e inativação lógica (sem exclusão física)
- Validação de integridade referencial com federação
- Validação de unicidade de sigla e código Unimed
- Auditoria de criação e atualização (campos de auditoria)
- Exposição via API REST versionada (`/api/v1/singulares`)

## Fora do Escopo

- Exclusão física de registros
- Gestão de federações (FT-FEDERACAO — referência mínima ou seed)
- Gestão de áreas (FT-AREA)
- Gestão de equipes (FT-EQUIPE)
- Gestão de colaboradores e vínculos organizacionais (Features futuras)
- Gestão de endereços e contatos institucionais (entidades ENDERECO, CONTATO)
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
| Administrador de Singular | Responsável pela consulta e manutenção limitada da própria singular conforme autorização |
| Usuário Autenticado | Colaborador autenticado via FT-AUTH; acesso de leitura conforme política de autorização |

---

# Requisitos Funcionais

## RF-SINGULAR-001 — Cadastrar Singular

| Campo | Valor |
|--------|--------|
| Identificador | RF-SINGULAR-001 |
| Descrição | O sistema deve permitir cadastrar uma nova singular informando federação, nome, sigla e código Unimed. |
| Prioridade | Must |
| Casos de Uso | UC-SINGULAR-001 |

## RF-SINGULAR-002 — Consultar Singular por Identificador

| Campo | Valor |
|--------|--------|
| Identificador | RF-SINGULAR-002 |
| Descrição | O sistema deve permitir consultar uma singular existente pelo seu identificador. |
| Prioridade | Must |
| Casos de Uso | UC-SINGULAR-002 |

## RF-SINGULAR-003 — Listar Singulares

| Campo | Valor |
|--------|--------|
| Identificador | RF-SINGULAR-003 |
| Descrição | O sistema deve permitir listar singulares com paginação, ordenação e filtros corporativos (status, federação, nome, sigla, código Unimed). |
| Prioridade | Must |
| Casos de Uso | UC-SINGULAR-003 |

## RF-SINGULAR-004 — Atualizar Singular

| Campo | Valor |
|--------|--------|
| Identificador | RF-SINGULAR-004 |
| Descrição | O sistema deve permitir atualizar os dados cadastrais de uma singular existente, respeitando imutabilidade de atributos definidos em regra de negócio. |
| Prioridade | Must |
| Casos de Uso | UC-SINGULAR-004 |

## RF-SINGULAR-005 — Alterar Status da Singular

| Campo | Valor |
|--------|--------|
| Identificador | RF-SINGULAR-005 |
| Descrição | O sistema deve permitir ativar ou inativar logicamente uma singular sem remoção física do registro. |
| Prioridade | Must |
| Casos de Uso | UC-SINGULAR-005 |

---

# Regras de Negócio

## RN-SINGULAR-001 — Vínculo obrigatório com Federação

| Campo | Valor |
|--------|--------|
| Identificador | RN-SINGULAR-001 |
| Descrição | Toda singular deve pertencer a exatamente uma federação válida e ativa. |
| Motivação | Modelo físico `COD_FEDERACAO NOT NULL`; hierarquia Federação → Singular (BR-013). |
| Impacto | No cadastro, rejeita federação inexistente ou inativa. Na atualização, rejeita quando a federação vinculada estiver inativa (`federacaoId` imutável conforme RN-SINGULAR-007). |
| Requisitos | RF-SINGULAR-001, RF-SINGULAR-004 |

## RN-SINGULAR-002 — Nome obrigatório

| Campo | Valor |
|--------|--------|
| Identificador | RN-SINGULAR-002 |
| Descrição | O nome da singular é obrigatório e deve respeitar o limite de 200 caracteres. |
| Motivação | Modelo físico `NOM_SINGULAR NOT NULL`. |
| Impacto | Validação de payload na API. |
| Requisitos | RF-SINGULAR-001, RF-SINGULAR-004 |

## RN-SINGULAR-003 — Sigla obrigatória e única

| Campo | Valor |
|--------|--------|
| Identificador | RN-SINGULAR-003 |
| Descrição | A sigla da singular é obrigatória, limitada a 30 caracteres e única em todo o sistema. |
| Motivação | `UK_SINGULAR_SIGLA`. |
| Impacto | Conflito retorna erro de negócio (HTTP 422). |
| Requisitos | RF-SINGULAR-001, RF-SINGULAR-004 |

## RN-SINGULAR-004 — Código Unimed obrigatório e único

| Campo | Valor |
|--------|--------|
| Identificador | RN-SINGULAR-004 |
| Descrição | O código Unimed é obrigatório, limitado a 20 caracteres e único em todo o sistema. |
| Motivação | `UK_SINGULAR_COD_UNIMED`. |
| Impacto | Conflito retorna erro de negócio (HTTP 422). |
| Requisitos | RF-SINGULAR-001, RF-SINGULAR-004 |

## RN-SINGULAR-005 — Inativação lógica

| Campo | Valor |
|--------|--------|
| Identificador | RN-SINGULAR-005 |
| Descrição | A remoção de singular é exclusivamente lógica via alteração de status para inativo (`FLG_ATIVO = 'N'`). |
| Motivação | Padrão corporativo de soft delete (`docs/implementation/06-database-standards.md`). |
| Impacto | Não há endpoint DELETE; uso de PATCH `/status`. |
| Requisitos | RF-SINGULAR-005 |

## RN-SINGULAR-006 — Restrição de inativação

| Campo | Valor |
|--------|--------|
| Identificador | RN-SINGULAR-006 |
| Descrição | Não é permitido inativar singular que possua áreas ativas vinculadas. |
| Motivação | Preservar integridade do agregado Organização Corporativa; RN-AREA-001 exige singular ativa. |
| Impacto | Inativação bloqueada com erro de negócio (HTTP 422). |
| Requisitos | RF-SINGULAR-005 |

## RN-SINGULAR-007 — Federação imutável

| Campo | Valor |
|--------|--------|
| Identificador | RN-SINGULAR-007 |
| Descrição | Após o cadastro, a federação da singular não pode ser alterada. |
| Motivação | Evitar inconsistência de escopo e vínculos dependentes. |
| Impacto | Campo `federacaoId` ausente ou ignorado em atualização. |
| Requisitos | RF-SINGULAR-004 |

---

# Requisitos Não Funcionais

## RNF-SINGULAR-001 — Autenticação obrigatória

| Campo | Valor |
|--------|--------|
| Identificador | RNF-SINGULAR-001 |
| Descrição | Todos os endpoints exigem usuário autenticado via FT-AUTH. |
| Requisitos | RF-SINGULAR-001 a RF-SINGULAR-005 |

## RNF-SINGULAR-002 — Autorização por escopo administrativo

| Campo | Valor |
|--------|--------|
| Identificador | RNF-SINGULAR-002 |
| Descrição | Operações de escrita (criar, atualizar, alterar status) exigem papel administrativo global ou no escopo da singular conforme matriz futura. Operações de leitura exigem autenticação e respeitam escopo organizacional. |
| Requisitos | RF-SINGULAR-001 a RF-SINGULAR-005 |
| Observação | Matriz detalhada depende de Feature de permissões (OQ-020). Regra mínima documentada para implementação incremental. |

## RNF-SINGULAR-003 — Padrões de API corporativos

| Campo | Valor |
|--------|--------|
| Identificador | RNF-SINGULAR-003 |
| Descrição | A API deve aderir a `docs/implementation/07-api-standards.md` (versionamento, DTOs, paginação, erros, observabilidade). |
| Requisitos | RF-SINGULAR-001 a RF-SINGULAR-005 |

## RNF-SINGULAR-004 — Persistência Oracle

| Campo | Valor |
|--------|--------|
| Identificador | RNF-SINGULAR-004 |
| Descrição | Dados persistidos na tabela `SINGULAR` do schema `UNMPORTCOM`, utilizando sequence `SQ_SINGULAR_COD_SINGULAR`. Schema provisionado pelo DBA (DEC-DB-019). |
| Requisitos | RF-SINGULAR-001 a RF-SINGULAR-005 |

## RNF-SINGULAR-005 — Auditoria

| Campo | Valor |
|--------|--------|
| Identificador | RNF-SINGULAR-005 |
| Descrição | Registros devem manter `DAT_CADASTRO` e `DAT_ATUALIZACAO` conforme padrão de auditoria da Platform Foundation. |
| Requisitos | RF-SINGULAR-001, RF-SINGULAR-004, RF-SINGULAR-005 |

---

# Dependências

| Dependência | Tipo | Descrição |
|-------------|------|-----------|
| FT-AUTH | Feature | Autenticação e identidade do usuário |
| FT-AREA | Feature | Consulta a áreas ativas para RN-SINGULAR-006 |
| Platform Foundation | Infraestrutura | Persistência, API, validação, exceções, observabilidade |
| Tabela SINGULAR (DDL DBA) | Banco de dados | Modelo físico aprovado em `docs/database/ddl/` |
| Federação (referência) | Domínio | Validação de `COD_FEDERACAO` — seed ou entidade mínima |

---

# Restrições

- Exclusão física proibida (RN-SINGULAR-005).
- Alteração de federação após cadastro proibida (RN-SINGULAR-007).
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

- todos os critérios de aceitação (AT-SINGULAR-*) forem aprovados;
- testes automatizados cobrirem os cenários Must;
- não existirem bloqueadores de Readiness Review.

---

# Matriz de Rastreabilidade

A matriz consolidada oficial desta Feature está em `traceability.md`.

Referência resumida:

| Requisito | Caso de Uso | API | Teste |
|-----------|-------------|-----|--------|
| RF-SINGULAR-001 | UC-SINGULAR-001 | POST /api/v1/singulares | AT-SINGULAR-001 |
| RF-SINGULAR-002 | UC-SINGULAR-002 | GET /api/v1/singulares/{id} | AT-SINGULAR-002 |
| RF-SINGULAR-003 | UC-SINGULAR-003 | GET /api/v1/singulares | AT-SINGULAR-003 |
| RF-SINGULAR-004 | UC-SINGULAR-004 | PUT /api/v1/singulares/{id} | AT-SINGULAR-004 |
| RF-SINGULAR-005 | UC-SINGULAR-005 | PATCH /api/v1/singulares/{id}/status | AT-SINGULAR-005 |

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
| 1.0 | 2026-07-14 | Specification Engineer | Especificação inicial FT-SINGULAR |
| 1.1.1 | 2026-07-14 | Specification Engineer | Refinamento Gate 1 — NC-01 (clareza RN-SINGULAR-001 × RF-SINGULAR-004) |
