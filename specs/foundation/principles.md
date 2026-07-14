# Princípios do SDD

## Objetivo

Definir os princípios que orientam a produção e o uso de especificações no Portal de Comunicação.

---

## Especificação como fonte da verdade

As especificações em `specs/` são a única fonte oficial para o desenvolvimento.

O código implementa a especificação. A especificação não documenta o código.

Quando o comportamento implementado divergir da especificação, a implementação está incorreta — não a especificação.

---

## Simplicidade

A estrutura de `specs/` deve ser mínima.

Novos diretórios e artefatos surgem apenas quando houver necessidade concreta. Não se antecipa organização futura.

Cada documento deve ter propósito claro e escopo delimitado.

---

## Evolução incremental

As especificações crescem conforme o projeto evolui.

Não se produz documentação especulativa. Não se cria infraestrutura documental antes de existir demanda real.

A estrutura atual reflete apenas o que o projeto precisa agora.

---

## Documentação histórica

O diretório `docs/` serve como referência para:

- entendimento do domínio legado;
- regras de negócio existentes;
- arquitetura e integrações atuais;
- processos e infraestrutura em produção.

`docs/` não substitui `specs/` e não orienta implementação diretamente.

Informações extraídas de `docs/` devem ser validadas e consolidadas em especificações antes de orientar qualquer desenvolvimento.

---

## Independência de framework

O SDD deste projeto não replica nenhum framework externo.

Adota boas práticas consolidadas de engenharia de software — especificação antes da implementação, rastreabilidade, clareza de escopo — sem amarras a ferramentas ou metodologias proprietárias.

---

## Rastreabilidade

Toda especificação deve ser rastreável:

- à necessidade que originou sua criação;
- às fontes consultivas utilizadas (quando aplicável);
- à implementação que a materializa.

Rastreabilidade não exige burocracia. Exige que a origem e o destino de cada decisão especificada sejam identificáveis.

---

## Governança de alterações

Alterações em especificações existentes seguem o mesmo rigor de criação:

1. Identificar a necessidade de mudança.
2. Atualizar a especificação correspondente.
3. Implementar conforme a especificação atualizada.
4. Validar aderência entre especificação e implementação.

Não se corrige comportamento diretamente no código sem atualizar a especificação que o define.
