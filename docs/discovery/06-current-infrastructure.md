# Discovery — Current Infrastructure

## Objetivo

Mapear a infraestrutura atual do Portal de Comunicação com base exclusiva em evidências de arquivos Docker, proxy, CI/CD e configurações localizadas.

**Nível de confiança da descoberta:** Alto para serviços e compose localizados; Médio para deploy Swarm/Traefik (labels presentes, runtime não inspecionado); Baixo para variáveis de ambiente referenciadas em CI sem arquivos `envs/` no repositório.

**Dependências utilizadas:** módulos (`01`), RBAC (`02`), entidades (`03`), endpoints (`04`), integrações (`05`). Validações anteriores: APROVADO COM RESSALVAS.

---

## Resumo Executivo

| Categoria | Quantidade |
|---|---|
| Ambientes identificados | 3 |
| Containers / serviços | 5 (+ 1 opcional) |
| Redes Docker | 3 |
| Volumes / bind mounts | 8 |
| Banco de dados | 1 (externo) |
| Componentes de armazenamento | 2 |
| Proxy / publicação | 2 |
| CI/CD | 2 |
| Monitoramento / health | 4 |
| Dependências operacionais críticas | 6 |
| Componentes legados | 3 |
| Lacunas identificadas | 7 |

---

## Ambientes Identificados

| Ambiente | Evidência | Status |
|---|---|---|
| Local | `compose.my-local.yml`, `envs/local/.env` (referenciado) | ATIVO |
| Dev / Homologação | `compose.dev.yml`, branches `development` e `stage` em `.gitlab-ci.yml`, `HML_ENV` / `HML_SECRET` | ATIVO |
| Produção | `compose.prd.yml`, branch `main` em `.gitlab-ci.yml`, `PRD_ENV` / `PRD_SECRET` | ATIVO |

**Observação:** branches `development` e `stage` compartilham mesma configuração de homologação (`HML_ENV`, `compose.dev.yml`, hosts `*-dev`).

---

## Containers Identificados

| Serviço | Tipo | Evidência | Status |
|---|---|---|---|
| Frontend | Container | `compose.my-local.yml`, `compose.dev.yml`, `compose.prd.yml`, `frontend/Dockerfile` | ATIVO |
| CMS (WordPress) | Container | `compose.*.yml`, `cms/Dockerfile`, `cms/Dockerfile.local` | ATIVO |
| Backend PHP | Container | `compose.*.yml`, `backend/Dockerfile`, `backend/Dockerfile.local` | LEGADO |
| Server (Nginx) | Container | `compose.my-local.yml`, `server/Dockerfile`, `server/nginx.conf` | PARCIAL |
| MySQL | Container | Comentário removido em `compose.my-local.yml`; ausente em `compose.dev/prd.yml` | LEGADO |
| Redis | Container | `docker-compose.cache.yml` (arquivo isolado, não referenciado nos compose principais) | PARCIAL |

### Detalhamento por serviço

#### Frontend

| Campo | Valor |
|---|---|
| Tipo | Container |
| Finalidade | Servir SPA Quasar/Vue; dev server local (porta 3000) ou build estático via Nginx em imagem de produção. |
| Evidência | `frontend/Dockerfile`, `frontend/Dockerfile.local`, `frontend/nginx.conf` |
| Status | ATIVO |

#### CMS

| Campo | Valor |
|---|---|
| Tipo | Container |
| Finalidade | WordPress + PHP-FPM + Nginx interno; API REST `portaldecomunicacao/v1` e armazenamento de uploads. |
| Evidência | `cms/Dockerfile`, `cms/config/nginx-wordpress.conf`, `cms/wp-config.php` |
| Status | ATIVO |

#### Backend

| Campo | Valor |
|---|---|
| Tipo | Container |
| Finalidade | API PHP legada (portas 8000/9000); exposto apenas na rede `private` em dev/prd. |
| Evidência | `backend/Dockerfile`, `backend/routes/api.php`, `05-current-integrations.md` |
| Status | LEGADO |

#### Server (Nginx reverse proxy)

| Campo | Valor |
|---|---|
| Tipo | Container |
| Finalidade | Proxy reverso local unificando frontend, CMS e backend em hosts virtuais distintos. |
| Evidência | `server/nginx.conf`, `compose.my-local.yml` |
| Status | PARCIAL — ativo apenas no ambiente local; comentado em `compose.dev.yml` e `compose.prd.yml` |

---

## Redes

