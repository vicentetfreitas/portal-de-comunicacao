# Known Issues

## Documento

```text
docs/discovery/09-known-issues.md
```

---

# Objetivo

Consolidar problemas conhecidos identificados durante a fase Discovery.

Este documento não registra dívidas técnicas estruturais.

Seu objetivo é registrar:

* falhas conhecidas;
* limitações operacionais;
* comportamentos inconsistentes;
* dependências frágeis;
* restrições temporárias;
* riscos observados em produção.

As informações aqui documentadas devem servir como insumo para:

* Architecture
* Solution Design
* Implementation
* Roadmap de correções

---

# Escopo

Inclui:

* problemas funcionais;
* problemas operacionais;
* problemas de integração;
* limitações arquiteturais observadas;
* comportamentos não documentados.

Não inclui:

* backlog futuro;
* novas funcionalidades;
* decisões arquiteturais;
* melhorias desejáveis.

---

# Classificação

| Severidade | Descrição                                  |
| ---------- | ------------------------------------------ |
| Crítica    | Impede operação ou causa indisponibilidade |
| Alta       | Impacta funcionalidade principal           |
| Média      | Impacta produtividade ou manutenção        |
| Baixa      | Inconsistência sem impacto relevante       |

---

# Known Issues

## KI-001

### Título

Dependência forte do ambiente legado

### Categoria

Arquitetura

### Severidade

Alta

### Descrição

Capacidades do Portal dependem de componentes externos ainda não migrados.

### Impacto

Evolução limitada da solução.

### Mitigação

Estratégia documentada em:

```text
09-migration-strategy.md
```

---

## KI-002

### Título

Integrações sem contratos formais

### Categoria

Integração

### Severidade

Alta

### Descrição

Existem integrações cuja documentação contratual é incompleta ou inexistente.

### Impacto

Risco de regressão e falhas de integração.

### Mitigação

Formalização em:

```text
06-integration-contracts.md
```

---

## KI-003

### Título

Dependência operacional do provedor de identidade

### Categoria

Segurança

### Severidade

Alta

### Descrição

Autenticação depende integralmente do provedor corporativo.

### Impacto

Falhas no provedor impedem acesso à plataforma.

### Mitigação

Monitoramento e observabilidade.

---

## KI-004

### Título

Fluxos de autorização incompletos

### Categoria

Negócio

### Severidade

Alta

### Descrição

Existem cenários ainda não completamente definidos para concessão, revogação e expiração de permissões.

### Impacto

Possíveis inconsistências de acesso.

### Mitigação

Resolução das Open Questions.

---

## KI-005

### Título

Coexistência temporária de soluções

### Categoria

Migração

### Severidade

Média

### Descrição

Sistemas novos e legados coexistirão durante o processo de transição.

### Impacto

Duplicidade operacional.

### Mitigação

Execução do roadmap de migração.

---

## Critério de Encerramento

Um Known Issue pode ser encerrado quando:

* a causa raiz for eliminada;
* existir evidência de validação;
* o impacto deixar de existir;
* a documentação relacionada for atualizada.

---

# Conclusão

Os problemas registrados neste documento não impedem a evolução da arquitetura.

Entretanto, devem ser considerados durante planejamento, implementação e migração da solução.
