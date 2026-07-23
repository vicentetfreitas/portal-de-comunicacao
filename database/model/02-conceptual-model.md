# Modelo Conceitual de Dados

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Schema | UNMPORTCOM |
| Versão | 1.5 |
| Status | Approved |
| Baseline DDL | `database/ddl/` (2026-07-10) |
| Última atualização | 2026-07-10 |

---

## 1. Objetivo

Representar as **entidades de negócio** e seus relacionamentos no domínio do Portal de Comunicação, independentemente da implementação física Oracle.

Este documento é a camada superior da documentação de dados. Detalhamento lógico: `02-logical-model.md`. Implementação física: `03-physical-model.md`.

---

## 2. Hierarquia de modelos

```text
02-conceptual-model.md   ← este documento (negócio)
        ↓
02-logical-model.md      (entidades e cardinalidades)
        ↓
03-physical-model.md     (tabelas Oracle)
        ↓
ddl/                     (baseline executável)
```

---

## 3. Domínios de negócio

| Domínio | Responsabilidade |
|---------|------------------|
| Organização Corporativa | Estrutura institucional e colaboradores |
| Gestão Documental | Acervo, versionamento e compartilhamento |
| Controle de Acesso | Autenticação, autorização e auditoria |
| Comunicação | Comunicados e notificações |
| Configuração | Parâmetros globais do portal |

---

## 4. Organização Corporativa

### Entidades

| Conceito | Descrição |
|----------|-----------|
| **Federação** | Instituição administradora do portal |
| **Singular** | Unidade cooperativa vinculada à federação |
| **Endereço** | Localização física da federação ou singular |
| **Contato** | Canal de comunicação da federação, singular, área, equipe ou colaborador |
| **Área** | Unidade organizacional hierárquica |
| **Equipe** | Grupo de trabalho dentro de uma área |
| **Colaborador** | Usuário interno autenticado no portal |
| **Solicitação de Onboarding** | Pedido de cadastro e ativação de acesso |

### Relacionamentos principais

```text
Federação (organização raiz — DEC-DB-021)
    ↓
Singulares
    ↓
Áreas  (podem pertencer à Federação com COD_SINGULAR nulo ou a uma Singular)
    ↓
Equipes
    ↓
Colaboradores (COD_FEDERACAO obrigatório; vínculos inferiores conforme contexto)
```

```text
Federação 1 ── N Singular
Federação 1 ── N Endereço
Federação 1 ── N Contato
Singular   1 ── N Endereço
Singular   1 ── N Contato
Área       1 ── N Contato
Equipe     1 ── N Contato
Colaborador 1 ── N Contato
Singular   1 ── N Área
Área       1 ── N Equipe
Área       0..1 ── 1 Colaborador (gestor)
Equipe     0..1 ── 1 Colaborador (líder)
Colaborador 0..1 ── 1 Colaborador (gestor direto)
Colaborador N ── 1 Federação (obrigatório)
Colaborador N ── 0..1 Singular / Área / Equipe (conforme contexto)
Solicitação de Onboarding N ── 1 Colaborador
```

### Regras conceituais

- Todo colaborador pertence obrigatoriamente à **Federação** (organização raiz). Singular, área e equipe são vínculos explícitos na hierarquia — não inferidos pela aplicação (DEC-DB-021).
- **Federação** armazena apenas identidade institucional estável (nome, sigla, códigos oficiais, site, descrição). Não é tabela de configuração operacional do portal.
- **Singular** armazena apenas dados da cooperativa filiada (nome, sigla, código Unimed); não duplica informações da federação.
- **Endereço** e **Contato** eliminam duplicação de localização e canais de comunicação entre federação e singulares (DEC-DB-013).
- Todos os meios de comunicação usam **Contato** — inclusive canais do colaborador (telefone, ramal, WhatsApp, e-mails adicionais). Não criar entidades `EMAIL`, `TELEFONE`, `WHATSAPP` (DEC-DB-014/016).
- **Colaborador** armazena apenas atributos intrínsecos ao perfil (biografia, datas pessoais/profissionais, vínculo organizacional, gestor direto, identidade Zimbra).
- **Área** representa um único nível organizacional na Federação ou na Singular; o detalhamento operacional é feito por **Equipes** (DEC-DB-022).
- Gestores/líderes organizacionais: `AREA.COD_GESTOR`, `EQUIPE.COD_LIDER` e `COLABORADOR.COD_GESTOR` referenciam **Colaborador** — sem tabelas de hierarquia entre áreas (DEC-DB-015/016, DEC-DB-022).
- Redes sociais e diretoria executiva permanecem fora do modelo relacional nesta versão (WordPress ou evolução futura).
- Branding do portal (logo, favicon) permanece em **Configuração do Portal**, não na Federação.
- A identidade do colaborador é composta por identificador interno, e-mail de login (`DES_EMAIL`, FT-AUTH) e identificador Zimbra (quando autenticado via IdP corporativo). Canais de comunicação adicionais permanecem em **Contato** (DEC-DB-016). **Não há número de matrícula corporativa na versão atual** (DEC-DB-011).
- O escopo de atuação no portal é definido por **atribuição de papéis**, não pela posição na hierarquia.

