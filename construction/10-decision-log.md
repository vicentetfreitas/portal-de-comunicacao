# Decision Log — Sprint 1A

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Construction |
| Sprint | 1A — Platform Foundation |
| Papel | **Decision Ledger oficial** |
| Consumidor primário | Construction Orchestrator, Reviewer, Auditor |
| Complementa | `construction/07-open-decisions.md` |
| Status | Ativo |
| Versão | 1.0 |
| Última atualização | 2026-07-09 |

---

# Objetivo

Registrar todas as decisões técnicas **efetivamente aprovadas e implementadas** durante a construção da Platform Foundation (Sprint 1A).

Este documento funciona como **Decision Ledger** — registro imutável e auditável de escolhas técnicas que deixaram de ser hipóteses e passaram a fazer parte da implementação.

**Princípio fundamental:** nenhuma decisão relevante implementada deve existir apenas em código. Toda decisão aprovada, revisada e auditada deve possuir registro neste documento.

**Este documento NÃO substitui** `construction/07-open-decisions.md`. Os dois possuem responsabilidades distintas e complementares.

---

# Escopo

## Registrar

Decisões relacionadas à implementação dos módulos da Platform Foundation:

| Módulo | Prefixo | Package |
|--------|---------|---------|
| Configuration Foundation | PF-CONF | PKG-01 |
| Persistence Foundation | PF-PERS | PKG-02 |
| Security Foundation | PF-SEC | PKG-03 |
| Integration Foundation | PF-INT | PKG-04 |
| Web Foundation | PF-WEB | PKG-05 |
| Observability Foundation | PF-OBS | PKG-06 |
| Testing Foundation | PF-TEST | PKG-07 |

Inclui decisões de:

- padrões de implementação adotados;
- bibliotecas e frameworks selecionados;
- estrutura de pacotes e componentes;
- configuração e integração técnica;
- estratégias de teste da Platform Foundation.

## Não registrar

| Categoria | Motivo |
|-----------|--------|
| Decisões funcionais da FT-AUTH | Escopo de Feature — `specs/features/` |
| Decisões de negócio | Fora do escopo técnico da Sprint 1A |
| Decisões da Sprint 0 | Já encerradas — `docs/governance/` |
| Hipóteses e discussões | Não implementadas |
| Ideias em avaliação | Aguardam processo em `07-open-decisions.md` |
| Decisões pendentes | Pertencem a `07-open-decisions.md` |
| Decisões rejeitadas | Não foram implementadas |

---

# Processo de Registro

Uma decisão somente poderá ser adicionada a este documento quando **todos** os critérios forem atendidos:

```text
Decisão identificada (07-open-decisions.md ou durante implementação)
   ↓
Aprovada pelo Construction Orchestrator
   ↓
Implementada pelo agente responsável (construction-engineer / auditor)
   ↓
Revisada — reviewer (Aprovado)
   ↓
Auditada — auditor (Conforme)
   ↓
Registrada em 10-decision-log.md com status APPROVED
   ↓
Removida de 07-open-decisions.md (quando aplicável)
```

### Gatilhos de registro

| Momento | Responsável | Ação |
|---------|-------------|------|
| Package em `IN_AUDIT` ou `APPROVED` | Construction Orchestrator | Verificar decisões implementadas no pacote |
| Encerramento de CD-S1A-* | Construction Orchestrator | Migrar decisão de `07` para `10` |
| Construction Audit (PKG-08) | `auditor` | Validar completude do Decision Log |

### Regras de publicação

1. Atribuir próximo ID disponível (`DL-S1A-XXX`) — sequencial, sem reutilização
2. Preencher **todos** os campos do Template Oficial
3. Status sempre `APPROVED`
4. Registrar Data da aprovação final (pós-auditoria)
5. Atualizar Índice e Estatísticas
6. Nunca alterar identificadores já publicados

---

# Convenção de Identificadores

