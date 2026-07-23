# 03-environment-strategy.md

# Environment Strategy

## Objetivo

Este documento define a estratégia oficial de ambientes do Portal de Comunicação Digital (PCD).

Seu propósito é garantir:

* Padronização entre ambientes
* Reprodutibilidade da aplicação
* Segurança de configurações
* Facilidade de implantação
* Redução de erros operacionais
* Governança de infraestrutura

Todos os ambientes devem seguir as diretrizes estabelecidas neste documento.

---

# Ambientes Oficiais

O projeto possuirá quatro ambientes principais:

| Ambiente           | Objetivo                   |
| ------------------ | -------------------------- |
| Local              | Desenvolvimento individual |
| Development (DEV)  | Integração contínua        |
| Homologation (HML) | Validação funcional        |
| Production (PRD)   | Operação oficial           |

---

# Estratégia Geral

## Princípios

### Configuração Externa

Nenhuma configuração específica de ambiente deve estar hardcoded no código.

Todas as configurações devem ser externas:

```text
Variáveis de ambiente
Secrets
Configuração de infraestrutura
```

---

### Build Único

O mesmo artefato gerado pelo pipeline deve ser promovido entre ambientes.

Fluxo:

```text
Build
 ↓
DEV
 ↓
HML
 ↓
PRD
```

Não é permitido recompilar a aplicação para cada ambiente.

---

### Infraestrutura como Código

Toda infraestrutura deve ser versionada.

Exemplos:

```text
Docker
Docker Compose
GitHub Actions
Scripts DDL (DBA)
```

---

# Ambiente Local

## Objetivo

Permitir desenvolvimento individual e execução completa do sistema.

---

## Tecnologias

| Item         | Tecnologia     |
| ------------ | -------------- |
| Backend      | Spring Boot    |
| Banco        | Oracle XE      |
| Containers   | Docker         |
| Orquestração | Docker Compose |

---

## Estrutura Local

```text
Developer Machine
│
├── Frontend (Quasar)
├── Backend (Spring Boot)
└── Oracle XE (Docker)
```

---

## Inicialização

```bash
docker compose up -d
```

---

## Perfis Spring

```text
local
```

Arquivo:

```text
application-local.yml
```

---

## Características

Permitido:

* Dados fictícios
* Dados de teste
* Logs detalhados
* Swagger habilitado

Proibido:

* Dados reais
* Credenciais corporativas

---

# Ambiente Development (DEV)

## Objetivo

Integração contínua do time.

---

## Responsabilidade

Validar:

* Integração entre módulos
* Build automatizado
* Testes automatizados
* Scripts DDL (DBA)

---

## Perfil

```text
dev
```

Arquivo:

```text
application-dev.yml
```

---

## Banco de Dados

```text
Oracle Database
```

Ambiente compartilhado pela equipe.

---

## Características

Permitido:

* Dados mascarados
* Testes integrados

Proibido:

* Dados sensíveis
* Uso para homologação de negócio

---

# Ambiente Homologation (HML)

## Objetivo

Validação funcional e aprovação dos requisitos.

---

## Perfil

```text
hml
```

Arquivo:

```text
application-hml.yml
```

---

## Banco

```text
Oracle Database
```

Estrutura semelhante à produção.

---

## Características

Permitido:

* Testes de aceitação
* Testes de negócio
* Validação pelos usuários

Proibido:

* Desenvolvimento direto
* Testes destrutivos

---

# Ambiente Production (PRD)

## Objetivo

Operação oficial do Portal de Comunicação Digital.

---

## Perfil

```text
prd
```

Arquivo:

```text
application-prd.yml
```

---

## Banco

```text
Oracle Database
```

Instância corporativa oficial.

---

## Características

Permitido:

* Operação produtiva

Proibido:

* Testes manuais
* Debug
* Alterações diretas

---

# Estratégia de Configuração

## Arquivos Permitidos

```text
application.yml
application-local.yml
application-dev.yml
application-hml.yml
application-prd.yml
```

---

## application.yml

Deve conter apenas:

* Configurações comuns
* Valores padrão
* Configurações independentes de ambiente

