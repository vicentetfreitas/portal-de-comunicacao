# Authentication

| Item | Valor |
|------|-------|
| Feature ID | **FT-AUTH** |
| Projeto | Portal de Comunicação |
| Camada | Features |
| Status | **Approved** |
| Versão | 2.2 |
| Última atualização | 2026-07-09 |

---

# Objetivo

Implementar o mecanismo de autenticação do Portal de Comunicação utilizando o **Zimbra** como Provedor de Identidade e arquitetura **Stateless** com JWT próprio e Refresh Token.

A autenticação deve garantir a identificação do colaborador, o estabelecimento de sessão via tokens em Cookies HttpOnly e o fornecimento das informações necessárias para que os mecanismos de autorização controlem o acesso aos recursos da aplicação.

---

# Problema

O Portal de Comunicação necessita de um mecanismo padronizado e seguro para autenticar colaboradores, eliminando dependências de autenticação local e centralizando a validação de credenciais no **Zimbra**.

Após autenticação, o Portal deve controlar integralmente a sessão sem consultas subsequentes ao Zimbra, carregando permissões do banco de dados do Portal.

---

# Objetivos da Feature (Goals)

- Autenticar colaboradores via Zimbra (única consulta no login).
- Emitir Access Token (JWT, 15 min) e Refresh Token (8h / 30d com "Lembrar-me").
- Armazenar tokens em Cookies HttpOnly + Secure.
- Renovar Access Token automaticamente via Refresh Token.
- Disponibilizar identidade e vínculos organizacionais do colaborador via `/api/v1/auth/me`.
- Encerrar sessões de forma segura (logout, expiração, revogação administrativa).
- Limitar sessões simultâneas a 3 dispositivos por colaborador.
- Proteger recursos contra acesso não autenticado.
- Auditar eventos de autenticação.

---

# Non-Goals

Esta Feature **não** tem como objetivo:

- Implementação frontend da Feature (login, guards, interceptadores) — fora do escopo da Sprint 1.
- Gerenciamento de usuários.
- Cadastro manual de colaboradores (criação automática no login).
- Administração de perfis e permissões (Features futuras).
- Recuperação de senha (responsabilidade do Zimbra).
- Administração do Zimbra.

---

# Escopo

## Escopo desta Sprint (Sprint 1 — Backend)

Esta Sprint entrega **exclusivamente o backend** da Feature FT-AUTH. A implementação frontend (login redirect, interceptadores HTTP, guards, CSRF no cliente, renovação transparente) **não faz parte desta Sprint** e será tratada em Sprint posterior.

### Inclui (Sprint 1)

- Processo de autenticação via Zimbra (backend).
- Emissão de JWT e Refresh Token.
- Armazenamento em Cookies HttpOnly + Secure (definido pelo backend).
- Renovação de Access Token via endpoint `POST /api/v1/auth/refresh`.
- Logout com revogação de Refresh Token.
- Consulta de identidade autenticada (`GET /api/v1/auth/me`).
- Localização ou criação automática de colaborador no login.
- Controle de sessões simultâneas (máx. 3).
- Revogação administrativa de sessões com autorização no backend.
- Proteção CSRF em fluxos autenticados por Cookie.
- Auditoria de eventos de autenticação.
- Tratamento de indisponibilidade do Zimbra.

### Não inclui (Sprint 1)

- Implementação frontend (FE-001 a FE-011) — **fora de escopo nesta Sprint**.
- Administração de perfis e permissões (Feature futura).
- Seleção de Singular, Área e Equipe (previsto em Features de organização).
- Recuperação de senha.
- Provisionamento de contas no Zimbra.

## Escopo da Feature (visão completa)

### Inclui

- Processo de autenticação via Zimbra.
- Emissão de JWT e Refresh Token.
- Armazenamento em Cookies HttpOnly + Secure.
- Renovação automática de Access Token.
- Logout com revogação de Refresh Token.
- Consulta de identidade autenticada (`/api/v1/auth/me`).
- Localização ou criação automática de colaborador no login.
- Controle de sessões simultâneas (máx. 3).
- Revogação administrativa de sessões.
- Proteção CSRF em fluxos autenticados por Cookie.
- Auditoria de eventos de autenticação.
- Tratamento de indisponibilidade do Zimbra.
- Frontend de autenticação (Sprint posterior).

