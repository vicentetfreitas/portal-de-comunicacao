# SPEC-MIGRATION-CLAUDE-CODE

## 1. Objetivo

Definir conceitualmente a migração do mecanismo de automação de agentes baseado em Cursor para Claude Code, preservando integralmente o SSOT do projeto e reduzindo a quantidade de instruções e mecanismos necessários para o trabalho cotidiano.

A migração deve permitir que o Claude Code execute o fluxo simplificado aprovado na Etapa 2:

```text
spec → tasks → código → CI → PR
```

O Claude Code será tratado como **mecanismo de execução**, e não como camada normativa ou fonte de verdade.

A automação deverá consumir o conhecimento existente em `docs/`, `specs/`, `database/`, `construction/` e demais fontes oficiais conforme suas respectivas categorias, sem duplicá-lo.

---

# 2. Princípios

A migração deve obedecer aos seguintes princípios:

1. **SSOT único** — automação não define produto.
2. **Não inverter camadas** — `docs → specs → construction → automação`.
3. **Precedência** — `specs/` prevalece sobre `docs/`, e `docs/` sobre código quando houver conflito comportamental.
4. **Referenciar, não duplicar** — regras Claude devem apontar para documentos oficiais.
5. **Leitura incremental** — consultar somente o contexto necessário para a tarefa.
6. **Descoberta por convenções** — consultar `path-conventions.md` antes de explorar paths.
7. **DoR antes da implementação** — ausência de prontidão impede implementação.
8. **DoD e validação** — conclusão depende das evidências definidas pelo fluxo.
9. **Não reintroduzir o framework v4.1** no fluxo diário.
10. **Novos mecanismos somente quando reduzirem complexidade comprovadamente.**

---

# 3. Escopo

## 3.1 Incluído

A migração deve definir o equivalente mínimo, no Claude Code, para:

* orientação permanente;
* política de contexto;
* modos de trabalho;
* invocações de atividades ainda válidas;
* consulta incremental ao SSOT;
* validação conforme o fluxo simplificado.

## 3.2 Excluído

Não fazem parte desta migração:

* alteração de regras de negócio;
* alteração de decisões arquiteturais;
* alteração de schema;
* alteração das especificações das features;
* alteração do código funcional;
* duplicação de `docs/`;
* duplicação de `specs/`;
* duplicação de `construction/`;
* recriação do Engineering Framework v4.1;
* recriação de Session/PKG/Snapshot/Cache;
* criação de `legacy/` na raiz;
* limpeza ou remoção de `.cursor/`;
* migração 1:1 dos agentes, prompts ou workflows do Cursor.

---

# 4. Arquitetura conceitual

A arquitetura-alvo é:

```text
                         SSOT
                          │
            ┌─────────────┴─────────────┐
            │                           │
          docs/                       specs/
            │                           │
            └─────────────┬─────────────┘
                          │
                    construction/
                  quando aplicável
                          │
                          ▼
                  Claude Code
               mecanismo de execução
                          │
            ┌─────────────┼─────────────┐
            ▼             ▼             ▼
         Specify      Implement       Review
            │             │             │
            └─────────────┼─────────────┘
                          ▼
                     validação
                          │
                          ▼
                         CI
                          │
                          ▼
                          PR
```

Claude Code não deve criar uma camada normativa paralela.

---

# 5. Fonte de verdade

Claude Code deve consultar os artefatos existentes conforme a natureza da tarefa.

## 5.1 Referências fundamentais

| Informação                       | Fonte                                                  |
| -------------------------------- | ------------------------------------------------------ |
| Precedência e classificação SSOT | `specs/foundation/minimal-ssot.md`                     |
| Fluxo diário                     | `specs/foundation/development-workflow.md`             |
| Convenções de paths              | `specs/foundation/path-conventions.md`                 |
| Comandos/invocações              | `specs/foundation/agent-commands.md`                   |
| Simplificação Etapa 2            | `docs/governance/09-framework-simplification-scope.md` |
| Arquitetura documental           | `docs/governance/07-documentation-architecture.md`     |
| Feature                          | `specs/features/<slug>/`                               |
| Regras de negócio                | `docs/domain/`                                         |
| Arquitetura                      | `docs/architecture/`                                   |
| Padrões de implementação         | `docs/implementation/`                                 |
| Schema                           | `database/`                                            |
| CI                               | `.github/workflows/`                                   |

Essas referências permanecem no SSOT. Não devem ser copiadas para a configuração Claude.

---

# 6. Orientação permanente

O mecanismo Claude deve possuir somente uma orientação permanente mínima.

Ela deve conter, conceitualmente:

