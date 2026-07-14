# Go Live Readiness

## Objetivo

Validar se a solução possui condições mínimas para iniciar a fase de implementação ou preparação para produção.

**Fonte normativa MVP:** `docs/audit/10-mvp-consolidation-audit.md`  
**Data de reconciliação:** 2026-06-22

---

# Escopo MVP

## Funcionalidades Incluídas

| Módulo | Etapa | Épico | Status |
| ------ | ----- | ----- | ------ |
| Fundação da Plataforma | 1 | EPIC-001 | Obrigatório |
| Organização Corporativa | 2 | EPIC-002 | Obrigatório |
| Controle de Acesso | 2 | EPIC-003 | Obrigatório |
| Gestão Documental | 3 | EPIC-004 | Obrigatório |
| Comunicação Interna (Notificações + Comunicados PARCIAL) | 4 | EPIC-005 | Obrigatório |
| Migração Operacional | 5 | EPIC-006 | Obrigatório |

### Capacidades obrigatórias

* Autenticação Zimbra + sessão
* Autorização papel + escopo
* Estrutura organizacional (singulares, áreas, equipes, vínculos)
* Publicação e consulta documental
* Download autorizado
* Separação metadado/binário
* Notificações in-app unificadas
* Migração núcleo AS-IS → TO-BE
* Observabilidade base (logs, health)

### Capacidades PARCIAL

| Capacidade | Condição |
| ---------- | -------- |
| Comunicados institucionais | OQ-004 |
| Auditoria inicial | OQ-019 |
| Compartilhamento ↔ autorização | OQ-005 |

---

## Funcionalidades Fora do MVP

| Funcionalidade | Motivo |
| -------------- | ------ |
| Gestão de Campanhas / EPIC-008 | Sem bounded context — C-002 |
| Gestão de Mensagens / FEATURE-046 | Sem conceito de domínio — C-004 |
| Métricas Administrativas / FEATURE-044 | Opcional R-016 — C-003 |
| Descomissionamento / EPIC-007 | Etapa 6 — pós-MVP |
| Onboarding oficial | OQ-001 |
| Aplicativo Mobile, Multi-idioma, IA, Analytics avançado | Exclusões documentadas |

---

# Readiness Checklist

## Negócio

* [x] Regras de negócio documentadas
* [x] Glossário aprovado
* [x] QST-001 (escopo MVP) encerrada

---

## Arquitetura

* [x] Arquitetura alvo aprovada
* [x] Integrações aprovadas
* [x] Estratégia de segurança definida
* [x] Estratégia de deploy definida

---

## Implementação

* [x] Estrutura de repositório definida
* [x] Padrões de desenvolvimento definidos
* [x] Estratégia de testes definida
* [x] Estratégia de observabilidade definida

---

## Infraestrutura

* [x] Ambientes definidos
* [x] Docker definido
* [x] CI/CD definido
* [x] Monitoramento definido

---

# Dependências Externas

| Dependência | Responsável | Status |
| ----------- | ----------- | ------ |
| Zimbra (autenticação corporativa) | Infraestrutura | Documentada — Etapa 2 |
| WordPress CMS (conteúdo institucional) | Arquitetura | Documentada — transição Etapa 5 |

---

# Riscos Remanescentes

| Risco | Impacto | Mitigação |
| ----- | ------- | --------- |
| R-001 a R-003 | Crítico | Acompanhar todas as fases (`10-delivery-roadmap.md`) |
| R-005, R-006, R-008 | Alto | Concentram-se em Migração e Descomissionamento |
| OQ-004, OQ-005 | Médio | Capacidades PARCIAL no MVP — R-007 |

---

# Critérios para Início da Construção

Todos os itens abaixo devem estar atendidos:

* [x] Domain concluído
* [x] Architecture concluída
* [x] Solution Design concluído
* [x] Implementation concluída
* [x] MVP aprovado (`docs/audit/10-mvp-consolidation-audit.md`)

---

# Critérios para Go Live

Todos os itens abaixo devem estar atendidos:

* [ ] Testes executados
* [ ] Homologação aprovada
* [ ] Plano de rollback definido
* [ ] Monitoramento ativo
* [ ] Logs centralizados
* [ ] Observabilidade validada
* [ ] Deploy automatizado validado

---

# Aprovação

| Papel           | Responsável | Data |
| --------------- | ----------- | ---- |
| Produto         | TBD         | TBD  |
| Arquitetura     | TBD         | TBD  |
| Desenvolvimento | TBD         | TBD  |
| Infraestrutura  | TBD         | TBD  |
