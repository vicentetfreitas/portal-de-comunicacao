# Relatório de Conformidade — Nomenclatura Oracle Unimed Ceará

> **Histórico (pré-homologação 2026-07-22).** Contagens de sequences/FK/índices neste relatório **não** refletem a baseline física homologada. Fonte oficial: [../baseline/oracle-baseline-2026-07-22.md](../baseline/oracle-baseline-2026-07-22.md).

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Schema | UNMPORTCOM |
| Referência | Padrão para Nomenclatura de Banco de Dados Oracle (Unimed Ceará) |
| Decisão | DEC-DB-017 (revisão 2026-07-10) |
| Status | CONFORME |

---

## 1. Regra aplicada

Objetos Oracle que já estejam em conformidade com o padrão corporativo (`SQ_<TABELA>_<CAMPO>`) **não** devem ser renomeados apenas para utilizar abreviações.

O Glossário Oficial de Abreviações é utilizado **exclusivamente** quando o identificador ultrapassa **30 caracteres** (Oracle 11g).

---

## 2. Tabela de sequences (23)

| Sequence | Caracteres | Situação | Justificativa |
|----------|------------|----------|---------------|
| `SQ_FEDERACAO_COD_FEDERACAO` | 27 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_SINGULAR_COD_SINGULAR` | 25 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_ENDERECO_COD_ENDERECO` | 25 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_CONTATO_COD_CONTATO` | 23 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_AREA_COD_AREA` | 17 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_EQUIPE_COD_EQUIPE` | 21 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_COLABORADOR_COD_COLABORADOR` | 30 | **Restaurada** | Formato corporativo; exatamente 30 — abreviação `SQ_COLAB_COD_COLAB` revertida |
| `SQ_AUTH_SESSAO_COD_SESSAO` | 25 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_ONBOARD_SOLIC` | 16 | Abreviada por necessidade | Original `SQ_ONBOARDING_SOLICITACAO_COD_ONBOARDING_SOLICITACAO` (51) > 30 |
| `SQ_CAT_DOC_COD_CAT_DOC` | 22 | Abreviada por necessidade | Original `SQ_CATEGORIA_DOCUMENTAL_COD_CATEGORIA_DOCUMENTAL` (47) > 30 |
| `SQ_PASTA_COD_PASTA` | 19 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_DOCUMENTO_COD_DOCUMENTO` | 26 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_ARQ_BIN_COD_ARQ_BIN` | 22 | Abreviada por necessidade | Original `SQ_ARQUIVO_BINARIO_COD_ARQUIVO_BINARIO` (37) > 30 |
| `SQ_DOC_VERS_COD_DOC_VERS` | 24 | Abreviada por necessidade | Original `SQ_DOCUMENTO_VERSAO_COD_DOCUMENTO_VERSAO` (39) > 30 |
| `SQ_COMPART_COD_COMPART` | 22 | Abreviada por necessidade | Original `SQ_COMPARTILHAMENTO_COD_COMPARTILHAMENTO` (39) > 30 |
| `SQ_PAPEL_COD_PAPEL` | 19 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_PAPEL_ATRIB_COD_PAPEL_ATRIB` | 29 | Abreviada por necessidade | Original `SQ_PAPEL_ATRIBUICAO_COD_PAPEL_ATRIBUICAO` (41) > 30 |
| `SQ_PERM_PASTA_COD_PERM_PASTA` | 26 | Abreviada por necessidade | Original `SQ_PERMISSAO_PASTA_COD_PERMISSAO_PASTA` (38) > 30 |
| `SQ_SOLIC_PERM_COD_SOLIC_PERM` | 28 | Abreviada por necessidade | Original `SQ_SOLICITACAO_PERMISSAO_COD_SOLICITACAO_PERMISSAO` (49) > 30 |
| `SQ_REG_AUDIT_COD_REG_AUDIT` | 24 | Abreviada por necessidade | Original `SQ_REGISTRO_AUDITORIA_COD_REGISTRO_AUDITORIA` (43) > 30 |
| `SQ_COMUNICADO_COD_COMUNICADO` | 27 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_NOTIFICACAO_COD_NOTIFICACAO` | 29 | Mantida | Formato corporativo; ≤ 30 |
| `SQ_CONFIG_PORT_COD_CONFIG_PORT` | 28 | Abreviada por necessidade | Original `SQ_CONFIGURACAO_PORTAL_COD_CONFIGURACAO_PORTAL` (46) > 30 |

**Resumo:** 12 mantidas · 1 restaurada · 10 abreviadas por necessidade

---

## 3. Correção desta revisão

| Antes (incorreto) | Depois (correto) | Motivo |
|-------------------|------------------|--------|
| `SQ_COLAB_COD_COLAB` | `SQ_COLABORADOR_COD_COLABORADOR` | 30 caracteres — conforme ao padrão; abreviação desnecessária |

### Artefatos atualizados na revisão

- `database/ddl/002-create-sequences.sql`
- `database/ddl/007-create-grants.sql`
- `database/model/03-physical-model.md`
- `database/model/04-entity-catalog.md`
- `database/model/05-decisions-and-risks.md`
- `docs/implementation/06-database-standards.md`
- `backend/.../ColaboradorEntity.java`
- `backend/.../V2__access_control.sql`
- `tools/apply-oracle-renames.py`

---

## 4. Outros objetos (FK, UK, índices)

Permanecem abreviados **somente** quando o nome original excedia 30 caracteres. Nenhuma reversão necessária nesta revisação.

| Tipo | Quantidade | Todas ≤ 30 chars |
|------|------------|------------------|
| FK | 41 | Sim |
| UK | 15 | Sim |
| CK | 32 | Sim |
| Índices | 62 | Sim |

---

## 5. Objetos que ultrapassam 30 caracteres

**Nenhum** após as correções. Auditoria: `tools/audit-oracle-names.py`.

---

## 6. Confirmação final

| Critério | Status |
|----------|--------|
| Todos os objetos aderem ao padrão corporativo | ✓ |
| Nenhuma abreviação aplicada desnecessariamente | ✓ |
| Violações do limite de 30 caracteres corrigidas | ✓ |
| Glossário usado exclusivamente quando > 30 chars | ✓ |

---

## 7. Impactos

| Área | Impacto |
|------|---------|
| JPA `ColaboradorEntity` | `sequenceName` restaurado para `SQ_COLABORADOR_COD_COLABORADOR` |
| Legado `backend/.../V2__access_control.sql` | Sequence restaurada (obsoleto — DEC-DB-019) |
| Regras de negócio / tabelas / colunas | Sem alteração |