### Não inclui

- Administração de perfis e permissões.
- Seleção de Singular, Área e Equipe (previsto em Features de organização).
- Recuperação de senha.
- Provisionamento de contas no Zimbra.

---

# Arquitetura Definitiva

Documentação completa em `specs/architecture/authentication-architecture.md`.

```text
Zimbra (Identity Provider)
    ↓ consulta única no login
Portal (Backend)
    ↓ emite JWT + Refresh Token
Cookies HttpOnly + Secure
    ↓ renovação automática
Sessão Stateless (sem HTTP Session)
    ↓ permissões
Banco de Dados do Portal
```

| Decisão | Escolha |
|---------|---------|
| Provedor de identidade | Zimbra |
| Modelo de sessão | Stateless |
| Access Token | JWT próprio (TTL 15 min) |
| Refresh Token | Token opaco (TTL 8h / 30d) |
| Armazenamento | Cookies HttpOnly + Secure |
| LocalStorage/SessionStorage | Proibido |
| Consulta ao Zimbra | Apenas no login |
| Permissões | Banco de dados do Portal |
| Sessões simultâneas | Máximo 3 dispositivos |
| HTTPS | Obrigatório |
| CSRF | Obrigatório |

---

# Conceitos de Sessão

Esta seção define a terminologia oficial da Feature para orientar implementação sem decisões adicionais. Os termos abaixo complementam `specs/foundation/glossary.md` no escopo de autenticação.

## Sessão

A **sessão** representa a autenticação ativa de um colaborador no Portal.

| Atributo | Definição |
|----------|-----------|
| Controle | Integralmente pelo Portal após o login (RN-AUTH-005) |
| Identificador | `session_id` — registrado na tabela `AUTH_SESSAO` |
| Tokens associados | Access Token (JWT) e Refresh Token (opaco) |
| Armazenamento dos tokens | Cookies HttpOnly + Secure (`access_token`, `refresh_token`) |
| Modelo | Stateless — sem HTTP Session (Servlet Session) |

Uma sessão é criada no login bem-sucedido (RF-AUTH-001, RF-AUTH-002) e encerrada por logout, expiração do Refresh Token, revogação por limite de sessões simultâneas ou revogação administrativa.

## Sessão Ativa

Sessão cujo Refresh Token **não** está revogado (`FLG_REVOGADA = N`) e **não** expirou. Permite renovação do Access Token e continuidade da autenticação.

## Sessão Revogada

Uma **sessão revogada** é uma sessão que deixou de ser válida para continuidade da autenticação.

Após a revogação:

- o Refresh Token torna-se **imediatamente inválido** (`FLG_REVOGADA = S`);
- novas renovações de Access Token são **proibidas**;
- a sessão **não pode** voltar ao estado ativo;
- o colaborador deverá realizar um **novo login** para criar uma nova sessão.

A revogação é persistida no banco (`AUTH_SESSAO`) — mecanismo adotado pela arquitetura Stateless (DA-AUTH-003, DA-AUTH-007). Não há blacklist de JWT nem introspecção de tokens.

## Comportamento do Access Token após Revogação

Consequência direta da arquitetura **Stateless** adotada pela Feature (DA-AUTH-005):

| Aspecto | Comportamento |
|---------|---------------|
| Invalidação imediata | O Access Token **não** é invalidado imediatamente após revogação |
| Validade residual | Permanece válido até o término natural do seu TTL (**15 minutos**) |
| Requisições protegidas | Enquanto não expirado, o JWT continua aceito pelo `SecurityFilterChain` (validação local de assinatura e `exp`) |
| Renovação | Após expiração do Access Token, qualquer tentativa de renovação utilizando Refresh Token revogado retorna **HTTP 401** |
| Frontend | Deve redirecionar o colaborador para novo login ao receber HTTP 401 na renovação |

Este comportamento aplica-se igualmente a logout, revogação administrativa e revogação automática por limite de sessões simultâneas. Não implica alteração de TTL, fluxo de autenticação ou introdução de HTTP Session.

