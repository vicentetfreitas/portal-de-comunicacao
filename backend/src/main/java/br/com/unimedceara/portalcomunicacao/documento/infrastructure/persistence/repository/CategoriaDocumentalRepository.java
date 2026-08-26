package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.CategoriaDocumentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA de categorias documentais — ver {@link CategoriaDocumentalEntity}.
 */
public interface CategoriaDocumentalRepository extends JpaRepository<CategoriaDocumentalEntity, Long> {
}
