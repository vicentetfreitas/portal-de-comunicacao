# Relatório — Consolidação da Camada de Banco

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Schema | UNMPORTCOM |
| Data | 2026-07-10 |
| Escopo | DDL oficial · modelo físico · documentação |
| Veredito | **Consolidado** (com pendências documentadas) |

---

## Objetivo

Consolidar a camada de banco de dados antes da próxima Sprint, incorporando estruturas da **FT-AUTH** (`AUTH_SESSAO`, `ID_ZIMBRA`) ao baseline oficial e alinhando a documentação.

---

## Artefatos criados

| Artefato | Descrição |
|----------|-----------|
| `database/migrations/V003__auth_sessao_and_colaborador_zimbra.sql` | Migração incremental idempotente pós-baseline |
| `database/migrations/README.md` | Governança de evoluções pós-baseline |
| `database/dml/README.md` | Documentação de cargas iniciais |
| `database/reports/report-database-layer-consolidation.md` | Este relatório |

## Artefatos atualizados

| Artefato | Alteração |
|----------|-----------|
| `database/ddl/002-create-sequences.sql` | `SQ_AUTH_SESSAO_COD_SESSAO` |
| `database/ddl/003-create-tables.sql` | Tabela `AUTH_SESSAO`; coluna `ID_ZIMBRA` em `COLABORADOR` |
| `database/ddl/004-create-constraints.sql` | Constraints `AUTH_SESSAO`; `UK_COLABORADOR_ZIMBRA` |
| `database/ddl/005-create-indexes.sql` | `IDX_AUTH_SESSAO_COLABORADOR`, `IDX_COLABORADOR_ZIMBRA` |
| `database/ddl/006-create-comments.sql` | Comentários `AUTH_SESSAO` e `ID_ZIMBRA` |
| `database/ddl/007-create-grants.sql` | Grants `AUTH_SESSAO` e sequence |
| `database/ddl/900-drop-all.sql` | Drop `AUTH_SESSAO` |
| `database/ddl/901-validation.sql` | Expectativas 21 tabelas / 21 sequences |
| `database/model/03-physical-model.md` | v4.1 — entidade `AUTH_SESSAO`, `ID_ZIMBRA` |
| `database/model/01-schema.md` | v3.1 — domínio Segurança |
| `database/model/04-entity-catalog.md` | Catálogo oficial 21 entidades |
| `database/model/05-decisions-and-risks.md` | DEC-DB-009, DEC-DB-010 |
| `database/README.md` | Consolidação Sprint 1 documentada |

---

## Estrutura consolidada

### Schema UNMPORTCOM — 21 tabelas

| Domínio | Tabelas |
|---------|---------|
| Organização Corporativa | FEDERACAO, SINGULAR, AREA, EQUIPE, COLABORADOR, ONBOARDING_SOLICITACAO |
| Gestão Documental | CATEGORIA_DOCUMENTAL, PASTA, DOCUMENTO, ARQUIVO_BINARIO, DOCUMENTO_VERSAO, COMPARTILHAMENTO |
| Controle de Acesso | **AUTH_SESSAO**, PAPEL, PAPEL_ATRIBUICAO, PERMISSAO_PASTA, SOLICITACAO_PERMISSAO, REGISTRO_AUDITORIA |
| Comunicação | COMUNICADO, NOTIFICACAO |
| Configuração | CONFIGURACAO_PORTAL |

### Novidades FT-AUTH (consolidadas no baseline)

**COLABORADOR** — coluna adicional:

| Coluna | Tipo | Restrição |
|--------|------|-----------|
| ID_ZIMBRA | VARCHAR2(255) | UK (nullable — onboarding sem Zimbra) |

**AUTH_SESSAO** — nova tabela:

| Coluna | Função |
|--------|--------|
| COD_SESSAO | PK interna |
| ID_SESSAO | session_id público |
| COD_COLABORADOR | FK colaborador |
| HASH_REFRESH_TOKEN | Hash SHA-256 do Refresh Token |
| DES_DISPOSITIVO | Dispositivo |
| FLG_REMEMBER_ME | Lembrar-me S/N |
| DAT_CRIACAO / DAT_EXPIRACAO | Vigência |
| FLG_REVOGADA / DAT_REVOGACAO | Revogação |

### Instalação

```text
Greenfield:  ddl/001 → ddl/000-install.sql (008 → dml/001 → 009-config) → ddl/901-validation.sql
Existente:   ddl/baseline anterior + migrations/V003__auth_sessao_and_colaborador_zimbra.sql
Institucional: dml/001-federacao.sql (install) → dml/002–004 (fase aprovada); colaborador via login Zimbra
```

---

## Pendências

| # | Item | Severidade | Responsável sugerido |
|---|------|------------|----------------------|
| 1 | **Nomenclatura de sequences** — JPA alinhado ao padrão corporativo `SQ_<TABELA>_<CAMPO_PK>` | Resolvido | DEC-DB-017 |
| 2 | **Legado backend** — `backend/.../db/migration/` contém scripts obsoletos; remover com DEC-DB-019 | Média | backend |
| 3 | **`specs/architecture/authentication-architecture.md`** — modelo conceitual sem `ID_SESSAO` | Baixa | engenharia (specs) |
| 4 | **Carga FT-AUTH** — colaborador/sessão via login Zimbra; ver `dml/README.md` | Informativa | — |

---

## Critério de conclusão

| Critério | Status |
|----------|--------|
| DDL representa estrutura oficial integral | ✅ 21 tabelas documentadas e scriptadas |
| Documentação consistente com DDL | ✅ Modelo físico v4.1 alinhado |
| Prontidão para próximas Features | ✅ Com pendências de alinhamento backend documentadas |

---

## Veredito

A camada de banco está **consolidada** para suportar as próximas Features. O baseline DDL em `database/ddl/` é a fonte oficial da estrutura física (DEC-DB-019). Pendências de legado no backend documentadas — fora do escopo desta consolidação.
