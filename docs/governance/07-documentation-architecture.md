# 07 - Documentation Architecture

| Item | Valor |
|------|-------|
| Status | **Approved — Exit Gate 2026-07-24 (com ressalvas conscientes)** |
| Versão | 2.0 |
| Última atualização | 2026-07-24 |
| Categoria documental | **SSOT** (governança documental) |

## Objetivo

Estabelecer a arquitetura documental oficial do Portal de Comunicação: camadas, categorias, SSOTs e regras que impedem duplicação e documentos sem dono.

Este documento é normativo. Toda criação, movimentação ou remoção de documentação deve observá-lo.

---

# Princípios permanentes (Governança Documental)

1. Cada conhecimento possui exatamente **um SSOT**.
2. Relatórios analíticos e de investigação são **Working** (transitórios) por padrão.
3. Toda informação permanente deve ser **incorporada ao SSOT** antes de descartar Working.
4. Documentos de **Evidence** não substituem especificações nem regras de negócio.
5. **Git** é o mecanismo oficial de versionamento e histórico.
6. Nenhum documento permanece apenas para registrar estados intermediários.
7. Simplicidade, baixo acoplamento documental, alta rastreabilidade e evolução incremental.
8. Evoluir a estrutura só com **redução comprovada** de complexidade.

Novos documentos somente quando:

- não existir SSOT adequado; **ou**
- possuírem ciclo de vida independente; **ou**
- constituírem Evidence permanente (não reconstruível).

---

# Regra fundamental — questionamento obrigatório

Antes de criar ou manter qualquer documento:

## Identidade

| Pergunta |
|----------|
| Qual problema este documento resolve? |
| Quem é o responsável? |
| Quem o manterá atualizado? |
| Qual Feature ou camada é dona? |

## Existência

| Pergunta |
|----------|
| Este documento realmente precisa existir? |
| Existe outro que já responda isso? |
| Posso atualizar um documento existente? |
| Este conteúdo pertence a esta camada? |
| O Git já resolve esse histórico? |

Se qualquer resposta indicar duplicidade: **não criar, não manter, consolidar no SSOT**.

Nenhum documento permanece apenas porque “já existia”.

---

# Categorias documentais (obrigatórias)

Todo documento pertence a **exatamente uma** categoria.

## 1 — SSOT

Fonte oficial do conhecimento. Permanente. Recebe atualizações.

Exemplos: Specifications, Domain, Architecture, ADR, DEC **aprovada**, Business Rules, Open Questions ativas, este documento, `database/` (schema).

## 2 — Evidence

Comprova um fato (homologação, readiness, auditoria, validação). Não substitui SSOT. Pode permanecer.

Exemplos: `docs/discovery/ft-auth-zimbra-homologacao.md`, `docs/audit/*` (fase), closure/review de Feature, `database/reports/*`.

## 3 — Working

Produzido durante análise/construção (investigação, rascunho, RFC, relatório funcional). Transitório.

Após incorporar ao SSOT → **remover** (Git guarda histórico). Status típico: `PENDING_REMOVAL`.

## 4 — Archive

Registro histórico que **não evolui**. Não recebe regras novas. Não vira SSOT.

Exemplos: `docs/governance/history/*`, `construction/history/*`, session.md de Feature fechada.

---

# Arquitetura em camadas

```text
                        Projeto
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
      docs/             specs/         construction/
                                               │
                                           .cursor/
```

| Camada | Responsabilidade | Não deve conter |
|--------|------------------|-----------------|
| `docs/` | Conhecimento permanente do sistema (domínio, arquitetura, padrões, governança, discovery) | Progresso de PKG, estado de Feature |
| `specs/` | Comportamento esperado (Features, contratos, DoR/DoD) | Evidências de execução, review reports |
| `construction/` | Estado e execução da construção | Novas regras de negócio / specs |
| `.cursor/` | Automação de agentes | Domínio ou specs como fonte |
| `database/` | SSOT do schema Oracle | Regras de UI |
| `engineering/` | Sprint de Integração (pós-construction) | Specs de Feature |

### Dependências (unidirecionais)

```text
docs → specs → construction → .cursor
```

É proibido inverter.

---

# Mapa SSOT por tipo de informação

