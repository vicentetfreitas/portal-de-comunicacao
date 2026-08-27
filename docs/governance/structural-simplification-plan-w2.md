# Structural Simplification — Plano W2 (Arquitetura-Alvo e Decisões)

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Data | 2026-08-20 |
| Categoria documental | Working (transitório — ver critério de remoção) |
| Responsável | Claude Code (sessão de planejamento) — decisão: Vicente Freitas |
| Camada dona | Governança documental / arquitetura de repositório |
| Origem | `docs/audit/12-structural-simplification-audit-w0-w1.md` (W0/W1) + decisões declaradas por Vicente nesta sessão (W2) |
| SSOT correspondente | Nenhum ainda. Após aprovação, incorporar a `docs/technology/01-technology-stack.md` (stack), `docs/governance/03-open-decisions.md` (decisões) e `docs/governance/05-roadmap.md` (sequência) |
| Critério de remoção (PENDING_REMOVAL quando) | As decisões P0/P1 abaixo forem aprovadas e incorporadas aos SSOTs correspondentes, ou o plano for substituído por um ADR/roadmap formal |
| Escopo | Somente planejamento. Nenhum arquivo do projeto foi movido, renomeado ou alterado como parte deste plano, exceto a criação deste artefato |

---

## 1. Estado-alvo da stack

```text
Frontend   → Vue 3 + Quasar + TypeScript
Backend    → Java + Spring Boot (único consumidor de Oracle e do Armazenamento)
Banco      → Oracle (domínio/transacional)
CMS        → WordPress (conteúdo editorial, via REST API)
Banco CMS  → banco próprio do WordPress (não é banco de domínio)
Storage    → Armazenamento de Arquivos (provedor concreto: decisão P0, ver Seção 3)
Docker     → empacotamento/execução de frontend, backend, WordPress+seu banco, storage escolhido
CI/CD      → GitHub Actions hoje; regras agnósticas para portar a GitLab CI/CD futuramente
Jira       → integração futura, ainda não iniciada
Versionamento → GitHub (mantido — sem migração)
```

## 2. Decisões

| # | Decisão | Prioridade | Status |
|---|---|---|---|
| D1 | GitLab vs. GitHub | — | **Resolvida nesta sessão** — GitHub mantido; regras agnósticas de plataforma |
| D2 | `docs/architecture/11-target-repository-structure.md` obsoleto | P1 | Conteúdo confirmado obsoleto nesta sessão (Next.js/PostgreSQL/retirada de WordPress não procedem); falta ato formal de reclassificação |
| D3 | `specs/domain/` (content-model/publication-model) | P1 | Provável domínio de conteúdo ligado ao WordPress agora confirmado como permanente; requer confirmação humana explícita antes de reclassificar |
| D4 | Storage de arquivos — provedor concreto | **P0** | Aberta — ver Seção 3 (trade-offs) |
| D5 | `docker-compose.yml` — serviço `database` em Postgres, deveria ser Oracle | P1 | Gap técnico já reconhecido (`docs/governance/01-project-status.md`); pré-requisito para qualquer trabalho sério de Docker/CI local |
| D6 | Ausência de CI backend | P1 | Pré-requisito antes de portar pipeline a GitLab CI/CD |
| D7 | Unificação `minimal-ssot.md` × `07-documentation-architecture.md` | P2 | Sem bloqueio operacional — pode aguardar |
| D8 | Timing do arquivamento físico de `construction/` | P2 | Gated pelo fechamento de `FT-COLABORADOR`/`FT-PRIMEIRO-ACESSO` |
| D9 | Mapeamento formal Jira ↔ `feature.yaml` | P2 | Jira é "posteriormente" por decisão já tomada nesta sessão |

## 3. Trade-offs — Storage de arquivos (D4)

| Critério | Volume persistente Docker | Filesystem externo | Object storage S3-compatível |
|---|---|---|---|
| Simplicidade | Alta | Média | Média |
| Segurança | Média | Média | Alta (políticas/URLs assinadas) |
| Backup | Média (scriptado) | Média-Alta | Alta (replicação/versionamento nativos) |
| Docker | Nativo | Bind mount externo | Serviço adicional (ex. MinIO) |
| Produção | Baixa-Média (preso ao host) | Média | Alta |
| Escalabilidade | Baixa (não compartilhável entre réplicas) | Média | Alta |
| Facilidade de migração | Baixa | Média | Alta (protocolo portável entre provedores) |

**Recomendação:** object storage S3-compatível (ex. MinIO em Docker para dev/homologação, trocável por provedor gerenciado em produção sem mudar contrato de código) — alinhado ao contrato já existente em `docs/solution-design/06-integration-contracts.md` (binário nunca no Banco — ADR-004; protocolo "de objetos/arquivos"; Backend como único consumidor).

**Ainda depende de decisão humana:**
- MinIO self-hosted é aceitável, ou produção já tem provedor S3 corporativo definido?
- Requisitos de retenção/tamanho máximo de arquivo (não definidos — ver `specs/features/arquivos/specification.md`).
- Requisitos de compliance/LGPD sobre residência física dos dados (não avaliado nesta sessão).

## 4. Plano de etapas (sem movimentação física ainda)

**Simplificação documental:**
1. Aprovar decisões P0/P1 desta sessão.
2. Reclassificar formalmente D2 como Archive (cabeçalho/metadado, não movimentação física).
3. Confirmar ou arquivar `specs/domain/` (D3).
4. Unificar os três catálogos de decisão (`docs/governance/03-open-decisions.md`, `docs/architecture/08-decision-records.md`, `docs/technology/04-decision-log.md`) sob namespace único.
5. Aplicar o critério ativo/histórico já proposto em `docs/audit/12-...md` Seção 5.
6. Só então planejar (não executar) a movimentação física de `construction/`, `engineering/`, `.cursor/archive/`.

**Preparação para desenvolvimento:**
```text
arquitetura (D1–D9) → documentação mínima (etapas acima) → Docker (corrigir D5; adicionar WordPress+banco; adicionar storage de D4)
→ qualidade/testes → CI/CD (backend, D6) → Jira (quando priorizado) → desenvolvimento
```

**Automação futura (não implementada):**
```text
Jira → Feature (feature.yaml) → Branch → Commit → PR/MR → CI → Quality Gates → Deploy
```

## 5. Critérios de conclusão por etapa

| Etapa | Concluída quando |
|---|---|
| Decisões P0/P1 | D2–D6 aprovadas por Vicente, registradas em `docs/governance/03-open-decisions.md` |
| Reclassificação documental | D2/D3 aplicadas sem referência quebrada (validação por busca textual, como na Etapa 10) |
| Unificação de catálogos | Um único namespace de ID de decisão, sem colisão |
| Docker corrigido | `docker-compose.yml` reflete Oracle + WordPress + storage escolhido; sobe localmente sem erro |
| CI backend | Pipeline `mvn clean verify` executando em CI, espelhando o que já existe para frontend |
| Storage implementado | D4 aprovada, provedor provisionado em ambiente de desenvolvimento, `FT-DOCUMENTO` desbloqueada |

---

## Nota sobre localização deste artefato

Colocado em `docs/governance/` — camada já responsável por decisões, riscos e roadmap do projeto (`03-open-decisions.md`, `05-roadmap.md`), sem prefixo numérico para não ser confundido com a série SSOT `01–09`. Segue o precedente já existente de `docs/governance/reconciliation-report.md` — um documento de trabalho, não numerado, na mesma camada. Nenhum diretório novo foi criado.
