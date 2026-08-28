# Traceability

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — Feature de leitura, sobre backend já aprovado) |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-AREA-COLABORADOR |
| Feature | Área — Visão do Colaborador |
| Domínio | AREA-COLAB |

---

# Objetivo

Este documento consolida a rastreabilidade completa da Feature FT-AREA-COLABORADOR.

Responsabilidade exclusiva: relacionar Requisitos, Regras de Negócio, Casos de Uso, API, Acceptance Tests e Tasks em uma visão única. Não substitui os demais artefatos nem duplica requisitos, contratos ou cenários.

Esta Feature não define contrato de API próprio (`api.md` não existe) — consome exclusivamente endpoints já `APPROVED` de `FT-AREA` e `FT-EQUIPE`. A coluna API desta matriz referencia esses contratos externos.

---

# Escopo da Cadeia

```text
RF
 ↓
RN
 ↓
UC
 ↓
API
 ↓
AT
 ↓
TK
```

---

# Matriz de Rastreabilidade Consolidada

| RF | RN | UC | API | AT | TK | Status |
|----|----|----|-----|----|----|--------|
| RF-AREA-COLAB-001 | — (herdado de `FT-AUTH`/`FT-SESSION`) | UC-AREA-COLAB-001 | — (composição de rota, sem chamada de API própria) | AT-AREA-COLAB-001, AT-AREA-COLAB-002 | TK-AREA-COLAB-001, TK-AREA-COLAB-004 | COMPLETE |
| RF-AREA-COLAB-002 | — (herdado de `FT-AREA`) | UC-AREA-COLAB-002 | GET /api/v1/areas/{id} (`FT-AREA`, APPROVED) | AT-AREA-COLAB-003, AT-AREA-COLAB-004 | TK-AREA-COLAB-002 | COMPLETE |
| RF-AREA-COLAB-003 | — (herdado de `FT-EQUIPE`) | UC-AREA-COLAB-003 | GET /api/v1/equipes (`FT-EQUIPE`, APPROVED) | AT-AREA-COLAB-005, AT-AREA-COLAB-006, AT-AREA-COLAB-007 | TK-AREA-COLAB-003 | COMPLETE |

Coluna **Status** `COMPLETE`: matriz validada, incluindo `tasks.md` (criado para DoR-Implementation).

---

# Cobertura

| Item | Total | Cobertos | Pendentes |
|------|------:|---------:|----------:|
| Requisitos Funcionais | 3 | 3 | 0 |
| Regras de Negócio (específicas desta Feature) | 0 | 0 | 0 |
| Casos de Uso | 3 | 3 | 0 |
| Endpoints (externos, reutilizados) | 2 | 2 | 0 |
| Acceptance Tests | 7 | 7 | 0 |
| Tasks | 4 | 4 | 0 |

---

# Validações Obrigatórias

Antes de encerrar a fase de Specification:

- [x] Todos os RF possuem UC e AT
- [x] Todos os RF que consomem API têm o endpoint externo identificado (`FT-AREA`/`FT-EQUIPE`, ambas `APPROVED`)
- [x] Todos os AT possuem RF associado
- [x] Todas as TK possuem RF associado
- [x] Nenhum endpoint sem justificativa funcional (RF)
- [x] Matriz consistente com `specification.md`, `use-cases.md`, `acceptance-tests.md` e `tasks.md`

---

# Critérios de Conclusão

A rastreabilidade será considerada **COMPLETE** quando:

- 100% dos Requisitos Funcionais estiverem cobertos na matriz — atendido;
- não existirem artefatos órfãos — atendido;
- não houver divergência entre este documento e os demais artefatos — atendido, incluindo a redução de escopo de RF-AREA-COLAB-003 registrada em "Decisões de produto" (`specification.md`).

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | 2026-08-20 | Engineering Framework | Criação — fechamento documental DoR-Spec |
| 1.1 | 2026-08-25 | Gate 3 REVIEW (pendência NECESSÁRIO_PARA_CONCLUIR) | Inclusão de TK-AREA-COLAB-004 na matriz (RF-AREA-COLAB-001) e na Cobertura — sincronização com `tasks.md` v1.1 |
