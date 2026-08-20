# feature.yaml — Contrato Oficial

| Campo | Valor |
|--------|--------|
| Artefato | feature-yaml.md |
| Camada | Foundation |
| Versão | 1.2 |
| Status | STABLE |

---

# Objetivo

Este documento define o contrato oficial do arquivo `feature.yaml`: Single Source of Truth (SSOT) de **identidade** e de **estado** de uma Feature.

O campo `status` representa o **ciclo de vida completo** da Feature — não somente a fase de Specification.

Nenhum artefato em `specs/features/<feature>/` pode contradizer as informações declaradas neste arquivo.

Git, CI, testes, logs e `tasks.md` são evidência ou plano operacional. **Não** substituem `feature.yaml` como SSOT de estado.

---

# Localização

```text
specs/features/<feature>/feature.yaml
```

O segmento `<feature>` deve ser o **kebab-case** do nome da Feature (ex.: `area`, `authentication`).

---

# Estrutura

```yaml
feature:
  id: <string>
  name: <string>

template:
  name: <string>
  version: <string>

status: <enum>
```

---

# Campos Obrigatórios

## `feature.id`

| Propriedade | Valor |
|-------------|--------|
| Tipo | `string` |
| Obrigatório | Sim |
| Formato | `FT-<DOMAIN>` |
| Padrão | `^FT-[A-Z][A-Z0-9_]*$` |
| Exemplo | `FT-AREA` |

Identificador único e imutável da Feature no projeto.

---

## `feature.name`

| Propriedade | Valor |
|-------------|--------|
| Tipo | `string` |
| Obrigatório | Sim |
| Formato | Nome legível em português |
| Exemplo | `Área` |

Nome funcional da Feature para documentação e relatórios.

---

## `template.name`

| Propriedade | Valor |
|-------------|--------|
| Tipo | `string` |
| Obrigatório | Sim |
| Formato | kebab-case |
| Exemplo | `crud-feature` |

Referência ao template oficial em `specs/templates/<template.name>/`.

---

## `template.version`

| Propriedade | Valor |
|-------------|--------|
| Tipo | `string` |
| Obrigatório | Sim |
| Formato | Semver `MAJOR.MINOR` |
| Exemplo | `"1.1"` |

Versão do template utilizada pela Feature. Deve corresponder à versão documentada no template referenciado.

---

## `status`

| Propriedade | Valor |
|-------------|--------|
| Tipo | `enum` |
| Obrigatório | Sim |
| Valores persistentes permitidos | `DRAFT` \| `READY_FOR_REVIEW` \| `APPROVED` \| `IMPLEMENTING` \| `DONE` |

Estado do ciclo de vida completo da Feature.

### Máquina oficial

```text
DRAFT
  ↓
READY_FOR_REVIEW
  ↓
APPROVED
  ↓
IMPLEMENTING
  ↓
DONE
```

Retrabalho de especificação (único retorno persistente):

```text
READY_FOR_REVIEW → DRAFT
```

`REWORK` **não é estado persistente**. Problemas na revisão da spec fazem `READY_FOR_REVIEW` → `DRAFT`. O motivo pertence à evidência da revisão.

### Semântica

| Valor | Significado |
|-------|-------------|
| `DRAFT` | Feature em elaboração. Spec pode estar incompleta. **Não** autoriza implementação. |
| `READY_FOR_REVIEW` | Spec atingiu o mínimo para revisão formal. DoR-Spec e Gate 1 verificados. Sem bloqueadores conhecidos para a revisão da spec. |
| `APPROVED` | Spec formalmente revisada e aprovada. Distinto da revisão de PR. **Não** autoriza código até DoR-Implementation. |
| `IMPLEMENTING` | Implementação formalmente autorizada e em execução. Inicia **após** Readiness de implementação (DoR-Implementation), não no primeiro commit. |
| `DONE` | Feature concluída. DoD, Gate 3, Gate 6, validação, revisão de PR e evidências atendidos. |

### Transições

