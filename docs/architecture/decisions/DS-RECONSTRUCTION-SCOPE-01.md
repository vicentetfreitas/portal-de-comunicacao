# DS-RECONSTRUCTION-SCOPE-01 — Escopo da reconstrução do Design System

| Campo | Valor |
|-------|--------|
| **ID** | DS-RECONSTRUCTION-SCOPE-01 |
| **Status** | `RECONSTRUCTION_SCOPE_DEFINED_WITH_PENDING_DECISIONS` |
| **Data** | 2026-08-19 |
| **Categoria documental** | SSOT (escopo de reconstrução; não é ADR aprovado nem Feature) |
| **Camada dona** | Frontend Foundation (não é Feature de negócio) |
| **Mantenedor** | Architecture / Frontend Foundation |
| **Precedentes** | `AUDITORIA-DS-EXISTENCIA-01`, `AUDITORIA-DS-ARQUITETURA-01`, `DECISÃO-DS-ARQUITETURA-01` |
| **Catálogo** | [`docs/architecture/08-decision-records.md`](../08-decision-records.md) |

Este documento é o contrato de escopo da reconstrução. Não autoriza implementação, criação de `FT-DS`, alteração de tokens/componentes/Quasar/Figma, configuração de plugins Claude nem migração de Features.

---

## Status

`RECONSTRUCTION_SCOPE_DEFINED_WITH_PENDING_DECISIONS`

O alvo está definido nos três níveis (implementação, sistema, workflow). Não é refatoração dos `Ds*` atuais. Ainda faltam catálogo-alvo item a item, SSOT definitivo de design/tokens e estratégia de compatibilidade. Isso impede começar código; não impede a próxima etapa de definição (inventário).

---

## 1. Objetivo da reconstrução

**Reconstruir o Design System como modelo de produto da Frontend Foundation, sobre Quasar, orientado por Figma e preparado para integração com Claude + plugins.**

Isso significa:

- Definir e implementar um **sistema** (tokens, componentes, padrões, acessibilidade, guidelines), não apenas reescrever wrappers.
- Manter **Quasar como framework de UI**; o DS continua camada de produto sobre primitivos Quasar (DEC-004; DS-04).
- Usar **Figma como camada de design** que orienta o modelo, não como handoff ocasional.
- Deixar o código **estruturado para** inspeção, comparação e assistência Claude, **sem** depender de plugins para existir.
- Preservar o contrato: DS não é Feature; Features consomem; AppShell não é DS.

Não significa: modernizar `Ds*` in-place, substituir Quasar, criar `FT-DS`, sincronizar Figma no dia 1, nem gerar código Claude como fonte de verdade.

---

## 2. O que será reconstruído

Os três níveis fazem parte do objetivo. Não são a mesma fase de execução.

### Nível B — Sistema (núcleo da reconstrução)

Regras de composição, hierarquia, taxonomia de tokens, padrões, acessibilidade e convenções. Sem este nível, recodificar `Ds*` é só refatoração.

### Nível A — Implementação

Tokens, componentes Vue/TS, estilos e tema Quasar, refeitos **a partir do modelo**, usando o baseline como referência, não como destino obrigatório.

### Nível C — Workflow

Figma → decisões de design → (futuro) Claude + plugins → código sobre Quasar/Vue → Features. O workflow entra no **alvo**. Plugins, pipeline e geração automática **não** entram na primeira execução. O código deve nascer preparado (nomes estáveis, tokens explícitos, contratos testáveis).

O fluxo `Figma → Claude → Código → Quasar → DS → Features` é **fluxo de design**, não grafo runtime. Runtime: Features → DS → Quasar; tokens → tema Quasar; a Foundation contém o DS.

---

## 3. Baseline atual

Classificação conceitual. Não autoriza apagar ou reescrever agora.

| Elemento | Classificação | Justificativa |
| --- | --- | --- |
| Tokens | `RECONSIDER` | Preservar a função de contrato visual em código; não preservar a estrutura SCSS atual como destino |
| Papel dos tokens | `PRESERVE` | Linguagem visual fundamental do DS (DS-06) |
| Átomos `Ds*` wrappers | `REBUILD` | `DsButton`, `DsInput`, `DsSelect`, `DsIcon`, `DsAvatar`, `DsBadge`, `DsCard`, `DsDialog`, `DsDataTable`, `ds-notify` encapsulam Quasar; API/contratos devem ser redefinidos a partir do Figma |
| Moléculas de produto | `RECONSIDER` | `DsActionCard`, `DsServiceCard`, `DsContentCard`, `DsNavItem`, `DsProfileSummary`, `DsPageHeader` têm visual próprio; fronteira DS × AppShell × Feature ainda não inventariada |
| Quasar | `PRESERVE` | Framework de UI permanente; encapsulamento `RECONSIDER` |
| Showcase | `RECONSIDER` | Catálogo é necessário; ferramenta (página vs Storybook) pendente |
| Contrato E2E / a11y | `PRESERVE` | Princípio público (roles/labels, não classes `ds-*`); conteúdo específico pode evoluir com a API |
| `ds.scss` / CSS vars | `REBUILD` | Expressão do novo modelo de tokens, não SSOT conceitual |
| Docs construction | `RECONSIDER` | Intenção histórica; não SSOT do novo modelo |
| `docs/frontend/frontend-structure.md` | `RETIRE` como descrição atual | Árvore desatualizada (sem `ds/` nem tokens) |
| Figma | papel `PRESERVE`; uso `REBUILD` | Handoff-only deve virar processo; SSOT = `DECISÃO PENDENTE` |
| Construction PKGs | `RETIRE` como SSOT | Evidência histórica apenas |
| Discovery CMS (~54) | `RETIRE` | Não portar o legado de produção |
| AppShell | `PRESERVE` | Foundation estrutural; sobreposição com DS `RECONSIDER` |
| Consumo Feature | princípio `PRESERVE`; API `REBUILD` | Um barrel da Foundation; migração ainda não escolhida |

---

## 4. Arquitetura alvo

O modelo Design Layer → DS Model → Implementation → Features é **parcialmente correto**. Falhas: trata Quasar como saída do DS; omite que o DS vive na Foundation; omite AppShell; mistura workflow Claude com dependência técnica.

```text
Design Layer
  └── Figma                    (fonte de design; SSOT visual = pendente)
        └── Claude + Plugins   (futuro, assistivo; não runtime)

Design System Model            (Foundation; não é Feature)
  ├── Tokens (primitivos + semânticos; de componente = pendente)
  ├── Components
  ├── Patterns
  ├── Accessibility contracts
  └── Guidelines

Implementation Layer           (Code SSOT)
  ├── Vue 3 + TypeScript
  ├── Quasar                   (framework; tema alimentado por tokens)
  ├── CSS / tokens em código
  └── Componentes de produto   (sucessores dos Ds*)

Frontend Foundation também inclui AppShell, Router, Pinia, i18n, HTTP, testes.

Application
  └── Features consomem DS + AppShell + infra
```

Fronteiras: Figma não entra no bundle; Claude não é SSOT; Quasar não é gerado pelo DS; Features não donas do DS; AppShell não é biblioteca visual.

---

## 5. Quasar

Quasar permanece o **framework de UI**. O DS não o substitui.

Primitivas já usadas: `q-btn`, `q-input`, `q-select`, `q-icon`, `q-avatar`, `q-badge`, `q-card`, `q-dialog`, `q-table`, `q-breadcrumbs`, Notify; no shell, `q-layout`, `q-header`, `q-drawer`, `q-footer`, `q-toolbar`; nas Features, `q-form`, `q-td`, `q-banner`, `q-checkbox`.

Observação (não norma):

- Wrappers: átomos e organismos listados no baseline.
- Comportamento visual próprio: `DsActionCard` (`button` nativo), `DsServiceCard`/`DsContentCard` (`article`), `DsNavItem`.
- Candidatos a Quasar puro: chrome de layout e slots `q-td`.

Catálogo normativo “obrigatório encapsular” vs “Feature pode usar Quasar direto”: **`DECISÃO PENDENTE`** (depois do inventário).

---

## 6. Tokens

Contrato visual do sistema, independente da Feature, consumido por componentes, AppShell e tema Quasar (`frontend/src/css/quasar.variables.scss` já faz `@use "tokens/palette"`).

O baseline em `frontend/src/css/tokens/` já mistura:

- **Primitivos:** `_palette.scss`, `_spacing.scss`, `_typography.scss`, `_radius.scss`, `_shadows.scss`, `_breakpoints.scss`, `_borders.scss`.
- **Semânticos:** surfaces, texto, success/error; `_text-styles.scss`.
- **Layout/shell:** `_layout.scss` (header 64px, footer 48px, sidebar 280px) — fronteira DS × AppShell.
- **De componente:** não há camada explícita.

Taxonomia independente do SCSS atual: **sim no nível conceitual do sistema** (primitivo / semântico; componente só se o Figma exigir). Não há evidência para abandonar SCSS/CSS variables como formato de implementação agora. Formato e tokens de estado adicionais: **`DECISÃO PENDENTE`**.

Este documento não cria tokens.

---

## 7. Figma

### Atual

Referência/handoff (PKG-FE-S0-10). Sem pipeline no repositório.

### Futuro

Figma como **Design Layer integrada ao processo** (orientação contínua, não extração pontual).

| Opção | Vantagem | Risco | Implicação |
| --- | --- | --- | --- |
| 1. Fonte de design | Alinha ao alvo; risco baixo | Não resolve drift | Código continua SSOT de implementação |
| 2. SSOT de tokens | Uma origem visual | Pipeline, dual-write, CI, indisponibilidade | Contradiz o SSOT de código até haver sync |
| 3. Sync Figma ↔ código | Menor drift | Plugins, governança, geração | Depende de Claude |

**Postura de escopo:** a reconstrução **assume a opção 1 no dia 1**. As opções 2 e 3 são evoluções possíveis; o sistema deve ser compatível com elas (tokens nomeados, componentes estáveis) sem exigí-las para começar o modelo.

---

## 8. Claude + Plugins

**Capacidades desejadas:** inspeção de designs; extração *candidata* de tokens; análise de componentes; comparação Figma ↔ código; geração assistida; detecção de divergências; apoio a documentação.

**Implementação técnica:** não escolher plugin, API, formato de sync, geração como fonte de verdade, nem governança de PRs gerados.

O modelo deve nascer legível por ferramentas. Claude **não** é pré-requisito para os Níveis A/B.

---

## 9. SSOT

| Domínio | Proposta | Status |
| --- | --- | --- |
| Arquitetura | ADR em `docs/architecture/` (catálogo `08-decision-records.md`) | Localização proposta; ADR de identidade DS-01–DS-10 ainda não criado |
| Design | Figma como fonte (opção 1) | Elevação a SSOT (2/3) = `DECISÃO PENDENTE` |
| Tokens | Hoje: `frontend/src/css/tokens/`; futuro: taxonomia no DS Model, implementação no código até decisão Figma | SSOT definitivo Figma vs código = `DECISÃO PENDENTE` |
| Código | `frontend/src/css/tokens/` + `frontend/src/components/ds/` (ou sucessores na Foundation) | Confirmado como SSOT de implementação |
| Consumo Feature | Barrel `@/components/ds` (ou sucessor único) | Features não duplicam DS |

---

## 10. DS × Feature

Princípio de trabalho (não política definitiva):

- Candidato a DS: reutilizável, sem regra de negócio, estável, visual/acessível genérico.
- Permanece na Feature: acoplado a domínio/API/estado (`frontend/src/components/organization/equipe/*`, `singular/*`).

Features **podem** ter componentes de domínio. Promoção Feature → DS: **`DECISÃO PENDENTE`**. A reconstrução não promove componentes de Feature por padrão. Não criar `FT-DS`.

---

## 11. DS × AppShell

**AppShell:** `AppShell`, `AppHeader`, `AppSidebar`, `AppFooter`, layouts; `q-layout` e filhos; navegação e responsividade do chrome.

**DS:** primitivos e padrões que o shell **usa** (botão, ícone, breadcrumbs).

**Sobreposição atual:** `DsNavItem` + `SidebarMenuItem`; `DsProfileSummary`; tokens `_layout.scss`; header/footer com `Ds*`.

A reconstrução deve reclassificar esses itens no inventário. Não absorver o AppShell no DS. Não transformar o DS em layout engine.

---

## 12. Reconstrução × Migração × Legado

Etapas **separadas**. Nenhuma é autorizada por este documento.

1. **Reconstrução** — modelo + implementação nova na Foundation (B, depois A; C preparado).
2. **Migração** — Features passam a consumir o novo contrato.
3. **Legado** — `Ds*` atuais podem permanecer na transição; construction/discovery não são o alvo.

Misturar as três num único PR é risco (E2E de Equipe/Singular já usa o contrato público `Ds*` em `construction/17-frontend-e2e-behavior-policy.md`).

---

## 13. Compatibilidade

Alternativas identificadas, **não escolhidas**:

- Substituição direta dos `Ds*` (mesmo nome/API)
- Coexistência temporária
- Versionamento explícito
- Camada de adapters
- Migração Feature a Feature

O barrel `@/components/ds` está em páginas, AppShell, organização e testes; E2E usa `DsInput`/`DsBadge`/`DsPageHeader`. Big-bang sem inventário é de alto risco. Escolha: **`DECISÃO PENDENTE`** após o inventário.

---

## 14. Contratos do novo DS

- Acessibilidade de superfície (roles, labels, heading)
- Comportamento e estados (variantes, disabled, loading, erro)
- Tokens (sem hardcode; primitivo → semântico)
- API pública de componentes (props, slots, eventos)
- Encapsulamento Quasar (interno vs importável)
- Consumo único pela Feature (um barrel)
- Regressão visual (ferramenta pendente)
- Responsividade de padrões DS (não o IA do AppShell)

Este documento não implementa testes. Showcase/Storybook: **`DECISÃO PENDENTE`**.

---

## 15. Decisões pendentes

1. Papel definitivo do Figma (fonte vs SSOT de tokens vs sync)
2. Taxonomia final de tokens e formato de implementação
3. Catálogo-alvo de componentes (DS vs AppShell vs Feature)
4. Política normativa Quasar vs componente de produto
5. Estratégia de compatibilidade / migração
6. Critérios operacionais de promoção Feature → DS
7. Ferramenta de catálogo e regressão visual
8. Persistência do ADR de identidade arquitetural (DS-01–DS-10)

Fora do caminho crítico: publicação como pacote; plugin Claude; API de sync; versionamento semântico como pacote.

---

## 16. Riscos

- Tratar reconstrução como rewrite in-place dos `Ds*` e perder o Nível B
- Quebrar Features/E2E ao mudar API sem contrato de migração
- Elevar Figma ou Claude a SSOT sem pipeline
- Geração automática como fonte de verdade
- Absorver AppShell ou domínio no DS
- Criar `FT-DS` e violar o contrato SDD
- Portar os ~54 componentes do discovery CMS
- Dual-use indefinido sem dono da migração

---

## 17. Próximo passo

**Inventário de reconstrução (somente leitura):** classificar cada `Ds*`, cada categoria de token e cada sobreposição AppShell/Feature contra os layouts Figma já usados como handoff, produzindo o catálogo-alvo preserve/rebuild/retire **por item**. Ainda sem código, sem ADR de identidade, sem plugins, sem migrar Features.

Se amanhã começarmos a reconstruir: sabemos o alvo e os três níveis; não sabemos ainda o catálogo-alvo nem as decisões 1–5 da seção 15.

---

## 18. Decisão registrada — Fronteira pública do Design System (2026-08-19)

**Contexto:** primeira decisão arquitetural tomada após o inventário de reconstrução (`DS-RECONSTRUCTION-INVENTORY-01`, informado por `AUDITORIA-DS-FIGMA-01`). Objetivo: destravar o início da reconstrução sem misturar reconstrução, migração de consumidores e higienização geral do projeto na mesma etapa.

**Decisão:** `frontend/src/components/ds/index.ts` (barrel) é adotado como a fronteira pública oficial do Design System. DS ativo = exatamente o conjunto exportado nesse arquivo, independentemente de onde o arquivo-fonte esteja fisicamente guardado dentro de `components/ds/`. Nenhuma camada física de diretório (`legacy/`, `deprecated/` ou equivalente) é criada agora. Higienização de itens sem consumidor usa rastreio documental (tabela com condição de remoção obrigatória por item), não uma pasta — remoção ocorre em PR único (arquivo + export), sem estado intermediário de "movido para pasta".

**Categorias resultantes:**
- **Dentro da fronteira do DS:** os 20 itens hoje exportados pelo barrel (inclui `PRESERVE` de alta evidência e `RECONSIDER` com consumidor real — `DsSelect`, `DsBreadcrumbs`, `DsDialog`, `DsFormCard`; revisão de API não remove da fronteira).
- **Fora da fronteira, mas válidas:** código que consome o DS sem ser o DS — `AppHeader`/`AppSidebar`/`AppFooter`, `sidebar/*`, componentes de Feature (`EquipeInfoCard`, `SingularInfoCard`, `ColaboradorInfoCard`).
- **Candidatas futuras à remoção/depreciação:** itens sem consumidor de produção identificados no inventário (`DsContentCard`, `DsContentCardCompact`, `DsServiceCard`, `DsSectionHeader` — hoje só usados em `pages/showcase.vue`).

**Critérios objetivos associados:**

| Estado | Critério |
| --- | --- |
| Preservado | ≥1 consumidor real fora de `ds/`, sem mudança de API planejada |
| Migrado | Nova implementação já adotada por ≥1 consumidor real, enquanto a antiga ainda tem consumidores pendentes — só existe sob decisão de compatibilidade (item 5 da seção 15) explicitamente ativa |
| Deprecated | Export mantido só por retrocompatibilidade; 0 novos usos permitidos; consumidores restantes com plano de migração associado a uma condição de saída |
| Removível | 0 consumidores fora de `ds/`, 0 uso em testes, não é dependência interna de outro export do barrel |

**Quando criar `legacy/`/`deprecated/` fisicamente (condição futura, não decidida agora):** quando a estratégia de compatibilidade (item 5 da seção 15) exigir coexistência real de duas implementações do mesmo componente por um período, ou quando o volume de candidatos a remoção deixar de caber em rastreio documental simples.

**Não resolve:** nenhuma das 8 pendências da seção 15 é fechada por este registro. Permanecem integralmente abertas.

**Risco a vigiar (não mitigado por este registro):** reconstruir um item `RECONSIDER` de alto uso antes de decidir o item 5 da seção 15 mistura migração e reconstrução no mesmo esforço, porque mudar API pública de item consumido força atualização de Feature junto. Mitigação natural: iniciar pelos itens `PRESERVE` sem mudança de API e pelos itens sem consumidor, deixando os `RECONSIDER` de alto uso para depois da decisão 5.

---

## 19. Decisão registrada — Taxonomia de tokens e formato de implementação (2026-08-19)

**Contexto:** segunda decisão arquitetural após a fronteira pública do DS (§18), resolvendo o item 2 da seção 15 ("Taxonomia final de tokens e formato de implementação"), identificado como pré-requisito explícito do Nível B (§2: "Sem este nível, recodificar `Ds*` é só refatoração").

**Decisão:** a taxonomia de tokens do Nível B é ratificada em **3 níveis — primitivo → semântico → de componente** — conforme já implementado e consumido em `frontend/src/css/tokens/` e `design-tokens.scss`. A camada "de componente" (`--text-page-title-*`, `--text-section-title-*`, `--text-card-title-*`, consumida por `DsPageHeader`/`DsSectionHeader`/`DsCard`) é reconhecida como parte oficial do modelo, **corrigindo a premissa do §6** ("de componente: não há camada explícita"). O formato de implementação (SCSS como fonte de valores + CSS custom properties como contrato de runtime, alimentando também o tema Quasar via `quasar.variables.scss`) permanece válido para o Nível A, sem alteração.

**Escopo explícito — o que este registro NÃO resolve:**
- Breakpoints ficam **fora** da taxonomia madura: tokens declarados em `_breakpoints.scss`, mas sem uso disciplinado (`AppSidebar.vue` usa `960` hardcoded em vez do token) e sem evidência de necessidade de design responsivo no Figma auditado (frames somente desktop, 1920×1080).
- Nenhum valor de token existente é alterado por este registro — inclusive a discrepância já registrada entre `_layout.scss` (header 64px/footer 48px/sidebar 280px) e a medição real do Figma (`AUDITORIA-DS-FIGMA-01`) permanece sem correção; é dívida a tratar separadamente, não parte da taxonomia.
- Não decide o papel do Figma como fonte/SSOT de tokens (item 1 da seção 15).
- Não decide catálogo de componentes, estratégia de compatibilidade/migração, critérios de promoção Feature→DS, ferramenta de catálogo/regressão visual, nem o ADR de identidade DS-01–DS-10 (itens 3, 5, 6, 7, 8 da seção 15) — permanecem integralmente abertos.
- Não introduz novo pipeline ou ferramenta de tokens (ex. Style Dictionary, sync com Figma Variables).

**Alternativas consideradas e não escolhidas:** manter a camada "de componente" como não oficial/experimental; adiar qualquer ratificação até a pendência do papel do Figma (item 1) ser resolvida.

**Fontes:** `DS-RECONSTRUCTION-INVENTORY-01` §4; `AUDITORIA-DS-FIGMA-01`; `frontend/src/css/tokens/*.scss`; `frontend/src/css/tokens/design-tokens.scss`.

---

## 20. Decisão registrada — Política normativa Quasar vs. componente de produto (2026-08-19)

**Contexto:** terceira decisão arquitetural após a fronteira pública do DS (§18) e a taxonomia de tokens (§19), resolvendo o item 4 da seção 15 ("Política normativa Quasar vs. componente de produto"), necessário para que todo componente do novo DS possa declarar o contrato "Encapsulamento Quasar (interno vs. importável)" exigido pelo §14.

**Decisão:** ratificado o padrão já observado no código como norma do Nível B:
- **Encapsulamento obrigatório via `Ds*`:** `q-btn`, `q-input`, `q-select`, `q-icon`, `q-avatar`, `q-badge`, `q-card`, `q-dialog`, `q-table`, `q-breadcrumbs` — evidência: 0 uso direto fora de `ds/` no código atual.
- **Quasar direto permitido, sem exigir wrapper `Ds*`:**
  - Chrome do AppShell: `q-layout`, `q-header`, `q-drawer`, `q-footer`, `q-toolbar`.
  - Partes de formulário de Feature ainda não encapsuladas: `q-form`, `q-td`, `q-banner`, `q-checkbox`, `q-page`.

