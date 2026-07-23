# Feature Specification — FT-FEDERACAO

| Feature ID | FT-FEDERACAO |
| Domínio | FEDERACAO |
| Status | APPROVED |

---

# Objetivo

CRUD administrativo de **Federações** (organização raiz), alinhado ao modelo físico Oracle.

API: `/api/v1/federacoes`

---

# Regras de Negócio

- **RN-001** — Nome obrigatório (200 caracteres)
- **RN-002** — Sigla obrigatória e única
- **RN-003** — Código Unimed obrigatório e único (NUMBER(3))
- **RN-004** — Registro ANS obrigatório
- **RN-005** — Inativação bloqueada com singulares ativas vinculadas

---

# Modelo físico (SSOT)

Ver `database/ddl/003-create-tables.sql` (tabela `FEDERACAO`).
