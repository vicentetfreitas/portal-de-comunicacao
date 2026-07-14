# Release Plan

## Objetivo

Definir a estratégia de releases do Portal de Comunicação.

Este documento estabelece o cronograma, governança, critérios de aprovação e planejamento das entregas da solução.

---

# Escopo

Abrange:

* Releases
* Marcos de entrega
* Critérios de promoção
* Aprovações
* Governança

---

# Estratégia

Modelo incremental.

Cada release deve entregar valor mensurável.

---

# Estrutura de Releases

```text id="mvs3gi"
Release 1
    ↓
Release 2
    ↓
Release 3
```

---

# Release 1

## Objetivo

Entrega do MVP.

---

## Escopo

Release 1 corresponde às **Etapas 1–5** do roadmap arquitetural (`docs/solution-design/10-delivery-roadmap.md`).

* Fundação da Plataforma (Etapa 1)
* Organização Corporativa + Controle de Acesso (Etapa 2)
* Gestão Documental (Etapa 3)
* Comunicação Interna — Notificações e Comunicados (Etapa 4)
* Migração Operacional (Etapa 5)
* Observabilidade base

**Fonte normativa:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado em 2026-06-22

**Excluído do Release 1:** Campanhas, Mensagens, Dashboard de negócio (Métricas Administrativas — pós-MVP).

---

## Critério de Conclusão

Todos os requisitos MVP aprovados.

---

# Release 2

## Objetivo

Ampliação funcional.

---

## Escopo

* Novas integrações
* Melhorias operacionais
* Evoluções de UX

---

# Release 3

## Objetivo

Escalabilidade e otimizações.

---

## Escopo

* Analytics
* Automações
* Recursos avançados

---

# Ambientes

```text id="vy5zqg"
Development
      ↓
Homologação
      ↓
Produção
```

---

# Critérios de Promoção

## Development → Homologação

* Build aprovado
* Testes aprovados
* Sonar aprovado

---

## Homologação → Produção

* UAT aprovado
* Segurança aprovada
* Aprovação do negócio

---

# Versionamento

Semantic Versioning.

---

## Exemplo

```text id="lgs7h6"
v1.0.0
v1.1.0
v1.1.1
```

---

# Janela de Release

Preferencialmente:

```text id="9vzvcz"
Terça a Quinta
```

---

## Evitar

* Sexta-feira
* Vésperas de feriado
* Datas críticas do negócio

---

# Aprovações

Obrigatórias:

## Técnica

* Arquitetura
* Desenvolvimento
* Infraestrutura

---

## Negócio

* Product Owner
* Stakeholders

---

# Checklist de Release

Antes da publicação:

* [ ] Build aprovado
* [ ] Testes aprovados
* [ ] Sonar aprovado
* [ ] Segurança aprovada
* [ ] Deploy homologado
* [ ] Backup realizado
* [ ] Rollback validado

---

# Comunicação

Antes da release comunicar:

* Equipe técnica
* Operação
* Negócio
* Suporte

---

# Métricas

Monitorar:

```text id="ljw2h4"
Lead Time

Deployment Frequency

Failure Rate

MTTR
```

---

# Governança

Toda release deve possuir:

* Escopo definido
* Aprovação registrada
* Evidências de testes
* Registro de implantação

---

# Critérios de Aceite

O plano de release será considerado aderente quando:

* Existirem critérios claros de promoção.
* Todas as aprovações forem rastreáveis.
* Todas as releases possuírem rollback.
* O histórico de entrega puder ser auditado.
