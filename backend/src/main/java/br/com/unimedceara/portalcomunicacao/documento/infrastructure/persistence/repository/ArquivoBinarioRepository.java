package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.ArquivoBinarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA de binários de arquivo (FT-DOCUMENTO).
 */
public interface ArquivoBinarioRepository extends JpaRepository<ArquivoBinarioEntity, Long> {
}
