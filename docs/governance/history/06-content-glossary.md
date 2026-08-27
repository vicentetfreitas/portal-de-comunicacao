# Glossário do Domínio de Conteúdo

| Campo | Valor |
|------|-------|
| Categoria documental | Archive |
| Status | Obsoleto — linguagem ubíqua do domínio de conteúdo editorial, hoje responsabilidade do WordPress (`DEC-CMS-001`) |
| Motivo | Nenhum termo confirmado em uso fora desta pasta; decisão D3, Plano W2, 2026-08-20 |
| Origem | Movido de `specs/domain/06-content-glossary.md` em 2026-08-20 |

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Domain |
| Artefato | 06-content-glossary.md |
| Status | Draft |
| Versão | 1.0 |
| Dependências | 01-content-model.md, 02-content-taxonomy.md, 03-content-lifecycle.md, 04-publication-model.md, 05-permission-model.md |
| Última atualização | 2026-07-08 |

---

# Objetivo

Este documento estabelece a **Linguagem Ubíqua** (Ubiquitous Language) do domínio de conteúdo do Portal de Comunicação.

Seu propósito é garantir que todas as equipes utilizem os mesmos conceitos, eliminando ambiguidades entre negócio, desenvolvimento, arquitetura, UX, banco de dados e documentação.

Este glossário é a referência oficial para nomenclatura do domínio.

---

# Escopo

Este documento aplica-se a:

- documentação;
- especificações;
- APIs;
- banco de dados;
- backend;
- frontend;
- testes;
- integrações;
- documentação técnica.

Sempre que houver conflito terminológico, este documento prevalece.

---

# Princípios

## GL-001

Cada conceito possui exatamente um significado oficial.

---

## GL-002

Um termo nunca deverá representar dois conceitos distintos.

---

## GL-003

Conceitos diferentes nunca deverão utilizar o mesmo nome.

---

## GL-004

A nomenclatura do domínio é independente da implementação.

---

## GL-005

Mudanças neste documento deverão refletir em todos os artefatos da camada.

---

# Conceitos Fundamentais

## Conteúdo

### Definição

Representa qualquer informação institucional administrada pelo Portal de Comunicação.

É o conceito central do domínio.

### Características

- possui identidade;
- possui ciclo de vida;
- possui auditoria;
- pode ser publicado;
- pode possuir arquivos;
- pode possuir categorias;
- pode possuir tags;
- pode possuir permissões.

### Não significa

- PDF;
- arquivo físico;
- página HTML;
- registro de banco.

---

## Documento

Conteúdo institucional de caráter permanente ou normativo.

Exemplos:

- políticas;
- procedimentos;
- manuais;
- regulamentos.

---

## Comunicado

Conteúdo oficial emitido pela organização para comunicar informações institucionais.

---

## Notícia

Conteúdo informativo destinado à divulgação de acontecimentos.

---

## Aviso

Conteúdo breve destinado a alertar usuários sobre situações temporárias.

---

## Postagem

Conteúdo institucional de comunicação mais livre e dinâmica.

---

# Organização

## Pasta

Estrutura lógica utilizada para organizar conteúdos.

Não representa um conteúdo.

---

## Categoria

Classificação institucional padronizada.

Exemplos:

- RH
- Jurídico
- Financeiro

---

## Tag

Classificação livre utilizada para facilitar pesquisa e relacionamento entre conteúdos.

---

# Arquivos

## Arquivo

Objeto físico armazenado pelo Portal.

Exemplos:

- PDF
- DOCX
- XLSX
- PNG
- MP4

Arquivo não representa o conteúdo.

---

## Anexo

Arquivo associado a um conteúdo.

Todo anexo é um arquivo.

Nem todo arquivo é necessariamente um anexo.

---

# Publicação

## Publicação

Ato de disponibilizar um conteúdo.

Não altera o conteúdo.

---

## Canal

Meio pelo qual um conteúdo é disponibilizado.

Exemplos:

- Portal
- Aplicativo
- API
- TV Corporativa

---

## Vigência

Período durante o qual uma publicação permanece ativa.

---

## Prioridade

Ordem de apresentação entre conteúdos publicados.

---

# Distribuição

## Notificação

Mensagem enviada ao usuário informando que determinado conteúdo foi publicado ou atualizado.

Não representa conteúdo.

---

## Destinatário

Usuário apto a receber uma publicação ou notificação.

---

## Público-Alvo

Conjunto de usuários aos quais uma publicação é destinada.

---

# Ciclo de Vida

## Estado

Situação atual de um conteúdo.

---

## Rascunho

Conteúdo em elaboração.

---

## Em Revisão

Conteúdo aguardando validação.

---

## Aprovado

Conteúdo autorizado para publicação.

---

## Agendado

Conteúdo aguardando publicação automática.

---

## Publicado

