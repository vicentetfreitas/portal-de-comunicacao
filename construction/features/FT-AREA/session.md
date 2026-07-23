# Feature Session — Área

| Item | Valor |
|------|-------|
| Feature Code | FT-AREA |
| Feature Slug | area |
| Sprint | 2 |
| Data da sessão | 2026-07-13 |
| Agente | construction-orchestrator |
| SSOD | `construction/features/FT-AREA/feature-manifest.yaml` |
| Estado operacional | `construction/features/FT-AREA/construction-state.yaml` |
| Imutabilidade | **READ ONLY** após criação (SESSION-01) |

---

# Regra SESSION-01 / STATE-04

Esta Session é **imutável** durante toda a execução da Feature.

Progresso operacional: `construction/features/FT-AREA/construction-state.yaml` (SSOT).

---

# Snapshot de Contexto

## Feature

| Campo | Valor |
|-------|-------|
| Code | FT-AREA |
| Slug | area |
| Tipo | business_feature (CRUD Reference) |
| Objetivo | CRUD de Áreas organizacionais vinculadas a Singulares |

## Objetivos

- Cadastrar área vinculada a singular ativa
- Consultar área por identificador
- Listar áreas com paginação, filtros e ordenação
- Atualizar dados cadastrais respeitando imutabilidade de singular
- Ativar/inativar logicamente (sem exclusão física)
- Validação de integridade com singular e gestor (colaborador)
- Validar gestor colaborador ativo

## Premissas

- Platform Foundation encerrada (`phase: closed`)
- FT-AUTH encerrada — autenticação JWT via cookie disponível
- Tabela `AREA` provisionada pelo DBA (DDL em `database/ddl/`)
- Entidade `Singular` mínima para validação referencial (FT-SINGULAR fora de escopo)
- Autorização administrativa incremental via lista de e-mails (OQ-020 pendente)

## Restrições

- Sem exclusão física (RN-AREA-007)
- `singularId` imutável após cadastro (RN-AREA-009)
- Backend apenas — frontend em Sprint posterior
- Não alterar Platform Foundation
- Prefixo `/api/v1` obrigatório

## Contratos

| Endpoint | Método | Autenticação | Autorização (escrita) |
|----------|--------|--------------|----------------------|
| `/api/v1/areas` | POST | Obrigatória | Administrador |
| `/api/v1/areas/{id}` | GET | Obrigatória | Autenticado |
| `/api/v1/areas` | GET | Obrigatória | Autenticado |
| `/api/v1/areas/{id}` | PUT | Obrigatória | Administrador |
| `/api/v1/areas/{id}/status` | PATCH | Obrigatória | Administrador |

**DTOs:** `CreateAreaRequest`, `UpdateAreaRequest`, `UpdateAreaStatusRequest`, `AreaResponse`.

**Respostas:** `ApiResponse<T>`, `PageResponse<T>`, `ErrorResponse` / `ValidationErrorResponse`.

**Status API:** `ACTIVE`/`INACTIVE` ↔ `FLG_ATIVO` `S`/`N`.

## Dependências

| Dependência | Status |
|-------------|--------|
| Platform Foundation | ✅ `phase: closed` |
| FT-AUTH | ✅ `phase: closed` |
| `specs/features/area/` | ✅ Approved v1.1.1 |
| Tabela AREA (DDL) | ✅ Disponível |
| Colaborador (gestor) | ✅ Entidade mínima FT-AUTH |

## Decisões

| ID | Decisão |
|----|---------|
| — | Reutilizar padrão `ColaboradorEntity` para mapeamento Oracle |
| — | BC `organization` para Área, Singular mínima e Equipe mínima |
| — | Autorização administrativa via `sessionAdministratorEmails` (incremental) |

## PKGs

| PKG | Nome | Tarefas | Dependências |
|-----|------|---------|--------------|
| PKG-01 | Organization Scaffold & Persistence | Entity, Repository, DTOs, Mapper | PF |
| PKG-02 | Create Area | POST, RN-001..006 | PKG-01 |
| PKG-03 | Read & List | GET/{id}, GET list | PKG-01 |
| PKG-04 | Update Area | PUT, RN-004..006, 009 | PKG-02 |
| PKG-05 | Status Change | PATCH/status, RN-007..008 | PKG-02 |
| PKG-06 | Acceptance & Closure | AT-AREA-001..005 | PKG-02..05 |

## Artefatos

| Camada | Artefato | Pontos-chave |
|--------|----------|--------------|
| Especificação | `specification.md` | 5 RF, 9 RN, 5 RNF |
| API | `api.md` | 5 endpoints, 4 DTOs |
| Tasks | `tasks.md` | TK-AREA-001..005 |
| Aceite | `acceptance-tests.md` | AT-AREA-001..005 |
| Engenharia | `docs/implementation/07-api-standards.md` | Padrões corporativos |

## Riscos

| Risco | Mitigação |
|-------|-----------|
| FT-SINGULAR não implementada | Entidade `SingularEntity` mínima read-only |
| Matriz de permissões incompleta | Autorização via e-mail administrador (incremental) |
| Equipe não implementada | `EquipeEntity` mínima para RN-AREA-008 |

## Pendências

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

Executar PKG-01 — Organization Scaffold & Persistence.
