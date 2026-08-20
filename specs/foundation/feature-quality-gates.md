# Feature Quality Gates

| Campo | Valor |
|--------|--------|
| Versão | 1.2 |
| Status | STABLE |

## Objetivo

Os Quality Gates são **verificações formais de critérios**. Não são estados da Feature. Não constituem uma segunda máquina de estados.

O SSOT de estado é `specs/features/<slug>/feature.yaml` (`status`: `DRAFT` \| `READY_FOR_REVIEW` \| `APPROVED` \| `IMPLEMENTING` \| `DONE`).

Resultados de Gate são **PASS** ou **FAIL** (verificação). Não gravar `APPROVED` nem `REWORK` como `status` da Feature a partir dos Gates 2–5.

---

# Uso no fluxo cotidiano mínimo

| Gate | Obrigatoriedade | Momento |
|------|-----------------|--------|
| Gate 1 | **Obrigatório** | Antes de `DRAFT` → `READY_FOR_REVIEW` (DoR-Spec) |
| Gate 2 | Condicional | Aderência arquitetural quando a Feature exigir |
| Gate 3 | **Obrigatório** | Review de PR durante `IMPLEMENTING`; exigido para `DONE` |
| Gate 4 | Condicional | Sincronização código/documentação quando aplicável |
| Gate 5 | Condicional | Auditoria completa da Feature quando aplicável |
| Gate 6 | **Obrigatório** | Encerramento: DoD; exigido para `IMPLEMENTING` → `DONE` |

Gates condicionais **permanecem disponíveis**. Não foram removidos. Não precisam ser serializados como etapas de status.

---

# Gate 1 — Specification Ready

Objetivo: garantir que a especificação está pronta para revisão formal.

Verificações (DoR-Spec):

- critérios DoR-Spec em `definition-of-ready.md`
- `feature.yaml` conforme `feature-yaml.md`
- artefatos obrigatórios do template (ex.: specification.md, use-cases.md, api.md, acceptance-tests.md; `traceability.md` quando exigido)
- ausência de bloqueadores conhecidos para a revisão da spec

`tasks.md` completo **não** é exigência do Gate 1 (pertence ao DoR-Implementation).

Resultado da verificação: **PASS** ou **FAIL**.

- PASS: autoriza transitar `status` para `READY_FOR_REVIEW`
- FAIL: `status` permanece `DRAFT`

---

# Gate 2 — Architecture Review

Objetivo: validar aderência à arquitetura (quando aplicável).

Verificações: padrões arquiteturais; domínio; integrações; segurança; decisões técnicas.

Resultado: **PASS** ou **FAIL**. Não altera `status` da Feature para `APPROVED`.

---

# Gate 3 — Implementation Review

Objetivo: validar a implementação (Review de PR).

Verificações: código; testes; qualidade; performance; segurança; aderência à spec.

Resultado: **PASS** ou **FAIL**.

Obrigatório no caminho para `DONE`. Não substitui Validate. Não altera `status` automaticamente.

---

# Gate 4 — Documentation Review

Objetivo: sincronização entre código e documentação (quando aplicável).

Verificações: specification; API; testes; decisões; tarefas.

Resultado: **PASS** ou **FAIL**. Não é `status` da Feature.

---

# Gate 5 — Feature Readiness Review

Objetivo: auditoria completa da Feature (quando aplicável).

Verificações: consistência entre artefatos; rastreabilidade (`traceability.md` quando aplicável); cobertura dos requisitos; critérios de aceitação; riscos.

Resultado: **PASS** ou **FAIL**. Não é `status` da Feature.

---

# Gate 6 — Definition of Done

Objetivo: validar encerramento.

Verificações:

- DoD completo (`definition-of-done.md`)
- Gate 1 e Gate 3 com PASS
- Gates 2, 4 e 5 com PASS **quando tiverem sido aplicáveis**

Resultado: **PASS** ou **FAIL**.

- PASS: autoriza transitar `status` para `DONE`
- FAIL: `status` permanece `IMPLEMENTING`

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.1 | — | Gates 1–6; resultados misturados com estados (`REWORK`, `APPROVED`) |
| 1.2 | 2026-08-19 | Gates como verificação; 1/3/6 obrigatórios no mínimo; 2/4/5 condicionais |