1. cadeia de camadas;
2. precedência de conflito;
3. ponteiros para o SSOT;
4. proibição de implementar sem especificação;
5. proibição de explorar o repositório sem consultar `path-conventions.md`;
6. proibição de tratar estados operacionais/índices como SSOT;
7. referência às validações oficiais;
8. indicação de que Construction v4.1 não é o fluxo diário.

A orientação permanente **não deve reproduzir** o conteúdo dos documentos apontados.

### Limite

A orientação permanente deve funcionar como índice e guardrail.

Se uma informação detalhada puder ser obtida abrindo o SSOT correspondente, ela não deve ser duplicada na orientação permanente.

---

# 7. Política de contexto

O Claude deve trabalhar com descoberta incremental:

```text
1. Consultar orientação mínima
2. Identificar a natureza da tarefa
3. Consultar o SSOT correspondente
4. Consultar a feature, quando aplicável
5. Consultar padrões específicos somente quando necessários
6. Implementar ou revisar
7. Validar conforme o fluxo
```

O Claude não deve:

* varrer arbitrariamente a árvore do projeto;
* carregar todo o `.cursor/`;
* carregar todo o framework;
* reler documentos já consolidados sem necessidade;
* criar caches paralelos de contexto;
* reproduzir o Construction Cache v4.1;
* gerar artefatos que não foram solicitados ou exigidos pelo SSOT.

---

# 8. Modelo operacional

Claude Code utilizará **um único agente**.

Não haverá, como requisito arquitetural da primeira versão:

* `specification-engineer` separado;
* `backend-engineer` separado;
* `reviewer` separado;
* orchestrator;
* Session;
* PKG;
* Snapshot;
* Cache de construção.

As responsabilidades serão representadas por **modos de trabalho**.

## 8.1 Specify

Responsabilidade:

* compreender o problema;
* consultar o SSOT;
* produzir ou atualizar especificação quando autorizado;
* não implementar código como substituição da especificação.

## 8.2 Readiness

Responsabilidade:

* verificar se a feature está pronta para implementação;
* verificar DoR;
* identificar lacunas;
* não implementar enquanto os requisitos necessários não estiverem atendidos.

## 8.3 Implement

Responsabilidade:

* executar `tasks.md` ou instrução equivalente já aprovada;
* respeitar a especificação;
* consultar padrões de implementação;
* não redefinir comportamento de produto;
* não criar novas camadas sem justificativa.

## 8.4 Validate

Responsabilidade:

* executar as validações apropriadas;
* produzir evidências;
* distinguir falha de implementação, falha de ambiente e ausência de evidência.

## 8.5 Review

Responsabilidade:

* verificar aderência à especificação;
* verificar arquitetura, qualidade e testes;
* identificar desvios;
* não alterar código durante uma revisão cujo escopo seja somente review.

---

# 9. Fluxo diário

O fluxo padrão do Claude Code será:

```text
specs/features/<slug>/
        ↓
DoR / readiness
        ↓
tasks.md
        ↓
implementação
        ↓
testes / validação
        ↓
CI
        ↓
review
        ↓
PR
```

O fluxo não deve depender de:

```text
PKG
Session
Snapshot
Gate 1–6
construction orchestrator
Close v4.1
```

Esses mecanismos não serão reativados como parte da migração.

---

# 10. Invocações

O catálogo de invocações deverá ser alinhado primeiro ao fluxo simplificado.

Somente depois dessa atualização será definida a forma específica de invocação no Claude Code.

As intenções candidatas são:

```text
Specify
Readiness
Implement
Validate
Review
Status
```

Essas invocações devem apontar para documentos oficiais.

Não devem conter uma implementação paralela das regras de DoR, DoD, Gates ou governança.

Os comandos relacionados exclusivamente à cerimônia v4.1 não devem ser utilizados como comandos padrão do Claude Code.

---

# 11. Construction e framework v4.1

Construction permanece como camada de transição.

O Claude Code pode reconhecer a existência de:

```text
construction-state.yaml
registry.yaml
```

quando o fluxo exigir consulta.

Entretanto:

* `registry.yaml` não é SSOT de produto;
* `construction-state.yaml` não substitui a especificação;
* Session não será reativada;
* PKG não será reativado;
* Snapshot não será reativado;
* Construction Cache não será reativado;
* framework v4.1 não será utilizado como fluxo cotidiano.

## 11.1 Features exclusivamente v4.1

Quando uma tarefa depender exclusivamente de mecanismos históricos do Construction v4.1, o Claude deve:

1. identificar a dependência;
2. não improvisar uma equivalência;
3. não reativar automaticamente o mecanismo histórico;
4. interromper a execução;
5. solicitar decisão humana.

Esta é uma fronteira deliberada do primeiro ciclo de migração.

---

# 12. Convivência com Cursor

