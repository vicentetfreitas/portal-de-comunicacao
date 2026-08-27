# Migrations — Evolução Estrutural (DBA)

| Item | Valor |
|------|-------|
| Schema | UNMPORTCOM |
| Baseline | `database/ddl/` + `database/baseline/oracle-baseline-2026-07-22.md` |
| Status | Referência brownfield |

---

## Objetivo

Registrar scripts de evolução estrutural **após** o baseline DDL corporativo.

O schema Oracle é administrado pelo **DBA** através do baseline DDL oficial do projeto. A aplicação pressupõe schema previamente criado.

Instalações **greenfield** devem utilizar exclusivamente `ddl/000-install.sql`.

---

## Baseline oficial (homologada 2026-07-22)

```text
database/ddl/000-install.sql
database/ddl/001-create-users.sql
…
database/ddl/901-validation.sql
```

Instalações **greenfield** após homologação: **somente** `000-install.sql`. Não aplicar V003/V004 se o baseline completo já estiver instalado.

---

## Scripts históricos (pré-homologação brownfield)

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V003 | `V003__auth_sessao_and_colaborador_zimbra.sql` | Evolução incremental legada — **desnecessário** se `000-install.sql` já aplicado |
| V004 | `V004__colaborador_corporate_columns.sql` | Alinha `COLABORADOR` brownfield ao baseline (DEC-DB-020) — **não** cria colunas novas no domínio; replica o que já está em `ddl/003-create-tables.sql` |
| V006 | `V006__drop_auth_sessao_organizational_context.sql` | Remove `COD_*_CTX` de `AUTH_SESSAO` (REF-DB-CTX-01) em ambientes que aplicaram evolução com colunas de contexto na sessão |
| V007 | `V007__colaborador_ssot_alignment.sql` | Remove `DES_CARGO`/`NUM_CPF`, alinha `NOM_COLABORADOR`, `ID_ZIMBRA` e `DES_BIOGRAFIA` ao SSOT FT-COLABORADOR |
| V008 | `V008__singular_email_domain.sql` | Brownfield: `SINGULAR.DES_DOMINIO_EMAIL` + `UK_SINGULAR_DOMINIO_EMAIL` + domínios Ceará/Cariri (GAP-028-04 / DEC-ORG-003 / DH-PA-02). **Não** aplicar após `000-install` (coluna/UK já no DDL). Execução DBA pendente no Oracle atual. |
| V009 | `V009__documento_upload_sequences_e_categorias.sql` | Brownfield FT-DOCUMENTO-UPLOAD (TK-DOC-UPLOAD-001): cria `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_VERSAO`, `SQ_CAT_DOC_COD_CAT_DOC` (ausentes de `002` — a 3ª já era referenciada por `008` e nunca existiu) + `GRANT SELECT` p/ `UNMPORTCOM_APP_ROLE` (DEC-DB-024) + taxonomia de `CATEGORIA_DOCUMENTAL` por tipo de mídia `Documentos`/`Imagens`/`Vídeos`/`Outros` (DEC-CMS-002); desativa a taxonomia histórica se presente. Idempotente. **Execução DBA pendente.** |

Evoluções futuras: novos scripts versionados nesta pasta, executados pelo DBA.

### Reconciliação greenfield pendente de V009 (para o DBA)

`V009` é brownfield. Para instalações **greenfield** (`000-install.sql`), o baseline precisa ser reconciliado com DEC-CMS-002 e com o gap de sequences:

| Arquivo | Ajuste necessário |
|---------|-------------------|
| `ddl/002-create-sequences.sql` | adicionar `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_VERSAO`, `SQ_CAT_DOC_COD_CAT_DOC` (contagem esperada passa de 12 para 15) |
| `ddl/007-create-grants.sql` e `security/V902__application_user_sequences.sql` | `GRANT SELECT` das 3 novas sequences para `UNMPORTCOM_APP_ROLE` |
| `ddl/008-initial-data.sql` | trocar o `MERGE` de `CATEGORIA_DOCUMENTAL` (5 categorias históricas) pelas 4 de mídia (`Documentos`/`Imagens`/`Vídeos`/`Outros`) — DEC-CMS-002 |

Enquanto essa reconciliação não ocorrer, o baseline greenfield **não** suporta upload de documentos (as tabelas `ARQUIVO_BINARIO`/`DOCUMENTO_VERSAO` existem mas não têm sequence, e `008` referencia sequence inexistente).

### VAL-DB-02 — Verificação antes de aplicar V004

Execute como `UNMPORTCOM`:

```sql
-- migrations/VAL-DB-02-verify-colaborador-columns.sql
SELECT column_name, nullable
  FROM user_tab_columns
 WHERE table_name = 'COLABORADOR'
   AND column_name IN (
     'COD_SINGULAR', 'COD_AREA', 'COD_EQUIPE', 'COD_GESTOR',
     'DES_BIOGRAFIA', 'DAT_NASCIMENTO', 'DAT_CONTRATACAO', 'ID_ZIMBRA', 'NOM_COLABORADOR'
   )
 ORDER BY column_name;
```

Se faltar qualquer coluna do baseline, aplicar `V004` (brownfield) ou reinstalar via `000-install.sql` (greenfield).

---

## Ordem de execução

```text
Greenfield:
  ddl/001-create-users.sql (SYS/DBA)
  ddl/000-install.sql      (UNMPORTCOM)
  ddl/901-validation.sql

Evolução pós-baseline:
  migrations/V00X__<descricao>.sql (DBA)
```

---

## Referência

DEC-DB-019 — Flyway não é utilizado. Baseline física: [../baseline/oracle-baseline-2026-07-22.md](../baseline/oracle-baseline-2026-07-22.md).

DEC-DB-020 — Modelo `COLABORADOR` (JPA ↔ DDL); V004 sincroniza Oracle incompleto ao baseline, não redefine domínio.

Governança: [../GOVERNANCE.md](../GOVERNANCE.md).
