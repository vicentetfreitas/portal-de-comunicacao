---
description: Consultar estado geral do projeto (não de uma Feature) com leitura mínima.
argument-hint: [tema opcional, ex. "DEC-015" ou "repositórios"]
---

Modo Project Status. Estado do **projeto**, não de uma Feature (para isso, usar `/status FT-<CODE>`). Não alterar artefatos.

Tema: $ARGUMENTS

## Ordem de leitura (obrigatória, parar assim que responder)

1. `docs/governance/01-project-status.md` — é o índice já condensado; ler inteiro primeiro. Na maioria das perguntas, já é suficiente.
2. Só se o tema pedir um `DEC-*`/`GAP-DEC-*` específico não coberto pelo índice: `grep -n "DEC-XXX"` em `docs/governance/03-open-decisions.md` ou `docs/technology/04-decision-log.md` — ler só a seção encontrada, não o arquivo inteiro.
3. Só se o tema for sobre auditoria/reclassificação documental: `docs/audit/14-governance-audit-inventory-status.md` primeiro (é o levantamento mais recente sobre o que já foi aplicado) — só abrir `12-...`/`13-...`/`15-...` se `14-...` não cobrir.

## Regras

- **Nunca** ler `03-open-decisions.md`, `04-decision-log.md` ou qualquer `docs/audit/*` por inteiro só para responder "qual o próximo passo" ou "o que está pendente" — essas perguntas já são respondidas por `01-project-status.md`.
- Ler arquivo inteiro só quando for de fato editar/registrar algo nele.
- `docs/governance/05-roadmap.md` está desatualizado (ver Pendências de `01-project-status.md`) — não usar como fonte de estado atual sem cruzar com o índice.
- Se `01-project-status.md` estiver ele mesmo desatualizado em relação ao código (`feature.yaml`, commits), registrar a divergência ali antes de responder — não silenciosamente confiar em nenhum dos dois.
