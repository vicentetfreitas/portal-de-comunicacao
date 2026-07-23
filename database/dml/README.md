# DML — Carga institucional Greenfield

| Item | Valor |
|------|-------|
| Schema | UNMPORTCOM |
| Premissa | Banco vazio · instalação inicial · **execução única** |
| Operação SQL | Apenas `INSERT` (+ `COMMIT`) |
| Reexecução | **Não suportada** |
| Evolução pós-instalação | [migrations/](../migrations/README.md) |
| Hierarquia | [DEC-DB-021](../model/05-decisions-and-risks.md#dec-db-021--organização-raiz-e-hierarquia-institucional) |
| Modelo | [02-conceptual-model.md](../model/02-conceptual-model.md) · [03-physical-model.md](../model/03-physical-model.md) |

Referência entre scripts: `COD_FEDERACAO` (não usar `COD_UNIMED` como FK em `002`–`006`).

---

## Estratégia

A camada `dml/` materializa os dados institucionais iniciais do Portal em ambiente **Greenfield**. O DBA executa cada script **uma vez**, na ordem abaixo, após a estrutura DDL e o bootstrap técnico (`ddl/008-initial-data.sql`).

Alterações de dados ou estrutura após a implantação **não** devem ser feitas reexecutando estes scripts. Use scripts versionados em [migrations/](../migrations/README.md) (brownfield), conforme [GOVERNANCE.md](../GOVERNANCE.md).

---

## Sequência oficial de persistência

```text
001-federacao.sql
    ↓
002-singulares.sql
    ↓
003-areas.sql
    ↓
004-equipes.sql
    ↓
005-colaboradores.sql
    ↓
006-homologacao-opcional.sql   (opcional)
```

---

## Responsabilidade por script

| Script | Conteúdo | `000-install` |
|--------|----------|---------------|
| `001-federacao.sql` | Federação Unimed Ceará (979, ativa) | Sim |
| `002-singulares.sql` | Singulares confirmadas | Não* |
| `003-areas.sql` | Áreas federação e piloto Cariri | Não* |
| `004-equipes.sql` | Equipes de referência | Não* |
| `005-colaboradores.sql` | Sem INSERT (FT-AUTH) | Não* |
| `006-homologacao-opcional.sql` | Extensão homologação | Não* |

\* Executar na mesma janela de implantação Greenfield, na ordem numérica, quando o escopo institucional estiver aprovado.

---

## Ordem de execução (DBA)

```text
@ddl/001-create-users.sql                 -- SYS
@ddl/000-install.sql                      -- 002–007, 008, dml/001, ddl/009-config
@ddl/901-validation.sql
@dml/002-singulares.sql                   -- implantação inicial (se no escopo)
@dml/003-areas.sql
@dml/004-equipes.sql
@dml/006-homologacao-opcional.sql          -- homologação, se aplicável
```

`005-colaboradores.sql` reserva o passo na sequência; não contém INSERT.

---

## Federação (`001`)

| Campo | Valor |
|-------|-------|
| `NOM_FEDERACAO` | Unimed Ceará |
| `COD_UNIMED` | 979 |
| `FLG_ATIVO` | S |

```sql
SELECT COD_FEDERACAO, NOM_FEDERACAO, FLG_ATIVO
  FROM FEDERACAO
 WHERE NOM_FEDERACAO = 'Unimed Ceará'
   AND FLG_ATIVO = 'S';
```

`application.auth.default-federation-id` = `COD_FEDERACAO` (não `979`).

---

## Singulares (`002`)

Fonte: [Unimeds Filiadas](https://www.unimedceara.com.br/institucional/unimeds-filiadas/). `COD_UNIMED` da singular = registro ANS (dígitos).

| SIG_SINGULAR | NOM_SINGULAR | COD_UNIMED | No script |
|--------------|--------------|------------|-----------|
| CARIRI | Unimed do Cariri | 356123 | Sim |
| SOBRAL | Unimed de Sobral | 303178 | Sim |
| ARACATI | Unimed Regional de Aracati | 322717 | Sim |
| Demais filiadas | — | Pendente | Atualizar `002` + migration se pós-go-live |

---

## Colaboradores

Sem carga DML: login Zimbra (FT-AUTH). Vínculos organizacionais: API ou `migrations/` quando aplicável.

---

## Observações

- SSOT estrutural: [../baseline/oracle-baseline-2026-07-22.md](../baseline/oracle-baseline-2026-07-22.md) e [../ddl/](../ddl/README.md).
- Não usar `MERGE`, `INSERT … NOT EXISTS` nem reexecutar scripts DML em banco já carregado.
