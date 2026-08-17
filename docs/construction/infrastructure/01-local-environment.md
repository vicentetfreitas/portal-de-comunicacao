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

Versão mínima:

```text
22.12+
```

---

## Yarn

Versão:

```text
1.22.22 (packageManager do frontend)
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
portal-de-comunicacao/
├── backend/
├── frontend/
├── database/
├── specs/
├── docs/
├── construction/
├── scripts/
└── docker-compose.yml
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
cd frontend && yarn install
```

---

## Execução

```bash
cd frontend && yarn dev
```

Servidor de desenvolvimento: **http://localhost:9000** (proxy `/api` → backend).

---

# Banco de Dados

O backend utiliza **Oracle Database 11g+** com schema `UNMPORTCOM` e usuário de aplicação `UNMPORTCOM_APP` (DEC-DB-024).

SSOT: `database/README.md`, `database/GOVERNANCE.md`.

---

## Oracle (obrigatório)

Configurar via `.env` na raiz do repositório:

```env
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@<host>:<porta>/<service>
SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP
SPRING_DATASOURCE_PASSWORD=<senha>
SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA=UNMPORTCOM
```

Instalação greenfield: `database/ddl/000-install.sql` (executar como DBA).

> **Atenção:** `docker-compose.yml` na raiz ainda provisiona PostgreSQL (legado). Não usar para o backend Oracle. Alinhamento previsto na Etapa 5.

---

# Variáveis de Ambiente

Backend (`.env` na raiz):

```env
SPRING_PROFILES_ACTIVE=local
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP
SPRING_DATASOURCE_PASSWORD=<senha>
```

---

Frontend (`frontend/.env`):

```env
VITE_APP_ENV=local
VITE_API_BASE_URL=/api/v1
BACKEND_URL=http://localhost:8080
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
Oxlint
Docker
GitLens
```

---

# Qualidade

Executar antes de commitar:

```bash
cd backend && mvn verify
```

```bash
cd frontend && yarn lint && yarn test
```

---

# Critérios de Aceite

* Backend inicia sem erros (com Oracle configurado).
* Frontend inicia sem erros (`yarn dev` na porta 9000).
* Oracle acessível com `UNMPORTCOM_APP`.
* Docker operacional (opcional).
