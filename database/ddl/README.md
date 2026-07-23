# DDL — implementação versionada

## Propósito

Scripts de **criação e instalação** do schema `UNMPORTCOM` (greenfield).

## Orquestração

| Script | Função |
|--------|--------|
| `001-create-users.sql` | Usuários e roles (SYS/DBA) |
| `000-install.sql` | Orquestra 002–009 + `dml/001` |
| `002`–`007` | Sequences, tabelas, constraints, índices, comentários, grants |
| `008-initial-data.sql` | Bootstrap técnico |
| `009-configuracao-portal.sql` | Configuração portal (após `dml/001`) |
| `901-validation.sql` | Validação pós-instalação |
| `900-drop-all.sql` | Remoção (ambientes descartáveis) |
| `902-compile-invalid-objects.sql` | Recompilação auxiliar |

## Governança

- Alinhado a [../baseline/oracle-baseline-2026-07-22.md](../baseline/oracle-baseline-2026-07-22.md) (DB-SYNC-99).
- Validação pós-instalação: `901-validation.sql`.
- Relatório de sincronização: [../reports/sync-report-2026-07-22.md](../reports/sync-report-2026-07-22.md).

Ver: [../GOVERNANCE.md](../GOVERNANCE.md) · [../README.md](../README.md).
