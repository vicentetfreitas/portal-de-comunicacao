package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA mínimo de singulares.
 */
public interface SingularRepository extends JpaRepository<SingularEntity, Long> {

    Optional<SingularEntity> findByIdAndAtivo(Long id, String ativo);
}
