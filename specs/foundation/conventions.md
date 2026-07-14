# Convenções

## Objetivo

Definir convenções fundamentais para a produção e organização de especificações no Portal de Comunicação.

---

## Idioma

Especificações são escritas em **português**.

Termos técnicos consagrados em inglês (API, endpoint, deploy, cache) podem ser utilizados sem tradução quando a tradução reduzir a clareza.

---

## Formato

Especificações são produzidas em **Markdown** (`.md`).

Cada documento deve conter:

- título com o assunto;
- seção de objetivo ou propósito;
- conteúdo com escopo delimitado.

Evitar documentos extensos que misturem múltiplos assuntos. Preferir documentos focados e compostos quando necessário.

---

## Nomenclatura de arquivos

- Nomes em **kebab-case**: `gestao-de-documentos.md`
- Sem prefixos numéricos obrigatórios — a numeração será adotada quando a estrutura de diretórios justificar ordenação
- Sem abreviações obscuras
- Extensão `.md`

---

## Organização de diretórios

- Um diretório por tipo de artefato, criado quando o primeiro artefato daquele tipo for necessário
- Não criar diretórios vazios ou reservados para uso futuro
- A estrutura de `specs/` evolui incrementalmente

---

## Relação entre `docs/` e `specs/`

| Aspecto | `docs/` | `specs/` |
|---|---|---|
| Natureza | Documentação histórica e consultiva | Especificação oficial |
| Orienta implementação | Não | Sim |
| Pode ser alterada para corrigir implementação | Não | Sim |
| Prevalece em caso de conflito | Não | Sim |

### Uso permitido de `docs/`

- Compreender o domínio e as regras de negócio do legado
- Identificar integrações, processos e infraestrutura existentes
- Extrair conhecimento para fundamentar especificações

### Uso proibido de `docs/`

- Implementar diretamente com base em documentos históricos
- Tratar `docs/` como fonte da verdade
- Reproduzir conteúdo de `docs/` em `specs/` sem validação e consolidação

### Consolidação

Informação extraída de `docs/` deve ser:

1. Validada quanto à relevância para o novo sistema
2. Adaptada quando o comportamento legado não representar o comportamento desejado
3. Registrada em especificação própria em `specs/`

---

## Referências cruzadas

Ao utilizar informação originada de `docs/`, a especificação deve indicar a fonte consultiva.

Exemplo:

```text
Fonte consultiva: docs/domain/05-business-rules.md
```

Referências a `docs/` são rastreabilidade, não dependência. A especificação deve ser compreensível e autossuficiente para orientar implementação.

---

## Referências entre especificações

Especificações podem referenciar outras especificações em `specs/`.

Utilizar caminhos relativos dentro de `specs/`:

```text
Ver: foundation/workflow.md
```

---

## Versionamento

Especificações são versionadas pelo controle de versão do repositório (Git).

Alterações significativas devem ser identificáveis no histórico de commits. Não se mantém versionamento manual dentro dos documentos nesta fase.

---

## Responsabilidade

Toda pessoa ou agente que implementa código no projeto é responsável por:

1. Consultar `specs/` antes de implementar
2. Não implementar comportamento não especificado
3. Sinalizar lacunas ou inconsistências nas especificações existentes
4. Iniciar alterações funcionais em `specs/`, não no código
