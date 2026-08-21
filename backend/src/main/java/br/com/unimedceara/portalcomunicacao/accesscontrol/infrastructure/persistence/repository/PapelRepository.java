package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA do catálogo de papéis ({@code PAPEL}).
 */
public interface PapelRepository extends JpaRepository<PapelEntity, Long> {
}
