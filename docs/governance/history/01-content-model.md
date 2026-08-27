# Modelo Conceitual de Conteúdo

| Campo | Valor |
|------|-------|
| Categoria documental | Archive |
| Status | Obsoleto — modelo conceitual de conteúdo editorial; responsabilidade transferida ao WordPress (`DEC-CMS-001`, aprovada) |
| Motivo | Zero consumidores confirmados; decisão D3, Plano W2, 2026-08-20 |
| Origem | Movido de `specs/domain/01-content-model.md` em 2026-08-20 |

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Domain |
| Artefato | 01-content-model.md |
| Status | Draft |
| Versão | 1.0 |
| Autor | Equipe de Arquitetura |
| Última atualização | 2026-07-08 |

---

# Objetivo

Definir o modelo conceitual do domínio de conteúdo do Portal de Comunicação.

Este documento estabelece a linguagem ubíqua utilizada por analistas, desenvolvedores, arquitetos, designers e Product Owners para representar conteúdos institucionais de forma consistente.

Este artefato **não define**:

- modelo físico do banco de dados;
- APIs;
- classes de implementação;
- interfaces gráficas.

Seu propósito é exclusivamente definir os conceitos do domínio e seus relacionamentos.

---

# Princípios

O domínio de conteúdo é baseado nos seguintes princípios.

## PR-001 — Conteúdo é diferente de Arquivo

O conteúdo representa a informação.

O arquivo representa sua materialização física.

Um mesmo conteúdo pode possuir diversos arquivos.

Exemplo:

```
Manual do Beneficiário

├── manual.pdf
├── manual.docx
└── capa.png
```

Existe apenas um conteúdo.

Existem vários arquivos.

---

## PR-002 — Conteúdo é independente da estrutura organizacional

O conteúdo possui um responsável pela publicação, porém não pertence estruturalmente à organização.

Mudanças organizacionais não devem exigir alterações no conteúdo.

---

## PR-003 — Conteúdo é independente do canal de distribuição

O mesmo conteúdo pode ser publicado em diversos canais.

Exemplos:

- Portal
- Aplicativo
- Push
- E-mail
- Banner
- TV Corporativa

A forma de entrega não altera o conteúdo.

---

## PR-004 — Todo conteúdo possui ciclo de vida

Independentemente do tipo, todo conteúdo possui um ciclo de vida comum.

Exemplos:

- criação;
- revisão;
- publicação;
- arquivamento;
- exclusão lógica.

---

## PR-005 — Recursos transversais são compartilhados

Todos os tipos de conteúdo compartilham capacidades comuns.

- auditoria;
- permissões;
- visibilidade;
- compartilhamento;
- publicação;
- indexação;
- busca;
- histórico;
- classificação.

Esses comportamentos não pertencem a um tipo específico de conteúdo.

---

# Domínio de Conteúdo

O domínio é organizado em cinco grupos conceituais.

```
Organização

├── Federação
├── Singular
├── Área
└── Equipe

------------------------------------

Conteúdo

├── Documento
├── Comunicado
├── Notícia
├── Aviso
└── Postagem

------------------------------------

Arquivos

└── Arquivo

------------------------------------

Organização do Conteúdo

├── Pasta
├── Categoria
└── Tag

------------------------------------

Distribuição

├── Publicação
└── Notificação
```

---

# Conceitos Fundamentais

## Conteúdo

É a entidade conceitual que representa qualquer informação institucional publicada pelo Portal.

Todo conteúdo:

- possui identidade;
- possui autor ou responsável;
- pode ser publicado;
- pode possuir anexos;
- pode possuir permissões;
- pode possuir visibilidade;
- pode ser pesquisado;
- pode ser auditado.

Conteúdo é o conceito central deste domínio.

---

## Documento

Representa conteúdo institucional permanente ou normativo.

Exemplos:

- Regulamentos;
- Políticas;
- Procedimentos;
- Manuais;
- Formulários;
- Guias.

Características:

- normalmente possui versionamento;
- pode exigir aprovação;
- pode possuir anexos;
- possui histórico.

---

## Comunicado

Representa uma comunicação oficial emitida pela organização.

Características:

- possui emissor oficial;
- possui público-alvo;
- possui período de vigência;
- pode possuir prioridade;
- pode gerar notificações.

Exemplos:

- Alteração de expediente;
- Mudança de processo;
- Orientações institucionais.

---

## Notícia

Representa conteúdo informativo destinado à divulgação de acontecimentos.

Características:

