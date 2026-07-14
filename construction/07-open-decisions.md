# Open Decisions — Construction Sprint 1A

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Sprint | 1A |
| Status | Ativo |
| Versão | 1.0 |
| Última atualização | 2026-07-09 |

---

# Objetivo

Registrar decisões técnicas pendentes específicas da construção da Platform Foundation.

Decisões já encerradas na Sprint 0 ou em `docs/governance/03-open-decisions.md` não são duplicadas aqui.

---

# Escopo

Apenas decisões que impactam a implementação dos módulos PF-CONF a PF-TEST.

---

# Processo

```text
Identificação → Análise → Aprovação → Implementação → Encerramento
```

Decisões encerradas devem ser removidas deste documento e referenciadas no módulo ou código correspondente.

---

# Resumo

| Status | Quantidade |
|--------|------------|
| Abertas | 0 |
| Em Análise | 0 |
| Encerradas (Sprint 1A) | 5 |

---

# Decisões Abertas

_Nenhuma decisão aberta na Sprint 1A. Ver seção **Decisões Encerradas (Sprint 1A)**._

---

# Decisões Encerradas (Sprint 1A)

| ID | Decisão | Opção escolhida | Encerramento | Evidência |
|----|---------|-----------------|--------------|-----------|
| CD-S1A-001 | Banco de testes de integração | **B** — H2 modo Oracle | 2026-07-09 | `application-test.yaml`, PKG-07 |
| CD-S1A-002 | OpenAPI / SpringDoc SB 4 | **A** — springdoc 3.0.3 | 2026-07-09 | `OpenApiConfiguration`, PKG-05 |
| CD-S1A-003 | MapStruct na fundação | **B** — adiado para FT-AUTH | 2026-07-09 | Ausência de MapStruct no código |
| CD-S1A-004 | Resiliência HTTP | **A** — Resilience4j 2.4.0 | 2026-07-09 | `ResilienceConfiguration`, PKG-04 |
| CD-S1A-005 | Naming métricas Micrometer | **A** — prefixo `portal.*` | 2026-07-09 | `ObservabilityConstants`, PKG-06 |

---

# Histórico — Decisões Abertas (arquivo)

## CD-S1A-001 — Estratégia de Banco de Dados para Testes de Integração

| Item | Valor |
|------|-------|
| Status | **Encerrada** — Opção B (H2 Oracle mode) |

## CD-S1A-002 — Biblioteca OpenAPI / SpringDoc para Spring Boot 4

| Item | Valor |
|------|-------|
| Status | **Encerrada** — Opção A (springdoc-openapi-starter-webmvc-ui 3.0.3) |

## CD-S1A-003 — Adoção de MapStruct na Platform Foundation

| Item | Valor |
|------|-------|
| Status | **Encerrada** — Opção B (adiado para FT-AUTH) |

## CD-S1A-004 — Estratégia de Resiliência para Cliente HTTP

| Item | Valor |
|------|-------|
| Status | **Encerrada** — Opção A (Resilience4j) |

## CD-S1A-005 — Convenção de Naming para Métricas Micrometer

| Item | Valor |
|------|-------|
| Status | **Encerrada** — Opção A (`portal.<modulo>.<metrica>`) |

---

# Decisões Encerradas (Referência — Não Duplicar)

As decisões abaixo foram resolvidas na Sprint 0 ou em governança. **Não reabrir.**

| ID Original | Decisão | Registro |
|-------------|---------|----------|
| DEC-005 | Versionamento SemVer | `docs/governance/03-open-decisions.md` |
| DEC-006 | Estratégia de testes (unitários + verify) | `docs/governance/03-open-decisions.md` |
| DEC-007 | Oracle Database (UNMPORTCOM) | `docs/governance/03-open-decisions.md` |
| DA-AUTH-001 a DA-AUTH-010 | Decisões de autenticação | `specs/features/authentication/decisions.md` |

---

# Dependências

- `08-open-risks.md` — Riscos derivados de decisões pendentes
- `platform-foundation/*/tasks.md` — Tarefas bloqueadas por decisões

---

# Referências

- `docs/governance/03-open-decisions.md` — Decisões de projeto
- `specs/features/authentication/decisions.md` — Decisões FT-AUTH
