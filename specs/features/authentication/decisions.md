# Decisões — Authentication

| Item | Valor |
|------|-------|
| Feature ID | **FT-AUTH** |
| Projeto | Portal de Comunicação |
| Camada | Features |
| Status | **Approved** |
| Versão | 2.1 |
| Última atualização | 2026-07-24 |

---

# DA-AUTH-001 — Autenticação Centralizada via Zimbra

## Decisão Adotada

**A2** — Toda validação de credenciais ocorre exclusivamente via Zimbra.

## Status

**Approved** (2026-07-06)

---

# DA-AUTH-002 — Separação entre Autenticação e Autorização

## Decisão Adotada

**B2** — Autenticação fornece identidade; autorização consome permissões do banco do Portal.

## Status

**Approved** (2026-07-06)

---

# DA-AUTH-003 — Sessão Stateless com JWT e Refresh Token

## Contexto

Após autenticação Zimbra, o Portal precisa manter estado de sessão sem HTTP Session.

## Problema

HTTP Session acopla estado ao servidor e dificulta escalabilidade horizontal.

## Alternativas Avaliadas

| Alternativa | Descrição |
|-------------|-----------|
| C1 | HTTP Session (Servlet Session) |
| C2 | Stateless — JWT + Refresh Token em Cookies HttpOnly |
| C3 | Stateless — validar token Zimbra em cada requisição |

## Decisão Adotada

**C2** — Arquitetura Stateless com JWT próprio (Access Token, 15 min) e Refresh Token opaco (8h / 30d) em Cookies HttpOnly + Secure.

## Justificativa

- Escalabilidade horizontal sem estado no servidor
- Renovação transparente via Refresh Token
- Revogação via registro no banco
- Zimbra consultado apenas no login (DA-AUTH-008)

## Impacto

- Tabela `AUTH_SESSAO` no banco para Refresh Tokens
- SecurityFilterChain valida JWT localmente
- Sem HTTP Session

## Status

**Approved** (2026-07-08) — Substitui decisão anterior de "sessão da aplicação" genérica.

---

# DA-AUTH-004 — Não Persistência de Credenciais

## Decisão Adotada

**D2** — O Portal não armazena, processa nem persiste credenciais.

## Status

**Approved** (2026-07-06)

---

# DA-AUTH-005 — Arquitetura Stateless

## Contexto

Definição oficial do modelo de sessão do Portal.

## Decisão Adotada

O Portal adota arquitetura **Stateless** — sem HTTP Session (Servlet Session). Sessão representada por JWT + Refresh Token com registro de metadados no banco.

## Justificativa

- Escalabilidade
- Independência do Zimbra após login
- Alinhamento com Spring Security stateless

## Status

**Approved** (2026-07-08)

---

# DA-AUTH-006 — JWT Próprio do Portal

## Decisão Adotada

O Portal emite **JWT próprio** (Access Token) assinado com chave exclusiva do Portal.

| Atributo | Valor |
|----------|-------|
| TTL | 15 minutos |
| Claims | sub, sid, email, name, iat, exp |
| Armazenamento | Cookie `access_token` (HttpOnly + Secure) |
| Permissões | Não incluídas no JWT — carregadas do banco |

## Status

**Approved** (2026-07-08)

---

# DA-AUTH-007 — Refresh Token com Cookies HttpOnly

## Decisão Adotada

Refresh Token é token opaco (UUID) armazenado em Cookie HttpOnly + Secure e registrado no banco.

| Atributo | Valor |
|----------|-------|
| TTL padrão | 8 horas |
| TTL "Lembrar-me" | 30 dias |
| Cookie | `refresh_token` — HttpOnly, Secure, SameSite=Strict |
| Revogação | Via banco (`FLG_REVOGADA`) |

## Proibições

- LocalStorage / SessionStorage para qualquer token
- Refresh Token como JWT (opaco para permitir revogação imediata)

## Status

**Approved** (2026-07-08)

---

# DA-AUTH-008 — Consulta Única ao Zimbra

## Decisão Adotada

O Zimbra é consultado **exclusivamente durante o login** (callback). Nenhuma requisição subsequente do Portal consulta o Zimbra.

## Justificativa

- Reduz dependência runtime do serviço externo
- Sessões ativas permanecem funcionais durante indisponibilidade Zimbra
- Performance — validação JWT local

## Status

**Approved** (2026-07-08)

---

# DA-AUTH-009 — Permissões Mantidas pelo Portal

## Decisão Adotada

Permissões da aplicação são carregadas **exclusivamente do banco de dados do Portal**. O Zimbra fornece apenas identidade (e-mail, nome, zimbraId).

## Justificativa

- DA-AUTH-002 (separação auth/authz)
- Modelo de permissões definido em `specs/domain/05-permission-model.md`
- Independência do modelo de permissões corporativo

## Status

**Approved** (2026-07-08)

---

# DA-AUTH-010 — Sessões Simultâneas Limitadas

## Decisão Adotada

Máximo **3 sessões simultâneas** por colaborador. Ao exceder, a sessão mais antiga é automaticamente revogada.

## Justificativa

- Segurança — limita exposição de credenciais de sessão
- Controle de dispositivos

## Status

**Approved** (2026-07-08)

---

# DA-AUTH-011 — Identidade do Colaborador sem Matrícula

