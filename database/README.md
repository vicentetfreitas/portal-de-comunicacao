# Database — Portal de Comunicação

**SSOT** da camada de banco Oracle `UNMPORTCOM` (homologação **2026-07-22**).

| Referência | Caminho |
|------------|---------|
| Baseline física | [baseline/oracle-baseline-2026-07-22.md](baseline/oracle-baseline-2026-07-22.md) |
| Evidência Oracle | [validation/oracle-schema-validation-2026-07-22.md](validation/oracle-schema-validation-2026-07-22.md) |
| Governança | [GOVERNANCE.md](GOVERNANCE.md) |

O banco é administrado pelo **DBA**. A aplicação não gerencia schema (DEC-DB-019).

---

## Estrutura

```text
database/
├── README.md              ← navegação (este arquivo)
├── GOVERNANCE.md          ← precedência e políticas (DB-ORG-01)
├── baseline/              ← especificação física homologada
├── validation/            ← evidências de inspeção Oracle
├── reports/               ← histórico de atividades (não normativo)
├── ddl/                   ← implementação versionada (alinhada à baseline homologada)
├── dml/                   ← cargas institucionais (dados)
├── migrations/            ← evolução brownfield / histórico
├── security/              ← grants application user (DEC-DB-024)
├── rollback/              ← reversões (futuro)
└── model/                 ← modelo complementar (engenharia)
```

Cada subpasta possui `README.md` com responsabilidade única.

---

## Ordem de precedência

1. Baseline física  
2. Evidências de validação Oracle  
3. Scripts DDL  
4. Migrações  
5. Documentação complementar  

Detalhes: [GOVERNANCE.md](GOVERNANCE.md).

---

## Fluxo DBA (greenfield)

```text
ddl/001-create-users.sql     (SYS)
ddl/000-install.sql            (UNMPORTCOM)
ddl/901-validation.sql
dml/002 … 006                  (se escopo institucional — ver dml/README.md)
```

**Brownfield:** [migrations/README.md](migrations/README.md).

**Application user (UNMPORTCOM_APP):** [security/README.md](security/README.md) — executar após DDL/migrations se `007-create-grants` não foi aplicado.

---

## Estatísticas homologadas

| Item | Qtd |
|------|-----|
| Tabelas | 23 |
| PK / FK / UNIQUE / CHECK | 23 / 36 / 11 / 172 |
| Índices | 95 |
| Sequences | 12 |

Fonte: [baseline/oracle-baseline-2026-07-22.md](baseline/oracle-baseline-2026-07-22.md).

---

## Atividades e relatórios

| Atividade | Relatório |
|-----------|-----------|
| Application user backend (INFRA-DB-01) | [reports/infra-db-01-application-user-migration.md](reports/infra-db-01-application-user-migration.md) |
| Organização e governança (DB-ORG-01) | [reports/database-organization-report.md](reports/database-organization-report.md) |
| Racionalização de camadas (ART-DB-01 · **ART-DB-02**) | [reports/database-organization-report.md](reports/database-organization-report.md) |
| Sincronização e integridade (DB-SYNC-99 · ART-DB-03) | [reports/sync-report-2026-07-22.md](reports/sync-report-2026-07-22.md) |

---

## Convenções

| Item | Valor |
|------|-------|
| Schema | `UNMPORTCOM` |
| Banco | Oracle 11g+ |
| Padrões corporativos | [docs/implementation/06-database-standards.md](../docs/implementation/06-database-standards.md) |

---

## Referências

- DEC-DB-019 — schema administrado pelo DBA  
- DEC-DB-024 — application user e `database/security/`  
- INFRA-DB-01 — [reports/infra-db-01-application-user-migration.md](reports/infra-db-01-application-user-migration.md)
- INFRA-DB-02 — [reports/infra-db-02-test-context-audit.md](reports/infra-db-02-test-context-audit.md)
- INFRA-BE-03 — [reports/infra-be-03-jpa-schema-alignment.md](reports/infra-be-03-jpa-schema-alignment.md)
- Modelo complementar: [model/README.md](model/README.md)
