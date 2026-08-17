# Relatório para Decisão Humana — Aplicabilidade da DEC-DB-027 (R1)

| Campo | Valor |
|-------|-------|
| Projeto | Portal de Comunicação |
| Artefato | `construction/review/primeiro-acesso-r1-dec-db-027-applicability.md` |
| Feature | FT-PRIMEIRO-ACESSO / domínio CARGO |
| Data submissão | 2026-08-17 |
| Data decisão | 2026-08-17 |
| Tipo | **Submissão para decisão humana** → **DECIDIDA** |
| Categoria documental | Evidence / Governance input |
| Status | **DECIDIDA** — registrada como **DH-CARGO-01** (escopo geral) |
| IDs relacionados | R1, DH-CARGO-01, DH-PA-03, DEC-DB-027, RECONCILIAÇÃO-DEC-DB-027, GAP-028-06 |

**Evidência analítica:** [`primeiro-acesso-dh-pa-03-db-reconciliation.md`](primeiro-acesso-dh-pa-03-db-reconciliation.md) (conflito normativo C — análise histórica).

---

## 1. Objetivo (histórico)

Este artefato submeteu à decisão humana a questão de governança identificada na reconciliação entre **DH-PA-03** e **DEC-DB-027**.

A pergunta R1 original limitava-se ao **Primeiro Acesso**. A decisão humana registrada em **DH-CARGO-01** possui **escopo geral** para criação/cadastro de **qualquer** COLABORADOR — **superset** da alternativa B original e **encerra** a reconciliação normativa.

---

## 2. Questão R1 (histórica)

> A regra da **DEC-DB-027** que torna CARGO obrigatório na criação de um **COLABORADOR** também se aplica ao **COLABORADOR** criado na conclusão do **Primeiro Acesso** (**DH-PA-03**)?

---

## 3. Decisão humana registrada

**ID de governança:** **DH-CARGO-01** — `docs/governance/03-open-decisions.md` § DH-CARGO-01.

### Decisão

> **CARGO é um domínio do sistema e possuirá persistência própria, porém CARGO não é requisito para o cadastro/criação do COLABORADOR. O COLABORADOR pode existir sem CARGO. A atribuição de CARGO ocorrerá posteriormente, em fluxo ainda não definido.**

### Regra normativa resultante

> **CARGO não é obrigatório para a criação/cadastro de qualquer COLABORADOR.**

### Escopo da decisão

**Geral** — abrange Primeiro Acesso, cadastro administrativo e demais fluxos de criação. **Não** é exceção restrita ao PA.

### Equivalência com alternativas R1

| Alternativa R1 (histórica) | Resultado |
|----------------------------|-----------|
| **A — Sim** (DEC-DB-027 integral ao PA) | **Não adotada** |
| **B — Não** (exceção de escopo PA) | **Superada** — decisão é **mais ampla** que B |
| **C — Indefinido** | **Não adotada** |

### Supersession DEC-DB-027

Os itens de **obrigatoriedade de CARGO na criação** de DEC-DB-027 foram **superseded** por **DH-CARGO-01**. Detalhamento: `database/model/05-decisions-and-risks.md` § DEC-DB-027 — Supersession parcial.

**Nenhuma solução técnica** (nullable, default, DDL) foi escolhida nesta decisão.

---

## 4. Card do decisor (preenchido)

```text
═══════════════════════════════════════════════════════════════
 R1 — Resolvida por DH-CARGO-01 (escopo geral)
═══════════════════════════════════════════════════════════════

Resposta efetiva:
[x] CARGO NÃO é requisito na criação/cadastro de QUALQUER COLABORADOR
    (inclui Primeiro Acesso e cadastro administrativo)

Decisão de governança: DH-CARGO-01
Data: 2026-08-17

Supersession: itens de obrigatoriedade na criação — DEC-DB-027
Reconciliação DEC-DB-027 × DH-PA-03: ENCERRADA
```

---

## 5. O que não foi decidido (mantido)

- `COD_CARGO` NULL ou NOT NULL;
- default sistêmico; prazo; workflow; ator; tela; seed; DDL; API.

---

## 6. Regra de governança pós-decisão

A persistência do COLABORADOR **não deve** reintroduzir, por inferência técnica, obrigatoriedade de CARGO na criação. Implementação física de `CARGO`/`COD_CARGO` permanece **delegada** à engenharia.

---

## Referências

| Fonte | Uso |
|-------|-----|
| `docs/governance/03-open-decisions.md` | DH-CARGO-01, DH-PA-03, DEC-DB-027 (ref.) |
| `database/model/05-decisions-and-risks.md` | Supersession parcial DEC-DB-027 |
| `construction/review/primeiro-acesso-dh-pa-03-db-reconciliation.md` | Análise histórica + §14 pós-decisão |

---

| Versão | 2.0 |
|--------|-----|
| Status | **DECIDIDA** — DH-CARGO-01 (2026-08-17); reconciliação encerrada |
