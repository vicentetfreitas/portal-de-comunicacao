# Portal de Comunicação — Frontend

Quasar App Vite · Vue 3 · TypeScript · Pinia · vue-i18n

## Prerequisites

- Node.js >= 22.12
- Yarn 1.x

## Setup

```bash
cp .env.example .env
yarn install
```

## Scripts

| Script            | Description                                              |
| ----------------- | -------------------------------------------------------- |
| `yarn dev`        | Development server (port 9000, history mode)             |
| `yarn build`      | Production SPA build                                     |
| `yarn lint`       | Format and lint                                          |
| `yarn lint:check` | CI lint check                                            |
| `yarn typecheck`  | TypeScript validation                                    |
| `yarn test`       | Typecheck (bootstrap); Vitest/Playwright in PKG-FE-S0-09 |

## Environment

External configuration via `VITE_*` variables (see `.env.example`):

| Variable            | Description                                     |
| ------------------- | ----------------------------------------------- |
| `VITE_APP_ENV`      | Environment name (`local`, `dev`, `hml`, `prd`) |
| `VITE_API_BASE_URL` | API prefix (default `/api/v1`)                  |
| `BACKEND_URL`       | Dev proxy target (quasar.config only)           |

## Directory Structure

```text
src/
├── boot/          # Quasar boot files
├── components/ds/ # Design system (PKG-FE-S0-03)
├── composables/
├── config/        # Runtime env
├── i18n/          # Locales (pt-BR default)
├── layouts/       # App layouts (PKG-FE-S0-04)
├── pages/         # Route pages
├── router/
├── services/      # API services (PKG-FE-S0-06)
├── stores/        # Pinia stores
└── types/
```

## Docker Compose

```bash
# From repository root
docker compose up frontend
```

Frontend dev server: `http://localhost:9000` (proxies `/api` to backend).

## Construction

Sprint 0 — PKG-FE-S0-01 Bootstrap. Specification: `docs/construction/frontend/00-frontend-foundation.md`.

## Validação local

Executar no **WSL** (caminho Linux nativo):

```bash
cd /home/projects/portal-de-comunicacao/frontend
cp -n .env.example .env
yarn install && yarn build && yarn lint:check && yarn test
```

Não executar `yarn` em path UNC (`\\wsl.localhost\...`). Não misturar `node_modules` entre Windows e WSL.
