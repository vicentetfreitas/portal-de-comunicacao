# Taxonomia de Conteúdo

| Item | Valor |
|------|-------|
| Projeto | Portal de Comunicação |
| Camada | Domain |
| Artefato | 02-content-taxonomy.md |
| Status | Draft |
| Versão | 1.0 |
| Dependência | `00-domain-overview.md`, `01-content-model.md` |
| Última atualização | 2026-07-08 |

---

# Objetivo

Este documento define a taxonomia oficial do domínio de conteúdo do Portal de Comunicação.

Seu objetivo é estabelecer uma classificação padronizada para todos os tipos de conteúdo suportados pelo sistema, permitindo uma evolução consistente do domínio, do modelo de dados, das APIs e da experiência do usuário.

Este documento complementa o **01-content-model.md**, detalhando a especialização do conceito abstrato **Conteúdo**.

---

# Objetivos da Taxonomia

A taxonomia deve permitir:

- padronização dos tipos de conteúdo;
- reutilização de funcionalidades comuns;
- organização da informação;
- facilidade de pesquisa;
- governança do conteúdo;
- escalabilidade do domínio.

---

# Estrutura Conceitual

```
Conteúdo
│
├── Documento
├── Comunicado
├── Notícia
├── Aviso
└── Postagem
```

Todo conteúdo institucional pertence obrigatoriamente a um dos tipos definidos nesta taxonomia.

---

# Conceito Fundamental

## Conteúdo

Conteúdo representa qualquer informação institucional administrada pelo Portal.

É um conceito abstrato.

Não possui utilização direta pelos usuários finais.

Seu objetivo é concentrar os comportamentos compartilhados por todos os tipos de conteúdo.

Todo Conteúdo possui:

- identidade única;
- título;
- descrição;
- autor;
- responsável;
- data de criação;
- situação;
- permissões;
- visibilidade;
- publicação;
- auditoria.

---

# Tipos de Conteúdo

## Documento

### Objetivo

Disponibilizar informações institucionais permanentes ou normativas.

### Exemplos

- Regulamentos
- Procedimentos
- Políticas
- Manuais
- Guias
- Formulários

### Características

- conteúdo formal;
- normalmente exige versionamento;
- pode possuir aprovação;
- pode possuir anexos;
- longa vida útil.

---

## Comunicado

### Objetivo

Transmitir comunicações oficiais emitidas pela organização.

### Exemplos

- Alterações de processos;
- Comunicados da Diretoria;
- Mudanças internas;
- Orientações corporativas.

### Características

- origem institucional;
- público-alvo definido;
- vigência;
- prioridade;
- publicação imediata ou agendada.

---

## Notícia

### Objetivo

Divulgar acontecimentos relevantes para a organização.

### Exemplos

- Eventos;
- Projetos;
- Conquistas;
- Campanhas.

### Características

- linguagem informativa;
- pode possuir galeria de imagens;
- pode possuir vídeos;
- autoria identificada.

---

## Aviso

### Objetivo

Alertar usuários sobre situações temporárias.

### Exemplos

- Indisponibilidade de sistemas;
- Manutenção;
- Alteração de expediente;
- Interrupção de serviços.

### Características

- curta duração;
- alta prioridade;
- alta visibilidade;
- normalmente exibido em destaque.

---

## Postagem

### Objetivo

Publicar conteúdos institucionais menos formais.

### Exemplos

- Datas comemorativas;
- Eventos;
- Campanhas internas;
- Ações sociais;
- Dicas.

### Características

- formato flexível;
- forte apelo visual;
- conteúdo dinâmico.

---

# Hierarquia Taxonômica

```
Conteúdo

├── Documento
│
├── Comunicado
│
├── Notícia
│
├── Aviso
│
└── Postagem
```

Os tipos definidos nesta hierarquia representam especializações do conceito de Conteúdo.

---

# Capacidades Herdadas

Todos os tipos de conteúdo herdam as seguintes capacidades.

