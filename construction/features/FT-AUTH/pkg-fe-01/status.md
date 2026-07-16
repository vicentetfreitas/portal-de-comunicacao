# PKG-FE-01 — FT-AUTH Frontend Integration

| Campo | Valor |
|--------|--------|
| Feature | FT-AUTH |
| PKG | PKG-FE-01 |
| Nome | Frontend Authentication Integration |
| Status | **DONE** |
| Data | 2026-07-16 |
| Executor | feature-implementer |

---

# Escopo

Implementação frontend da Feature FT-AUTH (TASK-AUTH-FE-001 a FE-011) sobre a Frontend Foundation.

---

# Tarefas

| ID | Descrição | Status |
|----|-----------|--------|
| TASK-AUTH-FE-001 | Fluxo de login (redirect `/api/v1/auth/login`) | ✅ |
| TASK-AUTH-FE-002 | Fluxo de logout com CSRF | ✅ |
| TASK-AUTH-FE-003 | Consumo de `/api/v1/auth/me` | ✅ |
| TASK-AUTH-FE-004 | Proteção de rotas (Vue Router Guards) | ✅ |
| TASK-AUTH-FE-005 | Estado de autenticação (Pinia) | ✅ |
| TASK-AUTH-FE-006 | Interceptor HTTP 401 → refresh → retry | ✅ |
| TASK-AUTH-FE-007 | Sessão expirada → redirect login | ✅ |
| TASK-AUTH-FE-008 | Tratamento visual de falhas de autenticação | ✅ |
| TASK-AUTH-FE-009 | Tratamento visual de acesso negado (403) | ✅ |
| TASK-AUTH-FE-010 | CSRF em requisições mutáveis | ✅ (foundation) |
| TASK-AUTH-FE-011 | Opção "Lembrar-me" no login | ✅ |

---

# Arquivos criados/alterados

```text
frontend/src/services/auth/auth.service.ts
frontend/src/auth/session-redirect.ts
frontend/src/auth/unauthorized-handler.ts
frontend/src/stores/auth-store.ts
frontend/src/boot/auth.ts
frontend/src/config/router.ts
frontend/src/router/guards/auth.guard.ts
frontend/src/pages/auth/index.vue
frontend/src/pages/app/index.vue
frontend/src/pages/unauthorized.vue
frontend/src/components/app/AppShell.vue
frontend/src/components/app/AppSidebar.vue
frontend/src/composables/useAuth.ts
frontend/src/i18n/pt-BR.ts
frontend/test/unit/auth/auth.service.spec.ts
frontend/test/unit/auth/auth-store.spec.ts
frontend/test/unit/auth/unauthorized-handler.spec.ts
frontend/test/unit/guards/auth.guard.spec.ts
frontend/test/unit/http/response.interceptor.spec.ts
```

---

# Validações

| Comando | Resultado |
|---------|-----------|
| `yarn lint` | ✅ PASS |
| `yarn typecheck` | ✅ PASS |
| `yarn test` | ✅ PASS (19 arquivos, 49 testes) |
| `yarn build` | ✅ PASS |

---

# Resumo

Frontend FT-AUTH integrado: login Zimbra via redirect, logout, hidratação `/auth/me`, guards ativos, refresh automático em 401, UI de login com "Lembrar-me" e mensagens de erro, perfil no AppShell e página 403 refinada.
