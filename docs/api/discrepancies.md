# Divergências — Documentação vs Implementação

| Sprint | API-DOCS-01 |
|--------|-------------|
| Data | 2026-07-16 |
| Política | Registrar sem corrigir código ou specs |

---

## DISC-001 — Nomenclatura JSON FT-SINGULAR

| Aspecto | Spec (`specs/features/singular/api.md`) | Implementação |
|---------|----------------------------------------|---------------|
| Federação | `federacaoId` | `federationId` |
| Código Unimed | `codigoUnimed` | `unimedCode` |
| Filtro listagem | `federacaoId`, `codigoUnimed` | `federationId`, `unimedCode` |

**Impacto:** clientes baseados na spec devem usar nomes em inglês conforme DTOs Java.

---

## DISC-002 — Permissões em `/auth/me`

| Aspecto | Spec FT-AUTH | Implementação |
|---------|--------------|---------------|
| `permissions` | Array com permissões do Portal (ex.: `DOCUMENT_READ`) | **Sempre `[]`** |

**Evidência:** `AuthenticatedUserResponse` retorna lista vazia; sem RBAC por permissão implementado.

---

## DISC-003 — CSRF em PATCH

| Aspecto | Spec FT-AUTH (seção CSRF) | Implementação |
|---------|---------------------------|---------------|
| Métodos protegidos | `POST`, `PUT`, `DELETE` | `POST`, `PUT`, `PATCH`, `DELETE` |

**Evidência:** `AreaAcceptanceIntegrationTest` e demais testes de aceite enviam `X-XSRF-TOKEN` em `PATCH`.

---

## DISC-004 — Código de erro Zimbra indisponível

| Aspecto | Spec FT-AUTH | Implementação |
|---------|--------------|---------------|
| Código `error` | `SERVICE_UNAVAILABLE` | `INTEGRATION_UNAVAILABLE` |

---

## DISC-005 — Modelo de autorização

| Aspecto | Specs CRUD (ex.: FT-AREA) | Implementação |
|---------|--------------------------|---------------|
| Autorização escrita | "Administrador no escopo da singular/área" | Lista `application.auth.session-administrator-emails` (e-mail do JWT) |
| RBAC / roles | Implícito em specs e docs de frontend | **Não implementado** — sem `@PreAuthorize` |

---

## DISC-006 — Autenticação Bearer

| Aspecto | Padrões/docs diversos | Implementação |
|---------|----------------------|---------------|
| Header `Authorization: Bearer` | Mencionado em alguns guias | **Não suportado** — apenas cookie `access_token` |

**Evidência:** `JwtAuthenticationFilter` lê somente cookie.

---

## DISC-007 — OpenAPI sem security scheme

| Aspecto | Esperado para consumo | Implementação |
|---------|----------------------|---------------|
| Swagger UI auth | Scheme JWT/cookie | `OpenApiConfiguration` **sem** `SecurityScheme` |

Endpoints protegidos não aparecem com requisito de auth no Swagger gerado.

---

## DISC-008 — Recursos documentados sem implementação

| Recurso | Documentação consultiva | Backend `/api/v1` |
|---------|------------------------|-------------------|
| Documentos | `07-api-standards.md`, `04-api-implementation.md` | **Ausente** |
| Usuários | `discovery/04-current-endpoints.md` (legado) | **Ausente** (usar `colaboradores`) |
| Comunicados | `construction/backend/04-api-implementation.md` | **Ausente** |
| Notificações | `construction/backend/04-api-implementation.md` | **Ausente** |

---

## DISC-009 — Filtro colaborador `teamId` vs recurso `equipes`

| Aspecto | Nomenclatura REST | Implementação |
|---------|-------------------|---------------|
| Recurso equipe | Path `/api/v1/equipes` | `EquipeController` |
| FK no colaborador | — | Query param e campo `teamId` (não `equipeId`) |

Consistente no código; spec colaborador pode usar terminologia diferente.

---

## DISC-010 — Sort interno `nome` vs API `name`

| Aspecto | API exposta | JPA/Pageable |
|---------|-------------|--------------|
| Sort padrão | `sort=name` aceito | `@PageableDefault(sort = "nome")` mapeamento interno |

Comportamento funcional validado nos testes; documentação externa deve aceitar ambos conforme Spring Data.

---

## DISC-011 — Homologação Postman vs testes Java (auth)

| Aspecto | Testes Java | Postman |
|---------|-------------|---------|
| Login Zimbra | MockMvc + mock Zimbra | Requer Zimbra real ou cookies importados |
| Cenários positivos auth | ✅ 14 casos automatizados | ⚠️ Manual ou setup prévio |

**Identificado em:** Sprint API-VALIDATION-01

---

## DISC-012 — Newman não integrado ao CI

| Aspecto | Estado atual | Esperado futuro |
|---------|--------------|-----------------|
| Execução Postman | Manual / local via Newman | Pipeline CI com `newman run` |
| Artefato | Scripts prontos em `Portal.postman_collection.json` | `newman-results.json` em CI |

**Identificado em:** Sprint API-VALIDATION-01

---

## Resumo

| ID | Severidade | Categoria |
|----|------------|-----------|
| DISC-001 | Média | Contrato spec vs DTO |
| DISC-002 | Alta | Funcionalidade incompleta |
| DISC-003 | Baixa | Documentação auth |
| DISC-004 | Baixa | Código erro |
| DISC-005 | Alta | Autorização |
| DISC-006 | Média | Autenticação |
| DISC-007 | Baixa | OpenAPI |
| DISC-008 | Alta | Escopo não implementado |
| DISC-009 | Baixa | Nomenclatura |
| DISC-010 | Baixa | Paginação |
| DISC-011 | Média | Homologação Postman |
| DISC-012 | Baixa | CI/CD |
