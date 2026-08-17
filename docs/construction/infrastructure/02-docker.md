# Docker

## Objetivo

Definir os padrões de containerização do Portal de Comunicação.

Este documento estabelece como aplicações, dependências e ambientes devem ser executados utilizando Docker.

---

# Escopo

Abrange:

* Dockerfiles
* Docker Compose
* Redes
* Volumes
* Build
* Imagens
* Segurança
* Desenvolvimento local

---

# Objetivos

Garantir:

* Portabilidade
* Consistência
* Reprodutibilidade
* Isolamento

---

# Arquitetura

```text
┌──────────────────┐
│ Frontend Vue/Quasar │
└────────┬─────────┘
         │
┌────────▼─────────┐
│ Spring Boot API  │
└────────┬─────────┘
         │
┌────────▼─────────┐
│ Oracle (externo) │
└──────────────────┘
```

> Compose na raiz (`docker-compose.yml`) inclui serviço Postgres legado — não substitui Oracle do backend.

---

# Estrutura

```text
portal-de-comunicacao/
├── backend/Dockerfile
├── frontend/Dockerfile
├── docker-compose.yml      # raiz — backend + frontend (+ Postgres legado)
└── database/ddl/           # Oracle — instalação via DBA
```

---

# Dockerfile Backend

Objetivo:

* Build reproduzível
* Imagem enxuta
* Execução segura

---

## Estratégia

Multi-stage build.

```dockerfile
FROM maven AS build

FROM eclipse-temurin:25-jre
```

---

# Dockerfile Frontend

Objetivo:

* Build otimizado
* SSR compatível
* Imagem reduzida

---

## Estratégia

```dockerfile
FROM node:lts AS build

FROM node:lts-alpine
```

---

# Usuário Não Root

Obrigatório.

---

## Exemplo

```dockerfile
RUN adduser appuser

USER appuser
```

---

# Docker Compose

Serviços mínimos:

```yaml
services:
  frontend:
  backend:
  database:
```

---

# Rede

Rede dedicada:

```yaml
networks:
  portal-network:
```

---

# Volumes

Persistência obrigatória para:

```text
Banco de dados
Logs
Uploads
```

---

# Exemplo

```yaml
volumes:
  postgres-data:
```

---

# Health Checks

Todos os containers devem possuir health check.

---

Backend:

```yaml
healthcheck:
```

---

Frontend:

```yaml
healthcheck:
```

---

Banco:

```yaml
healthcheck:
```

---

# Build

## Local

```bash
docker compose build
```

---

## Execução

```bash
docker compose up -d
```

---

# Logs

Visualização:

```bash
docker compose logs -f
```

---

# Segurança

Proibido:

* Executar como root
* Secrets em Dockerfile
* Credenciais hardcoded

---

# Variáveis

Utilizar:

```env
.env
```

ou

```text
Secrets Manager
Vault
```

---

# Versionamento

Tags obrigatórias:

```text
latest
1.0.0
1.0.1
```

---

# Limpeza

```bash
docker compose down
```

---

```bash
docker system prune
```

---

# Boas Práticas

* Imagens pequenas.
* Multi-stage build.
* Usuário não root.
* Health checks.
* Logs estruturados.
* Versionamento semântico.

---

# Critérios de Aceite

* Todos os serviços executam via Docker.
* Ambiente sobe com único comando.
* Persistência configurada.
* Containers saudáveis.
* Imagens seguras e reproduzíveis.