**Escopo explícito — o que este registro NÃO resolve:**
- Não cria novos wrappers `Ds*` agora (ex. `DsCheckbox`, `DsBanner`, wrapper de `q-form`) — se e quando isso ocorrer é decisão de escopo futuro, não parte deste registro.
- Não decide migração de Features que hoje usam Quasar direto — isso é migração, fora do escopo desta política.
- Não decide catálogo de componentes, papel do Figma, estratégia de compatibilidade, critérios de promoção Feature→DS, ferramenta de catálogo/regressão visual, nem o ADR de identidade DS-01–DS-10 (itens 1, 3, 5, 6, 7, 8 da seção 15) — permanecem integralmente abertos.

**Alternativas consideradas e não escolhidas:** ampliar já agora o escopo de reconstrução para encapsular os primitivos hoje usados direto em Feature; não normatizar e manter decisão caso a caso.

**Fontes:** `DS-RECONSTRUCTION-INVENTORY-01` §6; código atual de `frontend/src/components/ds/`, `frontend/src/components/app/` e `frontend/src/components/organization/`.

---

## 21. Decisão registrada — Gatilho de aplicabilidade da estratégia de compatibilidade (2026-08-19)

**Contexto:** quarta decisão arquitetural após fronteira pública (§18), taxonomia de tokens (§19) e política Quasar (§20), tratando o item 5 da seção 15 ("Estratégia de compatibilidade / migração"). O baseline (§3) classifica os átomos `Ds*` como `REBUILD` com a ressalva "API/contratos devem ser redefinidos a partir do Figma" — presumindo possível mudança de contrato público, o que expõe risco aos consumidores reais já mapeados no `DS-RECONSTRUCTION-INVENTORY-01` (ex. `DsButton` 23, `DsPageHeader` 21, `DsCard` 14) e ao contrato E2E documentado em `construction/17-frontend-e2e-behavior-policy.md`.

**Decisão:** fixado o **gatilho de aplicabilidade**, sem escolher entre as 5 alternativas do §13:

- Reconstrução que **não altera a API pública** de um item, ou que trata um item **sem consumidor real de produção** (ex. os 4 órfãos do inventário), segue livre — **não depende** da estratégia de compatibilidade.
- Reconstrução que **muda a API pública** de um item com consumidor real fica **explicitamente pausada** até a escolha entre as alternativas do §13 (substituição direta, coexistência temporária, versionamento explícito, camada de adapters, migração Feature a Feature) — escolha que pode ser feita **por item**, no momento em que a reconstrução daquele item específico de fato começar, não em bloco agora.

Esta decisão refina, sem contradizer, a consequência já registrada em §18 ("reconstrução pode começar imediatamente pelos itens `PRESERVE` e pelos itens sem consumidor de produção, sem aguardar... #5–#8"): o alcance dessa liberdade fica agora explicitamente limitado a mudanças que não quebram API pública.

**Escopo explícito — o que este registro NÃO resolve:**
- Não escolhe qual das 5 alternativas do §13 será usada — isso permanece em aberto, decidível item a item.
- Não define prazo ou cronograma de migração.
- Não decide se algum item de alto uso terá a API quebrada já no primeiro ciclo de reconstrução — decisão de priorização de produto/engenharia, não arquitetural.
- Não decide a forma física de eventual coexistência de versões — permanece ligada à pendência de `legacy/`/`deprecated/`, já deliberadamente adiada em §18.
- Não decide catálogo de componentes, papel do Figma, critérios de promoção Feature→DS, ferramenta de catálogo/regressão visual, nem o ADR de identidade DS-01–DS-10 (itens 1, 3, 6, 7, 8 da seção 15) — permanecem integralmente abertos.

**Alternativas consideradas e não escolhidas:** fixar desde já uma das 5 alternativas do §13 como padrão único do DS; adiar qualquer decisão sobre o item 5 até um item de alto uso precisar de fato ser reconstruído.

**Fontes:** `DS-RECONSTRUCTION-INVENTORY-01` §2–§3, §9; `construction/17-frontend-e2e-behavior-policy.md`.

---

## 22. Candidato registrado — Primeiro item de reconstrução (2026-08-19)

**Contexto:** com §18–§21 registrados, o conjunto liberado para iniciar o Nível A é: itens `PRESERVE` do `DS-RECONSTRUCTION-INVENTORY-01` sem necessidade de mudança de API pública, ou itens sem consumidor real de produção. Esta seção identifica o primeiro item dentro desse conjunto, sem autorizar implementação.

**Candidato identificado:** `DsIcon` (`frontend/src/components/ds/atoms/DsIcon.vue`).

**Justificativa:**
- **Menor superfície de API** — 3 props (`name`, `size`, `color`), sem slots, sem eventos, sem estado; baixíssima chance de acionar o gatilho de compatibilidade do §21.
- **Maior efeito de composição com menor risco** — segundo o `DS-RECONSTRUCTION-INVENTORY-01`, é consumido diretamente em 4 arquivos e internamente por 6 outros `Ds*` (`DsNavItem`, `DsProfileSummary`, `DsActionCard`, `DsContentCardCompact`, `DsServiceCard`, `DsSearchInput`) — é o ponto mais próximo da raiz da árvore de dependência do DS.
- **Uso real, não órfão** — ao contrário dos 4 itens sem consumidor de produção, valida o processo de reconstrução num item efetivamente consumido, com risco mínimo.

**Por que não os alternativos óbvios:** `DsButton` (23 consumidores) tem maior valor de validação mas também maior superfície e raio de exposição — candidato natural para depois de `DsIcon` validar o processo. Os 4 órfãos têm risco técnico zero, mas seu destino (retirar vs. manter) ainda depende de decisão de produto (achado do `AUDITORIA-DS-FIGMA-01`/`INVENTORY-01` §7) — reconstruí-los antes dessa definição arrisca retrabalho.

**Escopo explícito — o que este registro NÃO decide:**
- Não autoriza implementação — nenhum código foi ou deve ser alterado por este registro.
- Não define a ordem completa dos itens seguintes.
- Não confirma que `DsIcon` de fato dispensará mudança de API — isso só se confirma na tentativa; se precisar, o gatilho do §21 entra em vigor para este item especificamente.
- Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `DS-RECONSTRUCTION-INVENTORY-01` §1–§2; `AUDITORIA-DS-FIGMA-01`.

---

## 23. Validação registrada — `DsIcon`: sem alteração necessária (2026-08-19)

**Contexto:** validação pós-implementação do candidato registrado em §22. Uma edição inicial (adição de `aria-hidden="true"` explícito no template) foi implementada, revisada e **revertida** nesta mesma sessão.

**Resultado da auditoria:** `DsIcon.vue` já está em conformidade com §18 (exportado no barrel), §19 (usa exclusivamente tokens primitivos `--font-size-*`, sem hardcode) e §20 (único ponto de encapsulamento de `q-icon`, sem uso direto de Quasar fora de `ds/`). Nenhuma mudança de código foi necessária.

**Por que a edição inicial foi revertida:** inspeção do código-fonte do `QIcon` do Quasar (`node_modules/quasar/src/components/icon/QIcon.js`) mostrou que o componente **já define `aria-hidden="true"` incondicionalmente em todo render**, antes de qualquer atributo repassado. A adição feita em `DsIcon.vue` era, portanto, funcionalmente redundante — não alterava o DOM renderizado. Sem efeito real a preservar, a edição foi revertida para manter o arquivo idêntico ao estado anterior a esta reconstrução.

**Classificação de consumidores (levantada durante a validação, sem ação decorrente):** todos os usos diretos e internos de `DsIcon` são decorativos (ícone acompanhado de texto/label visível ou `aria-label` no elemento interativo pai), exceto `DsNavItem` em modo `mini`, que expõe um elemento interativo apenas com ícone e sem `aria-label` — condição **pré-existente**, não criada nem alterada por esta validação, e fora do escopo deste registro.

**Contrato:** preservado integralmente — props, slots, eventos, classes, tokens e comportamento visual de `DsIcon` permanecem exatamente como antes desta reconstrução.

**Testes:** `yarn typecheck` e `yarn lint` executados sobre o arquivo revertido — sem erros atribuíveis a `DsIcon.vue`. Não existe teste unitário dedicado a `DsIcon`; nenhum foi criado.

**O que isto confirma sobre a estratégia de reconstrução:** a seleção de `DsIcon` como primeiro candidato (§22) continua válida — baixo risco, sem mudança de API, sem regressão. O ciclo revelou uma premissa incorreta pontual (necessidade da edição de acessibilidade, não a escolha do candidato) e mostrou que um primeiro item de risco mínimo pode legitimamente resultar em "nenhuma mudança necessária" — um resultado válido de reconstrução, não uma falha do processo.

**Escopo explícito — o que este registro NÃO decide:** não resolve o achado pré-existente de `DsNavItem` em modo `mini` (fora de escopo, sem ação tomada); não decide o próximo item a reconstruir; não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** inspeção de `frontend/src/components/ds/atoms/DsIcon.vue` e `node_modules/quasar/src/components/icon/QIcon.js`; consumidores em `frontend/src/components/app/`, `frontend/src/components/shared/`, `frontend/src/components/ds/molecules/`, `frontend/src/pages/showcase.vue`.

---

## 24. Candidato registrado — Segundo item de reconstrução (2026-08-19)

**Contexto:** com o ciclo de `DsIcon` concluído (§22–§23: auditoria → implementação → validação → reversão → registro, resultado "sem alteração necessária", sem regressão), este registro identifica o próximo item dentro do conjunto liberado por §18+§21, sem autorizar implementação.

**Candidato identificado:** `DsAvatar` (`frontend/src/components/ds/atoms/DsAvatar.vue`).

**Justificativa:**
- **Superfície pequena** — 6 props (`src`, `alt`, `initials`, `size`, `color`, `textColor`), um slot default de fallback, sem eventos; baixa chance de acionar o gatilho de compatibilidade do §21.
- **Uso real, direto e interno** — consumido diretamente em `AppHeader.vue` e internamente por `DsProfileSummary`; segunda peça mais próxima da raiz da árvore de composição do DS, depois de `DsIcon`.
- **Sem contrato E2E documentado** — ao contrário de `DsBadge` (`role="status"`, `construction/17`) ou `DsInput` (`role="alert"`), não há dependência de teste de comportamento formalizada a preservar.
- **Continuidade da escalada de risco** — segundo degrau da mesma lógica usada para escolher `DsIcon`: validar o modelo num segundo item pequeno e independente antes de avançar para itens de maior consumo (`DsButton`, 23 consumidores) ou com contrato de acessibilidade já formalizado (`DsBadge`, `DsInput`).

**Candidatos seguintes prováveis, não decididos agora:** `DsBadge` e `DsButton`.

**Escopo explícito — o que este registro NÃO decide:**
- Não autoriza implementação — nenhum código foi ou deve ser alterado por este registro.
- Não confirma que `DsAvatar` dispensará mudança de API — só se confirma na tentativa; se precisar, o gatilho do §21 entra em vigor para este item especificamente.
- Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `DS-RECONSTRUCTION-INVENTORY-01` §1; código atual de `frontend/src/components/ds/atoms/DsAvatar.vue`, `frontend/src/components/app/AppHeader.vue`, `frontend/src/components/ds/molecules/DsProfileSummary.vue`.

---

## 25. Implementação registrada — `DsAvatar`: tokens de espaçamento estendidos (2026-08-19)

**Contexto:** implementação do candidato registrado em §24. Diagnóstico prévio mostrou que `DsAvatar.vue` já era conforme §18 (barrel) e §20 (encapsulamento Quasar), mas violava §19/§14 ("Tokens: sem hardcode; primitivo → semântico"): o mapa `quasarSize` usava pixels literais (`"24px"`, `"32px"`, `"40px"`, `"56px"`, `"72px"`), e dois desses valores (56px, 72px) não correspondiam a nenhum primitivo existente em `_spacing.scss`.

**Decisão de correção escolhida:** opção (b) — estender a escala primitiva de espaçamento, em vez de deixar `lg`/`xl` como hardcode documentado (opção a).

**Implementação:**
- `frontend/src/css/tokens/_spacing.scss` — dois primitivos novos, seguindo a convenção `spacing-N = N×4px`: `$spacing-14: 56px;` e `$spacing-18: 72px;`.
- `frontend/src/css/tokens/design-tokens.scss` — expostos como `--spacing-14` e `--spacing-18` (nomeados pelo número primitivo, não inseridos na escala semântica `xs..5xl`, para não renumerar ou colidir com aliases semânticos já em uso).
- `frontend/src/components/ds/atoms/DsAvatar.vue` — `quasarSize` passou a referenciar tokens: `xs→var(--spacing-lg)` (24px), `sm→var(--spacing-xl)` (32px), `md→var(--spacing-2xl)` (40px), `lg→var(--spacing-14)` (56px), `xl→var(--spacing-18)` (72px).

**Contrato:** preservado — nenhum valor de pixel mudou (cada `var()` resolve exatamente ao valor hardcoded anterior), props/slots/eventos de `DsAvatar` inalterados. Não aciona o gatilho do §21.

**Testes:** `yarn typecheck` e `yarn lint` executados sobre os 3 arquivos alterados — sem erros atribuíveis a esta mudança (os 3 erros de lint reportados são pré-existentes, em arquivos não relacionados). Não existe teste unitário dedicado a `DsAvatar`; nenhum foi criado.

**O que isto confirma sobre a estratégia de reconstrução:** ao contrário do ciclo `DsIcon` (§22–§23, "sem alteração necessária"), este ciclo produziu uma correção real e localizada — confirma que o modelo de tokens (§19) tem poder de detecção prática (achou um hardcode genuíno) e que extensões pontuais da escala primitiva são viáveis sem tocar API pública nem exigir decisão de compatibilidade (§21).

**Escopo explícito — o que este registro NÃO decide:**
- Não decide se `--spacing-14`/`--spacing-18` devem futuramente ganhar nome semântico (`xs..5xl`) — mantidos como referência primitiva direta.
- Não resolve a fronteira conceitual entre "escala de espaçamento" e "escala de dimensão de componente" (avatar) — os novos primitivos vivem na escala de espaçamento porque foi essa a opção escolhida, não porque a taxonomia do §19 exigisse essa camada especificamente para dimensões de componente.
- Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/css/tokens/_spacing.scss`, `frontend/src/css/tokens/design-tokens.scss`, `frontend/src/components/ds/atoms/DsAvatar.vue`.

---

## 26. Diagnóstico registrado — `DsButton`: CONFORME (2026-08-19)

**Contexto:** diagnóstico formal do candidato registrado a partir da identificação de próximo item após os ciclos `DsIcon` (§22–23) e `DsAvatar` (§24–25).

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — nenhum valor literal de cor/pixel; `color` usa nomes semânticos do tema Quasar (`primary`, `negative`, `grey-7`); `quasarSize` é passthrough de palavras-chave nativas do Quasar; sem bloco `<style>`.
- §20 (encapsulamento Quasar): conforme — zero uso direto de `q-btn` fora de `ds/`, `DsButton` é o único ponto de acesso.
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API foi proposta.

**Contrato público:** props (`variant`, `size`, `loading`, `disable`), slot default, sem eventos próprios declarados (fallthrough via `$attrs`), classes `ds-button`/`ds-button--${variant}` — tudo preservado, nada alterado.

**Nota de risco registrada — dependência de teste sobre classe CSS:** `test/unit/components/SingularStatusDialog.spec.ts` seleciona botões por `.ds-button` (classe CSS) e texto, não por `role`/`aria-label` (linhas 79–101), contrariando o princípio documentado em `construction/17` ("roles/labels, não classes `ds-*`"). É uma dependência de contrato real e não formalizada — não constatada nos ciclos anteriores (`DsIcon`, `DsAvatar`), que não tinham teste com essa dependência. Não é pendência do §15; é observação operacional para qualquer reconstrução futura de `DsButton` que toque o nome da classe.

**Testes:** `shared-components.spec.ts` (slot) e `SingularStatusDialog.spec.ts` (classe `.ds-button`) identificados como dependentes; nenhum executado nesta rodada por não haver alteração de código.

**Comparação com ciclos anteriores:** mesmo desfecho de `DsIcon` (§23, "sem alteração necessária"), diferente de `DsAvatar` (§25, correção real de hardcode).

**Escopo explícito:** nenhuma alteração de código, estrutural ou de implementação foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

---

## 27. Candidato registrado — Quarto item de reconstrução (2026-08-19)

**Contexto:** com os ciclos `DsIcon` (§22–23, sem alteração), `DsAvatar` (§24–25, reconstrução interna) e `DsButton` (§26, conforme) concluídos, este registro identifica o próximo item dentro do conjunto liberado por §18+§21, sem autorizar implementação.

**Candidato identificado:** `DsBadge` (`frontend/src/components/ds/atoms/DsBadge.vue`).

**Justificativa:**
- **Fecha a última dependência pendente de `DsNavItem`** — que já consome `DsIcon` (§23) e `DsBadge` internamente (`<DsBadge :label="badge" variant="neutral" />`).
- **Primeiro item do conjunto com contrato E2E documentado** — `construction/17` define `role="status"` para `DsBadge` (status Ativa/Inativa em Equipe e Singular); é o menor dos dois itens com contrato formal (`DsBadge` vs. `DsInput`).
- **Superfície pequena** — 3 props (`label`, `variant`, `outline`), sem `v-model`, sem slots nomeados.

**Leitura preliminar (não substitui diagnóstico formal):** estrutura semelhante a `DsButton` — sem bloco `<style>`, cor via nomes semânticos do tema Quasar, sem hardcode aparente à primeira vista.

**Escopo explícito:** este registro não altera implementação, testes ou estrutura; não autoriza implementação; não confirma que `DsBadge` dispensará mudança de API; não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `DS-RECONSTRUCTION-INVENTORY-01`; `construction/17-frontend-e2e-behavior-policy.md`; código atual de `frontend/src/components/ds/atoms/DsBadge.vue` e `frontend/src/components/ds/molecules/DsNavItem.vue`.

---

## 28. Diagnóstico registrado — `DsBadge`: CONFORME (2026-08-19)

**Contexto:** diagnóstico formal do candidato registrado em §27.

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — nenhum valor literal; `color` usa nomes semânticos do tema Quasar (`positive`, `negative`, `warning`, `info`, `grey-6`); sem bloco `<style>`.
- §20 (encapsulamento Quasar): conforme — zero uso direto de `q-badge` fora de `ds/`.
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API proposta.

**Achado central:** o contrato E2E documentado em `construction/17` (`role="status"` + `aria-label` = rótulo i18n, ex. `getByRole('status', { name: 'Ativa' })`) já é entregue pelo próprio `QBadge` do Quasar incondicionalmente (`node_modules/quasar/src/components/badge/QBadge.js`, linhas 54–64: `role: 'status'` e `'aria-label': props.label` sempre definidos), sem exigir nenhum código adicional em `DsBadge`. Mesmo padrão do ciclo `DsIcon` (§23).

**Achados de observação (não corrigidos, não são defeito de `DsBadge`):**
- Uso inconsistente entre consumidores: `EquipeInfoCard.vue` e `ColaboradorInfoCard.vue` passam `role="status"`/`:aria-label` explicitamente (redundante); `SingularInfoCard.vue` não passa, confiando no comportamento padrão — ambos funcionam igualmente.
- O contrato está documentado em `construction/17` e `test/e2e/README.md`, mas não há teste automatizado (unit ou e2e) que hoje o verifique — lacuna de cobertura, não falha de implementação.

**Contrato público:** props (`label`, `variant`, `outline`), slot default, sem eventos, classes `ds-badge`/`ds-badge--${variant}` — tudo preservado, nada alterado.

**Comparação com ciclos anteriores:** mesmo desfecho de `DsIcon` (§23) e `DsButton` (§26); diferente de `DsAvatar` (§25, correção real de hardcode).

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/atoms/DsBadge.vue`; `node_modules/quasar/src/components/badge/QBadge.js`; `construction/17-frontend-e2e-behavior-policy.md`; `frontend/test/e2e/README.md`; consumidores em `frontend/src/components/organization/` e `frontend/src/components/app/AppHeader.vue`.

---

## 29. Diagnóstico registrado — `DsInput`: CONFORME (2026-08-19)

**Contexto:** diagnóstico formal do candidato identificado após o ciclo `DsBadge` (§27–28), quinto e último átomo `PRESERVE` do `DS-RECONSTRUCTION-INVENTORY-01` §1 a passar pelo ciclo.

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — sem bloco `<style>`; `variant` mapeia para as props booleanas nativas do Quasar (`outlined`/`filled`/`standout`), sem valor literal de cor ou pixel.
- §20 (encapsulamento Quasar): conforme — zero uso direto de `q-input` fora de `ds/`.
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API proposta.

**Achado central — inverso dos ciclos `DsIcon`/`DsBadge`:** nesses dois ciclos o código custom de acessibilidade era redundante (o Quasar já entregava o mesmo resultado nativamente). Em `DsInput` é o oposto — necessário. Inspeção de `node_modules/quasar/src/composables/private.use-field/use-field.js:567-577` mostra que o `role="alert"` nativo do Quasar só é aplicado quando a prop `error-message` é usada (`errorMessage.value !== null`); sem ela, o Quasar cai no branch `hSlot(slots.error)`, renderizando exatamente o que o consumidor colocar no slot `#error`. `DsInput.vue` passa apenas `:error="!!error"` (booleano), nunca `error-message` — logo é o bloco manual `<template v-if="error" #error><div role="alert" :aria-label="error">{{ error }}</div></template>` que entrega 100% do contrato de `construction/17-frontend-e2e-behavior-policy.md:96` (`role="alert"` + `aria-label` = texto do erro).

**Dependência real confirmada:** `frontend/test/e2e/equipe/equipe.spec.ts:93,261` e `frontend/test/e2e/singular/singular.spec.ts:68,243` usam `page.getByRole("alert", { name: "..." })` contra erros reais de formulário; removê-lo quebraria esses quatro asserts.

**Uso real:** 6 consumidores diretos fora de `ds/` — `EquipeBasicInfoSection.vue`, `EquipeFilters.vue`, `SingularBasicInfoSection.vue`, `SingularFilters.vue`, `pages/auth/index.vue`, `pages/showcase.vue`.

**Contrato público:** preservado — props, `v-model`, slots `prepend`/`append`, classes `ds-input`/`ds-input--${variant}` — tudo inalterado.

**Testes:** nenhum teste unitário dedicado a `DsInput` (mesma lacuna de `DsAvatar`/`DsBadge`); dependência E2E real listada acima; nada executado por não haver alteração de código.

