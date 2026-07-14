# Feature Tasks

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature |
| Versão | 1.1 |
| Status | STABLE |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | ${FEATURE_ID} |
| Feature | ${FEATURE_NAME} |
| Domínio | ${DOMAIN} |

---

# Objetivo

Este documento descreve a decomposição funcional da Feature em unidades de implementação.

Cada tarefa representa a implementação de um ou mais requisitos funcionais.

Este documento não representa planejamento da construção, cronograma ou organização dos PKGs.

Essas responsabilidades pertencem exclusivamente à camada `construction/`.

---

# Convenções

As tarefas deverão utilizar a identificação:

```text
TK-${DOMAIN}-001
TK-${DOMAIN}-002
TK-${DOMAIN}-003
...
```

Cada tarefa deverá possuir rastreabilidade completa com:

- Requisitos Funcionais (RF)
- Casos de Uso (UC)
- Critérios de Aceitação (AT)

---

# Template

## TK-${DOMAIN}-XXX — <Título>

### Objetivo

Descrever claramente a funcionalidade entregue por esta tarefa.

---

### Requisitos Funcionais Relacionados

- RF-${DOMAIN}-XXX

---

### Casos de Uso Relacionados

- UC-${DOMAIN}-XXX

---

### Critérios de Aceitação Relacionados

- AT-${DOMAIN}-XXX

---

### Dependências

Relacionar apenas dependências específicas desta tarefa.

---

### Componentes Esperados

Documentar os principais componentes necessários para implementar a funcionalidade.

Exemplo:

- Entity
- Repository
- Domain Service
- Application Service
- DTO
- Mapper
- Controller
- Testes

A lista deverá conter apenas os componentes necessários para atender ao requisito.

---

### Critérios de Conclusão

A tarefa será considerada concluída quando:

- o requisito funcional estiver implementado;
- os critérios de aceitação estiverem atendidos;
- os testes estiverem aprovados;
- a rastreabilidade permanecer íntegra.

---

# Tarefas Base do CRUD

## TK-${DOMAIN}-001 — Implementar Cadastro

Relacionada a:

- RF-${DOMAIN}-001
- UC-${DOMAIN}-001
- AT-${DOMAIN}-001

Componentes normalmente envolvidos:

- Entity
- Repository
- Domain Service
- Application Service
- DTO
- Mapper
- Controller
- Testes

---

## TK-${DOMAIN}-002 — Implementar Consulta por Identificador

Relacionada a:

- RF-${DOMAIN}-002
- UC-${DOMAIN}-002
- AT-${DOMAIN}-002

Componentes normalmente envolvidos:

- Repository
- Application Service
- DTO
- Mapper
- Controller
- Testes

---

## TK-${DOMAIN}-003 — Implementar Listagem

Relacionada a:

- RF-${DOMAIN}-003
- UC-${DOMAIN}-003
- AT-${DOMAIN}-003

Componentes normalmente envolvidos:

- Repository
- Specification / Query
- Application Service
- Controller
- Testes

---

## TK-${DOMAIN}-004 — Implementar Atualização

Relacionada a:

- RF-${DOMAIN}-004
- UC-${DOMAIN}-004
- AT-${DOMAIN}-004

Componentes normalmente envolvidos:

- Repository
- Domain Service
- Application Service
- DTO
- Mapper
- Controller
- Testes

---

## TK-${DOMAIN}-005 — Implementar Alteração de Status

Relacionada a:

- RF-${DOMAIN}-005
- UC-${DOMAIN}-005
- AT-${DOMAIN}-005

Componentes normalmente envolvidos:

- Repository
- Domain Service
- Application Service
- Controller
- Testes

---

# Matriz de Rastreabilidade

| Task | RF | UC | AT |
|------|----|----|----|
| TK-${DOMAIN}-001 | RF-${DOMAIN}-001 | UC-${DOMAIN}-001 | AT-${DOMAIN}-001 |
| TK-${DOMAIN}-002 | RF-${DOMAIN}-002 | UC-${DOMAIN}-002 | AT-${DOMAIN}-002 |
| TK-${DOMAIN}-003 | RF-${DOMAIN}-003 | UC-${DOMAIN}-003 | AT-${DOMAIN}-003 |
| TK-${DOMAIN}-004 | RF-${DOMAIN}-004 | UC-${DOMAIN}-004 | AT-${DOMAIN}-004 |
| TK-${DOMAIN}-005 | RF-${DOMAIN}-005 | UC-${DOMAIN}-005 | AT-${DOMAIN}-005 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- todas as tarefas estiverem associadas a pelo menos um requisito funcional;
- todas as tarefas estiverem associadas a pelo menos um caso de uso;
- todas as tarefas estiverem associadas a pelo menos um critério de aceitação;
- não representar planejamento da construção;
- manter consistência com a Specification, Casos de Uso, API Contract e Acceptance Tests.

---

# Responsabilidades

## specs/

Define **o que** deverá ser implementado.

## construction/

Define **como**, **quando**, **por quem** e **em qual ordem** a implementação será realizada.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | YYYY-MM-DD | Engineering Framework | Criação do template |