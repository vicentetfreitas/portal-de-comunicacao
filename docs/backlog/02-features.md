# Features — Portal de Comunicação

## Objetivo

Consolidar features derivadas de Architecture, Solution Design, Implementation, Construction e Domain.

Toda feature possui vínculo com um épico (`01-epics.md`).

**Data de consolidação:** 2026-06-22  
**MVP oficial:** `docs/audit/10-mvp-consolidation-audit.md` — reconciliado em 2026-06-22

---

## EPIC-001 — Fundação da Plataforma

### FEATURE-001

**Epic:** EPIC-001 — Fundação da Plataforma  
**Nome:** Infraestrutura e Ambientes  
**Descrição:** Configurar estrutura base do repositório, Docker Compose Local/Dev, reverse proxy HTTPS, volumes persistentes.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — INF-001 a INF-005  
**Status:** PENDENTE

### FEATURE-002

**Epic:** EPIC-001  
**Nome:** Backend Bootstrap  
**Descrição:** Estrutura inicial Spring Boot modular (organization, access-control, document-management, internal-communication), health checks, configuração por ambiente.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — BE-001 a BE-004  
**Status:** PENDENTE

### FEATURE-003

**Epic:** EPIC-001  
**Nome:** Frontend Bootstrap  
**Descrição:** Projeto Vue, roteamento base, layout principal, cliente HTTP.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — FE-001 a FE-004; `docs/solution-design/01-solution-overview.md` — Vue  
**Status:** PENDENTE

### FEATURE-004

**Epic:** EPIC-001  
**Nome:** Observabilidade Base  
**Descrição:** Logs estruturados Backend/Frontend, monitoramento de containers, health checks.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — OBS-001 a OBS-003; `docs/construction/delivery/02-release-plan.md` — Release 1  
**Status:** PENDENTE

### FEATURE-005

**Epic:** EPIC-001  
**Nome:** Segurança Base  
**Descrição:** Estrutura de secrets, separação de configurações por ambiente, TLS na fronteira.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — SEC-001, SEC-002; `docs/solution-design/10-delivery-roadmap.md` Etapa 1  
**Status:** PENDENTE

---

## EPIC-002 — Organização Corporativa

### FEATURE-010

**Epic:** EPIC-002  
**Nome:** Gestão de Singulares  
**Descrição:** Administrar unidades singulares da federação; manter código Unimed.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Singulares; `docs/implementation/01-implementation-backlog.md` — ORG-002  
**Status arquitetural:** ATIVO

### FEATURE-011

**Epic:** EPIC-002  
**Nome:** Gestão de Áreas  
**Descrição:** Administrar áreas departamentais hierárquicas vinculadas a singulares.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Áreas; ORG-003  
**Status arquitetural:** ATIVO

### FEATURE-012

**Epic:** EPIC-002  
**Nome:** Gestão de Equipes  
**Descrição:** Administrar equipes vinculadas a áreas.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Equipes; ORG-004  
**Status arquitetural:** ATIVO

### FEATURE-013

**Epic:** EPIC-002  
**Nome:** Gestão de Colaboradores  
**Descrição:** Visualizar e administrar colaboradores por escopo de singular ou área.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Colaboradores; ORG-005  
**Status arquitetural:** ATIVO

### FEATURE-014

**Epic:** EPIC-002  
**Nome:** Gestão de Vínculos Organizacionais  
**Descrição:** Estabelecer e manter vínculo de identidade a singular, área e equipe.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Vínculos Organizacionais; ORG-006  
**Status arquitetural:** ATIVO

### FEATURE-015

**Epic:** EPIC-002  
**Nome:** Modelo Organizacional  
**Descrição:** Modelo de domínio organizacional (singulares, áreas, equipes, colaboradores, vínculos).  
**Origem:** `docs/implementation/01-implementation-backlog.md` — ORG-001  
**Status arquitetural:** ATIVO

### FEATURE-016

**Epic:** EPIC-002  
**Nome:** Gestão de Onboarding  
**Descrição:** Vincular novos colaboradores à singular e área adequadas.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Onboarding  
**Status arquitetural:** PARCIAL — OQ-001

