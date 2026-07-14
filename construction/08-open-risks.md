# Open Risks — Construction Sprint 1A

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Sprint | 1A |
| Status | Ativo |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Registrar riscos específicos da construção da Platform Foundation na Sprint 1A.

Riscos arquiteturais ou de negócio já tratados em `docs/governance/02-open-risks.md` não são repetidos.

---

# Escopo

Riscos que podem impactar cronograma, qualidade ou prontidão da Platform Foundation.

---

# Classificação

| Probabilidade | Impacto | Severidade |
|---------------|---------|------------|
| Baixa / Média / Alta | Baixo / Médio / Alto / Crítico | Derivada da matriz |

---

# Resumo

| Status | Quantidade |
|--------|------------|
| Abertos | 6 |
| Em Mitigação | 0 |
| Monitorados | 0 |
| Resolvidos | 0 |

---

# Registro de Riscos

## CR-S1A-001 — Escopo da Fundação Invadir FT-AUTH

| Item | Valor |
|------|-------|
| Categoria | Escopo |
| Probabilidade | Média |
| Impacto | Alto |

### Descrição

Durante a implementação da Platform Foundation, desenvolvedores podem antecipar fluxos de autenticação (login Zimbra, emissão JWT, tabela AUTH_SESSAO) que pertencem exclusivamente a FT-AUTH.

### Mitigação

- Regra R-10 em `04-construction-rules.md` — proibição explícita
- Code review focado em escopo
- Tarefas PF-* não referenciam endpoints `/api/v1/auth/*`
- Auditoria de escopo no Construction Audit

### Responsável

Tech Lead Backend

### Status

**Aberto**

---

## CR-S1A-002 — Dependência Circular entre Security e Web

| Item | Valor |
|------|-------|
| Categoria | Arquitetura |
| Probabilidade | Média |
| Impacto | Alto |

### Descrição

Configuração de SecurityFilterChain pode criar dependência circular se Web controllers forem referenciados diretamente na configuração de segurança.

### Mitigação

- Security configura por patterns de URL, não por classes de controller
- Ordem de construção: Security antes de Web
- Testes de contexto Spring para detectar circular dependency

### Responsável

Arquiteto de Software

### Status

**Aberto**

---

## CR-S1A-003 — Regressão nos Testes da Sprint 0

| Item | Valor |
|------|-------|
| Categoria | Qualidade |
| Probabilidade | Média |
| Impacto | Médio |

### Descrição

Extensão da fundação pode quebrar os 106 testes unitários congelados da Sprint 0, especialmente em `shared/` e `configuration/`.

### Mitigação

- Regra: não modificar Sprint 0 sem justificativa formal
- Executar `mvn clean verify` a cada pacote concluído
- CI deve falhar em regressão

### Responsável

Tech Lead Backend

### Status

**Aberto**

---

## CR-S1A-004 — Oracle 11.2 — Warnings de Dialect Hibernate

| Item | Valor |
|------|-------|
| Categoria | Tecnologia |
| Probabilidade | Alta |
| Impacto | Baixo |

### Descrição

Oracle 11.2 gera warnings de dialect Hibernate durante inicialização JPA. Não bloqueia Sprint 0, mas pode mascarar erros reais na camada Persistence.

### Mitigação

- Configurar dialect explicitamente em PersistenceProperties
- Monitorar logs de startup
- Documentar warning conhecido em `platform-foundation/persistence/review.md`
- Avaliar upgrade Oracle em sprint futura (fora do escopo S1A)

### Responsável

Tech Lead Backend

### Status

**Aberto**

---

## CR-S1A-005 — Decisões Pendentes Bloquearem Pacotes

| Item | Valor |
|------|-------|
| Categoria | Governança |
| Probabilidade | Média |
| Impacto | Médio |

### Descrição

Decisões em `07-open-decisions.md` (especialmente CD-S1A-001 e CD-S1A-004) podem bloquear pacotes PKG-04 e PKG-07 se não resolvidas a tempo.

### Mitigação

- Resolver decisões antes do pacote dependente
- Escalonar ao Arquiteto com prazo definido
- Definir fallback mínimo viável (ex.: timeout sem circuit breaker)

### Responsável

Arquiteto de Software

### Status

**Aberto**

---

## CR-S1A-006 — Ausência de Ambiente Zimbra para Testes de Integração

| Item | Valor |
|------|-------|
| Categoria | Integração |
| Probabilidade | Alta |
| Impacto | Médio |

### Descrição

A Platform Foundation define contrato `IdentityProviderClient` mas não implementa Zimbra. Testes de integração reais com Zimbra só ocorrem em FT-AUTH, podendo revelar incompatibilidades tardias na infraestrutura de integração.

### Mitigação

- Testes da fundação usam mock server (WireMock)
- Contrato `IdentityProviderClient` alinhado a `specs/architecture/authentication-architecture.md`
- Revisão de contrato antes de iniciar FT-AUTH
- Ambiente Zimbra de homologação solicitado antecipadamente à Infra

### Responsável

Tech Lead Backend

### Status

**Aberto**

---

# Riscos Não Registrados (Já Tratados)

| Risco Original | Motivo da Exclusão |
|----------------|-------------------|
| RSK-001 (Escopo funcional incompleto) | Risco de negócio — `docs/governance/02-open-risks.md` |
| RSK-003 (Dívida técnica legado) | Risco arquitetural geral — governança |
| RSK-005 (Integração Zimbra) | Parcialmente coberto; decisão DA-AUTH-008 aprovada |

---

# Matriz de Riscos

| ID | Probabilidade | Impacto | Severidade |
|----|---------------|---------|------------|
| CR-S1A-001 | Média | Alto | **Alta** |
| CR-S1A-002 | Média | Alto | **Alta** |
| CR-S1A-003 | Média | Médio | Média |
| CR-S1A-004 | Alta | Baixo | Baixa |
| CR-S1A-005 | Média | Médio | Média |
| CR-S1A-006 | Alta | Médio | Média |

---

# Referências

- `07-open-decisions.md` — Decisões que originam riscos
- `docs/governance/02-open-risks.md` — Riscos de projeto
