# Bootstrap Roadmap

## Objetivo

Definir o plano de implementação inicial da nova plataforma Portal de Comunicação.

Este documento estabelece a sequência oficial para criação do ambiente, infraestrutura, aplicações e componentes arquiteturais definidos nas camadas anteriores.

O objetivo é garantir:

* redução de retrabalho;
* evolução incremental;
* validação contínua;
* menor risco de migração;
* alinhamento entre arquitetura e implementação.

---

# Escopo

Este roadmap cobre:

* criação do novo repositório;
* estruturação dos ambientes;
* bootstrap da infraestrutura;
* bootstrap das aplicações;
* preparação para migração do legado.

Não contempla implementação de funcionalidades de negócio específicas.

---

# Estratégia de Execução

A implementação seguirá uma abordagem incremental.

Cada fase deve ser concluída e validada antes do início da próxima.

```text
Foundation
    ↓

Infrastructure
    ↓

Backend
    ↓

CMS
    ↓

Frontend
    ↓

Observability
    ↓

Migration
```

---

# Fase 0 — Foundation

## Objetivo

Criar a base organizacional do projeto.

---

## Entregáveis

### Repositório

```text
portal-comunicacao/
```

---

### Estrutura Inicial

```text
portal-comunicacao/

├── docs/
├── backend/
├── frontend/
├── cms/
├── docker/
├── scripts/
├── .github/
├── .cursor/
├── README.md
├── .gitignore
└── docker-compose.yml
```

---

### Documentação

Migrar integralmente:

```text
docs/discovery/
docs/domain/
docs/architecture/
docs/solution-design/
docs/implementation/
```

---

## Critério de Conclusão

* repositório criado;
* documentação versionada;
* estrutura validada.

---

# Fase 1 — Infrastructure Bootstrap

## Objetivo

Disponibilizar ambiente local completo.

---

## Componentes

### Oracle Database

Responsável por persistência principal.

Schema: `UNMPORTCOM`

Driver: `oracle.jdbc.OracleDriver`

---

### Redis

Responsável por cache distribuído.

---

### Nginx

Responsável por gateway.

---

### Docker Network

Rede compartilhada entre serviços.

---

## Entregáveis

```text
docker/

├── oracle/
├── redis/
├── nginx/
```

---

### Docker Compose

Ambiente executável com:

```text
Oracle Database
Redis
Nginx
```

---

## Critério de Conclusão

Todos os containers iniciam com:

```bash
docker compose up
```

sem intervenção manual.

---

# Fase 2 — Backend Bootstrap

## Objetivo

Criar fundação técnica transversal do backend.

---

## Stack

```text
Java 25
Spring Boot 4.1.0
Jackson 3
Oracle Database
Maven
```

---

## Estrutura

```text
backend/

├── src/
│   ├── main/
│   │   ├── java/br/com/unimedceara/portalcomunicacao/
│   │   │   ├── configuration/
│   │   │   ├── infrastructure/logging/
│   │   │   └── shared/
│   │   └── resources/
│   │       └── db/migration/
│   └── test/
├── pom.xml
└── README.md
```

---

## Entregáveis da Sprint 0

### Infraestrutura Transversal

* `configuration/` — beans de infraestrutura (Jackson, Locale, Async, ConfigurationProperties)
* `infrastructure/logging/` — Correlation ID, MDC, filtro HTTP
* `shared/dto/` — ApiResponse, PageResponse, ErrorResponse, ValidationErrorResponse, FieldValidationError
* `shared/exception/` — GlobalExceptionHandler e exceções de negócio
* `shared/validation/` — anotações e validadores customizados
* `shared/constants/` — constantes de API, headers, segurança e datas
* `shared/util/` — utilitários transversais

---

### Integração Oracle

Conexão validada via `application.yaml` com driver `oracle.jdbc.OracleDriver`.

Schema provisionado pelo DBA via baseline DDL em `database/ddl/` (DEC-DB-019).

---

### Critério de Conclusão (Sprint 0)

* projeto compila e executa localmente;
* testes unitários passam (`mvn clean verify`);
* conexão Oracle configurada;
* infraestrutura transversal implementada e testada.

---

## Backlog Técnico (pós-Sprint 0)

Itens deliberadamente adiados:

```text
MapStruct
OpenAPI / Swagger UI
REST Controllers (incluindo health endpoint)
SecurityFilterChain
Bounded contexts
Entidades JPA e repositórios
Métricas customizadas (Micrometer/Prometheus)
OpenTelemetry
Correlation ID no padrão de log
Request logging estruturado
```

---

# Fase 3 — CMS Bootstrap

## Objetivo

Disponibilizar gerenciamento de conteúdo.