| Rede | Finalidade | Evidência |
|---|---|---|
| `traefik-public` | Exposição pública TLS via Traefik (Swarm) | `compose.my-local.yml`, `compose.dev.yml`, `compose.prd.yml` |
| `private` | Comunicação interna entre serviços (frontend, cms, backend) | `compose.*.yml` |
| `portal-network` | Rede isolada para Redis opcional | `docker-compose.cache.yml` |

**Detalhes adicionais:**

| Rede | Driver | Evidência |
|---|---|---|
| `private` (dev/prd) | overlay, subnet `192.168.146.0/24` | `compose.dev.yml`, `compose.prd.yml` |
| `private` (local) | external | `compose.my-local.yml` |
| `traefik-public` | external | Todos os compose principais |

---

## Volumes

| Volume | Finalidade | Evidência |
|---|---|---|
| `./cms/wp-content/uploads` (bind) | Persistência local de mídia e documentos | `compose.my-local.yml` |
| `./cms/wp-content/mu-plugins` (bind) | Hot-reload de mu-plugins em desenvolvimento | `compose.my-local.yml` |
| `./cms/wp-config.php` (bind) | Hot-reload de configuração WordPress local | `compose.my-local.yml` |
| `./server/nginx.conf` (bind) | Configuração Nginx do proxy local | `compose.my-local.yml` |
| `./server/html` (bind) | Páginas estáticas de fallback e health | `compose.my-local.yml` |
| `/mnt/gfs/portal979com` (bind) | Uploads CMS em homologação | `compose.dev.yml` |
| `/mnt/portalcom` (bind) | Uploads CMS em produção | `compose.prd.yml` |
| `redis-data` (named) | Persistência Redis opcional | `docker-compose.cache.yml` |

**Volumes declarados mas não ativos:** `frontend-node-modules` em `compose.my-local.yml` (sem uso no serviço frontend); volumes comentados em `compose.dev.yml` (`uploads_dev`, `plugins_dev`, `backend-storage`).

---

## Banco de Dados

| Banco | Uso | Evidência |
|---|---|---|
| MySQL (externo) | Persistência WordPress: usuários, posts, termos, tabelas customizadas, transients | `wp-config.php` (`WORDPRESS_DB_*`), `compose.my-local.yml` (container removido) |

| Aspecto | Detalhe | Evidência |
|---|---|---|
| Container MySQL | Removido do stack local; conexão via host externo configurado em env | `compose.my-local.yml` |
| Extensão PHP | `php82-mysqli` / `pdo_mysql` no CMS e backend | `cms/Dockerfile`, `backend/Dockerfile` |
| Persistência | Não gerenciada pelos compose atuais; depende de infraestrutura externa | `compose.dev.yml`, `compose.prd.yml` |

---

## Armazenamento

| Componente | Uso | Evidência |
|---|---|---|
| `wp-content/uploads/portaldecomunicacao/` | Binários de documentos referenciados por metadados | `DocumentsService.php`, `DiagnosticsController.php`, `05-current-integrations.md` |
| GFS host mount (`/mnt/gfs/*`, `/mnt/portalcom`) | Persistência de uploads em ambientes Swarm | `compose.dev.yml`, `compose.prd.yml` |
| WordPress Media Library | Imagens e anexos via filesystem WordPress padrão | `cms/Dockerfile` (`VOLUME uploads`) |

---

## Proxy e Publicação

| Componente | Finalidade | Evidência |
|---|---|---|
| Nginx (server) | Proxy local: frontend (`:3000`), CMS (`:80`), backend (`:8000`); health `/health`; CORS preflight em `/wp-json/` | `server/nginx.conf` |
| Nginx (CMS interno) | Servir WordPress e REST API dentro do container CMS | `cms/config/nginx-wordpress.conf` |
| Nginx (frontend prod) | Servir SPA estático com gzip e cache de assets | `frontend/nginx.conf` |
| Traefik | Roteamento TLS `websecure` para frontend e CMS em Swarm | Labels em `compose.dev.yml`, `compose.prd.yml` |

### Roteamento local (Nginx server)

| Host virtual | Upstream | Evidência |
|---|---|---|
| `portaldecomunicacao-local.*` | `frontend-upstream` + proxy `/wp-json/` → CMS | `server/nginx.conf` |
| `portaldecomunicacao-cms-local.*` | `cms-upstream` | `server/nginx.conf` |
| `portaldecomunicacao-api-local.*` | `backend-upstream` | `server/nginx.conf` |

### Publicação dev/prd (Traefik)

