package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA de colaboradores.
 */
public interface ColaboradorRepository extends JpaRepository<ColaboradorEntity, Long> {

    Optional<ColaboradorEntity> findByEmailIgnoreCase(String email);

    Optional<ColaboradorEntity> findByZimbraId(String zimbraId);
}
