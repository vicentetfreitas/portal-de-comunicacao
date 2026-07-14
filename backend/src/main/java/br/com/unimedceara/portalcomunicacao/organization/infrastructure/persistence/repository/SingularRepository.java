package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositório JPA de singulares organizacionais.
 */
public interface SingularRepository extends JpaRepository<SingularEntity, Long> {

    Optional<SingularEntity> findByIdAndAtivo(Long id, String ativo);

    boolean existsBySiglaIgnoreCase(String sigla);

    boolean existsByCodigoUnimedIgnoreCase(String codigoUnimed);

    boolean existsBySiglaIgnoreCaseAndIdNot(String sigla, Long id);

    boolean existsByCodigoUnimedIgnoreCaseAndIdNot(String codigoUnimed, Long id);

    @Query("""
            SELECT s FROM SingularEntity s
            WHERE (:federacaoId IS NULL OR s.federacaoId = :federacaoId)
              AND (:ativo IS NULL OR s.ativo = :ativo)
              AND (:name IS NULL OR LOWER(s.nome) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:acronym IS NULL OR LOWER(s.sigla) LIKE LOWER(CONCAT('%', :acronym, '%')))
              AND (:codigoUnimed IS NULL OR LOWER(s.codigoUnimed) LIKE LOWER(CONCAT('%', :codigoUnimed, '%')))
            """)
    Page<SingularEntity> findByFilters(
            @Param("federacaoId") Long federacaoId,
            @Param("ativo") String ativo,
            @Param("name") String name,
            @Param("acronym") String acronym,
            @Param("codigoUnimed") String codigoUnimed,
            Pageable pageable);
}