| Capacidade | Documento | Comunicado | Notícia | Aviso | Postagem |
|------------|-----------|------------|----------|--------|-----------|
| Auditoria | ✔ | ✔ | ✔ | ✔ | ✔ |
| Publicação | ✔ | ✔ | ✔ | ✔ | ✔ |
| Compartilhamento | ✔ | ✔ | ✔ | ✔ | ✔ |
| Busca | ✔ | ✔ | ✔ | ✔ | ✔ |
| Permissões | ✔ | ✔ | ✔ | ✔ | ✔ |
| Visibilidade | ✔ | ✔ | ✔ | ✔ | ✔ |
| Histórico | ✔ | ✔ | ✔ | ✔ | ✔ |
| Versionamento | ✔ | ✔* | ✔* | ✔* | ✔* |

> O nível de versionamento poderá variar conforme as regras específicas de cada tipo.

---

# Capacidades Específicas

## Documento

- Controle formal de versões;
- Aprovação;
- Anexos;
- Histórico completo.

---

## Comunicado

- Vigência;
- Prioridade;
- Público-alvo;
- Geração de notificações.

---

## Notícia

- Galeria;
- Autor da publicação;
- Destaques.

---

## Aviso

- Prioridade elevada;
- Tempo limitado;
- Exibição em destaque.

---

## Postagem

- Conteúdo multimídia;
- Formatação rica;
- Campanhas.

---

# Critérios de Classificação

Todo novo conteúdo deverá responder às seguintes perguntas.

## É um documento institucional permanente?

→ Documento

---

## É uma comunicação oficial?

→ Comunicado

---

## Divulga um acontecimento?

→ Notícia

---

## Alerta sobre uma situação temporária?

→ Aviso

---

## É um conteúdo institucional mais livre?

→ Postagem

---

# Extensibilidade

Novos tipos poderão ser adicionados futuramente.

Exemplos:

```
Conteúdo

├── Documento
├── Comunicado
├── Notícia
├── Aviso
├── Postagem
├── FAQ
├── Evento
├── Pesquisa
├── Banner
└── Podcast
```

A inclusão de um novo tipo deverá:

- especializar Conteúdo;
- reutilizar capacidades compartilhadas;
- não duplicar funcionalidades existentes;
- ser documentada nesta taxonomia.

---

# Regras Gerais

## TG-001

Todo conteúdo pertence exatamente a um tipo.

---

## TG-002

Um tipo de conteúdo nunca poderá herdar de outro tipo específico.

Exemplo:

```
Documento

×

Comunicado
```

Ambos especializam diretamente Conteúdo.

---

## TG-003

Funcionalidades compartilhadas pertencem ao conceito Conteúdo.

Nunca devem ser implementadas individualmente em cada tipo.

---

## TG-004

Tipos representam comportamento de negócio.

Eles não representam formato de arquivo.

Exemplo:

```
Documento

≠

PDF
```

---

## TG-005

A taxonomia representa conceitos do domínio.

Ela independe de:

- banco de dados;
- APIs;
- frontend;
- implementação Java.

---

# Relação com Outros Artefatos

| Artefato | Relação |
|----------|----------|
| `00-domain-overview.md` | Visão arquitetural e classificação estratégica |
| `01-content-model.md` | Define os conceitos fundamentais |
| 03-content-lifecycle.md | Define o ciclo de vida dos conteúdos |
| 04-publication-model.md | Define como os conteúdos são publicados |
| 05-permission-model.md | Define visibilidade e permissões |
| 06-content-glossary.md | Consolida a linguagem ubíqua |

---

# Decisões Arquiteturais

## DA-005

Todo tipo de conteúdo especializa diretamente o conceito Conteúdo.

---

## DA-006

Não será permitido criar funcionalidades exclusivas que possam ser tratadas como capacidades compartilhadas.

---

## DA-007

A taxonomia representa exclusivamente conceitos de negócio.

Ela não deverá refletir limitações tecnológicas ou detalhes de implementação.

---

# Considerações para Evolução

A taxonomia foi projetada para suportar a evolução do Portal de Comunicação sem comprometer a consistência do domínio.

Novos tipos de conteúdo deverão preservar:

- simplicidade do modelo;
- reutilização das capacidades comuns;
- clareza da linguagem ubíqua;
- baixo acoplamento entre conceitos.