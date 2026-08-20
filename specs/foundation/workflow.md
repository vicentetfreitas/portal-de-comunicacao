# Fluxo de Trabalho

## Objetivo

Definir como o projeto utiliza especificações para conduzir o desenvolvimento.

Este documento descreve o fluxo geral. Artefatos específicos (features, domínio, APIs, decisões) serão definidos conforme a evolução natural do projeto.

---

## Ciclo fundamental

```text
Necessidade identificada
        ↓
Produção da especificação          (status: DRAFT)
        ↓
DoR-Spec + Gate 1                  → READY_FOR_REVIEW
        ↓
Review de Spec                     → APPROVED  ou  DRAFT
        ↓
DoR-Implementation                 → IMPLEMENTING
        ↓
Implementação + Validate + Review de PR (Gate 3)
        ↓
DoD + Gate 6                       → DONE
```

O SSOT de estado é `specs/features/<slug>/feature.yaml`. Contrato: `specs/foundation/feature-yaml.md`.

DoR, Gates, Review e Validate **não** são estados. Detalhe cotidiano: `development-workflow.md`.

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

Uma especificação consolidada e **aprovada** (`APPROVED`) pode orientar implementação somente após DoR-Implementation.

### 4. Implementação

O código, a infraestrutura e os testes materializam a especificação.

Desenvolvedores e agentes de IA consultam `specs/` antes de implementar. Não se infere comportamento a partir do código existente ou da documentação histórica.

### 5. Validação de aderência

Após a implementação, verifica-se que o comportamento entregue corresponde à especificação.

Divergências são resolvidas na origem: se a especificação está correta, corrige-se a implementação; se a especificação está incorreta ou incompleta, atualiza-se a especificação antes de ajustar o código.

---

## Consulta a `docs/`

`docs/domain/` (e demais docs consultivos de legado) alimenta a **produção da spec**, não o comportamento a implementar.

```text
docs/ (domínio, legado)  →  consulta  →  specs/  →  implementação
```

Durante `IMPLEMENTING`, `docs/implementation/` pode ser usado como **padrões de código** da camada tocada. Não substitui a spec como SSOT de comportamento.

Não implementar comportamento de produto direto de documentação histórica.

---

## Evolução da estrutura

Conforme o projeto cresce, novos tipos de artefato podem ser introduzidos em `specs/` (features, domínio, contratos de API, decisões, etc.).

Cada novo tipo de artefato será definido quando surgir a primeira necessidade real. A Foundation não antecipa essa estrutura.

Quando novos artefatos forem criados, este documento será atualizado para refletir o fluxo ampliado.
