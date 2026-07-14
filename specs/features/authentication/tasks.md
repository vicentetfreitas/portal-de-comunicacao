# Tarefas Técnicas — Authentication

| Item | Valor |
|------|-------|
| Feature ID | **FT-AUTH** |
| Projeto | Portal de Comunicação |
| Camada | Features |
| Status | **Approved** |
| Versão | 2.0 |
| Última atualização | 2026-07-08 |

---

## Objetivo

Decompor a implementação da Feature Authentication (Stateless, JWT, Zimbra) em tarefas técnicas rastreáveis.

Arquitetura: `specs/architecture/authentication-architecture.md`

---

# Backend

| ID | Tarefa | Origem | Prioridade | Dep. | Est. | Conclusão | Responsável |
|----|--------|--------|------------|------|------|-----------|-------------|
| TASK-AUTH-BE-001 | Configurar SecurityFilterChain stateless (sem HTTP Session) | DA-AUTH-005 | Alta | — | M | Filtro JWT em cookies configurado | Backend |
| TASK-AUTH-BE-002 | Configurar proteção CSRF para fluxos autenticados por Cookie | RNF-AUTH-005 | Alta | BE-001 | M | CSRF ativo em POST/PUT/DELETE | Backend |
| TASK-AUTH-BE-003 | Implementar `GET /api/v1/auth/login` com redirect ao Zimbra | AUTH-API-001 | Alta | BE-002 | P | Redirect com state anti-CSRF | Backend |
| TASK-AUTH-BE-004 | Implementar `GET /api/v1/auth/callback` | AUTH-API-002 | Alta | BE-003, INT-001 | G | Callback emite cookies e redireciona | Backend |
| TASK-AUTH-BE-005 | Implementar `GET /api/v1/auth/me` com `ApiResponse` | AUTH-API-003 | Alta | BE-001 | P | Identidade + permissões do banco | Backend |
| TASK-AUTH-BE-006 | Implementar `POST /api/v1/auth/logout` | AUTH-API-004 | Alta | BE-001 | P | Revoga refresh + remove cookies | Backend |
| TASK-AUTH-BE-007 | Implementar validação de identidade Zimbra no callback | RN-AUTH-004 | Alta | INT-004 | M | Identidade inválida rejeitada | Backend |
| TASK-AUTH-BE-008 | Implementar controle de acesso a recursos protegidos via JWT | RF-AUTH-007 | Alta | BE-001 | M | Rotas protegidas exigem JWT válido | Backend |
| TASK-AUTH-BE-009 | Implementar tratamento centralizado de falhas de autenticação | RF-AUTH-011 | Alta | BE-004 | M | Cenários negativos via ErrorResponse | Backend |
| TASK-AUTH-BE-010 | Implementar tratamento de colaborador sem autorização (403) | RF-AUTH-007 | Alta | BE-004 | M | ErrorResponse FORBIDDEN | Backend |
| TASK-AUTH-BE-011 | Implementar auditoria de eventos de autenticação | RNF-AUTH-007 | Média | BE-004 | M | Login, logout, falha, renovação auditados | Backend |
| TASK-AUTH-BE-012 | Registrar eventos para observabilidade (sem dados sensíveis) | RNF-AUTH-008 | Média | BE-011 | P | Logs estruturados de auth | Backend |
| TASK-AUTH-BE-013 | Implementar serviço de emissão de Access Token (JWT) | DA-AUTH-006 | Alta | BE-001 | M | JWT com claims e TTL 15 min | Backend |
| TASK-AUTH-BE-014 | Implementar serviço de emissão de Refresh Token | DA-AUTH-007 | Alta | BE-013 | M | UUID opaco com TTL 8h/30d | Backend |
| TASK-AUTH-BE-015 | Implementar configuração de Cookies HttpOnly + Secure | DA-AUTH-007 | Alta | BE-013, BE-014 | P | Cookies com flags corretas | Backend |
| TASK-AUTH-BE-016 | Implementar `POST /api/v1/auth/refresh` | AUTH-API-005 | Alta | BE-014, BE-015 | M | Renovação de Access Token | Backend |
| TASK-AUTH-BE-017 | Implementar revogação de Refresh Token no logout | RN-AUTH-008 | Alta | BE-006, BE-014 | P | FLG_REVOGADA no banco | Backend |
| TASK-AUTH-BE-018 | Implementar localização/criação automática de colaborador | RF-AUTH-008 | Alta | BE-004 | M | Colaborador criado se inexistente | Backend |
| TASK-AUTH-BE-019 | Implementar controle de sessões simultâneas (máx. 3) | DA-AUTH-010 | Alta | BE-014 | M | Sessão mais antiga revogada | Backend |
| TASK-AUTH-BE-020 | Implementar revogação administrativa de sessões | RF-AUTH-010 | Média | BE-014 | M | `DELETE /api/v1/admin/sessions/{sessionId}` implementado | Backend |

