package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoVersaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA de versões de documento (FT-DOCUMENTO).
 */
public interface DocumentoVersaoRepository extends JpaRepository<DocumentoVersaoEntity, Long> {

    Optional<DocumentoVersaoEntity> findByDocumentoIdAndVersaoAtual(Long documentoId, String versaoAtual);
}