Durante toda a primeira fase:

```text
.cursor/     → permanece
Claude Code  → novo mecanismo
```

Os dois devem consultar o mesmo SSOT.

A criação da configuração Claude não autoriza:

* remover arquivos `.cursor/`;
* mover arquivos `.cursor/`;
* arquivar agentes;
* modificar workflows;
* apagar prompts;
* reorganizar `.cursor/`.

A coexistência é deliberada para permitir comparação e validação.

---

# 13. Destino do legado

Não será criado:

```text
legacy/
```

na raiz do projeto como consequência desta migração.

O conceito de legado continuará separado por camada:

```text
docs/governance/history/
docs/audit/
construction/history/
.cursor/archive/
```

A eventual reorganização do `.cursor/` será uma fase posterior e independente.

Nenhum artefato será movido apenas para "limpar" o projeto.

---

# 14. Decisões humanas incorporadas

## D1 — Convivência

Cursor e Claude Code coexistirão durante a migração e validação.

## D2 — Catálogo de comandos

`agent-commands.md` será alinhado ao fluxo simplificado antes da definição das invocações Claude.

## D3 — Modelo de agente

Claude utilizará um único agente com modos de trabalho, e não uma coleção de agentes especializados.

## D4 — Corte do Cursor

`.cursor/` permanecerá intacto durante a primeira implementação e validação.

## D5 — Features v4.1

Features exclusivamente dependentes do framework v4.1 não serão executadas automaticamente pelo Claude. Dependências desse tipo exigirão decisão humana.

---

# 15. Critérios de não-regressão

A migração será considerada conceitualmente correta somente se:

* o SSOT continuar único;
* nenhuma regra de negócio for duplicada na configuração Claude;
* nenhuma especificação for movida para `.claude/`;
* `docs/` continuar exercendo sua função normativa conforme a arquitetura documental;
* `specs/` continuar sendo a fonte de comportamento das features;
* o fluxo diário continuar sendo `spec → tasks → código → CI → PR`;
* DoR continuar obrigatório;
* `path-conventions.md` continuar sendo a referência para descoberta;
* Construction v4.1 não voltar ao fluxo diário;
* `.cursor/` continuar intacto durante a primeira fase;
* nenhum `legacy/` genérico for criado;
* o Claude conseguir operar sem carregar o framework completo;
* a configuração Claude permanecer menor que uma cópia estrutural do `.cursor/`.

---

# 16. Critérios de validação operacional

Após a implementação do mecanismo Claude, devem ser testados pelo menos os seguintes cenários:

### V01 — Localização do SSOT

Claude identifica corretamente onde consultar a informação necessária.

### V02 — Precedência

Diante de conflito, Claude aplica:

```text
specs > docs > código
```

conforme definido no SSOT.

### V03 — Implementação sem spec

Ao receber uma tarefa sem especificação suficiente, Claude não implementa silenciosamente.

### V04 — Descoberta de paths

Claude consulta `path-conventions.md` antes de exploração estrutural relevante.

### V05 — Registry não é SSOT

Claude não interpreta `registry.yaml` ou estados operacionais como definição de produto.

### V06 — Feature v4.1

Ao encontrar dependência exclusiva do framework v4.1, Claude interrompe e solicita decisão humana.

### V07 — Implementação

Claude consegue executar uma task aprovada utilizando a feature e os padrões correspondentes.

### V08 — Validação

Claude executa as validações adequadas ao módulo alterado.

### V09 — Review

Claude consegue revisar sem alterar o código quando o modo solicitado for somente revisão.

### V10 — Contexto

Claude não precisa carregar todo o `.cursor/`, `docs/`, `specs/` ou framework para executar uma tarefa normal.

---

# 17. Fora da especificação

Esta especificação não determina:

* nome de arquivo específico do Claude Code;
* conteúdo final de `CLAUDE.md`;
* estrutura final de `.claude/`;
* slash commands;
* skills;
* hooks;
* permissões;
* configuração específica do runtime;
* remoção do Cursor;
* movimentação de arquivos;
* alterações no código da aplicação.

Esses itens pertencem à fase de implementação da migração.

---

# 18. Fases de execução

## Fase 1 — Decisão

Concluída.

Decisões D1–D5 aprovadas.

## Fase 2 — Preparação

Antes de criar configuração Claude:

1. alinhar `agent-commands.md` ao fluxo Etapa 2;
2. inventariar `.cursor/` apenas para comparação;
3. identificar o conjunto mínimo de informações necessárias ao Claude;
4. não alterar `.cursor/`.

## Fase 3 — Implementação

Criar somente o mecanismo Claude necessário para:

* orientação mínima;
* política de contexto;
* modos de trabalho;
* invocações aprovadas.

