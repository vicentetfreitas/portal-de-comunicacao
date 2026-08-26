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

Endpoint `GET /api/v1/pastas` retornando as pastas (com documentos aninhados) vinculadas à Área do Contexto Ativo do colaborador autenticado.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-001
- RF-DOCUMENTO-003 (parte de listagem — filtro por Área aplicado na query, não em memória)

### Casos de Uso Relacionados

- UC-DOCUMENTO-001
- UC-DOCUMENTO-003

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-001
- AT-DOCUMENTO-003 (cenário "listagem não vaza outras Áreas")

### Dependências

- Modelo de dados `PASTA`/`DOCUMENTO` proposto em `specification.md` § Modelo de Dados Proposto — **requer revisão de arquitetura/DBA antes do DDL** (mesmo padrão de `DEC-DB-027`); não é decisão desta Feature, é execução técnica pendente.
- `AREA` (entidade já existente, `FT-AREA`) para FK e resolução do Contexto Ativo (`FT-SESSION`).

### Componentes Esperados

- Entity (`Pasta`, `Documento`)
- Repository (filtro por `COD_AREA` do Contexto Ativo)
- Application Service
- DTO (`PastaResponse`, `DocumentoResponse` — `api.md`)
- Mapper
- Controller (`GET /api/v1/pastas`)
- Testes (unit + aceitação; cenário de coleção vazia; cenário de não-vazamento entre Áreas)

### Critérios de Conclusão

- RF-DOCUMENTO-001 implementado.
- AT-DOCUMENTO-001 e a parte aplicável de AT-DOCUMENTO-003 atendidos.
- Testes aprovados.
- Rastreabilidade íntegra.

---

## TK-DOCUMENTO-002 — Implementar download de arquivo (backend)

### Objetivo

Endpoint `GET /api/v1/documentos/{id}/download` retornando o binário via Object Storage (DEC-013), negando acesso a documento fora da Área do Contexto Ativo.

### Requisitos Funcionais Relacionados

- RF-DOCUMENTO-002
- RF-DOCUMENTO-003 (parte de download)

### Casos de Uso Relacionados

- UC-DOCUMENTO-002
- UC-DOCUMENTO-003

### Critérios de Aceitação Relacionados

- AT-DOCUMENTO-002
- AT-DOCUMENTO-003 (cenário "download negado")

### Dependências

- **Bloqueante de execução (não de spec):** provisionamento do Object Storage S3-compatible no ambiente (DEC-013 aprovada, execução pendente — ver `docs/governance/01-project-status.md`).
- `CHV_OBJETO_STORAGE` de `Documento` (TK-DOCUMENTO-001).

### Componentes Esperados

- Application Service (client do Object Storage — nunca expõe URL direta ao cliente, ADR-004)
- Controller (`GET /api/v1/documentos/{id}/download`, stream + `Content-Disposition`)
- Validação de Área antes do fetch ao storage (403 explícito — nunca 404 disfarçado)
- Testes (unit + aceitação; documento inexistente → 404; documento de outra Área → 403)

### Critérios de Conclusão

- RF-DOCUMENTO-002 implementado.
- AT-DOCUMENTO-002 e a parte aplicável de AT-DOCUMENTO-003 atendidos.
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
| TK-DOCUMENTO-001 | RF-DOCUMENTO-001, RF-DOCUMENTO-003 | UC-DOCUMENTO-001, UC-DOCUMENTO-003 | AT-DOCUMENTO-001, AT-DOCUMENTO-003 |
| TK-DOCUMENTO-002 | RF-DOCUMENTO-002, RF-DOCUMENTO-003 | UC-DOCUMENTO-002, UC-DOCUMENTO-003 | AT-DOCUMENTO-002, AT-DOCUMENTO-003 |
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
