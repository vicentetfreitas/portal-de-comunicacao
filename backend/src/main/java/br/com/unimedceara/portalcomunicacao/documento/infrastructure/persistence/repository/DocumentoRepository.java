package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA de documentos (FT-DOCUMENTO).
 */
public interface DocumentoRepository extends JpaRepository<DocumentoEntity, Long> {

    /**
     * Documentos de uma pasta, excluindo {@code EXPIRADO} (RF-DOCUMENTO-004).
     */
    List<DocumentoEntity> findByPastaIdAndStatusNotOrderByTituloAsc(Long pastaId, String status);

    /**
     * Documento por id, excluindo {@code EXPIRADO} — usado quando o chamador já sabe que
     * expirado deve ser tratado como inexistente (RF-DOCUMENTO-004).
     */
    Optional<DocumentoEntity> findByIdAndStatusNot(Long id, String status);
}
