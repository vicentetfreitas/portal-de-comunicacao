# Development Order

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Sprint | 1A |
| Status | Aprovado |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Definir a sequência incremental oficial de desenvolvimento da Platform Foundation, garantindo que cada pacote dependa apenas dos anteriores e evitando dependências circulares.

---

# Ordem Oficial

```text
1. Configuration
        ↓
2. Persistence
        ↓
3. Security
        ↓
4. Integration
        ↓
5. Web
        ↓
6. Observability
        ↓
7. Testing
        ↓
8. Construction Audit
```

---

# Justificativa por Módulo

## 1. Configuration (Primeiro)

**Por quê primeiro:** Todos os módulos subsequentes dependem de properties tipadas e beans de configuração.

**Dependências:** Sprint 0 (`ApplicationProperties`, `JacksonConfiguration`, etc.)

**Entrega mínima:** SecurityProperties, PersistenceProperties, IntegrationProperties, ZimbraProperties.

**Bloqueia:** Persistence (datasource config), Security (JWT/CSRF config), Integration (timeout config).

---

## 2. Persistence (Segundo)

**Por quê segundo:** Security (sessões FT-AUTH) e Features dependem de acesso a dados. A fundação deve estabelecer JPA antes de qualquer camada que persista.

**Dependências:** Configuration (PersistenceProperties).

**Entrega mínima:** JpaConfiguration, BaseEntity, BaseRepository.

**Bloqueia:** Security (futuras entidades de sessão), Observability (DatabaseHealthIndicator).

**Nota:** Entidades de domínio FT-AUTH (AUTH_SESSAO) serão criadas na Sprint 1, mas a infraestrutura JPA deve existir antes.

---

## 3. Security (Terceiro)

**Por quê terceiro:** Integration propaga contexto de segurança; Web protege endpoints; FT-AUTH estende o filter chain.

**Dependências:** Configuration (SecurityProperties), Persistence (opcional para testes com contexto).

**Entrega mínima:** SecurityFilterChain stateless, CSRF base, JWT filter esqueleto.

**Bloqueia:** Integration (propagação de headers), Web (proteção de endpoints), Testing (security test utils).

---

## 4. Integration (Quarto)

**Por quê quarto:** Depende de configuração (timeouts) e segurança (propagação de contexto). Web e FT-AUTH consomem clientes de integração.

**Dependências:** Configuration (IntegrationProperties), Security (context propagation).

**Entrega mínima:** RestClient, CorrelationIdInterceptor, IdentityProviderClient interface.

**Bloqueia:** Observability (métricas de integração), FT-AUTH (Zimbra client).

---

## 5. Web (Quinto)

**Por quê quinto:** Expõe endpoints REST que dependem de SecurityFilterChain e Configuration. Health endpoint valida toda a stack até este ponto.

**Dependências:** Configuration, Security.

**Entrega mínima:** HealthController, OpenAPI config, estrutura REST.

**Bloqueia:** Observability (request logging em controllers), Testing (WebMvcTest).

**Nota:** Pode iniciar em paralelo com Integration após Security, mas Observability exige ambos concluídos.

---

## 6. Observability (Sexto)

**Por quê sexto:** Instrumenta camadas já construídas. Request logging requer Web; health indicators requerem Persistence e Integration.

**Dependências:** Web, Integration, Persistence.

**Entrega mínima:** Metrics, RequestLoggingFilter, DatabaseHealthIndicator.

**Bloqueia:** Testing (validação de observabilidade em testes de integração).

---

## 7. Testing (Sétimo)

**Por quê sétimo:** Infraestrutura de testes valida todos os módulos anteriores integrados.

**Dependências:** Todos os módulos 1–6.

**Entrega mínima:** AbstractIntegrationTest, @IntegrationTest, security test utils.

**Bloqueia:** Construction Audit.

---

## 8. Construction Audit (Último)

**Por quê último:** Valida aderência de toda a fundação antes de liberar FT-AUTH.

**Dependências:** Todos os módulos 1–7.

**Entrega:** Relatórios em `review/`.

---

# Sequência de Tarefas por Pacote

## PKG-01 — Configuration

```text
PF-CONF-001 → PF-CONF-002 → PF-CONF-003 → PF-CONF-004 → PF-CONF-005
```

## PKG-02 — Persistence

```text
PF-PERS-001 → PF-PERS-002 → PF-PERS-003 → PF-PERS-004 → PF-PERS-005 → PF-PERS-006
```

## PKG-03 — Security

```text
PF-SEC-001 → PF-SEC-002 → PF-SEC-003 → PF-SEC-004 → PF-SEC-005 → PF-SEC-006
```

## PKG-04 — Integration

```text
PF-INT-001 → PF-INT-002 → PF-INT-003 → PF-INT-004 → PF-INT-005
```

## PKG-05 — Web

```text
PF-WEB-001 → PF-WEB-002 → PF-WEB-003 → PF-WEB-004 → PF-WEB-005
```

## PKG-06 — Observability

```text
PF-OBS-001 → PF-OBS-002 → PF-OBS-003 → PF-OBS-004 → PF-OBS-005
```

## PKG-07 — Testing

```text
PF-TEST-001 → PF-TEST-002 → PF-TEST-003 → PF-TEST-004 → PF-TEST-005
```

---

# Paralelismo Permitido

| Atividades | Paralelismo | Condição |
|------------|-------------|----------|
| PKG-04 + PKG-05 | Parcial | Ambos após PKG-03 |
| Testes unitários dentro de um pacote | Sim | Conforme dependências internas |
| Documentação de review | Sim | Após implementação do módulo |

**Paralelismo proibido:** Iniciar PKG-N+1 antes de concluir dependências obrigatórias de PKG-N.

---

# Dependências Proibidas (Circulares)

| De | Para | Motivo da Proibição |
|----|------|---------------------|
| Web | Integration | Web não deve conhecer clientes externos |
| Persistence | Security | JPA não deve depender de filter chain |
| Testing | qualquer módulo posterior | Testes validam o que já existe |
| Configuration | qualquer módulo posterior | Config é raiz da dependência |

---

# Validação de Ordem

Antes de iniciar cada pacote, confirmar:

1. Pacote anterior com `review.md` aprovado
2. Build `mvn clean verify` — SUCCESS
3. Tarefas do pacote anterior com status concluído em `09-progress.md`
4. Nenhum bloqueio aberto em `07-open-decisions.md` para o pacote

---

# Referências

- `02-construction-roadmap.md` — Fases e cronograma
- `03-construction-packages.md` — Definição dos pacotes
- `platform-foundation/*/tasks.md` — Backlog detalhado
