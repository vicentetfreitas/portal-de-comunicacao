# MVP Definition

## Objetivo

Definir o escopo mínimo viável (MVP) do Portal de Comunicação.

**Fonte normativa:** `docs/audit/10-mvp-consolidation-audit.md`  
**Origem arquitetural:** `docs/solution-design/10-delivery-roadmap.md` — Etapas 1–5  
**Data de reconciliação:** 2026-06-22

---

# Escopo

Esta documentação cobre:

* Funcionalidades MVP (Etapas 1–5)
* Funcionalidades fora do MVP
* Critérios de aceite
* Critérios de sucesso
* Restrições
* Dependências

---

# Objetivos do MVP

O MVP deve:

* Materializar o núcleo TO-BE documentado nas Etapas 1–5.
* Ser utilizável em produção após migração operacional (Etapa 5).
* Possuir operação monitorada (observabilidade base — Etapa 1).
* Permitir evolução incremental.

---

# Princípios

O MVP deve priorizar:

1. Valor para o negócio.
2. Simplicidade.
3. Estabilidade.
4. Tempo de entrega.
5. Aprendizado.

---

# Escopo Funcional

## Etapa 1 — Fundação da Plataforma

* Ambientes Local/Dev
* Persistência segregada
* Reverse Proxy HTTPS
* Esqueletos Backend/Frontend/CMS
* Observabilidade base (logs, health checks)

---

## Etapa 2 — Núcleo Organizacional

### Organização Corporativa

* Gestão de singulares, áreas, equipes, colaboradores e vínculos

### Controle de Acesso

* Login corporativo via Zimbra
* Logout
* Controle de sessão
* Controle de acesso por papel e escopo
* Auditoria inicial (registro de ações, histórico operacional)

---

## Etapa 3 — Gestão Documental

* Publicação e consulta documental
* Organização em pastas
* Visibilidade e compartilhamento
* Upload e download autorizado
* Separação metadado/binário
* Busca documental autorizada

---

## Etapa 4 — Comunicação Interna

### Notificações (obrigatório)

* Notificações in-app unificadas

### Gestão de Comunicados (PARCIAL — OQ-004)

* Criar comunicado
* Editar comunicado
* Consultar comunicado
* Excluir comunicado

---

## Etapa 5 — Migração Operacional

* Migração dados organizacionais, documentais e permissões
* Reconciliação metadado/binário
* Frontend → Backend principal
* Redução gradual de APIs CMS

---

# Não Incluído

Itens previstos para releases futuras ou pós-MVP:

* Gestão de Campanhas
* Gestão de Mensagens
* Painel Operacional / Métricas Administrativas de negócio
* Descomissionamento (Etapa 6)
* Aplicativo Mobile
* Multi-idioma
* IA Generativa
* Automações avançadas
* Segmentação avançada
* Analytics avançado
* Integrações não críticas

---

# Requisitos Não Funcionais

## Performance

95% das requisições:

```text
< 500ms
```

---

## Disponibilidade

```text
99.5%
```

---

## Segurança

* OAuth2
* JWT
* Auditoria
* LGPD

---

# Dependências

## Backend

Todos os módulos MVP das Etapas 1–5 implementados.

---

## Frontend

Fluxos críticos das Etapas 2–5 implementados.

---

## Infraestrutura

* Docker
* CI/CD
* Observabilidade
* Produção

---

# Critérios de Aceite

O MVP será considerado pronto quando:

* Todos os fluxos críticos das Etapas 1–5 estiverem operacionais.
* Não existirem defeitos críticos.
* Testes aprovados.
* Segurança validada.
* Observabilidade ativa.

---

# Critérios de Sucesso

Após implantação:

* Usuários conseguem operar sem suporte contínuo.
* Fluxos principais executados com sucesso.
* Disponibilidade dentro do SLA.
* Erro operacional abaixo do limite definido.

---

# Riscos

## Técnicos

* Integrações externas.
* Performance.
* Migração de dados.

---

## Operacionais

* Treinamento.
* Adoção dos usuários.
* Processos internos.

---

# Go/No-Go

O MVP somente poderá entrar em produção quando:

* Critérios de aceite atendidos.
* Aprovações concluídas.
* Plano de rollback aprovado.
* Equipe de suporte preparada.
