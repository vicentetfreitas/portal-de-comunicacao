# Delivery Governance

## Documento

```text
docs/implementation/12-delivery-governance.md
```

---

# Objetivo

Definir as regras de governança para execução da camada Implementation.

Este documento estabelece critérios de qualidade, fluxo de desenvolvimento, estratégia de branches e critérios de entrega.

---

# Escopo

Aplica-se a:

* Backend
* Frontend
* Infraestrutura
* Banco de Dados
* Observabilidade
* Segurança

---

# Definition of Ready (DoR)

Uma atividade somente pode iniciar desenvolvimento quando possuir:

## Requisitos

* objetivo claramente definido;
* bounded context identificado;
* dependências conhecidas;
* critérios de aceite definidos;
* impacto arquitetural analisado.

## Restrições

Não iniciar implementação quando:

* houver Open Question bloqueante;
* houver ADR pendente;
* houver dependência externa não resolvida.

---

# Definition of Done (DoD)

Uma atividade somente pode ser considerada concluída quando:

## Código

* implementado;
* compilando;
* versionado;
* revisado.

## Testes

* testes unitários executados;
* testes de integração executados;
* cenários críticos validados.

## Observabilidade

* logs implementados;
* métricas disponíveis;
* erros rastreáveis.

## Segurança

* validações implementadas;
* controles de acesso aplicados;
* segredos externos protegidos.

## Documentação

* documentação atualizada;
* ADR atualizado quando necessário.

---

# Branch Strategy

## Main

```text
main
```

Responsável por produção.

---

## Develop

```text
develop
```

Responsável pela integração contínua.

---

## Feature

```text
feature/<nome>
```

Novas funcionalidades.

---

## Fix

```text
fix/<nome>
```

Correções.

---

## Hotfix

```text
hotfix/<nome>
```

Correções emergenciais.

---

# Pull Request Checklist

## Arquitetura

* [ ] Respeita ADRs existentes
* [ ] Respeita bounded contexts
* [ ] Não cria acoplamento indevido

## Código

* [ ] Build executado
* [ ] Sem código morto
* [ ] Sem dependências desnecessárias

## Testes

* [ ] Testes criados
* [ ] Testes executados
* [ ] Evidências registradas

## Segurança

* [ ] Sem credenciais no código
* [ ] Sem segredos versionados
* [ ] Permissões revisadas

## Observabilidade

* [ ] Logs relevantes adicionados
* [ ] Tratamento de erros implementado

---

# Release Strategy

## Ambiente Local

Uso exclusivo para desenvolvimento.

---

## Ambiente Dev

Integração contínua.

Validação técnica inicial.

---

## Ambiente HML

Validação funcional.

Testes integrados.

Homologação.

---

## Ambiente Produção

Liberação controlada.

Necessário:

* aprovação funcional;
* aprovação técnica;
* rollback definido.

---

# Critérios de Go Live

Obrigatório:

* build aprovado;
* testes aprovados;
* observabilidade ativa;
* monitoramento ativo;
* rollback documentado.

---

# Governança de Mudanças

Mudanças que alterem:

* arquitetura;
* topologia;
* segurança;
* ownership de dados;
* contratos externos;

devem retornar para avaliação arquitetural antes da implementação.

---

# Métricas de Qualidade

Monitorar:

* lead time;
* tempo de revisão;
* taxa de falha em deploy;
* cobertura de testes;
* incidentes pós-release.

---

# Conclusão

A camada Implementation deve seguir este documento como referência oficial para execução, revisão e entrega das capacidades da plataforma.

Nenhuma funcionalidade deve ser promovida para produção sem atender aos critérios definidos neste artefato.
