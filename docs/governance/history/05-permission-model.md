# Modelo de Permissões e Visibilidade

| Campo | Valor |
|------|-------|
| Categoria documental | Archive |
| Status | Obsoleto — modelo de permissões dependente do domínio de conteúdo (01–04), hoje transferido ao WordPress (`DEC-CMS-001`) |
| Motivo | Princípios PR-015–018 já cobertos, com mais detalhe, por `docs/domain/09-business-rules.md` (BR-018, BR-019, BR-024) e `docs/architecture/06-security-architecture.md`; decisão D3, Plano W2, 2026-08-20 |
| Origem | Movido de `specs/domain/05-permission-model.md` em 2026-08-20 |

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Domain |
| Artefato | 05-permission-model.md |
| Status | Draft |
| Versão | 1.0 |
| Dependências | 01-content-model.md, 02-content-taxonomy.md, 03-content-lifecycle.md, 04-publication-model.md |
| Última atualização | 2026-07-08 |

---

# Objetivo

Este documento define o modelo conceitual de autorização, visibilidade e compartilhamento do Portal de Comunicação.

Seu objetivo é estabelecer **quem pode administrar um conteúdo**, **quem pode visualizá-lo** e **como o acesso é concedido**, preservando o desacoplamento entre o domínio de conteúdo e o mecanismo de autenticação.

Este documento **não define**:

- autenticação;
- login;
- OAuth2;
- OpenID Connect;
- Keycloak;
- JWT;
- Spring Security.

Esses assuntos pertencem à arquitetura da solução.

---

# Princípios

## PR-015

Autenticação não é autorização.

---

## PR-016

Autorização não é visibilidade.

---

## PR-017

Visibilidade não é compartilhamento.

---

## PR-018

Todo acesso deve ser auditável.

---

## PR-019

Permissões pertencem ao domínio de negócio.

Elas não devem depender da tecnologia utilizada.

---

# Modelo Conceitual

```
Usuário

↓

Possui

↓

Papéis

↓

Permissões

↓

Executa

↓

Ações

↓

Sobre

↓

Conteúdo

↓

Controlado por

↓

Visibilidade

↓

Complementado por

↓

Compartilhamentos
```

---

# Conceitos Fundamentais

## Usuário

Pessoa autenticada que interage com o Portal.

O usuário poderá:

- criar conteúdos;
- editar conteúdos;
- revisar;
- aprovar;
- publicar;
- compartilhar;
- consultar conteúdos.

---

## Papel

Representa uma responsabilidade institucional.

Exemplos:

- Administrador
- Editor
- Revisor
- Publicador
- Colaborador
- Leitor

Papéis representam funções.

Eles não representam pessoas.

---

## Permissão

Representa uma capacidade concedida.

Exemplos:

- criar;
- editar;
- revisar;
- aprovar;
- publicar;
- arquivar;
- excluir;
- compartilhar;
- visualizar.

Permissões poderão ser reutilizadas em todo o sistema.

---

## Visibilidade

Define quem pode visualizar determinado conteúdo.

A visibilidade não concede permissão administrativa.

Ela apenas controla quem pode consumir o conteúdo.

---

## Compartilhamento

Representa concessões adicionais de acesso.

Permite ampliar ou restringir o alcance de um conteúdo.

---

# Capacidades Administrativas

O Portal reconhecerá as seguintes ações administrativas.

| Ação | Descrição |
|------|-----------|
| Criar | Produzir conteúdo |
| Editar | Alterar conteúdo |
| Revisar | Validar conteúdo |
| Aprovar | Autorizar publicação |
| Publicar | Tornar disponível |
| Arquivar | Encerrar ciclo |
| Excluir | Exclusão lógica |
| Compartilhar | Conceder acesso |
| Restaurar | Reativar conteúdo |

---

# Visibilidade

A visibilidade determina quem poderá consultar um conteúdo publicado.

Ela poderá ser definida em diferentes níveis.

```
Pública

↓

Institucional

↓

Organizacional

↓

Grupo

↓

Individual
```

---

## Pública

Conteúdo disponível para todos os usuários autorizados pelo Portal.

---

## Institucional

Disponível para toda a organização.

---

## Organizacional

Disponível apenas para estruturas específicas.

Exemplos:

- Federação
- Singular
- Área
- Equipe

---

## Grupo

Disponível para grupos específicos.

Exemplos:

- Gestores
- RH
- Jurídico
- Compliance

---

## Individual

Disponível apenas para usuários explicitamente definidos.

---

# Compartilhamento

O compartilhamento amplia o acesso ao conteúdo.

Ele poderá ser realizado para:

- usuários;
- grupos;
- equipes;
- áreas;
- singulares;
- federação.