**Comparação com ciclos anteriores:** mesmo desfecho de `DsIcon`/`DsButton`/`DsBadge` ("CONFORME"), mas primeiro ciclo em que o achado de acessibilidade é necessário e correto, não redundante.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/atoms/DsInput.vue`; `node_modules/quasar/src/composables/private.use-field/use-field.js`; `construction/17-frontend-e2e-behavior-policy.md`; `frontend/test/e2e/equipe/equipe.spec.ts`, `frontend/test/e2e/singular/singular.spec.ts`.

---

## 30. Lote de auditoria registrado — Moléculas de composição fechada (2026-08-19)

**Contexto:** com os 5 átomos `PRESERVE` do inventário concluídos (`DsIcon` §22–23, `DsAvatar` §24–25, `DsButton` §26, `DsBadge` §27–28, `DsInput` §29), o usuário solicitou modo de auditoria em lote: até 5 candidatos consecutivos, mesma lógica de priorização, registrando imediatamente cada diagnóstico CONFORME e parando antes de qualquer alteração caso surja NÃO CONFORME, risco arquitetural relevante, quebra de contrato ou ambiguidade que exija decisão humana.

**Conjunto do lote (ordem fixada por fechamento de dependência, antes de qualquer diagnóstico individual):**

| Ordem | Candidato | Dependências `Ds*` internas | Status das dependências |
|---|---|---|---|
| 1 | `DsNavItem` | `DsIcon`, `DsBadge` | ambas concluídas |
| 2 | `DsProfileSummary` | `DsAvatar`, `DsButton`, `DsIcon` | todas concluídas |
| 3 | `DsSearchInput` | `DsIcon`, `DsInput` | ambas concluídas (Input recém-fechado) |
| 4 | `DsActionCard` | `DsIcon` | concluída |
| 5 | `DsPageHeader` | nenhuma | — |

**Justificativa da ordem:** todas as 5 moléculas têm suas dependências internas de `Ds*` 100% cobertas pelos ciclos já concluídos — nenhuma introduz uma dependência não auditada. `DsNavItem` vem primeiro porque já era apontado desde §27 como fechamento pendente (consumia `DsIcon`+`DsBadge`); `DsProfileSummary` replica a mesma lógica para `DsAvatar`/`DsButton`; `DsSearchInput` fecha com o `DsInput` recém-concluído; `DsActionCard` e `DsPageHeader` fecham o lote (o segundo sem nenhuma dependência `Ds*`, maior contagem de consumidores do grupo — 21 — e contrato E2E documentado).

**Excluídos deste lote (não regridem prioridade, apenas fora do conjunto de 5):** `DsCard` (14 consumidores, mas pass-through sem contrato próprio — próximo lote); `DsDataTable`, `ds-notify` (organismos, tier seguinte); `DsSelect`, `DsBreadcrumbs`, `DsDialog`, `DsFormCard` (todos `RECONSIDER` — pausados pelo gatilho §21 até decisão do item 5 da seção 15); `DsContentCard`, `DsContentCardCompact`, `DsServiceCard`, `DsSectionHeader` (0 consumidores de produção — destino depende de decisão de produto ainda não tomada, § 7/§10 do `INVENTORY-01`).

**Escopo explícito:** este registro não autoriza implementação; não garante que todos os 5 itens do lote resultarão CONFORME.

**Fontes:** `DS-RECONSTRUCTION-INVENTORY-01` §1–§2; `frontend/src/components/ds/molecules/*.vue`.

---

## 31. Achado registrado — `DsNavItem`: risco de acessibilidade pré-existente em modo `mini` (decisão pendente) (2026-08-19)

**Contexto:** diagnóstico formal do primeiro item do lote (§30). Auditoria interrompida nos termos da regra 7 do pedido do usuário — não é `NÃO CONFORME` de tokens/encapsulamento, é um risco de acessibilidade real que exige decisão humana antes de qualquer registro de conformidade.

**O que está conforme:**
- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — `ds.scss` (`.ds-nav-item` e modificadores) usa exclusivamente `var(--spacing-*)`, `var(--color-*)`, `var(--radius-*)`, `var(--text-*)`, `var(--font-weight-*)`; nenhum valor literal.
- §20 (encapsulamento Quasar): conforme — `DsNavItem` não usa Quasar diretamente (renderiza `button`/`a`/`router-link` nativo via `<component :is="tag">`).
- Dependências internas (`DsIcon`, `DsBadge`) já auditadas e conformes (§23, §28).

**O que não está conforme — achado de acessibilidade:**

Em modo `mini` (`props.mini === true`), o template renderiza apenas o ícone:

```
<span v-if="!mini" class="ds-nav-item__label"><slot>{{ label }}</slot></span>
```

O `label` (ou conteúdo do slot) é removido do DOM (`v-if`, não ocultação visual via CSS) e **nenhum `aria-label` é aplicado** ao elemento interativo raiz (`<component :is="tag">` — `button`, `a` ou `router-link`). O resultado é um controle interativo focável sem nome acessível — falha de acessibilidade (WCAG 4.1.2 "Name, Role, Value" / 2.4.4 "Link Purpose").

### Evidências

- `frontend/src/components/ds/molecules/DsNavItem.vue:8-17` — template acima.
- **Não é hipotético:** `frontend/src/components/app/AppSidebar.vue:109-111` define `sidebarMini = shell.sidebarCollapsed.value && !shell.isMobile.value`, controlado por um botão real e visível no rodapé do sidebar (`AppSidebar.vue:40-55`, `@click="shell.toggleSidebarCollapse()"`), com `:aria-label="$t('layout.sidebar.toggleCollapse')"` — ou seja, o **próprio botão de colapsar já segue o padrão correto** que falta em `DsNavItem`.
- `sidebarMini` é propagado por `AppSidebar.vue` → `SidebarMenu.vue` → `SidebarMenuItem.vue:7` (`:mini="mini"`) → `DsNavItem` — cadeia real de produção, não código morto.
- `SidebarMenuItem.vue` sempre recebe e repassa `label` (prop obrigatória), confirmando que o texto existe e está disponível no momento em que é descartado.
- Achado já apontado como observação em §23 ("condição pré-existente... fora do escopo deste registro") durante a validação de `DsIcon`; este registro o traz para dentro do escopo porque `DsNavItem` é agora o componente sob auditoria direta.

### Decisão necessária

Como resolver a ausência de nome acessível em modo `mini`, sem quebrar a API pública existente (gatilho §21):
1. Aplicar `:aria-label="label"` no elemento raiz quando `mini && label` (não introduz prop nova; usa dado já disponível).
2. Adicionar prop opcional `ariaLabel`/`aria-label` explícita, com fallback para `label`.
3. Manter como está e documentar a limitação (não fecha o gap real de acessibilidade).
4. Outra abordagem definida pelo usuário.

### Impacto

- **Escopo:** afeta todo usuário de teclado/leitor de tela navegando com o sidebar colapsado — caminho de navegação primário da aplicação (`AppShell`).
- **Risco de não corrigir:** gap de acessibilidade real permanece em produção; já existia antes desta reconstrução (não introduzido por ela).
- **Risco de corrigir agora:** opção 1 é aditiva (não muda props, apenas adiciona `aria-label` condicional) — não deveria acionar o gatilho de compatibilidade §21. Opção 2 adiciona prop opcional — também aditiva. Nenhuma das duas quebra consumidores existentes (`SidebarMenuItem.vue`, `pages/showcase.vue`).
- **Fora do escopo desta etapa:** por instrução do usuário (regra 10, "não implemente nada nesta etapa"), nenhuma correção é aplicada agora independentemente da opção escolhida.

### Recomendação

Opção 1 (`:aria-label="label"` condicional a `mini`) — resolve o gap real usando dado já disponível, sem nova prop, sem risco de quebra de API, e replica o padrão já validado no próprio botão de colapsar do `AppSidebar`. Fica registrada como recomendação; aguardando decisão do usuário para qualquer implementação.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15). Não decide se `DsProfileSummary`, `DsSearchInput`, `DsActionCard` e `DsPageHeader` (itens 2–5 do lote §30) serão auditados antes ou depois desta decisão.

**Fontes:** `frontend/src/components/ds/molecules/DsNavItem.vue`; `frontend/src/components/app/AppSidebar.vue`; `frontend/src/components/app/sidebar/SidebarMenuItem.vue`; `frontend/src/components/app/sidebar/SidebarMenu.vue`.

---

## 32. Diagnóstico registrado — `DsProfileSummary`: CONFORME (2026-08-19)

**Contexto:** diagnóstico formal do segundo item do lote (§30), continuado após a interrupção em `DsNavItem` (§31) para os 4 itens restantes, a pedido do usuário.

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — `ds.scss` (`.ds-profile-summary` e submodificadores) usa exclusivamente `var(--spacing-*)`, `var(--text-*)`, `var(--color-text-*)`; sem bloco `<style>` no próprio componente; nenhum valor literal.
- §20 (encapsulamento Quasar): conforme — zero uso direto de Quasar; compõe apenas `DsAvatar`, `DsButton`, `DsIcon` (todos já auditados e conformes — §25, §26, §23).
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API proposta.

**Diferença do achado de `DsNavItem` (§31):** em modo `mini`, `DsProfileSummary` esconde `__content` (nome/saudação/botão editar) e mantém só `DsAvatar` — mas `DsAvatar` não é um controle interativo (não é `button`/`a`, é conteúdo informativo/decorativo, como uma imagem), então não há requisito de nome acessível equivalente ao de `DsNavItem`. Não há elemento focável sem nome nesse componente.

**Uso real:** consumido via `SidebarProfile.vue` (wrapper fino de i18n/repasse de props — mesmo padrão de composição já reconhecido em `DS-RECONSTRUCTION-INVENTORY-01` §5), e `pages/showcase.vue`.

**Contrato E2E:** nenhum documentado em `construction/17-frontend-e2e-behavior-policy.md` para este componente.

**Contrato público:** preservado — props (`name`, `greeting`, `subtitle`, `avatarSrc`, `avatarInitials`, `editLabel`, `showEdit`, `mini`, `avatarSize`), evento `edit`, classes `ds-profile-summary*` — tudo inalterado.

**Testes:** nenhum teste unitário ou E2E referencia `DsProfileSummary` diretamente — lacuna de cobertura, não falha de implementação.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/molecules/DsProfileSummary.vue`; `frontend/src/components/ds/ds.scss`; `frontend/src/components/app/sidebar/SidebarProfile.vue`.

---

## 33. Diagnóstico registrado — `DsSearchInput`: CONFORME (2026-08-19)

**Contexto:** diagnóstico formal do terceiro item do lote (§30).

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — **nenhum bloco de estilo próprio existe**; `grep` em `ds.scss` confirma que `.ds-search-input` não tem nenhuma regra dedicada — o componente delega 100% da aparência ao `.ds-input` (já auditado e conforme em §29) via `class="ds-search-input"` adicionada, sem sobrescrever nada.
- §20 (encapsulamento Quasar): conforme — zero uso direto de Quasar; compõe apenas `DsIcon` e `DsInput` (ambos já auditados e conformes — §23, §29).
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API proposta.

**Observação estrutural:** é o item de menor risco do lote — nenhuma superfície visual própria, nenhum estado próprio além do `v-model` repassado ao `DsInput` interno (`type="search"`, `clearable`, ícone de lupa fixo via slot `prepend`).

**Uso real:** consumido diretamente em `AppHeader.vue:42-46` (chrome estrutural do shell, campo de busca do cabeçalho) e `pages/showcase.vue` — consistente com a nota do `DS-RECONSTRUCTION-INVENTORY-01` §1 ("2 consumidores, mas 1 é `AppHeader.vue`... uso estrutural justifica manter mesmo com baixa contagem").

**Contrato E2E:** nenhum documentado em `construction/17-frontend-e2e-behavior-policy.md` para este componente.

**Contrato público:** preservado — props (`label`, `placeholder`, `disable`, `dense`), `v-model`, passthrough de atributos (`inputAttrs`) — tudo inalterado.

**Testes:** nenhum teste unitário ou E2E referencia `DsSearchInput` diretamente — lacuna de cobertura, não falha de implementação.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/molecules/DsSearchInput.vue`; `frontend/src/components/ds/ds.scss`; `frontend/src/components/app/AppHeader.vue`.

---

## 34. Achado registrado — `DsActionCard`: hardcode de cor sem token correspondente (decisão pendente) (2026-08-19)

**Contexto:** diagnóstico formal do quarto item do lote (§30). Auditoria interrompida nos termos da regra 7 do pedido do usuário — segunda interrupção do lote (a primeira foi `DsNavItem`, §31), desta vez por violação real de §19 ("tokens: sem hardcode; primitivo → semântico"), sem token substituto disponível.

**O que está conforme:**
- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §20 (encapsulamento Quasar): conforme — `DsActionCard` não usa Quasar diretamente (`button` nativo); compõe apenas `DsIcon` (já auditado e conforme, §23).
- Estrutura de props/slots: pequena e estável (`label`, `icon`, `description`, `variant`, `disabled`, evento `click`).

**O que não está conforme — hardcode de cor sem token:**

`frontend/src/components/ds/ds.scss:476-483`:

```scss
&--primary {
  background-color: var(--color-primary);
  color: #ffffff;

  .ds-action-card__description {
    color: rgb(255 255 255 / 0.85);
  }
}
```

Dois valores literais (`#ffffff`, `rgb(255 255 255 / 0.85)`) sem `var()`, violando diretamente o critério de §19 ("nenhum valor de token existente é alterado... camada primitivo → semântico → de componente").

### Evidências

- Busca ampla em `frontend/src/css/tokens/` (`design-tokens.scss` e parciais) por qualquer token de "texto sobre fundo primary" / "cor inversa" / branco semântico: **nenhum resultado**. Os únicos usos de `#ffffff` no sistema de tokens são `--color-background` e `--color-surface` (fundos claros do tema, papel semântico diferente — não "texto sobre primary").
- Diferente do achado de `DsAvatar` (§25), onde os valores hardcoded (`56px`, `72px`) tinham substituto óbvio (extensão da escala primitiva de espaçamento já existente): aqui **não existe token candidato na taxonomia atual** para "cor de texto sobre fundo de cor primária" — introduzir um exige decisão de nomenclatura/camada (primitivo `--color-white`? semântico `--color-text-on-primary`? de componente, escopado só a `DsActionCard`?), o que toca a taxonomia já ratificada em §19.
- Único ponto do sistema com esse padrão: `grep` em `ds.scss` por `#fff\|rgb(255` fora deste bloco não retorna outro caso — não é uma convenção já estabelecida em outro componente que se possa replicar.

### Decisão necessária

Como tratar o hardcode de `.ds-action-card--primary`, sem quebrar a API pública (gatilho §21 não afetado — a mudança seria só em `ds.scss`, não na API do componente):
1. Criar token semântico novo (ex. `--color-text-on-primary` / `--color-text-inverse`), com implicação de taxonomia: onde ele entra nos 3 níveis já ratificados em §19 (primitivo vs. semântico)?
2. Criar token primitivo simples (ex. `--color-white: #ffffff;`) e usar `var(--color-white)` — mais conservador, não decide semântica "on-primary" ainda.
3. Manter como hardcode documentado (aceitar exceção pontual, registrar por quê).
4. Outra abordagem definida pelo usuário.

### Impacto

- **Escopo:** afeta apenas a variante visual `--primary` de `DsActionCard`, usada nas páginas Hub de Equipe/Singular/Colaborador (4 consumidores diretos — `EquipeHubPage.vue`, `SingularHubPage.vue`, `ColaboradorHubPage.vue`, `pages/showcase.vue`). Sem risco de acessibilidade (é decoração de cor, não texto ausente) — diferente em natureza do achado de `DsNavItem`.
- **Risco de não corrigir:** inconsistência de modelo permanece (tokens deveriam cobrir 100% das cores usadas; aqui não cobrem); não é regressão nem bug funcional visível ao usuário final.
- **Risco de corrigir agora:** depende da opção — opção 2 é de baixíssimo risco (mesma classe de correção aditiva de `DsAvatar`, §25); opção 1 tem maior peso porque introduz uma decisão de taxonomia não coberta por §19.
- **Fora do escopo desta etapa:** por instrução do usuário (regra 10, "não implemente nada nesta etapa"), nenhuma correção é aplicada agora independentemente da opção escolhida.

### Recomendação

Opção 2 (token primitivo `--color-white`) — resolve o hardcode com o menor comprometimento de decisão arquitetural, mantendo a opção 1 (semântica "on-primary") como evolução possível depois que a taxonomia de tokens "de componente"/inversão for discutida com mais casos reais (hoje só há este). Fica registrada como recomendação; aguardando decisão do usuário para qualquer implementação.

**Escopo explícito:** nenhuma alteração de código, teste, estrutura ou token foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15). Não decide se `DsPageHeader` (item 5 do lote §30, ainda não auditado) será auditado antes ou depois desta decisão.

**Fontes:** `frontend/src/components/ds/molecules/DsActionCard.vue`; `frontend/src/components/ds/ds.scss:459-520`; `frontend/src/css/tokens/design-tokens.scss`; consumidores em `frontend/src/pages/organization/*/HubPage.vue`.

---

## 35. Diagnóstico registrado — `DsPageHeader`: CONFORME, com nota de risco (2026-08-19)

**Contexto:** diagnóstico formal do quinto e último item do lote (§30), auditado por último a pedido do usuário. Fecha a leitura completa do lote de 5 candidatos.

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme quanto a texto/cor — o bloco `.ds-page-header` em `ds.scss:522-544` usa exclusivamente `var(--spacing-lg)`, `var(--color-text-primary)`, `var(--text-page-title-*)`, `var(--color-text-secondary)`, `var(--text-body-size)`, `var(--spacing-xs)`, `var(--spacing-sm)` — nenhum valor literal.
- §20 (encapsulamento Quasar): conforme quanto a componentes — zero uso de `q-*` como componente (`q-btn`, `q-input` etc.); nenhuma dependência `Ds*` interna (é o único item do lote sem composição de outro átomo/molécula).
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API proposta.
- **Contrato E2E documentado e íntegro** — `construction/17-frontend-e2e-behavior-policy.md:98` (`<h1>` via `getByRole('heading', { name })`); `DsPageHeader.vue:6` renderiza `<h1 class="ds-page-header__title">{{ title }}</h1>` diretamente, sem indireção. Dependência real confirmada em `frontend/test/e2e/equipe/equipe.spec.ts`, `singular/singular.spec.ts` e `bootstrap.spec.ts` (uso de `getByRole('heading'...)`).

### Nota de risco registrada — layout via classes utilitárias Quasar, não via token de gap

`DsPageHeader.vue:2-3` usa `class="ds-page-header row items-center justify-between q-col-gutter-md"` e `col`/`col-auto` internos — classes utilitárias de grid/flex do **Quasar** (`row`, `col`, `items-center`, `justify-between`, `q-col-gutter-md`), não a abordagem de `ds.scss` usada pelos demais componentes do lote (ex. `.ds-action-card { display: flex; ...; gap: var(--spacing-sm); }`).

**Verificação de divergência:** inspecionei `node_modules/quasar/src/css/variables.sass:3-31` — `$flex-gutter-md: $space-base` = `16px` (hardcoded no Quasar). Nosso `--spacing-md` (`_spacing.scss:6`, `$spacing-4: 16px`) resolve ao **mesmo valor hoje**, mas por coincidência de escala, não por relação declarada — as duas escalas podem divergir silenciosamente se qualquer uma mudar independentemente (mesma classe de risco já registrada em `DS-RECONSTRUCTION-INVENTORY-01` §4, sobre `_layout.scss` alegar alinhamento com Figma sem garantia).

**Por que não é bloqueante (diferente de `DsActionCard`, §34):** o token equivalente (`--spacing-md`) **já existe** — não há decisão de taxonomia pendente, só uma substituição mecânica de classes Quasar por regras já no padrão de `ds.scss` (`display:flex; gap: var(--spacing-md)`), sem introduzir conceito novo. Nenhum valor visual mudaria hoje (16px = 16px). Mesma categoria de achado não-bloqueante do `DsButton` (§26, dependência de classe CSS em teste) — observação operacional, não defeito.

**Único item do lote sem uso de nenhuma classe Quasar (nem utilitária) na própria composição:** nenhum — `DsActionCard`, `DsNavItem`, `DsProfileSummary`, `DsSearchInput` também não usam nada de Quasar diretamente; `DsPageHeader` é o único dos 5 que usa classes utilitárias do Quasar para o próprio layout.

**Uso real:** 21 consumidores diretos — o mais adotado do lote e o segundo do DS inteiro (atrás de `DsButton`, 23).

**Contrato público:** preservado — props (`title`, `subtitle`), slot `actions` — tudo inalterado.

**Testes:** nenhum teste unitário dedicado; dependência E2E real confirmada e listada acima.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15). Não decide as duas pendências já registradas neste lote (`DsNavItem` §31, `DsActionCard` §34).

**Fontes:** `frontend/src/components/ds/molecules/DsPageHeader.vue`; `frontend/src/components/ds/ds.scss`; `node_modules/quasar/src/css/variables.sass`; `frontend/src/css/tokens/_spacing.scss`; `construction/17-frontend-e2e-behavior-policy.md`; `frontend/test/e2e/equipe/equipe.spec.ts`, `singular/singular.spec.ts`, `bootstrap.spec.ts`.

---

## 36. Implementação registrada — `DsNavItem`: `aria-label` condicional em modo `mini` (2026-08-19)

**Contexto:** correção do achado registrado em §31 (elemento interativo sem nome acessível em modo `mini`). Decisão do usuário: Opção 1 — `:aria-label="label"` condicional a `mini`, usando o dado já existente, sem nova prop nem mudança de API pública.

**Implementação:**
- `frontend/src/components/ds/molecules/DsNavItem.vue:5-6` — adicionada a linha `:aria-label="mini ? label : undefined"` ao elemento raiz (`<component :is="tag">`), ao lado do já existente `:aria-current`. Quando `mini` é `false`/omitido, o atributo resolve a `undefined` e não é renderizado — comportamento inalterado nesse caso.
- `frontend/test/unit/components/shared-components.spec.ts` — adicionado `describe("DsNavItem", ...)` com 2 casos: (1) `mini: true` → `aria-label` = `label`, `.ds-nav-item__label` ausente do DOM; (2) sem `mini` → `aria-label` ausente, texto visível presente. Reutiliza o padrão já existente no arquivo (`describe("DsButton", ...)`), sem criar arquivo novo.

**Escopo estritamente respeitado:**
- Nenhuma prop nova, nenhum slot novo, nenhum evento novo.
- Nenhuma outra correção aplicada (`DsIcon`, `DsBadge` internos, classes, `componentAttrs`, lógica de `tag` — tudo inalterado).
- Nenhum outro componente do lote (§30) tocado por esta mudança.

**Contrato:** preservado — props, slots, eventos, classes de `DsNavItem` idênticos a antes; único efeito observável é a presença do atributo `aria-label` no DOM apenas quando `mini === true`.

**Testes:**
- `yarn vitest run test/unit/components/shared-components.spec.ts` — 6/6 testes passando (4 pré-existentes + 2 novos).
- `yarn typecheck` — sem erros.
- `yarn lint` — 3 erros pré-existentes reportados, todos em arquivos não relacionados a esta mudança (`useSingularForm.ts`, `auth.service.spec.ts`, `useLoginPage.spec.ts`); `DsNavItem.vue` e `shared-components.spec.ts` sem erros atribuíveis.

**Por que resolve o achado de §31 sem acionar o gatilho §21:** a mudança é puramente aditiva no DOM renderizado (um atributo `aria-label` condicional) — não altera nenhuma prop, slot, evento ou classe pública consumida por `SidebarMenuItem.vue` ou `pages/showcase.vue`. Nenhum consumidor precisa de atualização.

**Escopo explícito — o que este registro NÃO decide:** não resolve a pendência de `DsActionCard` (§34, ainda em aberto); não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/molecules/DsNavItem.vue`; `frontend/test/unit/components/shared-components.spec.ts`; achado original em §31.

