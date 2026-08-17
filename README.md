# Portal de Comunicação

Plataforma de comunicação interna corporativa.

## Escopo MVP

Etapas 1–5 (EPIC-001 a EPIC-006). SSOT: [`docs/backlog/04-mvp-scope.md`](docs/backlog/04-mvp-scope.md).

## Estrutura

```text
portal-de-comunicacao/
├── backend/          # API Spring Boot 4.1 (monólito modular, Java 25)
│   └── runtime/      # Artefatos de execução (logs, reports, coverage)
├── frontend/         # Vue 3 + Quasar (Yarn)
├── database/         # SSOT Oracle (baseline, DDL, migrations)
├── construction/     # Estado de construção (registry, workstreams)
├── specs/            # Especificações funcionais (SSOT comportamento)
├── docs/             # Documentação de engenharia
├── scripts/          # Scripts operacionais
└── docker-compose.yml
```

## Pré-requisitos

- Java 25+
- Maven 3.9+
- Node.js 22.12+ e Yarn 1.22+
- Oracle Database 11g+ (acesso com `UNMPORTCOM_APP`)
- Docker 27+ (opcional — frontend/backend em containers)

## Ambiente local

Variáveis de conexão Oracle são obrigatórias via `SPRING_DATASOURCE_*` (ver `.env.example`). O usuário JDBC deve ser **`UNMPORTCOM_APP`** (DEC-DB-024); o schema Hibernate (`UNMPORTCOM`) é configurado por `SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA`. O perfil `local` importa automaticamente o arquivo `.env` na raiz do repositório.

```bash
cp .env.example .env
# Ajuste SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME (UNMPORTCOM_APP) e SPRING_DATASOURCE_PASSWORD
cd backend && mvn spring-boot:run
```

Frontend (dev server na porta 9000):

```bash
cd frontend && yarn install && yarn dev
```

Formato esperado de `SPRING_DATASOURCE_URL`:

```text
jdbc:oracle:thin:@<host>:<porta>/<service>
```

Health check:

```http
GET http://localhost:8080/api/v1/health
GET http://localhost:8080/actuator/health
```

> **Nota:** `docker-compose.yml` na raiz provisiona PostgreSQL legado — o backend requer Oracle. Ver [`docs/construction/infrastructure/01-local-environment.md`](docs/construction/infrastructure/01-local-environment.md).

## Documentação

| Camada | Caminho |
| ------ | ------- |
| Specs (comportamento) | `specs/` |
| Domain | `docs/domain/` |
| Architecture | `docs/architecture/` |
| SSOT operacional | `specs/foundation/minimal-ssot.md` |
| Construction | `construction/registry.yaml` |

## Status (2026-08-14)

| Área | Estado |
| ---- | ------ |
| Platform Foundation | closed |
| Frontend Foundation | closed |
| FT-AUTH, FT-AREA, FT-SINGULAR, FT-EQUIPE, FT-SESSION | closed |
| FT-COLABORADOR | BE closed, FE em execução |
| FT-PRIMEIRO-ACESSO | spec APPROVED; BE not_started, FE em execução |
| Gestão Documental / Comunicação | não iniciados (Etapas 3–4) |

Fonte indicativa: `construction/registry.yaml`.
