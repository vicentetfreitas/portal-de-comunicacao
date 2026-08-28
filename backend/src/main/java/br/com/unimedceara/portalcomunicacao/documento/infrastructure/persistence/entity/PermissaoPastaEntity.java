package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA de permissão de pasta (FT-DOCUMENTO) — mecanismo real de visibilidade
 * documental (RF-DOCUMENTO-003), substituindo a proposta inicial de FK direta a Área.
 * {@code tipoDestinatario} ∈ (FEDERACAO, SINGULAR, AREA, EQUIPE, COLABORADOR);
 * {@code tipoAcesso} ∈ (LEITURA, DOWNLOAD, EDICAO, ADMINISTRACAO) — ver CHECK constraints
 * em {@code database/ddl/004-create-constraints.sql}. Esta Feature só usa os níveis
 * FEDERACAO/SINGULAR/AREA/EQUIPE e os acessos LEITURA/DOWNLOAD (ver
 * {@code specification.md} § Escopo — COLABORADOR e herança ficam fora desta entrega).
 */
@Getter
@Setter
@Entity
@Table(name = "PERMISSAO_PASTA", schema = "UNMPORTCOM")
public class PermissaoPastaEntity {

    public static final String DESTINATARIO_FEDERACAO = "FEDERACAO";
    public static final String DESTINATARIO_SINGULAR = "SINGULAR";
    public static final String DESTINATARIO_AREA = "AREA";
    public static final String DESTINATARIO_EQUIPE = "EQUIPE";
    public static final String DESTINATARIO_COLABORADOR = "COLABORADOR";

    public static final String ACESSO_LEITURA = "LEITURA";
    public static final String ACESSO_DOWNLOAD = "DOWNLOAD";

    @Id
    @Column(name = "COD_PERMISSAO_PASTA", nullable = false)
    private Long id;

    @Column(name = "COD_PASTA", nullable = false)
    private Long pastaId;

    @Column(name = "TIP_DESTINATARIO", nullable = false, length = 30)
    private String tipoDestinatario;

    @Column(name = "COD_DESTINATARIO", nullable = false)
    private Long codigoDestinatario;

    @Column(name = "TIP_ACESSO", nullable = false, length = 30)
    private String tipoAcesso;

    @Column(name = "DAT_CADASTRO", nullable = false)
    private Instant dataCadastro;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        PermissaoPastaEntity that = (PermissaoPastaEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
