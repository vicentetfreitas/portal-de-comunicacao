# Local Environment

## Objetivo

Definir o ambiente padrão de desenvolvimento local do Portal de Comunicação.

Este documento estabelece as ferramentas, versões, configurações e dependências necessárias para execução da solução em ambiente local.

---

# Escopo

Abrange:

* Ferramentas obrigatórias
* Instalação local
* Configuração de ambiente
* Banco de dados
* Backend
* Frontend
* Containers
* Variáveis de ambiente

---

# Objetivos

Garantir:

* Onboarding rápido
* Reprodutibilidade
* Padronização
* Redução de problemas de ambiente

---

# Sistema Operacional

Suportados:

```text
Windows 11
Ubuntu 24+
MacOS
```

---

# Ferramentas Obrigatórias

## Git

Versão mínima:

```text
2.45+
```

---

## Docker

Versão mínima:

```text
27+
```

---

## Docker Compose

Versão mínima:

```text
2.30+
```

---

## Node.js

Versão:

```text
LTS
```

---

## pnpm

Versão:

```text
10+
```

---

## Java

Versão:

```text
Java 25
```

---

## Maven

Versão:

```text
3.9+
```

---

# Estrutura do Workspace

```text
workspace
├── backend
├── frontend
├── docs
├── scripts
└── infrastructure
```

---

# Configuração do Backend

## Instalação

```bash
mvn clean install
```

---

## Execução

```bash
mvn spring-boot:run
```

---

# Configuração do Frontend

## Instalação

```bash
pnpm install
```

---

## Execução

```bash
pnpm dev
```

---

# Banco de Dados

Banco local executado via Docker.

---

## PostgreSQL

Container padrão:

```text
postgres:16-alpine
```

**Rastreabilidade:** `docs/implementation/06-database-standards.md`.

---

# Variáveis de Ambiente

Backend:

```env
SPRING_PROFILES_ACTIVE=local

DB_HOST=localhost
DB_PORT=5432
DB_NAME=portal
```

---

Frontend:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

# Estrutura de Arquivos

```text
.env.local
.env.example
```

---

# Regras

Nunca versionar:

```text
.env
.env.local
.env.production
```

---

# IDE Recomendada

## Backend

```text
IntelliJ IDEA Ultimate
```

---

## Frontend

```text
Visual Studio Code
```

---

# Extensões VSCode

```text
ESLint
Prettier
Docker
Tailwind CSS
GitLens
```

---

# Qualidade

Executar antes de commitar:

```bash
mvn verify
```

```bash
pnpm lint
```

```bash
pnpm test
```

---

# Critérios de Aceite

* Ambiente configurado em menos de 30 minutos.
* Backend inicia sem erros.
* Frontend inicia sem erros.
* Banco local disponível.
* Docker operacional.
