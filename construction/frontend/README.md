# Frontend Construction — Sprint 0

| Item | Valor |
|------|-------|
| Sprint | **Sprint 0 — Frontend Foundation** |
| Camada | Construction |
| Status | Ready for Execution |
| Versão | 1.0 |
| Data | 2026-07-15 |

---

## Objetivo

Executar a **Frontend Foundation** — infraestrutura compartilhada do Portal de Comunicação antes de qualquer Feature de negócio no frontend.

Esta Sprint **não** implementa Features. Entrega bootstrap, tema, design system base, layouts, roteamento, cliente HTTP, fundação de autenticação, componentes compartilhados e testes.

---

## Navegação (SSOD)

Consultar **sempre** nesta ordem:

```text
1. sprint-0-manifest.yaml     ← localização de artefatos
2. sprint-0-state.yaml        ← estado operacional (SSOT)
3. docs/construction/frontend/00-frontend-foundation.md  ← especificação
4. package-index.md           ← índice de Packages
5. package-dependency-graph.md
6. pkg-fe-s0-XX/status.md     ← status por Package
```

---

## Stack Oficial

Conforme `docs/technology/01-technology-stack.md` (DEC-004):

| Categoria | Tecnologia |
|-----------|------------|
| Framework | Vue 3 |
| UI | Quasar Framework |
| Linguagem | TypeScript |
| Estado | Pinia |
| HTTP | Axios |
| Roteamento | Vue Router 4 |
| Testes | Vitest, Playwright |

---

## Packages (Sprint 0)

| Package | Nome | Status |
|---------|------|--------|
| PKG-FE-S0-01 | Project Bootstrap | Pending Review |
| PKG-FE-S0-02 | Theme | Pending |
| PKG-FE-S0-03 | Design System | Pending |
| PKG-FE-S0-04 | Layouts | Pending |
| PKG-FE-S0-05 | Routing | Pending |
| PKG-FE-S0-06 | HTTP Client | Pending |
| PKG-FE-S0-07 | Authentication Integration | Pending |
| PKG-FE-S0-08 | Shared Components | Pending |
| PKG-FE-S0-09 | Testing Infrastructure | Pending |

Ordem de execução: ver `package-dependency-graph.md`.

---

## Próxima Sprint

**Sprint 1 — FT-AUTH** (frontend)

- Especificação: `specs/features/authentication/`
- Tarefas frontend: TASK-AUTH-FE-001 a TASK-AUTH-FE-011
- Pré-requisito: Sprint 0 concluída (Definition of Done em `00-frontend-foundation.md`)

---

## Referências

| Documento | Caminho |
|-----------|---------|
| Especificação Sprint 0 | `docs/construction/frontend/00-frontend-foundation.md` |
| Discovery produção | `docs/discovery/frontend-production-discovery.md` |
| Mapeamento Features | `docs/discovery/frontend-feature-mapping.md` |
| Technology Stack | `docs/technology/01-technology-stack.md` |
| FT-AUTH | `specs/features/authentication/` |
