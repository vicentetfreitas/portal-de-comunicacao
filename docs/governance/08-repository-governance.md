# Repository Governance

**Versão:** 1.0
**Status:** Approved
**Categoria:** Governança
**Objetivo:** Definir a política oficial de versionamento do repositório.

---

# Objetivo

Estabelecer critérios objetivos para determinar quais artefatos devem permanecer versionados ao longo da vida do projeto.

Esta política tem como finalidade preservar o conhecimento permanente do projeto, evitando o crescimento desnecessário do repositório por artefatos temporários ou reconstruíveis.

---

# Princípios

Todo artefato deve responder à seguinte pergunta:

> **Este artefato continuará agregando valor ao projeto daqui a um ano?**

Caso a resposta seja negativa, sua permanência no repositório deve ser reavaliada.

Além disso, aplica-se a seguinte regra:

> **Se um artefato puder ser reconstruído integralmente a partir dos demais arquivos do projeto, e sua perda não comprometer o conhecimento permanente, ele não deve fazer parte da baseline.**

---

# Classificação dos artefatos

## KEEP — Conhecimento Permanente

Devem permanecer versionados.

Incluem:

* código-fonte;
* testes;
* Specifications;
* documentação;
* templates;
* ADRs;
* contratos de API;
* DDL;
* scripts permanentes;
* regras de agentes;
* retrospectivas;
* auditorias finais;
* closure reports;
* feature manifests;
* construction-state final.

Esses artefatos representam a fonte oficial de conhecimento do projeto.

---

## ARCHIVE — Evidência Histórica

Representam registros importantes de auditoria ou rastreabilidade.

Podem permanecer versionados quando agregarem contexto histórico que não possa ser reconstruído.

Exemplos:

* session.md;
* review;
* relatórios finais;
* auditorias;
* retrospectivas.

A manutenção desses arquivos deve ser avaliada periodicamente.

---

## REMOVE — Artefatos Reconstruíveis

Não devem fazer parte da baseline.

Incluem:

* logs;
* artefatos de build;
* runtime;
* caches;
* coverage;
* relatórios temporários;
* arquivos de IDE;
* arquivos locais;
* backups;
* arquivos temporários;
* credenciais;
* variáveis locais.

Esses artefatos podem ser recriados automaticamente.

---

## REVIEW

Artefatos cuja permanência depende de análise humana.

Aplica-se quando existir dúvida legítima sobre seu valor futuro.

---

# Critérios de permanência

Um arquivo deve permanecer no repositório quando atender a pelo menos um dos critérios abaixo:

* representa conhecimento permanente;
* é a fonte oficial da informação (SSOT);
* possui valor histórico relevante;
* não pode ser reconstruído integralmente;
* é necessário para reproduzir o projeto.

---

# Critérios de remoção

Um arquivo deve ser removido da baseline quando:

* for gerado automaticamente;
* puder ser reconstruído;
* representar apenas estado temporário;
* possuir informações locais;
* contiver credenciais;
* representar apenas execução operacional sem valor histórico.

---

# Baseline do projeto

A baseline oficial deve conter apenas artefatos classificados como:

* KEEP
* ARCHIVE

Artefatos classificados como REMOVE jamais devem integrar uma release oficial.

---

# Checklist para Releases

Antes de qualquer release:

* validar `.gitignore`;
* verificar ausência de credenciais;
* confirmar ausência de artefatos de build;
* confirmar ausência de logs;
* revisar arquivos adicionados ao índice do Git;
* executar o processo de Security Readiness Review;
* executar o Repository Readiness Review.

---

# Responsabilidades

Toda inclusão de novo diretório ou novo tipo de artefato deve observar esta política.

Caso exista dúvida sobre a classificação de um artefato, deve ser realizada uma Repository Readiness Review antes da sua inclusão permanente no repositório.

---

# Evolução desta política

Esta política deve evoluir apenas quando evidências obtidas durante a execução de Features demonstrarem a necessidade de alteração.

Alterações não devem ser realizadas por preferência pessoal ou especulação.

Toda mudança deve ser fundamentada em retrospectivas, auditorias ou revisões oficiais do projeto.
