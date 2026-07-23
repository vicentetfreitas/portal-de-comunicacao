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
| Feature ID | FT-EQUIPE |
| Feature | Equipe |
| Domínio | EQUIPE |
| Tipo | CRUD Reference Implementation |
| Status | APPROVED |

---

# Objetivo

Permitir o cadastro, consulta, listagem, atualização e ativação/inativação de **Equipes** organizacionais no Portal de Comunicação, como agrupamento operacional vinculado a uma **Área**, com referência opcional a líder (colaborador).

A Feature materializa o agregado **Organização Corporativa** para o conceito **Equipe**, evoluindo o scaffold mínimo criado em FT-AREA.

**Fonte consultiva:** `docs/domain/04-domain-concepts.md`, `docs/domain/09-business-rules.md` (BR-007, BR-012, BR-034), `database/model/03-physical-model.md` (EQUIPE).

---

# Escopo

## Incluído

- Cadastro de equipe vinculada a uma área ativa
- Consulta de equipe por identificador
- Listagem paginada com filtros e ordenação corporativos
- Atualização de dados cadastrais da equipe
- Ativação e inativação lógica (sem exclusão física)
- Validação de integridade referencial com área e colaborador líder
- Auditoria de criação e atualização
- Exposição via API REST versionada (`/api/v1/equipes`)

## Fora do Escopo

- Exclusão física de registros
- Gestão de membros/colaboradores (FT-COLABORADOR)
- Gestão de contatos institucionais (entidade CONTATO)
- Matriz completa de permissões por papel (OQ-020)
- Múltiplos líderes por equipe (DEC-DB-015)
- Telas legadas de membros, permissões e documentos por equipe (ver `specification-frontend.md` — fora do escopo MVP)

---

# Atores

| Ator | Descrição |
|------|-----------|
| Administrador Global | Gestão institucional em escopo federativo |
| Administrador de Singular | Gestão de equipes das áreas da sua singular |
| Administrador de Área | Gestão de equipes da própria área |
| Proprietário de Equipe | Gestão limitada conforme autorização futura |
| Usuário Autenticado | Leitura conforme política de autorização |

---

# Requisitos Funcionais

## RF-EQUIPE-001 — Cadastrar Equipe

| Campo | Valor |
|--------|--------|
| Identificador | RF-EQUIPE-001 |
| Descrição | Cadastrar nova equipe informando área, nome e atributos opcionais (descrição, líder). |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-001 |

## RF-EQUIPE-002 — Consultar Equipe por Identificador

| Campo | Valor |
|--------|--------|
| Identificador | RF-EQUIPE-002 |
| Descrição | Consultar equipe existente pelo identificador. |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-002 |

## RF-EQUIPE-003 — Listar Equipes

| Campo | Valor |
|--------|--------|
| Identificador | RF-EQUIPE-003 |
| Descrição | Listar equipes com paginação, ordenação e filtros (status, área, nome). |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-003 |

## RF-EQUIPE-004 — Atualizar Equipe

| Campo | Valor |
|--------|--------|
| Identificador | RF-EQUIPE-004 |
| Descrição | Atualizar dados cadastrais respeitando imutabilidade de área. |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-004 |

## RF-EQUIPE-005 — Alterar Status da Equipe

| Campo | Valor |
|--------|--------|
| Identificador | RF-EQUIPE-005 |
| Descrição | Ativar ou inativar logicamente sem remoção física. |
| Prioridade | Must |
| Casos de Uso | UC-EQUIPE-005 |

---

# Regras de Negócio

## RN-EQUIPE-001 — Vínculo obrigatório com Área

| Campo | Valor |
|--------|--------|
| Identificador | RN-EQUIPE-001 |
| Descrição | Toda equipe deve pertencer a exatamente uma área válida e ativa. |
| Motivação | BR-007 — Equipe pertence a uma área. |
| Impacto | Rejeita área inexistente ou inativa no cadastro; na atualização valida área vinculada ativa. |
| Requisitos | RF-EQUIPE-001, RF-EQUIPE-004 |

## RN-EQUIPE-002 — Nome obrigatório

| Campo | Valor |
|--------|--------|
| Identificador | RN-EQUIPE-002 |
| Descrição | Nome obrigatório, máximo 200 caracteres. |
| Motivação | Modelo físico `NOM_EQUIPE NOT NULL`. |
| Impacto | Validação de payload na API. |
| Requisitos | RF-EQUIPE-001, RF-EQUIPE-004 |

## RN-EQUIPE-003 — Unicidade de nome por área

