# Security Readiness Review — v1.0.0

| Campo | Valor |
|--------|--------|
| ID | SEC-READINESS-001 |
| Tipo | Revisão de segurança para publicação |
| Escopo | Repositório completo (verificação somente leitura) |
| Data | 2026-07-14 |
| Versão alvo | `v1.0.0` |
| Executor | Security Readiness Review |
| Alterações realizadas | **Nenhuma** (conforme restrições) |

---

# 1. Resumo executivo

Foi realizada varredura de segurança orientada à publicação do repositório em remoto e criação da tag `v1.0.0`. A revisão abrangeu arquivos de configuração, código-fonte, Docker, artefatos de build, logging, `.gitignore`, dependências, documentação e externalização de credenciais.

**Classificação:** **APPROVED WITH OBSERVATIONS**

O repositório versionado **não contém credenciais reais** nos arquivos rastreados analisados (`git grep` em arquivos versionados; `.env.example` e `application*.yaml` com placeholders/variáveis de ambiente). A arquitetura de configuração externaliza segredos via variáveis de ambiente, alinhada à documentação do projeto.

Foram identificadas **observações relevantes** que não bloqueiam a publicação do código-fonte, mas exigem ações operacionais antes ou imediatamente após o release:

1. Arquivo `.env` **presente no workspace local** com credenciais reais (Oracle e JWT) — **não versionado**, porém risco operacional se publicado o diretório de trabalho sem `.gitignore` efetivo.
2. `application-local.yaml` **versionado** apesar de constar no `.gitignore` — inconsistência de governança Git.
3. Artefato JAR de build inclui `application-local.yaml` no classpath empacotado.
4. `docker-compose.yml` define senha padrão fraca (`portal`) para ambiente local.

**Não foram encontrados** certificados privados, chaves SSH, API keys reais ou senhas hardcoded em arquivos versionados de código de produção.

---

# 2. Checklist

| # | Categoria | Resultado | Observação resumida |
|---|-----------|-----------|---------------------|
| 1 | Arquivos de configuração | **WARNING** | Versionados sem segredos reais; `.env` local com credenciais |
| 2 | Segredos no código-fonte | **PASS** | Sem credenciais reais em `src/main` |
| 3 | Configuração Docker | **WARNING** | Senhas padrão locais; Dockerfile referenciado ausente |
| 4 | Build / artefatos | **WARNING** | JAR empacota `application-local.yaml` |
| 5 | Logs | **PASS** | Filtros de request/auditoria sem tokens ou senhas |
| 6 | Git / `.gitignore` | **WARNING** | `.env` ignorado corretamente; `application-local.yaml` ainda rastreado |
| 7 | Dependências | **PASS** | Sem credenciais em dependências locais |
| 8 | Documentação | **PASS** | Exemplos com placeholders; sem credenciais reais |
| 9 | Configuração por ambiente | **PASS** | Segredos via `${...}` e `.env` externo |

---

# 3. Achados

## SEC-001 — Credenciais reais em `.env` local (não versionado)

| Campo | Valor |
|-------|--------|
| Severidade | **Alta** (operacional) |
| Localização | `.env` (raiz do repositório, workspace local) |
| Versionado | **Não** — `git ls-files` lista apenas `.env.example` |
| Descrição | O arquivo `.env` local contém URL JDBC de ambiente interno (`ractst-scan.unimedce.com.br`), usuário Oracle (`UNMPORTCOM_APP`), senha de banco e JWT secret com valor real. |
| Impacto | Se o diretório for copiado, compactado ou publicado fora do Git sem exclusão do `.env`, credenciais serão expostas. O `.gitignore` mitiga push acidental ao remoto, mas não protege cópias locais. |
| Recomendação | Antes da tag `v1.0.0`: confirmar que `.env` nunca foi commitado; rotacionar credenciais se houver dúvida; publicar apenas o repositório Git (não snapshots de pasta). Manter `.env` exclusivamente local. |

---

## SEC-002 — `application-local.yaml` versionado apesar do `.gitignore`

| Campo | Valor |
|-------|--------|
| Severidade | **Média** |
| Localização | `backend/src/main/resources/application-local.yaml` |
| Versionado | **Sim** — presente em `git ls-files` |
| Descrição | O `.gitignore` declara `**/application-local.yaml` como proibido, porém o arquivo permanece rastreado (comportamento Git: ignore não remove arquivos já commitados). Conteúdo atual **não contém** segredos hardcoded — apenas referências `${APPLICATION_SECURITY_JWT_SECRET:}` e import de `.env`. |
| Impacto | Risco de commit futuro de segredos no perfil local; confusão de governança; perfil `local` pode ser ativado inadvertidamente em ambientes não locais. |
| Recomendação | Remover do índice Git (`git rm --cached`) em manutenção futura — **fora do escopo desta revisão**. Validar que o conteúdo versionado permanece sem valores reais antes do push. |

---

## SEC-003 — JAR empacota `application-local.yaml`

