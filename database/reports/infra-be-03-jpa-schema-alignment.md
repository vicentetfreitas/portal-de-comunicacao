# INFRA-BE-03 — Alinhamento JPA × Oracle (SSOT `database/`)

| Campo | Valor |
|-------|-------|
| Atividade | INFRA-BE-03 |
| Data | 2026-07-23 |
| SSOT | `database/ddl/003-create-tables.sql`, `002-create-sequences.sql` |

---

## 1. Resumo

Correção principal: **`SINGULAR.COD_UNIMED`** mapeado como `String`/`Long` sem `precision`, enquanto o Oracle define **`NUMBER(3) NOT NULL`**. Hibernate reportava `wrong column type` (esperava VARCHAR2, encontrou NUMBER).

Demais entidades organizacionais e de acesso foram confrontadas com o DDL; ajustes em **sequences** e coluna **`NUM_REGISTRO_ANS`** em `SINGULAR`.

---

## 2. Auditoria por entidade

### `SingularEntity` → `SINGULAR`

| Coluna Oracle | Tipo Oracle | Antes (Java) | Depois (Java) | Ação |
|---------------|-------------|--------------|---------------|------|
| `COD_UNIMED` | `NUMBER(3)` | `Long` / `String` (API) | `Integer` + `precision=3, scale=0` | Corrigido |
| `NUM_REGISTRO_ANS` | `VARCHAR2(20) NOT NULL` | ausente | `String registroAns` | Adicionado |
| Demais colunas | conforme DDL | OK | OK | — |

### `FederacaoEntity` → `FEDERACAO`

| Coluna | Oracle | Java | Status |
|--------|--------|------|--------|
| `COD_UNIMED` | `NUMBER(3)` | `Integer` precision 3 | OK |
| `DSC_FEDERACAO` | `CLOB` | `@Lob String` | OK |

### `AreaEntity` → `AREA`

| Coluna | Oracle | Java | Status |
|--------|--------|------|--------|
| `DSC_AREA` | `CLOB` | `@Lob` | OK |
| `COD_SINGULAR` | nullable | `Long` nullable | OK |

### `EquipeEntity` → `EQUIPE`

| Coluna | Oracle | Java | Status |
|--------|--------|------|--------|
| `DSC_EQUIPE` | `CLOB` | `@Lob` | OK |

### `ColaboradorEntity` → `COLABORADOR`

| Coluna | Oracle | Java | Status |
|--------|--------|------|--------|
| `DES_BIOGRAFIA` | `VARCHAR2(4000)` | `length=4000` (sem `@Lob`) | OK |
| Sequence | `SQ_COLABORADOR` | era `SQ_COLABORADOR_COD_COLABORADOR` | Corrigido |

### `AuthSessaoEntity` → `AUTH_SESSAO`

| Item | Oracle | Java | Status |
|------|--------|------|--------|
| Sequence | `SQ_AUTH_SESSAO` | era `SQ_AUTH_SESSAO_COD_SESSAO` | Corrigido |
| Colunas | DDL baseline | mapeamento | OK |

---

## 3. Camada API / aplicação (FT-SINGULAR)

| Artefato | Alteração |
|----------|-----------|
| `CreateSingularRequest` / `UpdateSingularRequest` | `unimedCode`: `Integer` (1–999); `registroAns` obrigatório |
| `SingularResponse` | `unimedCode` numérico; `registroAns` |
| `SingularRepository` | Unicidade e filtro por `Integer` (sem `IgnoreCase` em número) |
| `SingularDomainService` | Validação com `Integer` |
| `SingularApplicationService` / `SingularMapper` / `SingularController` | Propagação dos tipos |
| Testes de aceite e unitários | Payloads JSON e seeds atualizados |

**Nota:** Contratos REST de `unimedCode` passam de string alfanumérica para **número** (3 dígitos), alinhado ao DDL. Specs em `specs/features/singular/api.md` ainda citam String — evolução documental recomendada.

---

## 4. Arquivos alterados

- `SingularEntity.java`
- `ColaboradorEntity.java`, `AuthSessaoEntity.java`
- `SingularRepository.java`, `SingularDomainService.java`, `SingularApplicationService.java`
- `CreateSingularRequest.java`, `UpdateSingularRequest.java`, `SingularResponse.java`
- `SingularMapper.java`, `SingularController.java`
- Testes: `SingularAcceptanceIntegrationTest`, `Area*`, `Equipe*`, `Colaborador*`, `OrgCrossFeature*`, `SingularMapperTest`, `SingularDomainServiceTest`
- `SchemaOracleAuditTest.java` (expectativas de auditoria)

---

## 5. Evidências

Executar localmente:

```bash
cd backend && mvn clean test
```

Critério: `ddl-auto=validate` no perfil `test` sem erros `wrong column type` / `missing column` para as entidades mapeadas.

---

## 6. Próximos erros de validação

Se surgirem novas divergências em tabelas ainda sem entidade JPA, tratar na Feature correspondente. Entidades atuais no backend cobrem apenas o subconjunto organizacional + `AUTH_SESSAO` / `COLABORADOR`.
