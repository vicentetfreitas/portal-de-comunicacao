# Feature Quality Gates

| Campo | Valor |
|--------|--------|
| Versão | 1.1 |
| Status | STABLE |

## Objetivo

Os Quality Gates definem verificações obrigatórias durante o ciclo de vida de uma Feature.

Nenhum Gate pode ser ignorado.

---

# Gate 1 — Specification Ready

Objetivo:

Garantir que a especificação esteja completa.

Verificações:

- Definition of Ready
- `feature.yaml` conforme `specs/foundation/feature-yaml.md`
- specification.md
- use-cases.md
- api.md
- acceptance-tests.md
- tasks.md
- traceability.md (quando o template oficial exigir — ex.: `crud-feature`)

Resultado:

READY_FOR_REVIEW ou REWORK

---

# Gate 2 — Architecture Review

Objetivo:

Validar aderência à arquitetura.

Verificações:

- padrões arquiteturais
- domínio
- integrações
- segurança
- decisões técnicas

Resultado:

APPROVED ou REWORK

---

# Gate 3 — Implementation Review

Objetivo:

Validar implementação.

Verificações:

- código
- testes
- qualidade
- performance
- segurança

Resultado:

APPROVED ou REWORK

---

# Gate 4 — Documentation Review

Objetivo:

Garantir sincronização entre código e documentação.

Verificações:

- specification
- API
- testes
- decisões
- tarefas

Resultado:

APPROVED ou REWORK

---

# Gate 5 — Feature Readiness Review

Objetivo:

Executar auditoria completa da Feature.

Verificações:

- consistência entre artefatos
- rastreabilidade (`traceability.md` quando aplicável)
- cobertura dos requisitos
- critérios de aceitação
- riscos

Resultado:

APPROVED ou REWORK

---

# Gate 6 — Definition of Done

Objetivo:

Validar encerramento da Feature.

Verificações:

- DoD completo
- Todos os Gates aprovados

Resultado:

DONE