| Serviço | Host (variável) | Porta interna | Evidência |
|---|---|---|---|
| Frontend | `${APP_HOST}` | 80 | `compose.dev.yml` |
| CMS | `${CMS_HOST}` | 80 | `compose.dev.yml` |
| Backend | labels Traefik comentados | — | `compose.dev.yml` |
| Server | serviço comentado | — | `compose.dev.yml` |

---

## CI/CD

| Componente | Finalidade | Evidência |
|---|---|---|
| GitLab CI | Pipeline `build` + `deploy` em branches `stage` e `main` | `.gitlab-ci.yml` |
| Harbor Registry | Build, push e pull de imagens `frontend`, `cms`, `backend`, `server` | `.gitlab-ci.yml` (`REGISTRY_UNIMED_URL`) |
| Docker Swarm | Deploy via `docker stack deploy` | `.gitlab-ci.yml`, seção `deploy` em compose dev/prd |
| Docker contexts | `docker-tst` (build/staging), `cluster-prod` (produção) | `.gitlab-ci.yml` |

### Jobs de build

| Job | Dockerfile | Evidência |
|---|---|---|
| `build-frontend` | `frontend/Dockerfile` | `.gitlab-ci.yml` |
| `build-cms` | `cms/Dockerfile` | `.gitlab-ci.yml` |
| `build-backend` | `backend/Dockerfile` | `.gitlab-ci.yml` |
| `build-server` | `server/Dockerfile` | `.gitlab-ci.yml` |

### Ambientes de deploy (CI)

| Branch | Compose | Stack | Contexto Docker |
|---|---|---|---|
| `stage` / `development` | `compose.dev.yml` | `portalcom-979-dev` | `docker-tst` |
| `main` | `compose.prd.yml` | `portalcom-979` | `cluster-prod` |

---

## Dependências Operacionais

| Dependência | Categoria | Impacto |
|---|---|---|
| MySQL externo | Banco | Indisponibilidade impede login, documentos e RBAC |
| Traefik (`traefik-public`) | Proxy | Indisponibilidade impede acesso público em dev/prd |
| GFS / bind mount uploads | Storage | Perda impede download e upload de documentos |
| Zimbra (IMAP/SMTP) | Autenticação | Indisponibilidade impede login corporativo | `05-current-integrations.md` |
| WordPress + mu-plugins | CMS | Indisponibilidade impede toda API `portaldecomunicacao/v1` |
| Harbor Registry | CI/CD | Indisponibilidade impede build e deploy de imagens |
| Nexus npm (registry corporativo) | Build | Referenciado no build frontend de produção | `frontend/Dockerfile` |

---

## Configurações Identificadas

| Categoria | Evidência |
|---|---|
| Banco de dados | `wp-config.php` (`WORDPRESS_DB_*`, `WORDPRESS_TABLE_PREFIX`) |
| Auth / JWT | `wp-config.php` (`JWT_AUTH_SECRET`, `JWT_AUTH_CORS_ENABLE`), variáveis `ZIMBRA_*` em compose |
| URLs do portal | `wp-config.php` (`WP_HOME`, `WP_SITEURL`, `PORTAL_BACKEND_URL`, `INTERNAL_BACKEND_URL`) |
| Debug / logs | `wp-config.php` (`WP_DEBUG`, `WP_DEBUG_LOG`), `nginx-wordpress.conf` (access/error log) |
| Upload | `cms/Dockerfile` (`upload_max_filesize`, `post_max_size`), `server/nginx.conf` (`client_max_body_size 100m`) |
| Email / Zimbra | `compose.my-local.yml` (`ZIMBRA_IMAP_*`, `ZIMBRA_SMTP_*`, `ZIMBRA_SOAP_URL`) |
| Frontend (build-time) | `frontend/src/env.d.ts` (`VITE_*`), `compose.dev.yml` (variáveis comentadas) |
| CI / deploy | `.gitlab-ci.yml` (`STACK_NAME`, `IMAGE_TAG`, `APP_HOST`, `CMS_HOST`, `REGISTRY_UNIMED_URL`) |
| CORS | `cms/Dockerfile` (mapa de origins no `start.sh`), `CorsMiddleware.php` |
| Integrações backend | `wp-config.php` (`PORTAL_BACKEND_URL`), `BackendSync.php` |

**Variáveis sensíveis referenciadas sem arquivo local:** `HML_ENV`, `HML_SECRET`, `PRD_ENV`, `PRD_SECRET`, `REGISTRY_UNIMED_USER`, `REGISTRY_UNIMED_PASS` (definidas como variáveis CI/CD).

---

## Componentes Legados

