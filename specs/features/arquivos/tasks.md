# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — somente leitura, backend novo por inteiro) |
| Versão | 1.1 |
| Status | APPROVED |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-DOCUMENTO |
| Feature | Arquivos e Documentos |
| Domínio | DOCUMENTO |

---

# Objetivo

Decomposição funcional de FT-DOCUMENTO em unidades de implementação. Não representa planejamento de construção/cronograma — responsabilidade de `construction/`.

**Natureza da Feature:** backend **novo por inteiro** (sem Feature existente para reaproveitar, ao contrário de `FT-AREA-COLABORADOR`) + frontend novo. Sem Tarefas Base de Cadastro/Atualização/Alteração de Status — Feature somente leitura (`specification.md` § Escopo, decisão de produto 2026-08-26).

---

## TK-DOCUMENTO-001 — Implementar listagem de pastas e arquivos (backend)

### Objetivo

Endpoint `GET /api/v1/pastas` retornando as pastas (com documentos `ATIVO`/`ARQUIVADO` aninhados) para as quais existe `PERMISSAO_PASTA` (`TIP_ACESSO=LEITURA`) compatível com o Contexto Ativo do colaborador autenticado.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-001
- RF-DOCUMENTO-003 (filtro de permissão aplicado na query, não em memória)
- RF-DOCUMENTO-004 (filtro `STA_DOCUMENTO != EXPIRADO` aplicado na query)

### Casos de Uso Relacionados

- UC-DOCUMENTO-001
- UC-DOCUMENTO-003
- UC-DOCUMENTO-004

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-001
- AT-DOCUMENTO-003 (cenários de permissão multi-nível)
- AT-DOCUMENTO-004 (cenário "expirado não aparece na listagem")

### Dependências

- **Tabelas já instaladas** (`database/ddl/003-create-tables.sql`, baseline DBA — DEC-DB-019): `PASTA`, `DOCUMENTO`, `DOCUMENTO_VERSAO`, `ARQUIVO_BINARIO`, `PERMISSAO_PASTA`. Nenhuma migration nova necessária para esta task.
- `JwtAuthenticatedPrincipal` (`federationId`/`singularId`/`areaId`/`teamId`) já existente (`accesscontrol/domain/model/`) para resolução do Contexto Ativo.

### Componentes Esperados

- Entity (`Pasta`, `Documento`, `DocumentoVersao`, `ArquivoBinario`, `PermissaoPasta`)
- Repository (query de `PASTA` com `EXISTS` em `PERMISSAO_PASTA` filtrando por `TIP_DESTINATARIO`/`COD_DESTINATARIO` compatível com o Contexto Ativo e `TIP_ACESSO=LEITURA`; documentos filtrados por `STA_DOCUMENTO != EXPIRADO`)
- Application Service
- DTO (`PastaResponse`, `DocumentoResponse` — `api.md`)
- Mapper (inclui resolução do `ARQUIVO_BINARIO` da versão atual para `formato`/`tamanhoBytes`)
- Controller (`GET /api/v1/pastas`)
- Testes (unit + aceitação; coleção vazia; grant por Federação/Singular/Área/Equipe; expirado oculto)

### Critérios de Conclusão

- RF-DOCUMENTO-001 implementado.
- AT-DOCUMENTO-001 e as partes aplicáveis de AT-DOCUMENTO-003/004 atendidos.
- Testes aprovados.
- Rastreabilidade íntegra.

---

## TK-DOCUMENTO-002 — Implementar download de arquivo (backend)

### Objetivo

Endpoint `GET /api/v1/documentos/{id}/download` retornando o binário da versão atual via Object Storage (DEC-013), negando acesso quando não há `PERMISSAO_PASTA` (`TIP_ACESSO=DOWNLOAD`) compatível com o Contexto Ativo, e ocultando documentos `EXPIRADO`.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-002
- RF-DOCUMENTO-003 (permissão de download)
- RF-DOCUMENTO-004 (404 para `EXPIRADO`)

### Casos de Uso Relacionados

- UC-DOCUMENTO-002
- UC-DOCUMENTO-003
- UC-DOCUMENTO-004

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-002
- AT-DOCUMENTO-003 (cenário "download negado")
- AT-DOCUMENTO-004 (cenários "download de expirado" e "arquivado permanece visível")

### Dependências

- **Bloqueante de execução (não de spec):** provisionamento do Object Storage S3-compatível no ambiente (DEC-013 aprovada, execução pendente — ver `docs/governance/01-project-status.md`) **e** adição de cliente S3/MinIO ao `backend/pom.xml` (ausente hoje — confirmado por exploração de convenções; AWS SDK v2 é compatível com MinIO, não requer SDK proprietário).
- `TK-DOCUMENTO-001` para as entidades `DocumentoVersao`/`ArquivoBinario`/`PermissaoPasta`.

