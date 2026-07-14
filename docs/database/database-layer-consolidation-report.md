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
| `docs/database/migrations/V003__auth_sessao_and_colaborador_zimbra.sql` | Migração incremental idempotente pós-baseline |
| `docs/database/migrations/README.md` | Governança de evoluções pós-baseline |
| `docs/database/dml/README.md` | Documentação de cargas iniciais |
| `docs/database/database-layer-consolidation-report.md` | Este relatório |

## Artefatos atualizados

| Artefato | Alteração |
|----------|-----------|
| `docs/database/ddl/002-create-sequences.sql` | `SQ_AUTH_SESSAO_COD_SESSAO` |
| `docs/database/ddl/003-create-tables.sql` | Tabela `AUTH_SESSAO`; coluna `ID_ZIMBRA` em `COLABORADOR` |
| `docs/database/ddl/004-create-constraints.sql` | Constraints `AUTH_SESSAO`; `UK_COLABORADOR_ZIMBRA` |
| `docs/database/ddl/005-create-indexes.sql` | `IDX_AUTH_SESSAO_COLABORADOR`, `IDX_COLABORADOR_ZIMBRA` |
| `docs/database/ddl/006-create-comments.sql` | Comentários `AUTH_SESSAO` e `ID_ZIMBRA` |
| `docs/database/ddl/007-create-grants.sql` | Grants `AUTH_SESSAO` e sequence |
| `docs/database/ddl/900-drop-all.sql` | Drop `AUTH_SESSAO` |
| `docs/database/ddl/901-validation.sql` | Expectativas 21 tabelas / 21 sequences |
| `docs/database/model/03-physical-model.md` | v4.1 — entidade `AUTH_SESSAO`, `ID_ZIMBRA` |
| `docs/database/model/01-schema.md` | v3.1 — domínio Segurança |
| `docs/database/model/04-entity-catalog.md` | Catálogo oficial 21 entidades |
| `docs/database/model/05-decisions-and-risks.md` | DEC-DB-009, DEC-DB-010 |
| `docs/database/README.md` | Consolidação Sprint 1 documentada |

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
Greenfield:  ddl/001 → ddl/000-install.sql → ddl/901-validation.sql
Existente:   ddl/baseline anterior + migrations/V003__auth_sessao_and_colaborador_zimbra.sql
Carga dev:   ddl/008-initial-data.sql (federação e papéis — sem seed AUTH)
```

---

## Pendências

| # | Item | Severidade | Responsável sugerido |
|---|------|------------|----------------------|
| 1 | **Nomenclatura de sequences** — JPA alinhado ao padrão corporativo `SQ_<TABELA>_<CAMPO_PK>` | Resolvido | DEC-DB-017 |
| 2 | **Legado backend** — `backend/.../db/migration/` contém scripts obsoletos; remover com DEC-DB-019 | Média | backend |
| 3 | **`specs/architecture/authentication-architecture.md`** — modelo conceitual sem `ID_SESSAO` | Baixa | engenharia (specs) |
| 4 | **Seed dedicado FT-AUTH** — não necessário funcionalmente (login cria dados); documentado em `dml/README.md` | Informativa | — |

---

## Critério de conclusão

| Critério | Status |
|----------|--------|
| DDL representa estrutura oficial integral | ✅ 21 tabelas documentadas e scriptadas |
| Documentação consistente com DDL | ✅ Modelo físico v4.1 alinhado |
| Prontidão para próximas Features | ✅ Com pendências de alinhamento backend documentadas |

---

## Veredito

A camada de banco está **consolidada** para suportar as próximas Features. O baseline DDL em `docs/database/ddl/` é a fonte oficial da estrutura física (DEC-DB-019). Pendências de legado no backend documentadas — fora do escopo desta consolidação.