O compartilhamento nunca altera o conteúdo.

---

# Hierarquia de Escopo

```
Federação

↓

Singular

↓

Área

↓

Equipe

↓

Grupo

↓

Usuário
```

A utilização dessa hierarquia será definida conforme as regras de negócio de cada conteúdo.

---

# Herança de Permissões

As permissões poderão ser herdadas.

Exemplo:

```
Administrador

↓

Editor

↓

Leitor
```

A implementação da herança será definida posteriormente.

O modelo conceitual apenas estabelece sua existência.

---

# Regras de Administração

## Criação

Necessita permissão:

```
CRIAR_CONTEUDO
```

---

## Edição

Necessita:

```
EDITAR_CONTEUDO
```

---

## Aprovação

Necessita:

```
APROVAR_CONTEUDO
```

---

## Publicação

Necessita:

```
PUBLICAR_CONTEUDO
```

---

## Compartilhamento

Necessita:

```
COMPARTILHAR_CONTEUDO
```

---

# Modelo de Compartilhamento

```
Conteúdo

↓

Visibilidade

↓

Compartilhamentos

↓

Usuários Aptos
```

Primeiro aplica-se a visibilidade.

Depois são avaliados os compartilhamentos.

---

# Prioridade das Regras

Em caso de conflito.

```
Exclusão explícita

↓

Permissão explícita

↓

Compartilhamento

↓

Visibilidade

↓

Permissão herdada
```

Essa ordem garante comportamento previsível.

---

# Auditoria

Toda alteração deverá registrar:

- responsável;
- data;
- ação executada;
- justificativa (quando aplicável).

Também deverão ser auditadas:

- mudanças de visibilidade;
- compartilhamentos;
- revogações;
- alterações de permissões.

---

# Regras Gerais

## PV-001

Todo conteúdo possui uma configuração de visibilidade.

---

## PV-002

Todo conteúdo possui um responsável.

---

## PV-003

Toda ação administrativa depende de permissão.

---

## PV-004

Compartilhamentos não substituem permissões administrativas.

---

## PV-005

Usuários sem visibilidade nunca poderão visualizar um conteúdo.

---

## PV-006

Permissões administrativas independem da visibilidade.

Um administrador pode administrar um conteúdo mesmo que ele não esteja publicado.

---

## PV-007

Toda alteração de permissão deverá ser auditada.

---

## PV-008

Toda alteração de visibilidade deverá ser auditada.

---

## PV-009

Todo compartilhamento deverá possuir origem identificável.

---

## PV-010

O histórico de compartilhamentos deverá ser preservado.

---

# Relação com Publicação

```
Conteúdo

↓

Publicado

↓

Visibilidade

↓

Compartilhamentos

↓

Usuário

↓

Acesso
```

A publicação disponibiliza.

A visibilidade limita.

O compartilhamento complementa.

---

# Relação com Outros Artefatos

| Artefato | Responsabilidade |
|----------|------------------|
| `00-domain-overview.md` | Visão arquitetural e rastreabilidade |
| `01-content-model.md` | Conceitos fundamentais |
| 02-content-taxonomy.md | Tipos de conteúdo |
| 03-content-lifecycle.md | Estados do conteúdo |
| 04-publication-model.md | Distribuição |
| 06-content-glossary.md | Linguagem ubíqua |

---

# Decisões Arquiteturais

## DA-017

Autenticação é responsabilidade da infraestrutura.

---

## DA-018

Autorização pertence ao domínio.

---

## DA-019

Visibilidade é independente das permissões administrativas.

---

## DA-020

Compartilhamento complementa a visibilidade.

---

## DA-021

Toda decisão de acesso deve ser auditável.

---

## DA-022

O modelo de permissões deverá ser reutilizado por todos os tipos de conteúdo.

---

# Considerações para Implementação

O backend deverá implementar um mecanismo único de autorização para todos os tipos de conteúdo, baseado em capacidades compartilhadas do conceito **Conteúdo**.

A especificação conceitual dos serviços **Permission Service**, **Visibility Service** e **Sharing Service** encontra-se em `00-domain-overview.md`.

As decisões de autorização deverão considerar, nesta ordem:

1. Estado do conteúdo (conforme o ciclo de vida).
2. Permissões do usuário para executar a ação.
3. Visibilidade configurada para o conteúdo.
4. Compartilhamentos específicos concedidos.
5. Regras de negócio do tipo de conteúdo (quando existirem).

Essa abordagem mantém o domínio desacoplado da tecnologia de autenticação e permite que o mesmo modelo seja utilizado com Keycloak, Spring Security ou qualquer outro provedor de identidade, preservando a consistência das regras de negócio do Portal de Comunicação.