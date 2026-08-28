# Reorganização de Repositórios — Execução da DEC-015

| Item | Valor |
|------|-------|
| Artefato | `docs/governance/12-repository-reorganization.md` |
| Status | Approved |
| Categoria documental | **SSOT** (do plano de execução; a decisão está em DEC-015) |
| Camada dona | Governança |
| Data | 2026-08-28 |
| Resolve | DEC-015 Ponto em Aberto #2 (home da SSOT pós-divisão) |
| Complementa | `docs/technology/04-decision-log.md` §DEC-015, `08-repository-governance.md`, `11-jira-integration.md` |

---

## 1. Decisão

- **Código sai do monorepo.** `portal-de-comunicacao` deixa de conter `backend/`, `frontend/`, `wordpress/`. Os 3 repositórios GitLab passam a ser **primários** — fim do fluxo "editar no monorepo + `git subtree split`".
- **`portal-comunicacao-api` é o repositório-líder da SSOT.** Carrega `specs/` inteiro, o `docs/` transversal, `database/`, `engineering/` e o `CLAUDE.md` mestre.
- **`portal-comunicacao-app`** e **`portal-comunicacao-cms`** carregam só a documentação da própria camada + um `CLAUDE.md` fino que aponta para o api.
- **Cross-referência entre camadas** é o mecanismo de coordenação: um doc no app linka o doc no api por URL (`https://gitlab.unimedceara.com.br/unimedceara/portal-comunicacao/portal-comunicacao-api/-/blob/development/...`).
- O monorepo `portal-de-comunicacao` é **arquivado (read-only)** ao fim.
- **Jira** já é único e reorganizado — ver `11-jira-integration.md`.

---

## 2. Mapa de destino

### → `portal-comunicacao-api` (backend + SSOT líder)

| Origem (monorepo) | Destino (api) | Categoria |
|---|---|---|
| `specs/` (features, foundation, templates, architecture) | `specs/` | SSOT |
| `docs/governance/` | `docs/governance/` | SSOT |
| `docs/domain/` | `docs/domain/` | SSOT |
| `docs/architecture/` | `docs/architecture/` | SSOT |
| `docs/solution-design/` | `docs/solution-design/` | SSOT |
| `docs/technology/` | `docs/technology/` | SSOT |
| `docs/backlog/` | `docs/backlog/` | SSOT |
| `docs/api/` | `docs/api/` | SSOT (backend) |
| `docs/implementation/` | `docs/implementation/` | SSOT (partes backend; partes FE cruzam — mantidas juntas, app referencia) |
| `database/` | `database/` | SSOT (DBA) |
| `engineering/` | `engineering/` | SSOT (templates de integração) |
| `CLAUDE.md` | `CLAUDE.md` (mestre) | SSOT |
| `.github/workflows/` | descartar — CI é `.gitlab-ci.yml` por repo | — |
| `docker-compose.yml` (raiz) | descartar — compose é por repo (DEC-015 / DEC-009) | — |

### → `portal-comunicacao-app` (frontend)

| Origem | Destino (app) |
|---|---|
| `docs/frontend/` | `docs/frontend/` |
| `docs/figma/` | `docs/figma/` |
| — | `CLAUDE.md` **fino** (Apêndice A) |

### → `portal-comunicacao-cms` (wordpress)

| Origem | Destino (cms) |
|---|---|
| — | `CLAUDE.md` **fino** (Apêndice B) |
| specs `noticia`/`servicos` | **ficam** em `api/specs/features/` — CMS referencia |

### → `docs/archive/` (dentro do api) — Archive / Working

| Origem | Destino |
|---|---|
| `construction/` (249 arq.) | `docs/archive/construction/` |
| `docs/discovery/` | `docs/archive/discovery/` |
| `docs/audit/` | `docs/archive/audit/` |
| `docs/construction/` | `docs/archive/construction-docs/` |
| `reports/` | `docs/archive/reports/` |

> Base: `08-repository-governance.md` — Archive = "versionar, não evolui"; Working (`reports/`) = "remover após incorporação, Git guarda histórico". Mantidos como Archive por conterem evidência histórica (audits de fase, `session.md`).
> **Movimentação feita na Fase B (no api), não agora** — ver §3.

`docs/governance/history/` já é sub-Archive e continua onde está (dentro de `docs/governance/`).

---

## 3. Ordem de execução

### Fase A — decisão e plano no monorepo *(feita nesta sessão pelo agente)*

- [x] DEC-015 P.A. #2 e #4 marcados RESOLVIDO + entrada no Progresso da Execução.
- [x] Este plano.
- [x] `CLAUDE.md` mestre atualizado (arquitetura nova, tabela "Consultar primeiro", nota de arquivamento).
- [x] `CLAUDE.md` finos de app e cms preparados (Apêndices A e B).

> **Por que não movemos `construction/`/`docs/audit/`/`reports/` agora:** há uma teia de links entre `docs/governance/`, `docs/backlog/` e esses diretórios. Mover no monorepo — que vai ser congelado — cria um estado quebrado sem ganho. A consolidação para `docs/archive/` acontece **na Fase B**, dentro do api, com o conserto de links no mesmo passo.

### Fase B — semear o api com a SSOT + arquivar *(ação humana)*

1. No working tree do `portal-comunicacao-api` (branch `development`), copiar do monorepo: `specs/`, `docs/` (sem `frontend/` e `figma/` — esses vão para o app), `database/`, `engineering/`, `CLAUDE.md`.
   *(cópia simples + commit; specs/docs não têm histórico compartilhado relevante com o código do api. Alternativa: `git subtree add`/merge se quiser o histórico — não obrigatório.)*