| Campo | Valor |
|--------|--------|
| Identificador | RN-EQUIPE-003 |
| Descrição | Não pode existir mais de uma equipe ativa com o mesmo nome na mesma área. |
| Motivação | Integridade organizacional. |
| Impacto | HTTP 422 em conflito. |
| Requisitos | RF-EQUIPE-001, RF-EQUIPE-004 |

## RN-EQUIPE-004 — Líder colaborador válido

| Campo | Valor |
|--------|--------|
| Identificador | RN-EQUIPE-004 |
| Descrição | Quando informado, o líder deve referenciar colaborador existente e ativo. |
| Motivação | DEC-DB-015 — `EQUIPE.COD_LIDER` referencia colaborador. |
| Impacto | Cadastro e atualização rejeitam líder inválido. |
| Requisitos | RF-EQUIPE-001, RF-EQUIPE-004 |

## RN-EQUIPE-005 — Inativação lógica

| Campo | Valor |
|--------|--------|
| Identificador | RN-EQUIPE-005 |
| Descrição | Remoção exclusivamente lógica via `FLG_ATIVO = 'N'`. |
| Motivação | Padrão corporativo soft delete. |
| Impacto | PATCH `/status`; sem DELETE. |
| Requisitos | RF-EQUIPE-005 |

## RN-EQUIPE-006 — Restrição de inativação

| Campo | Valor |
|--------|--------|
| Identificador | RN-EQUIPE-006 |
| Descrição | Não é permitido inativar equipe com colaboradores ativos vinculados (`COLABORADOR.COD_EQUIPE`). |
| Motivação | Preservar integridade do agregado Organização Corporativa. |
| Impacto | HTTP 422. |
| Requisitos | RF-EQUIPE-005 |

## RN-EQUIPE-007 — Área imutável

| Campo | Valor |
|--------|--------|
| Identificador | RN-EQUIPE-007 |
| Descrição | Após cadastro, a área da equipe não pode ser alterada. |
| Motivação | Evitar inconsistência de escopo e vínculos dependentes. |
| Impacto | Campo `areaId` ausente em atualização. |
| Requisitos | RF-EQUIPE-004 |

---

# Requisitos Não Funcionais

## RNF-EQUIPE-001 — Autenticação obrigatória

Todos os endpoints exigem FT-AUTH.

## RNF-EQUIPE-002 — Autorização administrativa

Escritas exigem papel administrativo (regra incremental OQ-020).

## RNF-EQUIPE-003 — Padrões de API corporativos

Aderência a `docs/implementation/07-api-standards.md`.

## RNF-EQUIPE-004 — Persistência Oracle

Tabela `EQUIPE`, schema `UNMPORTCOM`, sequence `SQ_EQUIPE_COD_EQUIPE`.

## RNF-EQUIPE-005 — Auditoria

`DAT_CADASTRO` e `DAT_ATUALIZACAO` obrigatórios conforme Platform Foundation.

---

# Dependências

| Dependência | Tipo | Descrição |
|-------------|------|-----------|
| FT-AUTH | Feature | Autenticação |
| FT-AREA | Feature | Área deve existir para vínculo |
| Platform Foundation | Infraestrutura | Persistência, API, exceções |
| Colaborador (referência) | Domínio | Validação de líder e bloqueio de inativação |

---

# Restrições

- Exclusão física proibida (RN-EQUIPE-005).
- `areaId` imutável após cadastro (RN-EQUIPE-007).

---

# Camada Frontend

A especificação da interface administrativa (rotas, RF-FE, RNF-FE, AT-FE) está em **`specification-frontend.md`**.

O backend desta Feature permanece conforme os RF-EQUIPE-001 a 005 acima; a UI consome o contrato em `api.md` sem alteração de endpoints.

---

# Matriz de Rastreabilidade

Consolidada em `traceability.md`.

| Requisito | Caso de Uso | API | Teste |
|-----------|-------------|-----|--------|
| RF-EQUIPE-001 | UC-EQUIPE-001 | POST /api/v1/equipes | AT-EQUIPE-001 |
| RF-EQUIPE-002 | UC-EQUIPE-002 | GET /api/v1/equipes/{id} | AT-EQUIPE-002 |
| RF-EQUIPE-003 | UC-EQUIPE-003 | GET /api/v1/equipes | AT-EQUIPE-003 |
| RF-EQUIPE-004 | UC-EQUIPE-004 | PUT /api/v1/equipes/{id} | AT-EQUIPE-004 |
| RF-EQUIPE-005 | UC-EQUIPE-005 | PATCH /api/v1/equipes/{id}/status | AT-EQUIPE-005 |

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-07-14 | Specification Engineer | Especificação inicial FT-EQUIPE |
| 1.1 | 2026-07-17 | Specification Reviewer | Gate 1 — aprovação com ressalvas (`review-report.md`) |
