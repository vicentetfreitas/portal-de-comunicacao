# Execution Plan — FT-SINGULAR (Singular)

| Item | Valor |
|------|-------|
| Feature Code | **FT-SINGULAR** |
| Feature Slug | singular |
| Sprint | 3 |
| Status | **Encerrada** |
| SSOD | `construction/features/FT-SINGULAR/feature-manifest.yaml` |
| Construction State | `construction/features/FT-SINGULAR/construction-state.yaml` |
| Versão | 1.0 |
| Última atualização | 2026-07-14 |

---

# Estado Final

Feature encerrada em 2026-07-14 com **FEATURE_APPROVED**. Ver `closure-report.md` e `review/`.

---

# Objetivo

Implementar CRUD de Singulares organizacionais no Portal de Comunicação — cadastro, consulta, listagem paginada, atualização e ativação/inativação lógica via `/api/v1/singulares`.

Consultar `feature-manifest.yaml` (SSOD) antes de qualquer outro artefato.

---

# Escopo

## Inclui

- Evolução do bounded context `organization` no backend
- Endpoints `/api/v1/singulares` (POST, GET, GET/{id}, PUT/{id}, PATCH/{id}/status)
- Persistência na tabela `SINGULAR` (schema `UNMPORTCOM`)
- Validação referencial de Federação (seed ou repositório mínimo)
- Integração com FT-AREA para RN-SINGULAR-006 (áreas ativas vinculadas)
- Validações de negócio RN-SINGULAR-001 a RN-SINGULAR-007
- Testes de aceite conforme `specs/features/singular/acceptance-tests.md`

## Não inclui

- Frontend da Feature
- FT-FEDERACAO CRUD completo
- Gestão de endereços e contatos institucionais
- Matriz completa de permissões (OQ-020)
- Exclusão física de registros

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| Platform Foundation | `construction/platform-foundation/construction-state.yaml` | ✅ `phase: closed` |
| FT-AUTH | `construction/features/FT-AUTH/construction-state.yaml` | ✅ `phase: closed` |
| FT-AREA | `construction/features/FT-AREA/construction-state.yaml` | ✅ `phase: closed` |
| Especificação FT-SINGULAR | `specs/features/singular/` | ✅ Approved v1.1.1 |
| DDL SINGULAR (DBA) | `docs/database/ddl/` | ✅ Disponível |
| Seed FEDERACAO | `docs/database/ddl/008-initial-data.sql` | ✅ Disponível |

---

# Sequência de PKGs

| PKG | Nome | Escopo resumido | Tarefas principais |
|-----|------|-----------------|-------------------|
| PKG-01 | Singular Scaffold & Persistence | Evoluir `SingularEntity`, `SingularRepository`, `FederacaoRepository` mínimo, DTOs, mapper, status enum | TK-SINGULAR-001 (base) |
| PKG-02 | Create Singular | Domain service, POST endpoint, validações RN-001..004 | TK-SINGULAR-001, AT-SINGULAR-001 |
| PKG-03 | Read & List | GET/{id}, GET listagem paginada com filtros | TK-SINGULAR-002, TK-SINGULAR-003 |
| PKG-04 | Update Singular | PUT/{id}, imutabilidade federação, RN-001..004, 007 | TK-SINGULAR-004, AT-SINGULAR-004 |
| PKG-05 | Status Change | PATCH/{id}/status, RN-005..006, integração AreaRepository | TK-SINGULAR-005, AT-SINGULAR-005 |
| PKG-06 | Acceptance & Closure | Suíte AT-SINGULAR-*, validação incremental | Todos ATs |

Ordem obrigatória: PKG-01 → PKG-02 → … → PKG-06.

---

# Critérios de entrada (Definition of Ready)

1. Platform Foundation com `phase: closed`
2. FT-AUTH e FT-AREA encerradas
3. Especificação FT-SINGULAR Approved (v1.1.1)
4. `Execute Feature FT-SINGULAR` executado (Session congelada)

---

# Critérios de saída (Definition of Done)

1. Todos os PKGs (01–06) com `status.md` em **DONE**
2. `construction-state.yaml` com `phase: closed`
3. Review, Audit e Readiness aprovados
4. `mvn clean verify` — SUCCESS no encerramento
5. Critérios AT-SINGULAR-001 a AT-SINGULAR-005 validados

---

# Status

| Métrica | Valor |
|---------|-------|
| Fase | `execution` |
| PKG ativo | PKG-01 |
| PKGs concluídos | 0 / 6 |
| Próxima ação | `PKG-01 FT-SINGULAR` |
