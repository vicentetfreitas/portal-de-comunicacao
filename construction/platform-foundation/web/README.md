# Web Module

| Item | Valor |
|------|-------|
| Módulo | Web |
| Prefixo | PF-WEB |
| Pacote | `interfaces/rest/` |
| Pacote Construction | PKG-05 |
| Status | Não iniciado |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Estabelecer a camada de exposição REST padronizada, fornecendo estrutura de controllers, health endpoint e documentação OpenAPI para todas as Features.

---

# Escopo

## Inclui

- Estrutura `interfaces/rest/` (controller, request, response, mapper, config)
- `HealthController` — `GET /api/v1/health`
- `OpenApiConfiguration` — documentação OpenAPI 3
- Integração com `GlobalExceptionHandler` (Sprint 0)
- Padrão de resposta `ApiResponse<T>` (Sprint 0)
- Versionamento `/api/v1`

## Não inclui

- Controllers de negócio
- Endpoints FT-AUTH (`/api/v1/auth/*`)
- MapStruct (decisão CD-S1A-003)
- Regras de negócio em controllers

---

# Responsabilidades

| Componente | Responsabilidade |
|------------|------------------|
| HealthController | Endpoint de saúde da aplicação |
| HealthResponse | DTO de resposta do health |
| OpenApiConfiguration | Swagger UI e documentação API |
| Estrutura REST | Convenções para Features futuras |

---

# Limites

- Controllers apenas delegam — sem lógica de negócio
- Sem acesso direto a repositories
- Sem endpoints além de health na Sprint 1A

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| PF-SEC | Security | Pendente |
| PF-CONF | Configuration | Pendente |
| Sprint 0 ApiResponse, GlobalExceptionHandler | `shared/` | Concluído |
| `docs/implementation/07-api-standards.md` | Padrões API | Consultivo |
| `docs/construction/backend/04-api-implementation.md` | Implementação API | Consultivo |

---

# Componentes Esperados

```text
interfaces/rest/
├── controller/
│   └── HealthController.java
├── response/
│   └── HealthResponse.java
└── config/
    └── OpenApiConfiguration.java
```

---

# Contrato Health Endpoint

```http
GET /api/v1/health
```

**Response 200:**

```json
{
  "success": true,
  "data": {
    "status": "UP",
    "application": "portal-comunicacao",
    "version": "0.0.1-SNAPSHOT"
  }
}
```

Formato conforme `ApiResponse<HealthResponse>`.

---

# Ordem de Construção

```text
PF-WEB-001 (Estrutura interfaces/rest/)
    → PF-WEB-002 (HealthController + HealthResponse)
    → PF-WEB-003 (Integração SecurityFilterChain — whitelist)
    → PF-WEB-004 (OpenApiConfiguration)
    → PF-WEB-005 (Testes WebMvcTest + integração)
```

---

# Critérios de Aceite

1. `GET /api/v1/health` retorna 200 com ApiResponse
2. Health endpoint na whitelist de segurança
3. OpenAPI documenta health endpoint
4. Swagger UI acessível (conforme CD-S1A-002)
5. Erros tratados pelo GlobalExceptionHandler existente
6. Testes WebMvcTest e integração aprovados

---

# Definition of Done do Módulo

- [ ] Todas as tarefas PF-WEB-* concluídas
- [ ] Testes aprovados
- [ ] `review.md` validado
- [ ] Build SUCCESS
- [ ] FT-AUTH pode adicionar controllers em `interfaces/rest/controller/`

---

# Rastreabilidade

- `docs/construction/backend/04-api-implementation.md`
- `docs/implementation/07-api-standards.md`
- `construction/03-construction-packages.md` § PKG-05
