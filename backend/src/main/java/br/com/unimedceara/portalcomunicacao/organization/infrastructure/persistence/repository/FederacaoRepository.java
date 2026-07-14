package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA de federações organizacionais.
 */
public interface FederacaoRepository extends JpaRepository<FederacaoEntity, Long> {
}
