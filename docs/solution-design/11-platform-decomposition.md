# Platform Decomposition

## Objetivo

Definir a decomposição lógica da plataforma Portal de Comunicação.

Este documento estabelece os componentes da solução, suas responsabilidades, dependências, contratos e limites arquiteturais.

O objetivo é garantir que cada parte da plataforma possa evoluir de forma independente, reduzindo acoplamento e simplificando manutenção, escalabilidade e futuras substituições tecnológicas.

---

# Visão Geral da Plataforma

A plataforma é composta por quatro macrocomponentes:

```text
Portal de Comunicação

├── Frontend
├── Backend
├── CMS
└── Infraestrutura
```

Cada componente possui responsabilidades exclusivas e não deve assumir responsabilidades pertencentes a outro domínio arquitetural.

---

# Visão de Alto Nível

```text
                     Usuário
                         │
                         ▼

                    Frontend
                     Next.js

                         │
                         ▼

                     Backend
                   Spring Boot

            ┌────────────┴────────────┐
            │                         │
            ▼                         ▼

          CMS                  Serviços Externos
      Headless CMS

            │
            ▼

       PostgreSQL

            │
            ▼

          Redis
```

---

# Frontend

## Objetivo

Fornecer toda a experiência de navegação da plataforma.

---

## Responsabilidades

* Portal institucional;
* Área autenticada;
* Exibição de conteúdos;
* Consumo de APIs;
* Gestão de sessão do usuário;
* Renderização da interface.

---

## Não Responsável Por

* Persistência de dados;
* Regras de negócio;
* Integrações externas;
* Controle de permissões corporativas.

---

## Dependências

```text
Frontend
    │
    ├── Backend API
    └── CMS API
```

---

## Comunicação

### Backend

```http
GET /api/v1/*
POST /api/v1/*
PUT /api/v1/*
DELETE /api/v1/*
```

---

### CMS

```http
GET /content/*
```

---

# Backend

## Objetivo

Centralizar todas as capacidades de negócio da plataforma.

---

## Responsabilidades

* Autenticação;
* Autorização;
* Gestão de usuários;
* Gestão de perfis;
* Gestão de permissões;
* Processamento de regras de negócio;
* Integrações corporativas;
* APIs da plataforma;
* Eventos de domínio.

---

## Não Responsável Por

* Renderização de páginas;
* Gerenciamento editorial;
* Layout e experiência visual.

---

## Dependências

```text
Backend
    │
    ├── PostgreSQL
    ├── Redis
    ├── CMS
    └── Serviços Externos
```

---

## Módulos Internos

```text
Backend

├── Identity
├── User Management
├── Communication
├── Content Integration
├── Media Integration
├── Notification
├── Audit
└── Administration
```

---

### Identity

Responsável por:

* login;
* logout;
* tokens;
* sessões;
* autenticação.

---

### User Management

Responsável por:

* usuários;
* perfis;
* grupos;
* permissões.

---

### Communication

Responsável por:

* comunicados;
* publicações;
* distribuição;
* histórico.

---

### Content Integration

Responsável por:

* integração com CMS;
* sincronização de conteúdos;
* cache de conteúdo.

---

### Notification

Responsável por:

* e-mail;
* push notification;
* notificações internas.

---

### Audit

Responsável por:

* rastreabilidade;
* logs funcionais;
* trilha de auditoria.

---

# CMS

## Objetivo

Gerenciar conteúdo institucional.

---

## Responsabilidades

* páginas;
* notícias;
* categorias;
* banners;
* menus;
* SEO;
* biblioteca de mídia.

---

## Não Responsável Por

* autenticação corporativa;
* regras de negócio;
* permissões corporativas;
* integrações críticas.

---

## Consumidores

```text
CMS

├── Frontend
└── Backend
```

---

## Exposição de Dados

O CMS deve fornecer dados através de APIs.

Exemplo:

```http
GET /api/content/pages
GET /api/content/news
GET /api/content/categories
GET /api/content/banners
```

---

# Infraestrutura

## Objetivo

Fornecer recursos compartilhados para execução da plataforma.

---

## Componentes

```text
Infraestrutura

├── Nginx
├── PostgreSQL
├── Redis
├── Docker
├── Monitoring
└── CI/CD
```

---

# PostgreSQL

## Responsabilidade

Persistência principal da plataforma.

---

## Armazena

* usuários;
* permissões;
* auditoria;
* configurações;
* dados operacionais.

---

## Não Armazena

Arquivos binários.

Arquivos devem ser armazenados em serviço de storage dedicado.

---

# Redis

## Responsabilidade

Cache distribuído.

---

## Utilização

* cache de conteúdo;
* cache de sessão;
* cache de consultas frequentes.

---

# Gateway

## Objetivo

Centralizar entrada da plataforma.

---

## Componente

Nginx.

---

## Responsabilidades

* roteamento;
* SSL;
* compressão;
* cache HTTP;
* proteção básica.

---

## Exemplo

```text
portal.com.br
    │
    ▼

Nginx

├── / → Frontend
├── /api → Backend
└── /cms → CMS
```

---

# Fluxos Principais

## Consumo de Conteúdo

```text
Usuário
    │
    ▼

Frontend
    │
    ▼

CMS

Retorna conteúdo publicado
```

---

## Operação de Negócio

```text
Usuário
    │
    ▼

Frontend
    │
    ▼

Backend
    │
    ▼

PostgreSQL
```

---

## Conteúdo com Regras de Negócio

```text
Frontend
    │
    ▼

Backend
    │
    ▼

CMS
```

Neste fluxo o Backend atua como camada intermediária de controle.

---

# Contratos Entre Componentes

## Frontend ↔ Backend

Formato padrão:

```json
{
  "data": {},
  "metadata": {},
  "errors": []
}
```

---

## Frontend ↔ CMS

Formato padrão:

```json
{
  "content": {},
  "publishedAt": ""
}
```

---

## Backend ↔ CMS

Integração via REST API.

Nunca acesso direto ao banco do CMS.

---

# Regras de Dependência

## Permitido

```text
Frontend → Backend

Frontend → CMS

Backend → CMS

Backend → PostgreSQL

Backend → Redis
```

---

## Proibido

```text
CMS → Backend

Frontend → Banco de Dados

Frontend → Redis

CMS → Banco do Backend

CMS → Redis
```

---

# Princípios Arquiteturais

## Single Responsibility

Cada componente possui responsabilidade única.

---

## API First

Toda integração deve ocorrer por contratos explícitos.

---

## Loose Coupling

Componentes devem possuir dependência mínima.

---

## Independent Deployment

Frontend, Backend e CMS devem ser implantáveis independentemente.

---

## Replaceable Components

Qualquer componente poderá ser substituído sem necessidade de reescrever toda a plataforma.

---

# Resultado Esperado

Ao final da modernização:

* Frontend atua exclusivamente como camada de apresentação;
* Backend concentra todas as regras de negócio;
* CMS atua exclusivamente como provedor de conteúdo;
* Infraestrutura torna-se compartilhada e padronizada;
* Componentes podem evoluir de forma independente;
* A plataforma reduz significativamente o acoplamento existente na solução legada.
