# Fluxo de Trabalho

## Objetivo

Definir como o projeto utiliza especificações para conduzir o desenvolvimento.

Este documento descreve o fluxo geral. Artefatos específicos (features, domínio, APIs, decisões) serão definidos conforme a evolução natural do projeto.

---

## Ciclo fundamental

```text
Necessidade identificada
        ↓
Produção da especificação
        ↓
Definition of Ready (DoR)
        ↓
Implementação
        ↓
Definition of Done (DoD)
```

---

## Etapas

### 1. Identificação da necessidade

Uma necessidade pode originar-se de:

- requisito de negócio;
- análise do sistema legado (via `docs/`);
- correção de comportamento incorreto;
- evolução de funcionalidade existente.

A necessidade deve ser compreendida antes de qualquer especificação ser escrita.

### 2. Produção da especificação

A especificação traduz a necessidade em definição clara e implementável.

Durante a produção, `docs/` pode ser consultado como fonte de contexto. Informações extraídas devem ser validadas — o legado pode conter inconsistências, comportamentos obsoletos ou decisões que não se aplicam ao novo sistema.

### 3. Revisão e consolidação

A especificação deve ser revisada quanto a:

- clareza e completude;
- ausência de ambiguidade;
- aderência aos princípios definidos em `principles.md`;
- conformidade com as convenções definidas em `conventions.md`.

Uma especificação consolidada está pronta para orientar implementação.

### 4. Implementação

O código, a infraestrutura e os testes materializam a especificação.

Desenvolvedores e agentes de IA consultam `specs/` antes de implementar. Não se infere comportamento a partir do código existente ou da documentação histórica.

### 5. Validação de aderência

Após a implementação, verifica-se que o comportamento entregue corresponde à especificação.

Divergências são resolvidas na origem: se a especificação está correta, corrige-se a implementação; se a especificação está incorreta ou incompleta, atualiza-se a especificação antes de ajustar o código.

---

## Consulta a `docs/`

`docs/` é utilizado exclusivamente na etapa de produção da especificação, como fonte consultiva.

```text
docs/  →  consulta  →  specs/  →  implementação
```

Nunca:

```text
docs/  →  implementação
```

---

## Evolução da estrutura

Conforme o projeto cresce, novos tipos de artefato podem ser introduzidos em `specs/` (features, domínio, contratos de API, decisões, etc.).

Cada novo tipo de artefato será definido quando surgir a primeira necessidade real. A Foundation não antecipa essa estrutura.

Quando novos artefatos forem criados, este documento será atualizado para refletir o fluxo ampliado.
