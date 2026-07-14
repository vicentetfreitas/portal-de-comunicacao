# Golden Template da Camada Features

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Features |
| Artefato | GOLDEN_TEMPLATE.md |
| Status | Approved |
| Versão | 1.0 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Este documento estabelece a **Feature de Referência (Golden Template)** da camada `specs/features`.

Seu objetivo é definir o padrão oficial de organização, estrutura, qualidade e governança que deverá ser seguido por todas as futuras Features do Portal de Comunicação.

Este documento **não é um template reutilizável**.

Ele define qual Feature representa a referência oficial da camada.

---

# Feature de Referência

A Feature oficial de referência do projeto é:

```text
FT-AUTH

Authentication
```

Todos os novos módulos deverão utilizar esta Feature como base para estruturação de seus artefatos.

---

# Finalidade

A Golden Template existe para garantir:

- padronização entre Features;
- consistência arquitetural;
- rastreabilidade;
- reutilização da estrutura documental;
- facilidade de manutenção;
- previsibilidade durante a implementação.

---

# Estrutura Oficial

Toda nova Feature deverá possuir a seguinte estrutura.

```text
feature/

├── specification.md
├── use-cases.md
├── api.md
├── acceptance-tests.md
├── tasks.md
└── decisions.md
```

## Artefatos Obrigatórios

| Artefato | Obrigatório |
|-----------|-------------|
| specification.md | Sim |
| tasks.md | Sim |

## Artefatos Condicionais

| Artefato | Quando utilizar |
|-----------|-----------------|
| use-cases.md | Quando houver interação relevante |
| api.md | Quando houver APIs |
| acceptance-tests.md | Quando houver comportamento verificável |
| decisions.md | Quando houver decisões específicas da Feature |

---

# Padrão de Identificação

Toda Feature deverá possuir um identificador único.

Formato:

```text
FT-<NOME>
```

Exemplos:

```text
FT-AUTH

FT-CONTENT

FT-DOCUMENT

FT-NOTIFICATION

FT-SEARCH

FT-USERS
```

---

# Padrão de Identificadores

Todos os artefatos deverão utilizar identificadores consistentes.

| Tipo | Prefixo |
|------|----------|
| Feature | FT |
| Requisito Funcional | RF |
| Regra de Negócio | RN |
| Requisito Não Funcional | RNF |
| Caso de Uso | UC |
| API | API |
| Teste de Aceitação | AC |
| Task | TASK |
| Decisão | DA |

Exemplo:

```text
FT-AUTH-RF-001

FT-AUTH-RN-001

FT-AUTH-UC-001

FT-AUTH-API-001

FT-AUTH-AC-001

FT-AUTH-TASK-BE-001
```

---

# Estrutura de Rastreabilidade

Toda Feature deverá manter rastreabilidade completa.

```text
Feature

↓

RF

↓

RN

↓

UC

↓

API

↓

Acceptance Tests

↓

Tasks

↓

Implementação
```

Nenhum requisito poderá ficar sem implementação.

Nenhuma implementação poderá existir sem origem.

---

# Critérios de Qualidade

Toda Feature deverá possuir:

- escopo claramente definido;
- objetivos explícitos;
- requisitos funcionais;
- regras de negócio;
- requisitos não funcionais;
- casos de uso;
- contratos de API;
- testes de aceitação;
- tarefas técnicas;
- rastreabilidade ponta a ponta.

---

# Critérios de Governança

Uma Feature somente poderá ser considerada **Approved** quando:

- todos os artefatos obrigatórios existirem;
- todos os artefatos estiverem consistentes;
- não existirem conflitos internos;
- a rastreabilidade estiver completa;
- a auditoria da Feature estiver concluída.

---

# Evolução da Golden Template

A Feature Authentication deverá evoluir continuamente sempre que forem identificadas melhorias estruturais.

Essas melhorias deverão ser avaliadas antes de serem incorporadas às demais Features.

A Golden Template representa a baseline arquitetural da camada.

---

# Processo de Criação de Novas Features

Toda nova Feature deverá seguir o processo abaixo.

```text
Criar diretório

↓

Copiar estrutura da Golden Template

↓

Adaptar Specification

↓

Adaptar Use Cases

↓

Adaptar API

↓

Adaptar Acceptance Tests

↓

Adaptar Tasks

↓

Criar Decisions (quando necessário)

↓

Auditoria

↓

Correções

↓

Approved

↓

Implementação
```

---

# Regras de Evolução

Nenhuma nova Feature poderá:

- alterar a estrutura oficial da camada;
- remover artefatos obrigatórios;
- redefinir a linguagem ubíqua;
- criar novos conceitos de domínio.

Alterações estruturais deverão ocorrer primeiro na Golden Template e, posteriormente, serem propagadas para as demais Features.

---

# Relação com a Camada Domain

Todas as Features deverão derivar exclusivamente dos conceitos definidos em:

```text
specs/domain/
```

A camada Features não possui autoridade para alterar o domínio.

---

# Relação com Templates

Os modelos reutilizáveis do projeto permanecem em:

```text
specs/templates/
```

Este documento apenas define a Feature de referência da camada.

---

# Critérios para Promoção de uma Nova Golden Template

Uma Feature somente poderá substituir a Golden Template quando:

- apresentar evolução significativa da estrutura;
- for aprovada em auditoria arquitetural;
- mantiver compatibilidade com as demais Features;
- não introduzir regressões na governança da camada.

---

# Estado Atual

| Item | Situação |
|------|----------|
| Feature de Referência | FT-AUTH |
| Estrutura da Camada | Congelada |
| Governança | Ativa |
| Rastreabilidade | Obrigatória |
| Auditoria | Obrigatória |

---

# Decisão Arquitetural

A partir da versão **1.0** deste documento, a Feature **FT-AUTH (Authentication)** passa a ser oficialmente a **Feature de Referência (Golden Template)** da camada `specs/features`.

Toda nova Feature deverá preservar sua estrutura, organização, rastreabilidade e critérios de qualidade, adaptando apenas o conteúdo funcional específico.