---

# Encerramento de Sessão

## Logout (RF-AUTH-006)

Encerramento de sessão **iniciado pelo próprio colaborador**.

| Aspecto | Comportamento |
|---------|---------------|
| Ator iniciador | Colaborador |
| Endpoint | `POST /api/v1/auth/logout` (AUTH-API-004) |
| Refresh Token | Revogado imediatamente no banco |
| Cookies | `access_token` e `refresh_token` removidos |
| Access Token residual | Válido até TTL natural (ver seção anterior) |
| Participação do colaborador | Obrigatória — ação explícita no frontend |

## Revogação Administrativa (RF-AUTH-010)

Invalidação de sessão **iniciada por um administrador**, sem dependência da participação do colaborador.

| Aspecto | Comportamento |
|---------|---------------|
| Ator iniciador | Administrador (perfil autorizado) |
| Autorização | O backend deve validar que o solicitante possui perfil de **administrador** antes de executar a revogação; colaboradores sem autorização recebem **HTTP 403** |
| Endpoint | `DELETE /api/v1/admin/sessions/{sessionId}` (TASK-AUTH-BE-020) |
| Alvo | Sessão existente identificada por `session_id` |
| Refresh Token | Revogado imediatamente no banco (`FLG_REVOGADA = S`) |
| Renovações futuras | Proibidas — `POST /api/v1/auth/refresh` retorna HTTP 401 |
| Cookies do colaborador | Não removidos no momento da revogação administrativa; removidos quando o colaborador tentar renovar (`POST /api/v1/auth/refresh` → HTTP 401) ou realizar logout |
| Access Token residual | Válido até TTL natural; após expiração, renovação falha com HTTP 401 |
| Reativação | Impossível — novo login obrigatório para nova sessão |
| Auditoria | Evento de revogação administrativa registrado (RNF-AUTH-007) |

A revogação administrativa é exposta via `DELETE /api/v1/admin/sessions/{sessionId}` (TASK-AUTH-BE-020), com validação de autorização de administrador no backend (RN-AUTH-013). O efeito sobre a continuidade da autenticação é idêntico ao do logout quanto ao Refresh Token; a diferença está no ator, na exigência de perfil administrativo, na ausência de remoção automática de cookies e na independência da ação do colaborador.

## Diferença entre Logout e Revogação Administrativa

| Critério | Logout | Revogação Administrativa |
|----------|--------|--------------------------|
| Iniciador | Colaborador | Administrador |
| Revoga Refresh Token | Sim, imediato | Sim, imediato |
| Remove cookies | Sim (imediatamente) | Não no ato da revogação; sim na tentativa de refresh com HTTP 401 |
| Impede renovações futuras | Sim | Sim |
| Access Token invalidado imediatamente | Não | Não |
| Exige participação do colaborador | Sim | Não |
| Autorização no backend | Não aplicável | Obrigatória — apenas administrador |
| Novo login para nova sessão | Sim | Sim |

---

# Stakeholders

| Papel | Responsabilidade |
|-------|------------------|
| Colaborador | Acessar o Portal, autenticar-se e encerrar sessão |
| Administrador | Configurar integração Zimbra; revogar sessões |
| Equipe de Desenvolvimento | Implementar integração Zimbra e gerenciamento de sessão |
| Equipe de Infraestrutura | Disponibilizar conectividade segura com Zimbra |

---

# Requisitos Funcionais

