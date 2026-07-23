# Evidência de validação estrutural — Oracle homologado

**Schema:** `UNMPORTCOM`  
**Data da homologação:** 2026-07-22  
**Atividade de encerramento:** DB-SYNC-99  
**SSOT:** [../baseline/oracle-baseline-2026-07-22.md](../baseline/oracle-baseline-2026-07-22.md)

---

## Resultado

Validação estrutural final no ambiente homologado, após criação de `PK_FEDERACAO`, `PK_CATEGORIA_DOCUMENTAL` e das FKs pendentes. O inventário nominal confirma **sincronização** entre o Oracle homologado e os DDLs versionados em `database/ddl/`.

```text
DB-SYNC-99 = PASS
```

---

## Contagens homologadas

| Objeto | Quantidade |
|--------|----------:|
| Tabelas | 23 |
| Primary Keys | 23 |
| Foreign Keys | 36 |
| UNIQUE | 11 |
| CHECK | 172 |
| Índices (`USER_INDEXES`, exc. `SYS_%`) | 95 |
| Sequences | 12 |
| Objetos inválidos | 0 |
| Objetos `BIN$` | 0 |

Agregado de constraints (`P` + `R` + `U` + `C` em `USER_CONSTRAINTS`): **242** (23 + 36 + 11 + 172).

---

## Validação automatizada

Script oficial pós-instalação (greenfield):

```text
database/ddl/901-validation.sql
```

Expectativas alinhadas às contagens acima (executar como `UNMPORTCOM` após `000-install.sql` ou em brownfield equivalente).

---

## Escopo da evidência

| Incluído | Excluído |
|----------|----------|
| Contagens estruturais homologadas | DML / dados de negócio |
| Confirmação de zero objetos inválidos | Diff linha a linha de scripts (ver baseline + DDL) |
| Rastreabilidade da atividade DB-SYNC-99 | Relatórios intermediários de investigação (removidos do repositório) |

---

## Referências

| Artefato | Papel |
|----------|--------|
| [../baseline/oracle-baseline-2026-07-22.md](../baseline/oracle-baseline-2026-07-22.md) | Estado oficial do schema |
| [../reports/sync-report-2026-07-22.md](../reports/sync-report-2026-07-22.md) | Relatório final de sincronização Oracle × DDL |
| [../GOVERNANCE.md](../GOVERNANCE.md) | Precedência da camada `database/` |