2. **No api**, mover para `docs/archive/`: `construction/`, `docs/discovery/`, `docs/audit/`, `docs/construction/`, `reports/`. Rodar um find-and-replace nos links (`construction/` → `docs/archive/construction/`, `docs/audit/` → `docs/archive/audit/`, etc.) e validar (§Fase E).
3. Commit: `docs: importar SSOT do monorepo + arquivar Working/Archive (DEC-015 P.A. #2)`.
4. Push `development` → abrir MR → `stage`.

### Fase C — semear app e cms *(ação humana)*

1. `portal-comunicacao-app`: adicionar `docs/frontend/`, `docs/figma/` + `CLAUDE.md` (Apêndice A). Commit, push.
2. `portal-comunicacao-cms`: adicionar `CLAUDE.md` (Apêndice B). Commit, push.

### Fase D — retirar código do monorepo *(ação humana)*

1. `git rm -r backend/ frontend/ wordpress/ docker-compose.yml .github/workflows/` no monorepo.
2. Commit: `chore(dec-015): remover código — repos GitLab passam a ser primários`.
3. `README.md` do monorepo → aviso de arquivamento apontando para os 4 destinos.
4. Arquivar o repositório GitHub (`Settings → Archive`) ou marcar read-only.

### Fase E — atualizar referências *(ação humana + agente na próxima sessão)*

1. Nos docs do api, trocar caminhos que assumiam o monorepo (ex.: "`cd backend && mvn`") por "no repo `portal-comunicacao-api`".
2. `docs/governance/01-project-status.md`: registrar a conclusão.
3. `10-project-organization.md` §Camadas: refletir que a SSOT vive no api.
4. Validar links quebrados (`docs/` que apontava para `../construction/` etc.).

---

## 4. Riscos e mitigação

| Risco | Mitigação |
|---|---|
| Links relativos entre `docs/` e `construction/`/`specs/` quebram após a movimentação | `construction/` vira `docs/archive/construction/` — mesmo repo, ajuste de path pontual; Fase E valida |
| Feature spec cruza BE+FE e fica "longe" do dev de frontend | `specs/` fica no api; app referencia por URL. `feature.yaml` continua unidade única no api (não split) |
| Dois lugares editáveis durante a transição | Fase D fecha a janela: assim que o código sai do monorepo, o api é a única fonte |
| Histórico do monorepo "perdido" | Monorepo é arquivado, não deletado — histórico permanece consultável no GitHub |
| `database/` sem dono claro | Fica no api (mais próximo), mas continua SSOT do DBA (DEC-DB-019) — sem acoplamento de runtime |

---

## Apêndice A — `CLAUDE.md` de `portal-comunicacao-app`

```markdown
# Portal de Comunicação — Frontend (app)

Repositório de **código do frontend** (Vue 3 / Quasar). Camada FEATURE/FOUNDATION.

## SSOT — não está aqui

`specs/`, `docs/` (governança, domínio, arquitetura, solution-design, backlog),
`database/` e o `CLAUDE.md` mestre vivem em **`portal-comunicacao-api`**:
https://gitlab.unimedceara.com.br/unimedceara/portal-comunicacao/portal-comunicacao-api

- Precedência: `specs/ > docs/ > código` (ver o api).
- Não implementar sem spec que atenda DoR (`specs/foundation/definition-of-ready.md` no api).
- Feature = `specs/features/<slug>/` no api; `feature.yaml` lá é o status oficial.
- Jira: `docs/governance/11-jira-integration.md` no api. Story da feature = `PUC-XXX`.

## Aqui

- `docs/frontend/` — padrões de frontend desta camada.
- `docs/figma/` — referências de design.
- Validação: `yarn lint`, `yarn typecheck`, `yarn test:unit`; E2E no closure.
- CI: `.gitlab-ci.yml`.

## Commit

- Branch: `feature/<slug>` (ou `feature/PUC-XXX-<slug>`).
- Rodapé: `Refs PUC-XXX`. MR que fecha a parte de código: `Closes PUC-XXX`.
- Commits de agente terminam com `Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>`.
```

## Apêndice B — `CLAUDE.md` de `portal-comunicacao-cms`

```markdown
# Portal de Comunicação — CMS (WordPress)

Repositório de **código do CMS** (WordPress: imagem base + tema/plugins).
Camada FEATURE. Fronteira lógica isolada (DEC-CMS-001).

## SSOT — não está aqui

`specs/`, `docs/` e o `CLAUDE.md` mestre vivem em **`portal-comunicacao-api`**:
https://gitlab.unimedceara.com.br/unimedceara/portal-comunicacao/portal-comunicacao-api

- Comunicado = publicação no WordPress; ver `specs/features/noticia/` no api.
- Não construir tema/plugin antes de `FT-NOTICIA`/`FT-SERVICOS` atingirem DoR.

## Aqui

- `docker-compose.yml` — WordPress + MySQL locais.
- CI: `.gitlab-ci.yml` (placeholder até haver tema/plugin — DEC-015 P.A. #5).

## Commit

- Branch: `feature/<slug>`. Rodapé `Refs PUC-XXX`.
- Commits de agente terminam com `Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>`.
```

---

## Histórico

| Data | Autor | Alteração |
|---|---|---|
| 2026-08-28 | Governança | Criação. Resolve DEC-015 P.A. #2. Fase A executada; B–E pendem de ação humana. |