---

## 37. Implementação registrada — `DsActionCard`: token primitivo `--color-white` (2026-08-19)

**Contexto:** correção do achado registrado em §34 (hardcode de cor sem token correspondente em `.ds-action-card--primary`). Decisão do usuário: Opção 2 — criar token primitivo simples (`--color-white`) e usar `var(--color-white)`, em vez da opção semântica "on-primary" (maior peso de decisão de taxonomia, adiada).

**Implementação:**
- `frontend/src/css/tokens/_palette.scss` — adicionado `$color-white: #ffffff;`, em seção própria ("Neutral — theme-invariant"), antes da seção "Semantic". Nenhum valor de cor existente foi alterado.
- `frontend/src/css/tokens/design-tokens.scss` — exposto como `--color-white: #{palette.$color-white};`, no bloco `:root, [data-theme="light"]`, junto aos tokens de marca. **Não redefinido** em `[data-theme="dark"]` — decisão consciente: branco sobre `--color-primary` permanece legível nos dois temas (mesmo padrão já usado pelos primitivos de escala cinza/`primary-*`, que também não são redefinidos no bloco dark; só tokens semânticos de superfície mudam por tema).
- `frontend/src/components/ds/ds.scss:476-483` — `.ds-action-card--primary`: `color: #ffffff` → `color: var(--color-white)`. Para `.ds-action-card__description` (85% de opacidade), em vez de introduzir uma técnica CSS nova sem precedente no repositório (`color-mix()`/`rgb(from ...)`), reaproveitado o padrão já existente no mesmo arquivo (`opacity` para variação de estado — usado em `.ds-action-card--disabled` e `.ds-nav-item--disabled`): `color: rgb(255 255 255 / 0.85)` → `color: var(--color-white); opacity: 0.85;`. Visualmente equivalente (o `<span>` de descrição não tem fundo próprio — `DsActionCard.vue`, elemento de texto puro), sem introduzir sintaxe CSS nova ao projeto.

**Verificação de compilação:** `npx sass` sobre `ds.scss` e `design-tokens.scss` — compilação limpa, `--color-white` presente em `:root` (ausente em `[data-theme="dark"]`, como esperado); `.ds-action-card--primary`/`.ds-action-card--primary .ds-action-card__description` gerados corretamente no CSS de saída.

**Escopo estritamente respeitado:**
- Nenhuma prop, slot, evento ou classe pública de `DsActionCard` alterada — mudança inteiramente em tokens/`ds.scss`.
- Nenhum outro componente do lote (§30) tocado.
- Nenhuma técnica CSS nova introduzida ao repositório (verificado: `color-mix`/`rgb(from` não têm precedente em `src/css` ou `src/components`) — optou-se pelo padrão `opacity` já em uso, mais conservador.

**Contrato:** preservado — nenhum valor visual muda (branco a 100% e a 85% de opacidade, exatamente como antes); API pública de `DsActionCard` inalterada.

**Testes:** nenhum teste unitário/E2E depende de `DsActionCard` (confirmado em §34); `yarn typecheck` e `yarn lint` executados — sem erros atribuíveis a esta mudança (os 3 erros de lint reportados são pré-existentes, em arquivos não relacionados, mesma lista já registrada em §36).

**Por que não aciona o gatilho §21:** mudança é interna a tokens/estilo, sem alteração de API pública consumida por `EquipeHubPage.vue`, `SingularHubPage.vue`, `ColaboradorHubPage.vue` ou `pages/showcase.vue`.

**Escopo explícito — o que este registro NÃO decide:** não ratifica `--color-white` como parte formal da taxonomia de 3 níveis do §19 (é um primitivo pontual, não uma camada semântica "on-primary" — essa decisão mais ampla permanece em aberto, como já registrado em §34); não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/css/tokens/_palette.scss`; `frontend/src/css/tokens/design-tokens.scss`; `frontend/src/components/ds/ds.scss`; achado original em §34.

---

## 38. Lote de auditoria registrado — Organismos e pass-through (2026-08-19)

**Contexto:** com o lote de moléculas concluído (§30–§37: `DsNavItem` corrigido, `DsProfileSummary`/`DsSearchInput`/`DsPageHeader` conformes, `DsActionCard` corrigido), o usuário solicitou o próximo lote: `DsCard`, `DsDataTable`, `ds-notify`.

**Conjunto do lote:**

| Candidato | Categoria (`DS-RECONSTRUCTION-INVENTORY-01`) | Consumidores diretos fora de `ds/` |
|---|---|---|
| `DsCard` | Molécula, `PRESERVE` | 14 — páginas raiz, Hub/List de Equipe/Singular/Colaborador, `InfoCard`s de Feature, `showcase.vue` |
| `DsDataTable` | Organismo, `PRESERVE` | 3 — `SingularListPage.vue`, `EquipeListPage.vue`, `showcase.vue` |
| `ds-notify` | Organismo (runtime, não visual), `PRESERVE` | 10 — via `useNotify.ts` e uso direto em páginas de Equipe/Singular |

**Fontes:** `DS-RECONSTRUCTION-INVENTORY-01` §2–3.

---

## 39. Diagnóstico registrado — `DsCard`: CONFORME (2026-08-19)

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — `ds.scss` (`.ds-card`) e a classe utilitária `.ds-text-card-title` usam exclusivamente `var(--radius-lg)`, `var(--color-surface)`, `var(--border-width-thin)`/`var(--border-style-default)`/`var(--color-border)`, `var(--elevation-card)`, `var(--color-text-secondary)`, `var(--text-body-small-size)`, `var(--spacing-xs)`, `var(--text-card-title-*)`, `var(--color-text-primary)`; sem bloco `<style>` no componente; nenhum valor literal.
- §20 (encapsulamento Quasar): conforme — `DsCard` é o único ponto de acesso não só a `q-card` (já verificado em `DS-RECONSTRUCTION-INVENTORY-01` §6), mas também a `q-card-section`, `q-separator` e `q-card-actions`: `grep` confirma **zero uso direto** de qualquer um dos quatro fora de `ds/` (checagem que vai além do que o inventário original havia verificado explicitamente, já que só `q-card` estava na lista do §6).
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API proposta.

**Confirmação do achado do inventário:** `DsCard` é de fato "sem contrato próprio, pass-through" (`DS-RECONSTRUCTION-INVENTORY-01` §2, `construction/17-frontend-e2e-behavior-policy.md:110`) — não há regra de acessibilidade ou comportamento formalizada além da estrutura visual (header opcional, separador, corpo, ações opcionais).

**Uso real:** 14 consumidores confirmados, incluindo os 3 `InfoCard`s de Feature (`EquipeInfoCard`, `SingularInfoCard`, `ColaboradorInfoCard`, já classificados como Feature em `DS-RECONSTRUCTION-INVENTORY-01` §8) e as páginas Hub/List de Equipe/Singular/Colaborador.

**Contrato público:** preservado — props (`title`, `subtitle`, `variant`), slots (`header`, default, `actions`) — tudo inalterado.

**Testes:** nenhum teste unitário ou E2E referencia `DsCard` diretamente — lacuna de cobertura, não falha de implementação; consistente com o próprio contrato "pass-through" (comportamento validado indiretamente pelos testes das páginas que o consomem).

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/molecules/DsCard.vue`; `frontend/src/components/ds/ds.scss`; `construction/17-frontend-e2e-behavior-policy.md`.

---

## 40. Diagnóstico registrado — `DsDataTable`: CONFORME (2026-08-19)

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — `ds.scss` (`.ds-data-table`) usa exclusivamente `var(--radius-lg)`, `var(--border-width-thin)`/`var(--border-style-default)`/`var(--color-border)`, `var(--color-surface)`, `var(--elevation-card)`, `var(--color-text-primary)`, `var(--text-card-title-*)`, `var(--color-text-secondary)`, `var(--text-label-*)`, `var(--color-surface-elevated)`, `var(--text-body-small-size)`; sem bloco `<style>` no componente; nenhum valor literal.
- §20 (encapsulamento Quasar): conforme — zero uso direto de `q-table` fora de `ds/` (reconfirmado nesta auditoria).
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API proposta.

**Padrão pass-through validado por uso real:** `DsDataTable` repassa slots dinamicamente (`v-for="(_, slotName) in $slots"`) e atributos/listeners não declarados via `useAttrs()` (excluindo `pagination`, já coberto por `defineModel`). Confirmado em produção: `SingularListPage.vue` e `EquipeListPage.vue` usam `v-model:pagination`, `:rows`, `:columns`, `row-key`, `:loading`, `binary-state-sort` (fallthrough) e `@request` (fallthrough de listener) — e o slot nomeado `#body-cell-status` chega corretamente ao `q-table` interno.

**Contrato E2E documentado e íntegro:** `construction/17-frontend-e2e-behavior-policy.md:109-111` classifica explicitamente `DsDataTable` como estrutura pass-through — mudanças internas não devem exigir alteração de E2E se o comportamento funcional (paginação via botão "Próxima página", visibilidade de linha) for preservado. Nenhuma divergência encontrada entre o código atual e essa descrição.

**Uso real:** 3 consumidores diretos — `SingularListPage.vue`, `EquipeListPage.vue`, `showcase.vue`.

**Contrato público:** preservado — props (`title`, `rows`, `columns`, `rowKey`, `loading`), `v-model:pagination`, fallthrough de atributos/eventos, todos os slots — tudo inalterado.

**Testes:** nenhum teste unitário dedicado a `DsDataTable`; comportamento de paginação coberto indiretamente pelos E2E de Equipe/Singular (via `construction/17`).

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/organisms/DsDataTable.vue`; `frontend/src/components/ds/ds.scss`; `frontend/src/pages/organization/singular/SingularListPage.vue`, `equipe/EquipeListPage.vue`; `construction/17-frontend-e2e-behavior-policy.md`.

---

## 41. Diagnóstico registrado — `ds-notify`: CONFORME (2026-08-19)

**Classificação: CONFORME — nenhuma alteração necessária.**

- §18 (fronteira pública): conforme — `dsNotify`, `dsNotifySuccess`, `dsNotifyError`, `dsNotifyWarning`, `dsNotifyInfo` e o tipo `DsNotifyOptions` exportados em `index.ts`.
- §19 (tokens, sem hardcode): conforme — `ds-notify.ts` não define estilo próprio (é lógica pura); a única superfície visual é a classe `ds-notify` repassada a `Notify.create` (`classes: "ds-notify"`), estilizada em `ds.scss` só com `var(--radius-md)`/`var(--text-body-small-size)`.
- §20 (encapsulamento Quasar): conforme — **zero** uso direto de `Notify.create` ou `$q.notify` fora de `ds/`; `ds-notify.ts` é o único ponto de acesso ao plugin `Notify` do Quasar.
- §21 (gatilho de compatibilidade): não acionado — nenhuma mudança de API proposta.

**Diferente em natureza dos demais itens do DS:** não é um componente Vue visual estático comparável ao Figma auditado (`DS-RECONSTRUCTION-INVENTORY-01` §3: "Runtime... não comparável ao Figma... Função utilitária, sem ambiguidade") — é uma camada fina sobre o serviço `Notify` do Quasar, com mapeamento direto de tipo (`positive`/`negative`/`warning`/`info`) e `timeout`/`position` default.

**Consumo real amplo:** 10 arquivos fora de `ds/`, incluindo `frontend/src/composables/useNotify.ts` — verificado como facade pura (repassa as 5 funções sem reimplementar lógica, sem duplicar responsabilidade do DS) — e uso direto em páginas de criação/edição/detalhe de Equipe e Singular, `useStandardErrorHandling.ts`, `usePrimeiroAcessoPage.ts`.

**Contrato público:** preservado — assinatura das 5 funções e do tipo `DsNotifyOptions` inalteradas.

**Testes:** `test/unit/composables/useStandardErrorHandling.spec.ts` e `test/unit/composables/usePrimeiroAcessoPage.spec.ts` exercitam consumidores de `dsNotify*` indiretamente — única cobertura de teste real encontrada em todo este lote de 3 itens.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/organisms/ds-notify.ts`; `frontend/src/components/ds/ds.scss`; `frontend/src/composables/useNotify.ts`; `frontend/test/unit/composables/useStandardErrorHandling.spec.ts`, `usePrimeiroAcessoPage.spec.ts`.

---

## 42. Pendência registrada — `DsSelect`: pausado pelo gatilho §21 (2026-08-19)

**Classificação:** `RECONSIDER` (`DS-RECONSTRUCTION-INVENTORY-01` §1) — **não diagnosticado** nesta etapa (diferente dos itens `PRESERVE` do lote §30/§38, que foram diagnosticados formalmente). Registro de evidência, não de conformidade.

**Motivo original do `RECONSIDER`:** "sem evidência de variantes/estado no Figma auditado — não há Figma correspondente para validar API" (`DS-RECONSTRUCTION-INVENTORY-01` §1).

**Evidência atualizada nesta auditoria — uso real cresceu desde o inventário original:** o inventário registrava 5 consumidores; a contagem atual é **8**: `SingularFilters.vue`, `EquipeBasicInfoSection.vue`, `EquipeForm.vue`, `EquipeFilters.vue`, `useEquipeAreaOptions.ts`, `pages/showcase.vue`, e dois novos do trabalho em andamento de Primeiro Acesso — `usePrimeiroAcessoPage.ts`, `pages/primeiro-acesso/index.vue`. Crescimento real de superfície de risco desde a classificação original.

**Achados estruturais (leitura, sem diagnóstico formal):**
- API: `label`, `placeholder`, `options`, `disable`, `readonly`, `error`, `hint`, `dense`, `multiple` + `v-model`; único ponto de `q-select`.
- Ao contrário de `DsInput` (§29), `DsSelect` passa `:error-message="error"` diretamente ao `q-select` — aciona o `role="alert"` **nativo** do Quasar (mesmo mecanismo verificado em `use-field.js:567-577`), sem precisar de slot manual. Padrão diferente de `DsInput`, mas correto pelo mesmo mecanismo já validado.
- Contrato E2E existente: `construction/17-frontend-e2e-behavior-policy.md:99` — "Labels i18n em `DsInput` / `DsSelect`" → `getByLabel('Nome')`.
- Comportamento fixo não exposto como prop: `emit-value`, `map-options`, `outlined` sempre ativos — decisão de design já tomada, não uma lacuna.

**Por que não foi diagnosticado como CONFORME/NÃO CONFORME:** o gatilho §21 pausa reconstrução que *muda API pública com consumidor real* até a estratégia de compatibilidade (item 5, §15) ser escolhida. Como o objetivo declarado do `RECONSIDER` é justamente **redefinir a API a partir do Figma** (não apenas confirmar conformidade), qualquer diagnóstico completo already pressupõe a possibilidade de mudança de contrato — por isso permanece pausado, não apenas "auditado e aprovado".

**Fontes:** `frontend/src/components/ds/atoms/DsSelect.vue`; `DS-RECONSTRUCTION-INVENTORY-01` §1; `construction/17-frontend-e2e-behavior-policy.md`.

---

## 43. Pendência registrada — `DsBreadcrumbs`: pausado pelo gatilho §21 (2026-08-19)

**Classificação:** `RECONSIDER` (`DS-RECONSTRUCTION-INVENTORY-01` §1) — não diagnosticado.

**Motivo original:** "apenas 2 consumidores; sem evidência de uso no Figma auditado (breadcrumb não aparece nas 8 telas)".

**Consumidores confirmados (inalterado):** `AppShell.vue`, `pages/showcase.vue`.

**Achado estrutural relevante:** é o **único** componente `Ds*` auditado até agora (em todo este processo, §22–§41) que define seu próprio `<style scoped>` (`DsBreadcrumbs.vue:21-26`) em vez de centralizar em `ds.scss` — todos os demais (incluindo `DsDialog`, ver §44) seguem o padrão `ds.scss` como fonte única de estilo do DS. Usa tokens corretamente (`var(--font-size-sm)`, `var(--color-text-secondary)`) — não é hardcode, é inconsistência de **local** da fonte de estilo, relevante para a pendência 3 (catálogo-alvo) e 7 (ferramenta de catálogo) da seção 15.

**Fontes:** `frontend/src/components/ds/molecules/DsBreadcrumbs.vue`; `DS-RECONSTRUCTION-INVENTORY-01` §2.

---

## 44. Pendência registrada — `DsDialog`: pausado pelo gatilho §21 (2026-08-19)

**Classificação:** `RECONSIDER` (`DS-RECONSTRUCTION-INVENTORY-01` §1) — não diagnosticado.

**Motivo original:** "3 consumidores; nenhuma tela do Figma auditado mostra modal (frames estáticos, sem estado de overlay) — sem evidência visual, não sem uso real".

**Consumidores confirmados (inalterado):** `SingularStatusDialog.vue`, `EquipeStatusDialog.vue`, `pages/showcase.vue`.

**Achados estruturais relevantes:**
- **Fragmentação de estilo do mesmo seletor:** `.ds-dialog` é definido em **dois lugares** — `ds.scss:546-551` (`&__subtitle`) e um `<style scoped>` próprio em `DsDialog.vue:49-54` (`border-radius`, `box-shadow`). Ambos usam tokens (sem hardcode), mas a fonte de verdade do componente está dividida entre dois arquivos — mesma classe de observação de `DsBreadcrumbs` (§43), aqui mais grave por já existir uma entrada equivalente em `ds.scss`.
- **Composição interna não usa os próprios átomos do DS:** `DsDialog` usa `q-btn` diretamente para o botão de fechar (`DsDialog.vue:12`) em vez de `<DsButton>`, e reconstrói a estrutura de `q-card`/`q-card-section`/`q-card-actions` manualmente (com a classe `ds-card` reaproveitada) em vez de compor `<DsCard>`. Não viola a métrica de encapsulamento do §20 (que mede uso **fora** de `ds/`), mas é uma inconsistência de composição interna real a considerar se a API for redefinida.
- **Acoplamento de teste é só de contrato, não de implementação:** `test/unit/components/SingularStatusDialog.spec.ts` **stuba** `DsDialog` inteiramente (`DsDialogStub`, linhas 10-26), testando apenas `title`/`subtitle`/slot `actions` — mudanças internas de markup (ex. trocar `q-btn` por `DsButton`) não quebram esse teste; só uma mudança na assinatura de props/slots quebraria.

**Fontes:** `frontend/src/components/ds/molecules/DsDialog.vue`; `frontend/src/components/ds/ds.scss`; `frontend/test/unit/components/SingularStatusDialog.spec.ts`; `DS-RECONSTRUCTION-INVENTORY-01` §2.

---

## 45. Pendência registrada — `DsFormCard`: pausado pelo gatilho §21 (2026-08-19)

**Classificação:** `RECONSIDER` (`DS-RECONSTRUCTION-INVENTORY-01` §2) — não diagnosticado.

**Motivo original:** "é apenas `DsCard` com título fixo — sobreposição funcional quase total; candidato a fusão com `DsCard` (props opcionais), não a retirada".

**Consumidores confirmados:** `SingularForm.vue`, `EquipeForm.vue`, `pages/primeiro-acesso/index.vue`, `pages/showcase.vue`.

**Evidência que confirma a sobreposição apontada pelo inventário:** `DsFormCard.vue` (19 linhas) compõe `<DsCard>` diretamente, mas **não usa o slot `header` de `DsCard`** — em vez disso, coloca seu próprio `<h2 class="ds-form-card__title">` dentro do slot default de `DsCard`. Consequência real: o separador (`q-separator`) que `DsCard` renderiza entre header e corpo quando `title`/slot `header` está presente **não aparece** em `DsFormCard`, porque `DsCard` nunca recebe um título pelo canal esperado. Diferença semântica adicional: `DsCard` usa `<div class="ds-text-card-title">` para o título; `DsFormCard` usa `<h2>` — nível de heading diferente para um papel visualmente equivalente. Confirma concretamente a recomendação do inventário (fusão via prop opcional em `DsCard`, não duas APIs paralelas).

**Fontes:** `frontend/src/components/ds/organisms/DsFormCard.vue`; `frontend/src/components/ds/molecules/DsCard.vue`; `DS-RECONSTRUCTION-INVENTORY-01` §2.

---

## 46. Preparação de decisão — Item 5 da seção 15 (Estratégia de compatibilidade / migração) (2026-08-19)

**Contexto:** os 4 itens `RECONSIDER` com consumidor real (§42–45) estão pausados pelo gatilho de aplicabilidade já registrado em §21: reconstrução que muda API pública de item consumido fica pausada até a escolha entre as 5 alternativas do §13. Este registro **prepara** a decisão — reúne evidência e organiza as alternativas — sem escolher por conta própria; é insumo para decisão humana, não a decisão em si (mesma disciplina de `DsNavItem` §31 e `DsActionCard` §34, aplicada aqui a uma decisão mais ampla de política, não de item único).

### Achado prévio mais importante: nem todo `RECONSIDER` precisa de mudança de API

