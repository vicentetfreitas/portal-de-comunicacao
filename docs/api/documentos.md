# Documentos API

| Item | Valor |
|------|-------|
| Base path esperado | `/api/v1/documentos` ou `/api/v1/documents` |
| Status | **Sem implementação no backend atual** |

---

## Estado atual

Não existe `DocumentController` nem endpoints de gestão documental no backend Spring Boot implementado.

Controllers ativos sob `/api/v1`:

- `health`
- `auth`, `admin/sessions`
- `singulares`, `areas`, `equipes`, `colaboradores`

---

## Referências em documentação consultiva

Os seguintes documentos mencionam APIs de documentos como **planejamento** ou **padrão corporativo** — não refletem código existente:

| Documento | Menção |
|-----------|--------|
| `docs/implementation/07-api-standards.md` | Exemplos `/api/v1/documents` |
| `docs/construction/backend/04-api-implementation.md` | `/api/v1/documents` planejado |
| `docs/technology/01-technology-stack.md` | `/api/v1/documentos` no roadmap |

---

## Próxima implementação

Quando a Feature de Gestão Documental for implementada, este artefato deverá ser atualizado com os endpoints reais. Até lá, não há contrato executável.

Ver divergências em [discrepancies.md](./discrepancies.md).
