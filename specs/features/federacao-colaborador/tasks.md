# Tasks — FT-FEDERACAO-COLABORADOR

| Task | RF | Situação |
|------|----|----|
| TK-001 | RF-FEDCOLAB-001 | Feito — `FederacaoAreaHubPage.vue`, rota `/app/federacao/areas/:id` |
| TK-002 | RF-FEDCOLAB-002 | Feito — `FederacaoAreaEquipePage.vue`, rota `/app/federacao/areas/:id/equipe` |
| TK-003 | RF-FEDCOLAB-003 | Feito — `FederacaoSingularPage.vue`, rota `/app/federacao/singulares/:id` |

Escrito retroativamente (implementação decidida e construída em sessão interativa, não via Specify/Readiness formal prévio) — ver "Origem" em `specification.md`.

## Progresso (2026-08-26, 2ª rodada)

| Item | Situação |
|------|----------|
| Botões "Equipe"/"Arquivos e Documentos" — formato ícone+botão | Feito — trocado de `DsActionCard` para `DsButton`+`DsIcon` |
| Roster: cargo, telefone(s), ramal(is), "Contato setorial" | Feito, **mockado** (autorizado explicitamente pelo usuário) |
| Lista da Equipe sem borda/card | Feito — trocado de `DsDataTable` para lista simples |
| Botão de voltar ao lado do título (não no header) | Feito — `DsPageHeader` ganhou `show-back`/`@back`; removido de `AppHeader.vue` |
| "Editar perfil" funcional | Feito — navega para `/app/perfil` (visualização; `FT-PERFIL` segue `DRAFT`) |

**Gap explícito que continua aberto**: cargo/telefone/ramal/"Contato setorial" reais exigem mudança de contrato de backend (`FT-COLABORADOR`, `FT-AREA`) — fora do alcance desta Feature (frontend-only, sem alteração de API).
