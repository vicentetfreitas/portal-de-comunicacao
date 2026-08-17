# Etapa 4 — Auditoria Oracle × DDL × JPA

| Campo | Valor |
|-------|-------|
| Atividade | Etapa 4 — Reconciliação SSOT |
| Data | 2026-08-14 |
| SSOT | `database/baseline/oracle-baseline-2026-07-22.md`, `database/ddl/`, entidades JPA em `backend/` |
| Escopo | 6 entidades JPA de produção vs baseline homologado |

---

## 1. Resumo executivo

| Métrica | Valor |
|---------|-------|
| Tabelas no baseline | 23 |
| Entidades JPA mapeadas | 6 |
| Alinhamento colunas (6 entidades) | **OK** |
| Sequences JPA vs DDL greenfield | **OK** |
| Divergências críticas | **0** |
| Pendências documentais | 1 (`unimedCode` em `specs/features/singular/api.md`) |
| Pendências brownfield | 1 (nomenclatura `SQ_AUTH_SESSAO` vs `SQ_AUTH_SESSAO_COD_SESSAO` em V003) |

**Conclusão:** As 6 entidades implementadas estão alinhadas ao DDL greenfield homologado. Gaps restantes são escopo futuro (17 tabelas sem JPA) ou ambientes brownfield.

---

## 2. Matriz entidade × tabela × sequence

| Entidade JPA | Tabela | Sequence JPA | Sequence DDL | Status |
|--------------|--------|--------------|--------------|--------|
| `FederacaoEntity` | `FEDERACAO` | `SQ_FEDERACAO_COD_FEDERACAO` | `SQ_FEDERACAO_COD_FEDERACAO` | OK |
| `SingularEntity` | `SINGULAR` | `SQ_SINGULAR_COD_SINGULAR` | `SQ_SINGULAR_COD_SINGULAR` | OK |
| `AreaEntity` | `AREA` | `SQ_AREA_COD_AREA` | `SQ_AREA_COD_AREA` | OK |
| `EquipeEntity` | `EQUIPE` | `SQ_EQUIPE_COD_EQUIPE` | `SQ_EQUIPE_COD_EQUIPE` | OK |
| `ColaboradorEntity` | `COLABORADOR` | `SQ_COLABORADOR` | `SQ_COLABORADOR` | OK |
| `AuthSessaoEntity` | `AUTH_SESSAO` | `SQ_AUTH_SESSAO` | `SQ_AUTH_SESSAO` | OK |

---

## 3. Auditoria por entidade

### 3.1 FEDERACAO

| Coluna DDL | Tipo Oracle | Campo Java | Status |
|------------|-------------|------------|--------|
| COD_FEDERACAO | NUMBER(19) | `id` Long | OK |
| NOM_FEDERACAO | VARCHAR2(200) | `nome` | OK |
| SIG_FEDERACAO | VARCHAR2(30) | `sigla` | OK |
| COD_UNIMED | NUMBER(3) | `codigoUnimed` Integer precision 3 | OK |
| NUM_REGISTRO_ANS | VARCHAR2(20) | `registroAns` | OK |
| URL_SITE | VARCHAR2(300) | `urlSite` | OK |
| DSC_FEDERACAO | CLOB | `descricao` @Lob | OK |
| FLG_ATIVO | CHAR(1) | `ativo` CHAR | OK |
| DAT_CADASTRO / DAT_ATUALIZACAO | TIMESTAMP(6) | Instant | OK |

### 3.2 SINGULAR

| Coluna DDL | Tipo Oracle | Campo Java | Status |
|------------|-------------|------------|--------|
| COD_SINGULAR | NUMBER(19) | `id` | OK |
| COD_FEDERACAO | NUMBER(19) | `federacaoId` | OK |
| NOM_SINGULAR | VARCHAR2(200) | `nome` | OK |
| SIG_SINGULAR | VARCHAR2(30) | `sigla` | OK |
| COD_UNIMED | NUMBER(3) | `codigoUnimed` Integer | OK |
| NUM_REGISTRO_ANS | VARCHAR2(20) | `registroAns` | OK |
| FLG_ATIVO | CHAR(1) | `ativo` | OK |
| DAT_CADASTRO / DAT_ATUALIZACAO | TIMESTAMP(6) | Instant | OK |

### 3.3 AREA

| Coluna DDL | Tipo Oracle | Campo Java | Status |
|------------|-------------|------------|--------|
| COD_AREA | NUMBER(19) | `id` | OK |
| COD_SINGULAR | NUMBER(19) nullable | `singularId` | OK |
| NOM_AREA | VARCHAR2(200) | `nome` | OK |
| SIG_AREA | VARCHAR2(30) | `sigla` | OK |
| DSC_AREA | CLOB | `descricao` @Lob | OK |
| COD_GESTOR | NUMBER(19) | `gestorId` | OK |
| FLG_ATIVO | CHAR(1) | `ativo` | OK |
| DAT_CADASTRO / DAT_ATUALIZACAO | TIMESTAMP(6) | Instant | OK |

### 3.4 EQUIPE

| Coluna DDL | Tipo Oracle | Campo Java | Status |
|------------|-------------|------------|--------|
| COD_EQUIPE | NUMBER(19) | `id` | OK |
| COD_AREA | NUMBER(19) | `areaId` | OK |
| NOM_EQUIPE | VARCHAR2(200) | `nome` | OK |
| DSC_EQUIPE | CLOB | `descricao` @Lob | OK |
| COD_LIDER | NUMBER(19) | `liderId` | OK |
| FLG_ATIVO | CHAR(1) | `ativo` | OK |
| DAT_CADASTRO / DAT_ATUALIZACAO | TIMESTAMP(6) | Instant | OK |

