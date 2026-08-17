# Portal de Comunicação — Specifications

## Propósito

O diretório `specs/` contém as especificações oficiais do Portal de Comunicação.

Toda implementação — código, infraestrutura, integrações e testes — deve derivar exclusivamente das especificações produzidas aqui.

As especificações definem **o que** o sistema deve fazer e **como** deve se comportar. O código materializa essas definições.

---

## Escopo desta camada

```text
specs/
├── foundation/          — processo SDD, DoR/DoD, mapa SSOT
├── features/<slug>/     — especificações funcionais por feature
├── architecture/        — arquitetura normativa (ex.: authentication)
└── templates/           — templates oficiais de feature
```

Mapa SSOT operacional: [`foundation/minimal-ssot.md`](foundation/minimal-ssot.md).

---

## Artefatos Foundation

| Artefato | Papel |
|----------|-------|
| `principles.md` | Princípios do SDD |
| `workflow.md` | Ciclo SDD geral |
| `conventions.md` | Convenções e relação com `docs/` |
| `minimal-ssot.md` | **Mapa SSOT** (Etapa 2/3) |
| `path-conventions.md` | Paths sem manifest |
| `development-workflow.md` | Fluxo simplificado diário |
| `definition-of-ready.md` | DoR |
| `definition-of-done.md` | DoD |
| `feature-yaml.md` | Contrato `feature.yaml` |

---

## Documentação consultiva

O diretório `docs/` contém documentação de engenharia (domínio, arquitetura, governança).

`docs/` possui caráter **consultivo** para specs. Não orienta implementação diretamente.

Em caso de conflito entre uma especificação em `specs/` e um documento em `docs/`, prevalece a especificação.

---

## Regra fundamental

```text
Especificação → Implementação
```

O código nunca é a fonte da verdade. Alterações funcionais ou comportamentais começam em `specs/`.