- possui caráter jornalístico;
- pode possuir imagens;
- possui autor;
- normalmente permanece disponível após publicação.

Exemplos:

- Eventos;
- Campanhas;
- Resultados;
- Conquistas.

---

## Aviso

Representa uma comunicação breve destinada a alertar usuários.

Características:

- curta duração;
- alta visibilidade;
- normalmente não possui versionamento complexo.

Exemplos:

- Sistema indisponível;
- Manutenção;
- Interrupção de serviços.

---

## Postagem

Representa conteúdo institucional menos formal.

Características:

- comunicação mais dinâmica;
- pode possuir imagens e vídeos;
- pode ser utilizada para campanhas internas.

Exemplos:

- Eventos;
- Datas comemorativas;
- Ações sociais;
- Dicas;
- Informativos rápidos.

---

# Arquivo

Arquivo representa um objeto físico armazenado pelo sistema.

Exemplos:

- PDF
- DOCX
- XLSX
- PPTX
- JPG
- PNG
- MP4

Um arquivo nunca representa o conteúdo em si.

Ele apenas materializa parte do conteúdo.

Um conteúdo pode possuir vários arquivos.

---

# Pasta

Pasta representa uma estrutura lógica de organização.

Ela não possui significado de negócio.

Sua finalidade é facilitar a navegação.

Uma pasta pode conter:

- documentos;
- comunicados;
- notícias;
- avisos;
- postagens.

Pastas podem ser hierárquicas.

---

# Categoria

Categoria representa uma classificação institucional.

Exemplos:

- RH
- Financeiro
- Jurídico
- Tecnologia
- Compliance

Categorias facilitam organização e busca.

---

# Tag

Tag representa uma classificação livre.

Seu objetivo é facilitar pesquisas e relacionamentos entre conteúdos.

Exemplos:

- LGPD
- Home Office
- Benefícios
- Assembleia

---

# Publicação

Publicação representa o ato de disponibilizar um conteúdo para um ou mais canais.

Uma publicação define:

- quando publicar;
- quando retirar;
- onde publicar;
- para quem publicar.

O conteúdo permanece o mesmo.

A publicação controla sua distribuição.

---

# Notificação

Notificação não representa conteúdo.

Notificação representa um mecanismo de comunicação ao usuário.

Sua finalidade é informar que determinado conteúdo está disponível.

Uma notificação pode ser entregue por:

- push;
- e-mail;
- mensagem interna;
- aplicativo;
- web.

A notificação referencia um conteúdo.

Ela não substitui o conteúdo.

---

# Relacionamentos Conceituais

```
Pasta

    organiza

        Conteúdos

----------------------------

Conteúdo

    possui

        Arquivos

----------------------------

Conteúdo

    recebe

        Categorias

----------------------------

Conteúdo

    recebe

        Tags

----------------------------

Conteúdo

    origina

        Publicações

----------------------------

Publicação

    pode gerar

        Notificações
```

---

# Capacidades Compartilhadas

Todos os tipos de conteúdo herdam as seguintes capacidades.

| Capacidade | Aplicável |
|------------|-----------|
| Auditoria | Sim |
| Versionamento | Sim* |
| Compartilhamento | Sim |
| Visibilidade | Sim |
| Publicação | Sim |
| Busca | Sim |
| Classificação | Sim |
| Permissões | Sim |
| Histórico | Sim |

> **Observação:** o nível de versionamento poderá variar conforme o tipo de conteúdo.

---

# Fora do Escopo

Este documento não define:

- modelo relacional;
- modelo físico Oracle;
- APIs REST;
- eventos de integração;
- telas;
- componentes Vue;
- permissões detalhadas;
- fluxo de publicação.

Esses assuntos serão tratados em artefatos específicos.

---

# Decisões Arquiteturais

## DA-027

Conteúdo será o conceito central do domínio.

Todos os tipos de conteúdo deverão especializar este conceito.

---

## DA-028

Arquivo será tratado como entidade independente do conteúdo.

---

## DA-029

Notificação pertence ao domínio de distribuição, não ao domínio de conteúdo.

---

## DA-030

Os recursos de auditoria, publicação, compartilhamento, visibilidade e busca serão tratados como capacidades transversais comuns a todos os tipos de conteúdo.

---

# Próximos Artefatos

Este documento serve como base para os seguintes artefatos:

- `00-domain-overview.md`
- `02-content-taxonomy.md`
- `03-content-lifecycle.md`
- `04-publication-model.md`
- `05-permission-model.md`
- `06-content-glossary.md`