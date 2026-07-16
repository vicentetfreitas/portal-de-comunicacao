# API Validation — Sprint API-VALIDATION-01

| Item | Valor |
|------|-------|
| Sprint | API-VALIDATION-01 |
| Coleção | `../postman/Portal.postman_collection.json` |
| Ambiente | `../postman/Portal.postman_environment.json` |
| Helpers | `../postman/validation-helpers.js` |
| Relatório | [homologation-report.md](./homologation-report.md) |
| Matriz | [test-matrix.md](./test-matrix.md) |

---

## Objetivo

Homologar os 27 endpoints documentados em `docs/api/` contra a implementação backend, sem alterar código.

---

## Métodos de validação

| Camada | Ferramenta | Escopo |
|--------|------------|--------|
| **Primária** | Testes de aceite Java (`*AcceptanceIntegrationTest`) | Contratos, auth, 422, paginação |
| **Complementar** | Coleção Postman com scripts `pm.test` | Homologação manual/CI via Newman |
| **Evidência histórica** | Sprint Integração 03 — `APPROVED` | 40/40 checklist |

---

## Executar homologação Postman

### Pré-requisitos

1. Backend em execução (`SERVER_PORT=8080`)
2. Zimbra mock ou cookies válidos para cenários auth positivos
3. E-mail admin em `session-administrator-emails`
4. [Newman](https://www.npmjs.com/package/newman) instalado (opcional)

### Newman

```bash
cd docs/api/postman
newman run Portal.postman_collection.json \
  -e Portal.postman_environment.json \
  --folder "01 — Health" \
  --folder "02 — Authentication (Negative)" \
  --reporters cli,json \
  --reporter-json-export ../validation/newman-results.json
```

Cenários positivos de escrita exigem cookies — executar após login browser ou importar cookies de teste.

### Maven (homologação canônica)

```bash
cd backend
mvn test -Dtest='*AcceptanceIntegrationTest,OrgCrossFeatureIntegrationTest,HealthControllerIntegrationTest'
```

---

## Artefatos

| Arquivo | Descrição |
|---------|-----------|
| `homologation-report.md` | Relatório final da sprint |
| `test-matrix.md` | Mapeamento endpoint → casos de teste |
| `newman-results.json` | Gerado após execução Newman (opcional) |

---

## Resultado consolidado

Ver [homologation-report.md](./homologation-report.md) — **homologação aprovada** com base em testes de aceite + coleção Postman instrumentada.
