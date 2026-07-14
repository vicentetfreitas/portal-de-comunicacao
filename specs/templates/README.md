# Templates

## Objetivo

Este diretório contém os modelos oficiais utilizados para produzir especificações no Portal de Comunicação.

Os templates garantem que todas as Features sejam documentadas de forma consistente, reduzindo ambiguidades e facilitando a implementação por desenvolvedores e agentes de IA.

Os templates representam a estrutura padrão do projeto. Eles não contêm informações de negócio nem descrevem funcionalidades específicas.

---

# Princípios

Todos os templates deste diretório devem seguir os princípios definidos em:

* `../foundation/principles.md`
* `../foundation/conventions.md`
* `../foundation/workflow.md`
* `../foundation/glossary.md`
* `../foundation/definition-of-ready.md`
* `../foundation/definition-of-done.md`

---

# Templates Disponíveis

Cada template possui uma responsabilidade específica.

| Template              | Objetivo                                                        |
| --------------------- | --------------------------------------------------------------- |
| `feature.md`          | Especificação completa de uma Feature.                          |
| `use-case.md`         | Descrição dos casos de uso de uma Feature.                      |
| `api.md`              | Contratos e comportamento das APIs relacionadas à Feature.      |
| `acceptance-tests.md` | Critérios e cenários de aceitação da Feature.                   |
| `tasks.md`            | Decomposição da implementação em tarefas técnicas.              |
| `crud-feature/`       | Template CRUD v1.1 (inclui `traceability.md`).                  |
| `decision.md`         | Registro de decisões específicas da Feature, quando necessário. |

Novos templates poderão ser adicionados conforme a evolução do projeto.

---

# Utilização

Os templates são utilizados exclusivamente como ponto de partida para novos artefatos.

Após a criação da especificação correspondente em `specs/features/`, o documento passa a representar a Feature e evolui independentemente do template original.

Alterações realizadas em uma Feature não modificam o template.

---

# Estrutura Esperada

Cada diretório em `specs/features/` deverá conter apenas os documentos necessários para representar a Feature.

Nem todos os templates precisam ser utilizados em todas as Features.

A documentação deve permanecer simples, objetiva e proporcional à complexidade da funcionalidade.

---

# Responsabilidades

Os templates definem apenas a estrutura dos documentos.

O conteúdo funcional deve ser produzido a partir das regras de negócio, dos requisitos e das decisões registradas nas especificações do projeto.

---

# Evolução

Os templates evoluem juntamente com o processo de desenvolvimento.

Sempre que um novo padrão de documentação for adotado pelo projeto, o template correspondente deverá ser atualizado para refletir essa prática.

As alterações devem preservar a compatibilidade com as especificações já produzidas, evitando mudanças desnecessárias na documentação existente.