---

# Integração

| ID | Tarefa | Origem | Prioridade | Dep. | Est. | Conclusão | Responsável |
|----|--------|--------|------------|------|------|-----------|-------------|
| TASK-AUTH-INT-001 | Implementar integração com Zimbra (contrato abstrato) | DA-AUTH-008 | Alta | INF-001 | G | Fluxo login/callback funcional | Backend |
| TASK-AUTH-INT-002 | Configurar cliente Zimbra por ambiente | RNF-AUTH-006 | Alta | INT-001 | M | URLs e timeout configuráveis | Backend |
| TASK-AUTH-INT-003 | Implementar tratamento de indisponibilidade Zimbra | RF-AUTH-011 | Alta | INT-001 | M | 503 + ErrorResponse | Backend |
| TASK-AUTH-INT-004 | Validar identidade retornada pelo Zimbra | RN-AUTH-004 | Alta | INT-001 | M | Dados mínimos validados | Backend |
| TASK-AUTH-INT-005 | Implementar propagação de logout ao Zimbra quando aplicável | RN-AUTH-008 | Baixa | BE-006 | P | Logout Zimbra se suportado | Backend |

---

# Frontend

| ID | Tarefa | Origem | Prioridade | Dep. | Est. | Conclusão | Responsável |
|----|--------|--------|------------|------|------|-----------|-------------|
| TASK-AUTH-FE-001 | Implementar fluxo de login (redirect `/api/v1/auth/login`) | RF-AUTH-001 | Alta | BE-003 | M | Login redireciona ao Zimbra | Frontend |
| TASK-AUTH-FE-002 | Implementar fluxo de logout | RF-AUTH-006 | Alta | BE-006 | P | Logout com CSRF | Frontend |
| TASK-AUTH-FE-003 | Implementar consumo de `/api/v1/auth/me` | RF-AUTH-005 | Alta | BE-005 | P | ApiResponse consumido | Frontend |
| TASK-AUTH-FE-004 | Implementar proteção de rotas (Vue Router Guards) | RF-AUTH-007 | Alta | FE-003 | M | Rotas protegidas | Frontend |
| TASK-AUTH-FE-005 | Implementar gerenciamento de estado de autenticação | RF-AUTH-002 | Alta | FE-003 | M | Estado derivado de /me | Frontend |
| TASK-AUTH-FE-006 | Implementar interceptador HTTP com renovação automática | RF-AUTH-004 | Alta | BE-016 | M | 401 → refresh → retry | Frontend |
| TASK-AUTH-FE-007 | Implementar tratamento de sessão expirada (refresh expirado) | RF-AUTH-004 | Alta | FE-006 | M | Redirect ao login | Frontend |
| TASK-AUTH-FE-008 | Implementar tratamento visual de falhas de autenticação | RF-AUTH-011 | Média | FE-001 | P | Mensagens de erro | Frontend |
| TASK-AUTH-FE-009 | Implementar tratamento visual de acesso negado (403) | RF-AUTH-007 | Média | FE-004 | P | Mensagem FORBIDDEN | Frontend |
| TASK-AUTH-FE-010 | Implementar envio de token CSRF em requisições mutáveis | RNF-AUTH-005 | Alta | BE-002 | P | Header X-XSRF-TOKEN | Frontend |
| TASK-AUTH-FE-011 | Implementar opção "Lembrar-me" no login | RN-AUTH-010 | Média | BE-003 | P | Query param remember_me | Frontend |

---

# Infraestrutura

| ID | Tarefa | Origem | Prioridade | Dep. | Est. | Conclusão | Responsável |
|----|--------|--------|------------|------|------|-----------|-------------|
| TASK-AUTH-INF-001 | Configurar variáveis Zimbra por ambiente | DA-AUTH-008 | Alta | — | P | ZIMBRA_AUTH_URL, VALIDATE_URL, TIMEOUT | Infra |
| TASK-AUTH-INF-002 | Configurar conectividade segura com Zimbra | RNF-AUTH-001 | Alta | INF-001 | M | HTTPS validado | Infra |
| TASK-AUTH-INF-003 | Configurar certificados TLS | RNF-AUTH-001 | Alta | INF-002 | M | TLS operacional | Infra |
| TASK-AUTH-INF-004 | Configurar ambientes dev/hml/prod | FT-AUTH | Alta | INF-001 | G | Três ambientes | Infra |
| TASK-AUTH-INF-005 | Configurar logs de autenticação (sem dados sensíveis) | RNF-AUTH-008 | Média | INF-004 | P | Logs sanitizados | Infra |
| TASK-AUTH-INF-006 | Configurar monitoramento integração Zimbra | RNF-AUTH-006 | Média | INT-001 | M | Alertas indisponibilidade | Infra |
| TASK-AUTH-INF-007 | Configurar chave de assinatura JWT por ambiente | DA-AUTH-006 | Alta | INF-004 | P | JWT_SECRET segregado | Infra |