Precedente direto deste próprio processo: `DsButton`, `DsBadge` e `DsInput` estavam classificados `REBUILD` no baseline original do §3 ("API/contratos devem ser redefinidos a partir do Figma"), e os três resultaram **CONFORME, sem alteração** após diagnóstico formal (§26, §28, §29). Isso significa que a pausa do gatilho §21 só é **necessária de fato** se um diagnóstico completo concluir que a API precisa mudar — não pelo rótulo `RECONSIDER` isoladamente. Recomendação incorporada à decisão abaixo: considerar diagnosticar formalmente `DsSelect`, `DsBreadcrumbs`, `DsDialog` e `DsFormCard` **antes** de aplicar qualquer uma das 5 alternativas — alguns podem se resolver sem nunca precisar da estratégia de compatibilidade, exatamente como aconteceu no lote de átomos.

### As 5 alternativas (§13), avaliadas contra os 4 itens reais

| Alternativa | A favor (evidência deste lote) | Contra (evidência deste lote) |
|---|---|---|
| **1. Substituição direta (mesmo nome/API)** | Simples, sem código de transição; `DsDialog` já tem teste desacoplado da implementação (stub, §44) — tolera mudança interna, mas quebra se a assinatura de props mudar. | Risco imediato para `DsSelect` (8 consumidores reais, incluindo feature em desenvolvimento ativo — Primeiro Acesso) e `DsFormCard` (4 consumidores, incluindo `primeiro-acesso/index.vue`) — qualquer breaking change atinge trabalho em andamento sem aviso. |
| **2. Coexistência temporária** | Reduz risco de quebra simultânea; permite migrar consumidor a consumidor. | Contradiz a decisão já registrada em §18 ("nenhuma camada física `legacy/`/`deprecated/` é criada agora") — reabriria essa pendência adiada deliberadamente. |
| **3. Versionamento explícito** | Mais formal, rastreável. | Maior custo de ferramenta/processo para só 4 itens; nenhuma decisão de ferramenta de catálogo/versionamento foi tomada (pendência 7, §15) — dependência circular. |
| **4. Camada de adapters** | Isola consumidores de mudança de API por um tempo. | Mesma objeção de custo da opção 3; `DsFormCard` já é candidato a **fusão** (não substituição 1:1) — um adapter faria menos sentido que simplesmente estender `DsCard`. |
| **5. Migração Feature a Feature** | Menor blast radius por vez; compatível com o achado de `DsFormCard` (a "migração" ali é literalmente: mover os 4 consumidores para `DsCard` com prop nova, depois retirar `DsFormCard`). Consistente com a disciplina já usada neste processo inteiro (um item por vez, evidência antes de decisão). | Mais lento se houver muitos itens — mas aqui são só 4, com no máximo 8 consumidores cada. |

### Leitura por item (não uma escolha única obrigatória — §13 permite decisão por item)

- **`DsFormCard`:** não é uma decisão de "compatibilidade de API nova vs. antiga" — é uma fusão. Das 5 alternativas, a que melhor descreve o caminho real é a 5 (Migração Feature a Feature): mover os 4 consumidores para `DsCard` + prop `title` opcional, depois retirar `DsFormCard`.
- **`DsSelect`:** maior risco do grupo (mais consumidores, um deles em feature ativa). Recomendo diagnóstico formal primeiro (ver achado acima) — se resultar CONFORME como `DsInput`, a decisão de compatibilidade nem é necessária para este item.
- **`DsBreadcrumbs`:** menor risco (2 consumidores, um é `AppShell` — chrome estrutural). A inconsistência real encontrada (§43) é de **local** do estilo, não de API — resolvível sem gatilho de compatibilidade (só mover CSS de `<style scoped>` para `ds.scss`, sem tocar props).
- **`DsDialog`:** teste já desacoplado de implementação (§44) reduz o risco de mudanças internas (ex. compor `DsButton`/`DsCard` em vez de `q-btn`/`q-card` diretos) — essas mudanças poderiam avançar **sem** esperar o item 5, pois não tocam a API pública (`title`, `subtitle`, `persistent`, `maximized`, `minWidth`, `v-model`, slots). Só mudança de **assinatura** exigiria a estratégia de compatibilidade.

### Recomendação

Não escolher uma alternativa única e genérica para os 4 de uma vez. Em vez disso:
1. Diagnosticar formalmente os 4 itens primeiro (mesmo processo já aplicado aos `PRESERVE`) — separa o que é "correção interna sem risco de API" (provável em `DsBreadcrumbs`, possivelmente em `DsDialog`) do que é "mudança real de contrato" (mais provável em `DsSelect`, e a fusão de `DsFormCard`).
2. Só para os itens que o diagnóstico confirmar como exigindo mudança de API pública, aplicar a Alternativa 5 (Migração Feature a Feature) — é a que melhor se alinha à disciplina de risco já seguida neste processo inteiro (`DsIcon` → `DsPageHeader`) e à natureza de baixo volume de consumidores (2 a 8 por item, não centenas).

Fica registrada como recomendação; a escolha final — inclusive se diagnosticar primeiro ou decidir a política agora — permanece com o usuário.

**Escopo explícito — o que este registro NÃO decide:** não escolhe a alternativa do §13; não autoriza diagnóstico ou implementação de nenhum dos 4 itens; não resolve as pendências 1, 3, 6, 7, 8 da seção 15.

**Fontes:** §13, §15, §18, §21 deste documento; §42–45 (evidência dos 4 itens); `DS-RECONSTRUCTION-INVENTORY-01` §1–2, §9.

---

## 47. Diagnóstico registrado — `DsSelect`: CONFORME, API não precisa mudar (2026-08-19)

**Contexto:** diagnóstico formal do primeiro item `RECONSIDER` (§42), a pedido do usuário — "diagnosticar os 4 formalmente antes de decidir a política" (item 5, §15). Confirma a hipótese registrada em §46: nem todo `RECONSIDER` exige o gatilho §21.

**Classificação: CONFORME — nenhuma mudança de API necessária.** O motivo original do `RECONSIDER` ("sem Figma para validar API") permanece uma lacuna de **evidência de design**, não um defeito de implementação — não há nada no código que precise ser corrigido para ficar em conformidade com §18–§20.

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — sem `<style>` próprio; compartilha a regra `.ds-input, .ds-select` de `ds.scss:84-105` (só `var(--radius-md)`, `var(--color-text-*)`, `var(--text-*)`, `var(--color-surface-muted)`); nenhum valor literal.
- §20 (encapsulamento Quasar): conforme — zero uso direto de `q-select` fora de `ds/` (reconfirmado).
- §21 (gatilho de compatibilidade): **não acionado** — nenhuma correção identificada que exija mudança de API pública.

**Contrato E2E confirmado (não apenas suposto):** `construction/17-frontend-e2e-behavior-policy.md:99` — labels i18n em `DsSelect` via `getByLabel`. Verificado em uso real: `frontend/test/e2e/equipe/equipe.spec.ts:28` (`getByLabel("Área")`) e `:179,186` (`getByLabel("Status")`) — dependência real e funcional hoje.

**Diferença notável de `DsInput` (§29):** `DsSelect` passa `:error-message="error"` diretamente ao `q-select` (em vez do padrão de slot manual `#error` usado por `DsInput`) — isso aciona o `role="alert"` **nativo** do Quasar (mesmo mecanismo de `use-field.js:567-577`, mas pelo branch que `DsInput` não usa). Duas implementações diferentes para o mesmo contrato de acessibilidade, ambas corretas — não é inconsistência que exija correção, é uma observação de que os dois componentes resolveram o mesmo problema por caminhos distintos do próprio Quasar.

**Uso real:** 8 consumidores confirmados (ver §42 para lista completa e nota sobre crescimento desde o inventário original).

**Contrato público:** preservado — nenhuma mudança proposta ou necessária.

**Testes:** nenhum teste unitário dedicado; dependência E2E real confirmada acima.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/atoms/DsSelect.vue`; `frontend/src/components/ds/ds.scss`; `construction/17-frontend-e2e-behavior-policy.md`; `frontend/test/e2e/equipe/equipe.spec.ts`.

---

## 48. Diagnóstico registrado — `DsBreadcrumbs`: CONFORME, com nota (2026-08-19)

**Classificação: CONFORME — nenhuma mudança de API necessária.**

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — `<style scoped>` próprio (`DsBreadcrumbs.vue:21-26`) usa `var(--font-size-sm)`, `var(--color-text-secondary)`; `active-color="primary"` é um nome semântico do tema Quasar (mesmo padrão já validado em `DsButton`/`DsBadge`, não um literal de cor).
- §20 (encapsulamento Quasar): conforme — zero uso direto de `q-breadcrumbs`/`q-breadcrumbs-el` fora de `ds/` (reconfirmado).
- §21 (gatilho de compatibilidade): **não acionado** — nenhuma correção identificada que exija mudança de API pública.

**Nota de risco registrada — local do estilo (já antecipada em §43):** é o único componente `Ds*` de todo este processo (§22–§48) com estilo definido em `<style scoped>` próprio em vez de centralizado em `ds.scss`. Não é hardcode (tokens corretos) nem quebra de contrato — é uma inconsistência de **onde** a fonte de verdade do estilo mora, relevante para as pendências 3 e 7 da seção 15 (catálogo-alvo / ferramenta de regressão visual), não para o gatilho §21 (não toca API pública). Correção possível (mover para `ds.scss`) não foi aplicada nesta etapa — fora do pedido do usuário ("diagnosticar", não "corrigir").

**Uso real:** 2 consumidores — `AppShell.vue` (chrome estrutural) e `pages/showcase.vue`.

**Contrato público:** preservado — prop `items` (`DsBreadcrumbItem[]`) inalterada.

**Testes:** nenhum teste unitário ou E2E referencia `DsBreadcrumbs` diretamente.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/molecules/DsBreadcrumbs.vue`; `frontend/src/components/app/AppShell.vue`.

---

## 49. Achado registrado — `DsDialog`: botão de fechar sem nome acessível (decisão pendente) (2026-08-19)

**Contexto:** diagnóstico formal do terceiro item `RECONSIDER` (§44). Auditoria interrompida — mesma disciplina de `DsNavItem` (§31): não é problema de tokens/encapsulamento, é um risco real de acessibilidade que exige decisão humana. **A auditoria dos 4 itens para antes deste ponto** — `DsFormCard` não foi diagnosticado nesta rodada.

**O que está conforme:**
- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): conforme — tanto `ds.scss:546-551` quanto o `<style scoped>` próprio (`DsDialog.vue:49-54`) usam só `var(--radius-lg)`, `var(--elevation-modal)`, `var(--color-text-secondary)`, `var(--text-body-small-size)`.
- §20 (encapsulamento Quasar): conforme quanto à métrica formal — zero uso direto de `q-dialog` **fora** de `ds/` (reconfirmado).

**O que não está conforme — botão de fechar sem nome acessível:**

`DsDialog.vue:12`:
```vue
<q-btn v-close-popup icon="mdi-close" flat round dense />
```

Inspecionei `node_modules/quasar/src/components/btn/use-btn.js` e `QBtn.js` por completo: **nenhuma lógica gera `aria-label` para botão só-ícone** — os únicos usos de `aria-*` no arquivo são `aria-disabled` e os `aria-value*` de progresso. Diferente do botão de colapsar do `AppSidebar` (que passa `:aria-label` explicitamente, achado já registrado em §31) ou do botão "limpar" nativo do `QField` (`use-field.js:465`, `'aria-label': $q.lang.label.clear` — recurso interno do próprio Quasar). Como `DsIcon` sempre define `aria-hidden="true"` no ícone (§23), o botão de fechar do `DsDialog` fica **sem nenhum texto acessível**: nem conteúdo textual, nem `aria-label`, nem `title`. Falha de acessibilidade (WCAG 4.1.2 "Name, Role, Value").

### Evidências

- Renderizado **incondicionalmente** em todo `DsDialog` — não há prop para omitir ou nomear o botão.
- Não é hipotético: `SingularStatusDialog.vue` e `EquipeStatusDialog.vue` são os 2 consumidores reais em produção (além de `showcase.vue`) — toda vez que um diálogo de status de Equipe ou Singular abre, o botão de fechar está presente e sem nome.
- Busca em `src/i18n/pt-BR.ts` por chave "fechar"/"close": **nenhuma existe** — ao contrário do achado de `DsNavItem` (§31), aqui não há um dado já disponível (como `label`) para reaproveitar; a correção exigiria nova chave de i18n e importar `useI18n` em `DsDialog.vue` (hoje sem nenhum uso de i18n), ou um valor default hardcoded ("Fechar"), ou uma prop nova opcional.

### Achados adicionais (não bloqueantes, registrados para o catálogo-alvo, sem ação)

- **Fragmentação de estilo:** `.ds-dialog` dividido entre `ds.scss` e `<style scoped>` próprio (já antecipado em §44) — mesma categoria de `DsBreadcrumbs` (§48).
- **Composição interna:** usa `q-btn`/`q-card`/`q-card-section`/`q-card-actions` diretamente em vez de `DsButton`/`DsCard` — não viola a métrica formal do §20 (uso fora de `ds/`), mas é inconsistência de composição interna a considerar numa eventual reconstrução.
- **Acoplamento de teste é só de contrato:** `SingularStatusDialog.spec.ts` stuba `DsDialog` inteiramente (props/slots) — uma correção que só mexe em markup interno (ex. trocar `q-btn` por `DsButton` com `aria-label`) não quebraria esse teste.

### Decisão necessária

Como nomear o botão de fechar, sem quebrar a API pública (gatilho §21 não seria acionado por nenhuma das opções abaixo — todas são aditivas):
1. Adicionar `useI18n` + nova chave i18n (ex. `common.close` ou `layout.dialog.close`) e aplicar `:aria-label` fixo no botão — sem nova prop, sem input do consumidor.
2. Adicionar prop opcional `closeLabel` com default (string fixa ou já vinda de i18n no default), permitindo que o consumidor sobrescreva se necessário.
3. Manter como está e documentar a limitação.
4. Outra abordagem definida pelo usuário.

### Impacto

- **Escopo:** afeta todo usuário de leitor de tela que abre qualquer diálogo de status (Equipe, Singular) — fluxo de produto real, não edge case.
- **Diferença de `DsNavItem` (§31):** lá havia um dado já disponível (`label`) para reaproveitar sem custo; aqui não há — a correção tem um passo a mais (nova chave i18n ou string fixa), o que é uma decisão de "como", não de "se".
- **Risco de corrigir agora:** baixo — todas as opções são aditivas, não tocam a assinatura pública (`title`, `subtitle`, `persistent`, `maximized`, `minWidth`, `v-model`, slots); o teste existente (stub) não quebra em nenhuma delas.
- **Fora do escopo desta etapa:** por instrução do usuário ("diagnosticar... antes de decidir a política"), nenhuma correção é aplicada agora.

### Recomendação

Opção 1 (chave i18n fixa, sem nova prop) — mais simples, consistente com o padrão de acessibilidade já usado no `AppSidebar` (`:aria-label="$t(...)"`), sem expandir a API pública. Fica registrada como recomendação; aguardando decisão do usuário.

**Escopo explícito:** nenhuma alteração de código, teste, estrutura ou token foi feita. `DsFormCard` (quarto item, §45) **não foi diagnosticado** nesta rodada — auditoria parada aqui conforme a disciplina já estabelecida. Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/molecules/DsDialog.vue`; `node_modules/quasar/src/components/btn/use-btn.js`, `QBtn.js`; `frontend/src/i18n/pt-BR.ts`; `frontend/src/components/organization/singular/SingularStatusDialog.vue`, `equipe/EquipeStatusDialog.vue`; `frontend/test/unit/components/SingularStatusDialog.spec.ts`; achado original em §44.

---

## 50. Achado registrado — `DsFormCard`: inconsistência visual confirmada com `DsCard` (decisão pendente) (2026-08-19)

**Contexto:** diagnóstico formal do quarto e último item `RECONSIDER` (§45). Diferente de `DsDialog` (§49, defeito de acessibilidade) e de `DsNavItem`/`DsActionCard` (§31/§34, tokens/a11y), aqui a natureza do achado é de **duplicação arquitetural com efeito visual real já confirmado em produção** — mais próximo do item 3 da seção 15 (catálogo-alvo de componentes) e do item 6 (critérios de promoção/fusão) do que de um bug isolado.

**O que está conforme (mecânico, §18–§20):**
- §18: exportado em `index.ts`.
- §19 (tokens): conforme — sem `<style>` próprio; `.ds-form-card` em `ds.scss` usa só `var(--color-text-primary)`, `var(--text-card-title-*)`, `var(--spacing-md)`.
- §20 (encapsulamento): conforme — não usa Quasar diretamente, compõe `<DsCard>` (já auditado, §39).
- §21: não seria acionado por nenhuma das opções de correção abaixo — mudanças possíveis são aditivas ou de migração planejada, não quebra súbita de API.

**O que não está conforme — inconsistência visual confirmada, não apenas teórica:**

`DsFormCard.vue:2-10` compõe `<DsCard>` mas **nunca aciona a condição de header** de `DsCard` (`title || $slots.header`, `DsCard.vue:9`) — coloca seu próprio `<h2>` dentro do slot **default** de `DsCard`, não no slot `header`. Resultado real: nenhum dos 3 consumidores de `DsFormCard` (todos passam `:title`) recebe o `q-separator` que `DsCard` renderiza entre título e conteúdo.

**Evidência cruzada que confirma a inconsistência (não é hipótese):** `EquipeInfoCard.vue:2-8` usa `<DsCard :title="equipe.name">` **com** o slot `#header` — esse consumidor real, em produção, **recebe** o separador. Ou seja, hoje o produto tem dois tratamentos visuais diferentes para "card com título" convivendo lado a lado: `EquipeInfoCard`/`SingularInfoCard`/`ColaboradorInfoCard` (com separador, via `DsCard` direto) vs. `EquipeForm`/`SingularForm`/`pages/primeiro-acesso/index.vue` (sem separador, via `DsFormCard`) — e nada na especificação ou no Figma auditado (`DS-RECONSTRUCTION-INVENTORY-01` §7) indica que essa diferença é intencional.

**Diferenças adicionais confirmadas nos 3 consumidores reais (`SingularForm.vue:3`, `EquipeForm.vue:3`, `primeiro-acesso/index.vue:26-29`):** todos passam apenas `title`; nenhum usa `subtitle` ou `variant` (props que `DsCard` tem e `DsFormCard` não expõe). Nível de heading também diverge: `DsCard` usa `<div class="ds-text-card-title">`, `DsFormCard` usa `<h2>` — semântica de documento diferente para o mesmo papel visual.

**Nenhum teste (unit ou E2E) verifica a presença/ausência do separador** — a inconsistência é real no DOM/visual, mas não está coberta por nenhum contrato de teste hoje; corrigi-la não quebraria suites existentes.

### Decisão necessária

Como tratar a duplicação, sem decidir sozinho uma mudança que afeta 3 consumidores reais (incluindo a feature em desenvolvimento ativo, Primeiro Acesso):
1. **Fusão com `DsCard`** (recomendação original do inventário, `DS-RECONSTRUCTION-INVENTORY-01` §2) — estender `DsCard` para cobrir o caso de uso de `DsFormCard` (ou já é suficiente como está, bastando os 3 consumidores passarem a usar `DsCard` diretamente com o slot `header`), migrar os 3 consumidores, depois retirar `DsFormCard`. Corresponde à Alternativa 5 do §13 (Migração Feature a Feature), já antecipada em §46.
2. **Corrigir `DsFormCard` para repassar corretamente a `DsCard`** (usar o slot `header`/prop `title` de `DsCard` em vez de duplicar a marcação) — mantém os dois componentes separados, mas elimina a inconsistência visual. Mudança aditiva, sem quebra de API pública de `DsFormCard`.
3. **Manter como está**, registrando que "card de formulário sem separador" é uma decisão de produto aceita (não uma falha) — requer confirmação explícita, já que hoje não há evidência de que foi uma escolha deliberada.
4. Outra abordagem definida pelo usuário.

### Impacto

- **Escopo:** os 3 consumidores reais de `DsFormCard` — `EquipeForm.vue`, `SingularForm.vue`, `pages/primeiro-acesso/index.vue` (feature em desenvolvimento ativo). Puramente visual — sem risco de acessibilidade (diferente de `DsDialog`, §49) e sem token faltando (diferente de `DsActionCard`, §34 original).
- **Risco de não decidir agora:** a inconsistência visual permanece em produção (já está lá, não é introduzida por este achado); baixo risco funcional, risco de percepção de qualidade/consistência do produto.
- **Risco de cada opção:** Opção 2 é a de menor risco/escopo (só `DsFormCard.vue`, sem tocar consumidores). Opção 1 é maior escopo (3 arquivos de Feature + remoção de um export do barrel) mas resolve a duplicação na raiz, como o inventário já recomendava.
- **Fora do escopo desta etapa:** por instrução do usuário ("diagnosticar... antes de decidir a política"), nenhuma correção é aplicada agora.

### Recomendação

Opção 2 como correção imediata de baixo risco (elimina a inconsistência visual sem tocar consumidores), com Opção 1 como evolução planejada quando a fusão for decidida — não são mutuamente exclusivas: corrigir o repasse ao `DsCard` agora não impede a fusão completa depois. Fica registrada como recomendação; aguardando decisão do usuário.

