# Repository Governance

| Item | Valor |
|------|-------|
| Versão | 2.0 |
| Status | Approved — Baseline Gate Final |
| Categoria documental | **SSOT** |
| Complementa | `docs/governance/07-documentation-architecture.md` |
| Última atualização | 2026-07-24 |

---

# Objetivo

Definir o que permanece versionado no repositório, alinhado às categorias **SSOT / Evidence / Working / Archive**.

Evitar crescimento por artefatos temporários ou reconstruíveis.

---

# Princípios

> Este artefato agregará valor daqui a um ano?

> Se puder ser reconstruído integralmente e a perda não comprometer conhecimento permanente, não integra a baseline.

> Relatórios analíticos e investigação são Working por padrão — Git guarda o histórico após remoção.

---

# Classificação (alinhada à arquitetura documental)

| Categoria documental | Política de repositório | Ação |
|---------------------|-------------------------|------|
| **SSOT** | KEEP | Sempre versionar |
| **Evidence** | KEEP / ARCHIVE | Versionar; não promove a SSOT |
| **Archive** | ARCHIVE | Versionar; não evolui |
| **Working** | REMOVE após incorporação | Remover quando SSOT absorveu o conteúdo |
| Artefatos reconstruíveis (build, logs, cache, `.env` local) | REMOVE | Nunca na baseline |

## KEEP — SSOT e Evidence necessária

Inclui: código, testes, specs, docs permanentes, templates, ADRs, DDL, regras de agentes, feature manifests, construction-state, closure reports, homologações.

## ARCHIVE — Evidência histórica

Inclui: session.md de Features fechadas, audits de fase (`docs/audit/`), `docs/governance/history/`, `database/reports/` (não são estado oficial do banco).

## REMOVE — Working absorvido e reconstruíveis

Inclui: logs, build, runtime, coverage, IDE, backups, credenciais, relatórios Working com `PENDING_REMOVAL`, templates vazios.

## REVIEW

Dúvida legítima sobre valor futuro → manter e registrar; decidir com humano.

---

# Critérios de permanência

Permanecer se atender ao menos um:

- é SSOT;
- é Evidence não reconstruível;
- é Archive com valor histórico;
- necessário para reproduzir o projeto.

---

# Critérios de remoção

Remover se:

- Working já incorporado ao SSOT;
- gerado automaticamente / reconstruível;
- apenas estado intermediário;
- local / credencial;
- template vazio sem uso.

---

# Baseline

Baseline oficial = **SSOT + Evidence + Archive**.

Working e REMOVE não integram release.

---

# Checklist para Releases

- validar `.gitignore`;
- ausência de credenciais e artefatos de build/logs;
- ausência de Working `PENDING_REMOVAL`;
- novos docs declaram categoria + SSOT (ver `07-documentation-architecture.md`);
- Security / Repository Readiness quando aplicável.

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.0 | — | KEEP / ARCHIVE / REMOVE |
| 2.0 | 2026-07-24 | Gate Final — alinhamento a SSOT/Evidence/Working/Archive |