---

## 5. Autenticação e Sessão (FT-AUTH)

### Entidades

| Conceito | Descrição |
|----------|-----------|
| **Sessão de Autenticação** | Continuidade da autenticação após login; controla Refresh Token e revogação |
| **Identidade Zimbra** | Identificador externo do colaborador no provedor de identidade |

### Relacionamentos

```text
Colaborador 1 ── N Sessão de Autenticação
```

### Regras conceituais

- Uma sessão pertence a um único colaborador.
- A sessão armazena o estado do **Refresh Token** (hash), não o Access Token (JWT).
- Sessão pode ser encerrada por logout, expiração, limite de dispositivos ou revogação administrativa.
- Credenciais (senha) **não** são persistidas pelo portal.

---

## 6. Gestão Documental

### Entidades

| Conceito | Descrição |
|----------|-----------|
| **Categoria Documental** | Classificação de documentos |
| **Pasta** | Estrutura hierárquica de armazenamento |
| **Documento** | Metadados do documento corporativo |
| **Versão de Documento** | Histórico imutável de alterações |
| **Arquivo Binário** | Referência a conteúdo em armazenamento externo |
| **Compartilhamento** | Concessão de acesso a recurso (documento, pasta, comunicado) |

### Regras conceituais

- Documento nunca é sobrescrito — toda alteração gera nova versão.
- Apenas uma versão é considerada atual.
- Arquivos binários permanecem fora do banco relacional.

---

## 7. Autorização e Auditoria

### Entidades

| Conceito | Descrição |
|----------|-----------|
| **Papel** | Perfil de acesso do portal |
| **Atribuição de Papel** | Vínculo colaborador ↔ papel com escopo organizacional |
| **Permissão de Pasta** | Permissão explícita sobre pasta |
| **Solicitação de Permissão** | Pedido de acesso a recurso |
| **Registro de Auditoria** | Trilha de operações auditáveis |

### Regras conceituais

- Permissões são definidas pelo portal, não pelo Zimbra.
- Compartilhamento utiliza modelo polimórfico (múltiplos tipos de recurso e destinatário).
- Auditoria centralizada com retenção mínima de cinco anos.

---

## 8. Comunicação e Configuração

| Conceito | Descrição |
|----------|-----------|
| **Comunicado** | Publicação institucional |
| **Notificação** | Alerta gerado por evento do portal |
| **Configuração do Portal** | Parâmetros por federação (relação 1:1) |

---

## 9. Mapeamento conceitual → físico

| Conceito | Tabela física |
|----------|---------------|
| Federação | FEDERACAO |
| Singular | SINGULAR |
| Área | AREA |
| Equipe | EQUIPE |
| Colaborador | COLABORADOR |
| Identidade Zimbra | COLABORADOR.ID_ZIMBRA |
| Sessão de Autenticação | AUTH_SESSAO |
| Solicitação de Onboarding | ONBOARDING_SOLICITACAO |
| Papel | PAPEL |
| Atribuição de Papel | PAPEL_ATRIBUICAO |
| Permissão de Pasta | PERMISSAO_PASTA |
| Solicitação de Permissão | SOLICITACAO_PERMISSAO |
| Registro de Auditoria | REGISTRO_AUDITORIA |
| Categoria Documental | CATEGORIA_DOCUMENTAL |
| Pasta | PASTA |
| Documento | DOCUMENTO |
| Versão de Documento | DOCUMENTO_VERSAO |
| Arquivo Binário | ARQUIVO_BINARIO |
| Compartilhamento | COMPARTILHAMENTO |
| Comunicado | COMUNICADO |
| Notificação | NOTIFICACAO |
| Configuração do Portal | CONFIGURACAO_PORTAL |

---

## 10. Artefatos relacionados

- `01-schema.md` — escopo do schema
- `02-logical-model.md` — modelo lógico
- `03-physical-model.md` — modelo físico
- `04-entity-catalog.md` — catálogo de entidades