| Identificador | Requisito |
|---------------|-----------|
| RF-AUTH-001 | O Portal deve autenticar colaboradores exclusivamente via Zimbra, consultando-o apenas durante o login |
| RF-AUTH-002 | O Portal deve emitir Access Token (JWT, 15 min) e Refresh Token após autenticação bem-sucedida |
| RF-AUTH-003 | O Portal deve armazenar tokens em Cookies HttpOnly + Secure |
| RF-AUTH-004 | O Portal deve renovar automaticamente o Access Token via Refresh Token |
| RF-AUTH-005 | O Portal deve disponibilizar identidade autenticada via `GET /api/v1/auth/me` |
| RF-AUTH-006 | O Portal deve encerrar a sessão atual do colaborador via logout: revogar o Refresh Token, remover cookies e impedir renovações futuras dessa sessão |
| RF-AUTH-007 | O Portal deve restringir acesso a recursos protegidos a colaboradores autenticados |
| RF-AUTH-008 | O Portal deve localizar ou criar automaticamente o colaborador no banco após autenticação Zimbra |
| RF-AUTH-009 | O Portal deve limitar sessões simultâneas a 3 dispositivos por colaborador |
| RF-AUTH-010 | O Portal deve suportar revogação administrativa de sessões: exclusivamente um **administrador** autorizado pode invalidar uma sessão existente por `session_id`, com validação de autorização no backend, revogando imediatamente o Refresh Token e impedindo renovações futuras, sem participação do colaborador |
| RF-AUTH-011 | O Portal deve tratar falhas de autenticação e indisponibilidade do Zimbra |

---

# Regras de Negócio

| Identificador | Regra |
|---------------|-------|
| RN-AUTH-001 | Apenas colaboradores autenticados podem acessar recursos protegidos |
| RN-AUTH-002 | Toda validação de credenciais ocorre exclusivamente no Zimbra |
| RN-AUTH-003 | O Portal não armazena credenciais de colaboradores |
| RN-AUTH-004 | O Portal confia na identidade retornada pelo Zimbra apenas no momento do login |
| RN-AUTH-005 | Após o login, a sessão é controlada integralmente pelo Portal via JWT e Refresh Token |
| RN-AUTH-006 | Permissões da aplicação são carregadas exclusivamente do banco de dados do Portal |
| RN-AUTH-007 | Tokens nunca são armazenados em LocalStorage ou SessionStorage |
| RN-AUTH-008 | Logout (RF-AUTH-006) revoga o Refresh Token da sessão atual, remove cookies e impede renovações futuras dessa sessão |
| RN-AUTH-009 | Máximo 3 sessões simultâneas por colaborador; excedente revoga a mais antiga |
| RN-AUTH-010 | "Lembrar-me" estende Refresh Token para 30 dias |
| RN-AUTH-011 | Revogação administrativa (RF-AUTH-010) revoga imediatamente o Refresh Token da sessão alvo, impede renovações futuras e torna impossível reativar a mesma sessão — novo login obrigatório |
| RN-AUTH-012 | Após revogação de sessão (logout, administrativa ou por limite de dispositivos), o Access Token permanece válido até o TTL natural de 15 minutos; tentativa de renovação com Refresh Token revogado retorna HTTP 401 — consequência da arquitetura Stateless (DA-AUTH-005) |
| RN-AUTH-013 | Revogação administrativa (RF-AUTH-010) é restrita a colaboradores com perfil de **administrador**; o backend deve rejeitar solicitantes não autorizados com HTTP 403 |

---

# Requisitos Não Funcionais

| Identificador | Requisito |
|---------------|-----------|
| RNF-AUTH-001 | HTTPS obrigatório em todos os ambientes |
| RNF-AUTH-002 | Tokens exclusivamente em Cookies HttpOnly + Secure |
| RNF-AUTH-003 | Access Token TTL fixo de 15 minutos |
| RNF-AUTH-004 | Refresh Token TTL de 8 horas (padrão) ou 30 dias ("Lembrar-me") |
| RNF-AUTH-005 | CSRF obrigatório em requisições mutáveis autenticadas por Cookie |
| RNF-AUTH-006 | Zimbra consultado apenas durante o login; timeout de comunicação de **10 segundos**, configurável via propriedade `application.zimbra.timeout-ms` (padrão: `10000`) |
| RNF-AUTH-007 | Eventos auditáveis: login, logout, falha, expiração, renovação, revogação administrativa |
| RNF-AUTH-008 | Informações sensíveis nunca registradas em logs |
| RNF-AUTH-009 | JWT assinado com chave exclusiva do Portal |
| RNF-AUTH-010 | Renovação de Access Token transparente ao usuário (comportamento frontend — fora do escopo da Sprint 1) |

---

# Premissas

