# Readiness Review — Platform Foundation

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Sprint | 1A |
| Status | Checklist definido |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Fornecer checklist objetivo para validar que a Platform Foundation está completa, reutilizável e pronta para suportar a implementação de FT-AUTH.

---

# Escopo

Validação de prontidão ao final da Sprint 1A, antes da transição para Sprint 1 (FT-AUTH).

---

# Checklist — Platform Foundation Completa

## Módulo Configuration

| # | Critério | Evidência Esperada | Status |
|---|----------|-------------------|--------|
| RC-01 | SecurityProperties carregada e validada | Teste unitário `SecurityPropertiesTest` | ⬜ |
| RC-02 | PersistenceProperties carregada e validada | Teste unitário `PersistencePropertiesTest` | ⬜ |
| RC-03 | IntegrationProperties carregada e validada | Teste unitário `IntegrationPropertiesTest` | ⬜ |
| RC-04 | ZimbraProperties estrutura definida | Classe + teste de binding | ⬜ |
| RC-05 | Properties funcionam em perfis local/dev/hml | Testes por perfil | ⬜ |

## Módulo Persistence

| # | Critério | Evidência Esperada | Status |
|---|----------|-------------------|--------|
| RC-06 | JpaConfiguration inicializa contexto | Log de startup sem erro | ⬜ |
| RC-07 | BaseEntity e AuditableEntity disponíveis | Classes em `infrastructure/persistence/entity/` | ⬜ |
| RC-08 | BaseRepository funcional | Teste de repositório | ⬜ |
| RC-09 | Transação de leitura Oracle funcional | Teste de integração | ⬜ |
| RC-10 | PersistenceException mapeada no GlobalExceptionHandler | Teste de exception handler | ⬜ |

## Módulo Security

| # | Critério | Evidência Esperada | Status |
|---|----------|-------------------|--------|
| RC-11 | SecurityFilterChain stateless configurado | `SecurityConfiguration` sem session | ⬜ |
| RC-12 | CSRF token repository ativo | Teste CSRF base | ⬜ |
| RC-13 | JwtAuthenticationFilter registrado (esqueleto) | Filtro na chain | ⬜ |
| RC-14 | Endpoints públicos acessíveis sem token | Teste `/api/v1/health`, `/actuator/health` | ⬜ |
| RC-15 | Rotas protegidas rejeitam sem autenticação | Teste 401 Unauthorized | ⬜ |
| RC-16 | CORS configurado conforme padrão | Teste preflight | ⬜ |

## Módulo Integration

| # | Critério | Evidência Esperada | Status |
|---|----------|-------------------|--------|
| RC-17 | RestClient configurado com timeout | Teste de configuração | ⬜ |
| RC-18 | Correlation ID propagado em chamadas outbound | Teste com mock server | ⬜ |
| RC-19 | IntegrationException hierarchy definida | Classes + handler | ⬜ |
| RC-20 | IdentityProviderClient interface definida | Interface sem implementação Zimbra | ⬜ |
| RC-21 | Circuit breaker / retry configurados | Teste de resiliência | ⬜ |

## Módulo Web

| # | Critério | Evidência Esperada | Status |
|---|----------|-------------------|--------|
| RC-22 | Estrutura `interfaces/rest/` criada | Diretórios conforme padrão | ⬜ |
| RC-23 | `GET /api/v1/health` operacional | Teste de integração 200 OK | ⬜ |
| RC-24 | Respostas seguem padrão ApiResponse | Contrato validado | ⬜ |
| RC-25 | OpenAPI 3 documenta endpoints | Swagger UI acessível | ⬜ |
| RC-26 | GlobalExceptionHandler integrado | Teste de erro padronizado | ⬜ |

## Módulo Observability

| # | Critério | Evidência Esperada | Status |
|---|----------|-------------------|--------|
| RC-27 | Métricas Micrometer registradas | Endpoint `/actuator/metrics` | ⬜ |
| RC-28 | Request logging estruturado ativo | Log com method, path, status, duration | ⬜ |
| RC-29 | Correlation ID presente em logs de requisição | Verificação em log de teste | ⬜ |
| RC-30 | DatabaseHealthIndicator funcional | `/actuator/health` inclui db | ⬜ |
| RC-31 | Actuator endpoints com segurança adequada | Apenas endpoints autorizados expostos | ⬜ |

## Módulo Testing

