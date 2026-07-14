# Use Cases

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

Este documento descreve os casos de uso da Feature.

É responsável por detalhar o comportamento funcional esperado da aplicação e estabelecer a rastreabilidade entre:

- Specification;
- API Contract;
- Acceptance Tests;
- Regras de Negócio.

Este documento não descreve detalhes de implementação.

---

# Convenções

## Identificação

```text
UC-${DOMAIN}-001
UC-${DOMAIN}-002
UC-${DOMAIN}-003
...
```

## Fluxos Alternativos

```text
FA-001
FA-002
...
```

## Fluxos de Exceção

```text
FE-001
FE-002
...
```

---

# Template de Caso de Uso

## UC-${DOMAIN}-XXX — <Título>

### Objetivo

Descrever claramente o objetivo do caso de uso.

---

### Prioridade

- Must
- Should
- Could

---

### Complexidade

- Baixa
- Média
- Alta

---

### Atores

Relacionar todos os atores envolvidos.

---

### Pré-condições

Listar todas as condições necessárias para início da execução.

---

### Fluxo Principal

Descrever o fluxo principal numerado.

---

### Fluxos Alternativos

Registrar apenas quando existirem.

---

### Fluxos de Exceção

Registrar apenas quando existirem.

---

### Pós-condições

Descrever o estado esperado após a conclusão do caso de uso.

---

### Requisitos Funcionais Relacionados

- RF-${DOMAIN}-XXX

---

### Regras de Negócio Relacionadas

- RN-${DOMAIN}-XXX

---

### Requisitos Não Funcionais Relacionados

- RNF-${DOMAIN}-XXX

---

### Critérios de Aceitação Relacionados

- AT-${DOMAIN}-XXX

---

# CRUD Base

## UC-${DOMAIN}-001 — Cadastrar

Objetivo:

Cadastrar um novo recurso.

---

## UC-${DOMAIN}-002 — Consultar por Identificador

Objetivo:

Consultar um recurso existente.

---

## UC-${DOMAIN}-003 — Listar

Objetivo:

Listar recursos utilizando os mecanismos corporativos de paginação, ordenação e filtros.

---

## UC-${DOMAIN}-004 — Atualizar

Objetivo:

Atualizar um recurso existente.

---

## UC-${DOMAIN}-005 — Alterar Status

Objetivo:

Ativar ou inativar um recurso.

---

# Casos de Uso Opcionais

Criar somente quando fizerem parte da Specification.

Exemplos:

- Importar
- Exportar
- Aprovar
- Publicar
- Arquivar
- Restaurar
- Sincronizar

---

# Matriz de Rastreabilidade

| Caso de Uso | RF | RN | RNF | API | Teste |
|--------------|----|----|-----|-----|--------|
| UC-${DOMAIN}-001 | RF-${DOMAIN}-001 | RN-${DOMAIN}-001 | — | POST ${API_BASE_PATH} | AT-${DOMAIN}-001 |
| UC-${DOMAIN}-002 | RF-${DOMAIN}-002 | RN-${DOMAIN}-002 | — | GET ${API_BASE_PATH}/{${PRIMARY_KEY}} | AT-${DOMAIN}-002 |
| UC-${DOMAIN}-003 | RF-${DOMAIN}-003 | RN-${DOMAIN}-003 | — | GET ${API_BASE_PATH} | AT-${DOMAIN}-003 |
| UC-${DOMAIN}-004 | RF-${DOMAIN}-004 | RN-${DOMAIN}-004 | — | PUT ${API_BASE_PATH}/{${PRIMARY_KEY}} | AT-${DOMAIN}-004 |
| UC-${DOMAIN}-005 | RF-${DOMAIN}-005 | RN-${DOMAIN}-005 | — | PATCH ${API_BASE_PATH}/{${PRIMARY_KEY}}/status | AT-${DOMAIN}-005 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- todos os casos de uso estiverem rastreados;
- cada caso de uso possuir pelo menos um requisito funcional associado;
- todos os fluxos estiverem documentados quando aplicáveis;
- não existirem casos de uso sem critérios de aceitação;
- mantiver consistência com a Specification e o API Contract.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | YYYY-MM-DD | Engineering Framework | Criação do template |