| De | Para | Condição |
|----|------|----------|
| (criação) | `DRAFT` | `feature.yaml` criado |
| `DRAFT` | `READY_FOR_REVIEW` | DoR-Spec atendido; Gate 1 verificado; artefatos obrigatórios presentes; sem bloqueadores para revisão da spec |
| `READY_FOR_REVIEW` | `APPROVED` | Review de Spec aprovada |
| `READY_FOR_REVIEW` | `DRAFT` | Review de Spec com problemas (evidência na revisão) |
| `APPROVED` | `IMPLEMENTING` | DoR-Implementation atendido; Readiness de implementação aprovado; tarefas identificadas; dependências e impactos relevantes conhecidos |
| `IMPLEMENTING` | `DONE` | Implementação concluída; validação realizada; Review de PR realizada; Gate 3; DoD; Gate 6; evidências disponíveis |

Readiness é **avaliação** (comando/atividade), não estado.

Validate é **atividade de evidência**. Não altera `feature.yaml`.

Gates são **verificações**. Não são estados. Resultados PASS/FAIL dos Gates 2–5 **não** viram `status` da Feature.

---

# Compatibilidade

Features existentes podem ainda usar o formato legado:

```yaml
status:
  specification: <enum>
```

Esse formato **não faz parte do contrato v1.2**. Valores legados (`REWORK`, apenas fase de specification) são **impacto de migração futura**. Não migrar Features nesta versão do contrato.

Até a migração: `status.specification` não substitui o enum de ciclo completo. Consumidores novos devem usar `status:` escalar.

Campos adicionais (`status.construction`, `metadata.*`) **não** fazem parte deste contrato e não são SSOT de estado.

---

# Convenções

- Arquivo exclusivamente em **YAML**.
- Indentação: 2 espaços.
- Strings com caracteres especiais devem usar aspas.
- Versão de template sempre entre aspas (ex.: `"1.1"`).
- Um único `feature.yaml` por diretório de Feature.
- O valor de `feature.id` deve ser consistente com o prefixo de identificadores nos artefatos (ex.: `RF-AREA-*` para `FT-AREA`).

---

# Restrições

- `feature.id` não pode ser alterado após aprovação da Feature sem processo formal de governança.
- `template.name` e `template.version` devem referenciar template existente e estável.
- Não utilizar `feature.yaml` para regras de negócio, contratos de API ou conteúdo funcional — esses pertencem aos artefatos do template.
- `READY_FOR_REVIEW` só após DoR-Spec e Gate 1.
- `APPROVED` só após Review de Spec. Gate 1 **não** atribui `APPROVED`.
- `IMPLEMENTING` só após DoR-Implementation e Readiness de implementação.
- `DONE` só após DoD, Gate 3 e Gate 6 (e evidências associadas).
- Não persistir `REWORK` em `status`.

---

# Exemplo Válido

```yaml
feature:
  id: FT-AREA
  name: Área

template:
  name: crud-feature
  version: "1.1"

status: READY_FOR_REVIEW
```

---

# Validação

Antes de transitar `status`, validar:

- [ ] Arquivo existe em `specs/features/<feature>/feature.yaml`
- [ ] Todos os campos obrigatórios presentes
- [ ] `feature.id` segue o padrão `FT-<DOMAIN>`
- [ ] `template.name` corresponde a diretório existente em `specs/templates/`
- [ ] `template.version` corresponde à versão do template referenciado
- [ ] `status` utiliza um dos cinco valores persistentes oficiais

---

# Referências

- `specs/foundation/definition-of-ready.md` — DoR-Spec e DoR-Implementation
- `specs/foundation/feature-quality-gates.md` — verificações (não estados)
- `specs/foundation/review-process.md` — Review de Spec e Review de PR
- `specs/foundation/definition-of-done.md` — critério de `DONE`
- `specs/foundation/development-workflow.md` — fluxo cotidiano

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.1 | 2026-07-13 | Contrato inicial (`status.specification`; `REWORK` persistente) |
| 1.2 | 2026-08-19 | Ciclo completo em `status`; `IMPLEMENTING` e `DONE`; `REWORK` não persistente |