Conteúdo disponível aos usuários.

---

## Expirado

Conteúdo cuja vigência terminou.

---

## Arquivado

Conteúdo mantido apenas para consulta histórica.

---

## Excluído

Conteúdo removido logicamente.

---

# Segurança

## Usuário

Pessoa autenticada que utiliza o Portal.

---

## Papel

Responsabilidade institucional atribuída a um usuário.

Exemplos:

- Administrador
- Editor
- Revisor
- Leitor

---

## Permissão

Capacidade para executar determinada ação.

---

## Visibilidade

Conjunto de usuários aptos a visualizar determinado conteúdo.

---

## Compartilhamento

Concessão adicional de acesso a um conteúdo.

---

# Auditoria

## Auditoria

Registro histórico das alterações realizadas.

---

## Histórico

Conjunto de registros preservados ao longo da vida do conteúdo.

---

## Rastreabilidade

Capacidade de identificar toda a evolução de um conteúdo.

---

# Versionamento

## Versão

Representação específica de um conteúdo em determinado momento.

---

## Versão Atual

Última versão válida para publicação.

---

## Versão Histórica

Versão preservada apenas para consulta.

---

# Capacidades Compartilhadas

Os conceitos abaixo representam capacidades comuns a todos os tipos de conteúdo.

- Auditoria
- Versionamento
- Publicação
- Compartilhamento
- Permissões
- Visibilidade
- Busca
- Classificação
- Histórico

Esses conceitos nunca pertencem exclusivamente a um tipo de conteúdo.

---

# Termos Proibidos

Os termos abaixo não deverão ser utilizados como sinônimos.

| Não utilizar | Utilizar |
|--------------|----------|
| PDF | Arquivo |
| Documento PDF | Arquivo PDF |
| Documento (quando significar arquivo) | Arquivo |
| Página | Conteúdo |
| Publicação (quando significar conteúdo) | Conteúdo |
| Notificação (quando significar comunicado) | Comunicado |
| Aviso (quando significar notificação) | Notificação |
| Pasta (quando significar categoria) | Categoria |
| Categoria (quando significar pasta) | Pasta |

---

# Relação entre Conceitos

```
Conteúdo

├── Documento
├── Comunicado
├── Notícia
├── Aviso
└── Postagem

↓

Possui

↓

Arquivos

↓

É Organizado por

↓

Pastas
Categorias
Tags

↓

É Disponibilizado por

↓

Publicações

↓

Pode Gerar

↓

Notificações
```

---

# Convenções de Nomenclatura

## Conceitos

Utilizar substantivos no singular.

Exemplos:

- Documento
- Arquivo
- Categoria
- Publicação

---

## Tipos

Utilizar nomes de negócio.

Nunca utilizar nomes tecnológicos.

Correto:

- Comunicado
- Notícia

Incorreto:

- PDF
- HTML
- JSON

---

## Capacidades

Utilizar substantivos que representem comportamento do domínio.

Exemplos:

- Auditoria
- Versionamento
- Compartilhamento

---

# Relação com Outros Artefatos

| Artefato | Responsabilidade |
|----------|------------------|
| `00-domain-overview.md` | Visão arquitetural, classificação DDD e eventos |
| `01-content-model.md` | Modelo conceitual |
| 02-content-taxonomy.md | Tipos de conteúdo |
| 03-content-lifecycle.md | Estados do conteúdo |
| 04-publication-model.md | Publicação |
| 05-permission-model.md | Permissões e visibilidade |

---

# Decisões Arquiteturais

## DA-023

Este documento é a referência oficial da linguagem ubíqua do domínio de conteúdo.

---

## DA-024

Nenhum artefato poderá introduzir novos conceitos sem atualização deste glossário.

---

## DA-025

A nomenclatura do banco de dados, APIs, eventos de domínio e classes de negócio deverá ser derivada dos conceitos definidos neste documento.

---

## DA-026

Os conceitos aqui definidos prevalecem sobre nomenclaturas herdadas do sistema legado.

---

# Governança

Toda evolução do domínio deverá seguir o seguinte processo:

1. Atualizar o modelo conceitual (`01-content-model.md`), quando necessário.
2. Atualizar a taxonomia (`02-content-taxonomy.md`), se houver novos tipos.
3. Atualizar este glossário com os novos conceitos ou alterações.
4. Refletir as mudanças nos demais artefatos da camada, incluindo `00-domain-overview.md`.
5. Validar a consistência da linguagem ubíqua antes da implementação.

O modelo lógico foi consolidado durante a evolução da arquitetura; sua representação encontra-se incorporada ao modelo conceitual desta camada. Não existe artefato separado de modelo lógico.

Esse processo garante que o domínio permaneça consistente e que todos os participantes do projeto utilizem a mesma terminologia ao longo de todo o ciclo de vida do Portal de Comunicação.