---

## Arquivos Específicos

Devem conter apenas:

```text
URLs
Credenciais
Timeouts
Configurações específicas
```

---

# Variáveis de Ambiente

## Convenção

Formato:

```text
UPPER_SNAKE_CASE
```

Exemplos:

```text
DB_URL
DB_USERNAME
DB_PASSWORD

JWT_SECRET

SMTP_HOST
SMTP_PORT
SMTP_USERNAME
SMTP_PASSWORD
```

---

## Arquivo Local

```text
.env
```

---

## Versionamento

Permitido:

```text
.env.example
```

Proibido:

```text
.env
```

---

# Estratégia de Banco de Dados

## Banco Oficial

```text
Oracle Database 23ai
```

---

## Banco Local

```text
Oracle XE
```

---

## Evolução do schema (DBA)

Ferramenta:

```text
Baseline DDL — database/ddl/
```

---

## Regras (DEC-DB-019)

Toda alteração estrutural deve ser refletida em script DDL versionado, executado pelo DBA.

Exemplo de baseline:

```text
000-install.sql … 901-validation.sql
```

---

## Alterações Manuais

Proibido:

```text
ALTER TABLE manual fora do processo DBA
CREATE TABLE manual fora do processo DBA
DROP TABLE manual fora do processo DBA
```

Toda alteração deve passar por script DDL versionado.

---

# Gestão de Secrets

## Desenvolvimento Local

Secrets armazenados em:

```text
.env
```

---

## Ambientes Compartilhados

Secrets armazenados em:

```text
GitHub Secrets
Vault Corporativo
Ferramenta equivalente
```

---

## Regras

Nunca armazenar:

* Senhas
* Tokens
* Chaves privadas
* Certificados

em:

```text
Git
Código-fonte
Documentação
```

---

# Estratégia Docker

## Containers Oficiais

### Backend

```text
portal-comunicacao-backend
```

---

### Banco

```text
oracle-xe
```

---

## Imagens

As imagens devem ser versionadas.

Exemplo:

```text
portal-comunicacao-backend:1.0.0
portal-comunicacao-backend:1.1.0
```

Proibido:

```text
latest
```

em ambientes produtivos.

---

# Estratégia de Deploy

## Fluxo

```text
Feature Branch
        ↓
Develop
        ↓
Release
        ↓
Main
```

---

## Promoção

```text
DEV
 ↓
HML
 ↓
PRD
```

---

## Critérios

Somente promover quando:

* Build aprovado
* Testes aprovados
* Migrações validadas
* Aprovação funcional obtida

---

# Monitoramento

Todos os ambientes devem expor:

```text
/actuator/health
/actuator/info
/actuator/metrics
```

---

# Logs

## DEV

Nível:

```text
DEBUG
```

---

## HML

Nível:

```text
INFO
```

---

## PRD

Nível:

```text
WARN
ERROR
```

---

# Estratégia de Backup

## Banco de Dados

Produção deve possuir:

* Backup diário
* Retenção mínima de 30 dias
* Procedimento documentado de restauração

---

# Estratégia de Recuperação

Objetivos iniciais:

| Indicador | Meta     |
| --------- | -------- |
| RPO       | 24 horas |
| RTO       | 4 horas  |

---

# Critérios de Conformidade

Um ambiente será considerado conforme quando:

* Configurações externas
* Secrets protegidos
* Schema provisionado pelo DBA (baseline DDL)
* Health Check disponível
* Monitoramento ativo
* Logs centralizados

---

# Decisões Oficiais

| Item          | Decisão               |
| ------------- | --------------------- |
| Banco Oficial | Oracle Database       |
| Banco Local   | Oracle XE             |
| Containers    | Docker                |
| Orquestração  | Docker Compose        |
| Build         | Maven                 |
| Configuração  | Spring Profiles       |
| Evolução schema | Baseline DDL (DBA) — DEC-DB-019 |
| Segredos      | Variáveis de Ambiente |
| Pipeline      | GitHub Actions        |

---

# Status

Aprovado para utilização como padrão oficial de ambientes do Portal de Comunicação Digital.
