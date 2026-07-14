# Acceptance Tests

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

Este documento define os critérios de aceitação da Feature.

Seu objetivo é garantir que todos os requisitos funcionais possuam cenários verificáveis, rastreáveis e testáveis.

Este documento especifica apenas os critérios funcionais da Feature.

As estratégias de testes, níveis, ferramentas, cobertura mínima e convenções de implementação são definidos exclusivamente em:

```text
docs/implementation/08-testing-strategy.md
```

Este documento não deverá duplicar padrões corporativos de testes.

---

# Convenções

Os critérios de aceitação deverão utilizar a seguinte identificação:

```text
AT-${DOMAIN}-001
AT-${DOMAIN}-002
AT-${DOMAIN}-003
...
```

Cada critério deverá possuir rastreabilidade completa com:

- Requisito Funcional (RF)
- Caso de Uso (UC)
- Regra de Negócio (RN)

---

# Template

## AT-${DOMAIN}-XXX — <Título>

### Objetivo

Descrever claramente o comportamento esperado.

---

### Prioridade

- Must
- Should
- Could

---

### Tipo

- Happy Path
- Negative
- Boundary
- Authorization
- Business Rule

---

### Requisitos Funcionais Relacionados

- RF-${DOMAIN}-XXX

---

### Casos de Uso Relacionados

- UC-${DOMAIN}-XXX

---

### Regras de Negócio Relacionadas

- RN-${DOMAIN}-XXX

---

### Pré-condições

Descrever o estado necessário antes da execução do cenário.

---

### Cenário

Utilizar o padrão Given / When / Then.

#### Given

Estado inicial.

#### When

A ação executada.

#### Then

Resultado esperado.

---

### Resultado Esperado

Descrever claramente o comportamento esperado da aplicação.

---

### Observações

Registrar apenas quando necessário.

---

# CRUD Base

## AT-${DOMAIN}-001 — Criar Registro

Validar:

- criação com sucesso;
- obrigatoriedade de campos;
- duplicidade;
- auditoria.

---

## AT-${DOMAIN}-002 — Consultar por Identificador

Validar:

- consulta existente;
- recurso inexistente;
- autorização.

---

## AT-${DOMAIN}-003 — Listar Registros

Validar:

- paginação;
- ordenação;
- filtros;
- coleção vazia.

---

## AT-${DOMAIN}-004 — Atualizar Registro

Validar:

- atualização com sucesso;
- validações;
- conflitos;
- auditoria.

---

## AT-${DOMAIN}-005 — Alterar Status

Validar:

- ativação;
- inativação;
- operação inválida;
- auditoria.

---

# Cenários Negativos

Quando aplicável, documentar cenários como:

- usuário não autenticado;
- usuário sem permissão;
- recurso inexistente;
- conflito de dados;
- violação de regra de negócio;
- payload inválido.

---

# Matriz de Rastreabilidade

| Teste | RF | UC | RN |
|--------|----|----|----|
| AT-${DOMAIN}-001 | RF-${DOMAIN}-001 | UC-${DOMAIN}-001 | RN-${DOMAIN}-001 |
| AT-${DOMAIN}-002 | RF-${DOMAIN}-002 | UC-${DOMAIN}-002 | RN-${DOMAIN}-002 |
| AT-${DOMAIN}-003 | RF-${DOMAIN}-003 | UC-${DOMAIN}-003 | RN-${DOMAIN}-003 |
| AT-${DOMAIN}-004 | RF-${DOMAIN}-004 | UC-${DOMAIN}-004 | RN-${DOMAIN}-004 |
| AT-${DOMAIN}-005 | RF-${DOMAIN}-005 | UC-${DOMAIN}-005 | RN-${DOMAIN}-005 |

---

# Critérios de Conformidade

Este documento será considerado conforme quando:

- todos os requisitos funcionais possuírem pelo menos um critério de aceitação;
- todos os critérios estiverem rastreados;
- não existirem cenários sem requisito associado;
- mantiver consistência com a Specification, Casos de Uso e API Contract;
- não duplicar padrões definidos em `docs/implementation/08-testing-strategy.md`.

---

# Histórico de Alterações

| Versão | Data | Autor | Descrição |
|--------|------|--------|-----------|
| 1.0 | YYYY-MM-DD | Engineering Framework | Criação do template |