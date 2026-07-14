# Testing Module

| Item | Valor |
|------|-------|
| Módulo | Testing |
| Prefixo | PF-TEST |
| Pacote | `src/test/java/.../support/` |
| Pacote Construction | PKG-07 |
| Status | Concluído |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Estabelecer infraestrutura de testes reutilizável para testes unitários, de integração e de segurança de todos os módulos da Platform Foundation e Features futuras.

---

# Escopo

## Inclui

- `@IntegrationTest` — meta-anotação para testes de integração
- `AbstractIntegrationTest` — classe base com contexto Spring completo
- `TestSecurityContextFactory` — utilitários para simular autenticação em testes
- Configuração de perfil `test` com banco de dados
- Convenções de nomenclatura e organização de testes
- Builders/fixtures compartilhados

## Não inclui

- Testes de cenários FT-AUTH (Sprint 1)
- Testes de negócio de Features
- Testes E2E frontend
- Cobertura de código automatizada em CI (decisão futura)

---

# Responsabilidades

| Componente | Responsabilidade |
|------------|------------------|
| @IntegrationTest | Marcar testes de integração com perfil e config |
| AbstractIntegrationTest | Setup comum: RestClient, perfil test, porta aleatória |
| TestSecurityContextFactory | Criar SecurityContext com JWT mock para testes |
| Test profile config | application-test.yaml com datasource de teste |

---

# Limites

- Sem testes de cenários de negócio
- Sem dependência de Zimbra real
- Decisão CD-S1A-001 define estratégia de banco

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| PKG-01 a PKG-06 | Todos os módulos | Pendente |
| CD-S1A-001 | Banco de testes | Pendente |
| Sprint 0 testes | 106 testes unitários | Concluído |
| DEC-006 | Estratégia de testes | Aprovado |

---

# Componentes Esperados

```text
src/test/java/br/com/unimedceara/portalcomunicacao/support/
├── annotation/
│   └── IntegrationTest.java
├── base/
│   └── AbstractIntegrationTest.java
└── security/
    └── TestSecurityContextFactory.java

src/test/resources/
└── application-test.yaml
```

---

# Ordem de Construção

```text
PF-TEST-001 (Perfil test + application-test.yaml)
    → PF-TEST-002 (@IntegrationTest + AbstractIntegrationTest)
    → PF-TEST-003 (TestSecurityContextFactory)
    → PF-TEST-004 (Teste integração health end-to-end)
    → PF-TEST-005 (Documentação convenções de teste)
```

---

# Critérios de Aceite

1. Perfil `test` funcional com banco de dados (conforme CD-S1A-001)
2. @IntegrationTest carrega contexto Spring completo
3. AbstractIntegrationTest utilizável por testes de módulos
4. TestSecurityContextFactory simula autenticação
5. Pelo menos 1 teste de integração end-to-end (health) aprovado
6. Convenções documentadas

---

# Definition of Done do Módulo

- [x] Todas as tarefas PF-TEST-* concluídas
- [x] Testes aprovados
- [ ] `review.md` validado
- [ ] Build SUCCESS
- [ ] FT-AUTH pode usar infraestrutura (TASK-AUTH-QA-*)

---

# Convenções de Teste

## Nomenclatura

| Tipo | Sufixo | Exemplo |
|------|--------|---------|
| Teste unitário | `*Test` | `JwtStructureValidatorTest` |
| Teste de slice MVC | `*WebMvcTest` | `HealthControllerWebMvcTest` |
| Teste de integração (contexto completo) | `*IntegrationTest` ou `*E2ETest` | `SecurityFilterChainIntegrationTest`, `HealthEndpointE2ETest` |

## Organização de pacotes

```text
src/test/java/.../
├── support/                    # Infraestrutura compartilhada (PKG-07)
│   ├── annotation/             # @IntegrationTest
│   ├── base/                   # AbstractIntegrationTest
│   └── security/               # TestSecurityContextFactory
├── infrastructure/<módulo>/    # Testes do módulo de infraestrutura
└── interfaces/rest/            # Testes de controllers e E2E de API
```

## Quando usar cada abordagem

| Anotação / base | Escopo | Quando usar |
|-----------------|--------|-------------|
| `@WebMvcTest` | Slice MVC (controller isolado) | Validar mapeamento, serialização e status HTTP sem subir contexto completo |
| `@SpringBootTest` + `MockMvc` | Contexto completo, sem porta real | Testar filtros de segurança, CSRF e integração servlet no mesmo processo |
| `@IntegrationTest` / `AbstractIntegrationTest` | Contexto completo + `RANDOM_PORT` | E2E HTTP real contra servidor embarcado (ex.: health, fluxos FT-AUTH) |
| Teste unitário puro (`*Test`) | Sem contexto Spring | Validadores, utilitários e lógica isolada |

## Perfil e banco de teste

- Perfil ativo: `test` (via `@IntegrationTest` ou `@ActiveProfiles("test")`).
- Configuração: `src/test/resources/application-test.yaml`.
- Estratégia CD-S1A-001 opção B: H2 em modo Oracle (`MODE=Oracle`), JPA `create-drop` (schema em memória; sem DDL DBA).
- Testes de módulo isolado continuam usando `pf-*-test.properties` para excluir beans não necessários.

## Autenticação em testes

- **HTTP (cookie JWT):** `TestSecurityContextFactory.jwtCookie(subject)` ou `buildJwtToken(subject)`.
- **SecurityContext programático:** `TestSecurityContextFactory.setAuthenticatedUser(subject)` + `clear()` no `@AfterEach`.
- Tokens são estruturalmente válidos (3 partes, claim `sub`); assinatura criptográfica é escopo de FT-AUTH.

## Cliente HTTP em integração

`AbstractIntegrationTest` expõe `RestClient` apontando para `http://localhost:{port}` (Spring Boot 4 — `TestRestTemplate` deprecado).

## Artefatos de runtime (testes)

Relatórios Surefire e logs de build **não** são gravados em `target/surefire-reports/` nem na raiz de `backend/`.

| Artefato | Destino oficial |
|----------|-----------------|
| Relatórios Surefire | `backend/runtime/reports/surefire/` |
| Log da aplicação (testes) | `backend/runtime/logs/application.log` |
| Log de build | `backend/runtime/logs/` |

Convenção completa: `docs/construction/backend/01-project-bootstrap.md` § Artefatos de Runtime.

---

# Rastreabilidade

- `docs/construction/backend/01-project-bootstrap.md` § Testes
- `specs/foundation/definition-of-done.md`
- `construction/03-construction-packages.md` § PKG-07
