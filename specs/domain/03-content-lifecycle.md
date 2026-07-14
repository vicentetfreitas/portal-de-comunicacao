# Ciclo de Vida do Conteúdo

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Domain |
| Artefato | 03-content-lifecycle.md |
| Status | Draft |
| Versão | 1.0 |
| Dependências | 01-content-model.md, 02-content-taxonomy.md |
| Última atualização | 2026-07-08 |

---

# Objetivo

Este documento define o ciclo de vida oficial dos conteúdos do Portal de Comunicação.

Seu objetivo é estabelecer os estados pelos quais um conteúdo pode passar desde sua criação até sua retirada definitiva de uso, garantindo consistência para todos os tipos de conteúdo definidos na taxonomia.

O ciclo de vida é independente da implementação técnica e será utilizado como referência para:

- regras de negócio;
- APIs;
- banco de dados;
- interfaces do usuário;
- auditoria;
- workflows de publicação.

---

# Escopo

Este documento aplica-se a todos os tipos de conteúdo:

- Documento
- Comunicado
- Notícia
- Aviso
- Postagem

Cada tipo poderá especializar algumas regras, porém todos deverão respeitar o ciclo de vida definido neste documento.

---

# Princípios

## PR-006 — Todo conteúdo possui um ciclo de vida

Nenhum conteúdo existe fora de um estado.

---

## PR-007 — O estado representa a situação de negócio

O estado não representa detalhes técnicos da implementação.

---

## PR-008 — Mudanças de estado são auditáveis

Toda transição deve registrar:

- usuário responsável;
- data e hora;
- estado anterior;
- novo estado;
- justificativa (quando aplicável).

---

## PR-009 — Conteúdo nunca é removido fisicamente

A remoção lógica preserva:

- histórico;
- auditoria;
- referências;
- rastreabilidade.

---

# Estados do Ciclo de Vida

```
RASCUNHO
      │
      ▼
EM_REVISAO
      │
      ▼
APROVADO
      │
      ▼
AGENDADO
      │
      ▼
PUBLICADO
      │
      ▼
EXPIRADO
      │
      ▼
ARQUIVADO
      │
      ▼
EXCLUIDO
```

Algumas transições poderão ser opcionais, conforme o tipo de conteúdo.

---

# Definição dos Estados

## RASCUNHO

Conteúdo em elaboração.

Características:

- não visível ao público;
- totalmente editável;
- pode ser descartado;
- pode possuir versões intermediárias.

---

## EM_REVISAO

Conteúdo aguardando validação.

Características:

- bloqueado para alterações comuns;
- revisores autorizados podem solicitar ajustes;
- ainda não publicado.

---

## APROVADO

Conteúdo aprovado para publicação.

Características:

- conteúdo congelado;
- pronto para publicação;
- pode ser publicado imediatamente;
- pode ser agendado.

---

## AGENDADO

Conteúdo aprovado aguardando data de publicação.

Características:

- invisível ao público;
- publicação automática;
- edição controlada.

---

## PUBLICADO

Conteúdo disponível aos usuários.

Características:

- pesquisável;
- compartilhável;
- visível conforme permissões;
- sujeito às regras de vigência.

---

## EXPIRADO

Conteúdo cuja vigência terminou.

Características:

- deixa de aparecer como ativo;
- permanece consultável conforme regras;
- pode ser republicado.

---

## ARQUIVADO

Conteúdo mantido apenas para consulta histórica.

Características:

- não recebe novas publicações;
- preserva histórico completo;
- permanece auditável.

---

## EXCLUIDO

Representa exclusão lógica.

Características:

- invisível para usuários comuns;
- preserva auditoria;
- preserva referências;
- pode ser restaurado conforme política.

---

# Fluxo Principal

```
Criar

↓

RASCUNHO

↓

EM_REVISAO

↓

APROVADO

↓

PUBLICADO

↓

EXPIRADO

↓

ARQUIVADO
```

---

# Fluxos Alternativos

## Publicação Imediata

```
RASCUNHO

↓

APROVADO

↓

PUBLICADO
```

---

## Publicação Agendada

```
RASCUNHO

↓

APROVADO

↓

AGENDADO

↓

PUBLICADO
```

---

## Reprovação

```
EM_REVISAO

↓

RASCUNHO
```

