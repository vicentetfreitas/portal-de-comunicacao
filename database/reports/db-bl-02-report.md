# Relatório DB-BL-02 — Normalização da Oracle Baseline

**Data:** 2026-07-22  
**Atividade:** DB-BL-02  
**Schema:** `UNMPORTCOM`

---

## Resumo executivo

A baseline `database/baseline/oracle-baseline-2026-07-22.md` foi **reestruturada** para separar inventário nominal existente, estatísticas agregadas, estrutura parcial conhecida (tabela **AREA**), convenções e **lacunas explícitas**.

Nenhuma informação foi inferida a partir de DDL. Nenhum script em `database/ddl/`, `dml/` ou `model/` foi alterado.

A baseline **não** atende, por si só, aos pré-requisitos de **DB-SYNC-01** determinístico — isso está declarado no próprio documento (§ Pré-requisitos para DB-SYNC-01).

```text
DB-BL-02 = PASS
```

---

## Arquivos alterados

| Arquivo | Ação |
|---------|------|
| `database/baseline/oracle-baseline-2026-07-22.md` | Reestruturado integralmente (conteúdo factual preservado; lacunas explicitadas) |
| `database/reports/db-bl-02-report.md` | Criado (este relatório) |

**Não alterados:** `database/ddl/`, `database/dml/`, `database/model/`.

---

## Matriz de cobertura documental

| Categoria | Cobertura | Fonte |
|-----------|-----------|-------|
| Tabelas | **COMPLETO** | Baseline — § Inventário de Tabelas |
| Colunas | **PARCIAL** | Baseline — § Estrutura conhecida (AREA) |
| Primary Keys | **PARCIAL** | Baseline — `PK_AREA` + estatística 22 |
| Foreign Keys | **PARCIAL** | Baseline — 2 FK de AREA + estatística 29 |
| UNIQUE | **AUSENTE** | Baseline — apenas estatística 13 |
| CHECK | **AUSENTE** | Baseline — apenas estatística 180 |
| Índices | **PARCIAL** | Baseline — AREA (3 nomes) + estatística 93 |
| Sequences | **COMPLETO** | Baseline — § Inventário de Sequences |
| Comentários | **PARCIAL** | Baseline — comentário de tabela AREA |
| Relacionamentos | **PARCIAL** | Baseline — 2 linhas AREA |

---

## Lacunas encontradas

Consolidadas em **§ Lacunas para Sincronização** na baseline:

1. Inventário nominal completo de PK (22 homologadas)
2. Inventário nominal completo de FK (29 homologadas)
3. Inventário nominal de UNIQUE (13)
4. Inventário nominal de CHECK (180, incl. política `SYS_C*`)
5. Inventário nominal de índices (93)
6. Colunas para 22 tabelas (além de AREA)
7. Comentários de coluna
8. Matriz completa de relacionamentos FK → PK

Remissões históricas a `ddl/003`–`006` foram mantidas apenas como **localização de implementação**, não como preenchimento de lacuna.

---

## Recomendações para evolução da baseline

1. **Próxima homologação:** anexar ou incorporar inventário nominal Oracle (export `USER_*`) **sem** objetos `BIN$`, preenchendo cada categoria AUSENTE/PARCIAL.
2. **DB-SYNC-01:** executar somente após gate de pré-validação (todas as categorias necessárias **COMPLETO** ou política explícita para `SYS_C*`).
3. **Governança:** atualizar `GOVERNANCE.md` em atividade futura se a tríade baseline / validation / inventário nominal for formalizada.
4. **Não** usar cópia de DDL para “completar” a baseline — viola rastreabilidade homologada.

---

## Evidências utilizadas

- Conteúdo anterior de `oracle-baseline-2026-07-22.md` (única fonte normativa permitida para fatos da baseline)
- Estrutura de pastas `database/` (referência cruzada de caminhos, sem leitura normativa de DDL)

---

## Conclusão

```text
DB-BL-02 = PASS
```

A baseline está **normalizada**, **auditável** e com **lacunas explícitas**. A sincronização determinística permanece **bloqueada** até evolução documental homologada.
