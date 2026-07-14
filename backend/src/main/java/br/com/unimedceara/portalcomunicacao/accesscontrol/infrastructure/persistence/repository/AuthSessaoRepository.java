package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.AuthSessaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA de sessões de autenticação.
 */
public interface AuthSessaoRepository extends JpaRepository<AuthSessaoEntity, Long> {

    Optional<AuthSessaoEntity> findByRefreshTokenHash(String refreshTokenHash);

    Optional<AuthSessaoEntity> findBySessionId(String sessionId);

    List<AuthSessaoEntity> findByColaborador_IdAndRevogadaAndDataExpiracaoAfterOrderByDataCriacaoAsc(
            long colaboradorId,
            String revogada,
            Instant now);
}
