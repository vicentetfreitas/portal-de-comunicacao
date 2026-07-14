# Execution Plan — FT-AUTH (Authentication)

| Item | Valor |
|------|-------|
| Feature Code | **FT-AUTH** |
| Feature Slug | authentication |
| Sprint | 1 |
| Status | **Encerrada — BLOCKED** |
| SSOD | `construction/features/FT-AUTH/feature-manifest.yaml` |
| Construction State | `construction/features/FT-AUTH/construction-state.yaml` |
| Versão | 1.1 |
| Última atualização | 2026-07-09 |

---

# Objetivo

Implementar autenticação stateless do Portal de Comunicação via Zimbra, com JWT próprio, Refresh Token em cookies HttpOnly e integração com a Platform Foundation.

Este plano é o **ponto de entrada da execução**. Antes de qualquer ação, consultar `feature-manifest.yaml` (SSOD).

---

# Escopo

## Inclui

- Fluxo login → Zimbra → callback → emissão de tokens
- Endpoints `/api/v1/auth/*` (login, callback, me, logout, refresh)
- Persistência de sessão (`AUTH_SESSAO`) e colaborador
- Integração Zimbra com resiliência
- Auditoria e observabilidade de eventos de autenticação
- Testes de aceite conforme `specs/features/authentication/acceptance-tests.md`

## Não inclui

- Gerenciamento de perfis e permissões (Features futuras)
- Frontend completo (PKGs FE em `specs/features/authentication/tasks.md` — escopo paralelo)
- Alteração da Platform Foundation (registro histórico em `construction/platform-foundation/`)

---

# Dependências

| Dependência | Origem | Status |
|-------------|--------|--------|
| Platform Foundation | `construction/platform-foundation/construction-state.yaml` | ✅ `phase: closed` |
| Especificação FT-AUTH | `specs/features/authentication/` | ✅ Approved |
| Infraestrutura de testes | `backend/.../support/` (PKG-07 PF) | ✅ Disponível |

---

# Sequência de PKGs

| PKG | Nome | Escopo resumido | Tarefas principais |
|-----|------|-----------------|-------------------|
| PKG-01 | Security & Tokens | SecurityFilterChain stateless, JWT, cookies, CSRF auth | BE-001, BE-002, BE-013..015 |
| PKG-02 | Zimbra Integration | Cliente Zimbra, resiliência, validação identidade | INT-001..005, BE-007 |
| PKG-03 | Login & Callback | Redirect login, callback, emissão inicial de sessão | BE-003, BE-004, BE-009 |
| PKG-04 | Session & Refresh | Refresh token, renovação, sessões simultâneas | BE-014, BE-016..017, BE-019 |
| PKG-05 | Identity & Access | `/me`, logout, 403, colaborador automático | BE-005, BE-006, BE-008, BE-010, BE-018 |
| PKG-06 | Audit & Closure | Auditoria, observabilidade auth, testes QA, encerramento | BE-011, BE-012, QA-* |

Ordem obrigatória: PKG-01 → PKG-02 → … → PKG-06.

---

# Critérios de entrada (Definition of Ready)

1. Platform Foundation com `phase: closed` e audit `done`
2. Especificação FT-AUTH em `specs/features/authentication/` — Approved
3. `construction-state.yaml` com `phase: not_started` ou `session`
4. Manifesto SSOD e Snapshot criados (`Execute Feature FT-AUTH`)
5. Nenhum bloqueador aberto em `specs/features/authentication/decisions.md`

---

# Critérios de saída (Definition of Done)

1. Todos os PKGs (01–06) com `status.md` em **DONE**
2. `construction-state.yaml` com `phase: closed`
3. Review, Audit e Readiness aprovados em `review/`
4. `mvn clean verify` — SUCCESS no encerramento
5. Critérios de aceite em `acceptance-tests.md` validados
6. `closure-report.md` emitido

---

# Status

| Métrica | Valor |
|---------|-------|
| Fase | `closed` |
| PKG ativo | — |
| PKGs concluídos | 6 / 6 |
| Estado final | **FEATURE_BLOCKED** |
| Próxima ação | Corrigir bloqueadores → re-encerrar |

---

# Referências

- `construction/features/FT-AUTH/feature-manifest.yaml` — SSOD
- `specs/features/authentication/specification.md`
- `construction/platform-foundation/` — fundação (histórico, não alterar)
- `construction/11-feature-execution-workflow.md` — workflow v3.2