---

# Banco de Dados

| ID | Tarefa | Origem | Prioridade | Dep. | Est. | Conclusão | Responsável |
|----|--------|--------|------------|------|------|-----------|-------------|
| TASK-AUTH-DB-001 | Criar migration tabela AUTH_SESSAO | DA-AUTH-007 | Alta | — | M | Tabela conforme arquitetura | Backend |
| TASK-AUTH-DB-002 | Criar repositório de sessões | DA-AUTH-007 | Alta | DB-001 | P | CRUD sessões + revogação | Backend |

---

# Segurança

| ID | Tarefa | Origem | Prioridade | Dep. | Est. | Conclusão | Responsável |
|----|--------|--------|------------|------|------|-----------|-------------|
| TASK-AUTH-SEC-001 | Validar HTTPS obrigatório | RNF-AUTH-001 | Alta | INF-003 | P | Auditoria transporte | Segurança |
| TASK-AUTH-SEC-002 | Garantir ausência de tokens em LocalStorage/SessionStorage | RN-AUTH-007 | Alta | BE-015 | P | Verificação frontend | Segurança |
| TASK-AUTH-SEC-003 | Validar flags HttpOnly + Secure nos cookies | DA-AUTH-007 | Alta | BE-015 | P | Teste de cookies | Segurança |
| TASK-AUTH-SEC-004 | Validar proteção CSRF | RNF-AUTH-005 | Alta | BE-002 | P | Teste CSRF | Segurança |
| TASK-AUTH-SEC-005 | Validar encerramento seguro de sessão | RN-AUTH-008 | Alta | BE-017 | P | Sessão inválida pós-logout | Segurança |
| TASK-AUTH-SEC-006 | Validar ausência de dados sensíveis em logs | RNF-AUTH-008 | Alta | BE-011 | P | Revisão de logs | Segurança |

---

# Testes

| ID | Tarefa | Origem | Prioridade | Dep. | Est. | Conclusão | Responsável |
|----|--------|--------|------------|------|------|-----------|-------------|
| TASK-AUTH-QA-001 | Testes unitários — emissão/validação JWT | DA-AUTH-006 | Alta | BE-013 | M | Cobertura JWT service | QA |
| TASK-AUTH-QA-002 | Testes unitários — Refresh Token e revogação | DA-AUTH-007 | Alta | BE-014 | M | Cobertura refresh service | QA |
| TASK-AUTH-QA-003 | Testes de integração — fluxo login/callback | AC-AUTH-001 | Alta | INT-001 | G | Fluxo completo | QA |
| TASK-AUTH-QA-004 | Testes de integração — refresh e expiração | AC-AUTH-008, 009 | Alta | BE-016 | M | Renovação e expiração | QA |
| TASK-AUTH-QA-005 | Testes de segurança — CSRF, cookies, HTTPS | SEC-001 a 006 | Alta | SEC-004 | M | Cenários segurança | QA |
| TASK-AUTH-QA-006 | Executar todos os cenários AC-AUTH-001 a 014 | FT-AUTH | Alta | FE-011 | G | Todos os AC aprovados | QA |

---

# Documentação

| ID | Tarefa | Origem | Prioridade | Dep. | Est. | Conclusão | Responsável |
|----|--------|--------|------------|------|------|-----------|-------------|
| TASK-AUTH-DOC-001 | Atualizar documentação técnica pós-implementação | DoD | Baixa | QA-006 | P | Docs alinhadas | Documentação |
| TASK-AUTH-DOC-002 | Validar rastreabilidade RF→TASK | FT-AUTH | Média | QA-006 | P | Matriz completa | Documentação |

---

# Critérios de Conclusão

- Todos os endpoints `/api/v1/auth/*` implementados
- JWT e Refresh Token funcionais
- Cookies HttpOnly + Secure
- CSRF ativo
- Zimbra integrado (login apenas)
- 14 cenários de aceitação aprovados
- Definition of Done satisfeita

---

# Referências

- `specification.md`
- `api.md`
- `acceptance-tests.md`
- `specs/architecture/authentication-architecture.md`
- `../../foundation/definition-of-done.md`
