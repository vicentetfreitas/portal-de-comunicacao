# Rollback

## Propósito

Scripts de **reversão** de migrações estruturais (evolução futura).

## Estado atual

Não há scripts de rollback versionados na homologação **2026-07-22**.

## Governança

- Cada migração futura em `../migrations/` deverá, quando aplicável, referenciar ou incluir estratégia de rollback documentada aqui.
- Rollback **não** substitui restauração DBA nem baseline.

Ver: [../GOVERNANCE.md](../GOVERNANCE.md) · [../migrations/README.md](../migrations/README.md).