---

## Escopo

### Conteúdo

* páginas;
* notícias;
* banners;
* categorias.

---

### Mídia

* imagens;
* documentos;
* vídeos.

---

## Estrutura

```text
cms/

├── config/
├── content-types/
├── public/
└── Dockerfile
```

---

## Entregáveis

### Tipos de Conteúdo

```text
Page
News
Banner
Category
```

---

### API de Conteúdo

```http
GET /api/content/pages
GET /api/content/news
GET /api/content/categories
```

---

## Critério de Conclusão

Conteúdos podem ser cadastrados e publicados.

---

# Fase 4 — Frontend Bootstrap

## Objetivo

Criar fundação da experiência do usuário.

---

## Stack

```text
Next.js
React
TypeScript
```

---

## Estrutura

```text
frontend/

├── app/
├── components/
├── modules/
├── services/
├── hooks/
├── public/
└── tests/
```

---

## Entregáveis

### Layout Base

* header;
* footer;
* navegação.

---

### Integração CMS

Consumo de conteúdo publicado.

---

### Integração Backend

Consumo de APIs autenticadas.

---

## Critério de Conclusão

Frontend renderiza conteúdo proveniente do CMS e Backend.

---

# Fase 5 — CI/CD Bootstrap

## Objetivo

Automatizar validações e deploys.

---

## Estrutura

```text
.github/workflows/
```

---

## Pipelines

### Backend

* build;
* testes;
* análise estática.

---

### Frontend

* build;
* testes;
* lint.

---

### CMS

* validação;
* build.

---

## Critério de Conclusão

Pull Requests executam pipelines automaticamente.

---

# Fase 6 — Observability Bootstrap

## Objetivo

Garantir visibilidade operacional.

---

## Componentes

### Logs

Centralização de logs.

Na Sprint 0: Correlation ID via MDC e header HTTP implementados. Emissão no padrão de log pendente.

---

### Métricas

Coleta de métricas.

Na Sprint 0: `spring-boot-starter-actuator` no classpath. Métricas customizadas pendentes.

---

### Health Checks

Monitoramento contínuo.

Na Sprint 0: Actuator com configuração padrão. Health checks customizados pendentes.

---

## Endpoints

```http
GET /actuator/health
```

---

## Critério de Conclusão

Sprint 0: infraestrutura base de logging implementada.

Sprints futuras: métricas, dashboards, request logging estruturado e health checks customizados.

---

# Fase 7 — Security Bootstrap

## Objetivo

Estabelecer baseline de segurança.

---

## Entregáveis

### HTTPS

Obrigatório em todos os ambientes.

---

### Secrets

Remoção de segredos do código-fonte.

---

### Controle de Acesso

Perfis e permissões básicos.

---

### Auditoria

Registro de ações administrativas.

---

## Critério de Conclusão

Todos os serviços seguem os padrões definidos em Security Architecture.

---

# Fase 8 — Legacy Assessment

## Objetivo

Preparar migração do ambiente atual.

---

## Inventário

Mapear:

* páginas;
* notícias;
* banners;
* usuários;
* arquivos;
* integrações.

---

## Classificação

```text
Migrar
Refatorar
Descartar
```

---

## Critério de Conclusão

Inventário completo aprovado.

---

# Fase 9 — Content Migration

## Objetivo

Migrar conteúdo para o novo CMS.

---

## Processo

```text
Exportar
    ↓

Transformar
    ↓

Importar
    ↓

Validar
```

---

## Critério de Conclusão

Conteúdo disponível na nova plataforma.

---

# Fase 10 — Go Live Preparation

## Objetivo

Preparar entrada em produção.

---

## Checklist

### Infraestrutura

* validada;
* monitorada.

---

### Segurança

* validada;
* auditada.

---

### Performance

* testada.

---

### Backup

* configurado.

---

### Rollback

* documentado.

---

## Critério de Conclusão

A plataforma está apta para substituição gradual do ambiente legado.

---

# Roadmap Resumido

```text
Fase 0  Foundation
Fase 1  Infrastructure
Fase 2  Backend
Fase 3  CMS
Fase 4  Frontend
Fase 5  CI/CD
Fase 6  Observability
Fase 7  Security
Fase 8  Legacy Assessment
Fase 9  Content Migration
Fase 10 Go Live Preparation
```

---

# Resultado Esperado

Ao término deste roadmap:

* arquitetura definida estará materializada em código;
* componentes estarão desacoplados;
* infraestrutura estará containerizada;
* conteúdo estará desacoplado do legado;
* a plataforma estará preparada para evolução contínua;
* futuras funcionalidades poderão ser desenvolvidas sem dependência estrutural do ambiente atual.
