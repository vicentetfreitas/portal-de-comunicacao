# Open Risks

## Objetivo

Registrar, monitorar e gerenciar riscos conhecidos do projeto.

Este documento consolida riscos identificados em qualquer camada documental e permite acompanhamento contínuo durante todo o ciclo de vida da solução.

Os riscos registrados aqui devem possuir:

* descrição clara;
* impacto potencial;
* probabilidade estimada;
* plano de mitigação;
* responsável pelo acompanhamento;
* status atualizado.

---

# Classificação de Riscos

## Probabilidade

| Valor | Descrição                |
| ----- | ------------------------ |
| Baixa | Pouco provável           |
| Média | Pode ocorrer             |
| Alta  | Grande chance de ocorrer |

---

## Impacto

| Valor   | Descrição             |
| ------- | --------------------- |
| Baixo   | Pequeno impacto       |
| Médio   | Impacto moderado      |
| Alto    | Impacto significativo |
| Crítico | Compromete o projeto  |

---

## Status

| Status       | Descrição          |
| ------------ | ------------------ |
| Aberto       | Ainda não tratado  |
| Em Mitigação | Plano em execução  |
| Monitorado   | Sob acompanhamento |
| Resolvido    | Eliminado          |
| Aceito       | Risco assumido     |

---

# Resumo Executivo

## Total de Riscos

| Categoria    | Quantidade |
| ------------ | ---------- |
| Abertos      | 7          |
| Em Mitigação | 0          |
| Monitorados  | 1          |
| Resolvidos   | 1          |
| Aceitos      | 0          |

---

# Registro de Riscos

## RSK-001

### Título

Escopo funcional incompleto ou inconsistente.

### Categoria

Negócio.

### Descrição

Existência de requisitos não identificados durante Discovery e Domain.

### Probabilidade

Média.

### Impacto

Alto.

### Mitigação

* Revisões periódicas dos requisitos.
* Validação com stakeholders.
* Controle de mudanças.

### Responsável

Product Owner.

### Status

**Resolvido** — MVP consolidado em `docs/audit/10-mvp-consolidation-audit.md` (2026-06-22).

---

## RSK-002

### Título

Mudanças frequentes de requisitos.

### Categoria

Negócio.

### Descrição

Alterações recorrentes podem impactar cronograma, arquitetura e desenvolvimento.

### Probabilidade

Alta.

### Impacto

Alto.

### Mitigação

* Processo formal de gestão de mudanças.
* Priorização contínua do backlog.
* Aprovação formal de alterações.

### Responsável

Product Manager.

### Status

Aberto.

---

## RSK-003

### Título

Dependências externas não controladas.

### Categoria

Integrações.

### Descrição

APIs, serviços ou fornecedores externos podem apresentar indisponibilidade ou alterações inesperadas.

### Probabilidade

Média.

### Impacto

Alto.

### Mitigação

* Contratos de integração bem definidos.
* Estratégias de retry.
* Monitoramento contínuo.
* Ambientes de homologação.

### Responsável

Arquitetura.

### Status

Aberto.

---

## RSK-004

### Título

Débito técnico acumulado.

### Categoria

Tecnologia.

### Descrição

Decisões de curto prazo podem gerar manutenção excessiva no futuro.

### Probabilidade

Média.

### Impacto

Médio.

### Mitigação

* Code review.
* Arquitetura orientada a padrões.
* Refatorações planejadas.

### Responsável

Tech Lead.

### Status

Aberto.

---

## RSK-005

### Título

Baixa cobertura de testes.

### Categoria

Qualidade.

### Descrição

Ausência de validações automatizadas pode aumentar incidência de defeitos.

### Probabilidade

Média.

### Impacto

Alto.

### Mitigação

* Estratégia de testes definida.
* Testes automatizados.
* Pipeline de qualidade.

### Responsável

Líder de Qualidade.

### Status

**Monitorado** — Sprint 0 estabeleceu 106 testes unitários para infraestrutura transversal. Cobertura de features de negócio pendente (Sprint 1+).

---

## RSK-006

### Título

Problemas de performance sob carga.

### Categoria

Arquitetura.

### Descrição

A solução pode não atender requisitos de escalabilidade e desempenho.

### Probabilidade

Média.

### Impacto

Alto.

### Mitigação

* Testes de carga.
* Monitoramento de performance.
* Arquitetura escalável.

### Responsável

Arquitetura.

### Status

Aberto.

---

## RSK-007

### Título

Falhas de segurança.

### Categoria

Segurança.

### Descrição

Vulnerabilidades podem comprometer dados, serviços e reputação.

### Probabilidade

Média.

### Impacto

Crítico.

### Mitigação

* Secure by Design.
* Revisões de segurança.
* Pentests.
* Gestão de vulnerabilidades.

### Responsável

Security Lead.

### Status

Aberto.

---

## RSK-008

### Título

Atrasos na entrega.

### Categoria

Gestão.

### Descrição

Dependências, mudanças ou dificuldades técnicas podem impactar cronograma.

### Probabilidade

Média.

### Impacto

Alto.

### Mitigação

* Planejamento incremental.
* Gestão ativa do backlog.
* Monitoramento de marcos.

### Responsável

Project Manager.

### Status

Aberto.

---

# Matriz de Risco

| ID      | Probabilidade | Impacto | Criticidade | Status     |
| ------- | ------------- | ------- | ----------- | ---------- |
| RSK-001 | Média         | Alto    | Alto        | Resolvido  |
| RSK-002 | Alta          | Alto    | Crítico     | Aberto     |
| RSK-003 | Média         | Alto    | Alto        | Aberto     |
| RSK-004 | Média         | Médio   | Médio       | Aberto     |
| RSK-005 | Média         | Alto    | Alto        | Monitorado |
| RSK-006 | Média         | Alto    | Alto        | Aberto     |
| RSK-007 | Média         | Crítico | Crítico     | Aberto     |
| RSK-008 | Média         | Alto    | Alto        | Aberto     |
| RSK-009 | Alta          | Alto    | Alto        | Aberto     |

---

## RSK-009

### Título

Lacunas de autorização e bootstrap de sessão no TO-BE.

### Categoria

Segurança / Arquitetura.

### Descrição

`permissions` em `/auth/me` retorna lista vazia; guard de autorização no frontend desligado; `OAuthStateService` em memória (risco em escala horizontal); painel inicial hardcoded.

### Probabilidade

Alta.

### Impacto

Alto.

### Mitigação

Feature de autorização/papéis; DEC-009; state distribuído antes de multi-instância. Não reabrir narrativa OAuth (DA-AUTH-012).

### Responsável

Arquitetura.

### Status

Aberto.

### Referência

Incorporado a partir da análise AUTH-SESSION-FAR-001 (2026-07-24).

---

# Riscos Críticos

Os riscos classificados como críticos devem receber acompanhamento prioritário.

## Riscos Atuais

* RSK-002 — Mudanças frequentes de requisitos.
* RSK-007 — Falhas de segurança (mitigação iniciará com FT-AUTH na Sprint 1).
* RSK-009 — Lacunas de autorização e bootstrap de sessão.

---

# Processo de Revisão

## Frequência

Revisão obrigatória:

* ao final de cada sprint;
* antes de cada release;
* durante auditorias do projeto.

---

# Histórico de Atualizações

| Data       | Autor           | Alteração                                              |
| ---------- | --------------- | ------------------------------------------------------ |
| YYYY-MM-DD | Project Manager | Criação inicial do documento                           |
| 2026-07-08 | Governança      | Revisão pós-Sprint 0 — RSK-001 resolvido; RSK-005 monitorado |
