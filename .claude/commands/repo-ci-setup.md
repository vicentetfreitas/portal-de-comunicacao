---
description: Reproduzir a estratégia de branches (development/stage) + CI GitLab por repositório dividido (DEC-015).
argument-hint: <caminho-local-do-repo> <backend|frontend|cms>
---

Modo Repo CI Setup. Reproduz, para um repositório novo ou existente da separação DEC-015 (`docs/technology/04-decision-log.md`), o que já foi feito manualmente em 2026-08-26 nos três repositórios (`backend`, `frontend`, `wordpress` em `/home/projects/`). Não inventar uma estratégia nova sem o usuário pedir explicitamente — só replicar esta.

Alvo: $ARGUMENTS

## Pré-condições (parar e perguntar se qualquer uma faltar)

- O repositório já tem remote `origin` configurado (GitLab corporativo) e uma branch com o código real (ex.: `chore/repo-split-import` ou equivalente).
- **Não presumir** que `main` local corresponde a `origin/main` — conferir com `git rev-parse main` vs `git rev-parse origin/main`; se divergirem, `development` deve partir da branch de código, nunca de `main` local.
- Nomenclatura de pasta local: nome simples (`backend`/`frontend`/`wordpress`, ou equivalente), **distinto** do slug do projeto no GitLab — não renomear o projeto no GitLab sem pedido explícito do usuário.

## Passos

1. `git checkout -b development <branch-de-código>`.
2. Copiar o template correspondente ao tipo (`backend`, `frontend` ou `cms`) de `.claude/templates/gitlab-ci/<tipo>.yml` para `.gitlab-ci.yml` na raiz do repo-alvo — **ler o template antes de copiar** para confirmar que os scripts/imagens ainda batem com o `package.json`/`pom.xml` atuais do repo-alvo (versões podem ter mudado desde 2026-08-26).
3. Commitar (`ci: adicionar pipeline GitLab CI`, sem forjar scope de Feature — este trabalho é infra cross-cutting, não Feature; ver Pendência #10 em `docs/governance/01-project-status.md`).
4. `git checkout -b stage development`.
5. Confirmar com o usuário antes de `git push -u origin development stage` — é publicação em remote corporativo real, ação visível para o time.
6. Registrar a execução em `docs/technology/04-decision-log.md` § DEC-015 (tabela "Progresso da Execução") e, se algo relevante mudar de estado, em `docs/governance/01-project-status.md` (Pendência #8).

## Fora de escopo (a menos que pedido explicitamente)

- Proteção de branch no GitLab (não executável via CLI sem `glab`/token de API — ação manual do usuário na UI).
- Merge para `origin/main`.
- Fluxo Feature→Branch→PR→CI automatizado, sincronização código×documentação, integração Jira — avaliados como "não maduros o suficiente" em 2026-08-26.
