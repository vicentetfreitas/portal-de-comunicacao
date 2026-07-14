# CI/CD

## Objetivo

Definir a estratégia de Integração Contínua (CI) e Entrega Contínua (CD) do Portal de Comunicação.

Este documento estabelece os padrões para automação de build, testes, validações de qualidade, segurança, versionamento e deploy da solução.

---

# Escopo

Esta documentação cobre:

* Continuous Integration
* Continuous Delivery
* Build Pipeline
* Quality Gates
* Security Gates
* Deploy Automation
* Versionamento
* Promoção entre ambientes

Não cobre:

* Infraestrutura produtiva
* Observabilidade
* Segurança operacional

---

# Objetivos

Garantir:

* Entregas previsíveis
* Redução de falhas
* Rastreabilidade
* Automação
* Segurança
* Reprodutibilidade

---

# Princípios

Todo pipeline deve ser:

* Automatizado
* Reproduzível
* Auditável
* Seguro
* Observável
* Versionado

---

# Estratégia

Fluxo oficial:

```text id="0oj5f9"
Developer
    │
    ▼
Pull Request
    │
    ▼
CI Pipeline
    │
    ▼
Quality Gates
    │
    ▼
Merge
    │
    ▼
Build Release
    │
    ▼
Deploy
```

---

# Branch Strategy

Modelo baseado em trunk-based development.

---

## Branches Permanentes

```text id="xkp2n0"
main
develop
```

---

## Branches Temporárias

```text id="7fz9ot"
feature/*
bugfix/*
hotfix/*
```

---

# Fluxo

```text id="u9g6d4"
feature
    ↓
pull request
    ↓
develop
    ↓
homologação
    ↓
main
    ↓
produção
```

---

# Pull Requests

Obrigatórios para qualquer alteração.

---

# Requisitos

Antes do merge:

* Build aprovado
* Testes aprovados
* Security Scan aprovado
* Review aprovado

---

# Aprovação

Mínimo:

```text id="c3pkzc"
1 reviewer
```

---

## Recomendado

```text id="skt90o"
2 reviewers
```

---

# Pipeline

Estrutura padrão:

```text id="w6q9pw"
Checkout

Install

Build

Lint

Tests

Security Scan

Package

Publish

Deploy
```

---

# Pipeline Backend

## Build

```bash id="0iv16h"
mvn clean verify
```

---

## Testes

```bash id="blw7r8"
mvn test
```

---

## Coverage

```bash id="8vqq0s"
jacoco
```

---

# Pipeline Frontend

## Install

```bash id="wkt5pv"
pnpm install
```

---

## Lint

```bash id="1wst1m"
pnpm lint
```

---

## Tests

```bash id="o5qqhx"
pnpm test
```

---

## Build

```bash id="uzx7w3"
pnpm build
```

---

# Quality Gates

Todo pipeline deve validar:

---

## Backend

* Build
* Testes unitários
* Cobertura
* SonarQube

---

## Frontend

* Build
* ESLint
* Testes
* Coverage

---

# Cobertura Mínima

## Backend

```text id="b68v5k"
80%
```

---

## Frontend

```text id="9tsvzk"
80%
```

---

# SonarQube

Execução obrigatória.

---

## Validar

* Bugs
* Vulnerabilidades
* Code Smells
* Cobertura

---

# Critério

Nenhum blocker permitido.

---

# Security Pipeline

Toda entrega deve passar por verificações de segurança.

---

# SAST

Static Application Security Testing.

---

## Ferramentas

```text id="ol2e3g"
SonarQube
Semgrep
CodeQL
```

---

# Dependency Scan

Validar dependências vulneráveis.

---

## Ferramentas

```text id="s1jj2p"
OWASP Dependency Check

Snyk

Dependabot
```

---

# Secret Scan

Detectar:

* Senhas
* Chaves
* Tokens

---

## Ferramentas

```text id="lmup1p"
Gitleaks

Trufflehog
```

---

# Container Scan

Validar imagens Docker.

---

## Ferramentas

```text id="x7f8h0"
Trivy

Grype
```

---

# Build Artifacts

Artefatos gerados:

---

## Backend

```text id="zfp8fx"
application.jar
```

---

## Frontend

```text id="sdhknz"
.next
```

---

## Containers

```text id="grmwvz"
Docker Images
```

---

# Registry

Imagens devem ser publicadas em registry corporativo.

---

## Exemplos

```text id="yvmbny"
GitHub Container Registry

Azure Container Registry

AWS ECR

Harbor
```

---

# Versionamento

Utilizar Semantic Versioning.

---

## Exemplo

```text id="jbyrxk"
1.0.0

1.1.0

1.1.1
```

---

# Tags

Toda release deve gerar tag.

---

## Exemplo

```text id="1rrb8d"
v1.0.0
```

---

# Ambientes

## Development

Integração contínua.

---

## Homologação

Validação funcional.

---

## Produção

Ambiente oficial.

---

# Promoção

Fluxo:

```text id="m4z7vx"
Development
      ↓
Homologação
      ↓
Produção
```

---

# Deploy Strategy

Estratégia padrão:

```text id="jhd9p3"
Rolling Update
```

---

# Estratégias Permitidas

```text id="g1ay3o"
Rolling

Blue-Green

Canary
```

---

# Estratégia Recomendada

```text id="jlwm0s"
Blue-Green
```

para produção.

---

# Deploy Development

Automático.

---

# Deploy Homologação

Automático após aprovação.

---

# Deploy Produção

Manual com aprovação obrigatória.

---

# Aprovações

Produção exige:

* Aprovação técnica
* Aprovação negócio
* Janela autorizada

---

# Rollback

Todo deploy deve possuir rollback.

---

# Requisitos

* Automatizado
* Documentado
* Testado

---

# Observabilidade do Pipeline

Registrar:

* Tempo de execução
* Falhas
* Sucessos
* Deploys

---

# Métricas

Monitorar:

```text id="g1o2h8"
Build Time

Deployment Frequency

Lead Time

Change Failure Rate

MTTR
```

---

# Notificações

Eventos obrigatórios:

```text id="pt4bik"
Build Failed

Deploy Failed

Deploy Success

Security Violation
```

---

# Destinos

```text id="n97hln"
Teams

Slack

Email
```

---

# Estrutura Recomendada

```text id="xayx0i"
.github
└── workflows
    ├── backend-ci.yml
    ├── frontend-ci.yml
    ├── security.yml
    ├── release.yml
    └── deploy.yml
```

---

# Pipeline Ideal

```text id="lr78ns"
Commit
  ↓
PR
  ↓
CI
  ↓
Quality Gates
  ↓
Merge
  ↓
Package
  ↓
Container Build
  ↓
Registry
  ↓
Deploy HML
  ↓
Approval
  ↓
Deploy PRD
```

---

# Checklist

Antes da publicação:

* [ ] Build aprovado
* [ ] Testes aprovados
* [ ] Cobertura validada
* [ ] Sonar aprovado
* [ ] Dependency Scan aprovado
* [ ] Secret Scan aprovado
* [ ] Container Scan aprovado
* [ ] Artefatos publicados
* [ ] Tag criada
* [ ] Rollback validado

---

# Critérios de Aceite

A estratégia de CI/CD será considerada aderente quando:

* Todo build for automatizado.
* Nenhum deploy ocorrer manualmente fora do pipeline.
* Todos os Quality Gates forem executados.
* Todos os Security Gates forem executados.
* Houver rastreabilidade completa entre commit e deploy.
* Houver rollback documentado.
* Os ambientes forem promovidos de forma controlada.
* O pipeline suportar auditoria e governança corporativa.
