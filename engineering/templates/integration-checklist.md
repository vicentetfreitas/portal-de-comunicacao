# Integration Checklist — Template

| Item | Valor |
|------|-------|
| Sprint ID | {{SPRINT_ID}} |
| Status | PLANNED |
| Versão | 1.0 |
| Última atualização | {{DATE}} |

> Cada item possui: **ID**, **Descrição**, **Critério de aprovação**, **Evidência**, **Status**.
> Status: `PENDING` | `APPROVED` | `BLOCKED` | `WAIVED`
> Prioridade: **Must** bloqueia readiness | **Should** gera issue se pendente

---

## Fase ENV — Ambiente

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-ENV-001 | Build completo | `mvn clean verify` SUCCESS | | PENDING | | |
| INT-ENV-002 | Startup da aplicação | Aplicação inicia sem erro fatal | | PENDING | | |
| INT-ENV-003 | Datasource | Conexão com banco estabelecida | | PENDING | | |
| INT-ENV-004 | Actuator health | `/actuator/health` retorna UP | | PENDING | | |
| INT-ENV-005 | Profiles | Profiles `test`/`local` configurados | | PENDING | | |
| INT-ENV-006 | Variáveis de ambiente | Variáveis obrigatórias documentadas e presentes | | PENDING | | |
| INT-ENV-007 | Logs estruturados | Correlation ID presente nos logs | | PENDING | | |

---

## Fase INF — Infraestrutura

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-INF-001 | SecurityFilterChain | Stateless, filtros registrados | | PENDING | | |
| INT-INF-002 | JWT | Estrutura e claims válidos | | PENDING | | |
| INT-INF-003 | Cookies | HttpOnly, Secure, SameSite nos cookies de auth | | PENDING | | |
| INT-INF-004 | CORS | Preflight e origens permitidas | | PENDING | | |
| INT-INF-005 | Rotas protegidas | Sem token → 401; com token → 200 | | PENDING | | |
| INT-INF-006 | CSRF | POST mutável sem CSRF → 403 | | PENDING | | |
| INT-INF-007 | Exception handler | Erros retornam `ApiResponse` padronizado | | PENDING | | |

---

## Fase API — APIs

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-API-001 | CRUD — recurso 1 | Create, Read, Update, Status conforme spec | | PENDING | | |
| INT-API-002 | Paginação | `page`/`size` retornam metadados corretos | | PENDING | | |
| INT-API-003 | Ordenação | `sort` ordena conforme contrato | | PENDING | | |
| INT-API-004 | Filtros | Query params filtram corretamente | | PENDING | | |
| INT-API-005 | Status HTTP | Códigos conforme `api.md` | | PENDING | | |
| INT-API-006 | Mensagens de erro | Corpo padronizado com código e mensagem | | PENDING | | |
| INT-API-007 | OpenAPI | Endpoints do escopo documentados | | PENDING | | |

---

## Fase DB — Banco

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-DB-001 | Foreign keys | FKs entre entidades do escopo | | PENDING | | |
| INT-DB-002 | Constraints | UNIQUE/NOT NULL respeitados | | PENDING | | |
| INT-DB-003 | Índices | Colunas de busca indexadas | | PENDING | | |
| INT-DB-004 | Auditoria | `AuditableEntity` com timestamps | | PENDING | | |
| INT-DB-005 | Soft delete | Inativação preserva registro | | PENDING | | |
| INT-DB-006 | Relacionamentos | Hierarquia pai-filho coerente | | PENDING | | |

---

## Fase XFT — Cross-Feature

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-XFT-001 | Autenticação | Login retorna sessão válida | | PENDING | | |
| INT-XFT-002 | Fluxo principal | Cadeia documentada no plano executada | | PENDING | | |
| INT-XFT-003 | Consultas encadeadas | Filtros por FK funcionam | | PENDING | | |
| INT-XFT-004 | Regras de dependência | Inativação com dependentes bloqueada | | PENDING | | |

---

## Fase FUN — Funcional

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-FUN-001 | Casos positivos | Acceptance tests PASS por Feature | | PENDING | | |
| INT-FUN-002 | Casos negativos | Validações retornam 422 | | PENDING | | |
| INT-FUN-003 | Unicidade | Duplicatas retornam 409 | | PENDING | | |
| INT-FUN-004 | Regras de negócio | RN-* conforme specification | | PENDING | | |

---

## Fase OUT — Resultado

| ID | Descrição | Critério de aprovação | Evidência | Status | Executor | Data |
|----|-----------|----------------------|-----------|--------|----------|------|
| INT-OUT-001 | Issues consolidadas | `issues.md` atualizado | | PENDING | | |
| INT-OUT-002 | Riscos documentados | Seção em `integration-report.md` | | PENDING | | |
| INT-OUT-003 | Métricas finais | `integration-state.yaml` atualizado | | PENDING | | |
| INT-OUT-004 | Readiness executada | `integration-readiness.md` preenchido | | PENDING | | |

---

# Resumo

| Métrica | Valor |
|---------|-------|
| Total Must | — |
| Aprovados | — |
| Pendentes | — |
| Bloqueados | — |
| Waived | — |
