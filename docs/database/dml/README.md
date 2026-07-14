# DML — Cargas e Dados de Referência

| Item | Valor |
|------|-------|
| Schema | UNMPORTCOM |
| Script principal | `ddl/008-initial-data.sql` |

---

## Objetivo

Documentar cargas iniciais do Portal de Comunicação.

---

## Carga mínima para desenvolvimento

| Artefato | Conteúdo |
|----------|----------|
| `ddl/008-initial-data.sql` | Federação padrão, papéis base, metadados de entidades |

**FT-AUTH:** não exige seed de `AUTH_SESSAO` ou `COLABORADOR`. Colaboradores e sessões são criados no primeiro login via Zimbra (`locateOrCreate`).

### Pré-requisito

Executar após `ddl/007-create-grants.sql`:

```text
ddl/008-initial-data.sql
```

---

## Observações

- Scripts idempotentes (`MERGE`) — reexecução segura.
- Dados sensíveis (credenciais) **não** pertencem a esta camada.
- Seeds específicos por Feature devem ser documentados aqui quando introduzidos.