| Componente | Evidência | Status |
|---|---|---|
| Backend PHP (`backend/`) | `compose.*.yml`, `backend/routes/api.php`, doc `05` | LEGADO |
| Container MySQL | Removido de `compose.my-local.yml`; ausente em dev/prd | LEGADO |
| Server Nginx em Swarm | Serviço comentado em `compose.dev.yml` e `compose.prd.yml` | LEGADO |
| Redis cache | `docker-compose.cache.yml` isolado; sem `WP_REDIS_*` em `wp-config.php` | PARCIAL |

---

## Lacunas Encontradas

- **`build-images.sh`** referenciado em `.gitlab-ci.yml` (`source build-images.sh`) sem arquivo localizado no repositório.
- **Diretório `envs/`** referenciado (`envs/local`, `envs/dev`, `envs/prod`) sem arquivos versionados no repositório.
- **Serviço `server`** construído no CI (`build-server`) mas não implantado nos compose dev/prd (bloco comentado).
- **Container MySQL** ausente em todos os compose ativos; dependência de banco externo sem definição versionada.
- **Redis** (`docker-compose.cache.yml`) referencia `./redis/redis.conf` e `./redis/data` não localizados; não integrado ao stack principal.
- **Ambiente `stage`** usa mesma configuração de homologação que `development` — distinção operacional não evidenciada além do nome da branch.
- **Implementação `backend/src`** ausente conforme validação de módulos (`01-current-modules.md`); container existe mas código de aplicação não localizado.

---

## Cobertura da Descoberta

### Containers Cobertos

| Serviço | Compose local | Compose dev | Compose prd | Dockerfile |
|---|---|---|---|---|
| frontend | SIM | SIM | SIM | SIM |
| cms | SIM | SIM | SIM | SIM |
| backend | SIM | SIM | SIM | SIM |
| server | SIM | NÃO (comentado) | NÃO (comentado) | SIM |
| mysql | NÃO | NÃO | NÃO | NÃO |
| redis | NÃO | NÃO | NÃO | N/A (imagem upstream) |

### Redes Cobertas

| Rede | Local | Dev/Prd | Evidência |
|---|---|---|---|
| `traefik-public` | SIM | SIM | `compose.*.yml` |
| `private` | SIM | SIM | `compose.*.yml` |
| `portal-network` | NÃO | NÃO | Apenas `docker-compose.cache.yml` |

### Volumes Cobertos

| Volume | Local | Dev | Prd |
|---|---|---|---|
| uploads bind/GFS | bind local | `/mnt/gfs/portal979com` | `/mnt/portalcom` |
| mu-plugins bind | SIM | NÃO | NÃO |
| wp-config bind | SIM | NÃO | NÃO |
| nginx.conf bind | SIM | NÃO | NÃO |

### Dependências Cobertas

| Categoria | Cobertura |
|---|---|
| Banco | MySQL externo documentado |
| Proxy | Nginx local + Traefik Swarm |
| Storage | Filesystem + GFS |
| CI/CD | GitLab + Harbor + Swarm |
| Auth externa | Zimbra (via doc 05) |
| Monitoramento | Health endpoints + logs |

---

## Resultado da Validação

### Validação 1

Todos os serviços possuem evidência?

**SIM** — serviços nos compose e Dockerfiles localizados; script `build-images.sh` referenciado apenas indiretamente via CI.

### Validação 2

Todos os containers possuem configuração localizada?

**NÃO** — `server` sem deploy em dev/prd; MySQL sem container; Redis opcional sem integração; `envs/` ausente no repositório.

### Validação 3

Existem componentes legados?

**SIM** — backend PHP, container MySQL removido, server Nginx desativado em Swarm.

### Validação 4

Existem dependências críticas?

**SIM** — MySQL externo, Traefik, GFS/uploads, Zimbra, Harbor Registry.

### Validação 5

Existem ambientes parcialmente documentados?

**SIM** — local usa banco externo de homologação; `stage` e `development` compartilham configuração; arquivos `.env` não versionados.

### Validação 6

Existem configurações obrigatórias sem origem localizada?

**SIM** — `HML_ENV`, `HML_SECRET`, `PRD_ENV`, `PRD_SECRET` e credenciais de registry existem apenas como variáveis CI/CD.

---

## Status Final

**APROVADO COM RESSALVAS**

Ressalvas: banco MySQL externo sem container gerenciado, arquivos de ambiente ausentes no repositório, serviço `server` legado em Swarm, backend PHP legado coexistindo com CMS, e compose Redis isolado sem uso evidenciado. Não bloqueia continuidade da Discovery conforme validações anteriores.
