# PKG Artifact Model — Construction Framework (ART-01)

| Item | Valor |
|------|-------|
| Regra | **ART-01** / **R-24** |
| Versão | 1.0 |
| Compatível | VAL-01, VAL-02, BUILD-01, PARALLEL-01 |

---

## Objetivo

Definir o **conjunto mínimo** de artefatos por PKG, eliminando duplicidade sem perder rastreabilidade, auditoria ou capacidade de retomada.

---

## Estrutura canônica por PKG

```text
pkg-XX/
├── status.md                    ← obrigatório (histórico + VALIDATION SUMMARY)
└── evidence/                    ← opcional (quando validação foi executada)
    └── build-verify-YYYY-MM-DD.log
```

| Artefato | Obrigatório | Conteúdo |
|----------|-------------|----------|
| `status.md` | Sim | Escopo, entregas, implementação, **VALIDATION SUMMARY**, resumo operacional |
| `evidence/*.log` | Quando houver validação | Saída completa dos comandos (BUILD-01) |

**Não criar por PKG:** cópias de scripts de evidência, relatórios de implementação paralelos, índices markdown de log, pedidos de review isolados.

---

## Artefatos descontinuados (não gerar em novos PKGs)

| Artefato | Motivo | Substituto |
|----------|--------|------------|
| `implementation-report.md` | Duplica `status.md` (VAL-01-05) | Seções em `status.md` |
| `evidence/run-bv.sh` (cópia) | Duplica templates | `construction/templates/pkg-evidence-run-*.sh` + `PKG_DIR=...` |
| `evidence/verification-log-*.md` | Duplica `.log` + resumo | `build-verify-*.log` + VALIDATION SUMMARY |
| `evidence/verification-commands.md` | Comandos efêmeros | Template de evidência + `status.md` |
| `review-request.md` (nível PKG) | Processo pontual | `review/` da Feature no encerramento |

Relatórios **Feature-level** em `reports/` apenas para incidentes transversais (ex.: bloqueadores, auditorias pontuais) — não duplicar validação por PKG.

---

## Execução de evidência (sem cópia por PKG)

```bash
export NVM_DIR="$HOME/.nvm" && . "$NVM_DIR/nvm.sh"

# Gate PKG (PKG-FE-01..05)
PKG_DIR=construction/frontend/features/FT-SINGULAR/pkg-fe-01 \
  FULL_VALIDATION=1 \
  bash construction/templates/pkg-evidence-run-frontend.sh

# Gate PKG + E2E (PKG-FE-06 apenas)
PKG_DIR=construction/frontend/features/FT-SINGULAR/pkg-fe-06 \
  FULL_VALIDATION=1 E2E_VALIDATION=1 \
  bash construction/templates/pkg-evidence-run-frontend.sh
```

Log gravado em: `$PKG_DIR/evidence/build-verify-$(date +%Y-%m-%d).log`

Backend:

```bash
PKG_DIR=construction/features/FT-SINGULAR/pkg-01 \
  bash construction/templates/pkg-evidence-run-backend.sh
```

---

## Feature / Workstream (permanente)

```text
<workstream-root>/
├── feature-manifest.yaml
├── construction-state.yaml
├── execution-plan.md
├── session.md
├── pkg-XX/status.md (+ evidence opcional)
├── review/                 ← encerramento
├── closure-report.md
└── reports/                ← somente relatórios transversais (não validação por PKG)
```

---

## Governança

| ID | Regra |
|----|-------|
| ART-01-01 | Um PKG = um `status.md` como relatório principal |
| ART-01-02 | Validação resumida no `status.md`; logs somente em `evidence/*.log` |
| ART-01-03 | Scripts de validação vivem em `construction/templates/` — não copiar para cada PKG |
| ART-01-04 | Após `FEATURE_APPROVED`, manter `status.md` e último `build-verify-*.log` relevante |
| ART-01-05 | Não remover `status.md` nem logs referenciados em review/audit/closure |