| Elemento | Padrão |
|----------|--------|
| Prefixo | `DL-S1A` (Decision Log — Sprint 1A) |
| Formato | `DL-S1A-001`, `DL-S1A-002`, `DL-S1A-003`, … |
| Sequência | Numérica, três dígitos, iniciando em 001 |

**Regras:**

- Nunca reutilizar identificadores
- Nunca alterar identificadores já publicados
- IDs são imutáveis após publicação
- Correções de conteúdo permitem nova versão do campo, não novo ID

**Distinção de prefixos:**

| Prefixo | Documento | Significado |
|---------|-----------|-------------|
| `CD-S1A-*` | `07-open-decisions.md` | Construction Decision — pendente |
| `DL-S1A-*` | `10-decision-log.md` | Decision Log — aprovada e implementada |

---

# Template Oficial

Cada decisão registrada deve seguir **exatamente** o formato abaixo.

```markdown
## DL-S1A-XXX — Título

### Package

PKG-XX — Nome do Package

### Módulo

Configuration | Persistence | Security | Integration | Web | Observability | Testing

### Componente

Nome técnico do componente afetado.

### Decisão

Descrever a decisão tomada de forma clara e objetiva.

### Alternativas Avaliadas

| Alternativa | Descrição | Motivo de rejeição |
|-------------|-----------|-------------------|
| A | ... | ... |
| B | ... | ... |

### Justificativa

Explicar por que a decisão foi tomada.

### Impacto

Baixo | Médio | Alto

### Dependências

Listar packages, módulos ou componentes afetados.

### Artefatos Impactados

- `caminho/do/arquivo.java`
- `construction/platform-foundation/<modulo>/...`

### Data

YYYY-MM-DD

### Responsável

Agente ou papel que implementou a decisão.

### Reviewer

`reviewer` — parecer de revisão.

### Auditor

`auditor` — parecer de auditoria.

### Status

APPROVED
```

---

# Índice

## Decisões Registradas

| ID | Package | Módulo | Componente | Impacto | Data | Status |
|----|---------|--------|------------|---------|------|--------|
| DL-S1A-WF-001 | — | Governança | Feature-Oriented Workflow | Médio | 2026-07-09 | Aprovado |

> Decisões aparecerão nesta tabela após conclusão do ciclo completo (implementação → review → audit).

## Exemplos de Referência

| ID | Package | Módulo | Componente | Impacto | Tipo |
|----|---------|--------|------------|---------|------|
| DL-S1A-001 | PKG-01 | Configuration | ConfigurationProperties | Baixo | Exemplo |
| DL-S1A-002 | PKG-04 | Integration | RestClient | Médio | Exemplo |

---

# Estatísticas

| Métrica | Valor |
|---------|-------|
| Total de decisões registradas | 0 |
| Exemplos de referência | 2 |

## Por Módulo

| Módulo | Quantidade |
|--------|------------|
| Configuration (PF-CONF) | 0 |
| Persistence (PF-PERS) | 0 |
| Security (PF-SEC) | 0 |
| Integration (PF-INT) | 0 |
| Web (PF-WEB) | 0 |
| Observability (PF-OBS) | 0 |
| Testing (PF-TEST) | 0 |

## Por Package

| Package | Quantidade |
|---------|------------|
| PKG-01 | 0 |
| PKG-02 | 0 |
| PKG-03 | 0 |
| PKG-04 | 0 |
| PKG-05 | 0 |
| PKG-06 | 0 |
| PKG-07 | 0 |
| PKG-08 | 0 |

## Por Impacto

| Impacto | Quantidade |
|---------|------------|
| Baixo | 0 |
| Médio | 0 |
| Alto | 0 |

---

# Decisões Registradas

> Nenhuma decisão implementada registrada até o momento. A Sprint 1A encontra-se em pré-implementação.

As entradas reais serão adicionadas aqui conforme os Packages avançarem no Workflow Controller e atingirem estado `APPROVED`.

---

# Exemplos

Os exemplos abaixo ilustram o formato oficial. **Não constituem decisões registradas** — servem como referência para futuras entradas.

