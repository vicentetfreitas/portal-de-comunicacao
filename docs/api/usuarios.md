# Usuários API

| Item | Valor |
|------|-------|
| Base path esperado | `/api/v1/usuarios` ou `/api/v1/users` |
| Status | **Sem implementação no backend atual** |

---

## Estado atual

Não existe controller REST para o recurso `usuarios` ou `users` no backend Spring Boot.

A gestão de pessoas no domínio atual utiliza:

| Recurso implementado | Path | Descrição |
|---------------------|------|-----------|
| Colaboradores | `/api/v1/colaboradores` | CRUD organizacional — ver [colaboradores.md](./colaboradores.md) |
| Identidade autenticada | `/api/v1/auth/me` | Sessão do usuário logado — ver [authentication.md](./authentication.md) |

---

## Referências legadas (fora do escopo desta API)

O sistema legado (CMS WordPress) expõe endpoints em `portaldecomunicacao/v1/users` documentados em `docs/discovery/04-current-endpoints.md`. Esses endpoints **não** fazem parte do backend `/api/v1` atual.

---

## Planejamento documental

Este artefato permanece reservado para documentação futura caso um recurso `usuarios` seja implementado. Até lá, consultar [colaboradores.md](./colaboradores.md).
