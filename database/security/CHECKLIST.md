# Checklist — privilégios `UNMPORTCOM_APP` (entrega de Feature)

Use em toda Feature que criar ou alterar objetos Oracle consumidos pelo backend.

---

## Objetos novos ou alterados

- [ ] DDL ou migration versionada em `database/ddl/` ou `database/migrations/`
- [ ] `GRANT` de tabela(s) para `UNMPORTCOM_APP_ROLE` em `database/security/grants/`
- [ ] `GRANT SELECT` em sequence(s) nova(s), se aplicável
- [ ] View ou synonym documentado, se a aplicação depender dele
- [ ] `validate/validate-application-user.sql` executado sem falhas (DBA)
- [ ] Backend validado com `SPRING_DATASOURCE_USERNAME=UNMPORTCOM_APP`
- [ ] Teste `ApplicationUserConnectionIntegrationTest` / suíte de integração Oracle (DEC-DB-023)

---

## Proibido

- [ ] Conectar o backend como `UNMPORTCOM`
- [ ] Conceder privilégios apenas no ambiente, sem commit em `database/security/`
- [ ] Alterar entidades JPA apenas para contornar falta de `GRANT`

---

## Rastreabilidade

Registrar no PR ou `construction/features/<FEATURE>/` o script de grant aplicado e evidência da validação.
