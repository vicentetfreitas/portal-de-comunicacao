# Portal de Comunicação — monorepo (ARQUIVADO)

> **Este repositório está congelado (read-only) desde 2026-08-28.**
> A separação em repositórios independentes (DEC-015) foi concluída.
> Nada aqui deve ser editado. O conteúdo permanece apenas como histórico.

## Onde o projeto vive agora

| Camada | Repositório |
|--------|-------------|
| **SSOT** (specs, docs, database, engineering) + **backend** (API Spring Boot, Java 25) | `portal-comunicacao-api` — `gitlab.unimedceara.com.br/unimedceara/portal-comunicacao/portal-comunicacao-api` |
| **Frontend** (Vue 3 / Quasar) + docs de frontend | `portal-comunicacao-app` |
| **CMS** (WordPress) | `portal-comunicacao-cms` |
| **Acompanhamento** | Jira, projeto `PUC` |

`portal-comunicacao-api` é o **repositório-líder**: `specs/`, `docs/`, `database/`, `engineering/` e o `CLAUDE.md` mestre vivem lá. Os repos `app` e `cms` carregam só a documentação da própria camada e referenciam o `api`.

## Contexto da decisão

- `docs/technology/04-decision-log.md` § **DEC-015** — separação em repositórios.
- `docs/governance/12-repository-reorganization.md` — plano e execução da reorganização (P.A. #2: `api` como líder da SSOT).
- `docs/governance/11-jira-integration.md` — unificação e reorganização do Jira.

## O que sobrou aqui (snapshot histórico, 2026-08-28)

`specs/`, `docs/` (menos `frontend/`/`figma/`, migrados para o `app`), `database/`, `engineering/`, `construction/`, `reports/`, `scripts/`.
Removidos nesta data: `backend/`, `frontend/`, `wordpress/`, `docker-compose.yml`, `.github/`, `docs/frontend/`, `docs/figma/`, `docs/brandbook-unimed-ceara.pdf` (todos migrados — ver tabela acima). O histórico Git completo continua disponível.