**Escopo explícito:** nenhuma alteração de código, teste ou estrutura foi feita. Com este registro, os 4 itens `RECONSIDER` (§42–45) estão todos formalmente diagnosticados (§47–50) — 2 CONFORME (`DsSelect`, `DsBreadcrumbs`), 2 com decisão pendente (`DsDialog` §49, `DsFormCard` §50). Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/organisms/DsFormCard.vue`; `frontend/src/components/ds/molecules/DsCard.vue`; `frontend/src/components/organization/equipe/EquipeInfoCard.vue`, `EquipeForm.vue`; `frontend/src/components/organization/singular/SingularForm.vue`; `frontend/src/pages/primeiro-acesso/index.vue`; achado original em §45; `DS-RECONSTRUCTION-INVENTORY-01` §2.

---

## 51. Decisão registrada — `DsDialog`: correção de acessibilidade aprovada, API visual mantida em `RECONSIDER` (2026-08-19)

**Contexto:** decisão do usuário sobre o achado registrado em §49, informada pela análise de decisão (item 5 §15 × gatilho §21) apresentada em chat e resumida aqui.

**Decisão:**
1. **Aprovada a correção de acessibilidade pela Opção 1** (§49) — chave i18n fixa + `aria-label` no botão de fechar. Nenhuma prop nova. Nenhuma mudança de API pública.
2. **`DsDialog` permanece `RECONSIDER`** quanto à sua definição visual/API mais ampla (título/subtítulo fixos, ausência de variantes, composição interna via `q-btn`/`q-card` diretos) — pausa mantida **não** pelo gatilho de compatibilidade §21 (que, conforme a análise, não se aplica à correção aprovada), mas pela lacuna genuína de evidência: nenhum frame de modal no Figma auditado (`DS-RECONSTRUCTION-INVENTORY-01` §2) para validar essa API mais ampla. Enquanto essa evidência não existir, a reconstrução estrutural de `DsDialog` segue pausada.

**Base da decisão — por que o gatilho §21 não bloqueia esta correção:** nenhuma das 3 opções do Eixo A (§49) altera a assinatura pública de `DsDialog` (`title`, `subtitle`, `persistent`, `maximized`, `minWidth`, `v-model`, slots `default`/`actions`); o teste existente (`SingularStatusDialog.spec.ts`) stuba o componente e só depende de props/slots; não há contrato E2E formal para `DsDialog` em `construction/17-frontend-e2e-behavior-policy.md`. Mesma conclusão já obtida para `DsButton`, `DsBadge`, `DsInput`, `DsSelect`, `DsBreadcrumbs`.

---

## 52. Implementação registrada — `DsDialog`: `aria-label` no botão de fechar (2026-08-19)

**Contexto:** implementação da decisão registrada em §51.

**Implementação:**
- `frontend/src/i18n/pt-BR.ts` — adicionada a chave `common.close: "Fechar"` ao namespace `common` já existente (junto a `loading`, `notFound`).
- `frontend/src/components/ds/molecules/DsDialog.vue:9-17` — o `q-btn` de fechar passou a receber `:aria-label="$t('common.close')"`. Projeto usa `vue-i18n` em modo `legacy: false, globalInjection: true` (`src/i18n/instance.ts`) — `$t` disponível em qualquer template sem precisar de `useI18n()` no `<script setup>`; nenhuma outra alteração de script foi necessária.
- `frontend/test/unit/components/shared-components.spec.ts` — adicionado `describe("DsDialog", ...)`, reaproveitando o arquivo/padrão já usado para `DsButton`/`DsNavItem` (§36). Diferente dos testes anteriores, este monta o componente real (não um stub) com `modelValue: true` e um plugin `i18n` local (mesmo padrão de `SingularStatusDialog.spec.ts:44-50`, `createI18n` com as mensagens reais de `pt-BR`), porque `DsDialog` usa `q-dialog` (Quasar Teleport) — o botão só existe no DOM depois que o diálogo abre e o conteúdo é teleportado para `document.body`; teste usa `attachTo: document.body` + `await wrapper.vm.$nextTick()` + `document.body.querySelector('button[aria-label="Fechar"]')` em vez de `wrapper.find(...)`.

**Escopo estritamente respeitado:**
- Nenhuma prop nova, nenhum slot novo, nenhum evento novo em `DsDialog`.
- Nenhuma outra correção aplicada — a fragmentação de estilo (`ds.scss` + `<style scoped>`) e a composição interna via `q-btn`/`q-card` diretos (achados não-bloqueantes de §44/§49) **não** foram tocadas.
- Nenhum outro componente alterado.

**Contrato:** preservado — props, slots, eventos, classes de `DsDialog` idênticos a antes; único efeito observável é o atributo `aria-label="Fechar"` no botão de fechar.

**Testes e validações executados:**
- `yarn vitest run test/unit/components/shared-components.spec.ts` — 7/7 passando (6 pré-existentes + 1 novo).
- `yarn vitest run test/unit/components/SingularStatusDialog.spec.ts` — 3/3 passando, confirmando que o stub de `DsDialog` (que testa só props/slots) não foi afetado pela mudança interna.
- `yarn typecheck` — sem erros.
- `yarn lint` — 3 erros pré-existentes reportados, todos em arquivos não relacionados (`useSingularForm.ts`, `auth.service.spec.ts`, `useLoginPage.spec.ts`, mesma lista de §36/§37).

**Por que resolve o achado de §49 sem acionar o gatilho §21:** mudança puramente aditiva no DOM renderizado — nenhuma prop, slot, evento ou classe pública consumida por `SingularStatusDialog.vue`, `EquipeStatusDialog.vue` ou `pages/showcase.vue` foi alterada.

**Escopo explícito — o que este registro NÃO decide:** não resolve `DsFormCard` (§50, ainda em aberto); não resolve a lacuna de Figma que mantém `DsDialog` em `RECONSIDER` quanto à API mais ampla; não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/i18n/pt-BR.ts`; `frontend/src/components/ds/molecules/DsDialog.vue`; `frontend/test/unit/components/shared-components.spec.ts`; `frontend/test/unit/components/SingularStatusDialog.spec.ts`; achados originais em §44, §49, §51.

---

## 53. Decisão registrada — `DsFormCard`: Opção A aprovada (correção visual do repasse a `DsCard`) (2026-08-19)

**Contexto:** decisão do usuário sobre o achado registrado em §50 e as opções detalhadas em chat.

**Decisão:** aprovada a **Opção A** — corrigir o repasse interno de `DsFormCard` a `DsCard` para usar o slot `header`, em vez de fundir `DsFormCard` em `DsCard` (Opção B) ou manter a inconsistência como está (Opção C).

**Natureza explícita da alteração — visual, não estrutural:** a mudança faz `DsFormCard` acionar a condição de header já existente em `DsCard` (`title || $slots.header`, `DsCard.vue:9,16`), que hoje nunca era satisfeita porque `DsFormCard` colocava o título no slot default em vez do slot `header`. Efeito visual real: **os 3 consumidores de `DsFormCard` (`EquipeForm.vue`, `SingularForm.vue`, `pages/primeiro-acesso/index.vue`) passam a apresentar o `q-separator` entre título e conteúdo quando há `title`** — alinhando o visual ao já usado por `EquipeInfoCard`/`SingularInfoCard`/`ColaboradorInfoCard` (que consomem `DsCard` diretamente com o mesmo padrão).

**Opção B (fusão com `DsCard`) permanece apenas como possibilidade futura** — não decidida, não descartada, e **não faz parte desta alteração**. Se e quando a fusão for avaliada, ela é independente desta correção (esta não impede nem antecipa aquela).

---

## 54. Implementação registrada — `DsFormCard`: título movido para o slot `header` de `DsCard` (2026-08-19)

**Contexto:** implementação da decisão registrada em §53.

**Arquivo alterado (único, conforme instrução):** `frontend/src/components/ds/organisms/DsFormCard.vue`.

**Alteração realizada:**
```diff
   <DsCard class="ds-form-card">
-    <h2 v-if="title" class="ds-form-card__title">{{ title }}</h2>
+    <template v-if="title" #header>
+      <h2 class="ds-form-card__title">{{ title }}</h2>
+    </template>
     <div class="ds-form-card__content">
       <slot />
     </div>
```

**Escopo estritamente respeitado:**
- Prop `title` — preservada integralmente (mesmo tipo, mesmo `v-if`).
- Slot default — preservado integralmente (`<div class="ds-form-card__content"><slot /></div>` inalterado).
- Slot `actions` — preservado integralmente (bloco `<template v-if="$slots.actions" #actions>` inalterado).
- Comportamento sem `title`: preservado — `v-if="title"` continua controlando a renderização; sem `title`, nenhum slot `header` é passado a `DsCard`, logo nenhum header/separador aparece (mesmo comportamento de antes).
- Nenhuma prop pública, slot, evento, classe ou export do barrel alterado.
- Nenhum consumidor (`EquipeForm.vue`, `SingularForm.vue`, `pages/primeiro-acesso/index.vue`) tocado.
- Nenhum outro componente, teste ou estrutura de diretórios alterado.
- Nenhuma outra correção aproveitada (fragmentação de estilo/composição interna de outros componentes — fora de escopo, não tocados).

**Contrato:** preservado — `DsFormCard` continua com a mesma assinatura pública (`title?: string`, slots `default`/`actions`); o `<h2 class="ds-form-card__title">` continua sendo o elemento renderizado para o título (mesma classe, mesmo nível de heading) — só o **slot de destino** dentro de `DsCard` mudou, não a marcação do próprio título.

**Testes/validações executados:**
- `yarn typecheck` — sem erros.
- `yarn vitest run test/unit/composables/useSingularForm.spec.ts test/unit/composables/usePrimeiroAcessoPage.spec.ts test/unit/organization/useEquipeForm.spec.ts test/unit/components/shared-components.spec.ts` — 23/23 passando (nenhum desses arquivos monta `EquipeForm.vue`/`SingularForm.vue`/`primeiro-acesso/index.vue`/`DsFormCard` diretamente — são testes de composable puros; confirmação de que nada quebrou, não cobertura direta do componente).
- `yarn lint` — 3 erros pré-existentes reportados, todos em arquivos não relacionados (`useSingularForm.ts`, `auth.service.spec.ts`, `useLoginPage.spec.ts`, mesma lista de §36/§37/§52).
- Nenhum teste unitário ou E2E existente verifica presença/ausência do `q-separator` em `DsFormCard`/`DsCard` (confirmado em §50) — a mudança visual não tinha e não tem cobertura de regressão automatizada.

**Impacto visual esperado:** nas 3 telas reais que usam `DsFormCard` com `title` (criação/edição de Equipe, criação/edição de Singular, Primeiro Acesso), um `q-separator` (linha divisória, `ds.scss` sem alteração de token) passa a aparecer entre o título e o conteúdo do formulário — mesmo tratamento visual já usado nos `InfoCard`s de Equipe/Singular/Colaborador.

**Escopo explícito — o que este registro NÃO decide:** não decide a Opção B (fusão com `DsCard`) — permanece possibilidade futura, não avaliada nem agendada; não resolve a lacuna de Figma que mantém `DsDialog` em `RECONSIDER` (§51); não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/organisms/DsFormCard.vue`; `frontend/src/components/ds/molecules/DsCard.vue`; achados originais em §45, §50, §53.

---

## 55. Decisão registrada — `DsServiceCard`: permanência confirmada, interatividade não decidida, tokens tratados à parte (2026-08-19)

**Contexto:** decisão do usuário sobre a análise de decisão de `DsServiceCard` (candidato órfão do lote de auditoria §22 em diante, sem consumidor de produção, evidência de Figma de nível "fato" para o padrão visual — `AUDITORIA-DS-FIGMA-01.md:44`). A análise aprofundou um achado adicional: a prop `clickable` (`default: true`) só controla `cursor`/`hover` via CSS, sem nenhuma implementação funcional de clique, teclado ou navegação — mesmo padrão incompleto encontrado em `DsContentCardCompact`, ambos introduzidos no mesmo commit de fundação (`8fd83f9`).

Esta decisão trata **quatro questões distintas separadamente** — não devem ser fundidas em uma só conclusão:

### a) Permanência do componente no catálogo — decidida

`DsServiceCard` **permanece** no catálogo-alvo do Design System. A evidência de Figma disponível (`AUDITORIA-DS-FIGMA-01.md:44` — "Linhas Service Desk/Zimbra/CapacitaCoop/Faculdade Unimed", 4 blocos com mesma estrutura, classificada como **fato**, não inferência) é considerada **suficiente para justificar a permanência como componente visual/estrutural** — não como componente com contrato de interação definido (ver item b).

### b) Ausência de evidência de interatividade — decidida (negativa)

**Não há evidência suficiente** — em código, Figma (fonte limitada a frames estáticos, sem dado de protótipo/interação, conforme já registrado na análise) ou documentação — de que `DsServiceCard` deva ser interativo. Consequência explícita: **não será implementado** comportamento de clique, navegação, `role`, `tabindex`, suporte a teclado ou eventos próprios (`@click`, `to`, `href`) enquanto essa evidência não existir. A existência da prop `clickable` **não é tratada como evidência** de um contrato funcional real — é reconhecida como um artefato de código sem função comprovada (achado da análise anterior), não como especificação a cumprir.

### c) Decisão sobre a prop `clickable` — pendente, registrada separadamente

**Ainda não decidida.** Antes de qualquer alteração em `clickable`, as alternativas identificadas na análise permanecem em aberto, sem escolha automática:
1. Manter `clickable` como API futura (documentando explicitamente que hoje não tem efeito funcional, só visual).
2. Remover `clickable` por não possuir contrato funcional correspondente.
3. Outra alternativa, se surgir evidência que a justifique.

Nenhuma dessas três foi escolhida por este registro.

### d) Correção dos tokens (48px hardcoded) — tratada como questão separada, também pendente de execução

A não conformidade de §19 (`ds.scss:434-435`, `.ds-service-card__icon { width: 48px; height: 48px; }` sem `var()`) é **independente** da decisão de interatividade (b) e da pendência sobre `clickable` (c). Fica registrada como correção pendente, a ser tratada em momento próprio, não condicionada à resolução de (c) — mas nenhuma implementação ocorre neste registro.

**Escopo explícito — o que este registro NÃO faz:**
- Não implementa nenhuma das quatro questões acima — nem interatividade, nem decisão sobre `clickable`, nem correção de tokens.
- Não resolve a pendência sobre a prop `clickable` (item c) — permanece aberta para decisão futura específica.
- Não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).
- Não decide o destino dos outros 3 órfãos do lote (`DsSectionHeader`, `DsContentCard`, `DsContentCardCompact`) — permanecem como registrado na auditoria em lote anterior.

**Fontes:** `frontend/src/components/ds/molecules/DsServiceCard.vue`; `frontend/src/components/ds/molecules/DsContentCardCompact.vue`; `frontend/src/components/ds/ds.scss`; `AUDITORIA-DS-FIGMA-01.md`; `DS-RECONSTRUCTION-INVENTORY-01`; análises de decisão em chat (diagnóstico inicial, aprofundamento sobre `clickable`, decisão do usuário).

---

## 56. Decisão registrada — `DsServiceCard`: remoção da prop `clickable` aprovada (2026-08-19)

**Contexto:** decisão do usuário sobre a pendência registrada em §55c, informada pela análise de decisão dedicada à prop `clickable`.

**Decisão:** aprovada a remoção da prop `clickable` de `DsServiceCard`, entre as alternativas A (manter como API futura) / B (remover por ausência de contrato funcional) / C (outra, se houvesse evidência) — **Opção B**.

**Evidências que sustentam a decisão, registradas explicitamente:**
- **Zero consumidores da prop** — nenhuma referência a `clickable` em `DsServiceCard` em todo o repositório, fora da própria definição do componente.
- **`pages/showcase.vue` não a utiliza** — as 2 instâncias de `DsServiceCard` no showcase (`showcase.vue:232-241`) passam só `icon`/`title`/`description`; nem a demonstração exercita a prop, em nenhum dos dois estados.
- **Nenhum contrato, teste, spec ou ADR depende dela** — `grep` em `test/`, `docs/`, `specs/`, `construction/` não retorna nenhuma referência; nenhum componente `Ds*` compõe `DsServiceCard` internamente.
- **Não existe evidência de requisito futuro** — nenhum ADR, spec ou decisão de produto (dentro do escopo do DS) menciona necessidade de `DsServiceCard` vir a ser clicável; a prop é um artefato do commit único de fundação (`8fd83f9`), sem histórico de revisão desde então.
- **`DsActionCard` já cobre o caso de interação genuína** (§26→§37, `PRESERVE`, 4 consumidores reais) — não há lacuna arquitetural que `clickable` em `DsServiceCard` estivesse reservando para preencher; se interatividade real for necessária no futuro, o padrão de referência já existe e está validado.
- **A remoção não altera a estrutura visual do componente** — layout de ícone/título/descrição/ações permanece idêntico. Precisão sobre o efeito visual: como `clickable` tinha `default: true` e nenhum consumidor a definia, a classe `.ds-service-card--clickable` era aplicada por padrão hoje; removê-la junto com a prop (por ser CSS exclusivamente associado, sem função por trás) faz o `cursor: pointer` e o hover (`ds.scss:418-428`) deixarem de aparecer no estado padrão — efeito **esperado e intencional** da decisão (eliminar uma affordance sem função real), não uma mudança de estrutura/layout.

**Separação explícita mantida:** esta decisão resolve **somente** a pendência da prop `clickable` (§55c). Não decide, não implementa e não altera a pendência de tokens (`48px` hardcoded, §55d) — permanece separada, tratada em correção própria posterior.

---

## 57. Implementação registrada — `DsServiceCard`: prop `clickable` removida (2026-08-19)

**Contexto:** implementação da decisão registrada em §56.

**Arquivos alterados:**
- `frontend/src/components/ds/molecules/DsServiceCard.vue` — removida a prop `clickable` (declaração, default e binding de classe no template).
- `frontend/src/components/ds/ds.scss` — removido o bloco `&--clickable` de `.ds-service-card` (`cursor`/`transition`/`hover`), por ser CSS exclusivamente associado à prop removida.

**Diff conceitual:**
```diff
 <template>
   <article
     class="ds-service-card"
-    :class="{ 'ds-service-card--clickable': clickable }"
   >
```
```diff
   withDefaults(
     defineProps<{
       title: string;
       description?: string;
-      icon: string;
-      clickable?: boolean;
+      icon: string;
     }>(),
-    {
-      clickable: true
-    }
   );
```
```diff
   box-shadow: var(--elevation-card);

-  &--clickable {
-    cursor: pointer;
-    transition:
-      box-shadow 0.15s ease,
-      border-color 0.15s ease;
-
-    &:hover {
-      border-color: var(--color-primary-200);
-      box-shadow: var(--shadow-md);
-    }
-  }
-
   &__icon {
```

**Escopo estritamente respeitado:**
- Nenhuma interação implementada (`@click`, `to`, `href`, `role`, `tabindex`, teclado) — decisão de §55b mantida.
- Estrutura visual (ícone, título, descrição, slot `actions`) inalterada.
- Hardcode de `48px` (`__icon`) **não corrigido** nesta alteração — pendência de tokens permanece separada (§55d), sem ação.
- Nenhum consumidor alterado — verificação confirmou, antes da implementação, que nenhum consumidor real referenciava a prop.
- Nenhum teste ou documentação alterados além deste registro.

**Validações executadas:** ver resumo apresentado ao usuário nesta mesma resposta (typecheck e testes pertinentes).

**Escopo explícito — o que este registro NÃO decide:** não resolve a pendência de tokens de `DsServiceCard` (§55d); não decide o destino dos outros 3 órfãos do lote (`DsSectionHeader`, `DsContentCard`, `DsContentCardCompact`); não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/components/ds/molecules/DsServiceCard.vue`; `frontend/src/components/ds/ds.scss`; decisão em §56; achado original na análise de decisão de `clickable`.

---

## 58. Saneamento de tokens registrado — `DsServiceCard`: `48px` substituído por `--spacing-3xl` (2026-08-19)

**Contexto:** correção da pendência de tokens registrada em §55d, mantida **explicitamente separada** da decisão sobre a prop `clickable` (§56–57, já resolvida). Esta correção não decide nem reabre nada sobre interatividade.

**Token disponível verificado antes da substituição:** `frontend/src/css/tokens/_spacing.scss` define `$spacing-12: 48px;`, exposto em `design-tokens.scss:138` como `--spacing-3xl: #{spacing.$spacing-12};` — **equivalência numérica exata** (48px = 48px), não aproximação. Nenhum token novo foi criado (regra 2 do pedido) — reaproveita a mesma escala primitiva de espaçamento já usada para dimensão de componente no precedente de `DsAvatar` (§25: `--spacing-14`/`--spacing-18` para `56px`/`72px`), já que o sistema de tokens não tem uma camada dedicada a "dimensão de componente" separada da escala de espaçamento (confirmado: nenhum token `--icon-size-*`/`--size-*` existe em `src/css/tokens/`).

**Substituição:** `frontend/src/components/ds/ds.scss`, `.ds-service-card__icon` — `width: 48px; height: 48px;` → `width: var(--spacing-3xl); height: var(--spacing-3xl);`.

**Verificação de equivalência:** compilação via `sass` confirmou `--spacing-3xl` resolvendo a `48px` no CSS gerado, e `.ds-service-card__icon` sem nenhum literal `48px` restante — substituição é bit-a-bit idêntica ao valor anterior, sem mudança visual.

**Escopo estritamente respeitado:**
- Só os `48px` de `DsServiceCard` foram tocados — o `48px`/`40px` de `.ds-content-card-compact__icon`/`__thumb` (outro componente, fora de escopo) permanecem intactos.
- Nenhum token novo criado.
- Nenhuma alteração de nome, estrutura ou API pública de `DsServiceCard`.
- Prop `clickable` não reintroduzida.
- Nenhum consumidor alterado.
- Nenhuma outra melhoria de CSS aproveitada — só a substituição do valor literal pelo token.

**Testes/validações executadas:** `yarn typecheck` (sem erros); `yarn vitest run` — 37 arquivos, 138 testes, todos passando; `yarn lint` — 3 erros pré-existentes de sempre, em arquivos não relacionados; compilação `sass` de `ds.scss` e `design-tokens.scss` confirmando o valor resolvido.

**Impacto visual esperado:** nenhum — `--spacing-3xl` resolve exatamente ao mesmo `48px` já renderizado antes.

**Escopo explícito — o que este registro NÃO decide:** não reabre a decisão de `clickable` (§56, já resolvida); não decide o destino dos outros 3 órfãos do lote (`DsSectionHeader`, `DsContentCard`, `DsContentCardCompact`) nem seus próprios hardcodes de token; não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15). Com este registro, `DsServiceCard` está **totalmente saneado** quanto a §19/§20 — permanece apenas como especificação visual/estrutural sem interatividade (§55/§56), sem nenhuma não conformidade técnica pendente.

**Fontes:** `frontend/src/css/tokens/_spacing.scss`; `frontend/src/css/tokens/design-tokens.scss`; `frontend/src/components/ds/ds.scss`; achado original em §50 (auditoria em lote) e §55d.

---

## 59. Lote de auditoria registrado — Órfãos finais do catálogo (2026-08-20)

**Contexto:** com `DsServiceCard` totalmente saneado (§55–58), este lote cobre os **3 últimos candidatos elegíveis de todo o catálogo** do `DS-RECONSTRUCTION-INVENTORY-01` — `DsContentCard`, `DsContentCardCompact`, `DsSectionHeader`. Todos os itens com consumidor real (átomos, moléculas, organismos) já foram diagnosticados em §22–§58, incluindo os 4 `RECONSIDER` com uso em produção. Restam apenas estes 3 órfãos (0 consumidores em produção, uso exclusivo em `pages/showcase.vue`), classificados como "RETIRE candidato" no inventário original. Modo de auditoria em lote solicitado pelo usuário: auditar os 3, não interromper no primeiro achado, não implementar, agrupar em quatro categorias (CONFORME / CORREÇÃO MECÂNICA / RECONSIDER / BLOQUEADO).

