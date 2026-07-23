# Feature Session — Singular

| Item | Valor |
|------|-------|
| Feature Code | FT-SINGULAR |
| Feature Slug | singular |
| Sprint | 3 |
| Data da sessão | 2026-07-14 |
| Agente | construction-orchestrator |
| SSOD | `construction/features/FT-SINGULAR/feature-manifest.yaml` |
| Estado operacional | `construction/features/FT-SINGULAR/construction-state.yaml` |
| Imutabilidade | **READ ONLY** após criação (SESSION-01) |

---

# Regra SESSION-01 / STATE-04

Esta Session é **imutável** durante toda a execução da Feature.

Progresso operacional: `construction/features/FT-SINGULAR/construction-state.yaml` (SSOT).

---

# Snapshot de Contexto

## Feature

| Campo | Valor |
|-------|-------|
| Code | FT-SINGULAR |
| Slug | singular |
| Tipo | business_feature (CRUD Reference) |
| Objetivo | CRUD de Singulares organizacionais vinculadas a Federações |

## Objetivos

- Cadastrar singular vinculada a federação ativa
- Consultar singular por identificador
- Listar singulares com paginação, filtros e ordenação
- Atualizar dados cadastrais respeitando imutabilidade de federação
- Ativar/inativar logicamente (sem exclusão física)
- Validar unicidade global de sigla e código Unimed
- Bloquear inativação quando houver áreas ativas vinculadas (FT-AREA)

## Premissas

- Platform Foundation encerrada (`phase: closed`)
- FT-AUTH encerrada — autenticação JWT via cookie disponível
- FT-AREA encerrada — `AreaRepository` disponível para RN-SINGULAR-006
- Tabela `SINGULAR` provisionada pelo DBA (DDL em `database/ddl/`)
- Federação validada via seed DDL (`008-initial-data.sql`) e/ou `FederacaoRepository` mínimo
- `SingularEntity` e `SingularRepository` mínimos existentes (scaffold FT-AREA)
- Autorização administrativa incremental via lista de e-mails (OQ-020 pendente)

## Restrições

- Sem exclusão física (RN-SINGULAR-005)
- `federacaoId` imutável após cadastro (RN-SINGULAR-007)
- Backend apenas — frontend em Sprint posterior
- Não alterar Platform Foundation
- Prefixo `/api/v1` obrigatório
- FT-FEDERACAO CRUD fora de escopo — referência mínima de federação

## Contratos

| Endpoint | Método | Autenticação | Autorização (escrita) |
|----------|--------|--------------|----------------------|
| `/api/v1/singulares` | POST | Obrigatória | Administrador global |
| `/api/v1/singulares/{id}` | GET | Obrigatória | Autenticado |
| `/api/v1/singulares` | GET | Obrigatória | Autenticado |
| `/api/v1/singulares/{id}` | PUT | Obrigatória | Administrador no escopo |
| `/api/v1/singulares/{id}/status` | PATCH | Obrigatória | Administrador global |

**DTOs:** `CreateSingularRequest`, `UpdateSingularRequest`, `UpdateSingularStatusRequest`, `SingularResponse`.

**Respostas:** `ApiResponse<T>`, `PageResponse<T>`, `ErrorResponse` / `ValidationErrorResponse`.

**Status API:** `ACTIVE`/`INACTIVE` ↔ `FLG_ATIVO` `S`/`N`.

**Filtros listagem:** `status`, `federacaoId`, `name`, `acronym`, `codigoUnimed`, `page`, `size`, `sort`.

## Dependências

| Dependência | Status |
|-------------|--------|
| Platform Foundation | ✅ `phase: closed` |
| FT-AUTH | ✅ `phase: closed` |
| FT-AREA | ✅ `phase: closed` |
| `specs/features/singular/` | ✅ Approved v1.1.1 |
| Tabela SINGULAR (DDL) | ✅ Disponível |
| Seed FEDERACAO | ✅ `008-initial-data.sql` |

## Decisões

| ID | Decisão |
|----|---------|
| DS-SINGULAR-01 | Evoluir `SingularEntity`/`SingularRepository` existentes em vez de recriar |
| DS-SINGULAR-02 | `FederacaoRepository` mínimo para RN-SINGULAR-001 (validação existência/ativo) |
| DS-SINGULAR-03 | Reutilizar padrão de autorização administrativa de FT-AREA (`sessionAdministratorEmails`) |
| DS-SINGULAR-04 | `AreaRepository.existsBySingularIdAndAtivo` para RN-SINGULAR-006 |

## PKGs

| PKG | Nome | Tarefas | Dependências |
|-----|------|---------|--------------|
| PKG-01 | Singular Scaffold & Persistence | Entity, Repository, FederacaoRepository, DTOs, Mapper, Enum | PF |
| PKG-02 | Create Singular | POST, RN-001..004 | PKG-01 |
| PKG-03 | Read & List | GET/{id}, GET list | PKG-01 |
| PKG-04 | Update Singular | PUT, RN-001..004, 007 | PKG-02 |
| PKG-05 | Status Change | PATCH/status, RN-005..006 | PKG-02, FT-AREA |
| PKG-06 | Acceptance & Closure | AT-SINGULAR-001..005 | PKG-02..05 |

## Artefatos

| Camada | Artefato | Pontos-chave |
|--------|----------|--------------|
| Especificação | `specification.md` | 5 RF, 7 RN, 5 RNF |
| API | `api.md` | 5 endpoints, 4 DTOs |
| Tasks | `tasks.md` | TK-SINGULAR-001..005 |
| Aceite | `acceptance-tests.md` | AT-SINGULAR-001..005 |
| Rastreabilidade | `traceability.md` | COMPLETE |
| Engenharia | `docs/implementation/07-api-standards.md` | Padrões corporativos |

## Riscos

| Risco | Mitigação |
|-------|-----------|
| Sem `FederacaoEntity` | `FederacaoRepository` mínimo + seed DDL |
| Matriz de permissões incompleta (OQ-020) | Autorização via e-mail administrador (incremental) |
| RN-SINGULAR-006 depende de FT-AREA | Adicionar query em `AreaRepository` |
| Build com 1 falha pré-existente | Corrigir em PKG-06 / encerramento |

## Pendências

- OQ-SINGULAR-001: reativação com federação inativa — não bloqueante
- Nenhuma bloqueante para início da implementação

---

# Definition of Ready

| Critério | Atendido |
|----------|----------|
| Manifesto presente | ✅ |
| specification completa | ✅ |
| tasks com backlog | ✅ |
| acceptance-tests definidos | ✅ |
| dependências conhecidas | ✅ |
| decisões bloqueantes resolvidas | ✅ |

---

# Validação de Consistência

| Verificação | Resultado |
|-------------|-----------|
| Manifesto válido e completo | ✅ |
| Sem conflito specs vs docs | ✅ |
| Ordem de PKGs válida | ✅ |
| DoR atendida | ✅ |

---

# Próximo Passo

Executar PKG-01 — Singular Scaffold & Persistence.
