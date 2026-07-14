# Domain Summary

## Objetivo

Consolidar as informações essenciais da camada Domain em um único documento de consulta rápida para a camada Architecture.

Este documento não substitui os artefatos originais.

Seu objetivo é reduzir consumo de tokens durante análises arquiteturais.

---

# Nível de Maturidade

Médio-Alto

A camada Domain possui estrutura suficientemente estável para suportar modelagem arquitetural.

Questões abertas permanecem registradas em:

* 10-open-questions.md

---

# Bounded Contexts

## Organização Corporativa

Responsável por:

* colaboradores
* áreas
* equipes
* singulares
* vínculos organizacionais

---

## Gestão Documental

Responsável por:

* documentos
* pastas
* armazenamento
* compartilhamento
* publicação

---

## Controle de Acesso

Responsável por:

* permissões
* solicitações de acesso
* concessões
* auditoria

---

## Comunicação Interna

Responsável por:

* notificações
* comunicados
* central de colaboração
* fique por dentro

Status: contexto com menor nível de confiança.

---

# Aggregates Principais

## Organização Corporativa

Entidades centrais:

* Colaborador
* Área
* Equipe
* Singular

---

## Gestão Documental

Entidades centrais:

* Documento
* Pasta

---

## Controle de Acesso

Entidades centrais:

* Permissão
* Solicitação de Permissão

---

# Eventos Principais

* Colaborador Integrado
* Documento Publicado
* Compartilhamento Definido
* Solicitação de Permissão Registrada
* Permissão Concedida
* Notificação Emitida

---

# Regras Críticas

## BR-011

Colaborador deve ser integrado antes de acessar recursos organizacionais.

---

## BR-019

Documento possui visibilidade definida.

---

## BR-020

Compartilhamento define audiência autorizada.

---

## BR-029 até BR-032

Solicitação e concessão de permissões.

---

# Dependências de Negócio

Fluxo principal:

Organização Corporativa
→ Gestão Documental
→ Controle de Acesso
→ Comunicação Interna

---

# Questões Abertas Prioridade Alta

* OQ-001
* OQ-002
* OQ-003
* OQ-004
* OQ-005
* OQ-006
* OQ-007
* OQ-016
* OQ-017

Consultar 10-open-questions.md para detalhes.

---

# Uso Permitido

Este documento pode ser utilizado como fonte primária pela camada Architecture.

Consultar os documentos completos apenas quando houver necessidade de aprofundamento.