### `DsContentCard`

- §18 (fronteira pública): conforme — exportado em `index.ts`.
- §19 (tokens, sem hardcode): **não conforme** — `ds.scss`, `.ds-content-card__media { width: 120px; }`, valor literal sem `var()`. Restante do bloco (padding, radius, cores, tipografia) usa tokens corretamente.
- §20 (encapsulamento Quasar): conforme — não usa Quasar diretamente.
- §21 (gatilho de compatibilidade): não se aplica — sem consumidor real, reconstrução livre por definição do próprio §21.
- **Evidência Figma:** `AUDITORIA-DS-FIGMA-01:42` — classificada como **fato** ("padrão visual repetido": cards "Bem-estar 2025 - Colaborativa" em Home/Notícia). A evidência mais forte dos 3 itens deste lote.
- **Consumidores:** 0 em produção; `pages/showcase.vue` apenas.
- **Riscos:** hardcode isolado, mesmo padrão já corrigido em `DsAvatar` (§25), `DsActionCard` (§37) e `DsServiceCard` (§58) — correção de baixíssimo risco, sem consumidor a quebrar.
- **Decisão humana necessária:** sim, quanto ao destino do componente (manter enquanto Home/Notícia não é implementada vs. retirar). Não quanto ao hardcode, de natureza mecânica.

### `DsContentCardCompact`

- §18: conforme — exportado.
- §19: **não conforme** — `.ds-content-card-compact__icon { width: 40px; height: 40px; }` e `__thumb { width: 48px; height: 48px; }`, dois valores literais sem `var()`.
- §20: conforme — compõe `DsIcon` (já auditado, §23), sem Quasar direto.
- §21: não se aplica — sem consumidor real.
- **Achado adicional (já antecipado em §55, nunca fechado para este item):** prop `clickable` (`default: false`) controla só `cursor`/`hover` via CSS — mesmo padrão incompleto já identificado e resolvido em `DsServiceCard` (prop removida em §56–57). Aqui a pendência segue em aberto.
- **Evidência Figma:** `AUDITORIA-DS-FIGMA-01:43` — classificada como **inferência** ("variante compacta do mesmo padrão"), não fato direto — evidência mais fraca que `DsContentCard`.
- **Consumidores:** 0 em produção; `pages/showcase.vue` apenas.
- **Riscos:** dois hardcodes mecânicos (baixo risco); `clickable` sem contrato funcional é o mesmo risco de affordance-fantasma já resolvido em `DsServiceCard` — se não decidido, um consumidor futuro pode assumir que "clicável" funciona de fato.
- **Decisão humana necessária:** sim, duas frentes separadas — (a) destino do componente; (b) decisão sobre `clickable`, com as mesmas 3 opções já usadas em `DsServiceCard` (§55c/§56: manter como API futura / remover / outra).

### `DsSectionHeader`

- §18: conforme — exportado.
- §19: **conforme** — bloco inteiro (`gap`, `margin`, `font-size`, `color`) usa exclusivamente `var(--spacing-*)`, `var(--text-section-title-*)`, `var(--color-text-*)`. Nenhum hardcode encontrado — único item do lote sem não conformidade técnica.
- §20: conforme — sem Quasar direto.
- §21: não se aplica — sem consumidor real.
- **Evidência Figma:** `AUDITORIA-DS-FIGMA-01:47` — classificada como **RECONSIDER**, não REBUILD como os outros dois itens: "mesma ambiguidade... não dá para diferenciar `PageHeader` de `SectionHeader` só pela metadata". Estruturalmente quase idêntico a `DsPageHeader` (título + subtítulo + slot `actions`, já auditado CONFORME em §35) — a diferença é `h1` vs. `h2` e papel semântico (página vs. seção).
- **Consumidores:** 0 em produção; `pages/showcase.vue` apenas.
- **Riscos:** nenhum técnico. O risco é de catálogo — manter dois componentes quase idênticos sem uso real, ou fundir com `DsPageHeader` (ex. prop `level`/`as`), é decisão de modelagem, não de correção.
- **Decisão humana necessária:** sim — não há achado técnico a resolver; a única pendência é se o componente deve existir separado de `DsPageHeader`.

### Tabela consolidada

| Candidato | Classificação | Achado | Ação possível | Decisão humana necessária |
|---|---|---|---|---|
| `DsContentCard` | CORREÇÃO MECÂNICA (token) + RECONSIDER (destino) | `120px` hardcoded em `__media`, sem token | Corrigir hardcode agora (baixo risco); manter/retirar aguarda decisão de produto | Sim — só quanto ao destino |
| `DsContentCardCompact` | CORREÇÃO MECÂNICA (tokens) + RECONSIDER (destino + `clickable`) | `40px`/`48px` hardcoded em `__icon`/`__thumb`; prop `clickable` sem contrato funcional (mesmo padrão já resolvido em `DsServiceCard`) | Corrigir hardcodes agora; decidir `clickable` com as 3 opções já usadas em `DsServiceCard` (§55c) | Sim — destino do componente e decisão sobre `clickable` |
| `DsSectionHeader` | CONFORME (técnico) + RECONSIDER (catálogo) | Nenhuma não conformidade de código; sobreposição estrutural com `DsPageHeader` | Nenhuma correção necessária; decidir fusão vs. permanência separada | Sim — só decisão de catálogo, não de código |

### Prioridade de decisão — dependência comum identificada

Os 3 candidatos compartilham uma única decisão de produto que os desbloqueia simultaneamente: se as telas Home/Notícia/Serviços do Figma serão implementadas (achado crítico já registrado em `DS-RECONSTRUCTION-INVENTORY-01` §7, nunca resolvido). Essa decisão resolve o destino de `DsContentCard` e `DsContentCardCompact` (existem especificamente para essas telas) e informa `DsSectionHeader` indiretamente (a tela onde ele apareceria, "Fique por dentro", supriria um caso de uso real para diferenciá-lo de `DsPageHeader`).

Decisões que **não** dependem da anterior e podem ser resolvidas isoladamente:
- `clickable` em `DsContentCardCompact` — reaproveita o precedente já fechado de `DsServiceCard` (§56).
- Os 3 hardcodes de token — mecânicos, sem risco de API (zero consumidor real), mesmo padrão já aprovado 3 vezes (`DsAvatar` §25, `DsActionCard` §37, `DsServiceCard` §58).

### Decisão registrada — hardcodes de token aprovados como correção mecânica, execução pendente

O usuário aprovou os 3 hardcodes de token (`DsContentCard.__media` 120px; `DsContentCardCompact.__icon` 40px; `DsContentCardCompact.__thumb` 48px) como **CORREÇÃO MECÂNICA**, candidata a execução em lote. **Nenhuma implementação foi autorizada nesta etapa** — a aprovação é de classificação, não de execução; fica registrada como conjunto de correções pendentes de execução em lote futuro.

**Escopo explícito — o que este registro NÃO decide:** não decide o destino de catálogo dos 3 componentes (implementação das telas Home/Notícia/Serviços); não decide a prop `clickable` de `DsContentCardCompact`; não decide fusão/permanência separada de `DsSectionHeader` vs. `DsPageHeader`; não executa nenhuma correção de código, teste ou token; não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15). Com este registro, **todos os itens do catálogo do `DS-RECONSTRUCTION-INVENTORY-01` foram auditados ou diagnosticados pelo menos uma vez** (§22–§59).

**Fontes:** `frontend/src/components/ds/molecules/DsContentCard.vue`, `DsContentCardCompact.vue`, `DsSectionHeader.vue`; `frontend/src/components/ds/ds.scss`; `AUDITORIA-DS-FIGMA-01:42-47`; `DS-RECONSTRUCTION-INVENTORY-01` §2, §7; achado original de `clickable` em §55.

---

## 60. Implementação registrada — 3 hardcodes de token corrigidos (2026-08-20)

**Contexto:** execução dos 3 hardcodes de token aprovados como CORREÇÃO MECÂNICA em §59, sem tocar nenhuma das pendências de destino/catálogo/`clickable` deixadas explicitamente em aberto naquele registro.

**Tokens verificados antes da substituição:**
- **40px** e **48px** já tinham token exposto — `--spacing-2xl` (`$spacing-10`) e `--spacing-3xl` (`$spacing-12`), o mesmo `--spacing-3xl` já usado no precedente de `DsServiceCard` (§58). Nenhum token novo necessário para esses dois.
- **120px** não tinha primitivo correspondente na escala (`_spacing.scss` ia de `$spacing-24` = 96px a `$spacing-30`, inexistente). Seguindo o mesmo critério já aplicado em `DsAvatar` (§25) — estender a escala primitiva em vez de aceitar hardcode documentado —, foi adicionado `$spacing-30: 120px;`, respeitando a convenção já estabelecida `spacing-N = N×4px`.

**Implementação:**
- `frontend/src/css/tokens/_spacing.scss` — adicionado `$spacing-30: 120px;`.
- `frontend/src/css/tokens/design-tokens.scss` — exposto como `--spacing-30: #{spacing.$spacing-30};`, nomeado pelo número primitivo (mesmo padrão de `--spacing-14`/`--spacing-18`), sem inserir na escala semântica `xs..5xl`.
- `frontend/src/components/ds/ds.scss`:
  - `.ds-content-card__media` — `width: 120px` → `width: var(--spacing-30)`.
  - `.ds-content-card-compact__icon` — `width/height: 40px` → `var(--spacing-2xl)`.
  - `.ds-content-card-compact__thumb` — `width/height: 48px` → `var(--spacing-3xl)`.

**Escopo estritamente respeitado:**
- Nenhum valor visual alterado — os 3 `var()` resolvem exatamente aos pixels anteriores (confirmado por compilação `sass`, ver Testes).
- Nenhuma prop, slot, evento, classe ou export do barrel de `DsContentCard`/`DsContentCardCompact`/`DsSectionHeader` tocado.
- `DsSectionHeader` não foi alterado (já conforme, sem achado — §59).
- Prop `clickable` de `DsContentCardCompact` não tocada — pendência separada, ainda em aberto (§59).
- Nenhuma decisão de destino/catálogo destes 3 componentes implementada ou antecipada.
- Nenhum outro componente do DS alterado.

**Testes e validações executados:**
- `npx sass` sobre `design-tokens.scss` e `ds.scss` — compilação limpa; `--spacing-30: 120px`, `--spacing-2xl: 40px`, `--spacing-3xl: 48px` confirmados no CSS gerado; os 3 seletores (`__media`, `__icon`, `__thumb`) sem nenhum literal de pixel restante.
- `yarn typecheck` — sem erros.
- `yarn lint` — 3 erros pré-existentes reportados (`useSingularForm.ts`, `auth.service.spec.ts`, `useLoginPage.spec.ts`), mesma lista recorrente desde §36; nenhum atribuível a esta mudança.
- `yarn vitest run` — 37 arquivos, 138 testes, todos passando (nenhum teste unitário/E2E referencia os 3 componentes, confirmado em §59 — mudança sem cobertura direta, mas sem regressão em toda a suíte).

**Impacto visual esperado:** nenhum — os 3 componentes só aparecem em `pages/showcase.vue` hoje, e os valores resolvidos são bit-a-bit idênticos aos anteriores.

### Resultado consolidado do lote (§59 → §60)

| Candidato | Achado (§59) | Ação executada | Pendência remanescente |
|---|---|---|---|
| `DsContentCard` | `120px` hardcoded em `__media` | **Corrigido** — `var(--spacing-30)` (primitivo novo) | Destino de catálogo (Home/Notícia) — decisão humana, não técnica |
| `DsContentCardCompact` | `40px`/`48px` hardcoded em `__icon`/`__thumb` | **Corrigido** — `var(--spacing-2xl)` / `var(--spacing-3xl)` (tokens já existentes) | Destino de catálogo + decisão sobre prop `clickable` (§55c) — ambas não tocadas |
| `DsSectionHeader` | Nenhum (já CONFORME em §59) | Não aplicável — nenhuma correção existia a executar | Sobreposição de catálogo com `DsPageHeader` — decisão humana, não técnica |

**Escopo explícito — o que este registro NÃO decide:** não resolve o destino de catálogo dos 3 componentes (implementação de Home/Notícia/Serviços); não decide a prop `clickable` de `DsContentCardCompact`; não decide fusão/permanência separada de `DsSectionHeader` vs. `DsPageHeader`; não resolve nenhuma das pendências ainda abertas (itens 1, 3, 6, 7, 8 da seção 15).

**Fontes:** `frontend/src/css/tokens/_spacing.scss`; `frontend/src/css/tokens/design-tokens.scss`; `frontend/src/components/ds/ds.scss`; achado original e aprovação em §59; precedentes de extensão de escala em §25 e reuso de token em §58.

---

## 61. Encerramento registrado — 4 threads fechados sem decisão humana pendente (2026-08-20)

**Contexto:** lote de fechamento (2026-08-20, chat) revisou os 21 itens do `DS-RECONSTRUCTION-INVENTORY-01` — todos já tinham ao menos um diagnóstico registrado (§22–§60). Este registro fecha administrativamente os 4 threads cujo diagnóstico técnico já havia concluído sem pendência, mas cujo rótulo/status nunca havia sido formalmente encerrado. Nenhuma reabertura ocorreu (regra do lote: não reabrir CONFORME/decisões já registradas sem nova evidência) — nenhuma nova evidência surgiu para nenhum dos 4; o encerramento é puramente de status.

| Item | Rótulo original (`INVENTORY-01`) | Status final | Diagnóstico(s) que fundamentam o encerramento |
|---|---|---|---|
| `DsSelect` | `RECONSIDER` | **CONFORME — encerrado** | §47 — diagnóstico formal: nenhuma mudança de API necessária |
| `DsBreadcrumbs` | `RECONSIDER` | **CONFORME — encerrado** | §48 — diagnóstico formal: conforme §18–§20, nota de estilo não bloqueante |
| `DsDialog` (sub-item acessibilidade — botão de fechar) | `RECONSIDER` (item permanece `RECONSIDER` quanto à API ampla, ver backlog abaixo) | **CONFORME — encerrado** | §49 (achado) → §51 (decisão) → §52 (implementação; `yarn vitest` 7/7 e `SingularStatusDialog.spec.ts` 3/3 passando) |
| `DsFormCard` (sub-item inconsistência visual — separador) | `RECONSIDER` (item permanece `RECONSIDER` quanto à fusão com `DsCard`, ver backlog abaixo) | **CONFORME — encerrado** | §50 (achado) → §53 (decisão, Opção A) → §54 (implementação; 23/23 testes passando) |

**Natureza do encerramento — só o que está fechado:**
- `DsSelect` e `DsBreadcrumbs`: encerramento cobre o item inteiro — não há sub-issue remanescente para nenhum dos dois.
- `DsDialog`: encerramento cobre **apenas** o sub-item de acessibilidade. O item como um todo **permanece `RECONSIDER`** quanto à API/composição visual mais ampla — essa parte não é fechada por este registro (ver backlog, grupo Figma/evidência).
- `DsFormCard`: encerramento cobre **apenas** o sub-item da inconsistência visual do separador. O item como um todo **permanece `RECONSIDER`** quanto à possibilidade de fusão com `DsCard` (Opção B) — essa parte não é fechada por este registro (ver backlog, grupo decisão arquitetural).

**Escopo explícito — o que este registro NÃO faz:**
- Não altera código, teste ou estrutura — é registro documental.
- Não decide, resolve ou antecipa nenhuma das 4 pendências reais do backlog residual (roadmap de produto, evidência de Figma para modal, prop `clickable`, fusão `DsFormCard`/`DsCard`).
- Não resolve nenhuma das pendências ainda abertas da seção 15 (itens 1, 3, 5, 6, 7, 8).

**Fontes:** §47, §48, §49, §50, §51, §52, §53, §54; `DS-RECONSTRUCTION-INVENTORY-01` §1–§2; lote de fechamento (chat, 2026-08-20).

---

## 62. Decisão registrada — `DsContentCardCompact`: remoção da prop `clickable` aprovada (2026-08-20)

**Contexto:** decisão do usuário sobre a pendência registrada em §59 (grupo "decisão arquitetural" do backlog residual), informada por análise dedicada em chat que corrigiu um achado impreciso do próprio §59.

**Correção de evidência prévia ao registro da decisão:** §59 e o lote de fechamento subsequente afirmaram "0 consumidores (nem no showcase)" para a prop `clickable`. Isso estava **incorreto** — `frontend/src/pages/showcase.vue:223-229` usa `<DsContentCardCompact clickable ... />` explicitamente, sem `@click`/handler associado. Esta correção precede a decisão abaixo porque muda o raio de impacto da remoção (um arquivo consumidor a mais que em `DsServiceCard`, ainda que só no showcase, não em produção).

**Decisão:** aprovada a remoção da prop `clickable` de `DsContentCardCompact`, entre as alternativas A (manter como API futura, documentando que hoje só tem efeito visual) / B (remover por ausência de contrato funcional) / C (outra, se surgisse evidência) — **Opção B**, mesma escolha já tomada para `DsServiceCard` (§56).

**Evidências que sustentam a decisão:**
- **Nenhuma implementação funcional em nenhum dos dois componentes que já tiveram essa prop** (`DsServiceCard`, `DsContentCardCompact`) — só alterna `cursor: pointer`/hover via CSS; sem `@click`, `role`, `tabindex` ou suporte a teclado.
- **Uso explícito no showcase não é evidência de contrato de produto** — é uma demonstração visual isolada (`showcase.vue:228`), sem handler de clique, sem teste (unit ou E2E) que a exercite, sem consumidor real fora do showcase.
- **Mesmo commit de fundação (`8fd83f9`) sem revisão desde então** — mesma origem e mesma ausência de histórico de decisão de produto já constatada para `DsServiceCard` (§56).
- **`DsActionCard` já cobre o caso de interação genuína** (§26→§37, `PRESERVE`, 4 consumidores reais) — não há lacuna arquitetural que `clickable` estivesse reservando para preencher.
- **Diferença em relação a `DsServiceCard` reconhecida, mas não suficiente para mudar a decisão:** aqui a prop é opt-in (`default: false`, não `true`), e há um consumidor explícito (o showcase) — ainda assim, é o mesmo padrão de affordance sem função por trás; opt-in não torna a interação real.

**Escopo desta decisão:** cobre a remoção da prop, do binding de classe correspondente em `DsContentCardCompact.vue`, do bloco CSS `&--clickable` em `ds.scss`, e do atributo `clickable` na instância de `showcase.vue`. Não decide, reabre ou altera o destino de catálogo de `DsContentCardCompact` (item separado do backlog, grupo produto — §59/§61).

**Fontes:** achado original em §59 (com correção registrada acima); precedente de decisão idêntica em `DsServiceCard` (§55c, §56); análise dedicada em chat, 2026-08-20.

---

## 63. Implementação registrada — `DsContentCardCompact`: prop `clickable` removida (2026-08-20)

**Contexto:** implementação da decisão registrada em §62.

**Arquivos alterados:**
- `frontend/src/components/ds/molecules/DsContentCardCompact.vue` — removida a prop `clickable` (declaração, default e binding de classe no template).
- `frontend/src/components/ds/ds.scss` — removido o bloco `&--clickable` de `.ds-content-card-compact` (`cursor`/`transition`/`hover`), por ser CSS exclusivamente associado à prop removida.
- `frontend/src/pages/showcase.vue` — removido o atributo `clickable` da instância de `DsContentCardCompact` (linha 228), único consumidor da prop em todo o repositório.

**Escopo estritamente respeitado:**
- Nenhuma interação implementada (`@click`, `to`, `href`, `role`, `tabindex`, teclado) — decisão de §62 mantida (mesma disciplina de §55b para `DsServiceCard`).
- Estrutura visual (ícone/thumb, título, descrição, slot `trailing`) inalterada.
- Hardcodes de token de `DsContentCardCompact` (`__icon`/`__thumb`, já corrigidos em §60) não tocados.
- Nenhum outro componente alterado.
- Nenhum teste alterado — confirmado previamente que nenhum teste unitário ou E2E referencia `DsContentCardCompact` ou `clickable`.

**Contrato:** preservado quanto à API pública remanescente (`title`, `description`, `meta`, `icon`, `imageSrc`, `imageAlt`, slots `title`/`trailing`) — `clickable` deixa de existir, não é depreciada. Efeito visual: o hover/cursor-pointer que a demonstração do showcase exibia deixa de aparecer — efeito esperado e intencional (eliminar affordance sem função real), mesma leitura já registrada para `DsServiceCard` (§56).

**Testes/validações executadas:**
- `yarn typecheck` — sem erros.
- `yarn lint` — 3 erros pré-existentes reportados (`useSingularForm.ts`, `auth.service.spec.ts`, `useLoginPage.spec.ts`), mesma lista recorrente desde §36; nenhum atribuível a esta mudança.
- `yarn vitest run` — 37 arquivos, 138 testes, todos passando.

**Escopo explícito — o que este registro NÃO decide:** não decide o destino de catálogo de `DsContentCardCompact` (grupo produto, §59/§61 — Home/Notícia); não resolve nenhuma das pendências ainda abertas da seção 15 (itens 1, 3, 5, 6, 7, 8).

**Fontes:** `frontend/src/components/ds/molecules/DsContentCardCompact.vue`; `frontend/src/components/ds/ds.scss`; `frontend/src/pages/showcase.vue`; decisão em §62; precedente de implementação idêntica em `DsServiceCard` (§57).

---

## 64. Decisão registrada — `DsFormCard`: fusão com `DsCard` aprovada (Opção B, §13/§46) (2026-08-20)

**Contexto:** decisão do usuário sobre a pendência de fusão deixada explicitamente em aberto em §53 ("Opção B permanece apenas como possibilidade futura — não decidida, não descartada") e registrada no backlog residual de §59/§61 (grupo "decisão arquitetural").

**Recuperação do diagnóstico existente (sem nova auditoria ampla):** `DsFormCard` foi classificado `RECONSIDER` por sobreposição funcional quase total com `DsCard` (§45); §46 já apontava a fusão (Alternativa 5 do §13 — migração Feature a Feature) como o caminho mais provável; §50 encontrou a inconsistência visual do separador (corrigida via Opção A em §53–54) e, no mesmo achado, já registrava a diferença de nível de heading entre os dois componentes (`<div>` em `DsCard` vs. `<h2>` em `DsFormCard`) — constatação preexistente nesta sessão, não uma descoberta nova.

