# Feature Specification — FT-COLABORADOR

| Feature ID | FT-COLABORADOR |
| Domínio | COLABORADOR |
| Status | APPROVED |

---

# Objetivo

CRUD administrativo de **Colaboradores** com vínculo organizacional (federação, singular, área, equipe), evoluindo o scaffold mínimo de FT-AUTH.

API: `/api/v1/colaboradores`

---

# Escopo

## Incluído

- Cadastro administrativo com contexto organizacional
- Consulta, listagem paginada, atualização, ativação/inativação lógica
- Validação de vínculos com singular, área, equipe e gestor
- Preservação do fluxo `locateOrCreate` de FT-AUTH

## Fora do Escopo

- Exclusão física
- Onboarding / solicitação de vínculo
- Contatos institucionais (CONTATO)
- Matriz completa OQ-020
- Frontend

---

# Requisitos Funcionais

- **RF-COLABORADOR-001** — Cadastrar colaborador
- **RF-COLABORADOR-002** — Consultar por identificador
- **RF-COLABORADOR-003** — Listar com filtros
- **RF-COLABORADOR-004** — Atualizar cadastro
- **RF-COLABORADOR-005** — Alterar status

---

# Regras de Negócio

- **RN-001** — Federação obrigatória
- **RN-002** — Nome obrigatório (200 chars)
- **RN-003** — E-mail obrigatório e único
- **RN-004** — CPF único quando informado
- **RN-005** — Contexto organizacional coerente (singular → área obrigatória; equipe na área)
- **RN-006** — Gestor ativo; não pode ser o próprio colaborador
- **RN-007** — Inativação lógica
- **RN-008** — Não inativar com subordinados ativos
- **RN-009** — E-mail imutável após cadastro (identidade FT-AUTH)

---

# Rastreabilidade

| RF | API | AT |
|----|-----|-----|
| RF-001 | POST /api/v1/colaboradores | AT-001 |
| RF-002 | GET /api/v1/colaboradores/{id} | AT-002 |
| RF-003 | GET /api/v1/colaboradores | AT-003 |
| RF-004 | PUT /api/v1/colaboradores/{id} | AT-004 |
| RF-005 | PATCH /api/v1/colaboradores/{id}/status | AT-005 |

Detalhes em `traceability.md`.