| Campo | Valor |
|-------|--------|
| Severidade | **Média** |
| Localização | `backend/target/portal-comunicacao-0.0.1-SNAPSHOT.jar` → `BOOT-INF/classes/application-local.yaml` |
| Descrição | O build Maven inclui `application-local.yaml` no artefato executável. O arquivo não contém segredos, mas habilita perfil local e import opcional de `../.env`. |
| Impacto | Distribuição do JAR sem exclusão de perfis locais pode facilitar ativação de configuração de desenvolvimento em runtime. |
| Recomendação | Para publicação de artefatos: usar perfis `dev`/`hml`/`prod` em deploy; considerar exclusão de `application-local.yaml` do JAR de release em evolução futura do build. |

---

## SEC-004 — Senhas padrão em `docker-compose.yml`

| Campo | Valor |
|-------|--------|
| Severidade | **Baixa** |
| Localização | `docker-compose.yml` (linhas `POSTGRES_PASSWORD`, `DB_PASSWORD`) |
| Descrição | Valores default `${DB_PASSWORD:-portal}` e usuário `portal` para Postgres local. |
| Impacto | Aceitável para desenvolvimento local; inaceitável se o compose for usado em ambiente exposto sem sobrescrever variáveis. |
| Recomendação | Documentar que defaults são apenas para dev local; exigir variáveis em ambientes compartilhados. |

---

## SEC-005 — JWT secret de teste em arquivos versionados

| Campo | Valor |
|-------|--------|
| Severidade | **Baixa** |
| Localização | `backend/src/test/resources/application-test.yaml`, `pf-*-test.properties` |
| Descrição | Valor fixo `test-jwt-secret-32-characters-minimum` em perfil de teste. |
| Impacto | Nenhum em produção se perfil `test` não for ativado em deploy. Valor claramente fictício. |
| Recomendação | Manter segregação de perfis; não ativar `test` fora de CI/local. |

---

## SEC-006 — `show-sql: true` no perfil local

| Campo | Valor |
|-------|--------|
| Severidade | **Baixa** |
| Localização | `backend/src/main/resources/application-local.yaml` |
| Descrição | Perfil `local` habilita `show-sql` e `format_sql`. |
| Impacto | Em desenvolvimento, logs SQL podem expor dados de negócio em texto claro. Não afeta produção se perfil `local` não for usado. |
| Recomendação | Restringir perfil `local` a máquinas de desenvolvimento. |

---

## SEC-007 — Dockerfile referenciado ausente

| Campo | Valor |
|-------|--------|
| Severidade | **Informativa** |
| Localização | `docker-compose.yml` → `backend/Dockerfile` |
| Descrição | `docker-compose` referencia `dockerfile: Dockerfile` em `./backend`, mas o arquivo não foi encontrado no repositório. |
| Impacto | Não é exposição de segredo; impede build containerizado conforme compose documentado. |
| Recomendação | Tratar em atividade de infraestrutura separada. |

---

# 4. Verificações detalhadas

## 4.1 Arquivos de configuração

| Arquivo | Versionado | Contém segredo real? | Avaliação |
|---------|------------|----------------------|-----------|
| `.env` | Não | **Sim** (workspace local) | Fora do Git — risco operacional |
| `.env.example` | Sim | Não (placeholders vazios) | ✅ |
| `application.yaml` | Sim | Não (`${SPRING_DATASOURCE_*}`) | ✅ |
| `application-local.yaml` | Sim | Não (referências env) | ⚠️ SEC-002 |
| `application-dev.yaml` | Sim | Não | ✅ |
| `application-hml.yaml` | Sim | Não | ✅ |
| `application-prod.yaml` | Sim | Não | ✅ |
| `application-test.yaml` | Sim | JWT de teste fictício | ⚠️ SEC-005 |

## 4.2 Segredos no código-fonte (`src/main`)

Varredura por padrões `password`, `secret`, `jwt-secret`, `Bearer`, chaves privadas e hosts internos em código de produção:

- **Nenhuma credencial real** em constantes Java de `src/main`.
- `SecurityProperties` e `AuthProperties` recebem valores via binding externo.
- Constantes `ACCESS_TOKEN_COOKIE`, `REFRESH_TOKEN_COOKIE` são nomes de cookie, não valores secretos.

## 4.3 Docker

- `docker-compose.yml`: credenciais via variáveis de ambiente; defaults locais fracos (SEC-004).
- Nenhum `Dockerfile` encontrado no repositório (SEC-007).
- Sem `docker-compose.*.yml` adicionais por ambiente.

## 4.4 Build

- `backend/target/` ignorado pelo `.gitignore` — não versionado.
- JAR local contém `application-local.yaml` (SEC-003).
- `pom.xml` não empacota `.env` explicitamente.
- Sem certificados `.pem`/`.key` no projeto.

## 4.5 Logs

| Componente | Comportamento | Avaliação |
|------------|---------------|-----------|
| `RequestLoggingFilter` | Registra method, URI, status, duration, correlationId — **sem** headers sensíveis | ✅ |
| `RequestLoggingFilterTest` | Valida que `Authorization: Bearer ...` não é logado | ✅ |
| `AuthAuditService` | Registra IDs de colaborador/sessão — **sem** JWT, cookies ou senhas | ✅ |
| `backend/runtime/logs/application.log` | Amostra analisada — sem tokens/senhas | ✅ |

