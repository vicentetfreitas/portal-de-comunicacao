# Catálogo de Entidades — UNMPORTCOM

| Item | Valor |
|------|-------|
| Schema | UNMPORTCOM |
| Modelo físico | `model/03-physical-model.md` v4.7 |
| Modelo lógico | `model/02-logical-model.md` v1.5 |
| Modelo conceitual | `model/02-conceptual-model.md` v1.5 |
| DDL | `ddl/` |
| Última atualização | 2026-07-10 |

---

## Organização Corporativa

| Tabela | Sequence | Entidade lógica | Descrição |
|--------|----------|-----------------|-----------|
| FEDERACAO | SQ_FEDERACAO_COD_FEDERACAO | FEDERACAO | Federação administradora |
| SINGULAR | SQ_SINGULAR_COD_SINGULAR | SINGULAR | Singulares |
| ENDERECO | SQ_ENDERECO_COD_ENDERECO | ENDERECO | Endereços da federação ou singular |
| CONTATO | SQ_CONTATO_COD_CONTATO | CONTATO | Canais de comunicação (org. ou colaborador) |
| AREA | SQ_AREA_COD_AREA | AREA | Áreas organizacionais |
| EQUIPE | SQ_EQUIPE_COD_EQUIPE | EQUIPE | Equipes |
| COLABORADOR | SQ_COLABORADOR_COD_COLABORADOR | COLABORADOR | Perfil do colaborador autenticado |
| ONBOARDING_SOLICITACAO | SQ_ONBOARD_SOLIC | ONBOARDING_SOLICITACAO | Solicitações de cadastro |

## Gestão Documental

| Tabela | Sequence | Entidade lógica | Descrição |
|--------|----------|-----------------|-----------|
| CATEGORIA_DOCUMENTAL | SQ_CAT_DOC_COD_CAT_DOC | CATEGORIA_DOCUMENTAL | Categorias |
| PASTA | SQ_PASTA_COD_PASTA | PASTA | Pastas hierárquicas |
| DOCUMENTO | SQ_DOCUMENTO_COD_DOCUMENTO | DOCUMENTO | Metadados de documentos |
| ARQUIVO_BINARIO | SQ_ARQ_BIN_COD_ARQ_BIN | ARQUIVO_BINARIO | Arquivos externos |
| DOCUMENTO_VERSAO | SQ_DOC_VERS_COD_DOC_VERS | DOCUMENTO_VERSAO | Versionamento |
| COMPARTILHAMENTO | SQ_COMPART_COD_COMPART | COMPARTILHAMENTO | Compartilhamento polimórfico |

## Controle de Acesso

| Tabela | Sequence | Entidade lógica | Descrição |
|--------|----------|-----------------|-----------|
| **AUTH_SESSAO** | **SQ_AUTH_SESSAO_COD_SESSAO** | **AUTH_SESSAO** | **Sessões FT-AUTH (Refresh Token)** |
| PAPEL | SQ_PAPEL_COD_PAPEL | PAPEL | Perfis de acesso |
| PAPEL_ATRIBUICAO | SQ_PAPEL_ATRIB_COD_PAPEL_ATRIB | PAPEL_ATRIBUICAO | Atribuição de papéis |
| PERMISSAO_PASTA | SQ_PERM_PASTA_COD_PERM_PASTA | PERMISSAO_PASTA | Permissões em pastas |
| SOLICITACAO_PERMISSAO | SQ_SOLIC_PERM_COD_SOLIC_PERM | SOLICITACAO_PERMISSAO | Solicitações de acesso |
| REGISTRO_AUDITORIA | SQ_REG_AUDIT_COD_REG_AUDIT | REGISTRO_AUDITORIA | Auditoria corporativa |

## Comunicação

| Tabela | Sequence | Entidade lógica | Descrição |
|--------|----------|-----------------|-----------|
| COMUNICADO | SQ_COMUNICADO_COD_COMUNICADO | COMUNICADO | Comunicados |
| NOTIFICACAO | SQ_NOTIFICACAO_COD_NOTIFICACAO | NOTIFICACAO | Notificações |

## Configuração

| Tabela | Sequence | Entidade lógica | Descrição |
|--------|----------|-----------------|-----------|
| CONFIGURACAO_PORTAL | SQ_CONFIG_PORT_COD_CONFIG_PORT | CONFIGURACAO_PORTAL | Parâmetros por federação |

---

**Total:** 23 tabelas · 23 sequences

Detalhamento físico: `model/03-physical-model.md`.  
Modelo lógico: `model/02-logical-model.md`.  
Modelo conceitual: `model/02-conceptual-model.md`.
