# CRUD Feature Template

**Versão:** 1.1  
**Status:** STABLE  
**Owner:** Engineering Framework

---

# Objetivo

Este diretório contém o template oficial para especificação de Features CRUD do Portal de Comunicação.

Seu objetivo é padronizar a criação de novas Features de domínio, garantindo consistência entre especificação, implementação, testes e auditoria.

Este template constitui a referência oficial para Features CRUD do projeto.

---

# Quando utilizar

Este template deverá ser utilizado para qualquer Feature cujo objetivo principal seja gerenciar uma entidade de domínio.

Exemplos:

- FT-AREA
- FT-EQUIPE
- FT-COLABORADOR
- FT-SINGULAR
- FT-DOCUMENTO
- FT-CAMPANHA
- FT-CATEGORIA

Não deverá ser utilizado para Features de:

- autenticação;
- integrações;
- workflows;
- notificações;
- processamento em lote;
- relatórios;
- funcionalidades que não representem um CRUD de domínio.

---

# Estrutura

```text
crud-feature/
├── README.md
├── specification.md
├── use-cases.md
├── api.md
├── acceptance-tests.md
├── tasks.md
└── traceability.md
```

Cada artefato possui uma responsabilidade única.

---

# Responsabilidade dos Artefatos

| Artefato | Responsabilidade |
|----------|------------------|
| specification.md | Define o comportamento esperado da Feature. |
| use-cases.md | Descreve os fluxos funcionais da Feature. |
| api.md | Define exclusivamente o contrato funcional da API. |
| acceptance-tests.md | Define os critérios de aceitação da Feature. |
| tasks.md | Decompõe a Feature em unidades de implementação rastreáveis. |
| traceability.md | Consolida a rastreabilidade entre todos os artefatos. |

Nenhum artefato deverá assumir responsabilidades pertencentes a outro.

---

# Fluxo de Utilização

Toda nova Feature CRUD deverá seguir obrigatoriamente a sequência abaixo:

```text
Specification
        ↓
Use Cases
        ↓
API Contract
        ↓
Acceptance Tests
        ↓
Tasks
        ↓
Traceability
        ↓
Readiness Review
        ↓
Construction
        ↓
Code Review
        ↓
Audit
        ↓
Closure
```

A implementação somente poderá iniciar após a conclusão do Readiness Review.

---

# Variáveis Padronizadas

Todos os templates utilizam placeholders padronizados.

| Variável | Descrição |
|-----------|-----------|
| `${FEATURE_ID}` | Identificador da Feature (ex.: FT-AREA) |
| `${FEATURE_NAME}` | Nome da Feature |
| `${DOMAIN}` | Prefixo do domínio (AREA, EQUIPE...) |
| `${ENTITY_NAME}` | Nome da entidade |
| `${RESOURCE_NAME}` | Nome do recurso REST |
| `${PRIMARY_KEY}` | Identificador do recurso |
| `${API_BASE_PATH}` | Caminho base da API |

Não utilizar outros formatos de placeholders.

---

# Rastreabilidade

Toda Feature deverá manter rastreabilidade completa.

```text
RF
        ↓
RN
        ↓
UC
        ↓
API
        ↓
AT
        ↓
TK
        ↓
Construction
        ↓
Código
```

A consolidação oficial da rastreabilidade é responsabilidade de `traceability.md`.

Nenhum requisito poderá permanecer sem rastreabilidade.

---

# Dependências

Os templates reutilizam padrões corporativos definidos em outras camadas.

| Camada | Responsabilidade |
|---------|------------------|
| docs/implementation | Padrões técnicos corporativos |
| specs/templates | Estrutura de especificação |
| specs/foundation/feature-yaml.md | Contrato SSOT da Feature |
| construction | Execução da implementação |

Os templates não deverão duplicar conteúdo já definido nessas camadas.

---

# Regras Gerais

Toda Feature deverá:

- utilizar os templates sem alteração estrutural;
- manter os identificadores padronizados (RF, RN, RNF, UC, AT, TK);
- preservar a rastreabilidade entre todos os artefatos;
- reutilizar exclusivamente os padrões definidos em `docs/implementation`;
- registrar decisões arquiteturais fora desta camada, conforme a governança do projeto.

---

# Evolução do Template

Mudanças neste template deverão observar as seguintes regras:

- Correções de conteúdo ou padronização incrementam a versão menor (ex.: 1.0 → 1.1).
- Alterações estruturais ou incompatíveis incrementam a versão principal (ex.: 1.x → 2.0).
- Nenhuma Feature em desenvolvimento deverá alterar os templates diretamente.
- Evoluções deverão preservar compatibilidade sempre que possível.

---

# Status

**Versão atual:** 1.1

Este template encontra-se **STABLE** e constitui a implementação de referência para todas as Features CRUD do Portal de Comunicação.
