package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA de sessão de autenticação (AUTH_SESSAO).
 */
@Getter
@Setter
@Entity
@Table(name = "AUTH_SESSAO", schema = "UNMPORTCOM")
public class AuthSessaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_auth_sessao")
    @SequenceGenerator(name = "sq_auth_sessao", sequenceName = "SQ_AUTH_SESSAO_COD_SESSAO", allocationSize = 1)
    @Column(name = "COD_SESSAO", nullable = false)
    private Long id;

    @Column(name = "ID_SESSAO", nullable = false, length = 36, unique = true)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COD_COLABORADOR", nullable = false)
    private ColaboradorEntity colaborador;

    @Column(name = "HASH_REFRESH_TOKEN", nullable = false, length = 255, unique = true)
    private String refreshTokenHash;

    @Column(name = "DES_DISPOSITIVO", length = 255)
    private String dispositivo;

    @Column(name = "FLG_REMEMBER_ME", nullable = false, length = 1)
    private String rememberMe;

    @Column(name = "DAT_CRIACAO", nullable = false)
    private Instant dataCriacao;

    @Column(name = "DAT_EXPIRACAO", nullable = false)
    private Instant dataExpiracao;

    @Column(name = "FLG_REVOGADA", nullable = false, length = 1)
    private String revogada;

    @Column(name = "DAT_REVOGACAO")
    private Instant dataRevogacao;

    public boolean isRevogada() {
        return "S".equalsIgnoreCase(revogada);
    }

    public boolean isRememberMe() {
        return "S".equalsIgnoreCase(rememberMe);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(dataExpiracao);
    }

    public boolean isActive() {
        return !isRevogada() && !isExpired();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        AuthSessaoEntity that = (AuthSessaoEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
