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

### O que é esta pasta (não é Flyway)

**DEC-DB-019: o projeto não usa Flyway.** Não há runner automático, `flyway_schema_history`, nem versionamento aplicado pelo build. Esta pasta é:

- um **registro versionado e legível** do que mudou no schema depois do baseline homologado (`ddl/`), com o *porquê* de cada mudança;
- a **fonte de copiar-e-colar** para o DBA (ou quem tiver acesso) executar **manualmente** na IDE do banco (SQL Developer, DBeaver…).

Os nomes `V00X__` são só ordenação/rastreabilidade — não são lidos por nenhuma ferramenta. Cada script deve ser **SQL simples**, executável direto, com uma seção de conferência no fim. O baseline (`ddl/`) continua sendo a especificação greenfield; quando uma evolução aqui estabiliza, ela é incorporada ao `ddl/` numa próxima homologação.

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
| V009 | `V009__documento_upload_sequences_e_categorias.sql` | Brownfield FT-DOCUMENTO-UPLOAD (TK-DOC-UPLOAD-001). SQL simples: cria `SQ_ARQUIVO_BINARIO` e `SQ_DOCUMENTO_VERSAO` (ausentes de `002`) + `GRANT SELECT` p/ `UNMPORTCOM_APP_ROLE` (DEC-DB-024) + 4 linhas em `CATEGORIA_DOCUMENTAL` — taxonomia por tipo de mídia `Documentos`/`Imagens`/`Vídeos`/`Outros`, IDs explícitos (DEC-CMS-002). Pré-check: `VAL-DB-03-verify-documento-upload-prereqs.sql`. **Executado e validado 2026-08-27.** |
| V010 | `V010__pasta_permissao_pasta_sequences.sql` | Brownfield FT-DOCUMENTO-GESTAO (TK-DOC-GESTAO-001). SQL simples: cria `SQ_PASTA` e `SQ_PERMISSAO_PASTA` (ausentes de `002` — verificado via JDBC) + `GRANT SELECT` p/ `UNMPORTCOM_APP_ROLE` (DEC-DB-024). `START WITH 1` — as tabelas `PASTA`/`PERMISSAO_PASTA` existem, o app já tem DML e estão com 0 linhas no TST. Sem alteração de tabela. Pré-check: `VAL-DB-04-verify-pasta-write-prereqs.sql`. **Executado e validado 2026-08-27.** |
| V011 | `V011__homologacao_admin_area_e_pasta_teste.sql` | **Dados de homologação** (não baseline greenfield). SQL simples: catálogo `PAPEL` (4 linhas, IDs 1–4 — tabela vazia no TST, `SQ_PAPEL*` não existe); `PAPEL_ATRIBUICAO` do colaborador `1335` (vicentefreitas@unimedceara.com.br) como `ADMINISTRADOR` da Área 1 (TI); pasta-raiz da Área 1 + grants `LEITURA`/`DOWNLOAD`/`EDICAO` (`AREA/1`); 1 documento de exemplo. Pré-check: `VAL-DB-05-verify-homologacao-admin-prereqs.sql`. **Execução pendente.** |

Evoluções futuras: novos scripts SQL simples nesta pasta.

### Reconciliação greenfield pendente (baseline `ddl/`)

`V009` cobre o ambiente **atual** (brownfield). Para `000-install.sql` (greenfield), o baseline ainda precisa ser reconciliado — quando essa homologação acontecer:

| Arquivo | Ajuste |
|---------|--------|
| `ddl/002-create-sequences.sql` | adicionar `SQ_ARQUIVO_BINARIO`, `SQ_DOCUMENTO_VERSAO` (V009), `SQ_PASTA`, `SQ_PERMISSAO_PASTA` (V010) — contagem 12 → 16 |
| `ddl/007-create-grants.sql`, `security/V902__application_user_sequences.sql` | `GRANT SELECT` das quatro sequences novas |
| `ddl/008-initial-data.sql` | trocar o `MERGE` de `CATEGORIA_DOCUMENTAL` (5 categorias históricas, e ainda referencia `SQ_CAT_DOC_COD_CAT_DOC` inexistente) pelas 4 de mídia — DEC-CMS-002. Se optar por manter a sequence, criá-la em `002`. `PAPEL` também usa `SQ_PAPEL_COD_PAPEL` (inexistente) — V011 semeou o catálogo com IDs explícitos para homologação; reconciliar no greenfield. |

Enquanto isso, `000-install.sql` **não** suporta upload de documentos (V009) nem
gestão de pastas (V010).

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