### FEATURE-017

**Epic:** EPIC-002  
**Nome:** Apresentação Organizacional  
**Descrição:** Interface de navegação em singulares, áreas, equipes e colaboradores.  
**Origem:** `docs/architecture/03-component-diagram.md` — Apresentação Organizacional; FE-006  
**Status arquitetural:** ATIVO

---

## EPIC-003 — Controle de Acesso

### FEATURE-020

**Epic:** EPIC-003  
**Nome:** Autenticação Corporativa (Zimbra)  
**Descrição:** Login e logout via Backend → Zimbra; validação de identidade por e-mail corporativo.  
**Origem:** `docs/architecture/03-component-diagram.md` — Autenticação Corporativa; ACC-001, ACC-002; `docs/construction/delivery/01-mvp.md` — Autenticação  
**Status arquitetural:** ATIVO

### FEATURE-021

**Epic:** EPIC-003  
**Nome:** Gestão de Sessão  
**Descrição:** Manter sessão autenticada e contexto de operação do colaborador.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Sessão; ACC-003; `docs/construction/delivery/01-mvp.md` — Controle de sessão  
**Status arquitetural:** ATIVO

### FEATURE-022

**Epic:** EPIC-003  
**Nome:** Gestão de Papéis  
**Descrição:** Atribuir e governar papéis de negócio por escopo organizacional.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Papéis; ACC-004  
**Status arquitetural:** ATIVO

### FEATURE-023

**Epic:** EPIC-003  
**Nome:** Autorização por Escopo  
**Descrição:** Decidir se identidade pode executar ação sobre recurso conforme papel e contexto organizacional.  
**Origem:** `docs/architecture/03-component-diagram.md` — Autorização; ACC-005, ACC-006; `docs/construction/delivery/01-mvp.md` — Controle de acesso  
**Status arquitetural:** ATIVO

### FEATURE-024

**Epic:** EPIC-003  
**Nome:** Gestão de Permissões de Pastas  
**Descrição:** Aplicar regras granulares de acesso na hierarquia de pastas.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Permissões de Pastas  
**Status arquitetural:** ATIVO

### FEATURE-025

**Epic:** EPIC-003  
**Nome:** Auditoria  
**Descrição:** Registrar e consultar eventos de controle de acesso e alterações relevantes.  
**Origem:** `docs/architecture/03-component-diagram.md` — Auditoria; ACC-007; `docs/construction/delivery/01-mvp.md` — Auditoria  
**Status arquitetural:** ATIVO (catálogo ampliado PARCIAL — OQ-019)

### FEATURE-026

**Epic:** EPIC-003  
**Nome:** Gestão de Solicitações de Permissão  
**Descrição:** Registrar pedidos de acesso a recursos privados; conceder ou negar.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Solicitações de Permissão  
**Status arquitetural:** PARCIAL — OQ-003

### FEATURE-027

**Epic:** EPIC-003  
**Nome:** Gestão de Perfis Externos  
**Descrição:** Administrar perfis convidado e parceiro autorizado.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Perfis Externos  
**Status arquitetural:** PARCIAL — OQ-002

### FEATURE-028

**Epic:** EPIC-003  
**Nome:** Apresentação de Autenticação  
**Descrição:** Interface de login, logout e estado de sessão no cliente.  
**Origem:** `docs/architecture/03-component-diagram.md` — Apresentação de Autenticação; FE-005  
**Status arquitetural:** ATIVO

### FEATURE-029

**Epic:** EPIC-003  
**Nome:** Administração de Permissões  
**Descrição:** Interface de administração de permissões.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — FE-007  
**Status arquitetural:** ATIVO

---

## EPIC-004 — Gestão Documental

### FEATURE-030

**Epic:** EPIC-004  
**Nome:** Gestão de Documentos  
**Descrição:** Publicar, consultar, classificar e disponibilizar documentos no escopo organizacional.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Documentos; DOC-001  
**Status arquitetural:** ATIVO

### FEATURE-031

