# Phases and Activities — Integration Sprint

| Item | Valor |
|------|-------|
| Camada | `engineering/integration/` |
| Versão | 1.0 |
| Status | Stable |

---

# Visão Geral das Fases

```text
ENV (Ambiente)
    ↓
INF (Infraestrutura)
    ↓
API (APIs)
    ↓
DB (Banco)
    ↓
XFT (Cross-Feature)
    ↓
FUN (Funcional)
    ↓
OUT (Resultado)
```

---

# Fase ENV — Validação do Ambiente

**Objetivo:** Confirmar que a aplicação inicia e o ambiente está configurado corretamente.

| Atividade | Descrição | Entregável |
|-----------|-----------|------------|
| ENV-01 | Verificar build (`mvn clean verify` ou equivalente) | Log de build em `backend/runtime/` |
| ENV-02 | Confirmar startup da aplicação (profile local/test) | Log de startup sem erro fatal |
| ENV-03 | Validar datasource e conexão com banco | Health indicator / teste de conexão |
| ENV-04 | Verificar actuator (`/actuator/health`, métricas) | Resposta HTTP 200 com status UP |
| ENV-05 | Validar profiles ativos (`local`, `test`, `dev`) | `application-*.yaml` coerentes |
| ENV-06 | Verificar variáveis de ambiente obrigatórias | Lista documentada vs. valores efetivos |
| ENV-07 | Validar logs estruturados e correlation ID | Amostra de log com correlation ID |

---

# Fase INF — Validação da Infraestrutura

**Objetivo:** Confirmar que autenticação, segurança e políticas transversais funcionam.

| Atividade | Descrição | Entregável |
|-----------|-----------|------------|
| INF-01 | Validar SecurityFilterChain stateless | Teste de integração ou inspeção |
| INF-02 | Confirmar JWT (estrutura, expiração, claims) | Teste `JwtStructureValidator` ou equivalente |
| INF-03 | Validar cookies HttpOnly (access/refresh) | Inspeção de `Set-Cookie` no login |
| INF-04 | Verificar CORS (preflight e origens permitidas) | Teste preflight OPTIONS |
| INF-05 | Confirmar rotas públicas vs. protegidas (401/403) | Testes sem e com token |
| INF-06 | Validar CSRF em endpoints mutáveis | Teste POST sem token CSRF → 403 |
| INF-07 | Verificar GlobalExceptionHandler integrado | Respostas padronizadas `ApiResponse` |

---

# Fase API — Validação das APIs

**Objetivo:** Confirmar contratos REST por Feature no escopo.

| Atividade | Descrição | Entregável |
|-----------|-----------|------------|
| API-01 | CRUD completo por recurso (Create, Read, Update, Status) | Testes de aceitação por Feature |
| API-02 | Paginação (`page`, `size`, metadados) | Resposta com `Page`/`Pageable` |
| API-03 | Ordenação (`sort`) | Resultados ordenados conforme spec |
| API-04 | Filtros (query params documentados) | Filtros retornam subconjunto correto |
| API-05 | Status HTTP conforme contrato (201, 200, 404, 409, 422) | Matriz endpoint × status |
| API-06 | Mensagens de erro padronizadas | Corpo `ApiResponse` com código e mensagem |
| API-07 | OpenAPI documenta endpoints do escopo | `/v3/api-docs` inclui recursos |

**Recursos típicos (exemplo sprint org):**

| Feature | Base Path |
|---------|-----------|
| FT-AUTH | `/api/v1/auth` |
| FT-SINGULAR | `/api/v1/singulares` |
| FT-AREA | `/api/v1/areas` |
| FT-EQUIPE | `/api/v1/equipes` |
| FT-COLABORADOR | `/api/v1/colaboradores` |

---

# Fase DB — Validação do Banco

**Objetivo:** Confirmar integridade estrutural e padrões de persistência.

| Atividade | Descrição | Entregável |
|-----------|-----------|------------|
| DB-01 | Foreign keys entre entidades do escopo | Inspeção de migrations/DDL ou schema H2 |
| DB-02 | Constraints (UNIQUE, NOT NULL, CHECK) | Testes de violação retornam erro adequado |
| DB-03 | Índices em colunas de busca/filtro | Inspeção de schema |
| DB-04 | Auditoria (`createdAt`, `updatedAt`, `createdBy`) | Entidades estendem `AuditableEntity` |
| DB-05 | Soft delete / flag `ativo` | Inativação não remove registro fisicamente |
| DB-06 | Relacionamentos bidirecionais coerentes | Singular→Área→Equipe→Colaborador |
| DB-07 | Sequences e naming Oracle | Conformidade com padrão do projeto |

---

# Fase XFT — Validação entre Features

**Objetivo:** Executar fluxos end-to-end que atravessam múltiplas Features.

## Fluxo de referência — Organização Corporativa

```text
Login (FT-AUTH)
    ↓
Singular (FT-SINGULAR) — criar/consultar
    ↓
Área (FT-AREA) — vinculada à Singular
    ↓
Equipe (FT-EQUIPE) — vinculada à Área
    ↓
Colaborador (FT-COLABORADOR) — vinculado à Equipe/Singular/Área
```

| Atividade | Descrição | Entregável |
|-----------|-----------|------------|
| XFT-01 | Autenticar e obter sessão válida | Cookie `access_token` presente |
| XFT-02 | Criar hierarquia completa (Singular → Colaborador) | IDs encadeados corretamente |
| XFT-03 | Consultar entidades filhas a partir do pai | Filtros por `singularId`, `areaId`, etc. |
| XFT-04 | Inativar entidade pai com dependentes ativos | Regra de negócio (ex.: RN bloqueia inativação) |
| XFT-05 | Atualizar vínculos e validar integridade | FKs respeitadas após update |
| XFT-06 | Fluxo de listagem paginada cross-context | Dados consistentes entre BCs |

---

# Fase FUN — Validação Funcional

**Objetivo:** Confirmar regras de negócio e casos de borda.

| Atividade | Descrição | Entregável |
|-----------|-----------|------------|
| FUN-01 | Casos positivos (happy path) por Feature | Acceptance tests PASS |
| FUN-02 | Casos negativos (validação, 422) | Requests inválidos rejeitados |
| FUN-03 | Unicidade (código, CPF, e-mail quando aplicável) | 409 Conflict |
| FUN-04 | Regras de hierarquia e dependência | RN-* conforme specification |
| FUN-05 | Autorização incremental (se aplicável) | Comportamento documentado (ex.: OQ-020) |
| FUN-06 | Rastreabilidade spec → teste | Matriz AT-* referenciada |

---

# Fase OUT — Resultado

**Objetivo:** Consolidar findings e emitir decisão.

| Atividade | Descrição | Entregável |
|-----------|-----------|------------|
| OUT-01 | Consolidar issues abertas | `issues.md` atualizado |
| OUT-02 | Registrar riscos residuais | Seção em `integration-report.md` |
| OUT-03 | Listar pendências e deferimentos | Itens `WAIVED` e `DEFERRED` |
| OUT-04 | Calcular métricas finais | `integration-state.yaml` |
| OUT-05 | Emitir decisão de readiness | `integration-readiness.md` |
| OUT-06 | Aprovação formal | `phase: approved` ou `rejected` |

---

# Entregáveis por Fase

| Fase | Artefato principal |
|------|-------------------|
| ENV–FUN | Itens do checklist com evidência |
| OUT | `integration-report.md`, `integration-readiness.md`, `issues.md` |