## Decisão Adotada (texto histórico — 2026-07-10)

A criação e localização de colaborador no login **não exige número de matrícula corporativa**. A identidade persistida é composta por identificador interno (`COD_COLABORADOR`), e-mail institucional e identificador Zimbra (`ID_ZIMBRA`), conforme DEC-DB-011.

O primeiro login, no escopo de FT-AUTH, consistia em: autenticação Zimbra → localização/criação do colaborador → verificação de ativo → criação da sessão. **Seleção de Contexto Ativo, onboarding/primeiro acesso e Home dinâmica não fazem parte de FT-AUTH** (REF-DB-CTX-01; ver FT-SESSION, FT-PRIMEIRO-ACESSO, DEC-FA-001..004).

## Supersession parcial (2026-08-14/15 — DH-03, DH-PA-01)

Os itens abaixo do texto histórico que tratam de **`locateOrCreate` no login**, **criação automática de `COLABORADOR`** e **`AUTH_SESSAO` operacional imediata** para identidade sem vínculo completo estão **superseded** pelas decisões humanas vigentes:

| Item histórico | Status após DH-03 / DH-PA-01 |
|----------------|------------------------------|
| `locateOrCreate` no login como comportamento normativo | **SUPERSEDED** — GAP-028-01 |
| Criação de `COLABORADOR` durante autenticação | **SUPERSEDED** — COLABORADOR somente após vínculo completo (DH-03) |
| `AUTH_SESSAO` operacional no login para identidade em Primeiro Acesso | **SUPERSEDED** — credencial temporária sem sessão operacional (DH-PA-01) |
| Identidade sem matrícula (e-mail + Zimbra) | **MANTIDO** |
| FT-AUTH não resolve Contexto Ativo / onboarding / Home | **MANTIDO** |

### Regra vigente (FT-AUTH — hand-off pós-autenticação)

```text
Autenticação (Zimbra)
    ↓
identidade validada
    ↓
verificação de Primeiro Acesso (FT-PRIMEIRO-ACESSO)
    ↓
se necessário → onboarding (credencial temporária — DH-PA-01)
    ↓
vínculo completo (Federação + Singular + Área + Equipe opcional)
    ↓
persistência do COLABORADOR (DH-03)
    ↓
sessão operacional + Contexto Ativo derivado do vínculo (DH-02)
```

**FATO:** A implementação AS-IS (`ColaboradorService.locateOrCreate` em `finalizeLogin`) ainda reflete o texto histórico — ver `construction/review/baseline-saneamento.md` § Pendências de implementação.

## Status

**Approved** (2026-07-10); texto alinhado em 2026-07-24; **supersession parcial** registrada em 2026-08-17 (baseline saneamento).

---

# DA-AUTH-012 — Protocolo de Integração Zimbra (Proxy de Credenciais)

## Contexto

O contrato abstrato inicial previa URLs OAuth/JSON (`ZIMBRA_AUTH_URL` / `ZIMBRA_VALIDATE_URL`) inexistentes no Zimbra corporativo.

## Problema

Narrativa OAuth induzia implementação incompatível com a infraestrutura real e com o legado `ZimbraAuth.php`.

## Decisão Adotada

O Portal adota **proxy de credenciais**:

1. Página de login do Portal coleta e-mail/senha.
2. Backend valida no Zimbra na ordem **IMAP → SMTP AUTH → SOAP**.
3. Identidade mínima (`email`, `displayName`, `zimbraId`) retorna ao Portal.
4. Callback com token opaco permanece suportado via SOAP `authToken`.

SSOT operacional: `docs/discovery/ft-auth-zimbra-homologacao.md`.  
Arquitetura normativa: `specs/architecture/authentication-architecture.md` v1.1+.

## Status

**Approved** (2026-07-24) — formaliza homologação de 2026-07-20.

---

# Rastreabilidade

| Decisão | RF relacionados | RN relacionadas |
|---------|-----------------|-----------------|
| DA-AUTH-001 | RF-AUTH-001, 011 | RN-AUTH-002 |
| DA-AUTH-002 | RF-AUTH-005, 007 | RN-AUTH-006 |
| DA-AUTH-003 | RF-AUTH-002, 004 | RN-AUTH-005 |
| DA-AUTH-004 | RF-AUTH-001 | RN-AUTH-003 |
| DA-AUTH-005 | RF-AUTH-002, 004 | RN-AUTH-005 |
| DA-AUTH-006 | RF-AUTH-002 | RN-AUTH-005 |
| DA-AUTH-007 | RF-AUTH-003, 004 | RN-AUTH-007, RN-AUTH-008 |
| DA-AUTH-008 | RF-AUTH-001 | RN-AUTH-004 |
| DA-AUTH-009 | RF-AUTH-005 | RN-AUTH-006 |
| DA-AUTH-010 | RF-AUTH-009 | RN-AUTH-009 |
| DA-AUTH-011 | RF-AUTH-008 | RN-AUTH-004 |
| DA-AUTH-012 | RF-AUTH-001, 011 | RN-AUTH-002, RNF-AUTH-006 |

---

# Referências

- `specs/architecture/authentication-architecture.md`
- `specification.md`
- `specs/domain/05-permission-model.md`
- `docs/architecture/08-decision-records.md` (ADR-003)
