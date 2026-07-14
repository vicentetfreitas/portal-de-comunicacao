# Target Repository Structure

## Objetivo

Definir a estrutura alvo do repositório do Portal de Comunicação.

Este documento estabelece a organização física dos componentes da plataforma, permitindo:

* separação clara de responsabilidades;
* independência entre aplicações;
* escalabilidade futura;
* redução de acoplamento com tecnologias legadas;
* simplificação do processo de desenvolvimento e deploy.

---

# Contexto Arquitetural

A arquitetura alvo adota o princípio de separação entre:

* experiência do usuário;
* gestão de conteúdo;
* domínio de negócio;
* infraestrutura.

Cada componente deve possuir responsabilidades bem definidas e limites arquiteturais explícitos.

---

# Estrutura Geral

```text
portal-comunicacao/

├── docs/
│
├── backend/
│
├── frontend/
│
├── cms/
│
├── docker/
│
├── scripts/
│
├── .github/
│
├── .cursor/
│
├── .gitignore
│
├── docker-compose.yml
│
└── README.md
```

---

# Diretório Docs

Responsável por toda documentação do projeto.

```text
docs/

├── discovery/
├── domain/
├── architecture/
├── solution-design/
├── implementation/
```

---

## Responsabilidades

* documentação funcional;
* documentação técnica;
* decisões arquiteturais;
* padrões de desenvolvimento;
* estratégias de migração;
* governança da plataforma.

---

# Diretório Backend

Responsável pelas capacidades de negócio da plataforma.

```text
backend/

├── src/
├── tests/
├── docs/
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Responsabilidades

* APIs REST;
* autenticação;
* autorização;
* gestão de usuários;
* gestão de permissões;
* integrações externas;
* regras de negócio;
* eventos de domínio;
* persistência de dados.

---

## Estrutura Interna

```text
backend/src/main/java

├── domain/
├── application/
├── infrastructure/
└── interfaces/
```

### Domain

Contém:

* entidades;
* value objects;
* agregados;
* eventos de domínio;
* contratos.

### Application

Contém:

* casos de uso;
* serviços de aplicação;
* orquestração de fluxos.

### Infrastructure

Contém:

* banco de dados;
* mensageria;
* integrações externas;
* cache;
* implementações técnicas.

### Interfaces

Contém:

* controllers;
* DTOs;
* endpoints REST;
* adaptadores externos.

---

# Diretório Frontend

Responsável pela experiência do usuário.

```text
frontend/

├── app/
├── components/
├── modules/
├── services/
├── hooks/
├── types/
├── public/
├── tests/
├── Dockerfile
└── README.md
```

---

## Responsabilidades

* portal público;
* área autenticada;
* consumo de APIs;
* navegação;
* experiência do usuário;
* acessibilidade.

---

## Diretrizes

Frontend não deve:

* conter regras de negócio;
* acessar banco diretamente;
* implementar lógica de domínio.

Toda lógica de negócio deve permanecer no Backend.

---

# Diretório CMS

Responsável pela gestão de conteúdo.

```text
cms/

├── config/
├── content-types/
├── extensions/
├── public/
├── Dockerfile
└── README.md
```

---

## Responsabilidades

* páginas institucionais;
* notícias;
* banners;
* categorias;
* menus;
* SEO;
* mídia e arquivos.

---

## Diretrizes

CMS não deve:

* executar regras de negócio;
* controlar autenticação corporativa;
* substituir funcionalidades do backend.

O CMS deve atuar exclusivamente como provedor de conteúdo.

---

# Diretório Docker

Responsável pela infraestrutura local.

```text
docker/

├── backend/
├── frontend/
├── cms/
├── postgres/
├── redis/
├── nginx/
└── monitoring/
```

---

## Responsabilidades

* ambiente local;
* integração entre serviços;
* desenvolvimento padronizado;
* provisionamento simplificado.

---

# Diretório Scripts

Responsável por automações operacionais.

```text
scripts/

├── setup/
├── migration/
├── backup/
└── deployment/
```

---

## Responsabilidades

* bootstrap do ambiente;
* migração de dados;
* automação de deploy;
* manutenção operacional.

---

# Diretório GitHub

Responsável pela automação do ciclo de entrega.

```text
.github/

└── workflows/
```

---

## Responsabilidades

* CI;
* CD;
* validações automáticas;
* quality gates;
* análise estática.

---

# Diretório Cursor

Responsável pelas regras dos agentes de IA.

```text
.cursor/

├── rules/
└── prompts/
```

---

## Responsabilidades

* padronização dos agentes;
* redução de consumo de tokens;
* definição de fluxo documental;
* governança de geração de código.

---

# Estrutura de Infraestrutura Alvo

```text
Internet
    │
    ▼

Nginx Gateway
    │

┌─────────────┬─────────────┬─────────────┐
│             │             │
▼             ▼             ▼

Frontend    Backend        CMS
Next.js     Spring Boot    Headless CMS

                │
                ▼

           PostgreSQL

                │
                ▼

              Redis
```

---

# Princípios Arquiteturais

## Separação de Responsabilidades

Cada componente deve possuir um único propósito claramente definido.

---

## Baixo Acoplamento

Frontend, Backend e CMS devem evoluir independentemente.

---

## Escalabilidade

Cada componente poderá ser escalado individualmente.

---

## Independência Tecnológica

Mudanças de tecnologia em uma camada não devem impactar as demais.

---

## Container First

Todos os serviços devem executar em containers desde o início do projeto.

---

# Estado Futuro Esperado

Ao final da modernização:

* WordPress deixa de ser componente central da solução;
* Backend torna-se o único responsável pelo domínio de negócio;
* CMS torna-se exclusivamente um provedor de conteúdo;
* Frontend torna-se consumidor de APIs;
* Infraestrutura torna-se totalmente containerizada;
* Documentação torna-se a fonte oficial de arquitetura e governança da plataforma.