### 3.5 COLABORADOR

| Coluna DDL | Tipo Oracle | Campo Java | Status |
|------------|-------------|------------|--------|
| COD_COLABORADOR | NUMBER(19) | `id` | OK |
| COD_FEDERACAO | NUMBER(19) | `federacaoId` | OK |
| COD_SINGULAR / COD_AREA / COD_EQUIPE / COD_GESTOR | NUMBER(19) nullable | Long FKs | OK |
| NOM_COLABORADOR | VARCHAR2(255) | `nome` | OK |
| DES_EMAIL | VARCHAR2(255) | `email` | OK |
| ID_ZIMBRA | VARCHAR2(255) | `zimbraId` | OK |
| DES_BIOGRAFIA | VARCHAR2(4000) | `biografia` length 4000 | OK |
| FLG_ATIVO | CHAR(1) | `ativo` | OK |
| DAT_NASCIMENTO / DAT_CONTRATACAO / DAT_ULTIMO_ACESSO | TIMESTAMP(6) | Instant | OK |
| DAT_CADASTRO / DAT_ATUALIZACAO | TIMESTAMP(6) | Instant | OK |

### 3.6 AUTH_SESSAO

| Coluna DDL | Tipo Oracle | Campo Java | Status |
|------------|-------------|------------|--------|
| COD_SESSAO | NUMBER(19) | `id` | OK |
| ID_SESSAO | VARCHAR2(36) | `sessionId` | OK |
| COD_COLABORADOR | NUMBER(19) FK | `@ManyToOne colaborador` | OK |
| HASH_REFRESH_TOKEN | VARCHAR2(255) | `refreshTokenHash` | OK |
| DES_DISPOSITIVO | VARCHAR2(255) | `dispositivo` | OK |
| FLG_REMEMBER_ME | CHAR(1) | `rememberMe` | OK |
| DAT_CRIACAO / DAT_EXPIRACAO | TIMESTAMP(6) | Instant | OK |
| FLG_REVOGADA | CHAR(1) | `revogada` | OK |
| DAT_REVOGACAO | TIMESTAMP(6) | `dataRevogacao` | OK |

**REF-DB-CTX-01:** Nenhuma coluna `COD_*_CTX` em `AUTH_SESSAO` — alinhado entre DDL, JPA e DEC-FA-003.

---

## 4. Tabelas sem JPA (escopo futuro)

17 tabelas existem no baseline sem entidade JPA. Esperado para Etapas 3–5 do MVP:

| Domínio | Tabelas |
|---------|---------|
| Organização | `ENDERECO`, `CONTATO`, `ONBOARDING_SOLICITACAO` (reservada — DEC-FA-001) |
| Documental | `CATEGORIA_DOCUMENTAL`, `PASTA`, `DOCUMENTO`, `ARQUIVO_BINARIO`, `DOCUMENTO_VERSAO`, `COMPARTILHAMENTO` |
| Acesso | `PAPEL`, `PAPEL_ATRIBUICAO`, `PERMISSAO_PASTA`, `SOLICITACAO_PERMISSAO`, `REGISTRO_AUDITORIA` |
| Comunicação | `COMUNICADO`, `NOTIFICACAO` |
| Config | `CONFIGURACAO_PORTAL` |

---

## 5. Brownfield — sequences divergentes

| Ambiente | Sequence AUTH_SESSAO | Ação |
|----------|---------------------|------|
| Greenfield (`ddl/002`) | `SQ_AUTH_SESSAO` | SSOT — JPA correto |
| Brownfield (`migrations/V003`) | `SQ_AUTH_SESSAO_COD_SESSAO` | Validar Oracle real; renomear ou synonym se necessário |
| `database/security/OPERATIONS.md` | Cita nome legado | Atualizar na Etapa 5/6 |

**Recomendação:** Em ambientes brownfield, executar query de inspeção:

```sql
SELECT sequence_name FROM all_sequences
 WHERE sequence_owner = 'UNMPORTCOM'
   AND sequence_name LIKE '%AUTH_SESSAO%';
```

---

## 6. Pendências não bloqueantes

| ID | Item | Categoria | Ação |
|----|------|-----------|------|
| P-01 | `specs/features/singular/api.md` — `unimedCode` como String | Contrato | Atualizar para Integer (1–999) |
| P-02 | `application.yaml` — `access-token-expiration` / `refresh-token-expiration` não bindam | Config | Remover chaves mortas na Etapa 6 |
| P-03 | `ONBOARDING_SOLICITACAO` no baseline sem uso TO-BE | Domínio | Documentado em `database/model/05-decisions-and-risks.md` |

---

## 7. Validação recomendada (runtime)

Quando Oracle estiver disponível:

```bash
cd backend && mvn test -Dtest=SchemaOracleAuditTest
```

Testes de integração exigem `SPRING_DATASOURCE_URL` com `UNMPORTCOM_APP`.

---

## 8. Conclusão Etapa 4 (parcial)

```text
ETAPA 4 — CONCLUÍDA (escopo entidades implementadas)
```

Reconciliação Oracle×DDL×JPA das 6 entidades em produção: **sem divergências críticas**. Próximo passo: validação runtime em Oracle real (Etapa 5) e evolução JPA conforme novas features.
