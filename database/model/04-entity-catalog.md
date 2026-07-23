# Catálogo de Entidades — UNMPORTCOM

| Item | Valor |
|------|-------|
| Schema | UNMPORTCOM |
| Modelo físico | `model/03-physical-model.md` v4.7 |
| Modelo lógico | `model/02-logical-model.md` v1.5 |
| Modelo conceitual | `model/02-conceptual-model.md` v1.5 |
| DDL | `ddl/` |
| Baseline | `baseline/oracle-baseline-2026-07-22.md` |
| Última atualização | 2026-07-22 (DB-SYNC-01) |

---

## Organização Corporativa

| Tabela | Sequence (homologada) | Entidade lógica | Descrição |
|--------|------------------------|-----------------|-----------|
| FEDERACAO | SQ_FEDERACAO_COD_FEDERACAO | FEDERACAO | Federação administradora |
| SINGULAR | SQ_SINGULAR_COD_SINGULAR | SINGULAR | Singulares |
| ENDERECO | — | ENDERECO | Endereços da federação ou singular |
| CONTATO | — | CONTATO | Canais de comunicação (org. ou colaborador) |
| AREA | SQ_AREA_COD_AREA | AREA | Áreas organizacionais (nível único; DEC-DB-022) |
| EQUIPE | SQ_EQUIPE_COD_EQUIPE | EQUIPE | Equipes |
| COLABORADOR | SQ_COLABORADOR | COLABORADOR | Perfil do colaborador autenticado |
| ONBOARDING_SOLICITACAO | SQ_ONBOARD_SOLIC | ONBOARDING_SOLICITACAO | Solicitações de cadastro |

## Gestão Documental

| Tabela | Sequence (homologada) | Entidade lógica | Descrição |
|--------|------------------------|-----------------|-----------|
| CATEGORIA_DOCUMENTAL | — | CATEGORIA_DOCUMENTAL | Categorias |
| PASTA | — | PASTA | Pastas hierárquicas |
| DOCUMENTO | SQ_DOCUMENTO_COD_DOCUMENTO | DOCUMENTO | Metadados de documentos |
| ARQUIVO_BINARIO | — | ARQUIVO_BINARIO | Arquivos externos |
| DOCUMENTO_VERSAO | — | DOCUMENTO_VERSAO | Versionamento |
| COMPARTILHAMENTO | — | COMPARTILHAMENTO | Compartilhamento polimórfico |

## Controle de Acesso

| Tabela | Sequence (homologada) | Entidade lógica | Descrição |
|--------|------------------------|-----------------|-----------|
| AUTH_SESSAO | SQ_AUTH_SESSAO | AUTH_SESSAO | Sessões FT-AUTH (Refresh Token) |
| PAPEL | — | PAPEL | Perfis de acesso |
| PAPEL_ATRIBUICAO | — | PAPEL_ATRIBUICAO | Atribuição de papéis |
| PERMISSAO_PASTA | — | PERMISSAO_PASTA | Permissões em pastas |
| SOLICITACAO_PERMISSAO | — | SOLICITACAO_PERMISSAO | Solicitações de acesso |
| REGISTRO_AUDITORIA | SQ_REG_AUDIT_COD_REG_AUDIT | REGISTRO_AUDITORIA | Auditoria corporativa |

## Comunicação

| Tabela | Sequence (homologada) | Entidade lógica | Descrição |
|--------|------------------------|-----------------|-----------|
| COMUNICADO | SQ_COMUNICADO_COD_COMUNICADO | COMUNICADO | Comunicados |
| NOTIFICACAO | SQ_NOTIFICACAO_COD_NOTIFICACAO | NOTIFICACAO | Notificações |

## Configuração

| Tabela | Sequence (homologada) | Entidade lógica | Descrição |
|--------|------------------------|-----------------|-----------|
| CONFIGURACAO_PORTAL | SQ_CONFIG_PORT_COD_CONFIG_PORT | CONFIGURACAO_PORTAL | Parâmetros por federação |

---

**Total:** 23 tabelas · **12 sequences** homologadas (baseline 2026-07-22).

Ausência de sequence para uma tabela **não** indica inconsistência estrutural — ver baseline § Sequences.

Detalhamento físico: `model/03-physical-model.md`.  
Modelo lógico: `model/02-logical-model.md`.  
Modelo conceitual: `model/02-conceptual-model.md`.
