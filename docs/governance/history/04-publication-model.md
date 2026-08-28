# Modelo de Publicação

| Campo | Valor |
|------|-------|
| Categoria documental | Archive |
| Status | Obsoleto — canais/vigência de publicação de conteúdo editorial, nativo do WordPress (`DEC-CMS-001`, aprovada) |
| Motivo | Zero consumidores confirmados; decisão D3, Plano W2, 2026-08-20 |
| Origem | Movido de `specs/domain/04-publication-model.md` em 2026-08-20 |

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Domain |
| Artefato | 04-publication-model.md |
| Status | Draft |
| Versão | 1.0 |
| Dependências | 01-content-model.md, 02-content-taxonomy.md, 03-content-lifecycle.md |
| Última atualização | 2026-07-08 |

---

# Objetivo

Este documento define o modelo conceitual de publicação do Portal de Comunicação.

Seu objetivo é estabelecer como um conteúdo é disponibilizado aos usuários, separando claramente o conceito de **Conteúdo** do conceito de **Publicação**.

Um conteúdo representa a informação.

Uma publicação representa a disponibilização dessa informação em um ou mais canais.

---

# Escopo

Este documento aplica-se a todos os tipos de conteúdo.

- Documento
- Comunicado
- Notícia
- Aviso
- Postagem

Independentemente do tipo, o processo de publicação deverá seguir as regras aqui estabelecidas.

---

# Princípios

## PR-010

Conteúdo e Publicação são conceitos distintos.

---

## PR-011

Um conteúdo pode possuir nenhuma, uma ou várias publicações.

---

## PR-012

Uma publicação referencia exatamente um conteúdo.

---

## PR-013

A publicação nunca altera o conteúdo.

---

## PR-014

A publicação pode ser encerrada sem alterar o conteúdo.

---

# Modelo Conceitual

```
                 Conteúdo

                     │

        ┌────────────┴────────────┐

        │                         │

 Publicação A              Publicação B

        │                         │

   Portal Web              Aplicativo

                                  │

                           Push Notification
```

O conteúdo permanece único.

Cada publicação representa uma estratégia de distribuição.

---

# Conceitos

## Conteúdo

Representa a informação institucional.

É responsável por:

- autoria;
- classificação;
- auditoria;
- versionamento;
- permissões;
- visibilidade.

---

## Publicação

Representa a disponibilização de um conteúdo.

É responsável por:

- canal;
- período;
- prioridade;
- situação;
- distribuição.

---

## Canal

Representa onde o conteúdo será disponibilizado.

Exemplos:

- Portal Web
- Aplicativo Mobile
- Intranet
- API
- TV Corporativa
- Integrações futuras

Os canais não alteram o conteúdo.

---

# Estrutura Conceitual

```
Conteúdo

↓

Publicação

↓

Canal

↓

Usuário
```

---

# Capacidades da Publicação

Toda publicação poderá possuir:

- situação;
- data de início;
- data de término;
- prioridade;
- ordenação;
- canais;
- público-alvo;
- auditoria.

---

# Estados da Publicação

A publicação possui um ciclo próprio.

```
CRIADA

↓

AGENDADA

↓

ATIVA

↓

SUSPENSA

↓

ENCERRADA
```

---

## CRIADA

Publicação configurada, mas ainda não iniciada.

---

## AGENDADA

Aguardando data programada.

---

## ATIVA

Disponível aos usuários.

---

## SUSPENSA

Temporariamente indisponível.

Não altera o conteúdo.

---

## ENCERRADA

Publicação finalizada.

O conteúdo permanece existente.

---

# Vigência

Cada publicação poderá definir:

- início;
- término.

Exemplo

```
Comunicado

↓

Publicação

01/08

↓

31/08
```

Após esse período a publicação será encerrada automaticamente.

---

# Prioridade

A prioridade determina a ordem de apresentação.

Valores sugeridos:

| Prioridade | Utilização |
|------------|------------|
| Muito Alta | Alertas críticos |
| Alta | Comunicados |
| Normal | Notícias |
| Baixa | Conteúdos informativos |

A prioridade não altera o conteúdo.

---

# Público-Alvo

Uma publicação poderá ser direcionada para públicos específicos.

Exemplos:

- Federação
- Singular
- Área
- Equipe
- Cargo
- Perfil
- Grupo
- Usuário específico

A definição detalhada será tratada no modelo de permissões.

---

# Canais de Publicação

