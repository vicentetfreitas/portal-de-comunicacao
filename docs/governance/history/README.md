# Domain — Índice Arquitetural

| Campo | Valor |
|------|-------|
| Categoria documental | Archive |
| Status | Obsoleto — domínio de conteúdo editorial nunca implementado; responsabilidade transferida ao WordPress (`DEC-CMS-001`, aprovada) |
| Motivo | Zero consumidores confirmados em `specs/features/`, `backend/` ou `database/`; decisão D3, Plano W2, 2026-08-20 |
| Origem | Movido de `specs/domain/README.md` em 2026-08-20 |

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Domain |
| Status | Draft |
| Versão | 1.1 |
| Última atualização | 2026-07-08 |

---

# Objetivo

Este diretório concentra a especificação do domínio de conteúdo do Portal de Comunicação.

A camada define a linguagem ubíqua, os limites conceituais, as responsabilidades e as regras de negócio que orientam todas as demais camadas da especificação.

---

# Escopo

A camada Domain descreve exclusivamente o domínio de conteúdo institucional.

Não define implementação técnica, banco de dados, APIs, interfaces ou infraestrutura.

---

# Como Utilizar Esta Camada

1. Inicie pela visão geral (`00-domain-overview.md`).
2. Consulte o modelo conceitual (`01-content-model.md`) para compreender os conceitos fundamentais.
3. Aprofunde por taxonomia, ciclo de vida, publicação e permissões conforme a necessidade.
4. Utilize o glossário (`06-content-glossary.md`) como fonte oficial da linguagem ubíqua.
5. Em caso de conflito terminológico, prevalece o glossário.

O modelo lógico foi consolidado durante a evolução da arquitetura e encontra-se incorporado ao modelo conceitual desta camada. O modelo físico de implementação pertence à camada de persistência.

---

# Ordem de Leitura

```text
00-domain-overview.md
        ↓
01-content-model.md
        ↓
02-content-taxonomy.md
        ↓
03-content-lifecycle.md
        ↓
04-publication-model.md
        ↓
05-permission-model.md
        ↓
06-content-glossary.md
```

---

# Mapa dos Artefatos

| Artefato | Responsabilidade |
|----------|------------------|
| `00-domain-overview.md` | Visão arquitetural, classificação estratégica, Aggregate, DDD, rastreabilidade |
| `01-content-model.md` | Modelo conceitual e relacionamentos fundamentais |
| `02-content-taxonomy.md` | Taxonomia oficial dos tipos de conteúdo |
| `03-content-lifecycle.md` | Ciclo de vida e estados do conteúdo |
| `04-publication-model.md` | Modelo de publicação, canais e vigência |
| `05-permission-model.md` | Permissões, visibilidade e compartilhamento |
| `06-content-glossary.md` | Linguagem ubíqua oficial |

---

# Fluxo entre Documentos

```text
Overview ──► define visão, limites e classificação
    │
    ▼
Model ──► define conceitos e relacionamentos
    │
    ▼
Taxonomy ──► especializa tipos de Conteúdo
    │
    ▼
Lifecycle ──► define estados e transições
    │
    ▼
Publication ──► define distribuição
    │
    ▼
Permission ──► define autorização e visibilidade
    │
    ▼
Glossary ──► consolida linguagem ubíqua
```

---

# Convenções

| Prefixo | Uso |
|---------|-----|
| PR | Princípios |
| RN | Regras de Negócio |
| DA | Decisões Arquiteturais |
| GL | Glossário |
| TG | Taxonomia |
| CL | Ciclo de Vida |
| PM | Publicação |
| PV | Permissões e Visibilidade |

Identificadores são únicos em toda a camada.

---

# Critérios de Aprovação

A camada será considerada aprovada quando:

- todos os conceitos estiverem rastreados entre os artefatos;
- a linguagem ubíqua for única e consolidada no glossário;
- não houver conceitos duplicados ou ambíguos;
- Aggregate Boundaries e classificação DDD estiverem documentados;
- responsabilidades e eventos de domínio estiverem definidos;
- o domínio puder ser implementado independentemente da tecnologia.

---

# Dependências da Próxima Camada

A camada `specs/features` deverá:

- derivar funcionalidades exclusivamente dos conceitos desta camada;
- preservar a linguagem ubíqua do glossário;
- respeitar Aggregate Boundaries e classificação DDD;
- não redefinir regras de negócio já consolidadas;
- manter rastreabilidade com os identificadores (PR, RN, DA, CL, PM, PV, TG, GL).

---

# Referência Rápida

- Classificação estratégica, DDD e rastreabilidade: `00-domain-overview.md`
- Linguagem ubíqua oficial: `06-content-glossary.md`
