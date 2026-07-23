# Governança — camada `database/`

**Atividade:** DB-ORG-01  
**Schema:** `UNMPORTCOM`  
**Baseline homologada:** [baseline/oracle-baseline-2026-07-22.md](baseline/oracle-baseline-2026-07-22.md)

Este documento formaliza responsabilidades, precedência e políticas da SSOT de banco do Portal de Comunicação.

---

## 1. Ordem oficial de precedência

Em caso de conflito entre artefatos, **prevalece sempre a camada superior**:

| Prioridade | Camada | Localização |
|:----------:|--------|-------------|
| **1** | **Baseline física homologada** | `baseline/oracle-baseline-2026-07-22.md` |
| **2** | **Evidências de validação Oracle** | `validation/oracle-schema-validation-2026-07-22.md` |
| **3** | **Scripts DDL versionados** | `ddl/` |
| **4** | **Migrações** | `migrations/` |
| **5** | **Documentação complementar** | `model/`, `README.md`, `reports/` (histórico) |

Regra: nenhum script, modelo ou relatório pode contradizer a baseline. DDL e migrações **convergem** para a baseline (sincronização **DB-SYNC-99** concluída em 2026-07-22).

---

## 2. Responsabilidade por diretório

### `baseline/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Especificação física homologada do schema Oracle. |
| **O que não é** | Script executável, DML, histórico de atividades. |
| **Autoridade** | Fonte oficial do estado do banco quando o Oracle não está acessível. |
| **Alteração** | Somente após nova homologação DBA + evidência em `validation/`. |

### `validation/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Evidência técnica da inspeção estrutural no Oracle homologado. |
| **O que não é** | Documentação normativa nem substituto da baseline. |
| **Uso** | Rastreabilidade e auditoria; suporte a decisões de evolução estrutural. |

### `reports/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Histórico de atividades (organização, sincronização, consolidações). |
| **O que não é** | Estado oficial do banco. |
| **Uso** | Consulta contextual; não prevalece sobre baseline nem DDL. |

### `ddl/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Implementação versionada instalável (greenfield). |
| **Estado** | Alinhado ao Oracle homologado (**DB-SYNC-99**). |
| **Execução** | DBA; aplicação não altera schema (DEC-DB-019). |

### `migrations/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Evolução estrutural **brownfield** pós-baseline ou scripts históricos. |
| **Greenfield** | Usar apenas `ddl/000-install.sql` — **não** aplicar V003/V004 se o install completo já refletir a homologação. |
| **Brownfield** | Scripts versionados executados pelo DBA quando o ambiente legado não coincide com o baseline. |
| **Flyway** | Não utilizado na aplicação (DEC-DB-019). |

### `dml/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Cargas institucionais iniciais (dados), fora do escopo da baseline física. |
| **Política** | Execução única em greenfield; não reexecutar em banco já carregado. |

### `model/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Modelo conceitual, lógico e físico **complementar** (engenharia). |
| **Precedência** | Subordinado à baseline física em caso de divergência estrutural. |

### `rollback/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Reservado para scripts de reversão de migrações (evolução futura). |
| **Estado** | Sem scripts ativos na homologação 2026-07-22. |

### `security/`

| Aspecto | Definição |
|---------|-----------|
| **O que é** | Privilégios do usuário de aplicação (`UNMPORTCOM_APP`) sobre objetos do owner — grants de tabelas e sequences (DEC-DB-024). |
| **O que não é** | DDL estrutural nem substituto de `ddl/007-create-grants.sql` no greenfield (complementa e documenta reaplicação). |
| **Execução** | DBA, após `003`+ e após migrations que criem objetos. |
| **SSOT** | [security/README.md](security/README.md) |

---

## 3. Políticas

| ID | Política |
|----|----------|
| GOV-DB-01 | `database/` é a única SSOT da camada de banco no repositório. |
| GOV-DB-02 | Não criar camada paralela de banco fora de `database/` (ART-DB-01; validado em ART-DB-02 e ART-DB-03). |
| GOV-DB-03 | Evolução estrutural futura: baseline atualizada **ou** migração versionada + relatório em `reports/`. |
| GOV-DB-04 | Agentes sem acesso ao Oracle usam baseline + validation; DDL reflete a homologação vigente. |
| GOV-DB-05 | Privilégios do application user seguem `database/security/` (DEC-DB-024); nova tabela/sequence exige GRANT. |
| GOV-DB-06 | Conexão backend e testes Oracle usam exclusivamente `UNMPORTCOM_APP` (`SPRING_DATASOURCE_USERNAME`); `UNMPORTCOM` é somente schema owner (INFRA-DB-01). |

---

## 4. Fluxo de trabalho

```text
Homologação Oracle
        ↓
baseline/ + validation/
        ↓
DB-ORG-01 (governança)  ← concluída
        ↓
DB-SYNC-99 (Oracle × DDL)  ← concluída (2026-07-22)
        ↓
Evolução: migrations/ + atualização de baseline (quando aplicável)
```

---

## 5. Referências externas

- Nomenclatura e padrões corporativos: `docs/implementation/06-database-standards.md`
- Decisões DEC-DB-*: `model/05-decisions-and-risks.md`
- DEC-DB-024 (application user): `docs/architecture/decisions/DEC-DB-024-application-user-strategy.md`
