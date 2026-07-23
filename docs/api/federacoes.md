# Federações API

| Item | Valor |
|------|-------|
| Feature | FT-FEDERACAO |
| Base path | `/api/v1/federacoes` |
| Contrato SDD | `specs/features/federacao/api.md` |
| Controller | `FederacaoController` |

---

## Autorização

| Operação | Autenticação | Admin |
|----------|--------------|-------|
| `GET` | Sim | Não |
| `POST`, `PUT`, `PATCH` | Sim | Sim |

---

## Modelo (SSOT Oracle)

| Coluna | API |
|--------|-----|
| `NOM_FEDERACAO` | `name` |
| `SIG_FEDERACAO` | `acronym` |
| `COD_UNIMED` | `unimedCode` (NUMBER(3)) |
| `NUM_REGISTRO_ANS` | `ansRegistration` |
| `URL_SITE` | `websiteUrl` |
| `DSC_FEDERACAO` | `description` (CLOB) |
| `FLG_ATIVO` | `status` |
| `DAT_CADASTRO` | `createdAt` |
| `DAT_ATUALIZACAO` | `updatedAt` |

---

## POST /api/v1/federacoes

Exemplo:

```json
{
  "name": "Unimed Ceará",
  "acronym": "UNICE",
  "unimedCode": 979,
  "ansRegistration": "32195-8",
  "websiteUrl": "https://www.unimedceara.com.br",
  "description": "Federação administradora do portal."
}
```

---

## PATCH /api/v1/federacoes/{id}/status

Inativação rejeitada (422) quando existirem singulares ativas vinculadas.