| # | Critério | Evidência Esperada | Status |
|---|----------|-------------------|--------|
| RC-32 | `@IntegrationTest` disponível | Meta-anotação funcional | ⬜ |
| RC-33 | `AbstractIntegrationTest` operacional | Pelo menos 1 teste usando base | ⬜ |
| RC-34 | Utilitários de teste de segurança disponíveis | `TestSecurityContextFactory` | ⬜ |
| RC-35 | Banco de testes configurado | Perfil `test` funcional | ⬜ |

---

# Checklist — Reutilização Garantida

| # | Critério | Status |
|---|----------|--------|
| RR-01 | Nenhum componente de domínio na fundação | ⬜ |
| RR-02 | APIs internas documentadas por módulo (README.md) | ⬜ |
| RR-03 | FT-AUTH pode importar sem reimplementar infraestrutura | ⬜ |
| RR-04 | Bounded contexts futuros podem estender BaseEntity/Repository | ⬜ |
| RR-05 | Padrão Gateway disponível para novas integrações | ⬜ |

---

# Checklist — Arquitetura Preservada

| # | Critério | Status |
|---|----------|--------|
| RA-01 | Stack Java 25 / Spring Boot 4.1 mantida | ⬜ |
| RA-02 | Oracle Database (UNMPORTCOM) mantido | ⬜ |
| RA-03 | Pacote raiz `br.com.unimedceara.portalcomunicacao` | ⬜ |
| RA-04 | Arquitetura stateless preparada (sem HTTP Session) | ⬜ |
| RA-05 | Sprint 0 shared modules intactos | ⬜ |
| RA-06 | Nenhuma decisão arquitetural alterada | ⬜ |

---

# Checklist — Testes Disponíveis

| # | Critério | Status |
|---|----------|--------|
| RT-01 | `mvn clean verify` — SUCCESS | ⬜ |
| RT-02 | Testes Sprint 0 (106) sem regressão | ⬜ |
| RT-03 | Testes novos da fundação aprovados | ⬜ |
| RT-04 | Pelo menos 1 teste de integração end-to-end | ⬜ |
| RT-05 | Testes de segurança base aprovados | ⬜ |

---

# Checklist — Documentação Consistente

| # | Critério | Status |
|---|----------|--------|
| RD-01 | Todos os README.md de módulos preenchidos | ⬜ |
| RD-02 | Todas as tasks.md com tarefas rastreáveis | ⬜ |
| RD-03 | Todos os review.md com DoD definido | ⬜ |
| RD-04 | `09-progress.md` atualizado | ⬜ |
| RD-05 | Sem placeholders ou conteúdo fictício | ⬜ |

---

# Checklist — Backend Preparado para FT-AUTH

| # | Critério | Referência FT-AUTH | Status |
|---|----------|-------------------|--------|
| RB-01 | SecurityFilterChain extensível para JWT | TASK-AUTH-BE-001 | ⬜ |
| RB-02 | CSRF base para cookies | TASK-AUTH-BE-002 | ⬜ |
| RB-03 | Persistence pronta para migration AUTH_SESSAO | TASK-AUTH-DB-001 | ⬜ |
| RB-04 | Integration client base para Zimbra | TASK-AUTH-INT-001 | ⬜ |
| RB-05 | Web layer pronta para controllers auth | TASK-AUTH-BE-003+ | ⬜ |
| RB-06 | Observability pronta para logs de auth | TASK-AUTH-BE-012 | ⬜ |
| RB-07 | Testing pronto para QA auth | TASK-AUTH-QA-* | ⬜ |
| RB-08 | ZimbraProperties configurável por ambiente | TASK-AUTH-INF-001 | ⬜ |

---

# Critério de Aprovação

A Platform Foundation é considerada **PRONTA** quando:

- **100%** dos itens RC-* aprovados
- **100%** dos itens RR-*, RA-*, RT-*, RD-* aprovados
- **100%** dos itens RB-* aprovados
- `review/construction-audit.md` com resultado **APROVADO**

---

# Processo de Revisão

1. Tech Lead executa checklist item a item
2. Evidências registradas em `review/readiness-checklist.md`
3. Itens reprovados geram tarefas de correção no módulo correspondente
4. Revisão repetida até aprovação total
5. Aprovação formal registrada em `review/completion-report.md`

---

# Referências

- `review/readiness-checklist.md` — Registro de execução
- `specs/features/authentication/tasks.md` — Dependências FT-AUTH
- `05-readiness-review.md` (este documento) — Definição do checklist