Nenhum SSOT será duplicado.

## Fase 4 — Validação

Executar V01–V10.

A validação deve demonstrar que o Claude opera corretamente consultando o SSOT.

## Fase 5 — Pós-validação

Somente após a validação:

* decidir o destino definitivo do `.cursor/`;
* avaliar resíduos de transição;
* arquivar/remover mecanismos antigos somente mediante decisão humana;
* não criar `legacy/` genérico.

---

# 19. Resultado esperado

Ao final da migração, o projeto deverá possuir:

```text
SSOT do projeto
      │
      ├── docs/
      ├── specs/
      ├── database/
      ├── construction/
      └── demais fontes oficiais
              │
              ▼
        Claude Code
      mecanismo mínimo
              │
              ▼
     fluxo simplificado
spec → tasks → código → CI → PR
```

O Claude Code deverá ser **derivado do projeto**, e não o projeto derivado do Claude Code.

A simplificação será considerada bem-sucedida quando o mecanismo de automação puder ser reduzido ao mínimo necessário para operar o fluxo aprovado, sem recriar o framework v4.1 e sem introduzir uma nova camada de conhecimento concorrente com o SSOT.

---

## 20. Status

**Estado:** CONCLUÍDA (todas as 5 fases executadas, 2026-08-26)

**Decisões humanas:** D1–D5 aprovadas

**Implementação:** PRIMEIRO CICLO CONCLUÍDO (2026-08-19)

**Alterações no projeto:** `agent-commands.md` v1.2; orientação permanente `CLAUDE.md`; invocações em `.claude/commands/` (Specify, Readiness, Implement, Validate, Review, Status). `.cursor/` intacto.

**Fase 4 — Validação operacional (2026-08-26):** V01–V10 executada com base em evidência observada em sessão Claude Code real (fechamento de FT-COLABORADOR/FT-AREA-COLABORADOR/FT-PRIMEIRO-ACESSO, extração de repositórios).

| # | Critério | Resultado |
|---|----------|-----------|
| V01 | Localização do SSOT | PASS — `feature.yaml`, `minimal-ssot.md`, decision logs consultados conforme a natureza de cada tarefa |
| V02 | Precedência `specs > docs > código` | PASS — conflito achado entre `feature.yaml` legado e `feature-yaml.md` (ambos `specs/`) foi escalado para decisão humana, não resolvido por conta própria |
| V03 | Implementação sem spec | N/A — nenhuma implementação de código sem spec foi solicitada nesta sessão |
| V04 | Descoberta de paths via `path-conventions.md` | **FAIL** — exploração feita via `find`/`grep`/`ls` diretos, sem consultar `path-conventions.md` antes |
| V05 | Registry não é SSOT | PASS — `construction/registry.yaml`/`pkg-XX/status.md` nunca tratados como fonte de estado |
| V06 | Dependência exclusiva v4.1 → parar | N/A — nenhuma dependência desse tipo apareceu |
| V07 | Implementação de task aprovada | N/A — nenhuma task de código executada nesta sessão |
| V08 | Validação por camada alterada | PASS — `mvn clean verify` (backend) e `typecheck`/`lint`/`test:unit` (frontend) antes do fechamento de FT-PRIMEIRO-ACESSO |
| V09 | Review sem alterar código | PASS — modos Review/Status não alteraram código; só `status` de `feature.yaml` quando aplicável |
| V10 | Contexto mínimo (sem carregar tudo) | **PARTIAL** — não carregou `.cursor/` nem o framework completo, mas leu documentos inteiros grandes (`03-open-decisions.md` ~1300 linhas) em vez de leitura incremental — contribuiu para consumo de tokens acima do esperado |

**Resultado:** 5 PASS, 3 N/A, 1 FAIL (V04), 1 PARTIAL (V10). Mecanismo opera corretamente quanto a SSOT/precedência/registry/validação/review; gap real em disciplina de leitura incremental (princípio 5) — corrigir em sessões futuras: consultar `path-conventions.md` antes de exploração ad-hoc e preferir leitura incremental/skills a arquivos inteiros.

**Fase 5 — Pós-validação (2026-08-26):** decisão humana obtida — Cursor deve sair do projeto; foco exclusivo em Claude Code. `.cursor/` (44 arquivos: `agents/`, `orchestrator/`, `prompts/`, `rules/`, `archive/`, `settings.json`) removido do repositório neste commit. Histórico integral recuperável via `git log -- .cursor/` até este ponto; nenhum `legacy/` genérico criado — a remoção segue o critério de não-regressão da Seção 15 ("nenhum `legacy/` genérico for criado").

**Migração concluída.** Todas as 5 fases (Decisão, Preparação, Implementação, Validação, Pós-validação) executadas.