---

## Atualização

```
PUBLICADO

↓

RASCUNHO

↓

EM_REVISAO

↓

PUBLICADO
```

A atualização gera uma nova versão do conteúdo, preservando o histórico.

---

# Transições Permitidas

| Origem | Destino |
|---------|----------|
| RASCUNHO | EM_REVISAO |
| RASCUNHO | APROVADO |
| RASCUNHO | EXCLUIDO |
| EM_REVISAO | RASCUNHO |
| EM_REVISAO | APROVADO |
| APROVADO | AGENDADO |
| APROVADO | PUBLICADO |
| AGENDADO | PUBLICADO |
| PUBLICADO | EXPIRADO |
| PUBLICADO | RASCUNHO |
| EXPIRADO | PUBLICADO |
| EXPIRADO | ARQUIVADO |
| ARQUIVADO | PUBLICADO* |
| ARQUIVADO | EXCLUIDO |

> A republicação de conteúdo arquivado depende das regras específicas de negócio.

---

# Versionamento

O ciclo de vida é independente do versionamento.

Cada alteração significativa poderá gerar uma nova versão.

Exemplo:

```
Documento

Versão 1

↓

Publicado

↓

Versão 2

↓

Publicado

↓

Versão 3
```

O histórico deve permanecer íntegro.

---

# Vigência

A vigência controla quando um conteúdo deve permanecer disponível.

Um conteúdo poderá possuir:

- data inicial;
- data final.

Ao atingir a data final, poderá ser automaticamente marcado como **EXPIRADO**.

---

# Publicação

A publicação depende do estado.

| Estado | Publicação Permitida |
|----------|---------------------|
| RASCUNHO | Não |
| EM_REVISAO | Não |
| APROVADO | Sim |
| AGENDADO | Automática |
| PUBLICADO | Já publicado |
| EXPIRADO | Não |
| ARQUIVADO | Não |
| EXCLUIDO | Não |

---

# Regras Gerais

## CL-001

Todo conteúdo nasce em **RASCUNHO**.

---

## CL-002

Somente conteúdos aprovados podem ser publicados.

---

## CL-003

Conteúdos publicados permanecem auditáveis durante todo seu ciclo de vida.

---

## CL-004

Toda mudança de estado deve ser registrada em auditoria.

---

## CL-005

A exclusão é sempre lógica.

---

## CL-006

O histórico de estados deve ser preservado.

---

## CL-007

A alteração de um conteúdo publicado gera uma nova versão.

A versão anteriormente publicada permanece preservada.

---

## CL-008

Cada tipo de conteúdo poderá restringir transições específicas, mas nunca ampliar estados além dos definidos neste documento sem atualização do modelo conceitual.

---

# Responsabilidades

O ciclo de vida não define:

- quem aprova;
- quem publica;
- quem revisa.

Essas responsabilidades serão definidas no modelo de permissões.

---

# Relação com Outros Artefatos

| Artefato | Responsabilidade |
|----------|------------------|
| `00-domain-overview.md` | Visão arquitetural e rastreabilidade |
| `01-content-model.md` | Conceitos fundamentais |
| 02-content-taxonomy.md | Tipos de conteúdo |
| 04-publication-model.md | Publicação e canais |
| 05-permission-model.md | Aprovação e permissões |
| 06-content-glossary.md | Linguagem ubíqua |

---

# Decisões Arquiteturais

## DA-008

Todos os tipos de conteúdo compartilham o mesmo ciclo de vida base.

---

## DA-009

O fluxo de publicação é desacoplado do tipo de conteúdo.

---

## DA-010

Versionamento e ciclo de vida são conceitos independentes.

---

## DA-011

Nenhuma transição de estado poderá remover o histórico do conteúdo.

---

# Considerações para Implementação

Este modelo foi definido para permitir que o backend implemente um mecanismo único de gestão do ciclo de vida para qualquer tipo de conteúdo.

A especificação conceitual do **Content Service** e dos eventos de ciclo de vida encontra-se em `00-domain-overview.md`.

Cada especialização (Documento, Comunicado, Notícia, Aviso e Postagem) poderá acrescentar validações específicas, porém todas deverão utilizar os mesmos estados, regras de transição e mecanismos de auditoria definidos neste documento.