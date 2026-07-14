# Traceability Rules

Regras oficiais de rastreabilidade do Portal de Comunicação.

---

# Regras

Toda Feature deve possuir Epic.

Todo Epic deve possuir origem Domain.

Toda User Story deve possuir Feature.

Toda tarefa de Construction deve possuir referência a uma User Story ou Feature.

Itens sem rastreabilidade são inválidos.

---

# Validação Pós-Sprint 0

As regras acima permanecem **válidas e inalteradas** após o encerramento da Sprint 0.

| Verificação | Resultado |
| ----------- | --------- |
| FT-AUTH possui Epic | ✅ EPIC-003 (Controle de Acesso) |
| FT-AUTH possui artefatos completos em `specs/features/authentication/` | ✅ specification, use-cases, api, acceptance-tests, tasks, decisions |
| Sprint 0 — infraestrutura transversal | ✅ Sem Feature de negócio — escopo de fundação (EPIC-001) |
| Tasks FT-AUTH referenciam Feature ID | ✅ TASK-AUTH-* → FT-AUTH |

Nenhuma regra de rastreabilidade foi alterada na Sprint 0.

---

# Histórico de Atualizações

| Data       | Autor      | Alteração                                      |
| ---------- | ---------- | ---------------------------------------------- |
| YYYY-MM-DD | Governança | Criação inicial                                |
| 2026-07-08 | Governança | Confirmação de validade pós-Sprint 0 — sem alteração de regras |
