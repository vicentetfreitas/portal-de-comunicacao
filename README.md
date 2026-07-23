# Portal de Comunicação

Plataforma de comunicação interna corporativa.

## Escopo MVP

Fundação e construção seguem `docs/audit/10-mvp-consolidation-audit.md` (Etapas 1–5, EPIC-001 a EPIC-006).

## Estrutura

```text
portal-de-comunicacao/
├── backend/          # API Spring Boot (monólito modular)
│   └── runtime/      # Artefatos de execução (logs, reports, dumps, coverage)
├── frontend/         # Aplicação web (bootstrap pendente)
├── cms/              # Gestão de conteúdo (bootstrap pendente)
├── docker/           # Configurações de container
├── docs/             # Documentação oficial
├── scripts/          # Scripts operacionais
└── docker-compose.yml
```

## Pré-requisitos

- Docker 27+
- Docker Compose 2.30+
- Java 21+
- Maven 3.9+

## Ambiente local

Variáveis de conexão Oracle são obrigatórias via `SPRING_DATASOURCE_*` (ver `.env.example`). O usuário JDBC deve ser **`UNMPORTCOM_APP`** (DEC-DB-024); o schema Hibernate (`UNMPORTCOM`) é configurado por `SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA`. O perfil `local` importa automaticamente o arquivo `.env` na raiz do repositório.

```bash
cp .env.example .env
# Ajuste SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME (UNMPORTCOM_APP) e SPRING_DATASOURCE_PASSWORD
cd backend && mvn spring-boot:run
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

## Documentação

| Camada | Caminho |
| ------ | ------- |
| Domain | `docs/domain/` |
| Architecture | `docs/architecture/` |
| Solution Design | `docs/solution-design/` |
| Implementation | `docs/implementation/` |
| Construction | `docs/construction/` |

## Status

- **Fase 1:** Frontend Construction — concluída
- **Fase 2:** Backend Construction + Infraestrutura — em andamento
- **Fundação (EPIC-001):** estrutura, Docker, health check — iniciada