- O Zimbra está disponível e operacional.
- Backend e Zimbra possuem comunicação segura (HTTPS).
- O Zimbra retorna e-mail, nome e identificador mínimo.
- A identidade do colaborador no banco não inclui número de matrícula corporativa (DEC-DB-011 / DA-AUTH-011).
- Infraestrutura Sprint 0 (DTOs, exception handler, logging) está aprovada.

---

# Restrições

- Não utilizar HTTP Session (Servlet Session).
- Não armazenar senhas no Portal.
- Não consultar Zimbra após o login.
- Não fornecer permissões via Zimbra.
- Respeitar prefixo `/api/v1` para todos os endpoints.

---

# Dependências

## Funcionais

Nenhuma Feature prévia. Esta é a Feature base.

## Técnicas

- Zimbra (Provedor de Identidade).
- Infraestrutura Sprint 0 (shared DTOs, exception handler, logging).
- Banco de dados Oracle (tabela `AUTH_SESSAO`, colaborador).
- HTTPS / TLS.
- Propriedade `application.zimbra.timeout-ms` para timeout de integração Zimbra (padrão: 10000 ms).

---

# Impactos

| Área | Impacto |
|------|---------|
| Backend | SecurityFilterChain, JWT, cookies, integração Zimbra, persistência de sessão, autorização administrativa |
| Frontend | Fora do escopo da Sprint 1; impacto previsto em Sprint posterior (login redirect, interceptadores, guards, CSRF) |
| Banco de Dados | Tabela `AUTH_SESSAO`; localização/criação de colaborador |
| Segurança | CSRF, cookies HttpOnly, revogação |
| Observabilidade | Auditoria de eventos de autenticação |

---

# Critérios de Aceite

A Feature será considerada aceita quando:

- colaboradores autenticam via Zimbra com sucesso;
- JWT e Refresh Token emitidos em Cookies HttpOnly;
- renovação automática de Access Token funcional;
- logout revoga Refresh Token da sessão atual e remove cookies;
- após revogação de sessão, Access Token permanece válido até TTL de 15 minutos; renovação com Refresh revogado retorna HTTP 401;
- `/api/v1/auth/me` retorna identidade via `ApiResponse`;
- limite de 3 sessões simultâneas respeitado;
- revogação administrativa invalida sessão, exige autorização de administrador no backend e impede renovações futuras;
- indisponibilidade do Zimbra tratada (503);
- todos os cenários de `acceptance-tests.md` aprovados;
- Definition of Done satisfeita.

---

# Glossário da Feature

| Termo | Definição |
|-------|-----------|
| **Sessão** | Autenticação ativa de um colaborador no Portal, controlada pelo Portal após o login, associada a Access Token, Refresh Token e `session_id` |
| **Sessão ativa** | Sessão com Refresh Token não revogado e não expirado |
| **Sessão revogada** | Sessão invalidada para continuidade da autenticação; Refresh Token imediatamente inválido; renovações proibidas; reativação impossível |
| **Logout** | Encerramento de sessão iniciado pelo colaborador; revoga Refresh Token e remove cookies |
| **Revogação administrativa** | Invalidação de sessão iniciada exclusivamente por administrador autorizado; backend valida autorização (HTTP 403 se negado); revoga Refresh Token; cookies removidos na tentativa de refresh com HTTP 401 |
| **Administrador** | Colaborador com perfil autorizado a executar operações administrativas, incluindo revogação de sessões (RF-AUTH-010) |
| **Access Token** | JWT próprio do Portal (TTL 15 min); validado localmente; não invalidado imediatamente na revogação de sessão |
| **Refresh Token** | Token opaco (TTL 8h / 30d); revogável imediatamente via banco (`FLG_REVOGADA`) |
| **Renovação** | Emissão de novo Access Token via `POST /api/v1/auth/refresh`; bloqueada quando Refresh Token revogado (HTTP 401) |

---

# Artefatos Relacionados

| Documento | Status |
|-----------|--------|
| specification.md | Approved |
| use-cases.md | Approved |
| api.md | Approved |
| acceptance-tests.md | Approved |
| tasks.md | Approved |
| decisions.md | Approved |
| authentication-architecture.md | Approved |

---

# Matriz de Rastreabilidade