---

## DL-S1A-001 — ConfigurationProperties via @EnableConfigurationProperties

> **Tipo:** Exemplo de referência — não registrado

### Package

PKG-01 — Configuration Foundation

### Módulo

Configuration

### Componente

ConfigurationProperties

### Decisão

Utilizar `@EnableConfigurationProperties` para registrar beans de properties tipadas (`SecurityProperties`, `PersistenceProperties`, `IntegrationProperties`, `ZimbraProperties`) em classes de configuração dedicadas por módulo.

### Alternativas Avaliadas

| Alternativa | Descrição | Motivo de rejeição |
|-------------|-----------|-------------------|
| `@Configuration` com `@Bean` manual | Criação explícita de beans de properties | Verboso; duplica binding já oferecido pelo Spring Boot |
| `@ConfigurationPropertiesScan` global | Scan automático de todas as properties | Menos explícito; dificulta controle por módulo |

### Justificativa

Padronização com Spring Boot 4 e alinhamento ao padrão já estabelecido na Sprint 0 com `ApplicationProperties`. `@EnableConfigurationProperties` oferece binding tipado, validação declarativa e rastreabilidade por módulo.

### Impacto

Baixo

### Dependências

- PKG-01 (Configuration Foundation)
- Módulos subsequentes que consomem properties (PKG-02 a PKG-07)

### Artefatos Impactados

- `configuration/properties/SecurityProperties.java`
- `configuration/properties/PersistenceProperties.java`
- `configuration/properties/IntegrationProperties.java`
- `configuration/properties/ZimbraProperties.java`
- `configuration/*Configuration.java` (classes `@Configuration` por módulo)

### Data

— (exemplo)

### Responsável

`construction-engineer`

### Reviewer

`reviewer`

### Auditor

`auditor`

### Status

APPROVED *(formato de referência)*

---

## DL-S1A-002 — RestClient para Integração Síncrona

> **Tipo:** Exemplo de referência — não registrado

### Package

PKG-04 — Integration Foundation

### Módulo

Integration

### Componente

RestClient

### Decisão

Utilizar **Spring RestClient** (Spring Framework 6.1+) como cliente HTTP padrão para integrações síncronas da Platform Foundation, incluindo comunicação com Zimbra.

### Alternativas Avaliadas

| Alternativa | Descrição | Motivo de rejeição |
|-------------|-----------|-------------------|
| WebClient | Cliente reativo do Spring WebFlux | Integração com Zimbra é síncrona; adiciona complexidade reativa desnecessária |
| RestTemplate | Cliente legado do Spring | Deprecated; sem suporte a longo prazo no Spring Boot 4 |
| Feign Client | Cliente declarativo | Dependência adicional; CD-S1A-004 ainda pendente para resiliência |

### Justificativa

Integração síncrona com o Zimbra. RestClient oferece API fluente moderna, integração nativa com Spring Boot 4, suporte a interceptors e alinhamento com o stack síncrono do projeto.

### Impacto

Médio

### Dependências

- PKG-01 (Configuration Foundation) — `IntegrationProperties`, `ZimbraProperties`
- PKG-03 (Security Foundation) — autenticação em chamadas HTTP
- CD-S1A-004 (Resiliência HTTP) — decisão complementar pendente em `07-open-decisions.md`

### Artefatos Impactados

- `integration/client/RestClientConfiguration.java`
- `integration/client/ZimbraRestClient.java`
- `construction/platform-foundation/integration/`

### Data

— (exemplo)

### Responsável

`construction-engineer`

### Reviewer

`reviewer`

### Auditor

`auditor`

### Status

APPROVED *(formato de referência)*

---

# Relação com outros artefatos

```text
07-open-decisions.md          Decisões PENDENTES (CD-S1A-*)
        ↓
   [Aprovação + Implementação + Review + Audit]
        ↓
10-decision-log.md            Decisões IMPLEMENTADAS (DL-S1A-*)
        ↓
03-construction-packages.md   SSOT de escopo, critérios e agentes
        ↓
09-progress.md                SSOT de estado de execução (Workflow)
        ↓
Construction Audit (PKG-08)   Validação final de conformidade
```