### Componentes Esperados

- Application Service (client do Object Storage via `ARQUIVO_BINARIO.URL_ARQUIVO` da versão com `FLG_VERSAO_ATUAL='S'` — nunca expõe a URL diretamente ao cliente, ADR-004)
- Controller (`GET /api/v1/documentos/{id}/download`, stream + `Content-Disposition`)
- Validação de `PERMISSAO_PASTA` (`TIP_ACESSO=DOWNLOAD`) e de `STA_DOCUMENTO != EXPIRADO` antes do fetch ao storage (403/404 explícitos — nunca disfarçados)
- Testes (unit + aceitação; documento inexistente → 404; expirado → 404; sem grant compatível → 403)

### Critérios de Conclusão

- RF-DOCUMENTO-002 implementado.
- AT-DOCUMENTO-002 e as partes aplicáveis de AT-DOCUMENTO-003/004 atendidos.
- Testes aprovados.
- Rastreabilidade íntegra.

---

## TK-DOCUMENTO-003 — Implementar página de Arquivos e Documentos (frontend)

### Objetivo

Página consumindo os dois endpoints acima: lista pastas/arquivos da Área e permite baixar um arquivo.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-001
- RF-DOCUMENTO-002

### Casos de Uso Relacionados

- UC-DOCUMENTO-001
- UC-DOCUMENTO-002

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-001
- AT-DOCUMENTO-002

### Dependências

- TK-DOCUMENTO-001 e TK-DOCUMENTO-002 (endpoints devem existir antes do consumo).
- Rota já referenciada por `FT-AREA-COLABORADOR` (`TK-AREA-COLAB-001`, atalho "Arquivos e Documentos") — esta tarefa a torna funcional, não cria a entrada de navegação (já existe).

### Componentes Esperados

- Página/rota (`pages/`, `router/routes/`)
- Service client (`services/documento/`)
- Composable de leitura (listagem + acionamento de download)
- Tratamento de estado vazio (Área sem pastas), erro de comunicação e 403 (mensagem, não crash)
- Componente DS para listagem (pastas com itens de arquivo)
- Testes (unit de composable/página; cenário vazio; cenário de erro; cenário 403)

### Critérios de Conclusão

- RF-DOCUMENTO-001 e RF-DOCUMENTO-002 consumidos na UI.
- AT-DOCUMENTO-001 e AT-DOCUMENTO-002 atendidos do ponto de vista de frontend.
- Testes aprovados.
- Rastreabilidade íntegra.

---

# Matriz de Rastreabilidade

| Task | RF | UC | AT |
|------|----|----|----|
| TK-DOCUMENTO-001 | RF-DOCUMENTO-001, RF-DOCUMENTO-003, RF-DOCUMENTO-004 | UC-DOCUMENTO-001, UC-DOCUMENTO-003, UC-DOCUMENTO-004 | AT-DOCUMENTO-001, AT-DOCUMENTO-003, AT-DOCUMENTO-004 |
| TK-DOCUMENTO-002 | RF-DOCUMENTO-002, RF-DOCUMENTO-003, RF-DOCUMENTO-004 | UC-DOCUMENTO-002, UC-DOCUMENTO-003, UC-DOCUMENTO-004 | AT-DOCUMENTO-002, AT-DOCUMENTO-003, AT-DOCUMENTO-004 |
| TK-DOCUMENTO-003 | RF-DOCUMENTO-001, RF-DOCUMENTO-002 | UC-DOCUMENTO-001, UC-DOCUMENTO-002 | AT-DOCUMENTO-001, AT-DOCUMENTO-002 |

---

# Critérios de Conformidade

Este documento é considerado conforme quando:

- todas as tarefas estiverem associadas a pelo menos um requisito funcional, um caso de uso e um critério de aceitação;
- não representar planejamento da construção;
- manter consistência com `specification.md`, `use-cases.md`, `api.md`, `acceptance-tests.md` e `traceability.md`.

---

# Responsabilidades

## specs/

Define **o que** deverá ser implementado.

## construction/

Define **como**, **quando**, **por quem** e **em qual ordem** — fora do escopo deste documento.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.1 | 2026-08-26 | Claude Code (Specify) | Criação — 3 tasks (2 backend, 1 frontend) para DoR-Implementation |
| 1.2 | 2026-08-26 | Claude Code (Specify) | Reconciliação com schema físico real (PERMISSAO_PASTA multi-nível, DOCUMENTO_VERSAO/ARQUIVO_BINARIO, filtro STA_DOCUMENTO); dependência de cliente S3/MinIO explicitada em TK-002 |