**Epic:** EPIC-004  
**Nome:** Gestão de Pastas  
**Descrição:** Organizar documentos em estrutura hierárquica por contexto organizacional.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Pastas; DOC-002  
**Status arquitetural:** ATIVO

### FEATURE-032

**Epic:** EPIC-004  
**Nome:** Gestão de Visibilidade  
**Descrição:** Classificar exposição de documentos e pastas — público ou privado por escopo.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Visibilidade  
**Status arquitetural:** ATIVO

### FEATURE-033

**Epic:** EPIC-004  
**Nome:** Gestão de Compartilhamento  
**Descrição:** Definir audiência autorizada de recursos documentais.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Compartilhamento; DOC-004  
**Status arquitetural:** ATIVO — ressalva OQ-005

### FEATURE-034

**Epic:** EPIC-004  
**Nome:** Gestão de Armazenamento  
**Descrição:** Controlar quotas, upload e download autorizado de binários; separação metadado/binário.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Armazenamento; DOC-005, DOC-006, DOC-007  
**Status arquitetural:** ATIVO

### FEATURE-035

**Epic:** EPIC-004  
**Nome:** Busca Documental  
**Descrição:** Indexação e consulta documental filtrada por autorização.  
**Origem:** `docs/architecture/03-component-diagram.md` — Busca Unificada (projeção documental); DOC-008, DOC-009  
**Status arquitetural:** ATIVO — escopo adicional OQ-024 pendente

### FEATURE-036

**Epic:** EPIC-004  
**Nome:** Apresentação Documental  
**Descrição:** Explorador de documentos, upload e visualização.  
**Origem:** `docs/architecture/03-component-diagram.md` — Apresentação Documental; FE-008, FE-009, FE-010  
**Status arquitetural:** ATIVO

### FEATURE-037

**Epic:** EPIC-004  
**Nome:** Modelo de Categoria Documental  
**Descrição:** Modelo de categoria para classificação documental.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — DOC-003  
**Status arquitetural:** ATIVO

---

## EPIC-005 — Comunicação Interna

### FEATURE-040

**Epic:** EPIC-005  
**Nome:** Gestão de Notificações  
**Descrição:** Emitir, persistir e entregar notificações in-app unificadas ao colaborador.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Notificações; COM-001, COM-002, COM-003  
**Status arquitetural:** ATIVO

### FEATURE-041

**Epic:** EPIC-005  
**Nome:** Gestão de Comunicados  
**Descrição:** Publicar comunicações institucionais de comunicação corporativa.  
**Origem:** `docs/architecture/03-component-diagram.md` — Gestão de Comunicados; COM-004; `docs/construction/delivery/01-mvp.md` — Gestão de Comunicados  
**Status arquitetural:** PARCIAL — OQ-004  
**MVP:** PARCIAL — presente com ressalva OQ-004 (`docs/audit/10-mvp-consolidation-audit.md`).

### FEATURE-042

**Epic:** EPIC-005  
**Nome:** Canal Fique por Dentro  
**Descrição:** Feed de publicações e informações internas.  
**Origem:** `docs/architecture/03-component-diagram.md` — Canal Fique por Dentro  
**Status arquitetural:** PARCIAL

### FEATURE-043

**Epic:** EPIC-005  
**Nome:** Busca Unificada  
**Descrição:** Pesquisar transversalmente documentos, áreas, singulares e colaboradores.  
**Origem:** `docs/architecture/03-component-diagram.md` — Busca Unificada  
**Status arquitetural:** PARCIAL — ADR-014

### FEATURE-044

**Epic:** EPIC-005  
**Nome:** Métricas Administrativas  
**Descrição:** Indicadores e métricas administrativas de comunicação interna.  
**Origem:** `docs/architecture/03-component-diagram.md` — Métricas Administrativas  
**Status arquitetural:** PARCIAL — R-016, OQ-022  
**MVP:** Não — pós-MVP / capacidade opcional (`docs/audit/10-mvp-consolidation-audit.md` — C-003).

### FEATURE-045

**Epic:** EPIC-005  
**Nome:** Central de Colaboração  
**Descrição:** Espaço de interação entre colaboradores.  
**Origem:** `docs/architecture/03-component-diagram.md` — Central de Colaboração  
**Status arquitetural:** PARCIAL AS-IS