## 4.6 Git e `.gitignore`

| Item | Status |
|------|--------|
| `.env` listado no `.gitignore` | ✅ |
| `.env.*` ignorado exceto `.env.example` | ✅ |
| `**/application-local.yaml` no `.gitignore` | ⚠️ Arquivo ainda rastreado (SEC-002) |
| `**/target/`, `node_modules/`, logs, dumps | ✅ Ignorados |
| Certificados `*.pem`, `*.key` | ✅ Não rastreados |
| `git grep` em arquivos versionados | ✅ Sem `UnmP0rt` ou padrões de chave privada |

## 4.7 Dependências

- Dependências Maven resolvidas via repositórios públicos (Spring Boot, Oracle JDBC, H2 test).
- Sem JARs locais com credenciais embutidas identificados.
- Scripts locais (`backend/scripts/migrate-runtime-artifacts.sh`) movem logs de build — sem segredos.

## 4.8 Documentação

- `README.md`, `docs/implementation/`, `docs/technology/` utilizam placeholders (`<host>`, `${DB_PASSWORD}`, exemplos genéricos).
- **Nenhuma** ocorrência de hostname interno (`ractst-scan`) ou senhas reais em `docs/` ou `specs/`.
- E-mails corporativos em testes (`colaborador@unimedceara.com.br`) — identificadores de teste, não credenciais.

## 4.9 Configuração externa

O projeto **permite** configurar credenciais sem alterar código:

```yaml
# application.yaml (trecho representativo)
jwt-secret: ${APPLICATION_SECURITY_JWT_SECRET}
password: ${SPRING_DATASOURCE_PASSWORD}
```

Perfil `local` importa `optional:file:../.env[.properties]` — padrão adequado para desenvolvimento, desde que `.env` não seja versionado.

---

# 5. Observações

1. **Separação specs / código / secrets** está alinhada ao processo SDD — nenhum segredo encontrado em `specs/` ou `construction/`.
2. **FT-AUTH** registrou remoção histórica de credenciais de `application-local.yaml` no encerramento — estado atual do arquivo versionado está limpo de valores reais.
3. A revisão **não executou** secret scan em histórico Git completo com ferramentas dedicadas (ex.: gitleaks); `git grep` e busca por padrões não encontraram credenciais em arquivos rastreados atuais.
4. **Artefatos de runtime** (`backend/runtime/logs`, `reports`) existem localmente mas estão no `.gitignore` — adequado para publicação do repositório.
5. Publicação da **tag `v1.0.0`** refere-se ao código-fonte; publicação do **JAR** como artefato de release requer atenção adicional (SEC-003).

---

# 6. Conclusão

## O projeto está apto para receber a tag `v1.0.0` e ser publicado em um repositório remoto?

**Sim, com ressalvas operacionais** — classificação **APPROVED WITH OBSERVATIONS**.

### Justificativa

**A favor da publicação:**

- Arquivos versionados de configuração utilizam variáveis de ambiente ou placeholders.
- `.env` com credenciais reais **não está** no índice Git.
- Código de produção não contém senhas, API keys ou JWT secrets hardcoded.
- Logging de requisições e auditoria de autenticação evita exposição de tokens e headers sensíveis.
- `.gitignore` cobre adequadamente `.env`, `target/`, logs, dumps e artefatos de IDE.
- Documentação utiliza exemplos fictícios.

**Ressalvas antes ou no momento da publicação:**

1. **Confirmar** que `.env` nunca entrará no commit (validar com `git status` e secret scan no CI).
2. **Tratar** `application-local.yaml` rastreado (SEC-002) — conteúdo atual seguro, governança inadequada.
3. **Não publicar** o JAR de desenvolvimento como artefato oficial sem revisar perfis empacotados (SEC-003).
4. **Rotacionar** credenciais do `.env` local se houver qualquer chance de exposição prévia fora do Git.

### Bloqueadores para NOT APPROVED

Nenhum bloqueador foi identificado **nos arquivos versionados** do repositório. O achado de maior severidade (SEC-001) refere-se a arquivo local ignorado pelo Git — risco de processo, não de conteúdo já commitado.

---

# Referências da revisão

| Evidência | Método |
|-----------|--------|
| Conteúdo `.env` / `.env.example` | Leitura direta |
| Arquivos `application*.yaml` | Leitura direta |
| `git ls-files`, `git grep` | Análise de índice Git |
| Conteúdo JAR | `jar tf` no artefato local |
| Código de logging | `RequestLoggingFilter`, `AuthAuditService` |
| `.gitignore` | Leitura direta |
| `docker-compose.yml` | Leitura direta |

---

# Histórico

| Versão | Data | Descrição |
|--------|------|-----------|
| 1.0 | 2026-07-14 | Security Readiness Review pré-v1.0.0 |
