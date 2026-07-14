# feature.yaml — Contrato Oficial

| Campo | Valor |
|--------|--------|
| Artefato | feature-yaml.md |
| Camada | Foundation |
| Versão | 1.1 |
| Status | STABLE |

---

# Objetivo

Este documento define o contrato oficial do arquivo `feature.yaml`, Single Source of Truth (SSOT) de identidade e estado de uma Feature no Specification Framework.

Nenhum artefato em `specs/features/<feature>/` pode contradizer as informações declaradas neste arquivo.

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

status:
  specification: <enum>
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

## `status.specification`

| Propriedade | Valor |
|-------------|--------|
| Tipo | `enum` |
| Obrigatório | Sim |
| Valores permitidos | Ver tabela abaixo |

Estado da fase de Specification.

| Valor | Descrição |
|-------|-----------|
| `DRAFT` | Feature em elaboração; artefatos incompletos ou em revisão interna |
| `READY_FOR_REVIEW` | Fase de Specification concluída; aguardando revisão formal |
| `REWORK` | Revisão exigiu correções; retorno à elaboração |
| `APPROVED` | Especificação aprovada na revisão; apta para Readiness Review e implementação futura |

**Estado oficial de conclusão da fase de Specification:** `READY_FOR_REVIEW`.

---

# Campos Opcionais

Campos adicionais em `status` ou na raiz do documento **não fazem parte deste contrato v1.1** e devem ser ignorados por consumidores do Framework até formalização em versão futura.

Exemplos não padronizados (evitar até especificação futura):

- `status.construction`
- `metadata.*`

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
- Status `APPROVED` só pode ser atribuído após revisão formal bem-sucedida (Gate 1).

---

# Exemplo Válido

```yaml
feature:
  id: FT-AREA
  name: Área

template:
  name: crud-feature
  version: "1.1"

status:
  specification: READY_FOR_REVIEW
```

---

# Validação

Antes de iniciar ou encerrar a fase de Specification, validar:

- [ ] Arquivo existe em `specs/features/<feature>/feature.yaml`
- [ ] Todos os campos obrigatórios presentes
- [ ] `feature.id` segue o padrão `FT-<DOMAIN>`
- [ ] `template.name` corresponde a diretório existente em `specs/templates/`
- [ ] `template.version` corresponde à versão do template referenciado
- [ ] `status.specification` utiliza valor do enum oficial

---

# Referências

- `specs/foundation/feature-quality-gates.md` — Gate 1
- `specs/foundation/definition-of-ready.md`
- `.cursor/rules/workflows/specification-flow.mdc` — fluxo oficial

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.1 | 2026-07-13 | Contrato inicial formal (Sprint Framework v1.1) |
