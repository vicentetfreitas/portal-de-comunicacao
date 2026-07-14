# Portal de Comunicação — Specifications

## Propósito

O diretório `specs/` contém as especificações oficiais do Portal de Comunicação.

Toda implementação — código, infraestrutura, integrações e testes — deve derivar exclusivamente das especificações produzidas aqui.

As especificações definem **o que** o sistema deve fazer e **como** deve se comportar. O código materializa essas definições.

---

## Escopo desta camada

Este diretório contém, neste momento, apenas a **Foundation**: a base metodológica do Spec-Driven Development (SDD) adotado pelo projeto.

A Foundation não descreve funcionalidades, domínio, arquitetura ou APIs. Ela define como o projeto utiliza SDD.

---

## Artefatos

```text
specs/
└── foundation/
    ├── principles.md    — Princípios do SDD
    ├── workflow.md      — Fluxo de trabalho
    └── conventions.md   — Convenções e relação com docs/
```

---

## Documentação histórica

O diretório `docs/` contém documentação importada do sistema legado e das fases anteriores do projeto.

`docs/` possui caráter **exclusivamente consultivo**. Não orienta implementação diretamente.

Em caso de conflito entre uma especificação em `specs/` e um documento em `docs/`, prevalece a especificação.

---

## Regra fundamental

```text
Especificação → Implementação
```

O código nunca é a fonte da verdade. Alterações funcionais ou comportamentais começam em `specs/`.
