package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositório JPA de áreas organizacionais.
 */
public interface AreaRepository extends JpaRepository<AreaEntity, Long> {

    boolean existsBySingularIdAndNomeIgnoreCaseAndAtivo(Long singularId, String nome, String ativo);

    boolean existsBySingularIdAndNomeIgnoreCaseAndAtivoAndIdNot(
            Long singularId, String nome, String ativo, Long id);

    boolean existsByParentAreaIdAndAtivo(Long parentAreaId, String ativo);

    boolean existsBySingularIdAndAtivo(Long singularId, String ativo);

    @Query("""
            SELECT a FROM AreaEntity a
            WHERE (:singularId IS NULL OR a.singularId = :singularId)
              AND (:ativo IS NULL OR a.ativo = :ativo)
              AND (:name IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:acronym IS NULL OR LOWER(a.sigla) LIKE LOWER(CONCAT('%', :acronym, '%')))
            """)
    Page<AreaEntity> findByFilters(
            @Param("singularId") Long singularId,
            @Param("ativo") String ativo,
            @Param("name") String name,
            @Param("acronym") String acronym,
            Pageable pageable);

    Optional<AreaEntity> findByIdAndAtivo(Long id, String ativo);
}
