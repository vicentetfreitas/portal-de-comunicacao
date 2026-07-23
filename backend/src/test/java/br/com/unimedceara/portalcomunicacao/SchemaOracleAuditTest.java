package br.com.unimedceara.portalcomunicacao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Auditoria pontual JPA vs Oracle (DEC-DB-023). Somente leitura; não altera schema.
 */
@SpringBootTest(
        classes = DataSourceAutoConfiguration.class,
        properties = {
                "spring.autoconfigure.exclude="
                        + "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,"
                        + "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration"
        })
@Import(DataSourceAutoConfiguration.class)
@ActiveProfiles("test")
class SchemaOracleAuditTest {

    private static final String OWNER = "UNMPORTCOM"; // schema lógico JPA; metadado via USER_* views da conexão de teste

    @Autowired
    private DataSource dataSource;

    record JpaMapping(
            String table,
            String entity,
            String attribute,
            String column,
            String javaType,
            Integer length,
            Boolean nullable,
            boolean jdbcChar,
            boolean lob,
            boolean joinFk) {}

    record Divergence(
            String table,
            String column,
            String entity,
            String attribute,
            String category,
            String description) {}

    @Test
    void catalogAllDivergences() throws Exception {
        List<JpaMapping> mappings = jpaMappings();
        Map<String, Map<String, OracleColumn>> oracle = loadOracleColumns();
        Map<String, Integer> categoryCounts = new TreeMap<>();
        Map<String, Integer> entityCounts = new TreeMap<>();
        List<Divergence> divergences = new ArrayList<>();

        for (JpaMapping m : mappings) {
            Map<String, OracleColumn> tableCols = oracle.get(m.table());
            if (tableCols == null) {
                divergences.add(new Divergence(
                        m.table(),
                        m.column(),
                        m.entity(),
                        m.attribute(),
                        "A",
                        "Tabela ausente no Oracle"));
                continue;
            }
            OracleColumn col = tableCols.get(m.column());
            if (col == null) {
                divergences.add(new Divergence(
                        m.table(),
                        m.column(),
                        m.entity(),
                        m.attribute(),
                        "A",
                        "Coluna mapeada no JPA ausente no Oracle"));
                continue;
            }
            classifyType(m, col).ifPresent(desc -> divergences.add(new Divergence(
                    m.table(), m.column(), m.entity(), m.attribute(), desc.category(), desc.text())));
            if (m.nullable() != null && m.nullable() != col.nullable()) {
                divergences.add(new Divergence(
                        m.table(),
                        m.column(),
                        m.entity(),
                        m.attribute(),
                        "E",
                        "JPA nullable=" + m.nullable() + ", Oracle nullable=" + col.nullable()));
            }
            if (m.joinFk() && !col.hasForeignKey()) {
                divergences.add(new Divergence(
                        m.table(),
                        m.column(),
                        m.entity(),
                        m.attribute(),
                        "G",
                        "FK ausente no Oracle para @JoinColumn"));
            }
        }

        for (SequenceRef seq : sequences()) {
            if (!oracleSequenceExists(seq.name())) {
                divergences.add(new Divergence("", "", seq.entity(), "", "F", "Sequence " + seq.name() + " ausente no Oracle"));
            }
        }

        for (Divergence d : divergences) {
            categoryCounts.merge(d.category(), 1, Integer::sum);
            if (!d.entity().isBlank()) {
                entityCounts.merge(d.entity(), 1, Integer::sum);
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("table\tcolumn\tentity\tattribute\tcategory\tdescription\n");
        for (Divergence d : divergences) {
            report.append(d.table())
                    .append('\t')
                    .append(d.column())
                    .append('\t')
                    .append(d.entity())
                    .append('\t')
                    .append(d.attribute())
                    .append('\t')
                    .append(d.category())
                    .append('\t')
                    .append(d.description())
                    .append('\n');
        }
        report.append("\n--- MATRIX CATEGORY ---\n");
        for (char c : "ABCDEFG".toCharArray()) {
            report.append(c).append('\t').append(categoryCounts.getOrDefault(String.valueOf(c), 0)).append('\n');
        }
        report.append("\n--- MATRIX ENTITY ---\n");
        entityCounts.forEach((entity, count) -> report.append(entity).append('\t').append(count).append('\n'));

        Path out = Path.of("runtime/reports/schema-oracle-audit.txt");
        Files.createDirectories(out.getParent());
        Files.writeString(out, report.toString());
        System.out.println(report);
    }

    private record TypeFinding(String category, String text) {}

    private static java.util.Optional<TypeFinding> classifyType(JpaMapping m, OracleColumn col) {
        if (m.lob()) {
            if (!"CLOB".equals(col.dataType())) {
                return java.util.Optional.of(new TypeFinding("B", "JPA @Lob/CLOB vs Oracle " + col.dataType()));
            }
            return java.util.Optional.empty();
        }
        if (m.jdbcChar()) {
            if (!"CHAR".equals(col.dataType())) {
                return java.util.Optional.of(new TypeFinding("B", "JPA @JdbcTypeCode(CHAR) vs Oracle " + col.dataType()));
            }
            return java.util.Optional.empty();
        }
        if ("Long".equals(m.javaType()) || "long".equals(m.javaType())) {
            if (!"NUMBER".equals(col.dataType())) {
                return java.util.Optional.of(new TypeFinding("B", "JPA Long vs Oracle " + col.dataType()));
            }
            if (col.precision() != null && col.precision() != 19) {
                return java.util.Optional.of(new TypeFinding(
                        "C", "Oracle NUMBER(" + col.precision() + ") vs JPA NUMBER(19)"));
            }
            return java.util.Optional.empty();
        }
        if ("Integer".equals(m.javaType()) || "int".equals(m.javaType())) {
            if (!"NUMBER".equals(col.dataType())) {
                return java.util.Optional.of(new TypeFinding("B", "JPA Integer vs Oracle " + col.dataType()));
            }
            if (m.length() != null && col.precision() != null && !m.length().equals(col.precision())) {
                return java.util.Optional.of(new TypeFinding(
                        "C", "Oracle NUMBER(" + col.precision() + ") vs JPA NUMBER(" + m.length() + ")"));
            }
            return java.util.Optional.empty();
        }
        if ("String".equals(m.javaType())) {
            if ("NUMBER".equals(col.dataType())) {
                return java.util.Optional.of(new TypeFinding(
                        "B",
                        "JPA String mapeado como VARCHAR2(" + m.length() + ") vs Oracle NUMBER(" + col.precision() + ")"));
            }
            if (!"VARCHAR2".equals(col.dataType()) && !"NVARCHAR2".equals(col.dataType())) {
                return java.util.Optional.of(new TypeFinding("B", "JPA String vs Oracle " + col.dataType()));
            }
            if (m.length() != null && col.charLength() != null && !m.length().equals(col.charLength())) {
                return java.util.Optional.of(new TypeFinding(
                        "C", "Oracle VARCHAR2(" + col.charLength() + ") vs JPA length " + m.length()));
            }
            return java.util.Optional.empty();
        }
        if (m.javaType().contains("Instant")) {
            if (!col.dataType().startsWith("TIMESTAMP")) {
                return java.util.Optional.of(new TypeFinding("B", "JPA Instant vs Oracle " + col.dataType()));
            }
            return java.util.Optional.empty();
        }
        return java.util.Optional.empty();
    }

    private record OracleColumn(
            String dataType, Integer precision, Integer scale, Integer charLength, boolean nullable, boolean hasForeignKey) {}

    private Map<String, Map<String, OracleColumn>> loadOracleColumns() throws Exception {
        String sql =
                """
                SELECT c.table_name, c.column_name, c.data_type, c.data_precision, c.data_scale, c.char_length, c.nullable,
                       CASE WHEN fk.column_name IS NOT NULL THEN 1 ELSE 0 END AS has_fk
                FROM all_tab_columns c
                LEFT JOIN (
                    SELECT cc.table_name, cc.column_name
                    FROM all_constraints con
                    JOIN all_cons_columns cc ON con.owner = cc.owner AND con.constraint_name = cc.constraint_name
                    WHERE con.owner = ? AND con.constraint_type = 'R'
                ) fk ON c.table_name = fk.table_name AND c.column_name = fk.column_name
                WHERE c.owner = ?
                  AND c.table_name IN ('FEDERACAO','SINGULAR','AREA','EQUIPE','COLABORADOR','AUTH_SESSAO')
                ORDER BY c.table_name, c.column_id
                """;
        Map<String, Map<String, OracleColumn>> out = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, OWNER);
            ps.setString(2, OWNER);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String table = rs.getString(1);
                    String column = rs.getString(2);
                    out.computeIfAbsent(table, k -> new LinkedHashMap<>())
                            .put(
                                    column,
                                    new OracleColumn(
                                            rs.getString(3),
                                            rs.getObject(4) == null ? null : rs.getInt(4),
                                            rs.getObject(5) == null ? null : rs.getInt(5),
                                            rs.getObject(6) == null ? null : rs.getInt(6),
                                            "Y".equals(rs.getString(7)),
                                            rs.getInt(8) == 1));
                }
            }
        }
        return out;
    }

    private boolean oracleSequenceExists(String name) throws Exception {
        String sql = "SELECT 1 FROM all_sequences WHERE sequence_owner = ? AND sequence_name = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, OWNER);
            ps.setString(2, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private record SequenceRef(String entity, String name) {}

    private static List<SequenceRef> sequences() {
        return List.of(
                new SequenceRef("FederacaoEntity", "SQ_FEDERACAO_COD_FEDERACAO"),
                new SequenceRef("SingularEntity", "SQ_SINGULAR_COD_SINGULAR"),
                new SequenceRef("AreaEntity", "SQ_AREA_COD_AREA"),
                new SequenceRef("EquipeEntity", "SQ_EQUIPE_COD_EQUIPE"),
                new SequenceRef("ColaboradorEntity", "SQ_COLABORADOR"),
                new SequenceRef("AuthSessaoEntity", "SQ_AUTH_SESSAO"));
    }

  private static List<JpaMapping> jpaMappings() {
        return List.of(
                // FederacaoEntity
                map("FEDERACAO", "FederacaoEntity", "id", "COD_FEDERACAO", "Long", null, false, false, false, false),
                map("FEDERACAO", "FederacaoEntity", "nome", "NOM_FEDERACAO", "String", 200, false, false, false, false),
                map("FEDERACAO", "FederacaoEntity", "sigla", "SIG_FEDERACAO", "String", 30, false, false, false, false),
                map("FEDERACAO", "FederacaoEntity", "codigoUnimed", "COD_UNIMED", "Integer", 3, false, false, false, false),
                map("FEDERACAO", "FederacaoEntity", "registroAns", "NUM_REGISTRO_ANS", "String", 20, false, false, false, false),
                map("FEDERACAO", "FederacaoEntity", "urlSite", "URL_SITE", "String", 300, null, false, false, false),
                map("FEDERACAO", "FederacaoEntity", "descricao", "DSC_FEDERACAO", "String", null, null, false, true, false),
                map("FEDERACAO", "FederacaoEntity", "ativo", "FLG_ATIVO", "String", 1, false, true, false, false),
                map("FEDERACAO", "FederacaoEntity", "dataCadastro", "DAT_CADASTRO", "Instant", null, false, false, false, false),
                map("FEDERACAO", "FederacaoEntity", "dataAtualizacao", "DAT_ATUALIZACAO", "Instant", null, null, false, false, false),
                // SingularEntity
                map("SINGULAR", "SingularEntity", "id", "COD_SINGULAR", "Long", null, false, false, false, false),
                map("SINGULAR", "SingularEntity", "federacaoId", "COD_FEDERACAO", "Long", null, false, false, false, false),
                map("SINGULAR", "SingularEntity", "nome", "NOM_SINGULAR", "String", 200, false, false, false, false),
                map("SINGULAR", "SingularEntity", "sigla", "SIG_SINGULAR", "String", 30, false, false, false, false),
                map("SINGULAR", "SingularEntity", "codigoUnimed", "COD_UNIMED", "Integer", 3, false, false, false, false),
                map("SINGULAR", "SingularEntity", "registroAns", "NUM_REGISTRO_ANS", "String", 20, false, false, false, false),
                map("SINGULAR", "SingularEntity", "ativo", "FLG_ATIVO", "String", 1, false, true, false, false),
                map("SINGULAR", "SingularEntity", "dataCadastro", "DAT_CADASTRO", "Instant", null, false, false, false, false),
                map("SINGULAR", "SingularEntity", "dataAtualizacao", "DAT_ATUALIZACAO", "Instant", null, null, false, false, false),
                // AreaEntity
                map("AREA", "AreaEntity", "id", "COD_AREA", "Long", null, false, false, false, false),
                map("AREA", "AreaEntity", "singularId", "COD_SINGULAR", "Long", null, null, false, false, false),
                map("AREA", "AreaEntity", "nome", "NOM_AREA", "String", 200, false, false, false, false),
                map("AREA", "AreaEntity", "sigla", "SIG_AREA", "String", 30, null, false, false, false),
                map("AREA", "AreaEntity", "descricao", "DSC_AREA", "String", null, null, false, true, false),
                map("AREA", "AreaEntity", "gestorId", "COD_GESTOR", "Long", null, null, false, false, false),
                map("AREA", "AreaEntity", "ativo", "FLG_ATIVO", "String", 1, false, true, false, false),
                map("AREA", "AreaEntity", "dataCadastro", "DAT_CADASTRO", "Instant", null, false, false, false, false),
                map("AREA", "AreaEntity", "dataAtualizacao", "DAT_ATUALIZACAO", "Instant", null, null, false, false, false),
                // EquipeEntity
                map("EQUIPE", "EquipeEntity", "id", "COD_EQUIPE", "Long", null, false, false, false, false),
                map("EQUIPE", "EquipeEntity", "areaId", "COD_AREA", "Long", null, false, false, false, false),
                map("EQUIPE", "EquipeEntity", "nome", "NOM_EQUIPE", "String", 200, false, false, false, false),
                map("EQUIPE", "EquipeEntity", "descricao", "DSC_EQUIPE", "String", null, null, false, true, false),
                map("EQUIPE", "EquipeEntity", "liderId", "COD_LIDER", "Long", null, null, false, false, false),
                map("EQUIPE", "EquipeEntity", "ativo", "FLG_ATIVO", "String", 1, false, true, false, false),
                map("EQUIPE", "EquipeEntity", "dataCadastro", "DAT_CADASTRO", "Instant", null, false, false, false, false),
                map("EQUIPE", "EquipeEntity", "dataAtualizacao", "DAT_ATUALIZACAO", "Instant", null, null, false, false, false),
                // ColaboradorEntity
                map("COLABORADOR", "ColaboradorEntity", "id", "COD_COLABORADOR", "Long", null, false, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "federacaoId", "COD_FEDERACAO", "Long", null, false, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "singularId", "COD_SINGULAR", "Long", null, null, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "areaId", "COD_AREA", "Long", null, null, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "equipeId", "COD_EQUIPE", "Long", null, null, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "gestorId", "COD_GESTOR", "Long", null, null, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "nome", "NOM_COLABORADOR", "String", 255, false, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "email", "DES_EMAIL", "String", 255, false, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "zimbraId", "ID_ZIMBRA", "String", 255, false, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "biografia", "DES_BIOGRAFIA", "String", 4000, null, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "ativo", "FLG_ATIVO", "String", 1, false, true, false, false),
                map("COLABORADOR", "ColaboradorEntity", "dataNascimento", "DAT_NASCIMENTO", "Instant", null, null, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "dataContratacao", "DAT_CONTRATACAO", "Instant", null, null, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "dataUltimoAcesso", "DAT_ULTIMO_ACESSO", "Instant", null, null, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "dataCadastro", "DAT_CADASTRO", "Instant", null, false, false, false, false),
                map("COLABORADOR", "ColaboradorEntity", "dataAtualizacao", "DAT_ATUALIZACAO", "Instant", null, null, false, false, false),
                // AuthSessaoEntity
                map("AUTH_SESSAO", "AuthSessaoEntity", "id", "COD_SESSAO", "Long", null, false, false, false, false),
                map("AUTH_SESSAO", "AuthSessaoEntity", "sessionId", "ID_SESSAO", "String", 36, false, false, false, false),
                map("AUTH_SESSAO", "AuthSessaoEntity", "colaborador", "COD_COLABORADOR", "ColaboradorEntity", null, false, false, false, true),
                map("AUTH_SESSAO", "AuthSessaoEntity", "refreshTokenHash", "HASH_REFRESH_TOKEN", "String", 255, false, false, false, false),
                map("AUTH_SESSAO", "AuthSessaoEntity", "dispositivo", "DES_DISPOSITIVO", "String", 255, null, false, false, false),
                map("AUTH_SESSAO", "AuthSessaoEntity", "rememberMe", "FLG_REMEMBER_ME", "String", 1, false, true, false, false),
                map("AUTH_SESSAO", "AuthSessaoEntity", "dataCriacao", "DAT_CRIACAO", "Instant", null, false, false, false, false),
                map("AUTH_SESSAO", "AuthSessaoEntity", "dataExpiracao", "DAT_EXPIRACAO", "Instant", null, false, false, false, false),
                map("AUTH_SESSAO", "AuthSessaoEntity", "revogada", "FLG_REVOGADA", "String", 1, false, true, false, false),
                map("AUTH_SESSAO", "AuthSessaoEntity", "dataRevogacao", "DAT_REVOGACAO", "Instant", null, null, false, false, false));
    }

    private static JpaMapping map(
            String table,
            String entity,
            String attr,
            String column,
            String java,
            Integer length,
            Boolean nullable,
            boolean jdbcChar,
            boolean lob,
            boolean joinFk) {
        return new JpaMapping(table, entity, attr, column, java, length, nullable, jdbcChar, lob, joinFk);
    }
}