| Artefato | Função | Conteúdo |
|----------|--------|----------|
| `07-open-decisions.md` | Decisões **pendentes** | Hipóteses em análise; bloqueios de implementação; prefixo `CD-S1A-*` |
| `10-decision-log.md` | Decisões **implementadas** | Ledger imutável de escolhas aprovadas; prefixo `DL-S1A-*` |
| `03-construction-packages.md` | SSOT de execução | Escopo, dependências, tarefas e critérios por Package |
| `09-progress.md` | SSOT de estado | Workflow States, progresso e bloqueios da Sprint |
| `08-open-risks.md` | Riscos ativos | Riscos não mitigados que podem gerar decisões |
| `05-readiness-review.md` | Prontidão final | Checklist RC-* validado no PKG-08 |
| `construction/review/construction-audit.md` | Auditoria final | Valida completude do Decision Log |

### Fluxo de migração CD → DL

```text
1. Decisão identificada → registrada em 07-open-decisions.md (CD-S1A-XXX)
2. Construction Orchestrator aprova durante execução do Package
3. construction-engineer implementa
4. reviewer aprova revisão técnica
5. auditor confirma conformidade
6. Construction Orchestrator registra em 10-decision-log.md (DL-S1A-XXX)
7. Entrada removida de 07-open-decisions.md
8. Referência cruzada mantida no histórico do Package
```

---

# Regras

## Registrar somente

- Decisões **implementadas** em código ou configuração
- Decisões **aprovadas** pelo Construction Orchestrator
- Decisões **revisadas** pelo `reviewer`
- Decisões **auditadas** pelo `auditor`

## Não registrar

| Tipo | Destino correto |
|------|-----------------|
| Hipóteses | Discussão / `07-open-decisions.md` |
| Discussões em andamento | `07-open-decisions.md` |
| Ideias não avaliadas | Backlog técnico / `07-open-decisions.md` |
| Decisões pendentes | `07-open-decisions.md` |
| Decisões rejeitadas | Não registradas — documentar motivo em review/audit |

## Imutabilidade

- IDs publicados são permanentes
- Correções de conteúdo devem preservar o ID original
- Alterações substantivas que invalidem a decisão exigem nova entrada com novo ID e referência à decisão substituída

---

# Compatibilidade

Este documento é consumido e atualizado no contexto de:

| Artefato / Agente | Interação |
|-------------------|-----------|
| Construction Orchestrator | Autoriza registro após Package `APPROVED` |
| `reviewer` | Valida aderência técnica da decisão implementada |
| `auditor` | Valida conformidade e completude do registro |
| Construction Audit (PKG-08) | Verifica que decisões implementadas possuem entrada DL-S1A-* |
| Decision Review | Decisões em `07` são avaliadas antes da migração |
| Readiness Review | Completude do Decision Log é critério de prontidão |

---

# Instruções de Atualização

Ao registrar nova decisão:

1. Verificar próximo ID disponível na sequência `DL-S1A-*`
2. Preencher Template Oficial completo
3. Adicionar entrada em **Decisões Registradas**
4. Atualizar **Índice — Decisões Registradas**
5. Recalcular **Estatísticas** (total, por módulo, por package, por impacto)
6. Se originada de `07-open-decisions.md`, remover entrada `CD-S1A-*` correspondente
7. Registrar data e responsáveis

---

# Referências

- `07-open-decisions.md` — Decisões pendentes (CD-S1A-*)
- `08-open-risks.md` — Riscos que podem originar decisões
- `03-construction-packages.md` — SSOT de Packages
- `09-progress.md` — Estado de execução da Sprint
- `04-construction-rules.md` — Regras R-01 a R-10
- `05-readiness-review.md` — Critérios de prontidão
- `.cursor/orchestrator/construction-orchestrator.mdc` — Workflow Controller