### FEATURE-047

**Epic:** EPIC-005  
**Nome:** Segmentação de Comunicação  
**Descrição:** Segmentação de comunicados.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — COM-005  
**Status arquitetural:** Documentado; segmentação avançada **fora** do MVP

### FEATURE-048

**Epic:** EPIC-005  
**Nome:** Integrações de Canal (Webhook/E-mail)  
**Descrição:** Canais opcionais de entrega externa.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — COM-007, COM-008; `docs/solution-design/10-delivery-roadmap.md` — opcionais Etapa 4  
**Status arquitetural:** Opcional

### FEATURE-049

**Epic:** EPIC-005  
**Nome:** Apresentação de Comunicação  
**Descrição:** Central de Notificações e Central de Comunicados.  
**Origem:** `docs/architecture/03-component-diagram.md` — Apresentação de Comunicação; FE-011, FE-012  
**Status arquitetural:** ATIVO (comunicados PARCIAL)

---

## EPIC-006 — Migração Operacional

### FEATURE-050

**Epic:** EPIC-006  
**Nome:** Migração de Dados  
**Descrição:** Migrar metadados organizacionais, documentais, permissões e auditoria histórica.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — MIG-001 a MIG-004; `docs/solution-design/09-migration-strategy.md`  
**Status:** PENDENTE

### FEATURE-051

**Epic:** EPIC-006  
**Nome:** Validação e Reconciliação  
**Descrição:** Reconciliação metadado/binário, validação funcional, teste de rollback.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — MIG-005 a MIG-007  
**Status:** PENDENTE

### FEATURE-052

**Epic:** EPIC-006  
**Nome:** Migração de Integrações  
**Descrição:** Frontend → Backend principal; remoção gradual APIs CMS.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — MIG-008, MIG-009  
**Status:** PENDENTE

---

## EPIC-007 — Descomissionamento

### FEATURE-060

**Epic:** EPIC-007  
**Nome:** Remoção Backend Legado  
**Descrição:** Remover Backend PHP e BackendSync.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — DEC-001, DEC-002  
**Status:** PENDENTE — pós-MVP

### FEATURE-061

**Epic:** EPIC-007  
**Nome:** Desativação API CMS Negócio  
**Descrição:** Remover API de negócio WordPress; manter conteúdo institucional.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — DEC-003, DEC-004  
**Status:** PENDENTE — pós-MVP

### FEATURE-062

**Epic:** EPIC-007  
**Nome:** Unificação de Segurança e Notificações Legadas  
**Descrição:** Remover JWT legado e notificações duplicadas AS-IS.  
**Origem:** `docs/implementation/01-implementation-backlog.md` — DEC-005, DEC-006  
**Status:** PENDENTE — pós-MVP

---

## Itens Removidos do MVP

Removidos por decisão em `docs/audit/10-mvp-consolidation-audit.md`:

| ID | Nome | Motivo |
| -- | ---- | ------ |
| FEATURE-046 | Gestão de Mensagens | Sem conceito de domínio — C-004 |
| FEATURE-070 | CRUD de Campanhas | Construction-only — C-002 |

---

## Índice de Features

| ID | Epic | Nome | Status |
| -- | ---- | ---- | ------ |
| FEATURE-001 a 005 | EPIC-001 | Fundação | PENDENTE |
| FEATURE-010 a 017 | EPIC-002 | Organização | ATIVO / PARCIAL |
| FEATURE-020 a 029 | EPIC-003 | Acesso | ATIVO / PARCIAL |
| FEATURE-030 a 037 | EPIC-004 | Documental | ATIVO |
| FEATURE-040 a 049 | EPIC-005 | Comunicação | ATIVO / PARCIAL / Opcional |
| FEATURE-050 a 052 | EPIC-006 | Migração | PENDENTE |
| FEATURE-060 a 062 | EPIC-007 | Descomissionamento | Pós-MVP |

**Total:** 36 features (FEATURE-046 e FEATURE-070 removidas do MVP)