O Portal deverá permitir múltiplos canais.

Exemplo:

```
Conteúdo

↓

Portal

↓

Aplicativo

↓

API

↓

TV Corporativa

↓

Integrações
```

Novos canais poderão ser adicionados futuramente sem alteração do modelo conceitual.

---

# Publicação Imediata

```
Conteúdo

↓

Aprovado

↓

Publicado

↓

Publicação Ativa
```

---

# Publicação Agendada

```
Conteúdo

↓

Aprovado

↓

Agendado

↓

Publicação Agendada

↓

Publicação Ativa
```

---

# Múltiplas Publicações

Um mesmo conteúdo poderá possuir mais de uma publicação.

Exemplo:

```
Manual do Beneficiário

├── Portal
├── Aplicativo
└── API
```

Cada publicação possui sua própria configuração.

---

# Relação com Notificações

A publicação poderá originar notificações.

```
Conteúdo

↓

Publicação

↓

Evento

↓

Notificação
```

A notificação não faz parte da publicação.

Ela representa uma consequência da publicação.

---

# Eventos de Publicação

Uma publicação poderá gerar eventos de domínio.

Os eventos `PublicacaoCriada`, `PublicacaoEncerrada` e `ConteudoPublicado` estão especificados em `00-domain-overview.md`, incluindo descrição, disparo, consumidores e impacto no domínio.

Exemplos de situações que originam eventos:

- publicada;
- atualizada;
- encerrada;
- suspensa;
- reativada.

Esses eventos poderão ser utilizados por integrações futuras.

---

# Regras Gerais

## PM-001

Todo conteúdo publicado deverá possuir ao menos uma publicação.

---

## PM-002

Uma publicação referencia exatamente um conteúdo.

---

## PM-003

O encerramento de uma publicação não exclui o conteúdo.

---

## PM-004

Um conteúdo poderá possuir várias publicações simultaneamente.

---

## PM-005

Cada publicação possui seu próprio período de vigência.

---

## PM-006

Os canais são independentes entre si.

Falhas em um canal não deverão impedir os demais.

---

## PM-007

A publicação somente poderá ser criada para conteúdos aprovados.

---

## PM-008

Toda publicação deverá ser auditada.

---

## PM-009

A alteração de uma publicação não gera nova versão do conteúdo.

---

## PM-010

O histórico de publicações deverá ser preservado.

---

# Responsabilidades

## Conteúdo

Responsável por:

- informação;
- autoria;
- classificação;
- anexos;
- auditoria;
- versionamento.

---

## Publicação

Responsável por:

- distribuição;
- canais;
- vigência;
- prioridade;
- público-alvo.

---

## Notificação

Responsável por:

- informar usuários;
- consumir eventos de publicação;
- registrar entregas.

---

# Relação com Outros Artefatos

| Artefato | Responsabilidade |
|----------|------------------|
| `00-domain-overview.md` | Visão arquitetural e rastreabilidade |
| `01-content-model.md` | Conceitos fundamentais |
| 02-content-taxonomy.md | Tipos de conteúdo |
| 03-content-lifecycle.md | Estados do conteúdo |
| 05-permission-model.md | Visibilidade e autorização |
| 06-content-glossary.md | Linguagem ubíqua |

---

# Decisões Arquiteturais

## DA-012

Publicação é uma capacidade transversal do conceito **Conteúdo**.

---

## DA-013

A publicação é desacoplada da estrutura organizacional.

---

## DA-014

O mesmo conteúdo pode possuir múltiplas publicações.

---

## DA-015

A publicação não altera nem versiona o conteúdo.

---

## DA-016

Notificações são geradas por eventos de publicação e não fazem parte do modelo de publicação.

---

# Considerações para Implementação

O backend deverá tratar a publicação como um serviço de domínio reutilizável, independente do tipo de conteúdo.

A especificação conceitual do **Publication Service** encontra-se em `00-domain-overview.md` (objetivo, responsabilidades, entradas, saídas e eventos gerados).

Esse serviço será responsável por:

- validar pré-condições de publicação;
- controlar vigência;
- gerenciar canais;
- registrar auditoria;
- emitir eventos de domínio;
- disponibilizar informações para busca e distribuição.

Dessa forma, **Documento**, **Comunicado**, **Notícia**, **Aviso** e **Postagem** utilizarão o mesmo mecanismo de publicação, reduzindo duplicação de código e garantindo comportamento uniforme em toda a plataforma.