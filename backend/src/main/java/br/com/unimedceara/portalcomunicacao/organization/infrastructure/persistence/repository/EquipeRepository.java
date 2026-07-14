package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA mínimo de equipes.
 */
public interface EquipeRepository extends JpaRepository<EquipeEntity, Long> {

    boolean existsByAreaIdAndAtivo(Long areaId, String ativo);
}
