# Feature Specification

| Campo | Valor |
|--------|--------|
| Template | CRUD Feature (adaptado — catálogo de links, escopo mínimo até decisão de produto) |
| Versão | 1.1 |
| Status | DRAFT |
| Owner | Engineering Framework |

---

# Informações da Feature

| Campo | Valor |
|--------|--------|
| Feature ID | FT-SERVICOS |
| Feature | Serviços |
| Domínio | SERVICOS |
| Tipo | Nova capacidade — sem precedente no legado nem no backend atual |
| Status | DRAFT |

---

# Objetivo

Permitir que o colaborador autenticado acesse uma lista de serviços/sistemas externos institucionais (ex. Service Desk, Zimbra, CapacitaCoop, Faculdade Unimed).

**Achado relevante:** esta é a **única** das 7 telas sem nenhum correspondente no mapeamento do frontend legado (`docs/discovery/frontend-feature-mapping.md`) — não há `FT-SERVICOS` nem equivalente na lista de 15 Features daquele inventário. É produto genuinamente novo em relação ao que existia.

**Fonte de evidência visual:** `AUDITORIA-DS-FIGMA-01.md` — frame `Serviços` (node `97:91`): 4 linhas — "Service Desk", "Zimbra", "CapacitaCoop", "Faculdade Unimed" — cada uma com ícone/imagem e título, sem descrição adicional visível nem indicação de link/URL nos metadados inspecionados.

---

# Escopo

## Incluído (proposto — depende de decisão de produto)

- Listagem dos serviços/sistemas institucionais disponíveis.
- Navegação para cada serviço (provavelmente link externo — ver decisão pendente).

## Fora do Escopo

- Autenticação federada/SSO com os sistemas listados — não evidenciado.
- Gestão administrativa da lista (se vier a ser admin-manageable) — decisão pendente, ver abaixo.

---

# Atores

| Ator | Descrição |
|------|-----------|
| Colaborador autenticado | Consulta a lista de serviços |

---

# Requisitos Funcionais (propostos — dependem de decisão de produto)

## RF-SERVICOS-001 — Listar serviços disponíveis

| Campo | Valor |
|--------|--------|
| Identificador | RF-SERVICOS-001 |
| Descrição | O sistema deve exibir a lista de serviços/sistemas institucionais disponíveis ao colaborador. |

## RF-SERVICOS-002 — Acessar serviço

| Campo | Valor |
|--------|--------|
| Identificador | RF-SERVICOS-002 |
| Descrição | O sistema deve permitir que o colaborador acesse o serviço selecionado (mecanismo exato — decisão pendente). |

---

# Decisão de produto pendente

Diferente de `FT-AREA-COLABORADOR`/`FT-PERFIL` (que reaproveitam backend real) e mais próximo de `FT-DOCUMENTO` em grau de incerteza — mas de escopo potencialmente muito menor, **se** a resposta à primeira pergunta for "estático":

1. **Modelo de dados:** a lista de 4 serviços é conteúdo **estático** (config no frontend, sem backend) ou precisa ser **administrável** (cadastro, edição, ativação de serviços, análogo às demais entidades do Portal)? O Figma auditado não mostra nenhuma tela de administração de Serviços — isso é evidência a favor de "estático", mas não decide a questão sozinho.
2. **Navegação:** cada item abre um link externo (URL configurável), ou existe alguma integração de autenticação (SSO) com o sistema de destino? Nenhuma URL aparece nos metadados do Figma auditado.
3. **Autorização:** todos os colaboradores veem os mesmos 4 serviços, ou a lista varia por Área/Singular/papel? Não evidenciado.

Se a resposta a (1) for "estático" e a (2) for "link externo simples", esta é a Feature de **menor esforço de implementação** das 7 — mas isso não é decidido por esta especificação.

---

# Dependências

| Dependência | Tipo | Observação |
|---|---|---|
| Nenhuma dependência de backend de outra Feature | — | Depende só da decisão de modelagem acima |
| Design System (`ds/`) | Não bloqueante | `DsCard`/`DsActionCard`/lista já `CONFORME`, cobrem o padrão visual observado |

---

# Fontes

`docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`; `docs/discovery/frontend-feature-mapping.md` (ausência confirmada por busca).
