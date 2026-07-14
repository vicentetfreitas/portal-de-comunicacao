# 01-schema.md

# Schema do Banco de Dados

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Sistema | Portal de Comunicação – Unimed Ceará |
| Banco de Dados | Oracle Database |
| Schema | UNMPORTCOM |
| Versão | 3.2 |
| Status | Approved |

---

# 1. Objetivo

Definir o escopo, a organização funcional e os limites do schema do Portal de Comunicação.

Este documento estabelece:

- identificação do schema;
- responsabilidades funcionais;
- organização dos domínios de dados;
- limites arquiteturais do schema;
- integração com sistemas corporativos.

As convenções de nomenclatura, políticas de administração do banco, versionamento, auditoria e demais diretrizes gerais estão documentadas em `docs/database/README.md`.

---

# 2. Identificação do Schema

## Nome Oficial

```text
UNMPORTCOM
```

Onde:

```text
UNM = Unimed
PORTCOM = Portal de Comunicação
```

O schema representa exclusivamente os dados pertencentes ao Portal de Comunicação.

---

# 3. Escopo

O schema UNMPORTCOM possui responsabilidade exclusiva sobre os dados utilizados pelo Portal de Comunicação.

Fazem parte do schema:

- tabelas;
- views;
- sequences;
- índices;
- constraints;
- triggers.

Não fazem parte deste schema:

- objetos pertencentes a outros sistemas corporativos;
- objetos temporários de integração;
- estruturas legadas de aplicações externas.

---

# 4. Organização Funcional

O schema está organizado em cinco domínios funcionais (**23 tabelas** no baseline oficial).

## Organização Corporativa

Responsável pela estrutura organizacional da instituição.

Principais entidades:

```text
FEDERACAO
SINGULAR
ENDERECO
CONTATO
AREA
EQUIPE
COLABORADOR
ONBOARDING_SOLICITACAO
```

---

## Gestão Documental

Responsável pelo gerenciamento do acervo documental.

Principais entidades:

```text
CATEGORIA_DOCUMENTAL
PASTA
DOCUMENTO
DOCUMENTO_VERSAO
COMPARTILHAMENTO
ARQUIVO_BINARIO
```

---

## Controle de Acesso

Responsável pelo controle de autenticação, autorização e auditoria.

Principais entidades:

```text
AUTH_SESSAO
PAPEL
PAPEL_ATRIBUICAO
PERMISSAO_PASTA
SOLICITACAO_PERMISSAO
REGISTRO_AUDITORIA
```

---

## Comunicação

Responsável pelos recursos de comunicação interna.

Principais entidades:

```text
COMUNICADO
NOTIFICACAO
```

---

## Configuração

Responsável pelos parâmetros globais do Portal.

Principais entidades:

```text
CONFIGURACAO_PORTAL
```

---

# 5. Organização do Schema

```text
UNMPORTCOM
│
├── ORGANIZACAO
│   ├── FEDERACAO
│   ├── SINGULAR
│   ├── AREA
│   ├── EQUIPE
│   ├── COLABORADOR
│   └── ONBOARDING_SOLICITACAO
│
├── DOCUMENTOS
│   ├── CATEGORIA_DOCUMENTAL
│   ├── PASTA
│   ├── DOCUMENTO
│   ├── DOCUMENTO_VERSAO
│   ├── COMPARTILHAMENTO
│   └── ARQUIVO_BINARIO
│
├── SEGURANCA
│   ├── AUTH_SESSAO
│   ├── PAPEL
│   ├── PAPEL_ATRIBUICAO
│   ├── PERMISSAO_PASTA
│   ├── SOLICITACAO_PERMISSAO
│   └── REGISTRO_AUDITORIA
│
├── COMUNICACAO
│   ├── COMUNICADO
│   └── NOTIFICACAO
│
└── CONFIGURACAO
    └── CONFIGURACAO_PORTAL
```

---

# 6. Limites do Schema

O schema possui as seguintes restrições arquiteturais:

- armazenar exclusivamente dados pertencentes ao Portal de Comunicação;
- manter independência em relação aos demais sistemas corporativos;
- evitar compartilhamento direto de tabelas entre aplicações;
- preservar o isolamento funcional dos domínios de negócio.

---

# 7. Integração com Outros Sistemas

O Portal de Comunicação poderá integrar-se com outros sistemas corporativos da Unimed Ceará.

Essas integrações deverão ocorrer por mecanismos próprios da aplicação, como APIs, serviços, filas ou consultas controladas.

O compartilhamento direto de estruturas físicas entre sistemas não faz parte da arquitetura proposta.

---

# 8. Artefatos Relacionados

Este documento serve como base para os seguintes artefatos da camada de modelagem:

```text
02-conceptual-model.md
02-logical-model.md
03-physical-model.md
04-entity-catalog.md
05-decisions-and-risks.md
```

A evolução destes documentos deverá permanecer consistente com o escopo definido para o schema UNMPORTCOM e com o baseline em `docs/database/ddl/`.