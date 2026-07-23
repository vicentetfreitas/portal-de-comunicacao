# Execution Plan — FT-AREA (Área)

| Item | Valor |
|------|-------|
| Feature Code | **FT-AREA** |
| Feature Slug | area |
| Sprint | 2 |
| Status | **Encerrada — APPROVED** |
| SSOD | `construction/features/FT-AREA/feature-manifest.yaml` |
| Construction State | `construction/features/FT-AREA/construction-state.yaml` |
| Versão | 1.0 |
| Última atualização | 2026-07-13 |

---

# Objetivo

Implementar CRUD de Áreas organizacionais no Portal de Comunicação — cadastro, consulta, listagem paginada, atualização e ativação/inativação lógica via `/api/v1/areas`.

Consultar `feature-manifest.yaml` (SSOD) antes de qualquer outro artefato.

---

# Escopo

## Inclui

- Bounded context `organization` no backend
- Endpoints `/api/v1/areas` (POST, GET, GET/{id}, PUT/{id}, PATCH/{id}/status)
- Persistência na tabela `AREA` (schema `UNMPORTCOM`)
- Entidades mínimas `Singular` e `Equipe` para validações referenciais
- Validações de negócio RN-AREA-001 a RN-AREA-009
- Testes de aceite conforme `specs/features/area/acceptance-tests.md`

## Não inclui

- Frontend da Feature
- FT-SINGULAR completa (apenas entidade mínima para validação)
- FT-EQUIPE completa (apenas consulta de equipes ativas)
- Matriz completa de permissões (OQ-020)
- Exclusão física de registros

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| Platform Foundation | `construction/platform-foundation/construction-state.yaml` | ✅ `phase: closed` |
| FT-AUTH | `construction/features/FT-AUTH/construction-state.yaml` | ✅ `phase: closed` |
| Especificação FT-AREA | `specs/features/area/` | ✅ Approved |
| DDL AREA (DBA) | `database/ddl/` | ✅ Disponível |

---

# Sequência de PKGs

| PKG | Nome | Escopo resumido | Tarefas principais |
|-----|------|-----------------|-------------------|
| PKG-01 | Organization Scaffold & Persistence | BC `organization`, entities, repositories, DTOs, mapper | TK-AREA-001 (base) |
| PKG-02 | Create Area | Domain service, POST endpoint, validações RN-001..006 | TK-AREA-001, AT-AREA-001 |
| PKG-03 | Read & List | GET/{id}, GET listagem paginada com filtros | TK-AREA-002, TK-AREA-003 |
| PKG-04 | Update Area | PUT/{id}, imutabilidade singular | TK-AREA-004, AT-AREA-004 |
| PKG-05 | Status Change | PATCH/{id}/status, bloqueio inativação | TK-AREA-005, AT-AREA-005 |
| PKG-06 | Acceptance & Closure | Suíte AT-AREA-*, validação incremental | Todos ATs |

Ordem obrigatória: PKG-01 → PKG-02 → … → PKG-06.

---

# Critérios de entrada (Definition of Ready)

1. Platform Foundation com `phase: closed`
2. FT-AUTH encerrada
3. Especificação FT-AREA Approved
4. `Execute Feature FT-AREA` executado (Session congelada)

---

# Critérios de saída (Definition of Done)

1. Todos os PKGs (01–06) com `status.md` em **DONE**
2. `construction-state.yaml` com `phase: closed`
3. Review, Audit e Readiness aprovados
4. `mvn clean verify` — SUCCESS no encerramento
5. Critérios AT-AREA-001 a AT-AREA-005 validados

---

# Status

| Métrica | Valor |
|---------|-------|
| Fase | `closed` |
| PKG ativo | — |
| PKGs concluídos | 6 / 6 |
| Estado final | **FEATURE_APPROVED** |
| Próxima ação | — |