**Comparação final DsFormCard (pós-§54) vs. DsCard, feita antes da decisão:**

| | `DsCard` (título default) | `DsFormCard` (pós-§54) |
|---|---|---|
| Elemento do título | `<div class="ds-text-card-title">` | `<h2 class="ds-form-card__title">` |
| Margem do título | nenhuma | `margin: 0 0 var(--spacing-md)` |
| `subtitle`, `variant` | expostos | não expostos (nenhum consumidor de `DsFormCard` usava nenhum dos dois — confirmado em §50) |
| `__content` (wrapper do slot default) | não existe | `display:flex; flex-direction:column; gap: var(--spacing-md)` |

**Correção de análise feita durante esta decisão:** uma leitura inicial classificou `.ds-form-card__content` como "vestigial, sem CSS" — isso estava **incorreto** (falha de busca por string literal em SCSS aninhado); o bloco tem uma regra real de `flex`/`gap`. Verificação de impacto antes de decidir: os 3 consumidores reais (`EquipeForm.vue`, `SingularForm.vue`, `pages/primeiro-acesso/index.vue`) passam **exatamente um filho** no slot default — `gap` entre irmãos flex não tem efeito visual com um único filho. **Confirmado: zero impacto em produção** dessa diferença específica.

**Decisão:** aprovada a fusão (Opção B) — a diferença de heading (`<h2>` vs. `<div>`), já registrada como constatação preexistente em §50 e não como evidência nova, não foi tratada como incompatibilidade bloqueante nesta decisão. `DsFormCard` é retirado; os consumidores passam a usar `DsCard` diretamente, aceitando o título-padrão de `DsCard` (`<div class="ds-text-card-title">`) em vez de manter um `<h2>` próprio — sem introduzir prop nova ou variante de heading em `DsCard` (nenhuma nova abstração).

**Verificação de contrato de consumidores, feita antes da execução:** nenhum teste unitário ou E2E depende de `DsFormCard` ou do `<h2>` do formulário — `construction/17-frontend-e2e-behavior-policy.md` não menciona `DsFormCard`; as únicas asserções `getByRole("heading", ...)` em `test/e2e/equipe/`, `test/e2e/singular/` verificam o nome da entidade nas páginas de lista/detalhe (contrato de `DsPageHeader`, já fechado em §35), não o título do formulário.

**Escopo explícito — o que esta decisão NÃO faz:** não decide nenhuma das demais pendências do backlog residual (roadmap de produto, evidência de Figma para `DsDialog`); não introduz prop de nível de heading em `DsCard`; não resolve nenhuma das pendências ainda abertas da seção 15.

**Fontes:** §45, §46, §50, §53, §54 (diagnóstico e decisão anteriores); `construction/17-frontend-e2e-behavior-policy.md`; verificação de consumidores em chat, 2026-08-20.

---

## 65. Implementação registrada — `DsFormCard` retirado, consumidores migrados para `DsCard` (2026-08-20)

**Contexto:** implementação da decisão registrada em §64.

**Arquivos alterados:**
- `frontend/src/components/organization/equipe/EquipeForm.vue` — `<DsFormCard :title="title">` → `<DsCard :title="title">`; import atualizado.
- `frontend/src/components/organization/singular/SingularForm.vue` — mesma substituição; import atualizado.
- `frontend/src/pages/primeiro-acesso/index.vue` — mesma substituição no template; import atualizado; regra de estilo `:deep(.ds-form-card) { width: 100%; }` retargetada para `:deep(.ds-card)` (única instância de `DsCard` na página — confirmado antes da alteração), preservando a largura total já existente.
- `frontend/src/pages/showcase.vue` — mesma substituição; os 2 `DsInput` do demo (único consumidor com múltiplos filhos no slot default, ao contrário dos 3 reais) foram envolvidos em `<div class="column q-gutter-md">` para preservar o espaçamento visual que o `gap` de `.ds-form-card__content` fornecia — mesmo padrão de utilitário Quasar já usado em `DsPageHeader` (§35), local à página, sem tocar `DsCard`; import de `DsFormCard` removido.
- `frontend/src/components/ds/index.ts` — removido `export { default as DsFormCard } from "./organisms/DsFormCard.vue";`.
- `frontend/src/components/ds/organisms/DsFormCard.vue` — **arquivo removido**.
- `frontend/src/components/ds/ds.scss` — removido o bloco `.ds-form-card` (`__title`, `__content`) inteiro, órfão após a retirada do componente.
- `frontend/src/components/ds/molecules/DsCard.vue` — tipo da prop `title` alterado de `title?: string` para `title?: string | undefined`.

**Achado durante a execução (mecânico, não arquitetural):** `yarn typecheck` acusou incompatibilidade real sob `exactOptionalPropertyTypes` — `EquipeForm`/`SingularForm` têm prop própria `title?: string`, que o TypeScript infere como `string | undefined` ao ler `props.title`; passar esse valor via `:title="title"` para um `DsCard.title` tipado só como `string` opcional (sem `| undefined` explícito) é rejeitado pelo compilador, porque a propriedade passa a existir explicitamente com valor `undefined`, não a estar ausente. `DsFormCard` já declarava `title?: string | undefined` por esse motivo exato, mas nunca repassava `:title` a `DsCard` internamente — por isso o conflito nunca havia sido exercitado antes desta migração. Corrigido alinhando o tipo de `DsCard.title` ao mesmo padrão — mudança de tipo aditiva (amplia o que é aceito), não altera comportamento em runtime, não quebra nenhum consumidor existente.

**Escopo estritamente respeitado:**
- Nenhuma prop de nível de heading introduzida em `DsCard` — a decisão de §64 (aceitar `<div>`) foi seguida literalmente, sem contorná-la com uma abstração nova.
- Nenhum outro componente do DS alterado além de `DsCard.vue` (só o tipo da prop `title`).
- Nenhum teste alterado — confirmado em §64 que nenhum depende de `DsFormCard`.

**Impacto nos consumidores:**
- `EquipeForm.vue`, `SingularForm.vue`, `pages/primeiro-acesso/index.vue` — **zero impacto visual confirmado**: cada um passa um único filho no slot default (o `gap` do wrapper removido nunca teve efeito); o título passa de `<h2>` para `<div>` (mudança de heading aceita pela decisão de §64), mesmo tratamento visual de fonte/cor/peso (mesmos tokens `--text-card-title-*`).
- `pages/showcase.vue` — título muda de `<h2>` para `<div>` (mesma aceitação); espaçamento entre os 2 campos de demo preservado via wrapper local.

**Testes/validações executadas:**
- `npx sass` sobre `ds.scss` — compilação limpa, sem seletor `.ds-form-card` remanescente.
- `yarn typecheck` — sem erros (após a correção de tipo em `DsCard.vue`).
- `yarn lint` — 3 erros pré-existentes reportados (`useSingularForm.ts`, `auth.service.spec.ts`, `useLoginPage.spec.ts`), mesma lista recorrente desde §36; nenhum atribuível a esta mudança.
- `yarn vitest run` — 37 arquivos, 138 testes, todos passando.
- `grep` de confirmação — nenhuma referência restante a `DsFormCard`/`ds-form-card` em `frontend/src/` ou `frontend/test/`.

**Escopo explícito — o que este registro NÃO decide:** não resolve as demais pendências do backlog residual (roadmap de produto para `DsContentCard`/`DsContentCardCompact`/`DsSectionHeader`; evidência de Figma para `DsDialog`); não resolve nenhuma das pendências ainda abertas da seção 15 (itens 1, 3, 5, 6, 7, 8). Com este registro, o backlog residual da etapa de DS fica reduzido a 2 frentes (roadmap de produto; evidência de Figma para `DsDialog`).

**Fontes:** `frontend/src/components/organization/equipe/EquipeForm.vue`, `singular/SingularForm.vue`; `frontend/src/pages/primeiro-acesso/index.vue`, `showcase.vue`; `frontend/src/components/ds/index.ts`, `ds.scss`, `molecules/DsCard.vue`; decisão em §64; precedentes de padrão em §35 (utilitários Quasar) e §57 (retirada de componente).

---

## 66. Evidência registrada — `DsDialog`: ausência de frame de modal confirmada ao vivo no Figma (2026-08-20)

**Contexto:** tratamento da última pendência do backlog residual (§59/§61, grupo "Figma/evidência ausente") — `DsDialog` permanecia `RECONSIDER` quanto à API/composição visual mais ampla desde §51, por falta de frame de modal no Figma auditado (`AUDITORIA-DS-FIGMA-01`).

**Ação:** reconsulta ao vivo do Figma via MCP (`get_metadata`, arquivo `WHDHRAMXXslmxOIzK2dbJG`, node `0:1`), a mesma fonte usada em `AUDITORIA-DS-FIGMA-01`, relida agora por completo — não uma nova auditoria de componentes, apenas verificação direta e exaustiva da pendência específica de evidência.

**Resultado:** varridos todos os 8 frames do arquivo (`Login`, `Notícia`, `Perfil do Usuário`, `Areas`, `Home`, `Areas - Equipe`, `Areas - Arquivos e Downloads`, `Serviços`) e a totalidade de seus nós filhos. **Nenhum frame, grupo ou instância nomeado ou estruturado como modal/diálogo/overlay/popup/confirmação existe no arquivo** — inclusive `Areas - Equipe`, a tela mais próxima do caso de uso real de `DsDialog` (confirmação de status de Equipe/Singular), que contém apenas informação estática de contato, sem elemento de confirmação.

**Mudança de status da evidência:** de "não verificado" (§51: "nenhum frame de modal no Figma auditado") para **"verificado e confirmado ausente"** — a mesma conclusão já registrada, agora sustentada por leitura completa e atual do arquivo, não por recordação de auditoria anterior.

**Ressalva de limite da verificação:** `get_metadata` lê a árvore estática de nós, não dados de prototipagem/interação — um overlay definido só via protótipo do Figma (sem frame estático próprio) não apareceria nesta leitura, e não há ferramenta disponível nesta sessão para inspecionar especificamente reações/protótipo. Não há, porém, nenhuma evidência a favor da existência de tal overlay — é a mesma base de leitura já usada em toda a auditoria anterior (`AUDITORIA-DS-FIGMA-01`).

**O que isso muda, e o que não muda:** a pendência deixa de ser "aguardando verificação" e passa a ser "ausência confirmada, sem expectativa de que evidência surja desta fonte". Isso **não decide** a questão arquitetural em aberto — se `DsDialog` deve ser redesenhado sem Figma, mantido como está indefinidamente, ou aguardar uma eventual tela de Figma futura — apenas remove a incerteza sobre se a evidência existe e não foi encontrada.

**Escopo explícito — o que este registro NÃO decide:** não decide o destino da API/composição visual ampla de `DsDialog`; não resolve o backlog de produto (roadmap Home/Notícia/Serviços); não resolve nenhuma das pendências ainda abertas da seção 15.

**Fontes:** `mcp__plugin_figma_figma__get_metadata` (arquivo `WHDHRAMXXslmxOIzK2dbJG`, node `0:1`, 2026-08-20); `AUDITORIA-DS-FIGMA-01`; achado original em §51.

---

## 67. Encerramento registrado — `DsDialog`: `PRESERVE`, sem redesenho pendente (2026-08-20)

**Contexto:** decisão do usuário sobre a pendência tratada em §66. Com a ausência de evidência Figma confirmada (não mais apenas pendente de verificação), o usuário optou por encerrar o `RECONSIDER` em vez de mantê-lo em espera indefinida.

**Decisão:** `DsDialog` é reclassificado de `RECONSIDER` para `PRESERVE`. Não há redesenho de API ou composição visual pendente. O componente permanece como está — já `CONFORME` em §18–§21 (tokens, encapsulamento, fronteira pública) e com o achado de acessibilidade do botão de fechar já corrigido e validado (§49→§52→§61).

**Justificativa:** sem evidência de design que sustente uma API/composição diferente da atual, manter a pendência aberta indefinidamente não teria critério de encerramento — encerrar como `PRESERVE` reflete o estado real: nenhuma mudança está planejada, e nenhuma decisão de produto ou Figma futuro está sendo aguardada especificamente para este item (diferente de `DsContentCard`/`DsContentCardCompact`/`DsSectionHeader`, cujo destino depende de uma decisão de roadmap já identificada e ainda pendente).

**Reabertura futura:** se uma tela com modal/overlay for desenhada no Figma no futuro (ex. como parte do roadmap Home/Notícia/Serviços), essa nova evidência é motivo legítimo para reabrir a discussão — não é uma decisão irreversível, é o estado atual sem evidência.

**Escopo explícito — o que este registro NÃO decide:** não resolve o backlog de produto (roadmap Home/Notícia/Serviços) que ainda define o destino de `DsContentCard`, `DsContentCardCompact` e `DsSectionHeader`; não resolve nenhuma das pendências ainda abertas da seção 15 (itens 1, 3, 5, 6, 7, 8). Com este registro, o backlog residual da etapa de DS fica reduzido a **1 única frente**: o roadmap de produto.

**Fontes:** §66 (evidência); §49, §51, §52, §61 (histórico de `DsDialog`); decisão do usuário, 2026-08-20.

---

## 68. Checkpoint formal de encerramento — Reconstrução do Design System (2026-08-20)

**Contexto:** checkpoint solicitado pelo usuário ao final do ciclo de auditoria/reconstrução iniciado em §22. Verifica exclusivamente o estado já produzido nesta sessão (§22–§67) contra os 21 itens do `DS-RECONSTRUCTION-INVENTORY-01` — sem nova auditoria, sem reabertura de decisão encerrada, sem alteração de código.

### Verificação — correspondência inventário × registros

| # | Item | Categoria | Status final | Registro(s) |
|---|---|---|---|---|
| 1 | `DsButton` | Átomo | CONFORME | §26 |
| 2 | `DsIcon` | Átomo | CONFORME (validado, sem alteração) | §22–23 |
| 3 | `DsInput` | Átomo | CONFORME | §29 |
| 4 | `DsAvatar` | Átomo | CONFORME — implementado | §24–25 |
| 5 | `DsSelect` | Átomo | CONFORME — `RECONSIDER` encerrado | §42, §47, §61 |
| 6 | `DsBadge` | Átomo | CONFORME | §27–28 |
| 7 | `DsNavItem` | Molécula | CONFORME — implementado (a11y) | §30–31, §36 |
| 8 | `DsProfileSummary` | Molécula | CONFORME | §32 |
| 9 | `DsCard` | Molécula | CONFORME — tipo ajustado (§65) | §39, §65 |
| 10 | `DsPageHeader` | Molécula | CONFORME (nota de risco não bloqueante) | §35 |
| 11 | `DsBreadcrumbs` | Molécula | CONFORME — `RECONSIDER` encerrado | §43, §48, §61 |
| 12 | `DsSearchInput` | Molécula | CONFORME | §33 |
| 13 | `DsDialog` | Molécula | CONFORME/`PRESERVE` — implementado (a11y) + `RECONSIDER` amplo encerrado | §44, §49, §51–52, §61, §66–67 |
| 14 | `DsActionCard` | Molécula | CONFORME — implementado | §34, §37 |
| 15 | `DsContentCard` | Molécula (órfã) | CONFORME técnico — implementado (token); **destino pendente (produto)** | §59–60 |
| 16 | `DsContentCardCompact` | Molécula (órfã) | CONFORME técnico — implementado (tokens + remoção `clickable`); **destino pendente (produto)** | §59–60, §62–63 |
| 17 | `DsServiceCard` | Molécula (órfã) | CONFORME — permanência decidida, implementado | §55–58 |
| 18 | `DsSectionHeader` | Molécula (órfã) | CONFORME técnico; **destino pendente (produto)** | §59 |
| 19 | `DsDataTable` | Organismo | CONFORME | §40 |
| 20 | `DsFormCard` | Organismo | **Retirado** — fundido em `DsCard`, migração implementada | §45–46, §50, §53–54, §64–65 |
| 21 | `ds-notify` | Organismo | CONFORME | §41 |

**Correspondência verificada:** os 21 itens do inventário têm registro em §22–§67. Nenhum item sem diagnóstico. Barrel atual (`frontend/src/components/ds/index.ts`, relido nesta verificação) confirma **20 exports de componente** — os 21 originais menos `DsFormCard`, retirado — consistente com o histórico registrado.

**Decisões/implementações não refletidas no estado final:** nenhuma encontrada — toda decisão registrada (§26, §37, §51, §53, §56, §62, §64, §67...) tem implementação correspondente registrada em sequência (quando aplicável) ou justificativa explícita de não necessitar código (quando CONFORME sem achado).

### Correções mecânicas executadas e validadas

| Correção | Registro | Validação |
|---|---|---|
| `DsAvatar` — tokens de espaçamento estendidos | §25 | typecheck, lint, sem teste dedicado |
| `DsNavItem` — `aria-label` condicional (mini) | §36 | vitest 6/6, typecheck, lint |
| `DsActionCard` — token `--color-white` | §37 | typecheck, lint |
| `DsDialog` — `aria-label` no botão de fechar | §52 | vitest 7/7 + 3/3, typecheck, lint |
| `DsFormCard` — repasse correto ao slot `header` | §54 | typecheck, vitest 23/23, lint |
| `DsServiceCard` — prop `clickable` removida | §57 | typecheck, lint, vitest (resumo em chat) |
| `DsServiceCard` — token `--spacing-3xl` | §58 | typecheck, lint, vitest 37/37+138/138, sass |
| `DsContentCard`/`DsContentCardCompact` — 3 hardcodes de token | §60 | sass, typecheck, lint, vitest 37/37+138/138 |
| `DsContentCardCompact` — prop `clickable` removida | §63 | typecheck, lint, vitest 37/37+138/138 |
| `DsFormCard` → `DsCard` — fusão, retirada do componente | §65 | sass, typecheck (com correção de tipo), lint, vitest 37/37+138/138 |

Todas as 10 correções têm validação registrada; nenhuma pendente de execução.

### Decisões `RECONSIDER` efetivamente encerradas

`DsSelect` (§47/§61), `DsBreadcrumbs` (§48/§61), `DsDialog` — sub-item a11y (§52/§61) e API ampla (§66/§67), `DsFormCard` (fundido, §64/§65), `DsServiceCard` — permanência e `clickable` (§55–57). **Nenhum `RECONSIDER` arquitetural permanece aberto no catálogo.**

### Pendências verificadas como externas ao DS

`DsContentCard`, `DsContentCardCompact`, `DsSectionHeader` — os 3 únicos itens sem consumidor real de produção. Tecnicamente `CONFORME` (§59–60); seu **destino de catálogo** (manter, retirar, ou implementar as telas que os justificam) depende de uma decisão de **roadmap de produto** — implementar ou não Home/Notícia/Serviços — não de uma decisão de arquitetura do Design System. Esta é a única pendência que não se fecha dentro do escopo desta reconstrução.

### Classificação do estado final

## **DS RECONSTRUCTION — TECHNICALLY CLOSED**

Critério atendido: o único conjunto de itens não encerrado (`DsContentCard`, `DsContentCardCompact`, `DsSectionHeader`) depende exclusivamente de uma dependência externa de produto (roadmap), não de trabalho técnico, correção pendente ou decisão arquitetural em aberto.

### O que está encerrado

Os 21 itens do catálogo — 20 `CONFORME`/`PRESERVE` sem ação pendente, 1 retirado (`DsFormCard`, fundido em `DsCard`). Nenhuma correção mecânica pendente de execução. Nenhum `RECONSIDER` arquitetural aberto.

### O que foi implementado nesta sessão

10 correções de código (listadas na tabela acima), todas validadas (`sass`, `yarn typecheck`, `yarn lint`, `yarn vitest run`) sem regressão em nenhum ciclo.

### O que foi preservado

`DsButton`, `DsIcon`, `DsInput`, `DsBadge`, `DsProfileSummary`, `DsPageHeader`, `DsSearchInput`, `DsCard` (base), `DsDataTable`, `ds-notify`, `DsServiceCard` (estrutura visual, sem interatividade), `DsDialog` (estrutura ampla, sem redesenho) — conformes desde o diagnóstico original, sem necessidade de alteração de código.

### Pendência externa única

**Roadmap de produto — implementação das telas Home/Notícia/Serviços.** Não é uma pendência de Design System; é uma decisão de negócio que a Frontend Foundation não controla.

### Componentes cujo destino depende dessa pendência

`DsContentCard`, `DsContentCardCompact`, `DsSectionHeader` — tecnicamente saneados (sem hardcode, sem affordance sem função, sem violação de §18–§21), aguardando apenas a decisão de produto sobre as telas que os tornariam consumidos de fato. Nenhuma ação de Design System resolve essa pendência — só a evolução do roadmap.

### O que este checkpoint NÃO decide

Não resolve as 8 pendências arquiteturais mais amplas da seção 15 do `DS-RECONSTRUCTION-SCOPE-01` (papel do Figma como SSOT, taxonomia final além do já ratificado em §19, critérios de promoção Feature→DS, ferramenta de catálogo/regressão visual, ADR de identidade DS-01–DS-10) — essas permanecem abertas como trabalho de fundação de mais longo prazo, distinto do encerramento técnico do catálogo de componentes registrado aqui.

**Fontes:** §17–§67 integralmente; `DS-RECONSTRUCTION-INVENTORY-01`; `frontend/src/components/ds/index.ts` (relido nesta verificação).

---

## Referências

- [`docs/architecture/08-decision-records.md`](../08-decision-records.md)
- [`docs/technology/04-decision-log.md`](../../technology/04-decision-log.md) (DEC-004 — Quasar)
- [`docs/construction/frontend/00-frontend-foundation.md`](../../construction/frontend/00-frontend-foundation.md)
- [`docs/construction/frontend/02-design-system.md`](../../construction/frontend/02-design-system.md)
- [`construction/17-frontend-e2e-behavior-policy.md`](../../../construction/17-frontend-e2e-behavior-policy.md)
- [`frontend/src/components/ds/`](../../../frontend/src/components/ds/)
- [`frontend/src/css/tokens/`](../../../frontend/src/css/tokens/)
- [`AUDITORIA-DS-FIGMA-01`](./AUDITORIA-DS-FIGMA-01.md)
- [`DS-RECONSTRUCTION-INVENTORY-01`](../DS-RECONSTRUCTION-INVENTORY-01.md)