| Informação | SSOT | Categoria |
|------------|------|-----------|
| Arquitetura / ADR | `docs/architecture/` | SSOT |
| Domínio / BR / OQ de domínio | `docs/domain/` | SSOT |
| Tecnologia | `docs/technology/` | SSOT |
| Banco de dados (schema) | `database/` | SSOT |
| Padrões de implementação | `docs/implementation/` | SSOT |
| Especificação funcional | `specs/features/` | SSOT |
| Processo SDD | `specs/foundation/` | SSOT |
| Escopo MVP | `docs/backlog/04-mvp-scope.md` | SSOT |
| Roadmap | `docs/governance/05-roadmap.md` + `docs/solution-design/10-delivery-roadmap.md` | SSOT |
| OQs / DECs / Riscos de projeto | `docs/governance/` | SSOT |
| Fluxo frontend alvo | `docs/frontend/frontend-flow.md` | SSOT |
| Contrato API **implementado** | `docs/api/` | Evidence + espelho operacional |
| Contrato API **SDD** | `specs/features/*/api.md` | SSOT |
| Estado da Feature | `specs/features/<slug>/feature.yaml` | SSOT |
| Protocolo Zimbra homologado | `docs/discovery/ft-auth-zimbra-homologacao.md` | Evidence (SSOT operacional do protocolo) |
| Arquitetura AUTH normativa | `specs/architecture/authentication-architecture.md` | SSOT |
| Auditorias de fase | `docs/audit/` | Evidence / Archive |

---

# Diretórios com nomenclatura sensível (não mover automaticamente)

| Par | Papel | Decisão Gate Final |
|-----|-------|---------------------|
| `docs/construction/` | Padrões operacionais Sprint 0 (como construir) | **Manter** — distinto de execução; renomear só se reduzir confusão comprovada |
| `construction/` | Execução histórica / índice de workstreams | **Manter** — não é SSOT de estado da Feature (`feature.yaml`) |
| `docs/api/` | Contrato refletindo implementação | **Manter** — Evidence operacional; SDD prevalece em conflito normativo |
| `specs/*/api.md` | Contrato SDD | **Manter** — SSOT funcional |
| `docs/frontend/` | Arquitetura/fluxo FE alvo | **Manter** |
| `docs/construction/frontend/` | Padrões de construção FE | **Manter** — não duplicar regras de `docs/frontend/` |
| `docs/implementation/05-frontend-architecture.md` | Padrões implementation | **Manter** — revisar cruzamentos em evolução futura (REVIEW) |

---

# Fluxo Open Question → DEC

```text
Open Question → Discussão → DEC (alternativas + aprovação) → Implementação / registro definitivo
```

Nunca inverter. Enquanto a pergunta estiver aberta, **não** criar DEC “aberta” espelhando a OQ — manter só a OQ.

**IDs de DEC são únicos no repositório.** Antes de atribuir um ID, consultar:

- `docs/governance/03-open-decisions.md`
- `docs/architecture/08-decision-records.md` (ADR)
- `docs/technology/04-decision-log.md`

Colisão de IDs entre catálogos é defeito de governança (evidenciada no Gate Final 2026-07-24).

---

# Tipos de Open Question

Cada OQ deve declarar um tipo:

| Tipo | Onde registrar |
|------|----------------|
| Negócio / Domínio | `docs/domain/10-open-questions.md` |
| Arquitetura | `docs/domain/…` ou `docs/governance/04-open-questions.md` (QST) se for de projeto |
| Planejamento / Sprint | `docs/governance/04-open-questions.md` |
| Técnica (implementação) | Feature `decisions.md` / construction — não misturar com BR |

---

## Criação e cabeçalho mínimo

Todo **novo** documento SSOT, Evidence ou Archive deve declarar no cabeçalho:

| Campo | Obrigatório |
|-------|-------------|
| Categoria documental | Sim (SSOT / Evidence / Working / Archive) |
| Responsável / mantenedor | Sim |
| Camada / Feature dona | Sim |
| SSOT correspondente (se não for o SSOT) | Sim quando for Evidence/Working |

Working deve declarar também critério de remoção ou status `PENDING_REMOVAL` após incorporação.

## Remover automaticamente

- Working já incorporado ao SSOT
- `PENDING_REMOVAL`
- Templates vazios
- Duplicatas cujo SSOT já contém o conteúdo

## Nunca remover sem revisão humana

- SSOT
- Evidence
- Archive

Em dúvida: **não remover** — registrar em `docs/governance/reconciliation-report.md`.

---

# Critérios de validação

Arquitetura documental consistente quando:

- cada informação tem um SSOT;
- categorias estão explícitas;
- não há Working permanente sem justificativa;
- Evidence não substitui specs;
- camadas e dependências são respeitadas;
- um novo integrante encontra o mapa SSOT deste documento.

---

# Baseline Exit Gate

Em **2026-07-24** a validação Exit Gate concluiu **APROVADO COM RESSALVAS** (evidência: `docs/governance/reconciliation-report.md` § Exit Gate).

Após este marco:

- a governança é estável para desenvolvimento;
- evoluções **complementam** a estrutura (não a reinventam);
- toda criação de documento justifica existência, SSOT, categoria e ciclo de vida;
- ressalvas conscientes (catálogo DEC, índice FE, naming construction, OQs de login) permanecem registradas até resolução.

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.0 | — | Camadas docs/specs/construction/.cursor |
| 1.1 | 2026-07-24 | Audit = Archive; PENDING_REMOVAL |
| 2.0 | 2026-07-24 | Gate Final — categorias SSOT/Evidence/Working/Archive; princípios; mapa SSOT; fluxo OQ→DEC |