| RF | RN | UC | API | AC | TASK |
|----|----|----|-----|-----|------|
| RF-AUTH-001 | RN-AUTH-002, RN-AUTH-003 | UC-AUTH-001 | AUTH-API-001, AUTH-API-002 | AC-AUTH-001, AC-AUTH-002, AC-AUTH-014 | TASK-AUTH-BE-003, TASK-AUTH-BE-004, TASK-AUTH-INT-001 |
| RF-AUTH-002 | RN-AUTH-005 | UC-AUTH-001 | AUTH-API-002 | AC-AUTH-001 | TASK-AUTH-BE-013, TASK-AUTH-BE-014 |
| RF-AUTH-003 | RN-AUTH-007 | UC-AUTH-001 | AUTH-API-002 | AC-AUTH-013 | TASK-AUTH-BE-015 |
| RF-AUTH-004 | RN-AUTH-005 | UC-AUTH-005 | AUTH-API-005 | AC-AUTH-008, AC-AUTH-009 | TASK-AUTH-BE-016, TASK-AUTH-FE-007 |
| RF-AUTH-005 | RN-AUTH-006 | UC-AUTH-004 | AUTH-API-003 | AC-AUTH-007 | TASK-AUTH-BE-005, TASK-AUTH-FE-003 |
| RF-AUTH-006 | RN-AUTH-008, RN-AUTH-012 | UC-AUTH-002 | AUTH-API-004 | AC-AUTH-004 | TASK-AUTH-BE-006, TASK-AUTH-BE-017 |
| RF-AUTH-007 | RN-AUTH-001 | UC-AUTH-003 | AUTH-API-003 | AC-AUTH-003, AC-AUTH-005 | TASK-AUTH-BE-008 |
| RF-AUTH-008 | RN-AUTH-004 | UC-AUTH-001 | AUTH-API-002 | AC-AUTH-001 | TASK-AUTH-BE-018 |
| RF-AUTH-009 | RN-AUTH-009, RN-AUTH-012 | UC-AUTH-001 | AUTH-API-002 | AC-AUTH-011 | TASK-AUTH-BE-019 |
| RF-AUTH-010 | RN-AUTH-011, RN-AUTH-012, RN-AUTH-013 | UC-AUTH-006, UC-AUTH-005 | AUTH-API-005 | AC-AUTH-010 | TASK-AUTH-BE-020 |
| RF-AUTH-011 | RN-AUTH-002 | UC-AUTH-001 | AUTH-API-001, AUTH-API-002 | AC-AUTH-002, AC-AUTH-014 | TASK-AUTH-BE-009, TASK-AUTH-INT-003 |

---

# Checklist de Prontidão (Approved)

- [x] Specification concluída
- [x] Use Cases concluídos
- [x] API concluída
- [x] Acceptance Tests concluídos
- [x] Tasks concluídas
- [x] Decisions concluídas
- [x] Arquitetura de autenticação documentada
- [x] Rastreabilidade completa
- [x] Sem inconsistências entre artefatos
- [x] Bloqueantes L-01 e L-02 eliminados

---

# Status

**Approved**

A especificação da Feature Authentication foi consolidada com arquitetura Stateless, JWT próprio, integração Zimbra e gerenciamento de sessão via Cookies HttpOnly.

**Sprint 1:** escopo limitado ao **backend**. Frontend fora desta Sprint.

A Feature backend está apta para implementação e validação (Sprint 1).

---

# Histórico

| Data | Autor | Descrição |
|------|-------|-----------|
| 06/07/2026 | Equipe de Engenharia | Criação inicial |
| 07/07/2026 | Equipe de Engenharia | Revisão e padronização terminológica |
| 08/07/2026 | Equipe de Arquitetura | Golden Template — FT-AUTH |
| 08/07/2026 | Equipe de Arquitetura | v2.0 — Arquitetura Stateless, JWT, Zimbra, eliminação L-01/L-02 |
| 09/07/2026 | Equipe de Engenharia | v2.1 — RF-AUTH-010: conceitos de sessão, revogação administrativa, comportamento do Access Token e glossário |
| 09/07/2026 | Equipe de Engenharia | v2.2 — RNF-AUTH-006 (`timeout-ms` 10s), authz admin backend, escopo Sprint 1 backend-only |
