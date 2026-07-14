# Relatório — Consolidação do Modelo de Dados

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Schema | UNMPORTCOM |
| Data | 2026-07-10 |
| Escopo | Documentação da camada de banco (sem alteração DDL) |
| Veredito | **Sincronizado com baseline DDL** |

---

## Objetivo

Consolidar a documentação do modelo de dados após a conclusão da camada DDL, garantindo que conceitual, lógico, físico, catálogo e README representem integralmente o baseline oficial.

---

## Documentos criados

| Documento | Camada | Descrição |
|-----------|--------|-----------|
| `model/02-conceptual-model.md` v1.0 | Conceitual | Entidades de negócio, domínios, regras e mapeamento → físico |
| `model/02-logical-model.md` v1.0 | Lógico | 21 entidades, atributos, cardinalidades, diagrama ER, rastreabilidade DDL |

---

## Documentos atualizados

| Documento | Alteração |
|-----------|-----------|
| `model/01-schema.md` | v3.2 — referência aos modelos conceitual/lógico; contagem 21 tabelas |
| `model/03-physical-model.md` | v4.2 — hierarquia de modelos; declaração de sincronização com DDL |
| `model/04-entity-catalog.md` | Coluna entidade lógica; referências cruzadas às três camadas |
| `model/05-decisions-and-risks.md` | Modelo lógico em artefato próprio; referências atualizadas |
| `README.md` | Índice completo dos artefatos `model/` |

---

## Inconsistências corrigidas

| # | Inconsistência | Correção |
|---|----------------|----------|
| 1 | **Modelo conceitual ausente** — apenas físico documentado | Criado `02-conceptual-model.md` com domínios, Sessão de Autenticação e ID Zimbra |
| 2 | **Modelo lógico disperso** — referência única ao físico | Criado `02-logical-model.md` com 21 entidades e cardinalidades |
| 3 | **`05-decisions-and-risks.md`** afirmava lógico incorporado só no físico | Atualizado para apontar `02-logical-model.md` |
| 4 | **`01-schema.md`** não listava artefatos conceitual/lógico | Seção de artefatos relacionados ampliada |
| 5 | **`04-entity-catalog.md`** sem ligação explícita à camada lógica | Coluna "Entidade lógica" adicionada em todas as tabelas |
| 6 | **`README.md`** sem índice da hierarquia de modelos | Tabela de artefatos `model/` adicionada |
| 7 | **`03-physical-model.md`** sem declaração formal de sync com DDL | Seção 8 atualizada — baseline como referência executável |

---

## Validação de sincronização com baseline DDL

### Contagem de objetos

| Objeto | DDL (`ddl/`) | Modelo físico | Modelo lógico | Catálogo |
|--------|--------------|---------------|---------------|----------|
| Tabelas | 21 | 21 entidades § 4–5 | 21 entidades § 2 | 21 linhas |
| Sequences | 21 | Documentadas por entidade | — | 21 linhas |
| AUTH_SESSAO | `003-create-tables.sql` | § AUTH_SESSAO | § AUTH_SESSAO | ✅ |
| ID_ZIMBRA | `003` + `004` UK | COLABORADOR § | COLABORADOR § | ✅ |

### Validação por domínio

| Domínio | Tabelas DDL | Documentadas |
|---------|-------------|--------------|
| Organização Corporativa | 6 | ✅ |
| Gestão Documental | 6 | ✅ |
| Controle de Acesso | 6 | ✅ |
| Comunicação | 2 | ✅ |
| Configuração | 1 | ✅ |

### Script de validação

`ddl/901-validation.sql` — expectativas: **21 tabelas**, **21 sequences**, alinhadas à documentação.

---

## Hierarquia consolidada

```text
01-schema.md              Escopo UNMPORTCOM
02-conceptual-model.md    Negócio (Sessão, Colaborador, …)
02-logical-model.md       Entidades + cardinalidades
03-physical-model.md      Tabelas Oracle detalhadas
04-entity-catalog.md      Índice tabular DDL ↔ lógico
05-decisions-and-risks.md Decisões DEC-DB-001..010
ddl/                      Baseline executável (fonte física)
```

---

## Pendências (fora do escopo — não impedem documentação)

| Item | Camada | Nota |
|------|--------|------|
| Nomenclatura sequences backend vs DDL | Backend | `SQ_AUTH_SESSAO` vs `SQ_AUTH_SESSAO_COD_SESSAO` |
| `authentication-architecture.md` (specs) | Engenharia | Conceitual auth sem `ID_SESSAO` — specs fora deste escopo |

---

## Confirmação

A documentação da camada de banco **representa integralmente** o baseline oficial UNMPORTCOM:

- ✅ Modelo conceitual completo
- ✅ Modelo lógico completo (21 entidades)
- ✅ Modelo físico sincronizado (v4.2)
- ✅ Catálogo de entidades alinhado
- ✅ README e decisões atualizados
- ✅ Nenhum script DDL alterado nesta atividade

**Status:** documentação **sincronizada** com `docs/database/ddl/`.
